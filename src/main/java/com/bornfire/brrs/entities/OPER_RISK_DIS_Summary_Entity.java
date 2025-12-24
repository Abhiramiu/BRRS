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
@Table(name = "BRRS_OPER_RISK_DIS_SUMMARYTABLE")


public class OPER_RISK_DIS_Summary_Entity {
	
	

		
	
	private String	r3_qua_name ;
	private String	r3_product;
	private BigDecimal	r3_amt;

	private String	r4_qua_name ;
	private String	r4_product;
	private BigDecimal	r4_amt;

	private String	r9_qua_name ;
	private String	r9_product;
	private BigDecimal	r9_amt;

	private String	r10_product;
	private BigDecimal	r10_amt;

	private String	r11_qua_name ;
	private String	r11_product;
	private BigDecimal	r11_amt;

	private String	r12_product;
	private BigDecimal	r12_amt;

	private String	r18_tot_remu_cur_yr;
	private BigDecimal	r18_unrestricted;
	private BigDecimal	r18_deferred;
	private String	r19_tot_remu_cur_yr;
	private BigDecimal	r19_unrestricted;
	private BigDecimal	r19_deferred;
	private String	r20_tot_remu_cur_yr;
	private BigDecimal	r20_unrestricted;
	private BigDecimal	r20_deferred;
	private String	r21_tot_remu_cur_yr;
	private BigDecimal	r21_unrestricted;
	private BigDecimal	r21_deferred;
	private String	r22_tot_remu_cur_yr;
	private BigDecimal	r22_unrestricted;
	private BigDecimal	r22_deferred;
	private String	r23_tot_remu_cur_yr;
	private BigDecimal	r23_unrestricted;
	private BigDecimal	r23_deferred;
	private String	r24_tot_remu_cur_yr;
	private BigDecimal	r24_unrestricted;
	private BigDecimal	r24_deferred;
	private String	r25_tot_remu_cur_yr;
	private BigDecimal	r25_unrestricted;
	private BigDecimal	r25_deferred;

	
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
	public String getR3_qua_name() {
		return r3_qua_name;
	}
	public void setR3_qua_name(String r3_qua_name) {
		this.r3_qua_name = r3_qua_name;
	}
	public String getR3_product() {
		return r3_product;
	}
	public void setR3_product(String r3_product) {
		this.r3_product = r3_product;
	}
	public BigDecimal getR3_amt() {
		return r3_amt;
	}
	public void setR3_amt(BigDecimal r3_amt) {
		this.r3_amt = r3_amt;
	}
	public String getR4_qua_name() {
		return r4_qua_name;
	}
	public void setR4_qua_name(String r4_qua_name) {
		this.r4_qua_name = r4_qua_name;
	}
	public String getR4_product() {
		return r4_product;
	}
	public void setR4_product(String r4_product) {
		this.r4_product = r4_product;
	}
	public BigDecimal getR4_amt() {
		return r4_amt;
	}
	public void setR4_amt(BigDecimal r4_amt) {
		this.r4_amt = r4_amt;
	}
	public String getR9_qua_name() {
		return r9_qua_name;
	}
	public void setR9_qua_name(String r9_qua_name) {
		this.r9_qua_name = r9_qua_name;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amt() {
		return r9_amt;
	}
	public void setR9_amt(BigDecimal r9_amt) {
		this.r9_amt = r9_amt;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amt() {
		return r10_amt;
	}
	public void setR10_amt(BigDecimal r10_amt) {
		this.r10_amt = r10_amt;
	}
	public String getR11_qua_name() {
		return r11_qua_name;
	}
	public void setR11_qua_name(String r11_qua_name) {
		this.r11_qua_name = r11_qua_name;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_amt() {
		return r11_amt;
	}
	public void setR11_amt(BigDecimal r11_amt) {
		this.r11_amt = r11_amt;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_amt() {
		return r12_amt;
	}
	public void setR12_amt(BigDecimal r12_amt) {
		this.r12_amt = r12_amt;
	}
	public String getR18_tot_remu_cur_yr() {
		return r18_tot_remu_cur_yr;
	}
	public void setR18_tot_remu_cur_yr(String r18_tot_remu_cur_yr) {
		this.r18_tot_remu_cur_yr = r18_tot_remu_cur_yr;
	}
	public BigDecimal getR18_unrestricted() {
		return r18_unrestricted;
	}
	public void setR18_unrestricted(BigDecimal r18_unrestricted) {
		this.r18_unrestricted = r18_unrestricted;
	}
	public BigDecimal getR18_deferred() {
		return r18_deferred;
	}
	public void setR18_deferred(BigDecimal r18_deferred) {
		this.r18_deferred = r18_deferred;
	}
	public String getR19_tot_remu_cur_yr() {
		return r19_tot_remu_cur_yr;
	}
	public void setR19_tot_remu_cur_yr(String r19_tot_remu_cur_yr) {
		this.r19_tot_remu_cur_yr = r19_tot_remu_cur_yr;
	}
	public BigDecimal getR19_unrestricted() {
		return r19_unrestricted;
	}
	public void setR19_unrestricted(BigDecimal r19_unrestricted) {
		this.r19_unrestricted = r19_unrestricted;
	}
	public BigDecimal getR19_deferred() {
		return r19_deferred;
	}
	public void setR19_deferred(BigDecimal r19_deferred) {
		this.r19_deferred = r19_deferred;
	}
	public String getR20_tot_remu_cur_yr() {
		return r20_tot_remu_cur_yr;
	}
	public void setR20_tot_remu_cur_yr(String r20_tot_remu_cur_yr) {
		this.r20_tot_remu_cur_yr = r20_tot_remu_cur_yr;
	}
	public BigDecimal getR20_unrestricted() {
		return r20_unrestricted;
	}
	public void setR20_unrestricted(BigDecimal r20_unrestricted) {
		this.r20_unrestricted = r20_unrestricted;
	}
	public BigDecimal getR20_deferred() {
		return r20_deferred;
	}
	public void setR20_deferred(BigDecimal r20_deferred) {
		this.r20_deferred = r20_deferred;
	}
	public String getR21_tot_remu_cur_yr() {
		return r21_tot_remu_cur_yr;
	}
	public void setR21_tot_remu_cur_yr(String r21_tot_remu_cur_yr) {
		this.r21_tot_remu_cur_yr = r21_tot_remu_cur_yr;
	}
	public BigDecimal getR21_unrestricted() {
		return r21_unrestricted;
	}
	public void setR21_unrestricted(BigDecimal r21_unrestricted) {
		this.r21_unrestricted = r21_unrestricted;
	}
	public BigDecimal getR21_deferred() {
		return r21_deferred;
	}
	public void setR21_deferred(BigDecimal r21_deferred) {
		this.r21_deferred = r21_deferred;
	}
	public String getR22_tot_remu_cur_yr() {
		return r22_tot_remu_cur_yr;
	}
	public void setR22_tot_remu_cur_yr(String r22_tot_remu_cur_yr) {
		this.r22_tot_remu_cur_yr = r22_tot_remu_cur_yr;
	}
	public BigDecimal getR22_unrestricted() {
		return r22_unrestricted;
	}
	public void setR22_unrestricted(BigDecimal r22_unrestricted) {
		this.r22_unrestricted = r22_unrestricted;
	}
	public BigDecimal getR22_deferred() {
		return r22_deferred;
	}
	public void setR22_deferred(BigDecimal r22_deferred) {
		this.r22_deferred = r22_deferred;
	}
	public String getR23_tot_remu_cur_yr() {
		return r23_tot_remu_cur_yr;
	}
	public void setR23_tot_remu_cur_yr(String r23_tot_remu_cur_yr) {
		this.r23_tot_remu_cur_yr = r23_tot_remu_cur_yr;
	}
	public BigDecimal getR23_unrestricted() {
		return r23_unrestricted;
	}
	public void setR23_unrestricted(BigDecimal r23_unrestricted) {
		this.r23_unrestricted = r23_unrestricted;
	}
	public BigDecimal getR23_deferred() {
		return r23_deferred;
	}
	public void setR23_deferred(BigDecimal r23_deferred) {
		this.r23_deferred = r23_deferred;
	}
	public String getR24_tot_remu_cur_yr() {
		return r24_tot_remu_cur_yr;
	}
	public void setR24_tot_remu_cur_yr(String r24_tot_remu_cur_yr) {
		this.r24_tot_remu_cur_yr = r24_tot_remu_cur_yr;
	}
	public BigDecimal getR24_unrestricted() {
		return r24_unrestricted;
	}
	public void setR24_unrestricted(BigDecimal r24_unrestricted) {
		this.r24_unrestricted = r24_unrestricted;
	}
	public BigDecimal getR24_deferred() {
		return r24_deferred;
	}
	public void setR24_deferred(BigDecimal r24_deferred) {
		this.r24_deferred = r24_deferred;
	}
	public String getR25_tot_remu_cur_yr() {
		return r25_tot_remu_cur_yr;
	}
	public void setR25_tot_remu_cur_yr(String r25_tot_remu_cur_yr) {
		this.r25_tot_remu_cur_yr = r25_tot_remu_cur_yr;
	}
	public BigDecimal getR25_unrestricted() {
		return r25_unrestricted;
	}
	public void setR25_unrestricted(BigDecimal r25_unrestricted) {
		this.r25_unrestricted = r25_unrestricted;
	}
	public BigDecimal getR25_deferred() {
		return r25_deferred;
	}
	public void setR25_deferred(BigDecimal r25_deferred) {
		this.r25_deferred = r25_deferred;
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
	public OPER_RISK_DIS_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
