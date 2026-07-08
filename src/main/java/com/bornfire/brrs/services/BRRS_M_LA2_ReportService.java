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

public class BRRS_M_LA2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA2_ReportService.class);

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
	public List<M_LA2_Summary_Entity> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA2_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA2_RowMapper_Summary());
	}

	// ARCHIVAL

	// Fetch data by report date
	public List<M_LA2_Archival_Summary_Entity> ArchivalgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA2_RowMapper_Archival());
	}

	// RESUB

	// Fetch data by report date
	public List<M_LA2_RESUB_Summary_Entity> ResubgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA2_RowMapper_Resub());
	}

	/*
	 * // ARCHIVAL // GET REPORT_DATE + REPORT_VERSION
	 * 
	 * public List<Object[]> getM_LA2Archival() {
	 * 
	 * String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
	 * "FROM BRRS_M_LA2_ARCHIVALTABLE_SUMMARY" + "ORDER BY REPORT_VERSION";
	 * 
	 * return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] {
	 * rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") }); }
	 */

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_LA2_Archival_Summary_Entity> getdatabydateListarchival1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA2_RowMapper_Archival());
	}

	// GET RESUB FULL DATA BY DATE + VERSION

	public List<M_LA2_RESUB_Summary_Entity> getdatabydateListresub1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA2_RowMapper_Resub());
	}

	// GET DETAIL FULL DATA BY DATE + VERSION

	public List<M_LA2_Detail_Entity> getdatabydateListDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE" + "WHERE REPORT_DATE = ? " + "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA2RowMapper_Detail());
	}

	// GET ARCHIVAL DETAIL FULL DATA BY DATE + VERSION

	public List<M_LA2_Archival_Detail_Entity> getdatabydateListArchivalDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new M_LA2RowMapper_ArchivalDetail());
	}

	// GET RESUB DETAIL FULL DATA BY DATE + VERSION

	public List<M_LA2_RESUB_Detail_Entity> getdatabydateListResubDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA2RowMapper_ResubDetail());
	}

	// GET ALL WITH VERSION

	public List<M_LA2_Archival_Summary_Entity> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA2_RowMapper_Archival());
	}

	// GET RESUB ALL WITH VERSION

	public List<M_LA2_RESUB_Summary_Entity> ResubgetdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA2_RowMapper_Resub());
	}

	// GET ARCHIVAL MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA2_ARCHIVALTABLE_SUMMARY" + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// GET RESUB MAX VERSION BY DATE

	public BigDecimal RESUBfindMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA2_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// DETAIL TABLE 1
	// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA2_Detail_Entity> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA2RowMapper_Detail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_LA2_Detail_Entity> getDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA2RowMapper_Detail());
	}

	// 3. PAGINATION

	public List<M_LA2_Detail_Entity> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA2RowMapper_Detail());
	}

	// 4. COUNT

	public int getDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA2_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_LA2_Detail_Entity> GetDetailDataByRowIdAndColumnId1(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA2RowMapper_Detail());
	}

	// 6. BY ACCOUNT NUMBER

	public M_LA2_Detail_Entity findByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_LA2RowMapper_Detail());
	}

	// ARCHIVALTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA2_Archival_Detail_Entity> findByArchivalDetailReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA2RowMapper_ArchivalDetail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_LA2_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA2RowMapper_ArchivalDetail());
	}

	// 3. PAGINATION

	public List<M_LA2_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA2RowMapper_ArchivalDetail());
	}

	// 4. COUNT

	public int getArchivalDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_LA2_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA2RowMapper_ArchivalDetail());
	}
	// 6. BY ACCOUNT NUMBER

	public M_LA2_Archival_Detail_Entity ArchivalfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_LA2_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_LA2RowMapper_ArchivalDetail());
	}

	// RESUBTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA2_RESUB_Detail_Entity> findByResubReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA2RowMapper_ResubDetail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_LA2_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA2RowMapper_ResubDetail());
	}

	// 3. PAGINATION

	public List<M_LA2_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA2RowMapper_ResubDetail());
	}

	// 4. COUNT

	public int getResubdatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA2_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_LA2_RESUB_Detail_Entity> GetResubDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new M_LA2RowMapper_ResubDetail());
	}
	// 6. BY ACCOUNT NUMBER

	public M_LA2_RESUB_Detail_Entity ResubfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_M_LA2_RESUB_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_LA2RowMapper_ResubDetail());
	}

	// findSummaryByReportDate

	@Transactional(readOnly = true)
	public M_LA2_Summary_Entity findSummaryByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA2_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		List<M_LA2_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_LA2_RowMapper_Summary());

		return list.isEmpty() ? null : list.get(0);
	}

	@Transactional(readOnly = true)
	public M_LA2_Detail_Entity findDetailByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA2_DETAILTABLE " + "WHERE REPORT_DATE = ?";

		List<M_LA2_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_LA2RowMapper_Detail());

		return list.isEmpty() ? null : list.get(0);
	}
	
	// COMPOSITE KEY CLASS INSIDE SERVICE

		public static class M_LA2_PK implements Serializable {

			private Date REPORT_DATE;
			private BigDecimal REPORT_VERSION;

			public M_LA2_PK() {
			}

			public M_LA2_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
				this.REPORT_DATE = REPORT_DATE;
				this.REPORT_VERSION = REPORT_VERSION;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o)
					return true;
				if (!(o instanceof M_LA2_PK))
					return false;
				M_LA2_PK that = (M_LA2_PK) o;
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

	class M_LA2_RowMapper_Summary implements RowMapper<M_LA2_Summary_Entity> {

		@Override
		public M_LA2_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA2_Summary_Entity obj = new M_LA2_Summary_Entity();

			obj.setR12_INDUSTRY(rs.getString("R12_INDUSTRY"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INDUSTRY(rs.getString("R13_INDUSTRY"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INDUSTRY(rs.getString("R14_INDUSTRY"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			obj.setR15_INDUSTRY(rs.getString("R15_INDUSTRY"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			obj.setR16_INDUSTRY(rs.getString("R16_INDUSTRY"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			obj.setR17_INDUSTRY(rs.getString("R17_INDUSTRY"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			obj.setR18_INDUSTRY(rs.getString("R18_INDUSTRY"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			obj.setR19_INDUSTRY(rs.getString("R19_INDUSTRY"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			obj.setR20_INDUSTRY(rs.getString("R20_INDUSTRY"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));

			obj.setR21_INDUSTRY(rs.getString("R21_INDUSTRY"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			obj.setR22_INDUSTRY(rs.getString("R22_INDUSTRY"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			obj.setR23_INDUSTRY(rs.getString("R23_INDUSTRY"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			obj.setR24_INDUSTRY(rs.getString("R24_INDUSTRY"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			obj.setR25_INDUSTRY(rs.getString("R25_INDUSTRY"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			obj.setR26_INDUSTRY(rs.getString("R26_INDUSTRY"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

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

	public static class M_LA2_Summary_Entity {
		private String R12_INDUSTRY;
		private BigDecimal R12_TOTAL;
		private String R13_INDUSTRY;
		private BigDecimal R13_TOTAL;
		private String R14_INDUSTRY;
		private BigDecimal R14_TOTAL;
		private String R15_INDUSTRY;
		private BigDecimal R15_TOTAL;
		private String R16_INDUSTRY;
		private BigDecimal R16_TOTAL;
		private String R17_INDUSTRY;
		private BigDecimal R17_TOTAL;
		private String R18_INDUSTRY;
		private BigDecimal R18_TOTAL;
		private String R19_INDUSTRY;
		private BigDecimal R19_TOTAL;
		private String R20_INDUSTRY;
		private BigDecimal R20_TOTAL;
		private String R21_INDUSTRY;
		private BigDecimal R21_TOTAL;
		private String R22_INDUSTRY;
		private BigDecimal R22_TOTAL;
		private String R23_INDUSTRY;
		private BigDecimal R23_TOTAL;
		private String R24_INDUSTRY;
		private BigDecimal R24_TOTAL;
		private String R25_INDUSTRY;
		private BigDecimal R25_TOTAL;
		private String R26_INDUSTRY;
		private BigDecimal R26_TOTAL;

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

		public String getR12_INDUSTRY() {
			return R12_INDUSTRY;
		}

		public void setR12_INDUSTRY(String R12_INDUSTRY) {
			this.R12_INDUSTRY = R12_INDUSTRY;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal R12_TOTAL) {
			this.R12_TOTAL = R12_TOTAL;
		}

		public String getR13_INDUSTRY() {
			return R13_INDUSTRY;
		}

		public void setR13_INDUSTRY(String R13_INDUSTRY) {
			this.R13_INDUSTRY = R13_INDUSTRY;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		public String getR14_INDUSTRY() {
			return R14_INDUSTRY;
		}

		public void setR14_INDUSTRY(String R14_INDUSTRY) {
			this.R14_INDUSTRY = R14_INDUSTRY;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		public String getR15_INDUSTRY() {
			return R15_INDUSTRY;
		}

		public void setR15_INDUSTRY(String R15_INDUSTRY) {
			this.R15_INDUSTRY = R15_INDUSTRY;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		public String getR16_INDUSTRY() {
			return R16_INDUSTRY;
		}

		public void setR16_INDUSTRY(String R16_INDUSTRY) {
			this.R16_INDUSTRY = R16_INDUSTRY;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		public String getR17_INDUSTRY() {
			return R17_INDUSTRY;
		}

		public void setR17_INDUSTRY(String R17_INDUSTRY) {
			this.R17_INDUSTRY = R17_INDUSTRY;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		public String getR18_INDUSTRY() {
			return R18_INDUSTRY;
		}

		public void setR18_INDUSTRY(String R18_INDUSTRY) {
			this.R18_INDUSTRY = R18_INDUSTRY;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		public String getR19_INDUSTRY() {
			return R19_INDUSTRY;
		}

		public void setR19_INDUSTRY(String R19_INDUSTRY) {
			this.R19_INDUSTRY = R19_INDUSTRY;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}

		public String getR20_INDUSTRY() {
			return R20_INDUSTRY;
		}

		public void setR20_INDUSTRY(String R20_INDUSTRY) {
			this.R20_INDUSTRY = R20_INDUSTRY;
		}

		public BigDecimal getR20_TOTAL() {
			return R20_TOTAL;
		}

		public void setR20_TOTAL(BigDecimal R20_TOTAL) {
			this.R20_TOTAL = R20_TOTAL;
		}

		public String getR21_INDUSTRY() {
			return R21_INDUSTRY;
		}

		public void setR21_INDUSTRY(String R21_INDUSTRY) {
			this.R21_INDUSTRY = R21_INDUSTRY;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal R21_TOTAL) {
			this.R21_TOTAL = R21_TOTAL;
		}

		public String getR22_INDUSTRY() {
			return R22_INDUSTRY;
		}

		public void setR22_INDUSTRY(String R22_INDUSTRY) {
			this.R22_INDUSTRY = R22_INDUSTRY;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal R22_TOTAL) {
			this.R22_TOTAL = R22_TOTAL;
		}

		public String getR23_INDUSTRY() {
			return R23_INDUSTRY;
		}

		public void setR23_INDUSTRY(String R23_INDUSTRY) {
			this.R23_INDUSTRY = R23_INDUSTRY;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal R23_TOTAL) {
			this.R23_TOTAL = R23_TOTAL;
		}

		public String getR24_INDUSTRY() {
			return R24_INDUSTRY;
		}

		public void setR24_INDUSTRY(String R24_INDUSTRY) {
			this.R24_INDUSTRY = R24_INDUSTRY;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal R24_TOTAL) {
			this.R24_TOTAL = R24_TOTAL;
		}

		public String getR25_INDUSTRY() {
			return R25_INDUSTRY;
		}

		public void setR25_INDUSTRY(String R25_INDUSTRY) {
			this.R25_INDUSTRY = R25_INDUSTRY;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal R25_TOTAL) {
			this.R25_TOTAL = R25_TOTAL;
		}

		public String getR26_INDUSTRY() {
			return R26_INDUSTRY;
		}

		public void setR26_INDUSTRY(String R26_INDUSTRY) {
			this.R26_INDUSTRY = R26_INDUSTRY;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal R26_TOTAL) {
			this.R26_TOTAL = R26_TOTAL;
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

	class M_LA2RowMapper_Detail implements RowMapper<M_LA2_Detail_Entity> {

		@Override
		public M_LA2_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA2_Detail_Entity obj = new M_LA2_Detail_Entity();

			obj.setR12_INDUSTRY(rs.getString("R12_INDUSTRY"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INDUSTRY(rs.getString("R13_INDUSTRY"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INDUSTRY(rs.getString("R14_INDUSTRY"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			obj.setR15_INDUSTRY(rs.getString("R15_INDUSTRY"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			obj.setR16_INDUSTRY(rs.getString("R16_INDUSTRY"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			obj.setR17_INDUSTRY(rs.getString("R17_INDUSTRY"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			obj.setR18_INDUSTRY(rs.getString("R18_INDUSTRY"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			obj.setR19_INDUSTRY(rs.getString("R19_INDUSTRY"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			obj.setR20_INDUSTRY(rs.getString("R20_INDUSTRY"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));

			obj.setR21_INDUSTRY(rs.getString("R21_INDUSTRY"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			obj.setR22_INDUSTRY(rs.getString("R22_INDUSTRY"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			obj.setR23_INDUSTRY(rs.getString("R23_INDUSTRY"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			obj.setR24_INDUSTRY(rs.getString("R24_INDUSTRY"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			obj.setR25_INDUSTRY(rs.getString("R25_INDUSTRY"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			obj.setR26_INDUSTRY(rs.getString("R26_INDUSTRY"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

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

	public static class M_LA2_Detail_Entity {
		private String R12_INDUSTRY;
		private BigDecimal R12_TOTAL;
		private String R13_INDUSTRY;
		private BigDecimal R13_TOTAL;
		private String R14_INDUSTRY;
		private BigDecimal R14_TOTAL;
		private String R15_INDUSTRY;
		private BigDecimal R15_TOTAL;
		private String R16_INDUSTRY;
		private BigDecimal R16_TOTAL;
		private String R17_INDUSTRY;
		private BigDecimal R17_TOTAL;
		private String R18_INDUSTRY;
		private BigDecimal R18_TOTAL;
		private String R19_INDUSTRY;
		private BigDecimal R19_TOTAL;
		private String R20_INDUSTRY;
		private BigDecimal R20_TOTAL;
		private String R21_INDUSTRY;
		private BigDecimal R21_TOTAL;
		private String R22_INDUSTRY;
		private BigDecimal R22_TOTAL;
		private String R23_INDUSTRY;
		private BigDecimal R23_TOTAL;
		private String R24_INDUSTRY;
		private BigDecimal R24_TOTAL;
		private String R25_INDUSTRY;
		private BigDecimal R25_TOTAL;
		private String R26_INDUSTRY;
		private BigDecimal R26_TOTAL;

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

		public String getR12_INDUSTRY() {
			return R12_INDUSTRY;
		}

		public void setR12_INDUSTRY(String R12_INDUSTRY) {
			this.R12_INDUSTRY = R12_INDUSTRY;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal R12_TOTAL) {
			this.R12_TOTAL = R12_TOTAL;
		}

		public String getR13_INDUSTRY() {
			return R13_INDUSTRY;
		}

		public void setR13_INDUSTRY(String R13_INDUSTRY) {
			this.R13_INDUSTRY = R13_INDUSTRY;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		public String getR14_INDUSTRY() {
			return R14_INDUSTRY;
		}

		public void setR14_INDUSTRY(String R14_INDUSTRY) {
			this.R14_INDUSTRY = R14_INDUSTRY;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		public String getR15_INDUSTRY() {
			return R15_INDUSTRY;
		}

		public void setR15_INDUSTRY(String R15_INDUSTRY) {
			this.R15_INDUSTRY = R15_INDUSTRY;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		public String getR16_INDUSTRY() {
			return R16_INDUSTRY;
		}

		public void setR16_INDUSTRY(String R16_INDUSTRY) {
			this.R16_INDUSTRY = R16_INDUSTRY;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		public String getR17_INDUSTRY() {
			return R17_INDUSTRY;
		}

		public void setR17_INDUSTRY(String R17_INDUSTRY) {
			this.R17_INDUSTRY = R17_INDUSTRY;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		public String getR18_INDUSTRY() {
			return R18_INDUSTRY;
		}

		public void setR18_INDUSTRY(String R18_INDUSTRY) {
			this.R18_INDUSTRY = R18_INDUSTRY;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		public String getR19_INDUSTRY() {
			return R19_INDUSTRY;
		}

		public void setR19_INDUSTRY(String R19_INDUSTRY) {
			this.R19_INDUSTRY = R19_INDUSTRY;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}

		public String getR20_INDUSTRY() {
			return R20_INDUSTRY;
		}

		public void setR20_INDUSTRY(String R20_INDUSTRY) {
			this.R20_INDUSTRY = R20_INDUSTRY;
		}

		public BigDecimal getR20_TOTAL() {
			return R20_TOTAL;
		}

		public void setR20_TOTAL(BigDecimal R20_TOTAL) {
			this.R20_TOTAL = R20_TOTAL;
		}

		public String getR21_INDUSTRY() {
			return R21_INDUSTRY;
		}

		public void setR21_INDUSTRY(String R21_INDUSTRY) {
			this.R21_INDUSTRY = R21_INDUSTRY;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal R21_TOTAL) {
			this.R21_TOTAL = R21_TOTAL;
		}

		public String getR22_INDUSTRY() {
			return R22_INDUSTRY;
		}

		public void setR22_INDUSTRY(String R22_INDUSTRY) {
			this.R22_INDUSTRY = R22_INDUSTRY;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal R22_TOTAL) {
			this.R22_TOTAL = R22_TOTAL;
		}

		public String getR23_INDUSTRY() {
			return R23_INDUSTRY;
		}

		public void setR23_INDUSTRY(String R23_INDUSTRY) {
			this.R23_INDUSTRY = R23_INDUSTRY;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal R23_TOTAL) {
			this.R23_TOTAL = R23_TOTAL;
		}

		public String getR24_INDUSTRY() {
			return R24_INDUSTRY;
		}

		public void setR24_INDUSTRY(String R24_INDUSTRY) {
			this.R24_INDUSTRY = R24_INDUSTRY;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal R24_TOTAL) {
			this.R24_TOTAL = R24_TOTAL;
		}

		public String getR25_INDUSTRY() {
			return R25_INDUSTRY;
		}

		public void setR25_INDUSTRY(String R25_INDUSTRY) {
			this.R25_INDUSTRY = R25_INDUSTRY;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal R25_TOTAL) {
			this.R25_TOTAL = R25_TOTAL;
		}

		public String getR26_INDUSTRY() {
			return R26_INDUSTRY;
		}

		public void setR26_INDUSTRY(String R26_INDUSTRY) {
			this.R26_INDUSTRY = R26_INDUSTRY;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal R26_TOTAL) {
			this.R26_TOTAL = R26_TOTAL;
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

	class M_LA2_RowMapper_Archival implements RowMapper<M_LA2_Archival_Summary_Entity> {

		@Override
		public M_LA2_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA2_Archival_Summary_Entity obj = new M_LA2_Archival_Summary_Entity();

			obj.setR12_INDUSTRY(rs.getString("R12_INDUSTRY"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INDUSTRY(rs.getString("R13_INDUSTRY"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INDUSTRY(rs.getString("R14_INDUSTRY"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			obj.setR15_INDUSTRY(rs.getString("R15_INDUSTRY"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			obj.setR16_INDUSTRY(rs.getString("R16_INDUSTRY"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			obj.setR17_INDUSTRY(rs.getString("R17_INDUSTRY"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			obj.setR18_INDUSTRY(rs.getString("R18_INDUSTRY"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			obj.setR19_INDUSTRY(rs.getString("R19_INDUSTRY"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			obj.setR20_INDUSTRY(rs.getString("R20_INDUSTRY"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));

			obj.setR21_INDUSTRY(rs.getString("R21_INDUSTRY"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			obj.setR22_INDUSTRY(rs.getString("R22_INDUSTRY"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			obj.setR23_INDUSTRY(rs.getString("R23_INDUSTRY"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			obj.setR24_INDUSTRY(rs.getString("R24_INDUSTRY"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			obj.setR25_INDUSTRY(rs.getString("R25_INDUSTRY"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			obj.setR26_INDUSTRY(rs.getString("R26_INDUSTRY"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

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

	@IdClass(M_LA2_PK.class)
	public static class M_LA2_Archival_Summary_Entity {
		private String R12_INDUSTRY;
		private BigDecimal R12_TOTAL;
		private String R13_INDUSTRY;
		private BigDecimal R13_TOTAL;
		private String R14_INDUSTRY;
		private BigDecimal R14_TOTAL;
		private String R15_INDUSTRY;
		private BigDecimal R15_TOTAL;
		private String R16_INDUSTRY;
		private BigDecimal R16_TOTAL;
		private String R17_INDUSTRY;
		private BigDecimal R17_TOTAL;
		private String R18_INDUSTRY;
		private BigDecimal R18_TOTAL;
		private String R19_INDUSTRY;
		private BigDecimal R19_TOTAL;
		private String R20_INDUSTRY;
		private BigDecimal R20_TOTAL;
		private String R21_INDUSTRY;
		private BigDecimal R21_TOTAL;
		private String R22_INDUSTRY;
		private BigDecimal R22_TOTAL;
		private String R23_INDUSTRY;
		private BigDecimal R23_TOTAL;
		private String R24_INDUSTRY;
		private BigDecimal R24_TOTAL;
		private String R25_INDUSTRY;
		private BigDecimal R25_TOTAL;
		private String R26_INDUSTRY;
		private BigDecimal R26_TOTAL;

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

		public String getR12_INDUSTRY() {
			return R12_INDUSTRY;
		}

		public void setR12_INDUSTRY(String R12_INDUSTRY) {
			this.R12_INDUSTRY = R12_INDUSTRY;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal R12_TOTAL) {
			this.R12_TOTAL = R12_TOTAL;
		}

		public String getR13_INDUSTRY() {
			return R13_INDUSTRY;
		}

		public void setR13_INDUSTRY(String R13_INDUSTRY) {
			this.R13_INDUSTRY = R13_INDUSTRY;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		public String getR14_INDUSTRY() {
			return R14_INDUSTRY;
		}

		public void setR14_INDUSTRY(String R14_INDUSTRY) {
			this.R14_INDUSTRY = R14_INDUSTRY;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		public String getR15_INDUSTRY() {
			return R15_INDUSTRY;
		}

		public void setR15_INDUSTRY(String R15_INDUSTRY) {
			this.R15_INDUSTRY = R15_INDUSTRY;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		public String getR16_INDUSTRY() {
			return R16_INDUSTRY;
		}

		public void setR16_INDUSTRY(String R16_INDUSTRY) {
			this.R16_INDUSTRY = R16_INDUSTRY;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		public String getR17_INDUSTRY() {
			return R17_INDUSTRY;
		}

		public void setR17_INDUSTRY(String R17_INDUSTRY) {
			this.R17_INDUSTRY = R17_INDUSTRY;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		public String getR18_INDUSTRY() {
			return R18_INDUSTRY;
		}

		public void setR18_INDUSTRY(String R18_INDUSTRY) {
			this.R18_INDUSTRY = R18_INDUSTRY;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		public String getR19_INDUSTRY() {
			return R19_INDUSTRY;
		}

		public void setR19_INDUSTRY(String R19_INDUSTRY) {
			this.R19_INDUSTRY = R19_INDUSTRY;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}

		public String getR20_INDUSTRY() {
			return R20_INDUSTRY;
		}

		public void setR20_INDUSTRY(String R20_INDUSTRY) {
			this.R20_INDUSTRY = R20_INDUSTRY;
		}

		public BigDecimal getR20_TOTAL() {
			return R20_TOTAL;
		}

		public void setR20_TOTAL(BigDecimal R20_TOTAL) {
			this.R20_TOTAL = R20_TOTAL;
		}

		public String getR21_INDUSTRY() {
			return R21_INDUSTRY;
		}

		public void setR21_INDUSTRY(String R21_INDUSTRY) {
			this.R21_INDUSTRY = R21_INDUSTRY;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal R21_TOTAL) {
			this.R21_TOTAL = R21_TOTAL;
		}

		public String getR22_INDUSTRY() {
			return R22_INDUSTRY;
		}

		public void setR22_INDUSTRY(String R22_INDUSTRY) {
			this.R22_INDUSTRY = R22_INDUSTRY;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal R22_TOTAL) {
			this.R22_TOTAL = R22_TOTAL;
		}

		public String getR23_INDUSTRY() {
			return R23_INDUSTRY;
		}

		public void setR23_INDUSTRY(String R23_INDUSTRY) {
			this.R23_INDUSTRY = R23_INDUSTRY;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal R23_TOTAL) {
			this.R23_TOTAL = R23_TOTAL;
		}

		public String getR24_INDUSTRY() {
			return R24_INDUSTRY;
		}

		public void setR24_INDUSTRY(String R24_INDUSTRY) {
			this.R24_INDUSTRY = R24_INDUSTRY;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal R24_TOTAL) {
			this.R24_TOTAL = R24_TOTAL;
		}

		public String getR25_INDUSTRY() {
			return R25_INDUSTRY;
		}

		public void setR25_INDUSTRY(String R25_INDUSTRY) {
			this.R25_INDUSTRY = R25_INDUSTRY;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal R25_TOTAL) {
			this.R25_TOTAL = R25_TOTAL;
		}

		public String getR26_INDUSTRY() {
			return R26_INDUSTRY;
		}

		public void setR26_INDUSTRY(String R26_INDUSTRY) {
			this.R26_INDUSTRY = R26_INDUSTRY;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal R26_TOTAL) {
			this.R26_TOTAL = R26_TOTAL;
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

	class M_LA2RowMapper_ArchivalDetail implements RowMapper<M_LA2_Archival_Detail_Entity> {

		@Override
		public M_LA2_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA2_Archival_Detail_Entity obj = new M_LA2_Archival_Detail_Entity();

			obj.setR12_INDUSTRY(rs.getString("R12_INDUSTRY"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INDUSTRY(rs.getString("R13_INDUSTRY"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INDUSTRY(rs.getString("R14_INDUSTRY"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			obj.setR15_INDUSTRY(rs.getString("R15_INDUSTRY"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			obj.setR16_INDUSTRY(rs.getString("R16_INDUSTRY"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			obj.setR17_INDUSTRY(rs.getString("R17_INDUSTRY"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			obj.setR18_INDUSTRY(rs.getString("R18_INDUSTRY"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			obj.setR19_INDUSTRY(rs.getString("R19_INDUSTRY"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			obj.setR20_INDUSTRY(rs.getString("R20_INDUSTRY"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));

			obj.setR21_INDUSTRY(rs.getString("R21_INDUSTRY"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			obj.setR22_INDUSTRY(rs.getString("R22_INDUSTRY"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			obj.setR23_INDUSTRY(rs.getString("R23_INDUSTRY"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			obj.setR24_INDUSTRY(rs.getString("R24_INDUSTRY"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			obj.setR25_INDUSTRY(rs.getString("R25_INDUSTRY"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			obj.setR26_INDUSTRY(rs.getString("R26_INDUSTRY"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

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

	public static class M_LA2_Archival_Detail_Entity {
		private String R12_INDUSTRY;
		private BigDecimal R12_TOTAL;
		private String R13_INDUSTRY;
		private BigDecimal R13_TOTAL;
		private String R14_INDUSTRY;
		private BigDecimal R14_TOTAL;
		private String R15_INDUSTRY;
		private BigDecimal R15_TOTAL;
		private String R16_INDUSTRY;
		private BigDecimal R16_TOTAL;
		private String R17_INDUSTRY;
		private BigDecimal R17_TOTAL;
		private String R18_INDUSTRY;
		private BigDecimal R18_TOTAL;
		private String R19_INDUSTRY;
		private BigDecimal R19_TOTAL;
		private String R20_INDUSTRY;
		private BigDecimal R20_TOTAL;
		private String R21_INDUSTRY;
		private BigDecimal R21_TOTAL;
		private String R22_INDUSTRY;
		private BigDecimal R22_TOTAL;
		private String R23_INDUSTRY;
		private BigDecimal R23_TOTAL;
		private String R24_INDUSTRY;
		private BigDecimal R24_TOTAL;
		private String R25_INDUSTRY;
		private BigDecimal R25_TOTAL;
		private String R26_INDUSTRY;
		private BigDecimal R26_TOTAL;

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

		public String getR12_INDUSTRY() {
			return R12_INDUSTRY;
		}

		public void setR12_INDUSTRY(String R12_INDUSTRY) {
			this.R12_INDUSTRY = R12_INDUSTRY;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal R12_TOTAL) {
			this.R12_TOTAL = R12_TOTAL;
		}

		public String getR13_INDUSTRY() {
			return R13_INDUSTRY;
		}

		public void setR13_INDUSTRY(String R13_INDUSTRY) {
			this.R13_INDUSTRY = R13_INDUSTRY;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		public String getR14_INDUSTRY() {
			return R14_INDUSTRY;
		}

		public void setR14_INDUSTRY(String R14_INDUSTRY) {
			this.R14_INDUSTRY = R14_INDUSTRY;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		public String getR15_INDUSTRY() {
			return R15_INDUSTRY;
		}

		public void setR15_INDUSTRY(String R15_INDUSTRY) {
			this.R15_INDUSTRY = R15_INDUSTRY;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		public String getR16_INDUSTRY() {
			return R16_INDUSTRY;
		}

		public void setR16_INDUSTRY(String R16_INDUSTRY) {
			this.R16_INDUSTRY = R16_INDUSTRY;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		public String getR17_INDUSTRY() {
			return R17_INDUSTRY;
		}

		public void setR17_INDUSTRY(String R17_INDUSTRY) {
			this.R17_INDUSTRY = R17_INDUSTRY;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		public String getR18_INDUSTRY() {
			return R18_INDUSTRY;
		}

		public void setR18_INDUSTRY(String R18_INDUSTRY) {
			this.R18_INDUSTRY = R18_INDUSTRY;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		public String getR19_INDUSTRY() {
			return R19_INDUSTRY;
		}

		public void setR19_INDUSTRY(String R19_INDUSTRY) {
			this.R19_INDUSTRY = R19_INDUSTRY;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}

		public String getR20_INDUSTRY() {
			return R20_INDUSTRY;
		}

		public void setR20_INDUSTRY(String R20_INDUSTRY) {
			this.R20_INDUSTRY = R20_INDUSTRY;
		}

		public BigDecimal getR20_TOTAL() {
			return R20_TOTAL;
		}

		public void setR20_TOTAL(BigDecimal R20_TOTAL) {
			this.R20_TOTAL = R20_TOTAL;
		}

		public String getR21_INDUSTRY() {
			return R21_INDUSTRY;
		}

		public void setR21_INDUSTRY(String R21_INDUSTRY) {
			this.R21_INDUSTRY = R21_INDUSTRY;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal R21_TOTAL) {
			this.R21_TOTAL = R21_TOTAL;
		}

		public String getR22_INDUSTRY() {
			return R22_INDUSTRY;
		}

		public void setR22_INDUSTRY(String R22_INDUSTRY) {
			this.R22_INDUSTRY = R22_INDUSTRY;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal R22_TOTAL) {
			this.R22_TOTAL = R22_TOTAL;
		}

		public String getR23_INDUSTRY() {
			return R23_INDUSTRY;
		}

		public void setR23_INDUSTRY(String R23_INDUSTRY) {
			this.R23_INDUSTRY = R23_INDUSTRY;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal R23_TOTAL) {
			this.R23_TOTAL = R23_TOTAL;
		}

		public String getR24_INDUSTRY() {
			return R24_INDUSTRY;
		}

		public void setR24_INDUSTRY(String R24_INDUSTRY) {
			this.R24_INDUSTRY = R24_INDUSTRY;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal R24_TOTAL) {
			this.R24_TOTAL = R24_TOTAL;
		}

		public String getR25_INDUSTRY() {
			return R25_INDUSTRY;
		}

		public void setR25_INDUSTRY(String R25_INDUSTRY) {
			this.R25_INDUSTRY = R25_INDUSTRY;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal R25_TOTAL) {
			this.R25_TOTAL = R25_TOTAL;
		}

		public String getR26_INDUSTRY() {
			return R26_INDUSTRY;
		}

		public void setR26_INDUSTRY(String R26_INDUSTRY) {
			this.R26_INDUSTRY = R26_INDUSTRY;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal R26_TOTAL) {
			this.R26_TOTAL = R26_TOTAL;
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

	class M_LA2_RowMapper_Resub implements RowMapper<M_LA2_RESUB_Summary_Entity> {

		@Override
		public M_LA2_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA2_RESUB_Summary_Entity obj = new M_LA2_RESUB_Summary_Entity();	
			
			obj.setR12_INDUSTRY(rs.getString("R12_INDUSTRY"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INDUSTRY(rs.getString("R13_INDUSTRY"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INDUSTRY(rs.getString("R14_INDUSTRY"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			obj.setR15_INDUSTRY(rs.getString("R15_INDUSTRY"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			obj.setR16_INDUSTRY(rs.getString("R16_INDUSTRY"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			obj.setR17_INDUSTRY(rs.getString("R17_INDUSTRY"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			obj.setR18_INDUSTRY(rs.getString("R18_INDUSTRY"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			obj.setR19_INDUSTRY(rs.getString("R19_INDUSTRY"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			obj.setR20_INDUSTRY(rs.getString("R20_INDUSTRY"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));

			obj.setR21_INDUSTRY(rs.getString("R21_INDUSTRY"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			obj.setR22_INDUSTRY(rs.getString("R22_INDUSTRY"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			obj.setR23_INDUSTRY(rs.getString("R23_INDUSTRY"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			obj.setR24_INDUSTRY(rs.getString("R24_INDUSTRY"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			obj.setR25_INDUSTRY(rs.getString("R25_INDUSTRY"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			obj.setR26_INDUSTRY(rs.getString("R26_INDUSTRY"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));
			
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
	
	public static class M_LA2_RESUB_Summary_Entity {
		private String R12_INDUSTRY;
		private BigDecimal R12_TOTAL;
		private String R13_INDUSTRY;
		private BigDecimal R13_TOTAL;
		private String R14_INDUSTRY;
		private BigDecimal R14_TOTAL;
		private String R15_INDUSTRY;
		private BigDecimal R15_TOTAL;
		private String R16_INDUSTRY;
		private BigDecimal R16_TOTAL;
		private String R17_INDUSTRY;
		private BigDecimal R17_TOTAL;
		private String R18_INDUSTRY;
		private BigDecimal R18_TOTAL;
		private String R19_INDUSTRY;
		private BigDecimal R19_TOTAL;
		private String R20_INDUSTRY;
		private BigDecimal R20_TOTAL;
		private String R21_INDUSTRY;
		private BigDecimal R21_TOTAL;
		private String R22_INDUSTRY;
		private BigDecimal R22_TOTAL;
		private String R23_INDUSTRY;
		private BigDecimal R23_TOTAL;
		private String R24_INDUSTRY;
		private BigDecimal R24_TOTAL;
		private String R25_INDUSTRY;
		private BigDecimal R25_TOTAL;
		private String R26_INDUSTRY;
		private BigDecimal R26_TOTAL;
		
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
			
			public String getR12_INDUSTRY() {
				return R12_INDUSTRY;
			}

			public void setR12_INDUSTRY(String R12_INDUSTRY) {
				this.R12_INDUSTRY = R12_INDUSTRY;
			}

			public BigDecimal getR12_TOTAL() {
				return R12_TOTAL;
			}

			public void setR12_TOTAL(BigDecimal R12_TOTAL) {
				this.R12_TOTAL = R12_TOTAL;
			}

			public String getR13_INDUSTRY() {
				return R13_INDUSTRY;
			}

			public void setR13_INDUSTRY(String R13_INDUSTRY) {
				this.R13_INDUSTRY = R13_INDUSTRY;
			}

			public BigDecimal getR13_TOTAL() {
				return R13_TOTAL;
			}

			public void setR13_TOTAL(BigDecimal R13_TOTAL) {
				this.R13_TOTAL = R13_TOTAL;
			}

			public String getR14_INDUSTRY() {
				return R14_INDUSTRY;
			}

			public void setR14_INDUSTRY(String R14_INDUSTRY) {
				this.R14_INDUSTRY = R14_INDUSTRY;
			}

			public BigDecimal getR14_TOTAL() {
				return R14_TOTAL;
			}

			public void setR14_TOTAL(BigDecimal R14_TOTAL) {
				this.R14_TOTAL = R14_TOTAL;
			}

			public String getR15_INDUSTRY() {
				return R15_INDUSTRY;
			}

			public void setR15_INDUSTRY(String R15_INDUSTRY) {
				this.R15_INDUSTRY = R15_INDUSTRY;
			}

			public BigDecimal getR15_TOTAL() {
				return R15_TOTAL;
			}

			public void setR15_TOTAL(BigDecimal R15_TOTAL) {
				this.R15_TOTAL = R15_TOTAL;
			}

			public String getR16_INDUSTRY() {
				return R16_INDUSTRY;
			}

			public void setR16_INDUSTRY(String R16_INDUSTRY) {
				this.R16_INDUSTRY = R16_INDUSTRY;
			}

			public BigDecimal getR16_TOTAL() {
				return R16_TOTAL;
			}

			public void setR16_TOTAL(BigDecimal R16_TOTAL) {
				this.R16_TOTAL = R16_TOTAL;
			}

			public String getR17_INDUSTRY() {
				return R17_INDUSTRY;
			}

			public void setR17_INDUSTRY(String R17_INDUSTRY) {
				this.R17_INDUSTRY = R17_INDUSTRY;
			}

			public BigDecimal getR17_TOTAL() {
				return R17_TOTAL;
			}

			public void setR17_TOTAL(BigDecimal R17_TOTAL) {
				this.R17_TOTAL = R17_TOTAL;
			}

			public String getR18_INDUSTRY() {
				return R18_INDUSTRY;
			}

			public void setR18_INDUSTRY(String R18_INDUSTRY) {
				this.R18_INDUSTRY = R18_INDUSTRY;
			}

			public BigDecimal getR18_TOTAL() {
				return R18_TOTAL;
			}

			public void setR18_TOTAL(BigDecimal R18_TOTAL) {
				this.R18_TOTAL = R18_TOTAL;
			}

			public String getR19_INDUSTRY() {
				return R19_INDUSTRY;
			}

			public void setR19_INDUSTRY(String R19_INDUSTRY) {
				this.R19_INDUSTRY = R19_INDUSTRY;
			}

			public BigDecimal getR19_TOTAL() {
				return R19_TOTAL;
			}

			public void setR19_TOTAL(BigDecimal R19_TOTAL) {
				this.R19_TOTAL = R19_TOTAL;
			}

			public String getR20_INDUSTRY() {
				return R20_INDUSTRY;
			}

			public void setR20_INDUSTRY(String R20_INDUSTRY) {
				this.R20_INDUSTRY = R20_INDUSTRY;
			}

			public BigDecimal getR20_TOTAL() {
				return R20_TOTAL;
			}

			public void setR20_TOTAL(BigDecimal R20_TOTAL) {
				this.R20_TOTAL = R20_TOTAL;
			}

			public String getR21_INDUSTRY() {
				return R21_INDUSTRY;
			}

			public void setR21_INDUSTRY(String R21_INDUSTRY) {
				this.R21_INDUSTRY = R21_INDUSTRY;
			}

			public BigDecimal getR21_TOTAL() {
				return R21_TOTAL;
			}

			public void setR21_TOTAL(BigDecimal R21_TOTAL) {
				this.R21_TOTAL = R21_TOTAL;
			}

			public String getR22_INDUSTRY() {
				return R22_INDUSTRY;
			}

			public void setR22_INDUSTRY(String R22_INDUSTRY) {
				this.R22_INDUSTRY = R22_INDUSTRY;
			}

			public BigDecimal getR22_TOTAL() {
				return R22_TOTAL;
			}

			public void setR22_TOTAL(BigDecimal R22_TOTAL) {
				this.R22_TOTAL = R22_TOTAL;
			}

			public String getR23_INDUSTRY() {
				return R23_INDUSTRY;
			}

			public void setR23_INDUSTRY(String R23_INDUSTRY) {
				this.R23_INDUSTRY = R23_INDUSTRY;
			}

			public BigDecimal getR23_TOTAL() {
				return R23_TOTAL;
			}

			public void setR23_TOTAL(BigDecimal R23_TOTAL) {
				this.R23_TOTAL = R23_TOTAL;
			}

			public String getR24_INDUSTRY() {
				return R24_INDUSTRY;
			}

			public void setR24_INDUSTRY(String R24_INDUSTRY) {
				this.R24_INDUSTRY = R24_INDUSTRY;
			}

			public BigDecimal getR24_TOTAL() {
				return R24_TOTAL;
			}

			public void setR24_TOTAL(BigDecimal R24_TOTAL) {
				this.R24_TOTAL = R24_TOTAL;
			}

			public String getR25_INDUSTRY() {
				return R25_INDUSTRY;
			}

			public void setR25_INDUSTRY(String R25_INDUSTRY) {
				this.R25_INDUSTRY = R25_INDUSTRY;
			}

			public BigDecimal getR25_TOTAL() {
				return R25_TOTAL;
			}

			public void setR25_TOTAL(BigDecimal R25_TOTAL) {
				this.R25_TOTAL = R25_TOTAL;
			}

			public String getR26_INDUSTRY() {
				return R26_INDUSTRY;
			}

			public void setR26_INDUSTRY(String R26_INDUSTRY) {
				this.R26_INDUSTRY = R26_INDUSTRY;
			}

			public BigDecimal getR26_TOTAL() {
				return R26_TOTAL;
			}

			public void setR26_TOTAL(BigDecimal R26_TOTAL) {
				this.R26_TOTAL = R26_TOTAL;
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


	// ROW MAPPER RESUBDETAIL

	class M_LA2RowMapper_ResubDetail implements RowMapper<M_LA2_RESUB_Detail_Entity> {

		@Override
		public M_LA2_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA2_RESUB_Detail_Entity obj = new M_LA2_RESUB_Detail_Entity();
			
			obj.setR12_INDUSTRY(rs.getString("R12_INDUSTRY"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			obj.setR13_INDUSTRY(rs.getString("R13_INDUSTRY"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			obj.setR14_INDUSTRY(rs.getString("R14_INDUSTRY"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			obj.setR15_INDUSTRY(rs.getString("R15_INDUSTRY"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			obj.setR16_INDUSTRY(rs.getString("R16_INDUSTRY"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			obj.setR17_INDUSTRY(rs.getString("R17_INDUSTRY"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			obj.setR18_INDUSTRY(rs.getString("R18_INDUSTRY"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			obj.setR19_INDUSTRY(rs.getString("R19_INDUSTRY"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			obj.setR20_INDUSTRY(rs.getString("R20_INDUSTRY"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));

			obj.setR21_INDUSTRY(rs.getString("R21_INDUSTRY"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			obj.setR22_INDUSTRY(rs.getString("R22_INDUSTRY"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			obj.setR23_INDUSTRY(rs.getString("R23_INDUSTRY"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			obj.setR24_INDUSTRY(rs.getString("R24_INDUSTRY"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			obj.setR25_INDUSTRY(rs.getString("R25_INDUSTRY"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			obj.setR26_INDUSTRY(rs.getString("R26_INDUSTRY"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));
			
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
	
	public static class M_LA2_RESUB_Detail_Entity {
		private String R12_INDUSTRY;
		private BigDecimal R12_TOTAL;
		private String R13_INDUSTRY;
		private BigDecimal R13_TOTAL;
		private String R14_INDUSTRY;
		private BigDecimal R14_TOTAL;
		private String R15_INDUSTRY;
		private BigDecimal R15_TOTAL;
		private String R16_INDUSTRY;
		private BigDecimal R16_TOTAL;
		private String R17_INDUSTRY;
		private BigDecimal R17_TOTAL;
		private String R18_INDUSTRY;
		private BigDecimal R18_TOTAL;
		private String R19_INDUSTRY;
		private BigDecimal R19_TOTAL;
		private String R20_INDUSTRY;
		private BigDecimal R20_TOTAL;
		private String R21_INDUSTRY;
		private BigDecimal R21_TOTAL;
		private String R22_INDUSTRY;
		private BigDecimal R22_TOTAL;
		private String R23_INDUSTRY;
		private BigDecimal R23_TOTAL;
		private String R24_INDUSTRY;
		private BigDecimal R24_TOTAL;
		private String R25_INDUSTRY;
		private BigDecimal R25_TOTAL;
		private String R26_INDUSTRY;
		private BigDecimal R26_TOTAL;
		
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
			
			public String getR12_INDUSTRY() {
				return R12_INDUSTRY;
			}

			public void setR12_INDUSTRY(String R12_INDUSTRY) {
				this.R12_INDUSTRY = R12_INDUSTRY;
			}

			public BigDecimal getR12_TOTAL() {
				return R12_TOTAL;
			}

			public void setR12_TOTAL(BigDecimal R12_TOTAL) {
				this.R12_TOTAL = R12_TOTAL;
			}

			public String getR13_INDUSTRY() {
				return R13_INDUSTRY;
			}

			public void setR13_INDUSTRY(String R13_INDUSTRY) {
				this.R13_INDUSTRY = R13_INDUSTRY;
			}

			public BigDecimal getR13_TOTAL() {
				return R13_TOTAL;
			}

			public void setR13_TOTAL(BigDecimal R13_TOTAL) {
				this.R13_TOTAL = R13_TOTAL;
			}

			public String getR14_INDUSTRY() {
				return R14_INDUSTRY;
			}

			public void setR14_INDUSTRY(String R14_INDUSTRY) {
				this.R14_INDUSTRY = R14_INDUSTRY;
			}

			public BigDecimal getR14_TOTAL() {
				return R14_TOTAL;
			}

			public void setR14_TOTAL(BigDecimal R14_TOTAL) {
				this.R14_TOTAL = R14_TOTAL;
			}

			public String getR15_INDUSTRY() {
				return R15_INDUSTRY;
			}

			public void setR15_INDUSTRY(String R15_INDUSTRY) {
				this.R15_INDUSTRY = R15_INDUSTRY;
			}

			public BigDecimal getR15_TOTAL() {
				return R15_TOTAL;
			}

			public void setR15_TOTAL(BigDecimal R15_TOTAL) {
				this.R15_TOTAL = R15_TOTAL;
			}

			public String getR16_INDUSTRY() {
				return R16_INDUSTRY;
			}

			public void setR16_INDUSTRY(String R16_INDUSTRY) {
				this.R16_INDUSTRY = R16_INDUSTRY;
			}

			public BigDecimal getR16_TOTAL() {
				return R16_TOTAL;
			}

			public void setR16_TOTAL(BigDecimal R16_TOTAL) {
				this.R16_TOTAL = R16_TOTAL;
			}

			public String getR17_INDUSTRY() {
				return R17_INDUSTRY;
			}

			public void setR17_INDUSTRY(String R17_INDUSTRY) {
				this.R17_INDUSTRY = R17_INDUSTRY;
			}

			public BigDecimal getR17_TOTAL() {
				return R17_TOTAL;
			}

			public void setR17_TOTAL(BigDecimal R17_TOTAL) {
				this.R17_TOTAL = R17_TOTAL;
			}

			public String getR18_INDUSTRY() {
				return R18_INDUSTRY;
			}

			public void setR18_INDUSTRY(String R18_INDUSTRY) {
				this.R18_INDUSTRY = R18_INDUSTRY;
			}

			public BigDecimal getR18_TOTAL() {
				return R18_TOTAL;
			}

			public void setR18_TOTAL(BigDecimal R18_TOTAL) {
				this.R18_TOTAL = R18_TOTAL;
			}

			public String getR19_INDUSTRY() {
				return R19_INDUSTRY;
			}

			public void setR19_INDUSTRY(String R19_INDUSTRY) {
				this.R19_INDUSTRY = R19_INDUSTRY;
			}

			public BigDecimal getR19_TOTAL() {
				return R19_TOTAL;
			}

			public void setR19_TOTAL(BigDecimal R19_TOTAL) {
				this.R19_TOTAL = R19_TOTAL;
			}

			public String getR20_INDUSTRY() {
				return R20_INDUSTRY;
			}

			public void setR20_INDUSTRY(String R20_INDUSTRY) {
				this.R20_INDUSTRY = R20_INDUSTRY;
			}

			public BigDecimal getR20_TOTAL() {
				return R20_TOTAL;
			}

			public void setR20_TOTAL(BigDecimal R20_TOTAL) {
				this.R20_TOTAL = R20_TOTAL;
			}

			public String getR21_INDUSTRY() {
				return R21_INDUSTRY;
			}

			public void setR21_INDUSTRY(String R21_INDUSTRY) {
				this.R21_INDUSTRY = R21_INDUSTRY;
			}

			public BigDecimal getR21_TOTAL() {
				return R21_TOTAL;
			}

			public void setR21_TOTAL(BigDecimal R21_TOTAL) {
				this.R21_TOTAL = R21_TOTAL;
			}

			public String getR22_INDUSTRY() {
				return R22_INDUSTRY;
			}

			public void setR22_INDUSTRY(String R22_INDUSTRY) {
				this.R22_INDUSTRY = R22_INDUSTRY;
			}

			public BigDecimal getR22_TOTAL() {
				return R22_TOTAL;
			}

			public void setR22_TOTAL(BigDecimal R22_TOTAL) {
				this.R22_TOTAL = R22_TOTAL;
			}

			public String getR23_INDUSTRY() {
				return R23_INDUSTRY;
			}

			public void setR23_INDUSTRY(String R23_INDUSTRY) {
				this.R23_INDUSTRY = R23_INDUSTRY;
			}

			public BigDecimal getR23_TOTAL() {
				return R23_TOTAL;
			}

			public void setR23_TOTAL(BigDecimal R23_TOTAL) {
				this.R23_TOTAL = R23_TOTAL;
			}

			public String getR24_INDUSTRY() {
				return R24_INDUSTRY;
			}

			public void setR24_INDUSTRY(String R24_INDUSTRY) {
				this.R24_INDUSTRY = R24_INDUSTRY;
			}

			public BigDecimal getR24_TOTAL() {
				return R24_TOTAL;
			}

			public void setR24_TOTAL(BigDecimal R24_TOTAL) {
				this.R24_TOTAL = R24_TOTAL;
			}

			public String getR25_INDUSTRY() {
				return R25_INDUSTRY;
			}

			public void setR25_INDUSTRY(String R25_INDUSTRY) {
				this.R25_INDUSTRY = R25_INDUSTRY;
			}

			public BigDecimal getR25_TOTAL() {
				return R25_TOTAL;
			}

			public void setR25_TOTAL(BigDecimal R25_TOTAL) {
				this.R25_TOTAL = R25_TOTAL;
			}

			public String getR26_INDUSTRY() {
				return R26_INDUSTRY;
			}

			public void setR26_INDUSTRY(String R26_INDUSTRY) {
				this.R26_INDUSTRY = R26_INDUSTRY;
			}

			public BigDecimal getR26_TOTAL() {
				return R26_TOTAL;
			}

			public void setR26_TOTAL(BigDecimal R26_TOTAL) {
				this.R26_TOTAL = R26_TOTAL;
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

	public ModelAndView getBRRS_M_LA2View(String reportId, String fromdate, String todate, String currency,
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
				List<M_LA2_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dt, version);
						mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_LA2_RESUB_Summary_Entity> T1Master = getdatabydateListresub1(dt, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_LA2_Summary_Entity> T1Master = getDataByDate1(dt);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_LA2_Archival_Detail_Entity> T1Master = getdatabydateListArchivalDetail1(dt, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_LA2_RESUB_Detail_Entity> T1Master = getdatabydateListResubDetail1(dt, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_LA2_Detail_Entity> T1Master = getDetaildatabydateList1(dt);
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_LA2");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	@Transactional
	public void updateReport(M_LA2_Summary_Entity request1) {

	    try {

	        logger.info("Came to services");
	        logger.info("Report Date: {}", request1.getREPORT_DATE());

	        // Fetch existing record
	        List<M_LA2_Summary_Entity> records =
	                getDataByDate1(request1.getREPORT_DATE());

	        if (records == null || records.isEmpty()) {

	            throw new RuntimeException(
	                    "Record not found for REPORT_DATE : "
	                            + request1.getREPORT_DATE());
	        }

	        M_LA2_Summary_Entity existing = records.get(0);

	        // Audit old copy
	        M_LA2_Summary_Entity oldcopy =
	                new M_LA2_Summary_Entity();

	        BeanUtils.copyProperties(existing, oldcopy);

	        String changes = auditService.getChanges(oldcopy, request1);

	        if (!changes.isEmpty()) {

	            String sql =
	                    "UPDATE BRRS_M_LA2_SUMMARYTABLE SET " +
	                    "R12_INDUSTRY=?, R12_TOTAL=?, " +
	                    "R13_INDUSTRY=?, R13_TOTAL=?, " +
	                    "R14_INDUSTRY=?, R14_TOTAL=?, " +
	                    "R15_INDUSTRY=?, R15_TOTAL=?, " +
	                    "R16_INDUSTRY=?, R16_TOTAL=?, " +
	                    "R17_INDUSTRY=?, R17_TOTAL=?, " +
	                    "R18_INDUSTRY=?, R18_TOTAL=?, " +
	                    "R19_INDUSTRY=?, R19_TOTAL=?, " +
	                    "R20_INDUSTRY=?, R20_TOTAL=?, " +
	                    "R21_INDUSTRY=?, R21_TOTAL=?, " +
	                    "R22_INDUSTRY=?, R22_TOTAL=?, " +
	                    "R23_INDUSTRY=?, R23_TOTAL=?, " +
	                    "R24_INDUSTRY=?, R24_TOTAL=?, " +
	                    "R25_INDUSTRY=?, R25_TOTAL=?, " +
	                    "R26_INDUSTRY=?, R26_TOTAL=? " +
	                    "WHERE REPORT_DATE=?";

	            int count = jdbcTemplate.update(

	                    sql,

	                    request1.getR12_INDUSTRY(),
	                    request1.getR12_TOTAL(),

	                    request1.getR13_INDUSTRY(),
	                    request1.getR13_TOTAL(),

	                    request1.getR14_INDUSTRY(),
	                    request1.getR14_TOTAL(),

	                    request1.getR15_INDUSTRY(),
	                    request1.getR15_TOTAL(),

	                    request1.getR16_INDUSTRY(),
	                    request1.getR16_TOTAL(),

	                    request1.getR17_INDUSTRY(),
	                    request1.getR17_TOTAL(),

	                    request1.getR18_INDUSTRY(),
	                    request1.getR18_TOTAL(),

	                    request1.getR19_INDUSTRY(),
	                    request1.getR19_TOTAL(),

	                    request1.getR20_INDUSTRY(),
	                    request1.getR20_TOTAL(),

	                    request1.getR21_INDUSTRY(),
	                    request1.getR21_TOTAL(),

	                    request1.getR22_INDUSTRY(),
	                    request1.getR22_TOTAL(),

	                    request1.getR23_INDUSTRY(),
	                    request1.getR23_TOTAL(),

	                    request1.getR24_INDUSTRY(),
	                    request1.getR24_TOTAL(),

	                    request1.getR25_INDUSTRY(),
	                    request1.getR25_TOTAL(),

	                    request1.getR26_INDUSTRY(),
	                    request1.getR26_TOTAL(),

	                    request1.getREPORT_DATE()
	            );

	            if (count > 0) {

	                auditService.compareEntitiesmanual(
	                        oldcopy,
	                        request1,
	                        request1.getREPORT_DATE().toString(),
	                        "M LA2 Summary Screen",
	                        "BRRS_M_LA2_SUMMARYTABLE"
	                );

	                logger.info(
	                        "Audit completed for REPORT_DATE {}",
	                        request1.getREPORT_DATE());

	                logger.info(
	                        "M_LA2 Summary Updated Successfully. Rows Updated: {}",
	                        count);
	            }

	        } else {

	            logger.info(
	                    "No changes detected for REPORT_DATE {}",
	                    request1.getREPORT_DATE());
	        }

	    } catch (Exception e) {

	        logger.error(
	                "Error while updating BRRS_M_LA2 Report",
	                e);

	        throw new RuntimeException(
	                "Error while updating BRRS_M_LA2 Report",
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
	public void updateResubReport(M_LA2_RESUB_Summary_Entity updatedEntity) {

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

	    M_LA2_RESUB_Detail_Entity detailEntity =
	            new M_LA2_RESUB_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity, detailEntity);

	    detailEntity.setREPORT_DATE(reportDate);
	    detailEntity.setREPORT_VERSION(newVersion);
	    detailEntity.setREPORT_RESUBDATE(now);

	    insertResubDetail(detailEntity);

	    // ====================================================
	    // 5. ARCHIVAL SUMMARY
	    // ====================================================

	    M_LA2_Archival_Summary_Entity archivalSummary =
	            new M_LA2_Archival_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity, archivalSummary);

	    archivalSummary.setREPORT_DATE(reportDate);
	    archivalSummary.setREPORT_VERSION(newVersion);
	    archivalSummary.setREPORT_RESUBDATE(now);

	    insertArchivalSummary(archivalSummary);

	    // ====================================================
	    // 6. ARCHIVAL DETAIL
	    // ====================================================

	    M_LA2_Archival_Detail_Entity archivalDetail =
	            new M_LA2_Archival_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity, archivalDetail);

	    archivalDetail.setREPORT_DATE(reportDate);
	    archivalDetail.setREPORT_VERSION(newVersion);
	    archivalDetail.setREPORT_RESUBDATE(now);

	    insertArchivalDetail(archivalDetail);

	    System.out.println("Resubmission Version Created : " + newVersion);
	}
	
	
	private void insertResubSummary(M_LA2_RESUB_Summary_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_LA2_RESUB_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 11; i <= 25; i++) {

	            columns.append("R").append(i).append("_INDUSTRY,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,");

	            params.add(getValue(entity, "getR" + i + "_INDUSTRY"));
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
	
	private void insertResubDetail(M_LA2_RESUB_Detail_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_LA2_RESUB_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 11; i <= 25; i++) {

	            columns.append("R").append(i).append("_INDUSTRY,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,");

	            params.add(getValue(entity, "getR" + i + "_INDUSTRY"));
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
	
	private void insertArchivalSummary(M_LA2_Archival_Summary_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_LA2_ARCHIVAL_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 11; i <= 25; i++) {

	            columns.append("R").append(i).append("_INDUSTRY,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,");

	            params.add(getValue(entity, "getR" + i + "_INDUSTRY"));
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
	
	private void insertArchivalDetail(M_LA2_Archival_Detail_Entity entity) {

	    try {

	        StringBuilder columns = new StringBuilder(
	                "INSERT INTO BRRS_M_LA2_ARCHIVAL_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

	        StringBuilder values = new StringBuilder(
	                " VALUES (?,?,?,");

	        List<Object> params = new ArrayList<>();

	        params.add(entity.getREPORT_DATE());
	        params.add(entity.getREPORT_VERSION());
	        params.add(entity.getREPORT_RESUBDATE());

	        for (int i = 11; i <= 25; i++) {

	            columns.append("R").append(i).append("_INDUSTRY,")
	                   .append("R").append(i).append("_TOTAL,");

	            values.append("?,?,");

	            params.add(getValue(entity, "getR" + i + "_INDUSTRY"));
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
	
	
	
	
	
	
	
	
	
	

	public List<Object[]> getM_LA2Resub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {

	        List<M_LA2_Archival_Summary_Entity> latestArchivalList = getdatabydateListWithVersion1();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_LA2_Archival_Summary_Entity entity : latestArchivalList) {
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
	        System.err.println("Error fetching M_LA2 Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	public List<Object[]> getM_LA2Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " + "FROM BRRS_M_LA2_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	
	// Normal format Excel

	public byte[] getBRRS_M_LA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_LA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_LA2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_LA2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_LA2_Summary_Entity> dataList = getDataByDate1(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
							M_LA2_Summary_Entity record = dataList.get(i);
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

							// row12
							// Column B
							row = sheet.getRow(11);

							Cell cell2 = row.createCell(1);
							if (record.getR12_TOTAL() != null) {
								cell2.setCellValue(record.getR12_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// Column B
							Cell R13cell2 = row.getCell(1);
							if (record.getR13_TOTAL() != null) {
								R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);

							}

							// row14
							row = sheet.getRow(13);
							// Column B
							Cell R14cell2 = row.getCell(1);
							if (record.getR14_TOTAL() != null) {
								R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);

							}

							// row15
							row = sheet.getRow(14);
							// Column B
							Cell R15cell2 = row.getCell(1);
							if (record.getR15_TOTAL() != null) {
								R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);

							}

							// row16
							row = sheet.getRow(15);
							// Column B
							Cell R16cell2 = row.getCell(1);
							if (record.getR16_TOTAL() != null) {
								R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);

							}

							// row17
							row = sheet.getRow(16);
							// Column B
							Cell R17cell2 = row.getCell(1);
							if (record.getR17_TOTAL() != null) {
								R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
								R17cell2.setCellStyle(numberStyle);
							} else {
								R17cell2.setCellValue("");
								R17cell2.setCellStyle(textStyle);

							}

							// row18
							row = sheet.getRow(17);
							// Column B
							Cell R18cell2 = row.getCell(1);
							if (record.getR18_TOTAL() != null) {
								R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
								R18cell2.setCellStyle(numberStyle);
							} else {
								R18cell2.setCellValue("");
								R18cell2.setCellStyle(textStyle);

							}

							// row19
							row = sheet.getRow(18);
							// Column B
							Cell R19cell2 = row.getCell(1);
							if (record.getR19_TOTAL() != null) {
								R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
								R19cell2.setCellStyle(numberStyle);
							} else {
								R19cell2.setCellValue("");
								R19cell2.setCellStyle(textStyle);

							}

							// row20
							row = sheet.getRow(19);
							// Column B
							Cell R20cell2 = row.getCell(1);
							if (record.getR20_TOTAL() != null) {
								R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
								R20cell2.setCellStyle(numberStyle);
							} else {
								R20cell2.setCellValue("");
								R20cell2.setCellStyle(textStyle);

							}

							// row21
							row = sheet.getRow(20);
							// Column B
							Cell R21cell2 = row.getCell(1);
							if (record.getR21_TOTAL() != null) {
								R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
								R21cell2.setCellStyle(numberStyle);
							} else {
								R21cell2.setCellValue("");
								R21cell2.setCellStyle(textStyle);

							}

							// row22
							row = sheet.getRow(21);
							// Column B
							Cell R22cell2 = row.getCell(1);
							if (record.getR22_TOTAL() != null) {
								R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
								R22cell2.setCellStyle(numberStyle);
							} else {
								R22cell2.setCellValue("");
								R22cell2.setCellStyle(textStyle);

							}

							// row23
							row = sheet.getRow(22);
							// Column B
							Cell R23cell2 = row.getCell(1);
							if (record.getR23_TOTAL() != null) {
								R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
								R23cell2.setCellStyle(numberStyle);
							} else {
								R23cell2.setCellValue("");
								R23cell2.setCellStyle(textStyle);

							}

							// row24
							row = sheet.getRow(23);
							// Column B
							Cell R24cell2 = row.getCell(1);
							if (record.getR24_TOTAL() != null) {
								R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
								R24cell2.setCellStyle(numberStyle);
							} else {
								R24cell2.setCellValue("");
								R24cell2.setCellStyle(textStyle);

							}

							// row25
							row = sheet.getRow(24);
							// Column B
							Cell R25cell2 = row.getCell(1);
							if (record.getR25_TOTAL() != null) {
								R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA2 SUMMARY", null,
								"BRRS_M_LA2_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_LA2EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA2ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_LA2ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_LA2_Summary_Entity> dataList = getDataByDate1(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
						M_LA2_Summary_Entity record = dataList.get(i);
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

						// row12
						// Column B
						row = sheet.getRow(11);

						Cell cell2 = row.createCell(1);
						if (record.getR12_TOTAL() != null) {
							cell2.setCellValue(record.getR12_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);
						// Column B
						Cell R13cell2 = row.getCell(1);
						if (record.getR13_TOTAL() != null) {
							R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
							R13cell2.setCellStyle(numberStyle);
						} else {
							R13cell2.setCellValue("");
							R13cell2.setCellStyle(textStyle);

						}

						// row14
						row = sheet.getRow(13);
						// Column B
						Cell R14cell2 = row.getCell(1);
						if (record.getR14_TOTAL() != null) {
							R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
							R14cell2.setCellStyle(numberStyle);
						} else {
							R14cell2.setCellValue("");
							R14cell2.setCellStyle(textStyle);

						}

						// row15
						row = sheet.getRow(14);
						// Column B
						Cell R15cell2 = row.getCell(1);
						if (record.getR15_TOTAL() != null) {
							R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);

						}

						// row16
						row = sheet.getRow(15);
						// Column B
						Cell R16cell2 = row.getCell(1);
						if (record.getR16_TOTAL() != null) {
							R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);

						}

						// row17
						row = sheet.getRow(16);
						// Column B
						Cell R17cell2 = row.getCell(1);
						if (record.getR17_TOTAL() != null) {
							R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
							R17cell2.setCellStyle(numberStyle);
						} else {
							R17cell2.setCellValue("");
							R17cell2.setCellStyle(textStyle);

						}

						// row18
						row = sheet.getRow(17);
						// Column B
						Cell R18cell2 = row.getCell(1);
						if (record.getR18_TOTAL() != null) {
							R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
							R18cell2.setCellStyle(numberStyle);
						} else {
							R18cell2.setCellValue("");
							R18cell2.setCellStyle(textStyle);

						}

						// row19
						row = sheet.getRow(18);
						// Column B
						Cell R19cell2 = row.getCell(1);
						if (record.getR19_TOTAL() != null) {
							R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
							R19cell2.setCellStyle(numberStyle);
						} else {
							R19cell2.setCellValue("");
							R19cell2.setCellStyle(textStyle);

						}

						// row20
						row = sheet.getRow(19);
						// Column B
						Cell R20cell2 = row.getCell(1);
						if (record.getR20_TOTAL() != null) {
							R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
							R20cell2.setCellStyle(numberStyle);
						} else {
							R20cell2.setCellValue("");
							R20cell2.setCellStyle(textStyle);

						}

						// row21
						row = sheet.getRow(20);
						// Column B
						Cell R21cell2 = row.getCell(1);
						if (record.getR21_TOTAL() != null) {
							R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
							R21cell2.setCellStyle(numberStyle);
						} else {
							R21cell2.setCellValue("");
							R21cell2.setCellStyle(textStyle);

						}

						// row22
						row = sheet.getRow(21);
						// Column B
						Cell R22cell2 = row.getCell(1);
						if (record.getR22_TOTAL() != null) {
							R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
							R22cell2.setCellStyle(numberStyle);
						} else {
							R22cell2.setCellValue("");
							R22cell2.setCellStyle(textStyle);

						}

						// row23
						row = sheet.getRow(22);
						// Column B
						Cell R23cell2 = row.getCell(1);
						if (record.getR23_TOTAL() != null) {
							R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
							R23cell2.setCellStyle(numberStyle);
						} else {
							R23cell2.setCellValue("");
							R23cell2.setCellStyle(textStyle);

						}

						// row24
						row = sheet.getRow(23);
						// Column B
						Cell R24cell2 = row.getCell(1);
						if (record.getR24_TOTAL() != null) {
							R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
							R24cell2.setCellStyle(numberStyle);
						} else {
							R24cell2.setCellValue("");
							R24cell2.setCellStyle(textStyle);

						}

						// row25
						row = sheet.getRow(24);
						// Column B
						Cell R25cell2 = row.getCell(1);
						if (record.getR25_TOTAL() != null) {
							R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);

						}

					}
					workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				} else {

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA2 EMAIL SUMMARY", null,
							"BRRS_M_LA2_SUMMARYTABLE");
				}

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_LA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA2ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_LA2_Archival_Summary_Entity> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA2 report. Returning empty result.");
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
					M_LA2_Archival_Summary_Entity record = dataList.get(i);
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

					// row12
					// Column B
					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					Cell R13cell2 = row.getCell(1);
					if (record.getR13_TOTAL() != null) {
						R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);

					}

					// row14
					row = sheet.getRow(13);
					// Column B
					Cell R14cell2 = row.getCell(1);
					if (record.getR14_TOTAL() != null) {
						R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(14);
					// Column B
					Cell R15cell2 = row.getCell(1);
					if (record.getR15_TOTAL() != null) {
						R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);

					}

					// row16
					row = sheet.getRow(15);
					// Column B
					Cell R16cell2 = row.getCell(1);
					if (record.getR16_TOTAL() != null) {
						R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);

					}

					// row17
					row = sheet.getRow(16);
					// Column B
					Cell R17cell2 = row.getCell(1);
					if (record.getR17_TOTAL() != null) {
						R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);

					}

					// row18
					row = sheet.getRow(17);
					// Column B
					Cell R18cell2 = row.getCell(1);
					if (record.getR18_TOTAL() != null) {
						R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);

					}

					// row19
					row = sheet.getRow(18);
					// Column B
					Cell R19cell2 = row.getCell(1);
					if (record.getR19_TOTAL() != null) {
						R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);

					}

					// row20
					row = sheet.getRow(19);
					// Column B
					Cell R20cell2 = row.getCell(1);
					if (record.getR20_TOTAL() != null) {
						R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);

					}

					// row21
					row = sheet.getRow(20);
					// Column B
					Cell R21cell2 = row.getCell(1);
					if (record.getR21_TOTAL() != null) {
						R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);

					}

					// row22
					row = sheet.getRow(21);
					// Column B
					Cell R22cell2 = row.getCell(1);
					if (record.getR22_TOTAL() != null) {
						R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);

					}

					// row23
					row = sheet.getRow(22);
					// Column B
					Cell R23cell2 = row.getCell(1);
					if (record.getR23_TOTAL() != null) {
						R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);

					}

					// row24
					row = sheet.getRow(23);
					// Column B
					Cell R24cell2 = row.getCell(1);
					if (record.getR24_TOTAL() != null) {
						R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);

					}

					// row25
					row = sheet.getRow(24);
					// Column B
					Cell R25cell2 = row.getCell(1);
					if (record.getR25_TOTAL() != null) {
						R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);

					}

				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA2 ARCHIVAL SUMMARY", null,
						"BRRS_M_LA2_ARCHIVALTABLE_SUMMARY");
			}
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_LA2ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA2_Archival_Summary_Entity> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
					M_LA2_Archival_Summary_Entity record = dataList.get(i);
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

					// row12
					// Column B
					row = sheet.getRow(11);
					Cell cell2 = row.createCell(1);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					Cell R13cell2 = row.getCell(1);
					if (record.getR13_TOTAL() != null) {
						R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);

					}

					// row14
					row = sheet.getRow(13);
					// Column B
					Cell R14cell2 = row.getCell(1);
					if (record.getR14_TOTAL() != null) {
						R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(14);
					// Column B
					Cell R15cell2 = row.getCell(1);
					if (record.getR15_TOTAL() != null) {
						R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);

					}

					// row16
					row = sheet.getRow(15);
					// Column B
					Cell R16cell2 = row.getCell(1);
					if (record.getR16_TOTAL() != null) {
						R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);

					}

					// row17
					row = sheet.getRow(16);
					// Column B
					Cell R17cell2 = row.getCell(1);
					if (record.getR17_TOTAL() != null) {
						R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);

					}

					// row18
					row = sheet.getRow(17);
					// Column B
					Cell R18cell2 = row.getCell(1);
					if (record.getR18_TOTAL() != null) {
						R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);

					}

					// row19
					row = sheet.getRow(18);
					// Column B
					Cell R19cell2 = row.getCell(1);
					if (record.getR19_TOTAL() != null) {
						R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);

					}

					// row20
					row = sheet.getRow(19);
					// Column B
					Cell R20cell2 = row.getCell(1);
					if (record.getR20_TOTAL() != null) {
						R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);

					}

					// row21
					row = sheet.getRow(20);
					// Column B
					Cell R21cell2 = row.getCell(1);
					if (record.getR21_TOTAL() != null) {
						R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);

					}

					// row22
					row = sheet.getRow(21);
					// Column B
					Cell R22cell2 = row.getCell(1);
					if (record.getR22_TOTAL() != null) {
						R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);

					}

					// row23
					row = sheet.getRow(22);
					// Column B
					Cell R23cell2 = row.getCell(1);
					if (record.getR23_TOTAL() != null) {
						R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);

					}

					// row24
					row = sheet.getRow(23);
					// Column B
					Cell R24cell2 = row.getCell(1);
					if (record.getR24_TOTAL() != null) {
						R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);

					}

					// row25
					row = sheet.getRow(24);
					// Column B
					Cell R25cell2 = row.getCell(1);
					if (record.getR25_TOTAL() != null) {
						R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA2 EMAIL ARCHIVALSUMMARY", null,
						"BRRS_M_LA2_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_LA2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_LA2ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_LA2_RESUB_Summary_Entity> dataList = getdatabydateListresub1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA2 report. Returning empty result.");
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

					M_LA2_RESUB_Summary_Entity record = dataList.get(i);
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

					// row12
					// Column B
					row = sheet.getRow(11);
					Cell cell2 = row.createCell(1);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					Cell R13cell2 = row.getCell(1);
					if (record.getR13_TOTAL() != null) {
						R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);

					}

					// row14
					row = sheet.getRow(13);
					// Column B
					Cell R14cell2 = row.getCell(1);
					if (record.getR14_TOTAL() != null) {
						R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(14);
					// Column B
					Cell R15cell2 = row.getCell(1);
					if (record.getR15_TOTAL() != null) {
						R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);

					}

					// row16
					row = sheet.getRow(15);
					// Column B
					Cell R16cell2 = row.getCell(1);
					if (record.getR16_TOTAL() != null) {
						R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);

					}

					// row17
					row = sheet.getRow(16);
					// Column B
					Cell R17cell2 = row.getCell(1);
					if (record.getR17_TOTAL() != null) {
						R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);

					}

					// row18
					row = sheet.getRow(17);
					// Column B
					Cell R18cell2 = row.getCell(1);
					if (record.getR18_TOTAL() != null) {
						R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);

					}

					// row19
					row = sheet.getRow(18);
					// Column B
					Cell R19cell2 = row.getCell(1);
					if (record.getR19_TOTAL() != null) {
						R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);

					}

					// row20
					row = sheet.getRow(19);
					// Column B
					Cell R20cell2 = row.getCell(1);
					if (record.getR20_TOTAL() != null) {
						R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);

					}

					// row21
					row = sheet.getRow(20);
					// Column B
					Cell R21cell2 = row.getCell(1);
					if (record.getR21_TOTAL() != null) {
						R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);

					}

					// row22
					row = sheet.getRow(21);
					// Column B
					Cell R22cell2 = row.getCell(1);
					if (record.getR22_TOTAL() != null) {
						R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);

					}

					// row23
					row = sheet.getRow(22);
					// Column B
					Cell R23cell2 = row.getCell(1);
					if (record.getR23_TOTAL() != null) {
						R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);

					}

					// row24
					row = sheet.getRow(23);
					// Column B
					Cell R24cell2 = row.getCell(1);
					if (record.getR24_TOTAL() != null) {
						R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);

					}

					// row25
					row = sheet.getRow(24);
					// Column B
					Cell R25cell2 = row.getCell(1);
					if (record.getR25_TOTAL() != null) {
						R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA2 RESUB SUMMARY", null,
						"BRRS_M_LA2_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_LA2ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA2_RESUB_Summary_Entity> dataList = getdatabydateListresub1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA2 report. Returning empty result.");
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
					M_LA2_RESUB_Summary_Entity record = dataList.get(i);
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

					// row12
					// Column B
					row = sheet.getRow(11);
					Cell cell2 = row.createCell(1);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column B
					Cell R13cell2 = row.getCell(1);
					if (record.getR13_TOTAL() != null) {
						R13cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);

					}

					// row14
					row = sheet.getRow(13);
					// Column B
					Cell R14cell2 = row.getCell(1);
					if (record.getR14_TOTAL() != null) {
						R14cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(14);
					// Column B
					Cell R15cell2 = row.getCell(1);
					if (record.getR15_TOTAL() != null) {
						R15cell2.setCellValue(record.getR15_TOTAL().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);

					}

					// row16
					row = sheet.getRow(15);
					// Column B
					Cell R16cell2 = row.getCell(1);
					if (record.getR16_TOTAL() != null) {
						R16cell2.setCellValue(record.getR16_TOTAL().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);

					}

					// row17
					row = sheet.getRow(16);
					// Column B
					Cell R17cell2 = row.getCell(1);
					if (record.getR17_TOTAL() != null) {
						R17cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);

					}

					// row18
					row = sheet.getRow(17);
					// Column B
					Cell R18cell2 = row.getCell(1);
					if (record.getR18_TOTAL() != null) {
						R18cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);

					}

					// row19
					row = sheet.getRow(18);
					// Column B
					Cell R19cell2 = row.getCell(1);
					if (record.getR19_TOTAL() != null) {
						R19cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);

					}

					// row20
					row = sheet.getRow(19);
					// Column B
					Cell R20cell2 = row.getCell(1);
					if (record.getR20_TOTAL() != null) {
						R20cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);

					}

					// row21
					row = sheet.getRow(20);
					// Column B
					Cell R21cell2 = row.getCell(1);
					if (record.getR21_TOTAL() != null) {
						R21cell2.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);

					}

					// row22
					row = sheet.getRow(21);
					// Column B
					Cell R22cell2 = row.getCell(1);
					if (record.getR22_TOTAL() != null) {
						R22cell2.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);

					}

					// row23
					row = sheet.getRow(22);
					// Column B
					Cell R23cell2 = row.getCell(1);
					if (record.getR23_TOTAL() != null) {
						R23cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);

					}

					// row24
					row = sheet.getRow(23);
					// Column B
					Cell R24cell2 = row.getCell(1);
					if (record.getR24_TOTAL() != null) {
						R24cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);

					}

					// row25
					row = sheet.getRow(24);
					// Column B
					Cell R25cell2 = row.getCell(1);
					if (record.getR25_TOTAL() != null) {
						R25cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA2 EMAIL RESUBSUMMARY", null,
						"BRRS_M_LA2_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

}