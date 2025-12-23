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
@Table(name = "BRRS_SCOPE_OF_APP_ARCHIVALTABLE_SUMMARY")


public class SCOPE_OF_APP_Archival_Summary_Entity {
	
	

		
	
	  private String	r6_qua_name;
	  private String	r6_product;
	  private String	r6_bank_name  ;
	  private String	r7_product;
	  private BigDecimal	r7_amt ;
	  private String	r8_product;
	  private BigDecimal	r8_amt ;
	  private String	r9_qua_name;
	  private String	r9_product;
	  private BigDecimal	r9_amt ;
	  private String	r10_product;
	  private BigDecimal	r10_amt ;

	
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
	public String getR6_qua_name() {
		return r6_qua_name;
	}
	public void setR6_qua_name(String r6_qua_name) {
		this.r6_qua_name = r6_qua_name;
	}
	public String getR6_product() {
		return r6_product;
	}
	public void setR6_product(String r6_product) {
		this.r6_product = r6_product;
	}
	public String getR6_bank_name() {
		return r6_bank_name;
	}
	public void setR6_bank_name(String r6_bank_name) {
		this.r6_bank_name = r6_bank_name;
	}
	public String getR7_product() {
		return r7_product;
	}
	public void setR7_product(String r7_product) {
		this.r7_product = r7_product;
	}
	public BigDecimal getR7_amt() {
		return r7_amt;
	}
	public void setR7_amt(BigDecimal r7_amt) {
		this.r7_amt = r7_amt;
	}
	public String getR8_product() {
		return r8_product;
	}
	public void setR8_product(String r8_product) {
		this.r8_product = r8_product;
	}
	public BigDecimal getR8_amt() {
		return r8_amt;
	}
	public void setR8_amt(BigDecimal r8_amt) {
		this.r8_amt = r8_amt;
	}
	public String getR9_qua_name() {
		return r9_qua_name;
	}
	public void setR9_qua_name(String r9_qua_name) {
		this.r9_qua_name = r9_qua_name;
	}
	public String getR9_product() {
		return r9_product;
	}
	public void setR9_product(String r9_product) {
		this.r9_product = r9_product;
	}
	public BigDecimal getR9_amt() {
		return r9_amt;
	}
	public void setR9_amt(BigDecimal r9_amt) {
		this.r9_amt = r9_amt;
	}
	public String getR10_product() {
		return r10_product;
	}
	public void setR10_product(String r10_product) {
		this.r10_product = r10_product;
	}
	public BigDecimal getR10_amt() {
		return r10_amt;
	}
	public void setR10_amt(BigDecimal r10_amt) {
		this.r10_amt = r10_amt;
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
	public SCOPE_OF_APP_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
