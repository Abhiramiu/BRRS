package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_GL_SCH_ARCHIVALTABLE_DETAIL")
public class GL_SCH_Archival_Detail_Entity {

	@Column(name = "CUST_ID")
	private String custId;
	@Id
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

	@Column(name = "REPORT_LABEL_1")
	private String reportLabel1;

	@Column(name = "REPORT_LABEL_2")
	private String reportLabel2;

	@Column(name = "REPORT_LABEL_3")
	private String reportLabel3;

	@Column(name = "REPORT_LABEL_4")
	private String reportLabel4;

	@Column(name = "REPORT_LABEL_6")
	private String reportLabel6;

	@Column(name = "REPORT_ADDL_CRITERIA_1")
	private String reportAddlCriteria1;

	@Column(name = "REPORT_ADDL_CRITERIA_2")
	private String reportAddlCriteria2;

	@Column(name = "REPORT_ADDL_CRITERIA_3")
	private String reportAddlCriteria3;

	@Column(name = "REPORT_ADDL_CRITERIA_4")
	private String reportAddlCriteria4;

	@Column(name = "REPORT_ADDL_CRITERIA_5")
	private String reportAddlCriteria5;

	@Column(name = "REPORT_ADDL_CRITERIA_6")
	private String reportAddlCriteria6;

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

	public GL_SCH_Archival_Detail_Entity() {
		super();
	}

	public String getReportLabel2() {
		return reportLabel2;
	}

	public void setReportLabel2(String reportLabel2) {
		this.reportLabel2 = reportLabel2;
	}

	public String getReportLabel3() {
		return reportLabel3;
	}

	public void setReportLabel3(String reportLabel3) {
		this.reportLabel3 = reportLabel3;
	}

	public String getReportLabel4() {
		return reportLabel4;
	}

	public void setReportLabel4(String reportLabel4) {
		this.reportLabel4 = reportLabel4;
	}

	public String getReportLabel6() {
		return reportLabel6;
	}

	public void setReportLabel6(String reportLabel6) {
		this.reportLabel6 = reportLabel6;
	}

	public String getReportAddlCriteria2() {
		return reportAddlCriteria2;
	}

	public void setReportAddlCriteria2(String reportAddlCriteria2) {
		this.reportAddlCriteria2 = reportAddlCriteria2;
	}

	public String getReportAddlCriteria3() {
		return reportAddlCriteria3;
	}

	public void setReportAddlCriteria3(String reportAddlCriteria3) {
		this.reportAddlCriteria3 = reportAddlCriteria3;
	}

	public String getReportAddlCriteria4() {
		return reportAddlCriteria4;
	}

	public void setReportAddlCriteria4(String reportAddlCriteria4) {
		this.reportAddlCriteria4 = reportAddlCriteria4;
	}

	public String getReportAddlCriteria5() {
		return reportAddlCriteria5;
	}

	public void setReportAddlCriteria5(String reportAddlCriteria5) {
		this.reportAddlCriteria5 = reportAddlCriteria5;
	}

	public String getReportAddlCriteria6() {
		return reportAddlCriteria6;
	}

	public void setReportAddlCriteria6(String reportAddlCriteria6) {
		this.reportAddlCriteria6 = reportAddlCriteria6;
	}

	public String getReportLabel1() {
		return reportLabel1;
	}

	public void setReportLabel1(String reportLabel1) {
		this.reportLabel1 = reportLabel1;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
	}

}
