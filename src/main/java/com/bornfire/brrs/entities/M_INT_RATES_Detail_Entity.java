package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_M_INT_RATES_DETAILTABLE")
public class M_INT_RATES_Detail_Entity {

		@Id
	    @Column(name = "SNO", length = 20)
	    private String sno;

	    @Column(name = "CUST_ID", length = 100)
	    private String custId;

	    @Column(name = "ACCT_NUMBER", length = 100)
	    private String acctNumber;

	    @Column(name = "ACCT_NAME", length = 100)
	    private String acctName;

	    @Column(name = "REPORT_LABLE", length = 100)
	    private String reportLable;

	    @Column(name = "REPORT_ADDL_CRITERIA_1", length = 100)
	    private String reportAddlCriteria1;

	    @Column(name = "REPORT_REMARKS", length = 100)
	    private String reportRemarks;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "REPORT_DATE")
	    private Date reportDate;

	    @Column(name = "REPORT_NAME", length = 100)
	    private String reportName;

	    @Column(name = "CREATE_USER", length = 50)
	    private String createUser;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "CREATE_TIME")
	    private Date createTime;

	    @Column(name = "MODIFY_USER", length = 50)
	    private String modifyUser;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "MODIFY_TIME")
	    private Date modifyTime;

	    @Column(name = "VERIFY_USER", length = 50)
	    private String verifyUser;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "VERIFY_TIME")
	    private Date verifyTime;

	    @Column(name = "ENTITY_FLG", length = 1)
	    private String entityFlg;

	    @Column(name = "MODIFY_FLG", length = 1)
	    private String modifyFlg;

	    @Column(name = "DEL_FLG", length = 1)
	    private String delFlg;
	    
	    @Column(name = "NOMINAL_INTEREST_RATE")
	    private BigDecimal NOMINAL_INTEREST_RATE;
	    
	    @Column(name = "AVG_EFFECTIVE_RATE")
	    private BigDecimal AVG_EFFECTIVE_RATE;
	    
	    @Column(name = "VOLUME")
	    private BigDecimal VOLUME;
	    
	   
	    
	    @Column(name = "DATA_ENTRY_VERSION", length = 100)
	    private String dataEntryVersion;

	    @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
	    private BigDecimal acctBalanceInPula;

		public String getSno() {
			return sno;
		}

		public void setSno(String sno) {
			this.sno = sno;
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

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
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

		public BigDecimal getNOMINAL_INTEREST_RATE() {
			return NOMINAL_INTEREST_RATE;
		}

		public void setNOMINAL_INTEREST_RATE(BigDecimal nOMINAL_INTEREST_RATE) {
			NOMINAL_INTEREST_RATE = nOMINAL_INTEREST_RATE;
		}

		public BigDecimal getAVG_EFFECTIVE_RATE() {
			return AVG_EFFECTIVE_RATE;
		}

		public void setAVG_EFFECTIVE_RATE(BigDecimal aVG_EFFECTIVE_RATE) {
			AVG_EFFECTIVE_RATE = aVG_EFFECTIVE_RATE;
		}

		public BigDecimal getVOLUME() {
			return VOLUME;
		}

		public void setVOLUME(BigDecimal vOLUME) {
			VOLUME = vOLUME;
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

		public M_INT_RATES_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

		
	    

}
