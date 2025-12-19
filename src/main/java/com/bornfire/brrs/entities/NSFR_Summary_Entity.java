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
@Table(name = "BRRS_NSFR_SUMMARYTABLE")
public class NSFR_Summary_Entity {


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

private BigDecimal R12_FACTOR_BOB;
private BigDecimal R12_TOTAL_AMOUNT_BOB;
private BigDecimal R12_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R13_FACTOR_BOB;
private BigDecimal R13_TOTAL_AMOUNT_BOB;
private BigDecimal R13_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R14_FACTOR_BOB;
private BigDecimal R14_TOTAL_AMOUNT_BOB;
private BigDecimal R14_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R15_FACTOR_BOB;
private BigDecimal R15_TOTAL_AMOUNT_BOB;
private BigDecimal R15_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R16_FACTOR_BOB;
private BigDecimal R16_TOTAL_AMOUNT_BOB;
private BigDecimal R16_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R17_FACTOR_BOB;
private BigDecimal R17_TOTAL_AMOUNT_BOB;
private BigDecimal R17_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R18_FACTOR_BOB;
private BigDecimal R18_TOTAL_AMOUNT_BOB;
private BigDecimal R18_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R19_FACTOR_BOB;
private BigDecimal R19_TOTAL_AMOUNT_BOB;
private BigDecimal R19_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R20_FACTOR_BOB;
private BigDecimal R20_TOTAL_AMOUNT_BOB;
private BigDecimal R20_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R21_FACTOR_BOB;
private BigDecimal R21_TOTAL_AMOUNT_BOB;
private BigDecimal R21_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R22_FACTOR_BOB;
private BigDecimal R22_TOTAL_AMOUNT_BOB;
private BigDecimal R22_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R23_FACTOR_BOB;
private BigDecimal R23_TOTAL_AMOUNT_BOB;
private BigDecimal R23_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R24_FACTOR_BOB;
private BigDecimal R24_TOTAL_AMOUNT_BOB;
private BigDecimal R24_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R25_FACTOR_BOB;
private BigDecimal R25_TOTAL_AMOUNT_BOB;
private BigDecimal R25_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R26_FACTOR_BOB;
private BigDecimal R26_TOTAL_AMOUNT_BOB;
private BigDecimal R26_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R27_FACTOR_BOB;
private BigDecimal R27_TOTAL_AMOUNT_BOB;
private BigDecimal R27_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R28_FACTOR_BOB;
private BigDecimal R28_TOTAL_AMOUNT_BOB;
private BigDecimal R28_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R29_FACTOR_BOB;
private BigDecimal R29_TOTAL_AMOUNT_BOB;
private BigDecimal R29_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R30_FACTOR_BOB;
private BigDecimal R30_TOTAL_AMOUNT_BOB;
private BigDecimal R30_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R31_FACTOR_BOB;
private BigDecimal R31_TOTAL_AMOUNT_BOB;
private BigDecimal R31_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R32_FACTOR_BOB;
private BigDecimal R32_TOTAL_AMOUNT_BOB;
private BigDecimal R32_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R33_FACTOR_BOB;
private BigDecimal R33_TOTAL_AMOUNT_BOB;
private BigDecimal R33_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R34_FACTOR_BOB;
private BigDecimal R34_TOTAL_AMOUNT_BOB;
private BigDecimal R34_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R35_FACTOR_BOB;
private BigDecimal R35_TOTAL_AMOUNT_BOB;
private BigDecimal R35_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R36_FACTOR_BOB;
private BigDecimal R36_TOTAL_AMOUNT_BOB;
private BigDecimal R36_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R37_FACTOR_BOB;
private BigDecimal R37_TOTAL_AMOUNT_BOB;
private BigDecimal R37_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R38_FACTOR_BOB;
private BigDecimal R38_TOTAL_AMOUNT_BOB;
private BigDecimal R38_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R39_FACTOR_BOB;
private BigDecimal R39_TOTAL_AMOUNT_BOB;
private BigDecimal R39_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R40_FACTOR_BOB;
private BigDecimal R40_TOTAL_AMOUNT_BOB;
private BigDecimal R40_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R41_FACTOR_BOB;
private BigDecimal R41_TOTAL_AMOUNT_BOB;
private BigDecimal R41_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R42_FACTOR_BOB;
private BigDecimal R42_TOTAL_AMOUNT_BOB;
private BigDecimal R42_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R43_FACTOR_BOB;
private BigDecimal R43_TOTAL_AMOUNT_BOB;
private BigDecimal R43_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R44_FACTOR_BOB;
private BigDecimal R44_TOTAL_AMOUNT_BOB;
private BigDecimal R44_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R45_FACTOR_BOB;
private BigDecimal R45_TOTAL_AMOUNT_BOB;
private BigDecimal R45_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R46_FACTOR_BOB;
private BigDecimal R46_TOTAL_AMOUNT_BOB;
private BigDecimal R46_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R47_FACTOR_BOB;
private BigDecimal R47_TOTAL_AMOUNT_BOB;
private BigDecimal R47_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R48_FACTOR_BOB;
private BigDecimal R48_TOTAL_AMOUNT_BOB;
private BigDecimal R48_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R49_FACTOR_BOB;
private BigDecimal R49_TOTAL_AMOUNT_BOB;
private BigDecimal R49_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R50_FACTOR_BOB;
private BigDecimal R50_TOTAL_AMOUNT_BOB;
private BigDecimal R50_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R51_FACTOR_BOB;
private BigDecimal R51_TOTAL_AMOUNT_BOB;
private BigDecimal R51_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R52_FACTOR_BOB;
private BigDecimal R52_TOTAL_AMOUNT_BOB;
private BigDecimal R52_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R53_FACTOR_BOB;
private BigDecimal R53_TOTAL_AMOUNT_BOB;
private BigDecimal R53_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R54_FACTOR_BOB;
private BigDecimal R54_TOTAL_AMOUNT_BOB;
private BigDecimal R54_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R55_FACTOR_BOB;
private BigDecimal R55_TOTAL_AMOUNT_BOB;
private BigDecimal R55_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R56_FACTOR_BOB;
private BigDecimal R56_TOTAL_AMOUNT_BOB;
private BigDecimal R56_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R57_FACTOR_BOB;
private BigDecimal R57_TOTAL_AMOUNT_BOB;
private BigDecimal R57_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R58_FACTOR_BOB;
private BigDecimal R58_TOTAL_AMOUNT_BOB;
private BigDecimal R58_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R59_FACTOR_BOB;
private BigDecimal R59_TOTAL_AMOUNT_BOB;
private BigDecimal R59_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R60_FACTOR_BOB;
private BigDecimal R60_TOTAL_AMOUNT_BOB;
private BigDecimal R60_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R61_FACTOR_BOB;
private BigDecimal R61_TOTAL_AMOUNT_BOB;
private BigDecimal R61_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R62_FACTOR_BOB;
private BigDecimal R62_TOTAL_AMOUNT_BOB;
private BigDecimal R62_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R63_FACTOR_BOB;
private BigDecimal R63_TOTAL_AMOUNT_BOB;
private BigDecimal R63_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R64_FACTOR_BOB;
private BigDecimal R64_TOTAL_AMOUNT_BOB;
private BigDecimal R64_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R65_FACTOR_BOB;
private BigDecimal R65_TOTAL_AMOUNT_BOB;
private BigDecimal R65_WITH_FACTOR_APPLIED_BOB;
private BigDecimal R66_FACTOR_BOB;
private BigDecimal R66_TOTAL_AMOUNT_BOB;
private BigDecimal R66_WITH_FACTOR_APPLIED_BOB;



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



public BigDecimal getR12_FACTOR_BOB() {
	return R12_FACTOR_BOB;
}



public void setR12_FACTOR_BOB(BigDecimal r12_FACTOR_BOB) {
	R12_FACTOR_BOB = r12_FACTOR_BOB;
}



public BigDecimal getR12_TOTAL_AMOUNT_BOB() {
	return R12_TOTAL_AMOUNT_BOB;
}



public void setR12_TOTAL_AMOUNT_BOB(BigDecimal r12_TOTAL_AMOUNT_BOB) {
	R12_TOTAL_AMOUNT_BOB = r12_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR12_WITH_FACTOR_APPLIED_BOB() {
	return R12_WITH_FACTOR_APPLIED_BOB;
}



public void setR12_WITH_FACTOR_APPLIED_BOB(BigDecimal r12_WITH_FACTOR_APPLIED_BOB) {
	R12_WITH_FACTOR_APPLIED_BOB = r12_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR13_FACTOR_BOB() {
	return R13_FACTOR_BOB;
}



public void setR13_FACTOR_BOB(BigDecimal r13_FACTOR_BOB) {
	R13_FACTOR_BOB = r13_FACTOR_BOB;
}



public BigDecimal getR13_TOTAL_AMOUNT_BOB() {
	return R13_TOTAL_AMOUNT_BOB;
}



public void setR13_TOTAL_AMOUNT_BOB(BigDecimal r13_TOTAL_AMOUNT_BOB) {
	R13_TOTAL_AMOUNT_BOB = r13_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR13_WITH_FACTOR_APPLIED_BOB() {
	return R13_WITH_FACTOR_APPLIED_BOB;
}



public void setR13_WITH_FACTOR_APPLIED_BOB(BigDecimal r13_WITH_FACTOR_APPLIED_BOB) {
	R13_WITH_FACTOR_APPLIED_BOB = r13_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR14_FACTOR_BOB() {
	return R14_FACTOR_BOB;
}



public void setR14_FACTOR_BOB(BigDecimal r14_FACTOR_BOB) {
	R14_FACTOR_BOB = r14_FACTOR_BOB;
}



public BigDecimal getR14_TOTAL_AMOUNT_BOB() {
	return R14_TOTAL_AMOUNT_BOB;
}



public void setR14_TOTAL_AMOUNT_BOB(BigDecimal r14_TOTAL_AMOUNT_BOB) {
	R14_TOTAL_AMOUNT_BOB = r14_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR14_WITH_FACTOR_APPLIED_BOB() {
	return R14_WITH_FACTOR_APPLIED_BOB;
}



public void setR14_WITH_FACTOR_APPLIED_BOB(BigDecimal r14_WITH_FACTOR_APPLIED_BOB) {
	R14_WITH_FACTOR_APPLIED_BOB = r14_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR15_FACTOR_BOB() {
	return R15_FACTOR_BOB;
}



public void setR15_FACTOR_BOB(BigDecimal r15_FACTOR_BOB) {
	R15_FACTOR_BOB = r15_FACTOR_BOB;
}



public BigDecimal getR15_TOTAL_AMOUNT_BOB() {
	return R15_TOTAL_AMOUNT_BOB;
}



public void setR15_TOTAL_AMOUNT_BOB(BigDecimal r15_TOTAL_AMOUNT_BOB) {
	R15_TOTAL_AMOUNT_BOB = r15_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR15_WITH_FACTOR_APPLIED_BOB() {
	return R15_WITH_FACTOR_APPLIED_BOB;
}



public void setR15_WITH_FACTOR_APPLIED_BOB(BigDecimal r15_WITH_FACTOR_APPLIED_BOB) {
	R15_WITH_FACTOR_APPLIED_BOB = r15_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR16_FACTOR_BOB() {
	return R16_FACTOR_BOB;
}



public void setR16_FACTOR_BOB(BigDecimal r16_FACTOR_BOB) {
	R16_FACTOR_BOB = r16_FACTOR_BOB;
}



public BigDecimal getR16_TOTAL_AMOUNT_BOB() {
	return R16_TOTAL_AMOUNT_BOB;
}



public void setR16_TOTAL_AMOUNT_BOB(BigDecimal r16_TOTAL_AMOUNT_BOB) {
	R16_TOTAL_AMOUNT_BOB = r16_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR16_WITH_FACTOR_APPLIED_BOB() {
	return R16_WITH_FACTOR_APPLIED_BOB;
}



public void setR16_WITH_FACTOR_APPLIED_BOB(BigDecimal r16_WITH_FACTOR_APPLIED_BOB) {
	R16_WITH_FACTOR_APPLIED_BOB = r16_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR17_FACTOR_BOB() {
	return R17_FACTOR_BOB;
}



public void setR17_FACTOR_BOB(BigDecimal r17_FACTOR_BOB) {
	R17_FACTOR_BOB = r17_FACTOR_BOB;
}



public BigDecimal getR17_TOTAL_AMOUNT_BOB() {
	return R17_TOTAL_AMOUNT_BOB;
}



public void setR17_TOTAL_AMOUNT_BOB(BigDecimal r17_TOTAL_AMOUNT_BOB) {
	R17_TOTAL_AMOUNT_BOB = r17_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR17_WITH_FACTOR_APPLIED_BOB() {
	return R17_WITH_FACTOR_APPLIED_BOB;
}



public void setR17_WITH_FACTOR_APPLIED_BOB(BigDecimal r17_WITH_FACTOR_APPLIED_BOB) {
	R17_WITH_FACTOR_APPLIED_BOB = r17_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR18_FACTOR_BOB() {
	return R18_FACTOR_BOB;
}



public void setR18_FACTOR_BOB(BigDecimal r18_FACTOR_BOB) {
	R18_FACTOR_BOB = r18_FACTOR_BOB;
}



public BigDecimal getR18_TOTAL_AMOUNT_BOB() {
	return R18_TOTAL_AMOUNT_BOB;
}



public void setR18_TOTAL_AMOUNT_BOB(BigDecimal r18_TOTAL_AMOUNT_BOB) {
	R18_TOTAL_AMOUNT_BOB = r18_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR18_WITH_FACTOR_APPLIED_BOB() {
	return R18_WITH_FACTOR_APPLIED_BOB;
}



public void setR18_WITH_FACTOR_APPLIED_BOB(BigDecimal r18_WITH_FACTOR_APPLIED_BOB) {
	R18_WITH_FACTOR_APPLIED_BOB = r18_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR19_FACTOR_BOB() {
	return R19_FACTOR_BOB;
}



public void setR19_FACTOR_BOB(BigDecimal r19_FACTOR_BOB) {
	R19_FACTOR_BOB = r19_FACTOR_BOB;
}



public BigDecimal getR19_TOTAL_AMOUNT_BOB() {
	return R19_TOTAL_AMOUNT_BOB;
}



public void setR19_TOTAL_AMOUNT_BOB(BigDecimal r19_TOTAL_AMOUNT_BOB) {
	R19_TOTAL_AMOUNT_BOB = r19_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR19_WITH_FACTOR_APPLIED_BOB() {
	return R19_WITH_FACTOR_APPLIED_BOB;
}



public void setR19_WITH_FACTOR_APPLIED_BOB(BigDecimal r19_WITH_FACTOR_APPLIED_BOB) {
	R19_WITH_FACTOR_APPLIED_BOB = r19_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR20_FACTOR_BOB() {
	return R20_FACTOR_BOB;
}



public void setR20_FACTOR_BOB(BigDecimal r20_FACTOR_BOB) {
	R20_FACTOR_BOB = r20_FACTOR_BOB;
}



public BigDecimal getR20_TOTAL_AMOUNT_BOB() {
	return R20_TOTAL_AMOUNT_BOB;
}



public void setR20_TOTAL_AMOUNT_BOB(BigDecimal r20_TOTAL_AMOUNT_BOB) {
	R20_TOTAL_AMOUNT_BOB = r20_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR20_WITH_FACTOR_APPLIED_BOB() {
	return R20_WITH_FACTOR_APPLIED_BOB;
}



public void setR20_WITH_FACTOR_APPLIED_BOB(BigDecimal r20_WITH_FACTOR_APPLIED_BOB) {
	R20_WITH_FACTOR_APPLIED_BOB = r20_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR21_FACTOR_BOB() {
	return R21_FACTOR_BOB;
}



public void setR21_FACTOR_BOB(BigDecimal r21_FACTOR_BOB) {
	R21_FACTOR_BOB = r21_FACTOR_BOB;
}



public BigDecimal getR21_TOTAL_AMOUNT_BOB() {
	return R21_TOTAL_AMOUNT_BOB;
}



public void setR21_TOTAL_AMOUNT_BOB(BigDecimal r21_TOTAL_AMOUNT_BOB) {
	R21_TOTAL_AMOUNT_BOB = r21_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR21_WITH_FACTOR_APPLIED_BOB() {
	return R21_WITH_FACTOR_APPLIED_BOB;
}



public void setR21_WITH_FACTOR_APPLIED_BOB(BigDecimal r21_WITH_FACTOR_APPLIED_BOB) {
	R21_WITH_FACTOR_APPLIED_BOB = r21_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR22_FACTOR_BOB() {
	return R22_FACTOR_BOB;
}



public void setR22_FACTOR_BOB(BigDecimal r22_FACTOR_BOB) {
	R22_FACTOR_BOB = r22_FACTOR_BOB;
}



public BigDecimal getR22_TOTAL_AMOUNT_BOB() {
	return R22_TOTAL_AMOUNT_BOB;
}



public void setR22_TOTAL_AMOUNT_BOB(BigDecimal r22_TOTAL_AMOUNT_BOB) {
	R22_TOTAL_AMOUNT_BOB = r22_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR22_WITH_FACTOR_APPLIED_BOB() {
	return R22_WITH_FACTOR_APPLIED_BOB;
}



public void setR22_WITH_FACTOR_APPLIED_BOB(BigDecimal r22_WITH_FACTOR_APPLIED_BOB) {
	R22_WITH_FACTOR_APPLIED_BOB = r22_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR23_FACTOR_BOB() {
	return R23_FACTOR_BOB;
}



public void setR23_FACTOR_BOB(BigDecimal r23_FACTOR_BOB) {
	R23_FACTOR_BOB = r23_FACTOR_BOB;
}



public BigDecimal getR23_TOTAL_AMOUNT_BOB() {
	return R23_TOTAL_AMOUNT_BOB;
}



public void setR23_TOTAL_AMOUNT_BOB(BigDecimal r23_TOTAL_AMOUNT_BOB) {
	R23_TOTAL_AMOUNT_BOB = r23_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR23_WITH_FACTOR_APPLIED_BOB() {
	return R23_WITH_FACTOR_APPLIED_BOB;
}



public void setR23_WITH_FACTOR_APPLIED_BOB(BigDecimal r23_WITH_FACTOR_APPLIED_BOB) {
	R23_WITH_FACTOR_APPLIED_BOB = r23_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR24_FACTOR_BOB() {
	return R24_FACTOR_BOB;
}



public void setR24_FACTOR_BOB(BigDecimal r24_FACTOR_BOB) {
	R24_FACTOR_BOB = r24_FACTOR_BOB;
}



public BigDecimal getR24_TOTAL_AMOUNT_BOB() {
	return R24_TOTAL_AMOUNT_BOB;
}



public void setR24_TOTAL_AMOUNT_BOB(BigDecimal r24_TOTAL_AMOUNT_BOB) {
	R24_TOTAL_AMOUNT_BOB = r24_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR24_WITH_FACTOR_APPLIED_BOB() {
	return R24_WITH_FACTOR_APPLIED_BOB;
}



public void setR24_WITH_FACTOR_APPLIED_BOB(BigDecimal r24_WITH_FACTOR_APPLIED_BOB) {
	R24_WITH_FACTOR_APPLIED_BOB = r24_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR25_FACTOR_BOB() {
	return R25_FACTOR_BOB;
}



public void setR25_FACTOR_BOB(BigDecimal r25_FACTOR_BOB) {
	R25_FACTOR_BOB = r25_FACTOR_BOB;
}



public BigDecimal getR25_TOTAL_AMOUNT_BOB() {
	return R25_TOTAL_AMOUNT_BOB;
}



public void setR25_TOTAL_AMOUNT_BOB(BigDecimal r25_TOTAL_AMOUNT_BOB) {
	R25_TOTAL_AMOUNT_BOB = r25_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR25_WITH_FACTOR_APPLIED_BOB() {
	return R25_WITH_FACTOR_APPLIED_BOB;
}



public void setR25_WITH_FACTOR_APPLIED_BOB(BigDecimal r25_WITH_FACTOR_APPLIED_BOB) {
	R25_WITH_FACTOR_APPLIED_BOB = r25_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR26_FACTOR_BOB() {
	return R26_FACTOR_BOB;
}



public void setR26_FACTOR_BOB(BigDecimal r26_FACTOR_BOB) {
	R26_FACTOR_BOB = r26_FACTOR_BOB;
}



public BigDecimal getR26_TOTAL_AMOUNT_BOB() {
	return R26_TOTAL_AMOUNT_BOB;
}



public void setR26_TOTAL_AMOUNT_BOB(BigDecimal r26_TOTAL_AMOUNT_BOB) {
	R26_TOTAL_AMOUNT_BOB = r26_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR26_WITH_FACTOR_APPLIED_BOB() {
	return R26_WITH_FACTOR_APPLIED_BOB;
}



public void setR26_WITH_FACTOR_APPLIED_BOB(BigDecimal r26_WITH_FACTOR_APPLIED_BOB) {
	R26_WITH_FACTOR_APPLIED_BOB = r26_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR27_FACTOR_BOB() {
	return R27_FACTOR_BOB;
}



public void setR27_FACTOR_BOB(BigDecimal r27_FACTOR_BOB) {
	R27_FACTOR_BOB = r27_FACTOR_BOB;
}



public BigDecimal getR27_TOTAL_AMOUNT_BOB() {
	return R27_TOTAL_AMOUNT_BOB;
}



public void setR27_TOTAL_AMOUNT_BOB(BigDecimal r27_TOTAL_AMOUNT_BOB) {
	R27_TOTAL_AMOUNT_BOB = r27_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR27_WITH_FACTOR_APPLIED_BOB() {
	return R27_WITH_FACTOR_APPLIED_BOB;
}



public void setR27_WITH_FACTOR_APPLIED_BOB(BigDecimal r27_WITH_FACTOR_APPLIED_BOB) {
	R27_WITH_FACTOR_APPLIED_BOB = r27_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR28_FACTOR_BOB() {
	return R28_FACTOR_BOB;
}



public void setR28_FACTOR_BOB(BigDecimal r28_FACTOR_BOB) {
	R28_FACTOR_BOB = r28_FACTOR_BOB;
}



public BigDecimal getR28_TOTAL_AMOUNT_BOB() {
	return R28_TOTAL_AMOUNT_BOB;
}



public void setR28_TOTAL_AMOUNT_BOB(BigDecimal r28_TOTAL_AMOUNT_BOB) {
	R28_TOTAL_AMOUNT_BOB = r28_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR28_WITH_FACTOR_APPLIED_BOB() {
	return R28_WITH_FACTOR_APPLIED_BOB;
}



public void setR28_WITH_FACTOR_APPLIED_BOB(BigDecimal r28_WITH_FACTOR_APPLIED_BOB) {
	R28_WITH_FACTOR_APPLIED_BOB = r28_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR29_FACTOR_BOB() {
	return R29_FACTOR_BOB;
}



public void setR29_FACTOR_BOB(BigDecimal r29_FACTOR_BOB) {
	R29_FACTOR_BOB = r29_FACTOR_BOB;
}



public BigDecimal getR29_TOTAL_AMOUNT_BOB() {
	return R29_TOTAL_AMOUNT_BOB;
}



public void setR29_TOTAL_AMOUNT_BOB(BigDecimal r29_TOTAL_AMOUNT_BOB) {
	R29_TOTAL_AMOUNT_BOB = r29_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR29_WITH_FACTOR_APPLIED_BOB() {
	return R29_WITH_FACTOR_APPLIED_BOB;
}



public void setR29_WITH_FACTOR_APPLIED_BOB(BigDecimal r29_WITH_FACTOR_APPLIED_BOB) {
	R29_WITH_FACTOR_APPLIED_BOB = r29_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR30_FACTOR_BOB() {
	return R30_FACTOR_BOB;
}



public void setR30_FACTOR_BOB(BigDecimal r30_FACTOR_BOB) {
	R30_FACTOR_BOB = r30_FACTOR_BOB;
}



public BigDecimal getR30_TOTAL_AMOUNT_BOB() {
	return R30_TOTAL_AMOUNT_BOB;
}



public void setR30_TOTAL_AMOUNT_BOB(BigDecimal r30_TOTAL_AMOUNT_BOB) {
	R30_TOTAL_AMOUNT_BOB = r30_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR30_WITH_FACTOR_APPLIED_BOB() {
	return R30_WITH_FACTOR_APPLIED_BOB;
}



public void setR30_WITH_FACTOR_APPLIED_BOB(BigDecimal r30_WITH_FACTOR_APPLIED_BOB) {
	R30_WITH_FACTOR_APPLIED_BOB = r30_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR31_FACTOR_BOB() {
	return R31_FACTOR_BOB;
}



public void setR31_FACTOR_BOB(BigDecimal r31_FACTOR_BOB) {
	R31_FACTOR_BOB = r31_FACTOR_BOB;
}



public BigDecimal getR31_TOTAL_AMOUNT_BOB() {
	return R31_TOTAL_AMOUNT_BOB;
}



public void setR31_TOTAL_AMOUNT_BOB(BigDecimal r31_TOTAL_AMOUNT_BOB) {
	R31_TOTAL_AMOUNT_BOB = r31_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR31_WITH_FACTOR_APPLIED_BOB() {
	return R31_WITH_FACTOR_APPLIED_BOB;
}



public void setR31_WITH_FACTOR_APPLIED_BOB(BigDecimal r31_WITH_FACTOR_APPLIED_BOB) {
	R31_WITH_FACTOR_APPLIED_BOB = r31_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR32_FACTOR_BOB() {
	return R32_FACTOR_BOB;
}



public void setR32_FACTOR_BOB(BigDecimal r32_FACTOR_BOB) {
	R32_FACTOR_BOB = r32_FACTOR_BOB;
}



public BigDecimal getR32_TOTAL_AMOUNT_BOB() {
	return R32_TOTAL_AMOUNT_BOB;
}



public void setR32_TOTAL_AMOUNT_BOB(BigDecimal r32_TOTAL_AMOUNT_BOB) {
	R32_TOTAL_AMOUNT_BOB = r32_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR32_WITH_FACTOR_APPLIED_BOB() {
	return R32_WITH_FACTOR_APPLIED_BOB;
}



public void setR32_WITH_FACTOR_APPLIED_BOB(BigDecimal r32_WITH_FACTOR_APPLIED_BOB) {
	R32_WITH_FACTOR_APPLIED_BOB = r32_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR33_FACTOR_BOB() {
	return R33_FACTOR_BOB;
}



public void setR33_FACTOR_BOB(BigDecimal r33_FACTOR_BOB) {
	R33_FACTOR_BOB = r33_FACTOR_BOB;
}



public BigDecimal getR33_TOTAL_AMOUNT_BOB() {
	return R33_TOTAL_AMOUNT_BOB;
}



public void setR33_TOTAL_AMOUNT_BOB(BigDecimal r33_TOTAL_AMOUNT_BOB) {
	R33_TOTAL_AMOUNT_BOB = r33_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR33_WITH_FACTOR_APPLIED_BOB() {
	return R33_WITH_FACTOR_APPLIED_BOB;
}



public void setR33_WITH_FACTOR_APPLIED_BOB(BigDecimal r33_WITH_FACTOR_APPLIED_BOB) {
	R33_WITH_FACTOR_APPLIED_BOB = r33_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR34_FACTOR_BOB() {
	return R34_FACTOR_BOB;
}



public void setR34_FACTOR_BOB(BigDecimal r34_FACTOR_BOB) {
	R34_FACTOR_BOB = r34_FACTOR_BOB;
}



public BigDecimal getR34_TOTAL_AMOUNT_BOB() {
	return R34_TOTAL_AMOUNT_BOB;
}



public void setR34_TOTAL_AMOUNT_BOB(BigDecimal r34_TOTAL_AMOUNT_BOB) {
	R34_TOTAL_AMOUNT_BOB = r34_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR34_WITH_FACTOR_APPLIED_BOB() {
	return R34_WITH_FACTOR_APPLIED_BOB;
}



public void setR34_WITH_FACTOR_APPLIED_BOB(BigDecimal r34_WITH_FACTOR_APPLIED_BOB) {
	R34_WITH_FACTOR_APPLIED_BOB = r34_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR35_FACTOR_BOB() {
	return R35_FACTOR_BOB;
}



public void setR35_FACTOR_BOB(BigDecimal r35_FACTOR_BOB) {
	R35_FACTOR_BOB = r35_FACTOR_BOB;
}



public BigDecimal getR35_TOTAL_AMOUNT_BOB() {
	return R35_TOTAL_AMOUNT_BOB;
}



public void setR35_TOTAL_AMOUNT_BOB(BigDecimal r35_TOTAL_AMOUNT_BOB) {
	R35_TOTAL_AMOUNT_BOB = r35_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR35_WITH_FACTOR_APPLIED_BOB() {
	return R35_WITH_FACTOR_APPLIED_BOB;
}



public void setR35_WITH_FACTOR_APPLIED_BOB(BigDecimal r35_WITH_FACTOR_APPLIED_BOB) {
	R35_WITH_FACTOR_APPLIED_BOB = r35_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR36_FACTOR_BOB() {
	return R36_FACTOR_BOB;
}



public void setR36_FACTOR_BOB(BigDecimal r36_FACTOR_BOB) {
	R36_FACTOR_BOB = r36_FACTOR_BOB;
}



public BigDecimal getR36_TOTAL_AMOUNT_BOB() {
	return R36_TOTAL_AMOUNT_BOB;
}



public void setR36_TOTAL_AMOUNT_BOB(BigDecimal r36_TOTAL_AMOUNT_BOB) {
	R36_TOTAL_AMOUNT_BOB = r36_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR36_WITH_FACTOR_APPLIED_BOB() {
	return R36_WITH_FACTOR_APPLIED_BOB;
}



public void setR36_WITH_FACTOR_APPLIED_BOB(BigDecimal r36_WITH_FACTOR_APPLIED_BOB) {
	R36_WITH_FACTOR_APPLIED_BOB = r36_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR37_FACTOR_BOB() {
	return R37_FACTOR_BOB;
}



public void setR37_FACTOR_BOB(BigDecimal r37_FACTOR_BOB) {
	R37_FACTOR_BOB = r37_FACTOR_BOB;
}



public BigDecimal getR37_TOTAL_AMOUNT_BOB() {
	return R37_TOTAL_AMOUNT_BOB;
}



public void setR37_TOTAL_AMOUNT_BOB(BigDecimal r37_TOTAL_AMOUNT_BOB) {
	R37_TOTAL_AMOUNT_BOB = r37_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR37_WITH_FACTOR_APPLIED_BOB() {
	return R37_WITH_FACTOR_APPLIED_BOB;
}



public void setR37_WITH_FACTOR_APPLIED_BOB(BigDecimal r37_WITH_FACTOR_APPLIED_BOB) {
	R37_WITH_FACTOR_APPLIED_BOB = r37_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR38_FACTOR_BOB() {
	return R38_FACTOR_BOB;
}



public void setR38_FACTOR_BOB(BigDecimal r38_FACTOR_BOB) {
	R38_FACTOR_BOB = r38_FACTOR_BOB;
}



public BigDecimal getR38_TOTAL_AMOUNT_BOB() {
	return R38_TOTAL_AMOUNT_BOB;
}



public void setR38_TOTAL_AMOUNT_BOB(BigDecimal r38_TOTAL_AMOUNT_BOB) {
	R38_TOTAL_AMOUNT_BOB = r38_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR38_WITH_FACTOR_APPLIED_BOB() {
	return R38_WITH_FACTOR_APPLIED_BOB;
}



public void setR38_WITH_FACTOR_APPLIED_BOB(BigDecimal r38_WITH_FACTOR_APPLIED_BOB) {
	R38_WITH_FACTOR_APPLIED_BOB = r38_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR39_FACTOR_BOB() {
	return R39_FACTOR_BOB;
}



public void setR39_FACTOR_BOB(BigDecimal r39_FACTOR_BOB) {
	R39_FACTOR_BOB = r39_FACTOR_BOB;
}



public BigDecimal getR39_TOTAL_AMOUNT_BOB() {
	return R39_TOTAL_AMOUNT_BOB;
}



public void setR39_TOTAL_AMOUNT_BOB(BigDecimal r39_TOTAL_AMOUNT_BOB) {
	R39_TOTAL_AMOUNT_BOB = r39_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR39_WITH_FACTOR_APPLIED_BOB() {
	return R39_WITH_FACTOR_APPLIED_BOB;
}



public void setR39_WITH_FACTOR_APPLIED_BOB(BigDecimal r39_WITH_FACTOR_APPLIED_BOB) {
	R39_WITH_FACTOR_APPLIED_BOB = r39_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR40_FACTOR_BOB() {
	return R40_FACTOR_BOB;
}



public void setR40_FACTOR_BOB(BigDecimal r40_FACTOR_BOB) {
	R40_FACTOR_BOB = r40_FACTOR_BOB;
}



public BigDecimal getR40_TOTAL_AMOUNT_BOB() {
	return R40_TOTAL_AMOUNT_BOB;
}



public void setR40_TOTAL_AMOUNT_BOB(BigDecimal r40_TOTAL_AMOUNT_BOB) {
	R40_TOTAL_AMOUNT_BOB = r40_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR40_WITH_FACTOR_APPLIED_BOB() {
	return R40_WITH_FACTOR_APPLIED_BOB;
}



public void setR40_WITH_FACTOR_APPLIED_BOB(BigDecimal r40_WITH_FACTOR_APPLIED_BOB) {
	R40_WITH_FACTOR_APPLIED_BOB = r40_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR41_FACTOR_BOB() {
	return R41_FACTOR_BOB;
}



public void setR41_FACTOR_BOB(BigDecimal r41_FACTOR_BOB) {
	R41_FACTOR_BOB = r41_FACTOR_BOB;
}



public BigDecimal getR41_TOTAL_AMOUNT_BOB() {
	return R41_TOTAL_AMOUNT_BOB;
}



public void setR41_TOTAL_AMOUNT_BOB(BigDecimal r41_TOTAL_AMOUNT_BOB) {
	R41_TOTAL_AMOUNT_BOB = r41_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR41_WITH_FACTOR_APPLIED_BOB() {
	return R41_WITH_FACTOR_APPLIED_BOB;
}



public void setR41_WITH_FACTOR_APPLIED_BOB(BigDecimal r41_WITH_FACTOR_APPLIED_BOB) {
	R41_WITH_FACTOR_APPLIED_BOB = r41_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR42_FACTOR_BOB() {
	return R42_FACTOR_BOB;
}



public void setR42_FACTOR_BOB(BigDecimal r42_FACTOR_BOB) {
	R42_FACTOR_BOB = r42_FACTOR_BOB;
}



public BigDecimal getR42_TOTAL_AMOUNT_BOB() {
	return R42_TOTAL_AMOUNT_BOB;
}



public void setR42_TOTAL_AMOUNT_BOB(BigDecimal r42_TOTAL_AMOUNT_BOB) {
	R42_TOTAL_AMOUNT_BOB = r42_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR42_WITH_FACTOR_APPLIED_BOB() {
	return R42_WITH_FACTOR_APPLIED_BOB;
}



public void setR42_WITH_FACTOR_APPLIED_BOB(BigDecimal r42_WITH_FACTOR_APPLIED_BOB) {
	R42_WITH_FACTOR_APPLIED_BOB = r42_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR43_FACTOR_BOB() {
	return R43_FACTOR_BOB;
}



public void setR43_FACTOR_BOB(BigDecimal r43_FACTOR_BOB) {
	R43_FACTOR_BOB = r43_FACTOR_BOB;
}



public BigDecimal getR43_TOTAL_AMOUNT_BOB() {
	return R43_TOTAL_AMOUNT_BOB;
}



public void setR43_TOTAL_AMOUNT_BOB(BigDecimal r43_TOTAL_AMOUNT_BOB) {
	R43_TOTAL_AMOUNT_BOB = r43_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR43_WITH_FACTOR_APPLIED_BOB() {
	return R43_WITH_FACTOR_APPLIED_BOB;
}



public void setR43_WITH_FACTOR_APPLIED_BOB(BigDecimal r43_WITH_FACTOR_APPLIED_BOB) {
	R43_WITH_FACTOR_APPLIED_BOB = r43_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR44_FACTOR_BOB() {
	return R44_FACTOR_BOB;
}



public void setR44_FACTOR_BOB(BigDecimal r44_FACTOR_BOB) {
	R44_FACTOR_BOB = r44_FACTOR_BOB;
}



public BigDecimal getR44_TOTAL_AMOUNT_BOB() {
	return R44_TOTAL_AMOUNT_BOB;
}



public void setR44_TOTAL_AMOUNT_BOB(BigDecimal r44_TOTAL_AMOUNT_BOB) {
	R44_TOTAL_AMOUNT_BOB = r44_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR44_WITH_FACTOR_APPLIED_BOB() {
	return R44_WITH_FACTOR_APPLIED_BOB;
}



public void setR44_WITH_FACTOR_APPLIED_BOB(BigDecimal r44_WITH_FACTOR_APPLIED_BOB) {
	R44_WITH_FACTOR_APPLIED_BOB = r44_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR45_FACTOR_BOB() {
	return R45_FACTOR_BOB;
}



public void setR45_FACTOR_BOB(BigDecimal r45_FACTOR_BOB) {
	R45_FACTOR_BOB = r45_FACTOR_BOB;
}



public BigDecimal getR45_TOTAL_AMOUNT_BOB() {
	return R45_TOTAL_AMOUNT_BOB;
}



public void setR45_TOTAL_AMOUNT_BOB(BigDecimal r45_TOTAL_AMOUNT_BOB) {
	R45_TOTAL_AMOUNT_BOB = r45_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR45_WITH_FACTOR_APPLIED_BOB() {
	return R45_WITH_FACTOR_APPLIED_BOB;
}



public void setR45_WITH_FACTOR_APPLIED_BOB(BigDecimal r45_WITH_FACTOR_APPLIED_BOB) {
	R45_WITH_FACTOR_APPLIED_BOB = r45_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR46_FACTOR_BOB() {
	return R46_FACTOR_BOB;
}



public void setR46_FACTOR_BOB(BigDecimal r46_FACTOR_BOB) {
	R46_FACTOR_BOB = r46_FACTOR_BOB;
}



public BigDecimal getR46_TOTAL_AMOUNT_BOB() {
	return R46_TOTAL_AMOUNT_BOB;
}



public void setR46_TOTAL_AMOUNT_BOB(BigDecimal r46_TOTAL_AMOUNT_BOB) {
	R46_TOTAL_AMOUNT_BOB = r46_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR46_WITH_FACTOR_APPLIED_BOB() {
	return R46_WITH_FACTOR_APPLIED_BOB;
}



public void setR46_WITH_FACTOR_APPLIED_BOB(BigDecimal r46_WITH_FACTOR_APPLIED_BOB) {
	R46_WITH_FACTOR_APPLIED_BOB = r46_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR47_FACTOR_BOB() {
	return R47_FACTOR_BOB;
}



public void setR47_FACTOR_BOB(BigDecimal r47_FACTOR_BOB) {
	R47_FACTOR_BOB = r47_FACTOR_BOB;
}



public BigDecimal getR47_TOTAL_AMOUNT_BOB() {
	return R47_TOTAL_AMOUNT_BOB;
}



public void setR47_TOTAL_AMOUNT_BOB(BigDecimal r47_TOTAL_AMOUNT_BOB) {
	R47_TOTAL_AMOUNT_BOB = r47_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR47_WITH_FACTOR_APPLIED_BOB() {
	return R47_WITH_FACTOR_APPLIED_BOB;
}



public void setR47_WITH_FACTOR_APPLIED_BOB(BigDecimal r47_WITH_FACTOR_APPLIED_BOB) {
	R47_WITH_FACTOR_APPLIED_BOB = r47_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR48_FACTOR_BOB() {
	return R48_FACTOR_BOB;
}



public void setR48_FACTOR_BOB(BigDecimal r48_FACTOR_BOB) {
	R48_FACTOR_BOB = r48_FACTOR_BOB;
}



public BigDecimal getR48_TOTAL_AMOUNT_BOB() {
	return R48_TOTAL_AMOUNT_BOB;
}



public void setR48_TOTAL_AMOUNT_BOB(BigDecimal r48_TOTAL_AMOUNT_BOB) {
	R48_TOTAL_AMOUNT_BOB = r48_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR48_WITH_FACTOR_APPLIED_BOB() {
	return R48_WITH_FACTOR_APPLIED_BOB;
}



public void setR48_WITH_FACTOR_APPLIED_BOB(BigDecimal r48_WITH_FACTOR_APPLIED_BOB) {
	R48_WITH_FACTOR_APPLIED_BOB = r48_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR49_FACTOR_BOB() {
	return R49_FACTOR_BOB;
}



public void setR49_FACTOR_BOB(BigDecimal r49_FACTOR_BOB) {
	R49_FACTOR_BOB = r49_FACTOR_BOB;
}



public BigDecimal getR49_TOTAL_AMOUNT_BOB() {
	return R49_TOTAL_AMOUNT_BOB;
}



public void setR49_TOTAL_AMOUNT_BOB(BigDecimal r49_TOTAL_AMOUNT_BOB) {
	R49_TOTAL_AMOUNT_BOB = r49_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR49_WITH_FACTOR_APPLIED_BOB() {
	return R49_WITH_FACTOR_APPLIED_BOB;
}



public void setR49_WITH_FACTOR_APPLIED_BOB(BigDecimal r49_WITH_FACTOR_APPLIED_BOB) {
	R49_WITH_FACTOR_APPLIED_BOB = r49_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR50_FACTOR_BOB() {
	return R50_FACTOR_BOB;
}



public void setR50_FACTOR_BOB(BigDecimal r50_FACTOR_BOB) {
	R50_FACTOR_BOB = r50_FACTOR_BOB;
}



public BigDecimal getR50_TOTAL_AMOUNT_BOB() {
	return R50_TOTAL_AMOUNT_BOB;
}



public void setR50_TOTAL_AMOUNT_BOB(BigDecimal r50_TOTAL_AMOUNT_BOB) {
	R50_TOTAL_AMOUNT_BOB = r50_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR50_WITH_FACTOR_APPLIED_BOB() {
	return R50_WITH_FACTOR_APPLIED_BOB;
}



public void setR50_WITH_FACTOR_APPLIED_BOB(BigDecimal r50_WITH_FACTOR_APPLIED_BOB) {
	R50_WITH_FACTOR_APPLIED_BOB = r50_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR51_FACTOR_BOB() {
	return R51_FACTOR_BOB;
}



public void setR51_FACTOR_BOB(BigDecimal r51_FACTOR_BOB) {
	R51_FACTOR_BOB = r51_FACTOR_BOB;
}



public BigDecimal getR51_TOTAL_AMOUNT_BOB() {
	return R51_TOTAL_AMOUNT_BOB;
}



public void setR51_TOTAL_AMOUNT_BOB(BigDecimal r51_TOTAL_AMOUNT_BOB) {
	R51_TOTAL_AMOUNT_BOB = r51_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR51_WITH_FACTOR_APPLIED_BOB() {
	return R51_WITH_FACTOR_APPLIED_BOB;
}



public void setR51_WITH_FACTOR_APPLIED_BOB(BigDecimal r51_WITH_FACTOR_APPLIED_BOB) {
	R51_WITH_FACTOR_APPLIED_BOB = r51_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR52_FACTOR_BOB() {
	return R52_FACTOR_BOB;
}



public void setR52_FACTOR_BOB(BigDecimal r52_FACTOR_BOB) {
	R52_FACTOR_BOB = r52_FACTOR_BOB;
}



public BigDecimal getR52_TOTAL_AMOUNT_BOB() {
	return R52_TOTAL_AMOUNT_BOB;
}



public void setR52_TOTAL_AMOUNT_BOB(BigDecimal r52_TOTAL_AMOUNT_BOB) {
	R52_TOTAL_AMOUNT_BOB = r52_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR52_WITH_FACTOR_APPLIED_BOB() {
	return R52_WITH_FACTOR_APPLIED_BOB;
}



public void setR52_WITH_FACTOR_APPLIED_BOB(BigDecimal r52_WITH_FACTOR_APPLIED_BOB) {
	R52_WITH_FACTOR_APPLIED_BOB = r52_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR53_FACTOR_BOB() {
	return R53_FACTOR_BOB;
}



public void setR53_FACTOR_BOB(BigDecimal r53_FACTOR_BOB) {
	R53_FACTOR_BOB = r53_FACTOR_BOB;
}



public BigDecimal getR53_TOTAL_AMOUNT_BOB() {
	return R53_TOTAL_AMOUNT_BOB;
}



public void setR53_TOTAL_AMOUNT_BOB(BigDecimal r53_TOTAL_AMOUNT_BOB) {
	R53_TOTAL_AMOUNT_BOB = r53_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR53_WITH_FACTOR_APPLIED_BOB() {
	return R53_WITH_FACTOR_APPLIED_BOB;
}



public void setR53_WITH_FACTOR_APPLIED_BOB(BigDecimal r53_WITH_FACTOR_APPLIED_BOB) {
	R53_WITH_FACTOR_APPLIED_BOB = r53_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR54_FACTOR_BOB() {
	return R54_FACTOR_BOB;
}



public void setR54_FACTOR_BOB(BigDecimal r54_FACTOR_BOB) {
	R54_FACTOR_BOB = r54_FACTOR_BOB;
}



public BigDecimal getR54_TOTAL_AMOUNT_BOB() {
	return R54_TOTAL_AMOUNT_BOB;
}



public void setR54_TOTAL_AMOUNT_BOB(BigDecimal r54_TOTAL_AMOUNT_BOB) {
	R54_TOTAL_AMOUNT_BOB = r54_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR54_WITH_FACTOR_APPLIED_BOB() {
	return R54_WITH_FACTOR_APPLIED_BOB;
}



public void setR54_WITH_FACTOR_APPLIED_BOB(BigDecimal r54_WITH_FACTOR_APPLIED_BOB) {
	R54_WITH_FACTOR_APPLIED_BOB = r54_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR55_FACTOR_BOB() {
	return R55_FACTOR_BOB;
}



public void setR55_FACTOR_BOB(BigDecimal r55_FACTOR_BOB) {
	R55_FACTOR_BOB = r55_FACTOR_BOB;
}



public BigDecimal getR55_TOTAL_AMOUNT_BOB() {
	return R55_TOTAL_AMOUNT_BOB;
}



public void setR55_TOTAL_AMOUNT_BOB(BigDecimal r55_TOTAL_AMOUNT_BOB) {
	R55_TOTAL_AMOUNT_BOB = r55_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR55_WITH_FACTOR_APPLIED_BOB() {
	return R55_WITH_FACTOR_APPLIED_BOB;
}



public void setR55_WITH_FACTOR_APPLIED_BOB(BigDecimal r55_WITH_FACTOR_APPLIED_BOB) {
	R55_WITH_FACTOR_APPLIED_BOB = r55_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR56_FACTOR_BOB() {
	return R56_FACTOR_BOB;
}



public void setR56_FACTOR_BOB(BigDecimal r56_FACTOR_BOB) {
	R56_FACTOR_BOB = r56_FACTOR_BOB;
}



public BigDecimal getR56_TOTAL_AMOUNT_BOB() {
	return R56_TOTAL_AMOUNT_BOB;
}



public void setR56_TOTAL_AMOUNT_BOB(BigDecimal r56_TOTAL_AMOUNT_BOB) {
	R56_TOTAL_AMOUNT_BOB = r56_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR56_WITH_FACTOR_APPLIED_BOB() {
	return R56_WITH_FACTOR_APPLIED_BOB;
}



public void setR56_WITH_FACTOR_APPLIED_BOB(BigDecimal r56_WITH_FACTOR_APPLIED_BOB) {
	R56_WITH_FACTOR_APPLIED_BOB = r56_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR57_FACTOR_BOB() {
	return R57_FACTOR_BOB;
}



public void setR57_FACTOR_BOB(BigDecimal r57_FACTOR_BOB) {
	R57_FACTOR_BOB = r57_FACTOR_BOB;
}



public BigDecimal getR57_TOTAL_AMOUNT_BOB() {
	return R57_TOTAL_AMOUNT_BOB;
}



public void setR57_TOTAL_AMOUNT_BOB(BigDecimal r57_TOTAL_AMOUNT_BOB) {
	R57_TOTAL_AMOUNT_BOB = r57_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR57_WITH_FACTOR_APPLIED_BOB() {
	return R57_WITH_FACTOR_APPLIED_BOB;
}



public void setR57_WITH_FACTOR_APPLIED_BOB(BigDecimal r57_WITH_FACTOR_APPLIED_BOB) {
	R57_WITH_FACTOR_APPLIED_BOB = r57_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR58_FACTOR_BOB() {
	return R58_FACTOR_BOB;
}



public void setR58_FACTOR_BOB(BigDecimal r58_FACTOR_BOB) {
	R58_FACTOR_BOB = r58_FACTOR_BOB;
}



public BigDecimal getR58_TOTAL_AMOUNT_BOB() {
	return R58_TOTAL_AMOUNT_BOB;
}



public void setR58_TOTAL_AMOUNT_BOB(BigDecimal r58_TOTAL_AMOUNT_BOB) {
	R58_TOTAL_AMOUNT_BOB = r58_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR58_WITH_FACTOR_APPLIED_BOB() {
	return R58_WITH_FACTOR_APPLIED_BOB;
}



public void setR58_WITH_FACTOR_APPLIED_BOB(BigDecimal r58_WITH_FACTOR_APPLIED_BOB) {
	R58_WITH_FACTOR_APPLIED_BOB = r58_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR59_FACTOR_BOB() {
	return R59_FACTOR_BOB;
}



public void setR59_FACTOR_BOB(BigDecimal r59_FACTOR_BOB) {
	R59_FACTOR_BOB = r59_FACTOR_BOB;
}



public BigDecimal getR59_TOTAL_AMOUNT_BOB() {
	return R59_TOTAL_AMOUNT_BOB;
}



public void setR59_TOTAL_AMOUNT_BOB(BigDecimal r59_TOTAL_AMOUNT_BOB) {
	R59_TOTAL_AMOUNT_BOB = r59_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR59_WITH_FACTOR_APPLIED_BOB() {
	return R59_WITH_FACTOR_APPLIED_BOB;
}



public void setR59_WITH_FACTOR_APPLIED_BOB(BigDecimal r59_WITH_FACTOR_APPLIED_BOB) {
	R59_WITH_FACTOR_APPLIED_BOB = r59_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR60_FACTOR_BOB() {
	return R60_FACTOR_BOB;
}



public void setR60_FACTOR_BOB(BigDecimal r60_FACTOR_BOB) {
	R60_FACTOR_BOB = r60_FACTOR_BOB;
}



public BigDecimal getR60_TOTAL_AMOUNT_BOB() {
	return R60_TOTAL_AMOUNT_BOB;
}



public void setR60_TOTAL_AMOUNT_BOB(BigDecimal r60_TOTAL_AMOUNT_BOB) {
	R60_TOTAL_AMOUNT_BOB = r60_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR60_WITH_FACTOR_APPLIED_BOB() {
	return R60_WITH_FACTOR_APPLIED_BOB;
}



public void setR60_WITH_FACTOR_APPLIED_BOB(BigDecimal r60_WITH_FACTOR_APPLIED_BOB) {
	R60_WITH_FACTOR_APPLIED_BOB = r60_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR61_FACTOR_BOB() {
	return R61_FACTOR_BOB;
}



public void setR61_FACTOR_BOB(BigDecimal r61_FACTOR_BOB) {
	R61_FACTOR_BOB = r61_FACTOR_BOB;
}



public BigDecimal getR61_TOTAL_AMOUNT_BOB() {
	return R61_TOTAL_AMOUNT_BOB;
}



public void setR61_TOTAL_AMOUNT_BOB(BigDecimal r61_TOTAL_AMOUNT_BOB) {
	R61_TOTAL_AMOUNT_BOB = r61_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR61_WITH_FACTOR_APPLIED_BOB() {
	return R61_WITH_FACTOR_APPLIED_BOB;
}



public void setR61_WITH_FACTOR_APPLIED_BOB(BigDecimal r61_WITH_FACTOR_APPLIED_BOB) {
	R61_WITH_FACTOR_APPLIED_BOB = r61_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR62_FACTOR_BOB() {
	return R62_FACTOR_BOB;
}



public void setR62_FACTOR_BOB(BigDecimal r62_FACTOR_BOB) {
	R62_FACTOR_BOB = r62_FACTOR_BOB;
}



public BigDecimal getR62_TOTAL_AMOUNT_BOB() {
	return R62_TOTAL_AMOUNT_BOB;
}



public void setR62_TOTAL_AMOUNT_BOB(BigDecimal r62_TOTAL_AMOUNT_BOB) {
	R62_TOTAL_AMOUNT_BOB = r62_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR62_WITH_FACTOR_APPLIED_BOB() {
	return R62_WITH_FACTOR_APPLIED_BOB;
}



public void setR62_WITH_FACTOR_APPLIED_BOB(BigDecimal r62_WITH_FACTOR_APPLIED_BOB) {
	R62_WITH_FACTOR_APPLIED_BOB = r62_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR63_FACTOR_BOB() {
	return R63_FACTOR_BOB;
}



public void setR63_FACTOR_BOB(BigDecimal r63_FACTOR_BOB) {
	R63_FACTOR_BOB = r63_FACTOR_BOB;
}



public BigDecimal getR63_TOTAL_AMOUNT_BOB() {
	return R63_TOTAL_AMOUNT_BOB;
}



public void setR63_TOTAL_AMOUNT_BOB(BigDecimal r63_TOTAL_AMOUNT_BOB) {
	R63_TOTAL_AMOUNT_BOB = r63_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR63_WITH_FACTOR_APPLIED_BOB() {
	return R63_WITH_FACTOR_APPLIED_BOB;
}



public void setR63_WITH_FACTOR_APPLIED_BOB(BigDecimal r63_WITH_FACTOR_APPLIED_BOB) {
	R63_WITH_FACTOR_APPLIED_BOB = r63_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR64_FACTOR_BOB() {
	return R64_FACTOR_BOB;
}



public void setR64_FACTOR_BOB(BigDecimal r64_FACTOR_BOB) {
	R64_FACTOR_BOB = r64_FACTOR_BOB;
}



public BigDecimal getR64_TOTAL_AMOUNT_BOB() {
	return R64_TOTAL_AMOUNT_BOB;
}



public void setR64_TOTAL_AMOUNT_BOB(BigDecimal r64_TOTAL_AMOUNT_BOB) {
	R64_TOTAL_AMOUNT_BOB = r64_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR64_WITH_FACTOR_APPLIED_BOB() {
	return R64_WITH_FACTOR_APPLIED_BOB;
}



public void setR64_WITH_FACTOR_APPLIED_BOB(BigDecimal r64_WITH_FACTOR_APPLIED_BOB) {
	R64_WITH_FACTOR_APPLIED_BOB = r64_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR65_FACTOR_BOB() {
	return R65_FACTOR_BOB;
}



public void setR65_FACTOR_BOB(BigDecimal r65_FACTOR_BOB) {
	R65_FACTOR_BOB = r65_FACTOR_BOB;
}



public BigDecimal getR65_TOTAL_AMOUNT_BOB() {
	return R65_TOTAL_AMOUNT_BOB;
}



public void setR65_TOTAL_AMOUNT_BOB(BigDecimal r65_TOTAL_AMOUNT_BOB) {
	R65_TOTAL_AMOUNT_BOB = r65_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR65_WITH_FACTOR_APPLIED_BOB() {
	return R65_WITH_FACTOR_APPLIED_BOB;
}



public void setR65_WITH_FACTOR_APPLIED_BOB(BigDecimal r65_WITH_FACTOR_APPLIED_BOB) {
	R65_WITH_FACTOR_APPLIED_BOB = r65_WITH_FACTOR_APPLIED_BOB;
}



public BigDecimal getR66_FACTOR_BOB() {
	return R66_FACTOR_BOB;
}



public void setR66_FACTOR_BOB(BigDecimal r66_FACTOR_BOB) {
	R66_FACTOR_BOB = r66_FACTOR_BOB;
}



public BigDecimal getR66_TOTAL_AMOUNT_BOB() {
	return R66_TOTAL_AMOUNT_BOB;
}



public void setR66_TOTAL_AMOUNT_BOB(BigDecimal r66_TOTAL_AMOUNT_BOB) {
	R66_TOTAL_AMOUNT_BOB = r66_TOTAL_AMOUNT_BOB;
}



public BigDecimal getR66_WITH_FACTOR_APPLIED_BOB() {
	return R66_WITH_FACTOR_APPLIED_BOB;
}



public void setR66_WITH_FACTOR_APPLIED_BOB(BigDecimal r66_WITH_FACTOR_APPLIED_BOB) {
	R66_WITH_FACTOR_APPLIED_BOB = r66_WITH_FACTOR_APPLIED_BOB;
}



public NSFR_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}








}
