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
@Table(name = "BRRS_M_P_L_SUMMARYTABLE")
public class M_P_L_Summary_Entity {


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

// ---------- R08 ----------
    private BigDecimal R08_SCH_NO;
    private BigDecimal R08_NET_AMT;
    private BigDecimal R08_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R09 ----------
    private String R09_PRODUCT;
    private BigDecimal R09_SCH_NO;
    private BigDecimal R09_NET_AMT;
    private BigDecimal R09_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R10 ----------
    private String R10_PRODUCT;
    private BigDecimal R10_SCH_NO;
    private BigDecimal R10_NET_AMT;
    private BigDecimal R10_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R11 ----------
    private String R11_PRODUCT;
    private BigDecimal R11_SCH_NO;
    private BigDecimal R11_NET_AMT;
    private BigDecimal R11_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R12 ----------
    private String R12_PRODUCT;
    private BigDecimal R12_SCH_NO;
    private BigDecimal R12_NET_AMT;
    private BigDecimal R12_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R13 ----------
    private String R13_PRODUCT;
    private BigDecimal R13_SCH_NO;
    private BigDecimal R13_NET_AMT;
    private BigDecimal R13_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R14 ----------
    private String R14_PRODUCT;
    private BigDecimal R14_SCH_NO;
    private BigDecimal R14_NET_AMT;
    private BigDecimal R14_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R15 ----------
    private String R15_PRODUCT;
    private BigDecimal R15_SCH_NO;
    private BigDecimal R15_NET_AMT;
    private BigDecimal R15_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R16 ----------
    private String R16_PRODUCT;
    private BigDecimal R16_SCH_NO;
    private BigDecimal R16_NET_AMT;
    private BigDecimal R16_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R17 ----------
    private String R17_PRODUCT;
    private BigDecimal R17_SCH_NO;
    private BigDecimal R17_NET_AMT;
    private BigDecimal R17_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R18 ----------
    private String R18_PRODUCT;
    private BigDecimal R18_SCH_NO;
    private BigDecimal R18_NET_AMT;
    private BigDecimal R18_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R19 ----------
    private String R19_PRODUCT;
    private BigDecimal R19_SCH_NO;
    private BigDecimal R19_NET_AMT;
    private BigDecimal R19_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R20 ----------
    private String R20_PRODUCT;
    private BigDecimal R20_SCH_NO;
    private BigDecimal R20_NET_AMT;
    private BigDecimal R20_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R21 ----------
    private String R21_PRODUCT;
    private BigDecimal R21_SCH_NO;
    private BigDecimal R21_NET_AMT;
    private BigDecimal R21_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

    // ---------- R22 ----------
    private String R22_PRODUCT;
    private BigDecimal R22_SCH_NO;
    private BigDecimal R22_NET_AMT;
    private BigDecimal R22_BAL_W_BOB_AND_OTHR_SUBS;
    private BigDecimal R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;




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




	public BigDecimal getR08_SCH_NO() {
		return R08_SCH_NO;
	}




	public void setR08_SCH_NO(BigDecimal r08_SCH_NO) {
		R08_SCH_NO = r08_SCH_NO;
	}




	public BigDecimal getR08_NET_AMT() {
		return R08_NET_AMT;
	}




	public void setR08_NET_AMT(BigDecimal r08_NET_AMT) {
		R08_NET_AMT = r08_NET_AMT;
	}




	public BigDecimal getR08_BAL_W_BOB_AND_OTHR_SUBS() {
		return R08_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR08_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r08_BAL_W_BOB_AND_OTHR_SUBS) {
		R08_BAL_W_BOB_AND_OTHR_SUBS = r08_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r08_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR09_PRODUCT() {
		return R09_PRODUCT;
	}




	public void setR09_PRODUCT(String r09_PRODUCT) {
		R09_PRODUCT = r09_PRODUCT;
	}




	public BigDecimal getR09_SCH_NO() {
		return R09_SCH_NO;
	}




	public void setR09_SCH_NO(BigDecimal r09_SCH_NO) {
		R09_SCH_NO = r09_SCH_NO;
	}




	public BigDecimal getR09_NET_AMT() {
		return R09_NET_AMT;
	}




	public void setR09_NET_AMT(BigDecimal r09_NET_AMT) {
		R09_NET_AMT = r09_NET_AMT;
	}




	public BigDecimal getR09_BAL_W_BOB_AND_OTHR_SUBS() {
		return R09_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR09_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r09_BAL_W_BOB_AND_OTHR_SUBS) {
		R09_BAL_W_BOB_AND_OTHR_SUBS = r09_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r09_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}




	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}




	public BigDecimal getR10_SCH_NO() {
		return R10_SCH_NO;
	}




	public void setR10_SCH_NO(BigDecimal r10_SCH_NO) {
		R10_SCH_NO = r10_SCH_NO;
	}




	public BigDecimal getR10_NET_AMT() {
		return R10_NET_AMT;
	}




	public void setR10_NET_AMT(BigDecimal r10_NET_AMT) {
		R10_NET_AMT = r10_NET_AMT;
	}




	public BigDecimal getR10_BAL_W_BOB_AND_OTHR_SUBS() {
		return R10_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR10_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r10_BAL_W_BOB_AND_OTHR_SUBS) {
		R10_BAL_W_BOB_AND_OTHR_SUBS = r10_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r10_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}




	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}




	public BigDecimal getR11_SCH_NO() {
		return R11_SCH_NO;
	}




	public void setR11_SCH_NO(BigDecimal r11_SCH_NO) {
		R11_SCH_NO = r11_SCH_NO;
	}




	public BigDecimal getR11_NET_AMT() {
		return R11_NET_AMT;
	}




	public void setR11_NET_AMT(BigDecimal r11_NET_AMT) {
		R11_NET_AMT = r11_NET_AMT;
	}




	public BigDecimal getR11_BAL_W_BOB_AND_OTHR_SUBS() {
		return R11_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR11_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r11_BAL_W_BOB_AND_OTHR_SUBS) {
		R11_BAL_W_BOB_AND_OTHR_SUBS = r11_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r11_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}




	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}




	public BigDecimal getR12_SCH_NO() {
		return R12_SCH_NO;
	}




	public void setR12_SCH_NO(BigDecimal r12_SCH_NO) {
		R12_SCH_NO = r12_SCH_NO;
	}




	public BigDecimal getR12_NET_AMT() {
		return R12_NET_AMT;
	}




	public void setR12_NET_AMT(BigDecimal r12_NET_AMT) {
		R12_NET_AMT = r12_NET_AMT;
	}




	public BigDecimal getR12_BAL_W_BOB_AND_OTHR_SUBS() {
		return R12_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR12_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r12_BAL_W_BOB_AND_OTHR_SUBS) {
		R12_BAL_W_BOB_AND_OTHR_SUBS = r12_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r12_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}




	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}




	public BigDecimal getR13_SCH_NO() {
		return R13_SCH_NO;
	}




	public void setR13_SCH_NO(BigDecimal r13_SCH_NO) {
		R13_SCH_NO = r13_SCH_NO;
	}




	public BigDecimal getR13_NET_AMT() {
		return R13_NET_AMT;
	}




	public void setR13_NET_AMT(BigDecimal r13_NET_AMT) {
		R13_NET_AMT = r13_NET_AMT;
	}




	public BigDecimal getR13_BAL_W_BOB_AND_OTHR_SUBS() {
		return R13_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR13_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r13_BAL_W_BOB_AND_OTHR_SUBS) {
		R13_BAL_W_BOB_AND_OTHR_SUBS = r13_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r13_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}




	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}




	public BigDecimal getR14_SCH_NO() {
		return R14_SCH_NO;
	}




	public void setR14_SCH_NO(BigDecimal r14_SCH_NO) {
		R14_SCH_NO = r14_SCH_NO;
	}




	public BigDecimal getR14_NET_AMT() {
		return R14_NET_AMT;
	}




	public void setR14_NET_AMT(BigDecimal r14_NET_AMT) {
		R14_NET_AMT = r14_NET_AMT;
	}




	public BigDecimal getR14_BAL_W_BOB_AND_OTHR_SUBS() {
		return R14_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR14_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r14_BAL_W_BOB_AND_OTHR_SUBS) {
		R14_BAL_W_BOB_AND_OTHR_SUBS = r14_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r14_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}




	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}




	public BigDecimal getR15_SCH_NO() {
		return R15_SCH_NO;
	}




	public void setR15_SCH_NO(BigDecimal r15_SCH_NO) {
		R15_SCH_NO = r15_SCH_NO;
	}




	public BigDecimal getR15_NET_AMT() {
		return R15_NET_AMT;
	}




	public void setR15_NET_AMT(BigDecimal r15_NET_AMT) {
		R15_NET_AMT = r15_NET_AMT;
	}




	public BigDecimal getR15_BAL_W_BOB_AND_OTHR_SUBS() {
		return R15_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR15_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r15_BAL_W_BOB_AND_OTHR_SUBS) {
		R15_BAL_W_BOB_AND_OTHR_SUBS = r15_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r15_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}




	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}




	public BigDecimal getR16_SCH_NO() {
		return R16_SCH_NO;
	}




	public void setR16_SCH_NO(BigDecimal r16_SCH_NO) {
		R16_SCH_NO = r16_SCH_NO;
	}




	public BigDecimal getR16_NET_AMT() {
		return R16_NET_AMT;
	}




	public void setR16_NET_AMT(BigDecimal r16_NET_AMT) {
		R16_NET_AMT = r16_NET_AMT;
	}




	public BigDecimal getR16_BAL_W_BOB_AND_OTHR_SUBS() {
		return R16_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR16_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r16_BAL_W_BOB_AND_OTHR_SUBS) {
		R16_BAL_W_BOB_AND_OTHR_SUBS = r16_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r16_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}




	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}




	public BigDecimal getR17_SCH_NO() {
		return R17_SCH_NO;
	}




	public void setR17_SCH_NO(BigDecimal r17_SCH_NO) {
		R17_SCH_NO = r17_SCH_NO;
	}




	public BigDecimal getR17_NET_AMT() {
		return R17_NET_AMT;
	}




	public void setR17_NET_AMT(BigDecimal r17_NET_AMT) {
		R17_NET_AMT = r17_NET_AMT;
	}




	public BigDecimal getR17_BAL_W_BOB_AND_OTHR_SUBS() {
		return R17_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR17_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r17_BAL_W_BOB_AND_OTHR_SUBS) {
		R17_BAL_W_BOB_AND_OTHR_SUBS = r17_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r17_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}




	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}




	public BigDecimal getR18_SCH_NO() {
		return R18_SCH_NO;
	}




	public void setR18_SCH_NO(BigDecimal r18_SCH_NO) {
		R18_SCH_NO = r18_SCH_NO;
	}




	public BigDecimal getR18_NET_AMT() {
		return R18_NET_AMT;
	}




	public void setR18_NET_AMT(BigDecimal r18_NET_AMT) {
		R18_NET_AMT = r18_NET_AMT;
	}




	public BigDecimal getR18_BAL_W_BOB_AND_OTHR_SUBS() {
		return R18_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR18_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r18_BAL_W_BOB_AND_OTHR_SUBS) {
		R18_BAL_W_BOB_AND_OTHR_SUBS = r18_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r18_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}




	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}




	public BigDecimal getR19_SCH_NO() {
		return R19_SCH_NO;
	}




	public void setR19_SCH_NO(BigDecimal r19_SCH_NO) {
		R19_SCH_NO = r19_SCH_NO;
	}




	public BigDecimal getR19_NET_AMT() {
		return R19_NET_AMT;
	}




	public void setR19_NET_AMT(BigDecimal r19_NET_AMT) {
		R19_NET_AMT = r19_NET_AMT;
	}




	public BigDecimal getR19_BAL_W_BOB_AND_OTHR_SUBS() {
		return R19_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR19_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r19_BAL_W_BOB_AND_OTHR_SUBS) {
		R19_BAL_W_BOB_AND_OTHR_SUBS = r19_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r19_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}




	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}




	public BigDecimal getR20_SCH_NO() {
		return R20_SCH_NO;
	}




	public void setR20_SCH_NO(BigDecimal r20_SCH_NO) {
		R20_SCH_NO = r20_SCH_NO;
	}




	public BigDecimal getR20_NET_AMT() {
		return R20_NET_AMT;
	}




	public void setR20_NET_AMT(BigDecimal r20_NET_AMT) {
		R20_NET_AMT = r20_NET_AMT;
	}




	public BigDecimal getR20_BAL_W_BOB_AND_OTHR_SUBS() {
		return R20_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR20_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r20_BAL_W_BOB_AND_OTHR_SUBS) {
		R20_BAL_W_BOB_AND_OTHR_SUBS = r20_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r20_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}




	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}




	public BigDecimal getR21_SCH_NO() {
		return R21_SCH_NO;
	}




	public void setR21_SCH_NO(BigDecimal r21_SCH_NO) {
		R21_SCH_NO = r21_SCH_NO;
	}




	public BigDecimal getR21_NET_AMT() {
		return R21_NET_AMT;
	}




	public void setR21_NET_AMT(BigDecimal r21_NET_AMT) {
		R21_NET_AMT = r21_NET_AMT;
	}




	public BigDecimal getR21_BAL_W_BOB_AND_OTHR_SUBS() {
		return R21_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR21_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r21_BAL_W_BOB_AND_OTHR_SUBS) {
		R21_BAL_W_BOB_AND_OTHR_SUBS = r21_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r21_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}




	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}




	public BigDecimal getR22_SCH_NO() {
		return R22_SCH_NO;
	}




	public void setR22_SCH_NO(BigDecimal r22_SCH_NO) {
		R22_SCH_NO = r22_SCH_NO;
	}




	public BigDecimal getR22_NET_AMT() {
		return R22_NET_AMT;
	}




	public void setR22_NET_AMT(BigDecimal r22_NET_AMT) {
		R22_NET_AMT = r22_NET_AMT;
	}




	public BigDecimal getR22_BAL_W_BOB_AND_OTHR_SUBS() {
		return R22_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public void setR22_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r22_BAL_W_BOB_AND_OTHR_SUBS) {
		R22_BAL_W_BOB_AND_OTHR_SUBS = r22_BAL_W_BOB_AND_OTHR_SUBS;
	}




	public BigDecimal getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
		return R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




	public void setR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r22_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
		R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
	}




public M_P_L_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}








}
