package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;

@Entity
@Table(name = "BRRS_PL_SCHS_SUMMARYTABLE")
public class PL_SCHS_Summary_Entity {

    // ================= R9 =================
    @Column(name = "R9_INTREST_DIV")
    private String r9_intrest_div;

    @Column(name = "R9_FIG_BAL_SHEET")
    private BigDecimal r9_fig_bal_sheet;

    @Column(name = "R9_FIG_BAL_SHEET_BWP")
    private BigDecimal r9_fig_bal_sheet_bwp;

    @Column(name = "R9_AMT_STATEMENT_ADJ")
    private BigDecimal r9_amt_statement_adj;

    @Column(name = "R9_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r9_amt_statement_adj_bwp;

    @Column(name = "R9_NET_AMT")
    private BigDecimal r9_net_amt;

    @Column(name = "R9_NET_AMT_BWP")
    private BigDecimal r9_net_amt_bwp;

    @Column(name = "R9_BAL_SUB")
    private BigDecimal r9_bal_sub;

    @Column(name = "R9_BAL_SUB_BWP")
    private BigDecimal r9_bal_sub_bwp;

    @Column(name = "R9_BAL_SUB_DIARIES")
    private BigDecimal r9_bal_sub_diaries;

    @Column(name = "R9_BAL_SUB_DIARIES_BWP")
    private BigDecimal r9_bal_sub_diaries_bwp;

    // ================= R10 =================
    @Column(name = "R10_INTREST_DIV")
    private String r10_intrest_div;

    @Column(name = "R10_FIG_BAL_SHEET")
    private BigDecimal r10_fig_bal_sheet;

    @Column(name = "R10_FIG_BAL_SHEET_BWP")
    private BigDecimal r10_fig_bal_sheet_bwp;

    @Column(name = "R10_AMT_STATEMENT_ADJ")
    private BigDecimal r10_amt_statement_adj;

    @Column(name = "R10_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r10_amt_statement_adj_bwp;

    @Column(name = "R10_NET_AMT")
    private BigDecimal r10_net_amt;

    @Column(name = "R10_NET_AMT_BWP")
    private BigDecimal r10_net_amt_bwp;

    @Column(name = "R10_BAL_SUB")
    private BigDecimal r10_bal_sub;

    @Column(name = "R10_BAL_SUB_BWP")
    private BigDecimal r10_bal_sub_bwp;

    @Column(name = "R10_BAL_SUB_DIARIES")
    private BigDecimal r10_bal_sub_diaries;

    @Column(name = "R10_BAL_SUB_DIARIES_BWP")
    private BigDecimal r10_bal_sub_diaries_bwp;

    // ================= R11 =================
    @Column(name = "R11_INTREST_DIV")
    private String r11_intrest_div;

    @Column(name = "R11_FIG_BAL_SHEET")
    private BigDecimal r11_fig_bal_sheet;

    @Column(name = "R11_FIG_BAL_SHEET_BWP")
    private BigDecimal r11_fig_bal_sheet_bwp;

    @Column(name = "R11_AMT_STATEMENT_ADJ")
    private BigDecimal r11_amt_statement_adj;

    @Column(name = "R11_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r11_amt_statement_adj_bwp;

    @Column(name = "R11_NET_AMT")
    private BigDecimal r11_net_amt;

    @Column(name = "R11_NET_AMT_BWP")
    private BigDecimal r11_net_amt_bwp;

    @Column(name = "R11_BAL_SUB")
    private BigDecimal r11_bal_sub;

    @Column(name = "R11_BAL_SUB_BWP")
    private BigDecimal r11_bal_sub_bwp;

    @Column(name = "R11_BAL_SUB_DIARIES")
    private BigDecimal r11_bal_sub_diaries;

    @Column(name = "R11_BAL_SUB_DIARIES_BWP")
    private BigDecimal r11_bal_sub_diaries_bwp;

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

    // ================= R13 =================
    @Column(name = "R13_INTREST_DIV")
    private String r13_intrest_div;

    @Column(name = "R13_FIG_BAL_SHEET")
    private BigDecimal r13_fig_bal_sheet;

    @Column(name = "R13_FIG_BAL_SHEET_BWP")
    private BigDecimal r13_fig_bal_sheet_bwp;

    @Column(name = "R13_AMT_STATEMENT_ADJ")
    private BigDecimal r13_amt_statement_adj;

    @Column(name = "R13_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r13_amt_statement_adj_bwp;

    @Column(name = "R13_NET_AMT")
    private BigDecimal r13_net_amt;

    @Column(name = "R13_NET_AMT_BWP")
    private BigDecimal r13_net_amt_bwp;

    @Column(name = "R13_BAL_SUB")
    private BigDecimal r13_bal_sub;

    @Column(name = "R13_BAL_SUB_BWP")
    private BigDecimal r13_bal_sub_bwp;

    @Column(name = "R13_BAL_SUB_DIARIES")
    private BigDecimal r13_bal_sub_diaries;

    @Column(name = "R13_BAL_SUB_DIARIES_BWP")
    private BigDecimal r13_bal_sub_diaries_bwp;

    // ================= R17 =================
    @Column(name = "R17_OTHER_INCOME")
    private String r17_other_income;

    @Column(name = "R17_FIG_BAL_SHEET")
    private BigDecimal r17_fig_bal_sheet;

    @Column(name = "R17_FIG_BAL_SHEET_BWP")
    private BigDecimal r17_fig_bal_sheet_bwp;

    @Column(name = "R17_AMT_STATEMENT_ADJ")
    private BigDecimal r17_amt_statement_adj;

    @Column(name = "R17_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r17_amt_statement_adj_bwp;

    @Column(name = "R17_NET_AMT")
    private BigDecimal r17_net_amt;

    @Column(name = "R17_NET_AMT_BWP")
    private BigDecimal r17_net_amt_bwp;

    @Column(name = "R17_BAL_SUB")
    private BigDecimal r17_bal_sub;

    @Column(name = "R17_BAL_SUB_BWP")
    private BigDecimal r17_bal_sub_bwp;

    @Column(name = "R17_BAL_SUB_DIARIES")
    private BigDecimal r17_bal_sub_diaries;

    @Column(name = "R17_BAL_SUB_DIARIES_BWP")
    private BigDecimal r17_bal_sub_diaries_bwp;

    // ================= R18 =================
    @Column(name = "R18_OTHER_INCOME")
    private String r18_other_income;

    @Column(name = "R18_FIG_BAL_SHEET")
    private BigDecimal r18_fig_bal_sheet;

    @Column(name = "R18_FIG_BAL_SHEET_BWP")
    private BigDecimal r18_fig_bal_sheet_bwp;

    @Column(name = "R18_AMT_STATEMENT_ADJ")
    private BigDecimal r18_amt_statement_adj;

    @Column(name = "R18_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r18_amt_statement_adj_bwp;

    @Column(name = "R18_NET_AMT")
    private BigDecimal r18_net_amt;

    @Column(name = "R18_NET_AMT_BWP")
    private BigDecimal r18_net_amt_bwp;

    @Column(name = "R18_BAL_SUB")
    private BigDecimal r18_bal_sub;

    @Column(name = "R18_BAL_SUB_BWP")
    private BigDecimal r18_bal_sub_bwp;

    @Column(name = "R18_BAL_SUB_DIARIES_BWP")
    private BigDecimal r18_bal_sub_diaries_bwp;

    // ================= R19 =================
    @Column(name = "R19_OTHER_INCOME")
    private String r19_other_income;

    @Column(name = "R19_FIG_BAL_SHEET")
    private BigDecimal r19_fig_bal_sheet;

    @Column(name = "R19_FIG_BAL_SHEET_BWP")
    private BigDecimal r19_fig_bal_sheet_bwp;

    @Column(name = "R19_AMT_STATEMENT_ADJ")
    private BigDecimal r19_amt_statement_adj;

    @Column(name = "R19_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r19_amt_statement_adj_bwp;

    @Column(name = "R19_NET_AMT")
    private BigDecimal r19_net_amt;

    @Column(name = "R19_NET_AMT_BWP")
    private BigDecimal r19_net_amt_bwp;

    @Column(name = "R19_BAL_SUB")
    private BigDecimal r19_bal_sub;

    @Column(name = "R19_BAL_SUB_BWP")
    private BigDecimal r19_bal_sub_bwp;

    @Column(name = "R19_BAL_SUB_DIARIES_BWP")
    private BigDecimal r19_bal_sub_diaries_bwp;

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

    @Column(name = "R30_BAL_SUB_DIARIES_BWP")
    private BigDecimal r30_bal_sub_diaries_bwp;

    // ================= R31 =================
    @Column(name = "R31_OTHER_INCOME")
    private String r31_other_income;

    @Column(name = "R31_FIG_BAL_SHEET")
    private BigDecimal r31_fig_bal_sheet;

    @Column(name = "R31_FIG_BAL_SHEET_BWP")
    private BigDecimal r31_fig_bal_sheet_bwp;

    @Column(name = "R31_AMT_STATEMENT_ADJ")
    private BigDecimal r31_amt_statement_adj;

    @Column(name = "R31_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r31_amt_statement_adj_bwp;

    @Column(name = "R31_NET_AMT")
    private BigDecimal r31_net_amt;

    @Column(name = "R31_NET_AMT_BWP")
    private BigDecimal r31_net_amt_bwp;

    @Column(name = "R31_BAL_SUB")
    private BigDecimal r31_bal_sub;

    @Column(name = "R31_BAL_SUB_BWP")
    private BigDecimal r31_bal_sub_bwp;

    @Column(name = "R31_BAL_SUB_DIARIES_BWP")
    private BigDecimal r31_bal_sub_diaries_bwp;

    // ================= R40 =================
    @Column(name = "R40_INTREST_EXPENDED")
    private String r40_intrest_expended;

    @Column(name = "R40_FIG_BAL_SHEET")
    private BigDecimal r40_fig_bal_sheet;

    @Column(name = "R40_FIG_BAL_SHEET_BWP")
    private BigDecimal r40_fig_bal_sheet_bwp;

    @Column(name = "R40_AMT_STATEMENT_ADJ")
    private BigDecimal r40_amt_statement_adj;

    @Column(name = "R40_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r40_amt_statement_adj_bwp;

    @Column(name = "R40_NET_AMT")
    private BigDecimal r40_net_amt;

    @Column(name = "R40_NET_AMT_BWP")
    private BigDecimal r40_net_amt_bwp;

    @Column(name = "R40_BAL_SUB")
    private BigDecimal r40_bal_sub;

    @Column(name = "R40_BAL_SUB_BWP")
    private BigDecimal r40_bal_sub_bwp;

    @Column(name = "R40_BAL_SUB_DIARIES_BWP")
    private BigDecimal r40_bal_sub_diaries_bwp;

    // ================= R41 =================
    @Column(name = "R41_INTREST_EXPENDED")
    private String r41_intrest_expended;

    @Column(name = "R41_FIG_BAL_SHEET")
    private BigDecimal r41_fig_bal_sheet;

    @Column(name = "R41_FIG_BAL_SHEET_BWP")
    private BigDecimal r41_fig_bal_sheet_bwp;

    @Column(name = "R41_AMT_STATEMENT_ADJ")
    private BigDecimal r41_amt_statement_adj;

    @Column(name = "R41_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r41_amt_statement_adj_bwp;

    @Column(name = "R41_NET_AMT")
    private BigDecimal r41_net_amt;

    @Column(name = "R41_NET_AMT_BWP")
    private BigDecimal r41_net_amt_bwp;

    @Column(name = "R41_BAL_SUB")
    private BigDecimal r41_bal_sub;

    @Column(name = "R41_BAL_SUB_BWP")
    private BigDecimal r41_bal_sub_bwp;

    @Column(name = "R41_BAL_SUB_DIARIES_BWP")
    private BigDecimal r41_bal_sub_diaries_bwp;

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

    @Column(name = "R42_BAL_SUB_DIARIES_BWP")
    private BigDecimal r42_bal_sub_diaries_bwp;

    // ================= R43 =================
    @Column(name = "R43_INTREST_EXPENDED")
    private String r43_intrest_expended;

    @Column(name = "R43_FIG_BAL_SHEET")
    private BigDecimal r43_fig_bal_sheet;

    @Column(name = "R43_FIG_BAL_SHEET_BWP")
    private BigDecimal r43_fig_bal_sheet_bwp;

    @Column(name = "R43_AMT_STATEMENT_ADJ")
    private BigDecimal r43_amt_statement_adj;

    @Column(name = "R43_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r43_amt_statement_adj_bwp;

    @Column(name = "R43_NET_AMT")
    private BigDecimal r43_net_amt;

    @Column(name = "R43_NET_AMT_BWP")
    private BigDecimal r43_net_amt_bwp;

    @Column(name = "R43_BAL_SUB")
    private BigDecimal r43_bal_sub;

    @Column(name = "R43_BAL_SUB_BWP")
    private BigDecimal r43_bal_sub_bwp;

    @Column(name = "R43_BAL_SUB_DIARIES_BWP")
    private BigDecimal r43_bal_sub_diaries_bwp;

    // ================= R48 =================
    @Column(name = "R48_OPERATING_EXPENSES")
    private String r48_operating_expenses;

    @Column(name = "R48_FIG_BAL_SHEET")
    private BigDecimal r48_fig_bal_sheet;

    @Column(name = "R48_FIG_BAL_SHEET_BWP")
    private BigDecimal r48_fig_bal_sheet_bwp;

    @Column(name = "R48_AMT_STATEMENT_ADJ")
    private BigDecimal r48_amt_statement_adj;

    @Column(name = "R48_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r48_amt_statement_adj_bwp;

    @Column(name = "R48_NET_AMT")
    private BigDecimal r48_net_amt;

    @Column(name = "R48_NET_AMT_BWP")
    private BigDecimal r48_net_amt_bwp;

    @Column(name = "R48_BAL_SUB")
    private BigDecimal r48_bal_sub;

    @Column(name = "R48_BAL_SUB_BWP")
    private BigDecimal r48_bal_sub_bwp;


    @Column(name = "R48_BAL_SUB_DIARIES_BWP")
    private BigDecimal r48_bal_sub_diaries_bwp;

    // ================= R49 =================
    @Column(name = "R49_OPERATING_EXPENSES")
    private String r49_operating_expenses;

    @Column(name = "R49_FIG_BAL_SHEET")
    private BigDecimal r49_fig_bal_sheet;

    @Column(name = "R49_FIG_BAL_SHEET_BWP")
    private BigDecimal r49_fig_bal_sheet_bwp;

    @Column(name = "R49_AMT_STATEMENT_ADJ")
    private BigDecimal r49_amt_statement_adj;

    @Column(name = "R49_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r49_amt_statement_adj_bwp;

    @Column(name = "R49_NET_AMT")
    private BigDecimal r49_net_amt;

    @Column(name = "R49_NET_AMT_BWP")
    private BigDecimal r49_net_amt_bwp;

    @Column(name = "R49_BAL_SUB")
    private BigDecimal r49_bal_sub;

    @Column(name = "R49_BAL_SUB_BWP")
    private BigDecimal r49_bal_sub_bwp;

    @Column(name = "R49_BAL_SUB_DIARIES_BWP")
    private BigDecimal r49_bal_sub_diaries_bwp;

    // ================= R50 =================
    @Column(name = "R50_OPERATING_EXPENSES")
    private String r50_operating_expenses;

    @Column(name = "R50_FIG_BAL_SHEET")
    private BigDecimal r50_fig_bal_sheet;

    @Column(name = "R50_FIG_BAL_SHEET_BWP")
    private BigDecimal r50_fig_bal_sheet_bwp;

    @Column(name = "R50_AMT_STATEMENT_ADJ")
    private BigDecimal r50_amt_statement_adj;

    @Column(name = "R50_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r50_amt_statement_adj_bwp;

    @Column(name = "R50_NET_AMT")
    private BigDecimal r50_net_amt;

    @Column(name = "R50_NET_AMT_BWP")
    private BigDecimal r50_net_amt_bwp;

    @Column(name = "R50_BAL_SUB")
    private BigDecimal r50_bal_sub;

    @Column(name = "R50_BAL_SUB_BWP")
    private BigDecimal r50_bal_sub_bwp;

    @Column(name = "R50_BAL_SUB_DIARIES_BWP")
    private BigDecimal r50_bal_sub_diaries_bwp;

    // ================= R51 =================
    @Column(name = "R51_OPERATING_EXPENSES")
    private String r51_operating_expenses;

    @Column(name = "R51_FIG_BAL_SHEET")
    private BigDecimal r51_fig_bal_sheet;

    @Column(name = "R51_FIG_BAL_SHEET_BWP")
    private BigDecimal r51_fig_bal_sheet_bwp;

    @Column(name = "R51_AMT_STATEMENT_ADJ")
    private BigDecimal r51_amt_statement_adj;

    @Column(name = "R51_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r51_amt_statement_adj_bwp;

    @Column(name = "R51_NET_AMT")
    private BigDecimal r51_net_amt;

    @Column(name = "R51_NET_AMT_BWP")
    private BigDecimal r51_net_amt_bwp;

    @Column(name = "R51_BAL_SUB")
    private BigDecimal r51_bal_sub;

    @Column(name = "R51_BAL_SUB_BWP")
    private BigDecimal r51_bal_sub_bwp;

    @Column(name = "R51_BAL_SUB_DIARIES_BWP")
    private BigDecimal r51_bal_sub_diaries_bwp;

    @Column(name = "R52_OPERATING_EXPENSES")
    private String r52_operating_expenses;

    @Column(name = "R52_FIG_BAL_SHEET")
    private BigDecimal r52_fig_bal_sheet;

    @Column(name = "R52_FIG_BAL_SHEET_BWP")
    private BigDecimal r52_fig_bal_sheet_bwp;

    @Column(name = "R52_AMT_STATEMENT_ADJ")
    private BigDecimal r52_amt_statement_adj;

    @Column(name = "R52_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r52_amt_statement_adj_bwp;

    @Column(name = "R52_NET_AMT")
    private BigDecimal r52_net_amt;

    @Column(name = "R52_NET_AMT_BWP")
    private BigDecimal r52_net_amt_bwp;

    @Column(name = "R52_BAL_SUB")
    private BigDecimal r52_bal_sub;

    @Column(name = "R52_BAL_SUB_BWP")
    private BigDecimal r52_bal_sub_bwp;

    @Column(name = "R52_BAL_SUB_DIARIES_BWP")
    private BigDecimal r52_bal_sub_diaries_bwp;

    @Column(name = "R53_OPERATING_EXPENSES")
    private String r53_operating_expenses;

    @Column(name = "R53_FIG_BAL_SHEET")
    private BigDecimal r53_fig_bal_sheet;

    @Column(name = "R53_FIG_BAL_SHEET_BWP")
    private BigDecimal r53_fig_bal_sheet_bwp;

    @Column(name = "R53_AMT_STATEMENT_ADJ")
    private BigDecimal r53_amt_statement_adj;

    @Column(name = "R53_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r53_amt_statement_adj_bwp;

    @Column(name = "R53_NET_AMT")
    private BigDecimal r53_net_amt;

    @Column(name = "R53_NET_AMT_BWP")
    private BigDecimal r53_net_amt_bwp;

    @Column(name = "R53_BAL_SUB")
    private BigDecimal r53_bal_sub;

    @Column(name = "R53_BAL_SUB_BWP")
    private BigDecimal r53_bal_sub_bwp;

    @Column(name = "R53_BAL_SUB_DIARIES_BWP")
    private BigDecimal r53_bal_sub_diaries_bwp;

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

    @Column(name = "R54_BAL_SUB_DIARIES_BWP")
    private BigDecimal r54_bal_sub_diaries_bwp;

    @Column(name = "R55_OPERATING_EXPENSES")
    private String r55_operating_expenses;

    @Column(name = "R55_FIG_BAL_SHEET")
    private BigDecimal r55_fig_bal_sheet;

    @Column(name = "R55_FIG_BAL_SHEET_BWP")
    private BigDecimal r55_fig_bal_sheet_bwp;

    @Column(name = "R55_AMT_STATEMENT_ADJ")
    private BigDecimal r55_amt_statement_adj;

    @Column(name = "R55_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r55_amt_statement_adj_bwp;

    @Column(name = "R55_NET_AMT")
    private BigDecimal r55_net_amt;

    @Column(name = "R55_NET_AMT_BWP")
    private BigDecimal r55_net_amt_bwp;

    @Column(name = "R55_BAL_SUB")
    private BigDecimal r55_bal_sub;

    @Column(name = "R55_BAL_SUB_BWP")
    private BigDecimal r55_bal_sub_bwp;

    @Column(name = "R55_BAL_SUB_DIARIES_BWP")
    private BigDecimal r55_bal_sub_diaries_bwp;

    @Column(name = "R56_OPERATING_EXPENSES")
    private String r56_operating_expenses;

    @Column(name = "R56_FIG_BAL_SHEET")
    private BigDecimal r56_fig_bal_sheet;

    @Column(name = "R56_FIG_BAL_SHEET_BWP")
    private BigDecimal r56_fig_bal_sheet_bwp;

    @Column(name = "R56_AMT_STATEMENT_ADJ")
    private BigDecimal r56_amt_statement_adj;

    @Column(name = "R56_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r56_amt_statement_adj_bwp;

    @Column(name = "R56_NET_AMT")
    private BigDecimal r56_net_amt;

    @Column(name = "R56_NET_AMT_BWP")
    private BigDecimal r56_net_amt_bwp;

    @Column(name = "R56_BAL_SUB")
    private BigDecimal r56_bal_sub;

    @Column(name = "R56_BAL_SUB_BWP")
    private BigDecimal r56_bal_sub_bwp;

    @Column(name = "R56_BAL_SUB_DIARIES_BWP")
    private BigDecimal r56_bal_sub_diaries_bwp;

    @Column(name = "R57_OPERATING_EXPENSES")
    private String r57_operating_expenses;

    @Column(name = "R57_FIG_BAL_SHEET")
    private BigDecimal r57_fig_bal_sheet;

    @Column(name = "R57_FIG_BAL_SHEET_BWP")
    private BigDecimal r57_fig_bal_sheet_bwp;

    @Column(name = "R57_AMT_STATEMENT_ADJ")
    private BigDecimal r57_amt_statement_adj;

    @Column(name = "R57_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r57_amt_statement_adj_bwp;

    @Column(name = "R57_NET_AMT")
    private BigDecimal r57_net_amt;

    @Column(name = "R57_NET_AMT_BWP")
    private BigDecimal r57_net_amt_bwp;

    @Column(name = "R57_BAL_SUB")
    private BigDecimal r57_bal_sub;

    @Column(name = "R57_BAL_SUB_BWP")
    private BigDecimal r57_bal_sub_bwp;

    @Column(name = "R57_BAL_SUB_DIARIES_BWP")
    private BigDecimal r57_bal_sub_diaries_bwp;

    @Column(name = "R58_OPERATING_EXPENSES")
    private String r58_operating_expenses;

    @Column(name = "R58_FIG_BAL_SHEET")
    private BigDecimal r58_fig_bal_sheet;

    @Column(name = "R58_FIG_BAL_SHEET_BWP")
    private BigDecimal r58_fig_bal_sheet_bwp;

    @Column(name = "R58_AMT_STATEMENT_ADJ")
    private BigDecimal r58_amt_statement_adj;

    @Column(name = "R58_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r58_amt_statement_adj_bwp;

    @Column(name = "R58_NET_AMT")
    private BigDecimal r58_net_amt;

    @Column(name = "R58_NET_AMT_BWP")
    private BigDecimal r58_net_amt_bwp;

    @Column(name = "R58_BAL_SUB")
    private BigDecimal r58_bal_sub;

    @Column(name = "R58_BAL_SUB_BWP")
    private BigDecimal r58_bal_sub_bwp;

    @Column(name = "R58_BAL_SUB_DIARIES_BWP")
    private BigDecimal r58_bal_sub_diaries_bwp;

    @Column(name = "R59_OPERATING_EXPENSES")
    private String r59_operating_expenses;

    @Column(name = "R59_FIG_BAL_SHEET")
    private BigDecimal r59_fig_bal_sheet;

    @Column(name = "R59_FIG_BAL_SHEET_BWP")
    private BigDecimal r59_fig_bal_sheet_bwp;

    @Column(name = "R59_AMT_STATEMENT_ADJ")
    private BigDecimal r59_amt_statement_adj;

    @Column(name = "R59_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r59_amt_statement_adj_bwp;

    @Column(name = "R59_NET_AMT")
    private BigDecimal r59_net_amt;

    @Column(name = "R59_NET_AMT_BWP")
    private BigDecimal r59_net_amt_bwp;

    @Column(name = "R59_BAL_SUB")
    private BigDecimal r59_bal_sub;

    @Column(name = "R59_BAL_SUB_BWP")
    private BigDecimal r59_bal_sub_bwp;

    @Column(name = "R59_BAL_SUB_DIARIES_BWP")
    private BigDecimal r59_bal_sub_diaries_bwp;

    @Column(name = "R60_OPERATING_EXPENSES")
    private String r60_operating_expenses;

    @Column(name = "R60_FIG_BAL_SHEET")
    private BigDecimal r60_fig_bal_sheet;

    @Column(name = "R60_FIG_BAL_SHEET_BWP")
    private BigDecimal r60_fig_bal_sheet_bwp;

    @Column(name = "R60_AMT_STATEMENT_ADJ")
    private BigDecimal r60_amt_statement_adj;

    @Column(name = "R60_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r60_amt_statement_adj_bwp;

    @Column(name = "R60_NET_AMT")
    private BigDecimal r60_net_amt;

    @Column(name = "R60_NET_AMT_BWP")
    private BigDecimal r60_net_amt_bwp;

    @Column(name = "R60_BAL_SUB")
    private BigDecimal r60_bal_sub;

    @Column(name = "R60_BAL_SUB_BWP")
    private BigDecimal r60_bal_sub_bwp;

    @Column(name = "R60_BAL_SUB_DIARIES_BWP")
    private BigDecimal r60_bal_sub_diaries_bwp;

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

    @Column(name = "R61_BAL_SUB_DIARIES_BWP")
    private BigDecimal r61_bal_sub_diaries_bwp;

    @Column(name = "R62_OPERATING_EXPENSES")
    private String r62_operating_expenses;

    @Column(name = "R62_FIG_BAL_SHEET")
    private BigDecimal r62_fig_bal_sheet;

    @Column(name = "R62_FIG_BAL_SHEET_BWP")
    private BigDecimal r62_fig_bal_sheet_bwp;

    @Column(name = "R62_AMT_STATEMENT_ADJ")
    private BigDecimal r62_amt_statement_adj;

    @Column(name = "R62_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r62_amt_statement_adj_bwp;

    @Column(name = "R62_NET_AMT")
    private BigDecimal r62_net_amt;

    @Column(name = "R62_NET_AMT_BWP")
    private BigDecimal r62_net_amt_bwp;

    @Column(name = "R62_BAL_SUB")
    private BigDecimal r62_bal_sub;

    @Column(name = "R62_BAL_SUB_BWP")
    private BigDecimal r62_bal_sub_bwp;

    @Column(name = "R62_BAL_SUB_DIARIES_BWP")
    private BigDecimal r62_bal_sub_diaries_bwp;

    @Column(name = "R63_OPERATING_EXPENSES")
    private String r63_operating_expenses;

    @Column(name = "R63_FIG_BAL_SHEET")
    private BigDecimal r63_fig_bal_sheet;

    @Column(name = "R63_FIG_BAL_SHEET_BWP")
    private BigDecimal r63_fig_bal_sheet_bwp;

    @Column(name = "R63_AMT_STATEMENT_ADJ")
    private BigDecimal r63_amt_statement_adj;

    @Column(name = "R63_AMT_STATEMENT_ADJ_BWP")
    private BigDecimal r63_amt_statement_adj_bwp;

    @Column(name = "R63_NET_AMT")
    private BigDecimal r63_net_amt;

    @Column(name = "R63_NET_AMT_BWP")
    private BigDecimal r63_net_amt_bwp;

    @Column(name = "R63_BAL_SUB")
    private BigDecimal r63_bal_sub;

    @Column(name = "R63_BAL_SUB_BWP")
    private BigDecimal r63_bal_sub_bwp;

    @Column(name = "R63_BAL_SUB_DIARIES_BWP")
    private BigDecimal r63_bal_sub_diaries_bwp;
    @Id
    @Column(name = "REPORT_DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date REPORT_DATE;

    @Column(name = "REPORT_VERSION")
    private String REPORT_VERSION;

    @Column(name = "REPORT_FREQUENCY")
    private String REPORT_FREQUENCY;

    @Column(name = "REPORT_CODE")
    private String REPORT_CODE;

    @Column(name = "REPORT_DESC")
    private String REPORT_DESC;

    @Column(name = "ENTITY_FLG")
    private String ENTITY_FLG;

    @Column(name = "MODIFY_FLG")
    private String MODIFY_FLG;

    @Column(name = "DEL_FLG")
    private String DEL_FLG;

    @Column(name = "R18_BAL_SUB_DIARIES")
    private BigDecimal r18_bal_sub_diaries;

    @Column(name = "R19_BAL_SUB_DIARIES")
    private BigDecimal r19_bal_sub_diaries;

    @Column(name = "R20_BAL_SUB_DIARIES")
    private BigDecimal r20_bal_sub_diaries;

    @Column(name = "R21_BAL_SUB_DIARIES")
    private BigDecimal r21_bal_sub_diaries;

    @Column(name = "R22_BAL_SUB_DIARIES")
    private BigDecimal r22_bal_sub_diaries;

    @Column(name = "R23_BAL_SUB_DIARIES")
    private BigDecimal r23_bal_sub_diaries;

    @Column(name = "R24_BAL_SUB_DIARIES")
    private BigDecimal r24_bal_sub_diaries;

    @Column(name = "R25_BAL_SUB_DIARIES")
    private BigDecimal r25_bal_sub_diaries;

    @Column(name = "R26_BAL_SUB_DIARIES")
    private BigDecimal r26_bal_sub_diaries;

    @Column(name = "R27_BAL_SUB_DIARIES")
    private BigDecimal r27_bal_sub_diaries;

    @Column(name = "R28_BAL_SUB_DIARIES")
    private BigDecimal r28_bal_sub_diaries;

    @Column(name = "R29_BAL_SUB_DIARIES")
    private BigDecimal r29_bal_sub_diaries;

    @Column(name = "R30_BAL_SUB_DIARIES")
    private BigDecimal r30_bal_sub_diaries;

    @Column(name = "R31_BAL_SUB_DIARIES")
    private BigDecimal r31_bal_sub_diaries;

    @Column(name = "R40_BAL_SUB_DIARIES")
    private BigDecimal r40_bal_sub_diaries;

    @Column(name = "R41_BAL_SUB_DIARIES")
    private BigDecimal r41_bal_sub_diaries;

    @Column(name = "R42_BAL_SUB_DIARIES")
    private BigDecimal r42_bal_sub_diaries;

    @Column(name = "R43_BAL_SUB_DIARIES")
    private BigDecimal r43_bal_sub_diaries;


    
    @Column(name = "R48_BAL_SUB_DIARIES")
    private BigDecimal r48_bal_sub_diaries;
    
    @Column(name = "R49_BAL_SUB_DIARIES")
    private BigDecimal r49_bal_sub_diaries;

    @Column(name = "R50_BAL_SUB_DIARIES")
    private BigDecimal r50_bal_sub_diaries;

    @Column(name = "R51_BAL_SUB_DIARIES")
    private BigDecimal r51_bal_sub_diaries;

    @Column(name = "R52_BAL_SUB_DIARIES")
    private BigDecimal r52_bal_sub_diaries;

    @Column(name = "R53_BAL_SUB_DIARIES")
    private BigDecimal r53_bal_sub_diaries;

    @Column(name = "R54_BAL_SUB_DIARIES")
    private BigDecimal r54_bal_sub_diaries;

    @Column(name = "R55_BAL_SUB_DIARIES")
    private BigDecimal r55_bal_sub_diaries;

    @Column(name = "R56_BAL_SUB_DIARIES")
    private BigDecimal r56_bal_sub_diaries;

    @Column(name = "R57_BAL_SUB_DIARIES")
    private BigDecimal r57_bal_sub_diaries;

    @Column(name = "R58_BAL_SUB_DIARIES")
    private BigDecimal r58_bal_sub_diaries;

    @Column(name = "R59_BAL_SUB_DIARIES")
    private BigDecimal r59_bal_sub_diaries;

    @Column(name = "R60_BAL_SUB_DIARIES")
    private BigDecimal r60_bal_sub_diaries;

    @Column(name = "R61_BAL_SUB_DIARIES")
    private BigDecimal r61_bal_sub_diaries;

    @Column(name = "R62_BAL_SUB_DIARIES")
    private BigDecimal r62_bal_sub_diaries;

    @Column(name = "R63_BAL_SUB_DIARIES")
    private BigDecimal r63_bal_sub_diaries;

    public BigDecimal getR18_bal_sub_diaries() {
        return r18_bal_sub_diaries;
    }

    public void setR18_bal_sub_diaries(BigDecimal r18_bal_sub_diaries) {
        this.r18_bal_sub_diaries = r18_bal_sub_diaries;
    }

    public BigDecimal getR19_bal_sub_diaries() {
        return r19_bal_sub_diaries;
    }

    public void setR19_bal_sub_diaries(BigDecimal r19_bal_sub_diaries) {
        this.r19_bal_sub_diaries = r19_bal_sub_diaries;
    }

    public BigDecimal getR20_bal_sub_diaries() {
        return r20_bal_sub_diaries;
    }

    public void setR20_bal_sub_diaries(BigDecimal r20_bal_sub_diaries) {
        this.r20_bal_sub_diaries = r20_bal_sub_diaries;
    }

    public BigDecimal getR21_bal_sub_diaries() {
        return r21_bal_sub_diaries;
    }

    public void setR21_bal_sub_diaries(BigDecimal r21_bal_sub_diaries) {
        this.r21_bal_sub_diaries = r21_bal_sub_diaries;
    }

    public BigDecimal getR22_bal_sub_diaries() {
        return r22_bal_sub_diaries;
    }

    public void setR22_bal_sub_diaries(BigDecimal r22_bal_sub_diaries) {
        this.r22_bal_sub_diaries = r22_bal_sub_diaries;
    }

    public BigDecimal getR23_bal_sub_diaries() {
        return r23_bal_sub_diaries;
    }

    public void setR23_bal_sub_diaries(BigDecimal r23_bal_sub_diaries) {
        this.r23_bal_sub_diaries = r23_bal_sub_diaries;
    }

    public BigDecimal getR24_bal_sub_diaries() {
        return r24_bal_sub_diaries;
    }

    public void setR24_bal_sub_diaries(BigDecimal r24_bal_sub_diaries) {
        this.r24_bal_sub_diaries = r24_bal_sub_diaries;
    }

    public BigDecimal getR25_bal_sub_diaries() {
        return r25_bal_sub_diaries;
    }

    public void setR25_bal_sub_diaries(BigDecimal r25_bal_sub_diaries) {
        this.r25_bal_sub_diaries = r25_bal_sub_diaries;
    }

    public BigDecimal getR26_bal_sub_diaries() {
        return r26_bal_sub_diaries;
    }

    public void setR26_bal_sub_diaries(BigDecimal r26_bal_sub_diaries) {
        this.r26_bal_sub_diaries = r26_bal_sub_diaries;
    }

    public BigDecimal getR27_bal_sub_diaries() {
        return r27_bal_sub_diaries;
    }

    public void setR27_bal_sub_diaries(BigDecimal r27_bal_sub_diaries) {
        this.r27_bal_sub_diaries = r27_bal_sub_diaries;
    }

    public BigDecimal getR28_bal_sub_diaries() {
        return r28_bal_sub_diaries;
    }

    public void setR28_bal_sub_diaries(BigDecimal r28_bal_sub_diaries) {
        this.r28_bal_sub_diaries = r28_bal_sub_diaries;
    }

    public BigDecimal getR29_bal_sub_diaries() {
        return r29_bal_sub_diaries;
    }

    public void setR29_bal_sub_diaries(BigDecimal r29_bal_sub_diaries) {
        this.r29_bal_sub_diaries = r29_bal_sub_diaries;
    }

    public BigDecimal getR30_bal_sub_diaries() {
        return r30_bal_sub_diaries;
    }

    public void setR30_bal_sub_diaries(BigDecimal r30_bal_sub_diaries) {
        this.r30_bal_sub_diaries = r30_bal_sub_diaries;
    }

    public BigDecimal getR31_bal_sub_diaries() {
        return r31_bal_sub_diaries;
    }

    public void setR31_bal_sub_diaries(BigDecimal r31_bal_sub_diaries) {
        this.r31_bal_sub_diaries = r31_bal_sub_diaries;
    }

    public BigDecimal getR40_bal_sub_diaries() {
        return r40_bal_sub_diaries;
    }

    public void setR40_bal_sub_diaries(BigDecimal r40_bal_sub_diaries) {
        this.r40_bal_sub_diaries = r40_bal_sub_diaries;
    }

    public BigDecimal getR41_bal_sub_diaries() {
        return r41_bal_sub_diaries;
    }

    public void setR41_bal_sub_diaries(BigDecimal r41_bal_sub_diaries) {
        this.r41_bal_sub_diaries = r41_bal_sub_diaries;
    }

    public BigDecimal getR42_bal_sub_diaries() {
        return r42_bal_sub_diaries;
    }

    public void setR42_bal_sub_diaries(BigDecimal r42_bal_sub_diaries) {
        this.r42_bal_sub_diaries = r42_bal_sub_diaries;
    }

    public BigDecimal getR43_bal_sub_diaries() {
        return r43_bal_sub_diaries;
    }

    public void setR43_bal_sub_diaries(BigDecimal r43_bal_sub_diaries) {
        this.r43_bal_sub_diaries = r43_bal_sub_diaries;
    }

    public BigDecimal getR49_bal_sub_diaries() {
        return r49_bal_sub_diaries;
    }

    public void setR49_bal_sub_diaries(BigDecimal r49_bal_sub_diaries) {
        this.r49_bal_sub_diaries = r49_bal_sub_diaries;
    }

    public BigDecimal getR50_bal_sub_diaries() {
        return r50_bal_sub_diaries;
    }

    public void setR50_bal_sub_diaries(BigDecimal r50_bal_sub_diaries) {
        this.r50_bal_sub_diaries = r50_bal_sub_diaries;
    }

    public BigDecimal getR51_bal_sub_diaries() {
        return r51_bal_sub_diaries;
    }

    public void setR51_bal_sub_diaries(BigDecimal r51_bal_sub_diaries) {
        this.r51_bal_sub_diaries = r51_bal_sub_diaries;
    }

    public BigDecimal getR52_bal_sub_diaries() {
        return r52_bal_sub_diaries;
    }

    public void setR52_bal_sub_diaries(BigDecimal r52_bal_sub_diaries) {
        this.r52_bal_sub_diaries = r52_bal_sub_diaries;
    }

    public BigDecimal getR53_bal_sub_diaries() {
        return r53_bal_sub_diaries;
    }

    public void setR53_bal_sub_diaries(BigDecimal r53_bal_sub_diaries) {
        this.r53_bal_sub_diaries = r53_bal_sub_diaries;
    }

    public BigDecimal getR54_bal_sub_diaries() {
        return r54_bal_sub_diaries;
    }

    public void setR54_bal_sub_diaries(BigDecimal r54_bal_sub_diaries) {
        this.r54_bal_sub_diaries = r54_bal_sub_diaries;
    }

    public BigDecimal getR55_bal_sub_diaries() {
        return r55_bal_sub_diaries;
    }

    public void setR55_bal_sub_diaries(BigDecimal r55_bal_sub_diaries) {
        this.r55_bal_sub_diaries = r55_bal_sub_diaries;
    }

    public BigDecimal getR56_bal_sub_diaries() {
        return r56_bal_sub_diaries;
    }

    public void setR56_bal_sub_diaries(BigDecimal r56_bal_sub_diaries) {
        this.r56_bal_sub_diaries = r56_bal_sub_diaries;
    }

    public BigDecimal getR57_bal_sub_diaries() {
        return r57_bal_sub_diaries;
    }

    public void setR57_bal_sub_diaries(BigDecimal r57_bal_sub_diaries) {
        this.r57_bal_sub_diaries = r57_bal_sub_diaries;
    }

    public BigDecimal getR58_bal_sub_diaries() {
        return r58_bal_sub_diaries;
    }

    public void setR58_bal_sub_diaries(BigDecimal r58_bal_sub_diaries) {
        this.r58_bal_sub_diaries = r58_bal_sub_diaries;
    }

    public BigDecimal getR59_bal_sub_diaries() {
        return r59_bal_sub_diaries;
    }

    public void setR59_bal_sub_diaries(BigDecimal r59_bal_sub_diaries) {
        this.r59_bal_sub_diaries = r59_bal_sub_diaries;
    }

    public BigDecimal getR60_bal_sub_diaries() {
        return r60_bal_sub_diaries;
    }

    public void setR60_bal_sub_diaries(BigDecimal r60_bal_sub_diaries) {
        this.r60_bal_sub_diaries = r60_bal_sub_diaries;
    }

    public BigDecimal getR61_bal_sub_diaries() {
        return r61_bal_sub_diaries;
    }

    public void setR61_bal_sub_diaries(BigDecimal r61_bal_sub_diaries) {
        this.r61_bal_sub_diaries = r61_bal_sub_diaries;
    }

    public BigDecimal getR62_bal_sub_diaries() {
        return r62_bal_sub_diaries;
    }

    public void setR62_bal_sub_diaries(BigDecimal r62_bal_sub_diaries) {
        this.r62_bal_sub_diaries = r62_bal_sub_diaries;
    }

    public BigDecimal getR63_bal_sub_diaries() {
        return r63_bal_sub_diaries;
    }

    public void setR63_bal_sub_diaries(BigDecimal r63_bal_sub_diaries) {
        this.r63_bal_sub_diaries = r63_bal_sub_diaries;
    }

    public String getR9_intrest_div() {
        return r9_intrest_div;
    }

    public void setR9_intrest_div(String r9_intrest_div) {
        this.r9_intrest_div = r9_intrest_div;
    }

    public BigDecimal getR9_fig_bal_sheet() {
        return r9_fig_bal_sheet;
    }

    public void setR9_fig_bal_sheet(BigDecimal r9_fig_bal_sheet) {
        this.r9_fig_bal_sheet = r9_fig_bal_sheet;
    }

    public BigDecimal getR9_fig_bal_sheet_bwp() {
        return r9_fig_bal_sheet_bwp;
    }

    public void setR9_fig_bal_sheet_bwp(BigDecimal r9_fig_bal_sheet_bwp) {
        this.r9_fig_bal_sheet_bwp = r9_fig_bal_sheet_bwp;
    }

    public BigDecimal getR9_amt_statement_adj() {
        return r9_amt_statement_adj;
    }

    public void setR9_amt_statement_adj(BigDecimal r9_amt_statement_adj) {
        this.r9_amt_statement_adj = r9_amt_statement_adj;
    }

    public BigDecimal getR9_amt_statement_adj_bwp() {
        return r9_amt_statement_adj_bwp;
    }

    public void setR9_amt_statement_adj_bwp(BigDecimal r9_amt_statement_adj_bwp) {
        this.r9_amt_statement_adj_bwp = r9_amt_statement_adj_bwp;
    }

    public BigDecimal getR9_net_amt() {
        return r9_net_amt;
    }

    public void setR9_net_amt(BigDecimal r9_net_amt) {
        this.r9_net_amt = r9_net_amt;
    }

    public BigDecimal getR9_net_amt_bwp() {
        return r9_net_amt_bwp;
    }

    public void setR9_net_amt_bwp(BigDecimal r9_net_amt_bwp) {
        this.r9_net_amt_bwp = r9_net_amt_bwp;
    }

    public BigDecimal getR9_bal_sub() {
        return r9_bal_sub;
    }

    public void setR9_bal_sub(BigDecimal r9_bal_sub) {
        this.r9_bal_sub = r9_bal_sub;
    }

    public BigDecimal getR9_bal_sub_bwp() {
        return r9_bal_sub_bwp;
    }

    public void setR9_bal_sub_bwp(BigDecimal r9_bal_sub_bwp) {
        this.r9_bal_sub_bwp = r9_bal_sub_bwp;
    }

    public BigDecimal getR9_bal_sub_diaries() {
        return r9_bal_sub_diaries;
    }

    public void setR9_bal_sub_diaries(BigDecimal r9_bal_sub_diaries) {
        this.r9_bal_sub_diaries = r9_bal_sub_diaries;
    }

    public BigDecimal getR9_bal_sub_diaries_bwp() {
        return r9_bal_sub_diaries_bwp;
    }

    public void setR9_bal_sub_diaries_bwp(BigDecimal r9_bal_sub_diaries_bwp) {
        this.r9_bal_sub_diaries_bwp = r9_bal_sub_diaries_bwp;
    }

    public String getR10_intrest_div() {
        return r10_intrest_div;
    }

    public void setR10_intrest_div(String r10_intrest_div) {
        this.r10_intrest_div = r10_intrest_div;
    }

    public BigDecimal getR10_fig_bal_sheet() {
        return r10_fig_bal_sheet;
    }

    public void setR10_fig_bal_sheet(BigDecimal r10_fig_bal_sheet) {
        this.r10_fig_bal_sheet = r10_fig_bal_sheet;
    }

    public BigDecimal getR10_fig_bal_sheet_bwp() {
        return r10_fig_bal_sheet_bwp;
    }

    public void setR10_fig_bal_sheet_bwp(BigDecimal r10_fig_bal_sheet_bwp) {
        this.r10_fig_bal_sheet_bwp = r10_fig_bal_sheet_bwp;
    }

    public BigDecimal getR10_amt_statement_adj() {
        return r10_amt_statement_adj;
    }

    public void setR10_amt_statement_adj(BigDecimal r10_amt_statement_adj) {
        this.r10_amt_statement_adj = r10_amt_statement_adj;
    }

    public BigDecimal getR10_amt_statement_adj_bwp() {
        return r10_amt_statement_adj_bwp;
    }

    public void setR10_amt_statement_adj_bwp(BigDecimal r10_amt_statement_adj_bwp) {
        this.r10_amt_statement_adj_bwp = r10_amt_statement_adj_bwp;
    }

    public BigDecimal getR10_net_amt() {
        return r10_net_amt;
    }

    public void setR10_net_amt(BigDecimal r10_net_amt) {
        this.r10_net_amt = r10_net_amt;
    }

    public BigDecimal getR10_net_amt_bwp() {
        return r10_net_amt_bwp;
    }

    public void setR10_net_amt_bwp(BigDecimal r10_net_amt_bwp) {
        this.r10_net_amt_bwp = r10_net_amt_bwp;
    }

    public BigDecimal getR10_bal_sub() {
        return r10_bal_sub;
    }

    public void setR10_bal_sub(BigDecimal r10_bal_sub) {
        this.r10_bal_sub = r10_bal_sub;
    }

    public BigDecimal getR10_bal_sub_bwp() {
        return r10_bal_sub_bwp;
    }

    public void setR10_bal_sub_bwp(BigDecimal r10_bal_sub_bwp) {
        this.r10_bal_sub_bwp = r10_bal_sub_bwp;
    }

    public BigDecimal getR10_bal_sub_diaries() {
        return r10_bal_sub_diaries;
    }

    public void setR10_bal_sub_diaries(BigDecimal r10_bal_sub_diaries) {
        this.r10_bal_sub_diaries = r10_bal_sub_diaries;
    }

    public BigDecimal getR10_bal_sub_diaries_bwp() {
        return r10_bal_sub_diaries_bwp;
    }

    public void setR10_bal_sub_diaries_bwp(BigDecimal r10_bal_sub_diaries_bwp) {
        this.r10_bal_sub_diaries_bwp = r10_bal_sub_diaries_bwp;
    }

    public String getR11_intrest_div() {
        return r11_intrest_div;
    }

    public void setR11_intrest_div(String r11_intrest_div) {
        this.r11_intrest_div = r11_intrest_div;
    }

    public BigDecimal getR11_fig_bal_sheet() {
        return r11_fig_bal_sheet;
    }

    public void setR11_fig_bal_sheet(BigDecimal r11_fig_bal_sheet) {
        this.r11_fig_bal_sheet = r11_fig_bal_sheet;
    }

    public BigDecimal getR11_fig_bal_sheet_bwp() {
        return r11_fig_bal_sheet_bwp;
    }

    public void setR11_fig_bal_sheet_bwp(BigDecimal r11_fig_bal_sheet_bwp) {
        this.r11_fig_bal_sheet_bwp = r11_fig_bal_sheet_bwp;
    }

    public BigDecimal getR11_amt_statement_adj() {
        return r11_amt_statement_adj;
    }

    public void setR11_amt_statement_adj(BigDecimal r11_amt_statement_adj) {
        this.r11_amt_statement_adj = r11_amt_statement_adj;
    }

    public BigDecimal getR11_amt_statement_adj_bwp() {
        return r11_amt_statement_adj_bwp;
    }

    public void setR11_amt_statement_adj_bwp(BigDecimal r11_amt_statement_adj_bwp) {
        this.r11_amt_statement_adj_bwp = r11_amt_statement_adj_bwp;
    }

    public BigDecimal getR11_net_amt() {
        return r11_net_amt;
    }

    public void setR11_net_amt(BigDecimal r11_net_amt) {
        this.r11_net_amt = r11_net_amt;
    }

    public BigDecimal getR11_net_amt_bwp() {
        return r11_net_amt_bwp;
    }

    public void setR11_net_amt_bwp(BigDecimal r11_net_amt_bwp) {
        this.r11_net_amt_bwp = r11_net_amt_bwp;
    }

    public BigDecimal getR11_bal_sub() {
        return r11_bal_sub;
    }

    public void setR11_bal_sub(BigDecimal r11_bal_sub) {
        this.r11_bal_sub = r11_bal_sub;
    }

    public BigDecimal getR11_bal_sub_bwp() {
        return r11_bal_sub_bwp;
    }

    public void setR11_bal_sub_bwp(BigDecimal r11_bal_sub_bwp) {
        this.r11_bal_sub_bwp = r11_bal_sub_bwp;
    }

    public BigDecimal getR11_bal_sub_diaries() {
        return r11_bal_sub_diaries;
    }

    public void setR11_bal_sub_diaries(BigDecimal r11_bal_sub_diaries) {
        this.r11_bal_sub_diaries = r11_bal_sub_diaries;
    }

    public BigDecimal getR11_bal_sub_diaries_bwp() {
        return r11_bal_sub_diaries_bwp;
    }

    public void setR11_bal_sub_diaries_bwp(BigDecimal r11_bal_sub_diaries_bwp) {
        this.r11_bal_sub_diaries_bwp = r11_bal_sub_diaries_bwp;
    }

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

    public String getR13_intrest_div() {
        return r13_intrest_div;
    }

    public void setR13_intrest_div(String r13_intrest_div) {
        this.r13_intrest_div = r13_intrest_div;
    }

    public BigDecimal getR13_fig_bal_sheet() {
        return r13_fig_bal_sheet;
    }

    public void setR13_fig_bal_sheet(BigDecimal r13_fig_bal_sheet) {
        this.r13_fig_bal_sheet = r13_fig_bal_sheet;
    }

    public BigDecimal getR13_fig_bal_sheet_bwp() {
        return r13_fig_bal_sheet_bwp;
    }

    public void setR13_fig_bal_sheet_bwp(BigDecimal r13_fig_bal_sheet_bwp) {
        this.r13_fig_bal_sheet_bwp = r13_fig_bal_sheet_bwp;
    }

    public BigDecimal getR13_amt_statement_adj() {
        return r13_amt_statement_adj;
    }

    public void setR13_amt_statement_adj(BigDecimal r13_amt_statement_adj) {
        this.r13_amt_statement_adj = r13_amt_statement_adj;
    }

    public BigDecimal getR13_amt_statement_adj_bwp() {
        return r13_amt_statement_adj_bwp;
    }

    public void setR13_amt_statement_adj_bwp(BigDecimal r13_amt_statement_adj_bwp) {
        this.r13_amt_statement_adj_bwp = r13_amt_statement_adj_bwp;
    }

    public BigDecimal getR13_net_amt() {
        return r13_net_amt;
    }

    public void setR13_net_amt(BigDecimal r13_net_amt) {
        this.r13_net_amt = r13_net_amt;
    }

    public BigDecimal getR13_net_amt_bwp() {
        return r13_net_amt_bwp;
    }

    public void setR13_net_amt_bwp(BigDecimal r13_net_amt_bwp) {
        this.r13_net_amt_bwp = r13_net_amt_bwp;
    }

    public BigDecimal getR13_bal_sub() {
        return r13_bal_sub;
    }

    public void setR13_bal_sub(BigDecimal r13_bal_sub) {
        this.r13_bal_sub = r13_bal_sub;
    }

    public BigDecimal getR13_bal_sub_bwp() {
        return r13_bal_sub_bwp;
    }

    public void setR13_bal_sub_bwp(BigDecimal r13_bal_sub_bwp) {
        this.r13_bal_sub_bwp = r13_bal_sub_bwp;
    }

    public BigDecimal getR13_bal_sub_diaries() {
        return r13_bal_sub_diaries;
    }

    public void setR13_bal_sub_diaries(BigDecimal r13_bal_sub_diaries) {
        this.r13_bal_sub_diaries = r13_bal_sub_diaries;
    }

    public BigDecimal getR13_bal_sub_diaries_bwp() {
        return r13_bal_sub_diaries_bwp;
    }

    public void setR13_bal_sub_diaries_bwp(BigDecimal r13_bal_sub_diaries_bwp) {
        this.r13_bal_sub_diaries_bwp = r13_bal_sub_diaries_bwp;
    }

    public String getR17_other_income() {
        return r17_other_income;
    }

    public void setR17_other_income(String r17_other_income) {
        this.r17_other_income = r17_other_income;
    }

    public BigDecimal getR17_fig_bal_sheet() {
        return r17_fig_bal_sheet;
    }

    public void setR17_fig_bal_sheet(BigDecimal r17_fig_bal_sheet) {
        this.r17_fig_bal_sheet = r17_fig_bal_sheet;
    }

    public BigDecimal getR17_fig_bal_sheet_bwp() {
        return r17_fig_bal_sheet_bwp;
    }

    public void setR17_fig_bal_sheet_bwp(BigDecimal r17_fig_bal_sheet_bwp) {
        this.r17_fig_bal_sheet_bwp = r17_fig_bal_sheet_bwp;
    }

    public BigDecimal getR17_amt_statement_adj() {
        return r17_amt_statement_adj;
    }

    public void setR17_amt_statement_adj(BigDecimal r17_amt_statement_adj) {
        this.r17_amt_statement_adj = r17_amt_statement_adj;
    }

    public BigDecimal getR17_amt_statement_adj_bwp() {
        return r17_amt_statement_adj_bwp;
    }

    public void setR17_amt_statement_adj_bwp(BigDecimal r17_amt_statement_adj_bwp) {
        this.r17_amt_statement_adj_bwp = r17_amt_statement_adj_bwp;
    }

    public BigDecimal getR17_net_amt() {
        return r17_net_amt;
    }

    public void setR17_net_amt(BigDecimal r17_net_amt) {
        this.r17_net_amt = r17_net_amt;
    }

    public BigDecimal getR17_net_amt_bwp() {
        return r17_net_amt_bwp;
    }

    public void setR17_net_amt_bwp(BigDecimal r17_net_amt_bwp) {
        this.r17_net_amt_bwp = r17_net_amt_bwp;
    }

    public BigDecimal getR17_bal_sub() {
        return r17_bal_sub;
    }

    public void setR17_bal_sub(BigDecimal r17_bal_sub) {
        this.r17_bal_sub = r17_bal_sub;
    }

    public BigDecimal getR17_bal_sub_bwp() {
        return r17_bal_sub_bwp;
    }

    public void setR17_bal_sub_bwp(BigDecimal r17_bal_sub_bwp) {
        this.r17_bal_sub_bwp = r17_bal_sub_bwp;
    }

    public BigDecimal getR17_bal_sub_diaries_bwp() {
        return r17_bal_sub_diaries_bwp;
    }

    public void setR17_bal_sub_diaries_bwp(BigDecimal r17_bal_sub_diaries_bwp) {
        this.r17_bal_sub_diaries_bwp = r17_bal_sub_diaries_bwp;
    }

    public String getR18_other_income() {
        return r18_other_income;
    }

    public void setR18_other_income(String r18_other_income) {
        this.r18_other_income = r18_other_income;
    }

    public BigDecimal getR18_fig_bal_sheet() {
        return r18_fig_bal_sheet;
    }

    public void setR18_fig_bal_sheet(BigDecimal r18_fig_bal_sheet) {
        this.r18_fig_bal_sheet = r18_fig_bal_sheet;
    }

    public BigDecimal getR18_fig_bal_sheet_bwp() {
        return r18_fig_bal_sheet_bwp;
    }

    public void setR18_fig_bal_sheet_bwp(BigDecimal r18_fig_bal_sheet_bwp) {
        this.r18_fig_bal_sheet_bwp = r18_fig_bal_sheet_bwp;
    }

    public BigDecimal getR18_amt_statement_adj() {
        return r18_amt_statement_adj;
    }

    public void setR18_amt_statement_adj(BigDecimal r18_amt_statement_adj) {
        this.r18_amt_statement_adj = r18_amt_statement_adj;
    }

    public BigDecimal getR18_amt_statement_adj_bwp() {
        return r18_amt_statement_adj_bwp;
    }

    public void setR18_amt_statement_adj_bwp(BigDecimal r18_amt_statement_adj_bwp) {
        this.r18_amt_statement_adj_bwp = r18_amt_statement_adj_bwp;
    }

    public BigDecimal getR18_net_amt() {
        return r18_net_amt;
    }

    public void setR18_net_amt(BigDecimal r18_net_amt) {
        this.r18_net_amt = r18_net_amt;
    }

    public BigDecimal getR18_net_amt_bwp() {
        return r18_net_amt_bwp;
    }

    public void setR18_net_amt_bwp(BigDecimal r18_net_amt_bwp) {
        this.r18_net_amt_bwp = r18_net_amt_bwp;
    }

    public BigDecimal getR18_bal_sub() {
        return r18_bal_sub;
    }

    public void setR18_bal_sub(BigDecimal r18_bal_sub) {
        this.r18_bal_sub = r18_bal_sub;
    }

    public BigDecimal getR18_bal_sub_bwp() {
        return r18_bal_sub_bwp;
    }

    public void setR18_bal_sub_bwp(BigDecimal r18_bal_sub_bwp) {
        this.r18_bal_sub_bwp = r18_bal_sub_bwp;
    }

    public BigDecimal getR18_bal_sub_diaries_bwp() {
        return r18_bal_sub_diaries_bwp;
    }

    public void setR18_bal_sub_diaries_bwp(BigDecimal r18_bal_sub_diaries_bwp) {
        this.r18_bal_sub_diaries_bwp = r18_bal_sub_diaries_bwp;
    }

    public String getR19_other_income() {
        return r19_other_income;
    }

    public void setR19_other_income(String r19_other_income) {
        this.r19_other_income = r19_other_income;
    }

    public BigDecimal getR19_fig_bal_sheet() {
        return r19_fig_bal_sheet;
    }

    public void setR19_fig_bal_sheet(BigDecimal r19_fig_bal_sheet) {
        this.r19_fig_bal_sheet = r19_fig_bal_sheet;
    }

    public BigDecimal getR19_fig_bal_sheet_bwp() {
        return r19_fig_bal_sheet_bwp;
    }

    public void setR19_fig_bal_sheet_bwp(BigDecimal r19_fig_bal_sheet_bwp) {
        this.r19_fig_bal_sheet_bwp = r19_fig_bal_sheet_bwp;
    }

    public BigDecimal getR19_amt_statement_adj() {
        return r19_amt_statement_adj;
    }

    public void setR19_amt_statement_adj(BigDecimal r19_amt_statement_adj) {
        this.r19_amt_statement_adj = r19_amt_statement_adj;
    }

    public BigDecimal getR19_amt_statement_adj_bwp() {
        return r19_amt_statement_adj_bwp;
    }

    public void setR19_amt_statement_adj_bwp(BigDecimal r19_amt_statement_adj_bwp) {
        this.r19_amt_statement_adj_bwp = r19_amt_statement_adj_bwp;
    }

    public BigDecimal getR19_net_amt() {
        return r19_net_amt;
    }

    public void setR19_net_amt(BigDecimal r19_net_amt) {
        this.r19_net_amt = r19_net_amt;
    }

    public BigDecimal getR19_net_amt_bwp() {
        return r19_net_amt_bwp;
    }

    public void setR19_net_amt_bwp(BigDecimal r19_net_amt_bwp) {
        this.r19_net_amt_bwp = r19_net_amt_bwp;
    }

    public BigDecimal getR19_bal_sub() {
        return r19_bal_sub;
    }

    public void setR19_bal_sub(BigDecimal r19_bal_sub) {
        this.r19_bal_sub = r19_bal_sub;
    }

    public BigDecimal getR19_bal_sub_bwp() {
        return r19_bal_sub_bwp;
    }

    public void setR19_bal_sub_bwp(BigDecimal r19_bal_sub_bwp) {
        this.r19_bal_sub_bwp = r19_bal_sub_bwp;
    }

    public BigDecimal getR19_bal_sub_diaries_bwp() {
        return r19_bal_sub_diaries_bwp;
    }

    public void setR19_bal_sub_diaries_bwp(BigDecimal r19_bal_sub_diaries_bwp) {
        this.r19_bal_sub_diaries_bwp = r19_bal_sub_diaries_bwp;
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

    public BigDecimal getR30_bal_sub_diaries_bwp() {
        return r30_bal_sub_diaries_bwp;
    }

    public void setR30_bal_sub_diaries_bwp(BigDecimal r30_bal_sub_diaries_bwp) {
        this.r30_bal_sub_diaries_bwp = r30_bal_sub_diaries_bwp;
    }

    public String getR31_other_income() {
        return r31_other_income;
    }

    public void setR31_other_income(String r31_other_income) {
        this.r31_other_income = r31_other_income;
    }

    public BigDecimal getR31_fig_bal_sheet() {
        return r31_fig_bal_sheet;
    }

    public void setR31_fig_bal_sheet(BigDecimal r31_fig_bal_sheet) {
        this.r31_fig_bal_sheet = r31_fig_bal_sheet;
    }

    public BigDecimal getR31_fig_bal_sheet_bwp() {
        return r31_fig_bal_sheet_bwp;
    }

    public void setR31_fig_bal_sheet_bwp(BigDecimal r31_fig_bal_sheet_bwp) {
        this.r31_fig_bal_sheet_bwp = r31_fig_bal_sheet_bwp;
    }

    public BigDecimal getR31_amt_statement_adj() {
        return r31_amt_statement_adj;
    }

    public void setR31_amt_statement_adj(BigDecimal r31_amt_statement_adj) {
        this.r31_amt_statement_adj = r31_amt_statement_adj;
    }

    public BigDecimal getR31_amt_statement_adj_bwp() {
        return r31_amt_statement_adj_bwp;
    }

    public void setR31_amt_statement_adj_bwp(BigDecimal r31_amt_statement_adj_bwp) {
        this.r31_amt_statement_adj_bwp = r31_amt_statement_adj_bwp;
    }

    public BigDecimal getR31_net_amt() {
        return r31_net_amt;
    }

    public void setR31_net_amt(BigDecimal r31_net_amt) {
        this.r31_net_amt = r31_net_amt;
    }

    public BigDecimal getR31_net_amt_bwp() {
        return r31_net_amt_bwp;
    }

    public void setR31_net_amt_bwp(BigDecimal r31_net_amt_bwp) {
        this.r31_net_amt_bwp = r31_net_amt_bwp;
    }

    public BigDecimal getR31_bal_sub() {
        return r31_bal_sub;
    }

    public void setR31_bal_sub(BigDecimal r31_bal_sub) {
        this.r31_bal_sub = r31_bal_sub;
    }

    public BigDecimal getR31_bal_sub_bwp() {
        return r31_bal_sub_bwp;
    }

    public void setR31_bal_sub_bwp(BigDecimal r31_bal_sub_bwp) {
        this.r31_bal_sub_bwp = r31_bal_sub_bwp;
    }

    public BigDecimal getR31_bal_sub_diaries_bwp() {
        return r31_bal_sub_diaries_bwp;
    }

    public void setR31_bal_sub_diaries_bwp(BigDecimal r31_bal_sub_diaries_bwp) {
        this.r31_bal_sub_diaries_bwp = r31_bal_sub_diaries_bwp;
    }

    public String getR40_intrest_expended() {
        return r40_intrest_expended;
    }

    public void setR40_intrest_expended(String r40_intrest_expended) {
        this.r40_intrest_expended = r40_intrest_expended;
    }

    public BigDecimal getR40_fig_bal_sheet() {
        return r40_fig_bal_sheet;
    }

    public void setR40_fig_bal_sheet(BigDecimal r40_fig_bal_sheet) {
        this.r40_fig_bal_sheet = r40_fig_bal_sheet;
    }

    public BigDecimal getR40_fig_bal_sheet_bwp() {
        return r40_fig_bal_sheet_bwp;
    }

    public void setR40_fig_bal_sheet_bwp(BigDecimal r40_fig_bal_sheet_bwp) {
        this.r40_fig_bal_sheet_bwp = r40_fig_bal_sheet_bwp;
    }

    public BigDecimal getR40_amt_statement_adj() {
        return r40_amt_statement_adj;
    }

    public void setR40_amt_statement_adj(BigDecimal r40_amt_statement_adj) {
        this.r40_amt_statement_adj = r40_amt_statement_adj;
    }

    public BigDecimal getR40_amt_statement_adj_bwp() {
        return r40_amt_statement_adj_bwp;
    }

    public void setR40_amt_statement_adj_bwp(BigDecimal r40_amt_statement_adj_bwp) {
        this.r40_amt_statement_adj_bwp = r40_amt_statement_adj_bwp;
    }

    public BigDecimal getR40_net_amt() {
        return r40_net_amt;
    }

    public void setR40_net_amt(BigDecimal r40_net_amt) {
        this.r40_net_amt = r40_net_amt;
    }

    public BigDecimal getR40_net_amt_bwp() {
        return r40_net_amt_bwp;
    }

    public void setR40_net_amt_bwp(BigDecimal r40_net_amt_bwp) {
        this.r40_net_amt_bwp = r40_net_amt_bwp;
    }

    public BigDecimal getR40_bal_sub() {
        return r40_bal_sub;
    }

    public void setR40_bal_sub(BigDecimal r40_bal_sub) {
        this.r40_bal_sub = r40_bal_sub;
    }

    public BigDecimal getR40_bal_sub_bwp() {
        return r40_bal_sub_bwp;
    }

    public void setR40_bal_sub_bwp(BigDecimal r40_bal_sub_bwp) {
        this.r40_bal_sub_bwp = r40_bal_sub_bwp;
    }

    public BigDecimal getR40_bal_sub_diaries_bwp() {
        return r40_bal_sub_diaries_bwp;
    }

    public void setR40_bal_sub_diaries_bwp(BigDecimal r40_bal_sub_diaries_bwp) {
        this.r40_bal_sub_diaries_bwp = r40_bal_sub_diaries_bwp;
    }

    public String getR41_intrest_expended() {
        return r41_intrest_expended;
    }

    public void setR41_intrest_expended(String r41_intrest_expended) {
        this.r41_intrest_expended = r41_intrest_expended;
    }

    public BigDecimal getR41_fig_bal_sheet() {
        return r41_fig_bal_sheet;
    }

    public void setR41_fig_bal_sheet(BigDecimal r41_fig_bal_sheet) {
        this.r41_fig_bal_sheet = r41_fig_bal_sheet;
    }

    public BigDecimal getR41_fig_bal_sheet_bwp() {
        return r41_fig_bal_sheet_bwp;
    }

    public void setR41_fig_bal_sheet_bwp(BigDecimal r41_fig_bal_sheet_bwp) {
        this.r41_fig_bal_sheet_bwp = r41_fig_bal_sheet_bwp;
    }

    public BigDecimal getR41_amt_statement_adj() {
        return r41_amt_statement_adj;
    }

    public void setR41_amt_statement_adj(BigDecimal r41_amt_statement_adj) {
        this.r41_amt_statement_adj = r41_amt_statement_adj;
    }

    public BigDecimal getR41_amt_statement_adj_bwp() {
        return r41_amt_statement_adj_bwp;
    }

    public void setR41_amt_statement_adj_bwp(BigDecimal r41_amt_statement_adj_bwp) {
        this.r41_amt_statement_adj_bwp = r41_amt_statement_adj_bwp;
    }

    public BigDecimal getR41_net_amt() {
        return r41_net_amt;
    }

    public void setR41_net_amt(BigDecimal r41_net_amt) {
        this.r41_net_amt = r41_net_amt;
    }

    public BigDecimal getR41_net_amt_bwp() {
        return r41_net_amt_bwp;
    }

    public void setR41_net_amt_bwp(BigDecimal r41_net_amt_bwp) {
        this.r41_net_amt_bwp = r41_net_amt_bwp;
    }

    public BigDecimal getR41_bal_sub() {
        return r41_bal_sub;
    }

    public void setR41_bal_sub(BigDecimal r41_bal_sub) {
        this.r41_bal_sub = r41_bal_sub;
    }

    public BigDecimal getR41_bal_sub_bwp() {
        return r41_bal_sub_bwp;
    }

    public void setR41_bal_sub_bwp(BigDecimal r41_bal_sub_bwp) {
        this.r41_bal_sub_bwp = r41_bal_sub_bwp;
    }

    public BigDecimal getR41_bal_sub_diaries_bwp() {
        return r41_bal_sub_diaries_bwp;
    }

    public void setR41_bal_sub_diaries_bwp(BigDecimal r41_bal_sub_diaries_bwp) {
        this.r41_bal_sub_diaries_bwp = r41_bal_sub_diaries_bwp;
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

    public BigDecimal getR42_bal_sub_diaries_bwp() {
        return r42_bal_sub_diaries_bwp;
    }

    public void setR42_bal_sub_diaries_bwp(BigDecimal r42_bal_sub_diaries_bwp) {
        this.r42_bal_sub_diaries_bwp = r42_bal_sub_diaries_bwp;
    }

    public String getR43_intrest_expended() {
        return r43_intrest_expended;
    }

    public void setR43_intrest_expended(String r43_intrest_expended) {
        this.r43_intrest_expended = r43_intrest_expended;
    }

    public BigDecimal getR43_fig_bal_sheet() {
        return r43_fig_bal_sheet;
    }

    public void setR43_fig_bal_sheet(BigDecimal r43_fig_bal_sheet) {
        this.r43_fig_bal_sheet = r43_fig_bal_sheet;
    }

    public BigDecimal getR43_fig_bal_sheet_bwp() {
        return r43_fig_bal_sheet_bwp;
    }

    public void setR43_fig_bal_sheet_bwp(BigDecimal r43_fig_bal_sheet_bwp) {
        this.r43_fig_bal_sheet_bwp = r43_fig_bal_sheet_bwp;
    }

    public BigDecimal getR43_amt_statement_adj() {
        return r43_amt_statement_adj;
    }

    public void setR43_amt_statement_adj(BigDecimal r43_amt_statement_adj) {
        this.r43_amt_statement_adj = r43_amt_statement_adj;
    }

    public BigDecimal getR43_amt_statement_adj_bwp() {
        return r43_amt_statement_adj_bwp;
    }

    public void setR43_amt_statement_adj_bwp(BigDecimal r43_amt_statement_adj_bwp) {
        this.r43_amt_statement_adj_bwp = r43_amt_statement_adj_bwp;
    }

    public BigDecimal getR43_net_amt() {
        return r43_net_amt;
    }

    public void setR43_net_amt(BigDecimal r43_net_amt) {
        this.r43_net_amt = r43_net_amt;
    }

    public BigDecimal getR43_net_amt_bwp() {
        return r43_net_amt_bwp;
    }

    public void setR43_net_amt_bwp(BigDecimal r43_net_amt_bwp) {
        this.r43_net_amt_bwp = r43_net_amt_bwp;
    }

    public BigDecimal getR43_bal_sub() {
        return r43_bal_sub;
    }

    public void setR43_bal_sub(BigDecimal r43_bal_sub) {
        this.r43_bal_sub = r43_bal_sub;
    }

    public BigDecimal getR43_bal_sub_bwp() {
        return r43_bal_sub_bwp;
    }

    public void setR43_bal_sub_bwp(BigDecimal r43_bal_sub_bwp) {
        this.r43_bal_sub_bwp = r43_bal_sub_bwp;
    }

    public BigDecimal getR43_bal_sub_diaries_bwp() {
        return r43_bal_sub_diaries_bwp;
    }

    public void setR43_bal_sub_diaries_bwp(BigDecimal r43_bal_sub_diaries_bwp) {
        this.r43_bal_sub_diaries_bwp = r43_bal_sub_diaries_bwp;
    }

    public String getR48_operating_expenses() {
        return r48_operating_expenses;
    }

    public void setR48_operating_expenses(String r48_operating_expenses) {
        this.r48_operating_expenses = r48_operating_expenses;
    }

    public BigDecimal getR48_fig_bal_sheet() {
        return r48_fig_bal_sheet;
    }

    public void setR48_fig_bal_sheet(BigDecimal r48_fig_bal_sheet) {
        this.r48_fig_bal_sheet = r48_fig_bal_sheet;
    }

    public BigDecimal getR48_fig_bal_sheet_bwp() {
        return r48_fig_bal_sheet_bwp;
    }

    public void setR48_fig_bal_sheet_bwp(BigDecimal r48_fig_bal_sheet_bwp) {
        this.r48_fig_bal_sheet_bwp = r48_fig_bal_sheet_bwp;
    }

    public BigDecimal getR48_amt_statement_adj() {
        return r48_amt_statement_adj;
    }

    public void setR48_amt_statement_adj(BigDecimal r48_amt_statement_adj) {
        this.r48_amt_statement_adj = r48_amt_statement_adj;
    }

    public BigDecimal getR48_amt_statement_adj_bwp() {
        return r48_amt_statement_adj_bwp;
    }

    public void setR48_amt_statement_adj_bwp(BigDecimal r48_amt_statement_adj_bwp) {
        this.r48_amt_statement_adj_bwp = r48_amt_statement_adj_bwp;
    }

    public BigDecimal getR48_net_amt() {
        return r48_net_amt;
    }

    public void setR48_net_amt(BigDecimal r48_net_amt) {
        this.r48_net_amt = r48_net_amt;
    }

    public BigDecimal getR48_net_amt_bwp() {
        return r48_net_amt_bwp;
    }

    public void setR48_net_amt_bwp(BigDecimal r48_net_amt_bwp) {
        this.r48_net_amt_bwp = r48_net_amt_bwp;
    }

    public BigDecimal getR48_bal_sub() {
        return r48_bal_sub;
    }

    public void setR48_bal_sub(BigDecimal r48_bal_sub) {
        this.r48_bal_sub = r48_bal_sub;
    }

    public BigDecimal getR48_bal_sub_bwp() {
        return r48_bal_sub_bwp;
    }

    public void setR48_bal_sub_bwp(BigDecimal r48_bal_sub_bwp) {
        this.r48_bal_sub_bwp = r48_bal_sub_bwp;
    }

    public BigDecimal getR48_bal_sub_diaries_bwp() {
        return r48_bal_sub_diaries_bwp;
    }

    public void setR48_bal_sub_diaries_bwp(BigDecimal r48_bal_sub_diaries_bwp) {
        this.r48_bal_sub_diaries_bwp = r48_bal_sub_diaries_bwp;
    }

    public String getR49_operating_expenses() {
        return r49_operating_expenses;
    }

    public void setR49_operating_expenses(String r49_operating_expenses) {
        this.r49_operating_expenses = r49_operating_expenses;
    }

    public BigDecimal getR49_fig_bal_sheet() {
        return r49_fig_bal_sheet;
    }

    public void setR49_fig_bal_sheet(BigDecimal r49_fig_bal_sheet) {
        this.r49_fig_bal_sheet = r49_fig_bal_sheet;
    }

    public BigDecimal getR49_fig_bal_sheet_bwp() {
        return r49_fig_bal_sheet_bwp;
    }

    public void setR49_fig_bal_sheet_bwp(BigDecimal r49_fig_bal_sheet_bwp) {
        this.r49_fig_bal_sheet_bwp = r49_fig_bal_sheet_bwp;
    }

    public BigDecimal getR49_amt_statement_adj() {
        return r49_amt_statement_adj;
    }

    public void setR49_amt_statement_adj(BigDecimal r49_amt_statement_adj) {
        this.r49_amt_statement_adj = r49_amt_statement_adj;
    }

    public BigDecimal getR49_amt_statement_adj_bwp() {
        return r49_amt_statement_adj_bwp;
    }

    public void setR49_amt_statement_adj_bwp(BigDecimal r49_amt_statement_adj_bwp) {
        this.r49_amt_statement_adj_bwp = r49_amt_statement_adj_bwp;
    }

    public BigDecimal getR49_net_amt() {
        return r49_net_amt;
    }

    public void setR49_net_amt(BigDecimal r49_net_amt) {
        this.r49_net_amt = r49_net_amt;
    }

    public BigDecimal getR49_net_amt_bwp() {
        return r49_net_amt_bwp;
    }

    public void setR49_net_amt_bwp(BigDecimal r49_net_amt_bwp) {
        this.r49_net_amt_bwp = r49_net_amt_bwp;
    }

    public BigDecimal getR49_bal_sub() {
        return r49_bal_sub;
    }

    public void setR49_bal_sub(BigDecimal r49_bal_sub) {
        this.r49_bal_sub = r49_bal_sub;
    }

    public BigDecimal getR49_bal_sub_bwp() {
        return r49_bal_sub_bwp;
    }

    public void setR49_bal_sub_bwp(BigDecimal r49_bal_sub_bwp) {
        this.r49_bal_sub_bwp = r49_bal_sub_bwp;
    }

    public BigDecimal getR49_bal_sub_diaries_bwp() {
        return r49_bal_sub_diaries_bwp;
    }

    public void setR49_bal_sub_diaries_bwp(BigDecimal r49_bal_sub_diaries_bwp) {
        this.r49_bal_sub_diaries_bwp = r49_bal_sub_diaries_bwp;
    }

    public String getR50_operating_expenses() {
        return r50_operating_expenses;
    }

    public void setR50_operating_expenses(String r50_operating_expenses) {
        this.r50_operating_expenses = r50_operating_expenses;
    }

    public BigDecimal getR50_fig_bal_sheet() {
        return r50_fig_bal_sheet;
    }

    public void setR50_fig_bal_sheet(BigDecimal r50_fig_bal_sheet) {
        this.r50_fig_bal_sheet = r50_fig_bal_sheet;
    }

    public BigDecimal getR50_fig_bal_sheet_bwp() {
        return r50_fig_bal_sheet_bwp;
    }

    public void setR50_fig_bal_sheet_bwp(BigDecimal r50_fig_bal_sheet_bwp) {
        this.r50_fig_bal_sheet_bwp = r50_fig_bal_sheet_bwp;
    }

    public BigDecimal getR50_amt_statement_adj() {
        return r50_amt_statement_adj;
    }

    public void setR50_amt_statement_adj(BigDecimal r50_amt_statement_adj) {
        this.r50_amt_statement_adj = r50_amt_statement_adj;
    }

    public BigDecimal getR50_amt_statement_adj_bwp() {
        return r50_amt_statement_adj_bwp;
    }

    public void setR50_amt_statement_adj_bwp(BigDecimal r50_amt_statement_adj_bwp) {
        this.r50_amt_statement_adj_bwp = r50_amt_statement_adj_bwp;
    }

    public BigDecimal getR50_net_amt() {
        return r50_net_amt;
    }

    public void setR50_net_amt(BigDecimal r50_net_amt) {
        this.r50_net_amt = r50_net_amt;
    }

    public BigDecimal getR50_net_amt_bwp() {
        return r50_net_amt_bwp;
    }

    public void setR50_net_amt_bwp(BigDecimal r50_net_amt_bwp) {
        this.r50_net_amt_bwp = r50_net_amt_bwp;
    }

    public BigDecimal getR50_bal_sub() {
        return r50_bal_sub;
    }

    public void setR50_bal_sub(BigDecimal r50_bal_sub) {
        this.r50_bal_sub = r50_bal_sub;
    }

    public BigDecimal getR50_bal_sub_bwp() {
        return r50_bal_sub_bwp;
    }

    public void setR50_bal_sub_bwp(BigDecimal r50_bal_sub_bwp) {
        this.r50_bal_sub_bwp = r50_bal_sub_bwp;
    }

    public BigDecimal getR50_bal_sub_diaries_bwp() {
        return r50_bal_sub_diaries_bwp;
    }

    public void setR50_bal_sub_diaries_bwp(BigDecimal r50_bal_sub_diaries_bwp) {
        this.r50_bal_sub_diaries_bwp = r50_bal_sub_diaries_bwp;
    }

    public String getR51_operating_expenses() {
        return r51_operating_expenses;
    }

    public void setR51_operating_expenses(String r51_operating_expenses) {
        this.r51_operating_expenses = r51_operating_expenses;
    }

    public BigDecimal getR51_fig_bal_sheet() {
        return r51_fig_bal_sheet;
    }

    public void setR51_fig_bal_sheet(BigDecimal r51_fig_bal_sheet) {
        this.r51_fig_bal_sheet = r51_fig_bal_sheet;
    }

    public BigDecimal getR51_fig_bal_sheet_bwp() {
        return r51_fig_bal_sheet_bwp;
    }

    public void setR51_fig_bal_sheet_bwp(BigDecimal r51_fig_bal_sheet_bwp) {
        this.r51_fig_bal_sheet_bwp = r51_fig_bal_sheet_bwp;
    }

    public BigDecimal getR51_amt_statement_adj() {
        return r51_amt_statement_adj;
    }

    public void setR51_amt_statement_adj(BigDecimal r51_amt_statement_adj) {
        this.r51_amt_statement_adj = r51_amt_statement_adj;
    }

    public BigDecimal getR51_amt_statement_adj_bwp() {
        return r51_amt_statement_adj_bwp;
    }

    public void setR51_amt_statement_adj_bwp(BigDecimal r51_amt_statement_adj_bwp) {
        this.r51_amt_statement_adj_bwp = r51_amt_statement_adj_bwp;
    }

    public BigDecimal getR51_net_amt() {
        return r51_net_amt;
    }

    public void setR51_net_amt(BigDecimal r51_net_amt) {
        this.r51_net_amt = r51_net_amt;
    }

    public BigDecimal getR51_net_amt_bwp() {
        return r51_net_amt_bwp;
    }

    public void setR51_net_amt_bwp(BigDecimal r51_net_amt_bwp) {
        this.r51_net_amt_bwp = r51_net_amt_bwp;
    }

    public BigDecimal getR51_bal_sub() {
        return r51_bal_sub;
    }

    public void setR51_bal_sub(BigDecimal r51_bal_sub) {
        this.r51_bal_sub = r51_bal_sub;
    }

    public BigDecimal getR51_bal_sub_bwp() {
        return r51_bal_sub_bwp;
    }

    public void setR51_bal_sub_bwp(BigDecimal r51_bal_sub_bwp) {
        this.r51_bal_sub_bwp = r51_bal_sub_bwp;
    }

    public BigDecimal getR51_bal_sub_diaries_bwp() {
        return r51_bal_sub_diaries_bwp;
    }

    public void setR51_bal_sub_diaries_bwp(BigDecimal r51_bal_sub_diaries_bwp) {
        this.r51_bal_sub_diaries_bwp = r51_bal_sub_diaries_bwp;
    }

    public String getR52_operating_expenses() {
        return r52_operating_expenses;
    }

    public void setR52_operating_expenses(String r52_operating_expenses) {
        this.r52_operating_expenses = r52_operating_expenses;
    }

    public BigDecimal getR52_fig_bal_sheet() {
        return r52_fig_bal_sheet;
    }

    public void setR52_fig_bal_sheet(BigDecimal r52_fig_bal_sheet) {
        this.r52_fig_bal_sheet = r52_fig_bal_sheet;
    }

    public BigDecimal getR52_fig_bal_sheet_bwp() {
        return r52_fig_bal_sheet_bwp;
    }

    public void setR52_fig_bal_sheet_bwp(BigDecimal r52_fig_bal_sheet_bwp) {
        this.r52_fig_bal_sheet_bwp = r52_fig_bal_sheet_bwp;
    }

    public BigDecimal getR52_amt_statement_adj() {
        return r52_amt_statement_adj;
    }

    public void setR52_amt_statement_adj(BigDecimal r52_amt_statement_adj) {
        this.r52_amt_statement_adj = r52_amt_statement_adj;
    }

    public BigDecimal getR52_amt_statement_adj_bwp() {
        return r52_amt_statement_adj_bwp;
    }

    public void setR52_amt_statement_adj_bwp(BigDecimal r52_amt_statement_adj_bwp) {
        this.r52_amt_statement_adj_bwp = r52_amt_statement_adj_bwp;
    }

    public BigDecimal getR52_net_amt() {
        return r52_net_amt;
    }

    public void setR52_net_amt(BigDecimal r52_net_amt) {
        this.r52_net_amt = r52_net_amt;
    }

    public BigDecimal getR52_net_amt_bwp() {
        return r52_net_amt_bwp;
    }

    public void setR52_net_amt_bwp(BigDecimal r52_net_amt_bwp) {
        this.r52_net_amt_bwp = r52_net_amt_bwp;
    }

    public BigDecimal getR52_bal_sub() {
        return r52_bal_sub;
    }

    public void setR52_bal_sub(BigDecimal r52_bal_sub) {
        this.r52_bal_sub = r52_bal_sub;
    }

    public BigDecimal getR52_bal_sub_bwp() {
        return r52_bal_sub_bwp;
    }

    public void setR52_bal_sub_bwp(BigDecimal r52_bal_sub_bwp) {
        this.r52_bal_sub_bwp = r52_bal_sub_bwp;
    }

    public BigDecimal getR52_bal_sub_diaries_bwp() {
        return r52_bal_sub_diaries_bwp;
    }

    public void setR52_bal_sub_diaries_bwp(BigDecimal r52_bal_sub_diaries_bwp) {
        this.r52_bal_sub_diaries_bwp = r52_bal_sub_diaries_bwp;
    }

    public String getR53_operating_expenses() {
        return r53_operating_expenses;
    }

    public void setR53_operating_expenses(String r53_operating_expenses) {
        this.r53_operating_expenses = r53_operating_expenses;
    }

    public BigDecimal getR53_fig_bal_sheet() {
        return r53_fig_bal_sheet;
    }

    public void setR53_fig_bal_sheet(BigDecimal r53_fig_bal_sheet) {
        this.r53_fig_bal_sheet = r53_fig_bal_sheet;
    }

    public BigDecimal getR53_fig_bal_sheet_bwp() {
        return r53_fig_bal_sheet_bwp;
    }

    public void setR53_fig_bal_sheet_bwp(BigDecimal r53_fig_bal_sheet_bwp) {
        this.r53_fig_bal_sheet_bwp = r53_fig_bal_sheet_bwp;
    }

    public BigDecimal getR53_amt_statement_adj() {
        return r53_amt_statement_adj;
    }

    public void setR53_amt_statement_adj(BigDecimal r53_amt_statement_adj) {
        this.r53_amt_statement_adj = r53_amt_statement_adj;
    }

    public BigDecimal getR53_amt_statement_adj_bwp() {
        return r53_amt_statement_adj_bwp;
    }

    public void setR53_amt_statement_adj_bwp(BigDecimal r53_amt_statement_adj_bwp) {
        this.r53_amt_statement_adj_bwp = r53_amt_statement_adj_bwp;
    }

    public BigDecimal getR53_net_amt() {
        return r53_net_amt;
    }

    public void setR53_net_amt(BigDecimal r53_net_amt) {
        this.r53_net_amt = r53_net_amt;
    }

    public BigDecimal getR53_net_amt_bwp() {
        return r53_net_amt_bwp;
    }

    public void setR53_net_amt_bwp(BigDecimal r53_net_amt_bwp) {
        this.r53_net_amt_bwp = r53_net_amt_bwp;
    }

    public BigDecimal getR53_bal_sub() {
        return r53_bal_sub;
    }

    public void setR53_bal_sub(BigDecimal r53_bal_sub) {
        this.r53_bal_sub = r53_bal_sub;
    }

    public BigDecimal getR53_bal_sub_bwp() {
        return r53_bal_sub_bwp;
    }

    public void setR53_bal_sub_bwp(BigDecimal r53_bal_sub_bwp) {
        this.r53_bal_sub_bwp = r53_bal_sub_bwp;
    }

    public BigDecimal getR53_bal_sub_diaries_bwp() {
        return r53_bal_sub_diaries_bwp;
    }

    public void setR53_bal_sub_diaries_bwp(BigDecimal r53_bal_sub_diaries_bwp) {
        this.r53_bal_sub_diaries_bwp = r53_bal_sub_diaries_bwp;
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

    public BigDecimal getR54_bal_sub_diaries_bwp() {
        return r54_bal_sub_diaries_bwp;
    }

    public void setR54_bal_sub_diaries_bwp(BigDecimal r54_bal_sub_diaries_bwp) {
        this.r54_bal_sub_diaries_bwp = r54_bal_sub_diaries_bwp;
    }

    public String getR55_operating_expenses() {
        return r55_operating_expenses;
    }

    public void setR55_operating_expenses(String r55_operating_expenses) {
        this.r55_operating_expenses = r55_operating_expenses;
    }

    public BigDecimal getR55_fig_bal_sheet() {
        return r55_fig_bal_sheet;
    }

    public void setR55_fig_bal_sheet(BigDecimal r55_fig_bal_sheet) {
        this.r55_fig_bal_sheet = r55_fig_bal_sheet;
    }

    public BigDecimal getR55_fig_bal_sheet_bwp() {
        return r55_fig_bal_sheet_bwp;
    }

    public void setR55_fig_bal_sheet_bwp(BigDecimal r55_fig_bal_sheet_bwp) {
        this.r55_fig_bal_sheet_bwp = r55_fig_bal_sheet_bwp;
    }

    public BigDecimal getR55_amt_statement_adj() {
        return r55_amt_statement_adj;
    }

    public void setR55_amt_statement_adj(BigDecimal r55_amt_statement_adj) {
        this.r55_amt_statement_adj = r55_amt_statement_adj;
    }

    public BigDecimal getR55_amt_statement_adj_bwp() {
        return r55_amt_statement_adj_bwp;
    }

    public void setR55_amt_statement_adj_bwp(BigDecimal r55_amt_statement_adj_bwp) {
        this.r55_amt_statement_adj_bwp = r55_amt_statement_adj_bwp;
    }

    public BigDecimal getR55_net_amt() {
        return r55_net_amt;
    }

    public void setR55_net_amt(BigDecimal r55_net_amt) {
        this.r55_net_amt = r55_net_amt;
    }

    public BigDecimal getR55_net_amt_bwp() {
        return r55_net_amt_bwp;
    }

    public void setR55_net_amt_bwp(BigDecimal r55_net_amt_bwp) {
        this.r55_net_amt_bwp = r55_net_amt_bwp;
    }

    public BigDecimal getR55_bal_sub() {
        return r55_bal_sub;
    }

    public void setR55_bal_sub(BigDecimal r55_bal_sub) {
        this.r55_bal_sub = r55_bal_sub;
    }

    public BigDecimal getR55_bal_sub_bwp() {
        return r55_bal_sub_bwp;
    }

    public void setR55_bal_sub_bwp(BigDecimal r55_bal_sub_bwp) {
        this.r55_bal_sub_bwp = r55_bal_sub_bwp;
    }

    public BigDecimal getR55_bal_sub_diaries_bwp() {
        return r55_bal_sub_diaries_bwp;
    }

    public void setR55_bal_sub_diaries_bwp(BigDecimal r55_bal_sub_diaries_bwp) {
        this.r55_bal_sub_diaries_bwp = r55_bal_sub_diaries_bwp;
    }

    public String getR56_operating_expenses() {
        return r56_operating_expenses;
    }

    public void setR56_operating_expenses(String r56_operating_expenses) {
        this.r56_operating_expenses = r56_operating_expenses;
    }

    public BigDecimal getR56_fig_bal_sheet() {
        return r56_fig_bal_sheet;
    }

    public void setR56_fig_bal_sheet(BigDecimal r56_fig_bal_sheet) {
        this.r56_fig_bal_sheet = r56_fig_bal_sheet;
    }

    public BigDecimal getR56_fig_bal_sheet_bwp() {
        return r56_fig_bal_sheet_bwp;
    }

    public void setR56_fig_bal_sheet_bwp(BigDecimal r56_fig_bal_sheet_bwp) {
        this.r56_fig_bal_sheet_bwp = r56_fig_bal_sheet_bwp;
    }

    public BigDecimal getR56_amt_statement_adj() {
        return r56_amt_statement_adj;
    }

    public void setR56_amt_statement_adj(BigDecimal r56_amt_statement_adj) {
        this.r56_amt_statement_adj = r56_amt_statement_adj;
    }

    public BigDecimal getR56_amt_statement_adj_bwp() {
        return r56_amt_statement_adj_bwp;
    }

    public void setR56_amt_statement_adj_bwp(BigDecimal r56_amt_statement_adj_bwp) {
        this.r56_amt_statement_adj_bwp = r56_amt_statement_adj_bwp;
    }

    public BigDecimal getR56_net_amt() {
        return r56_net_amt;
    }

    public void setR56_net_amt(BigDecimal r56_net_amt) {
        this.r56_net_amt = r56_net_amt;
    }

    public BigDecimal getR56_net_amt_bwp() {
        return r56_net_amt_bwp;
    }

    public void setR56_net_amt_bwp(BigDecimal r56_net_amt_bwp) {
        this.r56_net_amt_bwp = r56_net_amt_bwp;
    }

    public BigDecimal getR56_bal_sub() {
        return r56_bal_sub;
    }

    public void setR56_bal_sub(BigDecimal r56_bal_sub) {
        this.r56_bal_sub = r56_bal_sub;
    }

    public BigDecimal getR56_bal_sub_bwp() {
        return r56_bal_sub_bwp;
    }

    public void setR56_bal_sub_bwp(BigDecimal r56_bal_sub_bwp) {
        this.r56_bal_sub_bwp = r56_bal_sub_bwp;
    }

    public BigDecimal getR56_bal_sub_diaries_bwp() {
        return r56_bal_sub_diaries_bwp;
    }

    public void setR56_bal_sub_diaries_bwp(BigDecimal r56_bal_sub_diaries_bwp) {
        this.r56_bal_sub_diaries_bwp = r56_bal_sub_diaries_bwp;
    }

    public String getR57_operating_expenses() {
        return r57_operating_expenses;
    }

    public void setR57_operating_expenses(String r57_operating_expenses) {
        this.r57_operating_expenses = r57_operating_expenses;
    }

    public BigDecimal getR57_fig_bal_sheet() {
        return r57_fig_bal_sheet;
    }

    public void setR57_fig_bal_sheet(BigDecimal r57_fig_bal_sheet) {
        this.r57_fig_bal_sheet = r57_fig_bal_sheet;
    }

    public BigDecimal getR57_fig_bal_sheet_bwp() {
        return r57_fig_bal_sheet_bwp;
    }

    public void setR57_fig_bal_sheet_bwp(BigDecimal r57_fig_bal_sheet_bwp) {
        this.r57_fig_bal_sheet_bwp = r57_fig_bal_sheet_bwp;
    }

    public BigDecimal getR57_amt_statement_adj() {
        return r57_amt_statement_adj;
    }

    public void setR57_amt_statement_adj(BigDecimal r57_amt_statement_adj) {
        this.r57_amt_statement_adj = r57_amt_statement_adj;
    }

    public BigDecimal getR57_amt_statement_adj_bwp() {
        return r57_amt_statement_adj_bwp;
    }

    public void setR57_amt_statement_adj_bwp(BigDecimal r57_amt_statement_adj_bwp) {
        this.r57_amt_statement_adj_bwp = r57_amt_statement_adj_bwp;
    }

    public BigDecimal getR57_net_amt() {
        return r57_net_amt;
    }

    public void setR57_net_amt(BigDecimal r57_net_amt) {
        this.r57_net_amt = r57_net_amt;
    }

    public BigDecimal getR57_net_amt_bwp() {
        return r57_net_amt_bwp;
    }

    public void setR57_net_amt_bwp(BigDecimal r57_net_amt_bwp) {
        this.r57_net_amt_bwp = r57_net_amt_bwp;
    }

    public BigDecimal getR57_bal_sub() {
        return r57_bal_sub;
    }

    public void setR57_bal_sub(BigDecimal r57_bal_sub) {
        this.r57_bal_sub = r57_bal_sub;
    }

    public BigDecimal getR57_bal_sub_bwp() {
        return r57_bal_sub_bwp;
    }

    public void setR57_bal_sub_bwp(BigDecimal r57_bal_sub_bwp) {
        this.r57_bal_sub_bwp = r57_bal_sub_bwp;
    }

    public BigDecimal getR57_bal_sub_diaries_bwp() {
        return r57_bal_sub_diaries_bwp;
    }

    public void setR57_bal_sub_diaries_bwp(BigDecimal r57_bal_sub_diaries_bwp) {
        this.r57_bal_sub_diaries_bwp = r57_bal_sub_diaries_bwp;
    }

    public String getR58_operating_expenses() {
        return r58_operating_expenses;
    }

    public void setR58_operating_expenses(String r58_operating_expenses) {
        this.r58_operating_expenses = r58_operating_expenses;
    }

    public BigDecimal getR58_fig_bal_sheet() {
        return r58_fig_bal_sheet;
    }

    public void setR58_fig_bal_sheet(BigDecimal r58_fig_bal_sheet) {
        this.r58_fig_bal_sheet = r58_fig_bal_sheet;
    }

    public BigDecimal getR58_fig_bal_sheet_bwp() {
        return r58_fig_bal_sheet_bwp;
    }

    public void setR58_fig_bal_sheet_bwp(BigDecimal r58_fig_bal_sheet_bwp) {
        this.r58_fig_bal_sheet_bwp = r58_fig_bal_sheet_bwp;
    }

    public BigDecimal getR58_amt_statement_adj() {
        return r58_amt_statement_adj;
    }

    public void setR58_amt_statement_adj(BigDecimal r58_amt_statement_adj) {
        this.r58_amt_statement_adj = r58_amt_statement_adj;
    }

    public BigDecimal getR58_amt_statement_adj_bwp() {
        return r58_amt_statement_adj_bwp;
    }

    public void setR58_amt_statement_adj_bwp(BigDecimal r58_amt_statement_adj_bwp) {
        this.r58_amt_statement_adj_bwp = r58_amt_statement_adj_bwp;
    }

    public BigDecimal getR58_net_amt() {
        return r58_net_amt;
    }

    public void setR58_net_amt(BigDecimal r58_net_amt) {
        this.r58_net_amt = r58_net_amt;
    }

    public BigDecimal getR58_net_amt_bwp() {
        return r58_net_amt_bwp;
    }

    public void setR58_net_amt_bwp(BigDecimal r58_net_amt_bwp) {
        this.r58_net_amt_bwp = r58_net_amt_bwp;
    }

    public BigDecimal getR58_bal_sub() {
        return r58_bal_sub;
    }

    public void setR58_bal_sub(BigDecimal r58_bal_sub) {
        this.r58_bal_sub = r58_bal_sub;
    }

    public BigDecimal getR58_bal_sub_bwp() {
        return r58_bal_sub_bwp;
    }

    public void setR58_bal_sub_bwp(BigDecimal r58_bal_sub_bwp) {
        this.r58_bal_sub_bwp = r58_bal_sub_bwp;
    }

    public BigDecimal getR58_bal_sub_diaries_bwp() {
        return r58_bal_sub_diaries_bwp;
    }

    public void setR58_bal_sub_diaries_bwp(BigDecimal r58_bal_sub_diaries_bwp) {
        this.r58_bal_sub_diaries_bwp = r58_bal_sub_diaries_bwp;
    }

    public String getR59_operating_expenses() {
        return r59_operating_expenses;
    }

    public void setR59_operating_expenses(String r59_operating_expenses) {
        this.r59_operating_expenses = r59_operating_expenses;
    }

    public BigDecimal getR59_fig_bal_sheet() {
        return r59_fig_bal_sheet;
    }

    public void setR59_fig_bal_sheet(BigDecimal r59_fig_bal_sheet) {
        this.r59_fig_bal_sheet = r59_fig_bal_sheet;
    }

    public BigDecimal getR59_fig_bal_sheet_bwp() {
        return r59_fig_bal_sheet_bwp;
    }

    public void setR59_fig_bal_sheet_bwp(BigDecimal r59_fig_bal_sheet_bwp) {
        this.r59_fig_bal_sheet_bwp = r59_fig_bal_sheet_bwp;
    }

    public BigDecimal getR59_amt_statement_adj() {
        return r59_amt_statement_adj;
    }

    public void setR59_amt_statement_adj(BigDecimal r59_amt_statement_adj) {
        this.r59_amt_statement_adj = r59_amt_statement_adj;
    }

    public BigDecimal getR59_amt_statement_adj_bwp() {
        return r59_amt_statement_adj_bwp;
    }

    public void setR59_amt_statement_adj_bwp(BigDecimal r59_amt_statement_adj_bwp) {
        this.r59_amt_statement_adj_bwp = r59_amt_statement_adj_bwp;
    }

    public BigDecimal getR59_net_amt() {
        return r59_net_amt;
    }

    public void setR59_net_amt(BigDecimal r59_net_amt) {
        this.r59_net_amt = r59_net_amt;
    }

    public BigDecimal getR59_net_amt_bwp() {
        return r59_net_amt_bwp;
    }

    public void setR59_net_amt_bwp(BigDecimal r59_net_amt_bwp) {
        this.r59_net_amt_bwp = r59_net_amt_bwp;
    }

    public BigDecimal getR59_bal_sub() {
        return r59_bal_sub;
    }

    public void setR59_bal_sub(BigDecimal r59_bal_sub) {
        this.r59_bal_sub = r59_bal_sub;
    }

    public BigDecimal getR59_bal_sub_bwp() {
        return r59_bal_sub_bwp;
    }

    public void setR59_bal_sub_bwp(BigDecimal r59_bal_sub_bwp) {
        this.r59_bal_sub_bwp = r59_bal_sub_bwp;
    }

    public BigDecimal getR59_bal_sub_diaries_bwp() {
        return r59_bal_sub_diaries_bwp;
    }

    public void setR59_bal_sub_diaries_bwp(BigDecimal r59_bal_sub_diaries_bwp) {
        this.r59_bal_sub_diaries_bwp = r59_bal_sub_diaries_bwp;
    }

    public String getR60_operating_expenses() {
        return r60_operating_expenses;
    }

    public void setR60_operating_expenses(String r60_operating_expenses) {
        this.r60_operating_expenses = r60_operating_expenses;
    }

    public BigDecimal getR60_fig_bal_sheet() {
        return r60_fig_bal_sheet;
    }

    public void setR60_fig_bal_sheet(BigDecimal r60_fig_bal_sheet) {
        this.r60_fig_bal_sheet = r60_fig_bal_sheet;
    }

    public BigDecimal getR60_fig_bal_sheet_bwp() {
        return r60_fig_bal_sheet_bwp;
    }

    public void setR60_fig_bal_sheet_bwp(BigDecimal r60_fig_bal_sheet_bwp) {
        this.r60_fig_bal_sheet_bwp = r60_fig_bal_sheet_bwp;
    }

    public BigDecimal getR60_amt_statement_adj() {
        return r60_amt_statement_adj;
    }

    public void setR60_amt_statement_adj(BigDecimal r60_amt_statement_adj) {
        this.r60_amt_statement_adj = r60_amt_statement_adj;
    }

    public BigDecimal getR60_amt_statement_adj_bwp() {
        return r60_amt_statement_adj_bwp;
    }

    public void setR60_amt_statement_adj_bwp(BigDecimal r60_amt_statement_adj_bwp) {
        this.r60_amt_statement_adj_bwp = r60_amt_statement_adj_bwp;
    }

    public BigDecimal getR60_net_amt() {
        return r60_net_amt;
    }

    public void setR60_net_amt(BigDecimal r60_net_amt) {
        this.r60_net_amt = r60_net_amt;
    }

    public BigDecimal getR60_net_amt_bwp() {
        return r60_net_amt_bwp;
    }

    public void setR60_net_amt_bwp(BigDecimal r60_net_amt_bwp) {
        this.r60_net_amt_bwp = r60_net_amt_bwp;
    }

    public BigDecimal getR60_bal_sub() {
        return r60_bal_sub;
    }

    public void setR60_bal_sub(BigDecimal r60_bal_sub) {
        this.r60_bal_sub = r60_bal_sub;
    }

    public BigDecimal getR60_bal_sub_bwp() {
        return r60_bal_sub_bwp;
    }

    public void setR60_bal_sub_bwp(BigDecimal r60_bal_sub_bwp) {
        this.r60_bal_sub_bwp = r60_bal_sub_bwp;
    }

    public BigDecimal getR60_bal_sub_diaries_bwp() {
        return r60_bal_sub_diaries_bwp;
    }

    public void setR60_bal_sub_diaries_bwp(BigDecimal r60_bal_sub_diaries_bwp) {
        this.r60_bal_sub_diaries_bwp = r60_bal_sub_diaries_bwp;
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

    public BigDecimal getR61_bal_sub_diaries_bwp() {
        return r61_bal_sub_diaries_bwp;
    }

    public void setR61_bal_sub_diaries_bwp(BigDecimal r61_bal_sub_diaries_bwp) {
        this.r61_bal_sub_diaries_bwp = r61_bal_sub_diaries_bwp;
    }

    public String getR62_operating_expenses() {
        return r62_operating_expenses;
    }

    public void setR62_operating_expenses(String r62_operating_expenses) {
        this.r62_operating_expenses = r62_operating_expenses;
    }

    public BigDecimal getR62_fig_bal_sheet() {
        return r62_fig_bal_sheet;
    }

    public void setR62_fig_bal_sheet(BigDecimal r62_fig_bal_sheet) {
        this.r62_fig_bal_sheet = r62_fig_bal_sheet;
    }

    public BigDecimal getR62_fig_bal_sheet_bwp() {
        return r62_fig_bal_sheet_bwp;
    }

    public void setR62_fig_bal_sheet_bwp(BigDecimal r62_fig_bal_sheet_bwp) {
        this.r62_fig_bal_sheet_bwp = r62_fig_bal_sheet_bwp;
    }

    public BigDecimal getR62_amt_statement_adj() {
        return r62_amt_statement_adj;
    }

    public void setR62_amt_statement_adj(BigDecimal r62_amt_statement_adj) {
        this.r62_amt_statement_adj = r62_amt_statement_adj;
    }

    public BigDecimal getR62_amt_statement_adj_bwp() {
        return r62_amt_statement_adj_bwp;
    }

    public void setR62_amt_statement_adj_bwp(BigDecimal r62_amt_statement_adj_bwp) {
        this.r62_amt_statement_adj_bwp = r62_amt_statement_adj_bwp;
    }

    public BigDecimal getR62_net_amt() {
        return r62_net_amt;
    }

    public void setR62_net_amt(BigDecimal r62_net_amt) {
        this.r62_net_amt = r62_net_amt;
    }

    public BigDecimal getR62_net_amt_bwp() {
        return r62_net_amt_bwp;
    }

    public void setR62_net_amt_bwp(BigDecimal r62_net_amt_bwp) {
        this.r62_net_amt_bwp = r62_net_amt_bwp;
    }

    public BigDecimal getR62_bal_sub() {
        return r62_bal_sub;
    }

    public void setR62_bal_sub(BigDecimal r62_bal_sub) {
        this.r62_bal_sub = r62_bal_sub;
    }

    public BigDecimal getR62_bal_sub_bwp() {
        return r62_bal_sub_bwp;
    }

    public void setR62_bal_sub_bwp(BigDecimal r62_bal_sub_bwp) {
        this.r62_bal_sub_bwp = r62_bal_sub_bwp;
    }

    public BigDecimal getR62_bal_sub_diaries_bwp() {
        return r62_bal_sub_diaries_bwp;
    }

    public void setR62_bal_sub_diaries_bwp(BigDecimal r62_bal_sub_diaries_bwp) {
        this.r62_bal_sub_diaries_bwp = r62_bal_sub_diaries_bwp;
    }

    public String getR63_operating_expenses() {
        return r63_operating_expenses;
    }

    public void setR63_operating_expenses(String r63_operating_expenses) {
        this.r63_operating_expenses = r63_operating_expenses;
    }

    public BigDecimal getR63_fig_bal_sheet() {
        return r63_fig_bal_sheet;
    }

    public void setR63_fig_bal_sheet(BigDecimal r63_fig_bal_sheet) {
        this.r63_fig_bal_sheet = r63_fig_bal_sheet;
    }

    public BigDecimal getR63_fig_bal_sheet_bwp() {
        return r63_fig_bal_sheet_bwp;
    }

    public void setR63_fig_bal_sheet_bwp(BigDecimal r63_fig_bal_sheet_bwp) {
        this.r63_fig_bal_sheet_bwp = r63_fig_bal_sheet_bwp;
    }

    public BigDecimal getR63_amt_statement_adj() {
        return r63_amt_statement_adj;
    }

    public void setR63_amt_statement_adj(BigDecimal r63_amt_statement_adj) {
        this.r63_amt_statement_adj = r63_amt_statement_adj;
    }

    public BigDecimal getR63_amt_statement_adj_bwp() {
        return r63_amt_statement_adj_bwp;
    }

    public void setR63_amt_statement_adj_bwp(BigDecimal r63_amt_statement_adj_bwp) {
        this.r63_amt_statement_adj_bwp = r63_amt_statement_adj_bwp;
    }

    public BigDecimal getR63_net_amt() {
        return r63_net_amt;
    }

    public void setR63_net_amt(BigDecimal r63_net_amt) {
        this.r63_net_amt = r63_net_amt;
    }

    public BigDecimal getR63_net_amt_bwp() {
        return r63_net_amt_bwp;
    }

    public void setR63_net_amt_bwp(BigDecimal r63_net_amt_bwp) {
        this.r63_net_amt_bwp = r63_net_amt_bwp;
    }

    public BigDecimal getR63_bal_sub() {
        return r63_bal_sub;
    }

    public void setR63_bal_sub(BigDecimal r63_bal_sub) {
        this.r63_bal_sub = r63_bal_sub;
    }

    public BigDecimal getR63_bal_sub_bwp() {
        return r63_bal_sub_bwp;
    }

    public void setR63_bal_sub_bwp(BigDecimal r63_bal_sub_bwp) {
        this.r63_bal_sub_bwp = r63_bal_sub_bwp;
    }

    public BigDecimal getR63_bal_sub_diaries_bwp() {
        return r63_bal_sub_diaries_bwp;
    }

    public void setR63_bal_sub_diaries_bwp(BigDecimal r63_bal_sub_diaries_bwp) {
        this.r63_bal_sub_diaries_bwp = r63_bal_sub_diaries_bwp;
    }

    public Date getREPORT_DATE() {
        return REPORT_DATE;
    }

    public void setREPORT_DATE(Date rEPORT_DATE) {
        REPORT_DATE = rEPORT_DATE;
    }

    public String getREPORT_VERSION() {
        return REPORT_VERSION;
    }

    public void setREPORT_VERSION(String rEPORT_VERSION) {
        REPORT_VERSION = rEPORT_VERSION;
    }

    public String getREPORT_FREQUENCY() {
        return REPORT_FREQUENCY;
    }

    public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
        REPORT_FREQUENCY = rEPORT_FREQUENCY;
    }

    public String getREPORT_CODE() {
        return REPORT_CODE;
    }

    public void setREPORT_CODE(String rEPORT_CODE) {
        REPORT_CODE = rEPORT_CODE;
    }

    public String getREPORT_DESC() {
        return REPORT_DESC;
    }

    public void setREPORT_DESC(String rEPORT_DESC) {
        REPORT_DESC = rEPORT_DESC;
    }

    public String getENTITY_FLG() {
        return ENTITY_FLG;
    }

    public void setENTITY_FLG(String eNTITY_FLG) {
        ENTITY_FLG = eNTITY_FLG;
    }

    public String getMODIFY_FLG() {
        return MODIFY_FLG;
    }

    public void setMODIFY_FLG(String mODIFY_FLG) {
        MODIFY_FLG = mODIFY_FLG;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String dEL_FLG) {
        DEL_FLG = dEL_FLG;
    }

    public PL_SCHS_Summary_Entity() {
        super();
    }

    public BigDecimal getR17_bal_sub_diaries() {
        return r17_bal_sub_diaries;
    }

    public void setR17_bal_sub_diaries(BigDecimal r17_bal_sub_diaries) {
        this.r17_bal_sub_diaries = r17_bal_sub_diaries;
    }

    public BigDecimal getR48_bal_sub_diaries() {
        return r48_bal_sub_diaries;
    }

    public void setR48_bal_sub_diaries(BigDecimal r48_bal_sub_diaries) {
        this.r48_bal_sub_diaries = r48_bal_sub_diaries;
    }

}
