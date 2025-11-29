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
@Table(name = "BRRS_M_SECA_SUMMARYTABLE")

public class M_SECA_Summary_Entity {

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
	
	private BigDecimal R36_BONDS;
	private BigDecimal R36_BOBC;
	private BigDecimal R36_PLEDGED_ASSET;
	
	

	public BigDecimal getR36_PLEDGED_ASSET() {
		return R36_PLEDGED_ASSET;
	}
	public void setR36_PLEDGED_ASSET(BigDecimal r36_PLEDGED_ASSET) {
		R36_PLEDGED_ASSET = r36_PLEDGED_ASSET;
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
	public BigDecimal getR36_BONDS() {
		return R36_BONDS;
	}
	public void setR36_BONDS(BigDecimal r36_BONDS) {
		R36_BONDS = r36_BONDS;
	}
	public BigDecimal getR36_BOBC() {
		return R36_BOBC;
	}
	public void setR36_BOBC(BigDecimal r36_BOBC) {
		R36_BOBC = r36_BOBC;
	}
	
	
	public M_SECA_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
}
