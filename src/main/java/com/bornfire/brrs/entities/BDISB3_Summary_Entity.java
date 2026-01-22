
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_BDISB3_SUMMARYTABLE")

public class BDISB3_Summary_Entity{	




	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
	
	//@Column(name = "REPORT_RESUBDATE")
	//@Temporal(TemporalType.TIMESTAMP)
	//private Date reportResubDate;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	
	public String R5_SCVRN;
    public BigDecimal R5_AGGREGATE_BALANCE;
    public BigDecimal R5_COMPENSATABLE_AMOUNT;

    // -------- R6 --------
    public String R6_SCVRN;
    public BigDecimal R6_AGGREGATE_BALANCE;
    public BigDecimal R6_COMPENSATABLE_AMOUNT;

    // -------- R7 --------
    public String R7_SCVRN;
    public BigDecimal R7_AGGREGATE_BALANCE;
    public BigDecimal R7_COMPENSATABLE_AMOUNT;

    // -------- R8 --------
    public String R8_SCVRN;
    public BigDecimal R8_AGGREGATE_BALANCE;
    public BigDecimal R8_COMPENSATABLE_AMOUNT;

    // -------- R9 --------
    public String R9_SCVRN;
    public BigDecimal R9_AGGREGATE_BALANCE;
    public BigDecimal R9_COMPENSATABLE_AMOUNT;

    // -------- R10 --------
    public String R10_SCVRN;
    public BigDecimal R10_AGGREGATE_BALANCE;
    public BigDecimal R10_COMPENSATABLE_AMOUNT;
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
	public String getR5_SCVRN() {
		return R5_SCVRN;
	}
	public void setR5_SCVRN(String r5_SCVRN) {
		R5_SCVRN = r5_SCVRN;
	}
	public BigDecimal getR5_AGGREGATE_BALANCE() {
		return R5_AGGREGATE_BALANCE;
	}
	public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
		R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
	}
	public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
		return R5_COMPENSATABLE_AMOUNT;
	}
	public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
		R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
	}
	public String getR6_SCVRN() {
		return R6_SCVRN;
	}
	public void setR6_SCVRN(String r6_SCVRN) {
		R6_SCVRN = r6_SCVRN;
	}
	public BigDecimal getR6_AGGREGATE_BALANCE() {
		return R6_AGGREGATE_BALANCE;
	}
	public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
		R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
	}
	public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
		return R6_COMPENSATABLE_AMOUNT;
	}
	public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
		R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
	}
	public String getR7_SCVRN() {
		return R7_SCVRN;
	}
	public void setR7_SCVRN(String r7_SCVRN) {
		R7_SCVRN = r7_SCVRN;
	}
	public BigDecimal getR7_AGGREGATE_BALANCE() {
		return R7_AGGREGATE_BALANCE;
	}
	public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
		R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
	}
	public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
		return R7_COMPENSATABLE_AMOUNT;
	}
	public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
		R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
	}
	public String getR8_SCVRN() {
		return R8_SCVRN;
	}
	public void setR8_SCVRN(String r8_SCVRN) {
		R8_SCVRN = r8_SCVRN;
	}
	public BigDecimal getR8_AGGREGATE_BALANCE() {
		return R8_AGGREGATE_BALANCE;
	}
	public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
		R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
	}
	public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
		return R8_COMPENSATABLE_AMOUNT;
	}
	public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
		R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
	}
	public String getR9_SCVRN() {
		return R9_SCVRN;
	}
	public void setR9_SCVRN(String r9_SCVRN) {
		R9_SCVRN = r9_SCVRN;
	}
	public BigDecimal getR9_AGGREGATE_BALANCE() {
		return R9_AGGREGATE_BALANCE;
	}
	public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
		R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
	}
	public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
		return R9_COMPENSATABLE_AMOUNT;
	}
	public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
		R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
	}
	public String getR10_SCVRN() {
		return R10_SCVRN;
	}
	public void setR10_SCVRN(String r10_SCVRN) {
		R10_SCVRN = r10_SCVRN;
	}
	public BigDecimal getR10_AGGREGATE_BALANCE() {
		return R10_AGGREGATE_BALANCE;
	}
	public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
		R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
	}
	public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
		return R10_COMPENSATABLE_AMOUNT;
	}
	public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
		R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
	}
	public BDISB3_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

}
