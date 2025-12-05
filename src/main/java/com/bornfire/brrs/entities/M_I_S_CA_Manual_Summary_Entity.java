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
@Table(name = "BRRS_M_I_S_CA_MANUAL_SUMMARYTABLE")


public class M_I_S_CA_Manual_Summary_Entity {
	
	
	private BigDecimal	r12_write_offs;
	
	private BigDecimal	r13_write_offs;
	
	private BigDecimal	r14_write_offs;
	
	private BigDecimal	r16_write_offs;
	
	

	private BigDecimal	r17_write_offs;

	
	
	private BigDecimal	r18_write_offs;
	
	

	private BigDecimal	r19_write_offs;

	
	private BigDecimal	r20_write_offs;

	

	private BigDecimal	r21_write_offs;
	
	

	private BigDecimal	r22_write_offs;
	

	private BigDecimal	r23_write_offs;

	
	
	private BigDecimal	r24_write_offs;

	

	private BigDecimal	r25_write_offs;

	
	
	private BigDecimal	r26_write_offs;
	
	
	
	private BigDecimal	r27_write_offs;
	
	
	
	private BigDecimal	r28_write_offs;
	
	


	private BigDecimal	r30_write_offs;
	

	private BigDecimal	r31_write_offs;


	private BigDecimal	r32_write_offs;

	
	
	private BigDecimal	r33_write_offs;
	
	
	
	private BigDecimal	r34_write_offs;
	
	
	
	private BigDecimal	r35_write_offs;
	
	
	
	private BigDecimal	r36_write_offs;
	
	

	private BigDecimal	r37_write_offs;
	
	


	private BigDecimal	r39_write_offs;

	

	private BigDecimal	r40_write_offs;
	
	


	private BigDecimal	r42_write_offs;
	
	

	private BigDecimal	r43_write_offs;
	
	
	

	private BigDecimal	r44_write_offs;

	

	private BigDecimal	r45_write_offs;
	
	

	private BigDecimal	r46_write_offs;
	
	
	
	private BigDecimal	r47_write_offs;
	
	
	
	private BigDecimal	r48_write_offs;

	

	
	private BigDecimal	r50_write_offs;
	
	
	private BigDecimal	r51_write_offs;

	
	private BigDecimal	r52_write_offs;
	
	


	private BigDecimal	r54_write_offs;
	
	
	
	private BigDecimal	r55_write_offs;
	
	

	private BigDecimal	r56_write_offs;
	
	


	private BigDecimal	r58_write_offs;
	
	
	private BigDecimal	r59_write_offs;

	
	
	private BigDecimal	r60_write_offs;
	
	

	private BigDecimal	r61_write_offs;

	
	private BigDecimal	r62_write_offs;
	
	
	
	private BigDecimal	r63_write_offs;

	
	private BigDecimal	r67_total;
	
	private BigDecimal	r68_total;
	
	

	
	
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
	public BigDecimal getR12_write_offs() {
		return r12_write_offs;
	}
	public void setR12_write_offs(BigDecimal r12_write_offs) {
		this.r12_write_offs = r12_write_offs;
	}
	public BigDecimal getR13_write_offs() {
		return r13_write_offs;
	}
	public void setR13_write_offs(BigDecimal r13_write_offs) {
		this.r13_write_offs = r13_write_offs;
	}
	public BigDecimal getR14_write_offs() {
		return r14_write_offs;
	}
	public void setR14_write_offs(BigDecimal r14_write_offs) {
		this.r14_write_offs = r14_write_offs;
	}
	public BigDecimal getR16_write_offs() {
		return r16_write_offs;
	}
	public void setR16_write_offs(BigDecimal r16_write_offs) {
		this.r16_write_offs = r16_write_offs;
	}
	public BigDecimal getR17_write_offs() {
		return r17_write_offs;
	}
	public void setR17_write_offs(BigDecimal r17_write_offs) {
		this.r17_write_offs = r17_write_offs;
	}
	public BigDecimal getR18_write_offs() {
		return r18_write_offs;
	}
	public void setR18_write_offs(BigDecimal r18_write_offs) {
		this.r18_write_offs = r18_write_offs;
	}
	public BigDecimal getR19_write_offs() {
		return r19_write_offs;
	}
	public void setR19_write_offs(BigDecimal r19_write_offs) {
		this.r19_write_offs = r19_write_offs;
	}
	public BigDecimal getR20_write_offs() {
		return r20_write_offs;
	}
	public void setR20_write_offs(BigDecimal r20_write_offs) {
		this.r20_write_offs = r20_write_offs;
	}
	public BigDecimal getR21_write_offs() {
		return r21_write_offs;
	}
	public void setR21_write_offs(BigDecimal r21_write_offs) {
		this.r21_write_offs = r21_write_offs;
	}
	public BigDecimal getR22_write_offs() {
		return r22_write_offs;
	}
	public void setR22_write_offs(BigDecimal r22_write_offs) {
		this.r22_write_offs = r22_write_offs;
	}
	public BigDecimal getR23_write_offs() {
		return r23_write_offs;
	}
	public void setR23_write_offs(BigDecimal r23_write_offs) {
		this.r23_write_offs = r23_write_offs;
	}
	public BigDecimal getR24_write_offs() {
		return r24_write_offs;
	}
	public void setR24_write_offs(BigDecimal r24_write_offs) {
		this.r24_write_offs = r24_write_offs;
	}
	public BigDecimal getR25_write_offs() {
		return r25_write_offs;
	}
	public void setR25_write_offs(BigDecimal r25_write_offs) {
		this.r25_write_offs = r25_write_offs;
	}
	public BigDecimal getR26_write_offs() {
		return r26_write_offs;
	}
	public void setR26_write_offs(BigDecimal r26_write_offs) {
		this.r26_write_offs = r26_write_offs;
	}
	public BigDecimal getR27_write_offs() {
		return r27_write_offs;
	}
	public void setR27_write_offs(BigDecimal r27_write_offs) {
		this.r27_write_offs = r27_write_offs;
	}
	public BigDecimal getR28_write_offs() {
		return r28_write_offs;
	}
	public void setR28_write_offs(BigDecimal r28_write_offs) {
		this.r28_write_offs = r28_write_offs;
	}
	public BigDecimal getR30_write_offs() {
		return r30_write_offs;
	}
	public void setR30_write_offs(BigDecimal r30_write_offs) {
		this.r30_write_offs = r30_write_offs;
	}
	public BigDecimal getR31_write_offs() {
		return r31_write_offs;
	}
	public void setR31_write_offs(BigDecimal r31_write_offs) {
		this.r31_write_offs = r31_write_offs;
	}
	public BigDecimal getR32_write_offs() {
		return r32_write_offs;
	}
	public void setR32_write_offs(BigDecimal r32_write_offs) {
		this.r32_write_offs = r32_write_offs;
	}
	public BigDecimal getR33_write_offs() {
		return r33_write_offs;
	}
	public void setR33_write_offs(BigDecimal r33_write_offs) {
		this.r33_write_offs = r33_write_offs;
	}
	public BigDecimal getR34_write_offs() {
		return r34_write_offs;
	}
	public void setR34_write_offs(BigDecimal r34_write_offs) {
		this.r34_write_offs = r34_write_offs;
	}
	public BigDecimal getR35_write_offs() {
		return r35_write_offs;
	}
	public void setR35_write_offs(BigDecimal r35_write_offs) {
		this.r35_write_offs = r35_write_offs;
	}
	public BigDecimal getR36_write_offs() {
		return r36_write_offs;
	}
	public void setR36_write_offs(BigDecimal r36_write_offs) {
		this.r36_write_offs = r36_write_offs;
	}
	public BigDecimal getR37_write_offs() {
		return r37_write_offs;
	}
	public void setR37_write_offs(BigDecimal r37_write_offs) {
		this.r37_write_offs = r37_write_offs;
	}
	public BigDecimal getR39_write_offs() {
		return r39_write_offs;
	}
	public void setR39_write_offs(BigDecimal r39_write_offs) {
		this.r39_write_offs = r39_write_offs;
	}
	public BigDecimal getR40_write_offs() {
		return r40_write_offs;
	}
	public void setR40_write_offs(BigDecimal r40_write_offs) {
		this.r40_write_offs = r40_write_offs;
	}
	public BigDecimal getR42_write_offs() {
		return r42_write_offs;
	}
	public void setR42_write_offs(BigDecimal r42_write_offs) {
		this.r42_write_offs = r42_write_offs;
	}
	public BigDecimal getR43_write_offs() {
		return r43_write_offs;
	}
	public void setR43_write_offs(BigDecimal r43_write_offs) {
		this.r43_write_offs = r43_write_offs;
	}
	public BigDecimal getR44_write_offs() {
		return r44_write_offs;
	}
	public void setR44_write_offs(BigDecimal r44_write_offs) {
		this.r44_write_offs = r44_write_offs;
	}
	public BigDecimal getR45_write_offs() {
		return r45_write_offs;
	}
	public void setR45_write_offs(BigDecimal r45_write_offs) {
		this.r45_write_offs = r45_write_offs;
	}
	public BigDecimal getR46_write_offs() {
		return r46_write_offs;
	}
	public void setR46_write_offs(BigDecimal r46_write_offs) {
		this.r46_write_offs = r46_write_offs;
	}
	public BigDecimal getR47_write_offs() {
		return r47_write_offs;
	}
	public void setR47_write_offs(BigDecimal r47_write_offs) {
		this.r47_write_offs = r47_write_offs;
	}
	public BigDecimal getR48_write_offs() {
		return r48_write_offs;
	}
	public void setR48_write_offs(BigDecimal r48_write_offs) {
		this.r48_write_offs = r48_write_offs;
	}
	public BigDecimal getR50_write_offs() {
		return r50_write_offs;
	}
	public void setR50_write_offs(BigDecimal r50_write_offs) {
		this.r50_write_offs = r50_write_offs;
	}
	public BigDecimal getR51_write_offs() {
		return r51_write_offs;
	}
	public void setR51_write_offs(BigDecimal r51_write_offs) {
		this.r51_write_offs = r51_write_offs;
	}
	public BigDecimal getR52_write_offs() {
		return r52_write_offs;
	}
	public void setR52_write_offs(BigDecimal r52_write_offs) {
		this.r52_write_offs = r52_write_offs;
	}
	public BigDecimal getR54_write_offs() {
		return r54_write_offs;
	}
	public void setR54_write_offs(BigDecimal r54_write_offs) {
		this.r54_write_offs = r54_write_offs;
	}
	public BigDecimal getR55_write_offs() {
		return r55_write_offs;
	}
	public void setR55_write_offs(BigDecimal r55_write_offs) {
		this.r55_write_offs = r55_write_offs;
	}
	public BigDecimal getR56_write_offs() {
		return r56_write_offs;
	}
	public void setR56_write_offs(BigDecimal r56_write_offs) {
		this.r56_write_offs = r56_write_offs;
	}
	public BigDecimal getR58_write_offs() {
		return r58_write_offs;
	}
	public void setR58_write_offs(BigDecimal r58_write_offs) {
		this.r58_write_offs = r58_write_offs;
	}
	public BigDecimal getR59_write_offs() {
		return r59_write_offs;
	}
	public void setR59_write_offs(BigDecimal r59_write_offs) {
		this.r59_write_offs = r59_write_offs;
	}
	public BigDecimal getR60_write_offs() {
		return r60_write_offs;
	}
	public void setR60_write_offs(BigDecimal r60_write_offs) {
		this.r60_write_offs = r60_write_offs;
	}
	public BigDecimal getR61_write_offs() {
		return r61_write_offs;
	}
	public void setR61_write_offs(BigDecimal r61_write_offs) {
		this.r61_write_offs = r61_write_offs;
	}
	public BigDecimal getR62_write_offs() {
		return r62_write_offs;
	}
	public void setR62_write_offs(BigDecimal r62_write_offs) {
		this.r62_write_offs = r62_write_offs;
	}
	public BigDecimal getR63_write_offs() {
		return r63_write_offs;
	}
	public void setR63_write_offs(BigDecimal r63_write_offs) {
		this.r63_write_offs = r63_write_offs;
	}
	public BigDecimal getR67_total() {
		return r67_total;
	}
	public void setR67_total(BigDecimal r67_total) {
		this.r67_total = r67_total;
	}
	public BigDecimal getR68_total() {
		return r68_total;
	}
	public void setR68_total(BigDecimal r68_total) {
		this.r68_total = r68_total;
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
	public M_I_S_CA_Manual_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
	
}
