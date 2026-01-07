package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_BDISB1_ARCHIVALTABLE_DETAIL")
public class BDISB1_Archival_Detail_Entity {
	
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

	    @Column(name = "REPORT_LABLE", length = 10)
	    private String reportLable;
	    
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
	   
	    private String RECORD_NUMBER;               // NUMBER(24,2)
	    private String TITLE;                           // VARCHAR2(100)
	    private String FIRST_NAME;                      // VARCHAR2(100)
	    private String MIDDLE_NAME;                     // VARCHAR2(100)
	    private String SURNAME;                         // VARCHAR2(100)
	    private String PREVIOUS_NAME;                   // VARCHAR2(100)
	    private String GENDER;                          // VARCHAR2(100)
	    private String IDENTIFICATION_TYPE;             // VARCHAR2(100)
	    private String PASSPORT_NUMBER;                 // VARCHAR2(100)
	    @Temporal(TemporalType.DATE)
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date DATE_OF_BIRTH;                     // DATE

	    private String HOME_ADDRESS;                    // VARCHAR2(100)
	    private String POSTAL_ADDRESS;                  // VARCHAR2(100)
	    private String RESIDENCE;                       // VARCHAR2(100)
	    private String EMAIL;                           // VARCHAR2(100)
	    private String LANDLINE;                        // VARCHAR2(100)
	    private String MOBILE_PHONE_NUMBER;             // VARCHAR2(100)
	    private String MOBILE_MONEY_NUMBER;             // VARCHAR2(100)

	    private String PRODUCT_TYPE;                    // VARCHAR2(100)
	    private String ACCOUNT_BY_OWNERSHIP;            // VARCHAR2(100)
	    private String ACCOUNT_NUMBER;                  // VARCHAR2(100)
	    private BigDecimal ACCOUNT_HOLDER_INDICATOR;         // VARCHAR2(100)
	    private String STATUS_OF_ACCOUNT;               // VARCHAR2(100)
	    private String NOT_FIT_FOR_STP;                 // VARCHAR2(100)
	    private String BRANCH_CODE_AND_NAME;             // VARCHAR2(100)

	    private BigDecimal ACCOUNT_BALANCE_IN_PULA;      // NUMBER(24,2)
	    private String CURRENCY_OF_ACCOUNT;              // VARCHAR2(100)
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
		public String getRECORD_NUMBER() {
			return RECORD_NUMBER;
		}
		public void setRECORD_NUMBER(String rECORD_NUMBER) {
			RECORD_NUMBER = rECORD_NUMBER;
		}
		public String getTITLE() {
			return TITLE;
		}
		public void setTITLE(String tITLE) {
			TITLE = tITLE;
		}
		public String getFIRST_NAME() {
			return FIRST_NAME;
		}
		public void setFIRST_NAME(String fIRST_NAME) {
			FIRST_NAME = fIRST_NAME;
		}
		public String getMIDDLE_NAME() {
			return MIDDLE_NAME;
		}
		public void setMIDDLE_NAME(String mIDDLE_NAME) {
			MIDDLE_NAME = mIDDLE_NAME;
		}
		public String getSURNAME() {
			return SURNAME;
		}
		public void setSURNAME(String sURNAME) {
			SURNAME = sURNAME;
		}
		public String getPREVIOUS_NAME() {
			return PREVIOUS_NAME;
		}
		public void setPREVIOUS_NAME(String pREVIOUS_NAME) {
			PREVIOUS_NAME = pREVIOUS_NAME;
		}
		public String getGENDER() {
			return GENDER;
		}
		public void setGENDER(String gENDER) {
			GENDER = gENDER;
		}
		public String getIDENTIFICATION_TYPE() {
			return IDENTIFICATION_TYPE;
		}
		public void setIDENTIFICATION_TYPE(String iDENTIFICATION_TYPE) {
			IDENTIFICATION_TYPE = iDENTIFICATION_TYPE;
		}
		public String getPASSPORT_NUMBER() {
			return PASSPORT_NUMBER;
		}
		public void setPASSPORT_NUMBER(String pASSPORT_NUMBER) {
			PASSPORT_NUMBER = pASSPORT_NUMBER;
		}
		public Date getDATE_OF_BIRTH() {
			return DATE_OF_BIRTH;
		}
		public void setDATE_OF_BIRTH(Date dATE_OF_BIRTH) {
			DATE_OF_BIRTH = dATE_OF_BIRTH;
		}
		public String getHOME_ADDRESS() {
			return HOME_ADDRESS;
		}
		public void setHOME_ADDRESS(String hOME_ADDRESS) {
			HOME_ADDRESS = hOME_ADDRESS;
		}
		public String getPOSTAL_ADDRESS() {
			return POSTAL_ADDRESS;
		}
		public void setPOSTAL_ADDRESS(String pOSTAL_ADDRESS) {
			POSTAL_ADDRESS = pOSTAL_ADDRESS;
		}
		public String getRESIDENCE() {
			return RESIDENCE;
		}
		public void setRESIDENCE(String rESIDENCE) {
			RESIDENCE = rESIDENCE;
		}
		public String getEMAIL() {
			return EMAIL;
		}
		public void setEMAIL(String eMAIL) {
			EMAIL = eMAIL;
		}
		public String getLANDLINE() {
			return LANDLINE;
		}
		public void setLANDLINE(String lANDLINE) {
			LANDLINE = lANDLINE;
		}
		public String getMOBILE_PHONE_NUMBER() {
			return MOBILE_PHONE_NUMBER;
		}
		public void setMOBILE_PHONE_NUMBER(String mOBILE_PHONE_NUMBER) {
			MOBILE_PHONE_NUMBER = mOBILE_PHONE_NUMBER;
		}
		public String getMOBILE_MONEY_NUMBER() {
			return MOBILE_MONEY_NUMBER;
		}
		public void setMOBILE_MONEY_NUMBER(String mOBILE_MONEY_NUMBER) {
			MOBILE_MONEY_NUMBER = mOBILE_MONEY_NUMBER;
		}
		public String getPRODUCT_TYPE() {
			return PRODUCT_TYPE;
		}
		public void setPRODUCT_TYPE(String pRODUCT_TYPE) {
			PRODUCT_TYPE = pRODUCT_TYPE;
		}
		public String getACCOUNT_BY_OWNERSHIP() {
			return ACCOUNT_BY_OWNERSHIP;
		}
		public void setACCOUNT_BY_OWNERSHIP(String aCCOUNT_BY_OWNERSHIP) {
			ACCOUNT_BY_OWNERSHIP = aCCOUNT_BY_OWNERSHIP;
		}
		public String getACCOUNT_NUMBER() {
			return ACCOUNT_NUMBER;
		}
		public void setACCOUNT_NUMBER(String aCCOUNT_NUMBER) {
			ACCOUNT_NUMBER = aCCOUNT_NUMBER;
		}
		public BigDecimal getACCOUNT_HOLDER_INDICATOR() {
			return ACCOUNT_HOLDER_INDICATOR;
		}
		public void setACCOUNT_HOLDER_INDICATOR(BigDecimal aCCOUNT_HOLDER_INDICATOR) {
			ACCOUNT_HOLDER_INDICATOR = aCCOUNT_HOLDER_INDICATOR;
		}
		public String getSTATUS_OF_ACCOUNT() {
			return STATUS_OF_ACCOUNT;
		}
		public void setSTATUS_OF_ACCOUNT(String sTATUS_OF_ACCOUNT) {
			STATUS_OF_ACCOUNT = sTATUS_OF_ACCOUNT;
		}
		public String getNOT_FIT_FOR_STP() {
			return NOT_FIT_FOR_STP;
		}
		public void setNOT_FIT_FOR_STP(String nOT_FIT_FOR_STP) {
			NOT_FIT_FOR_STP = nOT_FIT_FOR_STP;
		}
		public String getBRANCH_CODE_AND_NAME() {
			return BRANCH_CODE_AND_NAME;
		}
		public void setBRANCH_CODE_AND_NAME(String bRANCH_CODE_AND_NAME) {
			BRANCH_CODE_AND_NAME = bRANCH_CODE_AND_NAME;
		}
		public BigDecimal getACCOUNT_BALANCE_IN_PULA() {
			return ACCOUNT_BALANCE_IN_PULA;
		}
		public void setACCOUNT_BALANCE_IN_PULA(BigDecimal aCCOUNT_BALANCE_IN_PULA) {
			ACCOUNT_BALANCE_IN_PULA = aCCOUNT_BALANCE_IN_PULA;
		}
		public String getCURRENCY_OF_ACCOUNT() {
			return CURRENCY_OF_ACCOUNT;
		}
		public void setCURRENCY_OF_ACCOUNT(String cURRENCY_OF_ACCOUNT) {
			CURRENCY_OF_ACCOUNT = cURRENCY_OF_ACCOUNT;
		}
		public BigDecimal getEXCHANGE_RATE() {
			return EXCHANGE_RATE;
		}
		public void setEXCHANGE_RATE(BigDecimal eXCHANGE_RATE) {
			EXCHANGE_RATE = eXCHANGE_RATE;
		}
		public BDISB1_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
		
    

}
