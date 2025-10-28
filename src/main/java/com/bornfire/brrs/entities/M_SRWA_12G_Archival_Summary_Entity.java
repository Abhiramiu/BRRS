
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
@Table(name = "BRRS_M_SRWA_12G_ARCHIVALTABLE_SUMMARY")

public class M_SRWA_12G_Archival_Summary_Entity{	




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

	private String r11_security_firm;
	private BigDecimal r11_credit_rating;
	private String r11_rating_agency;
	private BigDecimal r11_exposure_amount;
	private BigDecimal r11_risk_weight;
	private BigDecimal r11_risk_weighted_amount;
	private String r12_security_firm;
	private BigDecimal r12_credit_rating;
	private String r12_rating_agency;
	private BigDecimal r12_exposure_amount;
	private BigDecimal r12_risk_weight;
	private BigDecimal r12_risk_weighted_amount;
	private String r13_security_firm;
	private BigDecimal r13_credit_rating;
	private String r13_rating_agency;
	private BigDecimal r13_exposure_amount;
	private BigDecimal r13_risk_weight;
	private BigDecimal r13_risk_weighted_amount;
	private String r14_security_firm;
	private BigDecimal r14_credit_rating;
	private String r14_rating_agency;
	private BigDecimal r14_exposure_amount;
	private BigDecimal r14_risk_weight;
	private BigDecimal r14_risk_weighted_amount;
	private String r15_security_firm;
	private BigDecimal r15_credit_rating;
	private String r15_rating_agency;
	private BigDecimal r15_exposure_amount;
	private BigDecimal r15_risk_weight;
	private BigDecimal r15_risk_weighted_amount;
	private String r16_security_firm;
	private BigDecimal r16_credit_rating;
	private String r16_rating_agency;
	private BigDecimal r16_exposure_amount;
	private BigDecimal r16_risk_weight;
	private BigDecimal r16_risk_weighted_amount;
	private String r17_security_firm;
	private BigDecimal r17_credit_rating;
	private String r17_rating_agency;
	private BigDecimal r17_exposure_amount;
	private BigDecimal r17_risk_weight;
	private BigDecimal r17_risk_weighted_amount;
	private String r18_security_firm;
	private BigDecimal r18_credit_rating;
	private String r18_rating_agency;
	private BigDecimal r18_exposure_amount;
	private BigDecimal r18_risk_weight;
	private BigDecimal r18_risk_weighted_amount;
	private String r19_security_firm;
	private BigDecimal r19_credit_rating;
	private String r19_rating_agency;
	private BigDecimal r19_exposure_amount;
	private BigDecimal r19_risk_weight;
	private BigDecimal r19_risk_weighted_amount;
	private String r20_security_firm;
	private BigDecimal r20_credit_rating;
	private String r20_rating_agency;
	private BigDecimal r20_exposure_amount;
	private BigDecimal r20_risk_weight;
	private BigDecimal r20_risk_weighted_amount;
	private String r21_security_firm;
	private BigDecimal r21_credit_rating;
	private String r21_rating_agency;
	private BigDecimal r21_exposure_amount;
	private BigDecimal r21_risk_weight;
	private BigDecimal r21_risk_weighted_amount;
	private String r22_security_firm;
	private BigDecimal r22_credit_rating;
	private String r22_rating_agency;
	private BigDecimal r22_exposure_amount;
	private BigDecimal r22_risk_weight;
	private BigDecimal r22_risk_weighted_amount;
	private String r23_security_firm;
	private BigDecimal r23_credit_rating;
	private String r23_rating_agency;
	private BigDecimal r23_exposure_amount;
	private BigDecimal r23_risk_weight;
	private BigDecimal r23_risk_weighted_amount;
	private String r24_security_firm;
	private BigDecimal r24_credit_rating;
	private String r24_rating_agency;
	private BigDecimal r24_exposure_amount;
	private BigDecimal r24_risk_weight;
	private BigDecimal r24_risk_weighted_amount;
	private String r25_security_firm;
	private BigDecimal r25_credit_rating;
	private String r25_rating_agency;
	private BigDecimal r25_exposure_amount;
	private BigDecimal r25_risk_weight;
	private BigDecimal r25_risk_weighted_amount;
	private String r26_security_firm;
	private BigDecimal r26_credit_rating;
	private String r26_rating_agency;
	private BigDecimal r26_exposure_amount;
	private BigDecimal r26_risk_weight;
	private BigDecimal r26_risk_weighted_amount;
	private String r27_security_firm;
	private BigDecimal r27_credit_rating;
	private String r27_rating_agency;
	private BigDecimal r27_exposure_amount;
	private BigDecimal r27_risk_weight;
	private BigDecimal r27_risk_weighted_amount;
	private String r28_security_firm;
	private BigDecimal r28_credit_rating;
	private String r28_rating_agency;
	private BigDecimal r28_exposure_amount;
	private BigDecimal r28_risk_weight;
	private BigDecimal r28_risk_weighted_amount;
	private String r29_security_firm;
	private BigDecimal r29_credit_rating;
	private String r29_rating_agency;
	private BigDecimal r29_exposure_amount;
	private BigDecimal r29_risk_weight;
	private BigDecimal r29_risk_weighted_amount;
	private String r30_security_firm;
	private BigDecimal r30_credit_rating;
	private String r30_rating_agency;
	private BigDecimal r30_exposure_amount;
	private BigDecimal r30_risk_weight;
	private BigDecimal r30_risk_weighted_amount;
	private String r31_security_firm;
	private BigDecimal r31_credit_rating;
	private String r31_rating_agency;
	private BigDecimal r31_exposure_amount;
	private BigDecimal r31_risk_weight;
	private BigDecimal r31_risk_weighted_amount;
	private String r32_security_firm;
	private BigDecimal r32_credit_rating;
	private String r32_rating_agency;
	private BigDecimal r32_exposure_amount;
	private BigDecimal r32_risk_weight;
	private BigDecimal r32_risk_weighted_amount;
	private String r33_security_firm;
	private BigDecimal r33_credit_rating;
	private String r33_rating_agency;
	private BigDecimal r33_exposure_amount;
	private BigDecimal r33_risk_weight;
	private BigDecimal r33_risk_weighted_amount;
	private String r34_security_firm;
	private BigDecimal r34_credit_rating;
	private String r34_rating_agency;
	private BigDecimal r34_exposure_amount;
	private BigDecimal r34_risk_weight;
	private BigDecimal r34_risk_weighted_amount;
	private String r35_security_firm;
	private BigDecimal r35_credit_rating;
	private String r35_rating_agency;
	private BigDecimal r35_exposure_amount;
	private BigDecimal r35_risk_weight;
	private BigDecimal r35_risk_weighted_amount;
	private String r36_security_firm;
	private BigDecimal r36_credit_rating;
	private String r36_rating_agency;
	private BigDecimal r36_exposure_amount;
	private BigDecimal r36_risk_weight;
	private BigDecimal r36_risk_weighted_amount;
	private String r37_security_firm;
	private BigDecimal r37_credit_rating;
	private String r37_rating_agency;
	private BigDecimal r37_exposure_amount;
	private BigDecimal r37_risk_weight;
	private BigDecimal r37_risk_weighted_amount;
	private String r38_security_firm;
	private BigDecimal r38_credit_rating;
	private String r38_rating_agency;
	private BigDecimal r38_exposure_amount;
	private BigDecimal r38_risk_weight;
	private BigDecimal r38_risk_weighted_amount;
	private String r39_security_firm;
	private BigDecimal r39_credit_rating;
	private String r39_rating_agency;
	private BigDecimal r39_exposure_amount;
	private BigDecimal r39_risk_weight;
	private BigDecimal r39_risk_weighted_amount;
	private String r40_security_firm;
	private BigDecimal r40_credit_rating;
	private String r40_rating_agency;
	private BigDecimal r40_exposure_amount;
	private BigDecimal r40_risk_weight;
	private BigDecimal r40_risk_weighted_amount;
	private String r41_security_firm;
	private BigDecimal r41_credit_rating;
	private String r41_rating_agency;
	private BigDecimal r41_exposure_amount;
	private BigDecimal r41_risk_weight;
	private BigDecimal r41_risk_weighted_amount;
	private String r42_security_firm;
	private BigDecimal r42_credit_rating;
	private String r42_rating_agency;
	private BigDecimal r42_exposure_amount;
	private BigDecimal r42_risk_weight;
	private BigDecimal r42_risk_weighted_amount;
	private String r43_security_firm;
	private BigDecimal r43_credit_rating;
	private String r43_rating_agency;
	private BigDecimal r43_exposure_amount;
	private BigDecimal r43_risk_weight;
	private BigDecimal r43_risk_weighted_amount;
	private String r44_security_firm;
	private BigDecimal r44_credit_rating;
	private String r44_rating_agency;
	private BigDecimal r44_exposure_amount;
	private BigDecimal r44_risk_weight;
	private BigDecimal r44_risk_weighted_amount;
	private String r45_security_firm;
	private BigDecimal r45_credit_rating;
	private String r45_rating_agency;
	private BigDecimal r45_exposure_amount;
	private BigDecimal r45_risk_weight;
	private BigDecimal r45_risk_weighted_amount;
	private String r46_security_firm;
	private BigDecimal r46_credit_rating;
	private String r46_rating_agency;
	private BigDecimal r46_exposure_amount;
	private BigDecimal r46_risk_weight;
	private BigDecimal r46_risk_weighted_amount;
	private String r47_security_firm;
	private BigDecimal r47_credit_rating;
	private String r47_rating_agency;
	private BigDecimal r47_exposure_amount;
	private BigDecimal r47_risk_weight;
	private BigDecimal r47_risk_weighted_amount;
	private String r48_security_firm;
	private BigDecimal r48_credit_rating;
	private String r48_rating_agency;
	private BigDecimal r48_exposure_amount;
	private BigDecimal r48_risk_weight;
	private BigDecimal r48_risk_weighted_amount;
	private String r49_security_firm;
	private BigDecimal r49_credit_rating;
	private String r49_rating_agency;
	private BigDecimal r49_exposure_amount;
	private BigDecimal r49_risk_weight;
	private BigDecimal r49_risk_weighted_amount;
	private String r50_security_firm;
	private BigDecimal r50_credit_rating;
	private String r50_rating_agency;
	private BigDecimal r50_exposure_amount;
	private BigDecimal r50_risk_weight;
	private BigDecimal r50_risk_weighted_amount;
	private String r51_security_firm;
	private BigDecimal r51_credit_rating;
	private String r51_rating_agency;
	private BigDecimal r51_exposure_amount;
	private BigDecimal r51_risk_weight;
	private BigDecimal r51_risk_weighted_amount;
	private String r52_security_firm;
	private BigDecimal r52_credit_rating;
	private String r52_rating_agency;
	private BigDecimal r52_exposure_amount;
	private BigDecimal r52_risk_weight;
	private BigDecimal r52_risk_weighted_amount;
	private String r53_security_firm;
	private BigDecimal r53_credit_rating;
	private String r53_rating_agency;
	private BigDecimal r53_exposure_amount;
	private BigDecimal r53_risk_weight;
	private BigDecimal r53_risk_weighted_amount;
	private String r54_security_firm;
	private BigDecimal r54_credit_rating;
	private String r54_rating_agency;
	private BigDecimal r54_exposure_amount;
	private BigDecimal r54_risk_weight;
	private BigDecimal r54_risk_weighted_amount;
	private String r55_security_firm;
	private BigDecimal r55_credit_rating;
	private String r55_rating_agency;
	private BigDecimal r55_exposure_amount;
	private BigDecimal r55_risk_weight;
	private BigDecimal r55_risk_weighted_amount;
	private String r56_security_firm;
	private BigDecimal r56_credit_rating;
	private String r56_rating_agency;
	private BigDecimal r56_exposure_amount;
	private BigDecimal r56_risk_weight;
	private BigDecimal r56_risk_weighted_amount;
	private String r57_security_firm;
	private BigDecimal r57_credit_rating;
	private String r57_rating_agency;
	private BigDecimal r57_exposure_amount;
	private BigDecimal r57_risk_weight;
	private BigDecimal r57_risk_weighted_amount;
	private String r58_security_firm;
	private BigDecimal r58_credit_rating;
	private String r58_rating_agency;
	private BigDecimal r58_exposure_amount;
	private BigDecimal r58_risk_weight;
	private BigDecimal r58_risk_weighted_amount;
	private String r59_security_firm;
	private BigDecimal r59_credit_rating;
	private String r59_rating_agency;
	private BigDecimal r59_exposure_amount;
	private BigDecimal r59_risk_weight;
	private BigDecimal r59_risk_weighted_amount;
	private String r60_security_firm;
	private BigDecimal r60_credit_rating;
	private String r60_rating_agency;
	private BigDecimal r60_exposure_amount;
	private BigDecimal r60_risk_weight;
	private BigDecimal r60_risk_weighted_amount;
	private BigDecimal r61_exposure_amount;
	private BigDecimal r61_risk_weighted_amount;

	
	
	
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




	public String getR11_security_firm() {
		return r11_security_firm;
	}




	public void setR11_security_firm(String r11_security_firm) {
		this.r11_security_firm = r11_security_firm;
	}




	public BigDecimal getR11_credit_rating() {
		return r11_credit_rating;
	}




	public void setR11_credit_rating(BigDecimal r11_credit_rating) {
		this.r11_credit_rating = r11_credit_rating;
	}




	public String getR11_rating_agency() {
		return r11_rating_agency;
	}




	public void setR11_rating_agency(String r11_rating_agency) {
		this.r11_rating_agency = r11_rating_agency;
	}




	public BigDecimal getR11_exposure_amount() {
		return r11_exposure_amount;
	}




	public void setR11_exposure_amount(BigDecimal r11_exposure_amount) {
		this.r11_exposure_amount = r11_exposure_amount;
	}




	public BigDecimal getR11_risk_weight() {
		return r11_risk_weight;
	}




	public void setR11_risk_weight(BigDecimal r11_risk_weight) {
		this.r11_risk_weight = r11_risk_weight;
	}




	public BigDecimal getR11_risk_weighted_amount() {
		return r11_risk_weighted_amount;
	}




	public void setR11_risk_weighted_amount(BigDecimal r11_risk_weighted_amount) {
		this.r11_risk_weighted_amount = r11_risk_weighted_amount;
	}




	public String getR12_security_firm() {
		return r12_security_firm;
	}




	public void setR12_security_firm(String r12_security_firm) {
		this.r12_security_firm = r12_security_firm;
	}




	public BigDecimal getR12_credit_rating() {
		return r12_credit_rating;
	}




	public void setR12_credit_rating(BigDecimal r12_credit_rating) {
		this.r12_credit_rating = r12_credit_rating;
	}




	public String getR12_rating_agency() {
		return r12_rating_agency;
	}




	public void setR12_rating_agency(String r12_rating_agency) {
		this.r12_rating_agency = r12_rating_agency;
	}




	public BigDecimal getR12_exposure_amount() {
		return r12_exposure_amount;
	}




	public void setR12_exposure_amount(BigDecimal r12_exposure_amount) {
		this.r12_exposure_amount = r12_exposure_amount;
	}




	public BigDecimal getR12_risk_weight() {
		return r12_risk_weight;
	}




	public void setR12_risk_weight(BigDecimal r12_risk_weight) {
		this.r12_risk_weight = r12_risk_weight;
	}




	public BigDecimal getR12_risk_weighted_amount() {
		return r12_risk_weighted_amount;
	}




	public void setR12_risk_weighted_amount(BigDecimal r12_risk_weighted_amount) {
		this.r12_risk_weighted_amount = r12_risk_weighted_amount;
	}




	public String getR13_security_firm() {
		return r13_security_firm;
	}




	public void setR13_security_firm(String r13_security_firm) {
		this.r13_security_firm = r13_security_firm;
	}




	public BigDecimal getR13_credit_rating() {
		return r13_credit_rating;
	}




	public void setR13_credit_rating(BigDecimal r13_credit_rating) {
		this.r13_credit_rating = r13_credit_rating;
	}




	public String getR13_rating_agency() {
		return r13_rating_agency;
	}




	public void setR13_rating_agency(String r13_rating_agency) {
		this.r13_rating_agency = r13_rating_agency;
	}




	public BigDecimal getR13_exposure_amount() {
		return r13_exposure_amount;
	}




	public void setR13_exposure_amount(BigDecimal r13_exposure_amount) {
		this.r13_exposure_amount = r13_exposure_amount;
	}




	public BigDecimal getR13_risk_weight() {
		return r13_risk_weight;
	}




	public void setR13_risk_weight(BigDecimal r13_risk_weight) {
		this.r13_risk_weight = r13_risk_weight;
	}




	public BigDecimal getR13_risk_weighted_amount() {
		return r13_risk_weighted_amount;
	}




	public void setR13_risk_weighted_amount(BigDecimal r13_risk_weighted_amount) {
		this.r13_risk_weighted_amount = r13_risk_weighted_amount;
	}




	public String getR14_security_firm() {
		return r14_security_firm;
	}




	public void setR14_security_firm(String r14_security_firm) {
		this.r14_security_firm = r14_security_firm;
	}




	public BigDecimal getR14_credit_rating() {
		return r14_credit_rating;
	}




	public void setR14_credit_rating(BigDecimal r14_credit_rating) {
		this.r14_credit_rating = r14_credit_rating;
	}




	public String getR14_rating_agency() {
		return r14_rating_agency;
	}




	public void setR14_rating_agency(String r14_rating_agency) {
		this.r14_rating_agency = r14_rating_agency;
	}




	public BigDecimal getR14_exposure_amount() {
		return r14_exposure_amount;
	}




	public void setR14_exposure_amount(BigDecimal r14_exposure_amount) {
		this.r14_exposure_amount = r14_exposure_amount;
	}




	public BigDecimal getR14_risk_weight() {
		return r14_risk_weight;
	}




	public void setR14_risk_weight(BigDecimal r14_risk_weight) {
		this.r14_risk_weight = r14_risk_weight;
	}




	public BigDecimal getR14_risk_weighted_amount() {
		return r14_risk_weighted_amount;
	}




	public void setR14_risk_weighted_amount(BigDecimal r14_risk_weighted_amount) {
		this.r14_risk_weighted_amount = r14_risk_weighted_amount;
	}




	public String getR15_security_firm() {
		return r15_security_firm;
	}




	public void setR15_security_firm(String r15_security_firm) {
		this.r15_security_firm = r15_security_firm;
	}




	public BigDecimal getR15_credit_rating() {
		return r15_credit_rating;
	}




	public void setR15_credit_rating(BigDecimal r15_credit_rating) {
		this.r15_credit_rating = r15_credit_rating;
	}




	public String getR15_rating_agency() {
		return r15_rating_agency;
	}




	public void setR15_rating_agency(String r15_rating_agency) {
		this.r15_rating_agency = r15_rating_agency;
	}




	public BigDecimal getR15_exposure_amount() {
		return r15_exposure_amount;
	}




	public void setR15_exposure_amount(BigDecimal r15_exposure_amount) {
		this.r15_exposure_amount = r15_exposure_amount;
	}




	public BigDecimal getR15_risk_weight() {
		return r15_risk_weight;
	}




	public void setR15_risk_weight(BigDecimal r15_risk_weight) {
		this.r15_risk_weight = r15_risk_weight;
	}




	public BigDecimal getR15_risk_weighted_amount() {
		return r15_risk_weighted_amount;
	}




	public void setR15_risk_weighted_amount(BigDecimal r15_risk_weighted_amount) {
		this.r15_risk_weighted_amount = r15_risk_weighted_amount;
	}




	public String getR16_security_firm() {
		return r16_security_firm;
	}




	public void setR16_security_firm(String r16_security_firm) {
		this.r16_security_firm = r16_security_firm;
	}




	public BigDecimal getR16_credit_rating() {
		return r16_credit_rating;
	}




	public void setR16_credit_rating(BigDecimal r16_credit_rating) {
		this.r16_credit_rating = r16_credit_rating;
	}




	public String getR16_rating_agency() {
		return r16_rating_agency;
	}




	public void setR16_rating_agency(String r16_rating_agency) {
		this.r16_rating_agency = r16_rating_agency;
	}




	public BigDecimal getR16_exposure_amount() {
		return r16_exposure_amount;
	}




	public void setR16_exposure_amount(BigDecimal r16_exposure_amount) {
		this.r16_exposure_amount = r16_exposure_amount;
	}




	public BigDecimal getR16_risk_weight() {
		return r16_risk_weight;
	}




	public void setR16_risk_weight(BigDecimal r16_risk_weight) {
		this.r16_risk_weight = r16_risk_weight;
	}




	public BigDecimal getR16_risk_weighted_amount() {
		return r16_risk_weighted_amount;
	}




	public void setR16_risk_weighted_amount(BigDecimal r16_risk_weighted_amount) {
		this.r16_risk_weighted_amount = r16_risk_weighted_amount;
	}




	public String getR17_security_firm() {
		return r17_security_firm;
	}




	public void setR17_security_firm(String r17_security_firm) {
		this.r17_security_firm = r17_security_firm;
	}




	public BigDecimal getR17_credit_rating() {
		return r17_credit_rating;
	}




	public void setR17_credit_rating(BigDecimal r17_credit_rating) {
		this.r17_credit_rating = r17_credit_rating;
	}




	public String getR17_rating_agency() {
		return r17_rating_agency;
	}




	public void setR17_rating_agency(String r17_rating_agency) {
		this.r17_rating_agency = r17_rating_agency;
	}




	public BigDecimal getR17_exposure_amount() {
		return r17_exposure_amount;
	}




	public void setR17_exposure_amount(BigDecimal r17_exposure_amount) {
		this.r17_exposure_amount = r17_exposure_amount;
	}




	public BigDecimal getR17_risk_weight() {
		return r17_risk_weight;
	}




	public void setR17_risk_weight(BigDecimal r17_risk_weight) {
		this.r17_risk_weight = r17_risk_weight;
	}




	public BigDecimal getR17_risk_weighted_amount() {
		return r17_risk_weighted_amount;
	}




	public void setR17_risk_weighted_amount(BigDecimal r17_risk_weighted_amount) {
		this.r17_risk_weighted_amount = r17_risk_weighted_amount;
	}




	public String getR18_security_firm() {
		return r18_security_firm;
	}




	public void setR18_security_firm(String r18_security_firm) {
		this.r18_security_firm = r18_security_firm;
	}




	public BigDecimal getR18_credit_rating() {
		return r18_credit_rating;
	}




	public void setR18_credit_rating(BigDecimal r18_credit_rating) {
		this.r18_credit_rating = r18_credit_rating;
	}




	public String getR18_rating_agency() {
		return r18_rating_agency;
	}




	public void setR18_rating_agency(String r18_rating_agency) {
		this.r18_rating_agency = r18_rating_agency;
	}




	public BigDecimal getR18_exposure_amount() {
		return r18_exposure_amount;
	}




	public void setR18_exposure_amount(BigDecimal r18_exposure_amount) {
		this.r18_exposure_amount = r18_exposure_amount;
	}




	public BigDecimal getR18_risk_weight() {
		return r18_risk_weight;
	}




	public void setR18_risk_weight(BigDecimal r18_risk_weight) {
		this.r18_risk_weight = r18_risk_weight;
	}




	public BigDecimal getR18_risk_weighted_amount() {
		return r18_risk_weighted_amount;
	}




	public void setR18_risk_weighted_amount(BigDecimal r18_risk_weighted_amount) {
		this.r18_risk_weighted_amount = r18_risk_weighted_amount;
	}




	public String getR19_security_firm() {
		return r19_security_firm;
	}




	public void setR19_security_firm(String r19_security_firm) {
		this.r19_security_firm = r19_security_firm;
	}




	public BigDecimal getR19_credit_rating() {
		return r19_credit_rating;
	}




	public void setR19_credit_rating(BigDecimal r19_credit_rating) {
		this.r19_credit_rating = r19_credit_rating;
	}




	public String getR19_rating_agency() {
		return r19_rating_agency;
	}




	public void setR19_rating_agency(String r19_rating_agency) {
		this.r19_rating_agency = r19_rating_agency;
	}




	public BigDecimal getR19_exposure_amount() {
		return r19_exposure_amount;
	}




	public void setR19_exposure_amount(BigDecimal r19_exposure_amount) {
		this.r19_exposure_amount = r19_exposure_amount;
	}




	public BigDecimal getR19_risk_weight() {
		return r19_risk_weight;
	}




	public void setR19_risk_weight(BigDecimal r19_risk_weight) {
		this.r19_risk_weight = r19_risk_weight;
	}




	public BigDecimal getR19_risk_weighted_amount() {
		return r19_risk_weighted_amount;
	}




	public void setR19_risk_weighted_amount(BigDecimal r19_risk_weighted_amount) {
		this.r19_risk_weighted_amount = r19_risk_weighted_amount;
	}




	public String getR20_security_firm() {
		return r20_security_firm;
	}




	public void setR20_security_firm(String r20_security_firm) {
		this.r20_security_firm = r20_security_firm;
	}




	public BigDecimal getR20_credit_rating() {
		return r20_credit_rating;
	}




	public void setR20_credit_rating(BigDecimal r20_credit_rating) {
		this.r20_credit_rating = r20_credit_rating;
	}




	public String getR20_rating_agency() {
		return r20_rating_agency;
	}




	public void setR20_rating_agency(String r20_rating_agency) {
		this.r20_rating_agency = r20_rating_agency;
	}




	public BigDecimal getR20_exposure_amount() {
		return r20_exposure_amount;
	}




	public void setR20_exposure_amount(BigDecimal r20_exposure_amount) {
		this.r20_exposure_amount = r20_exposure_amount;
	}




	public BigDecimal getR20_risk_weight() {
		return r20_risk_weight;
	}




	public void setR20_risk_weight(BigDecimal r20_risk_weight) {
		this.r20_risk_weight = r20_risk_weight;
	}




	public BigDecimal getR20_risk_weighted_amount() {
		return r20_risk_weighted_amount;
	}




	public void setR20_risk_weighted_amount(BigDecimal r20_risk_weighted_amount) {
		this.r20_risk_weighted_amount = r20_risk_weighted_amount;
	}




	public String getR21_security_firm() {
		return r21_security_firm;
	}




	public void setR21_security_firm(String r21_security_firm) {
		this.r21_security_firm = r21_security_firm;
	}




	public BigDecimal getR21_credit_rating() {
		return r21_credit_rating;
	}




	public void setR21_credit_rating(BigDecimal r21_credit_rating) {
		this.r21_credit_rating = r21_credit_rating;
	}




	public String getR21_rating_agency() {
		return r21_rating_agency;
	}




	public void setR21_rating_agency(String r21_rating_agency) {
		this.r21_rating_agency = r21_rating_agency;
	}




	public BigDecimal getR21_exposure_amount() {
		return r21_exposure_amount;
	}




	public void setR21_exposure_amount(BigDecimal r21_exposure_amount) {
		this.r21_exposure_amount = r21_exposure_amount;
	}




	public BigDecimal getR21_risk_weight() {
		return r21_risk_weight;
	}




	public void setR21_risk_weight(BigDecimal r21_risk_weight) {
		this.r21_risk_weight = r21_risk_weight;
	}




	public BigDecimal getR21_risk_weighted_amount() {
		return r21_risk_weighted_amount;
	}




	public void setR21_risk_weighted_amount(BigDecimal r21_risk_weighted_amount) {
		this.r21_risk_weighted_amount = r21_risk_weighted_amount;
	}




	public String getR22_security_firm() {
		return r22_security_firm;
	}




	public void setR22_security_firm(String r22_security_firm) {
		this.r22_security_firm = r22_security_firm;
	}




	public BigDecimal getR22_credit_rating() {
		return r22_credit_rating;
	}




	public void setR22_credit_rating(BigDecimal r22_credit_rating) {
		this.r22_credit_rating = r22_credit_rating;
	}




	public String getR22_rating_agency() {
		return r22_rating_agency;
	}




	public void setR22_rating_agency(String r22_rating_agency) {
		this.r22_rating_agency = r22_rating_agency;
	}




	public BigDecimal getR22_exposure_amount() {
		return r22_exposure_amount;
	}




	public void setR22_exposure_amount(BigDecimal r22_exposure_amount) {
		this.r22_exposure_amount = r22_exposure_amount;
	}




	public BigDecimal getR22_risk_weight() {
		return r22_risk_weight;
	}




	public void setR22_risk_weight(BigDecimal r22_risk_weight) {
		this.r22_risk_weight = r22_risk_weight;
	}




	public BigDecimal getR22_risk_weighted_amount() {
		return r22_risk_weighted_amount;
	}




	public void setR22_risk_weighted_amount(BigDecimal r22_risk_weighted_amount) {
		this.r22_risk_weighted_amount = r22_risk_weighted_amount;
	}




	public String getR23_security_firm() {
		return r23_security_firm;
	}




	public void setR23_security_firm(String r23_security_firm) {
		this.r23_security_firm = r23_security_firm;
	}




	public BigDecimal getR23_credit_rating() {
		return r23_credit_rating;
	}




	public void setR23_credit_rating(BigDecimal r23_credit_rating) {
		this.r23_credit_rating = r23_credit_rating;
	}




	public String getR23_rating_agency() {
		return r23_rating_agency;
	}




	public void setR23_rating_agency(String r23_rating_agency) {
		this.r23_rating_agency = r23_rating_agency;
	}




	public BigDecimal getR23_exposure_amount() {
		return r23_exposure_amount;
	}




	public void setR23_exposure_amount(BigDecimal r23_exposure_amount) {
		this.r23_exposure_amount = r23_exposure_amount;
	}




	public BigDecimal getR23_risk_weight() {
		return r23_risk_weight;
	}




	public void setR23_risk_weight(BigDecimal r23_risk_weight) {
		this.r23_risk_weight = r23_risk_weight;
	}




	public BigDecimal getR23_risk_weighted_amount() {
		return r23_risk_weighted_amount;
	}




	public void setR23_risk_weighted_amount(BigDecimal r23_risk_weighted_amount) {
		this.r23_risk_weighted_amount = r23_risk_weighted_amount;
	}




	public String getR24_security_firm() {
		return r24_security_firm;
	}




	public void setR24_security_firm(String r24_security_firm) {
		this.r24_security_firm = r24_security_firm;
	}




	public BigDecimal getR24_credit_rating() {
		return r24_credit_rating;
	}




	public void setR24_credit_rating(BigDecimal r24_credit_rating) {
		this.r24_credit_rating = r24_credit_rating;
	}




	public String getR24_rating_agency() {
		return r24_rating_agency;
	}




	public void setR24_rating_agency(String r24_rating_agency) {
		this.r24_rating_agency = r24_rating_agency;
	}




	public BigDecimal getR24_exposure_amount() {
		return r24_exposure_amount;
	}




	public void setR24_exposure_amount(BigDecimal r24_exposure_amount) {
		this.r24_exposure_amount = r24_exposure_amount;
	}




	public BigDecimal getR24_risk_weight() {
		return r24_risk_weight;
	}




	public void setR24_risk_weight(BigDecimal r24_risk_weight) {
		this.r24_risk_weight = r24_risk_weight;
	}




	public BigDecimal getR24_risk_weighted_amount() {
		return r24_risk_weighted_amount;
	}




	public void setR24_risk_weighted_amount(BigDecimal r24_risk_weighted_amount) {
		this.r24_risk_weighted_amount = r24_risk_weighted_amount;
	}




	public String getR25_security_firm() {
		return r25_security_firm;
	}




	public void setR25_security_firm(String r25_security_firm) {
		this.r25_security_firm = r25_security_firm;
	}




	public BigDecimal getR25_credit_rating() {
		return r25_credit_rating;
	}




	public void setR25_credit_rating(BigDecimal r25_credit_rating) {
		this.r25_credit_rating = r25_credit_rating;
	}




	public String getR25_rating_agency() {
		return r25_rating_agency;
	}




	public void setR25_rating_agency(String r25_rating_agency) {
		this.r25_rating_agency = r25_rating_agency;
	}




	public BigDecimal getR25_exposure_amount() {
		return r25_exposure_amount;
	}




	public void setR25_exposure_amount(BigDecimal r25_exposure_amount) {
		this.r25_exposure_amount = r25_exposure_amount;
	}




	public BigDecimal getR25_risk_weight() {
		return r25_risk_weight;
	}




	public void setR25_risk_weight(BigDecimal r25_risk_weight) {
		this.r25_risk_weight = r25_risk_weight;
	}




	public BigDecimal getR25_risk_weighted_amount() {
		return r25_risk_weighted_amount;
	}




	public void setR25_risk_weighted_amount(BigDecimal r25_risk_weighted_amount) {
		this.r25_risk_weighted_amount = r25_risk_weighted_amount;
	}




	public String getR26_security_firm() {
		return r26_security_firm;
	}




	public void setR26_security_firm(String r26_security_firm) {
		this.r26_security_firm = r26_security_firm;
	}




	public BigDecimal getR26_credit_rating() {
		return r26_credit_rating;
	}




	public void setR26_credit_rating(BigDecimal r26_credit_rating) {
		this.r26_credit_rating = r26_credit_rating;
	}




	public String getR26_rating_agency() {
		return r26_rating_agency;
	}




	public void setR26_rating_agency(String r26_rating_agency) {
		this.r26_rating_agency = r26_rating_agency;
	}




	public BigDecimal getR26_exposure_amount() {
		return r26_exposure_amount;
	}




	public void setR26_exposure_amount(BigDecimal r26_exposure_amount) {
		this.r26_exposure_amount = r26_exposure_amount;
	}




	public BigDecimal getR26_risk_weight() {
		return r26_risk_weight;
	}




	public void setR26_risk_weight(BigDecimal r26_risk_weight) {
		this.r26_risk_weight = r26_risk_weight;
	}




	public BigDecimal getR26_risk_weighted_amount() {
		return r26_risk_weighted_amount;
	}




	public void setR26_risk_weighted_amount(BigDecimal r26_risk_weighted_amount) {
		this.r26_risk_weighted_amount = r26_risk_weighted_amount;
	}




	public String getR27_security_firm() {
		return r27_security_firm;
	}




	public void setR27_security_firm(String r27_security_firm) {
		this.r27_security_firm = r27_security_firm;
	}




	public BigDecimal getR27_credit_rating() {
		return r27_credit_rating;
	}




	public void setR27_credit_rating(BigDecimal r27_credit_rating) {
		this.r27_credit_rating = r27_credit_rating;
	}




	public String getR27_rating_agency() {
		return r27_rating_agency;
	}




	public void setR27_rating_agency(String r27_rating_agency) {
		this.r27_rating_agency = r27_rating_agency;
	}




	public BigDecimal getR27_exposure_amount() {
		return r27_exposure_amount;
	}




	public void setR27_exposure_amount(BigDecimal r27_exposure_amount) {
		this.r27_exposure_amount = r27_exposure_amount;
	}




	public BigDecimal getR27_risk_weight() {
		return r27_risk_weight;
	}




	public void setR27_risk_weight(BigDecimal r27_risk_weight) {
		this.r27_risk_weight = r27_risk_weight;
	}




	public BigDecimal getR27_risk_weighted_amount() {
		return r27_risk_weighted_amount;
	}




	public void setR27_risk_weighted_amount(BigDecimal r27_risk_weighted_amount) {
		this.r27_risk_weighted_amount = r27_risk_weighted_amount;
	}




	public String getR28_security_firm() {
		return r28_security_firm;
	}




	public void setR28_security_firm(String r28_security_firm) {
		this.r28_security_firm = r28_security_firm;
	}




	public BigDecimal getR28_credit_rating() {
		return r28_credit_rating;
	}




	public void setR28_credit_rating(BigDecimal r28_credit_rating) {
		this.r28_credit_rating = r28_credit_rating;
	}




	public String getR28_rating_agency() {
		return r28_rating_agency;
	}




	public void setR28_rating_agency(String r28_rating_agency) {
		this.r28_rating_agency = r28_rating_agency;
	}




	public BigDecimal getR28_exposure_amount() {
		return r28_exposure_amount;
	}




	public void setR28_exposure_amount(BigDecimal r28_exposure_amount) {
		this.r28_exposure_amount = r28_exposure_amount;
	}




	public BigDecimal getR28_risk_weight() {
		return r28_risk_weight;
	}




	public void setR28_risk_weight(BigDecimal r28_risk_weight) {
		this.r28_risk_weight = r28_risk_weight;
	}




	public BigDecimal getR28_risk_weighted_amount() {
		return r28_risk_weighted_amount;
	}




	public void setR28_risk_weighted_amount(BigDecimal r28_risk_weighted_amount) {
		this.r28_risk_weighted_amount = r28_risk_weighted_amount;
	}




	public String getR29_security_firm() {
		return r29_security_firm;
	}




	public void setR29_security_firm(String r29_security_firm) {
		this.r29_security_firm = r29_security_firm;
	}




	public BigDecimal getR29_credit_rating() {
		return r29_credit_rating;
	}




	public void setR29_credit_rating(BigDecimal r29_credit_rating) {
		this.r29_credit_rating = r29_credit_rating;
	}




	public String getR29_rating_agency() {
		return r29_rating_agency;
	}




	public void setR29_rating_agency(String r29_rating_agency) {
		this.r29_rating_agency = r29_rating_agency;
	}




	public BigDecimal getR29_exposure_amount() {
		return r29_exposure_amount;
	}




	public void setR29_exposure_amount(BigDecimal r29_exposure_amount) {
		this.r29_exposure_amount = r29_exposure_amount;
	}




	public BigDecimal getR29_risk_weight() {
		return r29_risk_weight;
	}




	public void setR29_risk_weight(BigDecimal r29_risk_weight) {
		this.r29_risk_weight = r29_risk_weight;
	}




	public BigDecimal getR29_risk_weighted_amount() {
		return r29_risk_weighted_amount;
	}




	public void setR29_risk_weighted_amount(BigDecimal r29_risk_weighted_amount) {
		this.r29_risk_weighted_amount = r29_risk_weighted_amount;
	}




	public String getR30_security_firm() {
		return r30_security_firm;
	}




	public void setR30_security_firm(String r30_security_firm) {
		this.r30_security_firm = r30_security_firm;
	}




	public BigDecimal getR30_credit_rating() {
		return r30_credit_rating;
	}




	public void setR30_credit_rating(BigDecimal r30_credit_rating) {
		this.r30_credit_rating = r30_credit_rating;
	}




	public String getR30_rating_agency() {
		return r30_rating_agency;
	}




	public void setR30_rating_agency(String r30_rating_agency) {
		this.r30_rating_agency = r30_rating_agency;
	}




	public BigDecimal getR30_exposure_amount() {
		return r30_exposure_amount;
	}




	public void setR30_exposure_amount(BigDecimal r30_exposure_amount) {
		this.r30_exposure_amount = r30_exposure_amount;
	}




	public BigDecimal getR30_risk_weight() {
		return r30_risk_weight;
	}




	public void setR30_risk_weight(BigDecimal r30_risk_weight) {
		this.r30_risk_weight = r30_risk_weight;
	}




	public BigDecimal getR30_risk_weighted_amount() {
		return r30_risk_weighted_amount;
	}




	public void setR30_risk_weighted_amount(BigDecimal r30_risk_weighted_amount) {
		this.r30_risk_weighted_amount = r30_risk_weighted_amount;
	}




	public String getR31_security_firm() {
		return r31_security_firm;
	}




	public void setR31_security_firm(String r31_security_firm) {
		this.r31_security_firm = r31_security_firm;
	}




	public BigDecimal getR31_credit_rating() {
		return r31_credit_rating;
	}




	public void setR31_credit_rating(BigDecimal r31_credit_rating) {
		this.r31_credit_rating = r31_credit_rating;
	}




	public String getR31_rating_agency() {
		return r31_rating_agency;
	}




	public void setR31_rating_agency(String r31_rating_agency) {
		this.r31_rating_agency = r31_rating_agency;
	}




	public BigDecimal getR31_exposure_amount() {
		return r31_exposure_amount;
	}




	public void setR31_exposure_amount(BigDecimal r31_exposure_amount) {
		this.r31_exposure_amount = r31_exposure_amount;
	}




	public BigDecimal getR31_risk_weight() {
		return r31_risk_weight;
	}




	public void setR31_risk_weight(BigDecimal r31_risk_weight) {
		this.r31_risk_weight = r31_risk_weight;
	}




	public BigDecimal getR31_risk_weighted_amount() {
		return r31_risk_weighted_amount;
	}




	public void setR31_risk_weighted_amount(BigDecimal r31_risk_weighted_amount) {
		this.r31_risk_weighted_amount = r31_risk_weighted_amount;
	}




	public String getR32_security_firm() {
		return r32_security_firm;
	}




	public void setR32_security_firm(String r32_security_firm) {
		this.r32_security_firm = r32_security_firm;
	}




	public BigDecimal getR32_credit_rating() {
		return r32_credit_rating;
	}




	public void setR32_credit_rating(BigDecimal r32_credit_rating) {
		this.r32_credit_rating = r32_credit_rating;
	}




	public String getR32_rating_agency() {
		return r32_rating_agency;
	}




	public void setR32_rating_agency(String r32_rating_agency) {
		this.r32_rating_agency = r32_rating_agency;
	}




	public BigDecimal getR32_exposure_amount() {
		return r32_exposure_amount;
	}




	public void setR32_exposure_amount(BigDecimal r32_exposure_amount) {
		this.r32_exposure_amount = r32_exposure_amount;
	}




	public BigDecimal getR32_risk_weight() {
		return r32_risk_weight;
	}




	public void setR32_risk_weight(BigDecimal r32_risk_weight) {
		this.r32_risk_weight = r32_risk_weight;
	}




	public BigDecimal getR32_risk_weighted_amount() {
		return r32_risk_weighted_amount;
	}




	public void setR32_risk_weighted_amount(BigDecimal r32_risk_weighted_amount) {
		this.r32_risk_weighted_amount = r32_risk_weighted_amount;
	}




	public String getR33_security_firm() {
		return r33_security_firm;
	}




	public void setR33_security_firm(String r33_security_firm) {
		this.r33_security_firm = r33_security_firm;
	}




	public BigDecimal getR33_credit_rating() {
		return r33_credit_rating;
	}




	public void setR33_credit_rating(BigDecimal r33_credit_rating) {
		this.r33_credit_rating = r33_credit_rating;
	}




	public String getR33_rating_agency() {
		return r33_rating_agency;
	}




	public void setR33_rating_agency(String r33_rating_agency) {
		this.r33_rating_agency = r33_rating_agency;
	}




	public BigDecimal getR33_exposure_amount() {
		return r33_exposure_amount;
	}




	public void setR33_exposure_amount(BigDecimal r33_exposure_amount) {
		this.r33_exposure_amount = r33_exposure_amount;
	}




	public BigDecimal getR33_risk_weight() {
		return r33_risk_weight;
	}




	public void setR33_risk_weight(BigDecimal r33_risk_weight) {
		this.r33_risk_weight = r33_risk_weight;
	}




	public BigDecimal getR33_risk_weighted_amount() {
		return r33_risk_weighted_amount;
	}




	public void setR33_risk_weighted_amount(BigDecimal r33_risk_weighted_amount) {
		this.r33_risk_weighted_amount = r33_risk_weighted_amount;
	}




	public String getR34_security_firm() {
		return r34_security_firm;
	}




	public void setR34_security_firm(String r34_security_firm) {
		this.r34_security_firm = r34_security_firm;
	}




	public BigDecimal getR34_credit_rating() {
		return r34_credit_rating;
	}




	public void setR34_credit_rating(BigDecimal r34_credit_rating) {
		this.r34_credit_rating = r34_credit_rating;
	}




	public String getR34_rating_agency() {
		return r34_rating_agency;
	}




	public void setR34_rating_agency(String r34_rating_agency) {
		this.r34_rating_agency = r34_rating_agency;
	}




	public BigDecimal getR34_exposure_amount() {
		return r34_exposure_amount;
	}




	public void setR34_exposure_amount(BigDecimal r34_exposure_amount) {
		this.r34_exposure_amount = r34_exposure_amount;
	}




	public BigDecimal getR34_risk_weight() {
		return r34_risk_weight;
	}




	public void setR34_risk_weight(BigDecimal r34_risk_weight) {
		this.r34_risk_weight = r34_risk_weight;
	}




	public BigDecimal getR34_risk_weighted_amount() {
		return r34_risk_weighted_amount;
	}




	public void setR34_risk_weighted_amount(BigDecimal r34_risk_weighted_amount) {
		this.r34_risk_weighted_amount = r34_risk_weighted_amount;
	}




	public String getR35_security_firm() {
		return r35_security_firm;
	}




	public void setR35_security_firm(String r35_security_firm) {
		this.r35_security_firm = r35_security_firm;
	}




	public BigDecimal getR35_credit_rating() {
		return r35_credit_rating;
	}




	public void setR35_credit_rating(BigDecimal r35_credit_rating) {
		this.r35_credit_rating = r35_credit_rating;
	}




	public String getR35_rating_agency() {
		return r35_rating_agency;
	}




	public void setR35_rating_agency(String r35_rating_agency) {
		this.r35_rating_agency = r35_rating_agency;
	}




	public BigDecimal getR35_exposure_amount() {
		return r35_exposure_amount;
	}




	public void setR35_exposure_amount(BigDecimal r35_exposure_amount) {
		this.r35_exposure_amount = r35_exposure_amount;
	}




	public BigDecimal getR35_risk_weight() {
		return r35_risk_weight;
	}




	public void setR35_risk_weight(BigDecimal r35_risk_weight) {
		this.r35_risk_weight = r35_risk_weight;
	}




	public BigDecimal getR35_risk_weighted_amount() {
		return r35_risk_weighted_amount;
	}




	public void setR35_risk_weighted_amount(BigDecimal r35_risk_weighted_amount) {
		this.r35_risk_weighted_amount = r35_risk_weighted_amount;
	}




	public String getR36_security_firm() {
		return r36_security_firm;
	}




	public void setR36_security_firm(String r36_security_firm) {
		this.r36_security_firm = r36_security_firm;
	}




	public BigDecimal getR36_credit_rating() {
		return r36_credit_rating;
	}




	public void setR36_credit_rating(BigDecimal r36_credit_rating) {
		this.r36_credit_rating = r36_credit_rating;
	}




	public String getR36_rating_agency() {
		return r36_rating_agency;
	}




	public void setR36_rating_agency(String r36_rating_agency) {
		this.r36_rating_agency = r36_rating_agency;
	}




	public BigDecimal getR36_exposure_amount() {
		return r36_exposure_amount;
	}




	public void setR36_exposure_amount(BigDecimal r36_exposure_amount) {
		this.r36_exposure_amount = r36_exposure_amount;
	}




	public BigDecimal getR36_risk_weight() {
		return r36_risk_weight;
	}




	public void setR36_risk_weight(BigDecimal r36_risk_weight) {
		this.r36_risk_weight = r36_risk_weight;
	}




	public BigDecimal getR36_risk_weighted_amount() {
		return r36_risk_weighted_amount;
	}




	public void setR36_risk_weighted_amount(BigDecimal r36_risk_weighted_amount) {
		this.r36_risk_weighted_amount = r36_risk_weighted_amount;
	}




	public String getR37_security_firm() {
		return r37_security_firm;
	}




	public void setR37_security_firm(String r37_security_firm) {
		this.r37_security_firm = r37_security_firm;
	}




	public BigDecimal getR37_credit_rating() {
		return r37_credit_rating;
	}




	public void setR37_credit_rating(BigDecimal r37_credit_rating) {
		this.r37_credit_rating = r37_credit_rating;
	}




	public String getR37_rating_agency() {
		return r37_rating_agency;
	}




	public void setR37_rating_agency(String r37_rating_agency) {
		this.r37_rating_agency = r37_rating_agency;
	}




	public BigDecimal getR37_exposure_amount() {
		return r37_exposure_amount;
	}




	public void setR37_exposure_amount(BigDecimal r37_exposure_amount) {
		this.r37_exposure_amount = r37_exposure_amount;
	}




	public BigDecimal getR37_risk_weight() {
		return r37_risk_weight;
	}




	public void setR37_risk_weight(BigDecimal r37_risk_weight) {
		this.r37_risk_weight = r37_risk_weight;
	}




	public BigDecimal getR37_risk_weighted_amount() {
		return r37_risk_weighted_amount;
	}




	public void setR37_risk_weighted_amount(BigDecimal r37_risk_weighted_amount) {
		this.r37_risk_weighted_amount = r37_risk_weighted_amount;
	}




	public String getR38_security_firm() {
		return r38_security_firm;
	}




	public void setR38_security_firm(String r38_security_firm) {
		this.r38_security_firm = r38_security_firm;
	}




	public BigDecimal getR38_credit_rating() {
		return r38_credit_rating;
	}




	public void setR38_credit_rating(BigDecimal r38_credit_rating) {
		this.r38_credit_rating = r38_credit_rating;
	}




	public String getR38_rating_agency() {
		return r38_rating_agency;
	}




	public void setR38_rating_agency(String r38_rating_agency) {
		this.r38_rating_agency = r38_rating_agency;
	}




	public BigDecimal getR38_exposure_amount() {
		return r38_exposure_amount;
	}




	public void setR38_exposure_amount(BigDecimal r38_exposure_amount) {
		this.r38_exposure_amount = r38_exposure_amount;
	}




	public BigDecimal getR38_risk_weight() {
		return r38_risk_weight;
	}




	public void setR38_risk_weight(BigDecimal r38_risk_weight) {
		this.r38_risk_weight = r38_risk_weight;
	}




	public BigDecimal getR38_risk_weighted_amount() {
		return r38_risk_weighted_amount;
	}




	public void setR38_risk_weighted_amount(BigDecimal r38_risk_weighted_amount) {
		this.r38_risk_weighted_amount = r38_risk_weighted_amount;
	}




	public String getR39_security_firm() {
		return r39_security_firm;
	}




	public void setR39_security_firm(String r39_security_firm) {
		this.r39_security_firm = r39_security_firm;
	}




	public BigDecimal getR39_credit_rating() {
		return r39_credit_rating;
	}




	public void setR39_credit_rating(BigDecimal r39_credit_rating) {
		this.r39_credit_rating = r39_credit_rating;
	}




	public String getR39_rating_agency() {
		return r39_rating_agency;
	}




	public void setR39_rating_agency(String r39_rating_agency) {
		this.r39_rating_agency = r39_rating_agency;
	}




	public BigDecimal getR39_exposure_amount() {
		return r39_exposure_amount;
	}




	public void setR39_exposure_amount(BigDecimal r39_exposure_amount) {
		this.r39_exposure_amount = r39_exposure_amount;
	}




	public BigDecimal getR39_risk_weight() {
		return r39_risk_weight;
	}




	public void setR39_risk_weight(BigDecimal r39_risk_weight) {
		this.r39_risk_weight = r39_risk_weight;
	}




	public BigDecimal getR39_risk_weighted_amount() {
		return r39_risk_weighted_amount;
	}




	public void setR39_risk_weighted_amount(BigDecimal r39_risk_weighted_amount) {
		this.r39_risk_weighted_amount = r39_risk_weighted_amount;
	}




	public String getR40_security_firm() {
		return r40_security_firm;
	}




	public void setR40_security_firm(String r40_security_firm) {
		this.r40_security_firm = r40_security_firm;
	}




	public BigDecimal getR40_credit_rating() {
		return r40_credit_rating;
	}




	public void setR40_credit_rating(BigDecimal r40_credit_rating) {
		this.r40_credit_rating = r40_credit_rating;
	}




	public String getR40_rating_agency() {
		return r40_rating_agency;
	}




	public void setR40_rating_agency(String r40_rating_agency) {
		this.r40_rating_agency = r40_rating_agency;
	}




	public BigDecimal getR40_exposure_amount() {
		return r40_exposure_amount;
	}




	public void setR40_exposure_amount(BigDecimal r40_exposure_amount) {
		this.r40_exposure_amount = r40_exposure_amount;
	}




	public BigDecimal getR40_risk_weight() {
		return r40_risk_weight;
	}




	public void setR40_risk_weight(BigDecimal r40_risk_weight) {
		this.r40_risk_weight = r40_risk_weight;
	}




	public BigDecimal getR40_risk_weighted_amount() {
		return r40_risk_weighted_amount;
	}




	public void setR40_risk_weighted_amount(BigDecimal r40_risk_weighted_amount) {
		this.r40_risk_weighted_amount = r40_risk_weighted_amount;
	}




	public String getR41_security_firm() {
		return r41_security_firm;
	}




	public void setR41_security_firm(String r41_security_firm) {
		this.r41_security_firm = r41_security_firm;
	}




	public BigDecimal getR41_credit_rating() {
		return r41_credit_rating;
	}




	public void setR41_credit_rating(BigDecimal r41_credit_rating) {
		this.r41_credit_rating = r41_credit_rating;
	}




	public String getR41_rating_agency() {
		return r41_rating_agency;
	}




	public void setR41_rating_agency(String r41_rating_agency) {
		this.r41_rating_agency = r41_rating_agency;
	}




	public BigDecimal getR41_exposure_amount() {
		return r41_exposure_amount;
	}




	public void setR41_exposure_amount(BigDecimal r41_exposure_amount) {
		this.r41_exposure_amount = r41_exposure_amount;
	}




	public BigDecimal getR41_risk_weight() {
		return r41_risk_weight;
	}




	public void setR41_risk_weight(BigDecimal r41_risk_weight) {
		this.r41_risk_weight = r41_risk_weight;
	}




	public BigDecimal getR41_risk_weighted_amount() {
		return r41_risk_weighted_amount;
	}




	public void setR41_risk_weighted_amount(BigDecimal r41_risk_weighted_amount) {
		this.r41_risk_weighted_amount = r41_risk_weighted_amount;
	}




	public String getR42_security_firm() {
		return r42_security_firm;
	}




	public void setR42_security_firm(String r42_security_firm) {
		this.r42_security_firm = r42_security_firm;
	}




	public BigDecimal getR42_credit_rating() {
		return r42_credit_rating;
	}




	public void setR42_credit_rating(BigDecimal r42_credit_rating) {
		this.r42_credit_rating = r42_credit_rating;
	}




	public String getR42_rating_agency() {
		return r42_rating_agency;
	}




	public void setR42_rating_agency(String r42_rating_agency) {
		this.r42_rating_agency = r42_rating_agency;
	}




	public BigDecimal getR42_exposure_amount() {
		return r42_exposure_amount;
	}




	public void setR42_exposure_amount(BigDecimal r42_exposure_amount) {
		this.r42_exposure_amount = r42_exposure_amount;
	}




	public BigDecimal getR42_risk_weight() {
		return r42_risk_weight;
	}




	public void setR42_risk_weight(BigDecimal r42_risk_weight) {
		this.r42_risk_weight = r42_risk_weight;
	}




	public BigDecimal getR42_risk_weighted_amount() {
		return r42_risk_weighted_amount;
	}




	public void setR42_risk_weighted_amount(BigDecimal r42_risk_weighted_amount) {
		this.r42_risk_weighted_amount = r42_risk_weighted_amount;
	}




	public String getR43_security_firm() {
		return r43_security_firm;
	}




	public void setR43_security_firm(String r43_security_firm) {
		this.r43_security_firm = r43_security_firm;
	}




	public BigDecimal getR43_credit_rating() {
		return r43_credit_rating;
	}




	public void setR43_credit_rating(BigDecimal r43_credit_rating) {
		this.r43_credit_rating = r43_credit_rating;
	}




	public String getR43_rating_agency() {
		return r43_rating_agency;
	}




	public void setR43_rating_agency(String r43_rating_agency) {
		this.r43_rating_agency = r43_rating_agency;
	}




	public BigDecimal getR43_exposure_amount() {
		return r43_exposure_amount;
	}




	public void setR43_exposure_amount(BigDecimal r43_exposure_amount) {
		this.r43_exposure_amount = r43_exposure_amount;
	}




	public BigDecimal getR43_risk_weight() {
		return r43_risk_weight;
	}




	public void setR43_risk_weight(BigDecimal r43_risk_weight) {
		this.r43_risk_weight = r43_risk_weight;
	}




	public BigDecimal getR43_risk_weighted_amount() {
		return r43_risk_weighted_amount;
	}




	public void setR43_risk_weighted_amount(BigDecimal r43_risk_weighted_amount) {
		this.r43_risk_weighted_amount = r43_risk_weighted_amount;
	}




	public String getR44_security_firm() {
		return r44_security_firm;
	}




	public void setR44_security_firm(String r44_security_firm) {
		this.r44_security_firm = r44_security_firm;
	}




	public BigDecimal getR44_credit_rating() {
		return r44_credit_rating;
	}




	public void setR44_credit_rating(BigDecimal r44_credit_rating) {
		this.r44_credit_rating = r44_credit_rating;
	}




	public String getR44_rating_agency() {
		return r44_rating_agency;
	}




	public void setR44_rating_agency(String r44_rating_agency) {
		this.r44_rating_agency = r44_rating_agency;
	}




	public BigDecimal getR44_exposure_amount() {
		return r44_exposure_amount;
	}




	public void setR44_exposure_amount(BigDecimal r44_exposure_amount) {
		this.r44_exposure_amount = r44_exposure_amount;
	}




	public BigDecimal getR44_risk_weight() {
		return r44_risk_weight;
	}




	public void setR44_risk_weight(BigDecimal r44_risk_weight) {
		this.r44_risk_weight = r44_risk_weight;
	}




	public BigDecimal getR44_risk_weighted_amount() {
		return r44_risk_weighted_amount;
	}




	public void setR44_risk_weighted_amount(BigDecimal r44_risk_weighted_amount) {
		this.r44_risk_weighted_amount = r44_risk_weighted_amount;
	}




	public String getR45_security_firm() {
		return r45_security_firm;
	}




	public void setR45_security_firm(String r45_security_firm) {
		this.r45_security_firm = r45_security_firm;
	}




	public BigDecimal getR45_credit_rating() {
		return r45_credit_rating;
	}




	public void setR45_credit_rating(BigDecimal r45_credit_rating) {
		this.r45_credit_rating = r45_credit_rating;
	}




	public String getR45_rating_agency() {
		return r45_rating_agency;
	}




	public void setR45_rating_agency(String r45_rating_agency) {
		this.r45_rating_agency = r45_rating_agency;
	}




	public BigDecimal getR45_exposure_amount() {
		return r45_exposure_amount;
	}




	public void setR45_exposure_amount(BigDecimal r45_exposure_amount) {
		this.r45_exposure_amount = r45_exposure_amount;
	}




	public BigDecimal getR45_risk_weight() {
		return r45_risk_weight;
	}




	public void setR45_risk_weight(BigDecimal r45_risk_weight) {
		this.r45_risk_weight = r45_risk_weight;
	}




	public BigDecimal getR45_risk_weighted_amount() {
		return r45_risk_weighted_amount;
	}




	public void setR45_risk_weighted_amount(BigDecimal r45_risk_weighted_amount) {
		this.r45_risk_weighted_amount = r45_risk_weighted_amount;
	}




	public String getR46_security_firm() {
		return r46_security_firm;
	}




	public void setR46_security_firm(String r46_security_firm) {
		this.r46_security_firm = r46_security_firm;
	}




	public BigDecimal getR46_credit_rating() {
		return r46_credit_rating;
	}




	public void setR46_credit_rating(BigDecimal r46_credit_rating) {
		this.r46_credit_rating = r46_credit_rating;
	}




	public String getR46_rating_agency() {
		return r46_rating_agency;
	}




	public void setR46_rating_agency(String r46_rating_agency) {
		this.r46_rating_agency = r46_rating_agency;
	}




	public BigDecimal getR46_exposure_amount() {
		return r46_exposure_amount;
	}




	public void setR46_exposure_amount(BigDecimal r46_exposure_amount) {
		this.r46_exposure_amount = r46_exposure_amount;
	}




	public BigDecimal getR46_risk_weight() {
		return r46_risk_weight;
	}




	public void setR46_risk_weight(BigDecimal r46_risk_weight) {
		this.r46_risk_weight = r46_risk_weight;
	}




	public BigDecimal getR46_risk_weighted_amount() {
		return r46_risk_weighted_amount;
	}




	public void setR46_risk_weighted_amount(BigDecimal r46_risk_weighted_amount) {
		this.r46_risk_weighted_amount = r46_risk_weighted_amount;
	}




	public String getR47_security_firm() {
		return r47_security_firm;
	}




	public void setR47_security_firm(String r47_security_firm) {
		this.r47_security_firm = r47_security_firm;
	}




	public BigDecimal getR47_credit_rating() {
		return r47_credit_rating;
	}




	public void setR47_credit_rating(BigDecimal r47_credit_rating) {
		this.r47_credit_rating = r47_credit_rating;
	}




	public String getR47_rating_agency() {
		return r47_rating_agency;
	}




	public void setR47_rating_agency(String r47_rating_agency) {
		this.r47_rating_agency = r47_rating_agency;
	}




	public BigDecimal getR47_exposure_amount() {
		return r47_exposure_amount;
	}




	public void setR47_exposure_amount(BigDecimal r47_exposure_amount) {
		this.r47_exposure_amount = r47_exposure_amount;
	}




	public BigDecimal getR47_risk_weight() {
		return r47_risk_weight;
	}




	public void setR47_risk_weight(BigDecimal r47_risk_weight) {
		this.r47_risk_weight = r47_risk_weight;
	}




	public BigDecimal getR47_risk_weighted_amount() {
		return r47_risk_weighted_amount;
	}




	public void setR47_risk_weighted_amount(BigDecimal r47_risk_weighted_amount) {
		this.r47_risk_weighted_amount = r47_risk_weighted_amount;
	}




	public String getR48_security_firm() {
		return r48_security_firm;
	}




	public void setR48_security_firm(String r48_security_firm) {
		this.r48_security_firm = r48_security_firm;
	}




	public BigDecimal getR48_credit_rating() {
		return r48_credit_rating;
	}




	public void setR48_credit_rating(BigDecimal r48_credit_rating) {
		this.r48_credit_rating = r48_credit_rating;
	}




	public String getR48_rating_agency() {
		return r48_rating_agency;
	}




	public void setR48_rating_agency(String r48_rating_agency) {
		this.r48_rating_agency = r48_rating_agency;
	}




	public BigDecimal getR48_exposure_amount() {
		return r48_exposure_amount;
	}




	public void setR48_exposure_amount(BigDecimal r48_exposure_amount) {
		this.r48_exposure_amount = r48_exposure_amount;
	}




	public BigDecimal getR48_risk_weight() {
		return r48_risk_weight;
	}




	public void setR48_risk_weight(BigDecimal r48_risk_weight) {
		this.r48_risk_weight = r48_risk_weight;
	}




	public BigDecimal getR48_risk_weighted_amount() {
		return r48_risk_weighted_amount;
	}




	public void setR48_risk_weighted_amount(BigDecimal r48_risk_weighted_amount) {
		this.r48_risk_weighted_amount = r48_risk_weighted_amount;
	}




	public String getR49_security_firm() {
		return r49_security_firm;
	}




	public void setR49_security_firm(String r49_security_firm) {
		this.r49_security_firm = r49_security_firm;
	}




	public BigDecimal getR49_credit_rating() {
		return r49_credit_rating;
	}




	public void setR49_credit_rating(BigDecimal r49_credit_rating) {
		this.r49_credit_rating = r49_credit_rating;
	}




	public String getR49_rating_agency() {
		return r49_rating_agency;
	}




	public void setR49_rating_agency(String r49_rating_agency) {
		this.r49_rating_agency = r49_rating_agency;
	}




	public BigDecimal getR49_exposure_amount() {
		return r49_exposure_amount;
	}




	public void setR49_exposure_amount(BigDecimal r49_exposure_amount) {
		this.r49_exposure_amount = r49_exposure_amount;
	}




	public BigDecimal getR49_risk_weight() {
		return r49_risk_weight;
	}




	public void setR49_risk_weight(BigDecimal r49_risk_weight) {
		this.r49_risk_weight = r49_risk_weight;
	}




	public BigDecimal getR49_risk_weighted_amount() {
		return r49_risk_weighted_amount;
	}




	public void setR49_risk_weighted_amount(BigDecimal r49_risk_weighted_amount) {
		this.r49_risk_weighted_amount = r49_risk_weighted_amount;
	}




	public String getR50_security_firm() {
		return r50_security_firm;
	}




	public void setR50_security_firm(String r50_security_firm) {
		this.r50_security_firm = r50_security_firm;
	}




	public BigDecimal getR50_credit_rating() {
		return r50_credit_rating;
	}




	public void setR50_credit_rating(BigDecimal r50_credit_rating) {
		this.r50_credit_rating = r50_credit_rating;
	}




	public String getR50_rating_agency() {
		return r50_rating_agency;
	}




	public void setR50_rating_agency(String r50_rating_agency) {
		this.r50_rating_agency = r50_rating_agency;
	}




	public BigDecimal getR50_exposure_amount() {
		return r50_exposure_amount;
	}




	public void setR50_exposure_amount(BigDecimal r50_exposure_amount) {
		this.r50_exposure_amount = r50_exposure_amount;
	}




	public BigDecimal getR50_risk_weight() {
		return r50_risk_weight;
	}




	public void setR50_risk_weight(BigDecimal r50_risk_weight) {
		this.r50_risk_weight = r50_risk_weight;
	}




	public BigDecimal getR50_risk_weighted_amount() {
		return r50_risk_weighted_amount;
	}




	public void setR50_risk_weighted_amount(BigDecimal r50_risk_weighted_amount) {
		this.r50_risk_weighted_amount = r50_risk_weighted_amount;
	}




	public String getR51_security_firm() {
		return r51_security_firm;
	}




	public void setR51_security_firm(String r51_security_firm) {
		this.r51_security_firm = r51_security_firm;
	}




	public BigDecimal getR51_credit_rating() {
		return r51_credit_rating;
	}




	public void setR51_credit_rating(BigDecimal r51_credit_rating) {
		this.r51_credit_rating = r51_credit_rating;
	}




	public String getR51_rating_agency() {
		return r51_rating_agency;
	}




	public void setR51_rating_agency(String r51_rating_agency) {
		this.r51_rating_agency = r51_rating_agency;
	}




	public BigDecimal getR51_exposure_amount() {
		return r51_exposure_amount;
	}




	public void setR51_exposure_amount(BigDecimal r51_exposure_amount) {
		this.r51_exposure_amount = r51_exposure_amount;
	}




	public BigDecimal getR51_risk_weight() {
		return r51_risk_weight;
	}




	public void setR51_risk_weight(BigDecimal r51_risk_weight) {
		this.r51_risk_weight = r51_risk_weight;
	}




	public BigDecimal getR51_risk_weighted_amount() {
		return r51_risk_weighted_amount;
	}




	public void setR51_risk_weighted_amount(BigDecimal r51_risk_weighted_amount) {
		this.r51_risk_weighted_amount = r51_risk_weighted_amount;
	}




	public String getR52_security_firm() {
		return r52_security_firm;
	}




	public void setR52_security_firm(String r52_security_firm) {
		this.r52_security_firm = r52_security_firm;
	}




	public BigDecimal getR52_credit_rating() {
		return r52_credit_rating;
	}




	public void setR52_credit_rating(BigDecimal r52_credit_rating) {
		this.r52_credit_rating = r52_credit_rating;
	}




	public String getR52_rating_agency() {
		return r52_rating_agency;
	}




	public void setR52_rating_agency(String r52_rating_agency) {
		this.r52_rating_agency = r52_rating_agency;
	}




	public BigDecimal getR52_exposure_amount() {
		return r52_exposure_amount;
	}




	public void setR52_exposure_amount(BigDecimal r52_exposure_amount) {
		this.r52_exposure_amount = r52_exposure_amount;
	}




	public BigDecimal getR52_risk_weight() {
		return r52_risk_weight;
	}




	public void setR52_risk_weight(BigDecimal r52_risk_weight) {
		this.r52_risk_weight = r52_risk_weight;
	}




	public BigDecimal getR52_risk_weighted_amount() {
		return r52_risk_weighted_amount;
	}




	public void setR52_risk_weighted_amount(BigDecimal r52_risk_weighted_amount) {
		this.r52_risk_weighted_amount = r52_risk_weighted_amount;
	}




	public String getR53_security_firm() {
		return r53_security_firm;
	}




	public void setR53_security_firm(String r53_security_firm) {
		this.r53_security_firm = r53_security_firm;
	}




	public BigDecimal getR53_credit_rating() {
		return r53_credit_rating;
	}




	public void setR53_credit_rating(BigDecimal r53_credit_rating) {
		this.r53_credit_rating = r53_credit_rating;
	}




	public String getR53_rating_agency() {
		return r53_rating_agency;
	}




	public void setR53_rating_agency(String r53_rating_agency) {
		this.r53_rating_agency = r53_rating_agency;
	}




	public BigDecimal getR53_exposure_amount() {
		return r53_exposure_amount;
	}




	public void setR53_exposure_amount(BigDecimal r53_exposure_amount) {
		this.r53_exposure_amount = r53_exposure_amount;
	}




	public BigDecimal getR53_risk_weight() {
		return r53_risk_weight;
	}




	public void setR53_risk_weight(BigDecimal r53_risk_weight) {
		this.r53_risk_weight = r53_risk_weight;
	}




	public BigDecimal getR53_risk_weighted_amount() {
		return r53_risk_weighted_amount;
	}




	public void setR53_risk_weighted_amount(BigDecimal r53_risk_weighted_amount) {
		this.r53_risk_weighted_amount = r53_risk_weighted_amount;
	}




	public String getR54_security_firm() {
		return r54_security_firm;
	}




	public void setR54_security_firm(String r54_security_firm) {
		this.r54_security_firm = r54_security_firm;
	}




	public BigDecimal getR54_credit_rating() {
		return r54_credit_rating;
	}




	public void setR54_credit_rating(BigDecimal r54_credit_rating) {
		this.r54_credit_rating = r54_credit_rating;
	}




	public String getR54_rating_agency() {
		return r54_rating_agency;
	}




	public void setR54_rating_agency(String r54_rating_agency) {
		this.r54_rating_agency = r54_rating_agency;
	}




	public BigDecimal getR54_exposure_amount() {
		return r54_exposure_amount;
	}




	public void setR54_exposure_amount(BigDecimal r54_exposure_amount) {
		this.r54_exposure_amount = r54_exposure_amount;
	}




	public BigDecimal getR54_risk_weight() {
		return r54_risk_weight;
	}




	public void setR54_risk_weight(BigDecimal r54_risk_weight) {
		this.r54_risk_weight = r54_risk_weight;
	}




	public BigDecimal getR54_risk_weighted_amount() {
		return r54_risk_weighted_amount;
	}




	public void setR54_risk_weighted_amount(BigDecimal r54_risk_weighted_amount) {
		this.r54_risk_weighted_amount = r54_risk_weighted_amount;
	}




	public String getR55_security_firm() {
		return r55_security_firm;
	}




	public void setR55_security_firm(String r55_security_firm) {
		this.r55_security_firm = r55_security_firm;
	}




	public BigDecimal getR55_credit_rating() {
		return r55_credit_rating;
	}




	public void setR55_credit_rating(BigDecimal r55_credit_rating) {
		this.r55_credit_rating = r55_credit_rating;
	}




	public String getR55_rating_agency() {
		return r55_rating_agency;
	}




	public void setR55_rating_agency(String r55_rating_agency) {
		this.r55_rating_agency = r55_rating_agency;
	}




	public BigDecimal getR55_exposure_amount() {
		return r55_exposure_amount;
	}




	public void setR55_exposure_amount(BigDecimal r55_exposure_amount) {
		this.r55_exposure_amount = r55_exposure_amount;
	}




	public BigDecimal getR55_risk_weight() {
		return r55_risk_weight;
	}




	public void setR55_risk_weight(BigDecimal r55_risk_weight) {
		this.r55_risk_weight = r55_risk_weight;
	}




	public BigDecimal getR55_risk_weighted_amount() {
		return r55_risk_weighted_amount;
	}




	public void setR55_risk_weighted_amount(BigDecimal r55_risk_weighted_amount) {
		this.r55_risk_weighted_amount = r55_risk_weighted_amount;
	}




	public String getR56_security_firm() {
		return r56_security_firm;
	}




	public void setR56_security_firm(String r56_security_firm) {
		this.r56_security_firm = r56_security_firm;
	}




	public BigDecimal getR56_credit_rating() {
		return r56_credit_rating;
	}




	public void setR56_credit_rating(BigDecimal r56_credit_rating) {
		this.r56_credit_rating = r56_credit_rating;
	}




	public String getR56_rating_agency() {
		return r56_rating_agency;
	}




	public void setR56_rating_agency(String r56_rating_agency) {
		this.r56_rating_agency = r56_rating_agency;
	}




	public BigDecimal getR56_exposure_amount() {
		return r56_exposure_amount;
	}




	public void setR56_exposure_amount(BigDecimal r56_exposure_amount) {
		this.r56_exposure_amount = r56_exposure_amount;
	}




	public BigDecimal getR56_risk_weight() {
		return r56_risk_weight;
	}




	public void setR56_risk_weight(BigDecimal r56_risk_weight) {
		this.r56_risk_weight = r56_risk_weight;
	}




	public BigDecimal getR56_risk_weighted_amount() {
		return r56_risk_weighted_amount;
	}




	public void setR56_risk_weighted_amount(BigDecimal r56_risk_weighted_amount) {
		this.r56_risk_weighted_amount = r56_risk_weighted_amount;
	}




	public String getR57_security_firm() {
		return r57_security_firm;
	}




	public void setR57_security_firm(String r57_security_firm) {
		this.r57_security_firm = r57_security_firm;
	}




	public BigDecimal getR57_credit_rating() {
		return r57_credit_rating;
	}




	public void setR57_credit_rating(BigDecimal r57_credit_rating) {
		this.r57_credit_rating = r57_credit_rating;
	}




	public String getR57_rating_agency() {
		return r57_rating_agency;
	}




	public void setR57_rating_agency(String r57_rating_agency) {
		this.r57_rating_agency = r57_rating_agency;
	}




	public BigDecimal getR57_exposure_amount() {
		return r57_exposure_amount;
	}




	public void setR57_exposure_amount(BigDecimal r57_exposure_amount) {
		this.r57_exposure_amount = r57_exposure_amount;
	}




	public BigDecimal getR57_risk_weight() {
		return r57_risk_weight;
	}




	public void setR57_risk_weight(BigDecimal r57_risk_weight) {
		this.r57_risk_weight = r57_risk_weight;
	}




	public BigDecimal getR57_risk_weighted_amount() {
		return r57_risk_weighted_amount;
	}




	public void setR57_risk_weighted_amount(BigDecimal r57_risk_weighted_amount) {
		this.r57_risk_weighted_amount = r57_risk_weighted_amount;
	}




	public String getR58_security_firm() {
		return r58_security_firm;
	}




	public void setR58_security_firm(String r58_security_firm) {
		this.r58_security_firm = r58_security_firm;
	}




	public BigDecimal getR58_credit_rating() {
		return r58_credit_rating;
	}




	public void setR58_credit_rating(BigDecimal r58_credit_rating) {
		this.r58_credit_rating = r58_credit_rating;
	}




	public String getR58_rating_agency() {
		return r58_rating_agency;
	}




	public void setR58_rating_agency(String r58_rating_agency) {
		this.r58_rating_agency = r58_rating_agency;
	}




	public BigDecimal getR58_exposure_amount() {
		return r58_exposure_amount;
	}




	public void setR58_exposure_amount(BigDecimal r58_exposure_amount) {
		this.r58_exposure_amount = r58_exposure_amount;
	}




	public BigDecimal getR58_risk_weight() {
		return r58_risk_weight;
	}




	public void setR58_risk_weight(BigDecimal r58_risk_weight) {
		this.r58_risk_weight = r58_risk_weight;
	}




	public BigDecimal getR58_risk_weighted_amount() {
		return r58_risk_weighted_amount;
	}




	public void setR58_risk_weighted_amount(BigDecimal r58_risk_weighted_amount) {
		this.r58_risk_weighted_amount = r58_risk_weighted_amount;
	}




	public String getR59_security_firm() {
		return r59_security_firm;
	}




	public void setR59_security_firm(String r59_security_firm) {
		this.r59_security_firm = r59_security_firm;
	}




	public BigDecimal getR59_credit_rating() {
		return r59_credit_rating;
	}




	public void setR59_credit_rating(BigDecimal r59_credit_rating) {
		this.r59_credit_rating = r59_credit_rating;
	}




	public String getR59_rating_agency() {
		return r59_rating_agency;
	}




	public void setR59_rating_agency(String r59_rating_agency) {
		this.r59_rating_agency = r59_rating_agency;
	}




	public BigDecimal getR59_exposure_amount() {
		return r59_exposure_amount;
	}




	public void setR59_exposure_amount(BigDecimal r59_exposure_amount) {
		this.r59_exposure_amount = r59_exposure_amount;
	}




	public BigDecimal getR59_risk_weight() {
		return r59_risk_weight;
	}




	public void setR59_risk_weight(BigDecimal r59_risk_weight) {
		this.r59_risk_weight = r59_risk_weight;
	}




	public BigDecimal getR59_risk_weighted_amount() {
		return r59_risk_weighted_amount;
	}




	public void setR59_risk_weighted_amount(BigDecimal r59_risk_weighted_amount) {
		this.r59_risk_weighted_amount = r59_risk_weighted_amount;
	}




	public String getR60_security_firm() {
		return r60_security_firm;
	}




	public void setR60_security_firm(String r60_security_firm) {
		this.r60_security_firm = r60_security_firm;
	}




	public BigDecimal getR60_credit_rating() {
		return r60_credit_rating;
	}




	public void setR60_credit_rating(BigDecimal r60_credit_rating) {
		this.r60_credit_rating = r60_credit_rating;
	}




	public String getR60_rating_agency() {
		return r60_rating_agency;
	}




	public void setR60_rating_agency(String r60_rating_agency) {
		this.r60_rating_agency = r60_rating_agency;
	}




	public BigDecimal getR60_exposure_amount() {
		return r60_exposure_amount;
	}




	public void setR60_exposure_amount(BigDecimal r60_exposure_amount) {
		this.r60_exposure_amount = r60_exposure_amount;
	}




	public BigDecimal getR60_risk_weight() {
		return r60_risk_weight;
	}




	public void setR60_risk_weight(BigDecimal r60_risk_weight) {
		this.r60_risk_weight = r60_risk_weight;
	}




	public BigDecimal getR60_risk_weighted_amount() {
		return r60_risk_weighted_amount;
	}




	public void setR60_risk_weighted_amount(BigDecimal r60_risk_weighted_amount) {
		this.r60_risk_weighted_amount = r60_risk_weighted_amount;
	}



	public BigDecimal getR61_exposure_amount() {
		return r61_exposure_amount;
	}




	public void setR61_exposure_amount(BigDecimal r61_exposure_amount) {
		this.r61_exposure_amount = r61_exposure_amount;
	}





	public BigDecimal getR61_risk_weighted_amount() {
		return r61_risk_weighted_amount;
	}




	public void setR61_risk_weighted_amount(BigDecimal r61_risk_weighted_amount) {
		this.r61_risk_weighted_amount = r61_risk_weighted_amount;
	}




	public M_SRWA_12G_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}

	
}