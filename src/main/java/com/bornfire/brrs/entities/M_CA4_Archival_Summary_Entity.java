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
@Table(name = "BRRS_M_CA4_ARCHIVALTABLE_SUMMARY")


public class M_CA4_Archival_Summary_Entity {
	
	private String	r10_item ;
	private BigDecimal	r10_amt_name_of_sub1 ;
	private BigDecimal	r10_amt_name_of_sub2 ;
	private BigDecimal	r10_amt_name_of_sub3 ;
	private BigDecimal	r10_amt_name_of_sub4 ;
	private BigDecimal	r10_amt_name_of_sub5 ;
	private BigDecimal	r10_tot_amt ;
		
	private String	r11_item ;
	private BigDecimal	r11_amt_name_of_sub1 ;
	private BigDecimal	r11_amt_name_of_sub2 ;
	private BigDecimal	r11_amt_name_of_sub3 ;
	private BigDecimal	r11_amt_name_of_sub4 ;
	private BigDecimal	r11_amt_name_of_sub5 ;
	private BigDecimal	r11_tot_amt ;
		
	private String	r12_item ;
	private BigDecimal	r12_amt_name_of_sub1 ;
	private BigDecimal	r12_amt_name_of_sub2 ;
	private BigDecimal	r12_amt_name_of_sub3 ;
	private BigDecimal	r12_amt_name_of_sub4 ;
	private BigDecimal	r12_amt_name_of_sub5 ;
	private BigDecimal	r12_tot_amt ;
		
	private String	r13_item ;
	private BigDecimal	r13_amt_name_of_sub1 ;
	private BigDecimal	r13_amt_name_of_sub2 ;
	private BigDecimal	r13_amt_name_of_sub3 ;
	private BigDecimal	r13_amt_name_of_sub4 ;
	private BigDecimal	r13_amt_name_of_sub5 ;
	private BigDecimal	r13_tot_amt ;
		
	private String	r14_item ;
	private BigDecimal	r14_amt_name_of_sub1 ;
	private BigDecimal	r14_amt_name_of_sub2 ;
	private BigDecimal	r14_amt_name_of_sub3 ;
	private BigDecimal	r14_amt_name_of_sub4 ;
	private BigDecimal	r14_amt_name_of_sub5 ;
	private BigDecimal	r14_tot_amt ;
		
		
	private String	r17_item ;
	private BigDecimal	r17_amt_name_of_sub1 ;
	private BigDecimal	r17_amt_name_of_sub2 ;
	private BigDecimal	r17_amt_name_of_sub3 ;
	private BigDecimal	r17_amt_name_of_sub4 ;
	private BigDecimal	r17_amt_name_of_sub5 ;
	private BigDecimal	r17_tot_amt ;
		
	private String	r18_item ;
	private BigDecimal	r18_amt_name_of_sub1 ;
	private BigDecimal	r18_amt_name_of_sub2 ;
	private BigDecimal	r18_amt_name_of_sub3 ;
	private BigDecimal	r18_amt_name_of_sub4 ;
	private BigDecimal	r18_amt_name_of_sub5 ;
	private BigDecimal	r18_tot_amt ;
		
	private String	r19_item ;
	private BigDecimal	r19_amt_name_of_sub1 ;
	private BigDecimal	r19_amt_name_of_sub2 ;
	private BigDecimal	r19_amt_name_of_sub3 ;
	private BigDecimal	r19_amt_name_of_sub4 ;
	private BigDecimal	r19_amt_name_of_sub5 ;
	private BigDecimal	r19_tot_amt ;
		
	private String	r22_item ;
	private BigDecimal	r22_amt_name_of_sub1 ;
	private BigDecimal	r22_amt_name_of_sub2 ;
	private BigDecimal	r22_amt_name_of_sub3 ;
	private BigDecimal	r22_amt_name_of_sub4 ;
	private BigDecimal	r22_amt_name_of_sub5 ;
	private BigDecimal	r22_tot_amt ;
		
	private String	r23_item ;
	private BigDecimal	r23_amt_name_of_sub1 ;
	private BigDecimal	r23_amt_name_of_sub2 ;
	private BigDecimal	r23_amt_name_of_sub3 ;
	private BigDecimal	r23_amt_name_of_sub4 ;
	private BigDecimal	r23_amt_name_of_sub5 ;
	private BigDecimal	r23_tot_amt ;
		
	private String	r24_item ;
	private BigDecimal	r24_amt_name_of_sub1 ;
	private BigDecimal	r24_amt_name_of_sub2 ;
	private BigDecimal	r24_amt_name_of_sub3 ;
	private BigDecimal	r24_amt_name_of_sub4 ;
	private BigDecimal	r24_amt_name_of_sub5 ;
	private BigDecimal	r24_tot_amt ;
		
	private String	r27_item ;
	private BigDecimal	r27_amt_name_of_sub1 ;
	private BigDecimal	r27_amt_name_of_sub2 ;
	private BigDecimal	r27_amt_name_of_sub3 ;
	private BigDecimal	r27_amt_name_of_sub4 ;
	private BigDecimal	r27_amt_name_of_sub5 ;
	private BigDecimal	r27_tot_amt ;
		
	private String	r28_item ;
	private BigDecimal	r28_amt_name_of_sub1 ;
	private BigDecimal	r28_amt_name_of_sub2 ;
	private BigDecimal	r28_amt_name_of_sub3 ;
	private BigDecimal	r28_amt_name_of_sub4 ;
	private BigDecimal	r28_amt_name_of_sub5 ;
	private BigDecimal	r28_tot_amt ;
		
	private String	r29_item ;
	private BigDecimal	r29_amt_name_of_sub1 ;
	private BigDecimal	r29_amt_name_of_sub2 ;
	private BigDecimal	r29_amt_name_of_sub3 ;
	private BigDecimal	r29_amt_name_of_sub4 ;
	private BigDecimal	r29_amt_name_of_sub5 ;
	private BigDecimal	r29_tot_amt ;
		
	private String	r32_item ;
	private BigDecimal	r32_amt_name_of_sub1 ;
	private BigDecimal	r32_amt_name_of_sub2 ;
	private BigDecimal	r32_amt_name_of_sub3 ;
	private BigDecimal	r32_amt_name_of_sub4 ;
	private BigDecimal	r32_amt_name_of_sub5 ;
	private BigDecimal	r32_tot_amt ;
		
	private String	r33_item ;
	private BigDecimal	r33_amt_name_of_sub1 ;
	private BigDecimal	r33_amt_name_of_sub2 ;
	private BigDecimal	r33_amt_name_of_sub3 ;
	private BigDecimal	r33_amt_name_of_sub4 ;
	private BigDecimal	r33_amt_name_of_sub5 ;
	private BigDecimal	r33_tot_amt ;
		
	private String	r34_item ;
	private BigDecimal	r34_amt_name_of_sub1 ;
	private BigDecimal	r34_amt_name_of_sub2 ;
	private BigDecimal	r34_amt_name_of_sub3 ;
	private BigDecimal	r34_amt_name_of_sub4 ;
	private BigDecimal	r34_amt_name_of_sub5 ;
	private BigDecimal	r34_tot_amt ;
		
	private String	r36_item ;
	private BigDecimal	r36_amt_name_of_sub1;
	private BigDecimal	r36_amt_name_of_sub2;
	private BigDecimal	r36_amt_name_of_sub3 ;
	private BigDecimal	r36_amt_name_of_sub4 ;
	private BigDecimal	r36_amt_name_of_sub5 ;
	private BigDecimal	r36_tot_amt ;
	private String	r37_item ;
	private BigDecimal	r37_amt_name_of_sub1;
	private BigDecimal	r37_amt_name_of_sub2;
	private BigDecimal	r37_amt_name_of_sub3;
	private BigDecimal	r37_amt_name_of_sub4;
	private BigDecimal	r37_amt_name_of_sub5 ;
	private BigDecimal	r37_tot_amt ;
	private String	r38_item ;
	private BigDecimal	r38_amt_name_of_sub1 ;
	private BigDecimal	r38_amt_name_of_sub2 ;
	private BigDecimal	r38_amt_name_of_sub3 ;
	private BigDecimal	r38_amt_name_of_sub4 ;
	private BigDecimal	r38_amt_name_of_sub5 ;
	private BigDecimal	r38_tot_amt ;
		
	private String	r39_item ;
	private BigDecimal	r39_amt_name_of_sub1 ;
	private BigDecimal	r39_amt_name_of_sub2 ;
	private BigDecimal	r39_amt_name_of_sub3 ;
	private BigDecimal	r39_amt_name_of_sub4 ;
	private BigDecimal	r39_amt_name_of_sub5 ;
	private BigDecimal	r39_tot_amt ;
		
	private String	r40_item ;
	private BigDecimal	r40_amt_name_of_sub1 ;
	private BigDecimal	r40_amt_name_of_sub2 ;
	private BigDecimal	r40_amt_name_of_sub3 ;
	private BigDecimal	r40_amt_name_of_sub4 ;
	private BigDecimal	r40_amt_name_of_sub5 ;
	private BigDecimal	r40_tot_amt ;
		
	private String	r41_item ;
	private BigDecimal	r41_amt_name_of_sub1 ;
	private BigDecimal	r41_amt_name_of_sub2 ;
	private BigDecimal	r41_amt_name_of_sub3 ;
	private BigDecimal	r41_amt_name_of_sub4 ;
	private BigDecimal	r41_amt_name_of_sub5 ;
	private BigDecimal	r41_tot_amt ;
		
	private String	r42_item ;
	private BigDecimal	r42_amt_name_of_sub1 ;
	private BigDecimal	r42_amt_name_of_sub2 ;
	private BigDecimal	r42_amt_name_of_sub3 ;
	private BigDecimal	r42_amt_name_of_sub4 ;
	private BigDecimal	r42_amt_name_of_sub5 ;
	private BigDecimal	r42_tot_amt ;
		
	private String	r43_item ;
	private BigDecimal	r43_amt_name_of_sub1 ;
	private BigDecimal	r43_amt_name_of_sub2 ;
	private BigDecimal	r43_amt_name_of_sub3 ;
	private BigDecimal	r43_amt_name_of_sub4 ;
	private BigDecimal	r43_amt_name_of_sub5 ;
	private BigDecimal	r43_tot_amt ;
		
	private String	r44_item ;
	private BigDecimal	r44_amt_name_of_sub1 ;
	private BigDecimal	r44_amt_name_of_sub2 ;
	private BigDecimal	r44_amt_name_of_sub3 ;
	private BigDecimal	r44_amt_name_of_sub4 ;
	private BigDecimal	r44_amt_name_of_sub5 ;
	private BigDecimal	r44_tot_amt ;
		
	private String	r45_item ;
	private BigDecimal	r45_amt_name_of_sub1 ;
	private BigDecimal	r45_amt_name_of_sub2 ;
	private BigDecimal	r45_amt_name_of_sub3 ;
	private BigDecimal	r45_amt_name_of_sub4 ;
	private BigDecimal	r45_amt_name_of_sub5 ;
	private BigDecimal	r45_tot_amt ;
		
	private String	r46_item ;
	private BigDecimal	r46_amt_name_of_sub1 ;
	private BigDecimal	r46_amt_name_of_sub2 ;
	private BigDecimal	r46_amt_name_of_sub3 ;
	private BigDecimal	r46_amt_name_of_sub4 ;
	private BigDecimal	r46_amt_name_of_sub5 ;
	private BigDecimal	r46_tot_amt ;
		
	private String	r47_item ;
	private BigDecimal	r47_amt_name_of_sub1 ;
	private BigDecimal	r47_amt_name_of_sub2 ;
	private BigDecimal	r47_amt_name_of_sub3 ;
	private BigDecimal	r47_amt_name_of_sub4 ;
	private BigDecimal	r47_amt_name_of_sub5 ;
	private BigDecimal	r47_tot_amt ;
		
	private String	r48_item ;
	private BigDecimal	r48_amt_name_of_sub1 ;
	private BigDecimal	r48_amt_name_of_sub2 ;
	private BigDecimal	r48_amt_name_of_sub3 ;
	private BigDecimal	r48_amt_name_of_sub4 ;
	private BigDecimal	r48_amt_name_of_sub5 ;
	private BigDecimal	r48_tot_amt ;
		
	private String	r49_item ;
	private BigDecimal	r49_amt_name_of_sub1;
	private BigDecimal	r49_amt_name_of_sub2;
	private BigDecimal	r49_amt_name_of_sub3;
	private BigDecimal	r49_amt_name_of_sub4;
	private BigDecimal	r49_amt_name_of_sub5 ;
	private BigDecimal	r49_tot_amt ;
	private String	r50_item ;
	private BigDecimal	r50_amt_name_of_sub1 ;
	private BigDecimal	r50_amt_name_of_sub2 ;
	private BigDecimal	r50_amt_name_of_sub3 ;
	private BigDecimal	r50_amt_name_of_sub4 ;
	private BigDecimal	r50_amt_name_of_sub5 ;
	private BigDecimal	r50_tot_amt ;
		
	private String	r51_item ;
	private BigDecimal	r51_amt_name_of_sub1 ;
	private BigDecimal	r51_amt_name_of_sub2 ;
	private BigDecimal	r51_amt_name_of_sub3 ;
	private BigDecimal	r51_amt_name_of_sub4 ;
	private BigDecimal	r51_amt_name_of_sub5 ;
	private BigDecimal	r51_tot_amt ;
		
	private String	r52_item ;
	private BigDecimal	r52_amt_name_of_sub1 ;
	private BigDecimal	r52_amt_name_of_sub2 ;
	private BigDecimal	r52_amt_name_of_sub3 ;
	private BigDecimal	r52_amt_name_of_sub4 ;
	private BigDecimal	r52_amt_name_of_sub5 ;
	private BigDecimal	r52_tot_amt ;
		
	private String	r53_item ;
	private BigDecimal	r53_amt_name_of_sub1;
	private BigDecimal	r53_amt_name_of_sub2;
	private BigDecimal	r53_amt_name_of_sub3;
	private BigDecimal	r53_amt_name_of_sub4;
	private BigDecimal	r53_amt_name_of_sub5 ;
	private BigDecimal	r53_tot_amt ;
	private String	r54_item ;
	private BigDecimal	r54_amt_name_of_sub1 ;
	private BigDecimal	r54_amt_name_of_sub2 ;
	private BigDecimal	r54_amt_name_of_sub3 ;
	private BigDecimal	r54_amt_name_of_sub4 ;
	private BigDecimal	r54_amt_name_of_sub5 ;
	private BigDecimal	r54_tot_amt ;
		
	private String	r55_item ;
	private BigDecimal	r55_amt_name_of_sub1 ;
	private BigDecimal	r55_amt_name_of_sub2 ;
	private BigDecimal	r55_amt_name_of_sub3 ;
	private BigDecimal	r55_amt_name_of_sub4 ;
	private BigDecimal	r55_amt_name_of_sub5 ;
	private BigDecimal	r55_tot_amt ;
		
	private String	r56_item ;
	private BigDecimal	r56_amt_name_of_sub1 ;
	private BigDecimal	r56_amt_name_of_sub2 ;
	private BigDecimal	r56_amt_name_of_sub3 ;
	private BigDecimal	r56_amt_name_of_sub4 ;
	private BigDecimal	r56_amt_name_of_sub5 ;
	private BigDecimal	r56_tot_amt ;
		
	private String	r57_item ;
	private BigDecimal	r57_amt_name_of_sub1 ;
	private BigDecimal	r57_amt_name_of_sub2 ;
	private BigDecimal	r57_amt_name_of_sub3 ;
	private BigDecimal	r57_amt_name_of_sub4 ;
	private BigDecimal	r57_amt_name_of_sub5 ;
	private BigDecimal	r57_tot_amt ;
		
	private String	r58_item ;
	private BigDecimal	r58_amt_name_of_sub1;
	private BigDecimal	r58_amt_name_of_sub2;
	private BigDecimal	r58_amt_name_of_sub3;
	private BigDecimal	r58_amt_name_of_sub4;
	private BigDecimal	r58_amt_name_of_sub5;
	private BigDecimal	r58_tot_amt ;
		
	
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
	
	
	public String getR10_item() {
		return r10_item;
	}
	public void setR10_item(String r10_item) {
		this.r10_item = r10_item;
	}
	public BigDecimal getR10_amt_name_of_sub1() {
		return r10_amt_name_of_sub1;
	}
	public void setR10_amt_name_of_sub1(BigDecimal r10_amt_name_of_sub1) {
		this.r10_amt_name_of_sub1 = r10_amt_name_of_sub1;
	}
	public BigDecimal getR10_amt_name_of_sub2() {
		return r10_amt_name_of_sub2;
	}
	public void setR10_amt_name_of_sub2(BigDecimal r10_amt_name_of_sub2) {
		this.r10_amt_name_of_sub2 = r10_amt_name_of_sub2;
	}
	public BigDecimal getR10_amt_name_of_sub3() {
		return r10_amt_name_of_sub3;
	}
	public void setR10_amt_name_of_sub3(BigDecimal r10_amt_name_of_sub3) {
		this.r10_amt_name_of_sub3 = r10_amt_name_of_sub3;
	}
	public BigDecimal getR10_amt_name_of_sub4() {
		return r10_amt_name_of_sub4;
	}
	public void setR10_amt_name_of_sub4(BigDecimal r10_amt_name_of_sub4) {
		this.r10_amt_name_of_sub4 = r10_amt_name_of_sub4;
	}
	public BigDecimal getR10_amt_name_of_sub5() {
		return r10_amt_name_of_sub5;
	}
	public void setR10_amt_name_of_sub5(BigDecimal r10_amt_name_of_sub5) {
		this.r10_amt_name_of_sub5 = r10_amt_name_of_sub5;
	}
	public BigDecimal getR10_tot_amt() {
		return r10_tot_amt;
	}
	public void setR10_tot_amt(BigDecimal r10_tot_amt) {
		this.r10_tot_amt = r10_tot_amt;
	}
	public String getR11_item() {
		return r11_item;
	}
	public void setR11_item(String r11_item) {
		this.r11_item = r11_item;
	}
	public BigDecimal getR11_amt_name_of_sub1() {
		return r11_amt_name_of_sub1;
	}
	public void setR11_amt_name_of_sub1(BigDecimal r11_amt_name_of_sub1) {
		this.r11_amt_name_of_sub1 = r11_amt_name_of_sub1;
	}
	public BigDecimal getR11_amt_name_of_sub2() {
		return r11_amt_name_of_sub2;
	}
	public void setR11_amt_name_of_sub2(BigDecimal r11_amt_name_of_sub2) {
		this.r11_amt_name_of_sub2 = r11_amt_name_of_sub2;
	}
	public BigDecimal getR11_amt_name_of_sub3() {
		return r11_amt_name_of_sub3;
	}
	public void setR11_amt_name_of_sub3(BigDecimal r11_amt_name_of_sub3) {
		this.r11_amt_name_of_sub3 = r11_amt_name_of_sub3;
	}
	public BigDecimal getR11_amt_name_of_sub4() {
		return r11_amt_name_of_sub4;
	}
	public void setR11_amt_name_of_sub4(BigDecimal r11_amt_name_of_sub4) {
		this.r11_amt_name_of_sub4 = r11_amt_name_of_sub4;
	}
	public BigDecimal getR11_amt_name_of_sub5() {
		return r11_amt_name_of_sub5;
	}
	public void setR11_amt_name_of_sub5(BigDecimal r11_amt_name_of_sub5) {
		this.r11_amt_name_of_sub5 = r11_amt_name_of_sub5;
	}
	public BigDecimal getR11_tot_amt() {
		return r11_tot_amt;
	}
	public void setR11_tot_amt(BigDecimal r11_tot_amt) {
		this.r11_tot_amt = r11_tot_amt;
	}
	public String getR12_item() {
		return r12_item;
	}
	public void setR12_item(String r12_item) {
		this.r12_item = r12_item;
	}
	public BigDecimal getR12_amt_name_of_sub1() {
		return r12_amt_name_of_sub1;
	}
	public void setR12_amt_name_of_sub1(BigDecimal r12_amt_name_of_sub1) {
		this.r12_amt_name_of_sub1 = r12_amt_name_of_sub1;
	}
	public BigDecimal getR12_amt_name_of_sub2() {
		return r12_amt_name_of_sub2;
	}
	public void setR12_amt_name_of_sub2(BigDecimal r12_amt_name_of_sub2) {
		this.r12_amt_name_of_sub2 = r12_amt_name_of_sub2;
	}
	public BigDecimal getR12_amt_name_of_sub3() {
		return r12_amt_name_of_sub3;
	}
	public void setR12_amt_name_of_sub3(BigDecimal r12_amt_name_of_sub3) {
		this.r12_amt_name_of_sub3 = r12_amt_name_of_sub3;
	}
	public BigDecimal getR12_amt_name_of_sub4() {
		return r12_amt_name_of_sub4;
	}
	public void setR12_amt_name_of_sub4(BigDecimal r12_amt_name_of_sub4) {
		this.r12_amt_name_of_sub4 = r12_amt_name_of_sub4;
	}
	public BigDecimal getR12_amt_name_of_sub5() {
		return r12_amt_name_of_sub5;
	}
	public void setR12_amt_name_of_sub5(BigDecimal r12_amt_name_of_sub5) {
		this.r12_amt_name_of_sub5 = r12_amt_name_of_sub5;
	}
	public BigDecimal getR12_tot_amt() {
		return r12_tot_amt;
	}
	public void setR12_tot_amt(BigDecimal r12_tot_amt) {
		this.r12_tot_amt = r12_tot_amt;
	}
	public String getR13_item() {
		return r13_item;
	}
	public void setR13_item(String r13_item) {
		this.r13_item = r13_item;
	}
	public BigDecimal getR13_amt_name_of_sub1() {
		return r13_amt_name_of_sub1;
	}
	public void setR13_amt_name_of_sub1(BigDecimal r13_amt_name_of_sub1) {
		this.r13_amt_name_of_sub1 = r13_amt_name_of_sub1;
	}
	public BigDecimal getR13_amt_name_of_sub2() {
		return r13_amt_name_of_sub2;
	}
	public void setR13_amt_name_of_sub2(BigDecimal r13_amt_name_of_sub2) {
		this.r13_amt_name_of_sub2 = r13_amt_name_of_sub2;
	}
	public BigDecimal getR13_amt_name_of_sub3() {
		return r13_amt_name_of_sub3;
	}
	public void setR13_amt_name_of_sub3(BigDecimal r13_amt_name_of_sub3) {
		this.r13_amt_name_of_sub3 = r13_amt_name_of_sub3;
	}
	public BigDecimal getR13_amt_name_of_sub4() {
		return r13_amt_name_of_sub4;
	}
	public void setR13_amt_name_of_sub4(BigDecimal r13_amt_name_of_sub4) {
		this.r13_amt_name_of_sub4 = r13_amt_name_of_sub4;
	}
	public BigDecimal getR13_amt_name_of_sub5() {
		return r13_amt_name_of_sub5;
	}
	public void setR13_amt_name_of_sub5(BigDecimal r13_amt_name_of_sub5) {
		this.r13_amt_name_of_sub5 = r13_amt_name_of_sub5;
	}
	public BigDecimal getR13_tot_amt() {
		return r13_tot_amt;
	}
	public void setR13_tot_amt(BigDecimal r13_tot_amt) {
		this.r13_tot_amt = r13_tot_amt;
	}
	public String getR14_item() {
		return r14_item;
	}
	public void setR14_item(String r14_item) {
		this.r14_item = r14_item;
	}
	public BigDecimal getR14_amt_name_of_sub1() {
		return r14_amt_name_of_sub1;
	}
	public void setR14_amt_name_of_sub1(BigDecimal r14_amt_name_of_sub1) {
		this.r14_amt_name_of_sub1 = r14_amt_name_of_sub1;
	}
	public BigDecimal getR14_amt_name_of_sub2() {
		return r14_amt_name_of_sub2;
	}
	public void setR14_amt_name_of_sub2(BigDecimal r14_amt_name_of_sub2) {
		this.r14_amt_name_of_sub2 = r14_amt_name_of_sub2;
	}
	public BigDecimal getR14_amt_name_of_sub3() {
		return r14_amt_name_of_sub3;
	}
	public void setR14_amt_name_of_sub3(BigDecimal r14_amt_name_of_sub3) {
		this.r14_amt_name_of_sub3 = r14_amt_name_of_sub3;
	}
	public BigDecimal getR14_amt_name_of_sub4() {
		return r14_amt_name_of_sub4;
	}
	public void setR14_amt_name_of_sub4(BigDecimal r14_amt_name_of_sub4) {
		this.r14_amt_name_of_sub4 = r14_amt_name_of_sub4;
	}
	public BigDecimal getR14_amt_name_of_sub5() {
		return r14_amt_name_of_sub5;
	}
	public void setR14_amt_name_of_sub5(BigDecimal r14_amt_name_of_sub5) {
		this.r14_amt_name_of_sub5 = r14_amt_name_of_sub5;
	}
	public BigDecimal getR14_tot_amt() {
		return r14_tot_amt;
	}
	public void setR14_tot_amt(BigDecimal r14_tot_amt) {
		this.r14_tot_amt = r14_tot_amt;
	}
	public String getR17_item() {
		return r17_item;
	}
	public void setR17_item(String r17_item) {
		this.r17_item = r17_item;
	}
	public BigDecimal getR17_amt_name_of_sub1() {
		return r17_amt_name_of_sub1;
	}
	public void setR17_amt_name_of_sub1(BigDecimal r17_amt_name_of_sub1) {
		this.r17_amt_name_of_sub1 = r17_amt_name_of_sub1;
	}
	public BigDecimal getR17_amt_name_of_sub2() {
		return r17_amt_name_of_sub2;
	}
	public void setR17_amt_name_of_sub2(BigDecimal r17_amt_name_of_sub2) {
		this.r17_amt_name_of_sub2 = r17_amt_name_of_sub2;
	}
	public BigDecimal getR17_amt_name_of_sub3() {
		return r17_amt_name_of_sub3;
	}
	public void setR17_amt_name_of_sub3(BigDecimal r17_amt_name_of_sub3) {
		this.r17_amt_name_of_sub3 = r17_amt_name_of_sub3;
	}
	public BigDecimal getR17_amt_name_of_sub4() {
		return r17_amt_name_of_sub4;
	}
	public void setR17_amt_name_of_sub4(BigDecimal r17_amt_name_of_sub4) {
		this.r17_amt_name_of_sub4 = r17_amt_name_of_sub4;
	}
	public BigDecimal getR17_amt_name_of_sub5() {
		return r17_amt_name_of_sub5;
	}
	public void setR17_amt_name_of_sub5(BigDecimal r17_amt_name_of_sub5) {
		this.r17_amt_name_of_sub5 = r17_amt_name_of_sub5;
	}
	public BigDecimal getR17_tot_amt() {
		return r17_tot_amt;
	}
	public void setR17_tot_amt(BigDecimal r17_tot_amt) {
		this.r17_tot_amt = r17_tot_amt;
	}
	public String getR18_item() {
		return r18_item;
	}
	public void setR18_item(String r18_item) {
		this.r18_item = r18_item;
	}
	public BigDecimal getR18_amt_name_of_sub1() {
		return r18_amt_name_of_sub1;
	}
	public void setR18_amt_name_of_sub1(BigDecimal r18_amt_name_of_sub1) {
		this.r18_amt_name_of_sub1 = r18_amt_name_of_sub1;
	}
	public BigDecimal getR18_amt_name_of_sub2() {
		return r18_amt_name_of_sub2;
	}
	public void setR18_amt_name_of_sub2(BigDecimal r18_amt_name_of_sub2) {
		this.r18_amt_name_of_sub2 = r18_amt_name_of_sub2;
	}
	public BigDecimal getR18_amt_name_of_sub3() {
		return r18_amt_name_of_sub3;
	}
	public void setR18_amt_name_of_sub3(BigDecimal r18_amt_name_of_sub3) {
		this.r18_amt_name_of_sub3 = r18_amt_name_of_sub3;
	}
	public BigDecimal getR18_amt_name_of_sub4() {
		return r18_amt_name_of_sub4;
	}
	public void setR18_amt_name_of_sub4(BigDecimal r18_amt_name_of_sub4) {
		this.r18_amt_name_of_sub4 = r18_amt_name_of_sub4;
	}
	public BigDecimal getR18_amt_name_of_sub5() {
		return r18_amt_name_of_sub5;
	}
	public void setR18_amt_name_of_sub5(BigDecimal r18_amt_name_of_sub5) {
		this.r18_amt_name_of_sub5 = r18_amt_name_of_sub5;
	}
	public BigDecimal getR18_tot_amt() {
		return r18_tot_amt;
	}
	public void setR18_tot_amt(BigDecimal r18_tot_amt) {
		this.r18_tot_amt = r18_tot_amt;
	}
	public String getR19_item() {
		return r19_item;
	}
	public void setR19_item(String r19_item) {
		this.r19_item = r19_item;
	}
	public BigDecimal getR19_amt_name_of_sub1() {
		return r19_amt_name_of_sub1;
	}
	public void setR19_amt_name_of_sub1(BigDecimal r19_amt_name_of_sub1) {
		this.r19_amt_name_of_sub1 = r19_amt_name_of_sub1;
	}
	public BigDecimal getR19_amt_name_of_sub2() {
		return r19_amt_name_of_sub2;
	}
	public void setR19_amt_name_of_sub2(BigDecimal r19_amt_name_of_sub2) {
		this.r19_amt_name_of_sub2 = r19_amt_name_of_sub2;
	}
	public BigDecimal getR19_amt_name_of_sub3() {
		return r19_amt_name_of_sub3;
	}
	public void setR19_amt_name_of_sub3(BigDecimal r19_amt_name_of_sub3) {
		this.r19_amt_name_of_sub3 = r19_amt_name_of_sub3;
	}
	public BigDecimal getR19_amt_name_of_sub4() {
		return r19_amt_name_of_sub4;
	}
	public void setR19_amt_name_of_sub4(BigDecimal r19_amt_name_of_sub4) {
		this.r19_amt_name_of_sub4 = r19_amt_name_of_sub4;
	}
	public BigDecimal getR19_amt_name_of_sub5() {
		return r19_amt_name_of_sub5;
	}
	public void setR19_amt_name_of_sub5(BigDecimal r19_amt_name_of_sub5) {
		this.r19_amt_name_of_sub5 = r19_amt_name_of_sub5;
	}
	public BigDecimal getR19_tot_amt() {
		return r19_tot_amt;
	}
	public void setR19_tot_amt(BigDecimal r19_tot_amt) {
		this.r19_tot_amt = r19_tot_amt;
	}
	public String getR22_item() {
		return r22_item;
	}
	public void setR22_item(String r22_item) {
		this.r22_item = r22_item;
	}
	public BigDecimal getR22_amt_name_of_sub1() {
		return r22_amt_name_of_sub1;
	}
	public void setR22_amt_name_of_sub1(BigDecimal r22_amt_name_of_sub1) {
		this.r22_amt_name_of_sub1 = r22_amt_name_of_sub1;
	}
	public BigDecimal getR22_amt_name_of_sub2() {
		return r22_amt_name_of_sub2;
	}
	public void setR22_amt_name_of_sub2(BigDecimal r22_amt_name_of_sub2) {
		this.r22_amt_name_of_sub2 = r22_amt_name_of_sub2;
	}
	public BigDecimal getR22_amt_name_of_sub3() {
		return r22_amt_name_of_sub3;
	}
	public void setR22_amt_name_of_sub3(BigDecimal r22_amt_name_of_sub3) {
		this.r22_amt_name_of_sub3 = r22_amt_name_of_sub3;
	}
	public BigDecimal getR22_amt_name_of_sub4() {
		return r22_amt_name_of_sub4;
	}
	public void setR22_amt_name_of_sub4(BigDecimal r22_amt_name_of_sub4) {
		this.r22_amt_name_of_sub4 = r22_amt_name_of_sub4;
	}
	public BigDecimal getR22_amt_name_of_sub5() {
		return r22_amt_name_of_sub5;
	}
	public void setR22_amt_name_of_sub5(BigDecimal r22_amt_name_of_sub5) {
		this.r22_amt_name_of_sub5 = r22_amt_name_of_sub5;
	}
	public BigDecimal getR22_tot_amt() {
		return r22_tot_amt;
	}
	public void setR22_tot_amt(BigDecimal r22_tot_amt) {
		this.r22_tot_amt = r22_tot_amt;
	}
	public String getR23_item() {
		return r23_item;
	}
	public void setR23_item(String r23_item) {
		this.r23_item = r23_item;
	}
	public BigDecimal getR23_amt_name_of_sub1() {
		return r23_amt_name_of_sub1;
	}
	public void setR23_amt_name_of_sub1(BigDecimal r23_amt_name_of_sub1) {
		this.r23_amt_name_of_sub1 = r23_amt_name_of_sub1;
	}
	public BigDecimal getR23_amt_name_of_sub2() {
		return r23_amt_name_of_sub2;
	}
	public void setR23_amt_name_of_sub2(BigDecimal r23_amt_name_of_sub2) {
		this.r23_amt_name_of_sub2 = r23_amt_name_of_sub2;
	}
	public BigDecimal getR23_amt_name_of_sub3() {
		return r23_amt_name_of_sub3;
	}
	public void setR23_amt_name_of_sub3(BigDecimal r23_amt_name_of_sub3) {
		this.r23_amt_name_of_sub3 = r23_amt_name_of_sub3;
	}
	public BigDecimal getR23_amt_name_of_sub4() {
		return r23_amt_name_of_sub4;
	}
	public void setR23_amt_name_of_sub4(BigDecimal r23_amt_name_of_sub4) {
		this.r23_amt_name_of_sub4 = r23_amt_name_of_sub4;
	}
	public BigDecimal getR23_amt_name_of_sub5() {
		return r23_amt_name_of_sub5;
	}
	public void setR23_amt_name_of_sub5(BigDecimal r23_amt_name_of_sub5) {
		this.r23_amt_name_of_sub5 = r23_amt_name_of_sub5;
	}
	public BigDecimal getR23_tot_amt() {
		return r23_tot_amt;
	}
	public void setR23_tot_amt(BigDecimal r23_tot_amt) {
		this.r23_tot_amt = r23_tot_amt;
	}
	public String getR24_item() {
		return r24_item;
	}
	public void setR24_item(String r24_item) {
		this.r24_item = r24_item;
	}
	public BigDecimal getR24_amt_name_of_sub1() {
		return r24_amt_name_of_sub1;
	}
	public void setR24_amt_name_of_sub1(BigDecimal r24_amt_name_of_sub1) {
		this.r24_amt_name_of_sub1 = r24_amt_name_of_sub1;
	}
	public BigDecimal getR24_amt_name_of_sub2() {
		return r24_amt_name_of_sub2;
	}
	public void setR24_amt_name_of_sub2(BigDecimal r24_amt_name_of_sub2) {
		this.r24_amt_name_of_sub2 = r24_amt_name_of_sub2;
	}
	public BigDecimal getR24_amt_name_of_sub3() {
		return r24_amt_name_of_sub3;
	}
	public void setR24_amt_name_of_sub3(BigDecimal r24_amt_name_of_sub3) {
		this.r24_amt_name_of_sub3 = r24_amt_name_of_sub3;
	}
	public BigDecimal getR24_amt_name_of_sub4() {
		return r24_amt_name_of_sub4;
	}
	public void setR24_amt_name_of_sub4(BigDecimal r24_amt_name_of_sub4) {
		this.r24_amt_name_of_sub4 = r24_amt_name_of_sub4;
	}
	public BigDecimal getR24_amt_name_of_sub5() {
		return r24_amt_name_of_sub5;
	}
	public void setR24_amt_name_of_sub5(BigDecimal r24_amt_name_of_sub5) {
		this.r24_amt_name_of_sub5 = r24_amt_name_of_sub5;
	}
	public BigDecimal getR24_tot_amt() {
		return r24_tot_amt;
	}
	public void setR24_tot_amt(BigDecimal r24_tot_amt) {
		this.r24_tot_amt = r24_tot_amt;
	}
	public String getR27_item() {
		return r27_item;
	}
	public void setR27_item(String r27_item) {
		this.r27_item = r27_item;
	}
	public BigDecimal getR27_amt_name_of_sub1() {
		return r27_amt_name_of_sub1;
	}
	public void setR27_amt_name_of_sub1(BigDecimal r27_amt_name_of_sub1) {
		this.r27_amt_name_of_sub1 = r27_amt_name_of_sub1;
	}
	public BigDecimal getR27_amt_name_of_sub2() {
		return r27_amt_name_of_sub2;
	}
	public void setR27_amt_name_of_sub2(BigDecimal r27_amt_name_of_sub2) {
		this.r27_amt_name_of_sub2 = r27_amt_name_of_sub2;
	}
	public BigDecimal getR27_amt_name_of_sub3() {
		return r27_amt_name_of_sub3;
	}
	public void setR27_amt_name_of_sub3(BigDecimal r27_amt_name_of_sub3) {
		this.r27_amt_name_of_sub3 = r27_amt_name_of_sub3;
	}
	public BigDecimal getR27_amt_name_of_sub4() {
		return r27_amt_name_of_sub4;
	}
	public void setR27_amt_name_of_sub4(BigDecimal r27_amt_name_of_sub4) {
		this.r27_amt_name_of_sub4 = r27_amt_name_of_sub4;
	}
	public BigDecimal getR27_amt_name_of_sub5() {
		return r27_amt_name_of_sub5;
	}
	public void setR27_amt_name_of_sub5(BigDecimal r27_amt_name_of_sub5) {
		this.r27_amt_name_of_sub5 = r27_amt_name_of_sub5;
	}
	public BigDecimal getR27_tot_amt() {
		return r27_tot_amt;
	}
	public void setR27_tot_amt(BigDecimal r27_tot_amt) {
		this.r27_tot_amt = r27_tot_amt;
	}
	public String getR28_item() {
		return r28_item;
	}
	public void setR28_item(String r28_item) {
		this.r28_item = r28_item;
	}
	public BigDecimal getR28_amt_name_of_sub1() {
		return r28_amt_name_of_sub1;
	}
	public void setR28_amt_name_of_sub1(BigDecimal r28_amt_name_of_sub1) {
		this.r28_amt_name_of_sub1 = r28_amt_name_of_sub1;
	}
	public BigDecimal getR28_amt_name_of_sub2() {
		return r28_amt_name_of_sub2;
	}
	public void setR28_amt_name_of_sub2(BigDecimal r28_amt_name_of_sub2) {
		this.r28_amt_name_of_sub2 = r28_amt_name_of_sub2;
	}
	public BigDecimal getR28_amt_name_of_sub3() {
		return r28_amt_name_of_sub3;
	}
	public void setR28_amt_name_of_sub3(BigDecimal r28_amt_name_of_sub3) {
		this.r28_amt_name_of_sub3 = r28_amt_name_of_sub3;
	}
	public BigDecimal getR28_amt_name_of_sub4() {
		return r28_amt_name_of_sub4;
	}
	public void setR28_amt_name_of_sub4(BigDecimal r28_amt_name_of_sub4) {
		this.r28_amt_name_of_sub4 = r28_amt_name_of_sub4;
	}
	public BigDecimal getR28_amt_name_of_sub5() {
		return r28_amt_name_of_sub5;
	}
	public void setR28_amt_name_of_sub5(BigDecimal r28_amt_name_of_sub5) {
		this.r28_amt_name_of_sub5 = r28_amt_name_of_sub5;
	}
	public BigDecimal getR28_tot_amt() {
		return r28_tot_amt;
	}
	public void setR28_tot_amt(BigDecimal r28_tot_amt) {
		this.r28_tot_amt = r28_tot_amt;
	}
	public String getR29_item() {
		return r29_item;
	}
	public void setR29_item(String r29_item) {
		this.r29_item = r29_item;
	}
	public BigDecimal getR29_amt_name_of_sub1() {
		return r29_amt_name_of_sub1;
	}
	public void setR29_amt_name_of_sub1(BigDecimal r29_amt_name_of_sub1) {
		this.r29_amt_name_of_sub1 = r29_amt_name_of_sub1;
	}
	public BigDecimal getR29_amt_name_of_sub2() {
		return r29_amt_name_of_sub2;
	}
	public void setR29_amt_name_of_sub2(BigDecimal r29_amt_name_of_sub2) {
		this.r29_amt_name_of_sub2 = r29_amt_name_of_sub2;
	}
	public BigDecimal getR29_amt_name_of_sub3() {
		return r29_amt_name_of_sub3;
	}
	public void setR29_amt_name_of_sub3(BigDecimal r29_amt_name_of_sub3) {
		this.r29_amt_name_of_sub3 = r29_amt_name_of_sub3;
	}
	public BigDecimal getR29_amt_name_of_sub4() {
		return r29_amt_name_of_sub4;
	}
	public void setR29_amt_name_of_sub4(BigDecimal r29_amt_name_of_sub4) {
		this.r29_amt_name_of_sub4 = r29_amt_name_of_sub4;
	}
	public BigDecimal getR29_amt_name_of_sub5() {
		return r29_amt_name_of_sub5;
	}
	public void setR29_amt_name_of_sub5(BigDecimal r29_amt_name_of_sub5) {
		this.r29_amt_name_of_sub5 = r29_amt_name_of_sub5;
	}
	public BigDecimal getR29_tot_amt() {
		return r29_tot_amt;
	}
	public void setR29_tot_amt(BigDecimal r29_tot_amt) {
		this.r29_tot_amt = r29_tot_amt;
	}
	public String getR32_item() {
		return r32_item;
	}
	public void setR32_item(String r32_item) {
		this.r32_item = r32_item;
	}
	public BigDecimal getR32_amt_name_of_sub1() {
		return r32_amt_name_of_sub1;
	}
	public void setR32_amt_name_of_sub1(BigDecimal r32_amt_name_of_sub1) {
		this.r32_amt_name_of_sub1 = r32_amt_name_of_sub1;
	}
	public BigDecimal getR32_amt_name_of_sub2() {
		return r32_amt_name_of_sub2;
	}
	public void setR32_amt_name_of_sub2(BigDecimal r32_amt_name_of_sub2) {
		this.r32_amt_name_of_sub2 = r32_amt_name_of_sub2;
	}
	public BigDecimal getR32_amt_name_of_sub3() {
		return r32_amt_name_of_sub3;
	}
	public void setR32_amt_name_of_sub3(BigDecimal r32_amt_name_of_sub3) {
		this.r32_amt_name_of_sub3 = r32_amt_name_of_sub3;
	}
	public BigDecimal getR32_amt_name_of_sub4() {
		return r32_amt_name_of_sub4;
	}
	public void setR32_amt_name_of_sub4(BigDecimal r32_amt_name_of_sub4) {
		this.r32_amt_name_of_sub4 = r32_amt_name_of_sub4;
	}
	public BigDecimal getR32_amt_name_of_sub5() {
		return r32_amt_name_of_sub5;
	}
	public void setR32_amt_name_of_sub5(BigDecimal r32_amt_name_of_sub5) {
		this.r32_amt_name_of_sub5 = r32_amt_name_of_sub5;
	}
	public BigDecimal getR32_tot_amt() {
		return r32_tot_amt;
	}
	public void setR32_tot_amt(BigDecimal r32_tot_amt) {
		this.r32_tot_amt = r32_tot_amt;
	}
	public String getR33_item() {
		return r33_item;
	}
	public void setR33_item(String r33_item) {
		this.r33_item = r33_item;
	}
	public BigDecimal getR33_amt_name_of_sub1() {
		return r33_amt_name_of_sub1;
	}
	public void setR33_amt_name_of_sub1(BigDecimal r33_amt_name_of_sub1) {
		this.r33_amt_name_of_sub1 = r33_amt_name_of_sub1;
	}
	public BigDecimal getR33_amt_name_of_sub2() {
		return r33_amt_name_of_sub2;
	}
	public void setR33_amt_name_of_sub2(BigDecimal r33_amt_name_of_sub2) {
		this.r33_amt_name_of_sub2 = r33_amt_name_of_sub2;
	}
	public BigDecimal getR33_amt_name_of_sub3() {
		return r33_amt_name_of_sub3;
	}
	public void setR33_amt_name_of_sub3(BigDecimal r33_amt_name_of_sub3) {
		this.r33_amt_name_of_sub3 = r33_amt_name_of_sub3;
	}
	public BigDecimal getR33_amt_name_of_sub4() {
		return r33_amt_name_of_sub4;
	}
	public void setR33_amt_name_of_sub4(BigDecimal r33_amt_name_of_sub4) {
		this.r33_amt_name_of_sub4 = r33_amt_name_of_sub4;
	}
	public BigDecimal getR33_amt_name_of_sub5() {
		return r33_amt_name_of_sub5;
	}
	public void setR33_amt_name_of_sub5(BigDecimal r33_amt_name_of_sub5) {
		this.r33_amt_name_of_sub5 = r33_amt_name_of_sub5;
	}
	public BigDecimal getR33_tot_amt() {
		return r33_tot_amt;
	}
	public void setR33_tot_amt(BigDecimal r33_tot_amt) {
		this.r33_tot_amt = r33_tot_amt;
	}
	public String getR34_item() {
		return r34_item;
	}
	public void setR34_item(String r34_item) {
		this.r34_item = r34_item;
	}
	public BigDecimal getR34_amt_name_of_sub1() {
		return r34_amt_name_of_sub1;
	}
	public void setR34_amt_name_of_sub1(BigDecimal r34_amt_name_of_sub1) {
		this.r34_amt_name_of_sub1 = r34_amt_name_of_sub1;
	}
	public BigDecimal getR34_amt_name_of_sub2() {
		return r34_amt_name_of_sub2;
	}
	public void setR34_amt_name_of_sub2(BigDecimal r34_amt_name_of_sub2) {
		this.r34_amt_name_of_sub2 = r34_amt_name_of_sub2;
	}
	public BigDecimal getR34_amt_name_of_sub3() {
		return r34_amt_name_of_sub3;
	}
	public void setR34_amt_name_of_sub3(BigDecimal r34_amt_name_of_sub3) {
		this.r34_amt_name_of_sub3 = r34_amt_name_of_sub3;
	}
	public BigDecimal getR34_amt_name_of_sub4() {
		return r34_amt_name_of_sub4;
	}
	public void setR34_amt_name_of_sub4(BigDecimal r34_amt_name_of_sub4) {
		this.r34_amt_name_of_sub4 = r34_amt_name_of_sub4;
	}
	public BigDecimal getR34_amt_name_of_sub5() {
		return r34_amt_name_of_sub5;
	}
	public void setR34_amt_name_of_sub5(BigDecimal r34_amt_name_of_sub5) {
		this.r34_amt_name_of_sub5 = r34_amt_name_of_sub5;
	}
	public BigDecimal getR34_tot_amt() {
		return r34_tot_amt;
	}
	public void setR34_tot_amt(BigDecimal r34_tot_amt) {
		this.r34_tot_amt = r34_tot_amt;
	}
	public String getR36_item() {
		return r36_item;
	}
	public void setR36_item(String r36_item) {
		this.r36_item = r36_item;
	}
	public BigDecimal getR36_amt_name_of_sub1() {
		return r36_amt_name_of_sub1;
	}
	public void setR36_amt_name_of_sub1(BigDecimal r36_amt_name_of_sub1) {
		this.r36_amt_name_of_sub1 = r36_amt_name_of_sub1;
	}
	public BigDecimal getR36_amt_name_of_sub2() {
		return r36_amt_name_of_sub2;
	}
	public void setR36_amt_name_of_sub2(BigDecimal r36_amt_name_of_sub2) {
		this.r36_amt_name_of_sub2 = r36_amt_name_of_sub2;
	}
	public BigDecimal getR36_amt_name_of_sub3() {
		return r36_amt_name_of_sub3;
	}
	public void setR36_amt_name_of_sub3(BigDecimal r36_amt_name_of_sub3) {
		this.r36_amt_name_of_sub3 = r36_amt_name_of_sub3;
	}
	public BigDecimal getR36_amt_name_of_sub4() {
		return r36_amt_name_of_sub4;
	}
	public void setR36_amt_name_of_sub4(BigDecimal r36_amt_name_of_sub4) {
		this.r36_amt_name_of_sub4 = r36_amt_name_of_sub4;
	}
	public BigDecimal getR36_amt_name_of_sub5() {
		return r36_amt_name_of_sub5;
	}
	public void setR36_amt_name_of_sub5(BigDecimal r36_amt_name_of_sub5) {
		this.r36_amt_name_of_sub5 = r36_amt_name_of_sub5;
	}
	public BigDecimal getR36_tot_amt() {
		return r36_tot_amt;
	}
	public void setR36_tot_amt(BigDecimal r36_tot_amt) {
		this.r36_tot_amt = r36_tot_amt;
	}
	public String getR37_item() {
		return r37_item;
	}
	public void setR37_item(String r37_item) {
		this.r37_item = r37_item;
	}
	public BigDecimal getR37_amt_name_of_sub1() {
		return r37_amt_name_of_sub1;
	}
	public void setR37_amt_name_of_sub1(BigDecimal r37_amt_name_of_sub1) {
		this.r37_amt_name_of_sub1 = r37_amt_name_of_sub1;
	}
	public BigDecimal getR37_amt_name_of_sub2() {
		return r37_amt_name_of_sub2;
	}
	public void setR37_amt_name_of_sub2(BigDecimal r37_amt_name_of_sub2) {
		this.r37_amt_name_of_sub2 = r37_amt_name_of_sub2;
	}
	public BigDecimal getR37_amt_name_of_sub3() {
		return r37_amt_name_of_sub3;
	}
	public void setR37_amt_name_of_sub3(BigDecimal r37_amt_name_of_sub3) {
		this.r37_amt_name_of_sub3 = r37_amt_name_of_sub3;
	}
	public BigDecimal getR37_amt_name_of_sub4() {
		return r37_amt_name_of_sub4;
	}
	public void setR37_amt_name_of_sub4(BigDecimal r37_amt_name_of_sub4) {
		this.r37_amt_name_of_sub4 = r37_amt_name_of_sub4;
	}
	public BigDecimal getR37_amt_name_of_sub5() {
		return r37_amt_name_of_sub5;
	}
	public void setR37_amt_name_of_sub5(BigDecimal r37_amt_name_of_sub5) {
		this.r37_amt_name_of_sub5 = r37_amt_name_of_sub5;
	}
	public BigDecimal getR37_tot_amt() {
		return r37_tot_amt;
	}
	public void setR37_tot_amt(BigDecimal r37_tot_amt) {
		this.r37_tot_amt = r37_tot_amt;
	}
	public String getR38_item() {
		return r38_item;
	}
	public void setR38_item(String r38_item) {
		this.r38_item = r38_item;
	}
	public BigDecimal getR38_amt_name_of_sub1() {
		return r38_amt_name_of_sub1;
	}
	public void setR38_amt_name_of_sub1(BigDecimal r38_amt_name_of_sub1) {
		this.r38_amt_name_of_sub1 = r38_amt_name_of_sub1;
	}
	public BigDecimal getR38_amt_name_of_sub2() {
		return r38_amt_name_of_sub2;
	}
	public void setR38_amt_name_of_sub2(BigDecimal r38_amt_name_of_sub2) {
		this.r38_amt_name_of_sub2 = r38_amt_name_of_sub2;
	}
	public BigDecimal getR38_amt_name_of_sub3() {
		return r38_amt_name_of_sub3;
	}
	public void setR38_amt_name_of_sub3(BigDecimal r38_amt_name_of_sub3) {
		this.r38_amt_name_of_sub3 = r38_amt_name_of_sub3;
	}
	public BigDecimal getR38_amt_name_of_sub4() {
		return r38_amt_name_of_sub4;
	}
	public void setR38_amt_name_of_sub4(BigDecimal r38_amt_name_of_sub4) {
		this.r38_amt_name_of_sub4 = r38_amt_name_of_sub4;
	}
	public BigDecimal getR38_amt_name_of_sub5() {
		return r38_amt_name_of_sub5;
	}
	public void setR38_amt_name_of_sub5(BigDecimal r38_amt_name_of_sub5) {
		this.r38_amt_name_of_sub5 = r38_amt_name_of_sub5;
	}
	public BigDecimal getR38_tot_amt() {
		return r38_tot_amt;
	}
	public void setR38_tot_amt(BigDecimal r38_tot_amt) {
		this.r38_tot_amt = r38_tot_amt;
	}
	public String getR39_item() {
		return r39_item;
	}
	public void setR39_item(String r39_item) {
		this.r39_item = r39_item;
	}
	public BigDecimal getR39_amt_name_of_sub1() {
		return r39_amt_name_of_sub1;
	}
	public void setR39_amt_name_of_sub1(BigDecimal r39_amt_name_of_sub1) {
		this.r39_amt_name_of_sub1 = r39_amt_name_of_sub1;
	}
	public BigDecimal getR39_amt_name_of_sub2() {
		return r39_amt_name_of_sub2;
	}
	public void setR39_amt_name_of_sub2(BigDecimal r39_amt_name_of_sub2) {
		this.r39_amt_name_of_sub2 = r39_amt_name_of_sub2;
	}
	public BigDecimal getR39_amt_name_of_sub3() {
		return r39_amt_name_of_sub3;
	}
	public void setR39_amt_name_of_sub3(BigDecimal r39_amt_name_of_sub3) {
		this.r39_amt_name_of_sub3 = r39_amt_name_of_sub3;
	}
	public BigDecimal getR39_amt_name_of_sub4() {
		return r39_amt_name_of_sub4;
	}
	public void setR39_amt_name_of_sub4(BigDecimal r39_amt_name_of_sub4) {
		this.r39_amt_name_of_sub4 = r39_amt_name_of_sub4;
	}
	public BigDecimal getR39_amt_name_of_sub5() {
		return r39_amt_name_of_sub5;
	}
	public void setR39_amt_name_of_sub5(BigDecimal r39_amt_name_of_sub5) {
		this.r39_amt_name_of_sub5 = r39_amt_name_of_sub5;
	}
	public BigDecimal getR39_tot_amt() {
		return r39_tot_amt;
	}
	public void setR39_tot_amt(BigDecimal r39_tot_amt) {
		this.r39_tot_amt = r39_tot_amt;
	}
	public String getR40_item() {
		return r40_item;
	}
	public void setR40_item(String r40_item) {
		this.r40_item = r40_item;
	}
	public BigDecimal getR40_amt_name_of_sub1() {
		return r40_amt_name_of_sub1;
	}
	public void setR40_amt_name_of_sub1(BigDecimal r40_amt_name_of_sub1) {
		this.r40_amt_name_of_sub1 = r40_amt_name_of_sub1;
	}
	public BigDecimal getR40_amt_name_of_sub2() {
		return r40_amt_name_of_sub2;
	}
	public void setR40_amt_name_of_sub2(BigDecimal r40_amt_name_of_sub2) {
		this.r40_amt_name_of_sub2 = r40_amt_name_of_sub2;
	}
	public BigDecimal getR40_amt_name_of_sub3() {
		return r40_amt_name_of_sub3;
	}
	public void setR40_amt_name_of_sub3(BigDecimal r40_amt_name_of_sub3) {
		this.r40_amt_name_of_sub3 = r40_amt_name_of_sub3;
	}
	public BigDecimal getR40_amt_name_of_sub4() {
		return r40_amt_name_of_sub4;
	}
	public void setR40_amt_name_of_sub4(BigDecimal r40_amt_name_of_sub4) {
		this.r40_amt_name_of_sub4 = r40_amt_name_of_sub4;
	}
	public BigDecimal getR40_amt_name_of_sub5() {
		return r40_amt_name_of_sub5;
	}
	public void setR40_amt_name_of_sub5(BigDecimal r40_amt_name_of_sub5) {
		this.r40_amt_name_of_sub5 = r40_amt_name_of_sub5;
	}
	public BigDecimal getR40_tot_amt() {
		return r40_tot_amt;
	}
	public void setR40_tot_amt(BigDecimal r40_tot_amt) {
		this.r40_tot_amt = r40_tot_amt;
	}
	public String getR41_item() {
		return r41_item;
	}
	public void setR41_item(String r41_item) {
		this.r41_item = r41_item;
	}
	public BigDecimal getR41_amt_name_of_sub1() {
		return r41_amt_name_of_sub1;
	}
	public void setR41_amt_name_of_sub1(BigDecimal r41_amt_name_of_sub1) {
		this.r41_amt_name_of_sub1 = r41_amt_name_of_sub1;
	}
	public BigDecimal getR41_amt_name_of_sub2() {
		return r41_amt_name_of_sub2;
	}
	public void setR41_amt_name_of_sub2(BigDecimal r41_amt_name_of_sub2) {
		this.r41_amt_name_of_sub2 = r41_amt_name_of_sub2;
	}
	public BigDecimal getR41_amt_name_of_sub3() {
		return r41_amt_name_of_sub3;
	}
	public void setR41_amt_name_of_sub3(BigDecimal r41_amt_name_of_sub3) {
		this.r41_amt_name_of_sub3 = r41_amt_name_of_sub3;
	}
	public BigDecimal getR41_amt_name_of_sub4() {
		return r41_amt_name_of_sub4;
	}
	public void setR41_amt_name_of_sub4(BigDecimal r41_amt_name_of_sub4) {
		this.r41_amt_name_of_sub4 = r41_amt_name_of_sub4;
	}
	public BigDecimal getR41_amt_name_of_sub5() {
		return r41_amt_name_of_sub5;
	}
	public void setR41_amt_name_of_sub5(BigDecimal r41_amt_name_of_sub5) {
		this.r41_amt_name_of_sub5 = r41_amt_name_of_sub5;
	}
	public BigDecimal getR41_tot_amt() {
		return r41_tot_amt;
	}
	public void setR41_tot_amt(BigDecimal r41_tot_amt) {
		this.r41_tot_amt = r41_tot_amt;
	}
	public String getR42_item() {
		return r42_item;
	}
	public void setR42_item(String r42_item) {
		this.r42_item = r42_item;
	}
	public BigDecimal getR42_amt_name_of_sub1() {
		return r42_amt_name_of_sub1;
	}
	public void setR42_amt_name_of_sub1(BigDecimal r42_amt_name_of_sub1) {
		this.r42_amt_name_of_sub1 = r42_amt_name_of_sub1;
	}
	public BigDecimal getR42_amt_name_of_sub2() {
		return r42_amt_name_of_sub2;
	}
	public void setR42_amt_name_of_sub2(BigDecimal r42_amt_name_of_sub2) {
		this.r42_amt_name_of_sub2 = r42_amt_name_of_sub2;
	}
	public BigDecimal getR42_amt_name_of_sub3() {
		return r42_amt_name_of_sub3;
	}
	public void setR42_amt_name_of_sub3(BigDecimal r42_amt_name_of_sub3) {
		this.r42_amt_name_of_sub3 = r42_amt_name_of_sub3;
	}
	public BigDecimal getR42_amt_name_of_sub4() {
		return r42_amt_name_of_sub4;
	}
	public void setR42_amt_name_of_sub4(BigDecimal r42_amt_name_of_sub4) {
		this.r42_amt_name_of_sub4 = r42_amt_name_of_sub4;
	}
	public BigDecimal getR42_amt_name_of_sub5() {
		return r42_amt_name_of_sub5;
	}
	public void setR42_amt_name_of_sub5(BigDecimal r42_amt_name_of_sub5) {
		this.r42_amt_name_of_sub5 = r42_amt_name_of_sub5;
	}
	public BigDecimal getR42_tot_amt() {
		return r42_tot_amt;
	}
	public void setR42_tot_amt(BigDecimal r42_tot_amt) {
		this.r42_tot_amt = r42_tot_amt;
	}
	public String getR43_item() {
		return r43_item;
	}
	public void setR43_item(String r43_item) {
		this.r43_item = r43_item;
	}
	public BigDecimal getR43_amt_name_of_sub1() {
		return r43_amt_name_of_sub1;
	}
	public void setR43_amt_name_of_sub1(BigDecimal r43_amt_name_of_sub1) {
		this.r43_amt_name_of_sub1 = r43_amt_name_of_sub1;
	}
	public BigDecimal getR43_amt_name_of_sub2() {
		return r43_amt_name_of_sub2;
	}
	public void setR43_amt_name_of_sub2(BigDecimal r43_amt_name_of_sub2) {
		this.r43_amt_name_of_sub2 = r43_amt_name_of_sub2;
	}
	public BigDecimal getR43_amt_name_of_sub3() {
		return r43_amt_name_of_sub3;
	}
	public void setR43_amt_name_of_sub3(BigDecimal r43_amt_name_of_sub3) {
		this.r43_amt_name_of_sub3 = r43_amt_name_of_sub3;
	}
	public BigDecimal getR43_amt_name_of_sub4() {
		return r43_amt_name_of_sub4;
	}
	public void setR43_amt_name_of_sub4(BigDecimal r43_amt_name_of_sub4) {
		this.r43_amt_name_of_sub4 = r43_amt_name_of_sub4;
	}
	public BigDecimal getR43_amt_name_of_sub5() {
		return r43_amt_name_of_sub5;
	}
	public void setR43_amt_name_of_sub5(BigDecimal r43_amt_name_of_sub5) {
		this.r43_amt_name_of_sub5 = r43_amt_name_of_sub5;
	}
	public BigDecimal getR43_tot_amt() {
		return r43_tot_amt;
	}
	public void setR43_tot_amt(BigDecimal r43_tot_amt) {
		this.r43_tot_amt = r43_tot_amt;
	}
	public String getR44_item() {
		return r44_item;
	}
	public void setR44_item(String r44_item) {
		this.r44_item = r44_item;
	}
	public BigDecimal getR44_amt_name_of_sub1() {
		return r44_amt_name_of_sub1;
	}
	public void setR44_amt_name_of_sub1(BigDecimal r44_amt_name_of_sub1) {
		this.r44_amt_name_of_sub1 = r44_amt_name_of_sub1;
	}
	public BigDecimal getR44_amt_name_of_sub2() {
		return r44_amt_name_of_sub2;
	}
	public void setR44_amt_name_of_sub2(BigDecimal r44_amt_name_of_sub2) {
		this.r44_amt_name_of_sub2 = r44_amt_name_of_sub2;
	}
	public BigDecimal getR44_amt_name_of_sub3() {
		return r44_amt_name_of_sub3;
	}
	public void setR44_amt_name_of_sub3(BigDecimal r44_amt_name_of_sub3) {
		this.r44_amt_name_of_sub3 = r44_amt_name_of_sub3;
	}
	public BigDecimal getR44_amt_name_of_sub4() {
		return r44_amt_name_of_sub4;
	}
	public void setR44_amt_name_of_sub4(BigDecimal r44_amt_name_of_sub4) {
		this.r44_amt_name_of_sub4 = r44_amt_name_of_sub4;
	}
	public BigDecimal getR44_amt_name_of_sub5() {
		return r44_amt_name_of_sub5;
	}
	public void setR44_amt_name_of_sub5(BigDecimal r44_amt_name_of_sub5) {
		this.r44_amt_name_of_sub5 = r44_amt_name_of_sub5;
	}
	public BigDecimal getR44_tot_amt() {
		return r44_tot_amt;
	}
	public void setR44_tot_amt(BigDecimal r44_tot_amt) {
		this.r44_tot_amt = r44_tot_amt;
	}
	public String getR45_item() {
		return r45_item;
	}
	public void setR45_item(String r45_item) {
		this.r45_item = r45_item;
	}
	public BigDecimal getR45_amt_name_of_sub1() {
		return r45_amt_name_of_sub1;
	}
	public void setR45_amt_name_of_sub1(BigDecimal r45_amt_name_of_sub1) {
		this.r45_amt_name_of_sub1 = r45_amt_name_of_sub1;
	}
	public BigDecimal getR45_amt_name_of_sub2() {
		return r45_amt_name_of_sub2;
	}
	public void setR45_amt_name_of_sub2(BigDecimal r45_amt_name_of_sub2) {
		this.r45_amt_name_of_sub2 = r45_amt_name_of_sub2;
	}
	public BigDecimal getR45_amt_name_of_sub3() {
		return r45_amt_name_of_sub3;
	}
	public void setR45_amt_name_of_sub3(BigDecimal r45_amt_name_of_sub3) {
		this.r45_amt_name_of_sub3 = r45_amt_name_of_sub3;
	}
	public BigDecimal getR45_amt_name_of_sub4() {
		return r45_amt_name_of_sub4;
	}
	public void setR45_amt_name_of_sub4(BigDecimal r45_amt_name_of_sub4) {
		this.r45_amt_name_of_sub4 = r45_amt_name_of_sub4;
	}
	public BigDecimal getR45_amt_name_of_sub5() {
		return r45_amt_name_of_sub5;
	}
	public void setR45_amt_name_of_sub5(BigDecimal r45_amt_name_of_sub5) {
		this.r45_amt_name_of_sub5 = r45_amt_name_of_sub5;
	}
	public BigDecimal getR45_tot_amt() {
		return r45_tot_amt;
	}
	public void setR45_tot_amt(BigDecimal r45_tot_amt) {
		this.r45_tot_amt = r45_tot_amt;
	}
	public String getR46_item() {
		return r46_item;
	}
	public void setR46_item(String r46_item) {
		this.r46_item = r46_item;
	}
	public BigDecimal getR46_amt_name_of_sub1() {
		return r46_amt_name_of_sub1;
	}
	public void setR46_amt_name_of_sub1(BigDecimal r46_amt_name_of_sub1) {
		this.r46_amt_name_of_sub1 = r46_amt_name_of_sub1;
	}
	public BigDecimal getR46_amt_name_of_sub2() {
		return r46_amt_name_of_sub2;
	}
	public void setR46_amt_name_of_sub2(BigDecimal r46_amt_name_of_sub2) {
		this.r46_amt_name_of_sub2 = r46_amt_name_of_sub2;
	}
	public BigDecimal getR46_amt_name_of_sub3() {
		return r46_amt_name_of_sub3;
	}
	public void setR46_amt_name_of_sub3(BigDecimal r46_amt_name_of_sub3) {
		this.r46_amt_name_of_sub3 = r46_amt_name_of_sub3;
	}
	public BigDecimal getR46_amt_name_of_sub4() {
		return r46_amt_name_of_sub4;
	}
	public void setR46_amt_name_of_sub4(BigDecimal r46_amt_name_of_sub4) {
		this.r46_amt_name_of_sub4 = r46_amt_name_of_sub4;
	}
	public BigDecimal getR46_amt_name_of_sub5() {
		return r46_amt_name_of_sub5;
	}
	public void setR46_amt_name_of_sub5(BigDecimal r46_amt_name_of_sub5) {
		this.r46_amt_name_of_sub5 = r46_amt_name_of_sub5;
	}
	public BigDecimal getR46_tot_amt() {
		return r46_tot_amt;
	}
	public void setR46_tot_amt(BigDecimal r46_tot_amt) {
		this.r46_tot_amt = r46_tot_amt;
	}
	public String getR47_item() {
		return r47_item;
	}
	public void setR47_item(String r47_item) {
		this.r47_item = r47_item;
	}
	public BigDecimal getR47_amt_name_of_sub1() {
		return r47_amt_name_of_sub1;
	}
	public void setR47_amt_name_of_sub1(BigDecimal r47_amt_name_of_sub1) {
		this.r47_amt_name_of_sub1 = r47_amt_name_of_sub1;
	}
	public BigDecimal getR47_amt_name_of_sub2() {
		return r47_amt_name_of_sub2;
	}
	public void setR47_amt_name_of_sub2(BigDecimal r47_amt_name_of_sub2) {
		this.r47_amt_name_of_sub2 = r47_amt_name_of_sub2;
	}
	public BigDecimal getR47_amt_name_of_sub3() {
		return r47_amt_name_of_sub3;
	}
	public void setR47_amt_name_of_sub3(BigDecimal r47_amt_name_of_sub3) {
		this.r47_amt_name_of_sub3 = r47_amt_name_of_sub3;
	}
	public BigDecimal getR47_amt_name_of_sub4() {
		return r47_amt_name_of_sub4;
	}
	public void setR47_amt_name_of_sub4(BigDecimal r47_amt_name_of_sub4) {
		this.r47_amt_name_of_sub4 = r47_amt_name_of_sub4;
	}
	public BigDecimal getR47_amt_name_of_sub5() {
		return r47_amt_name_of_sub5;
	}
	public void setR47_amt_name_of_sub5(BigDecimal r47_amt_name_of_sub5) {
		this.r47_amt_name_of_sub5 = r47_amt_name_of_sub5;
	}
	public BigDecimal getR47_tot_amt() {
		return r47_tot_amt;
	}
	public void setR47_tot_amt(BigDecimal r47_tot_amt) {
		this.r47_tot_amt = r47_tot_amt;
	}
	public String getR48_item() {
		return r48_item;
	}
	public void setR48_item(String r48_item) {
		this.r48_item = r48_item;
	}
	public BigDecimal getR48_amt_name_of_sub1() {
		return r48_amt_name_of_sub1;
	}
	public void setR48_amt_name_of_sub1(BigDecimal r48_amt_name_of_sub1) {
		this.r48_amt_name_of_sub1 = r48_amt_name_of_sub1;
	}
	public BigDecimal getR48_amt_name_of_sub2() {
		return r48_amt_name_of_sub2;
	}
	public void setR48_amt_name_of_sub2(BigDecimal r48_amt_name_of_sub2) {
		this.r48_amt_name_of_sub2 = r48_amt_name_of_sub2;
	}
	public BigDecimal getR48_amt_name_of_sub3() {
		return r48_amt_name_of_sub3;
	}
	public void setR48_amt_name_of_sub3(BigDecimal r48_amt_name_of_sub3) {
		this.r48_amt_name_of_sub3 = r48_amt_name_of_sub3;
	}
	public BigDecimal getR48_amt_name_of_sub4() {
		return r48_amt_name_of_sub4;
	}
	public void setR48_amt_name_of_sub4(BigDecimal r48_amt_name_of_sub4) {
		this.r48_amt_name_of_sub4 = r48_amt_name_of_sub4;
	}
	public BigDecimal getR48_amt_name_of_sub5() {
		return r48_amt_name_of_sub5;
	}
	public void setR48_amt_name_of_sub5(BigDecimal r48_amt_name_of_sub5) {
		this.r48_amt_name_of_sub5 = r48_amt_name_of_sub5;
	}
	public BigDecimal getR48_tot_amt() {
		return r48_tot_amt;
	}
	public void setR48_tot_amt(BigDecimal r48_tot_amt) {
		this.r48_tot_amt = r48_tot_amt;
	}
	public String getR49_item() {
		return r49_item;
	}
	public void setR49_item(String r49_item) {
		this.r49_item = r49_item;
	}
	public BigDecimal getR49_amt_name_of_sub1() {
		return r49_amt_name_of_sub1;
	}
	public void setR49_amt_name_of_sub1(BigDecimal r49_amt_name_of_sub1) {
		this.r49_amt_name_of_sub1 = r49_amt_name_of_sub1;
	}
	public BigDecimal getR49_amt_name_of_sub2() {
		return r49_amt_name_of_sub2;
	}
	public void setR49_amt_name_of_sub2(BigDecimal r49_amt_name_of_sub2) {
		this.r49_amt_name_of_sub2 = r49_amt_name_of_sub2;
	}
	public BigDecimal getR49_amt_name_of_sub3() {
		return r49_amt_name_of_sub3;
	}
	public void setR49_amt_name_of_sub3(BigDecimal r49_amt_name_of_sub3) {
		this.r49_amt_name_of_sub3 = r49_amt_name_of_sub3;
	}
	public BigDecimal getR49_amt_name_of_sub4() {
		return r49_amt_name_of_sub4;
	}
	public void setR49_amt_name_of_sub4(BigDecimal r49_amt_name_of_sub4) {
		this.r49_amt_name_of_sub4 = r49_amt_name_of_sub4;
	}
	public BigDecimal getR49_amt_name_of_sub5() {
		return r49_amt_name_of_sub5;
	}
	public void setR49_amt_name_of_sub5(BigDecimal r49_amt_name_of_sub5) {
		this.r49_amt_name_of_sub5 = r49_amt_name_of_sub5;
	}
	public BigDecimal getR49_tot_amt() {
		return r49_tot_amt;
	}
	public void setR49_tot_amt(BigDecimal r49_tot_amt) {
		this.r49_tot_amt = r49_tot_amt;
	}
	public String getR50_item() {
		return r50_item;
	}
	public void setR50_item(String r50_item) {
		this.r50_item = r50_item;
	}
	public BigDecimal getR50_amt_name_of_sub1() {
		return r50_amt_name_of_sub1;
	}
	public void setR50_amt_name_of_sub1(BigDecimal r50_amt_name_of_sub1) {
		this.r50_amt_name_of_sub1 = r50_amt_name_of_sub1;
	}
	public BigDecimal getR50_amt_name_of_sub2() {
		return r50_amt_name_of_sub2;
	}
	public void setR50_amt_name_of_sub2(BigDecimal r50_amt_name_of_sub2) {
		this.r50_amt_name_of_sub2 = r50_amt_name_of_sub2;
	}
	public BigDecimal getR50_amt_name_of_sub3() {
		return r50_amt_name_of_sub3;
	}
	public void setR50_amt_name_of_sub3(BigDecimal r50_amt_name_of_sub3) {
		this.r50_amt_name_of_sub3 = r50_amt_name_of_sub3;
	}
	public BigDecimal getR50_amt_name_of_sub4() {
		return r50_amt_name_of_sub4;
	}
	public void setR50_amt_name_of_sub4(BigDecimal r50_amt_name_of_sub4) {
		this.r50_amt_name_of_sub4 = r50_amt_name_of_sub4;
	}
	public BigDecimal getR50_amt_name_of_sub5() {
		return r50_amt_name_of_sub5;
	}
	public void setR50_amt_name_of_sub5(BigDecimal r50_amt_name_of_sub5) {
		this.r50_amt_name_of_sub5 = r50_amt_name_of_sub5;
	}
	public BigDecimal getR50_tot_amt() {
		return r50_tot_amt;
	}
	public void setR50_tot_amt(BigDecimal r50_tot_amt) {
		this.r50_tot_amt = r50_tot_amt;
	}
	public String getR51_item() {
		return r51_item;
	}
	public void setR51_item(String r51_item) {
		this.r51_item = r51_item;
	}
	public BigDecimal getR51_amt_name_of_sub1() {
		return r51_amt_name_of_sub1;
	}
	public void setR51_amt_name_of_sub1(BigDecimal r51_amt_name_of_sub1) {
		this.r51_amt_name_of_sub1 = r51_amt_name_of_sub1;
	}
	public BigDecimal getR51_amt_name_of_sub2() {
		return r51_amt_name_of_sub2;
	}
	public void setR51_amt_name_of_sub2(BigDecimal r51_amt_name_of_sub2) {
		this.r51_amt_name_of_sub2 = r51_amt_name_of_sub2;
	}
	public BigDecimal getR51_amt_name_of_sub3() {
		return r51_amt_name_of_sub3;
	}
	public void setR51_amt_name_of_sub3(BigDecimal r51_amt_name_of_sub3) {
		this.r51_amt_name_of_sub3 = r51_amt_name_of_sub3;
	}
	public BigDecimal getR51_amt_name_of_sub4() {
		return r51_amt_name_of_sub4;
	}
	public void setR51_amt_name_of_sub4(BigDecimal r51_amt_name_of_sub4) {
		this.r51_amt_name_of_sub4 = r51_amt_name_of_sub4;
	}
	public BigDecimal getR51_amt_name_of_sub5() {
		return r51_amt_name_of_sub5;
	}
	public void setR51_amt_name_of_sub5(BigDecimal r51_amt_name_of_sub5) {
		this.r51_amt_name_of_sub5 = r51_amt_name_of_sub5;
	}
	public BigDecimal getR51_tot_amt() {
		return r51_tot_amt;
	}
	public void setR51_tot_amt(BigDecimal r51_tot_amt) {
		this.r51_tot_amt = r51_tot_amt;
	}
	public String getR52_item() {
		return r52_item;
	}
	public void setR52_item(String r52_item) {
		this.r52_item = r52_item;
	}
	public BigDecimal getR52_amt_name_of_sub1() {
		return r52_amt_name_of_sub1;
	}
	public void setR52_amt_name_of_sub1(BigDecimal r52_amt_name_of_sub1) {
		this.r52_amt_name_of_sub1 = r52_amt_name_of_sub1;
	}
	public BigDecimal getR52_amt_name_of_sub2() {
		return r52_amt_name_of_sub2;
	}
	public void setR52_amt_name_of_sub2(BigDecimal r52_amt_name_of_sub2) {
		this.r52_amt_name_of_sub2 = r52_amt_name_of_sub2;
	}
	public BigDecimal getR52_amt_name_of_sub3() {
		return r52_amt_name_of_sub3;
	}
	public void setR52_amt_name_of_sub3(BigDecimal r52_amt_name_of_sub3) {
		this.r52_amt_name_of_sub3 = r52_amt_name_of_sub3;
	}
	public BigDecimal getR52_amt_name_of_sub4() {
		return r52_amt_name_of_sub4;
	}
	public void setR52_amt_name_of_sub4(BigDecimal r52_amt_name_of_sub4) {
		this.r52_amt_name_of_sub4 = r52_amt_name_of_sub4;
	}
	public BigDecimal getR52_amt_name_of_sub5() {
		return r52_amt_name_of_sub5;
	}
	public void setR52_amt_name_of_sub5(BigDecimal r52_amt_name_of_sub5) {
		this.r52_amt_name_of_sub5 = r52_amt_name_of_sub5;
	}
	public BigDecimal getR52_tot_amt() {
		return r52_tot_amt;
	}
	public void setR52_tot_amt(BigDecimal r52_tot_amt) {
		this.r52_tot_amt = r52_tot_amt;
	}
	public String getR53_item() {
		return r53_item;
	}
	public void setR53_item(String r53_item) {
		this.r53_item = r53_item;
	}
	public BigDecimal getR53_amt_name_of_sub1() {
		return r53_amt_name_of_sub1;
	}
	public void setR53_amt_name_of_sub1(BigDecimal r53_amt_name_of_sub1) {
		this.r53_amt_name_of_sub1 = r53_amt_name_of_sub1;
	}
	public BigDecimal getR53_amt_name_of_sub2() {
		return r53_amt_name_of_sub2;
	}
	public void setR53_amt_name_of_sub2(BigDecimal r53_amt_name_of_sub2) {
		this.r53_amt_name_of_sub2 = r53_amt_name_of_sub2;
	}
	public BigDecimal getR53_amt_name_of_sub3() {
		return r53_amt_name_of_sub3;
	}
	public void setR53_amt_name_of_sub3(BigDecimal r53_amt_name_of_sub3) {
		this.r53_amt_name_of_sub3 = r53_amt_name_of_sub3;
	}
	public BigDecimal getR53_amt_name_of_sub4() {
		return r53_amt_name_of_sub4;
	}
	public void setR53_amt_name_of_sub4(BigDecimal r53_amt_name_of_sub4) {
		this.r53_amt_name_of_sub4 = r53_amt_name_of_sub4;
	}
	public BigDecimal getR53_amt_name_of_sub5() {
		return r53_amt_name_of_sub5;
	}
	public void setR53_amt_name_of_sub5(BigDecimal r53_amt_name_of_sub5) {
		this.r53_amt_name_of_sub5 = r53_amt_name_of_sub5;
	}
	public BigDecimal getR53_tot_amt() {
		return r53_tot_amt;
	}
	public void setR53_tot_amt(BigDecimal r53_tot_amt) {
		this.r53_tot_amt = r53_tot_amt;
	}
	public String getR54_item() {
		return r54_item;
	}
	public void setR54_item(String r54_item) {
		this.r54_item = r54_item;
	}
	public BigDecimal getR54_amt_name_of_sub1() {
		return r54_amt_name_of_sub1;
	}
	public void setR54_amt_name_of_sub1(BigDecimal r54_amt_name_of_sub1) {
		this.r54_amt_name_of_sub1 = r54_amt_name_of_sub1;
	}
	public BigDecimal getR54_amt_name_of_sub2() {
		return r54_amt_name_of_sub2;
	}
	public void setR54_amt_name_of_sub2(BigDecimal r54_amt_name_of_sub2) {
		this.r54_amt_name_of_sub2 = r54_amt_name_of_sub2;
	}
	public BigDecimal getR54_amt_name_of_sub3() {
		return r54_amt_name_of_sub3;
	}
	public void setR54_amt_name_of_sub3(BigDecimal r54_amt_name_of_sub3) {
		this.r54_amt_name_of_sub3 = r54_amt_name_of_sub3;
	}
	public BigDecimal getR54_amt_name_of_sub4() {
		return r54_amt_name_of_sub4;
	}
	public void setR54_amt_name_of_sub4(BigDecimal r54_amt_name_of_sub4) {
		this.r54_amt_name_of_sub4 = r54_amt_name_of_sub4;
	}
	public BigDecimal getR54_amt_name_of_sub5() {
		return r54_amt_name_of_sub5;
	}
	public void setR54_amt_name_of_sub5(BigDecimal r54_amt_name_of_sub5) {
		this.r54_amt_name_of_sub5 = r54_amt_name_of_sub5;
	}
	public BigDecimal getR54_tot_amt() {
		return r54_tot_amt;
	}
	public void setR54_tot_amt(BigDecimal r54_tot_amt) {
		this.r54_tot_amt = r54_tot_amt;
	}
	public String getR55_item() {
		return r55_item;
	}
	public void setR55_item(String r55_item) {
		this.r55_item = r55_item;
	}
	public BigDecimal getR55_amt_name_of_sub1() {
		return r55_amt_name_of_sub1;
	}
	public void setR55_amt_name_of_sub1(BigDecimal r55_amt_name_of_sub1) {
		this.r55_amt_name_of_sub1 = r55_amt_name_of_sub1;
	}
	public BigDecimal getR55_amt_name_of_sub2() {
		return r55_amt_name_of_sub2;
	}
	public void setR55_amt_name_of_sub2(BigDecimal r55_amt_name_of_sub2) {
		this.r55_amt_name_of_sub2 = r55_amt_name_of_sub2;
	}
	public BigDecimal getR55_amt_name_of_sub3() {
		return r55_amt_name_of_sub3;
	}
	public void setR55_amt_name_of_sub3(BigDecimal r55_amt_name_of_sub3) {
		this.r55_amt_name_of_sub3 = r55_amt_name_of_sub3;
	}
	public BigDecimal getR55_amt_name_of_sub4() {
		return r55_amt_name_of_sub4;
	}
	public void setR55_amt_name_of_sub4(BigDecimal r55_amt_name_of_sub4) {
		this.r55_amt_name_of_sub4 = r55_amt_name_of_sub4;
	}
	public BigDecimal getR55_amt_name_of_sub5() {
		return r55_amt_name_of_sub5;
	}
	public void setR55_amt_name_of_sub5(BigDecimal r55_amt_name_of_sub5) {
		this.r55_amt_name_of_sub5 = r55_amt_name_of_sub5;
	}
	public BigDecimal getR55_tot_amt() {
		return r55_tot_amt;
	}
	public void setR55_tot_amt(BigDecimal r55_tot_amt) {
		this.r55_tot_amt = r55_tot_amt;
	}
	public String getR56_item() {
		return r56_item;
	}
	public void setR56_item(String r56_item) {
		this.r56_item = r56_item;
	}
	public BigDecimal getR56_amt_name_of_sub1() {
		return r56_amt_name_of_sub1;
	}
	public void setR56_amt_name_of_sub1(BigDecimal r56_amt_name_of_sub1) {
		this.r56_amt_name_of_sub1 = r56_amt_name_of_sub1;
	}
	public BigDecimal getR56_amt_name_of_sub2() {
		return r56_amt_name_of_sub2;
	}
	public void setR56_amt_name_of_sub2(BigDecimal r56_amt_name_of_sub2) {
		this.r56_amt_name_of_sub2 = r56_amt_name_of_sub2;
	}
	public BigDecimal getR56_amt_name_of_sub3() {
		return r56_amt_name_of_sub3;
	}
	public void setR56_amt_name_of_sub3(BigDecimal r56_amt_name_of_sub3) {
		this.r56_amt_name_of_sub3 = r56_amt_name_of_sub3;
	}
	public BigDecimal getR56_amt_name_of_sub4() {
		return r56_amt_name_of_sub4;
	}
	public void setR56_amt_name_of_sub4(BigDecimal r56_amt_name_of_sub4) {
		this.r56_amt_name_of_sub4 = r56_amt_name_of_sub4;
	}
	public BigDecimal getR56_amt_name_of_sub5() {
		return r56_amt_name_of_sub5;
	}
	public void setR56_amt_name_of_sub5(BigDecimal r56_amt_name_of_sub5) {
		this.r56_amt_name_of_sub5 = r56_amt_name_of_sub5;
	}
	public BigDecimal getR56_tot_amt() {
		return r56_tot_amt;
	}
	public void setR56_tot_amt(BigDecimal r56_tot_amt) {
		this.r56_tot_amt = r56_tot_amt;
	}
	public String getR57_item() {
		return r57_item;
	}
	public void setR57_item(String r57_item) {
		this.r57_item = r57_item;
	}
	public BigDecimal getR57_amt_name_of_sub1() {
		return r57_amt_name_of_sub1;
	}
	public void setR57_amt_name_of_sub1(BigDecimal r57_amt_name_of_sub1) {
		this.r57_amt_name_of_sub1 = r57_amt_name_of_sub1;
	}
	public BigDecimal getR57_amt_name_of_sub2() {
		return r57_amt_name_of_sub2;
	}
	public void setR57_amt_name_of_sub2(BigDecimal r57_amt_name_of_sub2) {
		this.r57_amt_name_of_sub2 = r57_amt_name_of_sub2;
	}
	public BigDecimal getR57_amt_name_of_sub3() {
		return r57_amt_name_of_sub3;
	}
	public void setR57_amt_name_of_sub3(BigDecimal r57_amt_name_of_sub3) {
		this.r57_amt_name_of_sub3 = r57_amt_name_of_sub3;
	}
	public BigDecimal getR57_amt_name_of_sub4() {
		return r57_amt_name_of_sub4;
	}
	public void setR57_amt_name_of_sub4(BigDecimal r57_amt_name_of_sub4) {
		this.r57_amt_name_of_sub4 = r57_amt_name_of_sub4;
	}
	public BigDecimal getR57_amt_name_of_sub5() {
		return r57_amt_name_of_sub5;
	}
	public void setR57_amt_name_of_sub5(BigDecimal r57_amt_name_of_sub5) {
		this.r57_amt_name_of_sub5 = r57_amt_name_of_sub5;
	}
	public BigDecimal getR57_tot_amt() {
		return r57_tot_amt;
	}
	public void setR57_tot_amt(BigDecimal r57_tot_amt) {
		this.r57_tot_amt = r57_tot_amt;
	}
	public String getR58_item() {
		return r58_item;
	}
	public void setR58_item(String r58_item) {
		this.r58_item = r58_item;
	}
	public BigDecimal getR58_amt_name_of_sub1() {
		return r58_amt_name_of_sub1;
	}
	public void setR58_amt_name_of_sub1(BigDecimal r58_amt_name_of_sub1) {
		this.r58_amt_name_of_sub1 = r58_amt_name_of_sub1;
	}
	public BigDecimal getR58_amt_name_of_sub2() {
		return r58_amt_name_of_sub2;
	}
	public void setR58_amt_name_of_sub2(BigDecimal r58_amt_name_of_sub2) {
		this.r58_amt_name_of_sub2 = r58_amt_name_of_sub2;
	}
	public BigDecimal getR58_amt_name_of_sub3() {
		return r58_amt_name_of_sub3;
	}
	public void setR58_amt_name_of_sub3(BigDecimal r58_amt_name_of_sub3) {
		this.r58_amt_name_of_sub3 = r58_amt_name_of_sub3;
	}
	public BigDecimal getR58_amt_name_of_sub4() {
		return r58_amt_name_of_sub4;
	}
	public void setR58_amt_name_of_sub4(BigDecimal r58_amt_name_of_sub4) {
		this.r58_amt_name_of_sub4 = r58_amt_name_of_sub4;
	}
	public BigDecimal getR58_amt_name_of_sub5() {
		return r58_amt_name_of_sub5;
	}
	public void setR58_amt_name_of_sub5(BigDecimal r58_amt_name_of_sub5) {
		this.r58_amt_name_of_sub5 = r58_amt_name_of_sub5;
	}
	public BigDecimal getR58_tot_amt() {
		return r58_tot_amt;
	}
	public void setR58_tot_amt(BigDecimal r58_tot_amt) {
		this.r58_tot_amt = r58_tot_amt;
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
	public M_CA4_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
   
	
	 
	

	

}
