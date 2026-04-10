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
@Table(name = "BRRS_DEFERRED_TAX_SUMMARYTABLE")


public class DEFERRED_TAX_Summary_Entity {
	
	

	private String	r11_product;
	private BigDecimal	r11_31_asse_lc;
	private BigDecimal	r11_31_asse_inr;
	private BigDecimal	r11_31_liab_lc;
	private BigDecimal	r11_31_liab_inr;
	private BigDecimal	r11_30_asse_lc;
	private BigDecimal	r11_30_asse_inr;
	private BigDecimal	r11_30_liab_lc;
	private BigDecimal	r11_30_liab_inr;
	private String	r12_product;
	private BigDecimal	r12_31_asse_lc;
	private BigDecimal	r12_31_asse_inr;
	private BigDecimal	r12_31_liab_lc;
	private BigDecimal	r12_31_liab_inr;
	private BigDecimal	r12_30_asse_lc;
	private BigDecimal	r12_30_asse_inr;
	private BigDecimal	r12_30_liab_lc;
	private BigDecimal	r12_30_liab_inr;
	private String	r13_product;
	private BigDecimal	r13_31_asse_lc;
	private BigDecimal	r13_31_asse_inr;
	private BigDecimal	r13_31_liab_lc;
	private BigDecimal	r13_31_liab_inr;
	private BigDecimal	r13_30_asse_lc;
	private BigDecimal	r13_30_asse_inr;
	private BigDecimal	r13_30_liab_lc;
	private BigDecimal	r13_30_liab_inr;
	private String	r14_product;
	private BigDecimal	r14_31_asse_lc;
	private BigDecimal	r14_31_asse_inr;
	private BigDecimal	r14_31_liab_lc;
	private BigDecimal	r14_31_liab_inr;
	private BigDecimal	r14_30_asse_lc;
	private BigDecimal	r14_30_asse_inr;
	private BigDecimal	r14_30_liab_lc;
	private BigDecimal	r14_30_liab_inr;
	private String	r15_product;
	private BigDecimal	r15_31_asse_lc;
	private BigDecimal	r15_31_asse_inr;
	private BigDecimal	r15_31_liab_lc;
	private BigDecimal	r15_31_liab_inr;
	private BigDecimal	r15_30_asse_lc;
	private BigDecimal	r15_30_asse_inr;
	private BigDecimal	r15_30_liab_lc;
	private BigDecimal	r15_30_liab_inr;
	private String	r16_product;
	private BigDecimal	r16_31_asse_lc;
	private BigDecimal	r16_31_asse_inr;
	private BigDecimal	r16_31_liab_lc;
	private BigDecimal	r16_31_liab_inr;
	private BigDecimal	r16_30_asse_lc;
	private BigDecimal	r16_30_asse_inr;
	private BigDecimal	r16_30_liab_lc;
	private BigDecimal	r16_30_liab_inr;
	private String	r17_product;
	private BigDecimal	r17_31_asse_lc;
	private BigDecimal	r17_31_asse_inr;
	private BigDecimal	r17_31_liab_lc;
	private BigDecimal	r17_31_liab_inr;
	private BigDecimal	r17_30_asse_lc;
	private BigDecimal	r17_30_asse_inr;
	private BigDecimal	r17_30_liab_lc;
	private BigDecimal	r17_30_liab_inr;
	private String	r18_product;
	private BigDecimal	r18_31_asse_lc;
	private BigDecimal	r18_31_asse_inr;
	private BigDecimal	r18_31_liab_lc;
	private BigDecimal	r18_31_liab_inr;
	private BigDecimal	r18_30_asse_lc;
	private BigDecimal	r18_30_asse_inr;
	private BigDecimal	r18_30_liab_lc;
	private BigDecimal	r18_30_liab_inr;
	private String	r19_product;
	private BigDecimal	r19_31_asse_lc;
	private BigDecimal	r19_31_asse_inr;
	private BigDecimal	r19_31_liab_lc;
	private BigDecimal	r19_31_liab_inr;
	private BigDecimal	r19_30_asse_lc;
	private BigDecimal	r19_30_asse_inr;
	private BigDecimal	r19_30_liab_lc;
	private BigDecimal	r19_30_liab_inr;
	private String	r20_product;
	private BigDecimal	r20_31_asse_lc;
	private BigDecimal	r20_31_asse_inr;
	private BigDecimal	r20_31_liab_lc;
	private BigDecimal	r20_31_liab_inr;
	private BigDecimal	r20_30_asse_lc;
	private BigDecimal	r20_30_asse_inr;
	private BigDecimal	r20_30_liab_lc;
	private BigDecimal	r20_30_liab_inr;
	private String	r21_product;
	private BigDecimal	r21_31_asse_lc;
	private BigDecimal	r21_31_asse_inr;
	private BigDecimal	r21_31_liab_lc;
	private BigDecimal	r21_31_liab_inr;
	private BigDecimal	r21_30_asse_lc;
	private BigDecimal	r21_30_asse_inr;
	private BigDecimal	r21_30_liab_lc;
	private BigDecimal	r21_30_liab_inr;
	private String	r22_product;
	private BigDecimal	r22_31_asse_lc;
	private BigDecimal	r22_31_asse_inr;
	private BigDecimal	r22_31_liab_lc;
	private BigDecimal	r22_31_liab_inr;
	private BigDecimal	r22_30_asse_lc;
	private BigDecimal	r22_30_asse_inr;
	private BigDecimal	r22_30_liab_lc;
	private BigDecimal	r22_30_liab_inr;
	private String	r23_product;
	private BigDecimal	r23_31_asse_lc;
	private BigDecimal	r23_31_asse_inr;
	private BigDecimal	r23_31_liab_lc;
	private BigDecimal	r23_31_liab_inr;
	private BigDecimal	r23_30_asse_lc;
	private BigDecimal	r23_30_asse_inr;
	private BigDecimal	r23_30_liab_lc;
	private BigDecimal	r23_30_liab_inr;
	private String	r24_product;
	private BigDecimal	r24_31_asse_lc;
	private BigDecimal	r24_31_asse_inr;
	private BigDecimal	r24_31_liab_lc;
	private BigDecimal	r24_31_liab_inr;
	private BigDecimal	r24_30_asse_lc;
	private BigDecimal	r24_30_asse_inr;
	private BigDecimal	r24_30_liab_lc;
	private BigDecimal	r24_30_liab_inr;
	private String	r25_product;
	private BigDecimal	r25_31_asse_lc;
	private BigDecimal	r25_31_asse_inr;
	private BigDecimal	r25_31_liab_lc;
	private BigDecimal	r25_31_liab_inr;
	private BigDecimal	r25_30_asse_lc;
	private BigDecimal	r25_30_asse_inr;
	private BigDecimal	r25_30_liab_lc;
	private BigDecimal	r25_30_liab_inr;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_31_asse_lc() {
		return r11_31_asse_lc;
	}
	public void setR11_31_asse_lc(BigDecimal r11_31_asse_lc) {
		this.r11_31_asse_lc = r11_31_asse_lc;
	}
	public BigDecimal getR11_31_asse_inr() {
		return r11_31_asse_inr;
	}
	public void setR11_31_asse_inr(BigDecimal r11_31_asse_inr) {
		this.r11_31_asse_inr = r11_31_asse_inr;
	}
	public BigDecimal getR11_31_liab_lc() {
		return r11_31_liab_lc;
	}
	public void setR11_31_liab_lc(BigDecimal r11_31_liab_lc) {
		this.r11_31_liab_lc = r11_31_liab_lc;
	}
	public BigDecimal getR11_31_liab_inr() {
		return r11_31_liab_inr;
	}
	public void setR11_31_liab_inr(BigDecimal r11_31_liab_inr) {
		this.r11_31_liab_inr = r11_31_liab_inr;
	}
	public BigDecimal getR11_30_asse_lc() {
		return r11_30_asse_lc;
	}
	public void setR11_30_asse_lc(BigDecimal r11_30_asse_lc) {
		this.r11_30_asse_lc = r11_30_asse_lc;
	}
	public BigDecimal getR11_30_asse_inr() {
		return r11_30_asse_inr;
	}
	public void setR11_30_asse_inr(BigDecimal r11_30_asse_inr) {
		this.r11_30_asse_inr = r11_30_asse_inr;
	}
	public BigDecimal getR11_30_liab_lc() {
		return r11_30_liab_lc;
	}
	public void setR11_30_liab_lc(BigDecimal r11_30_liab_lc) {
		this.r11_30_liab_lc = r11_30_liab_lc;
	}
	public BigDecimal getR11_30_liab_inr() {
		return r11_30_liab_inr;
	}
	public void setR11_30_liab_inr(BigDecimal r11_30_liab_inr) {
		this.r11_30_liab_inr = r11_30_liab_inr;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_31_asse_lc() {
		return r12_31_asse_lc;
	}
	public void setR12_31_asse_lc(BigDecimal r12_31_asse_lc) {
		this.r12_31_asse_lc = r12_31_asse_lc;
	}
	public BigDecimal getR12_31_asse_inr() {
		return r12_31_asse_inr;
	}
	public void setR12_31_asse_inr(BigDecimal r12_31_asse_inr) {
		this.r12_31_asse_inr = r12_31_asse_inr;
	}
	public BigDecimal getR12_31_liab_lc() {
		return r12_31_liab_lc;
	}
	public void setR12_31_liab_lc(BigDecimal r12_31_liab_lc) {
		this.r12_31_liab_lc = r12_31_liab_lc;
	}
	public BigDecimal getR12_31_liab_inr() {
		return r12_31_liab_inr;
	}
	public void setR12_31_liab_inr(BigDecimal r12_31_liab_inr) {
		this.r12_31_liab_inr = r12_31_liab_inr;
	}
	public BigDecimal getR12_30_asse_lc() {
		return r12_30_asse_lc;
	}
	public void setR12_30_asse_lc(BigDecimal r12_30_asse_lc) {
		this.r12_30_asse_lc = r12_30_asse_lc;
	}
	public BigDecimal getR12_30_asse_inr() {
		return r12_30_asse_inr;
	}
	public void setR12_30_asse_inr(BigDecimal r12_30_asse_inr) {
		this.r12_30_asse_inr = r12_30_asse_inr;
	}
	public BigDecimal getR12_30_liab_lc() {
		return r12_30_liab_lc;
	}
	public void setR12_30_liab_lc(BigDecimal r12_30_liab_lc) {
		this.r12_30_liab_lc = r12_30_liab_lc;
	}
	public BigDecimal getR12_30_liab_inr() {
		return r12_30_liab_inr;
	}
	public void setR12_30_liab_inr(BigDecimal r12_30_liab_inr) {
		this.r12_30_liab_inr = r12_30_liab_inr;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_31_asse_lc() {
		return r13_31_asse_lc;
	}
	public void setR13_31_asse_lc(BigDecimal r13_31_asse_lc) {
		this.r13_31_asse_lc = r13_31_asse_lc;
	}
	public BigDecimal getR13_31_asse_inr() {
		return r13_31_asse_inr;
	}
	public void setR13_31_asse_inr(BigDecimal r13_31_asse_inr) {
		this.r13_31_asse_inr = r13_31_asse_inr;
	}
	public BigDecimal getR13_31_liab_lc() {
		return r13_31_liab_lc;
	}
	public void setR13_31_liab_lc(BigDecimal r13_31_liab_lc) {
		this.r13_31_liab_lc = r13_31_liab_lc;
	}
	public BigDecimal getR13_31_liab_inr() {
		return r13_31_liab_inr;
	}
	public void setR13_31_liab_inr(BigDecimal r13_31_liab_inr) {
		this.r13_31_liab_inr = r13_31_liab_inr;
	}
	public BigDecimal getR13_30_asse_lc() {
		return r13_30_asse_lc;
	}
	public void setR13_30_asse_lc(BigDecimal r13_30_asse_lc) {
		this.r13_30_asse_lc = r13_30_asse_lc;
	}
	public BigDecimal getR13_30_asse_inr() {
		return r13_30_asse_inr;
	}
	public void setR13_30_asse_inr(BigDecimal r13_30_asse_inr) {
		this.r13_30_asse_inr = r13_30_asse_inr;
	}
	public BigDecimal getR13_30_liab_lc() {
		return r13_30_liab_lc;
	}
	public void setR13_30_liab_lc(BigDecimal r13_30_liab_lc) {
		this.r13_30_liab_lc = r13_30_liab_lc;
	}
	public BigDecimal getR13_30_liab_inr() {
		return r13_30_liab_inr;
	}
	public void setR13_30_liab_inr(BigDecimal r13_30_liab_inr) {
		this.r13_30_liab_inr = r13_30_liab_inr;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_31_asse_lc() {
		return r14_31_asse_lc;
	}
	public void setR14_31_asse_lc(BigDecimal r14_31_asse_lc) {
		this.r14_31_asse_lc = r14_31_asse_lc;
	}
	public BigDecimal getR14_31_asse_inr() {
		return r14_31_asse_inr;
	}
	public void setR14_31_asse_inr(BigDecimal r14_31_asse_inr) {
		this.r14_31_asse_inr = r14_31_asse_inr;
	}
	public BigDecimal getR14_31_liab_lc() {
		return r14_31_liab_lc;
	}
	public void setR14_31_liab_lc(BigDecimal r14_31_liab_lc) {
		this.r14_31_liab_lc = r14_31_liab_lc;
	}
	public BigDecimal getR14_31_liab_inr() {
		return r14_31_liab_inr;
	}
	public void setR14_31_liab_inr(BigDecimal r14_31_liab_inr) {
		this.r14_31_liab_inr = r14_31_liab_inr;
	}
	public BigDecimal getR14_30_asse_lc() {
		return r14_30_asse_lc;
	}
	public void setR14_30_asse_lc(BigDecimal r14_30_asse_lc) {
		this.r14_30_asse_lc = r14_30_asse_lc;
	}
	public BigDecimal getR14_30_asse_inr() {
		return r14_30_asse_inr;
	}
	public void setR14_30_asse_inr(BigDecimal r14_30_asse_inr) {
		this.r14_30_asse_inr = r14_30_asse_inr;
	}
	public BigDecimal getR14_30_liab_lc() {
		return r14_30_liab_lc;
	}
	public void setR14_30_liab_lc(BigDecimal r14_30_liab_lc) {
		this.r14_30_liab_lc = r14_30_liab_lc;
	}
	public BigDecimal getR14_30_liab_inr() {
		return r14_30_liab_inr;
	}
	public void setR14_30_liab_inr(BigDecimal r14_30_liab_inr) {
		this.r14_30_liab_inr = r14_30_liab_inr;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_31_asse_lc() {
		return r15_31_asse_lc;
	}
	public void setR15_31_asse_lc(BigDecimal r15_31_asse_lc) {
		this.r15_31_asse_lc = r15_31_asse_lc;
	}
	public BigDecimal getR15_31_asse_inr() {
		return r15_31_asse_inr;
	}
	public void setR15_31_asse_inr(BigDecimal r15_31_asse_inr) {
		this.r15_31_asse_inr = r15_31_asse_inr;
	}
	public BigDecimal getR15_31_liab_lc() {
		return r15_31_liab_lc;
	}
	public void setR15_31_liab_lc(BigDecimal r15_31_liab_lc) {
		this.r15_31_liab_lc = r15_31_liab_lc;
	}
	public BigDecimal getR15_31_liab_inr() {
		return r15_31_liab_inr;
	}
	public void setR15_31_liab_inr(BigDecimal r15_31_liab_inr) {
		this.r15_31_liab_inr = r15_31_liab_inr;
	}
	public BigDecimal getR15_30_asse_lc() {
		return r15_30_asse_lc;
	}
	public void setR15_30_asse_lc(BigDecimal r15_30_asse_lc) {
		this.r15_30_asse_lc = r15_30_asse_lc;
	}
	public BigDecimal getR15_30_asse_inr() {
		return r15_30_asse_inr;
	}
	public void setR15_30_asse_inr(BigDecimal r15_30_asse_inr) {
		this.r15_30_asse_inr = r15_30_asse_inr;
	}
	public BigDecimal getR15_30_liab_lc() {
		return r15_30_liab_lc;
	}
	public void setR15_30_liab_lc(BigDecimal r15_30_liab_lc) {
		this.r15_30_liab_lc = r15_30_liab_lc;
	}
	public BigDecimal getR15_30_liab_inr() {
		return r15_30_liab_inr;
	}
	public void setR15_30_liab_inr(BigDecimal r15_30_liab_inr) {
		this.r15_30_liab_inr = r15_30_liab_inr;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_31_asse_lc() {
		return r16_31_asse_lc;
	}
	public void setR16_31_asse_lc(BigDecimal r16_31_asse_lc) {
		this.r16_31_asse_lc = r16_31_asse_lc;
	}
	public BigDecimal getR16_31_asse_inr() {
		return r16_31_asse_inr;
	}
	public void setR16_31_asse_inr(BigDecimal r16_31_asse_inr) {
		this.r16_31_asse_inr = r16_31_asse_inr;
	}
	public BigDecimal getR16_31_liab_lc() {
		return r16_31_liab_lc;
	}
	public void setR16_31_liab_lc(BigDecimal r16_31_liab_lc) {
		this.r16_31_liab_lc = r16_31_liab_lc;
	}
	public BigDecimal getR16_31_liab_inr() {
		return r16_31_liab_inr;
	}
	public void setR16_31_liab_inr(BigDecimal r16_31_liab_inr) {
		this.r16_31_liab_inr = r16_31_liab_inr;
	}
	public BigDecimal getR16_30_asse_lc() {
		return r16_30_asse_lc;
	}
	public void setR16_30_asse_lc(BigDecimal r16_30_asse_lc) {
		this.r16_30_asse_lc = r16_30_asse_lc;
	}
	public BigDecimal getR16_30_asse_inr() {
		return r16_30_asse_inr;
	}
	public void setR16_30_asse_inr(BigDecimal r16_30_asse_inr) {
		this.r16_30_asse_inr = r16_30_asse_inr;
	}
	public BigDecimal getR16_30_liab_lc() {
		return r16_30_liab_lc;
	}
	public void setR16_30_liab_lc(BigDecimal r16_30_liab_lc) {
		this.r16_30_liab_lc = r16_30_liab_lc;
	}
	public BigDecimal getR16_30_liab_inr() {
		return r16_30_liab_inr;
	}
	public void setR16_30_liab_inr(BigDecimal r16_30_liab_inr) {
		this.r16_30_liab_inr = r16_30_liab_inr;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_31_asse_lc() {
		return r17_31_asse_lc;
	}
	public void setR17_31_asse_lc(BigDecimal r17_31_asse_lc) {
		this.r17_31_asse_lc = r17_31_asse_lc;
	}
	public BigDecimal getR17_31_asse_inr() {
		return r17_31_asse_inr;
	}
	public void setR17_31_asse_inr(BigDecimal r17_31_asse_inr) {
		this.r17_31_asse_inr = r17_31_asse_inr;
	}
	public BigDecimal getR17_31_liab_lc() {
		return r17_31_liab_lc;
	}
	public void setR17_31_liab_lc(BigDecimal r17_31_liab_lc) {
		this.r17_31_liab_lc = r17_31_liab_lc;
	}
	public BigDecimal getR17_31_liab_inr() {
		return r17_31_liab_inr;
	}
	public void setR17_31_liab_inr(BigDecimal r17_31_liab_inr) {
		this.r17_31_liab_inr = r17_31_liab_inr;
	}
	public BigDecimal getR17_30_asse_lc() {
		return r17_30_asse_lc;
	}
	public void setR17_30_asse_lc(BigDecimal r17_30_asse_lc) {
		this.r17_30_asse_lc = r17_30_asse_lc;
	}
	public BigDecimal getR17_30_asse_inr() {
		return r17_30_asse_inr;
	}
	public void setR17_30_asse_inr(BigDecimal r17_30_asse_inr) {
		this.r17_30_asse_inr = r17_30_asse_inr;
	}
	public BigDecimal getR17_30_liab_lc() {
		return r17_30_liab_lc;
	}
	public void setR17_30_liab_lc(BigDecimal r17_30_liab_lc) {
		this.r17_30_liab_lc = r17_30_liab_lc;
	}
	public BigDecimal getR17_30_liab_inr() {
		return r17_30_liab_inr;
	}
	public void setR17_30_liab_inr(BigDecimal r17_30_liab_inr) {
		this.r17_30_liab_inr = r17_30_liab_inr;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_31_asse_lc() {
		return r18_31_asse_lc;
	}
	public void setR18_31_asse_lc(BigDecimal r18_31_asse_lc) {
		this.r18_31_asse_lc = r18_31_asse_lc;
	}
	public BigDecimal getR18_31_asse_inr() {
		return r18_31_asse_inr;
	}
	public void setR18_31_asse_inr(BigDecimal r18_31_asse_inr) {
		this.r18_31_asse_inr = r18_31_asse_inr;
	}
	public BigDecimal getR18_31_liab_lc() {
		return r18_31_liab_lc;
	}
	public void setR18_31_liab_lc(BigDecimal r18_31_liab_lc) {
		this.r18_31_liab_lc = r18_31_liab_lc;
	}
	public BigDecimal getR18_31_liab_inr() {
		return r18_31_liab_inr;
	}
	public void setR18_31_liab_inr(BigDecimal r18_31_liab_inr) {
		this.r18_31_liab_inr = r18_31_liab_inr;
	}
	public BigDecimal getR18_30_asse_lc() {
		return r18_30_asse_lc;
	}
	public void setR18_30_asse_lc(BigDecimal r18_30_asse_lc) {
		this.r18_30_asse_lc = r18_30_asse_lc;
	}
	public BigDecimal getR18_30_asse_inr() {
		return r18_30_asse_inr;
	}
	public void setR18_30_asse_inr(BigDecimal r18_30_asse_inr) {
		this.r18_30_asse_inr = r18_30_asse_inr;
	}
	public BigDecimal getR18_30_liab_lc() {
		return r18_30_liab_lc;
	}
	public void setR18_30_liab_lc(BigDecimal r18_30_liab_lc) {
		this.r18_30_liab_lc = r18_30_liab_lc;
	}
	public BigDecimal getR18_30_liab_inr() {
		return r18_30_liab_inr;
	}
	public void setR18_30_liab_inr(BigDecimal r18_30_liab_inr) {
		this.r18_30_liab_inr = r18_30_liab_inr;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_31_asse_lc() {
		return r19_31_asse_lc;
	}
	public void setR19_31_asse_lc(BigDecimal r19_31_asse_lc) {
		this.r19_31_asse_lc = r19_31_asse_lc;
	}
	public BigDecimal getR19_31_asse_inr() {
		return r19_31_asse_inr;
	}
	public void setR19_31_asse_inr(BigDecimal r19_31_asse_inr) {
		this.r19_31_asse_inr = r19_31_asse_inr;
	}
	public BigDecimal getR19_31_liab_lc() {
		return r19_31_liab_lc;
	}
	public void setR19_31_liab_lc(BigDecimal r19_31_liab_lc) {
		this.r19_31_liab_lc = r19_31_liab_lc;
	}
	public BigDecimal getR19_31_liab_inr() {
		return r19_31_liab_inr;
	}
	public void setR19_31_liab_inr(BigDecimal r19_31_liab_inr) {
		this.r19_31_liab_inr = r19_31_liab_inr;
	}
	public BigDecimal getR19_30_asse_lc() {
		return r19_30_asse_lc;
	}
	public void setR19_30_asse_lc(BigDecimal r19_30_asse_lc) {
		this.r19_30_asse_lc = r19_30_asse_lc;
	}
	public BigDecimal getR19_30_asse_inr() {
		return r19_30_asse_inr;
	}
	public void setR19_30_asse_inr(BigDecimal r19_30_asse_inr) {
		this.r19_30_asse_inr = r19_30_asse_inr;
	}
	public BigDecimal getR19_30_liab_lc() {
		return r19_30_liab_lc;
	}
	public void setR19_30_liab_lc(BigDecimal r19_30_liab_lc) {
		this.r19_30_liab_lc = r19_30_liab_lc;
	}
	public BigDecimal getR19_30_liab_inr() {
		return r19_30_liab_inr;
	}
	public void setR19_30_liab_inr(BigDecimal r19_30_liab_inr) {
		this.r19_30_liab_inr = r19_30_liab_inr;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_31_asse_lc() {
		return r20_31_asse_lc;
	}
	public void setR20_31_asse_lc(BigDecimal r20_31_asse_lc) {
		this.r20_31_asse_lc = r20_31_asse_lc;
	}
	public BigDecimal getR20_31_asse_inr() {
		return r20_31_asse_inr;
	}
	public void setR20_31_asse_inr(BigDecimal r20_31_asse_inr) {
		this.r20_31_asse_inr = r20_31_asse_inr;
	}
	public BigDecimal getR20_31_liab_lc() {
		return r20_31_liab_lc;
	}
	public void setR20_31_liab_lc(BigDecimal r20_31_liab_lc) {
		this.r20_31_liab_lc = r20_31_liab_lc;
	}
	public BigDecimal getR20_31_liab_inr() {
		return r20_31_liab_inr;
	}
	public void setR20_31_liab_inr(BigDecimal r20_31_liab_inr) {
		this.r20_31_liab_inr = r20_31_liab_inr;
	}
	public BigDecimal getR20_30_asse_lc() {
		return r20_30_asse_lc;
	}
	public void setR20_30_asse_lc(BigDecimal r20_30_asse_lc) {
		this.r20_30_asse_lc = r20_30_asse_lc;
	}
	public BigDecimal getR20_30_asse_inr() {
		return r20_30_asse_inr;
	}
	public void setR20_30_asse_inr(BigDecimal r20_30_asse_inr) {
		this.r20_30_asse_inr = r20_30_asse_inr;
	}
	public BigDecimal getR20_30_liab_lc() {
		return r20_30_liab_lc;
	}
	public void setR20_30_liab_lc(BigDecimal r20_30_liab_lc) {
		this.r20_30_liab_lc = r20_30_liab_lc;
	}
	public BigDecimal getR20_30_liab_inr() {
		return r20_30_liab_inr;
	}
	public void setR20_30_liab_inr(BigDecimal r20_30_liab_inr) {
		this.r20_30_liab_inr = r20_30_liab_inr;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_31_asse_lc() {
		return r21_31_asse_lc;
	}
	public void setR21_31_asse_lc(BigDecimal r21_31_asse_lc) {
		this.r21_31_asse_lc = r21_31_asse_lc;
	}
	public BigDecimal getR21_31_asse_inr() {
		return r21_31_asse_inr;
	}
	public void setR21_31_asse_inr(BigDecimal r21_31_asse_inr) {
		this.r21_31_asse_inr = r21_31_asse_inr;
	}
	public BigDecimal getR21_31_liab_lc() {
		return r21_31_liab_lc;
	}
	public void setR21_31_liab_lc(BigDecimal r21_31_liab_lc) {
		this.r21_31_liab_lc = r21_31_liab_lc;
	}
	public BigDecimal getR21_31_liab_inr() {
		return r21_31_liab_inr;
	}
	public void setR21_31_liab_inr(BigDecimal r21_31_liab_inr) {
		this.r21_31_liab_inr = r21_31_liab_inr;
	}
	public BigDecimal getR21_30_asse_lc() {
		return r21_30_asse_lc;
	}
	public void setR21_30_asse_lc(BigDecimal r21_30_asse_lc) {
		this.r21_30_asse_lc = r21_30_asse_lc;
	}
	public BigDecimal getR21_30_asse_inr() {
		return r21_30_asse_inr;
	}
	public void setR21_30_asse_inr(BigDecimal r21_30_asse_inr) {
		this.r21_30_asse_inr = r21_30_asse_inr;
	}
	public BigDecimal getR21_30_liab_lc() {
		return r21_30_liab_lc;
	}
	public void setR21_30_liab_lc(BigDecimal r21_30_liab_lc) {
		this.r21_30_liab_lc = r21_30_liab_lc;
	}
	public BigDecimal getR21_30_liab_inr() {
		return r21_30_liab_inr;
	}
	public void setR21_30_liab_inr(BigDecimal r21_30_liab_inr) {
		this.r21_30_liab_inr = r21_30_liab_inr;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_31_asse_lc() {
		return r22_31_asse_lc;
	}
	public void setR22_31_asse_lc(BigDecimal r22_31_asse_lc) {
		this.r22_31_asse_lc = r22_31_asse_lc;
	}
	public BigDecimal getR22_31_asse_inr() {
		return r22_31_asse_inr;
	}
	public void setR22_31_asse_inr(BigDecimal r22_31_asse_inr) {
		this.r22_31_asse_inr = r22_31_asse_inr;
	}
	public BigDecimal getR22_31_liab_lc() {
		return r22_31_liab_lc;
	}
	public void setR22_31_liab_lc(BigDecimal r22_31_liab_lc) {
		this.r22_31_liab_lc = r22_31_liab_lc;
	}
	public BigDecimal getR22_31_liab_inr() {
		return r22_31_liab_inr;
	}
	public void setR22_31_liab_inr(BigDecimal r22_31_liab_inr) {
		this.r22_31_liab_inr = r22_31_liab_inr;
	}
	public BigDecimal getR22_30_asse_lc() {
		return r22_30_asse_lc;
	}
	public void setR22_30_asse_lc(BigDecimal r22_30_asse_lc) {
		this.r22_30_asse_lc = r22_30_asse_lc;
	}
	public BigDecimal getR22_30_asse_inr() {
		return r22_30_asse_inr;
	}
	public void setR22_30_asse_inr(BigDecimal r22_30_asse_inr) {
		this.r22_30_asse_inr = r22_30_asse_inr;
	}
	public BigDecimal getR22_30_liab_lc() {
		return r22_30_liab_lc;
	}
	public void setR22_30_liab_lc(BigDecimal r22_30_liab_lc) {
		this.r22_30_liab_lc = r22_30_liab_lc;
	}
	public BigDecimal getR22_30_liab_inr() {
		return r22_30_liab_inr;
	}
	public void setR22_30_liab_inr(BigDecimal r22_30_liab_inr) {
		this.r22_30_liab_inr = r22_30_liab_inr;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_31_asse_lc() {
		return r23_31_asse_lc;
	}
	public void setR23_31_asse_lc(BigDecimal r23_31_asse_lc) {
		this.r23_31_asse_lc = r23_31_asse_lc;
	}
	public BigDecimal getR23_31_asse_inr() {
		return r23_31_asse_inr;
	}
	public void setR23_31_asse_inr(BigDecimal r23_31_asse_inr) {
		this.r23_31_asse_inr = r23_31_asse_inr;
	}
	public BigDecimal getR23_31_liab_lc() {
		return r23_31_liab_lc;
	}
	public void setR23_31_liab_lc(BigDecimal r23_31_liab_lc) {
		this.r23_31_liab_lc = r23_31_liab_lc;
	}
	public BigDecimal getR23_31_liab_inr() {
		return r23_31_liab_inr;
	}
	public void setR23_31_liab_inr(BigDecimal r23_31_liab_inr) {
		this.r23_31_liab_inr = r23_31_liab_inr;
	}
	public BigDecimal getR23_30_asse_lc() {
		return r23_30_asse_lc;
	}
	public void setR23_30_asse_lc(BigDecimal r23_30_asse_lc) {
		this.r23_30_asse_lc = r23_30_asse_lc;
	}
	public BigDecimal getR23_30_asse_inr() {
		return r23_30_asse_inr;
	}
	public void setR23_30_asse_inr(BigDecimal r23_30_asse_inr) {
		this.r23_30_asse_inr = r23_30_asse_inr;
	}
	public BigDecimal getR23_30_liab_lc() {
		return r23_30_liab_lc;
	}
	public void setR23_30_liab_lc(BigDecimal r23_30_liab_lc) {
		this.r23_30_liab_lc = r23_30_liab_lc;
	}
	public BigDecimal getR23_30_liab_inr() {
		return r23_30_liab_inr;
	}
	public void setR23_30_liab_inr(BigDecimal r23_30_liab_inr) {
		this.r23_30_liab_inr = r23_30_liab_inr;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_31_asse_lc() {
		return r24_31_asse_lc;
	}
	public void setR24_31_asse_lc(BigDecimal r24_31_asse_lc) {
		this.r24_31_asse_lc = r24_31_asse_lc;
	}
	public BigDecimal getR24_31_asse_inr() {
		return r24_31_asse_inr;
	}
	public void setR24_31_asse_inr(BigDecimal r24_31_asse_inr) {
		this.r24_31_asse_inr = r24_31_asse_inr;
	}
	public BigDecimal getR24_31_liab_lc() {
		return r24_31_liab_lc;
	}
	public void setR24_31_liab_lc(BigDecimal r24_31_liab_lc) {
		this.r24_31_liab_lc = r24_31_liab_lc;
	}
	public BigDecimal getR24_31_liab_inr() {
		return r24_31_liab_inr;
	}
	public void setR24_31_liab_inr(BigDecimal r24_31_liab_inr) {
		this.r24_31_liab_inr = r24_31_liab_inr;
	}
	public BigDecimal getR24_30_asse_lc() {
		return r24_30_asse_lc;
	}
	public void setR24_30_asse_lc(BigDecimal r24_30_asse_lc) {
		this.r24_30_asse_lc = r24_30_asse_lc;
	}
	public BigDecimal getR24_30_asse_inr() {
		return r24_30_asse_inr;
	}
	public void setR24_30_asse_inr(BigDecimal r24_30_asse_inr) {
		this.r24_30_asse_inr = r24_30_asse_inr;
	}
	public BigDecimal getR24_30_liab_lc() {
		return r24_30_liab_lc;
	}
	public void setR24_30_liab_lc(BigDecimal r24_30_liab_lc) {
		this.r24_30_liab_lc = r24_30_liab_lc;
	}
	public BigDecimal getR24_30_liab_inr() {
		return r24_30_liab_inr;
	}
	public void setR24_30_liab_inr(BigDecimal r24_30_liab_inr) {
		this.r24_30_liab_inr = r24_30_liab_inr;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_31_asse_lc() {
		return r25_31_asse_lc;
	}
	public void setR25_31_asse_lc(BigDecimal r25_31_asse_lc) {
		this.r25_31_asse_lc = r25_31_asse_lc;
	}
	public BigDecimal getR25_31_asse_inr() {
		return r25_31_asse_inr;
	}
	public void setR25_31_asse_inr(BigDecimal r25_31_asse_inr) {
		this.r25_31_asse_inr = r25_31_asse_inr;
	}
	public BigDecimal getR25_31_liab_lc() {
		return r25_31_liab_lc;
	}
	public void setR25_31_liab_lc(BigDecimal r25_31_liab_lc) {
		this.r25_31_liab_lc = r25_31_liab_lc;
	}
	public BigDecimal getR25_31_liab_inr() {
		return r25_31_liab_inr;
	}
	public void setR25_31_liab_inr(BigDecimal r25_31_liab_inr) {
		this.r25_31_liab_inr = r25_31_liab_inr;
	}
	public BigDecimal getR25_30_asse_lc() {
		return r25_30_asse_lc;
	}
	public void setR25_30_asse_lc(BigDecimal r25_30_asse_lc) {
		this.r25_30_asse_lc = r25_30_asse_lc;
	}
	public BigDecimal getR25_30_asse_inr() {
		return r25_30_asse_inr;
	}
	public void setR25_30_asse_inr(BigDecimal r25_30_asse_inr) {
		this.r25_30_asse_inr = r25_30_asse_inr;
	}
	public BigDecimal getR25_30_liab_lc() {
		return r25_30_liab_lc;
	}
	public void setR25_30_liab_lc(BigDecimal r25_30_liab_lc) {
		this.r25_30_liab_lc = r25_30_liab_lc;
	}
	public BigDecimal getR25_30_liab_inr() {
		return r25_30_liab_inr;
	}
	public void setR25_30_liab_inr(BigDecimal r25_30_liab_inr) {
		this.r25_30_liab_inr = r25_30_liab_inr;
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
	public DEFERRED_TAX_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
