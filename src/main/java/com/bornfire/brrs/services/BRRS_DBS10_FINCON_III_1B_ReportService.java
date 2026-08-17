package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

import com.bornfire.brrs.services.BRRS_DBS10_FINCON_II_1A_ReportService.DBS10_FINCON_II_1A_Detail_Entity;

@Service
@Component
public class BRRS_DBS10_FINCON_III_1B_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_DBS10_FINCON_III_1B_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ===========================================================
	// INNER ENTITY CLASSES
	// ===========================================================

	// Summary Entity
	public static class BRRS_DBS10_FINCON_III_1B_Summary_Entity {
	    private BigDecimal transSerialNo;
	    private String nameOfSfis;
	    private String nameOfCounterParty;
	    private String natureOfTrans;
	    private String orgIssue;
	    private Date dateOfTrnsBegin;
	    private Date dateOfTrnsEnd;
	    private String tenorOfTrans;
	    private BigDecimal amt;
	    private String returnVal;
	    private Date reportDate;
	    private BigDecimal reportVersion;
	    private String reportFrequency;
	    private String reportCode;
	    private String reportDesc;
	    private String entityFlg;
	    private String modifyFlg;
	    private String delFlg;
	    private Date reportResubDate;

	    // Getters and Setters
	    public BigDecimal getTransSerialNo() {
	        return transSerialNo;
	    }

	    public void setTransSerialNo(BigDecimal transSerialNo) {
	        this.transSerialNo = transSerialNo;
	    }

	    public String getNameOfSfis() {
	        return nameOfSfis;
	    }

	    public void setNameOfSfis(String nameOfSfis) {
	        this.nameOfSfis = nameOfSfis;
	    }

	    public String getNameOfCounterParty() {
	        return nameOfCounterParty;
	    }

	    public void setNameOfCounterParty(String nameOfCounterParty) {
	        this.nameOfCounterParty = nameOfCounterParty;
	    }

	    public String getNatureOfTrans() {
	        return natureOfTrans;
	    }

	    public void setNatureOfTrans(String natureOfTrans) {
	        this.natureOfTrans = natureOfTrans;
	    }

	    public String getOrgIssue() {
	        return orgIssue;
	    }

	    public void setOrgIssue(String orgIssue) {
	        this.orgIssue = orgIssue;
	    }

	    public Date getDateOfTrnsBegin() {
	        return dateOfTrnsBegin;
	    }

	    public void setDateOfTrnsBegin(Date dateOfTrnsBegin) {
	        this.dateOfTrnsBegin = dateOfTrnsBegin;
	    }

	    public Date getDateOfTrnsEnd() {
	        return dateOfTrnsEnd;
	    }

	    public void setDateOfTrnsEnd(Date dateOfTrnsEnd) {
	        this.dateOfTrnsEnd = dateOfTrnsEnd;
	    }

	    public String getTenorOfTrans() {
	        return tenorOfTrans;
	    }

	    public void setTenorOfTrans(String tenorOfTrans) {
	        this.tenorOfTrans = tenorOfTrans;
	    }

	    public BigDecimal getAmt() {
	        return amt;
	    }

	    public void setAmt(BigDecimal amt) {
	        this.amt = amt;
	    }

	    public String getReturnVal() {
	        return returnVal;
	    }

	    public void setReturnVal(String returnVal) {
	        this.returnVal = returnVal;
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

	    public Date getReportResubDate() {
	        return reportResubDate;
	    }

	    public void setReportResubDate(Date reportResubDate) {
	        this.reportResubDate = reportResubDate;
	    }
	}
	// Detail Entity
	public static class BRRS_DBS10_FINCON_III_1B_Detail_Entity {
		private Long sno;
		private String customerId;
		private String accountNumber;
		private String schemeCode;
		private String glCode;
		private String glDescription;
		private String typeOfLoan;
		private String name;
		private String accountCurrency;
		private BigDecimal outstandingBalanceAcctCcy;
		private BigDecimal outstandingBalanceInr;
		private Date accountOpeningDate;
		private Date maturityDate;
		private BigDecimal tenorMonth;
		private BigDecimal emiOfLoan;
		private String floatingFixed;
		private String existingBenchmark;
		private String existingRepricingFrequency;
		private Date lastRepricingDate;
		private Date nextRepricingDate;
		private String spreadOverBenchmark;
		private String finalRoi;
		private String capFloorRateOfInterest;
		private String assetStatus;
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

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getSchemeCode() {
			return schemeCode;
		}

		public void setSchemeCode(String schemeCode) {
			this.schemeCode = schemeCode;
		}

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String glCode) {
			this.glCode = glCode;
		}

		public String getGlDescription() {
			return glDescription;
		}

		public void setGlDescription(String glDescription) {
			this.glDescription = glDescription;
		}

		public String getTypeOfLoan() {
			return typeOfLoan;
		}

		public void setTypeOfLoan(String typeOfLoan) {
			this.typeOfLoan = typeOfLoan;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAccountCurrency() {
			return accountCurrency;
		}

		public void setAccountCurrency(String accountCurrency) {
			this.accountCurrency = accountCurrency;
		}

		public BigDecimal getOutstandingBalanceAcctCcy() {
			return outstandingBalanceAcctCcy;
		}

		public void setOutstandingBalanceAcctCcy(BigDecimal outstandingBalanceAcctCcy) {
			this.outstandingBalanceAcctCcy = outstandingBalanceAcctCcy;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
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

		public BigDecimal getTenorMonth() {
			return tenorMonth;
		}

		public void setTenorMonth(BigDecimal tenorMonth) {
			this.tenorMonth = tenorMonth;
		}

		public BigDecimal getEmiOfLoan() {
			return emiOfLoan;
		}

		public void setEmiOfLoan(BigDecimal emiOfLoan) {
			this.emiOfLoan = emiOfLoan;
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

		public String getFinalRoi() {
			return finalRoi;
		}

		public void setFinalRoi(String finalRoi) {
			this.finalRoi = finalRoi;
		}

		public String getCapFloorRateOfInterest() {
			return capFloorRateOfInterest;
		}

		public void setCapFloorRateOfInterest(String capFloorRateOfInterest) {
			this.capFloorRateOfInterest = capFloorRateOfInterest;
		}

		public String getAssetStatus() {
			return assetStatus;
		}

		public void setAssetStatus(String assetStatus) {
			this.assetStatus = assetStatus;
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
	public static class DBS10_FINCON_III_1B_Archival_Summary_Entity {
		private BigDecimal transSerialNo;
	    private String nameOfSfis;
	    private String nameOfCounterParty;
	    private String natureOfTrans;
	    private String orgIssue;
	    private Date dateOfTrnsBegin;
	    private Date dateOfTrnsEnd;
	    private String tenorOfTrans;
	    private BigDecimal amt;
	    private String returnVal;
	    private Date reportDate;
	    private BigDecimal reportVersion;
	    private String reportFrequency;
	    private String reportCode;
	    private String reportDesc;
	    private String entityFlg;
	    private String modifyFlg;
	    private String delFlg;
	    private Date reportResubDate;

	    // Getters and Setters
	    public BigDecimal getTransSerialNo() {
	        return transSerialNo;
	    }

	    public void setTransSerialNo(BigDecimal transSerialNo) {
	        this.transSerialNo = transSerialNo;
	    }

	    public String getNameOfSfis() {
	        return nameOfSfis;
	    }

	    public void setNameOfSfis(String nameOfSfis) {
	        this.nameOfSfis = nameOfSfis;
	    }

	    public String getNameOfCounterParty() {
	        return nameOfCounterParty;
	    }

	    public void setNameOfCounterParty(String nameOfCounterParty) {
	        this.nameOfCounterParty = nameOfCounterParty;
	    }

	    public String getNatureOfTrans() {
	        return natureOfTrans;
	    }

	    public void setNatureOfTrans(String natureOfTrans) {
	        this.natureOfTrans = natureOfTrans;
	    }

	    public String getOrgIssue() {
	        return orgIssue;
	    }

	    public void setOrgIssue(String orgIssue) {
	        this.orgIssue = orgIssue;
	    }

	    public Date getDateOfTrnsBegin() {
	        return dateOfTrnsBegin;
	    }

	    public void setDateOfTrnsBegin(Date dateOfTrnsBegin) {
	        this.dateOfTrnsBegin = dateOfTrnsBegin;
	    }

	    public Date getDateOfTrnsEnd() {
	        return dateOfTrnsEnd;
	    }

	    public void setDateOfTrnsEnd(Date dateOfTrnsEnd) {
	        this.dateOfTrnsEnd = dateOfTrnsEnd;
	    }

	    public String getTenorOfTrans() {
	        return tenorOfTrans;
	    }

	    public void setTenorOfTrans(String tenorOfTrans) {
	        this.tenorOfTrans = tenorOfTrans;
	    }

	    public BigDecimal getAmt() {
	        return amt;
	    }

	    public void setAmt(BigDecimal amt) {
	        this.amt = amt;
	    }

	    public String getReturnVal() {
	        return returnVal;
	    }

	    public void setReturnVal(String returnVal) {
	        this.returnVal = returnVal;
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

	    public Date getReportResubDate() {
	        return reportResubDate;
	    }

	    public void setReportResubDate(Date reportResubDate) {
	        this.reportResubDate = reportResubDate;
	    }
	}

	// Archival Detail Entity
	public static class BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity {
		private Long sno;
		private String customerId;
		private String accountNumber;
		private String schemeCode;
		private String glCode;
		private String glDescription;
		private String typeOfLoan;
		private String name;
		private String accountCurrency;
		private BigDecimal outstandingBalanceAcctCcy;
		private BigDecimal outstandingBalanceInr;
		private Date accountOpeningDate;
		private Date maturityDate;
		private BigDecimal tenorMonth;
		private BigDecimal emiOfLoan;
		private String floatingFixed;
		private String existingBenchmark;
		private String existingRepricingFrequency;
		private Date lastRepricingDate;
		private Date nextRepricingDate;
		private String spreadOverBenchmark;
		private String finalRoi;
		private String capFloorRateOfInterest;
		private String assetStatus;
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

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getSchemeCode() {
			return schemeCode;
		}

		public void setSchemeCode(String schemeCode) {
			this.schemeCode = schemeCode;
		}

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String glCode) {
			this.glCode = glCode;
		}

		public String getGlDescription() {
			return glDescription;
		}

		public void setGlDescription(String glDescription) {
			this.glDescription = glDescription;
		}

		public String getTypeOfLoan() {
			return typeOfLoan;
		}

		public void setTypeOfLoan(String typeOfLoan) {
			this.typeOfLoan = typeOfLoan;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAccountCurrency() {
			return accountCurrency;
		}

		public void setAccountCurrency(String accountCurrency) {
			this.accountCurrency = accountCurrency;
		}

		public BigDecimal getOutstandingBalanceAcctCcy() {
			return outstandingBalanceAcctCcy;
		}

		public void setOutstandingBalanceAcctCcy(BigDecimal outstandingBalanceAcctCcy) {
			this.outstandingBalanceAcctCcy = outstandingBalanceAcctCcy;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
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

		public BigDecimal getTenorMonth() {
			return tenorMonth;
		}

		public void setTenorMonth(BigDecimal tenorMonth) {
			this.tenorMonth = tenorMonth;
		}

		public BigDecimal getEmiOfLoan() {
			return emiOfLoan;
		}

		public void setEmiOfLoan(BigDecimal emiOfLoan) {
			this.emiOfLoan = emiOfLoan;
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

		public String getFinalRoi() {
			return finalRoi;
		}

		public void setFinalRoi(String finalRoi) {
			this.finalRoi = finalRoi;
		}

		public String getCapFloorRateOfInterest() {
			return capFloorRateOfInterest;
		}

		public void setCapFloorRateOfInterest(String capFloorRateOfInterest) {
			this.capFloorRateOfInterest = capFloorRateOfInterest;
		}

		public String getAssetStatus() {
			return assetStatus;
		}

		public void setAssetStatus(String assetStatus) {
			this.assetStatus = assetStatus;
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


	// ===========================================================
	// SAFE RESULTSET HELPERS
	// ===========================================================
	private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
		java.sql.ResultSetMetaData meta = rs.getMetaData();
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			if (columnName.equalsIgnoreCase(meta.getColumnLabel(i))
					|| columnName.equalsIgnoreCase(meta.getColumnName(i))) {
				return true;
			}
		}
		return false;
	}

	private String getStringSafe(ResultSet rs, String columnName) throws SQLException {
		return hasColumn(rs, columnName) ? rs.getString(columnName) : null;
	}

	private Date getDateSafe(ResultSet rs, String columnName) throws SQLException {
		return hasColumn(rs, columnName) ? rs.getDate(columnName) : null;
	}

	private BigDecimal getBigDecimalSafe(ResultSet rs, String columnName) throws SQLException {
		return hasColumn(rs, columnName) ? rs.getBigDecimal(columnName) : null;
	}

	// ===========================================================
	// ROW MAPPER CLASSES
	// ===========================================================

	// Normal Summary Row Mapper
	private class SummaryRowMapper implements RowMapper<BRRS_DBS10_FINCON_III_1B_Summary_Entity> {
		@Override
		public BRRS_DBS10_FINCON_III_1B_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			BRRS_DBS10_FINCON_III_1B_Summary_Entity entity =
					new BRRS_DBS10_FINCON_III_1B_Summary_Entity();

			/*
			 * IMPORTANT:
			 * The normal SUMMARYTABLE in the database does not necessarily contain
			 * every archival/resubmission column.  Do not call ResultSet.getXXX()
			 * directly for optional columns because Oracle throws
			 * "Invalid column name" when a column is absent.
			 */
			entity.setTransSerialNo(getBigDecimalSafe(rs, "TRANS_SERIAL_NO"));
			entity.setNameOfSfis(getStringSafe(rs, "NAME_OF_SFIS"));
			entity.setNameOfCounterParty(getStringSafe(rs, "NAME_OF_COUNTER_PARTY"));
			entity.setNatureOfTrans(getStringSafe(rs, "NATURE_OF_TRANS"));
			entity.setOrgIssue(getStringSafe(rs, "ORG_ISSUE"));
			entity.setDateOfTrnsBegin(getDateSafe(rs, "DATE_OF_TRNS_BEGIN"));
			entity.setDateOfTrnsEnd(getDateSafe(rs, "DATE_OF_TRNS_END"));
			entity.setTenorOfTrans(getStringSafe(rs, "TENOR_OF_TRANS"));
			entity.setAmt(getBigDecimalSafe(rs, "AMT"));
			entity.setReturnVal(getStringSafe(rs, "RETURN_VAL"));

			entity.setReportDate(getDateSafe(rs, "REPORT_DATE"));
			entity.setReportVersion(getBigDecimalSafe(rs, "REPORT_VERSION"));
			entity.setReportFrequency(getStringSafe(rs, "REPORT_FREQUENCY"));
			entity.setReportCode(getStringSafe(rs, "REPORT_CODE"));
			entity.setReportDesc(getStringSafe(rs, "REPORT_DESC"));
			entity.setEntityFlg(getStringSafe(rs, "ENTITY_FLG"));
			entity.setModifyFlg(getStringSafe(rs, "MODIFY_FLG"));
			entity.setDelFlg(getStringSafe(rs, "DEL_FLG"));

			// REPORT_RESUBDATE is intentionally NOT read from the normal table.
			// It belongs to archival/resubmission handling.
			return entity;
		}
	}

	// Normal Detail Row Mapper
	private class DetailRowMapper implements RowMapper<BRRS_DBS10_FINCON_III_1B_Detail_Entity> {
		@Override
		public BRRS_DBS10_FINCON_III_1B_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			BRRS_DBS10_FINCON_III_1B_Detail_Entity entity = new BRRS_DBS10_FINCON_III_1B_Detail_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setCustomerId(rs.getString("CUSTOMER_ID"));
			entity.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
			entity.setSchemeCode(rs.getString("SCHEME_CODE"));
			entity.setGlCode(rs.getString("GL_CODE"));
			entity.setGlDescription(rs.getString("GL_DESCRIPTION"));
			entity.setTypeOfLoan(rs.getString("TYPE_OF_LOAN"));
			entity.setName(rs.getString("NAME"));
			entity.setAccountCurrency(rs.getString("ACCOUNT_CURRENCY"));
			entity.setOutstandingBalanceAcctCcy(rs.getBigDecimal("OUTSTANDING_BALANCE_ACCT_CCY"));
			entity.setOutstandingBalanceInr(rs.getBigDecimal("OUTSTANDING_BALANCE_INR"));
			entity.setAccountOpeningDate(rs.getDate("ACCOUNT_OPENING_DATE"));
			entity.setMaturityDate(rs.getDate("MATURITY_DATE"));
			entity.setTenorMonth(rs.getBigDecimal("TENOR_MONTH"));
			entity.setEmiOfLoan(rs.getBigDecimal("EMI_OF_LOAN"));
			entity.setFloatingFixed(rs.getString("FLOATING_FIXED"));
			entity.setExistingBenchmark(rs.getString("EXISTING_BENCHMARK"));
			entity.setExistingRepricingFrequency(rs.getString("EXISTING_REPRICING_FREQUENCY"));
			entity.setLastRepricingDate(rs.getDate("LAST_REPRICING_DATE"));
			entity.setNextRepricingDate(rs.getDate("NEXT_REPRICING_DATE"));
			entity.setSpreadOverBenchmark(rs.getString("SPREAD_OVER_BENCHMARK"));
			entity.setFinalRoi(rs.getString("FINAL_ROI"));
			entity.setCapFloorRateOfInterest(rs.getString("CAP_FLOOR_RATE_OF_INTEREST"));
			entity.setAssetStatus(rs.getString("ASSET_STATUS"));
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

	// Archival Summary Row Mapper
	private class ArchivalSummaryRowMapper implements RowMapper<DBS10_FINCON_III_1B_Archival_Summary_Entity> {
		@Override
		public DBS10_FINCON_III_1B_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			DBS10_FINCON_III_1B_Archival_Summary_Entity entity = new DBS10_FINCON_III_1B_Archival_Summary_Entity();
			entity.setTransSerialNo(rs.getBigDecimal("TRANS_SERIAL_NO"));
			entity.setNameOfSfis(rs.getString("NAME_OF_SFIS"));
			entity.setNameOfCounterParty(rs.getString("NAME_OF_COUNTER_PARTY"));
			entity.setNatureOfTrans(rs.getString("NATURE_OF_TRANS"));
			entity.setOrgIssue(rs.getString("ORG_ISSUE"));
			entity.setDateOfTrnsBegin(rs.getDate("DATE_OF_TRNS_BEGIN"));
			entity.setDateOfTrnsEnd(rs.getDate("DATE_OF_TRNS_END"));
			entity.setTenorOfTrans(rs.getString("TENOR_OF_TRANS"));
			entity.setAmt(rs.getBigDecimal("AMT"));
			entity.setReturnVal(rs.getString("RETURN_VAL"));
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

	// Archival Detail Row Mapper
	private class ArchivalDetailRowMapper implements RowMapper<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> {
		@Override
		public BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity entity = new BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity();
			entity.setSno(rs.getLong("SNO"));
			entity.setCustomerId(rs.getString("CUSTOMER_ID"));
			entity.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
			entity.setSchemeCode(rs.getString("SCHEME_CODE"));
			entity.setGlCode(rs.getString("GL_CODE"));
			entity.setGlDescription(rs.getString("GL_DESCRIPTION"));
			entity.setTypeOfLoan(rs.getString("TYPE_OF_LOAN"));
			entity.setName(rs.getString("NAME"));
			entity.setAccountCurrency(rs.getString("ACCOUNT_CURRENCY"));
			entity.setOutstandingBalanceAcctCcy(rs.getBigDecimal("OUTSTANDING_BALANCE_ACCT_CCY"));
			entity.setOutstandingBalanceInr(rs.getBigDecimal("OUTSTANDING_BALANCE_INR"));
			entity.setAccountOpeningDate(rs.getDate("ACCOUNT_OPENING_DATE"));
			entity.setMaturityDate(rs.getDate("MATURITY_DATE"));
			entity.setTenorMonth(rs.getBigDecimal("TENOR_MONTH"));
			entity.setEmiOfLoan(rs.getBigDecimal("EMI_OF_LOAN"));
			entity.setFloatingFixed(rs.getString("FLOATING_FIXED"));
			entity.setExistingBenchmark(rs.getString("EXISTING_BENCHMARK"));
			entity.setExistingRepricingFrequency(rs.getString("EXISTING_REPRICING_FREQUENCY"));
			entity.setLastRepricingDate(rs.getDate("LAST_REPRICING_DATE"));
			entity.setNextRepricingDate(rs.getDate("NEXT_REPRICING_DATE"));
			entity.setSpreadOverBenchmark(rs.getString("SPREAD_OVER_BENCHMARK"));
			entity.setFinalRoi(rs.getString("FINAL_ROI"));
			entity.setCapFloorRateOfInterest(rs.getString("CAP_FLOOR_RATE_OF_INTEREST"));
			entity.setAssetStatus(rs.getString("ASSET_STATUS"));
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

	// ===========================================================
	// SERVICE METHODS
	// ===========================================================

	public ModelAndView getDBS10_FINCON_III_1BView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("DBS10_FINCON_III_1B View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================
		if ("ARCHIVAL".equals(type) && version != null) {
			try {
				Date dt = dateformat.parse(todate);

				List<DBS10_FINCON_III_1B_Archival_Summary_Entity> archivalSummary = getArchivalDataByDateAndVersion(dt,
						version);

				System.out.println("Archival Summary size = " + archivalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> archivalDetail = getArchivalDetailDataByDateAndVersion(
							dt, version);
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
		// RESUB MODE (Uses Archival Tables)
		// =====================================================
		else if ("RESUB".equals(type) && version != null) {
			try {
				Date dt = dateformat.parse(todate);

				List<DBS10_FINCON_III_1B_Archival_Summary_Entity> resubSummary = getResubDataByDateAndVersion(dt, version);

				System.out.println("Resub Summary size = " + resubSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> resubDetail = getResubDetailDataByDateAndVersion(dt,
							version);
					mv.addObject("reportdetails", resubDetail);
					mv.addObject("displaymode", "resubDetail");
					System.out.println("Resub Detail size = " + resubDetail.size());
				} else {
					mv.addObject("displaymode", "resubSummary");
				}

				mv.addObject("reportsummary", resubSummary);
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
				System.out.println("Normal report date = " + formattedDate);

				List<BRRS_DBS10_FINCON_III_1B_Summary_Entity> normalSummary = jdbcTemplate.query(
						"SELECT * FROM BRRS_DBS10_FINCON_III_1B_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
						new Object[] { formattedDate }, new SummaryRowMapper());

				System.out.println("Normal Summary size = " + normalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					/*
					 * The DBS10 FINCON III (1)(b) frontend transaction report
					 * uses the X010-X090 fields from the SUMMARYTABLE.
					 * Do not map the unrelated DETAILTABLE here; its columns
					 * are different (SNO, CUSTOMER_ID, ACCOUNT_NUMBER, etc.).
					 */
					mv.addObject("reportdetails", normalSummary);
					mv.addObject("displaymode", "Details");
					System.out.println("Transaction detail rows = " + normalSummary.size());
				} else {
					mv.addObject("displaymode", "summary");
				}

				mv.addObject("reportsummary", normalSummary);
				mv.addObject("report_date", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();

				// Keep the HTML report shell/header visible even if a database
				// column or row-mapping problem occurs.
				mv.addObject("reportsummary",
						new java.util.ArrayList<BRRS_DBS10_FINCON_III_1B_Summary_Entity>());

				if ("detail".equalsIgnoreCase(dtltype)) {
					mv.addObject("displaymode", "Details");
				} else {
					mv.addObject("displaymode", "summary");
				}
			}
		}

		mv.setViewName("BRRS/DBS10_FINCON_III_1B");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// ===========================================================

	// ===========================================================

	// Helper: Save normal data to ARCHIVAL SUMMARY table
	// NOTE: Previously this also duplicated the same record into the ARCHIVAL
	// DETAIL table because, under the old IRRBB_ADVANCES schema, Summary and
	// Detail shared identical columns. Now that BRRS_DBS10_FINCON_III_1B_Detail_Entity
	// has a different (loan/account level) column set than the Summary entity,
	// the detail archival must be done separately via saveDetailToArchivalFromNormal(...)
	// using the actual Detail_Entity record(s) for the report date.
	private void saveToArchivalFromNormal(BRRS_DBS10_FINCON_III_1B_Summary_Entity oldRecord, BigDecimal version) {
		String archivalSummarySql = "INSERT INTO BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE "
				+ "(TRANS_SERIAL_NO, NAME_OF_SFIS, NAME_OF_COUNTER_PARTY, NATURE_OF_TRANS, ORG_ISSUE, "
				+ "DATE_OF_TRNS_BEGIN, DATE_OF_TRNS_END, TENOR_OF_TRANS, AMT, RETURN_VAL, REPORT_DATE, "
				+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, "
				+ "REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			jdbcTemplate.update(archivalSummarySql, oldRecord.getTransSerialNo(), oldRecord.getNameOfSfis(),
					oldRecord.getNameOfCounterParty(), oldRecord.getNatureOfTrans(), oldRecord.getOrgIssue(),
					oldRecord.getDateOfTrnsBegin(), oldRecord.getDateOfTrnsEnd(), oldRecord.getTenorOfTrans(),
					oldRecord.getAmt(), oldRecord.getReturnVal(), oldRecord.getReportDate(), version,
					oldRecord.getReportFrequency(), oldRecord.getReportCode(), oldRecord.getReportDesc(),
					oldRecord.getEntityFlg(), oldRecord.getModifyFlg(), oldRecord.getDelFlg(),
					oldRecord.getReportResubDate());
		} catch (Exception e) {
			logger.error("Error saving to ARCHIVAL SUMMARY: {}", e.getMessage());
		}
	}

	// Helper: Save normal DETAIL data to ARCHIVAL DETAIL table
	private void saveDetailToArchivalFromNormal(BRRS_DBS10_FINCON_III_1B_Detail_Entity oldRecord, BigDecimal version) {
		String archivalDetailSql = "INSERT INTO BRRS_DBS10_FINCON_III_1B_ARCHIVAL_DETAILTABLE "
				+ "(SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
				+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
				+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
				+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
				+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
				+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			jdbcTemplate.update(archivalDetailSql, oldRecord.getSno(), oldRecord.getCustomerId(),
					oldRecord.getAccountNumber(), oldRecord.getSchemeCode(), oldRecord.getGlCode(),
					oldRecord.getGlDescription(), oldRecord.getTypeOfLoan(), oldRecord.getName(),
					oldRecord.getAccountCurrency(), oldRecord.getOutstandingBalanceAcctCcy(),
					oldRecord.getOutstandingBalanceInr(), oldRecord.getAccountOpeningDate(),
					oldRecord.getMaturityDate(), oldRecord.getTenorMonth(), oldRecord.getEmiOfLoan(),
					oldRecord.getFloatingFixed(), oldRecord.getExistingBenchmark(),
					oldRecord.getExistingRepricingFrequency(), oldRecord.getLastRepricingDate(),
					oldRecord.getNextRepricingDate(), oldRecord.getSpreadOverBenchmark(), oldRecord.getFinalRoi(),
					oldRecord.getCapFloorRateOfInterest(), oldRecord.getAssetStatus(), oldRecord.getReportDate(),
					version, oldRecord.getReportFrequency(), oldRecord.getReportCode(), oldRecord.getReportDesc(),
					oldRecord.getEntityFlg(), oldRecord.getModifyFlg(), oldRecord.getDelFlg());
		} catch (Exception e) {
			logger.error("Error saving to ARCHIVAL DETAIL: {}", e.getMessage());
		}
	}

	// Get next version for archival
	private BigDecimal getNextArchivalVersion(String reportDate) {
		try {
			String sql = "SELECT COALESCE(MAX(REPORT_VERSION), 0) + 1 FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
			BigDecimal nextVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return nextVersion != null ? nextVersion : BigDecimal.ONE;
		} catch (Exception e) {
			return BigDecimal.ONE;
		}
	}

	// ===========================================================
	// RESUB SUMMARY ADD (Uses Archival Table)
	// ===========================================================

	public void saveResubIrradv(DBS10_FINCON_III_1B_Archival_Summary_Entity summary) {
		BigDecimal nextTransSerialNo = jdbcTemplate.queryForObject(
				"SELECT BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE_TRANS_SERIAL_NO_SEQ.NEXTVAL FROM DUAL",
				BigDecimal.class);
		summary.setTransSerialNo(nextTransSerialNo);

		String archivalSummarySql = "INSERT INTO BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE (TRANS_SERIAL_NO, "
				+ "NAME_OF_SFIS, NAME_OF_COUNTER_PARTY, NATURE_OF_TRANS, ORG_ISSUE, DATE_OF_TRNS_BEGIN, "
				+ "DATE_OF_TRNS_END, TENOR_OF_TRANS, AMT, RETURN_VAL, REPORT_DATE, REPORT_VERSION, "
				+ "REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(archivalSummarySql, summary.getTransSerialNo(), summary.getNameOfSfis(),
				summary.getNameOfCounterParty(), summary.getNatureOfTrans(), summary.getOrgIssue(),
				summary.getDateOfTrnsBegin(), summary.getDateOfTrnsEnd(), summary.getTenorOfTrans(),
				summary.getAmt(), summary.getReturnVal(), summary.getReportDate(), summary.getReportVersion(),
				summary.getReportFrequency(), summary.getReportCode(), summary.getReportDesc(),
				summary.getEntityFlg(), summary.getModifyFlg(), summary.getDelFlg(), summary.getReportResubDate());
	}

	// Overloaded method - accepts Summary_Entity from controller
	public void saveResubIrradv(BRRS_DBS10_FINCON_III_1B_Summary_Entity summary) {
		DBS10_FINCON_III_1B_Archival_Summary_Entity resubEntity = new DBS10_FINCON_III_1B_Archival_Summary_Entity();
		resubEntity.setNameOfSfis(summary.getNameOfSfis());
		resubEntity.setNameOfCounterParty(summary.getNameOfCounterParty());
		resubEntity.setNatureOfTrans(summary.getNatureOfTrans());
		resubEntity.setOrgIssue(summary.getOrgIssue());
		resubEntity.setDateOfTrnsBegin(summary.getDateOfTrnsBegin());
		resubEntity.setDateOfTrnsEnd(summary.getDateOfTrnsEnd());
		resubEntity.setTenorOfTrans(summary.getTenorOfTrans());
		resubEntity.setAmt(summary.getAmt());
		resubEntity.setReturnVal(summary.getReturnVal());
		resubEntity.setReportDate(summary.getReportDate());
		resubEntity.setReportVersion(summary.getReportVersion());
		resubEntity.setReportFrequency(summary.getReportFrequency());
		resubEntity.setReportCode(summary.getReportCode());
		resubEntity.setReportDesc(summary.getReportDesc());
		resubEntity.setEntityFlg(summary.getEntityFlg());
		resubEntity.setModifyFlg(summary.getModifyFlg());
		resubEntity.setDelFlg(summary.getDelFlg());
		resubEntity.setReportResubDate(summary.getReportResubDate());

		saveResubIrradv(resubEntity);
	}

	// Helper: Save resub data as new version in ARCHIVAL SUMMARY table
	private void saveResubAsNewVersion(DBS10_FINCON_III_1B_Archival_Summary_Entity oldRecord, BigDecimal newVersion) {
		BigDecimal nextTransSerialNo = jdbcTemplate.queryForObject(
				"SELECT BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE_TRANS_SERIAL_NO_SEQ.NEXTVAL FROM DUAL",
				BigDecimal.class);

		String archivalSummarySql = "INSERT INTO BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE (TRANS_SERIAL_NO, "
				+ "NAME_OF_SFIS, NAME_OF_COUNTER_PARTY, NATURE_OF_TRANS, ORG_ISSUE, DATE_OF_TRNS_BEGIN, "
				+ "DATE_OF_TRNS_END, TENOR_OF_TRANS, AMT, RETURN_VAL, REPORT_DATE, REPORT_VERSION, "
				+ "REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(archivalSummarySql, nextTransSerialNo, oldRecord.getNameOfSfis(),
				oldRecord.getNameOfCounterParty(), oldRecord.getNatureOfTrans(), oldRecord.getOrgIssue(),
				oldRecord.getDateOfTrnsBegin(), oldRecord.getDateOfTrnsEnd(), oldRecord.getTenorOfTrans(),
				oldRecord.getAmt(), oldRecord.getReturnVal(), oldRecord.getReportDate(), newVersion,
				oldRecord.getReportFrequency(), oldRecord.getReportCode(), oldRecord.getReportDesc(),
				oldRecord.getEntityFlg(), oldRecord.getModifyFlg(), oldRecord.getDelFlg(),
				oldRecord.getReportResubDate());
	}

	// ===========================================================
	// ADDITIONAL METHODS FOR ARCHIVAL & RESUB
	// ===========================================================

	// Get Archival Summary Data
	public List<DBS10_FINCON_III_1B_Archival_Summary_Entity> getArchivalData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalSummaryRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalSummaryRowMapper());
	}

	// Get Archival Detail Data
	public List<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> getArchivalDetailData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalDetailRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalDetailRowMapper());
	}

	// Get Resub Summary Data (Uses Archival Table)
	public List<DBS10_FINCON_III_1B_Archival_Summary_Entity> getResubData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalSummaryRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalSummaryRowMapper());
	}

	// Get Resub Detail Data (Uses Archival Table)
	public List<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> getResubDetailData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalDetailRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalDetailRowMapper());
	}

	// Get All Versions for archival
	public List<BigDecimal> getArchivalVersions(String reportDate) {
		String sql = "SELECT DISTINCT REPORT_VERSION FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY') ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.queryForList(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// Get All Versions for resub
	public List<BigDecimal> getResubVersions(String reportDate) {
		String sql = "SELECT DISTINCT REPORT_VERSION FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY') ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.queryForList(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// Get next version number for resub
	public BigDecimal getNextResubVersion(String reportDate) {
		try {
			String sql = "SELECT COALESCE(MAX(REPORT_VERSION), 0) + 1 FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
			BigDecimal nextVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return nextVersion != null ? nextVersion : BigDecimal.ONE;
		} catch (Exception e) {
			return BigDecimal.ONE;
		}
	}

	// ===========================================================
	// GET REPORT_DATE + REPORT_VERSION for ARCHIVAL
	// ===========================================================
	public List<Object[]> getDBS10_FINCON_III_1BArchival() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE " + "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// ===========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION (SUMMARY)
	// ===========================================================
	public List<DBS10_FINCON_III_1B_Archival_Summary_Entity> getArchivalDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION (DETAIL)
	// ===========================================================
	public List<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	// ===========================================================
	// GET RESUB FULL DATA BY DATE + VERSION (SUMMARY)
	// ===========================================================
	public List<DBS10_FINCON_III_1B_Archival_Summary_Entity> getResubDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// GET RESUB FULL DATA BY DATE + VERSION (DETAIL)
	// ===========================================================
	public List<BRRS_DBS10_FINCON_III_1B_Archival_Detail_Entity> getResubDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	// ===========================================================
	// GET ALL WITH VERSION (for archival listing)
	// ===========================================================
	public List<DBS10_FINCON_III_1B_Archival_Summary_Entity> getAllArchivalWithVersion() {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// GET ALL RESUB WITH VERSION (for resub listing)
	// ===========================================================
	public List<DBS10_FINCON_III_1B_Archival_Summary_Entity> getAllResubWithVersion() {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// FIND MAX VERSION BY DATE (for archival)
	// ===========================================================
	public BigDecimal findMaxArchivalVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// ===========================================================
	// FIND MAX VERSION BY DATE (for resub)
	// ===========================================================
	public BigDecimal findMaxResubVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// ===========================================================
	// GET ARCHIVAL LIST (for dropdown)
	// ===========================================================
	public List<Object[]> getIRRBB_ADVANCESArchivalList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// ===========================================================
	// GET RESUB LIST (for dropdown)
	// ===========================================================
	public List<Object[]> getIRRBB_ADVANCESResubList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE " + "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// ===========================================================
	// EXCEL DOWNLOAD METHOD
	// ===========================================================

	public byte[] getDBS10_FINCON_III_1BExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		Date reportDate = dateformat.parse(todate);
		String formattedDate = dateformat.format(reportDate);

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			List<DBS10_FINCON_III_1B_Archival_Summary_Entity> dataList = getArchivalData(formattedDate, version);
			return generateExcelFromArchivalData(dataList, filename, type);
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			List<DBS10_FINCON_III_1B_Archival_Summary_Entity> dataList = getResubData(formattedDate, version);
			return generateExcelFromResubData(dataList, filename, type);
		} else {
			List<BRRS_DBS10_FINCON_III_1B_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_DBS10_FINCON_III_1B_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
					new Object[] { formattedDate }, new SummaryRowMapper());
			return generateExcelFromNormalData(dataList, filename);
		}
	}

	private byte[] generateExcelFromNormalData(List<BRRS_DBS10_FINCON_III_1B_Summary_Entity> dataList, String filename)
			throws Exception {
		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No data found for IRRBB_ADVANCES report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found : " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

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

			DataFormat dataFormat = workbook.createDataFormat();
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setFont(font);
			dateStyle.setDataFormat(dataFormat.getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			int rowIndex = 2;

			for (BRRS_DBS10_FINCON_III_1B_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getTransSerialNo(), numberStyle);
				setCellValue(row, 1, data.getNameOfSfis(), textStyle);
				setCellValue(row, 2, data.getNameOfCounterParty(), textStyle);
				setCellValue(row, 3, data.getNatureOfTrans(), textStyle);
				setCellValue(row, 4, data.getOrgIssue(), textStyle);
				setCellValue(row, 5, data.getDateOfTrnsBegin(), dateStyle);
				setCellValue(row, 6, data.getDateOfTrnsEnd(), dateStyle);
				setCellValue(row, 7, data.getTenorOfTrans(), textStyle);
				setCellValue(row, 8, data.getAmt(), numberStyle);
				setCellValue(row, 9, data.getReturnVal(), textStyle);
			}

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromArchivalData(List<DBS10_FINCON_III_1B_Archival_Summary_Entity> dataList, String filename,
			String type) throws Exception {
		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No archival data found for IRRBB_ADVANCES report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found : " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

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

			DataFormat dataFormat = workbook.createDataFormat();
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setFont(font);
			dateStyle.setDataFormat(dataFormat.getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			int rowIndex = 2;

			for (DBS10_FINCON_III_1B_Archival_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getTransSerialNo(), numberStyle);
				setCellValue(row, 1, data.getNameOfSfis(), textStyle);
				setCellValue(row, 2, data.getNameOfCounterParty(), textStyle);
				setCellValue(row, 3, data.getNatureOfTrans(), textStyle);
				setCellValue(row, 4, data.getOrgIssue(), textStyle);
				setCellValue(row, 5, data.getDateOfTrnsBegin(), dateStyle);
				setCellValue(row, 6, data.getDateOfTrnsEnd(), dateStyle);
				setCellValue(row, 7, data.getTenorOfTrans(), textStyle);
				setCellValue(row, 8, data.getAmt(), numberStyle);
				setCellValue(row, 9, data.getReturnVal(), textStyle);
				setCellValue(row, 10, data.getReportVersion(), numberStyle);
				setCellValue(row, 11, data.getReportResubDate(), dateStyle);
			}

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromResubData(List<DBS10_FINCON_III_1B_Archival_Summary_Entity> dataList, String filename,
			String type) throws Exception {
		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No resub data found for IRRBB_ADVANCES report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found : " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

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

			DataFormat dataFormat = workbook.createDataFormat();
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setFont(font);
			dateStyle.setDataFormat(dataFormat.getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			int rowIndex = 2;

			for (DBS10_FINCON_III_1B_Archival_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getTransSerialNo(), numberStyle);
				setCellValue(row, 1, data.getNameOfSfis(), textStyle);
				setCellValue(row, 2, data.getNameOfCounterParty(), textStyle);
				setCellValue(row, 3, data.getNatureOfTrans(), textStyle);
				setCellValue(row, 4, data.getOrgIssue(), textStyle);
				setCellValue(row, 5, data.getDateOfTrnsBegin(), dateStyle);
				setCellValue(row, 6, data.getDateOfTrnsEnd(), dateStyle);
				setCellValue(row, 7, data.getTenorOfTrans(), textStyle);
				setCellValue(row, 8, data.getAmt(), numberStyle);
				setCellValue(row, 9, data.getReturnVal(), textStyle);
				setCellValue(row, 10, data.getReportVersion(), numberStyle);
				setCellValue(row, 11, data.getReportResubDate(), dateStyle);
			}

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private Object formatIdValue(BigDecimal value) {
		if (value == null)
			return null;
		if (value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0) {
			return value.longValue();
		}
		return value;
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

	// ===========================================================
	// INLINE UPDATE ROW DTO & BATCH UPDATE / DELETE METHODS
	// ===========================================================

	public static class IRRBB_ADVANCES_Update_Row {
		private Long sno;
		private BigDecimal outstandingBalanceInr;
		private String floatingFixed;
		private String existingBenchmark;
		private String existingRepricingFrequency;
		private String lastRepricingDate;
		private String nextRepricingDate;
		private String spreadOverBenchmark;
		private String capFloorRateOfInterest;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public BigDecimal getOutstandingBalanceInr() {
			return outstandingBalanceInr;
		}

		public void setOutstandingBalanceInr(BigDecimal outstandingBalanceInr) {
			this.outstandingBalanceInr = outstandingBalanceInr;
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

		public String getLastRepricingDate() {
			return lastRepricingDate;
		}

		public void setLastRepricingDate(String lastRepricingDate) {
			this.lastRepricingDate = lastRepricingDate;
		}

		public String getNextRepricingDate() {
			return nextRepricingDate;
		}

		public void setNextRepricingDate(String nextRepricingDate) {
			this.nextRepricingDate = nextRepricingDate;
		}

		public String getSpreadOverBenchmark() {
			return spreadOverBenchmark;
		}

		public void setSpreadOverBenchmark(String spreadOverBenchmark) {
			this.spreadOverBenchmark = spreadOverBenchmark;
		}

		public String getCapFloorRateOfInterest() {
			return capFloorRateOfInterest;
		}

		public void setCapFloorRateOfInterest(String capFloorRateOfInterest) {
			this.capFloorRateOfInterest = capFloorRateOfInterest;
		}
	}

	public void updateAllIrradvRows(List<IRRBB_ADVANCES_Update_Row> rows, String type) {
		if (rows == null || rows.isEmpty())
			return;

		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

		String summaryTable = "RESUB".equalsIgnoreCase(type) ? "BRRS_DBS10_FINCON_III_1B_ARCHIVAL_SUMMARYTABLE"
				: "BRRS_DBS10_FINCON_III_1B_SUMMARYTABLE";
		String detailTable = "RESUB".equalsIgnoreCase(type) ? "BRRS_DBS10_FINCON_III_1B_ARCHIVAL_DETAILTABLE"
				: "BRRS_DBS10_FINCON_III_1B_DETAILTABLE";

		String summarySql = "UPDATE " + summaryTable + " SET " + "FLOATING_FIXED = ?, " + "EXISTING_BENCHMARK = ?, "
				+ "EXISTING_REPRICING_FREQUENCY = ?, " + "LAST_REPRICING_DATE = ?, " + "NEXT_REPRICING_DATE = ?, "
				+ "SPREAD_OVER_BENCHMARK = ?, " + "CAP_FLOOR_RATE_OF_INTEREST = ? " + "WHERE SNO = ?";

		String detailSql = "UPDATE " + detailTable + " SET " + "FLOATING_FIXED = ?, " + "EXISTING_BENCHMARK = ?, "
				+ "EXISTING_REPRICING_FREQUENCY = ?, " + "LAST_REPRICING_DATE = ?, " + "NEXT_REPRICING_DATE = ?, "
				+ "SPREAD_OVER_BENCHMARK = ?, " + "CAP_FLOOR_RATE_OF_INTEREST = ? " + "WHERE SNO = ?";

		for (IRRBB_ADVANCES_Update_Row row : rows) {
			if (row.getSno() == null)
				continue;

			Date lastRepriceDate = parseDate(row.getLastRepricingDate(), sdf1, sdf2);
			Date nextRepriceDate = parseDate(row.getNextRepricingDate(), sdf1, sdf2);

			jdbcTemplate.update(summarySql, row.getFloatingFixed(), row.getExistingBenchmark(),
					row.getExistingRepricingFrequency(), lastRepriceDate, nextRepriceDate, row.getSpreadOverBenchmark(),
					row.getCapFloorRateOfInterest(), row.getSno());

			int detailRowsUpdated = jdbcTemplate.update(detailSql, row.getFloatingFixed(), row.getExistingBenchmark(),
					row.getExistingRepricingFrequency(), lastRepriceDate, nextRepriceDate, row.getSpreadOverBenchmark(),
					row.getCapFloorRateOfInterest(), row.getSno());

			if (detailRowsUpdated == 0) {
				String detailSqlFallback = "UPDATE " + detailTable + " SET " + "FLOATING_FIXED = ?, "
						+ "EXISTING_BENCHMARK = ?, " + "EXISTING_REPRICING_FREQUENCY = ?, "
						+ "LAST_REPRICING_DATE = ?, " + "NEXT_REPRICING_DATE = ?, " + "SPREAD_OVER_BENCHMARK = ?, "
						+ "CAP_FLOOR_RATE_OF_INTEREST = ? " + "WHERE REPORT_DATE = (SELECT REPORT_DATE FROM "
						+ summaryTable + " WHERE SNO = ?) " + "AND (ACCOUNT_NUMBER = (SELECT ACCOUNT_NUMBER FROM "
						+ summaryTable + " WHERE SNO = ?) " + " OR CUSTOMER_ID = (SELECT CUSTOMER_ID FROM "
						+ summaryTable + " WHERE SNO = ?))";

				try {
					jdbcTemplate.update(detailSqlFallback, row.getFloatingFixed(), row.getExistingBenchmark(),
							row.getExistingRepricingFrequency(), lastRepriceDate, nextRepriceDate,
							row.getSpreadOverBenchmark(), row.getCapFloorRateOfInterest(), row.getSno(), row.getSno(),
							row.getSno());
				} catch (Exception ex) {
					logger.warn("Detail table fallback update warning for SNO {}: {}", row.getSno(), ex.getMessage());
				}
			}
		}

		// Fetch REPORT_DATE to pass to procedure if required
		String reportDateStr = null;
		try {
			reportDateStr = jdbcTemplate.queryForObject(
					"SELECT TO_CHAR(REPORT_DATE, 'DD-MON-YYYY') FROM " + summaryTable + " WHERE SNO = ?", String.class,
					rows.get(0).getSno());
		} catch (Exception e) {
			logger.warn("Could not query REPORT_DATE for SNO {}: {}", rows.get(0).getSno(), e.getMessage());
		}

		// Sequentially execute procedures: FIRST BRRS_IRRBB_ADV_DETAIL_PROCEDURE, THEN
		// BRRS_IRRBB_ADV_SUMMARY_PROCEDURE
		executeIrradvProcedures(reportDateStr);
	}

	private void executeIrradvProcedures(String reportDateStr) {
		// 1. First run BRRS_IRRBB_ADV_DETAIL_PROCEDURE
		try {
			logger.info("Executing BRRS_IRRBB_ADV_DETAIL_PROCEDURE with date: {}", reportDateStr);
			if (reportDateStr != null && !reportDateStr.trim().isEmpty()) {
				try {
					jdbcTemplate.update("BEGIN BRRS_IRRBB_ADV_DETAIL_PROCEDURE(?); END;", reportDateStr);
				} catch (Exception ex) {
					jdbcTemplate.update("BEGIN BRRS_IRRBB_ADV_DETAIL_PROCEDURE; END;");
				}
			} else {
				jdbcTemplate.update("BEGIN BRRS_IRRBB_ADV_DETAIL_PROCEDURE; END;");
			}
			logger.info("BRRS_IRRBB_ADV_DETAIL_PROCEDURE executed successfully.");
		} catch (Exception e) {
			logger.error("Error executing BRRS_IRRBB_ADV_DETAIL_PROCEDURE", e);
			throw new RuntimeException("Detail Procedure Execution Failed: " + e.getMessage(), e);
		}

		// 2. Then run BRRS_IRRBB_ADV_SUMMARY_PROCEDURE
		try {
			logger.info("Executing BRRS_IRRBB_ADV_SUMMARY_PROCEDURE with date: {}", reportDateStr);
			if (reportDateStr != null && !reportDateStr.trim().isEmpty()) {
				try {
					jdbcTemplate.update("BEGIN BRRS_IRRBB_ADV_SUMMARY_PROCEDURE(?); END;", reportDateStr);
				} catch (Exception ex) {
					jdbcTemplate.update("BEGIN BRRS_IRRBB_ADV_SUMMARY_PROCEDURE; END;");
				}
			} else {
				jdbcTemplate.update("BEGIN BRRS_IRRBB_ADV_SUMMARY_PROCEDURE; END;");
			}
			logger.info("BRRS_IRRBB_ADV_SUMMARY_PROCEDURE executed successfully.");
		} catch (Exception e) {
			logger.error("Error executing BRRS_IRRBB_ADV_SUMMARY_PROCEDURE", e);
			throw new RuntimeException("Summary Procedure Execution Failed: " + e.getMessage(), e);
		}
	}

	private Date parseDate(String str, SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
		if (str == null || str.trim().isEmpty())
			return null;
		String trimmed = str.trim();
		try {
			return sdf1.parse(trimmed);
		} catch (ParseException e) {
			try {
				return sdf2.parse(trimmed);
			} catch (ParseException ex) {
				logger.warn("Date parse error for string {}: {}", str, ex.getMessage());
				return null;
			}
		}
	}

}