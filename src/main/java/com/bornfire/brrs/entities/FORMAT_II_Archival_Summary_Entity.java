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
@Table(name = "BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY")


public class FORMAT_II_Archival_Summary_Entity {
	
	

		
	
	private String	r13_product;
	private BigDecimal	r13_amt;
	private BigDecimal	r13_amt_sub_add;
	private BigDecimal	r13_amt_sub_del;
	private BigDecimal	r13_amt_total;
	private String	r14_product;
	private BigDecimal	r14_amt;
	private BigDecimal	r14_amt_sub_add;
	private BigDecimal	r14_amt_sub_del;
	private BigDecimal	r14_amt_total;
	private String	r15_product;
	private BigDecimal	r15_amt;
	private BigDecimal	r15_amt_sub_add;
	private BigDecimal	r15_amt_sub_del;
	private BigDecimal	r15_amt_total;
	private String	r16_product;
	private BigDecimal	r16_amt;
	private BigDecimal	r16_amt_sub_add;
	private BigDecimal	r16_amt_sub_del;
	private BigDecimal	r16_amt_total;
	private String	r17_product;
	private BigDecimal	r17_amt;
	private BigDecimal	r17_amt_sub_add;
	private BigDecimal	r17_amt_sub_del;
	private BigDecimal	r17_amt_total;
	private String	r18_product;
	private BigDecimal	r18_amt;
	private BigDecimal	r18_amt_sub_add;
	private BigDecimal	r18_amt_sub_del;
	private BigDecimal	r18_amt_total;
	private String	r19_product;
	private BigDecimal	r19_amt;
	private BigDecimal	r19_amt_sub_add;
	private BigDecimal	r19_amt_sub_del;
	private BigDecimal	r19_amt_total;
	private String	r20_product;
	private BigDecimal	r20_amt;
	private BigDecimal	r20_amt_sub_add;
	private BigDecimal	r20_amt_sub_del;
	private BigDecimal	r20_amt_total;
	private String	r21_product;
	private BigDecimal	r21_amt;
	private BigDecimal	r21_amt_sub_add;
	private BigDecimal	r21_amt_sub_del;
	private BigDecimal	r21_amt_total;
	private String	r22_product;
	private BigDecimal	r22_amt;
	private BigDecimal	r22_amt_sub_add;
	private BigDecimal	r22_amt_sub_del;
	private BigDecimal	r22_amt_total;
	private String	r23_product;
	private BigDecimal	r23_amt;
	private BigDecimal	r23_amt_sub_add;
	private BigDecimal	r23_amt_sub_del;
	private BigDecimal	r23_amt_total;
	private String	r24_product;
	private BigDecimal	r24_amt;
	private BigDecimal	r24_amt_sub_add;
	private BigDecimal	r24_amt_sub_del;
	private BigDecimal	r24_amt_total;
	private String	r25_product;
	private BigDecimal	r25_amt;
	private BigDecimal	r25_amt_sub_add;
	private BigDecimal	r25_amt_sub_del;
	private BigDecimal	r25_amt_total;
	private String	r26_product;
	private BigDecimal	r26_amt;
	private BigDecimal	r26_amt_sub_add;
	private BigDecimal	r26_amt_sub_del;
	private BigDecimal	r26_amt_total;
	private String	r27_product;
	private BigDecimal	r27_amt;
	private BigDecimal	r27_amt_sub_add;
	private BigDecimal	r27_amt_sub_del;
	private BigDecimal	r27_amt_total;
	private String	r28_product;
	private BigDecimal	r28_amt;
	private BigDecimal	r28_amt_sub_add;
	private BigDecimal	r28_amt_sub_del;
	private BigDecimal	r28_amt_total;


	               
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
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_amt() {
		return r13_amt;
	}
	public void setR13_amt(BigDecimal r13_amt) {
		this.r13_amt = r13_amt;
	}
	public BigDecimal getR13_amt_sub_add() {
		return r13_amt_sub_add;
	}
	public void setR13_amt_sub_add(BigDecimal r13_amt_sub_add) {
		this.r13_amt_sub_add = r13_amt_sub_add;
	}
	public BigDecimal getR13_amt_sub_del() {
		return r13_amt_sub_del;
	}
	public void setR13_amt_sub_del(BigDecimal r13_amt_sub_del) {
		this.r13_amt_sub_del = r13_amt_sub_del;
	}
	public BigDecimal getR13_amt_total() {
		return r13_amt_total;
	}
	public void setR13_amt_total(BigDecimal r13_amt_total) {
		this.r13_amt_total = r13_amt_total;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_amt() {
		return r14_amt;
	}
	public void setR14_amt(BigDecimal r14_amt) {
		this.r14_amt = r14_amt;
	}
	public BigDecimal getR14_amt_sub_add() {
		return r14_amt_sub_add;
	}
	public void setR14_amt_sub_add(BigDecimal r14_amt_sub_add) {
		this.r14_amt_sub_add = r14_amt_sub_add;
	}
	public BigDecimal getR14_amt_sub_del() {
		return r14_amt_sub_del;
	}
	public void setR14_amt_sub_del(BigDecimal r14_amt_sub_del) {
		this.r14_amt_sub_del = r14_amt_sub_del;
	}
	public BigDecimal getR14_amt_total() {
		return r14_amt_total;
	}
	public void setR14_amt_total(BigDecimal r14_amt_total) {
		this.r14_amt_total = r14_amt_total;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_amt() {
		return r15_amt;
	}
	public void setR15_amt(BigDecimal r15_amt) {
		this.r15_amt = r15_amt;
	}
	public BigDecimal getR15_amt_sub_add() {
		return r15_amt_sub_add;
	}
	public void setR15_amt_sub_add(BigDecimal r15_amt_sub_add) {
		this.r15_amt_sub_add = r15_amt_sub_add;
	}
	public BigDecimal getR15_amt_sub_del() {
		return r15_amt_sub_del;
	}
	public void setR15_amt_sub_del(BigDecimal r15_amt_sub_del) {
		this.r15_amt_sub_del = r15_amt_sub_del;
	}
	public BigDecimal getR15_amt_total() {
		return r15_amt_total;
	}
	public void setR15_amt_total(BigDecimal r15_amt_total) {
		this.r15_amt_total = r15_amt_total;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_amt() {
		return r16_amt;
	}
	public void setR16_amt(BigDecimal r16_amt) {
		this.r16_amt = r16_amt;
	}
	public BigDecimal getR16_amt_sub_add() {
		return r16_amt_sub_add;
	}
	public void setR16_amt_sub_add(BigDecimal r16_amt_sub_add) {
		this.r16_amt_sub_add = r16_amt_sub_add;
	}
	public BigDecimal getR16_amt_sub_del() {
		return r16_amt_sub_del;
	}
	public void setR16_amt_sub_del(BigDecimal r16_amt_sub_del) {
		this.r16_amt_sub_del = r16_amt_sub_del;
	}
	public BigDecimal getR16_amt_total() {
		return r16_amt_total;
	}
	public void setR16_amt_total(BigDecimal r16_amt_total) {
		this.r16_amt_total = r16_amt_total;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_amt() {
		return r17_amt;
	}
	public void setR17_amt(BigDecimal r17_amt) {
		this.r17_amt = r17_amt;
	}
	public BigDecimal getR17_amt_sub_add() {
		return r17_amt_sub_add;
	}
	public void setR17_amt_sub_add(BigDecimal r17_amt_sub_add) {
		this.r17_amt_sub_add = r17_amt_sub_add;
	}
	public BigDecimal getR17_amt_sub_del() {
		return r17_amt_sub_del;
	}
	public void setR17_amt_sub_del(BigDecimal r17_amt_sub_del) {
		this.r17_amt_sub_del = r17_amt_sub_del;
	}
	public BigDecimal getR17_amt_total() {
		return r17_amt_total;
	}
	public void setR17_amt_total(BigDecimal r17_amt_total) {
		this.r17_amt_total = r17_amt_total;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_amt() {
		return r18_amt;
	}
	public void setR18_amt(BigDecimal r18_amt) {
		this.r18_amt = r18_amt;
	}
	public BigDecimal getR18_amt_sub_add() {
		return r18_amt_sub_add;
	}
	public void setR18_amt_sub_add(BigDecimal r18_amt_sub_add) {
		this.r18_amt_sub_add = r18_amt_sub_add;
	}
	public BigDecimal getR18_amt_sub_del() {
		return r18_amt_sub_del;
	}
	public void setR18_amt_sub_del(BigDecimal r18_amt_sub_del) {
		this.r18_amt_sub_del = r18_amt_sub_del;
	}
	public BigDecimal getR18_amt_total() {
		return r18_amt_total;
	}
	public void setR18_amt_total(BigDecimal r18_amt_total) {
		this.r18_amt_total = r18_amt_total;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_amt() {
		return r19_amt;
	}
	public void setR19_amt(BigDecimal r19_amt) {
		this.r19_amt = r19_amt;
	}
	public BigDecimal getR19_amt_sub_add() {
		return r19_amt_sub_add;
	}
	public void setR19_amt_sub_add(BigDecimal r19_amt_sub_add) {
		this.r19_amt_sub_add = r19_amt_sub_add;
	}
	public BigDecimal getR19_amt_sub_del() {
		return r19_amt_sub_del;
	}
	public void setR19_amt_sub_del(BigDecimal r19_amt_sub_del) {
		this.r19_amt_sub_del = r19_amt_sub_del;
	}
	public BigDecimal getR19_amt_total() {
		return r19_amt_total;
	}
	public void setR19_amt_total(BigDecimal r19_amt_total) {
		this.r19_amt_total = r19_amt_total;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_amt() {
		return r20_amt;
	}
	public void setR20_amt(BigDecimal r20_amt) {
		this.r20_amt = r20_amt;
	}
	public BigDecimal getR20_amt_sub_add() {
		return r20_amt_sub_add;
	}
	public void setR20_amt_sub_add(BigDecimal r20_amt_sub_add) {
		this.r20_amt_sub_add = r20_amt_sub_add;
	}
	public BigDecimal getR20_amt_sub_del() {
		return r20_amt_sub_del;
	}
	public void setR20_amt_sub_del(BigDecimal r20_amt_sub_del) {
		this.r20_amt_sub_del = r20_amt_sub_del;
	}
	public BigDecimal getR20_amt_total() {
		return r20_amt_total;
	}
	public void setR20_amt_total(BigDecimal r20_amt_total) {
		this.r20_amt_total = r20_amt_total;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_amt() {
		return r21_amt;
	}
	public void setR21_amt(BigDecimal r21_amt) {
		this.r21_amt = r21_amt;
	}
	public BigDecimal getR21_amt_sub_add() {
		return r21_amt_sub_add;
	}
	public void setR21_amt_sub_add(BigDecimal r21_amt_sub_add) {
		this.r21_amt_sub_add = r21_amt_sub_add;
	}
	public BigDecimal getR21_amt_sub_del() {
		return r21_amt_sub_del;
	}
	public void setR21_amt_sub_del(BigDecimal r21_amt_sub_del) {
		this.r21_amt_sub_del = r21_amt_sub_del;
	}
	public BigDecimal getR21_amt_total() {
		return r21_amt_total;
	}
	public void setR21_amt_total(BigDecimal r21_amt_total) {
		this.r21_amt_total = r21_amt_total;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_amt() {
		return r22_amt;
	}
	public void setR22_amt(BigDecimal r22_amt) {
		this.r22_amt = r22_amt;
	}
	public BigDecimal getR22_amt_sub_add() {
		return r22_amt_sub_add;
	}
	public void setR22_amt_sub_add(BigDecimal r22_amt_sub_add) {
		this.r22_amt_sub_add = r22_amt_sub_add;
	}
	public BigDecimal getR22_amt_sub_del() {
		return r22_amt_sub_del;
	}
	public void setR22_amt_sub_del(BigDecimal r22_amt_sub_del) {
		this.r22_amt_sub_del = r22_amt_sub_del;
	}
	public BigDecimal getR22_amt_total() {
		return r22_amt_total;
	}
	public void setR22_amt_total(BigDecimal r22_amt_total) {
		this.r22_amt_total = r22_amt_total;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_amt() {
		return r23_amt;
	}
	public void setR23_amt(BigDecimal r23_amt) {
		this.r23_amt = r23_amt;
	}
	public BigDecimal getR23_amt_sub_add() {
		return r23_amt_sub_add;
	}
	public void setR23_amt_sub_add(BigDecimal r23_amt_sub_add) {
		this.r23_amt_sub_add = r23_amt_sub_add;
	}
	public BigDecimal getR23_amt_sub_del() {
		return r23_amt_sub_del;
	}
	public void setR23_amt_sub_del(BigDecimal r23_amt_sub_del) {
		this.r23_amt_sub_del = r23_amt_sub_del;
	}
	public BigDecimal getR23_amt_total() {
		return r23_amt_total;
	}
	public void setR23_amt_total(BigDecimal r23_amt_total) {
		this.r23_amt_total = r23_amt_total;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_amt() {
		return r24_amt;
	}
	public void setR24_amt(BigDecimal r24_amt) {
		this.r24_amt = r24_amt;
	}
	public BigDecimal getR24_amt_sub_add() {
		return r24_amt_sub_add;
	}
	public void setR24_amt_sub_add(BigDecimal r24_amt_sub_add) {
		this.r24_amt_sub_add = r24_amt_sub_add;
	}
	public BigDecimal getR24_amt_sub_del() {
		return r24_amt_sub_del;
	}
	public void setR24_amt_sub_del(BigDecimal r24_amt_sub_del) {
		this.r24_amt_sub_del = r24_amt_sub_del;
	}
	public BigDecimal getR24_amt_total() {
		return r24_amt_total;
	}
	public void setR24_amt_total(BigDecimal r24_amt_total) {
		this.r24_amt_total = r24_amt_total;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_amt() {
		return r25_amt;
	}
	public void setR25_amt(BigDecimal r25_amt) {
		this.r25_amt = r25_amt;
	}
	public BigDecimal getR25_amt_sub_add() {
		return r25_amt_sub_add;
	}
	public void setR25_amt_sub_add(BigDecimal r25_amt_sub_add) {
		this.r25_amt_sub_add = r25_amt_sub_add;
	}
	public BigDecimal getR25_amt_sub_del() {
		return r25_amt_sub_del;
	}
	public void setR25_amt_sub_del(BigDecimal r25_amt_sub_del) {
		this.r25_amt_sub_del = r25_amt_sub_del;
	}
	public BigDecimal getR25_amt_total() {
		return r25_amt_total;
	}
	public void setR25_amt_total(BigDecimal r25_amt_total) {
		this.r25_amt_total = r25_amt_total;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_amt() {
		return r26_amt;
	}
	public void setR26_amt(BigDecimal r26_amt) {
		this.r26_amt = r26_amt;
	}
	public BigDecimal getR26_amt_sub_add() {
		return r26_amt_sub_add;
	}
	public void setR26_amt_sub_add(BigDecimal r26_amt_sub_add) {
		this.r26_amt_sub_add = r26_amt_sub_add;
	}
	public BigDecimal getR26_amt_sub_del() {
		return r26_amt_sub_del;
	}
	public void setR26_amt_sub_del(BigDecimal r26_amt_sub_del) {
		this.r26_amt_sub_del = r26_amt_sub_del;
	}
	public BigDecimal getR26_amt_total() {
		return r26_amt_total;
	}
	public void setR26_amt_total(BigDecimal r26_amt_total) {
		this.r26_amt_total = r26_amt_total;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_amt() {
		return r27_amt;
	}
	public void setR27_amt(BigDecimal r27_amt) {
		this.r27_amt = r27_amt;
	}
	public BigDecimal getR27_amt_sub_add() {
		return r27_amt_sub_add;
	}
	public void setR27_amt_sub_add(BigDecimal r27_amt_sub_add) {
		this.r27_amt_sub_add = r27_amt_sub_add;
	}
	public BigDecimal getR27_amt_sub_del() {
		return r27_amt_sub_del;
	}
	public void setR27_amt_sub_del(BigDecimal r27_amt_sub_del) {
		this.r27_amt_sub_del = r27_amt_sub_del;
	}
	public BigDecimal getR27_amt_total() {
		return r27_amt_total;
	}
	public void setR27_amt_total(BigDecimal r27_amt_total) {
		this.r27_amt_total = r27_amt_total;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_amt() {
		return r28_amt;
	}
	public void setR28_amt(BigDecimal r28_amt) {
		this.r28_amt = r28_amt;
	}
	public BigDecimal getR28_amt_sub_add() {
		return r28_amt_sub_add;
	}
	public void setR28_amt_sub_add(BigDecimal r28_amt_sub_add) {
		this.r28_amt_sub_add = r28_amt_sub_add;
	}
	public BigDecimal getR28_amt_sub_del() {
		return r28_amt_sub_del;
	}
	public void setR28_amt_sub_del(BigDecimal r28_amt_sub_del) {
		this.r28_amt_sub_del = r28_amt_sub_del;
	}
	public BigDecimal getR28_amt_total() {
		return r28_amt_total;
	}
	public void setR28_amt_total(BigDecimal r28_amt_total) {
		this.r28_amt_total = r28_amt_total;
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
	public FORMAT_II_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}