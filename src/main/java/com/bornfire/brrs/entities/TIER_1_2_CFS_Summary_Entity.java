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
@Table(name = "BRRS_TIER_1_2_CFS_SUMMARYTABLE")
public class TIER_1_2_CFS_Summary_Entity {


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

private BigDecimal R10_VALUE_IN_I_COLUMN;
private BigDecimal R10_BWP_AMOUNT;
private BigDecimal R11_VALUE_IN_I_COLUMN;
private BigDecimal R11_BWP_AMOUNT;
private BigDecimal R12_VALUE_IN_I_COLUMN;
private BigDecimal R12_BWP_AMOUNT;
private BigDecimal R13_VALUE_IN_I_COLUMN;
private BigDecimal R13_BWP_AMOUNT;
private BigDecimal R14_VALUE_IN_I_COLUMN;
private BigDecimal R14_BWP_AMOUNT;
private BigDecimal R15_VALUE_IN_I_COLUMN;
private BigDecimal R15_BWP_AMOUNT;
private BigDecimal R16_VALUE_IN_I_COLUMN;
private BigDecimal R16_BWP_AMOUNT;
private BigDecimal R17_VALUE_IN_I_COLUMN;
private BigDecimal R17_BWP_AMOUNT;
private BigDecimal R18_VALUE_IN_I_COLUMN;
private BigDecimal R18_BWP_AMOUNT;
private BigDecimal R19_VALUE_IN_I_COLUMN;
private BigDecimal R19_BWP_AMOUNT;
private BigDecimal R20_VALUE_IN_I_COLUMN;
private BigDecimal R20_BWP_AMOUNT;
private BigDecimal R21_VALUE_IN_I_COLUMN;
private BigDecimal R21_BWP_AMOUNT;
private BigDecimal R22_VALUE_IN_I_COLUMN;
private BigDecimal R22_BWP_AMOUNT;
private BigDecimal R23_VALUE_IN_I_COLUMN;
private BigDecimal R23_BWP_AMOUNT;
private BigDecimal R24_VALUE_IN_I_COLUMN;
private BigDecimal R24_BWP_AMOUNT;
private BigDecimal R25_VALUE_IN_I_COLUMN;
private BigDecimal R25_BWP_AMOUNT;
private BigDecimal R26_VALUE_IN_I_COLUMN;
private BigDecimal R26_BWP_AMOUNT;
private BigDecimal R27_VALUE_IN_I_COLUMN;
private BigDecimal R27_BWP_AMOUNT;
private BigDecimal R28_VALUE_IN_I_COLUMN;
private BigDecimal R28_BWP_AMOUNT;
private BigDecimal R29_VALUE_IN_I_COLUMN;
private BigDecimal R29_BWP_AMOUNT;
private BigDecimal R30_VALUE_IN_I_COLUMN;
private BigDecimal R30_BWP_AMOUNT;
private BigDecimal R31_VALUE_IN_I_COLUMN;
private BigDecimal R31_BWP_AMOUNT;
private BigDecimal R32_VALUE_IN_I_COLUMN;
private BigDecimal R32_BWP_AMOUNT;
private BigDecimal R33_VALUE_IN_I_COLUMN;
private BigDecimal R33_BWP_AMOUNT;
private BigDecimal R34_VALUE_IN_I_COLUMN;
private BigDecimal R34_BWP_AMOUNT;
private BigDecimal R35_VALUE_IN_I_COLUMN;
private BigDecimal R35_BWP_AMOUNT;
private BigDecimal R36_VALUE_IN_I_COLUMN;
private BigDecimal R36_BWP_AMOUNT;
private BigDecimal R37_VALUE_IN_I_COLUMN;
private BigDecimal R37_BWP_AMOUNT;
private BigDecimal R41_RISK_ASSETS;
private BigDecimal R42_RISK_ASSETS;
private BigDecimal R43_RISK_ASSETS;
private BigDecimal R44_RISK_ASSETS;
private BigDecimal R46_PERCENTAGE_CF_TO_RWA;
private BigDecimal R47_PERCENTAGE_TIER1;
private BigDecimal R48_PERCENTAGE_TIER2;
private BigDecimal R52_DOMESTIC_RISK_ASSETS;
private BigDecimal R52_FOREIGN_RISK_ASSETS;
private BigDecimal R52_TOTAL_RISK_ASSETS;
private BigDecimal R53_DOMESTIC_RISK_ASSETS;
private BigDecimal R53_FOREIGN_RISK_ASSETS;
private BigDecimal R53_TOTAL_RISK_ASSETS;
private BigDecimal R54_DOMESTIC_RISK_ASSETS;
private BigDecimal R54_FOREIGN_RISK_ASSETS;
private BigDecimal R54_TOTAL_RISK_ASSETS;



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



public BigDecimal getR10_VALUE_IN_I_COLUMN() {
	return R10_VALUE_IN_I_COLUMN;
}



public void setR10_VALUE_IN_I_COLUMN(BigDecimal r10_VALUE_IN_I_COLUMN) {
	R10_VALUE_IN_I_COLUMN = r10_VALUE_IN_I_COLUMN;
}



public BigDecimal getR10_BWP_AMOUNT() {
	return R10_BWP_AMOUNT;
}



public void setR10_BWP_AMOUNT(BigDecimal r10_BWP_AMOUNT) {
	R10_BWP_AMOUNT = r10_BWP_AMOUNT;
}



public BigDecimal getR11_VALUE_IN_I_COLUMN() {
	return R11_VALUE_IN_I_COLUMN;
}



public void setR11_VALUE_IN_I_COLUMN(BigDecimal r11_VALUE_IN_I_COLUMN) {
	R11_VALUE_IN_I_COLUMN = r11_VALUE_IN_I_COLUMN;
}



public BigDecimal getR11_BWP_AMOUNT() {
	return R11_BWP_AMOUNT;
}



public void setR11_BWP_AMOUNT(BigDecimal r11_BWP_AMOUNT) {
	R11_BWP_AMOUNT = r11_BWP_AMOUNT;
}



public BigDecimal getR12_VALUE_IN_I_COLUMN() {
	return R12_VALUE_IN_I_COLUMN;
}



public void setR12_VALUE_IN_I_COLUMN(BigDecimal r12_VALUE_IN_I_COLUMN) {
	R12_VALUE_IN_I_COLUMN = r12_VALUE_IN_I_COLUMN;
}



public BigDecimal getR12_BWP_AMOUNT() {
	return R12_BWP_AMOUNT;
}



public void setR12_BWP_AMOUNT(BigDecimal r12_BWP_AMOUNT) {
	R12_BWP_AMOUNT = r12_BWP_AMOUNT;
}



public BigDecimal getR13_VALUE_IN_I_COLUMN() {
	return R13_VALUE_IN_I_COLUMN;
}



public void setR13_VALUE_IN_I_COLUMN(BigDecimal r13_VALUE_IN_I_COLUMN) {
	R13_VALUE_IN_I_COLUMN = r13_VALUE_IN_I_COLUMN;
}



public BigDecimal getR13_BWP_AMOUNT() {
	return R13_BWP_AMOUNT;
}



public void setR13_BWP_AMOUNT(BigDecimal r13_BWP_AMOUNT) {
	R13_BWP_AMOUNT = r13_BWP_AMOUNT;
}



public BigDecimal getR14_VALUE_IN_I_COLUMN() {
	return R14_VALUE_IN_I_COLUMN;
}



public void setR14_VALUE_IN_I_COLUMN(BigDecimal r14_VALUE_IN_I_COLUMN) {
	R14_VALUE_IN_I_COLUMN = r14_VALUE_IN_I_COLUMN;
}



public BigDecimal getR14_BWP_AMOUNT() {
	return R14_BWP_AMOUNT;
}



public void setR14_BWP_AMOUNT(BigDecimal r14_BWP_AMOUNT) {
	R14_BWP_AMOUNT = r14_BWP_AMOUNT;
}



public BigDecimal getR15_VALUE_IN_I_COLUMN() {
	return R15_VALUE_IN_I_COLUMN;
}



public void setR15_VALUE_IN_I_COLUMN(BigDecimal r15_VALUE_IN_I_COLUMN) {
	R15_VALUE_IN_I_COLUMN = r15_VALUE_IN_I_COLUMN;
}



public BigDecimal getR15_BWP_AMOUNT() {
	return R15_BWP_AMOUNT;
}



public void setR15_BWP_AMOUNT(BigDecimal r15_BWP_AMOUNT) {
	R15_BWP_AMOUNT = r15_BWP_AMOUNT;
}



public BigDecimal getR16_VALUE_IN_I_COLUMN() {
	return R16_VALUE_IN_I_COLUMN;
}



public void setR16_VALUE_IN_I_COLUMN(BigDecimal r16_VALUE_IN_I_COLUMN) {
	R16_VALUE_IN_I_COLUMN = r16_VALUE_IN_I_COLUMN;
}



public BigDecimal getR16_BWP_AMOUNT() {
	return R16_BWP_AMOUNT;
}



public void setR16_BWP_AMOUNT(BigDecimal r16_BWP_AMOUNT) {
	R16_BWP_AMOUNT = r16_BWP_AMOUNT;
}



public BigDecimal getR17_VALUE_IN_I_COLUMN() {
	return R17_VALUE_IN_I_COLUMN;
}



public void setR17_VALUE_IN_I_COLUMN(BigDecimal r17_VALUE_IN_I_COLUMN) {
	R17_VALUE_IN_I_COLUMN = r17_VALUE_IN_I_COLUMN;
}



public BigDecimal getR17_BWP_AMOUNT() {
	return R17_BWP_AMOUNT;
}



public void setR17_BWP_AMOUNT(BigDecimal r17_BWP_AMOUNT) {
	R17_BWP_AMOUNT = r17_BWP_AMOUNT;
}



public BigDecimal getR18_VALUE_IN_I_COLUMN() {
	return R18_VALUE_IN_I_COLUMN;
}



public void setR18_VALUE_IN_I_COLUMN(BigDecimal r18_VALUE_IN_I_COLUMN) {
	R18_VALUE_IN_I_COLUMN = r18_VALUE_IN_I_COLUMN;
}



public BigDecimal getR18_BWP_AMOUNT() {
	return R18_BWP_AMOUNT;
}



public void setR18_BWP_AMOUNT(BigDecimal r18_BWP_AMOUNT) {
	R18_BWP_AMOUNT = r18_BWP_AMOUNT;
}



public BigDecimal getR19_VALUE_IN_I_COLUMN() {
	return R19_VALUE_IN_I_COLUMN;
}



public void setR19_VALUE_IN_I_COLUMN(BigDecimal r19_VALUE_IN_I_COLUMN) {
	R19_VALUE_IN_I_COLUMN = r19_VALUE_IN_I_COLUMN;
}



public BigDecimal getR19_BWP_AMOUNT() {
	return R19_BWP_AMOUNT;
}



public void setR19_BWP_AMOUNT(BigDecimal r19_BWP_AMOUNT) {
	R19_BWP_AMOUNT = r19_BWP_AMOUNT;
}



public BigDecimal getR20_VALUE_IN_I_COLUMN() {
	return R20_VALUE_IN_I_COLUMN;
}



public void setR20_VALUE_IN_I_COLUMN(BigDecimal r20_VALUE_IN_I_COLUMN) {
	R20_VALUE_IN_I_COLUMN = r20_VALUE_IN_I_COLUMN;
}



public BigDecimal getR20_BWP_AMOUNT() {
	return R20_BWP_AMOUNT;
}



public void setR20_BWP_AMOUNT(BigDecimal r20_BWP_AMOUNT) {
	R20_BWP_AMOUNT = r20_BWP_AMOUNT;
}



public BigDecimal getR21_VALUE_IN_I_COLUMN() {
	return R21_VALUE_IN_I_COLUMN;
}



public void setR21_VALUE_IN_I_COLUMN(BigDecimal r21_VALUE_IN_I_COLUMN) {
	R21_VALUE_IN_I_COLUMN = r21_VALUE_IN_I_COLUMN;
}



public BigDecimal getR21_BWP_AMOUNT() {
	return R21_BWP_AMOUNT;
}



public void setR21_BWP_AMOUNT(BigDecimal r21_BWP_AMOUNT) {
	R21_BWP_AMOUNT = r21_BWP_AMOUNT;
}



public BigDecimal getR22_VALUE_IN_I_COLUMN() {
	return R22_VALUE_IN_I_COLUMN;
}



public void setR22_VALUE_IN_I_COLUMN(BigDecimal r22_VALUE_IN_I_COLUMN) {
	R22_VALUE_IN_I_COLUMN = r22_VALUE_IN_I_COLUMN;
}



public BigDecimal getR22_BWP_AMOUNT() {
	return R22_BWP_AMOUNT;
}



public void setR22_BWP_AMOUNT(BigDecimal r22_BWP_AMOUNT) {
	R22_BWP_AMOUNT = r22_BWP_AMOUNT;
}



public BigDecimal getR23_VALUE_IN_I_COLUMN() {
	return R23_VALUE_IN_I_COLUMN;
}



public void setR23_VALUE_IN_I_COLUMN(BigDecimal r23_VALUE_IN_I_COLUMN) {
	R23_VALUE_IN_I_COLUMN = r23_VALUE_IN_I_COLUMN;
}



public BigDecimal getR23_BWP_AMOUNT() {
	return R23_BWP_AMOUNT;
}



public void setR23_BWP_AMOUNT(BigDecimal r23_BWP_AMOUNT) {
	R23_BWP_AMOUNT = r23_BWP_AMOUNT;
}



public BigDecimal getR24_VALUE_IN_I_COLUMN() {
	return R24_VALUE_IN_I_COLUMN;
}



public void setR24_VALUE_IN_I_COLUMN(BigDecimal r24_VALUE_IN_I_COLUMN) {
	R24_VALUE_IN_I_COLUMN = r24_VALUE_IN_I_COLUMN;
}



public BigDecimal getR24_BWP_AMOUNT() {
	return R24_BWP_AMOUNT;
}



public void setR24_BWP_AMOUNT(BigDecimal r24_BWP_AMOUNT) {
	R24_BWP_AMOUNT = r24_BWP_AMOUNT;
}



public BigDecimal getR25_VALUE_IN_I_COLUMN() {
	return R25_VALUE_IN_I_COLUMN;
}



public void setR25_VALUE_IN_I_COLUMN(BigDecimal r25_VALUE_IN_I_COLUMN) {
	R25_VALUE_IN_I_COLUMN = r25_VALUE_IN_I_COLUMN;
}



public BigDecimal getR25_BWP_AMOUNT() {
	return R25_BWP_AMOUNT;
}



public void setR25_BWP_AMOUNT(BigDecimal r25_BWP_AMOUNT) {
	R25_BWP_AMOUNT = r25_BWP_AMOUNT;
}



public BigDecimal getR26_VALUE_IN_I_COLUMN() {
	return R26_VALUE_IN_I_COLUMN;
}



public void setR26_VALUE_IN_I_COLUMN(BigDecimal r26_VALUE_IN_I_COLUMN) {
	R26_VALUE_IN_I_COLUMN = r26_VALUE_IN_I_COLUMN;
}



public BigDecimal getR26_BWP_AMOUNT() {
	return R26_BWP_AMOUNT;
}



public void setR26_BWP_AMOUNT(BigDecimal r26_BWP_AMOUNT) {
	R26_BWP_AMOUNT = r26_BWP_AMOUNT;
}



public BigDecimal getR27_VALUE_IN_I_COLUMN() {
	return R27_VALUE_IN_I_COLUMN;
}



public void setR27_VALUE_IN_I_COLUMN(BigDecimal r27_VALUE_IN_I_COLUMN) {
	R27_VALUE_IN_I_COLUMN = r27_VALUE_IN_I_COLUMN;
}



public BigDecimal getR27_BWP_AMOUNT() {
	return R27_BWP_AMOUNT;
}



public void setR27_BWP_AMOUNT(BigDecimal r27_BWP_AMOUNT) {
	R27_BWP_AMOUNT = r27_BWP_AMOUNT;
}



public BigDecimal getR28_VALUE_IN_I_COLUMN() {
	return R28_VALUE_IN_I_COLUMN;
}



public void setR28_VALUE_IN_I_COLUMN(BigDecimal r28_VALUE_IN_I_COLUMN) {
	R28_VALUE_IN_I_COLUMN = r28_VALUE_IN_I_COLUMN;
}



public BigDecimal getR28_BWP_AMOUNT() {
	return R28_BWP_AMOUNT;
}



public void setR28_BWP_AMOUNT(BigDecimal r28_BWP_AMOUNT) {
	R28_BWP_AMOUNT = r28_BWP_AMOUNT;
}



public BigDecimal getR29_VALUE_IN_I_COLUMN() {
	return R29_VALUE_IN_I_COLUMN;
}



public void setR29_VALUE_IN_I_COLUMN(BigDecimal r29_VALUE_IN_I_COLUMN) {
	R29_VALUE_IN_I_COLUMN = r29_VALUE_IN_I_COLUMN;
}



public BigDecimal getR29_BWP_AMOUNT() {
	return R29_BWP_AMOUNT;
}



public void setR29_BWP_AMOUNT(BigDecimal r29_BWP_AMOUNT) {
	R29_BWP_AMOUNT = r29_BWP_AMOUNT;
}



public BigDecimal getR30_VALUE_IN_I_COLUMN() {
	return R30_VALUE_IN_I_COLUMN;
}



public void setR30_VALUE_IN_I_COLUMN(BigDecimal r30_VALUE_IN_I_COLUMN) {
	R30_VALUE_IN_I_COLUMN = r30_VALUE_IN_I_COLUMN;
}



public BigDecimal getR30_BWP_AMOUNT() {
	return R30_BWP_AMOUNT;
}



public void setR30_BWP_AMOUNT(BigDecimal r30_BWP_AMOUNT) {
	R30_BWP_AMOUNT = r30_BWP_AMOUNT;
}



public BigDecimal getR31_VALUE_IN_I_COLUMN() {
	return R31_VALUE_IN_I_COLUMN;
}



public void setR31_VALUE_IN_I_COLUMN(BigDecimal r31_VALUE_IN_I_COLUMN) {
	R31_VALUE_IN_I_COLUMN = r31_VALUE_IN_I_COLUMN;
}



public BigDecimal getR31_BWP_AMOUNT() {
	return R31_BWP_AMOUNT;
}



public void setR31_BWP_AMOUNT(BigDecimal r31_BWP_AMOUNT) {
	R31_BWP_AMOUNT = r31_BWP_AMOUNT;
}



public BigDecimal getR32_VALUE_IN_I_COLUMN() {
	return R32_VALUE_IN_I_COLUMN;
}



public void setR32_VALUE_IN_I_COLUMN(BigDecimal r32_VALUE_IN_I_COLUMN) {
	R32_VALUE_IN_I_COLUMN = r32_VALUE_IN_I_COLUMN;
}



public BigDecimal getR32_BWP_AMOUNT() {
	return R32_BWP_AMOUNT;
}



public void setR32_BWP_AMOUNT(BigDecimal r32_BWP_AMOUNT) {
	R32_BWP_AMOUNT = r32_BWP_AMOUNT;
}



public BigDecimal getR33_VALUE_IN_I_COLUMN() {
	return R33_VALUE_IN_I_COLUMN;
}



public void setR33_VALUE_IN_I_COLUMN(BigDecimal r33_VALUE_IN_I_COLUMN) {
	R33_VALUE_IN_I_COLUMN = r33_VALUE_IN_I_COLUMN;
}



public BigDecimal getR33_BWP_AMOUNT() {
	return R33_BWP_AMOUNT;
}



public void setR33_BWP_AMOUNT(BigDecimal r33_BWP_AMOUNT) {
	R33_BWP_AMOUNT = r33_BWP_AMOUNT;
}



public BigDecimal getR34_VALUE_IN_I_COLUMN() {
	return R34_VALUE_IN_I_COLUMN;
}



public void setR34_VALUE_IN_I_COLUMN(BigDecimal r34_VALUE_IN_I_COLUMN) {
	R34_VALUE_IN_I_COLUMN = r34_VALUE_IN_I_COLUMN;
}



public BigDecimal getR34_BWP_AMOUNT() {
	return R34_BWP_AMOUNT;
}



public void setR34_BWP_AMOUNT(BigDecimal r34_BWP_AMOUNT) {
	R34_BWP_AMOUNT = r34_BWP_AMOUNT;
}



public BigDecimal getR35_VALUE_IN_I_COLUMN() {
	return R35_VALUE_IN_I_COLUMN;
}



public void setR35_VALUE_IN_I_COLUMN(BigDecimal r35_VALUE_IN_I_COLUMN) {
	R35_VALUE_IN_I_COLUMN = r35_VALUE_IN_I_COLUMN;
}



public BigDecimal getR35_BWP_AMOUNT() {
	return R35_BWP_AMOUNT;
}



public void setR35_BWP_AMOUNT(BigDecimal r35_BWP_AMOUNT) {
	R35_BWP_AMOUNT = r35_BWP_AMOUNT;
}



public BigDecimal getR36_VALUE_IN_I_COLUMN() {
	return R36_VALUE_IN_I_COLUMN;
}



public void setR36_VALUE_IN_I_COLUMN(BigDecimal r36_VALUE_IN_I_COLUMN) {
	R36_VALUE_IN_I_COLUMN = r36_VALUE_IN_I_COLUMN;
}



public BigDecimal getR36_BWP_AMOUNT() {
	return R36_BWP_AMOUNT;
}



public void setR36_BWP_AMOUNT(BigDecimal r36_BWP_AMOUNT) {
	R36_BWP_AMOUNT = r36_BWP_AMOUNT;
}



public BigDecimal getR37_VALUE_IN_I_COLUMN() {
	return R37_VALUE_IN_I_COLUMN;
}



public void setR37_VALUE_IN_I_COLUMN(BigDecimal r37_VALUE_IN_I_COLUMN) {
	R37_VALUE_IN_I_COLUMN = r37_VALUE_IN_I_COLUMN;
}



public BigDecimal getR37_BWP_AMOUNT() {
	return R37_BWP_AMOUNT;
}



public void setR37_BWP_AMOUNT(BigDecimal r37_BWP_AMOUNT) {
	R37_BWP_AMOUNT = r37_BWP_AMOUNT;
}



public BigDecimal getR41_RISK_ASSETS() {
	return R41_RISK_ASSETS;
}



public void setR41_RISK_ASSETS(BigDecimal r41_RISK_ASSETS) {
	R41_RISK_ASSETS = r41_RISK_ASSETS;
}



public BigDecimal getR42_RISK_ASSETS() {
	return R42_RISK_ASSETS;
}



public void setR42_RISK_ASSETS(BigDecimal r42_RISK_ASSETS) {
	R42_RISK_ASSETS = r42_RISK_ASSETS;
}



public BigDecimal getR43_RISK_ASSETS() {
	return R43_RISK_ASSETS;
}



public void setR43_RISK_ASSETS(BigDecimal r43_RISK_ASSETS) {
	R43_RISK_ASSETS = r43_RISK_ASSETS;
}



public BigDecimal getR44_RISK_ASSETS() {
	return R44_RISK_ASSETS;
}



public void setR44_RISK_ASSETS(BigDecimal r44_RISK_ASSETS) {
	R44_RISK_ASSETS = r44_RISK_ASSETS;
}



public BigDecimal getR46_PERCENTAGE_CF_TO_RWA() {
	return R46_PERCENTAGE_CF_TO_RWA;
}



public void setR46_PERCENTAGE_CF_TO_RWA(BigDecimal r46_PERCENTAGE_CF_TO_RWA) {
	R46_PERCENTAGE_CF_TO_RWA = r46_PERCENTAGE_CF_TO_RWA;
}



public BigDecimal getR47_PERCENTAGE_TIER1() {
	return R47_PERCENTAGE_TIER1;
}



public void setR47_PERCENTAGE_TIER1(BigDecimal r47_PERCENTAGE_TIER1) {
	R47_PERCENTAGE_TIER1 = r47_PERCENTAGE_TIER1;
}



public BigDecimal getR48_PERCENTAGE_TIER2() {
	return R48_PERCENTAGE_TIER2;
}



public void setR48_PERCENTAGE_TIER2(BigDecimal r48_PERCENTAGE_TIER2) {
	R48_PERCENTAGE_TIER2 = r48_PERCENTAGE_TIER2;
}



public BigDecimal getR52_DOMESTIC_RISK_ASSETS() {
	return R52_DOMESTIC_RISK_ASSETS;
}



public void setR52_DOMESTIC_RISK_ASSETS(BigDecimal r52_DOMESTIC_RISK_ASSETS) {
	R52_DOMESTIC_RISK_ASSETS = r52_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR52_FOREIGN_RISK_ASSETS() {
	return R52_FOREIGN_RISK_ASSETS;
}



public void setR52_FOREIGN_RISK_ASSETS(BigDecimal r52_FOREIGN_RISK_ASSETS) {
	R52_FOREIGN_RISK_ASSETS = r52_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR52_TOTAL_RISK_ASSETS() {
	return R52_TOTAL_RISK_ASSETS;
}



public void setR52_TOTAL_RISK_ASSETS(BigDecimal r52_TOTAL_RISK_ASSETS) {
	R52_TOTAL_RISK_ASSETS = r52_TOTAL_RISK_ASSETS;
}



public BigDecimal getR53_DOMESTIC_RISK_ASSETS() {
	return R53_DOMESTIC_RISK_ASSETS;
}



public void setR53_DOMESTIC_RISK_ASSETS(BigDecimal r53_DOMESTIC_RISK_ASSETS) {
	R53_DOMESTIC_RISK_ASSETS = r53_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR53_FOREIGN_RISK_ASSETS() {
	return R53_FOREIGN_RISK_ASSETS;
}



public void setR53_FOREIGN_RISK_ASSETS(BigDecimal r53_FOREIGN_RISK_ASSETS) {
	R53_FOREIGN_RISK_ASSETS = r53_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR53_TOTAL_RISK_ASSETS() {
	return R53_TOTAL_RISK_ASSETS;
}



public void setR53_TOTAL_RISK_ASSETS(BigDecimal r53_TOTAL_RISK_ASSETS) {
	R53_TOTAL_RISK_ASSETS = r53_TOTAL_RISK_ASSETS;
}



public BigDecimal getR54_DOMESTIC_RISK_ASSETS() {
	return R54_DOMESTIC_RISK_ASSETS;
}



public void setR54_DOMESTIC_RISK_ASSETS(BigDecimal r54_DOMESTIC_RISK_ASSETS) {
	R54_DOMESTIC_RISK_ASSETS = r54_DOMESTIC_RISK_ASSETS;
}



public BigDecimal getR54_FOREIGN_RISK_ASSETS() {
	return R54_FOREIGN_RISK_ASSETS;
}



public void setR54_FOREIGN_RISK_ASSETS(BigDecimal r54_FOREIGN_RISK_ASSETS) {
	R54_FOREIGN_RISK_ASSETS = r54_FOREIGN_RISK_ASSETS;
}



public BigDecimal getR54_TOTAL_RISK_ASSETS() {
	return R54_TOTAL_RISK_ASSETS;
}



public void setR54_TOTAL_RISK_ASSETS(BigDecimal r54_TOTAL_RISK_ASSETS) {
	R54_TOTAL_RISK_ASSETS = r54_TOTAL_RISK_ASSETS;
}



public TIER_1_2_CFS_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}








}
