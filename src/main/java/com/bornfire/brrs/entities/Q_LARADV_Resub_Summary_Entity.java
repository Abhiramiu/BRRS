package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_Q_LARADV_RESUB_SUMMARYTABLE")
public class Q_LARADV_Resub_Summary_Entity {
	
	@Id
	@GeneratedValue(
	        strategy = GenerationType.SEQUENCE,
	        generator = "brrs_q_laradv_resub_summarytable_seq_gen"
	)
	@SequenceGenerator(
	        name = "brrs_q_laradv_resub_summarytable_seq_gen",
	        sequenceName = "BRRS_Q_LARADV_RESUB_SUMMARYTABLE_SNO_SEQ",
	        allocationSize = 1
	)
	@Column(name = "SNO")
	private Long sno;

	    @Column(name = "GROUP_NAME")
	    private String groupName;

	    @Column(name = "CUSTOMER_GROUP_NAME")
	    private String customerGroupName;

	    @Column(name = "SECTOR_TYPE")
	    private String sectorType;

	    @Column(name = "FACILITY_TYPE")
	    private String facilityType;

	    @Column(name = "ORIGINAL_AMOUNT")
	    private BigDecimal originalAmount;

	    @Column(name = "UTILISATION_OUTSTANDING_BALANCE")
	    private BigDecimal utilisationOutstandingBalance;

	    @Temporal(TemporalType.DATE)
	    @DateTimeFormat(pattern = "dd/MM/yyyy")
		@Column(name = "EFFECTIVE_DATE")
	    private Date effectiveDate;

	    @Column(name = "REPAYMENT_PERIOD")
	    private String repaymentPeriod;

	    @Column(name = "PERFORMANCE_STATUS")
	    private String performanceStatus;

	    @Column(name = "SECURITY_DETAILS")
	    private String securityDetails;

	    @Column(name = "BOARD_APPROVAL")
	    private String boardApproval;

	    @Column(name = "INTEREST_RATE")
	    private BigDecimal interestRate;

	    @Column(name = "OUTSTANDING_BALANCE_PERCENT")
	    private BigDecimal outstandingBalancePercent;

	    @Column(name = "LIMIT_PERCENT")
	    private BigDecimal limitPercent;

	    @Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Column(name = "REPORT_DATE")
	    private Date reportDate;

	    @Column(name = "REPORT_VERSION")
	    private BigDecimal reportVersion;

	    @Column(name = "REPORT_FREQUENCY")
	    private String reportFrequency;

	    @Column(name = "REPORT_CODE")
	    private String reportCode;

	    @Column(name = "REPORT_DESC")
	    private String reportDesc;

	    @Column(name = "ENTITY_FLG")
	    private String entityFlg;

	    @Column(name = "MODIFY_FLG")
	    private String modifyFlg;

	    @Column(name = "DEL_FLG")
	    private String delFlg;

	    @Temporal(TemporalType.DATE)
	    @Column(name = "REPORT_RESUBDATE")
	    private Date reportResubdate;

	    // Getters and Setters

	    public Long getSno() {
	        return sno;
	    }

	    public void setSno(Long sno) {
	        this.sno = sno;
	    }

	    public String getGroupName() {
	        return groupName;
	    }

	    public void setGroupName(String groupName) {
	        this.groupName = groupName;
	    }

	    public String getCustomerGroupName() {
	        return customerGroupName;
	    }

	    public void setCustomerGroupName(String customerGroupName) {
	        this.customerGroupName = customerGroupName;
	    }

	    public String getSectorType() {
	        return sectorType;
	    }

	    public void setSectorType(String sectorType) {
	        this.sectorType = sectorType;
	    }

	    public String getFacilityType() {
	        return facilityType;
	    }

	    public void setFacilityType(String facilityType) {
	        this.facilityType = facilityType;
	    }

	    public BigDecimal getOriginalAmount() {
	        return originalAmount;
	    }

	    public void setOriginalAmount(BigDecimal originalAmount) {
	        this.originalAmount = originalAmount;
	    }

	    public BigDecimal getUtilisationOutstandingBalance() {
	        return utilisationOutstandingBalance;
	    }

	    public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) {
	        this.utilisationOutstandingBalance = utilisationOutstandingBalance;
	    }

	    public Date getEffectiveDate() {
	        return effectiveDate;
	    }

	    public void setEffectiveDate(Date effectiveDate) {
	        this.effectiveDate = effectiveDate;
	    }

	    public String getRepaymentPeriod() {
	        return repaymentPeriod;
	    }

	    public void setRepaymentPeriod(String repaymentPeriod) {
	        this.repaymentPeriod = repaymentPeriod;
	    }

	    public String getPerformanceStatus() {
	        return performanceStatus;
	    }

	    public void setPerformanceStatus(String performanceStatus) {
	        this.performanceStatus = performanceStatus;
	    }

	    public String getSecurityDetails() {
	        return securityDetails;
	    }

	    public void setSecurityDetails(String securityDetails) {
	        this.securityDetails = securityDetails;
	    }

	    public String getBoardApproval() {
	        return boardApproval;
	    }

	    public void setBoardApproval(String boardApproval) {
	        this.boardApproval = boardApproval;
	    }

	    public BigDecimal getInterestRate() {
	        return interestRate;
	    }

	    public void setInterestRate(BigDecimal interestRate) {
	        this.interestRate = interestRate;
	    }

	    public BigDecimal getOutstandingBalancePercent() {
	        return outstandingBalancePercent;
	    }

	    public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) {
	        this.outstandingBalancePercent = outstandingBalancePercent;
	    }

	    public BigDecimal getLimitPercent() {
	        return limitPercent;
	    }

	    public void setLimitPercent(BigDecimal limitPercent) {
	        this.limitPercent = limitPercent;
	    }

	    public Date getReportDate() {
	        return reportDate;
	    }

	    public void setReportDate(Date reportDate) {
	        this.reportDate = reportDate;
	    }

	    public BigDecimal getReportVersion() {
	        return reportVersion;
	    }

	    public void setReportVersion(BigDecimal reportVersion) {
	        this.reportVersion = reportVersion;
	    }

	    public String getReportFrequency() {
	        return reportFrequency;
	    }

	    public void setReportFrequency(String reportFrequency) {
	        this.reportFrequency = reportFrequency;
	    }

	    public String getReportCode() {
	        return reportCode;
	    }

	    public void setReportCode(String reportCode) {
	        this.reportCode = reportCode;
	    }

	    public String getReportDesc() {
	        return reportDesc;
	    }

	    public void setReportDesc(String reportDesc) {
	        this.reportDesc = reportDesc;
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

	    public Date getReportResubdate() {
	        return reportResubdate;
	    }

	    public void setReportResubdate(Date reportResubdate) {
	        this.reportResubdate = reportResubdate;
	    }
	}