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
@Table(name = "GENERAL_MASTER_TABLE")
public class GeneralMasterEntity {

    // =================== ID ===================
    @Id
    @Column(name = "SNO")
    private String SNO;

    // =================== ACCOUNT INFO ===================
    @Column(name = "ACCOUNT_NO")
    private String accountNo;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "SOL_ID")
    private String solId;

    @Column(name = "GENDER")
    private String gender;

    // =================== DATE COLUMNS ===================
    @Temporal(TemporalType.DATE)
    @Column(name = "REPORT_DATE")
    private Date reportDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "ACCT_OPEN_DATE")
    private Date acctOpenDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "ACCT_CLOSE_DATE")
    private Date acctCloseDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "MATURITY_DATE")
    private Date maturityDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "REFERENCE_DATE")
    private Date referenceDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPLOAD_DATE")
    private Date uploadDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_INTEREST_DEBIT_DATE")
    private Date lastInterestDebitDate;

    // =================== REPORTING ===================
    @Column(name = "REPORT_CODE")
    private String reportCode;

    @Column(name = "VERSION")
    private Integer version;

    // =================== MCBL ===================
    @Column(name = "MCBL_GL_CODE")
    private String mcblGlCode;

    @Column(name = "MCBL_DESCRIPTION")
    private String mcblDescription;

    @Column(name = "MCBL_DEBIT_BALANCE")
    private BigDecimal mcblDebitBalance;

    @Column(name = "MCBL_CREDIT_BALANCE")
    private BigDecimal mcblCreditBalance;

    @Column(name = "MCBL_DEBIT_EQUIVALENT")
    private BigDecimal mcblDebitEquivalent;

    @Column(name = "MCBL_CREDIT_EQUIVALENT")
    private BigDecimal mcblCreditEquivalent;

    // =================== SCHEME ===================
    @Column(name = "SCHM_CODE")
    private String schmCode;

    @Column(name = "SCHM_DESC")
    private String schmDesc;

    @Column(name = "TYPE_OF_ACCOUNTS")
    private String typeOfAccounts;

    @Column(name = "SEGMENT")
    private String segment;

    @Column(name = "SCHEME")
    private String scheme;

    // =================== FINANCIALS ===================
    @Column(name = "BALANCE_AS_ON")
    private BigDecimal balanceAsOn;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BAL_EQUI_TO_BWP")
    private BigDecimal balEquiToBwp;

    @Column(name = "RATE_OF_INTEREST")
    private BigDecimal rateOfInterest;

    @Column(name = "HUNDRED")
    private BigDecimal hundred;

    @Column(name = "MATURITY_AMOUNT")
    private BigDecimal maturityAmount;

    @Column(name = "EFFECTIVE_INTEREST_RATE")
    private BigDecimal effectiveInterestRate;

    @Column(name = "AMOUNT_DEPOSITED")
    private BigDecimal amountDeposited;

    @Column(name = "OUTSTANDING_BALANCE")
    private BigDecimal outstandingBalance;

    @Column(name = "OUSTNDNG_BAL_UGX")
    private BigDecimal oustndngBalUgx;

    @Column(name = "CR_PREF_INT_RATE")
    private BigDecimal crPrefIntRate;

    @Column(name = "DIFFERENCE")
    private BigDecimal difference;

    @Column(name = "DAYS")
    private BigDecimal days;

    @Column(name = "APPROVED_LIMIT")
    private BigDecimal approvedLimit;

    @Column(name = "SANCTION_LIMIT")
    private BigDecimal sanctionLimit;

    @Column(name = "DISBURSED_AMT")
    private BigDecimal disbursedAmt;

    @Column(name = "ACCRUED_INT_AMT")
    private BigDecimal accruedIntAmt;

    @Column(name = "MONTHLY_INTEREST")
    private BigDecimal monthlyInterest;

    @Column(name = "PROVISION")
    private BigDecimal provision;

    @Column(name = "UNDRAWN")
    private BigDecimal undrawn;

    @Column(name = "ECL_PROVISION")
    private BigDecimal eclProvision;

    @Column(name = "MAT_BUCKET")
    private BigDecimal matBucket;

    @Column(name = "EMI")
    private BigDecimal emi;

    // =================== STATUS & CLASSIFICATION ===================
    @Column(name = "STATUS")
    private String status;

    @Column(name = "GL_SUB_HEAD_CODE")
    private String glSubHeadCode;

    @Column(name = "GL_SUB_HEAD_DESC")
    private String glSubHeadDesc;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "ACCT_CLS_FLG")
    private String acctClsFlg;

    @Column(name = "CLASSIFICATION_CODE")
    private String classificationCode;

    @Column(name = "CONSTITUTION_CODE")
    private String constitutionCode;

    @Column(name = "TENOR_MONTH")
    private BigDecimal tenorMonth;

    @Column(name = "FACILITY")
    private String facility;

    @Column(name = "PAST_DUE")
    private String pastDue;

    @Column(name = "PAST_DUE_DAYS")
    private BigDecimal pastDueDays;

    @Column(name = "ASSET")
    private String asset;

    @Column(name = "UNSECURED")
    private String unsecured;

    @Column(name = "INT_BUCKET")
    private String intBucket;

    @Column(name = "STAFF")
    private String staff;

    @Column(name = "SMME")
    private String smme;

    @Column(name = "LABOD")
    private String labod;

    @Column(name = "NEW_AC")
    private String newAc;

    @Column(name = "SECTOR")
    private String sector;

    @Column(name = "STAGE")
    private String stage;

    @Column(name = "PERIOD_DAYS")
    private BigDecimal periodDays;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "BRANCH_CODE")
    private String branchCode;

    // =================== BUCKETS ===================
    @Column(name = "LIQGAP_BUCKET")
    private String liqgapBucket;

    @Column(name = "MDEP2A_BUCKET")
    private String mdep2aBucket;

    @Column(name = "MDEP_BUCKET")
    private String mdepBucket;

    // =================== AUDIT COLUMNS ===================
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENTRY_TIME")
    private Date entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VERIFY_TIME")
    private Date verifyTime;

    @Column(name = "ENTRY_USER")
    private String entryUser;

    @Column(name = "MODIFY_USER")
    private String modifyUser;

    @Column(name = "VERIFY_USER")
    private String verifyUser;

    @Column(name = "DEL_USER")
    private String delUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEL_TIME")
    private Date delTime;

    // =================== FLAGS ===================
    @Column(name = "ENTRY_FLG")
    private String entryFlg;

    @Column(name = "MODIFY_FLG")
    private String modifyFlg;

    @Column(name = "VERIFY_FLG")
    private String verifyFlg;

    @Column(name = "DEL_FLG")
    private String delFlg;

    @Column(name = "MCBL_FLG")
    private String mcblFlg;

    @Column(name = "BLBF_FLG")
    private String blbfFlg;

    @Column(name = "BDGF_FLG")
    private String bdgfFlg;

    @Column(name = "BFDB_FLG")
    private String bfdbFlg;

    

	



	public String getSNO() {
		return SNO;
	}



	public void setSNO(String sNO) {
		SNO = sNO;
	}



	public String getAccountNo() {
		return accountNo;
	}



	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}



	public String getCustomerId() {
		return customerId;
	}



	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}



	public String getCustomerName() {
		return customerName;
	}



	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	public String getSolId() {
		return solId;
	}



	public void setSolId(String solId) {
		this.solId = solId;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public Date getReportDate() {
		return reportDate;
	}



	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}



	public Date getAcctOpenDate() {
		return acctOpenDate;
	}



	public void setAcctOpenDate(Date acctOpenDate) {
		this.acctOpenDate = acctOpenDate;
	}



	public Date getAcctCloseDate() {
		return acctCloseDate;
	}



	public void setAcctCloseDate(Date acctCloseDate) {
		this.acctCloseDate = acctCloseDate;
	}



	public Date getMaturityDate() {
		return maturityDate;
	}



	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}



	public Date getReferenceDate() {
		return referenceDate;
	}



	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}



	public Date getUploadDate() {
		return uploadDate;
	}



	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}



	public Date getLastInterestDebitDate() {
		return lastInterestDebitDate;
	}



	public void setLastInterestDebitDate(Date lastInterestDebitDate) {
		this.lastInterestDebitDate = lastInterestDebitDate;
	}



	public String getReportCode() {
		return reportCode;
	}



	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}



	public Integer getVersion() {
		return version;
	}



	public void setVersion(Integer version) {
		this.version = version;
	}



	public String getMcblGlCode() {
		return mcblGlCode;
	}



	public void setMcblGlCode(String mcblGlCode) {
		this.mcblGlCode = mcblGlCode;
	}



	public String getMcblDescription() {
		return mcblDescription;
	}



	public void setMcblDescription(String mcblDescription) {
		this.mcblDescription = mcblDescription;
	}



	public BigDecimal getMcblDebitBalance() {
		return mcblDebitBalance;
	}



	public void setMcblDebitBalance(BigDecimal mcblDebitBalance) {
		this.mcblDebitBalance = mcblDebitBalance;
	}



	public BigDecimal getMcblCreditBalance() {
		return mcblCreditBalance;
	}



	public void setMcblCreditBalance(BigDecimal mcblCreditBalance) {
		this.mcblCreditBalance = mcblCreditBalance;
	}



	public BigDecimal getMcblDebitEquivalent() {
		return mcblDebitEquivalent;
	}



	public void setMcblDebitEquivalent(BigDecimal mcblDebitEquivalent) {
		this.mcblDebitEquivalent = mcblDebitEquivalent;
	}



	public BigDecimal getMcblCreditEquivalent() {
		return mcblCreditEquivalent;
	}



	public void setMcblCreditEquivalent(BigDecimal mcblCreditEquivalent) {
		this.mcblCreditEquivalent = mcblCreditEquivalent;
	}



	public String getSchmCode() {
		return schmCode;
	}



	public void setSchmCode(String schmCode) {
		this.schmCode = schmCode;
	}



	public String getSchmDesc() {
		return schmDesc;
	}



	public void setSchmDesc(String schmDesc) {
		this.schmDesc = schmDesc;
	}



	public String getTypeOfAccounts() {
		return typeOfAccounts;
	}



	public void setTypeOfAccounts(String typeOfAccounts) {
		this.typeOfAccounts = typeOfAccounts;
	}



	public String getSegment() {
		return segment;
	}



	public void setSegment(String segment) {
		this.segment = segment;
	}



	public String getScheme() {
		return scheme;
	}



	public void setScheme(String scheme) {
		this.scheme = scheme;
	}



	public BigDecimal getBalanceAsOn() {
		return balanceAsOn;
	}



	public void setBalanceAsOn(BigDecimal balanceAsOn) {
		this.balanceAsOn = balanceAsOn;
	}



	public String getCurrency() {
		return currency;
	}



	public void setCurrency(String currency) {
		this.currency = currency;
	}



	public BigDecimal getBalEquiToBwp() {
		return balEquiToBwp;
	}



	public void setBalEquiToBwp(BigDecimal balEquiToBwp) {
		this.balEquiToBwp = balEquiToBwp;
	}



	public BigDecimal getRateOfInterest() {
		return rateOfInterest;
	}



	public void setRateOfInterest(BigDecimal rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}



	public BigDecimal getHundred() {
		return hundred;
	}



	public void setHundred(BigDecimal hundred) {
		this.hundred = hundred;
	}



	public BigDecimal getMaturityAmount() {
		return maturityAmount;
	}



	public void setMaturityAmount(BigDecimal maturityAmount) {
		this.maturityAmount = maturityAmount;
	}



	public BigDecimal getEffectiveInterestRate() {
		return effectiveInterestRate;
	}



	public void setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
		this.effectiveInterestRate = effectiveInterestRate;
	}



	public BigDecimal getAmountDeposited() {
		return amountDeposited;
	}



	public void setAmountDeposited(BigDecimal amountDeposited) {
		this.amountDeposited = amountDeposited;
	}



	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}



	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}



	public BigDecimal getOustndngBalUgx() {
		return oustndngBalUgx;
	}



	public void setOustndngBalUgx(BigDecimal oustndngBalUgx) {
		this.oustndngBalUgx = oustndngBalUgx;
	}



	public BigDecimal getCrPrefIntRate() {
		return crPrefIntRate;
	}



	public void setCrPrefIntRate(BigDecimal crPrefIntRate) {
		this.crPrefIntRate = crPrefIntRate;
	}



	public BigDecimal getDifference() {
		return difference;
	}



	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}



	public BigDecimal getDays() {
		return days;
	}



	public void setDays(BigDecimal days) {
		this.days = days;
	}



	public BigDecimal getApprovedLimit() {
		return approvedLimit;
	}



	public void setApprovedLimit(BigDecimal approvedLimit) {
		this.approvedLimit = approvedLimit;
	}



	public BigDecimal getSanctionLimit() {
		return sanctionLimit;
	}



	public void setSanctionLimit(BigDecimal sanctionLimit) {
		this.sanctionLimit = sanctionLimit;
	}



	public BigDecimal getDisbursedAmt() {
		return disbursedAmt;
	}



	public void setDisbursedAmt(BigDecimal disbursedAmt) {
		this.disbursedAmt = disbursedAmt;
	}



	public BigDecimal getAccruedIntAmt() {
		return accruedIntAmt;
	}



	public void setAccruedIntAmt(BigDecimal accruedIntAmt) {
		this.accruedIntAmt = accruedIntAmt;
	}



	public BigDecimal getMonthlyInterest() {
		return monthlyInterest;
	}



	public void setMonthlyInterest(BigDecimal monthlyInterest) {
		this.monthlyInterest = monthlyInterest;
	}



	public BigDecimal getProvision() {
		return provision;
	}



	public void setProvision(BigDecimal provision) {
		this.provision = provision;
	}



	public BigDecimal getUndrawn() {
		return undrawn;
	}



	public void setUndrawn(BigDecimal undrawn) {
		this.undrawn = undrawn;
	}



	public BigDecimal getEclProvision() {
		return eclProvision;
	}



	public void setEclProvision(BigDecimal eclProvision) {
		this.eclProvision = eclProvision;
	}



	public BigDecimal getMatBucket() {
		return matBucket;
	}



	public void setMatBucket(BigDecimal matBucket) {
		this.matBucket = matBucket;
	}



	public BigDecimal getEmi() {
		return emi;
	}



	public void setEmi(BigDecimal emi) {
		this.emi = emi;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getGlSubHeadCode() {
		return glSubHeadCode;
	}



	public void setGlSubHeadCode(String glSubHeadCode) {
		this.glSubHeadCode = glSubHeadCode;
	}



	public String getGlSubHeadDesc() {
		return glSubHeadDesc;
	}



	public void setGlSubHeadDesc(String glSubHeadDesc) {
		this.glSubHeadDesc = glSubHeadDesc;
	}



	public String getPeriod() {
		return period;
	}



	public void setPeriod(String period) {
		this.period = period;
	}



	public String getAcctClsFlg() {
		return acctClsFlg;
	}



	public void setAcctClsFlg(String acctClsFlg) {
		this.acctClsFlg = acctClsFlg;
	}



	public String getClassificationCode() {
		return classificationCode;
	}



	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}



	public String getConstitutionCode() {
		return constitutionCode;
	}



	public void setConstitutionCode(String constitutionCode) {
		this.constitutionCode = constitutionCode;
	}



	public BigDecimal getTenorMonth() {
		return tenorMonth;
	}



	public void setTenorMonth(BigDecimal tenorMonth) {
		this.tenorMonth = tenorMonth;
	}



	public String getFacility() {
		return facility;
	}



	public void setFacility(String facility) {
		this.facility = facility;
	}



	public String getPastDue() {
		return pastDue;
	}



	public void setPastDue(String pastDue) {
		this.pastDue = pastDue;
	}



	public BigDecimal getPastDueDays() {
		return pastDueDays;
	}



	public void setPastDueDays(BigDecimal pastDueDays) {
		this.pastDueDays = pastDueDays;
	}



	public String getAsset() {
		return asset;
	}



	public void setAsset(String asset) {
		this.asset = asset;
	}



	public String getUnsecured() {
		return unsecured;
	}



	public void setUnsecured(String unsecured) {
		this.unsecured = unsecured;
	}



	public String getIntBucket() {
		return intBucket;
	}



	public void setIntBucket(String intBucket) {
		this.intBucket = intBucket;
	}



	public String getStaff() {
		return staff;
	}



	public void setStaff(String staff) {
		this.staff = staff;
	}



	public String getSmme() {
		return smme;
	}



	public void setSmme(String smme) {
		this.smme = smme;
	}



	public String getLabod() {
		return labod;
	}



	public void setLabod(String labod) {
		this.labod = labod;
	}



	public String getNewAc() {
		return newAc;
	}



	public void setNewAc(String newAc) {
		this.newAc = newAc;
	}



	public String getSector() {
		return sector;
	}



	public void setSector(String sector) {
		this.sector = sector;
	}



	public String getStage() {
		return stage;
	}



	public void setStage(String stage) {
		this.stage = stage;
	}



	public BigDecimal getPeriodDays() {
		return periodDays;
	}



	public void setPeriodDays(BigDecimal periodDays) {
		this.periodDays = periodDays;
	}



	public String getBranchName() {
		return branchName;
	}



	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}



	public String getBranchCode() {
		return branchCode;
	}



	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}



	public String getLiqgapBucket() {
		return liqgapBucket;
	}



	public void setLiqgapBucket(String liqgapBucket) {
		this.liqgapBucket = liqgapBucket;
	}



	public String getMdep2aBucket() {
		return mdep2aBucket;
	}



	public void setMdep2aBucket(String mdep2aBucket) {
		this.mdep2aBucket = mdep2aBucket;
	}



	public String getMdepBucket() {
		return mdepBucket;
	}



	public void setMdepBucket(String mdepBucket) {
		this.mdepBucket = mdepBucket;
	}



	public Date getEntryTime() {
		return entryTime;
	}



	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}



	public Date getModifyTime() {
		return modifyTime;
	}



	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}



	public Date getVerifyTime() {
		return verifyTime;
	}



	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}



	public String getEntryUser() {
		return entryUser;
	}



	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}



	public String getModifyUser() {
		return modifyUser;
	}



	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}



	public String getVerifyUser() {
		return verifyUser;
	}



	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}



	public String getDelUser() {
		return delUser;
	}



	public void setDelUser(String delUser) {
		this.delUser = delUser;
	}



	public Date getDelTime() {
		return delTime;
	}



	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}



	public String getEntryFlg() {
		return entryFlg;
	}



	public void setEntryFlg(String entryFlg) {
		this.entryFlg = entryFlg;
	}



	public String getModifyFlg() {
		return modifyFlg;
	}



	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}



	public String getVerifyFlg() {
		return verifyFlg;
	}



	public void setVerifyFlg(String verifyFlg) {
		this.verifyFlg = verifyFlg;
	}



	public String getDelFlg() {
		return delFlg;
	}



	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}



	public String getMcblFlg() {
		return mcblFlg;
	}



	public void setMcblFlg(String mcblFlg) {
		this.mcblFlg = mcblFlg;
	}



	public String getBlbfFlg() {
		return blbfFlg;
	}



	public void setBlbfFlg(String blbfFlg) {
		this.blbfFlg = blbfFlg;
	}



	public String getBdgfFlg() {
		return bdgfFlg;
	}



	public void setBdgfFlg(String bdgfFlg) {
		this.bdgfFlg = bdgfFlg;
	}



	public String getBfdbFlg() {
		return bfdbFlg;
	}



	public void setBfdbFlg(String bfdbFlg) {
		this.bfdbFlg = bfdbFlg;
	}



	public GeneralMasterEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

    
    
}
