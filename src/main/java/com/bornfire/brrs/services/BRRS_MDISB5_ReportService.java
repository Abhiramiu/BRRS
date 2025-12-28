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
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_MDISB5_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_MDISB5_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_MDISB5_Summary_Repo3;
import com.bornfire.brrs.entities.MDISB5_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.MDISB5_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.MDISB5_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.MDISB5_Summary_Entity1;
import com.bornfire.brrs.entities.MDISB5_Summary_Entity2;
import com.bornfire.brrs.entities.MDISB5_Summary_Entity3;
import com.bornfire.brrs.entities.MDISB5_Detail_Entity;
import com.bornfire.brrs.entities.MDISB5_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_MDISB5_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_MDISB5_Archival_Detail_Repo;


@Component
@Service

public class BRRS_MDISB5_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MDISB5_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BRRS_MDISB5_Summary_Repo1	BRRS_MDISB5_Summary_Repo1;
	
	@Autowired
	BRRS_MDISB5_Summary_Repo2	BRRS_MDISB5_Summary_Repo2;
	
	@Autowired
	BRRS_MDISB5_Summary_Repo3	BRRS_MDISB5_Summary_Repo3;
	
	@Autowired
	BRRS_MDISB5_Archival_Summary_Repo1	BRRS_MDISB5_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_MDISB5_Archival_Summary_Repo2	BRRS_MDISB5_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_MDISB5_Archival_Summary_Repo3	BRRS_MDISB5_Archival_Summary_Repo3;
	
	@Autowired
	BRRS_MDISB5_Detail_Repo	BRRS_MDISB5_Detail_Repo;
	
	@Autowired
	BRRS_MDISB5_Archival_Detail_Repo	BRRS_MDISB5_Archival_Detail_Repo;
				

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getMDISB5View(String reportId, String fromdate, String todate, 
			String currency, String dtltype, Pageable pageable, String type, String version) 
	{
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

	 // ---------- CASE 1: ARCHIVAL ----------
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<MDISB5_Archival_Summary_Entity1> T1Master = 
            		BRRS_MDISB5_Archival_Summary_Repo1.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity2> T2Master = 
            		BRRS_MDISB5_Archival_Summary_Repo2.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity3> T3Master = 
            		BRRS_MDISB5_Archival_Summary_Repo3.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
            mv.addObject("reportsummary3", T3Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
        	List<MDISB5_Archival_Summary_Entity1> T1Master = 
            		BRRS_MDISB5_Archival_Summary_Repo1.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity2> T2Master = 
            		BRRS_MDISB5_Archival_Summary_Repo2.getdatabydateListarchival(d1, version);
            List<MDISB5_Archival_Summary_Entity3> T3Master = 
            		BRRS_MDISB5_Archival_Summary_Repo3.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
            mv.addObject("reportsummary3", T3Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<MDISB5_Summary_Entity1> T1Master = 
            		BRRS_MDISB5_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
            List<MDISB5_Summary_Entity2> T2Master = 
            		BRRS_MDISB5_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
            List<MDISB5_Summary_Entity3> T3Master = 
            		BRRS_MDISB5_Summary_Repo3.getdatabydateList(dateformat.parse(todate));
            
            
            System.out.println("T1Master Size "+T1Master.size());
            
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
            mv.addObject("reportsummary3", T3Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/MDISB5");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
	
	private <T> List<T> safeList(List<T> list) {
		if (list == null) return new ArrayList<>();
		return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		}
	
	public ModelAndView getMDISB5currentDtl(String reportId, String fromdate, String todate, String currency,
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
				List<MDISB5_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_MDISB5_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = BRRS_MDISB5_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<MDISB5_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_MDISB5_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_MDISB5_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					System.out.println("bdisb2 size is : " + T1Dt1.size());
					totalPages = BRRS_MDISB5_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/MDISB5");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	
	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

		System.out.println("Updating MDISB5 detail table");

		for (Map.Entry<String, String> entry : params.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();

			// Allow only valid MDISB5 keys
			if (!key.matches("R\\d+_C\\d+_(" + "NAME_OF_SHAREHOLDER|" + "PERCENTAGE_SHAREHOLDING|" + "NAME_OF_BOARD_MEMBERS|"
					+ "EXECUTIVE_OR_NONEXECUTIVE|" + "NAME|" + "DESIGNATION_OR_POSITION|" + "NUMBER_OF_ACCOUNTS|"
					+ "AMOUNT" + ")")) {
				continue;
			}

			// Parse key parts
			String[] parts = key.split("_");
			String reportLable = parts[0]; // R6, R7...
			String addlCriteria = parts[1]; // C1, C2...
			String columnName = key.replaceFirst("R\\d+_C\\d+_", "");

			// Fetch matching rows
			List<MDISB5_Detail_Entity> rows = BRRS_MDISB5_Detail_Repo
					.findByReportDateAndReportLableAndReportAddlCriteria1(reportDate, reportLable, addlCriteria);

			for (MDISB5_Detail_Entity row : rows) {

				// ---------- NUMERIC COLUMNS ----------
				if ("PERCENTAGE_SHAREHOLDING".equals(columnName)) {

					BigDecimal num = (value == null || value.trim().isEmpty()) ? BigDecimal.ZERO
							: new BigDecimal(value.replace(",", ""));
					row.setPERCENTAGE_SHAREHOLDING(num);

				} else if ("NUMBER_OF_ACCOUNTS".equals(columnName)) {

					BigDecimal num = (value == null || value.trim().isEmpty()) ? BigDecimal.ZERO
							: new BigDecimal(value.replace(",", ""));
					row.setNUMBER_OF_ACCOUNTS(num);

				} else if ("AMOUNT".equals(columnName)) {

					BigDecimal num = (value == null || value.trim().isEmpty()) ? BigDecimal.ZERO
							: new BigDecimal(value.replace(",", ""));
					row.setAMOUNT(num);

				}

				// ---------- STRING COLUMNS ----------
				else if ("NAME_OF_SHAREHOLDER".equals(columnName)) {
					row.setNAME_OF_SHAREHOLDER(value);

				} else if ("NAME_OF_BOARD_MEMBERS".equals(columnName)) {
					row.setNAME_OF_BOARD_MEMBERS(value);

				} else if ("EXECUTIVE_OR_NONEXECUTIVE".equals(columnName)) {
					row.setEXECUTIVE_OR_NONEXECUTIVE(value);

				} else if ("NAME".equals(columnName)) {
					row.setNAME(value);

				} else if ("DESIGNATION_OR_POSITION".equals(columnName)) {
					row.setDESIGNATION_OR_POSITION(value);

				} 
				// mark row as modified
				row.setModifyFlg("Y");
			}

			BRRS_MDISB5_Detail_Repo.saveAll(rows);
		}

		callSummaryProcedure(reportDate);
	}

	private void callSummaryProcedure(Date reportDate) {

		String sql = "{ call BRRS_MDISB5_SUMMARY_PROCEDURE(?) }";

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
	 * public void updateReport1(MDISB5_Summary_Entity1 updatedEntity) {
	 * System.out.println("Came to services1"); System.out.println("Report Date: " +
	 * updatedEntity.getReportDate());
	 * 
	 * MDISB5_Summary_Entity1 existing = BRRS_MDISB5_Summary_Repo1
	 * .findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
	 * .orElseThrow(() -> new RuntimeException( "Record not found for REPORT_DATE: "
	 * + updatedEntity.getReportDate()));
	 * 
	 * try { // 1️⃣ Loop from R5 to R15 and copy fields for (int i = 5; i <= 15;
	 * i++) { String prefix = "R" + i + "_";
	 * 
	 * String[] fields = { "NAME_OF_SHAREHOLDER", "PERCENTAGE_SHAREHOLDING",
	 * "NUMBER_OF_ACCOUNTS","AMOUNT"};
	 * 
	 * for (String field : fields) { String getterName = "get" + prefix + field;
	 * String setterName = "set" + prefix + field;
	 * 
	 * try { Method getter = MDISB5_Summary_Entity1.class.getMethod(getterName);
	 * Method setter = MDISB5_Summary_Entity1.class.getMethod(setterName,
	 * getter.getReturnType());
	 * 
	 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
	 * newValue);
	 * 
	 * } catch (NoSuchMethodException e) { // Skip missing fields continue; } } }
	 * 
	 * 
	 * } catch (Exception e) { throw new
	 * RuntimeException("Error while updating report fields", e); }
	 * 
	 * // 3️⃣ Save updated entity BRRS_MDISB5_Summary_Repo1.save(existing); }
	 * 
	 * 
	 * public void updateReport2(MDISB5_Summary_Entity2 updatedEntity) {
	 * System.out.println("Came to services2"); System.out.println("Report Date: " +
	 * updatedEntity.getReportDate());
	 * 
	 * MDISB5_Summary_Entity2 existing = BRRS_MDISB5_Summary_Repo2
	 * .findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
	 * .orElseThrow(() -> new RuntimeException( "Record not found for REPORT_DATE: "
	 * + updatedEntity.getReportDate()));
	 * 
	 * try { // 1️⃣ Loop from R20 to R33 and copy fields for (int i = 20; i <= 33;
	 * i++) { String prefix = "R" + i + "_";
	 * 
	 * String[] fields = { "NAME_OF_BOARD_MEMBERS", "EXECUTIVE_OR_NONEXECUTIVE",
	 * "NUMBER_OF_ACCOUNTS","AMOUNT"};
	 * 
	 * for (String field : fields) { String getterName = "get" + prefix + field;
	 * String setterName = "set" + prefix + field;
	 * 
	 * try { Method getter = MDISB5_Summary_Entity2.class.getMethod(getterName);
	 * Method setter = MDISB5_Summary_Entity2.class.getMethod(setterName,
	 * getter.getReturnType());
	 * 
	 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
	 * newValue);
	 * 
	 * } catch (NoSuchMethodException e) { // Skip missing fields continue; } } }
	 * 
	 * 
	 * } catch (Exception e) { throw new
	 * RuntimeException("Error while updating report fields", e); }
	 * 
	 * // 3️⃣ Save updated entity BRRS_MDISB5_Summary_Repo2.save(existing); }
	 * 
	 * 
	 * public void updateReport3(MDISB5_Summary_Entity3 updatedEntity) {
	 * System.out.println("Came to services3"); System.out.println("Report Date: " +
	 * updatedEntity.getReportDate());
	 * 
	 * MDISB5_Summary_Entity3 existing = BRRS_MDISB5_Summary_Repo3
	 * .findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
	 * .orElseThrow(() -> new RuntimeException( "Record not found for REPORT_DATE: "
	 * + updatedEntity.getReportDate()));
	 * 
	 * try { // 1️⃣ Loop from R37 to R44 and copy fields for (int i = 37; i <= 44;
	 * i++) { String prefix = "R" + i + "_";
	 * 
	 * String[] fields = { "NAME", "DESIGNATION_OR_POSITION",
	 * "NUMBER_OF_ACCOUNTS","AMOUNT"};
	 * 
	 * for (String field : fields) { String getterName = "get" + prefix + field;
	 * String setterName = "set" + prefix + field;
	 * 
	 * try { Method getter = MDISB5_Summary_Entity3.class.getMethod(getterName);
	 * Method setter = MDISB5_Summary_Entity3.class.getMethod(setterName,
	 * getter.getReturnType());
	 * 
	 * Object newValue = getter.invoke(updatedEntity); setter.invoke(existing,
	 * newValue);
	 * 
	 * } catch (NoSuchMethodException e) { // Skip missing fields continue; } } }
	 * 
	 * 
	 * } catch (Exception e) { throw new
	 * RuntimeException("Error while updating report fields", e); }
	 * 
	 * // 3️⃣ Save updated entity BRRS_MDISB5_Summary_Repo3.save(existing); }
	 * 
	 */	
	public byte[] getMDISB5Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMDISB5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<MDISB5_Archival_Summary_Entity1> T1Master = BRRS_MDISB5_Archival_Summary_Repo1
					.getdatabydateListarchival(reportDate, version);
			
			List<MDISB5_Archival_Summary_Entity2> T2Master = BRRS_MDISB5_Archival_Summary_Repo2
					.getdatabydateListarchival(reportDate, version);
			
			List<MDISB5_Archival_Summary_Entity3> T3Master = BRRS_MDISB5_Archival_Summary_Repo3
					.getdatabydateListarchival(reportDate, version);

			// Generate Excel for RESUB
			return BRRS_MDISB5ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Default (LIVE) case
		List<MDISB5_Summary_Entity1> dataList1 = BRRS_MDISB5_Summary_Repo1.getdatabydateList(reportDate);
		List<MDISB5_Summary_Entity2> dataList2 = BRRS_MDISB5_Summary_Repo2.getdatabydateList(reportDate);
		List<MDISB5_Summary_Entity3> dataList3 = BRRS_MDISB5_Summary_Repo3.getdatabydateList(reportDate);

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
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
// --- End of Style Definitions ---

			int startRow = 4;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					MDISB5_Summary_Entity1 record1 = dataList1.get(i);
					MDISB5_Summary_Entity2 record2 = dataList2.get(i);
					MDISB5_Summary_Entity3 record3 = dataList3.get(i);
					
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					

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
	
	
	public byte[] getExcelMDISB5ARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<MDISB5_Archival_Summary_Entity1> dataList1 = BRRS_MDISB5_Archival_Summary_Repo1
					.getdatabydateListarchival(dateformat.parse(todate), version);
			
			List<MDISB5_Archival_Summary_Entity2> dataList2 = BRRS_MDISB5_Archival_Summary_Repo2
					.getdatabydateListarchival(dateformat.parse(todate), version);
			
			List<MDISB5_Archival_Summary_Entity3> dataList3 = BRRS_MDISB5_Archival_Summary_Repo3
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for MDISB5 report. Returning empty result.");
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
			int startRow = 4;
			
			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					MDISB5_Archival_Summary_Entity1 record1 = dataList1.get(i);
					MDISB5_Archival_Summary_Entity2 record2 = dataList2.get(i);
					MDISB5_Archival_Summary_Entity3 record3 = dataList3.get(i);
					
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
					
					
					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME()); 
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);
					
					
					// Col B 
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value 
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					//Col C 

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					//Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);
					
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
	
	
	public List<Object[]> getMDISB5Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
		List<MDISB5_Archival_Summary_Entity1> latestArchivalList = 
				BRRS_MDISB5_Archival_Summary_Repo1.getdatabydateListWithVersionAll();

		if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
		for (MDISB5_Archival_Summary_Entity1 entity : latestArchivalList) {
		Object[] row = new Object[] {
		entity.getReportDate(),
		entity.getReportVersion()
		};
		resubList.add(row);
		}
		System.out.println("Fetched " + resubList.size() + " record(s)");
		} else {
		System.out.println("No archival data found.");
		}
		} catch (Exception e) {
		System.err.println("Error fetching MDISB5 Resub data: " + e.getMessage());
		e.printStackTrace();
		}
		return resubList;
		}
			
			
			//Archival View
			public List<Object[]> getMDISB5Archival() {
				List<Object[]> archivalList = new ArrayList<>();

				try {
					List<MDISB5_Archival_Summary_Entity1> repoData = BRRS_MDISB5_Archival_Summary_Repo1
							.getdatabydateListWithVersionAll();

					if (repoData != null && !repoData.isEmpty()) {
						for (MDISB5_Archival_Summary_Entity1 entity : repoData) {
							Object[] row = new Object[] {
									entity.getReportDate(), 
									entity.getReportVersion() 
							};
							archivalList.add(row);
						}

						System.out.println("Fetched " + archivalList.size() + " archival records");
						MDISB5_Archival_Summary_Entity1 first = repoData.get(0);
						System.out.println("Latest archival version: " + first.getReportVersion());
					} else {
						System.out.println("No archival data found.");
					}

				} catch (Exception e) {
					System.err.println("Error fetching MDISB5 Archival data: " + e.getMessage());
					e.printStackTrace();
				}

				return archivalList;
			}

			@Transactional
			public void updateReportReSub(
			        MDISB5_Summary_Entity1 updatedEntity1,
			        MDISB5_Summary_Entity2 updatedEntity2,
			        MDISB5_Summary_Entity3 updatedEntity3) {

			    System.out.println("Came to MDISB5 Resub Service");

			    Date reportDate = updatedEntity1.getReportDate();
			    System.out.println("Report Date: " + reportDate);

			    try {

			        /* =========================================================
			         * 1️⃣ FETCH LATEST VERSION
			         * ========================================================= */
			        Optional<MDISB5_Archival_Summary_Entity1> latestArchivalOpt =
			                BRRS_MDISB5_Archival_Summary_Repo1
			                        .getLatestArchivalVersionByDate(reportDate);

			        int newVersion = 1;

			        if (latestArchivalOpt.isPresent()) {
			            try {
			                newVersion = Integer.parseInt(
			                        latestArchivalOpt.get().getReportVersion()) + 1;
			            } catch (NumberFormatException e) {
			                newVersion = 1;
			            }
			        }

			        /* =========================================================
			         * 2️⃣ PREVENT DUPLICATE VERSION
			         * ========================================================= */
			        boolean exists =
			                BRRS_MDISB5_Archival_Summary_Repo1
			                        .findByReportDateAndReportVersion(
			                                reportDate, String.valueOf(newVersion))
			                        .isPresent();

			        if (exists) {
			            throw new RuntimeException(
			                    "Version " + newVersion + " already exists for report date " + reportDate);
			        }

			        /* =========================================================
			         * 3️⃣ CREATE ARCHIVAL ENTITIES
			         * ========================================================= */
			        MDISB5_Archival_Summary_Entity1 archival1 = new MDISB5_Archival_Summary_Entity1();
			        MDISB5_Archival_Summary_Entity2 archival2 = new MDISB5_Archival_Summary_Entity2();
			        MDISB5_Archival_Summary_Entity3 archival3 = new MDISB5_Archival_Summary_Entity3();

			        BeanUtils.copyProperties(updatedEntity1, archival1);
			        BeanUtils.copyProperties(updatedEntity2, archival2);
			        BeanUtils.copyProperties(updatedEntity3, archival3);


			        /* =========================================================
			         * 4️⃣ READ RAW FORM PARAMETERS
			         * ========================================================= */
			        HttpServletRequest request =
			                ((ServletRequestAttributes) RequestContextHolder
			                        .getRequestAttributes()).getRequest();

			        Map<String, String[]> params = request.getParameterMap();

			        for (Map.Entry<String, String[]> entry : params.entrySet()) {

			            String key = entry.getKey();
			            String value = entry.getValue()[0];

			            // ignore non-data params
			            if ("asondate".equalsIgnoreCase(key) ||
			                "type".equalsIgnoreCase(key)) {
			                continue;
			            }

			            // normalize
			            // R5_C3_AMOUNT -> R5_AMOUNT
			            String normalizedKey = key.replaceFirst("_C\\d+_", "_");


			            /* =========================================================
			             * ENTITY 1  (R5 – R15)
			             * ========================================================= */

			            // -------- R5 --------
			            if ("R5_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR5_NAME_OF_SHAREHOLDER(value);

			            else if ("R5_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR5_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R5_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR5_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R5_AMOUNT".equals(normalizedKey))
			                archival1.setR5_AMOUNT(parseDouble(value));


			            // -------- R6 --------
			            else if ("R6_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR6_NAME_OF_SHAREHOLDER(value);

			            else if ("R6_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR6_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R6_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR6_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R6_AMOUNT".equals(normalizedKey))
			                archival1.setR6_AMOUNT(parseDouble(value));


			         // -------- R7 --------
			            else if ("R7_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR7_NAME_OF_SHAREHOLDER(value);

			            else if ("R7_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR7_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R7_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR7_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R7_AMOUNT".equals(normalizedKey))
			                archival1.setR7_AMOUNT(parseDouble(value));


			            // -------- R8 --------
			            else if ("R8_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR8_NAME_OF_SHAREHOLDER(value);

			            else if ("R8_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR8_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R8_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR8_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R8_AMOUNT".equals(normalizedKey))
			                archival1.setR8_AMOUNT(parseDouble(value));


			            // -------- R9 --------
			            else if ("R9_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR9_NAME_OF_SHAREHOLDER(value);

			            else if ("R9_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR9_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R9_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR9_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R9_AMOUNT".equals(normalizedKey))
			                archival1.setR9_AMOUNT(parseDouble(value));


			            // -------- R10 --------
			            else if ("R10_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR10_NAME_OF_SHAREHOLDER(value);

			            else if ("R10_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR10_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R10_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR10_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R10_AMOUNT".equals(normalizedKey))
			                archival1.setR10_AMOUNT(parseDouble(value));


			            // -------- R11 --------
			            else if ("R11_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR11_NAME_OF_SHAREHOLDER(value);

			            else if ("R11_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR11_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R11_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR11_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R11_AMOUNT".equals(normalizedKey))
			                archival1.setR11_AMOUNT(parseDouble(value));


			            // -------- R12 --------
			            else if ("R12_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR12_NAME_OF_SHAREHOLDER(value);

			            else if ("R12_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR12_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R12_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR12_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R12_AMOUNT".equals(normalizedKey))
			                archival1.setR12_AMOUNT(parseDouble(value));


			            // -------- R13 --------
			            else if ("R13_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR13_NAME_OF_SHAREHOLDER(value);

			            else if ("R13_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR13_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R13_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR13_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R13_AMOUNT".equals(normalizedKey))
			                archival1.setR13_AMOUNT(parseDouble(value));


			            // -------- R14 --------
			            else if ("R14_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR14_NAME_OF_SHAREHOLDER(value);

			            else if ("R14_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR14_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R14_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR14_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R14_AMOUNT".equals(normalizedKey))
			                archival1.setR14_AMOUNT(parseDouble(value));


			            // -------- R15 --------
			            else if ("R15_NAME_OF_SHAREHOLDER".equals(normalizedKey))
			                archival1.setR15_NAME_OF_SHAREHOLDER(value);

			            else if ("R15_PERCENTAGE_SHAREHOLDING".equals(normalizedKey))
			                archival1.setR15_PERCENTAGE_SHAREHOLDING(parseDouble(value));

			            else if ("R15_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival1.setR15_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R15_AMOUNT".equals(normalizedKey))
			                archival1.setR15_AMOUNT(parseDouble(value));



			            /* =========================================================
			             * ENTITY 2  (R20 – R33)
			             * ========================================================= */

			            else if ("R20_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR20_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R20_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR20_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R20_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR20_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R20_AMOUNT".equals(normalizedKey))
			                archival2.setR20_AMOUNT(parseDouble(value));

			         // -------- R21 --------
			            else if ("R21_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR21_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R21_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR21_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R21_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR21_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R21_AMOUNT".equals(normalizedKey))
			                archival2.setR21_AMOUNT(parseDouble(value));


			            // -------- R22 --------
			            else if ("R22_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR22_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R22_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR22_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R22_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR22_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R22_AMOUNT".equals(normalizedKey))
			                archival2.setR22_AMOUNT(parseDouble(value));


			            // -------- R23 --------
			            else if ("R23_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR23_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R23_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR23_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R23_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR23_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R23_AMOUNT".equals(normalizedKey))
			                archival2.setR23_AMOUNT(parseDouble(value));


			            // -------- R24 --------
			            else if ("R24_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR24_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R24_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR24_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R24_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR24_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R24_AMOUNT".equals(normalizedKey))
			                archival2.setR24_AMOUNT(parseDouble(value));


			            // -------- R25 --------
			            else if ("R25_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR25_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R25_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR25_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R25_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR25_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R25_AMOUNT".equals(normalizedKey))
			                archival2.setR25_AMOUNT(parseDouble(value));


			            // -------- R26 --------
			            else if ("R26_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR26_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R26_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR26_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R26_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR26_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R26_AMOUNT".equals(normalizedKey))
			                archival2.setR26_AMOUNT(parseDouble(value));


			            // -------- R27 --------
			            else if ("R27_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR27_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R27_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR27_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R27_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR27_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R27_AMOUNT".equals(normalizedKey))
			                archival2.setR27_AMOUNT(parseDouble(value));


			            // -------- R28 --------
			            else if ("R28_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR28_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R28_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR28_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R28_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR28_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R28_AMOUNT".equals(normalizedKey))
			                archival2.setR28_AMOUNT(parseDouble(value));


			            // -------- R29 --------
			            else if ("R29_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR29_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R29_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR29_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R29_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR29_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R29_AMOUNT".equals(normalizedKey))
			                archival2.setR29_AMOUNT(parseDouble(value));


			            // -------- R30 --------
			            else if ("R30_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR30_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R30_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR30_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R30_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR30_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R30_AMOUNT".equals(normalizedKey))
			                archival2.setR30_AMOUNT(parseDouble(value));


			            // -------- R31 --------
			            else if ("R31_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR31_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R31_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR31_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R31_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR31_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R31_AMOUNT".equals(normalizedKey))
			                archival2.setR31_AMOUNT(parseDouble(value));


			            // -------- R32 --------
			            else if ("R32_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR32_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R32_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR32_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R32_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR32_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R32_AMOUNT".equals(normalizedKey))
			                archival2.setR32_AMOUNT(parseDouble(value));


			            // -------- R33 --------
			            else if ("R33_NAME_OF_BOARD_MEMBERS".equals(normalizedKey))
			                archival2.setR33_NAME_OF_BOARD_MEMBERS(value);

			            else if ("R33_EXECUTIVE_OR_NONEXECUTIVE".equals(normalizedKey))
			                archival2.setR33_EXECUTIVE_OR_NONEXECUTIVE(value);

			            else if ("R33_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival2.setR33_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R33_AMOUNT".equals(normalizedKey))
			                archival2.setR33_AMOUNT(parseDouble(value));




			            /* =========================================================
			             * ENTITY 3  (R37 – R44)
			             * ========================================================= */

			            else if ("R37_NAME".equals(normalizedKey))
			                archival3.setR37_NAME(value);

			            else if ("R37_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR37_DESIGNATION_OR_POSITION(value);

			            else if ("R37_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR37_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R37_AMOUNT".equals(normalizedKey))
			                archival3.setR37_AMOUNT(parseDouble(value));

			         // -------- R38 --------
			            else if ("R38_NAME".equals(normalizedKey))
			                archival3.setR38_NAME(value);

			            else if ("R38_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR38_DESIGNATION_OR_POSITION(value);

			            else if ("R38_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR38_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R38_AMOUNT".equals(normalizedKey))
			                archival3.setR38_AMOUNT(parseDouble(value));


			            // -------- R39 --------
			            else if ("R39_NAME".equals(normalizedKey))
			                archival3.setR39_NAME(value);

			            else if ("R39_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR39_DESIGNATION_OR_POSITION(value);

			            else if ("R39_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR39_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R39_AMOUNT".equals(normalizedKey))
			                archival3.setR39_AMOUNT(parseDouble(value));


			            // -------- R40 --------
			            else if ("R40_NAME".equals(normalizedKey))
			                archival3.setR40_NAME(value);

			            else if ("R40_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR40_DESIGNATION_OR_POSITION(value);

			            else if ("R40_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR40_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R40_AMOUNT".equals(normalizedKey))
			                archival3.setR40_AMOUNT(parseDouble(value));


			            // -------- R41 --------
			            else if ("R41_NAME".equals(normalizedKey))
			                archival3.setR41_NAME(value);

			            else if ("R41_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR41_DESIGNATION_OR_POSITION(value);

			            else if ("R41_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR41_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R41_AMOUNT".equals(normalizedKey))
			                archival3.setR41_AMOUNT(parseDouble(value));


			            // -------- R42 --------
			            else if ("R42_NAME".equals(normalizedKey))
			                archival3.setR42_NAME(value);

			            else if ("R42_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR42_DESIGNATION_OR_POSITION(value);

			            else if ("R42_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR42_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R42_AMOUNT".equals(normalizedKey))
			                archival3.setR42_AMOUNT(parseDouble(value));


			            // -------- R43 --------
			            else if ("R43_NAME".equals(normalizedKey))
			                archival3.setR43_NAME(value);

			            else if ("R43_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR43_DESIGNATION_OR_POSITION(value);

			            else if ("R43_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR43_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R43_AMOUNT".equals(normalizedKey))
			                archival3.setR43_AMOUNT(parseDouble(value));


			            // -------- R44 --------
			            else if ("R44_NAME".equals(normalizedKey))
			                archival3.setR44_NAME(value);

			            else if ("R44_DESIGNATION_OR_POSITION".equals(normalizedKey))
			                archival3.setR44_DESIGNATION_OR_POSITION(value);

			            else if ("R44_NUMBER_OF_ACCOUNTS".equals(normalizedKey))
			                archival3.setR44_NUMBER_OF_ACCOUNTS(parseDouble(value));

			            else if ("R44_AMOUNT".equals(normalizedKey))
			                archival3.setR44_AMOUNT(parseDouble(value));

			        }


			        /* =========================================================
			         * 5️⃣ SET METADATA
			         * ========================================================= */
			        Date now = new Date();

			        archival1.setReportDate(reportDate);
			        archival2.setReportDate(reportDate);
			        archival3.setReportDate(reportDate);

			        archival1.setReportVersion(String.valueOf(newVersion));
			        archival2.setReportVersion(String.valueOf(newVersion));
			        archival3.setReportVersion(String.valueOf(newVersion));

			        archival1.setREPORT_RESUBDATE(now);
			        archival2.setREPORT_RESUBDATE(now);
			        archival3.setREPORT_RESUBDATE(now);


			        /* =========================================================
			         * 6️⃣ SAVE ALL RECORDS
			         * ========================================================= */
			        BRRS_MDISB5_Archival_Summary_Repo1.save(archival1);
			        BRRS_MDISB5_Archival_Summary_Repo2.save(archival2);
			        BRRS_MDISB5_Archival_Summary_Repo3.save(archival3);

			        System.out.println("✅ RESUB saved successfully. Version = " + newVersion);

			    } catch (Exception e) {
			        e.printStackTrace();
			        throw new RuntimeException(
			                "Error while creating MDISB5 archival resubmission record", e);
			    }
			}
			
			private Double parseDouble(String value) {
			    return (value == null || value.trim().isEmpty())
			            ? 0.0
			            : Double.valueOf(value.replace(",", ""));
			}




			
			/*
			 * public void updateReportReSub( MDISB5_Summary_Entity1 updatedEntity1,
			 * MDISB5_Summary_Entity2 updatedEntity2, MDISB5_Summary_Entity3 updatedEntity3)
			 * {
			 * 
			 * System.out.println("Came to MDISB5 Resub Service");
			 * System.out.println("Report Date: " + updatedEntity1.getReportDate());
			 * 
			 * Date reportDate = updatedEntity1.getReportDate(); int newVersion = 1;
			 * 
			 * try { // 🔹 Fetch the latest archival version for this report date from
			 * Entity1 Optional<MDISB5_Archival_Summary_Entity1> latestArchivalOpt1 =
			 * BRRS_MDISB5_Archival_Summary_Repo1
			 * .getLatestArchivalVersionByDate(reportDate);
			 * 
			 * if (latestArchivalOpt1.isPresent()) { MDISB5_Archival_Summary_Entity1
			 * latestArchival = latestArchivalOpt1.get(); try { newVersion =
			 * Integer.parseInt(latestArchival.getReportVersion()) + 1; } catch
			 * (NumberFormatException e) {
			 * System.err.println("Invalid version format. Defaulting to version 1");
			 * newVersion = 1; } } else {
			 * System.out.println("No previous archival found for date: " + reportDate); }
			 * 
			 * // 🔹 Prevent duplicate version number in Repo1 boolean exists =
			 * BRRS_MDISB5_Archival_Summary_Repo1
			 * .findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
			 * .isPresent();
			 * 
			 * if (exists) { throw new RuntimeException("⚠ Version " + newVersion +
			 * " already exists for report date " + reportDate); }
			 * 
			 * // Copy data from summary to archival entities for all 3 entities
			 * MDISB5_Archival_Summary_Entity1 archivalEntity1 = new
			 * MDISB5_Archival_Summary_Entity1(); MDISB5_Archival_Summary_Entity2
			 * archivalEntity2 = new MDISB5_Archival_Summary_Entity2();
			 * MDISB5_Archival_Summary_Entity3 archivalEntity3 = new
			 * MDISB5_Archival_Summary_Entity3();
			 * 
			 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity1,
			 * archivalEntity1);
			 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity2,
			 * archivalEntity2);
			 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity3,
			 * archivalEntity3);
			 * 
			 * // Set common fields Date now = new Date();
			 * archivalEntity1.setReportDate(reportDate);
			 * archivalEntity2.setReportDate(reportDate);
			 * archivalEntity3.setReportDate(reportDate);
			 * 
			 * archivalEntity1.setReportVersion(String.valueOf(newVersion));
			 * archivalEntity2.setReportVersion(String.valueOf(newVersion));
			 * archivalEntity3.setReportVersion(String.valueOf(newVersion));
			 * 
			 * archivalEntity1.setREPORT_RESUBDATE(now);
			 * archivalEntity2.setREPORT_RESUBDATE(now);
			 * archivalEntity3.setREPORT_RESUBDATE(now);
			 * 
			 * System.out.println("Saving new archival version: " + newVersion);
			 * 
			 * // Save to all three archival repositories
			 * BRRS_MDISB5_Archival_Summary_Repo1.save(archivalEntity1);
			 * BRRS_MDISB5_Archival_Summary_Repo2.save(archivalEntity2);
			 * BRRS_MDISB5_Archival_Summary_Repo3.save(archivalEntity3);
			 * 
			 * System.out.println("Saved archival version successfully: " + newVersion);
			 * 
			 * } catch (Exception e) { e.printStackTrace(); throw new
			 * RuntimeException("Error while creating MDISB5 archival resubmission record",
			 * e); } }
			 * 
			 */			
			
		    public byte[] BRRS_MDISB5ResubExcel(String filename, String reportId, String fromdate,
		            String todate, String currency, String dtltype,
		            String type, String version) throws Exception {

		        logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		        if (type.equals("RESUB") & version != null) {

		        }

		        List<MDISB5_Archival_Summary_Entity1> dataList = BRRS_MDISB5_Archival_Summary_Repo1
		                .getdatabydateListarchival(dateformat.parse(todate), version);
		        List<MDISB5_Archival_Summary_Entity2> dataList1 = BRRS_MDISB5_Archival_Summary_Repo2
		                .getdatabydateListarchival(dateformat.parse(todate), version);
		        List<MDISB5_Archival_Summary_Entity3> dataList2 = BRRS_MDISB5_Archival_Summary_Repo3
		                .getdatabydateListarchival(dateformat.parse(todate), version);

		        if (dataList.isEmpty()) {
		            logger.warn("Service: No data found for MDISB5 report. Returning empty result.");
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

		                	MDISB5_Archival_Summary_Entity1 record1 = dataList.get(i);
		                	MDISB5_Archival_Summary_Entity2 record2 = dataList1.get(i);
		                	MDISB5_Archival_Summary_Entity3 record3 = dataList2.get(i);
		                    System.out.println("rownumber=" + startRow + i);

							Row row;
							Cell cellA, cellB, cellC, cellD;
							CellStyle originalStyle;

							// ===== Row 5 / Col A =====
							row = sheet.getRow(4);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// ===== Row 5 / Col B =====
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							// ===== R11 / Col C =====

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							// ===== R11 / Col D =====

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR5_AMOUNT() != null)
								cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 6 / Col A =====
							row = sheet.getRow(5);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR6_AMOUNT() != null)
								cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 7 / Col A =====
							row = sheet.getRow(6);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR7_AMOUNT() != null)
								cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 8 / Col A =====
							row = sheet.getRow(7);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR8_AMOUNT() != null)
								cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 9 / Col A =====
							row = sheet.getRow(8);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR9_AMOUNT() != null)
								cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 10 / Col A =====
							row = sheet.getRow(9);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR10_AMOUNT() != null)
								cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 11 / Col A =====
							row = sheet.getRow(10);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR11_AMOUNT() != null)
								cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 12 / Col A =====
							row = sheet.getRow(11);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR12_AMOUNT() != null)
								cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 13 / Col A =====
							row = sheet.getRow(12);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR13_AMOUNT() != null)
								cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 14 / Col A =====
							row = sheet.getRow(13);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR14_AMOUNT() != null)
								cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 15 / Col A =====
							row = sheet.getRow(14);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
								cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
								cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record1.getR15_AMOUNT() != null)
								cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 20 / Col A =====
							row = sheet.getRow(19);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR20_AMOUNT() != null)
								cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 21 / Col A =====
							row = sheet.getRow(20);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR21_AMOUNT() != null)
								cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 22 / Col A =====
							row = sheet.getRow(21);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR22_AMOUNT() != null)
								cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 23 / Col A =====
							row = sheet.getRow(22);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR23_AMOUNT() != null)
								cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 24 / Col A =====
							row = sheet.getRow(23);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR24_AMOUNT() != null)
								cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 25 / Col A =====
							row = sheet.getRow(24);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR25_AMOUNT() != null)
								cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 26 / Col A =====
							row = sheet.getRow(25);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR26_AMOUNT() != null)
								cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 27 / Col A =====
							row = sheet.getRow(26);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR27_AMOUNT() != null)
								cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 28 / Col A =====
							row = sheet.getRow(27);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR28_AMOUNT() != null)
								cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 29 / Col A =====
							row = sheet.getRow(28);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR29_AMOUNT() != null)
								cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 30 / Col A =====
							row = sheet.getRow(29);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR30_AMOUNT() != null)
								cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 31 / Col A =====
							row = sheet.getRow(30);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR31_AMOUNT() != null)
								cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 32 / Col A =====
							row = sheet.getRow(31);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR32_AMOUNT() != null)
								cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 33 / Col A =====
							row = sheet.getRow(32);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
								cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
								cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record2.getR33_AMOUNT() != null)
								cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							// ===== Row 37 / Col A =====
							row = sheet.getRow(36);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR37_NAME() != null)
								cellA.setCellValue(record3.getR37_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR37_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR37_AMOUNT() != null)
								cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 38 / Col A =====
							row = sheet.getRow(37);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR38_NAME() != null)
								cellA.setCellValue(record3.getR38_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR38_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR38_AMOUNT() != null)
								cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 39 / Col A =====
							row = sheet.getRow(38);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR39_NAME() != null)
								cellA.setCellValue(record3.getR39_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR39_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR39_AMOUNT() != null)
								cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 40 / Col A =====
							row = sheet.getRow(39);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR40_NAME() != null)
								cellA.setCellValue(record3.getR40_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR40_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR40_AMOUNT() != null)
								cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 41 / Col A =====
							row = sheet.getRow(40);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR41_NAME() != null)
								cellA.setCellValue(record3.getR41_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR41_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR41_AMOUNT() != null)
								cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 42 / Col A =====
							row = sheet.getRow(41);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR42_NAME() != null)
								cellA.setCellValue(record3.getR42_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR42_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR42_AMOUNT() != null)
								cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 43 / Col A =====
							row = sheet.getRow(42);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR43_NAME() != null)
								cellA.setCellValue(record3.getR43_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR43_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR43_AMOUNT() != null)
								cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
							
							
							// ===== Row 44 / Col A =====
							row = sheet.getRow(43);
							cellA = row.getCell(0);
							if (cellA == null)
								cellA = row.createCell(1);
							originalStyle = cellA.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR44_NAME() != null)
								cellA.setCellValue(record3.getR44_NAME()); 
							else
								cellA.setCellValue("");
							cellA.setCellStyle(originalStyle);
							
							
							// Col B 
							cellB = row.getCell(1);
							if (cellB == null)
								cellB = row.createCell(1);
							originalStyle = cellB.getCellStyle();
							// ✅ Handle String value 
							if (record3.getR44_DESIGNATION_OR_POSITION() != null)
								cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
							else
								cellB.setCellValue("");
							cellB.setCellStyle(originalStyle);

							//Col C 

							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							originalStyle = cellC.getCellStyle();
							if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
								cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
							else
								cellC.setCellValue("");
							cellC.setCellStyle(originalStyle);

							//Col D

							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							originalStyle = cellD.getCellStyle();
							if (record3.getR44_AMOUNT() != null)
								cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
							else
								cellD.setCellValue("");
							cellD.setCellStyle(originalStyle);
							
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
