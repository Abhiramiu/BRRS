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
@Table(name = "BRRS_PL_SCHS_MANUAL_ARCHIVALTABLE_SUMMARY", schema = "BRRS")
public class PL_SCHS_Manual_Archival_Summary_Entity {

    // ================= R12 =================
    @Column(name = "R12_INTREST_DIV")
    private String r12_intrest_div;
    @Column(name = "R12_FIG_BAL_SHEET")
    private BigDecimal r12_fig_bal_sheet;
    @Column(name = "R12_FIG_BAL_SHEET_BWP")
    private BigDecimal r12_fig_bal_sheet_bwp;
    @Column(name = "R12_AMT_STATEMENT_ADJ")
    private BigDecimal r12_amt_statement_adj;
    @Column(name = "R12_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r12_amt_statement_adj_bwp;
    @Column(name = "R12_NET_AMT")
    private BigDecimal r12_net_amt;
    @Column(name = "R12_NET_AMT_BWP")
    private BigDecimal r12_net_amt_bwp;
    @Column(name = "R12_BAL_SUB")
    private BigDecimal r12_bal_sub;
    @Column(name = "R12_BAL_SUB_BWP")
    private BigDecimal r12_bal_sub_bwp;
    @Column(name = "R12_BAL_SUB_DIARIES")
    private BigDecimal r12_bal_sub_diaries;
    @Column(name = "R12_BAL_SUB_DIARIES_BWP")
    private BigDecimal r12_bal_sub_diaries_bwp;

    // ================= R20 =================
    @Column(name = "R20_OTHER_INCOME")
    private String r20_other_income;
    @Column(name = "R20_FIG_BAL_SHEET")
    private BigDecimal r20_fig_bal_sheet;
    @Column(name = "R20_FIG_BAL_SHEET_BWP")
    private BigDecimal r20_fig_bal_sheet_bwp;
    @Column(name = "R20_AMT_STATEMENT_ADJ")
    private BigDecimal r20_amt_statement_adj;
    @Column(name = "R20_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r20_amt_statement_adj_bwp;
    @Column(name = "R20_NET_AMT")
    private BigDecimal r20_net_amt;
    @Column(name = "R20_NET_AMT_BWP")
    private BigDecimal r20_net_amt_bwp;
    @Column(name = "R20_BAL_SUB")
    private BigDecimal r20_bal_sub;
    @Column(name = "R20_BAL_SUB_BWP")
    private BigDecimal r20_bal_sub_bwp;
    @Column(name = "R20_BAL_SUB_DIARIES")
    private BigDecimal r20_bal_sub_diaries;
    @Column(name = "R20_BAL_SUB_DIARIES_BWP")
    private BigDecimal r20_bal_sub_diaries_bwp;

    // ================= R21 =================
    @Column(name = "R21_OTHER_INCOME")
    private String r21_other_income;
    @Column(name = "R21_FIG_BAL_SHEET")
    private BigDecimal r21_fig_bal_sheet;
    @Column(name = "R21_FIG_BAL_SHEET_BWP")
    private BigDecimal r21_fig_bal_sheet_bwp;
    @Column(name = "R21_AMT_STATEMENT_ADJ")
    private BigDecimal r21_amt_statement_adj;
    @Column(name = "R21_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r21_amt_statement_adj_bwp;
    @Column(name = "R21_NET_AMT")
    private BigDecimal r21_net_amt;
    @Column(name = "R21_NET_AMT_BWP")
    private BigDecimal r21_net_amt_bwp;
    @Column(name = "R21_BAL_SUB")
    private BigDecimal r21_bal_sub;
    @Column(name = "R21_BAL_SUB_BWP")
    private BigDecimal r21_bal_sub_bwp;
    @Column(name = "R21_BAL_SUB_DIARIES")
    private BigDecimal r21_bal_sub_diaries;
    @Column(name = "R21_BAL_SUB_DIARIES_BWP")
    private BigDecimal r21_bal_sub_diaries_bwp;

    // ================= R22 =================
    @Column(name = "R22_OTHER_INCOME")
    private String r22_other_income;
    @Column(name = "R22_FIG_BAL_SHEET")
    private BigDecimal r22_fig_bal_sheet;
    @Column(name = "R22_FIG_BAL_SHEET_BWP")
    private BigDecimal r22_fig_bal_sheet_bwp;
    @Column(name = "R22_AMT_STATEMENT_ADJ")
    private BigDecimal r22_amt_statement_adj;
    @Column(name = "R22_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r22_amt_statement_adj_bwp;
    @Column(name = "R22_NET_AMT")
    private BigDecimal r22_net_amt;
    @Column(name = "R22_NET_AMT_BWP")
    private BigDecimal r22_net_amt_bwp;
    @Column(name = "R22_BAL_SUB")
    private BigDecimal r22_bal_sub;
    @Column(name = "R22_BAL_SUB_BWP")
    private BigDecimal r22_bal_sub_bwp;
    @Column(name = "R22_BAL_SUB_DIARIES")
    private BigDecimal r22_bal_sub_diaries;
    @Column(name = "R22_BAL_SUB_DIARIES_BWP")
    private BigDecimal r22_bal_sub_diaries_bwp;

    // ================= R23 =================
    @Column(name = "R23_OTHER_INCOME")
    private String r23_other_income;
    @Column(name = "R23_FIG_BAL_SHEET")
    private BigDecimal r23_fig_bal_sheet;
    @Column(name = "R23_FIG_BAL_SHEET_BWP")
    private BigDecimal r23_fig_bal_sheet_bwp;
    @Column(name = "R23_AMT_STATEMENT_ADJ")
    private BigDecimal r23_amt_statement_adj;
    @Column(name = "R23_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r23_amt_statement_adj_bwp;
    @Column(name = "R23_NET_AMT")
    private BigDecimal r23_net_amt;
    @Column(name = "R23_NET_AMT_BWP")
    private BigDecimal r23_net_amt_bwp;
    @Column(name = "R23_BAL_SUB")
    private BigDecimal r23_bal_sub;
    @Column(name = "R23_BAL_SUB_BWP")
    private BigDecimal r23_bal_sub_bwp;
    @Column(name = "R23_BAL_SUB_DIARIES")
    private BigDecimal r23_bal_sub_diaries;
    @Column(name = "R23_BAL_SUB_DIARIES_BWP")
    private BigDecimal r23_bal_sub_diaries_bwp;

    // ================= R24 =================
    @Column(name = "R24_OTHER_INCOME")
    private String r24_other_income;
    @Column(name = "R24_FIG_BAL_SHEET")
    private BigDecimal r24_fig_bal_sheet;
    @Column(name = "R24_FIG_BAL_SHEET_BWP")
    private BigDecimal r24_fig_bal_sheet_bwp;
    @Column(name = "R24_AMT_STATEMENT_ADJ")
    private BigDecimal r24_amt_statement_adj;
    @Column(name = "R24_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r24_amt_statement_adj_bwp;
    @Column(name = "R24_NET_AMT")
    private BigDecimal r24_net_amt;
    @Column(name = "R24_NET_AMT_BWP")
    private BigDecimal r24_net_amt_bwp;
    @Column(name = "R24_BAL_SUB")
    private BigDecimal r24_bal_sub;
    @Column(name = "R24_BAL_SUB_BWP")
    private BigDecimal r24_bal_sub_bwp;
    @Column(name = "R24_BAL_SUB_DIARIES")
    private BigDecimal r24_bal_sub_diaries;
    @Column(name = "R24_BAL_SUB_DIARIES_BWP")
    private BigDecimal r24_bal_sub_diaries_bwp;

    // ================= R25 =================
    @Column(name = "R25_OTHER_INCOME")
    private String r25_other_income;
    @Column(name = "R25_FIG_BAL_SHEET")
    private BigDecimal r25_fig_bal_sheet;
    @Column(name = "R25_FIG_BAL_SHEET_BWP")
    private BigDecimal r25_fig_bal_sheet_bwp;
    @Column(name = "R25_AMT_STATEMENT_ADJ")
    private BigDecimal r25_amt_statement_adj;
    @Column(name = "R25_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r25_amt_statement_adj_bwp;
    @Column(name = "R25_NET_AMT")
    private BigDecimal r25_net_amt;
    @Column(name = "R25_NET_AMT_BWP")
    private BigDecimal r25_net_amt_bwp;
    @Column(name = "R25_BAL_SUB")
    private BigDecimal r25_bal_sub;
    @Column(name = "R25_BAL_SUB_BWP")
    private BigDecimal r25_bal_sub_bwp;
    @Column(name = "R25_BAL_SUB_DIARIES")
    private BigDecimal r25_bal_sub_diaries;
    @Column(name = "R25_BAL_SUB_DIARIES_BWP")
    private BigDecimal r25_bal_sub_diaries_bwp;

    // ================= R26 =================
    @Column(name = "R26_OTHER_INCOME")
    private String r26_other_income;
    @Column(name = "R26_FIG_BAL_SHEET")
    private BigDecimal r26_fig_bal_sheet;
    @Column(name = "R26_FIG_BAL_SHEET_BWP")
    private BigDecimal r26_fig_bal_sheet_bwp;
    @Column(name = "R26_AMT_STATEMENT_ADJ")
    private BigDecimal r26_amt_statement_adj;
    @Column(name = "R26_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r26_amt_statement_adj_bwp;
    @Column(name = "R26_NET_AMT")
    private BigDecimal r26_net_amt;
    @Column(name = "R26_NET_AMT_BWP")
    private BigDecimal r26_net_amt_bwp;
    @Column(name = "R26_BAL_SUB")
    private BigDecimal r26_bal_sub;
    @Column(name = "R26_BAL_SUB_BWP")
    private BigDecimal r26_bal_sub_bwp;
    @Column(name = "R26_BAL_SUB_DIARIES")
    private BigDecimal r26_bal_sub_diaries;
    @Column(name = "R26_BAL_SUB_DIARIES_BWP")
    private BigDecimal r26_bal_sub_diaries_bwp;

    // ================= R27 =================
    @Column(name = "R27_OTHER_INCOME")
    private String r27_other_income;
    @Column(name = "R27_FIG_BAL_SHEET")
    private BigDecimal r27_fig_bal_sheet;
    @Column(name = "R27_FIG_BAL_SHEET_BWP")
    private BigDecimal r27_fig_bal_sheet_bwp;
    @Column(name = "R27_AMT_STATEMENT_ADJ")
    private BigDecimal r27_amt_statement_adj;
    @Column(name = "R27_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r27_amt_statement_adj_bwp;
    @Column(name = "R27_NET_AMT")
    private BigDecimal r27_net_amt;
    @Column(name = "R27_NET_AMT_BWP")
    private BigDecimal r27_net_amt_bwp;
    @Column(name = "R27_BAL_SUB")
    private BigDecimal r27_bal_sub;
    @Column(name = "R27_BAL_SUB_BWP")
    private BigDecimal r27_bal_sub_bwp;
    @Column(name = "R27_BAL_SUB_DIARIES")
    private BigDecimal r27_bal_sub_diaries;
    @Column(name = "R27_BAL_SUB_DIARIES_BWP")
    private BigDecimal r27_bal_sub_diaries_bwp;

    // ================= R28 =================
    @Column(name = "R28_OTHER_INCOME")
    private String r28_other_income;
    @Column(name = "R28_FIG_BAL_SHEET")
    private BigDecimal r28_fig_bal_sheet;
    @Column(name = "R28_FIG_BAL_SHEET_BWP")
    private BigDecimal r28_fig_bal_sheet_bwp;
    @Column(name = "R28_AMT_STATEMENT_ADJ")
    private BigDecimal r28_amt_statement_adj;
    @Column(name = "R28_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r28_amt_statement_adj_bwp;
    @Column(name = "R28_NET_AMT")
    private BigDecimal r28_net_amt;
    @Column(name = "R28_NET_AMT_BWP")
    private BigDecimal r28_net_amt_bwp;
    @Column(name = "R28_BAL_SUB")
    private BigDecimal r28_bal_sub;
    @Column(name = "R28_BAL_SUB_BWP")
    private BigDecimal r28_bal_sub_bwp;
    @Column(name = "R28_BAL_SUB_DIARIES")
    private BigDecimal r28_bal_sub_diaries;
    @Column(name = "R28_BAL_SUB_DIARIES_BWP")
    private BigDecimal r28_bal_sub_diaries_bwp;

    // ================= R29 =================
    @Column(name = "R29_OTHER_INCOME")
    private String r29_other_income;
    @Column(name = "R29_FIG_BAL_SHEET")
    private BigDecimal r29_fig_bal_sheet;
    @Column(name = "R29_FIG_BAL_SHEET_BWP")
    private BigDecimal r29_fig_bal_sheet_bwp;
    @Column(name = "R29_AMT_STATEMENT_ADJ")
    private BigDecimal r29_amt_statement_adj;
    @Column(name = "R29_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r29_amt_statement_adj_bwp;
    @Column(name = "R29_NET_AMT")
    private BigDecimal r29_net_amt;
    @Column(name = "R29_NET_AMT_BWP")
    private BigDecimal r29_net_amt_bwp;
    @Column(name = "R29_BAL_SUB")
    private BigDecimal r29_bal_sub;
    @Column(name = "R29_BAL_SUB_BWP")
    private BigDecimal r29_bal_sub_bwp;
    @Column(name = "R29_BAL_SUB_DIARIES")
    private BigDecimal r29_bal_sub_diaries;
    @Column(name = "R29_BAL_SUB_DIARIES_BWP")
    private BigDecimal r29_bal_sub_diaries_bwp;

    // ================= R30 =================
    @Column(name = "R30_OTHER_INCOME")
    private String r30_other_income;
    @Column(name = "R30_FIG_BAL_SHEET")
    private BigDecimal r30_fig_bal_sheet;
    @Column(name = "R30_FIG_BAL_SHEET_BWP")
    private BigDecimal r30_fig_bal_sheet_bwp;
    @Column(name = "R30_AMT_STATEMENT_ADJ")
    private BigDecimal r30_amt_statement_adj;
    @Column(name = "R30_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r30_amt_statement_adj_bwp;
    @Column(name = "R30_NET_AMT")
    private BigDecimal r30_net_amt;
    @Column(name = "R30_NET_AMT_BWP")
    private BigDecimal r30_net_amt_bwp;
    @Column(name = "R30_BAL_SUB")
    private BigDecimal r30_bal_sub;
    @Column(name = "R30_BAL_SUB_BWP")
    private BigDecimal r30_bal_sub_bwp;
    @Column(name = "R30_BAL_SUB_DIARIES")
    private BigDecimal r30_bal_sub_diaries;
    @Column(name = "R30_BAL_SUB_DIARIES_BWP")
    private BigDecimal r30_bal_sub_diaries_bwp;

    // ================= R42 =================
    @Column(name = "R42_INTREST_EXPENDED")
    private String r42_intrest_expended;
    @Column(name = "R42_FIG_BAL_SHEET")
    private BigDecimal r42_fig_bal_sheet;
    @Column(name = "R42_FIG_BAL_SHEET_BWP")
    private BigDecimal r42_fig_bal_sheet_bwp;
    @Column(name = "R42_AMT_STATEMENT_ADJ")
    private BigDecimal r42_amt_statement_adj;
    @Column(name = "R42_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r42_amt_statement_adj_bwp;
    @Column(name = "R42_NET_AMT")
    private BigDecimal r42_net_amt;
    @Column(name = "R42_NET_AMT_BWP")
    private BigDecimal r42_net_amt_bwp;
    @Column(name = "R42_BAL_SUB")
    private BigDecimal r42_bal_sub;
    @Column(name = "R42_BAL_SUB_BWP")
    private BigDecimal r42_bal_sub_bwp;
    @Column(name = "R42_BAL_SUB_DIARIES")
    private BigDecimal r42_bal_sub_diaries;
    @Column(name = "R42_BAL_SUB_DIARIES_BWP")
    private BigDecimal r42_bal_sub_diaries_bwp;

    // ================= R54 =================
    @Column(name = "R54_OPERATING_EXPENSES")
    private String r54_operating_expenses;
    @Column(name = "R54_FIG_BAL_SHEET")
    private BigDecimal r54_fig_bal_sheet;
    @Column(name = "R54_FIG_BAL_SHEET_BWP")
    private BigDecimal r54_fig_bal_sheet_bwp;
    @Column(name = "R54_AMT_STATEMENT_ADJ")
    private BigDecimal r54_amt_statement_adj;
    @Column(name = "R54_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r54_amt_statement_adj_bwp;
    @Column(name = "R54_NET_AMT")
    private BigDecimal r54_net_amt;
    @Column(name = "R54_NET_AMT_BWP")
    private BigDecimal r54_net_amt_bwp;
    @Column(name = "R54_BAL_SUB")
    private BigDecimal r54_bal_sub;
    @Column(name = "R54_BAL_SUB_BWP")
    private BigDecimal r54_bal_sub_bwp;
    @Column(name = "R54_BAL_SUB_DIARIES")
    private BigDecimal r54_bal_sub_diaries;
    @Column(name = "R54_BAL_SUB_DIARIES_BWP")
    private BigDecimal r54_bal_sub_diaries_bwp;

    // ================= R61 =================
    @Column(name = "R61_OPERATING_EXPENSES")
    private String r61_operating_expenses;
    @Column(name = "R61_FIG_BAL_SHEET")
    private BigDecimal r61_fig_bal_sheet;
    @Column(name = "R61_FIG_BAL_SHEET_BWP")
    private BigDecimal r61_fig_bal_sheet_bwp;
    @Column(name = "R61_AMT_STATEMENT_ADJ")
    private BigDecimal r61_amt_statement_adj;
    @Column(name = "R61_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r61_amt_statement_adj_bwp;
    @Column(name = "R61_NET_AMT")
    private BigDecimal r61_net_amt;
    @Column(name = "R61_NET_AMT_BWP")
    private BigDecimal r61_net_amt_bwp;
    @Column(name = "R61_BAL_SUB")
    private BigDecimal r61_bal_sub;
    @Column(name = "R61_BAL_SUB_BWP")
    private BigDecimal r61_bal_sub_bwp;
    @Column(name = "R61_BAL_SUB_DIARIES")
    private BigDecimal r61_bal_sub_diaries;
    @Column(name = "R61_BAL_SUB_DIARIES_BWP")
    private BigDecimal r61_bal_sub_diaries_bwp;

    // ================= REPORT INFO =================
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
    
    public String getR12_intrest_div() {
        return r12_intrest_div;
    }
    public void setR12_intrest_div(String r12_intrest_div) {
        this.r12_intrest_div = r12_intrest_div;
    }
    public BigDecimal getR12_fig_bal_sheet() {
        return r12_fig_bal_sheet;
    }
    public void setR12_fig_bal_sheet(BigDecimal r12_fig_bal_sheet) {
        this.r12_fig_bal_sheet = r12_fig_bal_sheet;
    }
    public BigDecimal getR12_fig_bal_sheet_bwp() {
        return r12_fig_bal_sheet_bwp;
    }
    public void setR12_fig_bal_sheet_bwp(BigDecimal r12_fig_bal_sheet_bwp) {
        this.r12_fig_bal_sheet_bwp = r12_fig_bal_sheet_bwp;
    }
    public BigDecimal getR12_amt_statement_adj() {
        return r12_amt_statement_adj;
    }
    public void setR12_amt_statement_adj(BigDecimal r12_amt_statement_adj) {
        this.r12_amt_statement_adj = r12_amt_statement_adj;
    }
    public BigDecimal getR12_amt_statement_adj_bwp() {
        return r12_amt_statement_adj_bwp;
    }
    public void setR12_amt_statement_adj_bwp(BigDecimal r12_amt_statement_adj_bwp) {
        this.r12_amt_statement_adj_bwp = r12_amt_statement_adj_bwp;
    }
    public BigDecimal getR12_net_amt() {
        return r12_net_amt;
    }
    public void setR12_net_amt(BigDecimal r12_net_amt) {
        this.r12_net_amt = r12_net_amt;
    }
    public BigDecimal getR12_net_amt_bwp() {
        return r12_net_amt_bwp;
    }
    public void setR12_net_amt_bwp(BigDecimal r12_net_amt_bwp) {
        this.r12_net_amt_bwp = r12_net_amt_bwp;
    }
    public BigDecimal getR12_bal_sub() {
        return r12_bal_sub;
    }
    public void setR12_bal_sub(BigDecimal r12_bal_sub) {
        this.r12_bal_sub = r12_bal_sub;
    }
    public BigDecimal getR12_bal_sub_bwp() {
        return r12_bal_sub_bwp;
    }
    public void setR12_bal_sub_bwp(BigDecimal r12_bal_sub_bwp) {
        this.r12_bal_sub_bwp = r12_bal_sub_bwp;
    }
    public BigDecimal getR12_bal_sub_diaries() {
        return r12_bal_sub_diaries;
    }
    public void setR12_bal_sub_diaries(BigDecimal r12_bal_sub_diaries) {
        this.r12_bal_sub_diaries = r12_bal_sub_diaries;
    }
    public BigDecimal getR12_bal_sub_diaries_bwp() {
        return r12_bal_sub_diaries_bwp;
    }
    public void setR12_bal_sub_diaries_bwp(BigDecimal r12_bal_sub_diaries_bwp) {
        this.r12_bal_sub_diaries_bwp = r12_bal_sub_diaries_bwp;
    }
    public String getR20_other_income() {
        return r20_other_income;
    }
    public void setR20_other_income(String r20_other_income) {
        this.r20_other_income = r20_other_income;
    }
    public BigDecimal getR20_fig_bal_sheet() {
        return r20_fig_bal_sheet;
    }
    public void setR20_fig_bal_sheet(BigDecimal r20_fig_bal_sheet) {
        this.r20_fig_bal_sheet = r20_fig_bal_sheet;
    }
    public BigDecimal getR20_fig_bal_sheet_bwp() {
        return r20_fig_bal_sheet_bwp;
    }
    public void setR20_fig_bal_sheet_bwp(BigDecimal r20_fig_bal_sheet_bwp) {
        this.r20_fig_bal_sheet_bwp = r20_fig_bal_sheet_bwp;
    }
    public BigDecimal getR20_amt_statement_adj() {
        return r20_amt_statement_adj;
    }
    public void setR20_amt_statement_adj(BigDecimal r20_amt_statement_adj) {
        this.r20_amt_statement_adj = r20_amt_statement_adj;
    }
    public BigDecimal getR20_amt_statement_adj_bwp() {
        return r20_amt_statement_adj_bwp;
    }
    public void setR20_amt_statement_adj_bwp(BigDecimal r20_amt_statement_adj_bwp) {
        this.r20_amt_statement_adj_bwp = r20_amt_statement_adj_bwp;
    }
    public BigDecimal getR20_net_amt() {
        return r20_net_amt;
    }
    public void setR20_net_amt(BigDecimal r20_net_amt) {
        this.r20_net_amt = r20_net_amt;
    }
    public BigDecimal getR20_net_amt_bwp() {
        return r20_net_amt_bwp;
    }
    public void setR20_net_amt_bwp(BigDecimal r20_net_amt_bwp) {
        this.r20_net_amt_bwp = r20_net_amt_bwp;
    }
    public BigDecimal getR20_bal_sub() {
        return r20_bal_sub;
    }
    public void setR20_bal_sub(BigDecimal r20_bal_sub) {
        this.r20_bal_sub = r20_bal_sub;
    }
    public BigDecimal getR20_bal_sub_bwp() {
        return r20_bal_sub_bwp;
    }
    public void setR20_bal_sub_bwp(BigDecimal r20_bal_sub_bwp) {
        this.r20_bal_sub_bwp = r20_bal_sub_bwp;
    }
    public BigDecimal getR20_bal_sub_diaries() {
        return r20_bal_sub_diaries;
    }
    public void setR20_bal_sub_diaries(BigDecimal r20_bal_sub_diaries) {
        this.r20_bal_sub_diaries = r20_bal_sub_diaries;
    }
    public BigDecimal getR20_bal_sub_diaries_bwp() {
        return r20_bal_sub_diaries_bwp;
    }
    public void setR20_bal_sub_diaries_bwp(BigDecimal r20_bal_sub_diaries_bwp) {
        this.r20_bal_sub_diaries_bwp = r20_bal_sub_diaries_bwp;
    }
    public String getR21_other_income() {
        return r21_other_income;
    }
    public void setR21_other_income(String r21_other_income) {
        this.r21_other_income = r21_other_income;
    }
    public BigDecimal getR21_fig_bal_sheet() {
        return r21_fig_bal_sheet;
    }
    public void setR21_fig_bal_sheet(BigDecimal r21_fig_bal_sheet) {
        this.r21_fig_bal_sheet = r21_fig_bal_sheet;
    }
    public BigDecimal getR21_fig_bal_sheet_bwp() {
        return r21_fig_bal_sheet_bwp;
    }
    public void setR21_fig_bal_sheet_bwp(BigDecimal r21_fig_bal_sheet_bwp) {
        this.r21_fig_bal_sheet_bwp = r21_fig_bal_sheet_bwp;
    }
    public BigDecimal getR21_amt_statement_adj() {
        return r21_amt_statement_adj;
    }
    public void setR21_amt_statement_adj(BigDecimal r21_amt_statement_adj) {
        this.r21_amt_statement_adj = r21_amt_statement_adj;
    }
    public BigDecimal getR21_amt_statement_adj_bwp() {
        return r21_amt_statement_adj_bwp;
    }
    public void setR21_amt_statement_adj_bwp(BigDecimal r21_amt_statement_adj_bwp) {
        this.r21_amt_statement_adj_bwp = r21_amt_statement_adj_bwp;
    }
    public BigDecimal getR21_net_amt() {
        return r21_net_amt;
    }
    public void setR21_net_amt(BigDecimal r21_net_amt) {
        this.r21_net_amt = r21_net_amt;
    }
    public BigDecimal getR21_net_amt_bwp() {
        return r21_net_amt_bwp;
    }
    public void setR21_net_amt_bwp(BigDecimal r21_net_amt_bwp) {
        this.r21_net_amt_bwp = r21_net_amt_bwp;
    }
    public BigDecimal getR21_bal_sub() {
        return r21_bal_sub;
    }
    public void setR21_bal_sub(BigDecimal r21_bal_sub) {
        this.r21_bal_sub = r21_bal_sub;
    }
    public BigDecimal getR21_bal_sub_bwp() {
        return r21_bal_sub_bwp;
    }
    public void setR21_bal_sub_bwp(BigDecimal r21_bal_sub_bwp) {
        this.r21_bal_sub_bwp = r21_bal_sub_bwp;
    }
    public BigDecimal getR21_bal_sub_diaries() {
        return r21_bal_sub_diaries;
    }
    public void setR21_bal_sub_diaries(BigDecimal r21_bal_sub_diaries) {
        this.r21_bal_sub_diaries = r21_bal_sub_diaries;
    }
    public BigDecimal getR21_bal_sub_diaries_bwp() {
        return r21_bal_sub_diaries_bwp;
    }
    public void setR21_bal_sub_diaries_bwp(BigDecimal r21_bal_sub_diaries_bwp) {
        this.r21_bal_sub_diaries_bwp = r21_bal_sub_diaries_bwp;
    }
    public String getR22_other_income() {
        return r22_other_income;
    }
    public void setR22_other_income(String r22_other_income) {
        this.r22_other_income = r22_other_income;
    }
    public BigDecimal getR22_fig_bal_sheet() {
        return r22_fig_bal_sheet;
    }
    public void setR22_fig_bal_sheet(BigDecimal r22_fig_bal_sheet) {
        this.r22_fig_bal_sheet = r22_fig_bal_sheet;
    }
    public BigDecimal getR22_fig_bal_sheet_bwp() {
        return r22_fig_bal_sheet_bwp;
    }
    public void setR22_fig_bal_sheet_bwp(BigDecimal r22_fig_bal_sheet_bwp) {
        this.r22_fig_bal_sheet_bwp = r22_fig_bal_sheet_bwp;
    }
    public BigDecimal getR22_amt_statement_adj() {
        return r22_amt_statement_adj;
    }
    public void setR22_amt_statement_adj(BigDecimal r22_amt_statement_adj) {
        this.r22_amt_statement_adj = r22_amt_statement_adj;
    }
    public BigDecimal getR22_amt_statement_adj_bwp() {
        return r22_amt_statement_adj_bwp;
    }
    public void setR22_amt_statement_adj_bwp(BigDecimal r22_amt_statement_adj_bwp) {
        this.r22_amt_statement_adj_bwp = r22_amt_statement_adj_bwp;
    }
    public BigDecimal getR22_net_amt() {
        return r22_net_amt;
    }
    public void setR22_net_amt(BigDecimal r22_net_amt) {
        this.r22_net_amt = r22_net_amt;
    }
    public BigDecimal getR22_net_amt_bwp() {
        return r22_net_amt_bwp;
    }
    public void setR22_net_amt_bwp(BigDecimal r22_net_amt_bwp) {
        this.r22_net_amt_bwp = r22_net_amt_bwp;
    }
    public BigDecimal getR22_bal_sub() {
        return r22_bal_sub;
    }
    public void setR22_bal_sub(BigDecimal r22_bal_sub) {
        this.r22_bal_sub = r22_bal_sub;
    }
    public BigDecimal getR22_bal_sub_bwp() {
        return r22_bal_sub_bwp;
    }
    public void setR22_bal_sub_bwp(BigDecimal r22_bal_sub_bwp) {
        this.r22_bal_sub_bwp = r22_bal_sub_bwp;
    }
    public BigDecimal getR22_bal_sub_diaries() {
        return r22_bal_sub_diaries;
    }
    public void setR22_bal_sub_diaries(BigDecimal r22_bal_sub_diaries) {
        this.r22_bal_sub_diaries = r22_bal_sub_diaries;
    }
    public BigDecimal getR22_bal_sub_diaries_bwp() {
        return r22_bal_sub_diaries_bwp;
    }
    public void setR22_bal_sub_diaries_bwp(BigDecimal r22_bal_sub_diaries_bwp) {
        this.r22_bal_sub_diaries_bwp = r22_bal_sub_diaries_bwp;
    }
    public String getR23_other_income() {
        return r23_other_income;
    }
    public void setR23_other_income(String r23_other_income) {
        this.r23_other_income = r23_other_income;
    }
    public BigDecimal getR23_fig_bal_sheet() {
        return r23_fig_bal_sheet;
    }
    public void setR23_fig_bal_sheet(BigDecimal r23_fig_bal_sheet) {
        this.r23_fig_bal_sheet = r23_fig_bal_sheet;
    }
    public BigDecimal getR23_fig_bal_sheet_bwp() {
        return r23_fig_bal_sheet_bwp;
    }
    public void setR23_fig_bal_sheet_bwp(BigDecimal r23_fig_bal_sheet_bwp) {
        this.r23_fig_bal_sheet_bwp = r23_fig_bal_sheet_bwp;
    }
    public BigDecimal getR23_amt_statement_adj() {
        return r23_amt_statement_adj;
    }
    public void setR23_amt_statement_adj(BigDecimal r23_amt_statement_adj) {
        this.r23_amt_statement_adj = r23_amt_statement_adj;
    }
    public BigDecimal getR23_amt_statement_adj_bwp() {
        return r23_amt_statement_adj_bwp;
    }
    public void setR23_amt_statement_adj_bwp(BigDecimal r23_amt_statement_adj_bwp) {
        this.r23_amt_statement_adj_bwp = r23_amt_statement_adj_bwp;
    }
    public BigDecimal getR23_net_amt() {
        return r23_net_amt;
    }
    public void setR23_net_amt(BigDecimal r23_net_amt) {
        this.r23_net_amt = r23_net_amt;
    }
    public BigDecimal getR23_net_amt_bwp() {
        return r23_net_amt_bwp;
    }
    public void setR23_net_amt_bwp(BigDecimal r23_net_amt_bwp) {
        this.r23_net_amt_bwp = r23_net_amt_bwp;
    }
    public BigDecimal getR23_bal_sub() {
        return r23_bal_sub;
    }
    public void setR23_bal_sub(BigDecimal r23_bal_sub) {
        this.r23_bal_sub = r23_bal_sub;
    }
    public BigDecimal getR23_bal_sub_bwp() {
        return r23_bal_sub_bwp;
    }
    public void setR23_bal_sub_bwp(BigDecimal r23_bal_sub_bwp) {
        this.r23_bal_sub_bwp = r23_bal_sub_bwp;
    }
    public BigDecimal getR23_bal_sub_diaries() {
        return r23_bal_sub_diaries;
    }
    public void setR23_bal_sub_diaries(BigDecimal r23_bal_sub_diaries) {
        this.r23_bal_sub_diaries = r23_bal_sub_diaries;
    }
    public BigDecimal getR23_bal_sub_diaries_bwp() {
        return r23_bal_sub_diaries_bwp;
    }
    public void setR23_bal_sub_diaries_bwp(BigDecimal r23_bal_sub_diaries_bwp) {
        this.r23_bal_sub_diaries_bwp = r23_bal_sub_diaries_bwp;
    }
    public String getR24_other_income() {
        return r24_other_income;
    }
    public void setR24_other_income(String r24_other_income) {
        this.r24_other_income = r24_other_income;
    }
    public BigDecimal getR24_fig_bal_sheet() {
        return r24_fig_bal_sheet;
    }
    public void setR24_fig_bal_sheet(BigDecimal r24_fig_bal_sheet) {
        this.r24_fig_bal_sheet = r24_fig_bal_sheet;
    }
    public BigDecimal getR24_fig_bal_sheet_bwp() {
        return r24_fig_bal_sheet_bwp;
    }
    public void setR24_fig_bal_sheet_bwp(BigDecimal r24_fig_bal_sheet_bwp) {
        this.r24_fig_bal_sheet_bwp = r24_fig_bal_sheet_bwp;
    }
    public BigDecimal getR24_amt_statement_adj() {
        return r24_amt_statement_adj;
    }
    public void setR24_amt_statement_adj(BigDecimal r24_amt_statement_adj) {
        this.r24_amt_statement_adj = r24_amt_statement_adj;
    }
    public BigDecimal getR24_amt_statement_adj_bwp() {
        return r24_amt_statement_adj_bwp;
    }
    public void setR24_amt_statement_adj_bwp(BigDecimal r24_amt_statement_adj_bwp) {
        this.r24_amt_statement_adj_bwp = r24_amt_statement_adj_bwp;
    }
    public BigDecimal getR24_net_amt() {
        return r24_net_amt;
    }
    public void setR24_net_amt(BigDecimal r24_net_amt) {
        this.r24_net_amt = r24_net_amt;
    }
    public BigDecimal getR24_net_amt_bwp() {
        return r24_net_amt_bwp;
    }
    public void setR24_net_amt_bwp(BigDecimal r24_net_amt_bwp) {
        this.r24_net_amt_bwp = r24_net_amt_bwp;
    }
    public BigDecimal getR24_bal_sub() {
        return r24_bal_sub;
    }
    public void setR24_bal_sub(BigDecimal r24_bal_sub) {
        this.r24_bal_sub = r24_bal_sub;
    }
    public BigDecimal getR24_bal_sub_bwp() {
        return r24_bal_sub_bwp;
    }
    public void setR24_bal_sub_bwp(BigDecimal r24_bal_sub_bwp) {
        this.r24_bal_sub_bwp = r24_bal_sub_bwp;
    }
    public BigDecimal getR24_bal_sub_diaries() {
        return r24_bal_sub_diaries;
    }
    public void setR24_bal_sub_diaries(BigDecimal r24_bal_sub_diaries) {
        this.r24_bal_sub_diaries = r24_bal_sub_diaries;
    }
    public BigDecimal getR24_bal_sub_diaries_bwp() {
        return r24_bal_sub_diaries_bwp;
    }
    public void setR24_bal_sub_diaries_bwp(BigDecimal r24_bal_sub_diaries_bwp) {
        this.r24_bal_sub_diaries_bwp = r24_bal_sub_diaries_bwp;
    }
    public String getR25_other_income() {
        return r25_other_income;
    }
    public void setR25_other_income(String r25_other_income) {
        this.r25_other_income = r25_other_income;
    }
    public BigDecimal getR25_fig_bal_sheet() {
        return r25_fig_bal_sheet;
    }
    public void setR25_fig_bal_sheet(BigDecimal r25_fig_bal_sheet) {
        this.r25_fig_bal_sheet = r25_fig_bal_sheet;
    }
    public BigDecimal getR25_fig_bal_sheet_bwp() {
        return r25_fig_bal_sheet_bwp;
    }
    public void setR25_fig_bal_sheet_bwp(BigDecimal r25_fig_bal_sheet_bwp) {
        this.r25_fig_bal_sheet_bwp = r25_fig_bal_sheet_bwp;
    }
    public BigDecimal getR25_amt_statement_adj() {
        return r25_amt_statement_adj;
    }
    public void setR25_amt_statement_adj(BigDecimal r25_amt_statement_adj) {
        this.r25_amt_statement_adj = r25_amt_statement_adj;
    }
    public BigDecimal getR25_amt_statement_adj_bwp() {
        return r25_amt_statement_adj_bwp;
    }
    public void setR25_amt_statement_adj_bwp(BigDecimal r25_amt_statement_adj_bwp) {
        this.r25_amt_statement_adj_bwp = r25_amt_statement_adj_bwp;
    }
    public BigDecimal getR25_net_amt() {
        return r25_net_amt;
    }
    public void setR25_net_amt(BigDecimal r25_net_amt) {
        this.r25_net_amt = r25_net_amt;
    }
    public BigDecimal getR25_net_amt_bwp() {
        return r25_net_amt_bwp;
    }
    public void setR25_net_amt_bwp(BigDecimal r25_net_amt_bwp) {
        this.r25_net_amt_bwp = r25_net_amt_bwp;
    }
    public BigDecimal getR25_bal_sub() {
        return r25_bal_sub;
    }
    public void setR25_bal_sub(BigDecimal r25_bal_sub) {
        this.r25_bal_sub = r25_bal_sub;
    }
    public BigDecimal getR25_bal_sub_bwp() {
        return r25_bal_sub_bwp;
    }
    public void setR25_bal_sub_bwp(BigDecimal r25_bal_sub_bwp) {
        this.r25_bal_sub_bwp = r25_bal_sub_bwp;
    }
    public BigDecimal getR25_bal_sub_diaries() {
        return r25_bal_sub_diaries;
    }
    public void setR25_bal_sub_diaries(BigDecimal r25_bal_sub_diaries) {
        this.r25_bal_sub_diaries = r25_bal_sub_diaries;
    }
    public BigDecimal getR25_bal_sub_diaries_bwp() {
        return r25_bal_sub_diaries_bwp;
    }
    public void setR25_bal_sub_diaries_bwp(BigDecimal r25_bal_sub_diaries_bwp) {
        this.r25_bal_sub_diaries_bwp = r25_bal_sub_diaries_bwp;
    }
    public String getR26_other_income() {
        return r26_other_income;
    }
    public void setR26_other_income(String r26_other_income) {
        this.r26_other_income = r26_other_income;
    }
    public BigDecimal getR26_fig_bal_sheet() {
        return r26_fig_bal_sheet;
    }
    public void setR26_fig_bal_sheet(BigDecimal r26_fig_bal_sheet) {
        this.r26_fig_bal_sheet = r26_fig_bal_sheet;
    }
    public BigDecimal getR26_fig_bal_sheet_bwp() {
        return r26_fig_bal_sheet_bwp;
    }
    public void setR26_fig_bal_sheet_bwp(BigDecimal r26_fig_bal_sheet_bwp) {
        this.r26_fig_bal_sheet_bwp = r26_fig_bal_sheet_bwp;
    }
    public BigDecimal getR26_amt_statement_adj() {
        return r26_amt_statement_adj;
    }
    public void setR26_amt_statement_adj(BigDecimal r26_amt_statement_adj) {
        this.r26_amt_statement_adj = r26_amt_statement_adj;
    }
    public BigDecimal getR26_amt_statement_adj_bwp() {
        return r26_amt_statement_adj_bwp;
    }
    public void setR26_amt_statement_adj_bwp(BigDecimal r26_amt_statement_adj_bwp) {
        this.r26_amt_statement_adj_bwp = r26_amt_statement_adj_bwp;
    }
    public BigDecimal getR26_net_amt() {
        return r26_net_amt;
    }
    public void setR26_net_amt(BigDecimal r26_net_amt) {
        this.r26_net_amt = r26_net_amt;
    }
    public BigDecimal getR26_net_amt_bwp() {
        return r26_net_amt_bwp;
    }
    public void setR26_net_amt_bwp(BigDecimal r26_net_amt_bwp) {
        this.r26_net_amt_bwp = r26_net_amt_bwp;
    }
    public BigDecimal getR26_bal_sub() {
        return r26_bal_sub;
    }
    public void setR26_bal_sub(BigDecimal r26_bal_sub) {
        this.r26_bal_sub = r26_bal_sub;
    }
    public BigDecimal getR26_bal_sub_bwp() {
        return r26_bal_sub_bwp;
    }
    public void setR26_bal_sub_bwp(BigDecimal r26_bal_sub_bwp) {
        this.r26_bal_sub_bwp = r26_bal_sub_bwp;
    }
    public BigDecimal getR26_bal_sub_diaries() {
        return r26_bal_sub_diaries;
    }
    public void setR26_bal_sub_diaries(BigDecimal r26_bal_sub_diaries) {
        this.r26_bal_sub_diaries = r26_bal_sub_diaries;
    }
    public BigDecimal getR26_bal_sub_diaries_bwp() {
        return r26_bal_sub_diaries_bwp;
    }
    public void setR26_bal_sub_diaries_bwp(BigDecimal r26_bal_sub_diaries_bwp) {
        this.r26_bal_sub_diaries_bwp = r26_bal_sub_diaries_bwp;
    }
    public String getR27_other_income() {
        return r27_other_income;
    }
    public void setR27_other_income(String r27_other_income) {
        this.r27_other_income = r27_other_income;
    }
    public BigDecimal getR27_fig_bal_sheet() {
        return r27_fig_bal_sheet;
    }
    public void setR27_fig_bal_sheet(BigDecimal r27_fig_bal_sheet) {
        this.r27_fig_bal_sheet = r27_fig_bal_sheet;
    }
    public BigDecimal getR27_fig_bal_sheet_bwp() {
        return r27_fig_bal_sheet_bwp;
    }
    public void setR27_fig_bal_sheet_bwp(BigDecimal r27_fig_bal_sheet_bwp) {
        this.r27_fig_bal_sheet_bwp = r27_fig_bal_sheet_bwp;
    }
    public BigDecimal getR27_amt_statement_adj() {
        return r27_amt_statement_adj;
    }
    public void setR27_amt_statement_adj(BigDecimal r27_amt_statement_adj) {
        this.r27_amt_statement_adj = r27_amt_statement_adj;
    }
    public BigDecimal getR27_amt_statement_adj_bwp() {
        return r27_amt_statement_adj_bwp;
    }
    public void setR27_amt_statement_adj_bwp(BigDecimal r27_amt_statement_adj_bwp) {
        this.r27_amt_statement_adj_bwp = r27_amt_statement_adj_bwp;
    }
    public BigDecimal getR27_net_amt() {
        return r27_net_amt;
    }
    public void setR27_net_amt(BigDecimal r27_net_amt) {
        this.r27_net_amt = r27_net_amt;
    }
    public BigDecimal getR27_net_amt_bwp() {
        return r27_net_amt_bwp;
    }
    public void setR27_net_amt_bwp(BigDecimal r27_net_amt_bwp) {
        this.r27_net_amt_bwp = r27_net_amt_bwp;
    }
    public BigDecimal getR27_bal_sub() {
        return r27_bal_sub;
    }
    public void setR27_bal_sub(BigDecimal r27_bal_sub) {
        this.r27_bal_sub = r27_bal_sub;
    }
    public BigDecimal getR27_bal_sub_bwp() {
        return r27_bal_sub_bwp;
    }
    public void setR27_bal_sub_bwp(BigDecimal r27_bal_sub_bwp) {
        this.r27_bal_sub_bwp = r27_bal_sub_bwp;
    }
    public BigDecimal getR27_bal_sub_diaries() {
        return r27_bal_sub_diaries;
    }
    public void setR27_bal_sub_diaries(BigDecimal r27_bal_sub_diaries) {
        this.r27_bal_sub_diaries = r27_bal_sub_diaries;
    }
    public BigDecimal getR27_bal_sub_diaries_bwp() {
        return r27_bal_sub_diaries_bwp;
    }
    public void setR27_bal_sub_diaries_bwp(BigDecimal r27_bal_sub_diaries_bwp) {
        this.r27_bal_sub_diaries_bwp = r27_bal_sub_diaries_bwp;
    }
    public String getR28_other_income() {
        return r28_other_income;
    }
    public void setR28_other_income(String r28_other_income) {
        this.r28_other_income = r28_other_income;
    }
    public BigDecimal getR28_fig_bal_sheet() {
        return r28_fig_bal_sheet;
    }
    public void setR28_fig_bal_sheet(BigDecimal r28_fig_bal_sheet) {
        this.r28_fig_bal_sheet = r28_fig_bal_sheet;
    }
    public BigDecimal getR28_fig_bal_sheet_bwp() {
        return r28_fig_bal_sheet_bwp;
    }
    public void setR28_fig_bal_sheet_bwp(BigDecimal r28_fig_bal_sheet_bwp) {
        this.r28_fig_bal_sheet_bwp = r28_fig_bal_sheet_bwp;
    }
    public BigDecimal getR28_amt_statement_adj() {
        return r28_amt_statement_adj;
    }
    public void setR28_amt_statement_adj(BigDecimal r28_amt_statement_adj) {
        this.r28_amt_statement_adj = r28_amt_statement_adj;
    }
    public BigDecimal getR28_amt_statement_adj_bwp() {
        return r28_amt_statement_adj_bwp;
    }
    public void setR28_amt_statement_adj_bwp(BigDecimal r28_amt_statement_adj_bwp) {
        this.r28_amt_statement_adj_bwp = r28_amt_statement_adj_bwp;
    }
    public BigDecimal getR28_net_amt() {
        return r28_net_amt;
    }
    public void setR28_net_amt(BigDecimal r28_net_amt) {
        this.r28_net_amt = r28_net_amt;
    }
    public BigDecimal getR28_net_amt_bwp() {
        return r28_net_amt_bwp;
    }
    public void setR28_net_amt_bwp(BigDecimal r28_net_amt_bwp) {
        this.r28_net_amt_bwp = r28_net_amt_bwp;
    }
    public BigDecimal getR28_bal_sub() {
        return r28_bal_sub;
    }
    public void setR28_bal_sub(BigDecimal r28_bal_sub) {
        this.r28_bal_sub = r28_bal_sub;
    }
    public BigDecimal getR28_bal_sub_bwp() {
        return r28_bal_sub_bwp;
    }
    public void setR28_bal_sub_bwp(BigDecimal r28_bal_sub_bwp) {
        this.r28_bal_sub_bwp = r28_bal_sub_bwp;
    }
    public BigDecimal getR28_bal_sub_diaries() {
        return r28_bal_sub_diaries;
    }
    public void setR28_bal_sub_diaries(BigDecimal r28_bal_sub_diaries) {
        this.r28_bal_sub_diaries = r28_bal_sub_diaries;
    }
    public BigDecimal getR28_bal_sub_diaries_bwp() {
        return r28_bal_sub_diaries_bwp;
    }
    public void setR28_bal_sub_diaries_bwp(BigDecimal r28_bal_sub_diaries_bwp) {
        this.r28_bal_sub_diaries_bwp = r28_bal_sub_diaries_bwp;
    }
    public String getR29_other_income() {
        return r29_other_income;
    }
    public void setR29_other_income(String r29_other_income) {
        this.r29_other_income = r29_other_income;
    }
    public BigDecimal getR29_fig_bal_sheet() {
        return r29_fig_bal_sheet;
    }
    public void setR29_fig_bal_sheet(BigDecimal r29_fig_bal_sheet) {
        this.r29_fig_bal_sheet = r29_fig_bal_sheet;
    }
    public BigDecimal getR29_fig_bal_sheet_bwp() {
        return r29_fig_bal_sheet_bwp;
    }
    public void setR29_fig_bal_sheet_bwp(BigDecimal r29_fig_bal_sheet_bwp) {
        this.r29_fig_bal_sheet_bwp = r29_fig_bal_sheet_bwp;
    }
    public BigDecimal getR29_amt_statement_adj() {
        return r29_amt_statement_adj;
    }
    public void setR29_amt_statement_adj(BigDecimal r29_amt_statement_adj) {
        this.r29_amt_statement_adj = r29_amt_statement_adj;
    }
    public BigDecimal getR29_amt_statement_adj_bwp() {
        return r29_amt_statement_adj_bwp;
    }
    public void setR29_amt_statement_adj_bwp(BigDecimal r29_amt_statement_adj_bwp) {
        this.r29_amt_statement_adj_bwp = r29_amt_statement_adj_bwp;
    }
    public BigDecimal getR29_net_amt() {
        return r29_net_amt;
    }
    public void setR29_net_amt(BigDecimal r29_net_amt) {
        this.r29_net_amt = r29_net_amt;
    }
    public BigDecimal getR29_net_amt_bwp() {
        return r29_net_amt_bwp;
    }
    public void setR29_net_amt_bwp(BigDecimal r29_net_amt_bwp) {
        this.r29_net_amt_bwp = r29_net_amt_bwp;
    }
    public BigDecimal getR29_bal_sub() {
        return r29_bal_sub;
    }
    public void setR29_bal_sub(BigDecimal r29_bal_sub) {
        this.r29_bal_sub = r29_bal_sub;
    }
    public BigDecimal getR29_bal_sub_bwp() {
        return r29_bal_sub_bwp;
    }
    public void setR29_bal_sub_bwp(BigDecimal r29_bal_sub_bwp) {
        this.r29_bal_sub_bwp = r29_bal_sub_bwp;
    }
    public BigDecimal getR29_bal_sub_diaries() {
        return r29_bal_sub_diaries;
    }
    public void setR29_bal_sub_diaries(BigDecimal r29_bal_sub_diaries) {
        this.r29_bal_sub_diaries = r29_bal_sub_diaries;
    }
    public BigDecimal getR29_bal_sub_diaries_bwp() {
        return r29_bal_sub_diaries_bwp;
    }
    public void setR29_bal_sub_diaries_bwp(BigDecimal r29_bal_sub_diaries_bwp) {
        this.r29_bal_sub_diaries_bwp = r29_bal_sub_diaries_bwp;
    }
    public String getR30_other_income() {
        return r30_other_income;
    }
    public void setR30_other_income(String r30_other_income) {
        this.r30_other_income = r30_other_income;
    }
    public BigDecimal getR30_fig_bal_sheet() {
        return r30_fig_bal_sheet;
    }
    public void setR30_fig_bal_sheet(BigDecimal r30_fig_bal_sheet) {
        this.r30_fig_bal_sheet = r30_fig_bal_sheet;
    }
    public BigDecimal getR30_fig_bal_sheet_bwp() {
        return r30_fig_bal_sheet_bwp;
    }
    public void setR30_fig_bal_sheet_bwp(BigDecimal r30_fig_bal_sheet_bwp) {
        this.r30_fig_bal_sheet_bwp = r30_fig_bal_sheet_bwp;
    }
    public BigDecimal getR30_amt_statement_adj() {
        return r30_amt_statement_adj;
    }
    public void setR30_amt_statement_adj(BigDecimal r30_amt_statement_adj) {
        this.r30_amt_statement_adj = r30_amt_statement_adj;
    }
    public BigDecimal getR30_amt_statement_adj_bwp() {
        return r30_amt_statement_adj_bwp;
    }
    public void setR30_amt_statement_adj_bwp(BigDecimal r30_amt_statement_adj_bwp) {
        this.r30_amt_statement_adj_bwp = r30_amt_statement_adj_bwp;
    }
    public BigDecimal getR30_net_amt() {
        return r30_net_amt;
    }
    public void setR30_net_amt(BigDecimal r30_net_amt) {
        this.r30_net_amt = r30_net_amt;
    }
    public BigDecimal getR30_net_amt_bwp() {
        return r30_net_amt_bwp;
    }
    public void setR30_net_amt_bwp(BigDecimal r30_net_amt_bwp) {
        this.r30_net_amt_bwp = r30_net_amt_bwp;
    }
    public BigDecimal getR30_bal_sub() {
        return r30_bal_sub;
    }
    public void setR30_bal_sub(BigDecimal r30_bal_sub) {
        this.r30_bal_sub = r30_bal_sub;
    }
    public BigDecimal getR30_bal_sub_bwp() {
        return r30_bal_sub_bwp;
    }
    public void setR30_bal_sub_bwp(BigDecimal r30_bal_sub_bwp) {
        this.r30_bal_sub_bwp = r30_bal_sub_bwp;
    }
    public BigDecimal getR30_bal_sub_diaries() {
        return r30_bal_sub_diaries;
    }
    public void setR30_bal_sub_diaries(BigDecimal r30_bal_sub_diaries) {
        this.r30_bal_sub_diaries = r30_bal_sub_diaries;
    }
    public BigDecimal getR30_bal_sub_diaries_bwp() {
        return r30_bal_sub_diaries_bwp;
    }
    public void setR30_bal_sub_diaries_bwp(BigDecimal r30_bal_sub_diaries_bwp) {
        this.r30_bal_sub_diaries_bwp = r30_bal_sub_diaries_bwp;
    }
    public String getR42_intrest_expended() {
        return r42_intrest_expended;
    }
    public void setR42_intrest_expended(String r42_intrest_expended) {
        this.r42_intrest_expended = r42_intrest_expended;
    }
    public BigDecimal getR42_fig_bal_sheet() {
        return r42_fig_bal_sheet;
    }
    public void setR42_fig_bal_sheet(BigDecimal r42_fig_bal_sheet) {
        this.r42_fig_bal_sheet = r42_fig_bal_sheet;
    }
    public BigDecimal getR42_fig_bal_sheet_bwp() {
        return r42_fig_bal_sheet_bwp;
    }
    public void setR42_fig_bal_sheet_bwp(BigDecimal r42_fig_bal_sheet_bwp) {
        this.r42_fig_bal_sheet_bwp = r42_fig_bal_sheet_bwp;
    }
    public BigDecimal getR42_amt_statement_adj() {
        return r42_amt_statement_adj;
    }
    public void setR42_amt_statement_adj(BigDecimal r42_amt_statement_adj) {
        this.r42_amt_statement_adj = r42_amt_statement_adj;
    }
    public BigDecimal getR42_amt_statement_adj_bwp() {
        return r42_amt_statement_adj_bwp;
    }
    public void setR42_amt_statement_adj_bwp(BigDecimal r42_amt_statement_adj_bwp) {
        this.r42_amt_statement_adj_bwp = r42_amt_statement_adj_bwp;
    }
    public BigDecimal getR42_net_amt() {
        return r42_net_amt;
    }
    public void setR42_net_amt(BigDecimal r42_net_amt) {
        this.r42_net_amt = r42_net_amt;
    }
    public BigDecimal getR42_net_amt_bwp() {
        return r42_net_amt_bwp;
    }
    public void setR42_net_amt_bwp(BigDecimal r42_net_amt_bwp) {
        this.r42_net_amt_bwp = r42_net_amt_bwp;
    }
    public BigDecimal getR42_bal_sub() {
        return r42_bal_sub;
    }
    public void setR42_bal_sub(BigDecimal r42_bal_sub) {
        this.r42_bal_sub = r42_bal_sub;
    }
    public BigDecimal getR42_bal_sub_bwp() {
        return r42_bal_sub_bwp;
    }
    public void setR42_bal_sub_bwp(BigDecimal r42_bal_sub_bwp) {
        this.r42_bal_sub_bwp = r42_bal_sub_bwp;
    }
    public BigDecimal getR42_bal_sub_diaries() {
        return r42_bal_sub_diaries;
    }
    public void setR42_bal_sub_diaries(BigDecimal r42_bal_sub_diaries) {
        this.r42_bal_sub_diaries = r42_bal_sub_diaries;
    }
    public BigDecimal getR42_bal_sub_diaries_bwp() {
        return r42_bal_sub_diaries_bwp;
    }
    public void setR42_bal_sub_diaries_bwp(BigDecimal r42_bal_sub_diaries_bwp) {
        this.r42_bal_sub_diaries_bwp = r42_bal_sub_diaries_bwp;
    }
    public String getR54_operating_expenses() {
        return r54_operating_expenses;
    }
    public void setR54_operating_expenses(String r54_operating_expenses) {
        this.r54_operating_expenses = r54_operating_expenses;
    }
    public BigDecimal getR54_fig_bal_sheet() {
        return r54_fig_bal_sheet;
    }
    public void setR54_fig_bal_sheet(BigDecimal r54_fig_bal_sheet) {
        this.r54_fig_bal_sheet = r54_fig_bal_sheet;
    }
    public BigDecimal getR54_fig_bal_sheet_bwp() {
        return r54_fig_bal_sheet_bwp;
    }
    public void setR54_fig_bal_sheet_bwp(BigDecimal r54_fig_bal_sheet_bwp) {
        this.r54_fig_bal_sheet_bwp = r54_fig_bal_sheet_bwp;
    }
    public BigDecimal getR54_amt_statement_adj() {
        return r54_amt_statement_adj;
    }
    public void setR54_amt_statement_adj(BigDecimal r54_amt_statement_adj) {
        this.r54_amt_statement_adj = r54_amt_statement_adj;
    }
    public BigDecimal getR54_amt_statement_adj_bwp() {
        return r54_amt_statement_adj_bwp;
    }
    public void setR54_amt_statement_adj_bwp(BigDecimal r54_amt_statement_adj_bwp) {
        this.r54_amt_statement_adj_bwp = r54_amt_statement_adj_bwp;
    }
    public BigDecimal getR54_net_amt() {
        return r54_net_amt;
    }
    public void setR54_net_amt(BigDecimal r54_net_amt) {
        this.r54_net_amt = r54_net_amt;
    }
    public BigDecimal getR54_net_amt_bwp() {
        return r54_net_amt_bwp;
    }
    public void setR54_net_amt_bwp(BigDecimal r54_net_amt_bwp) {
        this.r54_net_amt_bwp = r54_net_amt_bwp;
    }
    public BigDecimal getR54_bal_sub() {
        return r54_bal_sub;
    }
    public void setR54_bal_sub(BigDecimal r54_bal_sub) {
        this.r54_bal_sub = r54_bal_sub;
    }
    public BigDecimal getR54_bal_sub_bwp() {
        return r54_bal_sub_bwp;
    }
    public void setR54_bal_sub_bwp(BigDecimal r54_bal_sub_bwp) {
        this.r54_bal_sub_bwp = r54_bal_sub_bwp;
    }
    public BigDecimal getR54_bal_sub_diaries() {
        return r54_bal_sub_diaries;
    }
    public void setR54_bal_sub_diaries(BigDecimal r54_bal_sub_diaries) {
        this.r54_bal_sub_diaries = r54_bal_sub_diaries;
    }
    public BigDecimal getR54_bal_sub_diaries_bwp() {
        return r54_bal_sub_diaries_bwp;
    }
    public void setR54_bal_sub_diaries_bwp(BigDecimal r54_bal_sub_diaries_bwp) {
        this.r54_bal_sub_diaries_bwp = r54_bal_sub_diaries_bwp;
    }
    public String getR61_operating_expenses() {
        return r61_operating_expenses;
    }
    public void setR61_operating_expenses(String r61_operating_expenses) {
        this.r61_operating_expenses = r61_operating_expenses;
    }
    public BigDecimal getR61_fig_bal_sheet() {
        return r61_fig_bal_sheet;
    }
    public void setR61_fig_bal_sheet(BigDecimal r61_fig_bal_sheet) {
        this.r61_fig_bal_sheet = r61_fig_bal_sheet;
    }
    public BigDecimal getR61_fig_bal_sheet_bwp() {
        return r61_fig_bal_sheet_bwp;
    }
    public void setR61_fig_bal_sheet_bwp(BigDecimal r61_fig_bal_sheet_bwp) {
        this.r61_fig_bal_sheet_bwp = r61_fig_bal_sheet_bwp;
    }
    public BigDecimal getR61_amt_statement_adj() {
        return r61_amt_statement_adj;
    }
    public void setR61_amt_statement_adj(BigDecimal r61_amt_statement_adj) {
        this.r61_amt_statement_adj = r61_amt_statement_adj;
    }
    public BigDecimal getR61_amt_statement_adj_bwp() {
        return r61_amt_statement_adj_bwp;
    }
    public void setR61_amt_statement_adj_bwp(BigDecimal r61_amt_statement_adj_bwp) {
        this.r61_amt_statement_adj_bwp = r61_amt_statement_adj_bwp;
    }
    public BigDecimal getR61_net_amt() {
        return r61_net_amt;
    }
    public void setR61_net_amt(BigDecimal r61_net_amt) {
        this.r61_net_amt = r61_net_amt;
    }
    public BigDecimal getR61_net_amt_bwp() {
        return r61_net_amt_bwp;
    }
    public void setR61_net_amt_bwp(BigDecimal r61_net_amt_bwp) {
        this.r61_net_amt_bwp = r61_net_amt_bwp;
    }
    public BigDecimal getR61_bal_sub() {
        return r61_bal_sub;
    }
    public void setR61_bal_sub(BigDecimal r61_bal_sub) {
        this.r61_bal_sub = r61_bal_sub;
    }
    public BigDecimal getR61_bal_sub_bwp() {
        return r61_bal_sub_bwp;
    }
    public void setR61_bal_sub_bwp(BigDecimal r61_bal_sub_bwp) {
        this.r61_bal_sub_bwp = r61_bal_sub_bwp;
    }
    public BigDecimal getR61_bal_sub_diaries() {
        return r61_bal_sub_diaries;
    }
    public void setR61_bal_sub_diaries(BigDecimal r61_bal_sub_diaries) {
        this.r61_bal_sub_diaries = r61_bal_sub_diaries;
    }
    public BigDecimal getR61_bal_sub_diaries_bwp() {
        return r61_bal_sub_diaries_bwp;
    }
    public void setR61_bal_sub_diaries_bwp(BigDecimal r61_bal_sub_diaries_bwp) {
        this.r61_bal_sub_diaries_bwp = r61_bal_sub_diaries_bwp;
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
