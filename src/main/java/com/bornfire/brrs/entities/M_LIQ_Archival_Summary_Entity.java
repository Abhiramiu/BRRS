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
@Table(name = "BRRS_M_LIQ_ARCHIVALTABLE_SUMMARY")


public class M_LIQ_Archival_Summary_Entity {
	
	private String	r10_product;
	private BigDecimal	r10_total;
	
	private String	r11_product;
	private BigDecimal	r11_total;
	
	private String	r12_product;
	private BigDecimal	r12_total;
	
	private String	r13_product;
	private BigDecimal	r13_total;
	
	private String	r14_product;
	private BigDecimal	r14_total;
	
	private String	r15_product;
	private BigDecimal	r15_total;
	
	
	
	private String	r17_product;
	private BigDecimal	r17_total;
	
	private String	r18_product;
	private BigDecimal	r18_total;
	
	
	
	
	
	
	
	private String	r21_product;
	private BigDecimal	r21_total;
	
	private String	r22_product;
	
	
	
	
	private String	r24_product;
	private BigDecimal	r24_total;
	
	private String	r25_product;
	private BigDecimal	r25_total;
	

	private String	r27_product;
	private BigDecimal	r27_total;
	
	private String	r30_product;
	private BigDecimal	r30_total;
	
	private String	r31_product;
	private BigDecimal	r31_total;
	
	private String	r32_product;
	private BigDecimal	r32_total;
	
	private String	r34_product;
	private BigDecimal	r34_total;
	
	private String	r35_product;
	private BigDecimal	r35_total;
	
	
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
	private String	 del_flg;
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_total() {
		return r10_total;
	}
	public void setR10_total(BigDecimal r10_total) {
		this.r10_total = r10_total;
	}
	public String getR11_product() {
		return r11_product;
	}
	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}
	public BigDecimal getR11_total() {
		return r11_total;
	}
	public void setR11_total(BigDecimal r11_total) {
		this.r11_total = r11_total;
	}
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_total() {
		return r12_total;
	}
	public void setR12_total(BigDecimal r12_total) {
		this.r12_total = r12_total;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_total() {
		return r13_total;
	}
	public void setR13_total(BigDecimal r13_total) {
		this.r13_total = r13_total;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_total() {
		return r14_total;
	}
	public void setR14_total(BigDecimal r14_total) {
		this.r14_total = r14_total;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_total() {
		return r15_total;
	}
	public void setR15_total(BigDecimal r15_total) {
		this.r15_total = r15_total;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_total() {
		return r17_total;
	}
	public void setR17_total(BigDecimal r17_total) {
		this.r17_total = r17_total;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_total() {
		return r18_total;
	}
	public void setR18_total(BigDecimal r18_total) {
		this.r18_total = r18_total;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_total() {
		return r21_total;
	}
	public void setR21_total(BigDecimal r21_total) {
		this.r21_total = r21_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_total() {
		return r24_total;
	}
	public void setR24_total(BigDecimal r24_total) {
		this.r24_total = r24_total;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_total() {
		return r25_total;
	}
	public void setR25_total(BigDecimal r25_total) {
		this.r25_total = r25_total;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_total() {
		return r27_total;
	}
	public void setR27_total(BigDecimal r27_total) {
		this.r27_total = r27_total;
	}
	public String getR30_product() {
		return r30_product;
	}
	public void setR30_product(String r30_product) {
		this.r30_product = r30_product;
	}
	public BigDecimal getR30_total() {
		return r30_total;
	}
	public void setR30_total(BigDecimal r30_total) {
		this.r30_total = r30_total;
	}
	public String getR31_product() {
		return r31_product;
	}
	public void setR31_product(String r31_product) {
		this.r31_product = r31_product;
	}
	public BigDecimal getR31_total() {
		return r31_total;
	}
	public void setR31_total(BigDecimal r31_total) {
		this.r31_total = r31_total;
	}
	public String getR32_product() {
		return r32_product;
	}
	public void setR32_product(String r32_product) {
		this.r32_product = r32_product;
	}
	public BigDecimal getR32_total() {
		return r32_total;
	}
	public void setR32_total(BigDecimal r32_total) {
		this.r32_total = r32_total;
	}
	public String getR34_product() {
		return r34_product;
	}
	public void setR34_product(String r34_product) {
		this.r34_product = r34_product;
	}
	public BigDecimal getR34_total() {
		return r34_total;
	}
	public void setR34_total(BigDecimal r34_total) {
		this.r34_total = r34_total;
	}
	public String getR35_product() {
		return r35_product;
	}
	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}
	public BigDecimal getR35_total() {
		return r35_total;
	}
	public void setR35_total(BigDecimal r35_total) {
		this.r35_total = r35_total;
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
	public M_LIQ_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
    


	

	

}
