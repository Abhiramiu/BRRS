package com.bornfire.brrs.entities;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_M_FXR_ARCHIVALTABLE_SUMMARY3")
//@IdClass(M_FXR_Archival_Summary2_PK.class)

public class M_FXR_Archival_Summary_Entity3{	

	
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private String reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reportResubDate;
	
	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	
	
	private BigDecimal r29_greater_net_long_or_short;
	private BigDecimal r29_abs_value_net_gold_posi;
	//private BigDecimal r29_capital_require;
	private BigDecimal r29_capital_charge;
	private BigDecimal r30_capital_require;
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportVersion() {
		return reportVersion;
	}
	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}
	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}
	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}
	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}
	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}
	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}
	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}
	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
	}
	public M_FXR_Archival_Summary_Entity3() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	
}