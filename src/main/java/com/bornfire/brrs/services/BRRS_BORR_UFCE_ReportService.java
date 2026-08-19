package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class BRRS_BORR_UFCE_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_BORR_UFCE_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

	// ===========================================================
	// INNER ENTITY CLASSES
	// ===========================================================

	// ------------------------------
	// BORR_UFCE SUMMARY ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_SUMMARYTABLE")
	public static class BORR_UFCE_Summary_Entity {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String CUST_ID;
		private BigDecimal ACCT_NO;
		private String ACCT_NAME;
		private String SCHM_CODE;
		private String SCHM_DESC;
		private Date ACCT_OPN_DATE;
		private String CCY;
		private BigDecimal BAL_EQUI_TO_BWP;
		private BigDecimal SANCTION_AMT_BWP;
		private BigDecimal INT_RATE;
		private BigDecimal AMT_IN_INR;
		private BigDecimal VALUE_1;
		private BigDecimal VALUE_2;

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
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

		public String getCUST_ID() {
			return CUST_ID;
		}

		public void setCUST_ID(String cUST_ID) {
			CUST_ID = cUST_ID;
		}

		public BigDecimal getACCT_NO() {
			return ACCT_NO;
		}

		public void setACCT_NO(BigDecimal aCCT_NO) {
			ACCT_NO = aCCT_NO;
		}

		public String getACCT_NAME() {
			return ACCT_NAME;
		}

		public void setACCT_NAME(String aCCT_NAME) {
			ACCT_NAME = aCCT_NAME;
		}

		public String getSCHM_CODE() {
			return SCHM_CODE;
		}

		public void setSCHM_CODE(String sCHM_CODE) {
			SCHM_CODE = sCHM_CODE;
		}

		public String getSCHM_DESC() {
			return SCHM_DESC;
		}

		public void setSCHM_DESC(String sCHM_DESC) {
			SCHM_DESC = sCHM_DESC;
		}

		public Date getACCT_OPN_DATE() {
			return ACCT_OPN_DATE;
		}

		public void setACCT_OPN_DATE(Date aCCT_OPN_DATE) {
			ACCT_OPN_DATE = aCCT_OPN_DATE;
		}

		public String getCCY() {
			return CCY;
		}

		public void setCCY(String cCY) {
			CCY = cCY;
		}

		public BigDecimal getBAL_EQUI_TO_BWP() {
			return BAL_EQUI_TO_BWP;
		}

		public void setBAL_EQUI_TO_BWP(BigDecimal bAL_EQUI_TO_BWP) {
			BAL_EQUI_TO_BWP = bAL_EQUI_TO_BWP;
		}

		public BigDecimal getSANCTION_AMT_BWP() {
			return SANCTION_AMT_BWP;
		}

		public void setSANCTION_AMT_BWP(BigDecimal sANCTION_AMT_BWP) {
			SANCTION_AMT_BWP = sANCTION_AMT_BWP;
		}

		public BigDecimal getINT_RATE() {
			return INT_RATE;
		}

		public void setINT_RATE(BigDecimal iNT_RATE) {
			INT_RATE = iNT_RATE;
		}

		public BigDecimal getAMT_IN_INR() {
			return AMT_IN_INR;
		}

		public void setAMT_IN_INR(BigDecimal aMT_IN_INR) {
			AMT_IN_INR = aMT_IN_INR;
		}

		public BigDecimal getVALUE_1() {
			return VALUE_1;
		}

		public void setVALUE_1(BigDecimal vALUE_1) {
			VALUE_1 = vALUE_1;
		}

		public BigDecimal getVALUE_2() {
			return VALUE_2;
		}

		public void setVALUE_2(BigDecimal vALUE_2) {
			VALUE_2 = vALUE_2;
		}

		public BORR_UFCE_Summary_Entity() {
			super();
		}
	}

	// ------------------------------
	// BORR_UFCE DETAIL ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_DETAILTABLE")
	public static class BORR_UFCE_Detail_Entity {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String CUST_ID;
		private BigDecimal ACCT_NO;
		private String ACCT_NAME;
		private String SCHM_CODE;
		private String SCHM_DESC;
		private Date ACCT_OPN_DATE;
		private String CCY;
		private BigDecimal BAL_EQUI_TO_BWP;
		private BigDecimal SANCTION_AMT_BWP;
		private BigDecimal INT_RATE;
		private BigDecimal AMT_IN_INR;
		private BigDecimal VALUE_1;
		private BigDecimal VALUE_2;

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
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

		public String getCUST_ID() {
			return CUST_ID;
		}

		public void setCUST_ID(String cUST_ID) {
			CUST_ID = cUST_ID;
		}

		public BigDecimal getACCT_NO() {
			return ACCT_NO;
		}

		public void setACCT_NO(BigDecimal aCCT_NO) {
			ACCT_NO = aCCT_NO;
		}

		public String getACCT_NAME() {
			return ACCT_NAME;
		}

		public void setACCT_NAME(String aCCT_NAME) {
			ACCT_NAME = aCCT_NAME;
		}

		public String getSCHM_CODE() {
			return SCHM_CODE;
		}

		public void setSCHM_CODE(String sCHM_CODE) {
			SCHM_CODE = sCHM_CODE;
		}

		public String getSCHM_DESC() {
			return SCHM_DESC;
		}

		public void setSCHM_DESC(String sCHM_DESC) {
			SCHM_DESC = sCHM_DESC;
		}

		public Date getACCT_OPN_DATE() {
			return ACCT_OPN_DATE;
		}

		public void setACCT_OPN_DATE(Date aCCT_OPN_DATE) {
			ACCT_OPN_DATE = aCCT_OPN_DATE;
		}

		public String getCCY() {
			return CCY;
		}

		public void setCCY(String cCY) {
			CCY = cCY;
		}

		public BigDecimal getBAL_EQUI_TO_BWP() {
			return BAL_EQUI_TO_BWP;
		}

		public void setBAL_EQUI_TO_BWP(BigDecimal bAL_EQUI_TO_BWP) {
			BAL_EQUI_TO_BWP = bAL_EQUI_TO_BWP;
		}

		public BigDecimal getSANCTION_AMT_BWP() {
			return SANCTION_AMT_BWP;
		}

		public void setSANCTION_AMT_BWP(BigDecimal sANCTION_AMT_BWP) {
			SANCTION_AMT_BWP = sANCTION_AMT_BWP;
		}

		public BigDecimal getINT_RATE() {
			return INT_RATE;
		}

		public void setINT_RATE(BigDecimal iNT_RATE) {
			INT_RATE = iNT_RATE;
		}

		public BigDecimal getAMT_IN_INR() {
			return AMT_IN_INR;
		}

		public void setAMT_IN_INR(BigDecimal aMT_IN_INR) {
			AMT_IN_INR = aMT_IN_INR;
		}

		public BigDecimal getVALUE_1() {
			return VALUE_1;
		}

		public void setVALUE_1(BigDecimal vALUE_1) {
			VALUE_1 = vALUE_1;
		}

		public BigDecimal getVALUE_2() {
			return VALUE_2;
		}

		public void setVALUE_2(BigDecimal vALUE_2) {
			VALUE_2 = vALUE_2;
		}

		public BORR_UFCE_Detail_Entity() {
			super();
		}
	}

	// ------------------------------
	// BORR_UFCE ARCHIVAL SUMMARY ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE")
	public static class BORR_UFCE_Archival_Summary_Entity {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private Date resubreport_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String CUST_ID;
		private BigDecimal ACCT_NO;
		private String ACCT_NAME;
		private String SCHM_CODE;
		private String SCHM_DESC;
		private Date ACCT_OPN_DATE;
		private String CCY;
		private BigDecimal BAL_EQUI_TO_BWP;
		private BigDecimal SANCTION_AMT_BWP;
		private BigDecimal INT_RATE;
		private BigDecimal AMT_IN_INR;
		private BigDecimal VALUE_1;
		private BigDecimal VALUE_2;

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public Date getResubreport_date() {
			return resubreport_date;
		}

		public void setResubreport_date(Date resubreport_date) {
			this.resubreport_date = resubreport_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
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

		public String getCUST_ID() {
			return CUST_ID;
		}

		public void setCUST_ID(String cUST_ID) {
			CUST_ID = cUST_ID;
		}

		public BigDecimal getACCT_NO() {
			return ACCT_NO;
		}

		public void setACCT_NO(BigDecimal aCCT_NO) {
			ACCT_NO = aCCT_NO;
		}

		public String getACCT_NAME() {
			return ACCT_NAME;
		}

		public void setACCT_NAME(String aCCT_NAME) {
			ACCT_NAME = aCCT_NAME;
		}

		public String getSCHM_CODE() {
			return SCHM_CODE;
		}

		public void setSCHM_CODE(String sCHM_CODE) {
			SCHM_CODE = sCHM_CODE;
		}

		public String getSCHM_DESC() {
			return SCHM_DESC;
		}

		public void setSCHM_DESC(String sCHM_DESC) {
			SCHM_DESC = sCHM_DESC;
		}

		public Date getACCT_OPN_DATE() {
			return ACCT_OPN_DATE;
		}

		public void setACCT_OPN_DATE(Date aCCT_OPN_DATE) {
			ACCT_OPN_DATE = aCCT_OPN_DATE;
		}

		public String getCCY() {
			return CCY;
		}

		public void setCCY(String cCY) {
			CCY = cCY;
		}

		public BigDecimal getBAL_EQUI_TO_BWP() {
			return BAL_EQUI_TO_BWP;
		}

		public void setBAL_EQUI_TO_BWP(BigDecimal bAL_EQUI_TO_BWP) {
			BAL_EQUI_TO_BWP = bAL_EQUI_TO_BWP;
		}

		public BigDecimal getSANCTION_AMT_BWP() {
			return SANCTION_AMT_BWP;
		}

		public void setSANCTION_AMT_BWP(BigDecimal sANCTION_AMT_BWP) {
			SANCTION_AMT_BWP = sANCTION_AMT_BWP;
		}

		public BigDecimal getINT_RATE() {
			return INT_RATE;
		}

		public void setINT_RATE(BigDecimal iNT_RATE) {
			INT_RATE = iNT_RATE;
		}

		public BigDecimal getAMT_IN_INR() {
			return AMT_IN_INR;
		}

		public void setAMT_IN_INR(BigDecimal aMT_IN_INR) {
			AMT_IN_INR = aMT_IN_INR;
		}

		public BigDecimal getVALUE_1() {
			return VALUE_1;
		}

		public void setVALUE_1(BigDecimal vALUE_1) {
			VALUE_1 = vALUE_1;
		}

		public BigDecimal getVALUE_2() {
			return VALUE_2;
		}

		public void setVALUE_2(BigDecimal vALUE_2) {
			VALUE_2 = vALUE_2;
		}

		public BORR_UFCE_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	// ------------------------------
	// BORR_UFCE ARCHIVAL DETAIL ENTITY CLASS
	// ------------------------------
	@Entity
	@Table(name = "BRRS_BORR_UFCE_ARCHIVAL_DETAILTABLE")
	public static class BORR_UFCE_Archival_Detail_Entity {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private Date resubreport_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String CUST_ID;
		private BigDecimal ACCT_NO;
		private String ACCT_NAME;
		private String SCHM_CODE;
		private String SCHM_DESC;
		private Date ACCT_OPN_DATE;
		private String CCY;
		private BigDecimal BAL_EQUI_TO_BWP;
		private BigDecimal SANCTION_AMT_BWP;
		private BigDecimal INT_RATE;
		private BigDecimal AMT_IN_INR;
		private BigDecimal VALUE_1;
		private BigDecimal VALUE_2;

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public Date getResubreport_date() {
			return resubreport_date;
		}

		public void setResubreport_date(Date resubreport_date) {
			this.resubreport_date = resubreport_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
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

		public String getCUST_ID() {
			return CUST_ID;
		}

		public void setCUST_ID(String cUST_ID) {
			CUST_ID = cUST_ID;
		}

		public BigDecimal getACCT_NO() {
			return ACCT_NO;
		}

		public void setACCT_NO(BigDecimal aCCT_NO) {
			ACCT_NO = aCCT_NO;
		}

		public String getACCT_NAME() {
			return ACCT_NAME;
		}

		public void setACCT_NAME(String aCCT_NAME) {
			ACCT_NAME = aCCT_NAME;
		}

		public String getSCHM_CODE() {
			return SCHM_CODE;
		}

		public void setSCHM_CODE(String sCHM_CODE) {
			SCHM_CODE = sCHM_CODE;
		}

		public String getSCHM_DESC() {
			return SCHM_DESC;
		}

		public void setSCHM_DESC(String sCHM_DESC) {
			SCHM_DESC = sCHM_DESC;
		}

		public Date getACCT_OPN_DATE() {
			return ACCT_OPN_DATE;
		}

		public void setACCT_OPN_DATE(Date aCCT_OPN_DATE) {
			ACCT_OPN_DATE = aCCT_OPN_DATE;
		}

		public String getCCY() {
			return CCY;
		}

		public void setCCY(String cCY) {
			CCY = cCY;
		}

		public BigDecimal getBAL_EQUI_TO_BWP() {
			return BAL_EQUI_TO_BWP;
		}

		public void setBAL_EQUI_TO_BWP(BigDecimal bAL_EQUI_TO_BWP) {
			BAL_EQUI_TO_BWP = bAL_EQUI_TO_BWP;
		}

		public BigDecimal getSANCTION_AMT_BWP() {
			return SANCTION_AMT_BWP;
		}

		public void setSANCTION_AMT_BWP(BigDecimal sANCTION_AMT_BWP) {
			SANCTION_AMT_BWP = sANCTION_AMT_BWP;
		}

		public BigDecimal getINT_RATE() {
			return INT_RATE;
		}

		public void setINT_RATE(BigDecimal iNT_RATE) {
			INT_RATE = iNT_RATE;
		}

		public BigDecimal getAMT_IN_INR() {
			return AMT_IN_INR;
		}

		public void setAMT_IN_INR(BigDecimal aMT_IN_INR) {
			AMT_IN_INR = aMT_IN_INR;
		}

		public BigDecimal getVALUE_1() {
			return VALUE_1;
		}

		public void setVALUE_1(BigDecimal vALUE_1) {
			VALUE_1 = vALUE_1;
		}

		public BigDecimal getVALUE_2() {
			return VALUE_2;
		}

		public void setVALUE_2(BigDecimal vALUE_2) {
			VALUE_2 = vALUE_2;
		}

		public BORR_UFCE_Archival_Detail_Entity() {
			super();

		}
	}

	// ===========================================================
	// ROW MAPPER CLASSES
	// ===========================================================

	// Normal Summary Row Mapper
	private class BORR_UFCE_SummaryRowMapper implements RowMapper<BORR_UFCE_Summary_Entity> {

		@Override
		public BORR_UFCE_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			BORR_UFCE_Summary_Entity entity = new BORR_UFCE_Summary_Entity();

			// ===========================================================
			// REPORT DETAILS
			// ===========================================================

			entity.setReport_date(rs.getDate("REPORT_DATE"));

			entity.setReport_version(rs.getString("REPORT_VERSION"));

			entity.setReport_frequency(rs.getString("REPORT_FREQUENCY"));

			entity.setReport_code(rs.getString("REPORT_CODE"));

			entity.setReport_desc(rs.getString("REPORT_DESC"));

			entity.setEntity_flg(rs.getString("ENTITY_FLG"));

			entity.setModify_flg(rs.getString("MODIFY_FLG"));

			entity.setDel_flg(rs.getString("DEL_FLG"));

			// ===========================================================
			// CUSTOMER / ACCOUNT DETAILS
			// ===========================================================

			entity.setCUST_ID(rs.getString("CUST_ID"));

			entity.setACCT_NO(rs.getBigDecimal("ACCT_NO"));

			entity.setACCT_NAME(rs.getString("ACCT_NAME"));

			entity.setSCHM_CODE(rs.getString("SCHM_CODE"));

			entity.setSCHM_DESC(rs.getString("SCHM_DESC"));

			entity.setACCT_OPN_DATE(rs.getDate("ACCT_OPN_DATE"));

			entity.setCCY(rs.getString("CCY"));

			// ===========================================================
			// AMOUNT DETAILS
			// ===========================================================

			entity.setBAL_EQUI_TO_BWP(rs.getBigDecimal("BAL_EQUI_TO_BWP"));

			entity.setSANCTION_AMT_BWP(rs.getBigDecimal("SANCTION_AMT_BWP"));

			entity.setINT_RATE(rs.getBigDecimal("INT_RATE"));

			entity.setAMT_IN_INR(rs.getBigDecimal("AMT_IN_INR"));

			entity.setVALUE_1(rs.getBigDecimal("VALUE_1"));

			entity.setVALUE_2(rs.getBigDecimal("VALUE_2"));

			return entity;
		}
	}

	// ===========================================================
	// NORMAL DETAIL ROW MAPPER
	// ===========================================================

	private class BORR_UFCE_DetailRowMapper implements RowMapper<BORR_UFCE_Detail_Entity> {

		@Override
		public BORR_UFCE_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			BORR_UFCE_Detail_Entity entity = new BORR_UFCE_Detail_Entity();

			// ===========================================================
			// REPORT DETAILS
			// ===========================================================

			entity.setReport_date(rs.getDate("REPORT_DATE"));

			entity.setReport_version(rs.getString("REPORT_VERSION"));

			entity.setReport_frequency(rs.getString("REPORT_FREQUENCY"));

			entity.setReport_code(rs.getString("REPORT_CODE"));

			entity.setReport_desc(rs.getString("REPORT_DESC"));

			entity.setEntity_flg(rs.getString("ENTITY_FLG"));

			entity.setModify_flg(rs.getString("MODIFY_FLG"));

			entity.setDel_flg(rs.getString("DEL_FLG"));

			// ===========================================================
			// CUSTOMER / ACCOUNT DETAILS
			// ===========================================================

			entity.setCUST_ID(rs.getString("CUST_ID"));

			entity.setACCT_NO(rs.getBigDecimal("ACCT_NO"));

			entity.setACCT_NAME(rs.getString("ACCT_NAME"));

			entity.setSCHM_CODE(rs.getString("SCHM_CODE"));

			entity.setSCHM_DESC(rs.getString("SCHM_DESC"));

			entity.setACCT_OPN_DATE(rs.getDate("ACCT_OPN_DATE"));

			entity.setCCY(rs.getString("CCY"));

			// ===========================================================
			// AMOUNT DETAILS
			// ===========================================================

			entity.setBAL_EQUI_TO_BWP(rs.getBigDecimal("BAL_EQUI_TO_BWP"));

			entity.setSANCTION_AMT_BWP(rs.getBigDecimal("SANCTION_AMT_BWP"));

			entity.setINT_RATE(rs.getBigDecimal("INT_RATE"));

			entity.setAMT_IN_INR(rs.getBigDecimal("AMT_IN_INR"));

			entity.setVALUE_1(rs.getBigDecimal("VALUE_1"));

			entity.setVALUE_2(rs.getBigDecimal("VALUE_2"));

			return entity;
		}
	}

	// ===========================================================
	// ARCHIVAL SUMMARY ROW MAPPER
	// ===========================================================

	private class BORR_UFCE_Archival_SummaryRowMapper implements RowMapper<BORR_UFCE_Archival_Summary_Entity> {

		@Override
		public BORR_UFCE_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			BORR_UFCE_Archival_Summary_Entity entity = new BORR_UFCE_Archival_Summary_Entity();

			// ===========================================================
			// REPORT DETAILS
			// ===========================================================

			entity.setReport_date(rs.getDate("REPORT_DATE"));

			entity.setResubreport_date(rs.getDate("RESUBREPORT_DATE"));

			entity.setReport_version(rs.getString("REPORT_VERSION"));

			entity.setReport_frequency(rs.getString("REPORT_FREQUENCY"));

			entity.setReport_code(rs.getString("REPORT_CODE"));

			entity.setReport_desc(rs.getString("REPORT_DESC"));

			entity.setEntity_flg(rs.getString("ENTITY_FLG"));

			entity.setModify_flg(rs.getString("MODIFY_FLG"));

			entity.setDel_flg(rs.getString("DEL_FLG"));

			// ===========================================================
			// CUSTOMER / ACCOUNT DETAILS
			// ===========================================================

			entity.setCUST_ID(rs.getString("CUST_ID"));

			entity.setACCT_NO(rs.getBigDecimal("ACCT_NO"));

			entity.setACCT_NAME(rs.getString("ACCT_NAME"));

			entity.setSCHM_CODE(rs.getString("SCHM_CODE"));

			entity.setSCHM_DESC(rs.getString("SCHM_DESC"));

			entity.setACCT_OPN_DATE(rs.getDate("ACCT_OPN_DATE"));

			entity.setCCY(rs.getString("CCY"));

			// ===========================================================
			// AMOUNT DETAILS
			// ===========================================================

			entity.setBAL_EQUI_TO_BWP(rs.getBigDecimal("BAL_EQUI_TO_BWP"));

			entity.setSANCTION_AMT_BWP(rs.getBigDecimal("SANCTION_AMT_BWP"));

			entity.setINT_RATE(rs.getBigDecimal("INT_RATE"));

			entity.setAMT_IN_INR(rs.getBigDecimal("AMT_IN_INR"));

			entity.setVALUE_1(rs.getBigDecimal("VALUE_1"));

			entity.setVALUE_2(rs.getBigDecimal("VALUE_2"));

			return entity;
		}
	}
	// ===========================================================
	// ARCHIVAL DETAIL ROW MAPPER
	// ===========================================================

	private class BORR_UFCE_Archival_DetailRowMapper implements RowMapper<BORR_UFCE_Archival_Detail_Entity> {

		@Override
		public BORR_UFCE_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			BORR_UFCE_Archival_Detail_Entity entity = new BORR_UFCE_Archival_Detail_Entity();

			// ===========================================================
			// REPORT DETAILS
			// ===========================================================

			entity.setReport_date(rs.getDate("REPORT_DATE"));

			entity.setResubreport_date(rs.getDate("RESUBREPORT_DATE"));

			entity.setReport_version(rs.getString("REPORT_VERSION"));

			entity.setReport_frequency(rs.getString("REPORT_FREQUENCY"));

			entity.setReport_code(rs.getString("REPORT_CODE"));

			entity.setReport_desc(rs.getString("REPORT_DESC"));

			entity.setEntity_flg(rs.getString("ENTITY_FLG"));

			entity.setModify_flg(rs.getString("MODIFY_FLG"));

			entity.setDel_flg(rs.getString("DEL_FLG"));

			// ===========================================================
			// CUSTOMER / ACCOUNT DETAILS
			// ===========================================================

			entity.setCUST_ID(rs.getString("CUST_ID"));

			entity.setACCT_NO(rs.getBigDecimal("ACCT_NO"));

			entity.setACCT_NAME(rs.getString("ACCT_NAME"));

			entity.setSCHM_CODE(rs.getString("SCHM_CODE"));

			entity.setSCHM_DESC(rs.getString("SCHM_DESC"));

			entity.setACCT_OPN_DATE(rs.getDate("ACCT_OPN_DATE"));

			entity.setCCY(rs.getString("CCY"));

			// ===========================================================
			// AMOUNT DETAILS
			// ===========================================================

			entity.setBAL_EQUI_TO_BWP(rs.getBigDecimal("BAL_EQUI_TO_BWP"));

			entity.setSANCTION_AMT_BWP(rs.getBigDecimal("SANCTION_AMT_BWP"));

			entity.setINT_RATE(rs.getBigDecimal("INT_RATE"));

			entity.setAMT_IN_INR(rs.getBigDecimal("AMT_IN_INR"));

			entity.setVALUE_1(rs.getBigDecimal("VALUE_1"));

			entity.setVALUE_2(rs.getBigDecimal("VALUE_2"));

			return entity;

		}
	}

	// ===========================================================
	// ADDITIONAL METHODS FOR ARCHIVAL & RESUB - BORR_UFCE
	// ===========================================================

	// ===========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION (SUMMARY)
	// ===========================================================
	public List<BORR_UFCE_Archival_Summary_Entity> getArchivalDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BORR_UFCE_Archival_SummaryRowMapper());
	}

	// ===========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION (DETAIL)
	// ===========================================================
	public List<BORR_UFCE_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_BORR_UFCE_ARCHIVAL_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BORR_UFCE_Archival_DetailRowMapper());
	}
	// ===========================================================
	// SERVICE METHODS
	// ===========================================================

	// ------------------------------
	// GET BORR_UFCE DETAIL VIEW
	// ------------------------------
	public ModelAndView getBRRS_BORR_UFCE_DetailView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {
		BigDecimal ver = null;
		if (version != null && !version.trim().isEmpty()) {
			try {
				ver = new BigDecimal(version);
			} catch (Exception ignored) {
			}
		}
		return getBRRS_BORR_UFCE_View(reportId, fromdate, todate, currency, "detail", pageable, type, ver);
	}

	// ------------------------------
	// GET BORR_UFCE ARCHIVAL LIST
	// ------------------------------
	public List<Object[]> getBORR_UFCEArchival() {
		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION "
				+ "FROM BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), null });
	}

	// ===========================================================
	// SERVICE METHODS
	// ===========================================================

	// ------------------------------
	// GET BORR_UFCE MAIN VIEW
	// ------------------------------
	public ModelAndView getBRRS_BORR_UFCE_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("BORR_UFCE View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		Date dtFrom = parseDate(fromdate);
		Date dtTo = parseDate(todate);

		String formattedFromDate = dtFrom != null ? dateformat.format(dtFrom) : fromdate;
		String formattedToDate = dtTo != null ? dateformat.format(dtTo) : todate;

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================
		if ("ARCHIVAL".equals(type) && version != null) {
			try {
				List<BORR_UFCE_Archival_Summary_Entity> archivalSummary = getArchivalDataByDateAndVersion(dtTo, version);

				System.out.println("Archival Summary size = " + archivalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<BORR_UFCE_Archival_Detail_Entity> archivalDetail = getArchivalDetailDataByDateAndVersion(dtTo,
							version);
					mv.addObject("reportdetails", archivalDetail);
					mv.addObject("displaymode", "archivalDetail");
					System.out.println("Archival Detail size = " + archivalDetail.size());
				} else {
					mv.addObject("displaymode", "archivalSummary");
				}

				mv.addObject("reportsummary", archivalSummary);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// =====================================================
		// NORMAL MODE
		// =====================================================
		else {
			try {
				List<BORR_UFCE_Summary_Entity> normalSummary = jdbcTemplate.query(
						"SELECT * FROM BRRS_BORR_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD/MM/YYYY')",
						new Object[] { formattedToDate }, new BORR_UFCE_SummaryRowMapper());

				System.out.println("Normal Summary size = " + normalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<BORR_UFCE_Detail_Entity> normalDetail = jdbcTemplate.query(
							"SELECT * FROM BRRS_BORR_UFCE_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD/MM/YYYY')",
							new Object[] { formattedToDate }, new BORR_UFCE_DetailRowMapper());
					mv.addObject("reportdetails", normalDetail);
					mv.addObject("displaymode", "Details");
					System.out.println("Normal Detail size = " + normalDetail.size());
				} else {
					mv.addObject("displaymode", "summary");
				}

				mv.addObject("reportsummary", normalSummary);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		mv.setViewName("BRRS/BORR_UFCE");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);
		mv.addObject("reportid", reportId);
		mv.addObject("asondate", formattedToDate);
		mv.addObject("fromdate", formattedFromDate);
		mv.addObject("todate", formattedToDate);
		mv.addObject("report_date", formattedToDate);
		mv.addObject("dtltype", dtltype);
		mv.addObject("version", version);
		mv.addObject("type", type);

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// ===========================================================
	// EXCEL GENERATION METHODS
	// ===========================================================

	// ------------------------------
	// GENERATE BORR_UFCE EXCEL FILE
	// ------------------------------
	public byte[] getBRRS_BORR_UFCE_Excel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Generating Excel for BORR_UFCE...");

		Date reportDate = parseDate(todate);
		String formattedDate = reportDate != null ? dateformat.format(reportDate) : todate;

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			List<BORR_UFCE_Archival_Summary_Entity> dataList = getArchivalDataByDateAndVersion(reportDate, version);
			return generateExcelFromArchivalData(dataList, filename);
		} else {
			List<BORR_UFCE_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_BORR_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD/MM/YYYY')",
					new Object[] { formattedDate }, new BORR_UFCE_SummaryRowMapper());
			return generateExcelFromNormalData(dataList, filename);
		}
	}

	// ------------------------------
	// GENERATE EXCEL FROM NORMAL DATA
	// ------------------------------
	private byte[] generateExcelFromNormalData(List<BORR_UFCE_Summary_Entity> dataList, String filename)
			throws Exception {

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = templateDir != null ? Paths.get(templateDir, filename) : null;

		Workbook workbook;
		Sheet sheet;
		int rowIndex;

		if (templatePath != null && Files.exists(templatePath)) {
			try (InputStream is = Files.newInputStream(templatePath)) {
				workbook = WorkbookFactory.create(is);
			}
			sheet = workbook.getSheetAt(0);
			rowIndex = 3;
		} else {
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet("BORR_UFCE");
			createHeaderRow(workbook, sheet);
			rowIndex = 3;
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			CellStyle textStyle = createTextStyle(workbook);
			CellStyle numberStyle = createNumberStyle(workbook);
			CellStyle dateStyle = createDateStyle(workbook);
			CellStyle totalHeaderStyle = createTotalHeaderStyle(workbook);
			CellStyle totalNumberStyle = createTotalNumberStyle(workbook);

			// Populate 1st value of INR RATE into Row 2 (index 1), 11th Column (index 10)
			BigDecimal firstInrRate = (dataList != null && !dataList.isEmpty()) ? dataList.get(0).getVALUE_1() : null;
			Row paramRow = sheet.getRow(1) != null ? sheet.getRow(1) : sheet.createRow(1);
			setCellValue(paramRow, 10, firstInrRate, numberStyle);

			BigDecimal totalAmtInr = BigDecimal.ZERO;
			BigDecimal totalAmtLakhs = BigDecimal.ZERO;

			if (dataList != null) {
				for (BORR_UFCE_Summary_Entity data : dataList) {
					Row row = sheet.createRow(rowIndex++);
					setCellValue(row, 0, data.getCUST_ID(), textStyle);
					setCellValue(row, 1, data.getACCT_NO(), textStyle);
					setCellValue(row, 2, data.getACCT_NAME(), textStyle);
					setCellValue(row, 3, data.getSCHM_CODE(), textStyle);
					setCellValue(row, 4, data.getSCHM_DESC(), textStyle);
					setCellValue(row, 5, data.getACCT_OPN_DATE(), dateStyle);
					setCellValue(row, 6, data.getCCY(), textStyle);
					setCellValue(row, 7, data.getBAL_EQUI_TO_BWP(), numberStyle);
					setCellValue(row, 8, data.getSANCTION_AMT_BWP(), numberStyle);
					setCellValue(row, 9, data.getINT_RATE(), numberStyle);
					setCellValue(row, 10, data.getAMT_IN_INR(), numberStyle);
					setCellValue(row, 11, data.getVALUE_2(), numberStyle);

					if (data.getAMT_IN_INR() != null) {
						totalAmtInr = totalAmtInr.add(data.getAMT_IN_INR());
					}
					if (data.getVALUE_2() != null) {
						totalAmtLakhs = totalAmtLakhs.add(data.getVALUE_2());
					}
				}
			}

			// Add Total Row
			Row totalRow = sheet.createRow(rowIndex++);
			for (int c = 0; c <= 9; c++) {
				Cell cCell = totalRow.createCell(c);
				cCell.setCellStyle(totalHeaderStyle);
				if (c == 0) {
					cCell.setCellValue("Total");
				}
			}
			try {
				sheet.addMergedRegion(new CellRangeAddress(totalRow.getRowNum(), totalRow.getRowNum(), 0, 9));
			} catch (Exception e) {
				// Ignore if merge fails
			}

			setCellValue(totalRow, 10, totalAmtInr, totalNumberStyle);
			setCellValue(totalRow, 11, totalAmtLakhs, totalNumberStyle);

			workbook.write(out);
			workbook.close();
			return out.toByteArray();
		}
	}

	// ------------------------------
	// GENERATE EXCEL FROM ARCHIVAL DATA
	// ------------------------------
	private byte[] generateExcelFromArchivalData(List<BORR_UFCE_Archival_Summary_Entity> dataList, String filename)
			throws Exception {

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = templateDir != null ? Paths.get(templateDir, filename) : null;

		Workbook workbook;
		Sheet sheet;
		int rowIndex;

		if (templatePath != null && Files.exists(templatePath)) {
			try (InputStream is = Files.newInputStream(templatePath)) {
				workbook = WorkbookFactory.create(is);
			}
			sheet = workbook.getSheetAt(0);
			rowIndex = 3;
		} else {
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet("BORR_UFCE_Archival");
			createHeaderRow(workbook, sheet);
			rowIndex = 3;
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			CellStyle textStyle = createTextStyle(workbook);
			CellStyle numberStyle = createNumberStyle(workbook);
			CellStyle dateStyle = createDateStyle(workbook);
			CellStyle totalHeaderStyle = createTotalHeaderStyle(workbook);
			CellStyle totalNumberStyle = createTotalNumberStyle(workbook);

			// Populate 1st value of INR RATE into Row 2 (index 1), 11th Column (index 10)
			BigDecimal firstInrRate = (dataList != null && !dataList.isEmpty()) ? dataList.get(0).getVALUE_1() : null;
			Row paramRow = sheet.getRow(1) != null ? sheet.getRow(1) : sheet.createRow(1);
			setCellValue(paramRow, 10, firstInrRate, numberStyle);

			BigDecimal totalAmtInr = BigDecimal.ZERO;
			BigDecimal totalAmtLakhs = BigDecimal.ZERO;

			if (dataList != null) {
				for (BORR_UFCE_Archival_Summary_Entity data : dataList) {
					Row row = sheet.createRow(rowIndex++);
					setCellValue(row, 0, data.getCUST_ID(), textStyle);
					setCellValue(row, 1, data.getACCT_NO(), textStyle);
					setCellValue(row, 2, data.getACCT_NAME(), textStyle);
					setCellValue(row, 3, data.getSCHM_CODE(), textStyle);
					setCellValue(row, 4, data.getSCHM_DESC(), textStyle);
					setCellValue(row, 5, data.getACCT_OPN_DATE(), dateStyle);
					setCellValue(row, 6, data.getCCY(), textStyle);
					setCellValue(row, 7, data.getBAL_EQUI_TO_BWP(), numberStyle);
					setCellValue(row, 8, data.getSANCTION_AMT_BWP(), numberStyle);
					setCellValue(row, 9, data.getINT_RATE(), numberStyle);
					setCellValue(row, 10, data.getAMT_IN_INR(), numberStyle);
					setCellValue(row, 11, data.getVALUE_2(), numberStyle);

					if (data.getAMT_IN_INR() != null) {
						totalAmtInr = totalAmtInr.add(data.getAMT_IN_INR());
					}
					if (data.getVALUE_2() != null) {
						totalAmtLakhs = totalAmtLakhs.add(data.getVALUE_2());
					}
				}
			}

			// Add Total Row
			Row totalRow = sheet.createRow(rowIndex++);
			for (int c = 0; c <= 9; c++) {
				Cell cCell = totalRow.createCell(c);
				cCell.setCellStyle(totalHeaderStyle);
				if (c == 0) {
					cCell.setCellValue("Total");
				}
			}
			try {
				sheet.addMergedRegion(new CellRangeAddress(totalRow.getRowNum(), totalRow.getRowNum(), 0, 9));
			} catch (Exception e) {
				// Ignore if merge fails
			}

			setCellValue(totalRow, 10, totalAmtInr, totalNumberStyle);
			setCellValue(totalRow, 11, totalAmtLakhs, totalNumberStyle);

			workbook.write(out);
			workbook.close();
			return out.toByteArray();
		}
	}

	// ------------------------------
	// CREATE EXCEL HEADER ROW
	// ------------------------------
	private void createHeaderRow(Workbook workbook, Sheet sheet) {
		Row headerRow = sheet.createRow(2);

		Font headerFont = workbook.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 9);
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		String[] headers = { "CUST ID", "ACCOUNT NO", "ACCT NAME", "SCHM CODE", "SCHM DESC", "ACCT OPN DATE", "CCY",
				"BAL EQUI TO BWP", "SANCTION AMOUNT IN BWP", "INT RATE", "AMOUNT IN INR",
				"AMOUNT IN LAKHS" };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}
	}

	// ------------------------------
	// CREATE TEXT CELL STYLE
	// ------------------------------
	private CellStyle createTextStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);

		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setFont(font);
		textStyle.setWrapText(true);
		textStyle.setBorderBottom(BorderStyle.THIN);
		textStyle.setBorderTop(BorderStyle.THIN);
		textStyle.setBorderLeft(BorderStyle.THIN);
		textStyle.setBorderRight(BorderStyle.THIN);
		return textStyle;
	}

	// ------------------------------
	// CREATE NUMBER CELL STYLE
	// ------------------------------
	private CellStyle createNumberStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);

		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setFont(font);
		numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
		numberStyle.setBorderBottom(BorderStyle.THIN);
		numberStyle.setBorderTop(BorderStyle.THIN);
		numberStyle.setBorderLeft(BorderStyle.THIN);
		numberStyle.setBorderRight(BorderStyle.THIN);
		return numberStyle;
	}

	// ------------------------------
	// CREATE DATE CELL STYLE
	// ------------------------------
	private CellStyle createDateStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);

		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFont(font);
		dateStyle.setDataFormat(dataFormat.getFormat("dd/MM/yyyy"));
		dateStyle.setBorderBottom(BorderStyle.THIN);
		dateStyle.setBorderTop(BorderStyle.THIN);
		dateStyle.setBorderLeft(BorderStyle.THIN);
		dateStyle.setBorderRight(BorderStyle.THIN);
		return dateStyle;
	}

	// ------------------------------
	// CREATE TOTAL HEADER STYLE
	// ------------------------------
	private CellStyle createTotalHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 9);
		font.setBold(true);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	// ------------------------------
	// CREATE TOTAL NUMBER STYLE
	// ------------------------------
	private CellStyle createTotalNumberStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);
		font.setBold(true);

		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setDataFormat(dataFormat.getFormat("#,##0.00"));
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	// ------------------------------
	// SET CELL VALUE UTILITY
	// ------------------------------
	private void setCellValue(Row row, int column, Object value, CellStyle style) {
		Cell cell = row.createCell(column);
		if (value == null) {
			cell.setCellValue("");
			cell.setCellStyle(style);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
			cell.setCellStyle(style);
		} else if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			if (bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0) {
				cell.setCellValue(bd.longValue());
			} else {
				cell.setCellValue(bd.doubleValue());
			}
			cell.setCellStyle(style);
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
			cell.setCellStyle(style);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
			cell.setCellStyle(style);
		} else {
			cell.setCellValue(value.toString());
			cell.setCellStyle(style);
		}
	}

	// ------------------------------
	// PARSE DATE UTILITY METHOD
	// ------------------------------
	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		try {
			return dateformat.parse(dateStr);
		} catch (Exception e) {
			try {
				return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			} catch (Exception e1) {
				try {
					return new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
				} catch (Exception e2) {
					try {
						return new SimpleDateFormat("dd-MMM-yyyy").parse(dateStr);
					} catch (Exception e3) {
						try {
							return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
						} catch (Exception e4) {
							logger.error("Failed to parse date: {}", dateStr);
							return null;
						}
					}
				}
			}
		}
	}

	// ===========================================================
	// SAVE NORMAL BORR_UFCE SUMMARY DATA TO ARCHIVAL TABLE
	// ===========================================================

	// ------------------------------
	// SAVE NORMAL TO ARCHIVAL METHOD
	// ------------------------------
	private void saveToArchivalFromNormal(BORR_UFCE_Summary_Entity oldRecord, BigDecimal version) {

		String archivalSummarySql = "INSERT INTO BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE "
				+ "(CUST_ID, ACCT_NO, ACCT_NAME, SCHM_CODE, SCHM_DESC, "
				+ "ACCT_OPN_DATE, CCY, BAL_EQUI_TO_BWP, SANCTION_AMT_BWP, "
				+ "INT_RATE, AMT_IN_INR, VALUE_1, VALUE_2, REPORT_DATE, "
				+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " + "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {

			jdbcTemplate.update(archivalSummarySql,

					oldRecord.getCUST_ID(), oldRecord.getACCT_NO(), oldRecord.getACCT_NAME(), oldRecord.getSCHM_CODE(),
					oldRecord.getSCHM_DESC(), oldRecord.getACCT_OPN_DATE(), oldRecord.getCCY(),
					oldRecord.getBAL_EQUI_TO_BWP(), oldRecord.getSANCTION_AMT_BWP(), oldRecord.getINT_RATE(),
					oldRecord.getAMT_IN_INR(), oldRecord.getVALUE_1(), oldRecord.getVALUE_2(),
					oldRecord.getReport_date(),

					// ARCHIVAL VERSION
					version,

					oldRecord.getReport_frequency(), oldRecord.getReport_code(), oldRecord.getReport_desc(),
					oldRecord.getEntity_flg(), oldRecord.getModify_flg(), oldRecord.getDel_flg());

		} catch (Exception e) {

			logger.error("Error saving BORR_UFCE to ARCHIVAL SUMMARY: {}", e.getMessage(), e);
		}

		String archivalDetailSql = "INSERT INTO BRRS_BORR_UFCE_ARCHIVAL_DETAILTABLE "
				+ "(CUST_ID, ACCT_NO, ACCT_NAME, SCHM_CODE, SCHM_DESC, "
				+ "ACCT_OPN_DATE, CCY, BAL_EQUI_TO_BWP, SANCTION_AMT_BWP, "
				+ "INT_RATE, AMT_IN_INR, VALUE_1, VALUE_2, REPORT_DATE, "
				+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " + "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {

			jdbcTemplate.update(archivalDetailSql,

					oldRecord.getCUST_ID(), oldRecord.getACCT_NO(), oldRecord.getACCT_NAME(), oldRecord.getSCHM_CODE(),
					oldRecord.getSCHM_DESC(), oldRecord.getACCT_OPN_DATE(), oldRecord.getCCY(),
					oldRecord.getBAL_EQUI_TO_BWP(), oldRecord.getSANCTION_AMT_BWP(), oldRecord.getINT_RATE(),
					oldRecord.getAMT_IN_INR(), oldRecord.getVALUE_1(), oldRecord.getVALUE_2(),
					oldRecord.getReport_date(),

					// ARCHIVAL VERSION
					version,

					oldRecord.getReport_frequency(), oldRecord.getReport_code(), oldRecord.getReport_desc(),
					oldRecord.getEntity_flg(), oldRecord.getModify_flg(), oldRecord.getDel_flg());

		} catch (Exception e) {

			logger.error("Error saving BORR_UFCE to ARCHIVAL DETAIL: {}", e.getMessage(), e);
		}
	}

	// ------------------------------
	// GET NEXT ARCHIVAL VERSION
	// ------------------------------
	private BigDecimal getNextArchivalVersion(String reportDate) {
		try {
			String sql = "SELECT COALESCE(MAX(REPORT_VERSION), 0) + 1 FROM BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD/MM/YYYY')";
			BigDecimal nextVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return nextVersion != null ? nextVersion : BigDecimal.ONE;
		} catch (Exception e) {
			return BigDecimal.ONE;
		}
	}
}
