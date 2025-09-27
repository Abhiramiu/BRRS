package com.bornfire.brrs.entities;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
@Entity
@Table(name = "BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1")
public class M_LA3_Archival_Summary_Entity1{
	

	// ====== R10 ======
	private String r10_product; 
	private BigDecimal r10_no_of_ac; 
	private BigDecimal r10_approved_limit; 
	private BigDecimal r10_amount_outstanding; 

	// ====== R11 ======
	private String r11_product;
	private BigDecimal r11_no_of_ac;
	private BigDecimal r11_approved_limit;
	private BigDecimal r11_amount_outstanding;

	// ====== R12 ======
	private String r12_product;
	private BigDecimal r12_no_of_ac;
	private BigDecimal r12_approved_limit;
	private BigDecimal r12_amount_outstanding;

	// ====== R13 ======
	private String r13_product;
	private BigDecimal r13_no_of_ac;
	private BigDecimal r13_approved_limit;
	private BigDecimal r13_amount_outstanding;

	// ====== R14 ======
	private String r14_product;
	private BigDecimal r14_no_of_ac;
	private BigDecimal r14_approved_limit;
	private BigDecimal r14_amount_outstanding;

	// ====== R15 ======
	private String r15_product;
	private BigDecimal r15_no_of_ac;
	private BigDecimal r15_approved_limit;
	private BigDecimal r15_amount_outstanding;

	// ====== R16 ======
	private String r16_product;
	private BigDecimal r16_no_of_ac;
	private BigDecimal r16_approved_limit;
	private BigDecimal r16_amount_outstanding;

	// ====== R21 ======
	private String r21_product;
	private BigDecimal r21_no_of_ac;
	private BigDecimal r21_approved_limit;
	private BigDecimal r21_amount_outstanding;

	// ====== R22 ======
	private String r22_product;
	private BigDecimal r22_no_of_ac;
	private BigDecimal r22_approved_limit;
	private BigDecimal r22_amount_outstanding;

	// ====== R23 ======
	private String r23_product;
	private BigDecimal r23_no_of_ac;
	private BigDecimal r23_approved_limit;
	private BigDecimal r23_amount_outstanding;

	// ====== R24 ======
	private String r24_product;
	private BigDecimal r24_no_of_ac;
	private BigDecimal r24_approved_limit;
	private BigDecimal r24_amount_outstanding;

	// ====== R25 ======
	private String r25_product;
	private BigDecimal r25_no_of_ac;
	private BigDecimal r25_approved_limit;
	private BigDecimal r25_amount_outstanding;

	// ====== R26 ======
	private String r26_product;
	private BigDecimal r26_no_of_ac;
	private BigDecimal r26_approved_limit;
	private BigDecimal r26_amount_outstanding;

	// ====== R27 ======
	private String r27_product;
	private BigDecimal r27_no_of_ac;
	private BigDecimal r27_approved_limit;
	private BigDecimal r27_amount_outstanding;

	// ====== R28 ======
	private String r28_product;
	private BigDecimal r28_no_of_ac;
	private BigDecimal r28_approved_limit;
	private BigDecimal r28_amount_outstanding;

	// ====== R29 ======
	private String r29_product;
	private BigDecimal r29_no_of_ac;
	private BigDecimal r29_approved_limit;
	private BigDecimal r29_amount_outstanding;

	// ====== R30 ======
	private String r30_product;
	private BigDecimal r30_no_of_ac;
	private BigDecimal r30_approved_limit;
	private BigDecimal r30_amount_outstanding;

	// ====== R31 ======
	private String r31_product;
	private BigDecimal r31_no_of_ac;
	private BigDecimal r31_approved_limit;
	private BigDecimal r31_amount_outstanding;

	

	    
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
		public BigDecimal getR10_no_of_ac() {
			return r10_no_of_ac;
		}
		public void setR10_no_of_ac(BigDecimal r10_no_of_ac) {
			this.r10_no_of_ac = r10_no_of_ac;
		}
		public BigDecimal getR10_approved_limit() {
			return r10_approved_limit;
		}
		public void setR10_approved_limit(BigDecimal r10_approved_limit) {
			this.r10_approved_limit = r10_approved_limit;
		}
		public BigDecimal getR10_amount_outstanding() {
			return r10_amount_outstanding;
		}
		public void setR10_amount_outstanding(BigDecimal r10_amount_outstanding) {
			this.r10_amount_outstanding = r10_amount_outstanding;
		}
		public String getR11_product() {
			return r11_product;
		}
		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}
		public BigDecimal getR11_no_of_ac() {
			return r11_no_of_ac;
		}
		public void setR11_no_of_ac(BigDecimal r11_no_of_ac) {
			this.r11_no_of_ac = r11_no_of_ac;
		}
		public BigDecimal getR11_approved_limit() {
			return r11_approved_limit;
		}
		public void setR11_approved_limit(BigDecimal r11_approved_limit) {
			this.r11_approved_limit = r11_approved_limit;
		}
		public BigDecimal getR11_amount_outstanding() {
			return r11_amount_outstanding;
		}
		public void setR11_amount_outstanding(BigDecimal r11_amount_outstanding) {
			this.r11_amount_outstanding = r11_amount_outstanding;
		}
		public String getR12_product() {
			return r12_product;
		}
		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}
		public BigDecimal getR12_no_of_ac() {
			return r12_no_of_ac;
		}
		public void setR12_no_of_ac(BigDecimal r12_no_of_ac) {
			this.r12_no_of_ac = r12_no_of_ac;
		}
		public BigDecimal getR12_approved_limit() {
			return r12_approved_limit;
		}
		public void setR12_approved_limit(BigDecimal r12_approved_limit) {
			this.r12_approved_limit = r12_approved_limit;
		}
		public BigDecimal getR12_amount_outstanding() {
			return r12_amount_outstanding;
		}
		public void setR12_amount_outstanding(BigDecimal r12_amount_outstanding) {
			this.r12_amount_outstanding = r12_amount_outstanding;
		}
		public String getR13_product() {
			return r13_product;
		}
		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}
		public BigDecimal getR13_no_of_ac() {
			return r13_no_of_ac;
		}
		public void setR13_no_of_ac(BigDecimal r13_no_of_ac) {
			this.r13_no_of_ac = r13_no_of_ac;
		}
		public BigDecimal getR13_approved_limit() {
			return r13_approved_limit;
		}
		public void setR13_approved_limit(BigDecimal r13_approved_limit) {
			this.r13_approved_limit = r13_approved_limit;
		}
		public BigDecimal getR13_amount_outstanding() {
			return r13_amount_outstanding;
		}
		public void setR13_amount_outstanding(BigDecimal r13_amount_outstanding) {
			this.r13_amount_outstanding = r13_amount_outstanding;
		}
		public String getR14_product() {
			return r14_product;
		}
		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}
		public BigDecimal getR14_no_of_ac() {
			return r14_no_of_ac;
		}
		public void setR14_no_of_ac(BigDecimal r14_no_of_ac) {
			this.r14_no_of_ac = r14_no_of_ac;
		}
		public BigDecimal getR14_approved_limit() {
			return r14_approved_limit;
		}
		public void setR14_approved_limit(BigDecimal r14_approved_limit) {
			this.r14_approved_limit = r14_approved_limit;
		}
		public BigDecimal getR14_amount_outstanding() {
			return r14_amount_outstanding;
		}
		public void setR14_amount_outstanding(BigDecimal r14_amount_outstanding) {
			this.r14_amount_outstanding = r14_amount_outstanding;
		}
		public String getR15_product() {
			return r15_product;
		}
		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}
		public BigDecimal getR15_no_of_ac() {
			return r15_no_of_ac;
		}
		public void setR15_no_of_ac(BigDecimal r15_no_of_ac) {
			this.r15_no_of_ac = r15_no_of_ac;
		}
		public BigDecimal getR15_approved_limit() {
			return r15_approved_limit;
		}
		public void setR15_approved_limit(BigDecimal r15_approved_limit) {
			this.r15_approved_limit = r15_approved_limit;
		}
		public BigDecimal getR15_amount_outstanding() {
			return r15_amount_outstanding;
		}
		public void setR15_amount_outstanding(BigDecimal r15_amount_outstanding) {
			this.r15_amount_outstanding = r15_amount_outstanding;
		}
		public String getR16_product() {
			return r16_product;
		}
		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}
		public BigDecimal getR16_no_of_ac() {
			return r16_no_of_ac;
		}
		public void setR16_no_of_ac(BigDecimal r16_no_of_ac) {
			this.r16_no_of_ac = r16_no_of_ac;
		}
		public BigDecimal getR16_approved_limit() {
			return r16_approved_limit;
		}
		public void setR16_approved_limit(BigDecimal r16_approved_limit) {
			this.r16_approved_limit = r16_approved_limit;
		}
		public BigDecimal getR16_amount_outstanding() {
			return r16_amount_outstanding;
		}
		public void setR16_amount_outstanding(BigDecimal r16_amount_outstanding) {
			this.r16_amount_outstanding = r16_amount_outstanding;
		}
		public String getR21_product() {
			return r21_product;
		}
		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}
		public BigDecimal getR21_no_of_ac() {
			return r21_no_of_ac;
		}
		public void setR21_no_of_ac(BigDecimal r21_no_of_ac) {
			this.r21_no_of_ac = r21_no_of_ac;
		}
		public BigDecimal getR21_approved_limit() {
			return r21_approved_limit;
		}
		public void setR21_approved_limit(BigDecimal r21_approved_limit) {
			this.r21_approved_limit = r21_approved_limit;
		}
		public BigDecimal getR21_amount_outstanding() {
			return r21_amount_outstanding;
		}
		public void setR21_amount_outstanding(BigDecimal r21_amount_outstanding) {
			this.r21_amount_outstanding = r21_amount_outstanding;
		}
		public String getR22_product() {
			return r22_product;
		}
		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}
		public BigDecimal getR22_no_of_ac() {
			return r22_no_of_ac;
		}
		public void setR22_no_of_ac(BigDecimal r22_no_of_ac) {
			this.r22_no_of_ac = r22_no_of_ac;
		}
		public BigDecimal getR22_approved_limit() {
			return r22_approved_limit;
		}
		public void setR22_approved_limit(BigDecimal r22_approved_limit) {
			this.r22_approved_limit = r22_approved_limit;
		}
		public BigDecimal getR22_amount_outstanding() {
			return r22_amount_outstanding;
		}
		public void setR22_amount_outstanding(BigDecimal r22_amount_outstanding) {
			this.r22_amount_outstanding = r22_amount_outstanding;
		}
		public String getR23_product() {
			return r23_product;
		}
		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}
		public BigDecimal getR23_no_of_ac() {
			return r23_no_of_ac;
		}
		public void setR23_no_of_ac(BigDecimal r23_no_of_ac) {
			this.r23_no_of_ac = r23_no_of_ac;
		}
		public BigDecimal getR23_approved_limit() {
			return r23_approved_limit;
		}
		public void setR23_approved_limit(BigDecimal r23_approved_limit) {
			this.r23_approved_limit = r23_approved_limit;
		}
		public BigDecimal getR23_amount_outstanding() {
			return r23_amount_outstanding;
		}
		public void setR23_amount_outstanding(BigDecimal r23_amount_outstanding) {
			this.r23_amount_outstanding = r23_amount_outstanding;
		}
		public String getR24_product() {
			return r24_product;
		}
		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}
		public BigDecimal getR24_no_of_ac() {
			return r24_no_of_ac;
		}
		public void setR24_no_of_ac(BigDecimal r24_no_of_ac) {
			this.r24_no_of_ac = r24_no_of_ac;
		}
		public BigDecimal getR24_approved_limit() {
			return r24_approved_limit;
		}
		public void setR24_approved_limit(BigDecimal r24_approved_limit) {
			this.r24_approved_limit = r24_approved_limit;
		}
		public BigDecimal getR24_amount_outstanding() {
			return r24_amount_outstanding;
		}
		public void setR24_amount_outstanding(BigDecimal r24_amount_outstanding) {
			this.r24_amount_outstanding = r24_amount_outstanding;
		}
		public String getR25_product() {
			return r25_product;
		}
		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}
		public BigDecimal getR25_no_of_ac() {
			return r25_no_of_ac;
		}
		public void setR25_no_of_ac(BigDecimal r25_no_of_ac) {
			this.r25_no_of_ac = r25_no_of_ac;
		}
		public BigDecimal getR25_approved_limit() {
			return r25_approved_limit;
		}
		public void setR25_approved_limit(BigDecimal r25_approved_limit) {
			this.r25_approved_limit = r25_approved_limit;
		}
		public BigDecimal getR25_amount_outstanding() {
			return r25_amount_outstanding;
		}
		public void setR25_amount_outstanding(BigDecimal r25_amount_outstanding) {
			this.r25_amount_outstanding = r25_amount_outstanding;
		}
		public String getR26_product() {
			return r26_product;
		}
		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}
		public BigDecimal getR26_no_of_ac() {
			return r26_no_of_ac;
		}
		public void setR26_no_of_ac(BigDecimal r26_no_of_ac) {
			this.r26_no_of_ac = r26_no_of_ac;
		}
		public BigDecimal getR26_approved_limit() {
			return r26_approved_limit;
		}
		public void setR26_approved_limit(BigDecimal r26_approved_limit) {
			this.r26_approved_limit = r26_approved_limit;
		}
		public BigDecimal getR26_amount_outstanding() {
			return r26_amount_outstanding;
		}
		public void setR26_amount_outstanding(BigDecimal r26_amount_outstanding) {
			this.r26_amount_outstanding = r26_amount_outstanding;
		}
		public String getR27_product() {
			return r27_product;
		}
		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}
		public BigDecimal getR27_no_of_ac() {
			return r27_no_of_ac;
		}
		public void setR27_no_of_ac(BigDecimal r27_no_of_ac) {
			this.r27_no_of_ac = r27_no_of_ac;
		}
		public BigDecimal getR27_approved_limit() {
			return r27_approved_limit;
		}
		public void setR27_approved_limit(BigDecimal r27_approved_limit) {
			this.r27_approved_limit = r27_approved_limit;
		}
		public BigDecimal getR27_amount_outstanding() {
			return r27_amount_outstanding;
		}
		public void setR27_amount_outstanding(BigDecimal r27_amount_outstanding) {
			this.r27_amount_outstanding = r27_amount_outstanding;
		}
		public String getR28_product() {
			return r28_product;
		}
		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}
		public BigDecimal getR28_no_of_ac() {
			return r28_no_of_ac;
		}
		public void setR28_no_of_ac(BigDecimal r28_no_of_ac) {
			this.r28_no_of_ac = r28_no_of_ac;
		}
		public BigDecimal getR28_approved_limit() {
			return r28_approved_limit;
		}
		public void setR28_approved_limit(BigDecimal r28_approved_limit) {
			this.r28_approved_limit = r28_approved_limit;
		}
		public BigDecimal getR28_amount_outstanding() {
			return r28_amount_outstanding;
		}
		public void setR28_amount_outstanding(BigDecimal r28_amount_outstanding) {
			this.r28_amount_outstanding = r28_amount_outstanding;
		}
		public String getR29_product() {
			return r29_product;
		}
		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}
		public BigDecimal getR29_no_of_ac() {
			return r29_no_of_ac;
		}
		public void setR29_no_of_ac(BigDecimal r29_no_of_ac) {
			this.r29_no_of_ac = r29_no_of_ac;
		}
		public BigDecimal getR29_approved_limit() {
			return r29_approved_limit;
		}
		public void setR29_approved_limit(BigDecimal r29_approved_limit) {
			this.r29_approved_limit = r29_approved_limit;
		}
		public BigDecimal getR29_amount_outstanding() {
			return r29_amount_outstanding;
		}
		public void setR29_amount_outstanding(BigDecimal r29_amount_outstanding) {
			this.r29_amount_outstanding = r29_amount_outstanding;
		}
		public String getR30_product() {
			return r30_product;
		}
		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}
		public BigDecimal getR30_no_of_ac() {
			return r30_no_of_ac;
		}
		public void setR30_no_of_ac(BigDecimal r30_no_of_ac) {
			this.r30_no_of_ac = r30_no_of_ac;
		}
		public BigDecimal getR30_approved_limit() {
			return r30_approved_limit;
		}
		public void setR30_approved_limit(BigDecimal r30_approved_limit) {
			this.r30_approved_limit = r30_approved_limit;
		}
		public BigDecimal getR30_amount_outstanding() {
			return r30_amount_outstanding;
		}
		public void setR30_amount_outstanding(BigDecimal r30_amount_outstanding) {
			this.r30_amount_outstanding = r30_amount_outstanding;
		}
		public String getR31_product() {
			return r31_product;
		}
		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}
		public BigDecimal getR31_no_of_ac() {
			return r31_no_of_ac;
		}
		public void setR31_no_of_ac(BigDecimal r31_no_of_ac) {
			this.r31_no_of_ac = r31_no_of_ac;
		}
		public BigDecimal getR31_approved_limit() {
			return r31_approved_limit;
		}
		public void setR31_approved_limit(BigDecimal r31_approved_limit) {
			this.r31_approved_limit = r31_approved_limit;
		}
		public BigDecimal getR31_amount_outstanding() {
			return r31_amount_outstanding;
		}
		public void setR31_amount_outstanding(BigDecimal r31_amount_outstanding) {
			this.r31_amount_outstanding = r31_amount_outstanding;
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
		public M_LA3_Archival_Summary_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	
		
		

	}



