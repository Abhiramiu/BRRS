package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.bornfire.brrs.entities.BDISB2_Summary_Entity;
import com.bornfire.brrs.entities.BDISB2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB2_Detail_Entity;
import com.bornfire.brrs.entities.BDISB2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_BDISB2_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB2_Archival_Summary_Repo;

import com.bornfire.brrs.entities.BDISB2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB2_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_BDISB2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB2_Detail_Repo;

@Component
@Service
public class BRRS_BDISB2_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BRRS_BDISB2_Summary_Repo BRRS_BDISB2_Summary_Repo;

	@Autowired
	BRRS_BDISB2_Archival_Summary_Repo BRRS_BDISB2_Archival_Summary_Repo;

	@Autowired
	BRRS_BDISB2_Detail_Repo BRRS_BDISB2_Detail_Repo;

	@Autowired
	BRRS_BDISB2_Archival_Detail_Repo BRRS_BDISB2_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBDISB2View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<BDISB2_Archival_Summary_Entity> T1Master = BRRS_BDISB2_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<BDISB2_Archival_Summary_Entity> T1Master = BRRS_BDISB2_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<BDISB2_Summary_Entity> T1Master = BRRS_BDISB2_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB2");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

//		
//		else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//            List<BDISB2_Resub_Summary_Entity1> T1Master = new ArrayList<BDISB2_Resub_Summary_Entity1>();
//    
//            try {
//				Date d1 = dateformat.parse(todate);
//            T1Master = BRRS_BDISB2_Resub_Summary_Repo1.getdatabydateListResub(dateformat.parse(todate), version);
//             
//            T2Master = BRRS_BDISB2_Resub_Summary_Repo2.getdatabydateListResub(dateformat.parse(todate), version);
//            
//            T3Master = BRRS_BDISB2_Resub_Summary_Repo3.getdatabydateListResub(dateformat.parse(todate), version);
//            
//            } catch (ParseException e) {
//				e.printStackTrace();
//			}
//                
//                mv.addObject("reportsummary1", T1Master);
//                mv.addObject("reportsummary2", T2Master);
//                mv.addObject("reportsummary3", T3Master);
//		}
//		
//		
//		else {
//			List<BDISB2_Summary_Entity> T1Master = new ArrayList<BDISB2_Summary_Entity>();
//	
//			
//			try {
//				Date d1 = dateformat.parse(todate);
//
//				T1Master = BRRS_BDISB2_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//		
//				
//				
//				
//				System.out.println("Size of t1master is :"+T1Master.size());
//				
//				
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			mv.addObject("reportsummary1", T1Master);
//		
//		}
//
//		
//		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
//		mv.setViewName("BRRS/BDISB2");
//		mv.addObject("displaymode", "summary");
//		System.out.println("scv" + mv.getViewName());
//		return mv;
//	}

	public ModelAndView getBDISB2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String rowId = null;
			String columnId = null;

			// ✅ Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<BDISB2_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_BDISB2_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = BRRS_BDISB2_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<BDISB2_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_BDISB2_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_BDISB2_Detail_Repo.getdatabydateList(parsedDate);
					System.out.println("bdisb2 size is : " + T1Dt1.size());
					totalPages = BRRS_BDISB2_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/BDISB2");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	/*
	 * public void updateDetailFromForm(Date reportDate, Map<String, String> params)
	 * {
	 * 
	 * System.out.println("Updating BDISB2 detail table");
	 * 
	 * for (Map.Entry<String, String> entry : params.entrySet()) {
	 * 
	 * String key = entry.getKey(); String value = entry.getValue();
	 * 
	 * // Only process TOTAL fields if (!key.matches( "R\\d+_C\\d+_(" +
	 * "BANK_SPEC_SINGLE_CUST_REC_NUM|" + "COMPANY_NAME|" + "COMPANY_REG_NUM|" +
	 * "BUSINEES_PHY_ADDRESS|" + "POSTAL_ADDRESS|" + "COUNTRY_OF_REG|" +
	 * "COMPANY_EMAIL|" + "COMPANY_LANDLINE|" + "COMPANY_MOB_PHONE_NUM|" +
	 * "PRODUCT_TYPE|" + "ACCT_NUM|" + "STATUS_OF_ACCT|" +
	 * "ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT|" + "ACCT_BRANCH|" +
	 * "ACCT_BALANCE_PULA|" + "CURRENCY_OF_ACCT|" + "EXCHANGE_RATE" + ")" )) {
	 * continue; }
	 * 
	 * 
	 * String[] parts = key.split("_"); String reportLabel = parts[0]; // R12 String
	 * addlCriteria = parts[1]; // C2
	 * 
	 * BigDecimal amount = (value == null || value.isEmpty()) ? BigDecimal.ZERO :
	 * new BigDecimal(value);
	 * 
	 * List<BDISB2_Detail_Entity> rows =
	 * BRRS_BDISB2_Detail_Repo.findByReportDateAndReportLabelAndReportAddlCriteria1(
	 * reportDate, reportLabel, addlCriteria );
	 * 
	 * for (BDISB2_Detail_Entity row : rows) { row.setAcctBalanceInPula(amount);
	 * row.setModifyFlg("Y"); }
	 * 
	 * BRRS_BDISB2_Detail_Repo.saveAll(rows); }
	 * 
	 * callSummaryProcedure(reportDate); }
	 */
	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

		System.out.println("Updating BDISB2 detail table");

		for (Map.Entry<String, String> entry : params.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();

			// Allow only valid BDISB2 keys
			if (!key.matches("R\\d+_C\\d+_(" + "BANK_SPEC_SINGLE_CUST_REC_NUM|" + "COMPANY_NAME|" + "COMPANY_REG_NUM|"
					+ "BUSINEES_PHY_ADDRESS|" + "POSTAL_ADDRESS|" + "COUNTRY_OF_REG|" + "COMPANY_EMAIL|"
					+ "COMPANY_LANDLINE|" + "COMPANY_MOB_PHONE_NUM|" + "PRODUCT_TYPE|" + "ACCT_NUM|" + "STATUS_OF_ACCT|"
					+ "ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT|" + "ACCT_BRANCH|" + "ACCT_BALANCE_PULA|"
					+ "CURRENCY_OF_ACCT|" + "EXCHANGE_RATE" + ")")) {
				continue;
			}

			// Parse key parts
			String[] parts = key.split("_");
			String reportLabel = parts[0]; // R6, R7...
			String addlCriteria = parts[1]; // C1, C2...
			String columnName = key.replaceFirst("R\\d+_C\\d+_", "");

			// Fetch matching rows
			List<BDISB2_Detail_Entity> rows = BRRS_BDISB2_Detail_Repo
					.findByReportDateAndReportLabelAndReportAddlCriteria1(reportDate, reportLabel, addlCriteria);

			for (BDISB2_Detail_Entity row : rows) {

				// ---------- NUMERIC COLUMNS ----------
				if ("ACCT_NUM".equals(columnName)) {

					BigDecimal num = (value == null || value.trim().isEmpty()) ? BigDecimal.ZERO
							: new BigDecimal(value.replace(",", ""));
					row.setACCT_NUM(num);

				} else if ("ACCT_BALANCE_PULA".equals(columnName)) {

					BigDecimal num = (value == null || value.trim().isEmpty()) ? BigDecimal.ZERO
							: new BigDecimal(value.replace(",", ""));
					row.setACCT_BALANCE_PULA(num);

				} else if ("EXCHANGE_RATE".equals(columnName)) {

					BigDecimal num = (value == null || value.trim().isEmpty()) ? BigDecimal.ZERO
							: new BigDecimal(value.replace(",", ""));
					row.setEXCHANGE_RATE(num);
				}

				// ---------- STRING COLUMNS ----------
				else if ("BANK_SPEC_SINGLE_CUST_REC_NUM".equals(columnName)) {
					row.setBANK_SPEC_SINGLE_CUST_REC_NUM(value);

				} else if ("COMPANY_REG_NUM".equals(columnName)) {
					row.setCOMPANY_REG_NUM(value);
					
				} else if ("COMPANY_NAME".equals(columnName)) {
					row.setCOMPANY_NAME(value);

				} else if ("BUSINEES_PHY_ADDRESS".equals(columnName)) {
					row.setBUSINEES_PHY_ADDRESS(value);

				} else if ("POSTAL_ADDRESS".equals(columnName)) {
					row.setPOSTAL_ADDRESS(value);

				} else if ("COUNTRY_OF_REG".equals(columnName)) {
					row.setCOUNTRY_OF_REG(value);

				} else if ("COMPANY_EMAIL".equals(columnName)) {
					row.setCOMPANY_EMAIL(value);

				} else if ("COMPANY_LANDLINE".equals(columnName)) {
					row.setCOMPANY_LANDLINE(value);

				} else if ("COMPANY_MOB_PHONE_NUM".equals(columnName)) {
					row.setCOMPANY_MOB_PHONE_NUM(value);

				} else if ("PRODUCT_TYPE".equals(columnName)) {
					row.setPRODUCT_TYPE(value);

				} else if ("STATUS_OF_ACCT".equals(columnName)) {
					row.setSTATUS_OF_ACCT(value);

				} else if ("ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(columnName)) {
					row.setACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);

				} else if ("ACCT_BRANCH".equals(columnName)) {
					row.setACCT_BRANCH(value);

				} else if ("CURRENCY_OF_ACCT".equals(columnName)) {
					row.setCURRENCY_OF_ACCT(value);
				}

				// mark row as modified
				row.setModifyFlg("Y");
			}

			BRRS_BDISB2_Detail_Repo.saveAll(rows);
		}

		callSummaryProcedure(reportDate);
	}

	private void callSummaryProcedure(Date reportDate) {

		String sql = "{ call BRRS_BDISB2_SUMMARY_PROCEDURE(?) }";

		jdbcTemplate.update(connection -> {
			CallableStatement cs = connection.prepareCall(sql);

			// Force exact format expected by procedure
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(false);

			String formattedDate = sdf.format(reportDate);

			cs.setString(1, formattedDate); // 🔥 THIS IS MANDATORY
			return cs;
		});

		System.out.println(
				"✅ Summary procedure executed for date: " + new SimpleDateFormat("dd-MM-yyyy").format(reportDate));
	}

	/*
	 * public void updateReport(BDISB2_Summary_Entity updatedEntity) {
	 * System.out.println("Came to services1"); System.out.println("Report Date: " +
	 * updatedEntity.getReportDate());
	 * 
	 * BDISB2_Summary_Entity existing = BRRS_BDISB2_Summary_Repo
	 * .findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
	 * .orElseThrow(() -> new RuntimeException( "Record not found for REPORT_DATE: "
	 * + updatedEntity.getReportDate()));
	 * 
	 * try { // 1️⃣ Loop from R6 to R12 and copy fields for (int i = 6; i <= 12;
	 * i++) { String prefix = "R" + i + "_";
	 * 
	 * String[] fields = { "BANK_SPEC_SINGLE_CUST_REC_NUM", "COMPANY_NAME",
	 * "COMPANY_REG_NUM", "BUSINEES_PHY_ADDRESS", "POSTAL_ADDRESS",
	 * "COUNTRY_OF_REG", "COMPANY_EMAIL", "COMPANY_LANDLINE",
	 * "COMPANY_MOB_PHONE_NUM", "PRODUCT_TYPE", "ACCT_NUM", "STATUS_OF_ACCT",
	 * "ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT", "ACCT_BRANCH",
	 * "ACCT_BALANCE_PULA", "CURRENCY_OF_ACCT", "EXCHANGE_RATE" };
	 * 
	 * for (String field : fields) { String getterName = "get" + prefix + field;
	 * String setterName = "set" + prefix + field;
	 * 
	 * try { Method getter = BDISB2_Summary_Entity.class.getMethod(getterName);
	 * Method setter = BDISB2_Summary_Entity.class.getMethod(setterName,
	 * getter.getReturnType());
	 * 
	 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
	 * newValue);
	 * 
	 * } catch (NoSuchMethodException e) { // Skip missing fields continue; } } }
	 * 
	 * 
	 * 
	 * } catch (Exception e) { throw new
	 * RuntimeException("Error while updating report fields", e); }
	 * 
	 * // 3️⃣ Save updated entity BRRS_BDISB2_Summary_Repo.save(existing); }
	 */

	/*
	 * public void updateReport(BDISB2_Archival_Summary_Entity updatedEntity) {
	 * System.out.println("Came to services1"); System.out.println("Report Date: " +
	 * updatedEntity.getReportDate());
	 * 
	 * BDISB2_Archival_Summary_Entity existing = BRRS_BDISB2_Archival_Summary_Repo
	 * .findById(updatedEntity.getReportDate()) .orElseThrow(() -> new
	 * RuntimeException( "Record not found for REPORT_DATE: " +
	 * updatedEntity.getReportDate()));
	 * 
	 * try { // 1️⃣ Loop from R6 to R12 and copy fields for (int i = 6; i <= 12;
	 * i++) { String prefix = "R" + i + "_";
	 * 
	 * String[] fields = { "BANK_SPEC_SINGLE_CUST_REC_NUM", "COMPANY_NAME",
	 * "COMPANY_REG_NUM", "BUSINEES_PHY_ADDRESS", "POSTAL_ADDRESS",
	 * "COUNTRY_OF_REG", "COMPANY_EMAIL", "COMPANY_LANDLINE",
	 * "COMPANY_MOB_PHONE_NUM", "PRODUCT_TYPE", "ACCT_NUM", "STATUS_OF_ACCT",
	 * "ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT", "ACCT_BRANCH",
	 * "ACCT_BALANCE_PULA", "CURRENCY_OF_ACCT", "EXCHANGE_RATE" };
	 * 
	 * 
	 * for (String field : fields) { String getterName = "get" + prefix + field;
	 * String setterName = "set" + prefix + field;
	 * 
	 * try { Method getter =
	 * BDISB2_Archival_Summary_Entity.class.getMethod(getterName); Method setter =
	 * BDISB2_Archival_Summary_Entity.class.getMethod(setterName,
	 * getter.getReturnType());
	 * 
	 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
	 * newValue);
	 * 
	 * } catch (NoSuchMethodException e) { // Skip missing fields continue; } } }
	 * 
	 * 
	 * 
	 * } catch (Exception e) { throw new
	 * RuntimeException("Error while updating report fields", e); }
	 * 
	 * // 3️⃣ Save updated entity BRRS_BDISB2_Archival_Summary_Repo.save(existing);
	 * }
	 */

	public byte[] getBDISB2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelBDISB2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<BDISB2_Archival_Summary_Entity> T1Master = BRRS_BDISB2_Archival_Summary_Repo
					.getdatabydateListarchival(reportDate, version);

			// Generate Excel for RESUB
			return BRRS_BDISB2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Default (LIVE) case
		List<BDISB2_Summary_Entity> dataList1 = BRRS_BDISB2_Summary_Repo.getdatabydateList(reportDate);

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

			int startRow = 5;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BDISB2_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM,
							cellN, cellO, cellP, cellQ;
					CellStyle originalStyle;

					// ===== R6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R6 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR6_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R6 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR6_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					
					// ===== R6 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR6_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R6 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR6_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R6 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR6_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R6 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR6_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R6 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR6_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R6 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR6_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R6 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR6_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R6 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR6_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR6_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R6 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR6_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R6 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R6 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR6_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R6 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR6_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR6_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R6 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR6_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R6 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR6_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R7 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR7_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R7 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR7_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R7 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR7_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R7 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR7_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R7 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR7_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R7 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR7_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R7 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR7_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R7 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR7_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R7 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR7_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R7 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR7_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR7_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R7 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR7_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R7 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R7 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR7_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R7 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR7_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR7_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R7 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR7_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R7 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR7_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R8 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR8_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R8 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR8_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R8 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR8_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R8 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR8_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R8 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR8_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R8 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR8_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R8 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR8_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R8 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR8_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R8 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR8_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R8 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR8_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR8_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R8 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR8_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R8 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R8 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR8_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R8 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR8_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR8_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R8 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR8_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R8 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR8_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R9 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR9_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R9 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR9_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R9 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR9_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R9 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR9_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R9 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR9_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R9 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR9_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R9 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR9_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R9 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR9_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R9 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR9_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R9 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR9_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR9_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R9 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR9_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R9 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R9 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR9_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R9 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR9_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR9_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R9 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR9_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R9 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR9_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R10 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR10_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R10 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR10_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R10 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR10_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R10 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR10_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R10 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR10_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R10 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR10_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R10 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR10_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R10 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR10_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R10 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR10_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R10 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR10_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR10_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R10 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR10_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R10 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R10 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR10_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R10 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR10_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR10_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R10 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR10_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R10 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR10_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R11 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR11_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR11_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR11_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR11_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR11_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR11_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR11_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR11_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR11_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR11_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR11_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R11 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR11_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R11 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R11 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR11_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R11 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR11_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR11_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R11 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR11_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R11 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR11_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R12 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR12_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR12_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR12_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR12_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR12_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR12_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR12_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR12_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR12_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR12_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR12_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R12 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR12_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R12 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R12 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR12_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R12 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR12_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR12_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R12 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR12_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R12 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR12_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR12_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

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

//	public List<Object> getBDISB2Archival() {
//		List<Object> BDISB2Archivallist = new ArrayList<>();
////		List<Object> BDISB2Archivallist2 = new ArrayList<>();
////		List<Object> BDISB2Archivallist3 = new ArrayList<>();
//		try {
//			BDISB2Archivallist = BRRS_BDISB2_Archival_Summary_Repo.getBDISB2archival();
//			
//			System.out.println("countser" + BDISB2Archivallist.size());
////			System.out.println("countser" + BDISB2Archivallist.size());
////			System.out.println("countser" + BDISB2Archivallist.size());
//		} catch (Exception e) {
//			// Log the exception
//			System.err.println("Error fetching BDISB2 Archival data: " + e.getMessage());
//			e.printStackTrace();
//
//			// Optionally, you can rethrow it or return empty list
//			// throw new RuntimeException("Failed to fetch data", e);
//		}
//		return BDISB2Archivallist;
//	}
//

	public byte[] getExcelBDISB2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
		List<BDISB2_Archival_Summary_Entity> dataList1 = BRRS_BDISB2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB2 report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BDISB2_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM,
							cellN, cellO, cellP, cellQ;
					CellStyle originalStyle;

					// ===== R6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R6 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR6_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R6 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR6_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					
					// ===== R6 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR6_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R6 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR6_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R6 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR6_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R6 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR6_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R6 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR6_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R6 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR6_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R6 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR6_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R6 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR6_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR6_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R6 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR6_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R6 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R6 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR6_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R6 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR6_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR6_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R6 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR6_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R6 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR6_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R7 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR7_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R7 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR7_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R7 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR7_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R7 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR7_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R7 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR7_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R7 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR7_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R7 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR7_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R7 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR7_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R7 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR7_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R7 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR7_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR7_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R7 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR7_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R7 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R7 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR7_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R7 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR7_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR7_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R7 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR7_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R7 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR7_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R8 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR8_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R8 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR8_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R8 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR8_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R8 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR8_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R8 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR8_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R8 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR8_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R8 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR8_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R8 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR8_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R8 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR8_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R8 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR8_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR8_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R8 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR8_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R8 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R8 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR8_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R8 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR8_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR8_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R8 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR8_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R8 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR8_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R9 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR9_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R9 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR9_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R9 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR9_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R9 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR9_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R9 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR9_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R9 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR9_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R9 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR9_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R9 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR9_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R9 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR9_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R9 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR9_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR9_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R9 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR9_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R9 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R9 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR9_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R9 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR9_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR9_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R9 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR9_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R9 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR9_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R10 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR10_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R10 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR10_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R10 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR10_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R10 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR10_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R10 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR10_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R10 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR10_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R10 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR10_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R10 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR10_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R10 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR10_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R10 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR10_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR10_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R10 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR10_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R10 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R10 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR10_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R10 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR10_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR10_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R10 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR10_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R10 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR10_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R11 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR11_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR11_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR11_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR11_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR11_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR11_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR11_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR11_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR11_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR11_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR11_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R11 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR11_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R11 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R11 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR11_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R11 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR11_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR11_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R11 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR11_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R11 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR11_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R12 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR12_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR12_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR12_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR12_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR12_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR12_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR12_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR12_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR12_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR12_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR12_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R12 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR12_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R12 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R12 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR12_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R12 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR12_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR12_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R12 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR12_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R12 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR12_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR12_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

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

//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
	public List<Object[]> getBDISB2Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<BDISB2_Archival_Summary_Entity> latestArchivalList = BRRS_BDISB2_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (BDISB2_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching BDISB2 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getBDISB2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<BDISB2_Archival_Summary_Entity> repoData = BRRS_BDISB2_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (BDISB2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				BDISB2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BDISB2 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	@Transactional
	public void updateReportReSub(BDISB2_Summary_Entity updatedEntity) {

	    System.out.println("Came to Resub Service");

	    Date reportDate = updatedEntity.getReportDate();
	    System.out.println("Report Date: " + reportDate);

	    try {
	        /* =========================================================
	         * 1️⃣ FETCH LATEST ARCHIVAL VERSION
	         * ========================================================= */
	        Optional<BDISB2_Archival_Summary_Entity> latestArchivalOpt =
	                BRRS_BDISB2_Archival_Summary_Repo
	                        .getLatestArchivalVersionByDate(reportDate);

	        int newVersion = 1;
	        if (latestArchivalOpt.isPresent()) {
	            try {
	                newVersion =
	                        Integer.parseInt(latestArchivalOpt.get().getReportVersion()) + 1;
	            } catch (NumberFormatException e) {
	                newVersion = 1;
	            }
	        }

	        boolean exists =
	                BRRS_BDISB2_Archival_Summary_Repo
	                        .findByReportDateAndReportVersion(
	                                reportDate, String.valueOf(newVersion))
	                        .isPresent();

	        if (exists) {
	            throw new RuntimeException(
	                    "Version " + newVersion + " already exists for report date " + reportDate);
	        }

	        /* =========================================================
	         * 2️⃣ CREATE NEW ARCHIVAL ENTITY (BASE COPY)
	         * ========================================================= */
	        BDISB2_Archival_Summary_Entity archivalEntity =
	                new BDISB2_Archival_Summary_Entity();

	        if (latestArchivalOpt.isPresent()) {
	            BeanUtils.copyProperties(latestArchivalOpt.get(), archivalEntity);
	        }

	        /* =========================================================
	         * 3️⃣ READ RAW REQUEST PARAMETERS (CRITICAL FIX)
	         * ========================================================= */
	        HttpServletRequest request =
	                ((ServletRequestAttributes) RequestContextHolder
	                        .getRequestAttributes()).getRequest();

	        Map<String, String[]> parameterMap = request.getParameterMap();

	        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

	            String key = entry.getKey();              // R6_C11_ACCT_NUM
	            String value = entry.getValue()[0];

	            // Ignore non-field params
	            if ("asondate".equalsIgnoreCase(key) || "type".equalsIgnoreCase(key)) {
	                continue;
	            }

	            // Normalize: R6_C11_ACCT_NUM → R6_ACCT_NUM
	            String normalizedKey = key.replaceFirst("_C\\d+_", "_");

	            /* =====================================================
	             * 4️⃣ APPLY VALUES (EXPLICIT, SAFE, NO REFLECTION)
	             * ===================================================== */

	         // ======================= R6 =======================
	            
	                   if ("R6_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R6_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR6_COMPANY_NAME(value);
	            } else if ("R6_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR6_COMPANY_REG_NUM(value);
	            } else if ("R6_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR6_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R6_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR6_POSTAL_ADDRESS(value);
	            } else if ("R6_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR6_COUNTRY_OF_REG(value);
	            } else if ("R6_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR6_COMPANY_EMAIL(value);
	            } else if ("R6_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR6_COMPANY_LANDLINE(value);
	            } else if ("R6_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR6_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R6_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR6_PRODUCT_TYPE(value);
	            } else if ("R6_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR6_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R6_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR6_STATUS_OF_ACCT(value);
	            } else if ("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R6_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR6_ACCT_BRANCH(value);
	            } else if ("R6_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR6_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R6_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR6_CURRENCY_OF_ACCT(value);
	            } else if ("R6_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR6_EXCHANGE_RATE(parseBigDecimal(value));
	            }

	            // ======================= R7 =======================
	            else if ("R7_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R7_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR7_COMPANY_NAME(value);
	            } else if ("R7_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR7_COMPANY_REG_NUM(value);
	            } else if ("R7_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR7_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R7_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR7_POSTAL_ADDRESS(value);
	            } else if ("R7_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR7_COUNTRY_OF_REG(value);
	            } else if ("R7_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR7_COMPANY_EMAIL(value);
	            } else if ("R7_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR7_COMPANY_LANDLINE(value);
	            } else if ("R7_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR7_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R7_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR7_PRODUCT_TYPE(value);
	            } else if ("R7_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR7_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R7_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR7_STATUS_OF_ACCT(value);
	            } else if ("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R7_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR7_ACCT_BRANCH(value);
	            } else if ("R7_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR7_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R7_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR7_CURRENCY_OF_ACCT(value);
	            } else if ("R7_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR7_EXCHANGE_RATE(parseBigDecimal(value));
	            }

	            // ======================= R8 =======================
	            else if ("R8_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R8_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR8_COMPANY_NAME(value);
	            } else if ("R8_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR8_COMPANY_REG_NUM(value);
	            } else if ("R8_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR8_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R8_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR8_POSTAL_ADDRESS(value);
	            } else if ("R8_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR8_COUNTRY_OF_REG(value);
	            } else if ("R8_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR8_COMPANY_EMAIL(value);
	            } else if ("R8_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR8_COMPANY_LANDLINE(value);
	            } else if ("R8_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR8_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R8_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR8_PRODUCT_TYPE(value);
	            } else if ("R8_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR8_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R8_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR8_STATUS_OF_ACCT(value);
	            } else if ("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R8_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR8_ACCT_BRANCH(value);
	            } else if ("R8_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR8_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R8_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR8_CURRENCY_OF_ACCT(value);
	            } else if ("R8_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR8_EXCHANGE_RATE(parseBigDecimal(value));
	            }

	            // ======================= R9 =======================
	            else if ("R9_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R9_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR9_COMPANY_NAME(value);
	            } else if ("R9_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR9_COMPANY_REG_NUM(value);
	            } else if ("R9_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR9_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R9_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR9_POSTAL_ADDRESS(value);
	            } else if ("R9_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR9_COUNTRY_OF_REG(value);
	            } else if ("R9_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR9_COMPANY_EMAIL(value);
	            } else if ("R9_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR9_COMPANY_LANDLINE(value);
	            } else if ("R9_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR9_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R9_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR9_PRODUCT_TYPE(value);
	            } else if ("R9_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR9_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R9_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR9_STATUS_OF_ACCT(value);
	            } else if ("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R9_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR9_ACCT_BRANCH(value);
	            } else if ("R9_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR9_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R9_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR9_CURRENCY_OF_ACCT(value);
	            } else if ("R9_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR9_EXCHANGE_RATE(parseBigDecimal(value));
	            }

	            // ======================= R10 =======================
	            else if ("R10_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R10_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR10_COMPANY_NAME(value);
	            } else if ("R10_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR10_COMPANY_REG_NUM(value);
	            } else if ("R10_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR10_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R10_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR10_POSTAL_ADDRESS(value);
	            } else if ("R10_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR10_COUNTRY_OF_REG(value);
	            } else if ("R10_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR10_COMPANY_EMAIL(value);
	            } else if ("R10_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR10_COMPANY_LANDLINE(value);
	            } else if ("R10_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR10_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R10_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR10_PRODUCT_TYPE(value);
	            } else if ("R10_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR10_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R10_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR10_STATUS_OF_ACCT(value);
	            } else if ("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R10_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR10_ACCT_BRANCH(value);
	            } else if ("R10_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR10_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R10_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR10_CURRENCY_OF_ACCT(value);
	            } else if ("R10_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR10_EXCHANGE_RATE(parseBigDecimal(value));
	            }

	            // ======================= R11 =======================
	            else if ("R11_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R11_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR11_COMPANY_NAME(value);
	            } else if ("R11_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR11_COMPANY_REG_NUM(value);
	            } else if ("R11_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR11_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R11_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR11_POSTAL_ADDRESS(value);
	            } else if ("R11_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR11_COUNTRY_OF_REG(value);
	            } else if ("R11_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR11_COMPANY_EMAIL(value);
	            } else if ("R11_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR11_COMPANY_LANDLINE(value);
	            } else if ("R11_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR11_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R11_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR11_PRODUCT_TYPE(value);
	            } else if ("R11_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR11_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R11_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR11_STATUS_OF_ACCT(value);
	            } else if ("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R11_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR11_ACCT_BRANCH(value);
	            } else if ("R11_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR11_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R11_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR11_CURRENCY_OF_ACCT(value);
	            } else if ("R11_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR11_EXCHANGE_RATE(parseBigDecimal(value));
	            }

	            // ======================= R12 =======================
	            else if ("R12_BANK_SPEC_SINGLE_CUST_REC_NUM".equals(normalizedKey)) {
		            archivalEntity.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(value);
	            } else if ("R12_COMPANY_NAME".equals(normalizedKey)) {
	                archivalEntity.setR12_COMPANY_NAME(value);
	            } else if ("R12_COMPANY_REG_NUM".equals(normalizedKey)) {
	                archivalEntity.setR12_COMPANY_REG_NUM(value);
	            } else if ("R12_BUSINEES_PHY_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR12_BUSINEES_PHY_ADDRESS(value);
	            } else if ("R12_POSTAL_ADDRESS".equals(normalizedKey)) {
	                archivalEntity.setR12_POSTAL_ADDRESS(value);
	            } else if ("R12_COUNTRY_OF_REG".equals(normalizedKey)) {
	                archivalEntity.setR12_COUNTRY_OF_REG(value);
	            } else if ("R12_COMPANY_EMAIL".equals(normalizedKey)) {
	                archivalEntity.setR12_COMPANY_EMAIL(value);
	            } else if ("R12_COMPANY_LANDLINE".equals(normalizedKey)) {
	                archivalEntity.setR12_COMPANY_LANDLINE(value);
	            } else if ("R12_COMPANY_MOB_PHONE_NUM".equals(normalizedKey)) {
	                archivalEntity.setR12_COMPANY_MOB_PHONE_NUM(value);
	            } else if ("R12_PRODUCT_TYPE".equals(normalizedKey)) {
	                archivalEntity.setR12_PRODUCT_TYPE(value);
	            } else if ("R12_ACCT_NUM".equals(normalizedKey)) {
	                archivalEntity.setR12_ACCT_NUM(parseBigDecimal(value));
	            } else if ("R12_STATUS_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR12_STATUS_OF_ACCT(value);
	            } else if ("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT".equals(normalizedKey)) {
	                archivalEntity.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(value);
	            } else if ("R12_ACCT_BRANCH".equals(normalizedKey)) {
	                archivalEntity.setR12_ACCT_BRANCH(value);
	            } else if ("R12_ACCT_BALANCE_PULA".equals(normalizedKey)) {
	                archivalEntity.setR12_ACCT_BALANCE_PULA(parseBigDecimal(value));
	            } else if ("R12_CURRENCY_OF_ACCT".equals(normalizedKey)) {
	                archivalEntity.setR12_CURRENCY_OF_ACCT(value);
	            } else if ("R12_EXCHANGE_RATE".equals(normalizedKey)) {
	                archivalEntity.setR12_EXCHANGE_RATE(parseBigDecimal(value));
	            }
	        }

	        /* =========================================================
	         * 5️⃣ SET RESUB METADATA
	         * ========================================================= */
	        archivalEntity.setReportDate(reportDate);
	        archivalEntity.setReportVersion(String.valueOf(newVersion));
	        archivalEntity.setReportResubDate(new Date());

	        /* =========================================================
	         * 6️⃣ SAVE NEW ARCHIVAL VERSION
	         * ========================================================= */
	        BRRS_BDISB2_Archival_Summary_Repo.save(archivalEntity);

	        System.out.println("✅ RESUB saved successfully. Version = " + newVersion);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(
	                "Error while creating archival resubmission record", e);
	    }
	}

	private BigDecimal parseBigDecimal(String value) {
	    return (value == null || value.trim().isEmpty())
	            ? BigDecimal.ZERO
	            : new BigDecimal(value.replace(",", ""));
	}


	
	/*
	 * // Resubmit the values , latest version and Resub Date public void
	 * updateReportReSub(BDISB2_Summary_Entity updatedEntity) {
	 * System.out.println("Came to Resub Service");
	 * System.out.println("Report Date: " + updatedEntity.getReportDate());
	 * 
	 * Date reportDate = updatedEntity.getReportDate(); int newVersion = 1;
	 * 
	 * try { // Fetch the latest archival version for this report date
	 * Optional<BDISB2_Archival_Summary_Entity> latestArchivalOpt =
	 * BRRS_BDISB2_Archival_Summary_Repo
	 * .getLatestArchivalVersionByDate(reportDate);
	 * 
	 * // Determine next version number if (latestArchivalOpt.isPresent()) {
	 * BDISB2_Archival_Summary_Entity latestArchival = latestArchivalOpt.get(); try
	 * { newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1; }
	 * catch (NumberFormatException e) {
	 * System.err.println("Invalid version format. Defaulting to version 1");
	 * newVersion = 1; } } else {
	 * System.out.println("No previous archival found for date: " + reportDate); }
	 * 
	 * // Prevent duplicate version number boolean exists =
	 * BRRS_BDISB2_Archival_Summary_Repo
	 * .findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
	 * .isPresent(); if (exists) { throw new RuntimeException("Version " +
	 * newVersion + " already exists for report date " + reportDate); }
	 * 
	 * // Copy summary entity to archival entity BDISB2_Archival_Summary_Entity
	 * archivalEntity = new BDISB2_Archival_Summary_Entity();
	 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity,
	 * archivalEntity);
	 * 
	 * archivalEntity.setReportDate(reportDate);
	 * archivalEntity.setReportVersion(String.valueOf(newVersion));
	 * archivalEntity.setReportResubDate(new Date());
	 * 
	 * System.out.println("Saving new archival version: " + newVersion);
	 * 
	 * // Save new version to repository
	 * BRRS_BDISB2_Archival_Summary_Repo.save(archivalEntity);
	 * 
	 * System.out.println(" Saved archival version successfully: " + newVersion);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); throw new
	 * RuntimeException("Error while creating archival resubmission record", e); } }
	 */
	/// Downloaded for Archival & Resub
	public byte[] BRRS_BDISB2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<BDISB2_Archival_Summary_Entity> dataList1 = BRRS_BDISB2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB2 report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BDISB2_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM,
							cellN, cellO, cellP, cellQ;
					CellStyle originalStyle;

					// ===== R6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R6 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR6_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R6 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR6_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);
					
					// ===== R6 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR6_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R6 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR6_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R6 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR6_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R6 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR6_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R6 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR6_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R6 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR6_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R6 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR6_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R6 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR6_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR6_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R6 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR6_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R6 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R6 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR6_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R6 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR6_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR6_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R6 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR6_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R6 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR6_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R7 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR7_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R7 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR7_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R7 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR7_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R7 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR7_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R7 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR7_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R7 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR7_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R7 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR7_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R7 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR7_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R7 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR7_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R7 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR7_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR7_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R7 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR7_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R7 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R7 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR7_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R7 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR7_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR7_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R7 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR7_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R7 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR7_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R8 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR8_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R8 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR8_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R8 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR8_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R8 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR8_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R8 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR8_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R8 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR8_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R8 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR8_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R8 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR8_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R8 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR8_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R8 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR8_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR8_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R8 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR8_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R8 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R8 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR8_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R8 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR8_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR8_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R8 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR8_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R8 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR8_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R9 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR9_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R9 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR9_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R9 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR9_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R9 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR9_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R9 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR9_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R9 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR9_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R9 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR9_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R9 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR9_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R9 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR9_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R9 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR9_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR9_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R9 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR9_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R9 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R9 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR9_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R9 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR9_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR9_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R9 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR9_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R9 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR9_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R10 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR10_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R10 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR10_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R10 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR10_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R10 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR10_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R10 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR10_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R10 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR10_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R10 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR10_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R10 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR10_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R10 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR10_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R10 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR10_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR10_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R10 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR10_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R10 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R10 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR10_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R10 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR10_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR10_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R10 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR10_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R10 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR10_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R11 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR11_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR11_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR11_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR11_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR11_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR11_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR11_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR11_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR11_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR11_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR11_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R11 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR11_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R11 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R11 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR11_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R11 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR11_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR11_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R11 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR11_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R11 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR11_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R12 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR12_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR12_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR12_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR12_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR12_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR12_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR12_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR12_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR12_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR12_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR12_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R12 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR12_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R12 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R12 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR12_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R12 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR12_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR12_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R12 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR12_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R12 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR12_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR12_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

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

	public byte[] getBDISB2DetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BDISB2 Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("BDISB2Detail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
CellStyle headerStyle = workbook.createCellStyle();
Font headerFont = workbook.createFont();
headerFont.setBold(true);
headerFont.setFontHeightInPoints((short) 10);
headerStyle.setFont(headerFont);
headerStyle.setAlignment(HorizontalAlignment.LEFT);
headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
headerStyle.setBorderTop(border);
headerStyle.setBorderBottom(border);
headerStyle.setBorderLeft(border);
headerStyle.setBorderRight(border);

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with thousand separator)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);

//Header row
//String[] headers = {
//"CUST ID", "ACCT NO", "ACCT NAME","BANK_SPEC_SINGLE_CUST_REC_NUM","COMPANY_NAME ","COMPANY_REG_NUM",
//"BUSINEES_PHY_ADDRESS","POSTAL_ADDRESS","COUNTRY_OF_REG","COMPANY_EMAIL",
//"COMPANY_LANDLINE","COMPANY_MOB_PHONE_NUM","PRODUCT_TYPE","ACCT_NUM","STATUS_OF_ACCT",
//"ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT",
//"ACCT_BRANCH",
//"ACCT BALANCE IN PULA","CURRENCY_OF_ACCT","EXCHANGE_RATE", "REPORT LABEL", "REPORT ADDL CRITERIA1",
//"REPORT_DATE"
//};
String[] headers = {
"COMPANY_NAME ","COMPANY_REG_NUM",
"ACCT_NUM",
"ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA1",
"REPORT_DATE"
};

XSSFRow headerRow = sheet.createRow(0);
for (int i = 0; i < headers.length; i++) {
Cell cell = headerRow.createCell(i);
cell.setCellValue(headers[i]);

if (i == 3) { // ACCT BALANCE
cell.setCellStyle(rightAlignedHeaderStyle);
} else {
cell.setCellStyle(headerStyle);
}

sheet.setColumnWidth(i, 5000);
}

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<BDISB2_Detail_Entity> reportData = BRRS_BDISB2_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (BDISB2_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCOMPANY_NAME());
row.createCell(1).setCellValue(item.getCOMPANY_REG_NUM());

Cell bankSpecSingleCell = row.createCell(2);
if (item.getACCT_NUM() != null) {
	bankSpecSingleCell.setCellValue(item.getACCT_NUM().doubleValue());
} else {
	bankSpecSingleCell.setCellValue(0);
}

//ACCT BALANCE (right aligned, 3 decimal places)
Cell balanceCell = row.createCell(3);
if (item.getACCT_BALANCE_PULA() != null) {
balanceCell.setCellValue(item.getACCT_BALANCE_PULA().doubleValue());
} else {
balanceCell.setCellValue(0);
}
balanceCell.setCellStyle(balanceStyle);

 row.createCell(4).setCellValue(item.getReportLabel());
 row.createCell(5).setCellValue(item.getReportAddlCriteria1());
 row.createCell(6)
		.setCellValue(item.getReportDate() != null
		? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()): "");

		// Apply data style for all other cells
		for (int j = 0; j < 6; j++) {
			if (j != 3) {
				row.getCell(j).setCellStyle(dataStyle);
			}
		}
	}
} else {
	logger.info("No data found for BDISB2 — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating BDISB2 Excel", e);
return new byte[0];
}
}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_BDISB2 ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("BDISB2Detail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
CellStyle headerStyle = workbook.createCellStyle();
Font headerFont = workbook.createFont();
headerFont.setBold(true);
headerFont.setFontHeightInPoints((short) 10);
headerStyle.setFont(headerFont);
headerStyle.setAlignment(HorizontalAlignment.LEFT);
headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
headerStyle.setBorderTop(border);
headerStyle.setBorderBottom(border);
headerStyle.setBorderLeft(border);
headerStyle.setBorderRight(border);

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with 3 decimals)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);


//Header row
String[] headers = {
		"COMPANY_NAME ","COMPANY_REG_NUM",
		"ACCT_NUM",
		"ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA1",
		"REPORT_DATE"
};

XSSFRow headerRow = sheet.createRow(0);
for (int i = 0; i < headers.length; i++) {
Cell cell = headerRow.createCell(i);
cell.setCellValue(headers[i]);

if (i == 3) { // ACCT BALANCE
cell.setCellStyle(rightAlignedHeaderStyle);
} else {
cell.setCellStyle(headerStyle);
}

sheet.setColumnWidth(i, 5000);
}

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<BDISB2_Archival_Detail_Entity> reportData = BRRS_BDISB2_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (BDISB2_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

//row.createCell(0).setCellValue(item.getCustId());
//row.createCell(1).setCellValue(item.getAcctNumber());
//row.createCell(2).setCellValue(item.getAcctName());
//
////ACCT BALANCE (right aligned, 3 decimal places with comma separator)
//Cell balanceCell = row.createCell(3);
//
//if (item.getAcctBalanceInpula() != null) {
//balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
//} else {
//balanceCell.setCellValue(0);
//}
//
//Create style with thousand separator and decimal point
DataFormat format = workbook.createDataFormat();

//Format: 1,234,567
balanceStyle.setDataFormat(format.getFormat("#,##0"));

//Right alignment (optional)
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

//balanceCell.setCellStyle(balanceStyle);

//row.createCell(4).setCellValue(item.getReportLabel());
//row.createCell(5).setCellValue(item.getReportAddlCriteria1());
//row.createCell(6).setCellValue(
//item.getReportDate() != null ?
//new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
//);
//
////Apply data style for all other cells
//for (int j = 0; j < 7; j++) {
//if (j != 3) {
//row.getCell(j).setCellStyle(dataStyle);
//}
//}
//}
//}
row.createCell(0).setCellValue(item.getCOMPANY_NAME());
row.createCell(1).setCellValue(item.getCOMPANY_REG_NUM());

Cell bankSpecSingleCell = row.createCell(2);
if (item.getACCT_NUM() != null) {
	bankSpecSingleCell.setCellValue(item.getACCT_NUM().doubleValue());
} else {
	bankSpecSingleCell.setCellValue(0);
}

//ACCT BALANCE (right aligned, 3 decimal places)
Cell balanceCell = row.createCell(3);
if (item.getACCT_BALANCE_PULA() != null) {
balanceCell.setCellValue(item.getACCT_BALANCE_PULA().doubleValue());
} else {
balanceCell.setCellValue(0);
}
balanceCell.setCellStyle(balanceStyle);

 row.createCell(4).setCellValue(item.getReportLabel());
 row.createCell(5).setCellValue(item.getReportAddlCriteria1());
 row.createCell(6)
		.setCellValue(item.getReportDate() != null
		? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()): "");

		// Apply data style for all other cells
		for (int j = 0; j < 6; j++) {
			if (j != 3) {
				row.getCell(j).setCellStyle(dataStyle);
			}
		}
	}
}
else {
logger.info("No data found for BDISB2 — only header will be written.");
}
//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating BDISB2 Excel", e);
return new byte[0];
}
}
	
}

