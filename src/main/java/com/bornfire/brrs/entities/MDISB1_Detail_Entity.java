package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_MDISB1_DETAILTABLE")
public class MDISB1_Detail_Entity {

	@Id
	@Column(name = "SNO")
	private Long sno;
	
	 @Column(name = "CUST_ID")
     private String cust_id;

     
     @Column(name = "ACCT_NUMBER")
     private String acct_number;

     @Column(name = "ACCT_NAME")
     private String acct_name;

     @Column(name = "DATA_TYPE")
     private String data_type;

     @Column(name = "REPORT_LABEL")
     private String report_label;

     @Column(name = "REPORT_REMARKS")
     private String report_remarks;

     @Column(name = "MODIFICATION_REMARKS")
     private String modification_remarks;

     @Column(name = "DATA_ENTRY_VERSION")
     private String data_entry_version;

     @Column(name = "ACCT_BALANCE_IN_PULA")
     private BigDecimal acct_balance_in_pula;

     @Column(name = "REPORT_DATE")
     @DateTimeFormat(pattern = "yyyy-MM-dd")
     private Date report_date;

     @Column(name = "REPORT_NAME")
     private String report_name;

     @Column(name = "CREATE_USER")
     private String create_user;

     @Column(name = "CREATE_TIME")
     private Date create_time;

     @Column(name = "MODIFY_USER")
     private String modify_user;

     @Column(name = "MODIFY_TIME")
     private Date modify_time;

     @Column(name = "VERIFY_USER")
     private String verify_user;

     @Column(name = "VERIFY_TIME")
     private Date verify_time;

     @Column(name = "ENTITY_FLG")
     private Character entity_flg;

     @Column(name = "MODIFY_FLG")
     private Character modify_flg;

     @Column(name = "DEL_FLG")
     private Character del_flg;

     @Column(name = "REPORT_ADDL_CRITERIA_1")
     private String report_addl_criteria_1;

     @Column(name = "REPORT_ADDL_CRITERIA_2")
     private String report_addl_criteria_2;

     @Column(name = "REPORT_ADDL_CRITERIA_3")
     private String report_addl_criteria_3;

	 public Long getSno() {
		 return sno;
	 }

	 public void setSno(Long sno) {
		 this.sno = sno;
	 }

	 public String getCust_id() {
		 return cust_id;
	 }

	 public void setCust_id(String cust_id) {
		 this.cust_id = cust_id;
	 }

	 public String getAcct_number() {
		 return acct_number;
	 }

	 public void setAcct_number(String acct_number) {
		 this.acct_number = acct_number;
	 }

	 public String getAcct_name() {
		 return acct_name;
	 }

	 public void setAcct_name(String acct_name) {
		 this.acct_name = acct_name;
	 }

	 public String getData_type() {
		 return data_type;
	 }

	 public void setData_type(String data_type) {
		 this.data_type = data_type;
	 }

	 public String getReport_label() {
		 return report_label;
	 }

	 public void setReport_label(String report_label) {
		 this.report_label = report_label;
	 }

	 public String getReport_remarks() {
		 return report_remarks;
	 }

	 public void setReport_remarks(String report_remarks) {
		 this.report_remarks = report_remarks;
	 }

	 public String getModification_remarks() {
		 return modification_remarks;
	 }

	 public void setModification_remarks(String modification_remarks) {
		 this.modification_remarks = modification_remarks;
	 }

	 public String getData_entry_version() {
		 return data_entry_version;
	 }

	 public void setData_entry_version(String data_entry_version) {
		 this.data_entry_version = data_entry_version;
	 }

	 public BigDecimal getAcct_balance_in_pula() {
		 return acct_balance_in_pula;
	 }

	 public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
		 this.acct_balance_in_pula = acct_balance_in_pula;
	 }

	 public Date getReport_date() {
		 return report_date;
	 }

	 public void setReport_date(Date report_date) {
		 this.report_date = report_date;
	 }

	 public String getReport_name() {
		 return report_name;
	 }

	 public void setReport_name(String report_name) {
		 this.report_name = report_name;
	 }

	 public String getCreate_user() {
		 return create_user;
	 }

	 public void setCreate_user(String create_user) {
		 this.create_user = create_user;
	 }

	 public Date getCreate_time() {
		 return create_time;
	 }

	 public void setCreate_time(Date create_time) {
		 this.create_time = create_time;
	 }

	 public String getModify_user() {
		 return modify_user;
	 }

	 public void setModify_user(String modify_user) {
		 this.modify_user = modify_user;
	 }

	 public Date getModify_time() {
		 return modify_time;
	 }

	 public void setModify_time(Date modify_time) {
		 this.modify_time = modify_time;
	 }

	 public String getVerify_user() {
		 return verify_user;
	 }

	 public void setVerify_user(String verify_user) {
		 this.verify_user = verify_user;
	 }

	 public Date getVerify_time() {
		 return verify_time;
	 }

	 public void setVerify_time(Date verify_time) {
		 this.verify_time = verify_time;
	 }

	 public Character getEntity_flg() {
		 return entity_flg;
	 }

	 public void setEntity_flg(Character entity_flg) {
		 this.entity_flg = entity_flg;
	 }

	 public Character getModify_flg() {
		 return modify_flg;
	 }

	 public void setModify_flg(Character modify_flg) {
		 this.modify_flg = modify_flg;
	 }

	 public Character getDel_flg() {
		 return del_flg;
	 }

	 public void setDel_flg(Character del_flg) {
		 this.del_flg = del_flg;
	 }

	 public String getReport_addl_criteria_1() {
		 return report_addl_criteria_1;
	 }

	 public void setReport_addl_criteria_1(String report_addl_criteria_1) {
		 this.report_addl_criteria_1 = report_addl_criteria_1;
	 }

	 public String getReport_addl_criteria_2() {
		 return report_addl_criteria_2;
	 }

	 public void setReport_addl_criteria_2(String report_addl_criteria_2) {
		 this.report_addl_criteria_2 = report_addl_criteria_2;
	 }

	 public String getReport_addl_criteria_3() {
		 return report_addl_criteria_3;
	 }

	 public void setReport_addl_criteria_3(String report_addl_criteria_3) {
		 this.report_addl_criteria_3 = report_addl_criteria_3;
	 }

	 public MDISB1_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	 }

	

}
