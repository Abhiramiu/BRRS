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

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
public class BRRS_M_SEC_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SEC_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private SessionFactory sessionFactory;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

//====================
//JDBC REPOSITORIES
//====================

//===========================
//BRRS_M_SEC_Summary_Repo1
//===========================

	public List<BRRS_M_SEC_Summary_Entity1> getdatabydateList1(Date rpt_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new BRRS_M_SEC_Summary_RowMapper1());
	}

	public List<BRRS_M_SEC_Summary_Entity1> getdatabydateListWithVersion1(String todate) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE report_date = ? AND report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new BRRS_M_SEC_Summary_RowMapper1());
	}

	public Optional<BRRS_M_SEC_Summary_Entity1> findTopByreport_dateOrderByreport_versionDesc1(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE report_date = ? ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Summary_Entity1> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Summary_RowMapper1());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Summary_Entity1> findByreport_dateAndreport_version1(Date report_date,
			String report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE report_date = ? AND report_version = ?";
		List<BRRS_M_SEC_Summary_Entity1> results = jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Summary_RowMapper1());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public List<BRRS_M_SEC_Summary_Entity1> getdatabydateListWithVersionOnly1() {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Summary_RowMapper1());
	}

//===========================
//BRRS_M_SEC_Summary_Repo2
//===========================

	public List<BRRS_M_SEC_Summary_Entity2> getdatabydateList2(Date rpt_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new BRRS_M_SEC_Summary_RowMapper2());
	}

	public List<BRRS_M_SEC_Summary_Entity2> getdatabydateListWithVersion2(String todate) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE report_date = ? AND report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new BRRS_M_SEC_Summary_RowMapper2());
	}

	public Optional<BRRS_M_SEC_Summary_Entity2> findTopByreport_dateOrderByreport_versionDesc2(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE report_date = ? ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Summary_Entity2> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Summary_RowMapper2());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Summary_Entity2> findByreport_dateAndreport_version2(Date report_date,
			String report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE report_date = ? AND report_version = ?";
		List<BRRS_M_SEC_Summary_Entity2> results = jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Summary_RowMapper2());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public List<BRRS_M_SEC_Summary_Entity2> getdatabydateListWithVersionOnly2() {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Summary_RowMapper2());
	}

//===========================
//BRRS_M_SEC_Summary_Repo3
//===========================

	public List<BRRS_M_SEC_Summary_Entity3> getdatabydateList3(Date rpt_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new BRRS_M_SEC_Summary_RowMapper3());
	}

	public List<BRRS_M_SEC_Summary_Entity3> getdatabydateListWithVersion3(String todate) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE report_date = ? AND report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new BRRS_M_SEC_Summary_RowMapper3());
	}

	public Optional<BRRS_M_SEC_Summary_Entity3> findTopByreport_dateOrderByreport_versionDesc3(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE report_date = ? ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Summary_Entity3> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Summary_RowMapper3());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Summary_Entity3> findByreport_dateAndreport_version3(Date report_date,
			String report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE report_date = ? AND report_version = ?";
		List<BRRS_M_SEC_Summary_Entity3> results = jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Summary_RowMapper3());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public List<BRRS_M_SEC_Summary_Entity3> getdatabydateListWithVersionOnly3() {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Summary_RowMapper3());
	}

//===========================
//BRRS_M_SEC_Summary_Repo4
//===========================

	public List<BRRS_M_SEC_Summary_Entity4> getdatabydateList4(Date rpt_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new BRRS_M_SEC_Summary_RowMapper4());
	}

	public List<BRRS_M_SEC_Summary_Entity4> getdatabydateListWithVersion4(String todate) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE report_date = ? AND report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new BRRS_M_SEC_Summary_RowMapper4());
	}

	public Optional<BRRS_M_SEC_Summary_Entity4> findTopByreport_dateOrderByreport_versionDesc4(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE report_date = ? ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Summary_Entity4> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Summary_RowMapper4());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Summary_Entity4> findByreport_dateAndreport_version4(Date report_date,
			String report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE report_date = ? AND report_version = ?";
		List<BRRS_M_SEC_Summary_Entity4> results = jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Summary_RowMapper4());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public List<BRRS_M_SEC_Summary_Entity4> getdatabydateListWithVersionOnly4() {
		String sql = "SELECT * FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE report_version IS NOT NULL ORDER BY report_version DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Summary_RowMapper4());
	}

//===========================
//BRRS_M_SEC_Detail1_Repo
//===========================

	public List<M_SEC_Detail1_Entity> getdatabydateList5(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_DETAILTABLE1 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_SEC_Detail1_RowMapper());
	}

//===========================
//BRRS_M_SEC_Detail2_Repo
//===========================

	public List<M_SEC_Detail2_Entity> getdatabydateList6(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_DETAILTABLE2 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_SEC_Detail2_RowMapper());
	}

//===========================
//BRRS_M_SEC_Detail3_Repo
//===========================

	public List<M_SEC_Detail3_Entity> getdatabydateList7(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_DETAILTABLE3 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_SEC_Detail3_RowMapper());
	}

//===========================
//BRRS_M_SEC_Detail4_Repo
//===========================

	public List<M_SEC_Detail4_Entity> getdatabydateList8(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_DETAILTABLE4 WHERE report_date = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_SEC_Detail4_RowMapper());
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Repo1
//===========================

	public List<Object> getM_SECarchival1() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity1> getdatabydateListarchival1(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Archival_Summary_RowMapper1());
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity1> getdatabydateListWithVersion1() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 WHERE report_version IS NOT NULL ORDER BY report_version ASC";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Archival_Summary_RowMapper1());
	}

	public BigDecimal findMaxVersion1(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Repo2
//===========================

	public List<Object> getM_SECarchival2() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity2> getdatabydateListarchival2(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Archival_Summary_RowMapper2());
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity2> getdatabydateListWithVersion2() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE report_version IS NOT NULL ORDER BY report_version ASC";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Archival_Summary_RowMapper2());
	}

	public BigDecimal findMaxVersion2(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Repo3
//===========================

	public List<Object> getM_SECarchival3() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity3> getdatabydateListarchival3(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Archival_Summary_RowMapper3());
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity3> getdatabydateListWithVersion3() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 WHERE report_version IS NOT NULL ORDER BY report_version ASC";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Archival_Summary_RowMapper3());
	}

	public BigDecimal findMaxVersion3(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Repo4
//===========================

	public List<Object> getM_SECarchival4() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity4> getdatabydateListarchival4(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new BRRS_M_SEC_Archival_Summary_RowMapper4());
	}

	public List<BRRS_M_SEC_Archival_Summary_Entity4> getdatabydateListWithVersion4() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 WHERE report_version IS NOT NULL ORDER BY report_version ASC";
		return jdbcTemplate.query(sql, new BRRS_M_SEC_Archival_Summary_RowMapper4());
	}

	public BigDecimal findMaxVersion4(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//===========================
//BRRS_M_SEC_Archival_Detail1_Repo
//===========================

	public List<Object> getM_SECarchival5() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_Archival_Detail1_Entity> getdatabydateListarchival5(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_Archival_Detail1_RowMapper());
	}

	public BigDecimal findMaxVersion5(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_Archival_Detail1_Entity> getdatabydateListWithVersion5() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_Archival_Detail1_RowMapper());
	}

//===========================
//BRRS_M_SEC_Archival_Detail2_Repo
//===========================

	public List<Object> getM_SECarchival6() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_Archival_Detail2_Entity> getdatabydateListarchival6(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_Archival_Detail2_RowMapper());
	}

	public BigDecimal findMaxVersion6(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_Archival_Detail2_Entity> getdatabydateListWithVersion6() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_Archival_Detail2_RowMapper());
	}

//===========================
//BRRS_M_SEC_Archival_Detail3_Repo
//===========================

	public List<Object> getM_SECarchival7() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_Archival_Detail3_Entity> getdatabydateListarchival7(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_Archival_Detail3_RowMapper());
	}

	public BigDecimal findMaxVersion7(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_Archival_Detail3_Entity> getdatabydateListWithVersion7() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_Archival_Detail3_RowMapper());
	}

//===========================
//BRRS_M_SEC_Archival_Detail4_Repo
//===========================

	public List<Object> getM_SECarchival8() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_Archival_Detail4_Entity> getdatabydateListarchival8(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_Archival_Detail4_RowMapper());
	}

	public BigDecimal findMaxVersion8(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_Archival_Detail4_Entity> getdatabydateListWithVersion8() {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_Archival_Detail4_RowMapper());
	}

//===========================
//BRRS_M_SEC_RESUB_Summary_Repo1
//===========================

	public List<M_SEC_RESUB_Summary_Entity1> getdatabydateListarchival17(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE1 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Summary_RowMapper1());
	}

	public BigDecimal findMaxVersion17(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE1 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<Object> getM_SECarchival17() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_SUMMARYTABLE1 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Summary_Entity1> getdatabydateListWithVersion17() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE1 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Summary_RowMapper1());
	}

//===========================
//BRRS_M_SEC_RESUB_Summary_Repo2
//===========================

	public List<M_SEC_RESUB_Summary_Entity2> getdatabydateListarchival18(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE2 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Summary_RowMapper2());
	}

	public BigDecimal findMaxVersion18(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE2 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<Object> getM_SECarchival18() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_SUMMARYTABLE2 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Summary_Entity2> getdatabydateListWithVersion18() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE2 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Summary_RowMapper2());
	}

//===========================
//BRRS_M_SEC_RESUB_Summary_Repo3
//===========================

	public List<M_SEC_RESUB_Summary_Entity3> getdatabydateListarchival19(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE3 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Summary_RowMapper3());
	}

	public BigDecimal findMaxVersion19(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE3 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<Object> getM_SECarchival19() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_SUMMARYTABLE3 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Summary_Entity3> getdatabydateListWithVersion19() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE3 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Summary_RowMapper3());
	}

//===========================
//BRRS_M_SEC_RESUB_Summary_Repo4
//===========================

	public List<M_SEC_RESUB_Summary_Entity4> getdatabydateListarchival20(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE4 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Summary_RowMapper4());
	}

	public BigDecimal findMaxVersion20(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE4 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<Object> getM_SECarchival20() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_SUMMARYTABLE4 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Summary_Entity4> getdatabydateListWithVersion20() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_SUMMARYTABLE4 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Summary_RowMapper4());
	}

//===========================
//BRRS_M_SEC_RESUB_Detail_Repo1
//===========================

	public List<Object> getM_SECarchival21() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_DETAILTABLE1 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Detail_Entity1> getdatabydateListarchival21(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE1 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Detail_RowMapper1());
	}

	public BigDecimal findMaxVersion21(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_DETAILTABLE1 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_RESUB_Detail_Entity1> getdatabydateListWithVersion21() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE1 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Detail_RowMapper1());
	}

//===========================
//BRRS_M_SEC_RESUB_Detail_Repo2
//===========================

	public List<Object> getM_SECarchival22() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_DETAILTABLE2 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Detail_Entity2> getdatabydateListarchival22(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE2 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Detail_RowMapper2());
	}

	public BigDecimal findMaxVersion22(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_DETAILTABLE2 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_RESUB_Detail_Entity2> getdatabydateListWithVersion22() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE2 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Detail_RowMapper2());
	}

//===========================
//BRRS_M_SEC_RESUB_Detail_Repo3
//===========================

	public List<Object> getM_SECarchival23() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_DETAILTABLE3 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Detail_Entity3> getdatabydateListarchival23(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE3 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Detail_RowMapper3());
	}

	public BigDecimal findMaxVersion23(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_DETAILTABLE3 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_RESUB_Detail_Entity3> getdatabydateListWithVersion23() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE3 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Detail_RowMapper3());
	}

//===========================
//BRRS_M_SEC_RESUB_Detail_Repo4
//===========================

	public List<Object> getM_SECarchival24() {
		String sql = "SELECT report_date, report_version FROM BRRS_M_SEC_RESUB_DETAILTABLE4 ORDER BY report_version";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SEC_RESUB_Detail_Entity4> getdatabydateListarchival24(Date report_date, BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE4 WHERE report_date = ? AND report_version = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SEC_RESUB_Detail_RowMapper4());
	}

	public BigDecimal findMaxVersion24(Date date) {
		String sql = "SELECT MAX(report_version) FROM BRRS_M_SEC_RESUB_DETAILTABLE4 WHERE report_date = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	public List<M_SEC_RESUB_Detail_Entity4> getdatabydateListWithVersion24() {
		String sql = "SELECT * FROM BRRS_M_SEC_RESUB_DETAILTABLE4 WHERE report_version IS NOT NULL ORDER BY report_version ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SEC_RESUB_Detail_RowMapper4());
	}

	// ==============================
	// Get Latest Available Date
	// ==============================

	public Date getLatestReportDate() {
		String sql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SEC_SUMMARYTABLE1";
		try {
			return jdbcTemplate.queryForObject(sql, Date.class);
		} catch (Exception e) {
			logger.warn("No data found in BRRS_M_SEC_SUMMARYTABLE1");
			return null;
		}
	}

	// ==============================
	// Get Latest Archival Summary Version by Date
	// ==============================

	public Optional<BRRS_M_SEC_Archival_Summary_Entity1> getLatestArchivalSummaryVersionByDate(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Archival_Summary_Entity1> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Archival_Summary_RowMapper1());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Archival_Summary_Entity2> getLatestArchivalSummaryVersionByDate2(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Archival_Summary_Entity2> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Archival_Summary_RowMapper2());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Archival_Summary_Entity3> getLatestArchivalSummaryVersionByDate3(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Archival_Summary_Entity3> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Archival_Summary_RowMapper3());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<BRRS_M_SEC_Archival_Summary_Entity4> getLatestArchivalSummaryVersionByDate4(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<BRRS_M_SEC_Archival_Summary_Entity4> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new BRRS_M_SEC_Archival_Summary_RowMapper4());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

//==================== 
//ENTITY ROW MAPPERS 
//====================

//===========================
//BRRS_M_SEC_Summary_RowMapper1
//===========================

	public class BRRS_M_SEC_Summary_RowMapper1 implements RowMapper<BRRS_M_SEC_Summary_Entity1> {

		@Override
		public BRRS_M_SEC_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Summary_Entity1 obj = new BRRS_M_SEC_Summary_Entity1();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA(rs.getBigDecimal("R11_TCA"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA(rs.getBigDecimal("R12_TCA"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA(rs.getBigDecimal("R13_TCA"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA(rs.getBigDecimal("R14_TCA"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA(rs.getBigDecimal("R15_TCA"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA(rs.getBigDecimal("R16_TCA"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TCA(rs.getBigDecimal("R17_TCA"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TCA(rs.getBigDecimal("R18_TCA"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TCA(rs.getBigDecimal("R19_TCA"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Summary_Entity1
//===========================

	public class BRRS_M_SEC_Summary_Entity1 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA;
		private String R17_PRODUCT;
		private BigDecimal R17_TCA;
		private String R18_PRODUCT;
		private BigDecimal R18_TCA;
		private String R19_PRODUCT;
		private BigDecimal R19_TCA;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default Constructor
		public BRRS_M_SEC_Summary_Entity1() {
			super();
		}

		// Getters and Setters
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA() {
			return R11_TCA;
		}

		public void setR11_TCA(BigDecimal r11_TCA) {
			R11_TCA = r11_TCA;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA() {
			return R12_TCA;
		}

		public void setR12_TCA(BigDecimal r12_TCA) {
			R12_TCA = r12_TCA;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA() {
			return R13_TCA;
		}

		public void setR13_TCA(BigDecimal r13_TCA) {
			R13_TCA = r13_TCA;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA() {
			return R14_TCA;
		}

		public void setR14_TCA(BigDecimal r14_TCA) {
			R14_TCA = r14_TCA;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA() {
			return R15_TCA;
		}

		public void setR15_TCA(BigDecimal r15_TCA) {
			R15_TCA = r15_TCA;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA() {
			return R16_TCA;
		}

		public void setR16_TCA(BigDecimal r16_TCA) {
			R16_TCA = r16_TCA;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TCA() {
			return R17_TCA;
		}

		public void setR17_TCA(BigDecimal r17_TCA) {
			R17_TCA = r17_TCA;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_TCA() {
			return R18_TCA;
		}

		public void setR18_TCA(BigDecimal r18_TCA) {
			R18_TCA = r18_TCA;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_TCA() {
			return R19_TCA;
		}

		public void setR19_TCA(BigDecimal r19_TCA) {
			R19_TCA = r19_TCA;
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
	}

//===========================
//BRRS_M_SEC_Summary_RowMapper2
//===========================

	public class BRRS_M_SEC_Summary_RowMapper2 implements RowMapper<BRRS_M_SEC_Summary_Entity2> {

		@Override
		public BRRS_M_SEC_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Summary_Entity2 obj = new BRRS_M_SEC_Summary_Entity2();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA2(rs.getBigDecimal("R11_TCA2"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA2(rs.getBigDecimal("R12_TCA2"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA2(rs.getBigDecimal("R13_TCA2"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA2(rs.getBigDecimal("R14_TCA2"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA2(rs.getBigDecimal("R15_TCA2"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA2(rs.getBigDecimal("R16_TCA2"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Summary_Entity2
//===========================

	public class BRRS_M_SEC_Summary_Entity2 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA2;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA2;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA2;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA2;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA2;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA2;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default Constructor
		public BRRS_M_SEC_Summary_Entity2() {
			super();
		}

		// Getters and Setters
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA2() {
			return R11_TCA2;
		}

		public void setR11_TCA2(BigDecimal r11_TCA2) {
			R11_TCA2 = r11_TCA2;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA2() {
			return R12_TCA2;
		}

		public void setR12_TCA2(BigDecimal r12_TCA2) {
			R12_TCA2 = r12_TCA2;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA2() {
			return R13_TCA2;
		}

		public void setR13_TCA2(BigDecimal r13_TCA2) {
			R13_TCA2 = r13_TCA2;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA2() {
			return R14_TCA2;
		}

		public void setR14_TCA2(BigDecimal r14_TCA2) {
			R14_TCA2 = r14_TCA2;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA2() {
			return R15_TCA2;
		}

		public void setR15_TCA2(BigDecimal r15_TCA2) {
			R15_TCA2 = r15_TCA2;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA2() {
			return R16_TCA2;
		}

		public void setR16_TCA2(BigDecimal r16_TCA2) {
			R16_TCA2 = r16_TCA2;
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
	}

//===========================
//BRRS_M_SEC_Summary_RowMapper3
//===========================

	public class BRRS_M_SEC_Summary_RowMapper3 implements RowMapper<BRRS_M_SEC_Summary_Entity3> {

		@Override
		public BRRS_M_SEC_Summary_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Summary_Entity3 obj = new BRRS_M_SEC_Summary_Entity3();

			// =========================
			// R26
			// =========================
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_0_1Y_FT(rs.getBigDecimal("R26_0_1Y_FT"));
			obj.setR26_0_1Y_HTM(rs.getBigDecimal("R26_0_1Y_HTM"));
			obj.setR26_0_1Y_TOTAL(rs.getBigDecimal("R26_0_1Y_TOTAL"));
			obj.setR26_1_5Y_FT(rs.getBigDecimal("R26_1_5Y_FT"));
			obj.setR26_1_5Y_HTM(rs.getBigDecimal("R26_1_5Y_HTM"));
			obj.setR26_1_5Y_TOTAL(rs.getBigDecimal("R26_1_5Y_TOTAL"));
			obj.setR26_O5Y_FT(rs.getBigDecimal("R26_O5Y_FT"));
			obj.setR26_O5Y_HTM(rs.getBigDecimal("R26_O5Y_HTM"));
			obj.setR26_O5Y_TOTAL(rs.getBigDecimal("R26_O5Y_TOTAL"));
			obj.setR26_T_FT(rs.getBigDecimal("R26_T_FT"));
			obj.setR26_T_HTM(rs.getBigDecimal("R26_T_HTM"));
			obj.setR26_T_TOTAL(rs.getBigDecimal("R26_T_TOTAL"));

			// =========================
			// R27
			// =========================
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_0_1Y_FT(rs.getBigDecimal("R27_0_1Y_FT"));
			obj.setR27_0_1Y_HTM(rs.getBigDecimal("R27_0_1Y_HTM"));
			obj.setR27_0_1Y_TOTAL(rs.getBigDecimal("R27_0_1Y_TOTAL"));
			obj.setR27_1_5Y_FT(rs.getBigDecimal("R27_1_5Y_FT"));
			obj.setR27_1_5Y_HTM(rs.getBigDecimal("R27_1_5Y_HTM"));
			obj.setR27_1_5Y_TOTAL(rs.getBigDecimal("R27_1_5Y_TOTAL"));
			obj.setR27_O5Y_FT(rs.getBigDecimal("R27_O5Y_FT"));
			obj.setR27_O5Y_HTM(rs.getBigDecimal("R27_O5Y_HTM"));
			obj.setR27_O5Y_TOTAL(rs.getBigDecimal("R27_O5Y_TOTAL"));
			obj.setR27_T_FT(rs.getBigDecimal("R27_T_FT"));
			obj.setR27_T_HTM(rs.getBigDecimal("R27_T_HTM"));
			obj.setR27_T_TOTAL(rs.getBigDecimal("R27_T_TOTAL"));

			// =========================
			// R28
			// =========================
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_0_1Y_FT(rs.getBigDecimal("R28_0_1Y_FT"));
			obj.setR28_0_1Y_HTM(rs.getBigDecimal("R28_0_1Y_HTM"));
			obj.setR28_0_1Y_TOTAL(rs.getBigDecimal("R28_0_1Y_TOTAL"));
			obj.setR28_1_5Y_FT(rs.getBigDecimal("R28_1_5Y_FT"));
			obj.setR28_1_5Y_HTM(rs.getBigDecimal("R28_1_5Y_HTM"));
			obj.setR28_1_5Y_TOTAL(rs.getBigDecimal("R28_1_5Y_TOTAL"));
			obj.setR28_O5Y_FT(rs.getBigDecimal("R28_O5Y_FT"));
			obj.setR28_O5Y_HTM(rs.getBigDecimal("R28_O5Y_HTM"));
			obj.setR28_O5Y_TOTAL(rs.getBigDecimal("R28_O5Y_TOTAL"));
			obj.setR28_T_FT(rs.getBigDecimal("R28_T_FT"));
			obj.setR28_T_HTM(rs.getBigDecimal("R28_T_HTM"));
			obj.setR28_T_TOTAL(rs.getBigDecimal("R28_T_TOTAL"));

			// =========================
			// R29
			// =========================
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_0_1Y_FT(rs.getBigDecimal("R29_0_1Y_FT"));
			obj.setR29_0_1Y_HTM(rs.getBigDecimal("R29_0_1Y_HTM"));
			obj.setR29_0_1Y_TOTAL(rs.getBigDecimal("R29_0_1Y_TOTAL"));
			obj.setR29_1_5Y_FT(rs.getBigDecimal("R29_1_5Y_FT"));
			obj.setR29_1_5Y_HTM(rs.getBigDecimal("R29_1_5Y_HTM"));
			obj.setR29_1_5Y_TOTAL(rs.getBigDecimal("R29_1_5Y_TOTAL"));
			obj.setR29_O5Y_FT(rs.getBigDecimal("R29_O5Y_FT"));
			obj.setR29_O5Y_HTM(rs.getBigDecimal("R29_O5Y_HTM"));
			obj.setR29_O5Y_TOTAL(rs.getBigDecimal("R29_O5Y_TOTAL"));
			obj.setR29_T_FT(rs.getBigDecimal("R29_T_FT"));
			obj.setR29_T_HTM(rs.getBigDecimal("R29_T_HTM"));
			obj.setR29_T_TOTAL(rs.getBigDecimal("R29_T_TOTAL"));

			// =========================
			// R30
			// =========================
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_0_1Y_FT(rs.getBigDecimal("R30_0_1Y_FT"));
			obj.setR30_0_1Y_HTM(rs.getBigDecimal("R30_0_1Y_HTM"));
			obj.setR30_0_1Y_TOTAL(rs.getBigDecimal("R30_0_1Y_TOTAL"));
			obj.setR30_1_5Y_FT(rs.getBigDecimal("R30_1_5Y_FT"));
			obj.setR30_1_5Y_HTM(rs.getBigDecimal("R30_1_5Y_HTM"));
			obj.setR30_1_5Y_TOTAL(rs.getBigDecimal("R30_1_5Y_TOTAL"));
			obj.setR30_O5Y_FT(rs.getBigDecimal("R30_O5Y_FT"));
			obj.setR30_O5Y_HTM(rs.getBigDecimal("R30_O5Y_HTM"));
			obj.setR30_O5Y_TOTAL(rs.getBigDecimal("R30_O5Y_TOTAL"));
			obj.setR30_T_FT(rs.getBigDecimal("R30_T_FT"));
			obj.setR30_T_HTM(rs.getBigDecimal("R30_T_HTM"));
			obj.setR30_T_TOTAL(rs.getBigDecimal("R30_T_TOTAL"));

			// =========================
			// R31
			// =========================
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_0_1Y_FT(rs.getBigDecimal("R31_0_1Y_FT"));
			obj.setR31_0_1Y_HTM(rs.getBigDecimal("R31_0_1Y_HTM"));
			obj.setR31_0_1Y_TOTAL(rs.getBigDecimal("R31_0_1Y_TOTAL"));
			obj.setR31_1_5Y_FT(rs.getBigDecimal("R31_1_5Y_FT"));
			obj.setR31_1_5Y_HTM(rs.getBigDecimal("R31_1_5Y_HTM"));
			obj.setR31_1_5Y_TOTAL(rs.getBigDecimal("R31_1_5Y_TOTAL"));
			obj.setR31_O5Y_FT(rs.getBigDecimal("R31_O5Y_FT"));
			obj.setR31_O5Y_HTM(rs.getBigDecimal("R31_O5Y_HTM"));
			obj.setR31_O5Y_TOTAL(rs.getBigDecimal("R31_O5Y_TOTAL"));
			obj.setR31_T_FT(rs.getBigDecimal("R31_T_FT"));
			obj.setR31_T_HTM(rs.getBigDecimal("R31_T_HTM"));
			obj.setR31_T_TOTAL(rs.getBigDecimal("R31_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Summary_Entity3
//===========================

	public class BRRS_M_SEC_Summary_Entity3 {

		private String R26_PRODUCT;
		private BigDecimal R26_0_1Y_FT;
		private BigDecimal R26_0_1Y_HTM;
		private BigDecimal R26_0_1Y_TOTAL;
		private BigDecimal R26_1_5Y_FT;
		private BigDecimal R26_1_5Y_HTM;
		private BigDecimal R26_1_5Y_TOTAL;
		private BigDecimal R26_O5Y_FT;
		private BigDecimal R26_O5Y_HTM;
		private BigDecimal R26_O5Y_TOTAL;
		private BigDecimal R26_T_FT;
		private BigDecimal R26_T_HTM;
		private BigDecimal R26_T_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_0_1Y_FT;
		private BigDecimal R27_0_1Y_HTM;
		private BigDecimal R27_0_1Y_TOTAL;
		private BigDecimal R27_1_5Y_FT;
		private BigDecimal R27_1_5Y_HTM;
		private BigDecimal R27_1_5Y_TOTAL;
		private BigDecimal R27_O5Y_FT;
		private BigDecimal R27_O5Y_HTM;
		private BigDecimal R27_O5Y_TOTAL;
		private BigDecimal R27_T_FT;
		private BigDecimal R27_T_HTM;
		private BigDecimal R27_T_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_0_1Y_FT;
		private BigDecimal R28_0_1Y_HTM;
		private BigDecimal R28_0_1Y_TOTAL;
		private BigDecimal R28_1_5Y_FT;
		private BigDecimal R28_1_5Y_HTM;
		private BigDecimal R28_1_5Y_TOTAL;
		private BigDecimal R28_O5Y_FT;
		private BigDecimal R28_O5Y_HTM;
		private BigDecimal R28_O5Y_TOTAL;
		private BigDecimal R28_T_FT;
		private BigDecimal R28_T_HTM;
		private BigDecimal R28_T_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_0_1Y_FT;
		private BigDecimal R29_0_1Y_HTM;
		private BigDecimal R29_0_1Y_TOTAL;
		private BigDecimal R29_1_5Y_FT;
		private BigDecimal R29_1_5Y_HTM;
		private BigDecimal R29_1_5Y_TOTAL;
		private BigDecimal R29_O5Y_FT;
		private BigDecimal R29_O5Y_HTM;
		private BigDecimal R29_O5Y_TOTAL;
		private BigDecimal R29_T_FT;
		private BigDecimal R29_T_HTM;
		private BigDecimal R29_T_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_0_1Y_FT;
		private BigDecimal R30_0_1Y_HTM;
		private BigDecimal R30_0_1Y_TOTAL;
		private BigDecimal R30_1_5Y_FT;
		private BigDecimal R30_1_5Y_HTM;
		private BigDecimal R30_1_5Y_TOTAL;
		private BigDecimal R30_O5Y_FT;
		private BigDecimal R30_O5Y_HTM;
		private BigDecimal R30_O5Y_TOTAL;
		private BigDecimal R30_T_FT;
		private BigDecimal R30_T_HTM;
		private BigDecimal R30_T_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_0_1Y_FT;
		private BigDecimal R31_0_1Y_HTM;
		private BigDecimal R31_0_1Y_TOTAL;
		private BigDecimal R31_1_5Y_FT;
		private BigDecimal R31_1_5Y_HTM;
		private BigDecimal R31_1_5Y_TOTAL;
		private BigDecimal R31_O5Y_FT;
		private BigDecimal R31_O5Y_HTM;
		private BigDecimal R31_O5Y_TOTAL;
		private BigDecimal R31_T_FT;
		private BigDecimal R31_T_HTM;
		private BigDecimal R31_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default Constructor
		public BRRS_M_SEC_Summary_Entity3() {
			super();
		}

		// Getters and Setters
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}

		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}

		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}

		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}

		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}

		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}

		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}

		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}

		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}

		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}

		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}

		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}

		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}

		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}

		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}

		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}

		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}

		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}

		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}

		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}

		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}

		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}

		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}

		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}

		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}

		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}

		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}

		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}

		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}

		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}

		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}

		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}

		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}

		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}

		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}

		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}

		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}

		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}

		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}

		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}

		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}

		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}

		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}

		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}

		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}

		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}

		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}

		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}

		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}

		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}

		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}

		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}

		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}

		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}

		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}

		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}

		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}

		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}

		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}

		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}

		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}

		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}

		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}

		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}

		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}

		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}

		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}

		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}

		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}

		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}

		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}

		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}

		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}

		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}

		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}

		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}

		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}

		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}

		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}

		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}

		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}

		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}

		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}

		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}

		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}

		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}

		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}

		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}

		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}

		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}

		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}

		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}

		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}

		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}

		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}

		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}

		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}

		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}

		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}

		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}

		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}

		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}

		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}

		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}

		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}

		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}

		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}

		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}

		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}

		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}

		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}

		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}

		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}

		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}

		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}

		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}

		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}

		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}

		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}

		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}

		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}

		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}

		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}

		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}

		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}

		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}

		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}

		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}

		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}

		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}

		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}

		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}

		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}

		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}

		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}

		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}

		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}

		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}

		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
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
	}

//===========================
//BRRS_M_SEC_Summary_RowMapper4
//===========================

	public class BRRS_M_SEC_Summary_RowMapper4 implements RowMapper<BRRS_M_SEC_Summary_Entity4> {

		@Override
		public BRRS_M_SEC_Summary_Entity4 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Summary_Entity4 obj = new BRRS_M_SEC_Summary_Entity4();

			// =========================
			// R36
			// =========================
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_0_1Y_FT(rs.getBigDecimal("R36_0_1Y_FT"));
			obj.setR36_0_1Y_HTM(rs.getBigDecimal("R36_0_1Y_HTM"));
			obj.setR36_0_1Y_TOTAL(rs.getBigDecimal("R36_0_1Y_TOTAL"));
			obj.setR36_1_5Y_FT(rs.getBigDecimal("R36_1_5Y_FT"));
			obj.setR36_1_5Y_HTM(rs.getBigDecimal("R36_1_5Y_HTM"));
			obj.setR36_1_5Y_TOTAL(rs.getBigDecimal("R36_1_5Y_TOTAL"));
			obj.setR36_O5Y_FT(rs.getBigDecimal("R36_O5Y_FT"));
			obj.setR36_O5Y_HTM(rs.getBigDecimal("R36_O5Y_HTM"));
			obj.setR36_O5Y_TOTAL(rs.getBigDecimal("R36_O5Y_TOTAL"));
			obj.setR36_T_FT(rs.getBigDecimal("R36_T_FT"));
			obj.setR36_T_HTM(rs.getBigDecimal("R36_T_HTM"));
			obj.setR36_T_TOTAL(rs.getBigDecimal("R36_T_TOTAL"));

			// =========================
			// R37
			// =========================
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_0_1Y_FT(rs.getBigDecimal("R37_0_1Y_FT"));
			obj.setR37_0_1Y_HTM(rs.getBigDecimal("R37_0_1Y_HTM"));
			obj.setR37_0_1Y_TOTAL(rs.getBigDecimal("R37_0_1Y_TOTAL"));
			obj.setR37_1_5Y_FT(rs.getBigDecimal("R37_1_5Y_FT"));
			obj.setR37_1_5Y_HTM(rs.getBigDecimal("R37_1_5Y_HTM"));
			obj.setR37_1_5Y_TOTAL(rs.getBigDecimal("R37_1_5Y_TOTAL"));
			obj.setR37_O5Y_FT(rs.getBigDecimal("R37_O5Y_FT"));
			obj.setR37_O5Y_HTM(rs.getBigDecimal("R37_O5Y_HTM"));
			obj.setR37_O5Y_TOTAL(rs.getBigDecimal("R37_O5Y_TOTAL"));
			obj.setR37_T_FT(rs.getBigDecimal("R37_T_FT"));
			obj.setR37_T_HTM(rs.getBigDecimal("R37_T_HTM"));
			obj.setR37_T_TOTAL(rs.getBigDecimal("R37_T_TOTAL"));

			// =========================
			// R38
			// =========================
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_0_1Y_FT(rs.getBigDecimal("R38_0_1Y_FT"));
			obj.setR38_0_1Y_HTM(rs.getBigDecimal("R38_0_1Y_HTM"));
			obj.setR38_0_1Y_TOTAL(rs.getBigDecimal("R38_0_1Y_TOTAL"));
			obj.setR38_1_5Y_FT(rs.getBigDecimal("R38_1_5Y_FT"));
			obj.setR38_1_5Y_HTM(rs.getBigDecimal("R38_1_5Y_HTM"));
			obj.setR38_1_5Y_TOTAL(rs.getBigDecimal("R38_1_5Y_TOTAL"));
			obj.setR38_O5Y_FT(rs.getBigDecimal("R38_O5Y_FT"));
			obj.setR38_O5Y_HTM(rs.getBigDecimal("R38_O5Y_HTM"));
			obj.setR38_O5Y_TOTAL(rs.getBigDecimal("R38_O5Y_TOTAL"));
			obj.setR38_T_FT(rs.getBigDecimal("R38_T_FT"));
			obj.setR38_T_HTM(rs.getBigDecimal("R38_T_HTM"));
			obj.setR38_T_TOTAL(rs.getBigDecimal("R38_T_TOTAL"));

			// =========================
			// R39
			// =========================
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_0_1Y_FT(rs.getBigDecimal("R39_0_1Y_FT"));
			obj.setR39_0_1Y_HTM(rs.getBigDecimal("R39_0_1Y_HTM"));
			obj.setR39_0_1Y_TOTAL(rs.getBigDecimal("R39_0_1Y_TOTAL"));
			obj.setR39_1_5Y_FT(rs.getBigDecimal("R39_1_5Y_FT"));
			obj.setR39_1_5Y_HTM(rs.getBigDecimal("R39_1_5Y_HTM"));
			obj.setR39_1_5Y_TOTAL(rs.getBigDecimal("R39_1_5Y_TOTAL"));
			obj.setR39_O5Y_FT(rs.getBigDecimal("R39_O5Y_FT"));
			obj.setR39_O5Y_HTM(rs.getBigDecimal("R39_O5Y_HTM"));
			obj.setR39_O5Y_TOTAL(rs.getBigDecimal("R39_O5Y_TOTAL"));
			obj.setR39_T_FT(rs.getBigDecimal("R39_T_FT"));
			obj.setR39_T_HTM(rs.getBigDecimal("R39_T_HTM"));
			obj.setR39_T_TOTAL(rs.getBigDecimal("R39_T_TOTAL"));

			// =========================
			// R40
			// =========================
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_0_1Y_FT(rs.getBigDecimal("R40_0_1Y_FT"));
			obj.setR40_0_1Y_HTM(rs.getBigDecimal("R40_0_1Y_HTM"));
			obj.setR40_0_1Y_TOTAL(rs.getBigDecimal("R40_0_1Y_TOTAL"));
			obj.setR40_1_5Y_FT(rs.getBigDecimal("R40_1_5Y_FT"));
			obj.setR40_1_5Y_HTM(rs.getBigDecimal("R40_1_5Y_HTM"));
			obj.setR40_1_5Y_TOTAL(rs.getBigDecimal("R40_1_5Y_TOTAL"));
			obj.setR40_O5Y_FT(rs.getBigDecimal("R40_O5Y_FT"));
			obj.setR40_O5Y_HTM(rs.getBigDecimal("R40_O5Y_HTM"));
			obj.setR40_O5Y_TOTAL(rs.getBigDecimal("R40_O5Y_TOTAL"));
			obj.setR40_T_FT(rs.getBigDecimal("R40_T_FT"));
			obj.setR40_T_HTM(rs.getBigDecimal("R40_T_HTM"));
			obj.setR40_T_TOTAL(rs.getBigDecimal("R40_T_TOTAL"));

			// =========================
			// R41
			// =========================
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_0_1Y_FT(rs.getBigDecimal("R41_0_1Y_FT"));
			obj.setR41_0_1Y_HTM(rs.getBigDecimal("R41_0_1Y_HTM"));
			obj.setR41_0_1Y_TOTAL(rs.getBigDecimal("R41_0_1Y_TOTAL"));
			obj.setR41_1_5Y_FT(rs.getBigDecimal("R41_1_5Y_FT"));
			obj.setR41_1_5Y_HTM(rs.getBigDecimal("R41_1_5Y_HTM"));
			obj.setR41_1_5Y_TOTAL(rs.getBigDecimal("R41_1_5Y_TOTAL"));
			obj.setR41_O5Y_FT(rs.getBigDecimal("R41_O5Y_FT"));
			obj.setR41_O5Y_HTM(rs.getBigDecimal("R41_O5Y_HTM"));
			obj.setR41_O5Y_TOTAL(rs.getBigDecimal("R41_O5Y_TOTAL"));
			obj.setR41_T_FT(rs.getBigDecimal("R41_T_FT"));
			obj.setR41_T_HTM(rs.getBigDecimal("R41_T_HTM"));
			obj.setR41_T_TOTAL(rs.getBigDecimal("R41_T_TOTAL"));

			// =========================
			// R42
			// =========================
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_0_1Y_FT(rs.getBigDecimal("R42_0_1Y_FT"));
			obj.setR42_0_1Y_HTM(rs.getBigDecimal("R42_0_1Y_HTM"));
			obj.setR42_0_1Y_TOTAL(rs.getBigDecimal("R42_0_1Y_TOTAL"));
			obj.setR42_1_5Y_FT(rs.getBigDecimal("R42_1_5Y_FT"));
			obj.setR42_1_5Y_HTM(rs.getBigDecimal("R42_1_5Y_HTM"));
			obj.setR42_1_5Y_TOTAL(rs.getBigDecimal("R42_1_5Y_TOTAL"));
			obj.setR42_O5Y_FT(rs.getBigDecimal("R42_O5Y_FT"));
			obj.setR42_O5Y_HTM(rs.getBigDecimal("R42_O5Y_HTM"));
			obj.setR42_O5Y_TOTAL(rs.getBigDecimal("R42_O5Y_TOTAL"));
			obj.setR42_T_FT(rs.getBigDecimal("R42_T_FT"));
			obj.setR42_T_HTM(rs.getBigDecimal("R42_T_HTM"));
			obj.setR42_T_TOTAL(rs.getBigDecimal("R42_T_TOTAL"));

			// =========================
			// R43
			// =========================
			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
			obj.setR43_0_1Y_FT(rs.getBigDecimal("R43_0_1Y_FT"));
			obj.setR43_0_1Y_HTM(rs.getBigDecimal("R43_0_1Y_HTM"));
			obj.setR43_0_1Y_TOTAL(rs.getBigDecimal("R43_0_1Y_TOTAL"));
			obj.setR43_1_5Y_FT(rs.getBigDecimal("R43_1_5Y_FT"));
			obj.setR43_1_5Y_HTM(rs.getBigDecimal("R43_1_5Y_HTM"));
			obj.setR43_1_5Y_TOTAL(rs.getBigDecimal("R43_1_5Y_TOTAL"));
			obj.setR43_O5Y_FT(rs.getBigDecimal("R43_O5Y_FT"));
			obj.setR43_O5Y_HTM(rs.getBigDecimal("R43_O5Y_HTM"));
			obj.setR43_O5Y_TOTAL(rs.getBigDecimal("R43_O5Y_TOTAL"));
			obj.setR43_T_FT(rs.getBigDecimal("R43_T_FT"));
			obj.setR43_T_HTM(rs.getBigDecimal("R43_T_HTM"));
			obj.setR43_T_TOTAL(rs.getBigDecimal("R43_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Summary_Entity4
//===========================

	public class BRRS_M_SEC_Summary_Entity4 {

		private String R36_PRODUCT;
		private BigDecimal R36_0_1Y_FT;
		private BigDecimal R36_0_1Y_HTM;
		private BigDecimal R36_0_1Y_TOTAL;
		private BigDecimal R36_1_5Y_FT;
		private BigDecimal R36_1_5Y_HTM;
		private BigDecimal R36_1_5Y_TOTAL;
		private BigDecimal R36_O5Y_FT;
		private BigDecimal R36_O5Y_HTM;
		private BigDecimal R36_O5Y_TOTAL;
		private BigDecimal R36_T_FT;
		private BigDecimal R36_T_HTM;
		private BigDecimal R36_T_TOTAL;

		private String R37_PRODUCT;
		private BigDecimal R37_0_1Y_FT;
		private BigDecimal R37_0_1Y_HTM;
		private BigDecimal R37_0_1Y_TOTAL;
		private BigDecimal R37_1_5Y_FT;
		private BigDecimal R37_1_5Y_HTM;
		private BigDecimal R37_1_5Y_TOTAL;
		private BigDecimal R37_O5Y_FT;
		private BigDecimal R37_O5Y_HTM;
		private BigDecimal R37_O5Y_TOTAL;
		private BigDecimal R37_T_FT;
		private BigDecimal R37_T_HTM;
		private BigDecimal R37_T_TOTAL;

		private String R38_PRODUCT;
		private BigDecimal R38_0_1Y_FT;
		private BigDecimal R38_0_1Y_HTM;
		private BigDecimal R38_0_1Y_TOTAL;
		private BigDecimal R38_1_5Y_FT;
		private BigDecimal R38_1_5Y_HTM;
		private BigDecimal R38_1_5Y_TOTAL;
		private BigDecimal R38_O5Y_FT;
		private BigDecimal R38_O5Y_HTM;
		private BigDecimal R38_O5Y_TOTAL;
		private BigDecimal R38_T_FT;
		private BigDecimal R38_T_HTM;
		private BigDecimal R38_T_TOTAL;

		private String R39_PRODUCT;
		private BigDecimal R39_0_1Y_FT;
		private BigDecimal R39_0_1Y_HTM;
		private BigDecimal R39_0_1Y_TOTAL;
		private BigDecimal R39_1_5Y_FT;
		private BigDecimal R39_1_5Y_HTM;
		private BigDecimal R39_1_5Y_TOTAL;
		private BigDecimal R39_O5Y_FT;
		private BigDecimal R39_O5Y_HTM;
		private BigDecimal R39_O5Y_TOTAL;
		private BigDecimal R39_T_FT;
		private BigDecimal R39_T_HTM;
		private BigDecimal R39_T_TOTAL;

		private String R40_PRODUCT;
		private BigDecimal R40_0_1Y_FT;
		private BigDecimal R40_0_1Y_HTM;
		private BigDecimal R40_0_1Y_TOTAL;
		private BigDecimal R40_1_5Y_FT;
		private BigDecimal R40_1_5Y_HTM;
		private BigDecimal R40_1_5Y_TOTAL;
		private BigDecimal R40_O5Y_FT;
		private BigDecimal R40_O5Y_HTM;
		private BigDecimal R40_O5Y_TOTAL;
		private BigDecimal R40_T_FT;
		private BigDecimal R40_T_HTM;
		private BigDecimal R40_T_TOTAL;

		private String R41_PRODUCT;
		private BigDecimal R41_0_1Y_FT;
		private BigDecimal R41_0_1Y_HTM;
		private BigDecimal R41_0_1Y_TOTAL;
		private BigDecimal R41_1_5Y_FT;
		private BigDecimal R41_1_5Y_HTM;
		private BigDecimal R41_1_5Y_TOTAL;
		private BigDecimal R41_O5Y_FT;
		private BigDecimal R41_O5Y_HTM;
		private BigDecimal R41_O5Y_TOTAL;
		private BigDecimal R41_T_FT;
		private BigDecimal R41_T_HTM;
		private BigDecimal R41_T_TOTAL;

		private String R42_PRODUCT;
		private BigDecimal R42_0_1Y_FT;
		private BigDecimal R42_0_1Y_HTM;
		private BigDecimal R42_0_1Y_TOTAL;
		private BigDecimal R42_1_5Y_FT;
		private BigDecimal R42_1_5Y_HTM;
		private BigDecimal R42_1_5Y_TOTAL;
		private BigDecimal R42_O5Y_FT;
		private BigDecimal R42_O5Y_HTM;
		private BigDecimal R42_O5Y_TOTAL;
		private BigDecimal R42_T_FT;
		private BigDecimal R42_T_HTM;
		private BigDecimal R42_T_TOTAL;

		private String R43_PRODUCT;
		private BigDecimal R43_0_1Y_FT;
		private BigDecimal R43_0_1Y_HTM;
		private BigDecimal R43_0_1Y_TOTAL;
		private BigDecimal R43_1_5Y_FT;
		private BigDecimal R43_1_5Y_HTM;
		private BigDecimal R43_1_5Y_TOTAL;
		private BigDecimal R43_O5Y_FT;
		private BigDecimal R43_O5Y_HTM;
		private BigDecimal R43_O5Y_TOTAL;
		private BigDecimal R43_T_FT;
		private BigDecimal R43_T_HTM;
		private BigDecimal R43_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default Constructor
		public BRRS_M_SEC_Summary_Entity4() {
			super();
		}

		// Getters and Setters
		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_0_1Y_FT() {
			return R36_0_1Y_FT;
		}

		public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
			R36_0_1Y_FT = r36_0_1y_FT;
		}

		public BigDecimal getR36_0_1Y_HTM() {
			return R36_0_1Y_HTM;
		}

		public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
			R36_0_1Y_HTM = r36_0_1y_HTM;
		}

		public BigDecimal getR36_0_1Y_TOTAL() {
			return R36_0_1Y_TOTAL;
		}

		public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
			R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
		}

		public BigDecimal getR36_1_5Y_FT() {
			return R36_1_5Y_FT;
		}

		public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
			R36_1_5Y_FT = r36_1_5y_FT;
		}

		public BigDecimal getR36_1_5Y_HTM() {
			return R36_1_5Y_HTM;
		}

		public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
			R36_1_5Y_HTM = r36_1_5y_HTM;
		}

		public BigDecimal getR36_1_5Y_TOTAL() {
			return R36_1_5Y_TOTAL;
		}

		public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
			R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
		}

		public BigDecimal getR36_O5Y_FT() {
			return R36_O5Y_FT;
		}

		public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
			R36_O5Y_FT = r36_O5Y_FT;
		}

		public BigDecimal getR36_O5Y_HTM() {
			return R36_O5Y_HTM;
		}

		public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
			R36_O5Y_HTM = r36_O5Y_HTM;
		}

		public BigDecimal getR36_O5Y_TOTAL() {
			return R36_O5Y_TOTAL;
		}

		public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
			R36_O5Y_TOTAL = r36_O5Y_TOTAL;
		}

		public BigDecimal getR36_T_FT() {
			return R36_T_FT;
		}

		public void setR36_T_FT(BigDecimal r36_T_FT) {
			R36_T_FT = r36_T_FT;
		}

		public BigDecimal getR36_T_HTM() {
			return R36_T_HTM;
		}

		public void setR36_T_HTM(BigDecimal r36_T_HTM) {
			R36_T_HTM = r36_T_HTM;
		}

		public BigDecimal getR36_T_TOTAL() {
			return R36_T_TOTAL;
		}

		public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
			R36_T_TOTAL = r36_T_TOTAL;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_0_1Y_FT() {
			return R37_0_1Y_FT;
		}

		public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
			R37_0_1Y_FT = r37_0_1y_FT;
		}

		public BigDecimal getR37_0_1Y_HTM() {
			return R37_0_1Y_HTM;
		}

		public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
			R37_0_1Y_HTM = r37_0_1y_HTM;
		}

		public BigDecimal getR37_0_1Y_TOTAL() {
			return R37_0_1Y_TOTAL;
		}

		public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
			R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
		}

		public BigDecimal getR37_1_5Y_FT() {
			return R37_1_5Y_FT;
		}

		public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
			R37_1_5Y_FT = r37_1_5y_FT;
		}

		public BigDecimal getR37_1_5Y_HTM() {
			return R37_1_5Y_HTM;
		}

		public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
			R37_1_5Y_HTM = r37_1_5y_HTM;
		}

		public BigDecimal getR37_1_5Y_TOTAL() {
			return R37_1_5Y_TOTAL;
		}

		public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
			R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
		}

		public BigDecimal getR37_O5Y_FT() {
			return R37_O5Y_FT;
		}

		public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
			R37_O5Y_FT = r37_O5Y_FT;
		}

		public BigDecimal getR37_O5Y_HTM() {
			return R37_O5Y_HTM;
		}

		public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
			R37_O5Y_HTM = r37_O5Y_HTM;
		}

		public BigDecimal getR37_O5Y_TOTAL() {
			return R37_O5Y_TOTAL;
		}

		public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
			R37_O5Y_TOTAL = r37_O5Y_TOTAL;
		}

		public BigDecimal getR37_T_FT() {
			return R37_T_FT;
		}

		public void setR37_T_FT(BigDecimal r37_T_FT) {
			R37_T_FT = r37_T_FT;
		}

		public BigDecimal getR37_T_HTM() {
			return R37_T_HTM;
		}

		public void setR37_T_HTM(BigDecimal r37_T_HTM) {
			R37_T_HTM = r37_T_HTM;
		}

		public BigDecimal getR37_T_TOTAL() {
			return R37_T_TOTAL;
		}

		public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
			R37_T_TOTAL = r37_T_TOTAL;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_0_1Y_FT() {
			return R38_0_1Y_FT;
		}

		public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
			R38_0_1Y_FT = r38_0_1y_FT;
		}

		public BigDecimal getR38_0_1Y_HTM() {
			return R38_0_1Y_HTM;
		}

		public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
			R38_0_1Y_HTM = r38_0_1y_HTM;
		}

		public BigDecimal getR38_0_1Y_TOTAL() {
			return R38_0_1Y_TOTAL;
		}

		public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
			R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
		}

		public BigDecimal getR38_1_5Y_FT() {
			return R38_1_5Y_FT;
		}

		public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
			R38_1_5Y_FT = r38_1_5y_FT;
		}

		public BigDecimal getR38_1_5Y_HTM() {
			return R38_1_5Y_HTM;
		}

		public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
			R38_1_5Y_HTM = r38_1_5y_HTM;
		}

		public BigDecimal getR38_1_5Y_TOTAL() {
			return R38_1_5Y_TOTAL;
		}

		public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
			R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
		}

		public BigDecimal getR38_O5Y_FT() {
			return R38_O5Y_FT;
		}

		public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
			R38_O5Y_FT = r38_O5Y_FT;
		}

		public BigDecimal getR38_O5Y_HTM() {
			return R38_O5Y_HTM;
		}

		public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
			R38_O5Y_HTM = r38_O5Y_HTM;
		}

		public BigDecimal getR38_O5Y_TOTAL() {
			return R38_O5Y_TOTAL;
		}

		public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
			R38_O5Y_TOTAL = r38_O5Y_TOTAL;
		}

		public BigDecimal getR38_T_FT() {
			return R38_T_FT;
		}

		public void setR38_T_FT(BigDecimal r38_T_FT) {
			R38_T_FT = r38_T_FT;
		}

		public BigDecimal getR38_T_HTM() {
			return R38_T_HTM;
		}

		public void setR38_T_HTM(BigDecimal r38_T_HTM) {
			R38_T_HTM = r38_T_HTM;
		}

		public BigDecimal getR38_T_TOTAL() {
			return R38_T_TOTAL;
		}

		public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
			R38_T_TOTAL = r38_T_TOTAL;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_0_1Y_FT() {
			return R39_0_1Y_FT;
		}

		public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
			R39_0_1Y_FT = r39_0_1y_FT;
		}

		public BigDecimal getR39_0_1Y_HTM() {
			return R39_0_1Y_HTM;
		}

		public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
			R39_0_1Y_HTM = r39_0_1y_HTM;
		}

		public BigDecimal getR39_0_1Y_TOTAL() {
			return R39_0_1Y_TOTAL;
		}

		public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
			R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
		}

		public BigDecimal getR39_1_5Y_FT() {
			return R39_1_5Y_FT;
		}

		public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
			R39_1_5Y_FT = r39_1_5y_FT;
		}

		public BigDecimal getR39_1_5Y_HTM() {
			return R39_1_5Y_HTM;
		}

		public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
			R39_1_5Y_HTM = r39_1_5y_HTM;
		}

		public BigDecimal getR39_1_5Y_TOTAL() {
			return R39_1_5Y_TOTAL;
		}

		public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
			R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
		}

		public BigDecimal getR39_O5Y_FT() {
			return R39_O5Y_FT;
		}

		public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
			R39_O5Y_FT = r39_O5Y_FT;
		}

		public BigDecimal getR39_O5Y_HTM() {
			return R39_O5Y_HTM;
		}

		public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
			R39_O5Y_HTM = r39_O5Y_HTM;
		}

		public BigDecimal getR39_O5Y_TOTAL() {
			return R39_O5Y_TOTAL;
		}

		public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
			R39_O5Y_TOTAL = r39_O5Y_TOTAL;
		}

		public BigDecimal getR39_T_FT() {
			return R39_T_FT;
		}

		public void setR39_T_FT(BigDecimal r39_T_FT) {
			R39_T_FT = r39_T_FT;
		}

		public BigDecimal getR39_T_HTM() {
			return R39_T_HTM;
		}

		public void setR39_T_HTM(BigDecimal r39_T_HTM) {
			R39_T_HTM = r39_T_HTM;
		}

		public BigDecimal getR39_T_TOTAL() {
			return R39_T_TOTAL;
		}

		public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
			R39_T_TOTAL = r39_T_TOTAL;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_0_1Y_FT() {
			return R40_0_1Y_FT;
		}

		public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
			R40_0_1Y_FT = r40_0_1y_FT;
		}

		public BigDecimal getR40_0_1Y_HTM() {
			return R40_0_1Y_HTM;
		}

		public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
			R40_0_1Y_HTM = r40_0_1y_HTM;
		}

		public BigDecimal getR40_0_1Y_TOTAL() {
			return R40_0_1Y_TOTAL;
		}

		public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
			R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
		}

		public BigDecimal getR40_1_5Y_FT() {
			return R40_1_5Y_FT;
		}

		public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
			R40_1_5Y_FT = r40_1_5y_FT;
		}

		public BigDecimal getR40_1_5Y_HTM() {
			return R40_1_5Y_HTM;
		}

		public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
			R40_1_5Y_HTM = r40_1_5y_HTM;
		}

		public BigDecimal getR40_1_5Y_TOTAL() {
			return R40_1_5Y_TOTAL;
		}

		public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
			R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
		}

		public BigDecimal getR40_O5Y_FT() {
			return R40_O5Y_FT;
		}

		public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
			R40_O5Y_FT = r40_O5Y_FT;
		}

		public BigDecimal getR40_O5Y_HTM() {
			return R40_O5Y_HTM;
		}

		public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
			R40_O5Y_HTM = r40_O5Y_HTM;
		}

		public BigDecimal getR40_O5Y_TOTAL() {
			return R40_O5Y_TOTAL;
		}

		public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
			R40_O5Y_TOTAL = r40_O5Y_TOTAL;
		}

		public BigDecimal getR40_T_FT() {
			return R40_T_FT;
		}

		public void setR40_T_FT(BigDecimal r40_T_FT) {
			R40_T_FT = r40_T_FT;
		}

		public BigDecimal getR40_T_HTM() {
			return R40_T_HTM;
		}

		public void setR40_T_HTM(BigDecimal r40_T_HTM) {
			R40_T_HTM = r40_T_HTM;
		}

		public BigDecimal getR40_T_TOTAL() {
			return R40_T_TOTAL;
		}

		public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
			R40_T_TOTAL = r40_T_TOTAL;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_0_1Y_FT() {
			return R41_0_1Y_FT;
		}

		public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
			R41_0_1Y_FT = r41_0_1y_FT;
		}

		public BigDecimal getR41_0_1Y_HTM() {
			return R41_0_1Y_HTM;
		}

		public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
			R41_0_1Y_HTM = r41_0_1y_HTM;
		}

		public BigDecimal getR41_0_1Y_TOTAL() {
			return R41_0_1Y_TOTAL;
		}

		public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
			R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
		}

		public BigDecimal getR41_1_5Y_FT() {
			return R41_1_5Y_FT;
		}

		public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
			R41_1_5Y_FT = r41_1_5y_FT;
		}

		public BigDecimal getR41_1_5Y_HTM() {
			return R41_1_5Y_HTM;
		}

		public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
			R41_1_5Y_HTM = r41_1_5y_HTM;
		}

		public BigDecimal getR41_1_5Y_TOTAL() {
			return R41_1_5Y_TOTAL;
		}

		public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
			R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
		}

		public BigDecimal getR41_O5Y_FT() {
			return R41_O5Y_FT;
		}

		public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
			R41_O5Y_FT = r41_O5Y_FT;
		}

		public BigDecimal getR41_O5Y_HTM() {
			return R41_O5Y_HTM;
		}

		public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
			R41_O5Y_HTM = r41_O5Y_HTM;
		}

		public BigDecimal getR41_O5Y_TOTAL() {
			return R41_O5Y_TOTAL;
		}

		public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
			R41_O5Y_TOTAL = r41_O5Y_TOTAL;
		}

		public BigDecimal getR41_T_FT() {
			return R41_T_FT;
		}

		public void setR41_T_FT(BigDecimal r41_T_FT) {
			R41_T_FT = r41_T_FT;
		}

		public BigDecimal getR41_T_HTM() {
			return R41_T_HTM;
		}

		public void setR41_T_HTM(BigDecimal r41_T_HTM) {
			R41_T_HTM = r41_T_HTM;
		}

		public BigDecimal getR41_T_TOTAL() {
			return R41_T_TOTAL;
		}

		public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
			R41_T_TOTAL = r41_T_TOTAL;
		}

		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_0_1Y_FT() {
			return R42_0_1Y_FT;
		}

		public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
			R42_0_1Y_FT = r42_0_1y_FT;
		}

		public BigDecimal getR42_0_1Y_HTM() {
			return R42_0_1Y_HTM;
		}

		public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
			R42_0_1Y_HTM = r42_0_1y_HTM;
		}

		public BigDecimal getR42_0_1Y_TOTAL() {
			return R42_0_1Y_TOTAL;
		}

		public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
			R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
		}

		public BigDecimal getR42_1_5Y_FT() {
			return R42_1_5Y_FT;
		}

		public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
			R42_1_5Y_FT = r42_1_5y_FT;
		}

		public BigDecimal getR42_1_5Y_HTM() {
			return R42_1_5Y_HTM;
		}

		public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
			R42_1_5Y_HTM = r42_1_5y_HTM;
		}

		public BigDecimal getR42_1_5Y_TOTAL() {
			return R42_1_5Y_TOTAL;
		}

		public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
			R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
		}

		public BigDecimal getR42_O5Y_FT() {
			return R42_O5Y_FT;
		}

		public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
			R42_O5Y_FT = r42_O5Y_FT;
		}

		public BigDecimal getR42_O5Y_HTM() {
			return R42_O5Y_HTM;
		}

		public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
			R42_O5Y_HTM = r42_O5Y_HTM;
		}

		public BigDecimal getR42_O5Y_TOTAL() {
			return R42_O5Y_TOTAL;
		}

		public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
			R42_O5Y_TOTAL = r42_O5Y_TOTAL;
		}

		public BigDecimal getR42_T_FT() {
			return R42_T_FT;
		}

		public void setR42_T_FT(BigDecimal r42_T_FT) {
			R42_T_FT = r42_T_FT;
		}

		public BigDecimal getR42_T_HTM() {
			return R42_T_HTM;
		}

		public void setR42_T_HTM(BigDecimal r42_T_HTM) {
			R42_T_HTM = r42_T_HTM;
		}

		public BigDecimal getR42_T_TOTAL() {
			return R42_T_TOTAL;
		}

		public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
			R42_T_TOTAL = r42_T_TOTAL;
		}

		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public BigDecimal getR43_0_1Y_FT() {
			return R43_0_1Y_FT;
		}

		public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
			R43_0_1Y_FT = r43_0_1y_FT;
		}

		public BigDecimal getR43_0_1Y_HTM() {
			return R43_0_1Y_HTM;
		}

		public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
			R43_0_1Y_HTM = r43_0_1y_HTM;
		}

		public BigDecimal getR43_0_1Y_TOTAL() {
			return R43_0_1Y_TOTAL;
		}

		public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
			R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
		}

		public BigDecimal getR43_1_5Y_FT() {
			return R43_1_5Y_FT;
		}

		public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
			R43_1_5Y_FT = r43_1_5y_FT;
		}

		public BigDecimal getR43_1_5Y_HTM() {
			return R43_1_5Y_HTM;
		}

		public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
			R43_1_5Y_HTM = r43_1_5y_HTM;
		}

		public BigDecimal getR43_1_5Y_TOTAL() {
			return R43_1_5Y_TOTAL;
		}

		public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
			R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
		}

		public BigDecimal getR43_O5Y_FT() {
			return R43_O5Y_FT;
		}

		public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
			R43_O5Y_FT = r43_O5Y_FT;
		}

		public BigDecimal getR43_O5Y_HTM() {
			return R43_O5Y_HTM;
		}

		public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
			R43_O5Y_HTM = r43_O5Y_HTM;
		}

		public BigDecimal getR43_O5Y_TOTAL() {
			return R43_O5Y_TOTAL;
		}

		public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
			R43_O5Y_TOTAL = r43_O5Y_TOTAL;
		}

		public BigDecimal getR43_T_FT() {
			return R43_T_FT;
		}

		public void setR43_T_FT(BigDecimal r43_T_FT) {
			R43_T_FT = r43_T_FT;
		}

		public BigDecimal getR43_T_HTM() {
			return R43_T_HTM;
		}

		public void setR43_T_HTM(BigDecimal r43_T_HTM) {
			R43_T_HTM = r43_T_HTM;
		}

		public BigDecimal getR43_T_TOTAL() {
			return R43_T_TOTAL;
		}

		public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
			R43_T_TOTAL = r43_T_TOTAL;
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
	}

//===========================
//M_SEC_Detail1_RowMapper
//===========================

	public class M_SEC_Detail1_RowMapper implements RowMapper<M_SEC_Detail1_Entity> {

		@Override
		public M_SEC_Detail1_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Detail1_Entity obj = new M_SEC_Detail1_Entity();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA(rs.getBigDecimal("R11_TCA"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA(rs.getBigDecimal("R12_TCA"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA(rs.getBigDecimal("R13_TCA"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA(rs.getBigDecimal("R14_TCA"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA(rs.getBigDecimal("R15_TCA"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA(rs.getBigDecimal("R16_TCA"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TCA(rs.getBigDecimal("R17_TCA"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TCA(rs.getBigDecimal("R18_TCA"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TCA(rs.getBigDecimal("R19_TCA"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Detail1_Entity
//===========================

	public class M_SEC_Detail1_Entity {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA;
		private String R17_PRODUCT;
		private BigDecimal R17_TCA;
		private String R18_PRODUCT;
		private BigDecimal R18_TCA;
		private String R19_PRODUCT;
		private BigDecimal R19_TCA;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default Constructor
		public M_SEC_Detail1_Entity() {
			super();
		}

		// Getters and Setters
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA() {
			return R11_TCA;
		}

		public void setR11_TCA(BigDecimal r11_TCA) {
			R11_TCA = r11_TCA;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA() {
			return R12_TCA;
		}

		public void setR12_TCA(BigDecimal r12_TCA) {
			R12_TCA = r12_TCA;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA() {
			return R13_TCA;
		}

		public void setR13_TCA(BigDecimal r13_TCA) {
			R13_TCA = r13_TCA;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA() {
			return R14_TCA;
		}

		public void setR14_TCA(BigDecimal r14_TCA) {
			R14_TCA = r14_TCA;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA() {
			return R15_TCA;
		}

		public void setR15_TCA(BigDecimal r15_TCA) {
			R15_TCA = r15_TCA;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA() {
			return R16_TCA;
		}

		public void setR16_TCA(BigDecimal r16_TCA) {
			R16_TCA = r16_TCA;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TCA() {
			return R17_TCA;
		}

		public void setR17_TCA(BigDecimal r17_TCA) {
			R17_TCA = r17_TCA;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_TCA() {
			return R18_TCA;
		}

		public void setR18_TCA(BigDecimal r18_TCA) {
			R18_TCA = r18_TCA;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_TCA() {
			return R19_TCA;
		}

		public void setR19_TCA(BigDecimal r19_TCA) {
			R19_TCA = r19_TCA;
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
	}

//===========================
//M_SEC_Detail2_RowMapper
//===========================

	public class M_SEC_Detail2_RowMapper implements RowMapper<M_SEC_Detail2_Entity> {

		@Override
		public M_SEC_Detail2_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Detail2_Entity obj = new M_SEC_Detail2_Entity();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA2(rs.getBigDecimal("R11_TCA2"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA2(rs.getBigDecimal("R12_TCA2"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA2(rs.getBigDecimal("R13_TCA2"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA2(rs.getBigDecimal("R14_TCA2"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA2(rs.getBigDecimal("R15_TCA2"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA2(rs.getBigDecimal("R16_TCA2"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Detail2_Entity
//===========================

	public class M_SEC_Detail2_Entity {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA2;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA2;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA2;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA2;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA2;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA2;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Detail2_Entity() {
			super();
		}

		// Getters and Setters
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA2() {
			return R11_TCA2;
		}

		public void setR11_TCA2(BigDecimal r11_TCA2) {
			R11_TCA2 = r11_TCA2;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA2() {
			return R12_TCA2;
		}

		public void setR12_TCA2(BigDecimal r12_TCA2) {
			R12_TCA2 = r12_TCA2;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA2() {
			return R13_TCA2;
		}

		public void setR13_TCA2(BigDecimal r13_TCA2) {
			R13_TCA2 = r13_TCA2;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA2() {
			return R14_TCA2;
		}

		public void setR14_TCA2(BigDecimal r14_TCA2) {
			R14_TCA2 = r14_TCA2;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA2() {
			return R15_TCA2;
		}

		public void setR15_TCA2(BigDecimal r15_TCA2) {
			R15_TCA2 = r15_TCA2;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA2() {
			return R16_TCA2;
		}

		public void setR16_TCA2(BigDecimal r16_TCA2) {
			R16_TCA2 = r16_TCA2;
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
	}

//===========================
//M_SEC_Detail3_RowMapper
//===========================

	public class M_SEC_Detail3_RowMapper implements RowMapper<M_SEC_Detail3_Entity> {

		@Override
		public M_SEC_Detail3_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Detail3_Entity obj = new M_SEC_Detail3_Entity();

			// =========================
			// R26
			// =========================
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_0_1Y_FT(rs.getBigDecimal("R26_0_1Y_FT"));
			obj.setR26_0_1Y_HTM(rs.getBigDecimal("R26_0_1Y_HTM"));
			obj.setR26_0_1Y_TOTAL(rs.getBigDecimal("R26_0_1Y_TOTAL"));
			obj.setR26_1_5Y_FT(rs.getBigDecimal("R26_1_5Y_FT"));
			obj.setR26_1_5Y_HTM(rs.getBigDecimal("R26_1_5Y_HTM"));
			obj.setR26_1_5Y_TOTAL(rs.getBigDecimal("R26_1_5Y_TOTAL"));
			obj.setR26_O5Y_FT(rs.getBigDecimal("R26_O5Y_FT"));
			obj.setR26_O5Y_HTM(rs.getBigDecimal("R26_O5Y_HTM"));
			obj.setR26_O5Y_TOTAL(rs.getBigDecimal("R26_O5Y_TOTAL"));
			obj.setR26_T_FT(rs.getBigDecimal("R26_T_FT"));
			obj.setR26_T_HTM(rs.getBigDecimal("R26_T_HTM"));
			obj.setR26_T_TOTAL(rs.getBigDecimal("R26_T_TOTAL"));

			// =========================
			// R27
			// =========================
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_0_1Y_FT(rs.getBigDecimal("R27_0_1Y_FT"));
			obj.setR27_0_1Y_HTM(rs.getBigDecimal("R27_0_1Y_HTM"));
			obj.setR27_0_1Y_TOTAL(rs.getBigDecimal("R27_0_1Y_TOTAL"));
			obj.setR27_1_5Y_FT(rs.getBigDecimal("R27_1_5Y_FT"));
			obj.setR27_1_5Y_HTM(rs.getBigDecimal("R27_1_5Y_HTM"));
			obj.setR27_1_5Y_TOTAL(rs.getBigDecimal("R27_1_5Y_TOTAL"));
			obj.setR27_O5Y_FT(rs.getBigDecimal("R27_O5Y_FT"));
			obj.setR27_O5Y_HTM(rs.getBigDecimal("R27_O5Y_HTM"));
			obj.setR27_O5Y_TOTAL(rs.getBigDecimal("R27_O5Y_TOTAL"));
			obj.setR27_T_FT(rs.getBigDecimal("R27_T_FT"));
			obj.setR27_T_HTM(rs.getBigDecimal("R27_T_HTM"));
			obj.setR27_T_TOTAL(rs.getBigDecimal("R27_T_TOTAL"));

			// =========================
			// R28
			// =========================
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_0_1Y_FT(rs.getBigDecimal("R28_0_1Y_FT"));
			obj.setR28_0_1Y_HTM(rs.getBigDecimal("R28_0_1Y_HTM"));
			obj.setR28_0_1Y_TOTAL(rs.getBigDecimal("R28_0_1Y_TOTAL"));
			obj.setR28_1_5Y_FT(rs.getBigDecimal("R28_1_5Y_FT"));
			obj.setR28_1_5Y_HTM(rs.getBigDecimal("R28_1_5Y_HTM"));
			obj.setR28_1_5Y_TOTAL(rs.getBigDecimal("R28_1_5Y_TOTAL"));
			obj.setR28_O5Y_FT(rs.getBigDecimal("R28_O5Y_FT"));
			obj.setR28_O5Y_HTM(rs.getBigDecimal("R28_O5Y_HTM"));
			obj.setR28_O5Y_TOTAL(rs.getBigDecimal("R28_O5Y_TOTAL"));
			obj.setR28_T_FT(rs.getBigDecimal("R28_T_FT"));
			obj.setR28_T_HTM(rs.getBigDecimal("R28_T_HTM"));
			obj.setR28_T_TOTAL(rs.getBigDecimal("R28_T_TOTAL"));

			// =========================
			// R29
			// =========================
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_0_1Y_FT(rs.getBigDecimal("R29_0_1Y_FT"));
			obj.setR29_0_1Y_HTM(rs.getBigDecimal("R29_0_1Y_HTM"));
			obj.setR29_0_1Y_TOTAL(rs.getBigDecimal("R29_0_1Y_TOTAL"));
			obj.setR29_1_5Y_FT(rs.getBigDecimal("R29_1_5Y_FT"));
			obj.setR29_1_5Y_HTM(rs.getBigDecimal("R29_1_5Y_HTM"));
			obj.setR29_1_5Y_TOTAL(rs.getBigDecimal("R29_1_5Y_TOTAL"));
			obj.setR29_O5Y_FT(rs.getBigDecimal("R29_O5Y_FT"));
			obj.setR29_O5Y_HTM(rs.getBigDecimal("R29_O5Y_HTM"));
			obj.setR29_O5Y_TOTAL(rs.getBigDecimal("R29_O5Y_TOTAL"));
			obj.setR29_T_FT(rs.getBigDecimal("R29_T_FT"));
			obj.setR29_T_HTM(rs.getBigDecimal("R29_T_HTM"));
			obj.setR29_T_TOTAL(rs.getBigDecimal("R29_T_TOTAL"));

			// =========================
			// R30
			// =========================
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_0_1Y_FT(rs.getBigDecimal("R30_0_1Y_FT"));
			obj.setR30_0_1Y_HTM(rs.getBigDecimal("R30_0_1Y_HTM"));
			obj.setR30_0_1Y_TOTAL(rs.getBigDecimal("R30_0_1Y_TOTAL"));
			obj.setR30_1_5Y_FT(rs.getBigDecimal("R30_1_5Y_FT"));
			obj.setR30_1_5Y_HTM(rs.getBigDecimal("R30_1_5Y_HTM"));
			obj.setR30_1_5Y_TOTAL(rs.getBigDecimal("R30_1_5Y_TOTAL"));
			obj.setR30_O5Y_FT(rs.getBigDecimal("R30_O5Y_FT"));
			obj.setR30_O5Y_HTM(rs.getBigDecimal("R30_O5Y_HTM"));
			obj.setR30_O5Y_TOTAL(rs.getBigDecimal("R30_O5Y_TOTAL"));
			obj.setR30_T_FT(rs.getBigDecimal("R30_T_FT"));
			obj.setR30_T_HTM(rs.getBigDecimal("R30_T_HTM"));
			obj.setR30_T_TOTAL(rs.getBigDecimal("R30_T_TOTAL"));

			// =========================
			// R31
			// =========================
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_0_1Y_FT(rs.getBigDecimal("R31_0_1Y_FT"));
			obj.setR31_0_1Y_HTM(rs.getBigDecimal("R31_0_1Y_HTM"));
			obj.setR31_0_1Y_TOTAL(rs.getBigDecimal("R31_0_1Y_TOTAL"));
			obj.setR31_1_5Y_FT(rs.getBigDecimal("R31_1_5Y_FT"));
			obj.setR31_1_5Y_HTM(rs.getBigDecimal("R31_1_5Y_HTM"));
			obj.setR31_1_5Y_TOTAL(rs.getBigDecimal("R31_1_5Y_TOTAL"));
			obj.setR31_O5Y_FT(rs.getBigDecimal("R31_O5Y_FT"));
			obj.setR31_O5Y_HTM(rs.getBigDecimal("R31_O5Y_HTM"));
			obj.setR31_O5Y_TOTAL(rs.getBigDecimal("R31_O5Y_TOTAL"));
			obj.setR31_T_FT(rs.getBigDecimal("R31_T_FT"));
			obj.setR31_T_HTM(rs.getBigDecimal("R31_T_HTM"));
			obj.setR31_T_TOTAL(rs.getBigDecimal("R31_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Detail3_Entity
//===========================

	public class M_SEC_Detail3_Entity {

		private String R26_PRODUCT;
		private BigDecimal R26_0_1Y_FT;
		private BigDecimal R26_0_1Y_HTM;
		private BigDecimal R26_0_1Y_TOTAL;
		private BigDecimal R26_1_5Y_FT;
		private BigDecimal R26_1_5Y_HTM;
		private BigDecimal R26_1_5Y_TOTAL;
		private BigDecimal R26_O5Y_FT;
		private BigDecimal R26_O5Y_HTM;
		private BigDecimal R26_O5Y_TOTAL;
		private BigDecimal R26_T_FT;
		private BigDecimal R26_T_HTM;
		private BigDecimal R26_T_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_0_1Y_FT;
		private BigDecimal R27_0_1Y_HTM;
		private BigDecimal R27_0_1Y_TOTAL;
		private BigDecimal R27_1_5Y_FT;
		private BigDecimal R27_1_5Y_HTM;
		private BigDecimal R27_1_5Y_TOTAL;
		private BigDecimal R27_O5Y_FT;
		private BigDecimal R27_O5Y_HTM;
		private BigDecimal R27_O5Y_TOTAL;
		private BigDecimal R27_T_FT;
		private BigDecimal R27_T_HTM;
		private BigDecimal R27_T_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_0_1Y_FT;
		private BigDecimal R28_0_1Y_HTM;
		private BigDecimal R28_0_1Y_TOTAL;
		private BigDecimal R28_1_5Y_FT;
		private BigDecimal R28_1_5Y_HTM;
		private BigDecimal R28_1_5Y_TOTAL;
		private BigDecimal R28_O5Y_FT;
		private BigDecimal R28_O5Y_HTM;
		private BigDecimal R28_O5Y_TOTAL;
		private BigDecimal R28_T_FT;
		private BigDecimal R28_T_HTM;
		private BigDecimal R28_T_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_0_1Y_FT;
		private BigDecimal R29_0_1Y_HTM;
		private BigDecimal R29_0_1Y_TOTAL;
		private BigDecimal R29_1_5Y_FT;
		private BigDecimal R29_1_5Y_HTM;
		private BigDecimal R29_1_5Y_TOTAL;
		private BigDecimal R29_O5Y_FT;
		private BigDecimal R29_O5Y_HTM;
		private BigDecimal R29_O5Y_TOTAL;
		private BigDecimal R29_T_FT;
		private BigDecimal R29_T_HTM;
		private BigDecimal R29_T_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_0_1Y_FT;
		private BigDecimal R30_0_1Y_HTM;
		private BigDecimal R30_0_1Y_TOTAL;
		private BigDecimal R30_1_5Y_FT;
		private BigDecimal R30_1_5Y_HTM;
		private BigDecimal R30_1_5Y_TOTAL;
		private BigDecimal R30_O5Y_FT;
		private BigDecimal R30_O5Y_HTM;
		private BigDecimal R30_O5Y_TOTAL;
		private BigDecimal R30_T_FT;
		private BigDecimal R30_T_HTM;
		private BigDecimal R30_T_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_0_1Y_FT;
		private BigDecimal R31_0_1Y_HTM;
		private BigDecimal R31_0_1Y_TOTAL;
		private BigDecimal R31_1_5Y_FT;
		private BigDecimal R31_1_5Y_HTM;
		private BigDecimal R31_1_5Y_TOTAL;
		private BigDecimal R31_O5Y_FT;
		private BigDecimal R31_O5Y_HTM;
		private BigDecimal R31_O5Y_TOTAL;
		private BigDecimal R31_T_FT;
		private BigDecimal R31_T_HTM;
		private BigDecimal R31_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Detail3_Entity() {
			super();
		}

		// Getters and Setters
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}

		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}

		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}

		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}

		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}

		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}

		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}

		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}

		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}

		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}

		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}

		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}

		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}

		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}

		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}

		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}

		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}

		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}

		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}

		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}

		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}

		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}

		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}

		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}

		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}

		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}

		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}

		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}

		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}

		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}

		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}

		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}

		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}

		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}

		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}

		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}

		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}

		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}

		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}

		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}

		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}

		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}

		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}

		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}

		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}

		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}

		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}

		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}

		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}

		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}

		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}

		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}

		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}

		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}

		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}

		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}

		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}

		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}

		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}

		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}

		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}

		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}

		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}

		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}

		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}

		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}

		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}

		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}

		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}

		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}

		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}

		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}

		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}

		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}

		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}

		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}

		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}

		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}

		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}

		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}

		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}

		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}

		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}

		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}

		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}

		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}

		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}

		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}

		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}

		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}

		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}

		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}

		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}

		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}

		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}

		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}

		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}

		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}

		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}

		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}

		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}

		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}

		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}

		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}

		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}

		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}

		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}

		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}

		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}

		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}

		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}

		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}

		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}

		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}

		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}

		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}

		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}

		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}

		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}

		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}

		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}

		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}

		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}

		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}

		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}

		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}

		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}

		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}

		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}

		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}

		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}

		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}

		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}

		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}

		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}

		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}

		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}

		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}

		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
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
	}

//===========================
//M_SEC_Detail4_RowMapper
//===========================

	public class M_SEC_Detail4_RowMapper implements RowMapper<M_SEC_Detail4_Entity> {

		@Override
		public M_SEC_Detail4_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Detail4_Entity obj = new M_SEC_Detail4_Entity();

			// =========================
			// R36
			// =========================
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_0_1Y_FT(rs.getBigDecimal("R36_0_1Y_FT"));
			obj.setR36_0_1Y_HTM(rs.getBigDecimal("R36_0_1Y_HTM"));
			obj.setR36_0_1Y_TOTAL(rs.getBigDecimal("R36_0_1Y_TOTAL"));
			obj.setR36_1_5Y_FT(rs.getBigDecimal("R36_1_5Y_FT"));
			obj.setR36_1_5Y_HTM(rs.getBigDecimal("R36_1_5Y_HTM"));
			obj.setR36_1_5Y_TOTAL(rs.getBigDecimal("R36_1_5Y_TOTAL"));
			obj.setR36_O5Y_FT(rs.getBigDecimal("R36_O5Y_FT"));
			obj.setR36_O5Y_HTM(rs.getBigDecimal("R36_O5Y_HTM"));
			obj.setR36_O5Y_TOTAL(rs.getBigDecimal("R36_O5Y_TOTAL"));
			obj.setR36_T_FT(rs.getBigDecimal("R36_T_FT"));
			obj.setR36_T_HTM(rs.getBigDecimal("R36_T_HTM"));
			obj.setR36_T_TOTAL(rs.getBigDecimal("R36_T_TOTAL"));

			// =========================
			// R37
			// =========================
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_0_1Y_FT(rs.getBigDecimal("R37_0_1Y_FT"));
			obj.setR37_0_1Y_HTM(rs.getBigDecimal("R37_0_1Y_HTM"));
			obj.setR37_0_1Y_TOTAL(rs.getBigDecimal("R37_0_1Y_TOTAL"));
			obj.setR37_1_5Y_FT(rs.getBigDecimal("R37_1_5Y_FT"));
			obj.setR37_1_5Y_HTM(rs.getBigDecimal("R37_1_5Y_HTM"));
			obj.setR37_1_5Y_TOTAL(rs.getBigDecimal("R37_1_5Y_TOTAL"));
			obj.setR37_O5Y_FT(rs.getBigDecimal("R37_O5Y_FT"));
			obj.setR37_O5Y_HTM(rs.getBigDecimal("R37_O5Y_HTM"));
			obj.setR37_O5Y_TOTAL(rs.getBigDecimal("R37_O5Y_TOTAL"));
			obj.setR37_T_FT(rs.getBigDecimal("R37_T_FT"));
			obj.setR37_T_HTM(rs.getBigDecimal("R37_T_HTM"));
			obj.setR37_T_TOTAL(rs.getBigDecimal("R37_T_TOTAL"));

			// =========================
			// R38
			// =========================
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_0_1Y_FT(rs.getBigDecimal("R38_0_1Y_FT"));
			obj.setR38_0_1Y_HTM(rs.getBigDecimal("R38_0_1Y_HTM"));
			obj.setR38_0_1Y_TOTAL(rs.getBigDecimal("R38_0_1Y_TOTAL"));
			obj.setR38_1_5Y_FT(rs.getBigDecimal("R38_1_5Y_FT"));
			obj.setR38_1_5Y_HTM(rs.getBigDecimal("R38_1_5Y_HTM"));
			obj.setR38_1_5Y_TOTAL(rs.getBigDecimal("R38_1_5Y_TOTAL"));
			obj.setR38_O5Y_FT(rs.getBigDecimal("R38_O5Y_FT"));
			obj.setR38_O5Y_HTM(rs.getBigDecimal("R38_O5Y_HTM"));
			obj.setR38_O5Y_TOTAL(rs.getBigDecimal("R38_O5Y_TOTAL"));
			obj.setR38_T_FT(rs.getBigDecimal("R38_T_FT"));
			obj.setR38_T_HTM(rs.getBigDecimal("R38_T_HTM"));
			obj.setR38_T_TOTAL(rs.getBigDecimal("R38_T_TOTAL"));

			// =========================
			// R39
			// =========================
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_0_1Y_FT(rs.getBigDecimal("R39_0_1Y_FT"));
			obj.setR39_0_1Y_HTM(rs.getBigDecimal("R39_0_1Y_HTM"));
			obj.setR39_0_1Y_TOTAL(rs.getBigDecimal("R39_0_1Y_TOTAL"));
			obj.setR39_1_5Y_FT(rs.getBigDecimal("R39_1_5Y_FT"));
			obj.setR39_1_5Y_HTM(rs.getBigDecimal("R39_1_5Y_HTM"));
			obj.setR39_1_5Y_TOTAL(rs.getBigDecimal("R39_1_5Y_TOTAL"));
			obj.setR39_O5Y_FT(rs.getBigDecimal("R39_O5Y_FT"));
			obj.setR39_O5Y_HTM(rs.getBigDecimal("R39_O5Y_HTM"));
			obj.setR39_O5Y_TOTAL(rs.getBigDecimal("R39_O5Y_TOTAL"));
			obj.setR39_T_FT(rs.getBigDecimal("R39_T_FT"));
			obj.setR39_T_HTM(rs.getBigDecimal("R39_T_HTM"));
			obj.setR39_T_TOTAL(rs.getBigDecimal("R39_T_TOTAL"));

			// =========================
			// R40
			// =========================
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_0_1Y_FT(rs.getBigDecimal("R40_0_1Y_FT"));
			obj.setR40_0_1Y_HTM(rs.getBigDecimal("R40_0_1Y_HTM"));
			obj.setR40_0_1Y_TOTAL(rs.getBigDecimal("R40_0_1Y_TOTAL"));
			obj.setR40_1_5Y_FT(rs.getBigDecimal("R40_1_5Y_FT"));
			obj.setR40_1_5Y_HTM(rs.getBigDecimal("R40_1_5Y_HTM"));
			obj.setR40_1_5Y_TOTAL(rs.getBigDecimal("R40_1_5Y_TOTAL"));
			obj.setR40_O5Y_FT(rs.getBigDecimal("R40_O5Y_FT"));
			obj.setR40_O5Y_HTM(rs.getBigDecimal("R40_O5Y_HTM"));
			obj.setR40_O5Y_TOTAL(rs.getBigDecimal("R40_O5Y_TOTAL"));
			obj.setR40_T_FT(rs.getBigDecimal("R40_T_FT"));
			obj.setR40_T_HTM(rs.getBigDecimal("R40_T_HTM"));
			obj.setR40_T_TOTAL(rs.getBigDecimal("R40_T_TOTAL"));

			// =========================
			// R41
			// =========================
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_0_1Y_FT(rs.getBigDecimal("R41_0_1Y_FT"));
			obj.setR41_0_1Y_HTM(rs.getBigDecimal("R41_0_1Y_HTM"));
			obj.setR41_0_1Y_TOTAL(rs.getBigDecimal("R41_0_1Y_TOTAL"));
			obj.setR41_1_5Y_FT(rs.getBigDecimal("R41_1_5Y_FT"));
			obj.setR41_1_5Y_HTM(rs.getBigDecimal("R41_1_5Y_HTM"));
			obj.setR41_1_5Y_TOTAL(rs.getBigDecimal("R41_1_5Y_TOTAL"));
			obj.setR41_O5Y_FT(rs.getBigDecimal("R41_O5Y_FT"));
			obj.setR41_O5Y_HTM(rs.getBigDecimal("R41_O5Y_HTM"));
			obj.setR41_O5Y_TOTAL(rs.getBigDecimal("R41_O5Y_TOTAL"));
			obj.setR41_T_FT(rs.getBigDecimal("R41_T_FT"));
			obj.setR41_T_HTM(rs.getBigDecimal("R41_T_HTM"));
			obj.setR41_T_TOTAL(rs.getBigDecimal("R41_T_TOTAL"));

			// =========================
			// R42
			// =========================
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_0_1Y_FT(rs.getBigDecimal("R42_0_1Y_FT"));
			obj.setR42_0_1Y_HTM(rs.getBigDecimal("R42_0_1Y_HTM"));
			obj.setR42_0_1Y_TOTAL(rs.getBigDecimal("R42_0_1Y_TOTAL"));
			obj.setR42_1_5Y_FT(rs.getBigDecimal("R42_1_5Y_FT"));
			obj.setR42_1_5Y_HTM(rs.getBigDecimal("R42_1_5Y_HTM"));
			obj.setR42_1_5Y_TOTAL(rs.getBigDecimal("R42_1_5Y_TOTAL"));
			obj.setR42_O5Y_FT(rs.getBigDecimal("R42_O5Y_FT"));
			obj.setR42_O5Y_HTM(rs.getBigDecimal("R42_O5Y_HTM"));
			obj.setR42_O5Y_TOTAL(rs.getBigDecimal("R42_O5Y_TOTAL"));
			obj.setR42_T_FT(rs.getBigDecimal("R42_T_FT"));
			obj.setR42_T_HTM(rs.getBigDecimal("R42_T_HTM"));
			obj.setR42_T_TOTAL(rs.getBigDecimal("R42_T_TOTAL"));

			// =========================
			// R43
			// =========================
			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
			obj.setR43_0_1Y_FT(rs.getBigDecimal("R43_0_1Y_FT"));
			obj.setR43_0_1Y_HTM(rs.getBigDecimal("R43_0_1Y_HTM"));
			obj.setR43_0_1Y_TOTAL(rs.getBigDecimal("R43_0_1Y_TOTAL"));
			obj.setR43_1_5Y_FT(rs.getBigDecimal("R43_1_5Y_FT"));
			obj.setR43_1_5Y_HTM(rs.getBigDecimal("R43_1_5Y_HTM"));
			obj.setR43_1_5Y_TOTAL(rs.getBigDecimal("R43_1_5Y_TOTAL"));
			obj.setR43_O5Y_FT(rs.getBigDecimal("R43_O5Y_FT"));
			obj.setR43_O5Y_HTM(rs.getBigDecimal("R43_O5Y_HTM"));
			obj.setR43_O5Y_TOTAL(rs.getBigDecimal("R43_O5Y_TOTAL"));
			obj.setR43_T_FT(rs.getBigDecimal("R43_T_FT"));
			obj.setR43_T_HTM(rs.getBigDecimal("R43_T_HTM"));
			obj.setR43_T_TOTAL(rs.getBigDecimal("R43_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Detail4_Entity
//===========================

	public class M_SEC_Detail4_Entity {

		private String R36_PRODUCT;
		private BigDecimal R36_0_1Y_FT;
		private BigDecimal R36_0_1Y_HTM;
		private BigDecimal R36_0_1Y_TOTAL;
		private BigDecimal R36_1_5Y_FT;
		private BigDecimal R36_1_5Y_HTM;
		private BigDecimal R36_1_5Y_TOTAL;
		private BigDecimal R36_O5Y_FT;
		private BigDecimal R36_O5Y_HTM;
		private BigDecimal R36_O5Y_TOTAL;
		private BigDecimal R36_T_FT;
		private BigDecimal R36_T_HTM;
		private BigDecimal R36_T_TOTAL;

		private String R37_PRODUCT;
		private BigDecimal R37_0_1Y_FT;
		private BigDecimal R37_0_1Y_HTM;
		private BigDecimal R37_0_1Y_TOTAL;
		private BigDecimal R37_1_5Y_FT;
		private BigDecimal R37_1_5Y_HTM;
		private BigDecimal R37_1_5Y_TOTAL;
		private BigDecimal R37_O5Y_FT;
		private BigDecimal R37_O5Y_HTM;
		private BigDecimal R37_O5Y_TOTAL;
		private BigDecimal R37_T_FT;
		private BigDecimal R37_T_HTM;
		private BigDecimal R37_T_TOTAL;

		private String R38_PRODUCT;
		private BigDecimal R38_0_1Y_FT;
		private BigDecimal R38_0_1Y_HTM;
		private BigDecimal R38_0_1Y_TOTAL;
		private BigDecimal R38_1_5Y_FT;
		private BigDecimal R38_1_5Y_HTM;
		private BigDecimal R38_1_5Y_TOTAL;
		private BigDecimal R38_O5Y_FT;
		private BigDecimal R38_O5Y_HTM;
		private BigDecimal R38_O5Y_TOTAL;
		private BigDecimal R38_T_FT;
		private BigDecimal R38_T_HTM;
		private BigDecimal R38_T_TOTAL;

		private String R39_PRODUCT;
		private BigDecimal R39_0_1Y_FT;
		private BigDecimal R39_0_1Y_HTM;
		private BigDecimal R39_0_1Y_TOTAL;
		private BigDecimal R39_1_5Y_FT;
		private BigDecimal R39_1_5Y_HTM;
		private BigDecimal R39_1_5Y_TOTAL;
		private BigDecimal R39_O5Y_FT;
		private BigDecimal R39_O5Y_HTM;
		private BigDecimal R39_O5Y_TOTAL;
		private BigDecimal R39_T_FT;
		private BigDecimal R39_T_HTM;
		private BigDecimal R39_T_TOTAL;

		private String R40_PRODUCT;
		private BigDecimal R40_0_1Y_FT;
		private BigDecimal R40_0_1Y_HTM;
		private BigDecimal R40_0_1Y_TOTAL;
		private BigDecimal R40_1_5Y_FT;
		private BigDecimal R40_1_5Y_HTM;
		private BigDecimal R40_1_5Y_TOTAL;
		private BigDecimal R40_O5Y_FT;
		private BigDecimal R40_O5Y_HTM;
		private BigDecimal R40_O5Y_TOTAL;
		private BigDecimal R40_T_FT;
		private BigDecimal R40_T_HTM;
		private BigDecimal R40_T_TOTAL;

		private String R41_PRODUCT;
		private BigDecimal R41_0_1Y_FT;
		private BigDecimal R41_0_1Y_HTM;
		private BigDecimal R41_0_1Y_TOTAL;
		private BigDecimal R41_1_5Y_FT;
		private BigDecimal R41_1_5Y_HTM;
		private BigDecimal R41_1_5Y_TOTAL;
		private BigDecimal R41_O5Y_FT;
		private BigDecimal R41_O5Y_HTM;
		private BigDecimal R41_O5Y_TOTAL;
		private BigDecimal R41_T_FT;
		private BigDecimal R41_T_HTM;
		private BigDecimal R41_T_TOTAL;

		private String R42_PRODUCT;
		private BigDecimal R42_0_1Y_FT;
		private BigDecimal R42_0_1Y_HTM;
		private BigDecimal R42_0_1Y_TOTAL;
		private BigDecimal R42_1_5Y_FT;
		private BigDecimal R42_1_5Y_HTM;
		private BigDecimal R42_1_5Y_TOTAL;
		private BigDecimal R42_O5Y_FT;
		private BigDecimal R42_O5Y_HTM;
		private BigDecimal R42_O5Y_TOTAL;
		private BigDecimal R42_T_FT;
		private BigDecimal R42_T_HTM;
		private BigDecimal R42_T_TOTAL;

		private String R43_PRODUCT;
		private BigDecimal R43_0_1Y_FT;
		private BigDecimal R43_0_1Y_HTM;
		private BigDecimal R43_0_1Y_TOTAL;
		private BigDecimal R43_1_5Y_FT;
		private BigDecimal R43_1_5Y_HTM;
		private BigDecimal R43_1_5Y_TOTAL;
		private BigDecimal R43_O5Y_FT;
		private BigDecimal R43_O5Y_HTM;
		private BigDecimal R43_O5Y_TOTAL;
		private BigDecimal R43_T_FT;
		private BigDecimal R43_T_HTM;
		private BigDecimal R43_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Detail4_Entity() {
			super();
		}

		// Getters and Setters for R36
		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_0_1Y_FT() {
			return R36_0_1Y_FT;
		}

		public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
			R36_0_1Y_FT = r36_0_1y_FT;
		}

		public BigDecimal getR36_0_1Y_HTM() {
			return R36_0_1Y_HTM;
		}

		public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
			R36_0_1Y_HTM = r36_0_1y_HTM;
		}

		public BigDecimal getR36_0_1Y_TOTAL() {
			return R36_0_1Y_TOTAL;
		}

		public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
			R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
		}

		public BigDecimal getR36_1_5Y_FT() {
			return R36_1_5Y_FT;
		}

		public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
			R36_1_5Y_FT = r36_1_5y_FT;
		}

		public BigDecimal getR36_1_5Y_HTM() {
			return R36_1_5Y_HTM;
		}

		public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
			R36_1_5Y_HTM = r36_1_5y_HTM;
		}

		public BigDecimal getR36_1_5Y_TOTAL() {
			return R36_1_5Y_TOTAL;
		}

		public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
			R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
		}

		public BigDecimal getR36_O5Y_FT() {
			return R36_O5Y_FT;
		}

		public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
			R36_O5Y_FT = r36_O5Y_FT;
		}

		public BigDecimal getR36_O5Y_HTM() {
			return R36_O5Y_HTM;
		}

		public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
			R36_O5Y_HTM = r36_O5Y_HTM;
		}

		public BigDecimal getR36_O5Y_TOTAL() {
			return R36_O5Y_TOTAL;
		}

		public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
			R36_O5Y_TOTAL = r36_O5Y_TOTAL;
		}

		public BigDecimal getR36_T_FT() {
			return R36_T_FT;
		}

		public void setR36_T_FT(BigDecimal r36_T_FT) {
			R36_T_FT = r36_T_FT;
		}

		public BigDecimal getR36_T_HTM() {
			return R36_T_HTM;
		}

		public void setR36_T_HTM(BigDecimal r36_T_HTM) {
			R36_T_HTM = r36_T_HTM;
		}

		public BigDecimal getR36_T_TOTAL() {
			return R36_T_TOTAL;
		}

		public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
			R36_T_TOTAL = r36_T_TOTAL;
		}

		// Getters and Setters for R37
		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_0_1Y_FT() {
			return R37_0_1Y_FT;
		}

		public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
			R37_0_1Y_FT = r37_0_1y_FT;
		}

		public BigDecimal getR37_0_1Y_HTM() {
			return R37_0_1Y_HTM;
		}

		public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
			R37_0_1Y_HTM = r37_0_1y_HTM;
		}

		public BigDecimal getR37_0_1Y_TOTAL() {
			return R37_0_1Y_TOTAL;
		}

		public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
			R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
		}

		public BigDecimal getR37_1_5Y_FT() {
			return R37_1_5Y_FT;
		}

		public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
			R37_1_5Y_FT = r37_1_5y_FT;
		}

		public BigDecimal getR37_1_5Y_HTM() {
			return R37_1_5Y_HTM;
		}

		public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
			R37_1_5Y_HTM = r37_1_5y_HTM;
		}

		public BigDecimal getR37_1_5Y_TOTAL() {
			return R37_1_5Y_TOTAL;
		}

		public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
			R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
		}

		public BigDecimal getR37_O5Y_FT() {
			return R37_O5Y_FT;
		}

		public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
			R37_O5Y_FT = r37_O5Y_FT;
		}

		public BigDecimal getR37_O5Y_HTM() {
			return R37_O5Y_HTM;
		}

		public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
			R37_O5Y_HTM = r37_O5Y_HTM;
		}

		public BigDecimal getR37_O5Y_TOTAL() {
			return R37_O5Y_TOTAL;
		}

		public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
			R37_O5Y_TOTAL = r37_O5Y_TOTAL;
		}

		public BigDecimal getR37_T_FT() {
			return R37_T_FT;
		}

		public void setR37_T_FT(BigDecimal r37_T_FT) {
			R37_T_FT = r37_T_FT;
		}

		public BigDecimal getR37_T_HTM() {
			return R37_T_HTM;
		}

		public void setR37_T_HTM(BigDecimal r37_T_HTM) {
			R37_T_HTM = r37_T_HTM;
		}

		public BigDecimal getR37_T_TOTAL() {
			return R37_T_TOTAL;
		}

		public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
			R37_T_TOTAL = r37_T_TOTAL;
		}

		// Getters and Setters for R38
		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_0_1Y_FT() {
			return R38_0_1Y_FT;
		}

		public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
			R38_0_1Y_FT = r38_0_1y_FT;
		}

		public BigDecimal getR38_0_1Y_HTM() {
			return R38_0_1Y_HTM;
		}

		public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
			R38_0_1Y_HTM = r38_0_1y_HTM;
		}

		public BigDecimal getR38_0_1Y_TOTAL() {
			return R38_0_1Y_TOTAL;
		}

		public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
			R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
		}

		public BigDecimal getR38_1_5Y_FT() {
			return R38_1_5Y_FT;
		}

		public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
			R38_1_5Y_FT = r38_1_5y_FT;
		}

		public BigDecimal getR38_1_5Y_HTM() {
			return R38_1_5Y_HTM;
		}

		public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
			R38_1_5Y_HTM = r38_1_5y_HTM;
		}

		public BigDecimal getR38_1_5Y_TOTAL() {
			return R38_1_5Y_TOTAL;
		}

		public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
			R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
		}

		public BigDecimal getR38_O5Y_FT() {
			return R38_O5Y_FT;
		}

		public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
			R38_O5Y_FT = r38_O5Y_FT;
		}

		public BigDecimal getR38_O5Y_HTM() {
			return R38_O5Y_HTM;
		}

		public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
			R38_O5Y_HTM = r38_O5Y_HTM;
		}

		public BigDecimal getR38_O5Y_TOTAL() {
			return R38_O5Y_TOTAL;
		}

		public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
			R38_O5Y_TOTAL = r38_O5Y_TOTAL;
		}

		public BigDecimal getR38_T_FT() {
			return R38_T_FT;
		}

		public void setR38_T_FT(BigDecimal r38_T_FT) {
			R38_T_FT = r38_T_FT;
		}

		public BigDecimal getR38_T_HTM() {
			return R38_T_HTM;
		}

		public void setR38_T_HTM(BigDecimal r38_T_HTM) {
			R38_T_HTM = r38_T_HTM;
		}

		public BigDecimal getR38_T_TOTAL() {
			return R38_T_TOTAL;
		}

		public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
			R38_T_TOTAL = r38_T_TOTAL;
		}

		// Getters and Setters for R39
		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_0_1Y_FT() {
			return R39_0_1Y_FT;
		}

		public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
			R39_0_1Y_FT = r39_0_1y_FT;
		}

		public BigDecimal getR39_0_1Y_HTM() {
			return R39_0_1Y_HTM;
		}

		public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
			R39_0_1Y_HTM = r39_0_1y_HTM;
		}

		public BigDecimal getR39_0_1Y_TOTAL() {
			return R39_0_1Y_TOTAL;
		}

		public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
			R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
		}

		public BigDecimal getR39_1_5Y_FT() {
			return R39_1_5Y_FT;
		}

		public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
			R39_1_5Y_FT = r39_1_5y_FT;
		}

		public BigDecimal getR39_1_5Y_HTM() {
			return R39_1_5Y_HTM;
		}

		public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
			R39_1_5Y_HTM = r39_1_5y_HTM;
		}

		public BigDecimal getR39_1_5Y_TOTAL() {
			return R39_1_5Y_TOTAL;
		}

		public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
			R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
		}

		public BigDecimal getR39_O5Y_FT() {
			return R39_O5Y_FT;
		}

		public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
			R39_O5Y_FT = r39_O5Y_FT;
		}

		public BigDecimal getR39_O5Y_HTM() {
			return R39_O5Y_HTM;
		}

		public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
			R39_O5Y_HTM = r39_O5Y_HTM;
		}

		public BigDecimal getR39_O5Y_TOTAL() {
			return R39_O5Y_TOTAL;
		}

		public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
			R39_O5Y_TOTAL = r39_O5Y_TOTAL;
		}

		public BigDecimal getR39_T_FT() {
			return R39_T_FT;
		}

		public void setR39_T_FT(BigDecimal r39_T_FT) {
			R39_T_FT = r39_T_FT;
		}

		public BigDecimal getR39_T_HTM() {
			return R39_T_HTM;
		}

		public void setR39_T_HTM(BigDecimal r39_T_HTM) {
			R39_T_HTM = r39_T_HTM;
		}

		public BigDecimal getR39_T_TOTAL() {
			return R39_T_TOTAL;
		}

		public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
			R39_T_TOTAL = r39_T_TOTAL;
		}

		// Getters and Setters for R40
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_0_1Y_FT() {
			return R40_0_1Y_FT;
		}

		public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
			R40_0_1Y_FT = r40_0_1y_FT;
		}

		public BigDecimal getR40_0_1Y_HTM() {
			return R40_0_1Y_HTM;
		}

		public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
			R40_0_1Y_HTM = r40_0_1y_HTM;
		}

		public BigDecimal getR40_0_1Y_TOTAL() {
			return R40_0_1Y_TOTAL;
		}

		public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
			R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
		}

		public BigDecimal getR40_1_5Y_FT() {
			return R40_1_5Y_FT;
		}

		public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
			R40_1_5Y_FT = r40_1_5y_FT;
		}

		public BigDecimal getR40_1_5Y_HTM() {
			return R40_1_5Y_HTM;
		}

		public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
			R40_1_5Y_HTM = r40_1_5y_HTM;
		}

		public BigDecimal getR40_1_5Y_TOTAL() {
			return R40_1_5Y_TOTAL;
		}

		public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
			R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
		}

		public BigDecimal getR40_O5Y_FT() {
			return R40_O5Y_FT;
		}

		public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
			R40_O5Y_FT = r40_O5Y_FT;
		}

		public BigDecimal getR40_O5Y_HTM() {
			return R40_O5Y_HTM;
		}

		public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
			R40_O5Y_HTM = r40_O5Y_HTM;
		}

		public BigDecimal getR40_O5Y_TOTAL() {
			return R40_O5Y_TOTAL;
		}

		public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
			R40_O5Y_TOTAL = r40_O5Y_TOTAL;
		}

		public BigDecimal getR40_T_FT() {
			return R40_T_FT;
		}

		public void setR40_T_FT(BigDecimal r40_T_FT) {
			R40_T_FT = r40_T_FT;
		}

		public BigDecimal getR40_T_HTM() {
			return R40_T_HTM;
		}

		public void setR40_T_HTM(BigDecimal r40_T_HTM) {
			R40_T_HTM = r40_T_HTM;
		}

		public BigDecimal getR40_T_TOTAL() {
			return R40_T_TOTAL;
		}

		public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
			R40_T_TOTAL = r40_T_TOTAL;
		}

		// Getters and Setters for R41
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_0_1Y_FT() {
			return R41_0_1Y_FT;
		}

		public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
			R41_0_1Y_FT = r41_0_1y_FT;
		}

		public BigDecimal getR41_0_1Y_HTM() {
			return R41_0_1Y_HTM;
		}

		public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
			R41_0_1Y_HTM = r41_0_1y_HTM;
		}

		public BigDecimal getR41_0_1Y_TOTAL() {
			return R41_0_1Y_TOTAL;
		}

		public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
			R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
		}

		public BigDecimal getR41_1_5Y_FT() {
			return R41_1_5Y_FT;
		}

		public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
			R41_1_5Y_FT = r41_1_5y_FT;
		}

		public BigDecimal getR41_1_5Y_HTM() {
			return R41_1_5Y_HTM;
		}

		public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
			R41_1_5Y_HTM = r41_1_5y_HTM;
		}

		public BigDecimal getR41_1_5Y_TOTAL() {
			return R41_1_5Y_TOTAL;
		}

		public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
			R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
		}

		public BigDecimal getR41_O5Y_FT() {
			return R41_O5Y_FT;
		}

		public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
			R41_O5Y_FT = r41_O5Y_FT;
		}

		public BigDecimal getR41_O5Y_HTM() {
			return R41_O5Y_HTM;
		}

		public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
			R41_O5Y_HTM = r41_O5Y_HTM;
		}

		public BigDecimal getR41_O5Y_TOTAL() {
			return R41_O5Y_TOTAL;
		}

		public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
			R41_O5Y_TOTAL = r41_O5Y_TOTAL;
		}

		public BigDecimal getR41_T_FT() {
			return R41_T_FT;
		}

		public void setR41_T_FT(BigDecimal r41_T_FT) {
			R41_T_FT = r41_T_FT;
		}

		public BigDecimal getR41_T_HTM() {
			return R41_T_HTM;
		}

		public void setR41_T_HTM(BigDecimal r41_T_HTM) {
			R41_T_HTM = r41_T_HTM;
		}

		public BigDecimal getR41_T_TOTAL() {
			return R41_T_TOTAL;
		}

		public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
			R41_T_TOTAL = r41_T_TOTAL;
		}

		// Getters and Setters for R42
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_0_1Y_FT() {
			return R42_0_1Y_FT;
		}

		public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
			R42_0_1Y_FT = r42_0_1y_FT;
		}

		public BigDecimal getR42_0_1Y_HTM() {
			return R42_0_1Y_HTM;
		}

		public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
			R42_0_1Y_HTM = r42_0_1y_HTM;
		}

		public BigDecimal getR42_0_1Y_TOTAL() {
			return R42_0_1Y_TOTAL;
		}

		public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
			R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
		}

		public BigDecimal getR42_1_5Y_FT() {
			return R42_1_5Y_FT;
		}

		public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
			R42_1_5Y_FT = r42_1_5y_FT;
		}

		public BigDecimal getR42_1_5Y_HTM() {
			return R42_1_5Y_HTM;
		}

		public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
			R42_1_5Y_HTM = r42_1_5y_HTM;
		}

		public BigDecimal getR42_1_5Y_TOTAL() {
			return R42_1_5Y_TOTAL;
		}

		public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
			R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
		}

		public BigDecimal getR42_O5Y_FT() {
			return R42_O5Y_FT;
		}

		public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
			R42_O5Y_FT = r42_O5Y_FT;
		}

		public BigDecimal getR42_O5Y_HTM() {
			return R42_O5Y_HTM;
		}

		public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
			R42_O5Y_HTM = r42_O5Y_HTM;
		}

		public BigDecimal getR42_O5Y_TOTAL() {
			return R42_O5Y_TOTAL;
		}

		public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
			R42_O5Y_TOTAL = r42_O5Y_TOTAL;
		}

		public BigDecimal getR42_T_FT() {
			return R42_T_FT;
		}

		public void setR42_T_FT(BigDecimal r42_T_FT) {
			R42_T_FT = r42_T_FT;
		}

		public BigDecimal getR42_T_HTM() {
			return R42_T_HTM;
		}

		public void setR42_T_HTM(BigDecimal r42_T_HTM) {
			R42_T_HTM = r42_T_HTM;
		}

		public BigDecimal getR42_T_TOTAL() {
			return R42_T_TOTAL;
		}

		public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
			R42_T_TOTAL = r42_T_TOTAL;
		}

		// Getters and Setters for R43
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public BigDecimal getR43_0_1Y_FT() {
			return R43_0_1Y_FT;
		}

		public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
			R43_0_1Y_FT = r43_0_1y_FT;
		}

		public BigDecimal getR43_0_1Y_HTM() {
			return R43_0_1Y_HTM;
		}

		public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
			R43_0_1Y_HTM = r43_0_1y_HTM;
		}

		public BigDecimal getR43_0_1Y_TOTAL() {
			return R43_0_1Y_TOTAL;
		}

		public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
			R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
		}

		public BigDecimal getR43_1_5Y_FT() {
			return R43_1_5Y_FT;
		}

		public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
			R43_1_5Y_FT = r43_1_5y_FT;
		}

		public BigDecimal getR43_1_5Y_HTM() {
			return R43_1_5Y_HTM;
		}

		public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
			R43_1_5Y_HTM = r43_1_5y_HTM;
		}

		public BigDecimal getR43_1_5Y_TOTAL() {
			return R43_1_5Y_TOTAL;
		}

		public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
			R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
		}

		public BigDecimal getR43_O5Y_FT() {
			return R43_O5Y_FT;
		}

		public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
			R43_O5Y_FT = r43_O5Y_FT;
		}

		public BigDecimal getR43_O5Y_HTM() {
			return R43_O5Y_HTM;
		}

		public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
			R43_O5Y_HTM = r43_O5Y_HTM;
		}

		public BigDecimal getR43_O5Y_TOTAL() {
			return R43_O5Y_TOTAL;
		}

		public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
			R43_O5Y_TOTAL = r43_O5Y_TOTAL;
		}

		public BigDecimal getR43_T_FT() {
			return R43_T_FT;
		}

		public void setR43_T_FT(BigDecimal r43_T_FT) {
			R43_T_FT = r43_T_FT;
		}

		public BigDecimal getR43_T_HTM() {
			return R43_T_HTM;
		}

		public void setR43_T_HTM(BigDecimal r43_T_HTM) {
			R43_T_HTM = r43_T_HTM;
		}

		public BigDecimal getR43_T_TOTAL() {
			return R43_T_TOTAL;
		}

		public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
			R43_T_TOTAL = r43_T_TOTAL;
		}

		// Common Fields Getters and Setters
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
	}

//===========================
//BRRS_M_SEC_Archival_Summary_RowMapper1
//===========================

	public class BRRS_M_SEC_Archival_Summary_RowMapper1 implements RowMapper<BRRS_M_SEC_Archival_Summary_Entity1> {

		@Override
		public BRRS_M_SEC_Archival_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Archival_Summary_Entity1 obj = new BRRS_M_SEC_Archival_Summary_Entity1();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA(rs.getBigDecimal("R11_TCA"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA(rs.getBigDecimal("R12_TCA"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA(rs.getBigDecimal("R13_TCA"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA(rs.getBigDecimal("R14_TCA"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA(rs.getBigDecimal("R15_TCA"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA(rs.getBigDecimal("R16_TCA"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TCA(rs.getBigDecimal("R17_TCA"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TCA(rs.getBigDecimal("R18_TCA"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TCA(rs.getBigDecimal("R19_TCA"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Entity1
//===========================

	public class BRRS_M_SEC_Archival_Summary_Entity1 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA;
		private String R17_PRODUCT;
		private BigDecimal R17_TCA;
		private String R18_PRODUCT;
		private BigDecimal R18_TCA;
		private String R19_PRODUCT;
		private BigDecimal R19_TCA;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BRRS_M_SEC_Archival_Summary_Entity1() {
			super();
		}

		// Getters and Setters for R11
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA() {
			return R11_TCA;
		}

		public void setR11_TCA(BigDecimal r11_TCA) {
			R11_TCA = r11_TCA;
		}

		// Getters and Setters for R12
		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA() {
			return R12_TCA;
		}

		public void setR12_TCA(BigDecimal r12_TCA) {
			R12_TCA = r12_TCA;
		}

		// Getters and Setters for R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA() {
			return R13_TCA;
		}

		public void setR13_TCA(BigDecimal r13_TCA) {
			R13_TCA = r13_TCA;
		}

		// Getters and Setters for R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA() {
			return R14_TCA;
		}

		public void setR14_TCA(BigDecimal r14_TCA) {
			R14_TCA = r14_TCA;
		}

		// Getters and Setters for R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA() {
			return R15_TCA;
		}

		public void setR15_TCA(BigDecimal r15_TCA) {
			R15_TCA = r15_TCA;
		}

		// Getters and Setters for R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA() {
			return R16_TCA;
		}

		public void setR16_TCA(BigDecimal r16_TCA) {
			R16_TCA = r16_TCA;
		}

		// Getters and Setters for R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TCA() {
			return R17_TCA;
		}

		public void setR17_TCA(BigDecimal r17_TCA) {
			R17_TCA = r17_TCA;
		}

		// Getters and Setters for R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_TCA() {
			return R18_TCA;
		}

		public void setR18_TCA(BigDecimal r18_TCA) {
			R18_TCA = r18_TCA;
		}

		// Getters and Setters for R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_TCA() {
			return R19_TCA;
		}

		public void setR19_TCA(BigDecimal r19_TCA) {
			R19_TCA = r19_TCA;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//BRRS_M_SEC_Archival_Summary_RowMapper2
//===========================

	public class BRRS_M_SEC_Archival_Summary_RowMapper2 implements RowMapper<BRRS_M_SEC_Archival_Summary_Entity2> {

		@Override
		public BRRS_M_SEC_Archival_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Archival_Summary_Entity2 obj = new BRRS_M_SEC_Archival_Summary_Entity2();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA2(rs.getBigDecimal("R11_TCA2"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA2(rs.getBigDecimal("R12_TCA2"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA2(rs.getBigDecimal("R13_TCA2"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA2(rs.getBigDecimal("R14_TCA2"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA2(rs.getBigDecimal("R15_TCA2"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA2(rs.getBigDecimal("R16_TCA2"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Entity2
//===========================

	public class BRRS_M_SEC_Archival_Summary_Entity2 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA2;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA2;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA2;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA2;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA2;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA2;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BRRS_M_SEC_Archival_Summary_Entity2() {
			super();
		}

		// Getters and Setters for R11
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA2() {
			return R11_TCA2;
		}

		public void setR11_TCA2(BigDecimal r11_TCA2) {
			R11_TCA2 = r11_TCA2;
		}

		// Getters and Setters for R12
		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA2() {
			return R12_TCA2;
		}

		public void setR12_TCA2(BigDecimal r12_TCA2) {
			R12_TCA2 = r12_TCA2;
		}

		// Getters and Setters for R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA2() {
			return R13_TCA2;
		}

		public void setR13_TCA2(BigDecimal r13_TCA2) {
			R13_TCA2 = r13_TCA2;
		}

		// Getters and Setters for R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA2() {
			return R14_TCA2;
		}

		public void setR14_TCA2(BigDecimal r14_TCA2) {
			R14_TCA2 = r14_TCA2;
		}

		// Getters and Setters for R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA2() {
			return R15_TCA2;
		}

		public void setR15_TCA2(BigDecimal r15_TCA2) {
			R15_TCA2 = r15_TCA2;
		}

		// Getters and Setters for R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA2() {
			return R16_TCA2;
		}

		public void setR16_TCA2(BigDecimal r16_TCA2) {
			R16_TCA2 = r16_TCA2;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//BRRS_M_SEC_Archival_Summary_RowMapper3
//===========================

	public class BRRS_M_SEC_Archival_Summary_RowMapper3 implements RowMapper<BRRS_M_SEC_Archival_Summary_Entity3> {

		@Override
		public BRRS_M_SEC_Archival_Summary_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Archival_Summary_Entity3 obj = new BRRS_M_SEC_Archival_Summary_Entity3();

			// =========================
			// R26
			// =========================
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_0_1Y_FT(rs.getBigDecimal("R26_0_1Y_FT"));
			obj.setR26_0_1Y_HTM(rs.getBigDecimal("R26_0_1Y_HTM"));
			obj.setR26_0_1Y_TOTAL(rs.getBigDecimal("R26_0_1Y_TOTAL"));
			obj.setR26_1_5Y_FT(rs.getBigDecimal("R26_1_5Y_FT"));
			obj.setR26_1_5Y_HTM(rs.getBigDecimal("R26_1_5Y_HTM"));
			obj.setR26_1_5Y_TOTAL(rs.getBigDecimal("R26_1_5Y_TOTAL"));
			obj.setR26_O5Y_FT(rs.getBigDecimal("R26_O5Y_FT"));
			obj.setR26_O5Y_HTM(rs.getBigDecimal("R26_O5Y_HTM"));
			obj.setR26_O5Y_TOTAL(rs.getBigDecimal("R26_O5Y_TOTAL"));
			obj.setR26_T_FT(rs.getBigDecimal("R26_T_FT"));
			obj.setR26_T_HTM(rs.getBigDecimal("R26_T_HTM"));
			obj.setR26_T_TOTAL(rs.getBigDecimal("R26_T_TOTAL"));

			// =========================
			// R27
			// =========================
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_0_1Y_FT(rs.getBigDecimal("R27_0_1Y_FT"));
			obj.setR27_0_1Y_HTM(rs.getBigDecimal("R27_0_1Y_HTM"));
			obj.setR27_0_1Y_TOTAL(rs.getBigDecimal("R27_0_1Y_TOTAL"));
			obj.setR27_1_5Y_FT(rs.getBigDecimal("R27_1_5Y_FT"));
			obj.setR27_1_5Y_HTM(rs.getBigDecimal("R27_1_5Y_HTM"));
			obj.setR27_1_5Y_TOTAL(rs.getBigDecimal("R27_1_5Y_TOTAL"));
			obj.setR27_O5Y_FT(rs.getBigDecimal("R27_O5Y_FT"));
			obj.setR27_O5Y_HTM(rs.getBigDecimal("R27_O5Y_HTM"));
			obj.setR27_O5Y_TOTAL(rs.getBigDecimal("R27_O5Y_TOTAL"));
			obj.setR27_T_FT(rs.getBigDecimal("R27_T_FT"));
			obj.setR27_T_HTM(rs.getBigDecimal("R27_T_HTM"));
			obj.setR27_T_TOTAL(rs.getBigDecimal("R27_T_TOTAL"));

			// =========================
			// R28
			// =========================
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_0_1Y_FT(rs.getBigDecimal("R28_0_1Y_FT"));
			obj.setR28_0_1Y_HTM(rs.getBigDecimal("R28_0_1Y_HTM"));
			obj.setR28_0_1Y_TOTAL(rs.getBigDecimal("R28_0_1Y_TOTAL"));
			obj.setR28_1_5Y_FT(rs.getBigDecimal("R28_1_5Y_FT"));
			obj.setR28_1_5Y_HTM(rs.getBigDecimal("R28_1_5Y_HTM"));
			obj.setR28_1_5Y_TOTAL(rs.getBigDecimal("R28_1_5Y_TOTAL"));
			obj.setR28_O5Y_FT(rs.getBigDecimal("R28_O5Y_FT"));
			obj.setR28_O5Y_HTM(rs.getBigDecimal("R28_O5Y_HTM"));
			obj.setR28_O5Y_TOTAL(rs.getBigDecimal("R28_O5Y_TOTAL"));
			obj.setR28_T_FT(rs.getBigDecimal("R28_T_FT"));
			obj.setR28_T_HTM(rs.getBigDecimal("R28_T_HTM"));
			obj.setR28_T_TOTAL(rs.getBigDecimal("R28_T_TOTAL"));

			// =========================
			// R29
			// =========================
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_0_1Y_FT(rs.getBigDecimal("R29_0_1Y_FT"));
			obj.setR29_0_1Y_HTM(rs.getBigDecimal("R29_0_1Y_HTM"));
			obj.setR29_0_1Y_TOTAL(rs.getBigDecimal("R29_0_1Y_TOTAL"));
			obj.setR29_1_5Y_FT(rs.getBigDecimal("R29_1_5Y_FT"));
			obj.setR29_1_5Y_HTM(rs.getBigDecimal("R29_1_5Y_HTM"));
			obj.setR29_1_5Y_TOTAL(rs.getBigDecimal("R29_1_5Y_TOTAL"));
			obj.setR29_O5Y_FT(rs.getBigDecimal("R29_O5Y_FT"));
			obj.setR29_O5Y_HTM(rs.getBigDecimal("R29_O5Y_HTM"));
			obj.setR29_O5Y_TOTAL(rs.getBigDecimal("R29_O5Y_TOTAL"));
			obj.setR29_T_FT(rs.getBigDecimal("R29_T_FT"));
			obj.setR29_T_HTM(rs.getBigDecimal("R29_T_HTM"));
			obj.setR29_T_TOTAL(rs.getBigDecimal("R29_T_TOTAL"));

			// =========================
			// R30
			// =========================
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_0_1Y_FT(rs.getBigDecimal("R30_0_1Y_FT"));
			obj.setR30_0_1Y_HTM(rs.getBigDecimal("R30_0_1Y_HTM"));
			obj.setR30_0_1Y_TOTAL(rs.getBigDecimal("R30_0_1Y_TOTAL"));
			obj.setR30_1_5Y_FT(rs.getBigDecimal("R30_1_5Y_FT"));
			obj.setR30_1_5Y_HTM(rs.getBigDecimal("R30_1_5Y_HTM"));
			obj.setR30_1_5Y_TOTAL(rs.getBigDecimal("R30_1_5Y_TOTAL"));
			obj.setR30_O5Y_FT(rs.getBigDecimal("R30_O5Y_FT"));
			obj.setR30_O5Y_HTM(rs.getBigDecimal("R30_O5Y_HTM"));
			obj.setR30_O5Y_TOTAL(rs.getBigDecimal("R30_O5Y_TOTAL"));
			obj.setR30_T_FT(rs.getBigDecimal("R30_T_FT"));
			obj.setR30_T_HTM(rs.getBigDecimal("R30_T_HTM"));
			obj.setR30_T_TOTAL(rs.getBigDecimal("R30_T_TOTAL"));

			// =========================
			// R31
			// =========================
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_0_1Y_FT(rs.getBigDecimal("R31_0_1Y_FT"));
			obj.setR31_0_1Y_HTM(rs.getBigDecimal("R31_0_1Y_HTM"));
			obj.setR31_0_1Y_TOTAL(rs.getBigDecimal("R31_0_1Y_TOTAL"));
			obj.setR31_1_5Y_FT(rs.getBigDecimal("R31_1_5Y_FT"));
			obj.setR31_1_5Y_HTM(rs.getBigDecimal("R31_1_5Y_HTM"));
			obj.setR31_1_5Y_TOTAL(rs.getBigDecimal("R31_1_5Y_TOTAL"));
			obj.setR31_O5Y_FT(rs.getBigDecimal("R31_O5Y_FT"));
			obj.setR31_O5Y_HTM(rs.getBigDecimal("R31_O5Y_HTM"));
			obj.setR31_O5Y_TOTAL(rs.getBigDecimal("R31_O5Y_TOTAL"));
			obj.setR31_T_FT(rs.getBigDecimal("R31_T_FT"));
			obj.setR31_T_HTM(rs.getBigDecimal("R31_T_HTM"));
			obj.setR31_T_TOTAL(rs.getBigDecimal("R31_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Entity3
//===========================

	public class BRRS_M_SEC_Archival_Summary_Entity3 {

		private String R26_PRODUCT;
		private BigDecimal R26_0_1Y_FT;
		private BigDecimal R26_0_1Y_HTM;
		private BigDecimal R26_0_1Y_TOTAL;
		private BigDecimal R26_1_5Y_FT;
		private BigDecimal R26_1_5Y_HTM;
		private BigDecimal R26_1_5Y_TOTAL;
		private BigDecimal R26_O5Y_FT;
		private BigDecimal R26_O5Y_HTM;
		private BigDecimal R26_O5Y_TOTAL;
		private BigDecimal R26_T_FT;
		private BigDecimal R26_T_HTM;
		private BigDecimal R26_T_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_0_1Y_FT;
		private BigDecimal R27_0_1Y_HTM;
		private BigDecimal R27_0_1Y_TOTAL;
		private BigDecimal R27_1_5Y_FT;
		private BigDecimal R27_1_5Y_HTM;
		private BigDecimal R27_1_5Y_TOTAL;
		private BigDecimal R27_O5Y_FT;
		private BigDecimal R27_O5Y_HTM;
		private BigDecimal R27_O5Y_TOTAL;
		private BigDecimal R27_T_FT;
		private BigDecimal R27_T_HTM;
		private BigDecimal R27_T_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_0_1Y_FT;
		private BigDecimal R28_0_1Y_HTM;
		private BigDecimal R28_0_1Y_TOTAL;
		private BigDecimal R28_1_5Y_FT;
		private BigDecimal R28_1_5Y_HTM;
		private BigDecimal R28_1_5Y_TOTAL;
		private BigDecimal R28_O5Y_FT;
		private BigDecimal R28_O5Y_HTM;
		private BigDecimal R28_O5Y_TOTAL;
		private BigDecimal R28_T_FT;
		private BigDecimal R28_T_HTM;
		private BigDecimal R28_T_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_0_1Y_FT;
		private BigDecimal R29_0_1Y_HTM;
		private BigDecimal R29_0_1Y_TOTAL;
		private BigDecimal R29_1_5Y_FT;
		private BigDecimal R29_1_5Y_HTM;
		private BigDecimal R29_1_5Y_TOTAL;
		private BigDecimal R29_O5Y_FT;
		private BigDecimal R29_O5Y_HTM;
		private BigDecimal R29_O5Y_TOTAL;
		private BigDecimal R29_T_FT;
		private BigDecimal R29_T_HTM;
		private BigDecimal R29_T_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_0_1Y_FT;
		private BigDecimal R30_0_1Y_HTM;
		private BigDecimal R30_0_1Y_TOTAL;
		private BigDecimal R30_1_5Y_FT;
		private BigDecimal R30_1_5Y_HTM;
		private BigDecimal R30_1_5Y_TOTAL;
		private BigDecimal R30_O5Y_FT;
		private BigDecimal R30_O5Y_HTM;
		private BigDecimal R30_O5Y_TOTAL;
		private BigDecimal R30_T_FT;
		private BigDecimal R30_T_HTM;
		private BigDecimal R30_T_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_0_1Y_FT;
		private BigDecimal R31_0_1Y_HTM;
		private BigDecimal R31_0_1Y_TOTAL;
		private BigDecimal R31_1_5Y_FT;
		private BigDecimal R31_1_5Y_HTM;
		private BigDecimal R31_1_5Y_TOTAL;
		private BigDecimal R31_O5Y_FT;
		private BigDecimal R31_O5Y_HTM;
		private BigDecimal R31_O5Y_TOTAL;
		private BigDecimal R31_T_FT;
		private BigDecimal R31_T_HTM;
		private BigDecimal R31_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BRRS_M_SEC_Archival_Summary_Entity3() {
			super();
		}

		// Getters and Setters for R26
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}

		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}

		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}

		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}

		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}

		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}

		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}

		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}

		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}

		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}

		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}

		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}

		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}

		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}

		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}

		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}

		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}

		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}

		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}

		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}

		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}

		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}

		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}

		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}

		// Getters and Setters for R27
		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}

		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}

		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}

		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}

		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}

		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}

		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}

		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}

		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}

		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}

		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}

		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}

		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}

		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}

		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}

		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}

		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}

		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}

		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}

		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}

		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}

		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}

		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}

		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}

		// Getters and Setters for R28
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}

		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}

		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}

		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}

		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}

		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}

		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}

		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}

		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}

		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}

		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}

		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}

		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}

		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}

		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}

		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}

		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}

		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}

		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}

		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}

		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}

		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}

		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}

		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}

		// Getters and Setters for R29
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}

		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}

		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}

		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}

		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}

		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}

		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}

		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}

		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}

		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}

		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}

		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}

		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}

		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}

		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}

		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}

		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}

		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}

		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}

		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}

		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}

		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}

		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}

		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}

		// Getters and Setters for R30
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}

		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}

		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}

		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}

		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}

		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}

		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}

		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}

		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}

		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}

		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}

		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}

		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}

		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}

		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}

		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}

		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}

		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}

		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}

		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}

		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}

		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}

		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}

		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}

		// Getters and Setters for R31
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}

		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}

		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}

		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}

		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}

		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}

		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}

		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}

		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}

		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}

		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}

		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}

		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}

		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}

		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}

		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}

		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}

		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}

		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}

		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}

		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}

		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}

		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}

		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//BRRS_M_SEC_Archival_Summary_RowMapper4
//===========================

	public class BRRS_M_SEC_Archival_Summary_RowMapper4 implements RowMapper<BRRS_M_SEC_Archival_Summary_Entity4> {

		@Override
		public BRRS_M_SEC_Archival_Summary_Entity4 mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_M_SEC_Archival_Summary_Entity4 obj = new BRRS_M_SEC_Archival_Summary_Entity4();

			// =========================
			// R36
			// =========================
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_0_1Y_FT(rs.getBigDecimal("R36_0_1Y_FT"));
			obj.setR36_0_1Y_HTM(rs.getBigDecimal("R36_0_1Y_HTM"));
			obj.setR36_0_1Y_TOTAL(rs.getBigDecimal("R36_0_1Y_TOTAL"));
			obj.setR36_1_5Y_FT(rs.getBigDecimal("R36_1_5Y_FT"));
			obj.setR36_1_5Y_HTM(rs.getBigDecimal("R36_1_5Y_HTM"));
			obj.setR36_1_5Y_TOTAL(rs.getBigDecimal("R36_1_5Y_TOTAL"));
			obj.setR36_O5Y_FT(rs.getBigDecimal("R36_O5Y_FT"));
			obj.setR36_O5Y_HTM(rs.getBigDecimal("R36_O5Y_HTM"));
			obj.setR36_O5Y_TOTAL(rs.getBigDecimal("R36_O5Y_TOTAL"));
			obj.setR36_T_FT(rs.getBigDecimal("R36_T_FT"));
			obj.setR36_T_HTM(rs.getBigDecimal("R36_T_HTM"));
			obj.setR36_T_TOTAL(rs.getBigDecimal("R36_T_TOTAL"));

			// =========================
			// R37
			// =========================
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_0_1Y_FT(rs.getBigDecimal("R37_0_1Y_FT"));
			obj.setR37_0_1Y_HTM(rs.getBigDecimal("R37_0_1Y_HTM"));
			obj.setR37_0_1Y_TOTAL(rs.getBigDecimal("R37_0_1Y_TOTAL"));
			obj.setR37_1_5Y_FT(rs.getBigDecimal("R37_1_5Y_FT"));
			obj.setR37_1_5Y_HTM(rs.getBigDecimal("R37_1_5Y_HTM"));
			obj.setR37_1_5Y_TOTAL(rs.getBigDecimal("R37_1_5Y_TOTAL"));
			obj.setR37_O5Y_FT(rs.getBigDecimal("R37_O5Y_FT"));
			obj.setR37_O5Y_HTM(rs.getBigDecimal("R37_O5Y_HTM"));
			obj.setR37_O5Y_TOTAL(rs.getBigDecimal("R37_O5Y_TOTAL"));
			obj.setR37_T_FT(rs.getBigDecimal("R37_T_FT"));
			obj.setR37_T_HTM(rs.getBigDecimal("R37_T_HTM"));
			obj.setR37_T_TOTAL(rs.getBigDecimal("R37_T_TOTAL"));

			// =========================
			// R38
			// =========================
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_0_1Y_FT(rs.getBigDecimal("R38_0_1Y_FT"));
			obj.setR38_0_1Y_HTM(rs.getBigDecimal("R38_0_1Y_HTM"));
			obj.setR38_0_1Y_TOTAL(rs.getBigDecimal("R38_0_1Y_TOTAL"));
			obj.setR38_1_5Y_FT(rs.getBigDecimal("R38_1_5Y_FT"));
			obj.setR38_1_5Y_HTM(rs.getBigDecimal("R38_1_5Y_HTM"));
			obj.setR38_1_5Y_TOTAL(rs.getBigDecimal("R38_1_5Y_TOTAL"));
			obj.setR38_O5Y_FT(rs.getBigDecimal("R38_O5Y_FT"));
			obj.setR38_O5Y_HTM(rs.getBigDecimal("R38_O5Y_HTM"));
			obj.setR38_O5Y_TOTAL(rs.getBigDecimal("R38_O5Y_TOTAL"));
			obj.setR38_T_FT(rs.getBigDecimal("R38_T_FT"));
			obj.setR38_T_HTM(rs.getBigDecimal("R38_T_HTM"));
			obj.setR38_T_TOTAL(rs.getBigDecimal("R38_T_TOTAL"));

			// =========================
			// R39
			// =========================
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_0_1Y_FT(rs.getBigDecimal("R39_0_1Y_FT"));
			obj.setR39_0_1Y_HTM(rs.getBigDecimal("R39_0_1Y_HTM"));
			obj.setR39_0_1Y_TOTAL(rs.getBigDecimal("R39_0_1Y_TOTAL"));
			obj.setR39_1_5Y_FT(rs.getBigDecimal("R39_1_5Y_FT"));
			obj.setR39_1_5Y_HTM(rs.getBigDecimal("R39_1_5Y_HTM"));
			obj.setR39_1_5Y_TOTAL(rs.getBigDecimal("R39_1_5Y_TOTAL"));
			obj.setR39_O5Y_FT(rs.getBigDecimal("R39_O5Y_FT"));
			obj.setR39_O5Y_HTM(rs.getBigDecimal("R39_O5Y_HTM"));
			obj.setR39_O5Y_TOTAL(rs.getBigDecimal("R39_O5Y_TOTAL"));
			obj.setR39_T_FT(rs.getBigDecimal("R39_T_FT"));
			obj.setR39_T_HTM(rs.getBigDecimal("R39_T_HTM"));
			obj.setR39_T_TOTAL(rs.getBigDecimal("R39_T_TOTAL"));

			// =========================
			// R40
			// =========================
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_0_1Y_FT(rs.getBigDecimal("R40_0_1Y_FT"));
			obj.setR40_0_1Y_HTM(rs.getBigDecimal("R40_0_1Y_HTM"));
			obj.setR40_0_1Y_TOTAL(rs.getBigDecimal("R40_0_1Y_TOTAL"));
			obj.setR40_1_5Y_FT(rs.getBigDecimal("R40_1_5Y_FT"));
			obj.setR40_1_5Y_HTM(rs.getBigDecimal("R40_1_5Y_HTM"));
			obj.setR40_1_5Y_TOTAL(rs.getBigDecimal("R40_1_5Y_TOTAL"));
			obj.setR40_O5Y_FT(rs.getBigDecimal("R40_O5Y_FT"));
			obj.setR40_O5Y_HTM(rs.getBigDecimal("R40_O5Y_HTM"));
			obj.setR40_O5Y_TOTAL(rs.getBigDecimal("R40_O5Y_TOTAL"));
			obj.setR40_T_FT(rs.getBigDecimal("R40_T_FT"));
			obj.setR40_T_HTM(rs.getBigDecimal("R40_T_HTM"));
			obj.setR40_T_TOTAL(rs.getBigDecimal("R40_T_TOTAL"));

			// =========================
			// R41
			// =========================
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_0_1Y_FT(rs.getBigDecimal("R41_0_1Y_FT"));
			obj.setR41_0_1Y_HTM(rs.getBigDecimal("R41_0_1Y_HTM"));
			obj.setR41_0_1Y_TOTAL(rs.getBigDecimal("R41_0_1Y_TOTAL"));
			obj.setR41_1_5Y_FT(rs.getBigDecimal("R41_1_5Y_FT"));
			obj.setR41_1_5Y_HTM(rs.getBigDecimal("R41_1_5Y_HTM"));
			obj.setR41_1_5Y_TOTAL(rs.getBigDecimal("R41_1_5Y_TOTAL"));
			obj.setR41_O5Y_FT(rs.getBigDecimal("R41_O5Y_FT"));
			obj.setR41_O5Y_HTM(rs.getBigDecimal("R41_O5Y_HTM"));
			obj.setR41_O5Y_TOTAL(rs.getBigDecimal("R41_O5Y_TOTAL"));
			obj.setR41_T_FT(rs.getBigDecimal("R41_T_FT"));
			obj.setR41_T_HTM(rs.getBigDecimal("R41_T_HTM"));
			obj.setR41_T_TOTAL(rs.getBigDecimal("R41_T_TOTAL"));

			// =========================
			// R42
			// =========================
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_0_1Y_FT(rs.getBigDecimal("R42_0_1Y_FT"));
			obj.setR42_0_1Y_HTM(rs.getBigDecimal("R42_0_1Y_HTM"));
			obj.setR42_0_1Y_TOTAL(rs.getBigDecimal("R42_0_1Y_TOTAL"));
			obj.setR42_1_5Y_FT(rs.getBigDecimal("R42_1_5Y_FT"));
			obj.setR42_1_5Y_HTM(rs.getBigDecimal("R42_1_5Y_HTM"));
			obj.setR42_1_5Y_TOTAL(rs.getBigDecimal("R42_1_5Y_TOTAL"));
			obj.setR42_O5Y_FT(rs.getBigDecimal("R42_O5Y_FT"));
			obj.setR42_O5Y_HTM(rs.getBigDecimal("R42_O5Y_HTM"));
			obj.setR42_O5Y_TOTAL(rs.getBigDecimal("R42_O5Y_TOTAL"));
			obj.setR42_T_FT(rs.getBigDecimal("R42_T_FT"));
			obj.setR42_T_HTM(rs.getBigDecimal("R42_T_HTM"));
			obj.setR42_T_TOTAL(rs.getBigDecimal("R42_T_TOTAL"));

			// =========================
			// R43
			// =========================
			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
			obj.setR43_0_1Y_FT(rs.getBigDecimal("R43_0_1Y_FT"));
			obj.setR43_0_1Y_HTM(rs.getBigDecimal("R43_0_1Y_HTM"));
			obj.setR43_0_1Y_TOTAL(rs.getBigDecimal("R43_0_1Y_TOTAL"));
			obj.setR43_1_5Y_FT(rs.getBigDecimal("R43_1_5Y_FT"));
			obj.setR43_1_5Y_HTM(rs.getBigDecimal("R43_1_5Y_HTM"));
			obj.setR43_1_5Y_TOTAL(rs.getBigDecimal("R43_1_5Y_TOTAL"));
			obj.setR43_O5Y_FT(rs.getBigDecimal("R43_O5Y_FT"));
			obj.setR43_O5Y_HTM(rs.getBigDecimal("R43_O5Y_HTM"));
			obj.setR43_O5Y_TOTAL(rs.getBigDecimal("R43_O5Y_TOTAL"));
			obj.setR43_T_FT(rs.getBigDecimal("R43_T_FT"));
			obj.setR43_T_HTM(rs.getBigDecimal("R43_T_HTM"));
			obj.setR43_T_TOTAL(rs.getBigDecimal("R43_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//BRRS_M_SEC_Archival_Summary_Entity4
//===========================

	public class BRRS_M_SEC_Archival_Summary_Entity4 {

		private String R36_PRODUCT;
		private BigDecimal R36_0_1Y_FT;
		private BigDecimal R36_0_1Y_HTM;
		private BigDecimal R36_0_1Y_TOTAL;
		private BigDecimal R36_1_5Y_FT;
		private BigDecimal R36_1_5Y_HTM;
		private BigDecimal R36_1_5Y_TOTAL;
		private BigDecimal R36_O5Y_FT;
		private BigDecimal R36_O5Y_HTM;
		private BigDecimal R36_O5Y_TOTAL;
		private BigDecimal R36_T_FT;
		private BigDecimal R36_T_HTM;
		private BigDecimal R36_T_TOTAL;

		private String R37_PRODUCT;
		private BigDecimal R37_0_1Y_FT;
		private BigDecimal R37_0_1Y_HTM;
		private BigDecimal R37_0_1Y_TOTAL;
		private BigDecimal R37_1_5Y_FT;
		private BigDecimal R37_1_5Y_HTM;
		private BigDecimal R37_1_5Y_TOTAL;
		private BigDecimal R37_O5Y_FT;
		private BigDecimal R37_O5Y_HTM;
		private BigDecimal R37_O5Y_TOTAL;
		private BigDecimal R37_T_FT;
		private BigDecimal R37_T_HTM;
		private BigDecimal R37_T_TOTAL;

		private String R38_PRODUCT;
		private BigDecimal R38_0_1Y_FT;
		private BigDecimal R38_0_1Y_HTM;
		private BigDecimal R38_0_1Y_TOTAL;
		private BigDecimal R38_1_5Y_FT;
		private BigDecimal R38_1_5Y_HTM;
		private BigDecimal R38_1_5Y_TOTAL;
		private BigDecimal R38_O5Y_FT;
		private BigDecimal R38_O5Y_HTM;
		private BigDecimal R38_O5Y_TOTAL;
		private BigDecimal R38_T_FT;
		private BigDecimal R38_T_HTM;
		private BigDecimal R38_T_TOTAL;

		private String R39_PRODUCT;
		private BigDecimal R39_0_1Y_FT;
		private BigDecimal R39_0_1Y_HTM;
		private BigDecimal R39_0_1Y_TOTAL;
		private BigDecimal R39_1_5Y_FT;
		private BigDecimal R39_1_5Y_HTM;
		private BigDecimal R39_1_5Y_TOTAL;
		private BigDecimal R39_O5Y_FT;
		private BigDecimal R39_O5Y_HTM;
		private BigDecimal R39_O5Y_TOTAL;
		private BigDecimal R39_T_FT;
		private BigDecimal R39_T_HTM;
		private BigDecimal R39_T_TOTAL;

		private String R40_PRODUCT;
		private BigDecimal R40_0_1Y_FT;
		private BigDecimal R40_0_1Y_HTM;
		private BigDecimal R40_0_1Y_TOTAL;
		private BigDecimal R40_1_5Y_FT;
		private BigDecimal R40_1_5Y_HTM;
		private BigDecimal R40_1_5Y_TOTAL;
		private BigDecimal R40_O5Y_FT;
		private BigDecimal R40_O5Y_HTM;
		private BigDecimal R40_O5Y_TOTAL;
		private BigDecimal R40_T_FT;
		private BigDecimal R40_T_HTM;
		private BigDecimal R40_T_TOTAL;

		private String R41_PRODUCT;
		private BigDecimal R41_0_1Y_FT;
		private BigDecimal R41_0_1Y_HTM;
		private BigDecimal R41_0_1Y_TOTAL;
		private BigDecimal R41_1_5Y_FT;
		private BigDecimal R41_1_5Y_HTM;
		private BigDecimal R41_1_5Y_TOTAL;
		private BigDecimal R41_O5Y_FT;
		private BigDecimal R41_O5Y_HTM;
		private BigDecimal R41_O5Y_TOTAL;
		private BigDecimal R41_T_FT;
		private BigDecimal R41_T_HTM;
		private BigDecimal R41_T_TOTAL;

		private String R42_PRODUCT;
		private BigDecimal R42_0_1Y_FT;
		private BigDecimal R42_0_1Y_HTM;
		private BigDecimal R42_0_1Y_TOTAL;
		private BigDecimal R42_1_5Y_FT;
		private BigDecimal R42_1_5Y_HTM;
		private BigDecimal R42_1_5Y_TOTAL;
		private BigDecimal R42_O5Y_FT;
		private BigDecimal R42_O5Y_HTM;
		private BigDecimal R42_O5Y_TOTAL;
		private BigDecimal R42_T_FT;
		private BigDecimal R42_T_HTM;
		private BigDecimal R42_T_TOTAL;

		private String R43_PRODUCT;
		private BigDecimal R43_0_1Y_FT;
		private BigDecimal R43_0_1Y_HTM;
		private BigDecimal R43_0_1Y_TOTAL;
		private BigDecimal R43_1_5Y_FT;
		private BigDecimal R43_1_5Y_HTM;
		private BigDecimal R43_1_5Y_TOTAL;
		private BigDecimal R43_O5Y_FT;
		private BigDecimal R43_O5Y_HTM;
		private BigDecimal R43_O5Y_TOTAL;
		private BigDecimal R43_T_FT;
		private BigDecimal R43_T_HTM;
		private BigDecimal R43_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BRRS_M_SEC_Archival_Summary_Entity4() {
			super();
		}

		// Getters and Setters for R36
		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_0_1Y_FT() {
			return R36_0_1Y_FT;
		}

		public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
			R36_0_1Y_FT = r36_0_1y_FT;
		}

		public BigDecimal getR36_0_1Y_HTM() {
			return R36_0_1Y_HTM;
		}

		public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
			R36_0_1Y_HTM = r36_0_1y_HTM;
		}

		public BigDecimal getR36_0_1Y_TOTAL() {
			return R36_0_1Y_TOTAL;
		}

		public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
			R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
		}

		public BigDecimal getR36_1_5Y_FT() {
			return R36_1_5Y_FT;
		}

		public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
			R36_1_5Y_FT = r36_1_5y_FT;
		}

		public BigDecimal getR36_1_5Y_HTM() {
			return R36_1_5Y_HTM;
		}

		public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
			R36_1_5Y_HTM = r36_1_5y_HTM;
		}

		public BigDecimal getR36_1_5Y_TOTAL() {
			return R36_1_5Y_TOTAL;
		}

		public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
			R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
		}

		public BigDecimal getR36_O5Y_FT() {
			return R36_O5Y_FT;
		}

		public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
			R36_O5Y_FT = r36_O5Y_FT;
		}

		public BigDecimal getR36_O5Y_HTM() {
			return R36_O5Y_HTM;
		}

		public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
			R36_O5Y_HTM = r36_O5Y_HTM;
		}

		public BigDecimal getR36_O5Y_TOTAL() {
			return R36_O5Y_TOTAL;
		}

		public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
			R36_O5Y_TOTAL = r36_O5Y_TOTAL;
		}

		public BigDecimal getR36_T_FT() {
			return R36_T_FT;
		}

		public void setR36_T_FT(BigDecimal r36_T_FT) {
			R36_T_FT = r36_T_FT;
		}

		public BigDecimal getR36_T_HTM() {
			return R36_T_HTM;
		}

		public void setR36_T_HTM(BigDecimal r36_T_HTM) {
			R36_T_HTM = r36_T_HTM;
		}

		public BigDecimal getR36_T_TOTAL() {
			return R36_T_TOTAL;
		}

		public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
			R36_T_TOTAL = r36_T_TOTAL;
		}

		// Getters and Setters for R37
		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_0_1Y_FT() {
			return R37_0_1Y_FT;
		}

		public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
			R37_0_1Y_FT = r37_0_1y_FT;
		}

		public BigDecimal getR37_0_1Y_HTM() {
			return R37_0_1Y_HTM;
		}

		public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
			R37_0_1Y_HTM = r37_0_1y_HTM;
		}

		public BigDecimal getR37_0_1Y_TOTAL() {
			return R37_0_1Y_TOTAL;
		}

		public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
			R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
		}

		public BigDecimal getR37_1_5Y_FT() {
			return R37_1_5Y_FT;
		}

		public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
			R37_1_5Y_FT = r37_1_5y_FT;
		}

		public BigDecimal getR37_1_5Y_HTM() {
			return R37_1_5Y_HTM;
		}

		public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
			R37_1_5Y_HTM = r37_1_5y_HTM;
		}

		public BigDecimal getR37_1_5Y_TOTAL() {
			return R37_1_5Y_TOTAL;
		}

		public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
			R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
		}

		public BigDecimal getR37_O5Y_FT() {
			return R37_O5Y_FT;
		}

		public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
			R37_O5Y_FT = r37_O5Y_FT;
		}

		public BigDecimal getR37_O5Y_HTM() {
			return R37_O5Y_HTM;
		}

		public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
			R37_O5Y_HTM = r37_O5Y_HTM;
		}

		public BigDecimal getR37_O5Y_TOTAL() {
			return R37_O5Y_TOTAL;
		}

		public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
			R37_O5Y_TOTAL = r37_O5Y_TOTAL;
		}

		public BigDecimal getR37_T_FT() {
			return R37_T_FT;
		}

		public void setR37_T_FT(BigDecimal r37_T_FT) {
			R37_T_FT = r37_T_FT;
		}

		public BigDecimal getR37_T_HTM() {
			return R37_T_HTM;
		}

		public void setR37_T_HTM(BigDecimal r37_T_HTM) {
			R37_T_HTM = r37_T_HTM;
		}

		public BigDecimal getR37_T_TOTAL() {
			return R37_T_TOTAL;
		}

		public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
			R37_T_TOTAL = r37_T_TOTAL;
		}

		// Getters and Setters for R38
		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_0_1Y_FT() {
			return R38_0_1Y_FT;
		}

		public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
			R38_0_1Y_FT = r38_0_1y_FT;
		}

		public BigDecimal getR38_0_1Y_HTM() {
			return R38_0_1Y_HTM;
		}

		public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
			R38_0_1Y_HTM = r38_0_1y_HTM;
		}

		public BigDecimal getR38_0_1Y_TOTAL() {
			return R38_0_1Y_TOTAL;
		}

		public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
			R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
		}

		public BigDecimal getR38_1_5Y_FT() {
			return R38_1_5Y_FT;
		}

		public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
			R38_1_5Y_FT = r38_1_5y_FT;
		}

		public BigDecimal getR38_1_5Y_HTM() {
			return R38_1_5Y_HTM;
		}

		public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
			R38_1_5Y_HTM = r38_1_5y_HTM;
		}

		public BigDecimal getR38_1_5Y_TOTAL() {
			return R38_1_5Y_TOTAL;
		}

		public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
			R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
		}

		public BigDecimal getR38_O5Y_FT() {
			return R38_O5Y_FT;
		}

		public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
			R38_O5Y_FT = r38_O5Y_FT;
		}

		public BigDecimal getR38_O5Y_HTM() {
			return R38_O5Y_HTM;
		}

		public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
			R38_O5Y_HTM = r38_O5Y_HTM;
		}

		public BigDecimal getR38_O5Y_TOTAL() {
			return R38_O5Y_TOTAL;
		}

		public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
			R38_O5Y_TOTAL = r38_O5Y_TOTAL;
		}

		public BigDecimal getR38_T_FT() {
			return R38_T_FT;
		}

		public void setR38_T_FT(BigDecimal r38_T_FT) {
			R38_T_FT = r38_T_FT;
		}

		public BigDecimal getR38_T_HTM() {
			return R38_T_HTM;
		}

		public void setR38_T_HTM(BigDecimal r38_T_HTM) {
			R38_T_HTM = r38_T_HTM;
		}

		public BigDecimal getR38_T_TOTAL() {
			return R38_T_TOTAL;
		}

		public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
			R38_T_TOTAL = r38_T_TOTAL;
		}

		// Getters and Setters for R39
		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_0_1Y_FT() {
			return R39_0_1Y_FT;
		}

		public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
			R39_0_1Y_FT = r39_0_1y_FT;
		}

		public BigDecimal getR39_0_1Y_HTM() {
			return R39_0_1Y_HTM;
		}

		public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
			R39_0_1Y_HTM = r39_0_1y_HTM;
		}

		public BigDecimal getR39_0_1Y_TOTAL() {
			return R39_0_1Y_TOTAL;
		}

		public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
			R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
		}

		public BigDecimal getR39_1_5Y_FT() {
			return R39_1_5Y_FT;
		}

		public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
			R39_1_5Y_FT = r39_1_5y_FT;
		}

		public BigDecimal getR39_1_5Y_HTM() {
			return R39_1_5Y_HTM;
		}

		public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
			R39_1_5Y_HTM = r39_1_5y_HTM;
		}

		public BigDecimal getR39_1_5Y_TOTAL() {
			return R39_1_5Y_TOTAL;
		}

		public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
			R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
		}

		public BigDecimal getR39_O5Y_FT() {
			return R39_O5Y_FT;
		}

		public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
			R39_O5Y_FT = r39_O5Y_FT;
		}

		public BigDecimal getR39_O5Y_HTM() {
			return R39_O5Y_HTM;
		}

		public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
			R39_O5Y_HTM = r39_O5Y_HTM;
		}

		public BigDecimal getR39_O5Y_TOTAL() {
			return R39_O5Y_TOTAL;
		}

		public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
			R39_O5Y_TOTAL = r39_O5Y_TOTAL;
		}

		public BigDecimal getR39_T_FT() {
			return R39_T_FT;
		}

		public void setR39_T_FT(BigDecimal r39_T_FT) {
			R39_T_FT = r39_T_FT;
		}

		public BigDecimal getR39_T_HTM() {
			return R39_T_HTM;
		}

		public void setR39_T_HTM(BigDecimal r39_T_HTM) {
			R39_T_HTM = r39_T_HTM;
		}

		public BigDecimal getR39_T_TOTAL() {
			return R39_T_TOTAL;
		}

		public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
			R39_T_TOTAL = r39_T_TOTAL;
		}

		// Getters and Setters for R40
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_0_1Y_FT() {
			return R40_0_1Y_FT;
		}

		public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
			R40_0_1Y_FT = r40_0_1y_FT;
		}

		public BigDecimal getR40_0_1Y_HTM() {
			return R40_0_1Y_HTM;
		}

		public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
			R40_0_1Y_HTM = r40_0_1y_HTM;
		}

		public BigDecimal getR40_0_1Y_TOTAL() {
			return R40_0_1Y_TOTAL;
		}

		public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
			R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
		}

		public BigDecimal getR40_1_5Y_FT() {
			return R40_1_5Y_FT;
		}

		public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
			R40_1_5Y_FT = r40_1_5y_FT;
		}

		public BigDecimal getR40_1_5Y_HTM() {
			return R40_1_5Y_HTM;
		}

		public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
			R40_1_5Y_HTM = r40_1_5y_HTM;
		}

		public BigDecimal getR40_1_5Y_TOTAL() {
			return R40_1_5Y_TOTAL;
		}

		public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
			R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
		}

		public BigDecimal getR40_O5Y_FT() {
			return R40_O5Y_FT;
		}

		public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
			R40_O5Y_FT = r40_O5Y_FT;
		}

		public BigDecimal getR40_O5Y_HTM() {
			return R40_O5Y_HTM;
		}

		public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
			R40_O5Y_HTM = r40_O5Y_HTM;
		}

		public BigDecimal getR40_O5Y_TOTAL() {
			return R40_O5Y_TOTAL;
		}

		public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
			R40_O5Y_TOTAL = r40_O5Y_TOTAL;
		}

		public BigDecimal getR40_T_FT() {
			return R40_T_FT;
		}

		public void setR40_T_FT(BigDecimal r40_T_FT) {
			R40_T_FT = r40_T_FT;
		}

		public BigDecimal getR40_T_HTM() {
			return R40_T_HTM;
		}

		public void setR40_T_HTM(BigDecimal r40_T_HTM) {
			R40_T_HTM = r40_T_HTM;
		}

		public BigDecimal getR40_T_TOTAL() {
			return R40_T_TOTAL;
		}

		public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
			R40_T_TOTAL = r40_T_TOTAL;
		}

		// Getters and Setters for R41
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_0_1Y_FT() {
			return R41_0_1Y_FT;
		}

		public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
			R41_0_1Y_FT = r41_0_1y_FT;
		}

		public BigDecimal getR41_0_1Y_HTM() {
			return R41_0_1Y_HTM;
		}

		public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
			R41_0_1Y_HTM = r41_0_1y_HTM;
		}

		public BigDecimal getR41_0_1Y_TOTAL() {
			return R41_0_1Y_TOTAL;
		}

		public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
			R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
		}

		public BigDecimal getR41_1_5Y_FT() {
			return R41_1_5Y_FT;
		}

		public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
			R41_1_5Y_FT = r41_1_5y_FT;
		}

		public BigDecimal getR41_1_5Y_HTM() {
			return R41_1_5Y_HTM;
		}

		public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
			R41_1_5Y_HTM = r41_1_5y_HTM;
		}

		public BigDecimal getR41_1_5Y_TOTAL() {
			return R41_1_5Y_TOTAL;
		}

		public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
			R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
		}

		public BigDecimal getR41_O5Y_FT() {
			return R41_O5Y_FT;
		}

		public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
			R41_O5Y_FT = r41_O5Y_FT;
		}

		public BigDecimal getR41_O5Y_HTM() {
			return R41_O5Y_HTM;
		}

		public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
			R41_O5Y_HTM = r41_O5Y_HTM;
		}

		public BigDecimal getR41_O5Y_TOTAL() {
			return R41_O5Y_TOTAL;
		}

		public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
			R41_O5Y_TOTAL = r41_O5Y_TOTAL;
		}

		public BigDecimal getR41_T_FT() {
			return R41_T_FT;
		}

		public void setR41_T_FT(BigDecimal r41_T_FT) {
			R41_T_FT = r41_T_FT;
		}

		public BigDecimal getR41_T_HTM() {
			return R41_T_HTM;
		}

		public void setR41_T_HTM(BigDecimal r41_T_HTM) {
			R41_T_HTM = r41_T_HTM;
		}

		public BigDecimal getR41_T_TOTAL() {
			return R41_T_TOTAL;
		}

		public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
			R41_T_TOTAL = r41_T_TOTAL;
		}

		// Getters and Setters for R42
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_0_1Y_FT() {
			return R42_0_1Y_FT;
		}

		public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
			R42_0_1Y_FT = r42_0_1y_FT;
		}

		public BigDecimal getR42_0_1Y_HTM() {
			return R42_0_1Y_HTM;
		}

		public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
			R42_0_1Y_HTM = r42_0_1y_HTM;
		}

		public BigDecimal getR42_0_1Y_TOTAL() {
			return R42_0_1Y_TOTAL;
		}

		public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
			R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
		}

		public BigDecimal getR42_1_5Y_FT() {
			return R42_1_5Y_FT;
		}

		public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
			R42_1_5Y_FT = r42_1_5y_FT;
		}

		public BigDecimal getR42_1_5Y_HTM() {
			return R42_1_5Y_HTM;
		}

		public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
			R42_1_5Y_HTM = r42_1_5y_HTM;
		}

		public BigDecimal getR42_1_5Y_TOTAL() {
			return R42_1_5Y_TOTAL;
		}

		public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
			R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
		}

		public BigDecimal getR42_O5Y_FT() {
			return R42_O5Y_FT;
		}

		public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
			R42_O5Y_FT = r42_O5Y_FT;
		}

		public BigDecimal getR42_O5Y_HTM() {
			return R42_O5Y_HTM;
		}

		public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
			R42_O5Y_HTM = r42_O5Y_HTM;
		}

		public BigDecimal getR42_O5Y_TOTAL() {
			return R42_O5Y_TOTAL;
		}

		public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
			R42_O5Y_TOTAL = r42_O5Y_TOTAL;
		}

		public BigDecimal getR42_T_FT() {
			return R42_T_FT;
		}

		public void setR42_T_FT(BigDecimal r42_T_FT) {
			R42_T_FT = r42_T_FT;
		}

		public BigDecimal getR42_T_HTM() {
			return R42_T_HTM;
		}

		public void setR42_T_HTM(BigDecimal r42_T_HTM) {
			R42_T_HTM = r42_T_HTM;
		}

		public BigDecimal getR42_T_TOTAL() {
			return R42_T_TOTAL;
		}

		public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
			R42_T_TOTAL = r42_T_TOTAL;
		}

		// Getters and Setters for R43
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public BigDecimal getR43_0_1Y_FT() {
			return R43_0_1Y_FT;
		}

		public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
			R43_0_1Y_FT = r43_0_1y_FT;
		}

		public BigDecimal getR43_0_1Y_HTM() {
			return R43_0_1Y_HTM;
		}

		public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
			R43_0_1Y_HTM = r43_0_1y_HTM;
		}

		public BigDecimal getR43_0_1Y_TOTAL() {
			return R43_0_1Y_TOTAL;
		}

		public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
			R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
		}

		public BigDecimal getR43_1_5Y_FT() {
			return R43_1_5Y_FT;
		}

		public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
			R43_1_5Y_FT = r43_1_5y_FT;
		}

		public BigDecimal getR43_1_5Y_HTM() {
			return R43_1_5Y_HTM;
		}

		public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
			R43_1_5Y_HTM = r43_1_5y_HTM;
		}

		public BigDecimal getR43_1_5Y_TOTAL() {
			return R43_1_5Y_TOTAL;
		}

		public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
			R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
		}

		public BigDecimal getR43_O5Y_FT() {
			return R43_O5Y_FT;
		}

		public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
			R43_O5Y_FT = r43_O5Y_FT;
		}

		public BigDecimal getR43_O5Y_HTM() {
			return R43_O5Y_HTM;
		}

		public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
			R43_O5Y_HTM = r43_O5Y_HTM;
		}

		public BigDecimal getR43_O5Y_TOTAL() {
			return R43_O5Y_TOTAL;
		}

		public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
			R43_O5Y_TOTAL = r43_O5Y_TOTAL;
		}

		public BigDecimal getR43_T_FT() {
			return R43_T_FT;
		}

		public void setR43_T_FT(BigDecimal r43_T_FT) {
			R43_T_FT = r43_T_FT;
		}

		public BigDecimal getR43_T_HTM() {
			return R43_T_HTM;
		}

		public void setR43_T_HTM(BigDecimal r43_T_HTM) {
			R43_T_HTM = r43_T_HTM;
		}

		public BigDecimal getR43_T_TOTAL() {
			return R43_T_TOTAL;
		}

		public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
			R43_T_TOTAL = r43_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_Archival_Detail1_RowMapper
//===========================

	public class M_SEC_Archival_Detail1_RowMapper implements RowMapper<M_SEC_Archival_Detail1_Entity> {

		@Override
		public M_SEC_Archival_Detail1_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Archival_Detail1_Entity obj = new M_SEC_Archival_Detail1_Entity();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA(rs.getBigDecimal("R11_TCA"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA(rs.getBigDecimal("R12_TCA"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA(rs.getBigDecimal("R13_TCA"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA(rs.getBigDecimal("R14_TCA"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA(rs.getBigDecimal("R15_TCA"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA(rs.getBigDecimal("R16_TCA"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TCA(rs.getBigDecimal("R17_TCA"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TCA(rs.getBigDecimal("R18_TCA"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TCA(rs.getBigDecimal("R19_TCA"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Archival_Detail1_Entity
//===========================

	public class M_SEC_Archival_Detail1_Entity {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA;
		private String R17_PRODUCT;
		private BigDecimal R17_TCA;
		private String R18_PRODUCT;
		private BigDecimal R18_TCA;
		private String R19_PRODUCT;
		private BigDecimal R19_TCA;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Archival_Detail1_Entity() {
			super();
		}

		// Getters and Setters for R11
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA() {
			return R11_TCA;
		}

		public void setR11_TCA(BigDecimal r11_TCA) {
			R11_TCA = r11_TCA;
		}

		// Getters and Setters for R12
		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA() {
			return R12_TCA;
		}

		public void setR12_TCA(BigDecimal r12_TCA) {
			R12_TCA = r12_TCA;
		}

		// Getters and Setters for R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA() {
			return R13_TCA;
		}

		public void setR13_TCA(BigDecimal r13_TCA) {
			R13_TCA = r13_TCA;
		}

		// Getters and Setters for R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA() {
			return R14_TCA;
		}

		public void setR14_TCA(BigDecimal r14_TCA) {
			R14_TCA = r14_TCA;
		}

		// Getters and Setters for R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA() {
			return R15_TCA;
		}

		public void setR15_TCA(BigDecimal r15_TCA) {
			R15_TCA = r15_TCA;
		}

		// Getters and Setters for R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA() {
			return R16_TCA;
		}

		public void setR16_TCA(BigDecimal r16_TCA) {
			R16_TCA = r16_TCA;
		}

		// Getters and Setters for R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TCA() {
			return R17_TCA;
		}

		public void setR17_TCA(BigDecimal r17_TCA) {
			R17_TCA = r17_TCA;
		}

		// Getters and Setters for R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_TCA() {
			return R18_TCA;
		}

		public void setR18_TCA(BigDecimal r18_TCA) {
			R18_TCA = r18_TCA;
		}

		// Getters and Setters for R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_TCA() {
			return R19_TCA;
		}

		public void setR19_TCA(BigDecimal r19_TCA) {
			R19_TCA = r19_TCA;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_Archival_Detail2_RowMapper
//===========================

	public class M_SEC_Archival_Detail2_RowMapper implements RowMapper<M_SEC_Archival_Detail2_Entity> {

		@Override
		public M_SEC_Archival_Detail2_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Archival_Detail2_Entity obj = new M_SEC_Archival_Detail2_Entity();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA2(rs.getBigDecimal("R11_TCA2"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA2(rs.getBigDecimal("R12_TCA2"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA2(rs.getBigDecimal("R13_TCA2"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA2(rs.getBigDecimal("R14_TCA2"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA2(rs.getBigDecimal("R15_TCA2"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA2(rs.getBigDecimal("R16_TCA2"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Archival_Detail2_Entity
//===========================

	public class M_SEC_Archival_Detail2_Entity {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA2;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA2;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA2;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA2;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA2;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA2;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Archival_Detail2_Entity() {
			super();
		}

		// Getters and Setters for R11
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA2() {
			return R11_TCA2;
		}

		public void setR11_TCA2(BigDecimal r11_TCA2) {
			R11_TCA2 = r11_TCA2;
		}

		// Getters and Setters for R12
		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA2() {
			return R12_TCA2;
		}

		public void setR12_TCA2(BigDecimal r12_TCA2) {
			R12_TCA2 = r12_TCA2;
		}

		// Getters and Setters for R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA2() {
			return R13_TCA2;
		}

		public void setR13_TCA2(BigDecimal r13_TCA2) {
			R13_TCA2 = r13_TCA2;
		}

		// Getters and Setters for R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA2() {
			return R14_TCA2;
		}

		public void setR14_TCA2(BigDecimal r14_TCA2) {
			R14_TCA2 = r14_TCA2;
		}

		// Getters and Setters for R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA2() {
			return R15_TCA2;
		}

		public void setR15_TCA2(BigDecimal r15_TCA2) {
			R15_TCA2 = r15_TCA2;
		}

		// Getters and Setters for R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA2() {
			return R16_TCA2;
		}

		public void setR16_TCA2(BigDecimal r16_TCA2) {
			R16_TCA2 = r16_TCA2;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_Archival_Detail3_RowMapper
//===========================

	public class M_SEC_Archival_Detail3_RowMapper implements RowMapper<M_SEC_Archival_Detail3_Entity> {

		@Override
		public M_SEC_Archival_Detail3_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Archival_Detail3_Entity obj = new M_SEC_Archival_Detail3_Entity();

			// =========================
			// R26
			// =========================
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_0_1Y_FT(rs.getBigDecimal("R26_0_1Y_FT"));
			obj.setR26_0_1Y_HTM(rs.getBigDecimal("R26_0_1Y_HTM"));
			obj.setR26_0_1Y_TOTAL(rs.getBigDecimal("R26_0_1Y_TOTAL"));
			obj.setR26_1_5Y_FT(rs.getBigDecimal("R26_1_5Y_FT"));
			obj.setR26_1_5Y_HTM(rs.getBigDecimal("R26_1_5Y_HTM"));
			obj.setR26_1_5Y_TOTAL(rs.getBigDecimal("R26_1_5Y_TOTAL"));
			obj.setR26_O5Y_FT(rs.getBigDecimal("R26_O5Y_FT"));
			obj.setR26_O5Y_HTM(rs.getBigDecimal("R26_O5Y_HTM"));
			obj.setR26_O5Y_TOTAL(rs.getBigDecimal("R26_O5Y_TOTAL"));
			obj.setR26_T_FT(rs.getBigDecimal("R26_T_FT"));
			obj.setR26_T_HTM(rs.getBigDecimal("R26_T_HTM"));
			obj.setR26_T_TOTAL(rs.getBigDecimal("R26_T_TOTAL"));

			// =========================
			// R27
			// =========================
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_0_1Y_FT(rs.getBigDecimal("R27_0_1Y_FT"));
			obj.setR27_0_1Y_HTM(rs.getBigDecimal("R27_0_1Y_HTM"));
			obj.setR27_0_1Y_TOTAL(rs.getBigDecimal("R27_0_1Y_TOTAL"));
			obj.setR27_1_5Y_FT(rs.getBigDecimal("R27_1_5Y_FT"));
			obj.setR27_1_5Y_HTM(rs.getBigDecimal("R27_1_5Y_HTM"));
			obj.setR27_1_5Y_TOTAL(rs.getBigDecimal("R27_1_5Y_TOTAL"));
			obj.setR27_O5Y_FT(rs.getBigDecimal("R27_O5Y_FT"));
			obj.setR27_O5Y_HTM(rs.getBigDecimal("R27_O5Y_HTM"));
			obj.setR27_O5Y_TOTAL(rs.getBigDecimal("R27_O5Y_TOTAL"));
			obj.setR27_T_FT(rs.getBigDecimal("R27_T_FT"));
			obj.setR27_T_HTM(rs.getBigDecimal("R27_T_HTM"));
			obj.setR27_T_TOTAL(rs.getBigDecimal("R27_T_TOTAL"));

			// =========================
			// R28
			// =========================
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_0_1Y_FT(rs.getBigDecimal("R28_0_1Y_FT"));
			obj.setR28_0_1Y_HTM(rs.getBigDecimal("R28_0_1Y_HTM"));
			obj.setR28_0_1Y_TOTAL(rs.getBigDecimal("R28_0_1Y_TOTAL"));
			obj.setR28_1_5Y_FT(rs.getBigDecimal("R28_1_5Y_FT"));
			obj.setR28_1_5Y_HTM(rs.getBigDecimal("R28_1_5Y_HTM"));
			obj.setR28_1_5Y_TOTAL(rs.getBigDecimal("R28_1_5Y_TOTAL"));
			obj.setR28_O5Y_FT(rs.getBigDecimal("R28_O5Y_FT"));
			obj.setR28_O5Y_HTM(rs.getBigDecimal("R28_O5Y_HTM"));
			obj.setR28_O5Y_TOTAL(rs.getBigDecimal("R28_O5Y_TOTAL"));
			obj.setR28_T_FT(rs.getBigDecimal("R28_T_FT"));
			obj.setR28_T_HTM(rs.getBigDecimal("R28_T_HTM"));
			obj.setR28_T_TOTAL(rs.getBigDecimal("R28_T_TOTAL"));

			// =========================
			// R29
			// =========================
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_0_1Y_FT(rs.getBigDecimal("R29_0_1Y_FT"));
			obj.setR29_0_1Y_HTM(rs.getBigDecimal("R29_0_1Y_HTM"));
			obj.setR29_0_1Y_TOTAL(rs.getBigDecimal("R29_0_1Y_TOTAL"));
			obj.setR29_1_5Y_FT(rs.getBigDecimal("R29_1_5Y_FT"));
			obj.setR29_1_5Y_HTM(rs.getBigDecimal("R29_1_5Y_HTM"));
			obj.setR29_1_5Y_TOTAL(rs.getBigDecimal("R29_1_5Y_TOTAL"));
			obj.setR29_O5Y_FT(rs.getBigDecimal("R29_O5Y_FT"));
			obj.setR29_O5Y_HTM(rs.getBigDecimal("R29_O5Y_HTM"));
			obj.setR29_O5Y_TOTAL(rs.getBigDecimal("R29_O5Y_TOTAL"));
			obj.setR29_T_FT(rs.getBigDecimal("R29_T_FT"));
			obj.setR29_T_HTM(rs.getBigDecimal("R29_T_HTM"));
			obj.setR29_T_TOTAL(rs.getBigDecimal("R29_T_TOTAL"));

			// =========================
			// R30
			// =========================
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_0_1Y_FT(rs.getBigDecimal("R30_0_1Y_FT"));
			obj.setR30_0_1Y_HTM(rs.getBigDecimal("R30_0_1Y_HTM"));
			obj.setR30_0_1Y_TOTAL(rs.getBigDecimal("R30_0_1Y_TOTAL"));
			obj.setR30_1_5Y_FT(rs.getBigDecimal("R30_1_5Y_FT"));
			obj.setR30_1_5Y_HTM(rs.getBigDecimal("R30_1_5Y_HTM"));
			obj.setR30_1_5Y_TOTAL(rs.getBigDecimal("R30_1_5Y_TOTAL"));
			obj.setR30_O5Y_FT(rs.getBigDecimal("R30_O5Y_FT"));
			obj.setR30_O5Y_HTM(rs.getBigDecimal("R30_O5Y_HTM"));
			obj.setR30_O5Y_TOTAL(rs.getBigDecimal("R30_O5Y_TOTAL"));
			obj.setR30_T_FT(rs.getBigDecimal("R30_T_FT"));
			obj.setR30_T_HTM(rs.getBigDecimal("R30_T_HTM"));
			obj.setR30_T_TOTAL(rs.getBigDecimal("R30_T_TOTAL"));

			// =========================
			// R31
			// =========================
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_0_1Y_FT(rs.getBigDecimal("R31_0_1Y_FT"));
			obj.setR31_0_1Y_HTM(rs.getBigDecimal("R31_0_1Y_HTM"));
			obj.setR31_0_1Y_TOTAL(rs.getBigDecimal("R31_0_1Y_TOTAL"));
			obj.setR31_1_5Y_FT(rs.getBigDecimal("R31_1_5Y_FT"));
			obj.setR31_1_5Y_HTM(rs.getBigDecimal("R31_1_5Y_HTM"));
			obj.setR31_1_5Y_TOTAL(rs.getBigDecimal("R31_1_5Y_TOTAL"));
			obj.setR31_O5Y_FT(rs.getBigDecimal("R31_O5Y_FT"));
			obj.setR31_O5Y_HTM(rs.getBigDecimal("R31_O5Y_HTM"));
			obj.setR31_O5Y_TOTAL(rs.getBigDecimal("R31_O5Y_TOTAL"));
			obj.setR31_T_FT(rs.getBigDecimal("R31_T_FT"));
			obj.setR31_T_HTM(rs.getBigDecimal("R31_T_HTM"));
			obj.setR31_T_TOTAL(rs.getBigDecimal("R31_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Archival_Detail3_Entity
//===========================

	public class M_SEC_Archival_Detail3_Entity {

		private String R26_PRODUCT;
		private BigDecimal R26_0_1Y_FT;
		private BigDecimal R26_0_1Y_HTM;
		private BigDecimal R26_0_1Y_TOTAL;
		private BigDecimal R26_1_5Y_FT;
		private BigDecimal R26_1_5Y_HTM;
		private BigDecimal R26_1_5Y_TOTAL;
		private BigDecimal R26_O5Y_FT;
		private BigDecimal R26_O5Y_HTM;
		private BigDecimal R26_O5Y_TOTAL;
		private BigDecimal R26_T_FT;
		private BigDecimal R26_T_HTM;
		private BigDecimal R26_T_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_0_1Y_FT;
		private BigDecimal R27_0_1Y_HTM;
		private BigDecimal R27_0_1Y_TOTAL;
		private BigDecimal R27_1_5Y_FT;
		private BigDecimal R27_1_5Y_HTM;
		private BigDecimal R27_1_5Y_TOTAL;
		private BigDecimal R27_O5Y_FT;
		private BigDecimal R27_O5Y_HTM;
		private BigDecimal R27_O5Y_TOTAL;
		private BigDecimal R27_T_FT;
		private BigDecimal R27_T_HTM;
		private BigDecimal R27_T_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_0_1Y_FT;
		private BigDecimal R28_0_1Y_HTM;
		private BigDecimal R28_0_1Y_TOTAL;
		private BigDecimal R28_1_5Y_FT;
		private BigDecimal R28_1_5Y_HTM;
		private BigDecimal R28_1_5Y_TOTAL;
		private BigDecimal R28_O5Y_FT;
		private BigDecimal R28_O5Y_HTM;
		private BigDecimal R28_O5Y_TOTAL;
		private BigDecimal R28_T_FT;
		private BigDecimal R28_T_HTM;
		private BigDecimal R28_T_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_0_1Y_FT;
		private BigDecimal R29_0_1Y_HTM;
		private BigDecimal R29_0_1Y_TOTAL;
		private BigDecimal R29_1_5Y_FT;
		private BigDecimal R29_1_5Y_HTM;
		private BigDecimal R29_1_5Y_TOTAL;
		private BigDecimal R29_O5Y_FT;
		private BigDecimal R29_O5Y_HTM;
		private BigDecimal R29_O5Y_TOTAL;
		private BigDecimal R29_T_FT;
		private BigDecimal R29_T_HTM;
		private BigDecimal R29_T_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_0_1Y_FT;
		private BigDecimal R30_0_1Y_HTM;
		private BigDecimal R30_0_1Y_TOTAL;
		private BigDecimal R30_1_5Y_FT;
		private BigDecimal R30_1_5Y_HTM;
		private BigDecimal R30_1_5Y_TOTAL;
		private BigDecimal R30_O5Y_FT;
		private BigDecimal R30_O5Y_HTM;
		private BigDecimal R30_O5Y_TOTAL;
		private BigDecimal R30_T_FT;
		private BigDecimal R30_T_HTM;
		private BigDecimal R30_T_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_0_1Y_FT;
		private BigDecimal R31_0_1Y_HTM;
		private BigDecimal R31_0_1Y_TOTAL;
		private BigDecimal R31_1_5Y_FT;
		private BigDecimal R31_1_5Y_HTM;
		private BigDecimal R31_1_5Y_TOTAL;
		private BigDecimal R31_O5Y_FT;
		private BigDecimal R31_O5Y_HTM;
		private BigDecimal R31_O5Y_TOTAL;
		private BigDecimal R31_T_FT;
		private BigDecimal R31_T_HTM;
		private BigDecimal R31_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Archival_Detail3_Entity() {
			super();
		}

		// Getters and Setters for R26
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}

		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}

		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}

		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}

		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}

		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}

		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}

		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}

		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}

		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}

		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}

		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}

		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}

		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}

		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}

		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}

		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}

		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}

		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}

		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}

		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}

		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}

		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}

		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}

		// Getters and Setters for R27
		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}

		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}

		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}

		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}

		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}

		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}

		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}

		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}

		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}

		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}

		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}

		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}

		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}

		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}

		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}

		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}

		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}

		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}

		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}

		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}

		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}

		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}

		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}

		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}

		// Getters and Setters for R28
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}

		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}

		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}

		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}

		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}

		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}

		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}

		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}

		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}

		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}

		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}

		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}

		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}

		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}

		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}

		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}

		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}

		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}

		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}

		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}

		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}

		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}

		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}

		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}

		// Getters and Setters for R29
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}

		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}

		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}

		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}

		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}

		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}

		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}

		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}

		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}

		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}

		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}

		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}

		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}

		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}

		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}

		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}

		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}

		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}

		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}

		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}

		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}

		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}

		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}

		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}

		// Getters and Setters for R30
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}

		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}

		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}

		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}

		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}

		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}

		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}

		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}

		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}

		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}

		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}

		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}

		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}

		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}

		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}

		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}

		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}

		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}

		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}

		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}

		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}

		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}

		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}

		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}

		// Getters and Setters for R31
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}

		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}

		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}

		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}

		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}

		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}

		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}

		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}

		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}

		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}

		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}

		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}

		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}

		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}

		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}

		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}

		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}

		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}

		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}

		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}

		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}

		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}

		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}

		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_Archival_Detail4_RowMapper
//===========================

	public class M_SEC_Archival_Detail4_RowMapper implements RowMapper<M_SEC_Archival_Detail4_Entity> {

		@Override
		public M_SEC_Archival_Detail4_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_Archival_Detail4_Entity obj = new M_SEC_Archival_Detail4_Entity();

			// =========================
			// R36
			// =========================
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_0_1Y_FT(rs.getBigDecimal("R36_0_1Y_FT"));
			obj.setR36_0_1Y_HTM(rs.getBigDecimal("R36_0_1Y_HTM"));
			obj.setR36_0_1Y_TOTAL(rs.getBigDecimal("R36_0_1Y_TOTAL"));
			obj.setR36_1_5Y_FT(rs.getBigDecimal("R36_1_5Y_FT"));
			obj.setR36_1_5Y_HTM(rs.getBigDecimal("R36_1_5Y_HTM"));
			obj.setR36_1_5Y_TOTAL(rs.getBigDecimal("R36_1_5Y_TOTAL"));
			obj.setR36_O5Y_FT(rs.getBigDecimal("R36_O5Y_FT"));
			obj.setR36_O5Y_HTM(rs.getBigDecimal("R36_O5Y_HTM"));
			obj.setR36_O5Y_TOTAL(rs.getBigDecimal("R36_O5Y_TOTAL"));
			obj.setR36_T_FT(rs.getBigDecimal("R36_T_FT"));
			obj.setR36_T_HTM(rs.getBigDecimal("R36_T_HTM"));
			obj.setR36_T_TOTAL(rs.getBigDecimal("R36_T_TOTAL"));

			// =========================
			// R37
			// =========================
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_0_1Y_FT(rs.getBigDecimal("R37_0_1Y_FT"));
			obj.setR37_0_1Y_HTM(rs.getBigDecimal("R37_0_1Y_HTM"));
			obj.setR37_0_1Y_TOTAL(rs.getBigDecimal("R37_0_1Y_TOTAL"));
			obj.setR37_1_5Y_FT(rs.getBigDecimal("R37_1_5Y_FT"));
			obj.setR37_1_5Y_HTM(rs.getBigDecimal("R37_1_5Y_HTM"));
			obj.setR37_1_5Y_TOTAL(rs.getBigDecimal("R37_1_5Y_TOTAL"));
			obj.setR37_O5Y_FT(rs.getBigDecimal("R37_O5Y_FT"));
			obj.setR37_O5Y_HTM(rs.getBigDecimal("R37_O5Y_HTM"));
			obj.setR37_O5Y_TOTAL(rs.getBigDecimal("R37_O5Y_TOTAL"));
			obj.setR37_T_FT(rs.getBigDecimal("R37_T_FT"));
			obj.setR37_T_HTM(rs.getBigDecimal("R37_T_HTM"));
			obj.setR37_T_TOTAL(rs.getBigDecimal("R37_T_TOTAL"));

			// =========================
			// R38
			// =========================
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_0_1Y_FT(rs.getBigDecimal("R38_0_1Y_FT"));
			obj.setR38_0_1Y_HTM(rs.getBigDecimal("R38_0_1Y_HTM"));
			obj.setR38_0_1Y_TOTAL(rs.getBigDecimal("R38_0_1Y_TOTAL"));
			obj.setR38_1_5Y_FT(rs.getBigDecimal("R38_1_5Y_FT"));
			obj.setR38_1_5Y_HTM(rs.getBigDecimal("R38_1_5Y_HTM"));
			obj.setR38_1_5Y_TOTAL(rs.getBigDecimal("R38_1_5Y_TOTAL"));
			obj.setR38_O5Y_FT(rs.getBigDecimal("R38_O5Y_FT"));
			obj.setR38_O5Y_HTM(rs.getBigDecimal("R38_O5Y_HTM"));
			obj.setR38_O5Y_TOTAL(rs.getBigDecimal("R38_O5Y_TOTAL"));
			obj.setR38_T_FT(rs.getBigDecimal("R38_T_FT"));
			obj.setR38_T_HTM(rs.getBigDecimal("R38_T_HTM"));
			obj.setR38_T_TOTAL(rs.getBigDecimal("R38_T_TOTAL"));

			// =========================
			// R39
			// =========================
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_0_1Y_FT(rs.getBigDecimal("R39_0_1Y_FT"));
			obj.setR39_0_1Y_HTM(rs.getBigDecimal("R39_0_1Y_HTM"));
			obj.setR39_0_1Y_TOTAL(rs.getBigDecimal("R39_0_1Y_TOTAL"));
			obj.setR39_1_5Y_FT(rs.getBigDecimal("R39_1_5Y_FT"));
			obj.setR39_1_5Y_HTM(rs.getBigDecimal("R39_1_5Y_HTM"));
			obj.setR39_1_5Y_TOTAL(rs.getBigDecimal("R39_1_5Y_TOTAL"));
			obj.setR39_O5Y_FT(rs.getBigDecimal("R39_O5Y_FT"));
			obj.setR39_O5Y_HTM(rs.getBigDecimal("R39_O5Y_HTM"));
			obj.setR39_O5Y_TOTAL(rs.getBigDecimal("R39_O5Y_TOTAL"));
			obj.setR39_T_FT(rs.getBigDecimal("R39_T_FT"));
			obj.setR39_T_HTM(rs.getBigDecimal("R39_T_HTM"));
			obj.setR39_T_TOTAL(rs.getBigDecimal("R39_T_TOTAL"));

			// =========================
			// R40
			// =========================
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_0_1Y_FT(rs.getBigDecimal("R40_0_1Y_FT"));
			obj.setR40_0_1Y_HTM(rs.getBigDecimal("R40_0_1Y_HTM"));
			obj.setR40_0_1Y_TOTAL(rs.getBigDecimal("R40_0_1Y_TOTAL"));
			obj.setR40_1_5Y_FT(rs.getBigDecimal("R40_1_5Y_FT"));
			obj.setR40_1_5Y_HTM(rs.getBigDecimal("R40_1_5Y_HTM"));
			obj.setR40_1_5Y_TOTAL(rs.getBigDecimal("R40_1_5Y_TOTAL"));
			obj.setR40_O5Y_FT(rs.getBigDecimal("R40_O5Y_FT"));
			obj.setR40_O5Y_HTM(rs.getBigDecimal("R40_O5Y_HTM"));
			obj.setR40_O5Y_TOTAL(rs.getBigDecimal("R40_O5Y_TOTAL"));
			obj.setR40_T_FT(rs.getBigDecimal("R40_T_FT"));
			obj.setR40_T_HTM(rs.getBigDecimal("R40_T_HTM"));
			obj.setR40_T_TOTAL(rs.getBigDecimal("R40_T_TOTAL"));

			// =========================
			// R41
			// =========================
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_0_1Y_FT(rs.getBigDecimal("R41_0_1Y_FT"));
			obj.setR41_0_1Y_HTM(rs.getBigDecimal("R41_0_1Y_HTM"));
			obj.setR41_0_1Y_TOTAL(rs.getBigDecimal("R41_0_1Y_TOTAL"));
			obj.setR41_1_5Y_FT(rs.getBigDecimal("R41_1_5Y_FT"));
			obj.setR41_1_5Y_HTM(rs.getBigDecimal("R41_1_5Y_HTM"));
			obj.setR41_1_5Y_TOTAL(rs.getBigDecimal("R41_1_5Y_TOTAL"));
			obj.setR41_O5Y_FT(rs.getBigDecimal("R41_O5Y_FT"));
			obj.setR41_O5Y_HTM(rs.getBigDecimal("R41_O5Y_HTM"));
			obj.setR41_O5Y_TOTAL(rs.getBigDecimal("R41_O5Y_TOTAL"));
			obj.setR41_T_FT(rs.getBigDecimal("R41_T_FT"));
			obj.setR41_T_HTM(rs.getBigDecimal("R41_T_HTM"));
			obj.setR41_T_TOTAL(rs.getBigDecimal("R41_T_TOTAL"));

			// =========================
			// R42
			// =========================
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_0_1Y_FT(rs.getBigDecimal("R42_0_1Y_FT"));
			obj.setR42_0_1Y_HTM(rs.getBigDecimal("R42_0_1Y_HTM"));
			obj.setR42_0_1Y_TOTAL(rs.getBigDecimal("R42_0_1Y_TOTAL"));
			obj.setR42_1_5Y_FT(rs.getBigDecimal("R42_1_5Y_FT"));
			obj.setR42_1_5Y_HTM(rs.getBigDecimal("R42_1_5Y_HTM"));
			obj.setR42_1_5Y_TOTAL(rs.getBigDecimal("R42_1_5Y_TOTAL"));
			obj.setR42_O5Y_FT(rs.getBigDecimal("R42_O5Y_FT"));
			obj.setR42_O5Y_HTM(rs.getBigDecimal("R42_O5Y_HTM"));
			obj.setR42_O5Y_TOTAL(rs.getBigDecimal("R42_O5Y_TOTAL"));
			obj.setR42_T_FT(rs.getBigDecimal("R42_T_FT"));
			obj.setR42_T_HTM(rs.getBigDecimal("R42_T_HTM"));
			obj.setR42_T_TOTAL(rs.getBigDecimal("R42_T_TOTAL"));

			// =========================
			// R43
			// =========================
			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
			obj.setR43_0_1Y_FT(rs.getBigDecimal("R43_0_1Y_FT"));
			obj.setR43_0_1Y_HTM(rs.getBigDecimal("R43_0_1Y_HTM"));
			obj.setR43_0_1Y_TOTAL(rs.getBigDecimal("R43_0_1Y_TOTAL"));
			obj.setR43_1_5Y_FT(rs.getBigDecimal("R43_1_5Y_FT"));
			obj.setR43_1_5Y_HTM(rs.getBigDecimal("R43_1_5Y_HTM"));
			obj.setR43_1_5Y_TOTAL(rs.getBigDecimal("R43_1_5Y_TOTAL"));
			obj.setR43_O5Y_FT(rs.getBigDecimal("R43_O5Y_FT"));
			obj.setR43_O5Y_HTM(rs.getBigDecimal("R43_O5Y_HTM"));
			obj.setR43_O5Y_TOTAL(rs.getBigDecimal("R43_O5Y_TOTAL"));
			obj.setR43_T_FT(rs.getBigDecimal("R43_T_FT"));
			obj.setR43_T_HTM(rs.getBigDecimal("R43_T_HTM"));
			obj.setR43_T_TOTAL(rs.getBigDecimal("R43_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_Archival_Detail4_Entity
//===========================

	public class M_SEC_Archival_Detail4_Entity {

		private String R36_PRODUCT;
		private BigDecimal R36_0_1Y_FT;
		private BigDecimal R36_0_1Y_HTM;
		private BigDecimal R36_0_1Y_TOTAL;
		private BigDecimal R36_1_5Y_FT;
		private BigDecimal R36_1_5Y_HTM;
		private BigDecimal R36_1_5Y_TOTAL;
		private BigDecimal R36_O5Y_FT;
		private BigDecimal R36_O5Y_HTM;
		private BigDecimal R36_O5Y_TOTAL;
		private BigDecimal R36_T_FT;
		private BigDecimal R36_T_HTM;
		private BigDecimal R36_T_TOTAL;

		private String R37_PRODUCT;
		private BigDecimal R37_0_1Y_FT;
		private BigDecimal R37_0_1Y_HTM;
		private BigDecimal R37_0_1Y_TOTAL;
		private BigDecimal R37_1_5Y_FT;
		private BigDecimal R37_1_5Y_HTM;
		private BigDecimal R37_1_5Y_TOTAL;
		private BigDecimal R37_O5Y_FT;
		private BigDecimal R37_O5Y_HTM;
		private BigDecimal R37_O5Y_TOTAL;
		private BigDecimal R37_T_FT;
		private BigDecimal R37_T_HTM;
		private BigDecimal R37_T_TOTAL;

		private String R38_PRODUCT;
		private BigDecimal R38_0_1Y_FT;
		private BigDecimal R38_0_1Y_HTM;
		private BigDecimal R38_0_1Y_TOTAL;
		private BigDecimal R38_1_5Y_FT;
		private BigDecimal R38_1_5Y_HTM;
		private BigDecimal R38_1_5Y_TOTAL;
		private BigDecimal R38_O5Y_FT;
		private BigDecimal R38_O5Y_HTM;
		private BigDecimal R38_O5Y_TOTAL;
		private BigDecimal R38_T_FT;
		private BigDecimal R38_T_HTM;
		private BigDecimal R38_T_TOTAL;

		private String R39_PRODUCT;
		private BigDecimal R39_0_1Y_FT;
		private BigDecimal R39_0_1Y_HTM;
		private BigDecimal R39_0_1Y_TOTAL;
		private BigDecimal R39_1_5Y_FT;
		private BigDecimal R39_1_5Y_HTM;
		private BigDecimal R39_1_5Y_TOTAL;
		private BigDecimal R39_O5Y_FT;
		private BigDecimal R39_O5Y_HTM;
		private BigDecimal R39_O5Y_TOTAL;
		private BigDecimal R39_T_FT;
		private BigDecimal R39_T_HTM;
		private BigDecimal R39_T_TOTAL;

		private String R40_PRODUCT;
		private BigDecimal R40_0_1Y_FT;
		private BigDecimal R40_0_1Y_HTM;
		private BigDecimal R40_0_1Y_TOTAL;
		private BigDecimal R40_1_5Y_FT;
		private BigDecimal R40_1_5Y_HTM;
		private BigDecimal R40_1_5Y_TOTAL;
		private BigDecimal R40_O5Y_FT;
		private BigDecimal R40_O5Y_HTM;
		private BigDecimal R40_O5Y_TOTAL;
		private BigDecimal R40_T_FT;
		private BigDecimal R40_T_HTM;
		private BigDecimal R40_T_TOTAL;

		private String R41_PRODUCT;
		private BigDecimal R41_0_1Y_FT;
		private BigDecimal R41_0_1Y_HTM;
		private BigDecimal R41_0_1Y_TOTAL;
		private BigDecimal R41_1_5Y_FT;
		private BigDecimal R41_1_5Y_HTM;
		private BigDecimal R41_1_5Y_TOTAL;
		private BigDecimal R41_O5Y_FT;
		private BigDecimal R41_O5Y_HTM;
		private BigDecimal R41_O5Y_TOTAL;
		private BigDecimal R41_T_FT;
		private BigDecimal R41_T_HTM;
		private BigDecimal R41_T_TOTAL;

		private String R42_PRODUCT;
		private BigDecimal R42_0_1Y_FT;
		private BigDecimal R42_0_1Y_HTM;
		private BigDecimal R42_0_1Y_TOTAL;
		private BigDecimal R42_1_5Y_FT;
		private BigDecimal R42_1_5Y_HTM;
		private BigDecimal R42_1_5Y_TOTAL;
		private BigDecimal R42_O5Y_FT;
		private BigDecimal R42_O5Y_HTM;
		private BigDecimal R42_O5Y_TOTAL;
		private BigDecimal R42_T_FT;
		private BigDecimal R42_T_HTM;
		private BigDecimal R42_T_TOTAL;

		private String R43_PRODUCT;
		private BigDecimal R43_0_1Y_FT;
		private BigDecimal R43_0_1Y_HTM;
		private BigDecimal R43_0_1Y_TOTAL;
		private BigDecimal R43_1_5Y_FT;
		private BigDecimal R43_1_5Y_HTM;
		private BigDecimal R43_1_5Y_TOTAL;
		private BigDecimal R43_O5Y_FT;
		private BigDecimal R43_O5Y_HTM;
		private BigDecimal R43_O5Y_TOTAL;
		private BigDecimal R43_T_FT;
		private BigDecimal R43_T_HTM;
		private BigDecimal R43_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_Archival_Detail4_Entity() {
			super();
		}

		// Getters and Setters for R36
		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_0_1Y_FT() {
			return R36_0_1Y_FT;
		}

		public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
			R36_0_1Y_FT = r36_0_1y_FT;
		}

		public BigDecimal getR36_0_1Y_HTM() {
			return R36_0_1Y_HTM;
		}

		public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
			R36_0_1Y_HTM = r36_0_1y_HTM;
		}

		public BigDecimal getR36_0_1Y_TOTAL() {
			return R36_0_1Y_TOTAL;
		}

		public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
			R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
		}

		public BigDecimal getR36_1_5Y_FT() {
			return R36_1_5Y_FT;
		}

		public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
			R36_1_5Y_FT = r36_1_5y_FT;
		}

		public BigDecimal getR36_1_5Y_HTM() {
			return R36_1_5Y_HTM;
		}

		public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
			R36_1_5Y_HTM = r36_1_5y_HTM;
		}

		public BigDecimal getR36_1_5Y_TOTAL() {
			return R36_1_5Y_TOTAL;
		}

		public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
			R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
		}

		public BigDecimal getR36_O5Y_FT() {
			return R36_O5Y_FT;
		}

		public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
			R36_O5Y_FT = r36_O5Y_FT;
		}

		public BigDecimal getR36_O5Y_HTM() {
			return R36_O5Y_HTM;
		}

		public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
			R36_O5Y_HTM = r36_O5Y_HTM;
		}

		public BigDecimal getR36_O5Y_TOTAL() {
			return R36_O5Y_TOTAL;
		}

		public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
			R36_O5Y_TOTAL = r36_O5Y_TOTAL;
		}

		public BigDecimal getR36_T_FT() {
			return R36_T_FT;
		}

		public void setR36_T_FT(BigDecimal r36_T_FT) {
			R36_T_FT = r36_T_FT;
		}

		public BigDecimal getR36_T_HTM() {
			return R36_T_HTM;
		}

		public void setR36_T_HTM(BigDecimal r36_T_HTM) {
			R36_T_HTM = r36_T_HTM;
		}

		public BigDecimal getR36_T_TOTAL() {
			return R36_T_TOTAL;
		}

		public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
			R36_T_TOTAL = r36_T_TOTAL;
		}

		// Getters and Setters for R37
		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_0_1Y_FT() {
			return R37_0_1Y_FT;
		}

		public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
			R37_0_1Y_FT = r37_0_1y_FT;
		}

		public BigDecimal getR37_0_1Y_HTM() {
			return R37_0_1Y_HTM;
		}

		public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
			R37_0_1Y_HTM = r37_0_1y_HTM;
		}

		public BigDecimal getR37_0_1Y_TOTAL() {
			return R37_0_1Y_TOTAL;
		}

		public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
			R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
		}

		public BigDecimal getR37_1_5Y_FT() {
			return R37_1_5Y_FT;
		}

		public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
			R37_1_5Y_FT = r37_1_5y_FT;
		}

		public BigDecimal getR37_1_5Y_HTM() {
			return R37_1_5Y_HTM;
		}

		public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
			R37_1_5Y_HTM = r37_1_5y_HTM;
		}

		public BigDecimal getR37_1_5Y_TOTAL() {
			return R37_1_5Y_TOTAL;
		}

		public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
			R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
		}

		public BigDecimal getR37_O5Y_FT() {
			return R37_O5Y_FT;
		}

		public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
			R37_O5Y_FT = r37_O5Y_FT;
		}

		public BigDecimal getR37_O5Y_HTM() {
			return R37_O5Y_HTM;
		}

		public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
			R37_O5Y_HTM = r37_O5Y_HTM;
		}

		public BigDecimal getR37_O5Y_TOTAL() {
			return R37_O5Y_TOTAL;
		}

		public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
			R37_O5Y_TOTAL = r37_O5Y_TOTAL;
		}

		public BigDecimal getR37_T_FT() {
			return R37_T_FT;
		}

		public void setR37_T_FT(BigDecimal r37_T_FT) {
			R37_T_FT = r37_T_FT;
		}

		public BigDecimal getR37_T_HTM() {
			return R37_T_HTM;
		}

		public void setR37_T_HTM(BigDecimal r37_T_HTM) {
			R37_T_HTM = r37_T_HTM;
		}

		public BigDecimal getR37_T_TOTAL() {
			return R37_T_TOTAL;
		}

		public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
			R37_T_TOTAL = r37_T_TOTAL;
		}

		// Getters and Setters for R38
		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_0_1Y_FT() {
			return R38_0_1Y_FT;
		}

		public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
			R38_0_1Y_FT = r38_0_1y_FT;
		}

		public BigDecimal getR38_0_1Y_HTM() {
			return R38_0_1Y_HTM;
		}

		public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
			R38_0_1Y_HTM = r38_0_1y_HTM;
		}

		public BigDecimal getR38_0_1Y_TOTAL() {
			return R38_0_1Y_TOTAL;
		}

		public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
			R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
		}

		public BigDecimal getR38_1_5Y_FT() {
			return R38_1_5Y_FT;
		}

		public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
			R38_1_5Y_FT = r38_1_5y_FT;
		}

		public BigDecimal getR38_1_5Y_HTM() {
			return R38_1_5Y_HTM;
		}

		public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
			R38_1_5Y_HTM = r38_1_5y_HTM;
		}

		public BigDecimal getR38_1_5Y_TOTAL() {
			return R38_1_5Y_TOTAL;
		}

		public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
			R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
		}

		public BigDecimal getR38_O5Y_FT() {
			return R38_O5Y_FT;
		}

		public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
			R38_O5Y_FT = r38_O5Y_FT;
		}

		public BigDecimal getR38_O5Y_HTM() {
			return R38_O5Y_HTM;
		}

		public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
			R38_O5Y_HTM = r38_O5Y_HTM;
		}

		public BigDecimal getR38_O5Y_TOTAL() {
			return R38_O5Y_TOTAL;
		}

		public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
			R38_O5Y_TOTAL = r38_O5Y_TOTAL;
		}

		public BigDecimal getR38_T_FT() {
			return R38_T_FT;
		}

		public void setR38_T_FT(BigDecimal r38_T_FT) {
			R38_T_FT = r38_T_FT;
		}

		public BigDecimal getR38_T_HTM() {
			return R38_T_HTM;
		}

		public void setR38_T_HTM(BigDecimal r38_T_HTM) {
			R38_T_HTM = r38_T_HTM;
		}

		public BigDecimal getR38_T_TOTAL() {
			return R38_T_TOTAL;
		}

		public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
			R38_T_TOTAL = r38_T_TOTAL;
		}

		// Getters and Setters for R39
		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_0_1Y_FT() {
			return R39_0_1Y_FT;
		}

		public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
			R39_0_1Y_FT = r39_0_1y_FT;
		}

		public BigDecimal getR39_0_1Y_HTM() {
			return R39_0_1Y_HTM;
		}

		public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
			R39_0_1Y_HTM = r39_0_1y_HTM;
		}

		public BigDecimal getR39_0_1Y_TOTAL() {
			return R39_0_1Y_TOTAL;
		}

		public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
			R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
		}

		public BigDecimal getR39_1_5Y_FT() {
			return R39_1_5Y_FT;
		}

		public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
			R39_1_5Y_FT = r39_1_5y_FT;
		}

		public BigDecimal getR39_1_5Y_HTM() {
			return R39_1_5Y_HTM;
		}

		public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
			R39_1_5Y_HTM = r39_1_5y_HTM;
		}

		public BigDecimal getR39_1_5Y_TOTAL() {
			return R39_1_5Y_TOTAL;
		}

		public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
			R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
		}

		public BigDecimal getR39_O5Y_FT() {
			return R39_O5Y_FT;
		}

		public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
			R39_O5Y_FT = r39_O5Y_FT;
		}

		public BigDecimal getR39_O5Y_HTM() {
			return R39_O5Y_HTM;
		}

		public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
			R39_O5Y_HTM = r39_O5Y_HTM;
		}

		public BigDecimal getR39_O5Y_TOTAL() {
			return R39_O5Y_TOTAL;
		}

		public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
			R39_O5Y_TOTAL = r39_O5Y_TOTAL;
		}

		public BigDecimal getR39_T_FT() {
			return R39_T_FT;
		}

		public void setR39_T_FT(BigDecimal r39_T_FT) {
			R39_T_FT = r39_T_FT;
		}

		public BigDecimal getR39_T_HTM() {
			return R39_T_HTM;
		}

		public void setR39_T_HTM(BigDecimal r39_T_HTM) {
			R39_T_HTM = r39_T_HTM;
		}

		public BigDecimal getR39_T_TOTAL() {
			return R39_T_TOTAL;
		}

		public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
			R39_T_TOTAL = r39_T_TOTAL;
		}

		// Getters and Setters for R40
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_0_1Y_FT() {
			return R40_0_1Y_FT;
		}

		public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
			R40_0_1Y_FT = r40_0_1y_FT;
		}

		public BigDecimal getR40_0_1Y_HTM() {
			return R40_0_1Y_HTM;
		}

		public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
			R40_0_1Y_HTM = r40_0_1y_HTM;
		}

		public BigDecimal getR40_0_1Y_TOTAL() {
			return R40_0_1Y_TOTAL;
		}

		public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
			R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
		}

		public BigDecimal getR40_1_5Y_FT() {
			return R40_1_5Y_FT;
		}

		public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
			R40_1_5Y_FT = r40_1_5y_FT;
		}

		public BigDecimal getR40_1_5Y_HTM() {
			return R40_1_5Y_HTM;
		}

		public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
			R40_1_5Y_HTM = r40_1_5y_HTM;
		}

		public BigDecimal getR40_1_5Y_TOTAL() {
			return R40_1_5Y_TOTAL;
		}

		public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
			R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
		}

		public BigDecimal getR40_O5Y_FT() {
			return R40_O5Y_FT;
		}

		public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
			R40_O5Y_FT = r40_O5Y_FT;
		}

		public BigDecimal getR40_O5Y_HTM() {
			return R40_O5Y_HTM;
		}

		public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
			R40_O5Y_HTM = r40_O5Y_HTM;
		}

		public BigDecimal getR40_O5Y_TOTAL() {
			return R40_O5Y_TOTAL;
		}

		public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
			R40_O5Y_TOTAL = r40_O5Y_TOTAL;
		}

		public BigDecimal getR40_T_FT() {
			return R40_T_FT;
		}

		public void setR40_T_FT(BigDecimal r40_T_FT) {
			R40_T_FT = r40_T_FT;
		}

		public BigDecimal getR40_T_HTM() {
			return R40_T_HTM;
		}

		public void setR40_T_HTM(BigDecimal r40_T_HTM) {
			R40_T_HTM = r40_T_HTM;
		}

		public BigDecimal getR40_T_TOTAL() {
			return R40_T_TOTAL;
		}

		public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
			R40_T_TOTAL = r40_T_TOTAL;
		}

		// Getters and Setters for R41
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_0_1Y_FT() {
			return R41_0_1Y_FT;
		}

		public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
			R41_0_1Y_FT = r41_0_1y_FT;
		}

		public BigDecimal getR41_0_1Y_HTM() {
			return R41_0_1Y_HTM;
		}

		public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
			R41_0_1Y_HTM = r41_0_1y_HTM;
		}

		public BigDecimal getR41_0_1Y_TOTAL() {
			return R41_0_1Y_TOTAL;
		}

		public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
			R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
		}

		public BigDecimal getR41_1_5Y_FT() {
			return R41_1_5Y_FT;
		}

		public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
			R41_1_5Y_FT = r41_1_5y_FT;
		}

		public BigDecimal getR41_1_5Y_HTM() {
			return R41_1_5Y_HTM;
		}

		public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
			R41_1_5Y_HTM = r41_1_5y_HTM;
		}

		public BigDecimal getR41_1_5Y_TOTAL() {
			return R41_1_5Y_TOTAL;
		}

		public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
			R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
		}

		public BigDecimal getR41_O5Y_FT() {
			return R41_O5Y_FT;
		}

		public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
			R41_O5Y_FT = r41_O5Y_FT;
		}

		public BigDecimal getR41_O5Y_HTM() {
			return R41_O5Y_HTM;
		}

		public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
			R41_O5Y_HTM = r41_O5Y_HTM;
		}

		public BigDecimal getR41_O5Y_TOTAL() {
			return R41_O5Y_TOTAL;
		}

		public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
			R41_O5Y_TOTAL = r41_O5Y_TOTAL;
		}

		public BigDecimal getR41_T_FT() {
			return R41_T_FT;
		}

		public void setR41_T_FT(BigDecimal r41_T_FT) {
			R41_T_FT = r41_T_FT;
		}

		public BigDecimal getR41_T_HTM() {
			return R41_T_HTM;
		}

		public void setR41_T_HTM(BigDecimal r41_T_HTM) {
			R41_T_HTM = r41_T_HTM;
		}

		public BigDecimal getR41_T_TOTAL() {
			return R41_T_TOTAL;
		}

		public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
			R41_T_TOTAL = r41_T_TOTAL;
		}

		// Getters and Setters for R42
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_0_1Y_FT() {
			return R42_0_1Y_FT;
		}

		public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
			R42_0_1Y_FT = r42_0_1y_FT;
		}

		public BigDecimal getR42_0_1Y_HTM() {
			return R42_0_1Y_HTM;
		}

		public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
			R42_0_1Y_HTM = r42_0_1y_HTM;
		}

		public BigDecimal getR42_0_1Y_TOTAL() {
			return R42_0_1Y_TOTAL;
		}

		public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
			R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
		}

		public BigDecimal getR42_1_5Y_FT() {
			return R42_1_5Y_FT;
		}

		public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
			R42_1_5Y_FT = r42_1_5y_FT;
		}

		public BigDecimal getR42_1_5Y_HTM() {
			return R42_1_5Y_HTM;
		}

		public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
			R42_1_5Y_HTM = r42_1_5y_HTM;
		}

		public BigDecimal getR42_1_5Y_TOTAL() {
			return R42_1_5Y_TOTAL;
		}

		public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
			R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
		}

		public BigDecimal getR42_O5Y_FT() {
			return R42_O5Y_FT;
		}

		public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
			R42_O5Y_FT = r42_O5Y_FT;
		}

		public BigDecimal getR42_O5Y_HTM() {
			return R42_O5Y_HTM;
		}

		public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
			R42_O5Y_HTM = r42_O5Y_HTM;
		}

		public BigDecimal getR42_O5Y_TOTAL() {
			return R42_O5Y_TOTAL;
		}

		public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
			R42_O5Y_TOTAL = r42_O5Y_TOTAL;
		}

		public BigDecimal getR42_T_FT() {
			return R42_T_FT;
		}

		public void setR42_T_FT(BigDecimal r42_T_FT) {
			R42_T_FT = r42_T_FT;
		}

		public BigDecimal getR42_T_HTM() {
			return R42_T_HTM;
		}

		public void setR42_T_HTM(BigDecimal r42_T_HTM) {
			R42_T_HTM = r42_T_HTM;
		}

		public BigDecimal getR42_T_TOTAL() {
			return R42_T_TOTAL;
		}

		public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
			R42_T_TOTAL = r42_T_TOTAL;
		}

		// Getters and Setters for R43
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public BigDecimal getR43_0_1Y_FT() {
			return R43_0_1Y_FT;
		}

		public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
			R43_0_1Y_FT = r43_0_1y_FT;
		}

		public BigDecimal getR43_0_1Y_HTM() {
			return R43_0_1Y_HTM;
		}

		public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
			R43_0_1Y_HTM = r43_0_1y_HTM;
		}

		public BigDecimal getR43_0_1Y_TOTAL() {
			return R43_0_1Y_TOTAL;
		}

		public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
			R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
		}

		public BigDecimal getR43_1_5Y_FT() {
			return R43_1_5Y_FT;
		}

		public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
			R43_1_5Y_FT = r43_1_5y_FT;
		}

		public BigDecimal getR43_1_5Y_HTM() {
			return R43_1_5Y_HTM;
		}

		public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
			R43_1_5Y_HTM = r43_1_5y_HTM;
		}

		public BigDecimal getR43_1_5Y_TOTAL() {
			return R43_1_5Y_TOTAL;
		}

		public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
			R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
		}

		public BigDecimal getR43_O5Y_FT() {
			return R43_O5Y_FT;
		}

		public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
			R43_O5Y_FT = r43_O5Y_FT;
		}

		public BigDecimal getR43_O5Y_HTM() {
			return R43_O5Y_HTM;
		}

		public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
			R43_O5Y_HTM = r43_O5Y_HTM;
		}

		public BigDecimal getR43_O5Y_TOTAL() {
			return R43_O5Y_TOTAL;
		}

		public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
			R43_O5Y_TOTAL = r43_O5Y_TOTAL;
		}

		public BigDecimal getR43_T_FT() {
			return R43_T_FT;
		}

		public void setR43_T_FT(BigDecimal r43_T_FT) {
			R43_T_FT = r43_T_FT;
		}

		public BigDecimal getR43_T_HTM() {
			return R43_T_HTM;
		}

		public void setR43_T_HTM(BigDecimal r43_T_HTM) {
			R43_T_HTM = r43_T_HTM;
		}

		public BigDecimal getR43_T_TOTAL() {
			return R43_T_TOTAL;
		}

		public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
			R43_T_TOTAL = r43_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Summary_RowMapper1
//===========================

	public class M_SEC_RESUB_Summary_RowMapper1 implements RowMapper<M_SEC_RESUB_Summary_Entity1> {

		@Override
		public M_SEC_RESUB_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Summary_Entity1 obj = new M_SEC_RESUB_Summary_Entity1();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA(rs.getBigDecimal("R11_TCA"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA(rs.getBigDecimal("R12_TCA"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA(rs.getBigDecimal("R13_TCA"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA(rs.getBigDecimal("R14_TCA"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA(rs.getBigDecimal("R15_TCA"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA(rs.getBigDecimal("R16_TCA"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TCA(rs.getBigDecimal("R17_TCA"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TCA(rs.getBigDecimal("R18_TCA"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TCA(rs.getBigDecimal("R19_TCA"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Summary_Entity1
//===========================

	public class M_SEC_RESUB_Summary_Entity1 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA;
		private String R17_PRODUCT;
		private BigDecimal R17_TCA;
		private String R18_PRODUCT;
		private BigDecimal R18_TCA;
		private String R19_PRODUCT;
		private BigDecimal R19_TCA;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Summary_Entity1() {
			super();
		}

		// Getters and Setters
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA() {
			return R11_TCA;
		}

		public void setR11_TCA(BigDecimal r11_TCA) {
			R11_TCA = r11_TCA;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA() {
			return R12_TCA;
		}

		public void setR12_TCA(BigDecimal r12_TCA) {
			R12_TCA = r12_TCA;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA() {
			return R13_TCA;
		}

		public void setR13_TCA(BigDecimal r13_TCA) {
			R13_TCA = r13_TCA;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA() {
			return R14_TCA;
		}

		public void setR14_TCA(BigDecimal r14_TCA) {
			R14_TCA = r14_TCA;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA() {
			return R15_TCA;
		}

		public void setR15_TCA(BigDecimal r15_TCA) {
			R15_TCA = r15_TCA;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA() {
			return R16_TCA;
		}

		public void setR16_TCA(BigDecimal r16_TCA) {
			R16_TCA = r16_TCA;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TCA() {
			return R17_TCA;
		}

		public void setR17_TCA(BigDecimal r17_TCA) {
			R17_TCA = r17_TCA;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_TCA() {
			return R18_TCA;
		}

		public void setR18_TCA(BigDecimal r18_TCA) {
			R18_TCA = r18_TCA;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_TCA() {
			return R19_TCA;
		}

		public void setR19_TCA(BigDecimal r19_TCA) {
			R19_TCA = r19_TCA;
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Summary_RowMapper2
//===========================

	public class M_SEC_RESUB_Summary_RowMapper2 implements RowMapper<M_SEC_RESUB_Summary_Entity2> {

		@Override
		public M_SEC_RESUB_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Summary_Entity2 obj = new M_SEC_RESUB_Summary_Entity2();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA2(rs.getBigDecimal("R11_TCA2"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA2(rs.getBigDecimal("R12_TCA2"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA2(rs.getBigDecimal("R13_TCA2"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA2(rs.getBigDecimal("R14_TCA2"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA2(rs.getBigDecimal("R15_TCA2"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA2(rs.getBigDecimal("R16_TCA2"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Summary_Entity2
//===========================

	public class M_SEC_RESUB_Summary_Entity2 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA2;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA2;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA2;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA2;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA2;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA2;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Summary_Entity2() {
			super();
		}

		// Getters and Setters
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA2() {
			return R11_TCA2;
		}

		public void setR11_TCA2(BigDecimal r11_TCA2) {
			R11_TCA2 = r11_TCA2;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA2() {
			return R12_TCA2;
		}

		public void setR12_TCA2(BigDecimal r12_TCA2) {
			R12_TCA2 = r12_TCA2;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA2() {
			return R13_TCA2;
		}

		public void setR13_TCA2(BigDecimal r13_TCA2) {
			R13_TCA2 = r13_TCA2;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA2() {
			return R14_TCA2;
		}

		public void setR14_TCA2(BigDecimal r14_TCA2) {
			R14_TCA2 = r14_TCA2;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA2() {
			return R15_TCA2;
		}

		public void setR15_TCA2(BigDecimal r15_TCA2) {
			R15_TCA2 = r15_TCA2;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA2() {
			return R16_TCA2;
		}

		public void setR16_TCA2(BigDecimal r16_TCA2) {
			R16_TCA2 = r16_TCA2;
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Summary_RowMapper3
//===========================

	public class M_SEC_RESUB_Summary_RowMapper3 implements RowMapper<M_SEC_RESUB_Summary_Entity3> {

		@Override
		public M_SEC_RESUB_Summary_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Summary_Entity3 obj = new M_SEC_RESUB_Summary_Entity3();

			// =========================
			// R26
			// =========================
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_0_1Y_FT(rs.getBigDecimal("R26_0_1Y_FT"));
			obj.setR26_0_1Y_HTM(rs.getBigDecimal("R26_0_1Y_HTM"));
			obj.setR26_0_1Y_TOTAL(rs.getBigDecimal("R26_0_1Y_TOTAL"));
			obj.setR26_1_5Y_FT(rs.getBigDecimal("R26_1_5Y_FT"));
			obj.setR26_1_5Y_HTM(rs.getBigDecimal("R26_1_5Y_HTM"));
			obj.setR26_1_5Y_TOTAL(rs.getBigDecimal("R26_1_5Y_TOTAL"));
			obj.setR26_O5Y_FT(rs.getBigDecimal("R26_O5Y_FT"));
			obj.setR26_O5Y_HTM(rs.getBigDecimal("R26_O5Y_HTM"));
			obj.setR26_O5Y_TOTAL(rs.getBigDecimal("R26_O5Y_TOTAL"));
			obj.setR26_T_FT(rs.getBigDecimal("R26_T_FT"));
			obj.setR26_T_HTM(rs.getBigDecimal("R26_T_HTM"));
			obj.setR26_T_TOTAL(rs.getBigDecimal("R26_T_TOTAL"));

			// =========================
			// R27
			// =========================
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_0_1Y_FT(rs.getBigDecimal("R27_0_1Y_FT"));
			obj.setR27_0_1Y_HTM(rs.getBigDecimal("R27_0_1Y_HTM"));
			obj.setR27_0_1Y_TOTAL(rs.getBigDecimal("R27_0_1Y_TOTAL"));
			obj.setR27_1_5Y_FT(rs.getBigDecimal("R27_1_5Y_FT"));
			obj.setR27_1_5Y_HTM(rs.getBigDecimal("R27_1_5Y_HTM"));
			obj.setR27_1_5Y_TOTAL(rs.getBigDecimal("R27_1_5Y_TOTAL"));
			obj.setR27_O5Y_FT(rs.getBigDecimal("R27_O5Y_FT"));
			obj.setR27_O5Y_HTM(rs.getBigDecimal("R27_O5Y_HTM"));
			obj.setR27_O5Y_TOTAL(rs.getBigDecimal("R27_O5Y_TOTAL"));
			obj.setR27_T_FT(rs.getBigDecimal("R27_T_FT"));
			obj.setR27_T_HTM(rs.getBigDecimal("R27_T_HTM"));
			obj.setR27_T_TOTAL(rs.getBigDecimal("R27_T_TOTAL"));

			// =========================
			// R28
			// =========================
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_0_1Y_FT(rs.getBigDecimal("R28_0_1Y_FT"));
			obj.setR28_0_1Y_HTM(rs.getBigDecimal("R28_0_1Y_HTM"));
			obj.setR28_0_1Y_TOTAL(rs.getBigDecimal("R28_0_1Y_TOTAL"));
			obj.setR28_1_5Y_FT(rs.getBigDecimal("R28_1_5Y_FT"));
			obj.setR28_1_5Y_HTM(rs.getBigDecimal("R28_1_5Y_HTM"));
			obj.setR28_1_5Y_TOTAL(rs.getBigDecimal("R28_1_5Y_TOTAL"));
			obj.setR28_O5Y_FT(rs.getBigDecimal("R28_O5Y_FT"));
			obj.setR28_O5Y_HTM(rs.getBigDecimal("R28_O5Y_HTM"));
			obj.setR28_O5Y_TOTAL(rs.getBigDecimal("R28_O5Y_TOTAL"));
			obj.setR28_T_FT(rs.getBigDecimal("R28_T_FT"));
			obj.setR28_T_HTM(rs.getBigDecimal("R28_T_HTM"));
			obj.setR28_T_TOTAL(rs.getBigDecimal("R28_T_TOTAL"));

			// =========================
			// R29
			// =========================
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_0_1Y_FT(rs.getBigDecimal("R29_0_1Y_FT"));
			obj.setR29_0_1Y_HTM(rs.getBigDecimal("R29_0_1Y_HTM"));
			obj.setR29_0_1Y_TOTAL(rs.getBigDecimal("R29_0_1Y_TOTAL"));
			obj.setR29_1_5Y_FT(rs.getBigDecimal("R29_1_5Y_FT"));
			obj.setR29_1_5Y_HTM(rs.getBigDecimal("R29_1_5Y_HTM"));
			obj.setR29_1_5Y_TOTAL(rs.getBigDecimal("R29_1_5Y_TOTAL"));
			obj.setR29_O5Y_FT(rs.getBigDecimal("R29_O5Y_FT"));
			obj.setR29_O5Y_HTM(rs.getBigDecimal("R29_O5Y_HTM"));
			obj.setR29_O5Y_TOTAL(rs.getBigDecimal("R29_O5Y_TOTAL"));
			obj.setR29_T_FT(rs.getBigDecimal("R29_T_FT"));
			obj.setR29_T_HTM(rs.getBigDecimal("R29_T_HTM"));
			obj.setR29_T_TOTAL(rs.getBigDecimal("R29_T_TOTAL"));

			// =========================
			// R30
			// =========================
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_0_1Y_FT(rs.getBigDecimal("R30_0_1Y_FT"));
			obj.setR30_0_1Y_HTM(rs.getBigDecimal("R30_0_1Y_HTM"));
			obj.setR30_0_1Y_TOTAL(rs.getBigDecimal("R30_0_1Y_TOTAL"));
			obj.setR30_1_5Y_FT(rs.getBigDecimal("R30_1_5Y_FT"));
			obj.setR30_1_5Y_HTM(rs.getBigDecimal("R30_1_5Y_HTM"));
			obj.setR30_1_5Y_TOTAL(rs.getBigDecimal("R30_1_5Y_TOTAL"));
			obj.setR30_O5Y_FT(rs.getBigDecimal("R30_O5Y_FT"));
			obj.setR30_O5Y_HTM(rs.getBigDecimal("R30_O5Y_HTM"));
			obj.setR30_O5Y_TOTAL(rs.getBigDecimal("R30_O5Y_TOTAL"));
			obj.setR30_T_FT(rs.getBigDecimal("R30_T_FT"));
			obj.setR30_T_HTM(rs.getBigDecimal("R30_T_HTM"));
			obj.setR30_T_TOTAL(rs.getBigDecimal("R30_T_TOTAL"));

			// =========================
			// R31
			// =========================
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_0_1Y_FT(rs.getBigDecimal("R31_0_1Y_FT"));
			obj.setR31_0_1Y_HTM(rs.getBigDecimal("R31_0_1Y_HTM"));
			obj.setR31_0_1Y_TOTAL(rs.getBigDecimal("R31_0_1Y_TOTAL"));
			obj.setR31_1_5Y_FT(rs.getBigDecimal("R31_1_5Y_FT"));
			obj.setR31_1_5Y_HTM(rs.getBigDecimal("R31_1_5Y_HTM"));
			obj.setR31_1_5Y_TOTAL(rs.getBigDecimal("R31_1_5Y_TOTAL"));
			obj.setR31_O5Y_FT(rs.getBigDecimal("R31_O5Y_FT"));
			obj.setR31_O5Y_HTM(rs.getBigDecimal("R31_O5Y_HTM"));
			obj.setR31_O5Y_TOTAL(rs.getBigDecimal("R31_O5Y_TOTAL"));
			obj.setR31_T_FT(rs.getBigDecimal("R31_T_FT"));
			obj.setR31_T_HTM(rs.getBigDecimal("R31_T_HTM"));
			obj.setR31_T_TOTAL(rs.getBigDecimal("R31_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Summary_Entity3
//===========================

	public class M_SEC_RESUB_Summary_Entity3 {

		private String R26_PRODUCT;
		private BigDecimal R26_0_1Y_FT;
		private BigDecimal R26_0_1Y_HTM;
		private BigDecimal R26_0_1Y_TOTAL;
		private BigDecimal R26_1_5Y_FT;
		private BigDecimal R26_1_5Y_HTM;
		private BigDecimal R26_1_5Y_TOTAL;
		private BigDecimal R26_O5Y_FT;
		private BigDecimal R26_O5Y_HTM;
		private BigDecimal R26_O5Y_TOTAL;
		private BigDecimal R26_T_FT;
		private BigDecimal R26_T_HTM;
		private BigDecimal R26_T_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_0_1Y_FT;
		private BigDecimal R27_0_1Y_HTM;
		private BigDecimal R27_0_1Y_TOTAL;
		private BigDecimal R27_1_5Y_FT;
		private BigDecimal R27_1_5Y_HTM;
		private BigDecimal R27_1_5Y_TOTAL;
		private BigDecimal R27_O5Y_FT;
		private BigDecimal R27_O5Y_HTM;
		private BigDecimal R27_O5Y_TOTAL;
		private BigDecimal R27_T_FT;
		private BigDecimal R27_T_HTM;
		private BigDecimal R27_T_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_0_1Y_FT;
		private BigDecimal R28_0_1Y_HTM;
		private BigDecimal R28_0_1Y_TOTAL;
		private BigDecimal R28_1_5Y_FT;
		private BigDecimal R28_1_5Y_HTM;
		private BigDecimal R28_1_5Y_TOTAL;
		private BigDecimal R28_O5Y_FT;
		private BigDecimal R28_O5Y_HTM;
		private BigDecimal R28_O5Y_TOTAL;
		private BigDecimal R28_T_FT;
		private BigDecimal R28_T_HTM;
		private BigDecimal R28_T_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_0_1Y_FT;
		private BigDecimal R29_0_1Y_HTM;
		private BigDecimal R29_0_1Y_TOTAL;
		private BigDecimal R29_1_5Y_FT;
		private BigDecimal R29_1_5Y_HTM;
		private BigDecimal R29_1_5Y_TOTAL;
		private BigDecimal R29_O5Y_FT;
		private BigDecimal R29_O5Y_HTM;
		private BigDecimal R29_O5Y_TOTAL;
		private BigDecimal R29_T_FT;
		private BigDecimal R29_T_HTM;
		private BigDecimal R29_T_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_0_1Y_FT;
		private BigDecimal R30_0_1Y_HTM;
		private BigDecimal R30_0_1Y_TOTAL;
		private BigDecimal R30_1_5Y_FT;
		private BigDecimal R30_1_5Y_HTM;
		private BigDecimal R30_1_5Y_TOTAL;
		private BigDecimal R30_O5Y_FT;
		private BigDecimal R30_O5Y_HTM;
		private BigDecimal R30_O5Y_TOTAL;
		private BigDecimal R30_T_FT;
		private BigDecimal R30_T_HTM;
		private BigDecimal R30_T_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_0_1Y_FT;
		private BigDecimal R31_0_1Y_HTM;
		private BigDecimal R31_0_1Y_TOTAL;
		private BigDecimal R31_1_5Y_FT;
		private BigDecimal R31_1_5Y_HTM;
		private BigDecimal R31_1_5Y_TOTAL;
		private BigDecimal R31_O5Y_FT;
		private BigDecimal R31_O5Y_HTM;
		private BigDecimal R31_O5Y_TOTAL;
		private BigDecimal R31_T_FT;
		private BigDecimal R31_T_HTM;
		private BigDecimal R31_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Summary_Entity3() {
			super();
		}

		// Getters and Setters for R26
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}

		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}

		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}

		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}

		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}

		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}

		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}

		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}

		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}

		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}

		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}

		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}

		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}

		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}

		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}

		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}

		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}

		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}

		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}

		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}

		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}

		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}

		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}

		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}

		// Getters and Setters for R27
		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}

		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}

		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}

		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}

		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}

		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}

		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}

		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}

		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}

		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}

		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}

		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}

		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}

		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}

		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}

		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}

		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}

		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}

		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}

		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}

		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}

		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}

		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}

		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}

		// Getters and Setters for R28
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}

		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}

		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}

		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}

		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}

		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}

		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}

		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}

		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}

		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}

		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}

		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}

		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}

		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}

		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}

		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}

		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}

		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}

		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}

		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}

		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}

		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}

		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}

		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}

		// Getters and Setters for R29
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}

		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}

		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}

		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}

		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}

		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}

		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}

		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}

		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}

		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}

		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}

		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}

		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}

		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}

		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}

		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}

		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}

		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}

		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}

		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}

		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}

		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}

		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}

		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}

		// Getters and Setters for R30
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}

		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}

		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}

		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}

		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}

		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}

		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}

		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}

		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}

		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}

		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}

		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}

		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}

		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}

		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}

		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}

		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}

		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}

		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}

		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}

		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}

		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}

		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}

		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}

		// Getters and Setters for R31
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}

		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}

		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}

		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}

		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}

		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}

		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}

		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}

		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}

		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}

		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}

		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}

		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}

		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}

		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}

		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}

		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}

		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}

		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}

		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}

		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}

		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}

		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}

		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Summary_RowMapper4
//===========================

	public class M_SEC_RESUB_Summary_RowMapper4 implements RowMapper<M_SEC_RESUB_Summary_Entity4> {

		@Override
		public M_SEC_RESUB_Summary_Entity4 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Summary_Entity4 obj = new M_SEC_RESUB_Summary_Entity4();

			// =========================
			// R36
			// =========================
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_0_1Y_FT(rs.getBigDecimal("R36_0_1Y_FT"));
			obj.setR36_0_1Y_HTM(rs.getBigDecimal("R36_0_1Y_HTM"));
			obj.setR36_0_1Y_TOTAL(rs.getBigDecimal("R36_0_1Y_TOTAL"));
			obj.setR36_1_5Y_FT(rs.getBigDecimal("R36_1_5Y_FT"));
			obj.setR36_1_5Y_HTM(rs.getBigDecimal("R36_1_5Y_HTM"));
			obj.setR36_1_5Y_TOTAL(rs.getBigDecimal("R36_1_5Y_TOTAL"));
			obj.setR36_O5Y_FT(rs.getBigDecimal("R36_O5Y_FT"));
			obj.setR36_O5Y_HTM(rs.getBigDecimal("R36_O5Y_HTM"));
			obj.setR36_O5Y_TOTAL(rs.getBigDecimal("R36_O5Y_TOTAL"));
			obj.setR36_T_FT(rs.getBigDecimal("R36_T_FT"));
			obj.setR36_T_HTM(rs.getBigDecimal("R36_T_HTM"));
			obj.setR36_T_TOTAL(rs.getBigDecimal("R36_T_TOTAL"));

			// =========================
			// R37
			// =========================
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_0_1Y_FT(rs.getBigDecimal("R37_0_1Y_FT"));
			obj.setR37_0_1Y_HTM(rs.getBigDecimal("R37_0_1Y_HTM"));
			obj.setR37_0_1Y_TOTAL(rs.getBigDecimal("R37_0_1Y_TOTAL"));
			obj.setR37_1_5Y_FT(rs.getBigDecimal("R37_1_5Y_FT"));
			obj.setR37_1_5Y_HTM(rs.getBigDecimal("R37_1_5Y_HTM"));
			obj.setR37_1_5Y_TOTAL(rs.getBigDecimal("R37_1_5Y_TOTAL"));
			obj.setR37_O5Y_FT(rs.getBigDecimal("R37_O5Y_FT"));
			obj.setR37_O5Y_HTM(rs.getBigDecimal("R37_O5Y_HTM"));
			obj.setR37_O5Y_TOTAL(rs.getBigDecimal("R37_O5Y_TOTAL"));
			obj.setR37_T_FT(rs.getBigDecimal("R37_T_FT"));
			obj.setR37_T_HTM(rs.getBigDecimal("R37_T_HTM"));
			obj.setR37_T_TOTAL(rs.getBigDecimal("R37_T_TOTAL"));

			// =========================
			// R38
			// =========================
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_0_1Y_FT(rs.getBigDecimal("R38_0_1Y_FT"));
			obj.setR38_0_1Y_HTM(rs.getBigDecimal("R38_0_1Y_HTM"));
			obj.setR38_0_1Y_TOTAL(rs.getBigDecimal("R38_0_1Y_TOTAL"));
			obj.setR38_1_5Y_FT(rs.getBigDecimal("R38_1_5Y_FT"));
			obj.setR38_1_5Y_HTM(rs.getBigDecimal("R38_1_5Y_HTM"));
			obj.setR38_1_5Y_TOTAL(rs.getBigDecimal("R38_1_5Y_TOTAL"));
			obj.setR38_O5Y_FT(rs.getBigDecimal("R38_O5Y_FT"));
			obj.setR38_O5Y_HTM(rs.getBigDecimal("R38_O5Y_HTM"));
			obj.setR38_O5Y_TOTAL(rs.getBigDecimal("R38_O5Y_TOTAL"));
			obj.setR38_T_FT(rs.getBigDecimal("R38_T_FT"));
			obj.setR38_T_HTM(rs.getBigDecimal("R38_T_HTM"));
			obj.setR38_T_TOTAL(rs.getBigDecimal("R38_T_TOTAL"));

			// =========================
			// R39
			// =========================
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_0_1Y_FT(rs.getBigDecimal("R39_0_1Y_FT"));
			obj.setR39_0_1Y_HTM(rs.getBigDecimal("R39_0_1Y_HTM"));
			obj.setR39_0_1Y_TOTAL(rs.getBigDecimal("R39_0_1Y_TOTAL"));
			obj.setR39_1_5Y_FT(rs.getBigDecimal("R39_1_5Y_FT"));
			obj.setR39_1_5Y_HTM(rs.getBigDecimal("R39_1_5Y_HTM"));
			obj.setR39_1_5Y_TOTAL(rs.getBigDecimal("R39_1_5Y_TOTAL"));
			obj.setR39_O5Y_FT(rs.getBigDecimal("R39_O5Y_FT"));
			obj.setR39_O5Y_HTM(rs.getBigDecimal("R39_O5Y_HTM"));
			obj.setR39_O5Y_TOTAL(rs.getBigDecimal("R39_O5Y_TOTAL"));
			obj.setR39_T_FT(rs.getBigDecimal("R39_T_FT"));
			obj.setR39_T_HTM(rs.getBigDecimal("R39_T_HTM"));
			obj.setR39_T_TOTAL(rs.getBigDecimal("R39_T_TOTAL"));

			// =========================
			// R40
			// =========================
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_0_1Y_FT(rs.getBigDecimal("R40_0_1Y_FT"));
			obj.setR40_0_1Y_HTM(rs.getBigDecimal("R40_0_1Y_HTM"));
			obj.setR40_0_1Y_TOTAL(rs.getBigDecimal("R40_0_1Y_TOTAL"));
			obj.setR40_1_5Y_FT(rs.getBigDecimal("R40_1_5Y_FT"));
			obj.setR40_1_5Y_HTM(rs.getBigDecimal("R40_1_5Y_HTM"));
			obj.setR40_1_5Y_TOTAL(rs.getBigDecimal("R40_1_5Y_TOTAL"));
			obj.setR40_O5Y_FT(rs.getBigDecimal("R40_O5Y_FT"));
			obj.setR40_O5Y_HTM(rs.getBigDecimal("R40_O5Y_HTM"));
			obj.setR40_O5Y_TOTAL(rs.getBigDecimal("R40_O5Y_TOTAL"));
			obj.setR40_T_FT(rs.getBigDecimal("R40_T_FT"));
			obj.setR40_T_HTM(rs.getBigDecimal("R40_T_HTM"));
			obj.setR40_T_TOTAL(rs.getBigDecimal("R40_T_TOTAL"));

			// =========================
			// R41
			// =========================
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_0_1Y_FT(rs.getBigDecimal("R41_0_1Y_FT"));
			obj.setR41_0_1Y_HTM(rs.getBigDecimal("R41_0_1Y_HTM"));
			obj.setR41_0_1Y_TOTAL(rs.getBigDecimal("R41_0_1Y_TOTAL"));
			obj.setR41_1_5Y_FT(rs.getBigDecimal("R41_1_5Y_FT"));
			obj.setR41_1_5Y_HTM(rs.getBigDecimal("R41_1_5Y_HTM"));
			obj.setR41_1_5Y_TOTAL(rs.getBigDecimal("R41_1_5Y_TOTAL"));
			obj.setR41_O5Y_FT(rs.getBigDecimal("R41_O5Y_FT"));
			obj.setR41_O5Y_HTM(rs.getBigDecimal("R41_O5Y_HTM"));
			obj.setR41_O5Y_TOTAL(rs.getBigDecimal("R41_O5Y_TOTAL"));
			obj.setR41_T_FT(rs.getBigDecimal("R41_T_FT"));
			obj.setR41_T_HTM(rs.getBigDecimal("R41_T_HTM"));
			obj.setR41_T_TOTAL(rs.getBigDecimal("R41_T_TOTAL"));

			// =========================
			// R42
			// =========================
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_0_1Y_FT(rs.getBigDecimal("R42_0_1Y_FT"));
			obj.setR42_0_1Y_HTM(rs.getBigDecimal("R42_0_1Y_HTM"));
			obj.setR42_0_1Y_TOTAL(rs.getBigDecimal("R42_0_1Y_TOTAL"));
			obj.setR42_1_5Y_FT(rs.getBigDecimal("R42_1_5Y_FT"));
			obj.setR42_1_5Y_HTM(rs.getBigDecimal("R42_1_5Y_HTM"));
			obj.setR42_1_5Y_TOTAL(rs.getBigDecimal("R42_1_5Y_TOTAL"));
			obj.setR42_O5Y_FT(rs.getBigDecimal("R42_O5Y_FT"));
			obj.setR42_O5Y_HTM(rs.getBigDecimal("R42_O5Y_HTM"));
			obj.setR42_O5Y_TOTAL(rs.getBigDecimal("R42_O5Y_TOTAL"));
			obj.setR42_T_FT(rs.getBigDecimal("R42_T_FT"));
			obj.setR42_T_HTM(rs.getBigDecimal("R42_T_HTM"));
			obj.setR42_T_TOTAL(rs.getBigDecimal("R42_T_TOTAL"));

			// =========================
			// R43
			// =========================
			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
			obj.setR43_0_1Y_FT(rs.getBigDecimal("R43_0_1Y_FT"));
			obj.setR43_0_1Y_HTM(rs.getBigDecimal("R43_0_1Y_HTM"));
			obj.setR43_0_1Y_TOTAL(rs.getBigDecimal("R43_0_1Y_TOTAL"));
			obj.setR43_1_5Y_FT(rs.getBigDecimal("R43_1_5Y_FT"));
			obj.setR43_1_5Y_HTM(rs.getBigDecimal("R43_1_5Y_HTM"));
			obj.setR43_1_5Y_TOTAL(rs.getBigDecimal("R43_1_5Y_TOTAL"));
			obj.setR43_O5Y_FT(rs.getBigDecimal("R43_O5Y_FT"));
			obj.setR43_O5Y_HTM(rs.getBigDecimal("R43_O5Y_HTM"));
			obj.setR43_O5Y_TOTAL(rs.getBigDecimal("R43_O5Y_TOTAL"));
			obj.setR43_T_FT(rs.getBigDecimal("R43_T_FT"));
			obj.setR43_T_HTM(rs.getBigDecimal("R43_T_HTM"));
			obj.setR43_T_TOTAL(rs.getBigDecimal("R43_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Summary_Entity4
//===========================

	public class M_SEC_RESUB_Summary_Entity4 {

		private String R36_PRODUCT;
		private BigDecimal R36_0_1Y_FT;
		private BigDecimal R36_0_1Y_HTM;
		private BigDecimal R36_0_1Y_TOTAL;
		private BigDecimal R36_1_5Y_FT;
		private BigDecimal R36_1_5Y_HTM;
		private BigDecimal R36_1_5Y_TOTAL;
		private BigDecimal R36_O5Y_FT;
		private BigDecimal R36_O5Y_HTM;
		private BigDecimal R36_O5Y_TOTAL;
		private BigDecimal R36_T_FT;
		private BigDecimal R36_T_HTM;
		private BigDecimal R36_T_TOTAL;

		private String R37_PRODUCT;
		private BigDecimal R37_0_1Y_FT;
		private BigDecimal R37_0_1Y_HTM;
		private BigDecimal R37_0_1Y_TOTAL;
		private BigDecimal R37_1_5Y_FT;
		private BigDecimal R37_1_5Y_HTM;
		private BigDecimal R37_1_5Y_TOTAL;
		private BigDecimal R37_O5Y_FT;
		private BigDecimal R37_O5Y_HTM;
		private BigDecimal R37_O5Y_TOTAL;
		private BigDecimal R37_T_FT;
		private BigDecimal R37_T_HTM;
		private BigDecimal R37_T_TOTAL;

		private String R38_PRODUCT;
		private BigDecimal R38_0_1Y_FT;
		private BigDecimal R38_0_1Y_HTM;
		private BigDecimal R38_0_1Y_TOTAL;
		private BigDecimal R38_1_5Y_FT;
		private BigDecimal R38_1_5Y_HTM;
		private BigDecimal R38_1_5Y_TOTAL;
		private BigDecimal R38_O5Y_FT;
		private BigDecimal R38_O5Y_HTM;
		private BigDecimal R38_O5Y_TOTAL;
		private BigDecimal R38_T_FT;
		private BigDecimal R38_T_HTM;
		private BigDecimal R38_T_TOTAL;

		private String R39_PRODUCT;
		private BigDecimal R39_0_1Y_FT;
		private BigDecimal R39_0_1Y_HTM;
		private BigDecimal R39_0_1Y_TOTAL;
		private BigDecimal R39_1_5Y_FT;
		private BigDecimal R39_1_5Y_HTM;
		private BigDecimal R39_1_5Y_TOTAL;
		private BigDecimal R39_O5Y_FT;
		private BigDecimal R39_O5Y_HTM;
		private BigDecimal R39_O5Y_TOTAL;
		private BigDecimal R39_T_FT;
		private BigDecimal R39_T_HTM;
		private BigDecimal R39_T_TOTAL;

		private String R40_PRODUCT;
		private BigDecimal R40_0_1Y_FT;
		private BigDecimal R40_0_1Y_HTM;
		private BigDecimal R40_0_1Y_TOTAL;
		private BigDecimal R40_1_5Y_FT;
		private BigDecimal R40_1_5Y_HTM;
		private BigDecimal R40_1_5Y_TOTAL;
		private BigDecimal R40_O5Y_FT;
		private BigDecimal R40_O5Y_HTM;
		private BigDecimal R40_O5Y_TOTAL;
		private BigDecimal R40_T_FT;
		private BigDecimal R40_T_HTM;
		private BigDecimal R40_T_TOTAL;

		private String R41_PRODUCT;
		private BigDecimal R41_0_1Y_FT;
		private BigDecimal R41_0_1Y_HTM;
		private BigDecimal R41_0_1Y_TOTAL;
		private BigDecimal R41_1_5Y_FT;
		private BigDecimal R41_1_5Y_HTM;
		private BigDecimal R41_1_5Y_TOTAL;
		private BigDecimal R41_O5Y_FT;
		private BigDecimal R41_O5Y_HTM;
		private BigDecimal R41_O5Y_TOTAL;
		private BigDecimal R41_T_FT;
		private BigDecimal R41_T_HTM;
		private BigDecimal R41_T_TOTAL;

		private String R42_PRODUCT;
		private BigDecimal R42_0_1Y_FT;
		private BigDecimal R42_0_1Y_HTM;
		private BigDecimal R42_0_1Y_TOTAL;
		private BigDecimal R42_1_5Y_FT;
		private BigDecimal R42_1_5Y_HTM;
		private BigDecimal R42_1_5Y_TOTAL;
		private BigDecimal R42_O5Y_FT;
		private BigDecimal R42_O5Y_HTM;
		private BigDecimal R42_O5Y_TOTAL;
		private BigDecimal R42_T_FT;
		private BigDecimal R42_T_HTM;
		private BigDecimal R42_T_TOTAL;

		private String R43_PRODUCT;
		private BigDecimal R43_0_1Y_FT;
		private BigDecimal R43_0_1Y_HTM;
		private BigDecimal R43_0_1Y_TOTAL;
		private BigDecimal R43_1_5Y_FT;
		private BigDecimal R43_1_5Y_HTM;
		private BigDecimal R43_1_5Y_TOTAL;
		private BigDecimal R43_O5Y_FT;
		private BigDecimal R43_O5Y_HTM;
		private BigDecimal R43_O5Y_TOTAL;
		private BigDecimal R43_T_FT;
		private BigDecimal R43_T_HTM;
		private BigDecimal R43_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Summary_Entity4() {
			super();
		}

		// Getters and Setters for R36
		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_0_1Y_FT() {
			return R36_0_1Y_FT;
		}

		public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
			R36_0_1Y_FT = r36_0_1y_FT;
		}

		public BigDecimal getR36_0_1Y_HTM() {
			return R36_0_1Y_HTM;
		}

		public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
			R36_0_1Y_HTM = r36_0_1y_HTM;
		}

		public BigDecimal getR36_0_1Y_TOTAL() {
			return R36_0_1Y_TOTAL;
		}

		public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
			R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
		}

		public BigDecimal getR36_1_5Y_FT() {
			return R36_1_5Y_FT;
		}

		public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
			R36_1_5Y_FT = r36_1_5y_FT;
		}

		public BigDecimal getR36_1_5Y_HTM() {
			return R36_1_5Y_HTM;
		}

		public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
			R36_1_5Y_HTM = r36_1_5y_HTM;
		}

		public BigDecimal getR36_1_5Y_TOTAL() {
			return R36_1_5Y_TOTAL;
		}

		public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
			R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
		}

		public BigDecimal getR36_O5Y_FT() {
			return R36_O5Y_FT;
		}

		public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
			R36_O5Y_FT = r36_O5Y_FT;
		}

		public BigDecimal getR36_O5Y_HTM() {
			return R36_O5Y_HTM;
		}

		public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
			R36_O5Y_HTM = r36_O5Y_HTM;
		}

		public BigDecimal getR36_O5Y_TOTAL() {
			return R36_O5Y_TOTAL;
		}

		public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
			R36_O5Y_TOTAL = r36_O5Y_TOTAL;
		}

		public BigDecimal getR36_T_FT() {
			return R36_T_FT;
		}

		public void setR36_T_FT(BigDecimal r36_T_FT) {
			R36_T_FT = r36_T_FT;
		}

		public BigDecimal getR36_T_HTM() {
			return R36_T_HTM;
		}

		public void setR36_T_HTM(BigDecimal r36_T_HTM) {
			R36_T_HTM = r36_T_HTM;
		}

		public BigDecimal getR36_T_TOTAL() {
			return R36_T_TOTAL;
		}

		public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
			R36_T_TOTAL = r36_T_TOTAL;
		}

		// Getters and Setters for R37
		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_0_1Y_FT() {
			return R37_0_1Y_FT;
		}

		public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
			R37_0_1Y_FT = r37_0_1y_FT;
		}

		public BigDecimal getR37_0_1Y_HTM() {
			return R37_0_1Y_HTM;
		}

		public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
			R37_0_1Y_HTM = r37_0_1y_HTM;
		}

		public BigDecimal getR37_0_1Y_TOTAL() {
			return R37_0_1Y_TOTAL;
		}

		public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
			R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
		}

		public BigDecimal getR37_1_5Y_FT() {
			return R37_1_5Y_FT;
		}

		public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
			R37_1_5Y_FT = r37_1_5y_FT;
		}

		public BigDecimal getR37_1_5Y_HTM() {
			return R37_1_5Y_HTM;
		}

		public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
			R37_1_5Y_HTM = r37_1_5y_HTM;
		}

		public BigDecimal getR37_1_5Y_TOTAL() {
			return R37_1_5Y_TOTAL;
		}

		public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
			R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
		}

		public BigDecimal getR37_O5Y_FT() {
			return R37_O5Y_FT;
		}

		public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
			R37_O5Y_FT = r37_O5Y_FT;
		}

		public BigDecimal getR37_O5Y_HTM() {
			return R37_O5Y_HTM;
		}

		public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
			R37_O5Y_HTM = r37_O5Y_HTM;
		}

		public BigDecimal getR37_O5Y_TOTAL() {
			return R37_O5Y_TOTAL;
		}

		public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
			R37_O5Y_TOTAL = r37_O5Y_TOTAL;
		}

		public BigDecimal getR37_T_FT() {
			return R37_T_FT;
		}

		public void setR37_T_FT(BigDecimal r37_T_FT) {
			R37_T_FT = r37_T_FT;
		}

		public BigDecimal getR37_T_HTM() {
			return R37_T_HTM;
		}

		public void setR37_T_HTM(BigDecimal r37_T_HTM) {
			R37_T_HTM = r37_T_HTM;
		}

		public BigDecimal getR37_T_TOTAL() {
			return R37_T_TOTAL;
		}

		public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
			R37_T_TOTAL = r37_T_TOTAL;
		}

		// Getters and Setters for R38
		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_0_1Y_FT() {
			return R38_0_1Y_FT;
		}

		public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
			R38_0_1Y_FT = r38_0_1y_FT;
		}

		public BigDecimal getR38_0_1Y_HTM() {
			return R38_0_1Y_HTM;
		}

		public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
			R38_0_1Y_HTM = r38_0_1y_HTM;
		}

		public BigDecimal getR38_0_1Y_TOTAL() {
			return R38_0_1Y_TOTAL;
		}

		public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
			R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
		}

		public BigDecimal getR38_1_5Y_FT() {
			return R38_1_5Y_FT;
		}

		public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
			R38_1_5Y_FT = r38_1_5y_FT;
		}

		public BigDecimal getR38_1_5Y_HTM() {
			return R38_1_5Y_HTM;
		}

		public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
			R38_1_5Y_HTM = r38_1_5y_HTM;
		}

		public BigDecimal getR38_1_5Y_TOTAL() {
			return R38_1_5Y_TOTAL;
		}

		public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
			R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
		}

		public BigDecimal getR38_O5Y_FT() {
			return R38_O5Y_FT;
		}

		public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
			R38_O5Y_FT = r38_O5Y_FT;
		}

		public BigDecimal getR38_O5Y_HTM() {
			return R38_O5Y_HTM;
		}

		public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
			R38_O5Y_HTM = r38_O5Y_HTM;
		}

		public BigDecimal getR38_O5Y_TOTAL() {
			return R38_O5Y_TOTAL;
		}

		public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
			R38_O5Y_TOTAL = r38_O5Y_TOTAL;
		}

		public BigDecimal getR38_T_FT() {
			return R38_T_FT;
		}

		public void setR38_T_FT(BigDecimal r38_T_FT) {
			R38_T_FT = r38_T_FT;
		}

		public BigDecimal getR38_T_HTM() {
			return R38_T_HTM;
		}

		public void setR38_T_HTM(BigDecimal r38_T_HTM) {
			R38_T_HTM = r38_T_HTM;
		}

		public BigDecimal getR38_T_TOTAL() {
			return R38_T_TOTAL;
		}

		public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
			R38_T_TOTAL = r38_T_TOTAL;
		}

		// Getters and Setters for R39
		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_0_1Y_FT() {
			return R39_0_1Y_FT;
		}

		public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
			R39_0_1Y_FT = r39_0_1y_FT;
		}

		public BigDecimal getR39_0_1Y_HTM() {
			return R39_0_1Y_HTM;
		}

		public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
			R39_0_1Y_HTM = r39_0_1y_HTM;
		}

		public BigDecimal getR39_0_1Y_TOTAL() {
			return R39_0_1Y_TOTAL;
		}

		public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
			R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
		}

		public BigDecimal getR39_1_5Y_FT() {
			return R39_1_5Y_FT;
		}

		public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
			R39_1_5Y_FT = r39_1_5y_FT;
		}

		public BigDecimal getR39_1_5Y_HTM() {
			return R39_1_5Y_HTM;
		}

		public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
			R39_1_5Y_HTM = r39_1_5y_HTM;
		}

		public BigDecimal getR39_1_5Y_TOTAL() {
			return R39_1_5Y_TOTAL;
		}

		public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
			R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
		}

		public BigDecimal getR39_O5Y_FT() {
			return R39_O5Y_FT;
		}

		public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
			R39_O5Y_FT = r39_O5Y_FT;
		}

		public BigDecimal getR39_O5Y_HTM() {
			return R39_O5Y_HTM;
		}

		public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
			R39_O5Y_HTM = r39_O5Y_HTM;
		}

		public BigDecimal getR39_O5Y_TOTAL() {
			return R39_O5Y_TOTAL;
		}

		public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
			R39_O5Y_TOTAL = r39_O5Y_TOTAL;
		}

		public BigDecimal getR39_T_FT() {
			return R39_T_FT;
		}

		public void setR39_T_FT(BigDecimal r39_T_FT) {
			R39_T_FT = r39_T_FT;
		}

		public BigDecimal getR39_T_HTM() {
			return R39_T_HTM;
		}

		public void setR39_T_HTM(BigDecimal r39_T_HTM) {
			R39_T_HTM = r39_T_HTM;
		}

		public BigDecimal getR39_T_TOTAL() {
			return R39_T_TOTAL;
		}

		public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
			R39_T_TOTAL = r39_T_TOTAL;
		}

		// Getters and Setters for R40
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_0_1Y_FT() {
			return R40_0_1Y_FT;
		}

		public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
			R40_0_1Y_FT = r40_0_1y_FT;
		}

		public BigDecimal getR40_0_1Y_HTM() {
			return R40_0_1Y_HTM;
		}

		public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
			R40_0_1Y_HTM = r40_0_1y_HTM;
		}

		public BigDecimal getR40_0_1Y_TOTAL() {
			return R40_0_1Y_TOTAL;
		}

		public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
			R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
		}

		public BigDecimal getR40_1_5Y_FT() {
			return R40_1_5Y_FT;
		}

		public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
			R40_1_5Y_FT = r40_1_5y_FT;
		}

		public BigDecimal getR40_1_5Y_HTM() {
			return R40_1_5Y_HTM;
		}

		public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
			R40_1_5Y_HTM = r40_1_5y_HTM;
		}

		public BigDecimal getR40_1_5Y_TOTAL() {
			return R40_1_5Y_TOTAL;
		}

		public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
			R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
		}

		public BigDecimal getR40_O5Y_FT() {
			return R40_O5Y_FT;
		}

		public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
			R40_O5Y_FT = r40_O5Y_FT;
		}

		public BigDecimal getR40_O5Y_HTM() {
			return R40_O5Y_HTM;
		}

		public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
			R40_O5Y_HTM = r40_O5Y_HTM;
		}

		public BigDecimal getR40_O5Y_TOTAL() {
			return R40_O5Y_TOTAL;
		}

		public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
			R40_O5Y_TOTAL = r40_O5Y_TOTAL;
		}

		public BigDecimal getR40_T_FT() {
			return R40_T_FT;
		}

		public void setR40_T_FT(BigDecimal r40_T_FT) {
			R40_T_FT = r40_T_FT;
		}

		public BigDecimal getR40_T_HTM() {
			return R40_T_HTM;
		}

		public void setR40_T_HTM(BigDecimal r40_T_HTM) {
			R40_T_HTM = r40_T_HTM;
		}

		public BigDecimal getR40_T_TOTAL() {
			return R40_T_TOTAL;
		}

		public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
			R40_T_TOTAL = r40_T_TOTAL;
		}

		// Getters and Setters for R41
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_0_1Y_FT() {
			return R41_0_1Y_FT;
		}

		public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
			R41_0_1Y_FT = r41_0_1y_FT;
		}

		public BigDecimal getR41_0_1Y_HTM() {
			return R41_0_1Y_HTM;
		}

		public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
			R41_0_1Y_HTM = r41_0_1y_HTM;
		}

		public BigDecimal getR41_0_1Y_TOTAL() {
			return R41_0_1Y_TOTAL;
		}

		public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
			R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
		}

		public BigDecimal getR41_1_5Y_FT() {
			return R41_1_5Y_FT;
		}

		public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
			R41_1_5Y_FT = r41_1_5y_FT;
		}

		public BigDecimal getR41_1_5Y_HTM() {
			return R41_1_5Y_HTM;
		}

		public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
			R41_1_5Y_HTM = r41_1_5y_HTM;
		}

		public BigDecimal getR41_1_5Y_TOTAL() {
			return R41_1_5Y_TOTAL;
		}

		public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
			R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
		}

		public BigDecimal getR41_O5Y_FT() {
			return R41_O5Y_FT;
		}

		public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
			R41_O5Y_FT = r41_O5Y_FT;
		}

		public BigDecimal getR41_O5Y_HTM() {
			return R41_O5Y_HTM;
		}

		public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
			R41_O5Y_HTM = r41_O5Y_HTM;
		}

		public BigDecimal getR41_O5Y_TOTAL() {
			return R41_O5Y_TOTAL;
		}

		public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
			R41_O5Y_TOTAL = r41_O5Y_TOTAL;
		}

		public BigDecimal getR41_T_FT() {
			return R41_T_FT;
		}

		public void setR41_T_FT(BigDecimal r41_T_FT) {
			R41_T_FT = r41_T_FT;
		}

		public BigDecimal getR41_T_HTM() {
			return R41_T_HTM;
		}

		public void setR41_T_HTM(BigDecimal r41_T_HTM) {
			R41_T_HTM = r41_T_HTM;
		}

		public BigDecimal getR41_T_TOTAL() {
			return R41_T_TOTAL;
		}

		public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
			R41_T_TOTAL = r41_T_TOTAL;
		}

		// Getters and Setters for R42
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_0_1Y_FT() {
			return R42_0_1Y_FT;
		}

		public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
			R42_0_1Y_FT = r42_0_1y_FT;
		}

		public BigDecimal getR42_0_1Y_HTM() {
			return R42_0_1Y_HTM;
		}

		public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
			R42_0_1Y_HTM = r42_0_1y_HTM;
		}

		public BigDecimal getR42_0_1Y_TOTAL() {
			return R42_0_1Y_TOTAL;
		}

		public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
			R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
		}

		public BigDecimal getR42_1_5Y_FT() {
			return R42_1_5Y_FT;
		}

		public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
			R42_1_5Y_FT = r42_1_5y_FT;
		}

		public BigDecimal getR42_1_5Y_HTM() {
			return R42_1_5Y_HTM;
		}

		public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
			R42_1_5Y_HTM = r42_1_5y_HTM;
		}

		public BigDecimal getR42_1_5Y_TOTAL() {
			return R42_1_5Y_TOTAL;
		}

		public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
			R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
		}

		public BigDecimal getR42_O5Y_FT() {
			return R42_O5Y_FT;
		}

		public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
			R42_O5Y_FT = r42_O5Y_FT;
		}

		public BigDecimal getR42_O5Y_HTM() {
			return R42_O5Y_HTM;
		}

		public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
			R42_O5Y_HTM = r42_O5Y_HTM;
		}

		public BigDecimal getR42_O5Y_TOTAL() {
			return R42_O5Y_TOTAL;
		}

		public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
			R42_O5Y_TOTAL = r42_O5Y_TOTAL;
		}

		public BigDecimal getR42_T_FT() {
			return R42_T_FT;
		}

		public void setR42_T_FT(BigDecimal r42_T_FT) {
			R42_T_FT = r42_T_FT;
		}

		public BigDecimal getR42_T_HTM() {
			return R42_T_HTM;
		}

		public void setR42_T_HTM(BigDecimal r42_T_HTM) {
			R42_T_HTM = r42_T_HTM;
		}

		public BigDecimal getR42_T_TOTAL() {
			return R42_T_TOTAL;
		}

		public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
			R42_T_TOTAL = r42_T_TOTAL;
		}

		// Getters and Setters for R43
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public BigDecimal getR43_0_1Y_FT() {
			return R43_0_1Y_FT;
		}

		public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
			R43_0_1Y_FT = r43_0_1y_FT;
		}

		public BigDecimal getR43_0_1Y_HTM() {
			return R43_0_1Y_HTM;
		}

		public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
			R43_0_1Y_HTM = r43_0_1y_HTM;
		}

		public BigDecimal getR43_0_1Y_TOTAL() {
			return R43_0_1Y_TOTAL;
		}

		public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
			R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
		}

		public BigDecimal getR43_1_5Y_FT() {
			return R43_1_5Y_FT;
		}

		public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
			R43_1_5Y_FT = r43_1_5y_FT;
		}

		public BigDecimal getR43_1_5Y_HTM() {
			return R43_1_5Y_HTM;
		}

		public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
			R43_1_5Y_HTM = r43_1_5y_HTM;
		}

		public BigDecimal getR43_1_5Y_TOTAL() {
			return R43_1_5Y_TOTAL;
		}

		public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
			R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
		}

		public BigDecimal getR43_O5Y_FT() {
			return R43_O5Y_FT;
		}

		public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
			R43_O5Y_FT = r43_O5Y_FT;
		}

		public BigDecimal getR43_O5Y_HTM() {
			return R43_O5Y_HTM;
		}

		public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
			R43_O5Y_HTM = r43_O5Y_HTM;
		}

		public BigDecimal getR43_O5Y_TOTAL() {
			return R43_O5Y_TOTAL;
		}

		public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
			R43_O5Y_TOTAL = r43_O5Y_TOTAL;
		}

		public BigDecimal getR43_T_FT() {
			return R43_T_FT;
		}

		public void setR43_T_FT(BigDecimal r43_T_FT) {
			R43_T_FT = r43_T_FT;
		}

		public BigDecimal getR43_T_HTM() {
			return R43_T_HTM;
		}

		public void setR43_T_HTM(BigDecimal r43_T_HTM) {
			R43_T_HTM = r43_T_HTM;
		}

		public BigDecimal getR43_T_TOTAL() {
			return R43_T_TOTAL;
		}

		public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
			R43_T_TOTAL = r43_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Detail_RowMapper1
//===========================

	public class M_SEC_RESUB_Detail_RowMapper1 implements RowMapper<M_SEC_RESUB_Detail_Entity1> {

		@Override
		public M_SEC_RESUB_Detail_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Detail_Entity1 obj = new M_SEC_RESUB_Detail_Entity1();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA(rs.getBigDecimal("R11_TCA"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA(rs.getBigDecimal("R12_TCA"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA(rs.getBigDecimal("R13_TCA"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA(rs.getBigDecimal("R14_TCA"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA(rs.getBigDecimal("R15_TCA"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA(rs.getBigDecimal("R16_TCA"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TCA(rs.getBigDecimal("R17_TCA"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TCA(rs.getBigDecimal("R18_TCA"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TCA(rs.getBigDecimal("R19_TCA"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Detail_Entity1
//===========================

	public class M_SEC_RESUB_Detail_Entity1 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA;
		private String R17_PRODUCT;
		private BigDecimal R17_TCA;
		private String R18_PRODUCT;
		private BigDecimal R18_TCA;
		private String R19_PRODUCT;
		private BigDecimal R19_TCA;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Detail_Entity1() {
			super();
		}

		// Getters and Setters for R11
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA() {
			return R11_TCA;
		}

		public void setR11_TCA(BigDecimal r11_TCA) {
			R11_TCA = r11_TCA;
		}

		// Getters and Setters for R12
		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA() {
			return R12_TCA;
		}

		public void setR12_TCA(BigDecimal r12_TCA) {
			R12_TCA = r12_TCA;
		}

		// Getters and Setters for R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA() {
			return R13_TCA;
		}

		public void setR13_TCA(BigDecimal r13_TCA) {
			R13_TCA = r13_TCA;
		}

		// Getters and Setters for R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA() {
			return R14_TCA;
		}

		public void setR14_TCA(BigDecimal r14_TCA) {
			R14_TCA = r14_TCA;
		}

		// Getters and Setters for R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA() {
			return R15_TCA;
		}

		public void setR15_TCA(BigDecimal r15_TCA) {
			R15_TCA = r15_TCA;
		}

		// Getters and Setters for R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA() {
			return R16_TCA;
		}

		public void setR16_TCA(BigDecimal r16_TCA) {
			R16_TCA = r16_TCA;
		}

		// Getters and Setters for R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TCA() {
			return R17_TCA;
		}

		public void setR17_TCA(BigDecimal r17_TCA) {
			R17_TCA = r17_TCA;
		}

		// Getters and Setters for R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_TCA() {
			return R18_TCA;
		}

		public void setR18_TCA(BigDecimal r18_TCA) {
			R18_TCA = r18_TCA;
		}

		// Getters and Setters for R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_TCA() {
			return R19_TCA;
		}

		public void setR19_TCA(BigDecimal r19_TCA) {
			R19_TCA = r19_TCA;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Detail_RowMapper2
//===========================

	public class M_SEC_RESUB_Detail_RowMapper2 implements RowMapper<M_SEC_RESUB_Detail_Entity2> {

		@Override
		public M_SEC_RESUB_Detail_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Detail_Entity2 obj = new M_SEC_RESUB_Detail_Entity2();

			// =========================
			// R11
			// =========================
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TCA2(rs.getBigDecimal("R11_TCA2"));

			// =========================
			// R12
			// =========================
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TCA2(rs.getBigDecimal("R12_TCA2"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TCA2(rs.getBigDecimal("R13_TCA2"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TCA2(rs.getBigDecimal("R14_TCA2"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TCA2(rs.getBigDecimal("R15_TCA2"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TCA2(rs.getBigDecimal("R16_TCA2"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Detail_Entity2
//===========================

	public class M_SEC_RESUB_Detail_Entity2 {

		private String R11_PRODUCT;
		private BigDecimal R11_TCA2;
		private String R12_PRODUCT;
		private BigDecimal R12_TCA2;
		private String R13_PRODUCT;
		private BigDecimal R13_TCA2;
		private String R14_PRODUCT;
		private BigDecimal R14_TCA2;
		private String R15_PRODUCT;
		private BigDecimal R15_TCA2;
		private String R16_PRODUCT;
		private BigDecimal R16_TCA2;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Detail_Entity2() {
			super();
		}

		// Getters and Setters for R11
		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TCA2() {
			return R11_TCA2;
		}

		public void setR11_TCA2(BigDecimal r11_TCA2) {
			R11_TCA2 = r11_TCA2;
		}

		// Getters and Setters for R12
		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TCA2() {
			return R12_TCA2;
		}

		public void setR12_TCA2(BigDecimal r12_TCA2) {
			R12_TCA2 = r12_TCA2;
		}

		// Getters and Setters for R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TCA2() {
			return R13_TCA2;
		}

		public void setR13_TCA2(BigDecimal r13_TCA2) {
			R13_TCA2 = r13_TCA2;
		}

		// Getters and Setters for R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TCA2() {
			return R14_TCA2;
		}

		public void setR14_TCA2(BigDecimal r14_TCA2) {
			R14_TCA2 = r14_TCA2;
		}

		// Getters and Setters for R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TCA2() {
			return R15_TCA2;
		}

		public void setR15_TCA2(BigDecimal r15_TCA2) {
			R15_TCA2 = r15_TCA2;
		}

		// Getters and Setters for R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TCA2() {
			return R16_TCA2;
		}

		public void setR16_TCA2(BigDecimal r16_TCA2) {
			R16_TCA2 = r16_TCA2;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Detail_RowMapper3
//===========================

	public class M_SEC_RESUB_Detail_RowMapper3 implements RowMapper<M_SEC_RESUB_Detail_Entity3> {

		@Override
		public M_SEC_RESUB_Detail_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Detail_Entity3 obj = new M_SEC_RESUB_Detail_Entity3();

			// =========================
			// R26
			// =========================
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_0_1Y_FT(rs.getBigDecimal("R26_0_1Y_FT"));
			obj.setR26_0_1Y_HTM(rs.getBigDecimal("R26_0_1Y_HTM"));
			obj.setR26_0_1Y_TOTAL(rs.getBigDecimal("R26_0_1Y_TOTAL"));
			obj.setR26_1_5Y_FT(rs.getBigDecimal("R26_1_5Y_FT"));
			obj.setR26_1_5Y_HTM(rs.getBigDecimal("R26_1_5Y_HTM"));
			obj.setR26_1_5Y_TOTAL(rs.getBigDecimal("R26_1_5Y_TOTAL"));
			obj.setR26_O5Y_FT(rs.getBigDecimal("R26_O5Y_FT"));
			obj.setR26_O5Y_HTM(rs.getBigDecimal("R26_O5Y_HTM"));
			obj.setR26_O5Y_TOTAL(rs.getBigDecimal("R26_O5Y_TOTAL"));
			obj.setR26_T_FT(rs.getBigDecimal("R26_T_FT"));
			obj.setR26_T_HTM(rs.getBigDecimal("R26_T_HTM"));
			obj.setR26_T_TOTAL(rs.getBigDecimal("R26_T_TOTAL"));

			// =========================
			// R27
			// =========================
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_0_1Y_FT(rs.getBigDecimal("R27_0_1Y_FT"));
			obj.setR27_0_1Y_HTM(rs.getBigDecimal("R27_0_1Y_HTM"));
			obj.setR27_0_1Y_TOTAL(rs.getBigDecimal("R27_0_1Y_TOTAL"));
			obj.setR27_1_5Y_FT(rs.getBigDecimal("R27_1_5Y_FT"));
			obj.setR27_1_5Y_HTM(rs.getBigDecimal("R27_1_5Y_HTM"));
			obj.setR27_1_5Y_TOTAL(rs.getBigDecimal("R27_1_5Y_TOTAL"));
			obj.setR27_O5Y_FT(rs.getBigDecimal("R27_O5Y_FT"));
			obj.setR27_O5Y_HTM(rs.getBigDecimal("R27_O5Y_HTM"));
			obj.setR27_O5Y_TOTAL(rs.getBigDecimal("R27_O5Y_TOTAL"));
			obj.setR27_T_FT(rs.getBigDecimal("R27_T_FT"));
			obj.setR27_T_HTM(rs.getBigDecimal("R27_T_HTM"));
			obj.setR27_T_TOTAL(rs.getBigDecimal("R27_T_TOTAL"));

			// =========================
			// R28
			// =========================
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_0_1Y_FT(rs.getBigDecimal("R28_0_1Y_FT"));
			obj.setR28_0_1Y_HTM(rs.getBigDecimal("R28_0_1Y_HTM"));
			obj.setR28_0_1Y_TOTAL(rs.getBigDecimal("R28_0_1Y_TOTAL"));
			obj.setR28_1_5Y_FT(rs.getBigDecimal("R28_1_5Y_FT"));
			obj.setR28_1_5Y_HTM(rs.getBigDecimal("R28_1_5Y_HTM"));
			obj.setR28_1_5Y_TOTAL(rs.getBigDecimal("R28_1_5Y_TOTAL"));
			obj.setR28_O5Y_FT(rs.getBigDecimal("R28_O5Y_FT"));
			obj.setR28_O5Y_HTM(rs.getBigDecimal("R28_O5Y_HTM"));
			obj.setR28_O5Y_TOTAL(rs.getBigDecimal("R28_O5Y_TOTAL"));
			obj.setR28_T_FT(rs.getBigDecimal("R28_T_FT"));
			obj.setR28_T_HTM(rs.getBigDecimal("R28_T_HTM"));
			obj.setR28_T_TOTAL(rs.getBigDecimal("R28_T_TOTAL"));

			// =========================
			// R29
			// =========================
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_0_1Y_FT(rs.getBigDecimal("R29_0_1Y_FT"));
			obj.setR29_0_1Y_HTM(rs.getBigDecimal("R29_0_1Y_HTM"));
			obj.setR29_0_1Y_TOTAL(rs.getBigDecimal("R29_0_1Y_TOTAL"));
			obj.setR29_1_5Y_FT(rs.getBigDecimal("R29_1_5Y_FT"));
			obj.setR29_1_5Y_HTM(rs.getBigDecimal("R29_1_5Y_HTM"));
			obj.setR29_1_5Y_TOTAL(rs.getBigDecimal("R29_1_5Y_TOTAL"));
			obj.setR29_O5Y_FT(rs.getBigDecimal("R29_O5Y_FT"));
			obj.setR29_O5Y_HTM(rs.getBigDecimal("R29_O5Y_HTM"));
			obj.setR29_O5Y_TOTAL(rs.getBigDecimal("R29_O5Y_TOTAL"));
			obj.setR29_T_FT(rs.getBigDecimal("R29_T_FT"));
			obj.setR29_T_HTM(rs.getBigDecimal("R29_T_HTM"));
			obj.setR29_T_TOTAL(rs.getBigDecimal("R29_T_TOTAL"));

			// =========================
			// R30
			// =========================
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_0_1Y_FT(rs.getBigDecimal("R30_0_1Y_FT"));
			obj.setR30_0_1Y_HTM(rs.getBigDecimal("R30_0_1Y_HTM"));
			obj.setR30_0_1Y_TOTAL(rs.getBigDecimal("R30_0_1Y_TOTAL"));
			obj.setR30_1_5Y_FT(rs.getBigDecimal("R30_1_5Y_FT"));
			obj.setR30_1_5Y_HTM(rs.getBigDecimal("R30_1_5Y_HTM"));
			obj.setR30_1_5Y_TOTAL(rs.getBigDecimal("R30_1_5Y_TOTAL"));
			obj.setR30_O5Y_FT(rs.getBigDecimal("R30_O5Y_FT"));
			obj.setR30_O5Y_HTM(rs.getBigDecimal("R30_O5Y_HTM"));
			obj.setR30_O5Y_TOTAL(rs.getBigDecimal("R30_O5Y_TOTAL"));
			obj.setR30_T_FT(rs.getBigDecimal("R30_T_FT"));
			obj.setR30_T_HTM(rs.getBigDecimal("R30_T_HTM"));
			obj.setR30_T_TOTAL(rs.getBigDecimal("R30_T_TOTAL"));

			// =========================
			// R31
			// =========================
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_0_1Y_FT(rs.getBigDecimal("R31_0_1Y_FT"));
			obj.setR31_0_1Y_HTM(rs.getBigDecimal("R31_0_1Y_HTM"));
			obj.setR31_0_1Y_TOTAL(rs.getBigDecimal("R31_0_1Y_TOTAL"));
			obj.setR31_1_5Y_FT(rs.getBigDecimal("R31_1_5Y_FT"));
			obj.setR31_1_5Y_HTM(rs.getBigDecimal("R31_1_5Y_HTM"));
			obj.setR31_1_5Y_TOTAL(rs.getBigDecimal("R31_1_5Y_TOTAL"));
			obj.setR31_O5Y_FT(rs.getBigDecimal("R31_O5Y_FT"));
			obj.setR31_O5Y_HTM(rs.getBigDecimal("R31_O5Y_HTM"));
			obj.setR31_O5Y_TOTAL(rs.getBigDecimal("R31_O5Y_TOTAL"));
			obj.setR31_T_FT(rs.getBigDecimal("R31_T_FT"));
			obj.setR31_T_HTM(rs.getBigDecimal("R31_T_HTM"));
			obj.setR31_T_TOTAL(rs.getBigDecimal("R31_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Detail_Entity3
//===========================

	public class M_SEC_RESUB_Detail_Entity3 {

		private String R26_PRODUCT;
		private BigDecimal R26_0_1Y_FT;
		private BigDecimal R26_0_1Y_HTM;
		private BigDecimal R26_0_1Y_TOTAL;
		private BigDecimal R26_1_5Y_FT;
		private BigDecimal R26_1_5Y_HTM;
		private BigDecimal R26_1_5Y_TOTAL;
		private BigDecimal R26_O5Y_FT;
		private BigDecimal R26_O5Y_HTM;
		private BigDecimal R26_O5Y_TOTAL;
		private BigDecimal R26_T_FT;
		private BigDecimal R26_T_HTM;
		private BigDecimal R26_T_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_0_1Y_FT;
		private BigDecimal R27_0_1Y_HTM;
		private BigDecimal R27_0_1Y_TOTAL;
		private BigDecimal R27_1_5Y_FT;
		private BigDecimal R27_1_5Y_HTM;
		private BigDecimal R27_1_5Y_TOTAL;
		private BigDecimal R27_O5Y_FT;
		private BigDecimal R27_O5Y_HTM;
		private BigDecimal R27_O5Y_TOTAL;
		private BigDecimal R27_T_FT;
		private BigDecimal R27_T_HTM;
		private BigDecimal R27_T_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_0_1Y_FT;
		private BigDecimal R28_0_1Y_HTM;
		private BigDecimal R28_0_1Y_TOTAL;
		private BigDecimal R28_1_5Y_FT;
		private BigDecimal R28_1_5Y_HTM;
		private BigDecimal R28_1_5Y_TOTAL;
		private BigDecimal R28_O5Y_FT;
		private BigDecimal R28_O5Y_HTM;
		private BigDecimal R28_O5Y_TOTAL;
		private BigDecimal R28_T_FT;
		private BigDecimal R28_T_HTM;
		private BigDecimal R28_T_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_0_1Y_FT;
		private BigDecimal R29_0_1Y_HTM;
		private BigDecimal R29_0_1Y_TOTAL;
		private BigDecimal R29_1_5Y_FT;
		private BigDecimal R29_1_5Y_HTM;
		private BigDecimal R29_1_5Y_TOTAL;
		private BigDecimal R29_O5Y_FT;
		private BigDecimal R29_O5Y_HTM;
		private BigDecimal R29_O5Y_TOTAL;
		private BigDecimal R29_T_FT;
		private BigDecimal R29_T_HTM;
		private BigDecimal R29_T_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_0_1Y_FT;
		private BigDecimal R30_0_1Y_HTM;
		private BigDecimal R30_0_1Y_TOTAL;
		private BigDecimal R30_1_5Y_FT;
		private BigDecimal R30_1_5Y_HTM;
		private BigDecimal R30_1_5Y_TOTAL;
		private BigDecimal R30_O5Y_FT;
		private BigDecimal R30_O5Y_HTM;
		private BigDecimal R30_O5Y_TOTAL;
		private BigDecimal R30_T_FT;
		private BigDecimal R30_T_HTM;
		private BigDecimal R30_T_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_0_1Y_FT;
		private BigDecimal R31_0_1Y_HTM;
		private BigDecimal R31_0_1Y_TOTAL;
		private BigDecimal R31_1_5Y_FT;
		private BigDecimal R31_1_5Y_HTM;
		private BigDecimal R31_1_5Y_TOTAL;
		private BigDecimal R31_O5Y_FT;
		private BigDecimal R31_O5Y_HTM;
		private BigDecimal R31_O5Y_TOTAL;
		private BigDecimal R31_T_FT;
		private BigDecimal R31_T_HTM;
		private BigDecimal R31_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Detail_Entity3() {
			super();
		}

		// Getters and Setters for R26
		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_0_1Y_FT() {
			return R26_0_1Y_FT;
		}

		public void setR26_0_1Y_FT(BigDecimal r26_0_1y_FT) {
			R26_0_1Y_FT = r26_0_1y_FT;
		}

		public BigDecimal getR26_0_1Y_HTM() {
			return R26_0_1Y_HTM;
		}

		public void setR26_0_1Y_HTM(BigDecimal r26_0_1y_HTM) {
			R26_0_1Y_HTM = r26_0_1y_HTM;
		}

		public BigDecimal getR26_0_1Y_TOTAL() {
			return R26_0_1Y_TOTAL;
		}

		public void setR26_0_1Y_TOTAL(BigDecimal r26_0_1y_TOTAL) {
			R26_0_1Y_TOTAL = r26_0_1y_TOTAL;
		}

		public BigDecimal getR26_1_5Y_FT() {
			return R26_1_5Y_FT;
		}

		public void setR26_1_5Y_FT(BigDecimal r26_1_5y_FT) {
			R26_1_5Y_FT = r26_1_5y_FT;
		}

		public BigDecimal getR26_1_5Y_HTM() {
			return R26_1_5Y_HTM;
		}

		public void setR26_1_5Y_HTM(BigDecimal r26_1_5y_HTM) {
			R26_1_5Y_HTM = r26_1_5y_HTM;
		}

		public BigDecimal getR26_1_5Y_TOTAL() {
			return R26_1_5Y_TOTAL;
		}

		public void setR26_1_5Y_TOTAL(BigDecimal r26_1_5y_TOTAL) {
			R26_1_5Y_TOTAL = r26_1_5y_TOTAL;
		}

		public BigDecimal getR26_O5Y_FT() {
			return R26_O5Y_FT;
		}

		public void setR26_O5Y_FT(BigDecimal r26_O5Y_FT) {
			R26_O5Y_FT = r26_O5Y_FT;
		}

		public BigDecimal getR26_O5Y_HTM() {
			return R26_O5Y_HTM;
		}

		public void setR26_O5Y_HTM(BigDecimal r26_O5Y_HTM) {
			R26_O5Y_HTM = r26_O5Y_HTM;
		}

		public BigDecimal getR26_O5Y_TOTAL() {
			return R26_O5Y_TOTAL;
		}

		public void setR26_O5Y_TOTAL(BigDecimal r26_O5Y_TOTAL) {
			R26_O5Y_TOTAL = r26_O5Y_TOTAL;
		}

		public BigDecimal getR26_T_FT() {
			return R26_T_FT;
		}

		public void setR26_T_FT(BigDecimal r26_T_FT) {
			R26_T_FT = r26_T_FT;
		}

		public BigDecimal getR26_T_HTM() {
			return R26_T_HTM;
		}

		public void setR26_T_HTM(BigDecimal r26_T_HTM) {
			R26_T_HTM = r26_T_HTM;
		}

		public BigDecimal getR26_T_TOTAL() {
			return R26_T_TOTAL;
		}

		public void setR26_T_TOTAL(BigDecimal r26_T_TOTAL) {
			R26_T_TOTAL = r26_T_TOTAL;
		}

		// Getters and Setters for R27
		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_0_1Y_FT() {
			return R27_0_1Y_FT;
		}

		public void setR27_0_1Y_FT(BigDecimal r27_0_1y_FT) {
			R27_0_1Y_FT = r27_0_1y_FT;
		}

		public BigDecimal getR27_0_1Y_HTM() {
			return R27_0_1Y_HTM;
		}

		public void setR27_0_1Y_HTM(BigDecimal r27_0_1y_HTM) {
			R27_0_1Y_HTM = r27_0_1y_HTM;
		}

		public BigDecimal getR27_0_1Y_TOTAL() {
			return R27_0_1Y_TOTAL;
		}

		public void setR27_0_1Y_TOTAL(BigDecimal r27_0_1y_TOTAL) {
			R27_0_1Y_TOTAL = r27_0_1y_TOTAL;
		}

		public BigDecimal getR27_1_5Y_FT() {
			return R27_1_5Y_FT;
		}

		public void setR27_1_5Y_FT(BigDecimal r27_1_5y_FT) {
			R27_1_5Y_FT = r27_1_5y_FT;
		}

		public BigDecimal getR27_1_5Y_HTM() {
			return R27_1_5Y_HTM;
		}

		public void setR27_1_5Y_HTM(BigDecimal r27_1_5y_HTM) {
			R27_1_5Y_HTM = r27_1_5y_HTM;
		}

		public BigDecimal getR27_1_5Y_TOTAL() {
			return R27_1_5Y_TOTAL;
		}

		public void setR27_1_5Y_TOTAL(BigDecimal r27_1_5y_TOTAL) {
			R27_1_5Y_TOTAL = r27_1_5y_TOTAL;
		}

		public BigDecimal getR27_O5Y_FT() {
			return R27_O5Y_FT;
		}

		public void setR27_O5Y_FT(BigDecimal r27_O5Y_FT) {
			R27_O5Y_FT = r27_O5Y_FT;
		}

		public BigDecimal getR27_O5Y_HTM() {
			return R27_O5Y_HTM;
		}

		public void setR27_O5Y_HTM(BigDecimal r27_O5Y_HTM) {
			R27_O5Y_HTM = r27_O5Y_HTM;
		}

		public BigDecimal getR27_O5Y_TOTAL() {
			return R27_O5Y_TOTAL;
		}

		public void setR27_O5Y_TOTAL(BigDecimal r27_O5Y_TOTAL) {
			R27_O5Y_TOTAL = r27_O5Y_TOTAL;
		}

		public BigDecimal getR27_T_FT() {
			return R27_T_FT;
		}

		public void setR27_T_FT(BigDecimal r27_T_FT) {
			R27_T_FT = r27_T_FT;
		}

		public BigDecimal getR27_T_HTM() {
			return R27_T_HTM;
		}

		public void setR27_T_HTM(BigDecimal r27_T_HTM) {
			R27_T_HTM = r27_T_HTM;
		}

		public BigDecimal getR27_T_TOTAL() {
			return R27_T_TOTAL;
		}

		public void setR27_T_TOTAL(BigDecimal r27_T_TOTAL) {
			R27_T_TOTAL = r27_T_TOTAL;
		}

		// Getters and Setters for R28
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_0_1Y_FT() {
			return R28_0_1Y_FT;
		}

		public void setR28_0_1Y_FT(BigDecimal r28_0_1y_FT) {
			R28_0_1Y_FT = r28_0_1y_FT;
		}

		public BigDecimal getR28_0_1Y_HTM() {
			return R28_0_1Y_HTM;
		}

		public void setR28_0_1Y_HTM(BigDecimal r28_0_1y_HTM) {
			R28_0_1Y_HTM = r28_0_1y_HTM;
		}

		public BigDecimal getR28_0_1Y_TOTAL() {
			return R28_0_1Y_TOTAL;
		}

		public void setR28_0_1Y_TOTAL(BigDecimal r28_0_1y_TOTAL) {
			R28_0_1Y_TOTAL = r28_0_1y_TOTAL;
		}

		public BigDecimal getR28_1_5Y_FT() {
			return R28_1_5Y_FT;
		}

		public void setR28_1_5Y_FT(BigDecimal r28_1_5y_FT) {
			R28_1_5Y_FT = r28_1_5y_FT;
		}

		public BigDecimal getR28_1_5Y_HTM() {
			return R28_1_5Y_HTM;
		}

		public void setR28_1_5Y_HTM(BigDecimal r28_1_5y_HTM) {
			R28_1_5Y_HTM = r28_1_5y_HTM;
		}

		public BigDecimal getR28_1_5Y_TOTAL() {
			return R28_1_5Y_TOTAL;
		}

		public void setR28_1_5Y_TOTAL(BigDecimal r28_1_5y_TOTAL) {
			R28_1_5Y_TOTAL = r28_1_5y_TOTAL;
		}

		public BigDecimal getR28_O5Y_FT() {
			return R28_O5Y_FT;
		}

		public void setR28_O5Y_FT(BigDecimal r28_O5Y_FT) {
			R28_O5Y_FT = r28_O5Y_FT;
		}

		public BigDecimal getR28_O5Y_HTM() {
			return R28_O5Y_HTM;
		}

		public void setR28_O5Y_HTM(BigDecimal r28_O5Y_HTM) {
			R28_O5Y_HTM = r28_O5Y_HTM;
		}

		public BigDecimal getR28_O5Y_TOTAL() {
			return R28_O5Y_TOTAL;
		}

		public void setR28_O5Y_TOTAL(BigDecimal r28_O5Y_TOTAL) {
			R28_O5Y_TOTAL = r28_O5Y_TOTAL;
		}

		public BigDecimal getR28_T_FT() {
			return R28_T_FT;
		}

		public void setR28_T_FT(BigDecimal r28_T_FT) {
			R28_T_FT = r28_T_FT;
		}

		public BigDecimal getR28_T_HTM() {
			return R28_T_HTM;
		}

		public void setR28_T_HTM(BigDecimal r28_T_HTM) {
			R28_T_HTM = r28_T_HTM;
		}

		public BigDecimal getR28_T_TOTAL() {
			return R28_T_TOTAL;
		}

		public void setR28_T_TOTAL(BigDecimal r28_T_TOTAL) {
			R28_T_TOTAL = r28_T_TOTAL;
		}

		// Getters and Setters for R29
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_0_1Y_FT() {
			return R29_0_1Y_FT;
		}

		public void setR29_0_1Y_FT(BigDecimal r29_0_1y_FT) {
			R29_0_1Y_FT = r29_0_1y_FT;
		}

		public BigDecimal getR29_0_1Y_HTM() {
			return R29_0_1Y_HTM;
		}

		public void setR29_0_1Y_HTM(BigDecimal r29_0_1y_HTM) {
			R29_0_1Y_HTM = r29_0_1y_HTM;
		}

		public BigDecimal getR29_0_1Y_TOTAL() {
			return R29_0_1Y_TOTAL;
		}

		public void setR29_0_1Y_TOTAL(BigDecimal r29_0_1y_TOTAL) {
			R29_0_1Y_TOTAL = r29_0_1y_TOTAL;
		}

		public BigDecimal getR29_1_5Y_FT() {
			return R29_1_5Y_FT;
		}

		public void setR29_1_5Y_FT(BigDecimal r29_1_5y_FT) {
			R29_1_5Y_FT = r29_1_5y_FT;
		}

		public BigDecimal getR29_1_5Y_HTM() {
			return R29_1_5Y_HTM;
		}

		public void setR29_1_5Y_HTM(BigDecimal r29_1_5y_HTM) {
			R29_1_5Y_HTM = r29_1_5y_HTM;
		}

		public BigDecimal getR29_1_5Y_TOTAL() {
			return R29_1_5Y_TOTAL;
		}

		public void setR29_1_5Y_TOTAL(BigDecimal r29_1_5y_TOTAL) {
			R29_1_5Y_TOTAL = r29_1_5y_TOTAL;
		}

		public BigDecimal getR29_O5Y_FT() {
			return R29_O5Y_FT;
		}

		public void setR29_O5Y_FT(BigDecimal r29_O5Y_FT) {
			R29_O5Y_FT = r29_O5Y_FT;
		}

		public BigDecimal getR29_O5Y_HTM() {
			return R29_O5Y_HTM;
		}

		public void setR29_O5Y_HTM(BigDecimal r29_O5Y_HTM) {
			R29_O5Y_HTM = r29_O5Y_HTM;
		}

		public BigDecimal getR29_O5Y_TOTAL() {
			return R29_O5Y_TOTAL;
		}

		public void setR29_O5Y_TOTAL(BigDecimal r29_O5Y_TOTAL) {
			R29_O5Y_TOTAL = r29_O5Y_TOTAL;
		}

		public BigDecimal getR29_T_FT() {
			return R29_T_FT;
		}

		public void setR29_T_FT(BigDecimal r29_T_FT) {
			R29_T_FT = r29_T_FT;
		}

		public BigDecimal getR29_T_HTM() {
			return R29_T_HTM;
		}

		public void setR29_T_HTM(BigDecimal r29_T_HTM) {
			R29_T_HTM = r29_T_HTM;
		}

		public BigDecimal getR29_T_TOTAL() {
			return R29_T_TOTAL;
		}

		public void setR29_T_TOTAL(BigDecimal r29_T_TOTAL) {
			R29_T_TOTAL = r29_T_TOTAL;
		}

		// Getters and Setters for R30
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_0_1Y_FT() {
			return R30_0_1Y_FT;
		}

		public void setR30_0_1Y_FT(BigDecimal r30_0_1y_FT) {
			R30_0_1Y_FT = r30_0_1y_FT;
		}

		public BigDecimal getR30_0_1Y_HTM() {
			return R30_0_1Y_HTM;
		}

		public void setR30_0_1Y_HTM(BigDecimal r30_0_1y_HTM) {
			R30_0_1Y_HTM = r30_0_1y_HTM;
		}

		public BigDecimal getR30_0_1Y_TOTAL() {
			return R30_0_1Y_TOTAL;
		}

		public void setR30_0_1Y_TOTAL(BigDecimal r30_0_1y_TOTAL) {
			R30_0_1Y_TOTAL = r30_0_1y_TOTAL;
		}

		public BigDecimal getR30_1_5Y_FT() {
			return R30_1_5Y_FT;
		}

		public void setR30_1_5Y_FT(BigDecimal r30_1_5y_FT) {
			R30_1_5Y_FT = r30_1_5y_FT;
		}

		public BigDecimal getR30_1_5Y_HTM() {
			return R30_1_5Y_HTM;
		}

		public void setR30_1_5Y_HTM(BigDecimal r30_1_5y_HTM) {
			R30_1_5Y_HTM = r30_1_5y_HTM;
		}

		public BigDecimal getR30_1_5Y_TOTAL() {
			return R30_1_5Y_TOTAL;
		}

		public void setR30_1_5Y_TOTAL(BigDecimal r30_1_5y_TOTAL) {
			R30_1_5Y_TOTAL = r30_1_5y_TOTAL;
		}

		public BigDecimal getR30_O5Y_FT() {
			return R30_O5Y_FT;
		}

		public void setR30_O5Y_FT(BigDecimal r30_O5Y_FT) {
			R30_O5Y_FT = r30_O5Y_FT;
		}

		public BigDecimal getR30_O5Y_HTM() {
			return R30_O5Y_HTM;
		}

		public void setR30_O5Y_HTM(BigDecimal r30_O5Y_HTM) {
			R30_O5Y_HTM = r30_O5Y_HTM;
		}

		public BigDecimal getR30_O5Y_TOTAL() {
			return R30_O5Y_TOTAL;
		}

		public void setR30_O5Y_TOTAL(BigDecimal r30_O5Y_TOTAL) {
			R30_O5Y_TOTAL = r30_O5Y_TOTAL;
		}

		public BigDecimal getR30_T_FT() {
			return R30_T_FT;
		}

		public void setR30_T_FT(BigDecimal r30_T_FT) {
			R30_T_FT = r30_T_FT;
		}

		public BigDecimal getR30_T_HTM() {
			return R30_T_HTM;
		}

		public void setR30_T_HTM(BigDecimal r30_T_HTM) {
			R30_T_HTM = r30_T_HTM;
		}

		public BigDecimal getR30_T_TOTAL() {
			return R30_T_TOTAL;
		}

		public void setR30_T_TOTAL(BigDecimal r30_T_TOTAL) {
			R30_T_TOTAL = r30_T_TOTAL;
		}

		// Getters and Setters for R31
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_0_1Y_FT() {
			return R31_0_1Y_FT;
		}

		public void setR31_0_1Y_FT(BigDecimal r31_0_1y_FT) {
			R31_0_1Y_FT = r31_0_1y_FT;
		}

		public BigDecimal getR31_0_1Y_HTM() {
			return R31_0_1Y_HTM;
		}

		public void setR31_0_1Y_HTM(BigDecimal r31_0_1y_HTM) {
			R31_0_1Y_HTM = r31_0_1y_HTM;
		}

		public BigDecimal getR31_0_1Y_TOTAL() {
			return R31_0_1Y_TOTAL;
		}

		public void setR31_0_1Y_TOTAL(BigDecimal r31_0_1y_TOTAL) {
			R31_0_1Y_TOTAL = r31_0_1y_TOTAL;
		}

		public BigDecimal getR31_1_5Y_FT() {
			return R31_1_5Y_FT;
		}

		public void setR31_1_5Y_FT(BigDecimal r31_1_5y_FT) {
			R31_1_5Y_FT = r31_1_5y_FT;
		}

		public BigDecimal getR31_1_5Y_HTM() {
			return R31_1_5Y_HTM;
		}

		public void setR31_1_5Y_HTM(BigDecimal r31_1_5y_HTM) {
			R31_1_5Y_HTM = r31_1_5y_HTM;
		}

		public BigDecimal getR31_1_5Y_TOTAL() {
			return R31_1_5Y_TOTAL;
		}

		public void setR31_1_5Y_TOTAL(BigDecimal r31_1_5y_TOTAL) {
			R31_1_5Y_TOTAL = r31_1_5y_TOTAL;
		}

		public BigDecimal getR31_O5Y_FT() {
			return R31_O5Y_FT;
		}

		public void setR31_O5Y_FT(BigDecimal r31_O5Y_FT) {
			R31_O5Y_FT = r31_O5Y_FT;
		}

		public BigDecimal getR31_O5Y_HTM() {
			return R31_O5Y_HTM;
		}

		public void setR31_O5Y_HTM(BigDecimal r31_O5Y_HTM) {
			R31_O5Y_HTM = r31_O5Y_HTM;
		}

		public BigDecimal getR31_O5Y_TOTAL() {
			return R31_O5Y_TOTAL;
		}

		public void setR31_O5Y_TOTAL(BigDecimal r31_O5Y_TOTAL) {
			R31_O5Y_TOTAL = r31_O5Y_TOTAL;
		}

		public BigDecimal getR31_T_FT() {
			return R31_T_FT;
		}

		public void setR31_T_FT(BigDecimal r31_T_FT) {
			R31_T_FT = r31_T_FT;
		}

		public BigDecimal getR31_T_HTM() {
			return R31_T_HTM;
		}

		public void setR31_T_HTM(BigDecimal r31_T_HTM) {
			R31_T_HTM = r31_T_HTM;
		}

		public BigDecimal getR31_T_TOTAL() {
			return R31_T_TOTAL;
		}

		public void setR31_T_TOTAL(BigDecimal r31_T_TOTAL) {
			R31_T_TOTAL = r31_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//===========================
//M_SEC_RESUB_Detail_RowMapper4
//===========================

	public class M_SEC_RESUB_Detail_RowMapper4 implements RowMapper<M_SEC_RESUB_Detail_Entity4> {

		@Override
		public M_SEC_RESUB_Detail_Entity4 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_SEC_RESUB_Detail_Entity4 obj = new M_SEC_RESUB_Detail_Entity4();

			// =========================
			// R36
			// =========================
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_0_1Y_FT(rs.getBigDecimal("R36_0_1Y_FT"));
			obj.setR36_0_1Y_HTM(rs.getBigDecimal("R36_0_1Y_HTM"));
			obj.setR36_0_1Y_TOTAL(rs.getBigDecimal("R36_0_1Y_TOTAL"));
			obj.setR36_1_5Y_FT(rs.getBigDecimal("R36_1_5Y_FT"));
			obj.setR36_1_5Y_HTM(rs.getBigDecimal("R36_1_5Y_HTM"));
			obj.setR36_1_5Y_TOTAL(rs.getBigDecimal("R36_1_5Y_TOTAL"));
			obj.setR36_O5Y_FT(rs.getBigDecimal("R36_O5Y_FT"));
			obj.setR36_O5Y_HTM(rs.getBigDecimal("R36_O5Y_HTM"));
			obj.setR36_O5Y_TOTAL(rs.getBigDecimal("R36_O5Y_TOTAL"));
			obj.setR36_T_FT(rs.getBigDecimal("R36_T_FT"));
			obj.setR36_T_HTM(rs.getBigDecimal("R36_T_HTM"));
			obj.setR36_T_TOTAL(rs.getBigDecimal("R36_T_TOTAL"));

			// =========================
			// R37
			// =========================
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_0_1Y_FT(rs.getBigDecimal("R37_0_1Y_FT"));
			obj.setR37_0_1Y_HTM(rs.getBigDecimal("R37_0_1Y_HTM"));
			obj.setR37_0_1Y_TOTAL(rs.getBigDecimal("R37_0_1Y_TOTAL"));
			obj.setR37_1_5Y_FT(rs.getBigDecimal("R37_1_5Y_FT"));
			obj.setR37_1_5Y_HTM(rs.getBigDecimal("R37_1_5Y_HTM"));
			obj.setR37_1_5Y_TOTAL(rs.getBigDecimal("R37_1_5Y_TOTAL"));
			obj.setR37_O5Y_FT(rs.getBigDecimal("R37_O5Y_FT"));
			obj.setR37_O5Y_HTM(rs.getBigDecimal("R37_O5Y_HTM"));
			obj.setR37_O5Y_TOTAL(rs.getBigDecimal("R37_O5Y_TOTAL"));
			obj.setR37_T_FT(rs.getBigDecimal("R37_T_FT"));
			obj.setR37_T_HTM(rs.getBigDecimal("R37_T_HTM"));
			obj.setR37_T_TOTAL(rs.getBigDecimal("R37_T_TOTAL"));

			// =========================
			// R38
			// =========================
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_0_1Y_FT(rs.getBigDecimal("R38_0_1Y_FT"));
			obj.setR38_0_1Y_HTM(rs.getBigDecimal("R38_0_1Y_HTM"));
			obj.setR38_0_1Y_TOTAL(rs.getBigDecimal("R38_0_1Y_TOTAL"));
			obj.setR38_1_5Y_FT(rs.getBigDecimal("R38_1_5Y_FT"));
			obj.setR38_1_5Y_HTM(rs.getBigDecimal("R38_1_5Y_HTM"));
			obj.setR38_1_5Y_TOTAL(rs.getBigDecimal("R38_1_5Y_TOTAL"));
			obj.setR38_O5Y_FT(rs.getBigDecimal("R38_O5Y_FT"));
			obj.setR38_O5Y_HTM(rs.getBigDecimal("R38_O5Y_HTM"));
			obj.setR38_O5Y_TOTAL(rs.getBigDecimal("R38_O5Y_TOTAL"));
			obj.setR38_T_FT(rs.getBigDecimal("R38_T_FT"));
			obj.setR38_T_HTM(rs.getBigDecimal("R38_T_HTM"));
			obj.setR38_T_TOTAL(rs.getBigDecimal("R38_T_TOTAL"));

			// =========================
			// R39
			// =========================
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_0_1Y_FT(rs.getBigDecimal("R39_0_1Y_FT"));
			obj.setR39_0_1Y_HTM(rs.getBigDecimal("R39_0_1Y_HTM"));
			obj.setR39_0_1Y_TOTAL(rs.getBigDecimal("R39_0_1Y_TOTAL"));
			obj.setR39_1_5Y_FT(rs.getBigDecimal("R39_1_5Y_FT"));
			obj.setR39_1_5Y_HTM(rs.getBigDecimal("R39_1_5Y_HTM"));
			obj.setR39_1_5Y_TOTAL(rs.getBigDecimal("R39_1_5Y_TOTAL"));
			obj.setR39_O5Y_FT(rs.getBigDecimal("R39_O5Y_FT"));
			obj.setR39_O5Y_HTM(rs.getBigDecimal("R39_O5Y_HTM"));
			obj.setR39_O5Y_TOTAL(rs.getBigDecimal("R39_O5Y_TOTAL"));
			obj.setR39_T_FT(rs.getBigDecimal("R39_T_FT"));
			obj.setR39_T_HTM(rs.getBigDecimal("R39_T_HTM"));
			obj.setR39_T_TOTAL(rs.getBigDecimal("R39_T_TOTAL"));

			// =========================
			// R40
			// =========================
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_0_1Y_FT(rs.getBigDecimal("R40_0_1Y_FT"));
			obj.setR40_0_1Y_HTM(rs.getBigDecimal("R40_0_1Y_HTM"));
			obj.setR40_0_1Y_TOTAL(rs.getBigDecimal("R40_0_1Y_TOTAL"));
			obj.setR40_1_5Y_FT(rs.getBigDecimal("R40_1_5Y_FT"));
			obj.setR40_1_5Y_HTM(rs.getBigDecimal("R40_1_5Y_HTM"));
			obj.setR40_1_5Y_TOTAL(rs.getBigDecimal("R40_1_5Y_TOTAL"));
			obj.setR40_O5Y_FT(rs.getBigDecimal("R40_O5Y_FT"));
			obj.setR40_O5Y_HTM(rs.getBigDecimal("R40_O5Y_HTM"));
			obj.setR40_O5Y_TOTAL(rs.getBigDecimal("R40_O5Y_TOTAL"));
			obj.setR40_T_FT(rs.getBigDecimal("R40_T_FT"));
			obj.setR40_T_HTM(rs.getBigDecimal("R40_T_HTM"));
			obj.setR40_T_TOTAL(rs.getBigDecimal("R40_T_TOTAL"));

			// =========================
			// R41
			// =========================
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_0_1Y_FT(rs.getBigDecimal("R41_0_1Y_FT"));
			obj.setR41_0_1Y_HTM(rs.getBigDecimal("R41_0_1Y_HTM"));
			obj.setR41_0_1Y_TOTAL(rs.getBigDecimal("R41_0_1Y_TOTAL"));
			obj.setR41_1_5Y_FT(rs.getBigDecimal("R41_1_5Y_FT"));
			obj.setR41_1_5Y_HTM(rs.getBigDecimal("R41_1_5Y_HTM"));
			obj.setR41_1_5Y_TOTAL(rs.getBigDecimal("R41_1_5Y_TOTAL"));
			obj.setR41_O5Y_FT(rs.getBigDecimal("R41_O5Y_FT"));
			obj.setR41_O5Y_HTM(rs.getBigDecimal("R41_O5Y_HTM"));
			obj.setR41_O5Y_TOTAL(rs.getBigDecimal("R41_O5Y_TOTAL"));
			obj.setR41_T_FT(rs.getBigDecimal("R41_T_FT"));
			obj.setR41_T_HTM(rs.getBigDecimal("R41_T_HTM"));
			obj.setR41_T_TOTAL(rs.getBigDecimal("R41_T_TOTAL"));

			// =========================
			// R42
			// =========================
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_0_1Y_FT(rs.getBigDecimal("R42_0_1Y_FT"));
			obj.setR42_0_1Y_HTM(rs.getBigDecimal("R42_0_1Y_HTM"));
			obj.setR42_0_1Y_TOTAL(rs.getBigDecimal("R42_0_1Y_TOTAL"));
			obj.setR42_1_5Y_FT(rs.getBigDecimal("R42_1_5Y_FT"));
			obj.setR42_1_5Y_HTM(rs.getBigDecimal("R42_1_5Y_HTM"));
			obj.setR42_1_5Y_TOTAL(rs.getBigDecimal("R42_1_5Y_TOTAL"));
			obj.setR42_O5Y_FT(rs.getBigDecimal("R42_O5Y_FT"));
			obj.setR42_O5Y_HTM(rs.getBigDecimal("R42_O5Y_HTM"));
			obj.setR42_O5Y_TOTAL(rs.getBigDecimal("R42_O5Y_TOTAL"));
			obj.setR42_T_FT(rs.getBigDecimal("R42_T_FT"));
			obj.setR42_T_HTM(rs.getBigDecimal("R42_T_HTM"));
			obj.setR42_T_TOTAL(rs.getBigDecimal("R42_T_TOTAL"));

			// =========================
			// R43
			// =========================
			obj.setR43_PRODUCT(rs.getString("R43_PRODUCT"));
			obj.setR43_0_1Y_FT(rs.getBigDecimal("R43_0_1Y_FT"));
			obj.setR43_0_1Y_HTM(rs.getBigDecimal("R43_0_1Y_HTM"));
			obj.setR43_0_1Y_TOTAL(rs.getBigDecimal("R43_0_1Y_TOTAL"));
			obj.setR43_1_5Y_FT(rs.getBigDecimal("R43_1_5Y_FT"));
			obj.setR43_1_5Y_HTM(rs.getBigDecimal("R43_1_5Y_HTM"));
			obj.setR43_1_5Y_TOTAL(rs.getBigDecimal("R43_1_5Y_TOTAL"));
			obj.setR43_O5Y_FT(rs.getBigDecimal("R43_O5Y_FT"));
			obj.setR43_O5Y_HTM(rs.getBigDecimal("R43_O5Y_HTM"));
			obj.setR43_O5Y_TOTAL(rs.getBigDecimal("R43_O5Y_TOTAL"));
			obj.setR43_T_FT(rs.getBigDecimal("R43_T_FT"));
			obj.setR43_T_HTM(rs.getBigDecimal("R43_T_HTM"));
			obj.setR43_T_TOTAL(rs.getBigDecimal("R43_T_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("reportResubDate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

//===========================
//M_SEC_RESUB_Detail_Entity4
//===========================

	public class M_SEC_RESUB_Detail_Entity4 {

		private String R36_PRODUCT;
		private BigDecimal R36_0_1Y_FT;
		private BigDecimal R36_0_1Y_HTM;
		private BigDecimal R36_0_1Y_TOTAL;
		private BigDecimal R36_1_5Y_FT;
		private BigDecimal R36_1_5Y_HTM;
		private BigDecimal R36_1_5Y_TOTAL;
		private BigDecimal R36_O5Y_FT;
		private BigDecimal R36_O5Y_HTM;
		private BigDecimal R36_O5Y_TOTAL;
		private BigDecimal R36_T_FT;
		private BigDecimal R36_T_HTM;
		private BigDecimal R36_T_TOTAL;

		private String R37_PRODUCT;
		private BigDecimal R37_0_1Y_FT;
		private BigDecimal R37_0_1Y_HTM;
		private BigDecimal R37_0_1Y_TOTAL;
		private BigDecimal R37_1_5Y_FT;
		private BigDecimal R37_1_5Y_HTM;
		private BigDecimal R37_1_5Y_TOTAL;
		private BigDecimal R37_O5Y_FT;
		private BigDecimal R37_O5Y_HTM;
		private BigDecimal R37_O5Y_TOTAL;
		private BigDecimal R37_T_FT;
		private BigDecimal R37_T_HTM;
		private BigDecimal R37_T_TOTAL;

		private String R38_PRODUCT;
		private BigDecimal R38_0_1Y_FT;
		private BigDecimal R38_0_1Y_HTM;
		private BigDecimal R38_0_1Y_TOTAL;
		private BigDecimal R38_1_5Y_FT;
		private BigDecimal R38_1_5Y_HTM;
		private BigDecimal R38_1_5Y_TOTAL;
		private BigDecimal R38_O5Y_FT;
		private BigDecimal R38_O5Y_HTM;
		private BigDecimal R38_O5Y_TOTAL;
		private BigDecimal R38_T_FT;
		private BigDecimal R38_T_HTM;
		private BigDecimal R38_T_TOTAL;

		private String R39_PRODUCT;
		private BigDecimal R39_0_1Y_FT;
		private BigDecimal R39_0_1Y_HTM;
		private BigDecimal R39_0_1Y_TOTAL;
		private BigDecimal R39_1_5Y_FT;
		private BigDecimal R39_1_5Y_HTM;
		private BigDecimal R39_1_5Y_TOTAL;
		private BigDecimal R39_O5Y_FT;
		private BigDecimal R39_O5Y_HTM;
		private BigDecimal R39_O5Y_TOTAL;
		private BigDecimal R39_T_FT;
		private BigDecimal R39_T_HTM;
		private BigDecimal R39_T_TOTAL;

		private String R40_PRODUCT;
		private BigDecimal R40_0_1Y_FT;
		private BigDecimal R40_0_1Y_HTM;
		private BigDecimal R40_0_1Y_TOTAL;
		private BigDecimal R40_1_5Y_FT;
		private BigDecimal R40_1_5Y_HTM;
		private BigDecimal R40_1_5Y_TOTAL;
		private BigDecimal R40_O5Y_FT;
		private BigDecimal R40_O5Y_HTM;
		private BigDecimal R40_O5Y_TOTAL;
		private BigDecimal R40_T_FT;
		private BigDecimal R40_T_HTM;
		private BigDecimal R40_T_TOTAL;

		private String R41_PRODUCT;
		private BigDecimal R41_0_1Y_FT;
		private BigDecimal R41_0_1Y_HTM;
		private BigDecimal R41_0_1Y_TOTAL;
		private BigDecimal R41_1_5Y_FT;
		private BigDecimal R41_1_5Y_HTM;
		private BigDecimal R41_1_5Y_TOTAL;
		private BigDecimal R41_O5Y_FT;
		private BigDecimal R41_O5Y_HTM;
		private BigDecimal R41_O5Y_TOTAL;
		private BigDecimal R41_T_FT;
		private BigDecimal R41_T_HTM;
		private BigDecimal R41_T_TOTAL;

		private String R42_PRODUCT;
		private BigDecimal R42_0_1Y_FT;
		private BigDecimal R42_0_1Y_HTM;
		private BigDecimal R42_0_1Y_TOTAL;
		private BigDecimal R42_1_5Y_FT;
		private BigDecimal R42_1_5Y_HTM;
		private BigDecimal R42_1_5Y_TOTAL;
		private BigDecimal R42_O5Y_FT;
		private BigDecimal R42_O5Y_HTM;
		private BigDecimal R42_O5Y_TOTAL;
		private BigDecimal R42_T_FT;
		private BigDecimal R42_T_HTM;
		private BigDecimal R42_T_TOTAL;

		private String R43_PRODUCT;
		private BigDecimal R43_0_1Y_FT;
		private BigDecimal R43_0_1Y_HTM;
		private BigDecimal R43_0_1Y_TOTAL;
		private BigDecimal R43_1_5Y_FT;
		private BigDecimal R43_1_5Y_HTM;
		private BigDecimal R43_1_5Y_TOTAL;
		private BigDecimal R43_O5Y_FT;
		private BigDecimal R43_O5Y_HTM;
		private BigDecimal R43_O5Y_TOTAL;
		private BigDecimal R43_T_FT;
		private BigDecimal R43_T_HTM;
		private BigDecimal R43_T_TOTAL;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public M_SEC_RESUB_Detail_Entity4() {
			super();
		}

		// Getters and Setters for R36
		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_0_1Y_FT() {
			return R36_0_1Y_FT;
		}

		public void setR36_0_1Y_FT(BigDecimal r36_0_1y_FT) {
			R36_0_1Y_FT = r36_0_1y_FT;
		}

		public BigDecimal getR36_0_1Y_HTM() {
			return R36_0_1Y_HTM;
		}

		public void setR36_0_1Y_HTM(BigDecimal r36_0_1y_HTM) {
			R36_0_1Y_HTM = r36_0_1y_HTM;
		}

		public BigDecimal getR36_0_1Y_TOTAL() {
			return R36_0_1Y_TOTAL;
		}

		public void setR36_0_1Y_TOTAL(BigDecimal r36_0_1y_TOTAL) {
			R36_0_1Y_TOTAL = r36_0_1y_TOTAL;
		}

		public BigDecimal getR36_1_5Y_FT() {
			return R36_1_5Y_FT;
		}

		public void setR36_1_5Y_FT(BigDecimal r36_1_5y_FT) {
			R36_1_5Y_FT = r36_1_5y_FT;
		}

		public BigDecimal getR36_1_5Y_HTM() {
			return R36_1_5Y_HTM;
		}

		public void setR36_1_5Y_HTM(BigDecimal r36_1_5y_HTM) {
			R36_1_5Y_HTM = r36_1_5y_HTM;
		}

		public BigDecimal getR36_1_5Y_TOTAL() {
			return R36_1_5Y_TOTAL;
		}

		public void setR36_1_5Y_TOTAL(BigDecimal r36_1_5y_TOTAL) {
			R36_1_5Y_TOTAL = r36_1_5y_TOTAL;
		}

		public BigDecimal getR36_O5Y_FT() {
			return R36_O5Y_FT;
		}

		public void setR36_O5Y_FT(BigDecimal r36_O5Y_FT) {
			R36_O5Y_FT = r36_O5Y_FT;
		}

		public BigDecimal getR36_O5Y_HTM() {
			return R36_O5Y_HTM;
		}

		public void setR36_O5Y_HTM(BigDecimal r36_O5Y_HTM) {
			R36_O5Y_HTM = r36_O5Y_HTM;
		}

		public BigDecimal getR36_O5Y_TOTAL() {
			return R36_O5Y_TOTAL;
		}

		public void setR36_O5Y_TOTAL(BigDecimal r36_O5Y_TOTAL) {
			R36_O5Y_TOTAL = r36_O5Y_TOTAL;
		}

		public BigDecimal getR36_T_FT() {
			return R36_T_FT;
		}

		public void setR36_T_FT(BigDecimal r36_T_FT) {
			R36_T_FT = r36_T_FT;
		}

		public BigDecimal getR36_T_HTM() {
			return R36_T_HTM;
		}

		public void setR36_T_HTM(BigDecimal r36_T_HTM) {
			R36_T_HTM = r36_T_HTM;
		}

		public BigDecimal getR36_T_TOTAL() {
			return R36_T_TOTAL;
		}

		public void setR36_T_TOTAL(BigDecimal r36_T_TOTAL) {
			R36_T_TOTAL = r36_T_TOTAL;
		}

		// Getters and Setters for R37
		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_0_1Y_FT() {
			return R37_0_1Y_FT;
		}

		public void setR37_0_1Y_FT(BigDecimal r37_0_1y_FT) {
			R37_0_1Y_FT = r37_0_1y_FT;
		}

		public BigDecimal getR37_0_1Y_HTM() {
			return R37_0_1Y_HTM;
		}

		public void setR37_0_1Y_HTM(BigDecimal r37_0_1y_HTM) {
			R37_0_1Y_HTM = r37_0_1y_HTM;
		}

		public BigDecimal getR37_0_1Y_TOTAL() {
			return R37_0_1Y_TOTAL;
		}

		public void setR37_0_1Y_TOTAL(BigDecimal r37_0_1y_TOTAL) {
			R37_0_1Y_TOTAL = r37_0_1y_TOTAL;
		}

		public BigDecimal getR37_1_5Y_FT() {
			return R37_1_5Y_FT;
		}

		public void setR37_1_5Y_FT(BigDecimal r37_1_5y_FT) {
			R37_1_5Y_FT = r37_1_5y_FT;
		}

		public BigDecimal getR37_1_5Y_HTM() {
			return R37_1_5Y_HTM;
		}

		public void setR37_1_5Y_HTM(BigDecimal r37_1_5y_HTM) {
			R37_1_5Y_HTM = r37_1_5y_HTM;
		}

		public BigDecimal getR37_1_5Y_TOTAL() {
			return R37_1_5Y_TOTAL;
		}

		public void setR37_1_5Y_TOTAL(BigDecimal r37_1_5y_TOTAL) {
			R37_1_5Y_TOTAL = r37_1_5y_TOTAL;
		}

		public BigDecimal getR37_O5Y_FT() {
			return R37_O5Y_FT;
		}

		public void setR37_O5Y_FT(BigDecimal r37_O5Y_FT) {
			R37_O5Y_FT = r37_O5Y_FT;
		}

		public BigDecimal getR37_O5Y_HTM() {
			return R37_O5Y_HTM;
		}

		public void setR37_O5Y_HTM(BigDecimal r37_O5Y_HTM) {
			R37_O5Y_HTM = r37_O5Y_HTM;
		}

		public BigDecimal getR37_O5Y_TOTAL() {
			return R37_O5Y_TOTAL;
		}

		public void setR37_O5Y_TOTAL(BigDecimal r37_O5Y_TOTAL) {
			R37_O5Y_TOTAL = r37_O5Y_TOTAL;
		}

		public BigDecimal getR37_T_FT() {
			return R37_T_FT;
		}

		public void setR37_T_FT(BigDecimal r37_T_FT) {
			R37_T_FT = r37_T_FT;
		}

		public BigDecimal getR37_T_HTM() {
			return R37_T_HTM;
		}

		public void setR37_T_HTM(BigDecimal r37_T_HTM) {
			R37_T_HTM = r37_T_HTM;
		}

		public BigDecimal getR37_T_TOTAL() {
			return R37_T_TOTAL;
		}

		public void setR37_T_TOTAL(BigDecimal r37_T_TOTAL) {
			R37_T_TOTAL = r37_T_TOTAL;
		}

		// Getters and Setters for R38
		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_0_1Y_FT() {
			return R38_0_1Y_FT;
		}

		public void setR38_0_1Y_FT(BigDecimal r38_0_1y_FT) {
			R38_0_1Y_FT = r38_0_1y_FT;
		}

		public BigDecimal getR38_0_1Y_HTM() {
			return R38_0_1Y_HTM;
		}

		public void setR38_0_1Y_HTM(BigDecimal r38_0_1y_HTM) {
			R38_0_1Y_HTM = r38_0_1y_HTM;
		}

		public BigDecimal getR38_0_1Y_TOTAL() {
			return R38_0_1Y_TOTAL;
		}

		public void setR38_0_1Y_TOTAL(BigDecimal r38_0_1y_TOTAL) {
			R38_0_1Y_TOTAL = r38_0_1y_TOTAL;
		}

		public BigDecimal getR38_1_5Y_FT() {
			return R38_1_5Y_FT;
		}

		public void setR38_1_5Y_FT(BigDecimal r38_1_5y_FT) {
			R38_1_5Y_FT = r38_1_5y_FT;
		}

		public BigDecimal getR38_1_5Y_HTM() {
			return R38_1_5Y_HTM;
		}

		public void setR38_1_5Y_HTM(BigDecimal r38_1_5y_HTM) {
			R38_1_5Y_HTM = r38_1_5y_HTM;
		}

		public BigDecimal getR38_1_5Y_TOTAL() {
			return R38_1_5Y_TOTAL;
		}

		public void setR38_1_5Y_TOTAL(BigDecimal r38_1_5y_TOTAL) {
			R38_1_5Y_TOTAL = r38_1_5y_TOTAL;
		}

		public BigDecimal getR38_O5Y_FT() {
			return R38_O5Y_FT;
		}

		public void setR38_O5Y_FT(BigDecimal r38_O5Y_FT) {
			R38_O5Y_FT = r38_O5Y_FT;
		}

		public BigDecimal getR38_O5Y_HTM() {
			return R38_O5Y_HTM;
		}

		public void setR38_O5Y_HTM(BigDecimal r38_O5Y_HTM) {
			R38_O5Y_HTM = r38_O5Y_HTM;
		}

		public BigDecimal getR38_O5Y_TOTAL() {
			return R38_O5Y_TOTAL;
		}

		public void setR38_O5Y_TOTAL(BigDecimal r38_O5Y_TOTAL) {
			R38_O5Y_TOTAL = r38_O5Y_TOTAL;
		}

		public BigDecimal getR38_T_FT() {
			return R38_T_FT;
		}

		public void setR38_T_FT(BigDecimal r38_T_FT) {
			R38_T_FT = r38_T_FT;
		}

		public BigDecimal getR38_T_HTM() {
			return R38_T_HTM;
		}

		public void setR38_T_HTM(BigDecimal r38_T_HTM) {
			R38_T_HTM = r38_T_HTM;
		}

		public BigDecimal getR38_T_TOTAL() {
			return R38_T_TOTAL;
		}

		public void setR38_T_TOTAL(BigDecimal r38_T_TOTAL) {
			R38_T_TOTAL = r38_T_TOTAL;
		}

		// Getters and Setters for R39
		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_0_1Y_FT() {
			return R39_0_1Y_FT;
		}

		public void setR39_0_1Y_FT(BigDecimal r39_0_1y_FT) {
			R39_0_1Y_FT = r39_0_1y_FT;
		}

		public BigDecimal getR39_0_1Y_HTM() {
			return R39_0_1Y_HTM;
		}

		public void setR39_0_1Y_HTM(BigDecimal r39_0_1y_HTM) {
			R39_0_1Y_HTM = r39_0_1y_HTM;
		}

		public BigDecimal getR39_0_1Y_TOTAL() {
			return R39_0_1Y_TOTAL;
		}

		public void setR39_0_1Y_TOTAL(BigDecimal r39_0_1y_TOTAL) {
			R39_0_1Y_TOTAL = r39_0_1y_TOTAL;
		}

		public BigDecimal getR39_1_5Y_FT() {
			return R39_1_5Y_FT;
		}

		public void setR39_1_5Y_FT(BigDecimal r39_1_5y_FT) {
			R39_1_5Y_FT = r39_1_5y_FT;
		}

		public BigDecimal getR39_1_5Y_HTM() {
			return R39_1_5Y_HTM;
		}

		public void setR39_1_5Y_HTM(BigDecimal r39_1_5y_HTM) {
			R39_1_5Y_HTM = r39_1_5y_HTM;
		}

		public BigDecimal getR39_1_5Y_TOTAL() {
			return R39_1_5Y_TOTAL;
		}

		public void setR39_1_5Y_TOTAL(BigDecimal r39_1_5y_TOTAL) {
			R39_1_5Y_TOTAL = r39_1_5y_TOTAL;
		}

		public BigDecimal getR39_O5Y_FT() {
			return R39_O5Y_FT;
		}

		public void setR39_O5Y_FT(BigDecimal r39_O5Y_FT) {
			R39_O5Y_FT = r39_O5Y_FT;
		}

		public BigDecimal getR39_O5Y_HTM() {
			return R39_O5Y_HTM;
		}

		public void setR39_O5Y_HTM(BigDecimal r39_O5Y_HTM) {
			R39_O5Y_HTM = r39_O5Y_HTM;
		}

		public BigDecimal getR39_O5Y_TOTAL() {
			return R39_O5Y_TOTAL;
		}

		public void setR39_O5Y_TOTAL(BigDecimal r39_O5Y_TOTAL) {
			R39_O5Y_TOTAL = r39_O5Y_TOTAL;
		}

		public BigDecimal getR39_T_FT() {
			return R39_T_FT;
		}

		public void setR39_T_FT(BigDecimal r39_T_FT) {
			R39_T_FT = r39_T_FT;
		}

		public BigDecimal getR39_T_HTM() {
			return R39_T_HTM;
		}

		public void setR39_T_HTM(BigDecimal r39_T_HTM) {
			R39_T_HTM = r39_T_HTM;
		}

		public BigDecimal getR39_T_TOTAL() {
			return R39_T_TOTAL;
		}

		public void setR39_T_TOTAL(BigDecimal r39_T_TOTAL) {
			R39_T_TOTAL = r39_T_TOTAL;
		}

		// Getters and Setters for R40
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_0_1Y_FT() {
			return R40_0_1Y_FT;
		}

		public void setR40_0_1Y_FT(BigDecimal r40_0_1y_FT) {
			R40_0_1Y_FT = r40_0_1y_FT;
		}

		public BigDecimal getR40_0_1Y_HTM() {
			return R40_0_1Y_HTM;
		}

		public void setR40_0_1Y_HTM(BigDecimal r40_0_1y_HTM) {
			R40_0_1Y_HTM = r40_0_1y_HTM;
		}

		public BigDecimal getR40_0_1Y_TOTAL() {
			return R40_0_1Y_TOTAL;
		}

		public void setR40_0_1Y_TOTAL(BigDecimal r40_0_1y_TOTAL) {
			R40_0_1Y_TOTAL = r40_0_1y_TOTAL;
		}

		public BigDecimal getR40_1_5Y_FT() {
			return R40_1_5Y_FT;
		}

		public void setR40_1_5Y_FT(BigDecimal r40_1_5y_FT) {
			R40_1_5Y_FT = r40_1_5y_FT;
		}

		public BigDecimal getR40_1_5Y_HTM() {
			return R40_1_5Y_HTM;
		}

		public void setR40_1_5Y_HTM(BigDecimal r40_1_5y_HTM) {
			R40_1_5Y_HTM = r40_1_5y_HTM;
		}

		public BigDecimal getR40_1_5Y_TOTAL() {
			return R40_1_5Y_TOTAL;
		}

		public void setR40_1_5Y_TOTAL(BigDecimal r40_1_5y_TOTAL) {
			R40_1_5Y_TOTAL = r40_1_5y_TOTAL;
		}

		public BigDecimal getR40_O5Y_FT() {
			return R40_O5Y_FT;
		}

		public void setR40_O5Y_FT(BigDecimal r40_O5Y_FT) {
			R40_O5Y_FT = r40_O5Y_FT;
		}

		public BigDecimal getR40_O5Y_HTM() {
			return R40_O5Y_HTM;
		}

		public void setR40_O5Y_HTM(BigDecimal r40_O5Y_HTM) {
			R40_O5Y_HTM = r40_O5Y_HTM;
		}

		public BigDecimal getR40_O5Y_TOTAL() {
			return R40_O5Y_TOTAL;
		}

		public void setR40_O5Y_TOTAL(BigDecimal r40_O5Y_TOTAL) {
			R40_O5Y_TOTAL = r40_O5Y_TOTAL;
		}

		public BigDecimal getR40_T_FT() {
			return R40_T_FT;
		}

		public void setR40_T_FT(BigDecimal r40_T_FT) {
			R40_T_FT = r40_T_FT;
		}

		public BigDecimal getR40_T_HTM() {
			return R40_T_HTM;
		}

		public void setR40_T_HTM(BigDecimal r40_T_HTM) {
			R40_T_HTM = r40_T_HTM;
		}

		public BigDecimal getR40_T_TOTAL() {
			return R40_T_TOTAL;
		}

		public void setR40_T_TOTAL(BigDecimal r40_T_TOTAL) {
			R40_T_TOTAL = r40_T_TOTAL;
		}

		// Getters and Setters for R41
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_0_1Y_FT() {
			return R41_0_1Y_FT;
		}

		public void setR41_0_1Y_FT(BigDecimal r41_0_1y_FT) {
			R41_0_1Y_FT = r41_0_1y_FT;
		}

		public BigDecimal getR41_0_1Y_HTM() {
			return R41_0_1Y_HTM;
		}

		public void setR41_0_1Y_HTM(BigDecimal r41_0_1y_HTM) {
			R41_0_1Y_HTM = r41_0_1y_HTM;
		}

		public BigDecimal getR41_0_1Y_TOTAL() {
			return R41_0_1Y_TOTAL;
		}

		public void setR41_0_1Y_TOTAL(BigDecimal r41_0_1y_TOTAL) {
			R41_0_1Y_TOTAL = r41_0_1y_TOTAL;
		}

		public BigDecimal getR41_1_5Y_FT() {
			return R41_1_5Y_FT;
		}

		public void setR41_1_5Y_FT(BigDecimal r41_1_5y_FT) {
			R41_1_5Y_FT = r41_1_5y_FT;
		}

		public BigDecimal getR41_1_5Y_HTM() {
			return R41_1_5Y_HTM;
		}

		public void setR41_1_5Y_HTM(BigDecimal r41_1_5y_HTM) {
			R41_1_5Y_HTM = r41_1_5y_HTM;
		}

		public BigDecimal getR41_1_5Y_TOTAL() {
			return R41_1_5Y_TOTAL;
		}

		public void setR41_1_5Y_TOTAL(BigDecimal r41_1_5y_TOTAL) {
			R41_1_5Y_TOTAL = r41_1_5y_TOTAL;
		}

		public BigDecimal getR41_O5Y_FT() {
			return R41_O5Y_FT;
		}

		public void setR41_O5Y_FT(BigDecimal r41_O5Y_FT) {
			R41_O5Y_FT = r41_O5Y_FT;
		}

		public BigDecimal getR41_O5Y_HTM() {
			return R41_O5Y_HTM;
		}

		public void setR41_O5Y_HTM(BigDecimal r41_O5Y_HTM) {
			R41_O5Y_HTM = r41_O5Y_HTM;
		}

		public BigDecimal getR41_O5Y_TOTAL() {
			return R41_O5Y_TOTAL;
		}

		public void setR41_O5Y_TOTAL(BigDecimal r41_O5Y_TOTAL) {
			R41_O5Y_TOTAL = r41_O5Y_TOTAL;
		}

		public BigDecimal getR41_T_FT() {
			return R41_T_FT;
		}

		public void setR41_T_FT(BigDecimal r41_T_FT) {
			R41_T_FT = r41_T_FT;
		}

		public BigDecimal getR41_T_HTM() {
			return R41_T_HTM;
		}

		public void setR41_T_HTM(BigDecimal r41_T_HTM) {
			R41_T_HTM = r41_T_HTM;
		}

		public BigDecimal getR41_T_TOTAL() {
			return R41_T_TOTAL;
		}

		public void setR41_T_TOTAL(BigDecimal r41_T_TOTAL) {
			R41_T_TOTAL = r41_T_TOTAL;
		}

		// Getters and Setters for R42
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_0_1Y_FT() {
			return R42_0_1Y_FT;
		}

		public void setR42_0_1Y_FT(BigDecimal r42_0_1y_FT) {
			R42_0_1Y_FT = r42_0_1y_FT;
		}

		public BigDecimal getR42_0_1Y_HTM() {
			return R42_0_1Y_HTM;
		}

		public void setR42_0_1Y_HTM(BigDecimal r42_0_1y_HTM) {
			R42_0_1Y_HTM = r42_0_1y_HTM;
		}

		public BigDecimal getR42_0_1Y_TOTAL() {
			return R42_0_1Y_TOTAL;
		}

		public void setR42_0_1Y_TOTAL(BigDecimal r42_0_1y_TOTAL) {
			R42_0_1Y_TOTAL = r42_0_1y_TOTAL;
		}

		public BigDecimal getR42_1_5Y_FT() {
			return R42_1_5Y_FT;
		}

		public void setR42_1_5Y_FT(BigDecimal r42_1_5y_FT) {
			R42_1_5Y_FT = r42_1_5y_FT;
		}

		public BigDecimal getR42_1_5Y_HTM() {
			return R42_1_5Y_HTM;
		}

		public void setR42_1_5Y_HTM(BigDecimal r42_1_5y_HTM) {
			R42_1_5Y_HTM = r42_1_5y_HTM;
		}

		public BigDecimal getR42_1_5Y_TOTAL() {
			return R42_1_5Y_TOTAL;
		}

		public void setR42_1_5Y_TOTAL(BigDecimal r42_1_5y_TOTAL) {
			R42_1_5Y_TOTAL = r42_1_5y_TOTAL;
		}

		public BigDecimal getR42_O5Y_FT() {
			return R42_O5Y_FT;
		}

		public void setR42_O5Y_FT(BigDecimal r42_O5Y_FT) {
			R42_O5Y_FT = r42_O5Y_FT;
		}

		public BigDecimal getR42_O5Y_HTM() {
			return R42_O5Y_HTM;
		}

		public void setR42_O5Y_HTM(BigDecimal r42_O5Y_HTM) {
			R42_O5Y_HTM = r42_O5Y_HTM;
		}

		public BigDecimal getR42_O5Y_TOTAL() {
			return R42_O5Y_TOTAL;
		}

		public void setR42_O5Y_TOTAL(BigDecimal r42_O5Y_TOTAL) {
			R42_O5Y_TOTAL = r42_O5Y_TOTAL;
		}

		public BigDecimal getR42_T_FT() {
			return R42_T_FT;
		}

		public void setR42_T_FT(BigDecimal r42_T_FT) {
			R42_T_FT = r42_T_FT;
		}

		public BigDecimal getR42_T_HTM() {
			return R42_T_HTM;
		}

		public void setR42_T_HTM(BigDecimal r42_T_HTM) {
			R42_T_HTM = r42_T_HTM;
		}

		public BigDecimal getR42_T_TOTAL() {
			return R42_T_TOTAL;
		}

		public void setR42_T_TOTAL(BigDecimal r42_T_TOTAL) {
			R42_T_TOTAL = r42_T_TOTAL;
		}

		// Getters and Setters for R43
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public BigDecimal getR43_0_1Y_FT() {
			return R43_0_1Y_FT;
		}

		public void setR43_0_1Y_FT(BigDecimal r43_0_1y_FT) {
			R43_0_1Y_FT = r43_0_1y_FT;
		}

		public BigDecimal getR43_0_1Y_HTM() {
			return R43_0_1Y_HTM;
		}

		public void setR43_0_1Y_HTM(BigDecimal r43_0_1y_HTM) {
			R43_0_1Y_HTM = r43_0_1y_HTM;
		}

		public BigDecimal getR43_0_1Y_TOTAL() {
			return R43_0_1Y_TOTAL;
		}

		public void setR43_0_1Y_TOTAL(BigDecimal r43_0_1y_TOTAL) {
			R43_0_1Y_TOTAL = r43_0_1y_TOTAL;
		}

		public BigDecimal getR43_1_5Y_FT() {
			return R43_1_5Y_FT;
		}

		public void setR43_1_5Y_FT(BigDecimal r43_1_5y_FT) {
			R43_1_5Y_FT = r43_1_5y_FT;
		}

		public BigDecimal getR43_1_5Y_HTM() {
			return R43_1_5Y_HTM;
		}

		public void setR43_1_5Y_HTM(BigDecimal r43_1_5y_HTM) {
			R43_1_5Y_HTM = r43_1_5y_HTM;
		}

		public BigDecimal getR43_1_5Y_TOTAL() {
			return R43_1_5Y_TOTAL;
		}

		public void setR43_1_5Y_TOTAL(BigDecimal r43_1_5y_TOTAL) {
			R43_1_5Y_TOTAL = r43_1_5y_TOTAL;
		}

		public BigDecimal getR43_O5Y_FT() {
			return R43_O5Y_FT;
		}

		public void setR43_O5Y_FT(BigDecimal r43_O5Y_FT) {
			R43_O5Y_FT = r43_O5Y_FT;
		}

		public BigDecimal getR43_O5Y_HTM() {
			return R43_O5Y_HTM;
		}

		public void setR43_O5Y_HTM(BigDecimal r43_O5Y_HTM) {
			R43_O5Y_HTM = r43_O5Y_HTM;
		}

		public BigDecimal getR43_O5Y_TOTAL() {
			return R43_O5Y_TOTAL;
		}

		public void setR43_O5Y_TOTAL(BigDecimal r43_O5Y_TOTAL) {
			R43_O5Y_TOTAL = r43_O5Y_TOTAL;
		}

		public BigDecimal getR43_T_FT() {
			return R43_T_FT;
		}

		public void setR43_T_FT(BigDecimal r43_T_FT) {
			R43_T_FT = r43_T_FT;
		}

		public BigDecimal getR43_T_HTM() {
			return R43_T_HTM;
		}

		public void setR43_T_HTM(BigDecimal r43_T_HTM) {
			R43_T_HTM = r43_T_HTM;
		}

		public BigDecimal getR43_T_TOTAL() {
			return R43_T_TOTAL;
		}

		public void setR43_T_TOTAL(BigDecimal r43_T_TOTAL) {
			R43_T_TOTAL = r43_T_TOTAL;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
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

//=======================
//MODEL AND VIEW METHOD
//=======================

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SECView(String reportId, String fromdate, String todate, String currency, String dtltype,
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

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- ARCHIVAL SUMMARY ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<BRRS_M_SEC_Archival_Summary_Entity1> T1Master = getdatabydateListarchival1(d1, version);
				List<BRRS_M_SEC_Archival_Summary_Entity2> T2Master = getdatabydateListarchival2(d1, version);
				List<BRRS_M_SEC_Archival_Summary_Entity3> T3Master = getdatabydateListarchival3(d1, version);
				List<BRRS_M_SEC_Archival_Summary_Entity4> T4Master = getdatabydateListarchival4(d1, version);

				System.out.println("Archival Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary1", T1Master);
				mv.addObject("reportsummary2", T2Master);
				mv.addObject("reportsummary3", T3Master);
				mv.addObject("reportsummary4", T4Master);
			}

			// ---------- RESUB SUMMARY ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_SEC_RESUB_Summary_Entity1> T1Master = getdatabydateListarchival17(d1, version);
				List<M_SEC_RESUB_Summary_Entity2> T2Master = getdatabydateListarchival18(d1, version);
				List<M_SEC_RESUB_Summary_Entity3> T3Master = getdatabydateListarchival19(d1, version);
				List<M_SEC_RESUB_Summary_Entity4> T4Master = getdatabydateListarchival20(d1, version);

				System.out.println("Resub Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "resub");
				mv.addObject("reportsummary1", T1Master);
				mv.addObject("reportsummary2", T2Master);
				mv.addObject("reportsummary3", T3Master);
				mv.addObject("reportsummary4", T4Master);
			}

			// ---------- NORMAL SUMMARY ----------
			else {

				List<BRRS_M_SEC_Summary_Entity1> T1Master = getdatabydateList1(d1);
				List<BRRS_M_SEC_Summary_Entity2> T2Master = getdatabydateList2(d1);
				List<BRRS_M_SEC_Summary_Entity3> T3Master = getdatabydateList3(d1);
				List<BRRS_M_SEC_Summary_Entity4> T4Master = getdatabydateList4(d1);

				System.out.println("T1Master Size: " + T1Master.size());
				System.out.println("T2Master Size: " + T2Master.size());
				System.out.println("T3Master Size: " + T3Master.size());
				System.out.println("T4Master Size: " + T4Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary1", T1Master);
				mv.addObject("reportsummary2", T2Master);
				mv.addObject("reportsummary3", T3Master);
				mv.addObject("reportsummary4", T4Master);
			}

			// ===========================================================
			// DETAIL SECTION
			// ===========================================================

			if ("detail".equalsIgnoreCase(dtltype)) {

				// ---------- ARCHIVAL DETAIL ----------
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SEC_Archival_Detail1_Entity> T1Master = getdatabydateListarchival5(d1, version);
					List<M_SEC_Archival_Detail2_Entity> T2Master = getdatabydateListarchival6(d1, version);
					List<M_SEC_Archival_Detail3_Entity> T3Master = getdatabydateListarchival7(d1, version);
					List<M_SEC_Archival_Detail4_Entity> T4Master = getdatabydateListarchival8(d1, version);

					System.out.println("Archival Detail Size : " + T1Master.size());

					mv.addObject("reportsummary1", T1Master);
					mv.addObject("reportsummary2", T2Master);
					mv.addObject("reportsummary3", T3Master);
					mv.addObject("reportsummary4", T4Master);
				}

				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SEC_RESUB_Detail_Entity1> T1Master = getdatabydateListarchival21(d1, version);
					List<M_SEC_RESUB_Detail_Entity2> T2Master = getdatabydateListarchival22(d1, version);
					List<M_SEC_RESUB_Detail_Entity3> T3Master = getdatabydateListarchival23(d1, version);
					List<M_SEC_RESUB_Detail_Entity4> T4Master = getdatabydateListarchival24(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resub");
					mv.addObject("reportsummary1", T1Master);
					mv.addObject("reportsummary2", T2Master);
					mv.addObject("reportsummary3", T3Master);
					mv.addObject("reportsummary4", T4Master);
				}

				// ---------- NORMAL DETAIL ----------
				else {

					List<M_SEC_Detail1_Entity> T1Master = getdatabydateList5(d1);
					List<M_SEC_Detail2_Entity> T2Master = getdatabydateList6(d1);
					List<M_SEC_Detail3_Entity> T3Master = getdatabydateList7(d1);
					List<M_SEC_Detail4_Entity> T4Master = getdatabydateList8(d1);

					System.out.println("Normal Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary1", T1Master);
					mv.addObject("reportsummary2", T2Master);
					mv.addObject("reportsummary3", T3Master);
					mv.addObject("reportsummary4", T4Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SEC");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
	public void updateReport(BRRS_M_SEC_Summary_Entity1 updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 1️⃣ Fetch existing SUMMARY using JDBC
		Optional<BRRS_M_SEC_Summary_Entity1> existingSummaryOpt = findTopByreport_dateOrderByreport_versionDesc1(
				updatedEntity.getReport_date());

		if (!existingSummaryOpt.isPresent()) {
			throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		BRRS_M_SEC_Summary_Entity1 existingSummary = existingSummaryOpt.get();

		// 2️⃣ Audit old copy
		BRRS_M_SEC_Summary_Entity1 oldcopy = new BRRS_M_SEC_Summary_Entity1();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		// 3️⃣ Fetch or create DETAIL
		List<M_SEC_Detail1_Entity> detailList = getdatabydateList5(updatedEntity.getReport_date());
		M_SEC_Detail1_Entity existingDetail;
		if (detailList.isEmpty()) {
			existingDetail = new M_SEC_Detail1_Entity();
			existingDetail.setReport_date(updatedEntity.getReport_date());
		} else {
			existingDetail = detailList.get(0);
		}

		try {
			// Loop R11 → R19
			for (int i = 11; i <= 19; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "TCA" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity1.class.getMethod(getterName);
						Method summarySetter = BRRS_M_SEC_Summary_Entity1.class.getMethod(setterName,
								getter.getReturnType());
						Method detailSetter = M_SEC_Detail1_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						summarySetter.invoke(existingSummary, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 4️⃣ Check Changes
		String changes = auditService.getChanges(oldcopy, existingSummary);

		// 5️⃣ Save Summary & Detail (JDBC Update)
		saveOrUpdateSecSummary1(existingSummary);
		saveOrUpdateSecDetail1(existingDetail);

		// 6️⃣ Audit Only If Changes Found
		if (!changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M SEC Summary Screen", "BRRS_M_SEC_SUMMARYTABLE1");
		}

		System.out.println("M_SEC Summary1 & Detail1 Update Completed");
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC SUMMARY1
//============================

	private void saveOrUpdateSecSummary1(BRRS_M_SEC_Summary_Entity1 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_SUMMARYTABLE1 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Summary1 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_SUMMARYTABLE1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Summary1 inserted for date: " + entity.getReport_date());
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC DETAIL1
//============================

	private void saveOrUpdateSecDetail1(M_SEC_Detail1_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_DETAILTABLE1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_DETAILTABLE1 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Detail1 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_DETAILTABLE1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Detail1 inserted for date: " + entity.getReport_date());
		}
	}

	@Transactional
	public void updateReport1(BRRS_M_SEC_Summary_Entity2 updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 1️⃣ Fetch existing SUMMARY using JDBC
		Optional<BRRS_M_SEC_Summary_Entity2> existingSummaryOpt = findTopByreport_dateOrderByreport_versionDesc2(
				updatedEntity.getReport_date());

		if (!existingSummaryOpt.isPresent()) {
			throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		BRRS_M_SEC_Summary_Entity2 existingSummary = existingSummaryOpt.get();

		// 2️⃣ Audit old copy
		BRRS_M_SEC_Summary_Entity2 oldcopy = new BRRS_M_SEC_Summary_Entity2();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		// 3️⃣ Fetch or create DETAIL using JDBC
		List<M_SEC_Detail2_Entity> detailList = getdatabydateList6(updatedEntity.getReport_date());
		M_SEC_Detail2_Entity existingDetail;
		if (detailList.isEmpty()) {
			existingDetail = new M_SEC_Detail2_Entity();
			existingDetail.setReport_date(updatedEntity.getReport_date());
		} else {
			existingDetail = detailList.get(0);
		}

		try {
			// Loop R11 → R16
			for (int i = 11; i <= 16; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "TCA2" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity2.class.getMethod(getterName);
						Method summarySetter = BRRS_M_SEC_Summary_Entity2.class.getMethod(setterName,
								getter.getReturnType());
						Method detailSetter = M_SEC_Detail2_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						summarySetter.invoke(existingSummary, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 4️⃣ Check Changes
		String changes = auditService.getChanges(oldcopy, existingSummary);

		// 5️⃣ Save Summary & Detail (JDBC Update)
		saveOrUpdateSecSummary2(existingSummary);
		saveOrUpdateSecDetail2(existingDetail);

		// 6️⃣ Audit Only If Changes Found
		if (!changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M SEC Summary Screen", "BRRS_M_SEC_SUMMARYTABLE2");
		}

		System.out.println("M_SEC Summary2 & Detail2 Update Completed");
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC SUMMARY2
//============================

	private void saveOrUpdateSecSummary2(BRRS_M_SEC_Summary_Entity2 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE2 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_SUMMARYTABLE2 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Summary2 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_SUMMARYTABLE2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Summary2 inserted for date: " + entity.getReport_date());
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC DETAIL2
//============================

	private void saveOrUpdateSecDetail2(M_SEC_Detail2_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_DETAILTABLE2 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_DETAILTABLE2 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Detail2 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_DETAILTABLE2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Detail2 inserted for date: " + entity.getReport_date());
		}
	}

	@Transactional
	public void updateReport2(BRRS_M_SEC_Summary_Entity3 updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 1️⃣ Fetch existing SUMMARY using JDBC
		Optional<BRRS_M_SEC_Summary_Entity3> existingSummaryOpt = findTopByreport_dateOrderByreport_versionDesc3(
				updatedEntity.getReport_date());

		if (!existingSummaryOpt.isPresent()) {
			throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		BRRS_M_SEC_Summary_Entity3 existingSummary = existingSummaryOpt.get();

		// 2️⃣ Audit old copy
		BRRS_M_SEC_Summary_Entity3 oldcopy = new BRRS_M_SEC_Summary_Entity3();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		// 3️⃣ Fetch or create DETAIL using JDBC
		List<M_SEC_Detail3_Entity> detailList = getdatabydateList7(updatedEntity.getReport_date());
		M_SEC_Detail3_Entity existingDetail;
		if (detailList.isEmpty()) {
			existingDetail = new M_SEC_Detail3_Entity();
			existingDetail.setReport_date(updatedEntity.getReport_date());
		} else {
			existingDetail = detailList.get(0);
		}

		try {
			// Loop R26 → R31
			for (int i = 26; i <= 31; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL", "O5Y_FT",
						"O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity3.class.getMethod(getterName);
						Method summarySetter = BRRS_M_SEC_Summary_Entity3.class.getMethod(setterName,
								getter.getReturnType());
						Method detailSetter = M_SEC_Detail3_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						summarySetter.invoke(existingSummary, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 4️⃣ Check Changes
		String changes = auditService.getChanges(oldcopy, existingSummary);

		// 5️⃣ Save Summary & Detail (JDBC Update)
		saveOrUpdateSecSummary3(existingSummary);
		saveOrUpdateSecDetail3(existingDetail);

		// 6️⃣ Audit Only If Changes Found
		if (!changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M SEC Summary Screen", "BRRS_M_SEC_SUMMARYTABLE3");
		}

		System.out.println("M_SEC Summary3 & Detail3 Update Completed");
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC SUMMARY3
//============================

	private void saveOrUpdateSecSummary3(BRRS_M_SEC_Summary_Entity3 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE3 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_SUMMARYTABLE3 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Summary3 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_SUMMARYTABLE3 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Summary3 inserted for date: " + entity.getReport_date());
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC DETAIL3
//============================

	private void saveOrUpdateSecDetail3(M_SEC_Detail3_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_DETAILTABLE3 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_DETAILTABLE3 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Detail3 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_DETAILTABLE3 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Detail3 inserted for date: " + entity.getReport_date());
		}
	}

	@Transactional
	public void updateReport3(BRRS_M_SEC_Summary_Entity4 updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// 1️⃣ Fetch existing SUMMARY using JDBC
		Optional<BRRS_M_SEC_Summary_Entity4> existingSummaryOpt = findTopByreport_dateOrderByreport_versionDesc4(
				updatedEntity.getReport_date());

		if (!existingSummaryOpt.isPresent()) {
			throw new RuntimeException("Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		BRRS_M_SEC_Summary_Entity4 existingSummary = existingSummaryOpt.get();

		// 2️⃣ Audit old copy
		BRRS_M_SEC_Summary_Entity4 oldcopy = new BRRS_M_SEC_Summary_Entity4();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		// 3️⃣ Fetch or create DETAIL using JDBC
		List<M_SEC_Detail4_Entity> detailList = getdatabydateList8(updatedEntity.getReport_date());
		M_SEC_Detail4_Entity existingDetail;
		if (detailList.isEmpty()) {
			existingDetail = new M_SEC_Detail4_Entity();
			existingDetail.setReport_date(updatedEntity.getReport_date());
		} else {
			existingDetail = detailList.get(0);
		}

		try {
			// Loop R36 → R43
			for (int i = 36; i <= 43; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL", "O5Y_FT",
						"O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity4.class.getMethod(getterName);
						Method summarySetter = BRRS_M_SEC_Summary_Entity4.class.getMethod(setterName,
								getter.getReturnType());
						Method detailSetter = M_SEC_Detail4_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						summarySetter.invoke(existingSummary, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 4️⃣ Check Changes
		String changes = auditService.getChanges(oldcopy, existingSummary);

		// 5️⃣ Save Summary & Detail (JDBC Update)
		saveOrUpdateSecSummary4(existingSummary);
		saveOrUpdateSecDetail4(existingDetail);

		// 6️⃣ Audit Only If Changes Found
		if (!changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M SEC Summary Screen", "BRRS_M_SEC_SUMMARYTABLE4");
		}

		System.out.println("M_SEC Summary4 & Detail4 Update Completed");
	}

	// ============================
	// SAVE/UPDATE METHODS FOR M_SEC SUMMARY4
	// ============================

	private void saveOrUpdateSecSummary4(BRRS_M_SEC_Summary_Entity4 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_SUMMARYTABLE4 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_SUMMARYTABLE4 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Summary4 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_SUMMARYTABLE4 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Summary4 inserted for date: " + entity.getReport_date());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR M_SEC DETAIL4
	// ============================

	private void saveOrUpdateSecDetail4(M_SEC_Detail4_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_DETAILTABLE4 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_DETAILTABLE4 SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Detail4 updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_DETAILTABLE4 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Detail4 inserted for date: " + entity.getReport_date());
		}
	}

	public void updateResubReport(M_SEC_RESUB_Summary_Entity1 updatedEntity1,
			M_SEC_RESUB_Summary_Entity2 updatedEntity2, M_SEC_RESUB_Summary_Entity3 updatedEntity3,
			M_SEC_RESUB_Summary_Entity4 updatedEntity4) {

		Date report_date1 = updatedEntity1.getReport_date();
		Date report_date2 = updatedEntity2.getReport_date();
		Date report_date3 = updatedEntity3.getReport_date();
		Date report_date4 = updatedEntity4.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE (JDBC)
		// ----------------------------------------------------

		BigDecimal maxResubVer = findMaxVersion17(report_date1);
		BigDecimal maxResubVer2 = findMaxVersion18(report_date2);
		BigDecimal maxResubVer3 = findMaxVersion19(report_date3);
		BigDecimal maxResubVer4 = findMaxVersion20(report_date4);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + report_date1);
		if (maxResubVer2 == null)
			throw new RuntimeException("No record for: " + report_date2);
		if (maxResubVer3 == null)
			throw new RuntimeException("No record for: " + report_date3);
		if (maxResubVer4 == null)
			throw new RuntimeException("No record for: " + report_date4);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SEC_RESUB_Summary_Entity1 resubSummary = new M_SEC_RESUB_Summary_Entity1();
		M_SEC_RESUB_Summary_Entity2 resubSummary2 = new M_SEC_RESUB_Summary_Entity2();
		M_SEC_RESUB_Summary_Entity3 resubSummary3 = new M_SEC_RESUB_Summary_Entity3();
		M_SEC_RESUB_Summary_Entity4 resubSummary4 = new M_SEC_RESUB_Summary_Entity4();

		BeanUtils.copyProperties(updatedEntity1, resubSummary, "report_date", "report_version", "reportResubDate");

		resubSummary.setReport_date(report_date1);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, resubSummary2, "report_date", "report_version", "reportResubDate");

		resubSummary2.setReport_date(report_date2);
		resubSummary2.setReport_version(newVersion);
		resubSummary2.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity3, resubSummary3, "report_date", "report_version", "reportResubDate");

		resubSummary3.setReport_date(report_date3);
		resubSummary3.setReport_version(newVersion);
		resubSummary3.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity4, resubSummary4, "report_date", "report_version", "reportResubDate");

		resubSummary4.setReport_date(report_date4);
		resubSummary4.setReport_version(newVersion);
		resubSummary4.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SEC_RESUB_Detail_Entity1 resubDetail = new M_SEC_RESUB_Detail_Entity1();
		M_SEC_RESUB_Detail_Entity2 resubDetail2 = new M_SEC_RESUB_Detail_Entity2();
		M_SEC_RESUB_Detail_Entity3 resubDetail3 = new M_SEC_RESUB_Detail_Entity3();
		M_SEC_RESUB_Detail_Entity4 resubDetail4 = new M_SEC_RESUB_Detail_Entity4();

		BeanUtils.copyProperties(updatedEntity1, resubDetail, "report_date", "report_version", "reportResubDate");

		resubDetail.setReport_date(report_date1);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, resubDetail2, "report_date", "report_version", "reportResubDate");

		resubDetail2.setReport_date(report_date2);
		resubDetail2.setReport_version(newVersion);
		resubDetail2.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity3, resubDetail3, "report_date", "report_version", "reportResubDate");

		resubDetail3.setReport_date(report_date3);
		resubDetail3.setReport_version(newVersion);
		resubDetail3.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity4, resubDetail4, "report_date", "report_version", "reportResubDate");

		resubDetail4.setReport_date(report_date4);
		resubDetail4.setReport_version(newVersion);
		resubDetail4.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		BRRS_M_SEC_Archival_Summary_Entity1 archSummary = new BRRS_M_SEC_Archival_Summary_Entity1();
		BRRS_M_SEC_Archival_Summary_Entity2 archSummary2 = new BRRS_M_SEC_Archival_Summary_Entity2();
		BRRS_M_SEC_Archival_Summary_Entity3 archSummary3 = new BRRS_M_SEC_Archival_Summary_Entity3();
		BRRS_M_SEC_Archival_Summary_Entity4 archSummary4 = new BRRS_M_SEC_Archival_Summary_Entity4();

		BeanUtils.copyProperties(updatedEntity1, archSummary, "report_date", "report_version", "reportResubDate");

		archSummary.setReport_date(report_date1);
		archSummary.setReport_version(newVersion);
		archSummary.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, archSummary2, "report_date", "report_version", "reportResubDate");

		archSummary2.setReport_date(report_date2);
		archSummary2.setReport_version(newVersion);
		archSummary2.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity3, archSummary3, "report_date", "report_version", "reportResubDate");

		archSummary3.setReport_date(report_date3);
		archSummary3.setReport_version(newVersion);
		archSummary3.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity4, archSummary4, "report_date", "report_version", "reportResubDate");

		archSummary4.setReport_date(report_date4);
		archSummary4.setReport_version(newVersion);
		archSummary4.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SEC_Archival_Detail1_Entity archDetail = new M_SEC_Archival_Detail1_Entity();
		M_SEC_Archival_Detail2_Entity archDetail2 = new M_SEC_Archival_Detail2_Entity();
		M_SEC_Archival_Detail3_Entity archDetail3 = new M_SEC_Archival_Detail3_Entity();
		M_SEC_Archival_Detail4_Entity archDetail4 = new M_SEC_Archival_Detail4_Entity();

		BeanUtils.copyProperties(updatedEntity1, archDetail, "report_date", "report_version", "reportResubDate");

		archDetail.setReport_date(report_date1);
		archDetail.setReport_version(newVersion);
		archDetail.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, archDetail2, "report_date", "report_version", "reportResubDate");

		archDetail2.setReport_date(report_date2);
		archDetail2.setReport_version(newVersion);
		archDetail2.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity3, archDetail3, "report_date", "report_version", "reportResubDate");

		archDetail3.setReport_date(report_date3);
		archDetail3.setReport_version(newVersion);
		archDetail3.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity4, archDetail4, "report_date", "report_version", "reportResubDate");

		archDetail4.setReport_date(report_date4);
		archDetail4.setReport_version(newVersion);
		archDetail4.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL USING JDBC
		// ====================================================

		// Save Resub Summary
		saveOrUpdateResubSummary1(resubSummary);
		saveOrUpdateResubSummary2(resubSummary2);
		saveOrUpdateResubSummary3(resubSummary3);
		saveOrUpdateResubSummary4(resubSummary4);

		// Save Resub Detail
		saveOrUpdateResubDetail1(resubDetail);
		saveOrUpdateResubDetail2(resubDetail2);
		saveOrUpdateResubDetail3(resubDetail3);
		saveOrUpdateResubDetail4(resubDetail4);

		// Save Archival Summary
		saveOrUpdateArchivalSummary1(archSummary);
		saveOrUpdateArchivalSummary2(archSummary2);
		saveOrUpdateArchivalSummary3(archSummary3);
		saveOrUpdateArchivalSummary4(archSummary4);

		// Save Archival Detail
		saveOrUpdateArchivalDetail1(archDetail);
		saveOrUpdateArchivalDetail2(archDetail2);
		saveOrUpdateArchivalDetail3(archDetail3);
		saveOrUpdateArchivalDetail4(archDetail4);

		// ====================================================
		// AUDIT
		// ====================================================

		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			String userid = (String) request.getSession().getAttribute("USERID");

			auditService.createBusinessAudit(userid, "RESUBMIT", "M SEC Resub Summary", null,
					"BRRS_M_SEC_RESUB_SUMMARYTABLE");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB SUMMARY1
//============================

	private void saveOrUpdateResubSummary1(M_SEC_RESUB_Summary_Entity1 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_SUMMARYTABLE1 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Summary1 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_SUMMARYTABLE1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Summary1 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB SUMMARY2
//============================

	private void saveOrUpdateResubSummary2(M_SEC_RESUB_Summary_Entity2 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_SUMMARYTABLE2 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Summary2 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_SUMMARYTABLE2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Summary2 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB SUMMARY3
//============================

	private void saveOrUpdateResubSummary3(M_SEC_RESUB_Summary_Entity3 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE3 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_SUMMARYTABLE3 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Summary3 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_SUMMARYTABLE3 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Summary3 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB SUMMARY4
//============================

	private void saveOrUpdateResubSummary4(M_SEC_RESUB_Summary_Entity4 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_SUMMARYTABLE4 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_SUMMARYTABLE4 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Summary4 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_SUMMARYTABLE4 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Summary4 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB DETAIL1
//============================

	private void saveOrUpdateResubDetail1(M_SEC_RESUB_Detail_Entity1 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_DETAILTABLE1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_DETAILTABLE1 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Detail1 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_DETAILTABLE1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Detail1 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB DETAIL2
//============================

	private void saveOrUpdateResubDetail2(M_SEC_RESUB_Detail_Entity2 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_DETAILTABLE2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_DETAILTABLE2 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Detail2 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_DETAILTABLE2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Detail2 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB DETAIL3
//============================

	private void saveOrUpdateResubDetail3(M_SEC_RESUB_Detail_Entity3 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_DETAILTABLE3 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_DETAILTABLE3 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Detail3 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_DETAILTABLE3 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Detail3 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR M_SEC RESUB DETAIL4
//============================

	private void saveOrUpdateResubDetail4(M_SEC_RESUB_Detail_Entity4 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_RESUB_DETAILTABLE4 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_RESUB_DETAILTABLE4 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Detail4 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_RESUB_DETAILTABLE4 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Detail4 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL SUMMARY1
//============================

	private void saveOrUpdateArchivalSummary1(BRRS_M_SEC_Archival_Summary_Entity1 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Summary1 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_SUMMARY1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Summary1 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL SUMMARY2
//============================

	private void saveOrUpdateArchivalSummary2(BRRS_M_SEC_Archival_Summary_Entity2 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Summary2 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Summary2 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL SUMMARY3
//============================

	private void saveOrUpdateArchivalSummary3(BRRS_M_SEC_Archival_Summary_Entity3 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Summary3 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_SUMMARY3 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Summary3 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL SUMMARY4
//============================

	private void saveOrUpdateArchivalSummary4(BRRS_M_SEC_Archival_Summary_Entity4 entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Summary4 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_SUMMARY4 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Summary4 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL DETAIL1
//============================

	private void saveOrUpdateArchivalDetail1(M_SEC_Archival_Detail1_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Detail1 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_DETAIL1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Detail1 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL DETAIL2
//============================

	private void saveOrUpdateArchivalDetail2(M_SEC_Archival_Detail2_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Detail2 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_DETAIL2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Detail2 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL DETAIL3
//============================

	private void saveOrUpdateArchivalDetail3(M_SEC_Archival_Detail3_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Detail3 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_DETAIL3 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Detail3 inserted");
		}
	}

//============================
//SAVE/UPDATE METHODS FOR ARCHIVAL DETAIL4
//============================

	private void saveOrUpdateArchivalDetail4(M_SEC_Archival_Detail4_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Detail4 updated");
		} else {
			String sql = "INSERT INTO BRRS_M_SEC_ARCHIVALTABLE_DETAIL4 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Detail4 inserted");
		}
	}

//Archival View - JDBC Version
	public List<Object[]> getM_SECArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			// ✅ Using JDBC method instead of JPA Repository
			List<BRRS_M_SEC_Archival_Summary_Entity1> repoData = getdatabydateListWithVersion1();

			if (repoData != null && !repoData.isEmpty()) {
				for (BRRS_M_SEC_Archival_Summary_Entity1 entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				BRRS_M_SEC_Archival_Summary_Entity1 first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SEC Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public List<Object[]> getM_SECResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			// ✅ Using JDBC method instead of JPA Repository
			List<BRRS_M_SEC_Archival_Summary_Entity1> latestArchivalList = getdatabydateListWithVersion1();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (BRRS_M_SEC_Archival_Summary_Entity1 entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SEC Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Normal format Excel

	public byte[] getM_SECExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_SECARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SECResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SECEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				// Fetch data
				Date reportDate = dateformat.parse(todate);
				List<BRRS_M_SEC_Summary_Entity1> dataList1 = getdatabydateList1(reportDate);
				List<BRRS_M_SEC_Summary_Entity2> dataList2 = getdatabydateList2(reportDate);
				List<BRRS_M_SEC_Summary_Entity3> dataList3 = getdatabydateList3(reportDate);
				List<BRRS_M_SEC_Summary_Entity4> dataList4 = getdatabydateList4(reportDate);

				// If no data, try to get latest available date
				if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
					Date latestDate = getLatestReportDate();
					if (latestDate != null) {
						dataList1 = getdatabydateList1(latestDate);
						dataList2 = getdatabydateList2(latestDate);
						dataList3 = getdatabydateList3(latestDate);
						dataList4 = getdatabydateList4(latestDate);
						logger.info("No data for requested date {}. Using latest available date: {}", todate,
								latestDate);
					}
				}

				if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SEC report. Returning empty result.");
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

					int startRow = 6;

					if (!dataList1.isEmpty()) {
						for (int i = 0; i < dataList1.size(); i++) {
							BRRS_M_SEC_Summary_Entity1 record = dataList1.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row7
							// Column B
							Cell cellBdate = row.createCell(1);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}

							// ROW 11
							row = sheet.getRow(10);

							// row11
							Cell cellB = row.createCell(1);
							if (record.getR11_TCA() != null) {
								cellB.setCellValue(record.getR11_TCA().longValue()); // ← whole number
								cellB.setCellStyle(numberStyle); // ← format changed below
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							cellB = row.createCell(1);
							if (record.getR12_TCA() != null) {
								cellB.setCellValue(record.getR12_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							cellB = row.createCell(1);
							if (record.getR13_TCA() != null) {
								cellB.setCellValue(record.getR13_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							cellB = row.createCell(1);
							if (record.getR14_TCA() != null) {
								cellB.setCellValue(record.getR14_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);
							cellB = row.createCell(1);
							if (record.getR15_TCA() != null) {
								cellB.setCellValue(record.getR15_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);
							cellB = row.createCell(1);
							if (record.getR16_TCA() != null) {
								cellB.setCellValue(record.getR16_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);
							cellB = row.createCell(1);
							if (record.getR17_TCA() != null) {
								cellB.setCellValue(record.getR17_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);
							cellB = row.createCell(1);
							if (record.getR18_TCA() != null) {
								cellB.setCellValue(record.getR18_TCA().longValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}
						}
					}

					startRow = 10;

					if (!dataList2.isEmpty()) {
						for (int i = 0; i < dataList2.size(); i++) {

							BRRS_M_SEC_Summary_Entity2 record = dataList2.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							// row11
							// Column B2 - Original Amount
							Cell cellB = row.createCell(6);
							if (record.getR11_TCA2() != null) {
								cellB.setCellValue(record.getR11_TCA2().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							row = sheet.getRow(11);

							cellB = row.createCell(6);
							if (record.getR12_TCA2() != null) {
								cellB.setCellValue(record.getR12_TCA2().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);

							cellB = row.createCell(6);
							if (record.getR13_TCA2() != null) {
								cellB.setCellValue(record.getR13_TCA2().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);

							cellB = row.createCell(6);
							if (record.getR14_TCA2() != null) {
								cellB.setCellValue(record.getR14_TCA2().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							row = sheet.getRow(14);

							cellB = row.createCell(6);
							if (record.getR15_TCA2() != null) {
								cellB.setCellValue(record.getR15_TCA2().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}
						}
					}

					startRow = 25;

					if (!dataList3.isEmpty()) {
						for (int i = 0; i < dataList3.size(); i++) {

							BRRS_M_SEC_Summary_Entity3 record = dataList3.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							Cell cellB = row.createCell(1);
							if (record.getR26_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							Cell cellC = row.createCell(2);
							if (record.getR26_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							Cell cellE = row.createCell(4);
							if (record.getR26_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							Cell cellf = row.createCell(5);
							if (record.getR26_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							Cell cellH = row.createCell(7);
							if (record.getR26_O5Y_FT() != null) {
								cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							Cell celli = row.createCell(8);
							if (record.getR26_O5Y_HTM() != null) {
								celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}
							// row27

							row = sheet.getRow(26);

							cellB = row.createCell(1);
							if (record.getR27_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR27_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR27_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR27_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR27_O5Y_FT() != null) {
								cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR27_O5Y_HTM() != null) {
								celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row28

							row = sheet.getRow(27);

							cellB = row.createCell(1);
							if (record.getR28_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR28_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR28_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR28_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR28_O5Y_FT() != null) {
								cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR28_O5Y_HTM() != null) {
								celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row29

							row = sheet.getRow(28);

							cellB = row.createCell(1);
							if (record.getR29_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR29_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR29_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR29_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR29_O5Y_FT() != null) {
								cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR29_O5Y_HTM() != null) {
								celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row30

							row = sheet.getRow(29);

							cellB = row.createCell(1);
							if (record.getR30_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR30_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR30_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR30_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR30_O5Y_FT() != null) {
								cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR30_O5Y_HTM() != null) {
								celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}
						}
					}

					startRow = 35;

					if (!dataList4.isEmpty()) {
						for (int i = 0; i < dataList4.size(); i++) {

							BRRS_M_SEC_Summary_Entity4 record = dataList4.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							Cell cellB = row.createCell(1);
							if (record.getR36_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							Cell cellC = row.createCell(2);
							if (record.getR36_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							Cell cellE = row.createCell(4);
							if (record.getR36_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							Cell cellf = row.createCell(5);
							if (record.getR36_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							Cell cellH = row.createCell(7);
							if (record.getR36_O5Y_FT() != null) {
								cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							Cell celli = row.createCell(8);
							if (record.getR36_O5Y_HTM() != null) {
								celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}
							// row37

							row = sheet.getRow(36);

							cellB = row.createCell(1);
							if (record.getR37_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR37_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR37_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR37_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR37_O5Y_FT() != null) {
								cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR37_O5Y_HTM() != null) {
								celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row38

							row = sheet.getRow(37);

							cellB = row.createCell(1);
							if (record.getR38_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR38_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR38_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR38_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR38_O5Y_FT() != null) {
								cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR38_O5Y_HTM() != null) {
								celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row39

							row = sheet.getRow(38);

							cellB = row.createCell(1);
							if (record.getR39_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR39_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR39_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR39_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR39_O5Y_FT() != null) {
								cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR39_O5Y_HTM() != null) {
								celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row40
							row = sheet.getRow(39);

							cellB = row.createCell(1);
							if (record.getR40_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR40_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR40_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR40_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR40_O5Y_FT() != null) {
								cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR40_O5Y_HTM() != null) {
								celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row41
							row = sheet.getRow(40);

							cellB = row.createCell(1);
							if (record.getR41_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR41_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							cellE = row.createCell(4);
							if (record.getR41_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR41_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR41_O5Y_FT() != null) {
								cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR41_O5Y_HTM() != null) {
								celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

							// row42
							row = sheet.getRow(41);

							cellB = row.createCell(1);
							if (record.getR42_0_1Y_FT() != null) {
								cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							cellC = row.createCell(2);
							if (record.getR42_0_1Y_HTM() != null) {
								cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// row11
							// Column D4 - No. of Accounts
							cellE = row.createCell(4);
							if (record.getR42_1_5Y_FT() != null) {
								cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							cellf = row.createCell(5);
							if (record.getR42_1_5Y_HTM() != null) {
								cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
								cellf.setCellStyle(numberStyle);
							} else {
								cellf.setCellValue("");
								cellf.setCellStyle(textStyle);
							}

							cellH = row.createCell(7);
							if (record.getR42_O5Y_FT() != null) {
								cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
								cellH.setCellStyle(numberStyle);
							} else {
								cellH.setCellValue("");
								cellH.setCellStyle(textStyle);
							}

							celli = row.createCell(8);
							if (record.getR42_O5Y_HTM() != null) {
								celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
								celli.setCellStyle(numberStyle);
							} else {
								celli.setCellValue("");
								celli.setCellStyle(textStyle);
							}

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SEC SUMMARY", null,
								"BRRS_M_SEC_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_SECEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SECARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SECEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			Date reportDate = dateformat.parse(todate);
			List<BRRS_M_SEC_Summary_Entity1> dataList1 = getdatabydateList1(reportDate);
			List<BRRS_M_SEC_Summary_Entity2> dataList2 = getdatabydateList2(reportDate);
			List<BRRS_M_SEC_Summary_Entity3> dataList3 = getdatabydateList3(reportDate);
			List<BRRS_M_SEC_Summary_Entity4> dataList4 = getdatabydateList4(reportDate);

			// If no data, try to get latest available date
			if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
				Date latestDate = getLatestReportDate();
				if (latestDate != null) {
					dataList1 = getdatabydateList1(latestDate);
					dataList2 = getdatabydateList2(latestDate);
					dataList3 = getdatabydateList3(latestDate);
					dataList4 = getdatabydateList4(latestDate);
					logger.info("No data for requested date {}. Using latest available date: {}", todate, latestDate);
				}
			}

			if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SEC report. Returning empty result.");
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

				int startRow = 6;

				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						BRRS_M_SEC_Summary_Entity1 record = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row7
						// Column B
						Cell cellBdate = row.createCell(5);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}

						// ROW 10
						row = sheet.getRow(9);

						// row10
						// Column B2 - Original Amount
						Cell cellB = row.createCell(2);
						if (record.getR11_TCA() != null) {
							cellB.setCellValue(record.getR11_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);

						cellB = row.createCell(2);
						if (record.getR12_TCA() != null) {
							cellB.setCellValue(record.getR12_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						cellB = row.createCell(2);
						if (record.getR13_TCA() != null) {
							cellB.setCellValue(record.getR13_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);
						cellB = row.createCell(2);
						if (record.getR14_TCA() != null) {
							cellB.setCellValue(record.getR14_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);
						cellB = row.createCell(2);
						if (record.getR15_TCA() != null) {
							cellB.setCellValue(record.getR15_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row15
						row = sheet.getRow(14);
						cellB = row.createCell(2);
						if (record.getR16_TCA() != null) {
							cellB.setCellValue(record.getR16_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);
						cellB = row.createCell(2);
						if (record.getR17_TCA() != null) {
							cellB.setCellValue(record.getR17_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);
						cellB = row.createCell(2);
						if (record.getR18_TCA() != null) {
							cellB.setCellValue(record.getR18_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);
						cellB = row.createCell(2);
						if (record.getR19_TCA() != null) {
							cellB.setCellValue(record.getR19_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
					}
				}

				startRow = 9;

				if (!dataList2.isEmpty()) {
					for (int i = 0; i < dataList2.size(); i++) {
						BRRS_M_SEC_Summary_Entity2 record = dataList2.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						// row10
						// Column B2 - Original Amount
						Cell cellB = row.createCell(7);
						if (record.getR11_TCA2() != null) {
							cellB.setCellValue(record.getR11_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						cellB = row.createCell(7);
						if (record.getR12_TCA2() != null) {
							cellB.setCellValue(record.getR12_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(11);

						cellB = row.createCell(7);
						if (record.getR13_TCA2() != null) {
							cellB.setCellValue(record.getR13_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);

						cellB = row.createCell(7);
						if (record.getR14_TCA2() != null) {
							cellB.setCellValue(record.getR14_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);

						cellB = row.createCell(7);
						if (record.getR15_TCA2() != null) {
							cellB.setCellValue(record.getR15_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(14);

						cellB = row.createCell(7);
						if (record.getR16_TCA2() != null) {
							cellB.setCellValue(record.getR16_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
					}
				}

				startRow = 24;

				if (!dataList3.isEmpty()) {
					for (int i = 0; i < dataList3.size(); i++) {
						BRRS_M_SEC_Summary_Entity3 record = dataList3.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell cellB = row.createCell(2);
						if (record.getR26_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						Cell cellC = row.createCell(3);
						if (record.getR26_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D IN EXCEL E

						Cell cellD = row.createCell(4);
						if (record.getR26_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR26_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// row11
						// Column E - No. of Accounts
						Cell cellE = row.createCell(5);
						if (record.getR26_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}
						Cell cellf = row.createCell(6);
						if (record.getR26_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G IN EXCEL H

						Cell cellG = row.createCell(7);
						if (record.getR26_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR26_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

//		Cell cellH = row.createCell(8);
//		if (record.getR26_O5Y_FT() != null) {
//			cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
//			cellH.setCellStyle(numberStyle);
//		} else {
//			cellH.setCellValue("");
//			cellH.setCellStyle(textStyle);
//		}
						//
//		Cell celli = row.createCell(9);
//		if (record.getR26_O5Y_HTM() != null) {
//			celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
//			celli.setCellStyle(numberStyle);
//		} else {
//			celli.setCellValue("");
//			celli.setCellStyle(textStyle);
//		}
						//
//		//J 
						//
//		Cell cellJ = row.createCell(10);
//		if (record.getR26_O5Y_TOTAL() != null) {
//			cellJ.setCellValue(record.getR26_O5Y_TOTAL().doubleValue());
//			cellJ.setCellStyle(numberStyle);
//		} else {
//			cellJ.setCellValue("");
//			cellJ.setCellStyle(textStyle);
//		}

						// K

						Cell cellK = row.createCell(8);
						if (record.getR26_T_FT() != null) {
							cellK.setCellValue(record.getR26_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L

						Cell cellL = row.createCell(9);
						if (record.getR26_T_HTM() != null) {
							cellL.setCellValue(record.getR26_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M

						Cell cellM = row.createCell(10);
						if (record.getR26_T_TOTAL() != null) {
							cellM.setCellValue(record.getR26_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R27 OLD =====================

						// row26

						row = sheet.getRow(25);

						cellB = row.createCell(2);
						if (record.getR27_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR27_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR27_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR27_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR27_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR27_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR27_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR27_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR27_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR27_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR27_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR27_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR27_T_FT() != null) {
							cellK.setCellValue(record.getR27_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR27_T_HTM() != null) {
							cellL.setCellValue(record.getR27_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR27_T_TOTAL() != null) {
							cellM.setCellValue(record.getR27_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R28 =====================
						// row27
						row = sheet.getRow(26);

						cellB = row.createCell(2);
						if (record.getR28_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR28_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR28_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR28_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR28_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR28_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR28_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR28_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}
						//
						// cellH = row.createCell(8);
						// if (record.getR28_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR28_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR28_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR28_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR28_T_FT() != null) {
							cellK.setCellValue(record.getR28_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR28_T_HTM() != null) {
							cellL.setCellValue(record.getR28_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR28_T_TOTAL() != null) {
							cellM.setCellValue(record.getR28_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R29 =====================

						// row28
						row = sheet.getRow(27);

						cellB = row.createCell(2);
						if (record.getR29_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR29_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR29_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR29_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR29_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR29_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR29_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR29_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR29_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR29_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR29_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR29_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR29_T_FT() != null) {
							cellK.setCellValue(record.getR29_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR29_T_HTM() != null) {
							cellL.setCellValue(record.getR29_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR29_T_TOTAL() != null) {
							cellM.setCellValue(record.getR29_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R30 =====================

						// row29
						row = sheet.getRow(28);

						cellB = row.createCell(2);
						if (record.getR30_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR30_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR30_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR30_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR30_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR30_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR30_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR30_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR30_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR30_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR30_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR30_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR30_T_FT() != null) {
							cellK.setCellValue(record.getR30_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR30_T_HTM() != null) {
							cellL.setCellValue(record.getR30_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR30_T_TOTAL() != null) {
							cellM.setCellValue(record.getR30_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R31 =====================

						// row30
						row = sheet.getRow(29);

						cellB = row.createCell(2);
						if (record.getR31_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR31_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR31_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR31_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR31_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR31_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR31_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR31_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR31_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR31_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR31_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR31_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR31_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR31_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR31_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR31_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR31_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR31_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR31_T_FT() != null) {
							cellK.setCellValue(record.getR31_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR31_T_HTM() != null) {
							cellL.setCellValue(record.getR31_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR31_T_TOTAL() != null) {
							cellM.setCellValue(record.getR31_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

					}
				}

				startRow = 34;

				if (!dataList4.isEmpty()) {
					for (int i = 0; i < dataList4.size(); i++) {
						BRRS_M_SEC_Summary_Entity4 record = dataList4.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ===================== R36 =====================

						Cell cellB = row.createCell(2);
						if (record.getR36_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						Cell cellC = row.createCell(3);
						if (record.getR36_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						Cell cellD = row.createCell(4);
						if (record.getR36_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR36_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						Cell cellE = row.createCell(5);
						if (record.getR36_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						Cell cellf = row.createCell(6);
						if (record.getR36_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						Cell cellG = row.createCell(7);
						if (record.getR36_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR36_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// Cell cellH = row.createCell(8);
						// if (record.getR36_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// Cell celli = row.createCell(9);
						// if (record.getR36_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// Cell cellJ = row.createCell(10);
						// if (record.getR36_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR36_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						Cell cellK = row.createCell(8);
						if (record.getR36_T_FT() != null) {
							cellK.setCellValue(record.getR36_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						Cell cellL = row.createCell(9);
						if (record.getR36_T_HTM() != null) {
							cellL.setCellValue(record.getR36_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						Cell cellM = row.createCell(10);
						if (record.getR36_T_TOTAL() != null) {
							cellM.setCellValue(record.getR36_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R37 =====================

						// row36

						row = sheet.getRow(35);

						cellB = row.createCell(2);
						if (record.getR37_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR37_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR37_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR37_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR37_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR37_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR37_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR37_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR37_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR37_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR37_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR37_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR37_T_FT() != null) {
							cellK.setCellValue(record.getR37_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR37_T_HTM() != null) {
							cellL.setCellValue(record.getR37_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR37_T_TOTAL() != null) {
							cellM.setCellValue(record.getR37_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R38 =====================

						// row37

						row = sheet.getRow(36);

						cellB = row.createCell(2);
						if (record.getR38_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR38_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR38_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR38_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR38_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR38_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR38_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR38_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR38_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR38_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR38_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR38_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR38_T_FT() != null) {
							cellK.setCellValue(record.getR38_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR38_T_HTM() != null) {
							cellL.setCellValue(record.getR38_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR38_T_TOTAL() != null) {
							cellM.setCellValue(record.getR38_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R39 =====================

						// row38

						row = sheet.getRow(37);

						cellB = row.createCell(2);
						if (record.getR39_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR39_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR39_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR39_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR39_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR39_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR39_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR39_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR39_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);

						// }
						//
						// celli = row.createCell(9);
						// if (record.getR39_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR39_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR39_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR39_T_FT() != null) {
							cellK.setCellValue(record.getR39_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR39_T_HTM() != null) {
							cellL.setCellValue(record.getR39_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR39_T_TOTAL() != null) {
							cellM.setCellValue(record.getR39_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						// ===================== R40 =====================

						// row39
						row = sheet.getRow(38);

						cellB = row.createCell(2);
						if (record.getR40_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR40_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR40_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR40_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR40_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR40_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR40_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR40_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR40_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR40_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR40_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR40_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR40_T_FT() != null) {
							cellK.setCellValue(record.getR40_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR40_T_HTM() != null) {
							cellL.setCellValue(record.getR40_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR40_T_TOTAL() != null) {
							cellM.setCellValue(record.getR40_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R41 =====================
						// row40
						row = sheet.getRow(39);

						cellB = row.createCell(2);
						if (record.getR41_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR41_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						// Total D → Excel E
						cellD = row.createCell(4);
						if (record.getR41_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR41_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR41_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR41_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR41_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR41_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR41_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR41_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR41_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR41_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR41_T_FT() != null) {
							cellK.setCellValue(record.getR41_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// L
						cellL = row.createCell(9);
						if (record.getR41_T_HTM() != null) {
							cellL.setCellValue(record.getR41_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// M
						cellM = row.createCell(10);
						if (record.getR41_T_TOTAL() != null) {
							cellM.setCellValue(record.getR41_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// ===================== R42 =====================
						// row41
						row = sheet.getRow(40);

						cellB = row.createCell(2);
						if (record.getR42_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						cellC = row.createCell(3);
						if (record.getR42_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						//// Total D → Excel E
						// cellD = row.createCell(4);
						// if (record.getR42_0_1Y_TOTAL() != null) {
						// cellD.setCellValue(record.getR42_0_1Y_TOTAL().doubleValue());
						// cellD.setCellStyle(numberStyle);
						// } else {
						// cellD.setCellValue("");
						// cellD.setCellStyle(textStyle);
						// }

						// Column F
						cellE = row.createCell(5);
						if (record.getR42_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR42_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR42_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR42_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// cellH = row.createCell(8);
						// if (record.getR42_O5Y_FT() != null) {
						// cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
						// cellH.setCellStyle(numberStyle);
						// } else {
						// cellH.setCellValue("");
						// cellH.setCellStyle(textStyle);
						// }
						//
						// celli = row.createCell(9);
						// if (record.getR42_O5Y_HTM() != null) {
						// celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
						// celli.setCellStyle(numberStyle);
						// } else {
						// celli.setCellValue("");
						// celli.setCellStyle(textStyle);
						// }
						//
						//// J
						// cellJ = row.createCell(10);
						// if (record.getR42_O5Y_TOTAL() != null) {
						// cellJ.setCellValue(record.getR42_O5Y_TOTAL().doubleValue());
						// cellJ.setCellStyle(numberStyle);
						// } else {
						// cellJ.setCellValue("");
						// cellJ.setCellStyle(textStyle);
						// }

						// K
						cellK = row.createCell(8);
						if (record.getR42_T_FT() != null) {
							cellK.setCellValue(record.getR42_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						//// L
						// cellL = row.createCell(12);
						// if (record.getR42_T_HTM() != null) {
						// cellL.setCellValue(record.getR42_T_HTM().doubleValue());
						// cellL.setCellStyle(numberStyle);
						// } else {
						// cellL.setCellValue("");
						// cellL.setCellStyle(textStyle);
						// }
						//
						//// M
						// cellM = row.createCell(13);
						// if (record.getR42_T_TOTAL() != null) {
						// cellM.setCellValue(record.getR42_T_TOTAL().doubleValue());
						// cellM.setCellStyle(numberStyle);
						// } else {
						// cellM.setCellValue("");
						// cellM.setCellStyle(textStyle);
						// }
						//
//					

						// ===================== R43 =====================

						// row42
						row = sheet.getRow(41);

						cellB = row.createCell(2);
						if (record.getR43_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR43_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// Column F
						cellE = row.createCell(5);
						if (record.getR43_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR43_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						cellf = row.createCell(6);
						if (record.getR43_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR43_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}

						// Total G → Excel H
						cellG = row.createCell(7);
						if (record.getR43_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR43_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// K
						cellK = row.createCell(8);
						if (record.getR43_T_FT() != null) {
							cellK.setCellValue(record.getR43_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SEC EMAIL SUMMARY", null,
							"BRRS_M_SEC_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_SECARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SECARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		// --- LOAD ARCHIVAL DATA ---
		// --- LOAD ARCHIVAL DATA ---
		Date reportDate = dateformat.parse(todate);
		List<BRRS_M_SEC_Archival_Summary_Entity1> dataList1 = getdatabydateListarchival1(reportDate, version);
		List<BRRS_M_SEC_Archival_Summary_Entity2> dataList2 = getdatabydateListarchival2(reportDate, version);
		List<BRRS_M_SEC_Archival_Summary_Entity3> dataList3 = getdatabydateListarchival3(reportDate, version);
		List<BRRS_M_SEC_Archival_Summary_Entity4> dataList4 = getdatabydateListarchival4(reportDate, version);

		// If no data, try to get latest available date and version
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				// Get latest version for this date
				Optional<BRRS_M_SEC_Archival_Summary_Entity1> latestOpt1 = getLatestArchivalSummaryVersionByDate(
						latestDate);
				Optional<BRRS_M_SEC_Archival_Summary_Entity2> latestOpt2 = getLatestArchivalSummaryVersionByDate2(
						latestDate);
				Optional<BRRS_M_SEC_Archival_Summary_Entity3> latestOpt3 = getLatestArchivalSummaryVersionByDate3(
						latestDate);
				Optional<BRRS_M_SEC_Archival_Summary_Entity4> latestOpt4 = getLatestArchivalSummaryVersionByDate4(
						latestDate);

				if (latestOpt1.isPresent() && latestOpt2.isPresent() && latestOpt3.isPresent()
						&& latestOpt4.isPresent()) {
					dataList1 = getdatabydateListarchival1(latestDate, latestOpt1.get().getReport_version());
					dataList2 = getdatabydateListarchival2(latestDate, latestOpt2.get().getReport_version());
					dataList3 = getdatabydateListarchival3(latestDate, latestOpt3.get().getReport_version());
					dataList4 = getdatabydateListarchival4(latestDate, latestOpt4.get().getReport_version());
					logger.info("No data for requested date. Using latest available date: {} with version: {}",
							latestDate, latestOpt1.get().getReport_version());
				}
			}
		}

		// --- LOG IF EMPTY BUT DO NOT CANCEL DOWNLOAD ---
		if (dataList1.isEmpty())
			logger.warn("ARCHIVAL List1 is empty (continuing).");
		if (dataList2.isEmpty())
			logger.warn("ARCHIVAL List2 is empty (continuing).");
		if (dataList3.isEmpty())
			logger.warn("ARCHIVAL List3 is empty (continuing).");
		if (dataList4.isEmpty())
			logger.warn("ARCHIVAL List4 is empty (continuing).");

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

			int startRow = 6;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row7
					// Column B
					Cell cellBdate = row.createCell(1);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

					// ROW 11
					row = sheet.getRow(10);

					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(1);
					if (record.getR11_TCA() != null) {
						cellB.setCellValue(record.getR11_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					cellB = row.createCell(1);
					if (record.getR12_TCA() != null) {
						cellB.setCellValue(record.getR12_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					cellB = row.createCell(1);
					if (record.getR13_TCA() != null) {
						cellB.setCellValue(record.getR13_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					cellB = row.createCell(1);
					if (record.getR14_TCA() != null) {
						cellB.setCellValue(record.getR14_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);
					cellB = row.createCell(1);
					if (record.getR15_TCA() != null) {
						cellB.setCellValue(record.getR15_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cellB = row.createCell(1);
					if (record.getR16_TCA() != null) {
						cellB.setCellValue(record.getR16_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cellB = row.createCell(1);
					if (record.getR17_TCA() != null) {
						cellB.setCellValue(record.getR17_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					// row18
					row = sheet.getRow(17);
					cellB = row.createCell(1);
					if (record.getR18_TCA() != null) {
						cellB.setCellValue(record.getR18_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 10;

			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(6);
					if (record.getR11_TCA2() != null) {
						cellB.setCellValue(record.getR11_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					cellB = row.createCell(6);
					if (record.getR12_TCA2() != null) {
						cellB.setCellValue(record.getR12_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cellB = row.createCell(6);
					if (record.getR13_TCA2() != null) {
						cellB.setCellValue(record.getR13_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					cellB = row.createCell(6);
					if (record.getR14_TCA2() != null) {
						cellB.setCellValue(record.getR14_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					cellB = row.createCell(6);
					if (record.getR15_TCA2() != null) {
						cellB.setCellValue(record.getR15_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 25;

			if (!dataList3.isEmpty()) {
				for (int i = 0; i < dataList3.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity3 record = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB = row.createCell(1);
					if (record.getR26_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR26_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR26_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR26_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR26_O5Y_FT() != null) {
						cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR26_O5Y_HTM() != null) {
						celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row27

					row = sheet.getRow(26);

					cellB = row.createCell(1);
					if (record.getR27_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR27_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR27_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR27_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR27_O5Y_FT() != null) {
						cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR27_O5Y_HTM() != null) {
						celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cellB = row.createCell(1);
					if (record.getR28_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR28_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR28_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR28_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR28_O5Y_FT() != null) {
						cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR28_O5Y_HTM() != null) {
						celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cellB = row.createCell(1);
					if (record.getR29_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR29_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR29_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR29_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR29_O5Y_FT() != null) {
						cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR29_O5Y_HTM() != null) {
						celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cellB = row.createCell(1);
					if (record.getR30_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR30_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR30_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR30_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR30_O5Y_FT() != null) {
						cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR30_O5Y_HTM() != null) {
						celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
				}
			}

			startRow = 35;

			if (!dataList4.isEmpty()) {
				for (int i = 0; i < dataList4.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity4 record = dataList4.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellB = row.createCell(1);
					if (record.getR36_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR36_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR36_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR36_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR36_O5Y_FT() != null) {
						cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR36_O5Y_HTM() != null) {
						celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row37

					row = sheet.getRow(36);

					cellB = row.createCell(1);
					if (record.getR37_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR37_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR37_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR37_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR37_O5Y_FT() != null) {
						cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR37_O5Y_HTM() != null) {
						celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row38

					row = sheet.getRow(37);

					cellB = row.createCell(1);
					if (record.getR38_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR38_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR38_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR38_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR38_O5Y_FT() != null) {
						cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR38_O5Y_HTM() != null) {
						celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row39

					row = sheet.getRow(38);

					cellB = row.createCell(1);
					if (record.getR39_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR39_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR39_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR39_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR39_O5Y_FT() != null) {
						cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR39_O5Y_HTM() != null) {
						celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);

					cellB = row.createCell(1);
					if (record.getR40_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR40_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR40_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR40_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR40_O5Y_FT() != null) {
						cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR40_O5Y_HTM() != null) {
						celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cellB = row.createCell(1);
					if (record.getR41_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR41_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellE = row.createCell(4);
					if (record.getR41_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR41_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR41_O5Y_FT() != null) {
						cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR41_O5Y_HTM() != null) {
						celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cellB = row.createCell(1);
					if (record.getR42_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR42_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR42_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR42_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR42_O5Y_FT() != null) {
						cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR42_O5Y_HTM() != null) {
						celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SEC ARCHIVAL SUMMARY", null,
						"BRRS_M_SEC_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_SECARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		Date reportDate = dateformat.parse(todate);
		List<BRRS_M_SEC_Archival_Summary_Entity1> dataList1 = getdatabydateListarchival1(reportDate, version);
		List<BRRS_M_SEC_Archival_Summary_Entity2> dataList2 = getdatabydateListarchival2(reportDate, version);
		List<BRRS_M_SEC_Archival_Summary_Entity3> dataList3 = getdatabydateListarchival3(reportDate, version);
		List<BRRS_M_SEC_Archival_Summary_Entity4> dataList4 = getdatabydateListarchival4(reportDate, version);

		// If no data, try to get latest available date and version
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				Optional<BRRS_M_SEC_Archival_Summary_Entity1> latestOpt1 = getLatestArchivalSummaryVersionByDate(
						latestDate);
				Optional<BRRS_M_SEC_Archival_Summary_Entity2> latestOpt2 = getLatestArchivalSummaryVersionByDate2(
						latestDate);
				Optional<BRRS_M_SEC_Archival_Summary_Entity3> latestOpt3 = getLatestArchivalSummaryVersionByDate3(
						latestDate);
				Optional<BRRS_M_SEC_Archival_Summary_Entity4> latestOpt4 = getLatestArchivalSummaryVersionByDate4(
						latestDate);

				if (latestOpt1.isPresent() && latestOpt2.isPresent() && latestOpt3.isPresent()
						&& latestOpt4.isPresent()) {
					dataList1 = getdatabydateListarchival1(latestDate, latestOpt1.get().getReport_version());
					dataList2 = getdatabydateListarchival2(latestDate, latestOpt2.get().getReport_version());
					dataList3 = getdatabydateListarchival3(latestDate, latestOpt3.get().getReport_version());
					dataList4 = getdatabydateListarchival4(latestDate, latestOpt4.get().getReport_version());
					logger.info("No data for requested date. Using latest available date: {} with version: {}",
							latestDate, latestOpt1.get().getReport_version());
				}
			}
		}

		// --- LOG IF EMPTY BUT DO NOT CANCEL DOWNLOAD ---
		if (dataList1.isEmpty())
			logger.warn("ARCHIVAL List1 is empty (continuing).");
		if (dataList2.isEmpty())
			logger.warn("ARCHIVAL List2 is empty (continuing).");
		if (dataList3.isEmpty())
			logger.warn("ARCHIVAL List3 is empty (continuing).");
		if (dataList4.isEmpty())
			logger.warn("ARCHIVAL List4 is empty (continuing).");

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

			int startRow = 6;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row7
					// Column B
					Cell cellBdate = row.createCell(5);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

					// ROW 10
					row = sheet.getRow(9);

					// row10
					// Column B2 - Original Amount
					Cell cellB = row.createCell(2);
					if (record.getR11_TCA() != null) {
						cellB.setCellValue(record.getR11_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					cellB = row.createCell(2);
					if (record.getR12_TCA() != null) {
						cellB.setCellValue(record.getR12_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					cellB = row.createCell(2);
					if (record.getR13_TCA() != null) {
						cellB.setCellValue(record.getR13_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					cellB = row.createCell(2);
					if (record.getR14_TCA() != null) {
						cellB.setCellValue(record.getR14_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					cellB = row.createCell(2);
					if (record.getR15_TCA() != null) {
						cellB.setCellValue(record.getR15_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cellB = row.createCell(2);
					if (record.getR16_TCA() != null) {
						cellB.setCellValue(record.getR16_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cellB = row.createCell(2);
					if (record.getR17_TCA() != null) {
						cellB.setCellValue(record.getR17_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cellB = row.createCell(2);
					if (record.getR18_TCA() != null) {
						cellB.setCellValue(record.getR18_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					cellB = row.createCell(2);
					if (record.getR19_TCA() != null) {
						cellB.setCellValue(record.getR19_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 9;

			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row10
					// Column B2 - Original Amount
					Cell cellB = row.createCell(7);
					if (record.getR11_TCA2() != null) {
						cellB.setCellValue(record.getR11_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					cellB = row.createCell(7);
					if (record.getR12_TCA2() != null) {
						cellB.setCellValue(record.getR12_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					cellB = row.createCell(7);
					if (record.getR13_TCA2() != null) {
						cellB.setCellValue(record.getR13_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cellB = row.createCell(7);
					if (record.getR14_TCA2() != null) {
						cellB.setCellValue(record.getR14_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					cellB = row.createCell(7);
					if (record.getR15_TCA2() != null) {
						cellB.setCellValue(record.getR15_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					cellB = row.createCell(7);
					if (record.getR16_TCA2() != null) {
						cellB.setCellValue(record.getR16_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 24;

			if (!dataList3.isEmpty()) {
				for (int i = 0; i < dataList3.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity3 record = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB = row.createCell(2);
					if (record.getR26_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(3);
					if (record.getR26_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D IN EXCEL E

					Cell cellD = row.createCell(4);
					if (record.getR26_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR26_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row11
					// Column E - No. of Accounts
					Cell cellE = row.createCell(5);
					if (record.getR26_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(6);
					if (record.getR26_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G IN EXCEL H

					Cell cellG = row.createCell(7);
					if (record.getR26_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR26_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

//							Cell cellH = row.createCell(8);
//							if (record.getR26_O5Y_FT() != null) {
//								cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
//								cellH.setCellStyle(numberStyle);
//							} else {
//								cellH.setCellValue("");
//								cellH.setCellStyle(textStyle);
//							}
					//
//							Cell celli = row.createCell(9);
//							if (record.getR26_O5Y_HTM() != null) {
//								celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
//								celli.setCellStyle(numberStyle);
//							} else {
//								celli.setCellValue("");
//								celli.setCellStyle(textStyle);
//							}
					//
//							//J 
					//
//							Cell cellJ = row.createCell(10);
//							if (record.getR26_O5Y_TOTAL() != null) {
//								cellJ.setCellValue(record.getR26_O5Y_TOTAL().doubleValue());
//								cellJ.setCellStyle(numberStyle);
//							} else {
//								cellJ.setCellValue("");
//								cellJ.setCellStyle(textStyle);
//							}

					// K

					Cell cellK = row.createCell(8);
					if (record.getR26_T_FT() != null) {
						cellK.setCellValue(record.getR26_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L

					Cell cellL = row.createCell(9);
					if (record.getR26_T_HTM() != null) {
						cellL.setCellValue(record.getR26_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M

					Cell cellM = row.createCell(10);
					if (record.getR26_T_TOTAL() != null) {
						cellM.setCellValue(record.getR26_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R27 OLD =====================

					// row26

					row = sheet.getRow(25);

					cellB = row.createCell(2);
					if (record.getR27_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR27_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR27_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR27_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR27_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR27_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR27_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR27_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR27_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR27_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR27_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR27_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR27_T_FT() != null) {
						cellK.setCellValue(record.getR27_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR27_T_HTM() != null) {
						cellL.setCellValue(record.getR27_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR27_T_TOTAL() != null) {
						cellM.setCellValue(record.getR27_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R28 =====================
					// row27
					row = sheet.getRow(26);

					cellB = row.createCell(2);
					if (record.getR28_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR28_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR28_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR28_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR28_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR28_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR28_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR28_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					//
					// cellH = row.createCell(8);
					// if (record.getR28_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR28_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR28_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR28_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR28_T_FT() != null) {
						cellK.setCellValue(record.getR28_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR28_T_HTM() != null) {
						cellL.setCellValue(record.getR28_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR28_T_TOTAL() != null) {
						cellM.setCellValue(record.getR28_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R29 =====================

					// row28
					row = sheet.getRow(27);

					cellB = row.createCell(2);
					if (record.getR29_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR29_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR29_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR29_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR29_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR29_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR29_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR29_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR29_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR29_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR29_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR29_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR29_T_FT() != null) {
						cellK.setCellValue(record.getR29_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR29_T_HTM() != null) {
						cellL.setCellValue(record.getR29_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR29_T_TOTAL() != null) {
						cellM.setCellValue(record.getR29_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R30 =====================

					// row29
					row = sheet.getRow(28);

					cellB = row.createCell(2);
					if (record.getR30_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR30_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR30_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR30_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR30_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR30_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR30_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR30_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR30_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR30_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR30_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR30_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR30_T_FT() != null) {
						cellK.setCellValue(record.getR30_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR30_T_HTM() != null) {
						cellL.setCellValue(record.getR30_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR30_T_TOTAL() != null) {
						cellM.setCellValue(record.getR30_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R31 =====================

					// row30
					row = sheet.getRow(29);

					cellB = row.createCell(2);
					if (record.getR31_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR31_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR31_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR31_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR31_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR31_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR31_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR31_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR31_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR31_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR31_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR31_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR31_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR31_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR31_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR31_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR31_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR31_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR31_T_FT() != null) {
						cellK.setCellValue(record.getR31_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR31_T_HTM() != null) {
						cellL.setCellValue(record.getR31_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR31_T_TOTAL() != null) {
						cellM.setCellValue(record.getR31_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

				}
			}

			startRow = 34;

			if (!dataList4.isEmpty()) {
				for (int i = 0; i < dataList4.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity4 record = dataList4.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ===================== R36 =====================

					Cell cellB = row.createCell(2);
					if (record.getR36_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(3);
					if (record.getR36_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					Cell cellD = row.createCell(4);
					if (record.getR36_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR36_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					Cell cellE = row.createCell(5);
					if (record.getR36_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					Cell cellf = row.createCell(6);
					if (record.getR36_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					Cell cellG = row.createCell(7);
					if (record.getR36_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR36_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Cell cellH = row.createCell(8);
					// if (record.getR36_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// Cell celli = row.createCell(9);
					// if (record.getR36_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// Cell cellJ = row.createCell(10);
					// if (record.getR36_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR36_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					Cell cellK = row.createCell(8);
					if (record.getR36_T_FT() != null) {
						cellK.setCellValue(record.getR36_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					Cell cellL = row.createCell(9);
					if (record.getR36_T_HTM() != null) {
						cellL.setCellValue(record.getR36_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					Cell cellM = row.createCell(10);
					if (record.getR36_T_TOTAL() != null) {
						cellM.setCellValue(record.getR36_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R37 =====================

					// row36

					row = sheet.getRow(35);

					cellB = row.createCell(2);
					if (record.getR37_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR37_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR37_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR37_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR37_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR37_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR37_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR37_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR37_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR37_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR37_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR37_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR37_T_FT() != null) {
						cellK.setCellValue(record.getR37_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR37_T_HTM() != null) {
						cellL.setCellValue(record.getR37_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR37_T_TOTAL() != null) {
						cellM.setCellValue(record.getR37_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R38 =====================

					// row37

					row = sheet.getRow(36);

					cellB = row.createCell(2);
					if (record.getR38_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR38_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR38_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR38_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR38_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR38_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR38_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR38_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR38_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR38_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR38_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR38_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR38_T_FT() != null) {
						cellK.setCellValue(record.getR38_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR38_T_HTM() != null) {
						cellL.setCellValue(record.getR38_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR38_T_TOTAL() != null) {
						cellM.setCellValue(record.getR38_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R39 =====================

					// row38

					row = sheet.getRow(37);

					cellB = row.createCell(2);
					if (record.getR39_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR39_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR39_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR39_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR39_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR39_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR39_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR39_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR39_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR39_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR39_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR39_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR39_T_FT() != null) {
						cellK.setCellValue(record.getR39_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR39_T_HTM() != null) {
						cellL.setCellValue(record.getR39_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR39_T_TOTAL() != null) {
						cellM.setCellValue(record.getR39_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					// ===================== R40 =====================

					// row39
					row = sheet.getRow(38);

					cellB = row.createCell(2);
					if (record.getR40_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR40_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR40_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR40_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR40_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR40_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR40_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR40_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR40_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR40_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR40_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR40_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR40_T_FT() != null) {
						cellK.setCellValue(record.getR40_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR40_T_HTM() != null) {
						cellL.setCellValue(record.getR40_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR40_T_TOTAL() != null) {
						cellM.setCellValue(record.getR40_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R41 =====================
					// row40
					row = sheet.getRow(39);

					cellB = row.createCell(2);
					if (record.getR41_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR41_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR41_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR41_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR41_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR41_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR41_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR41_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR41_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR41_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR41_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR41_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR41_T_FT() != null) {
						cellK.setCellValue(record.getR41_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR41_T_HTM() != null) {
						cellL.setCellValue(record.getR41_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR41_T_TOTAL() != null) {
						cellM.setCellValue(record.getR41_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R42 =====================
					// row41
					row = sheet.getRow(40);

					cellB = row.createCell(2);
					if (record.getR42_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR42_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					//// Total D → Excel E
					// cellD = row.createCell(4);
					// if (record.getR42_0_1Y_TOTAL() != null) {
					// cellD.setCellValue(record.getR42_0_1Y_TOTAL().doubleValue());
					// cellD.setCellStyle(numberStyle);
					// } else {
					// cellD.setCellValue("");
					// cellD.setCellStyle(textStyle);
					// }

					// Column F
					cellE = row.createCell(5);
					if (record.getR42_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR42_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR42_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR42_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR42_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR42_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR42_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR42_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR42_T_FT() != null) {
						cellK.setCellValue(record.getR42_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					//// L
					// cellL = row.createCell(12);
					// if (record.getR42_T_HTM() != null) {
					// cellL.setCellValue(record.getR42_T_HTM().doubleValue());
					// cellL.setCellStyle(numberStyle);
					// } else {
					// cellL.setCellValue("");
					// cellL.setCellStyle(textStyle);
					// }
					//
					//// M
					// cellM = row.createCell(13);
					// if (record.getR42_T_TOTAL() != null) {
					// cellM.setCellValue(record.getR42_T_TOTAL().doubleValue());
					// cellM.setCellStyle(numberStyle);
					// } else {
					// cellM.setCellValue("");
					// cellM.setCellStyle(textStyle);
					// }
					//
//										

					// ===================== R43 =====================

					// row42
					row = sheet.getRow(41);

					cellB = row.createCell(2);
					if (record.getR43_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR43_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR43_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR43_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR43_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR43_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR43_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR43_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// K
					cellK = row.createCell(8);
					if (record.getR43_T_FT() != null) {
						cellK.setCellValue(record.getR43_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SEC EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_SEC_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_SECResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SECEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		// 🔹 Fetch archival or summary based on RESUB/NORMAL
		Date reportDate = dateformat.parse(todate);
		List<M_SEC_RESUB_Summary_Entity1> dataList1 = getdatabydateListarchival17(reportDate, version);
		List<M_SEC_RESUB_Summary_Entity2> dataList2 = getdatabydateListarchival18(reportDate, version);
		List<M_SEC_RESUB_Summary_Entity3> dataList3 = getdatabydateListarchival19(reportDate, version);
		List<M_SEC_RESUB_Summary_Entity4> dataList4 = getdatabydateListarchival20(reportDate, version);

		// 🔹 If no data, try to get latest available version
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
			BigDecimal maxVersion1 = findMaxVersion17(reportDate);
			BigDecimal maxVersion2 = findMaxVersion18(reportDate);
			BigDecimal maxVersion3 = findMaxVersion19(reportDate);
			BigDecimal maxVersion4 = findMaxVersion20(reportDate);

			if (maxVersion1 != null && maxVersion2 != null && maxVersion3 != null && maxVersion4 != null) {
				dataList1 = getdatabydateListarchival17(reportDate, maxVersion1);
				dataList2 = getdatabydateListarchival18(reportDate, maxVersion2);
				dataList3 = getdatabydateListarchival19(reportDate, maxVersion3);
				dataList4 = getdatabydateListarchival20(reportDate, maxVersion4);
				logger.info("No data for requested version. Using latest version: {}", maxVersion1);
			}
		}

		// 🔹 Validate all 4 lists
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
			logger.warn("Service: No data found for M_SEC Report (Archival or Live). Returning empty result.");
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

			int startRow = 6;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					M_SEC_RESUB_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row7
					// Column B
					Cell cellBdate = row.createCell(1);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

					// ROW 11
					row = sheet.getRow(10);

					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(1);
					if (record.getR11_TCA() != null) {
						cellB.setCellValue(record.getR11_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cellB = row.createCell(1);
					if (record.getR12_TCA() != null) {
						cellB.setCellValue(record.getR12_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					cellB = row.createCell(1);
					if (record.getR13_TCA() != null) {
						cellB.setCellValue(record.getR13_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					cellB = row.createCell(1);
					if (record.getR14_TCA() != null) {
						cellB.setCellValue(record.getR14_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);
					cellB = row.createCell(1);
					if (record.getR15_TCA() != null) {
						cellB.setCellValue(record.getR15_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cellB = row.createCell(1);
					if (record.getR16_TCA() != null) {
						cellB.setCellValue(record.getR16_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cellB = row.createCell(1);
					if (record.getR17_TCA() != null) {
						cellB.setCellValue(record.getR17_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					// row18
					row = sheet.getRow(17);
					cellB = row.createCell(1);
					if (record.getR18_TCA() != null) {
						cellB.setCellValue(record.getR18_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 10;

			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					M_SEC_RESUB_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(6);
					if (record.getR11_TCA2() != null) {
						cellB.setCellValue(record.getR11_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					cellB = row.createCell(6);
					if (record.getR12_TCA2() != null) {
						cellB.setCellValue(record.getR12_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cellB = row.createCell(6);
					if (record.getR13_TCA2() != null) {
						cellB.setCellValue(record.getR13_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					cellB = row.createCell(6);
					if (record.getR14_TCA2() != null) {
						cellB.setCellValue(record.getR14_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					cellB = row.createCell(6);
					if (record.getR15_TCA2() != null) {
						cellB.setCellValue(record.getR15_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 25;

			if (!dataList3.isEmpty()) {
				for (int i = 0; i < dataList3.size(); i++) {
					M_SEC_RESUB_Summary_Entity3 record = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB = row.createCell(1);
					if (record.getR26_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR26_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR26_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR26_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR26_O5Y_FT() != null) {
						cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR26_O5Y_HTM() != null) {
						celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row27

					row = sheet.getRow(26);

					cellB = row.createCell(1);
					if (record.getR27_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR27_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR27_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR27_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR27_O5Y_FT() != null) {
						cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR27_O5Y_HTM() != null) {
						celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cellB = row.createCell(1);
					if (record.getR28_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR28_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR28_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR28_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR28_O5Y_FT() != null) {
						cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR28_O5Y_HTM() != null) {
						celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cellB = row.createCell(1);
					if (record.getR29_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR29_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR29_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR29_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR29_O5Y_FT() != null) {
						cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR29_O5Y_HTM() != null) {
						celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cellB = row.createCell(1);
					if (record.getR30_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR30_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR30_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR30_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR30_O5Y_FT() != null) {
						cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR30_O5Y_HTM() != null) {
						celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
				}
			}

			startRow = 35;

			if (!dataList4.isEmpty()) {
				for (int i = 0; i < dataList4.size(); i++) {
					M_SEC_RESUB_Summary_Entity4 record = dataList4.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellB = row.createCell(1);
					if (record.getR36_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR36_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR36_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR36_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR36_O5Y_FT() != null) {
						cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR36_O5Y_HTM() != null) {
						celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row37

					row = sheet.getRow(36);

					cellB = row.createCell(1);
					if (record.getR37_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR37_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR37_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR37_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR37_O5Y_FT() != null) {
						cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR37_O5Y_HTM() != null) {
						celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row38

					row = sheet.getRow(37);

					cellB = row.createCell(1);
					if (record.getR38_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR38_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR38_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR38_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR38_O5Y_FT() != null) {
						cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR38_O5Y_HTM() != null) {
						celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row39

					row = sheet.getRow(38);

					cellB = row.createCell(1);
					if (record.getR39_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR39_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR39_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR39_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR39_O5Y_FT() != null) {
						cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR39_O5Y_HTM() != null) {
						celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);

					cellB = row.createCell(1);
					if (record.getR40_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR40_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR40_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR40_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR40_O5Y_FT() != null) {
						cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR40_O5Y_HTM() != null) {
						celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cellB = row.createCell(1);
					if (record.getR41_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR41_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellE = row.createCell(4);
					if (record.getR41_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR41_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR41_O5Y_FT() != null) {
						cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR41_O5Y_HTM() != null) {
						celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cellB = row.createCell(1);
					if (record.getR42_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR42_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR42_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR42_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR42_O5Y_FT() != null) {
						cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR42_O5Y_HTM() != null) {
						celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SEC RESUB SUMMARY", null,
						"BRRS_M_SEC_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_SECEmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Resub Email Excel generation process in memory.");

		// 🔹 Fetch archival or summary based on RESUB/NORMAL
		Date reportDate = dateformat.parse(todate);
		List<M_SEC_RESUB_Summary_Entity1> dataList1 = getdatabydateListarchival17(reportDate, version);
		List<M_SEC_RESUB_Summary_Entity2> dataList2 = getdatabydateListarchival18(reportDate, version);
		List<M_SEC_RESUB_Summary_Entity3> dataList3 = getdatabydateListarchival19(reportDate, version);
		List<M_SEC_RESUB_Summary_Entity4> dataList4 = getdatabydateListarchival20(reportDate, version);

		// 🔹 If no data, try to get latest available version
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
			BigDecimal maxVersion1 = findMaxVersion17(reportDate);
			BigDecimal maxVersion2 = findMaxVersion18(reportDate);
			BigDecimal maxVersion3 = findMaxVersion19(reportDate);
			BigDecimal maxVersion4 = findMaxVersion20(reportDate);

			if (maxVersion1 != null && maxVersion2 != null && maxVersion3 != null && maxVersion4 != null) {
				dataList1 = getdatabydateListarchival17(reportDate, maxVersion1);
				dataList2 = getdatabydateListarchival18(reportDate, maxVersion2);
				dataList3 = getdatabydateListarchival19(reportDate, maxVersion3);
				dataList4 = getdatabydateListarchival20(reportDate, maxVersion4);
				logger.info("No data for requested version. Using latest version: {}", maxVersion1);
			}
		}

		// 🔹 Validate all 4 lists
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
			logger.warn("Service: No data found for M_SEC Report (Archival or Live). Returning empty result.");
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

			int startRow = 6;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					M_SEC_RESUB_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row7
					// Column B
					Cell cellBdate = row.createCell(5);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

					// ROW 10
					row = sheet.getRow(9);

					// row10
					// Column B2 - Original Amount
					Cell cellB = row.createCell(2);
					if (record.getR11_TCA() != null) {
						cellB.setCellValue(record.getR11_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					cellB = row.createCell(2);
					if (record.getR12_TCA() != null) {
						cellB.setCellValue(record.getR12_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					cellB = row.createCell(2);
					if (record.getR13_TCA() != null) {
						cellB.setCellValue(record.getR13_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					cellB = row.createCell(2);
					if (record.getR14_TCA() != null) {
						cellB.setCellValue(record.getR14_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					cellB = row.createCell(2);
					if (record.getR15_TCA() != null) {
						cellB.setCellValue(record.getR15_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cellB = row.createCell(2);
					if (record.getR16_TCA() != null) {
						cellB.setCellValue(record.getR16_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cellB = row.createCell(2);
					if (record.getR17_TCA() != null) {
						cellB.setCellValue(record.getR17_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cellB = row.createCell(2);
					if (record.getR18_TCA() != null) {
						cellB.setCellValue(record.getR18_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					cellB = row.createCell(2);
					if (record.getR19_TCA() != null) {
						cellB.setCellValue(record.getR19_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 9;

			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					M_SEC_RESUB_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row10
					// Column B2 - Original Amount
					Cell cellB = row.createCell(7);
					if (record.getR11_TCA2() != null) {
						cellB.setCellValue(record.getR11_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					cellB = row.createCell(7);
					if (record.getR12_TCA2() != null) {
						cellB.setCellValue(record.getR12_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					cellB = row.createCell(7);
					if (record.getR13_TCA2() != null) {
						cellB.setCellValue(record.getR13_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cellB = row.createCell(7);
					if (record.getR14_TCA2() != null) {
						cellB.setCellValue(record.getR14_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					cellB = row.createCell(7);
					if (record.getR15_TCA2() != null) {
						cellB.setCellValue(record.getR15_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					cellB = row.createCell(7);
					if (record.getR16_TCA2() != null) {
						cellB.setCellValue(record.getR16_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 24;

			if (!dataList3.isEmpty()) {
				for (int i = 0; i < dataList3.size(); i++) {
					M_SEC_RESUB_Summary_Entity3 record = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB = row.createCell(2);
					if (record.getR26_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(3);
					if (record.getR26_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D IN EXCEL E

					Cell cellD = row.createCell(4);
					if (record.getR26_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR26_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row11
					// Column E - No. of Accounts
					Cell cellE = row.createCell(5);
					if (record.getR26_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(6);
					if (record.getR26_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G IN EXCEL H

					Cell cellG = row.createCell(7);
					if (record.getR26_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR26_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

//		Cell cellH = row.createCell(8);
//		if (record.getR26_O5Y_FT() != null) {
//			cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
//			cellH.setCellStyle(numberStyle);
//		} else {
//			cellH.setCellValue("");
//			cellH.setCellStyle(textStyle);
//		}
					//
//		Cell celli = row.createCell(9);
//		if (record.getR26_O5Y_HTM() != null) {
//			celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
//			celli.setCellStyle(numberStyle);
//		} else {
//			celli.setCellValue("");
//			celli.setCellStyle(textStyle);
//		}
					//
//		//J 
					//
//		Cell cellJ = row.createCell(10);
//		if (record.getR26_O5Y_TOTAL() != null) {
//			cellJ.setCellValue(record.getR26_O5Y_TOTAL().doubleValue());
//			cellJ.setCellStyle(numberStyle);
//		} else {
//			cellJ.setCellValue("");
//			cellJ.setCellStyle(textStyle);
//		}

					// K

					Cell cellK = row.createCell(8);
					if (record.getR26_T_FT() != null) {
						cellK.setCellValue(record.getR26_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L

					Cell cellL = row.createCell(9);
					if (record.getR26_T_HTM() != null) {
						cellL.setCellValue(record.getR26_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M

					Cell cellM = row.createCell(10);
					if (record.getR26_T_TOTAL() != null) {
						cellM.setCellValue(record.getR26_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R27 OLD =====================

					// row26

					row = sheet.getRow(25);

					cellB = row.createCell(2);
					if (record.getR27_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR27_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR27_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR27_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR27_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR27_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR27_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR27_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR27_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR27_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR27_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR27_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR27_T_FT() != null) {
						cellK.setCellValue(record.getR27_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR27_T_HTM() != null) {
						cellL.setCellValue(record.getR27_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR27_T_TOTAL() != null) {
						cellM.setCellValue(record.getR27_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R28 =====================
					// row27
					row = sheet.getRow(26);

					cellB = row.createCell(2);
					if (record.getR28_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR28_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR28_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR28_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR28_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR28_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR28_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR28_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					//
					// cellH = row.createCell(8);
					// if (record.getR28_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR28_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR28_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR28_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR28_T_FT() != null) {
						cellK.setCellValue(record.getR28_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR28_T_HTM() != null) {
						cellL.setCellValue(record.getR28_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR28_T_TOTAL() != null) {
						cellM.setCellValue(record.getR28_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R29 =====================

					// row28
					row = sheet.getRow(27);

					cellB = row.createCell(2);
					if (record.getR29_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR29_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR29_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR29_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR29_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR29_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR29_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR29_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR29_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR29_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR29_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR29_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR29_T_FT() != null) {
						cellK.setCellValue(record.getR29_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR29_T_HTM() != null) {
						cellL.setCellValue(record.getR29_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR29_T_TOTAL() != null) {
						cellM.setCellValue(record.getR29_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R30 =====================

					// row29
					row = sheet.getRow(28);

					cellB = row.createCell(2);
					if (record.getR30_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR30_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR30_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR30_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR30_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR30_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR30_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR30_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR30_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR30_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR30_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR30_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR30_T_FT() != null) {
						cellK.setCellValue(record.getR30_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR30_T_HTM() != null) {
						cellL.setCellValue(record.getR30_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR30_T_TOTAL() != null) {
						cellM.setCellValue(record.getR30_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R31 =====================

					// row30
					row = sheet.getRow(29);

					cellB = row.createCell(2);
					if (record.getR31_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR31_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR31_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR31_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR31_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR31_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR31_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR31_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR31_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR31_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR31_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR31_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR31_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR31_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR31_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR31_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR31_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR31_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR31_T_FT() != null) {
						cellK.setCellValue(record.getR31_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR31_T_HTM() != null) {
						cellL.setCellValue(record.getR31_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR31_T_TOTAL() != null) {
						cellM.setCellValue(record.getR31_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

				}
			}

			startRow = 34;

			if (!dataList4.isEmpty()) {
				for (int i = 0; i < dataList4.size(); i++) {
					M_SEC_RESUB_Summary_Entity4 record = dataList4.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ===================== R36 =====================

					Cell cellB = row.createCell(2);
					if (record.getR36_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(3);
					if (record.getR36_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					Cell cellD = row.createCell(4);
					if (record.getR36_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR36_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					Cell cellE = row.createCell(5);
					if (record.getR36_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					Cell cellf = row.createCell(6);
					if (record.getR36_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					Cell cellG = row.createCell(7);
					if (record.getR36_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR36_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Cell cellH = row.createCell(8);
					// if (record.getR36_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// Cell celli = row.createCell(9);
					// if (record.getR36_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// Cell cellJ = row.createCell(10);
					// if (record.getR36_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR36_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					Cell cellK = row.createCell(8);
					if (record.getR36_T_FT() != null) {
						cellK.setCellValue(record.getR36_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					Cell cellL = row.createCell(9);
					if (record.getR36_T_HTM() != null) {
						cellL.setCellValue(record.getR36_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					Cell cellM = row.createCell(10);
					if (record.getR36_T_TOTAL() != null) {
						cellM.setCellValue(record.getR36_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R37 =====================

					// row36

					row = sheet.getRow(35);

					cellB = row.createCell(2);
					if (record.getR37_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR37_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR37_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR37_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR37_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR37_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR37_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR37_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR37_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR37_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR37_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR37_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR37_T_FT() != null) {
						cellK.setCellValue(record.getR37_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR37_T_HTM() != null) {
						cellL.setCellValue(record.getR37_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR37_T_TOTAL() != null) {
						cellM.setCellValue(record.getR37_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R38 =====================

					// row37

					row = sheet.getRow(36);

					cellB = row.createCell(2);
					if (record.getR38_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR38_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR38_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR38_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR38_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR38_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR38_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR38_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR38_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR38_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR38_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR38_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR38_T_FT() != null) {
						cellK.setCellValue(record.getR38_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR38_T_HTM() != null) {
						cellL.setCellValue(record.getR38_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR38_T_TOTAL() != null) {
						cellM.setCellValue(record.getR38_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R39 =====================

					// row38

					row = sheet.getRow(37);

					cellB = row.createCell(2);
					if (record.getR39_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR39_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR39_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR39_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR39_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR39_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR39_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR39_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR39_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR39_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR39_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR39_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR39_T_FT() != null) {
						cellK.setCellValue(record.getR39_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR39_T_HTM() != null) {
						cellL.setCellValue(record.getR39_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR39_T_TOTAL() != null) {
						cellM.setCellValue(record.getR39_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					// ===================== R40 =====================

					// row39
					row = sheet.getRow(38);

					cellB = row.createCell(2);
					if (record.getR40_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR40_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR40_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR40_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR40_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR40_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR40_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR40_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR40_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR40_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR40_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR40_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR40_T_FT() != null) {
						cellK.setCellValue(record.getR40_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR40_T_HTM() != null) {
						cellL.setCellValue(record.getR40_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR40_T_TOTAL() != null) {
						cellM.setCellValue(record.getR40_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R41 =====================
					// row40
					row = sheet.getRow(39);

					cellB = row.createCell(2);
					if (record.getR41_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR41_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR41_0_1Y_TOTAL() != null) {
						cellD.setCellValue(record.getR41_0_1Y_TOTAL().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR41_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR41_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR41_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR41_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR41_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR41_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR41_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR41_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR41_T_FT() != null) {
						cellK.setCellValue(record.getR41_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// L
					cellL = row.createCell(9);
					if (record.getR41_T_HTM() != null) {
						cellL.setCellValue(record.getR41_T_HTM().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// M
					cellM = row.createCell(10);
					if (record.getR41_T_TOTAL() != null) {
						cellM.setCellValue(record.getR41_T_TOTAL().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// ===================== R42 =====================
					// row41
					row = sheet.getRow(40);

					cellB = row.createCell(2);
					if (record.getR42_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR42_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					//// Total D → Excel E
					// cellD = row.createCell(4);
					// if (record.getR42_0_1Y_TOTAL() != null) {
					// cellD.setCellValue(record.getR42_0_1Y_TOTAL().doubleValue());
					// cellD.setCellStyle(numberStyle);
					// } else {
					// cellD.setCellValue("");
					// cellD.setCellStyle(textStyle);
					// }

					// Column F
					cellE = row.createCell(5);
					if (record.getR42_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR42_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR42_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR42_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// cellH = row.createCell(8);
					// if (record.getR42_O5Y_FT() != null) {
					// cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
					// cellH.setCellStyle(numberStyle);
					// } else {
					// cellH.setCellValue("");
					// cellH.setCellStyle(textStyle);
					// }
					//
					// celli = row.createCell(9);
					// if (record.getR42_O5Y_HTM() != null) {
					// celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
					// celli.setCellStyle(numberStyle);
					// } else {
					// celli.setCellValue("");
					// celli.setCellStyle(textStyle);
					// }
					//
					//// J
					// cellJ = row.createCell(10);
					// if (record.getR42_O5Y_TOTAL() != null) {
					// cellJ.setCellValue(record.getR42_O5Y_TOTAL().doubleValue());
					// cellJ.setCellStyle(numberStyle);
					// } else {
					// cellJ.setCellValue("");
					// cellJ.setCellStyle(textStyle);
					// }

					// K
					cellK = row.createCell(8);
					if (record.getR42_T_FT() != null) {
						cellK.setCellValue(record.getR42_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					//// L
					// cellL = row.createCell(12);
					// if (record.getR42_T_HTM() != null) {
					// cellL.setCellValue(record.getR42_T_HTM().doubleValue());
					// cellL.setCellStyle(numberStyle);
					// } else {
					// cellL.setCellValue("");
					// cellL.setCellStyle(textStyle);
					// }
					//
					//// M
					// cellM = row.createCell(13);
					// if (record.getR42_T_TOTAL() != null) {
					// cellM.setCellValue(record.getR42_T_TOTAL().doubleValue());
					// cellM.setCellStyle(numberStyle);
					// } else {
					// cellM.setCellValue("");
					// cellM.setCellStyle(textStyle);
					// }
					//
//					

					// ===================== R43 =====================

					// row42
					row = sheet.getRow(41);

					cellB = row.createCell(2);
					if (record.getR43_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR43_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column F
					cellE = row.createCell(5);
					if (record.getR43_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR43_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR43_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR43_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					// Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR43_1_5Y_TOTAL() != null) {
						cellG.setCellValue(record.getR43_1_5Y_TOTAL().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// K
					cellK = row.createCell(8);
					if (record.getR43_T_FT() != null) {
						cellK.setCellValue(record.getR43_T_FT().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SEC EMAIL RESUB SUMMARY", null,
						"BRRS_M_SEC_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}
}
