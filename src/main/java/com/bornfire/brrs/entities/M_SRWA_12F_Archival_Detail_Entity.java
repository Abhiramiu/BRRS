
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_M_SRWA_12F_ARCHIVALTABLE_DETAIL")
public class M_SRWA_12F_Archival_Detail_Entity {

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;

	// ================== R11 ==================
	private String R11_NAME_OF_CORPORATE;
	private String R11_CREDIT_RATING;
	private String R11_RATING_AGENCY;
	private BigDecimal R11_EXPOSURE_AMT;
	private BigDecimal R11_RISK_WEIGHT;
	private BigDecimal R11_RISK_WEIGHTED_AMT;

	// ================== R12 ==================
	private String R12_NAME_OF_CORPORATE;
	private String R12_CREDIT_RATING;
	private String R12_RATING_AGENCY;
	private BigDecimal R12_EXPOSURE_AMT;
	private BigDecimal R12_RISK_WEIGHT;
	private BigDecimal R12_RISK_WEIGHTED_AMT;

	// ================== R13 ==================
	private String R13_NAME_OF_CORPORATE;
	private String R13_CREDIT_RATING;
	private String R13_RATING_AGENCY;
	private BigDecimal R13_EXPOSURE_AMT;
	private BigDecimal R13_RISK_WEIGHT;
	private BigDecimal R13_RISK_WEIGHTED_AMT;

	// ================== R14 ==================
	private String R14_NAME_OF_CORPORATE;
	private String R14_CREDIT_RATING;
	private String R14_RATING_AGENCY;
	private BigDecimal R14_EXPOSURE_AMT;
	private BigDecimal R14_RISK_WEIGHT;
	private BigDecimal R14_RISK_WEIGHTED_AMT;

	// ================== R15 ==================
	private String R15_NAME_OF_CORPORATE;
	private String R15_CREDIT_RATING;
	private String R15_RATING_AGENCY;
	private BigDecimal R15_EXPOSURE_AMT;
	private BigDecimal R15_RISK_WEIGHT;
	private BigDecimal R15_RISK_WEIGHTED_AMT;

	// ================== R16 ==================
	private String R16_NAME_OF_CORPORATE;
	private String R16_CREDIT_RATING;
	private String R16_RATING_AGENCY;
	private BigDecimal R16_EXPOSURE_AMT;
	private BigDecimal R16_RISK_WEIGHT;
	private BigDecimal R16_RISK_WEIGHTED_AMT;

	// ================== R17 ==================
	private String R17_NAME_OF_CORPORATE;
	private String R17_CREDIT_RATING;
	private String R17_RATING_AGENCY;
	private BigDecimal R17_EXPOSURE_AMT;
	private BigDecimal R17_RISK_WEIGHT;
	private BigDecimal R17_RISK_WEIGHTED_AMT;

	// ================== R18 ==================
	private String R18_NAME_OF_CORPORATE;
	private String R18_CREDIT_RATING;
	private String R18_RATING_AGENCY;
	private BigDecimal R18_EXPOSURE_AMT;
	private BigDecimal R18_RISK_WEIGHT;
	private BigDecimal R18_RISK_WEIGHTED_AMT;

	// ================== R19 ==================
	private String R19_NAME_OF_CORPORATE;
	private String R19_CREDIT_RATING;
	private String R19_RATING_AGENCY;
	private BigDecimal R19_EXPOSURE_AMT;
	private BigDecimal R19_RISK_WEIGHT;
	private BigDecimal R19_RISK_WEIGHTED_AMT;

	// ================== R20 ==================
	private String R20_NAME_OF_CORPORATE;
	private String R20_CREDIT_RATING;
	private String R20_RATING_AGENCY;
	private BigDecimal R20_EXPOSURE_AMT;
	private BigDecimal R20_RISK_WEIGHT;
	private BigDecimal R20_RISK_WEIGHTED_AMT;

	// ================== R21 ==================
	private String R21_NAME_OF_CORPORATE;
	private String R21_CREDIT_RATING;
	private String R21_RATING_AGENCY;
	private BigDecimal R21_EXPOSURE_AMT;
	private BigDecimal R21_RISK_WEIGHT;
	private BigDecimal R21_RISK_WEIGHTED_AMT;

	// ================== R22 ==================
	private String R22_NAME_OF_CORPORATE;
	private String R22_CREDIT_RATING;
	private String R22_RATING_AGENCY;
	private BigDecimal R22_EXPOSURE_AMT;
	private BigDecimal R22_RISK_WEIGHT;
	private BigDecimal R22_RISK_WEIGHTED_AMT;

	// ================== R23 ==================
	private String R23_NAME_OF_CORPORATE;
	private String R23_CREDIT_RATING;
	private String R23_RATING_AGENCY;
	private BigDecimal R23_EXPOSURE_AMT;
	private BigDecimal R23_RISK_WEIGHT;
	private BigDecimal R23_RISK_WEIGHTED_AMT;

	// ================== R24 ==================
	private String R24_NAME_OF_CORPORATE;
	private String R24_CREDIT_RATING;
	private String R24_RATING_AGENCY;
	private BigDecimal R24_EXPOSURE_AMT;
	private BigDecimal R24_RISK_WEIGHT;
	private BigDecimal R24_RISK_WEIGHTED_AMT;

	// ================== R25 ==================
	private String R25_NAME_OF_CORPORATE;
	private String R25_CREDIT_RATING;
	private String R25_RATING_AGENCY;
	private BigDecimal R25_EXPOSURE_AMT;
	private BigDecimal R25_RISK_WEIGHT;
	private BigDecimal R25_RISK_WEIGHTED_AMT;

	// ================== R26 ==================
	private String R26_NAME_OF_CORPORATE;
	private String R26_CREDIT_RATING;
	private String R26_RATING_AGENCY;
	private BigDecimal R26_EXPOSURE_AMT;
	private BigDecimal R26_RISK_WEIGHT;
	private BigDecimal R26_RISK_WEIGHTED_AMT;

	// ================== R27 ==================
	private String R27_NAME_OF_CORPORATE;
	private String R27_CREDIT_RATING;
	private String R27_RATING_AGENCY;
	private BigDecimal R27_EXPOSURE_AMT;
	private BigDecimal R27_RISK_WEIGHT;
	private BigDecimal R27_RISK_WEIGHTED_AMT;

	// ================== R28 ==================
	private String R28_NAME_OF_CORPORATE;
	private String R28_CREDIT_RATING;
	private String R28_RATING_AGENCY;
	private BigDecimal R28_EXPOSURE_AMT;
	private BigDecimal R28_RISK_WEIGHT;
	private BigDecimal R28_RISK_WEIGHTED_AMT;

	// ================== R29 ==================
	private String R29_NAME_OF_CORPORATE;
	private String R29_CREDIT_RATING;
	private String R29_RATING_AGENCY;
	private BigDecimal R29_EXPOSURE_AMT;
	private BigDecimal R29_RISK_WEIGHT;
	private BigDecimal R29_RISK_WEIGHTED_AMT;

	// ================== R30 ==================
	private String R30_NAME_OF_CORPORATE;
	private String R30_CREDIT_RATING;
	private String R30_RATING_AGENCY;
	private BigDecimal R30_EXPOSURE_AMT;
	private BigDecimal R30_RISK_WEIGHT;
	private BigDecimal R30_RISK_WEIGHTED_AMT;

	// ================== R31 ==================
	private String R31_NAME_OF_CORPORATE;
	private String R31_CREDIT_RATING;
	private String R31_RATING_AGENCY;
	private BigDecimal R31_EXPOSURE_AMT;
	private BigDecimal R31_RISK_WEIGHT;
	private BigDecimal R31_RISK_WEIGHTED_AMT;

	// ================== R32 ==================
	private String R32_NAME_OF_CORPORATE;
	private String R32_CREDIT_RATING;
	private String R32_RATING_AGENCY;
	private BigDecimal R32_EXPOSURE_AMT;
	private BigDecimal R32_RISK_WEIGHT;
	private BigDecimal R32_RISK_WEIGHTED_AMT;

	// ================== R33 ==================
	private String R33_NAME_OF_CORPORATE;
	private String R33_CREDIT_RATING;
	private String R33_RATING_AGENCY;
	private BigDecimal R33_EXPOSURE_AMT;
	private BigDecimal R33_RISK_WEIGHT;
	private BigDecimal R33_RISK_WEIGHTED_AMT;

	// ================== R34 ==================
	private String R34_NAME_OF_CORPORATE;
	private String R34_CREDIT_RATING;
	private String R34_RATING_AGENCY;
	private BigDecimal R34_EXPOSURE_AMT;
	private BigDecimal R34_RISK_WEIGHT;
	private BigDecimal R34_RISK_WEIGHTED_AMT;

	// ================== R35 ==================
	private String R35_NAME_OF_CORPORATE;
	private String R35_CREDIT_RATING;
	private String R35_RATING_AGENCY;
	private BigDecimal R35_EXPOSURE_AMT;
	private BigDecimal R35_RISK_WEIGHT;
	private BigDecimal R35_RISK_WEIGHTED_AMT;

	// ================== R36 ==================
	private String R36_NAME_OF_CORPORATE;
	private String R36_CREDIT_RATING;
	private String R36_RATING_AGENCY;
	private BigDecimal R36_EXPOSURE_AMT;
	private BigDecimal R36_RISK_WEIGHT;
	private BigDecimal R36_RISK_WEIGHTED_AMT;

	// ================== R37 ==================
	private String R37_NAME_OF_CORPORATE;
	private String R37_CREDIT_RATING;
	private String R37_RATING_AGENCY;
	private BigDecimal R37_EXPOSURE_AMT;
	private BigDecimal R37_RISK_WEIGHT;
	private BigDecimal R37_RISK_WEIGHTED_AMT;

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

	public String getR11_NAME_OF_CORPORATE() {
		return R11_NAME_OF_CORPORATE;
	}

	public void setR11_NAME_OF_CORPORATE(String r11_NAME_OF_CORPORATE) {
		R11_NAME_OF_CORPORATE = r11_NAME_OF_CORPORATE;
	}

	public String getR11_CREDIT_RATING() {
		return R11_CREDIT_RATING;
	}

	public void setR11_CREDIT_RATING(String r11_CREDIT_RATING) {
		R11_CREDIT_RATING = r11_CREDIT_RATING;
	}

	public String getR11_RATING_AGENCY() {
		return R11_RATING_AGENCY;
	}

	public void setR11_RATING_AGENCY(String r11_RATING_AGENCY) {
		R11_RATING_AGENCY = r11_RATING_AGENCY;
	}

	public BigDecimal getR11_EXPOSURE_AMT() {
		return R11_EXPOSURE_AMT;
	}

	public void setR11_EXPOSURE_AMT(BigDecimal r11_EXPOSURE_AMT) {
		R11_EXPOSURE_AMT = r11_EXPOSURE_AMT;
	}

	public BigDecimal getR11_RISK_WEIGHT() {
		return R11_RISK_WEIGHT;
	}

	public void setR11_RISK_WEIGHT(BigDecimal r11_RISK_WEIGHT) {
		R11_RISK_WEIGHT = r11_RISK_WEIGHT;
	}

	public BigDecimal getR11_RISK_WEIGHTED_AMT() {
		return R11_RISK_WEIGHTED_AMT;
	}

	public void setR11_RISK_WEIGHTED_AMT(BigDecimal r11_RISK_WEIGHTED_AMT) {
		R11_RISK_WEIGHTED_AMT = r11_RISK_WEIGHTED_AMT;
	}

	public String getR12_NAME_OF_CORPORATE() {
		return R12_NAME_OF_CORPORATE;
	}

	public void setR12_NAME_OF_CORPORATE(String r12_NAME_OF_CORPORATE) {
		R12_NAME_OF_CORPORATE = r12_NAME_OF_CORPORATE;
	}

	public String getR12_CREDIT_RATING() {
		return R12_CREDIT_RATING;
	}

	public void setR12_CREDIT_RATING(String r12_CREDIT_RATING) {
		R12_CREDIT_RATING = r12_CREDIT_RATING;
	}

	public String getR12_RATING_AGENCY() {
		return R12_RATING_AGENCY;
	}

	public void setR12_RATING_AGENCY(String r12_RATING_AGENCY) {
		R12_RATING_AGENCY = r12_RATING_AGENCY;
	}

	public BigDecimal getR12_EXPOSURE_AMT() {
		return R12_EXPOSURE_AMT;
	}

	public void setR12_EXPOSURE_AMT(BigDecimal r12_EXPOSURE_AMT) {
		R12_EXPOSURE_AMT = r12_EXPOSURE_AMT;
	}

	public BigDecimal getR12_RISK_WEIGHT() {
		return R12_RISK_WEIGHT;
	}

	public void setR12_RISK_WEIGHT(BigDecimal r12_RISK_WEIGHT) {
		R12_RISK_WEIGHT = r12_RISK_WEIGHT;
	}

	public BigDecimal getR12_RISK_WEIGHTED_AMT() {
		return R12_RISK_WEIGHTED_AMT;
	}

	public void setR12_RISK_WEIGHTED_AMT(BigDecimal r12_RISK_WEIGHTED_AMT) {
		R12_RISK_WEIGHTED_AMT = r12_RISK_WEIGHTED_AMT;
	}

	public String getR13_NAME_OF_CORPORATE() {
		return R13_NAME_OF_CORPORATE;
	}

	public void setR13_NAME_OF_CORPORATE(String r13_NAME_OF_CORPORATE) {
		R13_NAME_OF_CORPORATE = r13_NAME_OF_CORPORATE;
	}

	public String getR13_CREDIT_RATING() {
		return R13_CREDIT_RATING;
	}

	public void setR13_CREDIT_RATING(String r13_CREDIT_RATING) {
		R13_CREDIT_RATING = r13_CREDIT_RATING;
	}

	public String getR13_RATING_AGENCY() {
		return R13_RATING_AGENCY;
	}

	public void setR13_RATING_AGENCY(String r13_RATING_AGENCY) {
		R13_RATING_AGENCY = r13_RATING_AGENCY;
	}

	public BigDecimal getR13_EXPOSURE_AMT() {
		return R13_EXPOSURE_AMT;
	}

	public void setR13_EXPOSURE_AMT(BigDecimal r13_EXPOSURE_AMT) {
		R13_EXPOSURE_AMT = r13_EXPOSURE_AMT;
	}

	public BigDecimal getR13_RISK_WEIGHT() {
		return R13_RISK_WEIGHT;
	}

	public void setR13_RISK_WEIGHT(BigDecimal r13_RISK_WEIGHT) {
		R13_RISK_WEIGHT = r13_RISK_WEIGHT;
	}

	public BigDecimal getR13_RISK_WEIGHTED_AMT() {
		return R13_RISK_WEIGHTED_AMT;
	}

	public void setR13_RISK_WEIGHTED_AMT(BigDecimal r13_RISK_WEIGHTED_AMT) {
		R13_RISK_WEIGHTED_AMT = r13_RISK_WEIGHTED_AMT;
	}

	public String getR14_NAME_OF_CORPORATE() {
		return R14_NAME_OF_CORPORATE;
	}

	public void setR14_NAME_OF_CORPORATE(String r14_NAME_OF_CORPORATE) {
		R14_NAME_OF_CORPORATE = r14_NAME_OF_CORPORATE;
	}

	public String getR14_CREDIT_RATING() {
		return R14_CREDIT_RATING;
	}

	public void setR14_CREDIT_RATING(String r14_CREDIT_RATING) {
		R14_CREDIT_RATING = r14_CREDIT_RATING;
	}

	public String getR14_RATING_AGENCY() {
		return R14_RATING_AGENCY;
	}

	public void setR14_RATING_AGENCY(String r14_RATING_AGENCY) {
		R14_RATING_AGENCY = r14_RATING_AGENCY;
	}

	public BigDecimal getR14_EXPOSURE_AMT() {
		return R14_EXPOSURE_AMT;
	}

	public void setR14_EXPOSURE_AMT(BigDecimal r14_EXPOSURE_AMT) {
		R14_EXPOSURE_AMT = r14_EXPOSURE_AMT;
	}

	public BigDecimal getR14_RISK_WEIGHT() {
		return R14_RISK_WEIGHT;
	}

	public void setR14_RISK_WEIGHT(BigDecimal r14_RISK_WEIGHT) {
		R14_RISK_WEIGHT = r14_RISK_WEIGHT;
	}

	public BigDecimal getR14_RISK_WEIGHTED_AMT() {
		return R14_RISK_WEIGHTED_AMT;
	}

	public void setR14_RISK_WEIGHTED_AMT(BigDecimal r14_RISK_WEIGHTED_AMT) {
		R14_RISK_WEIGHTED_AMT = r14_RISK_WEIGHTED_AMT;
	}

	public String getR15_NAME_OF_CORPORATE() {
		return R15_NAME_OF_CORPORATE;
	}

	public void setR15_NAME_OF_CORPORATE(String r15_NAME_OF_CORPORATE) {
		R15_NAME_OF_CORPORATE = r15_NAME_OF_CORPORATE;
	}

	public String getR15_CREDIT_RATING() {
		return R15_CREDIT_RATING;
	}

	public void setR15_CREDIT_RATING(String r15_CREDIT_RATING) {
		R15_CREDIT_RATING = r15_CREDIT_RATING;
	}

	public String getR15_RATING_AGENCY() {
		return R15_RATING_AGENCY;
	}

	public void setR15_RATING_AGENCY(String r15_RATING_AGENCY) {
		R15_RATING_AGENCY = r15_RATING_AGENCY;
	}

	public BigDecimal getR15_EXPOSURE_AMT() {
		return R15_EXPOSURE_AMT;
	}

	public void setR15_EXPOSURE_AMT(BigDecimal r15_EXPOSURE_AMT) {
		R15_EXPOSURE_AMT = r15_EXPOSURE_AMT;
	}

	public BigDecimal getR15_RISK_WEIGHT() {
		return R15_RISK_WEIGHT;
	}

	public void setR15_RISK_WEIGHT(BigDecimal r15_RISK_WEIGHT) {
		R15_RISK_WEIGHT = r15_RISK_WEIGHT;
	}

	public BigDecimal getR15_RISK_WEIGHTED_AMT() {
		return R15_RISK_WEIGHTED_AMT;
	}

	public void setR15_RISK_WEIGHTED_AMT(BigDecimal r15_RISK_WEIGHTED_AMT) {
		R15_RISK_WEIGHTED_AMT = r15_RISK_WEIGHTED_AMT;
	}

	public String getR16_NAME_OF_CORPORATE() {
		return R16_NAME_OF_CORPORATE;
	}

	public void setR16_NAME_OF_CORPORATE(String r16_NAME_OF_CORPORATE) {
		R16_NAME_OF_CORPORATE = r16_NAME_OF_CORPORATE;
	}

	public String getR16_CREDIT_RATING() {
		return R16_CREDIT_RATING;
	}

	public void setR16_CREDIT_RATING(String r16_CREDIT_RATING) {
		R16_CREDIT_RATING = r16_CREDIT_RATING;
	}

	public String getR16_RATING_AGENCY() {
		return R16_RATING_AGENCY;
	}

	public void setR16_RATING_AGENCY(String r16_RATING_AGENCY) {
		R16_RATING_AGENCY = r16_RATING_AGENCY;
	}

	public BigDecimal getR16_EXPOSURE_AMT() {
		return R16_EXPOSURE_AMT;
	}

	public void setR16_EXPOSURE_AMT(BigDecimal r16_EXPOSURE_AMT) {
		R16_EXPOSURE_AMT = r16_EXPOSURE_AMT;
	}

	public BigDecimal getR16_RISK_WEIGHT() {
		return R16_RISK_WEIGHT;
	}

	public void setR16_RISK_WEIGHT(BigDecimal r16_RISK_WEIGHT) {
		R16_RISK_WEIGHT = r16_RISK_WEIGHT;
	}

	public BigDecimal getR16_RISK_WEIGHTED_AMT() {
		return R16_RISK_WEIGHTED_AMT;
	}

	public void setR16_RISK_WEIGHTED_AMT(BigDecimal r16_RISK_WEIGHTED_AMT) {
		R16_RISK_WEIGHTED_AMT = r16_RISK_WEIGHTED_AMT;
	}

	public String getR17_NAME_OF_CORPORATE() {
		return R17_NAME_OF_CORPORATE;
	}

	public void setR17_NAME_OF_CORPORATE(String r17_NAME_OF_CORPORATE) {
		R17_NAME_OF_CORPORATE = r17_NAME_OF_CORPORATE;
	}

	public String getR17_CREDIT_RATING() {
		return R17_CREDIT_RATING;
	}

	public void setR17_CREDIT_RATING(String r17_CREDIT_RATING) {
		R17_CREDIT_RATING = r17_CREDIT_RATING;
	}

	public String getR17_RATING_AGENCY() {
		return R17_RATING_AGENCY;
	}

	public void setR17_RATING_AGENCY(String r17_RATING_AGENCY) {
		R17_RATING_AGENCY = r17_RATING_AGENCY;
	}

	public BigDecimal getR17_EXPOSURE_AMT() {
		return R17_EXPOSURE_AMT;
	}

	public void setR17_EXPOSURE_AMT(BigDecimal r17_EXPOSURE_AMT) {
		R17_EXPOSURE_AMT = r17_EXPOSURE_AMT;
	}

	public BigDecimal getR17_RISK_WEIGHT() {
		return R17_RISK_WEIGHT;
	}

	public void setR17_RISK_WEIGHT(BigDecimal r17_RISK_WEIGHT) {
		R17_RISK_WEIGHT = r17_RISK_WEIGHT;
	}

	public BigDecimal getR17_RISK_WEIGHTED_AMT() {
		return R17_RISK_WEIGHTED_AMT;
	}

	public void setR17_RISK_WEIGHTED_AMT(BigDecimal r17_RISK_WEIGHTED_AMT) {
		R17_RISK_WEIGHTED_AMT = r17_RISK_WEIGHTED_AMT;
	}

	public String getR18_NAME_OF_CORPORATE() {
		return R18_NAME_OF_CORPORATE;
	}

	public void setR18_NAME_OF_CORPORATE(String r18_NAME_OF_CORPORATE) {
		R18_NAME_OF_CORPORATE = r18_NAME_OF_CORPORATE;
	}

	public String getR18_CREDIT_RATING() {
		return R18_CREDIT_RATING;
	}

	public void setR18_CREDIT_RATING(String r18_CREDIT_RATING) {
		R18_CREDIT_RATING = r18_CREDIT_RATING;
	}

	public String getR18_RATING_AGENCY() {
		return R18_RATING_AGENCY;
	}

	public void setR18_RATING_AGENCY(String r18_RATING_AGENCY) {
		R18_RATING_AGENCY = r18_RATING_AGENCY;
	}

	public BigDecimal getR18_EXPOSURE_AMT() {
		return R18_EXPOSURE_AMT;
	}

	public void setR18_EXPOSURE_AMT(BigDecimal r18_EXPOSURE_AMT) {
		R18_EXPOSURE_AMT = r18_EXPOSURE_AMT;
	}

	public BigDecimal getR18_RISK_WEIGHT() {
		return R18_RISK_WEIGHT;
	}

	public void setR18_RISK_WEIGHT(BigDecimal r18_RISK_WEIGHT) {
		R18_RISK_WEIGHT = r18_RISK_WEIGHT;
	}

	public BigDecimal getR18_RISK_WEIGHTED_AMT() {
		return R18_RISK_WEIGHTED_AMT;
	}

	public void setR18_RISK_WEIGHTED_AMT(BigDecimal r18_RISK_WEIGHTED_AMT) {
		R18_RISK_WEIGHTED_AMT = r18_RISK_WEIGHTED_AMT;
	}

	public String getR19_NAME_OF_CORPORATE() {
		return R19_NAME_OF_CORPORATE;
	}

	public void setR19_NAME_OF_CORPORATE(String r19_NAME_OF_CORPORATE) {
		R19_NAME_OF_CORPORATE = r19_NAME_OF_CORPORATE;
	}

	public String getR19_CREDIT_RATING() {
		return R19_CREDIT_RATING;
	}

	public void setR19_CREDIT_RATING(String r19_CREDIT_RATING) {
		R19_CREDIT_RATING = r19_CREDIT_RATING;
	}

	public String getR19_RATING_AGENCY() {
		return R19_RATING_AGENCY;
	}

	public void setR19_RATING_AGENCY(String r19_RATING_AGENCY) {
		R19_RATING_AGENCY = r19_RATING_AGENCY;
	}

	public BigDecimal getR19_EXPOSURE_AMT() {
		return R19_EXPOSURE_AMT;
	}

	public void setR19_EXPOSURE_AMT(BigDecimal r19_EXPOSURE_AMT) {
		R19_EXPOSURE_AMT = r19_EXPOSURE_AMT;
	}

	public BigDecimal getR19_RISK_WEIGHT() {
		return R19_RISK_WEIGHT;
	}

	public void setR19_RISK_WEIGHT(BigDecimal r19_RISK_WEIGHT) {
		R19_RISK_WEIGHT = r19_RISK_WEIGHT;
	}

	public BigDecimal getR19_RISK_WEIGHTED_AMT() {
		return R19_RISK_WEIGHTED_AMT;
	}

	public void setR19_RISK_WEIGHTED_AMT(BigDecimal r19_RISK_WEIGHTED_AMT) {
		R19_RISK_WEIGHTED_AMT = r19_RISK_WEIGHTED_AMT;
	}

	public String getR20_NAME_OF_CORPORATE() {
		return R20_NAME_OF_CORPORATE;
	}

	public void setR20_NAME_OF_CORPORATE(String r20_NAME_OF_CORPORATE) {
		R20_NAME_OF_CORPORATE = r20_NAME_OF_CORPORATE;
	}

	public String getR20_CREDIT_RATING() {
		return R20_CREDIT_RATING;
	}

	public void setR20_CREDIT_RATING(String r20_CREDIT_RATING) {
		R20_CREDIT_RATING = r20_CREDIT_RATING;
	}

	public String getR20_RATING_AGENCY() {
		return R20_RATING_AGENCY;
	}

	public void setR20_RATING_AGENCY(String r20_RATING_AGENCY) {
		R20_RATING_AGENCY = r20_RATING_AGENCY;
	}

	public BigDecimal getR20_EXPOSURE_AMT() {
		return R20_EXPOSURE_AMT;
	}

	public void setR20_EXPOSURE_AMT(BigDecimal r20_EXPOSURE_AMT) {
		R20_EXPOSURE_AMT = r20_EXPOSURE_AMT;
	}

	public BigDecimal getR20_RISK_WEIGHT() {
		return R20_RISK_WEIGHT;
	}

	public void setR20_RISK_WEIGHT(BigDecimal r20_RISK_WEIGHT) {
		R20_RISK_WEIGHT = r20_RISK_WEIGHT;
	}

	public BigDecimal getR20_RISK_WEIGHTED_AMT() {
		return R20_RISK_WEIGHTED_AMT;
	}

	public void setR20_RISK_WEIGHTED_AMT(BigDecimal r20_RISK_WEIGHTED_AMT) {
		R20_RISK_WEIGHTED_AMT = r20_RISK_WEIGHTED_AMT;
	}

	public String getR21_NAME_OF_CORPORATE() {
		return R21_NAME_OF_CORPORATE;
	}

	public void setR21_NAME_OF_CORPORATE(String r21_NAME_OF_CORPORATE) {
		R21_NAME_OF_CORPORATE = r21_NAME_OF_CORPORATE;
	}

	public String getR21_CREDIT_RATING() {
		return R21_CREDIT_RATING;
	}

	public void setR21_CREDIT_RATING(String r21_CREDIT_RATING) {
		R21_CREDIT_RATING = r21_CREDIT_RATING;
	}

	public String getR21_RATING_AGENCY() {
		return R21_RATING_AGENCY;
	}

	public void setR21_RATING_AGENCY(String r21_RATING_AGENCY) {
		R21_RATING_AGENCY = r21_RATING_AGENCY;
	}

	public BigDecimal getR21_EXPOSURE_AMT() {
		return R21_EXPOSURE_AMT;
	}

	public void setR21_EXPOSURE_AMT(BigDecimal r21_EXPOSURE_AMT) {
		R21_EXPOSURE_AMT = r21_EXPOSURE_AMT;
	}

	public BigDecimal getR21_RISK_WEIGHT() {
		return R21_RISK_WEIGHT;
	}

	public void setR21_RISK_WEIGHT(BigDecimal r21_RISK_WEIGHT) {
		R21_RISK_WEIGHT = r21_RISK_WEIGHT;
	}

	public BigDecimal getR21_RISK_WEIGHTED_AMT() {
		return R21_RISK_WEIGHTED_AMT;
	}

	public void setR21_RISK_WEIGHTED_AMT(BigDecimal r21_RISK_WEIGHTED_AMT) {
		R21_RISK_WEIGHTED_AMT = r21_RISK_WEIGHTED_AMT;
	}

	public String getR22_NAME_OF_CORPORATE() {
		return R22_NAME_OF_CORPORATE;
	}

	public void setR22_NAME_OF_CORPORATE(String r22_NAME_OF_CORPORATE) {
		R22_NAME_OF_CORPORATE = r22_NAME_OF_CORPORATE;
	}

	public String getR22_CREDIT_RATING() {
		return R22_CREDIT_RATING;
	}

	public void setR22_CREDIT_RATING(String r22_CREDIT_RATING) {
		R22_CREDIT_RATING = r22_CREDIT_RATING;
	}

	public String getR22_RATING_AGENCY() {
		return R22_RATING_AGENCY;
	}

	public void setR22_RATING_AGENCY(String r22_RATING_AGENCY) {
		R22_RATING_AGENCY = r22_RATING_AGENCY;
	}

	public BigDecimal getR22_EXPOSURE_AMT() {
		return R22_EXPOSURE_AMT;
	}

	public void setR22_EXPOSURE_AMT(BigDecimal r22_EXPOSURE_AMT) {
		R22_EXPOSURE_AMT = r22_EXPOSURE_AMT;
	}

	public BigDecimal getR22_RISK_WEIGHT() {
		return R22_RISK_WEIGHT;
	}

	public void setR22_RISK_WEIGHT(BigDecimal r22_RISK_WEIGHT) {
		R22_RISK_WEIGHT = r22_RISK_WEIGHT;
	}

	public BigDecimal getR22_RISK_WEIGHTED_AMT() {
		return R22_RISK_WEIGHTED_AMT;
	}

	public void setR22_RISK_WEIGHTED_AMT(BigDecimal r22_RISK_WEIGHTED_AMT) {
		R22_RISK_WEIGHTED_AMT = r22_RISK_WEIGHTED_AMT;
	}

	public String getR23_NAME_OF_CORPORATE() {
		return R23_NAME_OF_CORPORATE;
	}

	public void setR23_NAME_OF_CORPORATE(String r23_NAME_OF_CORPORATE) {
		R23_NAME_OF_CORPORATE = r23_NAME_OF_CORPORATE;
	}

	public String getR23_CREDIT_RATING() {
		return R23_CREDIT_RATING;
	}

	public void setR23_CREDIT_RATING(String r23_CREDIT_RATING) {
		R23_CREDIT_RATING = r23_CREDIT_RATING;
	}

	public String getR23_RATING_AGENCY() {
		return R23_RATING_AGENCY;
	}

	public void setR23_RATING_AGENCY(String r23_RATING_AGENCY) {
		R23_RATING_AGENCY = r23_RATING_AGENCY;
	}

	public BigDecimal getR23_EXPOSURE_AMT() {
		return R23_EXPOSURE_AMT;
	}

	public void setR23_EXPOSURE_AMT(BigDecimal r23_EXPOSURE_AMT) {
		R23_EXPOSURE_AMT = r23_EXPOSURE_AMT;
	}

	public BigDecimal getR23_RISK_WEIGHT() {
		return R23_RISK_WEIGHT;
	}

	public void setR23_RISK_WEIGHT(BigDecimal r23_RISK_WEIGHT) {
		R23_RISK_WEIGHT = r23_RISK_WEIGHT;
	}

	public BigDecimal getR23_RISK_WEIGHTED_AMT() {
		return R23_RISK_WEIGHTED_AMT;
	}

	public void setR23_RISK_WEIGHTED_AMT(BigDecimal r23_RISK_WEIGHTED_AMT) {
		R23_RISK_WEIGHTED_AMT = r23_RISK_WEIGHTED_AMT;
	}

	public String getR24_NAME_OF_CORPORATE() {
		return R24_NAME_OF_CORPORATE;
	}

	public void setR24_NAME_OF_CORPORATE(String r24_NAME_OF_CORPORATE) {
		R24_NAME_OF_CORPORATE = r24_NAME_OF_CORPORATE;
	}

	public String getR24_CREDIT_RATING() {
		return R24_CREDIT_RATING;
	}

	public void setR24_CREDIT_RATING(String r24_CREDIT_RATING) {
		R24_CREDIT_RATING = r24_CREDIT_RATING;
	}

	public String getR24_RATING_AGENCY() {
		return R24_RATING_AGENCY;
	}

	public void setR24_RATING_AGENCY(String r24_RATING_AGENCY) {
		R24_RATING_AGENCY = r24_RATING_AGENCY;
	}

	public BigDecimal getR24_EXPOSURE_AMT() {
		return R24_EXPOSURE_AMT;
	}

	public void setR24_EXPOSURE_AMT(BigDecimal r24_EXPOSURE_AMT) {
		R24_EXPOSURE_AMT = r24_EXPOSURE_AMT;
	}

	public BigDecimal getR24_RISK_WEIGHT() {
		return R24_RISK_WEIGHT;
	}

	public void setR24_RISK_WEIGHT(BigDecimal r24_RISK_WEIGHT) {
		R24_RISK_WEIGHT = r24_RISK_WEIGHT;
	}

	public BigDecimal getR24_RISK_WEIGHTED_AMT() {
		return R24_RISK_WEIGHTED_AMT;
	}

	public void setR24_RISK_WEIGHTED_AMT(BigDecimal r24_RISK_WEIGHTED_AMT) {
		R24_RISK_WEIGHTED_AMT = r24_RISK_WEIGHTED_AMT;
	}

	public String getR25_NAME_OF_CORPORATE() {
		return R25_NAME_OF_CORPORATE;
	}

	public void setR25_NAME_OF_CORPORATE(String r25_NAME_OF_CORPORATE) {
		R25_NAME_OF_CORPORATE = r25_NAME_OF_CORPORATE;
	}

	public String getR25_CREDIT_RATING() {
		return R25_CREDIT_RATING;
	}

	public void setR25_CREDIT_RATING(String r25_CREDIT_RATING) {
		R25_CREDIT_RATING = r25_CREDIT_RATING;
	}

	public String getR25_RATING_AGENCY() {
		return R25_RATING_AGENCY;
	}

	public void setR25_RATING_AGENCY(String r25_RATING_AGENCY) {
		R25_RATING_AGENCY = r25_RATING_AGENCY;
	}

	public BigDecimal getR25_EXPOSURE_AMT() {
		return R25_EXPOSURE_AMT;
	}

	public void setR25_EXPOSURE_AMT(BigDecimal r25_EXPOSURE_AMT) {
		R25_EXPOSURE_AMT = r25_EXPOSURE_AMT;
	}

	public BigDecimal getR25_RISK_WEIGHT() {
		return R25_RISK_WEIGHT;
	}

	public void setR25_RISK_WEIGHT(BigDecimal r25_RISK_WEIGHT) {
		R25_RISK_WEIGHT = r25_RISK_WEIGHT;
	}

	public BigDecimal getR25_RISK_WEIGHTED_AMT() {
		return R25_RISK_WEIGHTED_AMT;
	}

	public void setR25_RISK_WEIGHTED_AMT(BigDecimal r25_RISK_WEIGHTED_AMT) {
		R25_RISK_WEIGHTED_AMT = r25_RISK_WEIGHTED_AMT;
	}

	public String getR26_NAME_OF_CORPORATE() {
		return R26_NAME_OF_CORPORATE;
	}

	public void setR26_NAME_OF_CORPORATE(String r26_NAME_OF_CORPORATE) {
		R26_NAME_OF_CORPORATE = r26_NAME_OF_CORPORATE;
	}

	public String getR26_CREDIT_RATING() {
		return R26_CREDIT_RATING;
	}

	public void setR26_CREDIT_RATING(String r26_CREDIT_RATING) {
		R26_CREDIT_RATING = r26_CREDIT_RATING;
	}

	public String getR26_RATING_AGENCY() {
		return R26_RATING_AGENCY;
	}

	public void setR26_RATING_AGENCY(String r26_RATING_AGENCY) {
		R26_RATING_AGENCY = r26_RATING_AGENCY;
	}

	public BigDecimal getR26_EXPOSURE_AMT() {
		return R26_EXPOSURE_AMT;
	}

	public void setR26_EXPOSURE_AMT(BigDecimal r26_EXPOSURE_AMT) {
		R26_EXPOSURE_AMT = r26_EXPOSURE_AMT;
	}

	public BigDecimal getR26_RISK_WEIGHT() {
		return R26_RISK_WEIGHT;
	}

	public void setR26_RISK_WEIGHT(BigDecimal r26_RISK_WEIGHT) {
		R26_RISK_WEIGHT = r26_RISK_WEIGHT;
	}

	public BigDecimal getR26_RISK_WEIGHTED_AMT() {
		return R26_RISK_WEIGHTED_AMT;
	}

	public void setR26_RISK_WEIGHTED_AMT(BigDecimal r26_RISK_WEIGHTED_AMT) {
		R26_RISK_WEIGHTED_AMT = r26_RISK_WEIGHTED_AMT;
	}

	public String getR27_NAME_OF_CORPORATE() {
		return R27_NAME_OF_CORPORATE;
	}

	public void setR27_NAME_OF_CORPORATE(String r27_NAME_OF_CORPORATE) {
		R27_NAME_OF_CORPORATE = r27_NAME_OF_CORPORATE;
	}

	public String getR27_CREDIT_RATING() {
		return R27_CREDIT_RATING;
	}

	public void setR27_CREDIT_RATING(String r27_CREDIT_RATING) {
		R27_CREDIT_RATING = r27_CREDIT_RATING;
	}

	public String getR27_RATING_AGENCY() {
		return R27_RATING_AGENCY;
	}

	public void setR27_RATING_AGENCY(String r27_RATING_AGENCY) {
		R27_RATING_AGENCY = r27_RATING_AGENCY;
	}

	public BigDecimal getR27_EXPOSURE_AMT() {
		return R27_EXPOSURE_AMT;
	}

	public void setR27_EXPOSURE_AMT(BigDecimal r27_EXPOSURE_AMT) {
		R27_EXPOSURE_AMT = r27_EXPOSURE_AMT;
	}

	public BigDecimal getR27_RISK_WEIGHT() {
		return R27_RISK_WEIGHT;
	}

	public void setR27_RISK_WEIGHT(BigDecimal r27_RISK_WEIGHT) {
		R27_RISK_WEIGHT = r27_RISK_WEIGHT;
	}

	public BigDecimal getR27_RISK_WEIGHTED_AMT() {
		return R27_RISK_WEIGHTED_AMT;
	}

	public void setR27_RISK_WEIGHTED_AMT(BigDecimal r27_RISK_WEIGHTED_AMT) {
		R27_RISK_WEIGHTED_AMT = r27_RISK_WEIGHTED_AMT;
	}

	public String getR28_NAME_OF_CORPORATE() {
		return R28_NAME_OF_CORPORATE;
	}

	public void setR28_NAME_OF_CORPORATE(String r28_NAME_OF_CORPORATE) {
		R28_NAME_OF_CORPORATE = r28_NAME_OF_CORPORATE;
	}

	public String getR28_CREDIT_RATING() {
		return R28_CREDIT_RATING;
	}

	public void setR28_CREDIT_RATING(String r28_CREDIT_RATING) {
		R28_CREDIT_RATING = r28_CREDIT_RATING;
	}

	public String getR28_RATING_AGENCY() {
		return R28_RATING_AGENCY;
	}

	public void setR28_RATING_AGENCY(String r28_RATING_AGENCY) {
		R28_RATING_AGENCY = r28_RATING_AGENCY;
	}

	public BigDecimal getR28_EXPOSURE_AMT() {
		return R28_EXPOSURE_AMT;
	}

	public void setR28_EXPOSURE_AMT(BigDecimal r28_EXPOSURE_AMT) {
		R28_EXPOSURE_AMT = r28_EXPOSURE_AMT;
	}

	public BigDecimal getR28_RISK_WEIGHT() {
		return R28_RISK_WEIGHT;
	}

	public void setR28_RISK_WEIGHT(BigDecimal r28_RISK_WEIGHT) {
		R28_RISK_WEIGHT = r28_RISK_WEIGHT;
	}

	public BigDecimal getR28_RISK_WEIGHTED_AMT() {
		return R28_RISK_WEIGHTED_AMT;
	}

	public void setR28_RISK_WEIGHTED_AMT(BigDecimal r28_RISK_WEIGHTED_AMT) {
		R28_RISK_WEIGHTED_AMT = r28_RISK_WEIGHTED_AMT;
	}

	public String getR29_NAME_OF_CORPORATE() {
		return R29_NAME_OF_CORPORATE;
	}

	public void setR29_NAME_OF_CORPORATE(String r29_NAME_OF_CORPORATE) {
		R29_NAME_OF_CORPORATE = r29_NAME_OF_CORPORATE;
	}

	public String getR29_CREDIT_RATING() {
		return R29_CREDIT_RATING;
	}

	public void setR29_CREDIT_RATING(String r29_CREDIT_RATING) {
		R29_CREDIT_RATING = r29_CREDIT_RATING;
	}

	public String getR29_RATING_AGENCY() {
		return R29_RATING_AGENCY;
	}

	public void setR29_RATING_AGENCY(String r29_RATING_AGENCY) {
		R29_RATING_AGENCY = r29_RATING_AGENCY;
	}

	public BigDecimal getR29_EXPOSURE_AMT() {
		return R29_EXPOSURE_AMT;
	}

	public void setR29_EXPOSURE_AMT(BigDecimal r29_EXPOSURE_AMT) {
		R29_EXPOSURE_AMT = r29_EXPOSURE_AMT;
	}

	public BigDecimal getR29_RISK_WEIGHT() {
		return R29_RISK_WEIGHT;
	}

	public void setR29_RISK_WEIGHT(BigDecimal r29_RISK_WEIGHT) {
		R29_RISK_WEIGHT = r29_RISK_WEIGHT;
	}

	public BigDecimal getR29_RISK_WEIGHTED_AMT() {
		return R29_RISK_WEIGHTED_AMT;
	}

	public void setR29_RISK_WEIGHTED_AMT(BigDecimal r29_RISK_WEIGHTED_AMT) {
		R29_RISK_WEIGHTED_AMT = r29_RISK_WEIGHTED_AMT;
	}

	public String getR30_NAME_OF_CORPORATE() {
		return R30_NAME_OF_CORPORATE;
	}

	public void setR30_NAME_OF_CORPORATE(String r30_NAME_OF_CORPORATE) {
		R30_NAME_OF_CORPORATE = r30_NAME_OF_CORPORATE;
	}

	public String getR30_CREDIT_RATING() {
		return R30_CREDIT_RATING;
	}

	public void setR30_CREDIT_RATING(String r30_CREDIT_RATING) {
		R30_CREDIT_RATING = r30_CREDIT_RATING;
	}

	public String getR30_RATING_AGENCY() {
		return R30_RATING_AGENCY;
	}

	public void setR30_RATING_AGENCY(String r30_RATING_AGENCY) {
		R30_RATING_AGENCY = r30_RATING_AGENCY;
	}

	public BigDecimal getR30_EXPOSURE_AMT() {
		return R30_EXPOSURE_AMT;
	}

	public void setR30_EXPOSURE_AMT(BigDecimal r30_EXPOSURE_AMT) {
		R30_EXPOSURE_AMT = r30_EXPOSURE_AMT;
	}

	public BigDecimal getR30_RISK_WEIGHT() {
		return R30_RISK_WEIGHT;
	}

	public void setR30_RISK_WEIGHT(BigDecimal r30_RISK_WEIGHT) {
		R30_RISK_WEIGHT = r30_RISK_WEIGHT;
	}

	public BigDecimal getR30_RISK_WEIGHTED_AMT() {
		return R30_RISK_WEIGHTED_AMT;
	}

	public void setR30_RISK_WEIGHTED_AMT(BigDecimal r30_RISK_WEIGHTED_AMT) {
		R30_RISK_WEIGHTED_AMT = r30_RISK_WEIGHTED_AMT;
	}

	public String getR31_NAME_OF_CORPORATE() {
		return R31_NAME_OF_CORPORATE;
	}

	public void setR31_NAME_OF_CORPORATE(String r31_NAME_OF_CORPORATE) {
		R31_NAME_OF_CORPORATE = r31_NAME_OF_CORPORATE;
	}

	public String getR31_CREDIT_RATING() {
		return R31_CREDIT_RATING;
	}

	public void setR31_CREDIT_RATING(String r31_CREDIT_RATING) {
		R31_CREDIT_RATING = r31_CREDIT_RATING;
	}

	public String getR31_RATING_AGENCY() {
		return R31_RATING_AGENCY;
	}

	public void setR31_RATING_AGENCY(String r31_RATING_AGENCY) {
		R31_RATING_AGENCY = r31_RATING_AGENCY;
	}

	public BigDecimal getR31_EXPOSURE_AMT() {
		return R31_EXPOSURE_AMT;
	}

	public void setR31_EXPOSURE_AMT(BigDecimal r31_EXPOSURE_AMT) {
		R31_EXPOSURE_AMT = r31_EXPOSURE_AMT;
	}

	public BigDecimal getR31_RISK_WEIGHT() {
		return R31_RISK_WEIGHT;
	}

	public void setR31_RISK_WEIGHT(BigDecimal r31_RISK_WEIGHT) {
		R31_RISK_WEIGHT = r31_RISK_WEIGHT;
	}

	public BigDecimal getR31_RISK_WEIGHTED_AMT() {
		return R31_RISK_WEIGHTED_AMT;
	}

	public void setR31_RISK_WEIGHTED_AMT(BigDecimal r31_RISK_WEIGHTED_AMT) {
		R31_RISK_WEIGHTED_AMT = r31_RISK_WEIGHTED_AMT;
	}

	public String getR32_NAME_OF_CORPORATE() {
		return R32_NAME_OF_CORPORATE;
	}

	public void setR32_NAME_OF_CORPORATE(String r32_NAME_OF_CORPORATE) {
		R32_NAME_OF_CORPORATE = r32_NAME_OF_CORPORATE;
	}

	public String getR32_CREDIT_RATING() {
		return R32_CREDIT_RATING;
	}

	public void setR32_CREDIT_RATING(String r32_CREDIT_RATING) {
		R32_CREDIT_RATING = r32_CREDIT_RATING;
	}

	public String getR32_RATING_AGENCY() {
		return R32_RATING_AGENCY;
	}

	public void setR32_RATING_AGENCY(String r32_RATING_AGENCY) {
		R32_RATING_AGENCY = r32_RATING_AGENCY;
	}

	public BigDecimal getR32_EXPOSURE_AMT() {
		return R32_EXPOSURE_AMT;
	}

	public void setR32_EXPOSURE_AMT(BigDecimal r32_EXPOSURE_AMT) {
		R32_EXPOSURE_AMT = r32_EXPOSURE_AMT;
	}

	public BigDecimal getR32_RISK_WEIGHT() {
		return R32_RISK_WEIGHT;
	}

	public void setR32_RISK_WEIGHT(BigDecimal r32_RISK_WEIGHT) {
		R32_RISK_WEIGHT = r32_RISK_WEIGHT;
	}

	public BigDecimal getR32_RISK_WEIGHTED_AMT() {
		return R32_RISK_WEIGHTED_AMT;
	}

	public void setR32_RISK_WEIGHTED_AMT(BigDecimal r32_RISK_WEIGHTED_AMT) {
		R32_RISK_WEIGHTED_AMT = r32_RISK_WEIGHTED_AMT;
	}

	public String getR33_NAME_OF_CORPORATE() {
		return R33_NAME_OF_CORPORATE;
	}

	public void setR33_NAME_OF_CORPORATE(String r33_NAME_OF_CORPORATE) {
		R33_NAME_OF_CORPORATE = r33_NAME_OF_CORPORATE;
	}

	public String getR33_CREDIT_RATING() {
		return R33_CREDIT_RATING;
	}

	public void setR33_CREDIT_RATING(String r33_CREDIT_RATING) {
		R33_CREDIT_RATING = r33_CREDIT_RATING;
	}

	public String getR33_RATING_AGENCY() {
		return R33_RATING_AGENCY;
	}

	public void setR33_RATING_AGENCY(String r33_RATING_AGENCY) {
		R33_RATING_AGENCY = r33_RATING_AGENCY;
	}

	public BigDecimal getR33_EXPOSURE_AMT() {
		return R33_EXPOSURE_AMT;
	}

	public void setR33_EXPOSURE_AMT(BigDecimal r33_EXPOSURE_AMT) {
		R33_EXPOSURE_AMT = r33_EXPOSURE_AMT;
	}

	public BigDecimal getR33_RISK_WEIGHT() {
		return R33_RISK_WEIGHT;
	}

	public void setR33_RISK_WEIGHT(BigDecimal r33_RISK_WEIGHT) {
		R33_RISK_WEIGHT = r33_RISK_WEIGHT;
	}

	public BigDecimal getR33_RISK_WEIGHTED_AMT() {
		return R33_RISK_WEIGHTED_AMT;
	}

	public void setR33_RISK_WEIGHTED_AMT(BigDecimal r33_RISK_WEIGHTED_AMT) {
		R33_RISK_WEIGHTED_AMT = r33_RISK_WEIGHTED_AMT;
	}

	public String getR34_NAME_OF_CORPORATE() {
		return R34_NAME_OF_CORPORATE;
	}

	public void setR34_NAME_OF_CORPORATE(String r34_NAME_OF_CORPORATE) {
		R34_NAME_OF_CORPORATE = r34_NAME_OF_CORPORATE;
	}

	public String getR34_CREDIT_RATING() {
		return R34_CREDIT_RATING;
	}

	public void setR34_CREDIT_RATING(String r34_CREDIT_RATING) {
		R34_CREDIT_RATING = r34_CREDIT_RATING;
	}

	public String getR34_RATING_AGENCY() {
		return R34_RATING_AGENCY;
	}

	public void setR34_RATING_AGENCY(String r34_RATING_AGENCY) {
		R34_RATING_AGENCY = r34_RATING_AGENCY;
	}

	public BigDecimal getR34_EXPOSURE_AMT() {
		return R34_EXPOSURE_AMT;
	}

	public void setR34_EXPOSURE_AMT(BigDecimal r34_EXPOSURE_AMT) {
		R34_EXPOSURE_AMT = r34_EXPOSURE_AMT;
	}

	public BigDecimal getR34_RISK_WEIGHT() {
		return R34_RISK_WEIGHT;
	}

	public void setR34_RISK_WEIGHT(BigDecimal r34_RISK_WEIGHT) {
		R34_RISK_WEIGHT = r34_RISK_WEIGHT;
	}

	public BigDecimal getR34_RISK_WEIGHTED_AMT() {
		return R34_RISK_WEIGHTED_AMT;
	}

	public void setR34_RISK_WEIGHTED_AMT(BigDecimal r34_RISK_WEIGHTED_AMT) {
		R34_RISK_WEIGHTED_AMT = r34_RISK_WEIGHTED_AMT;
	}

	public String getR35_NAME_OF_CORPORATE() {
		return R35_NAME_OF_CORPORATE;
	}

	public void setR35_NAME_OF_CORPORATE(String r35_NAME_OF_CORPORATE) {
		R35_NAME_OF_CORPORATE = r35_NAME_OF_CORPORATE;
	}

	public String getR35_CREDIT_RATING() {
		return R35_CREDIT_RATING;
	}

	public void setR35_CREDIT_RATING(String r35_CREDIT_RATING) {
		R35_CREDIT_RATING = r35_CREDIT_RATING;
	}

	public String getR35_RATING_AGENCY() {
		return R35_RATING_AGENCY;
	}

	public void setR35_RATING_AGENCY(String r35_RATING_AGENCY) {
		R35_RATING_AGENCY = r35_RATING_AGENCY;
	}

	public BigDecimal getR35_EXPOSURE_AMT() {
		return R35_EXPOSURE_AMT;
	}

	public void setR35_EXPOSURE_AMT(BigDecimal r35_EXPOSURE_AMT) {
		R35_EXPOSURE_AMT = r35_EXPOSURE_AMT;
	}

	public BigDecimal getR35_RISK_WEIGHT() {
		return R35_RISK_WEIGHT;
	}

	public void setR35_RISK_WEIGHT(BigDecimal r35_RISK_WEIGHT) {
		R35_RISK_WEIGHT = r35_RISK_WEIGHT;
	}

	public BigDecimal getR35_RISK_WEIGHTED_AMT() {
		return R35_RISK_WEIGHTED_AMT;
	}

	public void setR35_RISK_WEIGHTED_AMT(BigDecimal r35_RISK_WEIGHTED_AMT) {
		R35_RISK_WEIGHTED_AMT = r35_RISK_WEIGHTED_AMT;
	}

	public String getR36_NAME_OF_CORPORATE() {
		return R36_NAME_OF_CORPORATE;
	}

	public void setR36_NAME_OF_CORPORATE(String r36_NAME_OF_CORPORATE) {
		R36_NAME_OF_CORPORATE = r36_NAME_OF_CORPORATE;
	}

	public String getR36_CREDIT_RATING() {
		return R36_CREDIT_RATING;
	}

	public void setR36_CREDIT_RATING(String r36_CREDIT_RATING) {
		R36_CREDIT_RATING = r36_CREDIT_RATING;
	}

	public String getR36_RATING_AGENCY() {
		return R36_RATING_AGENCY;
	}

	public void setR36_RATING_AGENCY(String r36_RATING_AGENCY) {
		R36_RATING_AGENCY = r36_RATING_AGENCY;
	}

	public BigDecimal getR36_EXPOSURE_AMT() {
		return R36_EXPOSURE_AMT;
	}

	public void setR36_EXPOSURE_AMT(BigDecimal r36_EXPOSURE_AMT) {
		R36_EXPOSURE_AMT = r36_EXPOSURE_AMT;
	}

	public BigDecimal getR36_RISK_WEIGHT() {
		return R36_RISK_WEIGHT;
	}

	public void setR36_RISK_WEIGHT(BigDecimal r36_RISK_WEIGHT) {
		R36_RISK_WEIGHT = r36_RISK_WEIGHT;
	}

	public BigDecimal getR36_RISK_WEIGHTED_AMT() {
		return R36_RISK_WEIGHTED_AMT;
	}

	public void setR36_RISK_WEIGHTED_AMT(BigDecimal r36_RISK_WEIGHTED_AMT) {
		R36_RISK_WEIGHTED_AMT = r36_RISK_WEIGHTED_AMT;
	}

	public String getR37_NAME_OF_CORPORATE() {
		return R37_NAME_OF_CORPORATE;
	}

	public void setR37_NAME_OF_CORPORATE(String r37_NAME_OF_CORPORATE) {
		R37_NAME_OF_CORPORATE = r37_NAME_OF_CORPORATE;
	}

	public String getR37_CREDIT_RATING() {
		return R37_CREDIT_RATING;
	}

	public void setR37_CREDIT_RATING(String r37_CREDIT_RATING) {
		R37_CREDIT_RATING = r37_CREDIT_RATING;
	}

	public String getR37_RATING_AGENCY() {
		return R37_RATING_AGENCY;
	}

	public void setR37_RATING_AGENCY(String r37_RATING_AGENCY) {
		R37_RATING_AGENCY = r37_RATING_AGENCY;
	}

	public BigDecimal getR37_EXPOSURE_AMT() {
		return R37_EXPOSURE_AMT;
	}

	public void setR37_EXPOSURE_AMT(BigDecimal r37_EXPOSURE_AMT) {
		R37_EXPOSURE_AMT = r37_EXPOSURE_AMT;
	}

	public BigDecimal getR37_RISK_WEIGHT() {
		return R37_RISK_WEIGHT;
	}

	public void setR37_RISK_WEIGHT(BigDecimal r37_RISK_WEIGHT) {
		R37_RISK_WEIGHT = r37_RISK_WEIGHT;
	}

	public BigDecimal getR37_RISK_WEIGHTED_AMT() {
		return R37_RISK_WEIGHTED_AMT;
	}

	public void setR37_RISK_WEIGHTED_AMT(BigDecimal r37_RISK_WEIGHTED_AMT) {
		R37_RISK_WEIGHTED_AMT = r37_RISK_WEIGHTED_AMT;
	}

	public M_SRWA_12F_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
