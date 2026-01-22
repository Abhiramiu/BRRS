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
@Table(name = "BRRS_BASEL_III_COM_EQUITY_DISC_SUMMARYTABLE")


public class BASEL_III_COM_EQUITY_DISC_Summary_Entity {
	
	

		
	
	private String	r7_product;
	private BigDecimal	r7_amount;
	private String	r8_product;
	private BigDecimal	r8_amount;
	private String	r9_product;
	private BigDecimal	r9_amount;
	private String	r10_product;
	private BigDecimal	r10_amount;
	private String	r11_product;
	private BigDecimal	r11_amount;
	private String	r12_product;
	private BigDecimal	r12_amount;

	private String	r14_product;
	private BigDecimal	r14_amount;
	private String	r15_product;
	private BigDecimal	r15_amount;
	private String	r16_product;
	private BigDecimal	r16_amount;
	private String	r17_product;
	private BigDecimal	r17_amount;
	private String	r18_product;
	private BigDecimal	r18_amount;
	private String	r19_product;
	private BigDecimal	r19_amount;
	private String	r20_product;
	private BigDecimal	r20_amount;
	private String	r21_product;
	private BigDecimal	r21_amount;
	private String	r22_product;
	private BigDecimal	r22_amount;
	private String	r23_product;
	private BigDecimal	r23_amount;
	private String	r24_product;
	private BigDecimal	r24_amount;
	private String	r25_product;
	private BigDecimal	r25_amount;
	private String	r26_product;
	private BigDecimal	r26_amount;
	private String	r27_product;
	private BigDecimal	r27_amount;
	private String	r28_product;
	private BigDecimal	r28_amount;
	private String	r29_product;
	private BigDecimal	r29_amount;
	private String	r30_product;
	private BigDecimal	r30_amount;
	private String	r31_product;
	private BigDecimal	r31_amount;
	private String	r32_product;
	private BigDecimal	r32_amount;
	private String	r33_product;
	private BigDecimal	r33_amount;
	private String	r34_product;
	private BigDecimal	r34_amount;
	private String	r35_product;
	private BigDecimal	r35_amount;
	private String	r36_product;
	private BigDecimal	r36_amount;

	private String	r38_product;
	private BigDecimal	r38_amount;
	private String	r39_product;
	private BigDecimal	r39_amount;
	private String	r40_product;
	private BigDecimal	r40_amount;
	private String	r41_product;
	private BigDecimal	r41_amount;
	private String	r42_product;
	private BigDecimal	r42_amount;
	private String	r43_product;
	private BigDecimal	r43_amount;
	private String	r44_product;
	private BigDecimal	r44_amount;

	private String	r46_product;
	private BigDecimal	r46_amount;
	private String	r47_product;
	private BigDecimal	r47_amount;
	private String	r48_product;
	private BigDecimal	r48_amount;
	private String	r49_product;
	private BigDecimal	r49_amount;
	private String	r50_product;
	private BigDecimal	r50_amount;
	private String	r51_product;
	private BigDecimal	r51_amount;
	private String	r52_product;
	private BigDecimal	r52_amount;
	private String	r53_product;
	private BigDecimal	r53_amount;
	private String	r54_product;
	private BigDecimal	r54_amount;
	private String	r55_product;
	private BigDecimal	r55_amount;

	private String	r57_product;
	private BigDecimal	r57_amount;
	private String	r58_product;
	private BigDecimal	r58_amount;
	private String	r59_product;
	private BigDecimal	r59_amount;
	private String	r60_product;
	private BigDecimal	r60_amount;
	private String	r61_product;
	private BigDecimal	r61_amount;
	private String	r62_product;
	private BigDecimal	r62_amount;

	private String	r64_product;
	private BigDecimal	r64_amount;
	private String	r65_product;
	private BigDecimal	r65_amount;
	private String	r66_product;
	private BigDecimal	r66_amount;
	private String	r67_product;
	private BigDecimal	r67_amount;
	private String	r68_product;
	private BigDecimal	r68_amount;
	private String	r69_product;
	private BigDecimal	r69_amount;
	private String	r70_product;
	private BigDecimal	r70_amount;
	private String	r71_product;
	private BigDecimal	r71_amount;
	private String	r72_product;
	private BigDecimal	r72_amount;
	private String	r73_product;
	private BigDecimal	r73_amount;

	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
	
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_amount() {
		return r7_amount;
	}
	public void setR7_amount(BigDecimal r7_amount) {
		this.r7_amount = r7_amount;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_amount() {
		return r8_amount;
	}
	public void setR8_amount(BigDecimal r8_amount) {
		this.r8_amount = r8_amount;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amount() {
		return r9_amount;
	}
	public void setR9_amount(BigDecimal r9_amount) {
		this.r9_amount = r9_amount;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amount() {
		return r10_amount;
	}
	public void setR10_amount(BigDecimal r10_amount) {
		this.r10_amount = r10_amount;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_amount() {
		return r11_amount;
	}
	public void setR11_amount(BigDecimal r11_amount) {
		this.r11_amount = r11_amount;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_amount() {
		return r12_amount;
	}
	public void setR12_amount(BigDecimal r12_amount) {
		this.r12_amount = r12_amount;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_amount() {
		return r14_amount;
	}
	public void setR14_amount(BigDecimal r14_amount) {
		this.r14_amount = r14_amount;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_amount() {
		return r15_amount;
	}
	public void setR15_amount(BigDecimal r15_amount) {
		this.r15_amount = r15_amount;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_amount() {
		return r16_amount;
	}
	public void setR16_amount(BigDecimal r16_amount) {
		this.r16_amount = r16_amount;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_amount() {
		return r17_amount;
	}
	public void setR17_amount(BigDecimal r17_amount) {
		this.r17_amount = r17_amount;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_amount() {
		return r18_amount;
	}
	public void setR18_amount(BigDecimal r18_amount) {
		this.r18_amount = r18_amount;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_amount() {
		return r19_amount;
	}
	public void setR19_amount(BigDecimal r19_amount) {
		this.r19_amount = r19_amount;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_amount() {
		return r20_amount;
	}
	public void setR20_amount(BigDecimal r20_amount) {
		this.r20_amount = r20_amount;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_amount() {
		return r21_amount;
	}
	public void setR21_amount(BigDecimal r21_amount) {
		this.r21_amount = r21_amount;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_amount() {
		return r22_amount;
	}
	public void setR22_amount(BigDecimal r22_amount) {
		this.r22_amount = r22_amount;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_amount() {
		return r23_amount;
	}
	public void setR23_amount(BigDecimal r23_amount) {
		this.r23_amount = r23_amount;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_amount() {
		return r24_amount;
	}
	public void setR24_amount(BigDecimal r24_amount) {
		this.r24_amount = r24_amount;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_amount() {
		return r25_amount;
	}
	public void setR25_amount(BigDecimal r25_amount) {
		this.r25_amount = r25_amount;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_amount() {
		return r26_amount;
	}
	public void setR26_amount(BigDecimal r26_amount) {
		this.r26_amount = r26_amount;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_amount() {
		return r27_amount;
	}
	public void setR27_amount(BigDecimal r27_amount) {
		this.r27_amount = r27_amount;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_amount() {
		return r28_amount;
	}
	public void setR28_amount(BigDecimal r28_amount) {
		this.r28_amount = r28_amount;
	}
	public String getR29_product() {
		return r29_product;
	}
	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}
	public BigDecimal getR29_amount() {
		return r29_amount;
	}
	public void setR29_amount(BigDecimal r29_amount) {
		this.r29_amount = r29_amount;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_amount() {
		return r30_amount;
	}
	public void setR30_amount(BigDecimal r30_amount) {
		this.r30_amount = r30_amount;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_amount() {
		return r31_amount;
	}
	public void setR31_amount(BigDecimal r31_amount) {
		this.r31_amount = r31_amount;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_amount() {
		return r32_amount;
	}
	public void setR32_amount(BigDecimal r32_amount) {
		this.r32_amount = r32_amount;
	}
	public String getR33_product() {
		return r33_product;
	}
	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}
	public BigDecimal getR33_amount() {
		return r33_amount;
	}
	public void setR33_amount(BigDecimal r33_amount) {
		this.r33_amount = r33_amount;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_amount() {
		return r34_amount;
	}
	public void setR34_amount(BigDecimal r34_amount) {
		this.r34_amount = r34_amount;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_amount() {
		return r35_amount;
	}
	public void setR35_amount(BigDecimal r35_amount) {
		this.r35_amount = r35_amount;
	}
	public String getR36_product() {
		return r36_product;
	}
	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}
	public BigDecimal getR36_amount() {
		return r36_amount;
	}
	public void setR36_amount(BigDecimal r36_amount) {
		this.r36_amount = r36_amount;
	}
	public String getR38_product() {
		return r38_product;
	}
	public void setR38_product(String r38_product) {
		this.r38_product = r38_product;
	}
	public BigDecimal getR38_amount() {
		return r38_amount;
	}
	public void setR38_amount(BigDecimal r38_amount) {
		this.r38_amount = r38_amount;
	}
	public String getR39_product() {
		return r39_product;
	}
	public void setR39_product(String r39_product) {
		this.r39_product = r39_product;
	}
	public BigDecimal getR39_amount() {
		return r39_amount;
	}
	public void setR39_amount(BigDecimal r39_amount) {
		this.r39_amount = r39_amount;
	}
	public String getR40_product() {
		return r40_product;
	}
	public void setR40_product(String r40_product) {
		this.r40_product = r40_product;
	}
	public BigDecimal getR40_amount() {
		return r40_amount;
	}
	public void setR40_amount(BigDecimal r40_amount) {
		this.r40_amount = r40_amount;
	}
	public String getR41_product() {
		return r41_product;
	}
	public void setR41_product(String r41_product) {
		this.r41_product = r41_product;
	}
	public BigDecimal getR41_amount() {
		return r41_amount;
	}
	public void setR41_amount(BigDecimal r41_amount) {
		this.r41_amount = r41_amount;
	}
	public String getR42_product() {
		return r42_product;
	}
	public void setR42_product(String r42_product) {
		this.r42_product = r42_product;
	}
	public BigDecimal getR42_amount() {
		return r42_amount;
	}
	public void setR42_amount(BigDecimal r42_amount) {
		this.r42_amount = r42_amount;
	}
	public String getR43_product() {
		return r43_product;
	}
	public void setR43_product(String r43_product) {
		this.r43_product = r43_product;
	}
	public BigDecimal getR43_amount() {
		return r43_amount;
	}
	public void setR43_amount(BigDecimal r43_amount) {
		this.r43_amount = r43_amount;
	}
	public String getR44_product() {
		return r44_product;
	}
	public void setR44_product(String r44_product) {
		this.r44_product = r44_product;
	}
	public BigDecimal getR44_amount() {
		return r44_amount;
	}
	public void setR44_amount(BigDecimal r44_amount) {
		this.r44_amount = r44_amount;
	}
	public String getR46_product() {
		return r46_product;
	}
	public void setR46_product(String r46_product) {
		this.r46_product = r46_product;
	}
	public BigDecimal getR46_amount() {
		return r46_amount;
	}
	public void setR46_amount(BigDecimal r46_amount) {
		this.r46_amount = r46_amount;
	}
	public String getR47_product() {
		return r47_product;
	}
	public void setR47_product(String r47_product) {
		this.r47_product = r47_product;
	}
	public BigDecimal getR47_amount() {
		return r47_amount;
	}
	public void setR47_amount(BigDecimal r47_amount) {
		this.r47_amount = r47_amount;
	}
	public String getR48_product() {
		return r48_product;
	}
	public void setR48_product(String r48_product) {
		this.r48_product = r48_product;
	}
	public BigDecimal getR48_amount() {
		return r48_amount;
	}
	public void setR48_amount(BigDecimal r48_amount) {
		this.r48_amount = r48_amount;
	}
	public String getR49_product() {
		return r49_product;
	}
	public void setR49_product(String r49_product) {
		this.r49_product = r49_product;
	}
	public BigDecimal getR49_amount() {
		return r49_amount;
	}
	public void setR49_amount(BigDecimal r49_amount) {
		this.r49_amount = r49_amount;
	}
	public String getR50_product() {
		return r50_product;
	}
	public void setR50_product(String r50_product) {
		this.r50_product = r50_product;
	}
	public BigDecimal getR50_amount() {
		return r50_amount;
	}
	public void setR50_amount(BigDecimal r50_amount) {
		this.r50_amount = r50_amount;
	}
	public String getR51_product() {
		return r51_product;
	}
	public void setR51_product(String r51_product) {
		this.r51_product = r51_product;
	}
	public BigDecimal getR51_amount() {
		return r51_amount;
	}
	public void setR51_amount(BigDecimal r51_amount) {
		this.r51_amount = r51_amount;
	}
	public String getR52_product() {
		return r52_product;
	}
	public void setR52_product(String r52_product) {
		this.r52_product = r52_product;
	}
	public BigDecimal getR52_amount() {
		return r52_amount;
	}
	public void setR52_amount(BigDecimal r52_amount) {
		this.r52_amount = r52_amount;
	}
	public String getR53_product() {
		return r53_product;
	}
	public void setR53_product(String r53_product) {
		this.r53_product = r53_product;
	}
	public BigDecimal getR53_amount() {
		return r53_amount;
	}
	public void setR53_amount(BigDecimal r53_amount) {
		this.r53_amount = r53_amount;
	}
	public String getR54_product() {
		return r54_product;
	}
	public void setR54_product(String r54_product) {
		this.r54_product = r54_product;
	}
	public BigDecimal getR54_amount() {
		return r54_amount;
	}
	public void setR54_amount(BigDecimal r54_amount) {
		this.r54_amount = r54_amount;
	}
	public String getR55_product() {
		return r55_product;
	}
	public void setR55_product(String r55_product) {
		this.r55_product = r55_product;
	}
	public BigDecimal getR55_amount() {
		return r55_amount;
	}
	public void setR55_amount(BigDecimal r55_amount) {
		this.r55_amount = r55_amount;
	}
	public String getR57_product() {
		return r57_product;
	}
	public void setR57_product(String r57_product) {
		this.r57_product = r57_product;
	}
	public BigDecimal getR57_amount() {
		return r57_amount;
	}
	public void setR57_amount(BigDecimal r57_amount) {
		this.r57_amount = r57_amount;
	}
	public String getR58_product() {
		return r58_product;
	}
	public void setR58_product(String r58_product) {
		this.r58_product = r58_product;
	}
	public BigDecimal getR58_amount() {
		return r58_amount;
	}
	public void setR58_amount(BigDecimal r58_amount) {
		this.r58_amount = r58_amount;
	}
	public String getR59_product() {
		return r59_product;
	}
	public void setR59_product(String r59_product) {
		this.r59_product = r59_product;
	}
	public BigDecimal getR59_amount() {
		return r59_amount;
	}
	public void setR59_amount(BigDecimal r59_amount) {
		this.r59_amount = r59_amount;
	}
	public String getR60_product() {
		return r60_product;
	}
	public void setR60_product(String r60_product) {
		this.r60_product = r60_product;
	}
	public BigDecimal getR60_amount() {
		return r60_amount;
	}
	public void setR60_amount(BigDecimal r60_amount) {
		this.r60_amount = r60_amount;
	}
	public String getR61_product() {
		return r61_product;
	}
	public void setR61_product(String r61_product) {
		this.r61_product = r61_product;
	}
	public BigDecimal getR61_amount() {
		return r61_amount;
	}
	public void setR61_amount(BigDecimal r61_amount) {
		this.r61_amount = r61_amount;
	}
	public String getR62_product() {
		return r62_product;
	}
	public void setR62_product(String r62_product) {
		this.r62_product = r62_product;
	}
	public BigDecimal getR62_amount() {
		return r62_amount;
	}
	public void setR62_amount(BigDecimal r62_amount) {
		this.r62_amount = r62_amount;
	}
	public String getR64_product() {
		return r64_product;
	}
	public void setR64_product(String r64_product) {
		this.r64_product = r64_product;
	}
	public BigDecimal getR64_amount() {
		return r64_amount;
	}
	public void setR64_amount(BigDecimal r64_amount) {
		this.r64_amount = r64_amount;
	}
	public String getR65_product() {
		return r65_product;
	}
	public void setR65_product(String r65_product) {
		this.r65_product = r65_product;
	}
	public BigDecimal getR65_amount() {
		return r65_amount;
	}
	public void setR65_amount(BigDecimal r65_amount) {
		this.r65_amount = r65_amount;
	}
	public String getR66_product() {
		return r66_product;
	}
	public void setR66_product(String r66_product) {
		this.r66_product = r66_product;
	}
	public BigDecimal getR66_amount() {
		return r66_amount;
	}
	public void setR66_amount(BigDecimal r66_amount) {
		this.r66_amount = r66_amount;
	}
	public String getR67_product() {
		return r67_product;
	}
	public void setR67_product(String r67_product) {
		this.r67_product = r67_product;
	}
	public BigDecimal getR67_amount() {
		return r67_amount;
	}
	public void setR67_amount(BigDecimal r67_amount) {
		this.r67_amount = r67_amount;
	}
	public String getR68_product() {
		return r68_product;
	}
	public void setR68_product(String r68_product) {
		this.r68_product = r68_product;
	}
	public BigDecimal getR68_amount() {
		return r68_amount;
	}
	public void setR68_amount(BigDecimal r68_amount) {
		this.r68_amount = r68_amount;
	}
	public String getR69_product() {
		return r69_product;
	}
	public void setR69_product(String r69_product) {
		this.r69_product = r69_product;
	}
	public BigDecimal getR69_amount() {
		return r69_amount;
	}
	public void setR69_amount(BigDecimal r69_amount) {
		this.r69_amount = r69_amount;
	}
	public String getR70_product() {
		return r70_product;
	}
	public void setR70_product(String r70_product) {
		this.r70_product = r70_product;
	}
	public BigDecimal getR70_amount() {
		return r70_amount;
	}
	public void setR70_amount(BigDecimal r70_amount) {
		this.r70_amount = r70_amount;
	}
	public String getR71_product() {
		return r71_product;
	}
	public void setR71_product(String r71_product) {
		this.r71_product = r71_product;
	}
	public BigDecimal getR71_amount() {
		return r71_amount;
	}
	public void setR71_amount(BigDecimal r71_amount) {
		this.r71_amount = r71_amount;
	}
	public String getR72_product() {
		return r72_product;
	}
	public void setR72_product(String r72_product) {
		this.r72_product = r72_product;
	}
	public BigDecimal getR72_amount() {
		return r72_amount;
	}
	public void setR72_amount(BigDecimal r72_amount) {
		this.r72_amount = r72_amount;
	}
	public String getR73_product() {
		return r73_product;
	}
	public void setR73_product(String r73_product) {
		this.r73_product = r73_product;
	}
	public BigDecimal getR73_amount() {
		return r73_amount;
	}
	public void setR73_amount(BigDecimal r73_amount) {
		this.r73_amount = r73_amount;
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
	public BASEL_III_COM_EQUITY_DISC_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
}
