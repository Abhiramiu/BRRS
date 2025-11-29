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
@Table(name = "BRRS_M_SCI_E_SUMMARYTABLE")


public class M_SCI_E_Summary_Entity {
	
	
	
	
		
		private String	r10_product_name;
		private String	r10_cross_reference;
		private BigDecimal	r10_month;                
		private BigDecimal	r10_ytd;
		
		private String	r11_product_name;
		private String	r11_cross_reference;
		private BigDecimal	r11_month;                 
		private BigDecimal	r11_ytd;
		
		private String	r12_product_name;
		private String	r12_cross_reference;
		private BigDecimal	r12_month;                
		private BigDecimal	r12_ytd;
		
		private String	r13_product_name;
		private String	r13_cross_reference;
		private BigDecimal	r13_month;                 
		private BigDecimal	r13_ytd;
		
		private String	r14_product_name;
		private String	r14_cross_reference;
		private BigDecimal	r14_month;                
		private BigDecimal	r14_ytd;
		
		private String	r15_product_name;
		private String	r15_cross_reference;
		private BigDecimal	r15_month;                
		private BigDecimal	r15_ytd;
		
		private String	r16_product_name;
		private String	r16_cross_reference;
		private BigDecimal	r16_month;                 
		private BigDecimal	r16_ytd;
		
		private String	r17_product_name;
		private String	r17_cross_reference;
		private BigDecimal	r17_month;                 
		private BigDecimal	r17_ytd;
		private String	r18_product_name;
		private String	r18_cross_reference;
		private BigDecimal	r18_month;                 
		private BigDecimal	r18_ytd;
		
		private String	r19_product_name;
		private String	r19_cross_reference;
		private BigDecimal	r19_month;                 
		private BigDecimal	r19_ytd;
		
		private String	r20_product_name;
		private String	r20_cross_reference;
		private BigDecimal	r20_month;                 
		private BigDecimal	r20_ytd;
		
		private String	r21_product_name;
		private String	r21_cross_reference;
		private BigDecimal	r21_month;
		private BigDecimal	r21_ytd;
		private String	r22_product_name;
		private String	r22_cross_reference;
		private BigDecimal	r22_month;
		private BigDecimal	r22_ytd;
		
		private String	r23_product_name;
		private String	r23_cross_reference;
		private BigDecimal	r23_month;
		private BigDecimal	r23_ytd;
		
		private String	r24_product_name;
		private String	r24_cross_reference;
		private BigDecimal	r24_month;
		private BigDecimal	r24_ytd;
		
		private String	r25_product_name;
		private String	r25_cross_reference;
		private BigDecimal	r25_month;
		private BigDecimal	r25_ytd;
		
		private String	r26_product_name;
		private String	r26_cross_reference;
		private BigDecimal	r26_month;
		private BigDecimal	r26_ytd;
		
		private String	r27_product_name;
		private String	r27_cross_reference;
		private BigDecimal	r27_month;
		private BigDecimal	r27_ytd;
		
		private String	r28_product_name;
		private String	r28_cross_reference;
		private BigDecimal	r28_month;
		private BigDecimal	r28_ytd;
		
		private String	r29_product_name;
		private String	r29_cross_reference;
		private BigDecimal	r29_month;
		private BigDecimal	r29_ytd;
		
		private String	r30_product_name;
		private String	r30_cross_reference;
		private BigDecimal	r30_month;
		private BigDecimal	r30_ytd;
		
		private String	r31_product_name;
		private String	r31_cross_reference;
		private BigDecimal	r31_month;
		private BigDecimal	r31_ytd;
		
		private String	r32_product_name;
		private String	r32_cross_reference;
		private BigDecimal	r32_month;
		private BigDecimal	r32_ytd;
		
		private String	r33_product_name;
		private String	r33_cross_reference;
		private BigDecimal	r33_month;
		private BigDecimal	r33_ytd;
		
		private String	r34_product_name;
		private String	r34_cross_reference;
		private BigDecimal	r34_month;
		private BigDecimal	r34_ytd;
		
		private String	r35_product_name;
		private String	r35_cross_reference;
		private BigDecimal	r35_month;
		private BigDecimal	r35_ytd;
		
		private String	r36_product_name;
		private String	r36_cross_reference;
		private BigDecimal	r36_month;
		private BigDecimal	r36_ytd;
		
		private String	r37_product_name;
		private String	r37_cross_reference;
		private BigDecimal	r37_month;
		private BigDecimal	r37_ytd;
		
		private String	r38_product_name;
		private String	r38_cross_reference;
		private BigDecimal	r38_month;
		private BigDecimal	r38_ytd;
		
		private String	r39_product_name;
		private String	r39_cross_reference;
		private BigDecimal	r39_month;
		private BigDecimal	r39_ytd;
		
		private String	r40_product_name;
		private String	r40_cross_reference;
		private BigDecimal	r40_month;
		private BigDecimal	r40_ytd;
		
		private String	r41_product_name;
		private String	r41_cross_reference;
		private BigDecimal	r41_month;
		private BigDecimal	r41_ytd;
		
		private String	r42_product_name;
		private String	r42_cross_reference;
		private BigDecimal	r42_month;
		private BigDecimal	r42_ytd;
		
		private String	r43_product_name;
		private String	r43_cross_reference;
		private BigDecimal	r43_month;
		private BigDecimal	r43_ytd;
		
		private String	r44_product_name;
		private String	r44_cross_reference;
		private BigDecimal	r44_month;
		private BigDecimal	r44_ytd;
		
		private String	r45_product_name;
		private String	r45_cross_reference;
		/* private BigDecimal r45_month; */
		private BigDecimal	r45_ytd;
		
		private String	r46_product_name;
		private String	r46_cross_reference;
		/* private BigDecimal r46_month; */
		private BigDecimal	r46_ytd;
		
		private String	r47_product_name;
		private String	r47_cross_reference;
		private BigDecimal	r47_month;
		private BigDecimal	r47_ytd;
		
		private String	r48_product_name;
		private String	r48_cross_reference;
		private BigDecimal	r48_month;
		private BigDecimal	r48_ytd;
		
		private String	r49_product_name;
		private String	r49_cross_reference;
		private BigDecimal	r49_month;
		private BigDecimal	r49_ytd;
		
		private String	r50_product_name;
		private String	r50_cross_reference;
		private BigDecimal	r50_month;
		private BigDecimal	r50_ytd;
		
		private String	r51_product_name;
		private String	r51_cross_reference;
		private BigDecimal	r51_month;
		private BigDecimal	r51_ytd;
		
		private String	r52_product_name;
		private String	r52_cross_reference;
		private BigDecimal	r52_month;
		private BigDecimal	r52_ytd;
		
		private String	r53_product_name;
		private String	r53_cross_reference;
		private BigDecimal	r53_month;
		private BigDecimal	r53_ytd;
		
		private String	r54_product_name;
		private String	r54_cross_reference;
		/* private BigDecimal r54_month; */
		private BigDecimal	r54_ytd;
		
		private String	r55_product_name;
		private String	r55_cross_reference;
		private BigDecimal	r55_month;
		private BigDecimal	r55_ytd;
		
		private String	r56_product_name;
		private String	r56_cross_reference;
		private BigDecimal	r56_month;
		private BigDecimal	r56_ytd;
		
		private String	r57_product_name;
		private String	r57_cross_reference;
		private BigDecimal	r57_month;
		private BigDecimal	r57_ytd;
		
		private String	r58_product_name;
		private String	r58_cross_reference;
		/* private BigDecimal r58_month; */
		private BigDecimal	r58_ytd;
		
		private String	r59_product_name;
		private String	r59_cross_reference;
		/* private BigDecimal r59_month; */
		private BigDecimal	r59_ytd;
		
		private String	r60_product_name;
		private String	r60_cross_reference;
		/* private BigDecimal r60_month; */
		private BigDecimal	r60_ytd;
		
		private String	r61_product_name;
		private String	r61_cross_reference;
		private BigDecimal	r61_month;
		private BigDecimal	r61_ytd;
		
		private String	r62_product_name;
		private String	r62_cross_reference;
		private BigDecimal	r62_month;
		private BigDecimal	r62_ytd;
		
		private String	r63_product_name;
		private String	r63_cross_reference;
		private BigDecimal	r63_month;
		private BigDecimal	r63_ytd;
		
		private String	r64_product_name;
		private String	r64_cross_reference;
		private BigDecimal	r64_month;
		private BigDecimal	r64_ytd;
		
		private String	r65_product_name;
		private String	r65_cross_reference;
		private BigDecimal	r65_month;
		private BigDecimal	r65_ytd;
		
		private String	r66_product_name;
		private String	r66_cross_reference;
		/* private BigDecimal r66_month; */
		private BigDecimal	r66_ytd;
		
		private String	r67_product_name;
		private String	r67_cross_reference;
		/* private BigDecimal r67_month; */
		private BigDecimal	r67_ytd;
		
		private String	r68_product_name;
		private String	r68_cross_reference;
		/* private BigDecimal r68_month; */
		private BigDecimal	r68_ytd;
		
		private String	r69_product_name;
		private String	r69_cross_reference;
		private BigDecimal	r69_month;
		private BigDecimal	r69_ytd;
		
		private String	r70_product_name;
		private String	r70_cross_reference;
		private BigDecimal	r70_month;
		private BigDecimal	r70_ytd;
		
		private String	r71_product_name;
		private String	r71_cross_reference;
		private BigDecimal	r71_month;
		private BigDecimal	r71_ytd;
		
		private String	r72_product_name;
		private String	r72_cross_reference;
		private BigDecimal	r72_month;
		private BigDecimal	r72_ytd;
		
		private String	r73_product_name;
		private String	r73_cross_reference;
		private BigDecimal	r73_month;
		private BigDecimal	r73_ytd;
		
		private String	r74_product_name;
		private String	r74_cross_reference;
		/* private BigDecimal r74_month; */
		private BigDecimal	r74_ytd;
		
		private String	r75_product_name;
		private String	r75_cross_reference;
		private BigDecimal	r75_month;
		private BigDecimal	r75_ytd;
		
		private String	r76_product_name;
		private String	r76_cross_reference;
		private BigDecimal	r76_month;
		private BigDecimal	r76_ytd;
		
		private String	r77_product_name;
		private String	r77_cross_reference;
		private BigDecimal	r77_month;
		private BigDecimal	r77_ytd;
		
		private String	r78_product_name;
		private String	r78_cross_reference;
		private BigDecimal	r78_month;
		private BigDecimal	r78_ytd 
		;
		private String	r79_product_name;
		private String	r79_cross_reference;
		private BigDecimal	r79_month;
		private BigDecimal	r79_ytd;
		
		private String	r80_product_name;
		private String	r80_cross_reference;
		private BigDecimal	r80_month;
		private BigDecimal	r80_ytd;
		
		private String	r81_product_name;
		private String	r81_cross_reference;
		private BigDecimal	r81_month;
		private BigDecimal	r81_ytd;
		
		private String	r82_product_name;
		private String	r82_cross_reference;
		private BigDecimal	r82_month;
		private BigDecimal	r82_ytd;
		
		private String	r83_product_name;
		private String	r83_cross_reference;
		private BigDecimal	r83_month;
		private BigDecimal	r83_ytd;
		
		private String	r84_product_name;
		private String	r84_cross_reference;
		private BigDecimal	r84_month;
		private BigDecimal	r84_ytd;
		
		private String	r85_product_name;
		private String	r85_cross_reference;
		/* private BigDecimal r85_month; */
		private BigDecimal	r85_ytd;
		
		private String	r86_product_name;
		private String	r86_cross_reference;
		private BigDecimal	r86_month;
		private BigDecimal	r86_ytd ;
		
		
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
		public String getR10_product_name() {
			return r10_product_name;
		}
		public void setR10_product_name(String r10_product_name) {
			this.r10_product_name = r10_product_name;
		}
		public String getR10_cross_reference() {
			return r10_cross_reference;
		}
		public void setR10_cross_reference(String r10_cross_reference) {
			this.r10_cross_reference = r10_cross_reference;
		}
		public BigDecimal getR10_month() {
			return r10_month;
		}
		public void setR10_month(BigDecimal r10_month) {
			this.r10_month = r10_month;
		}
		public BigDecimal getR10_ytd() {
			return r10_ytd;
		}
		public void setR10_ytd(BigDecimal r10_ytd) {
			this.r10_ytd = r10_ytd;
		}
		public String getR11_product_name() {
			return r11_product_name;
		}
		public void setR11_product_name(String r11_product_name) {
			this.r11_product_name = r11_product_name;
		}
		public String getR11_cross_reference() {
			return r11_cross_reference;
		}
		public void setR11_cross_reference(String r11_cross_reference) {
			this.r11_cross_reference = r11_cross_reference;
		}
		public BigDecimal getR11_month() {
			return r11_month;
		}
		public void setR11_month(BigDecimal r11_month) {
			this.r11_month = r11_month;
		}
		public BigDecimal getR11_ytd() {
			return r11_ytd;
		}
		public void setR11_ytd(BigDecimal r11_ytd) {
			this.r11_ytd = r11_ytd;
		}
		public String getR12_product_name() {
			return r12_product_name;
		}
		public void setR12_product_name(String r12_product_name) {
			this.r12_product_name = r12_product_name;
		}
		public String getR12_cross_reference() {
			return r12_cross_reference;
		}
		public void setR12_cross_reference(String r12_cross_reference) {
			this.r12_cross_reference = r12_cross_reference;
		}
		public BigDecimal getR12_month() {
			return r12_month;
		}
		public void setR12_month(BigDecimal r12_month) {
			this.r12_month = r12_month;
		}
		public BigDecimal getR12_ytd() {
			return r12_ytd;
		}
		public void setR12_ytd(BigDecimal r12_ytd) {
			this.r12_ytd = r12_ytd;
		}
		public String getR13_product_name() {
			return r13_product_name;
		}
		public void setR13_product_name(String r13_product_name) {
			this.r13_product_name = r13_product_name;
		}
		public String getR13_cross_reference() {
			return r13_cross_reference;
		}
		public void setR13_cross_reference(String r13_cross_reference) {
			this.r13_cross_reference = r13_cross_reference;
		}
		public BigDecimal getR13_month() {
			return r13_month;
		}
		public void setR13_month(BigDecimal r13_month) {
			this.r13_month = r13_month;
		}
		public BigDecimal getR13_ytd() {
			return r13_ytd;
		}
		public void setR13_ytd(BigDecimal r13_ytd) {
			this.r13_ytd = r13_ytd;
		}
		public String getR14_product_name() {
			return r14_product_name;
		}
		public void setR14_product_name(String r14_product_name) {
			this.r14_product_name = r14_product_name;
		}
		public String getR14_cross_reference() {
			return r14_cross_reference;
		}
		public void setR14_cross_reference(String r14_cross_reference) {
			this.r14_cross_reference = r14_cross_reference;
		}
		public BigDecimal getR14_month() {
			return r14_month;
		}
		public void setR14_month(BigDecimal r14_month) {
			this.r14_month = r14_month;
		}
		public BigDecimal getR14_ytd() {
			return r14_ytd;
		}
		public void setR14_ytd(BigDecimal r14_ytd) {
			this.r14_ytd = r14_ytd;
		}
		public String getR15_product_name() {
			return r15_product_name;
		}
		public void setR15_product_name(String r15_product_name) {
			this.r15_product_name = r15_product_name;
		}
		public String getR15_cross_reference() {
			return r15_cross_reference;
		}
		public void setR15_cross_reference(String r15_cross_reference) {
			this.r15_cross_reference = r15_cross_reference;
		}
		public BigDecimal getR15_month() {
			return r15_month;
		}
		public void setR15_month(BigDecimal r15_month) {
			this.r15_month = r15_month;
		}
		public BigDecimal getR15_ytd() {
			return r15_ytd;
		}
		public void setR15_ytd(BigDecimal r15_ytd) {
			this.r15_ytd = r15_ytd;
		}
		public String getR16_product_name() {
			return r16_product_name;
		}
		public void setR16_product_name(String r16_product_name) {
			this.r16_product_name = r16_product_name;
		}
		public String getR16_cross_reference() {
			return r16_cross_reference;
		}
		public void setR16_cross_reference(String r16_cross_reference) {
			this.r16_cross_reference = r16_cross_reference;
		}
		public BigDecimal getR16_month() {
			return r16_month;
		}
		public void setR16_month(BigDecimal r16_month) {
			this.r16_month = r16_month;
		}
		public BigDecimal getR16_ytd() {
			return r16_ytd;
		}
		public void setR16_ytd(BigDecimal r16_ytd) {
			this.r16_ytd = r16_ytd;
		}
		public String getR17_product_name() {
			return r17_product_name;
		}
		public void setR17_product_name(String r17_product_name) {
			this.r17_product_name = r17_product_name;
		}
		public String getR17_cross_reference() {
			return r17_cross_reference;
		}
		public void setR17_cross_reference(String r17_cross_reference) {
			this.r17_cross_reference = r17_cross_reference;
		}
		public BigDecimal getR17_month() {
			return r17_month;
		}
		public void setR17_month(BigDecimal r17_month) {
			this.r17_month = r17_month;
		}
		public BigDecimal getR17_ytd() {
			return r17_ytd;
		}
		public void setR17_ytd(BigDecimal r17_ytd) {
			this.r17_ytd = r17_ytd;
		}
		public String getR18_product_name() {
			return r18_product_name;
		}
		public void setR18_product_name(String r18_product_name) {
			this.r18_product_name = r18_product_name;
		}
		public String getR18_cross_reference() {
			return r18_cross_reference;
		}
		public void setR18_cross_reference(String r18_cross_reference) {
			this.r18_cross_reference = r18_cross_reference;
		}
		public BigDecimal getR18_month() {
			return r18_month;
		}
		public void setR18_month(BigDecimal r18_month) {
			this.r18_month = r18_month;
		}
		public BigDecimal getR18_ytd() {
			return r18_ytd;
		}
		public void setR18_ytd(BigDecimal r18_ytd) {
			this.r18_ytd = r18_ytd;
		}
		public String getR19_product_name() {
			return r19_product_name;
		}
		public void setR19_product_name(String r19_product_name) {
			this.r19_product_name = r19_product_name;
		}
		public String getR19_cross_reference() {
			return r19_cross_reference;
		}
		public void setR19_cross_reference(String r19_cross_reference) {
			this.r19_cross_reference = r19_cross_reference;
		}
		public BigDecimal getR19_month() {
			return r19_month;
		}
		public void setR19_month(BigDecimal r19_month) {
			this.r19_month = r19_month;
		}
		public BigDecimal getR19_ytd() {
			return r19_ytd;
		}
		public void setR19_ytd(BigDecimal r19_ytd) {
			this.r19_ytd = r19_ytd;
		}
		public String getR20_product_name() {
			return r20_product_name;
		}
		public void setR20_product_name(String r20_product_name) {
			this.r20_product_name = r20_product_name;
		}
		public String getR20_cross_reference() {
			return r20_cross_reference;
		}
		public void setR20_cross_reference(String r20_cross_reference) {
			this.r20_cross_reference = r20_cross_reference;
		}
		public BigDecimal getR20_month() {
			return r20_month;
		}
		public void setR20_month(BigDecimal r20_month) {
			this.r20_month = r20_month;
		}
		public BigDecimal getR20_ytd() {
			return r20_ytd;
		}
		public void setR20_ytd(BigDecimal r20_ytd) {
			this.r20_ytd = r20_ytd;
		}
		public String getR21_product_name() {
			return r21_product_name;
		}
		public void setR21_product_name(String r21_product_name) {
			this.r21_product_name = r21_product_name;
		}
		public String getR21_cross_reference() {
			return r21_cross_reference;
		}
		public void setR21_cross_reference(String r21_cross_reference) {
			this.r21_cross_reference = r21_cross_reference;
		}
		public BigDecimal getR21_month() {
			return r21_month;
		}
		public void setR21_month(BigDecimal r21_month) {
			this.r21_month = r21_month;
		}
		public BigDecimal getR21_ytd() {
			return r21_ytd;
		}
		public void setR21_ytd(BigDecimal r21_ytd) {
			this.r21_ytd = r21_ytd;
		}
		public String getR22_product_name() {
			return r22_product_name;
		}
		public void setR22_product_name(String r22_product_name) {
			this.r22_product_name = r22_product_name;
		}
		public String getR22_cross_reference() {
			return r22_cross_reference;
		}
		public void setR22_cross_reference(String r22_cross_reference) {
			this.r22_cross_reference = r22_cross_reference;
		}
		public BigDecimal getR22_month() {
			return r22_month;
		}
		public void setR22_month(BigDecimal r22_month) {
			this.r22_month = r22_month;
		}
		public BigDecimal getR22_ytd() {
			return r22_ytd;
		}
		public void setR22_ytd(BigDecimal r22_ytd) {
			this.r22_ytd = r22_ytd;
		}
		public String getR23_product_name() {
			return r23_product_name;
		}
		public void setR23_product_name(String r23_product_name) {
			this.r23_product_name = r23_product_name;
		}
		public String getR23_cross_reference() {
			return r23_cross_reference;
		}
		public void setR23_cross_reference(String r23_cross_reference) {
			this.r23_cross_reference = r23_cross_reference;
		}
		public BigDecimal getR23_month() {
			return r23_month;
		}
		public void setR23_month(BigDecimal r23_month) {
			this.r23_month = r23_month;
		}
		public BigDecimal getR23_ytd() {
			return r23_ytd;
		}
		public void setR23_ytd(BigDecimal r23_ytd) {
			this.r23_ytd = r23_ytd;
		}
		public String getR24_product_name() {
			return r24_product_name;
		}
		public void setR24_product_name(String r24_product_name) {
			this.r24_product_name = r24_product_name;
		}
		public String getR24_cross_reference() {
			return r24_cross_reference;
		}
		public void setR24_cross_reference(String r24_cross_reference) {
			this.r24_cross_reference = r24_cross_reference;
		}
		public BigDecimal getR24_month() {
			return r24_month;
		}
		public void setR24_month(BigDecimal r24_month) {
			this.r24_month = r24_month;
		}
		public BigDecimal getR24_ytd() {
			return r24_ytd;
		}
		public void setR24_ytd(BigDecimal r24_ytd) {
			this.r24_ytd = r24_ytd;
		}
		public String getR25_product_name() {
			return r25_product_name;
		}
		public void setR25_product_name(String r25_product_name) {
			this.r25_product_name = r25_product_name;
		}
		public String getR25_cross_reference() {
			return r25_cross_reference;
		}
		public void setR25_cross_reference(String r25_cross_reference) {
			this.r25_cross_reference = r25_cross_reference;
		}
		public BigDecimal getR25_month() {
			return r25_month;
		}
		public void setR25_month(BigDecimal r25_month) {
			this.r25_month = r25_month;
		}
		public BigDecimal getR25_ytd() {
			return r25_ytd;
		}
		public void setR25_ytd(BigDecimal r25_ytd) {
			this.r25_ytd = r25_ytd;
		}
		public String getR26_product_name() {
			return r26_product_name;
		}
		public void setR26_product_name(String r26_product_name) {
			this.r26_product_name = r26_product_name;
		}
		public String getR26_cross_reference() {
			return r26_cross_reference;
		}
		public void setR26_cross_reference(String r26_cross_reference) {
			this.r26_cross_reference = r26_cross_reference;
		}
		public BigDecimal getR26_month() {
			return r26_month;
		}
		public void setR26_month(BigDecimal r26_month) {
			this.r26_month = r26_month;
		}
		public BigDecimal getR26_ytd() {
			return r26_ytd;
		}
		public void setR26_ytd(BigDecimal r26_ytd) {
			this.r26_ytd = r26_ytd;
		}
		public String getR27_product_name() {
			return r27_product_name;
		}
		public void setR27_product_name(String r27_product_name) {
			this.r27_product_name = r27_product_name;
		}
		public String getR27_cross_reference() {
			return r27_cross_reference;
		}
		public void setR27_cross_reference(String r27_cross_reference) {
			this.r27_cross_reference = r27_cross_reference;
		}
		public BigDecimal getR27_month() {
			return r27_month;
		}
		public void setR27_month(BigDecimal r27_month) {
			this.r27_month = r27_month;
		}
		public BigDecimal getR27_ytd() {
			return r27_ytd;
		}
		public void setR27_ytd(BigDecimal r27_ytd) {
			this.r27_ytd = r27_ytd;
		}
		public String getR28_product_name() {
			return r28_product_name;
		}
		public void setR28_product_name(String r28_product_name) {
			this.r28_product_name = r28_product_name;
		}
		public String getR28_cross_reference() {
			return r28_cross_reference;
		}
		public void setR28_cross_reference(String r28_cross_reference) {
			this.r28_cross_reference = r28_cross_reference;
		}
		public BigDecimal getR28_month() {
			return r28_month;
		}
		public void setR28_month(BigDecimal r28_month) {
			this.r28_month = r28_month;
		}
		public BigDecimal getR28_ytd() {
			return r28_ytd;
		}
		public void setR28_ytd(BigDecimal r28_ytd) {
			this.r28_ytd = r28_ytd;
		}
		public String getR29_product_name() {
			return r29_product_name;
		}
		public void setR29_product_name(String r29_product_name) {
			this.r29_product_name = r29_product_name;
		}
		public String getR29_cross_reference() {
			return r29_cross_reference;
		}
		public void setR29_cross_reference(String r29_cross_reference) {
			this.r29_cross_reference = r29_cross_reference;
		}
		public BigDecimal getR29_month() {
			return r29_month;
		}
		public void setR29_month(BigDecimal r29_month) {
			this.r29_month = r29_month;
		}
		public BigDecimal getR29_ytd() {
			return r29_ytd;
		}
		public void setR29_ytd(BigDecimal r29_ytd) {
			this.r29_ytd = r29_ytd;
		}
		public String getR30_product_name() {
			return r30_product_name;
		}
		public void setR30_product_name(String r30_product_name) {
			this.r30_product_name = r30_product_name;
		}
		public String getR30_cross_reference() {
			return r30_cross_reference;
		}
		public void setR30_cross_reference(String r30_cross_reference) {
			this.r30_cross_reference = r30_cross_reference;
		}
		public BigDecimal getR30_month() {
			return r30_month;
		}
		public void setR30_month(BigDecimal r30_month) {
			this.r30_month = r30_month;
		}
		public BigDecimal getR30_ytd() {
			return r30_ytd;
		}
		public void setR30_ytd(BigDecimal r30_ytd) {
			this.r30_ytd = r30_ytd;
		}
		public String getR31_product_name() {
			return r31_product_name;
		}
		public void setR31_product_name(String r31_product_name) {
			this.r31_product_name = r31_product_name;
		}
		public String getR31_cross_reference() {
			return r31_cross_reference;
		}
		public void setR31_cross_reference(String r31_cross_reference) {
			this.r31_cross_reference = r31_cross_reference;
		}
		public BigDecimal getR31_month() {
			return r31_month;
		}
		public void setR31_month(BigDecimal r31_month) {
			this.r31_month = r31_month;
		}
		public BigDecimal getR31_ytd() {
			return r31_ytd;
		}
		public void setR31_ytd(BigDecimal r31_ytd) {
			this.r31_ytd = r31_ytd;
		}
		public String getR32_product_name() {
			return r32_product_name;
		}
		public void setR32_product_name(String r32_product_name) {
			this.r32_product_name = r32_product_name;
		}
		public String getR32_cross_reference() {
			return r32_cross_reference;
		}
		public void setR32_cross_reference(String r32_cross_reference) {
			this.r32_cross_reference = r32_cross_reference;
		}
		public BigDecimal getR32_month() {
			return r32_month;
		}
		public void setR32_month(BigDecimal r32_month) {
			this.r32_month = r32_month;
		}
		public BigDecimal getR32_ytd() {
			return r32_ytd;
		}
		public void setR32_ytd(BigDecimal r32_ytd) {
			this.r32_ytd = r32_ytd;
		}
		public String getR33_product_name() {
			return r33_product_name;
		}
		public void setR33_product_name(String r33_product_name) {
			this.r33_product_name = r33_product_name;
		}
		public String getR33_cross_reference() {
			return r33_cross_reference;
		}
		public void setR33_cross_reference(String r33_cross_reference) {
			this.r33_cross_reference = r33_cross_reference;
		}
		public BigDecimal getR33_month() {
			return r33_month;
		}
		public void setR33_month(BigDecimal r33_month) {
			this.r33_month = r33_month;
		}
		public BigDecimal getR33_ytd() {
			return r33_ytd;
		}
		public void setR33_ytd(BigDecimal r33_ytd) {
			this.r33_ytd = r33_ytd;
		}
		public String getR34_product_name() {
			return r34_product_name;
		}
		public void setR34_product_name(String r34_product_name) {
			this.r34_product_name = r34_product_name;
		}
		public String getR34_cross_reference() {
			return r34_cross_reference;
		}
		public void setR34_cross_reference(String r34_cross_reference) {
			this.r34_cross_reference = r34_cross_reference;
		}
		public BigDecimal getR34_month() {
			return r34_month;
		}
		public void setR34_month(BigDecimal r34_month) {
			this.r34_month = r34_month;
		}
		public BigDecimal getR34_ytd() {
			return r34_ytd;
		}
		public void setR34_ytd(BigDecimal r34_ytd) {
			this.r34_ytd = r34_ytd;
		}
		public String getR35_product_name() {
			return r35_product_name;
		}
		public void setR35_product_name(String r35_product_name) {
			this.r35_product_name = r35_product_name;
		}
		public String getR35_cross_reference() {
			return r35_cross_reference;
		}
		public void setR35_cross_reference(String r35_cross_reference) {
			this.r35_cross_reference = r35_cross_reference;
		}
		public BigDecimal getR35_month() {
			return r35_month;
		}
		public void setR35_month(BigDecimal r35_month) {
			this.r35_month = r35_month;
		}
		public BigDecimal getR35_ytd() {
			return r35_ytd;
		}
		public void setR35_ytd(BigDecimal r35_ytd) {
			this.r35_ytd = r35_ytd;
		}
		public String getR36_product_name() {
			return r36_product_name;
		}
		public void setR36_product_name(String r36_product_name) {
			this.r36_product_name = r36_product_name;
		}
		public String getR36_cross_reference() {
			return r36_cross_reference;
		}
		public void setR36_cross_reference(String r36_cross_reference) {
			this.r36_cross_reference = r36_cross_reference;
		}
		public BigDecimal getR36_month() {
			return r36_month;
		}
		public void setR36_month(BigDecimal r36_month) {
			this.r36_month = r36_month;
		}
		public BigDecimal getR36_ytd() {
			return r36_ytd;
		}
		public void setR36_ytd(BigDecimal r36_ytd) {
			this.r36_ytd = r36_ytd;
		}
		public String getR37_product_name() {
			return r37_product_name;
		}
		public void setR37_product_name(String r37_product_name) {
			this.r37_product_name = r37_product_name;
		}
		public String getR37_cross_reference() {
			return r37_cross_reference;
		}
		public void setR37_cross_reference(String r37_cross_reference) {
			this.r37_cross_reference = r37_cross_reference;
		}
		public BigDecimal getR37_month() {
			return r37_month;
		}
		public void setR37_month(BigDecimal r37_month) {
			this.r37_month = r37_month;
		}
		public BigDecimal getR37_ytd() {
			return r37_ytd;
		}
		public void setR37_ytd(BigDecimal r37_ytd) {
			this.r37_ytd = r37_ytd;
		}
		public String getR38_product_name() {
			return r38_product_name;
		}
		public void setR38_product_name(String r38_product_name) {
			this.r38_product_name = r38_product_name;
		}
		public String getR38_cross_reference() {
			return r38_cross_reference;
		}
		public void setR38_cross_reference(String r38_cross_reference) {
			this.r38_cross_reference = r38_cross_reference;
		}
		public BigDecimal getR38_month() {
			return r38_month;
		}
		public void setR38_month(BigDecimal r38_month) {
			this.r38_month = r38_month;
		}
		public BigDecimal getR38_ytd() {
			return r38_ytd;
		}
		public void setR38_ytd(BigDecimal r38_ytd) {
			this.r38_ytd = r38_ytd;
		}
		public String getR39_product_name() {
			return r39_product_name;
		}
		public void setR39_product_name(String r39_product_name) {
			this.r39_product_name = r39_product_name;
		}
		public String getR39_cross_reference() {
			return r39_cross_reference;
		}
		public void setR39_cross_reference(String r39_cross_reference) {
			this.r39_cross_reference = r39_cross_reference;
		}
		public BigDecimal getR39_month() {
			return r39_month;
		}
		public void setR39_month(BigDecimal r39_month) {
			this.r39_month = r39_month;
		}
		public BigDecimal getR39_ytd() {
			return r39_ytd;
		}
		public void setR39_ytd(BigDecimal r39_ytd) {
			this.r39_ytd = r39_ytd;
		}
		public String getR40_product_name() {
			return r40_product_name;
		}
		public void setR40_product_name(String r40_product_name) {
			this.r40_product_name = r40_product_name;
		}
		public String getR40_cross_reference() {
			return r40_cross_reference;
		}
		public void setR40_cross_reference(String r40_cross_reference) {
			this.r40_cross_reference = r40_cross_reference;
		}
		public BigDecimal getR40_month() {
			return r40_month;
		}
		public void setR40_month(BigDecimal r40_month) {
			this.r40_month = r40_month;
		}
		public BigDecimal getR40_ytd() {
			return r40_ytd;
		}
		public void setR40_ytd(BigDecimal r40_ytd) {
			this.r40_ytd = r40_ytd;
		}
		public String getR41_product_name() {
			return r41_product_name;
		}
		public void setR41_product_name(String r41_product_name) {
			this.r41_product_name = r41_product_name;
		}
		public String getR41_cross_reference() {
			return r41_cross_reference;
		}
		public void setR41_cross_reference(String r41_cross_reference) {
			this.r41_cross_reference = r41_cross_reference;
		}
		public BigDecimal getR41_month() {
			return r41_month;
		}
		public void setR41_month(BigDecimal r41_month) {
			this.r41_month = r41_month;
		}
		public BigDecimal getR41_ytd() {
			return r41_ytd;
		}
		public void setR41_ytd(BigDecimal r41_ytd) {
			this.r41_ytd = r41_ytd;
		}
		public String getR42_product_name() {
			return r42_product_name;
		}
		public void setR42_product_name(String r42_product_name) {
			this.r42_product_name = r42_product_name;
		}
		public String getR42_cross_reference() {
			return r42_cross_reference;
		}
		public void setR42_cross_reference(String r42_cross_reference) {
			this.r42_cross_reference = r42_cross_reference;
		}
		public BigDecimal getR42_month() {
			return r42_month;
		}
		public void setR42_month(BigDecimal r42_month) {
			this.r42_month = r42_month;
		}
		public BigDecimal getR42_ytd() {
			return r42_ytd;
		}
		public void setR42_ytd(BigDecimal r42_ytd) {
			this.r42_ytd = r42_ytd;
		}
		public String getR43_product_name() {
			return r43_product_name;
		}
		public void setR43_product_name(String r43_product_name) {
			this.r43_product_name = r43_product_name;
		}
		public String getR43_cross_reference() {
			return r43_cross_reference;
		}
		public void setR43_cross_reference(String r43_cross_reference) {
			this.r43_cross_reference = r43_cross_reference;
		}
		public BigDecimal getR43_month() {
			return r43_month;
		}
		public void setR43_month(BigDecimal r43_month) {
			this.r43_month = r43_month;
		}
		public BigDecimal getR43_ytd() {
			return r43_ytd;
		}
		public void setR43_ytd(BigDecimal r43_ytd) {
			this.r43_ytd = r43_ytd;
		}
		public String getR44_product_name() {
			return r44_product_name;
		}
		public void setR44_product_name(String r44_product_name) {
			this.r44_product_name = r44_product_name;
		}
		public String getR44_cross_reference() {
			return r44_cross_reference;
		}
		public void setR44_cross_reference(String r44_cross_reference) {
			this.r44_cross_reference = r44_cross_reference;
		}
		public BigDecimal getR44_month() {
			return r44_month;
		}
		public void setR44_month(BigDecimal r44_month) {
			this.r44_month = r44_month;
		}
		public BigDecimal getR44_ytd() {
			return r44_ytd;
		}
		public void setR44_ytd(BigDecimal r44_ytd) {
			this.r44_ytd = r44_ytd;
		}
		public String getR45_product_name() {
			return r45_product_name;
		}
		public void setR45_product_name(String r45_product_name) {
			this.r45_product_name = r45_product_name;
		}
		public String getR45_cross_reference() {
			return r45_cross_reference;
		}
		public void setR45_cross_reference(String r45_cross_reference) {
			this.r45_cross_reference = r45_cross_reference;
		}
		public BigDecimal getR45_ytd() {
			return r45_ytd;
		}
		public void setR45_ytd(BigDecimal r45_ytd) {
			this.r45_ytd = r45_ytd;
		}
		public String getR46_product_name() {
			return r46_product_name;
		}
		public void setR46_product_name(String r46_product_name) {
			this.r46_product_name = r46_product_name;
		}
		public String getR46_cross_reference() {
			return r46_cross_reference;
		}
		public void setR46_cross_reference(String r46_cross_reference) {
			this.r46_cross_reference = r46_cross_reference;
		}
		public BigDecimal getR46_ytd() {
			return r46_ytd;
		}
		public void setR46_ytd(BigDecimal r46_ytd) {
			this.r46_ytd = r46_ytd;
		}
		public String getR47_product_name() {
			return r47_product_name;
		}
		public void setR47_product_name(String r47_product_name) {
			this.r47_product_name = r47_product_name;
		}
		public String getR47_cross_reference() {
			return r47_cross_reference;
		}
		public void setR47_cross_reference(String r47_cross_reference) {
			this.r47_cross_reference = r47_cross_reference;
		}
		public BigDecimal getR47_month() {
			return r47_month;
		}
		public void setR47_month(BigDecimal r47_month) {
			this.r47_month = r47_month;
		}
		public BigDecimal getR47_ytd() {
			return r47_ytd;
		}
		public void setR47_ytd(BigDecimal r47_ytd) {
			this.r47_ytd = r47_ytd;
		}
		public String getR48_product_name() {
			return r48_product_name;
		}
		public void setR48_product_name(String r48_product_name) {
			this.r48_product_name = r48_product_name;
		}
		public String getR48_cross_reference() {
			return r48_cross_reference;
		}
		public void setR48_cross_reference(String r48_cross_reference) {
			this.r48_cross_reference = r48_cross_reference;
		}
		public BigDecimal getR48_month() {
			return r48_month;
		}
		public void setR48_month(BigDecimal r48_month) {
			this.r48_month = r48_month;
		}
		public BigDecimal getR48_ytd() {
			return r48_ytd;
		}
		public void setR48_ytd(BigDecimal r48_ytd) {
			this.r48_ytd = r48_ytd;
		}
		public String getR49_product_name() {
			return r49_product_name;
		}
		public void setR49_product_name(String r49_product_name) {
			this.r49_product_name = r49_product_name;
		}
		public String getR49_cross_reference() {
			return r49_cross_reference;
		}
		public void setR49_cross_reference(String r49_cross_reference) {
			this.r49_cross_reference = r49_cross_reference;
		}
		public BigDecimal getR49_month() {
			return r49_month;
		}
		public void setR49_month(BigDecimal r49_month) {
			this.r49_month = r49_month;
		}
		public BigDecimal getR49_ytd() {
			return r49_ytd;
		}
		public void setR49_ytd(BigDecimal r49_ytd) {
			this.r49_ytd = r49_ytd;
		}
		public String getR50_product_name() {
			return r50_product_name;
		}
		public void setR50_product_name(String r50_product_name) {
			this.r50_product_name = r50_product_name;
		}
		public String getR50_cross_reference() {
			return r50_cross_reference;
		}
		public void setR50_cross_reference(String r50_cross_reference) {
			this.r50_cross_reference = r50_cross_reference;
		}
		public BigDecimal getR50_month() {
			return r50_month;
		}
		public void setR50_month(BigDecimal r50_month) {
			this.r50_month = r50_month;
		}
		public BigDecimal getR50_ytd() {
			return r50_ytd;
		}
		public void setR50_ytd(BigDecimal r50_ytd) {
			this.r50_ytd = r50_ytd;
		}
		public String getR51_product_name() {
			return r51_product_name;
		}
		public void setR51_product_name(String r51_product_name) {
			this.r51_product_name = r51_product_name;
		}
		public String getR51_cross_reference() {
			return r51_cross_reference;
		}
		public void setR51_cross_reference(String r51_cross_reference) {
			this.r51_cross_reference = r51_cross_reference;
		}
		public BigDecimal getR51_month() {
			return r51_month;
		}
		public void setR51_month(BigDecimal r51_month) {
			this.r51_month = r51_month;
		}
		public BigDecimal getR51_ytd() {
			return r51_ytd;
		}
		public void setR51_ytd(BigDecimal r51_ytd) {
			this.r51_ytd = r51_ytd;
		}
		public String getR52_product_name() {
			return r52_product_name;
		}
		public void setR52_product_name(String r52_product_name) {
			this.r52_product_name = r52_product_name;
		}
		public String getR52_cross_reference() {
			return r52_cross_reference;
		}
		public void setR52_cross_reference(String r52_cross_reference) {
			this.r52_cross_reference = r52_cross_reference;
		}
		public BigDecimal getR52_month() {
			return r52_month;
		}
		public void setR52_month(BigDecimal r52_month) {
			this.r52_month = r52_month;
		}
		public BigDecimal getR52_ytd() {
			return r52_ytd;
		}
		public void setR52_ytd(BigDecimal r52_ytd) {
			this.r52_ytd = r52_ytd;
		}
		public String getR53_product_name() {
			return r53_product_name;
		}
		public void setR53_product_name(String r53_product_name) {
			this.r53_product_name = r53_product_name;
		}
		public String getR53_cross_reference() {
			return r53_cross_reference;
		}
		public void setR53_cross_reference(String r53_cross_reference) {
			this.r53_cross_reference = r53_cross_reference;
		}
		public BigDecimal getR53_month() {
			return r53_month;
		}
		public void setR53_month(BigDecimal r53_month) {
			this.r53_month = r53_month;
		}
		public BigDecimal getR53_ytd() {
			return r53_ytd;
		}
		public void setR53_ytd(BigDecimal r53_ytd) {
			this.r53_ytd = r53_ytd;
		}
		public String getR54_product_name() {
			return r54_product_name;
		}
		public void setR54_product_name(String r54_product_name) {
			this.r54_product_name = r54_product_name;
		}
		public String getR54_cross_reference() {
			return r54_cross_reference;
		}
		public void setR54_cross_reference(String r54_cross_reference) {
			this.r54_cross_reference = r54_cross_reference;
		}
		public BigDecimal getR54_ytd() {
			return r54_ytd;
		}
		public void setR54_ytd(BigDecimal r54_ytd) {
			this.r54_ytd = r54_ytd;
		}
		public String getR55_product_name() {
			return r55_product_name;
		}
		public void setR55_product_name(String r55_product_name) {
			this.r55_product_name = r55_product_name;
		}
		public String getR55_cross_reference() {
			return r55_cross_reference;
		}
		public void setR55_cross_reference(String r55_cross_reference) {
			this.r55_cross_reference = r55_cross_reference;
		}
		public BigDecimal getR55_month() {
			return r55_month;
		}
		public void setR55_month(BigDecimal r55_month) {
			this.r55_month = r55_month;
		}
		public BigDecimal getR55_ytd() {
			return r55_ytd;
		}
		public void setR55_ytd(BigDecimal r55_ytd) {
			this.r55_ytd = r55_ytd;
		}
		public String getR56_product_name() {
			return r56_product_name;
		}
		public void setR56_product_name(String r56_product_name) {
			this.r56_product_name = r56_product_name;
		}
		public String getR56_cross_reference() {
			return r56_cross_reference;
		}
		public void setR56_cross_reference(String r56_cross_reference) {
			this.r56_cross_reference = r56_cross_reference;
		}
		public BigDecimal getR56_month() {
			return r56_month;
		}
		public void setR56_month(BigDecimal r56_month) {
			this.r56_month = r56_month;
		}
		public BigDecimal getR56_ytd() {
			return r56_ytd;
		}
		public void setR56_ytd(BigDecimal r56_ytd) {
			this.r56_ytd = r56_ytd;
		}
		public String getR57_product_name() {
			return r57_product_name;
		}
		public void setR57_product_name(String r57_product_name) {
			this.r57_product_name = r57_product_name;
		}
		public String getR57_cross_reference() {
			return r57_cross_reference;
		}
		public void setR57_cross_reference(String r57_cross_reference) {
			this.r57_cross_reference = r57_cross_reference;
		}
		public BigDecimal getR57_month() {
			return r57_month;
		}
		public void setR57_month(BigDecimal r57_month) {
			this.r57_month = r57_month;
		}
		public BigDecimal getR57_ytd() {
			return r57_ytd;
		}
		public void setR57_ytd(BigDecimal r57_ytd) {
			this.r57_ytd = r57_ytd;
		}
		public String getR58_product_name() {
			return r58_product_name;
		}
		public void setR58_product_name(String r58_product_name) {
			this.r58_product_name = r58_product_name;
		}
		public String getR58_cross_reference() {
			return r58_cross_reference;
		}
		public void setR58_cross_reference(String r58_cross_reference) {
			this.r58_cross_reference = r58_cross_reference;
		}
		public BigDecimal getR58_ytd() {
			return r58_ytd;
		}
		public void setR58_ytd(BigDecimal r58_ytd) {
			this.r58_ytd = r58_ytd;
		}
		public String getR59_product_name() {
			return r59_product_name;
		}
		public void setR59_product_name(String r59_product_name) {
			this.r59_product_name = r59_product_name;
		}
		public String getR59_cross_reference() {
			return r59_cross_reference;
		}
		public void setR59_cross_reference(String r59_cross_reference) {
			this.r59_cross_reference = r59_cross_reference;
		}
		public BigDecimal getR59_ytd() {
			return r59_ytd;
		}
		public void setR59_ytd(BigDecimal r59_ytd) {
			this.r59_ytd = r59_ytd;
		}
		public String getR60_product_name() {
			return r60_product_name;
		}
		public void setR60_product_name(String r60_product_name) {
			this.r60_product_name = r60_product_name;
		}
		public String getR60_cross_reference() {
			return r60_cross_reference;
		}
		public void setR60_cross_reference(String r60_cross_reference) {
			this.r60_cross_reference = r60_cross_reference;
		}
		public BigDecimal getR60_ytd() {
			return r60_ytd;
		}
		public void setR60_ytd(BigDecimal r60_ytd) {
			this.r60_ytd = r60_ytd;
		}
		public String getR61_product_name() {
			return r61_product_name;
		}
		public void setR61_product_name(String r61_product_name) {
			this.r61_product_name = r61_product_name;
		}
		public String getR61_cross_reference() {
			return r61_cross_reference;
		}
		public void setR61_cross_reference(String r61_cross_reference) {
			this.r61_cross_reference = r61_cross_reference;
		}
		public BigDecimal getR61_month() {
			return r61_month;
		}
		public void setR61_month(BigDecimal r61_month) {
			this.r61_month = r61_month;
		}
		public BigDecimal getR61_ytd() {
			return r61_ytd;
		}
		public void setR61_ytd(BigDecimal r61_ytd) {
			this.r61_ytd = r61_ytd;
		}
		public String getR62_product_name() {
			return r62_product_name;
		}
		public void setR62_product_name(String r62_product_name) {
			this.r62_product_name = r62_product_name;
		}
		public String getR62_cross_reference() {
			return r62_cross_reference;
		}
		public void setR62_cross_reference(String r62_cross_reference) {
			this.r62_cross_reference = r62_cross_reference;
		}
		public BigDecimal getR62_month() {
			return r62_month;
		}
		public void setR62_month(BigDecimal r62_month) {
			this.r62_month = r62_month;
		}
		public BigDecimal getR62_ytd() {
			return r62_ytd;
		}
		public void setR62_ytd(BigDecimal r62_ytd) {
			this.r62_ytd = r62_ytd;
		}
		public String getR63_product_name() {
			return r63_product_name;
		}
		public void setR63_product_name(String r63_product_name) {
			this.r63_product_name = r63_product_name;
		}
		public String getR63_cross_reference() {
			return r63_cross_reference;
		}
		public void setR63_cross_reference(String r63_cross_reference) {
			this.r63_cross_reference = r63_cross_reference;
		}
		public BigDecimal getR63_month() {
			return r63_month;
		}
		public void setR63_month(BigDecimal r63_month) {
			this.r63_month = r63_month;
		}
		public BigDecimal getR63_ytd() {
			return r63_ytd;
		}
		public void setR63_ytd(BigDecimal r63_ytd) {
			this.r63_ytd = r63_ytd;
		}
		public String getR64_product_name() {
			return r64_product_name;
		}
		public void setR64_product_name(String r64_product_name) {
			this.r64_product_name = r64_product_name;
		}
		public String getR64_cross_reference() {
			return r64_cross_reference;
		}
		public void setR64_cross_reference(String r64_cross_reference) {
			this.r64_cross_reference = r64_cross_reference;
		}
		public BigDecimal getR64_month() {
			return r64_month;
		}
		public void setR64_month(BigDecimal r64_month) {
			this.r64_month = r64_month;
		}
		public BigDecimal getR64_ytd() {
			return r64_ytd;
		}
		public void setR64_ytd(BigDecimal r64_ytd) {
			this.r64_ytd = r64_ytd;
		}
		public String getR65_product_name() {
			return r65_product_name;
		}
		public void setR65_product_name(String r65_product_name) {
			this.r65_product_name = r65_product_name;
		}
		public String getR65_cross_reference() {
			return r65_cross_reference;
		}
		public void setR65_cross_reference(String r65_cross_reference) {
			this.r65_cross_reference = r65_cross_reference;
		}
		public BigDecimal getR65_month() {
			return r65_month;
		}
		public void setR65_month(BigDecimal r65_month) {
			this.r65_month = r65_month;
		}
		public BigDecimal getR65_ytd() {
			return r65_ytd;
		}
		public void setR65_ytd(BigDecimal r65_ytd) {
			this.r65_ytd = r65_ytd;
		}
		public String getR66_product_name() {
			return r66_product_name;
		}
		public void setR66_product_name(String r66_product_name) {
			this.r66_product_name = r66_product_name;
		}
		public String getR66_cross_reference() {
			return r66_cross_reference;
		}
		public void setR66_cross_reference(String r66_cross_reference) {
			this.r66_cross_reference = r66_cross_reference;
		}
		public BigDecimal getR66_ytd() {
			return r66_ytd;
		}
		public void setR66_ytd(BigDecimal r66_ytd) {
			this.r66_ytd = r66_ytd;
		}
		public String getR67_product_name() {
			return r67_product_name;
		}
		public void setR67_product_name(String r67_product_name) {
			this.r67_product_name = r67_product_name;
		}
		public String getR67_cross_reference() {
			return r67_cross_reference;
		}
		public void setR67_cross_reference(String r67_cross_reference) {
			this.r67_cross_reference = r67_cross_reference;
		}
		public BigDecimal getR67_ytd() {
			return r67_ytd;
		}
		public void setR67_ytd(BigDecimal r67_ytd) {
			this.r67_ytd = r67_ytd;
		}
		public String getR68_product_name() {
			return r68_product_name;
		}
		public void setR68_product_name(String r68_product_name) {
			this.r68_product_name = r68_product_name;
		}
		public String getR68_cross_reference() {
			return r68_cross_reference;
		}
		public void setR68_cross_reference(String r68_cross_reference) {
			this.r68_cross_reference = r68_cross_reference;
		}
		public BigDecimal getR68_ytd() {
			return r68_ytd;
		}
		public void setR68_ytd(BigDecimal r68_ytd) {
			this.r68_ytd = r68_ytd;
		}
		public String getR69_product_name() {
			return r69_product_name;
		}
		public void setR69_product_name(String r69_product_name) {
			this.r69_product_name = r69_product_name;
		}
		public String getR69_cross_reference() {
			return r69_cross_reference;
		}
		public void setR69_cross_reference(String r69_cross_reference) {
			this.r69_cross_reference = r69_cross_reference;
		}
		public BigDecimal getR69_month() {
			return r69_month;
		}
		public void setR69_month(BigDecimal r69_month) {
			this.r69_month = r69_month;
		}
		public BigDecimal getR69_ytd() {
			return r69_ytd;
		}
		public void setR69_ytd(BigDecimal r69_ytd) {
			this.r69_ytd = r69_ytd;
		}
		public String getR70_product_name() {
			return r70_product_name;
		}
		public void setR70_product_name(String r70_product_name) {
			this.r70_product_name = r70_product_name;
		}
		public String getR70_cross_reference() {
			return r70_cross_reference;
		}
		public void setR70_cross_reference(String r70_cross_reference) {
			this.r70_cross_reference = r70_cross_reference;
		}
		public BigDecimal getR70_month() {
			return r70_month;
		}
		public void setR70_month(BigDecimal r70_month) {
			this.r70_month = r70_month;
		}
		public BigDecimal getR70_ytd() {
			return r70_ytd;
		}
		public void setR70_ytd(BigDecimal r70_ytd) {
			this.r70_ytd = r70_ytd;
		}
		public String getR71_product_name() {
			return r71_product_name;
		}
		public void setR71_product_name(String r71_product_name) {
			this.r71_product_name = r71_product_name;
		}
		public String getR71_cross_reference() {
			return r71_cross_reference;
		}
		public void setR71_cross_reference(String r71_cross_reference) {
			this.r71_cross_reference = r71_cross_reference;
		}
		public BigDecimal getR71_month() {
			return r71_month;
		}
		public void setR71_month(BigDecimal r71_month) {
			this.r71_month = r71_month;
		}
		public BigDecimal getR71_ytd() {
			return r71_ytd;
		}
		public void setR71_ytd(BigDecimal r71_ytd) {
			this.r71_ytd = r71_ytd;
		}
		public String getR72_product_name() {
			return r72_product_name;
		}
		public void setR72_product_name(String r72_product_name) {
			this.r72_product_name = r72_product_name;
		}
		public String getR72_cross_reference() {
			return r72_cross_reference;
		}
		public void setR72_cross_reference(String r72_cross_reference) {
			this.r72_cross_reference = r72_cross_reference;
		}
		public BigDecimal getR72_month() {
			return r72_month;
		}
		public void setR72_month(BigDecimal r72_month) {
			this.r72_month = r72_month;
		}
		public BigDecimal getR72_ytd() {
			return r72_ytd;
		}
		public void setR72_ytd(BigDecimal r72_ytd) {
			this.r72_ytd = r72_ytd;
		}
		public String getR73_product_name() {
			return r73_product_name;
		}
		public void setR73_product_name(String r73_product_name) {
			this.r73_product_name = r73_product_name;
		}
		public String getR73_cross_reference() {
			return r73_cross_reference;
		}
		public void setR73_cross_reference(String r73_cross_reference) {
			this.r73_cross_reference = r73_cross_reference;
		}
		public BigDecimal getR73_month() {
			return r73_month;
		}
		public void setR73_month(BigDecimal r73_month) {
			this.r73_month = r73_month;
		}
		public BigDecimal getR73_ytd() {
			return r73_ytd;
		}
		public void setR73_ytd(BigDecimal r73_ytd) {
			this.r73_ytd = r73_ytd;
		}
		public String getR74_product_name() {
			return r74_product_name;
		}
		public void setR74_product_name(String r74_product_name) {
			this.r74_product_name = r74_product_name;
		}
		public String getR74_cross_reference() {
			return r74_cross_reference;
		}
		public void setR74_cross_reference(String r74_cross_reference) {
			this.r74_cross_reference = r74_cross_reference;
		}
		public BigDecimal getR74_ytd() {
			return r74_ytd;
		}
		public void setR74_ytd(BigDecimal r74_ytd) {
			this.r74_ytd = r74_ytd;
		}
		public String getR75_product_name() {
			return r75_product_name;
		}
		public void setR75_product_name(String r75_product_name) {
			this.r75_product_name = r75_product_name;
		}
		public String getR75_cross_reference() {
			return r75_cross_reference;
		}
		public void setR75_cross_reference(String r75_cross_reference) {
			this.r75_cross_reference = r75_cross_reference;
		}
		public BigDecimal getR75_month() {
			return r75_month;
		}
		public void setR75_month(BigDecimal r75_month) {
			this.r75_month = r75_month;
		}
		public BigDecimal getR75_ytd() {
			return r75_ytd;
		}
		public void setR75_ytd(BigDecimal r75_ytd) {
			this.r75_ytd = r75_ytd;
		}
		public String getR76_product_name() {
			return r76_product_name;
		}
		public void setR76_product_name(String r76_product_name) {
			this.r76_product_name = r76_product_name;
		}
		public String getR76_cross_reference() {
			return r76_cross_reference;
		}
		public void setR76_cross_reference(String r76_cross_reference) {
			this.r76_cross_reference = r76_cross_reference;
		}
		public BigDecimal getR76_month() {
			return r76_month;
		}
		public void setR76_month(BigDecimal r76_month) {
			this.r76_month = r76_month;
		}
		public BigDecimal getR76_ytd() {
			return r76_ytd;
		}
		public void setR76_ytd(BigDecimal r76_ytd) {
			this.r76_ytd = r76_ytd;
		}
		public String getR77_product_name() {
			return r77_product_name;
		}
		public void setR77_product_name(String r77_product_name) {
			this.r77_product_name = r77_product_name;
		}
		public String getR77_cross_reference() {
			return r77_cross_reference;
		}
		public void setR77_cross_reference(String r77_cross_reference) {
			this.r77_cross_reference = r77_cross_reference;
		}
		public BigDecimal getR77_month() {
			return r77_month;
		}
		public void setR77_month(BigDecimal r77_month) {
			this.r77_month = r77_month;
		}
		public BigDecimal getR77_ytd() {
			return r77_ytd;
		}
		public void setR77_ytd(BigDecimal r77_ytd) {
			this.r77_ytd = r77_ytd;
		}
		public String getR78_product_name() {
			return r78_product_name;
		}
		public void setR78_product_name(String r78_product_name) {
			this.r78_product_name = r78_product_name;
		}
		public String getR78_cross_reference() {
			return r78_cross_reference;
		}
		public void setR78_cross_reference(String r78_cross_reference) {
			this.r78_cross_reference = r78_cross_reference;
		}
		public BigDecimal getR78_month() {
			return r78_month;
		}
		public void setR78_month(BigDecimal r78_month) {
			this.r78_month = r78_month;
		}
		public BigDecimal getR78_ytd() {
			return r78_ytd;
		}
		public void setR78_ytd(BigDecimal r78_ytd) {
			this.r78_ytd = r78_ytd;
		}
		public String getR79_product_name() {
			return r79_product_name;
		}
		public void setR79_product_name(String r79_product_name) {
			this.r79_product_name = r79_product_name;
		}
		public String getR79_cross_reference() {
			return r79_cross_reference;
		}
		public void setR79_cross_reference(String r79_cross_reference) {
			this.r79_cross_reference = r79_cross_reference;
		}
		public BigDecimal getR79_month() {
			return r79_month;
		}
		public void setR79_month(BigDecimal r79_month) {
			this.r79_month = r79_month;
		}
		public BigDecimal getR79_ytd() {
			return r79_ytd;
		}
		public void setR79_ytd(BigDecimal r79_ytd) {
			this.r79_ytd = r79_ytd;
		}
		public String getR80_product_name() {
			return r80_product_name;
		}
		public void setR80_product_name(String r80_product_name) {
			this.r80_product_name = r80_product_name;
		}
		public String getR80_cross_reference() {
			return r80_cross_reference;
		}
		public void setR80_cross_reference(String r80_cross_reference) {
			this.r80_cross_reference = r80_cross_reference;
		}
		public BigDecimal getR80_month() {
			return r80_month;
		}
		public void setR80_month(BigDecimal r80_month) {
			this.r80_month = r80_month;
		}
		public BigDecimal getR80_ytd() {
			return r80_ytd;
		}
		public void setR80_ytd(BigDecimal r80_ytd) {
			this.r80_ytd = r80_ytd;
		}
		public String getR81_product_name() {
			return r81_product_name;
		}
		public void setR81_product_name(String r81_product_name) {
			this.r81_product_name = r81_product_name;
		}
		public String getR81_cross_reference() {
			return r81_cross_reference;
		}
		public void setR81_cross_reference(String r81_cross_reference) {
			this.r81_cross_reference = r81_cross_reference;
		}
		public BigDecimal getR81_month() {
			return r81_month;
		}
		public void setR81_month(BigDecimal r81_month) {
			this.r81_month = r81_month;
		}
		public BigDecimal getR81_ytd() {
			return r81_ytd;
		}
		public void setR81_ytd(BigDecimal r81_ytd) {
			this.r81_ytd = r81_ytd;
		}
		public String getR82_product_name() {
			return r82_product_name;
		}
		public void setR82_product_name(String r82_product_name) {
			this.r82_product_name = r82_product_name;
		}
		public String getR82_cross_reference() {
			return r82_cross_reference;
		}
		public void setR82_cross_reference(String r82_cross_reference) {
			this.r82_cross_reference = r82_cross_reference;
		}
		public BigDecimal getR82_month() {
			return r82_month;
		}
		public void setR82_month(BigDecimal r82_month) {
			this.r82_month = r82_month;
		}
		public BigDecimal getR82_ytd() {
			return r82_ytd;
		}
		public void setR82_ytd(BigDecimal r82_ytd) {
			this.r82_ytd = r82_ytd;
		}
		public String getR83_product_name() {
			return r83_product_name;
		}
		public void setR83_product_name(String r83_product_name) {
			this.r83_product_name = r83_product_name;
		}
		public String getR83_cross_reference() {
			return r83_cross_reference;
		}
		public void setR83_cross_reference(String r83_cross_reference) {
			this.r83_cross_reference = r83_cross_reference;
		}
		public BigDecimal getR83_month() {
			return r83_month;
		}
		public void setR83_month(BigDecimal r83_month) {
			this.r83_month = r83_month;
		}
		public BigDecimal getR83_ytd() {
			return r83_ytd;
		}
		public void setR83_ytd(BigDecimal r83_ytd) {
			this.r83_ytd = r83_ytd;
		}
		public String getR84_product_name() {
			return r84_product_name;
		}
		public void setR84_product_name(String r84_product_name) {
			this.r84_product_name = r84_product_name;
		}
		public String getR84_cross_reference() {
			return r84_cross_reference;
		}
		public void setR84_cross_reference(String r84_cross_reference) {
			this.r84_cross_reference = r84_cross_reference;
		}
		public BigDecimal getR84_month() {
			return r84_month;
		}
		public void setR84_month(BigDecimal r84_month) {
			this.r84_month = r84_month;
		}
		public BigDecimal getR84_ytd() {
			return r84_ytd;
		}
		public void setR84_ytd(BigDecimal r84_ytd) {
			this.r84_ytd = r84_ytd;
		}
		public String getR85_product_name() {
			return r85_product_name;
		}
		public void setR85_product_name(String r85_product_name) {
			this.r85_product_name = r85_product_name;
		}
		public String getR85_cross_reference() {
			return r85_cross_reference;
		}
		public void setR85_cross_reference(String r85_cross_reference) {
			this.r85_cross_reference = r85_cross_reference;
		}
		public BigDecimal getR85_ytd() {
			return r85_ytd;
		}
		public void setR85_ytd(BigDecimal r85_ytd) {
			this.r85_ytd = r85_ytd;
		}
		public String getR86_product_name() {
			return r86_product_name;
		}
		public void setR86_product_name(String r86_product_name) {
			this.r86_product_name = r86_product_name;
		}
		public String getR86_cross_reference() {
			return r86_cross_reference;
		}
		public void setR86_cross_reference(String r86_cross_reference) {
			this.r86_cross_reference = r86_cross_reference;
		}
		public BigDecimal getR86_month() {
			return r86_month;
		}
		public void setR86_month(BigDecimal r86_month) {
			this.r86_month = r86_month;
		}
		public BigDecimal getR86_ytd() {
			return r86_ytd;
		}
		public void setR86_ytd(BigDecimal r86_ytd) {
			this.r86_ytd = r86_ytd;
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
		public M_SCI_E_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	
	
   
		
		
	
	 
	

	

}
