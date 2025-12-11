package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_ADISB2_ARCHIVALTABLE_SUMMARY")
public class ADISB2_Archival_Summary_Entity {


@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id

private Date report_date;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;

//-------- R6 --------
private BigDecimal R6_COVERAGE_LEVEL;
private BigDecimal R6_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R6_COVERED_DEPOSITORS;
private BigDecimal R6_COVERED_DEPOSITORS_PCT;
private BigDecimal R6_EXCEEDING_DEPOSITORS;
private BigDecimal R6_COVERED_AMOUNT_PCT;

//-------- R7 --------
private BigDecimal R7_COVERAGE_LEVEL;
private BigDecimal R7_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R7_COVERED_DEPOSITORS;
private BigDecimal R7_COVERED_DEPOSITORS_PCT;
private BigDecimal R7_EXCEEDING_DEPOSITORS;
private BigDecimal R7_COVERED_AMOUNT_PCT;

//-------- R8 --------
private BigDecimal R8_COVERAGE_LEVEL;
private BigDecimal R8_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R8_COVERED_DEPOSITORS;
private BigDecimal R8_COVERED_DEPOSITORS_PCT;
private BigDecimal R8_EXCEEDING_DEPOSITORS;
private BigDecimal R8_COVERED_AMOUNT_PCT;

//-------- R9 --------
private BigDecimal R9_COVERAGE_LEVEL;
private BigDecimal R9_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R9_COVERED_DEPOSITORS;
private BigDecimal R9_COVERED_DEPOSITORS_PCT;
private BigDecimal R9_EXCEEDING_DEPOSITORS;
private BigDecimal R9_COVERED_AMOUNT_PCT;

//-------- R10 --------
private BigDecimal R10_COVERAGE_LEVEL;
private BigDecimal R10_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R10_COVERED_DEPOSITORS;
private BigDecimal R10_COVERED_DEPOSITORS_PCT;
private BigDecimal R10_EXCEEDING_DEPOSITORS;
private BigDecimal R10_COVERED_AMOUNT_PCT;

//-------- R11 --------
private BigDecimal R11_COVERAGE_LEVEL;
private BigDecimal R11_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R11_COVERED_DEPOSITORS;
private BigDecimal R11_COVERED_DEPOSITORS_PCT;
private BigDecimal R11_EXCEEDING_DEPOSITORS;
private BigDecimal R11_COVERED_AMOUNT_PCT;

//-------- R12 --------
private BigDecimal R12_COVERAGE_LEVEL;
private BigDecimal R12_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R12_COVERED_DEPOSITORS;
private BigDecimal R12_COVERED_DEPOSITORS_PCT;
private BigDecimal R12_EXCEEDING_DEPOSITORS;
private BigDecimal R12_COVERED_AMOUNT_PCT;

//-------- R13 --------
private BigDecimal R13_COVERAGE_LEVEL;
private BigDecimal R13_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R13_COVERED_DEPOSITORS;
private BigDecimal R13_COVERED_DEPOSITORS_PCT;
private BigDecimal R13_EXCEEDING_DEPOSITORS;
private BigDecimal R13_COVERED_AMOUNT_PCT;

//-------- R14 --------
private BigDecimal R14_COVERAGE_LEVEL;
private BigDecimal R14_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R14_COVERED_DEPOSITORS;
private BigDecimal R14_COVERED_DEPOSITORS_PCT;
private BigDecimal R14_EXCEEDING_DEPOSITORS;
private BigDecimal R14_COVERED_AMOUNT_PCT;

//-------- R15 --------
private BigDecimal R15_COVERAGE_LEVEL;
private BigDecimal R15_TOTAL_DEPOSIT_AMOUNT;
private BigDecimal R15_COVERED_DEPOSITORS;
private BigDecimal R15_COVERED_DEPOSITORS_PCT;
private BigDecimal R15_EXCEEDING_DEPOSITORS;
private BigDecimal R15_COVERED_AMOUNT_PCT;
public Date getReport_date() {
	return report_date;
}
public void setReport_date(Date report_date) {
	this.report_date = report_date;
}
public String getReport_version() {
	return report_version;
}
public void setReport_version(String report_version) {
	this.report_version = report_version;
}
public String getReport_frequency() {
	return report_frequency;
}
public void setReport_frequency(String report_frequency) {
	this.report_frequency = report_frequency;
}
public String getReport_code() {
	return report_code;
}
public void setReport_code(String report_code) {
	this.report_code = report_code;
}
public String getReport_desc() {
	return report_desc;
}
public void setReport_desc(String report_desc) {
	this.report_desc = report_desc;
}
public String getEntity_flg() {
	return entity_flg;
}
public void setEntity_flg(String entity_flg) {
	this.entity_flg = entity_flg;
}
public String getModify_flg() {
	return modify_flg;
}
public void setModify_flg(String modify_flg) {
	this.modify_flg = modify_flg;
}
public String getDel_flg() {
	return del_flg;
}
public void setDel_flg(String del_flg) {
	this.del_flg = del_flg;
}
public BigDecimal getR6_COVERAGE_LEVEL() {
	return R6_COVERAGE_LEVEL;
}
public void setR6_COVERAGE_LEVEL(BigDecimal r6_COVERAGE_LEVEL) {
	R6_COVERAGE_LEVEL = r6_COVERAGE_LEVEL;
}
public BigDecimal getR6_TOTAL_DEPOSIT_AMOUNT() {
	return R6_TOTAL_DEPOSIT_AMOUNT;
}
public void setR6_TOTAL_DEPOSIT_AMOUNT(BigDecimal r6_TOTAL_DEPOSIT_AMOUNT) {
	R6_TOTAL_DEPOSIT_AMOUNT = r6_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR6_COVERED_DEPOSITORS() {
	return R6_COVERED_DEPOSITORS;
}
public void setR6_COVERED_DEPOSITORS(BigDecimal r6_COVERED_DEPOSITORS) {
	R6_COVERED_DEPOSITORS = r6_COVERED_DEPOSITORS;
}
public BigDecimal getR6_COVERED_DEPOSITORS_PCT() {
	return R6_COVERED_DEPOSITORS_PCT;
}
public void setR6_COVERED_DEPOSITORS_PCT(BigDecimal r6_COVERED_DEPOSITORS_PCT) {
	R6_COVERED_DEPOSITORS_PCT = r6_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR6_EXCEEDING_DEPOSITORS() {
	return R6_EXCEEDING_DEPOSITORS;
}
public void setR6_EXCEEDING_DEPOSITORS(BigDecimal r6_EXCEEDING_DEPOSITORS) {
	R6_EXCEEDING_DEPOSITORS = r6_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR6_COVERED_AMOUNT_PCT() {
	return R6_COVERED_AMOUNT_PCT;
}
public void setR6_COVERED_AMOUNT_PCT(BigDecimal r6_COVERED_AMOUNT_PCT) {
	R6_COVERED_AMOUNT_PCT = r6_COVERED_AMOUNT_PCT;
}
public BigDecimal getR7_COVERAGE_LEVEL() {
	return R7_COVERAGE_LEVEL;
}
public void setR7_COVERAGE_LEVEL(BigDecimal r7_COVERAGE_LEVEL) {
	R7_COVERAGE_LEVEL = r7_COVERAGE_LEVEL;
}
public BigDecimal getR7_TOTAL_DEPOSIT_AMOUNT() {
	return R7_TOTAL_DEPOSIT_AMOUNT;
}
public void setR7_TOTAL_DEPOSIT_AMOUNT(BigDecimal r7_TOTAL_DEPOSIT_AMOUNT) {
	R7_TOTAL_DEPOSIT_AMOUNT = r7_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR7_COVERED_DEPOSITORS() {
	return R7_COVERED_DEPOSITORS;
}
public void setR7_COVERED_DEPOSITORS(BigDecimal r7_COVERED_DEPOSITORS) {
	R7_COVERED_DEPOSITORS = r7_COVERED_DEPOSITORS;
}
public BigDecimal getR7_COVERED_DEPOSITORS_PCT() {
	return R7_COVERED_DEPOSITORS_PCT;
}
public void setR7_COVERED_DEPOSITORS_PCT(BigDecimal r7_COVERED_DEPOSITORS_PCT) {
	R7_COVERED_DEPOSITORS_PCT = r7_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR7_EXCEEDING_DEPOSITORS() {
	return R7_EXCEEDING_DEPOSITORS;
}
public void setR7_EXCEEDING_DEPOSITORS(BigDecimal r7_EXCEEDING_DEPOSITORS) {
	R7_EXCEEDING_DEPOSITORS = r7_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR7_COVERED_AMOUNT_PCT() {
	return R7_COVERED_AMOUNT_PCT;
}
public void setR7_COVERED_AMOUNT_PCT(BigDecimal r7_COVERED_AMOUNT_PCT) {
	R7_COVERED_AMOUNT_PCT = r7_COVERED_AMOUNT_PCT;
}
public BigDecimal getR8_COVERAGE_LEVEL() {
	return R8_COVERAGE_LEVEL;
}
public void setR8_COVERAGE_LEVEL(BigDecimal r8_COVERAGE_LEVEL) {
	R8_COVERAGE_LEVEL = r8_COVERAGE_LEVEL;
}
public BigDecimal getR8_TOTAL_DEPOSIT_AMOUNT() {
	return R8_TOTAL_DEPOSIT_AMOUNT;
}
public void setR8_TOTAL_DEPOSIT_AMOUNT(BigDecimal r8_TOTAL_DEPOSIT_AMOUNT) {
	R8_TOTAL_DEPOSIT_AMOUNT = r8_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR8_COVERED_DEPOSITORS() {
	return R8_COVERED_DEPOSITORS;
}
public void setR8_COVERED_DEPOSITORS(BigDecimal r8_COVERED_DEPOSITORS) {
	R8_COVERED_DEPOSITORS = r8_COVERED_DEPOSITORS;
}
public BigDecimal getR8_COVERED_DEPOSITORS_PCT() {
	return R8_COVERED_DEPOSITORS_PCT;
}
public void setR8_COVERED_DEPOSITORS_PCT(BigDecimal r8_COVERED_DEPOSITORS_PCT) {
	R8_COVERED_DEPOSITORS_PCT = r8_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR8_EXCEEDING_DEPOSITORS() {
	return R8_EXCEEDING_DEPOSITORS;
}
public void setR8_EXCEEDING_DEPOSITORS(BigDecimal r8_EXCEEDING_DEPOSITORS) {
	R8_EXCEEDING_DEPOSITORS = r8_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR8_COVERED_AMOUNT_PCT() {
	return R8_COVERED_AMOUNT_PCT;
}
public void setR8_COVERED_AMOUNT_PCT(BigDecimal r8_COVERED_AMOUNT_PCT) {
	R8_COVERED_AMOUNT_PCT = r8_COVERED_AMOUNT_PCT;
}
public BigDecimal getR9_COVERAGE_LEVEL() {
	return R9_COVERAGE_LEVEL;
}
public void setR9_COVERAGE_LEVEL(BigDecimal r9_COVERAGE_LEVEL) {
	R9_COVERAGE_LEVEL = r9_COVERAGE_LEVEL;
}
public BigDecimal getR9_TOTAL_DEPOSIT_AMOUNT() {
	return R9_TOTAL_DEPOSIT_AMOUNT;
}
public void setR9_TOTAL_DEPOSIT_AMOUNT(BigDecimal r9_TOTAL_DEPOSIT_AMOUNT) {
	R9_TOTAL_DEPOSIT_AMOUNT = r9_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR9_COVERED_DEPOSITORS() {
	return R9_COVERED_DEPOSITORS;
}
public void setR9_COVERED_DEPOSITORS(BigDecimal r9_COVERED_DEPOSITORS) {
	R9_COVERED_DEPOSITORS = r9_COVERED_DEPOSITORS;
}
public BigDecimal getR9_COVERED_DEPOSITORS_PCT() {
	return R9_COVERED_DEPOSITORS_PCT;
}
public void setR9_COVERED_DEPOSITORS_PCT(BigDecimal r9_COVERED_DEPOSITORS_PCT) {
	R9_COVERED_DEPOSITORS_PCT = r9_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR9_EXCEEDING_DEPOSITORS() {
	return R9_EXCEEDING_DEPOSITORS;
}
public void setR9_EXCEEDING_DEPOSITORS(BigDecimal r9_EXCEEDING_DEPOSITORS) {
	R9_EXCEEDING_DEPOSITORS = r9_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR9_COVERED_AMOUNT_PCT() {
	return R9_COVERED_AMOUNT_PCT;
}
public void setR9_COVERED_AMOUNT_PCT(BigDecimal r9_COVERED_AMOUNT_PCT) {
	R9_COVERED_AMOUNT_PCT = r9_COVERED_AMOUNT_PCT;
}
public BigDecimal getR10_COVERAGE_LEVEL() {
	return R10_COVERAGE_LEVEL;
}
public void setR10_COVERAGE_LEVEL(BigDecimal r10_COVERAGE_LEVEL) {
	R10_COVERAGE_LEVEL = r10_COVERAGE_LEVEL;
}
public BigDecimal getR10_TOTAL_DEPOSIT_AMOUNT() {
	return R10_TOTAL_DEPOSIT_AMOUNT;
}
public void setR10_TOTAL_DEPOSIT_AMOUNT(BigDecimal r10_TOTAL_DEPOSIT_AMOUNT) {
	R10_TOTAL_DEPOSIT_AMOUNT = r10_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR10_COVERED_DEPOSITORS() {
	return R10_COVERED_DEPOSITORS;
}
public void setR10_COVERED_DEPOSITORS(BigDecimal r10_COVERED_DEPOSITORS) {
	R10_COVERED_DEPOSITORS = r10_COVERED_DEPOSITORS;
}
public BigDecimal getR10_COVERED_DEPOSITORS_PCT() {
	return R10_COVERED_DEPOSITORS_PCT;
}
public void setR10_COVERED_DEPOSITORS_PCT(BigDecimal r10_COVERED_DEPOSITORS_PCT) {
	R10_COVERED_DEPOSITORS_PCT = r10_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR10_EXCEEDING_DEPOSITORS() {
	return R10_EXCEEDING_DEPOSITORS;
}
public void setR10_EXCEEDING_DEPOSITORS(BigDecimal r10_EXCEEDING_DEPOSITORS) {
	R10_EXCEEDING_DEPOSITORS = r10_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR10_COVERED_AMOUNT_PCT() {
	return R10_COVERED_AMOUNT_PCT;
}
public void setR10_COVERED_AMOUNT_PCT(BigDecimal r10_COVERED_AMOUNT_PCT) {
	R10_COVERED_AMOUNT_PCT = r10_COVERED_AMOUNT_PCT;
}
public BigDecimal getR11_COVERAGE_LEVEL() {
	return R11_COVERAGE_LEVEL;
}
public void setR11_COVERAGE_LEVEL(BigDecimal r11_COVERAGE_LEVEL) {
	R11_COVERAGE_LEVEL = r11_COVERAGE_LEVEL;
}
public BigDecimal getR11_TOTAL_DEPOSIT_AMOUNT() {
	return R11_TOTAL_DEPOSIT_AMOUNT;
}
public void setR11_TOTAL_DEPOSIT_AMOUNT(BigDecimal r11_TOTAL_DEPOSIT_AMOUNT) {
	R11_TOTAL_DEPOSIT_AMOUNT = r11_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR11_COVERED_DEPOSITORS() {
	return R11_COVERED_DEPOSITORS;
}
public void setR11_COVERED_DEPOSITORS(BigDecimal r11_COVERED_DEPOSITORS) {
	R11_COVERED_DEPOSITORS = r11_COVERED_DEPOSITORS;
}
public BigDecimal getR11_COVERED_DEPOSITORS_PCT() {
	return R11_COVERED_DEPOSITORS_PCT;
}
public void setR11_COVERED_DEPOSITORS_PCT(BigDecimal r11_COVERED_DEPOSITORS_PCT) {
	R11_COVERED_DEPOSITORS_PCT = r11_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR11_EXCEEDING_DEPOSITORS() {
	return R11_EXCEEDING_DEPOSITORS;
}
public void setR11_EXCEEDING_DEPOSITORS(BigDecimal r11_EXCEEDING_DEPOSITORS) {
	R11_EXCEEDING_DEPOSITORS = r11_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR11_COVERED_AMOUNT_PCT() {
	return R11_COVERED_AMOUNT_PCT;
}
public void setR11_COVERED_AMOUNT_PCT(BigDecimal r11_COVERED_AMOUNT_PCT) {
	R11_COVERED_AMOUNT_PCT = r11_COVERED_AMOUNT_PCT;
}
public BigDecimal getR12_COVERAGE_LEVEL() {
	return R12_COVERAGE_LEVEL;
}
public void setR12_COVERAGE_LEVEL(BigDecimal r12_COVERAGE_LEVEL) {
	R12_COVERAGE_LEVEL = r12_COVERAGE_LEVEL;
}
public BigDecimal getR12_TOTAL_DEPOSIT_AMOUNT() {
	return R12_TOTAL_DEPOSIT_AMOUNT;
}
public void setR12_TOTAL_DEPOSIT_AMOUNT(BigDecimal r12_TOTAL_DEPOSIT_AMOUNT) {
	R12_TOTAL_DEPOSIT_AMOUNT = r12_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR12_COVERED_DEPOSITORS() {
	return R12_COVERED_DEPOSITORS;
}
public void setR12_COVERED_DEPOSITORS(BigDecimal r12_COVERED_DEPOSITORS) {
	R12_COVERED_DEPOSITORS = r12_COVERED_DEPOSITORS;
}
public BigDecimal getR12_COVERED_DEPOSITORS_PCT() {
	return R12_COVERED_DEPOSITORS_PCT;
}
public void setR12_COVERED_DEPOSITORS_PCT(BigDecimal r12_COVERED_DEPOSITORS_PCT) {
	R12_COVERED_DEPOSITORS_PCT = r12_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR12_EXCEEDING_DEPOSITORS() {
	return R12_EXCEEDING_DEPOSITORS;
}
public void setR12_EXCEEDING_DEPOSITORS(BigDecimal r12_EXCEEDING_DEPOSITORS) {
	R12_EXCEEDING_DEPOSITORS = r12_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR12_COVERED_AMOUNT_PCT() {
	return R12_COVERED_AMOUNT_PCT;
}
public void setR12_COVERED_AMOUNT_PCT(BigDecimal r12_COVERED_AMOUNT_PCT) {
	R12_COVERED_AMOUNT_PCT = r12_COVERED_AMOUNT_PCT;
}
public BigDecimal getR13_COVERAGE_LEVEL() {
	return R13_COVERAGE_LEVEL;
}
public void setR13_COVERAGE_LEVEL(BigDecimal r13_COVERAGE_LEVEL) {
	R13_COVERAGE_LEVEL = r13_COVERAGE_LEVEL;
}
public BigDecimal getR13_TOTAL_DEPOSIT_AMOUNT() {
	return R13_TOTAL_DEPOSIT_AMOUNT;
}
public void setR13_TOTAL_DEPOSIT_AMOUNT(BigDecimal r13_TOTAL_DEPOSIT_AMOUNT) {
	R13_TOTAL_DEPOSIT_AMOUNT = r13_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR13_COVERED_DEPOSITORS() {
	return R13_COVERED_DEPOSITORS;
}
public void setR13_COVERED_DEPOSITORS(BigDecimal r13_COVERED_DEPOSITORS) {
	R13_COVERED_DEPOSITORS = r13_COVERED_DEPOSITORS;
}
public BigDecimal getR13_COVERED_DEPOSITORS_PCT() {
	return R13_COVERED_DEPOSITORS_PCT;
}
public void setR13_COVERED_DEPOSITORS_PCT(BigDecimal r13_COVERED_DEPOSITORS_PCT) {
	R13_COVERED_DEPOSITORS_PCT = r13_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR13_EXCEEDING_DEPOSITORS() {
	return R13_EXCEEDING_DEPOSITORS;
}
public void setR13_EXCEEDING_DEPOSITORS(BigDecimal r13_EXCEEDING_DEPOSITORS) {
	R13_EXCEEDING_DEPOSITORS = r13_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR13_COVERED_AMOUNT_PCT() {
	return R13_COVERED_AMOUNT_PCT;
}
public void setR13_COVERED_AMOUNT_PCT(BigDecimal r13_COVERED_AMOUNT_PCT) {
	R13_COVERED_AMOUNT_PCT = r13_COVERED_AMOUNT_PCT;
}
public BigDecimal getR14_COVERAGE_LEVEL() {
	return R14_COVERAGE_LEVEL;
}
public void setR14_COVERAGE_LEVEL(BigDecimal r14_COVERAGE_LEVEL) {
	R14_COVERAGE_LEVEL = r14_COVERAGE_LEVEL;
}
public BigDecimal getR14_TOTAL_DEPOSIT_AMOUNT() {
	return R14_TOTAL_DEPOSIT_AMOUNT;
}
public void setR14_TOTAL_DEPOSIT_AMOUNT(BigDecimal r14_TOTAL_DEPOSIT_AMOUNT) {
	R14_TOTAL_DEPOSIT_AMOUNT = r14_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR14_COVERED_DEPOSITORS() {
	return R14_COVERED_DEPOSITORS;
}
public void setR14_COVERED_DEPOSITORS(BigDecimal r14_COVERED_DEPOSITORS) {
	R14_COVERED_DEPOSITORS = r14_COVERED_DEPOSITORS;
}
public BigDecimal getR14_COVERED_DEPOSITORS_PCT() {
	return R14_COVERED_DEPOSITORS_PCT;
}
public void setR14_COVERED_DEPOSITORS_PCT(BigDecimal r14_COVERED_DEPOSITORS_PCT) {
	R14_COVERED_DEPOSITORS_PCT = r14_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR14_EXCEEDING_DEPOSITORS() {
	return R14_EXCEEDING_DEPOSITORS;
}
public void setR14_EXCEEDING_DEPOSITORS(BigDecimal r14_EXCEEDING_DEPOSITORS) {
	R14_EXCEEDING_DEPOSITORS = r14_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR14_COVERED_AMOUNT_PCT() {
	return R14_COVERED_AMOUNT_PCT;
}
public void setR14_COVERED_AMOUNT_PCT(BigDecimal r14_COVERED_AMOUNT_PCT) {
	R14_COVERED_AMOUNT_PCT = r14_COVERED_AMOUNT_PCT;
}
public BigDecimal getR15_COVERAGE_LEVEL() {
	return R15_COVERAGE_LEVEL;
}
public void setR15_COVERAGE_LEVEL(BigDecimal r15_COVERAGE_LEVEL) {
	R15_COVERAGE_LEVEL = r15_COVERAGE_LEVEL;
}
public BigDecimal getR15_TOTAL_DEPOSIT_AMOUNT() {
	return R15_TOTAL_DEPOSIT_AMOUNT;
}
public void setR15_TOTAL_DEPOSIT_AMOUNT(BigDecimal r15_TOTAL_DEPOSIT_AMOUNT) {
	R15_TOTAL_DEPOSIT_AMOUNT = r15_TOTAL_DEPOSIT_AMOUNT;
}
public BigDecimal getR15_COVERED_DEPOSITORS() {
	return R15_COVERED_DEPOSITORS;
}
public void setR15_COVERED_DEPOSITORS(BigDecimal r15_COVERED_DEPOSITORS) {
	R15_COVERED_DEPOSITORS = r15_COVERED_DEPOSITORS;
}
public BigDecimal getR15_COVERED_DEPOSITORS_PCT() {
	return R15_COVERED_DEPOSITORS_PCT;
}
public void setR15_COVERED_DEPOSITORS_PCT(BigDecimal r15_COVERED_DEPOSITORS_PCT) {
	R15_COVERED_DEPOSITORS_PCT = r15_COVERED_DEPOSITORS_PCT;
}
public BigDecimal getR15_EXCEEDING_DEPOSITORS() {
	return R15_EXCEEDING_DEPOSITORS;
}
public void setR15_EXCEEDING_DEPOSITORS(BigDecimal r15_EXCEEDING_DEPOSITORS) {
	R15_EXCEEDING_DEPOSITORS = r15_EXCEEDING_DEPOSITORS;
}
public BigDecimal getR15_COVERED_AMOUNT_PCT() {
	return R15_COVERED_AMOUNT_PCT;
}
public void setR15_COVERED_AMOUNT_PCT(BigDecimal r15_COVERED_AMOUNT_PCT) {
	R15_COVERED_AMOUNT_PCT = r15_COVERED_AMOUNT_PCT;
}
public ADISB2_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}





}
