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
@Table(name = "BRRS_M_CA2_MANUAL_SUMMARYTABLE")

public class M_CA2_Manual_Summary_Entity {

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
	
	private BigDecimal r11_amount_2;
	private BigDecimal r13_amount_1;
	private BigDecimal r14_amount_1;
	private BigDecimal r15_amount_1;
	private BigDecimal r16_amount_1;
	private BigDecimal r18_amount_1;
	private BigDecimal r19_amount_1;
	private BigDecimal r21_amount_1;
	private BigDecimal r32_amount_2;
	private BigDecimal r42_amount_2;
	private BigDecimal r43_amount_2;
	private BigDecimal r44_amount_2;
	private BigDecimal r46_amount_2;
		







	public BigDecimal getR11_amount_2() {
		return r11_amount_2;
	}







	public void setR11_amount_2(BigDecimal r11_amount_2) {
		this.r11_amount_2 = r11_amount_2;
	}









	public BigDecimal getR14_amount_1() {
		return r14_amount_1;
	}







	public void setR14_amount_1(BigDecimal r14_amount_1) {
		this.r14_amount_1 = r14_amount_1;
	}








	public BigDecimal getR15_amount_1() {
		return r15_amount_1;
	}







	public void setR15_amount_1(BigDecimal r15_amount_1) {
		this.r15_amount_1 = r15_amount_1;
	}



	public BigDecimal getR16_amount_1() {
		return r16_amount_1;
	}







	public void setR16_amount_1(BigDecimal r16_amount_1) {
		this.r16_amount_1 = r16_amount_1;
	}


	public BigDecimal getR18_amount_1() {
		return r18_amount_1;
	}







	public void setR18_amount_1(BigDecimal r18_amount_1) {
		this.r18_amount_1 = r18_amount_1;
	}


	public BigDecimal getR19_amount_1() {
		return r19_amount_1;
	}







	public void setR19_amount_1(BigDecimal r19_amount_1) {
		this.r19_amount_1 = r19_amount_1;
	}



	public BigDecimal getR21_amount_1() {
		return r21_amount_1;
	}







	public void setR21_amount_1(BigDecimal r21_amount_1) {
		this.r21_amount_1 = r21_amount_1;
	}




	public BigDecimal getR32_amount_2() {
		return r32_amount_2;
	}







	public void setR32_amount_2(BigDecimal r32_amount_2) {
		this.r32_amount_2 = r32_amount_2;
	}



	public BigDecimal getR42_amount_2() {
		return r42_amount_2;
	}







	public void setR42_amount_2(BigDecimal r42_amount_2) {
		this.r42_amount_2 = r42_amount_2;
	}



	public BigDecimal getR44_amount_2() {
		return r44_amount_2;
	}







	public void setR44_amount_2(BigDecimal r44_amount_2) {
		this.r44_amount_2 = r44_amount_2;
	}



	public BigDecimal getR46_amount_2() {
		return r46_amount_2;
	}







	public void setR46_amount_2(BigDecimal r46_amount_2) {
		this.r46_amount_2 = r46_amount_2;
	}


	
	

	public BigDecimal getR13_amount_1() {
		return r13_amount_1;
	}







	public void setR13_amount_1(BigDecimal r13_amount_1) {
		this.r13_amount_1 = r13_amount_1;
	}







	public BigDecimal getR43_amount_2() {
		return r43_amount_2;
	}







	public void setR43_amount_2(BigDecimal r43_amount_2) {
		this.r43_amount_2 = r43_amount_2;
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



	
	
	




	public M_CA2_Manual_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}


}