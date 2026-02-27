
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_DEP3_DETAILTABLE")
public class M_DEP3_Detail_Entity {

	@Column(name = "CUST_ID")
	private String custId;

	@Id
	@Column(name = "ACCT_NUMBER")
	private String acctNumber;

	@Column(name = "ACCT_NAME")
	private String acctName;

	@Column(name = "DATA_TYPE")
	private String dataType;

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

	@Column(name = "ACCT_BALANCE_IN_PULA")
	private BigDecimal acctBalanceInpula;

	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Column(name = "REPORT_NAME")
	private String reportName;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "MODIFY_USER")
	private String modifyUser;

	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	@Column(name = "VERIFY_USER")
	private String verifyUser;

	@Column(name = "VERIFY_TIME")
	private Date verifyTime;

	@Column(name = "ENTITY_FLG")
	private String entityFlg;

	@Column(name = "MODIFY_FLG")
	private String modifyFlg;

	@Column(name = "DEL_FLG")
	private String delFlg;

	@Column(name = "CCY")
	private String ccy;

	@Column(name = "SEGMENT")
	private String segment;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "MAT_BUCK_1")
	private String matBuck1;

	@Column(name = "EX_RATE_BUY")
	private BigDecimal exRateBuy;

	@Column(name = "EX_RATE_SELL")
	private BigDecimal exRateSell;

	@Column(name = "NOTICE_0TO31")
	private BigDecimal notice0to31;

	@Column(name = "NOTICE_32TO88")
	private BigDecimal notice32to88;

	@Column(name = "CER_OF_DEPO")
	private BigDecimal cerOfDepo;

	@Column(name = "IMPORT")
	private BigDecimal importValue; // 'import' is Java keyword

	@Column(name = "INVESTMENT")
	private BigDecimal investment;

	@Column(name = "OTHER")
	private BigDecimal other;

	@Column(name = "RESIDENTS")
	private BigDecimal residents;

	@Column(name = "NON_RESIDENTS")
	private BigDecimal nonResidents;

	@Column(name = "SNO")
	private Long sno;

	@Column(name = "REPORT_CODE")
	private String reportCode;

	public M_DEP3_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
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

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMatBuck1() {
		return matBuck1;
	}

	public void setMatBuck1(String matBuck1) {
		this.matBuck1 = matBuck1;
	}

	public BigDecimal getExRateBuy() {
		return exRateBuy;
	}

	public void setExRateBuy(BigDecimal exRateBuy) {
		this.exRateBuy = exRateBuy;
	}

	public BigDecimal getExRateSell() {
		return exRateSell;
	}

	public void setExRateSell(BigDecimal exRateSell) {
		this.exRateSell = exRateSell;
	}

	public BigDecimal getNotice0to31() {
		return notice0to31;
	}

	public void setNotice0to31(BigDecimal notice0to31) {
		this.notice0to31 = notice0to31;
	}

	public BigDecimal getNotice32to88() {
		return notice32to88;
	}

	public void setNotice32to88(BigDecimal notice32to88) {
		this.notice32to88 = notice32to88;
	}

	public BigDecimal getCerOfDepo() {
		return cerOfDepo;
	}

	public void setCerOfDepo(BigDecimal cerOfDepo) {
		this.cerOfDepo = cerOfDepo;
	}

	public BigDecimal getImportValue() {
		return importValue;
	}

	public void setImportValue(BigDecimal importValue) {
		this.importValue = importValue;
	}

	public BigDecimal getInvestment() {
		return investment;
	}

	public void setInvestment(BigDecimal investment) {
		this.investment = investment;
	}

	public BigDecimal getOther() {
		return other;
	}

	public void setOther(BigDecimal other) {
		this.other = other;
	}

	public BigDecimal getResidents() {
		return residents;
	}

	public void setResidents(BigDecimal residents) {
		this.residents = residents;
	}

	public BigDecimal getNonResidents() {
		return nonResidents;
	}

	public void setNonResidents(BigDecimal nonResidents) {
		this.nonResidents = nonResidents;
	}

	public Long getSno() {
		return sno;
	}

	public void setSno(Long sno) {
		this.sno = sno;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

}