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
@Table(name = "BRRS_CAP_RATIO_BUFFER_ARCHIVALTABLE_SUMMARY")


public class CAP_RATIO_BUFFER_Archival_Summary_Entity {
	
	

		
	private String	r2_cap_ratio_buff;
	private BigDecimal	r2_cap_ratio_buff_amt;
	private String	r3_cap_ratio_buff;
	private BigDecimal	r3_cap_ratio_buff_amt;
	private String	r4_cap_ratio_buff;
	private BigDecimal	r4_cap_ratio_buff_amt;
	private String	r5_cap_ratio_buff;
	private BigDecimal	r5_cap_ratio_buff_amt;
	private String	r6_cap_ratio_buff;
	private BigDecimal	r6_cap_ratio_buff_amt;
	private String	r7_cap_ratio_buff;
	private BigDecimal	r7_cap_ratio_buff_amt;
	private String	r8_cap_ratio_buff;
	private BigDecimal	r8_cap_ratio_buff_amt;

	private String	r11_cap_ratio_buff;
	private BigDecimal	r11_cap_ratio_buff_amt;
	private String	r12_cap_ratio_buff;
	private BigDecimal	r12_cap_ratio_buff_amt;
	private String	r13_cap_ratio_buff;
	private BigDecimal	r13_cap_ratio_buff_amt;

	private String	r15_cap_ratio_buff;
	private BigDecimal	r15_cap_ratio_buff_amt;
	private String	r16_cap_ratio_buff;
	private BigDecimal	r16_cap_ratio_buff_amt;
	private String	r17_cap_ratio_buff;
	private BigDecimal	r17_cap_ratio_buff_amt;
	private String	r18_cap_ratio_buff;
	private BigDecimal	r18_cap_ratio_buff_amt;

	private String	r20_cap_ratio_buff;
	private BigDecimal	r20_cap_ratio_buff_amt;
	private String	r21_cap_ratio_buff;
	private BigDecimal	r21_cap_ratio_buff_amt;
	private String	r22_cap_ratio_buff;
	private BigDecimal	r22_cap_ratio_buff_amt;
	private String	r23_cap_ratio_buff;
	private BigDecimal	r23_cap_ratio_buff_amt;

	private String	r25_cap_ratio_buff;
	private BigDecimal	r25_cap_ratio_buff_amt;
	private String	r26_cap_ratio_buff;
	private BigDecimal	r26_cap_ratio_buff_amt;
	private String	r27_cap_ratio_buff;
	private BigDecimal	r27_cap_ratio_buff_amt;
	private String	r28_cap_ratio_buff;
	private BigDecimal	r28_cap_ratio_buff_amt;
	private String	r29_cap_ratio_buff;
	private BigDecimal	r29_cap_ratio_buff_amt;
	private String	r30_cap_ratio_buff;
	private BigDecimal	r30_cap_ratio_buff_amt;
	
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	
	private Date	report_date;
	private String	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR2_cap_ratio_buff() {
		return r2_cap_ratio_buff;
	}
	public void setR2_cap_ratio_buff(String r2_cap_ratio_buff) {
		this.r2_cap_ratio_buff = r2_cap_ratio_buff;
	}
	public BigDecimal getR2_cap_ratio_buff_amt() {
		return r2_cap_ratio_buff_amt;
	}
	public void setR2_cap_ratio_buff_amt(BigDecimal r2_cap_ratio_buff_amt) {
		this.r2_cap_ratio_buff_amt = r2_cap_ratio_buff_amt;
	}
	public String getR3_cap_ratio_buff() {
		return r3_cap_ratio_buff;
	}
	public void setR3_cap_ratio_buff(String r3_cap_ratio_buff) {
		this.r3_cap_ratio_buff = r3_cap_ratio_buff;
	}
	public BigDecimal getR3_cap_ratio_buff_amt() {
		return r3_cap_ratio_buff_amt;
	}
	public void setR3_cap_ratio_buff_amt(BigDecimal r3_cap_ratio_buff_amt) {
		this.r3_cap_ratio_buff_amt = r3_cap_ratio_buff_amt;
	}
	public String getR4_cap_ratio_buff() {
		return r4_cap_ratio_buff;
	}
	public void setR4_cap_ratio_buff(String r4_cap_ratio_buff) {
		this.r4_cap_ratio_buff = r4_cap_ratio_buff;
	}
	public BigDecimal getR4_cap_ratio_buff_amt() {
		return r4_cap_ratio_buff_amt;
	}
	public void setR4_cap_ratio_buff_amt(BigDecimal r4_cap_ratio_buff_amt) {
		this.r4_cap_ratio_buff_amt = r4_cap_ratio_buff_amt;
	}
	public String getR5_cap_ratio_buff() {
		return r5_cap_ratio_buff;
	}
	public void setR5_cap_ratio_buff(String r5_cap_ratio_buff) {
		this.r5_cap_ratio_buff = r5_cap_ratio_buff;
	}
	public BigDecimal getR5_cap_ratio_buff_amt() {
		return r5_cap_ratio_buff_amt;
	}
	public void setR5_cap_ratio_buff_amt(BigDecimal r5_cap_ratio_buff_amt) {
		this.r5_cap_ratio_buff_amt = r5_cap_ratio_buff_amt;
	}
	public String getR6_cap_ratio_buff() {
		return r6_cap_ratio_buff;
	}
	public void setR6_cap_ratio_buff(String r6_cap_ratio_buff) {
		this.r6_cap_ratio_buff = r6_cap_ratio_buff;
	}
	public BigDecimal getR6_cap_ratio_buff_amt() {
		return r6_cap_ratio_buff_amt;
	}
	public void setR6_cap_ratio_buff_amt(BigDecimal r6_cap_ratio_buff_amt) {
		this.r6_cap_ratio_buff_amt = r6_cap_ratio_buff_amt;
	}
	public String getR7_cap_ratio_buff() {
		return r7_cap_ratio_buff;
	}
	public void setR7_cap_ratio_buff(String r7_cap_ratio_buff) {
		this.r7_cap_ratio_buff = r7_cap_ratio_buff;
	}
	public BigDecimal getR7_cap_ratio_buff_amt() {
		return r7_cap_ratio_buff_amt;
	}
	public void setR7_cap_ratio_buff_amt(BigDecimal r7_cap_ratio_buff_amt) {
		this.r7_cap_ratio_buff_amt = r7_cap_ratio_buff_amt;
	}
	public String getR8_cap_ratio_buff() {
		return r8_cap_ratio_buff;
	}
	public void setR8_cap_ratio_buff(String r8_cap_ratio_buff) {
		this.r8_cap_ratio_buff = r8_cap_ratio_buff;
	}
	public BigDecimal getR8_cap_ratio_buff_amt() {
		return r8_cap_ratio_buff_amt;
	}
	public void setR8_cap_ratio_buff_amt(BigDecimal r8_cap_ratio_buff_amt) {
		this.r8_cap_ratio_buff_amt = r8_cap_ratio_buff_amt;
	}
	public String getR11_cap_ratio_buff() {
		return r11_cap_ratio_buff;
	}
	public void setR11_cap_ratio_buff(String r11_cap_ratio_buff) {
		this.r11_cap_ratio_buff = r11_cap_ratio_buff;
	}
	public BigDecimal getR11_cap_ratio_buff_amt() {
		return r11_cap_ratio_buff_amt;
	}
	public void setR11_cap_ratio_buff_amt(BigDecimal r11_cap_ratio_buff_amt) {
		this.r11_cap_ratio_buff_amt = r11_cap_ratio_buff_amt;
	}
	public String getR12_cap_ratio_buff() {
		return r12_cap_ratio_buff;
	}
	public void setR12_cap_ratio_buff(String r12_cap_ratio_buff) {
		this.r12_cap_ratio_buff = r12_cap_ratio_buff;
	}
	public BigDecimal getR12_cap_ratio_buff_amt() {
		return r12_cap_ratio_buff_amt;
	}
	public void setR12_cap_ratio_buff_amt(BigDecimal r12_cap_ratio_buff_amt) {
		this.r12_cap_ratio_buff_amt = r12_cap_ratio_buff_amt;
	}
	public String getR13_cap_ratio_buff() {
		return r13_cap_ratio_buff;
	}
	public void setR13_cap_ratio_buff(String r13_cap_ratio_buff) {
		this.r13_cap_ratio_buff = r13_cap_ratio_buff;
	}
	public BigDecimal getR13_cap_ratio_buff_amt() {
		return r13_cap_ratio_buff_amt;
	}
	public void setR13_cap_ratio_buff_amt(BigDecimal r13_cap_ratio_buff_amt) {
		this.r13_cap_ratio_buff_amt = r13_cap_ratio_buff_amt;
	}
	public String getR15_cap_ratio_buff() {
		return r15_cap_ratio_buff;
	}
	public void setR15_cap_ratio_buff(String r15_cap_ratio_buff) {
		this.r15_cap_ratio_buff = r15_cap_ratio_buff;
	}
	public BigDecimal getR15_cap_ratio_buff_amt() {
		return r15_cap_ratio_buff_amt;
	}
	public void setR15_cap_ratio_buff_amt(BigDecimal r15_cap_ratio_buff_amt) {
		this.r15_cap_ratio_buff_amt = r15_cap_ratio_buff_amt;
	}
	public String getR16_cap_ratio_buff() {
		return r16_cap_ratio_buff;
	}
	public void setR16_cap_ratio_buff(String r16_cap_ratio_buff) {
		this.r16_cap_ratio_buff = r16_cap_ratio_buff;
	}
	public BigDecimal getR16_cap_ratio_buff_amt() {
		return r16_cap_ratio_buff_amt;
	}
	public void setR16_cap_ratio_buff_amt(BigDecimal r16_cap_ratio_buff_amt) {
		this.r16_cap_ratio_buff_amt = r16_cap_ratio_buff_amt;
	}
	public String getR17_cap_ratio_buff() {
		return r17_cap_ratio_buff;
	}
	public void setR17_cap_ratio_buff(String r17_cap_ratio_buff) {
		this.r17_cap_ratio_buff = r17_cap_ratio_buff;
	}
	public BigDecimal getR17_cap_ratio_buff_amt() {
		return r17_cap_ratio_buff_amt;
	}
	public void setR17_cap_ratio_buff_amt(BigDecimal r17_cap_ratio_buff_amt) {
		this.r17_cap_ratio_buff_amt = r17_cap_ratio_buff_amt;
	}
	public String getR18_cap_ratio_buff() {
		return r18_cap_ratio_buff;
	}
	public void setR18_cap_ratio_buff(String r18_cap_ratio_buff) {
		this.r18_cap_ratio_buff = r18_cap_ratio_buff;
	}
	public BigDecimal getR18_cap_ratio_buff_amt() {
		return r18_cap_ratio_buff_amt;
	}
	public void setR18_cap_ratio_buff_amt(BigDecimal r18_cap_ratio_buff_amt) {
		this.r18_cap_ratio_buff_amt = r18_cap_ratio_buff_amt;
	}
	public String getR20_cap_ratio_buff() {
		return r20_cap_ratio_buff;
	}
	public void setR20_cap_ratio_buff(String r20_cap_ratio_buff) {
		this.r20_cap_ratio_buff = r20_cap_ratio_buff;
	}
	public BigDecimal getR20_cap_ratio_buff_amt() {
		return r20_cap_ratio_buff_amt;
	}
	public void setR20_cap_ratio_buff_amt(BigDecimal r20_cap_ratio_buff_amt) {
		this.r20_cap_ratio_buff_amt = r20_cap_ratio_buff_amt;
	}
	public String getR21_cap_ratio_buff() {
		return r21_cap_ratio_buff;
	}
	public void setR21_cap_ratio_buff(String r21_cap_ratio_buff) {
		this.r21_cap_ratio_buff = r21_cap_ratio_buff;
	}
	public BigDecimal getR21_cap_ratio_buff_amt() {
		return r21_cap_ratio_buff_amt;
	}
	public void setR21_cap_ratio_buff_amt(BigDecimal r21_cap_ratio_buff_amt) {
		this.r21_cap_ratio_buff_amt = r21_cap_ratio_buff_amt;
	}
	public String getR22_cap_ratio_buff() {
		return r22_cap_ratio_buff;
	}
	public void setR22_cap_ratio_buff(String r22_cap_ratio_buff) {
		this.r22_cap_ratio_buff = r22_cap_ratio_buff;
	}
	public BigDecimal getR22_cap_ratio_buff_amt() {
		return r22_cap_ratio_buff_amt;
	}
	public void setR22_cap_ratio_buff_amt(BigDecimal r22_cap_ratio_buff_amt) {
		this.r22_cap_ratio_buff_amt = r22_cap_ratio_buff_amt;
	}
	public String getR23_cap_ratio_buff() {
		return r23_cap_ratio_buff;
	}
	public void setR23_cap_ratio_buff(String r23_cap_ratio_buff) {
		this.r23_cap_ratio_buff = r23_cap_ratio_buff;
	}
	public BigDecimal getR23_cap_ratio_buff_amt() {
		return r23_cap_ratio_buff_amt;
	}
	public void setR23_cap_ratio_buff_amt(BigDecimal r23_cap_ratio_buff_amt) {
		this.r23_cap_ratio_buff_amt = r23_cap_ratio_buff_amt;
	}
	public String getR25_cap_ratio_buff() {
		return r25_cap_ratio_buff;
	}
	public void setR25_cap_ratio_buff(String r25_cap_ratio_buff) {
		this.r25_cap_ratio_buff = r25_cap_ratio_buff;
	}
	public BigDecimal getR25_cap_ratio_buff_amt() {
		return r25_cap_ratio_buff_amt;
	}
	public void setR25_cap_ratio_buff_amt(BigDecimal r25_cap_ratio_buff_amt) {
		this.r25_cap_ratio_buff_amt = r25_cap_ratio_buff_amt;
	}
	public String getR26_cap_ratio_buff() {
		return r26_cap_ratio_buff;
	}
	public void setR26_cap_ratio_buff(String r26_cap_ratio_buff) {
		this.r26_cap_ratio_buff = r26_cap_ratio_buff;
	}
	public BigDecimal getR26_cap_ratio_buff_amt() {
		return r26_cap_ratio_buff_amt;
	}
	public void setR26_cap_ratio_buff_amt(BigDecimal r26_cap_ratio_buff_amt) {
		this.r26_cap_ratio_buff_amt = r26_cap_ratio_buff_amt;
	}
	public String getR27_cap_ratio_buff() {
		return r27_cap_ratio_buff;
	}
	public void setR27_cap_ratio_buff(String r27_cap_ratio_buff) {
		this.r27_cap_ratio_buff = r27_cap_ratio_buff;
	}
	public BigDecimal getR27_cap_ratio_buff_amt() {
		return r27_cap_ratio_buff_amt;
	}
	public void setR27_cap_ratio_buff_amt(BigDecimal r27_cap_ratio_buff_amt) {
		this.r27_cap_ratio_buff_amt = r27_cap_ratio_buff_amt;
	}
	public String getR28_cap_ratio_buff() {
		return r28_cap_ratio_buff;
	}
	public void setR28_cap_ratio_buff(String r28_cap_ratio_buff) {
		this.r28_cap_ratio_buff = r28_cap_ratio_buff;
	}
	public BigDecimal getR28_cap_ratio_buff_amt() {
		return r28_cap_ratio_buff_amt;
	}
	public void setR28_cap_ratio_buff_amt(BigDecimal r28_cap_ratio_buff_amt) {
		this.r28_cap_ratio_buff_amt = r28_cap_ratio_buff_amt;
	}
	public String getR29_cap_ratio_buff() {
		return r29_cap_ratio_buff;
	}
	public void setR29_cap_ratio_buff(String r29_cap_ratio_buff) {
		this.r29_cap_ratio_buff = r29_cap_ratio_buff;
	}
	public BigDecimal getR29_cap_ratio_buff_amt() {
		return r29_cap_ratio_buff_amt;
	}
	public void setR29_cap_ratio_buff_amt(BigDecimal r29_cap_ratio_buff_amt) {
		this.r29_cap_ratio_buff_amt = r29_cap_ratio_buff_amt;
	}
	public String getR30_cap_ratio_buff() {
		return r30_cap_ratio_buff;
	}
	public void setR30_cap_ratio_buff(String r30_cap_ratio_buff) {
		this.r30_cap_ratio_buff = r30_cap_ratio_buff;
	}
	public BigDecimal getR30_cap_ratio_buff_amt() {
		return r30_cap_ratio_buff_amt;
	}
	public void setR30_cap_ratio_buff_amt(BigDecimal r30_cap_ratio_buff_amt) {
		this.r30_cap_ratio_buff_amt = r30_cap_ratio_buff_amt;
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
	public CAP_RATIO_BUFFER_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
