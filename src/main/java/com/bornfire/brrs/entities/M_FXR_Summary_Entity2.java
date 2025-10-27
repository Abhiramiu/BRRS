
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
@Table(name = "BRRS_M_FXR_SUMMARYTABLE2")

public class M_FXR_Summary_Entity2{	
	
	private BigDecimal r21_long;
	private BigDecimal r21_short;
	private BigDecimal r21_total_gross_long_short;
	private BigDecimal r21_net_position;
	private BigDecimal r22_long;
	private BigDecimal r22_short;
	private BigDecimal r22_total_gross_long_short;
	private BigDecimal r22_net_position;

	private BigDecimal r23_net_position;

	
	
	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
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
	
	public M_FXR_Summary_Entity2() {
	super();
	// TODO Auto-generated constructor stub
}

	
}