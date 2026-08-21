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

@Service
@Component
public class BRRS_IRRBB_ADVANCES_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_IRRBB_ADVANCES_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ===========================================================
	// INNER ENTITY CLASSES
	// ===========================================================

	// Summary Entity
	public static class IRRBB_ADVANCES_Summary_Entity {
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

		// Getters and Setters
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

	// Detail Entity
	public static class IRRBB_ADVANCES_Detail_Entity {
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
	public static class IRRBB_ADVANCES_Archival_Summary_Entity {
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
		private Date reportResubdate;

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

		public Date getReportResubdate() {
			return reportResubdate;
		}

		public void setReportResubdate(Date reportResubdate) {
			this.reportResubdate = reportResubdate;
		}
	}

	// Archival Detail Entity
	public static class IRRBB_ADVANCES_Archival_Detail_Entity {
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
		private Date reportResubdate;

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

	// Normal Summary Row Mapper
	private class SummaryRowMapper implements RowMapper<IRRBB_ADVANCES_Summary_Entity> {
		@Override
		public IRRBB_ADVANCES_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			IRRBB_ADVANCES_Summary_Entity entity = new IRRBB_ADVANCES_Summary_Entity();
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

	// Normal Detail Row Mapper
	private class DetailRowMapper implements RowMapper<IRRBB_ADVANCES_Detail_Entity> {
		@Override
		public IRRBB_ADVANCES_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			IRRBB_ADVANCES_Detail_Entity entity = new IRRBB_ADVANCES_Detail_Entity();
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
	private class ArchivalSummaryRowMapper implements RowMapper<IRRBB_ADVANCES_Archival_Summary_Entity> {
		@Override
		public IRRBB_ADVANCES_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			IRRBB_ADVANCES_Archival_Summary_Entity entity = new IRRBB_ADVANCES_Archival_Summary_Entity();
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
			entity.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return entity;
		}
	}

	// Archival Detail Row Mapper
	private class ArchivalDetailRowMapper implements RowMapper<IRRBB_ADVANCES_Archival_Detail_Entity> {
		@Override
		public IRRBB_ADVANCES_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			IRRBB_ADVANCES_Archival_Detail_Entity entity = new IRRBB_ADVANCES_Archival_Detail_Entity();
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
			entity.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return entity;
		}
	}

	// ===========================================================
	// SERVICE METHODS
	// ===========================================================

	public ModelAndView getBRRS_IRRBB_ADVANCES_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("IRRBB_ADVANCES View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// =====================================================
		// ARCHIVAL MODE
		// =====================================================
		if ("ARCHIVAL".equals(type) && version != null) {
			try {
				Date dt = dateformat.parse(todate);

				List<IRRBB_ADVANCES_Archival_Summary_Entity> archivalSummary = getArchivalDataByDateAndVersion(dt,
						version);

				System.out.println("Archival Summary size = " + archivalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<IRRBB_ADVANCES_Archival_Detail_Entity> archivalDetail = getArchivalDetailDataByDateAndVersion(
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

				List<IRRBB_ADVANCES_Archival_Summary_Entity> resubSummary = getResubDataByDateAndVersion(dt, version);

				System.out.println("Resub Summary size = " + resubSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<IRRBB_ADVANCES_Archival_Detail_Entity> resubDetail = getResubDetailDataByDateAndVersion(dt,
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

				List<IRRBB_ADVANCES_Summary_Entity> normalSummary = jdbcTemplate.query(
						"SELECT * FROM BRRS_IRRBB_ADV_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
						new Object[] { formattedDate }, new SummaryRowMapper());

				System.out.println("Normal Summary size = " + normalSummary.size());

				if ("detail".equalsIgnoreCase(dtltype)) {
					List<IRRBB_ADVANCES_Detail_Entity> normalDetail = jdbcTemplate.query(
							"SELECT * FROM BRRS_IRRBB_ADV_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
							new Object[] { formattedDate }, new DetailRowMapper());
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

		try {
			Date dtForMax = dateformat.parse(todate);
			if (dtForMax != null) {
				BigDecimal maxVer = findMaxResubVersion(dtForMax);
				mv.addObject("maxVersion", maxVer);
			}
		} catch (Exception ex) {
			logger.warn("Could not calculate maxVersion for date {}: {}", todate, ex.getMessage());
		}

		mv.setViewName("BRRS/IRRBB_ADVANCES");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);
		mv.addObject("version", version);
		mv.addObject("type", type);

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// ===========================================================

	// ===========================================================

	// Helper: Save normal data to ARCHIVAL tables
	private void saveToArchivalFromNormal(IRRBB_ADVANCES_Summary_Entity oldRecord, BigDecimal version) {
		String archivalSummarySql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
				+ "(SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
				+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
				+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
				+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
				+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
				+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			jdbcTemplate.update(archivalSummarySql, oldRecord.getSno(), oldRecord.getCustomerId(),
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
			logger.error("Error saving to ARCHIVAL SUMMARY: {}", e.getMessage());
		}

		String archivalDetailSql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE "
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
			String sql = "SELECT COALESCE(MAX(REPORT_VERSION), 0) + 1 FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
			BigDecimal nextVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return nextVersion != null ? nextVersion : BigDecimal.ONE;
		} catch (Exception e) {
			return BigDecimal.ONE;
		}
	}

	// ===========================================================
	// RESUB SUMMARY ADD (Uses Archival Tables)
	// ===========================================================

	public void saveResubIrradv(IRRBB_ADVANCES_Archival_Summary_Entity summary) {
		Long nextSno = jdbcTemplate
				.queryForObject("SELECT BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE_SNO_SEQ.NEXTVAL FROM DUAL", Long.class);
		summary.setSno(nextSno);

		String archivalSummarySql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE (SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, "
				+ "GL_CODE, GL_DESCRIPTION, TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
				+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, EXISTING_BENCHMARK, "
				+ "EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK, "
				+ "FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
				+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(archivalSummarySql, summary.getSno(), summary.getCustomerId(), summary.getAccountNumber(),
				summary.getSchemeCode(), summary.getGlCode(), summary.getGlDescription(), summary.getTypeOfLoan(),
				summary.getName(), summary.getAccountCurrency(), summary.getOutstandingBalanceAcctCcy(),
				summary.getOutstandingBalanceInr(), summary.getAccountOpeningDate(), summary.getMaturityDate(),
				summary.getTenorMonth(), summary.getEmiOfLoan(), summary.getFloatingFixed(),
				summary.getExistingBenchmark(), summary.getExistingRepricingFrequency(), summary.getLastRepricingDate(),
				summary.getNextRepricingDate(), summary.getSpreadOverBenchmark(), summary.getFinalRoi(),
				summary.getCapFloorRateOfInterest(), summary.getAssetStatus(), summary.getReportDate(),
				summary.getReportVersion(), summary.getReportFrequency(), summary.getReportCode(),
				summary.getReportDesc(), summary.getEntityFlg(), summary.getModifyFlg(), summary.getDelFlg(),
				summary.getReportResubdate());

		String archivalDetailSql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE (SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, "
				+ "GL_CODE, GL_DESCRIPTION, TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
				+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, EXISTING_BENCHMARK, "
				+ "EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK, "
				+ "FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
				+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(archivalDetailSql, summary.getSno(), summary.getCustomerId(), summary.getAccountNumber(),
				summary.getSchemeCode(), summary.getGlCode(), summary.getGlDescription(), summary.getTypeOfLoan(),
				summary.getName(), summary.getAccountCurrency(), summary.getOutstandingBalanceAcctCcy(),
				summary.getOutstandingBalanceInr(), summary.getAccountOpeningDate(), summary.getMaturityDate(),
				summary.getTenorMonth(), summary.getEmiOfLoan(), summary.getFloatingFixed(),
				summary.getExistingBenchmark(), summary.getExistingRepricingFrequency(), summary.getLastRepricingDate(),
				summary.getNextRepricingDate(), summary.getSpreadOverBenchmark(), summary.getFinalRoi(),
				summary.getCapFloorRateOfInterest(), summary.getAssetStatus(), summary.getReportDate(),
				summary.getReportVersion(), summary.getReportFrequency(), summary.getReportCode(),
				summary.getReportDesc(), summary.getEntityFlg(), summary.getModifyFlg(), summary.getDelFlg(),
				summary.getReportResubdate());
	}

	// Overloaded method - accepts Summary_Entity from controller
	public void saveResubIrradv(IRRBB_ADVANCES_Summary_Entity summary) {
		BigDecimal nextSno = jdbcTemplate.queryForObject(
				"SELECT BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE_SNO_SEQ.NEXTVAL FROM DUAL", BigDecimal.class);
		IRRBB_ADVANCES_Archival_Summary_Entity resubEntity = new IRRBB_ADVANCES_Archival_Summary_Entity();
		if (nextSno != null) {
			resubEntity.setSno(nextSno.longValue());
		}
		resubEntity.setCustomerId(summary.getCustomerId());
		resubEntity.setAccountNumber(summary.getAccountNumber());
		resubEntity.setSchemeCode(summary.getSchemeCode());
		resubEntity.setGlCode(summary.getGlCode());
		resubEntity.setGlDescription(summary.getGlDescription());
		resubEntity.setTypeOfLoan(summary.getTypeOfLoan());
		resubEntity.setName(summary.getName());
		resubEntity.setAccountCurrency(summary.getAccountCurrency());
		resubEntity.setOutstandingBalanceAcctCcy(summary.getOutstandingBalanceAcctCcy());
		resubEntity.setOutstandingBalanceInr(summary.getOutstandingBalanceInr());
		resubEntity.setAccountOpeningDate(summary.getAccountOpeningDate());
		resubEntity.setMaturityDate(summary.getMaturityDate());
		resubEntity.setTenorMonth(summary.getTenorMonth());
		resubEntity.setEmiOfLoan(summary.getEmiOfLoan());
		resubEntity.setFloatingFixed(summary.getFloatingFixed());
		resubEntity.setExistingBenchmark(summary.getExistingBenchmark());
		resubEntity.setExistingRepricingFrequency(summary.getExistingRepricingFrequency());
		resubEntity.setLastRepricingDate(summary.getLastRepricingDate());
		resubEntity.setNextRepricingDate(summary.getNextRepricingDate());
		resubEntity.setSpreadOverBenchmark(summary.getSpreadOverBenchmark());
		resubEntity.setFinalRoi(summary.getFinalRoi());
		resubEntity.setCapFloorRateOfInterest(summary.getCapFloorRateOfInterest());
		resubEntity.setAssetStatus(summary.getAssetStatus());
		resubEntity.setReportDate(summary.getReportDate());
		resubEntity.setReportVersion(summary.getReportVersion());
		resubEntity.setReportFrequency(summary.getReportFrequency());
		resubEntity.setReportCode(summary.getReportCode());
		resubEntity.setReportDesc(summary.getReportDesc());
		resubEntity.setEntityFlg(summary.getEntityFlg());
		resubEntity.setModifyFlg(summary.getModifyFlg());
		resubEntity.setDelFlg(summary.getDelFlg());

		saveResubIrradv(resubEntity);
	}

	// Helper: Save resub data as new version in ARCHIVAL tables
	private void saveResubAsNewVersion(IRRBB_ADVANCES_Archival_Summary_Entity oldRecord, BigDecimal newVersion) {
		Long nextSno = jdbcTemplate
				.queryForObject("SELECT BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE_SNO_SEQ.NEXTVAL FROM DUAL", Long.class);

		String archivalSummarySql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE (SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, "
				+ "GL_CODE, GL_DESCRIPTION, TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
				+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, EXISTING_BENCHMARK, "
				+ "EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK, "
				+ "FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
				+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(archivalSummarySql, nextSno, oldRecord.getCustomerId(), oldRecord.getAccountNumber(),
				oldRecord.getSchemeCode(), oldRecord.getGlCode(), oldRecord.getGlDescription(),
				oldRecord.getTypeOfLoan(), oldRecord.getName(), oldRecord.getAccountCurrency(),
				oldRecord.getOutstandingBalanceAcctCcy(), oldRecord.getOutstandingBalanceInr(),
				oldRecord.getAccountOpeningDate(), oldRecord.getMaturityDate(), oldRecord.getTenorMonth(),
				oldRecord.getEmiOfLoan(), oldRecord.getFloatingFixed(), oldRecord.getExistingBenchmark(),
				oldRecord.getExistingRepricingFrequency(), oldRecord.getLastRepricingDate(),
				oldRecord.getNextRepricingDate(), oldRecord.getSpreadOverBenchmark(), oldRecord.getFinalRoi(),
				oldRecord.getCapFloorRateOfInterest(), oldRecord.getAssetStatus(), oldRecord.getReportDate(),
				newVersion, oldRecord.getReportFrequency(), oldRecord.getReportCode(), oldRecord.getReportDesc(),
				oldRecord.getEntityFlg(), oldRecord.getModifyFlg(), oldRecord.getDelFlg(),
				oldRecord.getReportResubdate());

		String archivalDetailSql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE (SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, "
				+ "GL_CODE, GL_DESCRIPTION, TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
				+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, EXISTING_BENCHMARK, "
				+ "EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, SPREAD_OVER_BENCHMARK, "
				+ "FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
				+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(archivalDetailSql, nextSno, oldRecord.getCustomerId(), oldRecord.getAccountNumber(),
				oldRecord.getSchemeCode(), oldRecord.getGlCode(), oldRecord.getGlDescription(),
				oldRecord.getTypeOfLoan(), oldRecord.getName(), oldRecord.getAccountCurrency(),
				oldRecord.getOutstandingBalanceAcctCcy(), oldRecord.getOutstandingBalanceInr(),
				oldRecord.getAccountOpeningDate(), oldRecord.getMaturityDate(), oldRecord.getTenorMonth(),
				oldRecord.getEmiOfLoan(), oldRecord.getFloatingFixed(), oldRecord.getExistingBenchmark(),
				oldRecord.getExistingRepricingFrequency(), oldRecord.getLastRepricingDate(),
				oldRecord.getNextRepricingDate(), oldRecord.getSpreadOverBenchmark(), oldRecord.getFinalRoi(),
				oldRecord.getCapFloorRateOfInterest(), oldRecord.getAssetStatus(), oldRecord.getReportDate(),
				newVersion, oldRecord.getReportFrequency(), oldRecord.getReportCode(), oldRecord.getReportDesc(),
				oldRecord.getEntityFlg(), oldRecord.getModifyFlg(), oldRecord.getDelFlg(),
				oldRecord.getReportResubdate());
	}

	// ===========================================================
	// ADDITIONAL METHODS FOR ARCHIVAL & RESUB
	// ===========================================================

	// Get Archival Summary Data
	public List<IRRBB_ADVANCES_Archival_Summary_Entity> getArchivalData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalSummaryRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalSummaryRowMapper());
	}

	// Get Archival Detail Data
	public List<IRRBB_ADVANCES_Archival_Detail_Entity> getArchivalDetailData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalDetailRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalDetailRowMapper());
	}

	// Get Resub Summary Data (Uses Archival Table)
	public List<IRRBB_ADVANCES_Archival_Summary_Entity> getResubData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalSummaryRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalSummaryRowMapper());
	}

	// Get Resub Detail Data (Uses Archival Table)
	public List<IRRBB_ADVANCES_Archival_Detail_Entity> getResubDetailData(String reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
		if (version != null) {
			sql += " AND REPORT_VERSION = ?";
			return jdbcTemplate.query(sql, new Object[] { reportDate, version }, new ArchivalDetailRowMapper());
		}
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ArchivalDetailRowMapper());
	}

	// Get All Versions for archival
	public List<BigDecimal> getArchivalVersions(String reportDate) {
		String sql = "SELECT DISTINCT REPORT_VERSION FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY') ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.queryForList(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// Get All Versions for resub
	public List<BigDecimal> getResubVersions(String reportDate) {
		String sql = "SELECT DISTINCT REPORT_VERSION FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY') ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.queryForList(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// Get next version number for resub
	public BigDecimal getNextResubVersion(String reportDate) {
		try {
			String sql = "SELECT COALESCE(MAX(REPORT_VERSION), 0) + 1 FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')";
			BigDecimal nextVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return nextVersion != null ? nextVersion : BigDecimal.ONE;
		} catch (Exception e) {
			return BigDecimal.ONE;
		}
	}

	// ===========================================================
	// GET REPORT_DATE + REPORT_VERSION for ARCHIVAL
	// ===========================================================
	public List<Object[]> getIRRBB_ADVANCESArchival() {
		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// ===========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION (SUMMARY)
	// ===========================================================
	public List<IRRBB_ADVANCES_Archival_Summary_Entity> getArchivalDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION (DETAIL)
	// ===========================================================
	public List<IRRBB_ADVANCES_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	// ===========================================================
	// GET RESUB FULL DATA BY DATE + VERSION (SUMMARY)
	// ===========================================================
	public List<IRRBB_ADVANCES_Archival_Summary_Entity> getResubDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// GET RESUB FULL DATA BY DATE + VERSION (DETAIL)
	// ===========================================================
	public List<IRRBB_ADVANCES_Archival_Detail_Entity> getResubDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ArchivalDetailRowMapper());
	}

	// ===========================================================
	// GET ALL WITH VERSION (for archival listing)
	// ===========================================================
	public List<IRRBB_ADVANCES_Archival_Summary_Entity> getAllArchivalWithVersion() {

		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";

		return jdbcTemplate.query(sql, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// GET ALL RESUB WITH VERSION (for resub listing)
	// ===========================================================
	public List<IRRBB_ADVANCES_Archival_Summary_Entity> getAllResubWithVersion() {

		String sql = "SELECT * FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";

		return jdbcTemplate.query(sql, new ArchivalSummaryRowMapper());
	}

	// ===========================================================
	// FIND MAX VERSION BY DATE (for archival)
	// ===========================================================
	public BigDecimal findMaxArchivalVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', ''))) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			BigDecimal max = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return max != null ? max : BigDecimal.ZERO;
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal findMaxResubVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(TO_NUMBER(REGEXP_REPLACE(TO_CHAR(REPORT_VERSION), '[^0-9.]', ''))) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			BigDecimal max = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return max != null ? max : BigDecimal.ZERO;
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	// ===========================================================
	// GET ARCHIVAL LIST (for dropdown)
	// ===========================================================
	public List<Object[]> getIRRBB_ADVANCESArchivalList() {

		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// ===========================================================
	// GET RESUB LIST (for dropdown)
	// ===========================================================
	public List<Object[]> getIRRBB_ADVANCESResubList() {

		String sql = "SELECT DISTINCT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// ===========================================================
	// EXCEL DOWNLOAD METHOD
	// ===========================================================

	public byte[] getBRRS_IRRBB_ADVANCES_Excel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		Date reportDate = dateformat.parse(todate);
		String formattedDate = dateformat.format(reportDate);

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			List<IRRBB_ADVANCES_Archival_Summary_Entity> dataList = getArchivalData(formattedDate, version);
			return generateExcelFromArchivalData(dataList, filename, type);
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			List<IRRBB_ADVANCES_Archival_Summary_Entity> dataList = getResubData(formattedDate, version);
			return generateExcelFromResubData(dataList, filename, type);
		} else {
			List<IRRBB_ADVANCES_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_IRRBB_ADV_SUMMARYTABLE WHERE REPORT_DATE = TO_DATE(?, 'DD-MON-YYYY')",
					new Object[] { formattedDate }, new SummaryRowMapper());
			return generateExcelFromNormalData(dataList, filename);
		}
	}

	private byte[] generateExcelFromNormalData(List<IRRBB_ADVANCES_Summary_Entity> dataList, String filename)
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

			for (IRRBB_ADVANCES_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getCustomerId(), textStyle);
				setCellValue(row, 1, data.getAccountNumber(), textStyle);
				setCellValue(row, 2, data.getSchemeCode(), textStyle);
				setCellValue(row, 3, data.getGlCode(), textStyle);
				setCellValue(row, 4, data.getGlDescription(), textStyle);
				setCellValue(row, 5, data.getTypeOfLoan(), textStyle);
				setCellValue(row, 6, data.getName(), textStyle);
				setCellValue(row, 7, data.getAccountCurrency(), textStyle);
				setCellValue(row, 8, data.getOutstandingBalanceAcctCcy(), numberStyle);
				setCellValue(row, 9, data.getOutstandingBalanceInr(), numberStyle);
				setCellValue(row, 10, data.getAccountOpeningDate(), dateStyle);
				setCellValue(row, 11, data.getMaturityDate(), dateStyle);
				setCellValue(row, 12, data.getTenorMonth(), numberStyle);
				setCellValue(row, 13, data.getEmiOfLoan(), numberStyle);
				setCellValue(row, 14, data.getFloatingFixed(), textStyle);
				setCellValue(row, 15, data.getExistingBenchmark(), textStyle);
				setCellValue(row, 16, data.getExistingRepricingFrequency(), textStyle);
				setCellValue(row, 17, data.getLastRepricingDate(), dateStyle);
				setCellValue(row, 18, data.getNextRepricingDate(), dateStyle);
				setCellValue(row, 19, data.getSpreadOverBenchmark(), textStyle);
				setCellValue(row, 20, data.getFinalRoi(), textStyle);
				setCellValue(row, 21, data.getCapFloorRateOfInterest(), textStyle);
				setCellValue(row, 22, data.getAssetStatus(), textStyle);
			}

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromArchivalData(List<IRRBB_ADVANCES_Archival_Summary_Entity> dataList, String filename,
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

			for (IRRBB_ADVANCES_Archival_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getCustomerId(), textStyle);
				setCellValue(row, 1, data.getAccountNumber(), textStyle);
				setCellValue(row, 2, data.getSchemeCode(), textStyle);
				setCellValue(row, 3, data.getGlCode(), textStyle);
				setCellValue(row, 4, data.getGlDescription(), textStyle);
				setCellValue(row, 5, data.getTypeOfLoan(), textStyle);
				setCellValue(row, 6, data.getName(), textStyle);
				setCellValue(row, 7, data.getAccountCurrency(), textStyle);
				setCellValue(row, 8, data.getOutstandingBalanceAcctCcy(), numberStyle);
				setCellValue(row, 9, data.getOutstandingBalanceInr(), numberStyle);
				setCellValue(row, 10, data.getAccountOpeningDate(), dateStyle);
				setCellValue(row, 11, data.getMaturityDate(), dateStyle);
				setCellValue(row, 12, data.getTenorMonth(), numberStyle);
				setCellValue(row, 13, data.getEmiOfLoan(), numberStyle);
				setCellValue(row, 14, data.getFloatingFixed(), textStyle);
				setCellValue(row, 15, data.getExistingBenchmark(), textStyle);
				setCellValue(row, 16, data.getExistingRepricingFrequency(), textStyle);
				setCellValue(row, 17, data.getLastRepricingDate(), dateStyle);
				setCellValue(row, 18, data.getNextRepricingDate(), dateStyle);
				setCellValue(row, 19, data.getSpreadOverBenchmark(), textStyle);
				setCellValue(row, 20, data.getFinalRoi(), textStyle);
				setCellValue(row, 21, data.getCapFloorRateOfInterest(), textStyle);
				setCellValue(row, 22, data.getAssetStatus(), textStyle);
			}

			workbook.write(out);
			return out.toByteArray();
		}
	}

	private byte[] generateExcelFromResubData(List<IRRBB_ADVANCES_Archival_Summary_Entity> dataList, String filename,
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

			for (IRRBB_ADVANCES_Archival_Summary_Entity data : dataList) {
				Row row = sheet.createRow(rowIndex++);
				setCellValue(row, 0, data.getCustomerId(), textStyle);
				setCellValue(row, 1, data.getAccountNumber(), textStyle);
				setCellValue(row, 2, data.getSchemeCode(), textStyle);
				setCellValue(row, 3, data.getGlCode(), textStyle);
				setCellValue(row, 4, data.getGlDescription(), textStyle);
				setCellValue(row, 5, data.getTypeOfLoan(), textStyle);
				setCellValue(row, 6, data.getName(), textStyle);
				setCellValue(row, 7, data.getAccountCurrency(), textStyle);
				setCellValue(row, 8, data.getOutstandingBalanceAcctCcy(), numberStyle);
				setCellValue(row, 9, data.getOutstandingBalanceInr(), numberStyle);
				setCellValue(row, 10, data.getAccountOpeningDate(), dateStyle);
				setCellValue(row, 11, data.getMaturityDate(), dateStyle);
				setCellValue(row, 12, data.getTenorMonth(), numberStyle);
				setCellValue(row, 13, data.getEmiOfLoan(), numberStyle);
				setCellValue(row, 14, data.getFloatingFixed(), textStyle);
				setCellValue(row, 15, data.getExistingBenchmark(), textStyle);
				setCellValue(row, 16, data.getExistingRepricingFrequency(), textStyle);
				setCellValue(row, 17, data.getLastRepricingDate(), dateStyle);
				setCellValue(row, 18, data.getNextRepricingDate(), dateStyle);
				setCellValue(row, 19, data.getSpreadOverBenchmark(), textStyle);
				setCellValue(row, 20, data.getFinalRoi(), textStyle);
				setCellValue(row, 21, data.getCapFloorRateOfInterest(), textStyle);
				setCellValue(row, 22, data.getAssetStatus(), textStyle);
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

		// =====================================================
		// RESUBMISSION MODE: Update ONLY Archival Tables as a NEW version
		// =====================================================
		if ("RESUB".equalsIgnoreCase(type)) {
			Date reportDate = null;
			try {
				reportDate = jdbcTemplate.queryForObject("SELECT REPORT_DATE FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?",
						Date.class, rows.get(0).getSno());
			} catch (Exception e) {
				try {
					reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE", Date.class);
				} catch (Exception ex) {
					logger.warn("Could not query REPORT_DATE from archival table: {}", ex.getMessage());
				}
			}

			if (reportDate != null) {
				try {
					BigDecimal maxVer = findMaxResubVersion(reportDate);
					BigDecimal nextVersion = (maxVer != null) ? maxVer.add(BigDecimal.ONE) : BigDecimal.ONE;
					logger.info("Resubmission mode for IRRBB_ADVANCES: Cloned max version {} into new archival version {}", maxVer, nextVersion);

					// Insert modified summary records directly into nextVersion in archival summary table
					String insertArchivalSummarySql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
							+ "(SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
							+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
							+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
							+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
							+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
							+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
							+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE) + 1, "
							+ "CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
							+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
							+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, ?, "
							+ "?, ?, ?, ?, "
							+ "?, FINAL_ROI, ?, ASSET_STATUS, REPORT_DATE, "
							+ "?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
							+ "FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?";

					for (IRRBB_ADVANCES_Update_Row row : rows) {
						if (row.getSno() == null) continue;
						Date lastRepriceDate = parseDate(row.getLastRepricingDate(), sdf1, sdf2);
						Date nextRepriceDate = parseDate(row.getNextRepricingDate(), sdf1, sdf2);

						jdbcTemplate.update(insertArchivalSummarySql,
								row.getFloatingFixed(),
								row.getExistingBenchmark(),
								row.getExistingRepricingFrequency(),
								lastRepriceDate,
								nextRepriceDate,
								row.getSpreadOverBenchmark(),
								row.getCapFloorRateOfInterest(),
								nextVersion,
								row.getSno());
					}

					// Clone maxVer detail records to nextVersion in archival detail table
					String cloneDetailSql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE "
							+ "(SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
							+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
							+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
							+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
							+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
							+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
							+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE) + ROWNUM, "
							+ "CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
							+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
							+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
							+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
							+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
							+ "?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
							+ "FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

					jdbcTemplate.update(cloneDetailSql, nextVersion, reportDate, maxVer);

					String updateArchivalDetailSql = "UPDATE BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE SET "
							+ "FLOATING_FIXED = ?, EXISTING_BENCHMARK = ?, EXISTING_REPRICING_FREQUENCY = ?, "
							+ "LAST_REPRICING_DATE = ?, NEXT_REPRICING_DATE = ?, SPREAD_OVER_BENCHMARK = ?, CAP_FLOOR_RATE_OF_INTEREST = ? "
							+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? "
							+ "AND ("
							+ "  (ACCOUNT_NUMBER IS NOT NULL AND ACCOUNT_NUMBER = (SELECT ACCOUNT_NUMBER FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?)) "
							+ "  OR (CUSTOMER_ID IS NOT NULL AND CUSTOMER_ID = (SELECT CUSTOMER_ID FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE WHERE SNO = ?))"
							+ ")";

					for (IRRBB_ADVANCES_Update_Row row : rows) {
						if (row.getSno() == null) continue;
						Date lastRepriceDate = parseDate(row.getLastRepricingDate(), sdf1, sdf2);
						Date nextRepriceDate = parseDate(row.getNextRepricingDate(), sdf1, sdf2);

						try {
							jdbcTemplate.update(updateArchivalDetailSql, row.getFloatingFixed(), row.getExistingBenchmark(),
									row.getExistingRepricingFrequency(), lastRepriceDate, nextRepriceDate,
									row.getSpreadOverBenchmark(), row.getCapFloorRateOfInterest(),
									reportDate, nextVersion, row.getSno(), row.getSno());
						} catch (Exception ex) {
							logger.warn("Detail archival update warning for SNO {}: {}", row.getSno(), ex.getMessage());
						}
					}
				} catch (Exception e) {
					logger.error("Error performing IRRBB_ADVANCES resubmission update: {}", e.getMessage(), e);
				}
			}
			return;
		}

		// =====================================================
		// NORMAL MODE: Archive current un-modified snapshot FIRST, then Update Live Tables
		// =====================================================
		String summaryTable = "BRRS_IRRBB_ADV_SUMMARYTABLE";
		String detailTable = "BRRS_IRRBB_ADV_DETAILTABLE";

		Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject("SELECT REPORT_DATE FROM " + summaryTable + " WHERE SNO = ?",
					Date.class, rows.get(0).getSno());
		} catch (Exception e) {
			try {
				reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + summaryTable, Date.class);
			} catch (Exception ex) {
				logger.warn("Could not query REPORT_DATE: {}", ex.getMessage());
			}
		}

		// Step 1: Copy CURRENT un-modified record from SUMMARY/DETAIL table into ARCHIVAL table as maxVersion + 1 FIRST
		if (reportDate != null) {
			try {
				BigDecimal nextVersion = findMaxArchivalVersion(reportDate);
				nextVersion = (nextVersion != null) ? nextVersion.add(BigDecimal.ONE) : BigDecimal.ONE;
				logger.info("Archiving current summary & detail data for date {} as version {} before submit updates",
						reportDate, nextVersion);

				String archiveSummarySql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE "
						+ "(SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
						+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
						+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
						+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
						+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
						+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
						+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE) + ROWNUM, "
						+ "CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
						+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
						+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
						+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
						+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
						+ "?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
						+ "FROM BRRS_IRRBB_ADV_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";

				int summaryCount = jdbcTemplate.update(archiveSummarySql, nextVersion, reportDate);
				logger.info("Archived {} summary records into BRRS_IRRBB_ADV_ARCHIVAL_SUMMARYTABLE with version {}",
						summaryCount, nextVersion);

				String archiveDetailSql = "INSERT INTO BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE "
						+ "(SNO, CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
						+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
						+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
						+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
						+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
						+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
						+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE) + ROWNUM, "
						+ "CUSTOMER_ID, ACCOUNT_NUMBER, SCHEME_CODE, GL_CODE, GL_DESCRIPTION, "
						+ "TYPE_OF_LOAN, NAME, ACCOUNT_CURRENCY, OUTSTANDING_BALANCE_ACCT_CCY, OUTSTANDING_BALANCE_INR, "
						+ "ACCOUNT_OPENING_DATE, MATURITY_DATE, TENOR_MONTH, EMI_OF_LOAN, FLOATING_FIXED, "
						+ "EXISTING_BENCHMARK, EXISTING_REPRICING_FREQUENCY, LAST_REPRICING_DATE, NEXT_REPRICING_DATE, "
						+ "SPREAD_OVER_BENCHMARK, FINAL_ROI, CAP_FLOOR_RATE_OF_INTEREST, ASSET_STATUS, REPORT_DATE, "
						+ "?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
						+ "FROM BRRS_IRRBB_ADV_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";

				int detailCount = jdbcTemplate.update(archiveDetailSql, nextVersion, reportDate);
				logger.info("Archived {} detail records into BRRS_IRRBB_ADV_ARCHIVAL_DETAILTABLE with version {}",
						detailCount, nextVersion);
			} catch (Exception e) {
				logger.error("Error archiving summary & detail data before updates: {}", e.getMessage(), e);
			}
		}

		// Step 2: NOW apply inline updates to main summary and detail tables
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