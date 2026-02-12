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
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
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

import com.bornfire.brrs.entities.BRRS_M_GMIRT_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GMIRT_Summary_Repo;
import com.bornfire.brrs.entities.M_CA4_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA7_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA7_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA7_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_CA7_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Detail_Entity;
import com.bornfire.brrs.entities.M_GMIRT_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_GMIRT_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_GMIRT_Summary_Entity;
import com.bornfire.brrs.entities.M_GP_Archival_Summary_Entity;

@Component
@Service

public class BRRS_M_GMIRT_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GMIRT_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_GMIRT_Detail_Repo brrs_m_gmirt_detail_repo;

	@Autowired
	BRRS_M_GMIRT_Summary_Repo brrs_m_gmirt_summary_repo;

	@Autowired
	BRRS_M_GMIRT_Detail_Repo m_gmirt_Detail_Repo;

	@Autowired
	BRRS_M_GMIRT_Archival_Detail_Repo m_gmirt_Archival_Detail_Repo;

	@Autowired
	BRRS_M_GMIRT_Archival_Summary_Repo brrs_m_gmirt_Archival_summary_repo;

	@Autowired
	BRRS_M_GMIRT_RESUB_Summary_Repo BRRS_M_GMIRT_resub_Summary_Repo;

	@Autowired
	BRRS_M_GMIRT_RESUB_Detail_Repo BRRS_M_GMIRT_resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_GMIRTView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ---------- CASE 1: ARCHIVAL ----------
			if (type.equals("ARCHIVAL") & version != null) {

				List<M_GMIRT_Archival_Summary_Entity> T1Master = brrs_m_gmirt_Archival_summary_repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_GMIRT_RESUB_Summary_Entity> T1Master = BRRS_M_GMIRT_resub_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}
			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_GMIRT_Summary_Entity> T1Master = brrs_m_gmirt_summary_repo
						.getdatabydateList(dateformat.parse(todate));
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");

			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					
					List<M_GMIRT_Archival_Detail_Entity> T1Master = m_gmirt_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_GMIRT_RESUB_Detail_Entity> T1Master = BRRS_M_GMIRT_resub_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_GMIRT_Detail_Entity> T1Master = m_gmirt_Detail_Repo
							.getdatabydateList(dateformat.parse(todate));
					
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_GMIRT");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}


	public void updateReport(M_GMIRT_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_GMIRT_Summary_Entity existing = brrs_m_gmirt_summary_repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// 1️⃣ Loop from R11 to R23 and copy fields

			for (int i = 9; i <= 12; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "currency", "pula", "usd", "zar", "gbp", "euro", "jpy", "rupee", "renminbi",
						"other", "tot_cap_req" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_GMIRT_Summary_Entity.class.getMethod(getterName);
						Method setter = M_GMIRT_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue; // skip field that does not exist
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		brrs_m_gmirt_summary_repo.save(existing);
	}

	public void updateDetail(M_GMIRT_Detail_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_GMIRT_Detail_Entity existing = m_gmirt_Detail_Repo.findById(updatedEntity.getReport_date()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// 1️⃣ Loop from R11 to R23 and copy fields

			for (int i = 9; i <= 12; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "currency", "pula", "usd", "zar", "gbp", "euro", "jpy", "rupee", "renminbi",
						"other", "tot_cap_req" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_GMIRT_Detail_Entity.class.getMethod(getterName);
						Method setter = M_GMIRT_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue; // skip field that does not exist
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		m_gmirt_Detail_Repo.save(existing);
	}


	public void updateResubReport(M_GMIRT_RESUB_Summary_Entity updatedEntity) {


		Date reportDate = updatedEntity.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = BRRS_M_GMIRT_resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_GMIRT_RESUB_Summary_Entity resubSummary = new M_GMIRT_RESUB_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_GMIRT_RESUB_Detail_Entity resubDetail = new M_GMIRT_RESUB_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_GMIRT_Archival_Summary_Entity archSummary = new M_GMIRT_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_GMIRT_Archival_Detail_Entity archDetail = new M_GMIRT_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		BRRS_M_GMIRT_resub_Summary_Repo.save(resubSummary);
		BRRS_M_GMIRT_resub_Detail_Repo.save(resubDetail);

		brrs_m_gmirt_Archival_summary_repo.save(archSummary);
		m_gmirt_Archival_Detail_Repo.save(archDetail);
	}

	//RESUB VIEW
	public List<Object[]> getM_GMIRTResub() {

		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_GMIRT_RESUB_Summary_Entity> latestArchivalList = BRRS_M_GMIRT_resub_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_GMIRT_RESUB_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_GMIRT Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	//Archival View
	public List<Object[]> getM_GMIRTArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_GMIRT_Archival_Summary_Entity> repoData = brrs_m_gmirt_Archival_summary_repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_GMIRT_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(), 
							 entity.getReportResubDate()
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_GMIRT_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_GMIRT  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	//Normal Format Excel
	public byte[] getM_GMIRTExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_GMIRTARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return getExcelM_GMIRTResub(filename, reportId, fromdate, todate, currency, dtltype, type, format, version);
				
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		 else {
			 if ("email".equalsIgnoreCase(format) && version == null) {
					logger.info("Got format as Email");
					logger.info("Service: Generating Email report for version {}", version);
					return BRRS_M_GMIRTEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {


		

		List<M_GMIRT_Summary_Entity> dataList = brrs_m_gmirt_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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
	 }}

	//Normal Email Excel
	public byte[] BRRS_M_GMIRTEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// Email check

		// Fetch data

		List<M_GMIRT_Summary_Entity> dataList = brrs_m_gmirt_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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

	//Archival Format Excel
	public byte[] getExcelM_GMIRTARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format,BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");
		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_GMIRTARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		List<M_GMIRT_Archival_Summary_Entity> dataList = brrs_m_gmirt_Archival_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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

	//Archival Email Excel
	public byte[] BRRS_M_GMIRTARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		List<M_GMIRT_Archival_Summary_Entity> dataList = brrs_m_gmirt_Archival_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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

	//RESUB Format Excel
	public byte[] getExcelM_GMIRTResub(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_GMIRTResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		List<M_GMIRT_RESUB_Summary_Entity> dataList = BRRS_M_GMIRT_resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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

	//RESUB Email Excel
	public byte[] BRRS_M_GMIRTResubEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			List<M_GMIRT_RESUB_Summary_Entity> dataList = BRRS_M_GMIRT_resub_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_GMIRT_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row9
						// Column E
						Cell cellE = row.createCell(4);
						if (record.getR9_pula() != null) {
							cellE.setCellValue(record.getR9_pula().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row9
						// Column F
						Cell cellF = row.createCell(5);
						if (record.getR9_usd() != null) {
							cellF.setCellValue(record.getR9_usd().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row9
						// Column G
						Cell cellG = row.createCell(6);
						if (record.getR9_zar() != null) {
							cellG.setCellValue(record.getR9_zar().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row9
						// Column H
						Cell cellH = row.createCell(7);
						if (record.getR9_gbp() != null) {
							cellH.setCellValue(record.getR9_gbp().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row9
						// Column I
						Cell cellI = row.createCell(8);
						if (record.getR9_euro() != null) {
							cellI.setCellValue(record.getR9_euro().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row9
						// Column J
						Cell cellJ = row.createCell(9);
						if (record.getR9_jpy() != null) {
							cellJ.setCellValue(record.getR9_jpy().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row9
						// Column K
						Cell cellK = row.createCell(10);
						if (record.getR9_rupee() != null) {
							cellK.setCellValue(record.getR9_rupee().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row9
						// Column L
						Cell cellL = row.createCell(11);
						if (record.getR9_renminbi() != null) {
							cellL.setCellValue(record.getR9_renminbi().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// row9
						// Column M
						Cell cellM = row.createCell(12);
						if (record.getR9_other() != null) {
							cellM.setCellValue(record.getR9_other().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// row9
						// Column N
						Cell cellN = row.createCell(13);
						if (record.getR9_tot_cap_req() != null) {
							cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// row10

						// Column E
						row = sheet.getRow(9);
						cellE = row.createCell(4);
						if (record.getR10_pula() != null) {
							cellE.setCellValue(record.getR10_pula().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row10
						// Column F
						cellF = row.createCell(5);
						if (record.getR10_usd() != null) {
							cellF.setCellValue(record.getR10_usd().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row10
						// Column G
						cellG = row.createCell(6);
						if (record.getR10_zar() != null) {
							cellG.setCellValue(record.getR10_zar().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row10
						// Column H
						cellH = row.createCell(7);
						if (record.getR10_gbp() != null) {
							cellH.setCellValue(record.getR10_gbp().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row10
						// Column I
						cellI = row.createCell(8);
						if (record.getR10_euro() != null) {
							cellI.setCellValue(record.getR10_euro().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row10
						// Column J
						cellJ = row.createCell(9);
						if (record.getR10_jpy() != null) {
							cellJ.setCellValue(record.getR10_jpy().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row10
						// Column K
						cellK = row.createCell(10);
						if (record.getR10_rupee() != null) {
							cellK.setCellValue(record.getR10_rupee().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row10
						// Column L
						cellL = row.createCell(11);
						if (record.getR10_renminbi() != null) {
							cellL.setCellValue(record.getR10_renminbi().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// row10
						// Column M
						cellM = row.createCell(12);
						if (record.getR10_other() != null) {
							cellM.setCellValue(record.getR10_other().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// row10
						// Column N
						cellN = row.createCell(13);
						if (record.getR10_tot_cap_req() != null) {
							cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// row11
						// Column E
						row = sheet.getRow(10);
						cellE = row.createCell(4);
						if (record.getR11_pula() != null) {
							cellE.setCellValue(record.getR11_pula().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row11
						// Column F
						cellF = row.createCell(5);
						if (record.getR11_usd() != null) {
							cellF.setCellValue(record.getR11_usd().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row11
						// Column G
						cellG = row.createCell(6);
						if (record.getR11_zar() != null) {
							cellG.setCellValue(record.getR11_zar().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row11
						// Column H
						cellH = row.createCell(7);
						if (record.getR11_gbp() != null) {
							cellH.setCellValue(record.getR11_gbp().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row11
						// Column I
						cellI = row.createCell(8);
						if (record.getR11_euro() != null) {
							cellI.setCellValue(record.getR11_euro().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row11
						// Column J
						cellJ = row.createCell(9);
						if (record.getR11_jpy() != null) {
							cellJ.setCellValue(record.getR11_jpy().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row11
						// Column K
						cellK = row.createCell(10);
						if (record.getR11_rupee() != null) {
							cellK.setCellValue(record.getR11_rupee().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row11
						// Column L
						cellL = row.createCell(11);
						if (record.getR11_renminbi() != null) {
							cellL.setCellValue(record.getR11_renminbi().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// row11
						// Column M
						cellM = row.createCell(12);
						if (record.getR11_other() != null) {
							cellM.setCellValue(record.getR11_other().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// row11
						// Column N
						cellN = row.createCell(13);
						if (record.getR11_tot_cap_req() != null) {
							cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
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