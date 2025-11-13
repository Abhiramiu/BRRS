
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
@Table(name = "BRRS_M_OPTR_ARCHIVALTABLE_SUMMARY")
@IdClass(M_OPTR_Archival_Summary_PK.class)

public class M_OPTR_Archival_Summary_Entity {

	
	@Id
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "REPORT_DATE")
    private Date reportDate;
	
	@Id
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
	
    @Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;
    
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;



private BigDecimal R10_INTEREST_RATES;
private BigDecimal R10_EQUITIES;
private BigDecimal R10_FOREIGN_EXC_GOLD;
private BigDecimal R10_COMMODITIES;
private BigDecimal R10_TOTAL;

private BigDecimal R11_INTEREST_RATES;
private BigDecimal R11_EQUITIES;
private BigDecimal R11_FOREIGN_EXC_GOLD;
private BigDecimal R11_COMMODITIES;
private BigDecimal R11_TOTAL;

private BigDecimal R12_INTEREST_RATES;
private BigDecimal R12_EQUITIES;
private BigDecimal R12_FOREIGN_EXC_GOLD;
private BigDecimal R12_COMMODITIES;
private BigDecimal R12_TOTAL;

private BigDecimal R13_INTEREST_RATES;
private BigDecimal R13_EQUITIES;
private BigDecimal R13_FOREIGN_EXC_GOLD;
private BigDecimal R13_COMMODITIES;
private BigDecimal R13_TOTAL;

private BigDecimal R14_INTEREST_RATES;
private BigDecimal R14_EQUITIES;
private BigDecimal R14_FOREIGN_EXC_GOLD;
private BigDecimal R14_COMMODITIES;
private BigDecimal R14_TOTAL;






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






public Date getReportResubDate() {
	return reportResubDate;
}






public void setReportResubDate(Date reportResubDate) {
	this.reportResubDate = reportResubDate;
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






public String getDEL_FLG() {
	return DEL_FLG;
}






public void setDEL_FLG(String dEL_FLG) {
	DEL_FLG = dEL_FLG;
}






public BigDecimal getR10_INTEREST_RATES() {
	return R10_INTEREST_RATES;
}






public void setR10_INTEREST_RATES(BigDecimal r10_INTEREST_RATES) {
	R10_INTEREST_RATES = r10_INTEREST_RATES;
}






public BigDecimal getR10_EQUITIES() {
	return R10_EQUITIES;
}






public void setR10_EQUITIES(BigDecimal r10_EQUITIES) {
	R10_EQUITIES = r10_EQUITIES;
}






public BigDecimal getR10_FOREIGN_EXC_GOLD() {
	return R10_FOREIGN_EXC_GOLD;
}






public void setR10_FOREIGN_EXC_GOLD(BigDecimal r10_FOREIGN_EXC_GOLD) {
	R10_FOREIGN_EXC_GOLD = r10_FOREIGN_EXC_GOLD;
}






public BigDecimal getR10_COMMODITIES() {
	return R10_COMMODITIES;
}






public void setR10_COMMODITIES(BigDecimal r10_COMMODITIES) {
	R10_COMMODITIES = r10_COMMODITIES;
}






public BigDecimal getR10_TOTAL() {
	return R10_TOTAL;
}






public void setR10_TOTAL(BigDecimal r10_TOTAL) {
	R10_TOTAL = r10_TOTAL;
}






public BigDecimal getR11_INTEREST_RATES() {
	return R11_INTEREST_RATES;
}






public void setR11_INTEREST_RATES(BigDecimal r11_INTEREST_RATES) {
	R11_INTEREST_RATES = r11_INTEREST_RATES;
}






public BigDecimal getR11_EQUITIES() {
	return R11_EQUITIES;
}






public void setR11_EQUITIES(BigDecimal r11_EQUITIES) {
	R11_EQUITIES = r11_EQUITIES;
}






public BigDecimal getR11_FOREIGN_EXC_GOLD() {
	return R11_FOREIGN_EXC_GOLD;
}






public void setR11_FOREIGN_EXC_GOLD(BigDecimal r11_FOREIGN_EXC_GOLD) {
	R11_FOREIGN_EXC_GOLD = r11_FOREIGN_EXC_GOLD;
}






public BigDecimal getR11_COMMODITIES() {
	return R11_COMMODITIES;
}






public void setR11_COMMODITIES(BigDecimal r11_COMMODITIES) {
	R11_COMMODITIES = r11_COMMODITIES;
}






public BigDecimal getR11_TOTAL() {
	return R11_TOTAL;
}






public void setR11_TOTAL(BigDecimal r11_TOTAL) {
	R11_TOTAL = r11_TOTAL;
}






public BigDecimal getR12_INTEREST_RATES() {
	return R12_INTEREST_RATES;
}






public void setR12_INTEREST_RATES(BigDecimal r12_INTEREST_RATES) {
	R12_INTEREST_RATES = r12_INTEREST_RATES;
}






public BigDecimal getR12_EQUITIES() {
	return R12_EQUITIES;
}






public void setR12_EQUITIES(BigDecimal r12_EQUITIES) {
	R12_EQUITIES = r12_EQUITIES;
}






public BigDecimal getR12_FOREIGN_EXC_GOLD() {
	return R12_FOREIGN_EXC_GOLD;
}






public void setR12_FOREIGN_EXC_GOLD(BigDecimal r12_FOREIGN_EXC_GOLD) {
	R12_FOREIGN_EXC_GOLD = r12_FOREIGN_EXC_GOLD;
}






public BigDecimal getR12_COMMODITIES() {
	return R12_COMMODITIES;
}






public void setR12_COMMODITIES(BigDecimal r12_COMMODITIES) {
	R12_COMMODITIES = r12_COMMODITIES;
}






public BigDecimal getR12_TOTAL() {
	return R12_TOTAL;
}






public void setR12_TOTAL(BigDecimal r12_TOTAL) {
	R12_TOTAL = r12_TOTAL;
}






public BigDecimal getR13_INTEREST_RATES() {
	return R13_INTEREST_RATES;
}






public void setR13_INTEREST_RATES(BigDecimal r13_INTEREST_RATES) {
	R13_INTEREST_RATES = r13_INTEREST_RATES;
}






public BigDecimal getR13_EQUITIES() {
	return R13_EQUITIES;
}






public void setR13_EQUITIES(BigDecimal r13_EQUITIES) {
	R13_EQUITIES = r13_EQUITIES;
}






public BigDecimal getR13_FOREIGN_EXC_GOLD() {
	return R13_FOREIGN_EXC_GOLD;
}






public void setR13_FOREIGN_EXC_GOLD(BigDecimal r13_FOREIGN_EXC_GOLD) {
	R13_FOREIGN_EXC_GOLD = r13_FOREIGN_EXC_GOLD;
}






public BigDecimal getR13_COMMODITIES() {
	return R13_COMMODITIES;
}






public void setR13_COMMODITIES(BigDecimal r13_COMMODITIES) {
	R13_COMMODITIES = r13_COMMODITIES;
}






public BigDecimal getR13_TOTAL() {
	return R13_TOTAL;
}






public void setR13_TOTAL(BigDecimal r13_TOTAL) {
	R13_TOTAL = r13_TOTAL;
}






public BigDecimal getR14_INTEREST_RATES() {
	return R14_INTEREST_RATES;
}






public void setR14_INTEREST_RATES(BigDecimal r14_INTEREST_RATES) {
	R14_INTEREST_RATES = r14_INTEREST_RATES;
}






public BigDecimal getR14_EQUITIES() {
	return R14_EQUITIES;
}






public void setR14_EQUITIES(BigDecimal r14_EQUITIES) {
	R14_EQUITIES = r14_EQUITIES;
}






public BigDecimal getR14_FOREIGN_EXC_GOLD() {
	return R14_FOREIGN_EXC_GOLD;
}






public void setR14_FOREIGN_EXC_GOLD(BigDecimal r14_FOREIGN_EXC_GOLD) {
	R14_FOREIGN_EXC_GOLD = r14_FOREIGN_EXC_GOLD;
}






public BigDecimal getR14_COMMODITIES() {
	return R14_COMMODITIES;
}






public void setR14_COMMODITIES(BigDecimal r14_COMMODITIES) {
	R14_COMMODITIES = r14_COMMODITIES;
}






public BigDecimal getR14_TOTAL() {
	return R14_TOTAL;
}






public void setR14_TOTAL(BigDecimal r14_TOTAL) {
	R14_TOTAL = r14_TOTAL;
}






	public M_OPTR_Archival_Summary_Entity() {
        super();
    }

}