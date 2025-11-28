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
@Table(name = "BRRS_M_LIQGAP_MANUAL_ARCHIVALTABLE_SUMMARY")

public class M_LIQGAP_Manual_Archival_Summary_Entity {
	
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
	
	
	private BigDecimal r21_non_interest_bearing;

	
	private BigDecimal r32_non_interest_bearing;

	
	private BigDecimal r33_third_month;

	
	private BigDecimal r33_last_month;

	
	private BigDecimal r33_first_year;

	
	private BigDecimal r33_fifth_year;

	
	private BigDecimal r33_non_interest_bearing;

	
	private BigDecimal r34_first_month;


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


	public BigDecimal getR21_non_interest_bearing() {
		return r21_non_interest_bearing;
	}


	public void setR21_non_interest_bearing(BigDecimal r21_non_interest_bearing) {
		this.r21_non_interest_bearing = r21_non_interest_bearing;
	}


	public BigDecimal getR32_non_interest_bearing() {
		return r32_non_interest_bearing;
	}


	public void setR32_non_interest_bearing(BigDecimal r32_non_interest_bearing) {
		this.r32_non_interest_bearing = r32_non_interest_bearing;
	}


	public BigDecimal getR33_third_month() {
		return r33_third_month;
	}


	public void setR33_third_month(BigDecimal r33_third_month) {
		this.r33_third_month = r33_third_month;
	}


	public BigDecimal getR33_last_month() {
		return r33_last_month;
	}


	public void setR33_last_month(BigDecimal r33_last_month) {
		this.r33_last_month = r33_last_month;
	}


	public BigDecimal getR33_first_year() {
		return r33_first_year;
	}


	public void setR33_first_year(BigDecimal r33_first_year) {
		this.r33_first_year = r33_first_year;
	}


	public BigDecimal getR33_fifth_year() {
		return r33_fifth_year;
	}


	public void setR33_fifth_year(BigDecimal r33_fifth_year) {
		this.r33_fifth_year = r33_fifth_year;
	}


	public BigDecimal getR33_non_interest_bearing() {
		return r33_non_interest_bearing;
	}


	public void setR33_non_interest_bearing(BigDecimal r33_non_interest_bearing) {
		this.r33_non_interest_bearing = r33_non_interest_bearing;
	}


	public BigDecimal getR34_first_month() {
		return r34_first_month;
	}


	public void setR34_first_month(BigDecimal r34_first_month) {
		this.r34_first_month = r34_first_month;
	}


	public M_LIQGAP_Manual_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	


}
