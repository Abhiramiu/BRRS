package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "BRRS_M_SFINP2_DETAILTABLE")
public class M_SFINP2_Detail_Entity {
	
	
   @Column(name = "CUST_ID")
   private String custId;
	@Id
   @Column(name = "ACCT_NUMBER")
   private String acctNumber;
   
   @Column(name = "ACCT_NAME")
   private String acctName;
   
   @Column(name = "DATA_TYPE")
   private String dataType;
   
   @Column(name = "COLUMN_ID")
   private String columnId;
  
   @Column(name = "REPORT_LABEL")
   private String reportLabel;
   
   @Column(name = "REPORT_ADDL_CRITERIA_1")
   private String reportAddlCriteria_1;
   
   @Column(name = "REPORT_REMARKS")
   private String reportRemarks;
   
   @Column(name = "MODIFICATION_REMARKS")
   private String modificationRemarks;
   
   @Column(name = "DATA_ENTRY_VERSION")
   private String dataEntryVersion;
   
   @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
   private BigDecimal acctBalanceInpula;
   
   @Column(name = "REPORT_DATE")
   @DateTimeFormat(pattern = "dd-MM-yyyy")
   private Date reportDate;
   
   @Column(name = "REPORT_NAME")
   private String reportName;
   
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
   
  
// ---------- MISSING FIELDS ----------

@Column(name = "REPORT_NAME_1")
private String reportName1;

@Column(name = "GL_CODE")
private String glCode;

@Column(name = "GL_SUB_CODE")
private String glSubCode;

@Column(name = "HEAD_ACC_NO")
private String headAccNo;

@Column(name = "DESCRIPTION")
private String description;

@Column(name = "CURRENCY")
private String currency;

@Column(name = "DEBIT_BALANCE", precision = 24, scale = 3)
private BigDecimal debitBalance;

@Column(name = "CREDIT_BALANCE", precision = 24, scale = 3)
private BigDecimal creditBalance;

@Column(name = "DEBIT_EQUIVALENT", precision = 24, scale = 3)
private BigDecimal debitEquivalent;

@Column(name = "CREDIT_EQUIVALENT", precision = 24, scale = 3)
private BigDecimal creditEquivalent;

@Column(name = "ENTRY_USER")
private String entryUser;

@Column(name = "ENTRY_DATE")
@DateTimeFormat(pattern = "dd-MM-yyyy")
private Date entryDate;
   
   @Column(name = "AVERAGE", precision = 24, scale = 3)
   private BigDecimal average;

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

public String getColumnId() {
	return columnId;
}

public void setColumnId(String columnId) {
	this.columnId = columnId;
}

public String getReportLabel() {
	return reportLabel;
}

public void setReportLabel(String reportLabel) {
	this.reportLabel = reportLabel;
}

public String getReportAddlCriteria_1() {
	return reportAddlCriteria_1;
}

public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
	this.reportAddlCriteria_1 = reportAddlCriteria_1;
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

public String getReportName1() {
	return reportName1;
}

public void setReportName1(String reportName1) {
	this.reportName1 = reportName1;
}

public String getGlCode() {
	return glCode;
}

public void setGlCode(String glCode) {
	this.glCode = glCode;
}

public String getGlSubCode() {
	return glSubCode;
}

public void setGlSubCode(String glSubCode) {
	this.glSubCode = glSubCode;
}

public String getHeadAccNo() {
	return headAccNo;
}

public void setHeadAccNo(String headAccNo) {
	this.headAccNo = headAccNo;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getCurrency() {
	return currency;
}

public void setCurrency(String currency) {
	this.currency = currency;
}

public BigDecimal getDebitBalance() {
	return debitBalance;
}

public void setDebitBalance(BigDecimal debitBalance) {
	this.debitBalance = debitBalance;
}

public BigDecimal getCreditBalance() {
	return creditBalance;
}

public void setCreditBalance(BigDecimal creditBalance) {
	this.creditBalance = creditBalance;
}

public BigDecimal getDebitEquivalent() {
	return debitEquivalent;
}

public void setDebitEquivalent(BigDecimal debitEquivalent) {
	this.debitEquivalent = debitEquivalent;
}

public BigDecimal getCreditEquivalent() {
	return creditEquivalent;
}

public void setCreditEquivalent(BigDecimal creditEquivalent) {
	this.creditEquivalent = creditEquivalent;
}

public String getEntryUser() {
	return entryUser;
}

public void setEntryUser(String entryUser) {
	this.entryUser = entryUser;
}

public Date getEntryDate() {
	return entryDate;
}

public void setEntryDate(Date entryDate) {
	this.entryDate = entryDate;
}

public BigDecimal getAverage() {
	return average;
}

public void setAverage(BigDecimal average) {
	this.average = average;
}

public M_SFINP2_Detail_Entity() {
	super();
	// TODO Auto-generated constructor stub
}



  
}
