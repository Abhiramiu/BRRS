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
@Table(name = "BRRS_EXPOSURES_ARCHIVALTABLE_SUMMARY")
public class EXPOSURES_Archival_Summary_Entity {


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

private BigDecimal R4_AMOUNT;

private String R5_LINE_NO;
private String R5_BORROWER;
private String R5_AMOUNT;
private String R5_CAPITAL_FUNDS;

private BigDecimal R6_AMOUNT;
private BigDecimal R6_CAPITAL_FUNDS;

private BigDecimal R7_AMOUNT;
private BigDecimal R7_CAPITAL_FUNDS;

private BigDecimal R8_AMOUNT;
private BigDecimal R8_CAPITAL_FUNDS;

private BigDecimal R9_AMOUNT;
private BigDecimal R9_CAPITAL_FUNDS;

private BigDecimal R10_AMOUNT;
private BigDecimal R10_CAPITAL_FUNDS;

private BigDecimal R11_AMOUNT;
private BigDecimal R11_CAPITAL_FUNDS;

private BigDecimal R12_AMOUNT;
private BigDecimal R12_CAPITAL_FUNDS;

private BigDecimal R13_AMOUNT;
private BigDecimal R13_CAPITAL_FUNDS;

private BigDecimal R14_AMOUNT;
private BigDecimal R14_CAPITAL_FUNDS;

private BigDecimal R15_AMOUNT;
private BigDecimal R15_CAPITAL_FUNDS;

private BigDecimal R16_AMOUNT;
private BigDecimal R16_CAPITAL_FUNDS;

private BigDecimal R17_AMOUNT;
private BigDecimal R17_CAPITAL_FUNDS;

private BigDecimal R18_AMOUNT;
private BigDecimal R18_CAPITAL_FUNDS;

private BigDecimal R19_AMOUNT;
private BigDecimal R19_CAPITAL_FUNDS;

private BigDecimal R20_AMOUNT;
private BigDecimal R20_CAPITAL_FUNDS;

private BigDecimal R21_AMOUNT;
private BigDecimal R21_CAPITAL_FUNDS;

private BigDecimal R22_AMOUNT;
private BigDecimal R22_CAPITAL_FUNDS;

private BigDecimal R23_AMOUNT;
private BigDecimal R23_CAPITAL_FUNDS;

private BigDecimal R24_AMOUNT;
private BigDecimal R24_CAPITAL_FUNDS;

private BigDecimal R25_AMOUNT;
private BigDecimal R25_CAPITAL_FUNDS;

private BigDecimal R26_AMOUNT;
private BigDecimal R26_CAPITAL_FUNDS;

private BigDecimal R30_LINE_NO;
private BigDecimal R30_BORROWER;
private BigDecimal R30_AMOUNT;
private BigDecimal R30_CAPITAL_FUNDS;

private BigDecimal R31_AMOUNT;
private BigDecimal R31_CAPITAL_FUNDS;

private BigDecimal R32_AMOUNT;
private BigDecimal R32_CAPITAL_FUNDS;

private BigDecimal R33_AMOUNT;
private BigDecimal R33_CAPITAL_FUNDS;

private BigDecimal R34_AMOUNT;
private BigDecimal R34_CAPITAL_FUNDS;

private BigDecimal R35_AMOUNT;
private BigDecimal R35_CAPITAL_FUNDS;

private BigDecimal R36_AMOUNT;
private BigDecimal R36_CAPITAL_FUNDS;

private BigDecimal R37_AMOUNT;
private BigDecimal R37_CAPITAL_FUNDS;

private BigDecimal R38_AMOUNT;
private BigDecimal R38_CAPITAL_FUNDS;

private BigDecimal R39_AMOUNT;
private BigDecimal R39_CAPITAL_FUNDS;

private BigDecimal R40_AMOUNT;
private BigDecimal R40_CAPITAL_FUNDS;

private BigDecimal R41_AMOUNT;
private BigDecimal R41_CAPITAL_FUNDS;

private BigDecimal R42_AMOUNT;
private BigDecimal R42_CAPITAL_FUNDS;

private BigDecimal R43_AMOUNT;
private BigDecimal R43_CAPITAL_FUNDS;

private BigDecimal R44_AMOUNT;
private BigDecimal R44_CAPITAL_FUNDS;

private BigDecimal R45_AMOUNT;
private BigDecimal R45_CAPITAL_FUNDS;

private BigDecimal R46_AMOUNT;
private BigDecimal R46_CAPITAL_FUNDS;

private BigDecimal R47_AMOUNT;
private BigDecimal R47_CAPITAL_FUNDS;

private BigDecimal R48_AMOUNT;
private BigDecimal R48_CAPITAL_FUNDS;

private BigDecimal R49_AMOUNT;
private BigDecimal R49_CAPITAL_FUNDS;

private BigDecimal R50_AMOUNT;
private BigDecimal R50_CAPITAL_FUNDS;

private BigDecimal R51_AMOUNT;
private BigDecimal R51_CAPITAL_FUNDS;

private String R55_LINE_NO;
private String R55_BORROWER;
private String R55_AMOUNT;

private String R56_LINE_NO;
private String R56_BORROWER;
private String R56_AMOUNT;

private BigDecimal R57_AMOUNT;
private BigDecimal R58_AMOUNT;
private BigDecimal R59_AMOUNT;
private BigDecimal R60_AMOUNT;
private BigDecimal R61_AMOUNT;
private BigDecimal R62_AMOUNT;
private BigDecimal R63_AMOUNT;
private BigDecimal R64_AMOUNT;
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
public BigDecimal getR4_AMOUNT() {
	return R4_AMOUNT;
}
public void setR4_AMOUNT(BigDecimal r4_AMOUNT) {
	R4_AMOUNT = r4_AMOUNT;
}
public String getR5_LINE_NO() {
	return R5_LINE_NO;
}
public void setR5_LINE_NO(String r5_LINE_NO) {
	R5_LINE_NO = r5_LINE_NO;
}
public String getR5_BORROWER() {
	return R5_BORROWER;
}
public void setR5_BORROWER(String r5_BORROWER) {
	R5_BORROWER = r5_BORROWER;
}
public String getR5_AMOUNT() {
	return R5_AMOUNT;
}
public void setR5_AMOUNT(String r5_AMOUNT) {
	R5_AMOUNT = r5_AMOUNT;
}
public String getR5_CAPITAL_FUNDS() {
	return R5_CAPITAL_FUNDS;
}
public void setR5_CAPITAL_FUNDS(String r5_CAPITAL_FUNDS) {
	R5_CAPITAL_FUNDS = r5_CAPITAL_FUNDS;
}
public BigDecimal getR6_AMOUNT() {
	return R6_AMOUNT;
}
public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
	R6_AMOUNT = r6_AMOUNT;
}
public BigDecimal getR6_CAPITAL_FUNDS() {
	return R6_CAPITAL_FUNDS;
}
public void setR6_CAPITAL_FUNDS(BigDecimal r6_CAPITAL_FUNDS) {
	R6_CAPITAL_FUNDS = r6_CAPITAL_FUNDS;
}
public BigDecimal getR7_AMOUNT() {
	return R7_AMOUNT;
}
public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
	R7_AMOUNT = r7_AMOUNT;
}
public BigDecimal getR7_CAPITAL_FUNDS() {
	return R7_CAPITAL_FUNDS;
}
public void setR7_CAPITAL_FUNDS(BigDecimal r7_CAPITAL_FUNDS) {
	R7_CAPITAL_FUNDS = r7_CAPITAL_FUNDS;
}
public BigDecimal getR8_AMOUNT() {
	return R8_AMOUNT;
}
public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
	R8_AMOUNT = r8_AMOUNT;
}
public BigDecimal getR8_CAPITAL_FUNDS() {
	return R8_CAPITAL_FUNDS;
}
public void setR8_CAPITAL_FUNDS(BigDecimal r8_CAPITAL_FUNDS) {
	R8_CAPITAL_FUNDS = r8_CAPITAL_FUNDS;
}
public BigDecimal getR9_AMOUNT() {
	return R9_AMOUNT;
}
public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
	R9_AMOUNT = r9_AMOUNT;
}
public BigDecimal getR9_CAPITAL_FUNDS() {
	return R9_CAPITAL_FUNDS;
}
public void setR9_CAPITAL_FUNDS(BigDecimal r9_CAPITAL_FUNDS) {
	R9_CAPITAL_FUNDS = r9_CAPITAL_FUNDS;
}
public BigDecimal getR10_AMOUNT() {
	return R10_AMOUNT;
}
public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
	R10_AMOUNT = r10_AMOUNT;
}
public BigDecimal getR10_CAPITAL_FUNDS() {
	return R10_CAPITAL_FUNDS;
}
public void setR10_CAPITAL_FUNDS(BigDecimal r10_CAPITAL_FUNDS) {
	R10_CAPITAL_FUNDS = r10_CAPITAL_FUNDS;
}
public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
}
public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
}
public BigDecimal getR11_CAPITAL_FUNDS() {
	return R11_CAPITAL_FUNDS;
}
public void setR11_CAPITAL_FUNDS(BigDecimal r11_CAPITAL_FUNDS) {
	R11_CAPITAL_FUNDS = r11_CAPITAL_FUNDS;
}
public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
}
public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
}
public BigDecimal getR12_CAPITAL_FUNDS() {
	return R12_CAPITAL_FUNDS;
}
public void setR12_CAPITAL_FUNDS(BigDecimal r12_CAPITAL_FUNDS) {
	R12_CAPITAL_FUNDS = r12_CAPITAL_FUNDS;
}
public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
}
public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
}
public BigDecimal getR13_CAPITAL_FUNDS() {
	return R13_CAPITAL_FUNDS;
}
public void setR13_CAPITAL_FUNDS(BigDecimal r13_CAPITAL_FUNDS) {
	R13_CAPITAL_FUNDS = r13_CAPITAL_FUNDS;
}
public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
}
public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
}
public BigDecimal getR14_CAPITAL_FUNDS() {
	return R14_CAPITAL_FUNDS;
}
public void setR14_CAPITAL_FUNDS(BigDecimal r14_CAPITAL_FUNDS) {
	R14_CAPITAL_FUNDS = r14_CAPITAL_FUNDS;
}
public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
}
public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
}
public BigDecimal getR15_CAPITAL_FUNDS() {
	return R15_CAPITAL_FUNDS;
}
public void setR15_CAPITAL_FUNDS(BigDecimal r15_CAPITAL_FUNDS) {
	R15_CAPITAL_FUNDS = r15_CAPITAL_FUNDS;
}
public BigDecimal getR16_AMOUNT() {
	return R16_AMOUNT;
}
public void setR16_AMOUNT(BigDecimal r16_AMOUNT) {
	R16_AMOUNT = r16_AMOUNT;
}
public BigDecimal getR16_CAPITAL_FUNDS() {
	return R16_CAPITAL_FUNDS;
}
public void setR16_CAPITAL_FUNDS(BigDecimal r16_CAPITAL_FUNDS) {
	R16_CAPITAL_FUNDS = r16_CAPITAL_FUNDS;
}
public BigDecimal getR17_AMOUNT() {
	return R17_AMOUNT;
}
public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
	R17_AMOUNT = r17_AMOUNT;
}
public BigDecimal getR17_CAPITAL_FUNDS() {
	return R17_CAPITAL_FUNDS;
}
public void setR17_CAPITAL_FUNDS(BigDecimal r17_CAPITAL_FUNDS) {
	R17_CAPITAL_FUNDS = r17_CAPITAL_FUNDS;
}
public BigDecimal getR18_AMOUNT() {
	return R18_AMOUNT;
}
public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
	R18_AMOUNT = r18_AMOUNT;
}
public BigDecimal getR18_CAPITAL_FUNDS() {
	return R18_CAPITAL_FUNDS;
}
public void setR18_CAPITAL_FUNDS(BigDecimal r18_CAPITAL_FUNDS) {
	R18_CAPITAL_FUNDS = r18_CAPITAL_FUNDS;
}
public BigDecimal getR19_AMOUNT() {
	return R19_AMOUNT;
}
public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
	R19_AMOUNT = r19_AMOUNT;
}
public BigDecimal getR19_CAPITAL_FUNDS() {
	return R19_CAPITAL_FUNDS;
}
public void setR19_CAPITAL_FUNDS(BigDecimal r19_CAPITAL_FUNDS) {
	R19_CAPITAL_FUNDS = r19_CAPITAL_FUNDS;
}
public BigDecimal getR20_AMOUNT() {
	return R20_AMOUNT;
}
public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
	R20_AMOUNT = r20_AMOUNT;
}
public BigDecimal getR20_CAPITAL_FUNDS() {
	return R20_CAPITAL_FUNDS;
}
public void setR20_CAPITAL_FUNDS(BigDecimal r20_CAPITAL_FUNDS) {
	R20_CAPITAL_FUNDS = r20_CAPITAL_FUNDS;
}
public BigDecimal getR21_AMOUNT() {
	return R21_AMOUNT;
}
public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
	R21_AMOUNT = r21_AMOUNT;
}
public BigDecimal getR21_CAPITAL_FUNDS() {
	return R21_CAPITAL_FUNDS;
}
public void setR21_CAPITAL_FUNDS(BigDecimal r21_CAPITAL_FUNDS) {
	R21_CAPITAL_FUNDS = r21_CAPITAL_FUNDS;
}
public BigDecimal getR22_AMOUNT() {
	return R22_AMOUNT;
}
public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
	R22_AMOUNT = r22_AMOUNT;
}
public BigDecimal getR22_CAPITAL_FUNDS() {
	return R22_CAPITAL_FUNDS;
}
public void setR22_CAPITAL_FUNDS(BigDecimal r22_CAPITAL_FUNDS) {
	R22_CAPITAL_FUNDS = r22_CAPITAL_FUNDS;
}
public BigDecimal getR23_AMOUNT() {
	return R23_AMOUNT;
}
public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
	R23_AMOUNT = r23_AMOUNT;
}
public BigDecimal getR23_CAPITAL_FUNDS() {
	return R23_CAPITAL_FUNDS;
}
public void setR23_CAPITAL_FUNDS(BigDecimal r23_CAPITAL_FUNDS) {
	R23_CAPITAL_FUNDS = r23_CAPITAL_FUNDS;
}
public BigDecimal getR24_AMOUNT() {
	return R24_AMOUNT;
}
public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
	R24_AMOUNT = r24_AMOUNT;
}
public BigDecimal getR24_CAPITAL_FUNDS() {
	return R24_CAPITAL_FUNDS;
}
public void setR24_CAPITAL_FUNDS(BigDecimal r24_CAPITAL_FUNDS) {
	R24_CAPITAL_FUNDS = r24_CAPITAL_FUNDS;
}
public BigDecimal getR25_AMOUNT() {
	return R25_AMOUNT;
}
public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
	R25_AMOUNT = r25_AMOUNT;
}
public BigDecimal getR25_CAPITAL_FUNDS() {
	return R25_CAPITAL_FUNDS;
}
public void setR25_CAPITAL_FUNDS(BigDecimal r25_CAPITAL_FUNDS) {
	R25_CAPITAL_FUNDS = r25_CAPITAL_FUNDS;
}
public BigDecimal getR26_AMOUNT() {
	return R26_AMOUNT;
}
public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
	R26_AMOUNT = r26_AMOUNT;
}
public BigDecimal getR26_CAPITAL_FUNDS() {
	return R26_CAPITAL_FUNDS;
}
public void setR26_CAPITAL_FUNDS(BigDecimal r26_CAPITAL_FUNDS) {
	R26_CAPITAL_FUNDS = r26_CAPITAL_FUNDS;
}
public BigDecimal getR30_LINE_NO() {
	return R30_LINE_NO;
}
public void setR30_LINE_NO(BigDecimal r30_LINE_NO) {
	R30_LINE_NO = r30_LINE_NO;
}
public BigDecimal getR30_BORROWER() {
	return R30_BORROWER;
}
public void setR30_BORROWER(BigDecimal r30_BORROWER) {
	R30_BORROWER = r30_BORROWER;
}
public BigDecimal getR30_AMOUNT() {
	return R30_AMOUNT;
}
public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
	R30_AMOUNT = r30_AMOUNT;
}
public BigDecimal getR30_CAPITAL_FUNDS() {
	return R30_CAPITAL_FUNDS;
}
public void setR30_CAPITAL_FUNDS(BigDecimal r30_CAPITAL_FUNDS) {
	R30_CAPITAL_FUNDS = r30_CAPITAL_FUNDS;
}
public BigDecimal getR31_AMOUNT() {
	return R31_AMOUNT;
}
public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
	R31_AMOUNT = r31_AMOUNT;
}
public BigDecimal getR31_CAPITAL_FUNDS() {
	return R31_CAPITAL_FUNDS;
}
public void setR31_CAPITAL_FUNDS(BigDecimal r31_CAPITAL_FUNDS) {
	R31_CAPITAL_FUNDS = r31_CAPITAL_FUNDS;
}
public BigDecimal getR32_AMOUNT() {
	return R32_AMOUNT;
}
public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
	R32_AMOUNT = r32_AMOUNT;
}
public BigDecimal getR32_CAPITAL_FUNDS() {
	return R32_CAPITAL_FUNDS;
}
public void setR32_CAPITAL_FUNDS(BigDecimal r32_CAPITAL_FUNDS) {
	R32_CAPITAL_FUNDS = r32_CAPITAL_FUNDS;
}
public BigDecimal getR33_AMOUNT() {
	return R33_AMOUNT;
}
public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
	R33_AMOUNT = r33_AMOUNT;
}
public BigDecimal getR33_CAPITAL_FUNDS() {
	return R33_CAPITAL_FUNDS;
}
public void setR33_CAPITAL_FUNDS(BigDecimal r33_CAPITAL_FUNDS) {
	R33_CAPITAL_FUNDS = r33_CAPITAL_FUNDS;
}
public BigDecimal getR34_AMOUNT() {
	return R34_AMOUNT;
}
public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
	R34_AMOUNT = r34_AMOUNT;
}
public BigDecimal getR34_CAPITAL_FUNDS() {
	return R34_CAPITAL_FUNDS;
}
public void setR34_CAPITAL_FUNDS(BigDecimal r34_CAPITAL_FUNDS) {
	R34_CAPITAL_FUNDS = r34_CAPITAL_FUNDS;
}
public BigDecimal getR35_AMOUNT() {
	return R35_AMOUNT;
}
public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
	R35_AMOUNT = r35_AMOUNT;
}
public BigDecimal getR35_CAPITAL_FUNDS() {
	return R35_CAPITAL_FUNDS;
}
public void setR35_CAPITAL_FUNDS(BigDecimal r35_CAPITAL_FUNDS) {
	R35_CAPITAL_FUNDS = r35_CAPITAL_FUNDS;
}
public BigDecimal getR36_AMOUNT() {
	return R36_AMOUNT;
}
public void setR36_AMOUNT(BigDecimal r36_AMOUNT) {
	R36_AMOUNT = r36_AMOUNT;
}
public BigDecimal getR36_CAPITAL_FUNDS() {
	return R36_CAPITAL_FUNDS;
}
public void setR36_CAPITAL_FUNDS(BigDecimal r36_CAPITAL_FUNDS) {
	R36_CAPITAL_FUNDS = r36_CAPITAL_FUNDS;
}
public BigDecimal getR37_AMOUNT() {
	return R37_AMOUNT;
}
public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
	R37_AMOUNT = r37_AMOUNT;
}
public BigDecimal getR37_CAPITAL_FUNDS() {
	return R37_CAPITAL_FUNDS;
}
public void setR37_CAPITAL_FUNDS(BigDecimal r37_CAPITAL_FUNDS) {
	R37_CAPITAL_FUNDS = r37_CAPITAL_FUNDS;
}
public BigDecimal getR38_AMOUNT() {
	return R38_AMOUNT;
}
public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
	R38_AMOUNT = r38_AMOUNT;
}
public BigDecimal getR38_CAPITAL_FUNDS() {
	return R38_CAPITAL_FUNDS;
}
public void setR38_CAPITAL_FUNDS(BigDecimal r38_CAPITAL_FUNDS) {
	R38_CAPITAL_FUNDS = r38_CAPITAL_FUNDS;
}
public BigDecimal getR39_AMOUNT() {
	return R39_AMOUNT;
}
public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
	R39_AMOUNT = r39_AMOUNT;
}
public BigDecimal getR39_CAPITAL_FUNDS() {
	return R39_CAPITAL_FUNDS;
}
public void setR39_CAPITAL_FUNDS(BigDecimal r39_CAPITAL_FUNDS) {
	R39_CAPITAL_FUNDS = r39_CAPITAL_FUNDS;
}
public BigDecimal getR40_AMOUNT() {
	return R40_AMOUNT;
}
public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
	R40_AMOUNT = r40_AMOUNT;
}
public BigDecimal getR40_CAPITAL_FUNDS() {
	return R40_CAPITAL_FUNDS;
}
public void setR40_CAPITAL_FUNDS(BigDecimal r40_CAPITAL_FUNDS) {
	R40_CAPITAL_FUNDS = r40_CAPITAL_FUNDS;
}
public BigDecimal getR41_AMOUNT() {
	return R41_AMOUNT;
}
public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
	R41_AMOUNT = r41_AMOUNT;
}
public BigDecimal getR41_CAPITAL_FUNDS() {
	return R41_CAPITAL_FUNDS;
}
public void setR41_CAPITAL_FUNDS(BigDecimal r41_CAPITAL_FUNDS) {
	R41_CAPITAL_FUNDS = r41_CAPITAL_FUNDS;
}
public BigDecimal getR42_AMOUNT() {
	return R42_AMOUNT;
}
public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
	R42_AMOUNT = r42_AMOUNT;
}
public BigDecimal getR42_CAPITAL_FUNDS() {
	return R42_CAPITAL_FUNDS;
}
public void setR42_CAPITAL_FUNDS(BigDecimal r42_CAPITAL_FUNDS) {
	R42_CAPITAL_FUNDS = r42_CAPITAL_FUNDS;
}
public BigDecimal getR43_AMOUNT() {
	return R43_AMOUNT;
}
public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
	R43_AMOUNT = r43_AMOUNT;
}
public BigDecimal getR43_CAPITAL_FUNDS() {
	return R43_CAPITAL_FUNDS;
}
public void setR43_CAPITAL_FUNDS(BigDecimal r43_CAPITAL_FUNDS) {
	R43_CAPITAL_FUNDS = r43_CAPITAL_FUNDS;
}
public BigDecimal getR44_AMOUNT() {
	return R44_AMOUNT;
}
public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
	R44_AMOUNT = r44_AMOUNT;
}
public BigDecimal getR44_CAPITAL_FUNDS() {
	return R44_CAPITAL_FUNDS;
}
public void setR44_CAPITAL_FUNDS(BigDecimal r44_CAPITAL_FUNDS) {
	R44_CAPITAL_FUNDS = r44_CAPITAL_FUNDS;
}
public BigDecimal getR45_AMOUNT() {
	return R45_AMOUNT;
}
public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
	R45_AMOUNT = r45_AMOUNT;
}
public BigDecimal getR45_CAPITAL_FUNDS() {
	return R45_CAPITAL_FUNDS;
}
public void setR45_CAPITAL_FUNDS(BigDecimal r45_CAPITAL_FUNDS) {
	R45_CAPITAL_FUNDS = r45_CAPITAL_FUNDS;
}
public BigDecimal getR46_AMOUNT() {
	return R46_AMOUNT;
}
public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
	R46_AMOUNT = r46_AMOUNT;
}
public BigDecimal getR46_CAPITAL_FUNDS() {
	return R46_CAPITAL_FUNDS;
}
public void setR46_CAPITAL_FUNDS(BigDecimal r46_CAPITAL_FUNDS) {
	R46_CAPITAL_FUNDS = r46_CAPITAL_FUNDS;
}
public BigDecimal getR47_AMOUNT() {
	return R47_AMOUNT;
}
public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
	R47_AMOUNT = r47_AMOUNT;
}
public BigDecimal getR47_CAPITAL_FUNDS() {
	return R47_CAPITAL_FUNDS;
}
public void setR47_CAPITAL_FUNDS(BigDecimal r47_CAPITAL_FUNDS) {
	R47_CAPITAL_FUNDS = r47_CAPITAL_FUNDS;
}
public BigDecimal getR48_AMOUNT() {
	return R48_AMOUNT;
}
public void setR48_AMOUNT(BigDecimal r48_AMOUNT) {
	R48_AMOUNT = r48_AMOUNT;
}
public BigDecimal getR48_CAPITAL_FUNDS() {
	return R48_CAPITAL_FUNDS;
}
public void setR48_CAPITAL_FUNDS(BigDecimal r48_CAPITAL_FUNDS) {
	R48_CAPITAL_FUNDS = r48_CAPITAL_FUNDS;
}
public BigDecimal getR49_AMOUNT() {
	return R49_AMOUNT;
}
public void setR49_AMOUNT(BigDecimal r49_AMOUNT) {
	R49_AMOUNT = r49_AMOUNT;
}
public BigDecimal getR49_CAPITAL_FUNDS() {
	return R49_CAPITAL_FUNDS;
}
public void setR49_CAPITAL_FUNDS(BigDecimal r49_CAPITAL_FUNDS) {
	R49_CAPITAL_FUNDS = r49_CAPITAL_FUNDS;
}
public BigDecimal getR50_AMOUNT() {
	return R50_AMOUNT;
}
public void setR50_AMOUNT(BigDecimal r50_AMOUNT) {
	R50_AMOUNT = r50_AMOUNT;
}
public BigDecimal getR50_CAPITAL_FUNDS() {
	return R50_CAPITAL_FUNDS;
}
public void setR50_CAPITAL_FUNDS(BigDecimal r50_CAPITAL_FUNDS) {
	R50_CAPITAL_FUNDS = r50_CAPITAL_FUNDS;
}
public BigDecimal getR51_AMOUNT() {
	return R51_AMOUNT;
}
public void setR51_AMOUNT(BigDecimal r51_AMOUNT) {
	R51_AMOUNT = r51_AMOUNT;
}
public BigDecimal getR51_CAPITAL_FUNDS() {
	return R51_CAPITAL_FUNDS;
}
public void setR51_CAPITAL_FUNDS(BigDecimal r51_CAPITAL_FUNDS) {
	R51_CAPITAL_FUNDS = r51_CAPITAL_FUNDS;
}
public String getR55_LINE_NO() {
	return R55_LINE_NO;
}
public void setR55_LINE_NO(String r55_LINE_NO) {
	R55_LINE_NO = r55_LINE_NO;
}
public String getR55_BORROWER() {
	return R55_BORROWER;
}
public void setR55_BORROWER(String r55_BORROWER) {
	R55_BORROWER = r55_BORROWER;
}
public String getR55_AMOUNT() {
	return R55_AMOUNT;
}
public void setR55_AMOUNT(String r55_AMOUNT) {
	R55_AMOUNT = r55_AMOUNT;
}
public String getR56_LINE_NO() {
	return R56_LINE_NO;
}
public void setR56_LINE_NO(String r56_LINE_NO) {
	R56_LINE_NO = r56_LINE_NO;
}
public String getR56_BORROWER() {
	return R56_BORROWER;
}
public void setR56_BORROWER(String r56_BORROWER) {
	R56_BORROWER = r56_BORROWER;
}
public String getR56_AMOUNT() {
	return R56_AMOUNT;
}
public void setR56_AMOUNT(String r56_AMOUNT) {
	R56_AMOUNT = r56_AMOUNT;
}
public BigDecimal getR57_AMOUNT() {
	return R57_AMOUNT;
}
public void setR57_AMOUNT(BigDecimal r57_AMOUNT) {
	R57_AMOUNT = r57_AMOUNT;
}
public BigDecimal getR58_AMOUNT() {
	return R58_AMOUNT;
}
public void setR58_AMOUNT(BigDecimal r58_AMOUNT) {
	R58_AMOUNT = r58_AMOUNT;
}
public BigDecimal getR59_AMOUNT() {
	return R59_AMOUNT;
}
public void setR59_AMOUNT(BigDecimal r59_AMOUNT) {
	R59_AMOUNT = r59_AMOUNT;
}
public BigDecimal getR60_AMOUNT() {
	return R60_AMOUNT;
}
public void setR60_AMOUNT(BigDecimal r60_AMOUNT) {
	R60_AMOUNT = r60_AMOUNT;
}
public BigDecimal getR61_AMOUNT() {
	return R61_AMOUNT;
}
public void setR61_AMOUNT(BigDecimal r61_AMOUNT) {
	R61_AMOUNT = r61_AMOUNT;
}
public BigDecimal getR62_AMOUNT() {
	return R62_AMOUNT;
}
public void setR62_AMOUNT(BigDecimal r62_AMOUNT) {
	R62_AMOUNT = r62_AMOUNT;
}
public BigDecimal getR63_AMOUNT() {
	return R63_AMOUNT;
}
public void setR63_AMOUNT(BigDecimal r63_AMOUNT) {
	R63_AMOUNT = r63_AMOUNT;
}
public BigDecimal getR64_AMOUNT() {
	return R64_AMOUNT;
}
public void setR64_AMOUNT(BigDecimal r64_AMOUNT) {
	R64_AMOUNT = r64_AMOUNT;
}
public EXPOSURES_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}





}
