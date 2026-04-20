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
@Table(name = "BRRS_FORMAT_I_ARCHIVALTABLE_SUMMARY")

public class FORMAT_I_Archival_Summary_Entity {
	private String	r12_product;
	private BigDecimal	r12_sch_no;
	private BigDecimal	r12_net_amount;
	private BigDecimal	r12_balance_bank;
	private BigDecimal	r12_balance_statement;
	private String	r13_product;
	private BigDecimal	r13_sch_no;
	private BigDecimal	r13_net_amount;
	private BigDecimal	r13_balance_bank;
	private BigDecimal	r13_balance_statement;
	private String	r14_product;
	private BigDecimal	r14_sch_no;
	private BigDecimal	r14_net_amount;
	private BigDecimal	r14_balance_bank;
	private BigDecimal	r14_balance_statement;
	private String	r15_product;
	private BigDecimal	r15_sch_no;
	private BigDecimal	r15_net_amount;
	private BigDecimal	r15_balance_bank;
	private BigDecimal	r15_balance_statement;
	private String	r16_product;
	private BigDecimal	r16_sch_no;
	private BigDecimal	r16_net_amount;
	private BigDecimal	r16_balance_bank;
	private BigDecimal	r16_balance_statement;
	private String	r17_product;
	private BigDecimal	r17_sch_no;
	private BigDecimal	r17_net_amount;
	private BigDecimal	r17_balance_bank;
	private BigDecimal	r17_balance_statement;
	private String	r18_product;
	private BigDecimal	r18_sch_no;
	private BigDecimal	r18_net_amount;
	private BigDecimal	r18_balance_bank;
	private BigDecimal	r18_balance_statement;
	private String	r19_product;
	private BigDecimal	r19_sch_no;
	private BigDecimal	r19_net_amount;
	private BigDecimal	r19_balance_bank;
	private BigDecimal	r19_balance_statement;
	private String	r20_product;
	private BigDecimal	r20_sch_no;
	private BigDecimal	r20_net_amount;
	private BigDecimal	r20_balance_bank;
	private BigDecimal	r20_balance_statement;
	private String	r21_product;
	private BigDecimal	r21_sch_no;
	private BigDecimal	r21_net_amount;
	private BigDecimal	r21_balance_bank;
	private BigDecimal	r21_balance_statement;
	private String	r22_product;
	private BigDecimal	r22_sch_no;
	private BigDecimal	r22_net_amount;
	private BigDecimal	r22_balance_bank;
	private BigDecimal	r22_balance_statement;
	private String	r23_product;
	private BigDecimal	r23_sch_no;
	private BigDecimal	r23_net_amount;
	private BigDecimal	r23_balance_bank;
	private BigDecimal	r23_balance_statement;
	private String	r24_product;
	private BigDecimal	r24_sch_no;
	private BigDecimal	r24_net_amount;
	private BigDecimal	r24_balance_bank;
	private BigDecimal	r24_balance_statement;
	private String	r25_product;
	private BigDecimal	r25_sch_no;
	private BigDecimal	r25_net_amount;
	private BigDecimal	r25_balance_bank;
	private BigDecimal	r25_balance_statement;
	private String	r26_product;
	private BigDecimal	r26_sch_no;
	private BigDecimal	r26_net_amount;
	private BigDecimal	r26_balance_bank;
	private BigDecimal	r26_balance_statement;
	private String	r27_product;
	private BigDecimal	r27_sch_no;
	private BigDecimal	r27_net_amount;
	private BigDecimal	r27_balance_bank;
	private BigDecimal	r27_balance_statement;
	private String	r28_product;
	private BigDecimal	r28_sch_no;
	private BigDecimal	r28_net_amount;
	private BigDecimal	r28_balance_bank;
	private BigDecimal	r28_balance_statement;
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
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public BigDecimal getR12_sch_no() {
		return r12_sch_no;
	}
	public void setR12_sch_no(BigDecimal r12_sch_no) {
		this.r12_sch_no = r12_sch_no;
	}
	public BigDecimal getR12_net_amount() {
		return r12_net_amount;
	}
	public void setR12_net_amount(BigDecimal r12_net_amount) {
		this.r12_net_amount = r12_net_amount;
	}
	public BigDecimal getR12_balance_bank() {
		return r12_balance_bank;
	}
	public void setR12_balance_bank(BigDecimal r12_balance_bank) {
		this.r12_balance_bank = r12_balance_bank;
	}
	public BigDecimal getR12_balance_statement() {
		return r12_balance_statement;
	}
	public void setR12_balance_statement(BigDecimal r12_balance_statement) {
		this.r12_balance_statement = r12_balance_statement;
	}
	public String getR13_product() {
		return r13_product;
	}
	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}
	public BigDecimal getR13_sch_no() {
		return r13_sch_no;
	}
	public void setR13_sch_no(BigDecimal r13_sch_no) {
		this.r13_sch_no = r13_sch_no;
	}
	public BigDecimal getR13_net_amount() {
		return r13_net_amount;
	}
	public void setR13_net_amount(BigDecimal r13_net_amount) {
		this.r13_net_amount = r13_net_amount;
	}
	public BigDecimal getR13_balance_bank() {
		return r13_balance_bank;
	}
	public void setR13_balance_bank(BigDecimal r13_balance_bank) {
		this.r13_balance_bank = r13_balance_bank;
	}
	public BigDecimal getR13_balance_statement() {
		return r13_balance_statement;
	}
	public void setR13_balance_statement(BigDecimal r13_balance_statement) {
		this.r13_balance_statement = r13_balance_statement;
	}
	public String getR14_product() {
		return r14_product;
	}
	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}
	public BigDecimal getR14_sch_no() {
		return r14_sch_no;
	}
	public void setR14_sch_no(BigDecimal r14_sch_no) {
		this.r14_sch_no = r14_sch_no;
	}
	public BigDecimal getR14_net_amount() {
		return r14_net_amount;
	}
	public void setR14_net_amount(BigDecimal r14_net_amount) {
		this.r14_net_amount = r14_net_amount;
	}
	public BigDecimal getR14_balance_bank() {
		return r14_balance_bank;
	}
	public void setR14_balance_bank(BigDecimal r14_balance_bank) {
		this.r14_balance_bank = r14_balance_bank;
	}
	public BigDecimal getR14_balance_statement() {
		return r14_balance_statement;
	}
	public void setR14_balance_statement(BigDecimal r14_balance_statement) {
		this.r14_balance_statement = r14_balance_statement;
	}
	public String getR15_product() {
		return r15_product;
	}
	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}
	public BigDecimal getR15_sch_no() {
		return r15_sch_no;
	}
	public void setR15_sch_no(BigDecimal r15_sch_no) {
		this.r15_sch_no = r15_sch_no;
	}
	public BigDecimal getR15_net_amount() {
		return r15_net_amount;
	}
	public void setR15_net_amount(BigDecimal r15_net_amount) {
		this.r15_net_amount = r15_net_amount;
	}
	public BigDecimal getR15_balance_bank() {
		return r15_balance_bank;
	}
	public void setR15_balance_bank(BigDecimal r15_balance_bank) {
		this.r15_balance_bank = r15_balance_bank;
	}
	public BigDecimal getR15_balance_statement() {
		return r15_balance_statement;
	}
	public void setR15_balance_statement(BigDecimal r15_balance_statement) {
		this.r15_balance_statement = r15_balance_statement;
	}
	public String getR16_product() {
		return r16_product;
	}
	public void setR16_product(String r16_product) {
		this.r16_product = r16_product;
	}
	public BigDecimal getR16_sch_no() {
		return r16_sch_no;
	}
	public void setR16_sch_no(BigDecimal r16_sch_no) {
		this.r16_sch_no = r16_sch_no;
	}
	public BigDecimal getR16_net_amount() {
		return r16_net_amount;
	}
	public void setR16_net_amount(BigDecimal r16_net_amount) {
		this.r16_net_amount = r16_net_amount;
	}
	public BigDecimal getR16_balance_bank() {
		return r16_balance_bank;
	}
	public void setR16_balance_bank(BigDecimal r16_balance_bank) {
		this.r16_balance_bank = r16_balance_bank;
	}
	public BigDecimal getR16_balance_statement() {
		return r16_balance_statement;
	}
	public void setR16_balance_statement(BigDecimal r16_balance_statement) {
		this.r16_balance_statement = r16_balance_statement;
	}
	public String getR17_product() {
		return r17_product;
	}
	public void setR17_product(String r17_product) {
		this.r17_product = r17_product;
	}
	public BigDecimal getR17_sch_no() {
		return r17_sch_no;
	}
	public void setR17_sch_no(BigDecimal r17_sch_no) {
		this.r17_sch_no = r17_sch_no;
	}
	public BigDecimal getR17_net_amount() {
		return r17_net_amount;
	}
	public void setR17_net_amount(BigDecimal r17_net_amount) {
		this.r17_net_amount = r17_net_amount;
	}
	public BigDecimal getR17_balance_bank() {
		return r17_balance_bank;
	}
	public void setR17_balance_bank(BigDecimal r17_balance_bank) {
		this.r17_balance_bank = r17_balance_bank;
	}
	public BigDecimal getR17_balance_statement() {
		return r17_balance_statement;
	}
	public void setR17_balance_statement(BigDecimal r17_balance_statement) {
		this.r17_balance_statement = r17_balance_statement;
	}
	public String getR18_product() {
		return r18_product;
	}
	public void setR18_product(String r18_product) {
		this.r18_product = r18_product;
	}
	public BigDecimal getR18_sch_no() {
		return r18_sch_no;
	}
	public void setR18_sch_no(BigDecimal r18_sch_no) {
		this.r18_sch_no = r18_sch_no;
	}
	public BigDecimal getR18_net_amount() {
		return r18_net_amount;
	}
	public void setR18_net_amount(BigDecimal r18_net_amount) {
		this.r18_net_amount = r18_net_amount;
	}
	public BigDecimal getR18_balance_bank() {
		return r18_balance_bank;
	}
	public void setR18_balance_bank(BigDecimal r18_balance_bank) {
		this.r18_balance_bank = r18_balance_bank;
	}
	public BigDecimal getR18_balance_statement() {
		return r18_balance_statement;
	}
	public void setR18_balance_statement(BigDecimal r18_balance_statement) {
		this.r18_balance_statement = r18_balance_statement;
	}
	public String getR19_product() {
		return r19_product;
	}
	public void setR19_product(String r19_product) {
		this.r19_product = r19_product;
	}
	public BigDecimal getR19_sch_no() {
		return r19_sch_no;
	}
	public void setR19_sch_no(BigDecimal r19_sch_no) {
		this.r19_sch_no = r19_sch_no;
	}
	public BigDecimal getR19_net_amount() {
		return r19_net_amount;
	}
	public void setR19_net_amount(BigDecimal r19_net_amount) {
		this.r19_net_amount = r19_net_amount;
	}
	public BigDecimal getR19_balance_bank() {
		return r19_balance_bank;
	}
	public void setR19_balance_bank(BigDecimal r19_balance_bank) {
		this.r19_balance_bank = r19_balance_bank;
	}
	public BigDecimal getR19_balance_statement() {
		return r19_balance_statement;
	}
	public void setR19_balance_statement(BigDecimal r19_balance_statement) {
		this.r19_balance_statement = r19_balance_statement;
	}
	public String getR20_product() {
		return r20_product;
	}
	public void setR20_product(String r20_product) {
		this.r20_product = r20_product;
	}
	public BigDecimal getR20_sch_no() {
		return r20_sch_no;
	}
	public void setR20_sch_no(BigDecimal r20_sch_no) {
		this.r20_sch_no = r20_sch_no;
	}
	public BigDecimal getR20_net_amount() {
		return r20_net_amount;
	}
	public void setR20_net_amount(BigDecimal r20_net_amount) {
		this.r20_net_amount = r20_net_amount;
	}
	public BigDecimal getR20_balance_bank() {
		return r20_balance_bank;
	}
	public void setR20_balance_bank(BigDecimal r20_balance_bank) {
		this.r20_balance_bank = r20_balance_bank;
	}
	public BigDecimal getR20_balance_statement() {
		return r20_balance_statement;
	}
	public void setR20_balance_statement(BigDecimal r20_balance_statement) {
		this.r20_balance_statement = r20_balance_statement;
	}
	public String getR21_product() {
		return r21_product;
	}
	public void setR21_product(String r21_product) {
		this.r21_product = r21_product;
	}
	public BigDecimal getR21_sch_no() {
		return r21_sch_no;
	}
	public void setR21_sch_no(BigDecimal r21_sch_no) {
		this.r21_sch_no = r21_sch_no;
	}
	public BigDecimal getR21_net_amount() {
		return r21_net_amount;
	}
	public void setR21_net_amount(BigDecimal r21_net_amount) {
		this.r21_net_amount = r21_net_amount;
	}
	public BigDecimal getR21_balance_bank() {
		return r21_balance_bank;
	}
	public void setR21_balance_bank(BigDecimal r21_balance_bank) {
		this.r21_balance_bank = r21_balance_bank;
	}
	public BigDecimal getR21_balance_statement() {
		return r21_balance_statement;
	}
	public void setR21_balance_statement(BigDecimal r21_balance_statement) {
		this.r21_balance_statement = r21_balance_statement;
	}
	public String getR22_product() {
		return r22_product;
	}
	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}
	public BigDecimal getR22_sch_no() {
		return r22_sch_no;
	}
	public void setR22_sch_no(BigDecimal r22_sch_no) {
		this.r22_sch_no = r22_sch_no;
	}
	public BigDecimal getR22_net_amount() {
		return r22_net_amount;
	}
	public void setR22_net_amount(BigDecimal r22_net_amount) {
		this.r22_net_amount = r22_net_amount;
	}
	public BigDecimal getR22_balance_bank() {
		return r22_balance_bank;
	}
	public void setR22_balance_bank(BigDecimal r22_balance_bank) {
		this.r22_balance_bank = r22_balance_bank;
	}
	public BigDecimal getR22_balance_statement() {
		return r22_balance_statement;
	}
	public void setR22_balance_statement(BigDecimal r22_balance_statement) {
		this.r22_balance_statement = r22_balance_statement;
	}
	public String getR23_product() {
		return r23_product;
	}
	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}
	public BigDecimal getR23_sch_no() {
		return r23_sch_no;
	}
	public void setR23_sch_no(BigDecimal r23_sch_no) {
		this.r23_sch_no = r23_sch_no;
	}
	public BigDecimal getR23_net_amount() {
		return r23_net_amount;
	}
	public void setR23_net_amount(BigDecimal r23_net_amount) {
		this.r23_net_amount = r23_net_amount;
	}
	public BigDecimal getR23_balance_bank() {
		return r23_balance_bank;
	}
	public void setR23_balance_bank(BigDecimal r23_balance_bank) {
		this.r23_balance_bank = r23_balance_bank;
	}
	public BigDecimal getR23_balance_statement() {
		return r23_balance_statement;
	}
	public void setR23_balance_statement(BigDecimal r23_balance_statement) {
		this.r23_balance_statement = r23_balance_statement;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_sch_no() {
		return r24_sch_no;
	}
	public void setR24_sch_no(BigDecimal r24_sch_no) {
		this.r24_sch_no = r24_sch_no;
	}
	public BigDecimal getR24_net_amount() {
		return r24_net_amount;
	}
	public void setR24_net_amount(BigDecimal r24_net_amount) {
		this.r24_net_amount = r24_net_amount;
	}
	public BigDecimal getR24_balance_bank() {
		return r24_balance_bank;
	}
	public void setR24_balance_bank(BigDecimal r24_balance_bank) {
		this.r24_balance_bank = r24_balance_bank;
	}
	public BigDecimal getR24_balance_statement() {
		return r24_balance_statement;
	}
	public void setR24_balance_statement(BigDecimal r24_balance_statement) {
		this.r24_balance_statement = r24_balance_statement;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_sch_no() {
		return r25_sch_no;
	}
	public void setR25_sch_no(BigDecimal r25_sch_no) {
		this.r25_sch_no = r25_sch_no;
	}
	public BigDecimal getR25_net_amount() {
		return r25_net_amount;
	}
	public void setR25_net_amount(BigDecimal r25_net_amount) {
		this.r25_net_amount = r25_net_amount;
	}
	public BigDecimal getR25_balance_bank() {
		return r25_balance_bank;
	}
	public void setR25_balance_bank(BigDecimal r25_balance_bank) {
		this.r25_balance_bank = r25_balance_bank;
	}
	public BigDecimal getR25_balance_statement() {
		return r25_balance_statement;
	}
	public void setR25_balance_statement(BigDecimal r25_balance_statement) {
		this.r25_balance_statement = r25_balance_statement;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_sch_no() {
		return r26_sch_no;
	}
	public void setR26_sch_no(BigDecimal r26_sch_no) {
		this.r26_sch_no = r26_sch_no;
	}
	public BigDecimal getR26_net_amount() {
		return r26_net_amount;
	}
	public void setR26_net_amount(BigDecimal r26_net_amount) {
		this.r26_net_amount = r26_net_amount;
	}
	public BigDecimal getR26_balance_bank() {
		return r26_balance_bank;
	}
	public void setR26_balance_bank(BigDecimal r26_balance_bank) {
		this.r26_balance_bank = r26_balance_bank;
	}
	public BigDecimal getR26_balance_statement() {
		return r26_balance_statement;
	}
	public void setR26_balance_statement(BigDecimal r26_balance_statement) {
		this.r26_balance_statement = r26_balance_statement;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_sch_no() {
		return r27_sch_no;
	}
	public void setR27_sch_no(BigDecimal r27_sch_no) {
		this.r27_sch_no = r27_sch_no;
	}
	public BigDecimal getR27_net_amount() {
		return r27_net_amount;
	}
	public void setR27_net_amount(BigDecimal r27_net_amount) {
		this.r27_net_amount = r27_net_amount;
	}
	public BigDecimal getR27_balance_bank() {
		return r27_balance_bank;
	}
	public void setR27_balance_bank(BigDecimal r27_balance_bank) {
		this.r27_balance_bank = r27_balance_bank;
	}
	public BigDecimal getR27_balance_statement() {
		return r27_balance_statement;
	}
	public void setR27_balance_statement(BigDecimal r27_balance_statement) {
		this.r27_balance_statement = r27_balance_statement;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public BigDecimal getR28_sch_no() {
		return r28_sch_no;
	}
	public void setR28_sch_no(BigDecimal r28_sch_no) {
		this.r28_sch_no = r28_sch_no;
	}
	public BigDecimal getR28_net_amount() {
		return r28_net_amount;
	}
	public void setR28_net_amount(BigDecimal r28_net_amount) {
		this.r28_net_amount = r28_net_amount;
	}
	public BigDecimal getR28_balance_bank() {
		return r28_balance_bank;
	}
	public void setR28_balance_bank(BigDecimal r28_balance_bank) {
		this.r28_balance_bank = r28_balance_bank;
	}
	public BigDecimal getR28_balance_statement() {
		return r28_balance_statement;
	}
	public void setR28_balance_statement(BigDecimal r28_balance_statement) {
		this.r28_balance_statement = r28_balance_statement;
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
	public FORMAT_I_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
