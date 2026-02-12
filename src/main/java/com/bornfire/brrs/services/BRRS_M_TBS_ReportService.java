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

import com.bornfire.brrs.entities.BRRS_M_TBS_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Summary_Repo;
import com.bornfire.brrs.entities.M_CR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Summary_Entity;

@Component
@Service

public class BRRS_M_TBS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_TBS_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_TBS_Summary_Repo brrs_M_TBS_summary_repo;

	@Autowired
	BRRS_M_TBS_Detail_Repo brrs_M_TBS_detail_repo;

	@Autowired
	BRRS_M_TBS_Archival_Summary_Repo M_TBS_Archival_Summary_Repo;

	
	@Autowired
	BRRS_M_TBS_Archival_Detail_Repo BRRS_M_TBS_Archival_Detail_Repo;
	
	@Autowired
	BRRS_M_TBS_Resub_Summary_Repo M_TBS_Resub_Summary_Repo;

	@Autowired
	BRRS_M_TBS_Resub_Detail_Repo M_TBS_Resub_Detail_Repo;

	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_TBSView(String reportId, String fromdate, String todate, String currency,
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
				List<M_TBS_Archival_Summary_Entity> T1Master = M_TBS_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_TBS_Resub_Summary_Entity> T1Master = M_TBS_Resub_Summary_Repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_TBS_Summary_Entity> T1Master = brrs_M_TBS_summary_repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_TBS_Archival_Detail_Entity> T1Master = BRRS_M_TBS_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_TBS_Resub_Detail_Entity> T1Master = M_TBS_Resub_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_TBS_Detail_Entity> T1Master = brrs_M_TBS_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_TBS");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	
	public void updateReport(M_TBS_Summary_Entity updatedEntity) {

	    System.out.println("Came to TBS Summary services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	    M_TBS_Summary_Entity existingSummary = brrs_M_TBS_summary_repo
	            .findById(updatedEntity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL (LA2 style)
	    M_TBS_Detail_Entity existingDetail = brrs_M_TBS_detail_repo
	            .findById(updatedEntity.getReportDate())
	            .orElseGet(() -> {
	                M_TBS_Detail_Entity d = new M_TBS_Detail_Entity();
	                d.setReportDate(updatedEntity.getReportDate());
	                return d;
	            });

	    try {

	        String[] fields = { "NV_LONG", "NV_SHORT", "FV_LONG", "FV_SHORT", "QFHA" };

	        // ---------- Helper: copy rows into BOTH ----------
	        java.util.function.Consumer<int[]> copyRows = (rows) -> {
	            for (int row : rows) {
	                String prefix = "R" + row + "_";
	                for (String field : fields) {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;
	                    try {
	                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);

	                        Method summarySetter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                        Method detailSetter  = M_TBS_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

	                        Object newValue = getter.invoke(updatedEntity);

	                        // set into SUMMARY
	                        summarySetter.invoke(existingSummary, newValue);

	                        // set into DETAIL
	                        detailSetter.invoke(existingDetail, newValue);

	                    } catch (NoSuchMethodException e) {
	                        // skip missing safely
	                    } catch (Exception e) {
	                        throw new RuntimeException(e);
	                    }
	                }
	            }
	        };

	        // ---------- Helper: copy total row into BOTH ----------
	        java.util.function.Consumer<Integer> copyTotal = (row) -> {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;
	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter  = M_TBS_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        };

	        // 3️⃣ R11 = SUM(C12:C16) + C21
	        copyRows.accept(new int[] { 12, 13, 14, 15, 16, 21 });
	        copyTotal.accept(11);

	        // 4️⃣ R16 = SUM(C17:C20)
	        copyRows.accept(new int[] { 17, 18, 19, 20 });
	        copyTotal.accept(16);

	        // 5️⃣ R22 = SUM(C23:C27) + C33
	        copyRows.accept(new int[] { 23, 24, 25, 26, 27, 33 });
	        copyTotal.accept(22);

	        // 6️⃣ R27 = SUM(C28:C32)
	        copyRows.accept(new int[] { 28, 29, 30, 31, 32 });
	        copyTotal.accept(27);

	        // 7️⃣ R34 = C35 + C36 + C40
	        copyRows.accept(new int[] { 35, 36, 40 });
	        copyTotal.accept(34);

	        // 8️⃣ R36 = SUM(C37:C39)
	        copyRows.accept(new int[] { 37, 38, 39 });
	        copyTotal.accept(36);

	        // 9️⃣ R41 = C42 + C43 + C44 + C49
	        copyRows.accept(new int[] { 42, 43, 44, 49 });
	        copyTotal.accept(41);

	        // 🔟 R44 = SUM(C45:C48)
	        copyRows.accept(new int[] { 45, 46, 47, 48 });
	        copyTotal.accept(44);

	        // 1️⃣1️⃣ R50 = SUM(C51:C54)
	        copyRows.accept(new int[] { 51, 52, 53, 54 });
	        copyTotal.accept(50);

	        // 1️⃣2️⃣ R55 = C50 + C41 + C34 + C22 + C11
	        copyRows.accept(new int[] { 50, 41, 34, 22, 11 });
	        copyTotal.accept(55);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating M_TBS Summary & Detail report fields", e);
	    }

	    // 4️⃣ Save BOTH in same transaction
	    brrs_M_TBS_summary_repo.save(existingSummary);
	    brrs_M_TBS_detail_repo.save(existingDetail);
	}


	public void updateResubReport(M_TBS_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = M_TBS_Resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_TBS_Resub_Summary_Entity resubSummary = new M_TBS_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_TBS_Resub_Detail_Entity resubDetail = new M_TBS_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_TBS_Archival_Summary_Entity archSummary = new M_TBS_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_TBS_Archival_Detail_Entity archDetail = new M_TBS_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		M_TBS_Resub_Summary_Repo.save(resubSummary);
		M_TBS_Resub_Detail_Repo.save(resubDetail);

		M_TBS_Archival_Summary_Repo.save(archSummary);
		BRRS_M_TBS_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_TBSResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_TBS_Archival_Summary_Entity> latestArchivalList = M_TBS_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_TBSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_TBS_Archival_Summary_Entity> repoData = M_TBS_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(), entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_TBS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_TBSExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_TBSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_TBSResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_TBSEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_TBS_Summary_Entity> dataList = brrs_M_TBS_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
							M_TBS_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							/*
							 * 	private BigDecimal R11_NV_LONG;
										private BigDecimal R11_NV_SHORT;
										private BigDecimal R11_FV_LONG;
										private BigDecimal R11_FV_SHORT;
										private BigDecimal R11_QFHA;
							 */
							//row11
							// Column C 
							//row=sheet.getRow(11);

							Cell cell1 = row.getCell(2);
							if (record.getR11_NV_LONG() != null) {
								cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR11_NV_SHORT() != null) {
								cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

						    cell1 = row.getCell(4);
							if (record.getR11_FV_LONG() != null) {
								cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR11_FV_SHORT() != null) {
								cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR11_QFHA() != null) {
								cell1.setCellValue(record.getR11_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
		//------ROW11---------						
							row=sheet.getRow(11);
						    cell1 = row.getCell(2);
							if (record.getR12_NV_LONG() != null) {
								cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR12_NV_SHORT() != null) {
								cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR12_FV_LONG() != null) {
								cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR12_FV_SHORT() != null) {
								cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR12_QFHA() != null) {
								cell1.setCellValue(record.getR12_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							//------ROW12---------						
							row=sheet.getRow(12);
						    cell1 = row.getCell(2);
							if (record.getR13_NV_LONG() != null) {
								cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1=row.getCell(3);
							if (record.getR13_NV_SHORT() != null) {
								cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR13_FV_LONG() != null) {
								cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}		
								
								
							cell1 = row.getCell(5);
							if (record.getR13_FV_SHORT() != null) {
								cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	
							
							cell1 = row.getCell(6);
							if (record.getR13_QFHA() != null) {
								cell1.setCellValue(record.getR13_QFHA().doubleValue());
							
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}	

							//------ROW13---------
							row = sheet.getRow(13);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR14_NV_LONG() != null) {
							    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR14_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR14_FV_LONG() != null) {
							    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR14_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR14_QFHA() != null) {
							    cell1.setCellValue(record.getR14_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW14---------
							row = sheet.getRow(14);

							cell1 = row.getCell(2);
							if (record.getR15_NV_LONG() != null) {
							    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR15_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR15_FV_LONG() != null) {
							    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR15_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR15_QFHA() != null) {
							    cell1.setCellValue(record.getR15_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW15---------
							row = sheet.getRow(15);

							cell1 = row.getCell(2);
							if (record.getR16_NV_LONG() != null) {
							    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR16_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR16_FV_LONG() != null) {
							    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR16_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR16_QFHA() != null) {
							    cell1.setCellValue(record.getR16_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW16---------
							row = sheet.getRow(16);

							cell1 = row.getCell(2);
							if (record.getR17_NV_LONG() != null) {
							    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR17_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR17_FV_LONG() != null) {
							    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR17_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR17_QFHA() != null) {
							    cell1.setCellValue(record.getR17_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW17---------
							row = sheet.getRow(17);

							cell1 = row.getCell(2);
							if (record.getR18_NV_LONG() != null) {
							    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR18_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR18_FV_LONG() != null) {
							    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR18_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR18_QFHA() != null) {
							    cell1.setCellValue(record.getR18_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW18---------
							row = sheet.getRow(18);

							cell1 = row.getCell(2);
							if (record.getR19_NV_LONG() != null) {
							    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR19_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR19_FV_LONG() != null) {
							    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR19_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR19_QFHA() != null) {
							    cell1.setCellValue(record.getR19_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW19---------
							row = sheet.getRow(19);

							cell1 = row.getCell(2);
							if (record.getR20_NV_LONG() != null) {
							    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR20_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR20_FV_LONG() != null) {
							    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR20_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR20_QFHA() != null) {
							    cell1.setCellValue(record.getR20_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW20---------
							row = sheet.getRow(20);

							cell1 = row.getCell(2);
							if (record.getR21_NV_LONG() != null) {
							    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR21_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR21_FV_LONG() != null) {
							    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR21_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR21_QFHA() != null) {
							    cell1.setCellValue(record.getR21_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW21---------
							row = sheet.getRow(21);

							cell1 = row.getCell(2);
							if (record.getR22_NV_LONG() != null) {
							    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR22_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR22_FV_LONG() != null) {
							    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR22_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR22_QFHA() != null) {
							    cell1.setCellValue(record.getR22_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW22---------
							row = sheet.getRow(22);

							cell1 = row.getCell(2);
							if (record.getR23_NV_LONG() != null) {
							    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR23_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR23_FV_LONG() != null) {
							    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR23_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR23_QFHA() != null) {
							    cell1.setCellValue(record.getR23_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW23---------
							row = sheet.getRow(23);

							cell1 = row.getCell(2);
							if (record.getR24_NV_LONG() != null) {
							    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR24_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR24_FV_LONG() != null) {
							    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR24_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR24_QFHA() != null) {
							    cell1.setCellValue(record.getR24_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW24---------
							row = sheet.getRow(24);

							cell1 = row.getCell(2);
							if (record.getR25_NV_LONG() != null) {
							    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR25_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR25_FV_LONG() != null) {
							    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR25_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR25_QFHA() != null) {
							    cell1.setCellValue(record.getR25_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW25---------
							row = sheet.getRow(25);

							cell1 = row.getCell(2);
							if (record.getR26_NV_LONG() != null) {
							    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR26_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR26_FV_LONG() != null) {
							    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR26_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR26_QFHA() != null) {
							    cell1.setCellValue(record.getR26_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW26---------
							row = sheet.getRow(26);

							cell1 = row.getCell(2);
							if (record.getR27_NV_LONG() != null) {
							    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR27_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR27_FV_LONG() != null) {
							    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR27_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR27_QFHA() != null) {
							    cell1.setCellValue(record.getR27_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW27---------
							row = sheet.getRow(27);

							cell1 = row.getCell(2);
							if (record.getR28_NV_LONG() != null) {
							    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR28_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR28_FV_LONG() != null) {
							    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR28_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR28_QFHA() != null) {
							    cell1.setCellValue(record.getR28_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW28---------
							row = sheet.getRow(28);

							cell1 = row.getCell(2);
							if (record.getR29_NV_LONG() != null) {
							    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR29_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR29_FV_LONG() != null) {
							    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR29_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR29_QFHA() != null) {
							    cell1.setCellValue(record.getR29_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW29---------
							row = sheet.getRow(29);

							cell1 = row.getCell(2);
							if (record.getR30_NV_LONG() != null) {
							    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(3);
							if (record.getR30_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(4);
							if (record.getR30_FV_LONG() != null) {
							    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(5);
							if (record.getR30_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}

							cell1 = row.getCell(6);
							if (record.getR30_QFHA() != null) {
							    cell1.setCellValue(record.getR30_QFHA().doubleValue());
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							//------ROW30--------- R31
							row = sheet.getRow(30);

							// NV_LONG
							cell1 = row.getCell(2);
							if (record.getR31_NV_LONG() != null) {
							    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// NV_SHORT
							cell1 = row.getCell(3);
							if (record.getR31_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_LONG
							cell1 = row.getCell(4);
							if (record.getR31_FV_LONG() != null) {
							    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// FV_SHORT
							cell1 = row.getCell(5);
							if (record.getR31_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							// QFHA
							cell1 = row.getCell(6);
							if (record.getR31_QFHA() != null) {
							    cell1.setCellValue(record.getR31_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW31--------- R32
							row = sheet.getRow(31);

							cell1 = row.getCell(2);
							if (record.getR32_NV_LONG() != null) {
							    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR32_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR32_FV_LONG() != null) {
							    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR32_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR32_QFHA() != null) {
							    cell1.setCellValue(record.getR32_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW32--------- R33
							row = sheet.getRow(32);

							cell1 = row.getCell(2);
							if (record.getR33_NV_LONG() != null) {
							    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR33_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR33_FV_LONG() != null) {
							    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR33_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR33_QFHA() != null) {
							    cell1.setCellValue(record.getR33_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW33--------- R34
							row = sheet.getRow(33);

							cell1 = row.getCell(2);
							if (record.getR34_NV_LONG() != null) {
							    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR34_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR34_FV_LONG() != null) {
							    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR34_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR34_QFHA() != null) {
							    cell1.setCellValue(record.getR34_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW34--------- R35
							row = sheet.getRow(34);

							cell1 = row.getCell(2);
							if (record.getR35_NV_LONG() != null) {
							    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR35_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR35_FV_LONG() != null) {
							    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR35_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR35_QFHA() != null) {
							    cell1.setCellValue(record.getR35_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW35--------- R36
							row = sheet.getRow(35);

							cell1 = row.getCell(2);
							if (record.getR36_NV_LONG() != null) {
							    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR36_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR36_FV_LONG() != null) {
							    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR36_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR36_QFHA() != null) {
							    cell1.setCellValue(record.getR36_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW36--------- R37
							row = sheet.getRow(36);

							cell1 = row.getCell(2);
							if (record.getR37_NV_LONG() != null) {
							    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR37_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR37_FV_LONG() != null) {
							    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR37_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR37_QFHA() != null) {
							    cell1.setCellValue(record.getR37_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW37--------- R38
							row = sheet.getRow(37);

							cell1 = row.getCell(2);
							if (record.getR38_NV_LONG() != null) {
							    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR38_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR38_FV_LONG() != null) {
							    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR38_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR38_QFHA() != null) {
							    cell1.setCellValue(record.getR38_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW38--------- R39
							row = sheet.getRow(38);

							cell1 = row.getCell(2);
							if (record.getR39_NV_LONG() != null) {
							    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR39_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR39_FV_LONG() != null) {
							    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR39_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR39_QFHA() != null) {
							    cell1.setCellValue(record.getR39_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW39--------- R40
							row = sheet.getRow(39);

							cell1 = row.getCell(2);
							if (record.getR40_NV_LONG() != null) {
							    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR40_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR40_FV_LONG() != null) {
							    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR40_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR40_QFHA() != null) {
							    cell1.setCellValue(record.getR40_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							//------ROW40--------- R41
							row = sheet.getRow(40);

							cell1 = row.getCell(2);
							if (record.getR41_NV_LONG() != null) {
							    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR41_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR41_FV_LONG() != null) {
							    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR41_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR41_QFHA() != null) {
							    cell1.setCellValue(record.getR41_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW41--------- R42
							row = sheet.getRow(41);

							cell1 = row.getCell(2);
							if (record.getR42_NV_LONG() != null) {
							    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR42_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR42_FV_LONG() != null) {
							    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR42_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR42_QFHA() != null) {
							    cell1.setCellValue(record.getR42_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW42--------- R43
							row = sheet.getRow(42);

							cell1 = row.getCell(2);
							if (record.getR43_NV_LONG() != null) {
							    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR43_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR43_FV_LONG() != null) {
							    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR43_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR43_QFHA() != null) {
							    cell1.setCellValue(record.getR43_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW43--------- R44
							row = sheet.getRow(43);

							cell1 = row.getCell(2);
							if (record.getR44_NV_LONG() != null) {
							    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR44_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR44_FV_LONG() != null) {
							    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR44_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR44_QFHA() != null) {
							    cell1.setCellValue(record.getR44_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW44--------- R45
							row = sheet.getRow(44);

							cell1 = row.getCell(2);
							if (record.getR45_NV_LONG() != null) {
							    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR45_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR45_FV_LONG() != null) {
							    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR45_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR45_QFHA() != null) {
							    cell1.setCellValue(record.getR45_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW45--------- R46
							row = sheet.getRow(45);

							cell1 = row.getCell(2);
							if (record.getR46_NV_LONG() != null) {
							    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR46_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR46_FV_LONG() != null) {
							    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR46_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR46_QFHA() != null) {
							    cell1.setCellValue(record.getR46_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW46--------- R47
							row = sheet.getRow(46);

							cell1 = row.getCell(2);
							if (record.getR47_NV_LONG() != null) {
							    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR47_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR47_FV_LONG() != null) {
							    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR47_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR47_QFHA() != null) {
							    cell1.setCellValue(record.getR47_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW47--------- R48
							row = sheet.getRow(47);

							cell1 = row.getCell(2);
							if (record.getR48_NV_LONG() != null) {
							    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR48_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR48_FV_LONG() != null) {
							    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR48_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR48_QFHA() != null) {
							    cell1.setCellValue(record.getR48_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW48--------- R49
							row = sheet.getRow(48);

							cell1 = row.getCell(2);
							if (record.getR49_NV_LONG() != null) {
							    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR49_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR49_FV_LONG() != null) {
							    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR49_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR49_QFHA() != null) {
							    cell1.setCellValue(record.getR49_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



							//------ROW49--------- R50
							row = sheet.getRow(49);

							cell1 = row.getCell(2);
							if (record.getR50_NV_LONG() != null) {
							    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR50_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR50_FV_LONG() != null) {
							    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR50_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR50_QFHA() != null) {
							    cell1.setCellValue(record.getR50_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							//------ROW50--------- R51
							row = sheet.getRow(50);

							cell1 = row.getCell(2);
							if (record.getR51_NV_LONG() != null) {
							    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR51_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR51_FV_LONG() != null) {
							    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR51_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR51_QFHA() != null) {
							    cell1.setCellValue(record.getR51_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							
							
							//------ROW51--------- R52
							row = sheet.getRow(51);

							cell1 = row.getCell(2);
							if (record.getR52_NV_LONG() != null) {
							    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR52_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR52_FV_LONG() != null) {
							    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR52_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR52_QFHA() != null) {
							    cell1.setCellValue(record.getR52_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW52--------- R53
							row = sheet.getRow(52);

							cell1 = row.getCell(2);
							if (record.getR53_NV_LONG() != null) {
							    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR53_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR53_FV_LONG() != null) {
							    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR53_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR53_QFHA() != null) {
							    cell1.setCellValue(record.getR53_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW53--------- R54
							row = sheet.getRow(53);

							cell1 = row.getCell(2);
							if (record.getR54_NV_LONG() != null) {
							    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR54_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR54_FV_LONG() != null) {
							    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR54_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR54_QFHA() != null) {
							    cell1.setCellValue(record.getR54_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							
							//------ROW54--------- R55
							row = sheet.getRow(54);

							cell1 = row.getCell(2);
							if (record.getR55_NV_LONG() != null) {
							    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(3);
							if (record.getR55_NV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(4);
							if (record.getR55_FV_LONG() != null) {
							    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(5);
							if (record.getR55_FV_SHORT() != null) {
							    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

							cell1 = row.getCell(6);
							if (record.getR55_QFHA() != null) {
							    cell1.setCellValue(record.getR55_QFHA().doubleValue());
							} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	public byte[] BRRS_M_TBSEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_TBSArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_TBSResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		else {
		List<M_TBS_Summary_Entity> dataList = brrs_M_TBS_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
					M_TBS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					/*
					 * 	private BigDecimal R11_NV_LONG;
								private BigDecimal R11_NV_SHORT;
								private BigDecimal R11_FV_LONG;
								private BigDecimal R11_FV_SHORT;
								private BigDecimal R11_QFHA;
					 */
					//row11
					// Column C 
					//row=sheet.getRow(11);

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	public byte[] getExcelM_TBSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_TBSArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 

		List<M_TBS_Archival_Summary_Entity> dataList = M_TBS_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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
					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					/*
					 * 	private BigDecimal R11_NV_LONG;
								private BigDecimal R11_NV_SHORT;
								private BigDecimal R11_FV_LONG;
								private BigDecimal R11_FV_SHORT;
								private BigDecimal R11_QFHA;
					 */
					//row11
					// Column C 
					//row=sheet.getRow(11);

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	public byte[] BRRS_M_TBSArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_TBS_Archival_Summary_Entity> dataList = M_TBS_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					/*
					 * 	private BigDecimal R11_NV_LONG;
								private BigDecimal R11_NV_SHORT;
								private BigDecimal R11_FV_LONG;
								private BigDecimal R11_FV_SHORT;
								private BigDecimal R11_QFHA;
					 */
					//row11
					// Column C 
					//row=sheet.getRow(11);

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	public byte[] BRRS_M_TBSResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_TBSResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		List<M_TBS_Resub_Summary_Entity> dataList = M_TBS_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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

					M_TBS_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					/*
					 * 	private BigDecimal R11_NV_LONG;
								private BigDecimal R11_NV_SHORT;
								private BigDecimal R11_FV_LONG;
								private BigDecimal R11_FV_SHORT;
								private BigDecimal R11_QFHA;
					 */
					//row11
					// Column C 
					//row=sheet.getRow(11);

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	public byte[] BRRS_M_TBSResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_TBS_Resub_Summary_Entity> dataList = M_TBS_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_TBS report. Returning empty result.");
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
					M_TBS_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					/*
					 * 	private BigDecimal R11_NV_LONG;
								private BigDecimal R11_NV_SHORT;
								private BigDecimal R11_FV_LONG;
								private BigDecimal R11_FV_SHORT;
								private BigDecimal R11_QFHA;
					 */
					//row11
					// Column C 
					//row=sheet.getRow(11);

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

}				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

}