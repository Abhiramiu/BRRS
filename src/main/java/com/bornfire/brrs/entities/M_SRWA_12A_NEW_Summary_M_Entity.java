package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "BRRS_M_SRWA_12A_NEW_SUMMARYTABLE_M")


public class M_SRWA_12A_NEW_Summary_M_Entity {
	
	
	private BigDecimal	r17_expo_crm;
	private BigDecimal	r58_expo_crm;
	private BigDecimal	r61_expo_crm;
	private BigDecimal	r85_expo_crm;
	private BigDecimal	r90_expo_crm;
	private BigDecimal	r94_expo_crm;
	private BigDecimal	r115_expo_crm;
	private BigDecimal	r122_expo_crm;
	private BigDecimal	r127_expo_crm;
	private BigDecimal	r129_expo_crm;
	private BigDecimal	r159_nom_pri_amt;
	private BigDecimal	r241_nom_pri_amt;
	private BigDecimal	r249_nom_pri_amt;
	
	
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
	public BigDecimal getR17_expo_crm() {
		return r17_expo_crm;
	}
	public void setR17_expo_crm(BigDecimal r17_expo_crm) {
		this.r17_expo_crm = r17_expo_crm;
	}
	public BigDecimal getR58_expo_crm() {
		return r58_expo_crm;
	}
	public void setR58_expo_crm(BigDecimal r58_expo_crm) {
		this.r58_expo_crm = r58_expo_crm;
	}
	public BigDecimal getR61_expo_crm() {
		return r61_expo_crm;
	}
	public void setR61_expo_crm(BigDecimal r61_expo_crm) {
		this.r61_expo_crm = r61_expo_crm;
	}
	public BigDecimal getR85_expo_crm() {
		return r85_expo_crm;
	}
	public void setR85_expo_crm(BigDecimal r85_expo_crm) {
		this.r85_expo_crm = r85_expo_crm;
	}
	public BigDecimal getR90_expo_crm() {
		return r90_expo_crm;
	}
	public void setR90_expo_crm(BigDecimal r90_expo_crm) {
		this.r90_expo_crm = r90_expo_crm;
	}
	public BigDecimal getR94_expo_crm() {
		return r94_expo_crm;
	}
	public void setR94_expo_crm(BigDecimal r94_expo_crm) {
		this.r94_expo_crm = r94_expo_crm;
	}
	public BigDecimal getR115_expo_crm() {
		return r115_expo_crm;
	}
	public void setR115_expo_crm(BigDecimal r115_expo_crm) {
		this.r115_expo_crm = r115_expo_crm;
	}
	public BigDecimal getR122_expo_crm() {
		return r122_expo_crm;
	}
	public void setR122_expo_crm(BigDecimal r122_expo_crm) {
		this.r122_expo_crm = r122_expo_crm;
	}
	public BigDecimal getR127_expo_crm() {
		return r127_expo_crm;
	}
	public void setR127_expo_crm(BigDecimal r127_expo_crm) {
		this.r127_expo_crm = r127_expo_crm;
	}
	public BigDecimal getR129_expo_crm() {
		return r129_expo_crm;
	}
	public void setR129_expo_crm(BigDecimal r129_expo_crm) {
		this.r129_expo_crm = r129_expo_crm;
	}
	public BigDecimal getR159_nom_pri_amt() {
		return r159_nom_pri_amt;
	}
	public void setR159_nom_pri_amt(BigDecimal r159_nom_pri_amt) {
		this.r159_nom_pri_amt = r159_nom_pri_amt;
	}
	public BigDecimal getR241_nom_pri_amt() {
		return r241_nom_pri_amt;
	}
	public void setR241_nom_pri_amt(BigDecimal r241_nom_pri_amt) {
		this.r241_nom_pri_amt = r241_nom_pri_amt;
	}
	public BigDecimal getR249_nom_pri_amt() {
		return r249_nom_pri_amt;
	}
	public void setR249_nom_pri_amt(BigDecimal r249_nom_pri_amt) {
		this.r249_nom_pri_amt = r249_nom_pri_amt;
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
	public M_SRWA_12A_NEW_Summary_M_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	


	

	

}
