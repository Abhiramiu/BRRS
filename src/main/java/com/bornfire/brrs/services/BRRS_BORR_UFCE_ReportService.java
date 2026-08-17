package com.bornfire.brrs.services;

import java.math.BigDecimal;
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

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

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

		String sql = "SELECT * FROM BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE"
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

	public ModelAndView getBRRS_BORR_UFCE_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("BORR_UFCE View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================
		if ("ARCHIVAL".equals(type) && version != null) {
			try {
				Date dt = dateformat.parse(todate);

				List<BORR_UFCE_Archival_Summary_Entity> archivalSummary = getArchivalDataByDateAndVersion(dt, version);

				System.out.println("Archival Summary size = " + archivalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<BORR_UFCE_Archival_Detail_Entity> archivalDetail = getArchivalDetailDataByDateAndVersion(dt,
							version);
					mv.addObject("reportdetails", archivalDetail);
					mv.addObject("displaymode", "archivalDetail");
					System.out.println("Archival Detail size = " + archivalDetail.size());
				} else {
					mv.addObject("displaymode", "archivalSummary");
				}

				mv.addObject("reportsummary", archivalSummary);
				mv.addObject("report_date", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// =====================================================
		// NORMAL MODE
		// =====================================================
		else {
			try {
				Date dt = dateformat.parse(todate);
				String formattedDate = dateformat.format(dt);

				List<BORR_UFCE_Summary_Entity> normalSummary = jdbcTemplate.query(
						"SELECT * FROM BRRS_BORR_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
						new Object[] { formattedDate }, new BORR_UFCE_SummaryRowMapper());

				System.out.println("Normal Summary size = " + normalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<BORR_UFCE_Detail_Entity> normalDetail = jdbcTemplate.query(
							"SELECT * FROM BRRS_BORR_UFCE_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
							new Object[] { formattedDate }, new BORR_UFCE_DetailRowMapper());
					mv.addObject("reportdetails", normalDetail);
					mv.addObject("displaymode", "Details");
					System.out.println("Normal Detail size = " + normalDetail.size());
				} else {
					mv.addObject("displaymode", "summary");
				}

				mv.addObject("reportsummary", normalSummary);
				mv.addObject("report_date", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		mv.setViewName("BRRS/BORR_UFCE");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// ===========================================================
	// SAVE NORMAL BORR_UFCE SUMMARY DATA TO ARCHIVAL TABLE
	// ===========================================================

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

	// Get next version for archival
	private BigDecimal getNextArchivalVersion(String reportDate) {
			try {
				String sql = "SELECT COALESCE(MAX(REPORT_VERSION), 0) + 1 FROM BRRS_BORR_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
				BigDecimal nextVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
				return nextVersion != null ? nextVersion : BigDecimal.ONE;
			} catch (Exception e) {
				return BigDecimal.ONE;
			}
}
}