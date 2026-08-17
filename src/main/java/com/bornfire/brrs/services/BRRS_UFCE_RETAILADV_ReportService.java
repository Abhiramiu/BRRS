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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Component
public class BRRS_UFCE_RETAILADV_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_UFCE_RETAILADV_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		String[] formats = { "dd-MMM-yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy" };
		for (String format : formats) {
			try {
				return new SimpleDateFormat(format).parse(dateStr);
			} catch (ParseException ignored) {
			}
		}
		return null;
	}

	// ===========================================================
	// INNER ENTITY CLASSES
	// ===========================================================

	// Summary Entity
	public static class RETAILADV_UFCE_Summary_Entity {
		private Long sno;
		private String custId;
		private String accountNo;
		private String schmCode;
		private String schmDesc;
		private Date acctOpnDate;
		private String ccy;
		private BigDecimal sanctionAmount;
		private BigDecimal outstandingBalanceBwp;
		private String intRate;
		private BigDecimal outstandingBalanceInr;
		private String type;
		private String segment;
		private String category;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;

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

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getSchmCode() {
			return schmCode;
		}

		public void setSchmCode(String schmCode) {
			this.schmCode = schmCode;
		}

		public String getSchmDesc() {
			return schmDesc;
		}

		public void setSchmDesc(String schmDesc) {
			this.schmDesc = schmDesc;
		}

		public Date getAcctOpnDate() {
			return acctOpnDate;
		}

		public void setAcctOpnDate(Date acctOpnDate) {
			this.acctOpnDate = acctOpnDate;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public BigDecimal getSanctionAmount() {
			return sanctionAmount;
		}

		public void setSanctionAmount(BigDecimal sanctionAmount) {
			this.sanctionAmount = sanctionAmount;
		}

		public BigDecimal getOutstandingBalanceBwp() {
			return outstandingBalanceBwp;
		}

		public void setOutstandingBalanceBwp(BigDecimal outstandingBalanceBwp) {
			this.outstandingBalanceBwp = outstandingBalanceBwp;
		}

		public String getIntRate() {
			return intRate;
		}

		public void setIntRate(String intRate) {
			this.intRate = intRate;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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
	}

	// Detail Entity
	public static class RETAILADV_UFCE_Detail_Entity {
		private Long sno;
		private String custId;
		private String accountNo;
		private String schmCode;
		private String schmDesc;
		private Date acctOpnDate;
		private String ccy;
		private BigDecimal sanctionAmount;
		private BigDecimal outstandingBalanceBwp;
		private String intRate;
		private BigDecimal outstandingBalanceInr;
		private String type;
		private String segment;
		private String category;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;

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

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getSchmCode() {
			return schmCode;
		}

		public void setSchmCode(String schmCode) {
			this.schmCode = schmCode;
		}

		public String getSchmDesc() {
			return schmDesc;
		}

		public void setSchmDesc(String schmDesc) {
			this.schmDesc = schmDesc;
		}

		public Date getAcctOpnDate() {
			return acctOpnDate;
		}

		public void setAcctOpnDate(Date acctOpnDate) {
			this.acctOpnDate = acctOpnDate;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public BigDecimal getSanctionAmount() {
			return sanctionAmount;
		}

		public void setSanctionAmount(BigDecimal sanctionAmount) {
			this.sanctionAmount = sanctionAmount;
		}

		public BigDecimal getOutstandingBalanceBwp() {
			return outstandingBalanceBwp;
		}

		public void setOutstandingBalanceBwp(BigDecimal outstandingBalanceBwp) {
			this.outstandingBalanceBwp = outstandingBalanceBwp;
		}

		public String getIntRate() {
			return intRate;
		}

		public void setIntRate(String intRate) {
			this.intRate = intRate;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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
	}

	// Archival Summary Entity
	public static class RETAILADV_UFCE_Archival_Summary_Entity {
		private Long sno;
		private String custId;
		private String accountNo;
		private String schmCode;
		private String schmDesc;
		private Date acctOpnDate;
		private String ccy;
		private BigDecimal sanctionAmount;
		private BigDecimal outstandingBalanceBwp;
		private String intRate;
		private BigDecimal outstandingBalanceInr;
		private String type;
		private String segment;
		private String category;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

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

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getSchmCode() {
			return schmCode;
		}

		public void setSchmCode(String schmCode) {
			this.schmCode = schmCode;
		}

		public String getSchmDesc() {
			return schmDesc;
		}

		public void setSchmDesc(String schmDesc) {
			this.schmDesc = schmDesc;
		}

		public Date getAcctOpnDate() {
			return acctOpnDate;
		}

		public void setAcctOpnDate(Date acctOpnDate) {
			this.acctOpnDate = acctOpnDate;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public BigDecimal getSanctionAmount() {
			return sanctionAmount;
		}

		public void setSanctionAmount(BigDecimal sanctionAmount) {
			this.sanctionAmount = sanctionAmount;
		}

		public BigDecimal getOutstandingBalanceBwp() {
			return outstandingBalanceBwp;
		}

		public void setOutstandingBalanceBwp(BigDecimal outstandingBalanceBwp) {
			this.outstandingBalanceBwp = outstandingBalanceBwp;
		}

		public String getIntRate() {
			return intRate;
		}

		public void setIntRate(String intRate) {
			this.intRate = intRate;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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

	// Archival Detail Entity
	public static class RETAILADV_UFCE_Archival_Detail_Entity {
		private Long sno;
		private String custId;
		private String accountNo;
		private String schmCode;
		private String schmDesc;
		private Date acctOpnDate;
		private String ccy;
		private BigDecimal sanctionAmount;
		private BigDecimal outstandingBalanceBwp;
		private String intRate;
		private BigDecimal outstandingBalanceInr;
		private String type;
		private String segment;
		private String category;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

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

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getSchmCode() {
			return schmCode;
		}

		public void setSchmCode(String schmCode) {
			this.schmCode = schmCode;
		}

		public String getSchmDesc() {
			return schmDesc;
		}

		public void setSchmDesc(String schmDesc) {
			this.schmDesc = schmDesc;
		}

		public Date getAcctOpnDate() {
			return acctOpnDate;
		}

		public void setAcctOpnDate(Date acctOpnDate) {
			this.acctOpnDate = acctOpnDate;
		}

		public String getCcy() {
			return ccy;
		}

		public void setCcy(String ccy) {
			this.ccy = ccy;
		}

		public BigDecimal getSanctionAmount() {
			return sanctionAmount;
		}

		public void setSanctionAmount(BigDecimal sanctionAmount) {
			this.sanctionAmount = sanctionAmount;
		}

		public BigDecimal getOutstandingBalanceBwp() {
			return outstandingBalanceBwp;
		}

		public void setOutstandingBalanceBwp(BigDecimal outstandingBalanceBwp) {
			this.outstandingBalanceBwp = outstandingBalanceBwp;
		}

		public String getIntRate() {
			return intRate;
		}

		public void setIntRate(String intRate) {
			this.intRate = intRate;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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

	// ===========================================================
	// ROW MAPPER CLASSES
	// ===========================================================

	private class SummaryRowMapper implements RowMapper<RETAILADV_UFCE_Summary_Entity> {
		@Override
		public RETAILADV_UFCE_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			RETAILADV_UFCE_Summary_Entity entity = new RETAILADV_UFCE_Summary_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setCustId(rs.getString("CUST_ID"));
			entity.setAccountNo(rs.getString("ACCOUNT_NO"));
			entity.setSchmCode(rs.getString("SCHM_CODE"));
			entity.setSchmDesc(rs.getString("SCHM_DESC"));
			entity.setAcctOpnDate(rs.getDate("ACCT_OPN_DATE"));
			entity.setCcy(rs.getString("CCY"));
			entity.setSanctionAmount(rs.getBigDecimal("SANCTION_AMOUNT"));
			entity.setOutstandingBalanceBwp(rs.getBigDecimal("OUTSTANDING_BALANCE_BWP"));
			entity.setIntRate(rs.getString("INT_RATE"));
			entity.setOutstandingBalanceInr(rs.getBigDecimal("OUTSTANDING_BALANCE_INR"));
			entity.setType(rs.getString("TYPE"));
			entity.setSegment(rs.getString("SEGMENT"));
			entity.setCategory(rs.getString("CATEGORY"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			return entity;
		}
	}

	private class DetailRowMapper implements RowMapper<RETAILADV_UFCE_Detail_Entity> {
		@Override
		public RETAILADV_UFCE_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			RETAILADV_UFCE_Detail_Entity entity = new RETAILADV_UFCE_Detail_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setCustId(rs.getString("CUST_ID"));
			entity.setAccountNo(rs.getString("ACCOUNT_NO"));
			entity.setSchmCode(rs.getString("SCHM_CODE"));
			entity.setSchmDesc(rs.getString("SCHM_DESC"));
			entity.setAcctOpnDate(rs.getDate("ACCT_OPN_DATE"));
			entity.setCcy(rs.getString("CCY"));
			entity.setSanctionAmount(rs.getBigDecimal("SANCTION_AMOUNT"));
			entity.setOutstandingBalanceBwp(rs.getBigDecimal("OUTSTANDING_BALANCE_BWP"));
			entity.setIntRate(rs.getString("INT_RATE"));
			entity.setOutstandingBalanceInr(rs.getBigDecimal("OUTSTANDING_BALANCE_INR"));
			entity.setType(rs.getString("TYPE"));
			entity.setSegment(rs.getString("SEGMENT"));
			entity.setCategory(rs.getString("CATEGORY"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			return entity;
		}
	}

	private class ArchivalSummaryRowMapper implements RowMapper<RETAILADV_UFCE_Archival_Summary_Entity> {
		@Override
		public RETAILADV_UFCE_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			RETAILADV_UFCE_Archival_Summary_Entity entity = new RETAILADV_UFCE_Archival_Summary_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setCustId(rs.getString("CUST_ID"));
			entity.setAccountNo(rs.getString("ACCOUNT_NO"));
			entity.setSchmCode(rs.getString("SCHM_CODE"));
			entity.setSchmDesc(rs.getString("SCHM_DESC"));
			entity.setAcctOpnDate(rs.getDate("ACCT_OPN_DATE"));
			entity.setCcy(rs.getString("CCY"));
			entity.setSanctionAmount(rs.getBigDecimal("SANCTION_AMOUNT"));
			entity.setOutstandingBalanceBwp(rs.getBigDecimal("OUTSTANDING_BALANCE_BWP"));
			entity.setIntRate(rs.getString("INT_RATE"));
			entity.setOutstandingBalanceInr(rs.getBigDecimal("OUTSTANDING_BALANCE_INR"));
			entity.setType(rs.getString("TYPE"));
			entity.setSegment(rs.getString("SEGMENT"));
			entity.setCategory(rs.getString("CATEGORY"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			try {
				entity.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			} catch (SQLException ignored) {
			}
			return entity;
		}
	}

	private class ArchivalDetailRowMapper implements RowMapper<RETAILADV_UFCE_Archival_Detail_Entity> {
		@Override
		public RETAILADV_UFCE_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			RETAILADV_UFCE_Archival_Detail_Entity entity = new RETAILADV_UFCE_Archival_Detail_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setCustId(rs.getString("CUST_ID"));
			entity.setAccountNo(rs.getString("ACCOUNT_NO"));
			entity.setSchmCode(rs.getString("SCHM_CODE"));
			entity.setSchmDesc(rs.getString("SCHM_DESC"));
			entity.setAcctOpnDate(rs.getDate("ACCT_OPN_DATE"));
			entity.setCcy(rs.getString("CCY"));
			entity.setSanctionAmount(rs.getBigDecimal("SANCTION_AMOUNT"));
			entity.setOutstandingBalanceBwp(rs.getBigDecimal("OUTSTANDING_BALANCE_BWP"));
			entity.setIntRate(rs.getString("INT_RATE"));
			entity.setOutstandingBalanceInr(rs.getBigDecimal("OUTSTANDING_BALANCE_INR"));
			entity.setType(rs.getString("TYPE"));
			entity.setSegment(rs.getString("SEGMENT"));
			entity.setCategory(rs.getString("CATEGORY"));
			entity.setReportDate(rs.getDate("REPORT_DATE"));
			entity.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			entity.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			entity.setReportCode(rs.getString("REPORT_CODE"));
			entity.setReportDesc(rs.getString("REPORT_DESC"));
			entity.setEntityFlg(rs.getString("ENTITY_FLG"));
			entity.setModifyFlg(rs.getString("MODIFY_FLG"));
			entity.setDelFlg(rs.getString("DEL_FLG"));
			try {
				entity.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			} catch (SQLException ignored) {
			}
			return entity;
		}
	}

	// ===========================================================
	// SERVICE METHODS
	// ===========================================================

	public ModelAndView getBRRS_UFCE_RETAILADV_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		logger.info("RETAILADV_UFCE View Called - Type: {}, Version: {}, dtltype: {}", type, version, dtltype);

		Date dt = parseDate(todate);
		String formattedDate = dt != null ? dateformat.format(dt) : todate;

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================
		if ("ARCHIVAL".equals(type)) {
			try {
				List<RETAILADV_UFCE_Archival_Summary_Entity> archivalSummary = (version != null)
						? getArchivalDataByDateAndVersion(dt, version)
						: getArchivalData(formattedDate, null);

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<RETAILADV_UFCE_Archival_Detail_Entity> archivalDetail = (version != null)
							? getArchivalDetailDataByDateAndVersion(dt, version)
							: jdbcTemplate.query(
									"SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
									new Object[] { formattedDate }, new ArchivalDetailRowMapper());
					mv.addObject("reportdetails", archivalDetail);
					mv.addObject("displaymode", "archivalDetail");
				} else {
					mv.addObject("displaymode", "archivalSummary");
				}

				mv.addObject("reportsummary", archivalSummary);
				mv.addObject("report_date", formattedDate);

			} catch (Exception e) {
				logger.error("Error in RETAILADV_UFCE Archival View", e);
			}
		}
		// =====================================================
		// RESUB MODE (Uses Archival Tables)
		// =====================================================
		else if ("RESUB".equals(type)) {
			try {
				List<RETAILADV_UFCE_Archival_Summary_Entity> resubSummary = (version != null)
						? getResubDataByDateAndVersion(dt, version)
						: getResubData(formattedDate, null);

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<RETAILADV_UFCE_Archival_Detail_Entity> resubDetail = (version != null)
							? getResubDetailDataByDateAndVersion(dt, version)
							: jdbcTemplate.query(
									"SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
									new Object[] { formattedDate }, new ArchivalDetailRowMapper());
					mv.addObject("reportdetails", resubDetail);
					mv.addObject("displaymode", "resubDetail");
				} else {
					mv.addObject("displaymode", "resubSummary");
				}

				mv.addObject("reportsummary", resubSummary);
				mv.addObject("report_date", formattedDate);

			} catch (Exception e) {
				logger.error("Error in RETAILADV_UFCE Resub View", e);
			}
		}
		// =====================================================
		// NORMAL MODE
		// =====================================================
		else {
			try {
				List<RETAILADV_UFCE_Summary_Entity> normalSummary = jdbcTemplate.query(
						"SELECT * FROM BRRS_RETAILADV_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
						new Object[] { formattedDate }, new SummaryRowMapper());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<RETAILADV_UFCE_Detail_Entity> normalDetail = jdbcTemplate.query(
							"SELECT * FROM BRRS_RETAILADV_UFCE_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
							new Object[] { formattedDate }, new DetailRowMapper());
					mv.addObject("reportdetails", normalDetail);
					mv.addObject("displaymode", "Details");
				} else {
					mv.addObject("displaymode", "summary");
				}

				mv.addObject("reportsummary", normalSummary);
				mv.addObject("report_date", formattedDate);

			} catch (Exception e) {
				logger.error("Error in RETAILADV_UFCE Normal View", e);
			}
		}

		mv.setViewName("BRRS/UFCE_RETAILADV");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);
		mv.addObject("type", type);
		mv.addObject("version", version);
		mv.addObject("dtltype", dtltype);

		return mv;
	}

	public ModelAndView getBRRS_UFCE_RETAILADV_DetailView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {
		BigDecimal ver = null;
		if (version != null && !version.trim().isEmpty()) {
			try {
				ver = new BigDecimal(version);
			} catch (Exception ignored) {
			}
		}
		return getBRRS_UFCE_RETAILADV_View(reportId, fromdate, todate, currency, "detail", pageable, type, ver);
	}

	// ===========================================================
	// ARCHIVAL & RESUB DATA FETCHERS
	// ===========================================================

	public List<RETAILADV_UFCE_Archival_Summary_Entity> getArchivalDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	public List<RETAILADV_UFCE_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	public List<RETAILADV_UFCE_Archival_Summary_Entity> getResubDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	public List<RETAILADV_UFCE_Archival_Detail_Entity> getResubDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	public List<RETAILADV_UFCE_Archival_Summary_Entity> getArchivalData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_RETAILADV_UFCE_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalSummaryRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalSummaryRowMapper());
	}

	public List<RETAILADV_UFCE_Archival_Summary_Entity> getResubData(String reportDate, BigDecimal version) {
		return getArchivalData(reportDate, version);
	}

	public List<Object[]> getRETAILADV_UFCEArchival() {
		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_RETAILADV_UFCE_ARCHIVAL_SUMMARYTABLE ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<Object[]> getRETAILADV_UFCEResubList() {
		return getRETAILADV_UFCEArchival();
	}

	// ===========================================================
	// EXCEL DOWNLOAD METHOD
	// ===========================================================

	public byte[] getBRRS_UFCE_RETAILADV_Excel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Generating Excel for RETAILADV_UFCE...");

		Date reportDate = parseDate(todate);
		String formattedDate = reportDate != null ? dateformat.format(reportDate) : todate;

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			List<RETAILADV_UFCE_Archival_Summary_Entity> dataList = getArchivalData(formattedDate, version);
			return generateExcelFromArchivalData(dataList, filename, type);
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			List<RETAILADV_UFCE_Archival_Summary_Entity> dataList = getResubData(formattedDate, version);
			return generateExcelFromResubData(dataList, filename, type);
		} else {
			List<RETAILADV_UFCE_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_RETAILADV_UFCE_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
					new Object[] { formattedDate }, new SummaryRowMapper());
			return generateExcelFromNormalData(dataList, filename);
		}
	}

	private byte[] generateExcelFromNormalData(List<RETAILADV_UFCE_Summary_Entity> dataList, String filename)
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
			sheet = workbook.createSheet("RETAILADV_UFCE");
			createHeaderRow(workbook, sheet, false);
			rowIndex = 3;
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			CellStyle textStyle = createTextStyle(workbook);
			CellStyle numberStyle = createNumberStyle(workbook);
			CellStyle dateStyle = createDateStyle(workbook);

			if (dataList != null) {
				for (RETAILADV_UFCE_Summary_Entity data : dataList) {
					Row row = sheet.createRow(rowIndex++);
					setCellValue(row, 0, data.getCustId(), textStyle);
					setCellValue(row, 1, data.getAccountNo(), textStyle);
					setCellValue(row, 2, data.getSchmCode(), textStyle);
					setCellValue(row, 3, data.getSchmDesc(), textStyle);
					setCellValue(row, 4, data.getAcctOpnDate(), dateStyle);
					setCellValue(row, 5, data.getCcy(), textStyle);
					setCellValue(row, 6, data.getSanctionAmount(), numberStyle);
					setCellValue(row, 7, data.getOutstandingBalanceBwp(), numberStyle);
					setCellValue(row, 8, data.getIntRate(), textStyle);
					setCellValue(row, 9, data.getOutstandingBalanceInr(), numberStyle);
					setCellValue(row, 10, data.getType(), textStyle);
					setCellValue(row, 11, data.getSegment(), textStyle);
					setCellValue(row, 12, data.getCategory(), textStyle);
				}
			}

			workbook.write(out);
			workbook.close();
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromArchivalData(List<RETAILADV_UFCE_Archival_Summary_Entity> dataList, String filename,
			String type) throws Exception {

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
			sheet = workbook.createSheet("RETAILADV_UFCE_Archival");
			createHeaderRow(workbook, sheet, true);
			rowIndex = 3;
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			CellStyle textStyle = createTextStyle(workbook);
			CellStyle numberStyle = createNumberStyle(workbook);
			CellStyle dateStyle = createDateStyle(workbook);

			if (dataList != null) {
				for (RETAILADV_UFCE_Archival_Summary_Entity data : dataList) {
					Row row = sheet.createRow(rowIndex++);
					setCellValue(row, 0, data.getCustId(), textStyle);
					setCellValue(row, 1, data.getAccountNo(), textStyle);
					setCellValue(row, 2, data.getSchmCode(), textStyle);
					setCellValue(row, 3, data.getSchmDesc(), textStyle);
					setCellValue(row, 4, data.getAcctOpnDate(), dateStyle);
					setCellValue(row, 5, data.getCcy(), textStyle);
					setCellValue(row, 6, data.getSanctionAmount(), numberStyle);
					setCellValue(row, 7, data.getOutstandingBalanceBwp(), numberStyle);
					setCellValue(row, 8, data.getIntRate(), textStyle);
					setCellValue(row, 9, data.getOutstandingBalanceInr(), numberStyle);
					setCellValue(row, 10, data.getType(), textStyle);
					setCellValue(row, 11, data.getSegment(), textStyle);
					setCellValue(row, 12, data.getCategory(), textStyle);
				}
			}

			workbook.write(out);
			workbook.close();
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromResubData(List<RETAILADV_UFCE_Archival_Summary_Entity> dataList, String filename,
			String type) throws Exception {
		return generateExcelFromArchivalData(dataList, filename, type);
	}

	private void createHeaderRow(Workbook workbook, Sheet sheet, boolean isArchival) {
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

		String[] headers = { "Customer ID", "Account Number", "Scheme Code", "Scheme Description",
				"Account Opening Date", "Currency", "Sanction Amount", "Outstanding Balance (BWP)", "Interest Rate",
				"Outstanding Balance (INR)", "Type", "Segment", "Category" };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}
	}

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

	private CellStyle createDateStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 8);

		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFont(font);
		dateStyle.setDataFormat(dataFormat.getFormat("dd-MM-yyyy"));
		dateStyle.setBorderBottom(BorderStyle.THIN);
		dateStyle.setBorderTop(BorderStyle.THIN);
		dateStyle.setBorderLeft(BorderStyle.THIN);
		dateStyle.setBorderRight(BorderStyle.THIN);
		return dateStyle;
	}

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
}
