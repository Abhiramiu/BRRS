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
@Table(name = "BRRS_ADISB1_MANUAL_ARCHIVALTABLE_SUMMARY")
public class ADISB1_Manual_Archival_Summary_Entity {


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


private BigDecimal r23_total_no_of_acct;
private BigDecimal r23_total_value;
private BigDecimal r25_total_no_of_acct;
private BigDecimal r25_total_value;
private BigDecimal r26_total_no_of_acct;
private BigDecimal r26_total_value;
private BigDecimal r30_total_no_of_acct;
private BigDecimal r30_total_value;

private BigDecimal r35_total_no_of_acct;
private BigDecimal r35_total_value;

private BigDecimal r42_total_no_of_acct;
private BigDecimal r42_total_value;
private BigDecimal r43_total_no_of_acct;
private BigDecimal r43_total_value;
private BigDecimal r44_total_no_of_acct;
private BigDecimal r44_total_value;
private BigDecimal r46_total_no_of_acct;
private BigDecimal r46_total_value;










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










public BigDecimal getR23_total_no_of_acct() {
	return r23_total_no_of_acct;
}










public void setR23_total_no_of_acct(BigDecimal r23_total_no_of_acct) {
	this.r23_total_no_of_acct = r23_total_no_of_acct;
}










public BigDecimal getR23_total_value() {
	return r23_total_value;
}










public void setR23_total_value(BigDecimal r23_total_value) {
	this.r23_total_value = r23_total_value;
}










public BigDecimal getR25_total_no_of_acct() {
	return r25_total_no_of_acct;
}










public void setR25_total_no_of_acct(BigDecimal r25_total_no_of_acct) {
	this.r25_total_no_of_acct = r25_total_no_of_acct;
}










public BigDecimal getR25_total_value() {
	return r25_total_value;
}










public void setR25_total_value(BigDecimal r25_total_value) {
	this.r25_total_value = r25_total_value;
}










public BigDecimal getR26_total_no_of_acct() {
	return r26_total_no_of_acct;
}










public void setR26_total_no_of_acct(BigDecimal r26_total_no_of_acct) {
	this.r26_total_no_of_acct = r26_total_no_of_acct;
}










public BigDecimal getR26_total_value() {
	return r26_total_value;
}










public void setR26_total_value(BigDecimal r26_total_value) {
	this.r26_total_value = r26_total_value;
}










public BigDecimal getR30_total_no_of_acct() {
	return r30_total_no_of_acct;
}










public void setR30_total_no_of_acct(BigDecimal r30_total_no_of_acct) {
	this.r30_total_no_of_acct = r30_total_no_of_acct;
}










public BigDecimal getR30_total_value() {
	return r30_total_value;
}










public void setR30_total_value(BigDecimal r30_total_value) {
	this.r30_total_value = r30_total_value;
}










public BigDecimal getR35_total_no_of_acct() {
	return r35_total_no_of_acct;
}










public void setR35_total_no_of_acct(BigDecimal r35_total_no_of_acct) {
	this.r35_total_no_of_acct = r35_total_no_of_acct;
}










public BigDecimal getR35_total_value() {
	return r35_total_value;
}










public void setR35_total_value(BigDecimal r35_total_value) {
	this.r35_total_value = r35_total_value;
}










public BigDecimal getR42_total_no_of_acct() {
	return r42_total_no_of_acct;
}










public void setR42_total_no_of_acct(BigDecimal r42_total_no_of_acct) {
	this.r42_total_no_of_acct = r42_total_no_of_acct;
}










public BigDecimal getR42_total_value() {
	return r42_total_value;
}










public void setR42_total_value(BigDecimal r42_total_value) {
	this.r42_total_value = r42_total_value;
}










public BigDecimal getR43_total_no_of_acct() {
	return r43_total_no_of_acct;
}










public void setR43_total_no_of_acct(BigDecimal r43_total_no_of_acct) {
	this.r43_total_no_of_acct = r43_total_no_of_acct;
}










public BigDecimal getR43_total_value() {
	return r43_total_value;
}










public void setR43_total_value(BigDecimal r43_total_value) {
	this.r43_total_value = r43_total_value;
}










public BigDecimal getR44_total_no_of_acct() {
	return r44_total_no_of_acct;
}










public void setR44_total_no_of_acct(BigDecimal r44_total_no_of_acct) {
	this.r44_total_no_of_acct = r44_total_no_of_acct;
}










public BigDecimal getR44_total_value() {
	return r44_total_value;
}










public void setR44_total_value(BigDecimal r44_total_value) {
	this.r44_total_value = r44_total_value;
}










public BigDecimal getR46_total_no_of_acct() {
	return r46_total_no_of_acct;
}










public void setR46_total_no_of_acct(BigDecimal r46_total_no_of_acct) {
	this.r46_total_no_of_acct = r46_total_no_of_acct;
}










public BigDecimal getR46_total_value() {
	return r46_total_value;
}










public void setR46_total_value(BigDecimal r46_total_value) {
	this.r46_total_value = r46_total_value;
}










public ADISB1_Manual_Archival_Summary_Entity() {
    super();
}


}
