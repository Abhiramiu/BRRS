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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.IdClass;
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
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_FSI_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_FSI_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	// Fetch data by report date
	public List<FSI_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_FSI_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new FSIRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getFSIArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_FSI_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<FSI_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_FSI_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new FSIArchivalRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_FSI_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}
//GET ALL WITH VERSION

	public List<FSI_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_FSI_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new FSIArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_FSI_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<FSI_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_FSI_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new FSIDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<FSI_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_FSI_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new FSIDetailRowMapper());
	}

// 3. PAGINATION

	public List<FSI_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_FSI_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new FSIDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_FSI_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<FSI_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_FSI_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new FSIDetailRowMapper());
	}

	public FSI_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_FSI_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new FSIDetailRowMapper());
	}

	public FSI_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_FSI_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new FSIDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<FSI_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_FSI_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new FSIArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<FSI_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_FSI_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new FSIArchivalDetailRowMapper());
	}

	class FSIRowMapper implements RowMapper<FSI_Summary_Entity> {

		@Override
		public FSI_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FSI_Summary_Entity obj = new FSI_Summary_Entity();

			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));
			obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));
			obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));
			obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));
			obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));
			obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));
			obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

			obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));
			obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));
			obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR47_AMOUNT(rs.getBigDecimal("R47_AMOUNT"));
			obj.setR48_AMOUNT(rs.getBigDecimal("R48_AMOUNT"));
			obj.setR49_AMOUNT(rs.getBigDecimal("R49_AMOUNT"));
			obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR56_AMOUNT(rs.getBigDecimal("R56_AMOUNT"));
			obj.setR57_AMOUNT(rs.getBigDecimal("R57_AMOUNT"));
			obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			obj.setR61_AMOUNT(rs.getBigDecimal("R61_AMOUNT"));
			obj.setR62_AMOUNT(rs.getBigDecimal("R62_AMOUNT"));
			obj.setR63_AMOUNT(rs.getBigDecimal("R63_AMOUNT"));
			obj.setR64_AMOUNT(rs.getBigDecimal("R64_AMOUNT"));
			obj.setR65_AMOUNT(rs.getBigDecimal("R65_AMOUNT"));
			obj.setR66_AMOUNT(rs.getBigDecimal("R66_AMOUNT"));
			obj.setR67_AMOUNT(rs.getBigDecimal("R67_AMOUNT"));
			obj.setR68_AMOUNT(rs.getBigDecimal("R68_AMOUNT"));
			obj.setR69_AMOUNT(rs.getBigDecimal("R69_AMOUNT"));
			obj.setR70_AMOUNT(rs.getBigDecimal("R70_AMOUNT"));
			obj.setR71_AMOUNT(rs.getBigDecimal("R71_AMOUNT"));
			obj.setR72_AMOUNT(rs.getBigDecimal("R72_AMOUNT"));
			obj.setR73_AMOUNT(rs.getBigDecimal("R73_AMOUNT"));
			obj.setR74_AMOUNT(rs.getBigDecimal("R74_AMOUNT"));
			obj.setR75_AMOUNT(rs.getBigDecimal("R75_AMOUNT"));
			obj.setR76_AMOUNT(rs.getBigDecimal("R76_AMOUNT"));
			obj.setR77_AMOUNT(rs.getBigDecimal("R77_AMOUNT"));
			obj.setR86_AMOUNT(rs.getBigDecimal("R86_AMOUNT"));
			obj.setR87_AMOUNT(rs.getBigDecimal("R87_AMOUNT"));
			obj.setR88_AMOUNT(rs.getBigDecimal("R88_AMOUNT"));
			obj.setR89_AMOUNT(rs.getBigDecimal("R89_AMOUNT"));
			obj.setR90_AMOUNT(rs.getBigDecimal("R90_AMOUNT"));
			obj.setR91_AMOUNT(rs.getBigDecimal("R91_AMOUNT"));
			obj.setR92_AMOUNT(rs.getBigDecimal("R92_AMOUNT"));
			obj.setR93_AMOUNT(rs.getBigDecimal("R93_AMOUNT"));
			obj.setR94_AMOUNT(rs.getBigDecimal("R94_AMOUNT"));
			obj.setR95_AMOUNT(rs.getBigDecimal("R95_AMOUNT"));
			obj.setR96_AMOUNT(rs.getBigDecimal("R96_AMOUNT"));
			obj.setR97_AMOUNT(rs.getBigDecimal("R97_AMOUNT"));
			obj.setR98_AMOUNT(rs.getBigDecimal("R98_AMOUNT"));
			obj.setR99_AMOUNT(rs.getBigDecimal("R99_AMOUNT"));
			obj.setR100_AMOUNT(rs.getBigDecimal("R100_AMOUNT"));
			obj.setR101_AMOUNT(rs.getBigDecimal("R101_AMOUNT"));
			obj.setR102_AMOUNT(rs.getBigDecimal("R102_AMOUNT"));
			obj.setR103_AMOUNT(rs.getBigDecimal("R103_AMOUNT"));
			obj.setR104_AMOUNT(rs.getBigDecimal("R104_AMOUNT"));
			obj.setR105_AMOUNT(rs.getBigDecimal("R105_AMOUNT"));
			obj.setR106_AMOUNT(rs.getBigDecimal("R106_AMOUNT"));
			obj.setR107_AMOUNT(rs.getBigDecimal("R107_AMOUNT"));
			obj.setR108_AMOUNT(rs.getBigDecimal("R108_AMOUNT"));
			obj.setR109_AMOUNT(rs.getBigDecimal("R109_AMOUNT"));
			obj.setR110_AMOUNT(rs.getBigDecimal("R110_AMOUNT"));
			obj.setR111_AMOUNT(rs.getBigDecimal("R111_AMOUNT"));
			obj.setR112_AMOUNT(rs.getBigDecimal("R112_AMOUNT"));
			obj.setR113_AMOUNT(rs.getBigDecimal("R113_AMOUNT"));
			obj.setR114_AMOUNT(rs.getBigDecimal("R114_AMOUNT"));
			obj.setR115_AMOUNT(rs.getBigDecimal("R115_AMOUNT"));
			obj.setR116_AMOUNT(rs.getBigDecimal("R116_AMOUNT"));
			obj.setR117_AMOUNT(rs.getBigDecimal("R117_AMOUNT"));
			obj.setR118_AMOUNT(rs.getBigDecimal("R118_AMOUNT"));
			obj.setR119_AMOUNT(rs.getBigDecimal("R119_AMOUNT"));
			obj.setR120_AMOUNT(rs.getBigDecimal("R120_AMOUNT"));
			obj.setR121_AMOUNT(rs.getBigDecimal("R121_AMOUNT"));
			obj.setR122_AMOUNT(rs.getBigDecimal("R122_AMOUNT"));
			obj.setR123_AMOUNT(rs.getBigDecimal("R123_AMOUNT"));
			obj.setR124_AMOUNT(rs.getBigDecimal("R124_AMOUNT"));
			obj.setR125_AMOUNT(rs.getBigDecimal("R125_AMOUNT"));
			obj.setR126_AMOUNT(rs.getBigDecimal("R126_AMOUNT"));
			obj.setR127_AMOUNT(rs.getBigDecimal("R127_AMOUNT"));
			obj.setR128_AMOUNT(rs.getBigDecimal("R128_AMOUNT"));
			obj.setR129_AMOUNT(rs.getBigDecimal("R129_AMOUNT"));

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

	public static class FSI_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private BigDecimal R11_AMOUNT;
		private BigDecimal R12_AMOUNT;
		private BigDecimal R13_AMOUNT;
		private BigDecimal R14_AMOUNT;
		private BigDecimal R15_AMOUNT;
		private BigDecimal R16_AMOUNT;
		private BigDecimal R17_AMOUNT;
		private BigDecimal R18_AMOUNT;
		private BigDecimal R19_AMOUNT;
		private BigDecimal R20_AMOUNT;
		private BigDecimal R21_AMOUNT;
		private BigDecimal R22_AMOUNT;
		private BigDecimal R23_AMOUNT;
		private BigDecimal R24_AMOUNT;
		private BigDecimal R25_AMOUNT;
		private BigDecimal R26_AMOUNT;
		private BigDecimal R27_AMOUNT;
		private BigDecimal R28_AMOUNT;
		private BigDecimal R29_AMOUNT;
		private BigDecimal R30_AMOUNT;
		private BigDecimal R31_AMOUNT;
		private BigDecimal R32_AMOUNT;
		private BigDecimal R33_AMOUNT;

		private BigDecimal R39_AMOUNT;
		private BigDecimal R40_AMOUNT;
		private BigDecimal R41_AMOUNT;
		private BigDecimal R42_AMOUNT;
		private BigDecimal R43_AMOUNT;
		private BigDecimal R44_AMOUNT;
		private BigDecimal R45_AMOUNT;
		private BigDecimal R46_AMOUNT;
		private BigDecimal R47_AMOUNT;
		private BigDecimal R48_AMOUNT;
		private BigDecimal R49_AMOUNT;
		private BigDecimal R50_AMOUNT;
		private BigDecimal R51_AMOUNT;
		private BigDecimal R52_AMOUNT;
		private BigDecimal R53_AMOUNT;
		private BigDecimal R54_AMOUNT;
		private BigDecimal R55_AMOUNT;
		private BigDecimal R56_AMOUNT;
		private BigDecimal R57_AMOUNT;
		private BigDecimal R58_AMOUNT;
		private BigDecimal R59_AMOUNT;
		private BigDecimal R60_AMOUNT;
		private BigDecimal R61_AMOUNT;
		private BigDecimal R62_AMOUNT;
		private BigDecimal R63_AMOUNT;
		private BigDecimal R64_AMOUNT;
		private BigDecimal R65_AMOUNT;
		private BigDecimal R66_AMOUNT;
		private BigDecimal R67_AMOUNT;
		private BigDecimal R68_AMOUNT;
		private BigDecimal R69_AMOUNT;
		private BigDecimal R70_AMOUNT;
		private BigDecimal R71_AMOUNT;
		private BigDecimal R72_AMOUNT;
		private BigDecimal R73_AMOUNT;
		private BigDecimal R74_AMOUNT;
		private BigDecimal R75_AMOUNT;
		private BigDecimal R76_AMOUNT;
		private BigDecimal R77_AMOUNT;
		private BigDecimal R86_AMOUNT;
		private BigDecimal R87_AMOUNT;
		private BigDecimal R88_AMOUNT;
		private BigDecimal R89_AMOUNT;
		private BigDecimal R90_AMOUNT;
		private BigDecimal R91_AMOUNT;
		private BigDecimal R92_AMOUNT;
		private BigDecimal R93_AMOUNT;
		private BigDecimal R94_AMOUNT;
		private BigDecimal R95_AMOUNT;
		private BigDecimal R96_AMOUNT;
		private BigDecimal R97_AMOUNT;
		private BigDecimal R98_AMOUNT;
		private BigDecimal R99_AMOUNT;
		private BigDecimal R100_AMOUNT;
		private BigDecimal R101_AMOUNT;
		private BigDecimal R102_AMOUNT;
		private BigDecimal R103_AMOUNT;
		private BigDecimal R104_AMOUNT;
		private BigDecimal R105_AMOUNT;
		private BigDecimal R106_AMOUNT;
		private BigDecimal R107_AMOUNT;
		private BigDecimal R108_AMOUNT;
		private BigDecimal R109_AMOUNT;
		private BigDecimal R110_AMOUNT;
		private BigDecimal R111_AMOUNT;
		private BigDecimal R112_AMOUNT;
		private BigDecimal R113_AMOUNT;
		private BigDecimal R114_AMOUNT;
		private BigDecimal R115_AMOUNT;
		private BigDecimal R116_AMOUNT;
		private BigDecimal R117_AMOUNT;
		private BigDecimal R118_AMOUNT;
		private BigDecimal R119_AMOUNT;
		private BigDecimal R120_AMOUNT;
		private BigDecimal R121_AMOUNT;
		private BigDecimal R122_AMOUNT;
		private BigDecimal R123_AMOUNT;
		private BigDecimal R124_AMOUNT;
		private BigDecimal R125_AMOUNT;
		private BigDecimal R126_AMOUNT;
		private BigDecimal R127_AMOUNT;
		private BigDecimal R128_AMOUNT;
		private BigDecimal R129_AMOUNT;

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

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getR11_AMOUNT() {
			return R11_AMOUNT;
		}

		public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
			R11_AMOUNT = r11_AMOUNT;
		}

		public BigDecimal getR12_AMOUNT() {
			return R12_AMOUNT;
		}

		public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
			R12_AMOUNT = r12_AMOUNT;
		}

		public BigDecimal getR13_AMOUNT() {
			return R13_AMOUNT;
		}

		public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
			R13_AMOUNT = r13_AMOUNT;
		}

		public BigDecimal getR14_AMOUNT() {
			return R14_AMOUNT;
		}

		public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
			R14_AMOUNT = r14_AMOUNT;
		}

		public BigDecimal getR15_AMOUNT() {
			return R15_AMOUNT;
		}

		public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
			R15_AMOUNT = r15_AMOUNT;
		}

		public BigDecimal getR16_AMOUNT() {
			return R16_AMOUNT;
		}

		public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
			R16_AMOUNT = r16_AMOUNT;
		}

		public BigDecimal getR17_AMOUNT() {
			return R17_AMOUNT;
		}

		public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
			R17_AMOUNT = r17_AMOUNT;
		}

		public BigDecimal getR18_AMOUNT() {
			return R18_AMOUNT;
		}

		public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
			R18_AMOUNT = r18_AMOUNT;
		}

		public BigDecimal getR19_AMOUNT() {
			return R19_AMOUNT;
		}

		public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
			R19_AMOUNT = r19_AMOUNT;
		}

		public BigDecimal getR20_AMOUNT() {
			return R20_AMOUNT;
		}

		public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
			R20_AMOUNT = r20_AMOUNT;
		}

		public BigDecimal getR21_AMOUNT() {
			return R21_AMOUNT;
		}

		public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
			R21_AMOUNT = r21_AMOUNT;
		}

		public BigDecimal getR22_AMOUNT() {
			return R22_AMOUNT;
		}

		public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
			R22_AMOUNT = r22_AMOUNT;
		}

		public BigDecimal getR23_AMOUNT() {
			return R23_AMOUNT;
		}

		public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
			R23_AMOUNT = r23_AMOUNT;
		}

		public BigDecimal getR24_AMOUNT() {
			return R24_AMOUNT;
		}

		public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
			R24_AMOUNT = r24_AMOUNT;
		}

		public BigDecimal getR25_AMOUNT() {
			return R25_AMOUNT;
		}

		public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
			R25_AMOUNT = r25_AMOUNT;
		}

		public BigDecimal getR26_AMOUNT() {
			return R26_AMOUNT;
		}

		public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
			R26_AMOUNT = r26_AMOUNT;
		}

		public BigDecimal getR27_AMOUNT() {
			return R27_AMOUNT;
		}

		public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
			R27_AMOUNT = r27_AMOUNT;
		}

		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}

		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}

		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}

		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}

		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}

		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}

		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}

		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}

		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}

		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}

		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}

		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}

		public BigDecimal getR39_AMOUNT() {
			return R39_AMOUNT;
		}

		public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
			R39_AMOUNT = r39_AMOUNT;
		}

		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}

		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}

		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}

		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}

		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}

		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}

		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}

		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}

		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}

		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}

		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}

		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}

		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}

		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}

		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}

		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
		}

		public BigDecimal getR48_AMOUNT() {
			return R48_AMOUNT;
		}

		public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
			R48_AMOUNT = r48_AMOUNT;
		}

		public BigDecimal getR49_AMOUNT() {
			return R49_AMOUNT;
		}

		public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
			R49_AMOUNT = r49_AMOUNT;
		}

		public BigDecimal getR50_AMOUNT() {
			return R50_AMOUNT;
		}

		public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
			R50_AMOUNT = r50_AMOUNT;
		}

		public BigDecimal getR51_AMOUNT() {
			return R51_AMOUNT;
		}

		public void setR51_AMOUNT(BigDecimal r51_AMOUNT) {
			R51_AMOUNT = r51_AMOUNT;
		}

		public BigDecimal getR52_AMOUNT() {
			return R52_AMOUNT;
		}

		public void setR52_AMOUNT(BigDecimal r52_AMOUNT) {
			R52_AMOUNT = r52_AMOUNT;
		}

		public BigDecimal getR53_AMOUNT() {
			return R53_AMOUNT;
		}

		public void setR53_AMOUNT(BigDecimal r53_AMOUNT) {
			R53_AMOUNT = r53_AMOUNT;
		}

		public BigDecimal getR54_AMOUNT() {
			return R54_AMOUNT;
		}

		public void setR54_AMOUNT(BigDecimal r54_AMOUNT) {
			R54_AMOUNT = r54_AMOUNT;
		}

		public BigDecimal getR55_AMOUNT() {
			return R55_AMOUNT;
		}

		public void setR55_AMOUNT(BigDecimal r55_AMOUNT) {
			R55_AMOUNT = r55_AMOUNT;
		}

		public BigDecimal getR56_AMOUNT() {
			return R56_AMOUNT;
		}

		public void setR56_AMOUNT(BigDecimal r56_AMOUNT) {
			R56_AMOUNT = r56_AMOUNT;
		}

		public BigDecimal getR57_AMOUNT() {
			return R57_AMOUNT;
		}

		public void setR57_AMOUNT(BigDecimal r57_AMOUNT) {
			R57_AMOUNT = r57_AMOUNT;
		}

		public BigDecimal getR58_AMOUNT() {
			return R58_AMOUNT;
		}

		public void setR58_AMOUNT(BigDecimal r58_AMOUNT) {
			R58_AMOUNT = r58_AMOUNT;
		}

		public BigDecimal getR59_AMOUNT() {
			return R59_AMOUNT;
		}

		public void setR59_AMOUNT(BigDecimal r59_AMOUNT) {
			R59_AMOUNT = r59_AMOUNT;
		}

		public BigDecimal getR60_AMOUNT() {
			return R60_AMOUNT;
		}

		public void setR60_AMOUNT(BigDecimal r60_AMOUNT) {
			R60_AMOUNT = r60_AMOUNT;
		}

		public BigDecimal getR61_AMOUNT() {
			return R61_AMOUNT;
		}

		public void setR61_AMOUNT(BigDecimal r61_AMOUNT) {
			R61_AMOUNT = r61_AMOUNT;
		}

		public BigDecimal getR62_AMOUNT() {
			return R62_AMOUNT;
		}

		public void setR62_AMOUNT(BigDecimal r62_AMOUNT) {
			R62_AMOUNT = r62_AMOUNT;
		}

		public BigDecimal getR63_AMOUNT() {
			return R63_AMOUNT;
		}

		public void setR63_AMOUNT(BigDecimal r63_AMOUNT) {
			R63_AMOUNT = r63_AMOUNT;
		}

		public BigDecimal getR64_AMOUNT() {
			return R64_AMOUNT;
		}

		public void setR64_AMOUNT(BigDecimal r64_AMOUNT) {
			R64_AMOUNT = r64_AMOUNT;
		}

		public BigDecimal getR65_AMOUNT() {
			return R65_AMOUNT;
		}

		public void setR65_AMOUNT(BigDecimal r65_AMOUNT) {
			R65_AMOUNT = r65_AMOUNT;
		}

		public BigDecimal getR66_AMOUNT() {
			return R66_AMOUNT;
		}

		public void setR66_AMOUNT(BigDecimal r66_AMOUNT) {
			R66_AMOUNT = r66_AMOUNT;
		}

		public BigDecimal getR67_AMOUNT() {
			return R67_AMOUNT;
		}

		public void setR67_AMOUNT(BigDecimal r67_AMOUNT) {
			R67_AMOUNT = r67_AMOUNT;
		}

		public BigDecimal getR68_AMOUNT() {
			return R68_AMOUNT;
		}

		public void setR68_AMOUNT(BigDecimal r68_AMOUNT) {
			R68_AMOUNT = r68_AMOUNT;
		}

		public BigDecimal getR69_AMOUNT() {
			return R69_AMOUNT;
		}

		public void setR69_AMOUNT(BigDecimal r69_AMOUNT) {
			R69_AMOUNT = r69_AMOUNT;
		}

		public BigDecimal getR70_AMOUNT() {
			return R70_AMOUNT;
		}

		public void setR70_AMOUNT(BigDecimal r70_AMOUNT) {
			R70_AMOUNT = r70_AMOUNT;
		}

		public BigDecimal getR71_AMOUNT() {
			return R71_AMOUNT;
		}

		public void setR71_AMOUNT(BigDecimal r71_AMOUNT) {
			R71_AMOUNT = r71_AMOUNT;
		}

		public BigDecimal getR72_AMOUNT() {
			return R72_AMOUNT;
		}

		public void setR72_AMOUNT(BigDecimal r72_AMOUNT) {
			R72_AMOUNT = r72_AMOUNT;
		}

		public BigDecimal getR73_AMOUNT() {
			return R73_AMOUNT;
		}

		public void setR73_AMOUNT(BigDecimal r73_AMOUNT) {
			R73_AMOUNT = r73_AMOUNT;
		}

		public BigDecimal getR74_AMOUNT() {
			return R74_AMOUNT;
		}

		public void setR74_AMOUNT(BigDecimal r74_AMOUNT) {
			R74_AMOUNT = r74_AMOUNT;
		}

		public BigDecimal getR75_AMOUNT() {
			return R75_AMOUNT;
		}

		public void setR75_AMOUNT(BigDecimal r75_AMOUNT) {
			R75_AMOUNT = r75_AMOUNT;
		}

		public BigDecimal getR76_AMOUNT() {
			return R76_AMOUNT;
		}

		public void setR76_AMOUNT(BigDecimal r76_AMOUNT) {
			R76_AMOUNT = r76_AMOUNT;
		}

		public BigDecimal getR77_AMOUNT() {
			return R77_AMOUNT;
		}

		public void setR77_AMOUNT(BigDecimal r77_AMOUNT) {
			R77_AMOUNT = r77_AMOUNT;
		}

		public BigDecimal getR86_AMOUNT() {
			return R86_AMOUNT;
		}

		public void setR86_AMOUNT(BigDecimal r86_AMOUNT) {
			R86_AMOUNT = r86_AMOUNT;
		}

		public BigDecimal getR87_AMOUNT() {
			return R87_AMOUNT;
		}

		public void setR87_AMOUNT(BigDecimal r87_AMOUNT) {
			R87_AMOUNT = r87_AMOUNT;
		}

		public BigDecimal getR88_AMOUNT() {
			return R88_AMOUNT;
		}

		public void setR88_AMOUNT(BigDecimal r88_AMOUNT) {
			R88_AMOUNT = r88_AMOUNT;
		}

		public BigDecimal getR89_AMOUNT() {
			return R89_AMOUNT;
		}

		public void setR89_AMOUNT(BigDecimal r89_AMOUNT) {
			R89_AMOUNT = r89_AMOUNT;
		}

		public BigDecimal getR90_AMOUNT() {
			return R90_AMOUNT;
		}

		public void setR90_AMOUNT(BigDecimal r90_AMOUNT) {
			R90_AMOUNT = r90_AMOUNT;
		}

		public BigDecimal getR91_AMOUNT() {
			return R91_AMOUNT;
		}

		public void setR91_AMOUNT(BigDecimal r91_AMOUNT) {
			R91_AMOUNT = r91_AMOUNT;
		}

		public BigDecimal getR92_AMOUNT() {
			return R92_AMOUNT;
		}

		public void setR92_AMOUNT(BigDecimal r92_AMOUNT) {
			R92_AMOUNT = r92_AMOUNT;
		}

		public BigDecimal getR93_AMOUNT() {
			return R93_AMOUNT;
		}

		public void setR93_AMOUNT(BigDecimal r93_AMOUNT) {
			R93_AMOUNT = r93_AMOUNT;
		}

		public BigDecimal getR94_AMOUNT() {
			return R94_AMOUNT;
		}

		public void setR94_AMOUNT(BigDecimal r94_AMOUNT) {
			R94_AMOUNT = r94_AMOUNT;
		}

		public BigDecimal getR95_AMOUNT() {
			return R95_AMOUNT;
		}

		public void setR95_AMOUNT(BigDecimal r95_AMOUNT) {
			R95_AMOUNT = r95_AMOUNT;
		}

		public BigDecimal getR96_AMOUNT() {
			return R96_AMOUNT;
		}

		public void setR96_AMOUNT(BigDecimal r96_AMOUNT) {
			R96_AMOUNT = r96_AMOUNT;
		}

		public BigDecimal getR97_AMOUNT() {
			return R97_AMOUNT;
		}

		public void setR97_AMOUNT(BigDecimal r97_AMOUNT) {
			R97_AMOUNT = r97_AMOUNT;
		}

		public BigDecimal getR98_AMOUNT() {
			return R98_AMOUNT;
		}

		public void setR98_AMOUNT(BigDecimal r98_AMOUNT) {
			R98_AMOUNT = r98_AMOUNT;
		}

		public BigDecimal getR99_AMOUNT() {
			return R99_AMOUNT;
		}

		public void setR99_AMOUNT(BigDecimal r99_AMOUNT) {
			R99_AMOUNT = r99_AMOUNT;
		}

		public BigDecimal getR100_AMOUNT() {
			return R100_AMOUNT;
		}

		public void setR100_AMOUNT(BigDecimal r100_AMOUNT) {
			R100_AMOUNT = r100_AMOUNT;
		}

		public BigDecimal getR101_AMOUNT() {
			return R101_AMOUNT;
		}

		public void setR101_AMOUNT(BigDecimal r101_AMOUNT) {
			R101_AMOUNT = r101_AMOUNT;
		}

		public BigDecimal getR102_AMOUNT() {
			return R102_AMOUNT;
		}

		public void setR102_AMOUNT(BigDecimal r102_AMOUNT) {
			R102_AMOUNT = r102_AMOUNT;
		}

		public BigDecimal getR103_AMOUNT() {
			return R103_AMOUNT;
		}

		public void setR103_AMOUNT(BigDecimal r103_AMOUNT) {
			R103_AMOUNT = r103_AMOUNT;
		}

		public BigDecimal getR104_AMOUNT() {
			return R104_AMOUNT;
		}

		public void setR104_AMOUNT(BigDecimal r104_AMOUNT) {
			R104_AMOUNT = r104_AMOUNT;
		}

		public BigDecimal getR105_AMOUNT() {
			return R105_AMOUNT;
		}

		public void setR105_AMOUNT(BigDecimal r105_AMOUNT) {
			R105_AMOUNT = r105_AMOUNT;
		}

		public BigDecimal getR106_AMOUNT() {
			return R106_AMOUNT;
		}

		public void setR106_AMOUNT(BigDecimal r106_AMOUNT) {
			R106_AMOUNT = r106_AMOUNT;
		}

		public BigDecimal getR107_AMOUNT() {
			return R107_AMOUNT;
		}

		public void setR107_AMOUNT(BigDecimal r107_AMOUNT) {
			R107_AMOUNT = r107_AMOUNT;
		}

		public BigDecimal getR108_AMOUNT() {
			return R108_AMOUNT;
		}

		public void setR108_AMOUNT(BigDecimal r108_AMOUNT) {
			R108_AMOUNT = r108_AMOUNT;
		}

		public BigDecimal getR109_AMOUNT() {
			return R109_AMOUNT;
		}

		public void setR109_AMOUNT(BigDecimal r109_AMOUNT) {
			R109_AMOUNT = r109_AMOUNT;
		}

		public BigDecimal getR110_AMOUNT() {
			return R110_AMOUNT;
		}

		public void setR110_AMOUNT(BigDecimal r110_AMOUNT) {
			R110_AMOUNT = r110_AMOUNT;
		}

		public BigDecimal getR111_AMOUNT() {
			return R111_AMOUNT;
		}

		public void setR111_AMOUNT(BigDecimal r111_AMOUNT) {
			R111_AMOUNT = r111_AMOUNT;
		}

		public BigDecimal getR112_AMOUNT() {
			return R112_AMOUNT;
		}

		public void setR112_AMOUNT(BigDecimal r112_AMOUNT) {
			R112_AMOUNT = r112_AMOUNT;
		}

		public BigDecimal getR113_AMOUNT() {
			return R113_AMOUNT;
		}

		public void setR113_AMOUNT(BigDecimal r113_AMOUNT) {
			R113_AMOUNT = r113_AMOUNT;
		}

		public BigDecimal getR114_AMOUNT() {
			return R114_AMOUNT;
		}

		public void setR114_AMOUNT(BigDecimal r114_AMOUNT) {
			R114_AMOUNT = r114_AMOUNT;
		}

		public BigDecimal getR115_AMOUNT() {
			return R115_AMOUNT;
		}

		public void setR115_AMOUNT(BigDecimal r115_AMOUNT) {
			R115_AMOUNT = r115_AMOUNT;
		}

		public BigDecimal getR116_AMOUNT() {
			return R116_AMOUNT;
		}

		public void setR116_AMOUNT(BigDecimal r116_AMOUNT) {
			R116_AMOUNT = r116_AMOUNT;
		}

		public BigDecimal getR117_AMOUNT() {
			return R117_AMOUNT;
		}

		public void setR117_AMOUNT(BigDecimal r117_AMOUNT) {
			R117_AMOUNT = r117_AMOUNT;
		}

		public BigDecimal getR118_AMOUNT() {
			return R118_AMOUNT;
		}

		public void setR118_AMOUNT(BigDecimal r118_AMOUNT) {
			R118_AMOUNT = r118_AMOUNT;
		}

		public BigDecimal getR119_AMOUNT() {
			return R119_AMOUNT;
		}

		public void setR119_AMOUNT(BigDecimal r119_AMOUNT) {
			R119_AMOUNT = r119_AMOUNT;
		}

		public BigDecimal getR120_AMOUNT() {
			return R120_AMOUNT;
		}

		public void setR120_AMOUNT(BigDecimal r120_AMOUNT) {
			R120_AMOUNT = r120_AMOUNT;
		}

		public BigDecimal getR121_AMOUNT() {
			return R121_AMOUNT;
		}

		public void setR121_AMOUNT(BigDecimal r121_AMOUNT) {
			R121_AMOUNT = r121_AMOUNT;
		}

		public BigDecimal getR122_AMOUNT() {
			return R122_AMOUNT;
		}

		public void setR122_AMOUNT(BigDecimal r122_AMOUNT) {
			R122_AMOUNT = r122_AMOUNT;
		}

		public BigDecimal getR123_AMOUNT() {
			return R123_AMOUNT;
		}

		public void setR123_AMOUNT(BigDecimal r123_AMOUNT) {
			R123_AMOUNT = r123_AMOUNT;
		}

		public BigDecimal getR124_AMOUNT() {
			return R124_AMOUNT;
		}

		public void setR124_AMOUNT(BigDecimal r124_AMOUNT) {
			R124_AMOUNT = r124_AMOUNT;
		}

		public BigDecimal getR125_AMOUNT() {
			return R125_AMOUNT;
		}

		public void setR125_AMOUNT(BigDecimal r125_AMOUNT) {
			R125_AMOUNT = r125_AMOUNT;
		}

		public BigDecimal getR126_AMOUNT() {
			return R126_AMOUNT;
		}

		public void setR126_AMOUNT(BigDecimal r126_AMOUNT) {
			R126_AMOUNT = r126_AMOUNT;
		}

		public BigDecimal getR127_AMOUNT() {
			return R127_AMOUNT;
		}

		public void setR127_AMOUNT(BigDecimal r127_AMOUNT) {
			R127_AMOUNT = r127_AMOUNT;
		}

		public BigDecimal getR128_AMOUNT() {
			return R128_AMOUNT;
		}

		public void setR128_AMOUNT(BigDecimal r128_AMOUNT) {
			R128_AMOUNT = r128_AMOUNT;
		}

		public BigDecimal getR129_AMOUNT() {
			return R129_AMOUNT;
		}

		public void setR129_AMOUNT(BigDecimal r129_AMOUNT) {
			R129_AMOUNT = r129_AMOUNT;
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

//ARCHIVAL ROW MAPPER

	class FSIArchivalRowMapper implements RowMapper<FSI_Archival_Summary_Entity> {

		@Override
		public FSI_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FSI_Archival_Summary_Entity obj = new FSI_Archival_Summary_Entity();

			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));
			obj.setR16_AMOUNT(rs.getBigDecimal("R16_AMOUNT"));
			obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));
			obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));
			obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));
			obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));
			obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));
			obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));
			obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));
			obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));
			obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));
			obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));
			obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));
			obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));
			obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

			obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));
			obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));
			obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));
			obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));
			obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));
			obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));
			obj.setR45_AMOUNT(rs.getBigDecimal("R45_AMOUNT"));
			obj.setR46_AMOUNT(rs.getBigDecimal("R46_AMOUNT"));
			obj.setR47_AMOUNT(rs.getBigDecimal("R47_AMOUNT"));
			obj.setR48_AMOUNT(rs.getBigDecimal("R48_AMOUNT"));
			obj.setR49_AMOUNT(rs.getBigDecimal("R49_AMOUNT"));
			obj.setR50_AMOUNT(rs.getBigDecimal("R50_AMOUNT"));
			obj.setR51_AMOUNT(rs.getBigDecimal("R51_AMOUNT"));
			obj.setR52_AMOUNT(rs.getBigDecimal("R52_AMOUNT"));
			obj.setR53_AMOUNT(rs.getBigDecimal("R53_AMOUNT"));
			obj.setR54_AMOUNT(rs.getBigDecimal("R54_AMOUNT"));
			obj.setR55_AMOUNT(rs.getBigDecimal("R55_AMOUNT"));
			obj.setR56_AMOUNT(rs.getBigDecimal("R56_AMOUNT"));
			obj.setR57_AMOUNT(rs.getBigDecimal("R57_AMOUNT"));
			obj.setR58_AMOUNT(rs.getBigDecimal("R58_AMOUNT"));
			obj.setR59_AMOUNT(rs.getBigDecimal("R59_AMOUNT"));
			obj.setR60_AMOUNT(rs.getBigDecimal("R60_AMOUNT"));
			obj.setR61_AMOUNT(rs.getBigDecimal("R61_AMOUNT"));
			obj.setR62_AMOUNT(rs.getBigDecimal("R62_AMOUNT"));
			obj.setR63_AMOUNT(rs.getBigDecimal("R63_AMOUNT"));
			obj.setR64_AMOUNT(rs.getBigDecimal("R64_AMOUNT"));
			obj.setR65_AMOUNT(rs.getBigDecimal("R65_AMOUNT"));
			obj.setR66_AMOUNT(rs.getBigDecimal("R66_AMOUNT"));
			obj.setR67_AMOUNT(rs.getBigDecimal("R67_AMOUNT"));
			obj.setR68_AMOUNT(rs.getBigDecimal("R68_AMOUNT"));
			obj.setR69_AMOUNT(rs.getBigDecimal("R69_AMOUNT"));
			obj.setR70_AMOUNT(rs.getBigDecimal("R70_AMOUNT"));
			obj.setR71_AMOUNT(rs.getBigDecimal("R71_AMOUNT"));
			obj.setR72_AMOUNT(rs.getBigDecimal("R72_AMOUNT"));
			obj.setR73_AMOUNT(rs.getBigDecimal("R73_AMOUNT"));
			obj.setR74_AMOUNT(rs.getBigDecimal("R74_AMOUNT"));
			obj.setR75_AMOUNT(rs.getBigDecimal("R75_AMOUNT"));
			obj.setR76_AMOUNT(rs.getBigDecimal("R76_AMOUNT"));
			obj.setR77_AMOUNT(rs.getBigDecimal("R77_AMOUNT"));
			obj.setR86_AMOUNT(rs.getBigDecimal("R86_AMOUNT"));
			obj.setR87_AMOUNT(rs.getBigDecimal("R87_AMOUNT"));
			obj.setR88_AMOUNT(rs.getBigDecimal("R88_AMOUNT"));
			obj.setR89_AMOUNT(rs.getBigDecimal("R89_AMOUNT"));
			obj.setR90_AMOUNT(rs.getBigDecimal("R90_AMOUNT"));
			obj.setR91_AMOUNT(rs.getBigDecimal("R91_AMOUNT"));
			obj.setR92_AMOUNT(rs.getBigDecimal("R92_AMOUNT"));
			obj.setR93_AMOUNT(rs.getBigDecimal("R93_AMOUNT"));
			obj.setR94_AMOUNT(rs.getBigDecimal("R94_AMOUNT"));
			obj.setR95_AMOUNT(rs.getBigDecimal("R95_AMOUNT"));
			obj.setR96_AMOUNT(rs.getBigDecimal("R96_AMOUNT"));
			obj.setR97_AMOUNT(rs.getBigDecimal("R97_AMOUNT"));
			obj.setR98_AMOUNT(rs.getBigDecimal("R98_AMOUNT"));
			obj.setR99_AMOUNT(rs.getBigDecimal("R99_AMOUNT"));
			obj.setR100_AMOUNT(rs.getBigDecimal("R100_AMOUNT"));
			obj.setR101_AMOUNT(rs.getBigDecimal("R101_AMOUNT"));
			obj.setR102_AMOUNT(rs.getBigDecimal("R102_AMOUNT"));
			obj.setR103_AMOUNT(rs.getBigDecimal("R103_AMOUNT"));
			obj.setR104_AMOUNT(rs.getBigDecimal("R104_AMOUNT"));
			obj.setR105_AMOUNT(rs.getBigDecimal("R105_AMOUNT"));
			obj.setR106_AMOUNT(rs.getBigDecimal("R106_AMOUNT"));
			obj.setR107_AMOUNT(rs.getBigDecimal("R107_AMOUNT"));
			obj.setR108_AMOUNT(rs.getBigDecimal("R108_AMOUNT"));
			obj.setR109_AMOUNT(rs.getBigDecimal("R109_AMOUNT"));
			obj.setR110_AMOUNT(rs.getBigDecimal("R110_AMOUNT"));
			obj.setR111_AMOUNT(rs.getBigDecimal("R111_AMOUNT"));
			obj.setR112_AMOUNT(rs.getBigDecimal("R112_AMOUNT"));
			obj.setR113_AMOUNT(rs.getBigDecimal("R113_AMOUNT"));
			obj.setR114_AMOUNT(rs.getBigDecimal("R114_AMOUNT"));
			obj.setR115_AMOUNT(rs.getBigDecimal("R115_AMOUNT"));
			obj.setR116_AMOUNT(rs.getBigDecimal("R116_AMOUNT"));
			obj.setR117_AMOUNT(rs.getBigDecimal("R117_AMOUNT"));
			obj.setR118_AMOUNT(rs.getBigDecimal("R118_AMOUNT"));
			obj.setR119_AMOUNT(rs.getBigDecimal("R119_AMOUNT"));
			obj.setR120_AMOUNT(rs.getBigDecimal("R120_AMOUNT"));
			obj.setR121_AMOUNT(rs.getBigDecimal("R121_AMOUNT"));
			obj.setR122_AMOUNT(rs.getBigDecimal("R122_AMOUNT"));
			obj.setR123_AMOUNT(rs.getBigDecimal("R123_AMOUNT"));
			obj.setR124_AMOUNT(rs.getBigDecimal("R124_AMOUNT"));
			obj.setR125_AMOUNT(rs.getBigDecimal("R125_AMOUNT"));
			obj.setR126_AMOUNT(rs.getBigDecimal("R126_AMOUNT"));
			obj.setR127_AMOUNT(rs.getBigDecimal("R127_AMOUNT"));
			obj.setR128_AMOUNT(rs.getBigDecimal("R128_AMOUNT"));
			obj.setR129_AMOUNT(rs.getBigDecimal("R129_AMOUNT"));

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

	@IdClass(FSI_PK.class)
	public class FSI_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private BigDecimal R11_AMOUNT;
		private BigDecimal R12_AMOUNT;
		private BigDecimal R13_AMOUNT;
		private BigDecimal R14_AMOUNT;
		private BigDecimal R15_AMOUNT;
		private BigDecimal R16_AMOUNT;
		private BigDecimal R17_AMOUNT;
		private BigDecimal R18_AMOUNT;
		private BigDecimal R19_AMOUNT;
		private BigDecimal R20_AMOUNT;
		private BigDecimal R21_AMOUNT;
		private BigDecimal R22_AMOUNT;
		private BigDecimal R23_AMOUNT;
		private BigDecimal R24_AMOUNT;
		private BigDecimal R25_AMOUNT;
		private BigDecimal R26_AMOUNT;
		private BigDecimal R27_AMOUNT;
		private BigDecimal R28_AMOUNT;
		private BigDecimal R29_AMOUNT;
		private BigDecimal R30_AMOUNT;
		private BigDecimal R31_AMOUNT;
		private BigDecimal R32_AMOUNT;
		private BigDecimal R33_AMOUNT;

		private BigDecimal R39_AMOUNT;
		private BigDecimal R40_AMOUNT;
		private BigDecimal R41_AMOUNT;
		private BigDecimal R42_AMOUNT;
		private BigDecimal R43_AMOUNT;
		private BigDecimal R44_AMOUNT;
		private BigDecimal R45_AMOUNT;
		private BigDecimal R46_AMOUNT;
		private BigDecimal R47_AMOUNT;
		private BigDecimal R48_AMOUNT;
		private BigDecimal R49_AMOUNT;
		private BigDecimal R50_AMOUNT;
		private BigDecimal R51_AMOUNT;
		private BigDecimal R52_AMOUNT;
		private BigDecimal R53_AMOUNT;
		private BigDecimal R54_AMOUNT;
		private BigDecimal R55_AMOUNT;
		private BigDecimal R56_AMOUNT;
		private BigDecimal R57_AMOUNT;
		private BigDecimal R58_AMOUNT;
		private BigDecimal R59_AMOUNT;
		private BigDecimal R60_AMOUNT;
		private BigDecimal R61_AMOUNT;
		private BigDecimal R62_AMOUNT;
		private BigDecimal R63_AMOUNT;
		private BigDecimal R64_AMOUNT;
		private BigDecimal R65_AMOUNT;
		private BigDecimal R66_AMOUNT;
		private BigDecimal R67_AMOUNT;
		private BigDecimal R68_AMOUNT;
		private BigDecimal R69_AMOUNT;
		private BigDecimal R70_AMOUNT;
		private BigDecimal R71_AMOUNT;
		private BigDecimal R72_AMOUNT;
		private BigDecimal R73_AMOUNT;
		private BigDecimal R74_AMOUNT;
		private BigDecimal R75_AMOUNT;
		private BigDecimal R76_AMOUNT;
		private BigDecimal R77_AMOUNT;
		private BigDecimal R86_AMOUNT;
		private BigDecimal R87_AMOUNT;
		private BigDecimal R88_AMOUNT;
		private BigDecimal R89_AMOUNT;
		private BigDecimal R90_AMOUNT;
		private BigDecimal R91_AMOUNT;
		private BigDecimal R92_AMOUNT;
		private BigDecimal R93_AMOUNT;
		private BigDecimal R94_AMOUNT;
		private BigDecimal R95_AMOUNT;
		private BigDecimal R96_AMOUNT;
		private BigDecimal R97_AMOUNT;
		private BigDecimal R98_AMOUNT;
		private BigDecimal R99_AMOUNT;
		private BigDecimal R100_AMOUNT;
		private BigDecimal R101_AMOUNT;
		private BigDecimal R102_AMOUNT;
		private BigDecimal R103_AMOUNT;
		private BigDecimal R104_AMOUNT;
		private BigDecimal R105_AMOUNT;
		private BigDecimal R106_AMOUNT;
		private BigDecimal R107_AMOUNT;
		private BigDecimal R108_AMOUNT;
		private BigDecimal R109_AMOUNT;
		private BigDecimal R110_AMOUNT;
		private BigDecimal R111_AMOUNT;
		private BigDecimal R112_AMOUNT;
		private BigDecimal R113_AMOUNT;
		private BigDecimal R114_AMOUNT;
		private BigDecimal R115_AMOUNT;
		private BigDecimal R116_AMOUNT;
		private BigDecimal R117_AMOUNT;
		private BigDecimal R118_AMOUNT;
		private BigDecimal R119_AMOUNT;
		private BigDecimal R120_AMOUNT;
		private BigDecimal R121_AMOUNT;
		private BigDecimal R122_AMOUNT;
		private BigDecimal R123_AMOUNT;
		private BigDecimal R124_AMOUNT;
		private BigDecimal R125_AMOUNT;
		private BigDecimal R126_AMOUNT;
		private BigDecimal R127_AMOUNT;
		private BigDecimal R128_AMOUNT;
		private BigDecimal R129_AMOUNT;
		@Id
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

		private Date REPORT_RESUBDATE;

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getR11_AMOUNT() {
			return R11_AMOUNT;
		}

		public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
			R11_AMOUNT = r11_AMOUNT;
		}

		public BigDecimal getR12_AMOUNT() {
			return R12_AMOUNT;
		}

		public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
			R12_AMOUNT = r12_AMOUNT;
		}

		public BigDecimal getR13_AMOUNT() {
			return R13_AMOUNT;
		}

		public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
			R13_AMOUNT = r13_AMOUNT;
		}

		public BigDecimal getR14_AMOUNT() {
			return R14_AMOUNT;
		}

		public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
			R14_AMOUNT = r14_AMOUNT;
		}

		public BigDecimal getR15_AMOUNT() {
			return R15_AMOUNT;
		}

		public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
			R15_AMOUNT = r15_AMOUNT;
		}

		public BigDecimal getR16_AMOUNT() {
			return R16_AMOUNT;
		}

		public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
			R16_AMOUNT = r16_AMOUNT;
		}

		public BigDecimal getR17_AMOUNT() {
			return R17_AMOUNT;
		}

		public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
			R17_AMOUNT = r17_AMOUNT;
		}

		public BigDecimal getR18_AMOUNT() {
			return R18_AMOUNT;
		}

		public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
			R18_AMOUNT = r18_AMOUNT;
		}

		public BigDecimal getR19_AMOUNT() {
			return R19_AMOUNT;
		}

		public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
			R19_AMOUNT = r19_AMOUNT;
		}

		public BigDecimal getR20_AMOUNT() {
			return R20_AMOUNT;
		}

		public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
			R20_AMOUNT = r20_AMOUNT;
		}

		public BigDecimal getR21_AMOUNT() {
			return R21_AMOUNT;
		}

		public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
			R21_AMOUNT = r21_AMOUNT;
		}

		public BigDecimal getR22_AMOUNT() {
			return R22_AMOUNT;
		}

		public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
			R22_AMOUNT = r22_AMOUNT;
		}

		public BigDecimal getR23_AMOUNT() {
			return R23_AMOUNT;
		}

		public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
			R23_AMOUNT = r23_AMOUNT;
		}

		public BigDecimal getR24_AMOUNT() {
			return R24_AMOUNT;
		}

		public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
			R24_AMOUNT = r24_AMOUNT;
		}

		public BigDecimal getR25_AMOUNT() {
			return R25_AMOUNT;
		}

		public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
			R25_AMOUNT = r25_AMOUNT;
		}

		public BigDecimal getR26_AMOUNT() {
			return R26_AMOUNT;
		}

		public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
			R26_AMOUNT = r26_AMOUNT;
		}

		public BigDecimal getR27_AMOUNT() {
			return R27_AMOUNT;
		}

		public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
			R27_AMOUNT = r27_AMOUNT;
		}

		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}

		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}

		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}

		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}

		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}

		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}

		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}

		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}

		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}

		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}

		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}

		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}

		public BigDecimal getR39_AMOUNT() {
			return R39_AMOUNT;
		}

		public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
			R39_AMOUNT = r39_AMOUNT;
		}

		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}

		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}

		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}

		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}

		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}

		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}

		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}

		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}

		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}

		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}

		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}

		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}

		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}

		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}

		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}

		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
		}

		public BigDecimal getR48_AMOUNT() {
			return R48_AMOUNT;
		}

		public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
			R48_AMOUNT = r48_AMOUNT;
		}

		public BigDecimal getR49_AMOUNT() {
			return R49_AMOUNT;
		}

		public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
			R49_AMOUNT = r49_AMOUNT;
		}

		public BigDecimal getR50_AMOUNT() {
			return R50_AMOUNT;
		}

		public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
			R50_AMOUNT = r50_AMOUNT;
		}

		public BigDecimal getR51_AMOUNT() {
			return R51_AMOUNT;
		}

		public void setR51_AMOUNT(BigDecimal r51_AMOUNT) {
			R51_AMOUNT = r51_AMOUNT;
		}

		public BigDecimal getR52_AMOUNT() {
			return R52_AMOUNT;
		}

		public void setR52_AMOUNT(BigDecimal r52_AMOUNT) {
			R52_AMOUNT = r52_AMOUNT;
		}

		public BigDecimal getR53_AMOUNT() {
			return R53_AMOUNT;
		}

		public void setR53_AMOUNT(BigDecimal r53_AMOUNT) {
			R53_AMOUNT = r53_AMOUNT;
		}

		public BigDecimal getR54_AMOUNT() {
			return R54_AMOUNT;
		}

		public void setR54_AMOUNT(BigDecimal r54_AMOUNT) {
			R54_AMOUNT = r54_AMOUNT;
		}

		public BigDecimal getR55_AMOUNT() {
			return R55_AMOUNT;
		}

		public void setR55_AMOUNT(BigDecimal r55_AMOUNT) {
			R55_AMOUNT = r55_AMOUNT;
		}

		public BigDecimal getR56_AMOUNT() {
			return R56_AMOUNT;
		}

		public void setR56_AMOUNT(BigDecimal r56_AMOUNT) {
			R56_AMOUNT = r56_AMOUNT;
		}

		public BigDecimal getR57_AMOUNT() {
			return R57_AMOUNT;
		}

		public void setR57_AMOUNT(BigDecimal r57_AMOUNT) {
			R57_AMOUNT = r57_AMOUNT;
		}

		public BigDecimal getR58_AMOUNT() {
			return R58_AMOUNT;
		}

		public void setR58_AMOUNT(BigDecimal r58_AMOUNT) {
			R58_AMOUNT = r58_AMOUNT;
		}

		public BigDecimal getR59_AMOUNT() {
			return R59_AMOUNT;
		}

		public void setR59_AMOUNT(BigDecimal r59_AMOUNT) {
			R59_AMOUNT = r59_AMOUNT;
		}

		public BigDecimal getR60_AMOUNT() {
			return R60_AMOUNT;
		}

		public void setR60_AMOUNT(BigDecimal r60_AMOUNT) {
			R60_AMOUNT = r60_AMOUNT;
		}

		public BigDecimal getR61_AMOUNT() {
			return R61_AMOUNT;
		}

		public void setR61_AMOUNT(BigDecimal r61_AMOUNT) {
			R61_AMOUNT = r61_AMOUNT;
		}

		public BigDecimal getR62_AMOUNT() {
			return R62_AMOUNT;
		}

		public void setR62_AMOUNT(BigDecimal r62_AMOUNT) {
			R62_AMOUNT = r62_AMOUNT;
		}

		public BigDecimal getR63_AMOUNT() {
			return R63_AMOUNT;
		}

		public void setR63_AMOUNT(BigDecimal r63_AMOUNT) {
			R63_AMOUNT = r63_AMOUNT;
		}

		public BigDecimal getR64_AMOUNT() {
			return R64_AMOUNT;
		}

		public void setR64_AMOUNT(BigDecimal r64_AMOUNT) {
			R64_AMOUNT = r64_AMOUNT;
		}

		public BigDecimal getR65_AMOUNT() {
			return R65_AMOUNT;
		}

		public void setR65_AMOUNT(BigDecimal r65_AMOUNT) {
			R65_AMOUNT = r65_AMOUNT;
		}

		public BigDecimal getR66_AMOUNT() {
			return R66_AMOUNT;
		}

		public void setR66_AMOUNT(BigDecimal r66_AMOUNT) {
			R66_AMOUNT = r66_AMOUNT;
		}

		public BigDecimal getR67_AMOUNT() {
			return R67_AMOUNT;
		}

		public void setR67_AMOUNT(BigDecimal r67_AMOUNT) {
			R67_AMOUNT = r67_AMOUNT;
		}

		public BigDecimal getR68_AMOUNT() {
			return R68_AMOUNT;
		}

		public void setR68_AMOUNT(BigDecimal r68_AMOUNT) {
			R68_AMOUNT = r68_AMOUNT;
		}

		public BigDecimal getR69_AMOUNT() {
			return R69_AMOUNT;
		}

		public void setR69_AMOUNT(BigDecimal r69_AMOUNT) {
			R69_AMOUNT = r69_AMOUNT;
		}

		public BigDecimal getR70_AMOUNT() {
			return R70_AMOUNT;
		}

		public void setR70_AMOUNT(BigDecimal r70_AMOUNT) {
			R70_AMOUNT = r70_AMOUNT;
		}

		public BigDecimal getR71_AMOUNT() {
			return R71_AMOUNT;
		}

		public void setR71_AMOUNT(BigDecimal r71_AMOUNT) {
			R71_AMOUNT = r71_AMOUNT;
		}

		public BigDecimal getR72_AMOUNT() {
			return R72_AMOUNT;
		}

		public void setR72_AMOUNT(BigDecimal r72_AMOUNT) {
			R72_AMOUNT = r72_AMOUNT;
		}

		public BigDecimal getR73_AMOUNT() {
			return R73_AMOUNT;
		}

		public void setR73_AMOUNT(BigDecimal r73_AMOUNT) {
			R73_AMOUNT = r73_AMOUNT;
		}

		public BigDecimal getR74_AMOUNT() {
			return R74_AMOUNT;
		}

		public void setR74_AMOUNT(BigDecimal r74_AMOUNT) {
			R74_AMOUNT = r74_AMOUNT;
		}

		public BigDecimal getR75_AMOUNT() {
			return R75_AMOUNT;
		}

		public void setR75_AMOUNT(BigDecimal r75_AMOUNT) {
			R75_AMOUNT = r75_AMOUNT;
		}

		public BigDecimal getR76_AMOUNT() {
			return R76_AMOUNT;
		}

		public void setR76_AMOUNT(BigDecimal r76_AMOUNT) {
			R76_AMOUNT = r76_AMOUNT;
		}

		public BigDecimal getR77_AMOUNT() {
			return R77_AMOUNT;
		}

		public void setR77_AMOUNT(BigDecimal r77_AMOUNT) {
			R77_AMOUNT = r77_AMOUNT;
		}

		public BigDecimal getR86_AMOUNT() {
			return R86_AMOUNT;
		}

		public void setR86_AMOUNT(BigDecimal r86_AMOUNT) {
			R86_AMOUNT = r86_AMOUNT;
		}

		public BigDecimal getR87_AMOUNT() {
			return R87_AMOUNT;
		}

		public void setR87_AMOUNT(BigDecimal r87_AMOUNT) {
			R87_AMOUNT = r87_AMOUNT;
		}

		public BigDecimal getR88_AMOUNT() {
			return R88_AMOUNT;
		}

		public void setR88_AMOUNT(BigDecimal r88_AMOUNT) {
			R88_AMOUNT = r88_AMOUNT;
		}

		public BigDecimal getR89_AMOUNT() {
			return R89_AMOUNT;
		}

		public void setR89_AMOUNT(BigDecimal r89_AMOUNT) {
			R89_AMOUNT = r89_AMOUNT;
		}

		public BigDecimal getR90_AMOUNT() {
			return R90_AMOUNT;
		}

		public void setR90_AMOUNT(BigDecimal r90_AMOUNT) {
			R90_AMOUNT = r90_AMOUNT;
		}

		public BigDecimal getR91_AMOUNT() {
			return R91_AMOUNT;
		}

		public void setR91_AMOUNT(BigDecimal r91_AMOUNT) {
			R91_AMOUNT = r91_AMOUNT;
		}

		public BigDecimal getR92_AMOUNT() {
			return R92_AMOUNT;
		}

		public void setR92_AMOUNT(BigDecimal r92_AMOUNT) {
			R92_AMOUNT = r92_AMOUNT;
		}

		public BigDecimal getR93_AMOUNT() {
			return R93_AMOUNT;
		}

		public void setR93_AMOUNT(BigDecimal r93_AMOUNT) {
			R93_AMOUNT = r93_AMOUNT;
		}

		public BigDecimal getR94_AMOUNT() {
			return R94_AMOUNT;
		}

		public void setR94_AMOUNT(BigDecimal r94_AMOUNT) {
			R94_AMOUNT = r94_AMOUNT;
		}

		public BigDecimal getR95_AMOUNT() {
			return R95_AMOUNT;
		}

		public void setR95_AMOUNT(BigDecimal r95_AMOUNT) {
			R95_AMOUNT = r95_AMOUNT;
		}

		public BigDecimal getR96_AMOUNT() {
			return R96_AMOUNT;
		}

		public void setR96_AMOUNT(BigDecimal r96_AMOUNT) {
			R96_AMOUNT = r96_AMOUNT;
		}

		public BigDecimal getR97_AMOUNT() {
			return R97_AMOUNT;
		}

		public void setR97_AMOUNT(BigDecimal r97_AMOUNT) {
			R97_AMOUNT = r97_AMOUNT;
		}

		public BigDecimal getR98_AMOUNT() {
			return R98_AMOUNT;
		}

		public void setR98_AMOUNT(BigDecimal r98_AMOUNT) {
			R98_AMOUNT = r98_AMOUNT;
		}

		public BigDecimal getR99_AMOUNT() {
			return R99_AMOUNT;
		}

		public void setR99_AMOUNT(BigDecimal r99_AMOUNT) {
			R99_AMOUNT = r99_AMOUNT;
		}

		public BigDecimal getR100_AMOUNT() {
			return R100_AMOUNT;
		}

		public void setR100_AMOUNT(BigDecimal r100_AMOUNT) {
			R100_AMOUNT = r100_AMOUNT;
		}

		public BigDecimal getR101_AMOUNT() {
			return R101_AMOUNT;
		}

		public void setR101_AMOUNT(BigDecimal r101_AMOUNT) {
			R101_AMOUNT = r101_AMOUNT;
		}

		public BigDecimal getR102_AMOUNT() {
			return R102_AMOUNT;
		}

		public void setR102_AMOUNT(BigDecimal r102_AMOUNT) {
			R102_AMOUNT = r102_AMOUNT;
		}

		public BigDecimal getR103_AMOUNT() {
			return R103_AMOUNT;
		}

		public void setR103_AMOUNT(BigDecimal r103_AMOUNT) {
			R103_AMOUNT = r103_AMOUNT;
		}

		public BigDecimal getR104_AMOUNT() {
			return R104_AMOUNT;
		}

		public void setR104_AMOUNT(BigDecimal r104_AMOUNT) {
			R104_AMOUNT = r104_AMOUNT;
		}

		public BigDecimal getR105_AMOUNT() {
			return R105_AMOUNT;
		}

		public void setR105_AMOUNT(BigDecimal r105_AMOUNT) {
			R105_AMOUNT = r105_AMOUNT;
		}

		public BigDecimal getR106_AMOUNT() {
			return R106_AMOUNT;
		}

		public void setR106_AMOUNT(BigDecimal r106_AMOUNT) {
			R106_AMOUNT = r106_AMOUNT;
		}

		public BigDecimal getR107_AMOUNT() {
			return R107_AMOUNT;
		}

		public void setR107_AMOUNT(BigDecimal r107_AMOUNT) {
			R107_AMOUNT = r107_AMOUNT;
		}

		public BigDecimal getR108_AMOUNT() {
			return R108_AMOUNT;
		}

		public void setR108_AMOUNT(BigDecimal r108_AMOUNT) {
			R108_AMOUNT = r108_AMOUNT;
		}

		public BigDecimal getR109_AMOUNT() {
			return R109_AMOUNT;
		}

		public void setR109_AMOUNT(BigDecimal r109_AMOUNT) {
			R109_AMOUNT = r109_AMOUNT;
		}

		public BigDecimal getR110_AMOUNT() {
			return R110_AMOUNT;
		}

		public void setR110_AMOUNT(BigDecimal r110_AMOUNT) {
			R110_AMOUNT = r110_AMOUNT;
		}

		public BigDecimal getR111_AMOUNT() {
			return R111_AMOUNT;
		}

		public void setR111_AMOUNT(BigDecimal r111_AMOUNT) {
			R111_AMOUNT = r111_AMOUNT;
		}

		public BigDecimal getR112_AMOUNT() {
			return R112_AMOUNT;
		}

		public void setR112_AMOUNT(BigDecimal r112_AMOUNT) {
			R112_AMOUNT = r112_AMOUNT;
		}

		public BigDecimal getR113_AMOUNT() {
			return R113_AMOUNT;
		}

		public void setR113_AMOUNT(BigDecimal r113_AMOUNT) {
			R113_AMOUNT = r113_AMOUNT;
		}

		public BigDecimal getR114_AMOUNT() {
			return R114_AMOUNT;
		}

		public void setR114_AMOUNT(BigDecimal r114_AMOUNT) {
			R114_AMOUNT = r114_AMOUNT;
		}

		public BigDecimal getR115_AMOUNT() {
			return R115_AMOUNT;
		}

		public void setR115_AMOUNT(BigDecimal r115_AMOUNT) {
			R115_AMOUNT = r115_AMOUNT;
		}

		public BigDecimal getR116_AMOUNT() {
			return R116_AMOUNT;
		}

		public void setR116_AMOUNT(BigDecimal r116_AMOUNT) {
			R116_AMOUNT = r116_AMOUNT;
		}

		public BigDecimal getR117_AMOUNT() {
			return R117_AMOUNT;
		}

		public void setR117_AMOUNT(BigDecimal r117_AMOUNT) {
			R117_AMOUNT = r117_AMOUNT;
		}

		public BigDecimal getR118_AMOUNT() {
			return R118_AMOUNT;
		}

		public void setR118_AMOUNT(BigDecimal r118_AMOUNT) {
			R118_AMOUNT = r118_AMOUNT;
		}

		public BigDecimal getR119_AMOUNT() {
			return R119_AMOUNT;
		}

		public void setR119_AMOUNT(BigDecimal r119_AMOUNT) {
			R119_AMOUNT = r119_AMOUNT;
		}

		public BigDecimal getR120_AMOUNT() {
			return R120_AMOUNT;
		}

		public void setR120_AMOUNT(BigDecimal r120_AMOUNT) {
			R120_AMOUNT = r120_AMOUNT;
		}

		public BigDecimal getR121_AMOUNT() {
			return R121_AMOUNT;
		}

		public void setR121_AMOUNT(BigDecimal r121_AMOUNT) {
			R121_AMOUNT = r121_AMOUNT;
		}

		public BigDecimal getR122_AMOUNT() {
			return R122_AMOUNT;
		}

		public void setR122_AMOUNT(BigDecimal r122_AMOUNT) {
			R122_AMOUNT = r122_AMOUNT;
		}

		public BigDecimal getR123_AMOUNT() {
			return R123_AMOUNT;
		}

		public void setR123_AMOUNT(BigDecimal r123_AMOUNT) {
			R123_AMOUNT = r123_AMOUNT;
		}

		public BigDecimal getR124_AMOUNT() {
			return R124_AMOUNT;
		}

		public void setR124_AMOUNT(BigDecimal r124_AMOUNT) {
			R124_AMOUNT = r124_AMOUNT;
		}

		public BigDecimal getR125_AMOUNT() {
			return R125_AMOUNT;
		}

		public void setR125_AMOUNT(BigDecimal r125_AMOUNT) {
			R125_AMOUNT = r125_AMOUNT;
		}

		public BigDecimal getR126_AMOUNT() {
			return R126_AMOUNT;
		}

		public void setR126_AMOUNT(BigDecimal r126_AMOUNT) {
			R126_AMOUNT = r126_AMOUNT;
		}

		public BigDecimal getR127_AMOUNT() {
			return R127_AMOUNT;
		}

		public void setR127_AMOUNT(BigDecimal r127_AMOUNT) {
			R127_AMOUNT = r127_AMOUNT;
		}

		public BigDecimal getR128_AMOUNT() {
			return R128_AMOUNT;
		}

		public void setR128_AMOUNT(BigDecimal r128_AMOUNT) {
			R128_AMOUNT = r128_AMOUNT;
		}

		public BigDecimal getR129_AMOUNT() {
			return R129_AMOUNT;
		}

		public void setR129_AMOUNT(BigDecimal r129_AMOUNT) {
			R129_AMOUNT = r129_AMOUNT;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
	}

// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class FSI_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public FSI_PK() {
		}

		public FSI_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof FSI_PK))
				return false;
			FSI_PK that = (FSI_PK) o;
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

	public class FSI_Detail_Entity {

		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

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

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
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

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}
	}

	class FSIDetailRowMapper implements RowMapper<FSI_Detail_Entity> {

		@Override
		public FSI_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FSI_Detail_Entity obj = new FSI_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');

			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');

			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	class FSIArchivalDetailRowMapper implements RowMapper<FSI_Archival_Detail_Entity> {

		@Override
		public FSI_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FSI_Archival_Detail_Entity obj = new FSI_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');

			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');

			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	public class FSI_Archival_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

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

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
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

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}
	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getFSIView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("FSI View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<FSI_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival(dt, version);

				System.out.println(type + " Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		// NORMAL MODE
		else {

			List<FSI_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/FSI");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getFSIcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		ModelAndView mv = new ModelAndView();

		try {

			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
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

				List<FSI_Archival_Detail_Entity> detailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					detailList = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					detailList = getArchivalDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);

				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}

			// CURRENT MODE
			else {

				List<FSI_Detail_Entity> currentDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					currentDetailList = GetDetailDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					currentDetailList = getDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", currentDetailList);
				mv.addObject("reportmaster12", currentDetailList);

				System.out.println("CURRENT DETAIL COUNT: " + currentDetailList.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", e.getMessage());
		}

		mv.setViewName("BRRS/FSI");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getFSIArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<FSI_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (FSI_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				FSI_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  FSI  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/FSI");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				FSI_Detail_Entity FSIEntity = findBySnoArch(SNO);
				if (FSIEntity != null && FSIEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(FSIEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("FSIData", FSIEntity);
			} else {
				FSI_Detail_Entity FSIEntity = findBySno(SNO);
				if (FSIEntity != null && FSIEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(FSIEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("FSIData", FSIEntity);
			}
		}
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String Sno = request.getParameter("sno");

			String acctBalanceInpula = request.getParameter("acctBalanceInpula");

			String averageStr = request.getParameter("average");

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			FSI_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			FSI_Detail_Entity oldcopy = new FSI_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			if (existing == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {

					existing.setAcctName(acctName);

					isChanged = true;
				}
			}

			// Update Balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInpula(newBalance);

					isChanged = true;
				}
			}
// AVERAGE
			if (averageStr != null && !averageStr.isEmpty()) {

				BigDecimal newAverage = new BigDecimal(averageStr);

				if (existing.getAverage() == null || existing.getAverage().compareTo(newAverage) != 0) {

					existing.setAverage(newAverage);

					isChanged = true;
				}
			}
			// Save using JDBC
			if (isChanged) {
				String sql;
				System.out.println("Type in update block : " + type);
				if (type == "RESUB" || type.equals("RESUB")) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_FSI_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ?, "
							+ // ✅ comma added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_FSI_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ?, " + // ✅
																													// comma
																													// added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
						Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "FSI Archival Screen",
							"BRRS_FSI_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "FSI Screen", "BRRS_FSI_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_FSI_Procudure(reportDateStr, type, entry);

				if ((type == "RESUB" || type.equals("RESUB")) && (entry == "NO" || entry.equals("NO"))) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("Record updated successfully!");
			} else {
				return ResponseEntity.ok("No changes were made.");
			}

		}

		catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_FSI_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_FSI_Procudure(String reportDateStr, String type, String entry) {

		String formattedDate;
		try {
			formattedDate = new SimpleDateFormat("dd-MM-yyyy")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));
		} catch (Exception e) {
			System.out.println("Error parsing date. Post-commit logic aborted.");
			e.printStackTrace();
			return;
		}

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

			@Override
			public void afterCommit() {
				try {
					boolean isResubNoEntry = "RESUB".equals(type) && "NO".equals(entry);
					boolean shouldExecuteProcedure = !"RESUB".equals(type) || isResubNoEntry;

					if (isResubNoEntry) {
						String bdsql = "DELETE FROM BRRS_FSI_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_FSI_DETAILTABLE "
								+ " (SNO, GL_CODE, GLSH_CODE, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, GL_CODE, GLSH_CODE, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_FSI_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_FSI_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_FSI_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_FSI_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						String finalsql = "INSERT INTO BRRS_FSI_ARCHIVALTABLE_SUMMARY ("
								+ "R11_AMOUNT, R12_AMOUNT, R13_AMOUNT, R14_AMOUNT, R15_AMOUNT, R16_AMOUNT, R17_AMOUNT, R18_AMOUNT, R19_AMOUNT, R20_AMOUNT, "
								+ "R21_AMOUNT, R22_AMOUNT, R23_AMOUNT, R24_AMOUNT, R25_AMOUNT, R26_AMOUNT, R27_AMOUNT, R28_AMOUNT, R29_AMOUNT, R30_AMOUNT, "
								+ "R31_AMOUNT, R32_AMOUNT, R33_AMOUNT, R39_AMOUNT, R40_AMOUNT, R41_AMOUNT, R42_AMOUNT, R43_AMOUNT, R44_AMOUNT, R45_AMOUNT, "
								+ "R46_AMOUNT, R47_AMOUNT, R48_AMOUNT, R49_AMOUNT, R50_AMOUNT, R51_AMOUNT, R52_AMOUNT, R53_AMOUNT, R54_AMOUNT, R55_AMOUNT, "
								+ "R56_AMOUNT, R57_AMOUNT, R58_AMOUNT, R59_AMOUNT, R60_AMOUNT, R61_AMOUNT, R62_AMOUNT, R63_AMOUNT, R64_AMOUNT, R65_AMOUNT, "
								+ "R66_AMOUNT, R67_AMOUNT, R68_AMOUNT, R69_AMOUNT, R70_AMOUNT, R71_AMOUNT, R72_AMOUNT, R73_AMOUNT, R74_AMOUNT, R75_AMOUNT, "
								+ "R76_AMOUNT, R77_AMOUNT, R86_AMOUNT, R87_AMOUNT, R88_AMOUNT, R89_AMOUNT, R90_AMOUNT, R91_AMOUNT, R92_AMOUNT, R93_AMOUNT, "
								+ "R94_AMOUNT, R95_AMOUNT, R96_AMOUNT, R97_AMOUNT, R98_AMOUNT, R99_AMOUNT, R100_AMOUNT, R101_AMOUNT, R102_AMOUNT, R103_AMOUNT, "
								+ "R104_AMOUNT, R105_AMOUNT, R106_AMOUNT, R107_AMOUNT, R108_AMOUNT, R109_AMOUNT, R110_AMOUNT, R111_AMOUNT, R112_AMOUNT, R113_AMOUNT, "
								+ "R114_AMOUNT, R115_AMOUNT, R116_AMOUNT, R117_AMOUNT, R118_AMOUNT, R119_AMOUNT, R120_AMOUNT, R121_AMOUNT, R122_AMOUNT, R123_AMOUNT, "
								+ "R124_AMOUNT, R125_AMOUNT, R126_AMOUNT, R127_AMOUNT, R128_AMOUNT, R129_AMOUNT, "
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
								+ ") SELECT "
								+ "R11_AMOUNT, R12_AMOUNT, R13_AMOUNT, R14_AMOUNT, R15_AMOUNT, R16_AMOUNT, R17_AMOUNT, R18_AMOUNT, R19_AMOUNT, R20_AMOUNT, "
								+ "R21_AMOUNT, R22_AMOUNT, R23_AMOUNT, R24_AMOUNT, R25_AMOUNT, R26_AMOUNT, R27_AMOUNT, R28_AMOUNT, R29_AMOUNT, R30_AMOUNT, "
								+ "R31_AMOUNT, R32_AMOUNT, R33_AMOUNT, R39_AMOUNT, R40_AMOUNT, R41_AMOUNT, R42_AMOUNT, R43_AMOUNT, R44_AMOUNT, R45_AMOUNT, "
								+ "R46_AMOUNT, R47_AMOUNT, R48_AMOUNT, R49_AMOUNT, R50_AMOUNT, R51_AMOUNT, R52_AMOUNT, R53_AMOUNT, R54_AMOUNT, R55_AMOUNT, "
								+ "R56_AMOUNT, R57_AMOUNT, R58_AMOUNT, R59_AMOUNT, R60_AMOUNT, R61_AMOUNT, R62_AMOUNT, R63_AMOUNT, R64_AMOUNT, R65_AMOUNT, "
								+ "R66_AMOUNT, R67_AMOUNT, R68_AMOUNT, R69_AMOUNT, R70_AMOUNT, R71_AMOUNT, R72_AMOUNT, R73_AMOUNT, R74_AMOUNT, R75_AMOUNT, "
								+ "R76_AMOUNT, R77_AMOUNT, R86_AMOUNT, R87_AMOUNT, R88_AMOUNT, R89_AMOUNT, R90_AMOUNT, R91_AMOUNT, R92_AMOUNT, R93_AMOUNT, "
								+ "R94_AMOUNT, R95_AMOUNT, R96_AMOUNT, R97_AMOUNT, R98_AMOUNT, R99_AMOUNT, R100_AMOUNT, R101_AMOUNT, R102_AMOUNT, R103_AMOUNT, "
								+ "R104_AMOUNT, R105_AMOUNT, R106_AMOUNT, R107_AMOUNT, R108_AMOUNT, R109_AMOUNT, R110_AMOUNT, R111_AMOUNT, R112_AMOUNT, R113_AMOUNT, "
								+ "R114_AMOUNT, R115_AMOUNT, R116_AMOUNT, R117_AMOUNT, R118_AMOUNT, R119_AMOUNT, R120_AMOUNT, R121_AMOUNT, R122_AMOUNT, R123_AMOUNT, "
								+ "R124_AMOUNT, R125_AMOUNT, R126_AMOUNT, R127_AMOUNT, R128_AMOUNT, R129_AMOUNT, "
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_FSI_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_FSI_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] getFSIDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  FSI Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getFSIDetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				byte[] resubReport = getFSIDetailExcelRESUB(filename, fromdate, todate, currency, dtltype, type,
						version);

				return resubReport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FSIDetailsDetail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<FSI_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FSI_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FSI — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FSI Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFSIDetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FSI ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FSI Detail NEW");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<FSI_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FSI_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FSI — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FSI NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFSIDetailExcelRESUB(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FSI Resub Details...");
			System.out.println("came to Resub Detail download service");
			if (type.equals("RESUB") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FSI Detail NEW");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<FSI_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FSI_Archival_Detail_Entity item : reportData) {
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

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FSI — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FSI NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFSIExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.FSI");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelFSIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			return getExcelExcelRESUB(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// Fetch data

		List<FSI_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  FSI report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					FSI_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.createCell(1);
					if (record.getR12_AMOUNT() != null) {
						cell1.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_AMOUNT() != null) {
						cell1.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_AMOUNT() != null) {
						cell1.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_AMOUNT() != null) {
						cell1.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_AMOUNT() != null) {
						cell1.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_AMOUNT() != null) {
						cell1.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_AMOUNT() != null) {
						cell1.setCellValue(record.getR20_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_AMOUNT() != null) {
						cell1.setCellValue(record.getR23_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_AMOUNT() != null) {
						cell1.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_AMOUNT() != null) {
						cell1.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					if (record.getR27_AMOUNT() != null) {
						cell1.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_AMOUNT() != null) {
						cell1.setCellValue(record.getR29_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_AMOUNT() != null) {
						cell1.setCellValue(record.getR30_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_AMOUNT() != null) {
						cell1.setCellValue(record.getR32_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_AMOUNT() != null) {
						cell1.setCellValue(record.getR42_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_AMOUNT() != null) {
						cell1.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_AMOUNT() != null) {
						cell1.setCellValue(record.getR48_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_AMOUNT() != null) {
						cell1.setCellValue(record.getR49_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					cell1 = row.createCell(1);
					if (record.getR52_AMOUNT() != null) {
						cell1.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					cell1 = row.createCell(1);
					if (record.getR53_AMOUNT() != null) {
						cell1.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell1 = row.createCell(1);
					if (record.getR54_AMOUNT() != null) {
						cell1.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell1 = row.createCell(1);
					if (record.getR55_AMOUNT() != null) {
						cell1.setCellValue(record.getR55_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell1 = row.createCell(1);
					if (record.getR56_AMOUNT() != null) {
						cell1.setCellValue(record.getR56_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell1 = row.createCell(1);
					if (record.getR57_AMOUNT() != null) {
						cell1.setCellValue(record.getR57_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(57);
					cell1 = row.createCell(1);
					if (record.getR58_AMOUNT() != null) {
						cell1.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(58);
					cell1 = row.createCell(1);
					if (record.getR59_AMOUNT() != null) {
						cell1.setCellValue(record.getR59_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell1 = row.createCell(1);
					if (record.getR60_AMOUNT() != null) {
						cell1.setCellValue(record.getR60_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell1 = row.createCell(1);
					if (record.getR61_AMOUNT() != null) {
						cell1.setCellValue(record.getR61_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell1 = row.createCell(1);
					if (record.getR62_AMOUNT() != null) {
						cell1.setCellValue(record.getR62_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(64);
					cell1 = row.createCell(1);
					if (record.getR65_AMOUNT() != null) {
						cell1.setCellValue(record.getR65_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(66);
					cell1 = row.createCell(1);
					if (record.getR67_AMOUNT() != null) {
						cell1.setCellValue(record.getR67_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(67);
					cell1 = row.createCell(1);
					if (record.getR68_AMOUNT() != null) {
						cell1.setCellValue(record.getR68_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(68);
					cell1 = row.createCell(1);
					if (record.getR69_AMOUNT() != null) {
						cell1.setCellValue(record.getR69_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(69);
					cell1 = row.createCell(1);
					if (record.getR70_AMOUNT() != null) {
						cell1.setCellValue(record.getR70_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(70);
					cell1 = row.createCell(1);
					if (record.getR71_AMOUNT() != null) {
						cell1.setCellValue(record.getR71_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
					cell1 = row.createCell(1);
					if (record.getR72_AMOUNT() != null) {
						cell1.setCellValue(record.getR72_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(73);
					cell1 = row.createCell(1);
					if (record.getR74_AMOUNT() != null) {
						cell1.setCellValue(record.getR74_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(74);
					cell1 = row.createCell(1);
					if (record.getR75_AMOUNT() != null) {
						cell1.setCellValue(record.getR75_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(75);
					cell1 = row.createCell(1);
					if (record.getR76_AMOUNT() != null) {
						cell1.setCellValue(record.getR76_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(85);
					cell1 = row.createCell(1);
					if (record.getR86_AMOUNT() != null) {
						cell1.setCellValue(record.getR86_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(86);
					cell1 = row.createCell(1);
					if (record.getR87_AMOUNT() != null) {
						cell1.setCellValue(record.getR87_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(87);
					cell1 = row.createCell(1);
					if (record.getR88_AMOUNT() != null) {
						cell1.setCellValue(record.getR88_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(88);
					cell1 = row.createCell(1);
					if (record.getR89_AMOUNT() != null) {
						cell1.setCellValue(record.getR89_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(89);
					cell1 = row.createCell(1);
					if (record.getR90_AMOUNT() != null) {
						cell1.setCellValue(record.getR90_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(90);
					cell1 = row.createCell(1);
					if (record.getR91_AMOUNT() != null) {
						cell1.setCellValue(record.getR91_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(92);
					cell1 = row.createCell(1);
					if (record.getR93_AMOUNT() != null) {
						cell1.setCellValue(record.getR93_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(93);
					cell1 = row.createCell(1);
					if (record.getR94_AMOUNT() != null) {
						cell1.setCellValue(record.getR94_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(94);
					cell1 = row.createCell(1);
					if (record.getR95_AMOUNT() != null) {
						cell1.setCellValue(record.getR95_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(95);
					cell1 = row.createCell(1);
					if (record.getR96_AMOUNT() != null) {
						cell1.setCellValue(record.getR96_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(96);
					cell1 = row.createCell(1);
					if (record.getR97_AMOUNT() != null) {
						cell1.setCellValue(record.getR97_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(97);
					cell1 = row.createCell(1);
					if (record.getR98_AMOUNT() != null) {
						cell1.setCellValue(record.getR98_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(98);
					cell1 = row.createCell(1);
					if (record.getR99_AMOUNT() != null) {
						cell1.setCellValue(record.getR99_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(100);
					cell1 = row.createCell(1);
					if (record.getR101_AMOUNT() != null) {
						cell1.setCellValue(record.getR101_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(101);
					cell1 = row.createCell(1);
					if (record.getR102_AMOUNT() != null) {
						cell1.setCellValue(record.getR102_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(102);
					cell1 = row.createCell(1);
					if (record.getR103_AMOUNT() != null) {
						cell1.setCellValue(record.getR103_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(103);
					cell1 = row.createCell(1);
					if (record.getR104_AMOUNT() != null) {
						cell1.setCellValue(record.getR104_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(104);
					cell1 = row.createCell(1);
					if (record.getR105_AMOUNT() != null) {
						cell1.setCellValue(record.getR105_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(106);
					cell1 = row.createCell(1);
					if (record.getR107_AMOUNT() != null) {
						cell1.setCellValue(record.getR107_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(107);
					cell1 = row.createCell(1);
					if (record.getR108_AMOUNT() != null) {
						cell1.setCellValue(record.getR108_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(109);
					cell1 = row.createCell(1);
					if (record.getR110_AMOUNT() != null) {
						cell1.setCellValue(record.getR110_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(110);
					cell1 = row.createCell(1);
					if (record.getR111_AMOUNT() != null) {
						cell1.setCellValue(record.getR111_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(111);
					cell1 = row.createCell(1);
					if (record.getR112_AMOUNT() != null) {
						cell1.setCellValue(record.getR112_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(112);
					cell1 = row.createCell(1);
					if (record.getR113_AMOUNT() != null) {
						cell1.setCellValue(record.getR113_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(113);
					cell1 = row.createCell(1);
					if (record.getR114_AMOUNT() != null) {
						cell1.setCellValue(record.getR114_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(114);
					cell1 = row.createCell(1);
					if (record.getR115_AMOUNT() != null) {
						cell1.setCellValue(record.getR115_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(115);
					cell1 = row.createCell(1);
					if (record.getR116_AMOUNT() != null) {
						cell1.setCellValue(record.getR116_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(116);
					cell1 = row.createCell(1);
					if (record.getR117_AMOUNT() != null) {
						cell1.setCellValue(record.getR117_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(117);
					cell1 = row.createCell(1);
					if (record.getR118_AMOUNT() != null) {
						cell1.setCellValue(record.getR118_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(118);
					cell1 = row.createCell(1);
					if (record.getR119_AMOUNT() != null) {
						cell1.setCellValue(record.getR119_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(119);
					cell1 = row.createCell(1);
					if (record.getR120_AMOUNT() != null) {
						cell1.setCellValue(record.getR120_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(120);
					cell1 = row.createCell(1);
					if (record.getR121_AMOUNT() != null) {
						cell1.setCellValue(record.getR121_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(121);
					cell1 = row.createCell(1);
					if (record.getR122_AMOUNT() != null) {
						cell1.setCellValue(record.getR122_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(122);
					cell1 = row.createCell(1);
					if (record.getR123_AMOUNT() != null) {
						cell1.setCellValue(record.getR123_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(123);
					cell1 = row.createCell(1);
					if (record.getR124_AMOUNT() != null) {
						cell1.setCellValue(record.getR124_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(124);
					cell1 = row.createCell(1);
					if (record.getR125_AMOUNT() != null) {
						cell1.setCellValue(record.getR125_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(125);
					cell1 = row.createCell(1);
					if (record.getR126_AMOUNT() != null) {
						cell1.setCellValue(record.getR126_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(126);
					cell1 = row.createCell(1);
					if (record.getR127_AMOUNT() != null) {
						cell1.setCellValue(record.getR127_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(127);
					cell1 = row.createCell(1);
					if (record.getR128_AMOUNT() != null) {
						cell1.setCellValue(record.getR128_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(128);
					cell1 = row.createCell(1);
					if (record.getR129_AMOUNT() != null) {
						cell1.setCellValue(record.getR129_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] getExcelFSIARCHIVAL(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<FSI_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FSI new report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					FSI_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.createCell(1);
					if (record.getR12_AMOUNT() != null) {
						cell1.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_AMOUNT() != null) {
						cell1.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_AMOUNT() != null) {
						cell1.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_AMOUNT() != null) {
						cell1.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_AMOUNT() != null) {
						cell1.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_AMOUNT() != null) {
						cell1.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_AMOUNT() != null) {
						cell1.setCellValue(record.getR20_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_AMOUNT() != null) {
						cell1.setCellValue(record.getR23_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_AMOUNT() != null) {
						cell1.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_AMOUNT() != null) {
						cell1.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					if (record.getR27_AMOUNT() != null) {
						cell1.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_AMOUNT() != null) {
						cell1.setCellValue(record.getR29_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_AMOUNT() != null) {
						cell1.setCellValue(record.getR30_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_AMOUNT() != null) {
						cell1.setCellValue(record.getR32_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_AMOUNT() != null) {
						cell1.setCellValue(record.getR42_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_AMOUNT() != null) {
						cell1.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_AMOUNT() != null) {
						cell1.setCellValue(record.getR48_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_AMOUNT() != null) {
						cell1.setCellValue(record.getR49_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					cell1 = row.createCell(1);
					if (record.getR52_AMOUNT() != null) {
						cell1.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					cell1 = row.createCell(1);
					if (record.getR53_AMOUNT() != null) {
						cell1.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell1 = row.createCell(1);
					if (record.getR54_AMOUNT() != null) {
						cell1.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell1 = row.createCell(1);
					if (record.getR55_AMOUNT() != null) {
						cell1.setCellValue(record.getR55_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell1 = row.createCell(1);
					if (record.getR56_AMOUNT() != null) {
						cell1.setCellValue(record.getR56_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell1 = row.createCell(1);
					if (record.getR57_AMOUNT() != null) {
						cell1.setCellValue(record.getR57_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(57);
					cell1 = row.createCell(1);
					if (record.getR58_AMOUNT() != null) {
						cell1.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(58);
					cell1 = row.createCell(1);
					if (record.getR59_AMOUNT() != null) {
						cell1.setCellValue(record.getR59_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell1 = row.createCell(1);
					if (record.getR60_AMOUNT() != null) {
						cell1.setCellValue(record.getR60_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell1 = row.createCell(1);
					if (record.getR61_AMOUNT() != null) {
						cell1.setCellValue(record.getR61_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell1 = row.createCell(1);
					if (record.getR62_AMOUNT() != null) {
						cell1.setCellValue(record.getR62_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(64);
					cell1 = row.createCell(1);
					if (record.getR65_AMOUNT() != null) {
						cell1.setCellValue(record.getR65_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(66);
					cell1 = row.createCell(1);
					if (record.getR67_AMOUNT() != null) {
						cell1.setCellValue(record.getR67_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(67);
					cell1 = row.createCell(1);
					if (record.getR68_AMOUNT() != null) {
						cell1.setCellValue(record.getR68_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(68);
					cell1 = row.createCell(1);
					if (record.getR69_AMOUNT() != null) {
						cell1.setCellValue(record.getR69_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(69);
					cell1 = row.createCell(1);
					if (record.getR70_AMOUNT() != null) {
						cell1.setCellValue(record.getR70_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(70);
					cell1 = row.createCell(1);
					if (record.getR71_AMOUNT() != null) {
						cell1.setCellValue(record.getR71_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
					cell1 = row.createCell(1);
					if (record.getR72_AMOUNT() != null) {
						cell1.setCellValue(record.getR72_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(73);
					cell1 = row.createCell(1);
					if (record.getR74_AMOUNT() != null) {
						cell1.setCellValue(record.getR74_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(74);
					cell1 = row.createCell(1);
					if (record.getR75_AMOUNT() != null) {
						cell1.setCellValue(record.getR75_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(75);
					cell1 = row.createCell(1);
					if (record.getR76_AMOUNT() != null) {
						cell1.setCellValue(record.getR76_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(85);
					cell1 = row.createCell(1);
					if (record.getR86_AMOUNT() != null) {
						cell1.setCellValue(record.getR86_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(86);
					cell1 = row.createCell(1);
					if (record.getR87_AMOUNT() != null) {
						cell1.setCellValue(record.getR87_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(87);
					cell1 = row.createCell(1);
					if (record.getR88_AMOUNT() != null) {
						cell1.setCellValue(record.getR88_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(88);
					cell1 = row.createCell(1);
					if (record.getR89_AMOUNT() != null) {
						cell1.setCellValue(record.getR89_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(89);
					cell1 = row.createCell(1);
					if (record.getR90_AMOUNT() != null) {
						cell1.setCellValue(record.getR90_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(90);
					cell1 = row.createCell(1);
					if (record.getR91_AMOUNT() != null) {
						cell1.setCellValue(record.getR91_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(92);
					cell1 = row.createCell(1);
					if (record.getR93_AMOUNT() != null) {
						cell1.setCellValue(record.getR93_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(93);
					cell1 = row.createCell(1);
					if (record.getR94_AMOUNT() != null) {
						cell1.setCellValue(record.getR94_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(94);
					cell1 = row.createCell(1);
					if (record.getR95_AMOUNT() != null) {
						cell1.setCellValue(record.getR95_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(95);
					cell1 = row.createCell(1);
					if (record.getR96_AMOUNT() != null) {
						cell1.setCellValue(record.getR96_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(96);
					cell1 = row.createCell(1);
					if (record.getR97_AMOUNT() != null) {
						cell1.setCellValue(record.getR97_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(97);
					cell1 = row.createCell(1);
					if (record.getR98_AMOUNT() != null) {
						cell1.setCellValue(record.getR98_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(98);
					cell1 = row.createCell(1);
					if (record.getR99_AMOUNT() != null) {
						cell1.setCellValue(record.getR99_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(100);
					cell1 = row.createCell(1);
					if (record.getR101_AMOUNT() != null) {
						cell1.setCellValue(record.getR101_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(101);
					cell1 = row.createCell(1);
					if (record.getR102_AMOUNT() != null) {
						cell1.setCellValue(record.getR102_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(102);
					cell1 = row.createCell(1);
					if (record.getR103_AMOUNT() != null) {
						cell1.setCellValue(record.getR103_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(103);
					cell1 = row.createCell(1);
					if (record.getR104_AMOUNT() != null) {
						cell1.setCellValue(record.getR104_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(104);
					cell1 = row.createCell(1);
					if (record.getR105_AMOUNT() != null) {
						cell1.setCellValue(record.getR105_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(106);
					cell1 = row.createCell(1);
					if (record.getR107_AMOUNT() != null) {
						cell1.setCellValue(record.getR107_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(107);
					cell1 = row.createCell(1);
					if (record.getR108_AMOUNT() != null) {
						cell1.setCellValue(record.getR108_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(109);
					cell1 = row.createCell(1);
					if (record.getR110_AMOUNT() != null) {
						cell1.setCellValue(record.getR110_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(110);
					cell1 = row.createCell(1);
					if (record.getR111_AMOUNT() != null) {
						cell1.setCellValue(record.getR111_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(111);
					cell1 = row.createCell(1);
					if (record.getR112_AMOUNT() != null) {
						cell1.setCellValue(record.getR112_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(112);
					cell1 = row.createCell(1);
					if (record.getR113_AMOUNT() != null) {
						cell1.setCellValue(record.getR113_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(113);
					cell1 = row.createCell(1);
					if (record.getR114_AMOUNT() != null) {
						cell1.setCellValue(record.getR114_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(114);
					cell1 = row.createCell(1);
					if (record.getR115_AMOUNT() != null) {
						cell1.setCellValue(record.getR115_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(115);
					cell1 = row.createCell(1);
					if (record.getR116_AMOUNT() != null) {
						cell1.setCellValue(record.getR116_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(116);
					cell1 = row.createCell(1);
					if (record.getR117_AMOUNT() != null) {
						cell1.setCellValue(record.getR117_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(117);
					cell1 = row.createCell(1);
					if (record.getR118_AMOUNT() != null) {
						cell1.setCellValue(record.getR118_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(118);
					cell1 = row.createCell(1);
					if (record.getR119_AMOUNT() != null) {
						cell1.setCellValue(record.getR119_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(119);
					cell1 = row.createCell(1);
					if (record.getR120_AMOUNT() != null) {
						cell1.setCellValue(record.getR120_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(120);
					cell1 = row.createCell(1);
					if (record.getR121_AMOUNT() != null) {
						cell1.setCellValue(record.getR121_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(121);
					cell1 = row.createCell(1);
					if (record.getR122_AMOUNT() != null) {
						cell1.setCellValue(record.getR122_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(122);
					cell1 = row.createCell(1);
					if (record.getR123_AMOUNT() != null) {
						cell1.setCellValue(record.getR123_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(123);
					cell1 = row.createCell(1);
					if (record.getR124_AMOUNT() != null) {
						cell1.setCellValue(record.getR124_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(124);
					cell1 = row.createCell(1);
					if (record.getR125_AMOUNT() != null) {
						cell1.setCellValue(record.getR125_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(125);
					cell1 = row.createCell(1);
					if (record.getR126_AMOUNT() != null) {
						cell1.setCellValue(record.getR126_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(126);
					cell1 = row.createCell(1);
					if (record.getR127_AMOUNT() != null) {
						cell1.setCellValue(record.getR127_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(127);
					cell1 = row.createCell(1);
					if (record.getR128_AMOUNT() != null) {
						cell1.setCellValue(record.getR128_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(128);
					cell1 = row.createCell(1);
					if (record.getR129_AMOUNT() != null) {
						cell1.setCellValue(record.getR129_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] getExcelExcelRESUB(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		List<FSI_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_BRANCHNET report. Returning empty result.");
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
//--- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					FSI_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.createCell(1);
					if (record.getR12_AMOUNT() != null) {
						cell1.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_AMOUNT() != null) {
						cell1.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_AMOUNT() != null) {
						cell1.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_AMOUNT() != null) {
						cell1.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_AMOUNT() != null) {
						cell1.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_AMOUNT() != null) {
						cell1.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_AMOUNT() != null) {
						cell1.setCellValue(record.getR20_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_AMOUNT() != null) {
						cell1.setCellValue(record.getR23_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_AMOUNT() != null) {
						cell1.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_AMOUNT() != null) {
						cell1.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					if (record.getR27_AMOUNT() != null) {
						cell1.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_AMOUNT() != null) {
						cell1.setCellValue(record.getR29_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_AMOUNT() != null) {
						cell1.setCellValue(record.getR30_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_AMOUNT() != null) {
						cell1.setCellValue(record.getR32_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_AMOUNT() != null) {
						cell1.setCellValue(record.getR42_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_AMOUNT() != null) {
						cell1.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_AMOUNT() != null) {
						cell1.setCellValue(record.getR48_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_AMOUNT() != null) {
						cell1.setCellValue(record.getR49_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					cell1 = row.createCell(1);
					if (record.getR52_AMOUNT() != null) {
						cell1.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(52);
					cell1 = row.createCell(1);
					if (record.getR53_AMOUNT() != null) {
						cell1.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell1 = row.createCell(1);
					if (record.getR54_AMOUNT() != null) {
						cell1.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell1 = row.createCell(1);
					if (record.getR55_AMOUNT() != null) {
						cell1.setCellValue(record.getR55_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell1 = row.createCell(1);
					if (record.getR56_AMOUNT() != null) {
						cell1.setCellValue(record.getR56_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell1 = row.createCell(1);
					if (record.getR57_AMOUNT() != null) {
						cell1.setCellValue(record.getR57_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(57);
					cell1 = row.createCell(1);
					if (record.getR58_AMOUNT() != null) {
						cell1.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(58);
					cell1 = row.createCell(1);
					if (record.getR59_AMOUNT() != null) {
						cell1.setCellValue(record.getR59_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell1 = row.createCell(1);
					if (record.getR60_AMOUNT() != null) {
						cell1.setCellValue(record.getR60_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell1 = row.createCell(1);
					if (record.getR61_AMOUNT() != null) {
						cell1.setCellValue(record.getR61_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell1 = row.createCell(1);
					if (record.getR62_AMOUNT() != null) {
						cell1.setCellValue(record.getR62_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(64);
					cell1 = row.createCell(1);
					if (record.getR65_AMOUNT() != null) {
						cell1.setCellValue(record.getR65_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(66);
					cell1 = row.createCell(1);
					if (record.getR67_AMOUNT() != null) {
						cell1.setCellValue(record.getR67_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(67);
					cell1 = row.createCell(1);
					if (record.getR68_AMOUNT() != null) {
						cell1.setCellValue(record.getR68_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(68);
					cell1 = row.createCell(1);
					if (record.getR69_AMOUNT() != null) {
						cell1.setCellValue(record.getR69_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(69);
					cell1 = row.createCell(1);
					if (record.getR70_AMOUNT() != null) {
						cell1.setCellValue(record.getR70_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(70);
					cell1 = row.createCell(1);
					if (record.getR71_AMOUNT() != null) {
						cell1.setCellValue(record.getR71_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
					cell1 = row.createCell(1);
					if (record.getR72_AMOUNT() != null) {
						cell1.setCellValue(record.getR72_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(73);
					cell1 = row.createCell(1);
					if (record.getR74_AMOUNT() != null) {
						cell1.setCellValue(record.getR74_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(74);
					cell1 = row.createCell(1);
					if (record.getR75_AMOUNT() != null) {
						cell1.setCellValue(record.getR75_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(75);
					cell1 = row.createCell(1);
					if (record.getR76_AMOUNT() != null) {
						cell1.setCellValue(record.getR76_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(85);
					cell1 = row.createCell(1);
					if (record.getR86_AMOUNT() != null) {
						cell1.setCellValue(record.getR86_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(86);
					cell1 = row.createCell(1);
					if (record.getR87_AMOUNT() != null) {
						cell1.setCellValue(record.getR87_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(87);
					cell1 = row.createCell(1);
					if (record.getR88_AMOUNT() != null) {
						cell1.setCellValue(record.getR88_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(88);
					cell1 = row.createCell(1);
					if (record.getR89_AMOUNT() != null) {
						cell1.setCellValue(record.getR89_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(89);
					cell1 = row.createCell(1);
					if (record.getR90_AMOUNT() != null) {
						cell1.setCellValue(record.getR90_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(90);
					cell1 = row.createCell(1);
					if (record.getR91_AMOUNT() != null) {
						cell1.setCellValue(record.getR91_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(92);
					cell1 = row.createCell(1);
					if (record.getR93_AMOUNT() != null) {
						cell1.setCellValue(record.getR93_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(93);
					cell1 = row.createCell(1);
					if (record.getR94_AMOUNT() != null) {
						cell1.setCellValue(record.getR94_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(94);
					cell1 = row.createCell(1);
					if (record.getR95_AMOUNT() != null) {
						cell1.setCellValue(record.getR95_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(95);
					cell1 = row.createCell(1);
					if (record.getR96_AMOUNT() != null) {
						cell1.setCellValue(record.getR96_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(96);
					cell1 = row.createCell(1);
					if (record.getR97_AMOUNT() != null) {
						cell1.setCellValue(record.getR97_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(97);
					cell1 = row.createCell(1);
					if (record.getR98_AMOUNT() != null) {
						cell1.setCellValue(record.getR98_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(98);
					cell1 = row.createCell(1);
					if (record.getR99_AMOUNT() != null) {
						cell1.setCellValue(record.getR99_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(100);
					cell1 = row.createCell(1);
					if (record.getR101_AMOUNT() != null) {
						cell1.setCellValue(record.getR101_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(101);
					cell1 = row.createCell(1);
					if (record.getR102_AMOUNT() != null) {
						cell1.setCellValue(record.getR102_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(102);
					cell1 = row.createCell(1);
					if (record.getR103_AMOUNT() != null) {
						cell1.setCellValue(record.getR103_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(103);
					cell1 = row.createCell(1);
					if (record.getR104_AMOUNT() != null) {
						cell1.setCellValue(record.getR104_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(104);
					cell1 = row.createCell(1);
					if (record.getR105_AMOUNT() != null) {
						cell1.setCellValue(record.getR105_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(106);
					cell1 = row.createCell(1);
					if (record.getR107_AMOUNT() != null) {
						cell1.setCellValue(record.getR107_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(107);
					cell1 = row.createCell(1);
					if (record.getR108_AMOUNT() != null) {
						cell1.setCellValue(record.getR108_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(109);
					cell1 = row.createCell(1);
					if (record.getR110_AMOUNT() != null) {
						cell1.setCellValue(record.getR110_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(110);
					cell1 = row.createCell(1);
					if (record.getR111_AMOUNT() != null) {
						cell1.setCellValue(record.getR111_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(111);
					cell1 = row.createCell(1);
					if (record.getR112_AMOUNT() != null) {
						cell1.setCellValue(record.getR112_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(112);
					cell1 = row.createCell(1);
					if (record.getR113_AMOUNT() != null) {
						cell1.setCellValue(record.getR113_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(113);
					cell1 = row.createCell(1);
					if (record.getR114_AMOUNT() != null) {
						cell1.setCellValue(record.getR114_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(114);
					cell1 = row.createCell(1);
					if (record.getR115_AMOUNT() != null) {
						cell1.setCellValue(record.getR115_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(115);
					cell1 = row.createCell(1);
					if (record.getR116_AMOUNT() != null) {
						cell1.setCellValue(record.getR116_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(116);
					cell1 = row.createCell(1);
					if (record.getR117_AMOUNT() != null) {
						cell1.setCellValue(record.getR117_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(117);
					cell1 = row.createCell(1);
					if (record.getR118_AMOUNT() != null) {
						cell1.setCellValue(record.getR118_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(118);
					cell1 = row.createCell(1);
					if (record.getR119_AMOUNT() != null) {
						cell1.setCellValue(record.getR119_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(119);
					cell1 = row.createCell(1);
					if (record.getR120_AMOUNT() != null) {
						cell1.setCellValue(record.getR120_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(120);
					cell1 = row.createCell(1);
					if (record.getR121_AMOUNT() != null) {
						cell1.setCellValue(record.getR121_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(121);
					cell1 = row.createCell(1);
					if (record.getR122_AMOUNT() != null) {
						cell1.setCellValue(record.getR122_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(122);
					cell1 = row.createCell(1);
					if (record.getR123_AMOUNT() != null) {
						cell1.setCellValue(record.getR123_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(123);
					cell1 = row.createCell(1);
					if (record.getR124_AMOUNT() != null) {
						cell1.setCellValue(record.getR124_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(124);
					cell1 = row.createCell(1);
					if (record.getR125_AMOUNT() != null) {
						cell1.setCellValue(record.getR125_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(125);
					cell1 = row.createCell(1);
					if (record.getR126_AMOUNT() != null) {
						cell1.setCellValue(record.getR126_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(126);
					cell1 = row.createCell(1);
					if (record.getR127_AMOUNT() != null) {
						cell1.setCellValue(record.getR127_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(127);
					cell1 = row.createCell(1);
					if (record.getR128_AMOUNT() != null) {
						cell1.setCellValue(record.getR128_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(128);
					cell1 = row.createCell(1);
					if (record.getR129_AMOUNT() != null) {
						cell1.setCellValue(record.getR129_AMOUNT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

//Resubmission
	public List<Object[]> getFSIResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<FSI_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (FSI_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				FSI_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  FSI  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

}