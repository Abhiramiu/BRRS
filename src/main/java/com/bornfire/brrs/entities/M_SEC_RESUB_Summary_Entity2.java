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
@Table(name = "BRRS_M_SEC_RESUB_SUMMARYTABLE2")
@IdClass(M_SEC_PK.class)
public class M_SEC_RESUB_Summary_Entity2 {
	
	private String R11_PRODUCT;
	private BigDecimal R11_TCA2;
	private String R12_PRODUCT;
	private BigDecimal R12_TCA2;
	private String R13_PRODUCT;
	private BigDecimal R13_TCA2;
	private String R14_PRODUCT;
	private BigDecimal R14_TCA2;
	private String R15_PRODUCT;
	private BigDecimal R15_TCA2;
	private String R16_PRODUCT;
	private BigDecimal R16_TCA2;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id

		
	private Date	report_date;
	@Id
	private BigDecimal	report_version;

	@Column(name = "REPORT_RESUBDATE")

	private Date reportResubDate;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_TCA2() {
		return R11_TCA2;
	}
	public void setR11_TCA2(BigDecimal r11_TCA2) {
		R11_TCA2 = r11_TCA2;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_TCA2() {
		return R12_TCA2;
	}
	public void setR12_TCA2(BigDecimal r12_TCA2) {
		R12_TCA2 = r12_TCA2;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_TCA2() {
		return R13_TCA2;
	}
	public void setR13_TCA2(BigDecimal r13_TCA2) {
		R13_TCA2 = r13_TCA2;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_TCA2() {
		return R14_TCA2;
	}
	public void setR14_TCA2(BigDecimal r14_TCA2) {
		R14_TCA2 = r14_TCA2;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_TCA2() {
		return R15_TCA2;
	}
	public void setR15_TCA2(BigDecimal r15_TCA2) {
		R15_TCA2 = r15_TCA2;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_TCA2() {
		return R16_TCA2;
	}
	public void setR16_TCA2(BigDecimal r16_TCA2) {
		R16_TCA2 = r16_TCA2;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public BigDecimal getReport_version() {
		return report_version;
	}
	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
	}
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public M_SEC_RESUB_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	
	
		
    

}
