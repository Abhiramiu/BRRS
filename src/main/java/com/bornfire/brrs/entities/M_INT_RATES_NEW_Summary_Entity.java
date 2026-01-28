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
@Table(name = "BRRS_M_INT_RATES_NEW_SUMMARYTABLE")


public class M_INT_RATES_NEW_Summary_Entity {
	
	
	private String	r11_lending;
	private BigDecimal	r11_nominal_interest_rate;
	private BigDecimal	r11_avg_effective_rate;
	private String	r12_lending;
	private BigDecimal	r12_nominal_interest_rate;
	private BigDecimal	r12_avg_effective_rate;
	private String	r13_lending;
	private BigDecimal	r13_nominal_interest_rate;
	private BigDecimal	r13_avg_effective_rate;
	private String	r14_lending;
	private BigDecimal	r14_nominal_interest_rate;
	private BigDecimal	r14_avg_effective_rate;
	private String	r15_lending;
	private BigDecimal	r15_nominal_interest_rate;
	private BigDecimal	r15_avg_effective_rate;
	private String	r16_lending;
	private BigDecimal	r16_nominal_interest_rate;
	private BigDecimal	r16_avg_effective_rate;
	private String	r17_lending;
	private BigDecimal	r17_nominal_interest_rate;
	private BigDecimal	r17_avg_effective_rate;
	private String	r18_lending;
	private BigDecimal	r18_nominal_interest_rate;
	private BigDecimal	r18_avg_effective_rate;
	private String	r19_lending;
	private BigDecimal	r19_nominal_interest_rate;
	private BigDecimal	r19_avg_effective_rate;
	private String	r20_lending;
	private BigDecimal	r20_nominal_interest_rate;
	private BigDecimal	r20_avg_effective_rate;
	private String	r21_lending;
	private BigDecimal	r21_nominal_interest_rate;
	private BigDecimal	r21_avg_effective_rate;
	private String	r22_lending;
	private BigDecimal	r22_nominal_interest_rate;
	private BigDecimal	r22_avg_effective_rate;
	private String	r23_lending;
	private BigDecimal	r23_nominal_interest_rate;
	private BigDecimal	r23_avg_effective_rate;
	private String	r24_lending;
	private BigDecimal	r24_nominal_interest_rate;
	private BigDecimal	r24_avg_effective_rate;
	private String	r25_lending;
	private BigDecimal	r25_nominal_interest_rate;
	private BigDecimal	r25_avg_effective_rate;
	private String	r26_lending;
	private BigDecimal	r26_nominal_interest_rate;
	private BigDecimal	r26_avg_effective_rate;
	private String	r27_lending;
	private BigDecimal	r27_nominal_interest_rate;
	private BigDecimal	r27_avg_effective_rate;
	private String	r28_lending;
	private BigDecimal	r28_nominal_interest_rate;
	private BigDecimal	r28_avg_effective_rate;
	private String	r29_lending;
	private BigDecimal	r29_nominal_interest_rate;
	private BigDecimal	r29_avg_effective_rate;
	private String	r30_lending;
	private BigDecimal	r30_nominal_interest_rate;
	private BigDecimal	r30_avg_effective_rate;
	private String	r31_lending;
	private BigDecimal	r31_nominal_interest_rate;
	private BigDecimal	r31_avg_effective_rate;
	private String	r32_lending;
	private BigDecimal	r32_nominal_interest_rate;
	private BigDecimal	r32_avg_effective_rate;
	private String	r33_lending;
	private BigDecimal	r33_nominal_interest_rate;
	private BigDecimal	r33_avg_effective_rate;
	private String	r34_lending;
	private BigDecimal	r34_nominal_interest_rate;
	private BigDecimal	r34_avg_effective_rate;
	private String	r35_lending;
	private BigDecimal	r35_nominal_interest_rate;
	private BigDecimal	r35_avg_effective_rate;
	
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
	public String getR11_lending() {
		return r11_lending;
	}
	public void setR11_lending(String r11_lending) {
		this.r11_lending = r11_lending;
	}
	public BigDecimal getR11_nominal_interest_rate() {
		return r11_nominal_interest_rate;
	}
	public void setR11_nominal_interest_rate(BigDecimal r11_nominal_interest_rate) {
		this.r11_nominal_interest_rate = r11_nominal_interest_rate;
	}
	public BigDecimal getR11_avg_effective_rate() {
		return r11_avg_effective_rate;
	}
	public void setR11_avg_effective_rate(BigDecimal r11_avg_effective_rate) {
		this.r11_avg_effective_rate = r11_avg_effective_rate;
	}
	public String getR12_lending() {
		return r12_lending;
	}
	public void setR12_lending(String r12_lending) {
		this.r12_lending = r12_lending;
	}
	public BigDecimal getR12_nominal_interest_rate() {
		return r12_nominal_interest_rate;
	}
	public void setR12_nominal_interest_rate(BigDecimal r12_nominal_interest_rate) {
		this.r12_nominal_interest_rate = r12_nominal_interest_rate;
	}
	public BigDecimal getR12_avg_effective_rate() {
		return r12_avg_effective_rate;
	}
	public void setR12_avg_effective_rate(BigDecimal r12_avg_effective_rate) {
		this.r12_avg_effective_rate = r12_avg_effective_rate;
	}
	public String getR13_lending() {
		return r13_lending;
	}
	public void setR13_lending(String r13_lending) {
		this.r13_lending = r13_lending;
	}
	public BigDecimal getR13_nominal_interest_rate() {
		return r13_nominal_interest_rate;
	}
	public void setR13_nominal_interest_rate(BigDecimal r13_nominal_interest_rate) {
		this.r13_nominal_interest_rate = r13_nominal_interest_rate;
	}
	public BigDecimal getR13_avg_effective_rate() {
		return r13_avg_effective_rate;
	}
	public void setR13_avg_effective_rate(BigDecimal r13_avg_effective_rate) {
		this.r13_avg_effective_rate = r13_avg_effective_rate;
	}
	public String getR14_lending() {
		return r14_lending;
	}
	public void setR14_lending(String r14_lending) {
		this.r14_lending = r14_lending;
	}
	public BigDecimal getR14_nominal_interest_rate() {
		return r14_nominal_interest_rate;
	}
	public void setR14_nominal_interest_rate(BigDecimal r14_nominal_interest_rate) {
		this.r14_nominal_interest_rate = r14_nominal_interest_rate;
	}
	public BigDecimal getR14_avg_effective_rate() {
		return r14_avg_effective_rate;
	}
	public void setR14_avg_effective_rate(BigDecimal r14_avg_effective_rate) {
		this.r14_avg_effective_rate = r14_avg_effective_rate;
	}
	public String getR15_lending() {
		return r15_lending;
	}
	public void setR15_lending(String r15_lending) {
		this.r15_lending = r15_lending;
	}
	public BigDecimal getR15_nominal_interest_rate() {
		return r15_nominal_interest_rate;
	}
	public void setR15_nominal_interest_rate(BigDecimal r15_nominal_interest_rate) {
		this.r15_nominal_interest_rate = r15_nominal_interest_rate;
	}
	public BigDecimal getR15_avg_effective_rate() {
		return r15_avg_effective_rate;
	}
	public void setR15_avg_effective_rate(BigDecimal r15_avg_effective_rate) {
		this.r15_avg_effective_rate = r15_avg_effective_rate;
	}
	public String getR16_lending() {
		return r16_lending;
	}
	public void setR16_lending(String r16_lending) {
		this.r16_lending = r16_lending;
	}
	public BigDecimal getR16_nominal_interest_rate() {
		return r16_nominal_interest_rate;
	}
	public void setR16_nominal_interest_rate(BigDecimal r16_nominal_interest_rate) {
		this.r16_nominal_interest_rate = r16_nominal_interest_rate;
	}
	public BigDecimal getR16_avg_effective_rate() {
		return r16_avg_effective_rate;
	}
	public void setR16_avg_effective_rate(BigDecimal r16_avg_effective_rate) {
		this.r16_avg_effective_rate = r16_avg_effective_rate;
	}
	public String getR17_lending() {
		return r17_lending;
	}
	public void setR17_lending(String r17_lending) {
		this.r17_lending = r17_lending;
	}
	public BigDecimal getR17_nominal_interest_rate() {
		return r17_nominal_interest_rate;
	}
	public void setR17_nominal_interest_rate(BigDecimal r17_nominal_interest_rate) {
		this.r17_nominal_interest_rate = r17_nominal_interest_rate;
	}
	public BigDecimal getR17_avg_effective_rate() {
		return r17_avg_effective_rate;
	}
	public void setR17_avg_effective_rate(BigDecimal r17_avg_effective_rate) {
		this.r17_avg_effective_rate = r17_avg_effective_rate;
	}
	public String getR18_lending() {
		return r18_lending;
	}
	public void setR18_lending(String r18_lending) {
		this.r18_lending = r18_lending;
	}
	public BigDecimal getR18_nominal_interest_rate() {
		return r18_nominal_interest_rate;
	}
	public void setR18_nominal_interest_rate(BigDecimal r18_nominal_interest_rate) {
		this.r18_nominal_interest_rate = r18_nominal_interest_rate;
	}
	public BigDecimal getR18_avg_effective_rate() {
		return r18_avg_effective_rate;
	}
	public void setR18_avg_effective_rate(BigDecimal r18_avg_effective_rate) {
		this.r18_avg_effective_rate = r18_avg_effective_rate;
	}
	public String getR19_lending() {
		return r19_lending;
	}
	public void setR19_lending(String r19_lending) {
		this.r19_lending = r19_lending;
	}
	public BigDecimal getR19_nominal_interest_rate() {
		return r19_nominal_interest_rate;
	}
	public void setR19_nominal_interest_rate(BigDecimal r19_nominal_interest_rate) {
		this.r19_nominal_interest_rate = r19_nominal_interest_rate;
	}
	public BigDecimal getR19_avg_effective_rate() {
		return r19_avg_effective_rate;
	}
	public void setR19_avg_effective_rate(BigDecimal r19_avg_effective_rate) {
		this.r19_avg_effective_rate = r19_avg_effective_rate;
	}
	public String getR20_lending() {
		return r20_lending;
	}
	public void setR20_lending(String r20_lending) {
		this.r20_lending = r20_lending;
	}
	public BigDecimal getR20_nominal_interest_rate() {
		return r20_nominal_interest_rate;
	}
	public void setR20_nominal_interest_rate(BigDecimal r20_nominal_interest_rate) {
		this.r20_nominal_interest_rate = r20_nominal_interest_rate;
	}
	public BigDecimal getR20_avg_effective_rate() {
		return r20_avg_effective_rate;
	}
	public void setR20_avg_effective_rate(BigDecimal r20_avg_effective_rate) {
		this.r20_avg_effective_rate = r20_avg_effective_rate;
	}
	public String getR21_lending() {
		return r21_lending;
	}
	public void setR21_lending(String r21_lending) {
		this.r21_lending = r21_lending;
	}
	public BigDecimal getR21_nominal_interest_rate() {
		return r21_nominal_interest_rate;
	}
	public void setR21_nominal_interest_rate(BigDecimal r21_nominal_interest_rate) {
		this.r21_nominal_interest_rate = r21_nominal_interest_rate;
	}
	public BigDecimal getR21_avg_effective_rate() {
		return r21_avg_effective_rate;
	}
	public void setR21_avg_effective_rate(BigDecimal r21_avg_effective_rate) {
		this.r21_avg_effective_rate = r21_avg_effective_rate;
	}
	public String getR22_lending() {
		return r22_lending;
	}
	public void setR22_lending(String r22_lending) {
		this.r22_lending = r22_lending;
	}
	public BigDecimal getR22_nominal_interest_rate() {
		return r22_nominal_interest_rate;
	}
	public void setR22_nominal_interest_rate(BigDecimal r22_nominal_interest_rate) {
		this.r22_nominal_interest_rate = r22_nominal_interest_rate;
	}
	public BigDecimal getR22_avg_effective_rate() {
		return r22_avg_effective_rate;
	}
	public void setR22_avg_effective_rate(BigDecimal r22_avg_effective_rate) {
		this.r22_avg_effective_rate = r22_avg_effective_rate;
	}
	public String getR23_lending() {
		return r23_lending;
	}
	public void setR23_lending(String r23_lending) {
		this.r23_lending = r23_lending;
	}
	public BigDecimal getR23_nominal_interest_rate() {
		return r23_nominal_interest_rate;
	}
	public void setR23_nominal_interest_rate(BigDecimal r23_nominal_interest_rate) {
		this.r23_nominal_interest_rate = r23_nominal_interest_rate;
	}
	public BigDecimal getR23_avg_effective_rate() {
		return r23_avg_effective_rate;
	}
	public void setR23_avg_effective_rate(BigDecimal r23_avg_effective_rate) {
		this.r23_avg_effective_rate = r23_avg_effective_rate;
	}
	public String getR24_lending() {
		return r24_lending;
	}
	public void setR24_lending(String r24_lending) {
		this.r24_lending = r24_lending;
	}
	public BigDecimal getR24_nominal_interest_rate() {
		return r24_nominal_interest_rate;
	}
	public void setR24_nominal_interest_rate(BigDecimal r24_nominal_interest_rate) {
		this.r24_nominal_interest_rate = r24_nominal_interest_rate;
	}
	public BigDecimal getR24_avg_effective_rate() {
		return r24_avg_effective_rate;
	}
	public void setR24_avg_effective_rate(BigDecimal r24_avg_effective_rate) {
		this.r24_avg_effective_rate = r24_avg_effective_rate;
	}
	public String getR25_lending() {
		return r25_lending;
	}
	public void setR25_lending(String r25_lending) {
		this.r25_lending = r25_lending;
	}
	public BigDecimal getR25_nominal_interest_rate() {
		return r25_nominal_interest_rate;
	}
	public void setR25_nominal_interest_rate(BigDecimal r25_nominal_interest_rate) {
		this.r25_nominal_interest_rate = r25_nominal_interest_rate;
	}
	public BigDecimal getR25_avg_effective_rate() {
		return r25_avg_effective_rate;
	}
	public void setR25_avg_effective_rate(BigDecimal r25_avg_effective_rate) {
		this.r25_avg_effective_rate = r25_avg_effective_rate;
	}
	public String getR26_lending() {
		return r26_lending;
	}
	public void setR26_lending(String r26_lending) {
		this.r26_lending = r26_lending;
	}
	public BigDecimal getR26_nominal_interest_rate() {
		return r26_nominal_interest_rate;
	}
	public void setR26_nominal_interest_rate(BigDecimal r26_nominal_interest_rate) {
		this.r26_nominal_interest_rate = r26_nominal_interest_rate;
	}
	public BigDecimal getR26_avg_effective_rate() {
		return r26_avg_effective_rate;
	}
	public void setR26_avg_effective_rate(BigDecimal r26_avg_effective_rate) {
		this.r26_avg_effective_rate = r26_avg_effective_rate;
	}
	public String getR27_lending() {
		return r27_lending;
	}
	public void setR27_lending(String r27_lending) {
		this.r27_lending = r27_lending;
	}
	public BigDecimal getR27_nominal_interest_rate() {
		return r27_nominal_interest_rate;
	}
	public void setR27_nominal_interest_rate(BigDecimal r27_nominal_interest_rate) {
		this.r27_nominal_interest_rate = r27_nominal_interest_rate;
	}
	public BigDecimal getR27_avg_effective_rate() {
		return r27_avg_effective_rate;
	}
	public void setR27_avg_effective_rate(BigDecimal r27_avg_effective_rate) {
		this.r27_avg_effective_rate = r27_avg_effective_rate;
	}
	public String getR28_lending() {
		return r28_lending;
	}
	public void setR28_lending(String r28_lending) {
		this.r28_lending = r28_lending;
	}
	public BigDecimal getR28_nominal_interest_rate() {
		return r28_nominal_interest_rate;
	}
	public void setR28_nominal_interest_rate(BigDecimal r28_nominal_interest_rate) {
		this.r28_nominal_interest_rate = r28_nominal_interest_rate;
	}
	public BigDecimal getR28_avg_effective_rate() {
		return r28_avg_effective_rate;
	}
	public void setR28_avg_effective_rate(BigDecimal r28_avg_effective_rate) {
		this.r28_avg_effective_rate = r28_avg_effective_rate;
	}
	public String getR29_lending() {
		return r29_lending;
	}
	public void setR29_lending(String r29_lending) {
		this.r29_lending = r29_lending;
	}
	public BigDecimal getR29_nominal_interest_rate() {
		return r29_nominal_interest_rate;
	}
	public void setR29_nominal_interest_rate(BigDecimal r29_nominal_interest_rate) {
		this.r29_nominal_interest_rate = r29_nominal_interest_rate;
	}
	public BigDecimal getR29_avg_effective_rate() {
		return r29_avg_effective_rate;
	}
	public void setR29_avg_effective_rate(BigDecimal r29_avg_effective_rate) {
		this.r29_avg_effective_rate = r29_avg_effective_rate;
	}
	public String getR30_lending() {
		return r30_lending;
	}
	public void setR30_lending(String r30_lending) {
		this.r30_lending = r30_lending;
	}
	public BigDecimal getR30_nominal_interest_rate() {
		return r30_nominal_interest_rate;
	}
	public void setR30_nominal_interest_rate(BigDecimal r30_nominal_interest_rate) {
		this.r30_nominal_interest_rate = r30_nominal_interest_rate;
	}
	public BigDecimal getR30_avg_effective_rate() {
		return r30_avg_effective_rate;
	}
	public void setR30_avg_effective_rate(BigDecimal r30_avg_effective_rate) {
		this.r30_avg_effective_rate = r30_avg_effective_rate;
	}
	public String getR31_lending() {
		return r31_lending;
	}
	public void setR31_lending(String r31_lending) {
		this.r31_lending = r31_lending;
	}
	public BigDecimal getR31_nominal_interest_rate() {
		return r31_nominal_interest_rate;
	}
	public void setR31_nominal_interest_rate(BigDecimal r31_nominal_interest_rate) {
		this.r31_nominal_interest_rate = r31_nominal_interest_rate;
	}
	public BigDecimal getR31_avg_effective_rate() {
		return r31_avg_effective_rate;
	}
	public void setR31_avg_effective_rate(BigDecimal r31_avg_effective_rate) {
		this.r31_avg_effective_rate = r31_avg_effective_rate;
	}
	public String getR32_lending() {
		return r32_lending;
	}
	public void setR32_lending(String r32_lending) {
		this.r32_lending = r32_lending;
	}
	public BigDecimal getR32_nominal_interest_rate() {
		return r32_nominal_interest_rate;
	}
	public void setR32_nominal_interest_rate(BigDecimal r32_nominal_interest_rate) {
		this.r32_nominal_interest_rate = r32_nominal_interest_rate;
	}
	public BigDecimal getR32_avg_effective_rate() {
		return r32_avg_effective_rate;
	}
	public void setR32_avg_effective_rate(BigDecimal r32_avg_effective_rate) {
		this.r32_avg_effective_rate = r32_avg_effective_rate;
	}
	public String getR33_lending() {
		return r33_lending;
	}
	public void setR33_lending(String r33_lending) {
		this.r33_lending = r33_lending;
	}
	public BigDecimal getR33_nominal_interest_rate() {
		return r33_nominal_interest_rate;
	}
	public void setR33_nominal_interest_rate(BigDecimal r33_nominal_interest_rate) {
		this.r33_nominal_interest_rate = r33_nominal_interest_rate;
	}
	public BigDecimal getR33_avg_effective_rate() {
		return r33_avg_effective_rate;
	}
	public void setR33_avg_effective_rate(BigDecimal r33_avg_effective_rate) {
		this.r33_avg_effective_rate = r33_avg_effective_rate;
	}
	public String getR34_lending() {
		return r34_lending;
	}
	public void setR34_lending(String r34_lending) {
		this.r34_lending = r34_lending;
	}
	public BigDecimal getR34_nominal_interest_rate() {
		return r34_nominal_interest_rate;
	}
	public void setR34_nominal_interest_rate(BigDecimal r34_nominal_interest_rate) {
		this.r34_nominal_interest_rate = r34_nominal_interest_rate;
	}
	public BigDecimal getR34_avg_effective_rate() {
		return r34_avg_effective_rate;
	}
	public void setR34_avg_effective_rate(BigDecimal r34_avg_effective_rate) {
		this.r34_avg_effective_rate = r34_avg_effective_rate;
	}
	public String getR35_lending() {
		return r35_lending;
	}
	public void setR35_lending(String r35_lending) {
		this.r35_lending = r35_lending;
	}
	public BigDecimal getR35_nominal_interest_rate() {
		return r35_nominal_interest_rate;
	}
	public void setR35_nominal_interest_rate(BigDecimal r35_nominal_interest_rate) {
		this.r35_nominal_interest_rate = r35_nominal_interest_rate;
	}
	public BigDecimal getR35_avg_effective_rate() {
		return r35_avg_effective_rate;
	}
	public void setR35_avg_effective_rate(BigDecimal r35_avg_effective_rate) {
		this.r35_avg_effective_rate = r35_avg_effective_rate;
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
	public M_INT_RATES_NEW_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
