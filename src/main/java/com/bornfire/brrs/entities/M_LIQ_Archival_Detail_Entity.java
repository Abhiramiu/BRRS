package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_LIQ_ARCHIVALTABLE_DETAIL")

public class M_LIQ_Archival_Detail_Entity {
	
	
	

	@Id
	@Column(name = "ID", length = 50)
	private String id;

	@Column(name = "ACCT_NAME", length = 100)
	private String acctName;

	@Column(name = "DATA_TYPE", length = 100)
	private String dataType;

	@Column(name = "ROW_ID", length = 100)
	private String rowId;

	@Column(name = "COLUMN_ID", length = 100)
	private String columnId;

	@Column(name = "REPORT_REMARKS", length = 100)
	private String reportRemarks;

	@Column(name = "MODIFICATION_REMARKS", length = 100)
	private String modificationRemarks;

	@Column(name = "DATA_ENTRY_VERSION", length = 100)
	private String dataEntryVersion;

	@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
	private BigDecimal acctBalanceInPula;

	@Column(name = "REPORT_DATE")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date reportDate;

	@Column(name = "REPORT_NAME", length = 100)
	private String reportName;

	@Column(name = "CREATE_USER", length = 50)
	private String createUser;

	@Column(name = "CREATE_TIME")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date createTime;

	@Column(name = "MODIFY_USER", length = 50)
	private String modifyUser;

	@Column(name = "MODIFY_TIME")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date modifyTime;

	@Column(name = "VERIFY_USER", length = 50)
	private String verifyUser;

	@Column(name = "VERIFY_TIME")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date verifyTime;

	@Column(name = "ENTITY_FLG", length = 1)
	private String entityFlg;

	@Column(name = "MODIFY_FLG", length = 1)
	private String modifyFlg;

	@Column(name = "DEL_FLG", length = 1)
	private String delFlg;

	@Column(name = "REPORT_NAME_1", length = 500)
	private String reportName1;

	@Column(name = "GL_CODE", length = 50)
	private String glCode;

	@Column(name = "GL_SUB_CODE", length = 50)
	private String glSubCode;

	@Column(name = "HEAD_ACC_NO", length = 50)
	private String headAccNo;

	@Column(name = "DESCRIPTION", length = 200)
	private String description;

	@Column(name = "CURRENCY", length = 10)
	private String currency;

	@Column(name = "DEBIT_BALANCE", precision = 18, scale = 2)
	private BigDecimal debitBalance;

	@Column(name = "CREDIT_BALANCE", precision = 18, scale = 2)
	private BigDecimal creditBalance;

	@Column(name = "DEBIT_EQUIVALENT", precision = 18, scale = 2)
	private BigDecimal debitEquivalent;

	@Column(name = "CREDIT_EQUIVALENT", precision = 18, scale = 2)
	private BigDecimal creditEquivalent;

	@Column(name = "ENTRY_USER", length = 50)
	private String entryUser;

	@Column(name = "ENTRY_DATE")
	private Timestamp entryDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
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

	public BigDecimal getAcctBalanceInPula() {
		return acctBalanceInPula;
	}

	public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
		this.acctBalanceInPula = acctBalanceInPula;
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

	public Timestamp getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}

	public M_LIQ_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
    

	
    
    
    
    

	
    
    
    

}
