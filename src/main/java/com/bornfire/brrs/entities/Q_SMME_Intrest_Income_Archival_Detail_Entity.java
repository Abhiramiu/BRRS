package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL")
public class Q_SMME_Intrest_Income_Archival_Detail_Entity {

    @Id
    @Column(name = "CUST_ID")
    private String custId;

    @Column(name = "ACCT_NUMBER")
    private String acctNumber;

    @Column(name = "ACCT_NAME")
    private String acctName;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "COLUMN_ID")
    private String columnId;

    @Column(name = "ROW_ID")
    private String rowId;

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

   @Column(name = "SEGMENT")
    private String segment;

    @Column(name = "CONSTITUTION_CODE")
    private String constitutionCode;

    @Column(name = "SMME")
    private String smme;

    @Column(name = "COLUMN_ID_1")
    private String columnId1;

    @Column(name = "REPORT_NAME_1")
    private String reportName1;

    @Column(name = "INTEREST_JULY_2025", precision = 24, scale = 2)
    private BigDecimal interestJuly2025;

    @Column(name = "MONTHLY_INTEREST", precision = 24, scale = 2)
private BigDecimal monthlyInterest;

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

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
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


    
    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getConstitutionCode() {
        return constitutionCode;
    }

    public void setConstitutionCode(String constitutionCode) {
        this.constitutionCode = constitutionCode;
    }

    public String getSmme() {
        return smme;
    }

    public void setSmme(String smme) {
        this.smme = smme;
    }

    public String getColumnId1() {
        return columnId1;
    }

    public void setColumnId1(String columnId1) {
        this.columnId1 = columnId1;
    }

    public String getReportName1() {
        return reportName1;
    }

    public void setReportName1(String reportName1) {
        this.reportName1 = reportName1;
    }

    public BigDecimal getInterestJuly2025() {
        return interestJuly2025;
    }

    public void setInterestJuly2025(BigDecimal interestJuly2025) {
        this.interestJuly2025 = interestJuly2025;
    }
public BigDecimal getMonthlyInterest() {
    return monthlyInterest;
}

public void setMonthlyInterest(BigDecimal monthlyInterest) {
    this.monthlyInterest = monthlyInterest;
}
    public Q_SMME_Intrest_Income_Archival_Detail_Entity() {
        super();
    }

}