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
@Table(name = "BRRS_M_SRWA_12A_ARCHIVALTABLE_SUMMARY_M")


public class M_SRWA_12A_Archival_Summary_M_Entity {
	
	
	private BigDecimal	r19_expo_crm;
	private BigDecimal	r60_expo_crm;
	private BigDecimal	r63_expo_crm;
	private BigDecimal	r87_expo_crm;
	private BigDecimal	r92_expo_crm;
	private BigDecimal	r117_expo_crm;
	private BigDecimal	r124_expo_crm;
	private BigDecimal	r129_expo_crm;
	private BigDecimal	r131_expo_crm;
	private BigDecimal	r158_nom_pri_amt;
	private BigDecimal	r96_expo_crm;
	
	
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
	public BigDecimal getR19_expo_crm() {
		return r19_expo_crm;
	}
	public void setR19_expo_crm(BigDecimal r19_expo_crm) {
		this.r19_expo_crm = r19_expo_crm;
	}
	public BigDecimal getR60_expo_crm() {
		return r60_expo_crm;
	}
	public void setR60_expo_crm(BigDecimal r60_expo_crm) {
		this.r60_expo_crm = r60_expo_crm;
	}
	public BigDecimal getR63_expo_crm() {
		return r63_expo_crm;
	}
	public void setR63_expo_crm(BigDecimal r63_expo_crm) {
		this.r63_expo_crm = r63_expo_crm;
	}
	public BigDecimal getR87_expo_crm() {
		return r87_expo_crm;
	}
	public void setR87_expo_crm(BigDecimal r87_expo_crm) {
		this.r87_expo_crm = r87_expo_crm;
	}
	public BigDecimal getR92_expo_crm() {
		return r92_expo_crm;
	}
	public void setR92_expo_crm(BigDecimal r92_expo_crm) {
		this.r92_expo_crm = r92_expo_crm;
	}
	public BigDecimal getR117_expo_crm() {
		return r117_expo_crm;
	}
	public void setR117_expo_crm(BigDecimal r117_expo_crm) {
		this.r117_expo_crm = r117_expo_crm;
	}
	public BigDecimal getR124_expo_crm() {
		return r124_expo_crm;
	}
	public void setR124_expo_crm(BigDecimal r124_expo_crm) {
		this.r124_expo_crm = r124_expo_crm;
	}
	public BigDecimal getR129_expo_crm() {
		return r129_expo_crm;
	}
	public void setR129_expo_crm(BigDecimal r129_expo_crm) {
		this.r129_expo_crm = r129_expo_crm;
	}
	public BigDecimal getR131_expo_crm() {
		return r131_expo_crm;
	}
	public void setR131_expo_crm(BigDecimal r131_expo_crm) {
		this.r131_expo_crm = r131_expo_crm;
	}
	public BigDecimal getR158_nom_pri_amt() {
		return r158_nom_pri_amt;
	}
	public void setR158_nom_pri_amt(BigDecimal r158_nom_pri_amt) {
		this.r158_nom_pri_amt = r158_nom_pri_amt;
	}
	public BigDecimal getR96_expo_crm() {
		return r96_expo_crm;
	}
	public void setR96_expo_crm(BigDecimal r96_expo_crm) {
		this.r96_expo_crm = r96_expo_crm;
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
	public M_SRWA_12A_Archival_Summary_M_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	


	

	

}
