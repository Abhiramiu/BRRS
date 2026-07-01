package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfile;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_PI_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PI_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ─── JDBC query methods ────────────────────────────────────────────────────

	private List<M_PI_Summary_Entity> getSummaryAll() {
		return jdbcTemplate.query("select * from BRRS_M_PI_SUMMARYTABLE", new M_PISummaryRowMapper());
	}

	private List<M_PI_Manual_Summary_Entity> getManualSummaryAll() {
		return jdbcTemplate.query("select * from BRRS_M_PI_MANUAL_SUMMARYTABLE", new M_PIManualSummaryRowMapper());
	}

	private M_PI_Manual_Summary_Entity getManualSummaryByDate(Date reportDate) {
		String sql = "select * from BRRS_M_PI_MANUAL_SUMMARYTABLE where REPORT_DATE = ?";
		List<M_PI_Manual_Summary_Entity> list = jdbcTemplate.query(sql, new Object[]{reportDate}, new M_PIManualSummaryRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	private List<M_PI_Detail_Entity> getDetailAll() {
		return jdbcTemplate.query("select * from BRRS_M_PI_DETAILTABLE", new M_PIDetailRowMapper());
	}

	private int getDetailCount(Date reportDate) {
		return jdbcTemplate.queryForObject("select count(*) from BRRS_M_PI_DETAILTABLE where REPORT_DATE = ?",
				new Object[]{reportDate}, Integer.class);
	}

	private List<M_PI_Detail_Entity> getDetailByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1, Date reportDate) {
		String sql = "select * from BRRS_M_PI_DETAILTABLE where REPORT_LABLE = ? and REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{reportLabel, reportAddlCriteria1, reportDate}, new M_PIDetailRowMapper());
	}

	private M_PI_Detail_Entity getDetailByAcctNumber(String acctNumber) {
		String sql = "SELECT * FROM BRRS_M_PI_DETAILTABLE WHERE ACCT_NUMBER = ?";
		List<M_PI_Detail_Entity> list = jdbcTemplate.query(sql, new Object[]{acctNumber}, new M_PIDetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	private List<M_PI_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "select * from BRRS_M_PI_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_PIArchivalSummaryRowMapper());
	}

	private List<M_PI_Manual_Archival_Summary_Entity> getManualArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "select * from BRRS_M_PI_MANUAL_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_PIManualArchivalSummaryRowMapper());
	}

	private List<Object> getManualArchivalVersionList() {
		String sql = "select REPORT_DATE, REPORT_VERSION from BRRS_M_PI_MANUAL_ARCHIVALTABLE_SUMMARY order by REPORT_VERSION";
		return jdbcTemplate.query(sql, (rs, rowNum) -> (Object) new Object[]{rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION")});
	}

	private List<M_PI_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, String version) {
		String sql = "select * from BRRS_M_PI_ARCHIVALTABLE_DETAIL where REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_PIArchivalDetailRowMapper());
	}

	private List<M_PI_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1, Date reportDate, String version) {
		String sql = "select * from BRRS_M_PI_ARCHIVALTABLE_DETAIL where REPORT_LABLE = ? and REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportLabel, reportAddlCriteria1, reportDate, version}, new M_PIArchivalDetailRowMapper());
	}

	private void updateManualSummary(M_PI_Manual_Summary_Entity entity) {
		String sql = "UPDATE BRRS_M_PI_MANUAL_SUMMARYTABLE SET R14_VALUE=?, R18_VALUE=?, R19_VALUE=?, R25_VALUE=? WHERE REPORT_DATE=?";
		jdbcTemplate.update(sql, entity.getR14_VALUE(), entity.getR18_VALUE(), entity.getR19_VALUE(), entity.getR25_VALUE(), entity.getREPORT_DATE());
	}

	private void updateDetailRecord(M_PI_Detail_Entity entity) {
		String sql = "UPDATE BRRS_M_PI_DETAILTABLE SET ACCT_NAME=?, ACCT_BALANCE_IN_PULA=? WHERE ACCT_NUMBER=?";
		jdbcTemplate.update(sql, entity.getAcctName(), entity.getAcctBalanceInpula(), entity.getAcctNumber());
	}

	// ──────────────────────────────────────────────────────────────────────────

	public ModelAndView getM_PIView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {


		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		
		
		
		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_PI_Archival_Summary_Entity> T1Master = new ArrayList<M_PI_Archival_Summary_Entity>();
			List<M_PI_Manual_Archival_Summary_Entity> T2Master = new ArrayList<M_PI_Manual_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);
				T2Master = getManualArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);

		} else {

			List<M_PI_Summary_Entity> T1Master = new ArrayList<M_PI_Summary_Entity>();
			List<M_PI_Manual_Summary_Entity> T2Master = new ArrayList<M_PI_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = getSummaryAll();
				T2Master = getManualSummaryAll();

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_PI");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_PIcurrentDtl(String reportId, String fromdate, String todate, String currency,
			  String dtltype, Pageable pageable, String Filter, String type, String version,HttpServletRequest req1,Model md) {

	int pageSize = pageable != null ? pageable.getPageSize() : 10;
	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	int totalPages = 0;

	ModelAndView mv = new ModelAndView();
	String userid = (String) req1.getSession().getAttribute("USERID");

	System.out.println("User Id Maker and Checker: " + userid);
	String role = userProfileRep.getUserRole(userid);
	md.addAttribute("role", role);
	System.out.println("Role: " + role);
//	Session hs = sessionFactory.getCurrentSession();

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
	
		if ("ARCHIVAL".equals(type) && version != null) {
			// 🔹 Archival branch
			List<M_PI_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = getArchivalDetailByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<M_PI_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = getDetailByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = getDetailAll();
				totalPages = getDetailCount(parsedDate);
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
	mv.setViewName("BRRS/M_PI");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}


	public void updateReport(M_PI_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_PI_Manual_Summary_Entity existing = Optional.ofNullable(getManualSummaryByDate(updatedEntity.getREPORT_DATE()))
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Loop for amount_2 fields
	        int[] amount2Rows = {14, 18, 19, 25};
	        for (int i : amount2Rows) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"VALUE"};

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_PI_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_PI_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }

	        // ✅ Save after all updates
	        updateManualSummary(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	}


	
	public byte[] getBRRS_M_PIExcel(String filename, String reportId, String fromdate, String todate, String currency,
	        String dtltype, String type, String format, BigDecimal version) throws Exception {

	    logger.info("Service: Starting Excel generation process in memory.");

	    // ARCHIVAL check
	    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
	        logger.info("Service: Generating ARCHIVAL report for version {}", version);
	        return getExcelM_PIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format, version);
	    }

	    List<M_PI_Summary_Entity> dataList = getSummaryAll();

	    List<M_PI_Manual_Summary_Entity> dataList1 = getManualSummaryAll();

	    if (dataList.isEmpty()) {
	        logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
	        return new byte[0];
	    }

	    String templateDir = env.getProperty("output.exportpathtemp");
	    Path templatePath = Paths.get(templateDir, filename);

	    logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

	    if (!Files.exists(templatePath)) {
	        throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
	    }

	    if (!Files.isReadable(templatePath)) {
	        throw new SecurityException(
	                "Template file exists but is not readable (check permissions): "
	                        + templatePath.toAbsolutePath());
	    }

	    try (InputStream templateInputStream = Files.newInputStream(templatePath);
	         Workbook workbook = WorkbookFactory.create(templateInputStream);
	         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

	        Sheet sheet = workbook.getSheetAt(0);

	        // ===========================
	        // STYLE DEFINITIONS
	        // ===========================

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

	        CellStyle percentStyle = workbook.createCellStyle();
	        percentStyle.cloneStyleFrom(numberStyle);
	        percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
	        percentStyle.setAlignment(HorizontalAlignment.RIGHT);

	        // ===========================

	        int startRow = 3;


			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_PI_Summary_Entity record = dataList.get(i);
					M_PI_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					//REPORT_DATE
					row = sheet.getRow(3);
					Cell cell1 = row.getCell(6);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
					    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					// row8
					// Column F
					row = sheet.getRow(7);
					Cell cell5 = row.getCell(5);
					if (record.getR8_VALUE() != null) {
						cell5.setCellValue(record.getR8_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);

					}

					// row8
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row8
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_VALUE() != null) {
						cell5.setCellValue(record.getR9_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row9
					// Column G
					cell6 = row.createCell(6);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row9
					// Column H
					cell7 = row.createCell(7);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row10
					row = sheet.getRow(9);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_VALUE() != null) {
						cell5.setCellValue(record.getR10_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cell6 = row.createCell(6);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cell7 = row.createCell(7);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_VALUE() != null) {
						cell5.setCellValue(record.getR11_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cell6 = row.createCell(6);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cell7 = row.createCell(7);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_VALUE() != null) {
						cell5.setCellValue(record.getR12_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR13_VALUE() != null) {
						cell5.setCellValue(record.getR13_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR14_VALUE() != null) {
						cell5.setCellValue(record1.getR14_VALUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR15_VALUE() != null) {
						cell5.setCellValue(record.getR15_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR16_VALUE() != null) {
						cell5.setCellValue(record.getR16_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR17_VALUE() != null) {
						cell5.setCellValue(record.getR17_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR18_VALUE() != null) {
						cell5.setCellValue(record1.getR18_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR19_VALUE() != null) {
						cell5.setCellValue(record1.getR19_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR20_VALUE() != null) {
						cell5.setCellValue(record.getR20_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR21_VALUE() != null) {
						cell5.setCellValue(record.getR21_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR22_VALUE() != null) {
						cell5.setCellValue(record.getR22_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR23_VALUE() != null) {
						cell5.setCellValue(record.getR23_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR24_VALUE() != null) {
						cell5.setCellValue(record.getR24_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR25_VALUE() != null) {
						cell5.setCellValue(record1.getR25_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR26_VALUE() != null) {
						cell5.setCellValue(record.getR26_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR27_VALUE() != null) {
						cell5.setCellValue(record.getR27_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

				}
				 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		            evaluator.setIgnoreMissingWorkbooks(true);
		            evaluator.evaluateAll();
		        }

		        workbook.write(out);

		        logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_PI SUMMARY", null, "BRRS_M_PI_SUMMARYTABLE");
				}
		        return out.toByteArray();
		    }
		}

	public byte[] BRRS_M_PIDetailExcel(String filename, String fromdate, String todate,
            String currency, String dtltype,
            String type, String version) {

ByteArrayOutputStream bos = new ByteArrayOutputStream();

try {

logger.info("Generating Excel for M_PI Details...");

// ✅ SAFE ARCHIVAL CHECK (Prevents NullPointerException)
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.isEmpty()) {
return getDetailExcelARCHIVAL(filename, fromdate, todate,
currency, dtltype, type, version);
}

// ✅ Validate todate
if (todate == null || todate.trim().isEmpty()) {
logger.error("To Date is NULL or Empty!");
return new byte[0];
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_PIDetail");

BorderStyle border = BorderStyle.THIN;

// ================= HEADER STYLE =================
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

CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);

// ================= HEADER ROW =================
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME",
"ACCT BALANCE IN PULA", "REPORT LABEL",
"REPORT ADDL CRITERIA1", "REPORT_DATE"
};

XSSFRow headerRow = sheet.createRow(0);

for (int i = 0; i < headers.length; i++) {
Cell cell = headerRow.createCell(i);
cell.setCellValue(headers[i]);

if (i == 3) {
cell.setCellStyle(rightAlignedHeaderStyle);
} else {
cell.setCellStyle(headerStyle);
}

sheet.setColumnWidth(i, 5000);
}

// ================= FETCH DATA =================
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

List<M_PI_Detail_Entity> reportData = getDetailAll();

logger.info("Fetched {} records",
reportData != null ? reportData.size() : 0);

if (reportData != null && !reportData.isEmpty()) {

int rowIndex = 1;

for (M_PI_Detail_Entity item : reportData) {

if (item == null) continue;

XSSFRow row = sheet.createRow(rowIndex++);

// Cust ID
Cell c0 = row.createCell(0);
c0.setCellValue(item.getCustId() != null ? item.getCustId() : "");
c0.setCellStyle(dataStyle);

// Acct No
Cell c1 = row.createCell(1);
c1.setCellValue(item.getAcctNumber() != null ? item.getAcctNumber() : "");
c1.setCellStyle(dataStyle);

// Acct Name
Cell c2 = row.createCell(2);
c2.setCellValue(item.getAcctName() != null ? item.getAcctName() : "");
c2.setCellStyle(dataStyle);

// Balance
Cell balanceCell = row.createCell(3);
if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0.000);
}
balanceCell.setCellStyle(balanceStyle);

// Report Label
Cell c4 = row.createCell(4);
c4.setCellValue(item.getReportLabel() != null ? item.getReportLabel() : "");
c4.setCellStyle(dataStyle);

// Addl Criteria
Cell c5 = row.createCell(5);
c5.setCellValue(item.getReportAddlCriteria1() != null ? item.getReportAddlCriteria1() : "");
c5.setCellStyle(dataStyle);

// Report Date
Cell c6 = row.createCell(6);
c6.setCellValue(item.getReportDate() != null
 ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
 : "");
c6.setCellStyle(dataStyle);
}
} else {
logger.info("No data found for M_PI — only header written.");
}

workbook.write(bos);
workbook.close();

logger.info("Excel generation completed successfully.");
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_PI Excel", e);
return new byte[0];
}
}
	
	
	public List<Object> getM_PIArchival() {
		List<Object> M_PIArchivallist = new ArrayList<>();
		try {
			M_PIArchivallist = getManualArchivalVersionList();
			M_PIArchivallist = getManualArchivalVersionList();
			System.out.println("countser" + M_PIArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_PI Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_PIArchivallist;
	}

	public byte[] getExcelM_PIARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_PIArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		
		List<M_PI_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);
		List<M_PI_Manual_Archival_Summary_Entity> dataList1 = getManualArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty() || dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_PI report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 3;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_PI_Archival_Summary_Entity record = dataList.get(i);
					M_PI_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					//REPORT_DATE
					row = sheet.getRow(3);
					Cell cell1 = row.getCell(6);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
					    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					// row8
					// Column F
					row = sheet.getRow(7);
					Cell cell5 = row.getCell(5);
					if (record.getR8_VALUE() != null) {
						cell5.setCellValue(record.getR8_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);

					}

					// row8
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row8
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_VALUE() != null) {
						cell5.setCellValue(record.getR9_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					
					
					// Row 9 - Single Cell (G + H combined)
					 cell6 = row.createCell(6);   // You can choose any column index

					Double value1 = record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
					        ? record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
					        : 0.0;

					Double value2 = record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
					        ? record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
					        : 0.0;

					double total = value1 + value2;

					// If both are null, keep cell empty
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() == null 
					        && record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() == null) {
					    
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					} else {
					    cell6.setCellValue(total);
					    cell6.setCellStyle(numberStyle);
					}
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row9
					// Column H
					cell7 = row.createCell(7);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row10
					row = sheet.getRow(9);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_VALUE() != null) {
						cell5.setCellValue(record.getR10_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cell6 = row.createCell(6);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cell7 = row.createCell(7);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_VALUE() != null) {
						cell5.setCellValue(record.getR11_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cell6 = row.createCell(6);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cell7 = row.createCell(7);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_VALUE() != null) {
						cell5.setCellValue(record.getR12_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR13_VALUE() != null) {
						cell5.setCellValue(record.getR13_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR14_VALUE() != null) {
						cell5.setCellValue(record1.getR14_VALUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR15_VALUE() != null) {
						cell5.setCellValue(record.getR15_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR16_VALUE() != null) {
						cell5.setCellValue(record.getR16_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR17_VALUE() != null) {
						cell5.setCellValue(record.getR17_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR18_VALUE() != null) {
						cell5.setCellValue(record1.getR18_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR19_VALUE() != null) {
						cell5.setCellValue(record1.getR19_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR20_VALUE() != null) {
						cell5.setCellValue(record.getR20_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR21_VALUE() != null) {
						cell5.setCellValue(record.getR21_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR22_VALUE() != null) {
						cell5.setCellValue(record.getR22_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR23_VALUE() != null) {
						cell5.setCellValue(record.getR23_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR24_VALUE() != null) {
						cell5.setCellValue(record.getR24_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR25_VALUE() != null) {
						cell5.setCellValue(record1.getR25_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR26_VALUE() != null) {
						cell5.setCellValue(record.getR26_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR27_VALUE() != null) {
						cell5.setCellValue(record.getR27_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_PI ARCHIVAL SUMMARY", null, "BRRS_M_PI_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) {
try {
logger.info("Generating Excel for BRRS_M_PI ARCHIVAL Details...");
System.out.println("came to Detail download service");
if (type.equals("ARCHIVAL") & version != null) {

}
XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_PIDetail");

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
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA", "REPORT_DATE"
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
List<M_PI_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate, version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_PI_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places with comma separator)
Cell balanceCell = row.createCell(3);

if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}

//Create style with thousand separator and decimal point
DataFormat format = workbook.createDataFormat();

//Format: 1,234,567
balanceStyle.setDataFormat(format.getFormat("#,##0"));

//Right alignment (optional)
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

balanceCell.setCellStyle(balanceStyle);

row.createCell(4).setCellValue(item.getReportLabel());
row.createCell(5).setCellValue(item.getReportAddlCriteria1());
row.createCell(6).setCellValue(
item.getReportDate() != null ?
new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
);

//Apply data style for all other cells
for (int j = 0; j < 7; j++) {
if (j != 3) {
row.getCell(j).setCellStyle(dataStyle);
}
}
}
} else {
logger.info("No data found for M_PI — only header will be written.");
}
//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_PI Excel", e);
return new byte[0];
}
}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_PI"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_PI_Detail_Entity dep3Entity = getDetailByAcctNumber(acctNo);
	        if (dep3Entity != null && dep3Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dep3Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", dep3Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_PI"); // ✅ match the report name

	    if (acctNo != null) {
	        M_PI_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
	        if (la1Entity != null && la1Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	            System.out.println(formattedDate);
	        }
	        mv.addObject("Data", la1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
	    try {
	        String acctNo = request.getParameter("acctNumber");
	        String provisionStr = request.getParameter("acctBalanceInpula");
	        String acctName = request.getParameter("acctName");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_PI_Detail_Entity existing = getDetailByAcctNumber(acctNo);
	        if (existing == null) {
	            logger.warn("No record found for ACCT_NO: {}", acctNo);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
	        }

	        boolean isChanged = false;

	        if (acctName != null && !acctName.isEmpty()) {
	            if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
	                existing.setAcctName(acctName);
	                isChanged = true;
	                logger.info("Account name updated to {}", acctName);
	            }
	        }

	        if (provisionStr != null && !provisionStr.isEmpty()) {
	            BigDecimal newProvision = new BigDecimal(provisionStr);
	            if (existing.getAcctBalanceInpula() == null ||
	                existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
	                existing.setAcctBalanceInpula(newProvision);
	                isChanged = true;
	                logger.info("Balance updated to {}", newProvision);
	            }
	        }
	        
	        

	        if (isChanged) {
	        	updateDetailRecord(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_PI_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_PI_SUMMARY_PROCEDURE(?); END;", formattedDate);
	                        logger.info("Procedure executed successfully after commit.");
	                    } catch (Exception e) {
	                        logger.error("Error executing procedure after commit", e);
	                    }
	                }
	            });

	            return ResponseEntity.ok("Record updated successfully!");
	        } else {
	            logger.info("No changes detected for ACCT_NO: {}", acctNo);
	            return ResponseEntity.ok("No changes were made.");
	        }

	    } catch (Exception e) {
	        logger.error("Error updating M_PI record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	
	// Archival Email Excel
			public byte[] BRRS_M_PIArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Archival Email Excel generation process in memory.");

				
				List<M_PI_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);
				List<M_PI_Manual_Archival_Summary_Entity> dataList1 = getManualArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

				if (dataList.isEmpty() || dataList1.isEmpty()) {
					logger.warn("Service: No data found for M_PI report. Returning empty result.");
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

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					// --- End of Style Definitions ---
					int startRow = 3;

					if (!dataList.isEmpty() || !dataList1.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_PI_Archival_Summary_Entity record = dataList.get(i);
							M_PI_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}


							//REPORT_DATE
							row = sheet.getRow(3);
							Cell cell1 = row.getCell(6);
							if (cell1 == null) {
							    cell1 = row.createCell(1);
							}

							if (record.getREPORT_DATE() != null) {
							    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
							    cell1.setCellStyle(dateStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							// row8
							// Column F
							row = sheet.getRow(7);
							Cell cell5 = row.getCell(5);
							if (record.getR8_VALUE() != null) {
								cell5.setCellValue(record.getR8_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);

							}

							// row8
							// Single Cell (example: Column G -> index 6)
							Cell cell6 = row.createCell(6);

							Double value1 = record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double value2 = record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double total = value1 + value2;

							if (value1 != 0.0 || value2 != 0.0) {
							    cell6.setCellValue(total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row9
							row = sheet.getRow(7);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR9_VALUE() != null) {
								cell5.setCellValue(record.getR9_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row9
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r9Value1 = record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r9Value2 = record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r9Total = r9Value1 + r9Value2;

							if (r9Value1 != 0.0 || r9Value2 != 0.0) {
							    cell6.setCellValue(r9Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}
							
							// row10
							row = sheet.getRow(8);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR10_VALUE() != null) {
								cell5.setCellValue(record.getR10_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row10
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r10Value1 = record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r10Value2 = record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r10Total = r10Value1 + r10Value2;

							if (r10Value1 != 0.0 || r10Value2 != 0.0) {
							    cell6.setCellValue(r10Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}
							
							// row11
							row = sheet.getRow(9);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR11_VALUE() != null) {
								cell5.setCellValue(record.getR11_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row11
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r11Value1 = record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r11Value2 = record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r11Total = r11Value1 + r11Value2;

							if (r11Value1 != 0.0 || r11Value2 != 0.0) {
							    cell6.setCellValue(r11Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(10);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR12_VALUE() != null) {
								cell5.setCellValue(record.getR12_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row12
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r12Value1 = record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r12Value2 = record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r12Total = r12Value1 + r12Value2;

							if (r12Value1 != 0.0 || r12Value2 != 0.0) {
							    cell6.setCellValue(r12Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(11);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR13_VALUE() != null) {
								cell5.setCellValue(record.getR13_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row13
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r13Value1 = record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r13Value2 = record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r13Total = r13Value1 + r13Value2;

							if (r13Value1 != 0.0 || r13Value2 != 0.0) {
							    cell6.setCellValue(r13Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(12);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR14_VALUE() != null) {
								cell5.setCellValue(record1.getR14_VALUE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row14
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r14Value1 = record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r14Value2 = record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r14Total = r14Value1 + r14Value2;

							if (r14Value1 != 0.0 || r14Value2 != 0.0) {
							    cell6.setCellValue(r14Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}
							
							// row15
							row = sheet.getRow(13);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR15_VALUE() != null) {
								cell5.setCellValue(record.getR15_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row15
							// Column G
							cell6 = row.createCell(6);
							if (record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(14);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR16_VALUE() != null) {
								cell5.setCellValue(record.getR16_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row16
							// Column G
							cell6 = row.createCell(6);
							if (record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(15);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR17_VALUE() != null) {
								cell5.setCellValue(record.getR17_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row17
							// Column G
							cell6 = row.createCell(6);
							if (record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(16);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR18_VALUE() != null) {
								cell5.setCellValue(record1.getR18_VALUE().doubleValue() / 100);
								cell5.setCellStyle(percentStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row18
							// Column G
							cell6 = row.createCell(6);
							if (record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(17);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR19_VALUE() != null) {
								cell5.setCellValue(record1.getR19_VALUE().doubleValue() / 100);
								cell5.setCellStyle(percentStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row19
							// Column G
							cell6 = row.createCell(6);
							if (record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(18);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR20_VALUE() != null) {
								cell5.setCellValue(record.getR20_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row20
							// Column G
							cell6 = row.createCell(6);
							if (record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(19);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR21_VALUE() != null) {
								cell5.setCellValue(record.getR21_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row21
							// Column G
							cell6 = row.createCell(6);
							if (record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(20);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR22_VALUE() != null) {
								cell5.setCellValue(record.getR22_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row22
							// Column G
							cell6 = row.createCell(6);
							if (record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(21);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR23_VALUE() != null) {
								cell5.setCellValue(record.getR23_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row23
							// Column G
							cell6 = row.createCell(6);
							if (record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(22);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR24_VALUE() != null) {
								cell5.setCellValue(record.getR24_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row24
							// Column G
							cell6 = row.createCell(6);
							if (record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(23);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR25_VALUE() != null) {
								cell5.setCellValue(record1.getR25_VALUE().doubleValue() / 100);
								cell5.setCellStyle(percentStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row25
							// Column G
							cell6 = row.createCell(6);
							if (record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(24);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR26_VALUE() != null) {
								cell5.setCellValue(record.getR26_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row26
							// Column G
							cell6 = row.createCell(6);
							if (record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(25);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR27_VALUE() != null) {
								cell5.setCellValue(record.getR27_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row27
							// Column G
							cell6 = row.createCell(6);
							if (record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

						}


						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_PI EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_PI_ARCHIVALTABLE_SUMMARY");
					}
					return out.toByteArray();
				}
			}

			// Normal Email Excel
			public byte[] BRRS_M_PIEmailExcel(String filename, String reportId, String fromdate, String todate,
					String currency, String dtltype, String type, BigDecimal version) throws Exception {

				logger.info("Service: Starting Excel generation process in memory.");

				// ARCHIVAL check
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
					logger.info("Service: Generating ARCHIVAL report for version {}", version);
					return BRRS_M_PIArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
				}

				List<M_PI_Summary_Entity> dataList = getSummaryAll();

				List<M_PI_Manual_Summary_Entity> dataList1 = getManualSummaryAll();
				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					// --- End of Style Definitions ---

					int startRow = 3;

					if (!dataList.isEmpty() || !dataList1.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_PI_Summary_Entity record = dataList.get(i);
							M_PI_Manual_Summary_Entity record1 = dataList1.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							//REPORT_DATE
							row = sheet.getRow(3);
							Cell cell1 = row.getCell(6);
							if (cell1 == null) {
							    cell1 = row.createCell(1);
							}

							if (record.getREPORT_DATE() != null) {
							    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
							    cell1.setCellStyle(dateStyle);
							} else {
							    cell1.setCellValue("");
							    cell1.setCellStyle(textStyle);
							}
							// row8
							// Column F
							row = sheet.getRow(7);
							Cell cell5 = row.getCell(5);
							if (record.getR8_VALUE() != null) {
								cell5.setCellValue(record.getR8_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);

							}

							// row8
							// Single Cell (example: Column G -> index 6)
							Cell cell6 = row.createCell(6);

							Double value1 = record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double value2 = record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double total = value1 + value2;

							if (value1 != 0.0 || value2 != 0.0) {
							    cell6.setCellValue(total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row9
							row = sheet.getRow(7);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR9_VALUE() != null) {
								cell5.setCellValue(record.getR9_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row9
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r9Value1 = record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r9Value2 = record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r9Total = r9Value1 + r9Value2;

							if (r9Value1 != 0.0 || r9Value2 != 0.0) {
							    cell6.setCellValue(r9Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}
							
							// row10
							row = sheet.getRow(8);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR10_VALUE() != null) {
								cell5.setCellValue(record.getR10_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row10
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r10Value1 = record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r10Value2 = record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r10Total = r10Value1 + r10Value2;

							if (r10Value1 != 0.0 || r10Value2 != 0.0) {
							    cell6.setCellValue(r10Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}
							
							// row11
							row = sheet.getRow(9);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR11_VALUE() != null) {
								cell5.setCellValue(record.getR11_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row11
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r11Value1 = record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r11Value2 = record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r11Total = r11Value1 + r11Value2;

							if (r11Value1 != 0.0 || r11Value2 != 0.0) {
							    cell6.setCellValue(r11Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(10);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR12_VALUE() != null) {
								cell5.setCellValue(record.getR12_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row12
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r12Value1 = record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r12Value2 = record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r12Total = r12Value1 + r12Value2;

							if (r12Value1 != 0.0 || r12Value2 != 0.0) {
							    cell6.setCellValue(r12Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(11);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR13_VALUE() != null) {
								cell5.setCellValue(record.getR13_VALUE().doubleValue());

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row13
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r13Value1 = record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r13Value2 = record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r13Total = r13Value1 + r13Value2;

							if (r13Value1 != 0.0 || r13Value2 != 0.0) {
							    cell6.setCellValue(r13Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(12);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR14_VALUE() != null) {
								cell5.setCellValue(record1.getR14_VALUE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row14
							// Column G (index 6) - Sum of both fields
							cell6 = row.createCell(6);

							Double r14Value1 = record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null
							        ? record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue()
							        : 0.0;

							Double r14Value2 = record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null
							        ? record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue()
							        : 0.0;

							double r14Total = r14Value1 + r14Value2;

							if (r14Value1 != 0.0 || r14Value2 != 0.0) {
							    cell6.setCellValue(r14Total);
							    cell6.setCellStyle(numberStyle);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}
							
							// row15
							row = sheet.getRow(13);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR15_VALUE() != null) {
								cell5.setCellValue(record.getR15_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row15
							// Column G
							cell6 = row.createCell(6);
							if (record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(14);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR16_VALUE() != null) {
								cell5.setCellValue(record.getR16_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row16
							// Column G
							cell6 = row.createCell(6);
							if (record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(15);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR17_VALUE() != null) {
								cell5.setCellValue(record.getR17_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row17
							// Column G
							cell6 = row.createCell(6);
							if (record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(16);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR18_VALUE() != null) {
								cell5.setCellValue(record1.getR18_VALUE().doubleValue() / 100);
								cell5.setCellStyle(percentStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row18
							// Column G
							cell6 = row.createCell(6);
							if (record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(17);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR19_VALUE() != null) {
								cell5.setCellValue(record1.getR19_VALUE().doubleValue() / 100);
								cell5.setCellStyle(percentStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row19
							// Column G
							cell6 = row.createCell(6);
							if (record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(18);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR20_VALUE() != null) {
								cell5.setCellValue(record.getR20_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row20
							// Column G
							cell6 = row.createCell(6);
							if (record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(19);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR21_VALUE() != null) {
								cell5.setCellValue(record.getR21_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row21
							// Column G
							cell6 = row.createCell(6);
							if (record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(20);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR22_VALUE() != null) {
								cell5.setCellValue(record.getR22_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row22
							// Column G
							cell6 = row.createCell(6);
							if (record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(21);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR23_VALUE() != null) {
								cell5.setCellValue(record.getR23_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row23
							// Column G
							cell6 = row.createCell(6);
							if (record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(22);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR24_VALUE() != null) {
								cell5.setCellValue(record.getR24_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row24
							// Column G
							cell6 = row.createCell(6);
							if (record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(23);
							// Column F
							cell5 = row.createCell(5);
							if (record1.getR25_VALUE() != null) {
								cell5.setCellValue(record1.getR25_VALUE().doubleValue() / 100);
								cell5.setCellStyle(percentStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row25
							// Column G
							cell6 = row.createCell(6);
							if (record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(24);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR26_VALUE() != null) {
								cell5.setCellValue(record.getR26_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row26
							// Column G
							cell6 = row.createCell(6);
							if (record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(25);
							// Column F
							cell5 = row.getCell(5);
							if (record.getR27_VALUE() != null) {
								cell5.setCellValue(record.getR27_VALUE().doubleValue() / 100);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row27
							// Column G
							cell6 = row.createCell(6);
							if (record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
								cell6.setCellValue(record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
								cell6.setCellStyle(percentStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

						}

						try {
						    workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
						} catch (RuntimeException e) {
						    logger.warn("Skipping formula evaluation due to external references: {}", e.getMessage());
						}
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_PI EMAIL SUMMARY", null, "BRRS_M_PI_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}

	// ─── Inner entity classes ──────────────────────────────────────────────────

	public static class M_PI_Summary_Entity {
		private Date REPORT_DATE;
		private String REPORT_VERSION;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;
		private String R8_PRODUCT; private String R8_CROSS_REFERENCE; private BigDecimal R8_VALUE; private BigDecimal R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R9_PRODUCT; private String R9_CROSS_REFERENCE; private BigDecimal R9_VALUE; private BigDecimal R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R10_PRODUCT; private String R10_CROSS_REFERENCE; private BigDecimal R10_VALUE; private BigDecimal R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R11_PRODUCT; private String R11_CROSS_REFERENCE; private BigDecimal R11_VALUE; private BigDecimal R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R12_PRODUCT; private String R12_CROSS_REFERENCE; private BigDecimal R12_VALUE; private BigDecimal R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R13_PRODUCT; private String R13_CROSS_REFERENCE; private BigDecimal R13_VALUE; private BigDecimal R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R14_PRODUCT; private String R14_CROSS_REFERENCE; private BigDecimal R14_VALUE; private BigDecimal R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R15_PRODUCT; private String R15_CROSS_REFERENCE; private BigDecimal R15_VALUE; private BigDecimal R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R16_PRODUCT; private String R16_CROSS_REFERENCE; private BigDecimal R16_VALUE; private BigDecimal R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R17_PRODUCT; private String R17_CROSS_REFERENCE; private BigDecimal R17_VALUE; private BigDecimal R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R18_PRODUCT; private String R18_CROSS_REFERENCE; private BigDecimal R18_VALUE; private BigDecimal R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R19_PRODUCT; private String R19_CROSS_REFERENCE; private BigDecimal R19_VALUE; private BigDecimal R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R20_PRODUCT; private String R20_CROSS_REFERENCE; private BigDecimal R20_VALUE; private BigDecimal R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R21_PRODUCT; private String R21_CROSS_REFERENCE; private BigDecimal R21_VALUE; private BigDecimal R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R22_PRODUCT; private String R22_CROSS_REFERENCE; private BigDecimal R22_VALUE; private BigDecimal R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R23_PRODUCT; private String R23_CROSS_REFERENCE; private BigDecimal R23_VALUE; private BigDecimal R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R24_PRODUCT; private String R24_CROSS_REFERENCE; private BigDecimal R24_VALUE; private BigDecimal R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R25_PRODUCT; private String R25_CROSS_REFERENCE; private BigDecimal R25_VALUE; private BigDecimal R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R26_PRODUCT; private String R26_CROSS_REFERENCE; private BigDecimal R26_VALUE; private BigDecimal R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R27_PRODUCT; private String R27_CROSS_REFERENCE; private BigDecimal R27_VALUE; private BigDecimal R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		public Date getREPORT_DATE() { return REPORT_DATE; } public void setREPORT_DATE(Date rEPORT_DATE) { REPORT_DATE = rEPORT_DATE; }
		public String getREPORT_VERSION() { return REPORT_VERSION; } public void setREPORT_VERSION(String rEPORT_VERSION) { REPORT_VERSION = rEPORT_VERSION; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; } public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; } public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; } public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; } public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; } public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; } public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public String getR8_PRODUCT() { return R8_PRODUCT; } public void setR8_PRODUCT(String r8_PRODUCT) { R8_PRODUCT = r8_PRODUCT; }
		public String getR8_CROSS_REFERENCE() { return R8_CROSS_REFERENCE; } public void setR8_CROSS_REFERENCE(String r8_CROSS_REFERENCE) { R8_CROSS_REFERENCE = r8_CROSS_REFERENCE; }
		public BigDecimal getR8_VALUE() { return R8_VALUE; } public void setR8_VALUE(BigDecimal r8_VALUE) { R8_VALUE = r8_VALUE; }
		public BigDecimal getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR9_PRODUCT() { return R9_PRODUCT; } public void setR9_PRODUCT(String r9_PRODUCT) { R9_PRODUCT = r9_PRODUCT; }
		public String getR9_CROSS_REFERENCE() { return R9_CROSS_REFERENCE; } public void setR9_CROSS_REFERENCE(String r9_CROSS_REFERENCE) { R9_CROSS_REFERENCE = r9_CROSS_REFERENCE; }
		public BigDecimal getR9_VALUE() { return R9_VALUE; } public void setR9_VALUE(BigDecimal r9_VALUE) { R9_VALUE = r9_VALUE; }
		public BigDecimal getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR10_PRODUCT() { return R10_PRODUCT; } public void setR10_PRODUCT(String r10_PRODUCT) { R10_PRODUCT = r10_PRODUCT; }
		public String getR10_CROSS_REFERENCE() { return R10_CROSS_REFERENCE; } public void setR10_CROSS_REFERENCE(String r10_CROSS_REFERENCE) { R10_CROSS_REFERENCE = r10_CROSS_REFERENCE; }
		public BigDecimal getR10_VALUE() { return R10_VALUE; } public void setR10_VALUE(BigDecimal r10_VALUE) { R10_VALUE = r10_VALUE; }
		public BigDecimal getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR11_PRODUCT() { return R11_PRODUCT; } public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public String getR11_CROSS_REFERENCE() { return R11_CROSS_REFERENCE; } public void setR11_CROSS_REFERENCE(String r11_CROSS_REFERENCE) { R11_CROSS_REFERENCE = r11_CROSS_REFERENCE; }
		public BigDecimal getR11_VALUE() { return R11_VALUE; } public void setR11_VALUE(BigDecimal r11_VALUE) { R11_VALUE = r11_VALUE; }
		public BigDecimal getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR12_PRODUCT() { return R12_PRODUCT; } public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public String getR12_CROSS_REFERENCE() { return R12_CROSS_REFERENCE; } public void setR12_CROSS_REFERENCE(String r12_CROSS_REFERENCE) { R12_CROSS_REFERENCE = r12_CROSS_REFERENCE; }
		public BigDecimal getR12_VALUE() { return R12_VALUE; } public void setR12_VALUE(BigDecimal r12_VALUE) { R12_VALUE = r12_VALUE; }
		public BigDecimal getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR13_PRODUCT() { return R13_PRODUCT; } public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public String getR13_CROSS_REFERENCE() { return R13_CROSS_REFERENCE; } public void setR13_CROSS_REFERENCE(String r13_CROSS_REFERENCE) { R13_CROSS_REFERENCE = r13_CROSS_REFERENCE; }
		public BigDecimal getR13_VALUE() { return R13_VALUE; } public void setR13_VALUE(BigDecimal r13_VALUE) { R13_VALUE = r13_VALUE; }
		public BigDecimal getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR14_PRODUCT() { return R14_PRODUCT; } public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public String getR14_CROSS_REFERENCE() { return R14_CROSS_REFERENCE; } public void setR14_CROSS_REFERENCE(String r14_CROSS_REFERENCE) { R14_CROSS_REFERENCE = r14_CROSS_REFERENCE; }
		public BigDecimal getR14_VALUE() { return R14_VALUE; } public void setR14_VALUE(BigDecimal r14_VALUE) { R14_VALUE = r14_VALUE; }
		public BigDecimal getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR15_PRODUCT() { return R15_PRODUCT; } public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public String getR15_CROSS_REFERENCE() { return R15_CROSS_REFERENCE; } public void setR15_CROSS_REFERENCE(String r15_CROSS_REFERENCE) { R15_CROSS_REFERENCE = r15_CROSS_REFERENCE; }
		public BigDecimal getR15_VALUE() { return R15_VALUE; } public void setR15_VALUE(BigDecimal r15_VALUE) { R15_VALUE = r15_VALUE; }
		public BigDecimal getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR16_PRODUCT() { return R16_PRODUCT; } public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public String getR16_CROSS_REFERENCE() { return R16_CROSS_REFERENCE; } public void setR16_CROSS_REFERENCE(String r16_CROSS_REFERENCE) { R16_CROSS_REFERENCE = r16_CROSS_REFERENCE; }
		public BigDecimal getR16_VALUE() { return R16_VALUE; } public void setR16_VALUE(BigDecimal r16_VALUE) { R16_VALUE = r16_VALUE; }
		public BigDecimal getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR17_PRODUCT() { return R17_PRODUCT; } public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public String getR17_CROSS_REFERENCE() { return R17_CROSS_REFERENCE; } public void setR17_CROSS_REFERENCE(String r17_CROSS_REFERENCE) { R17_CROSS_REFERENCE = r17_CROSS_REFERENCE; }
		public BigDecimal getR17_VALUE() { return R17_VALUE; } public void setR17_VALUE(BigDecimal r17_VALUE) { R17_VALUE = r17_VALUE; }
		public BigDecimal getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR18_PRODUCT() { return R18_PRODUCT; } public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public String getR18_CROSS_REFERENCE() { return R18_CROSS_REFERENCE; } public void setR18_CROSS_REFERENCE(String r18_CROSS_REFERENCE) { R18_CROSS_REFERENCE = r18_CROSS_REFERENCE; }
		public BigDecimal getR18_VALUE() { return R18_VALUE; } public void setR18_VALUE(BigDecimal r18_VALUE) { R18_VALUE = r18_VALUE; }
		public BigDecimal getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR19_PRODUCT() { return R19_PRODUCT; } public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public String getR19_CROSS_REFERENCE() { return R19_CROSS_REFERENCE; } public void setR19_CROSS_REFERENCE(String r19_CROSS_REFERENCE) { R19_CROSS_REFERENCE = r19_CROSS_REFERENCE; }
		public BigDecimal getR19_VALUE() { return R19_VALUE; } public void setR19_VALUE(BigDecimal r19_VALUE) { R19_VALUE = r19_VALUE; }
		public BigDecimal getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR20_PRODUCT() { return R20_PRODUCT; } public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public String getR20_CROSS_REFERENCE() { return R20_CROSS_REFERENCE; } public void setR20_CROSS_REFERENCE(String r20_CROSS_REFERENCE) { R20_CROSS_REFERENCE = r20_CROSS_REFERENCE; }
		public BigDecimal getR20_VALUE() { return R20_VALUE; } public void setR20_VALUE(BigDecimal r20_VALUE) { R20_VALUE = r20_VALUE; }
		public BigDecimal getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR21_PRODUCT() { return R21_PRODUCT; } public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public String getR21_CROSS_REFERENCE() { return R21_CROSS_REFERENCE; } public void setR21_CROSS_REFERENCE(String r21_CROSS_REFERENCE) { R21_CROSS_REFERENCE = r21_CROSS_REFERENCE; }
		public BigDecimal getR21_VALUE() { return R21_VALUE; } public void setR21_VALUE(BigDecimal r21_VALUE) { R21_VALUE = r21_VALUE; }
		public BigDecimal getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR22_PRODUCT() { return R22_PRODUCT; } public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public String getR22_CROSS_REFERENCE() { return R22_CROSS_REFERENCE; } public void setR22_CROSS_REFERENCE(String r22_CROSS_REFERENCE) { R22_CROSS_REFERENCE = r22_CROSS_REFERENCE; }
		public BigDecimal getR22_VALUE() { return R22_VALUE; } public void setR22_VALUE(BigDecimal r22_VALUE) { R22_VALUE = r22_VALUE; }
		public BigDecimal getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR23_PRODUCT() { return R23_PRODUCT; } public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public String getR23_CROSS_REFERENCE() { return R23_CROSS_REFERENCE; } public void setR23_CROSS_REFERENCE(String r23_CROSS_REFERENCE) { R23_CROSS_REFERENCE = r23_CROSS_REFERENCE; }
		public BigDecimal getR23_VALUE() { return R23_VALUE; } public void setR23_VALUE(BigDecimal r23_VALUE) { R23_VALUE = r23_VALUE; }
		public BigDecimal getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR24_PRODUCT() { return R24_PRODUCT; } public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public String getR24_CROSS_REFERENCE() { return R24_CROSS_REFERENCE; } public void setR24_CROSS_REFERENCE(String r24_CROSS_REFERENCE) { R24_CROSS_REFERENCE = r24_CROSS_REFERENCE; }
		public BigDecimal getR24_VALUE() { return R24_VALUE; } public void setR24_VALUE(BigDecimal r24_VALUE) { R24_VALUE = r24_VALUE; }
		public BigDecimal getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR25_PRODUCT() { return R25_PRODUCT; } public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public String getR25_CROSS_REFERENCE() { return R25_CROSS_REFERENCE; } public void setR25_CROSS_REFERENCE(String r25_CROSS_REFERENCE) { R25_CROSS_REFERENCE = r25_CROSS_REFERENCE; }
		public BigDecimal getR25_VALUE() { return R25_VALUE; } public void setR25_VALUE(BigDecimal r25_VALUE) { R25_VALUE = r25_VALUE; }
		public BigDecimal getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR26_PRODUCT() { return R26_PRODUCT; } public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public String getR26_CROSS_REFERENCE() { return R26_CROSS_REFERENCE; } public void setR26_CROSS_REFERENCE(String r26_CROSS_REFERENCE) { R26_CROSS_REFERENCE = r26_CROSS_REFERENCE; }
		public BigDecimal getR26_VALUE() { return R26_VALUE; } public void setR26_VALUE(BigDecimal r26_VALUE) { R26_VALUE = r26_VALUE; }
		public BigDecimal getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR27_PRODUCT() { return R27_PRODUCT; } public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public String getR27_CROSS_REFERENCE() { return R27_CROSS_REFERENCE; } public void setR27_CROSS_REFERENCE(String r27_CROSS_REFERENCE) { R27_CROSS_REFERENCE = r27_CROSS_REFERENCE; }
		public BigDecimal getR27_VALUE() { return R27_VALUE; } public void setR27_VALUE(BigDecimal r27_VALUE) { R27_VALUE = r27_VALUE; }
		public BigDecimal getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
	}

	public static class M_PI_Manual_Summary_Entity {
		private Date REPORT_DATE;
		private String REPORT_VERSION;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;
		private BigDecimal R14_VALUE;
		private BigDecimal R18_VALUE;
		private BigDecimal R19_VALUE;
		private BigDecimal R25_VALUE;
		public Date getREPORT_DATE() { return REPORT_DATE; } public void setREPORT_DATE(Date rEPORT_DATE) { REPORT_DATE = rEPORT_DATE; }
		public String getREPORT_VERSION() { return REPORT_VERSION; } public void setREPORT_VERSION(String rEPORT_VERSION) { REPORT_VERSION = rEPORT_VERSION; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; } public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; } public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; } public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; } public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; } public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; } public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public BigDecimal getR14_VALUE() { return R14_VALUE; } public void setR14_VALUE(BigDecimal r14_VALUE) { R14_VALUE = r14_VALUE; }
		public BigDecimal getR18_VALUE() { return R18_VALUE; } public void setR18_VALUE(BigDecimal r18_VALUE) { R18_VALUE = r18_VALUE; }
		public BigDecimal getR19_VALUE() { return R19_VALUE; } public void setR19_VALUE(BigDecimal r19_VALUE) { R19_VALUE = r19_VALUE; }
		public BigDecimal getR25_VALUE() { return R25_VALUE; } public void setR25_VALUE(BigDecimal r25_VALUE) { R25_VALUE = r25_VALUE; }
	}

	public static class M_PI_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private String reportLabel;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		public String getCustId() { return custId; } public void setCustId(String custId) { this.custId = custId; }
		public String getAcctNumber() { return acctNumber; } public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }
		public String getAcctName() { return acctName; } public void setAcctName(String acctName) { this.acctName = acctName; }
		public String getDataType() { return dataType; } public void setDataType(String dataType) { this.dataType = dataType; }
		public String getReportAddlCriteria1() { return reportAddlCriteria1; } public void setReportAddlCriteria1(String reportAddlCriteria1) { this.reportAddlCriteria1 = reportAddlCriteria1; }
		public String getReportAddlCriteria2() { return reportAddlCriteria2; } public void setReportAddlCriteria2(String reportAddlCriteria2) { this.reportAddlCriteria2 = reportAddlCriteria2; }
		public String getReportAddlCriteria3() { return reportAddlCriteria3; } public void setReportAddlCriteria3(String reportAddlCriteria3) { this.reportAddlCriteria3 = reportAddlCriteria3; }
		public String getReportLabel() { return reportLabel; } public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }
		public String getReportRemarks() { return reportRemarks; } public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }
		public String getModificationRemarks() { return modificationRemarks; } public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }
		public String getDataEntryVersion() { return dataEntryVersion; } public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; } public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }
		public Date getReportDate() { return reportDate; } public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public String getReportName() { return reportName; } public void setReportName(String reportName) { this.reportName = reportName; }
		public String getCreateUser() { return createUser; } public void setCreateUser(String createUser) { this.createUser = createUser; }
		public Date getCreateTime() { return createTime; } public void setCreateTime(Date createTime) { this.createTime = createTime; }
		public String getModifyUser() { return modifyUser; } public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
		public Date getModifyTime() { return modifyTime; } public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
		public String getVerifyUser() { return verifyUser; } public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }
		public Date getVerifyTime() { return verifyTime; } public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
		public String getEntityFlg() { return entityFlg; } public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; } public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; } public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
	}

	public static class M_PI_Archival_Summary_Entity {
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;
		private String R8_PRODUCT; private String R8_CROSS_REFERENCE; private BigDecimal R8_VALUE; private BigDecimal R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R9_PRODUCT; private String R9_CROSS_REFERENCE; private BigDecimal R9_VALUE; private BigDecimal R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R10_PRODUCT; private String R10_CROSS_REFERENCE; private BigDecimal R10_VALUE; private BigDecimal R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R11_PRODUCT; private String R11_CROSS_REFERENCE; private BigDecimal R11_VALUE; private BigDecimal R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R12_PRODUCT; private String R12_CROSS_REFERENCE; private BigDecimal R12_VALUE; private BigDecimal R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R13_PRODUCT; private String R13_CROSS_REFERENCE; private BigDecimal R13_VALUE; private BigDecimal R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R14_PRODUCT; private String R14_CROSS_REFERENCE; private BigDecimal R14_VALUE; private BigDecimal R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; private BigDecimal R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2;
		private String R15_PRODUCT; private String R15_CROSS_REFERENCE; private BigDecimal R15_VALUE; private BigDecimal R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R16_PRODUCT; private String R16_CROSS_REFERENCE; private BigDecimal R16_VALUE; private BigDecimal R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R17_PRODUCT; private String R17_CROSS_REFERENCE; private BigDecimal R17_VALUE; private BigDecimal R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R18_PRODUCT; private String R18_CROSS_REFERENCE; private BigDecimal R18_VALUE; private BigDecimal R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R19_PRODUCT; private String R19_CROSS_REFERENCE; private BigDecimal R19_VALUE; private BigDecimal R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R20_PRODUCT; private String R20_CROSS_REFERENCE; private BigDecimal R20_VALUE; private BigDecimal R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R21_PRODUCT; private String R21_CROSS_REFERENCE; private BigDecimal R21_VALUE; private BigDecimal R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R22_PRODUCT; private String R22_CROSS_REFERENCE; private BigDecimal R22_VALUE; private BigDecimal R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R23_PRODUCT; private String R23_CROSS_REFERENCE; private BigDecimal R23_VALUE; private BigDecimal R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R24_PRODUCT; private String R24_CROSS_REFERENCE; private BigDecimal R24_VALUE; private BigDecimal R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R25_PRODUCT; private String R25_CROSS_REFERENCE; private BigDecimal R25_VALUE; private BigDecimal R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R26_PRODUCT; private String R26_CROSS_REFERENCE; private BigDecimal R26_VALUE; private BigDecimal R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		private String R27_PRODUCT; private String R27_CROSS_REFERENCE; private BigDecimal R27_VALUE; private BigDecimal R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS;
		public Date getREPORT_DATE() { return REPORT_DATE; } public void setREPORT_DATE(Date rEPORT_DATE) { REPORT_DATE = rEPORT_DATE; }
		public BigDecimal getREPORT_VERSION() { return REPORT_VERSION; } public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) { REPORT_VERSION = rEPORT_VERSION; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; } public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; } public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; } public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; } public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; } public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; } public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public String getR8_PRODUCT() { return R8_PRODUCT; } public void setR8_PRODUCT(String r8_PRODUCT) { R8_PRODUCT = r8_PRODUCT; }
		public String getR8_CROSS_REFERENCE() { return R8_CROSS_REFERENCE; } public void setR8_CROSS_REFERENCE(String r8_CROSS_REFERENCE) { R8_CROSS_REFERENCE = r8_CROSS_REFERENCE; }
		public BigDecimal getR8_VALUE() { return R8_VALUE; } public void setR8_VALUE(BigDecimal r8_VALUE) { R8_VALUE = r8_VALUE; }
		public BigDecimal getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR9_PRODUCT() { return R9_PRODUCT; } public void setR9_PRODUCT(String r9_PRODUCT) { R9_PRODUCT = r9_PRODUCT; }
		public String getR9_CROSS_REFERENCE() { return R9_CROSS_REFERENCE; } public void setR9_CROSS_REFERENCE(String r9_CROSS_REFERENCE) { R9_CROSS_REFERENCE = r9_CROSS_REFERENCE; }
		public BigDecimal getR9_VALUE() { return R9_VALUE; } public void setR9_VALUE(BigDecimal r9_VALUE) { R9_VALUE = r9_VALUE; }
		public BigDecimal getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR10_PRODUCT() { return R10_PRODUCT; } public void setR10_PRODUCT(String r10_PRODUCT) { R10_PRODUCT = r10_PRODUCT; }
		public String getR10_CROSS_REFERENCE() { return R10_CROSS_REFERENCE; } public void setR10_CROSS_REFERENCE(String r10_CROSS_REFERENCE) { R10_CROSS_REFERENCE = r10_CROSS_REFERENCE; }
		public BigDecimal getR10_VALUE() { return R10_VALUE; } public void setR10_VALUE(BigDecimal r10_VALUE) { R10_VALUE = r10_VALUE; }
		public BigDecimal getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR11_PRODUCT() { return R11_PRODUCT; } public void setR11_PRODUCT(String r11_PRODUCT) { R11_PRODUCT = r11_PRODUCT; }
		public String getR11_CROSS_REFERENCE() { return R11_CROSS_REFERENCE; } public void setR11_CROSS_REFERENCE(String r11_CROSS_REFERENCE) { R11_CROSS_REFERENCE = r11_CROSS_REFERENCE; }
		public BigDecimal getR11_VALUE() { return R11_VALUE; } public void setR11_VALUE(BigDecimal r11_VALUE) { R11_VALUE = r11_VALUE; }
		public BigDecimal getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR12_PRODUCT() { return R12_PRODUCT; } public void setR12_PRODUCT(String r12_PRODUCT) { R12_PRODUCT = r12_PRODUCT; }
		public String getR12_CROSS_REFERENCE() { return R12_CROSS_REFERENCE; } public void setR12_CROSS_REFERENCE(String r12_CROSS_REFERENCE) { R12_CROSS_REFERENCE = r12_CROSS_REFERENCE; }
		public BigDecimal getR12_VALUE() { return R12_VALUE; } public void setR12_VALUE(BigDecimal r12_VALUE) { R12_VALUE = r12_VALUE; }
		public BigDecimal getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR13_PRODUCT() { return R13_PRODUCT; } public void setR13_PRODUCT(String r13_PRODUCT) { R13_PRODUCT = r13_PRODUCT; }
		public String getR13_CROSS_REFERENCE() { return R13_CROSS_REFERENCE; } public void setR13_CROSS_REFERENCE(String r13_CROSS_REFERENCE) { R13_CROSS_REFERENCE = r13_CROSS_REFERENCE; }
		public BigDecimal getR13_VALUE() { return R13_VALUE; } public void setR13_VALUE(BigDecimal r13_VALUE) { R13_VALUE = r13_VALUE; }
		public BigDecimal getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR14_PRODUCT() { return R14_PRODUCT; } public void setR14_PRODUCT(String r14_PRODUCT) { R14_PRODUCT = r14_PRODUCT; }
		public String getR14_CROSS_REFERENCE() { return R14_CROSS_REFERENCE; } public void setR14_CROSS_REFERENCE(String r14_CROSS_REFERENCE) { R14_CROSS_REFERENCE = r14_CROSS_REFERENCE; }
		public BigDecimal getR14_VALUE() { return R14_VALUE; } public void setR14_VALUE(BigDecimal r14_VALUE) { R14_VALUE = r14_VALUE; }
		public BigDecimal getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public BigDecimal getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() { return R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; } public void setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(BigDecimal r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2) { R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2 = r14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2; }
		public String getR15_PRODUCT() { return R15_PRODUCT; } public void setR15_PRODUCT(String r15_PRODUCT) { R15_PRODUCT = r15_PRODUCT; }
		public String getR15_CROSS_REFERENCE() { return R15_CROSS_REFERENCE; } public void setR15_CROSS_REFERENCE(String r15_CROSS_REFERENCE) { R15_CROSS_REFERENCE = r15_CROSS_REFERENCE; }
		public BigDecimal getR15_VALUE() { return R15_VALUE; } public void setR15_VALUE(BigDecimal r15_VALUE) { R15_VALUE = r15_VALUE; }
		public BigDecimal getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR16_PRODUCT() { return R16_PRODUCT; } public void setR16_PRODUCT(String r16_PRODUCT) { R16_PRODUCT = r16_PRODUCT; }
		public String getR16_CROSS_REFERENCE() { return R16_CROSS_REFERENCE; } public void setR16_CROSS_REFERENCE(String r16_CROSS_REFERENCE) { R16_CROSS_REFERENCE = r16_CROSS_REFERENCE; }
		public BigDecimal getR16_VALUE() { return R16_VALUE; } public void setR16_VALUE(BigDecimal r16_VALUE) { R16_VALUE = r16_VALUE; }
		public BigDecimal getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR17_PRODUCT() { return R17_PRODUCT; } public void setR17_PRODUCT(String r17_PRODUCT) { R17_PRODUCT = r17_PRODUCT; }
		public String getR17_CROSS_REFERENCE() { return R17_CROSS_REFERENCE; } public void setR17_CROSS_REFERENCE(String r17_CROSS_REFERENCE) { R17_CROSS_REFERENCE = r17_CROSS_REFERENCE; }
		public BigDecimal getR17_VALUE() { return R17_VALUE; } public void setR17_VALUE(BigDecimal r17_VALUE) { R17_VALUE = r17_VALUE; }
		public BigDecimal getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR18_PRODUCT() { return R18_PRODUCT; } public void setR18_PRODUCT(String r18_PRODUCT) { R18_PRODUCT = r18_PRODUCT; }
		public String getR18_CROSS_REFERENCE() { return R18_CROSS_REFERENCE; } public void setR18_CROSS_REFERENCE(String r18_CROSS_REFERENCE) { R18_CROSS_REFERENCE = r18_CROSS_REFERENCE; }
		public BigDecimal getR18_VALUE() { return R18_VALUE; } public void setR18_VALUE(BigDecimal r18_VALUE) { R18_VALUE = r18_VALUE; }
		public BigDecimal getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR19_PRODUCT() { return R19_PRODUCT; } public void setR19_PRODUCT(String r19_PRODUCT) { R19_PRODUCT = r19_PRODUCT; }
		public String getR19_CROSS_REFERENCE() { return R19_CROSS_REFERENCE; } public void setR19_CROSS_REFERENCE(String r19_CROSS_REFERENCE) { R19_CROSS_REFERENCE = r19_CROSS_REFERENCE; }
		public BigDecimal getR19_VALUE() { return R19_VALUE; } public void setR19_VALUE(BigDecimal r19_VALUE) { R19_VALUE = r19_VALUE; }
		public BigDecimal getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR20_PRODUCT() { return R20_PRODUCT; } public void setR20_PRODUCT(String r20_PRODUCT) { R20_PRODUCT = r20_PRODUCT; }
		public String getR20_CROSS_REFERENCE() { return R20_CROSS_REFERENCE; } public void setR20_CROSS_REFERENCE(String r20_CROSS_REFERENCE) { R20_CROSS_REFERENCE = r20_CROSS_REFERENCE; }
		public BigDecimal getR20_VALUE() { return R20_VALUE; } public void setR20_VALUE(BigDecimal r20_VALUE) { R20_VALUE = r20_VALUE; }
		public BigDecimal getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR21_PRODUCT() { return R21_PRODUCT; } public void setR21_PRODUCT(String r21_PRODUCT) { R21_PRODUCT = r21_PRODUCT; }
		public String getR21_CROSS_REFERENCE() { return R21_CROSS_REFERENCE; } public void setR21_CROSS_REFERENCE(String r21_CROSS_REFERENCE) { R21_CROSS_REFERENCE = r21_CROSS_REFERENCE; }
		public BigDecimal getR21_VALUE() { return R21_VALUE; } public void setR21_VALUE(BigDecimal r21_VALUE) { R21_VALUE = r21_VALUE; }
		public BigDecimal getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR22_PRODUCT() { return R22_PRODUCT; } public void setR22_PRODUCT(String r22_PRODUCT) { R22_PRODUCT = r22_PRODUCT; }
		public String getR22_CROSS_REFERENCE() { return R22_CROSS_REFERENCE; } public void setR22_CROSS_REFERENCE(String r22_CROSS_REFERENCE) { R22_CROSS_REFERENCE = r22_CROSS_REFERENCE; }
		public BigDecimal getR22_VALUE() { return R22_VALUE; } public void setR22_VALUE(BigDecimal r22_VALUE) { R22_VALUE = r22_VALUE; }
		public BigDecimal getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR23_PRODUCT() { return R23_PRODUCT; } public void setR23_PRODUCT(String r23_PRODUCT) { R23_PRODUCT = r23_PRODUCT; }
		public String getR23_CROSS_REFERENCE() { return R23_CROSS_REFERENCE; } public void setR23_CROSS_REFERENCE(String r23_CROSS_REFERENCE) { R23_CROSS_REFERENCE = r23_CROSS_REFERENCE; }
		public BigDecimal getR23_VALUE() { return R23_VALUE; } public void setR23_VALUE(BigDecimal r23_VALUE) { R23_VALUE = r23_VALUE; }
		public BigDecimal getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR24_PRODUCT() { return R24_PRODUCT; } public void setR24_PRODUCT(String r24_PRODUCT) { R24_PRODUCT = r24_PRODUCT; }
		public String getR24_CROSS_REFERENCE() { return R24_CROSS_REFERENCE; } public void setR24_CROSS_REFERENCE(String r24_CROSS_REFERENCE) { R24_CROSS_REFERENCE = r24_CROSS_REFERENCE; }
		public BigDecimal getR24_VALUE() { return R24_VALUE; } public void setR24_VALUE(BigDecimal r24_VALUE) { R24_VALUE = r24_VALUE; }
		public BigDecimal getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR25_PRODUCT() { return R25_PRODUCT; } public void setR25_PRODUCT(String r25_PRODUCT) { R25_PRODUCT = r25_PRODUCT; }
		public String getR25_CROSS_REFERENCE() { return R25_CROSS_REFERENCE; } public void setR25_CROSS_REFERENCE(String r25_CROSS_REFERENCE) { R25_CROSS_REFERENCE = r25_CROSS_REFERENCE; }
		public BigDecimal getR25_VALUE() { return R25_VALUE; } public void setR25_VALUE(BigDecimal r25_VALUE) { R25_VALUE = r25_VALUE; }
		public BigDecimal getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR26_PRODUCT() { return R26_PRODUCT; } public void setR26_PRODUCT(String r26_PRODUCT) { R26_PRODUCT = r26_PRODUCT; }
		public String getR26_CROSS_REFERENCE() { return R26_CROSS_REFERENCE; } public void setR26_CROSS_REFERENCE(String r26_CROSS_REFERENCE) { R26_CROSS_REFERENCE = r26_CROSS_REFERENCE; }
		public BigDecimal getR26_VALUE() { return R26_VALUE; } public void setR26_VALUE(BigDecimal r26_VALUE) { R26_VALUE = r26_VALUE; }
		public BigDecimal getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
		public String getR27_PRODUCT() { return R27_PRODUCT; } public void setR27_PRODUCT(String r27_PRODUCT) { R27_PRODUCT = r27_PRODUCT; }
		public String getR27_CROSS_REFERENCE() { return R27_CROSS_REFERENCE; } public void setR27_CROSS_REFERENCE(String r27_CROSS_REFERENCE) { R27_CROSS_REFERENCE = r27_CROSS_REFERENCE; }
		public BigDecimal getR27_VALUE() { return R27_VALUE; } public void setR27_VALUE(BigDecimal r27_VALUE) { R27_VALUE = r27_VALUE; }
		public BigDecimal getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() { return R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; } public void setR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(BigDecimal r27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS) { R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS = r27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS; }
	}

	public static class M_PI_Manual_Archival_Summary_Entity {
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;
		private BigDecimal R14_VALUE;
		private BigDecimal R18_VALUE;
		private BigDecimal R19_VALUE;
		private BigDecimal R25_VALUE;
		public Date getREPORT_DATE() { return REPORT_DATE; } public void setREPORT_DATE(Date rEPORT_DATE) { REPORT_DATE = rEPORT_DATE; }
		public BigDecimal getREPORT_VERSION() { return REPORT_VERSION; } public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) { REPORT_VERSION = rEPORT_VERSION; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; } public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) { REPORT_FREQUENCY = rEPORT_FREQUENCY; }
		public String getREPORT_CODE() { return REPORT_CODE; } public void setREPORT_CODE(String rEPORT_CODE) { REPORT_CODE = rEPORT_CODE; }
		public String getREPORT_DESC() { return REPORT_DESC; } public void setREPORT_DESC(String rEPORT_DESC) { REPORT_DESC = rEPORT_DESC; }
		public String getENTITY_FLG() { return ENTITY_FLG; } public void setENTITY_FLG(String eNTITY_FLG) { ENTITY_FLG = eNTITY_FLG; }
		public String getMODIFY_FLG() { return MODIFY_FLG; } public void setMODIFY_FLG(String mODIFY_FLG) { MODIFY_FLG = mODIFY_FLG; }
		public String getDEL_FLG() { return DEL_FLG; } public void setDEL_FLG(String dEL_FLG) { DEL_FLG = dEL_FLG; }
		public BigDecimal getR14_VALUE() { return R14_VALUE; } public void setR14_VALUE(BigDecimal r14_VALUE) { R14_VALUE = r14_VALUE; }
		public BigDecimal getR18_VALUE() { return R18_VALUE; } public void setR18_VALUE(BigDecimal r18_VALUE) { R18_VALUE = r18_VALUE; }
		public BigDecimal getR19_VALUE() { return R19_VALUE; } public void setR19_VALUE(BigDecimal r19_VALUE) { R19_VALUE = r19_VALUE; }
		public BigDecimal getR25_VALUE() { return R25_VALUE; } public void setR25_VALUE(BigDecimal r25_VALUE) { R25_VALUE = r25_VALUE; }
	}

	public static class M_PI_Archival_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private String reportLabel;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		public String getCustId() { return custId; } public void setCustId(String custId) { this.custId = custId; }
		public String getAcctNumber() { return acctNumber; } public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }
		public String getAcctName() { return acctName; } public void setAcctName(String acctName) { this.acctName = acctName; }
		public String getDataType() { return dataType; } public void setDataType(String dataType) { this.dataType = dataType; }
		public String getReportAddlCriteria1() { return reportAddlCriteria1; } public void setReportAddlCriteria1(String reportAddlCriteria1) { this.reportAddlCriteria1 = reportAddlCriteria1; }
		public String getReportAddlCriteria2() { return reportAddlCriteria2; } public void setReportAddlCriteria2(String reportAddlCriteria2) { this.reportAddlCriteria2 = reportAddlCriteria2; }
		public String getReportAddlCriteria3() { return reportAddlCriteria3; } public void setReportAddlCriteria3(String reportAddlCriteria3) { this.reportAddlCriteria3 = reportAddlCriteria3; }
		public String getReportLabel() { return reportLabel; } public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }
		public String getReportRemarks() { return reportRemarks; } public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }
		public String getModificationRemarks() { return modificationRemarks; } public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }
		public String getDataEntryVersion() { return dataEntryVersion; } public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; } public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }
		public Date getReportDate() { return reportDate; } public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public String getReportName() { return reportName; } public void setReportName(String reportName) { this.reportName = reportName; }
		public String getCreateUser() { return createUser; } public void setCreateUser(String createUser) { this.createUser = createUser; }
		public Date getCreateTime() { return createTime; } public void setCreateTime(Date createTime) { this.createTime = createTime; }
		public String getModifyUser() { return modifyUser; } public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
		public Date getModifyTime() { return modifyTime; } public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
		public String getVerifyUser() { return verifyUser; } public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }
		public Date getVerifyTime() { return verifyTime; } public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
		public String getEntityFlg() { return entityFlg; } public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; } public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; } public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
	}

	// ─── RowMapper inner classes ───────────────────────────────────────────────

	class M_PISummaryRowMapper implements RowMapper<M_PI_Summary_Entity> {
		@Override
		public M_PI_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PI_Summary_Entity o = new M_PI_Summary_Entity();
			o.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			o.setREPORT_VERSION(rs.getString("REPORT_VERSION"));
			o.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			o.setREPORT_CODE(rs.getString("REPORT_CODE"));
			o.setREPORT_DESC(rs.getString("REPORT_DESC"));
			o.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			o.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			o.setDEL_FLG(rs.getString("DEL_FLG"));
			o.setR8_PRODUCT(rs.getString("R8_PRODUCT")); o.setR8_CROSS_REFERENCE(rs.getString("R8_CROSS_REFERENCE")); o.setR8_VALUE(rs.getBigDecimal("R8_VALUE")); o.setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR9_PRODUCT(rs.getString("R9_PRODUCT")); o.setR9_CROSS_REFERENCE(rs.getString("R9_CROSS_REFERENCE")); o.setR9_VALUE(rs.getBigDecimal("R9_VALUE")); o.setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR10_PRODUCT(rs.getString("R10_PRODUCT")); o.setR10_CROSS_REFERENCE(rs.getString("R10_CROSS_REFERENCE")); o.setR10_VALUE(rs.getBigDecimal("R10_VALUE")); o.setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR11_PRODUCT(rs.getString("R11_PRODUCT")); o.setR11_CROSS_REFERENCE(rs.getString("R11_CROSS_REFERENCE")); o.setR11_VALUE(rs.getBigDecimal("R11_VALUE")); o.setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR12_PRODUCT(rs.getString("R12_PRODUCT")); o.setR12_CROSS_REFERENCE(rs.getString("R12_CROSS_REFERENCE")); o.setR12_VALUE(rs.getBigDecimal("R12_VALUE")); o.setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR13_PRODUCT(rs.getString("R13_PRODUCT")); o.setR13_CROSS_REFERENCE(rs.getString("R13_CROSS_REFERENCE")); o.setR13_VALUE(rs.getBigDecimal("R13_VALUE")); o.setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR14_PRODUCT(rs.getString("R14_PRODUCT")); o.setR14_CROSS_REFERENCE(rs.getString("R14_CROSS_REFERENCE")); o.setR14_VALUE(rs.getBigDecimal("R14_VALUE")); o.setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR15_PRODUCT(rs.getString("R15_PRODUCT")); o.setR15_CROSS_REFERENCE(rs.getString("R15_CROSS_REFERENCE")); o.setR15_VALUE(rs.getBigDecimal("R15_VALUE")); o.setR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR16_PRODUCT(rs.getString("R16_PRODUCT")); o.setR16_CROSS_REFERENCE(rs.getString("R16_CROSS_REFERENCE")); o.setR16_VALUE(rs.getBigDecimal("R16_VALUE")); o.setR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR17_PRODUCT(rs.getString("R17_PRODUCT")); o.setR17_CROSS_REFERENCE(rs.getString("R17_CROSS_REFERENCE")); o.setR17_VALUE(rs.getBigDecimal("R17_VALUE")); o.setR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR18_PRODUCT(rs.getString("R18_PRODUCT")); o.setR18_CROSS_REFERENCE(rs.getString("R18_CROSS_REFERENCE")); o.setR18_VALUE(rs.getBigDecimal("R18_VALUE")); o.setR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR19_PRODUCT(rs.getString("R19_PRODUCT")); o.setR19_CROSS_REFERENCE(rs.getString("R19_CROSS_REFERENCE")); o.setR19_VALUE(rs.getBigDecimal("R19_VALUE")); o.setR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR20_PRODUCT(rs.getString("R20_PRODUCT")); o.setR20_CROSS_REFERENCE(rs.getString("R20_CROSS_REFERENCE")); o.setR20_VALUE(rs.getBigDecimal("R20_VALUE")); o.setR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR21_PRODUCT(rs.getString("R21_PRODUCT")); o.setR21_CROSS_REFERENCE(rs.getString("R21_CROSS_REFERENCE")); o.setR21_VALUE(rs.getBigDecimal("R21_VALUE")); o.setR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR22_PRODUCT(rs.getString("R22_PRODUCT")); o.setR22_CROSS_REFERENCE(rs.getString("R22_CROSS_REFERENCE")); o.setR22_VALUE(rs.getBigDecimal("R22_VALUE")); o.setR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR23_PRODUCT(rs.getString("R23_PRODUCT")); o.setR23_CROSS_REFERENCE(rs.getString("R23_CROSS_REFERENCE")); o.setR23_VALUE(rs.getBigDecimal("R23_VALUE")); o.setR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR24_PRODUCT(rs.getString("R24_PRODUCT")); o.setR24_CROSS_REFERENCE(rs.getString("R24_CROSS_REFERENCE")); o.setR24_VALUE(rs.getBigDecimal("R24_VALUE")); o.setR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR25_PRODUCT(rs.getString("R25_PRODUCT")); o.setR25_CROSS_REFERENCE(rs.getString("R25_CROSS_REFERENCE")); o.setR25_VALUE(rs.getBigDecimal("R25_VALUE")); o.setR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR26_PRODUCT(rs.getString("R26_PRODUCT")); o.setR26_CROSS_REFERENCE(rs.getString("R26_CROSS_REFERENCE")); o.setR26_VALUE(rs.getBigDecimal("R26_VALUE")); o.setR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR27_PRODUCT(rs.getString("R27_PRODUCT")); o.setR27_CROSS_REFERENCE(rs.getString("R27_CROSS_REFERENCE")); o.setR27_VALUE(rs.getBigDecimal("R27_VALUE")); o.setR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			return o;
		}
	}

	class M_PIManualSummaryRowMapper implements RowMapper<M_PI_Manual_Summary_Entity> {
		@Override
		public M_PI_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PI_Manual_Summary_Entity o = new M_PI_Manual_Summary_Entity();
			o.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			o.setREPORT_VERSION(rs.getString("REPORT_VERSION"));
			o.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			o.setREPORT_CODE(rs.getString("REPORT_CODE"));
			o.setREPORT_DESC(rs.getString("REPORT_DESC"));
			o.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			o.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			o.setDEL_FLG(rs.getString("DEL_FLG"));
			o.setR14_VALUE(rs.getBigDecimal("R14_VALUE"));
			o.setR18_VALUE(rs.getBigDecimal("R18_VALUE"));
			o.setR19_VALUE(rs.getBigDecimal("R19_VALUE"));
			o.setR25_VALUE(rs.getBigDecimal("R25_VALUE"));
			return o;
		}
	}

	class M_PIDetailRowMapper implements RowMapper<M_PI_Detail_Entity> {
		@Override
		public M_PI_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PI_Detail_Entity o = new M_PI_Detail_Entity();
			o.setCustId(rs.getString("CUST_ID"));
			o.setAcctNumber(rs.getString("ACCT_NUMBER"));
			o.setAcctName(rs.getString("ACCT_NAME"));
			o.setDataType(rs.getString("DATA_TYPE"));
			o.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			o.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			o.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			o.setReportLabel(rs.getString("REPORT_LABEL"));
			o.setReportRemarks(rs.getString("REPORT_REMARKS"));
			o.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			o.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			o.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			o.setReportDate(rs.getDate("REPORT_DATE"));
			o.setReportName(rs.getString("REPORT_NAME"));
			o.setCreateUser(rs.getString("CREATE_USER"));
			o.setCreateTime(rs.getDate("CREATE_TIME"));
			o.setModifyUser(rs.getString("MODIFY_USER"));
			o.setModifyTime(rs.getDate("MODIFY_TIME"));
			o.setVerifyUser(rs.getString("VERIFY_USER"));
			o.setVerifyTime(rs.getDate("VERIFY_TIME"));
			o.setEntityFlg(rs.getString("ENTITY_FLG"));
			o.setModifyFlg(rs.getString("MODIFY_FLG"));
			o.setDelFlg(rs.getString("DEL_FLG"));
			return o;
		}
	}

	class M_PIArchivalSummaryRowMapper implements RowMapper<M_PI_Archival_Summary_Entity> {
		@Override
		public M_PI_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PI_Archival_Summary_Entity o = new M_PI_Archival_Summary_Entity();
			o.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			o.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			o.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			o.setREPORT_CODE(rs.getString("REPORT_CODE"));
			o.setREPORT_DESC(rs.getString("REPORT_DESC"));
			o.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			o.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			o.setDEL_FLG(rs.getString("DEL_FLG"));
			o.setR8_PRODUCT(rs.getString("R8_PRODUCT")); o.setR8_CROSS_REFERENCE(rs.getString("R8_CROSS_REFERENCE")); o.setR8_VALUE(rs.getBigDecimal("R8_VALUE")); o.setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR9_PRODUCT(rs.getString("R9_PRODUCT")); o.setR9_CROSS_REFERENCE(rs.getString("R9_CROSS_REFERENCE")); o.setR9_VALUE(rs.getBigDecimal("R9_VALUE")); o.setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR10_PRODUCT(rs.getString("R10_PRODUCT")); o.setR10_CROSS_REFERENCE(rs.getString("R10_CROSS_REFERENCE")); o.setR10_VALUE(rs.getBigDecimal("R10_VALUE")); o.setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR11_PRODUCT(rs.getString("R11_PRODUCT")); o.setR11_CROSS_REFERENCE(rs.getString("R11_CROSS_REFERENCE")); o.setR11_VALUE(rs.getBigDecimal("R11_VALUE")); o.setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR12_PRODUCT(rs.getString("R12_PRODUCT")); o.setR12_CROSS_REFERENCE(rs.getString("R12_CROSS_REFERENCE")); o.setR12_VALUE(rs.getBigDecimal("R12_VALUE")); o.setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR13_PRODUCT(rs.getString("R13_PRODUCT")); o.setR13_CROSS_REFERENCE(rs.getString("R13_CROSS_REFERENCE")); o.setR13_VALUE(rs.getBigDecimal("R13_VALUE")); o.setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR14_PRODUCT(rs.getString("R14_PRODUCT")); o.setR14_CROSS_REFERENCE(rs.getString("R14_CROSS_REFERENCE")); o.setR14_VALUE(rs.getBigDecimal("R14_VALUE")); o.setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS")); o.setR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2(rs.getBigDecimal("R14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2"));
			o.setR15_PRODUCT(rs.getString("R15_PRODUCT")); o.setR15_CROSS_REFERENCE(rs.getString("R15_CROSS_REFERENCE")); o.setR15_VALUE(rs.getBigDecimal("R15_VALUE")); o.setR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR16_PRODUCT(rs.getString("R16_PRODUCT")); o.setR16_CROSS_REFERENCE(rs.getString("R16_CROSS_REFERENCE")); o.setR16_VALUE(rs.getBigDecimal("R16_VALUE")); o.setR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR17_PRODUCT(rs.getString("R17_PRODUCT")); o.setR17_CROSS_REFERENCE(rs.getString("R17_CROSS_REFERENCE")); o.setR17_VALUE(rs.getBigDecimal("R17_VALUE")); o.setR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR18_PRODUCT(rs.getString("R18_PRODUCT")); o.setR18_CROSS_REFERENCE(rs.getString("R18_CROSS_REFERENCE")); o.setR18_VALUE(rs.getBigDecimal("R18_VALUE")); o.setR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR19_PRODUCT(rs.getString("R19_PRODUCT")); o.setR19_CROSS_REFERENCE(rs.getString("R19_CROSS_REFERENCE")); o.setR19_VALUE(rs.getBigDecimal("R19_VALUE")); o.setR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR20_PRODUCT(rs.getString("R20_PRODUCT")); o.setR20_CROSS_REFERENCE(rs.getString("R20_CROSS_REFERENCE")); o.setR20_VALUE(rs.getBigDecimal("R20_VALUE")); o.setR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR21_PRODUCT(rs.getString("R21_PRODUCT")); o.setR21_CROSS_REFERENCE(rs.getString("R21_CROSS_REFERENCE")); o.setR21_VALUE(rs.getBigDecimal("R21_VALUE")); o.setR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR22_PRODUCT(rs.getString("R22_PRODUCT")); o.setR22_CROSS_REFERENCE(rs.getString("R22_CROSS_REFERENCE")); o.setR22_VALUE(rs.getBigDecimal("R22_VALUE")); o.setR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR23_PRODUCT(rs.getString("R23_PRODUCT")); o.setR23_CROSS_REFERENCE(rs.getString("R23_CROSS_REFERENCE")); o.setR23_VALUE(rs.getBigDecimal("R23_VALUE")); o.setR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR24_PRODUCT(rs.getString("R24_PRODUCT")); o.setR24_CROSS_REFERENCE(rs.getString("R24_CROSS_REFERENCE")); o.setR24_VALUE(rs.getBigDecimal("R24_VALUE")); o.setR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR25_PRODUCT(rs.getString("R25_PRODUCT")); o.setR25_CROSS_REFERENCE(rs.getString("R25_CROSS_REFERENCE")); o.setR25_VALUE(rs.getBigDecimal("R25_VALUE")); o.setR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR26_PRODUCT(rs.getString("R26_PRODUCT")); o.setR26_CROSS_REFERENCE(rs.getString("R26_CROSS_REFERENCE")); o.setR26_VALUE(rs.getBigDecimal("R26_VALUE")); o.setR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			o.setR27_PRODUCT(rs.getString("R27_PRODUCT")); o.setR27_CROSS_REFERENCE(rs.getString("R27_CROSS_REFERENCE")); o.setR27_VALUE(rs.getBigDecimal("R27_VALUE")); o.setR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS(rs.getBigDecimal("R27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS"));
			return o;
		}
	}

	class M_PIManualArchivalSummaryRowMapper implements RowMapper<M_PI_Manual_Archival_Summary_Entity> {
		@Override
		public M_PI_Manual_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PI_Manual_Archival_Summary_Entity o = new M_PI_Manual_Archival_Summary_Entity();
			o.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			o.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			o.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			o.setREPORT_CODE(rs.getString("REPORT_CODE"));
			o.setREPORT_DESC(rs.getString("REPORT_DESC"));
			o.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			o.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			o.setDEL_FLG(rs.getString("DEL_FLG"));
			o.setR14_VALUE(rs.getBigDecimal("R14_VALUE"));
			o.setR18_VALUE(rs.getBigDecimal("R18_VALUE"));
			o.setR19_VALUE(rs.getBigDecimal("R19_VALUE"));
			o.setR25_VALUE(rs.getBigDecimal("R25_VALUE"));
			return o;
		}
	}

	class M_PIArchivalDetailRowMapper implements RowMapper<M_PI_Archival_Detail_Entity> {
		@Override
		public M_PI_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_PI_Archival_Detail_Entity o = new M_PI_Archival_Detail_Entity();
			o.setCustId(rs.getString("CUST_ID"));
			o.setAcctNumber(rs.getString("ACCT_NUMBER"));
			o.setAcctName(rs.getString("ACCT_NAME"));
			o.setDataType(rs.getString("DATA_TYPE"));
			o.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			o.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			o.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			o.setReportLabel(rs.getString("REPORT_LABEL"));
			o.setReportRemarks(rs.getString("REPORT_REMARKS"));
			o.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			o.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			o.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			o.setReportDate(rs.getDate("REPORT_DATE"));
			o.setReportName(rs.getString("REPORT_NAME"));
			o.setCreateUser(rs.getString("CREATE_USER"));
			o.setCreateTime(rs.getDate("CREATE_TIME"));
			o.setModifyUser(rs.getString("MODIFY_USER"));
			o.setModifyTime(rs.getDate("MODIFY_TIME"));
			o.setVerifyUser(rs.getString("VERIFY_USER"));
			o.setVerifyTime(rs.getDate("VERIFY_TIME"));
			o.setEntityFlg(rs.getString("ENTITY_FLG"));
			o.setModifyFlg(rs.getString("MODIFY_FLG"));
			o.setDelFlg(rs.getString("DEL_FLG"));
			return o;
		}
	}
}
