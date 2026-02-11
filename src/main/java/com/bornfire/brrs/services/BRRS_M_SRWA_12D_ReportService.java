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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_SECL_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Summary_Repo;
import com.bornfire.brrs.entities.M_SECL_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_Summary_Entity;

@Component
@Service
public class BRRS_M_SRWA_12D_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12D_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_SFINP2_Detail_Repo M_SFINP2_DETAIL_Repo;

	@Autowired
	BRRS_M_SECL_Archival_Summary_Repo M_SECL_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12D_Summary_Repo M_SRWA_12D_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12D_Detail_Repo bRRS_M_SRWA_12D_Detail_Repo;
	
	@Autowired
	BRRS_M_SRWA_12D_Archival_Summary_Repo bRRS_M_SRWA_12D_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA_12DView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView("BRRS/M_SRWA_12D");

		Date reportDate = null;
		try {
			reportDate = dateformat.parse(todate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/* ================= ARCHIVAL SUMMARY ================= */
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

			List<M_SECL_Archival_Summary_Entity> summaryList = M_SECL_Archival_Summary_Repo
					.getdatabydateListarchival(reportDate, version);

			mv.addObject("reportsummary", summaryList);
			mv.addObject("displaymode", "summary");

			return mv; // ðŸ”¥ IMPORTANT
		}

		/* ================= DETAIL MODE ================= */
		if ("detail".equalsIgnoreCase(dtltype)) {

			List<M_SRWA_12D_Detail_Entity> detailList = bRRS_M_SRWA_12D_Detail_Repo.getdatabydateList(reportDate);

			mv.addObject("reportsummary", detailList);
			mv.addObject("displaymode", "detail");

		}
		/* ================= SUMMARY MODE ================= */
		else {

			List<M_SRWA_12D_Summary_Entity> summaryList = M_SRWA_12D_Summary_Repo.getdatabydateList(reportDate);

			mv.addObject("reportsummary", summaryList);
			mv.addObject("displaymode", "summary");
		}

		System.out.println("View = " + mv.getViewName() + " | displaymode = " + mv.getModel().get("displaymode"));

		return mv;
	}

	public void updateReport(M_SRWA_12D_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("report_date: " + updatedEntity.getReport_date());

		M_SRWA_12D_Summary_Entity existing = M_SRWA_12D_Summary_Repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {
			// Loop from R11 to R50 and copy fields
			for (int i = 12; i <= 15; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
						"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS",
						"APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS", "PRINCIPAL_AMOUNT_INTEREST_CONTRACTS",
						"TOTAL_CURRENT_INTEREST_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS",
						"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS",
						"APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12D_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12D_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// Loop from R17 to R30 and copy fields
			for (int i = 21; i <= 24; i++) {
				String prefix = "R" + i + "_";
				String[] fields = {

						"PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
						"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
						"PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS", "TOTAL_CURRENT_PRECIOUS_CONTRACTS",
						"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS",
						"APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12D_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12D_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// Loop from R32 to R33 and copy fields
			for (int i = 30; i <= 33; i++) {
				String prefix = "R" + i + "_";
				String[] fields = {

						"PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
						"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",

						"PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
						"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS",
						"APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12D_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12D_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// Loop from R35 to R36 and copy fields
			for (int i = 43; i <= 45; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS",
						"POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS", "ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS",
						"APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS",

				};

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12D_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12D_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			String[] totalFields = {

					"PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS",
					"CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS", "RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS",

					"PRINCIPAL_AMOUNT_INTEREST_CONTRACTS",

					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "CREDIT_EQUIVALENT_INTEREST_CONTRACTS",

					"RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS",

					"PRINCIPAL_AMOUNT_EQUITY_CONTRACTS",

					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "CREDIT_EQUIVALENT_EQUITY_CONTRACTS",

					"RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS",

					"PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS",

					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS",

					"RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS",

					"PRINCIPAL_AMOUNT_DEBT_CONTRACTS",

					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "CREDIT_EQUIVALENT_DEBT_CONTRACTS",

					"RISK_WEIGHTED_ASSET_DEBT_CONTRACTS",

					"PRINCIPAL_AMOUNT_CREDIT_CONTRACTS",

					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS", "CREDIT_EQUIVALENT_CREDIT_CONTRACTS",

					"RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS",

					"PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS",

					"CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS",

					"RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS"

			};

			// Loop from R12 to R57 and copy fields
			for (int i = 12; i <= 45; i++) {
				String prefix = "R" + i + "_";

				for (String field : totalFields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12D_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12D_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// Skip if field does not exist for this row/field
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Save updated entity
		System.out.println("abc");
		M_SRWA_12D_Summary_Repo.save(existing);
	}

	public byte[] getM_SRWA_12DExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		Date parsed = dateformat.parse(todate);

		boolean isEmail = "email".equalsIgnoreCase(dtltype);

		List<M_SRWA_12D_Summary_Entity> dataList = M_SRWA_12D_Summary_Repo.getdatabydateList(parsed);

		if (!isEmail && dataList.isEmpty()) {
			logger.warn("Service: No data found for download request. Returning null.");
			return null; // Controller sends 204
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found, exporting template only.");
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;

		Path templatePath = Paths.get(templateDir, templateFileName);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

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

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8);
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12D_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR12_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
						cell4.setCellValue(record.getR12_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(5);
					if (record.getR12_TOTAL_CURRENT_EXCHANGE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR12_TOTAL_CURRENT_EXCHANGE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(6);
					if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell9 = row.createCell(9);
					if (record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
						cell9.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					Cell cell15 = row.createCell(15);
					if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
						cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell16 = row.createCell(16);
					if (record.getR12_TOTAL_CURRENT_INTEREST_CONTRACTS() != null) {
						cell16.setCellValue(record.getR12_TOTAL_CURRENT_INTEREST_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					Cell cell17 = row.createCell(17);
					if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					Cell cell20 = row.createCell(20);
					if (record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
						cell20.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}

					cell4 = row.createCell(4);
					if (record.getR13_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
						cell4.setCellValue(record.getR13_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR13_TOTAL_CURRENT_EXCHANGE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR13_TOTAL_CURRENT_EXCHANGE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
						cell9.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
						cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR13_TOTAL_CURRENT_INTEREST_CONTRACTS() != null) {
						cell16.setCellValue(record.getR13_TOTAL_CURRENT_INTEREST_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
						cell20.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}

					cell4 = row.createCell(4);
					if (record.getR14_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
						cell4.setCellValue(record.getR14_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR14_TOTAL_CURRENT_EXCHANGE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR14_TOTAL_CURRENT_EXCHANGE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
						cell9.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
						cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR14_TOTAL_CURRENT_INTEREST_CONTRACTS() != null) {
						cell16.setCellValue(record.getR14_TOTAL_CURRENT_INTEREST_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
						cell20.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					if (row == null) {
						row = sheet.createRow(14);
					}

					cell5 = row.createCell(5);
					if (record.getR15_TOTAL_CURRENT_EXCHANGE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR15_TOTAL_CURRENT_EXCHANGE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR15_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
						cell9.setCellValue(record.getR15_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR15_TOTAL_CURRENT_INTEREST_CONTRACTS() != null) {
						cell16.setCellValue(record.getR15_TOTAL_CURRENT_INTEREST_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR15_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
						cell20.setCellValue(record.getR15_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					if (row == null) {
						row = sheet.createRow(20);
					}

					cell4 = row.createCell(4);
					if (record.getR21_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
						cell4.setCellValue(record.getR21_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR21_TOTAL_CURRENT_EQUITY_CONTRACTS() != null) {
						cell5.setCellValue(record.getR21_TOTAL_CURRENT_EQUITY_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
						cell9.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
						cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR21_TOTAL_CURRENT_PRECIOUS_CONTRACTS() != null) {
						cell16.setCellValue(record.getR21_TOTAL_CURRENT_PRECIOUS_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
						cell20.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					if (row == null) {
						row = sheet.createRow(21);
					}

					cell4 = row.createCell(4);
					if (record.getR22_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
						cell4.setCellValue(record.getR22_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR22_TOTAL_CURRENT_EQUITY_CONTRACTS() != null) {
						cell5.setCellValue(record.getR22_TOTAL_CURRENT_EQUITY_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
						cell9.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
						cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR22_TOTAL_CURRENT_PRECIOUS_CONTRACTS() != null) {
						cell16.setCellValue(record.getR22_TOTAL_CURRENT_PRECIOUS_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
						cell20.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					if (row == null) {
						row = sheet.createRow(22);
					}

					cell4 = row.createCell(4);
					if (record.getR23_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
						cell4.setCellValue(record.getR23_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR23_TOTAL_CURRENT_EQUITY_CONTRACTS() != null) {
						cell5.setCellValue(record.getR23_TOTAL_CURRENT_EQUITY_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
						cell9.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
						cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR23_TOTAL_CURRENT_PRECIOUS_CONTRACTS() != null) {
						cell16.setCellValue(record.getR23_TOTAL_CURRENT_PRECIOUS_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
						cell20.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					if (row == null) {
						row = sheet.createRow(23);
					}

					cell5 = row.createCell(6);
					if (record.getR24_TOTAL_CURRENT_EQUITY_CONTRACTS() != null) {
						cell5.setCellValue(record.getR24_TOTAL_CURRENT_EQUITY_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(7);
					if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR24_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
						cell9.setCellValue(record.getR24_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR24_TOTAL_CURRENT_PRECIOUS_CONTRACTS() != null) {
						cell16.setCellValue(record.getR24_TOTAL_CURRENT_PRECIOUS_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR24_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
						cell20.setCellValue(record.getR24_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					if (row == null) {
						row = sheet.createRow(29);
					}

					cell4 = row.createCell(4);
					if (record.getR30_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
						cell4.setCellValue(record.getR30_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR30_TOTAL_CURRENT_DEBT_CONTRACTS() != null) {
						cell5.setCellValue(record.getR30_TOTAL_CURRENT_DEBT_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
						cell9.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
						cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR30_TOTAL_CURRENT_CREDIT_CONTRACTS() != null) {
						cell16.setCellValue(record.getR30_TOTAL_CURRENT_CREDIT_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
						cell20.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					if (row == null) {
						row = sheet.createRow(30);
					}

					cell4 = row.createCell(4);
					if (record.getR31_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
						cell4.setCellValue(record.getR31_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR31_TOTAL_CURRENT_DEBT_CONTRACTS() != null) {
						cell5.setCellValue(record.getR31_TOTAL_CURRENT_DEBT_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
						cell9.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
						cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR31_TOTAL_CURRENT_CREDIT_CONTRACTS() != null) {
						cell16.setCellValue(record.getR31_TOTAL_CURRENT_CREDIT_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
						cell20.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					if (row == null) {
						row = sheet.createRow(31);
					}

					cell4 = row.createCell(4);
					if (record.getR32_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
						cell4.setCellValue(record.getR32_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR32_TOTAL_CURRENT_DEBT_CONTRACTS() != null) {
						cell5.setCellValue(record.getR32_TOTAL_CURRENT_DEBT_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
						cell9.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell15 = row.createCell(15);
					if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
						cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR32_TOTAL_CURRENT_CREDIT_CONTRACTS() != null) {
						cell16.setCellValue(record.getR32_TOTAL_CURRENT_CREDIT_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
						cell20.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					if (row == null) {
						row = sheet.createRow(32);
					}

					cell5 = row.createCell(6);
					if (record.getR33_TOTAL_CURRENT_DEBT_CONTRACTS() != null) {
						cell5.setCellValue(record.getR33_TOTAL_CURRENT_DEBT_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(7);
					if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
						cell6.setCellValue(
								record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR33_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
						cell9.setCellValue(record.getR33_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					cell16 = row.createCell(16);
					if (record.getR33_TOTAL_CURRENT_CREDIT_CONTRACTS() != null) {
						cell16.setCellValue(record.getR33_TOTAL_CURRENT_CREDIT_CONTRACTS().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					cell17 = row.createCell(17);
					if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
						cell17.setCellValue(
								record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					cell20 = row.createCell(20);
					if (record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
						cell20.setCellValue(record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					if (row == null) {
						row = sheet.createRow(42);
					}

					cell4 = row.createCell(4);
					if (record.getR43_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
						cell4.setCellValue(record.getR43_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR43_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR43_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
						cell6.setCellValue(record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell8 = row.createCell(8);
					if (record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
						cell8.setCellValue(record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					if (row == null) {
						row = sheet.createRow(43);
					}

					cell4 = row.createCell(4);
					if (record.getR44_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
						cell4.setCellValue(record.getR44_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR44_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR44_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
						cell6.setCellValue(record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
						cell8.setCellValue(record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					if (row == null) {
						row = sheet.createRow(44);
					}

					cell5 = row.createCell(5);
					if (record.getR45_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS() != null) {
						cell5.setCellValue(record.getR45_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR45_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
						cell6.setCellValue(record.getR45_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell7 = row.createCell(7);
					if (record.getR45_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
						cell7.setCellValue(record.getR45_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR45_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
						cell8.setCellValue(record.getR45_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	
	public List<Object> getSRWA_12DArchival() {

        List<Object> archivalList = new ArrayList<>();

        try {
            archivalList = bRRS_M_SRWA_12D_Archival_Summary_Repo.getM_SECLParchival();
            System.out.println("Archival count : " + archivalList.size());
        } catch (Exception e) {
            System.err.println("Error fetching SRWA_12D Archival data : " + e.getMessage());
            e.printStackTrace();
        }

        return archivalList;
    }


}