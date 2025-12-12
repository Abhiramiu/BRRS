package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "BRRS_EXPANDED_REGU_BS_DETAILTABLE", schema = "BRRS")
public class Expanded_Regu_BS_Detail_Entity {

    @Column(name = "CUST_ID", length = 100)
    private String custId;

    @Id
    @Column(name = "ACCT_NUMBER", length = 100)
    private String acctNumber;

    @Column(name = "ACCT_NAME", length = 100)
    private String acctName;

    @Column(name = "DATA_TYPE", length = 100)
    private String dataType;

    @Column(name = "REPORT_LABEL", length = 100)
    private String reportLabel;

    @Column(name = "REPORT_ADDL_CRITERIA_1", length = 100)
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

    @Column(name = "REPORT_NAME", length = 100)
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

    /* ===== Constructors ===== */

    public Expanded_Regu_BS_Detail_Entity() {
        super();
    }

    /* ===== Getters & Setters ===== */

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
}
