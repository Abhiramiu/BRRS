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
@Table(name = "BRRS_M_FAS_ARCHIVALTABLE_SUMMARY")

public class M_FAS_Archival_Summary_Entity {
private String	r10_fix_ass;
private BigDecimal	r10_cost;
private BigDecimal	r10_add;
private BigDecimal	r10_disposals;
private BigDecimal	r10_depreciation;
private BigDecimal	r10_net_book_value;

private String	r11_fix_ass;
private BigDecimal	r11_cost;
private BigDecimal	r11_add;
private BigDecimal	r11_disposals;
private BigDecimal	r11_depreciation;
private BigDecimal	r11_net_book_value;

// private String	r12_fix_ass;
// private BigDecimal	r12_cost;
// private BigDecimal	r12_add;
// private BigDecimal	r12_disposals;
// private BigDecimal	r12_depreciation;
// private BigDecimal	r12_net_book_value;

private String	r13_fix_ass;
private BigDecimal	r13_cost;
private BigDecimal	r13_add;
private BigDecimal	r13_disposals;
private BigDecimal	r13_depreciation;
private BigDecimal	r13_net_book_value;

private String	r14_fix_ass;
private BigDecimal	r14_cost;
private BigDecimal	r14_add;
private BigDecimal	r14_disposals;
private BigDecimal	r14_depreciation;
private BigDecimal	r14_net_book_value;

private String	r15_fix_ass;
private BigDecimal	r15_cost;
private BigDecimal	r15_add;
private BigDecimal	r15_disposals;
private BigDecimal	r15_depreciation;
private BigDecimal	r15_net_book_value;

private String	r16_fix_ass;
private BigDecimal	r16_cost;
private BigDecimal	r16_add;
private BigDecimal	r16_disposals;
private BigDecimal	r16_depreciation;
private BigDecimal	r16_net_book_value;

private String	r17_fix_ass;
private BigDecimal	r17_cost;
private BigDecimal	r17_add;
private BigDecimal	r17_disposals;
private BigDecimal	r17_depreciation;
private BigDecimal	r17_net_book_value;

// private String	r23_intangible_ass;
// private BigDecimal	r23_cost_rev;
// private BigDecimal	r23_useful_life;
// private BigDecimal	r23_res_value;
// private BigDecimal	r23_month_amort;
// private BigDecimal	r23_acc_amort_amt;
// private BigDecimal	r23_close_bal;

// private String	r24_intangible_ass;
// private BigDecimal	r24_cost_rev;
// private BigDecimal	r24_useful_life;
// private BigDecimal	r24_res_value;
// private BigDecimal	r24_month_amort;
// private BigDecimal	r24_acc_amort_amt;
// private BigDecimal	r24_close_bal;

// private String	r25_intangible_ass;
// private BigDecimal	r25_cost_rev;
// private BigDecimal	r25_useful_life;
// private BigDecimal	r25_res_value;
// private BigDecimal	r25_month_amort;
// private BigDecimal	r25_acc_amort_amt;
// private BigDecimal	r25_close_bal;

// private String	r26_intangible_ass;
// private BigDecimal	r26_cost_rev;
// private BigDecimal	r26_useful_life;
// private BigDecimal	r26_res_value;
// private BigDecimal	r26_month_amort;
// private BigDecimal	r26_acc_amort_amt;
// private BigDecimal	r26_close_bal;

// private String	r27_intangible_ass;
// private BigDecimal	r27_cost_rev;
// private BigDecimal	r27_useful_life;
// private BigDecimal	r27_res_value;
// private BigDecimal	r27_month_amort;
// private BigDecimal	r27_acc_amort_amt;
// private BigDecimal	r27_close_bal;

// private String	r28_intangible_ass;
// private BigDecimal	r28_cost_rev;
// private BigDecimal	r28_useful_life;
// private BigDecimal	r28_res_value;
// private BigDecimal	r28_month_amort;
// private BigDecimal	r28_acc_amort_amt;
// private BigDecimal	r28_close_bal;
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

public String getR10_fix_ass() {
    return r10_fix_ass;
}
public void setR10_fix_ass(String r10_fix_ass) {
    this.r10_fix_ass = r10_fix_ass;
}
public BigDecimal getR10_cost() {
    return r10_cost;
}
public void setR10_cost(BigDecimal r10_cost) {
    this.r10_cost = r10_cost;
}
public BigDecimal getR10_add() {
    return r10_add;
}
public void setR10_add(BigDecimal r10_add) {
    this.r10_add = r10_add;
}
public BigDecimal getR10_disposals() {
    return r10_disposals;
}
public void setR10_disposals(BigDecimal r10_disposals) {
    this.r10_disposals = r10_disposals;
}
public BigDecimal getR10_depreciation() {
    return r10_depreciation;
}
public void setR10_depreciation(BigDecimal r10_depreciation) {
    this.r10_depreciation = r10_depreciation;
}
public BigDecimal getR10_net_book_value() {
    return r10_net_book_value;
}
public void setR10_net_book_value(BigDecimal r10_net_book_value) {
    this.r10_net_book_value = r10_net_book_value;
}
public String getR11_fix_ass() {
    return r11_fix_ass;
}
public void setR11_fix_ass(String r11_fix_ass) {
    this.r11_fix_ass = r11_fix_ass;
}
public BigDecimal getR11_cost() {
    return r11_cost;
}
public void setR11_cost(BigDecimal r11_cost) {
    this.r11_cost = r11_cost;
}
public BigDecimal getR11_add() {
    return r11_add;
}
public void setR11_add(BigDecimal r11_add) {
    this.r11_add = r11_add;
}
public BigDecimal getR11_disposals() {
    return r11_disposals;
}
public void setR11_disposals(BigDecimal r11_disposals) {
    this.r11_disposals = r11_disposals;
}
public BigDecimal getR11_depreciation() {
    return r11_depreciation;
}
public void setR11_depreciation(BigDecimal r11_depreciation) {
    this.r11_depreciation = r11_depreciation;
}
public BigDecimal getR11_net_book_value() {
    return r11_net_book_value;
}
public void setR11_net_book_value(BigDecimal r11_net_book_value) {
    this.r11_net_book_value = r11_net_book_value;
}
// public String getR12_fix_ass() {
//     return r12_fix_ass;
// }
// public void setR12_fix_ass(String r12_fix_ass) {
//     this.r12_fix_ass = r12_fix_ass;
// }
// public BigDecimal getR12_cost() {
//     return r12_cost;
// }
// public void setR12_cost(BigDecimal r12_cost) {
//     this.r12_cost = r12_cost;
// }
// public BigDecimal getR12_add() {
//     return r12_add;
// }
// public void setR12_add(BigDecimal r12_add) {
//     this.r12_add = r12_add;
// }
// public BigDecimal getR12_disposals() {
//     return r12_disposals;
// }
// public void setR12_disposals(BigDecimal r12_disposals) {
//     this.r12_disposals = r12_disposals;
// }
// public BigDecimal getR12_depreciation() {
//     return r12_depreciation;
// }
// public void setR12_depreciation(BigDecimal r12_depreciation) {
//     this.r12_depreciation = r12_depreciation;
// }
// public BigDecimal getR12_net_book_value() {
//     return r12_net_book_value;
// }
// public void setR12_net_book_value(BigDecimal r12_net_book_value) {
//     this.r12_net_book_value = r12_net_book_value;
// }
public String getR13_fix_ass() {
    return r13_fix_ass;
}
public void setR13_fix_ass(String r13_fix_ass) {
    this.r13_fix_ass = r13_fix_ass;
}
public BigDecimal getR13_cost() {
    return r13_cost;
}
public void setR13_cost(BigDecimal r13_cost) {
    this.r13_cost = r13_cost;
}
public BigDecimal getR13_add() {
    return r13_add;
}
public void setR13_add(BigDecimal r13_add) {
    this.r13_add = r13_add;
}
public BigDecimal getR13_disposals() {
    return r13_disposals;
}
public void setR13_disposals(BigDecimal r13_disposals) {
    this.r13_disposals = r13_disposals;
}
public BigDecimal getR13_depreciation() {
    return r13_depreciation;
}
public void setR13_depreciation(BigDecimal r13_depreciation) {
    this.r13_depreciation = r13_depreciation;
}
public BigDecimal getR13_net_book_value() {
    return r13_net_book_value;
}
public void setR13_net_book_value(BigDecimal r13_net_book_value) {
    this.r13_net_book_value = r13_net_book_value;
}
public String getR14_fix_ass() {
    return r14_fix_ass;
}
public void setR14_fix_ass(String r14_fix_ass) {
    this.r14_fix_ass = r14_fix_ass;
}
public BigDecimal getR14_cost() {
    return r14_cost;
}
public void setR14_cost(BigDecimal r14_cost) {
    this.r14_cost = r14_cost;
}
public BigDecimal getR14_add() {
    return r14_add;
}
public void setR14_add(BigDecimal r14_add) {
    this.r14_add = r14_add;
}
public BigDecimal getR14_disposals() {
    return r14_disposals;
}
public void setR14_disposals(BigDecimal r14_disposals) {
    this.r14_disposals = r14_disposals;
}
public BigDecimal getR14_depreciation() {
    return r14_depreciation;
}
public void setR14_depreciation(BigDecimal r14_depreciation) {
    this.r14_depreciation = r14_depreciation;
}
public BigDecimal getR14_net_book_value() {
    return r14_net_book_value;
}
public void setR14_net_book_value(BigDecimal r14_net_book_value) {
    this.r14_net_book_value = r14_net_book_value;
}
public String getR15_fix_ass() {
    return r15_fix_ass;
}
public void setR15_fix_ass(String r15_fix_ass) {
    this.r15_fix_ass = r15_fix_ass;
}
public BigDecimal getR15_cost() {
    return r15_cost;
}
public void setR15_cost(BigDecimal r15_cost) {
    this.r15_cost = r15_cost;
}
public BigDecimal getR15_add() {
    return r15_add;
}
public void setR15_add(BigDecimal r15_add) {
    this.r15_add = r15_add;
}
public BigDecimal getR15_disposals() {
    return r15_disposals;
}
public void setR15_disposals(BigDecimal r15_disposals) {
    this.r15_disposals = r15_disposals;
}
public BigDecimal getR15_depreciation() {
    return r15_depreciation;
}
public void setR15_depreciation(BigDecimal r15_depreciation) {
    this.r15_depreciation = r15_depreciation;
}
public BigDecimal getR15_net_book_value() {
    return r15_net_book_value;
}
public void setR15_net_book_value(BigDecimal r15_net_book_value) {
    this.r15_net_book_value = r15_net_book_value;
}
public String getR16_fix_ass() {
    return r16_fix_ass;
}
public void setR16_fix_ass(String r16_fix_ass) {
    this.r16_fix_ass = r16_fix_ass;
}
public BigDecimal getR16_cost() {
    return r16_cost;
}
public void setR16_cost(BigDecimal r16_cost) {
    this.r16_cost = r16_cost;
}
public BigDecimal getR16_add() {
    return r16_add;
}
public void setR16_add(BigDecimal r16_add) {
    this.r16_add = r16_add;
}
public BigDecimal getR16_disposals() {
    return r16_disposals;
}
public void setR16_disposals(BigDecimal r16_disposals) {
    this.r16_disposals = r16_disposals;
}
public BigDecimal getR16_depreciation() {
    return r16_depreciation;
}
public void setR16_depreciation(BigDecimal r16_depreciation) {
    this.r16_depreciation = r16_depreciation;
}
public BigDecimal getR16_net_book_value() {
    return r16_net_book_value;
}
public void setR16_net_book_value(BigDecimal r16_net_book_value) {
    this.r16_net_book_value = r16_net_book_value;
}
public String getR17_fix_ass() {
    return r17_fix_ass;
}
public void setR17_fix_ass(String r17_fix_ass) {
    this.r17_fix_ass = r17_fix_ass;
}
public BigDecimal getR17_cost() {
    return r17_cost;
}
public void setR17_cost(BigDecimal r17_cost) {
    this.r17_cost = r17_cost;
}
public BigDecimal getR17_add() {
    return r17_add;
}
public void setR17_add(BigDecimal r17_add) {
    this.r17_add = r17_add;
}
public BigDecimal getR17_disposals() {
    return r17_disposals;
}
public void setR17_disposals(BigDecimal r17_disposals) {
    this.r17_disposals = r17_disposals;
}
public BigDecimal getR17_depreciation() {
    return r17_depreciation;
}
public void setR17_depreciation(BigDecimal r17_depreciation) {
    this.r17_depreciation = r17_depreciation;
}
public BigDecimal getR17_net_book_value() {
    return r17_net_book_value;
}
public void setR17_net_book_value(BigDecimal r17_net_book_value) {
    this.r17_net_book_value = r17_net_book_value;
}
// public String getR23_intangible_ass() {
//     return r23_intangible_ass;
// }
// public void setR23_intangible_ass(String r23_intangible_ass) {
//     this.r23_intangible_ass = r23_intangible_ass;
// }
// public BigDecimal getR23_cost_rev() {
//     return r23_cost_rev;
// }
// public void setR23_cost_rev(BigDecimal r23_cost_rev) {
//     this.r23_cost_rev = r23_cost_rev;
// }
// public BigDecimal getR23_useful_life() {
//     return r23_useful_life;
// }
// public void setR23_useful_life(BigDecimal r23_useful_life) {
//     this.r23_useful_life = r23_useful_life;
// }
// public BigDecimal getR23_res_value() {
//     return r23_res_value;
// }
// public void setR23_res_value(BigDecimal r23_res_value) {
//     this.r23_res_value = r23_res_value;
// }
// public BigDecimal getR23_month_amort() {
//     return r23_month_amort;
// }
// public void setR23_month_amort(BigDecimal r23_month_amort) {
//     this.r23_month_amort = r23_month_amort;
// }
// public BigDecimal getR23_acc_amort_amt() {
//     return r23_acc_amort_amt;
// }
// public void setR23_acc_amort_amt(BigDecimal r23_acc_amort_amt) {
//     this.r23_acc_amort_amt = r23_acc_amort_amt;
// }
// public BigDecimal getR23_close_bal() {
//     return r23_close_bal;
// }
// public void setR23_close_bal(BigDecimal r23_close_bal) {
//     this.r23_close_bal = r23_close_bal;
// }
// public String getR24_intangible_ass() {
//     return r24_intangible_ass;
// }
// public void setR24_intangible_ass(String r24_intangible_ass) {
//     this.r24_intangible_ass = r24_intangible_ass;
// }
// public BigDecimal getR24_cost_rev() {
//     return r24_cost_rev;
// }
// public void setR24_cost_rev(BigDecimal r24_cost_rev) {
//     this.r24_cost_rev = r24_cost_rev;
// }
// public BigDecimal getR24_useful_life() {
//     return r24_useful_life;
// }
// public void setR24_useful_life(BigDecimal r24_useful_life) {
//     this.r24_useful_life = r24_useful_life;
// }
// public BigDecimal getR24_res_value() {
//     return r24_res_value;
// }
// public void setR24_res_value(BigDecimal r24_res_value) {
//     this.r24_res_value = r24_res_value;
// }
// public BigDecimal getR24_month_amort() {
//     return r24_month_amort;
// }
// public void setR24_month_amort(BigDecimal r24_month_amort) {
//     this.r24_month_amort = r24_month_amort;
// }
// public BigDecimal getR24_acc_amort_amt() {
//     return r24_acc_amort_amt;
// }
// public void setR24_acc_amort_amt(BigDecimal r24_acc_amort_amt) {
//     this.r24_acc_amort_amt = r24_acc_amort_amt;
// }
// public BigDecimal getR24_close_bal() {
//     return r24_close_bal;
// }
// public void setR24_close_bal(BigDecimal r24_close_bal) {
//     this.r24_close_bal = r24_close_bal;
// }
// public String getR25_intangible_ass() {
//     return r25_intangible_ass;
// }
// public void setR25_intangible_ass(String r25_intangible_ass) {
//     this.r25_intangible_ass = r25_intangible_ass;
// }
// public BigDecimal getR25_cost_rev() {
//     return r25_cost_rev;
// }
// public void setR25_cost_rev(BigDecimal r25_cost_rev) {
//     this.r25_cost_rev = r25_cost_rev;
// }
// public BigDecimal getR25_useful_life() {
//     return r25_useful_life;
// }
// public void setR25_useful_life(BigDecimal r25_useful_life) {
//     this.r25_useful_life = r25_useful_life;
// }
// public BigDecimal getR25_res_value() {
//     return r25_res_value;
// }
// public void setR25_res_value(BigDecimal r25_res_value) {
//     this.r25_res_value = r25_res_value;
// }
// public BigDecimal getR25_month_amort() {
//     return r25_month_amort;
// }
// public void setR25_month_amort(BigDecimal r25_month_amort) {
//     this.r25_month_amort = r25_month_amort;
// }
// public BigDecimal getR25_acc_amort_amt() {
//     return r25_acc_amort_amt;
// }
// public void setR25_acc_amort_amt(BigDecimal r25_acc_amort_amt) {
//     this.r25_acc_amort_amt = r25_acc_amort_amt;
// }
// public BigDecimal getR25_close_bal() {
//     return r25_close_bal;
// }
// public void setR25_close_bal(BigDecimal r25_close_bal) {
//     this.r25_close_bal = r25_close_bal;
// }
// public String getR26_intangible_ass() {
//     return r26_intangible_ass;
// }
// public void setR26_intangible_ass(String r26_intangible_ass) {
//     this.r26_intangible_ass = r26_intangible_ass;
// }
// public BigDecimal getR26_cost_rev() {
//     return r26_cost_rev;
// }
// public void setR26_cost_rev(BigDecimal r26_cost_rev) {
//     this.r26_cost_rev = r26_cost_rev;
// }
// public BigDecimal getR26_useful_life() {
//     return r26_useful_life;
// }
// public void setR26_useful_life(BigDecimal r26_useful_life) {
//     this.r26_useful_life = r26_useful_life;
// }
// public BigDecimal getR26_res_value() {
//     return r26_res_value;
// }
// public void setR26_res_value(BigDecimal r26_res_value) {
//     this.r26_res_value = r26_res_value;
// }
// public BigDecimal getR26_month_amort() {
//     return r26_month_amort;
// }
// public void setR26_month_amort(BigDecimal r26_month_amort) {
//     this.r26_month_amort = r26_month_amort;
// }
// public BigDecimal getR26_acc_amort_amt() {
//     return r26_acc_amort_amt;
// }
// public void setR26_acc_amort_amt(BigDecimal r26_acc_amort_amt) {
//     this.r26_acc_amort_amt = r26_acc_amort_amt;
// }
// public BigDecimal getR26_close_bal() {
//     return r26_close_bal;
// }
// public void setR26_close_bal(BigDecimal r26_close_bal) {
//     this.r26_close_bal = r26_close_bal;
// }
// public String getR27_intangible_ass() {
//     return r27_intangible_ass;
// }
// public void setR27_intangible_ass(String r27_intangible_ass) {
//     this.r27_intangible_ass = r27_intangible_ass;
// }
// public BigDecimal getR27_cost_rev() {
//     return r27_cost_rev;
// }
// public void setR27_cost_rev(BigDecimal r27_cost_rev) {
//     this.r27_cost_rev = r27_cost_rev;
// }
// public BigDecimal getR27_useful_life() {
//     return r27_useful_life;
// }
// public void setR27_useful_life(BigDecimal r27_useful_life) {
//     this.r27_useful_life = r27_useful_life;
// }
// public BigDecimal getR27_res_value() {
//     return r27_res_value;
// }
// public void setR27_res_value(BigDecimal r27_res_value) {
//     this.r27_res_value = r27_res_value;
// }
// public BigDecimal getR27_month_amort() {
//     return r27_month_amort;
// }
// public void setR27_month_amort(BigDecimal r27_month_amort) {
//     this.r27_month_amort = r27_month_amort;
// }
// public BigDecimal getR27_acc_amort_amt() {
//     return r27_acc_amort_amt;
// }
// public void setR27_acc_amort_amt(BigDecimal r27_acc_amort_amt) {
//     this.r27_acc_amort_amt = r27_acc_amort_amt;
// }
// public BigDecimal getR27_close_bal() {
//     return r27_close_bal;
// }
// public void setR27_close_bal(BigDecimal r27_close_bal) {
//     this.r27_close_bal = r27_close_bal;
// }
// public String getR28_intangible_ass() {
//     return r28_intangible_ass;
// }
// public void setR28_intangible_ass(String r28_intangible_ass) {
//     this.r28_intangible_ass = r28_intangible_ass;
// }
// public BigDecimal getR28_cost_rev() {
//     return r28_cost_rev;
// }
// public void setR28_cost_rev(BigDecimal r28_cost_rev) {
//     this.r28_cost_rev = r28_cost_rev;
// }
// public BigDecimal getR28_useful_life() {
//     return r28_useful_life;
// }
// public void setR28_useful_life(BigDecimal r28_useful_life) {
//     this.r28_useful_life = r28_useful_life;
// }
// public BigDecimal getR28_res_value() {
//     return r28_res_value;
// }
// public void setR28_res_value(BigDecimal r28_res_value) {
//     this.r28_res_value = r28_res_value;
// }
// public BigDecimal getR28_month_amort() {
//     return r28_month_amort;
// }
// public void setR28_month_amort(BigDecimal r28_month_amort) {
//     this.r28_month_amort = r28_month_amort;
// }
// public BigDecimal getR28_acc_amort_amt() {
//     return r28_acc_amort_amt;
// }
// public void setR28_acc_amort_amt(BigDecimal r28_acc_amort_amt) {
//     this.r28_acc_amort_amt = r28_acc_amort_amt;
// }
// public BigDecimal getR28_close_bal() {
//     return r28_close_bal;
// }
// public void setR28_close_bal(BigDecimal r28_close_bal) {
//     this.r28_close_bal = r28_close_bal;
// }
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
public M_FAS_Archival_Summary_Entity() {
    super();
}

}



