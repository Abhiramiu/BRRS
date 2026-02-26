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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Detail_Repo1;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Detail_Repo2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Detail_Repo3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Detail_Repo4;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Resub_Summary_Repo4;
import com.bornfire.brrs.entities.BRRS_M_SECL_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12D_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12D_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_PK;
import com.bornfire.brrs.entities.M_SRWA_12D_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12D_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;

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

	@Autowired
	BRRS_M_SRWA_12D_Archival_Detail_Repo bRRS_M_SRWA_12D_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12D_Resub_Summary_Repo bRRS_M_SRWA_12D_Resub_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12D_Resub_Detail_Repo bRRS_M_SRWA_12D_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA_12DView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		System.out.println("========== ENTERED getM_SRWA_12DView ==========");

		ModelAndView mv = new ModelAndView("BRRS/M_SRWA_12D");

		Date reportDate = null;

		try {
			reportDate = dateformat.parse(todate);
			System.out.println("Parsed Report Date : " + reportDate);
		} catch (ParseException e) {
			System.out.println("❌ DATE PARSE ERROR");
			e.printStackTrace();
		}

		System.out.println("Report ID   : " + reportId);
		System.out.println("From Date   : " + fromdate);
		System.out.println("To Date     : " + todate);
		System.out.println("Currency    : " + currency);
		System.out.println("Type        : " + type);
		System.out.println("DtlType     : " + dtltype);
		System.out.println("Version     : " + version);

		if (type == null) {
			System.out.println("❌ TYPE IS NULL");
		}

		if (version == null) {
			System.out.println("❌ VERSION IS NULL");
		}

		/* ===================== ARCHIVAL ===================== */
		if ("ARCHIVAL".equalsIgnoreCase(type)) {

			System.out.println("➡ ENTERED ARCHIVAL BLOCK");

			if ("detail".equalsIgnoreCase(dtltype)) {

				System.out.println("➡ FETCHING ARCHIVAL DETAIL");

				List<M_SRWA_12D_Archival_Detail_Entity> detailList = bRRS_M_SRWA_12D_Archival_Detail_Repo
						.getdatabydateListarchival(reportDate, version);

				System.out.println("Archival Detail List Size : " + (detailList != null ? detailList.size() : "NULL"));

				mv.addObject("reportsummary", detailList);
				mv.addObject("displaymode", "detail");

			} else {

				System.out.println("➡ FETCHING ARCHIVAL SUMMARY");

				List<M_SRWA_12D_Archival_Summary_Entity> summaryList = bRRS_M_SRWA_12D_Archival_Summary_Repo
						.getdatabydateListarchival(reportDate, version);

				System.out
						.println("Archival Summary List Size : " + (summaryList != null ? summaryList.size() : "NULL"));

				mv.addObject("reportsummary", summaryList);
				mv.addObject("displaymode", "summary");
			}
		}

		/* ===================== RESUB ===================== */
		else if ("RESUB".equalsIgnoreCase(type)) {

			System.out.println("➡ ENTERED RESUB BLOCK");

			if ("detail".equalsIgnoreCase(dtltype)) {

				System.out.println("➡ FETCHING RESUB DETAIL");

				List<M_SRWA_12D_Resub_Detail_Entity> detailList = bRRS_M_SRWA_12D_Resub_Detail_Repo
						.getdatabydateListarchival(reportDate, version);

				System.out.println("Resub Detail List Size : " + (detailList != null ? detailList.size() : "NULL"));

				mv.addObject("reportsummary", detailList);
				mv.addObject("displaymode", "detail");

			} else {

				System.out.println("➡ FETCHING RESUB SUMMARY");

				List<M_SRWA_12D_Resub_Summary_Entity> summaryList = bRRS_M_SRWA_12D_Resub_Summary_Repo
						.getdatabydateListarchival(reportDate, version);

				System.out.println("Resub Summary List Size : " + (summaryList != null ? summaryList.size() : "NULL"));

				mv.addObject("reportsummary", summaryList);
				mv.addObject("displaymode", "summary");
			}
		}

		/* ===================== NORMAL ===================== */
		else {

			System.out.println("➡ ENTERED NORMAL BLOCK");

			if ("detail".equalsIgnoreCase(dtltype)) {

				System.out.println("➡ FETCHING NORMAL DETAIL");

				List<M_SRWA_12D_Detail_Entity> detailList = bRRS_M_SRWA_12D_Detail_Repo.getdatabydateList(reportDate);

				System.out.println("Normal Detail List Size : " + (detailList != null ? detailList.size() : "NULL"));

				mv.addObject("reportsummary", detailList);
				mv.addObject("displaymode", "detail");

			} else {

				System.out.println("➡ FETCHING NORMAL SUMMARY");

				List<M_SRWA_12D_Summary_Entity> summaryList = M_SRWA_12D_Summary_Repo.getdatabydateList(reportDate);

				System.out.println("Normal Summary List Size : " + (summaryList != null ? summaryList.size() : "NULL"));

				mv.addObject("reportsummary", summaryList);
				mv.addObject("displaymode", "summary");
			}
		}

		System.out.println("========== EXIT getM_SRWA_12DView ==========");

		return mv;
	}

	@Transactional
	public void updateReport(M_SRWA_12D_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("report_date: " + updatedEntity.getReport_date());

		M_SRWA_12D_Summary_Entity existing = M_SRWA_12D_Summary_Repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		M_SRWA_12D_Detail_Entity existing1 = bRRS_M_SRWA_12D_Detail_Repo.findById(updatedEntity.getReport_date())
				.orElseThrow(() -> new RuntimeException(
						"Detail Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		try {

			String[] totalFields = { "PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS", "APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS",
					"PRINCIPAL_AMOUNT_INTEREST_CONTRACTS", "TOTAL_CURRENT_INTEREST_CONTRACTS",
					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS",
					"PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
					"PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS", "TOTAL_CURRENT_PRECIOUS_CONTRACTS",
					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS",
					"PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",
					"PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
					"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS", "APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS",
					"PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS", "POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS",
					"ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS", "APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS" };

			for (int i = 12; i <= 45; i++) {

				String prefix = "R" + i + "_";

				for (String field : totalFields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {

						// ===== Summary Update =====
						Method getterSummary = M_SRWA_12D_Summary_Entity.class.getMethod(getterName);
						Method setterSummary = M_SRWA_12D_Summary_Entity.class.getMethod(setterName,
								getterSummary.getReturnType());

						Object newValue = getterSummary.invoke(updatedEntity);
						setterSummary.invoke(existing, newValue);

						// ===== Detail Update =====
						Method getterDetail = M_SRWA_12D_Detail_Entity.class.getMethod(getterName);
						Method setterDetail = M_SRWA_12D_Detail_Entity.class.getMethod(setterName,
								getterDetail.getReturnType());

						setterDetail.invoke(existing1, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Saving Summary and Detail...");

		M_SRWA_12D_Summary_Repo.save(existing);
		bRRS_M_SRWA_12D_Detail_Repo.save(existing1);

		System.out.println("Update Completed Successfully");
	}

	public byte[] getM_SRWA_12DExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		Date parsed = dateformat.parse(todate);

		boolean isEmail = "email".equalsIgnoreCase(dtltype);

		if ("ARCHIVAL".equalsIgnoreCase(type)) {
			return generateExcelForType(filename, parsed, version, dtltype, isEmail, "ARCHIVAL");
		} else if ("RESUB".equalsIgnoreCase(type)) {
			return generateExcelForType(filename, parsed, version, dtltype, isEmail, "RESUB");
		} else {
			return generateExcelForType(filename, parsed, version, dtltype, isEmail, "NORMAL");
		}
	}

	private byte[] generateExcelForType(String filename, Date parsed, BigDecimal version, String dtltype,
			boolean isEmail, String reportType) throws Exception {

		List<?> dataList = fetchData(reportType, parsed, version);

		return generateCommonExcel(filename, dataList, reportType, dtltype, isEmail);
	}

	private List<?> fetchData(String reportType, Date parsed, BigDecimal version) {

		switch (reportType) {

		case "ARCHIVAL":
			return bRRS_M_SRWA_12D_Archival_Summary_Repo.getdatabydateListarchival1(parsed, version);

		case "RESUB":
			return bRRS_M_SRWA_12D_Resub_Summary_Repo.getdatabydateListarchival1(parsed, version);

		default:
			return M_SRWA_12D_Summary_Repo.getdatabydateList(parsed);
		}
	}

	private void setBorder(CellStyle style) {
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
	}

	private void writeRowData(Sheet sheet, Row row, Object obj, CellStyle numberStyle, String dtltype,
			CellStyle textStyle) {

		if (obj instanceof M_SRWA_12D_Archival_Summary_Entity) {

			M_SRWA_12D_Archival_Summary_Entity record = (M_SRWA_12D_Archival_Summary_Entity) obj;

			if (dtltype.equalsIgnoreCase("email")) {
				writeArchivalEmailSection(sheet, row, record, numberStyle, textStyle);
			} else {
				writeArchivalSection(sheet, row, record, numberStyle, textStyle);
			}
		}

		else if (obj instanceof M_SRWA_12D_Resub_Summary_Entity) {

			M_SRWA_12D_Resub_Summary_Entity record = (M_SRWA_12D_Resub_Summary_Entity) obj;

			if (dtltype.equalsIgnoreCase("email")) {
				writeResubEmailSection(sheet, row, record, numberStyle, textStyle);
			} else {
				writeResubSection(sheet, row, record, numberStyle, textStyle);
			}
		}

		else if (obj instanceof M_SRWA_12D_Summary_Entity) {

			M_SRWA_12D_Summary_Entity record = (M_SRWA_12D_Summary_Entity) obj;

			if (dtltype.equalsIgnoreCase("email")) {
				writeNormalEmailSection(sheet, row, record, numberStyle, textStyle);
			} else {
				writeNormalSection(sheet, row, record, numberStyle, textStyle);
			}

		}
	}

	private byte[] generateCommonExcel(String filename, List<?> dataList, String reportType, String dtltype,
			boolean isEmail) throws Exception {

		if (dataList == null || dataList.isEmpty()) {
			logger.warn("Service: No data found, exporting template only.");
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		logger.info("Loading template from: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
			throw new SecurityException("Template file not readable: " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			CreationHelper createHelper = workbook.getCreationHelper();

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
			setBorder(dateStyle);

			CellStyle textStyle = workbook.createCellStyle();
			setBorder(textStyle);

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8);
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			setBorder(numberStyle);
			numberStyle.setFont(font);

			int startRow = 11;

			// ✅ ONLY ONE LOOP
			for (int i = 0; i < dataList.size(); i++) {

				Object obj = dataList.get(i);

				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}

				// 🔥 Delegate row writing
				writeRowData(sheet, row, obj, numberStyle, dtltype, textStyle);
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);

			logger.info("Excel generated successfully. Size: {} bytes", out.size());

			return out.toByteArray();
		}
	}

	private void writeArchivalSection(Sheet sheet, Row row, M_SRWA_12D_Archival_Summary_Entity record,
			CellStyle numberStyle, CellStyle textStyle) {

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

		Cell cellA6 = row.createCell(6);
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellA6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellA6.setCellStyle(numberStyle);
		} else {
			cellA6.setCellValue("");
			cellA6.setCellStyle(textStyle);
		}

		Cell cell6 = row.getCell(7);
		if (cell6 == null) {
			cell6 = row.createCell(7);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell6.setCellValue((String) null);
		}

		Cell cellA8 = row.getCell(8);
		if (cellA8 == null) {
			cellA8 = row.createCell(8);
		}
		if (record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellA8.setCellValue(record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellA8.setCellValue((String) null);
		}

		Cell cell9 = row.createCell(9);
		if (record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cell10 = row.getCell(10);
		if (cell10 == null) {
			cell10 = row.createCell(10);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cell10.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell10.setCellValue((String) null);
		}

		Cell cell15 = row.createCell(15);
		if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell15.setCellValue("");
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
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cell18 = row.getCell(18);
		if (cell18 == null) {
			cell18 = row.createCell(18);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cell18.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell18.setCellValue((String) null);
		}

		Cell cell19 = row.getCell(19);
		if (cell19 == null) {
			cell19 = row.createCell(19);
		}
		if (record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cell19.setCellValue(record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell19.setCellValue((String) null);
		}

		Cell cell20 = row.createCell(20);
		if (record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell21 = row.getCell(21);
		if (cell21 == null) {
			cell21 = row.createCell(21);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell21.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell21.setCellValue((String) null);
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

		Cell cell61 = row.createCell(6);
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cell61.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cell61.setCellStyle(numberStyle);
		} else {
			cell61.setCellValue("");
			cell61.setCellStyle(textStyle);
		}

		Cell cell611 = row.getCell(7);
		if (cell611 != null) {
			cell611 = row.createCell(7);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue((String) null);
			cell611.setCellStyle(textStyle);
		}

		Cell cellB8 = row.getCell(8);
		if (cellB8 == null) {
			cellB8 = row.createCell(8);
		}
		if (record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellB8.setCellValue(record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
			cellB8.setCellStyle(numberStyle);
		} else {
			cellB8.setCellValue((String) null);
			cellB8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cellB10 = row.getCell(10);
		if (cellB10 == null) {
			cellB10 = row.createCell(10);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellB10.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellB10.setCellStyle(numberStyle);
		} else {
			cellB10.setCellValue((String) null);
			cellB10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellBB18 = row.getCell(18);
		if (cellBB18 == null) {
			cellBB18 = row.createCell(18);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellBB18.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellBB18.setCellValue((String) null);
		}

		Cell cellB19 = row.getCell(19);
		if (cellB19 == null) {
			cellB19 = row.createCell(19);
		}
		if (record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellB19.setCellValue(record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB19.setCellValue((String) null);
		}

		cell20 = row.createCell(20);
		if (record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell211 = row.getCell(21);
		if (cell211 == null) {
			cell211 = row.createCell(21);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell211.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell211.setCellValue((String) null);
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

		Cell cellC6 = row.createCell(6);
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellC6.setCellStyle(numberStyle);
		} else {
			cellC6.setCellValue("");
			cellC6.setCellStyle(textStyle);
		}

		Cell cellCC6 = row.getCell(7);
		if (cellCC6 == null) {
			cellCC6 = row.createCell(7);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellCC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellCC6.setCellValue((String) null);
		}

		Cell cellC8 = row.getCell(8);
		if (cellC8 == null) {
			cellC8 = row.createCell(8);
		}
		if (record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellC8.setCellValue(record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellC8.setCellValue((String) null);
		}

		cell9 = row.createCell(9);
		if (record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cellC10 = row.getCell(10);
		if (cellC10 == null) {
			cellC10 = row.createCell(10);
		}
		if (record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellC10.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellC10.setCellStyle(numberStyle);
		} else {
			cellC10.setCellValue((String) null);
			cellC10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellB18 = row.getCell(18);
		if (cellB18 == null) {
			cellB18 = row.createCell(18);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellB18.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB18.setCellValue((String) null);
		}

		Cell cellC19 = row.getCell(19);
		if (cellC19 == null) {
			cellC19 = row.createCell(19);
		}
		if (record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellC19.setCellValue(record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellC19.setCellValue((String) null);
		}

		cell20 = row.createCell(20);
		if (record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell2111 = row.getCell(21);

		if (cell2111 == null) {
			cell2111 = row.createCell(21);
		}

		if (record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell2111.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell2111.setCellValue((String) null); // Compatible with old POI
		}

		row = sheet.getRow(14);
		if (row == null) {
			row = sheet.createRow(14);
		}

		Cell cellD5 = row.getCell(4);
		if (cellD5 == null) {
			cellD5 = row.createCell(4);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
			cellD5.setCellValue(record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD5.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD6 = row.getCell(7);
		if (cellD6 == null) {
			cellD6 = row.createCell(7);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellD6.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD6.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD9 = row.getCell(8);
		if (cellD9 == null) {
			cellD9 = row.createCell(8);
		}
		if (record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellD9.setCellValue(record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD9.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD10 = row.getCell(10);
		if (cellD10 == null) {
			cellD10 = row.createCell(10);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellD10.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD10.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD16 = row.getCell(15);
		if (cellD16 == null) {
			cellD16 = row.createCell(15);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cellD16.setCellValue(record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD16.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD17 = row.getCell(18);
		if (cellD17 == null) {
			cellD17 = row.createCell(18);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellD17.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD17.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD19 = row.getCell(19);
		if (cellD19 == null) {
			cellD19 = row.createCell(19);
		}
		if (record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellD19.setCellValue(record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD19.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD20 = row.getCell(21);
		if (cellD20 == null) {
			cellD20 = row.createCell(21);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cellD20.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD20.setCellValue((String) null);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell211 = row.createCell(8);
		if (record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell211.setCellValue(record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell211.setCellStyle(numberStyle);
		} else {
			cell211.setCellValue("");
			cell211.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellG8 = row.createCell(8);
		if (record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellG8.setCellValue(record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellG8.setCellStyle(numberStyle);
		} else {
			cellG8.setCellValue("");
			cellG8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellH8 = row.createCell(7);
		if (record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellH8.setCellValue(record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellH8.setCellStyle(numberStyle);
		} else {
			cellH8.setCellValue("");
			cellH8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		if (row == null) {
			row = sheet.createRow(23);
		}

		cell5 = row.createCell(4);
		if (record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell9 = row.createCell(8);
		if (record.getR24_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR24_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell16.setCellValue(record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell20 = row.createCell(21);
		if (record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellJ8 = row.createCell(8);
		if (record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellJ8.setCellValue(record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellJ8.setCellStyle(numberStyle);
		} else {
			cellJ8.setCellValue("");
			cellJ8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellK8 = row.createCell(7);
		if (record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellK8.setCellValue(record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellK8.setCellStyle(numberStyle);
		} else {
			cellK8.setCellValue("");
			cellK8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellM8 = row.createCell(8);
		if (record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellM8.setCellValue(record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellM8.setCellStyle(numberStyle);
		} else {
			cellM8.setCellValue("");
			cellM8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		if (row == null) {
			row = sheet.createRow(32);
		}

		cell5 = row.createCell(4);
		if (record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
			cell5.setCellValue(record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellL8 = row.createCell(8);
		if (record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellL8.setCellValue(record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellL8.setCellStyle(numberStyle);
		} else {
			cellL8.setCellValue("");
			cellL8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(10);
		if (record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell16.setCellValue(record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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

		cell611 = row.createCell(6);
		if (record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellN7 = row.createCell(7);
		if (record.getR43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
			cellN7.setCellValue(record.getR43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
			cellN7.setCellStyle(numberStyle);
		} else {
			cellN7.setCellValue("");
			cellN7.setCellStyle(textStyle);
		}

		Cell cell8 = row.createCell(8);
		if (record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
			cell8.setCellValue(record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		Cell cellN9 = row.createCell(9);
		if (record.getR43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cellN9.setCellValue(record.getR43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cellN9.setCellStyle(numberStyle);
		} else {
			cellN9.setCellValue("");
			cellN9.setCellStyle(textStyle);
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

		cell611 = row.createCell(6);
		if (record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellO7 = row.createCell(7);
		if (record.getR44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
			cellO7.setCellValue(record.getR44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
			cellO7.setCellStyle(numberStyle);
		} else {
			cellO7.setCellValue("");
			cellO7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
			cell8.setCellValue(record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		row = sheet.getRow(44);
		if (row == null) {
			row = sheet.createRow(44);
		}

		cell5 = row.createCell(4);
		if (record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
			cell5.setCellValue(record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(9);
		if (record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

// continue remaining fields...
	}

	private void writeArchivalEmailSection(Sheet sheet, Row row, M_SRWA_12D_Archival_Summary_Entity record,
			CellStyle numberStyle, CellStyle textStyle) {

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

		Cell cellA6 = row.createCell(6);
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellA6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellA6.setCellStyle(numberStyle);
		} else {
			cellA6.setCellValue("");
			cellA6.setCellStyle(textStyle);
		}

		Cell cell6 = row.getCell(7);
		if (cell6 == null) {
			cell6 = row.createCell(7);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell6.setCellValue((String) null);
		}

		Cell cellA8 = row.getCell(8);
		if (cellA8 == null) {
			cellA8 = row.createCell(8);
		}
		if (record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellA8.setCellValue(record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellA8.setCellValue((String) null);
		}

		Cell cell10 = row.getCell(9);
		if (cell10 == null) {
			cell10 = row.createCell(9);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cell10.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell10.setCellValue((String) null);
		}

		Cell cell15 = row.createCell(15);
		if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell15.setCellValue("");
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
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cell18 = row.getCell(18);
		if (cell18 == null) {
			cell18 = row.createCell(18);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cell18.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell18.setCellValue((String) null);
		}

		Cell cell19 = row.getCell(19);
		if (cell19 == null) {
			cell19 = row.createCell(19);
		}
		if (record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cell19.setCellValue(record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell19.setCellValue((String) null);
		}

		Cell cell21 = row.getCell(20);
		if (cell21 == null) {
			cell21 = row.createCell(20);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell21.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell21.setCellValue((String) null);
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

		Cell cell61 = row.createCell(6);
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cell61.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cell61.setCellStyle(numberStyle);
		} else {
			cell61.setCellValue("");
			cell61.setCellStyle(textStyle);
		}

		Cell cell611 = row.getCell(7);
		if (cell611 != null) {
			cell611 = row.createCell(7);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue((String) null);
			cell611.setCellStyle(textStyle);
		}

		Cell cellB8 = row.getCell(8);
		if (cellB8 == null) {
			cellB8 = row.createCell(8);
		}
		if (record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellB8.setCellValue(record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
			cellB8.setCellStyle(numberStyle);
		} else {
			cellB8.setCellValue((String) null);
			cellB8.setCellStyle(textStyle);
		}

		Cell cellB10 = row.getCell(9);
		if (cellB10 == null) {
			cellB10 = row.createCell(9);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellB10.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellB10.setCellStyle(numberStyle);
		} else {
			cellB10.setCellValue((String) null);
			cellB10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellBB18 = row.getCell(18);
		if (cellBB18 == null) {
			cellBB18 = row.createCell(18);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellBB18.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellBB18.setCellValue((String) null);
		}

		Cell cellB19 = row.getCell(19);
		if (cellB19 == null) {
			cellB19 = row.createCell(19);
		}
		if (record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellB19.setCellValue(record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB19.setCellValue((String) null);
		}

		Cell cell211 = row.getCell(20);
		if (cell211 == null) {
			cell211 = row.createCell(20);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell211.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell211.setCellValue((String) null);
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

		Cell cellC6 = row.createCell(6);
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellC6.setCellStyle(numberStyle);
		} else {
			cellC6.setCellValue("");
			cellC6.setCellStyle(textStyle);
		}

		Cell cellCC6 = row.getCell(7);
		if (cellCC6 == null) {
			cellCC6 = row.createCell(7);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellCC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellCC6.setCellValue((String) null);
		}

		Cell cellC8 = row.getCell(8);
		if (cellC8 == null) {
			cellC8 = row.createCell(8);
		}
		if (record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellC8.setCellValue(record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellC8.setCellValue((String) null);
		}

		Cell cellC10 = row.getCell(9);
		if (cellC10 == null) {
			cellC10 = row.createCell(9);
		}
		if (record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellC10.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellC10.setCellStyle(numberStyle);
		} else {
			cellC10.setCellValue((String) null);
			cellC10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellB18 = row.getCell(18);
		if (cellB18 == null) {
			cellB18 = row.createCell(18);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellB18.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB18.setCellValue((String) null);
		}

		Cell cellC19 = row.getCell(19);
		if (cellC19 == null) {
			cellC19 = row.createCell(19);
		}
		if (record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellC19.setCellValue(record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellC19.setCellValue((String) null);
		}

		Cell cell2111 = row.getCell(20);

		if (cell2111 == null) {
			cell2111 = row.createCell(20);
		}

		if (record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell2111.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell2111.setCellValue((String) null); // Compatible with old POI
		}

		row = sheet.getRow(14);
		if (row == null) {
			row = sheet.createRow(14);
		}

		Cell cellD5 = row.getCell(4);
		if (cellD5 == null) {
			cellD5 = row.createCell(4);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
			cellD5.setCellValue(record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD5.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD6 = row.getCell(7);
		if (cellD6 == null) {
			cellD6 = row.createCell(7);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellD6.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD6.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD9 = row.getCell(8);
		if (cellD9 == null) {
			cellD9 = row.createCell(8);
		}
		if (record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellD9.setCellValue(record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD9.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD10 = row.getCell(9);
		if (cellD10 == null) {
			cellD10 = row.createCell(9);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellD10.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD10.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD16 = row.getCell(15);
		if (cellD16 == null) {
			cellD16 = row.createCell(15);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cellD16.setCellValue(record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD16.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD17 = row.getCell(18);
		if (cellD17 == null) {
			cellD17 = row.createCell(18);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellD17.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD17.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD19 = row.getCell(19);
		if (cellD19 == null) {
			cellD19 = row.createCell(19);
		}
		if (record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellD19.setCellValue(record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD19.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD20 = row.getCell(20);
		if (cellD20 == null) {
			cellD20 = row.createCell(20);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cellD20.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD20.setCellValue((String) null);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell211 = row.createCell(8);
		if (record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell211.setCellValue(record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell211.setCellStyle(numberStyle);
		} else {
			cell211.setCellValue("");
			cell211.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellG8 = row.createCell(8);
		if (record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellG8.setCellValue(record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellG8.setCellStyle(numberStyle);
		} else {
			cellG8.setCellValue("");
			cellG8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellH8 = row.createCell(7);
		if (record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellH8.setCellValue(record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellH8.setCellStyle(numberStyle);
		} else {
			cellH8.setCellValue("");
			cellH8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		if (row == null) {
			row = sheet.createRow(23);
		}

		cell5 = row.createCell(4);
		if (record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell16.setCellValue(record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellJ8 = row.createCell(8);
		if (record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellJ8.setCellValue(record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellJ8.setCellStyle(numberStyle);
		} else {
			cellJ8.setCellValue("");
			cellJ8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellK8 = row.createCell(7);
		if (record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellK8.setCellValue(record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellK8.setCellStyle(numberStyle);
		} else {
			cellK8.setCellValue("");
			cellK8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellM8 = row.createCell(8);
		if (record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellM8.setCellValue(record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellM8.setCellStyle(numberStyle);
		} else {
			cellM8.setCellValue("");
			cellM8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		if (row == null) {
			row = sheet.createRow(32);
		}

		cell5 = row.createCell(4);
		if (record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
			cell5.setCellValue(record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellL8 = row.createCell(8);
		if (record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellL8.setCellValue(record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellL8.setCellStyle(numberStyle);
		} else {
			cellL8.setCellValue("");
			cellL8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell16.setCellValue(record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(42);
		if (row == null) {
			row = sheet.createRow(42);
		}

		cell5 = row.createCell(4);
		if (record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
			cell5.setCellValue(record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(8);
		if (record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

// continue remaining fields...
	}

	private void writeResubSection(Sheet sheet, Row row, M_SRWA_12D_Resub_Summary_Entity record, CellStyle numberStyle,
			CellStyle textStyle) {

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

		Cell cellA6 = row.createCell(6);
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellA6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellA6.setCellStyle(numberStyle);
		} else {
			cellA6.setCellValue("");
			cellA6.setCellStyle(textStyle);
		}

		Cell cell6 = row.getCell(7);
		if (cell6 == null) {
			cell6 = row.createCell(7);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell6.setCellValue((String) null);
		}

		Cell cellA8 = row.getCell(8);
		if (cellA8 == null) {
			cellA8 = row.createCell(8);
		}
		if (record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellA8.setCellValue(record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellA8.setCellValue((String) null);
		}

		Cell cell9 = row.createCell(9);
		if (record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cell10 = row.getCell(10);
		if (cell10 == null) {
			cell10 = row.createCell(10);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cell10.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell10.setCellValue((String) null);
		}

		Cell cell15 = row.createCell(15);
		if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell15.setCellValue("");
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
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cell18 = row.getCell(18);
		if (cell18 == null) {
			cell18 = row.createCell(18);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cell18.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell18.setCellValue((String) null);
		}

		Cell cell19 = row.getCell(19);
		if (cell19 == null) {
			cell19 = row.createCell(19);
		}
		if (record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cell19.setCellValue(record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell19.setCellValue((String) null);
		}

		Cell cell20 = row.createCell(20);
		if (record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell21 = row.getCell(21);
		if (cell21 == null) {
			cell21 = row.createCell(21);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell21.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell21.setCellValue((String) null);
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

		Cell cell61 = row.createCell(6);
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cell61.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cell61.setCellStyle(numberStyle);
		} else {
			cell61.setCellValue("");
			cell61.setCellStyle(textStyle);
		}

		Cell cell611 = row.getCell(7);
		if (cell611 != null) {
			cell611 = row.createCell(7);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue((String) null);
			cell611.setCellStyle(textStyle);
		}

		Cell cellB8 = row.getCell(8);
		if (cellB8 == null) {
			cellB8 = row.createCell(8);
		}
		if (record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellB8.setCellValue(record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
			cellB8.setCellStyle(numberStyle);
		} else {
			cellB8.setCellValue((String) null);
			cellB8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cellB10 = row.getCell(10);
		if (cellB10 == null) {
			cellB10 = row.createCell(10);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellB10.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellB10.setCellStyle(numberStyle);
		} else {
			cellB10.setCellValue((String) null);
			cellB10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellBB18 = row.getCell(18);
		if (cellBB18 == null) {
			cellBB18 = row.createCell(18);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellBB18.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellBB18.setCellValue((String) null);
		}

		Cell cellB19 = row.getCell(19);
		if (cellB19 == null) {
			cellB19 = row.createCell(19);
		}
		if (record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellB19.setCellValue(record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB19.setCellValue((String) null);
		}

		cell20 = row.createCell(20);
		if (record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell211 = row.getCell(21);
		if (cell211 == null) {
			cell211 = row.createCell(21);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell211.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell211.setCellValue((String) null);
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

		Cell cellC6 = row.createCell(6);
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellC6.setCellStyle(numberStyle);
		} else {
			cellC6.setCellValue("");
			cellC6.setCellStyle(textStyle);
		}

		Cell cellCC6 = row.getCell(7);
		if (cellCC6 == null) {
			cellCC6 = row.createCell(7);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellCC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellCC6.setCellValue((String) null);
		}

		Cell cellC8 = row.getCell(8);
		if (cellC8 == null) {
			cellC8 = row.createCell(8);
		}
		if (record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellC8.setCellValue(record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellC8.setCellValue((String) null);
		}

		cell9 = row.createCell(9);
		if (record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cellC10 = row.getCell(10);
		if (cellC10 == null) {
			cellC10 = row.createCell(10);
		}
		if (record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellC10.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellC10.setCellStyle(numberStyle);
		} else {
			cellC10.setCellValue((String) null);
			cellC10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellB18 = row.getCell(18);
		if (cellB18 == null) {
			cellB18 = row.createCell(18);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellB18.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB18.setCellValue((String) null);
		}

		Cell cellC19 = row.getCell(19);
		if (cellC19 == null) {
			cellC19 = row.createCell(19);
		}
		if (record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellC19.setCellValue(record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellC19.setCellValue((String) null);
		}

		cell20 = row.createCell(20);
		if (record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell2111 = row.getCell(21);

		if (cell2111 == null) {
			cell2111 = row.createCell(21);
		}

		if (record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell2111.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell2111.setCellValue((String) null); // Compatible with old POI
		}

		row = sheet.getRow(14);
		if (row == null) {
			row = sheet.createRow(14);
		}

		Cell cellD5 = row.getCell(4);
		if (cellD5 == null) {
			cellD5 = row.createCell(4);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
			cellD5.setCellValue(record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD5.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD6 = row.getCell(7);
		if (cellD6 == null) {
			cellD6 = row.createCell(7);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellD6.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD6.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD9 = row.getCell(8);
		if (cellD9 == null) {
			cellD9 = row.createCell(8);
		}
		if (record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellD9.setCellValue(record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD9.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD10 = row.getCell(10);
		if (cellD10 == null) {
			cellD10 = row.createCell(10);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellD10.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD10.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD16 = row.getCell(15);
		if (cellD16 == null) {
			cellD16 = row.createCell(15);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cellD16.setCellValue(record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD16.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD17 = row.getCell(18);
		if (cellD17 == null) {
			cellD17 = row.createCell(18);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellD17.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD17.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD19 = row.getCell(19);
		if (cellD19 == null) {
			cellD19 = row.createCell(19);
		}
		if (record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellD19.setCellValue(record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD19.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD20 = row.getCell(21);
		if (cellD20 == null) {
			cellD20 = row.createCell(21);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cellD20.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD20.setCellValue((String) null);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell211 = row.createCell(8);
		if (record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell211.setCellValue(record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell211.setCellStyle(numberStyle);
		} else {
			cell211.setCellValue("");
			cell211.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellG8 = row.createCell(8);
		if (record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellG8.setCellValue(record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellG8.setCellStyle(numberStyle);
		} else {
			cellG8.setCellValue("");
			cellG8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellH8 = row.createCell(7);
		if (record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellH8.setCellValue(record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellH8.setCellStyle(numberStyle);
		} else {
			cellH8.setCellValue("");
			cellH8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		if (row == null) {
			row = sheet.createRow(23);
		}

		cell5 = row.createCell(4);
		if (record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell9 = row.createCell(8);
		if (record.getR24_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR24_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell16.setCellValue(record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell20 = row.createCell(21);
		if (record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellJ8 = row.createCell(8);
		if (record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellJ8.setCellValue(record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellJ8.setCellStyle(numberStyle);
		} else {
			cellJ8.setCellValue("");
			cellJ8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellK8 = row.createCell(7);
		if (record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellK8.setCellValue(record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellK8.setCellStyle(numberStyle);
		} else {
			cellK8.setCellValue("");
			cellK8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellM8 = row.createCell(8);
		if (record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellM8.setCellValue(record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellM8.setCellStyle(numberStyle);
		} else {
			cellM8.setCellValue("");
			cellM8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		if (row == null) {
			row = sheet.createRow(32);
		}

		cell5 = row.createCell(4);
		if (record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
			cell5.setCellValue(record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellL8 = row.createCell(8);
		if (record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellL8.setCellValue(record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellL8.setCellStyle(numberStyle);
		} else {
			cellL8.setCellValue("");
			cellL8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(10);
		if (record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell16.setCellValue(record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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

		cell611 = row.createCell(6);
		if (record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellN7 = row.createCell(7);
		if (record.getR43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
			cellN7.setCellValue(record.getR43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
			cellN7.setCellStyle(numberStyle);
		} else {
			cellN7.setCellValue("");
			cellN7.setCellStyle(textStyle);
		}

		Cell cell8 = row.createCell(8);
		if (record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
			cell8.setCellValue(record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		Cell cellN9 = row.createCell(9);
		if (record.getR43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cellN9.setCellValue(record.getR43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cellN9.setCellStyle(numberStyle);
		} else {
			cellN9.setCellValue("");
			cellN9.setCellStyle(textStyle);
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

		cell611 = row.createCell(6);
		if (record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellO7 = row.createCell(7);
		if (record.getR44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
			cellO7.setCellValue(record.getR44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
			cellO7.setCellStyle(numberStyle);
		} else {
			cellO7.setCellValue("");
			cellO7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
			cell8.setCellValue(record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		row = sheet.getRow(44);
		if (row == null) {
			row = sheet.createRow(44);
		}

		cell5 = row.createCell(4);
		if (record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
			cell5.setCellValue(record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(9);
		if (record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

// continue remaining fields...
	}

	private void writeResubEmailSection(Sheet sheet, Row row, M_SRWA_12D_Resub_Summary_Entity record,
			CellStyle numberStyle, CellStyle textStyle) {

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

		Cell cellA6 = row.createCell(6);
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellA6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellA6.setCellStyle(numberStyle);
		} else {
			cellA6.setCellValue("");
			cellA6.setCellStyle(textStyle);
		}

		Cell cell6 = row.getCell(7);
		if (cell6 == null) {
			cell6 = row.createCell(7);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell6.setCellValue((String) null);
		}

		Cell cellA8 = row.getCell(8);
		if (cellA8 == null) {
			cellA8 = row.createCell(8);
		}
		if (record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellA8.setCellValue(record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellA8.setCellValue((String) null);
		}

		Cell cell10 = row.getCell(9);
		if (cell10 == null) {
			cell10 = row.createCell(9);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cell10.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell10.setCellValue((String) null);
		}

		Cell cell15 = row.createCell(15);
		if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell15.setCellValue("");
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
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cell18 = row.getCell(18);
		if (cell18 == null) {
			cell18 = row.createCell(18);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cell18.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell18.setCellValue((String) null);
		}

		Cell cell19 = row.getCell(19);
		if (cell19 == null) {
			cell19 = row.createCell(19);
		}
		if (record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cell19.setCellValue(record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell19.setCellValue((String) null);
		}

		Cell cell21 = row.getCell(20);
		if (cell21 == null) {
			cell21 = row.createCell(20);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell21.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell21.setCellValue((String) null);
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

		Cell cell61 = row.createCell(6);
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cell61.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cell61.setCellStyle(numberStyle);
		} else {
			cell61.setCellValue("");
			cell61.setCellStyle(textStyle);
		}

		Cell cell611 = row.getCell(7);
		if (cell611 != null) {
			cell611 = row.createCell(7);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue((String) null);
			cell611.setCellStyle(textStyle);
		}

		Cell cellB8 = row.getCell(8);
		if (cellB8 == null) {
			cellB8 = row.createCell(8);
		}
		if (record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellB8.setCellValue(record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
			cellB8.setCellStyle(numberStyle);
		} else {
			cellB8.setCellValue((String) null);
			cellB8.setCellStyle(textStyle);
		}

		Cell cellB10 = row.getCell(9);
		if (cellB10 == null) {
			cellB10 = row.createCell(9);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellB10.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellB10.setCellStyle(numberStyle);
		} else {
			cellB10.setCellValue((String) null);
			cellB10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellBB18 = row.getCell(18);
		if (cellBB18 == null) {
			cellBB18 = row.createCell(18);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellBB18.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellBB18.setCellValue((String) null);
		}

		Cell cellB19 = row.getCell(19);
		if (cellB19 == null) {
			cellB19 = row.createCell(19);
		}
		if (record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellB19.setCellValue(record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB19.setCellValue((String) null);
		}

		Cell cell211 = row.getCell(20);
		if (cell211 == null) {
			cell211 = row.createCell(20);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell211.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell211.setCellValue((String) null);
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

		Cell cellC6 = row.createCell(6);
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellC6.setCellStyle(numberStyle);
		} else {
			cellC6.setCellValue("");
			cellC6.setCellStyle(textStyle);
		}

		Cell cellCC6 = row.getCell(7);
		if (cellCC6 == null) {
			cellCC6 = row.createCell(7);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellCC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellCC6.setCellValue((String) null);
		}

		Cell cellC8 = row.getCell(8);
		if (cellC8 == null) {
			cellC8 = row.createCell(8);
		}
		if (record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellC8.setCellValue(record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellC8.setCellValue((String) null);
		}

		Cell cellC10 = row.getCell(9);
		if (cellC10 == null) {
			cellC10 = row.createCell(9);
		}
		if (record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellC10.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellC10.setCellStyle(numberStyle);
		} else {
			cellC10.setCellValue((String) null);
			cellC10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellB18 = row.getCell(18);
		if (cellB18 == null) {
			cellB18 = row.createCell(18);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellB18.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB18.setCellValue((String) null);
		}

		Cell cellC19 = row.getCell(19);
		if (cellC19 == null) {
			cellC19 = row.createCell(19);
		}
		if (record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellC19.setCellValue(record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellC19.setCellValue((String) null);
		}

		Cell cell2111 = row.getCell(20);

		if (cell2111 == null) {
			cell2111 = row.createCell(20);
		}

		if (record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell2111.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell2111.setCellValue((String) null); // Compatible with old POI
		}

		row = sheet.getRow(14);
		if (row == null) {
			row = sheet.createRow(14);
		}

		Cell cellD5 = row.getCell(4);
		if (cellD5 == null) {
			cellD5 = row.createCell(4);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
			cellD5.setCellValue(record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD5.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD6 = row.getCell(7);
		if (cellD6 == null) {
			cellD6 = row.createCell(7);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellD6.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD6.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD9 = row.getCell(8);
		if (cellD9 == null) {
			cellD9 = row.createCell(8);
		}
		if (record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellD9.setCellValue(record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD9.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD10 = row.getCell(9);
		if (cellD10 == null) {
			cellD10 = row.createCell(9);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellD10.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD10.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD16 = row.getCell(15);
		if (cellD16 == null) {
			cellD16 = row.createCell(15);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cellD16.setCellValue(record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD16.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD17 = row.getCell(18);
		if (cellD17 == null) {
			cellD17 = row.createCell(18);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellD17.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD17.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD19 = row.getCell(19);
		if (cellD19 == null) {
			cellD19 = row.createCell(19);
		}
		if (record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellD19.setCellValue(record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD19.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD20 = row.getCell(20);
		if (cellD20 == null) {
			cellD20 = row.createCell(20);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cellD20.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD20.setCellValue((String) null);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell211 = row.createCell(8);
		if (record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell211.setCellValue(record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell211.setCellStyle(numberStyle);
		} else {
			cell211.setCellValue("");
			cell211.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellG8 = row.createCell(8);
		if (record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellG8.setCellValue(record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellG8.setCellStyle(numberStyle);
		} else {
			cellG8.setCellValue("");
			cellG8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellH8 = row.createCell(7);
		if (record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellH8.setCellValue(record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellH8.setCellStyle(numberStyle);
		} else {
			cellH8.setCellValue("");
			cellH8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		if (row == null) {
			row = sheet.createRow(23);
		}

		cell5 = row.createCell(4);
		if (record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell16.setCellValue(record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellJ8 = row.createCell(8);
		if (record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellJ8.setCellValue(record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellJ8.setCellStyle(numberStyle);
		} else {
			cellJ8.setCellValue("");
			cellJ8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellK8 = row.createCell(7);
		if (record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellK8.setCellValue(record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellK8.setCellStyle(numberStyle);
		} else {
			cellK8.setCellValue("");
			cellK8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellM8 = row.createCell(8);
		if (record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellM8.setCellValue(record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellM8.setCellStyle(numberStyle);
		} else {
			cellM8.setCellValue("");
			cellM8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		if (row == null) {
			row = sheet.createRow(32);
		}

		cell5 = row.createCell(4);
		if (record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
			cell5.setCellValue(record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellL8 = row.createCell(8);
		if (record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellL8.setCellValue(record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellL8.setCellStyle(numberStyle);
		} else {
			cellL8.setCellValue("");
			cellL8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell16.setCellValue(record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(42);
		if (row == null) {
			row = sheet.createRow(42);
		}

		cell5 = row.createCell(4);
		if (record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
			cell5.setCellValue(record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(8);
		if (record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}
// continue remaining fields...
	}

	private void writeNormalSection(Sheet sheet, Row row, M_SRWA_12D_Summary_Entity record, CellStyle numberStyle,
			CellStyle textStyle) {

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

		Cell cellA6 = row.createCell(6);
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellA6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellA6.setCellStyle(numberStyle);
		} else {
			cellA6.setCellValue("");
			cellA6.setCellStyle(textStyle);
		}

		Cell cell6 = row.getCell(7);
		if (cell6 == null) {
			cell6 = row.createCell(7);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell6.setCellValue((String) null);
		}

		Cell cellA8 = row.getCell(8);
		if (cellA8 == null) {
			cellA8 = row.createCell(8);
		}
		if (record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellA8.setCellValue(record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellA8.setCellValue((String) null);
		}

		Cell cell9 = row.createCell(9);
		if (record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cell10 = row.getCell(10);
		if (cell10 == null) {
			cell10 = row.createCell(10);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cell10.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell10.setCellValue((String) null);
		}

		Cell cell15 = row.createCell(15);
		if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell15.setCellValue("");
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
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cell18 = row.getCell(18);
		if (cell18 == null) {
			cell18 = row.createCell(18);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cell18.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell18.setCellValue((String) null);
		}

		Cell cell19 = row.getCell(19);
		if (cell19 == null) {
			cell19 = row.createCell(19);
		}
		if (record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cell19.setCellValue(record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell19.setCellValue((String) null);
		}

		Cell cell20 = row.createCell(20);
		if (record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR12_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell21 = row.getCell(21);
		if (cell21 == null) {
			cell21 = row.createCell(21);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell21.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell21.setCellValue((String) null);
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

		Cell cell61 = row.createCell(6);
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cell61.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cell61.setCellStyle(numberStyle);
		} else {
			cell61.setCellValue("");
			cell61.setCellStyle(textStyle);
		}

		Cell cell611 = row.getCell(7);
		if (cell611 != null) {
			cell611 = row.createCell(7);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue((String) null);
			cell611.setCellStyle(textStyle);
		}

		Cell cellB8 = row.getCell(8);
		if (cellB8 == null) {
			cellB8 = row.createCell(8);
		}
		if (record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellB8.setCellValue(record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
			cellB8.setCellStyle(numberStyle);
		} else {
			cellB8.setCellValue((String) null);
			cellB8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cellB10 = row.getCell(10);
		if (cellB10 == null) {
			cellB10 = row.createCell(10);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellB10.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellB10.setCellStyle(numberStyle);
		} else {
			cellB10.setCellValue((String) null);
			cellB10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellBB18 = row.getCell(18);
		if (cellBB18 == null) {
			cellBB18 = row.createCell(18);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellBB18.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellBB18.setCellValue((String) null);
		}

		Cell cellB19 = row.getCell(19);
		if (cellB19 == null) {
			cellB19 = row.createCell(19);
		}
		if (record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellB19.setCellValue(record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB19.setCellValue((String) null);
		}

		cell20 = row.createCell(20);
		if (record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR13_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell211 = row.getCell(21);
		if (cell211 == null) {
			cell211 = row.createCell(21);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell211.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell211.setCellValue((String) null);
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

		Cell cellC6 = row.createCell(6);
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellC6.setCellStyle(numberStyle);
		} else {
			cellC6.setCellValue("");
			cellC6.setCellStyle(textStyle);
		}

		Cell cellCC6 = row.getCell(7);
		if (cellCC6 == null) {
			cellCC6 = row.createCell(7);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellCC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellCC6.setCellValue((String) null);
		}

		Cell cellC8 = row.getCell(8);
		if (cellC8 == null) {
			cellC8 = row.createCell(8);
		}
		if (record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellC8.setCellValue(record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellC8.setCellValue((String) null);
		}

		cell9 = row.createCell(9);
		if (record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		Cell cellC10 = row.getCell(10);
		if (cellC10 == null) {
			cellC10 = row.createCell(10);
		}
		if (record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellC10.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellC10.setCellStyle(numberStyle);
		} else {
			cellC10.setCellValue((String) null);
			cellC10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellB18 = row.getCell(18);
		if (cellB18 == null) {
			cellB18 = row.createCell(18);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellB18.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB18.setCellValue((String) null);
		}

		Cell cellC19 = row.getCell(19);
		if (cellC19 == null) {
			cellC19 = row.createCell(19);
		}
		if (record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellC19.setCellValue(record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellC19.setCellValue((String) null);
		}

		cell20 = row.createCell(20);
		if (record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS() != null) {
			cell20.setCellValue(record.getR14_APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		Cell cell2111 = row.getCell(21);

		if (cell2111 == null) {
			cell2111 = row.createCell(21);
		}

		if (record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell2111.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell2111.setCellValue((String) null); // Compatible with old POI
		}

		row = sheet.getRow(14);
		if (row == null) {
			row = sheet.createRow(14);
		}

		Cell cellD5 = row.getCell(4);
		if (cellD5 == null) {
			cellD5 = row.createCell(4);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
			cellD5.setCellValue(record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD5.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD6 = row.getCell(7);
		if (cellD6 == null) {
			cellD6 = row.createCell(7);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellD6.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD6.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD9 = row.getCell(8);
		if (cellD9 == null) {
			cellD9 = row.createCell(8);
		}
		if (record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellD9.setCellValue(record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD9.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD10 = row.getCell(10);
		if (cellD10 == null) {
			cellD10 = row.createCell(10);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellD10.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD10.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD16 = row.getCell(15);
		if (cellD16 == null) {
			cellD16 = row.createCell(15);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cellD16.setCellValue(record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD16.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD17 = row.getCell(18);
		if (cellD17 == null) {
			cellD17 = row.createCell(18);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellD17.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD17.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD19 = row.getCell(19);
		if (cellD19 == null) {
			cellD19 = row.createCell(19);
		}
		if (record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellD19.setCellValue(record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD19.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD20 = row.getCell(21);
		if (cellD20 == null) {
			cellD20 = row.createCell(21);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cellD20.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD20.setCellValue((String) null);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell211 = row.createCell(8);
		if (record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell211.setCellValue(record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell211.setCellStyle(numberStyle);
		} else {
			cell211.setCellValue("");
			cell211.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR21_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellG8 = row.createCell(8);
		if (record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellG8.setCellValue(record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellG8.setCellStyle(numberStyle);
		} else {
			cellG8.setCellValue("");
			cellG8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR22_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellH8 = row.createCell(7);
		if (record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellH8.setCellValue(record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellH8.setCellStyle(numberStyle);
		} else {
			cellH8.setCellValue("");
			cellH8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR23_APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		if (row == null) {
			row = sheet.createRow(23);
		}

		cell5 = row.createCell(4);
		if (record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell9 = row.createCell(8);
		if (record.getR24_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell9.setCellValue(record.getR24_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell16.setCellValue(record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell20 = row.createCell(21);
		if (record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell20.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellJ8 = row.createCell(8);
		if (record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellJ8.setCellValue(record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellJ8.setCellStyle(numberStyle);
		} else {
			cellJ8.setCellValue("");
			cellJ8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR30_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellK8 = row.createCell(7);
		if (record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellK8.setCellValue(record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellK8.setCellStyle(numberStyle);
		} else {
			cellK8.setCellValue("");
			cellK8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR31_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellM8 = row.createCell(8);
		if (record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellM8.setCellValue(record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellM8.setCellStyle(numberStyle);
		} else {
			cellM8.setCellValue("");
			cellM8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR32_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		if (row == null) {
			row = sheet.createRow(32);
		}

		cell5 = row.createCell(4);
		if (record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
			cell5.setCellValue(record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellL8 = row.createCell(8);
		if (record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellL8.setCellValue(record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellL8.setCellStyle(numberStyle);
		} else {
			cellL8.setCellValue("");
			cellL8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(10);
		if (record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell9.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell16.setCellValue(record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell20 = row.createCell(20);
		if (record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS() != null) {
			cell20.setCellValue(record.getR33_APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS().doubleValue());
			cell20.setCellStyle(numberStyle);
		} else {
			cell20.setCellValue("");
			cell20.setCellStyle(textStyle);
		}

		cell21 = row.createCell(21);
		if (record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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

		cell611 = row.createCell(6);
		if (record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellN7 = row.createCell(7);
		if (record.getR43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
			cellN7.setCellValue(record.getR43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
			cellN7.setCellStyle(numberStyle);
		} else {
			cellN7.setCellValue("");
			cellN7.setCellStyle(textStyle);
		}

		Cell cell8 = row.createCell(8);
		if (record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
			cell8.setCellValue(record.getR43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		Cell cellN9 = row.createCell(9);
		if (record.getR43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cellN9.setCellValue(record.getR43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cellN9.setCellStyle(numberStyle);
		} else {
			cellN9.setCellValue("");
			cellN9.setCellStyle(textStyle);
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

		cell611 = row.createCell(6);
		if (record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellO7 = row.createCell(7);
		if (record.getR44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS() != null) {
			cellO7.setCellValue(record.getR44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS().doubleValue());
			cellO7.setCellStyle(numberStyle);
		} else {
			cellO7.setCellValue("");
			cellO7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS() != null) {
			cell8.setCellValue(record.getR44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS().doubleValue());
			cell8.setCellStyle(numberStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell9.setCellValue(record.getR44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		row = sheet.getRow(44);
		if (row == null) {
			row = sheet.createRow(44);
		}

		cell5 = row.createCell(4);
		if (record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
			cell5.setCellValue(record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(9);
		if (record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}
// continue remaining fields...
	}

	private void writeNormalEmailSection(Sheet sheet, Row row, M_SRWA_12D_Summary_Entity record, CellStyle numberStyle,
			CellStyle textStyle) {

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

		Cell cellA6 = row.createCell(6);
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellA6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellA6.setCellStyle(numberStyle);
		} else {
			cellA6.setCellValue("");
			cellA6.setCellStyle(textStyle);
		}

		Cell cell6 = row.getCell(7);
		if (cell6 == null) {
			cell6 = row.createCell(7);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell6.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell6.setCellValue((String) null);
		}

		Cell cellA8 = row.getCell(8);
		if (cellA8 == null) {
			cellA8 = row.createCell(8);
		}
		if (record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellA8.setCellValue(record.getR12_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellA8.setCellValue((String) null);
		}

		Cell cell10 = row.getCell(9);
		if (cell10 == null) {
			cell10 = row.createCell(9);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cell10.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cell10.setCellValue((String) null);
		}

		Cell cell15 = row.createCell(15);
		if (record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR12_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell15.setCellValue("");
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
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cell18 = row.getCell(18);
		if (cell18 == null) {
			cell18 = row.createCell(18);
		}
		if (record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cell18.setCellValue(record.getR12_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell18.setCellValue((String) null);
		}

		Cell cell19 = row.getCell(19);
		if (cell19 == null) {
			cell19 = row.createCell(19);
		}
		if (record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cell19.setCellValue(record.getR12_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell19.setCellValue((String) null);
		}

		Cell cell21 = row.getCell(20);
		if (cell21 == null) {
			cell21 = row.createCell(20);
		}
		if (record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell21.setCellValue(record.getR12_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell21.setCellValue((String) null);
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

		Cell cell61 = row.createCell(6);
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cell61.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cell61.setCellStyle(numberStyle);
		} else {
			cell61.setCellValue("");
			cell61.setCellStyle(textStyle);
		}

		Cell cell611 = row.getCell(7);
		if (cell611 != null) {
			cell611 = row.createCell(7);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue((String) null);
			cell611.setCellStyle(textStyle);
		}

		Cell cellB8 = row.getCell(8);
		if (cellB8 == null) {
			cellB8 = row.createCell(8);
		}
		if (record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellB8.setCellValue(record.getR13_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
			cellB8.setCellStyle(numberStyle);
		} else {
			cellB8.setCellValue((String) null);
			cellB8.setCellStyle(textStyle);
		}

		Cell cellB10 = row.getCell(9);
		if (cellB10 == null) {
			cellB10 = row.createCell(9);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellB10.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellB10.setCellStyle(numberStyle);
		} else {
			cellB10.setCellValue((String) null);
			cellB10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR13_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellBB18 = row.getCell(18);
		if (cellBB18 == null) {
			cellBB18 = row.createCell(18);
		}
		if (record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellBB18.setCellValue(record.getR13_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellBB18.setCellValue((String) null);
		}

		Cell cellB19 = row.getCell(19);
		if (cellB19 == null) {
			cellB19 = row.createCell(19);
		}
		if (record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellB19.setCellValue(record.getR13_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB19.setCellValue((String) null);
		}

		Cell cell211 = row.getCell(20);
		if (cell211 == null) {
			cell211 = row.createCell(20);
		}
		if (record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell211.setCellValue(record.getR13_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell211.setCellValue((String) null);
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

		Cell cellC6 = row.createCell(6);
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS() != null) {
			cellC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS().doubleValue());
			cellC6.setCellStyle(numberStyle);
		} else {
			cellC6.setCellValue("");
			cellC6.setCellStyle(textStyle);
		}

		Cell cellCC6 = row.getCell(7);
		if (cellCC6 == null) {
			cellCC6 = row.createCell(7);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellCC6.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellCC6.setCellValue((String) null);
		}

		Cell cellC8 = row.getCell(8);
		if (cellC8 == null) {
			cellC8 = row.createCell(8);
		}
		if (record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellC8.setCellValue(record.getR14_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellC8.setCellValue((String) null);
		}

		Cell cellC10 = row.getCell(9);
		if (cellC10 == null) {
			cellC10 = row.createCell(9);
		}
		if (record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellC10.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
			cellC10.setCellStyle(numberStyle);
		} else {
			cellC10.setCellValue((String) null);
			cellC10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cell15.setCellValue(record.getR14_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell15.setCellValue("");
			cell15.setCellStyle(textStyle);
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
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS() != null) {
			cell17.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		Cell cellB18 = row.getCell(18);
		if (cellB18 == null) {
			cellB18 = row.createCell(18);
		}
		if (record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellB18.setCellValue(record.getR14_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellB18.setCellValue((String) null);
		}

		Cell cellC19 = row.getCell(19);
		if (cellC19 == null) {
			cellC19 = row.createCell(19);
		}
		if (record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellC19.setCellValue(record.getR14_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellC19.setCellValue((String) null);
		}

		Cell cell2111 = row.getCell(20);

		if (cell2111 == null) {
			cell2111 = row.createCell(20);
		}

		if (record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cell2111.setCellValue(record.getR14_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cell2111.setCellValue((String) null); // Compatible with old POI
		}

		row = sheet.getRow(14);
		if (row == null) {
			row = sheet.createRow(14);
		}

		Cell cellD5 = row.getCell(4);
		if (cellD5 == null) {
			cellD5 = row.createCell(4);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS() != null) {
			cellD5.setCellValue(record.getR15_PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD5.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD6 = row.getCell(7);
		if (cellD6 == null) {
			cellD6 = row.createCell(7);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS() != null) {
			cellD6.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD6.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD9 = row.getCell(8);
		if (cellD9 == null) {
			cellD9 = row.createCell(8);
		}
		if (record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS() != null) {
			cellD9.setCellValue(record.getR15_CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD9.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD10 = row.getCell(9);
		if (cellD10 == null) {
			cellD10 = row.createCell(9);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS() != null) {
			cellD10.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS().doubleValue());
		} else {
			cellD10.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD16 = row.getCell(15);
		if (cellD16 == null) {
			cellD16 = row.createCell(15);
		}
		if (record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS() != null) {
			cellD16.setCellValue(record.getR15_PRINCIPAL_AMOUNT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD16.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD17 = row.getCell(18);
		if (cellD17 == null) {
			cellD17 = row.createCell(18);
		}
		if (record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS() != null) {
			cellD17.setCellValue(record.getR15_POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD17.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD19 = row.getCell(19);
		if (cellD19 == null) {
			cellD19 = row.createCell(19);
		}
		if (record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS() != null) {
			cellD19.setCellValue(record.getR15_CREDIT_EQUIVALENT_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD19.setCellValue((String) null);
		}

		// --------------------------------------------

		Cell cellD20 = row.getCell(20);
		if (cellD20 == null) {
			cellD20 = row.createCell(20);
		}
		if (record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS() != null) {
			cellD20.setCellValue(record.getR15_RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS().doubleValue());
		} else {
			cellD20.setCellValue((String) null);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell211 = row.createCell(8);
		if (record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cell211.setCellValue(record.getR21_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cell211.setCellStyle(numberStyle);
		} else {
			cell211.setCellValue("");
			cell211.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR21_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR21_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR21_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR21_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellG8 = row.createCell(8);
		if (record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellG8.setCellValue(record.getR22_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellG8.setCellStyle(numberStyle);
		} else {
			cellG8.setCellValue("");
			cellG8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR22_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR22_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR22_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR22_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS() != null) {
			cell6.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellH8 = row.createCell(7);
		if (record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS() != null) {
			cellH8.setCellValue(record.getR23_CREDIT_EQUIVALENT_EQUITY_CONTRACTS().doubleValue());
			cellH8.setCellStyle(numberStyle);
		} else {
			cellH8.setCellValue("");
			cellH8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell15.setCellValue(record.getR23_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR23_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell19.setCellValue(record.getR23_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR23_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(23);
		if (row == null) {
			row = sheet.createRow(23);
		}

		cell5 = row.createCell(4);
		if (record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS() != null) {
			cell5.setCellValue(record.getR24_PRINCIPAL_AMOUNT_EQUITY_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS() != null) {
			cell611.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS() != null) {
			cell10.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS() != null) {
			cell16.setCellValue(record.getR24_PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS() != null) {
			cell17.setCellValue(record.getR24_POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(19);
		if (record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS() != null) {
			cell18.setCellValue(record.getR24_CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS() != null) {
			cell21.setCellValue(record.getR24_RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellJ8 = row.createCell(8);
		if (record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellJ8.setCellValue(record.getR30_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellJ8.setCellStyle(numberStyle);
		} else {
			cellJ8.setCellValue("");
			cellJ8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR30_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR30_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR30_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR30_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellK8 = row.createCell(7);
		if (record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellK8.setCellValue(record.getR31_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellK8.setCellStyle(numberStyle);
		} else {
			cellK8.setCellValue("");
			cellK8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR31_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR31_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR31_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR31_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS() != null) {
			cell6.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS().doubleValue());
			cell6.setCellStyle(numberStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellM8 = row.createCell(8);
		if (record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellM8.setCellValue(record.getR32_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellM8.setCellStyle(numberStyle);
		} else {
			cellM8.setCellValue("");
			cellM8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell15 = row.createCell(15);
		if (record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell15.setCellValue(record.getR32_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell15.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
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
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell18 = row.createCell(18);
		if (record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell18.setCellValue(record.getR32_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell18.setCellStyle(numberStyle);
		} else {
			cell18.setCellValue("");
			cell18.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR32_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR32_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(32);
		if (row == null) {
			row = sheet.createRow(32);
		}

		cell5 = row.createCell(4);
		if (record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS() != null) {
			cell5.setCellValue(record.getR33_PRINCIPAL_AMOUNT_DEBT_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(7);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS() != null) {
			cell611.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}

		Cell cellL8 = row.createCell(8);
		if (record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS() != null) {
			cellL8.setCellValue(record.getR33_CREDIT_EQUIVALENT_DEBT_CONTRACTS().doubleValue());
			cellL8.setCellStyle(numberStyle);
		} else {
			cellL8.setCellValue("");
			cellL8.setCellStyle(textStyle);
		}

		cell10 = row.createCell(9);
		if (record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS() != null) {
			cell10.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_DEBT_CONTRACTS().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		cell16 = row.createCell(15);
		if (record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS() != null) {
			cell16.setCellValue(record.getR33_PRINCIPAL_AMOUNT_CREDIT_CONTRACTS().doubleValue());
			cell16.setCellStyle(numberStyle);
		} else {
			cell16.setCellValue("");
			cell16.setCellStyle(textStyle);
		}

		cell17 = row.createCell(18);
		if (record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS() != null) {
			cell17.setCellValue(record.getR33_POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS().doubleValue());
			cell17.setCellStyle(numberStyle);
		} else {
			cell17.setCellValue("");
			cell17.setCellStyle(textStyle);
		}

		cell19 = row.createCell(19);
		if (record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS() != null) {
			cell19.setCellValue(record.getR33_CREDIT_EQUIVALENT_CREDIT_CONTRACTS().doubleValue());
			cell19.setCellStyle(numberStyle);
		} else {
			cell19.setCellValue("");
			cell19.setCellStyle(textStyle);
		}

		cell21 = row.createCell(20);
		if (record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS() != null) {
			cell21.setCellValue(record.getR33_RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS().doubleValue());
			cell21.setCellStyle(numberStyle);
		} else {
			cell21.setCellValue("");
			cell21.setCellStyle(textStyle);
		}

		row = sheet.getRow(42);
		if (row == null) {
			row = sheet.createRow(42);
		}

		cell5 = row.createCell(4);
		if (record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS() != null) {
			cell5.setCellValue(record.getR45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell611 = row.createCell(8);
		if (record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS() != null) {
			cell611.setCellValue(record.getR45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS().doubleValue());
			cell611.setCellStyle(numberStyle);
		} else {
			cell611.setCellValue("");
			cell611.setCellStyle(textStyle);
		}
// continue remaining fields...
	}

	public List<Date> getSRWA_12DArchival() {

		try {
			List<Date> archivalList = bRRS_M_SRWA_12D_Archival_Summary_Repo.getM_SECLParchival();

			System.out.println("Archival count : " + archivalList.size());

			return archivalList;

		} catch (Exception e) {
			System.err.println("Error fetching SRWA_12D Archival data : " + e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<Object> getSRWA_12DArchival1() {

		List<Object> archivalList = new ArrayList<>();

		try {

			List<M_SRWA_12D_Archival_Summary_Entity> repoData = bRRS_M_SRWA_12D_Archival_Summary_Repo
					.getdatabydateListWithVersion1();

			if (repoData != null && !repoData.isEmpty()) {

				for (M_SRWA_12D_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					archivalList.add(row); // Object[] stored as Object
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");

			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching SRWA_12D Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public List<Object[]> getM_SRWA_12DResub() {

		try {

			List<Object[]> repoData = bRRS_M_SRWA_12D_Resub_Summary_Repo.getResubData();

			if (repoData == null || repoData.isEmpty()) {
				return Collections.emptyList();
			}

			Map<String, Object[]> uniqueMap = new LinkedHashMap<>();

			for (Object[] e : repoData) {

				Date reportDate = e[0] != null ? (Date) e[0] : null;

				BigDecimal reportVersion = e[1] != null ? (BigDecimal) e[1] : null;

				Date reportResubDate = e[2] != null ? (Date) e[2] : null;

				String key = reportDate + "_" + reportVersion;

				uniqueMap.putIfAbsent(key, new Object[] { reportDate, reportVersion, reportResubDate });
			}

			return new ArrayList<>(uniqueMap.values());

		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Transactional
	public void updateReport1(M_SRWA_12D_Resub_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("report_date: " + updatedEntity.getReport_date());

		try {

			// ==========================
			// 1️⃣ GET NEW VERSION
			// ==========================
			BigDecimal maxVersion = bRRS_M_SRWA_12D_Resub_Summary_Repo.findGlobalMaxReportVersion();

			BigDecimal newVersion = (maxVersion == null) ? BigDecimal.ONE : maxVersion.add(BigDecimal.ONE);

			// ==========================
			// 2️⃣ CREATE NEW ROW OBJECT
			// ==========================
			M_SRWA_12D_Resub_Summary_Entity newEntity = new M_SRWA_12D_Resub_Summary_Entity();

			// ==========================
			// 3️⃣ SET PRIMARY KEY FIRST
			// ==========================
			newEntity.setReport_date(updatedEntity.getReport_date());
			newEntity.setReport_version(newVersion);
			newEntity.setReportResubDate(new Date());

			newEntity.setReport_frequency(updatedEntity.getReport_frequency());
			newEntity.setReport_code(updatedEntity.getReport_code());
			newEntity.setReport_desc(updatedEntity.getReport_desc());
			newEntity.setEntity_flg(updatedEntity.getEntity_flg());
			newEntity.setModify_flg(updatedEntity.getModify_flg());
			newEntity.setDel_flg(updatedEntity.getDel_flg());

			// ==========================
			// 4️⃣ LOOP R12–R15
			// ==========================
			for (int i = 12; i <= 15; i++) {
				copyFields(updatedEntity, newEntity, i);
			}

			// ==========================
			// 5️⃣ LOOP R21–R24
			// ==========================
			for (int i = 21; i <= 24; i++) {
				copyFields(updatedEntity, newEntity, i);
			}

			// ==========================
			// 6️⃣ LOOP R30–R33
			// ==========================
			for (int i = 30; i <= 33; i++) {
				copyFields(updatedEntity, newEntity, i);
			}

			// ==========================
			// 7️⃣ LOOP R43–R45
			// ==========================
			for (int i = 43; i <= 45; i++) {
				copyFields(updatedEntity, newEntity, i);
			}

			// ==========================
			// 8️⃣ SAVE
			// ==========================
			bRRS_M_SRWA_12D_Resub_Summary_Repo.saveAndFlush(newEntity);

			System.out.println("Insert Success with version: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Insert failed", e);
		}
	}

	private void copyFields(M_SRWA_12D_Resub_Summary_Entity source, M_SRWA_12D_Resub_Summary_Entity target, int rowNo) {

		String prefix = "R" + rowNo + "_";

		String[] fields = {

				"LINE_NO_EXCHANGE_CONTRACTS", "PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS", "APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS",
				"CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS", "RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS",

				"LINE_NO_INTEREST_CONTRACTS", "PRINCIPAL_AMOUNT_INTEREST_CONTRACTS", "TOTAL_CURRENT_INTEREST_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS",
				"CREDIT_EQUIVALENT_INTEREST_CONTRACTS", "RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS",

				"LINE_NO_EQUITY_CONTRACTS", "PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
				"CREDIT_EQUIVALENT_EQUITY_CONTRACTS", "RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS",

				"LINE_NO_PRECIOUS_CONTRACTS", "PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS", "TOTAL_CURRENT_PRECIOUS_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS",
				"CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS", "RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS",

				"LINE_NO_DEBT_CONTRACTS", "PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",
				"CREDIT_EQUIVALENT_DEBT_CONTRACTS", "RISK_WEIGHTED_ASSET_DEBT_CONTRACTS",

				"LINE_NO_CREDIT_CONTRACTS", "PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS", "APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS",
				"CREDIT_EQUIVALENT_CREDIT_CONTRACTS", "RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS",

				"LINE_NO_DERIVATIVE_CONTRACTS", "PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS",
				"POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS", "ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS",
				"APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS", "CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS",
				"RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS" };

		for (String field : fields) {

			try {

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				Method getter = M_SRWA_12D_Resub_Summary_Entity.class.getMethod(getterName);

				Method setter = M_SRWA_12D_Resub_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

				Object value = getter.invoke(source);
				setter.invoke(target, value);

			} catch (NoSuchMethodException ignored) {
				// Skip if field not available for that row
			} catch (Exception e) {
				throw new RuntimeException("Error copying field: " + prefix + field, e);
			}
		}
	}

	@Transactional
	public void updateReport2(M_SRWA_12D_Resub_Detail_Entity updatedEntity) {

		System.out.println("========== START updateReport2 ==========");
		System.out.println("report_date: " + updatedEntity.getReport_date());

		try {

			// ==========================
			// 1️⃣ GET NEW VERSION
			// ==========================
			BigDecimal maxVersion = bRRS_M_SRWA_12D_Resub_Detail_Repo.findGlobalMaxReportVersion();

			BigDecimal newVersion = (maxVersion == null) ? BigDecimal.ONE : maxVersion.add(BigDecimal.ONE);

			System.out.println("New Version: " + newVersion);

			// ==========================
			// 2️⃣ CREATE NEW ENTITY
			// ==========================
			M_SRWA_12D_Resub_Detail_Entity newEntity = new M_SRWA_12D_Resub_Detail_Entity();

			// ==========================
			// 3️⃣ SET PRIMARY KEY
			// ==========================
			newEntity.setReport_date(updatedEntity.getReport_date());
			newEntity.setReport_version(newVersion);
			newEntity.setReportResubDate(new Date());

			// ==========================
			// 4️⃣ COPY NORMAL FIELDS
			// ==========================
			newEntity.setReport_frequency(updatedEntity.getReport_frequency());
			newEntity.setReport_code(updatedEntity.getReport_code());
			newEntity.setReport_desc(updatedEntity.getReport_desc());
			newEntity.setEntity_flg(updatedEntity.getEntity_flg());
			newEntity.setModify_flg(updatedEntity.getModify_flg());
			newEntity.setDel_flg(updatedEntity.getDel_flg());

			// ==========================
			// 5️⃣ COPY ALL R FIELDS
			// ==========================
			for (int i = 12; i <= 15; i++) {
				copyFieldsDetail(updatedEntity, newEntity, i);
			}

			for (int i = 21; i <= 24; i++) {
				copyFieldsDetail(updatedEntity, newEntity, i);
			}

			for (int i = 30; i <= 33; i++) {
				copyFieldsDetail(updatedEntity, newEntity, i);
			}

			for (int i = 43; i <= 45; i++) {
				copyFieldsDetail(updatedEntity, newEntity, i);
			}

			// ==========================
			// 6️⃣ SAVE NEW ROW
			// ==========================
			bRRS_M_SRWA_12D_Resub_Detail_Repo.saveAndFlush(newEntity);

			System.out.println("✅ Detail Insert Success with Version: " + newVersion);
			System.out.println("========== END updateReport2 ==========");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Detail Insert failed", e);
		}
	}

	private void copyFieldsDetail(M_SRWA_12D_Resub_Detail_Entity source, M_SRWA_12D_Resub_Detail_Entity target,
			int rowNo) {

		String prefix = "R" + rowNo + "_";

		String[] fields = {

				"LINE_NO_EXCHANGE_CONTRACTS", "PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS", "APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS",
				"CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS", "RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS",

				"LINE_NO_INTEREST_CONTRACTS", "PRINCIPAL_AMOUNT_INTEREST_CONTRACTS", "TOTAL_CURRENT_INTEREST_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS",
				"CREDIT_EQUIVALENT_INTEREST_CONTRACTS", "RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS",

				"LINE_NO_EQUITY_CONTRACTS", "PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
				"CREDIT_EQUIVALENT_EQUITY_CONTRACTS", "RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS",

				"LINE_NO_PRECIOUS_CONTRACTS", "PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS", "TOTAL_CURRENT_PRECIOUS_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS",
				"CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS", "RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS",

				"LINE_NO_DEBT_CONTRACTS", "PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",
				"CREDIT_EQUIVALENT_DEBT_CONTRACTS", "RISK_WEIGHTED_ASSET_DEBT_CONTRACTS",

				"LINE_NO_CREDIT_CONTRACTS", "PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS", "APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS",
				"CREDIT_EQUIVALENT_CREDIT_CONTRACTS", "RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS",

				"LINE_NO_DERIVATIVE_CONTRACTS", "PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS",
				"POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS", "ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS",
				"APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS", "CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS",
				"RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS" };

		for (String field : fields) {

			try {

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				Method getter = M_SRWA_12D_Resub_Detail_Entity.class.getMethod(getterName);

				Method setter = M_SRWA_12D_Resub_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

				Object value = getter.invoke(source);
				setter.invoke(target, value);

			} catch (NoSuchMethodException ignored) {
				// Skip if field not available for that row
			} catch (Exception e) {
				throw new RuntimeException("Error copying field: " + prefix + field, e);
			}
		}
	}

	@Transactional
	public void updateReport3(M_SRWA_12D_Archival_Summary_Entity updatedEntity) {

		System.out.println("========== START updateReport3 ==========");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		try {

			// ==========================
			// 1️⃣ GET NEW VERSION
			// ==========================
			BigDecimal maxVersion = bRRS_M_SRWA_12D_Archival_Summary_Repo.findGlobalMaxReportVersion();

			BigDecimal newVersion = (maxVersion == null) ? BigDecimal.ONE : maxVersion.add(BigDecimal.ONE);

			System.out.println("New Version: " + newVersion);

			// ==========================
			// 2️⃣ CREATE NEW ENTITY
			// ==========================
			M_SRWA_12D_Archival_Summary_Entity newEntity = new M_SRWA_12D_Archival_Summary_Entity();

			// ==========================
			// 3️⃣ SET PRIMARY KEY FIRST
			// ==========================
			newEntity.setReport_date(updatedEntity.getReport_date());
			newEntity.setReport_version(newVersion);
			newEntity.setReportResubDate(new Date());

			// ==========================
			// 4️⃣ COPY NORMAL FIELDS
			// ==========================
			newEntity.setReport_frequency(updatedEntity.getReport_frequency());
			newEntity.setReport_code(updatedEntity.getReport_code());
			newEntity.setReport_desc(updatedEntity.getReport_desc());
			newEntity.setEntity_flg(updatedEntity.getEntity_flg());
			newEntity.setModify_flg(updatedEntity.getModify_flg());
			newEntity.setDel_flg(updatedEntity.getDel_flg());

			// ==========================
			// 5️⃣ COPY ALL R FIELDS
			// ==========================
			for (int i = 12; i <= 15; i++) {
				copyFieldsArchSummary(updatedEntity, newEntity, i);
			}

			for (int i = 21; i <= 24; i++) {
				copyFieldsArchSummary(updatedEntity, newEntity, i);
			}

			for (int i = 30; i <= 33; i++) {
				copyFieldsArchSummary(updatedEntity, newEntity, i);
			}

			for (int i = 43; i <= 45; i++) {
				copyFieldsArchSummary(updatedEntity, newEntity, i);
			}

			// ==========================
			// 6️⃣ SAVE NEW ROW
			// ==========================
			bRRS_M_SRWA_12D_Archival_Summary_Repo.saveAndFlush(newEntity);

			System.out.println("✅ Archival Summary Insert Success Version: " + newVersion);
			System.out.println("========== END updateReport3 ==========");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Archival Summary Insert failed", e);
		}
	}

	private void copyFieldsArchSummary(M_SRWA_12D_Archival_Summary_Entity source,
			M_SRWA_12D_Archival_Summary_Entity target, int rowNo) {

		String prefix = "R" + rowNo + "_";

		String[] fields = {

				"LINE_NO_EXCHANGE_CONTRACTS", "PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS", "APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS",
				"CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS", "RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS",

				"LINE_NO_INTEREST_CONTRACTS", "PRINCIPAL_AMOUNT_INTEREST_CONTRACTS", "TOTAL_CURRENT_INTEREST_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS",
				"CREDIT_EQUIVALENT_INTEREST_CONTRACTS", "RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS",

				"LINE_NO_EQUITY_CONTRACTS", "PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
				"CREDIT_EQUIVALENT_EQUITY_CONTRACTS", "RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS",

				"LINE_NO_PRECIOUS_CONTRACTS", "PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS", "TOTAL_CURRENT_PRECIOUS_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS",
				"CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS", "RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS",

				"LINE_NO_DEBT_CONTRACTS", "PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",
				"CREDIT_EQUIVALENT_DEBT_CONTRACTS", "RISK_WEIGHTED_ASSET_DEBT_CONTRACTS",

				"LINE_NO_CREDIT_CONTRACTS", "PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS", "APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS",
				"CREDIT_EQUIVALENT_CREDIT_CONTRACTS", "RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS",

				"LINE_NO_DERIVATIVE_CONTRACTS", "PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS",
				"POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS", "ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS",
				"APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS", "CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS",
				"RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS" };

		for (String field : fields) {

			try {

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				Method getter = M_SRWA_12D_Archival_Summary_Entity.class.getMethod(getterName);

				Method setter = M_SRWA_12D_Archival_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

				Object value = getter.invoke(source);
				setter.invoke(target, value);

			} catch (NoSuchMethodException ignored) {
				// Skip if field not available for that row
			} catch (Exception e) {
				throw new RuntimeException("Error copying field: " + prefix + field, e);
			}
		}
	}

	@Transactional
	public void updateReport4(M_SRWA_12D_Archival_Detail_Entity updatedEntity) {

		System.out.println("========== START updateReport4 ==========");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		try {

			// ==========================
			// 1️⃣ GET NEW VERSION
			// ==========================
			BigDecimal maxVersion = bRRS_M_SRWA_12D_Archival_Detail_Repo.findGlobalMaxReportVersion();

			BigDecimal newVersion = (maxVersion == null) ? BigDecimal.ONE : maxVersion.add(BigDecimal.ONE);

			System.out.println("New Version: " + newVersion);

			// ==========================
			// 2️⃣ CREATE NEW ENTITY
			// ==========================
			M_SRWA_12D_Archival_Detail_Entity newEntity = new M_SRWA_12D_Archival_Detail_Entity();

			// ==========================
			// 3️⃣ SET PRIMARY KEY FIRST
			// ==========================
			newEntity.setReport_date(updatedEntity.getReport_date());
			newEntity.setReport_version(newVersion);
			newEntity.setReportResubDate(new Date());

			// ==========================
			// 4️⃣ COPY NORMAL FIELDS
			// ==========================
			newEntity.setReport_frequency(updatedEntity.getReport_frequency());
			newEntity.setReport_code(updatedEntity.getReport_code());
			newEntity.setReport_desc(updatedEntity.getReport_desc());
			newEntity.setEntity_flg(updatedEntity.getEntity_flg());
			newEntity.setModify_flg(updatedEntity.getModify_flg());
			newEntity.setDel_flg(updatedEntity.getDel_flg());

			// ==========================
			// 5️⃣ COPY ALL R FIELDS
			// ==========================
			for (int i = 12; i <= 15; i++) {
				copyFieldsArchDetail(updatedEntity, newEntity, i);
			}

			for (int i = 21; i <= 24; i++) {
				copyFieldsArchDetail(updatedEntity, newEntity, i);
			}

			for (int i = 30; i <= 33; i++) {
				copyFieldsArchDetail(updatedEntity, newEntity, i);
			}

			for (int i = 43; i <= 45; i++) {
				copyFieldsArchDetail(updatedEntity, newEntity, i);
			}

			// ==========================
			// 6️⃣ SAVE NEW ROW
			// ==========================
			bRRS_M_SRWA_12D_Archival_Detail_Repo.saveAndFlush(newEntity);

			System.out.println("✅ Archival Detail Insert Success Version: " + newVersion);
			System.out.println("========== END updateReport4 ==========");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Archival Detail Insert failed", e);
		}
	}

	private void copyFieldsArchDetail(M_SRWA_12D_Archival_Detail_Entity source,
			M_SRWA_12D_Archival_Detail_Entity target, int rowNo) {

		String prefix = "R" + rowNo + "_";

		String[] fields = {

				"LINE_NO_EXCHANGE_CONTRACTS", "PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS", "APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS",
				"CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS", "RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS",

				"LINE_NO_INTEREST_CONTRACTS", "PRINCIPAL_AMOUNT_INTEREST_CONTRACTS", "TOTAL_CURRENT_INTEREST_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS",
				"CREDIT_EQUIVALENT_INTEREST_CONTRACTS", "RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS",

				"LINE_NO_EQUITY_CONTRACTS", "PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
				"CREDIT_EQUIVALENT_EQUITY_CONTRACTS", "RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS",

				"LINE_NO_PRECIOUS_CONTRACTS", "PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS", "TOTAL_CURRENT_PRECIOUS_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS",
				"CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS", "RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS",

				"LINE_NO_DEBT_CONTRACTS", "PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",
				"CREDIT_EQUIVALENT_DEBT_CONTRACTS", "RISK_WEIGHTED_ASSET_DEBT_CONTRACTS",

				"LINE_NO_CREDIT_CONTRACTS", "PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS", "APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS",
				"CREDIT_EQUIVALENT_CREDIT_CONTRACTS", "RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS",

				"LINE_NO_DERIVATIVE_CONTRACTS", "PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS",
				"POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS", "ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS",
				"APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS", "CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS",
				"RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS" };

		for (String field : fields) {

			try {

				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				Method getter = M_SRWA_12D_Archival_Detail_Entity.class.getMethod(getterName);

				Method setter = M_SRWA_12D_Archival_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

				Object value = getter.invoke(source);
				setter.invoke(target, value);

			} catch (NoSuchMethodException ignored) {
				// Skip if field not available for that row
			} catch (Exception e) {
				throw new RuntimeException("Error copying field: " + prefix + field, e);
			}
		}
	}
}