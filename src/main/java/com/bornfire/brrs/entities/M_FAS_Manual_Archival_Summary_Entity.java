package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_FAS_MANUAL_ARCHIVALTABLE_SUMMARY")
public class M_FAS_Manual_Archival_Summary_Entity {

    /* ------------------- R12 --------------------- */
    @Column(name = "R12_FIX_ASS")
    private String r12_fix_ass;

    @Column(name = "R12_COST")
    private BigDecimal r12_cost;

    @Column(name = "R12_ADD")
    private BigDecimal r12_add;

    @Column(name = "R12_DISPOSALS")
    private BigDecimal r12_disposals;

    @Column(name = "R12_DEPRECIATION")
    private BigDecimal r12_depreciation;

    @Column(name = "R12_NET_BOOK_VALUE")
    private BigDecimal r12_net_book_value;

    /* ------------------- R23 --------------------- */
    @Column(name = "R23_INTANGIBLE_ASS")
    private String r23_intangible_ass;

    @Column(name = "R23_COST_REV")
    private BigDecimal r23_cost_rev;

    @Column(name = "R23_USEFUL_LIFE")
    private BigDecimal r23_useful_life;

    @Column(name = "R23_RES_VALUE")
    private BigDecimal r23_res_value;

    @Column(name = "R23_MONTH_AMORT")
    private BigDecimal r23_month_amort;

    @Column(name = "R23_ACC_AMORT_AMT")
    private BigDecimal r23_acc_amort_amt;

    @Column(name = "R23_CLOSE_BAL")
    private BigDecimal r23_close_bal;

    /* ------------------- R24 --------------------- */
    @Column(name = "R24_INTANGIBLE_ASS")
    private String r24_intangible_ass;

    @Column(name = "R24_COST_REV")
    private BigDecimal r24_cost_rev;

    @Column(name = "R24_USEFUL_LIFE")
    private BigDecimal r24_useful_life;

    @Column(name = "R24_RES_VALUE")
    private BigDecimal r24_res_value;

    @Column(name = "R24_MONTH_AMORT")
    private BigDecimal r24_month_amort;

    @Column(name = "R24_ACC_AMORT_AMT")
    private BigDecimal r24_acc_amort_amt;

    @Column(name = "R24_CLOSE_BAL")
    private BigDecimal r24_close_bal;

    /* ------------------- R25 --------------------- */
    @Column(name = "R25_INTANGIBLE_ASS")
    private String r25_intangible_ass;

    @Column(name = "R25_COST_REV")
    private BigDecimal r25_cost_rev;

    @Column(name = "R25_USEFUL_LIFE")
    private BigDecimal r25_useful_life;

    @Column(name = "R25_RES_VALUE")
    private BigDecimal r25_res_value;

    @Column(name = "R25_MONTH_AMORT")
    private BigDecimal r25_month_amort;

    @Column(name = "R25_ACC_AMORT_AMT")
    private BigDecimal r25_acc_amort_amt;

    @Column(name = "R25_CLOSE_BAL")
    private BigDecimal r25_close_bal;

    /* ------------------- R26 --------------------- */
    @Column(name = "R26_INTANGIBLE_ASS")
    private String r26_intangible_ass;

    @Column(name = "R26_COST_REV")
    private BigDecimal r26_cost_rev;

    @Column(name = "R26_USEFUL_LIFE")
    private BigDecimal r26_useful_life;

    @Column(name = "R26_RES_VALUE")
    private BigDecimal r26_res_value;

    @Column(name = "R26_MONTH_AMORT")
    private BigDecimal r26_month_amort;

    @Column(name = "R26_ACC_AMORT_AMT")
    private BigDecimal r26_acc_amort_amt;

    @Column(name = "R26_CLOSE_BAL")
    private BigDecimal r26_close_bal;

    /* ------------------- R27 --------------------- */
    @Column(name = "R27_INTANGIBLE_ASS")
    private String r27_intangible_ass;

    @Column(name = "R27_COST_REV")
    private BigDecimal r27_cost_rev;

    @Column(name = "R27_USEFUL_LIFE")
    private BigDecimal r27_useful_life;

    @Column(name = "R27_RES_VALUE")
    private BigDecimal r27_res_value;

    @Column(name = "R27_MONTH_AMORT")
    private BigDecimal r27_month_amort;

    @Column(name = "R27_ACC_AMORT_AMT")
    private BigDecimal r27_acc_amort_amt;

    @Column(name = "R27_CLOSE_BAL")
    private BigDecimal r27_close_bal;

    /* ------------------- R28 --------------------- */
    @Column(name = "R28_INTANGIBLE_ASS")
    private String r28_intangible_ass;

    @Column(name = "R28_COST_REV")
    private BigDecimal r28_cost_rev;

    @Column(name = "R28_USEFUL_LIFE")
    private BigDecimal r28_useful_life;

    @Column(name = "R28_RES_VALUE")
    private BigDecimal r28_res_value;

    @Column(name = "R28_MONTH_AMORT")
    private BigDecimal r28_month_amort;

    @Column(name = "R28_ACC_AMORT_AMT")
    private BigDecimal r28_acc_amort_amt;

    @Column(name = "R28_CLOSE_BAL")
    private BigDecimal r28_close_bal;

    /* ------------------- REPORT INFO --------------------- */
    @Id
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "REPORT_DATE")
    private Date report_date;

    @Column(name = "REPORT_VERSION")
    private String report_version;

    @Column(name = "REPORT_FREQUENCY")
    private String report_frequency;

    @Column(name = "REPORT_CODE")
    private String report_code;

    @Column(name = "REPORT_DESC")
    private String report_desc;

    @Column(name = "ENTITY_FLG")
    private String entity_flg;

    @Column(name = "MODIFY_FLG")
    private String modify_flg;

    @Column(name = "DEL_FLG")
    private String del_flg;

    public M_FAS_Manual_Archival_Summary_Entity() {
        super();
    }

    public String getR12_fix_ass() {
        return r12_fix_ass;
    }

    public void setR12_fix_ass(String r12_fix_ass) {
        this.r12_fix_ass = r12_fix_ass;
    }

    public BigDecimal getR12_cost() {
        return r12_cost;
    }

    public void setR12_cost(BigDecimal r12_cost) {
        this.r12_cost = r12_cost;
    }

    public BigDecimal getR12_add() {
        return r12_add;
    }

    public void setR12_add(BigDecimal r12_add) {
        this.r12_add = r12_add;
    }

    public BigDecimal getR12_disposals() {
        return r12_disposals;
    }

    public void setR12_disposals(BigDecimal r12_disposals) {
        this.r12_disposals = r12_disposals;
    }

    public BigDecimal getR12_depreciation() {
        return r12_depreciation;
    }

    public void setR12_depreciation(BigDecimal r12_depreciation) {
        this.r12_depreciation = r12_depreciation;
    }

    public BigDecimal getR12_net_book_value() {
        return r12_net_book_value;
    }

    public void setR12_net_book_value(BigDecimal r12_net_book_value) {
        this.r12_net_book_value = r12_net_book_value;
    }

    public String getR23_intangible_ass() {
        return r23_intangible_ass;
    }

    public void setR23_intangible_ass(String r23_intangible_ass) {
        this.r23_intangible_ass = r23_intangible_ass;
    }

    public BigDecimal getR23_cost_rev() {
        return r23_cost_rev;
    }

    public void setR23_cost_rev(BigDecimal r23_cost_rev) {
        this.r23_cost_rev = r23_cost_rev;
    }

    public BigDecimal getR23_useful_life() {
        return r23_useful_life;
    }

    public void setR23_useful_life(BigDecimal r23_useful_life) {
        this.r23_useful_life = r23_useful_life;
    }

    public BigDecimal getR23_res_value() {
        return r23_res_value;
    }

    public void setR23_res_value(BigDecimal r23_res_value) {
        this.r23_res_value = r23_res_value;
    }

    public BigDecimal getR23_month_amort() {
        return r23_month_amort;
    }

    public void setR23_month_amort(BigDecimal r23_month_amort) {
        this.r23_month_amort = r23_month_amort;
    }

    public BigDecimal getR23_acc_amort_amt() {
        return r23_acc_amort_amt;
    }

    public void setR23_acc_amort_amt(BigDecimal r23_acc_amort_amt) {
        this.r23_acc_amort_amt = r23_acc_amort_amt;
    }

    public BigDecimal getR23_close_bal() {
        return r23_close_bal;
    }

    public void setR23_close_bal(BigDecimal r23_close_bal) {
        this.r23_close_bal = r23_close_bal;
    }

    public String getR24_intangible_ass() {
        return r24_intangible_ass;
    }

    public void setR24_intangible_ass(String r24_intangible_ass) {
        this.r24_intangible_ass = r24_intangible_ass;
    }

    public BigDecimal getR24_cost_rev() {
        return r24_cost_rev;
    }

    public void setR24_cost_rev(BigDecimal r24_cost_rev) {
        this.r24_cost_rev = r24_cost_rev;
    }

    public BigDecimal getR24_useful_life() {
        return r24_useful_life;
    }

    public void setR24_useful_life(BigDecimal r24_useful_life) {
        this.r24_useful_life = r24_useful_life;
    }

    public BigDecimal getR24_res_value() {
        return r24_res_value;
    }

    public void setR24_res_value(BigDecimal r24_res_value) {
        this.r24_res_value = r24_res_value;
    }

    public BigDecimal getR24_month_amort() {
        return r24_month_amort;
    }

    public void setR24_month_amort(BigDecimal r24_month_amort) {
        this.r24_month_amort = r24_month_amort;
    }

    public BigDecimal getR24_acc_amort_amt() {
        return r24_acc_amort_amt;
    }

    public void setR24_acc_amort_amt(BigDecimal r24_acc_amort_amt) {
        this.r24_acc_amort_amt = r24_acc_amort_amt;
    }

    public BigDecimal getR24_close_bal() {
        return r24_close_bal;
    }

    public void setR24_close_bal(BigDecimal r24_close_bal) {
        this.r24_close_bal = r24_close_bal;
    }

    public String getR25_intangible_ass() {
        return r25_intangible_ass;
    }

    public void setR25_intangible_ass(String r25_intangible_ass) {
        this.r25_intangible_ass = r25_intangible_ass;
    }

    public BigDecimal getR25_cost_rev() {
        return r25_cost_rev;
    }

    public void setR25_cost_rev(BigDecimal r25_cost_rev) {
        this.r25_cost_rev = r25_cost_rev;
    }

    public BigDecimal getR25_useful_life() {
        return r25_useful_life;
    }

    public void setR25_useful_life(BigDecimal r25_useful_life) {
        this.r25_useful_life = r25_useful_life;
    }

    public BigDecimal getR25_res_value() {
        return r25_res_value;
    }

    public void setR25_res_value(BigDecimal r25_res_value) {
        this.r25_res_value = r25_res_value;
    }

    public BigDecimal getR25_month_amort() {
        return r25_month_amort;
    }

    public void setR25_month_amort(BigDecimal r25_month_amort) {
        this.r25_month_amort = r25_month_amort;
    }

    public BigDecimal getR25_acc_amort_amt() {
        return r25_acc_amort_amt;
    }

    public void setR25_acc_amort_amt(BigDecimal r25_acc_amort_amt) {
        this.r25_acc_amort_amt = r25_acc_amort_amt;
    }

    public BigDecimal getR25_close_bal() {
        return r25_close_bal;
    }

    public void setR25_close_bal(BigDecimal r25_close_bal) {
        this.r25_close_bal = r25_close_bal;
    }

    public String getR26_intangible_ass() {
        return r26_intangible_ass;
    }

    public void setR26_intangible_ass(String r26_intangible_ass) {
        this.r26_intangible_ass = r26_intangible_ass;
    }

    public BigDecimal getR26_cost_rev() {
        return r26_cost_rev;
    }

    public void setR26_cost_rev(BigDecimal r26_cost_rev) {
        this.r26_cost_rev = r26_cost_rev;
    }

    public BigDecimal getR26_useful_life() {
        return r26_useful_life;
    }

    public void setR26_useful_life(BigDecimal r26_useful_life) {
        this.r26_useful_life = r26_useful_life;
    }

    public BigDecimal getR26_res_value() {
        return r26_res_value;
    }

    public void setR26_res_value(BigDecimal r26_res_value) {
        this.r26_res_value = r26_res_value;
    }

    public BigDecimal getR26_month_amort() {
        return r26_month_amort;
    }

    public void setR26_month_amort(BigDecimal r26_month_amort) {
        this.r26_month_amort = r26_month_amort;
    }

    public BigDecimal getR26_acc_amort_amt() {
        return r26_acc_amort_amt;
    }

    public void setR26_acc_amort_amt(BigDecimal r26_acc_amort_amt) {
        this.r26_acc_amort_amt = r26_acc_amort_amt;
    }

    public BigDecimal getR26_close_bal() {
        return r26_close_bal;
    }

    public void setR26_close_bal(BigDecimal r26_close_bal) {
        this.r26_close_bal = r26_close_bal;
    }

    public String getR27_intangible_ass() {
        return r27_intangible_ass;
    }

    public void setR27_intangible_ass(String r27_intangible_ass) {
        this.r27_intangible_ass = r27_intangible_ass;
    }

    public BigDecimal getR27_cost_rev() {
        return r27_cost_rev;
    }

    public void setR27_cost_rev(BigDecimal r27_cost_rev) {
        this.r27_cost_rev = r27_cost_rev;
    }

    public BigDecimal getR27_useful_life() {
        return r27_useful_life;
    }

    public void setR27_useful_life(BigDecimal r27_useful_life) {
        this.r27_useful_life = r27_useful_life;
    }

    public BigDecimal getR27_res_value() {
        return r27_res_value;
    }

    public void setR27_res_value(BigDecimal r27_res_value) {
        this.r27_res_value = r27_res_value;
    }

    public BigDecimal getR27_month_amort() {
        return r27_month_amort;
    }

    public void setR27_month_amort(BigDecimal r27_month_amort) {
        this.r27_month_amort = r27_month_amort;
    }

    public BigDecimal getR27_acc_amort_amt() {
        return r27_acc_amort_amt;
    }

    public void setR27_acc_amort_amt(BigDecimal r27_acc_amort_amt) {
        this.r27_acc_amort_amt = r27_acc_amort_amt;
    }

    public BigDecimal getR27_close_bal() {
        return r27_close_bal;
    }

    public void setR27_close_bal(BigDecimal r27_close_bal) {
        this.r27_close_bal = r27_close_bal;
    }

    public String getR28_intangible_ass() {
        return r28_intangible_ass;
    }

    public void setR28_intangible_ass(String r28_intangible_ass) {
        this.r28_intangible_ass = r28_intangible_ass;
    }

    public BigDecimal getR28_cost_rev() {
        return r28_cost_rev;
    }

    public void setR28_cost_rev(BigDecimal r28_cost_rev) {
        this.r28_cost_rev = r28_cost_rev;
    }

    public BigDecimal getR28_useful_life() {
        return r28_useful_life;
    }

    public void setR28_useful_life(BigDecimal r28_useful_life) {
        this.r28_useful_life = r28_useful_life;
    }

    public BigDecimal getR28_res_value() {
        return r28_res_value;
    }

    public void setR28_res_value(BigDecimal r28_res_value) {
        this.r28_res_value = r28_res_value;
    }

    public BigDecimal getR28_month_amort() {
        return r28_month_amort;
    }

    public void setR28_month_amort(BigDecimal r28_month_amort) {
        this.r28_month_amort = r28_month_amort;
    }

    public BigDecimal getR28_acc_amort_amt() {
        return r28_acc_amort_amt;
    }

    public void setR28_acc_amort_amt(BigDecimal r28_acc_amort_amt) {
        this.r28_acc_amort_amt = r28_acc_amort_amt;
    }

    public BigDecimal getR28_close_bal() {
        return r28_close_bal;
    }

    public void setR28_close_bal(BigDecimal r28_close_bal) {
        this.r28_close_bal = r28_close_bal;
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


}
