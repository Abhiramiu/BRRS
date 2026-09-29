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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import com.bornfire.brrs.entities.UserProfileRep;


@Service
@Transactional

public class BRRS_M_CA2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ─── Current summary by date ───────────────────────────────────────────────
	public List<M_CA2_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, new M_CA2SummaryRowMapper());
	}

	// ─── Archival summary by date and version ──────────────────────────────────
	public List<M_CA2_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ArchivalSummaryRowMapper());
	}

	// ─── All archival summaries with version ───────────────────────────────────
	public List<M_CA2_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC",
				new M_CA2ArchivalSummaryRowMapper());
	}

	// ─── Resub summary by date and version ─────────────────────────────────────
	public List<M_CA2_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ResubSummaryRowMapper());
	}

	// ─── Current detail by date ────────────────────────────────────────────────
	public List<M_CA2_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_CA2DetailRowMapper());
	}

	// ─── Count current detail rows ─────────────────────────────────────────────
	public int getDetailCount(Date reportDate) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BRRS_M_CA2_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, Integer.class);
	}

	// ─── Current detail by label and criteria ──────────────────────────────────
	public List<M_CA2_Detail_Entity> getDetailByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?",
				new Object[] { reportLabel, reportAddlCriteria1, reportDate }, new M_CA2DetailRowMapper());
	}

	// ─── Current detail by account number ──────────────────────────────────────
	public M_CA2_Detail_Entity getDetailByAcctNumber(String acctNumber) {
		List<M_CA2_Detail_Entity> list = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNumber },
				new M_CA2DetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}
	
	//=====================RESUB  
	
	// ─── Resub summary by date and version (archival table) ────────────────────
	public List<M_CA2_Summary_Entity> get_ResubSummaryByDate(Date reportDate, String reportVersion) {

	    return jdbcTemplate.query(
	        "SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY " +
	        "WHERE TRUNC(REPORT_DATE)=TRUNC(?) " +
	        "AND REPORT_VERSION=?",
	        new Object[] { reportDate, reportVersion },
	        new M_CA2SummaryRowMapper());
	}
	
	// ─── Find archival detail by ACCT_NUMBER ───────────────────────────────────
	public M_CA2_Detail_Entity findBySnoArch(String acctNumber) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		try {
			String sql = "SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? ORDER BY REPORT_DATE DESC";
			List<M_CA2_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { acctNumber.trim() }, new M_CA2DetailRowMapper());
			return (list != null && !list.isEmpty()) ? list.get(0) : null;
		} catch (Exception e) {
			return null;
		}
	}

	public M_CA2_Detail_Entity findByAcctNumberArch(String acctNumber, Date reportDate) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		if (reportDate != null) {
			try {
				String sql = "SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)";
				List<M_CA2_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { acctNumber.trim(), reportDate }, new M_CA2DetailRowMapper());
				if (list != null && !list.isEmpty()) {
					return list.get(0);
				}
			} catch (Exception ignored) {
			}
			return null;
		}
		return findBySnoArch(acctNumber);
	}
	
	// ─── Find current detail by SNO / ACCT_NUMBER ─────────────────────────────
	public M_CA2_Detail_Entity findBySno(String sno) {
		if (sno == null || sno.trim().isEmpty()) {
			return null;
		}
		try {
			String sql = "SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE ACCT_NUMBER = ?";
			List<M_CA2_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { sno.trim() }, new M_CA2DetailRowMapper());
			return (list != null && !list.isEmpty()) ? list.get(0) : null;
		} catch (Exception ignored) {
			return null;
		}
	}
	
	// ─── Check if version is the highest ───────────────────────────────────────
	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY " + "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}
//=====================
	// ─── Archival detail by date ────────────────────────────────────────────────
	public List<M_CA2_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate) {

	    return jdbcTemplate.query(
	            "SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
	            new Object[] { reportDate },
	            new M_CA2ArchivalDetailRowMapper());
	}

	// ─── Archival detail by label and criteria ─────────────────────────────────
	public List<M_CA2_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportLabel, reportAddlCriteria1, reportDate }, new M_CA2ArchivalDetailRowMapper());
	}

	// ─── Resub detail by date and version ──────────────────────────────────────
	public List<M_CA2_RESUB_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ResubDetailRowMapper());
	}

	// ─── Resub detail by label, criteria and version ───────────────────────────
	public List<M_CA2_RESUB_Detail_Entity> getResubDetailByRowIdAndColumnId(String reportLabel, String criteria1,
			Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_RESUB_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportLabel, criteria1, reportDate, version }, new M_CA2ResubDetailRowMapper());
	}

//	public ModelAndView getM_CA2View(String reportId, String fromdate, String todate, String currency, String dtltype, // kept
//																														// but
//																														// not
//																														// used
//			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
//
//		ModelAndView mv = new ModelAndView();
//		String userid = (String) req1.getSession().getAttribute("USERID");
//
//		System.out.println("User Id Maker and Checker: " + userid);
//		String role = userProfileRep.getUserRole(userid);
//		md.addAttribute("role", role);
//		System.out.println("Role: " + role);
//
//		try {
//
//			// Parse date only once
//			Date d1 = dateformat.parse(todate);
//
//			System.out.println("======= VIEW DEBUG =======");
//			System.out.println("TYPE    : " + type);
//			System.out.println("DATE    : " + d1);
//			System.out.println("VERSION : " + version);
//			System.out.println("==========================");
//
//			// ===========================================================
//			// SUMMARY ONLY
//			// ===========================================================
//
//			/* ---------- ARCHIVAL SUMMARY ---------- */
//			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
//
//				List<M_CA2_Archival_Summary_Entity> summaryList = getArchivalSummaryByDateAndVersion(d1, version);
//
//				System.out.println("Archival Summary Size : " + summaryList.size());
//
//				mv.addObject("displaymode", "summary");
//				mv.addObject("reportsummary", summaryList);
//			}
//
//			/* ---------- RESUB SUMMARY ---------- */
//			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//
//				List<M_CA2_RESUB_Summary_Entity> summaryList = getResubSummaryByDateAndVersion(d1, version);
//
//				System.out.println("Resub Summary Size : " + summaryList.size());
//
//				mv.addObject("displaymode", "resub");
//				mv.addObject("reportsummary", summaryList);
//			}
//
//			/* ---------- NORMAL SUMMARY ---------- */
//			else {
//
//				List<M_CA2_Summary_Entity> summaryList = getSummaryByDate(d1);
//
//				System.out.println("Normal Summary Size : " + summaryList.size());
//
//				mv.addObject("displaymode", "summary");
//				mv.addObject("reportsummary", summaryList);
//			}
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		mv.setViewName("BRRS/M_CA2");
//		System.out.println("View set to: " + mv.getViewName());
//
//		return mv;
//	}
	
	// ─── Main report view ───────────────────────────────────────────────────────
	public ModelAndView getM_CA2View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("M_CA2 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<M_CA2_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = null;
				try {
					dt = dateformat.parse(todate != null ? todate : fromdate);
				} catch (Exception e) {
					dt = new SimpleDateFormat("dd/MM/yyyy").parse(todate != null ? todate : fromdate);
				}

				T1Master = getArchivalSummaryByDateAndVersion(dt, version);

				System.out.println(type + " Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));

			} catch (Exception e) {
				e.printStackTrace();
			}
           mv.addObject("displaymode", "summary");
			mv.addObject("reportsummary", T1Master);
		}

		// NORMAL MODE
		else {

			List<M_CA2_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = null;
				try {
					dt = dateformat.parse(todate != null ? todate : fromdate);
				} catch (Exception e) {
					dt = new SimpleDateFormat("dd/MM/yyyy").parse(todate != null ? todate : fromdate);
				}

				T1Master = getSummaryByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}
           mv.addObject("displaymode", "summary");
			mv.addObject("reportsummary", T1Master);
		}

		Date parsedDate = null;
		try {
			parsedDate = dateformat.parse(todate != null ? todate : fromdate);
		} catch (Exception e) {
			try {
				parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate != null ? todate : fromdate);
			} catch (Exception ignored) {}
		}
		String formattedDdmmyyyy = parsedDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(parsedDate) : todate;

		mv.setViewName("BRRS/M_CA2");
		mv.addObject("reportId", reportId);
		mv.addObject("currency", currency);
		mv.addObject("type", type);
		mv.addObject("version", version);
		mv.addObject("asondate", formattedDdmmyyyy);
		mv.addObject("fromdate", formattedDdmmyyyy);
		mv.addObject("todate", formattedDdmmyyyy);

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}
	
	// ─── Current detail view ───────────────────────────────────────────────────
	public ModelAndView getM_CA2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Date parsedDate = null;

		try {

			if (todate != null && !todate.isEmpty()) {
				try {
					parsedDate = dateformat.parse(todate);
				} catch (Exception e) {
					parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
				}
			}
			if (parsedDate == null && fromdate != null && !fromdate.isEmpty()) {
				try {
					parsedDate = dateformat.parse(fromdate);
				} catch (Exception e) {
					parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromdate);
				}
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;

			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			// ARCHIVAL / RESUB MODE
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<M_CA2_Archival_Detail_Entity> detailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					detailList = getArchivalDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					detailList = getArchivalDetailByDateAndVersion(parsedDate);
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);

				try {
					BigDecimal verDecimal = new BigDecimal(version);
					mv.addObject("allowdetail", getishighestversion(parsedDate, verDecimal));
				} catch (Exception ex) {
					mv.addObject("allowdetail", "NO");
				}

				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}

			// CURRENT MODE
			else {

				List<M_CA2_Detail_Entity> currentDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					currentDetailList = getDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					currentDetailList = getDetailByDate(parsedDate);
				}

				mv.addObject("reportdetails", currentDetailList);
				mv.addObject("reportmaster12", currentDetailList);

				System.out.println("CURRENT DETAIL COUNT: " + currentDetailList.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", e.getMessage());
		}

		String formattedDdmmyyyy = parsedDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(parsedDate) : todate;

		mv.setViewName("BRRS/M_CA2");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);
		mv.addObject("type", type);
		mv.addObject("version", version);
		mv.addObject("asondate", formattedDdmmyyyy);
		mv.addObject("fromdate", formattedDdmmyyyy);
		mv.addObject("todate", formattedDdmmyyyy);

		return mv;
	}

//	public ModelAndView getM_CA2currentDtl(String reportId, String fromdate, String todate, String currency,
//			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
//			Model md) {
//
//		int pageSize = pageable != null ? pageable.getPageSize() : 10;
//		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
//		int totalPages = 0;
//
//		ModelAndView mv = new ModelAndView();
//
//		String userid = (String) req1.getSession().getAttribute("USERID");
//
//		System.out.println("User Id Maker and Checker: " + userid);
//		String role = userProfileRep.getUserRole(userid);
//		md.addAttribute("role", role);
//		System.out.println("Role: " + role);
//
//		/* Session hs = sessionFactory.getCurrentSession(); */
//
//		try {
//			Date parsedDate = null;
//			if (todate != null && !todate.isEmpty()) {
//				parsedDate = dateformat.parse(todate);
//			}
//
//			String reportLabel = null;
//			String reportAddlCriteria_1 = null;
//
//			// ✅ Split filter string into rowId & columnId
//			if (filter != null && filter.contains(",")) {
//				String[] parts = filter.split(",");
//				if (parts.length >= 2) {
//					reportLabel = parts[0];
//					reportAddlCriteria_1 = parts[1];
//				}
//			}
//
//			/*
//			 * ========================================================= ARCHIVAL DETAIL
//			 * =========================================================
//			 */
//			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
//
//				List<M_CA2_Archival_Detail_Entity> T1Dt1;
//
//				if (reportLabel != null && reportAddlCriteria_1 != null) {
//					T1Dt1 = getArchivalDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria_1, parsedDate, version);
//				} else {
//					T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//			}
//
//			/*
//			 * ========================================================= RESUB DETAIL ✅
//			 * ADDED =========================================================
//			 */
//			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//
//				List<M_CA2_RESUB_Detail_Entity> T1Dt1;
//
//				if (reportLabel != null && reportAddlCriteria_1 != null) {
//					T1Dt1 = getResubDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria_1, parsedDate, version);
//				} else {
//					T1Dt1 = getResubDetailByDateAndVersion(parsedDate, version);
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				System.out.println("RESUB COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//			}
//
//			/*
//			 * ========================================================= CURRENT DETAIL
//			 * =========================================================
//			 */
//			else {
//
//				List<M_CA2_Detail_Entity> T1Dt1;
//
//				if (reportLabel != null && reportAddlCriteria_1 != null) {
//					T1Dt1 = getDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria_1, parsedDate);
//				} else {
//					T1Dt1 = getDetailByDate(parsedDate);
//
//					totalPages = getDetailCount(parsedDate);
//
//					mv.addObject("pagination", "YES");
//				}
//
//				mv.addObject("reportdetails", T1Dt1);
//				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//			}
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//			mv.addObject("errorMessage", "Invalid date format: " + todate);
//		} catch (Exception e) {
//			e.printStackTrace();
//			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
//		}
//
//		// ✅ Common attributes
//		mv.setViewName("BRRS/M_CA2");
//		mv.addObject("displaymode", "Details");
//		mv.addObject("currentPage", currentPage);
//		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//		mv.addObject("reportsflag", "reportsflag");
//		mv.addObject("menu", reportId);
//
//		return mv;
//	}

	// ─── Archival report list ───────────────────────────────────────────────────
	public List<Object[]> getM_CA2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA2_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_CA2  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// ─── Resub report list ─────────────────────────────────────────────────────
	public List<Object[]> getM_CA2Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA2_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA2_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA2 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	@Transactional
	// ─── Update summary report (Overloaded) ─────────────────────────────────────
	public ResponseEntity<?> updateReport(M_CA2_Summary_Entity updatedEntity) {
		return updateReport(updatedEntity, null, null, null);
	}

	@Transactional
	public ResponseEntity<?> updateReport(M_CA2_Summary_Entity updatedEntity, String type, String version, String entry) {
		try {
			boolean isResub = "RESUB".equalsIgnoreCase(type);
			Date reportDate = updatedEntity.getReport_date();
			if (reportDate == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Report Date is required.");
			}
			java.sql.Date sqlDate = new java.sql.Date(reportDate.getTime());
			String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);

			System.out.println("Updating CA2 Summary: type=" + type + ", version=" + version + ", entry=" + entry + ", date=" + formattedDate);

			// =========================================
			// FETCH EXISTING RECORD
			// =========================================
			M_CA2_Summary_Entity existing;
			String ver = (version != null && !version.isEmpty()) ? version
					: (updatedEntity.getReport_version() != null ? updatedEntity.getReport_version().toString() : null);

			if (isResub) {
				List<M_CA2_Summary_Entity> list = null;
				if (ver != null) {
					list = get_ResubSummaryByDate(reportDate, ver);
				}
				if (list == null || list.isEmpty()) {
					String maxVerSql = "SELECT NVL(MAX(REPORT_VERSION), 0) FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
					Integer maxV = jdbcTemplate.queryForObject(maxVerSql, Integer.class, sqlDate);
					ver = String.valueOf(maxV);
					list = get_ResubSummaryByDate(reportDate, ver);
				}
				if (list.isEmpty()) {
					existing = new M_CA2_Summary_Entity();
					existing.setReport_date(reportDate);
					if (ver != null) {
						existing.setReport_version(ver);
					}
				} else {
					existing = list.get(0);
				}
			} else {
				List<M_CA2_Summary_Entity> list = getSummaryByDate(reportDate);
				if (list.isEmpty()) {
					existing = new M_CA2_Summary_Entity();
					existing.setReport_date(reportDate);
				} else {
					existing = list.get(0);
				}
			}

			// =========================================
			// AUDIT OLD COPY
			// =========================================
			M_CA2_Summary_Entity oldcopy = new M_CA2_Summary_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			// =========================================
			// ALLOWED ROWS
			// =========================================
			int[] amount2Indexes = { 11, 32, 42, 44, 43, 46 };
			int[] amount1Indexes = { 14, 15, 16, 18, 19, 21 };

			// AMOUNT_2 UPDATE
			for (int i : amount2Indexes) {
				String getterName = "getR" + i + "_amount_2";
				String setterName = "setR" + i + "_amount_2";
				try {
					Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
					Method setter = M_CA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					Object newValue = getter.invoke(updatedEntity);
					Object oldValue = getter.invoke(existing);

					if (newValue != null && !newValue.equals(oldValue)) {
						setter.invoke(existing, newValue);
						isChanged = true;
					}
				} catch (NoSuchMethodException e) {
					continue;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// AMOUNT_1 UPDATE
			for (int i : amount1Indexes) {
				String getterName = "getR" + i + "_amount_1";
				String setterName = "setR" + i + "_amount_1";
				try {
					Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
					Method setter = M_CA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					Object newValue = getter.invoke(updatedEntity);
					Object oldValue = getter.invoke(existing);

					if (newValue != null && !newValue.equals(oldValue)) {
						setter.invoke(existing, newValue);
						isChanged = true;
					}
				} catch (NoSuchMethodException e) {
					continue;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// METADATA
			if (updatedEntity.getReport_version() != null) {
				existing.setReport_version(updatedEntity.getReport_version());
			}
			if (updatedEntity.getReport_frequency() != null) {
				existing.setReport_frequency(updatedEntity.getReport_frequency());
			}
			if (updatedEntity.getReport_code() != null) {
				existing.setReport_code(updatedEntity.getReport_code());
			}
			if (updatedEntity.getReport_desc() != null) {
				existing.setReport_desc(updatedEntity.getReport_desc());
			}
			if (updatedEntity.getEntity_flg() != null) {
				existing.setEntity_flg(updatedEntity.getEntity_flg());
			}
			if (updatedEntity.getModify_flg() != null) {
				existing.setModify_flg(updatedEntity.getModify_flg());
			}
			if (updatedEntity.getDel_flg() != null) {
				existing.setDel_flg(updatedEntity.getDel_flg());
			}

			// SAVE CHANGES
			boolean isResubNoEntry = isResub && "NO".equalsIgnoreCase(entry);

			if (isChanged) {
				if (isResub) {
					if (isResubNoEntry) {
						// REGENERATE: Do NOT overwrite the old version in archival summary!
						// The old record remains in version 'ver'.
						// Clean live summary first:
						jdbcTemplate.update("DELETE FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);

						// Build columnsPart string
						StringBuilder columnsPart = new StringBuilder();
						int[] allRows = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 36, 41, 42, 43, 44, 45, 46, 47, 48, 49 };
						String[] tokens = { "PRODUCT", "AMOUNT_1", "AMOUNT_2" };
						for (int row : allRows) {
							for (String token : tokens) {
								columnsPart.append("R").append(row).append("_").append(token).append(", ");
							}
						}

						// Copy base archival row to live summary
						String copyToLiveSql = "INSERT INTO BRRS_M_CA2_SUMMARYTABLE ("
								+ columnsPart
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
								+ "SELECT "
								+ columnsPart
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
								+ "FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
						jdbcTemplate.update(copyToLiveSql, sqlDate, ver);

						// Apply modified manual values to live summary so procedure uses them
						String updateLiveSql = "UPDATE BRRS_M_CA2_SUMMARYTABLE SET "
								+ "R11_AMOUNT_2=?, R32_AMOUNT_2=?, R42_AMOUNT_2=?, R44_AMOUNT_2=?, R43_AMOUNT_2=?, R46_AMOUNT_2=?, "
								+ "R14_AMOUNT_1=?, R15_AMOUNT_1=?, R16_AMOUNT_1=?, R18_AMOUNT_1=?, R19_AMOUNT_1=?, R21_AMOUNT_1=? "
								+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
						jdbcTemplate.update(updateLiveSql,
								existing.getR11_amount_2(), existing.getR32_amount_2(), existing.getR42_amount_2(),
								existing.getR44_amount_2(), existing.getR43_amount_2(), existing.getR46_amount_2(),
								existing.getR14_amount_1(), existing.getR15_amount_1(), existing.getR16_amount_1(),
								existing.getR18_amount_1(), existing.getR19_amount_1(), existing.getR21_amount_1(),
								sqlDate);

						auditService.compareEntitiesmanual(
								oldcopy,
								existing,
								reportDate.toString(),
								"M CA2 Archival Summary Screen",
								"BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");

						System.out.println("Modified values staged into live summary. Old version " + ver + " preserved in archival summary.");

						Run_M_CA2_Procedure(formattedDate, "RESUB", "NO");
						return ResponseEntity.ok("Record updated and Report Regenerated successfully!");

					} else {
						// MODIFY ONLY: Update current version in archival summary
						String updateSql = "UPDATE BRRS_M_CA2_ARCHIVALTABLE_SUMMARY SET "
								+ "R11_AMOUNT_2=?, R32_AMOUNT_2=?, R42_AMOUNT_2=?, R44_AMOUNT_2=?, R43_AMOUNT_2=?, R46_AMOUNT_2=?, "
								+ "R14_AMOUNT_1=?, R15_AMOUNT_1=?, R16_AMOUNT_1=?, R18_AMOUNT_1=?, R19_AMOUNT_1=?, R21_AMOUNT_1=? "
								+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?) AND REPORT_VERSION=?";

						jdbcTemplate.update(updateSql,
								existing.getR11_amount_2(), existing.getR32_amount_2(), existing.getR42_amount_2(),
								existing.getR44_amount_2(), existing.getR43_amount_2(), existing.getR46_amount_2(),
								existing.getR14_amount_1(), existing.getR15_amount_1(), existing.getR16_amount_1(),
								existing.getR18_amount_1(), existing.getR19_amount_1(), existing.getR21_amount_1(),
								sqlDate, ver);

						auditService.compareEntitiesmanual(
								oldcopy,
								existing,
								reportDate.toString(),
								"M CA2 Archival Summary Screen",
								"BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");

						System.out.println("CA2 Archival Summary updated successfully for version " + ver);
						return ResponseEntity.ok("Record updated successfully!");
					}

				} else {
					jdbcTemplate.update(
							"UPDATE BRRS_M_CA2_SUMMARYTABLE SET "
									+ "R11_AMOUNT_2=?, R32_AMOUNT_2=?, R42_AMOUNT_2=?, R44_AMOUNT_2=?, R43_AMOUNT_2=?, R46_AMOUNT_2=?, "
									+ "R14_AMOUNT_1=?, R15_AMOUNT_1=?, R16_AMOUNT_1=?, R18_AMOUNT_1=?, R19_AMOUNT_1=?, R21_AMOUNT_1=?, "
									+ "REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?, ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=? "
									+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
							existing.getR11_amount_2(), existing.getR32_amount_2(), existing.getR42_amount_2(),
							existing.getR44_amount_2(), existing.getR43_amount_2(), existing.getR46_amount_2(),
							existing.getR14_amount_1(), existing.getR15_amount_1(), existing.getR16_amount_1(),
							existing.getR18_amount_1(), existing.getR19_amount_1(), existing.getR21_amount_1(),
							existing.getReport_version(), existing.getReport_frequency(), existing.getReport_code(),
							existing.getReport_desc(), existing.getEntity_flg(), existing.getModify_flg(),
							existing.getDel_flg(), sqlDate);

					auditService.compareEntitiesmanual(
							oldcopy,
							existing,
							reportDate.toString(),
							"M CA2 Summary Screen",
							"BRRS_M_CA2_SUMMARY");

					System.out.println("CA2 Summary updated successfully");

					return ResponseEntity.ok("CA2 Summary updated successfully");
				}
			} else {
				if (isResubNoEntry) {
					Run_M_CA2_Procedure(formattedDate, "RESUB", "NO");
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("No changes detected");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating CA2 Summary : " + e.getMessage());
		}
	}
	
	
//	@Transactional
//	public ResponseEntity<?> updateReport(M_CA2_Summary_Entity updatedEntity, String type) {
//
//		boolean isResub = "RESUB".equalsIgnoreCase(type);
//
//		String tableName = isResub
//				? "BRRS_M_CA2_ARCHIVALTABLE_SUMMARY"
//				: "BRRS_M_CA2_SUMMARYTABLE";
//
//		String screenName = isResub
//				? "M CA2 Resub Summary Screen"
//				: "M CA2 Summary Screen";
//		
//		
//		
//
//		try {
//
//			System.out.println("Updating CA2 Summary Table : " + tableName);
//
//			// =========================================
//			// FETCH EXISTING RECORD
//			// =========================================
//
//			List<M_CA2_Summary_Entity> list = isResub
//			        ? get_ResubSummaryByDate(
//			                updatedEntity.getReport_date(),
//			                updatedEntity.getReport_version())
//			        : getSummaryByDate(updatedEntity.getReport_date());
//
//			M_CA2_Summary_Entity existing;
//
//			if (list.isEmpty()) {
//
//				System.out.println("No record found for REPORT_DATE : " + updatedEntity.getReport_date());
//
//				existing = new M_CA2_Summary_Entity();
//				existing.setReport_date(updatedEntity.getReport_date());
//
//			} else {
//
//				existing = list.get(0);
//			}
//
//			// =========================================
//			// AUDIT OLD COPY
//			// =========================================
//
//			M_CA2_Summary_Entity oldcopy = new M_CA2_Summary_Entity();
//
//			BeanUtils.copyProperties(existing, oldcopy);
//
//			boolean isChanged = false;
//
//			// =========================================
//			// ALLOWED ROWS
//			// =========================================
//
//			int[] amount2Indexes = { 11, 32, 42, 44, 43, 46 };
//			int[] amount1Indexes = { 14, 15, 16, 18, 19, 21 };
//
//			// =========================================
//			// AMOUNT_2 UPDATE
//			// =========================================
//
//			for (int i : amount2Indexes) {
//
//			    String getterName = "getR" + i + "_amount_2";
//			    String setterName = "setR" + i + "_amount_2";
//
//			    try {
//
//			        Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
//			        Method setter = M_CA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//			        Object newValue = getter.invoke(updatedEntity);
//			        Object oldValue = getter.invoke(existing);
//
//			        System.out.println("Updating R" + i + "_amount_2 : " + newValue);
//
//			        if (newValue != null && !newValue.equals(oldValue)) {
//
//			        	// Update entity for audit
//			        	setter.invoke(existing, newValue);
//
//			        	// Update only the modified column
//			        	String columnName = "R" + i + "_AMOUNT_2";
//
//			        	String sql = "UPDATE " + tableName
//			        	        + " SET " + columnName + " = ? "
//			        	        + "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
//
//			        	List<Object> params = new ArrayList<>();
//			        	params.add(newValue);
//			        	params.add(updatedEntity.getReport_date());
//
//			        	// For RESUB update only the selected version
//			        	if (isResub) {
//			        	    sql += " AND REPORT_VERSION = ?";
//			        	    params.add(updatedEntity.getReport_version());
//			        	}
//
//			        	jdbcTemplate.update(sql, params.toArray());
//
//			        	System.out.println("Updated Column : " + columnName);
//
//			        	isChanged = true;
//			        }
//
//			    } catch (NoSuchMethodException e) {
//
//			        continue;
//
//			    } catch (Exception e) {
//
//			        e.printStackTrace();
//			    }
//			}
//
//			// =========================================
//			// AMOUNT_1 UPDATE
//			// =========================================
//
//			for (int i : amount1Indexes) {
//
//			    String getterName = "getR" + i + "_amount_1";
//			    String setterName = "setR" + i + "_amount_1";
//
//			    try {
//
//			        Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
//			        Method setter = M_CA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//			        Object newValue = getter.invoke(updatedEntity);
//			        Object oldValue = getter.invoke(existing);
//
//			        System.out.println("Updating R" + i + "_amount_1 : " + newValue);
//
//			        if (newValue != null && !newValue.equals(oldValue)) {
//			        	// Update entity for audit
//			        	setter.invoke(existing, newValue);
//
//			        	// Update only the modified column
//			        	String columnName = "R" + i + "_AMOUNT_1";
//
//			        	String sql = "UPDATE " + tableName
//			        	        + " SET " + columnName + " = ? "
//			        	        + "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
//
//			        	List<Object> params = new ArrayList<>();
//			        	params.add(newValue);
//			        	params.add(updatedEntity.getReport_date());
//
//			        	// For RESUB update only the selected version
//			        	if (isResub) {
//			        	    sql += " AND REPORT_VERSION = ?";
//			        	    params.add(updatedEntity.getReport_version());
//			        	}
//
//			        	jdbcTemplate.update(sql, params.toArray());
//
//			        	System.out.println("Updated Column : " + columnName);
//
//			        	isChanged = true;
//			        }
//
//			    } catch (NoSuchMethodException e) {
//
//			        continue;
//
//			    } catch (Exception e) {
//
//			        e.printStackTrace();
//			    }
//			}
//
//			// =========================================
//			// METADATA
//			// =========================================
//
//			if (isResub) {
//
//			    if (updatedEntity.getReport_version() != null) {
//			        existing.setReport_version(updatedEntity.getReport_version());
//			    }
//
//			} else {
//
//			    existing.setReport_version(updatedEntity.getReport_version());
//			}
//			existing.setReport_frequency(updatedEntity.getReport_frequency());
//			existing.setReport_code(updatedEntity.getReport_code());
//			existing.setReport_desc(updatedEntity.getReport_desc());
//			existing.setEntity_flg(updatedEntity.getEntity_flg());
//			existing.setModify_flg(updatedEntity.getModify_flg());
//			existing.setDel_flg(updatedEntity.getDel_flg());
//
//			// =========================================
//			// SAVE ONLY IF CHANGED
//			// =========================================
//
//			// =========================================
//			// SAVE ONLY IF CHANGED
//			// =========================================
//
//			if (isChanged) {
//
//			    String changes = auditService.getChanges(oldcopy, existing);
//
//			    // Update only metadata columns
//			    jdbcTemplate.update(
//			    	    "UPDATE " + tableName + " SET "
//			    	            + "REPORT_FREQUENCY=?, "
//			    	            + "REPORT_CODE=?, "
//			    	            + "REPORT_DESC=?, "
//			    	            + "ENTITY_FLG=?, "
//			    	            + "MODIFY_FLG=?, "
//			    	            + "DEL_FLG=? "
//			    	            + "WHERE TRUNC(REPORT_DATE)=TRUNC(?)"
//			    	            + " AND REPORT_VERSION=?",
//			    	    existing.getReport_frequency(),
//			    	    existing.getReport_code(),
//			    	    existing.getReport_desc(),
//			    	    existing.getEntity_flg(),
//			    	    existing.getModify_flg(),
//			    	    existing.getDel_flg(),
//			    	    existing.getReport_date(),
//			    	    existing.getReport_version());
//
//			    if (!changes.isEmpty()) {
//
//			        auditService.compareEntitiesmanual(
//			                oldcopy,
//			                existing,
//			                updatedEntity.getReport_date().toString(),
//			                screenName,
//			                tableName);
//			    }
//
//			    System.out.println("CA2 Summary Updated Successfully");
//
//			    String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
//			            .format(updatedEntity.getReport_date());
//
//			    TransactionSynchronizationManager.registerSynchronization(
//			            new TransactionSynchronizationAdapter() {
//
//			                @Override
//			                public void afterCommit() {
//
//			                    try {
//
//			                        System.out.println("Transaction committed - Calling CA2 Procedure");
//
//			                        jdbcTemplate.update(
//			                                "BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;",
//			                                formattedDate);
//
//			                        System.out.println("Procedure executed successfully");
//			                        
//			                        if (isResub) {
//
//			                            System.out.println("Copying calculated values to BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");
//
//			                            jdbcTemplate.update(
//			                            	    "BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;",
//			                            	    formattedDate);
//
//			                            System.out.println("Archival Summary updated successfully.");
//			                        }
//
//			                    } catch (Exception e) {
//
//			                        e.printStackTrace();
//			                    }
//			                }
//			            });
//
//			    return ResponseEntity.ok("CA2 Summary Updated Successfully");
//
//			} else {
//
//			    return ResponseEntity.ok("No Changes Detected");
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error Updating CA2 Summary : " + e.getMessage());
//		}
//	}

//	@Transactional
//	public void updateReport(M_CA2_Summary_Entity updatedEntity) {
//
//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_CA2_Summary_Entity existing =
//	            BRRS_M_CA2_Summary_Repo.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() ->
//	                    new RuntimeException("Record not found for REPORT_DATE: "
//	                            + updatedEntity.getReport_date()));
//
//	    // Only allowed R-numbers
//	    int[] amount2Indexes = { 11, 32, 42, 44,43, 46,48 };     // AMOUNT_2	
//	    int[] amount1Indexes = {  14, 15, 16, 18, 19, 21 }; // AMOUNT_1
//
//	    try {
//
//	        // ===== AMOUNT_2 updates =====
//	        for (int i : amount2Indexes) {
//
//	            String getterName = "getR" + i + "_amount_2";
//	            String setterName = "setR" + i + "_amount_2";
//
//	            try {
//	                Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
//	                Method setter = M_CA2_Summary_Entity.class.getMethod(
//	                        setterName,
//	                        getter.getReturnType()
//	                );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                continue; // skip safely
//	            }
//	        }
//
//	        // ===== AMOUNT_1 updates =====
//	        for (int i : amount1Indexes) {
//
//	            String getterName = "getR" + i + "_amount_1";
//	            String setterName = "setR" + i + "_amount_1";
//
//	            try {
//	                Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
//	                Method setter = M_CA2_Summary_Entity.class.getMethod(
//	                        setterName,
//	                        getter.getReturnType()
//	                );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                continue; // skip safely
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // Save only intended updates
//	    BRRS_M_CA2_Summary_Repo.saveAndFlush(existing);
//	}

	// ─── Detail Excel download ──────────────────────────────────────────────────
	public byte[] getM_CA2DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_CA2 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_CA2Detail");

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

//ACCT BALANCE style (right aligned with thousand separator)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<M_CA2_Detail_Entity> reportData = getDetailByDate(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA2_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
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
				logger.info("No data found for M_CA2 — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_CA2 Excel", e);
			return new byte[0];
		}
	}
//	public byte[] getM_CA2DetailExcel(String filename, String fromdate, String todate, String currency,
//										   String dtltype, String type, String version) {
//	    try {
//	        logger.info("Generating Excel for M_CA2 Details...");
//	        System.out.println("came to Detail download service");
//
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//
//	        XSSFWorkbook workbook = new XSSFWorkbook();
//	        XSSFSheet sheet = workbook.createSheet("M_CA2Details");
//
//	        // Common border style
//	        BorderStyle border = BorderStyle.THIN;
//
//	        // Header style (left aligned)
//	        CellStyle headerStyle = workbook.createCellStyle();
//	        Font headerFont = workbook.createFont();
//	        headerFont.setBold(true);
//	        headerFont.setFontHeightInPoints((short) 10);
//	        headerStyle.setFont(headerFont);
//	        headerStyle.setAlignment(HorizontalAlignment.LEFT);
//	        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//	        headerStyle.setBorderTop(border);
//	        headerStyle.setBorderBottom(border);
//	        headerStyle.setBorderLeft(border);
//	        headerStyle.setBorderRight(border);
//
//	        // Right-aligned header style for ACCT BALANCE
//	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//	        // Default data style (left aligned)
//	        CellStyle dataStyle = workbook.createCellStyle();
//	        dataStyle.setAlignment(HorizontalAlignment.LEFT);
//	        dataStyle.setBorderTop(border);
//	        dataStyle.setBorderBottom(border);
//	        dataStyle.setBorderLeft(border);
//	        dataStyle.setBorderRight(border);
//
//	        // ACCT BALANCE style (right aligned with 3 decimals)
//	        CellStyle balanceStyle = workbook.createCellStyle();
//	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//	        balanceStyle.setBorderTop(border);
//	        balanceStyle.setBorderBottom(border);
//	        balanceStyle.setBorderLeft(border);
//	        balanceStyle.setBorderRight(border);
//
//	        // Header row
//	        String[] headers = {
//	            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
//	        };
//
//	        XSSFRow headerRow = sheet.createRow(0);
//	        for (int i = 0; i < headers.length; i++) {
//	            Cell cell = headerRow.createCell(i);
//	            cell.setCellValue(headers[i]);
//
//	            if (i == 3) { // ACCT BALANCE
//	                cell.setCellStyle(rightAlignedHeaderStyle);
//	            } else {
//	                cell.setCellStyle(headerStyle);
//	            }
//
//	            sheet.setColumnWidth(i, 5000);
//	        }
//
//	        // Get data
//	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//	        List<M_CA2_Detail_Entity> reportData = getDetailByDate(parsedToDate);
//
//	        if (reportData != null && !reportData.isEmpty()) {
//	            int rowIndex = 1;
//	            for (M_CA2_Detail_Entity item : reportData) {
//	                XSSFRow row = sheet.createRow(rowIndex++);
//
//	                row.createCell(0).setCellValue(item.getCustId());
//	                row.createCell(1).setCellValue(item.getAcctNumber());
//	                row.createCell(2).setCellValue(item.getAcctName());
//
//	                // ACCT BALANCE (right aligned, 3 decimal places)
//	                Cell balanceCell = row.createCell(3);
//	                if (item.getAcctBalanceInPula() != null) {
//	                    balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
//	                } else {
//	                    balanceCell.setCellValue(0.000);
//	                }
//	                balanceCell.setCellStyle(balanceStyle);
//
//	                row.createCell(4).setCellValue(item.getRowId());
//	                row.createCell(5).setCellValue(item.getColumnId());
//	                row.createCell(6).setCellValue(
//	                    item.getReportDate() != null ?
//	                    new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
//	                );
//
//	                // Apply data style for all other cells
//	                for (int j = 0; j < 7; j++) {
//	                    if (j != 3) {
//	                        row.getCell(j).setCellStyle(dataStyle);
//	                    }
//	                }
//	            }
//	        } else {
//	            logger.info("No data found for M_CA2 — only header will be written.");
//	        }
//
//	        // Write to byte[]
//	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	        workbook.write(bos);
//	        workbook.close();
//
//	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//	        return bos.toByteArray();
//
//	    } catch (Exception e) {
//	        logger.error("Error generating M_CA2 Excel", e);
//	        return new byte[0];
//	    }
//	}

	// ─── Archival detail Excel download ────────────────────────────────────────
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_CA2 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_CA2Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

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
			List<M_CA2_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA2_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
					Cell balanceCell = row.createCell(3);

					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}

					// Create style with thousand separator and decimal point
					DataFormat format = workbook.createDataFormat();

					// Format: 1,234,567
					balanceStyle.setDataFormat(format.getFormat("#,##0"));

					// Right alignment (optional)
					balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

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
				logger.info("No data found for M_CA2 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_CA2 Excel", e);
			return new byte[0];
		}
	}
	
	    //========================
		// FOR RESUB 
		//==========================
	
	// ─── View or edit detail page ───────────────────────────────────────────────
	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		return getViewOrEditPage(SNO, formMode, type, null);
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("BRRS/M_CA2");

		Date reportDate = null;
		String acctNo = null;
		if (request != null) {
			String asondateParam = request.getParameter("asondate");
			if (asondateParam == null) asondateParam = request.getParameter("reportDate");
			if (asondateParam == null) asondateParam = request.getParameter("todate");
			if (asondateParam != null && !asondateParam.trim().isEmpty()) {
				try {
					reportDate = new SimpleDateFormat("dd-MM-yyyy").parse(asondateParam.trim());
				} catch (Exception e) {
					try {
						reportDate = new SimpleDateFormat("dd/MM/yyyy").parse(asondateParam.trim());
					} catch (Exception e2) {
						try {
							reportDate = dateformat.parse(asondateParam.trim());
						} catch (Exception ignored) {}
					}
				}
			}
			acctNo = request.getParameter("acctNo");
		}
		if (acctNo == null || acctNo.trim().isEmpty()) {
			acctNo = SNO;
		}

		System.out.println("getViewOrEditPage: SNO=" + SNO + ", acctNo=" + acctNo + ", reportDate=" + reportDate + ", type=" + type);

		M_CA2_Detail_Entity la1Entity = null;

		if ("RESUB".equals(type) || "ARCHIVAL".equals(type)) {
			System.out.println("Inside RESUB/ARCHIVAL FETCH for date: " + reportDate);
			if (acctNo != null && !acctNo.trim().isEmpty()) {
				la1Entity = findByAcctNumberArch(acctNo.trim(), reportDate);
			}
			if (la1Entity == null && SNO != null && !SNO.trim().isEmpty()) {
				la1Entity = findByAcctNumberArch(SNO.trim(), reportDate);
			}
		} else {
			if (reportDate != null && acctNo != null && !acctNo.trim().isEmpty()) {
				try {
					List<M_CA2_Detail_Entity> list = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
							new Object[] { acctNo.trim(), reportDate }, new M_CA2DetailRowMapper());
					if (list != null && !list.isEmpty()) {
						la1Entity = list.get(0);
					}
				} catch (Exception ignored) {
				}
			}
			if (la1Entity == null && SNO != null && !SNO.trim().isEmpty()) {
				la1Entity = findBySno(SNO);
			}
		}

		if (la1Entity != null) {
			if (reportDate != null && (la1Entity.getReportDate() == null || !la1Entity.getReportDate().equals(reportDate))) {
				la1Entity.setReportDate(reportDate);
			}
			Date d = la1Entity.getReportDate() != null ? la1Entity.getReportDate() : reportDate;
			if (d != null) {
				mv.addObject("asondate", new SimpleDateFormat("dd/MM/yyyy").format(d));
			}
		} else if (reportDate != null) {
			mv.addObject("asondate", new SimpleDateFormat("dd/MM/yyyy").format(reportDate));
		}

		mv.addObject("Data", la1Entity);
		mv.addObject("reportId", "M_CA2");
		mv.addObject("reportid", "M_CA2");
		mv.addObject("menu", "M_CA2");
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

//	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
//		ModelAndView mv = new ModelAndView("BRRS/M_CA2"); // ✅ match the report name
//		System.out.println("Hello");
//		if (acctNo != null) {
//			M_CA2_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
//			if (la1Entity != null && la1Entity.getReportDate() != null) {
//				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
//				mv.addObject("asondate", formattedDate);
//			}
//			mv.addObject("Data", la1Entity);
//		}
//
//		mv.addObject("displaymode", "edit");
//		mv.addObject("formmode", formMode != null ? formMode : "edit");
//		return mv;
//	}

	// ─── Detail edit page ───────────────────────────────────────────────────────
	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_CA2"); // ✅ match the report name

		if (acctNo != null) {
			M_CA2_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
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
	
	//========================
	// FOR RESUB 
	//==========================
	
	// ─── Save detail edit ───────────────────────────────────────────────────────
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String Sno = request.getParameter("sno");
			if (Sno == null) {
				Sno = request.getParameter("SNO");
			}
			String acctNumber = request.getParameter("acctNumber");
			if (acctNumber == null) {
				acctNumber = request.getParameter("ACCT_NUMBER");
			}
			String acctBalanceInpula = request.getParameter("acctBalanceInPula");
			if (acctBalanceInpula == null) {
				acctBalanceInpula = request.getParameter("acctBalanceInpula");
			}
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");
			Date parsedReportDate = null;
			if (reportDateStr != null && !reportDateStr.trim().isEmpty()) {
				try {
					parsedReportDate = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr.trim());
				} catch (Exception e) {
					try {
						parsedReportDate = new SimpleDateFormat("dd-MM-yyyy").parse(reportDateStr.trim());
					} catch (Exception ignored) {}
				}
			}

			System.out.println("updateDetailEdit: Sno=" + Sno + ", acctNumber=" + acctNumber + ", date=" + reportDateStr);

			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			M_CA2_Detail_Entity existing = null;

			System.out.println("type is : " + type);

			if ("RESUB".equals(type)) {
				if (acctNumber != null && !acctNumber.trim().isEmpty()) {
					existing = findByAcctNumberArch(acctNumber.trim(), parsedReportDate);
				}
				if (existing == null && Sno != null && !Sno.trim().isEmpty()) {
					existing = findByAcctNumberArch(Sno.trim(), parsedReportDate);
				}
				if (existing == null && acctNumber != null && !acctNumber.trim().isEmpty()) {
					existing = findBySnoArch(acctNumber.trim());
				}
			} else {
				if (Sno != null && !Sno.trim().isEmpty()) {
					try {
						existing = findBySno(Sno.trim());
					} catch (Exception ignored) {
					}
				}
				if (existing == null && acctNumber != null && !acctNumber.trim().isEmpty()) {
					if (parsedReportDate != null) {
						try {
							List<M_CA2_Detail_Entity> list = jdbcTemplate.query(
									"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
									new Object[] { acctNumber.trim(), parsedReportDate }, new M_CA2DetailRowMapper());
							if (list != null && !list.isEmpty()) {
								existing = list.get(0);
							}
						} catch (Exception ignored) {}
					}
					if (existing == null) {
						try {
							List<M_CA2_Detail_Entity> list = jdbcTemplate.query(
									"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE ACCT_NUMBER = ?",
									new Object[] { acctNumber.trim() }, new M_CA2DetailRowMapper());
							if (list != null && !list.isEmpty()) {
								existing = list.get(0);
							}
						} catch (Exception ignored) {}
					}
				}
			}

			if (existing == null) {
				System.err.println("updateDetailEdit: Record not found for Sno=" + Sno + ", acctNumber=" + acctNumber);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Record not found for update (Account: " + (acctNumber != null ? acctNumber : Sno) + ")");
			}

			if (existing.getReportDate() == null && parsedReportDate != null) {
				existing.setReportDate(parsedReportDate);
			}

			M_CA2_Detail_Entity oldcopy = new M_CA2_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			// Update Account Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {

					existing.setAcctName(acctName);
					isChanged = true;
				}
			}

			// Update Balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

				if (existing.getAcctBalanceInPula() == null
						|| existing.getAcctBalanceInPula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInPula(newBalance);
					isChanged = true;
				}
			}

			// Save using JDBC
			if (isChanged) {

				System.out.println("Type in update block : " + type);

				if ("RESUB".equals(type)) {

					System.out.println("Inside RESUB UPDATE for acct: " + existing.getAcctNumber());

					int rowsUpdated = 0;
					Date dt = existing.getReportDate() != null ? existing.getReportDate() : parsedReportDate;
					if (dt != null) {
						try {
							rowsUpdated = jdbcTemplate.update(
									"UPDATE BRRS_M_CA2_ARCHIVALTABLE_DETAIL SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
									existing.getAcctName(),
									existing.getAcctBalanceInPula(),
									existing.getAcctNumber(),
									dt);
						} catch (Exception ex) {
							System.err.println("Warning updating archival detail with date: " + ex.getMessage());
						}
					} else {
						rowsUpdated = jdbcTemplate.update(
								"UPDATE BRRS_M_CA2_ARCHIVALTABLE_DETAIL SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?",
								existing.getAcctName(),
								existing.getAcctBalanceInPula(),
								existing.getAcctNumber());
					}
					System.out.println("Archival rows updated: " + rowsUpdated);

					String auditId = (existing.getAcctNumber() != null) ? existing.getAcctNumber() : (Sno != null ? Sno : "0");
					try {
						auditService.compareEntitiesmanual(
								oldcopy,
								existing,
								auditId,
								"M_CA2 Archival Screen",
								"BRRS_M_CA2_ARCHIVALTABLE_DETAIL");
					} catch (Exception auditEx) {
						System.err.println("Audit warning: " + auditEx.getMessage());
					}

				} else {

					jdbcTemplate.update(
							"UPDATE BRRS_M_CA2_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?",
							existing.getAcctName(),
							existing.getAcctBalanceInPula(),
							existing.getAcctNumber());

					String auditId = existing.getAcctNumber();
					try {
						auditService.compareEntitiesmanual(
								oldcopy,
								existing,
								auditId,
								"M_CA2 Screen",
								"BRRS_M_CA2_DETAILTABLE");
					} catch (Exception auditEx) {
						System.err.println("Audit warning: " + auditEx.getMessage());
					}
				}

				System.out.println("Record updated using JDBC");

				Date effectiveDate = (existing != null && existing.getReportDate() != null) ? existing.getReportDate() : parsedReportDate;
				String dateForProc = (effectiveDate != null) ? new SimpleDateFormat("dd-MM-yyyy").format(effectiveDate) : reportDateStr;
				Run_M_CA2_Procedure(dateForProc, type, entry);

				if ("RESUB".equals(type) && "NO".equals(entry)) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}

				return ResponseEntity.ok("Record updated successfully!");

			} else {

				if ("RESUB".equals(type) && "NO".equals(entry)) {
					Date effectiveDate = (existing != null && existing.getReportDate() != null) ? existing.getReportDate() : parsedReportDate;
					String dateForProc = (effectiveDate != null) ? new SimpleDateFormat("dd-MM-yyyy").format(effectiveDate) : reportDateStr;
					Run_M_CA2_Procedure(dateForProc, type, entry);
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}

				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
     	//========================
		// FOR RESUB 
		//==========================
	// ─── Trigger report regeneration ───────────────────────────────────────────
	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_M_CA2_Procedure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}
    	//========================
		// FOR RESUB 
		//==========================
	
	private void Run_M_CA2_Procedure(String reportDateStr, String type, String entry) {
		Date parsedDate = null;
		if (reportDateStr != null && !reportDateStr.trim().isEmpty()) {
			try {
				parsedDate = new SimpleDateFormat("dd-MM-yyyy").parse(reportDateStr.trim());
			} catch (Exception e) {
				try {
					parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr.trim());
				} catch (Exception ignored) {}
			}
		}
		if (parsedDate == null) {
			System.out.println("Error parsing date: " + reportDateStr + ". Post-commit logic aborted.");
			return;
		}

		final Date finalDate = parsedDate;
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					executeProcedureLogic(finalDate, type, entry);
				}
			});
		} else {
			executeProcedureLogic(finalDate, type, entry);
		}
	}

	private void transferArchivalDetailsToLive(java.sql.Date sqlDate) {
		try {
			Integer count = jdbcTemplate.queryForObject(
					"SELECT COUNT(*) FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
					Integer.class, sqlDate);
			if (count == null || count == 0) {
				System.out.println("No archival detail records found for " + sqlDate + "; skipping detail transfer.");
				return;
			}

			final Set<String> archCols = new HashSet<>();
			jdbcTemplate.query("SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE 1=0", rs -> {
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					archCols.add(md.getColumnName(i).toUpperCase());
				}
				return null;
			});

			final Set<String> liveCols = new HashSet<>();
			jdbcTemplate.query("SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE 1=0", rs -> {
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					liveCols.add(md.getColumnName(i).toUpperCase());
				}
				return null;
			});

			List<String> commonCols = new ArrayList<>();
			for (String col : archCols) {
				if (liveCols.contains(col) && !"SNO".equalsIgnoreCase(col)) {
					commonCols.add(col);
				}
			}

			if (commonCols.isEmpty()) {
				System.out.println("No common columns found between archival and live detail tables.");
				return;
			}

			int rowsDeleted = jdbcTemplate.update("DELETE FROM BRRS_M_CA2_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
			System.out.println("Detail rows deleted before transfer: " + rowsDeleted);

			String colsJoined = String.join(", ", commonCols);
			boolean liveHasSno = liveCols.contains("SNO");
			String insertSql;
			if (liveHasSno) {
				insertSql = "INSERT INTO BRRS_M_CA2_DETAILTABLE (SNO, " + colsJoined + ") "
						+ "SELECT ROWNUM AS SNO, " + colsJoined + " FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			} else {
				insertSql = "INSERT INTO BRRS_M_CA2_DETAILTABLE (" + colsJoined + ") "
						+ "SELECT " + colsJoined + " FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			}
			int rowsTransferred = jdbcTemplate.update(insertSql, sqlDate);
			System.out.println("Detail rows transferred from archival: " + rowsTransferred);
			jdbcTemplate.update("UPDATE BRRS_M_CA2_DETAILTABLE SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
		} catch (Exception e) {
			System.err.println("Warning in transferArchivalDetailsToLive: " + e.getMessage());
		}
	}

	private void executeProcedureLogic(Date reportDate, String type, String entry) {
		try {
			boolean isResubNoEntry = "RESUB".equals(type) && "NO".equals(entry);
			boolean shouldExecuteProcedure = !"RESUB".equals(type) || isResubNoEntry;

			String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
			java.sql.Date sqlDate = new java.sql.Date(reportDate.getTime());

			System.out.println("executeProcedureLogic: formattedDate = " + formattedDate + ", type = " + type + ", entry = " + entry);

			// Build columns part for all 32 rows
			StringBuilder columnsPart = new StringBuilder();
			int[] allRows = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 36, 41, 42, 43, 44, 45, 46, 47, 48, 49 };
			String[] tokens = { "PRODUCT", "AMOUNT_1", "AMOUNT_2" };
			for (int row : allRows) {
				for (String token : tokens) {
					columnsPart.append("R").append(row).append("_").append(token).append(", ");
				}
			}

			if (isResubNoEntry) {
				// 1. Safely transfer archival detail records into live detail table
				transferArchivalDetailsToLive(sqlDate);

				// 2. Check if live summary already has staged modified values
				Integer liveSumCount = jdbcTemplate.queryForObject(
						"SELECT COUNT(*) FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
						Integer.class, sqlDate);

				if (liveSumCount == null || liveSumCount == 0) {
					// Live summary is empty (e.g. triggered from Details view) -> restore from latest archival summary
					String maxVerSql = "SELECT NVL(MAX(REPORT_VERSION), 0) FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
					Integer currentMaxVersion = jdbcTemplate.queryForObject(maxVerSql, Integer.class, sqlDate);

					if (currentMaxVersion != null && currentMaxVersion > 0) {
						String copyArchSummarySql = "INSERT INTO BRRS_M_CA2_SUMMARYTABLE ("
								+ columnsPart
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
								+ "SELECT "
								+ columnsPart
								+ "TRUNC(REPORT_DATE), REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
								+ "FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
						int sumCopied = jdbcTemplate.update(copyArchSummarySql, sqlDate, currentMaxVersion);
						System.out.println("Archival summary restored to live summary: " + sumCopied);
					}
				} else {
					System.out.println("Live summary already contains modified staged values; preserving for procedure.");
				}
				jdbcTemplate.update("UPDATE BRRS_M_CA2_SUMMARYTABLE SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
			}

			// 6. Execute stored procedure
			if (shouldExecuteProcedure) {
				jdbcTemplate.update("BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;", formattedDate);
				System.out.println("BRRS_M_CA2_SUMMARY_PROCEDURE executed successfully for " + formattedDate);
			}

			// 7. Post-execution archival handling
			if (isResubNoEntry) {
				// Delete detail records from live table
				String deleteDetailAfterSql = "DELETE FROM BRRS_M_CA2_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				int rowsDeletedAfter = jdbcTemplate.update(deleteDetailAfterSql, sqlDate);
				System.out.println("Live detail records deleted after procedure: " + rowsDeletedAfter);

				// Query max version again
				String versionSql = "SELECT NVL(MAX(REPORT_VERSION), 0) FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				Integer maxVersion = jdbcTemplate.queryForObject(versionSql, Integer.class, sqlDate);
				int highestValue = (maxVersion != null ? maxVersion : 0) + 1;
				System.out.println("Archiving new summary with version: " + highestValue);

				// Insert new version into archival summary
				String archiveSummarySql = "INSERT INTO BRRS_M_CA2_ARCHIVALTABLE_SUMMARY ("
						+ columnsPart
						+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
						+ "SELECT "
						+ columnsPart
						+ "TRUNC(REPORT_DATE), ?, NVL(REPORT_FREQUENCY, 'MONTHLY'), NVL(REPORT_CODE, 'M_CA2'), NVL(REPORT_DESC, 'Capital Adequacy 2'), NVL(ENTITY_FLG, 'N'), NVL(MODIFY_FLG, 'N'), NVL(DEL_FLG, 'N'), SYSDATE "
						+ "FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				int rowsArchived = jdbcTemplate.update(archiveSummarySql, highestValue, sqlDate);
				System.out.println("Summary rows archived: " + rowsArchived);

				// Delete temporary live summary records
				String deleteSummaryAfterSql = "DELETE FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				int rowsDeletedSum = jdbcTemplate.update(deleteSummaryAfterSql, sqlDate);
				System.out.println("Live summary rows deleted after archiving: " + rowsDeletedSum);
			}
		} catch (Exception e) {
			System.err.println("Error in executeProcedureLogic: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	


//	@Transactional
//	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
//		try {
//			String Sno = request.getParameter("sno");
//			String acctNo = request.getParameter("acctNumber");
//			String provisionStr = request.getParameter("acctBalanceInPula");
//			String acctName = request.getParameter("acctName");
//			String reportDateStr = request.getParameter("reportDate");
//			
//			
//
//			logger.info("Received update for ACCT_NO: {}", acctNo);
//
//			M_CA2_Detail_Entity existing = getDetailByAcctNumber(acctNo);
//			if (existing == null) {
//				logger.warn("No record found for ACCT_NO: {}", acctNo);
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
//			}
//
//			// Create old copy for audit comparison
//			M_CA2_Detail_Entity oldcopy = new M_CA2_Detail_Entity();
//			BeanUtils.copyProperties(existing, oldcopy);
//
//			boolean isChanged = false;
//
//			if (acctName != null && !acctName.isEmpty()) {
//				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
//					existing.setAcctName(acctName);
//					isChanged = true;
//					logger.info("Account name updated to {}", acctName);
//				}
//			}
//
//			if (provisionStr != null && !provisionStr.isEmpty()) {
//				BigDecimal newProvision = new BigDecimal(provisionStr);
//				if (existing.getAcctBalanceInPula() == null
//						|| existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
//					existing.setAcctBalanceInPula(newProvision);
//					isChanged = true;
//					logger.info("Balance updated to {}", newProvision);
//				}
//			}
//
//			if (isChanged) {
//				jdbcTemplate.update(
//						"UPDATE BRRS_M_CA2_DETAILTABLE SET ACCT_NAME=?, ACCT_BALANCE_IN_PULA=? WHERE ACCT_NUMBER=?",
//						existing.getAcctName(), existing.getAcctBalanceInPula(), existing.getAcctNumber());
//
//				// Audit comparison
//				auditService.compareEntitiesmanual(oldcopy, existing, acctNo, "M_CA2 Detail Screen",
//						"BRRS_M_CA2_DETAIL");
//
//				logger.info("Record updated successfully for account {}", acctNo);
//
//				// Format date for procedure
//				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
//						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));
//
//				// Run summary procedure after commit
//
//				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//
//					@Override
//					public void afterCommit() {
//						try {
//
//							logger.info("Transaction committed — calling BRRS_M_CA2_SUMMARY_PROCEDURE({})",
//									formattedDate);
//
//							jdbcTemplate.update("BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;", formattedDate);
//
//							logger.info("Procedure executed successfully after commit.");
//
//						} catch (Exception e) {
//							logger.error("Error executing procedure after commit", e);
//						}
//					}
//				});
//
//				return ResponseEntity.ok("Record updated successfully!");
//			} else {
//				logger.info("No changes detected for ACCT_NO: {}", acctNo);
//				return ResponseEntity.ok("No changes were made.");
//			}
//
//		} catch (Exception e) {
//			logger.error("Error updating M_CA2 record", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error updating record: " + e.getMessage());
//		}
//	}

//Normal format Excel

	// ─── Summary Excel download ─────────────────────────────────────────────────
	public byte[] getM_CA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + todate);
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
// Redirecting to Archival
				return getExcelM_CA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// ✅ Redirecting to Resub Excel
				return BRRS_M_CA2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CA2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

// Fetch data

				List<M_CA2_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_CA2_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							Cell cell3, cell4;
							CellStyle originalStyle;

							// row6
							// Column C
							Cell cellBdate = row.createCell(2);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}

// ===== Row 10 / Col D =====
							row = sheet.getRow(9);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR10_amount_2() != null)
								cell4.setCellValue(record.getR10_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 11 / Col D =====
							row = sheet.getRow(10);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR11_amount_2() != null)
								cell4.setCellValue(record.getR11_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 13 / Col C =====
							row = sheet.getRow(12);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR13_amount_1() != null)
								cell3.setCellValue(record.getR13_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 14 / Col C =====
							row = sheet.getRow(13);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR14_amount_1() != null)
								cell3.setCellValue(record.getR14_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 15 / Col C =====
							row = sheet.getRow(14);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR15_amount_1() != null)
								cell3.setCellValue(record.getR15_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 16 / Col C =====
							row = sheet.getRow(15);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR16_amount_1() != null)
								cell3.setCellValue(record.getR16_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 18 / Col C =====
							row = sheet.getRow(17);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR18_amount_1() != null)
								cell3.setCellValue(record.getR18_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 19 / Col C =====
							row = sheet.getRow(18);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR19_amount_1() != null)
								cell3.setCellValue(record.getR19_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 20 / Col C =====
							row = sheet.getRow(19);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR20_amount_1() != null)
								cell3.setCellValue(record.getR20_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 21 / Col C =====
							row = sheet.getRow(20);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR21_amount_1() != null)
								cell3.setCellValue(record.getR21_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 22 / Col D =====
							row = sheet.getRow(21);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR22_amount_2() != null)
								cell4.setCellValue(record.getR22_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 23 / Col D =====
							row = sheet.getRow(22);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR23_amount_2() != null)
								cell4.setCellValue(record.getR23_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 24 / Col D =====
							row = sheet.getRow(23);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR24_amount_2() != null)
								cell4.setCellValue(record.getR24_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 25 / Col D =====
							row = sheet.getRow(24);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR25_amount_2() != null)
								cell4.setCellValue(record.getR25_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 31 / Col D =====
							row = sheet.getRow(30);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR31_amount_2() != null)
								cell4.setCellValue(record.getR31_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 32 / Col D =====
							row = sheet.getRow(31);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR32_amount_2() != null)
								cell4.setCellValue(record.getR32_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 33 / Col D =====
							row = sheet.getRow(32);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR33_amount_2() != null)
								cell4.setCellValue(record.getR33_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 34 / Col D =====
							row = sheet.getRow(33);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR34_amount_2() != null)
								cell4.setCellValue(record.getR34_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 41 / Col D =====
							row = sheet.getRow(40);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR41_amount_2() != null)
								cell4.setCellValue(record.getR41_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 42 / Col D =====
							row = sheet.getRow(41);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR42_amount_2() != null)
								cell4.setCellValue(record.getR42_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 43 / Col D =====
							row = sheet.getRow(42);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR43_amount_2() != null)
								cell4.setCellValue(record.getR43_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 44 / Col D =====
							row = sheet.getRow(43);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR44_amount_2() != null)
								cell4.setCellValue(record.getR44_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 45 / Col D =====
							row = sheet.getRow(44);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR45_amount_2() != null)
								cell4.setCellValue(record.getR45_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 46 / Col D =====
							row = sheet.getRow(45);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR46_amount_2() != null)
								cell4.setCellValue(record.getR46_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 47 / Col D =====
							row = sheet.getRow(46);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR47_amount_2() != null)
								cell4.setCellValue(record.getR47_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);
						}

						workbook.setForceFormulaRecalculation(true);
					} else {

					}

// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					// audit service summary format

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 SUMMARY", null,
								"BRRS_M_CA2_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

	// ─── Summary email Excel ────────────────────────────────────────────────────
	public byte[] BRRS_M_CA2EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
// Redirecting to Archival
				return BRRS_M_CA2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// ✅ Redirecting to Resub Excel
				return BRRS_M_CA2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_CA2_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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

				int startRow = 5;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_CA2_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell cell3, cell4;
						CellStyle originalStyle;

						// row6
						// Column C
						Cell cellBdate = row.createCell(2);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}

// ROW 10 = Common shares

// ===== Row 10 / Col D =====
						row = sheet.getRow(9);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR10_amount_2() != null)
							cell4.setCellValue(record.getR10_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 11 = Share premium resulting from the issue of common shares

// ===== Row 11 / Col D =====
						row = sheet.getRow(10);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR11_amount_2() != null)
							cell4.setCellValue(record.getR11_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 12 = Retained earnings

//ROW 13 = Retained earnings brought forward from the previous financial year

//===== Row 13 / Col C =====
						row = sheet.getRow(12);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR13_amount_1() != null)
							cell3.setCellValue(record.getR13_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 14 = Add: Interim profits (reviewed/audited by external auditor)

// ===== Row 14 / Col C =====
						row = sheet.getRow(13);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR14_amount_1() != null)
							cell3.setCellValue(record.getR14_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 15 = Less: Dividend paid (2024–25)

// ===== Row 15 / Col C =====
						row = sheet.getRow(14);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR15_amount_1() != null)
							cell3.setCellValue(record.getR15_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 16 = Less: Dividend paid in the current financial year

// ===== Row 16 / Col C =====
						row = sheet.getRow(15);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR16_amount_1() != null)
							cell3.setCellValue(record.getR16_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 17 = Net Retained Earnings

//ROW 18 = Accumulated other Comprehensive income and other disclosed reserves

// ===== Row 17 / Col D =====
						row = sheet.getRow(17);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR17_amount_2() != null)
							cell4.setCellValue(record.getR17_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 19 = Common shares issued by consolidated subsidiaries of the bank and held by third parties (Minority interest)

// ===== Row 19 / Col D =====
						row = sheet.getRow(18);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR22_amount_2() != null)
							cell4.setCellValue(record.getR22_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 20 = Regulatory adjustments applied in the calculation of CET1 Capital

// ===== Row 23 / Col D =====
						row = sheet.getRow(19);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR23_amount_2() != null)
							cell4.setCellValue(record.getR23_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 21 = IFRS Provisions Transitional Adjustments

// ===== Row 24 / Col D =====
						row = sheet.getRow(20);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR24_amount_2() != null)
							cell4.setCellValue(record.getR24_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 22 = Transitional Adjustment Amount Added Back to CET1

// ===== Row 25 / Col D =====
						row = sheet.getRow(21);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR25_amount_2() != null)
							cell4.setCellValue(record.getR25_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 23 = CET1 Capital (Lines 1 + 2 + 3 + 4 + 5 − 6)

//ROW R29 = Instruments issued by the bank that meet the criteria for inclusion in Additional Tier 1 Capital as per paragraph 4.9 of the Capital Directive

// ===== Row 31 / Col D =====
						row = sheet.getRow(28);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR31_amount_2() != null)
							cell4.setCellValue(record.getR31_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R30 = Stock surplus (Share premium) resulting from the issue of Additional Tier 1 capital instruments meeting all relevant criteria for inclusion

// ===== Row 32 / Col D =====
						row = sheet.getRow(29);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR32_amount_2() != null)
							cell4.setCellValue(record.getR32_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R31 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Additional Tier 1 capital and are not included in CET1, subject to terms and conditions in 3.5

// ===== Row 33 / Col D =====
						row = sheet.getRow(30);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR33_amount_2() != null)
							cell4.setCellValue(record.getR33_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R32 = Regulatory adjustments applied in the calculation of Additional Tier 1 Capital

// ===== Row 34 / Col D =====
						row = sheet.getRow(31);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR34_amount_2() != null)
							cell4.setCellValue(record.getR34_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R33 = Additional Tier 1 Capital (Line 9 + 10 + 11 + 12)

//ROW R34 = Total Tier 1 Capital (Line 7 + 13)

//ROW R39 = Instruments issued by the bank that meet the criteria for inclusion in Tier 2 capital (and are not included in Tier 1 capital)

// ===== Row 41 / Col D =====
						row = sheet.getRow(38);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR41_amount_2() != null)
							cell4.setCellValue(record.getR41_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R40 = Stock surplus (share premium) resulting from the issue of instruments included in Tier 2 capital

// ===== Row 42 / Col D =====
						row = sheet.getRow(39);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR42_amount_2() != null)
							cell4.setCellValue(record.getR42_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R41 = Unpublished Current Year's Profits

// ===== Row 43 / Col D =====
						row = sheet.getRow(40);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR43_amount_2() != null)
							cell4.setCellValue(record.getR43_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R42 = Tier 2 capital instruments (subject to gradual phase-out treatment)

// ===== Row 44 / Col D =====
						row = sheet.getRow(41);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR44_amount_2() != null)
							cell4.setCellValue(record.getR44_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R43 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Tier 2 capital and are not included in Tier 1 capital (minority interests)

// ===== Row 45 / Col D =====
						row = sheet.getRow(42);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR45_amount_2() != null)
							cell4.setCellValue(record.getR45_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R44 = General provisions / general loan-loss reserves eligible for inclusion in Tier 2, limited to a maximum of 1.25 percentage points of credit risk-weighted risk assets calculated under the standardised approach

// ===== Row 46 / Col D =====
						row = sheet.getRow(43);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR46_amount_2() != null)
							cell4.setCellValue(record.getR46_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R45 = Regulatory adjustments applied in the calculation of Tier 2 Capital

// ===== Row 47 / Col D =====
						row = sheet.getRow(44);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR47_amount_2() != null)
							cell4.setCellValue(record.getR47_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);
					}

					workbook.setForceFormulaRecalculation(true);
				} else {

				}

// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				// audit service summary email

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 EMAIL SUMMARY", null,
							"BRRS_M_CA2_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

	// ─── Archival summary Excel download ───────────────────────────────────────
	public byte[] getExcelM_CA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
// Redirecting to Archival
				return BRRS_M_CA2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA2_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA2 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
//This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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

//Create the font
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
//--- End of Style Definitions ---

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR18_amount_1() != null)
						cell3.setCellValue(record.getR18_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR19_amount_1() != null)
						cell3.setCellValue(record.getR19_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 20 / Col C =====
					row = sheet.getRow(19);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR20_amount_1() != null)
						cell3.setCellValue(record.getR20_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 21 / Col C =====
					row = sheet.getRow(20);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR21_amount_1() != null)
						cell3.setCellValue(record.getR21_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 22 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 23 / Col D =====
					row = sheet.getRow(22);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 24 / Col D =====
					row = sheet.getRow(23);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 25 / Col D =====
					row = sheet.getRow(24);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 31 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 32 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 33 / Col D =====
					row = sheet.getRow(32);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 34 / Col D =====
					row = sheet.getRow(33);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 41 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 42 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 43 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 44 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 45 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 46 / Col D =====
					row = sheet.getRow(45);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 47 / Col D =====
					row = sheet.getRow(46);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

//Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 ARCHIVAL SUMMARY", null,
						"BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

	// ─── Archival summary email Excel ──────────────────────────────────────────
	public byte[] BRRS_M_CA2ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA2_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ROW 10 = Common shares

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 11 = Share premium resulting from the issue of common shares

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 12 = Retained earnings

//ROW 13 = Retained earnings brought forward from the previous financial year

//===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 14 = Add: Interim profits (reviewed/audited by external auditor)

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 15 = Less: Dividend paid (2024–25)

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 16 = Less: Dividend paid in the current financial year

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 17 = Net Retained Earnings

//ROW 18 = Accumulated other Comprehensive income and other disclosed reserves

// ===== Row 17 / Col D =====
					row = sheet.getRow(17);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR17_amount_2() != null)
						cell4.setCellValue(record.getR17_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 19 = Common shares issued by consolidated subsidiaries of the bank and held by third parties (Minority interest)

// ===== Row 19 / Col D =====
					row = sheet.getRow(18);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 20 = Regulatory adjustments applied in the calculation of CET1 Capital

// ===== Row 23 / Col D =====
					row = sheet.getRow(19);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 21 = IFRS Provisions Transitional Adjustments

// ===== Row 24 / Col D =====
					row = sheet.getRow(20);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 22 = Transitional Adjustment Amount Added Back to CET1

// ===== Row 25 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 23 = CET1 Capital (Lines 1 + 2 + 3 + 4 + 5 − 6)

//ROW R29 = Instruments issued by the bank that meet the criteria for inclusion in Additional Tier 1 Capital as per paragraph 4.9 of the Capital Directive

// ===== Row 31 / Col D =====
					row = sheet.getRow(28);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R30 = Stock surplus (Share premium) resulting from the issue of Additional Tier 1 capital instruments meeting all relevant criteria for inclusion

// ===== Row 32 / Col D =====
					row = sheet.getRow(29);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R31 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Additional Tier 1 capital and are not included in CET1, subject to terms and conditions in 3.5

// ===== Row 33 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R32 = Regulatory adjustments applied in the calculation of Additional Tier 1 Capital

// ===== Row 34 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R33 = Additional Tier 1 Capital (Line 9 + 10 + 11 + 12)

//ROW R34 = Total Tier 1 Capital (Line 7 + 13)

//ROW R39 = Instruments issued by the bank that meet the criteria for inclusion in Tier 2 capital (and are not included in Tier 1 capital)

// ===== Row 41 / Col D =====
					row = sheet.getRow(38);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R40 = Stock surplus (share premium) resulting from the issue of instruments included in Tier 2 capital

// ===== Row 42 / Col D =====
					row = sheet.getRow(39);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R41 = Unpublished Current Year's Profits

// ===== Row 43 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R42 = Tier 2 capital instruments (subject to gradual phase-out treatment)

// ===== Row 44 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R43 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Tier 2 capital and are not included in Tier 1 capital (minority interests)

// ===== Row 45 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R44 = General provisions / general loan-loss reserves eligible for inclusion in Tier 2, limited to a maximum of 1.25 percentage points of credit risk-weighted risk assets calculated under the standardised approach

// ===== Row 46 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R45 = Regulatory adjustments applied in the calculation of Tier 2 Capital

// ===== Row 47 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary email

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

	// ─── Resub summary Excel download ──────────────────────────────────────────
	public byte[] BRRS_M_CA2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// ✅ Redirecting to Resub Excel
				return BRRS_M_CA2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA2_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA2 report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_CA2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR18_amount_1() != null)
						cell3.setCellValue(record.getR18_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR19_amount_1() != null)
						cell3.setCellValue(record.getR19_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 20 / Col C =====
					row = sheet.getRow(19);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR20_amount_1() != null)
						cell3.setCellValue(record.getR20_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 21 / Col C =====
					row = sheet.getRow(20);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR21_amount_1() != null)
						cell3.setCellValue(record.getR21_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 22 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 23 / Col D =====
					row = sheet.getRow(22);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 24 / Col D =====
					row = sheet.getRow(23);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 25 / Col D =====
					row = sheet.getRow(24);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 31 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 32 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 33 / Col D =====
					row = sheet.getRow(32);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 34 / Col D =====
					row = sheet.getRow(33);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 41 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 42 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 43 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 44 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 45 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 46 / Col D =====
					row = sheet.getRow(45);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 47 / Col D =====
					row = sheet.getRow(46);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service summary resub format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 RESUB SUMMARY", null,
						"BRRS_M_CA2_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}

	}

	// ─── Resub summary email Excel ──────────────────────────────────────────────
	public byte[] BRRS_M_CA2EmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA2_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ROW 10 = Common shares

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 11 = Share premium resulting from the issue of common shares

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 12 = Retained earnings

//ROW 13 = Retained earnings brought forward from the previous financial year

//===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 14 = Add: Interim profits (reviewed/audited by external auditor)

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 15 = Less: Dividend paid (2024–25)

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 16 = Less: Dividend paid in the current financial year

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 17 = Net Retained Earnings

//ROW 18 = Accumulated other Comprehensive income and other disclosed reserves

// ===== Row 17 / Col D =====
					row = sheet.getRow(17);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR17_amount_2() != null)
						cell4.setCellValue(record.getR17_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 19 = Common shares issued by consolidated subsidiaries of the bank and held by third parties (Minority interest)

// ===== Row 19 / Col D =====
					row = sheet.getRow(18);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 20 = Regulatory adjustments applied in the calculation of CET1 Capital

// ===== Row 23 / Col D =====
					row = sheet.getRow(19);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 21 = IFRS Provisions Transitional Adjustments

// ===== Row 24 / Col D =====
					row = sheet.getRow(20);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 22 = Transitional Adjustment Amount Added Back to CET1

// ===== Row 25 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 23 = CET1 Capital (Lines 1 + 2 + 3 + 4 + 5 − 6)

//ROW R29 = Instruments issued by the bank that meet the criteria for inclusion in Additional Tier 1 Capital as per paragraph 4.9 of the Capital Directive

// ===== Row 31 / Col D =====
					row = sheet.getRow(28);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R30 = Stock surplus (Share premium) resulting from the issue of Additional Tier 1 capital instruments meeting all relevant criteria for inclusion

// ===== Row 32 / Col D =====
					row = sheet.getRow(29);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R31 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Additional Tier 1 capital and are not included in CET1, subject to terms and conditions in 3.5

// ===== Row 33 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R32 = Regulatory adjustments applied in the calculation of Additional Tier 1 Capital

// ===== Row 34 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R33 = Additional Tier 1 Capital (Line 9 + 10 + 11 + 12)

//ROW R34 = Total Tier 1 Capital (Line 7 + 13)

//ROW R39 = Instruments issued by the bank that meet the criteria for inclusion in Tier 2 capital (and are not included in Tier 1 capital)

// ===== Row 41 / Col D =====
					row = sheet.getRow(38);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R40 = Stock surplus (share premium) resulting from the issue of instruments included in Tier 2 capital

// ===== Row 42 / Col D =====
					row = sheet.getRow(39);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R41 = Unpublished Current Year's Profits

// ===== Row 43 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R42 = Tier 2 capital instruments (subject to gradual phase-out treatment)

// ===== Row 44 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R43 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Tier 2 capital and are not included in Tier 1 capital (minority interests)

// ===== Row 45 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R44 = General provisions / general loan-loss reserves eligible for inclusion in Tier 2, limited to a maximum of 1.25 percentage points of credit risk-weighted risk assets calculated under the standardised approach

// ===== Row 46 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R45 = Regulatory adjustments applied in the calculation of Tier 2 Capital

// ===== Row 47 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service summary resub email

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 EMAIL RESUB SUMMARY", null,
						"BRRS_M_CA2_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Summary_Entity
	// =========================================================
	public static class M_CA2_Summary_Entity {
		private String r10_product;
		private BigDecimal r10_amount_1;
		private BigDecimal r10_amount_2;
		private String r11_product;
		private BigDecimal r11_amount_1;
		private BigDecimal r11_amount_2;
		private String r12_product;
		private BigDecimal r12_amount_1;
		private BigDecimal r12_amount_2;
		private String r13_product;
		private BigDecimal r13_amount_1;
		private BigDecimal r13_amount_2;
		private String r14_product;
		private BigDecimal r14_amount_1;
		private BigDecimal r14_amount_2;
		private String r15_product;
		private BigDecimal r15_amount_1;
		private BigDecimal r15_amount_2;
		private String r16_product;
		private BigDecimal r16_amount_1;
		private BigDecimal r16_amount_2;
		private String r17_product;
		private BigDecimal r17_amount_1;
		private BigDecimal r17_amount_2;
		private String r18_product;
		private BigDecimal r18_amount_1;
		private BigDecimal r18_amount_2;
		private String r19_product;
		private BigDecimal r19_amount_1;
		private BigDecimal r19_amount_2;
		private String r20_product;
		private BigDecimal r20_amount_1;
		private BigDecimal r20_amount_2;
		private String r21_product;
		private BigDecimal r21_amount_1;
		private BigDecimal r21_amount_2;
		private String r22_product;
		private BigDecimal r22_amount_1;
		private BigDecimal r22_amount_2;
		private String r23_product;
		private BigDecimal r23_amount_1;
		private BigDecimal r23_amount_2;
		private String r24_product;
		private BigDecimal r24_amount_1;
		private BigDecimal r24_amount_2;
		private String r25_product;
		private BigDecimal r25_amount_1;
		private BigDecimal r25_amount_2;
		private String r26_product;
		private BigDecimal r26_amount_1;
		private BigDecimal r26_amount_2;
		private String r31_product;
		private BigDecimal r31_amount_1;
		private BigDecimal r31_amount_2;
		private String r32_product;
		private BigDecimal r32_amount_1;
		private BigDecimal r32_amount_2;
		private String r33_product;
		private BigDecimal r33_amount_1;
		private BigDecimal r33_amount_2;
		private String r34_product;
		private BigDecimal r34_amount_1;
		private BigDecimal r34_amount_2;
		private String r35_product;
		private BigDecimal r35_amount_1;
		private BigDecimal r35_amount_2;
		private String r36_product;
		private BigDecimal r36_amount_1;
		private BigDecimal r36_amount_2;
		private String r41_product;
		private BigDecimal r41_amount_1;
		private BigDecimal r41_amount_2;
		private String r42_product;
		private BigDecimal r42_amount_1;
		private BigDecimal r42_amount_2;
		private String r43_product;
		private BigDecimal r43_amount_1;
		private BigDecimal r43_amount_2;
		private String r44_product;
		private BigDecimal r44_amount_1;
		private BigDecimal r44_amount_2;
		private String r45_product;
		private BigDecimal r45_amount_1;
		private BigDecimal r45_amount_2;
		private String r46_product;
		private BigDecimal r46_amount_1;
		private BigDecimal r46_amount_2;
		private String r47_product;
		private BigDecimal r47_amount_1;
		private BigDecimal r47_amount_2;
		private String r48_product;
		private BigDecimal r48_amount_1;
		private BigDecimal r48_amount_2;
		private String r49_product;
		private BigDecimal r49_amount_1;
		private BigDecimal r49_amount_2;

		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() { return r10_product; }
		public void setR10_product(String r10_product) { this.r10_product = r10_product; }
		public BigDecimal getR10_amount_1() { return r10_amount_1; }
		public void setR10_amount_1(BigDecimal r10_amount_1) { this.r10_amount_1 = r10_amount_1; }
		public BigDecimal getR10_amount_2() { return r10_amount_2; }
		public void setR10_amount_2(BigDecimal r10_amount_2) { this.r10_amount_2 = r10_amount_2; }
		public String getR11_product() { return r11_product; }
		public void setR11_product(String r11_product) { this.r11_product = r11_product; }
		public BigDecimal getR11_amount_1() { return r11_amount_1; }
		public void setR11_amount_1(BigDecimal r11_amount_1) { this.r11_amount_1 = r11_amount_1; }
		public BigDecimal getR11_amount_2() { return r11_amount_2; }
		public void setR11_amount_2(BigDecimal r11_amount_2) { this.r11_amount_2 = r11_amount_2; }
		public String getR12_product() { return r12_product; }
		public void setR12_product(String r12_product) { this.r12_product = r12_product; }
		public BigDecimal getR12_amount_1() { return r12_amount_1; }
		public void setR12_amount_1(BigDecimal r12_amount_1) { this.r12_amount_1 = r12_amount_1; }
		public BigDecimal getR12_amount_2() { return r12_amount_2; }
		public void setR12_amount_2(BigDecimal r12_amount_2) { this.r12_amount_2 = r12_amount_2; }
		public String getR13_product() { return r13_product; }
		public void setR13_product(String r13_product) { this.r13_product = r13_product; }
		public BigDecimal getR13_amount_1() { return r13_amount_1; }
		public void setR13_amount_1(BigDecimal r13_amount_1) { this.r13_amount_1 = r13_amount_1; }
		public BigDecimal getR13_amount_2() { return r13_amount_2; }
		public void setR13_amount_2(BigDecimal r13_amount_2) { this.r13_amount_2 = r13_amount_2; }
		public String getR14_product() { return r14_product; }
		public void setR14_product(String r14_product) { this.r14_product = r14_product; }
		public BigDecimal getR14_amount_1() { return r14_amount_1; }
		public void setR14_amount_1(BigDecimal r14_amount_1) { this.r14_amount_1 = r14_amount_1; }
		public BigDecimal getR14_amount_2() { return r14_amount_2; }
		public void setR14_amount_2(BigDecimal r14_amount_2) { this.r14_amount_2 = r14_amount_2; }
		public String getR15_product() { return r15_product; }
		public void setR15_product(String r15_product) { this.r15_product = r15_product; }
		public BigDecimal getR15_amount_1() { return r15_amount_1; }
		public void setR15_amount_1(BigDecimal r15_amount_1) { this.r15_amount_1 = r15_amount_1; }
		public BigDecimal getR15_amount_2() { return r15_amount_2; }
		public void setR15_amount_2(BigDecimal r15_amount_2) { this.r15_amount_2 = r15_amount_2; }
		public String getR16_product() { return r16_product; }
		public void setR16_product(String r16_product) { this.r16_product = r16_product; }
		public BigDecimal getR16_amount_1() { return r16_amount_1; }
		public void setR16_amount_1(BigDecimal r16_amount_1) { this.r16_amount_1 = r16_amount_1; }
		public BigDecimal getR16_amount_2() { return r16_amount_2; }
		public void setR16_amount_2(BigDecimal r16_amount_2) { this.r16_amount_2 = r16_amount_2; }
		public String getR17_product() { return r17_product; }
		public void setR17_product(String r17_product) { this.r17_product = r17_product; }
		public BigDecimal getR17_amount_1() { return r17_amount_1; }
		public void setR17_amount_1(BigDecimal r17_amount_1) { this.r17_amount_1 = r17_amount_1; }
		public BigDecimal getR17_amount_2() { return r17_amount_2; }
		public void setR17_amount_2(BigDecimal r17_amount_2) { this.r17_amount_2 = r17_amount_2; }
		public String getR18_product() { return r18_product; }
		public void setR18_product(String r18_product) { this.r18_product = r18_product; }
		public BigDecimal getR18_amount_1() { return r18_amount_1; }
		public void setR18_amount_1(BigDecimal r18_amount_1) { this.r18_amount_1 = r18_amount_1; }
		public BigDecimal getR18_amount_2() { return r18_amount_2; }
		public void setR18_amount_2(BigDecimal r18_amount_2) { this.r18_amount_2 = r18_amount_2; }
		public String getR19_product() { return r19_product; }
		public void setR19_product(String r19_product) { this.r19_product = r19_product; }
		public BigDecimal getR19_amount_1() { return r19_amount_1; }
		public void setR19_amount_1(BigDecimal r19_amount_1) { this.r19_amount_1 = r19_amount_1; }
		public BigDecimal getR19_amount_2() { return r19_amount_2; }
		public void setR19_amount_2(BigDecimal r19_amount_2) { this.r19_amount_2 = r19_amount_2; }
		public String getR20_product() { return r20_product; }
		public void setR20_product(String r20_product) { this.r20_product = r20_product; }
		public BigDecimal getR20_amount_1() { return r20_amount_1; }
		public void setR20_amount_1(BigDecimal r20_amount_1) { this.r20_amount_1 = r20_amount_1; }
		public BigDecimal getR20_amount_2() { return r20_amount_2; }
		public void setR20_amount_2(BigDecimal r20_amount_2) { this.r20_amount_2 = r20_amount_2; }
		public String getR21_product() { return r21_product; }
		public void setR21_product(String r21_product) { this.r21_product = r21_product; }
		public BigDecimal getR21_amount_1() { return r21_amount_1; }
		public void setR21_amount_1(BigDecimal r21_amount_1) { this.r21_amount_1 = r21_amount_1; }
		public BigDecimal getR21_amount_2() { return r21_amount_2; }
		public void setR21_amount_2(BigDecimal r21_amount_2) { this.r21_amount_2 = r21_amount_2; }
		public String getR22_product() { return r22_product; }
		public void setR22_product(String r22_product) { this.r22_product = r22_product; }
		public BigDecimal getR22_amount_1() { return r22_amount_1; }
		public void setR22_amount_1(BigDecimal r22_amount_1) { this.r22_amount_1 = r22_amount_1; }
		public BigDecimal getR22_amount_2() { return r22_amount_2; }
		public void setR22_amount_2(BigDecimal r22_amount_2) { this.r22_amount_2 = r22_amount_2; }
		public String getR23_product() { return r23_product; }
		public void setR23_product(String r23_product) { this.r23_product = r23_product; }
		public BigDecimal getR23_amount_1() { return r23_amount_1; }
		public void setR23_amount_1(BigDecimal r23_amount_1) { this.r23_amount_1 = r23_amount_1; }
		public BigDecimal getR23_amount_2() { return r23_amount_2; }
		public void setR23_amount_2(BigDecimal r23_amount_2) { this.r23_amount_2 = r23_amount_2; }
		public String getR24_product() { return r24_product; }
		public void setR24_product(String r24_product) { this.r24_product = r24_product; }
		public BigDecimal getR24_amount_1() { return r24_amount_1; }
		public void setR24_amount_1(BigDecimal r24_amount_1) { this.r24_amount_1 = r24_amount_1; }
		public BigDecimal getR24_amount_2() { return r24_amount_2; }
		public void setR24_amount_2(BigDecimal r24_amount_2) { this.r24_amount_2 = r24_amount_2; }
		public String getR25_product() { return r25_product; }
		public void setR25_product(String r25_product) { this.r25_product = r25_product; }
		public BigDecimal getR25_amount_1() { return r25_amount_1; }
		public void setR25_amount_1(BigDecimal r25_amount_1) { this.r25_amount_1 = r25_amount_1; }
		public BigDecimal getR25_amount_2() { return r25_amount_2; }
		public void setR25_amount_2(BigDecimal r25_amount_2) { this.r25_amount_2 = r25_amount_2; }
		public String getR26_product() { return r26_product; }
		public void setR26_product(String r26_product) { this.r26_product = r26_product; }
		public BigDecimal getR26_amount_1() { return r26_amount_1; }
		public void setR26_amount_1(BigDecimal r26_amount_1) { this.r26_amount_1 = r26_amount_1; }
		public BigDecimal getR26_amount_2() { return r26_amount_2; }
		public void setR26_amount_2(BigDecimal r26_amount_2) { this.r26_amount_2 = r26_amount_2; }
		public String getR31_product() { return r31_product; }
		public void setR31_product(String r31_product) { this.r31_product = r31_product; }
		public BigDecimal getR31_amount_1() { return r31_amount_1; }
		public void setR31_amount_1(BigDecimal r31_amount_1) { this.r31_amount_1 = r31_amount_1; }
		public BigDecimal getR31_amount_2() { return r31_amount_2; }
		public void setR31_amount_2(BigDecimal r31_amount_2) { this.r31_amount_2 = r31_amount_2; }
		public String getR32_product() { return r32_product; }
		public void setR32_product(String r32_product) { this.r32_product = r32_product; }
		public BigDecimal getR32_amount_1() { return r32_amount_1; }
		public void setR32_amount_1(BigDecimal r32_amount_1) { this.r32_amount_1 = r32_amount_1; }
		public BigDecimal getR32_amount_2() { return r32_amount_2; }
		public void setR32_amount_2(BigDecimal r32_amount_2) { this.r32_amount_2 = r32_amount_2; }
		public String getR33_product() { return r33_product; }
		public void setR33_product(String r33_product) { this.r33_product = r33_product; }
		public BigDecimal getR33_amount_1() { return r33_amount_1; }
		public void setR33_amount_1(BigDecimal r33_amount_1) { this.r33_amount_1 = r33_amount_1; }
		public BigDecimal getR33_amount_2() { return r33_amount_2; }
		public void setR33_amount_2(BigDecimal r33_amount_2) { this.r33_amount_2 = r33_amount_2; }
		public String getR34_product() { return r34_product; }
		public void setR34_product(String r34_product) { this.r34_product = r34_product; }
		public BigDecimal getR34_amount_1() { return r34_amount_1; }
		public void setR34_amount_1(BigDecimal r34_amount_1) { this.r34_amount_1 = r34_amount_1; }
		public BigDecimal getR34_amount_2() { return r34_amount_2; }
		public void setR34_amount_2(BigDecimal r34_amount_2) { this.r34_amount_2 = r34_amount_2; }
		public String getR35_product() { return r35_product; }
		public void setR35_product(String r35_product) { this.r35_product = r35_product; }
		public BigDecimal getR35_amount_1() { return r35_amount_1; }
		public void setR35_amount_1(BigDecimal r35_amount_1) { this.r35_amount_1 = r35_amount_1; }
		public BigDecimal getR35_amount_2() { return r35_amount_2; }
		public void setR35_amount_2(BigDecimal r35_amount_2) { this.r35_amount_2 = r35_amount_2; }
		public String getR36_product() { return r36_product; }
		public void setR36_product(String r36_product) { this.r36_product = r36_product; }
		public BigDecimal getR36_amount_1() { return r36_amount_1; }
		public void setR36_amount_1(BigDecimal r36_amount_1) { this.r36_amount_1 = r36_amount_1; }
		public BigDecimal getR36_amount_2() { return r36_amount_2; }
		public void setR36_amount_2(BigDecimal r36_amount_2) { this.r36_amount_2 = r36_amount_2; }
		public String getR41_product() { return r41_product; }
		public void setR41_product(String r41_product) { this.r41_product = r41_product; }
		public BigDecimal getR41_amount_1() { return r41_amount_1; }
		public void setR41_amount_1(BigDecimal r41_amount_1) { this.r41_amount_1 = r41_amount_1; }
		public BigDecimal getR41_amount_2() { return r41_amount_2; }
		public void setR41_amount_2(BigDecimal r41_amount_2) { this.r41_amount_2 = r41_amount_2; }
		public String getR42_product() { return r42_product; }
		public void setR42_product(String r42_product) { this.r42_product = r42_product; }
		public BigDecimal getR42_amount_1() { return r42_amount_1; }
		public void setR42_amount_1(BigDecimal r42_amount_1) { this.r42_amount_1 = r42_amount_1; }
		public BigDecimal getR42_amount_2() { return r42_amount_2; }
		public void setR42_amount_2(BigDecimal r42_amount_2) { this.r42_amount_2 = r42_amount_2; }
		public String getR43_product() { return r43_product; }
		public void setR43_product(String r43_product) { this.r43_product = r43_product; }
		public BigDecimal getR43_amount_1() { return r43_amount_1; }
		public void setR43_amount_1(BigDecimal r43_amount_1) { this.r43_amount_1 = r43_amount_1; }
		public BigDecimal getR43_amount_2() { return r43_amount_2; }
		public void setR43_amount_2(BigDecimal r43_amount_2) { this.r43_amount_2 = r43_amount_2; }
		public String getR44_product() { return r44_product; }
		public void setR44_product(String r44_product) { this.r44_product = r44_product; }
		public BigDecimal getR44_amount_1() { return r44_amount_1; }
		public void setR44_amount_1(BigDecimal r44_amount_1) { this.r44_amount_1 = r44_amount_1; }
		public BigDecimal getR44_amount_2() { return r44_amount_2; }
		public void setR44_amount_2(BigDecimal r44_amount_2) { this.r44_amount_2 = r44_amount_2; }
		public String getR45_product() { return r45_product; }
		public void setR45_product(String r45_product) { this.r45_product = r45_product; }
		public BigDecimal getR45_amount_1() { return r45_amount_1; }
		public void setR45_amount_1(BigDecimal r45_amount_1) { this.r45_amount_1 = r45_amount_1; }
		public BigDecimal getR45_amount_2() { return r45_amount_2; }
		public void setR45_amount_2(BigDecimal r45_amount_2) { this.r45_amount_2 = r45_amount_2; }
		public String getR46_product() { return r46_product; }
		public void setR46_product(String r46_product) { this.r46_product = r46_product; }
		public BigDecimal getR46_amount_1() { return r46_amount_1; }
		public void setR46_amount_1(BigDecimal r46_amount_1) { this.r46_amount_1 = r46_amount_1; }
		public BigDecimal getR46_amount_2() { return r46_amount_2; }
		public void setR46_amount_2(BigDecimal r46_amount_2) { this.r46_amount_2 = r46_amount_2; }
		public String getR47_product() { return r47_product; }
		public void setR47_product(String r47_product) { this.r47_product = r47_product; }
		public BigDecimal getR47_amount_1() { return r47_amount_1; }
		public void setR47_amount_1(BigDecimal r47_amount_1) { this.r47_amount_1 = r47_amount_1; }
		public BigDecimal getR47_amount_2() { return r47_amount_2; }
		public void setR47_amount_2(BigDecimal r47_amount_2) { this.r47_amount_2 = r47_amount_2; }
		public String getR48_product() { return r48_product; }
		public void setR48_product(String r48_product) { this.r48_product = r48_product; }
		public BigDecimal getR48_amount_1() { return r48_amount_1; }
		public void setR48_amount_1(BigDecimal r48_amount_1) { this.r48_amount_1 = r48_amount_1; }
		public BigDecimal getR48_amount_2() { return r48_amount_2; }
		public void setR48_amount_2(BigDecimal r48_amount_2) { this.r48_amount_2 = r48_amount_2; }
		public String getR49_product() { return r49_product; }
		public void setR49_product(String r49_product) { this.r49_product = r49_product; }
		public BigDecimal getR49_amount_1() { return r49_amount_1; }
		public void setR49_amount_1(BigDecimal r49_amount_1) { this.r49_amount_1 = r49_amount_1; }
		public BigDecimal getR49_amount_2() { return r49_amount_2; }
		public void setR49_amount_2(BigDecimal r49_amount_2) { this.r49_amount_2 = r49_amount_2; }

		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public String getReport_version() { return report_version; }
		public void setReport_version(String report_version) { this.report_version = report_version; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// =========================================================
	// RowMapper: M_CA2_Summary_Entity
	// =========================================================
	class M_CA2SummaryRowMapper implements RowMapper<M_CA2_Summary_Entity> {
		@Override
		public M_CA2_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Summary_Entity obj = new M_CA2_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_amount_1(rs.getBigDecimal("R10_AMOUNT_1"));
			obj.setR10_amount_2(rs.getBigDecimal("R10_AMOUNT_2"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_1(rs.getBigDecimal("R11_AMOUNT_1"));
			obj.setR11_amount_2(rs.getBigDecimal("R11_AMOUNT_2"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_1(rs.getBigDecimal("R12_AMOUNT_1"));
			obj.setR12_amount_2(rs.getBigDecimal("R12_AMOUNT_2"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_1(rs.getBigDecimal("R13_AMOUNT_1"));
			obj.setR13_amount_2(rs.getBigDecimal("R13_AMOUNT_2"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_1(rs.getBigDecimal("R14_AMOUNT_1"));
			obj.setR14_amount_2(rs.getBigDecimal("R14_AMOUNT_2"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_1(rs.getBigDecimal("R15_AMOUNT_1"));
			obj.setR15_amount_2(rs.getBigDecimal("R15_AMOUNT_2"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_1(rs.getBigDecimal("R16_AMOUNT_1"));
			obj.setR16_amount_2(rs.getBigDecimal("R16_AMOUNT_2"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_1(rs.getBigDecimal("R17_AMOUNT_1"));
			obj.setR17_amount_2(rs.getBigDecimal("R17_AMOUNT_2"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_1(rs.getBigDecimal("R18_AMOUNT_1"));
			obj.setR18_amount_2(rs.getBigDecimal("R18_AMOUNT_2"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_1(rs.getBigDecimal("R19_AMOUNT_1"));
			obj.setR19_amount_2(rs.getBigDecimal("R19_AMOUNT_2"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_1(rs.getBigDecimal("R20_AMOUNT_1"));
			obj.setR20_amount_2(rs.getBigDecimal("R20_AMOUNT_2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_1(rs.getBigDecimal("R21_AMOUNT_1"));
			obj.setR21_amount_2(rs.getBigDecimal("R21_AMOUNT_2"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_1(rs.getBigDecimal("R22_AMOUNT_1"));
			obj.setR22_amount_2(rs.getBigDecimal("R22_AMOUNT_2"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_1(rs.getBigDecimal("R23_AMOUNT_1"));
			obj.setR23_amount_2(rs.getBigDecimal("R23_AMOUNT_2"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_1(rs.getBigDecimal("R24_AMOUNT_1"));
			obj.setR24_amount_2(rs.getBigDecimal("R24_AMOUNT_2"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_amount_1(rs.getBigDecimal("R25_AMOUNT_1"));
			obj.setR25_amount_2(rs.getBigDecimal("R25_AMOUNT_2"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_1(rs.getBigDecimal("R26_AMOUNT_1"));
			obj.setR26_amount_2(rs.getBigDecimal("R26_AMOUNT_2"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_1(rs.getBigDecimal("R31_AMOUNT_1"));
			obj.setR31_amount_2(rs.getBigDecimal("R31_AMOUNT_2"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_1(rs.getBigDecimal("R32_AMOUNT_1"));
			obj.setR32_amount_2(rs.getBigDecimal("R32_AMOUNT_2"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_1(rs.getBigDecimal("R33_AMOUNT_1"));
			obj.setR33_amount_2(rs.getBigDecimal("R33_AMOUNT_2"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_1(rs.getBigDecimal("R34_AMOUNT_1"));
			obj.setR34_amount_2(rs.getBigDecimal("R34_AMOUNT_2"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_1(rs.getBigDecimal("R35_AMOUNT_1"));
			obj.setR35_amount_2(rs.getBigDecimal("R35_AMOUNT_2"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_1(rs.getBigDecimal("R36_AMOUNT_1"));
			obj.setR36_amount_2(rs.getBigDecimal("R36_AMOUNT_2"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_1(rs.getBigDecimal("R41_AMOUNT_1"));
			obj.setR41_amount_2(rs.getBigDecimal("R41_AMOUNT_2"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_1(rs.getBigDecimal("R42_AMOUNT_1"));
			obj.setR42_amount_2(rs.getBigDecimal("R42_AMOUNT_2"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_1(rs.getBigDecimal("R43_AMOUNT_1"));
			obj.setR43_amount_2(rs.getBigDecimal("R43_AMOUNT_2"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_1(rs.getBigDecimal("R44_AMOUNT_1"));
			obj.setR44_amount_2(rs.getBigDecimal("R44_AMOUNT_2"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_1(rs.getBigDecimal("R45_AMOUNT_1"));
			obj.setR45_amount_2(rs.getBigDecimal("R45_AMOUNT_2"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_1(rs.getBigDecimal("R46_AMOUNT_1"));
			obj.setR46_amount_2(rs.getBigDecimal("R46_AMOUNT_2"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_1(rs.getBigDecimal("R47_AMOUNT_1"));
			obj.setR47_amount_2(rs.getBigDecimal("R47_AMOUNT_2"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_1(rs.getBigDecimal("R48_AMOUNT_1"));
			obj.setR48_amount_2(rs.getBigDecimal("R48_AMOUNT_2"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_1(rs.getBigDecimal("R49_AMOUNT_1"));
			obj.setR49_amount_2(rs.getBigDecimal("R49_AMOUNT_2"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getString("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Archival_Summary_Entity
	// =========================================================
	public static class M_CA2_Archival_Summary_Entity {
		private String r10_product;
		private BigDecimal r10_amount_1;
		private BigDecimal r10_amount_2;
		private String r11_product;
		private BigDecimal r11_amount_1;
		private BigDecimal r11_amount_2;
		private String r12_product;
		private BigDecimal r12_amount_1;
		private BigDecimal r12_amount_2;
		private String r13_product;
		private BigDecimal r13_amount_1;
		private BigDecimal r13_amount_2;
		private String r14_product;
		private BigDecimal r14_amount_1;
		private BigDecimal r14_amount_2;
		private String r15_product;
		private BigDecimal r15_amount_1;
		private BigDecimal r15_amount_2;
		private String r16_product;
		private BigDecimal r16_amount_1;
		private BigDecimal r16_amount_2;
		private String r17_product;
		private BigDecimal r17_amount_1;
		private BigDecimal r17_amount_2;
		private String r18_product;
		private BigDecimal r18_amount_1;
		private BigDecimal r18_amount_2;
		private String r19_product;
		private BigDecimal r19_amount_1;
		private BigDecimal r19_amount_2;
		private String r20_product;
		private BigDecimal r20_amount_1;
		private BigDecimal r20_amount_2;
		private String r21_product;
		private BigDecimal r21_amount_1;
		private BigDecimal r21_amount_2;
		private String r22_product;
		private BigDecimal r22_amount_1;
		private BigDecimal r22_amount_2;
		private String r23_product;
		private BigDecimal r23_amount_1;
		private BigDecimal r23_amount_2;
		private String r24_product;
		private BigDecimal r24_amount_1;
		private BigDecimal r24_amount_2;
		private String r25_product;
		private BigDecimal r25_amount_1;
		private BigDecimal r25_amount_2;
		private String r26_product;
		private BigDecimal r26_amount_1;
		private BigDecimal r26_amount_2;
		private String r31_product;
		private BigDecimal r31_amount_1;
		private BigDecimal r31_amount_2;
		private String r32_product;
		private BigDecimal r32_amount_1;
		private BigDecimal r32_amount_2;
		private String r33_product;
		private BigDecimal r33_amount_1;
		private BigDecimal r33_amount_2;
		private String r34_product;
		private BigDecimal r34_amount_1;
		private BigDecimal r34_amount_2;
		private String r35_product;
		private BigDecimal r35_amount_1;
		private BigDecimal r35_amount_2;
		private String r36_product;
		private BigDecimal r36_amount_1;
		private BigDecimal r36_amount_2;
		private String r41_product;
		private BigDecimal r41_amount_1;
		private BigDecimal r41_amount_2;
		private String r42_product;
		private BigDecimal r42_amount_1;
		private BigDecimal r42_amount_2;
		private String r43_product;
		private BigDecimal r43_amount_1;
		private BigDecimal r43_amount_2;
		private String r44_product;
		private BigDecimal r44_amount_1;
		private BigDecimal r44_amount_2;
		private String r45_product;
		private BigDecimal r45_amount_1;
		private BigDecimal r45_amount_2;
		private String r46_product;
		private BigDecimal r46_amount_1;
		private BigDecimal r46_amount_2;
		private String r47_product;
		private BigDecimal r47_amount_1;
		private BigDecimal r47_amount_2;
		private String r48_product;
		private BigDecimal r48_amount_1;
		private BigDecimal r48_amount_2;
		private String r49_product;
		private BigDecimal r49_amount_1;
		private BigDecimal r49_amount_2;
		private java.util.Date report_date;
		private BigDecimal report_version;
		private java.util.Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_amount_1() {
			return r10_amount_1;
		}

		public void setR10_amount_1(BigDecimal r10_amount_1) {
			this.r10_amount_1 = r10_amount_1;
		}

		public BigDecimal getR10_amount_2() {
			return r10_amount_2;
		}

		public void setR10_amount_2(BigDecimal r10_amount_2) {
			this.r10_amount_2 = r10_amount_2;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_amount_1() {
			return r11_amount_1;
		}

		public void setR11_amount_1(BigDecimal r11_amount_1) {
			this.r11_amount_1 = r11_amount_1;
		}

		public BigDecimal getR11_amount_2() {
			return r11_amount_2;
		}

		public void setR11_amount_2(BigDecimal r11_amount_2) {
			this.r11_amount_2 = r11_amount_2;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_amount_1() {
			return r12_amount_1;
		}

		public void setR12_amount_1(BigDecimal r12_amount_1) {
			this.r12_amount_1 = r12_amount_1;
		}

		public BigDecimal getR12_amount_2() {
			return r12_amount_2;
		}

		public void setR12_amount_2(BigDecimal r12_amount_2) {
			this.r12_amount_2 = r12_amount_2;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_amount_1() {
			return r13_amount_1;
		}

		public void setR13_amount_1(BigDecimal r13_amount_1) {
			this.r13_amount_1 = r13_amount_1;
		}

		public BigDecimal getR13_amount_2() {
			return r13_amount_2;
		}

		public void setR13_amount_2(BigDecimal r13_amount_2) {
			this.r13_amount_2 = r13_amount_2;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_amount_1() {
			return r14_amount_1;
		}

		public void setR14_amount_1(BigDecimal r14_amount_1) {
			this.r14_amount_1 = r14_amount_1;
		}

		public BigDecimal getR14_amount_2() {
			return r14_amount_2;
		}

		public void setR14_amount_2(BigDecimal r14_amount_2) {
			this.r14_amount_2 = r14_amount_2;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_amount_1() {
			return r15_amount_1;
		}

		public void setR15_amount_1(BigDecimal r15_amount_1) {
			this.r15_amount_1 = r15_amount_1;
		}

		public BigDecimal getR15_amount_2() {
			return r15_amount_2;
		}

		public void setR15_amount_2(BigDecimal r15_amount_2) {
			this.r15_amount_2 = r15_amount_2;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_amount_1() {
			return r16_amount_1;
		}

		public void setR16_amount_1(BigDecimal r16_amount_1) {
			this.r16_amount_1 = r16_amount_1;
		}

		public BigDecimal getR16_amount_2() {
			return r16_amount_2;
		}

		public void setR16_amount_2(BigDecimal r16_amount_2) {
			this.r16_amount_2 = r16_amount_2;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_amount_1() {
			return r17_amount_1;
		}

		public void setR17_amount_1(BigDecimal r17_amount_1) {
			this.r17_amount_1 = r17_amount_1;
		}

		public BigDecimal getR17_amount_2() {
			return r17_amount_2;
		}

		public void setR17_amount_2(BigDecimal r17_amount_2) {
			this.r17_amount_2 = r17_amount_2;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_amount_1() {
			return r18_amount_1;
		}

		public void setR18_amount_1(BigDecimal r18_amount_1) {
			this.r18_amount_1 = r18_amount_1;
		}

		public BigDecimal getR18_amount_2() {
			return r18_amount_2;
		}

		public void setR18_amount_2(BigDecimal r18_amount_2) {
			this.r18_amount_2 = r18_amount_2;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_amount_1() {
			return r19_amount_1;
		}

		public void setR19_amount_1(BigDecimal r19_amount_1) {
			this.r19_amount_1 = r19_amount_1;
		}

		public BigDecimal getR19_amount_2() {
			return r19_amount_2;
		}

		public void setR19_amount_2(BigDecimal r19_amount_2) {
			this.r19_amount_2 = r19_amount_2;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_amount_1() {
			return r20_amount_1;
		}

		public void setR20_amount_1(BigDecimal r20_amount_1) {
			this.r20_amount_1 = r20_amount_1;
		}

		public BigDecimal getR20_amount_2() {
			return r20_amount_2;
		}

		public void setR20_amount_2(BigDecimal r20_amount_2) {
			this.r20_amount_2 = r20_amount_2;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_amount_1() {
			return r21_amount_1;
		}

		public void setR21_amount_1(BigDecimal r21_amount_1) {
			this.r21_amount_1 = r21_amount_1;
		}

		public BigDecimal getR21_amount_2() {
			return r21_amount_2;
		}

		public void setR21_amount_2(BigDecimal r21_amount_2) {
			this.r21_amount_2 = r21_amount_2;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_amount_1() {
			return r22_amount_1;
		}

		public void setR22_amount_1(BigDecimal r22_amount_1) {
			this.r22_amount_1 = r22_amount_1;
		}

		public BigDecimal getR22_amount_2() {
			return r22_amount_2;
		}

		public void setR22_amount_2(BigDecimal r22_amount_2) {
			this.r22_amount_2 = r22_amount_2;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_amount_1() {
			return r23_amount_1;
		}

		public void setR23_amount_1(BigDecimal r23_amount_1) {
			this.r23_amount_1 = r23_amount_1;
		}

		public BigDecimal getR23_amount_2() {
			return r23_amount_2;
		}

		public void setR23_amount_2(BigDecimal r23_amount_2) {
			this.r23_amount_2 = r23_amount_2;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_amount_1() {
			return r24_amount_1;
		}

		public void setR24_amount_1(BigDecimal r24_amount_1) {
			this.r24_amount_1 = r24_amount_1;
		}

		public BigDecimal getR24_amount_2() {
			return r24_amount_2;
		}

		public void setR24_amount_2(BigDecimal r24_amount_2) {
			this.r24_amount_2 = r24_amount_2;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_amount_1() {
			return r25_amount_1;
		}

		public void setR25_amount_1(BigDecimal r25_amount_1) {
			this.r25_amount_1 = r25_amount_1;
		}

		public BigDecimal getR25_amount_2() {
			return r25_amount_2;
		}

		public void setR25_amount_2(BigDecimal r25_amount_2) {
			this.r25_amount_2 = r25_amount_2;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_amount_1() {
			return r26_amount_1;
		}

		public void setR26_amount_1(BigDecimal r26_amount_1) {
			this.r26_amount_1 = r26_amount_1;
		}

		public BigDecimal getR26_amount_2() {
			return r26_amount_2;
		}

		public void setR26_amount_2(BigDecimal r26_amount_2) {
			this.r26_amount_2 = r26_amount_2;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_amount_1() {
			return r31_amount_1;
		}

		public void setR31_amount_1(BigDecimal r31_amount_1) {
			this.r31_amount_1 = r31_amount_1;
		}

		public BigDecimal getR31_amount_2() {
			return r31_amount_2;
		}

		public void setR31_amount_2(BigDecimal r31_amount_2) {
			this.r31_amount_2 = r31_amount_2;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_amount_1() {
			return r32_amount_1;
		}

		public void setR32_amount_1(BigDecimal r32_amount_1) {
			this.r32_amount_1 = r32_amount_1;
		}

		public BigDecimal getR32_amount_2() {
			return r32_amount_2;
		}

		public void setR32_amount_2(BigDecimal r32_amount_2) {
			this.r32_amount_2 = r32_amount_2;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_amount_1() {
			return r33_amount_1;
		}

		public void setR33_amount_1(BigDecimal r33_amount_1) {
			this.r33_amount_1 = r33_amount_1;
		}

		public BigDecimal getR33_amount_2() {
			return r33_amount_2;
		}

		public void setR33_amount_2(BigDecimal r33_amount_2) {
			this.r33_amount_2 = r33_amount_2;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_amount_1() {
			return r34_amount_1;
		}

		public void setR34_amount_1(BigDecimal r34_amount_1) {
			this.r34_amount_1 = r34_amount_1;
		}

		public BigDecimal getR34_amount_2() {
			return r34_amount_2;
		}

		public void setR34_amount_2(BigDecimal r34_amount_2) {
			this.r34_amount_2 = r34_amount_2;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_amount_1() {
			return r35_amount_1;
		}

		public void setR35_amount_1(BigDecimal r35_amount_1) {
			this.r35_amount_1 = r35_amount_1;
		}

		public BigDecimal getR35_amount_2() {
			return r35_amount_2;
		}

		public void setR35_amount_2(BigDecimal r35_amount_2) {
			this.r35_amount_2 = r35_amount_2;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_amount_1() {
			return r36_amount_1;
		}

		public void setR36_amount_1(BigDecimal r36_amount_1) {
			this.r36_amount_1 = r36_amount_1;
		}

		public BigDecimal getR36_amount_2() {
			return r36_amount_2;
		}

		public void setR36_amount_2(BigDecimal r36_amount_2) {
			this.r36_amount_2 = r36_amount_2;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_amount_1() {
			return r41_amount_1;
		}

		public void setR41_amount_1(BigDecimal r41_amount_1) {
			this.r41_amount_1 = r41_amount_1;
		}

		public BigDecimal getR41_amount_2() {
			return r41_amount_2;
		}

		public void setR41_amount_2(BigDecimal r41_amount_2) {
			this.r41_amount_2 = r41_amount_2;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_amount_1() {
			return r42_amount_1;
		}

		public void setR42_amount_1(BigDecimal r42_amount_1) {
			this.r42_amount_1 = r42_amount_1;
		}

		public BigDecimal getR42_amount_2() {
			return r42_amount_2;
		}

		public void setR42_amount_2(BigDecimal r42_amount_2) {
			this.r42_amount_2 = r42_amount_2;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_amount_1() {
			return r43_amount_1;
		}

		public void setR43_amount_1(BigDecimal r43_amount_1) {
			this.r43_amount_1 = r43_amount_1;
		}

		public BigDecimal getR43_amount_2() {
			return r43_amount_2;
		}

		public void setR43_amount_2(BigDecimal r43_amount_2) {
			this.r43_amount_2 = r43_amount_2;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_amount_1() {
			return r44_amount_1;
		}

		public void setR44_amount_1(BigDecimal r44_amount_1) {
			this.r44_amount_1 = r44_amount_1;
		}

		public BigDecimal getR44_amount_2() {
			return r44_amount_2;
		}

		public void setR44_amount_2(BigDecimal r44_amount_2) {
			this.r44_amount_2 = r44_amount_2;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_amount_1() {
			return r45_amount_1;
		}

		public void setR45_amount_1(BigDecimal r45_amount_1) {
			this.r45_amount_1 = r45_amount_1;
		}

		public BigDecimal getR45_amount_2() {
			return r45_amount_2;
		}

		public void setR45_amount_2(BigDecimal r45_amount_2) {
			this.r45_amount_2 = r45_amount_2;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_amount_1() {
			return r46_amount_1;
		}

		public void setR46_amount_1(BigDecimal r46_amount_1) {
			this.r46_amount_1 = r46_amount_1;
		}

		public BigDecimal getR46_amount_2() {
			return r46_amount_2;
		}

		public void setR46_amount_2(BigDecimal r46_amount_2) {
			this.r46_amount_2 = r46_amount_2;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_amount_1() {
			return r47_amount_1;
		}

		public void setR47_amount_1(BigDecimal r47_amount_1) {
			this.r47_amount_1 = r47_amount_1;
		}

		public BigDecimal getR47_amount_2() {
			return r47_amount_2;
		}

		public void setR47_amount_2(BigDecimal r47_amount_2) {
			this.r47_amount_2 = r47_amount_2;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_amount_1() {
			return r48_amount_1;
		}

		public void setR48_amount_1(BigDecimal r48_amount_1) {
			this.r48_amount_1 = r48_amount_1;
		}

		public BigDecimal getR48_amount_2() {
			return r48_amount_2;
		}

		public void setR48_amount_2(BigDecimal r48_amount_2) {
			this.r48_amount_2 = r48_amount_2;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_amount_1() {
			return r49_amount_1;
		}

		public void setR49_amount_1(BigDecimal r49_amount_1) {
			this.r49_amount_1 = r49_amount_1;
		}

		public BigDecimal getR49_amount_2() {
			return r49_amount_2;
		}

		public void setR49_amount_2(BigDecimal r49_amount_2) {
			this.r49_amount_2 = r49_amount_2;
		}

		public java.util.Date getReport_date() {
			return report_date;
		}

		public void setReport_date(java.util.Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
			this.report_version = report_version;
		}

		public java.util.Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(java.util.Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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
	}

	class M_CA2ArchivalSummaryRowMapper implements RowMapper<M_CA2_Archival_Summary_Entity> {
		@Override
		public M_CA2_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Archival_Summary_Entity obj = new M_CA2_Archival_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_amount_1(rs.getBigDecimal("R10_AMOUNT_1"));
			obj.setR10_amount_2(rs.getBigDecimal("R10_AMOUNT_2"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_1(rs.getBigDecimal("R11_AMOUNT_1"));
			obj.setR11_amount_2(rs.getBigDecimal("R11_AMOUNT_2"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_1(rs.getBigDecimal("R12_AMOUNT_1"));
			obj.setR12_amount_2(rs.getBigDecimal("R12_AMOUNT_2"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_1(rs.getBigDecimal("R13_AMOUNT_1"));
			obj.setR13_amount_2(rs.getBigDecimal("R13_AMOUNT_2"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_1(rs.getBigDecimal("R14_AMOUNT_1"));
			obj.setR14_amount_2(rs.getBigDecimal("R14_AMOUNT_2"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_1(rs.getBigDecimal("R15_AMOUNT_1"));
			obj.setR15_amount_2(rs.getBigDecimal("R15_AMOUNT_2"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_1(rs.getBigDecimal("R16_AMOUNT_1"));
			obj.setR16_amount_2(rs.getBigDecimal("R16_AMOUNT_2"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_1(rs.getBigDecimal("R17_AMOUNT_1"));
			obj.setR17_amount_2(rs.getBigDecimal("R17_AMOUNT_2"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_1(rs.getBigDecimal("R18_AMOUNT_1"));
			obj.setR18_amount_2(rs.getBigDecimal("R18_AMOUNT_2"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_1(rs.getBigDecimal("R19_AMOUNT_1"));
			obj.setR19_amount_2(rs.getBigDecimal("R19_AMOUNT_2"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_1(rs.getBigDecimal("R20_AMOUNT_1"));
			obj.setR20_amount_2(rs.getBigDecimal("R20_AMOUNT_2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_1(rs.getBigDecimal("R21_AMOUNT_1"));
			obj.setR21_amount_2(rs.getBigDecimal("R21_AMOUNT_2"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_1(rs.getBigDecimal("R22_AMOUNT_1"));
			obj.setR22_amount_2(rs.getBigDecimal("R22_AMOUNT_2"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_1(rs.getBigDecimal("R23_AMOUNT_1"));
			obj.setR23_amount_2(rs.getBigDecimal("R23_AMOUNT_2"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_1(rs.getBigDecimal("R24_AMOUNT_1"));
			obj.setR24_amount_2(rs.getBigDecimal("R24_AMOUNT_2"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_amount_1(rs.getBigDecimal("R25_AMOUNT_1"));
			obj.setR25_amount_2(rs.getBigDecimal("R25_AMOUNT_2"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_1(rs.getBigDecimal("R26_AMOUNT_1"));
			obj.setR26_amount_2(rs.getBigDecimal("R26_AMOUNT_2"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_1(rs.getBigDecimal("R31_AMOUNT_1"));
			obj.setR31_amount_2(rs.getBigDecimal("R31_AMOUNT_2"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_1(rs.getBigDecimal("R32_AMOUNT_1"));
			obj.setR32_amount_2(rs.getBigDecimal("R32_AMOUNT_2"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_1(rs.getBigDecimal("R33_AMOUNT_1"));
			obj.setR33_amount_2(rs.getBigDecimal("R33_AMOUNT_2"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_1(rs.getBigDecimal("R34_AMOUNT_1"));
			obj.setR34_amount_2(rs.getBigDecimal("R34_AMOUNT_2"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_1(rs.getBigDecimal("R35_AMOUNT_1"));
			obj.setR35_amount_2(rs.getBigDecimal("R35_AMOUNT_2"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_1(rs.getBigDecimal("R36_AMOUNT_1"));
			obj.setR36_amount_2(rs.getBigDecimal("R36_AMOUNT_2"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_1(rs.getBigDecimal("R41_AMOUNT_1"));
			obj.setR41_amount_2(rs.getBigDecimal("R41_AMOUNT_2"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_1(rs.getBigDecimal("R42_AMOUNT_1"));
			obj.setR42_amount_2(rs.getBigDecimal("R42_AMOUNT_2"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_1(rs.getBigDecimal("R43_AMOUNT_1"));
			obj.setR43_amount_2(rs.getBigDecimal("R43_AMOUNT_2"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_1(rs.getBigDecimal("R44_AMOUNT_1"));
			obj.setR44_amount_2(rs.getBigDecimal("R44_AMOUNT_2"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_1(rs.getBigDecimal("R45_AMOUNT_1"));
			obj.setR45_amount_2(rs.getBigDecimal("R45_AMOUNT_2"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_1(rs.getBigDecimal("R46_AMOUNT_1"));
			obj.setR46_amount_2(rs.getBigDecimal("R46_AMOUNT_2"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_1(rs.getBigDecimal("R47_AMOUNT_1"));
			obj.setR47_amount_2(rs.getBigDecimal("R47_AMOUNT_2"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_1(rs.getBigDecimal("R48_AMOUNT_1"));
			obj.setR48_amount_2(rs.getBigDecimal("R48_AMOUNT_2"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_1(rs.getBigDecimal("R49_AMOUNT_1"));
			obj.setR49_amount_2(rs.getBigDecimal("R49_AMOUNT_2"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_RESUB_Summary_Entity
	// =========================================================
	public static class M_CA2_RESUB_Summary_Entity {
		private String r10_product;
		private BigDecimal r10_amount_1;
		private BigDecimal r10_amount_2;
		private String r11_product;
		private BigDecimal r11_amount_1;
		private BigDecimal r11_amount_2;
		private String r12_product;
		private BigDecimal r12_amount_1;
		private BigDecimal r12_amount_2;
		private String r13_product;
		private BigDecimal r13_amount_1;
		private BigDecimal r13_amount_2;
		private String r14_product;
		private BigDecimal r14_amount_1;
		private BigDecimal r14_amount_2;
		private String r15_product;
		private BigDecimal r15_amount_1;
		private BigDecimal r15_amount_2;
		private String r16_product;
		private BigDecimal r16_amount_1;
		private BigDecimal r16_amount_2;
		private String r17_product;
		private BigDecimal r17_amount_1;
		private BigDecimal r17_amount_2;
		private String r18_product;
		private BigDecimal r18_amount_1;
		private BigDecimal r18_amount_2;
		private String r19_product;
		private BigDecimal r19_amount_1;
		private BigDecimal r19_amount_2;
		private String r20_product;
		private BigDecimal r20_amount_1;
		private BigDecimal r20_amount_2;
		private String r21_product;
		private BigDecimal r21_amount_1;
		private BigDecimal r21_amount_2;
		private String r22_product;
		private BigDecimal r22_amount_1;
		private BigDecimal r22_amount_2;
		private String r23_product;
		private BigDecimal r23_amount_1;
		private BigDecimal r23_amount_2;
		private String r24_product;
		private BigDecimal r24_amount_1;
		private BigDecimal r24_amount_2;
		private String r25_product;
		private BigDecimal r25_amount_1;
		private BigDecimal r25_amount_2;
		private String r26_product;
		private BigDecimal r26_amount_1;
		private BigDecimal r26_amount_2;
		private String r31_product;
		private BigDecimal r31_amount_1;
		private BigDecimal r31_amount_2;
		private String r32_product;
		private BigDecimal r32_amount_1;
		private BigDecimal r32_amount_2;
		private String r33_product;
		private BigDecimal r33_amount_1;
		private BigDecimal r33_amount_2;
		private String r34_product;
		private BigDecimal r34_amount_1;
		private BigDecimal r34_amount_2;
		private String r35_product;
		private BigDecimal r35_amount_1;
		private BigDecimal r35_amount_2;
		private String r36_product;
		private BigDecimal r36_amount_1;
		private BigDecimal r36_amount_2;
		private String r41_product;
		private BigDecimal r41_amount_1;
		private BigDecimal r41_amount_2;
		private String r42_product;
		private BigDecimal r42_amount_1;
		private BigDecimal r42_amount_2;
		private String r43_product;
		private BigDecimal r43_amount_1;
		private BigDecimal r43_amount_2;
		private String r44_product;
		private BigDecimal r44_amount_1;
		private BigDecimal r44_amount_2;
		private String r45_product;
		private BigDecimal r45_amount_1;
		private BigDecimal r45_amount_2;
		private String r46_product;
		private BigDecimal r46_amount_1;
		private BigDecimal r46_amount_2;
		private String r47_product;
		private BigDecimal r47_amount_1;
		private BigDecimal r47_amount_2;
		private String r48_product;
		private BigDecimal r48_amount_1;
		private BigDecimal r48_amount_2;
		private String r49_product;
		private BigDecimal r49_amount_1;
		private BigDecimal r49_amount_2;
		private java.util.Date report_date;
		private BigDecimal report_version;
		private java.util.Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_amount_1() {
			return r10_amount_1;
		}

		public void setR10_amount_1(BigDecimal r10_amount_1) {
			this.r10_amount_1 = r10_amount_1;
		}

		public BigDecimal getR10_amount_2() {
			return r10_amount_2;
		}

		public void setR10_amount_2(BigDecimal r10_amount_2) {
			this.r10_amount_2 = r10_amount_2;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_amount_1() {
			return r11_amount_1;
		}

		public void setR11_amount_1(BigDecimal r11_amount_1) {
			this.r11_amount_1 = r11_amount_1;
		}

		public BigDecimal getR11_amount_2() {
			return r11_amount_2;
		}

		public void setR11_amount_2(BigDecimal r11_amount_2) {
			this.r11_amount_2 = r11_amount_2;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_amount_1() {
			return r12_amount_1;
		}

		public void setR12_amount_1(BigDecimal r12_amount_1) {
			this.r12_amount_1 = r12_amount_1;
		}

		public BigDecimal getR12_amount_2() {
			return r12_amount_2;
		}

		public void setR12_amount_2(BigDecimal r12_amount_2) {
			this.r12_amount_2 = r12_amount_2;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_amount_1() {
			return r13_amount_1;
		}

		public void setR13_amount_1(BigDecimal r13_amount_1) {
			this.r13_amount_1 = r13_amount_1;
		}

		public BigDecimal getR13_amount_2() {
			return r13_amount_2;
		}

		public void setR13_amount_2(BigDecimal r13_amount_2) {
			this.r13_amount_2 = r13_amount_2;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_amount_1() {
			return r14_amount_1;
		}

		public void setR14_amount_1(BigDecimal r14_amount_1) {
			this.r14_amount_1 = r14_amount_1;
		}

		public BigDecimal getR14_amount_2() {
			return r14_amount_2;
		}

		public void setR14_amount_2(BigDecimal r14_amount_2) {
			this.r14_amount_2 = r14_amount_2;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_amount_1() {
			return r15_amount_1;
		}

		public void setR15_amount_1(BigDecimal r15_amount_1) {
			this.r15_amount_1 = r15_amount_1;
		}

		public BigDecimal getR15_amount_2() {
			return r15_amount_2;
		}

		public void setR15_amount_2(BigDecimal r15_amount_2) {
			this.r15_amount_2 = r15_amount_2;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_amount_1() {
			return r16_amount_1;
		}

		public void setR16_amount_1(BigDecimal r16_amount_1) {
			this.r16_amount_1 = r16_amount_1;
		}

		public BigDecimal getR16_amount_2() {
			return r16_amount_2;
		}

		public void setR16_amount_2(BigDecimal r16_amount_2) {
			this.r16_amount_2 = r16_amount_2;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_amount_1() {
			return r17_amount_1;
		}

		public void setR17_amount_1(BigDecimal r17_amount_1) {
			this.r17_amount_1 = r17_amount_1;
		}

		public BigDecimal getR17_amount_2() {
			return r17_amount_2;
		}

		public void setR17_amount_2(BigDecimal r17_amount_2) {
			this.r17_amount_2 = r17_amount_2;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_amount_1() {
			return r18_amount_1;
		}

		public void setR18_amount_1(BigDecimal r18_amount_1) {
			this.r18_amount_1 = r18_amount_1;
		}

		public BigDecimal getR18_amount_2() {
			return r18_amount_2;
		}

		public void setR18_amount_2(BigDecimal r18_amount_2) {
			this.r18_amount_2 = r18_amount_2;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_amount_1() {
			return r19_amount_1;
		}

		public void setR19_amount_1(BigDecimal r19_amount_1) {
			this.r19_amount_1 = r19_amount_1;
		}

		public BigDecimal getR19_amount_2() {
			return r19_amount_2;
		}

		public void setR19_amount_2(BigDecimal r19_amount_2) {
			this.r19_amount_2 = r19_amount_2;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_amount_1() {
			return r20_amount_1;
		}

		public void setR20_amount_1(BigDecimal r20_amount_1) {
			this.r20_amount_1 = r20_amount_1;
		}

		public BigDecimal getR20_amount_2() {
			return r20_amount_2;
		}

		public void setR20_amount_2(BigDecimal r20_amount_2) {
			this.r20_amount_2 = r20_amount_2;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_amount_1() {
			return r21_amount_1;
		}

		public void setR21_amount_1(BigDecimal r21_amount_1) {
			this.r21_amount_1 = r21_amount_1;
		}

		public BigDecimal getR21_amount_2() {
			return r21_amount_2;
		}

		public void setR21_amount_2(BigDecimal r21_amount_2) {
			this.r21_amount_2 = r21_amount_2;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_amount_1() {
			return r22_amount_1;
		}

		public void setR22_amount_1(BigDecimal r22_amount_1) {
			this.r22_amount_1 = r22_amount_1;
		}

		public BigDecimal getR22_amount_2() {
			return r22_amount_2;
		}

		public void setR22_amount_2(BigDecimal r22_amount_2) {
			this.r22_amount_2 = r22_amount_2;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_amount_1() {
			return r23_amount_1;
		}

		public void setR23_amount_1(BigDecimal r23_amount_1) {
			this.r23_amount_1 = r23_amount_1;
		}

		public BigDecimal getR23_amount_2() {
			return r23_amount_2;
		}

		public void setR23_amount_2(BigDecimal r23_amount_2) {
			this.r23_amount_2 = r23_amount_2;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_amount_1() {
			return r24_amount_1;
		}

		public void setR24_amount_1(BigDecimal r24_amount_1) {
			this.r24_amount_1 = r24_amount_1;
		}

		public BigDecimal getR24_amount_2() {
			return r24_amount_2;
		}

		public void setR24_amount_2(BigDecimal r24_amount_2) {
			this.r24_amount_2 = r24_amount_2;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_amount_1() {
			return r25_amount_1;
		}

		public void setR25_amount_1(BigDecimal r25_amount_1) {
			this.r25_amount_1 = r25_amount_1;
		}

		public BigDecimal getR25_amount_2() {
			return r25_amount_2;
		}

		public void setR25_amount_2(BigDecimal r25_amount_2) {
			this.r25_amount_2 = r25_amount_2;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_amount_1() {
			return r26_amount_1;
		}

		public void setR26_amount_1(BigDecimal r26_amount_1) {
			this.r26_amount_1 = r26_amount_1;
		}

		public BigDecimal getR26_amount_2() {
			return r26_amount_2;
		}

		public void setR26_amount_2(BigDecimal r26_amount_2) {
			this.r26_amount_2 = r26_amount_2;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_amount_1() {
			return r31_amount_1;
		}

		public void setR31_amount_1(BigDecimal r31_amount_1) {
			this.r31_amount_1 = r31_amount_1;
		}

		public BigDecimal getR31_amount_2() {
			return r31_amount_2;
		}

		public void setR31_amount_2(BigDecimal r31_amount_2) {
			this.r31_amount_2 = r31_amount_2;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_amount_1() {
			return r32_amount_1;
		}

		public void setR32_amount_1(BigDecimal r32_amount_1) {
			this.r32_amount_1 = r32_amount_1;
		}

		public BigDecimal getR32_amount_2() {
			return r32_amount_2;
		}

		public void setR32_amount_2(BigDecimal r32_amount_2) {
			this.r32_amount_2 = r32_amount_2;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_amount_1() {
			return r33_amount_1;
		}

		public void setR33_amount_1(BigDecimal r33_amount_1) {
			this.r33_amount_1 = r33_amount_1;
		}

		public BigDecimal getR33_amount_2() {
			return r33_amount_2;
		}

		public void setR33_amount_2(BigDecimal r33_amount_2) {
			this.r33_amount_2 = r33_amount_2;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_amount_1() {
			return r34_amount_1;
		}

		public void setR34_amount_1(BigDecimal r34_amount_1) {
			this.r34_amount_1 = r34_amount_1;
		}

		public BigDecimal getR34_amount_2() {
			return r34_amount_2;
		}

		public void setR34_amount_2(BigDecimal r34_amount_2) {
			this.r34_amount_2 = r34_amount_2;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_amount_1() {
			return r35_amount_1;
		}

		public void setR35_amount_1(BigDecimal r35_amount_1) {
			this.r35_amount_1 = r35_amount_1;
		}

		public BigDecimal getR35_amount_2() {
			return r35_amount_2;
		}

		public void setR35_amount_2(BigDecimal r35_amount_2) {
			this.r35_amount_2 = r35_amount_2;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_amount_1() {
			return r36_amount_1;
		}

		public void setR36_amount_1(BigDecimal r36_amount_1) {
			this.r36_amount_1 = r36_amount_1;
		}

		public BigDecimal getR36_amount_2() {
			return r36_amount_2;
		}

		public void setR36_amount_2(BigDecimal r36_amount_2) {
			this.r36_amount_2 = r36_amount_2;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_amount_1() {
			return r41_amount_1;
		}

		public void setR41_amount_1(BigDecimal r41_amount_1) {
			this.r41_amount_1 = r41_amount_1;
		}

		public BigDecimal getR41_amount_2() {
			return r41_amount_2;
		}

		public void setR41_amount_2(BigDecimal r41_amount_2) {
			this.r41_amount_2 = r41_amount_2;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_amount_1() {
			return r42_amount_1;
		}

		public void setR42_amount_1(BigDecimal r42_amount_1) {
			this.r42_amount_1 = r42_amount_1;
		}

		public BigDecimal getR42_amount_2() {
			return r42_amount_2;
		}

		public void setR42_amount_2(BigDecimal r42_amount_2) {
			this.r42_amount_2 = r42_amount_2;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_amount_1() {
			return r43_amount_1;
		}

		public void setR43_amount_1(BigDecimal r43_amount_1) {
			this.r43_amount_1 = r43_amount_1;
		}

		public BigDecimal getR43_amount_2() {
			return r43_amount_2;
		}

		public void setR43_amount_2(BigDecimal r43_amount_2) {
			this.r43_amount_2 = r43_amount_2;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_amount_1() {
			return r44_amount_1;
		}

		public void setR44_amount_1(BigDecimal r44_amount_1) {
			this.r44_amount_1 = r44_amount_1;
		}

		public BigDecimal getR44_amount_2() {
			return r44_amount_2;
		}

		public void setR44_amount_2(BigDecimal r44_amount_2) {
			this.r44_amount_2 = r44_amount_2;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_amount_1() {
			return r45_amount_1;
		}

		public void setR45_amount_1(BigDecimal r45_amount_1) {
			this.r45_amount_1 = r45_amount_1;
		}

		public BigDecimal getR45_amount_2() {
			return r45_amount_2;
		}

		public void setR45_amount_2(BigDecimal r45_amount_2) {
			this.r45_amount_2 = r45_amount_2;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_amount_1() {
			return r46_amount_1;
		}

		public void setR46_amount_1(BigDecimal r46_amount_1) {
			this.r46_amount_1 = r46_amount_1;
		}

		public BigDecimal getR46_amount_2() {
			return r46_amount_2;
		}

		public void setR46_amount_2(BigDecimal r46_amount_2) {
			this.r46_amount_2 = r46_amount_2;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_amount_1() {
			return r47_amount_1;
		}

		public void setR47_amount_1(BigDecimal r47_amount_1) {
			this.r47_amount_1 = r47_amount_1;
		}

		public BigDecimal getR47_amount_2() {
			return r47_amount_2;
		}

		public void setR47_amount_2(BigDecimal r47_amount_2) {
			this.r47_amount_2 = r47_amount_2;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_amount_1() {
			return r48_amount_1;
		}

		public void setR48_amount_1(BigDecimal r48_amount_1) {
			this.r48_amount_1 = r48_amount_1;
		}

		public BigDecimal getR48_amount_2() {
			return r48_amount_2;
		}

		public void setR48_amount_2(BigDecimal r48_amount_2) {
			this.r48_amount_2 = r48_amount_2;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_amount_1() {
			return r49_amount_1;
		}

		public void setR49_amount_1(BigDecimal r49_amount_1) {
			this.r49_amount_1 = r49_amount_1;
		}

		public BigDecimal getR49_amount_2() {
			return r49_amount_2;
		}

		public void setR49_amount_2(BigDecimal r49_amount_2) {
			this.r49_amount_2 = r49_amount_2;
		}

		public java.util.Date getReport_date() {
			return report_date;
		}

		public void setReport_date(java.util.Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
			this.report_version = report_version;
		}

		public java.util.Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(java.util.Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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
	}

	class M_CA2ResubSummaryRowMapper implements RowMapper<M_CA2_RESUB_Summary_Entity> {
		@Override
		public M_CA2_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_RESUB_Summary_Entity obj = new M_CA2_RESUB_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_amount_1(rs.getBigDecimal("R10_AMOUNT_1"));
			obj.setR10_amount_2(rs.getBigDecimal("R10_AMOUNT_2"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_1(rs.getBigDecimal("R11_AMOUNT_1"));
			obj.setR11_amount_2(rs.getBigDecimal("R11_AMOUNT_2"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_1(rs.getBigDecimal("R12_AMOUNT_1"));
			obj.setR12_amount_2(rs.getBigDecimal("R12_AMOUNT_2"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_1(rs.getBigDecimal("R13_AMOUNT_1"));
			obj.setR13_amount_2(rs.getBigDecimal("R13_AMOUNT_2"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_1(rs.getBigDecimal("R14_AMOUNT_1"));
			obj.setR14_amount_2(rs.getBigDecimal("R14_AMOUNT_2"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_1(rs.getBigDecimal("R15_AMOUNT_1"));
			obj.setR15_amount_2(rs.getBigDecimal("R15_AMOUNT_2"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_1(rs.getBigDecimal("R16_AMOUNT_1"));
			obj.setR16_amount_2(rs.getBigDecimal("R16_AMOUNT_2"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_1(rs.getBigDecimal("R17_AMOUNT_1"));
			obj.setR17_amount_2(rs.getBigDecimal("R17_AMOUNT_2"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_1(rs.getBigDecimal("R18_AMOUNT_1"));
			obj.setR18_amount_2(rs.getBigDecimal("R18_AMOUNT_2"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_1(rs.getBigDecimal("R19_AMOUNT_1"));
			obj.setR19_amount_2(rs.getBigDecimal("R19_AMOUNT_2"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_1(rs.getBigDecimal("R20_AMOUNT_1"));
			obj.setR20_amount_2(rs.getBigDecimal("R20_AMOUNT_2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_1(rs.getBigDecimal("R21_AMOUNT_1"));
			obj.setR21_amount_2(rs.getBigDecimal("R21_AMOUNT_2"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_1(rs.getBigDecimal("R22_AMOUNT_1"));
			obj.setR22_amount_2(rs.getBigDecimal("R22_AMOUNT_2"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_1(rs.getBigDecimal("R23_AMOUNT_1"));
			obj.setR23_amount_2(rs.getBigDecimal("R23_AMOUNT_2"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_1(rs.getBigDecimal("R24_AMOUNT_1"));
			obj.setR24_amount_2(rs.getBigDecimal("R24_AMOUNT_2"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_amount_1(rs.getBigDecimal("R25_AMOUNT_1"));
			obj.setR25_amount_2(rs.getBigDecimal("R25_AMOUNT_2"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_1(rs.getBigDecimal("R26_AMOUNT_1"));
			obj.setR26_amount_2(rs.getBigDecimal("R26_AMOUNT_2"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_1(rs.getBigDecimal("R31_AMOUNT_1"));
			obj.setR31_amount_2(rs.getBigDecimal("R31_AMOUNT_2"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_1(rs.getBigDecimal("R32_AMOUNT_1"));
			obj.setR32_amount_2(rs.getBigDecimal("R32_AMOUNT_2"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_1(rs.getBigDecimal("R33_AMOUNT_1"));
			obj.setR33_amount_2(rs.getBigDecimal("R33_AMOUNT_2"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_1(rs.getBigDecimal("R34_AMOUNT_1"));
			obj.setR34_amount_2(rs.getBigDecimal("R34_AMOUNT_2"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_1(rs.getBigDecimal("R35_AMOUNT_1"));
			obj.setR35_amount_2(rs.getBigDecimal("R35_AMOUNT_2"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_1(rs.getBigDecimal("R36_AMOUNT_1"));
			obj.setR36_amount_2(rs.getBigDecimal("R36_AMOUNT_2"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_1(rs.getBigDecimal("R41_AMOUNT_1"));
			obj.setR41_amount_2(rs.getBigDecimal("R41_AMOUNT_2"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_1(rs.getBigDecimal("R42_AMOUNT_1"));
			obj.setR42_amount_2(rs.getBigDecimal("R42_AMOUNT_2"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_1(rs.getBigDecimal("R43_AMOUNT_1"));
			obj.setR43_amount_2(rs.getBigDecimal("R43_AMOUNT_2"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_1(rs.getBigDecimal("R44_AMOUNT_1"));
			obj.setR44_amount_2(rs.getBigDecimal("R44_AMOUNT_2"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_1(rs.getBigDecimal("R45_AMOUNT_1"));
			obj.setR45_amount_2(rs.getBigDecimal("R45_AMOUNT_2"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_1(rs.getBigDecimal("R46_AMOUNT_1"));
			obj.setR46_amount_2(rs.getBigDecimal("R46_AMOUNT_2"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_1(rs.getBigDecimal("R47_AMOUNT_1"));
			obj.setR47_amount_2(rs.getBigDecimal("R47_AMOUNT_2"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_1(rs.getBigDecimal("R48_AMOUNT_1"));
			obj.setR48_amount_2(rs.getBigDecimal("R48_AMOUNT_2"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_1(rs.getBigDecimal("R49_AMOUNT_1"));
			obj.setR49_amount_2(rs.getBigDecimal("R49_AMOUNT_2"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Detail_Entity (camelCase, matches @Column names)
	// =========================================================
	public static class M_CA2_Detail_Entity {
		
		private Long sno;
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInPula;
		private java.util.Date reportDate;
		private String reportName;
		private String createUser;
		private java.util.Date createTime;
		private String modifyUser;
		private java.util.Date modifyTime;
		private String verifyUser;
		private java.util.Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String glCode;
		private String glSubCode;
		
		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public java.util.Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(java.util.Date reportDate) {
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

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.util.Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public java.util.Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(java.util.Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public java.util.Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(java.util.Date verifyTime) {
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

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String glCode) {
			this.glCode = glCode;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String glSubCode) {
			this.glSubCode = glSubCode;
		}
	}

	class M_CA2DetailRowMapper implements RowMapper<M_CA2_Detail_Entity> {
		private Set<String> columnNames = null;

		private boolean hasCol(ResultSet rs, String colName) {
			if (columnNames == null) {
				columnNames = new HashSet<>();
				try {
					ResultSetMetaData md = rs.getMetaData();
					int count = md.getColumnCount();
					for (int i = 1; i <= count; i++) {
						columnNames.add(md.getColumnName(i).toUpperCase());
					}
				} catch (Exception ignored) {
				}
			}
			return columnNames.contains(colName.toUpperCase());
		}

		@Override
		public M_CA2_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Detail_Entity obj = new M_CA2_Detail_Entity();
			if (hasCol(rs, "SNO")) {
				obj.setSno(rs.getLong("SNO"));
			} else if (hasCol(rs, "SRL_NO")) {
				obj.setSno(rs.getLong("SRL_NO"));
			} else {
				obj.setSno((long) (rowNum + 1));
			}
			if (hasCol(rs, "CUST_ID")) obj.setCustId(rs.getString("CUST_ID"));
			if (hasCol(rs, "ACCT_NUMBER")) obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			if (hasCol(rs, "ACCT_NAME")) obj.setAcctName(rs.getString("ACCT_NAME"));
			if (hasCol(rs, "DATA_TYPE")) obj.setDataType(rs.getString("DATA_TYPE"));
			if (hasCol(rs, "REPORT_LABEL")) obj.setReportLabel(rs.getString("REPORT_LABEL"));
			if (hasCol(rs, "REPORT_ADDL_CRITERIA_1")) obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			if (hasCol(rs, "REPORT_ADDL_CRITERIA_2")) obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			if (hasCol(rs, "REPORT_ADDL_CRITERIA_3")) obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			if (hasCol(rs, "SANCTION_LIMIT")) obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			if (hasCol(rs, "REPORT_REMARKS")) obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			if (hasCol(rs, "MODIFICATION_REMARKS")) obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			if (hasCol(rs, "DATA_ENTRY_VERSION")) obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			if (hasCol(rs, "ACCT_BALANCE_IN_PULA")) obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			if (hasCol(rs, "REPORT_DATE")) obj.setReportDate(rs.getDate("REPORT_DATE"));
			if (hasCol(rs, "REPORT_NAME")) obj.setReportName(rs.getString("REPORT_NAME"));
			if (hasCol(rs, "CREATE_USER")) obj.setCreateUser(rs.getString("CREATE_USER"));
			if (hasCol(rs, "CREATE_TIME")) obj.setCreateTime(rs.getDate("CREATE_TIME"));
			if (hasCol(rs, "MODIFY_USER")) obj.setModifyUser(rs.getString("MODIFY_USER"));
			if (hasCol(rs, "MODIFY_TIME")) obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			if (hasCol(rs, "VERIFY_USER")) obj.setVerifyUser(rs.getString("VERIFY_USER"));
			if (hasCol(rs, "VERIFY_TIME")) obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			if (hasCol(rs, "ENTITY_FLG")) obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			if (hasCol(rs, "MODIFY_FLG")) obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			if (hasCol(rs, "DEL_FLG")) obj.setDelFlg(rs.getString("DEL_FLG"));
			if (hasCol(rs, "GL_CODE")) obj.setGlCode(rs.getString("GL_CODE"));
			if (hasCol(rs, "GL_SUB_CODE")) obj.setGlSubCode(rs.getString("GL_SUB_CODE"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Archival_Detail_Entity
	// =========================================================
	public static class M_CA2_Archival_Detail_Entity {
		
		private Long sno;
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInPula;
		private java.util.Date reportDate;
		private String reportName;
		private String createUser;
		private java.util.Date createTime;
		private String modifyUser;
		private java.util.Date modifyTime;
		private String verifyUser;
		private java.util.Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String glCode;
		private String glSubCode;
		
		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public java.util.Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(java.util.Date reportDate) {
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

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.util.Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public java.util.Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(java.util.Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public java.util.Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(java.util.Date verifyTime) {
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

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String glCode) {
			this.glCode = glCode;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String glSubCode) {
			this.glSubCode = glSubCode;
		}
	}

	class M_CA2ArchivalDetailRowMapper implements RowMapper<M_CA2_Archival_Detail_Entity> {
		private Set<String> columnNames = null;

		private boolean hasCol(ResultSet rs, String colName) {
			if (columnNames == null) {
				columnNames = new HashSet<>();
				try {
					ResultSetMetaData md = rs.getMetaData();
					int count = md.getColumnCount();
					for (int i = 1; i <= count; i++) {
						columnNames.add(md.getColumnName(i).toUpperCase());
					}
				} catch (Exception ignored) {
				}
			}
			return columnNames.contains(colName.toUpperCase());
		}

		@Override
		public M_CA2_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Archival_Detail_Entity obj = new M_CA2_Archival_Detail_Entity();
			if (hasCol(rs, "SNO")) {
				obj.setSno(rs.getLong("SNO"));
			} else if (hasCol(rs, "SRL_NO")) {
				obj.setSno(rs.getLong("SRL_NO"));
			} else {
				obj.setSno(null);
			}
			if (hasCol(rs, "CUST_ID")) obj.setCustId(rs.getString("CUST_ID"));
			if (hasCol(rs, "ACCT_NUMBER")) obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			if (hasCol(rs, "ACCT_NAME")) obj.setAcctName(rs.getString("ACCT_NAME"));
			if (hasCol(rs, "DATA_TYPE")) obj.setDataType(rs.getString("DATA_TYPE"));
			if (hasCol(rs, "REPORT_LABEL")) obj.setReportLabel(rs.getString("REPORT_LABEL"));
			if (hasCol(rs, "REPORT_ADDL_CRITERIA_1")) obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			if (hasCol(rs, "REPORT_ADDL_CRITERIA_2")) obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			if (hasCol(rs, "REPORT_ADDL_CRITERIA_3")) obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			if (hasCol(rs, "SANCTION_LIMIT")) obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			if (hasCol(rs, "REPORT_REMARKS")) obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			if (hasCol(rs, "MODIFICATION_REMARKS")) obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			if (hasCol(rs, "DATA_ENTRY_VERSION")) obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			if (hasCol(rs, "ACCT_BALANCE_IN_PULA")) obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			if (hasCol(rs, "REPORT_DATE")) obj.setReportDate(rs.getDate("REPORT_DATE"));
			if (hasCol(rs, "REPORT_NAME")) obj.setReportName(rs.getString("REPORT_NAME"));
			if (hasCol(rs, "CREATE_USER")) obj.setCreateUser(rs.getString("CREATE_USER"));
			if (hasCol(rs, "CREATE_TIME")) obj.setCreateTime(rs.getDate("CREATE_TIME"));
			if (hasCol(rs, "MODIFY_USER")) obj.setModifyUser(rs.getString("MODIFY_USER"));
			if (hasCol(rs, "MODIFY_TIME")) obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			if (hasCol(rs, "VERIFY_USER")) obj.setVerifyUser(rs.getString("VERIFY_USER"));
			if (hasCol(rs, "VERIFY_TIME")) obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			if (hasCol(rs, "ENTITY_FLG")) obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			if (hasCol(rs, "MODIFY_FLG")) obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			if (hasCol(rs, "DEL_FLG")) obj.setDelFlg(rs.getString("DEL_FLG"));
			if (hasCol(rs, "GL_CODE")) obj.setGlCode(rs.getString("GL_CODE"));
			if (hasCol(rs, "GL_SUB_CODE")) obj.setGlSubCode(rs.getString("GL_SUB_CODE"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_RESUB_Detail_Entity
	// =========================================================
	public static class M_CA2_RESUB_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInPula;
		private java.util.Date reportDate;
		private String reportName;
		private String createUser;
		private java.util.Date createTime;
		private String modifyUser;
		private java.util.Date modifyTime;
		private String verifyUser;
		private java.util.Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String glCode;
		private String glSubCode;

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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public java.util.Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(java.util.Date reportDate) {
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

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.util.Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public java.util.Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(java.util.Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public java.util.Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(java.util.Date verifyTime) {
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

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String glCode) {
			this.glCode = glCode;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String glSubCode) {
			this.glSubCode = glSubCode;
		}
	}

	class M_CA2ResubDetailRowMapper implements RowMapper<M_CA2_RESUB_Detail_Entity> {
		@Override
		public M_CA2_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_RESUB_Detail_Entity obj = new M_CA2_RESUB_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
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
			obj.setGlCode(rs.getString("GL_CODE"));
			obj.setGlSubCode(rs.getString("GL_SUB_CODE"));
			return obj;
		}
	}

}
