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
@Table(name = "BRRS_M_PLL_SUMMARYTABLE")

public class M_PLL_Summary_Entity {	
	
	
private String r11_provi_ins_sec;
private BigDecimal r11_provi_loan_loss;
private String r12_provi_ins_sec;
private BigDecimal r12_provi_loan_loss;
private String r13_provi_ins_sec;
private BigDecimal r13_provi_loan_loss;
private String r14_provi_ins_sec;
private BigDecimal r14_provi_loan_loss;
private String r15_provi_ins_sec;
private BigDecimal r15_provi_loan_loss;
private String r16_provi_ins_sec;
private BigDecimal r16_provi_loan_loss;
private String r17_provi_ins_sec;
private BigDecimal r17_provi_loan_loss;
private String r18_provi_ins_sec;
private BigDecimal r18_provi_loan_loss;
private String r19_provi_ins_sec;
private BigDecimal r19_provi_loan_loss;
private String r20_provi_ins_sec;
private BigDecimal r20_provi_loan_loss;
private String r21_provi_ins_sec;
private BigDecimal r21_provi_loan_loss;
private String r22_provi_ins_sec;
private BigDecimal r22_provi_loan_loss;
private String r23_provi_ins_sec;
private BigDecimal r23_provi_loan_loss;
private String r24_provi_ins_sec;
private BigDecimal r24_provi_loan_loss;
private String r25_provi_ins_sec;
private BigDecimal r25_provi_loan_loss;
private String r26_provi_ins_sec;
private BigDecimal r26_provi_loan_loss;
private String r27_provi_ins_sec;
private BigDecimal r27_provi_loan_loss;
private String r28_provi_ins_sec;
private BigDecimal r28_provi_loan_loss;
private String r29_provi_ins_sec;
private BigDecimal r29_provi_loan_loss;
private String r30_provi_ins_sec;
private BigDecimal r30_provi_loan_loss;
private String r31_provi_ins_sec;
private BigDecimal r31_provi_loan_loss;
private String r32_provi_ins_sec;
private BigDecimal r32_provi_loan_loss;
private String r33_provi_ins_sec;
private BigDecimal r33_provi_loan_loss;
private String r34_provi_ins_sec;
private BigDecimal r34_provi_loan_loss;
private String r35_provi_ins_sec;
private BigDecimal r35_provi_loan_loss;
private String r36_provi_ins_sec;
private BigDecimal r36_provi_loan_loss;
private String r37_provi_ins_sec;
private BigDecimal r37_provi_loan_loss;
private String r38_provi_ins_sec;
private BigDecimal r38_provi_loan_loss;
private String r39_provi_ins_sec;
private BigDecimal r39_provi_loan_loss;
private String r40_provi_ins_sec;
private BigDecimal r40_provi_loan_loss;
private String r41_provi_ins_sec;
private BigDecimal r41_provi_loan_loss;
private String r42_provi_ins_sec;
private BigDecimal r42_provi_loan_loss;
private String r43_provi_ins_sec;
private BigDecimal r43_provi_loan_loss;
private String r44_provi_ins_sec;
private BigDecimal r44_provi_loan_loss;
private String r45_provi_ins_sec;
private BigDecimal r45_provi_loan_loss;
private String r46_provi_ins_sec;
private BigDecimal r46_provi_loan_loss;
private String r47_provi_ins_sec;
private BigDecimal r47_provi_loan_loss;
private String r48_provi_ins_sec;
private BigDecimal r48_provi_loan_loss;
private String r49_provi_ins_sec;
private BigDecimal r49_provi_loan_loss;
private String r50_provi_ins_sec;
private BigDecimal r50_provi_loan_loss;
private String r51_provi_ins_sec;
private BigDecimal r51_provi_loan_loss;
private String r52_provi_ins_sec;
private BigDecimal r52_provi_loan_loss;
private String r53_provi_ins_sec;
private BigDecimal r53_provi_loan_loss;
private String r54_provi_ins_sec;
private BigDecimal r54_provi_loan_loss;
private String r55_provi_ins_sec;
private BigDecimal r55_provi_loan_loss;
private String r56_provi_ins_sec;
private BigDecimal r56_provi_loan_loss;
private String r57_provi_ins_sec;
private BigDecimal r57_provi_loan_loss;
private String r58_provi_ins_sec;
private BigDecimal r58_provi_loan_loss;
private String r59_provi_ins_sec;
private BigDecimal r59_provi_loan_loss;
private String r60_provi_ins_sec;
private BigDecimal r60_provi_loan_loss;
private String r61_provi_ins_sec;
private BigDecimal r61_provi_loan_loss;
private String r62_provi_ins_sec;
private BigDecimal r62_provi_loan_loss;
private String r63_provi_ins_sec;
private BigDecimal r63_provi_loan_loss;
private String r64_provi_ins_sec;
private BigDecimal r64_provi_loan_loss;

	
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
	

	
	








	public String getR11_provi_ins_sec() {
		return r11_provi_ins_sec;
	}












	public void setR11_provi_ins_sec(String r11_provi_ins_sec) {
		this.r11_provi_ins_sec = r11_provi_ins_sec;
	}












	public BigDecimal getR11_provi_loan_loss() {
		return r11_provi_loan_loss;
	}












	public void setR11_provi_loan_loss(BigDecimal r11_provi_loan_loss) {
		this.r11_provi_loan_loss = r11_provi_loan_loss;
	}












	public String getR12_provi_ins_sec() {
		return r12_provi_ins_sec;
	}












	public void setR12_provi_ins_sec(String r12_provi_ins_sec) {
		this.r12_provi_ins_sec = r12_provi_ins_sec;
	}












	public BigDecimal getR12_provi_loan_loss() {
		return r12_provi_loan_loss;
	}












	public void setR12_provi_loan_loss(BigDecimal r12_provi_loan_loss) {
		this.r12_provi_loan_loss = r12_provi_loan_loss;
	}












	public String getR13_provi_ins_sec() {
		return r13_provi_ins_sec;
	}












	public void setR13_provi_ins_sec(String r13_provi_ins_sec) {
		this.r13_provi_ins_sec = r13_provi_ins_sec;
	}












	public BigDecimal getR13_provi_loan_loss() {
		return r13_provi_loan_loss;
	}












	public void setR13_provi_loan_loss(BigDecimal r13_provi_loan_loss) {
		this.r13_provi_loan_loss = r13_provi_loan_loss;
	}












	public String getR14_provi_ins_sec() {
		return r14_provi_ins_sec;
	}












	public void setR14_provi_ins_sec(String r14_provi_ins_sec) {
		this.r14_provi_ins_sec = r14_provi_ins_sec;
	}












	public BigDecimal getR14_provi_loan_loss() {
		return r14_provi_loan_loss;
	}












	public void setR14_provi_loan_loss(BigDecimal r14_provi_loan_loss) {
		this.r14_provi_loan_loss = r14_provi_loan_loss;
	}












	public String getR15_provi_ins_sec() {
		return r15_provi_ins_sec;
	}












	public void setR15_provi_ins_sec(String r15_provi_ins_sec) {
		this.r15_provi_ins_sec = r15_provi_ins_sec;
	}












	public BigDecimal getR15_provi_loan_loss() {
		return r15_provi_loan_loss;
	}












	public void setR15_provi_loan_loss(BigDecimal r15_provi_loan_loss) {
		this.r15_provi_loan_loss = r15_provi_loan_loss;
	}












	public String getR16_provi_ins_sec() {
		return r16_provi_ins_sec;
	}












	public void setR16_provi_ins_sec(String r16_provi_ins_sec) {
		this.r16_provi_ins_sec = r16_provi_ins_sec;
	}












	public BigDecimal getR16_provi_loan_loss() {
		return r16_provi_loan_loss;
	}












	public void setR16_provi_loan_loss(BigDecimal r16_provi_loan_loss) {
		this.r16_provi_loan_loss = r16_provi_loan_loss;
	}












	public String getR17_provi_ins_sec() {
		return r17_provi_ins_sec;
	}












	public void setR17_provi_ins_sec(String r17_provi_ins_sec) {
		this.r17_provi_ins_sec = r17_provi_ins_sec;
	}












	public BigDecimal getR17_provi_loan_loss() {
		return r17_provi_loan_loss;
	}












	public void setR17_provi_loan_loss(BigDecimal r17_provi_loan_loss) {
		this.r17_provi_loan_loss = r17_provi_loan_loss;
	}












	public String getR18_provi_ins_sec() {
		return r18_provi_ins_sec;
	}












	public void setR18_provi_ins_sec(String r18_provi_ins_sec) {
		this.r18_provi_ins_sec = r18_provi_ins_sec;
	}












	public BigDecimal getR18_provi_loan_loss() {
		return r18_provi_loan_loss;
	}












	public void setR18_provi_loan_loss(BigDecimal r18_provi_loan_loss) {
		this.r18_provi_loan_loss = r18_provi_loan_loss;
	}












	public String getR19_provi_ins_sec() {
		return r19_provi_ins_sec;
	}












	public void setR19_provi_ins_sec(String r19_provi_ins_sec) {
		this.r19_provi_ins_sec = r19_provi_ins_sec;
	}












	public BigDecimal getR19_provi_loan_loss() {
		return r19_provi_loan_loss;
	}












	public void setR19_provi_loan_loss(BigDecimal r19_provi_loan_loss) {
		this.r19_provi_loan_loss = r19_provi_loan_loss;
	}












	public String getR20_provi_ins_sec() {
		return r20_provi_ins_sec;
	}












	public void setR20_provi_ins_sec(String r20_provi_ins_sec) {
		this.r20_provi_ins_sec = r20_provi_ins_sec;
	}












	public BigDecimal getR20_provi_loan_loss() {
		return r20_provi_loan_loss;
	}












	public void setR20_provi_loan_loss(BigDecimal r20_provi_loan_loss) {
		this.r20_provi_loan_loss = r20_provi_loan_loss;
	}












	public String getR21_provi_ins_sec() {
		return r21_provi_ins_sec;
	}












	public void setR21_provi_ins_sec(String r21_provi_ins_sec) {
		this.r21_provi_ins_sec = r21_provi_ins_sec;
	}












	public BigDecimal getR21_provi_loan_loss() {
		return r21_provi_loan_loss;
	}












	public void setR21_provi_loan_loss(BigDecimal r21_provi_loan_loss) {
		this.r21_provi_loan_loss = r21_provi_loan_loss;
	}












	public String getR22_provi_ins_sec() {
		return r22_provi_ins_sec;
	}












	public void setR22_provi_ins_sec(String r22_provi_ins_sec) {
		this.r22_provi_ins_sec = r22_provi_ins_sec;
	}












	public BigDecimal getR22_provi_loan_loss() {
		return r22_provi_loan_loss;
	}












	public void setR22_provi_loan_loss(BigDecimal r22_provi_loan_loss) {
		this.r22_provi_loan_loss = r22_provi_loan_loss;
	}












	public String getR23_provi_ins_sec() {
		return r23_provi_ins_sec;
	}












	public void setR23_provi_ins_sec(String r23_provi_ins_sec) {
		this.r23_provi_ins_sec = r23_provi_ins_sec;
	}












	public BigDecimal getR23_provi_loan_loss() {
		return r23_provi_loan_loss;
	}












	public void setR23_provi_loan_loss(BigDecimal r23_provi_loan_loss) {
		this.r23_provi_loan_loss = r23_provi_loan_loss;
	}












	public String getR24_provi_ins_sec() {
		return r24_provi_ins_sec;
	}












	public void setR24_provi_ins_sec(String r24_provi_ins_sec) {
		this.r24_provi_ins_sec = r24_provi_ins_sec;
	}












	public BigDecimal getR24_provi_loan_loss() {
		return r24_provi_loan_loss;
	}












	public void setR24_provi_loan_loss(BigDecimal r24_provi_loan_loss) {
		this.r24_provi_loan_loss = r24_provi_loan_loss;
	}












	public String getR25_provi_ins_sec() {
		return r25_provi_ins_sec;
	}












	public void setR25_provi_ins_sec(String r25_provi_ins_sec) {
		this.r25_provi_ins_sec = r25_provi_ins_sec;
	}












	public BigDecimal getR25_provi_loan_loss() {
		return r25_provi_loan_loss;
	}












	public void setR25_provi_loan_loss(BigDecimal r25_provi_loan_loss) {
		this.r25_provi_loan_loss = r25_provi_loan_loss;
	}












	public String getR26_provi_ins_sec() {
		return r26_provi_ins_sec;
	}












	public void setR26_provi_ins_sec(String r26_provi_ins_sec) {
		this.r26_provi_ins_sec = r26_provi_ins_sec;
	}












	public BigDecimal getR26_provi_loan_loss() {
		return r26_provi_loan_loss;
	}












	public void setR26_provi_loan_loss(BigDecimal r26_provi_loan_loss) {
		this.r26_provi_loan_loss = r26_provi_loan_loss;
	}












	public String getR27_provi_ins_sec() {
		return r27_provi_ins_sec;
	}












	public void setR27_provi_ins_sec(String r27_provi_ins_sec) {
		this.r27_provi_ins_sec = r27_provi_ins_sec;
	}












	public BigDecimal getR27_provi_loan_loss() {
		return r27_provi_loan_loss;
	}












	public void setR27_provi_loan_loss(BigDecimal r27_provi_loan_loss) {
		this.r27_provi_loan_loss = r27_provi_loan_loss;
	}












	public String getR28_provi_ins_sec() {
		return r28_provi_ins_sec;
	}












	public void setR28_provi_ins_sec(String r28_provi_ins_sec) {
		this.r28_provi_ins_sec = r28_provi_ins_sec;
	}












	public BigDecimal getR28_provi_loan_loss() {
		return r28_provi_loan_loss;
	}












	public void setR28_provi_loan_loss(BigDecimal r28_provi_loan_loss) {
		this.r28_provi_loan_loss = r28_provi_loan_loss;
	}












	public String getR29_provi_ins_sec() {
		return r29_provi_ins_sec;
	}












	public void setR29_provi_ins_sec(String r29_provi_ins_sec) {
		this.r29_provi_ins_sec = r29_provi_ins_sec;
	}












	public BigDecimal getR29_provi_loan_loss() {
		return r29_provi_loan_loss;
	}












	public void setR29_provi_loan_loss(BigDecimal r29_provi_loan_loss) {
		this.r29_provi_loan_loss = r29_provi_loan_loss;
	}












	public String getR30_provi_ins_sec() {
		return r30_provi_ins_sec;
	}












	public void setR30_provi_ins_sec(String r30_provi_ins_sec) {
		this.r30_provi_ins_sec = r30_provi_ins_sec;
	}












	public BigDecimal getR30_provi_loan_loss() {
		return r30_provi_loan_loss;
	}












	public void setR30_provi_loan_loss(BigDecimal r30_provi_loan_loss) {
		this.r30_provi_loan_loss = r30_provi_loan_loss;
	}












	public String getR31_provi_ins_sec() {
		return r31_provi_ins_sec;
	}












	public void setR31_provi_ins_sec(String r31_provi_ins_sec) {
		this.r31_provi_ins_sec = r31_provi_ins_sec;
	}












	public BigDecimal getR31_provi_loan_loss() {
		return r31_provi_loan_loss;
	}












	public void setR31_provi_loan_loss(BigDecimal r31_provi_loan_loss) {
		this.r31_provi_loan_loss = r31_provi_loan_loss;
	}












	public String getR32_provi_ins_sec() {
		return r32_provi_ins_sec;
	}












	public void setR32_provi_ins_sec(String r32_provi_ins_sec) {
		this.r32_provi_ins_sec = r32_provi_ins_sec;
	}












	public BigDecimal getR32_provi_loan_loss() {
		return r32_provi_loan_loss;
	}












	public void setR32_provi_loan_loss(BigDecimal r32_provi_loan_loss) {
		this.r32_provi_loan_loss = r32_provi_loan_loss;
	}












	public String getR33_provi_ins_sec() {
		return r33_provi_ins_sec;
	}












	public void setR33_provi_ins_sec(String r33_provi_ins_sec) {
		this.r33_provi_ins_sec = r33_provi_ins_sec;
	}












	public BigDecimal getR33_provi_loan_loss() {
		return r33_provi_loan_loss;
	}












	public void setR33_provi_loan_loss(BigDecimal r33_provi_loan_loss) {
		this.r33_provi_loan_loss = r33_provi_loan_loss;
	}












	public String getR34_provi_ins_sec() {
		return r34_provi_ins_sec;
	}












	public void setR34_provi_ins_sec(String r34_provi_ins_sec) {
		this.r34_provi_ins_sec = r34_provi_ins_sec;
	}












	public BigDecimal getR34_provi_loan_loss() {
		return r34_provi_loan_loss;
	}












	public void setR34_provi_loan_loss(BigDecimal r34_provi_loan_loss) {
		this.r34_provi_loan_loss = r34_provi_loan_loss;
	}












	public String getR35_provi_ins_sec() {
		return r35_provi_ins_sec;
	}












	public void setR35_provi_ins_sec(String r35_provi_ins_sec) {
		this.r35_provi_ins_sec = r35_provi_ins_sec;
	}












	public BigDecimal getR35_provi_loan_loss() {
		return r35_provi_loan_loss;
	}












	public void setR35_provi_loan_loss(BigDecimal r35_provi_loan_loss) {
		this.r35_provi_loan_loss = r35_provi_loan_loss;
	}












	public String getR36_provi_ins_sec() {
		return r36_provi_ins_sec;
	}












	public void setR36_provi_ins_sec(String r36_provi_ins_sec) {
		this.r36_provi_ins_sec = r36_provi_ins_sec;
	}












	public BigDecimal getR36_provi_loan_loss() {
		return r36_provi_loan_loss;
	}












	public void setR36_provi_loan_loss(BigDecimal r36_provi_loan_loss) {
		this.r36_provi_loan_loss = r36_provi_loan_loss;
	}












	public String getR37_provi_ins_sec() {
		return r37_provi_ins_sec;
	}












	public void setR37_provi_ins_sec(String r37_provi_ins_sec) {
		this.r37_provi_ins_sec = r37_provi_ins_sec;
	}












	public BigDecimal getR37_provi_loan_loss() {
		return r37_provi_loan_loss;
	}












	public void setR37_provi_loan_loss(BigDecimal r37_provi_loan_loss) {
		this.r37_provi_loan_loss = r37_provi_loan_loss;
	}












	public String getR38_provi_ins_sec() {
		return r38_provi_ins_sec;
	}












	public void setR38_provi_ins_sec(String r38_provi_ins_sec) {
		this.r38_provi_ins_sec = r38_provi_ins_sec;
	}












	public BigDecimal getR38_provi_loan_loss() {
		return r38_provi_loan_loss;
	}












	public void setR38_provi_loan_loss(BigDecimal r38_provi_loan_loss) {
		this.r38_provi_loan_loss = r38_provi_loan_loss;
	}












	public String getR39_provi_ins_sec() {
		return r39_provi_ins_sec;
	}












	public void setR39_provi_ins_sec(String r39_provi_ins_sec) {
		this.r39_provi_ins_sec = r39_provi_ins_sec;
	}












	public BigDecimal getR39_provi_loan_loss() {
		return r39_provi_loan_loss;
	}












	public void setR39_provi_loan_loss(BigDecimal r39_provi_loan_loss) {
		this.r39_provi_loan_loss = r39_provi_loan_loss;
	}












	public String getR40_provi_ins_sec() {
		return r40_provi_ins_sec;
	}












	public void setR40_provi_ins_sec(String r40_provi_ins_sec) {
		this.r40_provi_ins_sec = r40_provi_ins_sec;
	}












	public BigDecimal getR40_provi_loan_loss() {
		return r40_provi_loan_loss;
	}












	public void setR40_provi_loan_loss(BigDecimal r40_provi_loan_loss) {
		this.r40_provi_loan_loss = r40_provi_loan_loss;
	}












	public String getR41_provi_ins_sec() {
		return r41_provi_ins_sec;
	}












	public void setR41_provi_ins_sec(String r41_provi_ins_sec) {
		this.r41_provi_ins_sec = r41_provi_ins_sec;
	}












	public BigDecimal getR41_provi_loan_loss() {
		return r41_provi_loan_loss;
	}












	public void setR41_provi_loan_loss(BigDecimal r41_provi_loan_loss) {
		this.r41_provi_loan_loss = r41_provi_loan_loss;
	}












	public String getR42_provi_ins_sec() {
		return r42_provi_ins_sec;
	}












	public void setR42_provi_ins_sec(String r42_provi_ins_sec) {
		this.r42_provi_ins_sec = r42_provi_ins_sec;
	}












	public BigDecimal getR42_provi_loan_loss() {
		return r42_provi_loan_loss;
	}












	public void setR42_provi_loan_loss(BigDecimal r42_provi_loan_loss) {
		this.r42_provi_loan_loss = r42_provi_loan_loss;
	}












	public String getR43_provi_ins_sec() {
		return r43_provi_ins_sec;
	}












	public void setR43_provi_ins_sec(String r43_provi_ins_sec) {
		this.r43_provi_ins_sec = r43_provi_ins_sec;
	}












	public BigDecimal getR43_provi_loan_loss() {
		return r43_provi_loan_loss;
	}












	public void setR43_provi_loan_loss(BigDecimal r43_provi_loan_loss) {
		this.r43_provi_loan_loss = r43_provi_loan_loss;
	}












	public String getR44_provi_ins_sec() {
		return r44_provi_ins_sec;
	}












	public void setR44_provi_ins_sec(String r44_provi_ins_sec) {
		this.r44_provi_ins_sec = r44_provi_ins_sec;
	}












	public BigDecimal getR44_provi_loan_loss() {
		return r44_provi_loan_loss;
	}












	public void setR44_provi_loan_loss(BigDecimal r44_provi_loan_loss) {
		this.r44_provi_loan_loss = r44_provi_loan_loss;
	}












	public String getR45_provi_ins_sec() {
		return r45_provi_ins_sec;
	}












	public void setR45_provi_ins_sec(String r45_provi_ins_sec) {
		this.r45_provi_ins_sec = r45_provi_ins_sec;
	}












	public BigDecimal getR45_provi_loan_loss() {
		return r45_provi_loan_loss;
	}












	public void setR45_provi_loan_loss(BigDecimal r45_provi_loan_loss) {
		this.r45_provi_loan_loss = r45_provi_loan_loss;
	}












	public String getR46_provi_ins_sec() {
		return r46_provi_ins_sec;
	}












	public void setR46_provi_ins_sec(String r46_provi_ins_sec) {
		this.r46_provi_ins_sec = r46_provi_ins_sec;
	}












	public BigDecimal getR46_provi_loan_loss() {
		return r46_provi_loan_loss;
	}












	public void setR46_provi_loan_loss(BigDecimal r46_provi_loan_loss) {
		this.r46_provi_loan_loss = r46_provi_loan_loss;
	}












	public String getR47_provi_ins_sec() {
		return r47_provi_ins_sec;
	}












	public void setR47_provi_ins_sec(String r47_provi_ins_sec) {
		this.r47_provi_ins_sec = r47_provi_ins_sec;
	}












	public BigDecimal getR47_provi_loan_loss() {
		return r47_provi_loan_loss;
	}












	public void setR47_provi_loan_loss(BigDecimal r47_provi_loan_loss) {
		this.r47_provi_loan_loss = r47_provi_loan_loss;
	}












	public String getR48_provi_ins_sec() {
		return r48_provi_ins_sec;
	}












	public void setR48_provi_ins_sec(String r48_provi_ins_sec) {
		this.r48_provi_ins_sec = r48_provi_ins_sec;
	}












	public BigDecimal getR48_provi_loan_loss() {
		return r48_provi_loan_loss;
	}












	public void setR48_provi_loan_loss(BigDecimal r48_provi_loan_loss) {
		this.r48_provi_loan_loss = r48_provi_loan_loss;
	}












	public String getR49_provi_ins_sec() {
		return r49_provi_ins_sec;
	}












	public void setR49_provi_ins_sec(String r49_provi_ins_sec) {
		this.r49_provi_ins_sec = r49_provi_ins_sec;
	}












	public BigDecimal getR49_provi_loan_loss() {
		return r49_provi_loan_loss;
	}












	public void setR49_provi_loan_loss(BigDecimal r49_provi_loan_loss) {
		this.r49_provi_loan_loss = r49_provi_loan_loss;
	}












	public String getR50_provi_ins_sec() {
		return r50_provi_ins_sec;
	}












	public void setR50_provi_ins_sec(String r50_provi_ins_sec) {
		this.r50_provi_ins_sec = r50_provi_ins_sec;
	}












	public BigDecimal getR50_provi_loan_loss() {
		return r50_provi_loan_loss;
	}












	public void setR50_provi_loan_loss(BigDecimal r50_provi_loan_loss) {
		this.r50_provi_loan_loss = r50_provi_loan_loss;
	}












	public String getR51_provi_ins_sec() {
		return r51_provi_ins_sec;
	}












	public void setR51_provi_ins_sec(String r51_provi_ins_sec) {
		this.r51_provi_ins_sec = r51_provi_ins_sec;
	}












	public BigDecimal getR51_provi_loan_loss() {
		return r51_provi_loan_loss;
	}












	public void setR51_provi_loan_loss(BigDecimal r51_provi_loan_loss) {
		this.r51_provi_loan_loss = r51_provi_loan_loss;
	}












	public String getR52_provi_ins_sec() {
		return r52_provi_ins_sec;
	}












	public void setR52_provi_ins_sec(String r52_provi_ins_sec) {
		this.r52_provi_ins_sec = r52_provi_ins_sec;
	}












	public BigDecimal getR52_provi_loan_loss() {
		return r52_provi_loan_loss;
	}












	public void setR52_provi_loan_loss(BigDecimal r52_provi_loan_loss) {
		this.r52_provi_loan_loss = r52_provi_loan_loss;
	}












	public String getR53_provi_ins_sec() {
		return r53_provi_ins_sec;
	}












	public void setR53_provi_ins_sec(String r53_provi_ins_sec) {
		this.r53_provi_ins_sec = r53_provi_ins_sec;
	}












	public BigDecimal getR53_provi_loan_loss() {
		return r53_provi_loan_loss;
	}












	public void setR53_provi_loan_loss(BigDecimal r53_provi_loan_loss) {
		this.r53_provi_loan_loss = r53_provi_loan_loss;
	}












	public String getR54_provi_ins_sec() {
		return r54_provi_ins_sec;
	}












	public void setR54_provi_ins_sec(String r54_provi_ins_sec) {
		this.r54_provi_ins_sec = r54_provi_ins_sec;
	}












	public BigDecimal getR54_provi_loan_loss() {
		return r54_provi_loan_loss;
	}












	public void setR54_provi_loan_loss(BigDecimal r54_provi_loan_loss) {
		this.r54_provi_loan_loss = r54_provi_loan_loss;
	}












	public String getR55_provi_ins_sec() {
		return r55_provi_ins_sec;
	}












	public void setR55_provi_ins_sec(String r55_provi_ins_sec) {
		this.r55_provi_ins_sec = r55_provi_ins_sec;
	}












	public BigDecimal getR55_provi_loan_loss() {
		return r55_provi_loan_loss;
	}












	public void setR55_provi_loan_loss(BigDecimal r55_provi_loan_loss) {
		this.r55_provi_loan_loss = r55_provi_loan_loss;
	}












	public String getR56_provi_ins_sec() {
		return r56_provi_ins_sec;
	}












	public void setR56_provi_ins_sec(String r56_provi_ins_sec) {
		this.r56_provi_ins_sec = r56_provi_ins_sec;
	}












	public BigDecimal getR56_provi_loan_loss() {
		return r56_provi_loan_loss;
	}












	public void setR56_provi_loan_loss(BigDecimal r56_provi_loan_loss) {
		this.r56_provi_loan_loss = r56_provi_loan_loss;
	}












	public String getR57_provi_ins_sec() {
		return r57_provi_ins_sec;
	}












	public void setR57_provi_ins_sec(String r57_provi_ins_sec) {
		this.r57_provi_ins_sec = r57_provi_ins_sec;
	}












	public BigDecimal getR57_provi_loan_loss() {
		return r57_provi_loan_loss;
	}












	public void setR57_provi_loan_loss(BigDecimal r57_provi_loan_loss) {
		this.r57_provi_loan_loss = r57_provi_loan_loss;
	}












	public String getR58_provi_ins_sec() {
		return r58_provi_ins_sec;
	}












	public void setR58_provi_ins_sec(String r58_provi_ins_sec) {
		this.r58_provi_ins_sec = r58_provi_ins_sec;
	}












	public BigDecimal getR58_provi_loan_loss() {
		return r58_provi_loan_loss;
	}












	public void setR58_provi_loan_loss(BigDecimal r58_provi_loan_loss) {
		this.r58_provi_loan_loss = r58_provi_loan_loss;
	}












	public String getR59_provi_ins_sec() {
		return r59_provi_ins_sec;
	}












	public void setR59_provi_ins_sec(String r59_provi_ins_sec) {
		this.r59_provi_ins_sec = r59_provi_ins_sec;
	}












	public BigDecimal getR59_provi_loan_loss() {
		return r59_provi_loan_loss;
	}












	public void setR59_provi_loan_loss(BigDecimal r59_provi_loan_loss) {
		this.r59_provi_loan_loss = r59_provi_loan_loss;
	}












	public String getR60_provi_ins_sec() {
		return r60_provi_ins_sec;
	}












	public void setR60_provi_ins_sec(String r60_provi_ins_sec) {
		this.r60_provi_ins_sec = r60_provi_ins_sec;
	}












	public BigDecimal getR60_provi_loan_loss() {
		return r60_provi_loan_loss;
	}












	public void setR60_provi_loan_loss(BigDecimal r60_provi_loan_loss) {
		this.r60_provi_loan_loss = r60_provi_loan_loss;
	}












	public String getR61_provi_ins_sec() {
		return r61_provi_ins_sec;
	}












	public void setR61_provi_ins_sec(String r61_provi_ins_sec) {
		this.r61_provi_ins_sec = r61_provi_ins_sec;
	}












	public BigDecimal getR61_provi_loan_loss() {
		return r61_provi_loan_loss;
	}












	public void setR61_provi_loan_loss(BigDecimal r61_provi_loan_loss) {
		this.r61_provi_loan_loss = r61_provi_loan_loss;
	}












	public String getR62_provi_ins_sec() {
		return r62_provi_ins_sec;
	}












	public void setR62_provi_ins_sec(String r62_provi_ins_sec) {
		this.r62_provi_ins_sec = r62_provi_ins_sec;
	}












	public BigDecimal getR62_provi_loan_loss() {
		return r62_provi_loan_loss;
	}












	public void setR62_provi_loan_loss(BigDecimal r62_provi_loan_loss) {
		this.r62_provi_loan_loss = r62_provi_loan_loss;
	}












	public String getR63_provi_ins_sec() {
		return r63_provi_ins_sec;
	}












	public void setR63_provi_ins_sec(String r63_provi_ins_sec) {
		this.r63_provi_ins_sec = r63_provi_ins_sec;
	}












	public BigDecimal getR63_provi_loan_loss() {
		return r63_provi_loan_loss;
	}












	public void setR63_provi_loan_loss(BigDecimal r63_provi_loan_loss) {
		this.r63_provi_loan_loss = r63_provi_loan_loss;
	}












	public String getR64_provi_ins_sec() {
		return r64_provi_ins_sec;
	}












	public void setR64_provi_ins_sec(String r64_provi_ins_sec) {
		this.r64_provi_ins_sec = r64_provi_ins_sec;
	}












	public BigDecimal getR64_provi_loan_loss() {
		return r64_provi_loan_loss;
	}












	public void setR64_provi_loan_loss(BigDecimal r64_provi_loan_loss) {
		this.r64_provi_loan_loss = r64_provi_loan_loss;
	}












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












	public M_PLL_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}


}
	