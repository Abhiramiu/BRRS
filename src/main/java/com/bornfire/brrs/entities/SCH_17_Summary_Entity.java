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
@Table(name = "BRRS_SCH_17_SUMMARYTABLE")


public class SCH_17_Summary_Entity {
	
	

		
	private String	r9_product;
	private BigDecimal	r9_31_3_25_amt;
	private BigDecimal	r9_30_9_25_amt;

	private String	r10_product;
	private BigDecimal	r10_31_3_25_amt;
	private BigDecimal	r10_30_9_25_amt;

	private String	r11_product;
	private BigDecimal	r11_31_3_25_amt;
	private BigDecimal	r11_30_9_25_amt;

	private String	r12_product;
	private BigDecimal	r12_31_3_25_amt;
	private BigDecimal	r12_30_9_25_amt;

	private String	r13_product;
	private BigDecimal	r13_31_3_25_amt;
	private BigDecimal	r13_30_9_25_amt;

	private String	r14_product;
	private BigDecimal	r14_31_3_25_amt;
	private BigDecimal	r14_30_9_25_amt;

	private String	r15_product;
	private BigDecimal	r15_31_3_25_amt;
	private BigDecimal	r15_30_9_25_amt;

	private String	r16_product;
	private BigDecimal	r16_31_3_25_amt;
	private BigDecimal	r16_30_9_25_amt;

	private String	r17_product;
	private BigDecimal	r17_31_3_25_amt;
	private BigDecimal	r17_30_9_25_amt;

	private String	r18_product;
	private BigDecimal	r18_31_3_25_amt;
	private BigDecimal	r18_30_9_25_amt;

	private String	r19_product;
	private BigDecimal	r19_31_3_25_amt;
	private BigDecimal	r19_30_9_25_amt;

	private String	r20_product;
	private BigDecimal	r20_31_3_25_amt;
	private BigDecimal	r20_30_9_25_amt;




	               
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
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_31_3_25_amt() {
		return r9_31_3_25_amt;
	}
	public void setR9_31_3_25_amt(BigDecimal r9_31_3_25_amt) {
		this.r9_31_3_25_amt = r9_31_3_25_amt;
	}
	public BigDecimal getR9_30_9_25_amt() {
		return r9_30_9_25_amt;
	}
	public void setR9_30_9_25_amt(BigDecimal r9_30_9_25_amt) {
		this.r9_30_9_25_amt = r9_30_9_25_amt;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_31_3_25_amt() {
		return r10_31_3_25_amt;
	}
	public void setR10_31_3_25_amt(BigDecimal r10_31_3_25_amt) {
		this.r10_31_3_25_amt = r10_31_3_25_amt;
	}
	public BigDecimal getR10_30_9_25_amt() {
		return r10_30_9_25_amt;
	}
	public void setR10_30_9_25_amt(BigDecimal r10_30_9_25_amt) {
		this.r10_30_9_25_amt = r10_30_9_25_amt;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_31_3_25_amt() {
		return r11_31_3_25_amt;
	}
	public void setR11_31_3_25_amt(BigDecimal r11_31_3_25_amt) {
		this.r11_31_3_25_amt = r11_31_3_25_amt;
	}
	public BigDecimal getR11_30_9_25_amt() {
		return r11_30_9_25_amt;
	}
	public void setR11_30_9_25_amt(BigDecimal r11_30_9_25_amt) {
		this.r11_30_9_25_amt = r11_30_9_25_amt;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_31_3_25_amt() {
		return r12_31_3_25_amt;
	}
	public void setR12_31_3_25_amt(BigDecimal r12_31_3_25_amt) {
		this.r12_31_3_25_amt = r12_31_3_25_amt;
	}
	public BigDecimal getR12_30_9_25_amt() {
		return r12_30_9_25_amt;
	}
	public void setR12_30_9_25_amt(BigDecimal r12_30_9_25_amt) {
		this.r12_30_9_25_amt = r12_30_9_25_amt;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_31_3_25_amt() {
		return r13_31_3_25_amt;
	}
	public void setR13_31_3_25_amt(BigDecimal r13_31_3_25_amt) {
		this.r13_31_3_25_amt = r13_31_3_25_amt;
	}
	public BigDecimal getR13_30_9_25_amt() {
		return r13_30_9_25_amt;
	}
	public void setR13_30_9_25_amt(BigDecimal r13_30_9_25_amt) {
		this.r13_30_9_25_amt = r13_30_9_25_amt;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_31_3_25_amt() {
		return r14_31_3_25_amt;
	}
	public void setR14_31_3_25_amt(BigDecimal r14_31_3_25_amt) {
		this.r14_31_3_25_amt = r14_31_3_25_amt;
	}
	public BigDecimal getR14_30_9_25_amt() {
		return r14_30_9_25_amt;
	}
	public void setR14_30_9_25_amt(BigDecimal r14_30_9_25_amt) {
		this.r14_30_9_25_amt = r14_30_9_25_amt;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_31_3_25_amt() {
		return r15_31_3_25_amt;
	}
	public void setR15_31_3_25_amt(BigDecimal r15_31_3_25_amt) {
		this.r15_31_3_25_amt = r15_31_3_25_amt;
	}
	public BigDecimal getR15_30_9_25_amt() {
		return r15_30_9_25_amt;
	}
	public void setR15_30_9_25_amt(BigDecimal r15_30_9_25_amt) {
		this.r15_30_9_25_amt = r15_30_9_25_amt;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_31_3_25_amt() {
		return r16_31_3_25_amt;
	}
	public void setR16_31_3_25_amt(BigDecimal r16_31_3_25_amt) {
		this.r16_31_3_25_amt = r16_31_3_25_amt;
	}
	public BigDecimal getR16_30_9_25_amt() {
		return r16_30_9_25_amt;
	}
	public void setR16_30_9_25_amt(BigDecimal r16_30_9_25_amt) {
		this.r16_30_9_25_amt = r16_30_9_25_amt;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_31_3_25_amt() {
		return r17_31_3_25_amt;
	}
	public void setR17_31_3_25_amt(BigDecimal r17_31_3_25_amt) {
		this.r17_31_3_25_amt = r17_31_3_25_amt;
	}
	public BigDecimal getR17_30_9_25_amt() {
		return r17_30_9_25_amt;
	}
	public void setR17_30_9_25_amt(BigDecimal r17_30_9_25_amt) {
		this.r17_30_9_25_amt = r17_30_9_25_amt;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_31_3_25_amt() {
		return r18_31_3_25_amt;
	}
	public void setR18_31_3_25_amt(BigDecimal r18_31_3_25_amt) {
		this.r18_31_3_25_amt = r18_31_3_25_amt;
	}
	public BigDecimal getR18_30_9_25_amt() {
		return r18_30_9_25_amt;
	}
	public void setR18_30_9_25_amt(BigDecimal r18_30_9_25_amt) {
		this.r18_30_9_25_amt = r18_30_9_25_amt;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_31_3_25_amt() {
		return r19_31_3_25_amt;
	}
	public void setR19_31_3_25_amt(BigDecimal r19_31_3_25_amt) {
		this.r19_31_3_25_amt = r19_31_3_25_amt;
	}
	public BigDecimal getR19_30_9_25_amt() {
		return r19_30_9_25_amt;
	}
	public void setR19_30_9_25_amt(BigDecimal r19_30_9_25_amt) {
		this.r19_30_9_25_amt = r19_30_9_25_amt;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_31_3_25_amt() {
		return r20_31_3_25_amt;
	}
	public void setR20_31_3_25_amt(BigDecimal r20_31_3_25_amt) {
		this.r20_31_3_25_amt = r20_31_3_25_amt;
	}
	public BigDecimal getR20_30_9_25_amt() {
		return r20_30_9_25_amt;
	}
	public void setR20_30_9_25_amt(BigDecimal r20_30_9_25_amt) {
		this.r20_30_9_25_amt = r20_30_9_25_amt;
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
	public SCH_17_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
