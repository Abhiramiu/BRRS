package com.bornfire.brrs.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="BRRS_Q_RLFA1_RESUB_SUMMARY")
@IdClass(Q_RLFA1_Resub_Summary_PK.class)
public class Q_RLFA1_Resub_Summary_Entity {
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	private Date	report_date;
	@Id
	private BigDecimal	report_version;
	
	@Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;

    private String	r10_rene_loans;
    private BigDecimal	r10_collateral_amount;
    private BigDecimal	r10_carrying_amount;
    private BigDecimal	r10_no_of_accts;
    private String	r11_rene_loans;
    private BigDecimal	r11_collateral_amount;
    private BigDecimal	r11_carrying_amount;
    private BigDecimal	r11_no_of_accts;
    private String	r12_rene_loans;
    private BigDecimal	r12_collateral_amount;
    private BigDecimal	r12_carrying_amount;
    private BigDecimal	r12_no_of_accts;
    private String	r13_rene_loans;
    private BigDecimal	r13_collateral_amount;
    private BigDecimal	r13_carrying_amount;
    private BigDecimal	r13_no_of_accts;
    private String	r14_rene_loans;
    private BigDecimal	r14_collateral_amount;
    private BigDecimal	r14_carrying_amount;
    private BigDecimal	r14_no_of_accts;
    private String	r15_rene_loans;
    private BigDecimal	r15_collateral_amount;
    private BigDecimal	r15_carrying_amount;
    private BigDecimal	r15_no_of_accts;
    private String	r16_rene_loans;
    private BigDecimal	r16_collateral_amount;
    private BigDecimal	r16_carrying_amount;
    private BigDecimal	r16_no_of_accts;
    private String	r17_rene_loans;
    private BigDecimal	r17_collateral_amount;
    private BigDecimal	r17_carrying_amount;
    private BigDecimal	r17_no_of_accts;
    private String	r18_rene_loans;
    private BigDecimal	r18_collateral_amount;
    private BigDecimal	r18_carrying_amount;
    private BigDecimal	r18_no_of_accts;
    private String	r19_rene_loans;
    private BigDecimal	r19_collateral_amount;
    private BigDecimal	r19_carrying_amount;
    private BigDecimal	r19_no_of_accts;
    private String	r20_rene_loans;
    private BigDecimal	r20_collateral_amount;
    private BigDecimal	r20_carrying_amount;
    private BigDecimal	r20_no_of_accts;
    private String	r21_rene_loans;
    private BigDecimal	r21_collateral_amount;
    private BigDecimal	r21_carrying_amount;
    private BigDecimal	r21_no_of_accts;
    private String	r22_rene_loans;
    private BigDecimal	r22_collateral_amount;
    private BigDecimal	r22_carrying_amount;
    private BigDecimal	r22_no_of_accts;
    private String	r23_rene_loans;
    private BigDecimal	r23_collateral_amount;
    private BigDecimal	r23_carrying_amount;
    private BigDecimal	r23_no_of_accts;
    private String	r24_rene_loans;
    private BigDecimal	r24_collateral_amount;
    private BigDecimal	r24_carrying_amount;
    private BigDecimal	r24_no_of_accts;
    private String	r25_rene_loans;
    private BigDecimal	r25_collateral_amount;
    private BigDecimal	r25_carrying_amount;
    private BigDecimal	r25_no_of_accts;
    private String	r26_rene_loans;
    private BigDecimal	r26_collateral_amount;
    private BigDecimal	r26_carrying_amount;
    private BigDecimal	r26_no_of_accts;
    private String	r27_rene_loans;
    private BigDecimal	r27_collateral_amount;
    private BigDecimal	r27_carrying_amount;
    private BigDecimal	r27_no_of_accts;
    private String	r28_rene_loans;
    private BigDecimal	r28_collateral_amount;
    private BigDecimal	r28_carrying_amount;
    private BigDecimal	r28_no_of_accts;
    private String	r29_rene_loans;
    private BigDecimal	r29_collateral_amount;
    private BigDecimal	r29_carrying_amount;
    private BigDecimal	r29_no_of_accts;
    private String	r30_rene_loans;
    private BigDecimal	r30_collateral_amount;
    private BigDecimal	r30_carrying_amount;
    private BigDecimal	r30_no_of_accts;
    private String	r31_rene_loans;
    private BigDecimal	r31_collateral_amount;
    private BigDecimal	r31_carrying_amount;
    private BigDecimal	r31_no_of_accts;
    private String	r32_rene_loans;
    private BigDecimal	r32_collateral_amount;
    private BigDecimal	r32_carrying_amount;
    private BigDecimal	r32_no_of_accts;
    private String	r33_rene_loans;
    private BigDecimal	r33_collateral_amount;
    private BigDecimal	r33_carrying_amount;
    private BigDecimal	r33_no_of_accts;
    private String	r34_rene_loans;
    private BigDecimal	r34_collateral_amount;
    private BigDecimal	r34_carrying_amount;
    private BigDecimal	r34_no_of_accts;
    private String	r35_rene_loans;
    private BigDecimal	r35_collateral_amount;
    private BigDecimal	r35_carrying_amount;
    private BigDecimal	r35_no_of_accts;
    private String	r36_rene_loans;
    private BigDecimal	r36_collateral_amount;
    private BigDecimal	r36_carrying_amount;
    private BigDecimal	r36_no_of_accts;
    private String	r37_rene_loans;
    private BigDecimal	r37_collateral_amount;
    private BigDecimal	r37_carrying_amount;
    private BigDecimal	r37_no_of_accts;
    private String	r38_rene_loans;
    private BigDecimal	r38_collateral_amount;
    private BigDecimal	r38_carrying_amount;
    private BigDecimal	r38_no_of_accts;
    private String	r39_rene_loans;
    private BigDecimal	r39_collateral_amount;
    private BigDecimal	r39_carrying_amount;
    private BigDecimal	r39_no_of_accts;
    private String	r40_rene_loans;
    private BigDecimal	r40_collateral_amount;
    private BigDecimal	r40_carrying_amount;
    private BigDecimal	r40_no_of_accts;
    private String	r41_rene_loans;
    private BigDecimal	r41_collateral_amount;
    private BigDecimal	r41_carrying_amount;
    private BigDecimal	r41_no_of_accts;
    private String	r42_rene_loans;
    private BigDecimal	r42_collateral_amount;
    private BigDecimal	r42_carrying_amount;
    private BigDecimal	r42_no_of_accts;
    private String	r43_rene_loans;
    private BigDecimal	r43_collateral_amount;
    private BigDecimal	r43_carrying_amount;
    private BigDecimal	r43_no_of_accts;
    private String	r44_rene_loans;
    private BigDecimal	r44_collateral_amount;
    private BigDecimal	r44_carrying_amount;
    private BigDecimal	r44_no_of_accts;
    private String	r45_rene_loans;
    private BigDecimal	r45_collateral_amount;
    private BigDecimal	r45_carrying_amount;
    private BigDecimal	r45_no_of_accts;
    private String	r46_rene_loans;
    private BigDecimal	r46_collateral_amount;
    private BigDecimal	r46_carrying_amount;
    private BigDecimal	r46_no_of_accts;
    private String	r47_rene_loans;
    private BigDecimal	r47_collateral_amount;
    private BigDecimal	r47_carrying_amount;
    private BigDecimal	r47_no_of_accts;
    private String	r48_rene_loans;
    private BigDecimal	r48_collateral_amount;
    private BigDecimal	r48_carrying_amount;
    private BigDecimal	r48_no_of_accts;
    private String	r49_rene_loans;
    private BigDecimal	r49_collateral_amount;
    private BigDecimal	r49_carrying_amount;
    private BigDecimal	r49_no_of_accts;
    private String	r50_rene_loans;
    private BigDecimal	r50_collateral_amount;
    private BigDecimal	r50_carrying_amount;
    private BigDecimal	r50_no_of_accts;
    private String	r51_rene_loans;
    private BigDecimal	r51_collateral_amount;
    private BigDecimal	r51_carrying_amount;
    private BigDecimal	r51_no_of_accts;
    private String	r52_rene_loans;
    private BigDecimal	r52_collateral_amount;
    private BigDecimal	r52_carrying_amount;
    private BigDecimal	r52_no_of_accts;
    private String	r53_rene_loans;
    private BigDecimal	r53_collateral_amount;
    private BigDecimal	r53_carrying_amount;
    private BigDecimal	r53_no_of_accts;
    private String	r54_rene_loans;
    private BigDecimal	r54_collateral_amount;
    private BigDecimal	r54_carrying_amount;
    private BigDecimal	r54_no_of_accts;
    private String	r55_rene_loans;
    private BigDecimal	r55_collateral_amount;
    private BigDecimal	r55_carrying_amount;
    private BigDecimal	r55_no_of_accts;
    private String	r56_rene_loans;
    private BigDecimal	r56_collateral_amount;
    private BigDecimal	r56_carrying_amount;
    private BigDecimal	r56_no_of_accts;
    private String	r57_rene_loans;
    private BigDecimal	r57_collateral_amount;
    private BigDecimal	r57_carrying_amount;
    private BigDecimal	r57_no_of_accts;
    private String	r58_rene_loans;
    private BigDecimal	r58_collateral_amount;
    private BigDecimal	r58_carrying_amount;
    private BigDecimal	r58_no_of_accts;
    private String	r59_rene_loans;
    private BigDecimal	r59_collateral_amount;
    private BigDecimal	r59_carrying_amount;
    private BigDecimal	r59_no_of_accts;
    private String	r60_rene_loans;
    private BigDecimal	r60_collateral_amount;
    private BigDecimal	r60_carrying_amount;
    private BigDecimal	r60_no_of_accts;
    private String	r61_rene_loans;
    private BigDecimal	r61_collateral_amount;
    private BigDecimal	r61_carrying_amount;
    private BigDecimal	r61_no_of_accts;
    private String	r62_rene_loans;
    private BigDecimal	r62_collateral_amount;
    private BigDecimal	r62_carrying_amount;
    private BigDecimal	r62_no_of_accts;
    private String	r63_rene_loans;
    private BigDecimal	r63_collateral_amount;
    private BigDecimal	r63_carrying_amount;
    private BigDecimal	r63_no_of_accts;
    
    private String r27_new_column_rene_loans;
    private BigDecimal r27_new_column_collateral_amount;
    private BigDecimal r27_new_column_carrying_amount;
    private BigDecimal r27_new_column_no_of_accts;
    
    private String r42_new_column_rene_loans;
    private BigDecimal r42_new_column_collateral_amount;
    private BigDecimal r42_new_column_carrying_amount;
    private BigDecimal r42_new_column_no_of_accts;
    
    private String r48_new_column_rene_loans;
    private BigDecimal r48_new_column_collateral_amount;
    private BigDecimal r48_new_column_carrying_amount;
    private BigDecimal r48_new_column_no_of_accts;
    
    
    
	public String getR27_new_column_rene_loans() {
		return r27_new_column_rene_loans;
	}
	public BigDecimal getR27_new_column_collateral_amount() {
		return r27_new_column_collateral_amount;
	}
	public BigDecimal getR27_new_column_carrying_amount() {
		return r27_new_column_carrying_amount;
	}
	public BigDecimal getR27_new_column_no_of_accts() {
		return r27_new_column_no_of_accts;
	}
	public String getR42_new_column_rene_loans() {
		return r42_new_column_rene_loans;
	}
	public BigDecimal getR42_new_column_collateral_amount() {
		return r42_new_column_collateral_amount;
	}
	public BigDecimal getR42_new_column_carrying_amount() {
		return r42_new_column_carrying_amount;
	}
	public BigDecimal getR42_new_column_no_of_accts() {
		return r42_new_column_no_of_accts;
	}
	public String getR48_new_column_rene_loans() {
		return r48_new_column_rene_loans;
	}
	public BigDecimal getR48_new_column_collateral_amount() {
		return r48_new_column_collateral_amount;
	}
	public BigDecimal getR48_new_column_carrying_amount() {
		return r48_new_column_carrying_amount;
	}
	public BigDecimal getR48_new_column_no_of_accts() {
		return r48_new_column_no_of_accts;
	}
	public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
		this.r27_new_column_rene_loans = r27_new_column_rene_loans;
	}
	public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
		this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
	}
	public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
		this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
	}
	public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
		this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
	}
	public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
		this.r42_new_column_rene_loans = r42_new_column_rene_loans;
	}
	public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
		this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
	}
	public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
		this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
	}
	public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
		this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
	}
	public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
		this.r48_new_column_rene_loans = r48_new_column_rene_loans;
	}
	public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
		this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
	}
	public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
		this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
	}
	public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
		this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public String getR10_rene_loans() {
		return r10_rene_loans;
	}
	public void setR10_rene_loans(String r10_rene_loans) {
		this.r10_rene_loans = r10_rene_loans;
	}
	public BigDecimal getR10_collateral_amount() {
		return r10_collateral_amount;
	}
	public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
		this.r10_collateral_amount = r10_collateral_amount;
	}
	public BigDecimal getR10_carrying_amount() {
		return r10_carrying_amount;
	}
	public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
		this.r10_carrying_amount = r10_carrying_amount;
	}
	public BigDecimal getR10_no_of_accts() {
		return r10_no_of_accts;
	}
	public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
		this.r10_no_of_accts = r10_no_of_accts;
	}
	public String getR11_rene_loans() {
		return r11_rene_loans;
	}
	public void setR11_rene_loans(String r11_rene_loans) {
		this.r11_rene_loans = r11_rene_loans;
	}
	public BigDecimal getR11_collateral_amount() {
		return r11_collateral_amount;
	}
	public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
		this.r11_collateral_amount = r11_collateral_amount;
	}
	public BigDecimal getR11_carrying_amount() {
		return r11_carrying_amount;
	}
	public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
		this.r11_carrying_amount = r11_carrying_amount;
	}
	public BigDecimal getR11_no_of_accts() {
		return r11_no_of_accts;
	}
	public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
		this.r11_no_of_accts = r11_no_of_accts;
	}
	public String getR12_rene_loans() {
		return r12_rene_loans;
	}
	public void setR12_rene_loans(String r12_rene_loans) {
		this.r12_rene_loans = r12_rene_loans;
	}
	public BigDecimal getR12_collateral_amount() {
		return r12_collateral_amount;
	}
	public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
		this.r12_collateral_amount = r12_collateral_amount;
	}
	public BigDecimal getR12_carrying_amount() {
		return r12_carrying_amount;
	}
	public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
		this.r12_carrying_amount = r12_carrying_amount;
	}
	public BigDecimal getR12_no_of_accts() {
		return r12_no_of_accts;
	}
	public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
		this.r12_no_of_accts = r12_no_of_accts;
	}
	public String getR13_rene_loans() {
		return r13_rene_loans;
	}
	public void setR13_rene_loans(String r13_rene_loans) {
		this.r13_rene_loans = r13_rene_loans;
	}
	public BigDecimal getR13_collateral_amount() {
		return r13_collateral_amount;
	}
	public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
		this.r13_collateral_amount = r13_collateral_amount;
	}
	public BigDecimal getR13_carrying_amount() {
		return r13_carrying_amount;
	}
	public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
		this.r13_carrying_amount = r13_carrying_amount;
	}
	public BigDecimal getR13_no_of_accts() {
		return r13_no_of_accts;
	}
	public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
		this.r13_no_of_accts = r13_no_of_accts;
	}
	public String getR14_rene_loans() {
		return r14_rene_loans;
	}
	public void setR14_rene_loans(String r14_rene_loans) {
		this.r14_rene_loans = r14_rene_loans;
	}
	public BigDecimal getR14_collateral_amount() {
		return r14_collateral_amount;
	}
	public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
		this.r14_collateral_amount = r14_collateral_amount;
	}
	public BigDecimal getR14_carrying_amount() {
		return r14_carrying_amount;
	}
	public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
		this.r14_carrying_amount = r14_carrying_amount;
	}
	public BigDecimal getR14_no_of_accts() {
		return r14_no_of_accts;
	}
	public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
		this.r14_no_of_accts = r14_no_of_accts;
	}
	public String getR15_rene_loans() {
		return r15_rene_loans;
	}
	public void setR15_rene_loans(String r15_rene_loans) {
		this.r15_rene_loans = r15_rene_loans;
	}
	public BigDecimal getR15_collateral_amount() {
		return r15_collateral_amount;
	}
	public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
		this.r15_collateral_amount = r15_collateral_amount;
	}
	public BigDecimal getR15_carrying_amount() {
		return r15_carrying_amount;
	}
	public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
		this.r15_carrying_amount = r15_carrying_amount;
	}
	public BigDecimal getR15_no_of_accts() {
		return r15_no_of_accts;
	}
	public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
		this.r15_no_of_accts = r15_no_of_accts;
	}
	public String getR16_rene_loans() {
		return r16_rene_loans;
	}
	public void setR16_rene_loans(String r16_rene_loans) {
		this.r16_rene_loans = r16_rene_loans;
	}
	public BigDecimal getR16_collateral_amount() {
		return r16_collateral_amount;
	}
	public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
		this.r16_collateral_amount = r16_collateral_amount;
	}
	public BigDecimal getR16_carrying_amount() {
		return r16_carrying_amount;
	}
	public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
		this.r16_carrying_amount = r16_carrying_amount;
	}
	public BigDecimal getR16_no_of_accts() {
		return r16_no_of_accts;
	}
	public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
		this.r16_no_of_accts = r16_no_of_accts;
	}
	public String getR17_rene_loans() {
		return r17_rene_loans;
	}
	public void setR17_rene_loans(String r17_rene_loans) {
		this.r17_rene_loans = r17_rene_loans;
	}
	public BigDecimal getR17_collateral_amount() {
		return r17_collateral_amount;
	}
	public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
		this.r17_collateral_amount = r17_collateral_amount;
	}
	public BigDecimal getR17_carrying_amount() {
		return r17_carrying_amount;
	}
	public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
		this.r17_carrying_amount = r17_carrying_amount;
	}
	public BigDecimal getR17_no_of_accts() {
		return r17_no_of_accts;
	}
	public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
		this.r17_no_of_accts = r17_no_of_accts;
	}
	public String getR18_rene_loans() {
		return r18_rene_loans;
	}
	public void setR18_rene_loans(String r18_rene_loans) {
		this.r18_rene_loans = r18_rene_loans;
	}
	public BigDecimal getR18_collateral_amount() {
		return r18_collateral_amount;
	}
	public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
		this.r18_collateral_amount = r18_collateral_amount;
	}
	public BigDecimal getR18_carrying_amount() {
		return r18_carrying_amount;
	}
	public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
		this.r18_carrying_amount = r18_carrying_amount;
	}
	public BigDecimal getR18_no_of_accts() {
		return r18_no_of_accts;
	}
	public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
		this.r18_no_of_accts = r18_no_of_accts;
	}
	public String getR19_rene_loans() {
		return r19_rene_loans;
	}
	public void setR19_rene_loans(String r19_rene_loans) {
		this.r19_rene_loans = r19_rene_loans;
	}
	public BigDecimal getR19_collateral_amount() {
		return r19_collateral_amount;
	}
	public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
		this.r19_collateral_amount = r19_collateral_amount;
	}
	public BigDecimal getR19_carrying_amount() {
		return r19_carrying_amount;
	}
	public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
		this.r19_carrying_amount = r19_carrying_amount;
	}
	public BigDecimal getR19_no_of_accts() {
		return r19_no_of_accts;
	}
	public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
		this.r19_no_of_accts = r19_no_of_accts;
	}
	public String getR20_rene_loans() {
		return r20_rene_loans;
	}
	public void setR20_rene_loans(String r20_rene_loans) {
		this.r20_rene_loans = r20_rene_loans;
	}
	public BigDecimal getR20_collateral_amount() {
		return r20_collateral_amount;
	}
	public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
		this.r20_collateral_amount = r20_collateral_amount;
	}
	public BigDecimal getR20_carrying_amount() {
		return r20_carrying_amount;
	}
	public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
		this.r20_carrying_amount = r20_carrying_amount;
	}
	public BigDecimal getR20_no_of_accts() {
		return r20_no_of_accts;
	}
	public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
		this.r20_no_of_accts = r20_no_of_accts;
	}
	public String getR21_rene_loans() {
		return r21_rene_loans;
	}
	public void setR21_rene_loans(String r21_rene_loans) {
		this.r21_rene_loans = r21_rene_loans;
	}
	public BigDecimal getR21_collateral_amount() {
		return r21_collateral_amount;
	}
	public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
		this.r21_collateral_amount = r21_collateral_amount;
	}
	public BigDecimal getR21_carrying_amount() {
		return r21_carrying_amount;
	}
	public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
		this.r21_carrying_amount = r21_carrying_amount;
	}
	public BigDecimal getR21_no_of_accts() {
		return r21_no_of_accts;
	}
	public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
		this.r21_no_of_accts = r21_no_of_accts;
	}
	public String getR22_rene_loans() {
		return r22_rene_loans;
	}
	public void setR22_rene_loans(String r22_rene_loans) {
		this.r22_rene_loans = r22_rene_loans;
	}
	public BigDecimal getR22_collateral_amount() {
		return r22_collateral_amount;
	}
	public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
		this.r22_collateral_amount = r22_collateral_amount;
	}
	public BigDecimal getR22_carrying_amount() {
		return r22_carrying_amount;
	}
	public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
		this.r22_carrying_amount = r22_carrying_amount;
	}
	public BigDecimal getR22_no_of_accts() {
		return r22_no_of_accts;
	}
	public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
		this.r22_no_of_accts = r22_no_of_accts;
	}
	public String getR23_rene_loans() {
		return r23_rene_loans;
	}
	public void setR23_rene_loans(String r23_rene_loans) {
		this.r23_rene_loans = r23_rene_loans;
	}
	public BigDecimal getR23_collateral_amount() {
		return r23_collateral_amount;
	}
	public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
		this.r23_collateral_amount = r23_collateral_amount;
	}
	public BigDecimal getR23_carrying_amount() {
		return r23_carrying_amount;
	}
	public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
		this.r23_carrying_amount = r23_carrying_amount;
	}
	public BigDecimal getR23_no_of_accts() {
		return r23_no_of_accts;
	}
	public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
		this.r23_no_of_accts = r23_no_of_accts;
	}
	public String getR24_rene_loans() {
		return r24_rene_loans;
	}
	public void setR24_rene_loans(String r24_rene_loans) {
		this.r24_rene_loans = r24_rene_loans;
	}
	public BigDecimal getR24_collateral_amount() {
		return r24_collateral_amount;
	}
	public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
		this.r24_collateral_amount = r24_collateral_amount;
	}
	public BigDecimal getR24_carrying_amount() {
		return r24_carrying_amount;
	}
	public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
		this.r24_carrying_amount = r24_carrying_amount;
	}
	public BigDecimal getR24_no_of_accts() {
		return r24_no_of_accts;
	}
	public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
		this.r24_no_of_accts = r24_no_of_accts;
	}
	public String getR25_rene_loans() {
		return r25_rene_loans;
	}
	public void setR25_rene_loans(String r25_rene_loans) {
		this.r25_rene_loans = r25_rene_loans;
	}
	public BigDecimal getR25_collateral_amount() {
		return r25_collateral_amount;
	}
	public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
		this.r25_collateral_amount = r25_collateral_amount;
	}
	public BigDecimal getR25_carrying_amount() {
		return r25_carrying_amount;
	}
	public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
		this.r25_carrying_amount = r25_carrying_amount;
	}
	public BigDecimal getR25_no_of_accts() {
		return r25_no_of_accts;
	}
	public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
		this.r25_no_of_accts = r25_no_of_accts;
	}
	public String getR26_rene_loans() {
		return r26_rene_loans;
	}
	public void setR26_rene_loans(String r26_rene_loans) {
		this.r26_rene_loans = r26_rene_loans;
	}
	public BigDecimal getR26_collateral_amount() {
		return r26_collateral_amount;
	}
	public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
		this.r26_collateral_amount = r26_collateral_amount;
	}
	public BigDecimal getR26_carrying_amount() {
		return r26_carrying_amount;
	}
	public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
		this.r26_carrying_amount = r26_carrying_amount;
	}
	public BigDecimal getR26_no_of_accts() {
		return r26_no_of_accts;
	}
	public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
		this.r26_no_of_accts = r26_no_of_accts;
	}
	public String getR27_rene_loans() {
		return r27_rene_loans;
	}
	public void setR27_rene_loans(String r27_rene_loans) {
		this.r27_rene_loans = r27_rene_loans;
	}
	public BigDecimal getR27_collateral_amount() {
		return r27_collateral_amount;
	}
	public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
		this.r27_collateral_amount = r27_collateral_amount;
	}
	public BigDecimal getR27_carrying_amount() {
		return r27_carrying_amount;
	}
	public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
		this.r27_carrying_amount = r27_carrying_amount;
	}
	public BigDecimal getR27_no_of_accts() {
		return r27_no_of_accts;
	}
	public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
		this.r27_no_of_accts = r27_no_of_accts;
	}
	public String getR28_rene_loans() {
		return r28_rene_loans;
	}
	public void setR28_rene_loans(String r28_rene_loans) {
		this.r28_rene_loans = r28_rene_loans;
	}
	public BigDecimal getR28_collateral_amount() {
		return r28_collateral_amount;
	}
	public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
		this.r28_collateral_amount = r28_collateral_amount;
	}
	public BigDecimal getR28_carrying_amount() {
		return r28_carrying_amount;
	}
	public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
		this.r28_carrying_amount = r28_carrying_amount;
	}
	public BigDecimal getR28_no_of_accts() {
		return r28_no_of_accts;
	}
	public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
		this.r28_no_of_accts = r28_no_of_accts;
	}
	public String getR29_rene_loans() {
		return r29_rene_loans;
	}
	public void setR29_rene_loans(String r29_rene_loans) {
		this.r29_rene_loans = r29_rene_loans;
	}
	public BigDecimal getR29_collateral_amount() {
		return r29_collateral_amount;
	}
	public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
		this.r29_collateral_amount = r29_collateral_amount;
	}
	public BigDecimal getR29_carrying_amount() {
		return r29_carrying_amount;
	}
	public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
		this.r29_carrying_amount = r29_carrying_amount;
	}
	public BigDecimal getR29_no_of_accts() {
		return r29_no_of_accts;
	}
	public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
		this.r29_no_of_accts = r29_no_of_accts;
	}
	public String getR30_rene_loans() {
		return r30_rene_loans;
	}
	public void setR30_rene_loans(String r30_rene_loans) {
		this.r30_rene_loans = r30_rene_loans;
	}
	public BigDecimal getR30_collateral_amount() {
		return r30_collateral_amount;
	}
	public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
		this.r30_collateral_amount = r30_collateral_amount;
	}
	public BigDecimal getR30_carrying_amount() {
		return r30_carrying_amount;
	}
	public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
		this.r30_carrying_amount = r30_carrying_amount;
	}
	public BigDecimal getR30_no_of_accts() {
		return r30_no_of_accts;
	}
	public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
		this.r30_no_of_accts = r30_no_of_accts;
	}
	public String getR31_rene_loans() {
		return r31_rene_loans;
	}
	public void setR31_rene_loans(String r31_rene_loans) {
		this.r31_rene_loans = r31_rene_loans;
	}
	public BigDecimal getR31_collateral_amount() {
		return r31_collateral_amount;
	}
	public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
		this.r31_collateral_amount = r31_collateral_amount;
	}
	public BigDecimal getR31_carrying_amount() {
		return r31_carrying_amount;
	}
	public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
		this.r31_carrying_amount = r31_carrying_amount;
	}
	public BigDecimal getR31_no_of_accts() {
		return r31_no_of_accts;
	}
	public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
		this.r31_no_of_accts = r31_no_of_accts;
	}
	public String getR32_rene_loans() {
		return r32_rene_loans;
	}
	public void setR32_rene_loans(String r32_rene_loans) {
		this.r32_rene_loans = r32_rene_loans;
	}
	public BigDecimal getR32_collateral_amount() {
		return r32_collateral_amount;
	}
	public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
		this.r32_collateral_amount = r32_collateral_amount;
	}
	public BigDecimal getR32_carrying_amount() {
		return r32_carrying_amount;
	}
	public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
		this.r32_carrying_amount = r32_carrying_amount;
	}
	public BigDecimal getR32_no_of_accts() {
		return r32_no_of_accts;
	}
	public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
		this.r32_no_of_accts = r32_no_of_accts;
	}
	public String getR33_rene_loans() {
		return r33_rene_loans;
	}
	public void setR33_rene_loans(String r33_rene_loans) {
		this.r33_rene_loans = r33_rene_loans;
	}
	public BigDecimal getR33_collateral_amount() {
		return r33_collateral_amount;
	}
	public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
		this.r33_collateral_amount = r33_collateral_amount;
	}
	public BigDecimal getR33_carrying_amount() {
		return r33_carrying_amount;
	}
	public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
		this.r33_carrying_amount = r33_carrying_amount;
	}
	public BigDecimal getR33_no_of_accts() {
		return r33_no_of_accts;
	}
	public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
		this.r33_no_of_accts = r33_no_of_accts;
	}
	public String getR34_rene_loans() {
		return r34_rene_loans;
	}
	public void setR34_rene_loans(String r34_rene_loans) {
		this.r34_rene_loans = r34_rene_loans;
	}
	public BigDecimal getR34_collateral_amount() {
		return r34_collateral_amount;
	}
	public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
		this.r34_collateral_amount = r34_collateral_amount;
	}
	public BigDecimal getR34_carrying_amount() {
		return r34_carrying_amount;
	}
	public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
		this.r34_carrying_amount = r34_carrying_amount;
	}
	public BigDecimal getR34_no_of_accts() {
		return r34_no_of_accts;
	}
	public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
		this.r34_no_of_accts = r34_no_of_accts;
	}
	public String getR35_rene_loans() {
		return r35_rene_loans;
	}
	public void setR35_rene_loans(String r35_rene_loans) {
		this.r35_rene_loans = r35_rene_loans;
	}
	public BigDecimal getR35_collateral_amount() {
		return r35_collateral_amount;
	}
	public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
		this.r35_collateral_amount = r35_collateral_amount;
	}
	public BigDecimal getR35_carrying_amount() {
		return r35_carrying_amount;
	}
	public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
		this.r35_carrying_amount = r35_carrying_amount;
	}
	public BigDecimal getR35_no_of_accts() {
		return r35_no_of_accts;
	}
	public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
		this.r35_no_of_accts = r35_no_of_accts;
	}
	public String getR36_rene_loans() {
		return r36_rene_loans;
	}
	public void setR36_rene_loans(String r36_rene_loans) {
		this.r36_rene_loans = r36_rene_loans;
	}
	public BigDecimal getR36_collateral_amount() {
		return r36_collateral_amount;
	}
	public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
		this.r36_collateral_amount = r36_collateral_amount;
	}
	public BigDecimal getR36_carrying_amount() {
		return r36_carrying_amount;
	}
	public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
		this.r36_carrying_amount = r36_carrying_amount;
	}
	public BigDecimal getR36_no_of_accts() {
		return r36_no_of_accts;
	}
	public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
		this.r36_no_of_accts = r36_no_of_accts;
	}
	public String getR37_rene_loans() {
		return r37_rene_loans;
	}
	public void setR37_rene_loans(String r37_rene_loans) {
		this.r37_rene_loans = r37_rene_loans;
	}
	public BigDecimal getR37_collateral_amount() {
		return r37_collateral_amount;
	}
	public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
		this.r37_collateral_amount = r37_collateral_amount;
	}
	public BigDecimal getR37_carrying_amount() {
		return r37_carrying_amount;
	}
	public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
		this.r37_carrying_amount = r37_carrying_amount;
	}
	public BigDecimal getR37_no_of_accts() {
		return r37_no_of_accts;
	}
	public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
		this.r37_no_of_accts = r37_no_of_accts;
	}
	public String getR38_rene_loans() {
		return r38_rene_loans;
	}
	public void setR38_rene_loans(String r38_rene_loans) {
		this.r38_rene_loans = r38_rene_loans;
	}
	public BigDecimal getR38_collateral_amount() {
		return r38_collateral_amount;
	}
	public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
		this.r38_collateral_amount = r38_collateral_amount;
	}
	public BigDecimal getR38_carrying_amount() {
		return r38_carrying_amount;
	}
	public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
		this.r38_carrying_amount = r38_carrying_amount;
	}
	public BigDecimal getR38_no_of_accts() {
		return r38_no_of_accts;
	}
	public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
		this.r38_no_of_accts = r38_no_of_accts;
	}
	public String getR39_rene_loans() {
		return r39_rene_loans;
	}
	public void setR39_rene_loans(String r39_rene_loans) {
		this.r39_rene_loans = r39_rene_loans;
	}
	public BigDecimal getR39_collateral_amount() {
		return r39_collateral_amount;
	}
	public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
		this.r39_collateral_amount = r39_collateral_amount;
	}
	public BigDecimal getR39_carrying_amount() {
		return r39_carrying_amount;
	}
	public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
		this.r39_carrying_amount = r39_carrying_amount;
	}
	public BigDecimal getR39_no_of_accts() {
		return r39_no_of_accts;
	}
	public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
		this.r39_no_of_accts = r39_no_of_accts;
	}
	public String getR40_rene_loans() {
		return r40_rene_loans;
	}
	public void setR40_rene_loans(String r40_rene_loans) {
		this.r40_rene_loans = r40_rene_loans;
	}
	public BigDecimal getR40_collateral_amount() {
		return r40_collateral_amount;
	}
	public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
		this.r40_collateral_amount = r40_collateral_amount;
	}
	public BigDecimal getR40_carrying_amount() {
		return r40_carrying_amount;
	}
	public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
		this.r40_carrying_amount = r40_carrying_amount;
	}
	public BigDecimal getR40_no_of_accts() {
		return r40_no_of_accts;
	}
	public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
		this.r40_no_of_accts = r40_no_of_accts;
	}
	public String getR41_rene_loans() {
		return r41_rene_loans;
	}
	public void setR41_rene_loans(String r41_rene_loans) {
		this.r41_rene_loans = r41_rene_loans;
	}
	public BigDecimal getR41_collateral_amount() {
		return r41_collateral_amount;
	}
	public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
		this.r41_collateral_amount = r41_collateral_amount;
	}
	public BigDecimal getR41_carrying_amount() {
		return r41_carrying_amount;
	}
	public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
		this.r41_carrying_amount = r41_carrying_amount;
	}
	public BigDecimal getR41_no_of_accts() {
		return r41_no_of_accts;
	}
	public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
		this.r41_no_of_accts = r41_no_of_accts;
	}
	public String getR42_rene_loans() {
		return r42_rene_loans;
	}
	public void setR42_rene_loans(String r42_rene_loans) {
		this.r42_rene_loans = r42_rene_loans;
	}
	public BigDecimal getR42_collateral_amount() {
		return r42_collateral_amount;
	}
	public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
		this.r42_collateral_amount = r42_collateral_amount;
	}
	public BigDecimal getR42_carrying_amount() {
		return r42_carrying_amount;
	}
	public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
		this.r42_carrying_amount = r42_carrying_amount;
	}
	public BigDecimal getR42_no_of_accts() {
		return r42_no_of_accts;
	}
	public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
		this.r42_no_of_accts = r42_no_of_accts;
	}
	public String getR43_rene_loans() {
		return r43_rene_loans;
	}
	public void setR43_rene_loans(String r43_rene_loans) {
		this.r43_rene_loans = r43_rene_loans;
	}
	public BigDecimal getR43_collateral_amount() {
		return r43_collateral_amount;
	}
	public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
		this.r43_collateral_amount = r43_collateral_amount;
	}
	public BigDecimal getR43_carrying_amount() {
		return r43_carrying_amount;
	}
	public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
		this.r43_carrying_amount = r43_carrying_amount;
	}
	public BigDecimal getR43_no_of_accts() {
		return r43_no_of_accts;
	}
	public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
		this.r43_no_of_accts = r43_no_of_accts;
	}
	public String getR44_rene_loans() {
		return r44_rene_loans;
	}
	public void setR44_rene_loans(String r44_rene_loans) {
		this.r44_rene_loans = r44_rene_loans;
	}
	public BigDecimal getR44_collateral_amount() {
		return r44_collateral_amount;
	}
	public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
		this.r44_collateral_amount = r44_collateral_amount;
	}
	public BigDecimal getR44_carrying_amount() {
		return r44_carrying_amount;
	}
	public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
		this.r44_carrying_amount = r44_carrying_amount;
	}
	public BigDecimal getR44_no_of_accts() {
		return r44_no_of_accts;
	}
	public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
		this.r44_no_of_accts = r44_no_of_accts;
	}
	public String getR45_rene_loans() {
		return r45_rene_loans;
	}
	public void setR45_rene_loans(String r45_rene_loans) {
		this.r45_rene_loans = r45_rene_loans;
	}
	public BigDecimal getR45_collateral_amount() {
		return r45_collateral_amount;
	}
	public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
		this.r45_collateral_amount = r45_collateral_amount;
	}
	public BigDecimal getR45_carrying_amount() {
		return r45_carrying_amount;
	}
	public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
		this.r45_carrying_amount = r45_carrying_amount;
	}
	public BigDecimal getR45_no_of_accts() {
		return r45_no_of_accts;
	}
	public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
		this.r45_no_of_accts = r45_no_of_accts;
	}
	public String getR46_rene_loans() {
		return r46_rene_loans;
	}
	public void setR46_rene_loans(String r46_rene_loans) {
		this.r46_rene_loans = r46_rene_loans;
	}
	public BigDecimal getR46_collateral_amount() {
		return r46_collateral_amount;
	}
	public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
		this.r46_collateral_amount = r46_collateral_amount;
	}
	public BigDecimal getR46_carrying_amount() {
		return r46_carrying_amount;
	}
	public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
		this.r46_carrying_amount = r46_carrying_amount;
	}
	public BigDecimal getR46_no_of_accts() {
		return r46_no_of_accts;
	}
	public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
		this.r46_no_of_accts = r46_no_of_accts;
	}
	public String getR47_rene_loans() {
		return r47_rene_loans;
	}
	public void setR47_rene_loans(String r47_rene_loans) {
		this.r47_rene_loans = r47_rene_loans;
	}
	public BigDecimal getR47_collateral_amount() {
		return r47_collateral_amount;
	}
	public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
		this.r47_collateral_amount = r47_collateral_amount;
	}
	public BigDecimal getR47_carrying_amount() {
		return r47_carrying_amount;
	}
	public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
		this.r47_carrying_amount = r47_carrying_amount;
	}
	public BigDecimal getR47_no_of_accts() {
		return r47_no_of_accts;
	}
	public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
		this.r47_no_of_accts = r47_no_of_accts;
	}
	public String getR48_rene_loans() {
		return r48_rene_loans;
	}
	public void setR48_rene_loans(String r48_rene_loans) {
		this.r48_rene_loans = r48_rene_loans;
	}
	public BigDecimal getR48_collateral_amount() {
		return r48_collateral_amount;
	}
	public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
		this.r48_collateral_amount = r48_collateral_amount;
	}
	public BigDecimal getR48_carrying_amount() {
		return r48_carrying_amount;
	}
	public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
		this.r48_carrying_amount = r48_carrying_amount;
	}
	public BigDecimal getR48_no_of_accts() {
		return r48_no_of_accts;
	}
	public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
		this.r48_no_of_accts = r48_no_of_accts;
	}
	public String getR49_rene_loans() {
		return r49_rene_loans;
	}
	public void setR49_rene_loans(String r49_rene_loans) {
		this.r49_rene_loans = r49_rene_loans;
	}
	public BigDecimal getR49_collateral_amount() {
		return r49_collateral_amount;
	}
	public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
		this.r49_collateral_amount = r49_collateral_amount;
	}
	public BigDecimal getR49_carrying_amount() {
		return r49_carrying_amount;
	}
	public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
		this.r49_carrying_amount = r49_carrying_amount;
	}
	public BigDecimal getR49_no_of_accts() {
		return r49_no_of_accts;
	}
	public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
		this.r49_no_of_accts = r49_no_of_accts;
	}
	public String getR50_rene_loans() {
		return r50_rene_loans;
	}
	public void setR50_rene_loans(String r50_rene_loans) {
		this.r50_rene_loans = r50_rene_loans;
	}
	public BigDecimal getR50_collateral_amount() {
		return r50_collateral_amount;
	}
	public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
		this.r50_collateral_amount = r50_collateral_amount;
	}
	public BigDecimal getR50_carrying_amount() {
		return r50_carrying_amount;
	}
	public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
		this.r50_carrying_amount = r50_carrying_amount;
	}
	public BigDecimal getR50_no_of_accts() {
		return r50_no_of_accts;
	}
	public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
		this.r50_no_of_accts = r50_no_of_accts;
	}
	public String getR51_rene_loans() {
		return r51_rene_loans;
	}
	public void setR51_rene_loans(String r51_rene_loans) {
		this.r51_rene_loans = r51_rene_loans;
	}
	public BigDecimal getR51_collateral_amount() {
		return r51_collateral_amount;
	}
	public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
		this.r51_collateral_amount = r51_collateral_amount;
	}
	public BigDecimal getR51_carrying_amount() {
		return r51_carrying_amount;
	}
	public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
		this.r51_carrying_amount = r51_carrying_amount;
	}
	public BigDecimal getR51_no_of_accts() {
		return r51_no_of_accts;
	}
	public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
		this.r51_no_of_accts = r51_no_of_accts;
	}
	public String getR52_rene_loans() {
		return r52_rene_loans;
	}
	public void setR52_rene_loans(String r52_rene_loans) {
		this.r52_rene_loans = r52_rene_loans;
	}
	public BigDecimal getR52_collateral_amount() {
		return r52_collateral_amount;
	}
	public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
		this.r52_collateral_amount = r52_collateral_amount;
	}
	public BigDecimal getR52_carrying_amount() {
		return r52_carrying_amount;
	}
	public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
		this.r52_carrying_amount = r52_carrying_amount;
	}
	public BigDecimal getR52_no_of_accts() {
		return r52_no_of_accts;
	}
	public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
		this.r52_no_of_accts = r52_no_of_accts;
	}
	public String getR53_rene_loans() {
		return r53_rene_loans;
	}
	public void setR53_rene_loans(String r53_rene_loans) {
		this.r53_rene_loans = r53_rene_loans;
	}
	public BigDecimal getR53_collateral_amount() {
		return r53_collateral_amount;
	}
	public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
		this.r53_collateral_amount = r53_collateral_amount;
	}
	public BigDecimal getR53_carrying_amount() {
		return r53_carrying_amount;
	}
	public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
		this.r53_carrying_amount = r53_carrying_amount;
	}
	public BigDecimal getR53_no_of_accts() {
		return r53_no_of_accts;
	}
	public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
		this.r53_no_of_accts = r53_no_of_accts;
	}
	public String getR54_rene_loans() {
		return r54_rene_loans;
	}
	public void setR54_rene_loans(String r54_rene_loans) {
		this.r54_rene_loans = r54_rene_loans;
	}
	public BigDecimal getR54_collateral_amount() {
		return r54_collateral_amount;
	}
	public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
		this.r54_collateral_amount = r54_collateral_amount;
	}
	public BigDecimal getR54_carrying_amount() {
		return r54_carrying_amount;
	}
	public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
		this.r54_carrying_amount = r54_carrying_amount;
	}
	public BigDecimal getR54_no_of_accts() {
		return r54_no_of_accts;
	}
	public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
		this.r54_no_of_accts = r54_no_of_accts;
	}
	public String getR55_rene_loans() {
		return r55_rene_loans;
	}
	public void setR55_rene_loans(String r55_rene_loans) {
		this.r55_rene_loans = r55_rene_loans;
	}
	public BigDecimal getR55_collateral_amount() {
		return r55_collateral_amount;
	}
	public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
		this.r55_collateral_amount = r55_collateral_amount;
	}
	public BigDecimal getR55_carrying_amount() {
		return r55_carrying_amount;
	}
	public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
		this.r55_carrying_amount = r55_carrying_amount;
	}
	public BigDecimal getR55_no_of_accts() {
		return r55_no_of_accts;
	}
	public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
		this.r55_no_of_accts = r55_no_of_accts;
	}
	public String getR56_rene_loans() {
		return r56_rene_loans;
	}
	public void setR56_rene_loans(String r56_rene_loans) {
		this.r56_rene_loans = r56_rene_loans;
	}
	public BigDecimal getR56_collateral_amount() {
		return r56_collateral_amount;
	}
	public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
		this.r56_collateral_amount = r56_collateral_amount;
	}
	public BigDecimal getR56_carrying_amount() {
		return r56_carrying_amount;
	}
	public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
		this.r56_carrying_amount = r56_carrying_amount;
	}
	public BigDecimal getR56_no_of_accts() {
		return r56_no_of_accts;
	}
	public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
		this.r56_no_of_accts = r56_no_of_accts;
	}
	public String getR57_rene_loans() {
		return r57_rene_loans;
	}
	public void setR57_rene_loans(String r57_rene_loans) {
		this.r57_rene_loans = r57_rene_loans;
	}
	public BigDecimal getR57_collateral_amount() {
		return r57_collateral_amount;
	}
	public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
		this.r57_collateral_amount = r57_collateral_amount;
	}
	public BigDecimal getR57_carrying_amount() {
		return r57_carrying_amount;
	}
	public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
		this.r57_carrying_amount = r57_carrying_amount;
	}
	public BigDecimal getR57_no_of_accts() {
		return r57_no_of_accts;
	}
	public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
		this.r57_no_of_accts = r57_no_of_accts;
	}
	public String getR58_rene_loans() {
		return r58_rene_loans;
	}
	public void setR58_rene_loans(String r58_rene_loans) {
		this.r58_rene_loans = r58_rene_loans;
	}
	public BigDecimal getR58_collateral_amount() {
		return r58_collateral_amount;
	}
	public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
		this.r58_collateral_amount = r58_collateral_amount;
	}
	public BigDecimal getR58_carrying_amount() {
		return r58_carrying_amount;
	}
	public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
		this.r58_carrying_amount = r58_carrying_amount;
	}
	public BigDecimal getR58_no_of_accts() {
		return r58_no_of_accts;
	}
	public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
		this.r58_no_of_accts = r58_no_of_accts;
	}
	public String getR59_rene_loans() {
		return r59_rene_loans;
	}
	public void setR59_rene_loans(String r59_rene_loans) {
		this.r59_rene_loans = r59_rene_loans;
	}
	public BigDecimal getR59_collateral_amount() {
		return r59_collateral_amount;
	}
	public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
		this.r59_collateral_amount = r59_collateral_amount;
	}
	public BigDecimal getR59_carrying_amount() {
		return r59_carrying_amount;
	}
	public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
		this.r59_carrying_amount = r59_carrying_amount;
	}
	public BigDecimal getR59_no_of_accts() {
		return r59_no_of_accts;
	}
	public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
		this.r59_no_of_accts = r59_no_of_accts;
	}
	public String getR60_rene_loans() {
		return r60_rene_loans;
	}
	public void setR60_rene_loans(String r60_rene_loans) {
		this.r60_rene_loans = r60_rene_loans;
	}
	public BigDecimal getR60_collateral_amount() {
		return r60_collateral_amount;
	}
	public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
		this.r60_collateral_amount = r60_collateral_amount;
	}
	public BigDecimal getR60_carrying_amount() {
		return r60_carrying_amount;
	}
	public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
		this.r60_carrying_amount = r60_carrying_amount;
	}
	public BigDecimal getR60_no_of_accts() {
		return r60_no_of_accts;
	}
	public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
		this.r60_no_of_accts = r60_no_of_accts;
	}
	public String getR61_rene_loans() {
		return r61_rene_loans;
	}
	public void setR61_rene_loans(String r61_rene_loans) {
		this.r61_rene_loans = r61_rene_loans;
	}
	public BigDecimal getR61_collateral_amount() {
		return r61_collateral_amount;
	}
	public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
		this.r61_collateral_amount = r61_collateral_amount;
	}
	public BigDecimal getR61_carrying_amount() {
		return r61_carrying_amount;
	}
	public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
		this.r61_carrying_amount = r61_carrying_amount;
	}
	public BigDecimal getR61_no_of_accts() {
		return r61_no_of_accts;
	}
	public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
		this.r61_no_of_accts = r61_no_of_accts;
	}
	public String getR62_rene_loans() {
		return r62_rene_loans;
	}
	public void setR62_rene_loans(String r62_rene_loans) {
		this.r62_rene_loans = r62_rene_loans;
	}
	public BigDecimal getR62_collateral_amount() {
		return r62_collateral_amount;
	}
	public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
		this.r62_collateral_amount = r62_collateral_amount;
	}
	public BigDecimal getR62_carrying_amount() {
		return r62_carrying_amount;
	}
	public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
		this.r62_carrying_amount = r62_carrying_amount;
	}
	public BigDecimal getR62_no_of_accts() {
		return r62_no_of_accts;
	}
	public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
		this.r62_no_of_accts = r62_no_of_accts;
	}
	public String getR63_rene_loans() {
		return r63_rene_loans;
	}
	public void setR63_rene_loans(String r63_rene_loans) {
		this.r63_rene_loans = r63_rene_loans;
	}
	public BigDecimal getR63_collateral_amount() {
		return r63_collateral_amount;
	}
	public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
		this.r63_collateral_amount = r63_collateral_amount;
	}
	public BigDecimal getR63_carrying_amount() {
		return r63_carrying_amount;
	}
	public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
		this.r63_carrying_amount = r63_carrying_amount;
	}
	public BigDecimal getR63_no_of_accts() {
		return r63_no_of_accts;
	}
	public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
		this.r63_no_of_accts = r63_no_of_accts;
	}
	public Q_RLFA1_Resub_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
	
    
    
    
}
