package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_MDISB5_ARCHIVALTABLE_SUMMARY1")
@IdClass(MDISB5_Archival_Summary1_PK.class)

public class MDISB5_Archival_Summary_Entity1 {
	
	private String R5_NAME_OF_SHAREHOLDER;
    private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R5_NUMBER_OF_ACCOUNTS;
    private BigDecimal R5_AMOUNT;

    private String R6_NAME_OF_SHAREHOLDER;
    private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R6_NUMBER_OF_ACCOUNTS;
    private BigDecimal R6_AMOUNT;

    private String R7_NAME_OF_SHAREHOLDER;
    private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R7_NUMBER_OF_ACCOUNTS;
    private BigDecimal R7_AMOUNT;

    private String R8_NAME_OF_SHAREHOLDER;
    private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R8_NUMBER_OF_ACCOUNTS;
    private BigDecimal R8_AMOUNT;

    private String R9_NAME_OF_SHAREHOLDER;
    private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R9_NUMBER_OF_ACCOUNTS;
    private BigDecimal R9_AMOUNT;

    private String R10_NAME_OF_SHAREHOLDER;
    private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R10_NUMBER_OF_ACCOUNTS;
    private BigDecimal R10_AMOUNT;

    private String R11_NAME_OF_SHAREHOLDER;
    private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R11_NUMBER_OF_ACCOUNTS;
    private BigDecimal R11_AMOUNT;

    private String R12_NAME_OF_SHAREHOLDER;
    private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R12_NUMBER_OF_ACCOUNTS;
    private BigDecimal R12_AMOUNT;

    private String R13_NAME_OF_SHAREHOLDER;
    private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R13_NUMBER_OF_ACCOUNTS;
    private BigDecimal R13_AMOUNT;

    private String R14_NAME_OF_SHAREHOLDER;
    private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R14_NUMBER_OF_ACCOUNTS;
    private BigDecimal R14_AMOUNT;

    private String R15_NAME_OF_SHAREHOLDER;
    private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
    private BigDecimal R15_NUMBER_OF_ACCOUNTS;
    private BigDecimal R15_AMOUNT;
    
    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Id
    @Column(name = "REPORT_DATE")
	private Date reportDate;
    
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
   
    @Temporal(TemporalType.TIMESTAMP)
    private Date REPORT_RESUBDATE;
   
    private String REPORT_FREQUENCY;
    private String REPORT_CODE;
    private String REPORT_DESC;
    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DELETE_FLG;
    
    
    
	public String getR5_NAME_OF_SHAREHOLDER() {
		return R5_NAME_OF_SHAREHOLDER;
	}

	public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
		R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
		return R5_PERCENTAGE_SHAREHOLDING;
	}

	public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
		R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
		return R5_NUMBER_OF_ACCOUNTS;
	}

	public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
		R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR5_AMOUNT() {
		return R5_AMOUNT;
	}

	public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
		R5_AMOUNT = r5_AMOUNT;
	}

	public String getR6_NAME_OF_SHAREHOLDER() {
		return R6_NAME_OF_SHAREHOLDER;
	}

	public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
		R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
		return R6_PERCENTAGE_SHAREHOLDING;
	}

	public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
		R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
		return R6_NUMBER_OF_ACCOUNTS;
	}

	public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
		R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR6_AMOUNT() {
		return R6_AMOUNT;
	}

	public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
		R6_AMOUNT = r6_AMOUNT;
	}

	public String getR7_NAME_OF_SHAREHOLDER() {
		return R7_NAME_OF_SHAREHOLDER;
	}

	public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
		R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
		return R7_PERCENTAGE_SHAREHOLDING;
	}

	public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
		R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
		return R7_NUMBER_OF_ACCOUNTS;
	}

	public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
		R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR7_AMOUNT() {
		return R7_AMOUNT;
	}

	public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
		R7_AMOUNT = r7_AMOUNT;
	}

	public String getR8_NAME_OF_SHAREHOLDER() {
		return R8_NAME_OF_SHAREHOLDER;
	}

	public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
		R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
		return R8_PERCENTAGE_SHAREHOLDING;
	}

	public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
		R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
		return R8_NUMBER_OF_ACCOUNTS;
	}

	public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
		R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR8_AMOUNT() {
		return R8_AMOUNT;
	}

	public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
		R8_AMOUNT = r8_AMOUNT;
	}

	public String getR9_NAME_OF_SHAREHOLDER() {
		return R9_NAME_OF_SHAREHOLDER;
	}

	public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
		R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
		return R9_PERCENTAGE_SHAREHOLDING;
	}

	public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
		R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
		return R9_NUMBER_OF_ACCOUNTS;
	}

	public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
		R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR9_AMOUNT() {
		return R9_AMOUNT;
	}

	public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
		R9_AMOUNT = r9_AMOUNT;
	}

	public String getR10_NAME_OF_SHAREHOLDER() {
		return R10_NAME_OF_SHAREHOLDER;
	}

	public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
		R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
		return R10_PERCENTAGE_SHAREHOLDING;
	}

	public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
		R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
		return R10_NUMBER_OF_ACCOUNTS;
	}

	public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
		R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR10_AMOUNT() {
		return R10_AMOUNT;
	}

	public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
		R10_AMOUNT = r10_AMOUNT;
	}

	public String getR11_NAME_OF_SHAREHOLDER() {
		return R11_NAME_OF_SHAREHOLDER;
	}

	public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
		R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
		return R11_PERCENTAGE_SHAREHOLDING;
	}

	public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
		R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
		return R11_NUMBER_OF_ACCOUNTS;
	}

	public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
		R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR11_AMOUNT() {
		return R11_AMOUNT;
	}

	public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
		R11_AMOUNT = r11_AMOUNT;
	}

	public String getR12_NAME_OF_SHAREHOLDER() {
		return R12_NAME_OF_SHAREHOLDER;
	}

	public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
		R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
		return R12_PERCENTAGE_SHAREHOLDING;
	}

	public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
		R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
		return R12_NUMBER_OF_ACCOUNTS;
	}

	public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
		R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR12_AMOUNT() {
		return R12_AMOUNT;
	}

	public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
		R12_AMOUNT = r12_AMOUNT;
	}

	public String getR13_NAME_OF_SHAREHOLDER() {
		return R13_NAME_OF_SHAREHOLDER;
	}

	public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
		R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
		return R13_PERCENTAGE_SHAREHOLDING;
	}

	public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
		R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
		return R13_NUMBER_OF_ACCOUNTS;
	}

	public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
		R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR13_AMOUNT() {
		return R13_AMOUNT;
	}

	public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
		R13_AMOUNT = r13_AMOUNT;
	}

	public String getR14_NAME_OF_SHAREHOLDER() {
		return R14_NAME_OF_SHAREHOLDER;
	}

	public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
		R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
		return R14_PERCENTAGE_SHAREHOLDING;
	}

	public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
		R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
		return R14_NUMBER_OF_ACCOUNTS;
	}

	public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
		R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR14_AMOUNT() {
		return R14_AMOUNT;
	}

	public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
		R14_AMOUNT = r14_AMOUNT;
	}

	public String getR15_NAME_OF_SHAREHOLDER() {
		return R15_NAME_OF_SHAREHOLDER;
	}

	public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
		R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
	}

	public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
		return R15_PERCENTAGE_SHAREHOLDING;
	}

	public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
		R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
	}

	public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
		return R15_NUMBER_OF_ACCOUNTS;
	}

	public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
		R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
	}

	public BigDecimal getR15_AMOUNT() {
		return R15_AMOUNT;
	}

	public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
		R15_AMOUNT = r15_AMOUNT;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportVersion() {
		return reportVersion;
	}

	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}

	public Date getREPORT_RESUBDATE() {
		return REPORT_RESUBDATE;
	}

	public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
		REPORT_RESUBDATE = rEPORT_RESUBDATE;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}

	public String getDELETE_FLG() {
		return DELETE_FLG;
	}

	public void setDELETE_FLG(String dELETE_FLG) {
		DELETE_FLG = dELETE_FLG;
	}
	
	

	public MDISB5_Archival_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    

  
	

}
