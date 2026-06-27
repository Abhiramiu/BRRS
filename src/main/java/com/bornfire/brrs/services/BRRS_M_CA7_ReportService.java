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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_CA7_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA7_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public List<M_CA7_Summary_Entity> getSummaryByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_CA7_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_CA7SummaryRowMapper());
	}

	public List<M_CA7_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_CA7_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new M_CA7ArchivalSummaryRowMapper());
	}

	public List<M_CA7_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		String sql = "SELECT * FROM BRRS_M_CA7_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_CA7ArchivalSummaryRowMapper());
	}

	public List<M_CA7_Archival_Summary_Entity> getArchivalSummaryWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_CA7_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_CA7ArchivalSummaryRowMapper());
	}

	public BigDecimal getLatestArchivalVersionByDate(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_CA7_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<M_CA7_Detail_Entity> getDetailByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_CA7_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_CA7DetailRowMapper());
	}

	public List<M_CA7_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_CA7_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new M_CA7ArchivalDetailRowMapper());
	}

	public List<M_CA7_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_CA7_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new M_CA7ResubSummaryRowMapper());
	}

	public List<M_CA7_RESUB_Summary_Entity> getResubSummaryWithVersion() {
		String sql = "SELECT * FROM BRRS_M_CA7_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_CA7ResubSummaryRowMapper());
	}

	public List<M_CA7_RESUB_Summary_Entity> getResubSummaryWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_CA7_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_CA7ResubSummaryRowMapper());
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_CA7_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<M_CA7_RESUB_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_CA7_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new M_CA7ResubDetailRowMapper());
	}

	public ModelAndView getM_CA7View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

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
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_CA7_Archival_Summary_Entity> T1Master = getArchivalSummaryByDateAndVersion(d1, version);

				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_CA7_RESUB_Summary_Entity> T1Master = getResubSummaryByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_CA7_Summary_Entity> T1Master = getSummaryByDate(d1);

				System.out.println("T1Master Size: " + T1Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CA7_Archival_Detail_Entity> T1Master = getArchivalDetailByDateAndVersion(
							dateformat.parse(todate), version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA7_RESUB_Detail_Entity> T1Master = getResubDetailByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CA7_Detail_Entity> T1Master = getDetailByDate(dateformat.parse(todate));

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA7");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	public void updateReport(M_CA7_Summary_Entity Entity) {
		System.out.println("Report Date: " + Entity.getReportDate());
		List<M_CA7_Summary_Entity> results = getSummaryByDate(Entity.getReportDate());
		if (results.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + Entity.getReportDate());
		}
		M_CA7_Summary_Entity existing = results.get(0);

		try {
			String[] fields = { "pre_ifrs_pro", "post_ifrs9_pro", "trans_amt" };

			String prefix = "R12_";

			for (String field : fields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_CA7_Summary_Entity.class.getMethod(getterName);
					Method setter = M_CA7_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(Entity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}
			for (int i = 19; i <= 22; i++) {
				String prefix1 = "R" + i + "_";
				String[] fields1 = { "amt_add_year" + (i - 18) };
				// R19 -> amt_add_year1, R20 -> amt_add_year2, etc.

				for (String field : fields1) {
					String getterName = "get" + prefix1 + field;
					String setterName = "set" + prefix1 + field;

					try {
						Method getter = M_CA7_Summary_Entity.class.getMethod(getterName);
						Method setter = M_CA7_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(Entity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating R35 fields", e);
		}

		jdbcTemplate.update(
				"UPDATE BRRS_M_CA7_SUMMARYTABLE SET " + "R12_PRE_IFRS_PRO=?, R12_POST_IFRS9_PRO=?, R12_TRANS_AMT=?, "
						+ "R19_PRODUCT=?, R19_CAP_YEAR1=?, R19_AMT_ADD_YEAR1=?, "
						+ "R20_PRODUCT=?, R20_CAP_YEAR2=?, R20_AMT_ADD_YEAR2=?, "
						+ "R21_PRODUCT=?, R21_CAP_YEAR3=?, R21_AMT_ADD_YEAR3=?, "
						+ "R22_PRODUCT=?, R22_CAP_YEAR4=?, R22_AMT_ADD_YEAR4=?, "
						+ "REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?, "
						+ "ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=? " + "WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				existing.getR12_pre_ifrs_pro(), existing.getR12_post_ifrs9_pro(), existing.getR12_trans_amt(),
				existing.getR19_product(), existing.getR19_cap_year1(), existing.getR19_amt_add_year1(),
				existing.getR20_product(), existing.getR20_cap_year2(), existing.getR20_amt_add_year2(),
				existing.getR21_product(), existing.getR21_cap_year3(), existing.getR21_amt_add_year3(),
				existing.getR22_product(), existing.getR22_cap_year4(), existing.getR22_amt_add_year4(),
				existing.getReportVersion(), existing.getReport_frequency(), existing.getReport_code(),
				existing.getReport_desc(), existing.getEntity_flg(), existing.getModify_flg(), existing.getDel_flg(),
				existing.getReportDate());
	}

	public void updateDetail(M_CA7_Detail_Entity Entity) {
		System.out.println("Report Date: " + Entity.getReportDate());
		List<M_CA7_Detail_Entity> results = getDetailByDate(Entity.getReportDate());
		if (results.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + Entity.getReportDate());
		}
		M_CA7_Detail_Entity existing = results.get(0);

		try {
			String[] fields = { "pre_ifrs_pro", "post_ifrs9_pro", "trans_amt" };

			String prefix = "R12_";

			for (String field : fields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_CA7_Detail_Entity.class.getMethod(getterName);
					Method setter = M_CA7_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(Entity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}
			for (int i = 19; i <= 22; i++) {
				String prefix1 = "R" + i + "_";
				String[] fields1 = { "amt_add_year" + (i - 18) };
				// R19 -> amt_add_year1, R20 -> amt_add_year2, etc.

				for (String field : fields1) {
					String getterName = "get" + prefix1 + field;
					String setterName = "set" + prefix1 + field;

					try {
						Method getter = M_CA7_Detail_Entity.class.getMethod(getterName);
						Method setter = M_CA7_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(Entity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating R35 fields", e);
		}

		jdbcTemplate.update(
				"UPDATE BRRS_M_CA7_DETAILTABLE SET " + "R12_PRE_IFRS_PRO=?, R12_POST_IFRS9_PRO=?, R12_TRANS_AMT=?, "
						+ "R19_PRODUCT=?, R19_CAP_YEAR1=?, R19_AMT_ADD_YEAR1=?, "
						+ "R20_PRODUCT=?, R20_CAP_YEAR2=?, R20_AMT_ADD_YEAR2=?, "
						+ "R21_PRODUCT=?, R21_CAP_YEAR3=?, R21_AMT_ADD_YEAR3=?, "
						+ "R22_PRODUCT=?, R22_CAP_YEAR4=?, R22_AMT_ADD_YEAR4=?, "
						+ "REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?, "
						+ "ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=? " + "WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				existing.getR12_pre_ifrs_pro(), existing.getR12_post_ifrs9_pro(), existing.getR12_trans_amt(),
				existing.getR19_product(), existing.getR19_cap_year1(), existing.getR19_amt_add_year1(),
				existing.getR20_product(), existing.getR20_cap_year2(), existing.getR20_amt_add_year2(),
				existing.getR21_product(), existing.getR21_cap_year3(), existing.getR21_amt_add_year3(),
				existing.getR22_product(), existing.getR22_cap_year4(), existing.getR22_amt_add_year4(),
				existing.getReportVersion(), existing.getReport_frequency(), existing.getReport_code(),
				existing.getReport_desc(), existing.getEntity_flg(), existing.getModify_flg(), existing.getDel_flg(),
				existing.getReportDate());
	}

	public void updateResubReport(M_CA7_RESUB_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// 1. GET CURRENT VERSION FROM RESUB TABLE
		BigDecimal maxResubVer = findMaxResubVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// 2. RESUB SUMMARY
		M_CA7_RESUB_Summary_Entity resubSummary = new M_CA7_RESUB_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");
		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// 3. RESUB DETAIL
		M_CA7_RESUB_Detail_Entity resubDetail = new M_CA7_RESUB_Detail_Entity();
		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");
		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// 4. ARCHIVAL SUMMARY
		M_CA7_Archival_Summary_Entity archSummary = new M_CA7_Archival_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");
		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion);
		archSummary.setReportResubDate(now);

		// 5. ARCHIVAL DETAIL
		M_CA7_Archival_Detail_Entity archDetail = new M_CA7_Archival_Detail_Entity();
		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");
		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion);
		archDetail.setReportResubDate(now);

		// 6. INSERT ALL FOUR TABLES
		String insertSql = "INSERT INTO %s (REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, "
				+ "R12_PRE_IFRS_PRO, R12_POST_IFRS9_PRO, R12_TRANS_AMT, "
				+ "R19_PRODUCT, R19_CAP_YEAR1, R19_AMT_ADD_YEAR1, " + "R20_PRODUCT, R20_CAP_YEAR2, R20_AMT_ADD_YEAR2, "
				+ "R21_PRODUCT, R21_CAP_YEAR3, R21_AMT_ADD_YEAR3, " + "R22_PRODUCT, R22_CAP_YEAR4, R22_AMT_ADD_YEAR4, "
				+ "REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(String.format(insertSql, "BRRS_M_CA7_RESUB_SUMMARYTABLE"), resubSummary.getReportDate(),
				resubSummary.getReportVersion(), resubSummary.getReportResubDate(), resubSummary.getR12_pre_ifrs_pro(),
				resubSummary.getR12_post_ifrs9_pro(), resubSummary.getR12_trans_amt(), resubSummary.getR19_product(),
				resubSummary.getR19_cap_year1(), resubSummary.getR19_amt_add_year1(), resubSummary.getR20_product(),
				resubSummary.getR20_cap_year2(), resubSummary.getR20_amt_add_year2(), resubSummary.getR21_product(),
				resubSummary.getR21_cap_year3(), resubSummary.getR21_amt_add_year3(), resubSummary.getR22_product(),
				resubSummary.getR22_cap_year4(), resubSummary.getR22_amt_add_year4(),
				resubSummary.getReport_frequency(), resubSummary.getReport_code(), resubSummary.getReport_desc(),
				resubSummary.getEntity_flg(), resubSummary.getModify_flg(), resubSummary.getDel_flg());

		jdbcTemplate.update(String.format(insertSql, "BRRS_M_CA7_RESUB_DETAILTABLE"), resubDetail.getReportDate(),
				resubDetail.getReportVersion(), resubDetail.getReportResubDate(), resubDetail.getR12_pre_ifrs_pro(),
				resubDetail.getR12_post_ifrs9_pro(), resubDetail.getR12_trans_amt(), resubDetail.getR19_product(),
				resubDetail.getR19_cap_year1(), resubDetail.getR19_amt_add_year1(), resubDetail.getR20_product(),
				resubDetail.getR20_cap_year2(), resubDetail.getR20_amt_add_year2(), resubDetail.getR21_product(),
				resubDetail.getR21_cap_year3(), resubDetail.getR21_amt_add_year3(), resubDetail.getR22_product(),
				resubDetail.getR22_cap_year4(), resubDetail.getR22_amt_add_year4(), resubDetail.getReport_frequency(),
				resubDetail.getReport_code(), resubDetail.getReport_desc(), resubDetail.getEntity_flg(),
				resubDetail.getModify_flg(), resubDetail.getDel_flg());

		jdbcTemplate.update(String.format(insertSql, "BRRS_M_CA7_ARCHIVALTABLE_SUMMARY"), archSummary.getReportDate(),
				archSummary.getReportVersion(), archSummary.getReportResubDate(), archSummary.getR12_pre_ifrs_pro(),
				archSummary.getR12_post_ifrs9_pro(), archSummary.getR12_trans_amt(), archSummary.getR19_product(),
				archSummary.getR19_cap_year1(), archSummary.getR19_amt_add_year1(), archSummary.getR20_product(),
				archSummary.getR20_cap_year2(), archSummary.getR20_amt_add_year2(), archSummary.getR21_product(),
				archSummary.getR21_cap_year3(), archSummary.getR21_amt_add_year3(), archSummary.getR22_product(),
				archSummary.getR22_cap_year4(), archSummary.getR22_amt_add_year4(), archSummary.getReport_frequency(),
				archSummary.getReport_code(), archSummary.getReport_desc(), archSummary.getEntity_flg(),
				archSummary.getModify_flg(), archSummary.getDel_flg());

		jdbcTemplate.update(String.format(insertSql, "BRRS_M_CA7_ARCHIVALTABLE_DETAIL"), archDetail.getReportDate(),
				archDetail.getReportVersion(), archDetail.getReportResubDate(), archDetail.getR12_pre_ifrs_pro(),
				archDetail.getR12_post_ifrs9_pro(), archDetail.getR12_trans_amt(), archDetail.getR19_product(),
				archDetail.getR19_cap_year1(), archDetail.getR19_amt_add_year1(), archDetail.getR20_product(),
				archDetail.getR20_cap_year2(), archDetail.getR20_amt_add_year2(), archDetail.getR21_product(),
				archDetail.getR21_cap_year3(), archDetail.getR21_amt_add_year3(), archDetail.getR22_product(),
				archDetail.getR22_cap_year4(), archDetail.getR22_amt_add_year4(), archDetail.getReport_frequency(),
				archDetail.getReport_code(), archDetail.getReport_desc(), archDetail.getEntity_flg(),
				archDetail.getModify_flg(), archDetail.getDel_flg());
	}

	// Resubmit the values , latest version and Resub Date
	public void updateReportReSub(M_CA7_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		Date reportDate = updatedEntity.getReportDate();
		BigDecimal newVersion = BigDecimal.ONE;

		try {
			// Fetch the latest archival version for this report date
			BigDecimal latestVersion = getLatestArchivalVersionByDate(reportDate);

			// Determine next version number
			if (latestVersion != null) {
				try {
					newVersion = latestVersion.add(BigDecimal.ONE);
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = BigDecimal.ONE;
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// Prevent duplicate version number
			boolean exists = !getArchivalSummaryByDateAndVersion(reportDate, newVersion).isEmpty();

			if (exists) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy summary entity to archival entity
			M_CA7_Archival_Summary_Entity archivalEntity = new M_CA7_Archival_Summary_Entity();
			BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReportDate(reportDate);
			archivalEntity.setReportVersion(newVersion);
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);

			jdbcTemplate.update(
					"INSERT INTO BRRS_M_CA7_ARCHIVALTABLE_SUMMARY " + "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, "
							+ "R12_PRE_IFRS_PRO, R12_POST_IFRS9_PRO, R12_TRANS_AMT, "
							+ "R19_PRODUCT, R19_CAP_YEAR1, R19_AMT_ADD_YEAR1, "
							+ "R20_PRODUCT, R20_CAP_YEAR2, R20_AMT_ADD_YEAR2, "
							+ "R21_PRODUCT, R21_CAP_YEAR3, R21_AMT_ADD_YEAR3, "
							+ "R22_PRODUCT, R22_CAP_YEAR4, R22_AMT_ADD_YEAR4, "
							+ "REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					archivalEntity.getReportDate(), archivalEntity.getReportVersion(),
					archivalEntity.getReportResubDate(), archivalEntity.getR12_pre_ifrs_pro(),
					archivalEntity.getR12_post_ifrs9_pro(), archivalEntity.getR12_trans_amt(),
					archivalEntity.getR19_product(), archivalEntity.getR19_cap_year1(),
					archivalEntity.getR19_amt_add_year1(), archivalEntity.getR20_product(),
					archivalEntity.getR20_cap_year2(), archivalEntity.getR20_amt_add_year2(),
					archivalEntity.getR21_product(), archivalEntity.getR21_cap_year3(),
					archivalEntity.getR21_amt_add_year3(), archivalEntity.getR22_product(),
					archivalEntity.getR22_cap_year4(), archivalEntity.getR22_amt_add_year4(),
					archivalEntity.getReport_frequency(), archivalEntity.getReport_code(),
					archivalEntity.getReport_desc(), archivalEntity.getEntity_flg(), archivalEntity.getModify_flg(),
					archivalEntity.getDel_flg());

			System.out.println(" Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}

	/// RESUB VIEW
	public List<Object[]> getM_CA7Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA7_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA7_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CA7 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_CA7Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA7_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA7_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA7_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA7 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// NORMAL FORMAT EXCEL
	public byte[] getM_CA7Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

// Convert string to Date
		Date reportDate = dateformat.parse(todate);
// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_CA7ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA7ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return getEmail_M_CA7Excel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				List<M_CA7_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for M_CA7 report. Returning empty result.");
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
					numberStyle.setAlignment(HorizontalAlignment.CENTER);
					numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					// --- End of Style Definitions ---

					try {

						// Row 5 = Excel row 6
						Row dateRow = sheet.getRow(5);

						if (dateRow == null) {
							dateRow = sheet.createRow(5);
						}

						// Column 2 = Excel column C
						Cell dateCell = dateRow.getCell(2);

						if (dateCell == null) {
							dateCell = dateRow.createCell(2);
						}

						// Date conversion
						SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

						SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

						Date reportDateValue = inputFormat.parse(todate);

						// Set formatted date
						dateCell.setCellValue(outputFormat.format(reportDateValue));

						dateCell.setCellStyle(textStyle);

					} catch (ParseException e) {

						logger.error("Error parsing todate: {}", todate, e);
					}

					int startRow = 11;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_CA7_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row12
							// Column b
							Cell cell1 = row.getCell(1);
							if (record.getR12_pre_ifrs_pro() != null) {
								cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row12
							// Column c
							cell1 = row.getCell(2);
							if (record.getR12_post_ifrs9_pro() != null) {
								cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row12
							// Column d

							cell1 = row.getCell(3);
							if (record.getR12_trans_amt() != null) {
								cell1.setCellValue(record.getR12_trans_amt().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row19
							// Column c
							/*
							 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
							 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
							 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
							 * cell1.setCellStyle(textStyle); }
							 */

							// row19
							// Column d
							cell1 = row.getCell(3);
							if (record.getR19_amt_add_year1() != null) {
								cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// row20
							// Column c
							/*
							 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
							 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
							 * cell1.setCellStyle(numberStyle);
							 * 
							 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							 */
							// row20
							// Column d
							cell1 = row.getCell(3);
							if (record.getR20_amt_add_year2() != null) {
								cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row21
							// Column c
							/*
							 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
							 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
							 * cell1.setCellStyle(numberStyle);
							 * 
							 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
							 */
							// row21
							// Column d
							cell1 = row.getCell(3);
							if (record.getR21_amt_add_year3() != null) {
								cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row22
							// Column c

							row = sheet.getRow(21);
							cell1 = row.getCell(2);
							if (record.getR22_cap_year4() != null) {
								cell1.setCellValue(record.getR22_cap_year4().doubleValue());
								cell1.setCellStyle(numberStyle);

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row22
							// Column d
							cell1 = row.getCell(3);
							if (record.getR22_amt_add_year4() != null) {
								cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
						}
						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
					} else {

					}

// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA7 SUMMARY", null,
								"BRRS_M_CA7_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// NORMAL EMAIL EXCEL
	public byte[] getEmail_M_CA7Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Normal EMAIL EXCEL.");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		List<M_CA7_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA7 report. Returning empty result.");
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
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(5);

				if (dateRow == null) {
					dateRow = sheet.createRow(5);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA7_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column b
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column c
					cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column d

					cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19
					// Column c
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */

					// row19
					// Column d
					cell1 = row.getCell(3);
					if (record.getR19_amt_add_year1() != null) {
						cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row20
					// Column c
					/*
					 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
					 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row20
					// Column d
					cell1 = row.getCell(3);
					if (record.getR20_amt_add_year2() != null) {
						cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21
					// Column c
					/*
					 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
					 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row21
					// Column d
					cell1 = row.getCell(3);
					if (record.getR21_amt_add_year3() != null) {
						cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column c

					row = sheet.getRow(21);
					cell1 = row.getCell(2);
					if (record.getR22_cap_year4() != null) {
						cell1.setCellValue(record.getR22_cap_year4().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column d
					cell1 = row.getCell(3);
					if (record.getR22_amt_add_year4() != null) {
						cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA7 EMAIL SUMMARY", null,
						"BRRS_M_CA7_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// ARCHIVAL FORMAT EXCEL
	public byte[] getExcelM_CA7ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting ARCHIVAL FORMAT EXCEL.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA7ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA7_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_ca7 report. Returning empty result.");
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
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(5);

				if (dateRow == null) {
					dateRow = sheet.createRow(5);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA7_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column b
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column c
					cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column d

					cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19
					// Column c
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */
					// row19
					// Column d
					cell1 = row.getCell(3);
					if (record.getR19_amt_add_year1() != null) {
						cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row20
					// Column c
					/*
					 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
					 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row20
					// Column d
					cell1 = row.getCell(3);
					if (record.getR20_amt_add_year2() != null) {
						cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21
					// Column c
					/*
					 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
					 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row21
					// Column d
					cell1 = row.getCell(3);
					if (record.getR21_amt_add_year3() != null) {
						cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column c

					row = sheet.getRow(21);
					cell1 = row.getCell(2);
					if (record.getR22_cap_year4() != null) {
						cell1.setCellValue(record.getR22_cap_year4().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column d
					cell1 = row.getCell(3);
					if (record.getR22_amt_add_year4() != null) {
						cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA7 ARCHIVAL SUMMARY", null,
						"BRRS_M_CA7_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// ARCHIVAL EMAIL EXCEL
	public byte[] BRRS_M_CA7ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Archival EMAIL EXCEL.");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		List<M_CA7_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA7 report. Returning empty result.");
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
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(5);

				if (dateRow == null) {
					dateRow = sheet.createRow(5);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA7_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column b
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column c
					cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column d

					cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19
					// Column c
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */

					// row19
					// Column d
					cell1 = row.getCell(3);
					if (record.getR19_amt_add_year1() != null) {
						cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row20
					// Column c
					/*
					 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
					 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row20
					// Column d
					cell1 = row.getCell(3);
					if (record.getR20_amt_add_year2() != null) {
						cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21
					// Column c
					/*
					 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
					 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row21
					// Column d
					cell1 = row.getCell(3);
					if (record.getR21_amt_add_year3() != null) {
						cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column c

					row = sheet.getRow(21);
					cell1 = row.getCell(2);
					if (record.getR22_cap_year4() != null) {
						cell1.setCellValue(record.getR22_cap_year4().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column d
					cell1 = row.getCell(3);
					if (record.getR22_amt_add_year4() != null) {
						cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA7 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_CA7_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// RESUB FORMAT EXCEL
	public byte[] BRRS_M_CA7ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB FORMAT EXCEL.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA7RESUBEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA7_RESUB_Summary_Entity> dataList1 = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_CA7 report. Returning empty result.");
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
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(5);

				if (dateRow == null) {
					dateRow = sheet.createRow(5);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_CA7_RESUB_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column b
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column c
					cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column d

					cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19
					// Column c
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */

					// row19
					// Column d
					cell1 = row.getCell(3);
					if (record.getR19_amt_add_year1() != null) {
						cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row20
					// Column c
					/*
					 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
					 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row20
					// Column d
					cell1 = row.getCell(3);
					if (record.getR20_amt_add_year2() != null) {
						cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21
					// Column c
					/*
					 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
					 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row21
					// Column d
					cell1 = row.getCell(3);
					if (record.getR21_amt_add_year3() != null) {
						cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column c

					row = sheet.getRow(21);
					cell1 = row.getCell(2);
					if (record.getR22_cap_year4() != null) {
						cell1.setCellValue(record.getR22_cap_year4().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column d
					cell1 = row.getCell(3);
					if (record.getR22_amt_add_year4() != null) {
						cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA7 RESUB SUMMARY", null,
						"BRRS_M_CA7_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// RESUB EMAIL EXCEL
	public byte[] BRRS_M_CA7RESUBEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: STARTING RESUB EMAIL FORMAT EXCEL");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		List<M_CA7_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA7 report. Returning empty result.");
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
			numberStyle.setAlignment(HorizontalAlignment.CENTER);
			numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			try {

				// Row 5 = Excel row 6
				Row dateRow = sheet.getRow(5);

				if (dateRow == null) {
					dateRow = sheet.createRow(5);
				}

				// Column 2 = Excel column C
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA7_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column b
					Cell cell1 = row.getCell(1);
					if (record.getR12_pre_ifrs_pro() != null) {
						cell1.setCellValue(record.getR12_pre_ifrs_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column c
					cell1 = row.getCell(2);
					if (record.getR12_post_ifrs9_pro() != null) {
						cell1.setCellValue(record.getR12_post_ifrs9_pro().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row12
					// Column d

					cell1 = row.getCell(3);
					if (record.getR12_trans_amt() != null) {
						cell1.setCellValue(record.getR12_trans_amt().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19
					// Column c
					/*
					 * row=sheet.getRow(18); cell1 = row.getCell(2); if (record.getR19_cap_year1()!=
					 * null) { cell1.setCellValue(record.getR19_cap_year1().toString() + "%");
					 * cell1.setCellStyle(numberStyle); } else { cell1.setCellValue("");
					 * cell1.setCellStyle(textStyle); }
					 */

					// row19
					// Column d
					cell1 = row.getCell(3);
					if (record.getR19_amt_add_year1() != null) {
						cell1.setCellValue(record.getR19_amt_add_year1().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row20
					// Column c
					/*
					 * row=sheet.getRow(19); cell1 = row.getCell(2); if (record.getR20_cap_year2()!=
					 * null) { cell1.setCellValue(record.getR20_cap_year2().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row20
					// Column d
					cell1 = row.getCell(3);
					if (record.getR20_amt_add_year2() != null) {
						cell1.setCellValue(record.getR20_amt_add_year2().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21
					// Column c
					/*
					 * row=sheet.getRow(20); cell1 = row.getCell(2); if (record.getR21_cap_year3()!=
					 * null) { cell1.setCellValue(record.getR21_cap_year3().toString() + "%");
					 * cell1.setCellStyle(numberStyle);
					 * 
					 * } else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					 */
					// row21
					// Column d
					cell1 = row.getCell(3);
					if (record.getR21_amt_add_year3() != null) {
						cell1.setCellValue(record.getR21_amt_add_year3().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column c

					row = sheet.getRow(21);
					cell1 = row.getCell(2);
					if (record.getR22_cap_year4() != null) {
						cell1.setCellValue(record.getR22_cap_year4().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22
					// Column d
					cell1 = row.getCell(3);
					if (record.getR22_amt_add_year4() != null) {
						cell1.setCellValue(record.getR22_amt_add_year4().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA7 EMAIL RESUB SUMMARY", null,
						"BRRS_M_CA7_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// ============================================================
	// INNER ENTITY CLASSES
	// ============================================================

	public static class M_CA7_Summary_Entity {
		private BigDecimal r12_pre_ifrs_pro;
		private BigDecimal r12_post_ifrs9_pro;
		private BigDecimal r12_trans_amt;
		private String r19_product;
		private BigDecimal r19_cap_year1;
		private BigDecimal r19_amt_add_year1;
		private String r20_product;
		private BigDecimal r20_cap_year2;
		private BigDecimal r20_amt_add_year2;
		private String r21_product;
		private BigDecimal r21_cap_year3;
		private BigDecimal r21_amt_add_year3;
		private String r22_product;
		private BigDecimal r22_cap_year4;
		private BigDecimal r22_amt_add_year4;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}

		public void setR12_pre_ifrs_pro(BigDecimal v) {
			this.r12_pre_ifrs_pro = v;
		}

		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}

		public void setR12_post_ifrs9_pro(BigDecimal v) {
			this.r12_post_ifrs9_pro = v;
		}

		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}

		public void setR12_trans_amt(BigDecimal v) {
			this.r12_trans_amt = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			this.r19_product = v;
		}

		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}

		public void setR19_cap_year1(BigDecimal v) {
			this.r19_cap_year1 = v;
		}

		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}

		public void setR19_amt_add_year1(BigDecimal v) {
			this.r19_amt_add_year1 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			this.r20_product = v;
		}

		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}

		public void setR20_cap_year2(BigDecimal v) {
			this.r20_cap_year2 = v;
		}

		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}

		public void setR20_amt_add_year2(BigDecimal v) {
			this.r20_amt_add_year2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			this.r21_product = v;
		}

		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}

		public void setR21_cap_year3(BigDecimal v) {
			this.r21_cap_year3 = v;
		}

		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}

		public void setR21_amt_add_year3(BigDecimal v) {
			this.r21_amt_add_year3 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			this.r22_product = v;
		}

		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}

		public void setR22_cap_year4(BigDecimal v) {
			this.r22_cap_year4 = v;
		}

		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}

		public void setR22_amt_add_year4(BigDecimal v) {
			this.r22_amt_add_year4 = v;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date v) {
			this.reportDate = v;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal v) {
			this.reportVersion = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			this.report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			this.report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			this.report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			this.entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			this.modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			this.del_flg = v;
		}
	}

	public static class M_CA7_Detail_Entity {
		private BigDecimal r12_pre_ifrs_pro;
		private BigDecimal r12_post_ifrs9_pro;
		private BigDecimal r12_trans_amt;
		private String r19_product;
		private BigDecimal r19_cap_year1;
		private BigDecimal r19_amt_add_year1;
		private String r20_product;
		private BigDecimal r20_cap_year2;
		private BigDecimal r20_amt_add_year2;
		private String r21_product;
		private BigDecimal r21_cap_year3;
		private BigDecimal r21_amt_add_year3;
		private String r22_product;
		private BigDecimal r22_cap_year4;
		private BigDecimal r22_amt_add_year4;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}

		public void setR12_pre_ifrs_pro(BigDecimal v) {
			this.r12_pre_ifrs_pro = v;
		}

		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}

		public void setR12_post_ifrs9_pro(BigDecimal v) {
			this.r12_post_ifrs9_pro = v;
		}

		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}

		public void setR12_trans_amt(BigDecimal v) {
			this.r12_trans_amt = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			this.r19_product = v;
		}

		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}

		public void setR19_cap_year1(BigDecimal v) {
			this.r19_cap_year1 = v;
		}

		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}

		public void setR19_amt_add_year1(BigDecimal v) {
			this.r19_amt_add_year1 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			this.r20_product = v;
		}

		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}

		public void setR20_cap_year2(BigDecimal v) {
			this.r20_cap_year2 = v;
		}

		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}

		public void setR20_amt_add_year2(BigDecimal v) {
			this.r20_amt_add_year2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			this.r21_product = v;
		}

		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}

		public void setR21_cap_year3(BigDecimal v) {
			this.r21_cap_year3 = v;
		}

		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}

		public void setR21_amt_add_year3(BigDecimal v) {
			this.r21_amt_add_year3 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			this.r22_product = v;
		}

		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}

		public void setR22_cap_year4(BigDecimal v) {
			this.r22_cap_year4 = v;
		}

		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}

		public void setR22_amt_add_year4(BigDecimal v) {
			this.r22_amt_add_year4 = v;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date v) {
			this.reportDate = v;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal v) {
			this.reportVersion = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			this.report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			this.report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			this.report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			this.entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			this.modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			this.del_flg = v;
		}
	}

	public static class M_CA7_Archival_Summary_Entity {
		private BigDecimal r12_pre_ifrs_pro;
		private BigDecimal r12_post_ifrs9_pro;
		private BigDecimal r12_trans_amt;
		private String r19_product;
		private BigDecimal r19_cap_year1;
		private BigDecimal r19_amt_add_year1;
		private String r20_product;
		private BigDecimal r20_cap_year2;
		private BigDecimal r20_amt_add_year2;
		private String r21_product;
		private BigDecimal r21_cap_year3;
		private BigDecimal r21_amt_add_year3;
		private String r22_product;
		private BigDecimal r22_cap_year4;
		private BigDecimal r22_amt_add_year4;
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}

		public void setR12_pre_ifrs_pro(BigDecimal v) {
			this.r12_pre_ifrs_pro = v;
		}

		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}

		public void setR12_post_ifrs9_pro(BigDecimal v) {
			this.r12_post_ifrs9_pro = v;
		}

		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}

		public void setR12_trans_amt(BigDecimal v) {
			this.r12_trans_amt = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			this.r19_product = v;
		}

		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}

		public void setR19_cap_year1(BigDecimal v) {
			this.r19_cap_year1 = v;
		}

		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}

		public void setR19_amt_add_year1(BigDecimal v) {
			this.r19_amt_add_year1 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			this.r20_product = v;
		}

		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}

		public void setR20_cap_year2(BigDecimal v) {
			this.r20_cap_year2 = v;
		}

		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}

		public void setR20_amt_add_year2(BigDecimal v) {
			this.r20_amt_add_year2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			this.r21_product = v;
		}

		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}

		public void setR21_cap_year3(BigDecimal v) {
			this.r21_cap_year3 = v;
		}

		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}

		public void setR21_amt_add_year3(BigDecimal v) {
			this.r21_amt_add_year3 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			this.r22_product = v;
		}

		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}

		public void setR22_cap_year4(BigDecimal v) {
			this.r22_cap_year4 = v;
		}

		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}

		public void setR22_amt_add_year4(BigDecimal v) {
			this.r22_amt_add_year4 = v;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date v) {
			this.reportDate = v;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal v) {
			this.reportVersion = v;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date v) {
			this.reportResubDate = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			this.report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			this.report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			this.report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			this.entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			this.modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			this.del_flg = v;
		}
	}

	public static class M_CA7_Archival_Detail_Entity {
		private BigDecimal r12_pre_ifrs_pro;
		private BigDecimal r12_post_ifrs9_pro;
		private BigDecimal r12_trans_amt;
		private String r19_product;
		private BigDecimal r19_cap_year1;
		private BigDecimal r19_amt_add_year1;
		private String r20_product;
		private BigDecimal r20_cap_year2;
		private BigDecimal r20_amt_add_year2;
		private String r21_product;
		private BigDecimal r21_cap_year3;
		private BigDecimal r21_amt_add_year3;
		private String r22_product;
		private BigDecimal r22_cap_year4;
		private BigDecimal r22_amt_add_year4;
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}

		public void setR12_pre_ifrs_pro(BigDecimal v) {
			this.r12_pre_ifrs_pro = v;
		}

		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}

		public void setR12_post_ifrs9_pro(BigDecimal v) {
			this.r12_post_ifrs9_pro = v;
		}

		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}

		public void setR12_trans_amt(BigDecimal v) {
			this.r12_trans_amt = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			this.r19_product = v;
		}

		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}

		public void setR19_cap_year1(BigDecimal v) {
			this.r19_cap_year1 = v;
		}

		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}

		public void setR19_amt_add_year1(BigDecimal v) {
			this.r19_amt_add_year1 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			this.r20_product = v;
		}

		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}

		public void setR20_cap_year2(BigDecimal v) {
			this.r20_cap_year2 = v;
		}

		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}

		public void setR20_amt_add_year2(BigDecimal v) {
			this.r20_amt_add_year2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			this.r21_product = v;
		}

		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}

		public void setR21_cap_year3(BigDecimal v) {
			this.r21_cap_year3 = v;
		}

		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}

		public void setR21_amt_add_year3(BigDecimal v) {
			this.r21_amt_add_year3 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			this.r22_product = v;
		}

		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}

		public void setR22_cap_year4(BigDecimal v) {
			this.r22_cap_year4 = v;
		}

		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}

		public void setR22_amt_add_year4(BigDecimal v) {
			this.r22_amt_add_year4 = v;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date v) {
			this.reportDate = v;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal v) {
			this.reportVersion = v;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date v) {
			this.reportResubDate = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			this.report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			this.report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			this.report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			this.entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			this.modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			this.del_flg = v;
		}
	}

	public static class M_CA7_RESUB_Summary_Entity {
		private BigDecimal r12_pre_ifrs_pro;
		private BigDecimal r12_post_ifrs9_pro;
		private BigDecimal r12_trans_amt;
		private String r19_product;
		private BigDecimal r19_cap_year1;
		private BigDecimal r19_amt_add_year1;
		private String r20_product;
		private BigDecimal r20_cap_year2;
		private BigDecimal r20_amt_add_year2;
		private String r21_product;
		private BigDecimal r21_cap_year3;
		private BigDecimal r21_amt_add_year3;
		private String r22_product;
		private BigDecimal r22_cap_year4;
		private BigDecimal r22_amt_add_year4;
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}

		public void setR12_pre_ifrs_pro(BigDecimal v) {
			this.r12_pre_ifrs_pro = v;
		}

		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}

		public void setR12_post_ifrs9_pro(BigDecimal v) {
			this.r12_post_ifrs9_pro = v;
		}

		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}

		public void setR12_trans_amt(BigDecimal v) {
			this.r12_trans_amt = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			this.r19_product = v;
		}

		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}

		public void setR19_cap_year1(BigDecimal v) {
			this.r19_cap_year1 = v;
		}

		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}

		public void setR19_amt_add_year1(BigDecimal v) {
			this.r19_amt_add_year1 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			this.r20_product = v;
		}

		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}

		public void setR20_cap_year2(BigDecimal v) {
			this.r20_cap_year2 = v;
		}

		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}

		public void setR20_amt_add_year2(BigDecimal v) {
			this.r20_amt_add_year2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			this.r21_product = v;
		}

		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}

		public void setR21_cap_year3(BigDecimal v) {
			this.r21_cap_year3 = v;
		}

		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}

		public void setR21_amt_add_year3(BigDecimal v) {
			this.r21_amt_add_year3 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			this.r22_product = v;
		}

		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}

		public void setR22_cap_year4(BigDecimal v) {
			this.r22_cap_year4 = v;
		}

		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}

		public void setR22_amt_add_year4(BigDecimal v) {
			this.r22_amt_add_year4 = v;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date v) {
			this.reportDate = v;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal v) {
			this.reportVersion = v;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date v) {
			this.reportResubDate = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			this.report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			this.report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			this.report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			this.entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			this.modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			this.del_flg = v;
		}
	}

	public static class M_CA7_RESUB_Detail_Entity {
		private BigDecimal r12_pre_ifrs_pro;
		private BigDecimal r12_post_ifrs9_pro;
		private BigDecimal r12_trans_amt;
		private String r19_product;
		private BigDecimal r19_cap_year1;
		private BigDecimal r19_amt_add_year1;
		private String r20_product;
		private BigDecimal r20_cap_year2;
		private BigDecimal r20_amt_add_year2;
		private String r21_product;
		private BigDecimal r21_cap_year3;
		private BigDecimal r21_amt_add_year3;
		private String r22_product;
		private BigDecimal r22_cap_year4;
		private BigDecimal r22_amt_add_year4;
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR12_pre_ifrs_pro() {
			return r12_pre_ifrs_pro;
		}

		public void setR12_pre_ifrs_pro(BigDecimal v) {
			this.r12_pre_ifrs_pro = v;
		}

		public BigDecimal getR12_post_ifrs9_pro() {
			return r12_post_ifrs9_pro;
		}

		public void setR12_post_ifrs9_pro(BigDecimal v) {
			this.r12_post_ifrs9_pro = v;
		}

		public BigDecimal getR12_trans_amt() {
			return r12_trans_amt;
		}

		public void setR12_trans_amt(BigDecimal v) {
			this.r12_trans_amt = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			this.r19_product = v;
		}

		public BigDecimal getR19_cap_year1() {
			return r19_cap_year1;
		}

		public void setR19_cap_year1(BigDecimal v) {
			this.r19_cap_year1 = v;
		}

		public BigDecimal getR19_amt_add_year1() {
			return r19_amt_add_year1;
		}

		public void setR19_amt_add_year1(BigDecimal v) {
			this.r19_amt_add_year1 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			this.r20_product = v;
		}

		public BigDecimal getR20_cap_year2() {
			return r20_cap_year2;
		}

		public void setR20_cap_year2(BigDecimal v) {
			this.r20_cap_year2 = v;
		}

		public BigDecimal getR20_amt_add_year2() {
			return r20_amt_add_year2;
		}

		public void setR20_amt_add_year2(BigDecimal v) {
			this.r20_amt_add_year2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			this.r21_product = v;
		}

		public BigDecimal getR21_cap_year3() {
			return r21_cap_year3;
		}

		public void setR21_cap_year3(BigDecimal v) {
			this.r21_cap_year3 = v;
		}

		public BigDecimal getR21_amt_add_year3() {
			return r21_amt_add_year3;
		}

		public void setR21_amt_add_year3(BigDecimal v) {
			this.r21_amt_add_year3 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			this.r22_product = v;
		}

		public BigDecimal getR22_cap_year4() {
			return r22_cap_year4;
		}

		public void setR22_cap_year4(BigDecimal v) {
			this.r22_cap_year4 = v;
		}

		public BigDecimal getR22_amt_add_year4() {
			return r22_amt_add_year4;
		}

		public void setR22_amt_add_year4(BigDecimal v) {
			this.r22_amt_add_year4 = v;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date v) {
			this.reportDate = v;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal v) {
			this.reportVersion = v;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date v) {
			this.reportResubDate = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			this.report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			this.report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			this.report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			this.entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			this.modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			this.del_flg = v;
		}
	}

	// ============================================================
	// ROW MAPPERS
	// ============================================================

	class M_CA7SummaryRowMapper implements RowMapper<M_CA7_Summary_Entity> {
		@Override
		public M_CA7_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA7_Summary_Entity obj = new M_CA7_Summary_Entity();
			obj.setR12_pre_ifrs_pro(rs.getBigDecimal("R12_PRE_IFRS_PRO"));
			obj.setR12_post_ifrs9_pro(rs.getBigDecimal("R12_POST_IFRS9_PRO"));
			obj.setR12_trans_amt(rs.getBigDecimal("R12_TRANS_AMT"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_cap_year1(rs.getBigDecimal("R19_CAP_YEAR1"));
			obj.setR19_amt_add_year1(rs.getBigDecimal("R19_AMT_ADD_YEAR1"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_cap_year2(rs.getBigDecimal("R20_CAP_YEAR2"));
			obj.setR20_amt_add_year2(rs.getBigDecimal("R20_AMT_ADD_YEAR2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_cap_year3(rs.getBigDecimal("R21_CAP_YEAR3"));
			obj.setR21_amt_add_year3(rs.getBigDecimal("R21_AMT_ADD_YEAR3"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_cap_year4(rs.getBigDecimal("R22_CAP_YEAR4"));
			obj.setR22_amt_add_year4(rs.getBigDecimal("R22_AMT_ADD_YEAR4"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	class M_CA7DetailRowMapper implements RowMapper<M_CA7_Detail_Entity> {
		@Override
		public M_CA7_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA7_Detail_Entity obj = new M_CA7_Detail_Entity();
			obj.setR12_pre_ifrs_pro(rs.getBigDecimal("R12_PRE_IFRS_PRO"));
			obj.setR12_post_ifrs9_pro(rs.getBigDecimal("R12_POST_IFRS9_PRO"));
			obj.setR12_trans_amt(rs.getBigDecimal("R12_TRANS_AMT"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_cap_year1(rs.getBigDecimal("R19_CAP_YEAR1"));
			obj.setR19_amt_add_year1(rs.getBigDecimal("R19_AMT_ADD_YEAR1"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_cap_year2(rs.getBigDecimal("R20_CAP_YEAR2"));
			obj.setR20_amt_add_year2(rs.getBigDecimal("R20_AMT_ADD_YEAR2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_cap_year3(rs.getBigDecimal("R21_CAP_YEAR3"));
			obj.setR21_amt_add_year3(rs.getBigDecimal("R21_AMT_ADD_YEAR3"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_cap_year4(rs.getBigDecimal("R22_CAP_YEAR4"));
			obj.setR22_amt_add_year4(rs.getBigDecimal("R22_AMT_ADD_YEAR4"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	class M_CA7ArchivalSummaryRowMapper implements RowMapper<M_CA7_Archival_Summary_Entity> {
		@Override
		public M_CA7_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA7_Archival_Summary_Entity obj = new M_CA7_Archival_Summary_Entity();
			obj.setR12_pre_ifrs_pro(rs.getBigDecimal("R12_PRE_IFRS_PRO"));
			obj.setR12_post_ifrs9_pro(rs.getBigDecimal("R12_POST_IFRS9_PRO"));
			obj.setR12_trans_amt(rs.getBigDecimal("R12_TRANS_AMT"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_cap_year1(rs.getBigDecimal("R19_CAP_YEAR1"));
			obj.setR19_amt_add_year1(rs.getBigDecimal("R19_AMT_ADD_YEAR1"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_cap_year2(rs.getBigDecimal("R20_CAP_YEAR2"));
			obj.setR20_amt_add_year2(rs.getBigDecimal("R20_AMT_ADD_YEAR2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_cap_year3(rs.getBigDecimal("R21_CAP_YEAR3"));
			obj.setR21_amt_add_year3(rs.getBigDecimal("R21_AMT_ADD_YEAR3"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_cap_year4(rs.getBigDecimal("R22_CAP_YEAR4"));
			obj.setR22_amt_add_year4(rs.getBigDecimal("R22_AMT_ADD_YEAR4"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
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

	class M_CA7ArchivalDetailRowMapper implements RowMapper<M_CA7_Archival_Detail_Entity> {
		@Override
		public M_CA7_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA7_Archival_Detail_Entity obj = new M_CA7_Archival_Detail_Entity();
			obj.setR12_pre_ifrs_pro(rs.getBigDecimal("R12_PRE_IFRS_PRO"));
			obj.setR12_post_ifrs9_pro(rs.getBigDecimal("R12_POST_IFRS9_PRO"));
			obj.setR12_trans_amt(rs.getBigDecimal("R12_TRANS_AMT"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_cap_year1(rs.getBigDecimal("R19_CAP_YEAR1"));
			obj.setR19_amt_add_year1(rs.getBigDecimal("R19_AMT_ADD_YEAR1"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_cap_year2(rs.getBigDecimal("R20_CAP_YEAR2"));
			obj.setR20_amt_add_year2(rs.getBigDecimal("R20_AMT_ADD_YEAR2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_cap_year3(rs.getBigDecimal("R21_CAP_YEAR3"));
			obj.setR21_amt_add_year3(rs.getBigDecimal("R21_AMT_ADD_YEAR3"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_cap_year4(rs.getBigDecimal("R22_CAP_YEAR4"));
			obj.setR22_amt_add_year4(rs.getBigDecimal("R22_AMT_ADD_YEAR4"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
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

	class M_CA7ResubSummaryRowMapper implements RowMapper<M_CA7_RESUB_Summary_Entity> {
		@Override
		public M_CA7_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA7_RESUB_Summary_Entity obj = new M_CA7_RESUB_Summary_Entity();
			obj.setR12_pre_ifrs_pro(rs.getBigDecimal("R12_PRE_IFRS_PRO"));
			obj.setR12_post_ifrs9_pro(rs.getBigDecimal("R12_POST_IFRS9_PRO"));
			obj.setR12_trans_amt(rs.getBigDecimal("R12_TRANS_AMT"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_cap_year1(rs.getBigDecimal("R19_CAP_YEAR1"));
			obj.setR19_amt_add_year1(rs.getBigDecimal("R19_AMT_ADD_YEAR1"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_cap_year2(rs.getBigDecimal("R20_CAP_YEAR2"));
			obj.setR20_amt_add_year2(rs.getBigDecimal("R20_AMT_ADD_YEAR2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_cap_year3(rs.getBigDecimal("R21_CAP_YEAR3"));
			obj.setR21_amt_add_year3(rs.getBigDecimal("R21_AMT_ADD_YEAR3"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_cap_year4(rs.getBigDecimal("R22_CAP_YEAR4"));
			obj.setR22_amt_add_year4(rs.getBigDecimal("R22_AMT_ADD_YEAR4"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
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

	class M_CA7ResubDetailRowMapper implements RowMapper<M_CA7_RESUB_Detail_Entity> {
		@Override
		public M_CA7_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA7_RESUB_Detail_Entity obj = new M_CA7_RESUB_Detail_Entity();
			obj.setR12_pre_ifrs_pro(rs.getBigDecimal("R12_PRE_IFRS_PRO"));
			obj.setR12_post_ifrs9_pro(rs.getBigDecimal("R12_POST_IFRS9_PRO"));
			obj.setR12_trans_amt(rs.getBigDecimal("R12_TRANS_AMT"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_cap_year1(rs.getBigDecimal("R19_CAP_YEAR1"));
			obj.setR19_amt_add_year1(rs.getBigDecimal("R19_AMT_ADD_YEAR1"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_cap_year2(rs.getBigDecimal("R20_CAP_YEAR2"));
			obj.setR20_amt_add_year2(rs.getBigDecimal("R20_AMT_ADD_YEAR2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_cap_year3(rs.getBigDecimal("R21_CAP_YEAR3"));
			obj.setR21_amt_add_year3(rs.getBigDecimal("R21_AMT_ADD_YEAR3"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_cap_year4(rs.getBigDecimal("R22_CAP_YEAR4"));
			obj.setR22_amt_add_year4(rs.getBigDecimal("R22_AMT_ADD_YEAR4"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
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

}
