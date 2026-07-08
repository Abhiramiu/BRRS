package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
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
import java.util.Objects;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

public class BRRS_M_OPTR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OPTR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// SUMMARY
	// Fetch data by report date
	public List<M_OPTR_Summary_Entity> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_OPTR_RowMapper_Summary());
	}

	// ARCHIVAL

	// Fetch data by report date
	public List<M_OPTR_Archival_Summary_Entity> ArchivalgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_OPTR_RowMapper_Archival());
	}

	// RESUB

	// Fetch data by report date
	public List<M_OPTR_RESUB_Summary_Entity> ResubgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_OPTR_RowMapper_Resub());
	}

	/*
	 * // ARCHIVAL // GET REPORT_DATE + REPORT_VERSION
	 * 
	 * public List<Object[]> getM_OPTRArchival() {
	 * 
	 * String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
	 * "FROM BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY" + "ORDER BY REPORT_VERSION";
	 * 
	 * return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] {
	 * rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") }); }
	 */

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_OPTR_Archival_Summary_Entity> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_OPTR_RowMapper_Archival());
	}

	// GET RESUB FULL DATA BY DATE + VERSION

	public List<M_OPTR_RESUB_Summary_Entity> getdatabydateListresub1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_OPTR_RowMapper_Resub());
	}

	// GET DETAIL FULL DATA BY DATE + VERSION

	public List<M_OPTR_Detail_Entity> getdatabydateListDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE" + "WHERE REPORT_DATE = ? " + "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_OPTRRowMapper_Detail());
	}

	// GET ARCHIVAL DETAIL FULL DATA BY DATE + VERSION

	public List<M_OPTR_Archival_Detail_Entity> getdatabydateListArchivalDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new M_OPTRRowMapper_ArchivalDetail());
	}

	// GET RESUB DETAIL FULL DATA BY DATE + VERSION

	public List<M_OPTR_RESUB_Detail_Entity> getdatabydateListResubDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_OPTRRowMapper_ResubDetail());
	}

	// GET ALL WITH VERSION

	public List<M_OPTR_Archival_Summary_Entity> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_OPTR_RowMapper_Archival());
	}

	// GET RESUB ALL WITH VERSION

	public List<M_OPTR_RESUB_Summary_Entity> ResubgetdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_OPTR_RowMapper_Resub());
	}

	// GET ARCHIVAL MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY" + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// GET RESUB MAX VERSION BY DATE

	public BigDecimal RESUBfindMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_OPTR_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// DETAIL TABLE 1
	// 1. BY DATE + LABEL + CRITERIA

	public List<M_OPTR_Detail_Entity> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_OPTRRowMapper_Detail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_OPTR_Detail_Entity> getDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_OPTRRowMapper_Detail());
	}

	// 3. PAGINATION

	public List<M_OPTR_Detail_Entity> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_OPTRRowMapper_Detail());
	}

	// 4. COUNT

	public int getDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_OPTR_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_OPTR_Detail_Entity> GetDetailDataByRowIdAndColumnId1(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_OPTRRowMapper_Detail());
	}

	// 6. BY ACCOUNT NUMBER

	public M_OPTR_Detail_Entity findByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_OPTRRowMapper_Detail());
	}

	// ARCHIVALTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<M_OPTR_Archival_Detail_Entity> findByArchivalDetailReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_OPTRRowMapper_ArchivalDetail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_OPTR_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_OPTRRowMapper_ArchivalDetail());
	}

	// 3. PAGINATION

	public List<M_OPTR_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate, int offset,
			int limit) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new M_OPTRRowMapper_ArchivalDetail());
	}

	// 4. COUNT

	public int getArchivalDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_OPTR_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_OPTRRowMapper_ArchivalDetail());
	}
	// 6. BY ACCOUNT NUMBER

	public M_OPTR_Archival_Detail_Entity ArchivalfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_OPTR_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_OPTRRowMapper_ArchivalDetail());
	}

	// RESUBTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<M_OPTR_RESUB_Detail_Entity> findByResubReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_OPTRRowMapper_ResubDetail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_OPTR_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_OPTRRowMapper_ResubDetail());
	}

	// 3. PAGINATION

	public List<M_OPTR_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_OPTRRowMapper_ResubDetail());
	}

	// 4. COUNT

	public int getResubdatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_OPTR_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_OPTR_RESUB_Detail_Entity> GetResubDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_OPTRRowMapper_ResubDetail());
	}
	// 6. BY ACCOUNT NUMBER

	public M_OPTR_RESUB_Detail_Entity ResubfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_OPTR_RESUB_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_OPTRRowMapper_ResubDetail());
	}

	// findSummaryByReportDate

	@Transactional(readOnly = true)
	public M_OPTR_Summary_Entity findSummaryByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		List<M_OPTR_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_OPTR_RowMapper_Summary());

		return list.isEmpty() ? null : list.get(0);
	}

	@Transactional(readOnly = true)
	public M_OPTR_Detail_Entity findDetailByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OPTR_DETAILTABLE " + "WHERE REPORT_DATE = ?";

		List<M_OPTR_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_OPTRRowMapper_Detail());

		return list.isEmpty() ? null : list.get(0);
	}

	// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class M_OPTR_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public M_OPTR_PK() {
		}

		public M_OPTR_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_OPTR_PK))
				return false;
			M_OPTR_PK that = (M_OPTR_PK) o;
			return Objects.equals(REPORT_DATE, that.REPORT_DATE) && Objects.equals(REPORT_VERSION, that.REPORT_VERSION);
		}

		@Override
		public int hashCode() {
			return Objects.hash(REPORT_DATE, REPORT_VERSION);
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}
	}

	// ROW MAPPER SUMMARY

	class M_OPTR_RowMapper_Summary implements RowMapper<M_OPTR_Summary_Entity> {

		@Override
		public M_OPTR_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OPTR_Summary_Entity obj = new M_OPTR_Summary_Entity();

			obj.setR10_INTEREST_RATES(rs.getBigDecimal("R10_INTEREST_RATES"));
			obj.setR10_EQUITIES(rs.getBigDecimal("R10_EQUITIES"));
			obj.setR10_FOREIGN_EXC_GOLD(rs.getBigDecimal("R10_FOREIGN_EXC_GOLD"));
			obj.setR10_COMMODITIES(rs.getBigDecimal("R10_COMMODITIES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			obj.setR11_INTEREST_RATES(rs.getBigDecimal("R11_INTEREST_RATES"));
			obj.setR11_EQUITIES(rs.getBigDecimal("R11_EQUITIES"));
			obj.setR11_FOREIGN_EXC_GOLD(rs.getBigDecimal("R11_FOREIGN_EXC_GOLD"));
			obj.setR11_COMMODITIES(rs.getBigDecimal("R11_COMMODITIES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			obj.setR12_INTEREST_RATES(rs.getBigDecimal("R12_INTEREST_RATES"));
			obj.setR12_EQUITIES(rs.getBigDecimal("R12_EQUITIES"));
			obj.setR12_FOREIGN_EXC_GOLD(rs.getBigDecimal("R12_FOREIGN_EXC_GOLD"));
			obj.setR12_COMMODITIES(rs.getBigDecimal("R12_COMMODITIES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INTEREST_RATES(rs.getBigDecimal("R13_INTEREST_RATES"));
			obj.setR13_EQUITIES(rs.getBigDecimal("R13_EQUITIES"));
			obj.setR13_FOREIGN_EXC_GOLD(rs.getBigDecimal("R13_FOREIGN_EXC_GOLD"));
			obj.setR13_COMMODITIES(rs.getBigDecimal("R13_COMMODITIES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INTEREST_RATES(rs.getBigDecimal("R14_INTEREST_RATES"));
			obj.setR14_EQUITIES(rs.getBigDecimal("R14_EQUITIES"));
			obj.setR14_FOREIGN_EXC_GOLD(rs.getBigDecimal("R14_FOREIGN_EXC_GOLD"));
			obj.setR14_COMMODITIES(rs.getBigDecimal("R14_COMMODITIES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_OPTR_Summary_Entity {

		private BigDecimal R10_INTEREST_RATES;
		private BigDecimal R10_EQUITIES;
		private BigDecimal R10_FOREIGN_EXC_GOLD;
		private BigDecimal R10_COMMODITIES;
		private BigDecimal R10_TOTAL;

		private BigDecimal R11_INTEREST_RATES;
		private BigDecimal R11_EQUITIES;
		private BigDecimal R11_FOREIGN_EXC_GOLD;
		private BigDecimal R11_COMMODITIES;
		private BigDecimal R11_TOTAL;

		private BigDecimal R12_INTEREST_RATES;
		private BigDecimal R12_EQUITIES;
		private BigDecimal R12_FOREIGN_EXC_GOLD;
		private BigDecimal R12_COMMODITIES;
		private BigDecimal R12_TOTAL;

		private BigDecimal R13_INTEREST_RATES;
		private BigDecimal R13_EQUITIES;
		private BigDecimal R13_FOREIGN_EXC_GOLD;
		private BigDecimal R13_COMMODITIES;
		private BigDecimal R13_TOTAL;

		private BigDecimal R14_INTEREST_RATES;
		private BigDecimal R14_EQUITIES;
		private BigDecimal R14_FOREIGN_EXC_GOLD;
		private BigDecimal R14_COMMODITIES;
		private BigDecimal R14_TOTAL;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public BigDecimal getR10_INTEREST_RATES() {
			return R10_INTEREST_RATES;
		}

		public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
			R10_INTEREST_RATES = r10_INTEREST_RATES;
		}

		public BigDecimal getR10_EQUITIES() {
			return R10_EQUITIES;
		}

		public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
			R10_EQUITIES = r10_EQUITIES;
		}

		public BigDecimal getR10_FOREIGN_EXC_GOLD() {
			return R10_FOREIGN_EXC_GOLD;
		}

		public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
			R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR10_COMMODITIES() {
			return R10_COMMODITIES;
		}

		public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
			R10_COMMODITIES = r10_COMMODITIES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public BigDecimal getR11_INTEREST_RATES() {
			return R11_INTEREST_RATES;
		}

		public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
			R11_INTEREST_RATES = r11_INTEREST_RATES;
		}

		public BigDecimal getR11_EQUITIES() {
			return R11_EQUITIES;
		}

		public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
			R11_EQUITIES = r11_EQUITIES;
		}

		public BigDecimal getR11_FOREIGN_EXC_GOLD() {
			return R11_FOREIGN_EXC_GOLD;
		}

		public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
			R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR11_COMMODITIES() {
			return R11_COMMODITIES;
		}

		public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
			R11_COMMODITIES = r11_COMMODITIES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public BigDecimal getR12_INTEREST_RATES() {
			return R12_INTEREST_RATES;
		}

		public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
			R12_INTEREST_RATES = r12_INTEREST_RATES;
		}

		public BigDecimal getR12_EQUITIES() {
			return R12_EQUITIES;
		}

		public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
			R12_EQUITIES = r12_EQUITIES;
		}

		public BigDecimal getR12_FOREIGN_EXC_GOLD() {
			return R12_FOREIGN_EXC_GOLD;
		}

		public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
			R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR12_COMMODITIES() {
			return R12_COMMODITIES;
		}

		public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
			R12_COMMODITIES = r12_COMMODITIES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public BigDecimal getR13_INTEREST_RATES() {
			return R13_INTEREST_RATES;
		}

		public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
			R13_INTEREST_RATES = r13_INTEREST_RATES;
		}

		public BigDecimal getR13_EQUITIES() {
			return R13_EQUITIES;
		}

		public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
			R13_EQUITIES = r13_EQUITIES;
		}

		public BigDecimal getR13_FOREIGN_EXC_GOLD() {
			return R13_FOREIGN_EXC_GOLD;
		}

		public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
			R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR13_COMMODITIES() {
			return R13_COMMODITIES;
		}

		public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
			R13_COMMODITIES = r13_COMMODITIES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public BigDecimal getR14_INTEREST_RATES() {
			return R14_INTEREST_RATES;
		}

		public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
			R14_INTEREST_RATES = r14_INTEREST_RATES;
		}

		public BigDecimal getR14_EQUITIES() {
			return R14_EQUITIES;
		}

		public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
			R14_EQUITIES = r14_EQUITIES;
		}

		public BigDecimal getR14_FOREIGN_EXC_GOLD() {
			return R14_FOREIGN_EXC_GOLD;
		}

		public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
			R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR14_COMMODITIES() {
			return R14_COMMODITIES;
		}

		public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
			R14_COMMODITIES = r14_COMMODITIES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

	// ROW MAPPER DETAIL

	class M_OPTRRowMapper_Detail implements RowMapper<M_OPTR_Detail_Entity> {

		@Override
		public M_OPTR_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OPTR_Detail_Entity obj = new M_OPTR_Detail_Entity();

			obj.setR10_INTEREST_RATES(rs.getBigDecimal("R10_INTEREST_RATES"));
			obj.setR10_EQUITIES(rs.getBigDecimal("R10_EQUITIES"));
			obj.setR10_FOREIGN_EXC_GOLD(rs.getBigDecimal("R10_FOREIGN_EXC_GOLD"));
			obj.setR10_COMMODITIES(rs.getBigDecimal("R10_COMMODITIES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			obj.setR11_INTEREST_RATES(rs.getBigDecimal("R11_INTEREST_RATES"));
			obj.setR11_EQUITIES(rs.getBigDecimal("R11_EQUITIES"));
			obj.setR11_FOREIGN_EXC_GOLD(rs.getBigDecimal("R11_FOREIGN_EXC_GOLD"));
			obj.setR11_COMMODITIES(rs.getBigDecimal("R11_COMMODITIES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			obj.setR12_INTEREST_RATES(rs.getBigDecimal("R12_INTEREST_RATES"));
			obj.setR12_EQUITIES(rs.getBigDecimal("R12_EQUITIES"));
			obj.setR12_FOREIGN_EXC_GOLD(rs.getBigDecimal("R12_FOREIGN_EXC_GOLD"));
			obj.setR12_COMMODITIES(rs.getBigDecimal("R12_COMMODITIES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INTEREST_RATES(rs.getBigDecimal("R13_INTEREST_RATES"));
			obj.setR13_EQUITIES(rs.getBigDecimal("R13_EQUITIES"));
			obj.setR13_FOREIGN_EXC_GOLD(rs.getBigDecimal("R13_FOREIGN_EXC_GOLD"));
			obj.setR13_COMMODITIES(rs.getBigDecimal("R13_COMMODITIES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INTEREST_RATES(rs.getBigDecimal("R14_INTEREST_RATES"));
			obj.setR14_EQUITIES(rs.getBigDecimal("R14_EQUITIES"));
			obj.setR14_FOREIGN_EXC_GOLD(rs.getBigDecimal("R14_FOREIGN_EXC_GOLD"));
			obj.setR14_COMMODITIES(rs.getBigDecimal("R14_COMMODITIES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_OPTR_Detail_Entity {

		private BigDecimal R10_INTEREST_RATES;
		private BigDecimal R10_EQUITIES;
		private BigDecimal R10_FOREIGN_EXC_GOLD;
		private BigDecimal R10_COMMODITIES;
		private BigDecimal R10_TOTAL;

		private BigDecimal R11_INTEREST_RATES;
		private BigDecimal R11_EQUITIES;
		private BigDecimal R11_FOREIGN_EXC_GOLD;
		private BigDecimal R11_COMMODITIES;
		private BigDecimal R11_TOTAL;

		private BigDecimal R12_INTEREST_RATES;
		private BigDecimal R12_EQUITIES;
		private BigDecimal R12_FOREIGN_EXC_GOLD;
		private BigDecimal R12_COMMODITIES;
		private BigDecimal R12_TOTAL;

		private BigDecimal R13_INTEREST_RATES;
		private BigDecimal R13_EQUITIES;
		private BigDecimal R13_FOREIGN_EXC_GOLD;
		private BigDecimal R13_COMMODITIES;
		private BigDecimal R13_TOTAL;

		private BigDecimal R14_INTEREST_RATES;
		private BigDecimal R14_EQUITIES;
		private BigDecimal R14_FOREIGN_EXC_GOLD;
		private BigDecimal R14_COMMODITIES;
		private BigDecimal R14_TOTAL;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public BigDecimal getR10_INTEREST_RATES() {
			return R10_INTEREST_RATES;
		}

		public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
			R10_INTEREST_RATES = r10_INTEREST_RATES;
		}

		public BigDecimal getR10_EQUITIES() {
			return R10_EQUITIES;
		}

		public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
			R10_EQUITIES = r10_EQUITIES;
		}

		public BigDecimal getR10_FOREIGN_EXC_GOLD() {
			return R10_FOREIGN_EXC_GOLD;
		}

		public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
			R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR10_COMMODITIES() {
			return R10_COMMODITIES;
		}

		public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
			R10_COMMODITIES = r10_COMMODITIES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public BigDecimal getR11_INTEREST_RATES() {
			return R11_INTEREST_RATES;
		}

		public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
			R11_INTEREST_RATES = r11_INTEREST_RATES;
		}

		public BigDecimal getR11_EQUITIES() {
			return R11_EQUITIES;
		}

		public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
			R11_EQUITIES = r11_EQUITIES;
		}

		public BigDecimal getR11_FOREIGN_EXC_GOLD() {
			return R11_FOREIGN_EXC_GOLD;
		}

		public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
			R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR11_COMMODITIES() {
			return R11_COMMODITIES;
		}

		public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
			R11_COMMODITIES = r11_COMMODITIES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public BigDecimal getR12_INTEREST_RATES() {
			return R12_INTEREST_RATES;
		}

		public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
			R12_INTEREST_RATES = r12_INTEREST_RATES;
		}

		public BigDecimal getR12_EQUITIES() {
			return R12_EQUITIES;
		}

		public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
			R12_EQUITIES = r12_EQUITIES;
		}

		public BigDecimal getR12_FOREIGN_EXC_GOLD() {
			return R12_FOREIGN_EXC_GOLD;
		}

		public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
			R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR12_COMMODITIES() {
			return R12_COMMODITIES;
		}

		public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
			R12_COMMODITIES = r12_COMMODITIES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public BigDecimal getR13_INTEREST_RATES() {
			return R13_INTEREST_RATES;
		}

		public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
			R13_INTEREST_RATES = r13_INTEREST_RATES;
		}

		public BigDecimal getR13_EQUITIES() {
			return R13_EQUITIES;
		}

		public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
			R13_EQUITIES = r13_EQUITIES;
		}

		public BigDecimal getR13_FOREIGN_EXC_GOLD() {
			return R13_FOREIGN_EXC_GOLD;
		}

		public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
			R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR13_COMMODITIES() {
			return R13_COMMODITIES;
		}

		public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
			R13_COMMODITIES = r13_COMMODITIES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public BigDecimal getR14_INTEREST_RATES() {
			return R14_INTEREST_RATES;
		}

		public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
			R14_INTEREST_RATES = r14_INTEREST_RATES;
		}

		public BigDecimal getR14_EQUITIES() {
			return R14_EQUITIES;
		}

		public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
			R14_EQUITIES = r14_EQUITIES;
		}

		public BigDecimal getR14_FOREIGN_EXC_GOLD() {
			return R14_FOREIGN_EXC_GOLD;
		}

		public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
			R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR14_COMMODITIES() {
			return R14_COMMODITIES;
		}

		public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
			R14_COMMODITIES = r14_COMMODITIES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

	// ROW MAPPER ARCHIVAL SUMMARY

	class M_OPTR_RowMapper_Archival implements RowMapper<M_OPTR_Archival_Summary_Entity> {

		@Override
		public M_OPTR_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OPTR_Archival_Summary_Entity obj = new M_OPTR_Archival_Summary_Entity();

			obj.setR10_INTEREST_RATES(rs.getBigDecimal("R10_INTEREST_RATES"));
			obj.setR10_EQUITIES(rs.getBigDecimal("R10_EQUITIES"));
			obj.setR10_FOREIGN_EXC_GOLD(rs.getBigDecimal("R10_FOREIGN_EXC_GOLD"));
			obj.setR10_COMMODITIES(rs.getBigDecimal("R10_COMMODITIES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			obj.setR11_INTEREST_RATES(rs.getBigDecimal("R11_INTEREST_RATES"));
			obj.setR11_EQUITIES(rs.getBigDecimal("R11_EQUITIES"));
			obj.setR11_FOREIGN_EXC_GOLD(rs.getBigDecimal("R11_FOREIGN_EXC_GOLD"));
			obj.setR11_COMMODITIES(rs.getBigDecimal("R11_COMMODITIES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			obj.setR12_INTEREST_RATES(rs.getBigDecimal("R12_INTEREST_RATES"));
			obj.setR12_EQUITIES(rs.getBigDecimal("R12_EQUITIES"));
			obj.setR12_FOREIGN_EXC_GOLD(rs.getBigDecimal("R12_FOREIGN_EXC_GOLD"));
			obj.setR12_COMMODITIES(rs.getBigDecimal("R12_COMMODITIES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INTEREST_RATES(rs.getBigDecimal("R13_INTEREST_RATES"));
			obj.setR13_EQUITIES(rs.getBigDecimal("R13_EQUITIES"));
			obj.setR13_FOREIGN_EXC_GOLD(rs.getBigDecimal("R13_FOREIGN_EXC_GOLD"));
			obj.setR13_COMMODITIES(rs.getBigDecimal("R13_COMMODITIES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INTEREST_RATES(rs.getBigDecimal("R14_INTEREST_RATES"));
			obj.setR14_EQUITIES(rs.getBigDecimal("R14_EQUITIES"));
			obj.setR14_FOREIGN_EXC_GOLD(rs.getBigDecimal("R14_FOREIGN_EXC_GOLD"));
			obj.setR14_COMMODITIES(rs.getBigDecimal("R14_COMMODITIES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_OPTR_Archival_Summary_Entity {

		private BigDecimal R10_INTEREST_RATES;
		private BigDecimal R10_EQUITIES;
		private BigDecimal R10_FOREIGN_EXC_GOLD;
		private BigDecimal R10_COMMODITIES;
		private BigDecimal R10_TOTAL;

		private BigDecimal R11_INTEREST_RATES;
		private BigDecimal R11_EQUITIES;
		private BigDecimal R11_FOREIGN_EXC_GOLD;
		private BigDecimal R11_COMMODITIES;
		private BigDecimal R11_TOTAL;

		private BigDecimal R12_INTEREST_RATES;
		private BigDecimal R12_EQUITIES;
		private BigDecimal R12_FOREIGN_EXC_GOLD;
		private BigDecimal R12_COMMODITIES;
		private BigDecimal R12_TOTAL;

		private BigDecimal R13_INTEREST_RATES;
		private BigDecimal R13_EQUITIES;
		private BigDecimal R13_FOREIGN_EXC_GOLD;
		private BigDecimal R13_COMMODITIES;
		private BigDecimal R13_TOTAL;

		private BigDecimal R14_INTEREST_RATES;
		private BigDecimal R14_EQUITIES;
		private BigDecimal R14_FOREIGN_EXC_GOLD;
		private BigDecimal R14_COMMODITIES;
		private BigDecimal R14_TOTAL;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public BigDecimal getR10_INTEREST_RATES() {
			return R10_INTEREST_RATES;
		}

		public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
			R10_INTEREST_RATES = r10_INTEREST_RATES;
		}

		public BigDecimal getR10_EQUITIES() {
			return R10_EQUITIES;
		}

		public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
			R10_EQUITIES = r10_EQUITIES;
		}

		public BigDecimal getR10_FOREIGN_EXC_GOLD() {
			return R10_FOREIGN_EXC_GOLD;
		}

		public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
			R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR10_COMMODITIES() {
			return R10_COMMODITIES;
		}

		public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
			R10_COMMODITIES = r10_COMMODITIES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public BigDecimal getR11_INTEREST_RATES() {
			return R11_INTEREST_RATES;
		}

		public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
			R11_INTEREST_RATES = r11_INTEREST_RATES;
		}

		public BigDecimal getR11_EQUITIES() {
			return R11_EQUITIES;
		}

		public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
			R11_EQUITIES = r11_EQUITIES;
		}

		public BigDecimal getR11_FOREIGN_EXC_GOLD() {
			return R11_FOREIGN_EXC_GOLD;
		}

		public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
			R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR11_COMMODITIES() {
			return R11_COMMODITIES;
		}

		public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
			R11_COMMODITIES = r11_COMMODITIES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public BigDecimal getR12_INTEREST_RATES() {
			return R12_INTEREST_RATES;
		}

		public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
			R12_INTEREST_RATES = r12_INTEREST_RATES;
		}

		public BigDecimal getR12_EQUITIES() {
			return R12_EQUITIES;
		}

		public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
			R12_EQUITIES = r12_EQUITIES;
		}

		public BigDecimal getR12_FOREIGN_EXC_GOLD() {
			return R12_FOREIGN_EXC_GOLD;
		}

		public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
			R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR12_COMMODITIES() {
			return R12_COMMODITIES;
		}

		public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
			R12_COMMODITIES = r12_COMMODITIES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public BigDecimal getR13_INTEREST_RATES() {
			return R13_INTEREST_RATES;
		}

		public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
			R13_INTEREST_RATES = r13_INTEREST_RATES;
		}

		public BigDecimal getR13_EQUITIES() {
			return R13_EQUITIES;
		}

		public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
			R13_EQUITIES = r13_EQUITIES;
		}

		public BigDecimal getR13_FOREIGN_EXC_GOLD() {
			return R13_FOREIGN_EXC_GOLD;
		}

		public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
			R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR13_COMMODITIES() {
			return R13_COMMODITIES;
		}

		public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
			R13_COMMODITIES = r13_COMMODITIES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public BigDecimal getR14_INTEREST_RATES() {
			return R14_INTEREST_RATES;
		}

		public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
			R14_INTEREST_RATES = r14_INTEREST_RATES;
		}

		public BigDecimal getR14_EQUITIES() {
			return R14_EQUITIES;
		}

		public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
			R14_EQUITIES = r14_EQUITIES;
		}

		public BigDecimal getR14_FOREIGN_EXC_GOLD() {
			return R14_FOREIGN_EXC_GOLD;
		}

		public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
			R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR14_COMMODITIES() {
			return R14_COMMODITIES;
		}

		public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
			R14_COMMODITIES = r14_COMMODITIES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

	// ROW MAPPER ARCHIVAL DETAIL

	class M_OPTRRowMapper_ArchivalDetail implements RowMapper<M_OPTR_Archival_Detail_Entity> {

		@Override
		public M_OPTR_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OPTR_Archival_Detail_Entity obj = new M_OPTR_Archival_Detail_Entity();

			obj.setR10_INTEREST_RATES(rs.getBigDecimal("R10_INTEREST_RATES"));
			obj.setR10_EQUITIES(rs.getBigDecimal("R10_EQUITIES"));
			obj.setR10_FOREIGN_EXC_GOLD(rs.getBigDecimal("R10_FOREIGN_EXC_GOLD"));
			obj.setR10_COMMODITIES(rs.getBigDecimal("R10_COMMODITIES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			obj.setR11_INTEREST_RATES(rs.getBigDecimal("R11_INTEREST_RATES"));
			obj.setR11_EQUITIES(rs.getBigDecimal("R11_EQUITIES"));
			obj.setR11_FOREIGN_EXC_GOLD(rs.getBigDecimal("R11_FOREIGN_EXC_GOLD"));
			obj.setR11_COMMODITIES(rs.getBigDecimal("R11_COMMODITIES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			obj.setR12_INTEREST_RATES(rs.getBigDecimal("R12_INTEREST_RATES"));
			obj.setR12_EQUITIES(rs.getBigDecimal("R12_EQUITIES"));
			obj.setR12_FOREIGN_EXC_GOLD(rs.getBigDecimal("R12_FOREIGN_EXC_GOLD"));
			obj.setR12_COMMODITIES(rs.getBigDecimal("R12_COMMODITIES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INTEREST_RATES(rs.getBigDecimal("R13_INTEREST_RATES"));
			obj.setR13_EQUITIES(rs.getBigDecimal("R13_EQUITIES"));
			obj.setR13_FOREIGN_EXC_GOLD(rs.getBigDecimal("R13_FOREIGN_EXC_GOLD"));
			obj.setR13_COMMODITIES(rs.getBigDecimal("R13_COMMODITIES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INTEREST_RATES(rs.getBigDecimal("R14_INTEREST_RATES"));
			obj.setR14_EQUITIES(rs.getBigDecimal("R14_EQUITIES"));
			obj.setR14_FOREIGN_EXC_GOLD(rs.getBigDecimal("R14_FOREIGN_EXC_GOLD"));
			obj.setR14_COMMODITIES(rs.getBigDecimal("R14_COMMODITIES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_OPTR_Archival_Detail_Entity {

		private BigDecimal R10_INTEREST_RATES;
		private BigDecimal R10_EQUITIES;
		private BigDecimal R10_FOREIGN_EXC_GOLD;
		private BigDecimal R10_COMMODITIES;
		private BigDecimal R10_TOTAL;

		private BigDecimal R11_INTEREST_RATES;
		private BigDecimal R11_EQUITIES;
		private BigDecimal R11_FOREIGN_EXC_GOLD;
		private BigDecimal R11_COMMODITIES;
		private BigDecimal R11_TOTAL;

		private BigDecimal R12_INTEREST_RATES;
		private BigDecimal R12_EQUITIES;
		private BigDecimal R12_FOREIGN_EXC_GOLD;
		private BigDecimal R12_COMMODITIES;
		private BigDecimal R12_TOTAL;

		private BigDecimal R13_INTEREST_RATES;
		private BigDecimal R13_EQUITIES;
		private BigDecimal R13_FOREIGN_EXC_GOLD;
		private BigDecimal R13_COMMODITIES;
		private BigDecimal R13_TOTAL;

		private BigDecimal R14_INTEREST_RATES;
		private BigDecimal R14_EQUITIES;
		private BigDecimal R14_FOREIGN_EXC_GOLD;
		private BigDecimal R14_COMMODITIES;
		private BigDecimal R14_TOTAL;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public BigDecimal getR10_INTEREST_RATES() {
			return R10_INTEREST_RATES;
		}

		public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
			R10_INTEREST_RATES = r10_INTEREST_RATES;
		}

		public BigDecimal getR10_EQUITIES() {
			return R10_EQUITIES;
		}

		public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
			R10_EQUITIES = r10_EQUITIES;
		}

		public BigDecimal getR10_FOREIGN_EXC_GOLD() {
			return R10_FOREIGN_EXC_GOLD;
		}

		public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
			R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR10_COMMODITIES() {
			return R10_COMMODITIES;
		}

		public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
			R10_COMMODITIES = r10_COMMODITIES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public BigDecimal getR11_INTEREST_RATES() {
			return R11_INTEREST_RATES;
		}

		public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
			R11_INTEREST_RATES = r11_INTEREST_RATES;
		}

		public BigDecimal getR11_EQUITIES() {
			return R11_EQUITIES;
		}

		public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
			R11_EQUITIES = r11_EQUITIES;
		}

		public BigDecimal getR11_FOREIGN_EXC_GOLD() {
			return R11_FOREIGN_EXC_GOLD;
		}

		public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
			R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR11_COMMODITIES() {
			return R11_COMMODITIES;
		}

		public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
			R11_COMMODITIES = r11_COMMODITIES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public BigDecimal getR12_INTEREST_RATES() {
			return R12_INTEREST_RATES;
		}

		public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
			R12_INTEREST_RATES = r12_INTEREST_RATES;
		}

		public BigDecimal getR12_EQUITIES() {
			return R12_EQUITIES;
		}

		public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
			R12_EQUITIES = r12_EQUITIES;
		}

		public BigDecimal getR12_FOREIGN_EXC_GOLD() {
			return R12_FOREIGN_EXC_GOLD;
		}

		public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
			R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR12_COMMODITIES() {
			return R12_COMMODITIES;
		}

		public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
			R12_COMMODITIES = r12_COMMODITIES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public BigDecimal getR13_INTEREST_RATES() {
			return R13_INTEREST_RATES;
		}

		public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
			R13_INTEREST_RATES = r13_INTEREST_RATES;
		}

		public BigDecimal getR13_EQUITIES() {
			return R13_EQUITIES;
		}

		public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
			R13_EQUITIES = r13_EQUITIES;
		}

		public BigDecimal getR13_FOREIGN_EXC_GOLD() {
			return R13_FOREIGN_EXC_GOLD;
		}

		public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
			R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR13_COMMODITIES() {
			return R13_COMMODITIES;
		}

		public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
			R13_COMMODITIES = r13_COMMODITIES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public BigDecimal getR14_INTEREST_RATES() {
			return R14_INTEREST_RATES;
		}

		public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
			R14_INTEREST_RATES = r14_INTEREST_RATES;
		}

		public BigDecimal getR14_EQUITIES() {
			return R14_EQUITIES;
		}

		public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
			R14_EQUITIES = r14_EQUITIES;
		}

		public BigDecimal getR14_FOREIGN_EXC_GOLD() {
			return R14_FOREIGN_EXC_GOLD;
		}

		public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
			R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR14_COMMODITIES() {
			return R14_COMMODITIES;
		}

		public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
			R14_COMMODITIES = r14_COMMODITIES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

	// ROW MAPPER RESUB SUMMARY

	class M_OPTR_RowMapper_Resub implements RowMapper<M_OPTR_RESUB_Summary_Entity> {

		@Override
		public M_OPTR_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OPTR_RESUB_Summary_Entity obj = new M_OPTR_RESUB_Summary_Entity();

			obj.setR10_INTEREST_RATES(rs.getBigDecimal("R10_INTEREST_RATES"));
			obj.setR10_EQUITIES(rs.getBigDecimal("R10_EQUITIES"));
			obj.setR10_FOREIGN_EXC_GOLD(rs.getBigDecimal("R10_FOREIGN_EXC_GOLD"));
			obj.setR10_COMMODITIES(rs.getBigDecimal("R10_COMMODITIES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			obj.setR11_INTEREST_RATES(rs.getBigDecimal("R11_INTEREST_RATES"));
			obj.setR11_EQUITIES(rs.getBigDecimal("R11_EQUITIES"));
			obj.setR11_FOREIGN_EXC_GOLD(rs.getBigDecimal("R11_FOREIGN_EXC_GOLD"));
			obj.setR11_COMMODITIES(rs.getBigDecimal("R11_COMMODITIES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			obj.setR12_INTEREST_RATES(rs.getBigDecimal("R12_INTEREST_RATES"));
			obj.setR12_EQUITIES(rs.getBigDecimal("R12_EQUITIES"));
			obj.setR12_FOREIGN_EXC_GOLD(rs.getBigDecimal("R12_FOREIGN_EXC_GOLD"));
			obj.setR12_COMMODITIES(rs.getBigDecimal("R12_COMMODITIES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INTEREST_RATES(rs.getBigDecimal("R13_INTEREST_RATES"));
			obj.setR13_EQUITIES(rs.getBigDecimal("R13_EQUITIES"));
			obj.setR13_FOREIGN_EXC_GOLD(rs.getBigDecimal("R13_FOREIGN_EXC_GOLD"));
			obj.setR13_COMMODITIES(rs.getBigDecimal("R13_COMMODITIES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INTEREST_RATES(rs.getBigDecimal("R14_INTEREST_RATES"));
			obj.setR14_EQUITIES(rs.getBigDecimal("R14_EQUITIES"));
			obj.setR14_FOREIGN_EXC_GOLD(rs.getBigDecimal("R14_FOREIGN_EXC_GOLD"));
			obj.setR14_COMMODITIES(rs.getBigDecimal("R14_COMMODITIES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_OPTR_RESUB_Summary_Entity {

		private BigDecimal R10_INTEREST_RATES;
		private BigDecimal R10_EQUITIES;
		private BigDecimal R10_FOREIGN_EXC_GOLD;
		private BigDecimal R10_COMMODITIES;
		private BigDecimal R10_TOTAL;

		private BigDecimal R11_INTEREST_RATES;
		private BigDecimal R11_EQUITIES;
		private BigDecimal R11_FOREIGN_EXC_GOLD;
		private BigDecimal R11_COMMODITIES;
		private BigDecimal R11_TOTAL;

		private BigDecimal R12_INTEREST_RATES;
		private BigDecimal R12_EQUITIES;
		private BigDecimal R12_FOREIGN_EXC_GOLD;
		private BigDecimal R12_COMMODITIES;
		private BigDecimal R12_TOTAL;

		private BigDecimal R13_INTEREST_RATES;
		private BigDecimal R13_EQUITIES;
		private BigDecimal R13_FOREIGN_EXC_GOLD;
		private BigDecimal R13_COMMODITIES;
		private BigDecimal R13_TOTAL;

		private BigDecimal R14_INTEREST_RATES;
		private BigDecimal R14_EQUITIES;
		private BigDecimal R14_FOREIGN_EXC_GOLD;
		private BigDecimal R14_COMMODITIES;
		private BigDecimal R14_TOTAL;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public BigDecimal getR10_INTEREST_RATES() {
			return R10_INTEREST_RATES;
		}

		public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
			R10_INTEREST_RATES = r10_INTEREST_RATES;
		}

		public BigDecimal getR10_EQUITIES() {
			return R10_EQUITIES;
		}

		public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
			R10_EQUITIES = r10_EQUITIES;
		}

		public BigDecimal getR10_FOREIGN_EXC_GOLD() {
			return R10_FOREIGN_EXC_GOLD;
		}

		public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
			R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR10_COMMODITIES() {
			return R10_COMMODITIES;
		}

		public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
			R10_COMMODITIES = r10_COMMODITIES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public BigDecimal getR11_INTEREST_RATES() {
			return R11_INTEREST_RATES;
		}

		public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
			R11_INTEREST_RATES = r11_INTEREST_RATES;
		}

		public BigDecimal getR11_EQUITIES() {
			return R11_EQUITIES;
		}

		public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
			R11_EQUITIES = r11_EQUITIES;
		}

		public BigDecimal getR11_FOREIGN_EXC_GOLD() {
			return R11_FOREIGN_EXC_GOLD;
		}

		public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
			R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR11_COMMODITIES() {
			return R11_COMMODITIES;
		}

		public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
			R11_COMMODITIES = r11_COMMODITIES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public BigDecimal getR12_INTEREST_RATES() {
			return R12_INTEREST_RATES;
		}

		public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
			R12_INTEREST_RATES = r12_INTEREST_RATES;
		}

		public BigDecimal getR12_EQUITIES() {
			return R12_EQUITIES;
		}

		public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
			R12_EQUITIES = r12_EQUITIES;
		}

		public BigDecimal getR12_FOREIGN_EXC_GOLD() {
			return R12_FOREIGN_EXC_GOLD;
		}

		public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
			R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR12_COMMODITIES() {
			return R12_COMMODITIES;
		}

		public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
			R12_COMMODITIES = r12_COMMODITIES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public BigDecimal getR13_INTEREST_RATES() {
			return R13_INTEREST_RATES;
		}

		public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
			R13_INTEREST_RATES = r13_INTEREST_RATES;
		}

		public BigDecimal getR13_EQUITIES() {
			return R13_EQUITIES;
		}

		public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
			R13_EQUITIES = r13_EQUITIES;
		}

		public BigDecimal getR13_FOREIGN_EXC_GOLD() {
			return R13_FOREIGN_EXC_GOLD;
		}

		public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
			R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR13_COMMODITIES() {
			return R13_COMMODITIES;
		}

		public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
			R13_COMMODITIES = r13_COMMODITIES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public BigDecimal getR14_INTEREST_RATES() {
			return R14_INTEREST_RATES;
		}

		public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
			R14_INTEREST_RATES = r14_INTEREST_RATES;
		}

		public BigDecimal getR14_EQUITIES() {
			return R14_EQUITIES;
		}

		public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
			R14_EQUITIES = r14_EQUITIES;
		}

		public BigDecimal getR14_FOREIGN_EXC_GOLD() {
			return R14_FOREIGN_EXC_GOLD;
		}

		public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
			R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR14_COMMODITIES() {
			return R14_COMMODITIES;
		}

		public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
			R14_COMMODITIES = r14_COMMODITIES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

	// ROW MAPPER RESUB DETAIL

	class M_OPTRRowMapper_ResubDetail implements RowMapper<M_OPTR_RESUB_Detail_Entity> {

		@Override
		public M_OPTR_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OPTR_RESUB_Detail_Entity obj = new M_OPTR_RESUB_Detail_Entity();

			obj.setR10_INTEREST_RATES(rs.getBigDecimal("R10_INTEREST_RATES"));
			obj.setR10_EQUITIES(rs.getBigDecimal("R10_EQUITIES"));
			obj.setR10_FOREIGN_EXC_GOLD(rs.getBigDecimal("R10_FOREIGN_EXC_GOLD"));
			obj.setR10_COMMODITIES(rs.getBigDecimal("R10_COMMODITIES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			obj.setR11_INTEREST_RATES(rs.getBigDecimal("R11_INTEREST_RATES"));
			obj.setR11_EQUITIES(rs.getBigDecimal("R11_EQUITIES"));
			obj.setR11_FOREIGN_EXC_GOLD(rs.getBigDecimal("R11_FOREIGN_EXC_GOLD"));
			obj.setR11_COMMODITIES(rs.getBigDecimal("R11_COMMODITIES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			obj.setR12_INTEREST_RATES(rs.getBigDecimal("R12_INTEREST_RATES"));
			obj.setR12_EQUITIES(rs.getBigDecimal("R12_EQUITIES"));
			obj.setR12_FOREIGN_EXC_GOLD(rs.getBigDecimal("R12_FOREIGN_EXC_GOLD"));
			obj.setR12_COMMODITIES(rs.getBigDecimal("R12_COMMODITIES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INTEREST_RATES(rs.getBigDecimal("R13_INTEREST_RATES"));
			obj.setR13_EQUITIES(rs.getBigDecimal("R13_EQUITIES"));
			obj.setR13_FOREIGN_EXC_GOLD(rs.getBigDecimal("R13_FOREIGN_EXC_GOLD"));
			obj.setR13_COMMODITIES(rs.getBigDecimal("R13_COMMODITIES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INTEREST_RATES(rs.getBigDecimal("R14_INTEREST_RATES"));
			obj.setR14_EQUITIES(rs.getBigDecimal("R14_EQUITIES"));
			obj.setR14_FOREIGN_EXC_GOLD(rs.getBigDecimal("R14_FOREIGN_EXC_GOLD"));
			obj.setR14_COMMODITIES(rs.getBigDecimal("R14_COMMODITIES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class M_OPTR_RESUB_Detail_Entity {

		private BigDecimal R10_INTEREST_RATES;
		private BigDecimal R10_EQUITIES;
		private BigDecimal R10_FOREIGN_EXC_GOLD;
		private BigDecimal R10_COMMODITIES;
		private BigDecimal R10_TOTAL;

		private BigDecimal R11_INTEREST_RATES;
		private BigDecimal R11_EQUITIES;
		private BigDecimal R11_FOREIGN_EXC_GOLD;
		private BigDecimal R11_COMMODITIES;
		private BigDecimal R11_TOTAL;

		private BigDecimal R12_INTEREST_RATES;
		private BigDecimal R12_EQUITIES;
		private BigDecimal R12_FOREIGN_EXC_GOLD;
		private BigDecimal R12_COMMODITIES;
		private BigDecimal R12_TOTAL;

		private BigDecimal R13_INTEREST_RATES;
		private BigDecimal R13_EQUITIES;
		private BigDecimal R13_FOREIGN_EXC_GOLD;
		private BigDecimal R13_COMMODITIES;
		private BigDecimal R13_TOTAL;

		private BigDecimal R14_INTEREST_RATES;
		private BigDecimal R14_EQUITIES;
		private BigDecimal R14_FOREIGN_EXC_GOLD;
		private BigDecimal R14_COMMODITIES;
		private BigDecimal R14_TOTAL;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "REPORT_VERSION", length = 100)
		private BigDecimal REPORT_VERSION;

		@Column(name = "REPORT_RESUBDATE")
		private Date REPORT_RESUBDATE;

		@Column(name = "REPORT_FREQUENCY", length = 100)
		private String REPORT_FREQUENCY;

		@Column(name = "REPORT_CODE", length = 100)
		private String REPORT_CODE;

		@Column(name = "REPORT_DESC", length = 100)
		private String REPORT_DESC;

		@Column(name = "ENTITY_FLG", length = 1)
		private String ENTITY_FLG;

		@Column(name = "MODIFY_FLG", length = 1)
		private String MODIFY_FLG;

		@Column(name = "DEL_FLG", length = 1)
		private String DEL_FLG;

		public BigDecimal getR10_INTEREST_RATES() {
			return R10_INTEREST_RATES;
		}

		public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
			R10_INTEREST_RATES = r10_INTEREST_RATES;
		}

		public BigDecimal getR10_EQUITIES() {
			return R10_EQUITIES;
		}

		public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
			R10_EQUITIES = r10_EQUITIES;
		}

		public BigDecimal getR10_FOREIGN_EXC_GOLD() {
			return R10_FOREIGN_EXC_GOLD;
		}

		public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
			R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR10_COMMODITIES() {
			return R10_COMMODITIES;
		}

		public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
			R10_COMMODITIES = r10_COMMODITIES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public BigDecimal getR11_INTEREST_RATES() {
			return R11_INTEREST_RATES;
		}

		public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
			R11_INTEREST_RATES = r11_INTEREST_RATES;
		}

		public BigDecimal getR11_EQUITIES() {
			return R11_EQUITIES;
		}

		public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
			R11_EQUITIES = r11_EQUITIES;
		}

		public BigDecimal getR11_FOREIGN_EXC_GOLD() {
			return R11_FOREIGN_EXC_GOLD;
		}

		public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
			R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR11_COMMODITIES() {
			return R11_COMMODITIES;
		}

		public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
			R11_COMMODITIES = r11_COMMODITIES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public BigDecimal getR12_INTEREST_RATES() {
			return R12_INTEREST_RATES;
		}

		public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
			R12_INTEREST_RATES = r12_INTEREST_RATES;
		}

		public BigDecimal getR12_EQUITIES() {
			return R12_EQUITIES;
		}

		public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
			R12_EQUITIES = r12_EQUITIES;
		}

		public BigDecimal getR12_FOREIGN_EXC_GOLD() {
			return R12_FOREIGN_EXC_GOLD;
		}

		public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
			R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR12_COMMODITIES() {
			return R12_COMMODITIES;
		}

		public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
			R12_COMMODITIES = r12_COMMODITIES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public BigDecimal getR13_INTEREST_RATES() {
			return R13_INTEREST_RATES;
		}

		public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
			R13_INTEREST_RATES = r13_INTEREST_RATES;
		}

		public BigDecimal getR13_EQUITIES() {
			return R13_EQUITIES;
		}

		public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
			R13_EQUITIES = r13_EQUITIES;
		}

		public BigDecimal getR13_FOREIGN_EXC_GOLD() {
			return R13_FOREIGN_EXC_GOLD;
		}

		public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
			R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR13_COMMODITIES() {
			return R13_COMMODITIES;
		}

		public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
			R13_COMMODITIES = r13_COMMODITIES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public BigDecimal getR14_INTEREST_RATES() {
			return R14_INTEREST_RATES;
		}

		public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
			R14_INTEREST_RATES = r14_INTEREST_RATES;
		}

		public BigDecimal getR14_EQUITIES() {
			return R14_EQUITIES;
		}

		public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
			R14_EQUITIES = r14_EQUITIES;
		}

		public BigDecimal getR14_FOREIGN_EXC_GOLD() {
			return R14_FOREIGN_EXC_GOLD;
		}

		public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
			R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
		}

		public BigDecimal getR14_COMMODITIES() {
			return R14_COMMODITIES;
		}

		public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
			R14_COMMODITIES = r14_COMMODITIES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			this.REPORT_VERSION = REPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
			this.REPORT_RESUBDATE = REPORT_RESUBDATE;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

	}

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_OPTRView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("dtltype...." + dtltype);
		System.out.println("type...." + type);

		try {

			// Parse only once
			Date dt = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + dt);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_OPTR_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dt, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_OPTR_RESUB_Summary_Entity> T1Master = getdatabydateListresub1(dt, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_OPTR_Summary_Entity> T1Master = getDataByDate1(dt);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_OPTR_Archival_Detail_Entity> T1Master = getdatabydateListArchivalDetail1(dt, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_OPTR_RESUB_Detail_Entity> T1Master = getdatabydateListResubDetail1(dt, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_OPTR_Detail_Entity> T1Master = getDetaildatabydateList1(dt);
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OPTR");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	@Transactional
	public void updateReport(M_OPTR_Summary_Entity request1) {

	    try {

	        logger.info("Came to services");
	        logger.info("Report Date: {}", request1.getREPORT_DATE());

	        // Fetch existing record
	        List<M_OPTR_Summary_Entity> records =
	        		 getDataByDate1(request1.getREPORT_DATE());
	        
	        if (records == null || records.isEmpty()) {

	            throw new RuntimeException(
	                    "Record not found for REPORT_DATE : "
	                            + request1.getREPORT_DATE());
	        }

	        M_OPTR_Summary_Entity existing = records.get(0);
	        // Audit old copy
	        M_OPTR_Summary_Entity oldcopy = new M_OPTR_Summary_Entity();
	        BeanUtils.copyProperties(existing, oldcopy);

	        String changes = auditService.getChanges(oldcopy, request1);

	        if (!changes.isEmpty()) {

	            String sql =
	                    "UPDATE BRRS_M_OPTR_SUMMARYTABLE SET " +
	                    "R10_INTEREST_RATES=?, R10_EQUITIES=?, R10_FOREIGN_EXC_GOLD=?, R10_COMMODITIES=?, R10_TOTAL=?, " +
	                    "R11_INTEREST_RATES=?, R11_EQUITIES=?, R11_FOREIGN_EXC_GOLD=?, R11_COMMODITIES=?, R11_TOTAL=?, " +
	                    "R12_INTEREST_RATES=?, R12_EQUITIES=?, R12_FOREIGN_EXC_GOLD=?, R12_COMMODITIES=?, R12_TOTAL=?, " +
	                    "R13_INTEREST_RATES=?, R13_EQUITIES=?, R13_FOREIGN_EXC_GOLD=?, R13_COMMODITIES=?, R13_TOTAL=?, " +
	                    "R14_INTEREST_RATES=?, R14_EQUITIES=?, R14_FOREIGN_EXC_GOLD=?, R14_COMMODITIES=?, R14_TOTAL=? " +
	                    "WHERE REPORT_DATE=?";

	            int count = jdbcTemplate.update(

	                    sql,

	                    request1.getR10_INTEREST_RATES(),
	                    request1.getR10_EQUITIES(),
	                    request1.getR10_FOREIGN_EXC_GOLD(),
	                    request1.getR10_COMMODITIES(),
	                    request1.getR10_TOTAL(),

	                    request1.getR11_INTEREST_RATES(),
	                    request1.getR11_EQUITIES(),
	                    request1.getR11_FOREIGN_EXC_GOLD(),
	                    request1.getR11_COMMODITIES(),
	                    request1.getR11_TOTAL(),

	                    request1.getR12_INTEREST_RATES(),
	                    request1.getR12_EQUITIES(),
	                    request1.getR12_FOREIGN_EXC_GOLD(),
	                    request1.getR12_COMMODITIES(),
	                    request1.getR12_TOTAL(),

	                    request1.getR13_INTEREST_RATES(),
	                    request1.getR13_EQUITIES(),
	                    request1.getR13_FOREIGN_EXC_GOLD(),
	                    request1.getR13_COMMODITIES(),
	                    request1.getR13_TOTAL(),

	                    request1.getR14_INTEREST_RATES(),
	                    request1.getR14_EQUITIES(),
	                    request1.getR14_FOREIGN_EXC_GOLD(),
	                    request1.getR14_COMMODITIES(),
	                    request1.getR14_TOTAL(),

	                    request1.getREPORT_DATE()
	            );

	            if (count > 0) {

	                auditService.compareEntitiesmanual(
	                        oldcopy,
	                        request1,
	                        request1.getREPORT_DATE().toString(),
	                        "M OPTR Summary Screen",
	                        "BRRS_M_OPTR_SUMMARY"
	                );

	                logger.info(
	                        "Audit completed for REPORT_DATE {}",
	                        request1.getREPORT_DATE());

	                logger.info(
	                        "M_OPTR Summary Updated Successfully. Rows Updated: {}",
	                        count);
	            }

	        } else {

	            logger.info(
	                    "No changes detected for REPORT_DATE {}",
	                    request1.getREPORT_DATE());
	        }

	    } catch (Exception e) {

	        logger.error(
	                "Error while updating BRRS_M_OPTR Report",
	                e);

	        throw new RuntimeException(
	                "Error while updating BRRS_M_OPTR Report",
	                e);
	    }
	}

	private Object getValue(Object obj, String methodName) {
	    try {
	        return obj.getClass().getMethod(methodName).invoke(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	
	@Transactional
	public void updateResubReport(M_OPTR_RESUB_Summary_Entity updatedEntity) {

	    // ====================================================
	    // 1. GET REPORT DATE
	    // ====================================================

	    Date reportDate = updatedEntity.getREPORT_DATE();

	    if (reportDate == null) {
	        throw new RuntimeException("Report date cannot be null");
	    }

	    // ====================================================
	    // 2. FETCH MAX VERSION
	    // ====================================================

	    BigDecimal maxVersion = RESUBfindMaxVersion1(reportDate);

	    if (maxVersion == null) {
	        maxVersion = BigDecimal.ZERO;
	    }

	    BigDecimal newVersion = maxVersion.add(BigDecimal.ONE);

	    Date now = new Date();

	    // ====================================================
	    // 3. RESUB SUMMARY
	    // ====================================================

	    updatedEntity.setREPORT_VERSION(newVersion);
	    updatedEntity.setREPORT_RESUBDATE(now);

	    insertResubSummary(updatedEntity);

	    // ====================================================
	    // 4. RESUB DETAIL
	    // ====================================================

	    M_OPTR_RESUB_Detail_Entity detailEntity =
	            new M_OPTR_RESUB_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity, detailEntity);

	    detailEntity.setREPORT_DATE(reportDate);
	    detailEntity.setREPORT_VERSION(newVersion);
	    detailEntity.setREPORT_RESUBDATE(now);

	    insertResubDetail(detailEntity);

	    // ====================================================
	    // 5. ARCHIVAL SUMMARY
	    // ====================================================

	    M_OPTR_Archival_Summary_Entity archivalSummary =
	            new M_OPTR_Archival_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity, archivalSummary);

	    archivalSummary.setREPORT_DATE(reportDate);
	    archivalSummary.setREPORT_VERSION(newVersion);
	    archivalSummary.setREPORT_RESUBDATE(now);

	    insertArchivalSummary(archivalSummary);

	    // ====================================================
	    // 6. ARCHIVAL DETAIL
	    // ====================================================

	    M_OPTR_Archival_Detail_Entity archivalDetail =
	            new M_OPTR_Archival_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity, archivalDetail);

	    archivalDetail.setREPORT_DATE(reportDate);
	    archivalDetail.setREPORT_VERSION(newVersion);
	    archivalDetail.setREPORT_RESUBDATE(now);

	    insertArchivalDetail(archivalDetail);

	    System.out.println("Resubmission Version Created : " + newVersion);
	}
	
	private void insertResubSummary(M_OPTR_RESUB_Summary_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_OPTR_RESUB_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 10; i <= 14; i++) {

	            columns.append("R").append(i).append("_INTEREST_RATES,")
	                   .append("R").append(i).append("_EQUITIES,")
	                   .append("R").append(i).append("_FOREIGN_EXC_GOLD,")
	                   .append("R").append(i).append("_COMMODITIES,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,?,?,?,");

	            params.add(getValue(entity, "getR" + i + "_INTEREST_RATES"));
	            params.add(getValue(entity, "getR" + i + "_EQUITIES"));
	            params.add(getValue(entity, "getR" + i + "_FOREIGN_EXC_GOLD"));
	            params.add(getValue(entity, "getR" + i + "_COMMODITIES"));
	            params.add(getValue(entity, "getR" + i + "_TOTAL"));
	        }

	        columns.deleteCharAt(columns.length() - 1);
	        values.deleteCharAt(values.length() - 1);

	        columns.append(")");
	        values.append(")");

	        jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error inserting RESUB SUMMARY", e);
	    }
	}
	
	
	private void insertResubDetail(M_OPTR_RESUB_Detail_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_OPTR_RESUB_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 10; i <= 14; i++) {

	            columns.append("R").append(i).append("_INTEREST_RATES,")
	                   .append("R").append(i).append("_EQUITIES,")
	                   .append("R").append(i).append("_FOREIGN_EXC_GOLD,")
	                   .append("R").append(i).append("_COMMODITIES,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,?,?,?,");

	            params.add(getValue(entity, "getR" + i + "_INTEREST_RATES"));
	            params.add(getValue(entity, "getR" + i + "_EQUITIES"));
	            params.add(getValue(entity, "getR" + i + "_FOREIGN_EXC_GOLD"));
	            params.add(getValue(entity, "getR" + i + "_COMMODITIES"));
	            params.add(getValue(entity, "getR" + i + "_TOTAL"));
	        }

	        columns.deleteCharAt(columns.length() - 1);
	        values.deleteCharAt(values.length() - 1);

	        columns.append(")");
	        values.append(")");

	        jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error inserting RESUB DETAIL", e);
	    }
	}
	
	private void insertArchivalSummary(M_OPTR_Archival_Summary_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_OPTR_ARCHIVAL_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 10; i <= 14; i++) {

	            columns.append("R").append(i).append("_INTEREST_RATES,")
	                   .append("R").append(i).append("_EQUITIES,")
	                   .append("R").append(i).append("_FOREIGN_EXC_GOLD,")
	                   .append("R").append(i).append("_COMMODITIES,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,?,?,?,");

	            params.add(getValue(entity, "getR" + i + "_INTEREST_RATES"));
	            params.add(getValue(entity, "getR" + i + "_EQUITIES"));
	            params.add(getValue(entity, "getR" + i + "_FOREIGN_EXC_GOLD"));
	            params.add(getValue(entity, "getR" + i + "_COMMODITIES"));
	            params.add(getValue(entity, "getR" + i + "_TOTAL"));
	        }

	        columns.deleteCharAt(columns.length() - 1);
	        values.deleteCharAt(values.length() - 1);

	        columns.append(")");
	        values.append(")");

	        jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error inserting ARCHIVAL SUMMARY", e);
	    }
	}
	
	private void insertArchivalDetail(M_OPTR_Archival_Detail_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_OPTR_ARCHIVAL_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 10; i <= 14; i++) {

	            columns.append("R").append(i).append("_INTEREST_RATES,")
	                   .append("R").append(i).append("_EQUITIES,")
	                   .append("R").append(i).append("_FOREIGN_EXC_GOLD,")
	                   .append("R").append(i).append("_COMMODITIES,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,?,?,?,");

	            params.add(getValue(entity, "getR" + i + "_INTEREST_RATES"));
	            params.add(getValue(entity, "getR" + i + "_EQUITIES"));
	            params.add(getValue(entity, "getR" + i + "_FOREIGN_EXC_GOLD"));
	            params.add(getValue(entity, "getR" + i + "_COMMODITIES"));
	            params.add(getValue(entity, "getR" + i + "_TOTAL"));
	        }

	        columns.deleteCharAt(columns.length() - 1);
	        values.deleteCharAt(values.length() - 1);

	        columns.append(")");
	        values.append(")");

	        jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error inserting ARCHIVAL DETAIL", e);
	    }
	}

	public List<Object[]> getM_OPTRResub() {

	    List<Object[]> resubList = new ArrayList<>();

	    try {

	        List<M_OPTR_Archival_Summary_Entity> latestArchivalList = getdatabydateListWithVersion1();


	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {

	            for (M_OPTR_Archival_Summary_Entity entity : latestArchivalList) {

	                resubList.add(new Object[] {
	                		 entity.getREPORT_DATE(),
		                        entity.getREPORT_VERSION(),
		                        entity.getREPORT_RESUBDATE()
	                });
	            }

	            System.out.println("Fetched " + resubList.size() + " record(s)");

	        } else {

	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {

	        System.err.println("Error fetching M_OPTR Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return resubList;
	}
	
	public List<Object[]> getM_OPTRArchival() {

	    String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
	            + "FROM BRRS_M_OPTR_ARCHIVAL_SUMMARY "
	            + "ORDER BY REPORT_VERSION";

	    return jdbcTemplate.query(sql,
	            (rs, rowNum) -> new Object[] {
	                    rs.getDate("REPORT_DATE"),
	                    rs.getBigDecimal("REPORT_VERSION"),
	                    rs.getDate("REPORT_RESUBDATE")
	            });
	}
	
	// Normal format Excel

	public byte[] getBRRS_M_OPTRExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_OPTRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OPTRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OPTREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_OPTR_Summary_Entity> dataList = getDataByDate1(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
							M_OPTR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							// REPORT_DATE
							row = sheet.getRow(5);
							Cell cell1 = row.getCell(1);
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

							row = sheet.getRow(9);
							Cell cell2 = row.createCell(4);
							if (record.getR10_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(5);
							if (record.getR10_EQUITIES() != null) {
								cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(6);
							if (record.getR10_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(7);
							if (record.getR10_COMMODITIES() != null) {
								cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(10);

							cell2 = row.createCell(4);
							if (record.getR11_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(5);
							if (record.getR11_EQUITIES() != null) {
								cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(6);
							if (record.getR11_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(7);
							if (record.getR11_COMMODITIES() != null) {
								cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);
							cell2 = row.createCell(4);
							if (record.getR13_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(5);
							if (record.getR13_EQUITIES() != null) {
								cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(6);
							if (record.getR13_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(7);
							if (record.getR13_COMMODITIES() != null) {
								cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);
							cell2 = row.createCell(4);
							if (record.getR14_INTEREST_RATES() != null) {
								cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(5);
							if (record.getR14_EQUITIES() != null) {
								cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(6);
							if (record.getR14_FOREIGN_EXC_GOLD() != null) {
								cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(7);
							if (record.getR14_COMMODITIES() != null) {
								cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OPTR SUMMARY", null,
								"BRRS_M_OPTR_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_OPTREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OPTRArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OPTRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_OPTR_Summary_Entity> dataList = getDataByDate1(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
						M_OPTR_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// REPORT_DATE
						row = sheet.getRow(5);
						Cell cell1 = row.getCell(1);
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

						/*
						 * Cell cell2 = row.createCell(4); if (record.getR10_INTEREST_RATES() != null) {
						 * cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
						 * cell2.setCellStyle(numberStyle); } else { cell2.setCellValue("");
						 * cell2.setCellStyle(textStyle); }
						 * 
						 * Cell cell3 = row.createCell(5); if (record.getR10_EQUITIES() != null) {
						 * cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
						 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
						 * cell3.setCellStyle(textStyle); }
						 * 
						 * Cell cell4 = row.createCell(6); if (record.getR10_FOREIGN_EXC_GOLD() != null)
						 * { cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
						 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
						 * cell4.setCellStyle(textStyle); }
						 * 
						 * Cell cell5 = row.createCell(7); if (record.getR10_COMMODITIES() != null) {
						 * cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
						 * cell5.setCellStyle(numberStyle); } else { cell5.setCellValue("");
						 * cell5.setCellStyle(textStyle); }
						 */
						row = sheet.getRow(10);

						Cell cell2 = row.createCell(4);
						if (record.getR11_INTEREST_RATES() != null) {
							cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(5);
						if (record.getR11_EQUITIES() != null) {
							cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(6);
						if (record.getR11_FOREIGN_EXC_GOLD() != null) {
							cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(7);
						if (record.getR11_COMMODITIES() != null) {
							cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);
						cell2 = row.createCell(4);
						if (record.getR13_INTEREST_RATES() != null) {
							cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(5);
						if (record.getR13_EQUITIES() != null) {
							cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record.getR13_FOREIGN_EXC_GOLD() != null) {
							cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record.getR13_COMMODITIES() != null) {
							cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);
						cell2 = row.createCell(4);
						if (record.getR14_INTEREST_RATES() != null) {
							cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(5);
						if (record.getR14_EQUITIES() != null) {
							cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record.getR14_FOREIGN_EXC_GOLD() != null) {
							cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record.getR14_COMMODITIES() != null) {
							cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OPTR EMAIL SUMMARY", null,
							"BRRS_M_OPTR_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_OPTRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OPTRArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OPTR_Archival_Summary_Entity> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OPTR report. Returning empty result.");
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
					M_OPTR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
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

					Cell cell2 = row.createCell(4);
					if (record.getR10_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR10_EQUITIES() != null) {
						cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR10_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR10_COMMODITIES() != null) {
						cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OPTR ARCHIVAL SUMMARY", null,
						"BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_OPTRArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OPTR_Archival_Summary_Entity> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
					M_OPTR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
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

					/*
					 * Cell cell2 = row.createCell(4); if (record.getR10_INTEREST_RATES() != null) {
					 * cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
					 * cell2.setCellStyle(numberStyle); } else { cell2.setCellValue("");
					 * cell2.setCellStyle(textStyle); }
					 * 
					 * Cell cell3 = row.createCell(5); if (record.getR10_EQUITIES() != null) {
					 * cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 * 
					 * Cell cell4 = row.createCell(6); if (record.getR10_FOREIGN_EXC_GOLD() != null)
					 * { cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 * 
					 * Cell cell5 = row.createCell(7); if (record.getR10_COMMODITIES() != null) {
					 * cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
					 * cell5.setCellStyle(numberStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(10);

					Cell cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OPTR EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_OPTRResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OPTRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OPTR_RESUB_Summary_Entity> dataList = getdatabydateListresub1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OPTR report. Returning empty result.");
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

					M_OPTR_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
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

					row = sheet.getRow(9);
					Cell cell2 = row.createCell(4);
					if (record.getR10_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR10_EQUITIES() != null) {
						cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR10_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR10_COMMODITIES() != null) {
						cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OPTR RESUB SUMMARY", null,
						"BRRS_M_OPTR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_OPTRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OPTR_RESUB_Summary_Entity> dataList = getdatabydateListresub1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OPTR report. Returning empty result.");
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
					M_OPTR_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
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

					/*
					 * Cell cell2 = row.createCell(4); if (record.getR10_INTEREST_RATES() != null) {
					 * cell2.setCellValue(record.getR10_INTEREST_RATES().doubleValue());
					 * cell2.setCellStyle(numberStyle); } else { cell2.setCellValue("");
					 * cell2.setCellStyle(textStyle); }
					 * 
					 * Cell cell3 = row.createCell(5); if (record.getR10_EQUITIES() != null) {
					 * cell3.setCellValue(record.getR10_EQUITIES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 * 
					 * Cell cell4 = row.createCell(6); if (record.getR10_FOREIGN_EXC_GOLD() != null)
					 * { cell4.setCellValue(record.getR10_FOREIGN_EXC_GOLD().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 * 
					 * Cell cell5 = row.createCell(7); if (record.getR10_COMMODITIES() != null) {
					 * cell5.setCellValue(record.getR10_COMMODITIES().doubleValue());
					 * cell5.setCellStyle(numberStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(10);

					Cell cell2 = row.createCell(4);
					if (record.getR11_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR11_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(5);
					if (record.getR11_EQUITIES() != null) {
						cell3.setCellValue(record.getR11_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(6);
					if (record.getR11_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(7);
					if (record.getR11_COMMODITIES() != null) {
						cell5.setCellValue(record.getR11_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell2 = row.createCell(4);
					if (record.getR13_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR13_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR13_EQUITIES() != null) {
						cell3.setCellValue(record.getR13_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR13_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR13_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR13_COMMODITIES() != null) {
						cell5.setCellValue(record.getR13_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(4);
					if (record.getR14_INTEREST_RATES() != null) {
						cell2.setCellValue(record.getR14_INTEREST_RATES().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(5);
					if (record.getR14_EQUITIES() != null) {
						cell3.setCellValue(record.getR14_EQUITIES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record.getR14_FOREIGN_EXC_GOLD() != null) {
						cell4.setCellValue(record.getR14_FOREIGN_EXC_GOLD().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record.getR14_COMMODITIES() != null) {
						cell5.setCellValue(record.getR14_COMMODITIES().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OPTR EMAIL RESUB SUMMARY", null,
						"BRRS_M_OPTR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

}