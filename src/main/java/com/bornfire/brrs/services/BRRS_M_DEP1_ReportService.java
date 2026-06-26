package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_M_DEP1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_DEP1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String TBL_SUMMARY = "BRRS_M_DEP1_SUMMARYTABLE";
	private static final String TBL_DETAIL = "BRRS_M_DEP1_DETAILTABLE";
	private static final String TBL_ARCH_SUMMARY = "BRRS_M_DEP1_ARCHIVALTABLE_SUMMARY";
	private static final String TBL_ARCH_DETAIL = "BRRS_M_DEP1_ARCHIVALTABLE_DETAIL";

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	private <T> List<T> getByDate(String tableName, Date reportDate, Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate);
	}

	private <T> List<T> getByDateAndVersion(String tableName, Date reportDate, BigDecimal reportVersion,
			Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate, reportVersion);
	}

	private List<Object> getArchivalDateVersionList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM " + TBL_ARCH_SUMMARY + " ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	private List<M_DEP1_Detail_Entity> getDetailByDate(Date reportDate) {
		String sql = "SELECT * FROM " + TBL_DETAIL + " WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new M_DEP1DetailRowMapper(), reportDate);
	}

	private int getDetailCount(Date reportDate) {
		String sql = "SELECT COUNT(*) FROM " + TBL_DETAIL + " WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reportDate);
		return count != null ? count : 0;
	}

	private List<M_DEP1_Detail_Entity> getDetailByLabelAndCriteria(String reportLabel, String reportAddlCriteria1,
			Date reportDate) {
		String sql = "SELECT * FROM " + TBL_DETAIL
				+ " WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new M_DEP1DetailRowMapper(), reportLabel, reportAddlCriteria1, reportDate);
	}

	private M_DEP1_Detail_Entity findDetailByAcctNumber(String acctNumber) {
		String sql = "SELECT * FROM " + TBL_DETAIL + " WHERE ACCT_NUMBER = ?";
		List<M_DEP1_Detail_Entity> rows = jdbcTemplate.query(sql, new M_DEP1DetailRowMapper(), acctNumber);
		return rows.isEmpty() ? null : rows.get(0);
	}

	private void saveDetailEntity(M_DEP1_Detail_Entity entity) {
		String sql = "UPDATE " + TBL_DETAIL + " SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?";
		jdbcTemplate.update(sql, entity.getAcctName(), entity.getAcctBalanceInpula(), entity.getAcctNumber());
	}

	private List<M_DEP1_Archival_Detail_Entity> getArchivalDetailByDate(Date reportDate, String dataEntryVersion) {
		String sql = "SELECT * FROM " + TBL_ARCH_DETAIL + " WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new M_DEP1ArchivalDetailRowMapper(), reportDate, dataEntryVersion);
	}

	private List<M_DEP1_Archival_Detail_Entity> getArchivalDetailByLabelAndCriteria(String reportLabel,
			String reportAddlCriteria1, Date reportDate, String dataEntryVersion) {
		String sql = "SELECT * FROM " + TBL_ARCH_DETAIL
				+ " WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new M_DEP1ArchivalDetailRowMapper(), reportLabel, reportAddlCriteria1,
				reportDate, dataEntryVersion);
	}

	private M_DEP1_Detail_Entity mapDetailRow(ResultSet rs) throws SQLException {
		M_DEP1_Detail_Entity obj = new M_DEP1_Detail_Entity();
		obj.setCustId(rs.getString("CUST_ID"));
		obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
		obj.setAcctName(rs.getString("ACCT_NAME"));
		obj.setDataType(rs.getString("DATA_TYPE"));
		obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
		obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
		obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
		obj.setReportLabel(rs.getString("REPORT_LABEL"));
		obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
		obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
		obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
		obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
		obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
		obj.setReportDate(rs.getDate("REPORT_DATE"));
		obj.setReportName(rs.getString("REPORT_NAME"));
		obj.setCreateUser(rs.getString("CREATE_USER"));
		obj.setCreateTime(rs.getDate("CREATE_TIME"));
		obj.setModifyUser(rs.getString("MODIFY_USER"));
		obj.setModifyTime(rs.getDate("MODIFY_TIME"));
		obj.setVerifyUser(rs.getString("VERIFY_USER"));
		obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
		obj.setEntityFlg(rs.getString("ENTITY_FLG"));
		obj.setModifyFlg(rs.getString("MODIFY_FLG"));
		obj.setDelFlg(rs.getString("DEL_FLG"));
		return obj;
	}

	private M_DEP1_Archival_Detail_Entity mapArchivalDetailRow(ResultSet rs) throws SQLException {
		M_DEP1_Archival_Detail_Entity obj = new M_DEP1_Archival_Detail_Entity();
		obj.setCustId(rs.getString("CUST_ID"));
		obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
		obj.setAcctName(rs.getString("ACCT_NAME"));
		obj.setDataType(rs.getString("DATA_TYPE"));
		obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
		obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
		obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
		obj.setReportLabel(rs.getString("REPORT_LABEL"));
		obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
		obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
		obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
		obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
		obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
		obj.setReportDate(rs.getDate("REPORT_DATE"));
		obj.setReportName(rs.getString("REPORT_NAME"));
		obj.setCreateUser(rs.getString("CREATE_USER"));
		obj.setCreateTime(rs.getDate("CREATE_TIME"));
		obj.setModifyUser(rs.getString("MODIFY_USER"));
		obj.setModifyTime(rs.getDate("MODIFY_TIME"));
		obj.setVerifyUser(rs.getString("VERIFY_USER"));
		obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
		obj.setEntityFlg(rs.getString("ENTITY_FLG"));
		obj.setModifyFlg(rs.getString("MODIFY_FLG"));
		obj.setDelFlg(rs.getString("DEL_FLG"));
		return obj;
	}

	class M_DEP1DetailRowMapper implements RowMapper<M_DEP1_Detail_Entity> {
		@Override
		public M_DEP1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			return mapDetailRow(rs);
		}
	}

	class M_DEP1ArchivalDetailRowMapper implements RowMapper<M_DEP1_Archival_Detail_Entity> {
		@Override
		public M_DEP1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			return mapArchivalDetailRow(rs);
		}
	}

	public ModelAndView getM_DEP1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		/*
		 * Session hs = sessionFactory.getCurrentSession(); int pageSize =
		 * pageable.getPageSize(); int currentPage = pageable.getPageNumber(); int
		 * startItem = currentPage * pageSize;
		 */

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_DEP1_Archival_Summary_Entity> T1Master = new ArrayList<M_DEP1_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = getByDateAndVersion(TBL_ARCH_SUMMARY, dateformat.parse(todate), version,
						M_DEP1_Archival_Summary_Entity.class);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_DEP1_Summary_Entity> T1Master = new ArrayList<M_DEP1_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = getByDate(TBL_SUMMARY, dateformat.parse(todate), M_DEP1_Summary_Entity.class);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_DEP1");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_DEP1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;

			// ✅ Split the filter string here
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				System.out.println(version);
				// 🔹 Archival branch
				List<M_DEP1_Archival_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = getArchivalDetailByLabelAndCriteria(reportLabel, reportAddlCriteria1, parsedDate, version);
				} else {
					T1Dt1 = getArchivalDetailByDate(parsedDate, version);
					totalPages = getDetailCount(parsedDate);
					System.out.println(T1Dt1.size());
					mv.addObject("pagination", "YES");

				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				System.out.println("Praveen");
				// 🔹 Current branch
				List<M_DEP1_Detail_Entity> T1Dt1;
				if (reportLabel != null && reportAddlCriteria1 != null) {
					T1Dt1 = getDetailByLabelAndCriteria(reportLabel, reportAddlCriteria1, parsedDate);
				} else {
					T1Dt1 = getDetailByDate(parsedDate);
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
		mv.setViewName("BRRS/M_DEP1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] BRRS_M_DEP1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_DEP1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype,
					type, version);
			return ARCHIVALreport;
		}

		List<M_DEP1_Summary_Entity> dataList = getByDate(TBL_SUMMARY, dateformat.parse(todate),
				M_DEP1_Summary_Entity.class);

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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_DEP1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReport_date() != null) {
						cell1.setCellValue(record.getReport_date()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(11);
					cell1 = row.getCell(1);
					if (record.getR12_current() != null) {
						cell1.setCellValue(record.getR12_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_call() != null) {
						cell2.setCellValue(record.getR12_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_savings() != null) {
						cell3.setCellValue(record.getR12_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR12_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR12_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR12_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR12_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR12_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR12_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR12_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR12_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR12_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row12
					// Column L
					Cell cell11 = row.createCell(11);
					if (record.getR12_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR12_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row12
					// Column M
					Cell cell12 = row.createCell(12);
					if (record.getR12_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR12_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row12
					// Column N
					Cell cell13 = row.createCell(13);
					if (record.getR12_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR12_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR13_current() != null) {
						cell1.setCellValue(record.getR13_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_call() != null) {
						cell2.setCellValue(record.getR13_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_savings() != null) {
						cell3.setCellValue(record.getR13_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR13_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR13_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR13_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR13_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR13_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR13_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR13_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row13
					// Column K
					cell10 = row.createCell(10);
					if (record.getR13_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR13_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row13
					// Column L
					cell11 = row.createCell(11);
					if (record.getR13_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR13_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row13
					// Column M
					cell12 = row.createCell(12);
					if (record.getR13_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR13_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row13
					// Column N
					cell13 = row.createCell(13);
					if (record.getR13_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR13_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR14_current() != null) {
						cell1.setCellValue(record.getR14_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_call() != null) {
						cell2.setCellValue(record.getR14_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_savings() != null) {
						cell3.setCellValue(record.getR14_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR14_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR14_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR14_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR14_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR14_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row14
					// Column J
					cell9 = row.createCell(9);
					if (record.getR14_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR14_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row14
					// Column K
					cell10 = row.createCell(10);
					if (record.getR14_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR14_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row14
					// Column L
					cell11 = row.createCell(11);
					if (record.getR14_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR14_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row14
					// Column M
					cell12 = row.createCell(12);
					if (record.getR14_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR14_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row14
					// Column N
					cell13 = row.createCell(13);
					if (record.getR14_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR14_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 16
					row = sheet.getRow(15);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR16_current() != null) {
						cell1.setCellValue(record.getR16_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_call() != null) {
						cell2.setCellValue(record.getR16_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_savings() != null) {
						cell3.setCellValue(record.getR16_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR16_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR16_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR16_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR16_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR16_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 16
					// Column J
					cell9 = row.createCell(9);
					if (record.getR16_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR16_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 16
					// Column K
					cell10 = row.createCell(10);
					if (record.getR16_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR16_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 16
					// Column L
					cell11 = row.createCell(11);
					if (record.getR16_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR16_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 16
					// Column M
					cell12 = row.createCell(12);
					if (record.getR16_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR16_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 16
					// Column N
					cell13 = row.createCell(13);
					if (record.getR16_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR16_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 17
					row = sheet.getRow(16);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR17_current() != null) {
						cell1.setCellValue(record.getR17_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_call() != null) {
						cell2.setCellValue(record.getR17_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_savings() != null) {
						cell3.setCellValue(record.getR17_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR17_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR17_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR17_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR17_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR17_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 17
					// Column J
					cell9 = row.createCell(9);
					if (record.getR17_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR17_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 17
					// Column K
					cell10 = row.createCell(10);
					if (record.getR17_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR17_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 17
					// Column L
					cell11 = row.createCell(11);
					if (record.getR17_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR17_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 17
					// Column M
					cell12 = row.createCell(12);
					if (record.getR17_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR17_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 17
					// Column N
					cell13 = row.createCell(13);
					if (record.getR17_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR17_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 18
					row = sheet.getRow(17);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR18_current() != null) {
						cell1.setCellValue(record.getR18_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_call() != null) {
						cell2.setCellValue(record.getR18_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_savings() != null) {
						cell3.setCellValue(record.getR18_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR18_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR18_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR18_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR18_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR18_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 18
					// Column J
					cell9 = row.createCell(9);
					if (record.getR18_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR18_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 18
					// Column K
					cell10 = row.createCell(10);
					if (record.getR18_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR18_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 18
					// Column L
					cell11 = row.createCell(11);
					if (record.getR18_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR18_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 18
					// Column M
					cell12 = row.createCell(12);
					if (record.getR18_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR18_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 18
					// Column N
					cell13 = row.createCell(13);
					if (record.getR18_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR18_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 19
					row = sheet.getRow(18);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR19_current() != null) {
						cell1.setCellValue(record.getR19_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_call() != null) {
						cell2.setCellValue(record.getR19_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_savings() != null) {
						cell3.setCellValue(record.getR19_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR19_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR19_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR19_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR19_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR19_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 19
					// Column J
					cell9 = row.createCell(9);
					if (record.getR19_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR19_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 19
					// Column K
					cell10 = row.createCell(10);
					if (record.getR19_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR19_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 19
					// Column L
					cell11 = row.createCell(11);
					if (record.getR19_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR19_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 19
					// Column M
					cell12 = row.createCell(12);
					if (record.getR19_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR19_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 19
					// Column N
					cell13 = row.createCell(13);
					if (record.getR19_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR19_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 20
					row = sheet.getRow(19);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR20_current() != null) {
						cell1.setCellValue(record.getR20_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_call() != null) {
						cell2.setCellValue(record.getR20_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_savings() != null) {
						cell3.setCellValue(record.getR20_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR20_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR20_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR20_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR20_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR20_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 20
					// Column J
					cell9 = row.createCell(9);
					if (record.getR20_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR20_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 20
					// Column K
					cell10 = row.createCell(10);
					if (record.getR20_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR20_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 20
					// Column L
					cell11 = row.createCell(11);
					if (record.getR20_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR20_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 20
					// Column M
					cell12 = row.createCell(12);
					if (record.getR20_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR20_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 20
					// Column N
					cell13 = row.createCell(13);
					if (record.getR20_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR20_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 21
					row = sheet.getRow(20);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR21_current() != null) {
						cell1.setCellValue(record.getR21_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_call() != null) {
						cell2.setCellValue(record.getR21_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_savings() != null) {
						cell3.setCellValue(record.getR21_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR21_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR21_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR21_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR21_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR21_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 21
					// Column J
					cell9 = row.createCell(9);
					if (record.getR21_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR21_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 21
					// Column K
					cell10 = row.createCell(10);
					if (record.getR21_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR21_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 21
					// Column L
					cell11 = row.createCell(11);
					if (record.getR21_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR21_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 21
					// Column M
					cell12 = row.createCell(12);
					if (record.getR21_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR21_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 21
					// Column N
					cell13 = row.createCell(13);
					if (record.getR21_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR21_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 22
					row = sheet.getRow(21);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR22_current() != null) {
						cell1.setCellValue(record.getR22_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_call() != null) {
						cell2.setCellValue(record.getR22_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_savings() != null) {
						cell3.setCellValue(record.getR22_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR22_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR22_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR22_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR22_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR22_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 22
					// Column J
					cell9 = row.createCell(9);
					if (record.getR22_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR22_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 22
					// Column K
					cell10 = row.createCell(10);
					if (record.getR22_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR22_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 22
					// Column L
					cell11 = row.createCell(11);
					if (record.getR22_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR22_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 22
					// Column M
					cell12 = row.createCell(12);
					if (record.getR22_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR22_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 22
					// Column N
					cell13 = row.createCell(13);
					if (record.getR22_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR22_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 23
					row = sheet.getRow(22);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR23_current() != null) {
						cell1.setCellValue(record.getR23_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_call() != null) {
						cell2.setCellValue(record.getR23_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_savings() != null) {
						cell3.setCellValue(record.getR23_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR23_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR23_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR23_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR23_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR23_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 23
					// Column J
					cell9 = row.createCell(9);
					if (record.getR23_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR23_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 23
					// Column K
					cell10 = row.createCell(10);
					if (record.getR23_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR23_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 23
					// Column L
					cell11 = row.createCell(11);
					if (record.getR23_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR23_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 23
					// Column M
					cell12 = row.createCell(12);
					if (record.getR23_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR23_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 23
					// Column N
					cell13 = row.createCell(13);
					if (record.getR23_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR23_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 24
					row = sheet.getRow(23);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR24_current() != null) {
						cell1.setCellValue(record.getR24_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_call() != null) {
						cell2.setCellValue(record.getR24_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_savings() != null) {
						cell3.setCellValue(record.getR24_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR24_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR24_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR24_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR24_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR24_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 24
					// Column J
					cell9 = row.createCell(9);
					if (record.getR24_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR24_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 24
					// Column K
					cell10 = row.createCell(10);
					if (record.getR24_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR24_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 24
					// Column L
					cell11 = row.createCell(11);
					if (record.getR24_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR24_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 24
					// Column M
					cell12 = row.createCell(12);
					if (record.getR24_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR24_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 24
					// Column N
					cell13 = row.createCell(13);
					if (record.getR24_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR24_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 25
					row = sheet.getRow(24);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR25_current() != null) {
						cell1.setCellValue(record.getR25_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_call() != null) {
						cell2.setCellValue(record.getR25_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_savings() != null) {
						cell3.setCellValue(record.getR25_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR25_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR25_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR25_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR25_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR25_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 25
					// Column J
					cell9 = row.createCell(9);
					if (record.getR25_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR25_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 25
					// Column K
					cell10 = row.createCell(10);
					if (record.getR25_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR25_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 25
					// Column L
					cell11 = row.createCell(11);
					if (record.getR25_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR25_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 25
					// Column M
					cell12 = row.createCell(12);
					if (record.getR25_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR25_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 25
					// Column N
					cell13 = row.createCell(13);
					if (record.getR25_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR25_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 26
					row = sheet.getRow(25);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR26_current() != null) {
						cell1.setCellValue(record.getR26_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_call() != null) {
						cell2.setCellValue(record.getR26_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_savings() != null) {
						cell3.setCellValue(record.getR26_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR26_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR26_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR26_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR26_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR26_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 26
					// Column J
					cell9 = row.createCell(9);
					if (record.getR26_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR26_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 26
					// Column K
					cell10 = row.createCell(10);
					if (record.getR26_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR26_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 26
					// Column L
					cell11 = row.createCell(11);
					if (record.getR26_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR26_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 26
					// Column M
					cell12 = row.createCell(12);
					if (record.getR26_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR26_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 26
					// Column N
					cell13 = row.createCell(13);
					if (record.getR26_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR26_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 27
					row = sheet.getRow(26);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR27_current() != null) {
						cell1.setCellValue(record.getR27_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_call() != null) {
						cell2.setCellValue(record.getR27_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_savings() != null) {
						cell3.setCellValue(record.getR27_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR27_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR27_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR27_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR27_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR27_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 27
					// Column J
					cell9 = row.createCell(9);
					if (record.getR27_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR27_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 27
					// Column K
					cell10 = row.createCell(10);
					if (record.getR27_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR27_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 27
					// Column L
					cell11 = row.createCell(11);
					if (record.getR27_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR27_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 27
					// Column M
					cell12 = row.createCell(12);
					if (record.getR27_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR27_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 27
					// Column N
					cell13 = row.createCell(13);
					if (record.getR27_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR27_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 28
					row = sheet.getRow(27);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR28_current() != null) {
						cell1.setCellValue(record.getR28_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_call() != null) {
						cell2.setCellValue(record.getR28_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_savings() != null) {
						cell3.setCellValue(record.getR28_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR28_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR28_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR28_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR28_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR28_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 28
					// Column J
					cell9 = row.createCell(9);
					if (record.getR28_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR28_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 28
					// Column K
					cell10 = row.createCell(10);
					if (record.getR28_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR28_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 28
					// Column L
					cell11 = row.createCell(11);
					if (record.getR28_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR28_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 28
					// Column M
					cell12 = row.createCell(12);
					if (record.getR28_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR28_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 28
					// Column N
					cell13 = row.createCell(13);
					if (record.getR28_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR28_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 29
					row = sheet.getRow(28);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR29_current() != null) {
						cell1.setCellValue(record.getR29_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 29
					// Column C
					cell2 = row.createCell(2);
					if (record.getR29_call() != null) {
						cell2.setCellValue(record.getR29_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_savings() != null) {
						cell3.setCellValue(record.getR29_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR29_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR29_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR29_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR29_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR29_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 29
					// Column J
					cell9 = row.createCell(9);
					if (record.getR29_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR29_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 29
					// Column K
					cell10 = row.createCell(10);
					if (record.getR29_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR29_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 29
					// Column L
					cell11 = row.createCell(11);
					if (record.getR29_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR29_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 29
					// Column M
					cell12 = row.createCell(12);
					if (record.getR29_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR29_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 29
					// Column N
					cell13 = row.createCell(13);
					if (record.getR29_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR29_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 31
					row = sheet.getRow(30);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR31_current() != null) {
						cell1.setCellValue(record.getR31_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_call() != null) {
						cell2.setCellValue(record.getR31_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_savings() != null) {
						cell3.setCellValue(record.getR31_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR31_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR31_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR31_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR31_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR31_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 31
					// Column J
					cell9 = row.createCell(9);
					if (record.getR31_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR31_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 31
					// Column K
					cell10 = row.createCell(10);
					if (record.getR31_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR31_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 31
					// Column L
					cell11 = row.createCell(11);
					if (record.getR31_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR31_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 31
					// Column M
					cell12 = row.createCell(12);
					if (record.getR31_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR31_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 31
					// Column N
					cell13 = row.createCell(13);
					if (record.getR31_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR31_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 32
					row = sheet.getRow(31);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR32_current() != null) {
						cell1.setCellValue(record.getR32_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_call() != null) {
						cell2.setCellValue(record.getR32_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_savings() != null) {
						cell3.setCellValue(record.getR32_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR32_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR32_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR32_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR32_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR32_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 32
					// Column J
					cell9 = row.createCell(9);
					if (record.getR32_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR32_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 32
					// Column K
					cell10 = row.createCell(10);
					if (record.getR32_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR32_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 32
					// Column L
					cell11 = row.createCell(11);
					if (record.getR32_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR32_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 32
					// Column M
					cell12 = row.createCell(12);
					if (record.getR32_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR32_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 32
					// Column N
					cell13 = row.createCell(13);
					if (record.getR32_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR32_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 34
					row = sheet.getRow(33);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR34_current() != null) {
						cell1.setCellValue(record.getR34_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_call() != null) {
						cell2.setCellValue(record.getR34_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_savings() != null) {
						cell3.setCellValue(record.getR34_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 34
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR34_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR34_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 34
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR34_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 34
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR34_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 34
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR34_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 34
					// Column J
					cell9 = row.createCell(9);
					if (record.getR34_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR34_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 34
					// Column K
					cell10 = row.createCell(10);
					if (record.getR34_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR34_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 34
					// Column L
					cell11 = row.createCell(11);
					if (record.getR34_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR34_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 34
					// Column M
					cell12 = row.createCell(12);
					if (record.getR34_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR34_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 34
					// Column N
					cell13 = row.createCell(13);
					if (record.getR34_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR34_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 35
					row = sheet.getRow(34);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR35_current() != null) {
						cell1.setCellValue(record.getR35_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_call() != null) {
						cell2.setCellValue(record.getR35_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_savings() != null) {
						cell3.setCellValue(record.getR35_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR35_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR35_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR35_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR35_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR35_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 35
					// Column J
					cell9 = row.createCell(9);
					if (record.getR35_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR35_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 35
					// Column K
					cell10 = row.createCell(10);
					if (record.getR35_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR35_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 35
					// Column L
					cell11 = row.createCell(11);
					if (record.getR35_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR35_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 35
					// Column M
					cell12 = row.createCell(12);
					if (record.getR35_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR35_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 35
					// Column N
					cell13 = row.createCell(13);
					if (record.getR35_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR35_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 37
					row = sheet.getRow(36);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR37_current() != null) {
						cell1.setCellValue(record.getR37_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_call() != null) {
						cell2.setCellValue(record.getR37_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_savings() != null) {
						cell3.setCellValue(record.getR37_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR37_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR37_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR37_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR37_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR37_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 37
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR37_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 37
					// Column K
					cell10 = row.createCell(10);
					if (record.getR37_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR37_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 37
					// Column L
					cell11 = row.createCell(11);
					if (record.getR37_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR37_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 37
					// Column M
					cell12 = row.createCell(12);
					if (record.getR37_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR37_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 37
					// Column N
					cell13 = row.createCell(13);
					if (record.getR37_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR37_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 38
					row = sheet.getRow(37);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR38_current() != null) {
						cell1.setCellValue(record.getR38_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_call() != null) {
						cell2.setCellValue(record.getR38_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_savings() != null) {
						cell3.setCellValue(record.getR38_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR38_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR38_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR38_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR38_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR38_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 38
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR38_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 38
					// Column K
					cell10 = row.createCell(10);
					if (record.getR38_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR38_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 38
					// Column L
					cell11 = row.createCell(11);
					if (record.getR38_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR38_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 38
					// Column M
					cell12 = row.createCell(12);
					if (record.getR38_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR38_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 38
					// Column N
					cell13 = row.createCell(13);
					if (record.getR38_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR38_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 39
					row = sheet.getRow(38);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR39_current() != null) {
						cell1.setCellValue(record.getR39_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_call() != null) {
						cell2.setCellValue(record.getR39_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_savings() != null) {
						cell3.setCellValue(record.getR39_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 39
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR39_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 39
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR39_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 39
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR39_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 39
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR39_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 39
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR39_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 39
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR39_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 39
					// Column K
					cell10 = row.createCell(10);
					if (record.getR39_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR39_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 39
					// Column L
					cell11 = row.createCell(11);
					if (record.getR39_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR39_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 39
					// Column M
					cell12 = row.createCell(12);
					if (record.getR39_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR39_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 39
					// Column N
					cell13 = row.createCell(13);
					if (record.getR39_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR39_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 40
					row = sheet.getRow(39);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR40_current() != null) {
						cell1.setCellValue(record.getR40_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_call() != null) {
						cell2.setCellValue(record.getR40_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_savings() != null) {
						cell3.setCellValue(record.getR40_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 40
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR40_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 40
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR40_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 40
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR40_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 40
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR40_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 40
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR40_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 40
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR40_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 40
					// Column K
					cell10 = row.createCell(10);
					if (record.getR40_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR40_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 40
					// Column L
					cell11 = row.createCell(11);
					if (record.getR40_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR40_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 40
					// Column M
					cell12 = row.createCell(12);
					if (record.getR40_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR40_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 40
					// Column N
					cell13 = row.createCell(13);
					if (record.getR40_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR40_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(41);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR42_current() != null) {
						cell1.setCellValue(record.getR42_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_call() != null) {
						cell2.setCellValue(record.getR42_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_savings() != null) {
						cell3.setCellValue(record.getR42_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 42
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR42_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 42
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR42_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 42
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR42_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 42
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR42_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 42
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR42_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 42
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR42_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 42
					// Column K
					cell10 = row.createCell(10);
					if (record.getR42_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR42_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 42
					// Column L
					cell11 = row.createCell(11);
					if (record.getR42_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR42_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 42
					// Column M
					cell12 = row.createCell(12);
					if (record.getR42_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR42_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 42
					// Column N
					cell13 = row.createCell(13);
					if (record.getR42_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR42_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(42);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR43_current() != null) {
						cell1.setCellValue(record.getR43_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_call() != null) {
						cell2.setCellValue(record.getR43_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_savings() != null) {
						cell3.setCellValue(record.getR43_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 43
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR43_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 43
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR43_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 43
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR43_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 43
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR43_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 43
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR43_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 43
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR43_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 43
					// Column K
					cell10 = row.createCell(10);
					if (record.getR43_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR43_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 43
					// Column L
					cell11 = row.createCell(11);
					if (record.getR43_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR43_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 43
					// Column M
					cell12 = row.createCell(12);
					if (record.getR43_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR43_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 43
					// Column N
					cell13 = row.createCell(13);
					if (record.getR43_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR43_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 44
					row = sheet.getRow(43);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR44_current() != null) {
						cell1.setCellValue(record.getR44_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_call() != null) {
						cell2.setCellValue(record.getR44_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 44
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_savings() != null) {
						cell3.setCellValue(record.getR44_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 44
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR44_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 44
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR44_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 44
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR44_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 44
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR44_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 44
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR44_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 44
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR44_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 44
					// Column K
					cell10 = row.createCell(10);
					if (record.getR44_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR44_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 44
					// Column L
					cell11 = row.createCell(11);
					if (record.getR44_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR44_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 44
					// Column M
					cell12 = row.createCell(12);
					if (record.getR44_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR44_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 44
					// Column N
					cell13 = row.createCell(13);
					if (record.getR44_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR44_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 46
					row = sheet.getRow(45);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR46_current() != null) {
						cell1.setCellValue(record.getR46_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_call() != null) {
						cell2.setCellValue(record.getR46_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_savings() != null) {
						cell3.setCellValue(record.getR46_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 46
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR46_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 46
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR46_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 46
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR46_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 46
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR46_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 46
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR46_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 46
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR46_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 46
					// Column K
					cell10 = row.createCell(10);
					if (record.getR46_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR46_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 46
					// Column L
					cell11 = row.createCell(11);
					if (record.getR46_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR46_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 46
					// Column M
					cell12 = row.createCell(12);
					if (record.getR46_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR46_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 46
					// Column N
					cell13 = row.createCell(13);
					if (record.getR46_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR46_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 47
					row = sheet.getRow(46);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR47_current() != null) {
						cell1.setCellValue(record.getR47_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_call() != null) {
						cell2.setCellValue(record.getR47_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_savings() != null) {
						cell3.setCellValue(record.getR47_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 47
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR47_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 47
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR47_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 47
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR47_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 47
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR47_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 47
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR47_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 47
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR47_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 47
					// Column K
					cell10 = row.createCell(10);
					if (record.getR47_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR47_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 47
					// Column L
					cell11 = row.createCell(11);
					if (record.getR47_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR47_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 47
					// Column M
					cell12 = row.createCell(12);
					if (record.getR47_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR47_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 47
					// Column N
					cell13 = row.createCell(13);
					if (record.getR47_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR47_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 48
					row = sheet.getRow(47);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR48_current() != null) {
						cell1.setCellValue(record.getR48_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_call() != null) {
						cell2.setCellValue(record.getR48_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_savings() != null) {
						cell3.setCellValue(record.getR48_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 48
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR48_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 48
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR48_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 48
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR48_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 48
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR48_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 48
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR48_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 48
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR48_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 48
					// Column K
					cell10 = row.createCell(10);
					if (record.getR48_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR48_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 48
					// Column L
					cell11 = row.createCell(11);
					if (record.getR48_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR48_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 48
					// Column M
					cell12 = row.createCell(12);
					if (record.getR48_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR48_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 48
					// Column N
					cell13 = row.createCell(13);
					if (record.getR48_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR48_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 50
					row = sheet.getRow(49);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR50_current() != null) {
						cell1.setCellValue(record.getR50_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_call() != null) {
						cell2.setCellValue(record.getR50_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_savings() != null) {
						cell3.setCellValue(record.getR50_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 50
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR50_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 50
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR50_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 50
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR50_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 50
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR50_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 50
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR50_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 50
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR50_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 50
					// Column K
					cell10 = row.createCell(10);
					if (record.getR50_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR50_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 50
					// Column L
					cell11 = row.createCell(11);
					if (record.getR50_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR50_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 50
					// Column M
					cell12 = row.createCell(12);
					if (record.getR50_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR50_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 50
					// Column N
					cell13 = row.createCell(13);
					if (record.getR50_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR50_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 51
					row = sheet.getRow(50);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR51_current() != null) {
						cell1.setCellValue(record.getR51_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_call() != null) {
						cell2.setCellValue(record.getR51_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_savings() != null) {
						cell3.setCellValue(record.getR51_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR51_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR51_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR51_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR51_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR51_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR51_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 51
					// Column K
					cell10 = row.createCell(10);
					if (record.getR51_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR51_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 51
					// Column L
					cell11 = row.createCell(11);
					if (record.getR51_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR51_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 51
					// Column M
					cell12 = row.createCell(12);
					if (record.getR51_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR51_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 51
					// Column N
					cell13 = row.createCell(13);
					if (record.getR51_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR51_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 52
					row = sheet.getRow(51);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR52_current() != null) {
						cell1.setCellValue(record.getR52_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_call() != null) {
						cell2.setCellValue(record.getR52_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_savings() != null) {
						cell3.setCellValue(record.getR52_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR52_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR52_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR52_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR52_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR52_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR52_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 52
					// Column K
					cell10 = row.createCell(10);
					if (record.getR52_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR52_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 52
					// Column L
					cell11 = row.createCell(11);
					if (record.getR52_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR52_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 52
					// Column M
					cell12 = row.createCell(12);
					if (record.getR52_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR52_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 52
					// Column N
					cell13 = row.createCell(13);
					if (record.getR52_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR52_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 53
					row = sheet.getRow(52);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR53_current() != null) {
						cell1.setCellValue(record.getR53_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_call() != null) {
						cell2.setCellValue(record.getR53_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_savings() != null) {
						cell3.setCellValue(record.getR53_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR53_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR53_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR53_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR53_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR53_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR53_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 53
					// Column K
					cell10 = row.createCell(10);
					if (record.getR53_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR53_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 53
					// Column L
					cell11 = row.createCell(11);
					if (record.getR53_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR53_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 53
					// Column M
					cell12 = row.createCell(12);
					if (record.getR53_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR53_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 53
					// Column N
					cell13 = row.createCell(13);
					if (record.getR53_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR53_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 54
					row = sheet.getRow(53);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR54_current() != null) {
						cell1.setCellValue(record.getR54_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_call() != null) {
						cell2.setCellValue(record.getR54_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_savings() != null) {
						cell3.setCellValue(record.getR54_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR54_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR54_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR54_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR54_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR54_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR54_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 54
					// Column K
					cell10 = row.createCell(10);
					if (record.getR54_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR54_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 54
					// Column L
					cell11 = row.createCell(11);
					if (record.getR54_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR54_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 54
					// Column M
					cell12 = row.createCell(12);
					if (record.getR54_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR54_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 54
					// Column N
					cell13 = row.createCell(13);
					if (record.getR54_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR54_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 55
					row = sheet.getRow(54);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR55_current() != null) {
						cell1.setCellValue(record.getR55_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_call() != null) {
						cell2.setCellValue(record.getR55_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_savings() != null) {
						cell3.setCellValue(record.getR55_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR55_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR55_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR55_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR55_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR55_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR55_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 55
					// Column K
					cell10 = row.createCell(10);
					if (record.getR55_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR55_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 55
					// Column L
					cell11 = row.createCell(11);
					if (record.getR55_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR55_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 55
					// Column M
					cell12 = row.createCell(12);
					if (record.getR55_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR55_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 55
					// Column N
					cell13 = row.createCell(13);
					if (record.getR55_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR55_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 57
					row = sheet.getRow(56);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR57_current() != null) {
						cell1.setCellValue(record.getR57_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_call() != null) {
						cell2.setCellValue(record.getR57_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_savings() != null) {
						cell3.setCellValue(record.getR57_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR57_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR57_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR57_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR57_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR57_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR57_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 57
					// Column K
					cell10 = row.createCell(10);
					if (record.getR57_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR57_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 57
					// Column L
					cell11 = row.createCell(11);
					if (record.getR57_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR57_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 57
					// Column M
					cell12 = row.createCell(12);
					if (record.getR57_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR57_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 57
					// Column N
					cell13 = row.createCell(13);
					if (record.getR57_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR57_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_DEP1 SUMMARY", null,
						"BRRS_M_DEP1_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_DEP1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_DEP1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_DEP1Details");

// Common border style
			BorderStyle border = BorderStyle.THIN;
// Header style (left aligned)
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

// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABEL",
					"REPORT_ADDL_CRITERIA_1", "REPORT_DATE" };
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
// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_DEP1_Detail_Entity> reportData = getDetailByDate(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_DEP1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
// ACCT BALANCE (right aligned)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");
// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_DEP1 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_DEP1 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_DEP1Archival() {
		List<Object> M_DEP1Archivallist = new ArrayList<>();
		try {
			String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_DATE FROM " + TBL_ARCH_SUMMARY
					+ " ORDER BY REPORT_VERSION";
			M_DEP1Archivallist = jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
					rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_DATE") // This is the 3rd element (index 2)
			});
			System.out.println("countser" + M_DEP1Archivallist.size());
		} catch (Exception e) {
			System.err.println("Error fetching M_DEP1 Archival data: " + e.getMessage());
			e.printStackTrace();
		}
		return M_DEP1Archivallist;
	}

	public byte[] getExcelM_DEP1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_DEP1_Archival_Summary_Entity> dataList = getByDateAndVersion(TBL_ARCH_SUMMARY, dateformat.parse(todate),
				version, M_DEP1_Archival_Summary_Entity.class);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_DEP1 report. Returning empty result.");
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
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_DEP1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReport_date() != null) {
						cell1.setCellValue(record.getReport_date()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(11);
					cell1 = row.getCell(1);
					if (record.getR12_current() != null) {
						cell1.setCellValue(record.getR12_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_call() != null) {
						cell2.setCellValue(record.getR12_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_savings() != null) {
						cell3.setCellValue(record.getR12_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR12_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR12_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR12_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR12_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR12_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR12_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR12_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR12_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR12_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row12
					// Column L
					Cell cell11 = row.createCell(11);
					if (record.getR12_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR12_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row12
					// Column M
					Cell cell12 = row.createCell(12);
					if (record.getR12_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR12_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row12
					// Column N
					Cell cell13 = row.createCell(13);
					if (record.getR12_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR12_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR13_current() != null) {
						cell1.setCellValue(record.getR13_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_call() != null) {
						cell2.setCellValue(record.getR13_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_savings() != null) {
						cell3.setCellValue(record.getR13_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR13_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR13_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR13_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR13_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR13_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR13_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR13_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row13
					// Column K
					cell10 = row.createCell(10);
					if (record.getR13_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR13_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row13
					// Column L
					cell11 = row.createCell(11);
					if (record.getR13_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR13_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row13
					// Column M
					cell12 = row.createCell(12);
					if (record.getR13_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR13_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row13
					// Column N
					cell13 = row.createCell(13);
					if (record.getR13_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR13_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR14_current() != null) {
						cell1.setCellValue(record.getR14_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);

					}

					// row14
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_call() != null) {
						cell2.setCellValue(record.getR14_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_savings() != null) {
						cell3.setCellValue(record.getR14_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR14_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR14_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR14_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR14_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR14_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row14
					// Column J
					cell9 = row.createCell(9);
					if (record.getR14_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR14_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row14
					// Column K
					cell10 = row.createCell(10);
					if (record.getR14_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR14_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row14
					// Column L
					cell11 = row.createCell(11);
					if (record.getR14_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR14_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row14
					// Column M
					cell12 = row.createCell(12);
					if (record.getR14_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR14_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row14
					// Column N
					cell13 = row.createCell(13);
					if (record.getR14_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR14_certificates_of_deposit().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 16
					row = sheet.getRow(15);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR16_current() != null) {
						cell1.setCellValue(record.getR16_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 16
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_call() != null) {
						cell2.setCellValue(record.getR16_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_savings() != null) {
						cell3.setCellValue(record.getR16_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR16_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR16_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR16_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR16_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR16_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 16
					// Column J
					cell9 = row.createCell(9);
					if (record.getR16_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR16_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 16
					// Column K
					cell10 = row.createCell(10);
					if (record.getR16_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR16_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 16
					// Column L
					cell11 = row.createCell(11);
					if (record.getR16_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR16_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 16
					// Column M
					cell12 = row.createCell(12);
					if (record.getR16_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR16_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 16
					// Column N
					cell13 = row.createCell(13);
					if (record.getR16_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR16_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 17
					row = sheet.getRow(16);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR17_current() != null) {
						cell1.setCellValue(record.getR17_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 17
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_call() != null) {
						cell2.setCellValue(record.getR17_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_savings() != null) {
						cell3.setCellValue(record.getR17_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR17_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR17_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR17_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR17_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR17_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 17
					// Column J
					cell9 = row.createCell(9);
					if (record.getR17_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR17_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 17
					// Column K
					cell10 = row.createCell(10);
					if (record.getR17_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR17_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 17
					// Column L
					cell11 = row.createCell(11);
					if (record.getR17_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR17_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 17
					// Column M
					cell12 = row.createCell(12);
					if (record.getR17_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR17_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 17
					// Column N
					cell13 = row.createCell(13);
					if (record.getR17_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR17_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 18
					row = sheet.getRow(17);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR18_current() != null) {
						cell1.setCellValue(record.getR18_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 18
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_call() != null) {
						cell2.setCellValue(record.getR18_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_savings() != null) {
						cell3.setCellValue(record.getR18_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR18_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR18_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR18_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR18_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR18_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 18
					// Column J
					cell9 = row.createCell(9);
					if (record.getR18_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR18_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 18
					// Column K
					cell10 = row.createCell(10);
					if (record.getR18_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR18_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 18
					// Column L
					cell11 = row.createCell(11);
					if (record.getR18_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR18_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 18
					// Column M
					cell12 = row.createCell(12);
					if (record.getR18_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR18_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 18
					// Column N
					cell13 = row.createCell(13);
					if (record.getR18_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR18_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 19
					row = sheet.getRow(18);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR19_current() != null) {
						cell1.setCellValue(record.getR19_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 19
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_call() != null) {
						cell2.setCellValue(record.getR19_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_savings() != null) {
						cell3.setCellValue(record.getR19_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR19_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR19_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR19_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR19_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR19_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 19
					// Column J
					cell9 = row.createCell(9);
					if (record.getR19_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR19_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 19
					// Column K
					cell10 = row.createCell(10);
					if (record.getR19_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR19_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 19
					// Column L
					cell11 = row.createCell(11);
					if (record.getR19_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR19_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 19
					// Column M
					cell12 = row.createCell(12);
					if (record.getR19_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR19_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 19
					// Column N
					cell13 = row.createCell(13);
					if (record.getR19_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR19_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 20
					row = sheet.getRow(19);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR20_current() != null) {
						cell1.setCellValue(record.getR20_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 20
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_call() != null) {
						cell2.setCellValue(record.getR20_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_savings() != null) {
						cell3.setCellValue(record.getR20_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR20_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR20_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR20_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR20_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR20_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 20
					// Column J
					cell9 = row.createCell(9);
					if (record.getR20_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR20_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 20
					// Column K
					cell10 = row.createCell(10);
					if (record.getR20_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR20_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 20
					// Column L
					cell11 = row.createCell(11);
					if (record.getR20_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR20_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 20
					// Column M
					cell12 = row.createCell(12);
					if (record.getR20_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR20_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 20
					// Column N
					cell13 = row.createCell(13);
					if (record.getR20_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR20_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 21
					row = sheet.getRow(20);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR21_current() != null) {
						cell1.setCellValue(record.getR21_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 21
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_call() != null) {
						cell2.setCellValue(record.getR21_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_savings() != null) {
						cell3.setCellValue(record.getR21_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR21_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR21_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR21_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR21_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR21_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 21
					// Column J
					cell9 = row.createCell(9);
					if (record.getR21_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR21_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 21
					// Column K
					cell10 = row.createCell(10);
					if (record.getR21_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR21_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 21
					// Column L
					cell11 = row.createCell(11);
					if (record.getR21_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR21_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 21
					// Column M
					cell12 = row.createCell(12);
					if (record.getR21_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR21_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 21
					// Column N
					cell13 = row.createCell(13);
					if (record.getR21_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR21_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 22
					row = sheet.getRow(21);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR22_current() != null) {
						cell1.setCellValue(record.getR22_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 22
					// Column C
					cell2 = row.createCell(2);
					if (record.getR22_call() != null) {
						cell2.setCellValue(record.getR22_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_savings() != null) {
						cell3.setCellValue(record.getR22_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR22_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR22_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR22_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR22_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR22_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 22
					// Column J
					cell9 = row.createCell(9);
					if (record.getR22_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR22_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 22
					// Column K
					cell10 = row.createCell(10);
					if (record.getR22_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR22_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 22
					// Column L
					cell11 = row.createCell(11);
					if (record.getR22_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR22_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 22
					// Column M
					cell12 = row.createCell(12);
					if (record.getR22_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR22_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 22
					// Column N
					cell13 = row.createCell(13);
					if (record.getR22_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR22_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 23
					row = sheet.getRow(22);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR23_current() != null) {
						cell1.setCellValue(record.getR23_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 23
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_call() != null) {
						cell2.setCellValue(record.getR23_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_savings() != null) {
						cell3.setCellValue(record.getR23_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR23_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR23_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR23_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR23_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR23_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 23
					// Column J
					cell9 = row.createCell(9);
					if (record.getR23_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR23_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 23
					// Column K
					cell10 = row.createCell(10);
					if (record.getR23_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR23_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 23
					// Column L
					cell11 = row.createCell(11);
					if (record.getR23_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR23_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 23
					// Column M
					cell12 = row.createCell(12);
					if (record.getR23_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR23_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 23
					// Column N
					cell13 = row.createCell(13);
					if (record.getR23_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR23_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 24
					row = sheet.getRow(23);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR24_current() != null) {
						cell1.setCellValue(record.getR24_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 24
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_call() != null) {
						cell2.setCellValue(record.getR24_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_savings() != null) {
						cell3.setCellValue(record.getR24_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR24_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR24_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR24_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR24_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR24_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 24
					// Column J
					cell9 = row.createCell(9);
					if (record.getR24_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR24_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 24
					// Column K
					cell10 = row.createCell(10);
					if (record.getR24_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR24_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 24
					// Column L
					cell11 = row.createCell(11);
					if (record.getR24_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR24_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 24
					// Column M
					cell12 = row.createCell(12);
					if (record.getR24_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR24_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 24
					// Column N
					cell13 = row.createCell(13);
					if (record.getR24_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR24_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 25
					row = sheet.getRow(24);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR25_current() != null) {
						cell1.setCellValue(record.getR25_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 25
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_call() != null) {
						cell2.setCellValue(record.getR25_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_savings() != null) {
						cell3.setCellValue(record.getR25_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR25_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR25_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR25_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR25_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR25_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 25
					// Column J
					cell9 = row.createCell(9);
					if (record.getR25_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR25_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 25
					// Column K
					cell10 = row.createCell(10);
					if (record.getR25_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR25_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 25
					// Column L
					cell11 = row.createCell(11);
					if (record.getR25_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR25_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 25
					// Column M
					cell12 = row.createCell(12);
					if (record.getR25_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR25_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 25
					// Column N
					cell13 = row.createCell(13);
					if (record.getR25_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR25_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 26
					row = sheet.getRow(25);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR26_current() != null) {
						cell1.setCellValue(record.getR26_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Row 26
					// Column C
					cell2 = row.createCell(2);
					if (record.getR26_call() != null) {
						cell2.setCellValue(record.getR26_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Row 26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_savings() != null) {
						cell3.setCellValue(record.getR26_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Row 26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR26_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Row 26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR26_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Row 26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR26_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Row 26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR26_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Row 26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR26_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Row 26
					// Column J
					cell9 = row.createCell(9);
					if (record.getR26_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR26_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Row 26
					// Column K
					cell10 = row.createCell(10);
					if (record.getR26_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR26_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Row 26
					// Column L
					cell11 = row.createCell(11);
					if (record.getR26_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR26_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Row 26
					// Column M
					cell12 = row.createCell(12);
					if (record.getR26_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR26_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Row 26
					// Column N
					cell13 = row.createCell(13);
					if (record.getR26_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR26_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 27
					row = sheet.getRow(26);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR27_current() != null) {
						cell1.setCellValue(record.getR27_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 27
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_call() != null) {
						cell2.setCellValue(record.getR27_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_savings() != null) {
						cell3.setCellValue(record.getR27_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR27_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR27_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR27_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR27_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR27_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 27
					// Column J
					cell9 = row.createCell(9);
					if (record.getR27_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR27_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 27
					// Column K
					cell10 = row.createCell(10);
					if (record.getR27_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR27_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 27
					// Column L
					cell11 = row.createCell(11);
					if (record.getR27_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR27_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 27
					// Column M
					cell12 = row.createCell(12);
					if (record.getR27_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR27_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 27
					// Column N
					cell13 = row.createCell(13);
					if (record.getR27_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR27_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 28
					row = sheet.getRow(27);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR28_current() != null) {
						cell1.setCellValue(record.getR28_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 28
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_call() != null) {
						cell2.setCellValue(record.getR28_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_savings() != null) {
						cell3.setCellValue(record.getR28_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR28_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR28_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR28_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR28_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR28_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 28
					// Column J
					cell9 = row.createCell(9);
					if (record.getR28_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR28_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 28
					// Column K
					cell10 = row.createCell(10);
					if (record.getR28_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR28_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 28
					// Column L
					cell11 = row.createCell(11);
					if (record.getR28_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR28_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 28
					// Column M
					cell12 = row.createCell(12);
					if (record.getR28_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR28_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 28
					// Column N
					cell13 = row.createCell(13);
					if (record.getR28_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR28_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 29
					row = sheet.getRow(28);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR29_current() != null) {
						cell1.setCellValue(record.getR29_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 29
					// Column C
					cell2 = row.createCell(2);
					if (record.getR29_call() != null) {
						cell2.setCellValue(record.getR29_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_savings() != null) {
						cell3.setCellValue(record.getR29_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR29_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR29_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR29_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR29_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR29_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 29
					// Column J
					cell9 = row.createCell(9);
					if (record.getR29_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR29_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 29
					// Column K
					cell10 = row.createCell(10);
					if (record.getR29_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR29_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 29
					// Column L
					cell11 = row.createCell(11);
					if (record.getR29_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR29_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 29
					// Column M
					cell12 = row.createCell(12);
					if (record.getR29_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR29_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 29
					// Column N
					cell13 = row.createCell(13);
					if (record.getR29_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR29_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 31
					row = sheet.getRow(30);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR31_current() != null) {
						cell1.setCellValue(record.getR31_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 31
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_call() != null) {
						cell2.setCellValue(record.getR31_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_savings() != null) {
						cell3.setCellValue(record.getR31_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR31_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR31_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR31_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR31_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR31_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 31
					// Column J
					cell9 = row.createCell(9);
					if (record.getR31_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR31_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 31
					// Column K
					cell10 = row.createCell(10);
					if (record.getR31_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR31_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 31
					// Column L
					cell11 = row.createCell(11);
					if (record.getR31_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR31_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 31
					// Column M
					cell12 = row.createCell(12);
					if (record.getR31_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR31_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 31
					// Column N
					cell13 = row.createCell(13);
					if (record.getR31_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR31_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 32
					row = sheet.getRow(31);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR32_current() != null) {
						cell1.setCellValue(record.getR32_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 32
					// Column C
					cell2 = row.createCell(2);
					if (record.getR32_call() != null) {
						cell2.setCellValue(record.getR32_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_savings() != null) {
						cell3.setCellValue(record.getR32_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR32_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR32_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR32_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR32_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR32_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 32
					// Column J
					cell9 = row.createCell(9);
					if (record.getR32_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR32_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 32
					// Column K
					cell10 = row.createCell(10);
					if (record.getR32_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR32_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 32
					// Column L
					cell11 = row.createCell(11);
					if (record.getR32_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR32_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 32
					// Column M
					cell12 = row.createCell(12);
					if (record.getR32_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR32_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 32
					// Column N
					cell13 = row.createCell(13);
					if (record.getR32_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR32_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 34
					row = sheet.getRow(33);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR34_current() != null) {
						cell1.setCellValue(record.getR34_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 34
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_call() != null) {
						cell2.setCellValue(record.getR34_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_savings() != null) {
						cell3.setCellValue(record.getR34_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 34
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR34_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR34_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 34
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR34_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 34
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR34_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 34
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR34_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 34
					// Column J
					cell9 = row.createCell(9);
					if (record.getR34_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR34_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 34
					// Column K
					cell10 = row.createCell(10);
					if (record.getR34_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR34_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 34
					// Column L
					cell11 = row.createCell(11);
					if (record.getR34_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR34_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 34
					// Column M
					cell12 = row.createCell(12);
					if (record.getR34_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR34_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 34
					// Column N
					cell13 = row.createCell(13);
					if (record.getR34_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR34_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 35
					row = sheet.getRow(34);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR35_current() != null) {
						cell1.setCellValue(record.getR35_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 35
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_call() != null) {
						cell2.setCellValue(record.getR35_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_savings() != null) {
						cell3.setCellValue(record.getR35_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR35_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR35_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR35_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR35_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR35_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 35
					// Column J
					cell9 = row.createCell(9);
					if (record.getR35_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR35_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 35
					// Column K
					cell10 = row.createCell(10);
					if (record.getR35_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR35_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 35
					// Column L
					cell11 = row.createCell(11);
					if (record.getR35_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR35_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 35
					// Column M
					cell12 = row.createCell(12);
					if (record.getR35_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR35_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 35
					// Column N
					cell13 = row.createCell(13);
					if (record.getR35_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR35_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 37
					row = sheet.getRow(36);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR37_current() != null) {
						cell1.setCellValue(record.getR37_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 37
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_call() != null) {
						cell2.setCellValue(record.getR37_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_savings() != null) {
						cell3.setCellValue(record.getR37_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR37_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR37_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR37_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR37_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR37_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 37
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR37_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 37
					// Column K
					cell10 = row.createCell(10);
					if (record.getR37_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR37_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 37
					// Column L
					cell11 = row.createCell(11);
					if (record.getR37_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR37_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 37
					// Column M
					cell12 = row.createCell(12);
					if (record.getR37_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR37_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 37
					// Column N
					cell13 = row.createCell(13);
					if (record.getR37_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR37_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 38
					row = sheet.getRow(37);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR38_current() != null) {
						cell1.setCellValue(record.getR38_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_call() != null) {
						cell2.setCellValue(record.getR38_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_savings() != null) {
						cell3.setCellValue(record.getR38_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR38_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR38_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR38_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR38_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR38_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 38
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR38_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 38
					// Column K
					cell10 = row.createCell(10);
					if (record.getR38_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR38_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 38
					// Column L
					cell11 = row.createCell(11);
					if (record.getR38_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR38_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 38
					// Column M
					cell12 = row.createCell(12);
					if (record.getR38_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR38_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 38
					// Column N
					cell13 = row.createCell(13);
					if (record.getR38_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR38_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 39
					row = sheet.getRow(38);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR39_current() != null) {
						cell1.setCellValue(record.getR39_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_call() != null) {
						cell2.setCellValue(record.getR39_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_savings() != null) {
						cell3.setCellValue(record.getR39_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 39
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR39_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 39
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR39_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 39
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR39_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 39
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR39_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 39
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR39_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 39
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR39_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 39
					// Column K
					cell10 = row.createCell(10);
					if (record.getR39_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR39_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 39
					// Column L
					cell11 = row.createCell(11);
					if (record.getR39_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR39_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 39
					// Column M
					cell12 = row.createCell(12);
					if (record.getR39_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR39_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 39
					// Column N
					cell13 = row.createCell(13);
					if (record.getR39_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR39_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 40
					row = sheet.getRow(39);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR40_current() != null) {
						cell1.setCellValue(record.getR40_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_call() != null) {
						cell2.setCellValue(record.getR40_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_savings() != null) {
						cell3.setCellValue(record.getR40_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 40
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR40_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 40
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR40_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 40
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR40_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 40
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR40_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 40
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR40_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 40
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR40_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 40
					// Column K
					cell10 = row.createCell(10);
					if (record.getR40_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR40_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 40
					// Column L
					cell11 = row.createCell(11);
					if (record.getR40_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR40_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 40
					// Column M
					cell12 = row.createCell(12);
					if (record.getR40_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR40_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 40
					// Column N
					cell13 = row.createCell(13);
					if (record.getR40_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR40_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(41);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR42_current() != null) {
						cell1.setCellValue(record.getR42_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_call() != null) {
						cell2.setCellValue(record.getR42_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_savings() != null) {
						cell3.setCellValue(record.getR42_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 42
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR42_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 42
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR42_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 42
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR42_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 42
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR42_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 42
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR42_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 42
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR42_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 42
					// Column K
					cell10 = row.createCell(10);
					if (record.getR42_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR42_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 42
					// Column L
					cell11 = row.createCell(11);
					if (record.getR42_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR42_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 42
					// Column M
					cell12 = row.createCell(12);
					if (record.getR42_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR42_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 42
					// Column N
					cell13 = row.createCell(13);
					if (record.getR42_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR42_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(42);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR43_current() != null) {
						cell1.setCellValue(record.getR43_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_call() != null) {
						cell2.setCellValue(record.getR43_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_savings() != null) {
						cell3.setCellValue(record.getR43_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 43
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR43_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 43
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR43_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 43
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR43_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 43
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR43_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 43
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR43_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 43
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR43_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 43
					// Column K
					cell10 = row.createCell(10);
					if (record.getR43_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR43_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 43
					// Column L
					cell11 = row.createCell(11);
					if (record.getR43_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR43_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 43
					// Column M
					cell12 = row.createCell(12);
					if (record.getR43_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR43_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 43
					// Column N
					cell13 = row.createCell(13);
					if (record.getR43_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR43_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 44
					row = sheet.getRow(43);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR44_current() != null) {
						cell1.setCellValue(record.getR44_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_call() != null) {
						cell2.setCellValue(record.getR44_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 44
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_savings() != null) {
						cell3.setCellValue(record.getR44_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 44
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR44_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 44
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR44_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 44
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR44_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 44
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR44_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 44
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR44_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 44
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR44_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 44
					// Column K
					cell10 = row.createCell(10);
					if (record.getR44_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR44_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 44
					// Column L
					cell11 = row.createCell(11);
					if (record.getR44_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR44_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 44
					// Column M
					cell12 = row.createCell(12);
					if (record.getR44_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR44_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 44
					// Column N
					cell13 = row.createCell(13);
					if (record.getR44_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR44_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 46
					row = sheet.getRow(45);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR46_current() != null) {
						cell1.setCellValue(record.getR46_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_call() != null) {
						cell2.setCellValue(record.getR46_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 46
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_savings() != null) {
						cell3.setCellValue(record.getR46_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 46
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR46_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 46
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR46_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 46
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR46_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 46
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR46_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 46
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR46_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 46
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR46_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 46
					// Column K
					cell10 = row.createCell(10);
					if (record.getR46_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR46_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 46
					// Column L
					cell11 = row.createCell(11);
					if (record.getR46_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR46_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 46
					// Column M
					cell12 = row.createCell(12);
					if (record.getR46_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR46_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 46
					// Column N
					cell13 = row.createCell(13);
					if (record.getR46_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR46_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 47
					row = sheet.getRow(46);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR47_current() != null) {
						cell1.setCellValue(record.getR47_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_call() != null) {
						cell2.setCellValue(record.getR47_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_savings() != null) {
						cell3.setCellValue(record.getR47_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 47
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR47_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 47
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR47_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 47
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR47_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 47
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR47_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 47
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR47_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 47
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR47_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 47
					// Column K
					cell10 = row.createCell(10);
					if (record.getR47_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR47_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 47
					// Column L
					cell11 = row.createCell(11);
					if (record.getR47_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR47_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 47
					// Column M
					cell12 = row.createCell(12);
					if (record.getR47_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR47_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 47
					// Column N
					cell13 = row.createCell(13);
					if (record.getR47_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR47_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 48
					row = sheet.getRow(47);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR48_current() != null) {
						cell1.setCellValue(record.getR48_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_call() != null) {
						cell2.setCellValue(record.getR48_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_savings() != null) {
						cell3.setCellValue(record.getR48_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 48
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR48_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 48
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR48_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 48
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR48_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 48
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR48_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 48
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR48_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 48
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR48_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 48
					// Column K
					cell10 = row.createCell(10);
					if (record.getR48_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR48_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 48
					// Column L
					cell11 = row.createCell(11);
					if (record.getR48_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR48_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 48
					// Column M
					cell12 = row.createCell(12);
					if (record.getR48_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR48_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 48
					// Column N
					cell13 = row.createCell(13);
					if (record.getR48_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR48_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 50
					row = sheet.getRow(49);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR50_current() != null) {
						cell1.setCellValue(record.getR50_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_call() != null) {
						cell2.setCellValue(record.getR50_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 50
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_savings() != null) {
						cell3.setCellValue(record.getR50_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 50
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR50_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 50
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR50_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 50
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR50_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 50
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR50_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 50
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR50_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 50
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR50_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 50
					// Column K
					cell10 = row.createCell(10);
					if (record.getR50_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR50_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 50
					// Column L
					cell11 = row.createCell(11);
					if (record.getR50_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR50_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 50
					// Column M
					cell12 = row.createCell(12);
					if (record.getR50_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR50_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 50
					// Column N
					cell13 = row.createCell(13);
					if (record.getR50_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR50_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 51
					row = sheet.getRow(50);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR51_current() != null) {
						cell1.setCellValue(record.getR51_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_call() != null) {
						cell2.setCellValue(record.getR51_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_savings() != null) {
						cell3.setCellValue(record.getR51_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR51_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR51_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR51_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR51_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR51_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR51_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 51
					// Column K
					cell10 = row.createCell(10);
					if (record.getR51_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR51_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 51
					// Column L
					cell11 = row.createCell(11);
					if (record.getR51_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR51_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 51
					// Column M
					cell12 = row.createCell(12);
					if (record.getR51_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR51_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 51
					// Column N
					cell13 = row.createCell(13);
					if (record.getR51_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR51_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 52
					row = sheet.getRow(51);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR52_current() != null) {
						cell1.setCellValue(record.getR52_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_call() != null) {
						cell2.setCellValue(record.getR52_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_savings() != null) {
						cell3.setCellValue(record.getR52_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR52_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR52_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR52_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR52_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR52_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR52_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 52
					// Column K
					cell10 = row.createCell(10);
					if (record.getR52_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR52_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 52
					// Column L
					cell11 = row.createCell(11);
					if (record.getR52_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR52_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 52
					// Column M
					cell12 = row.createCell(12);
					if (record.getR52_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR52_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 52
					// Column N
					cell13 = row.createCell(13);
					if (record.getR52_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR52_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 53
					row = sheet.getRow(52);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR53_current() != null) {
						cell1.setCellValue(record.getR53_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_call() != null) {
						cell2.setCellValue(record.getR53_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_savings() != null) {
						cell3.setCellValue(record.getR53_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR53_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR53_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR53_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR53_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR53_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR53_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 53
					// Column K
					cell10 = row.createCell(10);
					if (record.getR53_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR53_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 53
					// Column L
					cell11 = row.createCell(11);
					if (record.getR53_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR53_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 53
					// Column M
					cell12 = row.createCell(12);
					if (record.getR53_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR53_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 53
					// Column N
					cell13 = row.createCell(13);
					if (record.getR53_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR53_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 54
					row = sheet.getRow(53);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR54_current() != null) {
						cell1.setCellValue(record.getR54_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_call() != null) {
						cell2.setCellValue(record.getR54_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_savings() != null) {
						cell3.setCellValue(record.getR54_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR54_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR54_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR54_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR54_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR54_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR54_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 54
					// Column K
					cell10 = row.createCell(10);
					if (record.getR54_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR54_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 54
					// Column L
					cell11 = row.createCell(11);
					if (record.getR54_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR54_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 54
					// Column M
					cell12 = row.createCell(12);
					if (record.getR54_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR54_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 54
					// Column N
					cell13 = row.createCell(13);
					if (record.getR54_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR54_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 55
					row = sheet.getRow(54);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR55_current() != null) {
						cell1.setCellValue(record.getR55_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_call() != null) {
						cell2.setCellValue(record.getR55_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_savings() != null) {
						cell3.setCellValue(record.getR55_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR55_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR55_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR55_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR55_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR55_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR55_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 55
					// Column K
					cell10 = row.createCell(10);
					if (record.getR55_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR55_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 55
					// Column L
					cell11 = row.createCell(11);
					if (record.getR55_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR55_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 55
					// Column M
					cell12 = row.createCell(12);
					if (record.getR55_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR55_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 55
					// Column N
					cell13 = row.createCell(13);
					if (record.getR55_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR55_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Row 57
					row = sheet.getRow(56);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR57_current() != null) {
						cell1.setCellValue(record.getR57_current().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Row 57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_call() != null) {
						cell2.setCellValue(record.getR57_call().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// Row 57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_savings() != null) {
						cell3.setCellValue(record.getR57_savings().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Row 57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_0_31_notice_days() != null) {
						cell4.setCellValue(record.getR57_0_31_notice_days().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					// Row 57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_32_88_notice_days() != null) {
						cell5.setCellValue(record.getR57_32_88_notice_days().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// Row 57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_91_day_deposit_fixed_deposit_months() != null) {
						cell6.setCellValue(record.getR57_91_day_deposit_fixed_deposit_months().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// Row 57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_1_2_fixed_deposits_months() != null) {
						cell7.setCellValue(record.getR57_1_2_fixed_deposits_months().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					// Row 57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_4_6_fixed_deposits_months() != null) {
						cell8.setCellValue(record.getR57_4_6_fixed_deposits_months().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					// Row 57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_7_12_fixed_deposits_months() != null) {
						cell9.setCellValue(record.getR57_7_12_fixed_deposits_months().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}
					// Row 57
					// Column K
					cell10 = row.createCell(10);
					if (record.getR57_13_18_fixed_deposits_months() != null) {
						cell10.setCellValue(record.getR57_13_18_fixed_deposits_months().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}
					// Row 57
					// Column L
					cell11 = row.createCell(11);
					if (record.getR57_19_24_fixed_deposits_months() != null) {
						cell11.setCellValue(record.getR57_19_24_fixed_deposits_months().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}
					// Row 57
					// Column M
					cell12 = row.createCell(12);
					if (record.getR57_over_24_fixed_deposits_months() != null) {
						cell12.setCellValue(record.getR57_over_24_fixed_deposits_months().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}
					// Row 57
					// Column N
					cell13 = row.createCell(13);
					if (record.getR57_certificates_of_deposit() != null) {
						cell13.setCellValue(record.getR57_certificates_of_deposit().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_DEP1 ARCHIVAL SUMMARY", null,
						"BRRS_M_DEP1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_DEP1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDEP1Detail");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
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

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT_LABEL",
					"REPORT_ADDL_CRITERIA_1", "REPORT_DATE" };

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

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_DEP1_Archival_Detail_Entity> reportData = getArchivalDetailByDate(parsedToDate, version);
			System.out.println("Size");
			System.out.println(reportData.size());
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_DEP1_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_DEP1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_DEP1Excel", e);
			return new byte[0];
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_DEP1"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_DEP1_Detail_Entity la1Entity = findDetailByAcctNumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_DEP1"); // ✅ match the report name

		if (acctNo != null) {
			M_DEP1_Detail_Entity la1Entity = findDetailByAcctNumber(acctNo);
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

			M_DEP1_Detail_Entity existing = findDetailByAcctNumber(acctNo);

			if (existing == null) {

				logger.warn("No record found for ACCT_NO: {}", acctNo);

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			// Create old copy for audit comparison
			M_DEP1_Detail_Entity oldcopy = new M_DEP1_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

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

				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {

					existing.setAcctBalanceInpula(newProvision);
					isChanged = true;

					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {

				saveDetailEntity(existing);

				// Audit comparison
				auditService.compareEntitiesmanual(oldcopy, existing, acctNo, "M DEP1 Detail Screen",
						"BRRS_M_DEP1_DETAIL");

				logger.info("Record updated successfully for account {}", acctNo);

				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

					@Override
					public void afterCommit() {

						try {

							logger.info("Transaction committed — calling BRRS_M_DEP1_SUMMARY_PROCEDURE({})",
									formattedDate);

							jdbcTemplate.update("BEGIN BRRS_M_DEP1_SUMMARY_PROCEDURE(?); END;", formattedDate);

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

			logger.error("Error updating M_DEP1 record", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// M_DEP1 entity classes

	public static class M_DEP1_Summary_Entity {

		private String r11_product;
		private BigDecimal r11_current;
		private BigDecimal r11_call;
		private BigDecimal r11_savings;
		private BigDecimal r11_0_31_notice_days;
		private BigDecimal r11_32_88_notice_days;
		private BigDecimal r11_91_day_deposit_fixed_deposit_months;
		private BigDecimal r11_1_2_fixed_deposits_months;
		private BigDecimal r11_4_6_fixed_deposits_months;
		private BigDecimal r11_7_12_fixed_deposits_months;
		private BigDecimal r11_13_18_fixed_deposits_months;
		private BigDecimal r11_19_24_fixed_deposits_months;
		private BigDecimal r11_over_24_fixed_deposits_months;
		private BigDecimal r11_certificates_of_deposit;
		private BigDecimal r11_total;

		private String r12_product;
		private BigDecimal r12_current;
		private BigDecimal r12_call;
		private BigDecimal r12_savings;
		private BigDecimal r12_0_31_notice_days;
		private BigDecimal r12_32_88_notice_days;
		private BigDecimal r12_91_day_deposit_fixed_deposit_months;
		private BigDecimal r12_1_2_fixed_deposits_months;
		private BigDecimal r12_4_6_fixed_deposits_months;
		private BigDecimal r12_7_12_fixed_deposits_months;
		private BigDecimal r12_13_18_fixed_deposits_months;
		private BigDecimal r12_19_24_fixed_deposits_months;
		private BigDecimal r12_over_24_fixed_deposits_months;
		private BigDecimal r12_certificates_of_deposit;
		private BigDecimal r12_total;

		private String r13_product;
		private BigDecimal r13_current;
		private BigDecimal r13_call;
		private BigDecimal r13_savings;
		private BigDecimal r13_0_31_notice_days;
		private BigDecimal r13_32_88_notice_days;
		private BigDecimal r13_91_day_deposit_fixed_deposit_months;
		private BigDecimal r13_1_2_fixed_deposits_months;
		private BigDecimal r13_4_6_fixed_deposits_months;
		private BigDecimal r13_7_12_fixed_deposits_months;
		private BigDecimal r13_13_18_fixed_deposits_months;
		private BigDecimal r13_19_24_fixed_deposits_months;
		private BigDecimal r13_over_24_fixed_deposits_months;
		private BigDecimal r13_certificates_of_deposit;
		private BigDecimal r13_total;

		private String r14_product;
		private BigDecimal r14_current;
		private BigDecimal r14_call;
		private BigDecimal r14_savings;
		private BigDecimal r14_0_31_notice_days;
		private BigDecimal r14_32_88_notice_days;
		private BigDecimal r14_91_day_deposit_fixed_deposit_months;
		private BigDecimal r14_1_2_fixed_deposits_months;
		private BigDecimal r14_4_6_fixed_deposits_months;
		private BigDecimal r14_7_12_fixed_deposits_months;
		private BigDecimal r14_13_18_fixed_deposits_months;
		private BigDecimal r14_19_24_fixed_deposits_months;
		private BigDecimal r14_over_24_fixed_deposits_months;
		private BigDecimal r14_certificates_of_deposit;
		private BigDecimal r14_total;

		private String r15_product;
		private BigDecimal r15_current;
		private BigDecimal r15_call;
		private BigDecimal r15_savings;
		private BigDecimal r15_0_31_notice_days;
		private BigDecimal r15_32_88_notice_days;
		private BigDecimal r15_91_day_deposit_fixed_deposit_months;
		private BigDecimal r15_1_2_fixed_deposits_months;
		private BigDecimal r15_4_6_fixed_deposits_months;
		private BigDecimal r15_7_12_fixed_deposits_months;
		private BigDecimal r15_13_18_fixed_deposits_months;
		private BigDecimal r15_19_24_fixed_deposits_months;
		private BigDecimal r15_over_24_fixed_deposits_months;
		private BigDecimal r15_certificates_of_deposit;
		private BigDecimal r15_total;

		private String r16_product;
		private BigDecimal r16_current;
		private BigDecimal r16_call;
		private BigDecimal r16_savings;
		private BigDecimal r16_0_31_notice_days;
		private BigDecimal r16_32_88_notice_days;
		private BigDecimal r16_91_day_deposit_fixed_deposit_months;
		private BigDecimal r16_1_2_fixed_deposits_months;
		private BigDecimal r16_4_6_fixed_deposits_months;
		private BigDecimal r16_7_12_fixed_deposits_months;
		private BigDecimal r16_13_18_fixed_deposits_months;
		private BigDecimal r16_19_24_fixed_deposits_months;
		private BigDecimal r16_over_24_fixed_deposits_months;
		private BigDecimal r16_certificates_of_deposit;
		private BigDecimal r16_total;

		private String r17_product;
		private BigDecimal r17_current;
		private BigDecimal r17_call;
		private BigDecimal r17_savings;
		private BigDecimal r17_0_31_notice_days;
		private BigDecimal r17_32_88_notice_days;
		private BigDecimal r17_91_day_deposit_fixed_deposit_months;
		private BigDecimal r17_1_2_fixed_deposits_months;
		private BigDecimal r17_4_6_fixed_deposits_months;
		private BigDecimal r17_7_12_fixed_deposits_months;
		private BigDecimal r17_13_18_fixed_deposits_months;
		private BigDecimal r17_19_24_fixed_deposits_months;
		private BigDecimal r17_over_24_fixed_deposits_months;
		private BigDecimal r17_certificates_of_deposit;
		private BigDecimal r17_total;

		private String r18_product;
		private BigDecimal r18_current;
		private BigDecimal r18_call;
		private BigDecimal r18_savings;
		private BigDecimal r18_0_31_notice_days;
		private BigDecimal r18_32_88_notice_days;
		private BigDecimal r18_91_day_deposit_fixed_deposit_months;
		private BigDecimal r18_1_2_fixed_deposits_months;
		private BigDecimal r18_4_6_fixed_deposits_months;
		private BigDecimal r18_7_12_fixed_deposits_months;
		private BigDecimal r18_13_18_fixed_deposits_months;
		private BigDecimal r18_19_24_fixed_deposits_months;
		private BigDecimal r18_over_24_fixed_deposits_months;
		private BigDecimal r18_certificates_of_deposit;
		private BigDecimal r18_total;

		private String r19_product;
		private BigDecimal r19_current;
		private BigDecimal r19_call;
		private BigDecimal r19_savings;
		private BigDecimal r19_0_31_notice_days;
		private BigDecimal r19_32_88_notice_days;
		private BigDecimal r19_91_day_deposit_fixed_deposit_months;
		private BigDecimal r19_1_2_fixed_deposits_months;
		private BigDecimal r19_4_6_fixed_deposits_months;
		private BigDecimal r19_7_12_fixed_deposits_months;
		private BigDecimal r19_13_18_fixed_deposits_months;
		private BigDecimal r19_19_24_fixed_deposits_months;
		private BigDecimal r19_over_24_fixed_deposits_months;
		private BigDecimal r19_certificates_of_deposit;
		private BigDecimal r19_total;

		private String r20_product;
		private BigDecimal r20_current;
		private BigDecimal r20_call;
		private BigDecimal r20_savings;
		private BigDecimal r20_0_31_notice_days;
		private BigDecimal r20_32_88_notice_days;
		private BigDecimal r20_91_day_deposit_fixed_deposit_months;
		private BigDecimal r20_1_2_fixed_deposits_months;
		private BigDecimal r20_4_6_fixed_deposits_months;
		private BigDecimal r20_7_12_fixed_deposits_months;
		private BigDecimal r20_13_18_fixed_deposits_months;
		private BigDecimal r20_19_24_fixed_deposits_months;
		private BigDecimal r20_over_24_fixed_deposits_months;
		private BigDecimal r20_certificates_of_deposit;
		private BigDecimal r20_total;

		private String r21_product;
		private BigDecimal r21_current;
		private BigDecimal r21_call;
		private BigDecimal r21_savings;
		private BigDecimal r21_0_31_notice_days;
		private BigDecimal r21_32_88_notice_days;
		private BigDecimal r21_91_day_deposit_fixed_deposit_months;
		private BigDecimal r21_1_2_fixed_deposits_months;
		private BigDecimal r21_4_6_fixed_deposits_months;
		private BigDecimal r21_7_12_fixed_deposits_months;
		private BigDecimal r21_13_18_fixed_deposits_months;
		private BigDecimal r21_19_24_fixed_deposits_months;
		private BigDecimal r21_over_24_fixed_deposits_months;
		private BigDecimal r21_certificates_of_deposit;
		private BigDecimal r21_total;

		private String r22_product;
		private BigDecimal r22_current;
		private BigDecimal r22_call;
		private BigDecimal r22_savings;
		private BigDecimal r22_0_31_notice_days;
		private BigDecimal r22_32_88_notice_days;
		private BigDecimal r22_91_day_deposit_fixed_deposit_months;
		private BigDecimal r22_1_2_fixed_deposits_months;
		private BigDecimal r22_4_6_fixed_deposits_months;
		private BigDecimal r22_7_12_fixed_deposits_months;
		private BigDecimal r22_13_18_fixed_deposits_months;
		private BigDecimal r22_19_24_fixed_deposits_months;
		private BigDecimal r22_over_24_fixed_deposits_months;
		private BigDecimal r22_certificates_of_deposit;
		private BigDecimal r22_total;

		private String r23_product;
		private BigDecimal r23_current;
		private BigDecimal r23_call;
		private BigDecimal r23_savings;
		private BigDecimal r23_0_31_notice_days;
		private BigDecimal r23_32_88_notice_days;
		private BigDecimal r23_91_day_deposit_fixed_deposit_months;
		private BigDecimal r23_1_2_fixed_deposits_months;
		private BigDecimal r23_4_6_fixed_deposits_months;
		private BigDecimal r23_7_12_fixed_deposits_months;
		private BigDecimal r23_13_18_fixed_deposits_months;
		private BigDecimal r23_19_24_fixed_deposits_months;
		private BigDecimal r23_over_24_fixed_deposits_months;
		private BigDecimal r23_certificates_of_deposit;
		private BigDecimal r23_total;

		private String r24_product;
		private BigDecimal r24_current;
		private BigDecimal r24_call;
		private BigDecimal r24_savings;
		private BigDecimal r24_0_31_notice_days;
		private BigDecimal r24_32_88_notice_days;
		private BigDecimal r24_91_day_deposit_fixed_deposit_months;
		private BigDecimal r24_1_2_fixed_deposits_months;
		private BigDecimal r24_4_6_fixed_deposits_months;
		private BigDecimal r24_7_12_fixed_deposits_months;
		private BigDecimal r24_13_18_fixed_deposits_months;
		private BigDecimal r24_19_24_fixed_deposits_months;
		private BigDecimal r24_over_24_fixed_deposits_months;
		private BigDecimal r24_certificates_of_deposit;
		private BigDecimal r24_total;

		private String r25_product;
		private BigDecimal r25_current;
		private BigDecimal r25_call;
		private BigDecimal r25_savings;
		private BigDecimal r25_0_31_notice_days;
		private BigDecimal r25_32_88_notice_days;
		private BigDecimal r25_91_day_deposit_fixed_deposit_months;
		private BigDecimal r25_1_2_fixed_deposits_months;
		private BigDecimal r25_4_6_fixed_deposits_months;
		private BigDecimal r25_7_12_fixed_deposits_months;
		private BigDecimal r25_13_18_fixed_deposits_months;
		private BigDecimal r25_19_24_fixed_deposits_months;
		private BigDecimal r25_over_24_fixed_deposits_months;
		private BigDecimal r25_certificates_of_deposit;
		private BigDecimal r25_total;

		private String r26_product;
		private BigDecimal r26_current;
		private BigDecimal r26_call;
		private BigDecimal r26_savings;
		private BigDecimal r26_0_31_notice_days;
		private BigDecimal r26_32_88_notice_days;
		private BigDecimal r26_91_day_deposit_fixed_deposit_months;
		private BigDecimal r26_1_2_fixed_deposits_months;
		private BigDecimal r26_4_6_fixed_deposits_months;
		private BigDecimal r26_7_12_fixed_deposits_months;
		private BigDecimal r26_13_18_fixed_deposits_months;
		private BigDecimal r26_19_24_fixed_deposits_months;
		private BigDecimal r26_over_24_fixed_deposits_months;
		private BigDecimal r26_certificates_of_deposit;
		private BigDecimal r26_total;

		private String r27_product;
		private BigDecimal r27_current;
		private BigDecimal r27_call;
		private BigDecimal r27_savings;
		private BigDecimal r27_0_31_notice_days;
		private BigDecimal r27_32_88_notice_days;
		private BigDecimal r27_91_day_deposit_fixed_deposit_months;
		private BigDecimal r27_1_2_fixed_deposits_months;
		private BigDecimal r27_4_6_fixed_deposits_months;
		private BigDecimal r27_7_12_fixed_deposits_months;
		private BigDecimal r27_13_18_fixed_deposits_months;
		private BigDecimal r27_19_24_fixed_deposits_months;
		private BigDecimal r27_over_24_fixed_deposits_months;
		private BigDecimal r27_certificates_of_deposit;
		private BigDecimal r27_total;

		private String r28_product;
		private BigDecimal r28_current;
		private BigDecimal r28_call;
		private BigDecimal r28_savings;
		private BigDecimal r28_0_31_notice_days;
		private BigDecimal r28_32_88_notice_days;
		private BigDecimal r28_91_day_deposit_fixed_deposit_months;
		private BigDecimal r28_1_2_fixed_deposits_months;
		private BigDecimal r28_4_6_fixed_deposits_months;
		private BigDecimal r28_7_12_fixed_deposits_months;
		private BigDecimal r28_13_18_fixed_deposits_months;
		private BigDecimal r28_19_24_fixed_deposits_months;
		private BigDecimal r28_over_24_fixed_deposits_months;
		private BigDecimal r28_certificates_of_deposit;
		private BigDecimal r28_total;

		private String r29_product;
		private BigDecimal r29_current;
		private BigDecimal r29_call;
		private BigDecimal r29_savings;
		private BigDecimal r29_0_31_notice_days;
		private BigDecimal r29_32_88_notice_days;
		private BigDecimal r29_91_day_deposit_fixed_deposit_months;
		private BigDecimal r29_1_2_fixed_deposits_months;
		private BigDecimal r29_4_6_fixed_deposits_months;
		private BigDecimal r29_7_12_fixed_deposits_months;
		private BigDecimal r29_13_18_fixed_deposits_months;
		private BigDecimal r29_19_24_fixed_deposits_months;
		private BigDecimal r29_over_24_fixed_deposits_months;
		private BigDecimal r29_certificates_of_deposit;
		private BigDecimal r29_total;

		private String r30_product;
		private BigDecimal r30_current;
		private BigDecimal r30_call;
		private BigDecimal r30_savings;
		private BigDecimal r30_0_31_notice_days;
		private BigDecimal r30_32_88_notice_days;
		private BigDecimal r30_91_day_deposit_fixed_deposit_months;
		private BigDecimal r30_1_2_fixed_deposits_months;
		private BigDecimal r30_4_6_fixed_deposits_months;
		private BigDecimal r30_7_12_fixed_deposits_months;
		private BigDecimal r30_13_18_fixed_deposits_months;
		private BigDecimal r30_19_24_fixed_deposits_months;
		private BigDecimal r30_over_24_fixed_deposits_months;
		private BigDecimal r30_certificates_of_deposit;
		private BigDecimal r30_total;

		private String r31_product;
		private BigDecimal r31_current;
		private BigDecimal r31_call;
		private BigDecimal r31_savings;
		private BigDecimal r31_0_31_notice_days;
		private BigDecimal r31_32_88_notice_days;
		private BigDecimal r31_91_day_deposit_fixed_deposit_months;
		private BigDecimal r31_1_2_fixed_deposits_months;
		private BigDecimal r31_4_6_fixed_deposits_months;
		private BigDecimal r31_7_12_fixed_deposits_months;
		private BigDecimal r31_13_18_fixed_deposits_months;
		private BigDecimal r31_19_24_fixed_deposits_months;
		private BigDecimal r31_over_24_fixed_deposits_months;
		private BigDecimal r31_certificates_of_deposit;
		private BigDecimal r31_total;

		private String r32_product;
		private BigDecimal r32_current;
		private BigDecimal r32_call;
		private BigDecimal r32_savings;
		private BigDecimal r32_0_31_notice_days;
		private BigDecimal r32_32_88_notice_days;
		private BigDecimal r32_91_day_deposit_fixed_deposit_months;
		private BigDecimal r32_1_2_fixed_deposits_months;
		private BigDecimal r32_4_6_fixed_deposits_months;
		private BigDecimal r32_7_12_fixed_deposits_months;
		private BigDecimal r32_13_18_fixed_deposits_months;
		private BigDecimal r32_19_24_fixed_deposits_months;
		private BigDecimal r32_over_24_fixed_deposits_months;
		private BigDecimal r32_certificates_of_deposit;
		private BigDecimal r32_total;

		private String r33_product;
		private BigDecimal r33_current;
		private BigDecimal r33_call;
		private BigDecimal r33_savings;
		private BigDecimal r33_0_31_notice_days;
		private BigDecimal r33_32_88_notice_days;
		private BigDecimal r33_91_day_deposit_fixed_deposit_months;
		private BigDecimal r33_1_2_fixed_deposits_months;
		private BigDecimal r33_4_6_fixed_deposits_months;
		private BigDecimal r33_7_12_fixed_deposits_months;
		private BigDecimal r33_13_18_fixed_deposits_months;
		private BigDecimal r33_19_24_fixed_deposits_months;
		private BigDecimal r33_over_24_fixed_deposits_months;
		private BigDecimal r33_certificates_of_deposit;
		private BigDecimal r33_total;

		private String r34_product;
		private BigDecimal r34_current;
		private BigDecimal r34_call;
		private BigDecimal r34_savings;
		private BigDecimal r34_0_31_notice_days;
		private BigDecimal r34_32_88_notice_days;
		private BigDecimal r34_91_day_deposit_fixed_deposit_months;
		private BigDecimal r34_1_2_fixed_deposits_months;
		private BigDecimal r34_4_6_fixed_deposits_months;
		private BigDecimal r34_7_12_fixed_deposits_months;
		private BigDecimal r34_13_18_fixed_deposits_months;
		private BigDecimal r34_19_24_fixed_deposits_months;
		private BigDecimal r34_over_24_fixed_deposits_months;
		private BigDecimal r34_certificates_of_deposit;
		private BigDecimal r34_total;

		private String r35_product;
		private BigDecimal r35_current;
		private BigDecimal r35_call;
		private BigDecimal r35_savings;
		private BigDecimal r35_0_31_notice_days;
		private BigDecimal r35_32_88_notice_days;
		private BigDecimal r35_91_day_deposit_fixed_deposit_months;
		private BigDecimal r35_1_2_fixed_deposits_months;
		private BigDecimal r35_4_6_fixed_deposits_months;
		private BigDecimal r35_7_12_fixed_deposits_months;
		private BigDecimal r35_13_18_fixed_deposits_months;
		private BigDecimal r35_19_24_fixed_deposits_months;
		private BigDecimal r35_over_24_fixed_deposits_months;
		private BigDecimal r35_certificates_of_deposit;
		private BigDecimal r35_total;

		private String r36_product;
		private BigDecimal r36_current;
		private BigDecimal r36_call;
		private BigDecimal r36_savings;
		private BigDecimal r36_0_31_notice_days;
		private BigDecimal r36_32_88_notice_days;
		private BigDecimal r36_91_day_deposit_fixed_deposit_months;
		private BigDecimal r36_1_2_fixed_deposits_months;
		private BigDecimal r36_4_6_fixed_deposits_months;
		private BigDecimal r36_7_12_fixed_deposits_months;
		private BigDecimal r36_13_18_fixed_deposits_months;
		private BigDecimal r36_19_24_fixed_deposits_months;
		private BigDecimal r36_over_24_fixed_deposits_months;
		private BigDecimal r36_certificates_of_deposit;
		private BigDecimal r36_total;

		private String r37_product;
		private BigDecimal r37_current;
		private BigDecimal r37_call;
		private BigDecimal r37_savings;
		private BigDecimal r37_0_31_notice_days;
		private BigDecimal r37_32_88_notice_days;
		private BigDecimal r37_91_day_deposit_fixed_deposit_months;
		private BigDecimal r37_1_2_fixed_deposits_months;
		private BigDecimal r37_4_6_fixed_deposits_months;
		private BigDecimal r37_7_12_fixed_deposits_months;
		private BigDecimal r37_13_18_fixed_deposits_months;
		private BigDecimal r37_19_24_fixed_deposits_months;
		private BigDecimal r37_over_24_fixed_deposits_months;
		private BigDecimal r37_certificates_of_deposit;
		private BigDecimal r37_total;

		private String r38_product;
		private BigDecimal r38_current;
		private BigDecimal r38_call;
		private BigDecimal r38_savings;
		private BigDecimal r38_0_31_notice_days;
		private BigDecimal r38_32_88_notice_days;
		private BigDecimal r38_91_day_deposit_fixed_deposit_months;
		private BigDecimal r38_1_2_fixed_deposits_months;
		private BigDecimal r38_4_6_fixed_deposits_months;
		private BigDecimal r38_7_12_fixed_deposits_months;
		private BigDecimal r38_13_18_fixed_deposits_months;
		private BigDecimal r38_19_24_fixed_deposits_months;
		private BigDecimal r38_over_24_fixed_deposits_months;
		private BigDecimal r38_certificates_of_deposit;
		private BigDecimal r38_total;

		private String r39_product;
		private BigDecimal r39_current;
		private BigDecimal r39_call;
		private BigDecimal r39_savings;
		private BigDecimal r39_0_31_notice_days;
		private BigDecimal r39_32_88_notice_days;
		private BigDecimal r39_91_day_deposit_fixed_deposit_months;
		private BigDecimal r39_1_2_fixed_deposits_months;
		private BigDecimal r39_4_6_fixed_deposits_months;
		private BigDecimal r39_7_12_fixed_deposits_months;
		private BigDecimal r39_13_18_fixed_deposits_months;
		private BigDecimal r39_19_24_fixed_deposits_months;
		private BigDecimal r39_over_24_fixed_deposits_months;
		private BigDecimal r39_certificates_of_deposit;
		private BigDecimal r39_total;

		private String r40_product;
		private BigDecimal r40_current;
		private BigDecimal r40_call;
		private BigDecimal r40_savings;
		private BigDecimal r40_0_31_notice_days;
		private BigDecimal r40_32_88_notice_days;
		private BigDecimal r40_91_day_deposit_fixed_deposit_months;
		private BigDecimal r40_1_2_fixed_deposits_months;
		private BigDecimal r40_4_6_fixed_deposits_months;
		private BigDecimal r40_7_12_fixed_deposits_months;
		private BigDecimal r40_13_18_fixed_deposits_months;
		private BigDecimal r40_19_24_fixed_deposits_months;
		private BigDecimal r40_over_24_fixed_deposits_months;
		private BigDecimal r40_certificates_of_deposit;
		private BigDecimal r40_total;

		private String r41_product;
		private BigDecimal r41_current;
		private BigDecimal r41_call;
		private BigDecimal r41_savings;
		private BigDecimal r41_0_31_notice_days;
		private BigDecimal r41_32_88_notice_days;
		private BigDecimal r41_91_day_deposit_fixed_deposit_months;
		private BigDecimal r41_1_2_fixed_deposits_months;
		private BigDecimal r41_4_6_fixed_deposits_months;
		private BigDecimal r41_7_12_fixed_deposits_months;
		private BigDecimal r41_13_18_fixed_deposits_months;
		private BigDecimal r41_19_24_fixed_deposits_months;
		private BigDecimal r41_over_24_fixed_deposits_months;
		private BigDecimal r41_certificates_of_deposit;
		private BigDecimal r41_total;

		private String r42_product;
		private BigDecimal r42_current;
		private BigDecimal r42_call;
		private BigDecimal r42_savings;
		private BigDecimal r42_0_31_notice_days;
		private BigDecimal r42_32_88_notice_days;
		private BigDecimal r42_91_day_deposit_fixed_deposit_months;
		private BigDecimal r42_1_2_fixed_deposits_months;
		private BigDecimal r42_4_6_fixed_deposits_months;
		private BigDecimal r42_7_12_fixed_deposits_months;
		private BigDecimal r42_13_18_fixed_deposits_months;
		private BigDecimal r42_19_24_fixed_deposits_months;
		private BigDecimal r42_over_24_fixed_deposits_months;
		private BigDecimal r42_certificates_of_deposit;
		private BigDecimal r42_total;

		private String r43_product;
		private BigDecimal r43_current;
		private BigDecimal r43_call;
		private BigDecimal r43_savings;
		private BigDecimal r43_0_31_notice_days;
		private BigDecimal r43_32_88_notice_days;
		private BigDecimal r43_91_day_deposit_fixed_deposit_months;
		private BigDecimal r43_1_2_fixed_deposits_months;
		private BigDecimal r43_4_6_fixed_deposits_months;
		private BigDecimal r43_7_12_fixed_deposits_months;
		private BigDecimal r43_13_18_fixed_deposits_months;
		private BigDecimal r43_19_24_fixed_deposits_months;
		private BigDecimal r43_over_24_fixed_deposits_months;
		private BigDecimal r43_certificates_of_deposit;
		private BigDecimal r43_total;

		private String r44_product;
		private BigDecimal r44_current;
		private BigDecimal r44_call;
		private BigDecimal r44_savings;
		private BigDecimal r44_0_31_notice_days;
		private BigDecimal r44_32_88_notice_days;
		private BigDecimal r44_91_day_deposit_fixed_deposit_months;
		private BigDecimal r44_1_2_fixed_deposits_months;
		private BigDecimal r44_4_6_fixed_deposits_months;
		private BigDecimal r44_7_12_fixed_deposits_months;
		private BigDecimal r44_13_18_fixed_deposits_months;
		private BigDecimal r44_19_24_fixed_deposits_months;
		private BigDecimal r44_over_24_fixed_deposits_months;
		private BigDecimal r44_certificates_of_deposit;
		private BigDecimal r44_total;

		private String r45_product;
		private BigDecimal r45_current;
		private BigDecimal r45_call;
		private BigDecimal r45_savings;
		private BigDecimal r45_0_31_notice_days;
		private BigDecimal r45_32_88_notice_days;
		private BigDecimal r45_91_day_deposit_fixed_deposit_months;
		private BigDecimal r45_1_2_fixed_deposits_months;
		private BigDecimal r45_4_6_fixed_deposits_months;
		private BigDecimal r45_7_12_fixed_deposits_months;
		private BigDecimal r45_13_18_fixed_deposits_months;
		private BigDecimal r45_19_24_fixed_deposits_months;
		private BigDecimal r45_over_24_fixed_deposits_months;
		private BigDecimal r45_certificates_of_deposit;
		private BigDecimal r45_total;

		private String r46_product;
		private BigDecimal r46_current;
		private BigDecimal r46_call;
		private BigDecimal r46_savings;
		private BigDecimal r46_0_31_notice_days;
		private BigDecimal r46_32_88_notice_days;
		private BigDecimal r46_91_day_deposit_fixed_deposit_months;
		private BigDecimal r46_1_2_fixed_deposits_months;
		private BigDecimal r46_4_6_fixed_deposits_months;
		private BigDecimal r46_7_12_fixed_deposits_months;
		private BigDecimal r46_13_18_fixed_deposits_months;
		private BigDecimal r46_19_24_fixed_deposits_months;
		private BigDecimal r46_over_24_fixed_deposits_months;
		private BigDecimal r46_certificates_of_deposit;
		private BigDecimal r46_total;

		private String r47_product;
		private BigDecimal r47_current;
		private BigDecimal r47_call;
		private BigDecimal r47_savings;
		private BigDecimal r47_0_31_notice_days;
		private BigDecimal r47_32_88_notice_days;
		private BigDecimal r47_91_day_deposit_fixed_deposit_months;
		private BigDecimal r47_1_2_fixed_deposits_months;
		private BigDecimal r47_4_6_fixed_deposits_months;
		private BigDecimal r47_7_12_fixed_deposits_months;
		private BigDecimal r47_13_18_fixed_deposits_months;
		private BigDecimal r47_19_24_fixed_deposits_months;
		private BigDecimal r47_over_24_fixed_deposits_months;
		private BigDecimal r47_certificates_of_deposit;
		private BigDecimal r47_total;

		private String r48_product;
		private BigDecimal r48_current;
		private BigDecimal r48_call;
		private BigDecimal r48_savings;
		private BigDecimal r48_0_31_notice_days;
		private BigDecimal r48_32_88_notice_days;
		private BigDecimal r48_91_day_deposit_fixed_deposit_months;
		private BigDecimal r48_1_2_fixed_deposits_months;
		private BigDecimal r48_4_6_fixed_deposits_months;
		private BigDecimal r48_7_12_fixed_deposits_months;
		private BigDecimal r48_13_18_fixed_deposits_months;
		private BigDecimal r48_19_24_fixed_deposits_months;
		private BigDecimal r48_over_24_fixed_deposits_months;
		private BigDecimal r48_certificates_of_deposit;
		private BigDecimal r48_total;

		private String r49_product;
		private BigDecimal r49_current;
		private BigDecimal r49_call;
		private BigDecimal r49_savings;
		private BigDecimal r49_0_31_notice_days;
		private BigDecimal r49_32_88_notice_days;
		private BigDecimal r49_91_day_deposit_fixed_deposit_months;
		private BigDecimal r49_1_2_fixed_deposits_months;
		private BigDecimal r49_4_6_fixed_deposits_months;
		private BigDecimal r49_7_12_fixed_deposits_months;
		private BigDecimal r49_13_18_fixed_deposits_months;
		private BigDecimal r49_19_24_fixed_deposits_months;
		private BigDecimal r49_over_24_fixed_deposits_months;
		private BigDecimal r49_certificates_of_deposit;
		private BigDecimal r49_total;

		private String r50_product;
		private BigDecimal r50_current;
		private BigDecimal r50_call;
		private BigDecimal r50_savings;
		private BigDecimal r50_0_31_notice_days;
		private BigDecimal r50_32_88_notice_days;
		private BigDecimal r50_91_day_deposit_fixed_deposit_months;
		private BigDecimal r50_1_2_fixed_deposits_months;
		private BigDecimal r50_4_6_fixed_deposits_months;
		private BigDecimal r50_7_12_fixed_deposits_months;
		private BigDecimal r50_13_18_fixed_deposits_months;
		private BigDecimal r50_19_24_fixed_deposits_months;
		private BigDecimal r50_over_24_fixed_deposits_months;
		private BigDecimal r50_certificates_of_deposit;
		private BigDecimal r50_total;

		private String r51_product;
		private BigDecimal r51_current;
		private BigDecimal r51_call;
		private BigDecimal r51_savings;
		private BigDecimal r51_0_31_notice_days;
		private BigDecimal r51_32_88_notice_days;
		private BigDecimal r51_91_day_deposit_fixed_deposit_months;
		private BigDecimal r51_1_2_fixed_deposits_months;
		private BigDecimal r51_4_6_fixed_deposits_months;
		private BigDecimal r51_7_12_fixed_deposits_months;
		private BigDecimal r51_13_18_fixed_deposits_months;
		private BigDecimal r51_19_24_fixed_deposits_months;
		private BigDecimal r51_over_24_fixed_deposits_months;
		private BigDecimal r51_certificates_of_deposit;
		private BigDecimal r51_total;

		private String r52_product;
		private BigDecimal r52_current;
		private BigDecimal r52_call;
		private BigDecimal r52_savings;
		private BigDecimal r52_0_31_notice_days;
		private BigDecimal r52_32_88_notice_days;
		private BigDecimal r52_91_day_deposit_fixed_deposit_months;
		private BigDecimal r52_1_2_fixed_deposits_months;
		private BigDecimal r52_4_6_fixed_deposits_months;
		private BigDecimal r52_7_12_fixed_deposits_months;
		private BigDecimal r52_13_18_fixed_deposits_months;
		private BigDecimal r52_19_24_fixed_deposits_months;
		private BigDecimal r52_over_24_fixed_deposits_months;
		private BigDecimal r52_certificates_of_deposit;
		private BigDecimal r52_total;

		private String r53_product;
		private BigDecimal r53_current;
		private BigDecimal r53_call;
		private BigDecimal r53_savings;
		private BigDecimal r53_0_31_notice_days;
		private BigDecimal r53_32_88_notice_days;
		private BigDecimal r53_91_day_deposit_fixed_deposit_months;
		private BigDecimal r53_1_2_fixed_deposits_months;
		private BigDecimal r53_4_6_fixed_deposits_months;
		private BigDecimal r53_7_12_fixed_deposits_months;
		private BigDecimal r53_13_18_fixed_deposits_months;
		private BigDecimal r53_19_24_fixed_deposits_months;
		private BigDecimal r53_over_24_fixed_deposits_months;
		private BigDecimal r53_certificates_of_deposit;
		private BigDecimal r53_total;

		private String r54_product;
		private BigDecimal r54_current;
		private BigDecimal r54_call;
		private BigDecimal r54_savings;
		private BigDecimal r54_0_31_notice_days;
		private BigDecimal r54_32_88_notice_days;
		private BigDecimal r54_91_day_deposit_fixed_deposit_months;
		private BigDecimal r54_1_2_fixed_deposits_months;
		private BigDecimal r54_4_6_fixed_deposits_months;
		private BigDecimal r54_7_12_fixed_deposits_months;
		private BigDecimal r54_13_18_fixed_deposits_months;
		private BigDecimal r54_19_24_fixed_deposits_months;
		private BigDecimal r54_over_24_fixed_deposits_months;
		private BigDecimal r54_certificates_of_deposit;
		private BigDecimal r54_total;

		private String r55_product;
		private BigDecimal r55_current;
		private BigDecimal r55_call;
		private BigDecimal r55_savings;
		private BigDecimal r55_0_31_notice_days;
		private BigDecimal r55_32_88_notice_days;
		private BigDecimal r55_91_day_deposit_fixed_deposit_months;
		private BigDecimal r55_1_2_fixed_deposits_months;
		private BigDecimal r55_4_6_fixed_deposits_months;
		private BigDecimal r55_7_12_fixed_deposits_months;
		private BigDecimal r55_13_18_fixed_deposits_months;
		private BigDecimal r55_19_24_fixed_deposits_months;
		private BigDecimal r55_over_24_fixed_deposits_months;
		private BigDecimal r55_certificates_of_deposit;
		private BigDecimal r55_total;

		private String r56_product;
		private BigDecimal r56_current;
		private BigDecimal r56_call;
		private BigDecimal r56_savings;
		private BigDecimal r56_0_31_notice_days;
		private BigDecimal r56_32_88_notice_days;
		private BigDecimal r56_91_day_deposit_fixed_deposit_months;
		private BigDecimal r56_1_2_fixed_deposits_months;
		private BigDecimal r56_4_6_fixed_deposits_months;
		private BigDecimal r56_7_12_fixed_deposits_months;
		private BigDecimal r56_13_18_fixed_deposits_months;
		private BigDecimal r56_19_24_fixed_deposits_months;
		private BigDecimal r56_over_24_fixed_deposits_months;
		private BigDecimal r56_certificates_of_deposit;
		private BigDecimal r56_total;

		private String r57_product;
		private BigDecimal r57_current;
		private BigDecimal r57_call;
		private BigDecimal r57_savings;
		private BigDecimal r57_0_31_notice_days;
		private BigDecimal r57_32_88_notice_days;
		private BigDecimal r57_91_day_deposit_fixed_deposit_months;
		private BigDecimal r57_1_2_fixed_deposits_months;
		private BigDecimal r57_4_6_fixed_deposits_months;
		private BigDecimal r57_7_12_fixed_deposits_months;
		private BigDecimal r57_13_18_fixed_deposits_months;
		private BigDecimal r57_19_24_fixed_deposits_months;
		private BigDecimal r57_over_24_fixed_deposits_months;
		private BigDecimal r57_certificates_of_deposit;
		private BigDecimal r57_total;

		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_current() {
			return r11_current;
		}

		public void setR11_current(BigDecimal r11_current) {
			this.r11_current = r11_current;
		}

		public BigDecimal getR11_call() {
			return r11_call;
		}

		public void setR11_call(BigDecimal r11_call) {
			this.r11_call = r11_call;
		}

		public BigDecimal getR11_savings() {
			return r11_savings;
		}

		public void setR11_savings(BigDecimal r11_savings) {
			this.r11_savings = r11_savings;
		}

		public BigDecimal getR11_0_31_notice_days() {
			return r11_0_31_notice_days;
		}

		public void setR11_0_31_notice_days(BigDecimal r11_0_31_notice_days) {
			this.r11_0_31_notice_days = r11_0_31_notice_days;
		}

		public BigDecimal getR11_32_88_notice_days() {
			return r11_32_88_notice_days;
		}

		public void setR11_32_88_notice_days(BigDecimal r11_32_88_notice_days) {
			this.r11_32_88_notice_days = r11_32_88_notice_days;
		}

		public BigDecimal getR11_91_day_deposit_fixed_deposit_months() {
			return r11_91_day_deposit_fixed_deposit_months;
		}

		public void setR11_91_day_deposit_fixed_deposit_months(BigDecimal r11_91_day_deposit_fixed_deposit_months) {
			this.r11_91_day_deposit_fixed_deposit_months = r11_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR11_1_2_fixed_deposits_months() {
			return r11_1_2_fixed_deposits_months;
		}

		public void setR11_1_2_fixed_deposits_months(BigDecimal r11_1_2_fixed_deposits_months) {
			this.r11_1_2_fixed_deposits_months = r11_1_2_fixed_deposits_months;
		}

		public BigDecimal getR11_4_6_fixed_deposits_months() {
			return r11_4_6_fixed_deposits_months;
		}

		public void setR11_4_6_fixed_deposits_months(BigDecimal r11_4_6_fixed_deposits_months) {
			this.r11_4_6_fixed_deposits_months = r11_4_6_fixed_deposits_months;
		}

		public BigDecimal getR11_7_12_fixed_deposits_months() {
			return r11_7_12_fixed_deposits_months;
		}

		public void setR11_7_12_fixed_deposits_months(BigDecimal r11_7_12_fixed_deposits_months) {
			this.r11_7_12_fixed_deposits_months = r11_7_12_fixed_deposits_months;
		}

		public BigDecimal getR11_13_18_fixed_deposits_months() {
			return r11_13_18_fixed_deposits_months;
		}

		public void setR11_13_18_fixed_deposits_months(BigDecimal r11_13_18_fixed_deposits_months) {
			this.r11_13_18_fixed_deposits_months = r11_13_18_fixed_deposits_months;
		}

		public BigDecimal getR11_19_24_fixed_deposits_months() {
			return r11_19_24_fixed_deposits_months;
		}

		public void setR11_19_24_fixed_deposits_months(BigDecimal r11_19_24_fixed_deposits_months) {
			this.r11_19_24_fixed_deposits_months = r11_19_24_fixed_deposits_months;
		}

		public BigDecimal getR11_over_24_fixed_deposits_months() {
			return r11_over_24_fixed_deposits_months;
		}

		public void setR11_over_24_fixed_deposits_months(BigDecimal r11_over_24_fixed_deposits_months) {
			this.r11_over_24_fixed_deposits_months = r11_over_24_fixed_deposits_months;
		}

		public BigDecimal getR11_certificates_of_deposit() {
			return r11_certificates_of_deposit;
		}

		public void setR11_certificates_of_deposit(BigDecimal r11_certificates_of_deposit) {
			this.r11_certificates_of_deposit = r11_certificates_of_deposit;
		}

		public BigDecimal getR11_total() {
			return r11_total;
		}

		public void setR11_total(BigDecimal r11_total) {
			this.r11_total = r11_total;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_current() {
			return r12_current;
		}

		public void setR12_current(BigDecimal r12_current) {
			this.r12_current = r12_current;
		}

		public BigDecimal getR12_call() {
			return r12_call;
		}

		public void setR12_call(BigDecimal r12_call) {
			this.r12_call = r12_call;
		}

		public BigDecimal getR12_savings() {
			return r12_savings;
		}

		public void setR12_savings(BigDecimal r12_savings) {
			this.r12_savings = r12_savings;
		}

		public BigDecimal getR12_0_31_notice_days() {
			return r12_0_31_notice_days;
		}

		public void setR12_0_31_notice_days(BigDecimal r12_0_31_notice_days) {
			this.r12_0_31_notice_days = r12_0_31_notice_days;
		}

		public BigDecimal getR12_32_88_notice_days() {
			return r12_32_88_notice_days;
		}

		public void setR12_32_88_notice_days(BigDecimal r12_32_88_notice_days) {
			this.r12_32_88_notice_days = r12_32_88_notice_days;
		}

		public BigDecimal getR12_91_day_deposit_fixed_deposit_months() {
			return r12_91_day_deposit_fixed_deposit_months;
		}

		public void setR12_91_day_deposit_fixed_deposit_months(BigDecimal r12_91_day_deposit_fixed_deposit_months) {
			this.r12_91_day_deposit_fixed_deposit_months = r12_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR12_1_2_fixed_deposits_months() {
			return r12_1_2_fixed_deposits_months;
		}

		public void setR12_1_2_fixed_deposits_months(BigDecimal r12_1_2_fixed_deposits_months) {
			this.r12_1_2_fixed_deposits_months = r12_1_2_fixed_deposits_months;
		}

		public BigDecimal getR12_4_6_fixed_deposits_months() {
			return r12_4_6_fixed_deposits_months;
		}

		public void setR12_4_6_fixed_deposits_months(BigDecimal r12_4_6_fixed_deposits_months) {
			this.r12_4_6_fixed_deposits_months = r12_4_6_fixed_deposits_months;
		}

		public BigDecimal getR12_7_12_fixed_deposits_months() {
			return r12_7_12_fixed_deposits_months;
		}

		public void setR12_7_12_fixed_deposits_months(BigDecimal r12_7_12_fixed_deposits_months) {
			this.r12_7_12_fixed_deposits_months = r12_7_12_fixed_deposits_months;
		}

		public BigDecimal getR12_13_18_fixed_deposits_months() {
			return r12_13_18_fixed_deposits_months;
		}

		public void setR12_13_18_fixed_deposits_months(BigDecimal r12_13_18_fixed_deposits_months) {
			this.r12_13_18_fixed_deposits_months = r12_13_18_fixed_deposits_months;
		}

		public BigDecimal getR12_19_24_fixed_deposits_months() {
			return r12_19_24_fixed_deposits_months;
		}

		public void setR12_19_24_fixed_deposits_months(BigDecimal r12_19_24_fixed_deposits_months) {
			this.r12_19_24_fixed_deposits_months = r12_19_24_fixed_deposits_months;
		}

		public BigDecimal getR12_over_24_fixed_deposits_months() {
			return r12_over_24_fixed_deposits_months;
		}

		public void setR12_over_24_fixed_deposits_months(BigDecimal r12_over_24_fixed_deposits_months) {
			this.r12_over_24_fixed_deposits_months = r12_over_24_fixed_deposits_months;
		}

		public BigDecimal getR12_certificates_of_deposit() {
			return r12_certificates_of_deposit;
		}

		public void setR12_certificates_of_deposit(BigDecimal r12_certificates_of_deposit) {
			this.r12_certificates_of_deposit = r12_certificates_of_deposit;
		}

		public BigDecimal getR12_total() {
			return r12_total;
		}

		public void setR12_total(BigDecimal r12_total) {
			this.r12_total = r12_total;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_current() {
			return r13_current;
		}

		public void setR13_current(BigDecimal r13_current) {
			this.r13_current = r13_current;
		}

		public BigDecimal getR13_call() {
			return r13_call;
		}

		public void setR13_call(BigDecimal r13_call) {
			this.r13_call = r13_call;
		}

		public BigDecimal getR13_savings() {
			return r13_savings;
		}

		public void setR13_savings(BigDecimal r13_savings) {
			this.r13_savings = r13_savings;
		}

		public BigDecimal getR13_0_31_notice_days() {
			return r13_0_31_notice_days;
		}

		public void setR13_0_31_notice_days(BigDecimal r13_0_31_notice_days) {
			this.r13_0_31_notice_days = r13_0_31_notice_days;
		}

		public BigDecimal getR13_32_88_notice_days() {
			return r13_32_88_notice_days;
		}

		public void setR13_32_88_notice_days(BigDecimal r13_32_88_notice_days) {
			this.r13_32_88_notice_days = r13_32_88_notice_days;
		}

		public BigDecimal getR13_91_day_deposit_fixed_deposit_months() {
			return r13_91_day_deposit_fixed_deposit_months;
		}

		public void setR13_91_day_deposit_fixed_deposit_months(BigDecimal r13_91_day_deposit_fixed_deposit_months) {
			this.r13_91_day_deposit_fixed_deposit_months = r13_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR13_1_2_fixed_deposits_months() {
			return r13_1_2_fixed_deposits_months;
		}

		public void setR13_1_2_fixed_deposits_months(BigDecimal r13_1_2_fixed_deposits_months) {
			this.r13_1_2_fixed_deposits_months = r13_1_2_fixed_deposits_months;
		}

		public BigDecimal getR13_4_6_fixed_deposits_months() {
			return r13_4_6_fixed_deposits_months;
		}

		public void setR13_4_6_fixed_deposits_months(BigDecimal r13_4_6_fixed_deposits_months) {
			this.r13_4_6_fixed_deposits_months = r13_4_6_fixed_deposits_months;
		}

		public BigDecimal getR13_7_12_fixed_deposits_months() {
			return r13_7_12_fixed_deposits_months;
		}

		public void setR13_7_12_fixed_deposits_months(BigDecimal r13_7_12_fixed_deposits_months) {
			this.r13_7_12_fixed_deposits_months = r13_7_12_fixed_deposits_months;
		}

		public BigDecimal getR13_13_18_fixed_deposits_months() {
			return r13_13_18_fixed_deposits_months;
		}

		public void setR13_13_18_fixed_deposits_months(BigDecimal r13_13_18_fixed_deposits_months) {
			this.r13_13_18_fixed_deposits_months = r13_13_18_fixed_deposits_months;
		}

		public BigDecimal getR13_19_24_fixed_deposits_months() {
			return r13_19_24_fixed_deposits_months;
		}

		public void setR13_19_24_fixed_deposits_months(BigDecimal r13_19_24_fixed_deposits_months) {
			this.r13_19_24_fixed_deposits_months = r13_19_24_fixed_deposits_months;
		}

		public BigDecimal getR13_over_24_fixed_deposits_months() {
			return r13_over_24_fixed_deposits_months;
		}

		public void setR13_over_24_fixed_deposits_months(BigDecimal r13_over_24_fixed_deposits_months) {
			this.r13_over_24_fixed_deposits_months = r13_over_24_fixed_deposits_months;
		}

		public BigDecimal getR13_certificates_of_deposit() {
			return r13_certificates_of_deposit;
		}

		public void setR13_certificates_of_deposit(BigDecimal r13_certificates_of_deposit) {
			this.r13_certificates_of_deposit = r13_certificates_of_deposit;
		}

		public BigDecimal getR13_total() {
			return r13_total;
		}

		public void setR13_total(BigDecimal r13_total) {
			this.r13_total = r13_total;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_current() {
			return r14_current;
		}

		public void setR14_current(BigDecimal r14_current) {
			this.r14_current = r14_current;
		}

		public BigDecimal getR14_call() {
			return r14_call;
		}

		public void setR14_call(BigDecimal r14_call) {
			this.r14_call = r14_call;
		}

		public BigDecimal getR14_savings() {
			return r14_savings;
		}

		public void setR14_savings(BigDecimal r14_savings) {
			this.r14_savings = r14_savings;
		}

		public BigDecimal getR14_0_31_notice_days() {
			return r14_0_31_notice_days;
		}

		public void setR14_0_31_notice_days(BigDecimal r14_0_31_notice_days) {
			this.r14_0_31_notice_days = r14_0_31_notice_days;
		}

		public BigDecimal getR14_32_88_notice_days() {
			return r14_32_88_notice_days;
		}

		public void setR14_32_88_notice_days(BigDecimal r14_32_88_notice_days) {
			this.r14_32_88_notice_days = r14_32_88_notice_days;
		}

		public BigDecimal getR14_91_day_deposit_fixed_deposit_months() {
			return r14_91_day_deposit_fixed_deposit_months;
		}

		public void setR14_91_day_deposit_fixed_deposit_months(BigDecimal r14_91_day_deposit_fixed_deposit_months) {
			this.r14_91_day_deposit_fixed_deposit_months = r14_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR14_1_2_fixed_deposits_months() {
			return r14_1_2_fixed_deposits_months;
		}

		public void setR14_1_2_fixed_deposits_months(BigDecimal r14_1_2_fixed_deposits_months) {
			this.r14_1_2_fixed_deposits_months = r14_1_2_fixed_deposits_months;
		}

		public BigDecimal getR14_4_6_fixed_deposits_months() {
			return r14_4_6_fixed_deposits_months;
		}

		public void setR14_4_6_fixed_deposits_months(BigDecimal r14_4_6_fixed_deposits_months) {
			this.r14_4_6_fixed_deposits_months = r14_4_6_fixed_deposits_months;
		}

		public BigDecimal getR14_7_12_fixed_deposits_months() {
			return r14_7_12_fixed_deposits_months;
		}

		public void setR14_7_12_fixed_deposits_months(BigDecimal r14_7_12_fixed_deposits_months) {
			this.r14_7_12_fixed_deposits_months = r14_7_12_fixed_deposits_months;
		}

		public BigDecimal getR14_13_18_fixed_deposits_months() {
			return r14_13_18_fixed_deposits_months;
		}

		public void setR14_13_18_fixed_deposits_months(BigDecimal r14_13_18_fixed_deposits_months) {
			this.r14_13_18_fixed_deposits_months = r14_13_18_fixed_deposits_months;
		}

		public BigDecimal getR14_19_24_fixed_deposits_months() {
			return r14_19_24_fixed_deposits_months;
		}

		public void setR14_19_24_fixed_deposits_months(BigDecimal r14_19_24_fixed_deposits_months) {
			this.r14_19_24_fixed_deposits_months = r14_19_24_fixed_deposits_months;
		}

		public BigDecimal getR14_over_24_fixed_deposits_months() {
			return r14_over_24_fixed_deposits_months;
		}

		public void setR14_over_24_fixed_deposits_months(BigDecimal r14_over_24_fixed_deposits_months) {
			this.r14_over_24_fixed_deposits_months = r14_over_24_fixed_deposits_months;
		}

		public BigDecimal getR14_certificates_of_deposit() {
			return r14_certificates_of_deposit;
		}

		public void setR14_certificates_of_deposit(BigDecimal r14_certificates_of_deposit) {
			this.r14_certificates_of_deposit = r14_certificates_of_deposit;
		}

		public BigDecimal getR14_total() {
			return r14_total;
		}

		public void setR14_total(BigDecimal r14_total) {
			this.r14_total = r14_total;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_current() {
			return r15_current;
		}

		public void setR15_current(BigDecimal r15_current) {
			this.r15_current = r15_current;
		}

		public BigDecimal getR15_call() {
			return r15_call;
		}

		public void setR15_call(BigDecimal r15_call) {
			this.r15_call = r15_call;
		}

		public BigDecimal getR15_savings() {
			return r15_savings;
		}

		public void setR15_savings(BigDecimal r15_savings) {
			this.r15_savings = r15_savings;
		}

		public BigDecimal getR15_0_31_notice_days() {
			return r15_0_31_notice_days;
		}

		public void setR15_0_31_notice_days(BigDecimal r15_0_31_notice_days) {
			this.r15_0_31_notice_days = r15_0_31_notice_days;
		}

		public BigDecimal getR15_32_88_notice_days() {
			return r15_32_88_notice_days;
		}

		public void setR15_32_88_notice_days(BigDecimal r15_32_88_notice_days) {
			this.r15_32_88_notice_days = r15_32_88_notice_days;
		}

		public BigDecimal getR15_91_day_deposit_fixed_deposit_months() {
			return r15_91_day_deposit_fixed_deposit_months;
		}

		public void setR15_91_day_deposit_fixed_deposit_months(BigDecimal r15_91_day_deposit_fixed_deposit_months) {
			this.r15_91_day_deposit_fixed_deposit_months = r15_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR15_1_2_fixed_deposits_months() {
			return r15_1_2_fixed_deposits_months;
		}

		public void setR15_1_2_fixed_deposits_months(BigDecimal r15_1_2_fixed_deposits_months) {
			this.r15_1_2_fixed_deposits_months = r15_1_2_fixed_deposits_months;
		}

		public BigDecimal getR15_4_6_fixed_deposits_months() {
			return r15_4_6_fixed_deposits_months;
		}

		public void setR15_4_6_fixed_deposits_months(BigDecimal r15_4_6_fixed_deposits_months) {
			this.r15_4_6_fixed_deposits_months = r15_4_6_fixed_deposits_months;
		}

		public BigDecimal getR15_7_12_fixed_deposits_months() {
			return r15_7_12_fixed_deposits_months;
		}

		public void setR15_7_12_fixed_deposits_months(BigDecimal r15_7_12_fixed_deposits_months) {
			this.r15_7_12_fixed_deposits_months = r15_7_12_fixed_deposits_months;
		}

		public BigDecimal getR15_13_18_fixed_deposits_months() {
			return r15_13_18_fixed_deposits_months;
		}

		public void setR15_13_18_fixed_deposits_months(BigDecimal r15_13_18_fixed_deposits_months) {
			this.r15_13_18_fixed_deposits_months = r15_13_18_fixed_deposits_months;
		}

		public BigDecimal getR15_19_24_fixed_deposits_months() {
			return r15_19_24_fixed_deposits_months;
		}

		public void setR15_19_24_fixed_deposits_months(BigDecimal r15_19_24_fixed_deposits_months) {
			this.r15_19_24_fixed_deposits_months = r15_19_24_fixed_deposits_months;
		}

		public BigDecimal getR15_over_24_fixed_deposits_months() {
			return r15_over_24_fixed_deposits_months;
		}

		public void setR15_over_24_fixed_deposits_months(BigDecimal r15_over_24_fixed_deposits_months) {
			this.r15_over_24_fixed_deposits_months = r15_over_24_fixed_deposits_months;
		}

		public BigDecimal getR15_certificates_of_deposit() {
			return r15_certificates_of_deposit;
		}

		public void setR15_certificates_of_deposit(BigDecimal r15_certificates_of_deposit) {
			this.r15_certificates_of_deposit = r15_certificates_of_deposit;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_current() {
			return r16_current;
		}

		public void setR16_current(BigDecimal r16_current) {
			this.r16_current = r16_current;
		}

		public BigDecimal getR16_call() {
			return r16_call;
		}

		public void setR16_call(BigDecimal r16_call) {
			this.r16_call = r16_call;
		}

		public BigDecimal getR16_savings() {
			return r16_savings;
		}

		public void setR16_savings(BigDecimal r16_savings) {
			this.r16_savings = r16_savings;
		}

		public BigDecimal getR16_0_31_notice_days() {
			return r16_0_31_notice_days;
		}

		public void setR16_0_31_notice_days(BigDecimal r16_0_31_notice_days) {
			this.r16_0_31_notice_days = r16_0_31_notice_days;
		}

		public BigDecimal getR16_32_88_notice_days() {
			return r16_32_88_notice_days;
		}

		public void setR16_32_88_notice_days(BigDecimal r16_32_88_notice_days) {
			this.r16_32_88_notice_days = r16_32_88_notice_days;
		}

		public BigDecimal getR16_91_day_deposit_fixed_deposit_months() {
			return r16_91_day_deposit_fixed_deposit_months;
		}

		public void setR16_91_day_deposit_fixed_deposit_months(BigDecimal r16_91_day_deposit_fixed_deposit_months) {
			this.r16_91_day_deposit_fixed_deposit_months = r16_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR16_1_2_fixed_deposits_months() {
			return r16_1_2_fixed_deposits_months;
		}

		public void setR16_1_2_fixed_deposits_months(BigDecimal r16_1_2_fixed_deposits_months) {
			this.r16_1_2_fixed_deposits_months = r16_1_2_fixed_deposits_months;
		}

		public BigDecimal getR16_4_6_fixed_deposits_months() {
			return r16_4_6_fixed_deposits_months;
		}

		public void setR16_4_6_fixed_deposits_months(BigDecimal r16_4_6_fixed_deposits_months) {
			this.r16_4_6_fixed_deposits_months = r16_4_6_fixed_deposits_months;
		}

		public BigDecimal getR16_7_12_fixed_deposits_months() {
			return r16_7_12_fixed_deposits_months;
		}

		public void setR16_7_12_fixed_deposits_months(BigDecimal r16_7_12_fixed_deposits_months) {
			this.r16_7_12_fixed_deposits_months = r16_7_12_fixed_deposits_months;
		}

		public BigDecimal getR16_13_18_fixed_deposits_months() {
			return r16_13_18_fixed_deposits_months;
		}

		public void setR16_13_18_fixed_deposits_months(BigDecimal r16_13_18_fixed_deposits_months) {
			this.r16_13_18_fixed_deposits_months = r16_13_18_fixed_deposits_months;
		}

		public BigDecimal getR16_19_24_fixed_deposits_months() {
			return r16_19_24_fixed_deposits_months;
		}

		public void setR16_19_24_fixed_deposits_months(BigDecimal r16_19_24_fixed_deposits_months) {
			this.r16_19_24_fixed_deposits_months = r16_19_24_fixed_deposits_months;
		}

		public BigDecimal getR16_over_24_fixed_deposits_months() {
			return r16_over_24_fixed_deposits_months;
		}

		public void setR16_over_24_fixed_deposits_months(BigDecimal r16_over_24_fixed_deposits_months) {
			this.r16_over_24_fixed_deposits_months = r16_over_24_fixed_deposits_months;
		}

		public BigDecimal getR16_certificates_of_deposit() {
			return r16_certificates_of_deposit;
		}

		public void setR16_certificates_of_deposit(BigDecimal r16_certificates_of_deposit) {
			this.r16_certificates_of_deposit = r16_certificates_of_deposit;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_current() {
			return r17_current;
		}

		public void setR17_current(BigDecimal r17_current) {
			this.r17_current = r17_current;
		}

		public BigDecimal getR17_call() {
			return r17_call;
		}

		public void setR17_call(BigDecimal r17_call) {
			this.r17_call = r17_call;
		}

		public BigDecimal getR17_savings() {
			return r17_savings;
		}

		public void setR17_savings(BigDecimal r17_savings) {
			this.r17_savings = r17_savings;
		}

		public BigDecimal getR17_0_31_notice_days() {
			return r17_0_31_notice_days;
		}

		public void setR17_0_31_notice_days(BigDecimal r17_0_31_notice_days) {
			this.r17_0_31_notice_days = r17_0_31_notice_days;
		}

		public BigDecimal getR17_32_88_notice_days() {
			return r17_32_88_notice_days;
		}

		public void setR17_32_88_notice_days(BigDecimal r17_32_88_notice_days) {
			this.r17_32_88_notice_days = r17_32_88_notice_days;
		}

		public BigDecimal getR17_91_day_deposit_fixed_deposit_months() {
			return r17_91_day_deposit_fixed_deposit_months;
		}

		public void setR17_91_day_deposit_fixed_deposit_months(BigDecimal r17_91_day_deposit_fixed_deposit_months) {
			this.r17_91_day_deposit_fixed_deposit_months = r17_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR17_1_2_fixed_deposits_months() {
			return r17_1_2_fixed_deposits_months;
		}

		public void setR17_1_2_fixed_deposits_months(BigDecimal r17_1_2_fixed_deposits_months) {
			this.r17_1_2_fixed_deposits_months = r17_1_2_fixed_deposits_months;
		}

		public BigDecimal getR17_4_6_fixed_deposits_months() {
			return r17_4_6_fixed_deposits_months;
		}

		public void setR17_4_6_fixed_deposits_months(BigDecimal r17_4_6_fixed_deposits_months) {
			this.r17_4_6_fixed_deposits_months = r17_4_6_fixed_deposits_months;
		}

		public BigDecimal getR17_7_12_fixed_deposits_months() {
			return r17_7_12_fixed_deposits_months;
		}

		public void setR17_7_12_fixed_deposits_months(BigDecimal r17_7_12_fixed_deposits_months) {
			this.r17_7_12_fixed_deposits_months = r17_7_12_fixed_deposits_months;
		}

		public BigDecimal getR17_13_18_fixed_deposits_months() {
			return r17_13_18_fixed_deposits_months;
		}

		public void setR17_13_18_fixed_deposits_months(BigDecimal r17_13_18_fixed_deposits_months) {
			this.r17_13_18_fixed_deposits_months = r17_13_18_fixed_deposits_months;
		}

		public BigDecimal getR17_19_24_fixed_deposits_months() {
			return r17_19_24_fixed_deposits_months;
		}

		public void setR17_19_24_fixed_deposits_months(BigDecimal r17_19_24_fixed_deposits_months) {
			this.r17_19_24_fixed_deposits_months = r17_19_24_fixed_deposits_months;
		}

		public BigDecimal getR17_over_24_fixed_deposits_months() {
			return r17_over_24_fixed_deposits_months;
		}

		public void setR17_over_24_fixed_deposits_months(BigDecimal r17_over_24_fixed_deposits_months) {
			this.r17_over_24_fixed_deposits_months = r17_over_24_fixed_deposits_months;
		}

		public BigDecimal getR17_certificates_of_deposit() {
			return r17_certificates_of_deposit;
		}

		public void setR17_certificates_of_deposit(BigDecimal r17_certificates_of_deposit) {
			this.r17_certificates_of_deposit = r17_certificates_of_deposit;
		}

		public BigDecimal getR17_total() {
			return r17_total;
		}

		public void setR17_total(BigDecimal r17_total) {
			this.r17_total = r17_total;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_current() {
			return r18_current;
		}

		public void setR18_current(BigDecimal r18_current) {
			this.r18_current = r18_current;
		}

		public BigDecimal getR18_call() {
			return r18_call;
		}

		public void setR18_call(BigDecimal r18_call) {
			this.r18_call = r18_call;
		}

		public BigDecimal getR18_savings() {
			return r18_savings;
		}

		public void setR18_savings(BigDecimal r18_savings) {
			this.r18_savings = r18_savings;
		}

		public BigDecimal getR18_0_31_notice_days() {
			return r18_0_31_notice_days;
		}

		public void setR18_0_31_notice_days(BigDecimal r18_0_31_notice_days) {
			this.r18_0_31_notice_days = r18_0_31_notice_days;
		}

		public BigDecimal getR18_32_88_notice_days() {
			return r18_32_88_notice_days;
		}

		public void setR18_32_88_notice_days(BigDecimal r18_32_88_notice_days) {
			this.r18_32_88_notice_days = r18_32_88_notice_days;
		}

		public BigDecimal getR18_91_day_deposit_fixed_deposit_months() {
			return r18_91_day_deposit_fixed_deposit_months;
		}

		public void setR18_91_day_deposit_fixed_deposit_months(BigDecimal r18_91_day_deposit_fixed_deposit_months) {
			this.r18_91_day_deposit_fixed_deposit_months = r18_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR18_1_2_fixed_deposits_months() {
			return r18_1_2_fixed_deposits_months;
		}

		public void setR18_1_2_fixed_deposits_months(BigDecimal r18_1_2_fixed_deposits_months) {
			this.r18_1_2_fixed_deposits_months = r18_1_2_fixed_deposits_months;
		}

		public BigDecimal getR18_4_6_fixed_deposits_months() {
			return r18_4_6_fixed_deposits_months;
		}

		public void setR18_4_6_fixed_deposits_months(BigDecimal r18_4_6_fixed_deposits_months) {
			this.r18_4_6_fixed_deposits_months = r18_4_6_fixed_deposits_months;
		}

		public BigDecimal getR18_7_12_fixed_deposits_months() {
			return r18_7_12_fixed_deposits_months;
		}

		public void setR18_7_12_fixed_deposits_months(BigDecimal r18_7_12_fixed_deposits_months) {
			this.r18_7_12_fixed_deposits_months = r18_7_12_fixed_deposits_months;
		}

		public BigDecimal getR18_13_18_fixed_deposits_months() {
			return r18_13_18_fixed_deposits_months;
		}

		public void setR18_13_18_fixed_deposits_months(BigDecimal r18_13_18_fixed_deposits_months) {
			this.r18_13_18_fixed_deposits_months = r18_13_18_fixed_deposits_months;
		}

		public BigDecimal getR18_19_24_fixed_deposits_months() {
			return r18_19_24_fixed_deposits_months;
		}

		public void setR18_19_24_fixed_deposits_months(BigDecimal r18_19_24_fixed_deposits_months) {
			this.r18_19_24_fixed_deposits_months = r18_19_24_fixed_deposits_months;
		}

		public BigDecimal getR18_over_24_fixed_deposits_months() {
			return r18_over_24_fixed_deposits_months;
		}

		public void setR18_over_24_fixed_deposits_months(BigDecimal r18_over_24_fixed_deposits_months) {
			this.r18_over_24_fixed_deposits_months = r18_over_24_fixed_deposits_months;
		}

		public BigDecimal getR18_certificates_of_deposit() {
			return r18_certificates_of_deposit;
		}

		public void setR18_certificates_of_deposit(BigDecimal r18_certificates_of_deposit) {
			this.r18_certificates_of_deposit = r18_certificates_of_deposit;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_current() {
			return r19_current;
		}

		public void setR19_current(BigDecimal r19_current) {
			this.r19_current = r19_current;
		}

		public BigDecimal getR19_call() {
			return r19_call;
		}

		public void setR19_call(BigDecimal r19_call) {
			this.r19_call = r19_call;
		}

		public BigDecimal getR19_savings() {
			return r19_savings;
		}

		public void setR19_savings(BigDecimal r19_savings) {
			this.r19_savings = r19_savings;
		}

		public BigDecimal getR19_0_31_notice_days() {
			return r19_0_31_notice_days;
		}

		public void setR19_0_31_notice_days(BigDecimal r19_0_31_notice_days) {
			this.r19_0_31_notice_days = r19_0_31_notice_days;
		}

		public BigDecimal getR19_32_88_notice_days() {
			return r19_32_88_notice_days;
		}

		public void setR19_32_88_notice_days(BigDecimal r19_32_88_notice_days) {
			this.r19_32_88_notice_days = r19_32_88_notice_days;
		}

		public BigDecimal getR19_91_day_deposit_fixed_deposit_months() {
			return r19_91_day_deposit_fixed_deposit_months;
		}

		public void setR19_91_day_deposit_fixed_deposit_months(BigDecimal r19_91_day_deposit_fixed_deposit_months) {
			this.r19_91_day_deposit_fixed_deposit_months = r19_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR19_1_2_fixed_deposits_months() {
			return r19_1_2_fixed_deposits_months;
		}

		public void setR19_1_2_fixed_deposits_months(BigDecimal r19_1_2_fixed_deposits_months) {
			this.r19_1_2_fixed_deposits_months = r19_1_2_fixed_deposits_months;
		}

		public BigDecimal getR19_4_6_fixed_deposits_months() {
			return r19_4_6_fixed_deposits_months;
		}

		public void setR19_4_6_fixed_deposits_months(BigDecimal r19_4_6_fixed_deposits_months) {
			this.r19_4_6_fixed_deposits_months = r19_4_6_fixed_deposits_months;
		}

		public BigDecimal getR19_7_12_fixed_deposits_months() {
			return r19_7_12_fixed_deposits_months;
		}

		public void setR19_7_12_fixed_deposits_months(BigDecimal r19_7_12_fixed_deposits_months) {
			this.r19_7_12_fixed_deposits_months = r19_7_12_fixed_deposits_months;
		}

		public BigDecimal getR19_13_18_fixed_deposits_months() {
			return r19_13_18_fixed_deposits_months;
		}

		public void setR19_13_18_fixed_deposits_months(BigDecimal r19_13_18_fixed_deposits_months) {
			this.r19_13_18_fixed_deposits_months = r19_13_18_fixed_deposits_months;
		}

		public BigDecimal getR19_19_24_fixed_deposits_months() {
			return r19_19_24_fixed_deposits_months;
		}

		public void setR19_19_24_fixed_deposits_months(BigDecimal r19_19_24_fixed_deposits_months) {
			this.r19_19_24_fixed_deposits_months = r19_19_24_fixed_deposits_months;
		}

		public BigDecimal getR19_over_24_fixed_deposits_months() {
			return r19_over_24_fixed_deposits_months;
		}

		public void setR19_over_24_fixed_deposits_months(BigDecimal r19_over_24_fixed_deposits_months) {
			this.r19_over_24_fixed_deposits_months = r19_over_24_fixed_deposits_months;
		}

		public BigDecimal getR19_certificates_of_deposit() {
			return r19_certificates_of_deposit;
		}

		public void setR19_certificates_of_deposit(BigDecimal r19_certificates_of_deposit) {
			this.r19_certificates_of_deposit = r19_certificates_of_deposit;
		}

		public BigDecimal getR19_total() {
			return r19_total;
		}

		public void setR19_total(BigDecimal r19_total) {
			this.r19_total = r19_total;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_current() {
			return r20_current;
		}

		public void setR20_current(BigDecimal r20_current) {
			this.r20_current = r20_current;
		}

		public BigDecimal getR20_call() {
			return r20_call;
		}

		public void setR20_call(BigDecimal r20_call) {
			this.r20_call = r20_call;
		}

		public BigDecimal getR20_savings() {
			return r20_savings;
		}

		public void setR20_savings(BigDecimal r20_savings) {
			this.r20_savings = r20_savings;
		}

		public BigDecimal getR20_0_31_notice_days() {
			return r20_0_31_notice_days;
		}

		public void setR20_0_31_notice_days(BigDecimal r20_0_31_notice_days) {
			this.r20_0_31_notice_days = r20_0_31_notice_days;
		}

		public BigDecimal getR20_32_88_notice_days() {
			return r20_32_88_notice_days;
		}

		public void setR20_32_88_notice_days(BigDecimal r20_32_88_notice_days) {
			this.r20_32_88_notice_days = r20_32_88_notice_days;
		}

		public BigDecimal getR20_91_day_deposit_fixed_deposit_months() {
			return r20_91_day_deposit_fixed_deposit_months;
		}

		public void setR20_91_day_deposit_fixed_deposit_months(BigDecimal r20_91_day_deposit_fixed_deposit_months) {
			this.r20_91_day_deposit_fixed_deposit_months = r20_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR20_1_2_fixed_deposits_months() {
			return r20_1_2_fixed_deposits_months;
		}

		public void setR20_1_2_fixed_deposits_months(BigDecimal r20_1_2_fixed_deposits_months) {
			this.r20_1_2_fixed_deposits_months = r20_1_2_fixed_deposits_months;
		}

		public BigDecimal getR20_4_6_fixed_deposits_months() {
			return r20_4_6_fixed_deposits_months;
		}

		public void setR20_4_6_fixed_deposits_months(BigDecimal r20_4_6_fixed_deposits_months) {
			this.r20_4_6_fixed_deposits_months = r20_4_6_fixed_deposits_months;
		}

		public BigDecimal getR20_7_12_fixed_deposits_months() {
			return r20_7_12_fixed_deposits_months;
		}

		public void setR20_7_12_fixed_deposits_months(BigDecimal r20_7_12_fixed_deposits_months) {
			this.r20_7_12_fixed_deposits_months = r20_7_12_fixed_deposits_months;
		}

		public BigDecimal getR20_13_18_fixed_deposits_months() {
			return r20_13_18_fixed_deposits_months;
		}

		public void setR20_13_18_fixed_deposits_months(BigDecimal r20_13_18_fixed_deposits_months) {
			this.r20_13_18_fixed_deposits_months = r20_13_18_fixed_deposits_months;
		}

		public BigDecimal getR20_19_24_fixed_deposits_months() {
			return r20_19_24_fixed_deposits_months;
		}

		public void setR20_19_24_fixed_deposits_months(BigDecimal r20_19_24_fixed_deposits_months) {
			this.r20_19_24_fixed_deposits_months = r20_19_24_fixed_deposits_months;
		}

		public BigDecimal getR20_over_24_fixed_deposits_months() {
			return r20_over_24_fixed_deposits_months;
		}

		public void setR20_over_24_fixed_deposits_months(BigDecimal r20_over_24_fixed_deposits_months) {
			this.r20_over_24_fixed_deposits_months = r20_over_24_fixed_deposits_months;
		}

		public BigDecimal getR20_certificates_of_deposit() {
			return r20_certificates_of_deposit;
		}

		public void setR20_certificates_of_deposit(BigDecimal r20_certificates_of_deposit) {
			this.r20_certificates_of_deposit = r20_certificates_of_deposit;
		}

		public BigDecimal getR20_total() {
			return r20_total;
		}

		public void setR20_total(BigDecimal r20_total) {
			this.r20_total = r20_total;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_current() {
			return r21_current;
		}

		public void setR21_current(BigDecimal r21_current) {
			this.r21_current = r21_current;
		}

		public BigDecimal getR21_call() {
			return r21_call;
		}

		public void setR21_call(BigDecimal r21_call) {
			this.r21_call = r21_call;
		}

		public BigDecimal getR21_savings() {
			return r21_savings;
		}

		public void setR21_savings(BigDecimal r21_savings) {
			this.r21_savings = r21_savings;
		}

		public BigDecimal getR21_0_31_notice_days() {
			return r21_0_31_notice_days;
		}

		public void setR21_0_31_notice_days(BigDecimal r21_0_31_notice_days) {
			this.r21_0_31_notice_days = r21_0_31_notice_days;
		}

		public BigDecimal getR21_32_88_notice_days() {
			return r21_32_88_notice_days;
		}

		public void setR21_32_88_notice_days(BigDecimal r21_32_88_notice_days) {
			this.r21_32_88_notice_days = r21_32_88_notice_days;
		}

		public BigDecimal getR21_91_day_deposit_fixed_deposit_months() {
			return r21_91_day_deposit_fixed_deposit_months;
		}

		public void setR21_91_day_deposit_fixed_deposit_months(BigDecimal r21_91_day_deposit_fixed_deposit_months) {
			this.r21_91_day_deposit_fixed_deposit_months = r21_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR21_1_2_fixed_deposits_months() {
			return r21_1_2_fixed_deposits_months;
		}

		public void setR21_1_2_fixed_deposits_months(BigDecimal r21_1_2_fixed_deposits_months) {
			this.r21_1_2_fixed_deposits_months = r21_1_2_fixed_deposits_months;
		}

		public BigDecimal getR21_4_6_fixed_deposits_months() {
			return r21_4_6_fixed_deposits_months;
		}

		public void setR21_4_6_fixed_deposits_months(BigDecimal r21_4_6_fixed_deposits_months) {
			this.r21_4_6_fixed_deposits_months = r21_4_6_fixed_deposits_months;
		}

		public BigDecimal getR21_7_12_fixed_deposits_months() {
			return r21_7_12_fixed_deposits_months;
		}

		public void setR21_7_12_fixed_deposits_months(BigDecimal r21_7_12_fixed_deposits_months) {
			this.r21_7_12_fixed_deposits_months = r21_7_12_fixed_deposits_months;
		}

		public BigDecimal getR21_13_18_fixed_deposits_months() {
			return r21_13_18_fixed_deposits_months;
		}

		public void setR21_13_18_fixed_deposits_months(BigDecimal r21_13_18_fixed_deposits_months) {
			this.r21_13_18_fixed_deposits_months = r21_13_18_fixed_deposits_months;
		}

		public BigDecimal getR21_19_24_fixed_deposits_months() {
			return r21_19_24_fixed_deposits_months;
		}

		public void setR21_19_24_fixed_deposits_months(BigDecimal r21_19_24_fixed_deposits_months) {
			this.r21_19_24_fixed_deposits_months = r21_19_24_fixed_deposits_months;
		}

		public BigDecimal getR21_over_24_fixed_deposits_months() {
			return r21_over_24_fixed_deposits_months;
		}

		public void setR21_over_24_fixed_deposits_months(BigDecimal r21_over_24_fixed_deposits_months) {
			this.r21_over_24_fixed_deposits_months = r21_over_24_fixed_deposits_months;
		}

		public BigDecimal getR21_certificates_of_deposit() {
			return r21_certificates_of_deposit;
		}

		public void setR21_certificates_of_deposit(BigDecimal r21_certificates_of_deposit) {
			this.r21_certificates_of_deposit = r21_certificates_of_deposit;
		}

		public BigDecimal getR21_total() {
			return r21_total;
		}

		public void setR21_total(BigDecimal r21_total) {
			this.r21_total = r21_total;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_current() {
			return r22_current;
		}

		public void setR22_current(BigDecimal r22_current) {
			this.r22_current = r22_current;
		}

		public BigDecimal getR22_call() {
			return r22_call;
		}

		public void setR22_call(BigDecimal r22_call) {
			this.r22_call = r22_call;
		}

		public BigDecimal getR22_savings() {
			return r22_savings;
		}

		public void setR22_savings(BigDecimal r22_savings) {
			this.r22_savings = r22_savings;
		}

		public BigDecimal getR22_0_31_notice_days() {
			return r22_0_31_notice_days;
		}

		public void setR22_0_31_notice_days(BigDecimal r22_0_31_notice_days) {
			this.r22_0_31_notice_days = r22_0_31_notice_days;
		}

		public BigDecimal getR22_32_88_notice_days() {
			return r22_32_88_notice_days;
		}

		public void setR22_32_88_notice_days(BigDecimal r22_32_88_notice_days) {
			this.r22_32_88_notice_days = r22_32_88_notice_days;
		}

		public BigDecimal getR22_91_day_deposit_fixed_deposit_months() {
			return r22_91_day_deposit_fixed_deposit_months;
		}

		public void setR22_91_day_deposit_fixed_deposit_months(BigDecimal r22_91_day_deposit_fixed_deposit_months) {
			this.r22_91_day_deposit_fixed_deposit_months = r22_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR22_1_2_fixed_deposits_months() {
			return r22_1_2_fixed_deposits_months;
		}

		public void setR22_1_2_fixed_deposits_months(BigDecimal r22_1_2_fixed_deposits_months) {
			this.r22_1_2_fixed_deposits_months = r22_1_2_fixed_deposits_months;
		}

		public BigDecimal getR22_4_6_fixed_deposits_months() {
			return r22_4_6_fixed_deposits_months;
		}

		public void setR22_4_6_fixed_deposits_months(BigDecimal r22_4_6_fixed_deposits_months) {
			this.r22_4_6_fixed_deposits_months = r22_4_6_fixed_deposits_months;
		}

		public BigDecimal getR22_7_12_fixed_deposits_months() {
			return r22_7_12_fixed_deposits_months;
		}

		public void setR22_7_12_fixed_deposits_months(BigDecimal r22_7_12_fixed_deposits_months) {
			this.r22_7_12_fixed_deposits_months = r22_7_12_fixed_deposits_months;
		}

		public BigDecimal getR22_13_18_fixed_deposits_months() {
			return r22_13_18_fixed_deposits_months;
		}

		public void setR22_13_18_fixed_deposits_months(BigDecimal r22_13_18_fixed_deposits_months) {
			this.r22_13_18_fixed_deposits_months = r22_13_18_fixed_deposits_months;
		}

		public BigDecimal getR22_19_24_fixed_deposits_months() {
			return r22_19_24_fixed_deposits_months;
		}

		public void setR22_19_24_fixed_deposits_months(BigDecimal r22_19_24_fixed_deposits_months) {
			this.r22_19_24_fixed_deposits_months = r22_19_24_fixed_deposits_months;
		}

		public BigDecimal getR22_over_24_fixed_deposits_months() {
			return r22_over_24_fixed_deposits_months;
		}

		public void setR22_over_24_fixed_deposits_months(BigDecimal r22_over_24_fixed_deposits_months) {
			this.r22_over_24_fixed_deposits_months = r22_over_24_fixed_deposits_months;
		}

		public BigDecimal getR22_certificates_of_deposit() {
			return r22_certificates_of_deposit;
		}

		public void setR22_certificates_of_deposit(BigDecimal r22_certificates_of_deposit) {
			this.r22_certificates_of_deposit = r22_certificates_of_deposit;
		}

		public BigDecimal getR22_total() {
			return r22_total;
		}

		public void setR22_total(BigDecimal r22_total) {
			this.r22_total = r22_total;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_current() {
			return r23_current;
		}

		public void setR23_current(BigDecimal r23_current) {
			this.r23_current = r23_current;
		}

		public BigDecimal getR23_call() {
			return r23_call;
		}

		public void setR23_call(BigDecimal r23_call) {
			this.r23_call = r23_call;
		}

		public BigDecimal getR23_savings() {
			return r23_savings;
		}

		public void setR23_savings(BigDecimal r23_savings) {
			this.r23_savings = r23_savings;
		}

		public BigDecimal getR23_0_31_notice_days() {
			return r23_0_31_notice_days;
		}

		public void setR23_0_31_notice_days(BigDecimal r23_0_31_notice_days) {
			this.r23_0_31_notice_days = r23_0_31_notice_days;
		}

		public BigDecimal getR23_32_88_notice_days() {
			return r23_32_88_notice_days;
		}

		public void setR23_32_88_notice_days(BigDecimal r23_32_88_notice_days) {
			this.r23_32_88_notice_days = r23_32_88_notice_days;
		}

		public BigDecimal getR23_91_day_deposit_fixed_deposit_months() {
			return r23_91_day_deposit_fixed_deposit_months;
		}

		public void setR23_91_day_deposit_fixed_deposit_months(BigDecimal r23_91_day_deposit_fixed_deposit_months) {
			this.r23_91_day_deposit_fixed_deposit_months = r23_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR23_1_2_fixed_deposits_months() {
			return r23_1_2_fixed_deposits_months;
		}

		public void setR23_1_2_fixed_deposits_months(BigDecimal r23_1_2_fixed_deposits_months) {
			this.r23_1_2_fixed_deposits_months = r23_1_2_fixed_deposits_months;
		}

		public BigDecimal getR23_4_6_fixed_deposits_months() {
			return r23_4_6_fixed_deposits_months;
		}

		public void setR23_4_6_fixed_deposits_months(BigDecimal r23_4_6_fixed_deposits_months) {
			this.r23_4_6_fixed_deposits_months = r23_4_6_fixed_deposits_months;
		}

		public BigDecimal getR23_7_12_fixed_deposits_months() {
			return r23_7_12_fixed_deposits_months;
		}

		public void setR23_7_12_fixed_deposits_months(BigDecimal r23_7_12_fixed_deposits_months) {
			this.r23_7_12_fixed_deposits_months = r23_7_12_fixed_deposits_months;
		}

		public BigDecimal getR23_13_18_fixed_deposits_months() {
			return r23_13_18_fixed_deposits_months;
		}

		public void setR23_13_18_fixed_deposits_months(BigDecimal r23_13_18_fixed_deposits_months) {
			this.r23_13_18_fixed_deposits_months = r23_13_18_fixed_deposits_months;
		}

		public BigDecimal getR23_19_24_fixed_deposits_months() {
			return r23_19_24_fixed_deposits_months;
		}

		public void setR23_19_24_fixed_deposits_months(BigDecimal r23_19_24_fixed_deposits_months) {
			this.r23_19_24_fixed_deposits_months = r23_19_24_fixed_deposits_months;
		}

		public BigDecimal getR23_over_24_fixed_deposits_months() {
			return r23_over_24_fixed_deposits_months;
		}

		public void setR23_over_24_fixed_deposits_months(BigDecimal r23_over_24_fixed_deposits_months) {
			this.r23_over_24_fixed_deposits_months = r23_over_24_fixed_deposits_months;
		}

		public BigDecimal getR23_certificates_of_deposit() {
			return r23_certificates_of_deposit;
		}

		public void setR23_certificates_of_deposit(BigDecimal r23_certificates_of_deposit) {
			this.r23_certificates_of_deposit = r23_certificates_of_deposit;
		}

		public BigDecimal getR23_total() {
			return r23_total;
		}

		public void setR23_total(BigDecimal r23_total) {
			this.r23_total = r23_total;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_current() {
			return r24_current;
		}

		public void setR24_current(BigDecimal r24_current) {
			this.r24_current = r24_current;
		}

		public BigDecimal getR24_call() {
			return r24_call;
		}

		public void setR24_call(BigDecimal r24_call) {
			this.r24_call = r24_call;
		}

		public BigDecimal getR24_savings() {
			return r24_savings;
		}

		public void setR24_savings(BigDecimal r24_savings) {
			this.r24_savings = r24_savings;
		}

		public BigDecimal getR24_0_31_notice_days() {
			return r24_0_31_notice_days;
		}

		public void setR24_0_31_notice_days(BigDecimal r24_0_31_notice_days) {
			this.r24_0_31_notice_days = r24_0_31_notice_days;
		}

		public BigDecimal getR24_32_88_notice_days() {
			return r24_32_88_notice_days;
		}

		public void setR24_32_88_notice_days(BigDecimal r24_32_88_notice_days) {
			this.r24_32_88_notice_days = r24_32_88_notice_days;
		}

		public BigDecimal getR24_91_day_deposit_fixed_deposit_months() {
			return r24_91_day_deposit_fixed_deposit_months;
		}

		public void setR24_91_day_deposit_fixed_deposit_months(BigDecimal r24_91_day_deposit_fixed_deposit_months) {
			this.r24_91_day_deposit_fixed_deposit_months = r24_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR24_1_2_fixed_deposits_months() {
			return r24_1_2_fixed_deposits_months;
		}

		public void setR24_1_2_fixed_deposits_months(BigDecimal r24_1_2_fixed_deposits_months) {
			this.r24_1_2_fixed_deposits_months = r24_1_2_fixed_deposits_months;
		}

		public BigDecimal getR24_4_6_fixed_deposits_months() {
			return r24_4_6_fixed_deposits_months;
		}

		public void setR24_4_6_fixed_deposits_months(BigDecimal r24_4_6_fixed_deposits_months) {
			this.r24_4_6_fixed_deposits_months = r24_4_6_fixed_deposits_months;
		}

		public BigDecimal getR24_7_12_fixed_deposits_months() {
			return r24_7_12_fixed_deposits_months;
		}

		public void setR24_7_12_fixed_deposits_months(BigDecimal r24_7_12_fixed_deposits_months) {
			this.r24_7_12_fixed_deposits_months = r24_7_12_fixed_deposits_months;
		}

		public BigDecimal getR24_13_18_fixed_deposits_months() {
			return r24_13_18_fixed_deposits_months;
		}

		public void setR24_13_18_fixed_deposits_months(BigDecimal r24_13_18_fixed_deposits_months) {
			this.r24_13_18_fixed_deposits_months = r24_13_18_fixed_deposits_months;
		}

		public BigDecimal getR24_19_24_fixed_deposits_months() {
			return r24_19_24_fixed_deposits_months;
		}

		public void setR24_19_24_fixed_deposits_months(BigDecimal r24_19_24_fixed_deposits_months) {
			this.r24_19_24_fixed_deposits_months = r24_19_24_fixed_deposits_months;
		}

		public BigDecimal getR24_over_24_fixed_deposits_months() {
			return r24_over_24_fixed_deposits_months;
		}

		public void setR24_over_24_fixed_deposits_months(BigDecimal r24_over_24_fixed_deposits_months) {
			this.r24_over_24_fixed_deposits_months = r24_over_24_fixed_deposits_months;
		}

		public BigDecimal getR24_certificates_of_deposit() {
			return r24_certificates_of_deposit;
		}

		public void setR24_certificates_of_deposit(BigDecimal r24_certificates_of_deposit) {
			this.r24_certificates_of_deposit = r24_certificates_of_deposit;
		}

		public BigDecimal getR24_total() {
			return r24_total;
		}

		public void setR24_total(BigDecimal r24_total) {
			this.r24_total = r24_total;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_current() {
			return r25_current;
		}

		public void setR25_current(BigDecimal r25_current) {
			this.r25_current = r25_current;
		}

		public BigDecimal getR25_call() {
			return r25_call;
		}

		public void setR25_call(BigDecimal r25_call) {
			this.r25_call = r25_call;
		}

		public BigDecimal getR25_savings() {
			return r25_savings;
		}

		public void setR25_savings(BigDecimal r25_savings) {
			this.r25_savings = r25_savings;
		}

		public BigDecimal getR25_0_31_notice_days() {
			return r25_0_31_notice_days;
		}

		public void setR25_0_31_notice_days(BigDecimal r25_0_31_notice_days) {
			this.r25_0_31_notice_days = r25_0_31_notice_days;
		}

		public BigDecimal getR25_32_88_notice_days() {
			return r25_32_88_notice_days;
		}

		public void setR25_32_88_notice_days(BigDecimal r25_32_88_notice_days) {
			this.r25_32_88_notice_days = r25_32_88_notice_days;
		}

		public BigDecimal getR25_91_day_deposit_fixed_deposit_months() {
			return r25_91_day_deposit_fixed_deposit_months;
		}

		public void setR25_91_day_deposit_fixed_deposit_months(BigDecimal r25_91_day_deposit_fixed_deposit_months) {
			this.r25_91_day_deposit_fixed_deposit_months = r25_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR25_1_2_fixed_deposits_months() {
			return r25_1_2_fixed_deposits_months;
		}

		public void setR25_1_2_fixed_deposits_months(BigDecimal r25_1_2_fixed_deposits_months) {
			this.r25_1_2_fixed_deposits_months = r25_1_2_fixed_deposits_months;
		}

		public BigDecimal getR25_4_6_fixed_deposits_months() {
			return r25_4_6_fixed_deposits_months;
		}

		public void setR25_4_6_fixed_deposits_months(BigDecimal r25_4_6_fixed_deposits_months) {
			this.r25_4_6_fixed_deposits_months = r25_4_6_fixed_deposits_months;
		}

		public BigDecimal getR25_7_12_fixed_deposits_months() {
			return r25_7_12_fixed_deposits_months;
		}

		public void setR25_7_12_fixed_deposits_months(BigDecimal r25_7_12_fixed_deposits_months) {
			this.r25_7_12_fixed_deposits_months = r25_7_12_fixed_deposits_months;
		}

		public BigDecimal getR25_13_18_fixed_deposits_months() {
			return r25_13_18_fixed_deposits_months;
		}

		public void setR25_13_18_fixed_deposits_months(BigDecimal r25_13_18_fixed_deposits_months) {
			this.r25_13_18_fixed_deposits_months = r25_13_18_fixed_deposits_months;
		}

		public BigDecimal getR25_19_24_fixed_deposits_months() {
			return r25_19_24_fixed_deposits_months;
		}

		public void setR25_19_24_fixed_deposits_months(BigDecimal r25_19_24_fixed_deposits_months) {
			this.r25_19_24_fixed_deposits_months = r25_19_24_fixed_deposits_months;
		}

		public BigDecimal getR25_over_24_fixed_deposits_months() {
			return r25_over_24_fixed_deposits_months;
		}

		public void setR25_over_24_fixed_deposits_months(BigDecimal r25_over_24_fixed_deposits_months) {
			this.r25_over_24_fixed_deposits_months = r25_over_24_fixed_deposits_months;
		}

		public BigDecimal getR25_certificates_of_deposit() {
			return r25_certificates_of_deposit;
		}

		public void setR25_certificates_of_deposit(BigDecimal r25_certificates_of_deposit) {
			this.r25_certificates_of_deposit = r25_certificates_of_deposit;
		}

		public BigDecimal getR25_total() {
			return r25_total;
		}

		public void setR25_total(BigDecimal r25_total) {
			this.r25_total = r25_total;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_current() {
			return r26_current;
		}

		public void setR26_current(BigDecimal r26_current) {
			this.r26_current = r26_current;
		}

		public BigDecimal getR26_call() {
			return r26_call;
		}

		public void setR26_call(BigDecimal r26_call) {
			this.r26_call = r26_call;
		}

		public BigDecimal getR26_savings() {
			return r26_savings;
		}

		public void setR26_savings(BigDecimal r26_savings) {
			this.r26_savings = r26_savings;
		}

		public BigDecimal getR26_0_31_notice_days() {
			return r26_0_31_notice_days;
		}

		public void setR26_0_31_notice_days(BigDecimal r26_0_31_notice_days) {
			this.r26_0_31_notice_days = r26_0_31_notice_days;
		}

		public BigDecimal getR26_32_88_notice_days() {
			return r26_32_88_notice_days;
		}

		public void setR26_32_88_notice_days(BigDecimal r26_32_88_notice_days) {
			this.r26_32_88_notice_days = r26_32_88_notice_days;
		}

		public BigDecimal getR26_91_day_deposit_fixed_deposit_months() {
			return r26_91_day_deposit_fixed_deposit_months;
		}

		public void setR26_91_day_deposit_fixed_deposit_months(BigDecimal r26_91_day_deposit_fixed_deposit_months) {
			this.r26_91_day_deposit_fixed_deposit_months = r26_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR26_1_2_fixed_deposits_months() {
			return r26_1_2_fixed_deposits_months;
		}

		public void setR26_1_2_fixed_deposits_months(BigDecimal r26_1_2_fixed_deposits_months) {
			this.r26_1_2_fixed_deposits_months = r26_1_2_fixed_deposits_months;
		}

		public BigDecimal getR26_4_6_fixed_deposits_months() {
			return r26_4_6_fixed_deposits_months;
		}

		public void setR26_4_6_fixed_deposits_months(BigDecimal r26_4_6_fixed_deposits_months) {
			this.r26_4_6_fixed_deposits_months = r26_4_6_fixed_deposits_months;
		}

		public BigDecimal getR26_7_12_fixed_deposits_months() {
			return r26_7_12_fixed_deposits_months;
		}

		public void setR26_7_12_fixed_deposits_months(BigDecimal r26_7_12_fixed_deposits_months) {
			this.r26_7_12_fixed_deposits_months = r26_7_12_fixed_deposits_months;
		}

		public BigDecimal getR26_13_18_fixed_deposits_months() {
			return r26_13_18_fixed_deposits_months;
		}

		public void setR26_13_18_fixed_deposits_months(BigDecimal r26_13_18_fixed_deposits_months) {
			this.r26_13_18_fixed_deposits_months = r26_13_18_fixed_deposits_months;
		}

		public BigDecimal getR26_19_24_fixed_deposits_months() {
			return r26_19_24_fixed_deposits_months;
		}

		public void setR26_19_24_fixed_deposits_months(BigDecimal r26_19_24_fixed_deposits_months) {
			this.r26_19_24_fixed_deposits_months = r26_19_24_fixed_deposits_months;
		}

		public BigDecimal getR26_over_24_fixed_deposits_months() {
			return r26_over_24_fixed_deposits_months;
		}

		public void setR26_over_24_fixed_deposits_months(BigDecimal r26_over_24_fixed_deposits_months) {
			this.r26_over_24_fixed_deposits_months = r26_over_24_fixed_deposits_months;
		}

		public BigDecimal getR26_certificates_of_deposit() {
			return r26_certificates_of_deposit;
		}

		public void setR26_certificates_of_deposit(BigDecimal r26_certificates_of_deposit) {
			this.r26_certificates_of_deposit = r26_certificates_of_deposit;
		}

		public BigDecimal getR26_total() {
			return r26_total;
		}

		public void setR26_total(BigDecimal r26_total) {
			this.r26_total = r26_total;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_current() {
			return r27_current;
		}

		public void setR27_current(BigDecimal r27_current) {
			this.r27_current = r27_current;
		}

		public BigDecimal getR27_call() {
			return r27_call;
		}

		public void setR27_call(BigDecimal r27_call) {
			this.r27_call = r27_call;
		}

		public BigDecimal getR27_savings() {
			return r27_savings;
		}

		public void setR27_savings(BigDecimal r27_savings) {
			this.r27_savings = r27_savings;
		}

		public BigDecimal getR27_0_31_notice_days() {
			return r27_0_31_notice_days;
		}

		public void setR27_0_31_notice_days(BigDecimal r27_0_31_notice_days) {
			this.r27_0_31_notice_days = r27_0_31_notice_days;
		}

		public BigDecimal getR27_32_88_notice_days() {
			return r27_32_88_notice_days;
		}

		public void setR27_32_88_notice_days(BigDecimal r27_32_88_notice_days) {
			this.r27_32_88_notice_days = r27_32_88_notice_days;
		}

		public BigDecimal getR27_91_day_deposit_fixed_deposit_months() {
			return r27_91_day_deposit_fixed_deposit_months;
		}

		public void setR27_91_day_deposit_fixed_deposit_months(BigDecimal r27_91_day_deposit_fixed_deposit_months) {
			this.r27_91_day_deposit_fixed_deposit_months = r27_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR27_1_2_fixed_deposits_months() {
			return r27_1_2_fixed_deposits_months;
		}

		public void setR27_1_2_fixed_deposits_months(BigDecimal r27_1_2_fixed_deposits_months) {
			this.r27_1_2_fixed_deposits_months = r27_1_2_fixed_deposits_months;
		}

		public BigDecimal getR27_4_6_fixed_deposits_months() {
			return r27_4_6_fixed_deposits_months;
		}

		public void setR27_4_6_fixed_deposits_months(BigDecimal r27_4_6_fixed_deposits_months) {
			this.r27_4_6_fixed_deposits_months = r27_4_6_fixed_deposits_months;
		}

		public BigDecimal getR27_7_12_fixed_deposits_months() {
			return r27_7_12_fixed_deposits_months;
		}

		public void setR27_7_12_fixed_deposits_months(BigDecimal r27_7_12_fixed_deposits_months) {
			this.r27_7_12_fixed_deposits_months = r27_7_12_fixed_deposits_months;
		}

		public BigDecimal getR27_13_18_fixed_deposits_months() {
			return r27_13_18_fixed_deposits_months;
		}

		public void setR27_13_18_fixed_deposits_months(BigDecimal r27_13_18_fixed_deposits_months) {
			this.r27_13_18_fixed_deposits_months = r27_13_18_fixed_deposits_months;
		}

		public BigDecimal getR27_19_24_fixed_deposits_months() {
			return r27_19_24_fixed_deposits_months;
		}

		public void setR27_19_24_fixed_deposits_months(BigDecimal r27_19_24_fixed_deposits_months) {
			this.r27_19_24_fixed_deposits_months = r27_19_24_fixed_deposits_months;
		}

		public BigDecimal getR27_over_24_fixed_deposits_months() {
			return r27_over_24_fixed_deposits_months;
		}

		public void setR27_over_24_fixed_deposits_months(BigDecimal r27_over_24_fixed_deposits_months) {
			this.r27_over_24_fixed_deposits_months = r27_over_24_fixed_deposits_months;
		}

		public BigDecimal getR27_certificates_of_deposit() {
			return r27_certificates_of_deposit;
		}

		public void setR27_certificates_of_deposit(BigDecimal r27_certificates_of_deposit) {
			this.r27_certificates_of_deposit = r27_certificates_of_deposit;
		}

		public BigDecimal getR27_total() {
			return r27_total;
		}

		public void setR27_total(BigDecimal r27_total) {
			this.r27_total = r27_total;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_current() {
			return r28_current;
		}

		public void setR28_current(BigDecimal r28_current) {
			this.r28_current = r28_current;
		}

		public BigDecimal getR28_call() {
			return r28_call;
		}

		public void setR28_call(BigDecimal r28_call) {
			this.r28_call = r28_call;
		}

		public BigDecimal getR28_savings() {
			return r28_savings;
		}

		public void setR28_savings(BigDecimal r28_savings) {
			this.r28_savings = r28_savings;
		}

		public BigDecimal getR28_0_31_notice_days() {
			return r28_0_31_notice_days;
		}

		public void setR28_0_31_notice_days(BigDecimal r28_0_31_notice_days) {
			this.r28_0_31_notice_days = r28_0_31_notice_days;
		}

		public BigDecimal getR28_32_88_notice_days() {
			return r28_32_88_notice_days;
		}

		public void setR28_32_88_notice_days(BigDecimal r28_32_88_notice_days) {
			this.r28_32_88_notice_days = r28_32_88_notice_days;
		}

		public BigDecimal getR28_91_day_deposit_fixed_deposit_months() {
			return r28_91_day_deposit_fixed_deposit_months;
		}

		public void setR28_91_day_deposit_fixed_deposit_months(BigDecimal r28_91_day_deposit_fixed_deposit_months) {
			this.r28_91_day_deposit_fixed_deposit_months = r28_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR28_1_2_fixed_deposits_months() {
			return r28_1_2_fixed_deposits_months;
		}

		public void setR28_1_2_fixed_deposits_months(BigDecimal r28_1_2_fixed_deposits_months) {
			this.r28_1_2_fixed_deposits_months = r28_1_2_fixed_deposits_months;
		}

		public BigDecimal getR28_4_6_fixed_deposits_months() {
			return r28_4_6_fixed_deposits_months;
		}

		public void setR28_4_6_fixed_deposits_months(BigDecimal r28_4_6_fixed_deposits_months) {
			this.r28_4_6_fixed_deposits_months = r28_4_6_fixed_deposits_months;
		}

		public BigDecimal getR28_7_12_fixed_deposits_months() {
			return r28_7_12_fixed_deposits_months;
		}

		public void setR28_7_12_fixed_deposits_months(BigDecimal r28_7_12_fixed_deposits_months) {
			this.r28_7_12_fixed_deposits_months = r28_7_12_fixed_deposits_months;
		}

		public BigDecimal getR28_13_18_fixed_deposits_months() {
			return r28_13_18_fixed_deposits_months;
		}

		public void setR28_13_18_fixed_deposits_months(BigDecimal r28_13_18_fixed_deposits_months) {
			this.r28_13_18_fixed_deposits_months = r28_13_18_fixed_deposits_months;
		}

		public BigDecimal getR28_19_24_fixed_deposits_months() {
			return r28_19_24_fixed_deposits_months;
		}

		public void setR28_19_24_fixed_deposits_months(BigDecimal r28_19_24_fixed_deposits_months) {
			this.r28_19_24_fixed_deposits_months = r28_19_24_fixed_deposits_months;
		}

		public BigDecimal getR28_over_24_fixed_deposits_months() {
			return r28_over_24_fixed_deposits_months;
		}

		public void setR28_over_24_fixed_deposits_months(BigDecimal r28_over_24_fixed_deposits_months) {
			this.r28_over_24_fixed_deposits_months = r28_over_24_fixed_deposits_months;
		}

		public BigDecimal getR28_certificates_of_deposit() {
			return r28_certificates_of_deposit;
		}

		public void setR28_certificates_of_deposit(BigDecimal r28_certificates_of_deposit) {
			this.r28_certificates_of_deposit = r28_certificates_of_deposit;
		}

		public BigDecimal getR28_total() {
			return r28_total;
		}

		public void setR28_total(BigDecimal r28_total) {
			this.r28_total = r28_total;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_current() {
			return r29_current;
		}

		public void setR29_current(BigDecimal r29_current) {
			this.r29_current = r29_current;
		}

		public BigDecimal getR29_call() {
			return r29_call;
		}

		public void setR29_call(BigDecimal r29_call) {
			this.r29_call = r29_call;
		}

		public BigDecimal getR29_savings() {
			return r29_savings;
		}

		public void setR29_savings(BigDecimal r29_savings) {
			this.r29_savings = r29_savings;
		}

		public BigDecimal getR29_0_31_notice_days() {
			return r29_0_31_notice_days;
		}

		public void setR29_0_31_notice_days(BigDecimal r29_0_31_notice_days) {
			this.r29_0_31_notice_days = r29_0_31_notice_days;
		}

		public BigDecimal getR29_32_88_notice_days() {
			return r29_32_88_notice_days;
		}

		public void setR29_32_88_notice_days(BigDecimal r29_32_88_notice_days) {
			this.r29_32_88_notice_days = r29_32_88_notice_days;
		}

		public BigDecimal getR29_91_day_deposit_fixed_deposit_months() {
			return r29_91_day_deposit_fixed_deposit_months;
		}

		public void setR29_91_day_deposit_fixed_deposit_months(BigDecimal r29_91_day_deposit_fixed_deposit_months) {
			this.r29_91_day_deposit_fixed_deposit_months = r29_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR29_1_2_fixed_deposits_months() {
			return r29_1_2_fixed_deposits_months;
		}

		public void setR29_1_2_fixed_deposits_months(BigDecimal r29_1_2_fixed_deposits_months) {
			this.r29_1_2_fixed_deposits_months = r29_1_2_fixed_deposits_months;
		}

		public BigDecimal getR29_4_6_fixed_deposits_months() {
			return r29_4_6_fixed_deposits_months;
		}

		public void setR29_4_6_fixed_deposits_months(BigDecimal r29_4_6_fixed_deposits_months) {
			this.r29_4_6_fixed_deposits_months = r29_4_6_fixed_deposits_months;
		}

		public BigDecimal getR29_7_12_fixed_deposits_months() {
			return r29_7_12_fixed_deposits_months;
		}

		public void setR29_7_12_fixed_deposits_months(BigDecimal r29_7_12_fixed_deposits_months) {
			this.r29_7_12_fixed_deposits_months = r29_7_12_fixed_deposits_months;
		}

		public BigDecimal getR29_13_18_fixed_deposits_months() {
			return r29_13_18_fixed_deposits_months;
		}

		public void setR29_13_18_fixed_deposits_months(BigDecimal r29_13_18_fixed_deposits_months) {
			this.r29_13_18_fixed_deposits_months = r29_13_18_fixed_deposits_months;
		}

		public BigDecimal getR29_19_24_fixed_deposits_months() {
			return r29_19_24_fixed_deposits_months;
		}

		public void setR29_19_24_fixed_deposits_months(BigDecimal r29_19_24_fixed_deposits_months) {
			this.r29_19_24_fixed_deposits_months = r29_19_24_fixed_deposits_months;
		}

		public BigDecimal getR29_over_24_fixed_deposits_months() {
			return r29_over_24_fixed_deposits_months;
		}

		public void setR29_over_24_fixed_deposits_months(BigDecimal r29_over_24_fixed_deposits_months) {
			this.r29_over_24_fixed_deposits_months = r29_over_24_fixed_deposits_months;
		}

		public BigDecimal getR29_certificates_of_deposit() {
			return r29_certificates_of_deposit;
		}

		public void setR29_certificates_of_deposit(BigDecimal r29_certificates_of_deposit) {
			this.r29_certificates_of_deposit = r29_certificates_of_deposit;
		}

		public BigDecimal getR29_total() {
			return r29_total;
		}

		public void setR29_total(BigDecimal r29_total) {
			this.r29_total = r29_total;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_current() {
			return r30_current;
		}

		public void setR30_current(BigDecimal r30_current) {
			this.r30_current = r30_current;
		}

		public BigDecimal getR30_call() {
			return r30_call;
		}

		public void setR30_call(BigDecimal r30_call) {
			this.r30_call = r30_call;
		}

		public BigDecimal getR30_savings() {
			return r30_savings;
		}

		public void setR30_savings(BigDecimal r30_savings) {
			this.r30_savings = r30_savings;
		}

		public BigDecimal getR30_0_31_notice_days() {
			return r30_0_31_notice_days;
		}

		public void setR30_0_31_notice_days(BigDecimal r30_0_31_notice_days) {
			this.r30_0_31_notice_days = r30_0_31_notice_days;
		}

		public BigDecimal getR30_32_88_notice_days() {
			return r30_32_88_notice_days;
		}

		public void setR30_32_88_notice_days(BigDecimal r30_32_88_notice_days) {
			this.r30_32_88_notice_days = r30_32_88_notice_days;
		}

		public BigDecimal getR30_91_day_deposit_fixed_deposit_months() {
			return r30_91_day_deposit_fixed_deposit_months;
		}

		public void setR30_91_day_deposit_fixed_deposit_months(BigDecimal r30_91_day_deposit_fixed_deposit_months) {
			this.r30_91_day_deposit_fixed_deposit_months = r30_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR30_1_2_fixed_deposits_months() {
			return r30_1_2_fixed_deposits_months;
		}

		public void setR30_1_2_fixed_deposits_months(BigDecimal r30_1_2_fixed_deposits_months) {
			this.r30_1_2_fixed_deposits_months = r30_1_2_fixed_deposits_months;
		}

		public BigDecimal getR30_4_6_fixed_deposits_months() {
			return r30_4_6_fixed_deposits_months;
		}

		public void setR30_4_6_fixed_deposits_months(BigDecimal r30_4_6_fixed_deposits_months) {
			this.r30_4_6_fixed_deposits_months = r30_4_6_fixed_deposits_months;
		}

		public BigDecimal getR30_7_12_fixed_deposits_months() {
			return r30_7_12_fixed_deposits_months;
		}

		public void setR30_7_12_fixed_deposits_months(BigDecimal r30_7_12_fixed_deposits_months) {
			this.r30_7_12_fixed_deposits_months = r30_7_12_fixed_deposits_months;
		}

		public BigDecimal getR30_13_18_fixed_deposits_months() {
			return r30_13_18_fixed_deposits_months;
		}

		public void setR30_13_18_fixed_deposits_months(BigDecimal r30_13_18_fixed_deposits_months) {
			this.r30_13_18_fixed_deposits_months = r30_13_18_fixed_deposits_months;
		}

		public BigDecimal getR30_19_24_fixed_deposits_months() {
			return r30_19_24_fixed_deposits_months;
		}

		public void setR30_19_24_fixed_deposits_months(BigDecimal r30_19_24_fixed_deposits_months) {
			this.r30_19_24_fixed_deposits_months = r30_19_24_fixed_deposits_months;
		}

		public BigDecimal getR30_over_24_fixed_deposits_months() {
			return r30_over_24_fixed_deposits_months;
		}

		public void setR30_over_24_fixed_deposits_months(BigDecimal r30_over_24_fixed_deposits_months) {
			this.r30_over_24_fixed_deposits_months = r30_over_24_fixed_deposits_months;
		}

		public BigDecimal getR30_certificates_of_deposit() {
			return r30_certificates_of_deposit;
		}

		public void setR30_certificates_of_deposit(BigDecimal r30_certificates_of_deposit) {
			this.r30_certificates_of_deposit = r30_certificates_of_deposit;
		}

		public BigDecimal getR30_total() {
			return r30_total;
		}

		public void setR30_total(BigDecimal r30_total) {
			this.r30_total = r30_total;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_current() {
			return r31_current;
		}

		public void setR31_current(BigDecimal r31_current) {
			this.r31_current = r31_current;
		}

		public BigDecimal getR31_call() {
			return r31_call;
		}

		public void setR31_call(BigDecimal r31_call) {
			this.r31_call = r31_call;
		}

		public BigDecimal getR31_savings() {
			return r31_savings;
		}

		public void setR31_savings(BigDecimal r31_savings) {
			this.r31_savings = r31_savings;
		}

		public BigDecimal getR31_0_31_notice_days() {
			return r31_0_31_notice_days;
		}

		public void setR31_0_31_notice_days(BigDecimal r31_0_31_notice_days) {
			this.r31_0_31_notice_days = r31_0_31_notice_days;
		}

		public BigDecimal getR31_32_88_notice_days() {
			return r31_32_88_notice_days;
		}

		public void setR31_32_88_notice_days(BigDecimal r31_32_88_notice_days) {
			this.r31_32_88_notice_days = r31_32_88_notice_days;
		}

		public BigDecimal getR31_91_day_deposit_fixed_deposit_months() {
			return r31_91_day_deposit_fixed_deposit_months;
		}

		public void setR31_91_day_deposit_fixed_deposit_months(BigDecimal r31_91_day_deposit_fixed_deposit_months) {
			this.r31_91_day_deposit_fixed_deposit_months = r31_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR31_1_2_fixed_deposits_months() {
			return r31_1_2_fixed_deposits_months;
		}

		public void setR31_1_2_fixed_deposits_months(BigDecimal r31_1_2_fixed_deposits_months) {
			this.r31_1_2_fixed_deposits_months = r31_1_2_fixed_deposits_months;
		}

		public BigDecimal getR31_4_6_fixed_deposits_months() {
			return r31_4_6_fixed_deposits_months;
		}

		public void setR31_4_6_fixed_deposits_months(BigDecimal r31_4_6_fixed_deposits_months) {
			this.r31_4_6_fixed_deposits_months = r31_4_6_fixed_deposits_months;
		}

		public BigDecimal getR31_7_12_fixed_deposits_months() {
			return r31_7_12_fixed_deposits_months;
		}

		public void setR31_7_12_fixed_deposits_months(BigDecimal r31_7_12_fixed_deposits_months) {
			this.r31_7_12_fixed_deposits_months = r31_7_12_fixed_deposits_months;
		}

		public BigDecimal getR31_13_18_fixed_deposits_months() {
			return r31_13_18_fixed_deposits_months;
		}

		public void setR31_13_18_fixed_deposits_months(BigDecimal r31_13_18_fixed_deposits_months) {
			this.r31_13_18_fixed_deposits_months = r31_13_18_fixed_deposits_months;
		}

		public BigDecimal getR31_19_24_fixed_deposits_months() {
			return r31_19_24_fixed_deposits_months;
		}

		public void setR31_19_24_fixed_deposits_months(BigDecimal r31_19_24_fixed_deposits_months) {
			this.r31_19_24_fixed_deposits_months = r31_19_24_fixed_deposits_months;
		}

		public BigDecimal getR31_over_24_fixed_deposits_months() {
			return r31_over_24_fixed_deposits_months;
		}

		public void setR31_over_24_fixed_deposits_months(BigDecimal r31_over_24_fixed_deposits_months) {
			this.r31_over_24_fixed_deposits_months = r31_over_24_fixed_deposits_months;
		}

		public BigDecimal getR31_certificates_of_deposit() {
			return r31_certificates_of_deposit;
		}

		public void setR31_certificates_of_deposit(BigDecimal r31_certificates_of_deposit) {
			this.r31_certificates_of_deposit = r31_certificates_of_deposit;
		}

		public BigDecimal getR31_total() {
			return r31_total;
		}

		public void setR31_total(BigDecimal r31_total) {
			this.r31_total = r31_total;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_current() {
			return r32_current;
		}

		public void setR32_current(BigDecimal r32_current) {
			this.r32_current = r32_current;
		}

		public BigDecimal getR32_call() {
			return r32_call;
		}

		public void setR32_call(BigDecimal r32_call) {
			this.r32_call = r32_call;
		}

		public BigDecimal getR32_savings() {
			return r32_savings;
		}

		public void setR32_savings(BigDecimal r32_savings) {
			this.r32_savings = r32_savings;
		}

		public BigDecimal getR32_0_31_notice_days() {
			return r32_0_31_notice_days;
		}

		public void setR32_0_31_notice_days(BigDecimal r32_0_31_notice_days) {
			this.r32_0_31_notice_days = r32_0_31_notice_days;
		}

		public BigDecimal getR32_32_88_notice_days() {
			return r32_32_88_notice_days;
		}

		public void setR32_32_88_notice_days(BigDecimal r32_32_88_notice_days) {
			this.r32_32_88_notice_days = r32_32_88_notice_days;
		}

		public BigDecimal getR32_91_day_deposit_fixed_deposit_months() {
			return r32_91_day_deposit_fixed_deposit_months;
		}

		public void setR32_91_day_deposit_fixed_deposit_months(BigDecimal r32_91_day_deposit_fixed_deposit_months) {
			this.r32_91_day_deposit_fixed_deposit_months = r32_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR32_1_2_fixed_deposits_months() {
			return r32_1_2_fixed_deposits_months;
		}

		public void setR32_1_2_fixed_deposits_months(BigDecimal r32_1_2_fixed_deposits_months) {
			this.r32_1_2_fixed_deposits_months = r32_1_2_fixed_deposits_months;
		}

		public BigDecimal getR32_4_6_fixed_deposits_months() {
			return r32_4_6_fixed_deposits_months;
		}

		public void setR32_4_6_fixed_deposits_months(BigDecimal r32_4_6_fixed_deposits_months) {
			this.r32_4_6_fixed_deposits_months = r32_4_6_fixed_deposits_months;
		}

		public BigDecimal getR32_7_12_fixed_deposits_months() {
			return r32_7_12_fixed_deposits_months;
		}

		public void setR32_7_12_fixed_deposits_months(BigDecimal r32_7_12_fixed_deposits_months) {
			this.r32_7_12_fixed_deposits_months = r32_7_12_fixed_deposits_months;
		}

		public BigDecimal getR32_13_18_fixed_deposits_months() {
			return r32_13_18_fixed_deposits_months;
		}

		public void setR32_13_18_fixed_deposits_months(BigDecimal r32_13_18_fixed_deposits_months) {
			this.r32_13_18_fixed_deposits_months = r32_13_18_fixed_deposits_months;
		}

		public BigDecimal getR32_19_24_fixed_deposits_months() {
			return r32_19_24_fixed_deposits_months;
		}

		public void setR32_19_24_fixed_deposits_months(BigDecimal r32_19_24_fixed_deposits_months) {
			this.r32_19_24_fixed_deposits_months = r32_19_24_fixed_deposits_months;
		}

		public BigDecimal getR32_over_24_fixed_deposits_months() {
			return r32_over_24_fixed_deposits_months;
		}

		public void setR32_over_24_fixed_deposits_months(BigDecimal r32_over_24_fixed_deposits_months) {
			this.r32_over_24_fixed_deposits_months = r32_over_24_fixed_deposits_months;
		}

		public BigDecimal getR32_certificates_of_deposit() {
			return r32_certificates_of_deposit;
		}

		public void setR32_certificates_of_deposit(BigDecimal r32_certificates_of_deposit) {
			this.r32_certificates_of_deposit = r32_certificates_of_deposit;
		}

		public BigDecimal getR32_total() {
			return r32_total;
		}

		public void setR32_total(BigDecimal r32_total) {
			this.r32_total = r32_total;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_current() {
			return r33_current;
		}

		public void setR33_current(BigDecimal r33_current) {
			this.r33_current = r33_current;
		}

		public BigDecimal getR33_call() {
			return r33_call;
		}

		public void setR33_call(BigDecimal r33_call) {
			this.r33_call = r33_call;
		}

		public BigDecimal getR33_savings() {
			return r33_savings;
		}

		public void setR33_savings(BigDecimal r33_savings) {
			this.r33_savings = r33_savings;
		}

		public BigDecimal getR33_0_31_notice_days() {
			return r33_0_31_notice_days;
		}

		public void setR33_0_31_notice_days(BigDecimal r33_0_31_notice_days) {
			this.r33_0_31_notice_days = r33_0_31_notice_days;
		}

		public BigDecimal getR33_32_88_notice_days() {
			return r33_32_88_notice_days;
		}

		public void setR33_32_88_notice_days(BigDecimal r33_32_88_notice_days) {
			this.r33_32_88_notice_days = r33_32_88_notice_days;
		}

		public BigDecimal getR33_91_day_deposit_fixed_deposit_months() {
			return r33_91_day_deposit_fixed_deposit_months;
		}

		public void setR33_91_day_deposit_fixed_deposit_months(BigDecimal r33_91_day_deposit_fixed_deposit_months) {
			this.r33_91_day_deposit_fixed_deposit_months = r33_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR33_1_2_fixed_deposits_months() {
			return r33_1_2_fixed_deposits_months;
		}

		public void setR33_1_2_fixed_deposits_months(BigDecimal r33_1_2_fixed_deposits_months) {
			this.r33_1_2_fixed_deposits_months = r33_1_2_fixed_deposits_months;
		}

		public BigDecimal getR33_4_6_fixed_deposits_months() {
			return r33_4_6_fixed_deposits_months;
		}

		public void setR33_4_6_fixed_deposits_months(BigDecimal r33_4_6_fixed_deposits_months) {
			this.r33_4_6_fixed_deposits_months = r33_4_6_fixed_deposits_months;
		}

		public BigDecimal getR33_7_12_fixed_deposits_months() {
			return r33_7_12_fixed_deposits_months;
		}

		public void setR33_7_12_fixed_deposits_months(BigDecimal r33_7_12_fixed_deposits_months) {
			this.r33_7_12_fixed_deposits_months = r33_7_12_fixed_deposits_months;
		}

		public BigDecimal getR33_13_18_fixed_deposits_months() {
			return r33_13_18_fixed_deposits_months;
		}

		public void setR33_13_18_fixed_deposits_months(BigDecimal r33_13_18_fixed_deposits_months) {
			this.r33_13_18_fixed_deposits_months = r33_13_18_fixed_deposits_months;
		}

		public BigDecimal getR33_19_24_fixed_deposits_months() {
			return r33_19_24_fixed_deposits_months;
		}

		public void setR33_19_24_fixed_deposits_months(BigDecimal r33_19_24_fixed_deposits_months) {
			this.r33_19_24_fixed_deposits_months = r33_19_24_fixed_deposits_months;
		}

		public BigDecimal getR33_over_24_fixed_deposits_months() {
			return r33_over_24_fixed_deposits_months;
		}

		public void setR33_over_24_fixed_deposits_months(BigDecimal r33_over_24_fixed_deposits_months) {
			this.r33_over_24_fixed_deposits_months = r33_over_24_fixed_deposits_months;
		}

		public BigDecimal getR33_certificates_of_deposit() {
			return r33_certificates_of_deposit;
		}

		public void setR33_certificates_of_deposit(BigDecimal r33_certificates_of_deposit) {
			this.r33_certificates_of_deposit = r33_certificates_of_deposit;
		}

		public BigDecimal getR33_total() {
			return r33_total;
		}

		public void setR33_total(BigDecimal r33_total) {
			this.r33_total = r33_total;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_current() {
			return r34_current;
		}

		public void setR34_current(BigDecimal r34_current) {
			this.r34_current = r34_current;
		}

		public BigDecimal getR34_call() {
			return r34_call;
		}

		public void setR34_call(BigDecimal r34_call) {
			this.r34_call = r34_call;
		}

		public BigDecimal getR34_savings() {
			return r34_savings;
		}

		public void setR34_savings(BigDecimal r34_savings) {
			this.r34_savings = r34_savings;
		}

		public BigDecimal getR34_0_31_notice_days() {
			return r34_0_31_notice_days;
		}

		public void setR34_0_31_notice_days(BigDecimal r34_0_31_notice_days) {
			this.r34_0_31_notice_days = r34_0_31_notice_days;
		}

		public BigDecimal getR34_32_88_notice_days() {
			return r34_32_88_notice_days;
		}

		public void setR34_32_88_notice_days(BigDecimal r34_32_88_notice_days) {
			this.r34_32_88_notice_days = r34_32_88_notice_days;
		}

		public BigDecimal getR34_91_day_deposit_fixed_deposit_months() {
			return r34_91_day_deposit_fixed_deposit_months;
		}

		public void setR34_91_day_deposit_fixed_deposit_months(BigDecimal r34_91_day_deposit_fixed_deposit_months) {
			this.r34_91_day_deposit_fixed_deposit_months = r34_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR34_1_2_fixed_deposits_months() {
			return r34_1_2_fixed_deposits_months;
		}

		public void setR34_1_2_fixed_deposits_months(BigDecimal r34_1_2_fixed_deposits_months) {
			this.r34_1_2_fixed_deposits_months = r34_1_2_fixed_deposits_months;
		}

		public BigDecimal getR34_4_6_fixed_deposits_months() {
			return r34_4_6_fixed_deposits_months;
		}

		public void setR34_4_6_fixed_deposits_months(BigDecimal r34_4_6_fixed_deposits_months) {
			this.r34_4_6_fixed_deposits_months = r34_4_6_fixed_deposits_months;
		}

		public BigDecimal getR34_7_12_fixed_deposits_months() {
			return r34_7_12_fixed_deposits_months;
		}

		public void setR34_7_12_fixed_deposits_months(BigDecimal r34_7_12_fixed_deposits_months) {
			this.r34_7_12_fixed_deposits_months = r34_7_12_fixed_deposits_months;
		}

		public BigDecimal getR34_13_18_fixed_deposits_months() {
			return r34_13_18_fixed_deposits_months;
		}

		public void setR34_13_18_fixed_deposits_months(BigDecimal r34_13_18_fixed_deposits_months) {
			this.r34_13_18_fixed_deposits_months = r34_13_18_fixed_deposits_months;
		}

		public BigDecimal getR34_19_24_fixed_deposits_months() {
			return r34_19_24_fixed_deposits_months;
		}

		public void setR34_19_24_fixed_deposits_months(BigDecimal r34_19_24_fixed_deposits_months) {
			this.r34_19_24_fixed_deposits_months = r34_19_24_fixed_deposits_months;
		}

		public BigDecimal getR34_over_24_fixed_deposits_months() {
			return r34_over_24_fixed_deposits_months;
		}

		public void setR34_over_24_fixed_deposits_months(BigDecimal r34_over_24_fixed_deposits_months) {
			this.r34_over_24_fixed_deposits_months = r34_over_24_fixed_deposits_months;
		}

		public BigDecimal getR34_certificates_of_deposit() {
			return r34_certificates_of_deposit;
		}

		public void setR34_certificates_of_deposit(BigDecimal r34_certificates_of_deposit) {
			this.r34_certificates_of_deposit = r34_certificates_of_deposit;
		}

		public BigDecimal getR34_total() {
			return r34_total;
		}

		public void setR34_total(BigDecimal r34_total) {
			this.r34_total = r34_total;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_current() {
			return r35_current;
		}

		public void setR35_current(BigDecimal r35_current) {
			this.r35_current = r35_current;
		}

		public BigDecimal getR35_call() {
			return r35_call;
		}

		public void setR35_call(BigDecimal r35_call) {
			this.r35_call = r35_call;
		}

		public BigDecimal getR35_savings() {
			return r35_savings;
		}

		public void setR35_savings(BigDecimal r35_savings) {
			this.r35_savings = r35_savings;
		}

		public BigDecimal getR35_0_31_notice_days() {
			return r35_0_31_notice_days;
		}

		public void setR35_0_31_notice_days(BigDecimal r35_0_31_notice_days) {
			this.r35_0_31_notice_days = r35_0_31_notice_days;
		}

		public BigDecimal getR35_32_88_notice_days() {
			return r35_32_88_notice_days;
		}

		public void setR35_32_88_notice_days(BigDecimal r35_32_88_notice_days) {
			this.r35_32_88_notice_days = r35_32_88_notice_days;
		}

		public BigDecimal getR35_91_day_deposit_fixed_deposit_months() {
			return r35_91_day_deposit_fixed_deposit_months;
		}

		public void setR35_91_day_deposit_fixed_deposit_months(BigDecimal r35_91_day_deposit_fixed_deposit_months) {
			this.r35_91_day_deposit_fixed_deposit_months = r35_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR35_1_2_fixed_deposits_months() {
			return r35_1_2_fixed_deposits_months;
		}

		public void setR35_1_2_fixed_deposits_months(BigDecimal r35_1_2_fixed_deposits_months) {
			this.r35_1_2_fixed_deposits_months = r35_1_2_fixed_deposits_months;
		}

		public BigDecimal getR35_4_6_fixed_deposits_months() {
			return r35_4_6_fixed_deposits_months;
		}

		public void setR35_4_6_fixed_deposits_months(BigDecimal r35_4_6_fixed_deposits_months) {
			this.r35_4_6_fixed_deposits_months = r35_4_6_fixed_deposits_months;
		}

		public BigDecimal getR35_7_12_fixed_deposits_months() {
			return r35_7_12_fixed_deposits_months;
		}

		public void setR35_7_12_fixed_deposits_months(BigDecimal r35_7_12_fixed_deposits_months) {
			this.r35_7_12_fixed_deposits_months = r35_7_12_fixed_deposits_months;
		}

		public BigDecimal getR35_13_18_fixed_deposits_months() {
			return r35_13_18_fixed_deposits_months;
		}

		public void setR35_13_18_fixed_deposits_months(BigDecimal r35_13_18_fixed_deposits_months) {
			this.r35_13_18_fixed_deposits_months = r35_13_18_fixed_deposits_months;
		}

		public BigDecimal getR35_19_24_fixed_deposits_months() {
			return r35_19_24_fixed_deposits_months;
		}

		public void setR35_19_24_fixed_deposits_months(BigDecimal r35_19_24_fixed_deposits_months) {
			this.r35_19_24_fixed_deposits_months = r35_19_24_fixed_deposits_months;
		}

		public BigDecimal getR35_over_24_fixed_deposits_months() {
			return r35_over_24_fixed_deposits_months;
		}

		public void setR35_over_24_fixed_deposits_months(BigDecimal r35_over_24_fixed_deposits_months) {
			this.r35_over_24_fixed_deposits_months = r35_over_24_fixed_deposits_months;
		}

		public BigDecimal getR35_certificates_of_deposit() {
			return r35_certificates_of_deposit;
		}

		public void setR35_certificates_of_deposit(BigDecimal r35_certificates_of_deposit) {
			this.r35_certificates_of_deposit = r35_certificates_of_deposit;
		}

		public BigDecimal getR35_total() {
			return r35_total;
		}

		public void setR35_total(BigDecimal r35_total) {
			this.r35_total = r35_total;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_current() {
			return r36_current;
		}

		public void setR36_current(BigDecimal r36_current) {
			this.r36_current = r36_current;
		}

		public BigDecimal getR36_call() {
			return r36_call;
		}

		public void setR36_call(BigDecimal r36_call) {
			this.r36_call = r36_call;
		}

		public BigDecimal getR36_savings() {
			return r36_savings;
		}

		public void setR36_savings(BigDecimal r36_savings) {
			this.r36_savings = r36_savings;
		}

		public BigDecimal getR36_0_31_notice_days() {
			return r36_0_31_notice_days;
		}

		public void setR36_0_31_notice_days(BigDecimal r36_0_31_notice_days) {
			this.r36_0_31_notice_days = r36_0_31_notice_days;
		}

		public BigDecimal getR36_32_88_notice_days() {
			return r36_32_88_notice_days;
		}

		public void setR36_32_88_notice_days(BigDecimal r36_32_88_notice_days) {
			this.r36_32_88_notice_days = r36_32_88_notice_days;
		}

		public BigDecimal getR36_91_day_deposit_fixed_deposit_months() {
			return r36_91_day_deposit_fixed_deposit_months;
		}

		public void setR36_91_day_deposit_fixed_deposit_months(BigDecimal r36_91_day_deposit_fixed_deposit_months) {
			this.r36_91_day_deposit_fixed_deposit_months = r36_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR36_1_2_fixed_deposits_months() {
			return r36_1_2_fixed_deposits_months;
		}

		public void setR36_1_2_fixed_deposits_months(BigDecimal r36_1_2_fixed_deposits_months) {
			this.r36_1_2_fixed_deposits_months = r36_1_2_fixed_deposits_months;
		}

		public BigDecimal getR36_4_6_fixed_deposits_months() {
			return r36_4_6_fixed_deposits_months;
		}

		public void setR36_4_6_fixed_deposits_months(BigDecimal r36_4_6_fixed_deposits_months) {
			this.r36_4_6_fixed_deposits_months = r36_4_6_fixed_deposits_months;
		}

		public BigDecimal getR36_7_12_fixed_deposits_months() {
			return r36_7_12_fixed_deposits_months;
		}

		public void setR36_7_12_fixed_deposits_months(BigDecimal r36_7_12_fixed_deposits_months) {
			this.r36_7_12_fixed_deposits_months = r36_7_12_fixed_deposits_months;
		}

		public BigDecimal getR36_13_18_fixed_deposits_months() {
			return r36_13_18_fixed_deposits_months;
		}

		public void setR36_13_18_fixed_deposits_months(BigDecimal r36_13_18_fixed_deposits_months) {
			this.r36_13_18_fixed_deposits_months = r36_13_18_fixed_deposits_months;
		}

		public BigDecimal getR36_19_24_fixed_deposits_months() {
			return r36_19_24_fixed_deposits_months;
		}

		public void setR36_19_24_fixed_deposits_months(BigDecimal r36_19_24_fixed_deposits_months) {
			this.r36_19_24_fixed_deposits_months = r36_19_24_fixed_deposits_months;
		}

		public BigDecimal getR36_over_24_fixed_deposits_months() {
			return r36_over_24_fixed_deposits_months;
		}

		public void setR36_over_24_fixed_deposits_months(BigDecimal r36_over_24_fixed_deposits_months) {
			this.r36_over_24_fixed_deposits_months = r36_over_24_fixed_deposits_months;
		}

		public BigDecimal getR36_certificates_of_deposit() {
			return r36_certificates_of_deposit;
		}

		public void setR36_certificates_of_deposit(BigDecimal r36_certificates_of_deposit) {
			this.r36_certificates_of_deposit = r36_certificates_of_deposit;
		}

		public BigDecimal getR36_total() {
			return r36_total;
		}

		public void setR36_total(BigDecimal r36_total) {
			this.r36_total = r36_total;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_current() {
			return r37_current;
		}

		public void setR37_current(BigDecimal r37_current) {
			this.r37_current = r37_current;
		}

		public BigDecimal getR37_call() {
			return r37_call;
		}

		public void setR37_call(BigDecimal r37_call) {
			this.r37_call = r37_call;
		}

		public BigDecimal getR37_savings() {
			return r37_savings;
		}

		public void setR37_savings(BigDecimal r37_savings) {
			this.r37_savings = r37_savings;
		}

		public BigDecimal getR37_0_31_notice_days() {
			return r37_0_31_notice_days;
		}

		public void setR37_0_31_notice_days(BigDecimal r37_0_31_notice_days) {
			this.r37_0_31_notice_days = r37_0_31_notice_days;
		}

		public BigDecimal getR37_32_88_notice_days() {
			return r37_32_88_notice_days;
		}

		public void setR37_32_88_notice_days(BigDecimal r37_32_88_notice_days) {
			this.r37_32_88_notice_days = r37_32_88_notice_days;
		}

		public BigDecimal getR37_91_day_deposit_fixed_deposit_months() {
			return r37_91_day_deposit_fixed_deposit_months;
		}

		public void setR37_91_day_deposit_fixed_deposit_months(BigDecimal r37_91_day_deposit_fixed_deposit_months) {
			this.r37_91_day_deposit_fixed_deposit_months = r37_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR37_1_2_fixed_deposits_months() {
			return r37_1_2_fixed_deposits_months;
		}

		public void setR37_1_2_fixed_deposits_months(BigDecimal r37_1_2_fixed_deposits_months) {
			this.r37_1_2_fixed_deposits_months = r37_1_2_fixed_deposits_months;
		}

		public BigDecimal getR37_4_6_fixed_deposits_months() {
			return r37_4_6_fixed_deposits_months;
		}

		public void setR37_4_6_fixed_deposits_months(BigDecimal r37_4_6_fixed_deposits_months) {
			this.r37_4_6_fixed_deposits_months = r37_4_6_fixed_deposits_months;
		}

		public BigDecimal getR37_7_12_fixed_deposits_months() {
			return r37_7_12_fixed_deposits_months;
		}

		public void setR37_7_12_fixed_deposits_months(BigDecimal r37_7_12_fixed_deposits_months) {
			this.r37_7_12_fixed_deposits_months = r37_7_12_fixed_deposits_months;
		}

		public BigDecimal getR37_13_18_fixed_deposits_months() {
			return r37_13_18_fixed_deposits_months;
		}

		public void setR37_13_18_fixed_deposits_months(BigDecimal r37_13_18_fixed_deposits_months) {
			this.r37_13_18_fixed_deposits_months = r37_13_18_fixed_deposits_months;
		}

		public BigDecimal getR37_19_24_fixed_deposits_months() {
			return r37_19_24_fixed_deposits_months;
		}

		public void setR37_19_24_fixed_deposits_months(BigDecimal r37_19_24_fixed_deposits_months) {
			this.r37_19_24_fixed_deposits_months = r37_19_24_fixed_deposits_months;
		}

		public BigDecimal getR37_over_24_fixed_deposits_months() {
			return r37_over_24_fixed_deposits_months;
		}

		public void setR37_over_24_fixed_deposits_months(BigDecimal r37_over_24_fixed_deposits_months) {
			this.r37_over_24_fixed_deposits_months = r37_over_24_fixed_deposits_months;
		}

		public BigDecimal getR37_certificates_of_deposit() {
			return r37_certificates_of_deposit;
		}

		public void setR37_certificates_of_deposit(BigDecimal r37_certificates_of_deposit) {
			this.r37_certificates_of_deposit = r37_certificates_of_deposit;
		}

		public BigDecimal getR37_total() {
			return r37_total;
		}

		public void setR37_total(BigDecimal r37_total) {
			this.r37_total = r37_total;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_current() {
			return r38_current;
		}

		public void setR38_current(BigDecimal r38_current) {
			this.r38_current = r38_current;
		}

		public BigDecimal getR38_call() {
			return r38_call;
		}

		public void setR38_call(BigDecimal r38_call) {
			this.r38_call = r38_call;
		}

		public BigDecimal getR38_savings() {
			return r38_savings;
		}

		public void setR38_savings(BigDecimal r38_savings) {
			this.r38_savings = r38_savings;
		}

		public BigDecimal getR38_0_31_notice_days() {
			return r38_0_31_notice_days;
		}

		public void setR38_0_31_notice_days(BigDecimal r38_0_31_notice_days) {
			this.r38_0_31_notice_days = r38_0_31_notice_days;
		}

		public BigDecimal getR38_32_88_notice_days() {
			return r38_32_88_notice_days;
		}

		public void setR38_32_88_notice_days(BigDecimal r38_32_88_notice_days) {
			this.r38_32_88_notice_days = r38_32_88_notice_days;
		}

		public BigDecimal getR38_91_day_deposit_fixed_deposit_months() {
			return r38_91_day_deposit_fixed_deposit_months;
		}

		public void setR38_91_day_deposit_fixed_deposit_months(BigDecimal r38_91_day_deposit_fixed_deposit_months) {
			this.r38_91_day_deposit_fixed_deposit_months = r38_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR38_1_2_fixed_deposits_months() {
			return r38_1_2_fixed_deposits_months;
		}

		public void setR38_1_2_fixed_deposits_months(BigDecimal r38_1_2_fixed_deposits_months) {
			this.r38_1_2_fixed_deposits_months = r38_1_2_fixed_deposits_months;
		}

		public BigDecimal getR38_4_6_fixed_deposits_months() {
			return r38_4_6_fixed_deposits_months;
		}

		public void setR38_4_6_fixed_deposits_months(BigDecimal r38_4_6_fixed_deposits_months) {
			this.r38_4_6_fixed_deposits_months = r38_4_6_fixed_deposits_months;
		}

		public BigDecimal getR38_7_12_fixed_deposits_months() {
			return r38_7_12_fixed_deposits_months;
		}

		public void setR38_7_12_fixed_deposits_months(BigDecimal r38_7_12_fixed_deposits_months) {
			this.r38_7_12_fixed_deposits_months = r38_7_12_fixed_deposits_months;
		}

		public BigDecimal getR38_13_18_fixed_deposits_months() {
			return r38_13_18_fixed_deposits_months;
		}

		public void setR38_13_18_fixed_deposits_months(BigDecimal r38_13_18_fixed_deposits_months) {
			this.r38_13_18_fixed_deposits_months = r38_13_18_fixed_deposits_months;
		}

		public BigDecimal getR38_19_24_fixed_deposits_months() {
			return r38_19_24_fixed_deposits_months;
		}

		public void setR38_19_24_fixed_deposits_months(BigDecimal r38_19_24_fixed_deposits_months) {
			this.r38_19_24_fixed_deposits_months = r38_19_24_fixed_deposits_months;
		}

		public BigDecimal getR38_over_24_fixed_deposits_months() {
			return r38_over_24_fixed_deposits_months;
		}

		public void setR38_over_24_fixed_deposits_months(BigDecimal r38_over_24_fixed_deposits_months) {
			this.r38_over_24_fixed_deposits_months = r38_over_24_fixed_deposits_months;
		}

		public BigDecimal getR38_certificates_of_deposit() {
			return r38_certificates_of_deposit;
		}

		public void setR38_certificates_of_deposit(BigDecimal r38_certificates_of_deposit) {
			this.r38_certificates_of_deposit = r38_certificates_of_deposit;
		}

		public BigDecimal getR38_total() {
			return r38_total;
		}

		public void setR38_total(BigDecimal r38_total) {
			this.r38_total = r38_total;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_current() {
			return r39_current;
		}

		public void setR39_current(BigDecimal r39_current) {
			this.r39_current = r39_current;
		}

		public BigDecimal getR39_call() {
			return r39_call;
		}

		public void setR39_call(BigDecimal r39_call) {
			this.r39_call = r39_call;
		}

		public BigDecimal getR39_savings() {
			return r39_savings;
		}

		public void setR39_savings(BigDecimal r39_savings) {
			this.r39_savings = r39_savings;
		}

		public BigDecimal getR39_0_31_notice_days() {
			return r39_0_31_notice_days;
		}

		public void setR39_0_31_notice_days(BigDecimal r39_0_31_notice_days) {
			this.r39_0_31_notice_days = r39_0_31_notice_days;
		}

		public BigDecimal getR39_32_88_notice_days() {
			return r39_32_88_notice_days;
		}

		public void setR39_32_88_notice_days(BigDecimal r39_32_88_notice_days) {
			this.r39_32_88_notice_days = r39_32_88_notice_days;
		}

		public BigDecimal getR39_91_day_deposit_fixed_deposit_months() {
			return r39_91_day_deposit_fixed_deposit_months;
		}

		public void setR39_91_day_deposit_fixed_deposit_months(BigDecimal r39_91_day_deposit_fixed_deposit_months) {
			this.r39_91_day_deposit_fixed_deposit_months = r39_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR39_1_2_fixed_deposits_months() {
			return r39_1_2_fixed_deposits_months;
		}

		public void setR39_1_2_fixed_deposits_months(BigDecimal r39_1_2_fixed_deposits_months) {
			this.r39_1_2_fixed_deposits_months = r39_1_2_fixed_deposits_months;
		}

		public BigDecimal getR39_4_6_fixed_deposits_months() {
			return r39_4_6_fixed_deposits_months;
		}

		public void setR39_4_6_fixed_deposits_months(BigDecimal r39_4_6_fixed_deposits_months) {
			this.r39_4_6_fixed_deposits_months = r39_4_6_fixed_deposits_months;
		}

		public BigDecimal getR39_7_12_fixed_deposits_months() {
			return r39_7_12_fixed_deposits_months;
		}

		public void setR39_7_12_fixed_deposits_months(BigDecimal r39_7_12_fixed_deposits_months) {
			this.r39_7_12_fixed_deposits_months = r39_7_12_fixed_deposits_months;
		}

		public BigDecimal getR39_13_18_fixed_deposits_months() {
			return r39_13_18_fixed_deposits_months;
		}

		public void setR39_13_18_fixed_deposits_months(BigDecimal r39_13_18_fixed_deposits_months) {
			this.r39_13_18_fixed_deposits_months = r39_13_18_fixed_deposits_months;
		}

		public BigDecimal getR39_19_24_fixed_deposits_months() {
			return r39_19_24_fixed_deposits_months;
		}

		public void setR39_19_24_fixed_deposits_months(BigDecimal r39_19_24_fixed_deposits_months) {
			this.r39_19_24_fixed_deposits_months = r39_19_24_fixed_deposits_months;
		}

		public BigDecimal getR39_over_24_fixed_deposits_months() {
			return r39_over_24_fixed_deposits_months;
		}

		public void setR39_over_24_fixed_deposits_months(BigDecimal r39_over_24_fixed_deposits_months) {
			this.r39_over_24_fixed_deposits_months = r39_over_24_fixed_deposits_months;
		}

		public BigDecimal getR39_certificates_of_deposit() {
			return r39_certificates_of_deposit;
		}

		public void setR39_certificates_of_deposit(BigDecimal r39_certificates_of_deposit) {
			this.r39_certificates_of_deposit = r39_certificates_of_deposit;
		}

		public BigDecimal getR39_total() {
			return r39_total;
		}

		public void setR39_total(BigDecimal r39_total) {
			this.r39_total = r39_total;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_current() {
			return r40_current;
		}

		public void setR40_current(BigDecimal r40_current) {
			this.r40_current = r40_current;
		}

		public BigDecimal getR40_call() {
			return r40_call;
		}

		public void setR40_call(BigDecimal r40_call) {
			this.r40_call = r40_call;
		}

		public BigDecimal getR40_savings() {
			return r40_savings;
		}

		public void setR40_savings(BigDecimal r40_savings) {
			this.r40_savings = r40_savings;
		}

		public BigDecimal getR40_0_31_notice_days() {
			return r40_0_31_notice_days;
		}

		public void setR40_0_31_notice_days(BigDecimal r40_0_31_notice_days) {
			this.r40_0_31_notice_days = r40_0_31_notice_days;
		}

		public BigDecimal getR40_32_88_notice_days() {
			return r40_32_88_notice_days;
		}

		public void setR40_32_88_notice_days(BigDecimal r40_32_88_notice_days) {
			this.r40_32_88_notice_days = r40_32_88_notice_days;
		}

		public BigDecimal getR40_91_day_deposit_fixed_deposit_months() {
			return r40_91_day_deposit_fixed_deposit_months;
		}

		public void setR40_91_day_deposit_fixed_deposit_months(BigDecimal r40_91_day_deposit_fixed_deposit_months) {
			this.r40_91_day_deposit_fixed_deposit_months = r40_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR40_1_2_fixed_deposits_months() {
			return r40_1_2_fixed_deposits_months;
		}

		public void setR40_1_2_fixed_deposits_months(BigDecimal r40_1_2_fixed_deposits_months) {
			this.r40_1_2_fixed_deposits_months = r40_1_2_fixed_deposits_months;
		}

		public BigDecimal getR40_4_6_fixed_deposits_months() {
			return r40_4_6_fixed_deposits_months;
		}

		public void setR40_4_6_fixed_deposits_months(BigDecimal r40_4_6_fixed_deposits_months) {
			this.r40_4_6_fixed_deposits_months = r40_4_6_fixed_deposits_months;
		}

		public BigDecimal getR40_7_12_fixed_deposits_months() {
			return r40_7_12_fixed_deposits_months;
		}

		public void setR40_7_12_fixed_deposits_months(BigDecimal r40_7_12_fixed_deposits_months) {
			this.r40_7_12_fixed_deposits_months = r40_7_12_fixed_deposits_months;
		}

		public BigDecimal getR40_13_18_fixed_deposits_months() {
			return r40_13_18_fixed_deposits_months;
		}

		public void setR40_13_18_fixed_deposits_months(BigDecimal r40_13_18_fixed_deposits_months) {
			this.r40_13_18_fixed_deposits_months = r40_13_18_fixed_deposits_months;
		}

		public BigDecimal getR40_19_24_fixed_deposits_months() {
			return r40_19_24_fixed_deposits_months;
		}

		public void setR40_19_24_fixed_deposits_months(BigDecimal r40_19_24_fixed_deposits_months) {
			this.r40_19_24_fixed_deposits_months = r40_19_24_fixed_deposits_months;
		}

		public BigDecimal getR40_over_24_fixed_deposits_months() {
			return r40_over_24_fixed_deposits_months;
		}

		public void setR40_over_24_fixed_deposits_months(BigDecimal r40_over_24_fixed_deposits_months) {
			this.r40_over_24_fixed_deposits_months = r40_over_24_fixed_deposits_months;
		}

		public BigDecimal getR40_certificates_of_deposit() {
			return r40_certificates_of_deposit;
		}

		public void setR40_certificates_of_deposit(BigDecimal r40_certificates_of_deposit) {
			this.r40_certificates_of_deposit = r40_certificates_of_deposit;
		}

		public BigDecimal getR40_total() {
			return r40_total;
		}

		public void setR40_total(BigDecimal r40_total) {
			this.r40_total = r40_total;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_current() {
			return r41_current;
		}

		public void setR41_current(BigDecimal r41_current) {
			this.r41_current = r41_current;
		}

		public BigDecimal getR41_call() {
			return r41_call;
		}

		public void setR41_call(BigDecimal r41_call) {
			this.r41_call = r41_call;
		}

		public BigDecimal getR41_savings() {
			return r41_savings;
		}

		public void setR41_savings(BigDecimal r41_savings) {
			this.r41_savings = r41_savings;
		}

		public BigDecimal getR41_0_31_notice_days() {
			return r41_0_31_notice_days;
		}

		public void setR41_0_31_notice_days(BigDecimal r41_0_31_notice_days) {
			this.r41_0_31_notice_days = r41_0_31_notice_days;
		}

		public BigDecimal getR41_32_88_notice_days() {
			return r41_32_88_notice_days;
		}

		public void setR41_32_88_notice_days(BigDecimal r41_32_88_notice_days) {
			this.r41_32_88_notice_days = r41_32_88_notice_days;
		}

		public BigDecimal getR41_91_day_deposit_fixed_deposit_months() {
			return r41_91_day_deposit_fixed_deposit_months;
		}

		public void setR41_91_day_deposit_fixed_deposit_months(BigDecimal r41_91_day_deposit_fixed_deposit_months) {
			this.r41_91_day_deposit_fixed_deposit_months = r41_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR41_1_2_fixed_deposits_months() {
			return r41_1_2_fixed_deposits_months;
		}

		public void setR41_1_2_fixed_deposits_months(BigDecimal r41_1_2_fixed_deposits_months) {
			this.r41_1_2_fixed_deposits_months = r41_1_2_fixed_deposits_months;
		}

		public BigDecimal getR41_4_6_fixed_deposits_months() {
			return r41_4_6_fixed_deposits_months;
		}

		public void setR41_4_6_fixed_deposits_months(BigDecimal r41_4_6_fixed_deposits_months) {
			this.r41_4_6_fixed_deposits_months = r41_4_6_fixed_deposits_months;
		}

		public BigDecimal getR41_7_12_fixed_deposits_months() {
			return r41_7_12_fixed_deposits_months;
		}

		public void setR41_7_12_fixed_deposits_months(BigDecimal r41_7_12_fixed_deposits_months) {
			this.r41_7_12_fixed_deposits_months = r41_7_12_fixed_deposits_months;
		}

		public BigDecimal getR41_13_18_fixed_deposits_months() {
			return r41_13_18_fixed_deposits_months;
		}

		public void setR41_13_18_fixed_deposits_months(BigDecimal r41_13_18_fixed_deposits_months) {
			this.r41_13_18_fixed_deposits_months = r41_13_18_fixed_deposits_months;
		}

		public BigDecimal getR41_19_24_fixed_deposits_months() {
			return r41_19_24_fixed_deposits_months;
		}

		public void setR41_19_24_fixed_deposits_months(BigDecimal r41_19_24_fixed_deposits_months) {
			this.r41_19_24_fixed_deposits_months = r41_19_24_fixed_deposits_months;
		}

		public BigDecimal getR41_over_24_fixed_deposits_months() {
			return r41_over_24_fixed_deposits_months;
		}

		public void setR41_over_24_fixed_deposits_months(BigDecimal r41_over_24_fixed_deposits_months) {
			this.r41_over_24_fixed_deposits_months = r41_over_24_fixed_deposits_months;
		}

		public BigDecimal getR41_certificates_of_deposit() {
			return r41_certificates_of_deposit;
		}

		public void setR41_certificates_of_deposit(BigDecimal r41_certificates_of_deposit) {
			this.r41_certificates_of_deposit = r41_certificates_of_deposit;
		}

		public BigDecimal getR41_total() {
			return r41_total;
		}

		public void setR41_total(BigDecimal r41_total) {
			this.r41_total = r41_total;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_current() {
			return r42_current;
		}

		public void setR42_current(BigDecimal r42_current) {
			this.r42_current = r42_current;
		}

		public BigDecimal getR42_call() {
			return r42_call;
		}

		public void setR42_call(BigDecimal r42_call) {
			this.r42_call = r42_call;
		}

		public BigDecimal getR42_savings() {
			return r42_savings;
		}

		public void setR42_savings(BigDecimal r42_savings) {
			this.r42_savings = r42_savings;
		}

		public BigDecimal getR42_0_31_notice_days() {
			return r42_0_31_notice_days;
		}

		public void setR42_0_31_notice_days(BigDecimal r42_0_31_notice_days) {
			this.r42_0_31_notice_days = r42_0_31_notice_days;
		}

		public BigDecimal getR42_32_88_notice_days() {
			return r42_32_88_notice_days;
		}

		public void setR42_32_88_notice_days(BigDecimal r42_32_88_notice_days) {
			this.r42_32_88_notice_days = r42_32_88_notice_days;
		}

		public BigDecimal getR42_91_day_deposit_fixed_deposit_months() {
			return r42_91_day_deposit_fixed_deposit_months;
		}

		public void setR42_91_day_deposit_fixed_deposit_months(BigDecimal r42_91_day_deposit_fixed_deposit_months) {
			this.r42_91_day_deposit_fixed_deposit_months = r42_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR42_1_2_fixed_deposits_months() {
			return r42_1_2_fixed_deposits_months;
		}

		public void setR42_1_2_fixed_deposits_months(BigDecimal r42_1_2_fixed_deposits_months) {
			this.r42_1_2_fixed_deposits_months = r42_1_2_fixed_deposits_months;
		}

		public BigDecimal getR42_4_6_fixed_deposits_months() {
			return r42_4_6_fixed_deposits_months;
		}

		public void setR42_4_6_fixed_deposits_months(BigDecimal r42_4_6_fixed_deposits_months) {
			this.r42_4_6_fixed_deposits_months = r42_4_6_fixed_deposits_months;
		}

		public BigDecimal getR42_7_12_fixed_deposits_months() {
			return r42_7_12_fixed_deposits_months;
		}

		public void setR42_7_12_fixed_deposits_months(BigDecimal r42_7_12_fixed_deposits_months) {
			this.r42_7_12_fixed_deposits_months = r42_7_12_fixed_deposits_months;
		}

		public BigDecimal getR42_13_18_fixed_deposits_months() {
			return r42_13_18_fixed_deposits_months;
		}

		public void setR42_13_18_fixed_deposits_months(BigDecimal r42_13_18_fixed_deposits_months) {
			this.r42_13_18_fixed_deposits_months = r42_13_18_fixed_deposits_months;
		}

		public BigDecimal getR42_19_24_fixed_deposits_months() {
			return r42_19_24_fixed_deposits_months;
		}

		public void setR42_19_24_fixed_deposits_months(BigDecimal r42_19_24_fixed_deposits_months) {
			this.r42_19_24_fixed_deposits_months = r42_19_24_fixed_deposits_months;
		}

		public BigDecimal getR42_over_24_fixed_deposits_months() {
			return r42_over_24_fixed_deposits_months;
		}

		public void setR42_over_24_fixed_deposits_months(BigDecimal r42_over_24_fixed_deposits_months) {
			this.r42_over_24_fixed_deposits_months = r42_over_24_fixed_deposits_months;
		}

		public BigDecimal getR42_certificates_of_deposit() {
			return r42_certificates_of_deposit;
		}

		public void setR42_certificates_of_deposit(BigDecimal r42_certificates_of_deposit) {
			this.r42_certificates_of_deposit = r42_certificates_of_deposit;
		}

		public BigDecimal getR42_total() {
			return r42_total;
		}

		public void setR42_total(BigDecimal r42_total) {
			this.r42_total = r42_total;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_current() {
			return r43_current;
		}

		public void setR43_current(BigDecimal r43_current) {
			this.r43_current = r43_current;
		}

		public BigDecimal getR43_call() {
			return r43_call;
		}

		public void setR43_call(BigDecimal r43_call) {
			this.r43_call = r43_call;
		}

		public BigDecimal getR43_savings() {
			return r43_savings;
		}

		public void setR43_savings(BigDecimal r43_savings) {
			this.r43_savings = r43_savings;
		}

		public BigDecimal getR43_0_31_notice_days() {
			return r43_0_31_notice_days;
		}

		public void setR43_0_31_notice_days(BigDecimal r43_0_31_notice_days) {
			this.r43_0_31_notice_days = r43_0_31_notice_days;
		}

		public BigDecimal getR43_32_88_notice_days() {
			return r43_32_88_notice_days;
		}

		public void setR43_32_88_notice_days(BigDecimal r43_32_88_notice_days) {
			this.r43_32_88_notice_days = r43_32_88_notice_days;
		}

		public BigDecimal getR43_91_day_deposit_fixed_deposit_months() {
			return r43_91_day_deposit_fixed_deposit_months;
		}

		public void setR43_91_day_deposit_fixed_deposit_months(BigDecimal r43_91_day_deposit_fixed_deposit_months) {
			this.r43_91_day_deposit_fixed_deposit_months = r43_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR43_1_2_fixed_deposits_months() {
			return r43_1_2_fixed_deposits_months;
		}

		public void setR43_1_2_fixed_deposits_months(BigDecimal r43_1_2_fixed_deposits_months) {
			this.r43_1_2_fixed_deposits_months = r43_1_2_fixed_deposits_months;
		}

		public BigDecimal getR43_4_6_fixed_deposits_months() {
			return r43_4_6_fixed_deposits_months;
		}

		public void setR43_4_6_fixed_deposits_months(BigDecimal r43_4_6_fixed_deposits_months) {
			this.r43_4_6_fixed_deposits_months = r43_4_6_fixed_deposits_months;
		}

		public BigDecimal getR43_7_12_fixed_deposits_months() {
			return r43_7_12_fixed_deposits_months;
		}

		public void setR43_7_12_fixed_deposits_months(BigDecimal r43_7_12_fixed_deposits_months) {
			this.r43_7_12_fixed_deposits_months = r43_7_12_fixed_deposits_months;
		}

		public BigDecimal getR43_13_18_fixed_deposits_months() {
			return r43_13_18_fixed_deposits_months;
		}

		public void setR43_13_18_fixed_deposits_months(BigDecimal r43_13_18_fixed_deposits_months) {
			this.r43_13_18_fixed_deposits_months = r43_13_18_fixed_deposits_months;
		}

		public BigDecimal getR43_19_24_fixed_deposits_months() {
			return r43_19_24_fixed_deposits_months;
		}

		public void setR43_19_24_fixed_deposits_months(BigDecimal r43_19_24_fixed_deposits_months) {
			this.r43_19_24_fixed_deposits_months = r43_19_24_fixed_deposits_months;
		}

		public BigDecimal getR43_over_24_fixed_deposits_months() {
			return r43_over_24_fixed_deposits_months;
		}

		public void setR43_over_24_fixed_deposits_months(BigDecimal r43_over_24_fixed_deposits_months) {
			this.r43_over_24_fixed_deposits_months = r43_over_24_fixed_deposits_months;
		}

		public BigDecimal getR43_certificates_of_deposit() {
			return r43_certificates_of_deposit;
		}

		public void setR43_certificates_of_deposit(BigDecimal r43_certificates_of_deposit) {
			this.r43_certificates_of_deposit = r43_certificates_of_deposit;
		}

		public BigDecimal getR43_total() {
			return r43_total;
		}

		public void setR43_total(BigDecimal r43_total) {
			this.r43_total = r43_total;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_current() {
			return r44_current;
		}

		public void setR44_current(BigDecimal r44_current) {
			this.r44_current = r44_current;
		}

		public BigDecimal getR44_call() {
			return r44_call;
		}

		public void setR44_call(BigDecimal r44_call) {
			this.r44_call = r44_call;
		}

		public BigDecimal getR44_savings() {
			return r44_savings;
		}

		public void setR44_savings(BigDecimal r44_savings) {
			this.r44_savings = r44_savings;
		}

		public BigDecimal getR44_0_31_notice_days() {
			return r44_0_31_notice_days;
		}

		public void setR44_0_31_notice_days(BigDecimal r44_0_31_notice_days) {
			this.r44_0_31_notice_days = r44_0_31_notice_days;
		}

		public BigDecimal getR44_32_88_notice_days() {
			return r44_32_88_notice_days;
		}

		public void setR44_32_88_notice_days(BigDecimal r44_32_88_notice_days) {
			this.r44_32_88_notice_days = r44_32_88_notice_days;
		}

		public BigDecimal getR44_91_day_deposit_fixed_deposit_months() {
			return r44_91_day_deposit_fixed_deposit_months;
		}

		public void setR44_91_day_deposit_fixed_deposit_months(BigDecimal r44_91_day_deposit_fixed_deposit_months) {
			this.r44_91_day_deposit_fixed_deposit_months = r44_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR44_1_2_fixed_deposits_months() {
			return r44_1_2_fixed_deposits_months;
		}

		public void setR44_1_2_fixed_deposits_months(BigDecimal r44_1_2_fixed_deposits_months) {
			this.r44_1_2_fixed_deposits_months = r44_1_2_fixed_deposits_months;
		}

		public BigDecimal getR44_4_6_fixed_deposits_months() {
			return r44_4_6_fixed_deposits_months;
		}

		public void setR44_4_6_fixed_deposits_months(BigDecimal r44_4_6_fixed_deposits_months) {
			this.r44_4_6_fixed_deposits_months = r44_4_6_fixed_deposits_months;
		}

		public BigDecimal getR44_7_12_fixed_deposits_months() {
			return r44_7_12_fixed_deposits_months;
		}

		public void setR44_7_12_fixed_deposits_months(BigDecimal r44_7_12_fixed_deposits_months) {
			this.r44_7_12_fixed_deposits_months = r44_7_12_fixed_deposits_months;
		}

		public BigDecimal getR44_13_18_fixed_deposits_months() {
			return r44_13_18_fixed_deposits_months;
		}

		public void setR44_13_18_fixed_deposits_months(BigDecimal r44_13_18_fixed_deposits_months) {
			this.r44_13_18_fixed_deposits_months = r44_13_18_fixed_deposits_months;
		}

		public BigDecimal getR44_19_24_fixed_deposits_months() {
			return r44_19_24_fixed_deposits_months;
		}

		public void setR44_19_24_fixed_deposits_months(BigDecimal r44_19_24_fixed_deposits_months) {
			this.r44_19_24_fixed_deposits_months = r44_19_24_fixed_deposits_months;
		}

		public BigDecimal getR44_over_24_fixed_deposits_months() {
			return r44_over_24_fixed_deposits_months;
		}

		public void setR44_over_24_fixed_deposits_months(BigDecimal r44_over_24_fixed_deposits_months) {
			this.r44_over_24_fixed_deposits_months = r44_over_24_fixed_deposits_months;
		}

		public BigDecimal getR44_certificates_of_deposit() {
			return r44_certificates_of_deposit;
		}

		public void setR44_certificates_of_deposit(BigDecimal r44_certificates_of_deposit) {
			this.r44_certificates_of_deposit = r44_certificates_of_deposit;
		}

		public BigDecimal getR44_total() {
			return r44_total;
		}

		public void setR44_total(BigDecimal r44_total) {
			this.r44_total = r44_total;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_current() {
			return r45_current;
		}

		public void setR45_current(BigDecimal r45_current) {
			this.r45_current = r45_current;
		}

		public BigDecimal getR45_call() {
			return r45_call;
		}

		public void setR45_call(BigDecimal r45_call) {
			this.r45_call = r45_call;
		}

		public BigDecimal getR45_savings() {
			return r45_savings;
		}

		public void setR45_savings(BigDecimal r45_savings) {
			this.r45_savings = r45_savings;
		}

		public BigDecimal getR45_0_31_notice_days() {
			return r45_0_31_notice_days;
		}

		public void setR45_0_31_notice_days(BigDecimal r45_0_31_notice_days) {
			this.r45_0_31_notice_days = r45_0_31_notice_days;
		}

		public BigDecimal getR45_32_88_notice_days() {
			return r45_32_88_notice_days;
		}

		public void setR45_32_88_notice_days(BigDecimal r45_32_88_notice_days) {
			this.r45_32_88_notice_days = r45_32_88_notice_days;
		}

		public BigDecimal getR45_91_day_deposit_fixed_deposit_months() {
			return r45_91_day_deposit_fixed_deposit_months;
		}

		public void setR45_91_day_deposit_fixed_deposit_months(BigDecimal r45_91_day_deposit_fixed_deposit_months) {
			this.r45_91_day_deposit_fixed_deposit_months = r45_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR45_1_2_fixed_deposits_months() {
			return r45_1_2_fixed_deposits_months;
		}

		public void setR45_1_2_fixed_deposits_months(BigDecimal r45_1_2_fixed_deposits_months) {
			this.r45_1_2_fixed_deposits_months = r45_1_2_fixed_deposits_months;
		}

		public BigDecimal getR45_4_6_fixed_deposits_months() {
			return r45_4_6_fixed_deposits_months;
		}

		public void setR45_4_6_fixed_deposits_months(BigDecimal r45_4_6_fixed_deposits_months) {
			this.r45_4_6_fixed_deposits_months = r45_4_6_fixed_deposits_months;
		}

		public BigDecimal getR45_7_12_fixed_deposits_months() {
			return r45_7_12_fixed_deposits_months;
		}

		public void setR45_7_12_fixed_deposits_months(BigDecimal r45_7_12_fixed_deposits_months) {
			this.r45_7_12_fixed_deposits_months = r45_7_12_fixed_deposits_months;
		}

		public BigDecimal getR45_13_18_fixed_deposits_months() {
			return r45_13_18_fixed_deposits_months;
		}

		public void setR45_13_18_fixed_deposits_months(BigDecimal r45_13_18_fixed_deposits_months) {
			this.r45_13_18_fixed_deposits_months = r45_13_18_fixed_deposits_months;
		}

		public BigDecimal getR45_19_24_fixed_deposits_months() {
			return r45_19_24_fixed_deposits_months;
		}

		public void setR45_19_24_fixed_deposits_months(BigDecimal r45_19_24_fixed_deposits_months) {
			this.r45_19_24_fixed_deposits_months = r45_19_24_fixed_deposits_months;
		}

		public BigDecimal getR45_over_24_fixed_deposits_months() {
			return r45_over_24_fixed_deposits_months;
		}

		public void setR45_over_24_fixed_deposits_months(BigDecimal r45_over_24_fixed_deposits_months) {
			this.r45_over_24_fixed_deposits_months = r45_over_24_fixed_deposits_months;
		}

		public BigDecimal getR45_certificates_of_deposit() {
			return r45_certificates_of_deposit;
		}

		public void setR45_certificates_of_deposit(BigDecimal r45_certificates_of_deposit) {
			this.r45_certificates_of_deposit = r45_certificates_of_deposit;
		}

		public BigDecimal getR45_total() {
			return r45_total;
		}

		public void setR45_total(BigDecimal r45_total) {
			this.r45_total = r45_total;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_current() {
			return r46_current;
		}

		public void setR46_current(BigDecimal r46_current) {
			this.r46_current = r46_current;
		}

		public BigDecimal getR46_call() {
			return r46_call;
		}

		public void setR46_call(BigDecimal r46_call) {
			this.r46_call = r46_call;
		}

		public BigDecimal getR46_savings() {
			return r46_savings;
		}

		public void setR46_savings(BigDecimal r46_savings) {
			this.r46_savings = r46_savings;
		}

		public BigDecimal getR46_0_31_notice_days() {
			return r46_0_31_notice_days;
		}

		public void setR46_0_31_notice_days(BigDecimal r46_0_31_notice_days) {
			this.r46_0_31_notice_days = r46_0_31_notice_days;
		}

		public BigDecimal getR46_32_88_notice_days() {
			return r46_32_88_notice_days;
		}

		public void setR46_32_88_notice_days(BigDecimal r46_32_88_notice_days) {
			this.r46_32_88_notice_days = r46_32_88_notice_days;
		}

		public BigDecimal getR46_91_day_deposit_fixed_deposit_months() {
			return r46_91_day_deposit_fixed_deposit_months;
		}

		public void setR46_91_day_deposit_fixed_deposit_months(BigDecimal r46_91_day_deposit_fixed_deposit_months) {
			this.r46_91_day_deposit_fixed_deposit_months = r46_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR46_1_2_fixed_deposits_months() {
			return r46_1_2_fixed_deposits_months;
		}

		public void setR46_1_2_fixed_deposits_months(BigDecimal r46_1_2_fixed_deposits_months) {
			this.r46_1_2_fixed_deposits_months = r46_1_2_fixed_deposits_months;
		}

		public BigDecimal getR46_4_6_fixed_deposits_months() {
			return r46_4_6_fixed_deposits_months;
		}

		public void setR46_4_6_fixed_deposits_months(BigDecimal r46_4_6_fixed_deposits_months) {
			this.r46_4_6_fixed_deposits_months = r46_4_6_fixed_deposits_months;
		}

		public BigDecimal getR46_7_12_fixed_deposits_months() {
			return r46_7_12_fixed_deposits_months;
		}

		public void setR46_7_12_fixed_deposits_months(BigDecimal r46_7_12_fixed_deposits_months) {
			this.r46_7_12_fixed_deposits_months = r46_7_12_fixed_deposits_months;
		}

		public BigDecimal getR46_13_18_fixed_deposits_months() {
			return r46_13_18_fixed_deposits_months;
		}

		public void setR46_13_18_fixed_deposits_months(BigDecimal r46_13_18_fixed_deposits_months) {
			this.r46_13_18_fixed_deposits_months = r46_13_18_fixed_deposits_months;
		}

		public BigDecimal getR46_19_24_fixed_deposits_months() {
			return r46_19_24_fixed_deposits_months;
		}

		public void setR46_19_24_fixed_deposits_months(BigDecimal r46_19_24_fixed_deposits_months) {
			this.r46_19_24_fixed_deposits_months = r46_19_24_fixed_deposits_months;
		}

		public BigDecimal getR46_over_24_fixed_deposits_months() {
			return r46_over_24_fixed_deposits_months;
		}

		public void setR46_over_24_fixed_deposits_months(BigDecimal r46_over_24_fixed_deposits_months) {
			this.r46_over_24_fixed_deposits_months = r46_over_24_fixed_deposits_months;
		}

		public BigDecimal getR46_certificates_of_deposit() {
			return r46_certificates_of_deposit;
		}

		public void setR46_certificates_of_deposit(BigDecimal r46_certificates_of_deposit) {
			this.r46_certificates_of_deposit = r46_certificates_of_deposit;
		}

		public BigDecimal getR46_total() {
			return r46_total;
		}

		public void setR46_total(BigDecimal r46_total) {
			this.r46_total = r46_total;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_current() {
			return r47_current;
		}

		public void setR47_current(BigDecimal r47_current) {
			this.r47_current = r47_current;
		}

		public BigDecimal getR47_call() {
			return r47_call;
		}

		public void setR47_call(BigDecimal r47_call) {
			this.r47_call = r47_call;
		}

		public BigDecimal getR47_savings() {
			return r47_savings;
		}

		public void setR47_savings(BigDecimal r47_savings) {
			this.r47_savings = r47_savings;
		}

		public BigDecimal getR47_0_31_notice_days() {
			return r47_0_31_notice_days;
		}

		public void setR47_0_31_notice_days(BigDecimal r47_0_31_notice_days) {
			this.r47_0_31_notice_days = r47_0_31_notice_days;
		}

		public BigDecimal getR47_32_88_notice_days() {
			return r47_32_88_notice_days;
		}

		public void setR47_32_88_notice_days(BigDecimal r47_32_88_notice_days) {
			this.r47_32_88_notice_days = r47_32_88_notice_days;
		}

		public BigDecimal getR47_91_day_deposit_fixed_deposit_months() {
			return r47_91_day_deposit_fixed_deposit_months;
		}

		public void setR47_91_day_deposit_fixed_deposit_months(BigDecimal r47_91_day_deposit_fixed_deposit_months) {
			this.r47_91_day_deposit_fixed_deposit_months = r47_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR47_1_2_fixed_deposits_months() {
			return r47_1_2_fixed_deposits_months;
		}

		public void setR47_1_2_fixed_deposits_months(BigDecimal r47_1_2_fixed_deposits_months) {
			this.r47_1_2_fixed_deposits_months = r47_1_2_fixed_deposits_months;
		}

		public BigDecimal getR47_4_6_fixed_deposits_months() {
			return r47_4_6_fixed_deposits_months;
		}

		public void setR47_4_6_fixed_deposits_months(BigDecimal r47_4_6_fixed_deposits_months) {
			this.r47_4_6_fixed_deposits_months = r47_4_6_fixed_deposits_months;
		}

		public BigDecimal getR47_7_12_fixed_deposits_months() {
			return r47_7_12_fixed_deposits_months;
		}

		public void setR47_7_12_fixed_deposits_months(BigDecimal r47_7_12_fixed_deposits_months) {
			this.r47_7_12_fixed_deposits_months = r47_7_12_fixed_deposits_months;
		}

		public BigDecimal getR47_13_18_fixed_deposits_months() {
			return r47_13_18_fixed_deposits_months;
		}

		public void setR47_13_18_fixed_deposits_months(BigDecimal r47_13_18_fixed_deposits_months) {
			this.r47_13_18_fixed_deposits_months = r47_13_18_fixed_deposits_months;
		}

		public BigDecimal getR47_19_24_fixed_deposits_months() {
			return r47_19_24_fixed_deposits_months;
		}

		public void setR47_19_24_fixed_deposits_months(BigDecimal r47_19_24_fixed_deposits_months) {
			this.r47_19_24_fixed_deposits_months = r47_19_24_fixed_deposits_months;
		}

		public BigDecimal getR47_over_24_fixed_deposits_months() {
			return r47_over_24_fixed_deposits_months;
		}

		public void setR47_over_24_fixed_deposits_months(BigDecimal r47_over_24_fixed_deposits_months) {
			this.r47_over_24_fixed_deposits_months = r47_over_24_fixed_deposits_months;
		}

		public BigDecimal getR47_certificates_of_deposit() {
			return r47_certificates_of_deposit;
		}

		public void setR47_certificates_of_deposit(BigDecimal r47_certificates_of_deposit) {
			this.r47_certificates_of_deposit = r47_certificates_of_deposit;
		}

		public BigDecimal getR47_total() {
			return r47_total;
		}

		public void setR47_total(BigDecimal r47_total) {
			this.r47_total = r47_total;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_current() {
			return r48_current;
		}

		public void setR48_current(BigDecimal r48_current) {
			this.r48_current = r48_current;
		}

		public BigDecimal getR48_call() {
			return r48_call;
		}

		public void setR48_call(BigDecimal r48_call) {
			this.r48_call = r48_call;
		}

		public BigDecimal getR48_savings() {
			return r48_savings;
		}

		public void setR48_savings(BigDecimal r48_savings) {
			this.r48_savings = r48_savings;
		}

		public BigDecimal getR48_0_31_notice_days() {
			return r48_0_31_notice_days;
		}

		public void setR48_0_31_notice_days(BigDecimal r48_0_31_notice_days) {
			this.r48_0_31_notice_days = r48_0_31_notice_days;
		}

		public BigDecimal getR48_32_88_notice_days() {
			return r48_32_88_notice_days;
		}

		public void setR48_32_88_notice_days(BigDecimal r48_32_88_notice_days) {
			this.r48_32_88_notice_days = r48_32_88_notice_days;
		}

		public BigDecimal getR48_91_day_deposit_fixed_deposit_months() {
			return r48_91_day_deposit_fixed_deposit_months;
		}

		public void setR48_91_day_deposit_fixed_deposit_months(BigDecimal r48_91_day_deposit_fixed_deposit_months) {
			this.r48_91_day_deposit_fixed_deposit_months = r48_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR48_1_2_fixed_deposits_months() {
			return r48_1_2_fixed_deposits_months;
		}

		public void setR48_1_2_fixed_deposits_months(BigDecimal r48_1_2_fixed_deposits_months) {
			this.r48_1_2_fixed_deposits_months = r48_1_2_fixed_deposits_months;
		}

		public BigDecimal getR48_4_6_fixed_deposits_months() {
			return r48_4_6_fixed_deposits_months;
		}

		public void setR48_4_6_fixed_deposits_months(BigDecimal r48_4_6_fixed_deposits_months) {
			this.r48_4_6_fixed_deposits_months = r48_4_6_fixed_deposits_months;
		}

		public BigDecimal getR48_7_12_fixed_deposits_months() {
			return r48_7_12_fixed_deposits_months;
		}

		public void setR48_7_12_fixed_deposits_months(BigDecimal r48_7_12_fixed_deposits_months) {
			this.r48_7_12_fixed_deposits_months = r48_7_12_fixed_deposits_months;
		}

		public BigDecimal getR48_13_18_fixed_deposits_months() {
			return r48_13_18_fixed_deposits_months;
		}

		public void setR48_13_18_fixed_deposits_months(BigDecimal r48_13_18_fixed_deposits_months) {
			this.r48_13_18_fixed_deposits_months = r48_13_18_fixed_deposits_months;
		}

		public BigDecimal getR48_19_24_fixed_deposits_months() {
			return r48_19_24_fixed_deposits_months;
		}

		public void setR48_19_24_fixed_deposits_months(BigDecimal r48_19_24_fixed_deposits_months) {
			this.r48_19_24_fixed_deposits_months = r48_19_24_fixed_deposits_months;
		}

		public BigDecimal getR48_over_24_fixed_deposits_months() {
			return r48_over_24_fixed_deposits_months;
		}

		public void setR48_over_24_fixed_deposits_months(BigDecimal r48_over_24_fixed_deposits_months) {
			this.r48_over_24_fixed_deposits_months = r48_over_24_fixed_deposits_months;
		}

		public BigDecimal getR48_certificates_of_deposit() {
			return r48_certificates_of_deposit;
		}

		public void setR48_certificates_of_deposit(BigDecimal r48_certificates_of_deposit) {
			this.r48_certificates_of_deposit = r48_certificates_of_deposit;
		}

		public BigDecimal getR48_total() {
			return r48_total;
		}

		public void setR48_total(BigDecimal r48_total) {
			this.r48_total = r48_total;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_current() {
			return r49_current;
		}

		public void setR49_current(BigDecimal r49_current) {
			this.r49_current = r49_current;
		}

		public BigDecimal getR49_call() {
			return r49_call;
		}

		public void setR49_call(BigDecimal r49_call) {
			this.r49_call = r49_call;
		}

		public BigDecimal getR49_savings() {
			return r49_savings;
		}

		public void setR49_savings(BigDecimal r49_savings) {
			this.r49_savings = r49_savings;
		}

		public BigDecimal getR49_0_31_notice_days() {
			return r49_0_31_notice_days;
		}

		public void setR49_0_31_notice_days(BigDecimal r49_0_31_notice_days) {
			this.r49_0_31_notice_days = r49_0_31_notice_days;
		}

		public BigDecimal getR49_32_88_notice_days() {
			return r49_32_88_notice_days;
		}

		public void setR49_32_88_notice_days(BigDecimal r49_32_88_notice_days) {
			this.r49_32_88_notice_days = r49_32_88_notice_days;
		}

		public BigDecimal getR49_91_day_deposit_fixed_deposit_months() {
			return r49_91_day_deposit_fixed_deposit_months;
		}

		public void setR49_91_day_deposit_fixed_deposit_months(BigDecimal r49_91_day_deposit_fixed_deposit_months) {
			this.r49_91_day_deposit_fixed_deposit_months = r49_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR49_1_2_fixed_deposits_months() {
			return r49_1_2_fixed_deposits_months;
		}

		public void setR49_1_2_fixed_deposits_months(BigDecimal r49_1_2_fixed_deposits_months) {
			this.r49_1_2_fixed_deposits_months = r49_1_2_fixed_deposits_months;
		}

		public BigDecimal getR49_4_6_fixed_deposits_months() {
			return r49_4_6_fixed_deposits_months;
		}

		public void setR49_4_6_fixed_deposits_months(BigDecimal r49_4_6_fixed_deposits_months) {
			this.r49_4_6_fixed_deposits_months = r49_4_6_fixed_deposits_months;
		}

		public BigDecimal getR49_7_12_fixed_deposits_months() {
			return r49_7_12_fixed_deposits_months;
		}

		public void setR49_7_12_fixed_deposits_months(BigDecimal r49_7_12_fixed_deposits_months) {
			this.r49_7_12_fixed_deposits_months = r49_7_12_fixed_deposits_months;
		}

		public BigDecimal getR49_13_18_fixed_deposits_months() {
			return r49_13_18_fixed_deposits_months;
		}

		public void setR49_13_18_fixed_deposits_months(BigDecimal r49_13_18_fixed_deposits_months) {
			this.r49_13_18_fixed_deposits_months = r49_13_18_fixed_deposits_months;
		}

		public BigDecimal getR49_19_24_fixed_deposits_months() {
			return r49_19_24_fixed_deposits_months;
		}

		public void setR49_19_24_fixed_deposits_months(BigDecimal r49_19_24_fixed_deposits_months) {
			this.r49_19_24_fixed_deposits_months = r49_19_24_fixed_deposits_months;
		}

		public BigDecimal getR49_over_24_fixed_deposits_months() {
			return r49_over_24_fixed_deposits_months;
		}

		public void setR49_over_24_fixed_deposits_months(BigDecimal r49_over_24_fixed_deposits_months) {
			this.r49_over_24_fixed_deposits_months = r49_over_24_fixed_deposits_months;
		}

		public BigDecimal getR49_certificates_of_deposit() {
			return r49_certificates_of_deposit;
		}

		public void setR49_certificates_of_deposit(BigDecimal r49_certificates_of_deposit) {
			this.r49_certificates_of_deposit = r49_certificates_of_deposit;
		}

		public BigDecimal getR49_total() {
			return r49_total;
		}

		public void setR49_total(BigDecimal r49_total) {
			this.r49_total = r49_total;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_current() {
			return r50_current;
		}

		public void setR50_current(BigDecimal r50_current) {
			this.r50_current = r50_current;
		}

		public BigDecimal getR50_call() {
			return r50_call;
		}

		public void setR50_call(BigDecimal r50_call) {
			this.r50_call = r50_call;
		}

		public BigDecimal getR50_savings() {
			return r50_savings;
		}

		public void setR50_savings(BigDecimal r50_savings) {
			this.r50_savings = r50_savings;
		}

		public BigDecimal getR50_0_31_notice_days() {
			return r50_0_31_notice_days;
		}

		public void setR50_0_31_notice_days(BigDecimal r50_0_31_notice_days) {
			this.r50_0_31_notice_days = r50_0_31_notice_days;
		}

		public BigDecimal getR50_32_88_notice_days() {
			return r50_32_88_notice_days;
		}

		public void setR50_32_88_notice_days(BigDecimal r50_32_88_notice_days) {
			this.r50_32_88_notice_days = r50_32_88_notice_days;
		}

		public BigDecimal getR50_91_day_deposit_fixed_deposit_months() {
			return r50_91_day_deposit_fixed_deposit_months;
		}

		public void setR50_91_day_deposit_fixed_deposit_months(BigDecimal r50_91_day_deposit_fixed_deposit_months) {
			this.r50_91_day_deposit_fixed_deposit_months = r50_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR50_1_2_fixed_deposits_months() {
			return r50_1_2_fixed_deposits_months;
		}

		public void setR50_1_2_fixed_deposits_months(BigDecimal r50_1_2_fixed_deposits_months) {
			this.r50_1_2_fixed_deposits_months = r50_1_2_fixed_deposits_months;
		}

		public BigDecimal getR50_4_6_fixed_deposits_months() {
			return r50_4_6_fixed_deposits_months;
		}

		public void setR50_4_6_fixed_deposits_months(BigDecimal r50_4_6_fixed_deposits_months) {
			this.r50_4_6_fixed_deposits_months = r50_4_6_fixed_deposits_months;
		}

		public BigDecimal getR50_7_12_fixed_deposits_months() {
			return r50_7_12_fixed_deposits_months;
		}

		public void setR50_7_12_fixed_deposits_months(BigDecimal r50_7_12_fixed_deposits_months) {
			this.r50_7_12_fixed_deposits_months = r50_7_12_fixed_deposits_months;
		}

		public BigDecimal getR50_13_18_fixed_deposits_months() {
			return r50_13_18_fixed_deposits_months;
		}

		public void setR50_13_18_fixed_deposits_months(BigDecimal r50_13_18_fixed_deposits_months) {
			this.r50_13_18_fixed_deposits_months = r50_13_18_fixed_deposits_months;
		}

		public BigDecimal getR50_19_24_fixed_deposits_months() {
			return r50_19_24_fixed_deposits_months;
		}

		public void setR50_19_24_fixed_deposits_months(BigDecimal r50_19_24_fixed_deposits_months) {
			this.r50_19_24_fixed_deposits_months = r50_19_24_fixed_deposits_months;
		}

		public BigDecimal getR50_over_24_fixed_deposits_months() {
			return r50_over_24_fixed_deposits_months;
		}

		public void setR50_over_24_fixed_deposits_months(BigDecimal r50_over_24_fixed_deposits_months) {
			this.r50_over_24_fixed_deposits_months = r50_over_24_fixed_deposits_months;
		}

		public BigDecimal getR50_certificates_of_deposit() {
			return r50_certificates_of_deposit;
		}

		public void setR50_certificates_of_deposit(BigDecimal r50_certificates_of_deposit) {
			this.r50_certificates_of_deposit = r50_certificates_of_deposit;
		}

		public BigDecimal getR50_total() {
			return r50_total;
		}

		public void setR50_total(BigDecimal r50_total) {
			this.r50_total = r50_total;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_current() {
			return r51_current;
		}

		public void setR51_current(BigDecimal r51_current) {
			this.r51_current = r51_current;
		}

		public BigDecimal getR51_call() {
			return r51_call;
		}

		public void setR51_call(BigDecimal r51_call) {
			this.r51_call = r51_call;
		}

		public BigDecimal getR51_savings() {
			return r51_savings;
		}

		public void setR51_savings(BigDecimal r51_savings) {
			this.r51_savings = r51_savings;
		}

		public BigDecimal getR51_0_31_notice_days() {
			return r51_0_31_notice_days;
		}

		public void setR51_0_31_notice_days(BigDecimal r51_0_31_notice_days) {
			this.r51_0_31_notice_days = r51_0_31_notice_days;
		}

		public BigDecimal getR51_32_88_notice_days() {
			return r51_32_88_notice_days;
		}

		public void setR51_32_88_notice_days(BigDecimal r51_32_88_notice_days) {
			this.r51_32_88_notice_days = r51_32_88_notice_days;
		}

		public BigDecimal getR51_91_day_deposit_fixed_deposit_months() {
			return r51_91_day_deposit_fixed_deposit_months;
		}

		public void setR51_91_day_deposit_fixed_deposit_months(BigDecimal r51_91_day_deposit_fixed_deposit_months) {
			this.r51_91_day_deposit_fixed_deposit_months = r51_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR51_1_2_fixed_deposits_months() {
			return r51_1_2_fixed_deposits_months;
		}

		public void setR51_1_2_fixed_deposits_months(BigDecimal r51_1_2_fixed_deposits_months) {
			this.r51_1_2_fixed_deposits_months = r51_1_2_fixed_deposits_months;
		}

		public BigDecimal getR51_4_6_fixed_deposits_months() {
			return r51_4_6_fixed_deposits_months;
		}

		public void setR51_4_6_fixed_deposits_months(BigDecimal r51_4_6_fixed_deposits_months) {
			this.r51_4_6_fixed_deposits_months = r51_4_6_fixed_deposits_months;
		}

		public BigDecimal getR51_7_12_fixed_deposits_months() {
			return r51_7_12_fixed_deposits_months;
		}

		public void setR51_7_12_fixed_deposits_months(BigDecimal r51_7_12_fixed_deposits_months) {
			this.r51_7_12_fixed_deposits_months = r51_7_12_fixed_deposits_months;
		}

		public BigDecimal getR51_13_18_fixed_deposits_months() {
			return r51_13_18_fixed_deposits_months;
		}

		public void setR51_13_18_fixed_deposits_months(BigDecimal r51_13_18_fixed_deposits_months) {
			this.r51_13_18_fixed_deposits_months = r51_13_18_fixed_deposits_months;
		}

		public BigDecimal getR51_19_24_fixed_deposits_months() {
			return r51_19_24_fixed_deposits_months;
		}

		public void setR51_19_24_fixed_deposits_months(BigDecimal r51_19_24_fixed_deposits_months) {
			this.r51_19_24_fixed_deposits_months = r51_19_24_fixed_deposits_months;
		}

		public BigDecimal getR51_over_24_fixed_deposits_months() {
			return r51_over_24_fixed_deposits_months;
		}

		public void setR51_over_24_fixed_deposits_months(BigDecimal r51_over_24_fixed_deposits_months) {
			this.r51_over_24_fixed_deposits_months = r51_over_24_fixed_deposits_months;
		}

		public BigDecimal getR51_certificates_of_deposit() {
			return r51_certificates_of_deposit;
		}

		public void setR51_certificates_of_deposit(BigDecimal r51_certificates_of_deposit) {
			this.r51_certificates_of_deposit = r51_certificates_of_deposit;
		}

		public BigDecimal getR51_total() {
			return r51_total;
		}

		public void setR51_total(BigDecimal r51_total) {
			this.r51_total = r51_total;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_current() {
			return r52_current;
		}

		public void setR52_current(BigDecimal r52_current) {
			this.r52_current = r52_current;
		}

		public BigDecimal getR52_call() {
			return r52_call;
		}

		public void setR52_call(BigDecimal r52_call) {
			this.r52_call = r52_call;
		}

		public BigDecimal getR52_savings() {
			return r52_savings;
		}

		public void setR52_savings(BigDecimal r52_savings) {
			this.r52_savings = r52_savings;
		}

		public BigDecimal getR52_0_31_notice_days() {
			return r52_0_31_notice_days;
		}

		public void setR52_0_31_notice_days(BigDecimal r52_0_31_notice_days) {
			this.r52_0_31_notice_days = r52_0_31_notice_days;
		}

		public BigDecimal getR52_32_88_notice_days() {
			return r52_32_88_notice_days;
		}

		public void setR52_32_88_notice_days(BigDecimal r52_32_88_notice_days) {
			this.r52_32_88_notice_days = r52_32_88_notice_days;
		}

		public BigDecimal getR52_91_day_deposit_fixed_deposit_months() {
			return r52_91_day_deposit_fixed_deposit_months;
		}

		public void setR52_91_day_deposit_fixed_deposit_months(BigDecimal r52_91_day_deposit_fixed_deposit_months) {
			this.r52_91_day_deposit_fixed_deposit_months = r52_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR52_1_2_fixed_deposits_months() {
			return r52_1_2_fixed_deposits_months;
		}

		public void setR52_1_2_fixed_deposits_months(BigDecimal r52_1_2_fixed_deposits_months) {
			this.r52_1_2_fixed_deposits_months = r52_1_2_fixed_deposits_months;
		}

		public BigDecimal getR52_4_6_fixed_deposits_months() {
			return r52_4_6_fixed_deposits_months;
		}

		public void setR52_4_6_fixed_deposits_months(BigDecimal r52_4_6_fixed_deposits_months) {
			this.r52_4_6_fixed_deposits_months = r52_4_6_fixed_deposits_months;
		}

		public BigDecimal getR52_7_12_fixed_deposits_months() {
			return r52_7_12_fixed_deposits_months;
		}

		public void setR52_7_12_fixed_deposits_months(BigDecimal r52_7_12_fixed_deposits_months) {
			this.r52_7_12_fixed_deposits_months = r52_7_12_fixed_deposits_months;
		}

		public BigDecimal getR52_13_18_fixed_deposits_months() {
			return r52_13_18_fixed_deposits_months;
		}

		public void setR52_13_18_fixed_deposits_months(BigDecimal r52_13_18_fixed_deposits_months) {
			this.r52_13_18_fixed_deposits_months = r52_13_18_fixed_deposits_months;
		}

		public BigDecimal getR52_19_24_fixed_deposits_months() {
			return r52_19_24_fixed_deposits_months;
		}

		public void setR52_19_24_fixed_deposits_months(BigDecimal r52_19_24_fixed_deposits_months) {
			this.r52_19_24_fixed_deposits_months = r52_19_24_fixed_deposits_months;
		}

		public BigDecimal getR52_over_24_fixed_deposits_months() {
			return r52_over_24_fixed_deposits_months;
		}

		public void setR52_over_24_fixed_deposits_months(BigDecimal r52_over_24_fixed_deposits_months) {
			this.r52_over_24_fixed_deposits_months = r52_over_24_fixed_deposits_months;
		}

		public BigDecimal getR52_certificates_of_deposit() {
			return r52_certificates_of_deposit;
		}

		public void setR52_certificates_of_deposit(BigDecimal r52_certificates_of_deposit) {
			this.r52_certificates_of_deposit = r52_certificates_of_deposit;
		}

		public BigDecimal getR52_total() {
			return r52_total;
		}

		public void setR52_total(BigDecimal r52_total) {
			this.r52_total = r52_total;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_current() {
			return r53_current;
		}

		public void setR53_current(BigDecimal r53_current) {
			this.r53_current = r53_current;
		}

		public BigDecimal getR53_call() {
			return r53_call;
		}

		public void setR53_call(BigDecimal r53_call) {
			this.r53_call = r53_call;
		}

		public BigDecimal getR53_savings() {
			return r53_savings;
		}

		public void setR53_savings(BigDecimal r53_savings) {
			this.r53_savings = r53_savings;
		}

		public BigDecimal getR53_0_31_notice_days() {
			return r53_0_31_notice_days;
		}

		public void setR53_0_31_notice_days(BigDecimal r53_0_31_notice_days) {
			this.r53_0_31_notice_days = r53_0_31_notice_days;
		}

		public BigDecimal getR53_32_88_notice_days() {
			return r53_32_88_notice_days;
		}

		public void setR53_32_88_notice_days(BigDecimal r53_32_88_notice_days) {
			this.r53_32_88_notice_days = r53_32_88_notice_days;
		}

		public BigDecimal getR53_91_day_deposit_fixed_deposit_months() {
			return r53_91_day_deposit_fixed_deposit_months;
		}

		public void setR53_91_day_deposit_fixed_deposit_months(BigDecimal r53_91_day_deposit_fixed_deposit_months) {
			this.r53_91_day_deposit_fixed_deposit_months = r53_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR53_1_2_fixed_deposits_months() {
			return r53_1_2_fixed_deposits_months;
		}

		public void setR53_1_2_fixed_deposits_months(BigDecimal r53_1_2_fixed_deposits_months) {
			this.r53_1_2_fixed_deposits_months = r53_1_2_fixed_deposits_months;
		}

		public BigDecimal getR53_4_6_fixed_deposits_months() {
			return r53_4_6_fixed_deposits_months;
		}

		public void setR53_4_6_fixed_deposits_months(BigDecimal r53_4_6_fixed_deposits_months) {
			this.r53_4_6_fixed_deposits_months = r53_4_6_fixed_deposits_months;
		}

		public BigDecimal getR53_7_12_fixed_deposits_months() {
			return r53_7_12_fixed_deposits_months;
		}

		public void setR53_7_12_fixed_deposits_months(BigDecimal r53_7_12_fixed_deposits_months) {
			this.r53_7_12_fixed_deposits_months = r53_7_12_fixed_deposits_months;
		}

		public BigDecimal getR53_13_18_fixed_deposits_months() {
			return r53_13_18_fixed_deposits_months;
		}

		public void setR53_13_18_fixed_deposits_months(BigDecimal r53_13_18_fixed_deposits_months) {
			this.r53_13_18_fixed_deposits_months = r53_13_18_fixed_deposits_months;
		}

		public BigDecimal getR53_19_24_fixed_deposits_months() {
			return r53_19_24_fixed_deposits_months;
		}

		public void setR53_19_24_fixed_deposits_months(BigDecimal r53_19_24_fixed_deposits_months) {
			this.r53_19_24_fixed_deposits_months = r53_19_24_fixed_deposits_months;
		}

		public BigDecimal getR53_over_24_fixed_deposits_months() {
			return r53_over_24_fixed_deposits_months;
		}

		public void setR53_over_24_fixed_deposits_months(BigDecimal r53_over_24_fixed_deposits_months) {
			this.r53_over_24_fixed_deposits_months = r53_over_24_fixed_deposits_months;
		}

		public BigDecimal getR53_certificates_of_deposit() {
			return r53_certificates_of_deposit;
		}

		public void setR53_certificates_of_deposit(BigDecimal r53_certificates_of_deposit) {
			this.r53_certificates_of_deposit = r53_certificates_of_deposit;
		}

		public BigDecimal getR53_total() {
			return r53_total;
		}

		public void setR53_total(BigDecimal r53_total) {
			this.r53_total = r53_total;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_current() {
			return r54_current;
		}

		public void setR54_current(BigDecimal r54_current) {
			this.r54_current = r54_current;
		}

		public BigDecimal getR54_call() {
			return r54_call;
		}

		public void setR54_call(BigDecimal r54_call) {
			this.r54_call = r54_call;
		}

		public BigDecimal getR54_savings() {
			return r54_savings;
		}

		public void setR54_savings(BigDecimal r54_savings) {
			this.r54_savings = r54_savings;
		}

		public BigDecimal getR54_0_31_notice_days() {
			return r54_0_31_notice_days;
		}

		public void setR54_0_31_notice_days(BigDecimal r54_0_31_notice_days) {
			this.r54_0_31_notice_days = r54_0_31_notice_days;
		}

		public BigDecimal getR54_32_88_notice_days() {
			return r54_32_88_notice_days;
		}

		public void setR54_32_88_notice_days(BigDecimal r54_32_88_notice_days) {
			this.r54_32_88_notice_days = r54_32_88_notice_days;
		}

		public BigDecimal getR54_91_day_deposit_fixed_deposit_months() {
			return r54_91_day_deposit_fixed_deposit_months;
		}

		public void setR54_91_day_deposit_fixed_deposit_months(BigDecimal r54_91_day_deposit_fixed_deposit_months) {
			this.r54_91_day_deposit_fixed_deposit_months = r54_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR54_1_2_fixed_deposits_months() {
			return r54_1_2_fixed_deposits_months;
		}

		public void setR54_1_2_fixed_deposits_months(BigDecimal r54_1_2_fixed_deposits_months) {
			this.r54_1_2_fixed_deposits_months = r54_1_2_fixed_deposits_months;
		}

		public BigDecimal getR54_4_6_fixed_deposits_months() {
			return r54_4_6_fixed_deposits_months;
		}

		public void setR54_4_6_fixed_deposits_months(BigDecimal r54_4_6_fixed_deposits_months) {
			this.r54_4_6_fixed_deposits_months = r54_4_6_fixed_deposits_months;
		}

		public BigDecimal getR54_7_12_fixed_deposits_months() {
			return r54_7_12_fixed_deposits_months;
		}

		public void setR54_7_12_fixed_deposits_months(BigDecimal r54_7_12_fixed_deposits_months) {
			this.r54_7_12_fixed_deposits_months = r54_7_12_fixed_deposits_months;
		}

		public BigDecimal getR54_13_18_fixed_deposits_months() {
			return r54_13_18_fixed_deposits_months;
		}

		public void setR54_13_18_fixed_deposits_months(BigDecimal r54_13_18_fixed_deposits_months) {
			this.r54_13_18_fixed_deposits_months = r54_13_18_fixed_deposits_months;
		}

		public BigDecimal getR54_19_24_fixed_deposits_months() {
			return r54_19_24_fixed_deposits_months;
		}

		public void setR54_19_24_fixed_deposits_months(BigDecimal r54_19_24_fixed_deposits_months) {
			this.r54_19_24_fixed_deposits_months = r54_19_24_fixed_deposits_months;
		}

		public BigDecimal getR54_over_24_fixed_deposits_months() {
			return r54_over_24_fixed_deposits_months;
		}

		public void setR54_over_24_fixed_deposits_months(BigDecimal r54_over_24_fixed_deposits_months) {
			this.r54_over_24_fixed_deposits_months = r54_over_24_fixed_deposits_months;
		}

		public BigDecimal getR54_certificates_of_deposit() {
			return r54_certificates_of_deposit;
		}

		public void setR54_certificates_of_deposit(BigDecimal r54_certificates_of_deposit) {
			this.r54_certificates_of_deposit = r54_certificates_of_deposit;
		}

		public BigDecimal getR54_total() {
			return r54_total;
		}

		public void setR54_total(BigDecimal r54_total) {
			this.r54_total = r54_total;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_current() {
			return r55_current;
		}

		public void setR55_current(BigDecimal r55_current) {
			this.r55_current = r55_current;
		}

		public BigDecimal getR55_call() {
			return r55_call;
		}

		public void setR55_call(BigDecimal r55_call) {
			this.r55_call = r55_call;
		}

		public BigDecimal getR55_savings() {
			return r55_savings;
		}

		public void setR55_savings(BigDecimal r55_savings) {
			this.r55_savings = r55_savings;
		}

		public BigDecimal getR55_0_31_notice_days() {
			return r55_0_31_notice_days;
		}

		public void setR55_0_31_notice_days(BigDecimal r55_0_31_notice_days) {
			this.r55_0_31_notice_days = r55_0_31_notice_days;
		}

		public BigDecimal getR55_32_88_notice_days() {
			return r55_32_88_notice_days;
		}

		public void setR55_32_88_notice_days(BigDecimal r55_32_88_notice_days) {
			this.r55_32_88_notice_days = r55_32_88_notice_days;
		}

		public BigDecimal getR55_91_day_deposit_fixed_deposit_months() {
			return r55_91_day_deposit_fixed_deposit_months;
		}

		public void setR55_91_day_deposit_fixed_deposit_months(BigDecimal r55_91_day_deposit_fixed_deposit_months) {
			this.r55_91_day_deposit_fixed_deposit_months = r55_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR55_1_2_fixed_deposits_months() {
			return r55_1_2_fixed_deposits_months;
		}

		public void setR55_1_2_fixed_deposits_months(BigDecimal r55_1_2_fixed_deposits_months) {
			this.r55_1_2_fixed_deposits_months = r55_1_2_fixed_deposits_months;
		}

		public BigDecimal getR55_4_6_fixed_deposits_months() {
			return r55_4_6_fixed_deposits_months;
		}

		public void setR55_4_6_fixed_deposits_months(BigDecimal r55_4_6_fixed_deposits_months) {
			this.r55_4_6_fixed_deposits_months = r55_4_6_fixed_deposits_months;
		}

		public BigDecimal getR55_7_12_fixed_deposits_months() {
			return r55_7_12_fixed_deposits_months;
		}

		public void setR55_7_12_fixed_deposits_months(BigDecimal r55_7_12_fixed_deposits_months) {
			this.r55_7_12_fixed_deposits_months = r55_7_12_fixed_deposits_months;
		}

		public BigDecimal getR55_13_18_fixed_deposits_months() {
			return r55_13_18_fixed_deposits_months;
		}

		public void setR55_13_18_fixed_deposits_months(BigDecimal r55_13_18_fixed_deposits_months) {
			this.r55_13_18_fixed_deposits_months = r55_13_18_fixed_deposits_months;
		}

		public BigDecimal getR55_19_24_fixed_deposits_months() {
			return r55_19_24_fixed_deposits_months;
		}

		public void setR55_19_24_fixed_deposits_months(BigDecimal r55_19_24_fixed_deposits_months) {
			this.r55_19_24_fixed_deposits_months = r55_19_24_fixed_deposits_months;
		}

		public BigDecimal getR55_over_24_fixed_deposits_months() {
			return r55_over_24_fixed_deposits_months;
		}

		public void setR55_over_24_fixed_deposits_months(BigDecimal r55_over_24_fixed_deposits_months) {
			this.r55_over_24_fixed_deposits_months = r55_over_24_fixed_deposits_months;
		}

		public BigDecimal getR55_certificates_of_deposit() {
			return r55_certificates_of_deposit;
		}

		public void setR55_certificates_of_deposit(BigDecimal r55_certificates_of_deposit) {
			this.r55_certificates_of_deposit = r55_certificates_of_deposit;
		}

		public BigDecimal getR55_total() {
			return r55_total;
		}

		public void setR55_total(BigDecimal r55_total) {
			this.r55_total = r55_total;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_current() {
			return r56_current;
		}

		public void setR56_current(BigDecimal r56_current) {
			this.r56_current = r56_current;
		}

		public BigDecimal getR56_call() {
			return r56_call;
		}

		public void setR56_call(BigDecimal r56_call) {
			this.r56_call = r56_call;
		}

		public BigDecimal getR56_savings() {
			return r56_savings;
		}

		public void setR56_savings(BigDecimal r56_savings) {
			this.r56_savings = r56_savings;
		}

		public BigDecimal getR56_0_31_notice_days() {
			return r56_0_31_notice_days;
		}

		public void setR56_0_31_notice_days(BigDecimal r56_0_31_notice_days) {
			this.r56_0_31_notice_days = r56_0_31_notice_days;
		}

		public BigDecimal getR56_32_88_notice_days() {
			return r56_32_88_notice_days;
		}

		public void setR56_32_88_notice_days(BigDecimal r56_32_88_notice_days) {
			this.r56_32_88_notice_days = r56_32_88_notice_days;
		}

		public BigDecimal getR56_91_day_deposit_fixed_deposit_months() {
			return r56_91_day_deposit_fixed_deposit_months;
		}

		public void setR56_91_day_deposit_fixed_deposit_months(BigDecimal r56_91_day_deposit_fixed_deposit_months) {
			this.r56_91_day_deposit_fixed_deposit_months = r56_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR56_1_2_fixed_deposits_months() {
			return r56_1_2_fixed_deposits_months;
		}

		public void setR56_1_2_fixed_deposits_months(BigDecimal r56_1_2_fixed_deposits_months) {
			this.r56_1_2_fixed_deposits_months = r56_1_2_fixed_deposits_months;
		}

		public BigDecimal getR56_4_6_fixed_deposits_months() {
			return r56_4_6_fixed_deposits_months;
		}

		public void setR56_4_6_fixed_deposits_months(BigDecimal r56_4_6_fixed_deposits_months) {
			this.r56_4_6_fixed_deposits_months = r56_4_6_fixed_deposits_months;
		}

		public BigDecimal getR56_7_12_fixed_deposits_months() {
			return r56_7_12_fixed_deposits_months;
		}

		public void setR56_7_12_fixed_deposits_months(BigDecimal r56_7_12_fixed_deposits_months) {
			this.r56_7_12_fixed_deposits_months = r56_7_12_fixed_deposits_months;
		}

		public BigDecimal getR56_13_18_fixed_deposits_months() {
			return r56_13_18_fixed_deposits_months;
		}

		public void setR56_13_18_fixed_deposits_months(BigDecimal r56_13_18_fixed_deposits_months) {
			this.r56_13_18_fixed_deposits_months = r56_13_18_fixed_deposits_months;
		}

		public BigDecimal getR56_19_24_fixed_deposits_months() {
			return r56_19_24_fixed_deposits_months;
		}

		public void setR56_19_24_fixed_deposits_months(BigDecimal r56_19_24_fixed_deposits_months) {
			this.r56_19_24_fixed_deposits_months = r56_19_24_fixed_deposits_months;
		}

		public BigDecimal getR56_over_24_fixed_deposits_months() {
			return r56_over_24_fixed_deposits_months;
		}

		public void setR56_over_24_fixed_deposits_months(BigDecimal r56_over_24_fixed_deposits_months) {
			this.r56_over_24_fixed_deposits_months = r56_over_24_fixed_deposits_months;
		}

		public BigDecimal getR56_certificates_of_deposit() {
			return r56_certificates_of_deposit;
		}

		public void setR56_certificates_of_deposit(BigDecimal r56_certificates_of_deposit) {
			this.r56_certificates_of_deposit = r56_certificates_of_deposit;
		}

		public BigDecimal getR56_total() {
			return r56_total;
		}

		public void setR56_total(BigDecimal r56_total) {
			this.r56_total = r56_total;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_current() {
			return r57_current;
		}

		public void setR57_current(BigDecimal r57_current) {
			this.r57_current = r57_current;
		}

		public BigDecimal getR57_call() {
			return r57_call;
		}

		public void setR57_call(BigDecimal r57_call) {
			this.r57_call = r57_call;
		}

		public BigDecimal getR57_savings() {
			return r57_savings;
		}

		public void setR57_savings(BigDecimal r57_savings) {
			this.r57_savings = r57_savings;
		}

		public BigDecimal getR57_0_31_notice_days() {
			return r57_0_31_notice_days;
		}

		public void setR57_0_31_notice_days(BigDecimal r57_0_31_notice_days) {
			this.r57_0_31_notice_days = r57_0_31_notice_days;
		}

		public BigDecimal getR57_32_88_notice_days() {
			return r57_32_88_notice_days;
		}

		public void setR57_32_88_notice_days(BigDecimal r57_32_88_notice_days) {
			this.r57_32_88_notice_days = r57_32_88_notice_days;
		}

		public BigDecimal getR57_91_day_deposit_fixed_deposit_months() {
			return r57_91_day_deposit_fixed_deposit_months;
		}

		public void setR57_91_day_deposit_fixed_deposit_months(BigDecimal r57_91_day_deposit_fixed_deposit_months) {
			this.r57_91_day_deposit_fixed_deposit_months = r57_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR57_1_2_fixed_deposits_months() {
			return r57_1_2_fixed_deposits_months;
		}

		public void setR57_1_2_fixed_deposits_months(BigDecimal r57_1_2_fixed_deposits_months) {
			this.r57_1_2_fixed_deposits_months = r57_1_2_fixed_deposits_months;
		}

		public BigDecimal getR57_4_6_fixed_deposits_months() {
			return r57_4_6_fixed_deposits_months;
		}

		public void setR57_4_6_fixed_deposits_months(BigDecimal r57_4_6_fixed_deposits_months) {
			this.r57_4_6_fixed_deposits_months = r57_4_6_fixed_deposits_months;
		}

		public BigDecimal getR57_7_12_fixed_deposits_months() {
			return r57_7_12_fixed_deposits_months;
		}

		public void setR57_7_12_fixed_deposits_months(BigDecimal r57_7_12_fixed_deposits_months) {
			this.r57_7_12_fixed_deposits_months = r57_7_12_fixed_deposits_months;
		}

		public BigDecimal getR57_13_18_fixed_deposits_months() {
			return r57_13_18_fixed_deposits_months;
		}

		public void setR57_13_18_fixed_deposits_months(BigDecimal r57_13_18_fixed_deposits_months) {
			this.r57_13_18_fixed_deposits_months = r57_13_18_fixed_deposits_months;
		}

		public BigDecimal getR57_19_24_fixed_deposits_months() {
			return r57_19_24_fixed_deposits_months;
		}

		public void setR57_19_24_fixed_deposits_months(BigDecimal r57_19_24_fixed_deposits_months) {
			this.r57_19_24_fixed_deposits_months = r57_19_24_fixed_deposits_months;
		}

		public BigDecimal getR57_over_24_fixed_deposits_months() {
			return r57_over_24_fixed_deposits_months;
		}

		public void setR57_over_24_fixed_deposits_months(BigDecimal r57_over_24_fixed_deposits_months) {
			this.r57_over_24_fixed_deposits_months = r57_over_24_fixed_deposits_months;
		}

		public BigDecimal getR57_certificates_of_deposit() {
			return r57_certificates_of_deposit;
		}

		public void setR57_certificates_of_deposit(BigDecimal r57_certificates_of_deposit) {
			this.r57_certificates_of_deposit = r57_certificates_of_deposit;
		}

		public BigDecimal getR57_total() {
			return r57_total;
		}

		public void setR57_total(BigDecimal r57_total) {
			this.r57_total = r57_total;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public M_DEP1_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_DEP1_Archival_Summary_Entity {

		private String r11_product;
		private BigDecimal r11_current;
		private BigDecimal r11_call;
		private BigDecimal r11_savings;
		private BigDecimal r11_0_31_notice_days;
		private BigDecimal r11_32_88_notice_days;
		private BigDecimal r11_91_day_deposit_fixed_deposit_months;
		private BigDecimal r11_1_2_fixed_deposits_months;
		private BigDecimal r11_4_6_fixed_deposits_months;
		private BigDecimal r11_7_12_fixed_deposits_months;
		private BigDecimal r11_13_18_fixed_deposits_months;
		private BigDecimal r11_19_24_fixed_deposits_months;
		private BigDecimal r11_over_24_fixed_deposits_months;
		private BigDecimal r11_certificates_of_deposit;
		private BigDecimal r11_total;

		private String r12_product;
		private BigDecimal r12_current;
		private BigDecimal r12_call;
		private BigDecimal r12_savings;
		private BigDecimal r12_0_31_notice_days;
		private BigDecimal r12_32_88_notice_days;
		private BigDecimal r12_91_day_deposit_fixed_deposit_months;
		private BigDecimal r12_1_2_fixed_deposits_months;
		private BigDecimal r12_4_6_fixed_deposits_months;
		private BigDecimal r12_7_12_fixed_deposits_months;
		private BigDecimal r12_13_18_fixed_deposits_months;
		private BigDecimal r12_19_24_fixed_deposits_months;
		private BigDecimal r12_over_24_fixed_deposits_months;
		private BigDecimal r12_certificates_of_deposit;
		private BigDecimal r12_total;

		private String r13_product;
		private BigDecimal r13_current;
		private BigDecimal r13_call;
		private BigDecimal r13_savings;
		private BigDecimal r13_0_31_notice_days;
		private BigDecimal r13_32_88_notice_days;
		private BigDecimal r13_91_day_deposit_fixed_deposit_months;
		private BigDecimal r13_1_2_fixed_deposits_months;
		private BigDecimal r13_4_6_fixed_deposits_months;
		private BigDecimal r13_7_12_fixed_deposits_months;
		private BigDecimal r13_13_18_fixed_deposits_months;
		private BigDecimal r13_19_24_fixed_deposits_months;
		private BigDecimal r13_over_24_fixed_deposits_months;
		private BigDecimal r13_certificates_of_deposit;
		private BigDecimal r13_total;

		private String r14_product;
		private BigDecimal r14_current;
		private BigDecimal r14_call;
		private BigDecimal r14_savings;
		private BigDecimal r14_0_31_notice_days;
		private BigDecimal r14_32_88_notice_days;
		private BigDecimal r14_91_day_deposit_fixed_deposit_months;
		private BigDecimal r14_1_2_fixed_deposits_months;
		private BigDecimal r14_4_6_fixed_deposits_months;
		private BigDecimal r14_7_12_fixed_deposits_months;
		private BigDecimal r14_13_18_fixed_deposits_months;
		private BigDecimal r14_19_24_fixed_deposits_months;
		private BigDecimal r14_over_24_fixed_deposits_months;
		private BigDecimal r14_certificates_of_deposit;
		private BigDecimal r14_total;

		private String r15_product;
		private BigDecimal r15_current;
		private BigDecimal r15_call;
		private BigDecimal r15_savings;
		private BigDecimal r15_0_31_notice_days;
		private BigDecimal r15_32_88_notice_days;
		private BigDecimal r15_91_day_deposit_fixed_deposit_months;
		private BigDecimal r15_1_2_fixed_deposits_months;
		private BigDecimal r15_4_6_fixed_deposits_months;
		private BigDecimal r15_7_12_fixed_deposits_months;
		private BigDecimal r15_13_18_fixed_deposits_months;
		private BigDecimal r15_19_24_fixed_deposits_months;
		private BigDecimal r15_over_24_fixed_deposits_months;
		private BigDecimal r15_certificates_of_deposit;
		private BigDecimal r15_total;

		private String r16_product;
		private BigDecimal r16_current;
		private BigDecimal r16_call;
		private BigDecimal r16_savings;
		private BigDecimal r16_0_31_notice_days;
		private BigDecimal r16_32_88_notice_days;
		private BigDecimal r16_91_day_deposit_fixed_deposit_months;
		private BigDecimal r16_1_2_fixed_deposits_months;
		private BigDecimal r16_4_6_fixed_deposits_months;
		private BigDecimal r16_7_12_fixed_deposits_months;
		private BigDecimal r16_13_18_fixed_deposits_months;
		private BigDecimal r16_19_24_fixed_deposits_months;
		private BigDecimal r16_over_24_fixed_deposits_months;
		private BigDecimal r16_certificates_of_deposit;
		private BigDecimal r16_total;

		private String r17_product;
		private BigDecimal r17_current;
		private BigDecimal r17_call;
		private BigDecimal r17_savings;
		private BigDecimal r17_0_31_notice_days;
		private BigDecimal r17_32_88_notice_days;
		private BigDecimal r17_91_day_deposit_fixed_deposit_months;
		private BigDecimal r17_1_2_fixed_deposits_months;
		private BigDecimal r17_4_6_fixed_deposits_months;
		private BigDecimal r17_7_12_fixed_deposits_months;
		private BigDecimal r17_13_18_fixed_deposits_months;
		private BigDecimal r17_19_24_fixed_deposits_months;
		private BigDecimal r17_over_24_fixed_deposits_months;
		private BigDecimal r17_certificates_of_deposit;
		private BigDecimal r17_total;

		private String r18_product;
		private BigDecimal r18_current;
		private BigDecimal r18_call;
		private BigDecimal r18_savings;
		private BigDecimal r18_0_31_notice_days;
		private BigDecimal r18_32_88_notice_days;
		private BigDecimal r18_91_day_deposit_fixed_deposit_months;
		private BigDecimal r18_1_2_fixed_deposits_months;
		private BigDecimal r18_4_6_fixed_deposits_months;
		private BigDecimal r18_7_12_fixed_deposits_months;
		private BigDecimal r18_13_18_fixed_deposits_months;
		private BigDecimal r18_19_24_fixed_deposits_months;
		private BigDecimal r18_over_24_fixed_deposits_months;
		private BigDecimal r18_certificates_of_deposit;
		private BigDecimal r18_total;

		private String r19_product;
		private BigDecimal r19_current;
		private BigDecimal r19_call;
		private BigDecimal r19_savings;
		private BigDecimal r19_0_31_notice_days;
		private BigDecimal r19_32_88_notice_days;
		private BigDecimal r19_91_day_deposit_fixed_deposit_months;
		private BigDecimal r19_1_2_fixed_deposits_months;
		private BigDecimal r19_4_6_fixed_deposits_months;
		private BigDecimal r19_7_12_fixed_deposits_months;
		private BigDecimal r19_13_18_fixed_deposits_months;
		private BigDecimal r19_19_24_fixed_deposits_months;
		private BigDecimal r19_over_24_fixed_deposits_months;
		private BigDecimal r19_certificates_of_deposit;
		private BigDecimal r19_total;

		private String r20_product;
		private BigDecimal r20_current;
		private BigDecimal r20_call;
		private BigDecimal r20_savings;
		private BigDecimal r20_0_31_notice_days;
		private BigDecimal r20_32_88_notice_days;
		private BigDecimal r20_91_day_deposit_fixed_deposit_months;
		private BigDecimal r20_1_2_fixed_deposits_months;
		private BigDecimal r20_4_6_fixed_deposits_months;
		private BigDecimal r20_7_12_fixed_deposits_months;
		private BigDecimal r20_13_18_fixed_deposits_months;
		private BigDecimal r20_19_24_fixed_deposits_months;
		private BigDecimal r20_over_24_fixed_deposits_months;
		private BigDecimal r20_certificates_of_deposit;
		private BigDecimal r20_total;

		private String r21_product;
		private BigDecimal r21_current;
		private BigDecimal r21_call;
		private BigDecimal r21_savings;
		private BigDecimal r21_0_31_notice_days;
		private BigDecimal r21_32_88_notice_days;
		private BigDecimal r21_91_day_deposit_fixed_deposit_months;
		private BigDecimal r21_1_2_fixed_deposits_months;
		private BigDecimal r21_4_6_fixed_deposits_months;
		private BigDecimal r21_7_12_fixed_deposits_months;
		private BigDecimal r21_13_18_fixed_deposits_months;
		private BigDecimal r21_19_24_fixed_deposits_months;
		private BigDecimal r21_over_24_fixed_deposits_months;
		private BigDecimal r21_certificates_of_deposit;
		private BigDecimal r21_total;

		private String r22_product;
		private BigDecimal r22_current;
		private BigDecimal r22_call;
		private BigDecimal r22_savings;
		private BigDecimal r22_0_31_notice_days;
		private BigDecimal r22_32_88_notice_days;
		private BigDecimal r22_91_day_deposit_fixed_deposit_months;
		private BigDecimal r22_1_2_fixed_deposits_months;
		private BigDecimal r22_4_6_fixed_deposits_months;
		private BigDecimal r22_7_12_fixed_deposits_months;
		private BigDecimal r22_13_18_fixed_deposits_months;
		private BigDecimal r22_19_24_fixed_deposits_months;
		private BigDecimal r22_over_24_fixed_deposits_months;
		private BigDecimal r22_certificates_of_deposit;
		private BigDecimal r22_total;

		private String r23_product;
		private BigDecimal r23_current;
		private BigDecimal r23_call;
		private BigDecimal r23_savings;
		private BigDecimal r23_0_31_notice_days;
		private BigDecimal r23_32_88_notice_days;
		private BigDecimal r23_91_day_deposit_fixed_deposit_months;
		private BigDecimal r23_1_2_fixed_deposits_months;
		private BigDecimal r23_4_6_fixed_deposits_months;
		private BigDecimal r23_7_12_fixed_deposits_months;
		private BigDecimal r23_13_18_fixed_deposits_months;
		private BigDecimal r23_19_24_fixed_deposits_months;
		private BigDecimal r23_over_24_fixed_deposits_months;
		private BigDecimal r23_certificates_of_deposit;
		private BigDecimal r23_total;

		private String r24_product;
		private BigDecimal r24_current;
		private BigDecimal r24_call;
		private BigDecimal r24_savings;
		private BigDecimal r24_0_31_notice_days;
		private BigDecimal r24_32_88_notice_days;
		private BigDecimal r24_91_day_deposit_fixed_deposit_months;
		private BigDecimal r24_1_2_fixed_deposits_months;
		private BigDecimal r24_4_6_fixed_deposits_months;
		private BigDecimal r24_7_12_fixed_deposits_months;
		private BigDecimal r24_13_18_fixed_deposits_months;
		private BigDecimal r24_19_24_fixed_deposits_months;
		private BigDecimal r24_over_24_fixed_deposits_months;
		private BigDecimal r24_certificates_of_deposit;
		private BigDecimal r24_total;

		private String r25_product;
		private BigDecimal r25_current;
		private BigDecimal r25_call;
		private BigDecimal r25_savings;
		private BigDecimal r25_0_31_notice_days;
		private BigDecimal r25_32_88_notice_days;
		private BigDecimal r25_91_day_deposit_fixed_deposit_months;
		private BigDecimal r25_1_2_fixed_deposits_months;
		private BigDecimal r25_4_6_fixed_deposits_months;
		private BigDecimal r25_7_12_fixed_deposits_months;
		private BigDecimal r25_13_18_fixed_deposits_months;
		private BigDecimal r25_19_24_fixed_deposits_months;
		private BigDecimal r25_over_24_fixed_deposits_months;
		private BigDecimal r25_certificates_of_deposit;
		private BigDecimal r25_total;

		private String r26_product;
		private BigDecimal r26_current;
		private BigDecimal r26_call;
		private BigDecimal r26_savings;
		private BigDecimal r26_0_31_notice_days;
		private BigDecimal r26_32_88_notice_days;
		private BigDecimal r26_91_day_deposit_fixed_deposit_months;
		private BigDecimal r26_1_2_fixed_deposits_months;
		private BigDecimal r26_4_6_fixed_deposits_months;
		private BigDecimal r26_7_12_fixed_deposits_months;
		private BigDecimal r26_13_18_fixed_deposits_months;
		private BigDecimal r26_19_24_fixed_deposits_months;
		private BigDecimal r26_over_24_fixed_deposits_months;
		private BigDecimal r26_certificates_of_deposit;
		private BigDecimal r26_total;

		private String r27_product;
		private BigDecimal r27_current;
		private BigDecimal r27_call;
		private BigDecimal r27_savings;
		private BigDecimal r27_0_31_notice_days;
		private BigDecimal r27_32_88_notice_days;
		private BigDecimal r27_91_day_deposit_fixed_deposit_months;
		private BigDecimal r27_1_2_fixed_deposits_months;
		private BigDecimal r27_4_6_fixed_deposits_months;
		private BigDecimal r27_7_12_fixed_deposits_months;
		private BigDecimal r27_13_18_fixed_deposits_months;
		private BigDecimal r27_19_24_fixed_deposits_months;
		private BigDecimal r27_over_24_fixed_deposits_months;
		private BigDecimal r27_certificates_of_deposit;
		private BigDecimal r27_total;

		private String r28_product;
		private BigDecimal r28_current;
		private BigDecimal r28_call;
		private BigDecimal r28_savings;
		private BigDecimal r28_0_31_notice_days;
		private BigDecimal r28_32_88_notice_days;
		private BigDecimal r28_91_day_deposit_fixed_deposit_months;
		private BigDecimal r28_1_2_fixed_deposits_months;
		private BigDecimal r28_4_6_fixed_deposits_months;
		private BigDecimal r28_7_12_fixed_deposits_months;
		private BigDecimal r28_13_18_fixed_deposits_months;
		private BigDecimal r28_19_24_fixed_deposits_months;
		private BigDecimal r28_over_24_fixed_deposits_months;
		private BigDecimal r28_certificates_of_deposit;
		private BigDecimal r28_total;

		private String r29_product;
		private BigDecimal r29_current;
		private BigDecimal r29_call;
		private BigDecimal r29_savings;
		private BigDecimal r29_0_31_notice_days;
		private BigDecimal r29_32_88_notice_days;
		private BigDecimal r29_91_day_deposit_fixed_deposit_months;
		private BigDecimal r29_1_2_fixed_deposits_months;
		private BigDecimal r29_4_6_fixed_deposits_months;
		private BigDecimal r29_7_12_fixed_deposits_months;
		private BigDecimal r29_13_18_fixed_deposits_months;
		private BigDecimal r29_19_24_fixed_deposits_months;
		private BigDecimal r29_over_24_fixed_deposits_months;
		private BigDecimal r29_certificates_of_deposit;
		private BigDecimal r29_total;

		private String r30_product;
		private BigDecimal r30_current;
		private BigDecimal r30_call;
		private BigDecimal r30_savings;
		private BigDecimal r30_0_31_notice_days;
		private BigDecimal r30_32_88_notice_days;
		private BigDecimal r30_91_day_deposit_fixed_deposit_months;
		private BigDecimal r30_1_2_fixed_deposits_months;
		private BigDecimal r30_4_6_fixed_deposits_months;
		private BigDecimal r30_7_12_fixed_deposits_months;
		private BigDecimal r30_13_18_fixed_deposits_months;
		private BigDecimal r30_19_24_fixed_deposits_months;
		private BigDecimal r30_over_24_fixed_deposits_months;
		private BigDecimal r30_certificates_of_deposit;
		private BigDecimal r30_total;

		private String r31_product;
		private BigDecimal r31_current;
		private BigDecimal r31_call;
		private BigDecimal r31_savings;
		private BigDecimal r31_0_31_notice_days;
		private BigDecimal r31_32_88_notice_days;
		private BigDecimal r31_91_day_deposit_fixed_deposit_months;
		private BigDecimal r31_1_2_fixed_deposits_months;
		private BigDecimal r31_4_6_fixed_deposits_months;
		private BigDecimal r31_7_12_fixed_deposits_months;
		private BigDecimal r31_13_18_fixed_deposits_months;
		private BigDecimal r31_19_24_fixed_deposits_months;
		private BigDecimal r31_over_24_fixed_deposits_months;
		private BigDecimal r31_certificates_of_deposit;
		private BigDecimal r31_total;

		private String r32_product;
		private BigDecimal r32_current;
		private BigDecimal r32_call;
		private BigDecimal r32_savings;
		private BigDecimal r32_0_31_notice_days;
		private BigDecimal r32_32_88_notice_days;
		private BigDecimal r32_91_day_deposit_fixed_deposit_months;
		private BigDecimal r32_1_2_fixed_deposits_months;
		private BigDecimal r32_4_6_fixed_deposits_months;
		private BigDecimal r32_7_12_fixed_deposits_months;
		private BigDecimal r32_13_18_fixed_deposits_months;
		private BigDecimal r32_19_24_fixed_deposits_months;
		private BigDecimal r32_over_24_fixed_deposits_months;
		private BigDecimal r32_certificates_of_deposit;
		private BigDecimal r32_total;

		private String r33_product;
		private BigDecimal r33_current;
		private BigDecimal r33_call;
		private BigDecimal r33_savings;
		private BigDecimal r33_0_31_notice_days;
		private BigDecimal r33_32_88_notice_days;
		private BigDecimal r33_91_day_deposit_fixed_deposit_months;
		private BigDecimal r33_1_2_fixed_deposits_months;
		private BigDecimal r33_4_6_fixed_deposits_months;
		private BigDecimal r33_7_12_fixed_deposits_months;
		private BigDecimal r33_13_18_fixed_deposits_months;
		private BigDecimal r33_19_24_fixed_deposits_months;
		private BigDecimal r33_over_24_fixed_deposits_months;
		private BigDecimal r33_certificates_of_deposit;
		private BigDecimal r33_total;

		private String r34_product;
		private BigDecimal r34_current;
		private BigDecimal r34_call;
		private BigDecimal r34_savings;
		private BigDecimal r34_0_31_notice_days;
		private BigDecimal r34_32_88_notice_days;
		private BigDecimal r34_91_day_deposit_fixed_deposit_months;
		private BigDecimal r34_1_2_fixed_deposits_months;
		private BigDecimal r34_4_6_fixed_deposits_months;
		private BigDecimal r34_7_12_fixed_deposits_months;
		private BigDecimal r34_13_18_fixed_deposits_months;
		private BigDecimal r34_19_24_fixed_deposits_months;
		private BigDecimal r34_over_24_fixed_deposits_months;
		private BigDecimal r34_certificates_of_deposit;
		private BigDecimal r34_total;

		private String r35_product;
		private BigDecimal r35_current;
		private BigDecimal r35_call;
		private BigDecimal r35_savings;
		private BigDecimal r35_0_31_notice_days;
		private BigDecimal r35_32_88_notice_days;
		private BigDecimal r35_91_day_deposit_fixed_deposit_months;
		private BigDecimal r35_1_2_fixed_deposits_months;
		private BigDecimal r35_4_6_fixed_deposits_months;
		private BigDecimal r35_7_12_fixed_deposits_months;
		private BigDecimal r35_13_18_fixed_deposits_months;
		private BigDecimal r35_19_24_fixed_deposits_months;
		private BigDecimal r35_over_24_fixed_deposits_months;
		private BigDecimal r35_certificates_of_deposit;
		private BigDecimal r35_total;

		private String r36_product;
		private BigDecimal r36_current;
		private BigDecimal r36_call;
		private BigDecimal r36_savings;
		private BigDecimal r36_0_31_notice_days;
		private BigDecimal r36_32_88_notice_days;
		private BigDecimal r36_91_day_deposit_fixed_deposit_months;
		private BigDecimal r36_1_2_fixed_deposits_months;
		private BigDecimal r36_4_6_fixed_deposits_months;
		private BigDecimal r36_7_12_fixed_deposits_months;
		private BigDecimal r36_13_18_fixed_deposits_months;
		private BigDecimal r36_19_24_fixed_deposits_months;
		private BigDecimal r36_over_24_fixed_deposits_months;
		private BigDecimal r36_certificates_of_deposit;
		private BigDecimal r36_total;

		private String r37_product;
		private BigDecimal r37_current;
		private BigDecimal r37_call;
		private BigDecimal r37_savings;
		private BigDecimal r37_0_31_notice_days;
		private BigDecimal r37_32_88_notice_days;
		private BigDecimal r37_91_day_deposit_fixed_deposit_months;
		private BigDecimal r37_1_2_fixed_deposits_months;
		private BigDecimal r37_4_6_fixed_deposits_months;
		private BigDecimal r37_7_12_fixed_deposits_months;
		private BigDecimal r37_13_18_fixed_deposits_months;
		private BigDecimal r37_19_24_fixed_deposits_months;
		private BigDecimal r37_over_24_fixed_deposits_months;
		private BigDecimal r37_certificates_of_deposit;
		private BigDecimal r37_total;

		private String r38_product;
		private BigDecimal r38_current;
		private BigDecimal r38_call;
		private BigDecimal r38_savings;
		private BigDecimal r38_0_31_notice_days;
		private BigDecimal r38_32_88_notice_days;
		private BigDecimal r38_91_day_deposit_fixed_deposit_months;
		private BigDecimal r38_1_2_fixed_deposits_months;
		private BigDecimal r38_4_6_fixed_deposits_months;
		private BigDecimal r38_7_12_fixed_deposits_months;
		private BigDecimal r38_13_18_fixed_deposits_months;
		private BigDecimal r38_19_24_fixed_deposits_months;
		private BigDecimal r38_over_24_fixed_deposits_months;
		private BigDecimal r38_certificates_of_deposit;
		private BigDecimal r38_total;

		private String r39_product;
		private BigDecimal r39_current;
		private BigDecimal r39_call;
		private BigDecimal r39_savings;
		private BigDecimal r39_0_31_notice_days;
		private BigDecimal r39_32_88_notice_days;
		private BigDecimal r39_91_day_deposit_fixed_deposit_months;
		private BigDecimal r39_1_2_fixed_deposits_months;
		private BigDecimal r39_4_6_fixed_deposits_months;
		private BigDecimal r39_7_12_fixed_deposits_months;
		private BigDecimal r39_13_18_fixed_deposits_months;
		private BigDecimal r39_19_24_fixed_deposits_months;
		private BigDecimal r39_over_24_fixed_deposits_months;
		private BigDecimal r39_certificates_of_deposit;
		private BigDecimal r39_total;

		private String r40_product;
		private BigDecimal r40_current;
		private BigDecimal r40_call;
		private BigDecimal r40_savings;
		private BigDecimal r40_0_31_notice_days;
		private BigDecimal r40_32_88_notice_days;
		private BigDecimal r40_91_day_deposit_fixed_deposit_months;
		private BigDecimal r40_1_2_fixed_deposits_months;
		private BigDecimal r40_4_6_fixed_deposits_months;
		private BigDecimal r40_7_12_fixed_deposits_months;
		private BigDecimal r40_13_18_fixed_deposits_months;
		private BigDecimal r40_19_24_fixed_deposits_months;
		private BigDecimal r40_over_24_fixed_deposits_months;
		private BigDecimal r40_certificates_of_deposit;
		private BigDecimal r40_total;

		private String r41_product;
		private BigDecimal r41_current;
		private BigDecimal r41_call;
		private BigDecimal r41_savings;
		private BigDecimal r41_0_31_notice_days;
		private BigDecimal r41_32_88_notice_days;
		private BigDecimal r41_91_day_deposit_fixed_deposit_months;
		private BigDecimal r41_1_2_fixed_deposits_months;
		private BigDecimal r41_4_6_fixed_deposits_months;
		private BigDecimal r41_7_12_fixed_deposits_months;
		private BigDecimal r41_13_18_fixed_deposits_months;
		private BigDecimal r41_19_24_fixed_deposits_months;
		private BigDecimal r41_over_24_fixed_deposits_months;
		private BigDecimal r41_certificates_of_deposit;
		private BigDecimal r41_total;

		private String r42_product;
		private BigDecimal r42_current;
		private BigDecimal r42_call;
		private BigDecimal r42_savings;
		private BigDecimal r42_0_31_notice_days;
		private BigDecimal r42_32_88_notice_days;
		private BigDecimal r42_91_day_deposit_fixed_deposit_months;
		private BigDecimal r42_1_2_fixed_deposits_months;
		private BigDecimal r42_4_6_fixed_deposits_months;
		private BigDecimal r42_7_12_fixed_deposits_months;
		private BigDecimal r42_13_18_fixed_deposits_months;
		private BigDecimal r42_19_24_fixed_deposits_months;
		private BigDecimal r42_over_24_fixed_deposits_months;
		private BigDecimal r42_certificates_of_deposit;
		private BigDecimal r42_total;

		private String r43_product;
		private BigDecimal r43_current;
		private BigDecimal r43_call;
		private BigDecimal r43_savings;
		private BigDecimal r43_0_31_notice_days;
		private BigDecimal r43_32_88_notice_days;
		private BigDecimal r43_91_day_deposit_fixed_deposit_months;
		private BigDecimal r43_1_2_fixed_deposits_months;
		private BigDecimal r43_4_6_fixed_deposits_months;
		private BigDecimal r43_7_12_fixed_deposits_months;
		private BigDecimal r43_13_18_fixed_deposits_months;
		private BigDecimal r43_19_24_fixed_deposits_months;
		private BigDecimal r43_over_24_fixed_deposits_months;
		private BigDecimal r43_certificates_of_deposit;
		private BigDecimal r43_total;

		private String r44_product;
		private BigDecimal r44_current;
		private BigDecimal r44_call;
		private BigDecimal r44_savings;
		private BigDecimal r44_0_31_notice_days;
		private BigDecimal r44_32_88_notice_days;
		private BigDecimal r44_91_day_deposit_fixed_deposit_months;
		private BigDecimal r44_1_2_fixed_deposits_months;
		private BigDecimal r44_4_6_fixed_deposits_months;
		private BigDecimal r44_7_12_fixed_deposits_months;
		private BigDecimal r44_13_18_fixed_deposits_months;
		private BigDecimal r44_19_24_fixed_deposits_months;
		private BigDecimal r44_over_24_fixed_deposits_months;
		private BigDecimal r44_certificates_of_deposit;
		private BigDecimal r44_total;

		private String r45_product;
		private BigDecimal r45_current;
		private BigDecimal r45_call;
		private BigDecimal r45_savings;
		private BigDecimal r45_0_31_notice_days;
		private BigDecimal r45_32_88_notice_days;
		private BigDecimal r45_91_day_deposit_fixed_deposit_months;
		private BigDecimal r45_1_2_fixed_deposits_months;
		private BigDecimal r45_4_6_fixed_deposits_months;
		private BigDecimal r45_7_12_fixed_deposits_months;
		private BigDecimal r45_13_18_fixed_deposits_months;
		private BigDecimal r45_19_24_fixed_deposits_months;
		private BigDecimal r45_over_24_fixed_deposits_months;
		private BigDecimal r45_certificates_of_deposit;
		private BigDecimal r45_total;

		private String r46_product;
		private BigDecimal r46_current;
		private BigDecimal r46_call;
		private BigDecimal r46_savings;
		private BigDecimal r46_0_31_notice_days;
		private BigDecimal r46_32_88_notice_days;
		private BigDecimal r46_91_day_deposit_fixed_deposit_months;
		private BigDecimal r46_1_2_fixed_deposits_months;
		private BigDecimal r46_4_6_fixed_deposits_months;
		private BigDecimal r46_7_12_fixed_deposits_months;
		private BigDecimal r46_13_18_fixed_deposits_months;
		private BigDecimal r46_19_24_fixed_deposits_months;
		private BigDecimal r46_over_24_fixed_deposits_months;
		private BigDecimal r46_certificates_of_deposit;
		private BigDecimal r46_total;

		private String r47_product;
		private BigDecimal r47_current;
		private BigDecimal r47_call;
		private BigDecimal r47_savings;
		private BigDecimal r47_0_31_notice_days;
		private BigDecimal r47_32_88_notice_days;
		private BigDecimal r47_91_day_deposit_fixed_deposit_months;
		private BigDecimal r47_1_2_fixed_deposits_months;
		private BigDecimal r47_4_6_fixed_deposits_months;
		private BigDecimal r47_7_12_fixed_deposits_months;
		private BigDecimal r47_13_18_fixed_deposits_months;
		private BigDecimal r47_19_24_fixed_deposits_months;
		private BigDecimal r47_over_24_fixed_deposits_months;
		private BigDecimal r47_certificates_of_deposit;
		private BigDecimal r47_total;

		private String r48_product;
		private BigDecimal r48_current;
		private BigDecimal r48_call;
		private BigDecimal r48_savings;
		private BigDecimal r48_0_31_notice_days;
		private BigDecimal r48_32_88_notice_days;
		private BigDecimal r48_91_day_deposit_fixed_deposit_months;
		private BigDecimal r48_1_2_fixed_deposits_months;
		private BigDecimal r48_4_6_fixed_deposits_months;
		private BigDecimal r48_7_12_fixed_deposits_months;
		private BigDecimal r48_13_18_fixed_deposits_months;
		private BigDecimal r48_19_24_fixed_deposits_months;
		private BigDecimal r48_over_24_fixed_deposits_months;
		private BigDecimal r48_certificates_of_deposit;
		private BigDecimal r48_total;

		private String r49_product;
		private BigDecimal r49_current;
		private BigDecimal r49_call;
		private BigDecimal r49_savings;
		private BigDecimal r49_0_31_notice_days;
		private BigDecimal r49_32_88_notice_days;
		private BigDecimal r49_91_day_deposit_fixed_deposit_months;
		private BigDecimal r49_1_2_fixed_deposits_months;
		private BigDecimal r49_4_6_fixed_deposits_months;
		private BigDecimal r49_7_12_fixed_deposits_months;
		private BigDecimal r49_13_18_fixed_deposits_months;
		private BigDecimal r49_19_24_fixed_deposits_months;
		private BigDecimal r49_over_24_fixed_deposits_months;
		private BigDecimal r49_certificates_of_deposit;
		private BigDecimal r49_total;

		private String r50_product;
		private BigDecimal r50_current;
		private BigDecimal r50_call;
		private BigDecimal r50_savings;
		private BigDecimal r50_0_31_notice_days;
		private BigDecimal r50_32_88_notice_days;
		private BigDecimal r50_91_day_deposit_fixed_deposit_months;
		private BigDecimal r50_1_2_fixed_deposits_months;
		private BigDecimal r50_4_6_fixed_deposits_months;
		private BigDecimal r50_7_12_fixed_deposits_months;
		private BigDecimal r50_13_18_fixed_deposits_months;
		private BigDecimal r50_19_24_fixed_deposits_months;
		private BigDecimal r50_over_24_fixed_deposits_months;
		private BigDecimal r50_certificates_of_deposit;
		private BigDecimal r50_total;

		private String r51_product;
		private BigDecimal r51_current;
		private BigDecimal r51_call;
		private BigDecimal r51_savings;
		private BigDecimal r51_0_31_notice_days;
		private BigDecimal r51_32_88_notice_days;
		private BigDecimal r51_91_day_deposit_fixed_deposit_months;
		private BigDecimal r51_1_2_fixed_deposits_months;
		private BigDecimal r51_4_6_fixed_deposits_months;
		private BigDecimal r51_7_12_fixed_deposits_months;
		private BigDecimal r51_13_18_fixed_deposits_months;
		private BigDecimal r51_19_24_fixed_deposits_months;
		private BigDecimal r51_over_24_fixed_deposits_months;
		private BigDecimal r51_certificates_of_deposit;
		private BigDecimal r51_total;

		private String r52_product;
		private BigDecimal r52_current;
		private BigDecimal r52_call;
		private BigDecimal r52_savings;
		private BigDecimal r52_0_31_notice_days;
		private BigDecimal r52_32_88_notice_days;
		private BigDecimal r52_91_day_deposit_fixed_deposit_months;
		private BigDecimal r52_1_2_fixed_deposits_months;
		private BigDecimal r52_4_6_fixed_deposits_months;
		private BigDecimal r52_7_12_fixed_deposits_months;
		private BigDecimal r52_13_18_fixed_deposits_months;
		private BigDecimal r52_19_24_fixed_deposits_months;
		private BigDecimal r52_over_24_fixed_deposits_months;
		private BigDecimal r52_certificates_of_deposit;
		private BigDecimal r52_total;

		private String r53_product;
		private BigDecimal r53_current;
		private BigDecimal r53_call;
		private BigDecimal r53_savings;
		private BigDecimal r53_0_31_notice_days;
		private BigDecimal r53_32_88_notice_days;
		private BigDecimal r53_91_day_deposit_fixed_deposit_months;
		private BigDecimal r53_1_2_fixed_deposits_months;
		private BigDecimal r53_4_6_fixed_deposits_months;
		private BigDecimal r53_7_12_fixed_deposits_months;
		private BigDecimal r53_13_18_fixed_deposits_months;
		private BigDecimal r53_19_24_fixed_deposits_months;
		private BigDecimal r53_over_24_fixed_deposits_months;
		private BigDecimal r53_certificates_of_deposit;
		private BigDecimal r53_total;

		private String r54_product;
		private BigDecimal r54_current;
		private BigDecimal r54_call;
		private BigDecimal r54_savings;
		private BigDecimal r54_0_31_notice_days;
		private BigDecimal r54_32_88_notice_days;
		private BigDecimal r54_91_day_deposit_fixed_deposit_months;
		private BigDecimal r54_1_2_fixed_deposits_months;
		private BigDecimal r54_4_6_fixed_deposits_months;
		private BigDecimal r54_7_12_fixed_deposits_months;
		private BigDecimal r54_13_18_fixed_deposits_months;
		private BigDecimal r54_19_24_fixed_deposits_months;
		private BigDecimal r54_over_24_fixed_deposits_months;
		private BigDecimal r54_certificates_of_deposit;
		private BigDecimal r54_total;

		private String r55_product;
		private BigDecimal r55_current;
		private BigDecimal r55_call;
		private BigDecimal r55_savings;
		private BigDecimal r55_0_31_notice_days;
		private BigDecimal r55_32_88_notice_days;
		private BigDecimal r55_91_day_deposit_fixed_deposit_months;
		private BigDecimal r55_1_2_fixed_deposits_months;
		private BigDecimal r55_4_6_fixed_deposits_months;
		private BigDecimal r55_7_12_fixed_deposits_months;
		private BigDecimal r55_13_18_fixed_deposits_months;
		private BigDecimal r55_19_24_fixed_deposits_months;
		private BigDecimal r55_over_24_fixed_deposits_months;
		private BigDecimal r55_certificates_of_deposit;
		private BigDecimal r55_total;

		private String r56_product;
		private BigDecimal r56_current;
		private BigDecimal r56_call;
		private BigDecimal r56_savings;
		private BigDecimal r56_0_31_notice_days;
		private BigDecimal r56_32_88_notice_days;
		private BigDecimal r56_91_day_deposit_fixed_deposit_months;
		private BigDecimal r56_1_2_fixed_deposits_months;
		private BigDecimal r56_4_6_fixed_deposits_months;
		private BigDecimal r56_7_12_fixed_deposits_months;
		private BigDecimal r56_13_18_fixed_deposits_months;
		private BigDecimal r56_19_24_fixed_deposits_months;
		private BigDecimal r56_over_24_fixed_deposits_months;
		private BigDecimal r56_certificates_of_deposit;
		private BigDecimal r56_total;

		private String r57_product;
		private BigDecimal r57_current;
		private BigDecimal r57_call;
		private BigDecimal r57_savings;
		private BigDecimal r57_0_31_notice_days;
		private BigDecimal r57_32_88_notice_days;
		private BigDecimal r57_91_day_deposit_fixed_deposit_months;
		private BigDecimal r57_1_2_fixed_deposits_months;
		private BigDecimal r57_4_6_fixed_deposits_months;
		private BigDecimal r57_7_12_fixed_deposits_months;
		private BigDecimal r57_13_18_fixed_deposits_months;
		private BigDecimal r57_19_24_fixed_deposits_months;
		private BigDecimal r57_over_24_fixed_deposits_months;
		private BigDecimal r57_certificates_of_deposit;
		private BigDecimal r57_total;

		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_current() {
			return r11_current;
		}

		public void setR11_current(BigDecimal r11_current) {
			this.r11_current = r11_current;
		}

		public BigDecimal getR11_call() {
			return r11_call;
		}

		public void setR11_call(BigDecimal r11_call) {
			this.r11_call = r11_call;
		}

		public BigDecimal getR11_savings() {
			return r11_savings;
		}

		public void setR11_savings(BigDecimal r11_savings) {
			this.r11_savings = r11_savings;
		}

		public BigDecimal getR11_0_31_notice_days() {
			return r11_0_31_notice_days;
		}

		public void setR11_0_31_notice_days(BigDecimal r11_0_31_notice_days) {
			this.r11_0_31_notice_days = r11_0_31_notice_days;
		}

		public BigDecimal getR11_32_88_notice_days() {
			return r11_32_88_notice_days;
		}

		public void setR11_32_88_notice_days(BigDecimal r11_32_88_notice_days) {
			this.r11_32_88_notice_days = r11_32_88_notice_days;
		}

		public BigDecimal getR11_91_day_deposit_fixed_deposit_months() {
			return r11_91_day_deposit_fixed_deposit_months;
		}

		public void setR11_91_day_deposit_fixed_deposit_months(BigDecimal r11_91_day_deposit_fixed_deposit_months) {
			this.r11_91_day_deposit_fixed_deposit_months = r11_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR11_1_2_fixed_deposits_months() {
			return r11_1_2_fixed_deposits_months;
		}

		public void setR11_1_2_fixed_deposits_months(BigDecimal r11_1_2_fixed_deposits_months) {
			this.r11_1_2_fixed_deposits_months = r11_1_2_fixed_deposits_months;
		}

		public BigDecimal getR11_4_6_fixed_deposits_months() {
			return r11_4_6_fixed_deposits_months;
		}

		public void setR11_4_6_fixed_deposits_months(BigDecimal r11_4_6_fixed_deposits_months) {
			this.r11_4_6_fixed_deposits_months = r11_4_6_fixed_deposits_months;
		}

		public BigDecimal getR11_7_12_fixed_deposits_months() {
			return r11_7_12_fixed_deposits_months;
		}

		public void setR11_7_12_fixed_deposits_months(BigDecimal r11_7_12_fixed_deposits_months) {
			this.r11_7_12_fixed_deposits_months = r11_7_12_fixed_deposits_months;
		}

		public BigDecimal getR11_13_18_fixed_deposits_months() {
			return r11_13_18_fixed_deposits_months;
		}

		public void setR11_13_18_fixed_deposits_months(BigDecimal r11_13_18_fixed_deposits_months) {
			this.r11_13_18_fixed_deposits_months = r11_13_18_fixed_deposits_months;
		}

		public BigDecimal getR11_19_24_fixed_deposits_months() {
			return r11_19_24_fixed_deposits_months;
		}

		public void setR11_19_24_fixed_deposits_months(BigDecimal r11_19_24_fixed_deposits_months) {
			this.r11_19_24_fixed_deposits_months = r11_19_24_fixed_deposits_months;
		}

		public BigDecimal getR11_over_24_fixed_deposits_months() {
			return r11_over_24_fixed_deposits_months;
		}

		public void setR11_over_24_fixed_deposits_months(BigDecimal r11_over_24_fixed_deposits_months) {
			this.r11_over_24_fixed_deposits_months = r11_over_24_fixed_deposits_months;
		}

		public BigDecimal getR11_certificates_of_deposit() {
			return r11_certificates_of_deposit;
		}

		public void setR11_certificates_of_deposit(BigDecimal r11_certificates_of_deposit) {
			this.r11_certificates_of_deposit = r11_certificates_of_deposit;
		}

		public BigDecimal getR11_total() {
			return r11_total;
		}

		public void setR11_total(BigDecimal r11_total) {
			this.r11_total = r11_total;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_current() {
			return r12_current;
		}

		public void setR12_current(BigDecimal r12_current) {
			this.r12_current = r12_current;
		}

		public BigDecimal getR12_call() {
			return r12_call;
		}

		public void setR12_call(BigDecimal r12_call) {
			this.r12_call = r12_call;
		}

		public BigDecimal getR12_savings() {
			return r12_savings;
		}

		public void setR12_savings(BigDecimal r12_savings) {
			this.r12_savings = r12_savings;
		}

		public BigDecimal getR12_0_31_notice_days() {
			return r12_0_31_notice_days;
		}

		public void setR12_0_31_notice_days(BigDecimal r12_0_31_notice_days) {
			this.r12_0_31_notice_days = r12_0_31_notice_days;
		}

		public BigDecimal getR12_32_88_notice_days() {
			return r12_32_88_notice_days;
		}

		public void setR12_32_88_notice_days(BigDecimal r12_32_88_notice_days) {
			this.r12_32_88_notice_days = r12_32_88_notice_days;
		}

		public BigDecimal getR12_91_day_deposit_fixed_deposit_months() {
			return r12_91_day_deposit_fixed_deposit_months;
		}

		public void setR12_91_day_deposit_fixed_deposit_months(BigDecimal r12_91_day_deposit_fixed_deposit_months) {
			this.r12_91_day_deposit_fixed_deposit_months = r12_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR12_1_2_fixed_deposits_months() {
			return r12_1_2_fixed_deposits_months;
		}

		public void setR12_1_2_fixed_deposits_months(BigDecimal r12_1_2_fixed_deposits_months) {
			this.r12_1_2_fixed_deposits_months = r12_1_2_fixed_deposits_months;
		}

		public BigDecimal getR12_4_6_fixed_deposits_months() {
			return r12_4_6_fixed_deposits_months;
		}

		public void setR12_4_6_fixed_deposits_months(BigDecimal r12_4_6_fixed_deposits_months) {
			this.r12_4_6_fixed_deposits_months = r12_4_6_fixed_deposits_months;
		}

		public BigDecimal getR12_7_12_fixed_deposits_months() {
			return r12_7_12_fixed_deposits_months;
		}

		public void setR12_7_12_fixed_deposits_months(BigDecimal r12_7_12_fixed_deposits_months) {
			this.r12_7_12_fixed_deposits_months = r12_7_12_fixed_deposits_months;
		}

		public BigDecimal getR12_13_18_fixed_deposits_months() {
			return r12_13_18_fixed_deposits_months;
		}

		public void setR12_13_18_fixed_deposits_months(BigDecimal r12_13_18_fixed_deposits_months) {
			this.r12_13_18_fixed_deposits_months = r12_13_18_fixed_deposits_months;
		}

		public BigDecimal getR12_19_24_fixed_deposits_months() {
			return r12_19_24_fixed_deposits_months;
		}

		public void setR12_19_24_fixed_deposits_months(BigDecimal r12_19_24_fixed_deposits_months) {
			this.r12_19_24_fixed_deposits_months = r12_19_24_fixed_deposits_months;
		}

		public BigDecimal getR12_over_24_fixed_deposits_months() {
			return r12_over_24_fixed_deposits_months;
		}

		public void setR12_over_24_fixed_deposits_months(BigDecimal r12_over_24_fixed_deposits_months) {
			this.r12_over_24_fixed_deposits_months = r12_over_24_fixed_deposits_months;
		}

		public BigDecimal getR12_certificates_of_deposit() {
			return r12_certificates_of_deposit;
		}

		public void setR12_certificates_of_deposit(BigDecimal r12_certificates_of_deposit) {
			this.r12_certificates_of_deposit = r12_certificates_of_deposit;
		}

		public BigDecimal getR12_total() {
			return r12_total;
		}

		public void setR12_total(BigDecimal r12_total) {
			this.r12_total = r12_total;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_current() {
			return r13_current;
		}

		public void setR13_current(BigDecimal r13_current) {
			this.r13_current = r13_current;
		}

		public BigDecimal getR13_call() {
			return r13_call;
		}

		public void setR13_call(BigDecimal r13_call) {
			this.r13_call = r13_call;
		}

		public BigDecimal getR13_savings() {
			return r13_savings;
		}

		public void setR13_savings(BigDecimal r13_savings) {
			this.r13_savings = r13_savings;
		}

		public BigDecimal getR13_0_31_notice_days() {
			return r13_0_31_notice_days;
		}

		public void setR13_0_31_notice_days(BigDecimal r13_0_31_notice_days) {
			this.r13_0_31_notice_days = r13_0_31_notice_days;
		}

		public BigDecimal getR13_32_88_notice_days() {
			return r13_32_88_notice_days;
		}

		public void setR13_32_88_notice_days(BigDecimal r13_32_88_notice_days) {
			this.r13_32_88_notice_days = r13_32_88_notice_days;
		}

		public BigDecimal getR13_91_day_deposit_fixed_deposit_months() {
			return r13_91_day_deposit_fixed_deposit_months;
		}

		public void setR13_91_day_deposit_fixed_deposit_months(BigDecimal r13_91_day_deposit_fixed_deposit_months) {
			this.r13_91_day_deposit_fixed_deposit_months = r13_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR13_1_2_fixed_deposits_months() {
			return r13_1_2_fixed_deposits_months;
		}

		public void setR13_1_2_fixed_deposits_months(BigDecimal r13_1_2_fixed_deposits_months) {
			this.r13_1_2_fixed_deposits_months = r13_1_2_fixed_deposits_months;
		}

		public BigDecimal getR13_4_6_fixed_deposits_months() {
			return r13_4_6_fixed_deposits_months;
		}

		public void setR13_4_6_fixed_deposits_months(BigDecimal r13_4_6_fixed_deposits_months) {
			this.r13_4_6_fixed_deposits_months = r13_4_6_fixed_deposits_months;
		}

		public BigDecimal getR13_7_12_fixed_deposits_months() {
			return r13_7_12_fixed_deposits_months;
		}

		public void setR13_7_12_fixed_deposits_months(BigDecimal r13_7_12_fixed_deposits_months) {
			this.r13_7_12_fixed_deposits_months = r13_7_12_fixed_deposits_months;
		}

		public BigDecimal getR13_13_18_fixed_deposits_months() {
			return r13_13_18_fixed_deposits_months;
		}

		public void setR13_13_18_fixed_deposits_months(BigDecimal r13_13_18_fixed_deposits_months) {
			this.r13_13_18_fixed_deposits_months = r13_13_18_fixed_deposits_months;
		}

		public BigDecimal getR13_19_24_fixed_deposits_months() {
			return r13_19_24_fixed_deposits_months;
		}

		public void setR13_19_24_fixed_deposits_months(BigDecimal r13_19_24_fixed_deposits_months) {
			this.r13_19_24_fixed_deposits_months = r13_19_24_fixed_deposits_months;
		}

		public BigDecimal getR13_over_24_fixed_deposits_months() {
			return r13_over_24_fixed_deposits_months;
		}

		public void setR13_over_24_fixed_deposits_months(BigDecimal r13_over_24_fixed_deposits_months) {
			this.r13_over_24_fixed_deposits_months = r13_over_24_fixed_deposits_months;
		}

		public BigDecimal getR13_certificates_of_deposit() {
			return r13_certificates_of_deposit;
		}

		public void setR13_certificates_of_deposit(BigDecimal r13_certificates_of_deposit) {
			this.r13_certificates_of_deposit = r13_certificates_of_deposit;
		}

		public BigDecimal getR13_total() {
			return r13_total;
		}

		public void setR13_total(BigDecimal r13_total) {
			this.r13_total = r13_total;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_current() {
			return r14_current;
		}

		public void setR14_current(BigDecimal r14_current) {
			this.r14_current = r14_current;
		}

		public BigDecimal getR14_call() {
			return r14_call;
		}

		public void setR14_call(BigDecimal r14_call) {
			this.r14_call = r14_call;
		}

		public BigDecimal getR14_savings() {
			return r14_savings;
		}

		public void setR14_savings(BigDecimal r14_savings) {
			this.r14_savings = r14_savings;
		}

		public BigDecimal getR14_0_31_notice_days() {
			return r14_0_31_notice_days;
		}

		public void setR14_0_31_notice_days(BigDecimal r14_0_31_notice_days) {
			this.r14_0_31_notice_days = r14_0_31_notice_days;
		}

		public BigDecimal getR14_32_88_notice_days() {
			return r14_32_88_notice_days;
		}

		public void setR14_32_88_notice_days(BigDecimal r14_32_88_notice_days) {
			this.r14_32_88_notice_days = r14_32_88_notice_days;
		}

		public BigDecimal getR14_91_day_deposit_fixed_deposit_months() {
			return r14_91_day_deposit_fixed_deposit_months;
		}

		public void setR14_91_day_deposit_fixed_deposit_months(BigDecimal r14_91_day_deposit_fixed_deposit_months) {
			this.r14_91_day_deposit_fixed_deposit_months = r14_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR14_1_2_fixed_deposits_months() {
			return r14_1_2_fixed_deposits_months;
		}

		public void setR14_1_2_fixed_deposits_months(BigDecimal r14_1_2_fixed_deposits_months) {
			this.r14_1_2_fixed_deposits_months = r14_1_2_fixed_deposits_months;
		}

		public BigDecimal getR14_4_6_fixed_deposits_months() {
			return r14_4_6_fixed_deposits_months;
		}

		public void setR14_4_6_fixed_deposits_months(BigDecimal r14_4_6_fixed_deposits_months) {
			this.r14_4_6_fixed_deposits_months = r14_4_6_fixed_deposits_months;
		}

		public BigDecimal getR14_7_12_fixed_deposits_months() {
			return r14_7_12_fixed_deposits_months;
		}

		public void setR14_7_12_fixed_deposits_months(BigDecimal r14_7_12_fixed_deposits_months) {
			this.r14_7_12_fixed_deposits_months = r14_7_12_fixed_deposits_months;
		}

		public BigDecimal getR14_13_18_fixed_deposits_months() {
			return r14_13_18_fixed_deposits_months;
		}

		public void setR14_13_18_fixed_deposits_months(BigDecimal r14_13_18_fixed_deposits_months) {
			this.r14_13_18_fixed_deposits_months = r14_13_18_fixed_deposits_months;
		}

		public BigDecimal getR14_19_24_fixed_deposits_months() {
			return r14_19_24_fixed_deposits_months;
		}

		public void setR14_19_24_fixed_deposits_months(BigDecimal r14_19_24_fixed_deposits_months) {
			this.r14_19_24_fixed_deposits_months = r14_19_24_fixed_deposits_months;
		}

		public BigDecimal getR14_over_24_fixed_deposits_months() {
			return r14_over_24_fixed_deposits_months;
		}

		public void setR14_over_24_fixed_deposits_months(BigDecimal r14_over_24_fixed_deposits_months) {
			this.r14_over_24_fixed_deposits_months = r14_over_24_fixed_deposits_months;
		}

		public BigDecimal getR14_certificates_of_deposit() {
			return r14_certificates_of_deposit;
		}

		public void setR14_certificates_of_deposit(BigDecimal r14_certificates_of_deposit) {
			this.r14_certificates_of_deposit = r14_certificates_of_deposit;
		}

		public BigDecimal getR14_total() {
			return r14_total;
		}

		public void setR14_total(BigDecimal r14_total) {
			this.r14_total = r14_total;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_current() {
			return r15_current;
		}

		public void setR15_current(BigDecimal r15_current) {
			this.r15_current = r15_current;
		}

		public BigDecimal getR15_call() {
			return r15_call;
		}

		public void setR15_call(BigDecimal r15_call) {
			this.r15_call = r15_call;
		}

		public BigDecimal getR15_savings() {
			return r15_savings;
		}

		public void setR15_savings(BigDecimal r15_savings) {
			this.r15_savings = r15_savings;
		}

		public BigDecimal getR15_0_31_notice_days() {
			return r15_0_31_notice_days;
		}

		public void setR15_0_31_notice_days(BigDecimal r15_0_31_notice_days) {
			this.r15_0_31_notice_days = r15_0_31_notice_days;
		}

		public BigDecimal getR15_32_88_notice_days() {
			return r15_32_88_notice_days;
		}

		public void setR15_32_88_notice_days(BigDecimal r15_32_88_notice_days) {
			this.r15_32_88_notice_days = r15_32_88_notice_days;
		}

		public BigDecimal getR15_91_day_deposit_fixed_deposit_months() {
			return r15_91_day_deposit_fixed_deposit_months;
		}

		public void setR15_91_day_deposit_fixed_deposit_months(BigDecimal r15_91_day_deposit_fixed_deposit_months) {
			this.r15_91_day_deposit_fixed_deposit_months = r15_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR15_1_2_fixed_deposits_months() {
			return r15_1_2_fixed_deposits_months;
		}

		public void setR15_1_2_fixed_deposits_months(BigDecimal r15_1_2_fixed_deposits_months) {
			this.r15_1_2_fixed_deposits_months = r15_1_2_fixed_deposits_months;
		}

		public BigDecimal getR15_4_6_fixed_deposits_months() {
			return r15_4_6_fixed_deposits_months;
		}

		public void setR15_4_6_fixed_deposits_months(BigDecimal r15_4_6_fixed_deposits_months) {
			this.r15_4_6_fixed_deposits_months = r15_4_6_fixed_deposits_months;
		}

		public BigDecimal getR15_7_12_fixed_deposits_months() {
			return r15_7_12_fixed_deposits_months;
		}

		public void setR15_7_12_fixed_deposits_months(BigDecimal r15_7_12_fixed_deposits_months) {
			this.r15_7_12_fixed_deposits_months = r15_7_12_fixed_deposits_months;
		}

		public BigDecimal getR15_13_18_fixed_deposits_months() {
			return r15_13_18_fixed_deposits_months;
		}

		public void setR15_13_18_fixed_deposits_months(BigDecimal r15_13_18_fixed_deposits_months) {
			this.r15_13_18_fixed_deposits_months = r15_13_18_fixed_deposits_months;
		}

		public BigDecimal getR15_19_24_fixed_deposits_months() {
			return r15_19_24_fixed_deposits_months;
		}

		public void setR15_19_24_fixed_deposits_months(BigDecimal r15_19_24_fixed_deposits_months) {
			this.r15_19_24_fixed_deposits_months = r15_19_24_fixed_deposits_months;
		}

		public BigDecimal getR15_over_24_fixed_deposits_months() {
			return r15_over_24_fixed_deposits_months;
		}

		public void setR15_over_24_fixed_deposits_months(BigDecimal r15_over_24_fixed_deposits_months) {
			this.r15_over_24_fixed_deposits_months = r15_over_24_fixed_deposits_months;
		}

		public BigDecimal getR15_certificates_of_deposit() {
			return r15_certificates_of_deposit;
		}

		public void setR15_certificates_of_deposit(BigDecimal r15_certificates_of_deposit) {
			this.r15_certificates_of_deposit = r15_certificates_of_deposit;
		}

		public BigDecimal getR15_total() {
			return r15_total;
		}

		public void setR15_total(BigDecimal r15_total) {
			this.r15_total = r15_total;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_current() {
			return r16_current;
		}

		public void setR16_current(BigDecimal r16_current) {
			this.r16_current = r16_current;
		}

		public BigDecimal getR16_call() {
			return r16_call;
		}

		public void setR16_call(BigDecimal r16_call) {
			this.r16_call = r16_call;
		}

		public BigDecimal getR16_savings() {
			return r16_savings;
		}

		public void setR16_savings(BigDecimal r16_savings) {
			this.r16_savings = r16_savings;
		}

		public BigDecimal getR16_0_31_notice_days() {
			return r16_0_31_notice_days;
		}

		public void setR16_0_31_notice_days(BigDecimal r16_0_31_notice_days) {
			this.r16_0_31_notice_days = r16_0_31_notice_days;
		}

		public BigDecimal getR16_32_88_notice_days() {
			return r16_32_88_notice_days;
		}

		public void setR16_32_88_notice_days(BigDecimal r16_32_88_notice_days) {
			this.r16_32_88_notice_days = r16_32_88_notice_days;
		}

		public BigDecimal getR16_91_day_deposit_fixed_deposit_months() {
			return r16_91_day_deposit_fixed_deposit_months;
		}

		public void setR16_91_day_deposit_fixed_deposit_months(BigDecimal r16_91_day_deposit_fixed_deposit_months) {
			this.r16_91_day_deposit_fixed_deposit_months = r16_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR16_1_2_fixed_deposits_months() {
			return r16_1_2_fixed_deposits_months;
		}

		public void setR16_1_2_fixed_deposits_months(BigDecimal r16_1_2_fixed_deposits_months) {
			this.r16_1_2_fixed_deposits_months = r16_1_2_fixed_deposits_months;
		}

		public BigDecimal getR16_4_6_fixed_deposits_months() {
			return r16_4_6_fixed_deposits_months;
		}

		public void setR16_4_6_fixed_deposits_months(BigDecimal r16_4_6_fixed_deposits_months) {
			this.r16_4_6_fixed_deposits_months = r16_4_6_fixed_deposits_months;
		}

		public BigDecimal getR16_7_12_fixed_deposits_months() {
			return r16_7_12_fixed_deposits_months;
		}

		public void setR16_7_12_fixed_deposits_months(BigDecimal r16_7_12_fixed_deposits_months) {
			this.r16_7_12_fixed_deposits_months = r16_7_12_fixed_deposits_months;
		}

		public BigDecimal getR16_13_18_fixed_deposits_months() {
			return r16_13_18_fixed_deposits_months;
		}

		public void setR16_13_18_fixed_deposits_months(BigDecimal r16_13_18_fixed_deposits_months) {
			this.r16_13_18_fixed_deposits_months = r16_13_18_fixed_deposits_months;
		}

		public BigDecimal getR16_19_24_fixed_deposits_months() {
			return r16_19_24_fixed_deposits_months;
		}

		public void setR16_19_24_fixed_deposits_months(BigDecimal r16_19_24_fixed_deposits_months) {
			this.r16_19_24_fixed_deposits_months = r16_19_24_fixed_deposits_months;
		}

		public BigDecimal getR16_over_24_fixed_deposits_months() {
			return r16_over_24_fixed_deposits_months;
		}

		public void setR16_over_24_fixed_deposits_months(BigDecimal r16_over_24_fixed_deposits_months) {
			this.r16_over_24_fixed_deposits_months = r16_over_24_fixed_deposits_months;
		}

		public BigDecimal getR16_certificates_of_deposit() {
			return r16_certificates_of_deposit;
		}

		public void setR16_certificates_of_deposit(BigDecimal r16_certificates_of_deposit) {
			this.r16_certificates_of_deposit = r16_certificates_of_deposit;
		}

		public BigDecimal getR16_total() {
			return r16_total;
		}

		public void setR16_total(BigDecimal r16_total) {
			this.r16_total = r16_total;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_current() {
			return r17_current;
		}

		public void setR17_current(BigDecimal r17_current) {
			this.r17_current = r17_current;
		}

		public BigDecimal getR17_call() {
			return r17_call;
		}

		public void setR17_call(BigDecimal r17_call) {
			this.r17_call = r17_call;
		}

		public BigDecimal getR17_savings() {
			return r17_savings;
		}

		public void setR17_savings(BigDecimal r17_savings) {
			this.r17_savings = r17_savings;
		}

		public BigDecimal getR17_0_31_notice_days() {
			return r17_0_31_notice_days;
		}

		public void setR17_0_31_notice_days(BigDecimal r17_0_31_notice_days) {
			this.r17_0_31_notice_days = r17_0_31_notice_days;
		}

		public BigDecimal getR17_32_88_notice_days() {
			return r17_32_88_notice_days;
		}

		public void setR17_32_88_notice_days(BigDecimal r17_32_88_notice_days) {
			this.r17_32_88_notice_days = r17_32_88_notice_days;
		}

		public BigDecimal getR17_91_day_deposit_fixed_deposit_months() {
			return r17_91_day_deposit_fixed_deposit_months;
		}

		public void setR17_91_day_deposit_fixed_deposit_months(BigDecimal r17_91_day_deposit_fixed_deposit_months) {
			this.r17_91_day_deposit_fixed_deposit_months = r17_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR17_1_2_fixed_deposits_months() {
			return r17_1_2_fixed_deposits_months;
		}

		public void setR17_1_2_fixed_deposits_months(BigDecimal r17_1_2_fixed_deposits_months) {
			this.r17_1_2_fixed_deposits_months = r17_1_2_fixed_deposits_months;
		}

		public BigDecimal getR17_4_6_fixed_deposits_months() {
			return r17_4_6_fixed_deposits_months;
		}

		public void setR17_4_6_fixed_deposits_months(BigDecimal r17_4_6_fixed_deposits_months) {
			this.r17_4_6_fixed_deposits_months = r17_4_6_fixed_deposits_months;
		}

		public BigDecimal getR17_7_12_fixed_deposits_months() {
			return r17_7_12_fixed_deposits_months;
		}

		public void setR17_7_12_fixed_deposits_months(BigDecimal r17_7_12_fixed_deposits_months) {
			this.r17_7_12_fixed_deposits_months = r17_7_12_fixed_deposits_months;
		}

		public BigDecimal getR17_13_18_fixed_deposits_months() {
			return r17_13_18_fixed_deposits_months;
		}

		public void setR17_13_18_fixed_deposits_months(BigDecimal r17_13_18_fixed_deposits_months) {
			this.r17_13_18_fixed_deposits_months = r17_13_18_fixed_deposits_months;
		}

		public BigDecimal getR17_19_24_fixed_deposits_months() {
			return r17_19_24_fixed_deposits_months;
		}

		public void setR17_19_24_fixed_deposits_months(BigDecimal r17_19_24_fixed_deposits_months) {
			this.r17_19_24_fixed_deposits_months = r17_19_24_fixed_deposits_months;
		}

		public BigDecimal getR17_over_24_fixed_deposits_months() {
			return r17_over_24_fixed_deposits_months;
		}

		public void setR17_over_24_fixed_deposits_months(BigDecimal r17_over_24_fixed_deposits_months) {
			this.r17_over_24_fixed_deposits_months = r17_over_24_fixed_deposits_months;
		}

		public BigDecimal getR17_certificates_of_deposit() {
			return r17_certificates_of_deposit;
		}

		public void setR17_certificates_of_deposit(BigDecimal r17_certificates_of_deposit) {
			this.r17_certificates_of_deposit = r17_certificates_of_deposit;
		}

		public BigDecimal getR17_total() {
			return r17_total;
		}

		public void setR17_total(BigDecimal r17_total) {
			this.r17_total = r17_total;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_current() {
			return r18_current;
		}

		public void setR18_current(BigDecimal r18_current) {
			this.r18_current = r18_current;
		}

		public BigDecimal getR18_call() {
			return r18_call;
		}

		public void setR18_call(BigDecimal r18_call) {
			this.r18_call = r18_call;
		}

		public BigDecimal getR18_savings() {
			return r18_savings;
		}

		public void setR18_savings(BigDecimal r18_savings) {
			this.r18_savings = r18_savings;
		}

		public BigDecimal getR18_0_31_notice_days() {
			return r18_0_31_notice_days;
		}

		public void setR18_0_31_notice_days(BigDecimal r18_0_31_notice_days) {
			this.r18_0_31_notice_days = r18_0_31_notice_days;
		}

		public BigDecimal getR18_32_88_notice_days() {
			return r18_32_88_notice_days;
		}

		public void setR18_32_88_notice_days(BigDecimal r18_32_88_notice_days) {
			this.r18_32_88_notice_days = r18_32_88_notice_days;
		}

		public BigDecimal getR18_91_day_deposit_fixed_deposit_months() {
			return r18_91_day_deposit_fixed_deposit_months;
		}

		public void setR18_91_day_deposit_fixed_deposit_months(BigDecimal r18_91_day_deposit_fixed_deposit_months) {
			this.r18_91_day_deposit_fixed_deposit_months = r18_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR18_1_2_fixed_deposits_months() {
			return r18_1_2_fixed_deposits_months;
		}

		public void setR18_1_2_fixed_deposits_months(BigDecimal r18_1_2_fixed_deposits_months) {
			this.r18_1_2_fixed_deposits_months = r18_1_2_fixed_deposits_months;
		}

		public BigDecimal getR18_4_6_fixed_deposits_months() {
			return r18_4_6_fixed_deposits_months;
		}

		public void setR18_4_6_fixed_deposits_months(BigDecimal r18_4_6_fixed_deposits_months) {
			this.r18_4_6_fixed_deposits_months = r18_4_6_fixed_deposits_months;
		}

		public BigDecimal getR18_7_12_fixed_deposits_months() {
			return r18_7_12_fixed_deposits_months;
		}

		public void setR18_7_12_fixed_deposits_months(BigDecimal r18_7_12_fixed_deposits_months) {
			this.r18_7_12_fixed_deposits_months = r18_7_12_fixed_deposits_months;
		}

		public BigDecimal getR18_13_18_fixed_deposits_months() {
			return r18_13_18_fixed_deposits_months;
		}

		public void setR18_13_18_fixed_deposits_months(BigDecimal r18_13_18_fixed_deposits_months) {
			this.r18_13_18_fixed_deposits_months = r18_13_18_fixed_deposits_months;
		}

		public BigDecimal getR18_19_24_fixed_deposits_months() {
			return r18_19_24_fixed_deposits_months;
		}

		public void setR18_19_24_fixed_deposits_months(BigDecimal r18_19_24_fixed_deposits_months) {
			this.r18_19_24_fixed_deposits_months = r18_19_24_fixed_deposits_months;
		}

		public BigDecimal getR18_over_24_fixed_deposits_months() {
			return r18_over_24_fixed_deposits_months;
		}

		public void setR18_over_24_fixed_deposits_months(BigDecimal r18_over_24_fixed_deposits_months) {
			this.r18_over_24_fixed_deposits_months = r18_over_24_fixed_deposits_months;
		}

		public BigDecimal getR18_certificates_of_deposit() {
			return r18_certificates_of_deposit;
		}

		public void setR18_certificates_of_deposit(BigDecimal r18_certificates_of_deposit) {
			this.r18_certificates_of_deposit = r18_certificates_of_deposit;
		}

		public BigDecimal getR18_total() {
			return r18_total;
		}

		public void setR18_total(BigDecimal r18_total) {
			this.r18_total = r18_total;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_current() {
			return r19_current;
		}

		public void setR19_current(BigDecimal r19_current) {
			this.r19_current = r19_current;
		}

		public BigDecimal getR19_call() {
			return r19_call;
		}

		public void setR19_call(BigDecimal r19_call) {
			this.r19_call = r19_call;
		}

		public BigDecimal getR19_savings() {
			return r19_savings;
		}

		public void setR19_savings(BigDecimal r19_savings) {
			this.r19_savings = r19_savings;
		}

		public BigDecimal getR19_0_31_notice_days() {
			return r19_0_31_notice_days;
		}

		public void setR19_0_31_notice_days(BigDecimal r19_0_31_notice_days) {
			this.r19_0_31_notice_days = r19_0_31_notice_days;
		}

		public BigDecimal getR19_32_88_notice_days() {
			return r19_32_88_notice_days;
		}

		public void setR19_32_88_notice_days(BigDecimal r19_32_88_notice_days) {
			this.r19_32_88_notice_days = r19_32_88_notice_days;
		}

		public BigDecimal getR19_91_day_deposit_fixed_deposit_months() {
			return r19_91_day_deposit_fixed_deposit_months;
		}

		public void setR19_91_day_deposit_fixed_deposit_months(BigDecimal r19_91_day_deposit_fixed_deposit_months) {
			this.r19_91_day_deposit_fixed_deposit_months = r19_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR19_1_2_fixed_deposits_months() {
			return r19_1_2_fixed_deposits_months;
		}

		public void setR19_1_2_fixed_deposits_months(BigDecimal r19_1_2_fixed_deposits_months) {
			this.r19_1_2_fixed_deposits_months = r19_1_2_fixed_deposits_months;
		}

		public BigDecimal getR19_4_6_fixed_deposits_months() {
			return r19_4_6_fixed_deposits_months;
		}

		public void setR19_4_6_fixed_deposits_months(BigDecimal r19_4_6_fixed_deposits_months) {
			this.r19_4_6_fixed_deposits_months = r19_4_6_fixed_deposits_months;
		}

		public BigDecimal getR19_7_12_fixed_deposits_months() {
			return r19_7_12_fixed_deposits_months;
		}

		public void setR19_7_12_fixed_deposits_months(BigDecimal r19_7_12_fixed_deposits_months) {
			this.r19_7_12_fixed_deposits_months = r19_7_12_fixed_deposits_months;
		}

		public BigDecimal getR19_13_18_fixed_deposits_months() {
			return r19_13_18_fixed_deposits_months;
		}

		public void setR19_13_18_fixed_deposits_months(BigDecimal r19_13_18_fixed_deposits_months) {
			this.r19_13_18_fixed_deposits_months = r19_13_18_fixed_deposits_months;
		}

		public BigDecimal getR19_19_24_fixed_deposits_months() {
			return r19_19_24_fixed_deposits_months;
		}

		public void setR19_19_24_fixed_deposits_months(BigDecimal r19_19_24_fixed_deposits_months) {
			this.r19_19_24_fixed_deposits_months = r19_19_24_fixed_deposits_months;
		}

		public BigDecimal getR19_over_24_fixed_deposits_months() {
			return r19_over_24_fixed_deposits_months;
		}

		public void setR19_over_24_fixed_deposits_months(BigDecimal r19_over_24_fixed_deposits_months) {
			this.r19_over_24_fixed_deposits_months = r19_over_24_fixed_deposits_months;
		}

		public BigDecimal getR19_certificates_of_deposit() {
			return r19_certificates_of_deposit;
		}

		public void setR19_certificates_of_deposit(BigDecimal r19_certificates_of_deposit) {
			this.r19_certificates_of_deposit = r19_certificates_of_deposit;
		}

		public BigDecimal getR19_total() {
			return r19_total;
		}

		public void setR19_total(BigDecimal r19_total) {
			this.r19_total = r19_total;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_current() {
			return r20_current;
		}

		public void setR20_current(BigDecimal r20_current) {
			this.r20_current = r20_current;
		}

		public BigDecimal getR20_call() {
			return r20_call;
		}

		public void setR20_call(BigDecimal r20_call) {
			this.r20_call = r20_call;
		}

		public BigDecimal getR20_savings() {
			return r20_savings;
		}

		public void setR20_savings(BigDecimal r20_savings) {
			this.r20_savings = r20_savings;
		}

		public BigDecimal getR20_0_31_notice_days() {
			return r20_0_31_notice_days;
		}

		public void setR20_0_31_notice_days(BigDecimal r20_0_31_notice_days) {
			this.r20_0_31_notice_days = r20_0_31_notice_days;
		}

		public BigDecimal getR20_32_88_notice_days() {
			return r20_32_88_notice_days;
		}

		public void setR20_32_88_notice_days(BigDecimal r20_32_88_notice_days) {
			this.r20_32_88_notice_days = r20_32_88_notice_days;
		}

		public BigDecimal getR20_91_day_deposit_fixed_deposit_months() {
			return r20_91_day_deposit_fixed_deposit_months;
		}

		public void setR20_91_day_deposit_fixed_deposit_months(BigDecimal r20_91_day_deposit_fixed_deposit_months) {
			this.r20_91_day_deposit_fixed_deposit_months = r20_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR20_1_2_fixed_deposits_months() {
			return r20_1_2_fixed_deposits_months;
		}

		public void setR20_1_2_fixed_deposits_months(BigDecimal r20_1_2_fixed_deposits_months) {
			this.r20_1_2_fixed_deposits_months = r20_1_2_fixed_deposits_months;
		}

		public BigDecimal getR20_4_6_fixed_deposits_months() {
			return r20_4_6_fixed_deposits_months;
		}

		public void setR20_4_6_fixed_deposits_months(BigDecimal r20_4_6_fixed_deposits_months) {
			this.r20_4_6_fixed_deposits_months = r20_4_6_fixed_deposits_months;
		}

		public BigDecimal getR20_7_12_fixed_deposits_months() {
			return r20_7_12_fixed_deposits_months;
		}

		public void setR20_7_12_fixed_deposits_months(BigDecimal r20_7_12_fixed_deposits_months) {
			this.r20_7_12_fixed_deposits_months = r20_7_12_fixed_deposits_months;
		}

		public BigDecimal getR20_13_18_fixed_deposits_months() {
			return r20_13_18_fixed_deposits_months;
		}

		public void setR20_13_18_fixed_deposits_months(BigDecimal r20_13_18_fixed_deposits_months) {
			this.r20_13_18_fixed_deposits_months = r20_13_18_fixed_deposits_months;
		}

		public BigDecimal getR20_19_24_fixed_deposits_months() {
			return r20_19_24_fixed_deposits_months;
		}

		public void setR20_19_24_fixed_deposits_months(BigDecimal r20_19_24_fixed_deposits_months) {
			this.r20_19_24_fixed_deposits_months = r20_19_24_fixed_deposits_months;
		}

		public BigDecimal getR20_over_24_fixed_deposits_months() {
			return r20_over_24_fixed_deposits_months;
		}

		public void setR20_over_24_fixed_deposits_months(BigDecimal r20_over_24_fixed_deposits_months) {
			this.r20_over_24_fixed_deposits_months = r20_over_24_fixed_deposits_months;
		}

		public BigDecimal getR20_certificates_of_deposit() {
			return r20_certificates_of_deposit;
		}

		public void setR20_certificates_of_deposit(BigDecimal r20_certificates_of_deposit) {
			this.r20_certificates_of_deposit = r20_certificates_of_deposit;
		}

		public BigDecimal getR20_total() {
			return r20_total;
		}

		public void setR20_total(BigDecimal r20_total) {
			this.r20_total = r20_total;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_current() {
			return r21_current;
		}

		public void setR21_current(BigDecimal r21_current) {
			this.r21_current = r21_current;
		}

		public BigDecimal getR21_call() {
			return r21_call;
		}

		public void setR21_call(BigDecimal r21_call) {
			this.r21_call = r21_call;
		}

		public BigDecimal getR21_savings() {
			return r21_savings;
		}

		public void setR21_savings(BigDecimal r21_savings) {
			this.r21_savings = r21_savings;
		}

		public BigDecimal getR21_0_31_notice_days() {
			return r21_0_31_notice_days;
		}

		public void setR21_0_31_notice_days(BigDecimal r21_0_31_notice_days) {
			this.r21_0_31_notice_days = r21_0_31_notice_days;
		}

		public BigDecimal getR21_32_88_notice_days() {
			return r21_32_88_notice_days;
		}

		public void setR21_32_88_notice_days(BigDecimal r21_32_88_notice_days) {
			this.r21_32_88_notice_days = r21_32_88_notice_days;
		}

		public BigDecimal getR21_91_day_deposit_fixed_deposit_months() {
			return r21_91_day_deposit_fixed_deposit_months;
		}

		public void setR21_91_day_deposit_fixed_deposit_months(BigDecimal r21_91_day_deposit_fixed_deposit_months) {
			this.r21_91_day_deposit_fixed_deposit_months = r21_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR21_1_2_fixed_deposits_months() {
			return r21_1_2_fixed_deposits_months;
		}

		public void setR21_1_2_fixed_deposits_months(BigDecimal r21_1_2_fixed_deposits_months) {
			this.r21_1_2_fixed_deposits_months = r21_1_2_fixed_deposits_months;
		}

		public BigDecimal getR21_4_6_fixed_deposits_months() {
			return r21_4_6_fixed_deposits_months;
		}

		public void setR21_4_6_fixed_deposits_months(BigDecimal r21_4_6_fixed_deposits_months) {
			this.r21_4_6_fixed_deposits_months = r21_4_6_fixed_deposits_months;
		}

		public BigDecimal getR21_7_12_fixed_deposits_months() {
			return r21_7_12_fixed_deposits_months;
		}

		public void setR21_7_12_fixed_deposits_months(BigDecimal r21_7_12_fixed_deposits_months) {
			this.r21_7_12_fixed_deposits_months = r21_7_12_fixed_deposits_months;
		}

		public BigDecimal getR21_13_18_fixed_deposits_months() {
			return r21_13_18_fixed_deposits_months;
		}

		public void setR21_13_18_fixed_deposits_months(BigDecimal r21_13_18_fixed_deposits_months) {
			this.r21_13_18_fixed_deposits_months = r21_13_18_fixed_deposits_months;
		}

		public BigDecimal getR21_19_24_fixed_deposits_months() {
			return r21_19_24_fixed_deposits_months;
		}

		public void setR21_19_24_fixed_deposits_months(BigDecimal r21_19_24_fixed_deposits_months) {
			this.r21_19_24_fixed_deposits_months = r21_19_24_fixed_deposits_months;
		}

		public BigDecimal getR21_over_24_fixed_deposits_months() {
			return r21_over_24_fixed_deposits_months;
		}

		public void setR21_over_24_fixed_deposits_months(BigDecimal r21_over_24_fixed_deposits_months) {
			this.r21_over_24_fixed_deposits_months = r21_over_24_fixed_deposits_months;
		}

		public BigDecimal getR21_certificates_of_deposit() {
			return r21_certificates_of_deposit;
		}

		public void setR21_certificates_of_deposit(BigDecimal r21_certificates_of_deposit) {
			this.r21_certificates_of_deposit = r21_certificates_of_deposit;
		}

		public BigDecimal getR21_total() {
			return r21_total;
		}

		public void setR21_total(BigDecimal r21_total) {
			this.r21_total = r21_total;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_current() {
			return r22_current;
		}

		public void setR22_current(BigDecimal r22_current) {
			this.r22_current = r22_current;
		}

		public BigDecimal getR22_call() {
			return r22_call;
		}

		public void setR22_call(BigDecimal r22_call) {
			this.r22_call = r22_call;
		}

		public BigDecimal getR22_savings() {
			return r22_savings;
		}

		public void setR22_savings(BigDecimal r22_savings) {
			this.r22_savings = r22_savings;
		}

		public BigDecimal getR22_0_31_notice_days() {
			return r22_0_31_notice_days;
		}

		public void setR22_0_31_notice_days(BigDecimal r22_0_31_notice_days) {
			this.r22_0_31_notice_days = r22_0_31_notice_days;
		}

		public BigDecimal getR22_32_88_notice_days() {
			return r22_32_88_notice_days;
		}

		public void setR22_32_88_notice_days(BigDecimal r22_32_88_notice_days) {
			this.r22_32_88_notice_days = r22_32_88_notice_days;
		}

		public BigDecimal getR22_91_day_deposit_fixed_deposit_months() {
			return r22_91_day_deposit_fixed_deposit_months;
		}

		public void setR22_91_day_deposit_fixed_deposit_months(BigDecimal r22_91_day_deposit_fixed_deposit_months) {
			this.r22_91_day_deposit_fixed_deposit_months = r22_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR22_1_2_fixed_deposits_months() {
			return r22_1_2_fixed_deposits_months;
		}

		public void setR22_1_2_fixed_deposits_months(BigDecimal r22_1_2_fixed_deposits_months) {
			this.r22_1_2_fixed_deposits_months = r22_1_2_fixed_deposits_months;
		}

		public BigDecimal getR22_4_6_fixed_deposits_months() {
			return r22_4_6_fixed_deposits_months;
		}

		public void setR22_4_6_fixed_deposits_months(BigDecimal r22_4_6_fixed_deposits_months) {
			this.r22_4_6_fixed_deposits_months = r22_4_6_fixed_deposits_months;
		}

		public BigDecimal getR22_7_12_fixed_deposits_months() {
			return r22_7_12_fixed_deposits_months;
		}

		public void setR22_7_12_fixed_deposits_months(BigDecimal r22_7_12_fixed_deposits_months) {
			this.r22_7_12_fixed_deposits_months = r22_7_12_fixed_deposits_months;
		}

		public BigDecimal getR22_13_18_fixed_deposits_months() {
			return r22_13_18_fixed_deposits_months;
		}

		public void setR22_13_18_fixed_deposits_months(BigDecimal r22_13_18_fixed_deposits_months) {
			this.r22_13_18_fixed_deposits_months = r22_13_18_fixed_deposits_months;
		}

		public BigDecimal getR22_19_24_fixed_deposits_months() {
			return r22_19_24_fixed_deposits_months;
		}

		public void setR22_19_24_fixed_deposits_months(BigDecimal r22_19_24_fixed_deposits_months) {
			this.r22_19_24_fixed_deposits_months = r22_19_24_fixed_deposits_months;
		}

		public BigDecimal getR22_over_24_fixed_deposits_months() {
			return r22_over_24_fixed_deposits_months;
		}

		public void setR22_over_24_fixed_deposits_months(BigDecimal r22_over_24_fixed_deposits_months) {
			this.r22_over_24_fixed_deposits_months = r22_over_24_fixed_deposits_months;
		}

		public BigDecimal getR22_certificates_of_deposit() {
			return r22_certificates_of_deposit;
		}

		public void setR22_certificates_of_deposit(BigDecimal r22_certificates_of_deposit) {
			this.r22_certificates_of_deposit = r22_certificates_of_deposit;
		}

		public BigDecimal getR22_total() {
			return r22_total;
		}

		public void setR22_total(BigDecimal r22_total) {
			this.r22_total = r22_total;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_current() {
			return r23_current;
		}

		public void setR23_current(BigDecimal r23_current) {
			this.r23_current = r23_current;
		}

		public BigDecimal getR23_call() {
			return r23_call;
		}

		public void setR23_call(BigDecimal r23_call) {
			this.r23_call = r23_call;
		}

		public BigDecimal getR23_savings() {
			return r23_savings;
		}

		public void setR23_savings(BigDecimal r23_savings) {
			this.r23_savings = r23_savings;
		}

		public BigDecimal getR23_0_31_notice_days() {
			return r23_0_31_notice_days;
		}

		public void setR23_0_31_notice_days(BigDecimal r23_0_31_notice_days) {
			this.r23_0_31_notice_days = r23_0_31_notice_days;
		}

		public BigDecimal getR23_32_88_notice_days() {
			return r23_32_88_notice_days;
		}

		public void setR23_32_88_notice_days(BigDecimal r23_32_88_notice_days) {
			this.r23_32_88_notice_days = r23_32_88_notice_days;
		}

		public BigDecimal getR23_91_day_deposit_fixed_deposit_months() {
			return r23_91_day_deposit_fixed_deposit_months;
		}

		public void setR23_91_day_deposit_fixed_deposit_months(BigDecimal r23_91_day_deposit_fixed_deposit_months) {
			this.r23_91_day_deposit_fixed_deposit_months = r23_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR23_1_2_fixed_deposits_months() {
			return r23_1_2_fixed_deposits_months;
		}

		public void setR23_1_2_fixed_deposits_months(BigDecimal r23_1_2_fixed_deposits_months) {
			this.r23_1_2_fixed_deposits_months = r23_1_2_fixed_deposits_months;
		}

		public BigDecimal getR23_4_6_fixed_deposits_months() {
			return r23_4_6_fixed_deposits_months;
		}

		public void setR23_4_6_fixed_deposits_months(BigDecimal r23_4_6_fixed_deposits_months) {
			this.r23_4_6_fixed_deposits_months = r23_4_6_fixed_deposits_months;
		}

		public BigDecimal getR23_7_12_fixed_deposits_months() {
			return r23_7_12_fixed_deposits_months;
		}

		public void setR23_7_12_fixed_deposits_months(BigDecimal r23_7_12_fixed_deposits_months) {
			this.r23_7_12_fixed_deposits_months = r23_7_12_fixed_deposits_months;
		}

		public BigDecimal getR23_13_18_fixed_deposits_months() {
			return r23_13_18_fixed_deposits_months;
		}

		public void setR23_13_18_fixed_deposits_months(BigDecimal r23_13_18_fixed_deposits_months) {
			this.r23_13_18_fixed_deposits_months = r23_13_18_fixed_deposits_months;
		}

		public BigDecimal getR23_19_24_fixed_deposits_months() {
			return r23_19_24_fixed_deposits_months;
		}

		public void setR23_19_24_fixed_deposits_months(BigDecimal r23_19_24_fixed_deposits_months) {
			this.r23_19_24_fixed_deposits_months = r23_19_24_fixed_deposits_months;
		}

		public BigDecimal getR23_over_24_fixed_deposits_months() {
			return r23_over_24_fixed_deposits_months;
		}

		public void setR23_over_24_fixed_deposits_months(BigDecimal r23_over_24_fixed_deposits_months) {
			this.r23_over_24_fixed_deposits_months = r23_over_24_fixed_deposits_months;
		}

		public BigDecimal getR23_certificates_of_deposit() {
			return r23_certificates_of_deposit;
		}

		public void setR23_certificates_of_deposit(BigDecimal r23_certificates_of_deposit) {
			this.r23_certificates_of_deposit = r23_certificates_of_deposit;
		}

		public BigDecimal getR23_total() {
			return r23_total;
		}

		public void setR23_total(BigDecimal r23_total) {
			this.r23_total = r23_total;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_current() {
			return r24_current;
		}

		public void setR24_current(BigDecimal r24_current) {
			this.r24_current = r24_current;
		}

		public BigDecimal getR24_call() {
			return r24_call;
		}

		public void setR24_call(BigDecimal r24_call) {
			this.r24_call = r24_call;
		}

		public BigDecimal getR24_savings() {
			return r24_savings;
		}

		public void setR24_savings(BigDecimal r24_savings) {
			this.r24_savings = r24_savings;
		}

		public BigDecimal getR24_0_31_notice_days() {
			return r24_0_31_notice_days;
		}

		public void setR24_0_31_notice_days(BigDecimal r24_0_31_notice_days) {
			this.r24_0_31_notice_days = r24_0_31_notice_days;
		}

		public BigDecimal getR24_32_88_notice_days() {
			return r24_32_88_notice_days;
		}

		public void setR24_32_88_notice_days(BigDecimal r24_32_88_notice_days) {
			this.r24_32_88_notice_days = r24_32_88_notice_days;
		}

		public BigDecimal getR24_91_day_deposit_fixed_deposit_months() {
			return r24_91_day_deposit_fixed_deposit_months;
		}

		public void setR24_91_day_deposit_fixed_deposit_months(BigDecimal r24_91_day_deposit_fixed_deposit_months) {
			this.r24_91_day_deposit_fixed_deposit_months = r24_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR24_1_2_fixed_deposits_months() {
			return r24_1_2_fixed_deposits_months;
		}

		public void setR24_1_2_fixed_deposits_months(BigDecimal r24_1_2_fixed_deposits_months) {
			this.r24_1_2_fixed_deposits_months = r24_1_2_fixed_deposits_months;
		}

		public BigDecimal getR24_4_6_fixed_deposits_months() {
			return r24_4_6_fixed_deposits_months;
		}

		public void setR24_4_6_fixed_deposits_months(BigDecimal r24_4_6_fixed_deposits_months) {
			this.r24_4_6_fixed_deposits_months = r24_4_6_fixed_deposits_months;
		}

		public BigDecimal getR24_7_12_fixed_deposits_months() {
			return r24_7_12_fixed_deposits_months;
		}

		public void setR24_7_12_fixed_deposits_months(BigDecimal r24_7_12_fixed_deposits_months) {
			this.r24_7_12_fixed_deposits_months = r24_7_12_fixed_deposits_months;
		}

		public BigDecimal getR24_13_18_fixed_deposits_months() {
			return r24_13_18_fixed_deposits_months;
		}

		public void setR24_13_18_fixed_deposits_months(BigDecimal r24_13_18_fixed_deposits_months) {
			this.r24_13_18_fixed_deposits_months = r24_13_18_fixed_deposits_months;
		}

		public BigDecimal getR24_19_24_fixed_deposits_months() {
			return r24_19_24_fixed_deposits_months;
		}

		public void setR24_19_24_fixed_deposits_months(BigDecimal r24_19_24_fixed_deposits_months) {
			this.r24_19_24_fixed_deposits_months = r24_19_24_fixed_deposits_months;
		}

		public BigDecimal getR24_over_24_fixed_deposits_months() {
			return r24_over_24_fixed_deposits_months;
		}

		public void setR24_over_24_fixed_deposits_months(BigDecimal r24_over_24_fixed_deposits_months) {
			this.r24_over_24_fixed_deposits_months = r24_over_24_fixed_deposits_months;
		}

		public BigDecimal getR24_certificates_of_deposit() {
			return r24_certificates_of_deposit;
		}

		public void setR24_certificates_of_deposit(BigDecimal r24_certificates_of_deposit) {
			this.r24_certificates_of_deposit = r24_certificates_of_deposit;
		}

		public BigDecimal getR24_total() {
			return r24_total;
		}

		public void setR24_total(BigDecimal r24_total) {
			this.r24_total = r24_total;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_current() {
			return r25_current;
		}

		public void setR25_current(BigDecimal r25_current) {
			this.r25_current = r25_current;
		}

		public BigDecimal getR25_call() {
			return r25_call;
		}

		public void setR25_call(BigDecimal r25_call) {
			this.r25_call = r25_call;
		}

		public BigDecimal getR25_savings() {
			return r25_savings;
		}

		public void setR25_savings(BigDecimal r25_savings) {
			this.r25_savings = r25_savings;
		}

		public BigDecimal getR25_0_31_notice_days() {
			return r25_0_31_notice_days;
		}

		public void setR25_0_31_notice_days(BigDecimal r25_0_31_notice_days) {
			this.r25_0_31_notice_days = r25_0_31_notice_days;
		}

		public BigDecimal getR25_32_88_notice_days() {
			return r25_32_88_notice_days;
		}

		public void setR25_32_88_notice_days(BigDecimal r25_32_88_notice_days) {
			this.r25_32_88_notice_days = r25_32_88_notice_days;
		}

		public BigDecimal getR25_91_day_deposit_fixed_deposit_months() {
			return r25_91_day_deposit_fixed_deposit_months;
		}

		public void setR25_91_day_deposit_fixed_deposit_months(BigDecimal r25_91_day_deposit_fixed_deposit_months) {
			this.r25_91_day_deposit_fixed_deposit_months = r25_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR25_1_2_fixed_deposits_months() {
			return r25_1_2_fixed_deposits_months;
		}

		public void setR25_1_2_fixed_deposits_months(BigDecimal r25_1_2_fixed_deposits_months) {
			this.r25_1_2_fixed_deposits_months = r25_1_2_fixed_deposits_months;
		}

		public BigDecimal getR25_4_6_fixed_deposits_months() {
			return r25_4_6_fixed_deposits_months;
		}

		public void setR25_4_6_fixed_deposits_months(BigDecimal r25_4_6_fixed_deposits_months) {
			this.r25_4_6_fixed_deposits_months = r25_4_6_fixed_deposits_months;
		}

		public BigDecimal getR25_7_12_fixed_deposits_months() {
			return r25_7_12_fixed_deposits_months;
		}

		public void setR25_7_12_fixed_deposits_months(BigDecimal r25_7_12_fixed_deposits_months) {
			this.r25_7_12_fixed_deposits_months = r25_7_12_fixed_deposits_months;
		}

		public BigDecimal getR25_13_18_fixed_deposits_months() {
			return r25_13_18_fixed_deposits_months;
		}

		public void setR25_13_18_fixed_deposits_months(BigDecimal r25_13_18_fixed_deposits_months) {
			this.r25_13_18_fixed_deposits_months = r25_13_18_fixed_deposits_months;
		}

		public BigDecimal getR25_19_24_fixed_deposits_months() {
			return r25_19_24_fixed_deposits_months;
		}

		public void setR25_19_24_fixed_deposits_months(BigDecimal r25_19_24_fixed_deposits_months) {
			this.r25_19_24_fixed_deposits_months = r25_19_24_fixed_deposits_months;
		}

		public BigDecimal getR25_over_24_fixed_deposits_months() {
			return r25_over_24_fixed_deposits_months;
		}

		public void setR25_over_24_fixed_deposits_months(BigDecimal r25_over_24_fixed_deposits_months) {
			this.r25_over_24_fixed_deposits_months = r25_over_24_fixed_deposits_months;
		}

		public BigDecimal getR25_certificates_of_deposit() {
			return r25_certificates_of_deposit;
		}

		public void setR25_certificates_of_deposit(BigDecimal r25_certificates_of_deposit) {
			this.r25_certificates_of_deposit = r25_certificates_of_deposit;
		}

		public BigDecimal getR25_total() {
			return r25_total;
		}

		public void setR25_total(BigDecimal r25_total) {
			this.r25_total = r25_total;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_current() {
			return r26_current;
		}

		public void setR26_current(BigDecimal r26_current) {
			this.r26_current = r26_current;
		}

		public BigDecimal getR26_call() {
			return r26_call;
		}

		public void setR26_call(BigDecimal r26_call) {
			this.r26_call = r26_call;
		}

		public BigDecimal getR26_savings() {
			return r26_savings;
		}

		public void setR26_savings(BigDecimal r26_savings) {
			this.r26_savings = r26_savings;
		}

		public BigDecimal getR26_0_31_notice_days() {
			return r26_0_31_notice_days;
		}

		public void setR26_0_31_notice_days(BigDecimal r26_0_31_notice_days) {
			this.r26_0_31_notice_days = r26_0_31_notice_days;
		}

		public BigDecimal getR26_32_88_notice_days() {
			return r26_32_88_notice_days;
		}

		public void setR26_32_88_notice_days(BigDecimal r26_32_88_notice_days) {
			this.r26_32_88_notice_days = r26_32_88_notice_days;
		}

		public BigDecimal getR26_91_day_deposit_fixed_deposit_months() {
			return r26_91_day_deposit_fixed_deposit_months;
		}

		public void setR26_91_day_deposit_fixed_deposit_months(BigDecimal r26_91_day_deposit_fixed_deposit_months) {
			this.r26_91_day_deposit_fixed_deposit_months = r26_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR26_1_2_fixed_deposits_months() {
			return r26_1_2_fixed_deposits_months;
		}

		public void setR26_1_2_fixed_deposits_months(BigDecimal r26_1_2_fixed_deposits_months) {
			this.r26_1_2_fixed_deposits_months = r26_1_2_fixed_deposits_months;
		}

		public BigDecimal getR26_4_6_fixed_deposits_months() {
			return r26_4_6_fixed_deposits_months;
		}

		public void setR26_4_6_fixed_deposits_months(BigDecimal r26_4_6_fixed_deposits_months) {
			this.r26_4_6_fixed_deposits_months = r26_4_6_fixed_deposits_months;
		}

		public BigDecimal getR26_7_12_fixed_deposits_months() {
			return r26_7_12_fixed_deposits_months;
		}

		public void setR26_7_12_fixed_deposits_months(BigDecimal r26_7_12_fixed_deposits_months) {
			this.r26_7_12_fixed_deposits_months = r26_7_12_fixed_deposits_months;
		}

		public BigDecimal getR26_13_18_fixed_deposits_months() {
			return r26_13_18_fixed_deposits_months;
		}

		public void setR26_13_18_fixed_deposits_months(BigDecimal r26_13_18_fixed_deposits_months) {
			this.r26_13_18_fixed_deposits_months = r26_13_18_fixed_deposits_months;
		}

		public BigDecimal getR26_19_24_fixed_deposits_months() {
			return r26_19_24_fixed_deposits_months;
		}

		public void setR26_19_24_fixed_deposits_months(BigDecimal r26_19_24_fixed_deposits_months) {
			this.r26_19_24_fixed_deposits_months = r26_19_24_fixed_deposits_months;
		}

		public BigDecimal getR26_over_24_fixed_deposits_months() {
			return r26_over_24_fixed_deposits_months;
		}

		public void setR26_over_24_fixed_deposits_months(BigDecimal r26_over_24_fixed_deposits_months) {
			this.r26_over_24_fixed_deposits_months = r26_over_24_fixed_deposits_months;
		}

		public BigDecimal getR26_certificates_of_deposit() {
			return r26_certificates_of_deposit;
		}

		public void setR26_certificates_of_deposit(BigDecimal r26_certificates_of_deposit) {
			this.r26_certificates_of_deposit = r26_certificates_of_deposit;
		}

		public BigDecimal getR26_total() {
			return r26_total;
		}

		public void setR26_total(BigDecimal r26_total) {
			this.r26_total = r26_total;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_current() {
			return r27_current;
		}

		public void setR27_current(BigDecimal r27_current) {
			this.r27_current = r27_current;
		}

		public BigDecimal getR27_call() {
			return r27_call;
		}

		public void setR27_call(BigDecimal r27_call) {
			this.r27_call = r27_call;
		}

		public BigDecimal getR27_savings() {
			return r27_savings;
		}

		public void setR27_savings(BigDecimal r27_savings) {
			this.r27_savings = r27_savings;
		}

		public BigDecimal getR27_0_31_notice_days() {
			return r27_0_31_notice_days;
		}

		public void setR27_0_31_notice_days(BigDecimal r27_0_31_notice_days) {
			this.r27_0_31_notice_days = r27_0_31_notice_days;
		}

		public BigDecimal getR27_32_88_notice_days() {
			return r27_32_88_notice_days;
		}

		public void setR27_32_88_notice_days(BigDecimal r27_32_88_notice_days) {
			this.r27_32_88_notice_days = r27_32_88_notice_days;
		}

		public BigDecimal getR27_91_day_deposit_fixed_deposit_months() {
			return r27_91_day_deposit_fixed_deposit_months;
		}

		public void setR27_91_day_deposit_fixed_deposit_months(BigDecimal r27_91_day_deposit_fixed_deposit_months) {
			this.r27_91_day_deposit_fixed_deposit_months = r27_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR27_1_2_fixed_deposits_months() {
			return r27_1_2_fixed_deposits_months;
		}

		public void setR27_1_2_fixed_deposits_months(BigDecimal r27_1_2_fixed_deposits_months) {
			this.r27_1_2_fixed_deposits_months = r27_1_2_fixed_deposits_months;
		}

		public BigDecimal getR27_4_6_fixed_deposits_months() {
			return r27_4_6_fixed_deposits_months;
		}

		public void setR27_4_6_fixed_deposits_months(BigDecimal r27_4_6_fixed_deposits_months) {
			this.r27_4_6_fixed_deposits_months = r27_4_6_fixed_deposits_months;
		}

		public BigDecimal getR27_7_12_fixed_deposits_months() {
			return r27_7_12_fixed_deposits_months;
		}

		public void setR27_7_12_fixed_deposits_months(BigDecimal r27_7_12_fixed_deposits_months) {
			this.r27_7_12_fixed_deposits_months = r27_7_12_fixed_deposits_months;
		}

		public BigDecimal getR27_13_18_fixed_deposits_months() {
			return r27_13_18_fixed_deposits_months;
		}

		public void setR27_13_18_fixed_deposits_months(BigDecimal r27_13_18_fixed_deposits_months) {
			this.r27_13_18_fixed_deposits_months = r27_13_18_fixed_deposits_months;
		}

		public BigDecimal getR27_19_24_fixed_deposits_months() {
			return r27_19_24_fixed_deposits_months;
		}

		public void setR27_19_24_fixed_deposits_months(BigDecimal r27_19_24_fixed_deposits_months) {
			this.r27_19_24_fixed_deposits_months = r27_19_24_fixed_deposits_months;
		}

		public BigDecimal getR27_over_24_fixed_deposits_months() {
			return r27_over_24_fixed_deposits_months;
		}

		public void setR27_over_24_fixed_deposits_months(BigDecimal r27_over_24_fixed_deposits_months) {
			this.r27_over_24_fixed_deposits_months = r27_over_24_fixed_deposits_months;
		}

		public BigDecimal getR27_certificates_of_deposit() {
			return r27_certificates_of_deposit;
		}

		public void setR27_certificates_of_deposit(BigDecimal r27_certificates_of_deposit) {
			this.r27_certificates_of_deposit = r27_certificates_of_deposit;
		}

		public BigDecimal getR27_total() {
			return r27_total;
		}

		public void setR27_total(BigDecimal r27_total) {
			this.r27_total = r27_total;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_current() {
			return r28_current;
		}

		public void setR28_current(BigDecimal r28_current) {
			this.r28_current = r28_current;
		}

		public BigDecimal getR28_call() {
			return r28_call;
		}

		public void setR28_call(BigDecimal r28_call) {
			this.r28_call = r28_call;
		}

		public BigDecimal getR28_savings() {
			return r28_savings;
		}

		public void setR28_savings(BigDecimal r28_savings) {
			this.r28_savings = r28_savings;
		}

		public BigDecimal getR28_0_31_notice_days() {
			return r28_0_31_notice_days;
		}

		public void setR28_0_31_notice_days(BigDecimal r28_0_31_notice_days) {
			this.r28_0_31_notice_days = r28_0_31_notice_days;
		}

		public BigDecimal getR28_32_88_notice_days() {
			return r28_32_88_notice_days;
		}

		public void setR28_32_88_notice_days(BigDecimal r28_32_88_notice_days) {
			this.r28_32_88_notice_days = r28_32_88_notice_days;
		}

		public BigDecimal getR28_91_day_deposit_fixed_deposit_months() {
			return r28_91_day_deposit_fixed_deposit_months;
		}

		public void setR28_91_day_deposit_fixed_deposit_months(BigDecimal r28_91_day_deposit_fixed_deposit_months) {
			this.r28_91_day_deposit_fixed_deposit_months = r28_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR28_1_2_fixed_deposits_months() {
			return r28_1_2_fixed_deposits_months;
		}

		public void setR28_1_2_fixed_deposits_months(BigDecimal r28_1_2_fixed_deposits_months) {
			this.r28_1_2_fixed_deposits_months = r28_1_2_fixed_deposits_months;
		}

		public BigDecimal getR28_4_6_fixed_deposits_months() {
			return r28_4_6_fixed_deposits_months;
		}

		public void setR28_4_6_fixed_deposits_months(BigDecimal r28_4_6_fixed_deposits_months) {
			this.r28_4_6_fixed_deposits_months = r28_4_6_fixed_deposits_months;
		}

		public BigDecimal getR28_7_12_fixed_deposits_months() {
			return r28_7_12_fixed_deposits_months;
		}

		public void setR28_7_12_fixed_deposits_months(BigDecimal r28_7_12_fixed_deposits_months) {
			this.r28_7_12_fixed_deposits_months = r28_7_12_fixed_deposits_months;
		}

		public BigDecimal getR28_13_18_fixed_deposits_months() {
			return r28_13_18_fixed_deposits_months;
		}

		public void setR28_13_18_fixed_deposits_months(BigDecimal r28_13_18_fixed_deposits_months) {
			this.r28_13_18_fixed_deposits_months = r28_13_18_fixed_deposits_months;
		}

		public BigDecimal getR28_19_24_fixed_deposits_months() {
			return r28_19_24_fixed_deposits_months;
		}

		public void setR28_19_24_fixed_deposits_months(BigDecimal r28_19_24_fixed_deposits_months) {
			this.r28_19_24_fixed_deposits_months = r28_19_24_fixed_deposits_months;
		}

		public BigDecimal getR28_over_24_fixed_deposits_months() {
			return r28_over_24_fixed_deposits_months;
		}

		public void setR28_over_24_fixed_deposits_months(BigDecimal r28_over_24_fixed_deposits_months) {
			this.r28_over_24_fixed_deposits_months = r28_over_24_fixed_deposits_months;
		}

		public BigDecimal getR28_certificates_of_deposit() {
			return r28_certificates_of_deposit;
		}

		public void setR28_certificates_of_deposit(BigDecimal r28_certificates_of_deposit) {
			this.r28_certificates_of_deposit = r28_certificates_of_deposit;
		}

		public BigDecimal getR28_total() {
			return r28_total;
		}

		public void setR28_total(BigDecimal r28_total) {
			this.r28_total = r28_total;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_current() {
			return r29_current;
		}

		public void setR29_current(BigDecimal r29_current) {
			this.r29_current = r29_current;
		}

		public BigDecimal getR29_call() {
			return r29_call;
		}

		public void setR29_call(BigDecimal r29_call) {
			this.r29_call = r29_call;
		}

		public BigDecimal getR29_savings() {
			return r29_savings;
		}

		public void setR29_savings(BigDecimal r29_savings) {
			this.r29_savings = r29_savings;
		}

		public BigDecimal getR29_0_31_notice_days() {
			return r29_0_31_notice_days;
		}

		public void setR29_0_31_notice_days(BigDecimal r29_0_31_notice_days) {
			this.r29_0_31_notice_days = r29_0_31_notice_days;
		}

		public BigDecimal getR29_32_88_notice_days() {
			return r29_32_88_notice_days;
		}

		public void setR29_32_88_notice_days(BigDecimal r29_32_88_notice_days) {
			this.r29_32_88_notice_days = r29_32_88_notice_days;
		}

		public BigDecimal getR29_91_day_deposit_fixed_deposit_months() {
			return r29_91_day_deposit_fixed_deposit_months;
		}

		public void setR29_91_day_deposit_fixed_deposit_months(BigDecimal r29_91_day_deposit_fixed_deposit_months) {
			this.r29_91_day_deposit_fixed_deposit_months = r29_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR29_1_2_fixed_deposits_months() {
			return r29_1_2_fixed_deposits_months;
		}

		public void setR29_1_2_fixed_deposits_months(BigDecimal r29_1_2_fixed_deposits_months) {
			this.r29_1_2_fixed_deposits_months = r29_1_2_fixed_deposits_months;
		}

		public BigDecimal getR29_4_6_fixed_deposits_months() {
			return r29_4_6_fixed_deposits_months;
		}

		public void setR29_4_6_fixed_deposits_months(BigDecimal r29_4_6_fixed_deposits_months) {
			this.r29_4_6_fixed_deposits_months = r29_4_6_fixed_deposits_months;
		}

		public BigDecimal getR29_7_12_fixed_deposits_months() {
			return r29_7_12_fixed_deposits_months;
		}

		public void setR29_7_12_fixed_deposits_months(BigDecimal r29_7_12_fixed_deposits_months) {
			this.r29_7_12_fixed_deposits_months = r29_7_12_fixed_deposits_months;
		}

		public BigDecimal getR29_13_18_fixed_deposits_months() {
			return r29_13_18_fixed_deposits_months;
		}

		public void setR29_13_18_fixed_deposits_months(BigDecimal r29_13_18_fixed_deposits_months) {
			this.r29_13_18_fixed_deposits_months = r29_13_18_fixed_deposits_months;
		}

		public BigDecimal getR29_19_24_fixed_deposits_months() {
			return r29_19_24_fixed_deposits_months;
		}

		public void setR29_19_24_fixed_deposits_months(BigDecimal r29_19_24_fixed_deposits_months) {
			this.r29_19_24_fixed_deposits_months = r29_19_24_fixed_deposits_months;
		}

		public BigDecimal getR29_over_24_fixed_deposits_months() {
			return r29_over_24_fixed_deposits_months;
		}

		public void setR29_over_24_fixed_deposits_months(BigDecimal r29_over_24_fixed_deposits_months) {
			this.r29_over_24_fixed_deposits_months = r29_over_24_fixed_deposits_months;
		}

		public BigDecimal getR29_certificates_of_deposit() {
			return r29_certificates_of_deposit;
		}

		public void setR29_certificates_of_deposit(BigDecimal r29_certificates_of_deposit) {
			this.r29_certificates_of_deposit = r29_certificates_of_deposit;
		}

		public BigDecimal getR29_total() {
			return r29_total;
		}

		public void setR29_total(BigDecimal r29_total) {
			this.r29_total = r29_total;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_current() {
			return r30_current;
		}

		public void setR30_current(BigDecimal r30_current) {
			this.r30_current = r30_current;
		}

		public BigDecimal getR30_call() {
			return r30_call;
		}

		public void setR30_call(BigDecimal r30_call) {
			this.r30_call = r30_call;
		}

		public BigDecimal getR30_savings() {
			return r30_savings;
		}

		public void setR30_savings(BigDecimal r30_savings) {
			this.r30_savings = r30_savings;
		}

		public BigDecimal getR30_0_31_notice_days() {
			return r30_0_31_notice_days;
		}

		public void setR30_0_31_notice_days(BigDecimal r30_0_31_notice_days) {
			this.r30_0_31_notice_days = r30_0_31_notice_days;
		}

		public BigDecimal getR30_32_88_notice_days() {
			return r30_32_88_notice_days;
		}

		public void setR30_32_88_notice_days(BigDecimal r30_32_88_notice_days) {
			this.r30_32_88_notice_days = r30_32_88_notice_days;
		}

		public BigDecimal getR30_91_day_deposit_fixed_deposit_months() {
			return r30_91_day_deposit_fixed_deposit_months;
		}

		public void setR30_91_day_deposit_fixed_deposit_months(BigDecimal r30_91_day_deposit_fixed_deposit_months) {
			this.r30_91_day_deposit_fixed_deposit_months = r30_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR30_1_2_fixed_deposits_months() {
			return r30_1_2_fixed_deposits_months;
		}

		public void setR30_1_2_fixed_deposits_months(BigDecimal r30_1_2_fixed_deposits_months) {
			this.r30_1_2_fixed_deposits_months = r30_1_2_fixed_deposits_months;
		}

		public BigDecimal getR30_4_6_fixed_deposits_months() {
			return r30_4_6_fixed_deposits_months;
		}

		public void setR30_4_6_fixed_deposits_months(BigDecimal r30_4_6_fixed_deposits_months) {
			this.r30_4_6_fixed_deposits_months = r30_4_6_fixed_deposits_months;
		}

		public BigDecimal getR30_7_12_fixed_deposits_months() {
			return r30_7_12_fixed_deposits_months;
		}

		public void setR30_7_12_fixed_deposits_months(BigDecimal r30_7_12_fixed_deposits_months) {
			this.r30_7_12_fixed_deposits_months = r30_7_12_fixed_deposits_months;
		}

		public BigDecimal getR30_13_18_fixed_deposits_months() {
			return r30_13_18_fixed_deposits_months;
		}

		public void setR30_13_18_fixed_deposits_months(BigDecimal r30_13_18_fixed_deposits_months) {
			this.r30_13_18_fixed_deposits_months = r30_13_18_fixed_deposits_months;
		}

		public BigDecimal getR30_19_24_fixed_deposits_months() {
			return r30_19_24_fixed_deposits_months;
		}

		public void setR30_19_24_fixed_deposits_months(BigDecimal r30_19_24_fixed_deposits_months) {
			this.r30_19_24_fixed_deposits_months = r30_19_24_fixed_deposits_months;
		}

		public BigDecimal getR30_over_24_fixed_deposits_months() {
			return r30_over_24_fixed_deposits_months;
		}

		public void setR30_over_24_fixed_deposits_months(BigDecimal r30_over_24_fixed_deposits_months) {
			this.r30_over_24_fixed_deposits_months = r30_over_24_fixed_deposits_months;
		}

		public BigDecimal getR30_certificates_of_deposit() {
			return r30_certificates_of_deposit;
		}

		public void setR30_certificates_of_deposit(BigDecimal r30_certificates_of_deposit) {
			this.r30_certificates_of_deposit = r30_certificates_of_deposit;
		}

		public BigDecimal getR30_total() {
			return r30_total;
		}

		public void setR30_total(BigDecimal r30_total) {
			this.r30_total = r30_total;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_current() {
			return r31_current;
		}

		public void setR31_current(BigDecimal r31_current) {
			this.r31_current = r31_current;
		}

		public BigDecimal getR31_call() {
			return r31_call;
		}

		public void setR31_call(BigDecimal r31_call) {
			this.r31_call = r31_call;
		}

		public BigDecimal getR31_savings() {
			return r31_savings;
		}

		public void setR31_savings(BigDecimal r31_savings) {
			this.r31_savings = r31_savings;
		}

		public BigDecimal getR31_0_31_notice_days() {
			return r31_0_31_notice_days;
		}

		public void setR31_0_31_notice_days(BigDecimal r31_0_31_notice_days) {
			this.r31_0_31_notice_days = r31_0_31_notice_days;
		}

		public BigDecimal getR31_32_88_notice_days() {
			return r31_32_88_notice_days;
		}

		public void setR31_32_88_notice_days(BigDecimal r31_32_88_notice_days) {
			this.r31_32_88_notice_days = r31_32_88_notice_days;
		}

		public BigDecimal getR31_91_day_deposit_fixed_deposit_months() {
			return r31_91_day_deposit_fixed_deposit_months;
		}

		public void setR31_91_day_deposit_fixed_deposit_months(BigDecimal r31_91_day_deposit_fixed_deposit_months) {
			this.r31_91_day_deposit_fixed_deposit_months = r31_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR31_1_2_fixed_deposits_months() {
			return r31_1_2_fixed_deposits_months;
		}

		public void setR31_1_2_fixed_deposits_months(BigDecimal r31_1_2_fixed_deposits_months) {
			this.r31_1_2_fixed_deposits_months = r31_1_2_fixed_deposits_months;
		}

		public BigDecimal getR31_4_6_fixed_deposits_months() {
			return r31_4_6_fixed_deposits_months;
		}

		public void setR31_4_6_fixed_deposits_months(BigDecimal r31_4_6_fixed_deposits_months) {
			this.r31_4_6_fixed_deposits_months = r31_4_6_fixed_deposits_months;
		}

		public BigDecimal getR31_7_12_fixed_deposits_months() {
			return r31_7_12_fixed_deposits_months;
		}

		public void setR31_7_12_fixed_deposits_months(BigDecimal r31_7_12_fixed_deposits_months) {
			this.r31_7_12_fixed_deposits_months = r31_7_12_fixed_deposits_months;
		}

		public BigDecimal getR31_13_18_fixed_deposits_months() {
			return r31_13_18_fixed_deposits_months;
		}

		public void setR31_13_18_fixed_deposits_months(BigDecimal r31_13_18_fixed_deposits_months) {
			this.r31_13_18_fixed_deposits_months = r31_13_18_fixed_deposits_months;
		}

		public BigDecimal getR31_19_24_fixed_deposits_months() {
			return r31_19_24_fixed_deposits_months;
		}

		public void setR31_19_24_fixed_deposits_months(BigDecimal r31_19_24_fixed_deposits_months) {
			this.r31_19_24_fixed_deposits_months = r31_19_24_fixed_deposits_months;
		}

		public BigDecimal getR31_over_24_fixed_deposits_months() {
			return r31_over_24_fixed_deposits_months;
		}

		public void setR31_over_24_fixed_deposits_months(BigDecimal r31_over_24_fixed_deposits_months) {
			this.r31_over_24_fixed_deposits_months = r31_over_24_fixed_deposits_months;
		}

		public BigDecimal getR31_certificates_of_deposit() {
			return r31_certificates_of_deposit;
		}

		public void setR31_certificates_of_deposit(BigDecimal r31_certificates_of_deposit) {
			this.r31_certificates_of_deposit = r31_certificates_of_deposit;
		}

		public BigDecimal getR31_total() {
			return r31_total;
		}

		public void setR31_total(BigDecimal r31_total) {
			this.r31_total = r31_total;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_current() {
			return r32_current;
		}

		public void setR32_current(BigDecimal r32_current) {
			this.r32_current = r32_current;
		}

		public BigDecimal getR32_call() {
			return r32_call;
		}

		public void setR32_call(BigDecimal r32_call) {
			this.r32_call = r32_call;
		}

		public BigDecimal getR32_savings() {
			return r32_savings;
		}

		public void setR32_savings(BigDecimal r32_savings) {
			this.r32_savings = r32_savings;
		}

		public BigDecimal getR32_0_31_notice_days() {
			return r32_0_31_notice_days;
		}

		public void setR32_0_31_notice_days(BigDecimal r32_0_31_notice_days) {
			this.r32_0_31_notice_days = r32_0_31_notice_days;
		}

		public BigDecimal getR32_32_88_notice_days() {
			return r32_32_88_notice_days;
		}

		public void setR32_32_88_notice_days(BigDecimal r32_32_88_notice_days) {
			this.r32_32_88_notice_days = r32_32_88_notice_days;
		}

		public BigDecimal getR32_91_day_deposit_fixed_deposit_months() {
			return r32_91_day_deposit_fixed_deposit_months;
		}

		public void setR32_91_day_deposit_fixed_deposit_months(BigDecimal r32_91_day_deposit_fixed_deposit_months) {
			this.r32_91_day_deposit_fixed_deposit_months = r32_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR32_1_2_fixed_deposits_months() {
			return r32_1_2_fixed_deposits_months;
		}

		public void setR32_1_2_fixed_deposits_months(BigDecimal r32_1_2_fixed_deposits_months) {
			this.r32_1_2_fixed_deposits_months = r32_1_2_fixed_deposits_months;
		}

		public BigDecimal getR32_4_6_fixed_deposits_months() {
			return r32_4_6_fixed_deposits_months;
		}

		public void setR32_4_6_fixed_deposits_months(BigDecimal r32_4_6_fixed_deposits_months) {
			this.r32_4_6_fixed_deposits_months = r32_4_6_fixed_deposits_months;
		}

		public BigDecimal getR32_7_12_fixed_deposits_months() {
			return r32_7_12_fixed_deposits_months;
		}

		public void setR32_7_12_fixed_deposits_months(BigDecimal r32_7_12_fixed_deposits_months) {
			this.r32_7_12_fixed_deposits_months = r32_7_12_fixed_deposits_months;
		}

		public BigDecimal getR32_13_18_fixed_deposits_months() {
			return r32_13_18_fixed_deposits_months;
		}

		public void setR32_13_18_fixed_deposits_months(BigDecimal r32_13_18_fixed_deposits_months) {
			this.r32_13_18_fixed_deposits_months = r32_13_18_fixed_deposits_months;
		}

		public BigDecimal getR32_19_24_fixed_deposits_months() {
			return r32_19_24_fixed_deposits_months;
		}

		public void setR32_19_24_fixed_deposits_months(BigDecimal r32_19_24_fixed_deposits_months) {
			this.r32_19_24_fixed_deposits_months = r32_19_24_fixed_deposits_months;
		}

		public BigDecimal getR32_over_24_fixed_deposits_months() {
			return r32_over_24_fixed_deposits_months;
		}

		public void setR32_over_24_fixed_deposits_months(BigDecimal r32_over_24_fixed_deposits_months) {
			this.r32_over_24_fixed_deposits_months = r32_over_24_fixed_deposits_months;
		}

		public BigDecimal getR32_certificates_of_deposit() {
			return r32_certificates_of_deposit;
		}

		public void setR32_certificates_of_deposit(BigDecimal r32_certificates_of_deposit) {
			this.r32_certificates_of_deposit = r32_certificates_of_deposit;
		}

		public BigDecimal getR32_total() {
			return r32_total;
		}

		public void setR32_total(BigDecimal r32_total) {
			this.r32_total = r32_total;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_current() {
			return r33_current;
		}

		public void setR33_current(BigDecimal r33_current) {
			this.r33_current = r33_current;
		}

		public BigDecimal getR33_call() {
			return r33_call;
		}

		public void setR33_call(BigDecimal r33_call) {
			this.r33_call = r33_call;
		}

		public BigDecimal getR33_savings() {
			return r33_savings;
		}

		public void setR33_savings(BigDecimal r33_savings) {
			this.r33_savings = r33_savings;
		}

		public BigDecimal getR33_0_31_notice_days() {
			return r33_0_31_notice_days;
		}

		public void setR33_0_31_notice_days(BigDecimal r33_0_31_notice_days) {
			this.r33_0_31_notice_days = r33_0_31_notice_days;
		}

		public BigDecimal getR33_32_88_notice_days() {
			return r33_32_88_notice_days;
		}

		public void setR33_32_88_notice_days(BigDecimal r33_32_88_notice_days) {
			this.r33_32_88_notice_days = r33_32_88_notice_days;
		}

		public BigDecimal getR33_91_day_deposit_fixed_deposit_months() {
			return r33_91_day_deposit_fixed_deposit_months;
		}

		public void setR33_91_day_deposit_fixed_deposit_months(BigDecimal r33_91_day_deposit_fixed_deposit_months) {
			this.r33_91_day_deposit_fixed_deposit_months = r33_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR33_1_2_fixed_deposits_months() {
			return r33_1_2_fixed_deposits_months;
		}

		public void setR33_1_2_fixed_deposits_months(BigDecimal r33_1_2_fixed_deposits_months) {
			this.r33_1_2_fixed_deposits_months = r33_1_2_fixed_deposits_months;
		}

		public BigDecimal getR33_4_6_fixed_deposits_months() {
			return r33_4_6_fixed_deposits_months;
		}

		public void setR33_4_6_fixed_deposits_months(BigDecimal r33_4_6_fixed_deposits_months) {
			this.r33_4_6_fixed_deposits_months = r33_4_6_fixed_deposits_months;
		}

		public BigDecimal getR33_7_12_fixed_deposits_months() {
			return r33_7_12_fixed_deposits_months;
		}

		public void setR33_7_12_fixed_deposits_months(BigDecimal r33_7_12_fixed_deposits_months) {
			this.r33_7_12_fixed_deposits_months = r33_7_12_fixed_deposits_months;
		}

		public BigDecimal getR33_13_18_fixed_deposits_months() {
			return r33_13_18_fixed_deposits_months;
		}

		public void setR33_13_18_fixed_deposits_months(BigDecimal r33_13_18_fixed_deposits_months) {
			this.r33_13_18_fixed_deposits_months = r33_13_18_fixed_deposits_months;
		}

		public BigDecimal getR33_19_24_fixed_deposits_months() {
			return r33_19_24_fixed_deposits_months;
		}

		public void setR33_19_24_fixed_deposits_months(BigDecimal r33_19_24_fixed_deposits_months) {
			this.r33_19_24_fixed_deposits_months = r33_19_24_fixed_deposits_months;
		}

		public BigDecimal getR33_over_24_fixed_deposits_months() {
			return r33_over_24_fixed_deposits_months;
		}

		public void setR33_over_24_fixed_deposits_months(BigDecimal r33_over_24_fixed_deposits_months) {
			this.r33_over_24_fixed_deposits_months = r33_over_24_fixed_deposits_months;
		}

		public BigDecimal getR33_certificates_of_deposit() {
			return r33_certificates_of_deposit;
		}

		public void setR33_certificates_of_deposit(BigDecimal r33_certificates_of_deposit) {
			this.r33_certificates_of_deposit = r33_certificates_of_deposit;
		}

		public BigDecimal getR33_total() {
			return r33_total;
		}

		public void setR33_total(BigDecimal r33_total) {
			this.r33_total = r33_total;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_current() {
			return r34_current;
		}

		public void setR34_current(BigDecimal r34_current) {
			this.r34_current = r34_current;
		}

		public BigDecimal getR34_call() {
			return r34_call;
		}

		public void setR34_call(BigDecimal r34_call) {
			this.r34_call = r34_call;
		}

		public BigDecimal getR34_savings() {
			return r34_savings;
		}

		public void setR34_savings(BigDecimal r34_savings) {
			this.r34_savings = r34_savings;
		}

		public BigDecimal getR34_0_31_notice_days() {
			return r34_0_31_notice_days;
		}

		public void setR34_0_31_notice_days(BigDecimal r34_0_31_notice_days) {
			this.r34_0_31_notice_days = r34_0_31_notice_days;
		}

		public BigDecimal getR34_32_88_notice_days() {
			return r34_32_88_notice_days;
		}

		public void setR34_32_88_notice_days(BigDecimal r34_32_88_notice_days) {
			this.r34_32_88_notice_days = r34_32_88_notice_days;
		}

		public BigDecimal getR34_91_day_deposit_fixed_deposit_months() {
			return r34_91_day_deposit_fixed_deposit_months;
		}

		public void setR34_91_day_deposit_fixed_deposit_months(BigDecimal r34_91_day_deposit_fixed_deposit_months) {
			this.r34_91_day_deposit_fixed_deposit_months = r34_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR34_1_2_fixed_deposits_months() {
			return r34_1_2_fixed_deposits_months;
		}

		public void setR34_1_2_fixed_deposits_months(BigDecimal r34_1_2_fixed_deposits_months) {
			this.r34_1_2_fixed_deposits_months = r34_1_2_fixed_deposits_months;
		}

		public BigDecimal getR34_4_6_fixed_deposits_months() {
			return r34_4_6_fixed_deposits_months;
		}

		public void setR34_4_6_fixed_deposits_months(BigDecimal r34_4_6_fixed_deposits_months) {
			this.r34_4_6_fixed_deposits_months = r34_4_6_fixed_deposits_months;
		}

		public BigDecimal getR34_7_12_fixed_deposits_months() {
			return r34_7_12_fixed_deposits_months;
		}

		public void setR34_7_12_fixed_deposits_months(BigDecimal r34_7_12_fixed_deposits_months) {
			this.r34_7_12_fixed_deposits_months = r34_7_12_fixed_deposits_months;
		}

		public BigDecimal getR34_13_18_fixed_deposits_months() {
			return r34_13_18_fixed_deposits_months;
		}

		public void setR34_13_18_fixed_deposits_months(BigDecimal r34_13_18_fixed_deposits_months) {
			this.r34_13_18_fixed_deposits_months = r34_13_18_fixed_deposits_months;
		}

		public BigDecimal getR34_19_24_fixed_deposits_months() {
			return r34_19_24_fixed_deposits_months;
		}

		public void setR34_19_24_fixed_deposits_months(BigDecimal r34_19_24_fixed_deposits_months) {
			this.r34_19_24_fixed_deposits_months = r34_19_24_fixed_deposits_months;
		}

		public BigDecimal getR34_over_24_fixed_deposits_months() {
			return r34_over_24_fixed_deposits_months;
		}

		public void setR34_over_24_fixed_deposits_months(BigDecimal r34_over_24_fixed_deposits_months) {
			this.r34_over_24_fixed_deposits_months = r34_over_24_fixed_deposits_months;
		}

		public BigDecimal getR34_certificates_of_deposit() {
			return r34_certificates_of_deposit;
		}

		public void setR34_certificates_of_deposit(BigDecimal r34_certificates_of_deposit) {
			this.r34_certificates_of_deposit = r34_certificates_of_deposit;
		}

		public BigDecimal getR34_total() {
			return r34_total;
		}

		public void setR34_total(BigDecimal r34_total) {
			this.r34_total = r34_total;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_current() {
			return r35_current;
		}

		public void setR35_current(BigDecimal r35_current) {
			this.r35_current = r35_current;
		}

		public BigDecimal getR35_call() {
			return r35_call;
		}

		public void setR35_call(BigDecimal r35_call) {
			this.r35_call = r35_call;
		}

		public BigDecimal getR35_savings() {
			return r35_savings;
		}

		public void setR35_savings(BigDecimal r35_savings) {
			this.r35_savings = r35_savings;
		}

		public BigDecimal getR35_0_31_notice_days() {
			return r35_0_31_notice_days;
		}

		public void setR35_0_31_notice_days(BigDecimal r35_0_31_notice_days) {
			this.r35_0_31_notice_days = r35_0_31_notice_days;
		}

		public BigDecimal getR35_32_88_notice_days() {
			return r35_32_88_notice_days;
		}

		public void setR35_32_88_notice_days(BigDecimal r35_32_88_notice_days) {
			this.r35_32_88_notice_days = r35_32_88_notice_days;
		}

		public BigDecimal getR35_91_day_deposit_fixed_deposit_months() {
			return r35_91_day_deposit_fixed_deposit_months;
		}

		public void setR35_91_day_deposit_fixed_deposit_months(BigDecimal r35_91_day_deposit_fixed_deposit_months) {
			this.r35_91_day_deposit_fixed_deposit_months = r35_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR35_1_2_fixed_deposits_months() {
			return r35_1_2_fixed_deposits_months;
		}

		public void setR35_1_2_fixed_deposits_months(BigDecimal r35_1_2_fixed_deposits_months) {
			this.r35_1_2_fixed_deposits_months = r35_1_2_fixed_deposits_months;
		}

		public BigDecimal getR35_4_6_fixed_deposits_months() {
			return r35_4_6_fixed_deposits_months;
		}

		public void setR35_4_6_fixed_deposits_months(BigDecimal r35_4_6_fixed_deposits_months) {
			this.r35_4_6_fixed_deposits_months = r35_4_6_fixed_deposits_months;
		}

		public BigDecimal getR35_7_12_fixed_deposits_months() {
			return r35_7_12_fixed_deposits_months;
		}

		public void setR35_7_12_fixed_deposits_months(BigDecimal r35_7_12_fixed_deposits_months) {
			this.r35_7_12_fixed_deposits_months = r35_7_12_fixed_deposits_months;
		}

		public BigDecimal getR35_13_18_fixed_deposits_months() {
			return r35_13_18_fixed_deposits_months;
		}

		public void setR35_13_18_fixed_deposits_months(BigDecimal r35_13_18_fixed_deposits_months) {
			this.r35_13_18_fixed_deposits_months = r35_13_18_fixed_deposits_months;
		}

		public BigDecimal getR35_19_24_fixed_deposits_months() {
			return r35_19_24_fixed_deposits_months;
		}

		public void setR35_19_24_fixed_deposits_months(BigDecimal r35_19_24_fixed_deposits_months) {
			this.r35_19_24_fixed_deposits_months = r35_19_24_fixed_deposits_months;
		}

		public BigDecimal getR35_over_24_fixed_deposits_months() {
			return r35_over_24_fixed_deposits_months;
		}

		public void setR35_over_24_fixed_deposits_months(BigDecimal r35_over_24_fixed_deposits_months) {
			this.r35_over_24_fixed_deposits_months = r35_over_24_fixed_deposits_months;
		}

		public BigDecimal getR35_certificates_of_deposit() {
			return r35_certificates_of_deposit;
		}

		public void setR35_certificates_of_deposit(BigDecimal r35_certificates_of_deposit) {
			this.r35_certificates_of_deposit = r35_certificates_of_deposit;
		}

		public BigDecimal getR35_total() {
			return r35_total;
		}

		public void setR35_total(BigDecimal r35_total) {
			this.r35_total = r35_total;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_current() {
			return r36_current;
		}

		public void setR36_current(BigDecimal r36_current) {
			this.r36_current = r36_current;
		}

		public BigDecimal getR36_call() {
			return r36_call;
		}

		public void setR36_call(BigDecimal r36_call) {
			this.r36_call = r36_call;
		}

		public BigDecimal getR36_savings() {
			return r36_savings;
		}

		public void setR36_savings(BigDecimal r36_savings) {
			this.r36_savings = r36_savings;
		}

		public BigDecimal getR36_0_31_notice_days() {
			return r36_0_31_notice_days;
		}

		public void setR36_0_31_notice_days(BigDecimal r36_0_31_notice_days) {
			this.r36_0_31_notice_days = r36_0_31_notice_days;
		}

		public BigDecimal getR36_32_88_notice_days() {
			return r36_32_88_notice_days;
		}

		public void setR36_32_88_notice_days(BigDecimal r36_32_88_notice_days) {
			this.r36_32_88_notice_days = r36_32_88_notice_days;
		}

		public BigDecimal getR36_91_day_deposit_fixed_deposit_months() {
			return r36_91_day_deposit_fixed_deposit_months;
		}

		public void setR36_91_day_deposit_fixed_deposit_months(BigDecimal r36_91_day_deposit_fixed_deposit_months) {
			this.r36_91_day_deposit_fixed_deposit_months = r36_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR36_1_2_fixed_deposits_months() {
			return r36_1_2_fixed_deposits_months;
		}

		public void setR36_1_2_fixed_deposits_months(BigDecimal r36_1_2_fixed_deposits_months) {
			this.r36_1_2_fixed_deposits_months = r36_1_2_fixed_deposits_months;
		}

		public BigDecimal getR36_4_6_fixed_deposits_months() {
			return r36_4_6_fixed_deposits_months;
		}

		public void setR36_4_6_fixed_deposits_months(BigDecimal r36_4_6_fixed_deposits_months) {
			this.r36_4_6_fixed_deposits_months = r36_4_6_fixed_deposits_months;
		}

		public BigDecimal getR36_7_12_fixed_deposits_months() {
			return r36_7_12_fixed_deposits_months;
		}

		public void setR36_7_12_fixed_deposits_months(BigDecimal r36_7_12_fixed_deposits_months) {
			this.r36_7_12_fixed_deposits_months = r36_7_12_fixed_deposits_months;
		}

		public BigDecimal getR36_13_18_fixed_deposits_months() {
			return r36_13_18_fixed_deposits_months;
		}

		public void setR36_13_18_fixed_deposits_months(BigDecimal r36_13_18_fixed_deposits_months) {
			this.r36_13_18_fixed_deposits_months = r36_13_18_fixed_deposits_months;
		}

		public BigDecimal getR36_19_24_fixed_deposits_months() {
			return r36_19_24_fixed_deposits_months;
		}

		public void setR36_19_24_fixed_deposits_months(BigDecimal r36_19_24_fixed_deposits_months) {
			this.r36_19_24_fixed_deposits_months = r36_19_24_fixed_deposits_months;
		}

		public BigDecimal getR36_over_24_fixed_deposits_months() {
			return r36_over_24_fixed_deposits_months;
		}

		public void setR36_over_24_fixed_deposits_months(BigDecimal r36_over_24_fixed_deposits_months) {
			this.r36_over_24_fixed_deposits_months = r36_over_24_fixed_deposits_months;
		}

		public BigDecimal getR36_certificates_of_deposit() {
			return r36_certificates_of_deposit;
		}

		public void setR36_certificates_of_deposit(BigDecimal r36_certificates_of_deposit) {
			this.r36_certificates_of_deposit = r36_certificates_of_deposit;
		}

		public BigDecimal getR36_total() {
			return r36_total;
		}

		public void setR36_total(BigDecimal r36_total) {
			this.r36_total = r36_total;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_current() {
			return r37_current;
		}

		public void setR37_current(BigDecimal r37_current) {
			this.r37_current = r37_current;
		}

		public BigDecimal getR37_call() {
			return r37_call;
		}

		public void setR37_call(BigDecimal r37_call) {
			this.r37_call = r37_call;
		}

		public BigDecimal getR37_savings() {
			return r37_savings;
		}

		public void setR37_savings(BigDecimal r37_savings) {
			this.r37_savings = r37_savings;
		}

		public BigDecimal getR37_0_31_notice_days() {
			return r37_0_31_notice_days;
		}

		public void setR37_0_31_notice_days(BigDecimal r37_0_31_notice_days) {
			this.r37_0_31_notice_days = r37_0_31_notice_days;
		}

		public BigDecimal getR37_32_88_notice_days() {
			return r37_32_88_notice_days;
		}

		public void setR37_32_88_notice_days(BigDecimal r37_32_88_notice_days) {
			this.r37_32_88_notice_days = r37_32_88_notice_days;
		}

		public BigDecimal getR37_91_day_deposit_fixed_deposit_months() {
			return r37_91_day_deposit_fixed_deposit_months;
		}

		public void setR37_91_day_deposit_fixed_deposit_months(BigDecimal r37_91_day_deposit_fixed_deposit_months) {
			this.r37_91_day_deposit_fixed_deposit_months = r37_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR37_1_2_fixed_deposits_months() {
			return r37_1_2_fixed_deposits_months;
		}

		public void setR37_1_2_fixed_deposits_months(BigDecimal r37_1_2_fixed_deposits_months) {
			this.r37_1_2_fixed_deposits_months = r37_1_2_fixed_deposits_months;
		}

		public BigDecimal getR37_4_6_fixed_deposits_months() {
			return r37_4_6_fixed_deposits_months;
		}

		public void setR37_4_6_fixed_deposits_months(BigDecimal r37_4_6_fixed_deposits_months) {
			this.r37_4_6_fixed_deposits_months = r37_4_6_fixed_deposits_months;
		}

		public BigDecimal getR37_7_12_fixed_deposits_months() {
			return r37_7_12_fixed_deposits_months;
		}

		public void setR37_7_12_fixed_deposits_months(BigDecimal r37_7_12_fixed_deposits_months) {
			this.r37_7_12_fixed_deposits_months = r37_7_12_fixed_deposits_months;
		}

		public BigDecimal getR37_13_18_fixed_deposits_months() {
			return r37_13_18_fixed_deposits_months;
		}

		public void setR37_13_18_fixed_deposits_months(BigDecimal r37_13_18_fixed_deposits_months) {
			this.r37_13_18_fixed_deposits_months = r37_13_18_fixed_deposits_months;
		}

		public BigDecimal getR37_19_24_fixed_deposits_months() {
			return r37_19_24_fixed_deposits_months;
		}

		public void setR37_19_24_fixed_deposits_months(BigDecimal r37_19_24_fixed_deposits_months) {
			this.r37_19_24_fixed_deposits_months = r37_19_24_fixed_deposits_months;
		}

		public BigDecimal getR37_over_24_fixed_deposits_months() {
			return r37_over_24_fixed_deposits_months;
		}

		public void setR37_over_24_fixed_deposits_months(BigDecimal r37_over_24_fixed_deposits_months) {
			this.r37_over_24_fixed_deposits_months = r37_over_24_fixed_deposits_months;
		}

		public BigDecimal getR37_certificates_of_deposit() {
			return r37_certificates_of_deposit;
		}

		public void setR37_certificates_of_deposit(BigDecimal r37_certificates_of_deposit) {
			this.r37_certificates_of_deposit = r37_certificates_of_deposit;
		}

		public BigDecimal getR37_total() {
			return r37_total;
		}

		public void setR37_total(BigDecimal r37_total) {
			this.r37_total = r37_total;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_current() {
			return r38_current;
		}

		public void setR38_current(BigDecimal r38_current) {
			this.r38_current = r38_current;
		}

		public BigDecimal getR38_call() {
			return r38_call;
		}

		public void setR38_call(BigDecimal r38_call) {
			this.r38_call = r38_call;
		}

		public BigDecimal getR38_savings() {
			return r38_savings;
		}

		public void setR38_savings(BigDecimal r38_savings) {
			this.r38_savings = r38_savings;
		}

		public BigDecimal getR38_0_31_notice_days() {
			return r38_0_31_notice_days;
		}

		public void setR38_0_31_notice_days(BigDecimal r38_0_31_notice_days) {
			this.r38_0_31_notice_days = r38_0_31_notice_days;
		}

		public BigDecimal getR38_32_88_notice_days() {
			return r38_32_88_notice_days;
		}

		public void setR38_32_88_notice_days(BigDecimal r38_32_88_notice_days) {
			this.r38_32_88_notice_days = r38_32_88_notice_days;
		}

		public BigDecimal getR38_91_day_deposit_fixed_deposit_months() {
			return r38_91_day_deposit_fixed_deposit_months;
		}

		public void setR38_91_day_deposit_fixed_deposit_months(BigDecimal r38_91_day_deposit_fixed_deposit_months) {
			this.r38_91_day_deposit_fixed_deposit_months = r38_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR38_1_2_fixed_deposits_months() {
			return r38_1_2_fixed_deposits_months;
		}

		public void setR38_1_2_fixed_deposits_months(BigDecimal r38_1_2_fixed_deposits_months) {
			this.r38_1_2_fixed_deposits_months = r38_1_2_fixed_deposits_months;
		}

		public BigDecimal getR38_4_6_fixed_deposits_months() {
			return r38_4_6_fixed_deposits_months;
		}

		public void setR38_4_6_fixed_deposits_months(BigDecimal r38_4_6_fixed_deposits_months) {
			this.r38_4_6_fixed_deposits_months = r38_4_6_fixed_deposits_months;
		}

		public BigDecimal getR38_7_12_fixed_deposits_months() {
			return r38_7_12_fixed_deposits_months;
		}

		public void setR38_7_12_fixed_deposits_months(BigDecimal r38_7_12_fixed_deposits_months) {
			this.r38_7_12_fixed_deposits_months = r38_7_12_fixed_deposits_months;
		}

		public BigDecimal getR38_13_18_fixed_deposits_months() {
			return r38_13_18_fixed_deposits_months;
		}

		public void setR38_13_18_fixed_deposits_months(BigDecimal r38_13_18_fixed_deposits_months) {
			this.r38_13_18_fixed_deposits_months = r38_13_18_fixed_deposits_months;
		}

		public BigDecimal getR38_19_24_fixed_deposits_months() {
			return r38_19_24_fixed_deposits_months;
		}

		public void setR38_19_24_fixed_deposits_months(BigDecimal r38_19_24_fixed_deposits_months) {
			this.r38_19_24_fixed_deposits_months = r38_19_24_fixed_deposits_months;
		}

		public BigDecimal getR38_over_24_fixed_deposits_months() {
			return r38_over_24_fixed_deposits_months;
		}

		public void setR38_over_24_fixed_deposits_months(BigDecimal r38_over_24_fixed_deposits_months) {
			this.r38_over_24_fixed_deposits_months = r38_over_24_fixed_deposits_months;
		}

		public BigDecimal getR38_certificates_of_deposit() {
			return r38_certificates_of_deposit;
		}

		public void setR38_certificates_of_deposit(BigDecimal r38_certificates_of_deposit) {
			this.r38_certificates_of_deposit = r38_certificates_of_deposit;
		}

		public BigDecimal getR38_total() {
			return r38_total;
		}

		public void setR38_total(BigDecimal r38_total) {
			this.r38_total = r38_total;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_current() {
			return r39_current;
		}

		public void setR39_current(BigDecimal r39_current) {
			this.r39_current = r39_current;
		}

		public BigDecimal getR39_call() {
			return r39_call;
		}

		public void setR39_call(BigDecimal r39_call) {
			this.r39_call = r39_call;
		}

		public BigDecimal getR39_savings() {
			return r39_savings;
		}

		public void setR39_savings(BigDecimal r39_savings) {
			this.r39_savings = r39_savings;
		}

		public BigDecimal getR39_0_31_notice_days() {
			return r39_0_31_notice_days;
		}

		public void setR39_0_31_notice_days(BigDecimal r39_0_31_notice_days) {
			this.r39_0_31_notice_days = r39_0_31_notice_days;
		}

		public BigDecimal getR39_32_88_notice_days() {
			return r39_32_88_notice_days;
		}

		public void setR39_32_88_notice_days(BigDecimal r39_32_88_notice_days) {
			this.r39_32_88_notice_days = r39_32_88_notice_days;
		}

		public BigDecimal getR39_91_day_deposit_fixed_deposit_months() {
			return r39_91_day_deposit_fixed_deposit_months;
		}

		public void setR39_91_day_deposit_fixed_deposit_months(BigDecimal r39_91_day_deposit_fixed_deposit_months) {
			this.r39_91_day_deposit_fixed_deposit_months = r39_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR39_1_2_fixed_deposits_months() {
			return r39_1_2_fixed_deposits_months;
		}

		public void setR39_1_2_fixed_deposits_months(BigDecimal r39_1_2_fixed_deposits_months) {
			this.r39_1_2_fixed_deposits_months = r39_1_2_fixed_deposits_months;
		}

		public BigDecimal getR39_4_6_fixed_deposits_months() {
			return r39_4_6_fixed_deposits_months;
		}

		public void setR39_4_6_fixed_deposits_months(BigDecimal r39_4_6_fixed_deposits_months) {
			this.r39_4_6_fixed_deposits_months = r39_4_6_fixed_deposits_months;
		}

		public BigDecimal getR39_7_12_fixed_deposits_months() {
			return r39_7_12_fixed_deposits_months;
		}

		public void setR39_7_12_fixed_deposits_months(BigDecimal r39_7_12_fixed_deposits_months) {
			this.r39_7_12_fixed_deposits_months = r39_7_12_fixed_deposits_months;
		}

		public BigDecimal getR39_13_18_fixed_deposits_months() {
			return r39_13_18_fixed_deposits_months;
		}

		public void setR39_13_18_fixed_deposits_months(BigDecimal r39_13_18_fixed_deposits_months) {
			this.r39_13_18_fixed_deposits_months = r39_13_18_fixed_deposits_months;
		}

		public BigDecimal getR39_19_24_fixed_deposits_months() {
			return r39_19_24_fixed_deposits_months;
		}

		public void setR39_19_24_fixed_deposits_months(BigDecimal r39_19_24_fixed_deposits_months) {
			this.r39_19_24_fixed_deposits_months = r39_19_24_fixed_deposits_months;
		}

		public BigDecimal getR39_over_24_fixed_deposits_months() {
			return r39_over_24_fixed_deposits_months;
		}

		public void setR39_over_24_fixed_deposits_months(BigDecimal r39_over_24_fixed_deposits_months) {
			this.r39_over_24_fixed_deposits_months = r39_over_24_fixed_deposits_months;
		}

		public BigDecimal getR39_certificates_of_deposit() {
			return r39_certificates_of_deposit;
		}

		public void setR39_certificates_of_deposit(BigDecimal r39_certificates_of_deposit) {
			this.r39_certificates_of_deposit = r39_certificates_of_deposit;
		}

		public BigDecimal getR39_total() {
			return r39_total;
		}

		public void setR39_total(BigDecimal r39_total) {
			this.r39_total = r39_total;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_current() {
			return r40_current;
		}

		public void setR40_current(BigDecimal r40_current) {
			this.r40_current = r40_current;
		}

		public BigDecimal getR40_call() {
			return r40_call;
		}

		public void setR40_call(BigDecimal r40_call) {
			this.r40_call = r40_call;
		}

		public BigDecimal getR40_savings() {
			return r40_savings;
		}

		public void setR40_savings(BigDecimal r40_savings) {
			this.r40_savings = r40_savings;
		}

		public BigDecimal getR40_0_31_notice_days() {
			return r40_0_31_notice_days;
		}

		public void setR40_0_31_notice_days(BigDecimal r40_0_31_notice_days) {
			this.r40_0_31_notice_days = r40_0_31_notice_days;
		}

		public BigDecimal getR40_32_88_notice_days() {
			return r40_32_88_notice_days;
		}

		public void setR40_32_88_notice_days(BigDecimal r40_32_88_notice_days) {
			this.r40_32_88_notice_days = r40_32_88_notice_days;
		}

		public BigDecimal getR40_91_day_deposit_fixed_deposit_months() {
			return r40_91_day_deposit_fixed_deposit_months;
		}

		public void setR40_91_day_deposit_fixed_deposit_months(BigDecimal r40_91_day_deposit_fixed_deposit_months) {
			this.r40_91_day_deposit_fixed_deposit_months = r40_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR40_1_2_fixed_deposits_months() {
			return r40_1_2_fixed_deposits_months;
		}

		public void setR40_1_2_fixed_deposits_months(BigDecimal r40_1_2_fixed_deposits_months) {
			this.r40_1_2_fixed_deposits_months = r40_1_2_fixed_deposits_months;
		}

		public BigDecimal getR40_4_6_fixed_deposits_months() {
			return r40_4_6_fixed_deposits_months;
		}

		public void setR40_4_6_fixed_deposits_months(BigDecimal r40_4_6_fixed_deposits_months) {
			this.r40_4_6_fixed_deposits_months = r40_4_6_fixed_deposits_months;
		}

		public BigDecimal getR40_7_12_fixed_deposits_months() {
			return r40_7_12_fixed_deposits_months;
		}

		public void setR40_7_12_fixed_deposits_months(BigDecimal r40_7_12_fixed_deposits_months) {
			this.r40_7_12_fixed_deposits_months = r40_7_12_fixed_deposits_months;
		}

		public BigDecimal getR40_13_18_fixed_deposits_months() {
			return r40_13_18_fixed_deposits_months;
		}

		public void setR40_13_18_fixed_deposits_months(BigDecimal r40_13_18_fixed_deposits_months) {
			this.r40_13_18_fixed_deposits_months = r40_13_18_fixed_deposits_months;
		}

		public BigDecimal getR40_19_24_fixed_deposits_months() {
			return r40_19_24_fixed_deposits_months;
		}

		public void setR40_19_24_fixed_deposits_months(BigDecimal r40_19_24_fixed_deposits_months) {
			this.r40_19_24_fixed_deposits_months = r40_19_24_fixed_deposits_months;
		}

		public BigDecimal getR40_over_24_fixed_deposits_months() {
			return r40_over_24_fixed_deposits_months;
		}

		public void setR40_over_24_fixed_deposits_months(BigDecimal r40_over_24_fixed_deposits_months) {
			this.r40_over_24_fixed_deposits_months = r40_over_24_fixed_deposits_months;
		}

		public BigDecimal getR40_certificates_of_deposit() {
			return r40_certificates_of_deposit;
		}

		public void setR40_certificates_of_deposit(BigDecimal r40_certificates_of_deposit) {
			this.r40_certificates_of_deposit = r40_certificates_of_deposit;
		}

		public BigDecimal getR40_total() {
			return r40_total;
		}

		public void setR40_total(BigDecimal r40_total) {
			this.r40_total = r40_total;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_current() {
			return r41_current;
		}

		public void setR41_current(BigDecimal r41_current) {
			this.r41_current = r41_current;
		}

		public BigDecimal getR41_call() {
			return r41_call;
		}

		public void setR41_call(BigDecimal r41_call) {
			this.r41_call = r41_call;
		}

		public BigDecimal getR41_savings() {
			return r41_savings;
		}

		public void setR41_savings(BigDecimal r41_savings) {
			this.r41_savings = r41_savings;
		}

		public BigDecimal getR41_0_31_notice_days() {
			return r41_0_31_notice_days;
		}

		public void setR41_0_31_notice_days(BigDecimal r41_0_31_notice_days) {
			this.r41_0_31_notice_days = r41_0_31_notice_days;
		}

		public BigDecimal getR41_32_88_notice_days() {
			return r41_32_88_notice_days;
		}

		public void setR41_32_88_notice_days(BigDecimal r41_32_88_notice_days) {
			this.r41_32_88_notice_days = r41_32_88_notice_days;
		}

		public BigDecimal getR41_91_day_deposit_fixed_deposit_months() {
			return r41_91_day_deposit_fixed_deposit_months;
		}

		public void setR41_91_day_deposit_fixed_deposit_months(BigDecimal r41_91_day_deposit_fixed_deposit_months) {
			this.r41_91_day_deposit_fixed_deposit_months = r41_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR41_1_2_fixed_deposits_months() {
			return r41_1_2_fixed_deposits_months;
		}

		public void setR41_1_2_fixed_deposits_months(BigDecimal r41_1_2_fixed_deposits_months) {
			this.r41_1_2_fixed_deposits_months = r41_1_2_fixed_deposits_months;
		}

		public BigDecimal getR41_4_6_fixed_deposits_months() {
			return r41_4_6_fixed_deposits_months;
		}

		public void setR41_4_6_fixed_deposits_months(BigDecimal r41_4_6_fixed_deposits_months) {
			this.r41_4_6_fixed_deposits_months = r41_4_6_fixed_deposits_months;
		}

		public BigDecimal getR41_7_12_fixed_deposits_months() {
			return r41_7_12_fixed_deposits_months;
		}

		public void setR41_7_12_fixed_deposits_months(BigDecimal r41_7_12_fixed_deposits_months) {
			this.r41_7_12_fixed_deposits_months = r41_7_12_fixed_deposits_months;
		}

		public BigDecimal getR41_13_18_fixed_deposits_months() {
			return r41_13_18_fixed_deposits_months;
		}

		public void setR41_13_18_fixed_deposits_months(BigDecimal r41_13_18_fixed_deposits_months) {
			this.r41_13_18_fixed_deposits_months = r41_13_18_fixed_deposits_months;
		}

		public BigDecimal getR41_19_24_fixed_deposits_months() {
			return r41_19_24_fixed_deposits_months;
		}

		public void setR41_19_24_fixed_deposits_months(BigDecimal r41_19_24_fixed_deposits_months) {
			this.r41_19_24_fixed_deposits_months = r41_19_24_fixed_deposits_months;
		}

		public BigDecimal getR41_over_24_fixed_deposits_months() {
			return r41_over_24_fixed_deposits_months;
		}

		public void setR41_over_24_fixed_deposits_months(BigDecimal r41_over_24_fixed_deposits_months) {
			this.r41_over_24_fixed_deposits_months = r41_over_24_fixed_deposits_months;
		}

		public BigDecimal getR41_certificates_of_deposit() {
			return r41_certificates_of_deposit;
		}

		public void setR41_certificates_of_deposit(BigDecimal r41_certificates_of_deposit) {
			this.r41_certificates_of_deposit = r41_certificates_of_deposit;
		}

		public BigDecimal getR41_total() {
			return r41_total;
		}

		public void setR41_total(BigDecimal r41_total) {
			this.r41_total = r41_total;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_current() {
			return r42_current;
		}

		public void setR42_current(BigDecimal r42_current) {
			this.r42_current = r42_current;
		}

		public BigDecimal getR42_call() {
			return r42_call;
		}

		public void setR42_call(BigDecimal r42_call) {
			this.r42_call = r42_call;
		}

		public BigDecimal getR42_savings() {
			return r42_savings;
		}

		public void setR42_savings(BigDecimal r42_savings) {
			this.r42_savings = r42_savings;
		}

		public BigDecimal getR42_0_31_notice_days() {
			return r42_0_31_notice_days;
		}

		public void setR42_0_31_notice_days(BigDecimal r42_0_31_notice_days) {
			this.r42_0_31_notice_days = r42_0_31_notice_days;
		}

		public BigDecimal getR42_32_88_notice_days() {
			return r42_32_88_notice_days;
		}

		public void setR42_32_88_notice_days(BigDecimal r42_32_88_notice_days) {
			this.r42_32_88_notice_days = r42_32_88_notice_days;
		}

		public BigDecimal getR42_91_day_deposit_fixed_deposit_months() {
			return r42_91_day_deposit_fixed_deposit_months;
		}

		public void setR42_91_day_deposit_fixed_deposit_months(BigDecimal r42_91_day_deposit_fixed_deposit_months) {
			this.r42_91_day_deposit_fixed_deposit_months = r42_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR42_1_2_fixed_deposits_months() {
			return r42_1_2_fixed_deposits_months;
		}

		public void setR42_1_2_fixed_deposits_months(BigDecimal r42_1_2_fixed_deposits_months) {
			this.r42_1_2_fixed_deposits_months = r42_1_2_fixed_deposits_months;
		}

		public BigDecimal getR42_4_6_fixed_deposits_months() {
			return r42_4_6_fixed_deposits_months;
		}

		public void setR42_4_6_fixed_deposits_months(BigDecimal r42_4_6_fixed_deposits_months) {
			this.r42_4_6_fixed_deposits_months = r42_4_6_fixed_deposits_months;
		}

		public BigDecimal getR42_7_12_fixed_deposits_months() {
			return r42_7_12_fixed_deposits_months;
		}

		public void setR42_7_12_fixed_deposits_months(BigDecimal r42_7_12_fixed_deposits_months) {
			this.r42_7_12_fixed_deposits_months = r42_7_12_fixed_deposits_months;
		}

		public BigDecimal getR42_13_18_fixed_deposits_months() {
			return r42_13_18_fixed_deposits_months;
		}

		public void setR42_13_18_fixed_deposits_months(BigDecimal r42_13_18_fixed_deposits_months) {
			this.r42_13_18_fixed_deposits_months = r42_13_18_fixed_deposits_months;
		}

		public BigDecimal getR42_19_24_fixed_deposits_months() {
			return r42_19_24_fixed_deposits_months;
		}

		public void setR42_19_24_fixed_deposits_months(BigDecimal r42_19_24_fixed_deposits_months) {
			this.r42_19_24_fixed_deposits_months = r42_19_24_fixed_deposits_months;
		}

		public BigDecimal getR42_over_24_fixed_deposits_months() {
			return r42_over_24_fixed_deposits_months;
		}

		public void setR42_over_24_fixed_deposits_months(BigDecimal r42_over_24_fixed_deposits_months) {
			this.r42_over_24_fixed_deposits_months = r42_over_24_fixed_deposits_months;
		}

		public BigDecimal getR42_certificates_of_deposit() {
			return r42_certificates_of_deposit;
		}

		public void setR42_certificates_of_deposit(BigDecimal r42_certificates_of_deposit) {
			this.r42_certificates_of_deposit = r42_certificates_of_deposit;
		}

		public BigDecimal getR42_total() {
			return r42_total;
		}

		public void setR42_total(BigDecimal r42_total) {
			this.r42_total = r42_total;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_current() {
			return r43_current;
		}

		public void setR43_current(BigDecimal r43_current) {
			this.r43_current = r43_current;
		}

		public BigDecimal getR43_call() {
			return r43_call;
		}

		public void setR43_call(BigDecimal r43_call) {
			this.r43_call = r43_call;
		}

		public BigDecimal getR43_savings() {
			return r43_savings;
		}

		public void setR43_savings(BigDecimal r43_savings) {
			this.r43_savings = r43_savings;
		}

		public BigDecimal getR43_0_31_notice_days() {
			return r43_0_31_notice_days;
		}

		public void setR43_0_31_notice_days(BigDecimal r43_0_31_notice_days) {
			this.r43_0_31_notice_days = r43_0_31_notice_days;
		}

		public BigDecimal getR43_32_88_notice_days() {
			return r43_32_88_notice_days;
		}

		public void setR43_32_88_notice_days(BigDecimal r43_32_88_notice_days) {
			this.r43_32_88_notice_days = r43_32_88_notice_days;
		}

		public BigDecimal getR43_91_day_deposit_fixed_deposit_months() {
			return r43_91_day_deposit_fixed_deposit_months;
		}

		public void setR43_91_day_deposit_fixed_deposit_months(BigDecimal r43_91_day_deposit_fixed_deposit_months) {
			this.r43_91_day_deposit_fixed_deposit_months = r43_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR43_1_2_fixed_deposits_months() {
			return r43_1_2_fixed_deposits_months;
		}

		public void setR43_1_2_fixed_deposits_months(BigDecimal r43_1_2_fixed_deposits_months) {
			this.r43_1_2_fixed_deposits_months = r43_1_2_fixed_deposits_months;
		}

		public BigDecimal getR43_4_6_fixed_deposits_months() {
			return r43_4_6_fixed_deposits_months;
		}

		public void setR43_4_6_fixed_deposits_months(BigDecimal r43_4_6_fixed_deposits_months) {
			this.r43_4_6_fixed_deposits_months = r43_4_6_fixed_deposits_months;
		}

		public BigDecimal getR43_7_12_fixed_deposits_months() {
			return r43_7_12_fixed_deposits_months;
		}

		public void setR43_7_12_fixed_deposits_months(BigDecimal r43_7_12_fixed_deposits_months) {
			this.r43_7_12_fixed_deposits_months = r43_7_12_fixed_deposits_months;
		}

		public BigDecimal getR43_13_18_fixed_deposits_months() {
			return r43_13_18_fixed_deposits_months;
		}

		public void setR43_13_18_fixed_deposits_months(BigDecimal r43_13_18_fixed_deposits_months) {
			this.r43_13_18_fixed_deposits_months = r43_13_18_fixed_deposits_months;
		}

		public BigDecimal getR43_19_24_fixed_deposits_months() {
			return r43_19_24_fixed_deposits_months;
		}

		public void setR43_19_24_fixed_deposits_months(BigDecimal r43_19_24_fixed_deposits_months) {
			this.r43_19_24_fixed_deposits_months = r43_19_24_fixed_deposits_months;
		}

		public BigDecimal getR43_over_24_fixed_deposits_months() {
			return r43_over_24_fixed_deposits_months;
		}

		public void setR43_over_24_fixed_deposits_months(BigDecimal r43_over_24_fixed_deposits_months) {
			this.r43_over_24_fixed_deposits_months = r43_over_24_fixed_deposits_months;
		}

		public BigDecimal getR43_certificates_of_deposit() {
			return r43_certificates_of_deposit;
		}

		public void setR43_certificates_of_deposit(BigDecimal r43_certificates_of_deposit) {
			this.r43_certificates_of_deposit = r43_certificates_of_deposit;
		}

		public BigDecimal getR43_total() {
			return r43_total;
		}

		public void setR43_total(BigDecimal r43_total) {
			this.r43_total = r43_total;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_current() {
			return r44_current;
		}

		public void setR44_current(BigDecimal r44_current) {
			this.r44_current = r44_current;
		}

		public BigDecimal getR44_call() {
			return r44_call;
		}

		public void setR44_call(BigDecimal r44_call) {
			this.r44_call = r44_call;
		}

		public BigDecimal getR44_savings() {
			return r44_savings;
		}

		public void setR44_savings(BigDecimal r44_savings) {
			this.r44_savings = r44_savings;
		}

		public BigDecimal getR44_0_31_notice_days() {
			return r44_0_31_notice_days;
		}

		public void setR44_0_31_notice_days(BigDecimal r44_0_31_notice_days) {
			this.r44_0_31_notice_days = r44_0_31_notice_days;
		}

		public BigDecimal getR44_32_88_notice_days() {
			return r44_32_88_notice_days;
		}

		public void setR44_32_88_notice_days(BigDecimal r44_32_88_notice_days) {
			this.r44_32_88_notice_days = r44_32_88_notice_days;
		}

		public BigDecimal getR44_91_day_deposit_fixed_deposit_months() {
			return r44_91_day_deposit_fixed_deposit_months;
		}

		public void setR44_91_day_deposit_fixed_deposit_months(BigDecimal r44_91_day_deposit_fixed_deposit_months) {
			this.r44_91_day_deposit_fixed_deposit_months = r44_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR44_1_2_fixed_deposits_months() {
			return r44_1_2_fixed_deposits_months;
		}

		public void setR44_1_2_fixed_deposits_months(BigDecimal r44_1_2_fixed_deposits_months) {
			this.r44_1_2_fixed_deposits_months = r44_1_2_fixed_deposits_months;
		}

		public BigDecimal getR44_4_6_fixed_deposits_months() {
			return r44_4_6_fixed_deposits_months;
		}

		public void setR44_4_6_fixed_deposits_months(BigDecimal r44_4_6_fixed_deposits_months) {
			this.r44_4_6_fixed_deposits_months = r44_4_6_fixed_deposits_months;
		}

		public BigDecimal getR44_7_12_fixed_deposits_months() {
			return r44_7_12_fixed_deposits_months;
		}

		public void setR44_7_12_fixed_deposits_months(BigDecimal r44_7_12_fixed_deposits_months) {
			this.r44_7_12_fixed_deposits_months = r44_7_12_fixed_deposits_months;
		}

		public BigDecimal getR44_13_18_fixed_deposits_months() {
			return r44_13_18_fixed_deposits_months;
		}

		public void setR44_13_18_fixed_deposits_months(BigDecimal r44_13_18_fixed_deposits_months) {
			this.r44_13_18_fixed_deposits_months = r44_13_18_fixed_deposits_months;
		}

		public BigDecimal getR44_19_24_fixed_deposits_months() {
			return r44_19_24_fixed_deposits_months;
		}

		public void setR44_19_24_fixed_deposits_months(BigDecimal r44_19_24_fixed_deposits_months) {
			this.r44_19_24_fixed_deposits_months = r44_19_24_fixed_deposits_months;
		}

		public BigDecimal getR44_over_24_fixed_deposits_months() {
			return r44_over_24_fixed_deposits_months;
		}

		public void setR44_over_24_fixed_deposits_months(BigDecimal r44_over_24_fixed_deposits_months) {
			this.r44_over_24_fixed_deposits_months = r44_over_24_fixed_deposits_months;
		}

		public BigDecimal getR44_certificates_of_deposit() {
			return r44_certificates_of_deposit;
		}

		public void setR44_certificates_of_deposit(BigDecimal r44_certificates_of_deposit) {
			this.r44_certificates_of_deposit = r44_certificates_of_deposit;
		}

		public BigDecimal getR44_total() {
			return r44_total;
		}

		public void setR44_total(BigDecimal r44_total) {
			this.r44_total = r44_total;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_current() {
			return r45_current;
		}

		public void setR45_current(BigDecimal r45_current) {
			this.r45_current = r45_current;
		}

		public BigDecimal getR45_call() {
			return r45_call;
		}

		public void setR45_call(BigDecimal r45_call) {
			this.r45_call = r45_call;
		}

		public BigDecimal getR45_savings() {
			return r45_savings;
		}

		public void setR45_savings(BigDecimal r45_savings) {
			this.r45_savings = r45_savings;
		}

		public BigDecimal getR45_0_31_notice_days() {
			return r45_0_31_notice_days;
		}

		public void setR45_0_31_notice_days(BigDecimal r45_0_31_notice_days) {
			this.r45_0_31_notice_days = r45_0_31_notice_days;
		}

		public BigDecimal getR45_32_88_notice_days() {
			return r45_32_88_notice_days;
		}

		public void setR45_32_88_notice_days(BigDecimal r45_32_88_notice_days) {
			this.r45_32_88_notice_days = r45_32_88_notice_days;
		}

		public BigDecimal getR45_91_day_deposit_fixed_deposit_months() {
			return r45_91_day_deposit_fixed_deposit_months;
		}

		public void setR45_91_day_deposit_fixed_deposit_months(BigDecimal r45_91_day_deposit_fixed_deposit_months) {
			this.r45_91_day_deposit_fixed_deposit_months = r45_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR45_1_2_fixed_deposits_months() {
			return r45_1_2_fixed_deposits_months;
		}

		public void setR45_1_2_fixed_deposits_months(BigDecimal r45_1_2_fixed_deposits_months) {
			this.r45_1_2_fixed_deposits_months = r45_1_2_fixed_deposits_months;
		}

		public BigDecimal getR45_4_6_fixed_deposits_months() {
			return r45_4_6_fixed_deposits_months;
		}

		public void setR45_4_6_fixed_deposits_months(BigDecimal r45_4_6_fixed_deposits_months) {
			this.r45_4_6_fixed_deposits_months = r45_4_6_fixed_deposits_months;
		}

		public BigDecimal getR45_7_12_fixed_deposits_months() {
			return r45_7_12_fixed_deposits_months;
		}

		public void setR45_7_12_fixed_deposits_months(BigDecimal r45_7_12_fixed_deposits_months) {
			this.r45_7_12_fixed_deposits_months = r45_7_12_fixed_deposits_months;
		}

		public BigDecimal getR45_13_18_fixed_deposits_months() {
			return r45_13_18_fixed_deposits_months;
		}

		public void setR45_13_18_fixed_deposits_months(BigDecimal r45_13_18_fixed_deposits_months) {
			this.r45_13_18_fixed_deposits_months = r45_13_18_fixed_deposits_months;
		}

		public BigDecimal getR45_19_24_fixed_deposits_months() {
			return r45_19_24_fixed_deposits_months;
		}

		public void setR45_19_24_fixed_deposits_months(BigDecimal r45_19_24_fixed_deposits_months) {
			this.r45_19_24_fixed_deposits_months = r45_19_24_fixed_deposits_months;
		}

		public BigDecimal getR45_over_24_fixed_deposits_months() {
			return r45_over_24_fixed_deposits_months;
		}

		public void setR45_over_24_fixed_deposits_months(BigDecimal r45_over_24_fixed_deposits_months) {
			this.r45_over_24_fixed_deposits_months = r45_over_24_fixed_deposits_months;
		}

		public BigDecimal getR45_certificates_of_deposit() {
			return r45_certificates_of_deposit;
		}

		public void setR45_certificates_of_deposit(BigDecimal r45_certificates_of_deposit) {
			this.r45_certificates_of_deposit = r45_certificates_of_deposit;
		}

		public BigDecimal getR45_total() {
			return r45_total;
		}

		public void setR45_total(BigDecimal r45_total) {
			this.r45_total = r45_total;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_current() {
			return r46_current;
		}

		public void setR46_current(BigDecimal r46_current) {
			this.r46_current = r46_current;
		}

		public BigDecimal getR46_call() {
			return r46_call;
		}

		public void setR46_call(BigDecimal r46_call) {
			this.r46_call = r46_call;
		}

		public BigDecimal getR46_savings() {
			return r46_savings;
		}

		public void setR46_savings(BigDecimal r46_savings) {
			this.r46_savings = r46_savings;
		}

		public BigDecimal getR46_0_31_notice_days() {
			return r46_0_31_notice_days;
		}

		public void setR46_0_31_notice_days(BigDecimal r46_0_31_notice_days) {
			this.r46_0_31_notice_days = r46_0_31_notice_days;
		}

		public BigDecimal getR46_32_88_notice_days() {
			return r46_32_88_notice_days;
		}

		public void setR46_32_88_notice_days(BigDecimal r46_32_88_notice_days) {
			this.r46_32_88_notice_days = r46_32_88_notice_days;
		}

		public BigDecimal getR46_91_day_deposit_fixed_deposit_months() {
			return r46_91_day_deposit_fixed_deposit_months;
		}

		public void setR46_91_day_deposit_fixed_deposit_months(BigDecimal r46_91_day_deposit_fixed_deposit_months) {
			this.r46_91_day_deposit_fixed_deposit_months = r46_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR46_1_2_fixed_deposits_months() {
			return r46_1_2_fixed_deposits_months;
		}

		public void setR46_1_2_fixed_deposits_months(BigDecimal r46_1_2_fixed_deposits_months) {
			this.r46_1_2_fixed_deposits_months = r46_1_2_fixed_deposits_months;
		}

		public BigDecimal getR46_4_6_fixed_deposits_months() {
			return r46_4_6_fixed_deposits_months;
		}

		public void setR46_4_6_fixed_deposits_months(BigDecimal r46_4_6_fixed_deposits_months) {
			this.r46_4_6_fixed_deposits_months = r46_4_6_fixed_deposits_months;
		}

		public BigDecimal getR46_7_12_fixed_deposits_months() {
			return r46_7_12_fixed_deposits_months;
		}

		public void setR46_7_12_fixed_deposits_months(BigDecimal r46_7_12_fixed_deposits_months) {
			this.r46_7_12_fixed_deposits_months = r46_7_12_fixed_deposits_months;
		}

		public BigDecimal getR46_13_18_fixed_deposits_months() {
			return r46_13_18_fixed_deposits_months;
		}

		public void setR46_13_18_fixed_deposits_months(BigDecimal r46_13_18_fixed_deposits_months) {
			this.r46_13_18_fixed_deposits_months = r46_13_18_fixed_deposits_months;
		}

		public BigDecimal getR46_19_24_fixed_deposits_months() {
			return r46_19_24_fixed_deposits_months;
		}

		public void setR46_19_24_fixed_deposits_months(BigDecimal r46_19_24_fixed_deposits_months) {
			this.r46_19_24_fixed_deposits_months = r46_19_24_fixed_deposits_months;
		}

		public BigDecimal getR46_over_24_fixed_deposits_months() {
			return r46_over_24_fixed_deposits_months;
		}

		public void setR46_over_24_fixed_deposits_months(BigDecimal r46_over_24_fixed_deposits_months) {
			this.r46_over_24_fixed_deposits_months = r46_over_24_fixed_deposits_months;
		}

		public BigDecimal getR46_certificates_of_deposit() {
			return r46_certificates_of_deposit;
		}

		public void setR46_certificates_of_deposit(BigDecimal r46_certificates_of_deposit) {
			this.r46_certificates_of_deposit = r46_certificates_of_deposit;
		}

		public BigDecimal getR46_total() {
			return r46_total;
		}

		public void setR46_total(BigDecimal r46_total) {
			this.r46_total = r46_total;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_current() {
			return r47_current;
		}

		public void setR47_current(BigDecimal r47_current) {
			this.r47_current = r47_current;
		}

		public BigDecimal getR47_call() {
			return r47_call;
		}

		public void setR47_call(BigDecimal r47_call) {
			this.r47_call = r47_call;
		}

		public BigDecimal getR47_savings() {
			return r47_savings;
		}

		public void setR47_savings(BigDecimal r47_savings) {
			this.r47_savings = r47_savings;
		}

		public BigDecimal getR47_0_31_notice_days() {
			return r47_0_31_notice_days;
		}

		public void setR47_0_31_notice_days(BigDecimal r47_0_31_notice_days) {
			this.r47_0_31_notice_days = r47_0_31_notice_days;
		}

		public BigDecimal getR47_32_88_notice_days() {
			return r47_32_88_notice_days;
		}

		public void setR47_32_88_notice_days(BigDecimal r47_32_88_notice_days) {
			this.r47_32_88_notice_days = r47_32_88_notice_days;
		}

		public BigDecimal getR47_91_day_deposit_fixed_deposit_months() {
			return r47_91_day_deposit_fixed_deposit_months;
		}

		public void setR47_91_day_deposit_fixed_deposit_months(BigDecimal r47_91_day_deposit_fixed_deposit_months) {
			this.r47_91_day_deposit_fixed_deposit_months = r47_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR47_1_2_fixed_deposits_months() {
			return r47_1_2_fixed_deposits_months;
		}

		public void setR47_1_2_fixed_deposits_months(BigDecimal r47_1_2_fixed_deposits_months) {
			this.r47_1_2_fixed_deposits_months = r47_1_2_fixed_deposits_months;
		}

		public BigDecimal getR47_4_6_fixed_deposits_months() {
			return r47_4_6_fixed_deposits_months;
		}

		public void setR47_4_6_fixed_deposits_months(BigDecimal r47_4_6_fixed_deposits_months) {
			this.r47_4_6_fixed_deposits_months = r47_4_6_fixed_deposits_months;
		}

		public BigDecimal getR47_7_12_fixed_deposits_months() {
			return r47_7_12_fixed_deposits_months;
		}

		public void setR47_7_12_fixed_deposits_months(BigDecimal r47_7_12_fixed_deposits_months) {
			this.r47_7_12_fixed_deposits_months = r47_7_12_fixed_deposits_months;
		}

		public BigDecimal getR47_13_18_fixed_deposits_months() {
			return r47_13_18_fixed_deposits_months;
		}

		public void setR47_13_18_fixed_deposits_months(BigDecimal r47_13_18_fixed_deposits_months) {
			this.r47_13_18_fixed_deposits_months = r47_13_18_fixed_deposits_months;
		}

		public BigDecimal getR47_19_24_fixed_deposits_months() {
			return r47_19_24_fixed_deposits_months;
		}

		public void setR47_19_24_fixed_deposits_months(BigDecimal r47_19_24_fixed_deposits_months) {
			this.r47_19_24_fixed_deposits_months = r47_19_24_fixed_deposits_months;
		}

		public BigDecimal getR47_over_24_fixed_deposits_months() {
			return r47_over_24_fixed_deposits_months;
		}

		public void setR47_over_24_fixed_deposits_months(BigDecimal r47_over_24_fixed_deposits_months) {
			this.r47_over_24_fixed_deposits_months = r47_over_24_fixed_deposits_months;
		}

		public BigDecimal getR47_certificates_of_deposit() {
			return r47_certificates_of_deposit;
		}

		public void setR47_certificates_of_deposit(BigDecimal r47_certificates_of_deposit) {
			this.r47_certificates_of_deposit = r47_certificates_of_deposit;
		}

		public BigDecimal getR47_total() {
			return r47_total;
		}

		public void setR47_total(BigDecimal r47_total) {
			this.r47_total = r47_total;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_current() {
			return r48_current;
		}

		public void setR48_current(BigDecimal r48_current) {
			this.r48_current = r48_current;
		}

		public BigDecimal getR48_call() {
			return r48_call;
		}

		public void setR48_call(BigDecimal r48_call) {
			this.r48_call = r48_call;
		}

		public BigDecimal getR48_savings() {
			return r48_savings;
		}

		public void setR48_savings(BigDecimal r48_savings) {
			this.r48_savings = r48_savings;
		}

		public BigDecimal getR48_0_31_notice_days() {
			return r48_0_31_notice_days;
		}

		public void setR48_0_31_notice_days(BigDecimal r48_0_31_notice_days) {
			this.r48_0_31_notice_days = r48_0_31_notice_days;
		}

		public BigDecimal getR48_32_88_notice_days() {
			return r48_32_88_notice_days;
		}

		public void setR48_32_88_notice_days(BigDecimal r48_32_88_notice_days) {
			this.r48_32_88_notice_days = r48_32_88_notice_days;
		}

		public BigDecimal getR48_91_day_deposit_fixed_deposit_months() {
			return r48_91_day_deposit_fixed_deposit_months;
		}

		public void setR48_91_day_deposit_fixed_deposit_months(BigDecimal r48_91_day_deposit_fixed_deposit_months) {
			this.r48_91_day_deposit_fixed_deposit_months = r48_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR48_1_2_fixed_deposits_months() {
			return r48_1_2_fixed_deposits_months;
		}

		public void setR48_1_2_fixed_deposits_months(BigDecimal r48_1_2_fixed_deposits_months) {
			this.r48_1_2_fixed_deposits_months = r48_1_2_fixed_deposits_months;
		}

		public BigDecimal getR48_4_6_fixed_deposits_months() {
			return r48_4_6_fixed_deposits_months;
		}

		public void setR48_4_6_fixed_deposits_months(BigDecimal r48_4_6_fixed_deposits_months) {
			this.r48_4_6_fixed_deposits_months = r48_4_6_fixed_deposits_months;
		}

		public BigDecimal getR48_7_12_fixed_deposits_months() {
			return r48_7_12_fixed_deposits_months;
		}

		public void setR48_7_12_fixed_deposits_months(BigDecimal r48_7_12_fixed_deposits_months) {
			this.r48_7_12_fixed_deposits_months = r48_7_12_fixed_deposits_months;
		}

		public BigDecimal getR48_13_18_fixed_deposits_months() {
			return r48_13_18_fixed_deposits_months;
		}

		public void setR48_13_18_fixed_deposits_months(BigDecimal r48_13_18_fixed_deposits_months) {
			this.r48_13_18_fixed_deposits_months = r48_13_18_fixed_deposits_months;
		}

		public BigDecimal getR48_19_24_fixed_deposits_months() {
			return r48_19_24_fixed_deposits_months;
		}

		public void setR48_19_24_fixed_deposits_months(BigDecimal r48_19_24_fixed_deposits_months) {
			this.r48_19_24_fixed_deposits_months = r48_19_24_fixed_deposits_months;
		}

		public BigDecimal getR48_over_24_fixed_deposits_months() {
			return r48_over_24_fixed_deposits_months;
		}

		public void setR48_over_24_fixed_deposits_months(BigDecimal r48_over_24_fixed_deposits_months) {
			this.r48_over_24_fixed_deposits_months = r48_over_24_fixed_deposits_months;
		}

		public BigDecimal getR48_certificates_of_deposit() {
			return r48_certificates_of_deposit;
		}

		public void setR48_certificates_of_deposit(BigDecimal r48_certificates_of_deposit) {
			this.r48_certificates_of_deposit = r48_certificates_of_deposit;
		}

		public BigDecimal getR48_total() {
			return r48_total;
		}

		public void setR48_total(BigDecimal r48_total) {
			this.r48_total = r48_total;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_current() {
			return r49_current;
		}

		public void setR49_current(BigDecimal r49_current) {
			this.r49_current = r49_current;
		}

		public BigDecimal getR49_call() {
			return r49_call;
		}

		public void setR49_call(BigDecimal r49_call) {
			this.r49_call = r49_call;
		}

		public BigDecimal getR49_savings() {
			return r49_savings;
		}

		public void setR49_savings(BigDecimal r49_savings) {
			this.r49_savings = r49_savings;
		}

		public BigDecimal getR49_0_31_notice_days() {
			return r49_0_31_notice_days;
		}

		public void setR49_0_31_notice_days(BigDecimal r49_0_31_notice_days) {
			this.r49_0_31_notice_days = r49_0_31_notice_days;
		}

		public BigDecimal getR49_32_88_notice_days() {
			return r49_32_88_notice_days;
		}

		public void setR49_32_88_notice_days(BigDecimal r49_32_88_notice_days) {
			this.r49_32_88_notice_days = r49_32_88_notice_days;
		}

		public BigDecimal getR49_91_day_deposit_fixed_deposit_months() {
			return r49_91_day_deposit_fixed_deposit_months;
		}

		public void setR49_91_day_deposit_fixed_deposit_months(BigDecimal r49_91_day_deposit_fixed_deposit_months) {
			this.r49_91_day_deposit_fixed_deposit_months = r49_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR49_1_2_fixed_deposits_months() {
			return r49_1_2_fixed_deposits_months;
		}

		public void setR49_1_2_fixed_deposits_months(BigDecimal r49_1_2_fixed_deposits_months) {
			this.r49_1_2_fixed_deposits_months = r49_1_2_fixed_deposits_months;
		}

		public BigDecimal getR49_4_6_fixed_deposits_months() {
			return r49_4_6_fixed_deposits_months;
		}

		public void setR49_4_6_fixed_deposits_months(BigDecimal r49_4_6_fixed_deposits_months) {
			this.r49_4_6_fixed_deposits_months = r49_4_6_fixed_deposits_months;
		}

		public BigDecimal getR49_7_12_fixed_deposits_months() {
			return r49_7_12_fixed_deposits_months;
		}

		public void setR49_7_12_fixed_deposits_months(BigDecimal r49_7_12_fixed_deposits_months) {
			this.r49_7_12_fixed_deposits_months = r49_7_12_fixed_deposits_months;
		}

		public BigDecimal getR49_13_18_fixed_deposits_months() {
			return r49_13_18_fixed_deposits_months;
		}

		public void setR49_13_18_fixed_deposits_months(BigDecimal r49_13_18_fixed_deposits_months) {
			this.r49_13_18_fixed_deposits_months = r49_13_18_fixed_deposits_months;
		}

		public BigDecimal getR49_19_24_fixed_deposits_months() {
			return r49_19_24_fixed_deposits_months;
		}

		public void setR49_19_24_fixed_deposits_months(BigDecimal r49_19_24_fixed_deposits_months) {
			this.r49_19_24_fixed_deposits_months = r49_19_24_fixed_deposits_months;
		}

		public BigDecimal getR49_over_24_fixed_deposits_months() {
			return r49_over_24_fixed_deposits_months;
		}

		public void setR49_over_24_fixed_deposits_months(BigDecimal r49_over_24_fixed_deposits_months) {
			this.r49_over_24_fixed_deposits_months = r49_over_24_fixed_deposits_months;
		}

		public BigDecimal getR49_certificates_of_deposit() {
			return r49_certificates_of_deposit;
		}

		public void setR49_certificates_of_deposit(BigDecimal r49_certificates_of_deposit) {
			this.r49_certificates_of_deposit = r49_certificates_of_deposit;
		}

		public BigDecimal getR49_total() {
			return r49_total;
		}

		public void setR49_total(BigDecimal r49_total) {
			this.r49_total = r49_total;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_current() {
			return r50_current;
		}

		public void setR50_current(BigDecimal r50_current) {
			this.r50_current = r50_current;
		}

		public BigDecimal getR50_call() {
			return r50_call;
		}

		public void setR50_call(BigDecimal r50_call) {
			this.r50_call = r50_call;
		}

		public BigDecimal getR50_savings() {
			return r50_savings;
		}

		public void setR50_savings(BigDecimal r50_savings) {
			this.r50_savings = r50_savings;
		}

		public BigDecimal getR50_0_31_notice_days() {
			return r50_0_31_notice_days;
		}

		public void setR50_0_31_notice_days(BigDecimal r50_0_31_notice_days) {
			this.r50_0_31_notice_days = r50_0_31_notice_days;
		}

		public BigDecimal getR50_32_88_notice_days() {
			return r50_32_88_notice_days;
		}

		public void setR50_32_88_notice_days(BigDecimal r50_32_88_notice_days) {
			this.r50_32_88_notice_days = r50_32_88_notice_days;
		}

		public BigDecimal getR50_91_day_deposit_fixed_deposit_months() {
			return r50_91_day_deposit_fixed_deposit_months;
		}

		public void setR50_91_day_deposit_fixed_deposit_months(BigDecimal r50_91_day_deposit_fixed_deposit_months) {
			this.r50_91_day_deposit_fixed_deposit_months = r50_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR50_1_2_fixed_deposits_months() {
			return r50_1_2_fixed_deposits_months;
		}

		public void setR50_1_2_fixed_deposits_months(BigDecimal r50_1_2_fixed_deposits_months) {
			this.r50_1_2_fixed_deposits_months = r50_1_2_fixed_deposits_months;
		}

		public BigDecimal getR50_4_6_fixed_deposits_months() {
			return r50_4_6_fixed_deposits_months;
		}

		public void setR50_4_6_fixed_deposits_months(BigDecimal r50_4_6_fixed_deposits_months) {
			this.r50_4_6_fixed_deposits_months = r50_4_6_fixed_deposits_months;
		}

		public BigDecimal getR50_7_12_fixed_deposits_months() {
			return r50_7_12_fixed_deposits_months;
		}

		public void setR50_7_12_fixed_deposits_months(BigDecimal r50_7_12_fixed_deposits_months) {
			this.r50_7_12_fixed_deposits_months = r50_7_12_fixed_deposits_months;
		}

		public BigDecimal getR50_13_18_fixed_deposits_months() {
			return r50_13_18_fixed_deposits_months;
		}

		public void setR50_13_18_fixed_deposits_months(BigDecimal r50_13_18_fixed_deposits_months) {
			this.r50_13_18_fixed_deposits_months = r50_13_18_fixed_deposits_months;
		}

		public BigDecimal getR50_19_24_fixed_deposits_months() {
			return r50_19_24_fixed_deposits_months;
		}

		public void setR50_19_24_fixed_deposits_months(BigDecimal r50_19_24_fixed_deposits_months) {
			this.r50_19_24_fixed_deposits_months = r50_19_24_fixed_deposits_months;
		}

		public BigDecimal getR50_over_24_fixed_deposits_months() {
			return r50_over_24_fixed_deposits_months;
		}

		public void setR50_over_24_fixed_deposits_months(BigDecimal r50_over_24_fixed_deposits_months) {
			this.r50_over_24_fixed_deposits_months = r50_over_24_fixed_deposits_months;
		}

		public BigDecimal getR50_certificates_of_deposit() {
			return r50_certificates_of_deposit;
		}

		public void setR50_certificates_of_deposit(BigDecimal r50_certificates_of_deposit) {
			this.r50_certificates_of_deposit = r50_certificates_of_deposit;
		}

		public BigDecimal getR50_total() {
			return r50_total;
		}

		public void setR50_total(BigDecimal r50_total) {
			this.r50_total = r50_total;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_current() {
			return r51_current;
		}

		public void setR51_current(BigDecimal r51_current) {
			this.r51_current = r51_current;
		}

		public BigDecimal getR51_call() {
			return r51_call;
		}

		public void setR51_call(BigDecimal r51_call) {
			this.r51_call = r51_call;
		}

		public BigDecimal getR51_savings() {
			return r51_savings;
		}

		public void setR51_savings(BigDecimal r51_savings) {
			this.r51_savings = r51_savings;
		}

		public BigDecimal getR51_0_31_notice_days() {
			return r51_0_31_notice_days;
		}

		public void setR51_0_31_notice_days(BigDecimal r51_0_31_notice_days) {
			this.r51_0_31_notice_days = r51_0_31_notice_days;
		}

		public BigDecimal getR51_32_88_notice_days() {
			return r51_32_88_notice_days;
		}

		public void setR51_32_88_notice_days(BigDecimal r51_32_88_notice_days) {
			this.r51_32_88_notice_days = r51_32_88_notice_days;
		}

		public BigDecimal getR51_91_day_deposit_fixed_deposit_months() {
			return r51_91_day_deposit_fixed_deposit_months;
		}

		public void setR51_91_day_deposit_fixed_deposit_months(BigDecimal r51_91_day_deposit_fixed_deposit_months) {
			this.r51_91_day_deposit_fixed_deposit_months = r51_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR51_1_2_fixed_deposits_months() {
			return r51_1_2_fixed_deposits_months;
		}

		public void setR51_1_2_fixed_deposits_months(BigDecimal r51_1_2_fixed_deposits_months) {
			this.r51_1_2_fixed_deposits_months = r51_1_2_fixed_deposits_months;
		}

		public BigDecimal getR51_4_6_fixed_deposits_months() {
			return r51_4_6_fixed_deposits_months;
		}

		public void setR51_4_6_fixed_deposits_months(BigDecimal r51_4_6_fixed_deposits_months) {
			this.r51_4_6_fixed_deposits_months = r51_4_6_fixed_deposits_months;
		}

		public BigDecimal getR51_7_12_fixed_deposits_months() {
			return r51_7_12_fixed_deposits_months;
		}

		public void setR51_7_12_fixed_deposits_months(BigDecimal r51_7_12_fixed_deposits_months) {
			this.r51_7_12_fixed_deposits_months = r51_7_12_fixed_deposits_months;
		}

		public BigDecimal getR51_13_18_fixed_deposits_months() {
			return r51_13_18_fixed_deposits_months;
		}

		public void setR51_13_18_fixed_deposits_months(BigDecimal r51_13_18_fixed_deposits_months) {
			this.r51_13_18_fixed_deposits_months = r51_13_18_fixed_deposits_months;
		}

		public BigDecimal getR51_19_24_fixed_deposits_months() {
			return r51_19_24_fixed_deposits_months;
		}

		public void setR51_19_24_fixed_deposits_months(BigDecimal r51_19_24_fixed_deposits_months) {
			this.r51_19_24_fixed_deposits_months = r51_19_24_fixed_deposits_months;
		}

		public BigDecimal getR51_over_24_fixed_deposits_months() {
			return r51_over_24_fixed_deposits_months;
		}

		public void setR51_over_24_fixed_deposits_months(BigDecimal r51_over_24_fixed_deposits_months) {
			this.r51_over_24_fixed_deposits_months = r51_over_24_fixed_deposits_months;
		}

		public BigDecimal getR51_certificates_of_deposit() {
			return r51_certificates_of_deposit;
		}

		public void setR51_certificates_of_deposit(BigDecimal r51_certificates_of_deposit) {
			this.r51_certificates_of_deposit = r51_certificates_of_deposit;
		}

		public BigDecimal getR51_total() {
			return r51_total;
		}

		public void setR51_total(BigDecimal r51_total) {
			this.r51_total = r51_total;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public BigDecimal getR52_current() {
			return r52_current;
		}

		public void setR52_current(BigDecimal r52_current) {
			this.r52_current = r52_current;
		}

		public BigDecimal getR52_call() {
			return r52_call;
		}

		public void setR52_call(BigDecimal r52_call) {
			this.r52_call = r52_call;
		}

		public BigDecimal getR52_savings() {
			return r52_savings;
		}

		public void setR52_savings(BigDecimal r52_savings) {
			this.r52_savings = r52_savings;
		}

		public BigDecimal getR52_0_31_notice_days() {
			return r52_0_31_notice_days;
		}

		public void setR52_0_31_notice_days(BigDecimal r52_0_31_notice_days) {
			this.r52_0_31_notice_days = r52_0_31_notice_days;
		}

		public BigDecimal getR52_32_88_notice_days() {
			return r52_32_88_notice_days;
		}

		public void setR52_32_88_notice_days(BigDecimal r52_32_88_notice_days) {
			this.r52_32_88_notice_days = r52_32_88_notice_days;
		}

		public BigDecimal getR52_91_day_deposit_fixed_deposit_months() {
			return r52_91_day_deposit_fixed_deposit_months;
		}

		public void setR52_91_day_deposit_fixed_deposit_months(BigDecimal r52_91_day_deposit_fixed_deposit_months) {
			this.r52_91_day_deposit_fixed_deposit_months = r52_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR52_1_2_fixed_deposits_months() {
			return r52_1_2_fixed_deposits_months;
		}

		public void setR52_1_2_fixed_deposits_months(BigDecimal r52_1_2_fixed_deposits_months) {
			this.r52_1_2_fixed_deposits_months = r52_1_2_fixed_deposits_months;
		}

		public BigDecimal getR52_4_6_fixed_deposits_months() {
			return r52_4_6_fixed_deposits_months;
		}

		public void setR52_4_6_fixed_deposits_months(BigDecimal r52_4_6_fixed_deposits_months) {
			this.r52_4_6_fixed_deposits_months = r52_4_6_fixed_deposits_months;
		}

		public BigDecimal getR52_7_12_fixed_deposits_months() {
			return r52_7_12_fixed_deposits_months;
		}

		public void setR52_7_12_fixed_deposits_months(BigDecimal r52_7_12_fixed_deposits_months) {
			this.r52_7_12_fixed_deposits_months = r52_7_12_fixed_deposits_months;
		}

		public BigDecimal getR52_13_18_fixed_deposits_months() {
			return r52_13_18_fixed_deposits_months;
		}

		public void setR52_13_18_fixed_deposits_months(BigDecimal r52_13_18_fixed_deposits_months) {
			this.r52_13_18_fixed_deposits_months = r52_13_18_fixed_deposits_months;
		}

		public BigDecimal getR52_19_24_fixed_deposits_months() {
			return r52_19_24_fixed_deposits_months;
		}

		public void setR52_19_24_fixed_deposits_months(BigDecimal r52_19_24_fixed_deposits_months) {
			this.r52_19_24_fixed_deposits_months = r52_19_24_fixed_deposits_months;
		}

		public BigDecimal getR52_over_24_fixed_deposits_months() {
			return r52_over_24_fixed_deposits_months;
		}

		public void setR52_over_24_fixed_deposits_months(BigDecimal r52_over_24_fixed_deposits_months) {
			this.r52_over_24_fixed_deposits_months = r52_over_24_fixed_deposits_months;
		}

		public BigDecimal getR52_certificates_of_deposit() {
			return r52_certificates_of_deposit;
		}

		public void setR52_certificates_of_deposit(BigDecimal r52_certificates_of_deposit) {
			this.r52_certificates_of_deposit = r52_certificates_of_deposit;
		}

		public BigDecimal getR52_total() {
			return r52_total;
		}

		public void setR52_total(BigDecimal r52_total) {
			this.r52_total = r52_total;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public BigDecimal getR53_current() {
			return r53_current;
		}

		public void setR53_current(BigDecimal r53_current) {
			this.r53_current = r53_current;
		}

		public BigDecimal getR53_call() {
			return r53_call;
		}

		public void setR53_call(BigDecimal r53_call) {
			this.r53_call = r53_call;
		}

		public BigDecimal getR53_savings() {
			return r53_savings;
		}

		public void setR53_savings(BigDecimal r53_savings) {
			this.r53_savings = r53_savings;
		}

		public BigDecimal getR53_0_31_notice_days() {
			return r53_0_31_notice_days;
		}

		public void setR53_0_31_notice_days(BigDecimal r53_0_31_notice_days) {
			this.r53_0_31_notice_days = r53_0_31_notice_days;
		}

		public BigDecimal getR53_32_88_notice_days() {
			return r53_32_88_notice_days;
		}

		public void setR53_32_88_notice_days(BigDecimal r53_32_88_notice_days) {
			this.r53_32_88_notice_days = r53_32_88_notice_days;
		}

		public BigDecimal getR53_91_day_deposit_fixed_deposit_months() {
			return r53_91_day_deposit_fixed_deposit_months;
		}

		public void setR53_91_day_deposit_fixed_deposit_months(BigDecimal r53_91_day_deposit_fixed_deposit_months) {
			this.r53_91_day_deposit_fixed_deposit_months = r53_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR53_1_2_fixed_deposits_months() {
			return r53_1_2_fixed_deposits_months;
		}

		public void setR53_1_2_fixed_deposits_months(BigDecimal r53_1_2_fixed_deposits_months) {
			this.r53_1_2_fixed_deposits_months = r53_1_2_fixed_deposits_months;
		}

		public BigDecimal getR53_4_6_fixed_deposits_months() {
			return r53_4_6_fixed_deposits_months;
		}

		public void setR53_4_6_fixed_deposits_months(BigDecimal r53_4_6_fixed_deposits_months) {
			this.r53_4_6_fixed_deposits_months = r53_4_6_fixed_deposits_months;
		}

		public BigDecimal getR53_7_12_fixed_deposits_months() {
			return r53_7_12_fixed_deposits_months;
		}

		public void setR53_7_12_fixed_deposits_months(BigDecimal r53_7_12_fixed_deposits_months) {
			this.r53_7_12_fixed_deposits_months = r53_7_12_fixed_deposits_months;
		}

		public BigDecimal getR53_13_18_fixed_deposits_months() {
			return r53_13_18_fixed_deposits_months;
		}

		public void setR53_13_18_fixed_deposits_months(BigDecimal r53_13_18_fixed_deposits_months) {
			this.r53_13_18_fixed_deposits_months = r53_13_18_fixed_deposits_months;
		}

		public BigDecimal getR53_19_24_fixed_deposits_months() {
			return r53_19_24_fixed_deposits_months;
		}

		public void setR53_19_24_fixed_deposits_months(BigDecimal r53_19_24_fixed_deposits_months) {
			this.r53_19_24_fixed_deposits_months = r53_19_24_fixed_deposits_months;
		}

		public BigDecimal getR53_over_24_fixed_deposits_months() {
			return r53_over_24_fixed_deposits_months;
		}

		public void setR53_over_24_fixed_deposits_months(BigDecimal r53_over_24_fixed_deposits_months) {
			this.r53_over_24_fixed_deposits_months = r53_over_24_fixed_deposits_months;
		}

		public BigDecimal getR53_certificates_of_deposit() {
			return r53_certificates_of_deposit;
		}

		public void setR53_certificates_of_deposit(BigDecimal r53_certificates_of_deposit) {
			this.r53_certificates_of_deposit = r53_certificates_of_deposit;
		}

		public BigDecimal getR53_total() {
			return r53_total;
		}

		public void setR53_total(BigDecimal r53_total) {
			this.r53_total = r53_total;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public BigDecimal getR54_current() {
			return r54_current;
		}

		public void setR54_current(BigDecimal r54_current) {
			this.r54_current = r54_current;
		}

		public BigDecimal getR54_call() {
			return r54_call;
		}

		public void setR54_call(BigDecimal r54_call) {
			this.r54_call = r54_call;
		}

		public BigDecimal getR54_savings() {
			return r54_savings;
		}

		public void setR54_savings(BigDecimal r54_savings) {
			this.r54_savings = r54_savings;
		}

		public BigDecimal getR54_0_31_notice_days() {
			return r54_0_31_notice_days;
		}

		public void setR54_0_31_notice_days(BigDecimal r54_0_31_notice_days) {
			this.r54_0_31_notice_days = r54_0_31_notice_days;
		}

		public BigDecimal getR54_32_88_notice_days() {
			return r54_32_88_notice_days;
		}

		public void setR54_32_88_notice_days(BigDecimal r54_32_88_notice_days) {
			this.r54_32_88_notice_days = r54_32_88_notice_days;
		}

		public BigDecimal getR54_91_day_deposit_fixed_deposit_months() {
			return r54_91_day_deposit_fixed_deposit_months;
		}

		public void setR54_91_day_deposit_fixed_deposit_months(BigDecimal r54_91_day_deposit_fixed_deposit_months) {
			this.r54_91_day_deposit_fixed_deposit_months = r54_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR54_1_2_fixed_deposits_months() {
			return r54_1_2_fixed_deposits_months;
		}

		public void setR54_1_2_fixed_deposits_months(BigDecimal r54_1_2_fixed_deposits_months) {
			this.r54_1_2_fixed_deposits_months = r54_1_2_fixed_deposits_months;
		}

		public BigDecimal getR54_4_6_fixed_deposits_months() {
			return r54_4_6_fixed_deposits_months;
		}

		public void setR54_4_6_fixed_deposits_months(BigDecimal r54_4_6_fixed_deposits_months) {
			this.r54_4_6_fixed_deposits_months = r54_4_6_fixed_deposits_months;
		}

		public BigDecimal getR54_7_12_fixed_deposits_months() {
			return r54_7_12_fixed_deposits_months;
		}

		public void setR54_7_12_fixed_deposits_months(BigDecimal r54_7_12_fixed_deposits_months) {
			this.r54_7_12_fixed_deposits_months = r54_7_12_fixed_deposits_months;
		}

		public BigDecimal getR54_13_18_fixed_deposits_months() {
			return r54_13_18_fixed_deposits_months;
		}

		public void setR54_13_18_fixed_deposits_months(BigDecimal r54_13_18_fixed_deposits_months) {
			this.r54_13_18_fixed_deposits_months = r54_13_18_fixed_deposits_months;
		}

		public BigDecimal getR54_19_24_fixed_deposits_months() {
			return r54_19_24_fixed_deposits_months;
		}

		public void setR54_19_24_fixed_deposits_months(BigDecimal r54_19_24_fixed_deposits_months) {
			this.r54_19_24_fixed_deposits_months = r54_19_24_fixed_deposits_months;
		}

		public BigDecimal getR54_over_24_fixed_deposits_months() {
			return r54_over_24_fixed_deposits_months;
		}

		public void setR54_over_24_fixed_deposits_months(BigDecimal r54_over_24_fixed_deposits_months) {
			this.r54_over_24_fixed_deposits_months = r54_over_24_fixed_deposits_months;
		}

		public BigDecimal getR54_certificates_of_deposit() {
			return r54_certificates_of_deposit;
		}

		public void setR54_certificates_of_deposit(BigDecimal r54_certificates_of_deposit) {
			this.r54_certificates_of_deposit = r54_certificates_of_deposit;
		}

		public BigDecimal getR54_total() {
			return r54_total;
		}

		public void setR54_total(BigDecimal r54_total) {
			this.r54_total = r54_total;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public BigDecimal getR55_current() {
			return r55_current;
		}

		public void setR55_current(BigDecimal r55_current) {
			this.r55_current = r55_current;
		}

		public BigDecimal getR55_call() {
			return r55_call;
		}

		public void setR55_call(BigDecimal r55_call) {
			this.r55_call = r55_call;
		}

		public BigDecimal getR55_savings() {
			return r55_savings;
		}

		public void setR55_savings(BigDecimal r55_savings) {
			this.r55_savings = r55_savings;
		}

		public BigDecimal getR55_0_31_notice_days() {
			return r55_0_31_notice_days;
		}

		public void setR55_0_31_notice_days(BigDecimal r55_0_31_notice_days) {
			this.r55_0_31_notice_days = r55_0_31_notice_days;
		}

		public BigDecimal getR55_32_88_notice_days() {
			return r55_32_88_notice_days;
		}

		public void setR55_32_88_notice_days(BigDecimal r55_32_88_notice_days) {
			this.r55_32_88_notice_days = r55_32_88_notice_days;
		}

		public BigDecimal getR55_91_day_deposit_fixed_deposit_months() {
			return r55_91_day_deposit_fixed_deposit_months;
		}

		public void setR55_91_day_deposit_fixed_deposit_months(BigDecimal r55_91_day_deposit_fixed_deposit_months) {
			this.r55_91_day_deposit_fixed_deposit_months = r55_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR55_1_2_fixed_deposits_months() {
			return r55_1_2_fixed_deposits_months;
		}

		public void setR55_1_2_fixed_deposits_months(BigDecimal r55_1_2_fixed_deposits_months) {
			this.r55_1_2_fixed_deposits_months = r55_1_2_fixed_deposits_months;
		}

		public BigDecimal getR55_4_6_fixed_deposits_months() {
			return r55_4_6_fixed_deposits_months;
		}

		public void setR55_4_6_fixed_deposits_months(BigDecimal r55_4_6_fixed_deposits_months) {
			this.r55_4_6_fixed_deposits_months = r55_4_6_fixed_deposits_months;
		}

		public BigDecimal getR55_7_12_fixed_deposits_months() {
			return r55_7_12_fixed_deposits_months;
		}

		public void setR55_7_12_fixed_deposits_months(BigDecimal r55_7_12_fixed_deposits_months) {
			this.r55_7_12_fixed_deposits_months = r55_7_12_fixed_deposits_months;
		}

		public BigDecimal getR55_13_18_fixed_deposits_months() {
			return r55_13_18_fixed_deposits_months;
		}

		public void setR55_13_18_fixed_deposits_months(BigDecimal r55_13_18_fixed_deposits_months) {
			this.r55_13_18_fixed_deposits_months = r55_13_18_fixed_deposits_months;
		}

		public BigDecimal getR55_19_24_fixed_deposits_months() {
			return r55_19_24_fixed_deposits_months;
		}

		public void setR55_19_24_fixed_deposits_months(BigDecimal r55_19_24_fixed_deposits_months) {
			this.r55_19_24_fixed_deposits_months = r55_19_24_fixed_deposits_months;
		}

		public BigDecimal getR55_over_24_fixed_deposits_months() {
			return r55_over_24_fixed_deposits_months;
		}

		public void setR55_over_24_fixed_deposits_months(BigDecimal r55_over_24_fixed_deposits_months) {
			this.r55_over_24_fixed_deposits_months = r55_over_24_fixed_deposits_months;
		}

		public BigDecimal getR55_certificates_of_deposit() {
			return r55_certificates_of_deposit;
		}

		public void setR55_certificates_of_deposit(BigDecimal r55_certificates_of_deposit) {
			this.r55_certificates_of_deposit = r55_certificates_of_deposit;
		}

		public BigDecimal getR55_total() {
			return r55_total;
		}

		public void setR55_total(BigDecimal r55_total) {
			this.r55_total = r55_total;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public BigDecimal getR56_current() {
			return r56_current;
		}

		public void setR56_current(BigDecimal r56_current) {
			this.r56_current = r56_current;
		}

		public BigDecimal getR56_call() {
			return r56_call;
		}

		public void setR56_call(BigDecimal r56_call) {
			this.r56_call = r56_call;
		}

		public BigDecimal getR56_savings() {
			return r56_savings;
		}

		public void setR56_savings(BigDecimal r56_savings) {
			this.r56_savings = r56_savings;
		}

		public BigDecimal getR56_0_31_notice_days() {
			return r56_0_31_notice_days;
		}

		public void setR56_0_31_notice_days(BigDecimal r56_0_31_notice_days) {
			this.r56_0_31_notice_days = r56_0_31_notice_days;
		}

		public BigDecimal getR56_32_88_notice_days() {
			return r56_32_88_notice_days;
		}

		public void setR56_32_88_notice_days(BigDecimal r56_32_88_notice_days) {
			this.r56_32_88_notice_days = r56_32_88_notice_days;
		}

		public BigDecimal getR56_91_day_deposit_fixed_deposit_months() {
			return r56_91_day_deposit_fixed_deposit_months;
		}

		public void setR56_91_day_deposit_fixed_deposit_months(BigDecimal r56_91_day_deposit_fixed_deposit_months) {
			this.r56_91_day_deposit_fixed_deposit_months = r56_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR56_1_2_fixed_deposits_months() {
			return r56_1_2_fixed_deposits_months;
		}

		public void setR56_1_2_fixed_deposits_months(BigDecimal r56_1_2_fixed_deposits_months) {
			this.r56_1_2_fixed_deposits_months = r56_1_2_fixed_deposits_months;
		}

		public BigDecimal getR56_4_6_fixed_deposits_months() {
			return r56_4_6_fixed_deposits_months;
		}

		public void setR56_4_6_fixed_deposits_months(BigDecimal r56_4_6_fixed_deposits_months) {
			this.r56_4_6_fixed_deposits_months = r56_4_6_fixed_deposits_months;
		}

		public BigDecimal getR56_7_12_fixed_deposits_months() {
			return r56_7_12_fixed_deposits_months;
		}

		public void setR56_7_12_fixed_deposits_months(BigDecimal r56_7_12_fixed_deposits_months) {
			this.r56_7_12_fixed_deposits_months = r56_7_12_fixed_deposits_months;
		}

		public BigDecimal getR56_13_18_fixed_deposits_months() {
			return r56_13_18_fixed_deposits_months;
		}

		public void setR56_13_18_fixed_deposits_months(BigDecimal r56_13_18_fixed_deposits_months) {
			this.r56_13_18_fixed_deposits_months = r56_13_18_fixed_deposits_months;
		}

		public BigDecimal getR56_19_24_fixed_deposits_months() {
			return r56_19_24_fixed_deposits_months;
		}

		public void setR56_19_24_fixed_deposits_months(BigDecimal r56_19_24_fixed_deposits_months) {
			this.r56_19_24_fixed_deposits_months = r56_19_24_fixed_deposits_months;
		}

		public BigDecimal getR56_over_24_fixed_deposits_months() {
			return r56_over_24_fixed_deposits_months;
		}

		public void setR56_over_24_fixed_deposits_months(BigDecimal r56_over_24_fixed_deposits_months) {
			this.r56_over_24_fixed_deposits_months = r56_over_24_fixed_deposits_months;
		}

		public BigDecimal getR56_certificates_of_deposit() {
			return r56_certificates_of_deposit;
		}

		public void setR56_certificates_of_deposit(BigDecimal r56_certificates_of_deposit) {
			this.r56_certificates_of_deposit = r56_certificates_of_deposit;
		}

		public BigDecimal getR56_total() {
			return r56_total;
		}

		public void setR56_total(BigDecimal r56_total) {
			this.r56_total = r56_total;
		}

		public String getR57_product() {
			return r57_product;
		}

		public void setR57_product(String r57_product) {
			this.r57_product = r57_product;
		}

		public BigDecimal getR57_current() {
			return r57_current;
		}

		public void setR57_current(BigDecimal r57_current) {
			this.r57_current = r57_current;
		}

		public BigDecimal getR57_call() {
			return r57_call;
		}

		public void setR57_call(BigDecimal r57_call) {
			this.r57_call = r57_call;
		}

		public BigDecimal getR57_savings() {
			return r57_savings;
		}

		public void setR57_savings(BigDecimal r57_savings) {
			this.r57_savings = r57_savings;
		}

		public BigDecimal getR57_0_31_notice_days() {
			return r57_0_31_notice_days;
		}

		public void setR57_0_31_notice_days(BigDecimal r57_0_31_notice_days) {
			this.r57_0_31_notice_days = r57_0_31_notice_days;
		}

		public BigDecimal getR57_32_88_notice_days() {
			return r57_32_88_notice_days;
		}

		public void setR57_32_88_notice_days(BigDecimal r57_32_88_notice_days) {
			this.r57_32_88_notice_days = r57_32_88_notice_days;
		}

		public BigDecimal getR57_91_day_deposit_fixed_deposit_months() {
			return r57_91_day_deposit_fixed_deposit_months;
		}

		public void setR57_91_day_deposit_fixed_deposit_months(BigDecimal r57_91_day_deposit_fixed_deposit_months) {
			this.r57_91_day_deposit_fixed_deposit_months = r57_91_day_deposit_fixed_deposit_months;
		}

		public BigDecimal getR57_1_2_fixed_deposits_months() {
			return r57_1_2_fixed_deposits_months;
		}

		public void setR57_1_2_fixed_deposits_months(BigDecimal r57_1_2_fixed_deposits_months) {
			this.r57_1_2_fixed_deposits_months = r57_1_2_fixed_deposits_months;
		}

		public BigDecimal getR57_4_6_fixed_deposits_months() {
			return r57_4_6_fixed_deposits_months;
		}

		public void setR57_4_6_fixed_deposits_months(BigDecimal r57_4_6_fixed_deposits_months) {
			this.r57_4_6_fixed_deposits_months = r57_4_6_fixed_deposits_months;
		}

		public BigDecimal getR57_7_12_fixed_deposits_months() {
			return r57_7_12_fixed_deposits_months;
		}

		public void setR57_7_12_fixed_deposits_months(BigDecimal r57_7_12_fixed_deposits_months) {
			this.r57_7_12_fixed_deposits_months = r57_7_12_fixed_deposits_months;
		}

		public BigDecimal getR57_13_18_fixed_deposits_months() {
			return r57_13_18_fixed_deposits_months;
		}

		public void setR57_13_18_fixed_deposits_months(BigDecimal r57_13_18_fixed_deposits_months) {
			this.r57_13_18_fixed_deposits_months = r57_13_18_fixed_deposits_months;
		}

		public BigDecimal getR57_19_24_fixed_deposits_months() {
			return r57_19_24_fixed_deposits_months;
		}

		public void setR57_19_24_fixed_deposits_months(BigDecimal r57_19_24_fixed_deposits_months) {
			this.r57_19_24_fixed_deposits_months = r57_19_24_fixed_deposits_months;
		}

		public BigDecimal getR57_over_24_fixed_deposits_months() {
			return r57_over_24_fixed_deposits_months;
		}

		public void setR57_over_24_fixed_deposits_months(BigDecimal r57_over_24_fixed_deposits_months) {
			this.r57_over_24_fixed_deposits_months = r57_over_24_fixed_deposits_months;
		}

		public BigDecimal getR57_certificates_of_deposit() {
			return r57_certificates_of_deposit;
		}

		public void setR57_certificates_of_deposit(BigDecimal r57_certificates_of_deposit) {
			this.r57_certificates_of_deposit = r57_certificates_of_deposit;
		}

		public BigDecimal getR57_total() {
			return r57_total;
		}

		public void setR57_total(BigDecimal r57_total) {
			this.r57_total = r57_total;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public M_DEP1_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_DEP1_Detail_Entity {

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
		private BigDecimal sanctionLimit;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;
		private String reportName;
		private String createUser;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;
		private String modifyUser;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;
		private String verifyUser;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;
		private String entityFlg;

		private String modifyFlg;

		private String delFlg;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
		}

		public M_DEP1_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_DEP1_Archival_Detail_Entity {

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
		private BigDecimal sanctionLimit;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;
		private String reportName;
		private String createUser;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;
		private String modifyUser;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;
		private String verifyUser;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;
		private String entityFlg;

		private String modifyFlg;

		private String delFlg;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
		}

		public M_DEP1_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

}
