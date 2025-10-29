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
@Table(name = "BRRS_M_CA2_ARCHIVALTABLE_SUMMARY")

public class M_CA2_Archival_Summary_Entity {
	private String r10_product;
private BigDecimal r10_amount_1;
private BigDecimal r10_amount_2;
private String r11_product;
private BigDecimal r11_amount_1;
//private BigDecimal r11_amount_2;
private String r12_product;
private BigDecimal r12_amount_1;
private BigDecimal r12_amount_2;
private String r13_product;
//private BigDecimal r13_amount_1;
private BigDecimal r13_amount_2;
private String r14_product;
//private BigDecimal r14_amount_1;
private BigDecimal r14_amount_2;
private String r15_product;
//private BigDecimal r15_amount_1;
private BigDecimal r15_amount_2;
private String r16_product;
//private BigDecimal r16_amount_1;
private BigDecimal r16_amount_2;
private String r17_product;
private BigDecimal r17_amount_1;
private BigDecimal r17_amount_2;
private String r18_product;
//private BigDecimal r18_amount_1;
private BigDecimal r18_amount_2;
private String r19_product;
//private BigDecimal r19_amount_1;
private BigDecimal r19_amount_2;
private String r20_product;
private BigDecimal r20_amount_1;
private BigDecimal r20_amount_2;
private String r21_product;
//private BigDecimal r21_amount_1;
private BigDecimal r21_amount_2;
private String r22_product;
private BigDecimal r22_amount_1;
private BigDecimal r22_amount_2;
private String r23_product;
private BigDecimal r23_amount_1;
private BigDecimal r23_amount_2;
private String r24_product;
private BigDecimal r24_amount_1;
private BigDecimal r24_amount_2;
private String r25_product;
private BigDecimal r25_amount_1;
private BigDecimal r25_amount_2;
private String r26_product;
private BigDecimal r26_amount_1;
private BigDecimal r26_amount_2;
private String r31_product;
private BigDecimal r31_amount_1;
private BigDecimal r31_amount_2;
private String r32_product;
private BigDecimal r32_amount_1;
//private BigDecimal r32_amount_2;
private String r33_product;
private BigDecimal r33_amount_1;
private BigDecimal r33_amount_2;
private String r34_product;
private BigDecimal r34_amount_1;
private BigDecimal r34_amount_2;
private String r35_product;
private BigDecimal r35_amount_1;
private BigDecimal r35_amount_2;
private String r36_product;
private BigDecimal r36_amount_1;
private BigDecimal r36_amount_2;
private String r41_product;
private BigDecimal r41_amount_1;
private BigDecimal r41_amount_2;
private String r42_product;
private BigDecimal r42_amount_1;
//private BigDecimal r42_amount_2;
private String r43_product;
private BigDecimal r43_amount_1;
//private BigDecimal r43_amount_2;
private String r44_product;
private BigDecimal r44_amount_1;
//private BigDecimal r44_amount_2;
private String r45_product;
private BigDecimal r45_amount_1;
private BigDecimal r45_amount_2;
private String r46_product;
private BigDecimal r46_amount_1;
//private BigDecimal r46_amount_2;
private String r47_product;
private BigDecimal r47_amount_1;
private BigDecimal r47_amount_2;
private String r48_product;
private BigDecimal r48_amount_1;
private BigDecimal r48_amount_2;
private String r49_product;
private BigDecimal r49_amount_1;
private BigDecimal r49_amount_2;

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
	
	
		




	public String getR10_product() {
		return r10_product;
	}







	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}







	public BigDecimal getR10_amount_1() {
		return r10_amount_1;
	}







	public void setR10_amount_1(BigDecimal r10_amount_1) {
		this.r10_amount_1 = r10_amount_1;
	}







	public BigDecimal getR10_amount_2() {
		return r10_amount_2;
	}







	public void setR10_amount_2(BigDecimal r10_amount_2) {
		this.r10_amount_2 = r10_amount_2;
	}







	public String getR11_product() {
		return r11_product;
	}







	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}







	public BigDecimal getR11_amount_1() {
		return r11_amount_1;
	}







	public void setR11_amount_1(BigDecimal r11_amount_1) {
		this.r11_amount_1 = r11_amount_1;
	}






//
//	public BigDecimal getR11_amount_2() {
//		return r11_amount_2;
//	}
//
//
//
//
//
//
//
//	public void setR11_amount_2(BigDecimal r11_amount_2) {
//		this.r11_amount_2 = r11_amount_2;
//	}







	public String getR12_product() {
		return r12_product;
	}







	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}







	public BigDecimal getR12_amount_1() {
		return r12_amount_1;
	}







	public void setR12_amount_1(BigDecimal r12_amount_1) {
		this.r12_amount_1 = r12_amount_1;
	}







	public BigDecimal getR12_amount_2() {
		return r12_amount_2;
	}







	public void setR12_amount_2(BigDecimal r12_amount_2) {
		this.r12_amount_2 = r12_amount_2;
	}







	public String getR13_product() {
		return r13_product;
	}







	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}






//
//	public BigDecimal getR13_amount_1() {
//		return r13_amount_1;
//	}
//
//
//
//
//
//
//
//	public void setR13_amount_1(BigDecimal r13_amount_1) {
//		this.r13_amount_1 = r13_amount_1;
//	}







	public BigDecimal getR13_amount_2() {
		return r13_amount_2;
	}







	public void setR13_amount_2(BigDecimal r13_amount_2) {
		this.r13_amount_2 = r13_amount_2;
	}







	public String getR14_product() {
		return r14_product;
	}







	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}






//
//	public BigDecimal getR14_amount_1() {
//		return r14_amount_1;
//	}
//
//
//
//
//
//
//
//	public void setR14_amount_1(BigDecimal r14_amount_1) {
//		this.r14_amount_1 = r14_amount_1;
//	}
//






	public BigDecimal getR14_amount_2() {
		return r14_amount_2;
	}







	public void setR14_amount_2(BigDecimal r14_amount_2) {
		this.r14_amount_2 = r14_amount_2;
	}







	public String getR15_product() {
		return r15_product;
	}







	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}






//
//	public BigDecimal getR15_amount_1() {
//		return r15_amount_1;
//	}
//
//
//
//
//
//
//
//	public void setR15_amount_1(BigDecimal r15_amount_1) {
//		this.r15_amount_1 = r15_amount_1;
//	}







	public BigDecimal getR15_amount_2() {
		return r15_amount_2;
	}







	public void setR15_amount_2(BigDecimal r15_amount_2) {
		this.r15_amount_2 = r15_amount_2;
	}







	public String getR16_product() {
		return r16_product;
	}







	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}





//
//
//	public BigDecimal getR16_amount_1() {
//		return r16_amount_1;
//	}
//
//
//
//
//
//
//
//	public void setR16_amount_1(BigDecimal r16_amount_1) {
//		this.r16_amount_1 = r16_amount_1;
//	}
//
//
//
//



	public BigDecimal getR16_amount_2() {
		return r16_amount_2;
	}







	public void setR16_amount_2(BigDecimal r16_amount_2) {
		this.r16_amount_2 = r16_amount_2;
	}







	public String getR17_product() {
		return r17_product;
	}







	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}







	public BigDecimal getR17_amount_1() {
		return r17_amount_1;
	}







	public void setR17_amount_1(BigDecimal r17_amount_1) {
		this.r17_amount_1 = r17_amount_1;
	}







	public BigDecimal getR17_amount_2() {
		return r17_amount_2;
	}







	public void setR17_amount_2(BigDecimal r17_amount_2) {
		this.r17_amount_2 = r17_amount_2;
	}







	public String getR18_product() {
		return r18_product;
	}







	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}





//
//
//	public BigDecimal getR18_amount_1() {
//		return r18_amount_1;
//	}
//
//
//
//
//
//
//
//	public void setR18_amount_1(BigDecimal r18_amount_1) {
//		this.r18_amount_1 = r18_amount_1;
//	}







	public BigDecimal getR18_amount_2() {
		return r18_amount_2;
	}







	public void setR18_amount_2(BigDecimal r18_amount_2) {
		this.r18_amount_2 = r18_amount_2;
	}







	public String getR19_product() {
		return r19_product;
	}







	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}






//
//	public BigDecimal getR19_amount_1() {
//		return r19_amount_1;
//	}
//
//
//
//
//
//
//
//	public void setR19_amount_1(BigDecimal r19_amount_1) {
//		this.r19_amount_1 = r19_amount_1;
//	}
//
//
//




	public BigDecimal getR19_amount_2() {
		return r19_amount_2;
	}







	public void setR19_amount_2(BigDecimal r19_amount_2) {
		this.r19_amount_2 = r19_amount_2;
	}







	public String getR20_product() {
		return r20_product;
	}







	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}







	public BigDecimal getR20_amount_1() {
		return r20_amount_1;
	}







	public void setR20_amount_1(BigDecimal r20_amount_1) {
		this.r20_amount_1 = r20_amount_1;
	}







	public BigDecimal getR20_amount_2() {
		return r20_amount_2;
	}







	public void setR20_amount_2(BigDecimal r20_amount_2) {
		this.r20_amount_2 = r20_amount_2;
	}







	public String getR21_product() {
		return r21_product;
	}







	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}







//	public BigDecimal getR21_amount_1() {
//		return r21_amount_1;
//	}
//
//
//
//
//

//
//	public void setR21_amount_1(BigDecimal r21_amount_1) {
//		this.r21_amount_1 = r21_amount_1;
//	}
//
//
//




	public BigDecimal getR21_amount_2() {
		return r21_amount_2;
	}







	public void setR21_amount_2(BigDecimal r21_amount_2) {
		this.r21_amount_2 = r21_amount_2;
	}







	public String getR22_product() {
		return r22_product;
	}







	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}







	public BigDecimal getR22_amount_1() {
		return r22_amount_1;
	}







	public void setR22_amount_1(BigDecimal r22_amount_1) {
		this.r22_amount_1 = r22_amount_1;
	}







	public BigDecimal getR22_amount_2() {
		return r22_amount_2;
	}







	public void setR22_amount_2(BigDecimal r22_amount_2) {
		this.r22_amount_2 = r22_amount_2;
	}







	public String getR23_product() {
		return r23_product;
	}







	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}







	public BigDecimal getR23_amount_1() {
		return r23_amount_1;
	}







	public void setR23_amount_1(BigDecimal r23_amount_1) {
		this.r23_amount_1 = r23_amount_1;
	}







	public BigDecimal getR23_amount_2() {
		return r23_amount_2;
	}







	public void setR23_amount_2(BigDecimal r23_amount_2) {
		this.r23_amount_2 = r23_amount_2;
	}







	public String getR24_product() {
		return r24_product;
	}







	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}







	public BigDecimal getR24_amount_1() {
		return r24_amount_1;
	}







	public void setR24_amount_1(BigDecimal r24_amount_1) {
		this.r24_amount_1 = r24_amount_1;
	}







	public BigDecimal getR24_amount_2() {
		return r24_amount_2;
	}







	public void setR24_amount_2(BigDecimal r24_amount_2) {
		this.r24_amount_2 = r24_amount_2;
	}







	public String getR25_product() {
		return r25_product;
	}







	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}







	public BigDecimal getR25_amount_1() {
		return r25_amount_1;
	}







	public void setR25_amount_1(BigDecimal r25_amount_1) {
		this.r25_amount_1 = r25_amount_1;
	}







	public BigDecimal getR25_amount_2() {
		return r25_amount_2;
	}







	public void setR25_amount_2(BigDecimal r25_amount_2) {
		this.r25_amount_2 = r25_amount_2;
	}







	public String getR26_product() {
		return r26_product;
	}







	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}







	public BigDecimal getR26_amount_1() {
		return r26_amount_1;
	}







	public void setR26_amount_1(BigDecimal r26_amount_1) {
		this.r26_amount_1 = r26_amount_1;
	}







	public BigDecimal getR26_amount_2() {
		return r26_amount_2;
	}







	public void setR26_amount_2(BigDecimal r26_amount_2) {
		this.r26_amount_2 = r26_amount_2;
	}







	public String getR31_product() {
		return r31_product;
	}







	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}







	public BigDecimal getR31_amount_1() {
		return r31_amount_1;
	}







	public void setR31_amount_1(BigDecimal r31_amount_1) {
		this.r31_amount_1 = r31_amount_1;
	}







	public BigDecimal getR31_amount_2() {
		return r31_amount_2;
	}







	public void setR31_amount_2(BigDecimal r31_amount_2) {
		this.r31_amount_2 = r31_amount_2;
	}







	public String getR32_product() {
		return r32_product;
	}







	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}







	public BigDecimal getR32_amount_1() {
		return r32_amount_1;
	}







	public void setR32_amount_1(BigDecimal r32_amount_1) {
		this.r32_amount_1 = r32_amount_1;
	}





//
//
//	public BigDecimal getR32_amount_2() {
//		return r32_amount_2;
//	}






//
//	public void setR32_amount_2(BigDecimal r32_amount_2) {
//		this.r32_amount_2 = r32_amount_2;
//	}
//
//
//
//



	public String getR33_product() {
		return r33_product;
	}







	public void setR33_product(String r33_product) {
		this.r33_product = r33_product;
	}







	public BigDecimal getR33_amount_1() {
		return r33_amount_1;
	}







	public void setR33_amount_1(BigDecimal r33_amount_1) {
		this.r33_amount_1 = r33_amount_1;
	}







	public BigDecimal getR33_amount_2() {
		return r33_amount_2;
	}







	public void setR33_amount_2(BigDecimal r33_amount_2) {
		this.r33_amount_2 = r33_amount_2;
	}







	public String getR34_product() {
		return r34_product;
	}







	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}







	public BigDecimal getR34_amount_1() {
		return r34_amount_1;
	}







	public void setR34_amount_1(BigDecimal r34_amount_1) {
		this.r34_amount_1 = r34_amount_1;
	}







	public BigDecimal getR34_amount_2() {
		return r34_amount_2;
	}







	public void setR34_amount_2(BigDecimal r34_amount_2) {
		this.r34_amount_2 = r34_amount_2;
	}







	public String getR35_product() {
		return r35_product;
	}







	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}







	public BigDecimal getR35_amount_1() {
		return r35_amount_1;
	}







	public void setR35_amount_1(BigDecimal r35_amount_1) {
		this.r35_amount_1 = r35_amount_1;
	}







	public BigDecimal getR35_amount_2() {
		return r35_amount_2;
	}







	public void setR35_amount_2(BigDecimal r35_amount_2) {
		this.r35_amount_2 = r35_amount_2;
	}







	public String getR36_product() {
		return r36_product;
	}







	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}







	public BigDecimal getR36_amount_1() {
		return r36_amount_1;
	}







	public void setR36_amount_1(BigDecimal r36_amount_1) {
		this.r36_amount_1 = r36_amount_1;
	}







	public BigDecimal getR36_amount_2() {
		return r36_amount_2;
	}







	public void setR36_amount_2(BigDecimal r36_amount_2) {
		this.r36_amount_2 = r36_amount_2;
	}







	public String getR41_product() {
		return r41_product;
	}







	public void setR41_product(String r41_product) {
		this.r41_product = r41_product;
	}







	public BigDecimal getR41_amount_1() {
		return r41_amount_1;
	}







	public void setR41_amount_1(BigDecimal r41_amount_1) {
		this.r41_amount_1 = r41_amount_1;
	}







	public BigDecimal getR41_amount_2() {
		return r41_amount_2;
	}







	public void setR41_amount_2(BigDecimal r41_amount_2) {
		this.r41_amount_2 = r41_amount_2;
	}







	public String getR42_product() {
		return r42_product;
	}







	public void setR42_product(String r42_product) {
		this.r42_product = r42_product;
	}







	public BigDecimal getR42_amount_1() {
		return r42_amount_1;
	}







	public void setR42_amount_1(BigDecimal r42_amount_1) {
		this.r42_amount_1 = r42_amount_1;
	}






//
//	public BigDecimal getR42_amount_2() {
//		return r42_amount_2;
//	}
//
//
//
//
//
//
//
//	public void setR42_amount_2(BigDecimal r42_amount_2) {
//		this.r42_amount_2 = r42_amount_2;
//	}







	public String getR43_product() {
		return r43_product;
	}







	public void setR43_product(String r43_product) {
		this.r43_product = r43_product;
	}







	public BigDecimal getR43_amount_1() {
		return r43_amount_1;
	}







	public void setR43_amount_1(BigDecimal r43_amount_1) {
		this.r43_amount_1 = r43_amount_1;
	}






//
//	public BigDecimal getR43_amount_2() {
//		return r43_amount_2;
//	}
//
//
//
//
//

//
//	public void setR43_amount_2(BigDecimal r43_amount_2) {
//		this.r43_amount_2 = r43_amount_2;
//	}
//
//
//
//
//


	public String getR44_product() {
		return r44_product;
	}







	public void setR44_product(String r44_product) {
		this.r44_product = r44_product;
	}







	public BigDecimal getR44_amount_1() {
		return r44_amount_1;
	}







	public void setR44_amount_1(BigDecimal r44_amount_1) {
		this.r44_amount_1 = r44_amount_1;
	}





//
//
//	public BigDecimal getR44_amount_2() {
//		return r44_amount_2;
//	}
//
//
//
//
//
//
//
//	public void setR44_amount_2(BigDecimal r44_amount_2) {
//		this.r44_amount_2 = r44_amount_2;
//	}
//






	public String getR45_product() {
		return r45_product;
	}







	public void setR45_product(String r45_product) {
		this.r45_product = r45_product;
	}







	public BigDecimal getR45_amount_1() {
		return r45_amount_1;
	}







	public void setR45_amount_1(BigDecimal r45_amount_1) {
		this.r45_amount_1 = r45_amount_1;
	}







	public BigDecimal getR45_amount_2() {
		return r45_amount_2;
	}







	public void setR45_amount_2(BigDecimal r45_amount_2) {
		this.r45_amount_2 = r45_amount_2;
	}







	public String getR46_product() {
		return r46_product;
	}







	public void setR46_product(String r46_product) {
		this.r46_product = r46_product;
	}







	public BigDecimal getR46_amount_1() {
		return r46_amount_1;
	}







	public void setR46_amount_1(BigDecimal r46_amount_1) {
		this.r46_amount_1 = r46_amount_1;
	}





//
//
//	public BigDecimal getR46_amount_2() {
//		return r46_amount_2;
//	}
//
//
//
//
//
//
//
//	public void setR46_amount_2(BigDecimal r46_amount_2) {
//		this.r46_amount_2 = r46_amount_2;
//	}
//






	public String getR47_product() {
		return r47_product;
	}







	public void setR47_product(String r47_product) {
		this.r47_product = r47_product;
	}







	public BigDecimal getR47_amount_1() {
		return r47_amount_1;
	}







	public void setR47_amount_1(BigDecimal r47_amount_1) {
		this.r47_amount_1 = r47_amount_1;
	}







	public BigDecimal getR47_amount_2() {
		return r47_amount_2;
	}







	public void setR47_amount_2(BigDecimal r47_amount_2) {
		this.r47_amount_2 = r47_amount_2;
	}







	public String getR48_product() {
		return r48_product;
	}







	public void setR48_product(String r48_product) {
		this.r48_product = r48_product;
	}







	public BigDecimal getR48_amount_1() {
		return r48_amount_1;
	}







	public void setR48_amount_1(BigDecimal r48_amount_1) {
		this.r48_amount_1 = r48_amount_1;
	}







	public BigDecimal getR48_amount_2() {
		return r48_amount_2;
	}







	public void setR48_amount_2(BigDecimal r48_amount_2) {
		this.r48_amount_2 = r48_amount_2;
	}







	public String getR49_product() {
		return r49_product;
	}







	public void setR49_product(String r49_product) {
		this.r49_product = r49_product;
	}







	public BigDecimal getR49_amount_1() {
		return r49_amount_1;
	}







	public void setR49_amount_1(BigDecimal r49_amount_1) {
		this.r49_amount_1 = r49_amount_1;
	}







	public BigDecimal getR49_amount_2() {
		return r49_amount_2;
	}







	public void setR49_amount_2(BigDecimal r49_amount_2) {
		this.r49_amount_2 = r49_amount_2;
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



	
	
	


	public M_CA2_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}


}