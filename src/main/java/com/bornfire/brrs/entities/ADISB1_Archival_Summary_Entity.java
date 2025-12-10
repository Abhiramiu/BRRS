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
@Table(name = "BRRS_ADISB1_ARCHIVALTABLE_SUMMARY")
public class ADISB1_Archival_Summary_Entity {


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

private BigDecimal r7_total_no_of_acct;
private BigDecimal r7_total_value;
private BigDecimal r8_total_no_of_acct;
private BigDecimal r8_total_value;
private BigDecimal r9_total_no_of_acct;
private BigDecimal r9_total_value;
private BigDecimal r12_total_no_of_acct;
private BigDecimal r12_total_value;
private BigDecimal r13_total_no_of_acct;
private BigDecimal r13_total_value;
private BigDecimal r14_total_no_of_acct;
private BigDecimal r14_total_value;

private BigDecimal r34_total_no_of_acct;
private BigDecimal r34_total_value;

private BigDecimal r38_total_no_of_acct;
private BigDecimal r38_total_value;
private BigDecimal r39_total_no_of_acct;
private BigDecimal r39_total_value;






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






public BigDecimal getR7_total_no_of_acct() {
	return r7_total_no_of_acct;
}






public void setR7_total_no_of_acct(BigDecimal r7_total_no_of_acct) {
	this.r7_total_no_of_acct = r7_total_no_of_acct;
}






public BigDecimal getR7_total_value() {
	return r7_total_value;
}






public void setR7_total_value(BigDecimal r7_total_value) {
	this.r7_total_value = r7_total_value;
}






public BigDecimal getR8_total_no_of_acct() {
	return r8_total_no_of_acct;
}






public void setR8_total_no_of_acct(BigDecimal r8_total_no_of_acct) {
	this.r8_total_no_of_acct = r8_total_no_of_acct;
}






public BigDecimal getR8_total_value() {
	return r8_total_value;
}






public void setR8_total_value(BigDecimal r8_total_value) {
	this.r8_total_value = r8_total_value;
}






public BigDecimal getR9_total_no_of_acct() {
	return r9_total_no_of_acct;
}






public void setR9_total_no_of_acct(BigDecimal r9_total_no_of_acct) {
	this.r9_total_no_of_acct = r9_total_no_of_acct;
}






public BigDecimal getR9_total_value() {
	return r9_total_value;
}






public void setR9_total_value(BigDecimal r9_total_value) {
	this.r9_total_value = r9_total_value;
}






public BigDecimal getR12_total_no_of_acct() {
	return r12_total_no_of_acct;
}






public void setR12_total_no_of_acct(BigDecimal r12_total_no_of_acct) {
	this.r12_total_no_of_acct = r12_total_no_of_acct;
}






public BigDecimal getR12_total_value() {
	return r12_total_value;
}






public void setR12_total_value(BigDecimal r12_total_value) {
	this.r12_total_value = r12_total_value;
}






public BigDecimal getR13_total_no_of_acct() {
	return r13_total_no_of_acct;
}






public void setR13_total_no_of_acct(BigDecimal r13_total_no_of_acct) {
	this.r13_total_no_of_acct = r13_total_no_of_acct;
}






public BigDecimal getR13_total_value() {
	return r13_total_value;
}






public void setR13_total_value(BigDecimal r13_total_value) {
	this.r13_total_value = r13_total_value;
}






public BigDecimal getR14_total_no_of_acct() {
	return r14_total_no_of_acct;
}






public void setR14_total_no_of_acct(BigDecimal r14_total_no_of_acct) {
	this.r14_total_no_of_acct = r14_total_no_of_acct;
}






public BigDecimal getR14_total_value() {
	return r14_total_value;
}






public void setR14_total_value(BigDecimal r14_total_value) {
	this.r14_total_value = r14_total_value;
}






public BigDecimal getR34_total_no_of_acct() {
	return r34_total_no_of_acct;
}






public void setR34_total_no_of_acct(BigDecimal r34_total_no_of_acct) {
	this.r34_total_no_of_acct = r34_total_no_of_acct;
}






public BigDecimal getR34_total_value() {
	return r34_total_value;
}






public void setR34_total_value(BigDecimal r34_total_value) {
	this.r34_total_value = r34_total_value;
}






public BigDecimal getR38_total_no_of_acct() {
	return r38_total_no_of_acct;
}






public void setR38_total_no_of_acct(BigDecimal r38_total_no_of_acct) {
	this.r38_total_no_of_acct = r38_total_no_of_acct;
}






public BigDecimal getR38_total_value() {
	return r38_total_value;
}






public void setR38_total_value(BigDecimal r38_total_value) {
	this.r38_total_value = r38_total_value;
}






public BigDecimal getR39_total_no_of_acct() {
	return r39_total_no_of_acct;
}






public void setR39_total_no_of_acct(BigDecimal r39_total_no_of_acct) {
	this.r39_total_no_of_acct = r39_total_no_of_acct;
}






public BigDecimal getR39_total_value() {
	return r39_total_value;
}






public void setR39_total_value(BigDecimal r39_total_value) {
	this.r39_total_value = r39_total_value;
}






public ADISB1_Archival_Summary_Entity() {
    super();
}


}
