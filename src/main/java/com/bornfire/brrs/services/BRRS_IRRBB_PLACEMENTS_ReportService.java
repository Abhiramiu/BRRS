package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_IRRBB_PLACEMENTS_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_IRRBB_PLACEMENTS_ReportService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	@Autowired
	private AuditService auditService;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ── SUMMARY QUERY ─────────────────────────────────────────────────────────

	public List<IRRBB_PLACEMENTS_Summary_Entity> getdatabydateList(Date reportDate) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_SUMMARY_TABLE a WHERE TRUNC(a.REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new IRRBB_PLACEMENTS_RowMapper());
	}

	// ── SUMMARY LOOKUP BY PK ──────────────────────────────────────────────────

	public IRRBB_PLACEMENTS_Summary_Entity findById(Long sno) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_SUMMARY_TABLE WHERE SNO = ?";
		List<IRRBB_PLACEMENTS_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { sno },
				new IRRBB_PLACEMENTS_RowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	// ── SUMMARY INSERT ────────────────────────────────────────────────────────

	public void saveRecord(IRRBB_PLACEMENTS_Summary_Entity e) {
		String sql = "INSERT INTO BRRS_IRRBB_PLA_SUMMARY_TABLE (" + "  SNO, ACCOUNT_NUMBER, GL_CODE, GL_DESCRIPTION,"
				+ "  ACCOUNT_CURRENCY, ACCOUNT_OPENING_DATE, MATURITY_DATE," + "  OUT_BAL_ACC_CCY, OUT_BAL_INR,"
				+ "  FLOATING_FIXED, EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY,"
				+ "  LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK,"
				+ "  FINAL_ROI_PERCENT, REMARKS, BACID,"
				+ "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
				+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE" + ") VALUES ("
				+ "  BRRS_IRRBB_PLA_SUMMARY_TABLE_SNO_SEQ.NEXTVAL, ?, ?, ?," + "  ?, ?, ?," + "  ?, ?," + "  ?, ?, ?,"
				+ "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?, ?, ?," + "  ?, ?, ?, ?" + ")";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate());
		logger.info("IRRBB_PLACEMENTS saveRecord — new record inserted");
	}

	// ── SUMMARY UPDATE ────────────────────────────────────────────────────────

	public void updateRecord(IRRBB_PLACEMENTS_Summary_Entity e) {
		IRRBB_PLACEMENTS_Summary_Entity oldEntity = findById(e.getSno());
		String sql = "UPDATE BRRS_IRRBB_PLA_SUMMARY_TABLE SET"
				+ "  ACCOUNT_NUMBER = ?, GL_CODE = ?, GL_DESCRIPTION = ?,"
				+ "  ACCOUNT_CURRENCY = ?, ACCOUNT_OPENING_DATE = ?, MATURITY_DATE = ?,"
				+ "  OUT_BAL_ACC_CCY = ?, OUT_BAL_INR = ?,"
				+ "  FLOATING_FIXED = ?, EXISTING_BENCHMARK = ?, EXISTING_REPRICING_FREQUENCY = ?,"
				+ "  LAST_REPRICING_DATE = ?, NEXT_REPRICING_DATE = ?, SPREAD_OVER_BENCHMARK = ?,"
				+ "  FINAL_ROI_PERCENT = ?, REMARKS = ?, BACID = ?,"
				+ "  REPORT_DATE = ?, REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, REPORT_DESC = ?,"
				+ "  ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ?, REPORT_RESUBDATE = ?" + " WHERE SNO = ?";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate(), e.getSno());
		// ── Sync to Detail table (same ACCOUNT_NUMBER + REPORT_DATE) ─────────
		String detailSyncSql = "UPDATE BRRS_IRRBB_PLA_DETAIL_TABLE SET" + "  GL_CODE=?, GL_DESCRIPTION=?,"
				+ "  ACCOUNT_CURRENCY=?, ACCOUNT_OPENING_DATE=?, MATURITY_DATE=?,"
				+ "  OUT_BAL_ACC_CCY=?, OUT_BAL_INR=?,"
				+ "  FLOATING_FIXED=?, EXISTING_BENCHMARK=?, EXISTING_REPRICING_FREQUENCY=?,"
				+ "  LAST_REPRICING_DATE=?, NEXT_REPRICING_DATE=?, SPREAD_OVER_BENCHMARK=?,"
				+ "  FINAL_ROI_PERCENT=?, REMARKS=?, BACID=?,"
				+ "  REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?,"
				+ "  ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=?, REPORT_RESUBDATE=?"
				+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND ACCOUNT_NUMBER = ?";
		jdbcTemplate.update(detailSyncSql, e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportVersion(), e.getReportFrequency(), e.getReportCode(),
				e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(), e.getReportResubdate(),
				e.getReportDate(), e.getAccountNumber());
		logger.info("IRRBB_PLACEMENTS updateRecord — sno={}", e.getSno());

		auditService.compareEntitiesmanual(oldEntity, e, e.getSno().toString(), "IRRBB_PLACEMENTS Summary Screen",
				"BRRS_IRRBB_PLA_SUMMARY_TABLE");
	}

	// ── UNIFIED VIEW HELPER (supports NORMAL / ARCHIVAL / RESUB) ─

	public ModelAndView getBRRS_IRRBB_PLACEMENTS_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		try {
			Date d1 = dateformat.parse(todate);

			boolean isArchival = "ARCHIVAL".equalsIgnoreCase(type) && version != null;
			boolean isResub = "RESUB".equalsIgnoreCase(type) && version != null;

			mv.addObject("archiveMode", isArchival);
			mv.addObject("resubMode", isResub);

			// SUMMARY branch
			if (isArchival) {
				String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary",
						jdbcTemplate.query(sql, new Object[] { d1, version }, new IRRBB_PLACEMENTS_RowMapper()));
			} else if (isResub) {
				String sql = "SELECT * FROM BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary",
						jdbcTemplate.query(sql, new Object[] { d1, version }, new IRRBB_PLACEMENTS_RowMapper()));
			} else {
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", getdatabydateList(d1));
			}

			// DETAIL branch (overlays summary branch when dtltype="detail")
			if ("detail".equalsIgnoreCase(dtltype)) {
				if (isArchival) {
					String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
					mv.addObject("displaymode", "detail");
					mv.addObject("reportdetail",
							jdbcTemplate.query(sql, new Object[] { d1, version }, new IRRBB_PLACEMENTS_RowMapper()));
				} else if (isResub) {
					String sql = "SELECT * FROM BRRS_IRRBB_PLA_RESUBMISSION_DETAIL_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
					mv.addObject("displaymode", "detail");
					mv.addObject("reportdetail",
							jdbcTemplate.query(sql, new Object[] { d1, version }, new IRRBB_PLACEMENTS_RowMapper()));
				} else {
					mv.addObject("displaymode", "detail");
					mv.addObject("reportdetail", getDetailByDateList(d1));
				}
			}

			mv.addObject("asondate", todate);
			mv.addObject("fromdate", fromdate);
			mv.addObject("todate", todate);
			mv.addObject("type", type);
			mv.addObject("version", version);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		mv.setViewName("BRRS/IRRBB_PLACEMENTS");
		return mv;
	}

	// ── DETAIL QUERY ──────────────────────────────────────────────────────────

	public List<IRRBB_PLACEMENTS_Summary_Entity> getDetailByDateList(Date reportDate) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_DETAIL_TABLE a WHERE TRUNC(a.REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new IRRBB_PLACEMENTS_RowMapper());
	}

	// ── DETAIL LOOKUP BY PK ───────────────────────────────────────────────────

	public IRRBB_PLACEMENTS_Summary_Entity findDetailById(Long sno) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_DETAIL_TABLE WHERE SNO = ?";
		List<IRRBB_PLACEMENTS_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { sno },
				new IRRBB_PLACEMENTS_RowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	// ── DETAIL INSERT — writes to BOTH summary and detail tables ──────────────

	public void saveDetailRecord(IRRBB_PLACEMENTS_Summary_Entity e) {
		saveRecord(e);
		String sql = "INSERT INTO BRRS_IRRBB_PLA_DETAIL_TABLE (" + "  SNO, ACCOUNT_NUMBER, GL_CODE, GL_DESCRIPTION,"
				+ "  ACCOUNT_CURRENCY, ACCOUNT_OPENING_DATE, MATURITY_DATE," + "  OUT_BAL_ACC_CCY, OUT_BAL_INR,"
				+ "  FLOATING_FIXED, EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY,"
				+ "  LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK,"
				+ "  FINAL_ROI_PERCENT, REMARKS, BACID,"
				+ "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
				+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE" + ") VALUES ("
				+ "  BRRS_IRRBB_PLA_DETAIL_TABLE_SNO_SEQ.NEXTVAL, ?, ?, ?," + "  ?, ?, ?," + "  ?, ?," + "  ?, ?, ?,"
				+ "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?, ?, ?," + "  ?, ?, ?, ?" + ")";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate());
		logger.info("IRRBB_PLACEMENTS saveDetailRecord — written to both tables");
	}

	// ── DETAIL UPDATE — updates BOTH summary and detail tables ────────────────

	public void updateDetailRecord(IRRBB_PLACEMENTS_Summary_Entity e) {
		IRRBB_PLACEMENTS_Summary_Entity oldDetailEntity = findDetailById(e.getSno());

		// ── Update the Detail row itself (correct — keyed by Detail's own SNO) ──
		String sql = "UPDATE BRRS_IRRBB_PLA_DETAIL_TABLE SET" + "  ACCOUNT_NUMBER=?, GL_CODE=?, GL_DESCRIPTION=?,"
				+ "  ACCOUNT_CURRENCY=?, ACCOUNT_OPENING_DATE=?, MATURITY_DATE=?,"
				+ "  OUT_BAL_ACC_CCY=?, OUT_BAL_INR=?,"
				+ "  FLOATING_FIXED=?, EXISTING_BENCHMARK=?, EXISTING_REPRICING_FREQUENCY=?,"
				+ "  LAST_REPRICING_DATE=?, NEXT_REPRICING_DATE=?, SPREAD_OVER_BENCHMARK=?,"
				+ "  FINAL_ROI_PERCENT=?, REMARKS=?, BACID=?,"
				+ "  REPORT_DATE=?, REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?,"
				+ "  ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=?, REPORT_RESUBDATE=?" + " WHERE SNO=?";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate(), e.getSno());
		logger.info("IRRBB_PLACEMENTS updateDetailRecord — sno={}", e.getSno());

		// ── Sync to Summary table — keyed by ACCOUNT_NUMBER + REPORT_DATE, ──────
		// ── NOT by SNO (Summary and Detail rows use independent sequences) ──────
		String summarySyncSql = "UPDATE BRRS_IRRBB_PLA_SUMMARY_TABLE SET" + "  GL_CODE=?, GL_DESCRIPTION=?,"
				+ "  ACCOUNT_CURRENCY=?, ACCOUNT_OPENING_DATE=?, MATURITY_DATE=?,"
				+ "  OUT_BAL_ACC_CCY=?, OUT_BAL_INR=?,"
				+ "  FLOATING_FIXED=?, EXISTING_BENCHMARK=?, EXISTING_REPRICING_FREQUENCY=?,"
				+ "  LAST_REPRICING_DATE=?, NEXT_REPRICING_DATE=?, SPREAD_OVER_BENCHMARK=?,"
				+ "  FINAL_ROI_PERCENT=?, REMARKS=?, BACID=?,"
				+ "  REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?,"
				+ "  ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=?, REPORT_RESUBDATE=?"
				+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND ACCOUNT_NUMBER = ?";
		jdbcTemplate.update(summarySyncSql, e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportVersion(), e.getReportFrequency(), e.getReportCode(),
				e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(), e.getReportResubdate(),
				e.getReportDate(), e.getAccountNumber());
		logger.info("IRRBB_PLACEMENTS updateDetailRecord — summary synced for account={}", e.getAccountNumber());

		auditService.compareEntitiesmanual(oldDetailEntity, e, e.getSno().toString(), "IRRBB_PLACEMENTS Detail Screen",
				"BRRS_IRRBB_PLA_DETAIL_TABLE");
	}

	// ── DETAIL VIEW HELPER ─────────────────────────────────────────────────────

	public ModelAndView getIRRBB_PLACEMENTS_DetailView(String fromdate, String todate, String asondate) {
		ModelAndView mv = new ModelAndView();
		try {
			Date d1 = dateformat.parse(todate);
			List<IRRBB_PLACEMENTS_Summary_Entity> dataList = getDetailByDateList(d1);
			mv.addObject("displaymode", "detail");
			mv.addObject("reportdetail", dataList);
			mv.addObject("asondate", asondate);
			mv.addObject("fromdate", fromdate);
			mv.addObject("todate", todate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mv.setViewName("BRRS/IRRBB_PLACEMENTS");
		return mv;
	}

	// ── RESUB — INSERT new versioned records ──────────────────────────────────

	private BigDecimal findMaxResubVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		BigDecimal max = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		return (max != null) ? max : BigDecimal.ZERO;
	}

	public void updateResubRecord(IRRBB_PLACEMENTS_Summary_Entity e) {
		Date reportDate = e.getReportDate();
		BigDecimal newVersion = findMaxResubVersion(reportDate).add(BigDecimal.ONE);
		Date now = new Date();
		e.setReportVersion(newVersion);
		e.setReportResubdate(now);

		insertResubSummary(e);
		logger.info("IRRBB_PLACEMENTS updateResubRecord — inserted RESUB_SUMMARYTABLE version={}", newVersion);

		insertResubDetail(e);
		logger.info("IRRBB_PLACEMENTS updateResubRecord — inserted RESUB_DETAILTABLE version={}", newVersion);

		insertArchivalSummary(e);
		logger.info("IRRBB_PLACEMENTS updateResubRecord — inserted ARCHIVAL_SUMMARYTABLE version={}", newVersion);

		insertArchivalDetail(e);
		logger.info("IRRBB_PLACEMENTS updateResubRecord — inserted ARCHIVAL_DETAILTABLE version={}", newVersion);
	}

	// ── RESUB / ARCHIVAL INSERT HELPERS ──────────────────────────────────────

	private void insertResubSummary(IRRBB_PLACEMENTS_Summary_Entity e) {
		String sql = "INSERT INTO BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE ("
				+ "  SNO, ACCOUNT_NUMBER, GL_CODE, GL_DESCRIPTION,"
				+ "  ACCOUNT_CURRENCY, ACCOUNT_OPENING_DATE, MATURITY_DATE," + "  OUT_BAL_ACC_CCY, OUT_BAL_INR,"
				+ "  FLOATING_FIXED, EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY,"
				+ "  LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK,"
				+ "  FINAL_ROI_PERCENT, REMARKS, BACID,"
				+ "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
				+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE" + ") VALUES ("
				+ "  BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE_SNO_SEQ.NEXTVAL, ?, ?, ?," + "  ?, ?, ?," + "  ?, ?,"
				+ "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?, ?, ?," + "  ?, ?, ?, ?" + ")";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate());
	}

	private void insertResubDetail(IRRBB_PLACEMENTS_Summary_Entity e) {
		String sql = "INSERT INTO BRRS_IRRBB_PLA_RESUBMISSION_DETAIL_TABLE ("
				+ "  SNO, ACCOUNT_NUMBER, GL_CODE, GL_DESCRIPTION,"
				+ "  ACCOUNT_CURRENCY, ACCOUNT_OPENING_DATE, MATURITY_DATE," + "  OUT_BAL_ACC_CCY, OUT_BAL_INR,"
				+ "  FLOATING_FIXED, EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY,"
				+ "  LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK,"
				+ "  FINAL_ROI_PERCENT, REMARKS, BACID,"
				+ "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
				+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE" + ") VALUES ("
				+ "  BRRS_IRRBB_PLA_RESUBMISSION_DETAIL_TABLE_SNO_SEQ.NEXTVAL, ?, ?, ?," + "  ?, ?, ?," + "  ?, ?,"
				+ "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?, ?, ?," + "  ?, ?, ?, ?" + ")";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate());
	}

	private void insertArchivalSummary(IRRBB_PLACEMENTS_Summary_Entity e) {
		String sql = "INSERT INTO BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE ("
				+ "  SNO, ACCOUNT_NUMBER, GL_CODE, GL_DESCRIPTION,"
				+ "  ACCOUNT_CURRENCY, ACCOUNT_OPENING_DATE, MATURITY_DATE," + "  OUT_BAL_ACC_CCY, OUT_BAL_INR,"
				+ "  FLOATING_FIXED, EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY,"
				+ "  LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK,"
				+ "  FINAL_ROI_PERCENT, REMARKS, BACID,"
				+ "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
				+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE" + ") VALUES ("
				+ "  BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE_SNO_SEQ.NEXTVAL, ?, ?, ?," + "  ?, ?, ?," + "  ?, ?,"
				+ "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?, ?, ?," + "  ?, ?, ?, ?" + ")";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate());
	}

	private void insertArchivalDetail(IRRBB_PLACEMENTS_Summary_Entity e) {
		String sql = "INSERT INTO BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE ("
				+ "  SNO, ACCOUNT_NUMBER, GL_CODE, GL_DESCRIPTION,"
				+ "  ACCOUNT_CURRENCY, ACCOUNT_OPENING_DATE, MATURITY_DATE," + "  OUT_BAL_ACC_CCY, OUT_BAL_INR,"
				+ "  FLOATING_FIXED, EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY,"
				+ "  LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK,"
				+ "  FINAL_ROI_PERCENT, REMARKS, BACID,"
				+ "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
				+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE" + ") VALUES ("
				+ "  BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE_SNO_SEQ.NEXTVAL, ?, ?, ?," + "  ?, ?, ?," + "  ?, ?,"
				+ "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?," + "  ?, ?, ?, ?, ?," + "  ?, ?, ?, ?" + ")";
		jdbcTemplate.update(sql, e.getAccountNumber(), e.getGlCode(), e.getGlDescription(), e.getAccountCurrency(),
				e.getAccountOpeningDate(), e.getMaturityDate(), e.getOutBalAccCcy(), e.getOutBalInr(),
				e.getFloatingFixed(), e.getExistingBenchmark(), e.getExistingRepricingFrequency(),
				e.getLastRepricingDate(), e.getNextRepricingDate(), e.getSpreadOverBenchmark(), e.getFinalRoiPercent(),
				e.getRemarks(), e.getBacid(), e.getReportDate(), e.getReportVersion(), e.getReportFrequency(),
				e.getReportCode(), e.getReportDesc(), e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(),
				e.getReportResubdate());
	}

	// ── ARCHIVAL / RESUB LIST HELPERS ──────────────────────────────────────

	public List<Object[]> getIRRBB_PLACEMENTS_Archival() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<Object[]> getIRRBB_PLACEMENTS_Resub() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// ── ARCHIVAL READ-SIDE PARITY ────────────────────────────────────────────

	public List<IRRBB_PLACEMENTS_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate,
			BigDecimal version) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new IRRBB_PLACEMENTS_RowMapper());
	}

	public List<IRRBB_PLACEMENTS_Summary_Entity> getArchivalDetailByDateAndVersion(Date reportDate,
			BigDecimal version) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new IRRBB_PLACEMENTS_RowMapper());
	}

	public List<IRRBB_PLACEMENTS_Summary_Entity> getArchivalSummaryWithVersion() {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new IRRBB_PLACEMENTS_RowMapper());
	}

	public BigDecimal findMaxArchivalVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		BigDecimal max = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		return (max != null) ? max : BigDecimal.ZERO;
	}

	public List<IRRBB_PLACEMENTS_Summary_Entity> getArchivalDetailByDateList(Date reportDate, int offset, int limit) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { reportDate, offset, limit }, new IRRBB_PLACEMENTS_RowMapper());
	}

	public int getArchivalDetailCount(Date reportDate) {
		String sql = "SELECT COUNT(*) FROM BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, Integer.class);
	}

	public List<IRRBB_PLACEMENTS_Summary_Entity> getArchivalDetailByAccountNumber(Long accountNumber) {
		String sql = "SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE WHERE ACCOUNT_NUMBER = ?";
		return jdbcTemplate.query(sql, new Object[] { accountNumber }, new IRRBB_PLACEMENTS_RowMapper());
	}

	// ── EXCEL GENERATION ──────────────────────────────────────────────────────

	public byte[] generateSummaryExcel(String dateStr) throws Exception {
		byte[] bytes = IRRBB_PLACEMENTS_Excel("IRRBB_PLACEMENTS", dateStr);
		auditDownload("IRRBB_PLACEMENTS SUMMARY", "BRRS_IRRBB_PLA_SUMMARY_TABLE");
		return bytes;
	}

	public byte[] generateDetailExcel(String dateStr) throws Exception {
		byte[] bytes = IRRBB_PLACEMENTS_Excel("IRRBB_PLACEMENTS_Detail", dateStr);
		auditDownload("IRRBB_PLACEMENTS DETAIL", "BRRS_IRRBB_PLA_DETAIL_TABLE");
		return bytes;
	}

	public byte[] generateArchiveSummaryExcel(String dateStr) throws Exception {
		byte[] bytes = IRRBB_PLACEMENTS_Excel("IRRBB_PLACEMENTS_Archive", dateStr);
		auditDownload("IRRBB_PLACEMENTS ARCHIVAL SUMMARY", "BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE");
		return bytes;
	}

	public byte[] generateArchiveDetailExcel(String dateStr) throws Exception {
		byte[] bytes = IRRBB_PLACEMENTS_Excel("IRRBB_PLACEMENTS_Archive_Detail", dateStr);
		auditDownload("IRRBB_PLACEMENTS ARCHIVAL DETAIL", "BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE");
		return bytes;
	}

	public byte[] generateResubSummaryExcel(String dateStr) throws Exception {
		byte[] bytes = IRRBB_PLACEMENTS_Excel("IRRBB_PLACEMENTS_Resub", dateStr);
		auditDownload("IRRBB_PLACEMENTS RESUB SUMMARY", "BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE");
		return bytes;
	}

	public byte[] generateResubDetailExcel(String dateStr) throws Exception {
		byte[] bytes = IRRBB_PLACEMENTS_Excel("IRRBB_PLACEMENTS_Resub_Detail", dateStr);
		auditDownload("IRRBB_PLACEMENTS RESUB DETAIL", "BRRS_IRRBB_PLA_RESUBMISSION_DETAIL_TABLE");
		return bytes;
	}

	private void auditDownload(String screenName, String tableName) {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			String userid = (String) request.getSession().getAttribute("USERID");
			auditService.createBusinessAudit(userid, "DOWNLOAD", screenName, null, tableName);
		}
	}

	public byte[] IRRBB_PLACEMENTS_Excel(String filename, String todate) throws Exception {
		Date d = parseDateSafe(todate);
		List<IRRBB_PLACEMENTS_Summary_Entity> data;
		String fn = filename != null ? filename : "";
		if (fn.contains("Archive_Detail")) {
			data = jdbcTemplate.query(
					"SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_DETAIL_TABLE a WHERE TRUNC(a.REPORT_DATE) = TRUNC(?)",
					new Object[] { d }, new IRRBB_PLACEMENTS_RowMapper());
		} else if (fn.contains("Archive")) {
			data = jdbcTemplate.query(
					"SELECT * FROM BRRS_IRRBB_PLA_ARCHIVAL_SUMMARY_TABLE a WHERE TRUNC(a.REPORT_DATE) = TRUNC(?)",
					new Object[] { d }, new IRRBB_PLACEMENTS_RowMapper());
		} else if (fn.contains("Resub_Detail")) {
			data = jdbcTemplate.query(
					"SELECT * FROM BRRS_IRRBB_PLA_RESUBMISSION_DETAIL_TABLE a WHERE TRUNC(a.REPORT_DATE) = TRUNC(?)",
					new Object[] { d }, new IRRBB_PLACEMENTS_RowMapper());
		} else if (fn.contains("Resub")) {
			data = jdbcTemplate.query(
					"SELECT * FROM BRRS_IRRBB_PLA_RESUBMISSION_SUMMARY_TABLE a WHERE TRUNC(a.REPORT_DATE) = TRUNC(?)",
					new Object[] { d }, new IRRBB_PLACEMENTS_RowMapper());
		} else if (fn.contains("Detail")) {
			data = getDetailByDateList(d);
		} else {
			data = getdatabydateList(d);
		}
		return buildExcel(data);
	}

	private static final int PLACEMENTS_NUM_COLS = 17;
	private static final int PLACEMENTS_FIRST_DATA = 1;

	private byte[] buildExcel(List<IRRBB_PLACEMENTS_Summary_Entity> data) throws Exception {
		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, "IRRBB_PLACEMENTS.xlsx");

		if (!Files.exists(templatePath))
			throw new FileNotFoundException("Template not found: " + templatePath.toAbsolutePath());

		try (InputStream in = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(in);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			CellStyle[] colStyles = new CellStyle[PLACEMENTS_NUM_COLS];
			Row templateDataRow = sheet.getRow(PLACEMENTS_FIRST_DATA);
			for (int c = 0; c < PLACEMENTS_NUM_COLS; c++) {
				Cell tc = (templateDataRow != null) ? templateDataRow.getCell(c) : null;
				CellStyle cs = workbook.createCellStyle();
				if (tc != null) {
					cs.cloneStyleFrom(tc.getCellStyle());
				} else {
					cs.setBorderTop(BorderStyle.THIN);
					cs.setBorderBottom(BorderStyle.THIN);
					cs.setBorderLeft(BorderStyle.THIN);
					cs.setBorderRight(BorderStyle.THIN);
				}
				colStyles[c] = cs;
			}

			for (int i = sheet.getLastRowNum(); i >= PLACEMENTS_FIRST_DATA; i--) {
				Row r = sheet.getRow(i);
				if (r != null)
					sheet.removeRow(r);
			}

			SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");

			int rowIdx = PLACEMENTS_FIRST_DATA;
			for (IRRBB_PLACEMENTS_Summary_Entity rec : data) {

				Row row = sheet.createRow(rowIdx++);
				setCellStr(row, 0, rec.getAccountNumber() != null ? rec.getAccountNumber().toString() : "",
						colStyles[0]);
				setCellStr(row, 1, rec.getGlCode() != null ? rec.getGlCode().toString() : "", colStyles[1]);
				setCellStr(row, 2, rec.getGlDescription(), colStyles[2]);
				setCellStr(row, 3, rec.getAccountCurrency(), colStyles[3]);
				setCellStr(row, 4, rec.getAccountOpeningDate() != null ? fmt.format(rec.getAccountOpeningDate()) : "",
						colStyles[4]);
				setCellStr(row, 5, rec.getMaturityDate() != null ? fmt.format(rec.getMaturityDate()) : "",
						colStyles[5]);
				setCellNum(row, 6, rec.getOutBalAccCcy(), colStyles[6]);
				setCellNum(row, 7, rec.getOutBalInr(), colStyles[7]);
				setCellStr(row, 8, rec.getFloatingFixed(), colStyles[8]);
				setCellStr(row, 9, rec.getExistingBenchmark(), colStyles[9]);
				setCellStr(row, 10, rec.getExistingRepricingFrequency(), colStyles[10]);
				setCellStr(row, 11, rec.getLastRepricingDate() != null ? fmt.format(rec.getLastRepricingDate()) : "",
						colStyles[11]);
				setCellStr(row, 12, rec.getNextRepricingDate() != null ? fmt.format(rec.getNextRepricingDate()) : "",
						colStyles[12]);
				setCellStr(row, 13, rec.getSpreadOverBenchmark(), colStyles[13]);
				setCellStr(row, 14, rec.getFinalRoiPercent(), colStyles[14]);
				setCellStr(row, 15, rec.getRemarks(), colStyles[15]);
				setCellStr(row, 16, rec.getBacid(), colStyles[16]);
			}

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private void setCellStr(Row row, int col, String val, CellStyle style) {
		Cell cell = row.createCell(col);
		cell.setCellValue(val != null ? val : "");
		if (style != null)
			cell.setCellStyle(style);
	}

	private void setCellNum(Row row, int col, BigDecimal val, CellStyle style) {
		Cell cell = row.createCell(col);
		cell.setCellValue(val != null ? val.doubleValue() : 0.0);
		if (style != null)
			cell.setCellStyle(style);
	}

	private Date parseDateSafe(String s) throws Exception {
		if (s == null)
			return new Date();
		String[] fmts = { "dd-MMM-yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy" };
		for (String f : fmts) {
			try {
				return new SimpleDateFormat(f).parse(s);
			} catch (Exception ignored) {
			}
		}
		throw new ParseException("Cannot parse date: " + s, 0);
	}

	// ── ROW MAPPER ────────────────────────────────────────────────────────────

	class IRRBB_PLACEMENTS_RowMapper implements RowMapper<IRRBB_PLACEMENTS_Summary_Entity> {

		@Override
		public IRRBB_PLACEMENTS_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			IRRBB_PLACEMENTS_Summary_Entity obj = new IRRBB_PLACEMENTS_Summary_Entity();

			obj.setSno(rs.getLong("SNO"));
			obj.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
			obj.setGlCode(rs.getLong("GL_CODE"));
			obj.setGlDescription(rs.getString("GL_DESCRIPTION"));
			obj.setAccountCurrency(rs.getString("ACCOUNT_CURRENCY"));
			obj.setAccountOpeningDate(rs.getDate("ACCOUNT_OPENING_DATE"));
			obj.setMaturityDate(rs.getDate("MATURITY_DATE"));
			obj.setOutBalAccCcy(rs.getBigDecimal("OUT_BAL_ACC_CCY"));
			obj.setOutBalInr(rs.getBigDecimal("OUT_BAL_INR"));
			obj.setFloatingFixed(rs.getString("FLOATING_FIXED"));
			obj.setExistingBenchmark(rs.getString("EXISTING_BENCHMARK"));
			obj.setExistingRepricingFrequency(rs.getString("EXISTING_REPRICING_FREQUENCY"));
			obj.setLastRepricingDate(rs.getDate("LAST_REPRICING_DATE"));
			obj.setNextRepricingDate(rs.getDate("NEXT_REPRICING_DATE"));
			obj.setSpreadOverBenchmark(rs.getString("SPREAD_OVER_BENCHMARK"));
			obj.setFinalRoiPercent(rs.getString("FINAL_ROI_PERCENT"));
			obj.setRemarks(rs.getString("REMARKS"));
			obj.setBacid(rs.getString("BACID"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));

			return obj;
		}
	}

	// ── ENTITY (PLAIN POJO — no JPA annotations) ──────────────────────────────

	public static class IRRBB_PLACEMENTS_Summary_Entity {

		private Long sno;
		private String accountNumber;
		private Long glCode;
		private String glDescription;
		private String accountCurrency;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date accountOpeningDate;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date maturityDate;
		private BigDecimal outBalAccCcy;
		private BigDecimal outBalInr;
		private String floatingFixed;
		private String existingBenchmark;
		private String existingRepricingFrequency;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date lastRepricingDate;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date nextRepricingDate;
		private String spreadOverBenchmark;
		private String finalRoiPercent;
		private String remarks;
		private String bacid;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date reportResubdate;

		// ── Getters and Setters ──

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public Long getGlCode() {
			return glCode;
		}

		public void setGlCode(Long glCode) {
			this.glCode = glCode;
		}

		public String getGlDescription() {
			return glDescription;
		}

		public void setGlDescription(String glDescription) {
			this.glDescription = glDescription;
		}

		public String getAccountCurrency() {
			return accountCurrency;
		}

		public void setAccountCurrency(String accountCurrency) {
			this.accountCurrency = accountCurrency;
		}

		public Date getAccountOpeningDate() {
			return accountOpeningDate;
		}

		public void setAccountOpeningDate(Date accountOpeningDate) {
			this.accountOpeningDate = accountOpeningDate;
		}

		public Date getMaturityDate() {
			return maturityDate;
		}

		public void setMaturityDate(Date maturityDate) {
			this.maturityDate = maturityDate;
		}

		public BigDecimal getOutBalAccCcy() {
			return outBalAccCcy;
		}

		public void setOutBalAccCcy(BigDecimal outBalAccCcy) {
			this.outBalAccCcy = outBalAccCcy;
		}

		public BigDecimal getOutBalInr() {
			return outBalInr;
		}

		public void setOutBalInr(BigDecimal outBalInr) {
			this.outBalInr = outBalInr;
		}

		public String getFloatingFixed() {
			return floatingFixed;
		}

		public void setFloatingFixed(String floatingFixed) {
			this.floatingFixed = floatingFixed;
		}

		public String getExistingBenchmark() {
			return existingBenchmark;
		}

		public void setExistingBenchmark(String existingBenchmark) {
			this.existingBenchmark = existingBenchmark;
		}

		public String getExistingRepricingFrequency() {
			return existingRepricingFrequency;
		}

		public void setExistingRepricingFrequency(String existingRepricingFrequency) {
			this.existingRepricingFrequency = existingRepricingFrequency;
		}

		public Date getLastRepricingDate() {
			return lastRepricingDate;
		}

		public void setLastRepricingDate(Date lastRepricingDate) {
			this.lastRepricingDate = lastRepricingDate;
		}

		public Date getNextRepricingDate() {
			return nextRepricingDate;
		}

		public void setNextRepricingDate(Date nextRepricingDate) {
			this.nextRepricingDate = nextRepricingDate;
		}

		public String getSpreadOverBenchmark() {
			return spreadOverBenchmark;
		}

		public void setSpreadOverBenchmark(String spreadOverBenchmark) {
			this.spreadOverBenchmark = spreadOverBenchmark;
		}

		public String getFinalRoiPercent() {
			return finalRoiPercent;
		}

		public void setFinalRoiPercent(String finalRoiPercent) {
			this.finalRoiPercent = finalRoiPercent;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getBacid() {
			return bacid;
		}

		public void setBacid(String bacid) {
			this.bacid = bacid;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}

		public String getReportFrequency() {
			return reportFrequency;
		}

		public void setReportFrequency(String reportFrequency) {
			this.reportFrequency = reportFrequency;
		}

		public String getReportCode() {
			return reportCode;
		}

		public void setReportCode(String reportCode) {
			this.reportCode = reportCode;
		}

		public String getReportDesc() {
			return reportDesc;
		}

		public void setReportDesc(String reportDesc) {
			this.reportDesc = reportDesc;
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

		public Date getReportResubdate() {
			return reportResubdate;
		}

		public void setReportResubdate(Date reportResubdate) {
			this.reportResubdate = reportResubdate;
		}
	}

}