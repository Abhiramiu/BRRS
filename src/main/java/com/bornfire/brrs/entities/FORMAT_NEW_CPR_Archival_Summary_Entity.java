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
@Table(name = "BRRS_FORMAT_NEW_CPR_ARCHIVALTABLE_SUMMARY")
public class FORMAT_NEW_CPR_Archival_Summary_Entity {


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

//-------- R5 --------
private String R5_LINE_NO;
private String R5_PARAMETERS;
private String R5_AMOUNT;

//-------- R6 --------
private String R6_LINE_NO;
private String R6_PARAMETERS;
private String R6_AMOUNT;

//-------- R7 --------
private String R7_LINE_NO;
private String R7_PARAMETERS;
private BigDecimal R7_AMOUNT;

//-------- R8 --------
private String R8_LINE_NO;
private String R8_PARAMETERS;
private BigDecimal R8_AMOUNT;

//-------- R9 --------
private String R9_LINE_NO;
private String R9_PARAMETERS;
private BigDecimal R9_AMOUNT;

//-------- R10 --------
private String R10_LINE_NO;
private String R10_PARAMETERS;
private BigDecimal R10_AMOUNT;

//-------- R11 --------
private String R11_LINE_NO;
private String R11_PARAMETERS;
private BigDecimal R11_AMOUNT;

//-------- R12 --------
private String R12_LINE_NO;
private String R12_PARAMETERS;
private BigDecimal R12_AMOUNT;

//-------- R13 --------
private String R13_LINE_NO;
private String R13_PARAMETERS;
private BigDecimal R13_AMOUNT;

//-------- R14 --------
private String R14_LINE_NO;
private String R14_PARAMETERS;
private BigDecimal R14_AMOUNT;

//-------- R15 --------
private String R15_LINE_NO;
private String R15_PARAMETERS;
private BigDecimal R15_AMOUNT;

//-------- R16 --------
private String R16_LINE_NO;
private String R16_PARAMETERS;
private BigDecimal R16_AMOUNT;

//-------- R17 --------
private String R17_LINE_NO;
private String R17_PARAMETERS;
private BigDecimal R17_AMOUNT;

//-------- R18 --------
private String R18_LINE_NO;
private String R18_PARAMETERS;
private BigDecimal R18_AMOUNT;

//-------- R19 --------
private String R19_LINE_NO;
private String R19_PARAMETERS;
private BigDecimal R19_AMOUNT;

//-------- R20 --------
private String R20_LINE_NO;
private String R20_PARAMETERS;
private BigDecimal R20_AMOUNT;

//-------- R21 --------
private String R21_LINE_NO;
private String R21_PARAMETERS;
private BigDecimal R21_AMOUNT;

//-------- R22 --------
private String R22_LINE_NO;
private String R22_PARAMETERS;
private BigDecimal R22_AMOUNT;

//-------- R23 --------
private String R23_LINE_NO;
private String R23_PARAMETERS;
private BigDecimal R23_AMOUNT;

//-------- R24 --------
private String R24_LINE_NO;
private String R24_PARAMETERS;
private BigDecimal R24_AMOUNT;

//-------- R25 --------
private String R25_LINE_NO;
private String R25_PARAMETERS;
private BigDecimal R25_AMOUNT;

//-------- R26 --------
private String R26_LINE_NO;
private String R26_PARAMETERS;
private BigDecimal R26_AMOUNT;

//-------- R27 --------
private String R27_LINE_NO;
private String R27_PARAMETERS;
private BigDecimal R27_AMOUNT;

//-------- R28 --------
private String R28_LINE_NO;
private String R28_PARAMETERS;
private BigDecimal R28_AMOUNT;

//-------- R29 --------
private String R29_LINE_NO;
private String R29_PARAMETERS;
private BigDecimal R29_AMOUNT;

//-------- R30 --------
private String R30_LINE_NO;
private String R30_PARAMETERS;
private BigDecimal R30_AMOUNT;

//-------- R31 --------
private String R31_LINE_NO;
private String R31_PARAMETERS;
private BigDecimal R31_AMOUNT;

//-------- R32 --------
private String R32_LINE_NO;
private String R32_PARAMETERS;
private BigDecimal R32_AMOUNT;

//-------- R33 --------
private String R33_LINE_NO;
private String R33_PARAMETERS;
private BigDecimal R33_AMOUNT;

//-------- R34 --------
private String R34_LINE_NO;
private String R34_PARAMETERS;
private BigDecimal R34_AMOUNT;

//-------- R35 --------
private String R35_LINE_NO;
private String R35_PARAMETERS;
private BigDecimal R35_AMOUNT;

//-------- R36 --------
private String R36_LINE_NO;
private String R36_PARAMETERS;
private BigDecimal R36_AMOUNT;

//-------- R37 --------
private String R37_LINE_NO;
private String R37_PARAMETERS;
private BigDecimal R37_AMOUNT;

//-------- R38 --------
private String R38_LINE_NO;
private String R38_PARAMETERS;
private BigDecimal R38_AMOUNT;

//-------- R39 --------
private String R39_LINE_NO;
private String R39_PARAMETERS;
private BigDecimal R39_AMOUNT;

//-------- R40 --------
private String R40_LINE_NO;
private String R40_PARAMETERS;
private BigDecimal R40_AMOUNT;

//-------- R41 --------
private String R41_LINE_NO;
private String R41_PARAMETERS;
private BigDecimal R41_AMOUNT;

//-------- R42 --------
private String R42_LINE_NO;
private String R42_PARAMETERS;
private BigDecimal R42_AMOUNT;

//-------- R43 --------
private String R43_LINE_NO;
private String R43_PARAMETERS;
private BigDecimal R43_AMOUNT;

//-------- R44 --------
private String R44_LINE_NO;
private String R44_PARAMETERS;
private BigDecimal R44_AMOUNT;

//-------- R45 --------
private String R45_LINE_NO;
private String R45_PARAMETERS;
private BigDecimal R45_AMOUNT;

//-------- R46 --------
private String R46_LINE_NO;
private String R46_PARAMETERS;
private BigDecimal R46_AMOUNT;

//-------- R47 --------
private String R47_LINE_NO;
private String R47_PARAMETERS;
private BigDecimal R47_AMOUNT;

//-------- R48 --------
private String R48_LINE_NO;
private String R48_PARAMETERS;
private BigDecimal R48_AMOUNT;

//-------- R49 --------
private String R49_LINE_NO;
private String R49_PARAMETERS;
private BigDecimal R49_AMOUNT;

//-------- R50 --------
private String R50_LINE_NO;
private String R50_PARAMETERS;
private BigDecimal R50_AMOUNT;
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
public String getR5_LINE_NO() {
	return R5_LINE_NO;
}
public void setR5_LINE_NO(String r5_LINE_NO) {
	R5_LINE_NO = r5_LINE_NO;
}
public String getR5_PARAMETERS() {
	return R5_PARAMETERS;
}
public void setR5_PARAMETERS(String r5_PARAMETERS) {
	R5_PARAMETERS = r5_PARAMETERS;
}
public String getR5_AMOUNT() {
	return R5_AMOUNT;
}
public void setR5_AMOUNT(String r5_AMOUNT) {
	R5_AMOUNT = r5_AMOUNT;
}
public String getR6_LINE_NO() {
	return R6_LINE_NO;
}
public void setR6_LINE_NO(String r6_LINE_NO) {
	R6_LINE_NO = r6_LINE_NO;
}
public String getR6_PARAMETERS() {
	return R6_PARAMETERS;
}
public void setR6_PARAMETERS(String r6_PARAMETERS) {
	R6_PARAMETERS = r6_PARAMETERS;
}
public String getR6_AMOUNT() {
	return R6_AMOUNT;
}
public void setR6_AMOUNT(String r6_AMOUNT) {
	R6_AMOUNT = r6_AMOUNT;
}
public String getR7_LINE_NO() {
	return R7_LINE_NO;
}
public void setR7_LINE_NO(String r7_LINE_NO) {
	R7_LINE_NO = r7_LINE_NO;
}
public String getR7_PARAMETERS() {
	return R7_PARAMETERS;
}
public void setR7_PARAMETERS(String r7_PARAMETERS) {
	R7_PARAMETERS = r7_PARAMETERS;
}
public BigDecimal getR7_AMOUNT() {
	return R7_AMOUNT;
}
public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
	R7_AMOUNT = r7_AMOUNT;
}
public String getR8_LINE_NO() {
	return R8_LINE_NO;
}
public void setR8_LINE_NO(String r8_LINE_NO) {
	R8_LINE_NO = r8_LINE_NO;
}
public String getR8_PARAMETERS() {
	return R8_PARAMETERS;
}
public void setR8_PARAMETERS(String r8_PARAMETERS) {
	R8_PARAMETERS = r8_PARAMETERS;
}
public BigDecimal getR8_AMOUNT() {
	return R8_AMOUNT;
}
public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
	R8_AMOUNT = r8_AMOUNT;
}
public String getR9_LINE_NO() {
	return R9_LINE_NO;
}
public void setR9_LINE_NO(String r9_LINE_NO) {
	R9_LINE_NO = r9_LINE_NO;
}
public String getR9_PARAMETERS() {
	return R9_PARAMETERS;
}
public void setR9_PARAMETERS(String r9_PARAMETERS) {
	R9_PARAMETERS = r9_PARAMETERS;
}
public BigDecimal getR9_AMOUNT() {
	return R9_AMOUNT;
}
public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
	R9_AMOUNT = r9_AMOUNT;
}
public String getR10_LINE_NO() {
	return R10_LINE_NO;
}
public void setR10_LINE_NO(String r10_LINE_NO) {
	R10_LINE_NO = r10_LINE_NO;
}
public String getR10_PARAMETERS() {
	return R10_PARAMETERS;
}
public void setR10_PARAMETERS(String r10_PARAMETERS) {
	R10_PARAMETERS = r10_PARAMETERS;
}
public BigDecimal getR10_AMOUNT() {
	return R10_AMOUNT;
}
public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
	R10_AMOUNT = r10_AMOUNT;
}
public String getR11_LINE_NO() {
	return R11_LINE_NO;
}
public void setR11_LINE_NO(String r11_LINE_NO) {
	R11_LINE_NO = r11_LINE_NO;
}
public String getR11_PARAMETERS() {
	return R11_PARAMETERS;
}
public void setR11_PARAMETERS(String r11_PARAMETERS) {
	R11_PARAMETERS = r11_PARAMETERS;
}
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public String getR12_LINE_NO() {
	return R12_LINE_NO;
}
public void setR12_LINE_NO(String r12_LINE_NO) {
	R12_LINE_NO = r12_LINE_NO;
}
public String getR12_PARAMETERS() {
	return R12_PARAMETERS;
}
public void setR12_PARAMETERS(String r12_PARAMETERS) {
	R12_PARAMETERS = r12_PARAMETERS;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public String getR13_LINE_NO() {
	return R13_LINE_NO;
}
public void setR13_LINE_NO(String r13_LINE_NO) {
	R13_LINE_NO = r13_LINE_NO;
}
public String getR13_PARAMETERS() {
	return R13_PARAMETERS;
}
public void setR13_PARAMETERS(String r13_PARAMETERS) {
	R13_PARAMETERS = r13_PARAMETERS;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public String getR14_LINE_NO() {
	return R14_LINE_NO;
}
public void setR14_LINE_NO(String r14_LINE_NO) {
	R14_LINE_NO = r14_LINE_NO;
}
public String getR14_PARAMETERS() {
	return R14_PARAMETERS;
}
public void setR14_PARAMETERS(String r14_PARAMETERS) {
	R14_PARAMETERS = r14_PARAMETERS;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public String getR15_LINE_NO() {
	return R15_LINE_NO;
}
public void setR15_LINE_NO(String r15_LINE_NO) {
	R15_LINE_NO = r15_LINE_NO;
}
public String getR15_PARAMETERS() {
	return R15_PARAMETERS;
}
public void setR15_PARAMETERS(String r15_PARAMETERS) {
	R15_PARAMETERS = r15_PARAMETERS;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public String getR16_LINE_NO() {
	return R16_LINE_NO;
}
public void setR16_LINE_NO(String r16_LINE_NO) {
	R16_LINE_NO = r16_LINE_NO;
}
public String getR16_PARAMETERS() {
	return R16_PARAMETERS;
}
public void setR16_PARAMETERS(String r16_PARAMETERS) {
	R16_PARAMETERS = r16_PARAMETERS;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public String getR17_LINE_NO() {
	return R17_LINE_NO;
}
public void setR17_LINE_NO(String r17_LINE_NO) {
	R17_LINE_NO = r17_LINE_NO;
}
public String getR17_PARAMETERS() {
	return R17_PARAMETERS;
}
public void setR17_PARAMETERS(String r17_PARAMETERS) {
	R17_PARAMETERS = r17_PARAMETERS;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public String getR18_LINE_NO() {
	return R18_LINE_NO;
}
public void setR18_LINE_NO(String r18_LINE_NO) {
	R18_LINE_NO = r18_LINE_NO;
}
public String getR18_PARAMETERS() {
	return R18_PARAMETERS;
}
public void setR18_PARAMETERS(String r18_PARAMETERS) {
	R18_PARAMETERS = r18_PARAMETERS;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public String getR19_LINE_NO() {
	return R19_LINE_NO;
}
public void setR19_LINE_NO(String r19_LINE_NO) {
	R19_LINE_NO = r19_LINE_NO;
}
public String getR19_PARAMETERS() {
	return R19_PARAMETERS;
}
public void setR19_PARAMETERS(String r19_PARAMETERS) {
	R19_PARAMETERS = r19_PARAMETERS;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public String getR20_LINE_NO() {
	return R20_LINE_NO;
}
public void setR20_LINE_NO(String r20_LINE_NO) {
	R20_LINE_NO = r20_LINE_NO;
}
public String getR20_PARAMETERS() {
	return R20_PARAMETERS;
}
public void setR20_PARAMETERS(String r20_PARAMETERS) {
	R20_PARAMETERS = r20_PARAMETERS;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public String getR21_LINE_NO() {
	return R21_LINE_NO;
}
public void setR21_LINE_NO(String r21_LINE_NO) {
	R21_LINE_NO = r21_LINE_NO;
}
public String getR21_PARAMETERS() {
	return R21_PARAMETERS;
}
public void setR21_PARAMETERS(String r21_PARAMETERS) {
	R21_PARAMETERS = r21_PARAMETERS;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public String getR22_LINE_NO() {
	return R22_LINE_NO;
}
public void setR22_LINE_NO(String r22_LINE_NO) {
	R22_LINE_NO = r22_LINE_NO;
}
public String getR22_PARAMETERS() {
	return R22_PARAMETERS;
}
public void setR22_PARAMETERS(String r22_PARAMETERS) {
	R22_PARAMETERS = r22_PARAMETERS;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public String getR23_LINE_NO() {
	return R23_LINE_NO;
}
public void setR23_LINE_NO(String r23_LINE_NO) {
	R23_LINE_NO = r23_LINE_NO;
}
public String getR23_PARAMETERS() {
	return R23_PARAMETERS;
}
public void setR23_PARAMETERS(String r23_PARAMETERS) {
	R23_PARAMETERS = r23_PARAMETERS;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public String getR24_LINE_NO() {
	return R24_LINE_NO;
}
public void setR24_LINE_NO(String r24_LINE_NO) {
	R24_LINE_NO = r24_LINE_NO;
}
public String getR24_PARAMETERS() {
	return R24_PARAMETERS;
}
public void setR24_PARAMETERS(String r24_PARAMETERS) {
	R24_PARAMETERS = r24_PARAMETERS;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public String getR25_LINE_NO() {
	return R25_LINE_NO;
}
public void setR25_LINE_NO(String r25_LINE_NO) {
	R25_LINE_NO = r25_LINE_NO;
}
public String getR25_PARAMETERS() {
	return R25_PARAMETERS;
}
public void setR25_PARAMETERS(String r25_PARAMETERS) {
	R25_PARAMETERS = r25_PARAMETERS;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public String getR26_LINE_NO() {
	return R26_LINE_NO;
}
public void setR26_LINE_NO(String r26_LINE_NO) {
	R26_LINE_NO = r26_LINE_NO;
}
public String getR26_PARAMETERS() {
	return R26_PARAMETERS;
}
public void setR26_PARAMETERS(String r26_PARAMETERS) {
	R26_PARAMETERS = r26_PARAMETERS;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public String getR27_LINE_NO() {
	return R27_LINE_NO;
}
public void setR27_LINE_NO(String r27_LINE_NO) {
	R27_LINE_NO = r27_LINE_NO;
}
public String getR27_PARAMETERS() {
	return R27_PARAMETERS;
}
public void setR27_PARAMETERS(String r27_PARAMETERS) {
	R27_PARAMETERS = r27_PARAMETERS;
}
public BigDecimal getR27_AMOUNT() {
	return R27_AMOUNT;
}
public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
	R27_AMOUNT = r27_AMOUNT;
}
public String getR28_LINE_NO() {
	return R28_LINE_NO;
}
public void setR28_LINE_NO(String r28_LINE_NO) {
	R28_LINE_NO = r28_LINE_NO;
}
public String getR28_PARAMETERS() {
	return R28_PARAMETERS;
}
public void setR28_PARAMETERS(String r28_PARAMETERS) {
	R28_PARAMETERS = r28_PARAMETERS;
}
public BigDecimal getR28_AMOUNT() {
	return R28_AMOUNT;
}
public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
	R28_AMOUNT = r28_AMOUNT;
}
public String getR29_LINE_NO() {
	return R29_LINE_NO;
}
public void setR29_LINE_NO(String r29_LINE_NO) {
	R29_LINE_NO = r29_LINE_NO;
}
public String getR29_PARAMETERS() {
	return R29_PARAMETERS;
}
public void setR29_PARAMETERS(String r29_PARAMETERS) {
	R29_PARAMETERS = r29_PARAMETERS;
}
public BigDecimal getR29_AMOUNT() {
	return R29_AMOUNT;
}
public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
	R29_AMOUNT = r29_AMOUNT;
}
public String getR30_LINE_NO() {
	return R30_LINE_NO;
}
public void setR30_LINE_NO(String r30_LINE_NO) {
	R30_LINE_NO = r30_LINE_NO;
}
public String getR30_PARAMETERS() {
	return R30_PARAMETERS;
}
public void setR30_PARAMETERS(String r30_PARAMETERS) {
	R30_PARAMETERS = r30_PARAMETERS;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public String getR31_LINE_NO() {
	return R31_LINE_NO;
}
public void setR31_LINE_NO(String r31_LINE_NO) {
	R31_LINE_NO = r31_LINE_NO;
}
public String getR31_PARAMETERS() {
	return R31_PARAMETERS;
}
public void setR31_PARAMETERS(String r31_PARAMETERS) {
	R31_PARAMETERS = r31_PARAMETERS;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public String getR32_LINE_NO() {
	return R32_LINE_NO;
}
public void setR32_LINE_NO(String r32_LINE_NO) {
	R32_LINE_NO = r32_LINE_NO;
}
public String getR32_PARAMETERS() {
	return R32_PARAMETERS;
}
public void setR32_PARAMETERS(String r32_PARAMETERS) {
	R32_PARAMETERS = r32_PARAMETERS;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public String getR33_LINE_NO() {
	return R33_LINE_NO;
}
public void setR33_LINE_NO(String r33_LINE_NO) {
	R33_LINE_NO = r33_LINE_NO;
}
public String getR33_PARAMETERS() {
	return R33_PARAMETERS;
}
public void setR33_PARAMETERS(String r33_PARAMETERS) {
	R33_PARAMETERS = r33_PARAMETERS;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public String getR34_LINE_NO() {
	return R34_LINE_NO;
}
public void setR34_LINE_NO(String r34_LINE_NO) {
	R34_LINE_NO = r34_LINE_NO;
}
public String getR34_PARAMETERS() {
	return R34_PARAMETERS;
}
public void setR34_PARAMETERS(String r34_PARAMETERS) {
	R34_PARAMETERS = r34_PARAMETERS;
}
public BigDecimal getR34_AMOUNT() {
	return R34_AMOUNT;
}
public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
	R34_AMOUNT = r34_AMOUNT;
}
public String getR35_LINE_NO() {
	return R35_LINE_NO;
}
public void setR35_LINE_NO(String r35_LINE_NO) {
	R35_LINE_NO = r35_LINE_NO;
}
public String getR35_PARAMETERS() {
	return R35_PARAMETERS;
}
public void setR35_PARAMETERS(String r35_PARAMETERS) {
	R35_PARAMETERS = r35_PARAMETERS;
}
public BigDecimal getR35_AMOUNT() {
	return R35_AMOUNT;
}
public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
	R35_AMOUNT = r35_AMOUNT;
}
public String getR36_LINE_NO() {
	return R36_LINE_NO;
}
public void setR36_LINE_NO(String r36_LINE_NO) {
	R36_LINE_NO = r36_LINE_NO;
}
public String getR36_PARAMETERS() {
	return R36_PARAMETERS;
}
public void setR36_PARAMETERS(String r36_PARAMETERS) {
	R36_PARAMETERS = r36_PARAMETERS;
}
public BigDecimal getR36_AMOUNT() {
	return R36_AMOUNT;
}
public void setR36_AMOUNT(BigDecimal r36_AMOUNT) {
	R36_AMOUNT = r36_AMOUNT;
}
public String getR37_LINE_NO() {
	return R37_LINE_NO;
}
public void setR37_LINE_NO(String r37_LINE_NO) {
	R37_LINE_NO = r37_LINE_NO;
}
public String getR37_PARAMETERS() {
	return R37_PARAMETERS;
}
public void setR37_PARAMETERS(String r37_PARAMETERS) {
	R37_PARAMETERS = r37_PARAMETERS;
}
public BigDecimal getR37_AMOUNT() {
	return R37_AMOUNT;
}
public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
	R37_AMOUNT = r37_AMOUNT;
}
public String getR38_LINE_NO() {
	return R38_LINE_NO;
}
public void setR38_LINE_NO(String r38_LINE_NO) {
	R38_LINE_NO = r38_LINE_NO;
}
public String getR38_PARAMETERS() {
	return R38_PARAMETERS;
}
public void setR38_PARAMETERS(String r38_PARAMETERS) {
	R38_PARAMETERS = r38_PARAMETERS;
}
public BigDecimal getR38_AMOUNT() {
	return R38_AMOUNT;
}
public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
	R38_AMOUNT = r38_AMOUNT;
}
public String getR39_LINE_NO() {
	return R39_LINE_NO;
}
public void setR39_LINE_NO(String r39_LINE_NO) {
	R39_LINE_NO = r39_LINE_NO;
}
public String getR39_PARAMETERS() {
	return R39_PARAMETERS;
}
public void setR39_PARAMETERS(String r39_PARAMETERS) {
	R39_PARAMETERS = r39_PARAMETERS;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public String getR40_LINE_NO() {
	return R40_LINE_NO;
}
public void setR40_LINE_NO(String r40_LINE_NO) {
	R40_LINE_NO = r40_LINE_NO;
}
public String getR40_PARAMETERS() {
	return R40_PARAMETERS;
}
public void setR40_PARAMETERS(String r40_PARAMETERS) {
	R40_PARAMETERS = r40_PARAMETERS;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public String getR41_LINE_NO() {
	return R41_LINE_NO;
}
public void setR41_LINE_NO(String r41_LINE_NO) {
	R41_LINE_NO = r41_LINE_NO;
}
public String getR41_PARAMETERS() {
	return R41_PARAMETERS;
}
public void setR41_PARAMETERS(String r41_PARAMETERS) {
	R41_PARAMETERS = r41_PARAMETERS;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public String getR42_LINE_NO() {
	return R42_LINE_NO;
}
public void setR42_LINE_NO(String r42_LINE_NO) {
	R42_LINE_NO = r42_LINE_NO;
}
public String getR42_PARAMETERS() {
	return R42_PARAMETERS;
}
public void setR42_PARAMETERS(String r42_PARAMETERS) {
	R42_PARAMETERS = r42_PARAMETERS;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public String getR43_LINE_NO() {
	return R43_LINE_NO;
}
public void setR43_LINE_NO(String r43_LINE_NO) {
	R43_LINE_NO = r43_LINE_NO;
}
public String getR43_PARAMETERS() {
	return R43_PARAMETERS;
}
public void setR43_PARAMETERS(String r43_PARAMETERS) {
	R43_PARAMETERS = r43_PARAMETERS;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public String getR44_LINE_NO() {
	return R44_LINE_NO;
}
public void setR44_LINE_NO(String r44_LINE_NO) {
	R44_LINE_NO = r44_LINE_NO;
}
public String getR44_PARAMETERS() {
	return R44_PARAMETERS;
}
public void setR44_PARAMETERS(String r44_PARAMETERS) {
	R44_PARAMETERS = r44_PARAMETERS;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public String getR45_LINE_NO() {
	return R45_LINE_NO;
}
public void setR45_LINE_NO(String r45_LINE_NO) {
	R45_LINE_NO = r45_LINE_NO;
}
public String getR45_PARAMETERS() {
	return R45_PARAMETERS;
}
public void setR45_PARAMETERS(String r45_PARAMETERS) {
	R45_PARAMETERS = r45_PARAMETERS;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
}
public String getR46_LINE_NO() {
	return R46_LINE_NO;
}
public void setR46_LINE_NO(String r46_LINE_NO) {
	R46_LINE_NO = r46_LINE_NO;
}
public String getR46_PARAMETERS() {
	return R46_PARAMETERS;
}
public void setR46_PARAMETERS(String r46_PARAMETERS) {
	R46_PARAMETERS = r46_PARAMETERS;
}
public BigDecimal getR46_AMOUNT() {
	return R46_AMOUNT;
}
public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
	R46_AMOUNT = r46_AMOUNT;
}
public String getR47_LINE_NO() {
	return R47_LINE_NO;
}
public void setR47_LINE_NO(String r47_LINE_NO) {
	R47_LINE_NO = r47_LINE_NO;
}
public String getR47_PARAMETERS() {
	return R47_PARAMETERS;
}
public void setR47_PARAMETERS(String r47_PARAMETERS) {
	R47_PARAMETERS = r47_PARAMETERS;
}
public BigDecimal getR47_AMOUNT() {
	return R47_AMOUNT;
}
public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
	R47_AMOUNT = r47_AMOUNT;
}
public String getR48_LINE_NO() {
	return R48_LINE_NO;
}
public void setR48_LINE_NO(String r48_LINE_NO) {
	R48_LINE_NO = r48_LINE_NO;
}
public String getR48_PARAMETERS() {
	return R48_PARAMETERS;
}
public void setR48_PARAMETERS(String r48_PARAMETERS) {
	R48_PARAMETERS = r48_PARAMETERS;
}
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public String getR49_LINE_NO() {
	return R49_LINE_NO;
}
public void setR49_LINE_NO(String r49_LINE_NO) {
	R49_LINE_NO = r49_LINE_NO;
}
public String getR49_PARAMETERS() {
	return R49_PARAMETERS;
}
public void setR49_PARAMETERS(String r49_PARAMETERS) {
	R49_PARAMETERS = r49_PARAMETERS;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public String getR50_LINE_NO() {
	return R50_LINE_NO;
}
public void setR50_LINE_NO(String r50_LINE_NO) {
	R50_LINE_NO = r50_LINE_NO;
}
public String getR50_PARAMETERS() {
	return R50_PARAMETERS;
}
public void setR50_PARAMETERS(String r50_PARAMETERS) {
	R50_PARAMETERS = r50_PARAMETERS;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
}
public FORMAT_NEW_CPR_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}






}
