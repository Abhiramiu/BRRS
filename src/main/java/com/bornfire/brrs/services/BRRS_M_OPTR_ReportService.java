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
import org.hibernate.Session;
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

import com.bornfire.brrs.entities.BRRS_M_OPTR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OPTR_Summary_Repo;
import com.bornfire.brrs.entities.M_OPTR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_OPTR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OPTR_Detail_Entity;
import com.bornfire.brrs.entities.M_OPTR_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_OPTR_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_OPTR_Summary_Entity;

@Component
@Service

public class BRRS_M_OPTR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OPTR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_OPTR_Summary_Repo brrs_M_OPTR_summary_repo;

	@Autowired
	BRRS_M_OPTR_Detail_Repo brrs_M_OPTR_detail_repo;

	@Autowired
	BRRS_M_OPTR_Archival_Summary_Repo M_OPTR_Archival_Summary_Repo;

	@Autowired
	BRRS_M_OPTR_Archival_Detail_Repo BRRS_M_OPTR_Archival_Detail_Repo;

	@Autowired
	BRRS_M_OPTR_Resub_Summary_Repo M_OPTR_Resub_Summary_Repo;

	@Autowired
	BRRS_M_OPTR_Resub_Detail_Repo M_OPTR_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_OPTRView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("dtltype...." + dtltype);
		System.out.println("type...." + type);

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_OPTR_Archival_Summary_Entity> T1Master = M_OPTR_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_OPTR_Resub_Summary_Entity> T1Master = M_OPTR_Resub_Summary_Repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_OPTR_Summary_Entity> T1Master = brrs_M_OPTR_summary_repo
						.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_OPTR_Archival_Detail_Entity> T1Master = BRRS_M_OPTR_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_OPTR_Resub_Detail_Entity> T1Master = M_OPTR_Resub_Detail_Repo.getdatabydateListarchival(d1,
							version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_OPTR_Detail_Entity> T1Master = brrs_M_OPTR_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OPTR");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_OPTR_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    M_OPTR_Summary_Entity existing = brrs_M_OPTR_summary_repo.findById(updatedEntity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    M_OPTR_Detail_Entity existingDetail =
	    		brrs_M_OPTR_detail_repo.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                        M_OPTR_Detail_Entity d = new M_OPTR_Detail_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });
	    
	    try {
	        // 1️⃣ Loop from R10 to R58 and copy fields
	    	
	        for (int i = 10; i <= 14; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = {"INTEREST_RATES", "EQUITIES", "FOREIGN_EXC_GOLD", "COMMODITIES","TOTAL" };

	            for (String field : fields) {
	            	   String getterName = "get" + prefix + field; // e.g., getR10_item
	                   String setterName = "set" + prefix + field; // e.g., setR10_item

	                try {
	                    Method getter = M_OPTR_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_OPTR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    
	                    Method detailSetter =
	                            M_OPTR_Detail_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	       

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    brrs_M_OPTR_detail_repo.save(existingDetail);
	    brrs_M_OPTR_summary_repo.save(existing);
	    
		
	}
	
	public void updateResubReport(M_OPTR_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = M_OPTR_Resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_OPTR_Resub_Summary_Entity resubSummary = new M_OPTR_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_OPTR_Resub_Detail_Entity resubDetail = new M_OPTR_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_OPTR_Archival_Summary_Entity archSummary = new M_OPTR_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_OPTR_Archival_Detail_Entity archDetail = new M_OPTR_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		M_OPTR_Resub_Summary_Repo.save(resubSummary);
		M_OPTR_Resub_Detail_Repo.save(resubDetail);

		M_OPTR_Archival_Summary_Repo.save(archSummary);
		BRRS_M_OPTR_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_OPTRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_OPTR_Archival_Summary_Entity> latestArchivalList = M_OPTR_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_OPTR_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_OPTR Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_OPTRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_OPTR_Archival_Summary_Entity> repoData = M_OPTR_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_OPTR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_OPTR_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_OPTR Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_OPTRExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_OPTRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OPTRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OPTREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_OPTR_Summary_Entity> dataList = brrs_M_OPTR_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
							M_OPTR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							Cell cell2 = row.createCell(4);
							if (record.getR10_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(5);
							if (record.getR10_EQUITIES() != null) {
								cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(6);
							if (record.getR10_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(7);
							if (record.getR10_COMMODITIES() != null) {
								cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(10);

							cell2 = row.createCell(4);
							if (record.getR11_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(5);
							if (record.getR11_EQUITIES() != null) {
								cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(6);
							if (record.getR11_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(7);
							if (record.getR11_COMMODITIES() != null) {
								cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);
							cell2 = row.createCell(4);
							if (record.getR13_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(5);
							if (record.getR13_EQUITIES() != null) {
								cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(6);
							if (record.getR13_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(7);
							if (record.getR13_COMMODITIES() != null) {
								cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);
							cell2 = row.createCell(4);
							if (record.getR14_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(5);
							if (record.getR14_EQUITIES() != null) {
								cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(6);
							if (record.getR14_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(7);
							if (record.getR14_COMMODITIES() != null) {
								cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
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
	}

	// Normal Email Excel
	public byte[] BRRS_M_OPTREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OPTRArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OPTRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_OPTR_Summary_Entity> dataList = brrs_M_OPTR_summary_repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
						M_OPTR_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						/*
						 * Cell cell2 = row.createCell(4); if (record.getR10_INTEREST_RATES() != null) {
						 * cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
						 * cell2.setCellStyle(numberStyle); } else { cell2.setCellValue("");
						 * cell2.setCellStyle(textStyle); }
						 * 
						 * Cell cell3 = row.createCell(5); if (record.getR10_EQUITIES() != null) {
						 * cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
						 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
						 * cell3.setCellStyle(textStyle); }
						 * 
						 * Cell cell4 = row.createCell(6); if (record.getR10_FOREIGN_EXC_GOLD() != null)
						 * { cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
						 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
						 * cell4.setCellStyle(textStyle); }
						 * 
						 * Cell cell5 = row.createCell(7); if (record.getR10_COMMODITIES() != null) {
						 * cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
						 * cell5.setCellStyle(numberStyle); } else { cell5.setCellValue("");
						 * cell5.setCellStyle(textStyle); }
						 */
						row = sheet.getRow(10);

						Cell cell2 = row.createCell(4);
						if (record.getR11_INTEREST_RATES() != null) {
							cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(5);
						if (record.getR11_EQUITIES() != null) {
							cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(6);
						if (record.getR11_FOREIGN_EXC_GOLD() != null) {
							cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(7);
						if (record.getR11_COMMODITIES() != null) {
							cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);
						cell2 = row.createCell(4);
						if (record.getR13_INTEREST_RATES() != null) {
							cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(5);
						if (record.getR13_EQUITIES() != null) {
							cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record.getR13_FOREIGN_EXC_GOLD() != null) {
							cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record.getR13_COMMODITIES() != null) {
							cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);
						cell2 = row.createCell(4);
						if (record.getR14_INTEREST_RATES() != null) {
							cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(5);
						if (record.getR14_EQUITIES() != null) {
							cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record.getR14_FOREIGN_EXC_GOLD() != null) {
							cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record.getR14_COMMODITIES() != null) {
							cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
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

	// Archival format excel
	public byte[] getExcelM_OPTRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OPTRArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OPTR_Archival_Summary_Entity> dataList = M_OPTR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OPTR report. Returning empty result.");
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
					M_OPTR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell2 = row.createCell(4);
					if (record.getR10_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR10_EQUITIES() != null) {
						cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR10_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR10_COMMODITIES() != null) {
						cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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

	// Archival Email Excel
	public byte[] BRRS_M_OPTRArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OPTR_Archival_Summary_Entity> dataList = M_OPTR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
					M_OPTR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					/*
					 * Cell cell2 = row.createCell(4); if (record.getR10_INTEREST_RATES() != null) {
					 * cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
					 * cell2.setCellStyle(numberStyle); } else { cell2.setCellValue("");
					 * cell2.setCellStyle(textStyle); }
					 * 
					 * Cell cell3 = row.createCell(5); if (record.getR10_EQUITIES() != null) {
					 * cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 * 
					 * Cell cell4 = row.createCell(6); if (record.getR10_FOREIGN_EXC_GOLD() != null)
					 * { cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 * 
					 * Cell cell5 = row.createCell(7); if (record.getR10_COMMODITIES() != null) {
					 * cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
					 * cell5.setCellStyle(numberStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(10);

					Cell cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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

	// Resub Format excel
	public byte[] BRRS_M_OPTRResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OPTRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OPTR_Resub_Summary_Entity> dataList = M_OPTR_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OPTR report. Returning empty result.");
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

					M_OPTR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell2 = row.createCell(4);
					if (record.getR10_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR10_EQUITIES() != null) {
						cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR10_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR10_COMMODITIES() != null) {
						cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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

	// Resub Email Excel
	public byte[] BRRS_M_OPTRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OPTR_Resub_Summary_Entity> dataList = M_OPTR_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
					M_OPTR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					/*
					 * Cell cell2 = row.createCell(4); if (record.getR10_INTEREST_RATES() != null) {
					 * cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
					 * cell2.setCellStyle(numberStyle); } else { cell2.setCellValue("");
					 * cell2.setCellStyle(textStyle); }
					 * 
					 * Cell cell3 = row.createCell(5); if (record.getR10_EQUITIES() != null) {
					 * cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 * 
					 * Cell cell4 = row.createCell(6); if (record.getR10_FOREIGN_EXC_GOLD() != null)
					 * { cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 * 
					 * Cell cell5 = row.createCell(7); if (record.getR10_COMMODITIES() != null) {
					 * cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
					 * cell5.setCellStyle(numberStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(10);

					Cell cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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