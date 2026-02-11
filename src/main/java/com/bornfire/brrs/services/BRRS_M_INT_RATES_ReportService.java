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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_INT_RATES_Summary_Repo;

import com.bornfire.brrs.entities.M_INT_RATES_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_Summary_Entity;

@Component
@Service

public class BRRS_M_INT_RATES_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_INT_RATES_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	BRRS_M_INT_RATES_RESUB_Summary_Repo M_INT_RATES_resub_summary_repo;
	
@Autowired
	BRRS_M_INT_RATES_RESUB_Detail_Repo M_INT_RATES_resub_detail_repo;
	
	

	@Autowired
	BRRS_M_INT_RATES_Detail_Repo M_INT_RATES_Detail_Repo;

	@Autowired
	BRRS_M_INT_RATES_Archival_Detail_Repo  M_INT_RATES_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_INT_RATES_Summary_Repo M_INT_RATES_Summary_Repo;

	@Autowired
	BRRS_M_INT_RATES_Archival_Summary_Repo M_INT_RATES_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_INTRATESView(
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

	            List<M_INT_RATES_Archival_Summary_Entity> T1Master =
	                    M_INT_RATES_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_INT_RATES_RESUB_Summary_Entity> T1Master =
	                    M_INT_RATES_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_INT_RATES_Summary_Entity> T1Master =
	                    M_INT_RATES_Summary_Repo
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

	                List<M_INT_RATES_Archival_Detail_Entity> T1Master =
	                        M_INT_RATES_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ---------------------------------------------------------------------------------------------------------------------------------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_INT_RATES_RESUB_Detail_Entity> T1Master =
	                        M_INT_RATES_resub_detail_repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_INT_RATES_Detail_Entity> T1Master =
		                        M_INT_RATES_Detail_Repo
		                                .getdatabydateList(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_INT_RATES");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}

	
	
	
	@Transactional
	public void updateReport(M_INT_RATES_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     M_INT_RATES_Summary_Entity existingSummary =
	            M_INT_RATES_Summary_Repo.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_INT_RATES_Detail_Entity existingDetail =
	            M_INT_RATES_Detail_Repo.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_INT_RATES_Detail_Entity d = new   M_INT_RATES_Detail_Entity();
	                        d.setReport_date(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 11; i <= 42; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "LENDING", "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME"};

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              M_INT_RATES_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                              M_INT_RATES_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_INT_RATES_Detail_Entity.class.getMethod(
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
	    M_INT_RATES_Summary_Repo.save(existingSummary);
	    M_INT_RATES_Detail_Repo.save(existingDetail);
	}

	@Transactional
	public void updateResubReport(M_INT_RATES_RESUB_Summary_Entity updatedEntity) {

	    Date reportDate = updatedEntity.getReport_date();

	    // ----------------------------------------------------
	    // GET CURRENT VERSION FROM RESUB TABLE
	    // ----------------------------------------------------

	    BigDecimal maxResubVer =
	        M_INT_RATES_resub_summary_repo.findMaxVersion(reportDate);

	    if (maxResubVer == null)
	        throw new RuntimeException("No record for: " + reportDate);

	    BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

	    Date now = new Date();

	    // ====================================================
	    // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
	    // ====================================================

	    M_INT_RATES_RESUB_Summary_Entity resubSummary =
	        new M_INT_RATES_RESUB_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity, resubSummary,
	        "reportDate", "reportVersion", "reportResubDate");

	    resubSummary.setReport_date(reportDate);
	    resubSummary.setReport_version(newVersion);
	    resubSummary.setReportResubDate(now);

	    // ====================================================
	    // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
	    // ====================================================

	    M_INT_RATES_RESUB_Detail_Entity resubDetail =
	        new M_INT_RATES_RESUB_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity, resubDetail,
	        "reportDate", "reportVersion", "reportResubDate");

	    resubDetail.setReport_date(reportDate);
	    resubDetail.setReport_version(newVersion);
	    resubDetail.setReportResubDate(now);

	    // ====================================================
	    // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
	    // ====================================================

	    M_INT_RATES_Archival_Summary_Entity archSummary =
	        new M_INT_RATES_Archival_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity, archSummary,
	        "reportDate", "reportVersion", "reportResubDate");

	    archSummary.setReport_date(reportDate);
	    archSummary.setReport_version(newVersion);   // SAME VERSION
	    archSummary.setReportResubDate(now);

	    // ====================================================
	    // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
	    // ====================================================

	    M_INT_RATES_Archival_Detail_Entity archDetail =
	        new M_INT_RATES_Archival_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity, archDetail,
	        "reportDate", "reportVersion", "reportResubDate");

	    archDetail.setReport_date(reportDate);
	    archDetail.setReport_version(newVersion);    // SAME VERSION
	    archDetail.setReportResubDate(now);

	    // ====================================================
	    // 6️⃣ SAVE ALL WITH SAME DATA
	    // ====================================================

	    M_INT_RATES_resub_summary_repo.save(resubSummary);
	    M_INT_RATES_resub_detail_repo.save(resubDetail);

	    M_INT_RATES_Archival_Summary_Repo.save(archSummary);
	    M_INT_RATES_Archival_Detail_Repo.save(archDetail);
	}

		
	//Archival View
	public List<Object[]> getM_INT_RATESArchival() {
	List<Object[]> archivalList = new ArrayList<>();

	try {
		List<M_INT_RATES_Archival_Summary_Entity> repoData = M_INT_RATES_Archival_Summary_Repo
				.getdatabydateListWithVersion();

		if (repoData != null && !repoData.isEmpty()) {
			for (M_INT_RATES_Archival_Summary_Entity entity : repoData) {
				Object[] row = new Object[] {
						entity.getReport_date(), 
						entity.getReport_version() 
				};
				archivalList.add(row);
			}

			System.out.println("Fetched " + archivalList.size() + " archival records");
			M_INT_RATES_Archival_Summary_Entity first = repoData.get(0);
			System.out.println("Latest archival version: " + first.getReport_version());
		} else {
			System.out.println("No archival data found.");
		}

	} catch (Exception e) {
		System.err.println("Error fetching M_INT_RATES  Archival data: " + e.getMessage());
		e.printStackTrace();
	}

	return archivalList;
	}

	/// RESUB VIEW



	public List<Object[]> getM_INTRATESResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_INT_RATES_Archival_Summary_Entity> latestArchivalList =
	        		M_INT_RATES_Archival_Summary_Repo.getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_INT_RATES_Archival_Summary_Entity entity : latestArchivalList) {
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
	        System.err.println("Error fetching M_INT_RATES Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	
	// Normal format Excel

		public byte[] getM_INTRATESExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
					return getExcelM_INTRATESARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_INT_RATESResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else {

				if ("email".equalsIgnoreCase(format) && version == null) {
					logger.info("Got format as Email");
					logger.info("Service: Generating Email report for version {}", version);
					return BRRS_M_INT_RATESEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} else {

					// Fetch data

					List<M_INT_RATES_Summary_Entity> dataList = M_INT_RATES_Summary_Repo
							.getdatabydateList(dateformat.parse(todate));

					if (dataList.isEmpty()) {
						logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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

						int startRow = 10;

						if (!dataList.isEmpty()) {
							for (int i = 0; i < dataList.size(); i++) {
								M_INT_RATES_Summary_Entity record = dataList.get(i);
								System.out.println("rownumber=" + startRow + i);
								Row row = sheet.getRow(startRow + i);
								if (row == null) {
									row = sheet.createRow(startRow + i);
								}

		Cell cell1 = row.createCell(1);
						if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
							cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
							cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
						Cell cell3 = row.createCell(3);
						if (record.getR11_VOLUME() != null) {
							cell3.setCellValue(record.getR11_VOLUME().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						
						
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_VOLUME() != null) {
						    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_VOLUME() != null) {
						    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_VOLUME() != null) {
						    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_VOLUME() != null) {
						    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(15);
						if (row == null) {
						    row = sheet.createRow(15);
						}

						cell1 = row.createCell(1);
						if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_VOLUME() != null) {
						    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(16);
						if (row == null) {
						    row = sheet.createRow(16);
						}

						cell1 = row.createCell(1);
						if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_VOLUME() != null) {
						    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(17);
						if (row == null) {
						    row = sheet.createRow(17);
						}

						cell1 = row.createCell(1);
						if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR18_VOLUME() != null) {
						    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(18);
						if (row == null) {
						    row = sheet.createRow(18);
						}

						cell1 = row.createCell(1);
						if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_VOLUME() != null) {
						    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(19);
						if (row == null) {
						    row = sheet.createRow(19);
						}

						cell1 = row.createCell(1);
						if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR20_VOLUME() != null) {
						    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(20);
						if (row == null) {
						    row = sheet.createRow(20);
						}

						cell1 = row.createCell(1);
						if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR21_VOLUME() != null) {
						    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(21);
						if (row == null) {
						    row = sheet.createRow(21);
						}

						cell1 = row.createCell(1);
						if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR22_VOLUME() != null) {
						    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(22);
						if (row == null) {
						    row = sheet.createRow(22);
						}

						cell1 = row.createCell(1);
						if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR23_VOLUME() != null) {
						    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}


						row = sheet.getRow(24);
						if (row == null) {
						    row = sheet.createRow(24);
						}

						cell1 = row.createCell(1);
						if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR25_VOLUME() != null) {
						    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(25);
						if (row == null) {
						    row = sheet.createRow(25);
						}

						cell1 = row.createCell(1);
						if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR26_VOLUME() != null) {
						    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(26);
						if (row == null) {
						    row = sheet.createRow(26);
						}

						cell1 = row.createCell(1);
						if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR27_VOLUME() != null) {
						    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(27);
						if (row == null) {
						    row = sheet.createRow(27);
						}

						cell1 = row.createCell(1);
						if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR28_VOLUME() != null) {
						    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(28);
						if (row == null) {
						    row = sheet.createRow(28);
						}

						cell1 = row.createCell(1);
						if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR29_VOLUME() != null) {
						    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(29);
						if (row == null) {
						    row = sheet.createRow(29);
						}

						cell1 = row.createCell(1);
						if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR30_VOLUME() != null) {
						    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(30);
						if (row == null) {
						    row = sheet.createRow(30);
						}

						cell1 = row.createCell(1);
						if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR31_VOLUME() != null) {
						    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(31);
						if (row == null) {
						    row = sheet.createRow(31);
						}

						cell1 = row.createCell(1);
						if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR32_VOLUME() != null) {
						    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(32);
						if (row == null) {
						    row = sheet.createRow(32);
						}

						cell1 = row.createCell(1);
						if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR33_VOLUME() != null) {
						    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(33);
						if (row == null) {
						    row = sheet.createRow(33);
						}

						cell1 = row.createCell(1);
						if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR34_VOLUME() != null) {
						    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(34);
						if (row == null) {
						    row = sheet.createRow(34);
						}

						cell1 = row.createCell(1);
						if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR35_VOLUME() != null) {
						    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(35);
						if (row == null) {
						    row = sheet.createRow(35);
						}

						cell1 = row.createCell(1);
						if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR36_VOLUME() != null) {
						    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(36);
						if (row == null) {
						    row = sheet.createRow(36);
						}

						cell1 = row.createCell(1);
						if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR37_VOLUME() != null) {
						    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(37);
						if (row == null) {
						    row = sheet.createRow(37);
						}

						cell1 = row.createCell(1);
						if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR38_VOLUME() != null) {
						    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(38);
						if (row == null) {
						    row = sheet.createRow(38);
						}

						cell1 = row.createCell(1);
						if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR39_VOLUME() != null) {
						    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(39);
						if (row == null) {
						    row = sheet.createRow(39);
						}

						cell1 = row.createCell(1);
						if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR40_VOLUME() != null) {
						    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(40);
						if (row == null) {
						    row = sheet.createRow(40);
						}

						cell1 = row.createCell(1);
						if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR41_VOLUME() != null) {
						    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
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
		public byte[] BRRS_M_INT_RATESEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Email Excel generation process in memory.");
			
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_INT_RATESARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_INT_RATESEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 
			else {
			List<M_INT_RATES_Summary_Entity> dataList = M_INT_RATES_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_INT_RATES_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}


							Cell cell1 = row.createCell(1);
			if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
				cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
				cell1.setCellStyle(numberStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(textStyle);
			}
			
			Cell cell2 = row.createCell(2);
			if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
				cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
				cell2.setCellStyle(numberStyle);
			} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
			}
			
			
			
			row = sheet.getRow(11);
			if (row == null) {
			    row = sheet.createRow(11);
			}

			cell1 = row.createCell(1);
			if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(12);
			if (row == null) {
			    row = sheet.createRow(12);
			}

			cell1 = row.createCell(1);
			if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(13);
			if (row == null) {
			    row = sheet.createRow(13);
			}

			cell1 = row.createCell(1);
			if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			

			row = sheet.getRow(14);
			if (row == null) {
			    row = sheet.createRow(14);
			}

			cell1 = row.createCell(1);
			if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(15);
			if (row == null) {
			    row = sheet.createRow(15);
			}

			cell1 = row.createCell(1);
			if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(16);
			if (row == null) {
			    row = sheet.createRow(16);
			}

			cell1 = row.createCell(1);
			if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(17);
			if (row == null) {
			    row = sheet.createRow(17);
			}

			cell1 = row.createCell(1);
			if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(18);
			if (row == null) {
			    row = sheet.createRow(18);
			}

			cell1 = row.createCell(1);
			if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(19);
			if (row == null) {
			    row = sheet.createRow(19);
			}

			cell1 = row.createCell(1);
			if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}



			row = sheet.getRow(20);
			if (row == null) {
			    row = sheet.createRow(20);
			}

			cell1 = row.createCell(1);
			if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(21);
			if (row == null) {
			    row = sheet.createRow(21);
			}

			cell1 = row.createCell(1);
			if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(22);
			if (row == null) {
			    row = sheet.createRow(22);
			}

			cell1 = row.createCell(1);
			if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		

			row = sheet.getRow(23);
			if (row == null) {
			    row = sheet.createRow(23);
			}

			cell1 = row.createCell(1);
			if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(24);
			if (row == null) {
			    row = sheet.createRow(24);
			}

			cell1 = row.createCell(1);
			if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(25);
			if (row == null) {
			    row = sheet.createRow(25);
			}

			cell1 = row.createCell(1);
			if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(26);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(27);
			if (row == null) {
			    row = sheet.createRow(27);
			}

			cell1 = row.createCell(1);
			if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(28);
			if (row == null) {
			    row = sheet.createRow(28);
			}

			cell1 = row.createCell(1);
			if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(29);
			if (row == null) {
			    row = sheet.createRow(29);
			}

			cell1 = row.createCell(1);
			if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(30);
			if (row == null) {
			    row = sheet.createRow(30);
			}

			cell1 = row.createCell(1);
			if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(31);
			if (row == null) {
			    row = sheet.createRow(31);
			}

			cell1 = row.createCell(1);
			if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(32);
			if (row == null) {
			    row = sheet.createRow(32);
			}

			cell1 = row.createCell(1);
			if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(33);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
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
		public byte[] getExcelM_INTRATESARCHIVAL(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory in Archival.");

			if ("email".equalsIgnoreCase(format) && version != null) {
				try {
					// Redirecting to Archival
					return BRRS_M_INT_RATESARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
				}
			} 

			List<M_INT_RATES_Archival_Summary_Entity> dataList = M_INT_RATES_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_INT_RATES report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_INT_RATES_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		Cell cell1 = row.createCell(1);
							if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
								cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
								cell1.setCellStyle(numberStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							
							Cell cell2 = row.createCell(2);
							if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
								cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							
							Cell cell3 = row.createCell(3);
							if (record.getR11_VOLUME() != null) {
								cell3.setCellValue(record.getR11_VOLUME().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							
							row = sheet.getRow(11);
							if (row == null) {
							    row = sheet.createRow(11);
							}

							cell1 = row.createCell(1);
							if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_VOLUME() != null) {
							    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(12);
							if (row == null) {
							    row = sheet.createRow(12);
							}

							cell1 = row.createCell(1);
							if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR13_VOLUME() != null) {
							    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(13);
							if (row == null) {
							    row = sheet.createRow(13);
							}

							cell1 = row.createCell(1);
							if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_VOLUME() != null) {
							    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(14);
							if (row == null) {
							    row = sheet.createRow(14);
							}

							cell1 = row.createCell(1);
							if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_VOLUME() != null) {
							    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(15);
							if (row == null) {
							    row = sheet.createRow(15);
							}

							cell1 = row.createCell(1);
							if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR16_VOLUME() != null) {
							    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(16);
							if (row == null) {
							    row = sheet.createRow(16);
							}

							cell1 = row.createCell(1);
							if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR17_VOLUME() != null) {
							    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(17);
							if (row == null) {
							    row = sheet.createRow(17);
							}

							cell1 = row.createCell(1);
							if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR18_VOLUME() != null) {
							    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(18);
							if (row == null) {
							    row = sheet.createRow(18);
							}

							cell1 = row.createCell(1);
							if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR19_VOLUME() != null) {
							    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(19);
							if (row == null) {
							    row = sheet.createRow(19);
							}

							cell1 = row.createCell(1);
							if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR20_VOLUME() != null) {
							    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(20);
							if (row == null) {
							    row = sheet.createRow(20);
							}

							cell1 = row.createCell(1);
							if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR21_VOLUME() != null) {
							    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(21);
							if (row == null) {
							    row = sheet.createRow(21);
							}

							cell1 = row.createCell(1);
							if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR22_VOLUME() != null) {
							    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(22);
							if (row == null) {
							    row = sheet.createRow(22);
							}

							cell1 = row.createCell(1);
							if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR23_VOLUME() != null) {
							    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}
							
							
							/*
							 * // ----------------- R24 ----------------- row = sheet.getRow(23); // R24 =
							 * Excel row 24 (index 23) if (row == null) { row = sheet.createRow(23); }
							 * 
							 * cell1 = row.createCell(1); if (record.getR24_NOMINAL_INTEREST_RATE() != null)
							 * { cell1.setCellValue(record.getR24_NOMINAL_INTEREST_RATE()); // <-- use text
							 * if 200-200 cell1.setCellStyle(textStyle); } else { cell1.setCellValue("");
							 * cell1.setCellStyle(textStyle); }
							 * 
							 * cell2 = row.createCell(2); if (record.getR24_AVG_EFFECTIVE_RATE() != null) {
							 * cell2.setCellValue(record.getR24_AVG_EFFECTIVE_RATE());
							 * cell2.setCellStyle(textStyle); } else { cell2.setCellValue("");
							 * cell2.setCellStyle(textStyle); }
							 */

							row = sheet.getRow(24);
							if (row == null) {
							    row = sheet.createRow(24);
							}

							cell1 = row.createCell(1);
							if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR25_VOLUME() != null) {
							    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(25);
							if (row == null) {
							    row = sheet.createRow(25);
							}

							cell1 = row.createCell(1);
							if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR26_VOLUME() != null) {
							    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(26);
							if (row == null) {
							    row = sheet.createRow(26);
							}

							cell1 = row.createCell(1);
							if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR27_VOLUME() != null) {
							    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(27);
							if (row == null) {
							    row = sheet.createRow(27);
							}

							cell1 = row.createCell(1);
							if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR28_VOLUME() != null) {
							    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(28);
							if (row == null) {
							    row = sheet.createRow(28);
							}

							cell1 = row.createCell(1);
							if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR29_VOLUME() != null) {
							    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(29);
							if (row == null) {
							    row = sheet.createRow(29);
							}

							cell1 = row.createCell(1);
							if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR30_VOLUME() != null) {
							    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(30);
							if (row == null) {
							    row = sheet.createRow(30);
							}

							cell1 = row.createCell(1);
							if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR31_VOLUME() != null) {
							    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(31);
							if (row == null) {
							    row = sheet.createRow(31);
							}

							cell1 = row.createCell(1);
							if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR32_VOLUME() != null) {
							    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(32);
							if (row == null) {
							    row = sheet.createRow(32);
							}

							cell1 = row.createCell(1);
							if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR33_VOLUME() != null) {
							    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(33);
							if (row == null) {
							    row = sheet.createRow(33);
							}

							cell1 = row.createCell(1);
							if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR34_VOLUME() != null) {
							    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(34);
							if (row == null) {
							    row = sheet.createRow(34);
							}

							cell1 = row.createCell(1);
							if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR35_VOLUME() != null) {
							    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(35);
							if (row == null) {
							    row = sheet.createRow(35);
							}

							cell1 = row.createCell(1);
							if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR36_VOLUME() != null) {
							    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(36);
							if (row == null) {
							    row = sheet.createRow(36);
							}

							cell1 = row.createCell(1);
							if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR37_VOLUME() != null) {
							    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(37);
							if (row == null) {
							    row = sheet.createRow(37);
							}

							cell1 = row.createCell(1);
							if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR38_VOLUME() != null) {
							    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(38);
							if (row == null) {
							    row = sheet.createRow(38);
							}

							cell1 = row.createCell(1);
							if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR39_VOLUME() != null) {
							    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(39);
							if (row == null) {
							    row = sheet.createRow(39);
							}

							cell1 = row.createCell(1);
							if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR40_VOLUME() != null) {
							    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}



							row = sheet.getRow(40);
							if (row == null) {
							    row = sheet.createRow(40);
							}

							cell1 = row.createCell(1);
							if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
							    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
							    cell1.setCellStyle(numberStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
							    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
							    cell2.setCellStyle(numberStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR41_VOLUME() != null) {
							    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
							    cell3.setCellStyle(numberStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
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
		public byte[] BRRS_M_INT_RATESARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Archival Email Excel generation process in memory.");

			List<M_INT_RATES_Archival_Summary_Entity> dataList = M_INT_RATES_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_INT_RATES_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

	Cell cell1 = row.createCell(1);
			if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
				cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
				cell1.setCellStyle(numberStyle);
			} else {
				cell1.setCellValue("");
				cell1.setCellStyle(textStyle);
			}
			
			Cell cell2 = row.createCell(2);
			if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
				cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
				cell2.setCellStyle(numberStyle);
			} else {
				cell2.setCellValue("");
				cell2.setCellStyle(textStyle);
			}
			
			
			
			row = sheet.getRow(11);
			if (row == null) {
			    row = sheet.createRow(11);
			}

			cell1 = row.createCell(1);
			if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(12);
			if (row == null) {
			    row = sheet.createRow(12);
			}

			cell1 = row.createCell(1);
			if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(13);
			if (row == null) {
			    row = sheet.createRow(13);
			}

			cell1 = row.createCell(1);
			if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			

			row = sheet.getRow(14);
			if (row == null) {
			    row = sheet.createRow(14);
			}

			cell1 = row.createCell(1);
			if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(15);
			if (row == null) {
			    row = sheet.createRow(15);
			}

			cell1 = row.createCell(1);
			if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(16);
			if (row == null) {
			    row = sheet.createRow(16);
			}

			cell1 = row.createCell(1);
			if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(17);
			if (row == null) {
			    row = sheet.createRow(17);
			}

			cell1 = row.createCell(1);
			if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(18);
			if (row == null) {
			    row = sheet.createRow(18);
			}

			cell1 = row.createCell(1);
			if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(19);
			if (row == null) {
			    row = sheet.createRow(19);
			}

			cell1 = row.createCell(1);
			if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}



			row = sheet.getRow(20);
			if (row == null) {
			    row = sheet.createRow(20);
			}

			cell1 = row.createCell(1);
			if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(21);
			if (row == null) {
			    row = sheet.createRow(21);
			}

			cell1 = row.createCell(1);
			if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		


			row = sheet.getRow(22);
			if (row == null) {
			    row = sheet.createRow(22);
			}

			cell1 = row.createCell(1);
			if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		

			row = sheet.getRow(23);
			if (row == null) {
			    row = sheet.createRow(23);
			}

			cell1 = row.createCell(1);
			if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(24);
			if (row == null) {
			    row = sheet.createRow(24);
			}

			cell1 = row.createCell(1);
			if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(25);
			if (row == null) {
			    row = sheet.createRow(25);
			}

			cell1 = row.createCell(1);
			if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(26);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(27);
			if (row == null) {
			    row = sheet.createRow(27);
			}

			cell1 = row.createCell(1);
			if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(28);
			if (row == null) {
			    row = sheet.createRow(28);
			}

			cell1 = row.createCell(1);
			if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(29);
			if (row == null) {
			    row = sheet.createRow(29);
			}

			cell1 = row.createCell(1);
			if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(30);
			if (row == null) {
			    row = sheet.createRow(30);
			}

			cell1 = row.createCell(1);
			if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			



			row = sheet.getRow(31);
			if (row == null) {
			    row = sheet.createRow(31);
			}

			cell1 = row.createCell(1);
			if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

		



			row = sheet.getRow(32);
			if (row == null) {
			    row = sheet.createRow(32);
			}

			cell1 = row.createCell(1);
			if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
			}

			


			row = sheet.getRow(33);
			if (row == null) {
			    row = sheet.createRow(33);
			}

			cell1 = row.createCell(1);
			if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
			    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
			    cell1.setCellStyle(numberStyle);
			} else {
			    cell1.setCellValue("");
			    cell1.setCellStyle(textStyle);
			}

			cell2 = row.createCell(2);
			if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
			    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
			    cell2.setCellStyle(numberStyle);
			} else {
			    cell2.setCellValue("");
			    cell2.setCellStyle(textStyle);
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
		public byte[] BRRS_M_INT_RATESResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

			if ("email".equalsIgnoreCase(format) && version != null) {
				logger.info("Service: Generating RESUB report for version {}", version);

				try {
					// ✅ Redirecting to Resub Excel
					return BRRS_M_INT_RATESEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

				} catch (ParseException e) {
					logger.error("Invalid report date format: {}", fromdate, e);
					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

			List<M_INT_RATES_RESUB_Summary_Entity> dataList = M_INT_RATES_resub_summary_repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_INT_RATES report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {

						M_INT_RATES_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell cell1 = row.createCell(1);
						if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
							cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						
						Cell cell2 = row.createCell(2);
						if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
							cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						
						Cell cell3 = row.createCell(3);
						if (record.getR11_VOLUME() != null) {
							cell3.setCellValue(record.getR11_VOLUME().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						
						
						row = sheet.getRow(11);
						if (row == null) {
						    row = sheet.createRow(11);
						}

						cell1 = row.createCell(1);
						if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR12_VOLUME() != null) {
						    cell3.setCellValue(record.getR12_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(12);
						if (row == null) {
						    row = sheet.createRow(12);
						}

						cell1 = row.createCell(1);
						if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR13_VOLUME() != null) {
						    cell3.setCellValue(record.getR13_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(13);
						if (row == null) {
						    row = sheet.createRow(13);
						}

						cell1 = row.createCell(1);
						if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_VOLUME() != null) {
						    cell3.setCellValue(record.getR14_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(14);
						if (row == null) {
						    row = sheet.createRow(14);
						}

						cell1 = row.createCell(1);
						if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_VOLUME() != null) {
						    cell3.setCellValue(record.getR15_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(15);
						if (row == null) {
						    row = sheet.createRow(15);
						}

						cell1 = row.createCell(1);
						if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_VOLUME() != null) {
						    cell3.setCellValue(record.getR16_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(16);
						if (row == null) {
						    row = sheet.createRow(16);
						}

						cell1 = row.createCell(1);
						if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_VOLUME() != null) {
						    cell3.setCellValue(record.getR17_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(17);
						if (row == null) {
						    row = sheet.createRow(17);
						}

						cell1 = row.createCell(1);
						if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR18_VOLUME() != null) {
						    cell3.setCellValue(record.getR18_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(18);
						if (row == null) {
						    row = sheet.createRow(18);
						}

						cell1 = row.createCell(1);
						if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_VOLUME() != null) {
						    cell3.setCellValue(record.getR19_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(19);
						if (row == null) {
						    row = sheet.createRow(19);
						}

						cell1 = row.createCell(1);
						if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR20_VOLUME() != null) {
						    cell3.setCellValue(record.getR20_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(20);
						if (row == null) {
						    row = sheet.createRow(20);
						}

						cell1 = row.createCell(1);
						if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR21_VOLUME() != null) {
						    cell3.setCellValue(record.getR21_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(21);
						if (row == null) {
						    row = sheet.createRow(21);
						}

						cell1 = row.createCell(1);
						if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR22_VOLUME() != null) {
						    cell3.setCellValue(record.getR22_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(22);
						if (row == null) {
						    row = sheet.createRow(22);
						}

						cell1 = row.createCell(1);
						if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR23_VOLUME() != null) {
						    cell3.setCellValue(record.getR23_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}
						
						
						/*
						 * // ----------------- R24 ----------------- row = sheet.getRow(23); // R24 =
						 * Excel row 24 (index 23) if (row == null) { row = sheet.createRow(23); }
						 * 
						 * cell1 = row.createCell(1); if (record.getR24_NOMINAL_INTEREST_RATE() != null)
						 * { cell1.setCellValue(record.getR24_NOMINAL_INTEREST_RATE()); // <-- use text
						 * if 200-200 cell1.setCellStyle(textStyle); } else { cell1.setCellValue("");
						 * cell1.setCellStyle(textStyle); }
						 * 
						 * cell2 = row.createCell(2); if (record.getR24_AVG_EFFECTIVE_RATE() != null) {
						 * cell2.setCellValue(record.getR24_AVG_EFFECTIVE_RATE());
						 * cell2.setCellStyle(textStyle); } else { cell2.setCellValue("");
						 * cell2.setCellStyle(textStyle); }
						 */

						row = sheet.getRow(24);
						if (row == null) {
						    row = sheet.createRow(24);
						}

						cell1 = row.createCell(1);
						if (record.getR25_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR25_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR25_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR25_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR25_VOLUME() != null) {
						    cell3.setCellValue(record.getR25_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(25);
						if (row == null) {
						    row = sheet.createRow(25);
						}

						cell1 = row.createCell(1);
						if (record.getR26_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR26_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR26_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR26_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR26_VOLUME() != null) {
						    cell3.setCellValue(record.getR26_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(26);
						if (row == null) {
						    row = sheet.createRow(26);
						}

						cell1 = row.createCell(1);
						if (record.getR27_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR27_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR27_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR27_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR27_VOLUME() != null) {
						    cell3.setCellValue(record.getR27_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(27);
						if (row == null) {
						    row = sheet.createRow(27);
						}

						cell1 = row.createCell(1);
						if (record.getR28_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR28_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR28_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR28_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR28_VOLUME() != null) {
						    cell3.setCellValue(record.getR28_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(28);
						if (row == null) {
						    row = sheet.createRow(28);
						}

						cell1 = row.createCell(1);
						if (record.getR29_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR29_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR29_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR29_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR29_VOLUME() != null) {
						    cell3.setCellValue(record.getR29_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(29);
						if (row == null) {
						    row = sheet.createRow(29);
						}

						cell1 = row.createCell(1);
						if (record.getR30_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR30_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR30_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR30_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR30_VOLUME() != null) {
						    cell3.setCellValue(record.getR30_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(30);
						if (row == null) {
						    row = sheet.createRow(30);
						}

						cell1 = row.createCell(1);
						if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR31_VOLUME() != null) {
						    cell3.setCellValue(record.getR31_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(31);
						if (row == null) {
						    row = sheet.createRow(31);
						}

						cell1 = row.createCell(1);
						if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR32_VOLUME() != null) {
						    cell3.setCellValue(record.getR32_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(32);
						if (row == null) {
						    row = sheet.createRow(32);
						}

						cell1 = row.createCell(1);
						if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR33_VOLUME() != null) {
						    cell3.setCellValue(record.getR33_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(33);
						if (row == null) {
						    row = sheet.createRow(33);
						}

						cell1 = row.createCell(1);
						if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR34_VOLUME() != null) {
						    cell3.setCellValue(record.getR34_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(34);
						if (row == null) {
						    row = sheet.createRow(34);
						}

						cell1 = row.createCell(1);
						if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR35_VOLUME() != null) {
						    cell3.setCellValue(record.getR35_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(35);
						if (row == null) {
						    row = sheet.createRow(35);
						}

						cell1 = row.createCell(1);
						if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR36_VOLUME() != null) {
						    cell3.setCellValue(record.getR36_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(36);
						if (row == null) {
						    row = sheet.createRow(36);
						}

						cell1 = row.createCell(1);
						if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR37_VOLUME() != null) {
						    cell3.setCellValue(record.getR37_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(37);
						if (row == null) {
						    row = sheet.createRow(37);
						}

						cell1 = row.createCell(1);
						if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR38_VOLUME() != null) {
						    cell3.setCellValue(record.getR38_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(38);
						if (row == null) {
						    row = sheet.createRow(38);
						}

						cell1 = row.createCell(1);
						if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR39_VOLUME() != null) {
						    cell3.setCellValue(record.getR39_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(39);
						if (row == null) {
						    row = sheet.createRow(39);
						}

						cell1 = row.createCell(1);
						if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR40_VOLUME() != null) {
						    cell3.setCellValue(record.getR40_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}



						row = sheet.getRow(40);
						if (row == null) {
						    row = sheet.createRow(40);
						}

						cell1 = row.createCell(1);
						if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
						    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
						    cell1.setCellStyle(numberStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
						    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
						    cell2.setCellStyle(numberStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR41_VOLUME() != null) {
						    cell3.setCellValue(record.getR41_VOLUME().doubleValue());
						    cell3.setCellStyle(numberStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
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
		public byte[] BRRS_M_INT_RATESEmailResubExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Archival Email Excel generation process in memory.");

			List<M_INT_RATES_RESUB_Summary_Entity> dataList = M_INT_RATES_resub_summary_repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_INT_RATES report. Returning empty result.");
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

				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_INT_RATES_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

		Cell cell1 = row.createCell(1);
		if (record.getR11_NOMINAL_INTEREST_RATE() != null) {
			cell1.setCellValue(record.getR11_NOMINAL_INTEREST_RATE().doubleValue());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}
		
		Cell cell2 = row.createCell(2);
		if (record.getR11_AVG_EFFECTIVE_RATE() != null) {
			cell2.setCellValue(record.getR11_AVG_EFFECTIVE_RATE().doubleValue());
			cell2.setCellStyle(numberStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}
		
		
		
		row = sheet.getRow(11);
		if (row == null) {
		    row = sheet.createRow(11);
		}

		cell1 = row.createCell(1);
		if (record.getR12_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR12_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR12_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR12_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(12);
		if (row == null) {
		    row = sheet.createRow(12);
		}

		cell1 = row.createCell(1);
		if (record.getR13_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR13_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR13_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR13_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}




		row = sheet.getRow(13);
		if (row == null) {
		    row = sheet.createRow(13);
		}

		cell1 = row.createCell(1);
		if (record.getR14_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR14_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR14_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR14_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		

		row = sheet.getRow(14);
		if (row == null) {
		    row = sheet.createRow(14);
		}

		cell1 = row.createCell(1);
		if (record.getR15_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR15_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR15_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR15_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}




		row = sheet.getRow(15);
		if (row == null) {
		    row = sheet.createRow(15);
		}

		cell1 = row.createCell(1);
		if (record.getR16_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR16_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR16_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR16_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(16);
		if (row == null) {
		    row = sheet.createRow(16);
		}

		cell1 = row.createCell(1);
		if (record.getR17_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR17_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR17_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR17_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(17);
		if (row == null) {
		    row = sheet.createRow(17);
		}

		cell1 = row.createCell(1);
		if (record.getR18_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR18_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR18_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR18_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}





		row = sheet.getRow(18);
		if (row == null) {
		    row = sheet.createRow(18);
		}

		cell1 = row.createCell(1);
		if (record.getR19_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR19_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR19_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR19_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(19);
		if (row == null) {
		    row = sheet.createRow(19);
		}

		cell1 = row.createCell(1);
		if (record.getR20_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR20_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR20_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR20_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}



		row = sheet.getRow(20);
		if (row == null) {
		    row = sheet.createRow(20);
		}

		cell1 = row.createCell(1);
		if (record.getR21_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR21_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR21_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR21_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(21);
		if (row == null) {
		    row = sheet.createRow(21);
		}

		cell1 = row.createCell(1);
		if (record.getR22_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR22_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR22_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR22_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}




		row = sheet.getRow(22);
		if (row == null) {
		    row = sheet.createRow(22);
		}

		cell1 = row.createCell(1);
		if (record.getR23_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR23_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR23_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR23_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}



		row = sheet.getRow(23);
		if (row == null) {
		    row = sheet.createRow(23);
		}

		cell1 = row.createCell(1);
		if (record.getR31_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR31_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR31_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR31_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(24);
		if (row == null) {
		    row = sheet.createRow(24);
		}

		cell1 = row.createCell(1);
		if (record.getR32_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR32_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR32_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR32_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(25);
		if (row == null) {
		    row = sheet.createRow(25);
		}

		cell1 = row.createCell(1);
		if (record.getR33_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR33_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR33_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR33_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}





		row = sheet.getRow(26);
		if (row == null) {
		    row = sheet.createRow(33);
		}

		cell1 = row.createCell(1);
		if (record.getR34_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR34_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR34_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR34_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(27);
		if (row == null) {
		    row = sheet.createRow(27);
		}

		cell1 = row.createCell(1);
		if (record.getR35_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR35_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR35_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR35_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(28);
		if (row == null) {
		    row = sheet.createRow(28);
		}

		cell1 = row.createCell(1);
		if (record.getR36_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR36_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR36_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR36_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

	row = sheet.getRow(29);
		if (row == null) {
		    row = sheet.createRow(29);
		}

		cell1 = row.createCell(1);
		if (record.getR37_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR37_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR37_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR37_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(30);
		if (row == null) {
		    row = sheet.createRow(30);
		}

		cell1 = row.createCell(1);
		if (record.getR38_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR38_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR38_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR38_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		



		row = sheet.getRow(31);
		if (row == null) {
		    row = sheet.createRow(31);
		}

		cell1 = row.createCell(1);
		if (record.getR39_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR39_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR39_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR39_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}





		row = sheet.getRow(32);
		if (row == null) {
		    row = sheet.createRow(32);
		}

		cell1 = row.createCell(1);
		if (record.getR40_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR40_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR40_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR40_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
		}

		


		row = sheet.getRow(33);
		if (row == null) {
		    row = sheet.createRow(33);
		}

		cell1 = row.createCell(1);
		if (record.getR41_NOMINAL_INTEREST_RATE() != null) {
		    cell1.setCellValue(record.getR41_NOMINAL_INTEREST_RATE().doubleValue());
		    cell1.setCellStyle(numberStyle);
		} else {
		    cell1.setCellValue("");
		    cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR41_AVG_EFFECTIVE_RATE() != null) {
		    cell2.setCellValue(record.getR41_AVG_EFFECTIVE_RATE().doubleValue());
		    cell2.setCellStyle(numberStyle);
		} else {
		    cell2.setCellValue("");
		    cell2.setCellStyle(textStyle);
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
