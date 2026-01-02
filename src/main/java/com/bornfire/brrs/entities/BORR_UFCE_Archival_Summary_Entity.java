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
@Table(name = "BRRS_BORR_UFCE_ARCHIVALTABLE_SUMMARY")
public class BORR_UFCE_Archival_Summary_Entity {


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

private String R4_CUST_ID;
private BigDecimal R4_ACCT_NO;
private String R4_ACCT_NAME;
private String R4_SCHM_CODE;
private String R4_SCHM_DESC;
private Date R4_ACCT_OPN_DATE;
private String R4_CCY;
private BigDecimal R4_BAL_EQUI_TO_BWP;
private BigDecimal R4_SANCTION_AMT_BWP;
private BigDecimal R4_INT_RATE;
private BigDecimal R4_AMT_IN_INR;
private BigDecimal R4_VALUE_1;
private BigDecimal R4_VALUE_2;
private String R5_CUST_ID;
private BigDecimal R5_ACCT_NO;
private String R5_ACCT_NAME;
private String R5_SCHM_CODE;
private String R5_SCHM_DESC;
private Date R5_ACCT_OPN_DATE;
private String R5_CCY;
private BigDecimal R5_BAL_EQUI_TO_BWP;
private BigDecimal R5_SANCTION_AMT_BWP;
private BigDecimal R5_INT_RATE;
private BigDecimal R5_AMT_IN_INR;
private BigDecimal R5_VALUE_1;
private BigDecimal R5_VALUE_2;
private String R6_CUST_ID;
private BigDecimal R6_ACCT_NO;
private String R6_ACCT_NAME;
private String R6_SCHM_CODE;
private String R6_SCHM_DESC;
private Date R6_ACCT_OPN_DATE;
private String R6_CCY;
private BigDecimal R6_BAL_EQUI_TO_BWP;
private BigDecimal R6_SANCTION_AMT_BWP;
private BigDecimal R6_INT_RATE;
private BigDecimal R6_AMT_IN_INR;
private BigDecimal R6_VALUE_1;
private BigDecimal R6_VALUE_2;
private String R7_CUST_ID;
private BigDecimal R7_ACCT_NO;
private String R7_ACCT_NAME;
private String R7_SCHM_CODE;
private String R7_SCHM_DESC;
private Date R7_ACCT_OPN_DATE;
private String R7_CCY;
private BigDecimal R7_BAL_EQUI_TO_BWP;
private BigDecimal R7_SANCTION_AMT_BWP;
private BigDecimal R7_INT_RATE;
private BigDecimal R7_AMT_IN_INR;
private BigDecimal R7_VALUE_1;
private BigDecimal R7_VALUE_2;
private String R8_CUST_ID;
private BigDecimal R8_ACCT_NO;
private String R8_ACCT_NAME;
private String R8_SCHM_CODE;
private String R8_SCHM_DESC;
private Date R8_ACCT_OPN_DATE;
private String R8_CCY;
private BigDecimal R8_BAL_EQUI_TO_BWP;
private BigDecimal R8_SANCTION_AMT_BWP;
private BigDecimal R8_INT_RATE;
private BigDecimal R8_AMT_IN_INR;
private BigDecimal R8_VALUE_1;
private BigDecimal R8_VALUE_2;
private String R9_CUST_ID;
private BigDecimal R9_ACCT_NO;
private String R9_ACCT_NAME;
private String R9_SCHM_CODE;
private String R9_SCHM_DESC;
private Date R9_ACCT_OPN_DATE;
private String R9_CCY;
private BigDecimal R9_BAL_EQUI_TO_BWP;
private BigDecimal R9_SANCTION_AMT_BWP;
private BigDecimal R9_INT_RATE;
private BigDecimal R9_AMT_IN_INR;
private BigDecimal R9_VALUE_1;
private BigDecimal R9_VALUE_2;
private String R10_CUST_ID;
private BigDecimal R10_ACCT_NO;
private String R10_ACCT_NAME;
private String R10_SCHM_CODE;
private String R10_SCHM_DESC;
private Date R10_ACCT_OPN_DATE;
private String R10_CCY;
private BigDecimal R10_BAL_EQUI_TO_BWP;
private BigDecimal R10_SANCTION_AMT_BWP;
private BigDecimal R10_INT_RATE;
private BigDecimal R10_AMT_IN_INR;
private BigDecimal R10_VALUE_1;
private BigDecimal R10_VALUE_2;
private String R11_CUST_ID;
private BigDecimal R11_ACCT_NO;
private String R11_ACCT_NAME;
private String R11_SCHM_CODE;
private String R11_SCHM_DESC;
private Date R11_ACCT_OPN_DATE;
private String R11_CCY;
private BigDecimal R11_BAL_EQUI_TO_BWP;
private BigDecimal R11_SANCTION_AMT_BWP;
private BigDecimal R11_INT_RATE;
private BigDecimal R11_AMT_IN_INR;
private BigDecimal R11_VALUE_1;
private BigDecimal R11_VALUE_2;
private String R12_CUST_ID;
private BigDecimal R12_ACCT_NO;
private String R12_ACCT_NAME;
private String R12_SCHM_CODE;
private String R12_SCHM_DESC;
private Date R12_ACCT_OPN_DATE;
private String R12_CCY;
private BigDecimal R12_BAL_EQUI_TO_BWP;
private BigDecimal R12_SANCTION_AMT_BWP;
private BigDecimal R12_INT_RATE;
private BigDecimal R12_AMT_IN_INR;
private BigDecimal R12_VALUE_1;
private BigDecimal R12_VALUE_2;
private BigDecimal R3_VAL_MULTIPLY_AMT_IN_INR;
private BigDecimal R3_VAL_DIVIDE_AMT_IN_INR;
private BigDecimal R14_AMT_IN_INR;
private BigDecimal R14_VALUE_2;





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





public String getR4_CUST_ID() {
	return R4_CUST_ID;
}





public void setR4_CUST_ID(String r4_CUST_ID) {
	R4_CUST_ID = r4_CUST_ID;
}





public BigDecimal getR4_ACCT_NO() {
	return R4_ACCT_NO;
}





public void setR4_ACCT_NO(BigDecimal r4_ACCT_NO) {
	R4_ACCT_NO = r4_ACCT_NO;
}





public String getR4_ACCT_NAME() {
	return R4_ACCT_NAME;
}





public void setR4_ACCT_NAME(String r4_ACCT_NAME) {
	R4_ACCT_NAME = r4_ACCT_NAME;
}





public String getR4_SCHM_CODE() {
	return R4_SCHM_CODE;
}





public void setR4_SCHM_CODE(String r4_SCHM_CODE) {
	R4_SCHM_CODE = r4_SCHM_CODE;
}





public String getR4_SCHM_DESC() {
	return R4_SCHM_DESC;
}





public void setR4_SCHM_DESC(String r4_SCHM_DESC) {
	R4_SCHM_DESC = r4_SCHM_DESC;
}





public Date getR4_ACCT_OPN_DATE() {
	return R4_ACCT_OPN_DATE;
}





public void setR4_ACCT_OPN_DATE(Date r4_ACCT_OPN_DATE) {
	R4_ACCT_OPN_DATE = r4_ACCT_OPN_DATE;
}





public String getR4_CCY() {
	return R4_CCY;
}





public void setR4_CCY(String r4_CCY) {
	R4_CCY = r4_CCY;
}





public BigDecimal getR4_BAL_EQUI_TO_BWP() {
	return R4_BAL_EQUI_TO_BWP;
}





public void setR4_BAL_EQUI_TO_BWP(BigDecimal r4_BAL_EQUI_TO_BWP) {
	R4_BAL_EQUI_TO_BWP = r4_BAL_EQUI_TO_BWP;
}





public BigDecimal getR4_SANCTION_AMT_BWP() {
	return R4_SANCTION_AMT_BWP;
}





public void setR4_SANCTION_AMT_BWP(BigDecimal r4_SANCTION_AMT_BWP) {
	R4_SANCTION_AMT_BWP = r4_SANCTION_AMT_BWP;
}





public BigDecimal getR4_INT_RATE() {
	return R4_INT_RATE;
}





public void setR4_INT_RATE(BigDecimal r4_INT_RATE) {
	R4_INT_RATE = r4_INT_RATE;
}





public BigDecimal getR4_AMT_IN_INR() {
	return R4_AMT_IN_INR;
}





public void setR4_AMT_IN_INR(BigDecimal r4_AMT_IN_INR) {
	R4_AMT_IN_INR = r4_AMT_IN_INR;
}





public BigDecimal getR4_VALUE_1() {
	return R4_VALUE_1;
}





public void setR4_VALUE_1(BigDecimal r4_VALUE_1) {
	R4_VALUE_1 = r4_VALUE_1;
}





public BigDecimal getR4_VALUE_2() {
	return R4_VALUE_2;
}





public void setR4_VALUE_2(BigDecimal r4_VALUE_2) {
	R4_VALUE_2 = r4_VALUE_2;
}





public String getR5_CUST_ID() {
	return R5_CUST_ID;
}





public void setR5_CUST_ID(String r5_CUST_ID) {
	R5_CUST_ID = r5_CUST_ID;
}





public BigDecimal getR5_ACCT_NO() {
	return R5_ACCT_NO;
}





public void setR5_ACCT_NO(BigDecimal r5_ACCT_NO) {
	R5_ACCT_NO = r5_ACCT_NO;
}





public String getR5_ACCT_NAME() {
	return R5_ACCT_NAME;
}





public void setR5_ACCT_NAME(String r5_ACCT_NAME) {
	R5_ACCT_NAME = r5_ACCT_NAME;
}





public String getR5_SCHM_CODE() {
	return R5_SCHM_CODE;
}





public void setR5_SCHM_CODE(String r5_SCHM_CODE) {
	R5_SCHM_CODE = r5_SCHM_CODE;
}





public String getR5_SCHM_DESC() {
	return R5_SCHM_DESC;
}





public void setR5_SCHM_DESC(String r5_SCHM_DESC) {
	R5_SCHM_DESC = r5_SCHM_DESC;
}





public Date getR5_ACCT_OPN_DATE() {
	return R5_ACCT_OPN_DATE;
}





public void setR5_ACCT_OPN_DATE(Date r5_ACCT_OPN_DATE) {
	R5_ACCT_OPN_DATE = r5_ACCT_OPN_DATE;
}





public String getR5_CCY() {
	return R5_CCY;
}





public void setR5_CCY(String r5_CCY) {
	R5_CCY = r5_CCY;
}





public BigDecimal getR5_BAL_EQUI_TO_BWP() {
	return R5_BAL_EQUI_TO_BWP;
}





public void setR5_BAL_EQUI_TO_BWP(BigDecimal r5_BAL_EQUI_TO_BWP) {
	R5_BAL_EQUI_TO_BWP = r5_BAL_EQUI_TO_BWP;
}





public BigDecimal getR5_SANCTION_AMT_BWP() {
	return R5_SANCTION_AMT_BWP;
}





public void setR5_SANCTION_AMT_BWP(BigDecimal r5_SANCTION_AMT_BWP) {
	R5_SANCTION_AMT_BWP = r5_SANCTION_AMT_BWP;
}





public BigDecimal getR5_INT_RATE() {
	return R5_INT_RATE;
}





public void setR5_INT_RATE(BigDecimal r5_INT_RATE) {
	R5_INT_RATE = r5_INT_RATE;
}





public BigDecimal getR5_AMT_IN_INR() {
	return R5_AMT_IN_INR;
}





public void setR5_AMT_IN_INR(BigDecimal r5_AMT_IN_INR) {
	R5_AMT_IN_INR = r5_AMT_IN_INR;
}





public BigDecimal getR5_VALUE_1() {
	return R5_VALUE_1;
}





public void setR5_VALUE_1(BigDecimal r5_VALUE_1) {
	R5_VALUE_1 = r5_VALUE_1;
}





public BigDecimal getR5_VALUE_2() {
	return R5_VALUE_2;
}





public void setR5_VALUE_2(BigDecimal r5_VALUE_2) {
	R5_VALUE_2 = r5_VALUE_2;
}





public String getR6_CUST_ID() {
	return R6_CUST_ID;
}





public void setR6_CUST_ID(String r6_CUST_ID) {
	R6_CUST_ID = r6_CUST_ID;
}





public BigDecimal getR6_ACCT_NO() {
	return R6_ACCT_NO;
}





public void setR6_ACCT_NO(BigDecimal r6_ACCT_NO) {
	R6_ACCT_NO = r6_ACCT_NO;
}





public String getR6_ACCT_NAME() {
	return R6_ACCT_NAME;
}





public void setR6_ACCT_NAME(String r6_ACCT_NAME) {
	R6_ACCT_NAME = r6_ACCT_NAME;
}





public String getR6_SCHM_CODE() {
	return R6_SCHM_CODE;
}





public void setR6_SCHM_CODE(String r6_SCHM_CODE) {
	R6_SCHM_CODE = r6_SCHM_CODE;
}





public String getR6_SCHM_DESC() {
	return R6_SCHM_DESC;
}





public void setR6_SCHM_DESC(String r6_SCHM_DESC) {
	R6_SCHM_DESC = r6_SCHM_DESC;
}





public Date getR6_ACCT_OPN_DATE() {
	return R6_ACCT_OPN_DATE;
}





public void setR6_ACCT_OPN_DATE(Date r6_ACCT_OPN_DATE) {
	R6_ACCT_OPN_DATE = r6_ACCT_OPN_DATE;
}





public String getR6_CCY() {
	return R6_CCY;
}





public void setR6_CCY(String r6_CCY) {
	R6_CCY = r6_CCY;
}





public BigDecimal getR6_BAL_EQUI_TO_BWP() {
	return R6_BAL_EQUI_TO_BWP;
}





public void setR6_BAL_EQUI_TO_BWP(BigDecimal r6_BAL_EQUI_TO_BWP) {
	R6_BAL_EQUI_TO_BWP = r6_BAL_EQUI_TO_BWP;
}





public BigDecimal getR6_SANCTION_AMT_BWP() {
	return R6_SANCTION_AMT_BWP;
}





public void setR6_SANCTION_AMT_BWP(BigDecimal r6_SANCTION_AMT_BWP) {
	R6_SANCTION_AMT_BWP = r6_SANCTION_AMT_BWP;
}





public BigDecimal getR6_INT_RATE() {
	return R6_INT_RATE;
}





public void setR6_INT_RATE(BigDecimal r6_INT_RATE) {
	R6_INT_RATE = r6_INT_RATE;
}





public BigDecimal getR6_AMT_IN_INR() {
	return R6_AMT_IN_INR;
}





public void setR6_AMT_IN_INR(BigDecimal r6_AMT_IN_INR) {
	R6_AMT_IN_INR = r6_AMT_IN_INR;
}





public BigDecimal getR6_VALUE_1() {
	return R6_VALUE_1;
}





public void setR6_VALUE_1(BigDecimal r6_VALUE_1) {
	R6_VALUE_1 = r6_VALUE_1;
}





public BigDecimal getR6_VALUE_2() {
	return R6_VALUE_2;
}





public void setR6_VALUE_2(BigDecimal r6_VALUE_2) {
	R6_VALUE_2 = r6_VALUE_2;
}





public String getR7_CUST_ID() {
	return R7_CUST_ID;
}





public void setR7_CUST_ID(String r7_CUST_ID) {
	R7_CUST_ID = r7_CUST_ID;
}





public BigDecimal getR7_ACCT_NO() {
	return R7_ACCT_NO;
}





public void setR7_ACCT_NO(BigDecimal r7_ACCT_NO) {
	R7_ACCT_NO = r7_ACCT_NO;
}





public String getR7_ACCT_NAME() {
	return R7_ACCT_NAME;
}





public void setR7_ACCT_NAME(String r7_ACCT_NAME) {
	R7_ACCT_NAME = r7_ACCT_NAME;
}





public String getR7_SCHM_CODE() {
	return R7_SCHM_CODE;
}





public void setR7_SCHM_CODE(String r7_SCHM_CODE) {
	R7_SCHM_CODE = r7_SCHM_CODE;
}





public String getR7_SCHM_DESC() {
	return R7_SCHM_DESC;
}





public void setR7_SCHM_DESC(String r7_SCHM_DESC) {
	R7_SCHM_DESC = r7_SCHM_DESC;
}





public Date getR7_ACCT_OPN_DATE() {
	return R7_ACCT_OPN_DATE;
}





public void setR7_ACCT_OPN_DATE(Date r7_ACCT_OPN_DATE) {
	R7_ACCT_OPN_DATE = r7_ACCT_OPN_DATE;
}





public String getR7_CCY() {
	return R7_CCY;
}





public void setR7_CCY(String r7_CCY) {
	R7_CCY = r7_CCY;
}





public BigDecimal getR7_BAL_EQUI_TO_BWP() {
	return R7_BAL_EQUI_TO_BWP;
}





public void setR7_BAL_EQUI_TO_BWP(BigDecimal r7_BAL_EQUI_TO_BWP) {
	R7_BAL_EQUI_TO_BWP = r7_BAL_EQUI_TO_BWP;
}





public BigDecimal getR7_SANCTION_AMT_BWP() {
	return R7_SANCTION_AMT_BWP;
}





public void setR7_SANCTION_AMT_BWP(BigDecimal r7_SANCTION_AMT_BWP) {
	R7_SANCTION_AMT_BWP = r7_SANCTION_AMT_BWP;
}





public BigDecimal getR7_INT_RATE() {
	return R7_INT_RATE;
}





public void setR7_INT_RATE(BigDecimal r7_INT_RATE) {
	R7_INT_RATE = r7_INT_RATE;
}





public BigDecimal getR7_AMT_IN_INR() {
	return R7_AMT_IN_INR;
}





public void setR7_AMT_IN_INR(BigDecimal r7_AMT_IN_INR) {
	R7_AMT_IN_INR = r7_AMT_IN_INR;
}





public BigDecimal getR7_VALUE_1() {
	return R7_VALUE_1;
}





public void setR7_VALUE_1(BigDecimal r7_VALUE_1) {
	R7_VALUE_1 = r7_VALUE_1;
}





public BigDecimal getR7_VALUE_2() {
	return R7_VALUE_2;
}





public void setR7_VALUE_2(BigDecimal r7_VALUE_2) {
	R7_VALUE_2 = r7_VALUE_2;
}





public String getR8_CUST_ID() {
	return R8_CUST_ID;
}





public void setR8_CUST_ID(String r8_CUST_ID) {
	R8_CUST_ID = r8_CUST_ID;
}





public BigDecimal getR8_ACCT_NO() {
	return R8_ACCT_NO;
}





public void setR8_ACCT_NO(BigDecimal r8_ACCT_NO) {
	R8_ACCT_NO = r8_ACCT_NO;
}





public String getR8_ACCT_NAME() {
	return R8_ACCT_NAME;
}





public void setR8_ACCT_NAME(String r8_ACCT_NAME) {
	R8_ACCT_NAME = r8_ACCT_NAME;
}





public String getR8_SCHM_CODE() {
	return R8_SCHM_CODE;
}





public void setR8_SCHM_CODE(String r8_SCHM_CODE) {
	R8_SCHM_CODE = r8_SCHM_CODE;
}





public String getR8_SCHM_DESC() {
	return R8_SCHM_DESC;
}





public void setR8_SCHM_DESC(String r8_SCHM_DESC) {
	R8_SCHM_DESC = r8_SCHM_DESC;
}





public Date getR8_ACCT_OPN_DATE() {
	return R8_ACCT_OPN_DATE;
}





public void setR8_ACCT_OPN_DATE(Date r8_ACCT_OPN_DATE) {
	R8_ACCT_OPN_DATE = r8_ACCT_OPN_DATE;
}





public String getR8_CCY() {
	return R8_CCY;
}





public void setR8_CCY(String r8_CCY) {
	R8_CCY = r8_CCY;
}





public BigDecimal getR8_BAL_EQUI_TO_BWP() {
	return R8_BAL_EQUI_TO_BWP;
}





public void setR8_BAL_EQUI_TO_BWP(BigDecimal r8_BAL_EQUI_TO_BWP) {
	R8_BAL_EQUI_TO_BWP = r8_BAL_EQUI_TO_BWP;
}





public BigDecimal getR8_SANCTION_AMT_BWP() {
	return R8_SANCTION_AMT_BWP;
}





public void setR8_SANCTION_AMT_BWP(BigDecimal r8_SANCTION_AMT_BWP) {
	R8_SANCTION_AMT_BWP = r8_SANCTION_AMT_BWP;
}





public BigDecimal getR8_INT_RATE() {
	return R8_INT_RATE;
}





public void setR8_INT_RATE(BigDecimal r8_INT_RATE) {
	R8_INT_RATE = r8_INT_RATE;
}





public BigDecimal getR8_AMT_IN_INR() {
	return R8_AMT_IN_INR;
}





public void setR8_AMT_IN_INR(BigDecimal r8_AMT_IN_INR) {
	R8_AMT_IN_INR = r8_AMT_IN_INR;
}





public BigDecimal getR8_VALUE_1() {
	return R8_VALUE_1;
}





public void setR8_VALUE_1(BigDecimal r8_VALUE_1) {
	R8_VALUE_1 = r8_VALUE_1;
}





public BigDecimal getR8_VALUE_2() {
	return R8_VALUE_2;
}





public void setR8_VALUE_2(BigDecimal r8_VALUE_2) {
	R8_VALUE_2 = r8_VALUE_2;
}





public String getR9_CUST_ID() {
	return R9_CUST_ID;
}





public void setR9_CUST_ID(String r9_CUST_ID) {
	R9_CUST_ID = r9_CUST_ID;
}





public BigDecimal getR9_ACCT_NO() {
	return R9_ACCT_NO;
}





public void setR9_ACCT_NO(BigDecimal r9_ACCT_NO) {
	R9_ACCT_NO = r9_ACCT_NO;
}





public String getR9_ACCT_NAME() {
	return R9_ACCT_NAME;
}





public void setR9_ACCT_NAME(String r9_ACCT_NAME) {
	R9_ACCT_NAME = r9_ACCT_NAME;
}





public String getR9_SCHM_CODE() {
	return R9_SCHM_CODE;
}





public void setR9_SCHM_CODE(String r9_SCHM_CODE) {
	R9_SCHM_CODE = r9_SCHM_CODE;
}





public String getR9_SCHM_DESC() {
	return R9_SCHM_DESC;
}





public void setR9_SCHM_DESC(String r9_SCHM_DESC) {
	R9_SCHM_DESC = r9_SCHM_DESC;
}





public Date getR9_ACCT_OPN_DATE() {
	return R9_ACCT_OPN_DATE;
}





public void setR9_ACCT_OPN_DATE(Date r9_ACCT_OPN_DATE) {
	R9_ACCT_OPN_DATE = r9_ACCT_OPN_DATE;
}





public String getR9_CCY() {
	return R9_CCY;
}





public void setR9_CCY(String r9_CCY) {
	R9_CCY = r9_CCY;
}





public BigDecimal getR9_BAL_EQUI_TO_BWP() {
	return R9_BAL_EQUI_TO_BWP;
}





public void setR9_BAL_EQUI_TO_BWP(BigDecimal r9_BAL_EQUI_TO_BWP) {
	R9_BAL_EQUI_TO_BWP = r9_BAL_EQUI_TO_BWP;
}





public BigDecimal getR9_SANCTION_AMT_BWP() {
	return R9_SANCTION_AMT_BWP;
}





public void setR9_SANCTION_AMT_BWP(BigDecimal r9_SANCTION_AMT_BWP) {
	R9_SANCTION_AMT_BWP = r9_SANCTION_AMT_BWP;
}





public BigDecimal getR9_INT_RATE() {
	return R9_INT_RATE;
}





public void setR9_INT_RATE(BigDecimal r9_INT_RATE) {
	R9_INT_RATE = r9_INT_RATE;
}





public BigDecimal getR9_AMT_IN_INR() {
	return R9_AMT_IN_INR;
}





public void setR9_AMT_IN_INR(BigDecimal r9_AMT_IN_INR) {
	R9_AMT_IN_INR = r9_AMT_IN_INR;
}





public BigDecimal getR9_VALUE_1() {
	return R9_VALUE_1;
}





public void setR9_VALUE_1(BigDecimal r9_VALUE_1) {
	R9_VALUE_1 = r9_VALUE_1;
}





public BigDecimal getR9_VALUE_2() {
	return R9_VALUE_2;
}





public void setR9_VALUE_2(BigDecimal r9_VALUE_2) {
	R9_VALUE_2 = r9_VALUE_2;
}





public String getR10_CUST_ID() {
	return R10_CUST_ID;
}





public void setR10_CUST_ID(String r10_CUST_ID) {
	R10_CUST_ID = r10_CUST_ID;
}





public BigDecimal getR10_ACCT_NO() {
	return R10_ACCT_NO;
}





public void setR10_ACCT_NO(BigDecimal r10_ACCT_NO) {
	R10_ACCT_NO = r10_ACCT_NO;
}





public String getR10_ACCT_NAME() {
	return R10_ACCT_NAME;
}





public void setR10_ACCT_NAME(String r10_ACCT_NAME) {
	R10_ACCT_NAME = r10_ACCT_NAME;
}





public String getR10_SCHM_CODE() {
	return R10_SCHM_CODE;
}





public void setR10_SCHM_CODE(String r10_SCHM_CODE) {
	R10_SCHM_CODE = r10_SCHM_CODE;
}





public String getR10_SCHM_DESC() {
	return R10_SCHM_DESC;
}





public void setR10_SCHM_DESC(String r10_SCHM_DESC) {
	R10_SCHM_DESC = r10_SCHM_DESC;
}





public Date getR10_ACCT_OPN_DATE() {
	return R10_ACCT_OPN_DATE;
}





public void setR10_ACCT_OPN_DATE(Date r10_ACCT_OPN_DATE) {
	R10_ACCT_OPN_DATE = r10_ACCT_OPN_DATE;
}





public String getR10_CCY() {
	return R10_CCY;
}





public void setR10_CCY(String r10_CCY) {
	R10_CCY = r10_CCY;
}





public BigDecimal getR10_BAL_EQUI_TO_BWP() {
	return R10_BAL_EQUI_TO_BWP;
}





public void setR10_BAL_EQUI_TO_BWP(BigDecimal r10_BAL_EQUI_TO_BWP) {
	R10_BAL_EQUI_TO_BWP = r10_BAL_EQUI_TO_BWP;
}





public BigDecimal getR10_SANCTION_AMT_BWP() {
	return R10_SANCTION_AMT_BWP;
}





public void setR10_SANCTION_AMT_BWP(BigDecimal r10_SANCTION_AMT_BWP) {
	R10_SANCTION_AMT_BWP = r10_SANCTION_AMT_BWP;
}





public BigDecimal getR10_INT_RATE() {
	return R10_INT_RATE;
}





public void setR10_INT_RATE(BigDecimal r10_INT_RATE) {
	R10_INT_RATE = r10_INT_RATE;
}





public BigDecimal getR10_AMT_IN_INR() {
	return R10_AMT_IN_INR;
}





public void setR10_AMT_IN_INR(BigDecimal r10_AMT_IN_INR) {
	R10_AMT_IN_INR = r10_AMT_IN_INR;
}





public BigDecimal getR10_VALUE_1() {
	return R10_VALUE_1;
}





public void setR10_VALUE_1(BigDecimal r10_VALUE_1) {
	R10_VALUE_1 = r10_VALUE_1;
}





public BigDecimal getR10_VALUE_2() {
	return R10_VALUE_2;
}





public void setR10_VALUE_2(BigDecimal r10_VALUE_2) {
	R10_VALUE_2 = r10_VALUE_2;
}





public String getR11_CUST_ID() {
	return R11_CUST_ID;
}





public void setR11_CUST_ID(String r11_CUST_ID) {
	R11_CUST_ID = r11_CUST_ID;
}





public BigDecimal getR11_ACCT_NO() {
	return R11_ACCT_NO;
}





public void setR11_ACCT_NO(BigDecimal r11_ACCT_NO) {
	R11_ACCT_NO = r11_ACCT_NO;
}





public String getR11_ACCT_NAME() {
	return R11_ACCT_NAME;
}





public void setR11_ACCT_NAME(String r11_ACCT_NAME) {
	R11_ACCT_NAME = r11_ACCT_NAME;
}





public String getR11_SCHM_CODE() {
	return R11_SCHM_CODE;
}





public void setR11_SCHM_CODE(String r11_SCHM_CODE) {
	R11_SCHM_CODE = r11_SCHM_CODE;
}





public String getR11_SCHM_DESC() {
	return R11_SCHM_DESC;
}





public void setR11_SCHM_DESC(String r11_SCHM_DESC) {
	R11_SCHM_DESC = r11_SCHM_DESC;
}





public Date getR11_ACCT_OPN_DATE() {
	return R11_ACCT_OPN_DATE;
}





public void setR11_ACCT_OPN_DATE(Date r11_ACCT_OPN_DATE) {
	R11_ACCT_OPN_DATE = r11_ACCT_OPN_DATE;
}





public String getR11_CCY() {
	return R11_CCY;
}





public void setR11_CCY(String r11_CCY) {
	R11_CCY = r11_CCY;
}





public BigDecimal getR11_BAL_EQUI_TO_BWP() {
	return R11_BAL_EQUI_TO_BWP;
}





public void setR11_BAL_EQUI_TO_BWP(BigDecimal r11_BAL_EQUI_TO_BWP) {
	R11_BAL_EQUI_TO_BWP = r11_BAL_EQUI_TO_BWP;
}





public BigDecimal getR11_SANCTION_AMT_BWP() {
	return R11_SANCTION_AMT_BWP;
}





public void setR11_SANCTION_AMT_BWP(BigDecimal r11_SANCTION_AMT_BWP) {
	R11_SANCTION_AMT_BWP = r11_SANCTION_AMT_BWP;
}





public BigDecimal getR11_INT_RATE() {
	return R11_INT_RATE;
}





public void setR11_INT_RATE(BigDecimal r11_INT_RATE) {
	R11_INT_RATE = r11_INT_RATE;
}





public BigDecimal getR11_AMT_IN_INR() {
	return R11_AMT_IN_INR;
}





public void setR11_AMT_IN_INR(BigDecimal r11_AMT_IN_INR) {
	R11_AMT_IN_INR = r11_AMT_IN_INR;
}





public BigDecimal getR11_VALUE_1() {
	return R11_VALUE_1;
}





public void setR11_VALUE_1(BigDecimal r11_VALUE_1) {
	R11_VALUE_1 = r11_VALUE_1;
}





public BigDecimal getR11_VALUE_2() {
	return R11_VALUE_2;
}





public void setR11_VALUE_2(BigDecimal r11_VALUE_2) {
	R11_VALUE_2 = r11_VALUE_2;
}





public String getR12_CUST_ID() {
	return R12_CUST_ID;
}





public void setR12_CUST_ID(String r12_CUST_ID) {
	R12_CUST_ID = r12_CUST_ID;
}





public BigDecimal getR12_ACCT_NO() {
	return R12_ACCT_NO;
}





public void setR12_ACCT_NO(BigDecimal r12_ACCT_NO) {
	R12_ACCT_NO = r12_ACCT_NO;
}





public String getR12_ACCT_NAME() {
	return R12_ACCT_NAME;
}





public void setR12_ACCT_NAME(String r12_ACCT_NAME) {
	R12_ACCT_NAME = r12_ACCT_NAME;
}





public String getR12_SCHM_CODE() {
	return R12_SCHM_CODE;
}





public void setR12_SCHM_CODE(String r12_SCHM_CODE) {
	R12_SCHM_CODE = r12_SCHM_CODE;
}





public String getR12_SCHM_DESC() {
	return R12_SCHM_DESC;
}





public void setR12_SCHM_DESC(String r12_SCHM_DESC) {
	R12_SCHM_DESC = r12_SCHM_DESC;
}





public Date getR12_ACCT_OPN_DATE() {
	return R12_ACCT_OPN_DATE;
}





public void setR12_ACCT_OPN_DATE(Date r12_ACCT_OPN_DATE) {
	R12_ACCT_OPN_DATE = r12_ACCT_OPN_DATE;
}





public String getR12_CCY() {
	return R12_CCY;
}





public void setR12_CCY(String r12_CCY) {
	R12_CCY = r12_CCY;
}





public BigDecimal getR12_BAL_EQUI_TO_BWP() {
	return R12_BAL_EQUI_TO_BWP;
}





public void setR12_BAL_EQUI_TO_BWP(BigDecimal r12_BAL_EQUI_TO_BWP) {
	R12_BAL_EQUI_TO_BWP = r12_BAL_EQUI_TO_BWP;
}





public BigDecimal getR12_SANCTION_AMT_BWP() {
	return R12_SANCTION_AMT_BWP;
}





public void setR12_SANCTION_AMT_BWP(BigDecimal r12_SANCTION_AMT_BWP) {
	R12_SANCTION_AMT_BWP = r12_SANCTION_AMT_BWP;
}





public BigDecimal getR12_INT_RATE() {
	return R12_INT_RATE;
}





public void setR12_INT_RATE(BigDecimal r12_INT_RATE) {
	R12_INT_RATE = r12_INT_RATE;
}





public BigDecimal getR12_AMT_IN_INR() {
	return R12_AMT_IN_INR;
}





public void setR12_AMT_IN_INR(BigDecimal r12_AMT_IN_INR) {
	R12_AMT_IN_INR = r12_AMT_IN_INR;
}





public BigDecimal getR12_VALUE_1() {
	return R12_VALUE_1;
}





public void setR12_VALUE_1(BigDecimal r12_VALUE_1) {
	R12_VALUE_1 = r12_VALUE_1;
}





public BigDecimal getR12_VALUE_2() {
	return R12_VALUE_2;
}





public void setR12_VALUE_2(BigDecimal r12_VALUE_2) {
	R12_VALUE_2 = r12_VALUE_2;
}





public BigDecimal getR3_VAL_MULTIPLY_AMT_IN_INR() {
	return R3_VAL_MULTIPLY_AMT_IN_INR;
}





public void setR3_VAL_MULTIPLY_AMT_IN_INR(BigDecimal r3_VAL_MULTIPLY_AMT_IN_INR) {
	R3_VAL_MULTIPLY_AMT_IN_INR = r3_VAL_MULTIPLY_AMT_IN_INR;
}





public BigDecimal getR3_VAL_DIVIDE_AMT_IN_INR() {
	return R3_VAL_DIVIDE_AMT_IN_INR;
}





public void setR3_VAL_DIVIDE_AMT_IN_INR(BigDecimal r3_VAL_DIVIDE_AMT_IN_INR) {
	R3_VAL_DIVIDE_AMT_IN_INR = r3_VAL_DIVIDE_AMT_IN_INR;
}





public BigDecimal getR14_AMT_IN_INR() {
	return R14_AMT_IN_INR;
}





public void setR14_AMT_IN_INR(BigDecimal r14_AMT_IN_INR) {
	R14_AMT_IN_INR = r14_AMT_IN_INR;
}





public BigDecimal getR14_VALUE_2() {
	return R14_VALUE_2;
}





public void setR14_VALUE_2(BigDecimal r14_VALUE_2) {
	R14_VALUE_2 = r14_VALUE_2;
}





public BORR_UFCE_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}





}
