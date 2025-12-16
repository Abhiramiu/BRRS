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
@Table(name = "BRRS_CREDIT_RISK_SUMMARYTABLE")


public class CREDIT_RISK_Summary_Entity {
	
	

		
	
	private String	r5_qua_disc;
	private String	r4_product;
	private BigDecimal	r4_num;
	private String	r5_product;
	private BigDecimal	r5_num;
	private String	r6_product;
	private BigDecimal	r6_num;
	private String	r7_product;
	private BigDecimal	r7_num;
	private String	r8_product;
	private BigDecimal	r8_num;
	private String	r9_product;
	private BigDecimal	r9_num;
	private String	r10_product;
	private BigDecimal	r10_num;
	private String	r11_product;
	private BigDecimal	r11_num;
	private String	r12_product;
	private BigDecimal	r12_num;

	private String	r16_qua_disc;
	private String	r16_product;
	private BigDecimal	r16_num;
	private String	r17_qua_disc;
	private String	r17_product;
	private BigDecimal	r17_num;

	private String	r21_qua_disc;
	private String	r21_product;
	private BigDecimal	r21_num;

	private String	r22_qua_disc;
	private String	r22_product;
	private BigDecimal	r22_num;

	private String	r26_qua_disc;
	private String	r26_product;
	private BigDecimal	r26_num;

	private String	r27_qua_disc;
	private String	r27_product;
	private BigDecimal	r27_num;

	private String	r28_product;
	private BigDecimal	r28_num;


	               
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	
	private Date	report_date;
	private String	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR5_qua_disc() {
		return r5_qua_disc;
	}
	public void setR5_qua_disc(String r5_qua_disc) {
		this.r5_qua_disc = r5_qua_disc;
	}
	public String getR4_product() {
		return r4_product;
	}
	public void setR4_product(String r4_product) {
		this.r4_product = r4_product;
	}
	public BigDecimal getR4_num() {
		return r4_num;
	}
	public void setR4_num(BigDecimal r4_num) {
		this.r4_num = r4_num;
	}
	public String getR5_product() {
		return r5_product;
	}
	public void setR5_product(String r5_product) {
		this.r5_product = r5_product;
	}
	public BigDecimal getR5_num() {
		return r5_num;
	}
	public void setR5_num(BigDecimal r5_num) {
		this.r5_num = r5_num;
	}
	public String getR6_product() {
		return r6_product;
	}
	public void setR6_product(String r6_product) {
		this.r6_product = r6_product;
	}
	public BigDecimal getR6_num() {
		return r6_num;
	}
	public void setR6_num(BigDecimal r6_num) {
		this.r6_num = r6_num;
	}
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_num() {
		return r7_num;
	}
	public void setR7_num(BigDecimal r7_num) {
		this.r7_num = r7_num;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_num() {
		return r8_num;
	}
	public void setR8_num(BigDecimal r8_num) {
		this.r8_num = r8_num;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_num() {
		return r9_num;
	}
	public void setR9_num(BigDecimal r9_num) {
		this.r9_num = r9_num;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_num() {
		return r10_num;
	}
	public void setR10_num(BigDecimal r10_num) {
		this.r10_num = r10_num;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_num() {
		return r11_num;
	}
	public void setR11_num(BigDecimal r11_num) {
		this.r11_num = r11_num;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_num() {
		return r12_num;
	}
	public void setR12_num(BigDecimal r12_num) {
		this.r12_num = r12_num;
	}
	public String getR16_qua_disc() {
		return r16_qua_disc;
	}
	public void setR16_qua_disc(String r16_qua_disc) {
		this.r16_qua_disc = r16_qua_disc;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_num() {
		return r16_num;
	}
	public void setR16_num(BigDecimal r16_num) {
		this.r16_num = r16_num;
	}
	public String getR17_qua_disc() {
		return r17_qua_disc;
	}
	public void setR17_qua_disc(String r17_qua_disc) {
		this.r17_qua_disc = r17_qua_disc;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_num() {
		return r17_num;
	}
	public void setR17_num(BigDecimal r17_num) {
		this.r17_num = r17_num;
	}
	public String getR21_qua_disc() {
		return r21_qua_disc;
	}
	public void setR21_qua_disc(String r21_qua_disc) {
		this.r21_qua_disc = r21_qua_disc;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_num() {
		return r21_num;
	}
	public void setR21_num(BigDecimal r21_num) {
		this.r21_num = r21_num;
	}
	public String getR22_qua_disc() {
		return r22_qua_disc;
	}
	public void setR22_qua_disc(String r22_qua_disc) {
		this.r22_qua_disc = r22_qua_disc;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_num() {
		return r22_num;
	}
	public void setR22_num(BigDecimal r22_num) {
		this.r22_num = r22_num;
	}
	public String getR26_qua_disc() {
		return r26_qua_disc;
	}
	public void setR26_qua_disc(String r26_qua_disc) {
		this.r26_qua_disc = r26_qua_disc;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_num() {
		return r26_num;
	}
	public void setR26_num(BigDecimal r26_num) {
		this.r26_num = r26_num;
	}
	public String getR27_qua_disc() {
		return r27_qua_disc;
	}
	public void setR27_qua_disc(String r27_qua_disc) {
		this.r27_qua_disc = r27_qua_disc;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_num() {
		return r27_num;
	}
	public void setR27_num(BigDecimal r27_num) {
		this.r27_num = r27_num;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_num() {
		return r28_num;
	}
	public void setR28_num(BigDecimal r28_num) {
		this.r28_num = r28_num;
	}
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
	public CREDIT_RISK_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
