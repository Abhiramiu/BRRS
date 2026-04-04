package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY")
public class FORMAT_III_Archival_Summary_Entity {

	@Column(name = "R13_BRIEF_BANK")
	private String r13_brief_bank;

	@Column(name = "R13_BRIEF_SUBDIARY")
	private String r13_brief_subdiary;

	@Column(name = "R13_EFF_NAME")
	private String r13_eff_name;

	@Column(name = "R13_EFF_INCREASE")
	private BigDecimal r13_eff_increase;

	@Column(name = "R13_EFF_DECREASE")
	private BigDecimal r13_eff_decrease;

	@Column(name = "R13_BAL_NAME")
	private String r13_bal_name;

	@Column(name = "R13_BAL_INCREASE")
	private BigDecimal r13_bal_increase;

	@Column(name = "R13_BAL_DECREASE")
	private BigDecimal r13_bal_decrease;

	@Column(name = "R14_BRIEF_BANK")
	private String r14_brief_bank;

	@Column(name = "R14_BRIEF_SUBDIARY")
	private String r14_brief_subdiary;

	@Column(name = "R14_EFF_NAME")
	private String r14_eff_name;

	@Column(name = "R14_EFF_INCREASE")
	private BigDecimal r14_eff_increase;

	@Column(name = "R14_EFF_DECREASE")
	private BigDecimal r14_eff_decrease;

	@Column(name = "R14_BAL_NAME")
	private String r14_bal_name;

	@Column(name = "R14_BAL_INCREASE")
	private BigDecimal r14_bal_increase;

	@Column(name = "R14_BAL_DECREASE")
	private BigDecimal r14_bal_decrease;

	@Column(name = "R15_BRIEF_BANK")
	private String r15_brief_bank;

	@Column(name = "R15_BRIEF_SUBDIARY")
	private String r15_brief_subdiary;

	@Column(name = "R15_EFF_NAME")
	private String r15_eff_name;

	@Column(name = "R15_EFF_INCREASE")
	private BigDecimal r15_eff_increase;

	@Column(name = "R15_EFF_DECREASE")
	private BigDecimal r15_eff_decrease;

	@Column(name = "R15_BAL_NAME")
	private String r15_bal_name;

	@Column(name = "R15_BAL_INCREASE")
	private BigDecimal r15_bal_increase;

	@Column(name = "R15_BAL_DECREASE")
	private BigDecimal r15_bal_decrease;

	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	private Date REPORT_RESUB_DATE;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	public FORMAT_III_Archival_Summary_Entity() {
		super();
	}

	public Date getREPORT_RESUB_DATE() {
		return REPORT_RESUB_DATE;
	}

	public void setREPORT_RESUB_DATE(Date rEPORT_RESUB_DATE) {
		REPORT_RESUB_DATE = rEPORT_RESUB_DATE;
	}

	public String getR13_brief_bank() {
		return r13_brief_bank;
	}

	public void setR13_brief_bank(String r13_brief_bank) {
		this.r13_brief_bank = r13_brief_bank;
	}

	public String getR13_brief_subdiary() {
		return r13_brief_subdiary;
	}

	public void setR13_brief_subdiary(String r13_brief_subdiary) {
		this.r13_brief_subdiary = r13_brief_subdiary;
	}

	public String getR13_eff_name() {
		return r13_eff_name;
	}

	public void setR13_eff_name(String r13_eff_name) {
		this.r13_eff_name = r13_eff_name;
	}

	public BigDecimal getR13_eff_increase() {
		return r13_eff_increase;
	}

	public void setR13_eff_increase(BigDecimal r13_eff_increase) {
		this.r13_eff_increase = r13_eff_increase;
	}

	public BigDecimal getR13_eff_decrease() {
		return r13_eff_decrease;
	}

	public void setR13_eff_decrease(BigDecimal r13_eff_decrease) {
		this.r13_eff_decrease = r13_eff_decrease;
	}

	public String getR13_bal_name() {
		return r13_bal_name;
	}

	public void setR13_bal_name(String r13_bal_name) {
		this.r13_bal_name = r13_bal_name;
	}

	public BigDecimal getR13_bal_increase() {
		return r13_bal_increase;
	}

	public void setR13_bal_increase(BigDecimal r13_bal_increase) {
		this.r13_bal_increase = r13_bal_increase;
	}

	public BigDecimal getR13_bal_decrease() {
		return r13_bal_decrease;
	}

	public void setR13_bal_decrease(BigDecimal r13_bal_decrease) {
		this.r13_bal_decrease = r13_bal_decrease;
	}

	public String getR14_brief_bank() {
		return r14_brief_bank;
	}

	public void setR14_brief_bank(String r14_brief_bank) {
		this.r14_brief_bank = r14_brief_bank;
	}

	public String getR14_brief_subdiary() {
		return r14_brief_subdiary;
	}

	public void setR14_brief_subdiary(String r14_brief_subdiary) {
		this.r14_brief_subdiary = r14_brief_subdiary;
	}

	public String getR14_eff_name() {
		return r14_eff_name;
	}

	public void setR14_eff_name(String r14_eff_name) {
		this.r14_eff_name = r14_eff_name;
	}

	public BigDecimal getR14_eff_increase() {
		return r14_eff_increase;
	}

	public void setR14_eff_increase(BigDecimal r14_eff_increase) {
		this.r14_eff_increase = r14_eff_increase;
	}

	public BigDecimal getR14_eff_decrease() {
		return r14_eff_decrease;
	}

	public void setR14_eff_decrease(BigDecimal r14_eff_decrease) {
		this.r14_eff_decrease = r14_eff_decrease;
	}

	public String getR14_bal_name() {
		return r14_bal_name;
	}

	public void setR14_bal_name(String r14_bal_name) {
		this.r14_bal_name = r14_bal_name;
	}

	public BigDecimal getR14_bal_increase() {
		return r14_bal_increase;
	}

	public void setR14_bal_increase(BigDecimal r14_bal_increase) {
		this.r14_bal_increase = r14_bal_increase;
	}

	public BigDecimal getR14_bal_decrease() {
		return r14_bal_decrease;
	}

	public void setR14_bal_decrease(BigDecimal r14_bal_decrease) {
		this.r14_bal_decrease = r14_bal_decrease;
	}

	public String getR15_brief_bank() {
		return r15_brief_bank;
	}

	public void setR15_brief_bank(String r15_brief_bank) {
		this.r15_brief_bank = r15_brief_bank;
	}

	public String getR15_brief_subdiary() {
		return r15_brief_subdiary;
	}

	public void setR15_brief_subdiary(String r15_brief_subdiary) {
		this.r15_brief_subdiary = r15_brief_subdiary;
	}

	public String getR15_eff_name() {
		return r15_eff_name;
	}

	public void setR15_eff_name(String r15_eff_name) {
		this.r15_eff_name = r15_eff_name;
	}

	public BigDecimal getR15_eff_increase() {
		return r15_eff_increase;
	}

	public void setR15_eff_increase(BigDecimal r15_eff_increase) {
		this.r15_eff_increase = r15_eff_increase;
	}

	public BigDecimal getR15_eff_decrease() {
		return r15_eff_decrease;
	}

	public void setR15_eff_decrease(BigDecimal r15_eff_decrease) {
		this.r15_eff_decrease = r15_eff_decrease;
	}

	public String getR15_bal_name() {
		return r15_bal_name;
	}

	public void setR15_bal_name(String r15_bal_name) {
		this.r15_bal_name = r15_bal_name;
	}

	public BigDecimal getR15_bal_increase() {
		return r15_bal_increase;
	}

	public void setR15_bal_increase(BigDecimal r15_bal_increase) {
		this.r15_bal_increase = r15_bal_increase;
	}

	public BigDecimal getR15_bal_decrease() {
		return r15_bal_decrease;
	}

	public void setR15_bal_decrease(BigDecimal r15_bal_decrease) {
		this.r15_bal_decrease = r15_bal_decrease;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public BigDecimal getReportVersion() {
		return reportVersion;
	}

	public void setReportVersion(BigDecimal reportVersion) {
		this.reportVersion = reportVersion;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}

}
