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
@Table(name = "BRRS_M_LA2_ARCHIVALTABLE_DETAIL")
public class M_LA2_Archival_Detail_Entity {

	 @Id
	    @Column(name = "SNO", length = 10)
	    private String sno;

	    @Column(name = "CUST_ID", length = 100)
	    private String custId;

	    @Column(name = "ACCT_NUMBER", length = 100)
	    private String acctNumber;

	    @Column(name = "ACCT_NAME", length = 100)
	    private String acctName;

	    @Column(name = "DATA_TYPE", length = 100)
	    private String dataType;

	    @Column(name = "REPORT_ADDL_CRITERIA_1", length = 10)
	    private String reportAddlCriteria1;

	    @Column(name = "REPORT_REMARKS", length = 100)
	    private String reportRemarks;

	    @Column(name = "MODIFICATION_REMARKS", length = 100)
	    private String modificationRemarks;

	    @Column(name = "DATA_ENTRY_VERSION", length = 100)
	    private String dataEntryVersion;

	    @Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
	    private BigDecimal acctBalanceInPula;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "REPORT_DATE")
	    private Date reportDate;

	    @Column(name = "REPORT_NAME", length = 50)
	    private String reportName;

	    @Column(name = "CREATE_USER", length = 50)
	    private String createUser;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "CREATE_TIME")
	    private Date createTime;

	    @Column(name = "MODIFY_USER", length = 50)
	    private String modifyUser;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "MODIFY_TIME")
	    private Date modifyTime;

	    @Column(name = "VERIFY_USER", length = 50)
	    private String verifyUser;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "VERIFY_TIME")
	    private Date verifyTime;

	    @Column(name = "ENTITY_FLG", length = 1)
	    private Character entityFlg;

	    @Column(name = "MODIFY_FLG", length = 1)
	    private Character modifyFlg;

	    @Column(name = "DEL_FLG", length = 1)
	    private Character delFlg;

	    @Column(name = "REPORT_ADDL_CRITERIA_2", length = 10)
	    private String reportAddlCriteria2;

	    @Column(name = "REPORT_ADDL_CRITERIA_3", length = 10)
	    private String reportAddlCriteria3;

	    @Column(name = "SEGMENT", length = 150)
	    private String segment;

	    @Column(name = "INT_BUCKET", length = 26)
	    private String intBucket;

	    @Column(name = "FACILITY", length = 26)
	    private String facility;

	    @Column(name = "MAT_BUCKET", length = 26)
	    private String matBucket;

	    @Column(name = "SANCTION_LIMIT", precision = 24, scale = 4)
	    private BigDecimal sanctionLimit;

	    @Column(name = "REPORT_LABEL", length = 10)
	    private String reportLabel;

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

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
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

		public Character getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(Character entityFlg) {
			this.entityFlg = entityFlg;
		}

		public Character getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(Character modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public Character getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(Character delFlg) {
			this.delFlg = delFlg;
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

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getIntBucket() {
			return intBucket;
		}

		public void setIntBucket(String intBucket) {
			this.intBucket = intBucket;
		}

		public String getFacility() {
			return facility;
		}

		public void setFacility(String facility) {
			this.facility = facility;
		}

		public String getMatBucket() {
			return matBucket;
		}

		public void setMatBucket(String matBucket) {
			this.matBucket = matBucket;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

	    
	    	public M_LA2_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
    
    
}