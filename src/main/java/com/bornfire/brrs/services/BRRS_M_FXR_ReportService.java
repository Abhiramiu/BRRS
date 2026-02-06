package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.bornfire.brrs.entities.M_FXR_Summary_Entity;
import com.bornfire.brrs.entities.M_FXR_Detail_Entity;
import com.bornfire.brrs.entities.M_FXR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_FXR_Summary_Repo;
import com.bornfire.brrs.entities.M_FXR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Detail_Repo;

@Component
@Service
public class BRRS_M_FXR_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_FXR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_FXR_Summary_Repo BRRS_M_FXR_Summary_Repo;

	@Autowired
	BRRS_M_FXR_Archival_Summary_Repo BRRS_M_FXR_Archival_Summary_Repo;

	@Autowired
	BRRS_M_FXR_Detail_Repo M_FXR_Detail_Repo;

	@Autowired
	BRRS_M_FXR_Archival_Detail_Repo M_FXR_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_FXRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_FXR_Archival_Summary_Entity> T1Master = BRRS_M_FXR_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_FXR_Archival_Summary_Entity> T1Master = BRRS_M_FXR_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_FXR_Summary_Entity> T1Master = BRRS_M_FXR_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				mv.addObject("displaymode", "summary");
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if (version != null) {
					List<M_FXR_Archival_Detail_Entity> T1Master = M_FXR_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_FXR_Detail_Entity> T1Master = M_FXR_Detail_Repo.getdatabydateList(dateformat.parse(todate));
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_FXR");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	public void updateReport1(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = BRRS_M_FXR_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = M_FXR_Detail_Repo.findById(updatedEntity.getReportDate()).orElseGet(() -> {
			M_FXR_Detail_Entity d = new M_FXR_Detail_Entity();
			d.setReportDate(updatedEntity.getReportDate());
			return d;
		});

		try {
			// 1️⃣ Loop from R11 to R16 and copy fields
			for (int i = 11; i <= 16; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "net_spot_position", "net_forward_position", "guarantees", "net_future_inc_or_exp",
						"net_delta_wei_fx_opt_posi", "other_items", "net_long_position", "or", "net_short_position" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R17 totals
			String[] totalFields = { "net_long_position", "net_short_position" };
			for (String field : totalFields) {
				String getterName = "getR17_" + field;
				String setterName = "setR17_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip if not present
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		BRRS_M_FXR_Summary_Repo.save(existingSummary);
		M_FXR_Detail_Repo.save(detailEntity);
	}

	public void updateReport2(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services2");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = BRRS_M_FXR_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = M_FXR_Detail_Repo.findById(updatedEntity.getReportDate()).orElseGet(() -> {
			M_FXR_Detail_Entity d = new M_FXR_Detail_Entity();
			d.setReportDate(updatedEntity.getReportDate());
			return d;
		});

		try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 21; i <= 22; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "long", "short" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
				String[] formulaFields = { "total_gross_long_short", "net_position" };
				for (String field : formulaFields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// 2️⃣ Handle R23 totals
			String getterName = "getR23_net_position";
			String setterName = "setR23_net_position";

			try {
				// Getter from UPDATED entity
				Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

				Object newValue = getter.invoke(updatedEntity);

				// SUMMARY setter
				Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

				summarySetter.invoke(existingSummary, newValue);

				// DETAIL setter
				Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

				detailSetter.invoke(detailEntity, newValue);

			} catch (NoSuchMethodException e) {
				// Skip if not present
				// continue;
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		BRRS_M_FXR_Summary_Repo.save(existingSummary);
		M_FXR_Detail_Repo.save(detailEntity);
	}

	public void updateReport3(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services3");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = BRRS_M_FXR_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = M_FXR_Detail_Repo.findById(updatedEntity.getReportDate()).orElseGet(() -> {
			M_FXR_Detail_Entity d = new M_FXR_Detail_Entity();
			d.setReportDate(updatedEntity.getReportDate());
			return d;
		});

		try {

			String[] fields = { "greater_net_long_or_short", "abs_value_net_gold_posi", "capital_charge" };

			for (String field : fields) {
				String getterName = "getR29_" + field;
				String setterName = "setR29_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}

			String getterName = "getR30_capital_require";
			String setterName = "setR30_capital_require";

			try {
				// Getter from UPDATED entity
				Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

				Object newValue = getter.invoke(updatedEntity);

				// SUMMARY setter
				Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

				summarySetter.invoke(existingSummary, newValue);

				// DETAIL setter
				Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

				detailSetter.invoke(detailEntity, newValue);

			} catch (NoSuchMethodException e) {
				// Skip if not present
				// continue;
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		BRRS_M_FXR_Summary_Repo.save(existingSummary);
		M_FXR_Detail_Repo.save(detailEntity);
	}

	public byte[] getM_FXRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_FXRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// RESUB check
//	        else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
//	            logger.info("Service: Generating RESUB report for version {}", version);
//
//	            List<M_FXR_Archival_Summary_Entity> T1Master1 = BRRS_M_FXR_Archival_Summary_Repo
//	                    .getdatabydateListarchival(reportDate, version);
//
//	            // Generate Excel for RESUB
//	            return BRRS_M_FXRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
//	        }
		// Email check
		if ("email".equalsIgnoreCase(type) && version == null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_FXREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("email".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_FXREmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}
		List<M_FXR_Summary_Entity> dataList = BRRS_M_FXR_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty() || dataList.isEmpty() || dataList.isEmpty()) {
			logger.warn("Service: No data found for brrs2.4 report. Returning empty result.");
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

			if (!dataList.isEmpty() || !dataList.isEmpty() || !dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR11_currency() != null)
//					cell1.setCellValue(record.getR11_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR11_net_spot_position() != null)
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR11_net_forward_position() != null)
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_guarantees() != null)
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR11_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR11_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR11_other_items() != null)
						cell7.setCellValue(record.getR11_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR11_net_long_position() != null)
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR11_or() != null)
						cell9.setCellValue(record.getR11_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR11_net_short_position() != null)
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R12 / Col B =====
					row = sheet.getRow(11);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR12_currency() != null)
//					cell1.setCellValue(record.getR12_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR12_net_spot_position() != null)
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR12_net_forward_position() != null)
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR12_guarantees() != null)
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR12_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR12_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR12_other_items() != null)
						cell7.setCellValue(record.getR12_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR12_net_long_position() != null)
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR12_or() != null)
						cell9.setCellValue(record.getR12_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR12_net_short_position() != null)
						cell10.setCellValue(record.getR12_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R13 / Col B =====
					row = sheet.getRow(12);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR13_currency() != null)
//					cell1.setCellValue(record.getR13_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R13 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR13_net_spot_position() != null)
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R13 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_net_forward_position() != null)
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R13 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR13_guarantees() != null)
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R13 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR13_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R13 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR13_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R13 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR13_other_items() != null)
						cell7.setCellValue(record.getR13_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R13 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR13_net_long_position() != null)
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R13 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR13_or() != null)
						cell9.setCellValue(record.getR13_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R13 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR13_net_short_position() != null)
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R14 / Col B =====
					row = sheet.getRow(13);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR14_currency() != null)
//					cell1.setCellValue(record.getR14_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R14 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR14_net_spot_position() != null)
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R14 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_net_forward_position() != null)
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R14 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR14_guarantees() != null)
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R14 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR14_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R14 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR14_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R14 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR14_other_items() != null)
						cell7.setCellValue(record.getR14_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R14 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR14_net_long_position() != null)
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R14 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR14_or() != null)
						cell9.setCellValue(record.getR14_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R14 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR14_net_short_position() != null)
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R15 / Col B =====
					row = sheet.getRow(14);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR15_currency() != null)
//					cell1.setCellValue(record.getR15_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R15 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR15_net_spot_position() != null)
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R15 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_net_forward_position() != null)
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R15 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR15_guarantees() != null)
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R15 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR15_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R15 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR15_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R15 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR15_other_items() != null)
						cell7.setCellValue(record.getR15_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R15 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR15_net_long_position() != null)
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R15 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR15_or() != null)
						cell9.setCellValue(record.getR15_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R15 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR15_net_short_position() != null)
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R16 / Col B =====
					row = sheet.getRow(15);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR16_currency() != null)
//					cell1.setCellValue(record.getR16_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R16 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR16_net_spot_position() != null)
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R16 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_net_forward_position() != null)
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R16 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR16_guarantees() != null)
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R16 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR16_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R16 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR16_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R16 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR16_other_items() != null)
						cell7.setCellValue(record.getR16_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R16 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR16_net_long_position() != null)
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R16 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR16_or() != null)
						cell9.setCellValue(record.getR16_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R16 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR16_net_short_position() != null)
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== R21 / Col G =====
					row = sheet.getRow(20);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR21_long() != null)
						cell6.setCellValue(record.getR21_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R21 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR21_short() != null)
						cell7.setCellValue(record.getR21_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R22 / Col G =====
					row = sheet.getRow(21);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR22_long() != null)
						cell6.setCellValue(record.getR22_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R22 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR22_short() != null)
						cell7.setCellValue(record.getR22_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell8;
					CellStyle originalStyle;

					// ===== R30 / Col I =====
					row = sheet.getRow(29);
					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();

					if (record.getR30_capital_require() != null)
						cell8.setCellValue(record.getR30_capital_require().doubleValue());
					else {
						cell8.setCellValue("");
					}

					// Keep the same style (make sure your template cell is formatted as Percentage)
					cell8.setCellStyle(originalStyle);

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

	public byte[] getExcelM_FXRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
		List<M_FXR_Archival_Summary_Entity> dataList = BRRS_M_FXR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_FXR report. Returning empty result.");
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

			if (!dataList.isEmpty() || !dataList.isEmpty() || !dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;

					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR11_currency() != null)
//					cell1.setCellValue(record.getR11_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR11_net_spot_position() != null)
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR11_net_forward_position() != null)
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_guarantees() != null)
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR11_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR11_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR11_other_items() != null)
						cell7.setCellValue(record.getR11_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR11_net_long_position() != null)
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR11_or() != null)
						cell9.setCellValue(record.getR11_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR11_net_short_position() != null)
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R12 / Col B =====
					row = sheet.getRow(11);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR12_currency() != null)
//					cell1.setCellValue(record.getR12_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR12_net_spot_position() != null)
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR12_net_forward_position() != null)
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR12_guarantees() != null)
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR12_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR12_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR12_other_items() != null)
						cell7.setCellValue(record.getR12_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR12_net_long_position() != null)
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR12_or() != null)
						cell9.setCellValue(record.getR12_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR12_net_short_position() != null)
						cell10.setCellValue(record.getR12_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R13 / Col B =====
					row = sheet.getRow(12);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR13_currency() != null)
//					cell1.setCellValue(record.getR13_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R13 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR13_net_spot_position() != null)
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R13 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_net_forward_position() != null)
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R13 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR13_guarantees() != null)
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R13 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR13_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R13 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR13_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R13 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR13_other_items() != null)
						cell7.setCellValue(record.getR13_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R13 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR13_net_long_position() != null)
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R13 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR13_or() != null)
						cell9.setCellValue(record.getR13_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R13 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR13_net_short_position() != null)
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R14 / Col B =====
					row = sheet.getRow(13);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR14_currency() != null)
//					cell1.setCellValue(record.getR14_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R14 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR14_net_spot_position() != null)
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R14 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_net_forward_position() != null)
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R14 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR14_guarantees() != null)
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R14 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR14_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R14 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR14_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R14 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR14_other_items() != null)
						cell7.setCellValue(record.getR14_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R14 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR14_net_long_position() != null)
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R14 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR14_or() != null)
						cell9.setCellValue(record.getR14_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R14 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR14_net_short_position() != null)
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R15 / Col B =====
					row = sheet.getRow(14);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR15_currency() != null)
//					cell1.setCellValue(record.getR15_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R15 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR15_net_spot_position() != null)
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R15 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_net_forward_position() != null)
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R15 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR15_guarantees() != null)
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R15 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR15_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R15 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR15_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R15 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR15_other_items() != null)
						cell7.setCellValue(record.getR15_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R15 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR15_net_long_position() != null)
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R15 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR15_or() != null)
						cell9.setCellValue(record.getR15_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R15 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR15_net_short_position() != null)
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R16 / Col B =====
					row = sheet.getRow(15);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record.getR16_currency() != null)
//					cell1.setCellValue(record.getR16_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

					// ===== R16 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR16_net_spot_position() != null)
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R16 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_net_forward_position() != null)
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R16 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR16_guarantees() != null)
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R16 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR16_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R16 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR16_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R16 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR16_other_items() != null)
						cell7.setCellValue(record.getR16_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R16 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR16_net_long_position() != null)
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R16 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR16_or() != null)
						cell9.setCellValue(record.getR16_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R16 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR16_net_short_position() != null)
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== R21 / Col G =====
					row = sheet.getRow(20);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR21_long() != null)
						cell6.setCellValue(record.getR21_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R21 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR21_short() != null)
						cell7.setCellValue(record.getR21_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R22 / Col G =====
					row = sheet.getRow(21);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR22_long() != null)
						cell6.setCellValue(record.getR22_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R22 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR22_short() != null)
						cell7.setCellValue(record.getR22_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell8;
					CellStyle originalStyle;

					// ===== R30 / Col I =====
					row = sheet.getRow(29);
					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();

					if (record.getR30_capital_require() != null)
						cell8.setCellValue(record.getR30_capital_require().doubleValue());
					else {
						cell8.setCellValue("");
					}

					// Keep the same style (make sure your template cell is formatted as Percentage)
					cell8.setCellStyle(originalStyle);

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

	////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
	/// Report Date | Report Version | Domain
	/// RESUB VIEW

	public List<Object[]> getM_FXRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_FXR_Archival_Summary_Entity> latestArchivalList = BRRS_M_FXR_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_FXR_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_FXR Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_FXRArchival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_FXR_Archival_Summary_Entity> latestArchivalList = BRRS_M_FXR_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_FXR_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_FXR Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	// Email Download
	public byte[] BRRS_M_FXREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<M_FXR_Summary_Entity> dataList = BRRS_M_FXR_Summary_Repo.getdatabydateList(reportDate);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forM_SRWA_12H report. Returning empty result.");
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

			if (!dataList.isEmpty() || !dataList.isEmpty() || !dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR11_currency() != null)
//    					cell1.setCellValue(record.getR11_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR11_net_spot_position() != null)
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR11_net_forward_position() != null)
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_guarantees() != null)
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR11_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR11_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR11_other_items() != null)
						cell7.setCellValue(record.getR11_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR11_net_long_position() != null)
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR11_or() != null)
						cell9.setCellValue(record.getR11_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR11_net_short_position() != null)
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R12 / Col B =====
					row = sheet.getRow(11);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR12_currency() != null)
//    					cell1.setCellValue(record.getR12_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR12_net_spot_position() != null)
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR12_net_forward_position() != null)
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR12_guarantees() != null)
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR12_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR12_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR12_other_items() != null)
						cell7.setCellValue(record.getR12_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR12_net_long_position() != null)
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR12_or() != null)
						cell9.setCellValue(record.getR12_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR12_net_short_position() != null)
						cell10.setCellValue(record.getR12_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R13 / Col B =====
					row = sheet.getRow(12);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR13_currency() != null)
//    					cell1.setCellValue(record.getR13_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R13 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR13_net_spot_position() != null)
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R13 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_net_forward_position() != null)
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R13 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR13_guarantees() != null)
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R13 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR13_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R13 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR13_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R13 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR13_other_items() != null)
						cell7.setCellValue(record.getR13_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R13 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR13_net_long_position() != null)
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R13 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR13_or() != null)
						cell9.setCellValue(record.getR13_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R13 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR13_net_short_position() != null)
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R14 / Col B =====
					row = sheet.getRow(13);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR14_currency() != null)
//    					cell1.setCellValue(record.getR14_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R14 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR14_net_spot_position() != null)
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R14 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_net_forward_position() != null)
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R14 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR14_guarantees() != null)
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R14 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR14_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R14 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR14_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R14 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR14_other_items() != null)
						cell7.setCellValue(record.getR14_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R14 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR14_net_long_position() != null)
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R14 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR14_or() != null)
						cell9.setCellValue(record.getR14_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R14 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR14_net_short_position() != null)
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R15 / Col B =====
					row = sheet.getRow(14);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR15_currency() != null)
//    					cell1.setCellValue(record.getR15_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R15 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR15_net_spot_position() != null)
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R15 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_net_forward_position() != null)
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R15 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR15_guarantees() != null)
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R15 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR15_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R15 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR15_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R15 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR15_other_items() != null)
						cell7.setCellValue(record.getR15_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R15 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR15_net_long_position() != null)
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R15 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR15_or() != null)
						cell9.setCellValue(record.getR15_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R15 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR15_net_short_position() != null)
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R16 / Col B =====
					row = sheet.getRow(15);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR16_currency() != null)
//    					cell1.setCellValue(record.getR16_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R16 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR16_net_spot_position() != null)
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R16 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_net_forward_position() != null)
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R16 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR16_guarantees() != null)
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R16 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR16_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R16 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR16_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R16 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR16_other_items() != null)
						cell7.setCellValue(record.getR16_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R16 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR16_net_long_position() != null)
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R16 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR16_or() != null)
						cell9.setCellValue(record.getR16_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R16 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR16_net_short_position() != null)
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== R21 / Col G =====
					row = sheet.getRow(20);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR21_long() != null)
						cell6.setCellValue(record.getR21_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R21 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR21_short() != null)
						cell7.setCellValue(record.getR21_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR21_total_gross_long_short() != null)
						cell8.setCellValue(record.getR21_total_gross_long_short().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR21_net_position() != null)
						cell9.setCellValue(record.getR21_net_position().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);
					// ===== R22 / Col G =====
					row = sheet.getRow(21);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR22_long() != null)
						cell6.setCellValue(record.getR22_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R22 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR22_short() != null)
						cell7.setCellValue(record.getR22_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR22_total_gross_long_short() != null)
						cell8.setCellValue(record.getR22_total_gross_long_short().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR22_net_position() != null)
						cell9.setCellValue(record.getR22_net_position().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));
					Row row = sheet.getRow(22);

					Cell R28cell22 = row.createCell(9);
					if (record.getR23_net_position() != null) {
						R28cell22.setCellValue(record.getR23_net_position().doubleValue());
						R28cell22.setCellStyle(numberStyle);
					} else {
						R28cell22.setCellValue("");
						R28cell22.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);

					Cell R28cell = row.createCell(6);
					if (record.getR29_greater_net_long_or_short() != null) {
						R28cell.setCellValue(record.getR29_greater_net_long_or_short().doubleValue());
						R28cell.setCellStyle(numberStyle);
					} else {
						R28cell.setCellValue("");
						R28cell.setCellStyle(textStyle);
					}
					Cell R28cell1 = row.createCell(7);
					if (record.getR29_abs_value_net_gold_posi() != null) {
						R28cell1.setCellValue(record.getR29_abs_value_net_gold_posi().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					Cell R28cell3 = row.createCell(9);
					if (record.getR29_capital_charge() != null) {
						R28cell3.setCellValue(record.getR29_capital_charge().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// ===== R30 / Col I =====
					row = sheet.getRow(29);

					// ✅ CRITICAL FIX
					if (row == null) {
						row = sheet.createRow(29);
					}

					Cell cell8 = row.getCell(8);
					if (cell8 == null) {
						cell8 = row.createCell(8);
					}

					if (record.getR30_capital_require() != null) {
						cell8.setCellValue(record.getR30_capital_require().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
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

	// Archival download for email
	public byte[] BRRS_M_FXREmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<M_FXR_Archival_Summary_Entity> dataList = BRRS_M_FXR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forM_SRWA_12H report. Returning empty result.");
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

			if (!dataList.isEmpty() || !dataList.isEmpty() || !dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== Row 11 / Col B =====
					row = sheet.getRow(10);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR11_currency() != null)
//    					cell1.setCellValue(record.getR11_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR11_net_spot_position() != null)
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR11_net_forward_position() != null)
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_guarantees() != null)
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR11_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR11_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR11_other_items() != null)
						cell7.setCellValue(record.getR11_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR11_net_long_position() != null)
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR11_or() != null)
						cell9.setCellValue(record.getR11_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR11_net_short_position() != null)
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R12 / Col B =====
					row = sheet.getRow(11);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR12_currency() != null)
//    					cell1.setCellValue(record.getR12_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR12_net_spot_position() != null)
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR12_net_forward_position() != null)
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR12_guarantees() != null)
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR12_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR12_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR12_other_items() != null)
						cell7.setCellValue(record.getR12_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR12_net_long_position() != null)
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR12_or() != null)
						cell9.setCellValue(record.getR12_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR12_net_short_position() != null)
						cell10.setCellValue(record.getR12_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R13 / Col B =====
					row = sheet.getRow(12);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR13_currency() != null)
//    					cell1.setCellValue(record.getR13_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R13 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR13_net_spot_position() != null)
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R13 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_net_forward_position() != null)
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R13 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR13_guarantees() != null)
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R13 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR13_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R13 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR13_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R13 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR13_other_items() != null)
						cell7.setCellValue(record.getR13_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R13 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR13_net_long_position() != null)
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R13 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR13_or() != null)
						cell9.setCellValue(record.getR13_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R13 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR13_net_short_position() != null)
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R14 / Col B =====
					row = sheet.getRow(13);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR14_currency() != null)
//    					cell1.setCellValue(record.getR14_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R14 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR14_net_spot_position() != null)
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R14 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_net_forward_position() != null)
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R14 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR14_guarantees() != null)
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R14 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR14_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R14 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR14_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R14 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR14_other_items() != null)
						cell7.setCellValue(record.getR14_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R14 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR14_net_long_position() != null)
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R14 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR14_or() != null)
						cell9.setCellValue(record.getR14_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R14 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR14_net_short_position() != null)
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R15 / Col B =====
					row = sheet.getRow(14);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR15_currency() != null)
//    					cell1.setCellValue(record.getR15_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R15 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR15_net_spot_position() != null)
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R15 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_net_forward_position() != null)
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R15 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR15_guarantees() != null)
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R15 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR15_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R15 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR15_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R15 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR15_other_items() != null)
						cell7.setCellValue(record.getR15_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R15 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR15_net_long_position() != null)
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R15 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR15_or() != null)
						cell9.setCellValue(record.getR15_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R15 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR15_net_short_position() != null)
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

					// ===== R16 / Col B =====
					row = sheet.getRow(15);
//    					cell1 = row.getCell(1);
//    					if (cell1 == null) cell1 = row.createCell(1);
//    					originalStyle = cell1.getCellStyle();
//    			// ✅ Handle String value 
//    					if (record.getR16_currency() != null)
//    					cell1.setCellValue(record.getR16_currency()); // String directly 
//    					else cell1.setCellValue(""); 
//    					cell1.setCellStyle(originalStyle);

					// ===== R16 / Col C =====

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);
					originalStyle = cell2.getCellStyle();
					if (record.getR16_net_spot_position() != null)
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
					else
						cell2.setCellValue("");
					cell2.setCellStyle(originalStyle);

					// ===== R16 / Col D =====

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_net_forward_position() != null)
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

					// ===== R16 / Col E =====

					cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);
					originalStyle = cell4.getCellStyle();
					if (record.getR16_guarantees() != null)
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

					// ===== R16 / Col F =====

					cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);
					originalStyle = cell5.getCellStyle();
					if (record.getR16_net_future_inc_or_exp() != null)
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
					else
						cell5.setCellValue("");
					cell5.setCellStyle(originalStyle);

					// ===== R16 / Col G =====

					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR16_net_delta_wei_fx_opt_posi() != null)
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R16 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR16_other_items() != null)
						cell7.setCellValue(record.getR16_other_items().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					// ===== R16 / Col I =====

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR16_net_long_position() != null)
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					// ===== R16 / Col J =====

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR16_or() != null)
						cell9.setCellValue(record.getR16_or().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

					// ===== R16 / Col K =====

					cell10 = row.getCell(10);
					if (cell10 == null)
						cell10 = row.createCell(10);
					originalStyle = cell10.getCellStyle();
					if (record.getR16_net_short_position() != null)
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
					else
						cell10.setCellValue("");
					cell10.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {
					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
					CellStyle originalStyle;

					// ===== R21 / Col G =====
					row = sheet.getRow(20);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR21_long() != null)
						cell6.setCellValue(record.getR21_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R21 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR21_short() != null)
						cell7.setCellValue(record.getR21_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR21_total_gross_long_short() != null)
						cell8.setCellValue(record.getR21_total_gross_long_short().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR21_net_position() != null)
						cell9.setCellValue(record.getR21_net_position().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);
					// ===== R22 / Col G =====
					row = sheet.getRow(21);
					cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);
					originalStyle = cell6.getCellStyle();
					if (record.getR22_long() != null)
						cell6.setCellValue(record.getR22_long().doubleValue());
					else
						cell6.setCellValue("");
					cell6.setCellStyle(originalStyle);

					// ===== R22 / Col H =====

					cell7 = row.getCell(7);
					if (cell7 == null)
						cell7 = row.createCell(7);
					originalStyle = cell7.getCellStyle();
					if (record.getR22_short() != null)
						cell7.setCellValue(record.getR22_short().doubleValue());
					else
						cell7.setCellValue("");
					cell7.setCellStyle(originalStyle);

					cell8 = row.getCell(8);
					if (cell8 == null)
						cell8 = row.createCell(8);
					originalStyle = cell8.getCellStyle();
					if (record.getR22_total_gross_long_short() != null)
						cell8.setCellValue(record.getR22_total_gross_long_short().doubleValue());
					else
						cell8.setCellValue("");
					cell8.setCellStyle(originalStyle);

					cell9 = row.getCell(9);
					if (cell9 == null)
						cell9 = row.createCell(9);
					originalStyle = cell9.getCellStyle();
					if (record.getR22_net_position() != null)
						cell9.setCellValue(record.getR22_net_position().doubleValue());
					else
						cell9.setCellValue("");
					cell9.setCellStyle(originalStyle);

				}

				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + (startRow + i));
					Row row = sheet.getRow(22);

					Cell R28cell22 = row.createCell(9);
					if (record.getR23_net_position() != null) {
						R28cell22.setCellValue(record.getR23_net_position().doubleValue());
						R28cell22.setCellStyle(numberStyle);
					} else {
						R28cell22.setCellValue("");
						R28cell22.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);

					Cell R28cell = row.createCell(6);
					if (record.getR29_greater_net_long_or_short() != null) {
						R28cell.setCellValue(record.getR29_greater_net_long_or_short().doubleValue());
						R28cell.setCellStyle(numberStyle);
					} else {
						R28cell.setCellValue("");
						R28cell.setCellStyle(textStyle);
					}
					Cell R28cell1 = row.createCell(7);
					if (record.getR29_abs_value_net_gold_posi() != null) {
						R28cell1.setCellValue(record.getR29_abs_value_net_gold_posi().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					Cell R28cell3 = row.createCell(9);
					if (record.getR29_capital_charge() != null) {
						R28cell3.setCellValue(record.getR29_capital_charge().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// ===== R30 / Col I =====
					row = sheet.getRow(29);

					// ✅ CRITICAL FIX
					if (row == null) {
						row = sheet.createRow(29);
					}

					Cell cell8 = row.getCell(8);
					if (cell8 == null) {
						cell8 = row.createCell(8);
					}

					if (record.getR30_capital_require() != null) {
						cell8.setCellValue(record.getR30_capital_require().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
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
