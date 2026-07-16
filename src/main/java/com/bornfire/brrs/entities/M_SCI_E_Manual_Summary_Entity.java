package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "BRRS_M_SCI_E_MANUAL_SUMMARYTABLE")


public class M_SCI_E_Manual_Summary_Entity {
	
	
	
	
		
	private BigDecimal	r45_month ;
	private BigDecimal	r46_month ;
	private BigDecimal	r54_month ;
	private BigDecimal	r58_month ;
	private BigDecimal	r59_month ;
	private BigDecimal	r60_month ;
	private BigDecimal	r66_month ;
	private BigDecimal	r67_month ;
	private BigDecimal	r68_month ;
	private BigDecimal	r74_month ;
	private BigDecimal	r85_month ;

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
	private String	 del_flg;
	public BigDecimal getR45_month() {
		return r45_month;
	}
	public void setR45_month(BigDecimal r45_month) {
		this.r45_month = r45_month;
	}
	public BigDecimal getR46_month() {
		return r46_month;
	}
	public void setR46_month(BigDecimal r46_month) {
		this.r46_month = r46_month;
	}
	public BigDecimal getR54_month() {
		return r54_month;
	}
	public void setR54_month(BigDecimal r54_month) {
		this.r54_month = r54_month;
	}
	public BigDecimal getR58_month() {
		return r58_month;
	}
	public void setR58_month(BigDecimal r58_month) {
		this.r58_month = r58_month;
	}
	public BigDecimal getR59_month() {
		return r59_month;
	}
	public void setR59_month(BigDecimal r59_month) {
		this.r59_month = r59_month;
	}
	public BigDecimal getR60_month() {
		return r60_month;
	}
	public void setR60_month(BigDecimal r60_month) {
		this.r60_month = r60_month;
	}
	public BigDecimal getR66_month() {
		return r66_month;
	}
	public void setR66_month(BigDecimal r66_month) {
		this.r66_month = r66_month;
	}
	public BigDecimal getR67_month() {
		return r67_month;
	}
	public void setR67_month(BigDecimal r67_month) {
		this.r67_month = r67_month;
	}
	public BigDecimal getR68_month() {
		return r68_month;
	}
	public void setR68_month(BigDecimal r68_month) {
		this.r68_month = r68_month;
	}
	public BigDecimal getR74_month() {
		return r74_month;
	}
	public void setR74_month(BigDecimal r74_month) {
		this.r74_month = r74_month;
	}
	public BigDecimal getR85_month() {
		return r85_month;
	}
	public void setR85_month(BigDecimal r85_month) {
		this.r85_month = r85_month;
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
	public M_SCI_E_Manual_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
   
		
		
	
	 
	

	

}
