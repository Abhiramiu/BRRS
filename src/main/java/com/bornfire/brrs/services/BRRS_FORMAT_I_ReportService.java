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
import java.util.Map;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

@Component
@Service

public class BRRS_FORMAT_I_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_I_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	SessionFactory sessionFactory;

	// SUMMARY
	// Fetch data by report date
	public List<FORMAT_I_Summary_Entity> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new FORMAT_I_RowMapper_Summary());
	}

	// ARCHIVAL

	// Fetch data by report date
	public List<FORMAT_I_Archival_Summary_Entity> ArchivalgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new FORMAT_I_RowMapper_Archival());
	}

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<FORMAT_I_Archival_Summary_Entity> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new FORMAT_I_RowMapper_Archival());
	}

	// GET DETAIL FULL DATA BY DATE + VERSION

	public List<FORMAT_I_Detail_Entity> getdatabydateListDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_DETAILTABLE" + "WHERE REPORT_DATE = ? " + "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new FORMAT_IRowMapper_Detail());
	}

	// GET ARCHIVAL DETAIL FULL DATA BY DATE + VERSION

	public List<FORMAT_I_Archival_Detail_Entity> getdatabydateListArchivalDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new FORMAT_IArchivalDetailRowMapper());
	}

	// GET ALL WITH VERSION

	public List<FORMAT_I_Archival_Summary_Entity> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new FORMAT_I_RowMapper_Archival());
	}

	// GET ARCHIVAL MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_FORMAT_I_ARCHIVALTABLE_SUMMARY"
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// DETAIL TABLE 1
	// 1. BY DATE + LABEL + CRITERIA

	public List<FORMAT_I_Detail_Entity> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new FORMAT_IRowMapper_Detail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<FORMAT_I_Detail_Entity> getDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new FORMAT_IRowMapper_Detail());
	}

	// 3. PAGINATION

	public List<FORMAT_I_Detail_Entity> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new FORMAT_IRowMapper_Detail());
	}

	// 4. COUNT

	public int getDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_FORMAT_I_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<FORMAT_I_Detail_Entity> GetDetailDataByRowIdAndColumnId1(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new FORMAT_IRowMapper_Detail());
	}

	// 6. BY ACCOUNT NUMBER

	public FORMAT_I_Detail_Entity findByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new FORMAT_IRowMapper_Detail());
	}

	// ARCHIVALTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<FORMAT_I_Archival_Detail_Entity> findByArchivalDetailReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new FORMAT_IArchivalDetailRowMapper());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<FORMAT_I_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new FORMAT_IArchivalDetailRowMapper());
	}

	// 3. PAGINATION

	public List<FORMAT_I_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate, int offset,
			int limit) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new FORMAT_IArchivalDetailRowMapper());
	}

	// 4. COUNT

	public int getArchivalDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<FORMAT_I_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new FORMAT_IArchivalDetailRowMapper());
	}
	// 6. BY ACCOUNT NUMBER

	public FORMAT_I_Archival_Detail_Entity ArchivalfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new FORMAT_IArchivalDetailRowMapper());
	}

	// 1. GET BY DATE + VERSION

	public List<FORMAT_I_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new FORMAT_IArchivalDetailRowMapper());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<FORMAT_I_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String report_label,
			String report_addl_criteria_1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_FORMAT_I_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql,
				new Object[] { report_label, report_addl_criteria_1, reportdate, dataEntryVersion },
				new FORMAT_IArchivalDetailRowMapper());
	}

	// ROW MAPPER SUMMARY

	class FORMAT_I_RowMapper_Summary implements RowMapper<FORMAT_I_Summary_Entity> {

		@Override
		public FORMAT_I_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_I_Summary_Entity obj = new FORMAT_I_Summary_Entity();

			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_sch_no(rs.getBigDecimal("R12_SCH_NO"));
			obj.setR12_net_amount(rs.getBigDecimal("R12_NET_AMOUNT"));
			obj.setR12_balance_bank(rs.getBigDecimal("R12_BALANCE_BANK"));
			obj.setR12_balance_statement(rs.getBigDecimal("R12_BALANCE_STATEMENT"));

			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_sch_no(rs.getBigDecimal("R13_SCH_NO"));
			obj.setR13_net_amount(rs.getBigDecimal("R13_NET_AMOUNT"));
			obj.setR13_balance_bank(rs.getBigDecimal("R13_BALANCE_BANK"));
			obj.setR13_balance_statement(rs.getBigDecimal("R13_BALANCE_STATEMENT"));

			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_sch_no(rs.getBigDecimal("R14_SCH_NO"));
			obj.setR14_net_amount(rs.getBigDecimal("R14_NET_AMOUNT"));
			obj.setR14_balance_bank(rs.getBigDecimal("R14_BALANCE_BANK"));
			obj.setR14_balance_statement(rs.getBigDecimal("R14_BALANCE_STATEMENT"));

			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_sch_no(rs.getBigDecimal("R15_SCH_NO"));
			obj.setR15_net_amount(rs.getBigDecimal("R15_NET_AMOUNT"));
			obj.setR15_balance_bank(rs.getBigDecimal("R15_BALANCE_BANK"));
			obj.setR15_balance_statement(rs.getBigDecimal("R15_BALANCE_STATEMENT"));

			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_sch_no(rs.getBigDecimal("R16_SCH_NO"));
			obj.setR16_net_amount(rs.getBigDecimal("R16_NET_AMOUNT"));
			obj.setR16_balance_bank(rs.getBigDecimal("R16_BALANCE_BANK"));
			obj.setR16_balance_statement(rs.getBigDecimal("R16_BALANCE_STATEMENT"));

			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_sch_no(rs.getBigDecimal("R17_SCH_NO"));
			obj.setR17_net_amount(rs.getBigDecimal("R17_NET_AMOUNT"));
			obj.setR17_balance_bank(rs.getBigDecimal("R17_BALANCE_BANK"));
			obj.setR17_balance_statement(rs.getBigDecimal("R17_BALANCE_STATEMENT"));

			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_sch_no(rs.getBigDecimal("R18_SCH_NO"));
			obj.setR18_net_amount(rs.getBigDecimal("R18_NET_AMOUNT"));
			obj.setR18_balance_bank(rs.getBigDecimal("R18_BALANCE_BANK"));
			obj.setR18_balance_statement(rs.getBigDecimal("R18_BALANCE_STATEMENT"));

			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_sch_no(rs.getBigDecimal("R19_SCH_NO"));
			obj.setR19_net_amount(rs.getBigDecimal("R19_NET_AMOUNT"));
			obj.setR19_balance_bank(rs.getBigDecimal("R19_BALANCE_BANK"));
			obj.setR19_balance_statement(rs.getBigDecimal("R19_BALANCE_STATEMENT"));

			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_sch_no(rs.getBigDecimal("R20_SCH_NO"));
			obj.setR20_net_amount(rs.getBigDecimal("R20_NET_AMOUNT"));
			obj.setR20_balance_bank(rs.getBigDecimal("R20_BALANCE_BANK"));
			obj.setR20_balance_statement(rs.getBigDecimal("R20_BALANCE_STATEMENT"));

			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_sch_no(rs.getBigDecimal("R21_SCH_NO"));
			obj.setR21_net_amount(rs.getBigDecimal("R21_NET_AMOUNT"));
			obj.setR21_balance_bank(rs.getBigDecimal("R21_BALANCE_BANK"));
			obj.setR21_balance_statement(rs.getBigDecimal("R21_BALANCE_STATEMENT"));

			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_sch_no(rs.getBigDecimal("R22_SCH_NO"));
			obj.setR22_net_amount(rs.getBigDecimal("R22_NET_AMOUNT"));
			obj.setR22_balance_bank(rs.getBigDecimal("R22_BALANCE_BANK"));
			obj.setR22_balance_statement(rs.getBigDecimal("R22_BALANCE_STATEMENT"));

			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_sch_no(rs.getBigDecimal("R23_SCH_NO"));
			obj.setR23_net_amount(rs.getBigDecimal("R23_NET_AMOUNT"));
			obj.setR23_balance_bank(rs.getBigDecimal("R23_BALANCE_BANK"));
			obj.setR23_balance_statement(rs.getBigDecimal("R23_BALANCE_STATEMENT"));

			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_sch_no(rs.getBigDecimal("R24_SCH_NO"));
			obj.setR24_net_amount(rs.getBigDecimal("R24_NET_AMOUNT"));
			obj.setR24_balance_bank(rs.getBigDecimal("R24_BALANCE_BANK"));
			obj.setR24_balance_statement(rs.getBigDecimal("R24_BALANCE_STATEMENT"));

			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_sch_no(rs.getBigDecimal("R25_SCH_NO"));
			obj.setR25_net_amount(rs.getBigDecimal("R25_NET_AMOUNT"));
			obj.setR25_balance_bank(rs.getBigDecimal("R25_BALANCE_BANK"));
			obj.setR25_balance_statement(rs.getBigDecimal("R25_BALANCE_STATEMENT"));

			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_sch_no(rs.getBigDecimal("R26_SCH_NO"));
			obj.setR26_net_amount(rs.getBigDecimal("R26_NET_AMOUNT"));
			obj.setR26_balance_bank(rs.getBigDecimal("R26_BALANCE_BANK"));
			obj.setR26_balance_statement(rs.getBigDecimal("R26_BALANCE_STATEMENT"));

			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_sch_no(rs.getBigDecimal("R27_SCH_NO"));
			obj.setR27_net_amount(rs.getBigDecimal("R27_NET_AMOUNT"));
			obj.setR27_balance_bank(rs.getBigDecimal("R27_BALANCE_BANK"));
			obj.setR27_balance_statement(rs.getBigDecimal("R27_BALANCE_STATEMENT"));

			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_sch_no(rs.getBigDecimal("R28_SCH_NO"));
			obj.setR28_net_amount(rs.getBigDecimal("R28_NET_AMOUNT"));
			obj.setR28_balance_bank(rs.getBigDecimal("R28_BALANCE_BANK"));
			obj.setR28_balance_statement(rs.getBigDecimal("R28_BALANCE_STATEMENT"));

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

	public static class FORMAT_I_Summary_Entity {
		private String r12_product;
		private BigDecimal r12_sch_no;
		private BigDecimal r12_net_amount;
		private BigDecimal r12_balance_bank;
		private BigDecimal r12_balance_statement;
		private String r13_product;
		private BigDecimal r13_sch_no;
		private BigDecimal r13_net_amount;
		private BigDecimal r13_balance_bank;
		private BigDecimal r13_balance_statement;
		private String r14_product;
		private BigDecimal r14_sch_no;
		private BigDecimal r14_net_amount;
		private BigDecimal r14_balance_bank;
		private BigDecimal r14_balance_statement;
		private String r15_product;
		private BigDecimal r15_sch_no;
		private BigDecimal r15_net_amount;
		private BigDecimal r15_balance_bank;
		private BigDecimal r15_balance_statement;
		private String r16_product;
		private BigDecimal r16_sch_no;
		private BigDecimal r16_net_amount;
		private BigDecimal r16_balance_bank;
		private BigDecimal r16_balance_statement;
		private String r17_product;
		private BigDecimal r17_sch_no;
		private BigDecimal r17_net_amount;
		private BigDecimal r17_balance_bank;
		private BigDecimal r17_balance_statement;
		private String r18_product;
		private BigDecimal r18_sch_no;
		private BigDecimal r18_net_amount;
		private BigDecimal r18_balance_bank;
		private BigDecimal r18_balance_statement;
		private String r19_product;
		private BigDecimal r19_sch_no;
		private BigDecimal r19_net_amount;
		private BigDecimal r19_balance_bank;
		private BigDecimal r19_balance_statement;
		private String r20_product;
		private BigDecimal r20_sch_no;
		private BigDecimal r20_net_amount;
		private BigDecimal r20_balance_bank;
		private BigDecimal r20_balance_statement;
		private String r21_product;
		private BigDecimal r21_sch_no;
		private BigDecimal r21_net_amount;
		private BigDecimal r21_balance_bank;
		private BigDecimal r21_balance_statement;
		private String r22_product;
		private BigDecimal r22_sch_no;
		private BigDecimal r22_net_amount;
		private BigDecimal r22_balance_bank;
		private BigDecimal r22_balance_statement;
		private String r23_product;
		private BigDecimal r23_sch_no;
		private BigDecimal r23_net_amount;
		private BigDecimal r23_balance_bank;
		private BigDecimal r23_balance_statement;
		private String r24_product;
		private BigDecimal r24_sch_no;
		private BigDecimal r24_net_amount;
		private BigDecimal r24_balance_bank;
		private BigDecimal r24_balance_statement;
		private String r25_product;
		private BigDecimal r25_sch_no;
		private BigDecimal r25_net_amount;
		private BigDecimal r25_balance_bank;
		private BigDecimal r25_balance_statement;
		private String r26_product;
		private BigDecimal r26_sch_no;
		private BigDecimal r26_net_amount;
		private BigDecimal r26_balance_bank;
		private BigDecimal r26_balance_statement;
		private String r27_product;
		private BigDecimal r27_sch_no;
		private BigDecimal r27_net_amount;
		private BigDecimal r27_balance_bank;
		private BigDecimal r27_balance_statement;
		private String r28_product;
		private BigDecimal r28_sch_no;
		private BigDecimal r28_net_amount;
		private BigDecimal r28_balance_bank;
		private BigDecimal r28_balance_statement;

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

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_sch_no() {
			return r12_sch_no;
		}

		public void setR12_sch_no(BigDecimal r12_sch_no) {
			this.r12_sch_no = r12_sch_no;
		}

		public BigDecimal getR12_net_amount() {
			return r12_net_amount;
		}

		public void setR12_net_amount(BigDecimal r12_net_amount) {
			this.r12_net_amount = r12_net_amount;
		}

		public BigDecimal getR12_balance_bank() {
			return r12_balance_bank;
		}

		public void setR12_balance_bank(BigDecimal r12_balance_bank) {
			this.r12_balance_bank = r12_balance_bank;
		}

		public BigDecimal getR12_balance_statement() {
			return r12_balance_statement;
		}

		public void setR12_balance_statement(BigDecimal r12_balance_statement) {
			this.r12_balance_statement = r12_balance_statement;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_sch_no() {
			return r13_sch_no;
		}

		public void setR13_sch_no(BigDecimal r13_sch_no) {
			this.r13_sch_no = r13_sch_no;
		}

		public BigDecimal getR13_net_amount() {
			return r13_net_amount;
		}

		public void setR13_net_amount(BigDecimal r13_net_amount) {
			this.r13_net_amount = r13_net_amount;
		}

		public BigDecimal getR13_balance_bank() {
			return r13_balance_bank;
		}

		public void setR13_balance_bank(BigDecimal r13_balance_bank) {
			this.r13_balance_bank = r13_balance_bank;
		}

		public BigDecimal getR13_balance_statement() {
			return r13_balance_statement;
		}

		public void setR13_balance_statement(BigDecimal r13_balance_statement) {
			this.r13_balance_statement = r13_balance_statement;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_sch_no() {
			return r14_sch_no;
		}

		public void setR14_sch_no(BigDecimal r14_sch_no) {
			this.r14_sch_no = r14_sch_no;
		}

		public BigDecimal getR14_net_amount() {
			return r14_net_amount;
		}

		public void setR14_net_amount(BigDecimal r14_net_amount) {
			this.r14_net_amount = r14_net_amount;
		}

		public BigDecimal getR14_balance_bank() {
			return r14_balance_bank;
		}

		public void setR14_balance_bank(BigDecimal r14_balance_bank) {
			this.r14_balance_bank = r14_balance_bank;
		}

		public BigDecimal getR14_balance_statement() {
			return r14_balance_statement;
		}

		public void setR14_balance_statement(BigDecimal r14_balance_statement) {
			this.r14_balance_statement = r14_balance_statement;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_sch_no() {
			return r15_sch_no;
		}

		public void setR15_sch_no(BigDecimal r15_sch_no) {
			this.r15_sch_no = r15_sch_no;
		}

		public BigDecimal getR15_net_amount() {
			return r15_net_amount;
		}

		public void setR15_net_amount(BigDecimal r15_net_amount) {
			this.r15_net_amount = r15_net_amount;
		}

		public BigDecimal getR15_balance_bank() {
			return r15_balance_bank;
		}

		public void setR15_balance_bank(BigDecimal r15_balance_bank) {
			this.r15_balance_bank = r15_balance_bank;
		}

		public BigDecimal getR15_balance_statement() {
			return r15_balance_statement;
		}

		public void setR15_balance_statement(BigDecimal r15_balance_statement) {
			this.r15_balance_statement = r15_balance_statement;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_sch_no() {
			return r16_sch_no;
		}

		public void setR16_sch_no(BigDecimal r16_sch_no) {
			this.r16_sch_no = r16_sch_no;
		}

		public BigDecimal getR16_net_amount() {
			return r16_net_amount;
		}

		public void setR16_net_amount(BigDecimal r16_net_amount) {
			this.r16_net_amount = r16_net_amount;
		}

		public BigDecimal getR16_balance_bank() {
			return r16_balance_bank;
		}

		public void setR16_balance_bank(BigDecimal r16_balance_bank) {
			this.r16_balance_bank = r16_balance_bank;
		}

		public BigDecimal getR16_balance_statement() {
			return r16_balance_statement;
		}

		public void setR16_balance_statement(BigDecimal r16_balance_statement) {
			this.r16_balance_statement = r16_balance_statement;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_sch_no() {
			return r17_sch_no;
		}

		public void setR17_sch_no(BigDecimal r17_sch_no) {
			this.r17_sch_no = r17_sch_no;
		}

		public BigDecimal getR17_net_amount() {
			return r17_net_amount;
		}

		public void setR17_net_amount(BigDecimal r17_net_amount) {
			this.r17_net_amount = r17_net_amount;
		}

		public BigDecimal getR17_balance_bank() {
			return r17_balance_bank;
		}

		public void setR17_balance_bank(BigDecimal r17_balance_bank) {
			this.r17_balance_bank = r17_balance_bank;
		}

		public BigDecimal getR17_balance_statement() {
			return r17_balance_statement;
		}

		public void setR17_balance_statement(BigDecimal r17_balance_statement) {
			this.r17_balance_statement = r17_balance_statement;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_sch_no() {
			return r18_sch_no;
		}

		public void setR18_sch_no(BigDecimal r18_sch_no) {
			this.r18_sch_no = r18_sch_no;
		}

		public BigDecimal getR18_net_amount() {
			return r18_net_amount;
		}

		public void setR18_net_amount(BigDecimal r18_net_amount) {
			this.r18_net_amount = r18_net_amount;
		}

		public BigDecimal getR18_balance_bank() {
			return r18_balance_bank;
		}

		public void setR18_balance_bank(BigDecimal r18_balance_bank) {
			this.r18_balance_bank = r18_balance_bank;
		}

		public BigDecimal getR18_balance_statement() {
			return r18_balance_statement;
		}

		public void setR18_balance_statement(BigDecimal r18_balance_statement) {
			this.r18_balance_statement = r18_balance_statement;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_sch_no() {
			return r19_sch_no;
		}

		public void setR19_sch_no(BigDecimal r19_sch_no) {
			this.r19_sch_no = r19_sch_no;
		}

		public BigDecimal getR19_net_amount() {
			return r19_net_amount;
		}

		public void setR19_net_amount(BigDecimal r19_net_amount) {
			this.r19_net_amount = r19_net_amount;
		}

		public BigDecimal getR19_balance_bank() {
			return r19_balance_bank;
		}

		public void setR19_balance_bank(BigDecimal r19_balance_bank) {
			this.r19_balance_bank = r19_balance_bank;
		}

		public BigDecimal getR19_balance_statement() {
			return r19_balance_statement;
		}

		public void setR19_balance_statement(BigDecimal r19_balance_statement) {
			this.r19_balance_statement = r19_balance_statement;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_sch_no() {
			return r20_sch_no;
		}

		public void setR20_sch_no(BigDecimal r20_sch_no) {
			this.r20_sch_no = r20_sch_no;
		}

		public BigDecimal getR20_net_amount() {
			return r20_net_amount;
		}

		public void setR20_net_amount(BigDecimal r20_net_amount) {
			this.r20_net_amount = r20_net_amount;
		}

		public BigDecimal getR20_balance_bank() {
			return r20_balance_bank;
		}

		public void setR20_balance_bank(BigDecimal r20_balance_bank) {
			this.r20_balance_bank = r20_balance_bank;
		}

		public BigDecimal getR20_balance_statement() {
			return r20_balance_statement;
		}

		public void setR20_balance_statement(BigDecimal r20_balance_statement) {
			this.r20_balance_statement = r20_balance_statement;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_sch_no() {
			return r21_sch_no;
		}

		public void setR21_sch_no(BigDecimal r21_sch_no) {
			this.r21_sch_no = r21_sch_no;
		}

		public BigDecimal getR21_net_amount() {
			return r21_net_amount;
		}

		public void setR21_net_amount(BigDecimal r21_net_amount) {
			this.r21_net_amount = r21_net_amount;
		}

		public BigDecimal getR21_balance_bank() {
			return r21_balance_bank;
		}

		public void setR21_balance_bank(BigDecimal r21_balance_bank) {
			this.r21_balance_bank = r21_balance_bank;
		}

		public BigDecimal getR21_balance_statement() {
			return r21_balance_statement;
		}

		public void setR21_balance_statement(BigDecimal r21_balance_statement) {
			this.r21_balance_statement = r21_balance_statement;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_sch_no() {
			return r22_sch_no;
		}

		public void setR22_sch_no(BigDecimal r22_sch_no) {
			this.r22_sch_no = r22_sch_no;
		}

		public BigDecimal getR22_net_amount() {
			return r22_net_amount;
		}

		public void setR22_net_amount(BigDecimal r22_net_amount) {
			this.r22_net_amount = r22_net_amount;
		}

		public BigDecimal getR22_balance_bank() {
			return r22_balance_bank;
		}

		public void setR22_balance_bank(BigDecimal r22_balance_bank) {
			this.r22_balance_bank = r22_balance_bank;
		}

		public BigDecimal getR22_balance_statement() {
			return r22_balance_statement;
		}

		public void setR22_balance_statement(BigDecimal r22_balance_statement) {
			this.r22_balance_statement = r22_balance_statement;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_sch_no() {
			return r23_sch_no;
		}

		public void setR23_sch_no(BigDecimal r23_sch_no) {
			this.r23_sch_no = r23_sch_no;
		}

		public BigDecimal getR23_net_amount() {
			return r23_net_amount;
		}

		public void setR23_net_amount(BigDecimal r23_net_amount) {
			this.r23_net_amount = r23_net_amount;
		}

		public BigDecimal getR23_balance_bank() {
			return r23_balance_bank;
		}

		public void setR23_balance_bank(BigDecimal r23_balance_bank) {
			this.r23_balance_bank = r23_balance_bank;
		}

		public BigDecimal getR23_balance_statement() {
			return r23_balance_statement;
		}

		public void setR23_balance_statement(BigDecimal r23_balance_statement) {
			this.r23_balance_statement = r23_balance_statement;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_sch_no() {
			return r24_sch_no;
		}

		public void setR24_sch_no(BigDecimal r24_sch_no) {
			this.r24_sch_no = r24_sch_no;
		}

		public BigDecimal getR24_net_amount() {
			return r24_net_amount;
		}

		public void setR24_net_amount(BigDecimal r24_net_amount) {
			this.r24_net_amount = r24_net_amount;
		}

		public BigDecimal getR24_balance_bank() {
			return r24_balance_bank;
		}

		public void setR24_balance_bank(BigDecimal r24_balance_bank) {
			this.r24_balance_bank = r24_balance_bank;
		}

		public BigDecimal getR24_balance_statement() {
			return r24_balance_statement;
		}

		public void setR24_balance_statement(BigDecimal r24_balance_statement) {
			this.r24_balance_statement = r24_balance_statement;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_sch_no() {
			return r25_sch_no;
		}

		public void setR25_sch_no(BigDecimal r25_sch_no) {
			this.r25_sch_no = r25_sch_no;
		}

		public BigDecimal getR25_net_amount() {
			return r25_net_amount;
		}

		public void setR25_net_amount(BigDecimal r25_net_amount) {
			this.r25_net_amount = r25_net_amount;
		}

		public BigDecimal getR25_balance_bank() {
			return r25_balance_bank;
		}

		public void setR25_balance_bank(BigDecimal r25_balance_bank) {
			this.r25_balance_bank = r25_balance_bank;
		}

		public BigDecimal getR25_balance_statement() {
			return r25_balance_statement;
		}

		public void setR25_balance_statement(BigDecimal r25_balance_statement) {
			this.r25_balance_statement = r25_balance_statement;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_sch_no() {
			return r26_sch_no;
		}

		public void setR26_sch_no(BigDecimal r26_sch_no) {
			this.r26_sch_no = r26_sch_no;
		}

		public BigDecimal getR26_net_amount() {
			return r26_net_amount;
		}

		public void setR26_net_amount(BigDecimal r26_net_amount) {
			this.r26_net_amount = r26_net_amount;
		}

		public BigDecimal getR26_balance_bank() {
			return r26_balance_bank;
		}

		public void setR26_balance_bank(BigDecimal r26_balance_bank) {
			this.r26_balance_bank = r26_balance_bank;
		}

		public BigDecimal getR26_balance_statement() {
			return r26_balance_statement;
		}

		public void setR26_balance_statement(BigDecimal r26_balance_statement) {
			this.r26_balance_statement = r26_balance_statement;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_sch_no() {
			return r27_sch_no;
		}

		public void setR27_sch_no(BigDecimal r27_sch_no) {
			this.r27_sch_no = r27_sch_no;
		}

		public BigDecimal getR27_net_amount() {
			return r27_net_amount;
		}

		public void setR27_net_amount(BigDecimal r27_net_amount) {
			this.r27_net_amount = r27_net_amount;
		}

		public BigDecimal getR27_balance_bank() {
			return r27_balance_bank;
		}

		public void setR27_balance_bank(BigDecimal r27_balance_bank) {
			this.r27_balance_bank = r27_balance_bank;
		}

		public BigDecimal getR27_balance_statement() {
			return r27_balance_statement;
		}

		public void setR27_balance_statement(BigDecimal r27_balance_statement) {
			this.r27_balance_statement = r27_balance_statement;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_sch_no() {
			return r28_sch_no;
		}

		public void setR28_sch_no(BigDecimal r28_sch_no) {
			this.r28_sch_no = r28_sch_no;
		}

		public BigDecimal getR28_net_amount() {
			return r28_net_amount;
		}

		public void setR28_net_amount(BigDecimal r28_net_amount) {
			this.r28_net_amount = r28_net_amount;
		}

		public BigDecimal getR28_balance_bank() {
			return r28_balance_bank;
		}

		public void setR28_balance_bank(BigDecimal r28_balance_bank) {
			this.r28_balance_bank = r28_balance_bank;
		}

		public BigDecimal getR28_balance_statement() {
			return r28_balance_statement;
		}

		public void setR28_balance_statement(BigDecimal r28_balance_statement) {
			this.r28_balance_statement = r28_balance_statement;
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

	// =====================================================
	// DETAIL ENTITY
	// =====================================================

	public class FORMAT_IRowMapper_Detail implements RowMapper<FORMAT_I_Detail_Entity> {

		@Override
		public FORMAT_I_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_I_Detail_Entity obj = new FORMAT_I_Detail_Entity();

			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getDate("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getDate("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getDate("VERIFY_TIME"));

			obj.setEntity_flg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			return obj;
		}
	}

	public class FORMAT_I_Detail_Entity {

		@Column(name = "CUST_ID")
		private String cust_id;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acct_number;

		@Column(name = "ACCT_NAME")
		private String acct_name;

		@Column(name = "DATA_TYPE")
		private String data_type;

		@Column(name = "REPORT_LABEL")
		private String report_label;

		@Column(name = "REPORT_REMARKS")
		private String report_remarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modification_remarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String data_entry_version;

		@Column(name = "ACCT_BALANCE_IN_PULA")
		private BigDecimal acct_balance_in_pula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;

		@Column(name = "REPORT_NAME")
		private String report_name;

		@Column(name = "CREATE_USER")
		private String create_user;

		@Column(name = "CREATE_TIME")
		private Date create_time;

		@Column(name = "MODIFY_USER")
		private String modify_user;

		@Column(name = "MODIFY_TIME")
		private Date modify_time;

		@Column(name = "VERIFY_USER")
		private String verify_user;

		@Column(name = "VERIFY_TIME")
		private Date verify_time;

		@Column(name = "ENTITY_FLG")
		private Character entity_flg;

		@Column(name = "MODIFY_FLG")
		private Character modify_flg;

		@Column(name = "DEL_FLG")
		private Character del_flg;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String report_addl_criteria_1;

		public String getCust_id() {
			return cust_id;
		}

		public void setCust_id(String cust_id) {
			this.cust_id = cust_id;
		}

		public String getAcct_number() {
			return acct_number;
		}

		public void setAcct_number(String acct_number) {
			this.acct_number = acct_number;
		}

		public String getAcct_name() {
			return acct_name;
		}

		public void setAcct_name(String acct_name) {
			this.acct_name = acct_name;
		}

		public String getData_type() {
			return data_type;
		}

		public void setData_type(String data_type) {
			this.data_type = data_type;
		}

		public String getReport_label() {
			return report_label;
		}

		public void setReport_label(String report_label) {
			this.report_label = report_label;
		}

		public String getReport_remarks() {
			return report_remarks;
		}

		public void setReport_remarks(String report_remarks) {
			this.report_remarks = report_remarks;
		}

		public String getModification_remarks() {
			return modification_remarks;
		}

		public void setModification_remarks(String modification_remarks) {
			this.modification_remarks = modification_remarks;
		}

		public String getData_entry_version() {
			return data_entry_version;
		}

		public void setData_entry_version(String data_entry_version) {
			this.data_entry_version = data_entry_version;
		}

		public BigDecimal getAcct_balance_in_pula() {
			return acct_balance_in_pula;
		}

		public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
			this.acct_balance_in_pula = acct_balance_in_pula;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_name() {
			return report_name;
		}

		public void setReport_name(String report_name) {
			this.report_name = report_name;
		}

		public String getCreate_user() {
			return create_user;
		}

		public void setCreate_user(String create_user) {
			this.create_user = create_user;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}

		public String getModify_user() {
			return modify_user;
		}

		public void setModify_user(String modify_user) {
			this.modify_user = modify_user;
		}

		public Date getModify_time() {
			return modify_time;
		}

		public void setModify_time(Date modify_time) {
			this.modify_time = modify_time;
		}

		public String getVerify_user() {
			return verify_user;
		}

		public void setVerify_user(String verify_user) {
			this.verify_user = verify_user;
		}

		public Date getVerify_time() {
			return verify_time;
		}

		public void setVerify_time(Date verify_time) {
			this.verify_time = verify_time;
		}

		public Character getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(Character entity_flg) {
			this.entity_flg = entity_flg;
		}

		public Character getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(Character modify_flg) {
			this.modify_flg = modify_flg;
		}

		public Character getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(Character del_flg) {
			this.del_flg = del_flg;
		}

		public String getReport_addl_criteria_1() {
			return report_addl_criteria_1;
		}

		public void setReport_addl_criteria_1(String report_addl_criteria_1) {
			this.report_addl_criteria_1 = report_addl_criteria_1;
		}

	}

	// ROW MAPPER ARCHIVAL SUMMARY

	class FORMAT_I_RowMapper_Archival implements RowMapper<FORMAT_I_Archival_Summary_Entity> {

		@Override
		public FORMAT_I_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_I_Archival_Summary_Entity obj = new FORMAT_I_Archival_Summary_Entity();

			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_sch_no(rs.getBigDecimal("R12_SCH_NO"));
			obj.setR12_net_amount(rs.getBigDecimal("R12_NET_AMOUNT"));
			obj.setR12_balance_bank(rs.getBigDecimal("R12_BALANCE_BANK"));
			obj.setR12_balance_statement(rs.getBigDecimal("R12_BALANCE_STATEMENT"));

			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_sch_no(rs.getBigDecimal("R13_SCH_NO"));
			obj.setR13_net_amount(rs.getBigDecimal("R13_NET_AMOUNT"));
			obj.setR13_balance_bank(rs.getBigDecimal("R13_BALANCE_BANK"));
			obj.setR13_balance_statement(rs.getBigDecimal("R13_BALANCE_STATEMENT"));

			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_sch_no(rs.getBigDecimal("R14_SCH_NO"));
			obj.setR14_net_amount(rs.getBigDecimal("R14_NET_AMOUNT"));
			obj.setR14_balance_bank(rs.getBigDecimal("R14_BALANCE_BANK"));
			obj.setR14_balance_statement(rs.getBigDecimal("R14_BALANCE_STATEMENT"));

			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_sch_no(rs.getBigDecimal("R15_SCH_NO"));
			obj.setR15_net_amount(rs.getBigDecimal("R15_NET_AMOUNT"));
			obj.setR15_balance_bank(rs.getBigDecimal("R15_BALANCE_BANK"));
			obj.setR15_balance_statement(rs.getBigDecimal("R15_BALANCE_STATEMENT"));

			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_sch_no(rs.getBigDecimal("R16_SCH_NO"));
			obj.setR16_net_amount(rs.getBigDecimal("R16_NET_AMOUNT"));
			obj.setR16_balance_bank(rs.getBigDecimal("R16_BALANCE_BANK"));
			obj.setR16_balance_statement(rs.getBigDecimal("R16_BALANCE_STATEMENT"));

			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_sch_no(rs.getBigDecimal("R17_SCH_NO"));
			obj.setR17_net_amount(rs.getBigDecimal("R17_NET_AMOUNT"));
			obj.setR17_balance_bank(rs.getBigDecimal("R17_BALANCE_BANK"));
			obj.setR17_balance_statement(rs.getBigDecimal("R17_BALANCE_STATEMENT"));

			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_sch_no(rs.getBigDecimal("R18_SCH_NO"));
			obj.setR18_net_amount(rs.getBigDecimal("R18_NET_AMOUNT"));
			obj.setR18_balance_bank(rs.getBigDecimal("R18_BALANCE_BANK"));
			obj.setR18_balance_statement(rs.getBigDecimal("R18_BALANCE_STATEMENT"));

			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_sch_no(rs.getBigDecimal("R19_SCH_NO"));
			obj.setR19_net_amount(rs.getBigDecimal("R19_NET_AMOUNT"));
			obj.setR19_balance_bank(rs.getBigDecimal("R19_BALANCE_BANK"));
			obj.setR19_balance_statement(rs.getBigDecimal("R19_BALANCE_STATEMENT"));

			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_sch_no(rs.getBigDecimal("R20_SCH_NO"));
			obj.setR20_net_amount(rs.getBigDecimal("R20_NET_AMOUNT"));
			obj.setR20_balance_bank(rs.getBigDecimal("R20_BALANCE_BANK"));
			obj.setR20_balance_statement(rs.getBigDecimal("R20_BALANCE_STATEMENT"));

			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_sch_no(rs.getBigDecimal("R21_SCH_NO"));
			obj.setR21_net_amount(rs.getBigDecimal("R21_NET_AMOUNT"));
			obj.setR21_balance_bank(rs.getBigDecimal("R21_BALANCE_BANK"));
			obj.setR21_balance_statement(rs.getBigDecimal("R21_BALANCE_STATEMENT"));

			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_sch_no(rs.getBigDecimal("R22_SCH_NO"));
			obj.setR22_net_amount(rs.getBigDecimal("R22_NET_AMOUNT"));
			obj.setR22_balance_bank(rs.getBigDecimal("R22_BALANCE_BANK"));
			obj.setR22_balance_statement(rs.getBigDecimal("R22_BALANCE_STATEMENT"));

			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_sch_no(rs.getBigDecimal("R23_SCH_NO"));
			obj.setR23_net_amount(rs.getBigDecimal("R23_NET_AMOUNT"));
			obj.setR23_balance_bank(rs.getBigDecimal("R23_BALANCE_BANK"));
			obj.setR23_balance_statement(rs.getBigDecimal("R23_BALANCE_STATEMENT"));

			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_sch_no(rs.getBigDecimal("R24_SCH_NO"));
			obj.setR24_net_amount(rs.getBigDecimal("R24_NET_AMOUNT"));
			obj.setR24_balance_bank(rs.getBigDecimal("R24_BALANCE_BANK"));
			obj.setR24_balance_statement(rs.getBigDecimal("R24_BALANCE_STATEMENT"));

			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_sch_no(rs.getBigDecimal("R25_SCH_NO"));
			obj.setR25_net_amount(rs.getBigDecimal("R25_NET_AMOUNT"));
			obj.setR25_balance_bank(rs.getBigDecimal("R25_BALANCE_BANK"));
			obj.setR25_balance_statement(rs.getBigDecimal("R25_BALANCE_STATEMENT"));

			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_sch_no(rs.getBigDecimal("R26_SCH_NO"));
			obj.setR26_net_amount(rs.getBigDecimal("R26_NET_AMOUNT"));
			obj.setR26_balance_bank(rs.getBigDecimal("R26_BALANCE_BANK"));
			obj.setR26_balance_statement(rs.getBigDecimal("R26_BALANCE_STATEMENT"));

			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_sch_no(rs.getBigDecimal("R27_SCH_NO"));
			obj.setR27_net_amount(rs.getBigDecimal("R27_NET_AMOUNT"));
			obj.setR27_balance_bank(rs.getBigDecimal("R27_BALANCE_BANK"));
			obj.setR27_balance_statement(rs.getBigDecimal("R27_BALANCE_STATEMENT"));

			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_sch_no(rs.getBigDecimal("R28_SCH_NO"));
			obj.setR28_net_amount(rs.getBigDecimal("R28_NET_AMOUNT"));
			obj.setR28_balance_bank(rs.getBigDecimal("R28_BALANCE_BANK"));
			obj.setR28_balance_statement(rs.getBigDecimal("R28_BALANCE_STATEMENT"));

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

	public static class FORMAT_I_Archival_Summary_Entity {
		private String r12_product;
		private BigDecimal r12_sch_no;
		private BigDecimal r12_net_amount;
		private BigDecimal r12_balance_bank;
		private BigDecimal r12_balance_statement;
		private String r13_product;
		private BigDecimal r13_sch_no;
		private BigDecimal r13_net_amount;
		private BigDecimal r13_balance_bank;
		private BigDecimal r13_balance_statement;
		private String r14_product;
		private BigDecimal r14_sch_no;
		private BigDecimal r14_net_amount;
		private BigDecimal r14_balance_bank;
		private BigDecimal r14_balance_statement;
		private String r15_product;
		private BigDecimal r15_sch_no;
		private BigDecimal r15_net_amount;
		private BigDecimal r15_balance_bank;
		private BigDecimal r15_balance_statement;
		private String r16_product;
		private BigDecimal r16_sch_no;
		private BigDecimal r16_net_amount;
		private BigDecimal r16_balance_bank;
		private BigDecimal r16_balance_statement;
		private String r17_product;
		private BigDecimal r17_sch_no;
		private BigDecimal r17_net_amount;
		private BigDecimal r17_balance_bank;
		private BigDecimal r17_balance_statement;
		private String r18_product;
		private BigDecimal r18_sch_no;
		private BigDecimal r18_net_amount;
		private BigDecimal r18_balance_bank;
		private BigDecimal r18_balance_statement;
		private String r19_product;
		private BigDecimal r19_sch_no;
		private BigDecimal r19_net_amount;
		private BigDecimal r19_balance_bank;
		private BigDecimal r19_balance_statement;
		private String r20_product;
		private BigDecimal r20_sch_no;
		private BigDecimal r20_net_amount;
		private BigDecimal r20_balance_bank;
		private BigDecimal r20_balance_statement;
		private String r21_product;
		private BigDecimal r21_sch_no;
		private BigDecimal r21_net_amount;
		private BigDecimal r21_balance_bank;
		private BigDecimal r21_balance_statement;
		private String r22_product;
		private BigDecimal r22_sch_no;
		private BigDecimal r22_net_amount;
		private BigDecimal r22_balance_bank;
		private BigDecimal r22_balance_statement;
		private String r23_product;
		private BigDecimal r23_sch_no;
		private BigDecimal r23_net_amount;
		private BigDecimal r23_balance_bank;
		private BigDecimal r23_balance_statement;
		private String r24_product;
		private BigDecimal r24_sch_no;
		private BigDecimal r24_net_amount;
		private BigDecimal r24_balance_bank;
		private BigDecimal r24_balance_statement;
		private String r25_product;
		private BigDecimal r25_sch_no;
		private BigDecimal r25_net_amount;
		private BigDecimal r25_balance_bank;
		private BigDecimal r25_balance_statement;
		private String r26_product;
		private BigDecimal r26_sch_no;
		private BigDecimal r26_net_amount;
		private BigDecimal r26_balance_bank;
		private BigDecimal r26_balance_statement;
		private String r27_product;
		private BigDecimal r27_sch_no;
		private BigDecimal r27_net_amount;
		private BigDecimal r27_balance_bank;
		private BigDecimal r27_balance_statement;
		private String r28_product;
		private BigDecimal r28_sch_no;
		private BigDecimal r28_net_amount;
		private BigDecimal r28_balance_bank;
		private BigDecimal r28_balance_statement;

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

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_sch_no() {
			return r12_sch_no;
		}

		public void setR12_sch_no(BigDecimal r12_sch_no) {
			this.r12_sch_no = r12_sch_no;
		}

		public BigDecimal getR12_net_amount() {
			return r12_net_amount;
		}

		public void setR12_net_amount(BigDecimal r12_net_amount) {
			this.r12_net_amount = r12_net_amount;
		}

		public BigDecimal getR12_balance_bank() {
			return r12_balance_bank;
		}

		public void setR12_balance_bank(BigDecimal r12_balance_bank) {
			this.r12_balance_bank = r12_balance_bank;
		}

		public BigDecimal getR12_balance_statement() {
			return r12_balance_statement;
		}

		public void setR12_balance_statement(BigDecimal r12_balance_statement) {
			this.r12_balance_statement = r12_balance_statement;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_sch_no() {
			return r13_sch_no;
		}

		public void setR13_sch_no(BigDecimal r13_sch_no) {
			this.r13_sch_no = r13_sch_no;
		}

		public BigDecimal getR13_net_amount() {
			return r13_net_amount;
		}

		public void setR13_net_amount(BigDecimal r13_net_amount) {
			this.r13_net_amount = r13_net_amount;
		}

		public BigDecimal getR13_balance_bank() {
			return r13_balance_bank;
		}

		public void setR13_balance_bank(BigDecimal r13_balance_bank) {
			this.r13_balance_bank = r13_balance_bank;
		}

		public BigDecimal getR13_balance_statement() {
			return r13_balance_statement;
		}

		public void setR13_balance_statement(BigDecimal r13_balance_statement) {
			this.r13_balance_statement = r13_balance_statement;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_sch_no() {
			return r14_sch_no;
		}

		public void setR14_sch_no(BigDecimal r14_sch_no) {
			this.r14_sch_no = r14_sch_no;
		}

		public BigDecimal getR14_net_amount() {
			return r14_net_amount;
		}

		public void setR14_net_amount(BigDecimal r14_net_amount) {
			this.r14_net_amount = r14_net_amount;
		}

		public BigDecimal getR14_balance_bank() {
			return r14_balance_bank;
		}

		public void setR14_balance_bank(BigDecimal r14_balance_bank) {
			this.r14_balance_bank = r14_balance_bank;
		}

		public BigDecimal getR14_balance_statement() {
			return r14_balance_statement;
		}

		public void setR14_balance_statement(BigDecimal r14_balance_statement) {
			this.r14_balance_statement = r14_balance_statement;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_sch_no() {
			return r15_sch_no;
		}

		public void setR15_sch_no(BigDecimal r15_sch_no) {
			this.r15_sch_no = r15_sch_no;
		}

		public BigDecimal getR15_net_amount() {
			return r15_net_amount;
		}

		public void setR15_net_amount(BigDecimal r15_net_amount) {
			this.r15_net_amount = r15_net_amount;
		}

		public BigDecimal getR15_balance_bank() {
			return r15_balance_bank;
		}

		public void setR15_balance_bank(BigDecimal r15_balance_bank) {
			this.r15_balance_bank = r15_balance_bank;
		}

		public BigDecimal getR15_balance_statement() {
			return r15_balance_statement;
		}

		public void setR15_balance_statement(BigDecimal r15_balance_statement) {
			this.r15_balance_statement = r15_balance_statement;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_sch_no() {
			return r16_sch_no;
		}

		public void setR16_sch_no(BigDecimal r16_sch_no) {
			this.r16_sch_no = r16_sch_no;
		}

		public BigDecimal getR16_net_amount() {
			return r16_net_amount;
		}

		public void setR16_net_amount(BigDecimal r16_net_amount) {
			this.r16_net_amount = r16_net_amount;
		}

		public BigDecimal getR16_balance_bank() {
			return r16_balance_bank;
		}

		public void setR16_balance_bank(BigDecimal r16_balance_bank) {
			this.r16_balance_bank = r16_balance_bank;
		}

		public BigDecimal getR16_balance_statement() {
			return r16_balance_statement;
		}

		public void setR16_balance_statement(BigDecimal r16_balance_statement) {
			this.r16_balance_statement = r16_balance_statement;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_sch_no() {
			return r17_sch_no;
		}

		public void setR17_sch_no(BigDecimal r17_sch_no) {
			this.r17_sch_no = r17_sch_no;
		}

		public BigDecimal getR17_net_amount() {
			return r17_net_amount;
		}

		public void setR17_net_amount(BigDecimal r17_net_amount) {
			this.r17_net_amount = r17_net_amount;
		}

		public BigDecimal getR17_balance_bank() {
			return r17_balance_bank;
		}

		public void setR17_balance_bank(BigDecimal r17_balance_bank) {
			this.r17_balance_bank = r17_balance_bank;
		}

		public BigDecimal getR17_balance_statement() {
			return r17_balance_statement;
		}

		public void setR17_balance_statement(BigDecimal r17_balance_statement) {
			this.r17_balance_statement = r17_balance_statement;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_sch_no() {
			return r18_sch_no;
		}

		public void setR18_sch_no(BigDecimal r18_sch_no) {
			this.r18_sch_no = r18_sch_no;
		}

		public BigDecimal getR18_net_amount() {
			return r18_net_amount;
		}

		public void setR18_net_amount(BigDecimal r18_net_amount) {
			this.r18_net_amount = r18_net_amount;
		}

		public BigDecimal getR18_balance_bank() {
			return r18_balance_bank;
		}

		public void setR18_balance_bank(BigDecimal r18_balance_bank) {
			this.r18_balance_bank = r18_balance_bank;
		}

		public BigDecimal getR18_balance_statement() {
			return r18_balance_statement;
		}

		public void setR18_balance_statement(BigDecimal r18_balance_statement) {
			this.r18_balance_statement = r18_balance_statement;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_sch_no() {
			return r19_sch_no;
		}

		public void setR19_sch_no(BigDecimal r19_sch_no) {
			this.r19_sch_no = r19_sch_no;
		}

		public BigDecimal getR19_net_amount() {
			return r19_net_amount;
		}

		public void setR19_net_amount(BigDecimal r19_net_amount) {
			this.r19_net_amount = r19_net_amount;
		}

		public BigDecimal getR19_balance_bank() {
			return r19_balance_bank;
		}

		public void setR19_balance_bank(BigDecimal r19_balance_bank) {
			this.r19_balance_bank = r19_balance_bank;
		}

		public BigDecimal getR19_balance_statement() {
			return r19_balance_statement;
		}

		public void setR19_balance_statement(BigDecimal r19_balance_statement) {
			this.r19_balance_statement = r19_balance_statement;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_sch_no() {
			return r20_sch_no;
		}

		public void setR20_sch_no(BigDecimal r20_sch_no) {
			this.r20_sch_no = r20_sch_no;
		}

		public BigDecimal getR20_net_amount() {
			return r20_net_amount;
		}

		public void setR20_net_amount(BigDecimal r20_net_amount) {
			this.r20_net_amount = r20_net_amount;
		}

		public BigDecimal getR20_balance_bank() {
			return r20_balance_bank;
		}

		public void setR20_balance_bank(BigDecimal r20_balance_bank) {
			this.r20_balance_bank = r20_balance_bank;
		}

		public BigDecimal getR20_balance_statement() {
			return r20_balance_statement;
		}

		public void setR20_balance_statement(BigDecimal r20_balance_statement) {
			this.r20_balance_statement = r20_balance_statement;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_sch_no() {
			return r21_sch_no;
		}

		public void setR21_sch_no(BigDecimal r21_sch_no) {
			this.r21_sch_no = r21_sch_no;
		}

		public BigDecimal getR21_net_amount() {
			return r21_net_amount;
		}

		public void setR21_net_amount(BigDecimal r21_net_amount) {
			this.r21_net_amount = r21_net_amount;
		}

		public BigDecimal getR21_balance_bank() {
			return r21_balance_bank;
		}

		public void setR21_balance_bank(BigDecimal r21_balance_bank) {
			this.r21_balance_bank = r21_balance_bank;
		}

		public BigDecimal getR21_balance_statement() {
			return r21_balance_statement;
		}

		public void setR21_balance_statement(BigDecimal r21_balance_statement) {
			this.r21_balance_statement = r21_balance_statement;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_sch_no() {
			return r22_sch_no;
		}

		public void setR22_sch_no(BigDecimal r22_sch_no) {
			this.r22_sch_no = r22_sch_no;
		}

		public BigDecimal getR22_net_amount() {
			return r22_net_amount;
		}

		public void setR22_net_amount(BigDecimal r22_net_amount) {
			this.r22_net_amount = r22_net_amount;
		}

		public BigDecimal getR22_balance_bank() {
			return r22_balance_bank;
		}

		public void setR22_balance_bank(BigDecimal r22_balance_bank) {
			this.r22_balance_bank = r22_balance_bank;
		}

		public BigDecimal getR22_balance_statement() {
			return r22_balance_statement;
		}

		public void setR22_balance_statement(BigDecimal r22_balance_statement) {
			this.r22_balance_statement = r22_balance_statement;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_sch_no() {
			return r23_sch_no;
		}

		public void setR23_sch_no(BigDecimal r23_sch_no) {
			this.r23_sch_no = r23_sch_no;
		}

		public BigDecimal getR23_net_amount() {
			return r23_net_amount;
		}

		public void setR23_net_amount(BigDecimal r23_net_amount) {
			this.r23_net_amount = r23_net_amount;
		}

		public BigDecimal getR23_balance_bank() {
			return r23_balance_bank;
		}

		public void setR23_balance_bank(BigDecimal r23_balance_bank) {
			this.r23_balance_bank = r23_balance_bank;
		}

		public BigDecimal getR23_balance_statement() {
			return r23_balance_statement;
		}

		public void setR23_balance_statement(BigDecimal r23_balance_statement) {
			this.r23_balance_statement = r23_balance_statement;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_sch_no() {
			return r24_sch_no;
		}

		public void setR24_sch_no(BigDecimal r24_sch_no) {
			this.r24_sch_no = r24_sch_no;
		}

		public BigDecimal getR24_net_amount() {
			return r24_net_amount;
		}

		public void setR24_net_amount(BigDecimal r24_net_amount) {
			this.r24_net_amount = r24_net_amount;
		}

		public BigDecimal getR24_balance_bank() {
			return r24_balance_bank;
		}

		public void setR24_balance_bank(BigDecimal r24_balance_bank) {
			this.r24_balance_bank = r24_balance_bank;
		}

		public BigDecimal getR24_balance_statement() {
			return r24_balance_statement;
		}

		public void setR24_balance_statement(BigDecimal r24_balance_statement) {
			this.r24_balance_statement = r24_balance_statement;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_sch_no() {
			return r25_sch_no;
		}

		public void setR25_sch_no(BigDecimal r25_sch_no) {
			this.r25_sch_no = r25_sch_no;
		}

		public BigDecimal getR25_net_amount() {
			return r25_net_amount;
		}

		public void setR25_net_amount(BigDecimal r25_net_amount) {
			this.r25_net_amount = r25_net_amount;
		}

		public BigDecimal getR25_balance_bank() {
			return r25_balance_bank;
		}

		public void setR25_balance_bank(BigDecimal r25_balance_bank) {
			this.r25_balance_bank = r25_balance_bank;
		}

		public BigDecimal getR25_balance_statement() {
			return r25_balance_statement;
		}

		public void setR25_balance_statement(BigDecimal r25_balance_statement) {
			this.r25_balance_statement = r25_balance_statement;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_sch_no() {
			return r26_sch_no;
		}

		public void setR26_sch_no(BigDecimal r26_sch_no) {
			this.r26_sch_no = r26_sch_no;
		}

		public BigDecimal getR26_net_amount() {
			return r26_net_amount;
		}

		public void setR26_net_amount(BigDecimal r26_net_amount) {
			this.r26_net_amount = r26_net_amount;
		}

		public BigDecimal getR26_balance_bank() {
			return r26_balance_bank;
		}

		public void setR26_balance_bank(BigDecimal r26_balance_bank) {
			this.r26_balance_bank = r26_balance_bank;
		}

		public BigDecimal getR26_balance_statement() {
			return r26_balance_statement;
		}

		public void setR26_balance_statement(BigDecimal r26_balance_statement) {
			this.r26_balance_statement = r26_balance_statement;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_sch_no() {
			return r27_sch_no;
		}

		public void setR27_sch_no(BigDecimal r27_sch_no) {
			this.r27_sch_no = r27_sch_no;
		}

		public BigDecimal getR27_net_amount() {
			return r27_net_amount;
		}

		public void setR27_net_amount(BigDecimal r27_net_amount) {
			this.r27_net_amount = r27_net_amount;
		}

		public BigDecimal getR27_balance_bank() {
			return r27_balance_bank;
		}

		public void setR27_balance_bank(BigDecimal r27_balance_bank) {
			this.r27_balance_bank = r27_balance_bank;
		}

		public BigDecimal getR27_balance_statement() {
			return r27_balance_statement;
		}

		public void setR27_balance_statement(BigDecimal r27_balance_statement) {
			this.r27_balance_statement = r27_balance_statement;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_sch_no() {
			return r28_sch_no;
		}

		public void setR28_sch_no(BigDecimal r28_sch_no) {
			this.r28_sch_no = r28_sch_no;
		}

		public BigDecimal getR28_net_amount() {
			return r28_net_amount;
		}

		public void setR28_net_amount(BigDecimal r28_net_amount) {
			this.r28_net_amount = r28_net_amount;
		}

		public BigDecimal getR28_balance_bank() {
			return r28_balance_bank;
		}

		public void setR28_balance_bank(BigDecimal r28_balance_bank) {
			this.r28_balance_bank = r28_balance_bank;
		}

		public BigDecimal getR28_balance_statement() {
			return r28_balance_statement;
		}

		public void setR28_balance_statement(BigDecimal r28_balance_statement) {
			this.r28_balance_statement = r28_balance_statement;
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

	// =====================================================
	// ARCHIVAL DETAIL ENTITY
	// =====================================================

	public class FORMAT_IArchivalDetailRowMapper implements RowMapper<FORMAT_I_Archival_Detail_Entity> {

		@Override
		public FORMAT_I_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_I_Archival_Detail_Entity obj = new FORMAT_I_Archival_Detail_Entity();

			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getDate("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getDate("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getDate("VERIFY_TIME"));

			obj.setEntity_flg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			return obj;
		}
	}

	public class FORMAT_I_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String cust_id;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acct_number;

		@Column(name = "ACCT_NAME")
		private String acct_name;

		@Column(name = "DATA_TYPE")
		private String data_type;

		@Column(name = "REPORT_LABEL")
		private String report_label;

		@Column(name = "REPORT_REMARKS")
		private String report_remarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modification_remarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String data_entry_version;

		@Column(name = "ACCT_BALANCE_IN_PULA")
		private BigDecimal acct_balance_in_pula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;

		@Column(name = "REPORT_RESUBDATE")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_resubdate;

		@Column(name = "REPORT_NAME")
		private String report_name;

		@Column(name = "CREATE_USER")
		private String create_user;

		@Column(name = "CREATE_TIME")
		private Date create_time;

		@Column(name = "MODIFY_USER")
		private String modify_user;

		@Column(name = "MODIFY_TIME")
		private Date modify_time;

		@Column(name = "VERIFY_USER")
		private String verify_user;

		@Column(name = "VERIFY_TIME")
		private Date verify_time;

		@Column(name = "ENTITY_FLG")
		private Character entity_flg;

		@Column(name = "MODIFY_FLG")
		private Character modify_flg;

		@Column(name = "DEL_FLG")
		private Character del_flg;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String report_addl_criteria_1;

		public String getCust_id() {
			return cust_id;
		}

		public void setCust_id(String cust_id) {
			this.cust_id = cust_id;
		}

		public String getAcct_number() {
			return acct_number;
		}

		public void setAcct_number(String acct_number) {
			this.acct_number = acct_number;
		}

		public String getAcct_name() {
			return acct_name;
		}

		public void setAcct_name(String acct_name) {
			this.acct_name = acct_name;
		}

		public String getData_type() {
			return data_type;
		}

		public void setData_type(String data_type) {
			this.data_type = data_type;
		}

		public String getReport_label() {
			return report_label;
		}

		public void setReport_label(String report_label) {
			this.report_label = report_label;
		}

		public String getReport_remarks() {
			return report_remarks;
		}

		public void setReport_remarks(String report_remarks) {
			this.report_remarks = report_remarks;
		}

		public String getModification_remarks() {
			return modification_remarks;
		}

		public void setModification_remarks(String modification_remarks) {
			this.modification_remarks = modification_remarks;
		}

		public String getData_entry_version() {
			return data_entry_version;
		}

		public void setData_entry_version(String data_entry_version) {
			this.data_entry_version = data_entry_version;
		}

		public BigDecimal getAcct_balance_in_pula() {
			return acct_balance_in_pula;
		}

		public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
			this.acct_balance_in_pula = acct_balance_in_pula;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
		}

		public String getReport_name() {
			return report_name;
		}

		public void setReport_name(String report_name) {
			this.report_name = report_name;
		}

		public String getCreate_user() {
			return create_user;
		}

		public void setCreate_user(String create_user) {
			this.create_user = create_user;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}

		public String getModify_user() {
			return modify_user;
		}

		public void setModify_user(String modify_user) {
			this.modify_user = modify_user;
		}

		public Date getModify_time() {
			return modify_time;
		}

		public void setModify_time(Date modify_time) {
			this.modify_time = modify_time;
		}

		public String getVerify_user() {
			return verify_user;
		}

		public void setVerify_user(String verify_user) {
			this.verify_user = verify_user;
		}

		public Date getVerify_time() {
			return verify_time;
		}

		public void setVerify_time(Date verify_time) {
			this.verify_time = verify_time;
		}

		public Character getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(Character entity_flg) {
			this.entity_flg = entity_flg;
		}

		public Character getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(Character modify_flg) {
			this.modify_flg = modify_flg;
		}

		public Character getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(Character del_flg) {
			this.del_flg = del_flg;
		}

		public String getReport_addl_criteria_1() {
			return report_addl_criteria_1;
		}

		public void setReport_addl_criteria_1(String report_addl_criteria_1) {
			this.report_addl_criteria_1 = report_addl_criteria_1;
		}

	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getFORMAT_IView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {

			System.out.println("ARCHIVAL MODE");
			System.out.println("version = " + version);

			List<FORMAT_I_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival1(dt, version);

				System.out.println("T1Master size = " + T1Master.size());

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);

		} else {

			List<FORMAT_I_Summary_Entity> T1Master = new ArrayList<FORMAT_I_Summary_Entity>();
			try {
				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate1(dt);
				System.out.println("T1Master size " + T1Master.size());
				mv.addObject("report_date", dateformat.format(dt));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		}

		mv.setViewName("BRRS/FORMAT_I");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getFORMAT_IcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		// Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String report_label = null;
			String report_addl_criteria_1 = null;

			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					report_label = parts[0];
					report_addl_criteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<FORMAT_I_Archival_Detail_Entity> T1Dt1;
				if (report_label != null && report_addl_criteria_1 != null) {
					T1Dt1 = GetArchivalDataByRowIdAndColumnId(report_label, report_addl_criteria_1, parsedDate,
							version);

				} else {
					T1Dt1 = getArchivalDetaildatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<FORMAT_I_Detail_Entity> T1Dt1;
				if (report_label != null && report_addl_criteria_1 != null) {
					T1Dt1 = GetDetailDataByRowIdAndColumnId1(report_label, report_addl_criteria_1, parsedDate);
				} else {
					T1Dt1 = getDetaildatabydateList1(parsedDate);

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

		mv.setViewName("BRRS/FORMAT_I");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getFORMAT_IExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelFORMAT_IARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<FORMAT_I_Summary_Entity> dataList = getDataByDate1(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  FORMAT_I report. Returning empty result.");
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
					FORMAT_I_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ROW 12
					// Column C
					Cell cellE = row.createCell(2);
					if (record.getR12_net_amount() != null) {
						cellE.setCellValue(record.getR12_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					Cell cellF = row.createCell(3);
					if (record.getR12_balance_bank() != null) {
						cellF.setCellValue(record.getR12_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column E
					Cell cellG = row.createCell(2);
					if (record.getR12_balance_statement() != null) {
						cellG.setCellValue(record.getR12_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 13 ===================== */
					// Column C
					row = sheet.getRow(12);
					cellE = row.createCell(2);
					if (record.getR13_net_amount() != null) {
						cellE.setCellValue(record.getR13_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR13_balance_bank() != null) {
						cellF.setCellValue(record.getR13_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(2);
					if (record.getR13_balance_statement() != null) {
						cellG.setCellValue(record.getR13_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					/* ===================== ROW 14 ===================== */
					// Column C
					row = sheet.getRow(13);
					cellE = row.createCell(2);
					if (record.getR14_net_amount() != null) {
						cellE.setCellValue(record.getR14_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR14_balance_bank() != null) {
						cellF.setCellValue(record.getR14_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR14_balance_statement() != null) {
						cellG.setCellValue(record.getR14_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 15 ===================== */
					// Column C
					row = sheet.getRow(14);
					cellE = row.createCell(2);
					if (record.getR15_net_amount() != null) {
						cellE.setCellValue(record.getR15_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR15_balance_bank() != null) {
						cellF.setCellValue(record.getR15_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR15_balance_statement() != null) {
						cellG.setCellValue(record.getR15_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 16 ===================== */
					// Column C
					row = sheet.getRow(15);
					cellE = row.createCell(2);
					if (record.getR16_net_amount() != null) {
						cellE.setCellValue(record.getR16_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR16_balance_bank() != null) {
						cellF.setCellValue(record.getR16_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR16_balance_statement() != null) {
						cellG.setCellValue(record.getR16_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 19 ===================== */
					// Column C
					row = sheet.getRow(18);
					cellE = row.createCell(2);
					if (record.getR19_net_amount() != null) {
						cellE.setCellValue(record.getR19_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR19_balance_bank() != null) {
						cellF.setCellValue(record.getR19_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR19_balance_statement() != null) {
						cellG.setCellValue(record.getR19_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 20 ===================== */
					// Column C
					row = sheet.getRow(19);
					cellE = row.createCell(2);
					if (record.getR20_net_amount() != null) {
						cellE.setCellValue(record.getR20_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR20_balance_bank() != null) {
						cellF.setCellValue(record.getR20_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR20_balance_statement() != null) {
						cellG.setCellValue(record.getR20_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 21 ===================== */
					// Column C
					row = sheet.getRow(20);
					cellE = row.createCell(2);
					if (record.getR21_net_amount() != null) {
						cellE.setCellValue(record.getR21_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR21_balance_bank() != null) {
						cellF.setCellValue(record.getR21_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR21_balance_statement() != null) {
						cellG.setCellValue(record.getR21_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 22 ===================== */
					// Column C
					row = sheet.getRow(21);
					cellE = row.createCell(2);
					if (record.getR22_net_amount() != null) {
						cellE.setCellValue(record.getR22_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR22_balance_bank() != null) {
						cellF.setCellValue(record.getR22_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR22_balance_statement() != null) {
						cellG.setCellValue(record.getR22_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 23 ===================== */
					// Column C
					row = sheet.getRow(22);
					cellE = row.createCell(2);
					if (record.getR23_net_amount() != null) {
						cellE.setCellValue(record.getR23_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR23_balance_bank() != null) {
						cellF.setCellValue(record.getR23_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR23_balance_statement() != null) {
						cellG.setCellValue(record.getR23_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 24 ===================== */
					// Column C
					row = sheet.getRow(23);
					cellE = row.createCell(2);
					if (record.getR24_net_amount() != null) {
						cellE.setCellValue(record.getR24_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR24_balance_bank() != null) {
						cellF.setCellValue(record.getR24_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR24_balance_statement() != null) {
						cellG.setCellValue(record.getR24_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 25 ===================== */
					// Column C
					row = sheet.getRow(24);
					cellE = row.createCell(2);
					if (record.getR25_net_amount() != null) {
						cellE.setCellValue(record.getR25_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR25_balance_bank() != null) {
						cellF.setCellValue(record.getR25_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR25_balance_statement() != null) {
						cellG.setCellValue(record.getR25_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 27 ===================== */
					// Column C
					row = sheet.getRow(26);
					cellE = row.createCell(2);
					if (record.getR27_net_amount() != null) {
						cellE.setCellValue(record.getR27_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR27_balance_bank() != null) {
						cellF.setCellValue(record.getR27_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR27_balance_statement() != null) {
						cellG.setCellValue(record.getR27_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 28 ===================== */
					// Column C
					row = sheet.getRow(27);
					cellE = row.createCell(2);
					if (record.getR28_net_amount() != null) {
						cellE.setCellValue(record.getR28_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR28_balance_bank() != null) {
						cellF.setCellValue(record.getR28_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR28_balance_statement() != null) {
						cellG.setCellValue(record.getR28_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

				}

			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] getExcelFORMAT_IARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<FORMAT_I_Archival_Summary_Entity> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FORMAT_I report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					FORMAT_I_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ROW 12
					// Column C
					Cell cellE = row.createCell(2);
					if (record.getR12_net_amount() != null) {
						cellE.setCellValue(record.getR12_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					Cell cellF = row.createCell(3);
					if (record.getR12_balance_bank() != null) {
						cellF.setCellValue(record.getR12_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column E
					Cell cellG = row.createCell(2);
					if (record.getR12_balance_statement() != null) {
						cellG.setCellValue(record.getR12_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 13 ===================== */
					// Column C
					row = sheet.getRow(12);
					cellE = row.createCell(2);
					if (record.getR13_net_amount() != null) {
						cellE.setCellValue(record.getR13_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR13_balance_bank() != null) {
						cellF.setCellValue(record.getR13_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR13_balance_statement() != null) {
						cellG.setCellValue(record.getR13_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					/* ===================== ROW 14 ===================== */
					// Column C
					row = sheet.getRow(13);
					cellE = row.createCell(2);
					if (record.getR14_net_amount() != null) {
						cellE.setCellValue(record.getR14_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR14_balance_bank() != null) {
						cellF.setCellValue(record.getR14_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR14_balance_statement() != null) {
						cellG.setCellValue(record.getR14_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 15 ===================== */
					// Column C
					row = sheet.getRow(14);
					cellE = row.createCell(2);
					if (record.getR15_net_amount() != null) {
						cellE.setCellValue(record.getR15_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR15_balance_bank() != null) {
						cellF.setCellValue(record.getR15_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR15_balance_statement() != null) {
						cellG.setCellValue(record.getR15_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 16 ===================== */
					// Column C
					row = sheet.getRow(15);
					cellE = row.createCell(2);
					if (record.getR16_net_amount() != null) {
						cellE.setCellValue(record.getR16_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR16_balance_bank() != null) {
						cellF.setCellValue(record.getR16_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR16_balance_statement() != null) {
						cellG.setCellValue(record.getR16_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 19 ===================== */
					// Column C
					row = sheet.getRow(18);
					cellE = row.createCell(2);
					if (record.getR19_net_amount() != null) {
						cellE.setCellValue(record.getR19_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR19_balance_bank() != null) {
						cellF.setCellValue(record.getR19_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR19_balance_statement() != null) {
						cellG.setCellValue(record.getR19_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 20 ===================== */
					// Column C
					row = sheet.getRow(19);
					cellE = row.createCell(2);
					if (record.getR20_net_amount() != null) {
						cellE.setCellValue(record.getR20_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR20_balance_bank() != null) {
						cellF.setCellValue(record.getR20_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR20_balance_statement() != null) {
						cellG.setCellValue(record.getR20_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 21 ===================== */
					// Column C
					row = sheet.getRow(20);
					cellE = row.createCell(2);
					if (record.getR21_net_amount() != null) {
						cellE.setCellValue(record.getR21_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR21_balance_bank() != null) {
						cellF.setCellValue(record.getR21_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR21_balance_statement() != null) {
						cellG.setCellValue(record.getR21_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 22 ===================== */
					// Column C
					row = sheet.getRow(21);
					cellE = row.createCell(2);
					if (record.getR22_net_amount() != null) {
						cellE.setCellValue(record.getR22_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR22_balance_bank() != null) {
						cellF.setCellValue(record.getR22_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR22_balance_statement() != null) {
						cellG.setCellValue(record.getR22_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 23 ===================== */
					// Column C
					row = sheet.getRow(22);
					cellE = row.createCell(2);
					if (record.getR23_net_amount() != null) {
						cellE.setCellValue(record.getR23_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR23_balance_bank() != null) {
						cellF.setCellValue(record.getR23_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR23_balance_statement() != null) {
						cellG.setCellValue(record.getR23_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 24 ===================== */
					// Column C
					row = sheet.getRow(23);
					cellE = row.createCell(2);
					if (record.getR24_net_amount() != null) {
						cellE.setCellValue(record.getR24_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR24_balance_bank() != null) {
						cellF.setCellValue(record.getR24_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR24_balance_statement() != null) {
						cellG.setCellValue(record.getR24_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 25 ===================== */
					// Column C
					row = sheet.getRow(24);
					cellE = row.createCell(2);
					if (record.getR25_net_amount() != null) {
						cellE.setCellValue(record.getR25_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR25_balance_bank() != null) {
						cellF.setCellValue(record.getR25_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR25_balance_statement() != null) {
						cellG.setCellValue(record.getR25_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 27 ===================== */
					// Column C
					row = sheet.getRow(26);
					cellE = row.createCell(2);
					if (record.getR27_net_amount() != null) {
						cellE.setCellValue(record.getR27_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR27_balance_bank() != null) {
						cellF.setCellValue(record.getR27_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR27_balance_statement() != null) {
						cellG.setCellValue(record.getR27_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/* ===================== ROW 28 ===================== */
					// Column C
					row = sheet.getRow(27);
					cellE = row.createCell(2);
					if (record.getR28_net_amount() != null) {
						cellE.setCellValue(record.getR28_net_amount().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column D
					cellF = row.createCell(3);
					if (record.getR28_balance_bank() != null) {
						cellF.setCellValue(record.getR28_balance_bank().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column D
					cellG = row.createCell(4);
					if (record.getR28_balance_statement() != null) {
						cellG.setCellValue(record.getR28_balance_statement().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

				}

			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public List<Object> getFORMAT_IArchival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_FORMAT_I_ARCHIVALTABLE_SUMMARY"
				+ "ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public byte[] getFORMAT_IDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  FORMAT_I Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getFORMAT_IDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_I Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<FORMAT_I_Detail_Entity> reportData = getDetaildatabydateList1(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_I_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FORMAT_I — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_I Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFORMAT_IDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FORMAT_I ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_I Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<FORMAT_I_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_I_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FORMAT_I — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_I Excel", e);
			return new byte[0];
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_I");

		System.out.println("Came to view method");

		if (acctNo != null) {
			FORMAT_I_Detail_Entity Entity = findByAcctnumber1(acctNo);
			if (Entity != null && Entity.getReport_date() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(Entity.getReport_date());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", Entity);
		}

		else {
			System.out.println(acctNo);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String acctNo = request.getParameter("acct_number");
			String acctName = request.getParameter("acct_name");
			String acctBalanceInPula = request.getParameter("acct_balance_in_pula");
			String reportDateStr = request.getParameter("report_date");

			logger.info("Received update for ACCT_NUMBER: {}", acctNo);

			FORMAT_I_Detail_Entity existing = findByAcctnumber1(acctNo);

			if (existing == null) {
				logger.warn("No record found for ACCT_NUMBER: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update Account Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcct_name() == null || !existing.getAcct_name().equals(acctName)) {

					existing.setAcct_name(acctName);
					isChanged = true;

					logger.info("Account Name updated to {}", acctName);
				}
			}

			// Update Account Balance
			if (acctBalanceInPula != null && !acctBalanceInPula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInPula);

				if (existing.getAcct_balance_in_pula() == null
						|| existing.getAcct_balance_in_pula().compareTo(newBalance) != 0) {

					existing.setAcct_balance_in_pula(newBalance);
					isChanged = true;

					logger.info("Account Balance updated to {}", newBalance);
				}
			}

			if (isChanged) {

				String sql = "UPDATE BRRS_FORMAT_I_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ? "
						+ "WHERE ACCT_NUMBER = ?";

				jdbcTemplate.update(sql, existing.getAcct_name(), existing.getAcct_balance_in_pula(),
						existing.getAcct_number());

				// Format date for procedure
				String formattedDate = null;

				if (reportDateStr != null && !reportDateStr.isEmpty()) {

					formattedDate = new SimpleDateFormat("dd-MM-yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				} else if (existing.getReport_date() != null) {

					formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(existing.getReport_date());
				}

				final String procDate = formattedDate;

				if (procDate != null) {

					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

						@Override
						public void afterCommit() {

							try {

								logger.info("Transaction committed — calling BRRS_FORMAT_I_SUMMARY_PROCEDURE({})",
										procDate);

								jdbcTemplate.update("BEGIN BRRS_FORMAT_I_SUMMARY_PROCEDURE(?); END;", procDate);

								logger.info("Procedure executed successfully after commit.");

							} catch (Exception e) {

								logger.error("Error executing procedure after commit", e);
							}
						}
					});
				}

				return ResponseEntity.ok("Record updated successfully!");

			} else {

				logger.info("No changes detected for ACCT_NUMBER: {}", acctNo);

				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {

			logger.error("Error updating FORMAT_I record", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

}