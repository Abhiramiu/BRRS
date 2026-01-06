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
@Table(name = "BRRS_BDISB2_ARCHIVALTABLE_DETAIL")
public class BDISB2_Archival_Detail_Entity {

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

    @Column(name = "REPORT_LABEL", length = 10)
    private String reportLabel;
    
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
    private String entityFlg;

    @Column(name = "MODIFY_FLG", length = 1)
    private String modifyFlg;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

	private String BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String COMPANY_NAME;
	private String COMPANY_REG_NUM;
	private String BUSINEES_PHY_ADDRESS;
	private String POSTAL_ADDRESS;
	private String COUNTRY_OF_REG;
	private String COMPANY_EMAIL;
	private String COMPANY_LANDLINE;
	private String COMPANY_MOB_PHONE_NUM;
	private String PRODUCT_TYPE;
	private BigDecimal ACCT_NUM;
	private String STATUS_OF_ACCT;
	private String ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String ACCT_BRANCH;
	private BigDecimal ACCT_BALANCE_PULA;
	private String CURRENCY_OF_ACCT;
	private BigDecimal EXCHANGE_RATE;
    
	

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



	public String getBANK_SPEC_SINGLE_CUST_REC_NUM() {
		return BANK_SPEC_SINGLE_CUST_REC_NUM;
	}



	public void setBANK_SPEC_SINGLE_CUST_REC_NUM(String bANK_SPEC_SINGLE_CUST_REC_NUM) {
		BANK_SPEC_SINGLE_CUST_REC_NUM = bANK_SPEC_SINGLE_CUST_REC_NUM;
	}



	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}



	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}



	public String getCOMPANY_REG_NUM() {
		return COMPANY_REG_NUM;
	}



	public void setCOMPANY_REG_NUM(String cOMPANY_REG_NUM) {
		COMPANY_REG_NUM = cOMPANY_REG_NUM;
	}



	public String getBUSINEES_PHY_ADDRESS() {
		return BUSINEES_PHY_ADDRESS;
	}



	public void setBUSINEES_PHY_ADDRESS(String bUSINEES_PHY_ADDRESS) {
		BUSINEES_PHY_ADDRESS = bUSINEES_PHY_ADDRESS;
	}



	public String getPOSTAL_ADDRESS() {
		return POSTAL_ADDRESS;
	}



	public void setPOSTAL_ADDRESS(String pOSTAL_ADDRESS) {
		POSTAL_ADDRESS = pOSTAL_ADDRESS;
	}



	public String getCOUNTRY_OF_REG() {
		return COUNTRY_OF_REG;
	}



	public void setCOUNTRY_OF_REG(String cOUNTRY_OF_REG) {
		COUNTRY_OF_REG = cOUNTRY_OF_REG;
	}



	public String getCOMPANY_EMAIL() {
		return COMPANY_EMAIL;
	}



	public void setCOMPANY_EMAIL(String cOMPANY_EMAIL) {
		COMPANY_EMAIL = cOMPANY_EMAIL;
	}



	public String getCOMPANY_LANDLINE() {
		return COMPANY_LANDLINE;
	}



	public void setCOMPANY_LANDLINE(String cOMPANY_LANDLINE) {
		COMPANY_LANDLINE = cOMPANY_LANDLINE;
	}



	public String getCOMPANY_MOB_PHONE_NUM() {
		return COMPANY_MOB_PHONE_NUM;
	}



	public void setCOMPANY_MOB_PHONE_NUM(String cOMPANY_MOB_PHONE_NUM) {
		COMPANY_MOB_PHONE_NUM = cOMPANY_MOB_PHONE_NUM;
	}



	public String getPRODUCT_TYPE() {
		return PRODUCT_TYPE;
	}



	public void setPRODUCT_TYPE(String pRODUCT_TYPE) {
		PRODUCT_TYPE = pRODUCT_TYPE;
	}



	public BigDecimal getACCT_NUM() {
		return ACCT_NUM;
	}



	public void setACCT_NUM(BigDecimal aCCT_NUM) {
		ACCT_NUM = aCCT_NUM;
	}



	public String getSTATUS_OF_ACCT() {
		return STATUS_OF_ACCT;
	}



	public void setSTATUS_OF_ACCT(String sTATUS_OF_ACCT) {
		STATUS_OF_ACCT = sTATUS_OF_ACCT;
	}



	public String getACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}



	public void setACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String aCCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = aCCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}



	public String getACCT_BRANCH() {
		return ACCT_BRANCH;
	}



	public void setACCT_BRANCH(String aCCT_BRANCH) {
		ACCT_BRANCH = aCCT_BRANCH;
	}



	public BigDecimal getACCT_BALANCE_PULA() {
		return ACCT_BALANCE_PULA;
	}



	public void setACCT_BALANCE_PULA(BigDecimal aCCT_BALANCE_PULA) {
		ACCT_BALANCE_PULA = aCCT_BALANCE_PULA;
	}



	public String getCURRENCY_OF_ACCT() {
		return CURRENCY_OF_ACCT;
	}



	public void setCURRENCY_OF_ACCT(String cURRENCY_OF_ACCT) {
		CURRENCY_OF_ACCT = cURRENCY_OF_ACCT;
	}



	public BigDecimal getEXCHANGE_RATE() {
		return EXCHANGE_RATE;
	}



	public void setEXCHANGE_RATE(BigDecimal eXCHANGE_RATE) {
		EXCHANGE_RATE = eXCHANGE_RATE;
	}



	public BDISB2_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
    
    
}