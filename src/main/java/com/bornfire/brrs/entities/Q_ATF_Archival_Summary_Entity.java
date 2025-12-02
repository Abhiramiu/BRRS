package com.bornfire.brrs.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "BRRS_Q_ATF_ARCHIVALTABLE_SUMMARY")


public class Q_ATF_Archival_Summary_Entity {
	

	private String	r11_num_by_inst_sec;
	private BigDecimal	r11_num_depo;
	private BigDecimal	r11_num_depo_acc;
	private BigDecimal	r11_num_borrowers;
	private BigDecimal	r11_num_loan_acc;
	private String	r12_num_by_inst_sec;
	private BigDecimal	r12_num_depo;
	private BigDecimal	r12_num_depo_acc;
	private BigDecimal	r12_num_borrowers;
	private BigDecimal	r12_num_loan_acc;
	private String	r13_num_by_inst_sec;
	private BigDecimal	r13_num_depo;
	private BigDecimal	r13_num_depo_acc;
	private BigDecimal	r13_num_borrowers;
	private BigDecimal	r13_num_loan_acc;
	private String	r14_num_by_inst_sec;
	private BigDecimal	r14_num_depo;
	private BigDecimal	r14_num_depo_acc;
	private BigDecimal	r14_num_borrowers;
	private BigDecimal	r14_num_loan_acc;
	private String	r15_num_by_inst_sec;
	private BigDecimal	r15_num_depo;
	private BigDecimal	r15_num_depo_acc;
	private BigDecimal	r15_num_borrowers;
	private BigDecimal	r15_num_loan_acc;
	private String	r16_num_by_inst_sec;
	private BigDecimal	r16_num_depo;
	private BigDecimal	r16_num_depo_acc;
	private BigDecimal	r16_num_borrowers;
	private BigDecimal	r16_num_loan_acc;
	private String	r17_num_by_inst_sec;
	private BigDecimal	r17_num_depo;
	private BigDecimal	r17_num_depo_acc;
	private BigDecimal	r17_num_borrowers;
	private BigDecimal	r17_num_loan_acc;
	private String	r18_num_by_inst_sec;
	private BigDecimal	r18_num_depo;
	private BigDecimal	r18_num_depo_acc;
	private BigDecimal	r18_num_borrowers;
	private BigDecimal	r18_num_loan_acc;
	private String	r19_num_by_inst_sec;
	private BigDecimal	r19_num_depo;
	private BigDecimal	r19_num_depo_acc;
	private BigDecimal	r19_num_borrowers;
	private BigDecimal	r19_num_loan_acc;
	private String	r20_num_by_inst_sec;
	private BigDecimal	r20_num_depo;
	private BigDecimal	r20_num_depo_acc;
	private BigDecimal	r20_num_borrowers;
	private BigDecimal	r20_num_loan_acc;
	private String	r21_num_by_inst_sec;
	private BigDecimal	r21_num_depo;
	private BigDecimal	r21_num_depo_acc;
	private BigDecimal	r21_num_borrowers;
	private BigDecimal	r21_num_loan_acc;
	private String	r22_num_by_inst_sec;
	private BigDecimal	r22_num_depo;
	private BigDecimal	r22_num_depo_acc;
	private BigDecimal	r22_num_borrowers;
	private BigDecimal	r22_num_loan_acc;
	private String	r23_num_by_inst_sec;
	private BigDecimal	r23_num_depo;
	private BigDecimal	r23_num_depo_acc;
	private BigDecimal	r23_num_borrowers;
	private BigDecimal	r23_num_loan_acc;
	private String	r24_num_by_inst_sec;
	private BigDecimal	r24_num_depo;
	private BigDecimal	r24_num_depo_acc;
	private BigDecimal	r24_num_borrowers;
	private BigDecimal	r24_num_loan_acc;
	private String	r25_num_by_inst_sec;
	private BigDecimal	r25_num_depo;
	private BigDecimal	r25_num_depo_acc;
	private BigDecimal	r25_num_borrowers;
	private BigDecimal	r25_num_loan_acc;
	private String	r26_num_by_inst_sec;
	private BigDecimal	r26_num_depo;
	private BigDecimal	r26_num_depo_acc;
	private BigDecimal	r26_num_borrowers;
	private BigDecimal	r26_num_loan_acc;
	private String	r27_num_by_inst_sec;
	private BigDecimal	r27_num_depo;
	private BigDecimal	r27_num_depo_acc;
	private BigDecimal	r27_num_borrowers;
	private BigDecimal	r27_num_loan_acc;
	private String	r28_num_by_inst_sec;
	private BigDecimal	r28_num_depo;
	private BigDecimal	r28_num_depo_acc;
	private BigDecimal	r28_num_borrowers;
	private BigDecimal	r28_num_loan_acc;
	private String	r29_num_by_inst_sec;
	private BigDecimal	r29_num_depo;
	private BigDecimal	r29_num_depo_acc;
	private BigDecimal	r29_num_borrowers;
	private BigDecimal	r29_num_loan_acc;
	private String	r30_num_by_inst_sec;
	private BigDecimal	r30_num_depo;
	private BigDecimal	r30_num_depo_acc;
	private BigDecimal	r30_num_borrowers;
	private BigDecimal	r30_num_loan_acc;
	private String	r31_num_by_inst_sec;
	private BigDecimal	r31_num_depo;
	private BigDecimal	r31_num_depo_acc;
	private BigDecimal	r31_num_borrowers;
	private BigDecimal	r31_num_loan_acc;
	private String	r32_num_by_inst_sec;
	private BigDecimal	r32_num_depo;
	private BigDecimal	r32_num_depo_acc;
	private BigDecimal	r32_num_borrowers;
	private BigDecimal	r32_num_loan_acc;
	private String	r33_num_by_inst_sec;
	private BigDecimal	r33_num_depo;
	private BigDecimal	r33_num_depo_acc;
	private BigDecimal	r33_num_borrowers;
	private BigDecimal	r33_num_loan_acc;
	private String	r34_num_by_inst_sec;
	private BigDecimal	r34_num_depo;
	private BigDecimal	r34_num_depo_acc;
	private BigDecimal	r34_num_borrowers;
	private BigDecimal	r34_num_loan_acc;
	private String	r35_num_by_inst_sec;
	private BigDecimal	r35_num_depo;
	private BigDecimal	r35_num_depo_acc;
	private BigDecimal	r35_num_borrowers;
	private BigDecimal	r35_num_loan_acc;
	private String	r36_num_by_inst_sec;
	private BigDecimal	r36_num_depo;
	private BigDecimal	r36_num_depo_acc;
	private BigDecimal	r36_num_borrowers;
	private BigDecimal	r36_num_loan_acc;
	private String	r37_num_by_inst_sec;
	private BigDecimal	r37_num_depo;
	private BigDecimal	r37_num_depo_acc;
	private BigDecimal	r37_num_borrowers;
	private BigDecimal	r37_num_loan_acc;
	private String	r38_num_by_inst_sec;
	private BigDecimal	r38_num_depo;
	private BigDecimal	r38_num_depo_acc;
	private BigDecimal	r38_num_borrowers;
	private BigDecimal	r38_num_loan_acc;
	private String	r39_num_by_inst_sec;
	private BigDecimal	r39_num_depo;
	private BigDecimal	r39_num_depo_acc;
	private BigDecimal	r39_num_borrowers;
	private BigDecimal	r39_num_loan_acc;
	private String	r40_num_by_inst_sec;
	private BigDecimal	r40_num_depo;
	private BigDecimal	r40_num_depo_acc;
	private BigDecimal	r40_num_borrowers;
	private BigDecimal	r40_num_loan_acc;
	private String	r41_num_by_inst_sec;
	private BigDecimal	r41_num_depo;
	private BigDecimal	r41_num_depo_acc;
	private BigDecimal	r41_num_borrowers;
	private BigDecimal	r41_num_loan_acc;
	private String	r42_num_by_inst_sec;
	private BigDecimal	r42_num_depo;
	private BigDecimal	r42_num_depo_acc;
	private BigDecimal	r42_num_borrowers;
	private BigDecimal	r42_num_loan_acc;
	private String	r43_num_by_inst_sec;
	private BigDecimal	r43_num_depo;
	private BigDecimal	r43_num_depo_acc;
	private BigDecimal	r43_num_borrowers;
	private BigDecimal	r43_num_loan_acc;
	private String	r44_num_by_inst_sec;
	private BigDecimal	r44_num_depo;
	private BigDecimal	r44_num_depo_acc;
	private BigDecimal	r44_num_borrowers;
	private BigDecimal	r44_num_loan_acc;
	private String	r45_num_by_inst_sec;
	private BigDecimal	r45_num_depo;
	private BigDecimal	r45_num_depo_acc;
	private BigDecimal	r45_num_borrowers;
	private BigDecimal	r45_num_loan_acc;
	private String	r46_num_by_inst_sec;
	private BigDecimal	r46_num_depo;
	private BigDecimal	r46_num_depo_acc;
	private BigDecimal	r46_num_borrowers;
	private BigDecimal	r46_num_loan_acc;
	private String	r47_num_by_inst_sec;
	private BigDecimal	r47_num_depo;
	private BigDecimal	r47_num_depo_acc;
	private BigDecimal	r47_num_borrowers;
	private BigDecimal	r47_num_loan_acc;
	private String	r48_num_by_inst_sec;
	private BigDecimal	r48_num_depo;
	private BigDecimal	r48_num_depo_acc;
	private BigDecimal	r48_num_borrowers;
	private BigDecimal	r48_num_loan_acc;
	private String	r49_num_by_inst_sec;
	private BigDecimal	r49_num_depo;
	private BigDecimal	r49_num_depo_acc;
	private BigDecimal	r49_num_borrowers;
	private BigDecimal	r49_num_loan_acc;
	private String	r50_num_by_inst_sec;
	private BigDecimal	r50_num_depo;
	private BigDecimal	r50_num_depo_acc;
	private BigDecimal	r50_num_borrowers;
	private BigDecimal	r50_num_loan_acc;
	private String	r51_num_by_inst_sec;
	private BigDecimal	r51_num_depo;
	private BigDecimal	r51_num_depo_acc;
	private BigDecimal	r51_num_borrowers;
	private BigDecimal	r51_num_loan_acc;
	private String	r52_num_by_inst_sec;
	private BigDecimal	r52_num_depo;
	private BigDecimal	r52_num_depo_acc;
	private BigDecimal	r52_num_borrowers;
	private BigDecimal	r52_num_loan_acc;
	private String	r53_num_by_inst_sec;
	private BigDecimal	r53_num_depo;
	private BigDecimal	r53_num_depo_acc;
	private BigDecimal	r53_num_borrowers;
	private BigDecimal	r53_num_loan_acc;
	private String	r54_num_by_inst_sec;
	private BigDecimal	r54_num_depo;
	private BigDecimal	r54_num_depo_acc;
	private BigDecimal	r54_num_borrowers;
	private BigDecimal	r54_num_loan_acc;
	private String	r55_num_by_inst_sec;
	private BigDecimal	r55_num_depo;
	private BigDecimal	r55_num_depo_acc;
	private BigDecimal	r55_num_borrowers;
	private BigDecimal	r55_num_loan_acc;
	private String	r56_num_by_inst_sec;
	private BigDecimal	r56_num_depo;
	private BigDecimal	r56_num_depo_acc;
	private BigDecimal	r56_num_borrowers;
	private BigDecimal	r56_num_loan_acc;
	private String	r57_num_by_inst_sec;
	private BigDecimal	r57_num_depo;
	private BigDecimal	r57_num_depo_acc;
	private BigDecimal	r57_num_borrowers;
	private BigDecimal	r57_num_loan_acc;
	private String	r58_num_by_inst_sec;
	private BigDecimal	r58_num_depo;
	private BigDecimal	r58_num_depo_acc;
	private BigDecimal	r58_num_borrowers;
	private BigDecimal	r58_num_loan_acc;
	private String	r59_num_by_inst_sec;
	private BigDecimal	r59_num_depo;
	private BigDecimal	r59_num_depo_acc;
	private BigDecimal	r59_num_borrowers;
	private BigDecimal	r59_num_loan_acc;
	private String	r60_num_by_inst_sec;
	private BigDecimal	r60_num_depo;
	private BigDecimal	r60_num_depo_acc;
	private BigDecimal	r60_num_borrowers;
	private BigDecimal	r60_num_loan_acc;
	private String	r61_num_by_inst_sec;
	private BigDecimal	r61_num_depo;
	private BigDecimal	r61_num_depo_acc;
	private BigDecimal	r61_num_borrowers;
	private BigDecimal	r61_num_loan_acc;
	private String	r62_num_by_inst_sec;
	private BigDecimal	r62_num_depo;
	private BigDecimal	r62_num_depo_acc;
	private BigDecimal	r62_num_borrowers;
	private BigDecimal	r62_num_loan_acc;
	private String	r63_num_by_inst_sec;
	private BigDecimal	r63_num_depo;
	private BigDecimal	r63_num_depo_acc;
	private BigDecimal	r63_num_borrowers;
	private BigDecimal	r63_num_loan_acc;
	private String	r64_num_by_inst_sec;
	private BigDecimal	r64_num_depo;
	private BigDecimal	r64_num_depo_acc;
	private BigDecimal	r64_num_borrowers;
	private BigDecimal	r64_num_loan_acc;

	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Id
    private Date report_date;

    private String	report_version;
    private String	report_frequency;
    private String	report_code;
    private String	report_desc;
    private String	entity_flg;
    private String	modify_flg;
    private String	del_flg;
	public String getR11_num_by_inst_sec() {
		return r11_num_by_inst_sec;
	}
	public void setR11_num_by_inst_sec(String r11_num_by_inst_sec) {
		this.r11_num_by_inst_sec = r11_num_by_inst_sec;
	}
	public BigDecimal getR11_num_depo() {
		return r11_num_depo;
	}
	public void setR11_num_depo(BigDecimal r11_num_depo) {
		this.r11_num_depo = r11_num_depo;
	}
	public BigDecimal getR11_num_depo_acc() {
		return r11_num_depo_acc;
	}
	public void setR11_num_depo_acc(BigDecimal r11_num_depo_acc) {
		this.r11_num_depo_acc = r11_num_depo_acc;
	}
	public BigDecimal getR11_num_borrowers() {
		return r11_num_borrowers;
	}
	public void setR11_num_borrowers(BigDecimal r11_num_borrowers) {
		this.r11_num_borrowers = r11_num_borrowers;
	}
	public BigDecimal getR11_num_loan_acc() {
		return r11_num_loan_acc;
	}
	public void setR11_num_loan_acc(BigDecimal r11_num_loan_acc) {
		this.r11_num_loan_acc = r11_num_loan_acc;
	}
	public String getR12_num_by_inst_sec() {
		return r12_num_by_inst_sec;
	}
	public void setR12_num_by_inst_sec(String r12_num_by_inst_sec) {
		this.r12_num_by_inst_sec = r12_num_by_inst_sec;
	}
	public BigDecimal getR12_num_depo() {
		return r12_num_depo;
	}
	public void setR12_num_depo(BigDecimal r12_num_depo) {
		this.r12_num_depo = r12_num_depo;
	}
	public BigDecimal getR12_num_depo_acc() {
		return r12_num_depo_acc;
	}
	public void setR12_num_depo_acc(BigDecimal r12_num_depo_acc) {
		this.r12_num_depo_acc = r12_num_depo_acc;
	}
	public BigDecimal getR12_num_borrowers() {
		return r12_num_borrowers;
	}
	public void setR12_num_borrowers(BigDecimal r12_num_borrowers) {
		this.r12_num_borrowers = r12_num_borrowers;
	}
	public BigDecimal getR12_num_loan_acc() {
		return r12_num_loan_acc;
	}
	public void setR12_num_loan_acc(BigDecimal r12_num_loan_acc) {
		this.r12_num_loan_acc = r12_num_loan_acc;
	}
	public String getR13_num_by_inst_sec() {
		return r13_num_by_inst_sec;
	}
	public void setR13_num_by_inst_sec(String r13_num_by_inst_sec) {
		this.r13_num_by_inst_sec = r13_num_by_inst_sec;
	}
	public BigDecimal getR13_num_depo() {
		return r13_num_depo;
	}
	public void setR13_num_depo(BigDecimal r13_num_depo) {
		this.r13_num_depo = r13_num_depo;
	}
	public BigDecimal getR13_num_depo_acc() {
		return r13_num_depo_acc;
	}
	public void setR13_num_depo_acc(BigDecimal r13_num_depo_acc) {
		this.r13_num_depo_acc = r13_num_depo_acc;
	}
	public BigDecimal getR13_num_borrowers() {
		return r13_num_borrowers;
	}
	public void setR13_num_borrowers(BigDecimal r13_num_borrowers) {
		this.r13_num_borrowers = r13_num_borrowers;
	}
	public BigDecimal getR13_num_loan_acc() {
		return r13_num_loan_acc;
	}
	public void setR13_num_loan_acc(BigDecimal r13_num_loan_acc) {
		this.r13_num_loan_acc = r13_num_loan_acc;
	}
	public String getR14_num_by_inst_sec() {
		return r14_num_by_inst_sec;
	}
	public void setR14_num_by_inst_sec(String r14_num_by_inst_sec) {
		this.r14_num_by_inst_sec = r14_num_by_inst_sec;
	}
	public BigDecimal getR14_num_depo() {
		return r14_num_depo;
	}
	public void setR14_num_depo(BigDecimal r14_num_depo) {
		this.r14_num_depo = r14_num_depo;
	}
	public BigDecimal getR14_num_depo_acc() {
		return r14_num_depo_acc;
	}
	public void setR14_num_depo_acc(BigDecimal r14_num_depo_acc) {
		this.r14_num_depo_acc = r14_num_depo_acc;
	}
	public BigDecimal getR14_num_borrowers() {
		return r14_num_borrowers;
	}
	public void setR14_num_borrowers(BigDecimal r14_num_borrowers) {
		this.r14_num_borrowers = r14_num_borrowers;
	}
	public BigDecimal getR14_num_loan_acc() {
		return r14_num_loan_acc;
	}
	public void setR14_num_loan_acc(BigDecimal r14_num_loan_acc) {
		this.r14_num_loan_acc = r14_num_loan_acc;
	}
	public String getR15_num_by_inst_sec() {
		return r15_num_by_inst_sec;
	}
	public void setR15_num_by_inst_sec(String r15_num_by_inst_sec) {
		this.r15_num_by_inst_sec = r15_num_by_inst_sec;
	}
	public BigDecimal getR15_num_depo() {
		return r15_num_depo;
	}
	public void setR15_num_depo(BigDecimal r15_num_depo) {
		this.r15_num_depo = r15_num_depo;
	}
	public BigDecimal getR15_num_depo_acc() {
		return r15_num_depo_acc;
	}
	public void setR15_num_depo_acc(BigDecimal r15_num_depo_acc) {
		this.r15_num_depo_acc = r15_num_depo_acc;
	}
	public BigDecimal getR15_num_borrowers() {
		return r15_num_borrowers;
	}
	public void setR15_num_borrowers(BigDecimal r15_num_borrowers) {
		this.r15_num_borrowers = r15_num_borrowers;
	}
	public BigDecimal getR15_num_loan_acc() {
		return r15_num_loan_acc;
	}
	public void setR15_num_loan_acc(BigDecimal r15_num_loan_acc) {
		this.r15_num_loan_acc = r15_num_loan_acc;
	}
	public String getR16_num_by_inst_sec() {
		return r16_num_by_inst_sec;
	}
	public void setR16_num_by_inst_sec(String r16_num_by_inst_sec) {
		this.r16_num_by_inst_sec = r16_num_by_inst_sec;
	}
	public BigDecimal getR16_num_depo() {
		return r16_num_depo;
	}
	public void setR16_num_depo(BigDecimal r16_num_depo) {
		this.r16_num_depo = r16_num_depo;
	}
	public BigDecimal getR16_num_depo_acc() {
		return r16_num_depo_acc;
	}
	public void setR16_num_depo_acc(BigDecimal r16_num_depo_acc) {
		this.r16_num_depo_acc = r16_num_depo_acc;
	}
	public BigDecimal getR16_num_borrowers() {
		return r16_num_borrowers;
	}
	public void setR16_num_borrowers(BigDecimal r16_num_borrowers) {
		this.r16_num_borrowers = r16_num_borrowers;
	}
	public BigDecimal getR16_num_loan_acc() {
		return r16_num_loan_acc;
	}
	public void setR16_num_loan_acc(BigDecimal r16_num_loan_acc) {
		this.r16_num_loan_acc = r16_num_loan_acc;
	}
	public String getR17_num_by_inst_sec() {
		return r17_num_by_inst_sec;
	}
	public void setR17_num_by_inst_sec(String r17_num_by_inst_sec) {
		this.r17_num_by_inst_sec = r17_num_by_inst_sec;
	}
	public BigDecimal getR17_num_depo() {
		return r17_num_depo;
	}
	public void setR17_num_depo(BigDecimal r17_num_depo) {
		this.r17_num_depo = r17_num_depo;
	}
	public BigDecimal getR17_num_depo_acc() {
		return r17_num_depo_acc;
	}
	public void setR17_num_depo_acc(BigDecimal r17_num_depo_acc) {
		this.r17_num_depo_acc = r17_num_depo_acc;
	}
	public BigDecimal getR17_num_borrowers() {
		return r17_num_borrowers;
	}
	public void setR17_num_borrowers(BigDecimal r17_num_borrowers) {
		this.r17_num_borrowers = r17_num_borrowers;
	}
	public BigDecimal getR17_num_loan_acc() {
		return r17_num_loan_acc;
	}
	public void setR17_num_loan_acc(BigDecimal r17_num_loan_acc) {
		this.r17_num_loan_acc = r17_num_loan_acc;
	}
	public String getR18_num_by_inst_sec() {
		return r18_num_by_inst_sec;
	}
	public void setR18_num_by_inst_sec(String r18_num_by_inst_sec) {
		this.r18_num_by_inst_sec = r18_num_by_inst_sec;
	}
	public BigDecimal getR18_num_depo() {
		return r18_num_depo;
	}
	public void setR18_num_depo(BigDecimal r18_num_depo) {
		this.r18_num_depo = r18_num_depo;
	}
	public BigDecimal getR18_num_depo_acc() {
		return r18_num_depo_acc;
	}
	public void setR18_num_depo_acc(BigDecimal r18_num_depo_acc) {
		this.r18_num_depo_acc = r18_num_depo_acc;
	}
	public BigDecimal getR18_num_borrowers() {
		return r18_num_borrowers;
	}
	public void setR18_num_borrowers(BigDecimal r18_num_borrowers) {
		this.r18_num_borrowers = r18_num_borrowers;
	}
	public BigDecimal getR18_num_loan_acc() {
		return r18_num_loan_acc;
	}
	public void setR18_num_loan_acc(BigDecimal r18_num_loan_acc) {
		this.r18_num_loan_acc = r18_num_loan_acc;
	}
	public String getR19_num_by_inst_sec() {
		return r19_num_by_inst_sec;
	}
	public void setR19_num_by_inst_sec(String r19_num_by_inst_sec) {
		this.r19_num_by_inst_sec = r19_num_by_inst_sec;
	}
	public BigDecimal getR19_num_depo() {
		return r19_num_depo;
	}
	public void setR19_num_depo(BigDecimal r19_num_depo) {
		this.r19_num_depo = r19_num_depo;
	}
	public BigDecimal getR19_num_depo_acc() {
		return r19_num_depo_acc;
	}
	public void setR19_num_depo_acc(BigDecimal r19_num_depo_acc) {
		this.r19_num_depo_acc = r19_num_depo_acc;
	}
	public BigDecimal getR19_num_borrowers() {
		return r19_num_borrowers;
	}
	public void setR19_num_borrowers(BigDecimal r19_num_borrowers) {
		this.r19_num_borrowers = r19_num_borrowers;
	}
	public BigDecimal getR19_num_loan_acc() {
		return r19_num_loan_acc;
	}
	public void setR19_num_loan_acc(BigDecimal r19_num_loan_acc) {
		this.r19_num_loan_acc = r19_num_loan_acc;
	}
	public String getR20_num_by_inst_sec() {
		return r20_num_by_inst_sec;
	}
	public void setR20_num_by_inst_sec(String r20_num_by_inst_sec) {
		this.r20_num_by_inst_sec = r20_num_by_inst_sec;
	}
	public BigDecimal getR20_num_depo() {
		return r20_num_depo;
	}
	public void setR20_num_depo(BigDecimal r20_num_depo) {
		this.r20_num_depo = r20_num_depo;
	}
	public BigDecimal getR20_num_depo_acc() {
		return r20_num_depo_acc;
	}
	public void setR20_num_depo_acc(BigDecimal r20_num_depo_acc) {
		this.r20_num_depo_acc = r20_num_depo_acc;
	}
	public BigDecimal getR20_num_borrowers() {
		return r20_num_borrowers;
	}
	public void setR20_num_borrowers(BigDecimal r20_num_borrowers) {
		this.r20_num_borrowers = r20_num_borrowers;
	}
	public BigDecimal getR20_num_loan_acc() {
		return r20_num_loan_acc;
	}
	public void setR20_num_loan_acc(BigDecimal r20_num_loan_acc) {
		this.r20_num_loan_acc = r20_num_loan_acc;
	}
	public String getR21_num_by_inst_sec() {
		return r21_num_by_inst_sec;
	}
	public void setR21_num_by_inst_sec(String r21_num_by_inst_sec) {
		this.r21_num_by_inst_sec = r21_num_by_inst_sec;
	}
	public BigDecimal getR21_num_depo() {
		return r21_num_depo;
	}
	public void setR21_num_depo(BigDecimal r21_num_depo) {
		this.r21_num_depo = r21_num_depo;
	}
	public BigDecimal getR21_num_depo_acc() {
		return r21_num_depo_acc;
	}
	public void setR21_num_depo_acc(BigDecimal r21_num_depo_acc) {
		this.r21_num_depo_acc = r21_num_depo_acc;
	}
	public BigDecimal getR21_num_borrowers() {
		return r21_num_borrowers;
	}
	public void setR21_num_borrowers(BigDecimal r21_num_borrowers) {
		this.r21_num_borrowers = r21_num_borrowers;
	}
	public BigDecimal getR21_num_loan_acc() {
		return r21_num_loan_acc;
	}
	public void setR21_num_loan_acc(BigDecimal r21_num_loan_acc) {
		this.r21_num_loan_acc = r21_num_loan_acc;
	}
	public String getR22_num_by_inst_sec() {
		return r22_num_by_inst_sec;
	}
	public void setR22_num_by_inst_sec(String r22_num_by_inst_sec) {
		this.r22_num_by_inst_sec = r22_num_by_inst_sec;
	}
	public BigDecimal getR22_num_depo() {
		return r22_num_depo;
	}
	public void setR22_num_depo(BigDecimal r22_num_depo) {
		this.r22_num_depo = r22_num_depo;
	}
	public BigDecimal getR22_num_depo_acc() {
		return r22_num_depo_acc;
	}
	public void setR22_num_depo_acc(BigDecimal r22_num_depo_acc) {
		this.r22_num_depo_acc = r22_num_depo_acc;
	}
	public BigDecimal getR22_num_borrowers() {
		return r22_num_borrowers;
	}
	public void setR22_num_borrowers(BigDecimal r22_num_borrowers) {
		this.r22_num_borrowers = r22_num_borrowers;
	}
	public BigDecimal getR22_num_loan_acc() {
		return r22_num_loan_acc;
	}
	public void setR22_num_loan_acc(BigDecimal r22_num_loan_acc) {
		this.r22_num_loan_acc = r22_num_loan_acc;
	}
	public String getR23_num_by_inst_sec() {
		return r23_num_by_inst_sec;
	}
	public void setR23_num_by_inst_sec(String r23_num_by_inst_sec) {
		this.r23_num_by_inst_sec = r23_num_by_inst_sec;
	}
	public BigDecimal getR23_num_depo() {
		return r23_num_depo;
	}
	public void setR23_num_depo(BigDecimal r23_num_depo) {
		this.r23_num_depo = r23_num_depo;
	}
	public BigDecimal getR23_num_depo_acc() {
		return r23_num_depo_acc;
	}
	public void setR23_num_depo_acc(BigDecimal r23_num_depo_acc) {
		this.r23_num_depo_acc = r23_num_depo_acc;
	}
	public BigDecimal getR23_num_borrowers() {
		return r23_num_borrowers;
	}
	public void setR23_num_borrowers(BigDecimal r23_num_borrowers) {
		this.r23_num_borrowers = r23_num_borrowers;
	}
	public BigDecimal getR23_num_loan_acc() {
		return r23_num_loan_acc;
	}
	public void setR23_num_loan_acc(BigDecimal r23_num_loan_acc) {
		this.r23_num_loan_acc = r23_num_loan_acc;
	}
	public String getR24_num_by_inst_sec() {
		return r24_num_by_inst_sec;
	}
	public void setR24_num_by_inst_sec(String r24_num_by_inst_sec) {
		this.r24_num_by_inst_sec = r24_num_by_inst_sec;
	}
	public BigDecimal getR24_num_depo() {
		return r24_num_depo;
	}
	public void setR24_num_depo(BigDecimal r24_num_depo) {
		this.r24_num_depo = r24_num_depo;
	}
	public BigDecimal getR24_num_depo_acc() {
		return r24_num_depo_acc;
	}
	public void setR24_num_depo_acc(BigDecimal r24_num_depo_acc) {
		this.r24_num_depo_acc = r24_num_depo_acc;
	}
	public BigDecimal getR24_num_borrowers() {
		return r24_num_borrowers;
	}
	public void setR24_num_borrowers(BigDecimal r24_num_borrowers) {
		this.r24_num_borrowers = r24_num_borrowers;
	}
	public BigDecimal getR24_num_loan_acc() {
		return r24_num_loan_acc;
	}
	public void setR24_num_loan_acc(BigDecimal r24_num_loan_acc) {
		this.r24_num_loan_acc = r24_num_loan_acc;
	}
	public String getR25_num_by_inst_sec() {
		return r25_num_by_inst_sec;
	}
	public void setR25_num_by_inst_sec(String r25_num_by_inst_sec) {
		this.r25_num_by_inst_sec = r25_num_by_inst_sec;
	}
	public BigDecimal getR25_num_depo() {
		return r25_num_depo;
	}
	public void setR25_num_depo(BigDecimal r25_num_depo) {
		this.r25_num_depo = r25_num_depo;
	}
	public BigDecimal getR25_num_depo_acc() {
		return r25_num_depo_acc;
	}
	public void setR25_num_depo_acc(BigDecimal r25_num_depo_acc) {
		this.r25_num_depo_acc = r25_num_depo_acc;
	}
	public BigDecimal getR25_num_borrowers() {
		return r25_num_borrowers;
	}
	public void setR25_num_borrowers(BigDecimal r25_num_borrowers) {
		this.r25_num_borrowers = r25_num_borrowers;
	}
	public BigDecimal getR25_num_loan_acc() {
		return r25_num_loan_acc;
	}
	public void setR25_num_loan_acc(BigDecimal r25_num_loan_acc) {
		this.r25_num_loan_acc = r25_num_loan_acc;
	}
	public String getR26_num_by_inst_sec() {
		return r26_num_by_inst_sec;
	}
	public void setR26_num_by_inst_sec(String r26_num_by_inst_sec) {
		this.r26_num_by_inst_sec = r26_num_by_inst_sec;
	}
	public BigDecimal getR26_num_depo() {
		return r26_num_depo;
	}
	public void setR26_num_depo(BigDecimal r26_num_depo) {
		this.r26_num_depo = r26_num_depo;
	}
	public BigDecimal getR26_num_depo_acc() {
		return r26_num_depo_acc;
	}
	public void setR26_num_depo_acc(BigDecimal r26_num_depo_acc) {
		this.r26_num_depo_acc = r26_num_depo_acc;
	}
	public BigDecimal getR26_num_borrowers() {
		return r26_num_borrowers;
	}
	public void setR26_num_borrowers(BigDecimal r26_num_borrowers) {
		this.r26_num_borrowers = r26_num_borrowers;
	}
	public BigDecimal getR26_num_loan_acc() {
		return r26_num_loan_acc;
	}
	public void setR26_num_loan_acc(BigDecimal r26_num_loan_acc) {
		this.r26_num_loan_acc = r26_num_loan_acc;
	}
	public String getR27_num_by_inst_sec() {
		return r27_num_by_inst_sec;
	}
	public void setR27_num_by_inst_sec(String r27_num_by_inst_sec) {
		this.r27_num_by_inst_sec = r27_num_by_inst_sec;
	}
	public BigDecimal getR27_num_depo() {
		return r27_num_depo;
	}
	public void setR27_num_depo(BigDecimal r27_num_depo) {
		this.r27_num_depo = r27_num_depo;
	}
	public BigDecimal getR27_num_depo_acc() {
		return r27_num_depo_acc;
	}
	public void setR27_num_depo_acc(BigDecimal r27_num_depo_acc) {
		this.r27_num_depo_acc = r27_num_depo_acc;
	}
	public BigDecimal getR27_num_borrowers() {
		return r27_num_borrowers;
	}
	public void setR27_num_borrowers(BigDecimal r27_num_borrowers) {
		this.r27_num_borrowers = r27_num_borrowers;
	}
	public BigDecimal getR27_num_loan_acc() {
		return r27_num_loan_acc;
	}
	public void setR27_num_loan_acc(BigDecimal r27_num_loan_acc) {
		this.r27_num_loan_acc = r27_num_loan_acc;
	}
	public String getR28_num_by_inst_sec() {
		return r28_num_by_inst_sec;
	}
	public void setR28_num_by_inst_sec(String r28_num_by_inst_sec) {
		this.r28_num_by_inst_sec = r28_num_by_inst_sec;
	}
	public BigDecimal getR28_num_depo() {
		return r28_num_depo;
	}
	public void setR28_num_depo(BigDecimal r28_num_depo) {
		this.r28_num_depo = r28_num_depo;
	}
	public BigDecimal getR28_num_depo_acc() {
		return r28_num_depo_acc;
	}
	public void setR28_num_depo_acc(BigDecimal r28_num_depo_acc) {
		this.r28_num_depo_acc = r28_num_depo_acc;
	}
	public BigDecimal getR28_num_borrowers() {
		return r28_num_borrowers;
	}
	public void setR28_num_borrowers(BigDecimal r28_num_borrowers) {
		this.r28_num_borrowers = r28_num_borrowers;
	}
	public BigDecimal getR28_num_loan_acc() {
		return r28_num_loan_acc;
	}
	public void setR28_num_loan_acc(BigDecimal r28_num_loan_acc) {
		this.r28_num_loan_acc = r28_num_loan_acc;
	}
	public String getR29_num_by_inst_sec() {
		return r29_num_by_inst_sec;
	}
	public void setR29_num_by_inst_sec(String r29_num_by_inst_sec) {
		this.r29_num_by_inst_sec = r29_num_by_inst_sec;
	}
	public BigDecimal getR29_num_depo() {
		return r29_num_depo;
	}
	public void setR29_num_depo(BigDecimal r29_num_depo) {
		this.r29_num_depo = r29_num_depo;
	}
	public BigDecimal getR29_num_depo_acc() {
		return r29_num_depo_acc;
	}
	public void setR29_num_depo_acc(BigDecimal r29_num_depo_acc) {
		this.r29_num_depo_acc = r29_num_depo_acc;
	}
	public BigDecimal getR29_num_borrowers() {
		return r29_num_borrowers;
	}
	public void setR29_num_borrowers(BigDecimal r29_num_borrowers) {
		this.r29_num_borrowers = r29_num_borrowers;
	}
	public BigDecimal getR29_num_loan_acc() {
		return r29_num_loan_acc;
	}
	public void setR29_num_loan_acc(BigDecimal r29_num_loan_acc) {
		this.r29_num_loan_acc = r29_num_loan_acc;
	}
	public String getR30_num_by_inst_sec() {
		return r30_num_by_inst_sec;
	}
	public void setR30_num_by_inst_sec(String r30_num_by_inst_sec) {
		this.r30_num_by_inst_sec = r30_num_by_inst_sec;
	}
	public BigDecimal getR30_num_depo() {
		return r30_num_depo;
	}
	public void setR30_num_depo(BigDecimal r30_num_depo) {
		this.r30_num_depo = r30_num_depo;
	}
	public BigDecimal getR30_num_depo_acc() {
		return r30_num_depo_acc;
	}
	public void setR30_num_depo_acc(BigDecimal r30_num_depo_acc) {
		this.r30_num_depo_acc = r30_num_depo_acc;
	}
	public BigDecimal getR30_num_borrowers() {
		return r30_num_borrowers;
	}
	public void setR30_num_borrowers(BigDecimal r30_num_borrowers) {
		this.r30_num_borrowers = r30_num_borrowers;
	}
	public BigDecimal getR30_num_loan_acc() {
		return r30_num_loan_acc;
	}
	public void setR30_num_loan_acc(BigDecimal r30_num_loan_acc) {
		this.r30_num_loan_acc = r30_num_loan_acc;
	}
	public String getR31_num_by_inst_sec() {
		return r31_num_by_inst_sec;
	}
	public void setR31_num_by_inst_sec(String r31_num_by_inst_sec) {
		this.r31_num_by_inst_sec = r31_num_by_inst_sec;
	}
	public BigDecimal getR31_num_depo() {
		return r31_num_depo;
	}
	public void setR31_num_depo(BigDecimal r31_num_depo) {
		this.r31_num_depo = r31_num_depo;
	}
	public BigDecimal getR31_num_depo_acc() {
		return r31_num_depo_acc;
	}
	public void setR31_num_depo_acc(BigDecimal r31_num_depo_acc) {
		this.r31_num_depo_acc = r31_num_depo_acc;
	}
	public BigDecimal getR31_num_borrowers() {
		return r31_num_borrowers;
	}
	public void setR31_num_borrowers(BigDecimal r31_num_borrowers) {
		this.r31_num_borrowers = r31_num_borrowers;
	}
	public BigDecimal getR31_num_loan_acc() {
		return r31_num_loan_acc;
	}
	public void setR31_num_loan_acc(BigDecimal r31_num_loan_acc) {
		this.r31_num_loan_acc = r31_num_loan_acc;
	}
	public String getR32_num_by_inst_sec() {
		return r32_num_by_inst_sec;
	}
	public void setR32_num_by_inst_sec(String r32_num_by_inst_sec) {
		this.r32_num_by_inst_sec = r32_num_by_inst_sec;
	}
	public BigDecimal getR32_num_depo() {
		return r32_num_depo;
	}
	public void setR32_num_depo(BigDecimal r32_num_depo) {
		this.r32_num_depo = r32_num_depo;
	}
	public BigDecimal getR32_num_depo_acc() {
		return r32_num_depo_acc;
	}
	public void setR32_num_depo_acc(BigDecimal r32_num_depo_acc) {
		this.r32_num_depo_acc = r32_num_depo_acc;
	}
	public BigDecimal getR32_num_borrowers() {
		return r32_num_borrowers;
	}
	public void setR32_num_borrowers(BigDecimal r32_num_borrowers) {
		this.r32_num_borrowers = r32_num_borrowers;
	}
	public BigDecimal getR32_num_loan_acc() {
		return r32_num_loan_acc;
	}
	public void setR32_num_loan_acc(BigDecimal r32_num_loan_acc) {
		this.r32_num_loan_acc = r32_num_loan_acc;
	}
	public String getR33_num_by_inst_sec() {
		return r33_num_by_inst_sec;
	}
	public void setR33_num_by_inst_sec(String r33_num_by_inst_sec) {
		this.r33_num_by_inst_sec = r33_num_by_inst_sec;
	}
	public BigDecimal getR33_num_depo() {
		return r33_num_depo;
	}
	public void setR33_num_depo(BigDecimal r33_num_depo) {
		this.r33_num_depo = r33_num_depo;
	}
	public BigDecimal getR33_num_depo_acc() {
		return r33_num_depo_acc;
	}
	public void setR33_num_depo_acc(BigDecimal r33_num_depo_acc) {
		this.r33_num_depo_acc = r33_num_depo_acc;
	}
	public BigDecimal getR33_num_borrowers() {
		return r33_num_borrowers;
	}
	public void setR33_num_borrowers(BigDecimal r33_num_borrowers) {
		this.r33_num_borrowers = r33_num_borrowers;
	}
	public BigDecimal getR33_num_loan_acc() {
		return r33_num_loan_acc;
	}
	public void setR33_num_loan_acc(BigDecimal r33_num_loan_acc) {
		this.r33_num_loan_acc = r33_num_loan_acc;
	}
	public String getR34_num_by_inst_sec() {
		return r34_num_by_inst_sec;
	}
	public void setR34_num_by_inst_sec(String r34_num_by_inst_sec) {
		this.r34_num_by_inst_sec = r34_num_by_inst_sec;
	}
	public BigDecimal getR34_num_depo() {
		return r34_num_depo;
	}
	public void setR34_num_depo(BigDecimal r34_num_depo) {
		this.r34_num_depo = r34_num_depo;
	}
	public BigDecimal getR34_num_depo_acc() {
		return r34_num_depo_acc;
	}
	public void setR34_num_depo_acc(BigDecimal r34_num_depo_acc) {
		this.r34_num_depo_acc = r34_num_depo_acc;
	}
	public BigDecimal getR34_num_borrowers() {
		return r34_num_borrowers;
	}
	public void setR34_num_borrowers(BigDecimal r34_num_borrowers) {
		this.r34_num_borrowers = r34_num_borrowers;
	}
	public BigDecimal getR34_num_loan_acc() {
		return r34_num_loan_acc;
	}
	public void setR34_num_loan_acc(BigDecimal r34_num_loan_acc) {
		this.r34_num_loan_acc = r34_num_loan_acc;
	}
	public String getR35_num_by_inst_sec() {
		return r35_num_by_inst_sec;
	}
	public void setR35_num_by_inst_sec(String r35_num_by_inst_sec) {
		this.r35_num_by_inst_sec = r35_num_by_inst_sec;
	}
	public BigDecimal getR35_num_depo() {
		return r35_num_depo;
	}
	public void setR35_num_depo(BigDecimal r35_num_depo) {
		this.r35_num_depo = r35_num_depo;
	}
	public BigDecimal getR35_num_depo_acc() {
		return r35_num_depo_acc;
	}
	public void setR35_num_depo_acc(BigDecimal r35_num_depo_acc) {
		this.r35_num_depo_acc = r35_num_depo_acc;
	}
	public BigDecimal getR35_num_borrowers() {
		return r35_num_borrowers;
	}
	public void setR35_num_borrowers(BigDecimal r35_num_borrowers) {
		this.r35_num_borrowers = r35_num_borrowers;
	}
	public BigDecimal getR35_num_loan_acc() {
		return r35_num_loan_acc;
	}
	public void setR35_num_loan_acc(BigDecimal r35_num_loan_acc) {
		this.r35_num_loan_acc = r35_num_loan_acc;
	}
	public String getR36_num_by_inst_sec() {
		return r36_num_by_inst_sec;
	}
	public void setR36_num_by_inst_sec(String r36_num_by_inst_sec) {
		this.r36_num_by_inst_sec = r36_num_by_inst_sec;
	}
	public BigDecimal getR36_num_depo() {
		return r36_num_depo;
	}
	public void setR36_num_depo(BigDecimal r36_num_depo) {
		this.r36_num_depo = r36_num_depo;
	}
	public BigDecimal getR36_num_depo_acc() {
		return r36_num_depo_acc;
	}
	public void setR36_num_depo_acc(BigDecimal r36_num_depo_acc) {
		this.r36_num_depo_acc = r36_num_depo_acc;
	}
	public BigDecimal getR36_num_borrowers() {
		return r36_num_borrowers;
	}
	public void setR36_num_borrowers(BigDecimal r36_num_borrowers) {
		this.r36_num_borrowers = r36_num_borrowers;
	}
	public BigDecimal getR36_num_loan_acc() {
		return r36_num_loan_acc;
	}
	public void setR36_num_loan_acc(BigDecimal r36_num_loan_acc) {
		this.r36_num_loan_acc = r36_num_loan_acc;
	}
	public String getR37_num_by_inst_sec() {
		return r37_num_by_inst_sec;
	}
	public void setR37_num_by_inst_sec(String r37_num_by_inst_sec) {
		this.r37_num_by_inst_sec = r37_num_by_inst_sec;
	}
	public BigDecimal getR37_num_depo() {
		return r37_num_depo;
	}
	public void setR37_num_depo(BigDecimal r37_num_depo) {
		this.r37_num_depo = r37_num_depo;
	}
	public BigDecimal getR37_num_depo_acc() {
		return r37_num_depo_acc;
	}
	public void setR37_num_depo_acc(BigDecimal r37_num_depo_acc) {
		this.r37_num_depo_acc = r37_num_depo_acc;
	}
	public BigDecimal getR37_num_borrowers() {
		return r37_num_borrowers;
	}
	public void setR37_num_borrowers(BigDecimal r37_num_borrowers) {
		this.r37_num_borrowers = r37_num_borrowers;
	}
	public BigDecimal getR37_num_loan_acc() {
		return r37_num_loan_acc;
	}
	public void setR37_num_loan_acc(BigDecimal r37_num_loan_acc) {
		this.r37_num_loan_acc = r37_num_loan_acc;
	}
	public String getR38_num_by_inst_sec() {
		return r38_num_by_inst_sec;
	}
	public void setR38_num_by_inst_sec(String r38_num_by_inst_sec) {
		this.r38_num_by_inst_sec = r38_num_by_inst_sec;
	}
	public BigDecimal getR38_num_depo() {
		return r38_num_depo;
	}
	public void setR38_num_depo(BigDecimal r38_num_depo) {
		this.r38_num_depo = r38_num_depo;
	}
	public BigDecimal getR38_num_depo_acc() {
		return r38_num_depo_acc;
	}
	public void setR38_num_depo_acc(BigDecimal r38_num_depo_acc) {
		this.r38_num_depo_acc = r38_num_depo_acc;
	}
	public BigDecimal getR38_num_borrowers() {
		return r38_num_borrowers;
	}
	public void setR38_num_borrowers(BigDecimal r38_num_borrowers) {
		this.r38_num_borrowers = r38_num_borrowers;
	}
	public BigDecimal getR38_num_loan_acc() {
		return r38_num_loan_acc;
	}
	public void setR38_num_loan_acc(BigDecimal r38_num_loan_acc) {
		this.r38_num_loan_acc = r38_num_loan_acc;
	}
	public String getR39_num_by_inst_sec() {
		return r39_num_by_inst_sec;
	}
	public void setR39_num_by_inst_sec(String r39_num_by_inst_sec) {
		this.r39_num_by_inst_sec = r39_num_by_inst_sec;
	}
	public BigDecimal getR39_num_depo() {
		return r39_num_depo;
	}
	public void setR39_num_depo(BigDecimal r39_num_depo) {
		this.r39_num_depo = r39_num_depo;
	}
	public BigDecimal getR39_num_depo_acc() {
		return r39_num_depo_acc;
	}
	public void setR39_num_depo_acc(BigDecimal r39_num_depo_acc) {
		this.r39_num_depo_acc = r39_num_depo_acc;
	}
	public BigDecimal getR39_num_borrowers() {
		return r39_num_borrowers;
	}
	public void setR39_num_borrowers(BigDecimal r39_num_borrowers) {
		this.r39_num_borrowers = r39_num_borrowers;
	}
	public BigDecimal getR39_num_loan_acc() {
		return r39_num_loan_acc;
	}
	public void setR39_num_loan_acc(BigDecimal r39_num_loan_acc) {
		this.r39_num_loan_acc = r39_num_loan_acc;
	}
	public String getR40_num_by_inst_sec() {
		return r40_num_by_inst_sec;
	}
	public void setR40_num_by_inst_sec(String r40_num_by_inst_sec) {
		this.r40_num_by_inst_sec = r40_num_by_inst_sec;
	}
	public BigDecimal getR40_num_depo() {
		return r40_num_depo;
	}
	public void setR40_num_depo(BigDecimal r40_num_depo) {
		this.r40_num_depo = r40_num_depo;
	}
	public BigDecimal getR40_num_depo_acc() {
		return r40_num_depo_acc;
	}
	public void setR40_num_depo_acc(BigDecimal r40_num_depo_acc) {
		this.r40_num_depo_acc = r40_num_depo_acc;
	}
	public BigDecimal getR40_num_borrowers() {
		return r40_num_borrowers;
	}
	public void setR40_num_borrowers(BigDecimal r40_num_borrowers) {
		this.r40_num_borrowers = r40_num_borrowers;
	}
	public BigDecimal getR40_num_loan_acc() {
		return r40_num_loan_acc;
	}
	public void setR40_num_loan_acc(BigDecimal r40_num_loan_acc) {
		this.r40_num_loan_acc = r40_num_loan_acc;
	}
	public String getR41_num_by_inst_sec() {
		return r41_num_by_inst_sec;
	}
	public void setR41_num_by_inst_sec(String r41_num_by_inst_sec) {
		this.r41_num_by_inst_sec = r41_num_by_inst_sec;
	}
	public BigDecimal getR41_num_depo() {
		return r41_num_depo;
	}
	public void setR41_num_depo(BigDecimal r41_num_depo) {
		this.r41_num_depo = r41_num_depo;
	}
	public BigDecimal getR41_num_depo_acc() {
		return r41_num_depo_acc;
	}
	public void setR41_num_depo_acc(BigDecimal r41_num_depo_acc) {
		this.r41_num_depo_acc = r41_num_depo_acc;
	}
	public BigDecimal getR41_num_borrowers() {
		return r41_num_borrowers;
	}
	public void setR41_num_borrowers(BigDecimal r41_num_borrowers) {
		this.r41_num_borrowers = r41_num_borrowers;
	}
	public BigDecimal getR41_num_loan_acc() {
		return r41_num_loan_acc;
	}
	public void setR41_num_loan_acc(BigDecimal r41_num_loan_acc) {
		this.r41_num_loan_acc = r41_num_loan_acc;
	}
	public String getR42_num_by_inst_sec() {
		return r42_num_by_inst_sec;
	}
	public void setR42_num_by_inst_sec(String r42_num_by_inst_sec) {
		this.r42_num_by_inst_sec = r42_num_by_inst_sec;
	}
	public BigDecimal getR42_num_depo() {
		return r42_num_depo;
	}
	public void setR42_num_depo(BigDecimal r42_num_depo) {
		this.r42_num_depo = r42_num_depo;
	}
	public BigDecimal getR42_num_depo_acc() {
		return r42_num_depo_acc;
	}
	public void setR42_num_depo_acc(BigDecimal r42_num_depo_acc) {
		this.r42_num_depo_acc = r42_num_depo_acc;
	}
	public BigDecimal getR42_num_borrowers() {
		return r42_num_borrowers;
	}
	public void setR42_num_borrowers(BigDecimal r42_num_borrowers) {
		this.r42_num_borrowers = r42_num_borrowers;
	}
	public BigDecimal getR42_num_loan_acc() {
		return r42_num_loan_acc;
	}
	public void setR42_num_loan_acc(BigDecimal r42_num_loan_acc) {
		this.r42_num_loan_acc = r42_num_loan_acc;
	}
	public String getR43_num_by_inst_sec() {
		return r43_num_by_inst_sec;
	}
	public void setR43_num_by_inst_sec(String r43_num_by_inst_sec) {
		this.r43_num_by_inst_sec = r43_num_by_inst_sec;
	}
	public BigDecimal getR43_num_depo() {
		return r43_num_depo;
	}
	public void setR43_num_depo(BigDecimal r43_num_depo) {
		this.r43_num_depo = r43_num_depo;
	}
	public BigDecimal getR43_num_depo_acc() {
		return r43_num_depo_acc;
	}
	public void setR43_num_depo_acc(BigDecimal r43_num_depo_acc) {
		this.r43_num_depo_acc = r43_num_depo_acc;
	}
	public BigDecimal getR43_num_borrowers() {
		return r43_num_borrowers;
	}
	public void setR43_num_borrowers(BigDecimal r43_num_borrowers) {
		this.r43_num_borrowers = r43_num_borrowers;
	}
	public BigDecimal getR43_num_loan_acc() {
		return r43_num_loan_acc;
	}
	public void setR43_num_loan_acc(BigDecimal r43_num_loan_acc) {
		this.r43_num_loan_acc = r43_num_loan_acc;
	}
	public String getR44_num_by_inst_sec() {
		return r44_num_by_inst_sec;
	}
	public void setR44_num_by_inst_sec(String r44_num_by_inst_sec) {
		this.r44_num_by_inst_sec = r44_num_by_inst_sec;
	}
	public BigDecimal getR44_num_depo() {
		return r44_num_depo;
	}
	public void setR44_num_depo(BigDecimal r44_num_depo) {
		this.r44_num_depo = r44_num_depo;
	}
	public BigDecimal getR44_num_depo_acc() {
		return r44_num_depo_acc;
	}
	public void setR44_num_depo_acc(BigDecimal r44_num_depo_acc) {
		this.r44_num_depo_acc = r44_num_depo_acc;
	}
	public BigDecimal getR44_num_borrowers() {
		return r44_num_borrowers;
	}
	public void setR44_num_borrowers(BigDecimal r44_num_borrowers) {
		this.r44_num_borrowers = r44_num_borrowers;
	}
	public BigDecimal getR44_num_loan_acc() {
		return r44_num_loan_acc;
	}
	public void setR44_num_loan_acc(BigDecimal r44_num_loan_acc) {
		this.r44_num_loan_acc = r44_num_loan_acc;
	}
	public String getR45_num_by_inst_sec() {
		return r45_num_by_inst_sec;
	}
	public void setR45_num_by_inst_sec(String r45_num_by_inst_sec) {
		this.r45_num_by_inst_sec = r45_num_by_inst_sec;
	}
	public BigDecimal getR45_num_depo() {
		return r45_num_depo;
	}
	public void setR45_num_depo(BigDecimal r45_num_depo) {
		this.r45_num_depo = r45_num_depo;
	}
	public BigDecimal getR45_num_depo_acc() {
		return r45_num_depo_acc;
	}
	public void setR45_num_depo_acc(BigDecimal r45_num_depo_acc) {
		this.r45_num_depo_acc = r45_num_depo_acc;
	}
	public BigDecimal getR45_num_borrowers() {
		return r45_num_borrowers;
	}
	public void setR45_num_borrowers(BigDecimal r45_num_borrowers) {
		this.r45_num_borrowers = r45_num_borrowers;
	}
	public BigDecimal getR45_num_loan_acc() {
		return r45_num_loan_acc;
	}
	public void setR45_num_loan_acc(BigDecimal r45_num_loan_acc) {
		this.r45_num_loan_acc = r45_num_loan_acc;
	}
	public String getR46_num_by_inst_sec() {
		return r46_num_by_inst_sec;
	}
	public void setR46_num_by_inst_sec(String r46_num_by_inst_sec) {
		this.r46_num_by_inst_sec = r46_num_by_inst_sec;
	}
	public BigDecimal getR46_num_depo() {
		return r46_num_depo;
	}
	public void setR46_num_depo(BigDecimal r46_num_depo) {
		this.r46_num_depo = r46_num_depo;
	}
	public BigDecimal getR46_num_depo_acc() {
		return r46_num_depo_acc;
	}
	public void setR46_num_depo_acc(BigDecimal r46_num_depo_acc) {
		this.r46_num_depo_acc = r46_num_depo_acc;
	}
	public BigDecimal getR46_num_borrowers() {
		return r46_num_borrowers;
	}
	public void setR46_num_borrowers(BigDecimal r46_num_borrowers) {
		this.r46_num_borrowers = r46_num_borrowers;
	}
	public BigDecimal getR46_num_loan_acc() {
		return r46_num_loan_acc;
	}
	public void setR46_num_loan_acc(BigDecimal r46_num_loan_acc) {
		this.r46_num_loan_acc = r46_num_loan_acc;
	}
	public String getR47_num_by_inst_sec() {
		return r47_num_by_inst_sec;
	}
	public void setR47_num_by_inst_sec(String r47_num_by_inst_sec) {
		this.r47_num_by_inst_sec = r47_num_by_inst_sec;
	}
	public BigDecimal getR47_num_depo() {
		return r47_num_depo;
	}
	public void setR47_num_depo(BigDecimal r47_num_depo) {
		this.r47_num_depo = r47_num_depo;
	}
	public BigDecimal getR47_num_depo_acc() {
		return r47_num_depo_acc;
	}
	public void setR47_num_depo_acc(BigDecimal r47_num_depo_acc) {
		this.r47_num_depo_acc = r47_num_depo_acc;
	}
	public BigDecimal getR47_num_borrowers() {
		return r47_num_borrowers;
	}
	public void setR47_num_borrowers(BigDecimal r47_num_borrowers) {
		this.r47_num_borrowers = r47_num_borrowers;
	}
	public BigDecimal getR47_num_loan_acc() {
		return r47_num_loan_acc;
	}
	public void setR47_num_loan_acc(BigDecimal r47_num_loan_acc) {
		this.r47_num_loan_acc = r47_num_loan_acc;
	}
	public String getR48_num_by_inst_sec() {
		return r48_num_by_inst_sec;
	}
	public void setR48_num_by_inst_sec(String r48_num_by_inst_sec) {
		this.r48_num_by_inst_sec = r48_num_by_inst_sec;
	}
	public BigDecimal getR48_num_depo() {
		return r48_num_depo;
	}
	public void setR48_num_depo(BigDecimal r48_num_depo) {
		this.r48_num_depo = r48_num_depo;
	}
	public BigDecimal getR48_num_depo_acc() {
		return r48_num_depo_acc;
	}
	public void setR48_num_depo_acc(BigDecimal r48_num_depo_acc) {
		this.r48_num_depo_acc = r48_num_depo_acc;
	}
	public BigDecimal getR48_num_borrowers() {
		return r48_num_borrowers;
	}
	public void setR48_num_borrowers(BigDecimal r48_num_borrowers) {
		this.r48_num_borrowers = r48_num_borrowers;
	}
	public BigDecimal getR48_num_loan_acc() {
		return r48_num_loan_acc;
	}
	public void setR48_num_loan_acc(BigDecimal r48_num_loan_acc) {
		this.r48_num_loan_acc = r48_num_loan_acc;
	}
	public String getR49_num_by_inst_sec() {
		return r49_num_by_inst_sec;
	}
	public void setR49_num_by_inst_sec(String r49_num_by_inst_sec) {
		this.r49_num_by_inst_sec = r49_num_by_inst_sec;
	}
	public BigDecimal getR49_num_depo() {
		return r49_num_depo;
	}
	public void setR49_num_depo(BigDecimal r49_num_depo) {
		this.r49_num_depo = r49_num_depo;
	}
	public BigDecimal getR49_num_depo_acc() {
		return r49_num_depo_acc;
	}
	public void setR49_num_depo_acc(BigDecimal r49_num_depo_acc) {
		this.r49_num_depo_acc = r49_num_depo_acc;
	}
	public BigDecimal getR49_num_borrowers() {
		return r49_num_borrowers;
	}
	public void setR49_num_borrowers(BigDecimal r49_num_borrowers) {
		this.r49_num_borrowers = r49_num_borrowers;
	}
	public BigDecimal getR49_num_loan_acc() {
		return r49_num_loan_acc;
	}
	public void setR49_num_loan_acc(BigDecimal r49_num_loan_acc) {
		this.r49_num_loan_acc = r49_num_loan_acc;
	}
	public String getR50_num_by_inst_sec() {
		return r50_num_by_inst_sec;
	}
	public void setR50_num_by_inst_sec(String r50_num_by_inst_sec) {
		this.r50_num_by_inst_sec = r50_num_by_inst_sec;
	}
	public BigDecimal getR50_num_depo() {
		return r50_num_depo;
	}
	public void setR50_num_depo(BigDecimal r50_num_depo) {
		this.r50_num_depo = r50_num_depo;
	}
	public BigDecimal getR50_num_depo_acc() {
		return r50_num_depo_acc;
	}
	public void setR50_num_depo_acc(BigDecimal r50_num_depo_acc) {
		this.r50_num_depo_acc = r50_num_depo_acc;
	}
	public BigDecimal getR50_num_borrowers() {
		return r50_num_borrowers;
	}
	public void setR50_num_borrowers(BigDecimal r50_num_borrowers) {
		this.r50_num_borrowers = r50_num_borrowers;
	}
	public BigDecimal getR50_num_loan_acc() {
		return r50_num_loan_acc;
	}
	public void setR50_num_loan_acc(BigDecimal r50_num_loan_acc) {
		this.r50_num_loan_acc = r50_num_loan_acc;
	}
	public String getR51_num_by_inst_sec() {
		return r51_num_by_inst_sec;
	}
	public void setR51_num_by_inst_sec(String r51_num_by_inst_sec) {
		this.r51_num_by_inst_sec = r51_num_by_inst_sec;
	}
	public BigDecimal getR51_num_depo() {
		return r51_num_depo;
	}
	public void setR51_num_depo(BigDecimal r51_num_depo) {
		this.r51_num_depo = r51_num_depo;
	}
	public BigDecimal getR51_num_depo_acc() {
		return r51_num_depo_acc;
	}
	public void setR51_num_depo_acc(BigDecimal r51_num_depo_acc) {
		this.r51_num_depo_acc = r51_num_depo_acc;
	}
	public BigDecimal getR51_num_borrowers() {
		return r51_num_borrowers;
	}
	public void setR51_num_borrowers(BigDecimal r51_num_borrowers) {
		this.r51_num_borrowers = r51_num_borrowers;
	}
	public BigDecimal getR51_num_loan_acc() {
		return r51_num_loan_acc;
	}
	public void setR51_num_loan_acc(BigDecimal r51_num_loan_acc) {
		this.r51_num_loan_acc = r51_num_loan_acc;
	}
	public String getR52_num_by_inst_sec() {
		return r52_num_by_inst_sec;
	}
	public void setR52_num_by_inst_sec(String r52_num_by_inst_sec) {
		this.r52_num_by_inst_sec = r52_num_by_inst_sec;
	}
	public BigDecimal getR52_num_depo() {
		return r52_num_depo;
	}
	public void setR52_num_depo(BigDecimal r52_num_depo) {
		this.r52_num_depo = r52_num_depo;
	}
	public BigDecimal getR52_num_depo_acc() {
		return r52_num_depo_acc;
	}
	public void setR52_num_depo_acc(BigDecimal r52_num_depo_acc) {
		this.r52_num_depo_acc = r52_num_depo_acc;
	}
	public BigDecimal getR52_num_borrowers() {
		return r52_num_borrowers;
	}
	public void setR52_num_borrowers(BigDecimal r52_num_borrowers) {
		this.r52_num_borrowers = r52_num_borrowers;
	}
	public BigDecimal getR52_num_loan_acc() {
		return r52_num_loan_acc;
	}
	public void setR52_num_loan_acc(BigDecimal r52_num_loan_acc) {
		this.r52_num_loan_acc = r52_num_loan_acc;
	}
	public String getR53_num_by_inst_sec() {
		return r53_num_by_inst_sec;
	}
	public void setR53_num_by_inst_sec(String r53_num_by_inst_sec) {
		this.r53_num_by_inst_sec = r53_num_by_inst_sec;
	}
	public BigDecimal getR53_num_depo() {
		return r53_num_depo;
	}
	public void setR53_num_depo(BigDecimal r53_num_depo) {
		this.r53_num_depo = r53_num_depo;
	}
	public BigDecimal getR53_num_depo_acc() {
		return r53_num_depo_acc;
	}
	public void setR53_num_depo_acc(BigDecimal r53_num_depo_acc) {
		this.r53_num_depo_acc = r53_num_depo_acc;
	}
	public BigDecimal getR53_num_borrowers() {
		return r53_num_borrowers;
	}
	public void setR53_num_borrowers(BigDecimal r53_num_borrowers) {
		this.r53_num_borrowers = r53_num_borrowers;
	}
	public BigDecimal getR53_num_loan_acc() {
		return r53_num_loan_acc;
	}
	public void setR53_num_loan_acc(BigDecimal r53_num_loan_acc) {
		this.r53_num_loan_acc = r53_num_loan_acc;
	}
	public String getR54_num_by_inst_sec() {
		return r54_num_by_inst_sec;
	}
	public void setR54_num_by_inst_sec(String r54_num_by_inst_sec) {
		this.r54_num_by_inst_sec = r54_num_by_inst_sec;
	}
	public BigDecimal getR54_num_depo() {
		return r54_num_depo;
	}
	public void setR54_num_depo(BigDecimal r54_num_depo) {
		this.r54_num_depo = r54_num_depo;
	}
	public BigDecimal getR54_num_depo_acc() {
		return r54_num_depo_acc;
	}
	public void setR54_num_depo_acc(BigDecimal r54_num_depo_acc) {
		this.r54_num_depo_acc = r54_num_depo_acc;
	}
	public BigDecimal getR54_num_borrowers() {
		return r54_num_borrowers;
	}
	public void setR54_num_borrowers(BigDecimal r54_num_borrowers) {
		this.r54_num_borrowers = r54_num_borrowers;
	}
	public BigDecimal getR54_num_loan_acc() {
		return r54_num_loan_acc;
	}
	public void setR54_num_loan_acc(BigDecimal r54_num_loan_acc) {
		this.r54_num_loan_acc = r54_num_loan_acc;
	}
	public String getR55_num_by_inst_sec() {
		return r55_num_by_inst_sec;
	}
	public void setR55_num_by_inst_sec(String r55_num_by_inst_sec) {
		this.r55_num_by_inst_sec = r55_num_by_inst_sec;
	}
	public BigDecimal getR55_num_depo() {
		return r55_num_depo;
	}
	public void setR55_num_depo(BigDecimal r55_num_depo) {
		this.r55_num_depo = r55_num_depo;
	}
	public BigDecimal getR55_num_depo_acc() {
		return r55_num_depo_acc;
	}
	public void setR55_num_depo_acc(BigDecimal r55_num_depo_acc) {
		this.r55_num_depo_acc = r55_num_depo_acc;
	}
	public BigDecimal getR55_num_borrowers() {
		return r55_num_borrowers;
	}
	public void setR55_num_borrowers(BigDecimal r55_num_borrowers) {
		this.r55_num_borrowers = r55_num_borrowers;
	}
	public BigDecimal getR55_num_loan_acc() {
		return r55_num_loan_acc;
	}
	public void setR55_num_loan_acc(BigDecimal r55_num_loan_acc) {
		this.r55_num_loan_acc = r55_num_loan_acc;
	}
	public String getR56_num_by_inst_sec() {
		return r56_num_by_inst_sec;
	}
	public void setR56_num_by_inst_sec(String r56_num_by_inst_sec) {
		this.r56_num_by_inst_sec = r56_num_by_inst_sec;
	}
	public BigDecimal getR56_num_depo() {
		return r56_num_depo;
	}
	public void setR56_num_depo(BigDecimal r56_num_depo) {
		this.r56_num_depo = r56_num_depo;
	}
	public BigDecimal getR56_num_depo_acc() {
		return r56_num_depo_acc;
	}
	public void setR56_num_depo_acc(BigDecimal r56_num_depo_acc) {
		this.r56_num_depo_acc = r56_num_depo_acc;
	}
	public BigDecimal getR56_num_borrowers() {
		return r56_num_borrowers;
	}
	public void setR56_num_borrowers(BigDecimal r56_num_borrowers) {
		this.r56_num_borrowers = r56_num_borrowers;
	}
	public BigDecimal getR56_num_loan_acc() {
		return r56_num_loan_acc;
	}
	public void setR56_num_loan_acc(BigDecimal r56_num_loan_acc) {
		this.r56_num_loan_acc = r56_num_loan_acc;
	}
	public String getR57_num_by_inst_sec() {
		return r57_num_by_inst_sec;
	}
	public void setR57_num_by_inst_sec(String r57_num_by_inst_sec) {
		this.r57_num_by_inst_sec = r57_num_by_inst_sec;
	}
	public BigDecimal getR57_num_depo() {
		return r57_num_depo;
	}
	public void setR57_num_depo(BigDecimal r57_num_depo) {
		this.r57_num_depo = r57_num_depo;
	}
	public BigDecimal getR57_num_depo_acc() {
		return r57_num_depo_acc;
	}
	public void setR57_num_depo_acc(BigDecimal r57_num_depo_acc) {
		this.r57_num_depo_acc = r57_num_depo_acc;
	}
	public BigDecimal getR57_num_borrowers() {
		return r57_num_borrowers;
	}
	public void setR57_num_borrowers(BigDecimal r57_num_borrowers) {
		this.r57_num_borrowers = r57_num_borrowers;
	}
	public BigDecimal getR57_num_loan_acc() {
		return r57_num_loan_acc;
	}
	public void setR57_num_loan_acc(BigDecimal r57_num_loan_acc) {
		this.r57_num_loan_acc = r57_num_loan_acc;
	}
	public String getR58_num_by_inst_sec() {
		return r58_num_by_inst_sec;
	}
	public void setR58_num_by_inst_sec(String r58_num_by_inst_sec) {
		this.r58_num_by_inst_sec = r58_num_by_inst_sec;
	}
	public BigDecimal getR58_num_depo() {
		return r58_num_depo;
	}
	public void setR58_num_depo(BigDecimal r58_num_depo) {
		this.r58_num_depo = r58_num_depo;
	}
	public BigDecimal getR58_num_depo_acc() {
		return r58_num_depo_acc;
	}
	public void setR58_num_depo_acc(BigDecimal r58_num_depo_acc) {
		this.r58_num_depo_acc = r58_num_depo_acc;
	}
	public BigDecimal getR58_num_borrowers() {
		return r58_num_borrowers;
	}
	public void setR58_num_borrowers(BigDecimal r58_num_borrowers) {
		this.r58_num_borrowers = r58_num_borrowers;
	}
	public BigDecimal getR58_num_loan_acc() {
		return r58_num_loan_acc;
	}
	public void setR58_num_loan_acc(BigDecimal r58_num_loan_acc) {
		this.r58_num_loan_acc = r58_num_loan_acc;
	}
	public String getR59_num_by_inst_sec() {
		return r59_num_by_inst_sec;
	}
	public void setR59_num_by_inst_sec(String r59_num_by_inst_sec) {
		this.r59_num_by_inst_sec = r59_num_by_inst_sec;
	}
	public BigDecimal getR59_num_depo() {
		return r59_num_depo;
	}
	public void setR59_num_depo(BigDecimal r59_num_depo) {
		this.r59_num_depo = r59_num_depo;
	}
	public BigDecimal getR59_num_depo_acc() {
		return r59_num_depo_acc;
	}
	public void setR59_num_depo_acc(BigDecimal r59_num_depo_acc) {
		this.r59_num_depo_acc = r59_num_depo_acc;
	}
	public BigDecimal getR59_num_borrowers() {
		return r59_num_borrowers;
	}
	public void setR59_num_borrowers(BigDecimal r59_num_borrowers) {
		this.r59_num_borrowers = r59_num_borrowers;
	}
	public BigDecimal getR59_num_loan_acc() {
		return r59_num_loan_acc;
	}
	public void setR59_num_loan_acc(BigDecimal r59_num_loan_acc) {
		this.r59_num_loan_acc = r59_num_loan_acc;
	}
	public String getR60_num_by_inst_sec() {
		return r60_num_by_inst_sec;
	}
	public void setR60_num_by_inst_sec(String r60_num_by_inst_sec) {
		this.r60_num_by_inst_sec = r60_num_by_inst_sec;
	}
	public BigDecimal getR60_num_depo() {
		return r60_num_depo;
	}
	public void setR60_num_depo(BigDecimal r60_num_depo) {
		this.r60_num_depo = r60_num_depo;
	}
	public BigDecimal getR60_num_depo_acc() {
		return r60_num_depo_acc;
	}
	public void setR60_num_depo_acc(BigDecimal r60_num_depo_acc) {
		this.r60_num_depo_acc = r60_num_depo_acc;
	}
	public BigDecimal getR60_num_borrowers() {
		return r60_num_borrowers;
	}
	public void setR60_num_borrowers(BigDecimal r60_num_borrowers) {
		this.r60_num_borrowers = r60_num_borrowers;
	}
	public BigDecimal getR60_num_loan_acc() {
		return r60_num_loan_acc;
	}
	public void setR60_num_loan_acc(BigDecimal r60_num_loan_acc) {
		this.r60_num_loan_acc = r60_num_loan_acc;
	}
	public String getR61_num_by_inst_sec() {
		return r61_num_by_inst_sec;
	}
	public void setR61_num_by_inst_sec(String r61_num_by_inst_sec) {
		this.r61_num_by_inst_sec = r61_num_by_inst_sec;
	}
	public BigDecimal getR61_num_depo() {
		return r61_num_depo;
	}
	public void setR61_num_depo(BigDecimal r61_num_depo) {
		this.r61_num_depo = r61_num_depo;
	}
	public BigDecimal getR61_num_depo_acc() {
		return r61_num_depo_acc;
	}
	public void setR61_num_depo_acc(BigDecimal r61_num_depo_acc) {
		this.r61_num_depo_acc = r61_num_depo_acc;
	}
	public BigDecimal getR61_num_borrowers() {
		return r61_num_borrowers;
	}
	public void setR61_num_borrowers(BigDecimal r61_num_borrowers) {
		this.r61_num_borrowers = r61_num_borrowers;
	}
	public BigDecimal getR61_num_loan_acc() {
		return r61_num_loan_acc;
	}
	public void setR61_num_loan_acc(BigDecimal r61_num_loan_acc) {
		this.r61_num_loan_acc = r61_num_loan_acc;
	}
	public String getR62_num_by_inst_sec() {
		return r62_num_by_inst_sec;
	}
	public void setR62_num_by_inst_sec(String r62_num_by_inst_sec) {
		this.r62_num_by_inst_sec = r62_num_by_inst_sec;
	}
	public BigDecimal getR62_num_depo() {
		return r62_num_depo;
	}
	public void setR62_num_depo(BigDecimal r62_num_depo) {
		this.r62_num_depo = r62_num_depo;
	}
	public BigDecimal getR62_num_depo_acc() {
		return r62_num_depo_acc;
	}
	public void setR62_num_depo_acc(BigDecimal r62_num_depo_acc) {
		this.r62_num_depo_acc = r62_num_depo_acc;
	}
	public BigDecimal getR62_num_borrowers() {
		return r62_num_borrowers;
	}
	public void setR62_num_borrowers(BigDecimal r62_num_borrowers) {
		this.r62_num_borrowers = r62_num_borrowers;
	}
	public BigDecimal getR62_num_loan_acc() {
		return r62_num_loan_acc;
	}
	public void setR62_num_loan_acc(BigDecimal r62_num_loan_acc) {
		this.r62_num_loan_acc = r62_num_loan_acc;
	}
	public String getR63_num_by_inst_sec() {
		return r63_num_by_inst_sec;
	}
	public void setR63_num_by_inst_sec(String r63_num_by_inst_sec) {
		this.r63_num_by_inst_sec = r63_num_by_inst_sec;
	}
	public BigDecimal getR63_num_depo() {
		return r63_num_depo;
	}
	public void setR63_num_depo(BigDecimal r63_num_depo) {
		this.r63_num_depo = r63_num_depo;
	}
	public BigDecimal getR63_num_depo_acc() {
		return r63_num_depo_acc;
	}
	public void setR63_num_depo_acc(BigDecimal r63_num_depo_acc) {
		this.r63_num_depo_acc = r63_num_depo_acc;
	}
	public BigDecimal getR63_num_borrowers() {
		return r63_num_borrowers;
	}
	public void setR63_num_borrowers(BigDecimal r63_num_borrowers) {
		this.r63_num_borrowers = r63_num_borrowers;
	}
	public BigDecimal getR63_num_loan_acc() {
		return r63_num_loan_acc;
	}
	public void setR63_num_loan_acc(BigDecimal r63_num_loan_acc) {
		this.r63_num_loan_acc = r63_num_loan_acc;
	}
	public String getR64_num_by_inst_sec() {
		return r64_num_by_inst_sec;
	}
	public void setR64_num_by_inst_sec(String r64_num_by_inst_sec) {
		this.r64_num_by_inst_sec = r64_num_by_inst_sec;
	}
	public BigDecimal getR64_num_depo() {
		return r64_num_depo;
	}
	public void setR64_num_depo(BigDecimal r64_num_depo) {
		this.r64_num_depo = r64_num_depo;
	}
	public BigDecimal getR64_num_depo_acc() {
		return r64_num_depo_acc;
	}
	public void setR64_num_depo_acc(BigDecimal r64_num_depo_acc) {
		this.r64_num_depo_acc = r64_num_depo_acc;
	}
	public BigDecimal getR64_num_borrowers() {
		return r64_num_borrowers;
	}
	public void setR64_num_borrowers(BigDecimal r64_num_borrowers) {
		this.r64_num_borrowers = r64_num_borrowers;
	}
	public BigDecimal getR64_num_loan_acc() {
		return r64_num_loan_acc;
	}
	public void setR64_num_loan_acc(BigDecimal r64_num_loan_acc) {
		this.r64_num_loan_acc = r64_num_loan_acc;
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
	public Q_ATF_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

   
	
	 
	

	

}
