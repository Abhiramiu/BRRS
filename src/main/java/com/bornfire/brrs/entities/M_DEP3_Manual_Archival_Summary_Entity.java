

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
@Table(name = "BRRS_M_DEP3_ARCHIVAL_MANUAL_SUMMARYTABLE")
public class M_DEP3_Manual_Archival_Summary_Entity {

@Id
@Temporal(TemporalType.DATE)
@DateTimeFormat(pattern = "dd/MM/yyyy")
private Date report_date;
private String report_version;
private String report_frequency;
private String report_code;
private String report_desc;
private String entity_flg;
private String modify_flg;
private String del_flg;


	
	private BigDecimal	r11_ex_rate_buy;
	private BigDecimal	r11_ex_rate_mid;
	private BigDecimal	r11_ex_rate_sell;

	private BigDecimal	r11_notice_0to31;
	private BigDecimal	r11_notice_32to88;

	private BigDecimal	r11_cer_of_depo;

	private BigDecimal	r12_ex_rate_buy;
	private BigDecimal	r12_ex_rate_mid;
	private BigDecimal	r12_ex_rate_sell;

	private BigDecimal	r12_notice_0to31;
	private BigDecimal	r12_notice_32to88;

	private BigDecimal	r12_cer_of_depo;
	
	private BigDecimal	r13_ex_rate_buy;
	private BigDecimal	r13_ex_rate_mid;
	private BigDecimal	r13_ex_rate_sell;

	private BigDecimal	r13_notice_0to31;
	private BigDecimal	r13_notice_32to88;

	private BigDecimal	r13_cer_of_depo;
	
	private BigDecimal	r14_ex_rate_buy;
	private BigDecimal	r14_ex_rate_mid;
	private BigDecimal	r14_ex_rate_sell;

	private BigDecimal	r14_notice_0to31;
	private BigDecimal	r14_notice_32to88;

	private BigDecimal	r14_cer_of_depo;
	
	private BigDecimal	r15_ex_rate_buy;
	private BigDecimal	r15_ex_rate_mid;
	private BigDecimal	r15_ex_rate_sell;

	private BigDecimal	r15_notice_0to31;
	private BigDecimal	r15_notice_32to88;

	private BigDecimal	r15_cer_of_depo;
	
	private BigDecimal	r16_ex_rate_buy;
	private BigDecimal	r16_ex_rate_mid;
	private BigDecimal	r16_ex_rate_sell;

	private BigDecimal	r16_notice_0to31;
	private BigDecimal	r16_notice_32to88;

	private BigDecimal	r16_cer_of_depo;
	

	private BigDecimal	r18_notice_0to31;
	private BigDecimal	r18_notice_32to88;

	private BigDecimal	r18_cer_of_depo;

	
private BigDecimal	r28_import;
private BigDecimal	r28_investment;
private BigDecimal	r28_other;


private BigDecimal	r29_import;
private BigDecimal	r29_investment;
private BigDecimal	r29_other;



private BigDecimal	r30_import;
private BigDecimal	r30_investment;
private BigDecimal	r30_other;


private BigDecimal	r31_import;
private BigDecimal	r31_investment;
private BigDecimal	r31_other;


private BigDecimal	r32_import;
private BigDecimal	r32_investment;
private BigDecimal	r32_other;


private BigDecimal	r33_import;
private BigDecimal	r33_investment;
private BigDecimal	r33_other;


private BigDecimal	r34_import;
private BigDecimal	r34_investment;
private BigDecimal	r34_other;


private BigDecimal	r28_residents;
private BigDecimal	r28_non_residents;


private BigDecimal	r29_residents;
private BigDecimal	r29_non_residents;


private BigDecimal	r30_residents;
private BigDecimal	r30_non_residents;


private BigDecimal	r31_residents;
private BigDecimal	r31_non_residents;


private BigDecimal	r32_residents;
private BigDecimal	r32_non_residents;


private BigDecimal	r33_residents;
private BigDecimal	r33_non_residents;


private BigDecimal	r34_residents;
private BigDecimal	r34_non_residents;
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
public BigDecimal getR11_ex_rate_buy() {
	return r11_ex_rate_buy;
}
public void setR11_ex_rate_buy(BigDecimal r11_ex_rate_buy) {
	this.r11_ex_rate_buy = r11_ex_rate_buy;
}
public BigDecimal getR11_ex_rate_sell() {
	return r11_ex_rate_sell;
}
public void setR11_ex_rate_sell(BigDecimal r11_ex_rate_sell) {
	this.r11_ex_rate_sell = r11_ex_rate_sell;
}
public BigDecimal getR11_notice_0to31() {
	return r11_notice_0to31;
}
public void setR11_notice_0to31(BigDecimal r11_notice_0to31) {
	this.r11_notice_0to31 = r11_notice_0to31;
}
public BigDecimal getR11_notice_32to88() {
	return r11_notice_32to88;
}
public void setR11_notice_32to88(BigDecimal r11_notice_32to88) {
	this.r11_notice_32to88 = r11_notice_32to88;
}
public BigDecimal getR11_cer_of_depo() {
	return r11_cer_of_depo;
}
public void setR11_cer_of_depo(BigDecimal r11_cer_of_depo) {
	this.r11_cer_of_depo = r11_cer_of_depo;
}
public BigDecimal getR12_ex_rate_buy() {
	return r12_ex_rate_buy;
}
public void setR12_ex_rate_buy(BigDecimal r12_ex_rate_buy) {
	this.r12_ex_rate_buy = r12_ex_rate_buy;
}
public BigDecimal getR12_ex_rate_sell() {
	return r12_ex_rate_sell;
}
public void setR12_ex_rate_sell(BigDecimal r12_ex_rate_sell) {
	this.r12_ex_rate_sell = r12_ex_rate_sell;
}
public BigDecimal getR12_notice_0to31() {
	return r12_notice_0to31;
}
public void setR12_notice_0to31(BigDecimal r12_notice_0to31) {
	this.r12_notice_0to31 = r12_notice_0to31;
}
public BigDecimal getR12_notice_32to88() {
	return r12_notice_32to88;
}
public void setR12_notice_32to88(BigDecimal r12_notice_32to88) {
	this.r12_notice_32to88 = r12_notice_32to88;
}
public BigDecimal getR12_cer_of_depo() {
	return r12_cer_of_depo;
}
public void setR12_cer_of_depo(BigDecimal r12_cer_of_depo) {
	this.r12_cer_of_depo = r12_cer_of_depo;
}
public BigDecimal getR13_ex_rate_buy() {
	return r13_ex_rate_buy;
}
public void setR13_ex_rate_buy(BigDecimal r13_ex_rate_buy) {
	this.r13_ex_rate_buy = r13_ex_rate_buy;
}
public BigDecimal getR13_ex_rate_sell() {
	return r13_ex_rate_sell;
}
public void setR13_ex_rate_sell(BigDecimal r13_ex_rate_sell) {
	this.r13_ex_rate_sell = r13_ex_rate_sell;
}
public BigDecimal getR13_notice_0to31() {
	return r13_notice_0to31;
}
public void setR13_notice_0to31(BigDecimal r13_notice_0to31) {
	this.r13_notice_0to31 = r13_notice_0to31;
}
public BigDecimal getR13_notice_32to88() {
	return r13_notice_32to88;
}
public void setR13_notice_32to88(BigDecimal r13_notice_32to88) {
	this.r13_notice_32to88 = r13_notice_32to88;
}
public BigDecimal getR13_cer_of_depo() {
	return r13_cer_of_depo;
}
public void setR13_cer_of_depo(BigDecimal r13_cer_of_depo) {
	this.r13_cer_of_depo = r13_cer_of_depo;
}
public BigDecimal getR14_ex_rate_buy() {
	return r14_ex_rate_buy;
}
public void setR14_ex_rate_buy(BigDecimal r14_ex_rate_buy) {
	this.r14_ex_rate_buy = r14_ex_rate_buy;
}
public BigDecimal getR14_ex_rate_sell() {
	return r14_ex_rate_sell;
}
public void setR14_ex_rate_sell(BigDecimal r14_ex_rate_sell) {
	this.r14_ex_rate_sell = r14_ex_rate_sell;
}
public BigDecimal getR14_notice_0to31() {
	return r14_notice_0to31;
}
public void setR14_notice_0to31(BigDecimal r14_notice_0to31) {
	this.r14_notice_0to31 = r14_notice_0to31;
}
public BigDecimal getR14_notice_32to88() {
	return r14_notice_32to88;
}
public void setR14_notice_32to88(BigDecimal r14_notice_32to88) {
	this.r14_notice_32to88 = r14_notice_32to88;
}
public BigDecimal getR14_cer_of_depo() {
	return r14_cer_of_depo;
}
public void setR14_cer_of_depo(BigDecimal r14_cer_of_depo) {
	this.r14_cer_of_depo = r14_cer_of_depo;
}
public BigDecimal getR15_ex_rate_buy() {
	return r15_ex_rate_buy;
}
public void setR15_ex_rate_buy(BigDecimal r15_ex_rate_buy) {
	this.r15_ex_rate_buy = r15_ex_rate_buy;
}
public BigDecimal getR15_ex_rate_sell() {
	return r15_ex_rate_sell;
}
public void setR15_ex_rate_sell(BigDecimal r15_ex_rate_sell) {
	this.r15_ex_rate_sell = r15_ex_rate_sell;
}
public BigDecimal getR15_notice_0to31() {
	return r15_notice_0to31;
}
public void setR15_notice_0to31(BigDecimal r15_notice_0to31) {
	this.r15_notice_0to31 = r15_notice_0to31;
}
public BigDecimal getR15_notice_32to88() {
	return r15_notice_32to88;
}
public void setR15_notice_32to88(BigDecimal r15_notice_32to88) {
	this.r15_notice_32to88 = r15_notice_32to88;
}
public BigDecimal getR15_cer_of_depo() {
	return r15_cer_of_depo;
}
public void setR15_cer_of_depo(BigDecimal r15_cer_of_depo) {
	this.r15_cer_of_depo = r15_cer_of_depo;
}
public BigDecimal getR16_ex_rate_buy() {
	return r16_ex_rate_buy;
}
public void setR16_ex_rate_buy(BigDecimal r16_ex_rate_buy) {
	this.r16_ex_rate_buy = r16_ex_rate_buy;
}
public BigDecimal getR16_ex_rate_sell() {
	return r16_ex_rate_sell;
}
public void setR16_ex_rate_sell(BigDecimal r16_ex_rate_sell) {
	this.r16_ex_rate_sell = r16_ex_rate_sell;
}
public BigDecimal getR16_notice_0to31() {
	return r16_notice_0to31;
}
public void setR16_notice_0to31(BigDecimal r16_notice_0to31) {
	this.r16_notice_0to31 = r16_notice_0to31;
}
public BigDecimal getR16_notice_32to88() {
	return r16_notice_32to88;
}
public void setR16_notice_32to88(BigDecimal r16_notice_32to88) {
	this.r16_notice_32to88 = r16_notice_32to88;
}
public BigDecimal getR16_cer_of_depo() {
	return r16_cer_of_depo;
}
public void setR16_cer_of_depo(BigDecimal r16_cer_of_depo) {
	this.r16_cer_of_depo = r16_cer_of_depo;
}
public BigDecimal getR18_notice_0to31() {
	return r18_notice_0to31;
}
public void setR18_notice_0to31(BigDecimal r18_notice_0to31) {
	this.r18_notice_0to31 = r18_notice_0to31;
}
public BigDecimal getR18_notice_32to88() {
	return r18_notice_32to88;
}
public void setR18_notice_32to88(BigDecimal r18_notice_32to88) {
	this.r18_notice_32to88 = r18_notice_32to88;
}
public BigDecimal getR18_cer_of_depo() {
	return r18_cer_of_depo;
}
public void setR18_cer_of_depo(BigDecimal r18_cer_of_depo) {
	this.r18_cer_of_depo = r18_cer_of_depo;
}
public BigDecimal getR28_import() {
	return r28_import;
}
public void setR28_import(BigDecimal r28_import) {
	this.r28_import = r28_import;
}
public BigDecimal getR28_investment() {
	return r28_investment;
}
public void setR28_investment(BigDecimal r28_investment) {
	this.r28_investment = r28_investment;
}
public BigDecimal getR28_other() {
	return r28_other;
}
public void setR28_other(BigDecimal r28_other) {
	this.r28_other = r28_other;
}
public BigDecimal getR29_import() {
	return r29_import;
}
public void setR29_import(BigDecimal r29_import) {
	this.r29_import = r29_import;
}
public BigDecimal getR29_investment() {
	return r29_investment;
}
public void setR29_investment(BigDecimal r29_investment) {
	this.r29_investment = r29_investment;
}
public BigDecimal getR29_other() {
	return r29_other;
}
public void setR29_other(BigDecimal r29_other) {
	this.r29_other = r29_other;
}
public BigDecimal getR30_import() {
	return r30_import;
}
public void setR30_import(BigDecimal r30_import) {
	this.r30_import = r30_import;
}
public BigDecimal getR30_investment() {
	return r30_investment;
}
public void setR30_investment(BigDecimal r30_investment) {
	this.r30_investment = r30_investment;
}
public BigDecimal getR30_other() {
	return r30_other;
}
public void setR30_other(BigDecimal r30_other) {
	this.r30_other = r30_other;
}
public BigDecimal getR31_import() {
	return r31_import;
}
public void setR31_import(BigDecimal r31_import) {
	this.r31_import = r31_import;
}
public BigDecimal getR31_investment() {
	return r31_investment;
}
public void setR31_investment(BigDecimal r31_investment) {
	this.r31_investment = r31_investment;
}
public BigDecimal getR31_other() {
	return r31_other;
}
public void setR31_other(BigDecimal r31_other) {
	this.r31_other = r31_other;
}
public BigDecimal getR32_import() {
	return r32_import;
}
public void setR32_import(BigDecimal r32_import) {
	this.r32_import = r32_import;
}
public BigDecimal getR32_investment() {
	return r32_investment;
}
public void setR32_investment(BigDecimal r32_investment) {
	this.r32_investment = r32_investment;
}
public BigDecimal getR32_other() {
	return r32_other;
}
public void setR32_other(BigDecimal r32_other) {
	this.r32_other = r32_other;
}
public BigDecimal getR33_import() {
	return r33_import;
}
public void setR33_import(BigDecimal r33_import) {
	this.r33_import = r33_import;
}
public BigDecimal getR33_investment() {
	return r33_investment;
}
public void setR33_investment(BigDecimal r33_investment) {
	this.r33_investment = r33_investment;
}
public BigDecimal getR33_other() {
	return r33_other;
}
public void setR33_other(BigDecimal r33_other) {
	this.r33_other = r33_other;
}
public BigDecimal getR34_import() {
	return r34_import;
}
public void setR34_import(BigDecimal r34_import) {
	this.r34_import = r34_import;
}
public BigDecimal getR34_investment() {
	return r34_investment;
}
public void setR34_investment(BigDecimal r34_investment) {
	this.r34_investment = r34_investment;
}
public BigDecimal getR34_other() {
	return r34_other;
}
public void setR34_other(BigDecimal r34_other) {
	this.r34_other = r34_other;
}
public BigDecimal getR28_residents() {
	return r28_residents;
}
public void setR28_residents(BigDecimal r28_residents) {
	this.r28_residents = r28_residents;
}
public BigDecimal getR28_non_residents() {
	return r28_non_residents;
}
public void setR28_non_residents(BigDecimal r28_non_residents) {
	this.r28_non_residents = r28_non_residents;
}
public BigDecimal getR29_residents() {
	return r29_residents;
}
public void setR29_residents(BigDecimal r29_residents) {
	this.r29_residents = r29_residents;
}
public BigDecimal getR29_non_residents() {
	return r29_non_residents;
}
public void setR29_non_residents(BigDecimal r29_non_residents) {
	this.r29_non_residents = r29_non_residents;
}
public BigDecimal getR30_residents() {
	return r30_residents;
}
public void setR30_residents(BigDecimal r30_residents) {
	this.r30_residents = r30_residents;
}
public BigDecimal getR30_non_residents() {
	return r30_non_residents;
}
public void setR30_non_residents(BigDecimal r30_non_residents) {
	this.r30_non_residents = r30_non_residents;
}
public BigDecimal getR31_residents() {
	return r31_residents;
}
public void setR31_residents(BigDecimal r31_residents) {
	this.r31_residents = r31_residents;
}
public BigDecimal getR31_non_residents() {
	return r31_non_residents;
}
public void setR31_non_residents(BigDecimal r31_non_residents) {
	this.r31_non_residents = r31_non_residents;
}
public BigDecimal getR32_residents() {
	return r32_residents;
}
public void setR32_residents(BigDecimal r32_residents) {
	this.r32_residents = r32_residents;
}
public BigDecimal getR32_non_residents() {
	return r32_non_residents;
}
public void setR32_non_residents(BigDecimal r32_non_residents) {
	this.r32_non_residents = r32_non_residents;
}
public BigDecimal getR33_residents() {
	return r33_residents;
}
public void setR33_residents(BigDecimal r33_residents) {
	this.r33_residents = r33_residents;
}
public BigDecimal getR33_non_residents() {
	return r33_non_residents;
}
public void setR33_non_residents(BigDecimal r33_non_residents) {
	this.r33_non_residents = r33_non_residents;
}
public BigDecimal getR34_residents() {
	return r34_residents;
}
public void setR34_residents(BigDecimal r34_residents) {
	this.r34_residents = r34_residents;
}
public BigDecimal getR34_non_residents() {
	return r34_non_residents;
}
public void setR34_non_residents(BigDecimal r34_non_residents) {
	this.r34_non_residents = r34_non_residents;
}




public BigDecimal getR11_ex_rate_mid() {
	return r11_ex_rate_mid;
}
public void setR11_ex_rate_mid(BigDecimal r11_ex_rate_mid) {
	this.r11_ex_rate_mid = r11_ex_rate_mid;
}
public BigDecimal getR12_ex_rate_mid() {
	return r12_ex_rate_mid;
}
public void setR12_ex_rate_mid(BigDecimal r12_ex_rate_mid) {
	this.r12_ex_rate_mid = r12_ex_rate_mid;
}
public BigDecimal getR13_ex_rate_mid() {
	return r13_ex_rate_mid;
}
public void setR13_ex_rate_mid(BigDecimal r13_ex_rate_mid) {
	this.r13_ex_rate_mid = r13_ex_rate_mid;
}
public BigDecimal getR14_ex_rate_mid() {
	return r14_ex_rate_mid;
}
public void setR14_ex_rate_mid(BigDecimal r14_ex_rate_mid) {
	this.r14_ex_rate_mid = r14_ex_rate_mid;
}
public BigDecimal getR15_ex_rate_mid() {
	return r15_ex_rate_mid;
}
public void setR15_ex_rate_mid(BigDecimal r15_ex_rate_mid) {
	this.r15_ex_rate_mid = r15_ex_rate_mid;
}
public BigDecimal getR16_ex_rate_mid() {
	return r16_ex_rate_mid;
}
public void setR16_ex_rate_mid(BigDecimal r16_ex_rate_mid) {
	this.r16_ex_rate_mid = r16_ex_rate_mid;
}
public M_DEP3_Manual_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}




}