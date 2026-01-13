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
@Table(name = "BRRS_M_INT_RATES_FCA_NEW_ARCHIVALTABLE_DETAIL")
public class M_INT_RATES_FCA_NEW_Archival_Detail_Entity {

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
	    
	    @Column(name = "CURRENT_AMT")
	    private BigDecimal CURRENT_AMT;
	    
	    @Column(name = "CALL_AMT")
	    private BigDecimal CALL_AMT;
	    
	    @Column(name = "SAVINGS")
	    private BigDecimal SAVINGS;
	    
	    @Column(name = "NOTICE_0_31_DAYS")
	    private BigDecimal NOTICE_0_31_DAYS;
	    
	    @Column(name = "NOTICE_32_88_DAYS")
	    private BigDecimal NOTICE_32_88_DAYS;
	    
	    @Column(name = "FD_91_DEPOSIT_DAY")
	    private BigDecimal FD_91_DEPOSIT_DAY;
	    
	    @Column(name = "FD_1_6_MONTHS")
	    private BigDecimal FD_1_6_MONTHS;
	    
	    @Column(name = "FD_7_12_MONTHS")
	    private BigDecimal FD_7_12_MONTHS;
	    
	    @Column(name = "FD_13_18_MONTHS")
	    private BigDecimal FD_13_18_MONTHS;
	    
	    @Column(name = "FD_19_24_MONTHS")
	    private BigDecimal FD_19_24_MONTHS;
	    
	    @Column(name = "FD_OVER_24_MONTHS")
	    private BigDecimal FD_OVER_24_MONTHS;
	    
	    @Column(name = "TOTAL")
	    private BigDecimal TOTAL;
	    
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

		public BigDecimal getCURRENT_AMT() {
			return CURRENT_AMT;
		}

		public void setCURRENT_AMT(BigDecimal cURRENT_AMT) {
			CURRENT_AMT = cURRENT_AMT;
		}

		public BigDecimal getCALL_AMT() {
			return CALL_AMT;
		}

		public void setCALL_AMT(BigDecimal cALL_AMT) {
			CALL_AMT = cALL_AMT;
		}

		public BigDecimal getSAVINGS() {
			return SAVINGS;
		}

		public void setSAVINGS(BigDecimal sAVINGS) {
			SAVINGS = sAVINGS;
		}

		public BigDecimal getNOTICE_0_31_DAYS() {
			return NOTICE_0_31_DAYS;
		}

		public void setNOTICE_0_31_DAYS(BigDecimal nOTICE_0_31_DAYS) {
			NOTICE_0_31_DAYS = nOTICE_0_31_DAYS;
		}

		public BigDecimal getNOTICE_32_88_DAYS() {
			return NOTICE_32_88_DAYS;
		}

		public void setNOTICE_32_88_DAYS(BigDecimal nOTICE_32_88_DAYS) {
			NOTICE_32_88_DAYS = nOTICE_32_88_DAYS;
		}

		public BigDecimal getFD_91_DEPOSIT_DAY() {
			return FD_91_DEPOSIT_DAY;
		}

		public void setFD_91_DEPOSIT_DAY(BigDecimal fD_91_DEPOSIT_DAY) {
			FD_91_DEPOSIT_DAY = fD_91_DEPOSIT_DAY;
		}

		public BigDecimal getFD_1_6_MONTHS() {
			return FD_1_6_MONTHS;
		}

		public void setFD_1_6_MONTHS(BigDecimal fD_1_6_MONTHS) {
			FD_1_6_MONTHS = fD_1_6_MONTHS;
		}

		public BigDecimal getFD_7_12_MONTHS() {
			return FD_7_12_MONTHS;
		}

		public void setFD_7_12_MONTHS(BigDecimal fD_7_12_MONTHS) {
			FD_7_12_MONTHS = fD_7_12_MONTHS;
		}

		public BigDecimal getFD_13_18_MONTHS() {
			return FD_13_18_MONTHS;
		}

		public void setFD_13_18_MONTHS(BigDecimal fD_13_18_MONTHS) {
			FD_13_18_MONTHS = fD_13_18_MONTHS;
		}

		public BigDecimal getFD_19_24_MONTHS() {
			return FD_19_24_MONTHS;
		}

		public void setFD_19_24_MONTHS(BigDecimal fD_19_24_MONTHS) {
			FD_19_24_MONTHS = fD_19_24_MONTHS;
		}

		public BigDecimal getFD_OVER_24_MONTHS() {
			return FD_OVER_24_MONTHS;
		}

		public void setFD_OVER_24_MONTHS(BigDecimal fD_OVER_24_MONTHS) {
			FD_OVER_24_MONTHS = fD_OVER_24_MONTHS;
		}

		public BigDecimal getTOTAL() {
			return TOTAL;
		}

		public void setTOTAL(BigDecimal tOTAL) {
			TOTAL = tOTAL;
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

		public M_INT_RATES_FCA_NEW_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
	    


}
