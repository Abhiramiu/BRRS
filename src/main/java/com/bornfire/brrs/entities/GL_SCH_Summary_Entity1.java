package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_GL_SCH_SUMMARYTABLE1", schema = "BRRS")

public class GL_SCH_Summary_Entity1 {

    /* ================= R11 ================= */
    @Column(name = "R11_PRODUCT")
    private String R11_PRODUCT;

    @Column(name = "R11_FIG_BAL_BWP1")
    private BigDecimal R11_FIG_BAL_BWP1;

    @Column(name = "R11_FIG_BAL_BWP2")
    private BigDecimal R11_FIG_BAL_BWP2;

    @Column(name = "R11_AMT_ADJ_BWP1")
    private BigDecimal R11_AMT_ADJ_BWP1;

    @Column(name = "R11_AMT_ADJ_BWP2")
    private BigDecimal R11_AMT_ADJ_BWP2;

    @Column(name = "R11_NET_AMT_BWP1")
    private BigDecimal R11_NET_AMT_BWP1;

    @Column(name = "R11_NET_AMT_BWP2")
    private BigDecimal R11_NET_AMT_BWP2;

    @Column(name = "R11_BAL_SUB_BWP1")
    private BigDecimal R11_BAL_SUB_BWP1;

    @Column(name = "R11_BAL_SUB_BWP2")
    private BigDecimal R11_BAL_SUB_BWP2;

    @Column(name = "R11_BAL_ACT_SUB_BWP1")
    private BigDecimal R11_BAL_ACT_SUB_BWP1;

    @Column(name = "R11_BAL_ACT_SUB_BWP2")
    private BigDecimal R11_BAL_ACT_SUB_BWP2;

    /* ================= R12 ================= */
    @Column(name = "R12_PRODUCT")
    private String R12_PRODUCT;

    @Column(name = "R12_FIG_BAL_BWP1")
    private BigDecimal R12_FIG_BAL_BWP1;

    @Column(name = "R12_FIG_BAL_BWP2")
    private BigDecimal R12_FIG_BAL_BWP2;

    @Column(name = "R12_AMT_ADJ_BWP1")
    private BigDecimal R12_AMT_ADJ_BWP1;

    @Column(name = "R12_AMT_ADJ_BWP2")
    private BigDecimal R12_AMT_ADJ_BWP2;

    @Column(name = "R12_NET_AMT_BWP1")
    private BigDecimal R12_NET_AMT_BWP1;

    @Column(name = "R12_NET_AMT_BWP2")
    private BigDecimal R12_NET_AMT_BWP2;

    @Column(name = "R12_BAL_SUB_BWP1")
    private BigDecimal R12_BAL_SUB_BWP1;

    @Column(name = "R12_BAL_SUB_BWP2")
    private BigDecimal R12_BAL_SUB_BWP2;

    @Column(name = "R12_BAL_ACT_SUB_BWP1")
    private BigDecimal R12_BAL_ACT_SUB_BWP1;

    @Column(name = "R12_BAL_ACT_SUB_BWP2")
    private BigDecimal R12_BAL_ACT_SUB_BWP2;

    /* ================= R13 ================= */
    @Column(name = "R13_PRODUCT")
    private String R13_PRODUCT;

    @Column(name = "R13_FIG_BAL_BWP1")
    private BigDecimal R13_FIG_BAL_BWP1;

    @Column(name = "R13_FIG_BAL_BWP2")
    private BigDecimal R13_FIG_BAL_BWP2;

    @Column(name = "R13_AMT_ADJ_BWP1")
    private BigDecimal R13_AMT_ADJ_BWP1;

    @Column(name = "R13_AMT_ADJ_BWP2")
    private BigDecimal R13_AMT_ADJ_BWP2;

    @Column(name = "R13_NET_AMT_BWP1")
    private BigDecimal R13_NET_AMT_BWP1;

    @Column(name = "R13_NET_AMT_BWP2")
    private BigDecimal R13_NET_AMT_BWP2;

    @Column(name = "R13_BAL_SUB_BWP1")
    private BigDecimal R13_BAL_SUB_BWP1;

    @Column(name = "R13_BAL_SUB_BWP2")
    private BigDecimal R13_BAL_SUB_BWP2;

    @Column(name = "R13_BAL_ACT_SUB_BWP1")
    private BigDecimal R13_BAL_ACT_SUB_BWP1;

    @Column(name = "R13_BAL_ACT_SUB_BWP2")
    private BigDecimal R13_BAL_ACT_SUB_BWP2;

    /* ================= R14 ================= */
    @Column(name = "R14_PRODUCT")
    private String R14_PRODUCT;

    @Column(name = "R14_FIG_BAL_BWP1")
    private BigDecimal R14_FIG_BAL_BWP1;

    @Column(name = "R14_FIG_BAL_BWP2")
    private BigDecimal R14_FIG_BAL_BWP2;

    @Column(name = "R14_AMT_ADJ_BWP1")
    private BigDecimal R14_AMT_ADJ_BWP1;

    @Column(name = "R14_AMT_ADJ_BWP2")
    private BigDecimal R14_AMT_ADJ_BWP2;

    @Column(name = "R14_NET_AMT_BWP1")
    private BigDecimal R14_NET_AMT_BWP1;

    @Column(name = "R14_NET_AMT_BWP2")
    private BigDecimal R14_NET_AMT_BWP2;

    @Column(name = "R14_BAL_SUB_BWP1")
    private BigDecimal R14_BAL_SUB_BWP1;

    @Column(name = "R14_BAL_SUB_BWP2")
    private BigDecimal R14_BAL_SUB_BWP2;

    @Column(name = "R14_BAL_ACT_SUB_BWP1")
    private BigDecimal R14_BAL_ACT_SUB_BWP1;

    @Column(name = "R14_BAL_ACT_SUB_BWP2")
    private BigDecimal R14_BAL_ACT_SUB_BWP2;

    /* ================= R15 ================= */
    @Column(name = "R15_PRODUCT")
    private String R15_PRODUCT;

    @Column(name = "R15_FIG_BAL_BWP1")
    private BigDecimal R15_FIG_BAL_BWP1;

    @Column(name = "R15_FIG_BAL_BWP2")
    private BigDecimal R15_FIG_BAL_BWP2;

    @Column(name = "R15_AMT_ADJ_BWP1")
    private BigDecimal R15_AMT_ADJ_BWP1;

    @Column(name = "R15_AMT_ADJ_BWP2")
    private BigDecimal R15_AMT_ADJ_BWP2;

    @Column(name = "R15_NET_AMT_BWP1")
    private BigDecimal R15_NET_AMT_BWP1;

    @Column(name = "R15_NET_AMT_BWP2")
    private BigDecimal R15_NET_AMT_BWP2;

    @Column(name = "R15_BAL_SUB_BWP1")
    private BigDecimal R15_BAL_SUB_BWP1;

    @Column(name = "R15_BAL_SUB_BWP2")
    private BigDecimal R15_BAL_SUB_BWP2;

    @Column(name = "R15_BAL_ACT_SUB_BWP1")
    private BigDecimal R15_BAL_ACT_SUB_BWP1;

    @Column(name = "R15_BAL_ACT_SUB_BWP2")
    private BigDecimal R15_BAL_ACT_SUB_BWP2;

    /* ================= R16 ================= */
    @Column(name = "R16_PRODUCT")
    private String R16_PRODUCT;

    @Column(name = "R16_FIG_BAL_BWP1")
    private BigDecimal R16_FIG_BAL_BWP1;

    @Column(name = "R16_FIG_BAL_BWP2")
    private BigDecimal R16_FIG_BAL_BWP2;

    @Column(name = "R16_AMT_ADJ_BWP1")
    private BigDecimal R16_AMT_ADJ_BWP1;

    @Column(name = "R16_AMT_ADJ_BWP2")
    private BigDecimal R16_AMT_ADJ_BWP2;

    @Column(name = "R16_NET_AMT_BWP1")
    private BigDecimal R16_NET_AMT_BWP1;

    @Column(name = "R16_NET_AMT_BWP2")
    private BigDecimal R16_NET_AMT_BWP2;

    @Column(name = "R16_BAL_SUB_BWP1")
    private BigDecimal R16_BAL_SUB_BWP1;

    @Column(name = "R16_BAL_SUB_BWP2")
    private BigDecimal R16_BAL_SUB_BWP2;

    @Column(name = "R16_BAL_ACT_SUB_BWP1")
    private BigDecimal R16_BAL_ACT_SUB_BWP1;

    @Column(name = "R16_BAL_ACT_SUB_BWP2")
    private BigDecimal R16_BAL_ACT_SUB_BWP2;

    /* ================= R17 ================= */
    @Column(name = "R17_PRODUCT")
    private String R17_PRODUCT;

    @Column(name = "R17_FIG_BAL_BWP1")
    private BigDecimal R17_FIG_BAL_BWP1;

    @Column(name = "R17_FIG_BAL_BWP2")
    private BigDecimal R17_FIG_BAL_BWP2;

    @Column(name = "R17_AMT_ADJ_BWP1")
    private BigDecimal R17_AMT_ADJ_BWP1;

    @Column(name = "R17_AMT_ADJ_BWP2")
    private BigDecimal R17_AMT_ADJ_BWP2;

    @Column(name = "R17_NET_AMT_BWP1")
    private BigDecimal R17_NET_AMT_BWP1;

    @Column(name = "R17_NET_AMT_BWP2")
    private BigDecimal R17_NET_AMT_BWP2;

    @Column(name = "R17_BAL_SUB_BWP1")
    private BigDecimal R17_BAL_SUB_BWP1;

    @Column(name = "R17_BAL_SUB_BWP2")
    private BigDecimal R17_BAL_SUB_BWP2;

    @Column(name = "R17_BAL_ACT_SUB_BWP1")
    private BigDecimal R17_BAL_ACT_SUB_BWP1;

    @Column(name = "R17_BAL_ACT_SUB_BWP2")
    private BigDecimal R17_BAL_ACT_SUB_BWP2;

    /* ================= R18 ================= */
    @Column(name = "R18_PRODUCT")
    private String R18_PRODUCT;

    @Column(name = "R18_FIG_BAL_BWP1")
    private BigDecimal R18_FIG_BAL_BWP1;

    @Column(name = "R18_FIG_BAL_BWP2")
    private BigDecimal R18_FIG_BAL_BWP2;

    @Column(name = "R18_AMT_ADJ_BWP1")
    private BigDecimal R18_AMT_ADJ_BWP1;

    @Column(name = "R18_AMT_ADJ_BWP2")
    private BigDecimal R18_AMT_ADJ_BWP2;

    @Column(name = "R18_NET_AMT_BWP1")
    private BigDecimal R18_NET_AMT_BWP1;

    @Column(name = "R18_NET_AMT_BWP2")
    private BigDecimal R18_NET_AMT_BWP2;

    @Column(name = "R18_BAL_SUB_BWP1")
    private BigDecimal R18_BAL_SUB_BWP1;

    @Column(name = "R18_BAL_SUB_BWP2")
    private BigDecimal R18_BAL_SUB_BWP2;

    @Column(name = "R18_BAL_ACT_SUB_BWP1")
    private BigDecimal R18_BAL_ACT_SUB_BWP1;

    @Column(name = "R18_BAL_ACT_SUB_BWP2")
    private BigDecimal R18_BAL_ACT_SUB_BWP2;

    /* ================= R19 ================= */
    @Column(name = "R19_PRODUCT")
    private String R19_PRODUCT;

    @Column(name = "R19_FIG_BAL_BWP1")
    private BigDecimal R19_FIG_BAL_BWP1;

    @Column(name = "R19_FIG_BAL_BWP2")
    private BigDecimal R19_FIG_BAL_BWP2;

    @Column(name = "R19_AMT_ADJ_BWP1")
    private BigDecimal R19_AMT_ADJ_BWP1;

    @Column(name = "R19_AMT_ADJ_BWP2")
    private BigDecimal R19_AMT_ADJ_BWP2;

    @Column(name = "R19_NET_AMT_BWP1")
    private BigDecimal R19_NET_AMT_BWP1;

    @Column(name = "R19_NET_AMT_BWP2")
    private BigDecimal R19_NET_AMT_BWP2;

    @Column(name = "R19_BAL_SUB_BWP1")
    private BigDecimal R19_BAL_SUB_BWP1;

    @Column(name = "R19_BAL_SUB_BWP2")
    private BigDecimal R19_BAL_SUB_BWP2;

    @Column(name = "R19_BAL_ACT_SUB_BWP1")
    private BigDecimal R19_BAL_ACT_SUB_BWP1;

    @Column(name = "R19_BAL_ACT_SUB_BWP2")
    private BigDecimal R19_BAL_ACT_SUB_BWP2;

    /* ================= R20 ================= */
    @Column(name = "R20_PRODUCT")
    private String R20_PRODUCT;

    @Column(name = "R20_FIG_BAL_BWP1")
    private BigDecimal R20_FIG_BAL_BWP1;

    @Column(name = "R20_FIG_BAL_BWP2")
    private BigDecimal R20_FIG_BAL_BWP2;

    @Column(name = "R20_AMT_ADJ_BWP1")
    private BigDecimal R20_AMT_ADJ_BWP1;

    @Column(name = "R20_AMT_ADJ_BWP2")
    private BigDecimal R20_AMT_ADJ_BWP2;

    @Column(name = "R20_NET_AMT_BWP1")
    private BigDecimal R20_NET_AMT_BWP1;

    @Column(name = "R20_NET_AMT_BWP2")
    private BigDecimal R20_NET_AMT_BWP2;

    @Column(name = "R20_BAL_SUB_BWP1")
    private BigDecimal R20_BAL_SUB_BWP1;

    @Column(name = "R20_BAL_SUB_BWP2")
    private BigDecimal R20_BAL_SUB_BWP2;

    @Column(name = "R20_BAL_ACT_SUB_BWP1")
    private BigDecimal R20_BAL_ACT_SUB_BWP1;

    @Column(name = "R20_BAL_ACT_SUB_BWP2")
    private BigDecimal R20_BAL_ACT_SUB_BWP2;

    /* ================= R21 ================= */
    @Column(name = "R21_PRODUCT")
    private String R21_PRODUCT;

    @Column(name = "R21_FIG_BAL_BWP1")
    private BigDecimal R21_FIG_BAL_BWP1;

    @Column(name = "R21_FIG_BAL_BWP2")
    private BigDecimal R21_FIG_BAL_BWP2;

    @Column(name = "R21_AMT_ADJ_BWP1")
    private BigDecimal R21_AMT_ADJ_BWP1;

    @Column(name = "R21_AMT_ADJ_BWP2")
    private BigDecimal R21_AMT_ADJ_BWP2;

    @Column(name = "R21_NET_AMT_BWP1")
    private BigDecimal R21_NET_AMT_BWP1;

    @Column(name = "R21_NET_AMT_BWP2")
    private BigDecimal R21_NET_AMT_BWP2;

    @Column(name = "R21_BAL_SUB_BWP1")
    private BigDecimal R21_BAL_SUB_BWP1;

    @Column(name = "R21_BAL_SUB_BWP2")
    private BigDecimal R21_BAL_SUB_BWP2;

    @Column(name = "R21_BAL_ACT_SUB_BWP1")
    private BigDecimal R21_BAL_ACT_SUB_BWP1;

    @Column(name = "R21_BAL_ACT_SUB_BWP2")
    private BigDecimal R21_BAL_ACT_SUB_BWP2;

    /* ================= R22 ================= */
    @Column(name = "R22_PRODUCT")
    private String R22_PRODUCT;

    @Column(name = "R22_FIG_BAL_BWP1")
    private BigDecimal R22_FIG_BAL_BWP1;

    @Column(name = "R22_FIG_BAL_BWP2")
    private BigDecimal R22_FIG_BAL_BWP2;

    @Column(name = "R22_AMT_ADJ_BWP1")
    private BigDecimal R22_AMT_ADJ_BWP1;

    @Column(name = "R22_AMT_ADJ_BWP2")
    private BigDecimal R22_AMT_ADJ_BWP2;

    @Column(name = "R22_NET_AMT_BWP1")
    private BigDecimal R22_NET_AMT_BWP1;

    @Column(name = "R22_NET_AMT_BWP2")
    private BigDecimal R22_NET_AMT_BWP2;

    @Column(name = "R22_BAL_SUB_BWP1")
    private BigDecimal R22_BAL_SUB_BWP1;

    @Column(name = "R22_BAL_SUB_BWP2")
    private BigDecimal R22_BAL_SUB_BWP2;

    @Column(name = "R22_BAL_ACT_SUB_BWP1")
    private BigDecimal R22_BAL_ACT_SUB_BWP1;

    @Column(name = "R22_BAL_ACT_SUB_BWP2")
    private BigDecimal R22_BAL_ACT_SUB_BWP2;

    /* ================= R23 ================= */
    @Column(name = "R23_PRODUCT")
    private String R23_PRODUCT;

    @Column(name = "R23_FIG_BAL_BWP1")
    private BigDecimal R23_FIG_BAL_BWP1;

    @Column(name = "R23_FIG_BAL_BWP2")
    private BigDecimal R23_FIG_BAL_BWP2;

    @Column(name = "R23_AMT_ADJ_BWP1")
    private BigDecimal R23_AMT_ADJ_BWP1;

    @Column(name = "R23_AMT_ADJ_BWP2")
    private BigDecimal R23_AMT_ADJ_BWP2;

    @Column(name = "R23_NET_AMT_BWP1")
    private BigDecimal R23_NET_AMT_BWP1;

    @Column(name = "R23_NET_AMT_BWP2")
    private BigDecimal R23_NET_AMT_BWP2;

    @Column(name = "R23_BAL_SUB_BWP1")
    private BigDecimal R23_BAL_SUB_BWP1;

    @Column(name = "R23_BAL_SUB_BWP2")
    private BigDecimal R23_BAL_SUB_BWP2;

    @Column(name = "R23_BAL_ACT_SUB_BWP1")
    private BigDecimal R23_BAL_ACT_SUB_BWP1;

    @Column(name = "R23_BAL_ACT_SUB_BWP2")
    private BigDecimal R23_BAL_ACT_SUB_BWP2;

    /* ================= R24 ================= */
    @Column(name = "R24_PRODUCT")
    private String R24_PRODUCT;

    @Column(name = "R24_FIG_BAL_BWP1")
    private BigDecimal R24_FIG_BAL_BWP1;

    @Column(name = "R24_FIG_BAL_BWP2")
    private BigDecimal R24_FIG_BAL_BWP2;

    @Column(name = "R24_AMT_ADJ_BWP1")
    private BigDecimal R24_AMT_ADJ_BWP1;

    @Column(name = "R24_AMT_ADJ_BWP2")
    private BigDecimal R24_AMT_ADJ_BWP2;

    @Column(name = "R24_NET_AMT_BWP1")
    private BigDecimal R24_NET_AMT_BWP1;

    @Column(name = "R24_NET_AMT_BWP2")
    private BigDecimal R24_NET_AMT_BWP2;

    @Column(name = "R24_BAL_SUB_BWP1")
    private BigDecimal R24_BAL_SUB_BWP1;

    @Column(name = "R24_BAL_SUB_BWP2")
    private BigDecimal R24_BAL_SUB_BWP2;

    @Column(name = "R24_BAL_ACT_SUB_BWP1")
    private BigDecimal R24_BAL_ACT_SUB_BWP1;

    @Column(name = "R24_BAL_ACT_SUB_BWP2")
    private BigDecimal R24_BAL_ACT_SUB_BWP2;

    /* ================= R25 ================= */
    @Column(name = "R25_PRODUCT")
    private String R25_PRODUCT;

    @Column(name = "R25_FIG_BAL_BWP1")
    private BigDecimal R25_FIG_BAL_BWP1;

    @Column(name = "R25_FIG_BAL_BWP2")
    private BigDecimal R25_FIG_BAL_BWP2;

    @Column(name = "R25_AMT_ADJ_BWP1")
    private BigDecimal R25_AMT_ADJ_BWP1;

    @Column(name = "R25_AMT_ADJ_BWP2")
    private BigDecimal R25_AMT_ADJ_BWP2;

    @Column(name = "R25_NET_AMT_BWP1")
    private BigDecimal R25_NET_AMT_BWP1;

    @Column(name = "R25_NET_AMT_BWP2")
    private BigDecimal R25_NET_AMT_BWP2;

    @Column(name = "R25_BAL_SUB_BWP1")
    private BigDecimal R25_BAL_SUB_BWP1;

    @Column(name = "R25_BAL_SUB_BWP2")
    private BigDecimal R25_BAL_SUB_BWP2;

    @Column(name = "R25_BAL_ACT_SUB_BWP1")
    private BigDecimal R25_BAL_ACT_SUB_BWP1;

    @Column(name = "R25_BAL_ACT_SUB_BWP2")
    private BigDecimal R25_BAL_ACT_SUB_BWP2;

    /* ================= R26 ================= */
    @Column(name = "R26_PRODUCT")
    private String R26_PRODUCT;

    @Column(name = "R26_FIG_BAL_BWP1")
    private BigDecimal R26_FIG_BAL_BWP1;

    @Column(name = "R26_FIG_BAL_BWP2")
    private BigDecimal R26_FIG_BAL_BWP2;

    @Column(name = "R26_AMT_ADJ_BWP1")
    private BigDecimal R26_AMT_ADJ_BWP1;

    @Column(name = "R26_AMT_ADJ_BWP2")
    private BigDecimal R26_AMT_ADJ_BWP2;

    @Column(name = "R26_NET_AMT_BWP1")
    private BigDecimal R26_NET_AMT_BWP1;

    @Column(name = "R26_NET_AMT_BWP2")
    private BigDecimal R26_NET_AMT_BWP2;

    @Column(name = "R26_BAL_SUB_BWP1")
    private BigDecimal R26_BAL_SUB_BWP1;

    @Column(name = "R26_BAL_SUB_BWP2")
    private BigDecimal R26_BAL_SUB_BWP2;

    @Column(name = "R26_BAL_ACT_SUB_BWP1")
    private BigDecimal R26_BAL_ACT_SUB_BWP1;

    @Column(name = "R26_BAL_ACT_SUB_BWP2")
    private BigDecimal R26_BAL_ACT_SUB_BWP2;

    /* ================= R27 ================= */
    @Column(name = "R27_PRODUCT")
    private String R27_PRODUCT;

    @Column(name = "R27_FIG_BAL_BWP1")
    private BigDecimal R27_FIG_BAL_BWP1;

    @Column(name = "R27_FIG_BAL_BWP2")
    private BigDecimal R27_FIG_BAL_BWP2;

    @Column(name = "R27_AMT_ADJ_BWP1")
    private BigDecimal R27_AMT_ADJ_BWP1;

    @Column(name = "R27_AMT_ADJ_BWP2")
    private BigDecimal R27_AMT_ADJ_BWP2;

    @Column(name = "R27_NET_AMT_BWP1")
    private BigDecimal R27_NET_AMT_BWP1;

    @Column(name = "R27_NET_AMT_BWP2")
    private BigDecimal R27_NET_AMT_BWP2;

    @Column(name = "R27_BAL_SUB_BWP1")
    private BigDecimal R27_BAL_SUB_BWP1;

    @Column(name = "R27_BAL_SUB_BWP2")
    private BigDecimal R27_BAL_SUB_BWP2;

    @Column(name = "R27_BAL_ACT_SUB_BWP1")
    private BigDecimal R27_BAL_ACT_SUB_BWP1;

    @Column(name = "R27_BAL_ACT_SUB_BWP2")
    private BigDecimal R27_BAL_ACT_SUB_BWP2;

    /* ================= R34 ================= */
    @Column(name = "R34_PRODUCT")
    private String R34_PRODUCT;

    @Column(name = "R34_FIG_BAL_BWP1")
    private BigDecimal R34_FIG_BAL_BWP1;

    @Column(name = "R34_FIG_BAL_BWP2")
    private BigDecimal R34_FIG_BAL_BWP2;

    @Column(name = "R34_AMT_ADJ_BWP1")
    private BigDecimal R34_AMT_ADJ_BWP1;

    @Column(name = "R34_AMT_ADJ_BWP2")
    private BigDecimal R34_AMT_ADJ_BWP2;

    @Column(name = "R34_NET_AMT_BWP1")
    private BigDecimal R34_NET_AMT_BWP1;

    @Column(name = "R34_NET_AMT_BWP2")
    private BigDecimal R34_NET_AMT_BWP2;

    @Column(name = "R34_BAL_SUB_BWP1")
    private BigDecimal R34_BAL_SUB_BWP1;

    @Column(name = "R34_BAL_SUB_BWP2")
    private BigDecimal R34_BAL_SUB_BWP2;

    @Column(name = "R34_BAL_ACT_SUB_BWP1")
    private BigDecimal R34_BAL_ACT_SUB_BWP1;

    @Column(name = "R34_BAL_ACT_SUB_BWP2")
    private BigDecimal R34_BAL_ACT_SUB_BWP2;

    /* ================= R35 ================= */
    @Column(name = "R35_PRODUCT")
    private String R35_PRODUCT;

    @Column(name = "R35_FIG_BAL_BWP1")
    private BigDecimal R35_FIG_BAL_BWP1;

    @Column(name = "R35_FIG_BAL_BWP2")
    private BigDecimal R35_FIG_BAL_BWP2;

    @Column(name = "R35_AMT_ADJ_BWP1")
    private BigDecimal R35_AMT_ADJ_BWP1;

    @Column(name = "R35_AMT_ADJ_BWP2")
    private BigDecimal R35_AMT_ADJ_BWP2;

    @Column(name = "R35_NET_AMT_BWP1")
    private BigDecimal R35_NET_AMT_BWP1;

    @Column(name = "R35_NET_AMT_BWP2")
    private BigDecimal R35_NET_AMT_BWP2;

    @Column(name = "R35_BAL_SUB_BWP1")
    private BigDecimal R35_BAL_SUB_BWP1;

    @Column(name = "R35_BAL_SUB_BWP2")
    private BigDecimal R35_BAL_SUB_BWP2;

    @Column(name = "R35_BAL_ACT_SUB_BWP1")
    private BigDecimal R35_BAL_ACT_SUB_BWP1;

    @Column(name = "R35_BAL_ACT_SUB_BWP2")
    private BigDecimal R35_BAL_ACT_SUB_BWP2;

    /* ================= R36 ================= */
    @Column(name = "R36_PRODUCT")
    private String R36_PRODUCT;

    @Column(name = "R36_FIG_BAL_BWP1")
    private BigDecimal R36_FIG_BAL_BWP1;

    @Column(name = "R36_FIG_BAL_BWP2")
    private BigDecimal R36_FIG_BAL_BWP2;

    @Column(name = "R36_AMT_ADJ_BWP1")
    private BigDecimal R36_AMT_ADJ_BWP1;

    @Column(name = "R36_AMT_ADJ_BWP2")
    private BigDecimal R36_AMT_ADJ_BWP2;

    @Column(name = "R36_NET_AMT_BWP1")
    private BigDecimal R36_NET_AMT_BWP1;

    @Column(name = "R36_NET_AMT_BWP2")
    private BigDecimal R36_NET_AMT_BWP2;

    @Column(name = "R36_BAL_SUB_BWP1")
    private BigDecimal R36_BAL_SUB_BWP1;

    @Column(name = "R36_BAL_SUB_BWP2")
    private BigDecimal R36_BAL_SUB_BWP2;

    @Column(name = "R36_BAL_ACT_SUB_BWP1")
    private BigDecimal R36_BAL_ACT_SUB_BWP1;

    @Column(name = "R36_BAL_ACT_SUB_BWP2")
    private BigDecimal R36_BAL_ACT_SUB_BWP2;

    /* ================= R37 ================= */
    @Column(name = "R37_PRODUCT")
    private String R37_PRODUCT;

    @Column(name = "R37_FIG_BAL_BWP1")
    private BigDecimal R37_FIG_BAL_BWP1;

    @Column(name = "R37_FIG_BAL_BWP2")
    private BigDecimal R37_FIG_BAL_BWP2;

    @Column(name = "R37_AMT_ADJ_BWP1")
    private BigDecimal R37_AMT_ADJ_BWP1;

    @Column(name = "R37_AMT_ADJ_BWP2")
    private BigDecimal R37_AMT_ADJ_BWP2;

    @Column(name = "R37_NET_AMT_BWP1")
    private BigDecimal R37_NET_AMT_BWP1;

    @Column(name = "R37_NET_AMT_BWP2")
    private BigDecimal R37_NET_AMT_BWP2;

    @Column(name = "R37_BAL_SUB_BWP1")
    private BigDecimal R37_BAL_SUB_BWP1;

    @Column(name = "R37_BAL_SUB_BWP2")
    private BigDecimal R37_BAL_SUB_BWP2;

    @Column(name = "R37_BAL_ACT_SUB_BWP1")
    private BigDecimal R37_BAL_ACT_SUB_BWP1;

    @Column(name = "R37_BAL_ACT_SUB_BWP2")
    private BigDecimal R37_BAL_ACT_SUB_BWP2;

    /* ================= R38 ================= */
    @Column(name = "R38_PRODUCT")
    private String R38_PRODUCT;

    @Column(name = "R38_FIG_BAL_BWP1")
    private BigDecimal R38_FIG_BAL_BWP1;

    @Column(name = "R38_FIG_BAL_BWP2")
    private BigDecimal R38_FIG_BAL_BWP2;

    @Column(name = "R38_AMT_ADJ_BWP1")
    private BigDecimal R38_AMT_ADJ_BWP1;

    @Column(name = "R38_AMT_ADJ_BWP2")
    private BigDecimal R38_AMT_ADJ_BWP2;

    @Column(name = "R38_NET_AMT_BWP1")
    private BigDecimal R38_NET_AMT_BWP1;

    @Column(name = "R38_NET_AMT_BWP2")
    private BigDecimal R38_NET_AMT_BWP2;

    @Column(name = "R38_BAL_SUB_BWP1")
    private BigDecimal R38_BAL_SUB_BWP1;

    @Column(name = "R38_BAL_SUB_BWP2")
    private BigDecimal R38_BAL_SUB_BWP2;

    @Column(name = "R38_BAL_ACT_SUB_BWP1")
    private BigDecimal R38_BAL_ACT_SUB_BWP1;

    @Column(name = "R38_BAL_ACT_SUB_BWP2")
    private BigDecimal R38_BAL_ACT_SUB_BWP2;

    /* ================= R39 ================= */
    @Column(name = "R39_PRODUCT")
    private String R39_PRODUCT;

    @Column(name = "R39_FIG_BAL_BWP1")
    private BigDecimal R39_FIG_BAL_BWP1;

    @Column(name = "R39_FIG_BAL_BWP2")
    private BigDecimal R39_FIG_BAL_BWP2;

    @Column(name = "R39_AMT_ADJ_BWP1")
    private BigDecimal R39_AMT_ADJ_BWP1;

    @Column(name = "R39_AMT_ADJ_BWP2")
    private BigDecimal R39_AMT_ADJ_BWP2;

    @Column(name = "R39_NET_AMT_BWP1")
    private BigDecimal R39_NET_AMT_BWP1;

    @Column(name = "R39_NET_AMT_BWP2")
    private BigDecimal R39_NET_AMT_BWP2;

    @Column(name = "R39_BAL_SUB_BWP1")
    private BigDecimal R39_BAL_SUB_BWP1;

    @Column(name = "R39_BAL_SUB_BWP2")
    private BigDecimal R39_BAL_SUB_BWP2;

    @Column(name = "R39_BAL_ACT_SUB_BWP1")
    private BigDecimal R39_BAL_ACT_SUB_BWP1;

    @Column(name = "R39_BAL_ACT_SUB_BWP2")
    private BigDecimal R39_BAL_ACT_SUB_BWP2;

    /* ================= R40 ================= */
    @Column(name = "R40_PRODUCT")
    private String R40_PRODUCT;

    @Column(name = "R40_FIG_BAL_BWP1")
    private BigDecimal R40_FIG_BAL_BWP1;

    @Column(name = "R40_FIG_BAL_BWP2")
    private BigDecimal R40_FIG_BAL_BWP2;

    @Column(name = "R40_AMT_ADJ_BWP1")
    private BigDecimal R40_AMT_ADJ_BWP1;

    @Column(name = "R40_AMT_ADJ_BWP2")
    private BigDecimal R40_AMT_ADJ_BWP2;

    @Column(name = "R40_NET_AMT_BWP1")
    private BigDecimal R40_NET_AMT_BWP1;

    @Column(name = "R40_NET_AMT_BWP2")
    private BigDecimal R40_NET_AMT_BWP2;

    @Column(name = "R40_BAL_SUB_BWP1")
    private BigDecimal R40_BAL_SUB_BWP1;

    @Column(name = "R40_BAL_SUB_BWP2")
    private BigDecimal R40_BAL_SUB_BWP2;

    @Column(name = "R40_BAL_ACT_SUB_BWP1")
    private BigDecimal R40_BAL_ACT_SUB_BWP1;

    @Column(name = "R40_BAL_ACT_SUB_BWP2")
    private BigDecimal R40_BAL_ACT_SUB_BWP2;

    /* ================= R41 ================= */
    @Column(name = "R41_PRODUCT")
    private String R41_PRODUCT;

    @Column(name = "R41_FIG_BAL_BWP1")
    private BigDecimal R41_FIG_BAL_BWP1;

    @Column(name = "R41_FIG_BAL_BWP2")
    private BigDecimal R41_FIG_BAL_BWP2;

    @Column(name = "R41_AMT_ADJ_BWP1")
    private BigDecimal R41_AMT_ADJ_BWP1;

    @Column(name = "R41_AMT_ADJ_BWP2")
    private BigDecimal R41_AMT_ADJ_BWP2;

    @Column(name = "R41_NET_AMT_BWP1")
    private BigDecimal R41_NET_AMT_BWP1;

    @Column(name = "R41_NET_AMT_BWP2")
    private BigDecimal R41_NET_AMT_BWP2;

    @Column(name = "R41_BAL_SUB_BWP1")
    private BigDecimal R41_BAL_SUB_BWP1;

    @Column(name = "R41_BAL_SUB_BWP2")
    private BigDecimal R41_BAL_SUB_BWP2;

    @Column(name = "R41_BAL_ACT_SUB_BWP1")
    private BigDecimal R41_BAL_ACT_SUB_BWP1;

    @Column(name = "R41_BAL_ACT_SUB_BWP2")
    private BigDecimal R41_BAL_ACT_SUB_BWP2;

    /* ================= R42 ================= */
    @Column(name = "R42_PRODUCT")
    private String R42_PRODUCT;

    @Column(name = "R42_FIG_BAL_BWP1")
    private BigDecimal R42_FIG_BAL_BWP1;

    @Column(name = "R42_FIG_BAL_BWP2")
    private BigDecimal R42_FIG_BAL_BWP2;

    @Column(name = "R42_AMT_ADJ_BWP1")
    private BigDecimal R42_AMT_ADJ_BWP1;

    @Column(name = "R42_AMT_ADJ_BWP2")
    private BigDecimal R42_AMT_ADJ_BWP2;

    @Column(name = "R42_NET_AMT_BWP1")
    private BigDecimal R42_NET_AMT_BWP1;

    @Column(name = "R42_NET_AMT_BWP2")
    private BigDecimal R42_NET_AMT_BWP2;

    @Column(name = "R42_BAL_SUB_BWP1")
    private BigDecimal R42_BAL_SUB_BWP1;

    @Column(name = "R42_BAL_SUB_BWP2")
    private BigDecimal R42_BAL_SUB_BWP2;

    @Column(name = "R42_BAL_ACT_SUB_BWP1")
    private BigDecimal R42_BAL_ACT_SUB_BWP1;

    @Column(name = "R42_BAL_ACT_SUB_BWP2")
    private BigDecimal R42_BAL_ACT_SUB_BWP2;

    /* ================= R43 ================= */
    @Column(name = "R43_PRODUCT")
    private String R43_PRODUCT;

    @Column(name = "R43_FIG_BAL_BWP1")
    private BigDecimal R43_FIG_BAL_BWP1;

    @Column(name = "R43_FIG_BAL_BWP2")
    private BigDecimal R43_FIG_BAL_BWP2;

    @Column(name = "R43_AMT_ADJ_BWP1")
    private BigDecimal R43_AMT_ADJ_BWP1;

    @Column(name = "R43_AMT_ADJ_BWP2")
    private BigDecimal R43_AMT_ADJ_BWP2;

    @Column(name = "R43_NET_AMT_BWP1")
    private BigDecimal R43_NET_AMT_BWP1;

    @Column(name = "R43_NET_AMT_BWP2")
    private BigDecimal R43_NET_AMT_BWP2;

    @Column(name = "R43_BAL_SUB_BWP1")
    private BigDecimal R43_BAL_SUB_BWP1;

    @Column(name = "R43_BAL_SUB_BWP2")
    private BigDecimal R43_BAL_SUB_BWP2;

    @Column(name = "R43_BAL_ACT_SUB_BWP1")
    private BigDecimal R43_BAL_ACT_SUB_BWP1;

    @Column(name = "R43_BAL_ACT_SUB_BWP2")
    private BigDecimal R43_BAL_ACT_SUB_BWP2;

    /* ================= R44 ================= */
    @Column(name = "R44_PRODUCT")
    private String R44_PRODUCT;

    @Column(name = "R44_FIG_BAL_BWP1")
    private BigDecimal R44_FIG_BAL_BWP1;

    @Column(name = "R44_FIG_BAL_BWP2")
    private BigDecimal R44_FIG_BAL_BWP2;

    @Column(name = "R44_AMT_ADJ_BWP1")
    private BigDecimal R44_AMT_ADJ_BWP1;

    @Column(name = "R44_AMT_ADJ_BWP2")
    private BigDecimal R44_AMT_ADJ_BWP2;

    @Column(name = "R44_NET_AMT_BWP1")
    private BigDecimal R44_NET_AMT_BWP1;

    @Column(name = "R44_NET_AMT_BWP2")
    private BigDecimal R44_NET_AMT_BWP2;

    @Column(name = "R44_BAL_SUB_BWP1")
    private BigDecimal R44_BAL_SUB_BWP1;

    @Column(name = "R44_BAL_SUB_BWP2")
    private BigDecimal R44_BAL_SUB_BWP2;

    @Column(name = "R44_BAL_ACT_SUB_BWP1")
    private BigDecimal R44_BAL_ACT_SUB_BWP1;

    @Column(name = "R44_BAL_ACT_SUB_BWP2")
    private BigDecimal R44_BAL_ACT_SUB_BWP2;

    /* ================= R45 ================= */
    @Column(name = "R45_PRODUCT")
    private String R45_PRODUCT;

    @Column(name = "R45_FIG_BAL_BWP1")
    private BigDecimal R45_FIG_BAL_BWP1;

    @Column(name = "R45_FIG_BAL_BWP2")
    private BigDecimal R45_FIG_BAL_BWP2;

    @Column(name = "R45_AMT_ADJ_BWP1")
    private BigDecimal R45_AMT_ADJ_BWP1;

    @Column(name = "R45_AMT_ADJ_BWP2")
    private BigDecimal R45_AMT_ADJ_BWP2;

    @Column(name = "R45_NET_AMT_BWP1")
    private BigDecimal R45_NET_AMT_BWP1;

    @Column(name = "R45_NET_AMT_BWP2")
    private BigDecimal R45_NET_AMT_BWP2;

    @Column(name = "R45_BAL_SUB_BWP1")
    private BigDecimal R45_BAL_SUB_BWP1;

    @Column(name = "R45_BAL_SUB_BWP2")
    private BigDecimal R45_BAL_SUB_BWP2;

    @Column(name = "R45_BAL_ACT_SUB_BWP1")
    private BigDecimal R45_BAL_ACT_SUB_BWP1;

    @Column(name = "R45_BAL_ACT_SUB_BWP2")
    private BigDecimal R45_BAL_ACT_SUB_BWP2;

    /* ================= R46 ================= */
    @Column(name = "R46_PRODUCT")
    private String R46_PRODUCT;

    @Column(name = "R46_FIG_BAL_BWP1")
    private BigDecimal R46_FIG_BAL_BWP1;

    @Column(name = "R46_FIG_BAL_BWP2")
    private BigDecimal R46_FIG_BAL_BWP2;

    @Column(name = "R46_AMT_ADJ_BWP1")
    private BigDecimal R46_AMT_ADJ_BWP1;

    @Column(name = "R46_AMT_ADJ_BWP2")
    private BigDecimal R46_AMT_ADJ_BWP2;

    @Column(name = "R46_NET_AMT_BWP1")
    private BigDecimal R46_NET_AMT_BWP1;

    @Column(name = "R46_NET_AMT_BWP2")
    private BigDecimal R46_NET_AMT_BWP2;

    @Column(name = "R46_BAL_SUB_BWP1")
    private BigDecimal R46_BAL_SUB_BWP1;

    @Column(name = "R46_BAL_SUB_BWP2")
    private BigDecimal R46_BAL_SUB_BWP2;

    @Column(name = "R46_BAL_ACT_SUB_BWP1")
    private BigDecimal R46_BAL_ACT_SUB_BWP1;

    @Column(name = "R46_BAL_ACT_SUB_BWP2")
    private BigDecimal R46_BAL_ACT_SUB_BWP2;

    /* ================= R47 ================= */
    @Column(name = "R47_PRODUCT")
    private String R47_PRODUCT;

    @Column(name = "R47_FIG_BAL_BWP1")
    private BigDecimal R47_FIG_BAL_BWP1;

    @Column(name = "R47_FIG_BAL_BWP2")
    private BigDecimal R47_FIG_BAL_BWP2;

    @Column(name = "R47_AMT_ADJ_BWP1")
    private BigDecimal R47_AMT_ADJ_BWP1;

    @Column(name = "R47_AMT_ADJ_BWP2")
    private BigDecimal R47_AMT_ADJ_BWP2;

    @Column(name = "R47_NET_AMT_BWP1")
    private BigDecimal R47_NET_AMT_BWP1;

    @Column(name = "R47_NET_AMT_BWP2")
    private BigDecimal R47_NET_AMT_BWP2;

    @Column(name = "R47_BAL_SUB_BWP1")
    private BigDecimal R47_BAL_SUB_BWP1;

    @Column(name = "R47_BAL_SUB_BWP2")
    private BigDecimal R47_BAL_SUB_BWP2;

    @Column(name = "R47_BAL_ACT_SUB_BWP1")
    private BigDecimal R47_BAL_ACT_SUB_BWP1;

    @Column(name = "R47_BAL_ACT_SUB_BWP2")
    private BigDecimal R47_BAL_ACT_SUB_BWP2;

    /* ================= R48 ================= */
    @Column(name = "R48_PRODUCT")
    private String R48_PRODUCT;

    @Column(name = "R48_FIG_BAL_BWP1")
    private BigDecimal R48_FIG_BAL_BWP1;

    @Column(name = "R48_FIG_BAL_BWP2")
    private BigDecimal R48_FIG_BAL_BWP2;

    @Column(name = "R48_AMT_ADJ_BWP1")
    private BigDecimal R48_AMT_ADJ_BWP1;

    @Column(name = "R48_AMT_ADJ_BWP2")
    private BigDecimal R48_AMT_ADJ_BWP2;

    @Column(name = "R48_NET_AMT_BWP1")
    private BigDecimal R48_NET_AMT_BWP1;

    @Column(name = "R48_NET_AMT_BWP2")
    private BigDecimal R48_NET_AMT_BWP2;

    @Column(name = "R48_BAL_SUB_BWP1")
    private BigDecimal R48_BAL_SUB_BWP1;

    @Column(name = "R48_BAL_SUB_BWP2")
    private BigDecimal R48_BAL_SUB_BWP2;

    @Column(name = "R48_BAL_ACT_SUB_BWP1")
    private BigDecimal R48_BAL_ACT_SUB_BWP1;

    @Column(name = "R48_BAL_ACT_SUB_BWP2")
    private BigDecimal R48_BAL_ACT_SUB_BWP2;

    /* ================= R49 ================= */
    @Column(name = "R49_PRODUCT")
    private String R49_PRODUCT;

    @Column(name = "R49_FIG_BAL_BWP1")
    private BigDecimal R49_FIG_BAL_BWP1;

    @Column(name = "R49_FIG_BAL_BWP2")
    private BigDecimal R49_FIG_BAL_BWP2;

    @Column(name = "R49_AMT_ADJ_BWP1")
    private BigDecimal R49_AMT_ADJ_BWP1;

    @Column(name = "R49_AMT_ADJ_BWP2")
    private BigDecimal R49_AMT_ADJ_BWP2;

    @Column(name = "R49_NET_AMT_BWP1")
    private BigDecimal R49_NET_AMT_BWP1;

    @Column(name = "R49_NET_AMT_BWP2")
    private BigDecimal R49_NET_AMT_BWP2;

    @Column(name = "R49_BAL_SUB_BWP1")
    private BigDecimal R49_BAL_SUB_BWP1;

    @Column(name = "R49_BAL_SUB_BWP2")
    private BigDecimal R49_BAL_SUB_BWP2;

    @Column(name = "R49_BAL_ACT_SUB_BWP1")
    private BigDecimal R49_BAL_ACT_SUB_BWP1;

    @Column(name = "R49_BAL_ACT_SUB_BWP2")
    private BigDecimal R49_BAL_ACT_SUB_BWP2;

    /* ================= R50 ================= */
    @Column(name = "R50_PRODUCT")
    private String R50_PRODUCT;

    @Column(name = "R50_FIG_BAL_BWP1")
    private BigDecimal R50_FIG_BAL_BWP1;

    @Column(name = "R50_FIG_BAL_BWP2")
    private BigDecimal R50_FIG_BAL_BWP2;

    @Column(name = "R50_AMT_ADJ_BWP1")
    private BigDecimal R50_AMT_ADJ_BWP1;

    @Column(name = "R50_AMT_ADJ_BWP2")
    private BigDecimal R50_AMT_ADJ_BWP2;

    @Column(name = "R50_NET_AMT_BWP1")
    private BigDecimal R50_NET_AMT_BWP1;

    @Column(name = "R50_NET_AMT_BWP2")
    private BigDecimal R50_NET_AMT_BWP2;

    @Column(name = "R50_BAL_SUB_BWP1")
    private BigDecimal R50_BAL_SUB_BWP1;

    @Column(name = "R50_BAL_SUB_BWP2")
    private BigDecimal R50_BAL_SUB_BWP2;

    @Column(name = "R50_BAL_ACT_SUB_BWP1")
    private BigDecimal R50_BAL_ACT_SUB_BWP1;

    @Column(name = "R50_BAL_ACT_SUB_BWP2")
    private BigDecimal R50_BAL_ACT_SUB_BWP2;

    /* ================= R51 ================= */
    @Column(name = "R51_PRODUCT")
    private String R51_PRODUCT;

    @Column(name = "R51_FIG_BAL_BWP1")
    private BigDecimal R51_FIG_BAL_BWP1;

    @Column(name = "R51_FIG_BAL_BWP2")
    private BigDecimal R51_FIG_BAL_BWP2;

    @Column(name = "R51_AMT_ADJ_BWP1")
    private BigDecimal R51_AMT_ADJ_BWP1;

    @Column(name = "R51_AMT_ADJ_BWP2")
    private BigDecimal R51_AMT_ADJ_BWP2;

    @Column(name = "R51_NET_AMT_BWP1")
    private BigDecimal R51_NET_AMT_BWP1;

    @Column(name = "R51_NET_AMT_BWP2")
    private BigDecimal R51_NET_AMT_BWP2;

    @Column(name = "R51_BAL_SUB_BWP1")
    private BigDecimal R51_BAL_SUB_BWP1;

    @Column(name = "R51_BAL_SUB_BWP2")
    private BigDecimal R51_BAL_SUB_BWP2;

    @Column(name = "R51_BAL_ACT_SUB_BWP1")
    private BigDecimal R51_BAL_ACT_SUB_BWP1;

    @Column(name = "R51_BAL_ACT_SUB_BWP2")
    private BigDecimal R51_BAL_ACT_SUB_BWP2;

    /* ================= R52 ================= */
    @Column(name = "R52_PRODUCT")
    private String R52_PRODUCT;

    @Column(name = "R52_FIG_BAL_BWP1")
    private BigDecimal R52_FIG_BAL_BWP1;

    @Column(name = "R52_FIG_BAL_BWP2")
    private BigDecimal R52_FIG_BAL_BWP2;

    @Column(name = "R52_AMT_ADJ_BWP1")
    private BigDecimal R52_AMT_ADJ_BWP1;

    @Column(name = "R52_AMT_ADJ_BWP2")
    private BigDecimal R52_AMT_ADJ_BWP2;

    @Column(name = "R52_NET_AMT_BWP1")
    private BigDecimal R52_NET_AMT_BWP1;

    @Column(name = "R52_NET_AMT_BWP2")
    private BigDecimal R52_NET_AMT_BWP2;

    @Column(name = "R52_BAL_SUB_BWP1")
    private BigDecimal R52_BAL_SUB_BWP1;

    @Column(name = "R52_BAL_SUB_BWP2")
    private BigDecimal R52_BAL_SUB_BWP2;

    @Column(name = "R52_BAL_ACT_SUB_BWP1")
    private BigDecimal R52_BAL_ACT_SUB_BWP1;

    @Column(name = "R52_BAL_ACT_SUB_BWP2")
    private BigDecimal R52_BAL_ACT_SUB_BWP2;

    /* ================= R53 ================= */
    @Column(name = "R53_PRODUCT")
    private String R53_PRODUCT;

    @Column(name = "R53_FIG_BAL_BWP1")
    private BigDecimal R53_FIG_BAL_BWP1;

    @Column(name = "R53_FIG_BAL_BWP2")
    private BigDecimal R53_FIG_BAL_BWP2;

    @Column(name = "R53_AMT_ADJ_BWP1")
    private BigDecimal R53_AMT_ADJ_BWP1;

    @Column(name = "R53_AMT_ADJ_BWP2")
    private BigDecimal R53_AMT_ADJ_BWP2;

    @Column(name = "R53_NET_AMT_BWP1")
    private BigDecimal R53_NET_AMT_BWP1;

    @Column(name = "R53_NET_AMT_BWP2")
    private BigDecimal R53_NET_AMT_BWP2;

    @Column(name = "R53_BAL_SUB_BWP1")
    private BigDecimal R53_BAL_SUB_BWP1;

    @Column(name = "R53_BAL_SUB_BWP2")
    private BigDecimal R53_BAL_SUB_BWP2;

    @Column(name = "R53_BAL_ACT_SUB_BWP1")
    private BigDecimal R53_BAL_ACT_SUB_BWP1;

    @Column(name = "R53_BAL_ACT_SUB_BWP2")
    private BigDecimal R53_BAL_ACT_SUB_BWP2;

    /* ================= R54 ================= */
    @Column(name = "R54_PRODUCT")
    private String R54_PRODUCT;

    @Column(name = "R54_FIG_BAL_BWP1")
    private BigDecimal R54_FIG_BAL_BWP1;

    @Column(name = "R54_FIG_BAL_BWP2")
    private BigDecimal R54_FIG_BAL_BWP2;

    @Column(name = "R54_AMT_ADJ_BWP1")
    private BigDecimal R54_AMT_ADJ_BWP1;

    @Column(name = "R54_AMT_ADJ_BWP2")
    private BigDecimal R54_AMT_ADJ_BWP2;

    @Column(name = "R54_NET_AMT_BWP1")
    private BigDecimal R54_NET_AMT_BWP1;

    @Column(name = "R54_NET_AMT_BWP2")
    private BigDecimal R54_NET_AMT_BWP2;

    @Column(name = "R54_BAL_SUB_BWP1")
    private BigDecimal R54_BAL_SUB_BWP1;

    @Column(name = "R54_BAL_SUB_BWP2")
    private BigDecimal R54_BAL_SUB_BWP2;

    @Column(name = "R54_BAL_ACT_SUB_BWP1")
    private BigDecimal R54_BAL_ACT_SUB_BWP1;

    @Column(name = "R54_BAL_ACT_SUB_BWP2")
    private BigDecimal R54_BAL_ACT_SUB_BWP2;

    /* ================= R55 ================= */
    @Column(name = "R55_PRODUCT")
    private String R55_PRODUCT;

    @Column(name = "R55_FIG_BAL_BWP1")
    private BigDecimal R55_FIG_BAL_BWP1;

    @Column(name = "R55_FIG_BAL_BWP2")
    private BigDecimal R55_FIG_BAL_BWP2;

    @Column(name = "R55_AMT_ADJ_BWP1")
    private BigDecimal R55_AMT_ADJ_BWP1;

    @Column(name = "R55_AMT_ADJ_BWP2")
    private BigDecimal R55_AMT_ADJ_BWP2;

    @Column(name = "R55_NET_AMT_BWP1")
    private BigDecimal R55_NET_AMT_BWP1;

    @Column(name = "R55_NET_AMT_BWP2")
    private BigDecimal R55_NET_AMT_BWP2;

    @Column(name = "R55_BAL_SUB_BWP1")
    private BigDecimal R55_BAL_SUB_BWP1;

    @Column(name = "R55_BAL_SUB_BWP2")
    private BigDecimal R55_BAL_SUB_BWP2;

    @Column(name = "R55_BAL_ACT_SUB_BWP1")
    private BigDecimal R55_BAL_ACT_SUB_BWP1;

    @Column(name = "R55_BAL_ACT_SUB_BWP2")
    private BigDecimal R55_BAL_ACT_SUB_BWP2;

    /* ================= R56 ================= */
    @Column(name = "R56_PRODUCT")
    private String R56_PRODUCT;

    @Column(name = "R56_FIG_BAL_BWP1")
    private BigDecimal R56_FIG_BAL_BWP1;

    @Column(name = "R56_FIG_BAL_BWP2")
    private BigDecimal R56_FIG_BAL_BWP2;

    @Column(name = "R56_AMT_ADJ_BWP1")
    private BigDecimal R56_AMT_ADJ_BWP1;

    @Column(name = "R56_AMT_ADJ_BWP2")
    private BigDecimal R56_AMT_ADJ_BWP2;

    @Column(name = "R56_NET_AMT_BWP1")
    private BigDecimal R56_NET_AMT_BWP1;

    @Column(name = "R56_NET_AMT_BWP2")
    private BigDecimal R56_NET_AMT_BWP2;

    @Column(name = "R56_BAL_SUB_BWP1")
    private BigDecimal R56_BAL_SUB_BWP1;

    @Column(name = "R56_BAL_SUB_BWP2")
    private BigDecimal R56_BAL_SUB_BWP2;

    @Column(name = "R56_BAL_ACT_SUB_BWP1")
    private BigDecimal R56_BAL_ACT_SUB_BWP1;

    @Column(name = "R56_BAL_ACT_SUB_BWP2")
    private BigDecimal R56_BAL_ACT_SUB_BWP2;

    /* ================= R57 ================= */
    @Column(name = "R57_PRODUCT")
    private String R57_PRODUCT;

    @Column(name = "R57_FIG_BAL_BWP1")
    private BigDecimal R57_FIG_BAL_BWP1;

    @Column(name = "R57_FIG_BAL_BWP2")
    private BigDecimal R57_FIG_BAL_BWP2;

    @Column(name = "R57_AMT_ADJ_BWP1")
    private BigDecimal R57_AMT_ADJ_BWP1;

    @Column(name = "R57_AMT_ADJ_BWP2")
    private BigDecimal R57_AMT_ADJ_BWP2;

    @Column(name = "R57_NET_AMT_BWP1")
    private BigDecimal R57_NET_AMT_BWP1;

    @Column(name = "R57_NET_AMT_BWP2")
    private BigDecimal R57_NET_AMT_BWP2;

    @Column(name = "R57_BAL_SUB_BWP1")
    private BigDecimal R57_BAL_SUB_BWP1;

    @Column(name = "R57_BAL_SUB_BWP2")
    private BigDecimal R57_BAL_SUB_BWP2;

    @Column(name = "R57_BAL_ACT_SUB_BWP1")
    private BigDecimal R57_BAL_ACT_SUB_BWP1;

    @Column(name = "R57_BAL_ACT_SUB_BWP2")
    private BigDecimal R57_BAL_ACT_SUB_BWP2;

    /* ================= R58 ================= */
    @Column(name = "R58_PRODUCT")
    private String R58_PRODUCT;

    @Column(name = "R58_FIG_BAL_BWP1")
    private BigDecimal R58_FIG_BAL_BWP1;

    @Column(name = "R58_FIG_BAL_BWP2")
    private BigDecimal R58_FIG_BAL_BWP2;

    @Column(name = "R58_AMT_ADJ_BWP1")
    private BigDecimal R58_AMT_ADJ_BWP1;

    @Column(name = "R58_AMT_ADJ_BWP2")
    private BigDecimal R58_AMT_ADJ_BWP2;

    @Column(name = "R58_NET_AMT_BWP1")
    private BigDecimal R58_NET_AMT_BWP1;

    @Column(name = "R58_NET_AMT_BWP2")
    private BigDecimal R58_NET_AMT_BWP2;

    @Column(name = "R58_BAL_SUB_BWP1")
    private BigDecimal R58_BAL_SUB_BWP1;

    @Column(name = "R58_BAL_SUB_BWP2")
    private BigDecimal R58_BAL_SUB_BWP2;

    @Column(name = "R58_BAL_ACT_SUB_BWP1")
    private BigDecimal R58_BAL_ACT_SUB_BWP1;

    @Column(name = "R58_BAL_ACT_SUB_BWP2")
    private BigDecimal R58_BAL_ACT_SUB_BWP2;

    /* ================= R59 ================= */
    @Column(name = "R59_PRODUCT")
    private String R59_PRODUCT;

    @Column(name = "R59_FIG_BAL_BWP1")
    private BigDecimal R59_FIG_BAL_BWP1;

    @Column(name = "R59_FIG_BAL_BWP2")
    private BigDecimal R59_FIG_BAL_BWP2;

    @Column(name = "R59_AMT_ADJ_BWP1")
    private BigDecimal R59_AMT_ADJ_BWP1;

    @Column(name = "R59_AMT_ADJ_BWP2")
    private BigDecimal R59_AMT_ADJ_BWP2;

    @Column(name = "R59_NET_AMT_BWP1")
    private BigDecimal R59_NET_AMT_BWP1;

    @Column(name = "R59_NET_AMT_BWP2")
    private BigDecimal R59_NET_AMT_BWP2;

    @Column(name = "R59_BAL_SUB_BWP1")
    private BigDecimal R59_BAL_SUB_BWP1;

    @Column(name = "R59_BAL_SUB_BWP2")
    private BigDecimal R59_BAL_SUB_BWP2;

    @Column(name = "R59_BAL_ACT_SUB_BWP1")
    private BigDecimal R59_BAL_ACT_SUB_BWP1;

    @Column(name = "R59_BAL_ACT_SUB_BWP2")
    private BigDecimal R59_BAL_ACT_SUB_BWP2;

    /* ================= R60 ================= */
    @Column(name = "R60_PRODUCT")
    private String R60_PRODUCT;

    @Column(name = "R60_FIG_BAL_BWP1")
    private BigDecimal R60_FIG_BAL_BWP1;

    @Column(name = "R60_FIG_BAL_BWP2")
    private BigDecimal R60_FIG_BAL_BWP2;

    @Column(name = "R60_AMT_ADJ_BWP1")
    private BigDecimal R60_AMT_ADJ_BWP1;

    @Column(name = "R60_AMT_ADJ_BWP2")
    private BigDecimal R60_AMT_ADJ_BWP2;

    @Column(name = "R60_NET_AMT_BWP1")
    private BigDecimal R60_NET_AMT_BWP1;

    @Column(name = "R60_NET_AMT_BWP2")
    private BigDecimal R60_NET_AMT_BWP2;

    @Column(name = "R60_BAL_SUB_BWP1")
    private BigDecimal R60_BAL_SUB_BWP1;

    @Column(name = "R60_BAL_SUB_BWP2")
    private BigDecimal R60_BAL_SUB_BWP2;

    @Column(name = "R60_BAL_ACT_SUB_BWP1")
    private BigDecimal R60_BAL_ACT_SUB_BWP1;

    @Column(name = "R60_BAL_ACT_SUB_BWP2")
    private BigDecimal R60_BAL_ACT_SUB_BWP2;

    /* ================= R61 ================= */
    @Column(name = "R61_PRODUCT")
    private String R61_PRODUCT;

    @Column(name = "R61_FIG_BAL_BWP1")
    private BigDecimal R61_FIG_BAL_BWP1;

    @Column(name = "R61_FIG_BAL_BWP2")
    private BigDecimal R61_FIG_BAL_BWP2;

    @Column(name = "R61_AMT_ADJ_BWP1")
    private BigDecimal R61_AMT_ADJ_BWP1;

    @Column(name = "R61_AMT_ADJ_BWP2")
    private BigDecimal R61_AMT_ADJ_BWP2;

    @Column(name = "R61_NET_AMT_BWP1")
    private BigDecimal R61_NET_AMT_BWP1;

    @Column(name = "R61_NET_AMT_BWP2")
    private BigDecimal R61_NET_AMT_BWP2;

    @Column(name = "R61_BAL_SUB_BWP1")
    private BigDecimal R61_BAL_SUB_BWP1;

    @Column(name = "R61_BAL_SUB_BWP2")
    private BigDecimal R61_BAL_SUB_BWP2;

    @Column(name = "R61_BAL_ACT_SUB_BWP1")
    private BigDecimal R61_BAL_ACT_SUB_BWP1;

    @Column(name = "R61_BAL_ACT_SUB_BWP2")
    private BigDecimal R61_BAL_ACT_SUB_BWP2;

    /* ================= R62 ================= */
    @Column(name = "R62_PRODUCT")
    private String R62_PRODUCT;

    @Column(name = "R62_FIG_BAL_BWP1")
    private BigDecimal R62_FIG_BAL_BWP1;

    @Column(name = "R62_FIG_BAL_BWP2")
    private BigDecimal R62_FIG_BAL_BWP2;

    @Column(name = "R62_AMT_ADJ_BWP1")
    private BigDecimal R62_AMT_ADJ_BWP1;

    @Column(name = "R62_AMT_ADJ_BWP2")
    private BigDecimal R62_AMT_ADJ_BWP2;

    @Column(name = "R62_NET_AMT_BWP1")
    private BigDecimal R62_NET_AMT_BWP1;

    @Column(name = "R62_NET_AMT_BWP2")
    private BigDecimal R62_NET_AMT_BWP2;

    @Column(name = "R62_BAL_SUB_BWP1")
    private BigDecimal R62_BAL_SUB_BWP1;

    @Column(name = "R62_BAL_SUB_BWP2")
    private BigDecimal R62_BAL_SUB_BWP2;

    @Column(name = "R62_BAL_ACT_SUB_BWP1")
    private BigDecimal R62_BAL_ACT_SUB_BWP1;

    @Column(name = "R62_BAL_ACT_SUB_BWP2")
    private BigDecimal R62_BAL_ACT_SUB_BWP2;

    /* ================= R63 ================= */
    @Column(name = "R63_PRODUCT")
    private String R63_PRODUCT;

    @Column(name = "R63_FIG_BAL_BWP1")
    private BigDecimal R63_FIG_BAL_BWP1;

    @Column(name = "R63_FIG_BAL_BWP2")
    private BigDecimal R63_FIG_BAL_BWP2;

    @Column(name = "R63_AMT_ADJ_BWP1")
    private BigDecimal R63_AMT_ADJ_BWP1;

    @Column(name = "R63_AMT_ADJ_BWP2")
    private BigDecimal R63_AMT_ADJ_BWP2;

    @Column(name = "R63_NET_AMT_BWP1")
    private BigDecimal R63_NET_AMT_BWP1;

    @Column(name = "R63_NET_AMT_BWP2")
    private BigDecimal R63_NET_AMT_BWP2;

    @Column(name = "R63_BAL_SUB_BWP1")
    private BigDecimal R63_BAL_SUB_BWP1;

    @Column(name = "R63_BAL_SUB_BWP2")
    private BigDecimal R63_BAL_SUB_BWP2;

    @Column(name = "R63_BAL_ACT_SUB_BWP1")
    private BigDecimal R63_BAL_ACT_SUB_BWP1;

    @Column(name = "R63_BAL_ACT_SUB_BWP2")
    private BigDecimal R63_BAL_ACT_SUB_BWP2;

    /* ================= R64 ================= */
    @Column(name = "R64_PRODUCT")
    private String R64_PRODUCT;

    @Column(name = "R64_FIG_BAL_BWP1")
    private BigDecimal R64_FIG_BAL_BWP1;

    @Column(name = "R64_FIG_BAL_BWP2")
    private BigDecimal R64_FIG_BAL_BWP2;

    @Column(name = "R64_AMT_ADJ_BWP1")
    private BigDecimal R64_AMT_ADJ_BWP1;

    @Column(name = "R64_AMT_ADJ_BWP2")
    private BigDecimal R64_AMT_ADJ_BWP2;

    @Column(name = "R64_NET_AMT_BWP1")
    private BigDecimal R64_NET_AMT_BWP1;

    @Column(name = "R64_NET_AMT_BWP2")
    private BigDecimal R64_NET_AMT_BWP2;

    @Column(name = "R64_BAL_SUB_BWP1")
    private BigDecimal R64_BAL_SUB_BWP1;

    @Column(name = "R64_BAL_SUB_BWP2")
    private BigDecimal R64_BAL_SUB_BWP2;

    @Column(name = "R64_BAL_ACT_SUB_BWP1")
    private BigDecimal R64_BAL_ACT_SUB_BWP1;

    @Column(name = "R64_BAL_ACT_SUB_BWP2")
    private BigDecimal R64_BAL_ACT_SUB_BWP2;

    /* ================= R65 ================= */
    @Column(name = "R65_PRODUCT")
    private String R65_PRODUCT;

    @Column(name = "R65_FIG_BAL_BWP1")
    private BigDecimal R65_FIG_BAL_BWP1;

    @Column(name = "R65_FIG_BAL_BWP2")
    private BigDecimal R65_FIG_BAL_BWP2;

    @Column(name = "R65_AMT_ADJ_BWP1")
    private BigDecimal R65_AMT_ADJ_BWP1;

    @Column(name = "R65_AMT_ADJ_BWP2")
    private BigDecimal R65_AMT_ADJ_BWP2;

    @Column(name = "R65_NET_AMT_BWP1")
    private BigDecimal R65_NET_AMT_BWP1;

    @Column(name = "R65_NET_AMT_BWP2")
    private BigDecimal R65_NET_AMT_BWP2;

    @Column(name = "R65_BAL_SUB_BWP1")
    private BigDecimal R65_BAL_SUB_BWP1;

    @Column(name = "R65_BAL_SUB_BWP2")
    private BigDecimal R65_BAL_SUB_BWP2;

    @Column(name = "R65_BAL_ACT_SUB_BWP1")
    private BigDecimal R65_BAL_ACT_SUB_BWP1;

    @Column(name = "R65_BAL_ACT_SUB_BWP2")
    private BigDecimal R65_BAL_ACT_SUB_BWP2;

    /* ================= R66 ================= */
    @Column(name = "R66_PRODUCT")
    private String R66_PRODUCT;

    @Column(name = "R66_FIG_BAL_BWP1")
    private BigDecimal R66_FIG_BAL_BWP1;

    @Column(name = "R66_FIG_BAL_BWP2")
    private BigDecimal R66_FIG_BAL_BWP2;

    @Column(name = "R66_AMT_ADJ_BWP1")
    private BigDecimal R66_AMT_ADJ_BWP1;

    @Column(name = "R66_AMT_ADJ_BWP2")
    private BigDecimal R66_AMT_ADJ_BWP2;

    @Column(name = "R66_NET_AMT_BWP1")
    private BigDecimal R66_NET_AMT_BWP1;

    @Column(name = "R66_NET_AMT_BWP2")
    private BigDecimal R66_NET_AMT_BWP2;

    @Column(name = "R66_BAL_SUB_BWP1")
    private BigDecimal R66_BAL_SUB_BWP1;

    @Column(name = "R66_BAL_SUB_BWP2")
    private BigDecimal R66_BAL_SUB_BWP2;

    @Column(name = "R66_BAL_ACT_SUB_BWP1")
    private BigDecimal R66_BAL_ACT_SUB_BWP1;

    @Column(name = "R66_BAL_ACT_SUB_BWP2")
    private BigDecimal R66_BAL_ACT_SUB_BWP2;
    
    
    /* ================= R71 ================= */
    @Column(name = "R71_PRODUCT")
    private String R71_PRODUCT;
    @Column(name = "R71_FIG_BAL_BWP1")
    private BigDecimal R71_FIG_BAL_BWP1;
    @Column(name = "R71_FIG_BAL_BWP2")
    private BigDecimal R71_FIG_BAL_BWP2;
    @Column(name = "R71_AMT_ADJ_BWP1")
    private BigDecimal R71_AMT_ADJ_BWP1;
    @Column(name = "R71_AMT_ADJ_BWP2")
    private BigDecimal R71_AMT_ADJ_BWP2;
    @Column(name = "R71_NET_AMT_BWP1")
    private BigDecimal R71_NET_AMT_BWP1;
    @Column(name = "R71_NET_AMT_BWP2")
    private BigDecimal R71_NET_AMT_BWP2;
    @Column(name = "R71_BAL_SUB_BWP1")
    private BigDecimal R71_BAL_SUB_BWP1;
    @Column(name = "R71_BAL_SUB_BWP2")
    private BigDecimal R71_BAL_SUB_BWP2;
    @Column(name = "R71_BAL_ACT_SUB_BWP1")
    private BigDecimal R71_BAL_ACT_SUB_BWP1;
    @Column(name = "R71_BAL_ACT_SUB_BWP2")
    private BigDecimal R71_BAL_ACT_SUB_BWP2;

    /* ================= R72 ================= */
    @Column(name = "R72_PRODUCT")
    private String R72_PRODUCT;
    @Column(name = "R72_FIG_BAL_BWP1")
    private BigDecimal R72_FIG_BAL_BWP1;
    @Column(name = "R72_FIG_BAL_BWP2")
    private BigDecimal R72_FIG_BAL_BWP2;
    @Column(name = "R72_AMT_ADJ_BWP1")
    private BigDecimal R72_AMT_ADJ_BWP1;
    @Column(name = "R72_AMT_ADJ_BWP2")
    private BigDecimal R72_AMT_ADJ_BWP2;
    @Column(name = "R72_NET_AMT_BWP1")
    private BigDecimal R72_NET_AMT_BWP1;
    @Column(name = "R72_NET_AMT_BWP2")
    private BigDecimal R72_NET_AMT_BWP2;
    @Column(name = "R72_BAL_SUB_BWP1")
    private BigDecimal R72_BAL_SUB_BWP1;
    @Column(name = "R72_BAL_SUB_BWP2")
    private BigDecimal R72_BAL_SUB_BWP2;
    @Column(name = "R72_BAL_ACT_SUB_BWP1")
    private BigDecimal R72_BAL_ACT_SUB_BWP1;
    @Column(name = "R72_BAL_ACT_SUB_BWP2")
    private BigDecimal R72_BAL_ACT_SUB_BWP2;

    /* ================= R73 ================= */
    @Column(name = "R73_PRODUCT")
    private String R73_PRODUCT;
    @Column(name = "R73_FIG_BAL_BWP1")
    private BigDecimal R73_FIG_BAL_BWP1;
    @Column(name = "R73_FIG_BAL_BWP2")
    private BigDecimal R73_FIG_BAL_BWP2;
    @Column(name = "R73_AMT_ADJ_BWP1")
    private BigDecimal R73_AMT_ADJ_BWP1;
    @Column(name = "R73_AMT_ADJ_BWP2")
    private BigDecimal R73_AMT_ADJ_BWP2;
    @Column(name = "R73_NET_AMT_BWP1")
    private BigDecimal R73_NET_AMT_BWP1;
    @Column(name = "R73_NET_AMT_BWP2")
    private BigDecimal R73_NET_AMT_BWP2;
    @Column(name = "R73_BAL_SUB_BWP1")
    private BigDecimal R73_BAL_SUB_BWP1;
    @Column(name = "R73_BAL_SUB_BWP2")
    private BigDecimal R73_BAL_SUB_BWP2;
    @Column(name = "R73_BAL_ACT_SUB_BWP1")
    private BigDecimal R73_BAL_ACT_SUB_BWP1;
    @Column(name = "R73_BAL_ACT_SUB_BWP2")
    private BigDecimal R73_BAL_ACT_SUB_BWP2;

    /* ================= R74 ================= */
    @Column(name = "R74_PRODUCT")
    private String R74_PRODUCT;
    @Column(name = "R74_FIG_BAL_BWP1")
    private BigDecimal R74_FIG_BAL_BWP1;
    @Column(name = "R74_FIG_BAL_BWP2")
    private BigDecimal R74_FIG_BAL_BWP2;
    @Column(name = "R74_AMT_ADJ_BWP1")
    private BigDecimal R74_AMT_ADJ_BWP1;
    @Column(name = "R74_AMT_ADJ_BWP2")
    private BigDecimal R74_AMT_ADJ_BWP2;
    @Column(name = "R74_NET_AMT_BWP1")
    private BigDecimal R74_NET_AMT_BWP1;
    @Column(name = "R74_NET_AMT_BWP2")
    private BigDecimal R74_NET_AMT_BWP2;
    @Column(name = "R74_BAL_SUB_BWP1")
    private BigDecimal R74_BAL_SUB_BWP1;
    @Column(name = "R74_BAL_SUB_BWP2")
    private BigDecimal R74_BAL_SUB_BWP2;
    @Column(name = "R74_BAL_ACT_SUB_BWP1")
    private BigDecimal R74_BAL_ACT_SUB_BWP1;
    @Column(name = "R74_BAL_ACT_SUB_BWP2")
    private BigDecimal R74_BAL_ACT_SUB_BWP2;

    /* ================= R75 ================= */
    @Column(name = "R75_PRODUCT")
    private String R75_PRODUCT;
    @Column(name = "R75_FIG_BAL_BWP1")
    private BigDecimal R75_FIG_BAL_BWP1;
    @Column(name = "R75_FIG_BAL_BWP2")
    private BigDecimal R75_FIG_BAL_BWP2;
    @Column(name = "R75_AMT_ADJ_BWP1")
    private BigDecimal R75_AMT_ADJ_BWP1;
    @Column(name = "R75_AMT_ADJ_BWP2")
    private BigDecimal R75_AMT_ADJ_BWP2;
    @Column(name = "R75_NET_AMT_BWP1")
    private BigDecimal R75_NET_AMT_BWP1;
    @Column(name = "R75_NET_AMT_BWP2")
    private BigDecimal R75_NET_AMT_BWP2;
    @Column(name = "R75_BAL_SUB_BWP1")
    private BigDecimal R75_BAL_SUB_BWP1;
    @Column(name = "R75_BAL_SUB_BWP2")
    private BigDecimal R75_BAL_SUB_BWP2;
    @Column(name = "R75_BAL_ACT_SUB_BWP1")
    private BigDecimal R75_BAL_ACT_SUB_BWP1;
    @Column(name = "R75_BAL_ACT_SUB_BWP2")
    private BigDecimal R75_BAL_ACT_SUB_BWP2;

    /* ================= R76 ================= */
    @Column(name = "R76_PRODUCT")
    private String R76_PRODUCT;
    @Column(name = "R76_FIG_BAL_BWP1")
    private BigDecimal R76_FIG_BAL_BWP1;
    @Column(name = "R76_FIG_BAL_BWP2")
    private BigDecimal R76_FIG_BAL_BWP2;
    @Column(name = "R76_AMT_ADJ_BWP1")
    private BigDecimal R76_AMT_ADJ_BWP1;
    @Column(name = "R76_AMT_ADJ_BWP2")
    private BigDecimal R76_AMT_ADJ_BWP2;
    @Column(name = "R76_NET_AMT_BWP1")
    private BigDecimal R76_NET_AMT_BWP1;
    @Column(name = "R76_NET_AMT_BWP2")
    private BigDecimal R76_NET_AMT_BWP2;
    @Column(name = "R76_BAL_SUB_BWP1")
    private BigDecimal R76_BAL_SUB_BWP1;
    @Column(name = "R76_BAL_SUB_BWP2")
    private BigDecimal R76_BAL_SUB_BWP2;
    @Column(name = "R76_BAL_ACT_SUB_BWP1")
    private BigDecimal R76_BAL_ACT_SUB_BWP1;
    @Column(name = "R76_BAL_ACT_SUB_BWP2")
    private BigDecimal R76_BAL_ACT_SUB_BWP2;

    /* ================= R77 ================= */
    @Column(name = "R77_PRODUCT")
    private String R77_PRODUCT;
    @Column(name = "R77_FIG_BAL_BWP1")
    private BigDecimal R77_FIG_BAL_BWP1;
    @Column(name = "R77_FIG_BAL_BWP2")
    private BigDecimal R77_FIG_BAL_BWP2;
    @Column(name = "R77_AMT_ADJ_BWP1")
    private BigDecimal R77_AMT_ADJ_BWP1;
    @Column(name = "R77_AMT_ADJ_BWP2")
    private BigDecimal R77_AMT_ADJ_BWP2;
    @Column(name = "R77_NET_AMT_BWP1")
    private BigDecimal R77_NET_AMT_BWP1;
    @Column(name = "R77_NET_AMT_BWP2")
    private BigDecimal R77_NET_AMT_BWP2;
    @Column(name = "R77_BAL_SUB_BWP1")
    private BigDecimal R77_BAL_SUB_BWP1;
    @Column(name = "R77_BAL_SUB_BWP2")
    private BigDecimal R77_BAL_SUB_BWP2;
    @Column(name = "R77_BAL_ACT_SUB_BWP1")
    private BigDecimal R77_BAL_ACT_SUB_BWP1;
    @Column(name = "R77_BAL_ACT_SUB_BWP2")
    private BigDecimal R77_BAL_ACT_SUB_BWP2;

    /* ================= R78 ================= */
    @Column(name = "R78_PRODUCT")
    private String R78_PRODUCT;
    @Column(name = "R78_FIG_BAL_BWP1")
    private BigDecimal R78_FIG_BAL_BWP1;
    @Column(name = "R78_FIG_BAL_BWP2")
    private BigDecimal R78_FIG_BAL_BWP2;
    @Column(name = "R78_AMT_ADJ_BWP1")
    private BigDecimal R78_AMT_ADJ_BWP1;
    @Column(name = "R78_AMT_ADJ_BWP2")
    private BigDecimal R78_AMT_ADJ_BWP2;
    @Column(name = "R78_NET_AMT_BWP1")
    private BigDecimal R78_NET_AMT_BWP1;
    @Column(name = "R78_NET_AMT_BWP2")
    private BigDecimal R78_NET_AMT_BWP2;
    @Column(name = "R78_BAL_SUB_BWP1")
    private BigDecimal R78_BAL_SUB_BWP1;
    @Column(name = "R78_BAL_SUB_BWP2")
    private BigDecimal R78_BAL_SUB_BWP2;
    @Column(name = "R78_BAL_ACT_SUB_BWP1")
    private BigDecimal R78_BAL_ACT_SUB_BWP1;
    @Column(name = "R78_BAL_ACT_SUB_BWP2")
    private BigDecimal R78_BAL_ACT_SUB_BWP2;

    /* ================= R79 ================= */
    @Column(name = "R79_PRODUCT")
    private String R79_PRODUCT;
    @Column(name = "R79_FIG_BAL_BWP1")
    private BigDecimal R79_FIG_BAL_BWP1;
    @Column(name = "R79_FIG_BAL_BWP2")
    private BigDecimal R79_FIG_BAL_BWP2;
    @Column(name = "R79_AMT_ADJ_BWP1")
    private BigDecimal R79_AMT_ADJ_BWP1;
    @Column(name = "R79_AMT_ADJ_BWP2")
    private BigDecimal R79_AMT_ADJ_BWP2;
    @Column(name = "R79_NET_AMT_BWP1")
    private BigDecimal R79_NET_AMT_BWP1;
    @Column(name = "R79_NET_AMT_BWP2")
    private BigDecimal R79_NET_AMT_BWP2;
    @Column(name = "R79_BAL_SUB_BWP1")
    private BigDecimal R79_BAL_SUB_BWP1;
    @Column(name = "R79_BAL_SUB_BWP2")
    private BigDecimal R79_BAL_SUB_BWP2;
    @Column(name = "R79_BAL_ACT_SUB_BWP1")
    private BigDecimal R79_BAL_ACT_SUB_BWP1;
    @Column(name = "R79_BAL_ACT_SUB_BWP2")
    private BigDecimal R79_BAL_ACT_SUB_BWP2;

    /* ================= R80 ================= */
    @Column(name = "R80_PRODUCT")
    private String R80_PRODUCT;
    @Column(name = "R80_FIG_BAL_BWP1")
    private BigDecimal R80_FIG_BAL_BWP1;
    @Column(name = "R80_FIG_BAL_BWP2")
    private BigDecimal R80_FIG_BAL_BWP2;
    @Column(name = "R80_AMT_ADJ_BWP1")
    private BigDecimal R80_AMT_ADJ_BWP1;
    @Column(name = "R80_AMT_ADJ_BWP2")
    private BigDecimal R80_AMT_ADJ_BWP2;
    @Column(name = "R80_NET_AMT_BWP1")
    private BigDecimal R80_NET_AMT_BWP1;
    @Column(name = "R80_NET_AMT_BWP2")
    private BigDecimal R80_NET_AMT_BWP2;
    @Column(name = "R80_BAL_SUB_BWP1")
    private BigDecimal R80_BAL_SUB_BWP1;
    @Column(name = "R80_BAL_SUB_BWP2")
    private BigDecimal R80_BAL_SUB_BWP2;
    @Column(name = "R80_BAL_ACT_SUB_BWP1")
    private BigDecimal R80_BAL_ACT_SUB_BWP1;
    @Column(name = "R80_BAL_ACT_SUB_BWP2")
    private BigDecimal R80_BAL_ACT_SUB_BWP2;

    /* ================= R81 ================= */
    @Column(name = "R81_PRODUCT")
    private String R81_PRODUCT;
    @Column(name = "R81_FIG_BAL_BWP1")
    private BigDecimal R81_FIG_BAL_BWP1;
    @Column(name = "R81_FIG_BAL_BWP2")
    private BigDecimal R81_FIG_BAL_BWP2;
    @Column(name = "R81_AMT_ADJ_BWP1")
    private BigDecimal R81_AMT_ADJ_BWP1;
    @Column(name = "R81_AMT_ADJ_BWP2")
    private BigDecimal R81_AMT_ADJ_BWP2;
    @Column(name = "R81_NET_AMT_BWP1")
    private BigDecimal R81_NET_AMT_BWP1;
    @Column(name = "R81_NET_AMT_BWP2")
    private BigDecimal R81_NET_AMT_BWP2;
    @Column(name = "R81_BAL_SUB_BWP1")
    private BigDecimal R81_BAL_SUB_BWP1;
    @Column(name = "R81_BAL_SUB_BWP2")
    private BigDecimal R81_BAL_SUB_BWP2;
    @Column(name = "R81_BAL_ACT_SUB_BWP1")
    private BigDecimal R81_BAL_ACT_SUB_BWP1;
    @Column(name = "R81_BAL_ACT_SUB_BWP2")
    private BigDecimal R81_BAL_ACT_SUB_BWP2;

    /* ================= R82 ================= */
    @Column(name = "R82_PRODUCT")
    private String R82_PRODUCT;
    @Column(name = "R82_FIG_BAL_BWP1")
    private BigDecimal R82_FIG_BAL_BWP1;
    @Column(name = "R82_FIG_BAL_BWP2")
    private BigDecimal R82_FIG_BAL_BWP2;
    @Column(name = "R82_AMT_ADJ_BWP1")
    private BigDecimal R82_AMT_ADJ_BWP1;
    @Column(name = "R82_AMT_ADJ_BWP2")
    private BigDecimal R82_AMT_ADJ_BWP2;
    @Column(name = "R82_NET_AMT_BWP1")
    private BigDecimal R82_NET_AMT_BWP1;
    @Column(name = "R82_NET_AMT_BWP2")
    private BigDecimal R82_NET_AMT_BWP2;
    @Column(name = "R82_BAL_SUB_BWP1")
    private BigDecimal R82_BAL_SUB_BWP1;
    @Column(name = "R82_BAL_SUB_BWP2")
    private BigDecimal R82_BAL_SUB_BWP2;
    @Column(name = "R82_BAL_ACT_SUB_BWP1")
    private BigDecimal R82_BAL_ACT_SUB_BWP1;
    @Column(name = "R82_BAL_ACT_SUB_BWP2")
    private BigDecimal R82_BAL_ACT_SUB_BWP2;

    /* ================= R83 ================= */
    @Column(name = "R83_PRODUCT")
    private String R83_PRODUCT;
    @Column(name = "R83_FIG_BAL_BWP1")
    private BigDecimal R83_FIG_BAL_BWP1;
    @Column(name = "R83_FIG_BAL_BWP2")
    private BigDecimal R83_FIG_BAL_BWP2;
    @Column(name = "R83_AMT_ADJ_BWP1")
    private BigDecimal R83_AMT_ADJ_BWP1;
    @Column(name = "R83_AMT_ADJ_BWP2")
    private BigDecimal R83_AMT_ADJ_BWP2;
    @Column(name = "R83_NET_AMT_BWP1")
    private BigDecimal R83_NET_AMT_BWP1;
    @Column(name = "R83_NET_AMT_BWP2")
    private BigDecimal R83_NET_AMT_BWP2;
    @Column(name = "R83_BAL_SUB_BWP1")
    private BigDecimal R83_BAL_SUB_BWP1;
    @Column(name = "R83_BAL_SUB_BWP2")
    private BigDecimal R83_BAL_SUB_BWP2;
    @Column(name = "R83_BAL_ACT_SUB_BWP1")
    private BigDecimal R83_BAL_ACT_SUB_BWP1;
    @Column(name = "R83_BAL_ACT_SUB_BWP2")
    private BigDecimal R83_BAL_ACT_SUB_BWP2;

    /* ================= R84 ================= */
    @Column(name = "R84_PRODUCT")
    private String R84_PRODUCT;
    @Column(name = "R84_FIG_BAL_BWP1")
    private BigDecimal R84_FIG_BAL_BWP1;
    @Column(name = "R84_FIG_BAL_BWP2")
    private BigDecimal R84_FIG_BAL_BWP2;
    @Column(name = "R84_AMT_ADJ_BWP1")
    private BigDecimal R84_AMT_ADJ_BWP1;
    @Column(name = "R84_AMT_ADJ_BWP2")
    private BigDecimal R84_AMT_ADJ_BWP2;
    @Column(name = "R84_NET_AMT_BWP1")
    private BigDecimal R84_NET_AMT_BWP1;
    @Column(name = "R84_NET_AMT_BWP2")
    private BigDecimal R84_NET_AMT_BWP2;
    @Column(name = "R84_BAL_SUB_BWP1")
    private BigDecimal R84_BAL_SUB_BWP1;
    @Column(name = "R84_BAL_SUB_BWP2")
    private BigDecimal R84_BAL_SUB_BWP2;
    @Column(name = "R84_BAL_ACT_SUB_BWP1")
    private BigDecimal R84_BAL_ACT_SUB_BWP1;
    @Column(name = "R84_BAL_ACT_SUB_BWP2")
    private BigDecimal R84_BAL_ACT_SUB_BWP2;

    /* ================= R87 ================= */
    @Column(name = "R87_PRODUCT")
    private String R87_PRODUCT;

    @Column(name = "R87_FIG_BAL_BWP1")
    private BigDecimal R87_FIG_BAL_BWP1;

    @Column(name = "R87_FIG_BAL_BWP2")
    private BigDecimal R87_FIG_BAL_BWP2;

    @Column(name = "R87_AMT_ADJ_BWP1")
    private BigDecimal R87_AMT_ADJ_BWP1;

    @Column(name = "R87_AMT_ADJ_BWP2")
    private BigDecimal R87_AMT_ADJ_BWP2;

    @Column(name = "R87_NET_AMT_BWP1")
    private BigDecimal R87_NET_AMT_BWP1;

    @Column(name = "R87_NET_AMT_BWP2")
    private BigDecimal R87_NET_AMT_BWP2;

    @Column(name = "R87_BAL_SUB_BWP1")
    private BigDecimal R87_BAL_SUB_BWP1;

    @Column(name = "R87_BAL_SUB_BWP2")
    private BigDecimal R87_BAL_SUB_BWP2;

    @Column(name = "R87_BAL_ACT_SUB_BWP1")
    private BigDecimal R87_BAL_ACT_SUB_BWP1;

    @Column(name = "R87_BAL_ACT_SUB_BWP2")
    private BigDecimal R87_BAL_ACT_SUB_BWP2;

    /* ================= R88 ================= */
    @Column(name = "R88_PRODUCT")
    private String R88_PRODUCT;

    @Column(name = "R88_FIG_BAL_BWP1")
    private BigDecimal R88_FIG_BAL_BWP1;

    @Column(name = "R88_FIG_BAL_BWP2")
    private BigDecimal R88_FIG_BAL_BWP2;

    @Column(name = "R88_AMT_ADJ_BWP1")
    private BigDecimal R88_AMT_ADJ_BWP1;

    @Column(name = "R88_AMT_ADJ_BWP2")
    private BigDecimal R88_AMT_ADJ_BWP2;

    @Column(name = "R88_NET_AMT_BWP1")
    private BigDecimal R88_NET_AMT_BWP1;

    @Column(name = "R88_NET_AMT_BWP2")
    private BigDecimal R88_NET_AMT_BWP2;

    @Column(name = "R88_BAL_SUB_BWP1")
    private BigDecimal R88_BAL_SUB_BWP1;

    @Column(name = "R88_BAL_SUB_BWP2")
    private BigDecimal R88_BAL_SUB_BWP2;

    @Column(name = "R88_BAL_ACT_SUB_BWP1")
    private BigDecimal R88_BAL_ACT_SUB_BWP1;

    @Column(name = "R88_BAL_ACT_SUB_BWP2")
    private BigDecimal R88_BAL_ACT_SUB_BWP2;

    /* ================= R89 ================= */
    @Column(name = "R89_PRODUCT")
    private String R89_PRODUCT;

    @Column(name = "R89_FIG_BAL_BWP1")
    private BigDecimal R89_FIG_BAL_BWP1;

    @Column(name = "R89_FIG_BAL_BWP2")
    private BigDecimal R89_FIG_BAL_BWP2;

    @Column(name = "R89_AMT_ADJ_BWP1")
    private BigDecimal R89_AMT_ADJ_BWP1;

    @Column(name = "R89_AMT_ADJ_BWP2")
    private BigDecimal R89_AMT_ADJ_BWP2;

    @Column(name = "R89_NET_AMT_BWP1")
    private BigDecimal R89_NET_AMT_BWP1;

    @Column(name = "R89_NET_AMT_BWP2")
    private BigDecimal R89_NET_AMT_BWP2;

    @Column(name = "R89_BAL_SUB_BWP1")
    private BigDecimal R89_BAL_SUB_BWP1;

    @Column(name = "R89_BAL_SUB_BWP2")
    private BigDecimal R89_BAL_SUB_BWP2;

    @Column(name = "R89_BAL_ACT_SUB_BWP1")
    private BigDecimal R89_BAL_ACT_SUB_BWP1;

    @Column(name = "R89_BAL_ACT_SUB_BWP2")
    private BigDecimal R89_BAL_ACT_SUB_BWP2;

    /* ================= R90 ================= */
    @Column(name = "R90_PRODUCT")
    private String R90_PRODUCT;

    @Column(name = "R90_FIG_BAL_BWP1")
    private BigDecimal R90_FIG_BAL_BWP1;

    @Column(name = "R90_FIG_BAL_BWP2")
    private BigDecimal R90_FIG_BAL_BWP2;

    @Column(name = "R90_AMT_ADJ_BWP1")
    private BigDecimal R90_AMT_ADJ_BWP1;

    @Column(name = "R90_AMT_ADJ_BWP2")
    private BigDecimal R90_AMT_ADJ_BWP2;

    @Column(name = "R90_NET_AMT_BWP1")
    private BigDecimal R90_NET_AMT_BWP1;

    @Column(name = "R90_NET_AMT_BWP2")
    private BigDecimal R90_NET_AMT_BWP2;

    @Column(name = "R90_BAL_SUB_BWP1")
    private BigDecimal R90_BAL_SUB_BWP1;

    @Column(name = "R90_BAL_SUB_BWP2")
    private BigDecimal R90_BAL_SUB_BWP2;

    @Column(name = "R90_BAL_ACT_SUB_BWP1")
    private BigDecimal R90_BAL_ACT_SUB_BWP1;

    @Column(name = "R90_BAL_ACT_SUB_BWP2")
    private BigDecimal R90_BAL_ACT_SUB_BWP2;

    /* ================= R91 ================= */
    @Column(name = "R91_PRODUCT")
    private String R91_PRODUCT;

    @Column(name = "R91_FIG_BAL_BWP1")
    private BigDecimal R91_FIG_BAL_BWP1;

    @Column(name = "R91_FIG_BAL_BWP2")
    private BigDecimal R91_FIG_BAL_BWP2;

    @Column(name = "R91_AMT_ADJ_BWP1")
    private BigDecimal R91_AMT_ADJ_BWP1;

    @Column(name = "R91_AMT_ADJ_BWP2")
    private BigDecimal R91_AMT_ADJ_BWP2;

    @Column(name = "R91_NET_AMT_BWP1")
    private BigDecimal R91_NET_AMT_BWP1;

    @Column(name = "R91_NET_AMT_BWP2")
    private BigDecimal R91_NET_AMT_BWP2;

    @Column(name = "R91_BAL_SUB_BWP1")
    private BigDecimal R91_BAL_SUB_BWP1;

    @Column(name = "R91_BAL_SUB_BWP2")
    private BigDecimal R91_BAL_SUB_BWP2;

    @Column(name = "R91_BAL_ACT_SUB_BWP1")
    private BigDecimal R91_BAL_ACT_SUB_BWP1;

    @Column(name = "R91_BAL_ACT_SUB_BWP2")
    private BigDecimal R91_BAL_ACT_SUB_BWP2;

    /* ================= R92 ================= */
    @Column(name = "R92_PRODUCT")
    private String R92_PRODUCT;

    @Column(name = "R92_FIG_BAL_BWP1")
    private BigDecimal R92_FIG_BAL_BWP1;

    @Column(name = "R92_FIG_BAL_BWP2")
    private BigDecimal R92_FIG_BAL_BWP2;

    @Column(name = "R92_AMT_ADJ_BWP1")
    private BigDecimal R92_AMT_ADJ_BWP1;

    @Column(name = "R92_AMT_ADJ_BWP2")
    private BigDecimal R92_AMT_ADJ_BWP2;

    @Column(name = "R92_NET_AMT_BWP1")
    private BigDecimal R92_NET_AMT_BWP1;

    @Column(name = "R92_NET_AMT_BWP2")
    private BigDecimal R92_NET_AMT_BWP2;

    @Column(name = "R92_BAL_SUB_BWP1")
    private BigDecimal R92_BAL_SUB_BWP1;

    @Column(name = "R92_BAL_SUB_BWP2")
    private BigDecimal R92_BAL_SUB_BWP2;

    @Column(name = "R92_BAL_ACT_SUB_BWP1")
    private BigDecimal R92_BAL_ACT_SUB_BWP1;

    @Column(name = "R92_BAL_ACT_SUB_BWP2")
    private BigDecimal R92_BAL_ACT_SUB_BWP2;

    /* ================= R93 ================= */
    @Column(name = "R93_PRODUCT")
    private String R93_PRODUCT;

    @Column(name = "R93_FIG_BAL_BWP1")
    private BigDecimal R93_FIG_BAL_BWP1;

    @Column(name = "R93_FIG_BAL_BWP2")
    private BigDecimal R93_FIG_BAL_BWP2;

    @Column(name = "R93_AMT_ADJ_BWP1")
    private BigDecimal R93_AMT_ADJ_BWP1;

    @Column(name = "R93_AMT_ADJ_BWP2")
    private BigDecimal R93_AMT_ADJ_BWP2;

    @Column(name = "R93_NET_AMT_BWP1")
    private BigDecimal R93_NET_AMT_BWP1;

    @Column(name = "R93_NET_AMT_BWP2")
    private BigDecimal R93_NET_AMT_BWP2;

    @Column(name = "R93_BAL_SUB_BWP1")
    private BigDecimal R93_BAL_SUB_BWP1;

    @Column(name = "R93_BAL_SUB_BWP2")
    private BigDecimal R93_BAL_SUB_BWP2;

    @Column(name = "R93_BAL_ACT_SUB_BWP1")
    private BigDecimal R93_BAL_ACT_SUB_BWP1;

    @Column(name = "R93_BAL_ACT_SUB_BWP2")
    private BigDecimal R93_BAL_ACT_SUB_BWP2;

    /* ================= R94 ================= */
    @Column(name = "R94_PRODUCT")
    private String R94_PRODUCT;

    @Column(name = "R94_FIG_BAL_BWP1")
    private BigDecimal R94_FIG_BAL_BWP1;

    @Column(name = "R94_FIG_BAL_BWP2")
    private BigDecimal R94_FIG_BAL_BWP2;

    @Column(name = "R94_AMT_ADJ_BWP1")
    private BigDecimal R94_AMT_ADJ_BWP1;

    @Column(name = "R94_AMT_ADJ_BWP2")
    private BigDecimal R94_AMT_ADJ_BWP2;

    @Column(name = "R94_NET_AMT_BWP1")
    private BigDecimal R94_NET_AMT_BWP1;

    @Column(name = "R94_NET_AMT_BWP2")
    private BigDecimal R94_NET_AMT_BWP2;

    @Column(name = "R94_BAL_SUB_BWP1")
    private BigDecimal R94_BAL_SUB_BWP1;

    @Column(name = "R94_BAL_SUB_BWP2")
    private BigDecimal R94_BAL_SUB_BWP2;

    @Column(name = "R94_BAL_ACT_SUB_BWP1")
    private BigDecimal R94_BAL_ACT_SUB_BWP1;

    @Column(name = "R94_BAL_ACT_SUB_BWP2")
    private BigDecimal R94_BAL_ACT_SUB_BWP2;

    /* ================= R102 ================= */
    @Column(name = "R102_PRODUCT")
    private String R102_PRODUCT;

    @Column(name = "R102_FIG_BAL_BWP1")
    private BigDecimal R102_FIG_BAL_BWP1;

    @Column(name = "R102_FIG_BAL_BWP2")
    private BigDecimal R102_FIG_BAL_BWP2;

    @Column(name = "R102_AMT_ADJ_BWP1")
    private BigDecimal R102_AMT_ADJ_BWP1;

    @Column(name = "R102_AMT_ADJ_BWP2")
    private BigDecimal R102_AMT_ADJ_BWP2;

    @Column(name = "R102_NET_AMT_BWP1")
    private BigDecimal R102_NET_AMT_BWP1;

    @Column(name = "R102_NET_AMT_BWP2")
    private BigDecimal R102_NET_AMT_BWP2;

    @Column(name = "R102_BAL_SUB_BWP1")
    private BigDecimal R102_BAL_SUB_BWP1;

    @Column(name = "R102_BAL_SUB_BWP2")
    private BigDecimal R102_BAL_SUB_BWP2;

    @Column(name = "R102_BAL_ACT_SUB_BWP1")
    private BigDecimal R102_BAL_ACT_SUB_BWP1;

    @Column(name = "R102_BAL_ACT_SUB_BWP2")
    private BigDecimal R102_BAL_ACT_SUB_BWP2;

    /* ================= R103 ================= */
    @Column(name = "R103_PRODUCT")
    private String R103_PRODUCT;

    @Column(name = "R103_FIG_BAL_BWP1")
    private BigDecimal R103_FIG_BAL_BWP1;

    @Column(name = "R103_FIG_BAL_BWP2")
    private BigDecimal R103_FIG_BAL_BWP2;

    @Column(name = "R103_AMT_ADJ_BWP1")
    private BigDecimal R103_AMT_ADJ_BWP1;

    @Column(name = "R103_AMT_ADJ_BWP2")
    private BigDecimal R103_AMT_ADJ_BWP2;

    @Column(name = "R103_NET_AMT_BWP1")
    private BigDecimal R103_NET_AMT_BWP1;

    @Column(name = "R103_NET_AMT_BWP2")
    private BigDecimal R103_NET_AMT_BWP2;

    @Column(name = "R103_BAL_SUB_BWP1")
    private BigDecimal R103_BAL_SUB_BWP1;

    @Column(name = "R103_BAL_SUB_BWP2")
    private BigDecimal R103_BAL_SUB_BWP2;

    @Column(name = "R103_BAL_ACT_SUB_BWP1")
    private BigDecimal R103_BAL_ACT_SUB_BWP1;

    @Column(name = "R103_BAL_ACT_SUB_BWP2")
    private BigDecimal R103_BAL_ACT_SUB_BWP2;

    /* ================= R104 ================= */
    @Column(name = "R104_PRODUCT")
    private String R104_PRODUCT;

    @Column(name = "R104_FIG_BAL_BWP1")
    private BigDecimal R104_FIG_BAL_BWP1;

    @Column(name = "R104_FIG_BAL_BWP2")
    private BigDecimal R104_FIG_BAL_BWP2;

    @Column(name = "R104_AMT_ADJ_BWP1")
    private BigDecimal R104_AMT_ADJ_BWP1;

    @Column(name = "R104_AMT_ADJ_BWP2")
    private BigDecimal R104_AMT_ADJ_BWP2;

    @Column(name = "R104_NET_AMT_BWP1")
    private BigDecimal R104_NET_AMT_BWP1;

    @Column(name = "R104_NET_AMT_BWP2")
    private BigDecimal R104_NET_AMT_BWP2;

    @Column(name = "R104_BAL_SUB_BWP1")
    private BigDecimal R104_BAL_SUB_BWP1;

    @Column(name = "R104_BAL_SUB_BWP2")
    private BigDecimal R104_BAL_SUB_BWP2;

    @Column(name = "R104_BAL_ACT_SUB_BWP1")
    private BigDecimal R104_BAL_ACT_SUB_BWP1;

    @Column(name = "R104_BAL_ACT_SUB_BWP2")
    private BigDecimal R104_BAL_ACT_SUB_BWP2;

    /* ================= R105 ================= */
    @Column(name = "R105_PRODUCT")
    private String R105_PRODUCT;

    @Column(name = "R105_FIG_BAL_BWP1")
    private BigDecimal R105_FIG_BAL_BWP1;

    @Column(name = "R105_FIG_BAL_BWP2")
    private BigDecimal R105_FIG_BAL_BWP2;

    @Column(name = "R105_AMT_ADJ_BWP1")
    private BigDecimal R105_AMT_ADJ_BWP1;

    @Column(name = "R105_AMT_ADJ_BWP2")
    private BigDecimal R105_AMT_ADJ_BWP2;

    @Column(name = "R105_NET_AMT_BWP1")
    private BigDecimal R105_NET_AMT_BWP1;

    @Column(name = "R105_NET_AMT_BWP2")
    private BigDecimal R105_NET_AMT_BWP2;

    @Column(name = "R105_BAL_SUB_BWP1")
    private BigDecimal R105_BAL_SUB_BWP1;

    @Column(name = "R105_BAL_SUB_BWP2")
    private BigDecimal R105_BAL_SUB_BWP2;

    @Column(name = "R105_BAL_ACT_SUB_BWP1")
    private BigDecimal R105_BAL_ACT_SUB_BWP1;

    @Column(name = "R105_BAL_ACT_SUB_BWP2")
    private BigDecimal R105_BAL_ACT_SUB_BWP2;

    /* ================= R106 ================= */
    @Column(name = "R106_PRODUCT")
    private String R106_PRODUCT;

    @Column(name = "R106_FIG_BAL_BWP1")
    private BigDecimal R106_FIG_BAL_BWP1;

    @Column(name = "R106_FIG_BAL_BWP2")
    private BigDecimal R106_FIG_BAL_BWP2;

    @Column(name = "R106_AMT_ADJ_BWP1")
    private BigDecimal R106_AMT_ADJ_BWP1;

    @Column(name = "R106_AMT_ADJ_BWP2")
    private BigDecimal R106_AMT_ADJ_BWP2;

    @Column(name = "R106_NET_AMT_BWP1")
    private BigDecimal R106_NET_AMT_BWP1;

    @Column(name = "R106_NET_AMT_BWP2")
    private BigDecimal R106_NET_AMT_BWP2;

    @Column(name = "R106_BAL_SUB_BWP1")
    private BigDecimal R106_BAL_SUB_BWP1;

    @Column(name = "R106_BAL_SUB_BWP2")
    private BigDecimal R106_BAL_SUB_BWP2;

    @Column(name = "R106_BAL_ACT_SUB_BWP1")
    private BigDecimal R106_BAL_ACT_SUB_BWP1;

    @Column(name = "R106_BAL_ACT_SUB_BWP2")
    private BigDecimal R106_BAL_ACT_SUB_BWP2;

    /* ================= R107 ================= */
    @Column(name = "R107_PRODUCT")
    private String R107_PRODUCT;

    @Column(name = "R107_FIG_BAL_BWP1")
    private BigDecimal R107_FIG_BAL_BWP1;

    @Column(name = "R107_FIG_BAL_BWP2")
    private BigDecimal R107_FIG_BAL_BWP2;

    @Column(name = "R107_AMT_ADJ_BWP1")
    private BigDecimal R107_AMT_ADJ_BWP1;

    @Column(name = "R107_AMT_ADJ_BWP2")
    private BigDecimal R107_AMT_ADJ_BWP2;

    @Column(name = "R107_NET_AMT_BWP1")
    private BigDecimal R107_NET_AMT_BWP1;

    @Column(name = "R107_NET_AMT_BWP2")
    private BigDecimal R107_NET_AMT_BWP2;

    @Column(name = "R107_BAL_SUB_BWP1")
    private BigDecimal R107_BAL_SUB_BWP1;

    @Column(name = "R107_BAL_SUB_BWP2")
    private BigDecimal R107_BAL_SUB_BWP2;

    @Column(name = "R107_BAL_ACT_SUB_BWP1")
    private BigDecimal R107_BAL_ACT_SUB_BWP1;

    @Column(name = "R107_BAL_ACT_SUB_BWP2")
    private BigDecimal R107_BAL_ACT_SUB_BWP2;

    /* ================= R108 ================= */
    @Column(name = "R108_PRODUCT")
    private String R108_PRODUCT;

    @Column(name = "R108_FIG_BAL_BWP1")
    private BigDecimal R108_FIG_BAL_BWP1;

    @Column(name = "R108_FIG_BAL_BWP2")
    private BigDecimal R108_FIG_BAL_BWP2;

    @Column(name = "R108_AMT_ADJ_BWP1")
    private BigDecimal R108_AMT_ADJ_BWP1;

    @Column(name = "R108_AMT_ADJ_BWP2")
    private BigDecimal R108_AMT_ADJ_BWP2;

    @Column(name = "R108_NET_AMT_BWP1")
    private BigDecimal R108_NET_AMT_BWP1;

    @Column(name = "R108_NET_AMT_BWP2")
    private BigDecimal R108_NET_AMT_BWP2;

    @Column(name = "R108_BAL_SUB_BWP1")
    private BigDecimal R108_BAL_SUB_BWP1;

    @Column(name = "R108_BAL_SUB_BWP2")
    private BigDecimal R108_BAL_SUB_BWP2;

    @Column(name = "R108_BAL_ACT_SUB_BWP1")
    private BigDecimal R108_BAL_ACT_SUB_BWP1;

    @Column(name = "R108_BAL_ACT_SUB_BWP2")
    private BigDecimal R108_BAL_ACT_SUB_BWP2;

    /* ================= COMMON ================= */

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

	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}

	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}

	public BigDecimal getR11_FIG_BAL_BWP1() {
		return R11_FIG_BAL_BWP1;
	}

	public void setR11_FIG_BAL_BWP1(BigDecimal r11_FIG_BAL_BWP1) {
		R11_FIG_BAL_BWP1 = r11_FIG_BAL_BWP1;
	}

	public BigDecimal getR11_FIG_BAL_BWP2() {
		return R11_FIG_BAL_BWP2;
	}

	public void setR11_FIG_BAL_BWP2(BigDecimal r11_FIG_BAL_BWP2) {
		R11_FIG_BAL_BWP2 = r11_FIG_BAL_BWP2;
	}

	public BigDecimal getR11_AMT_ADJ_BWP1() {
		return R11_AMT_ADJ_BWP1;
	}

	public void setR11_AMT_ADJ_BWP1(BigDecimal r11_AMT_ADJ_BWP1) {
		R11_AMT_ADJ_BWP1 = r11_AMT_ADJ_BWP1;
	}

	public BigDecimal getR11_AMT_ADJ_BWP2() {
		return R11_AMT_ADJ_BWP2;
	}

	public void setR11_AMT_ADJ_BWP2(BigDecimal r11_AMT_ADJ_BWP2) {
		R11_AMT_ADJ_BWP2 = r11_AMT_ADJ_BWP2;
	}

	public BigDecimal getR11_NET_AMT_BWP1() {
		return R11_NET_AMT_BWP1;
	}

	public void setR11_NET_AMT_BWP1(BigDecimal r11_NET_AMT_BWP1) {
		R11_NET_AMT_BWP1 = r11_NET_AMT_BWP1;
	}

	public BigDecimal getR11_NET_AMT_BWP2() {
		return R11_NET_AMT_BWP2;
	}

	public void setR11_NET_AMT_BWP2(BigDecimal r11_NET_AMT_BWP2) {
		R11_NET_AMT_BWP2 = r11_NET_AMT_BWP2;
	}

	public BigDecimal getR11_BAL_SUB_BWP1() {
		return R11_BAL_SUB_BWP1;
	}

	public void setR11_BAL_SUB_BWP1(BigDecimal r11_BAL_SUB_BWP1) {
		R11_BAL_SUB_BWP1 = r11_BAL_SUB_BWP1;
	}

	public BigDecimal getR11_BAL_SUB_BWP2() {
		return R11_BAL_SUB_BWP2;
	}

	public void setR11_BAL_SUB_BWP2(BigDecimal r11_BAL_SUB_BWP2) {
		R11_BAL_SUB_BWP2 = r11_BAL_SUB_BWP2;
	}

	public BigDecimal getR11_BAL_ACT_SUB_BWP1() {
		return R11_BAL_ACT_SUB_BWP1;
	}

	public void setR11_BAL_ACT_SUB_BWP1(BigDecimal r11_BAL_ACT_SUB_BWP1) {
		R11_BAL_ACT_SUB_BWP1 = r11_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR11_BAL_ACT_SUB_BWP2() {
		return R11_BAL_ACT_SUB_BWP2;
	}

	public void setR11_BAL_ACT_SUB_BWP2(BigDecimal r11_BAL_ACT_SUB_BWP2) {
		R11_BAL_ACT_SUB_BWP2 = r11_BAL_ACT_SUB_BWP2;
	}

	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}

	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}

	public BigDecimal getR12_FIG_BAL_BWP1() {
		return R12_FIG_BAL_BWP1;
	}

	public void setR12_FIG_BAL_BWP1(BigDecimal r12_FIG_BAL_BWP1) {
		R12_FIG_BAL_BWP1 = r12_FIG_BAL_BWP1;
	}

	public BigDecimal getR12_FIG_BAL_BWP2() {
		return R12_FIG_BAL_BWP2;
	}

	public void setR12_FIG_BAL_BWP2(BigDecimal r12_FIG_BAL_BWP2) {
		R12_FIG_BAL_BWP2 = r12_FIG_BAL_BWP2;
	}

	public BigDecimal getR12_AMT_ADJ_BWP1() {
		return R12_AMT_ADJ_BWP1;
	}

	public void setR12_AMT_ADJ_BWP1(BigDecimal r12_AMT_ADJ_BWP1) {
		R12_AMT_ADJ_BWP1 = r12_AMT_ADJ_BWP1;
	}

	public BigDecimal getR12_AMT_ADJ_BWP2() {
		return R12_AMT_ADJ_BWP2;
	}

	public void setR12_AMT_ADJ_BWP2(BigDecimal r12_AMT_ADJ_BWP2) {
		R12_AMT_ADJ_BWP2 = r12_AMT_ADJ_BWP2;
	}

	public BigDecimal getR12_NET_AMT_BWP1() {
		return R12_NET_AMT_BWP1;
	}

	public void setR12_NET_AMT_BWP1(BigDecimal r12_NET_AMT_BWP1) {
		R12_NET_AMT_BWP1 = r12_NET_AMT_BWP1;
	}

	public BigDecimal getR12_NET_AMT_BWP2() {
		return R12_NET_AMT_BWP2;
	}

	public void setR12_NET_AMT_BWP2(BigDecimal r12_NET_AMT_BWP2) {
		R12_NET_AMT_BWP2 = r12_NET_AMT_BWP2;
	}

	public BigDecimal getR12_BAL_SUB_BWP1() {
		return R12_BAL_SUB_BWP1;
	}

	public void setR12_BAL_SUB_BWP1(BigDecimal r12_BAL_SUB_BWP1) {
		R12_BAL_SUB_BWP1 = r12_BAL_SUB_BWP1;
	}

	public BigDecimal getR12_BAL_SUB_BWP2() {
		return R12_BAL_SUB_BWP2;
	}

	public void setR12_BAL_SUB_BWP2(BigDecimal r12_BAL_SUB_BWP2) {
		R12_BAL_SUB_BWP2 = r12_BAL_SUB_BWP2;
	}

	public BigDecimal getR12_BAL_ACT_SUB_BWP1() {
		return R12_BAL_ACT_SUB_BWP1;
	}

	public void setR12_BAL_ACT_SUB_BWP1(BigDecimal r12_BAL_ACT_SUB_BWP1) {
		R12_BAL_ACT_SUB_BWP1 = r12_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR12_BAL_ACT_SUB_BWP2() {
		return R12_BAL_ACT_SUB_BWP2;
	}

	public void setR12_BAL_ACT_SUB_BWP2(BigDecimal r12_BAL_ACT_SUB_BWP2) {
		R12_BAL_ACT_SUB_BWP2 = r12_BAL_ACT_SUB_BWP2;
	}

	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}

	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}

	public BigDecimal getR13_FIG_BAL_BWP1() {
		return R13_FIG_BAL_BWP1;
	}

	public void setR13_FIG_BAL_BWP1(BigDecimal r13_FIG_BAL_BWP1) {
		R13_FIG_BAL_BWP1 = r13_FIG_BAL_BWP1;
	}

	public BigDecimal getR13_FIG_BAL_BWP2() {
		return R13_FIG_BAL_BWP2;
	}

	public void setR13_FIG_BAL_BWP2(BigDecimal r13_FIG_BAL_BWP2) {
		R13_FIG_BAL_BWP2 = r13_FIG_BAL_BWP2;
	}

	public BigDecimal getR13_AMT_ADJ_BWP1() {
		return R13_AMT_ADJ_BWP1;
	}

	public void setR13_AMT_ADJ_BWP1(BigDecimal r13_AMT_ADJ_BWP1) {
		R13_AMT_ADJ_BWP1 = r13_AMT_ADJ_BWP1;
	}

	public BigDecimal getR13_AMT_ADJ_BWP2() {
		return R13_AMT_ADJ_BWP2;
	}

	public void setR13_AMT_ADJ_BWP2(BigDecimal r13_AMT_ADJ_BWP2) {
		R13_AMT_ADJ_BWP2 = r13_AMT_ADJ_BWP2;
	}

	public BigDecimal getR13_NET_AMT_BWP1() {
		return R13_NET_AMT_BWP1;
	}

	public void setR13_NET_AMT_BWP1(BigDecimal r13_NET_AMT_BWP1) {
		R13_NET_AMT_BWP1 = r13_NET_AMT_BWP1;
	}

	public BigDecimal getR13_NET_AMT_BWP2() {
		return R13_NET_AMT_BWP2;
	}

	public void setR13_NET_AMT_BWP2(BigDecimal r13_NET_AMT_BWP2) {
		R13_NET_AMT_BWP2 = r13_NET_AMT_BWP2;
	}

	public BigDecimal getR13_BAL_SUB_BWP1() {
		return R13_BAL_SUB_BWP1;
	}

	public void setR13_BAL_SUB_BWP1(BigDecimal r13_BAL_SUB_BWP1) {
		R13_BAL_SUB_BWP1 = r13_BAL_SUB_BWP1;
	}

	public BigDecimal getR13_BAL_SUB_BWP2() {
		return R13_BAL_SUB_BWP2;
	}

	public void setR13_BAL_SUB_BWP2(BigDecimal r13_BAL_SUB_BWP2) {
		R13_BAL_SUB_BWP2 = r13_BAL_SUB_BWP2;
	}

	public BigDecimal getR13_BAL_ACT_SUB_BWP1() {
		return R13_BAL_ACT_SUB_BWP1;
	}

	public void setR13_BAL_ACT_SUB_BWP1(BigDecimal r13_BAL_ACT_SUB_BWP1) {
		R13_BAL_ACT_SUB_BWP1 = r13_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR13_BAL_ACT_SUB_BWP2() {
		return R13_BAL_ACT_SUB_BWP2;
	}

	public void setR13_BAL_ACT_SUB_BWP2(BigDecimal r13_BAL_ACT_SUB_BWP2) {
		R13_BAL_ACT_SUB_BWP2 = r13_BAL_ACT_SUB_BWP2;
	}

	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}

	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}

	public BigDecimal getR14_FIG_BAL_BWP1() {
		return R14_FIG_BAL_BWP1;
	}

	public void setR14_FIG_BAL_BWP1(BigDecimal r14_FIG_BAL_BWP1) {
		R14_FIG_BAL_BWP1 = r14_FIG_BAL_BWP1;
	}

	public BigDecimal getR14_FIG_BAL_BWP2() {
		return R14_FIG_BAL_BWP2;
	}

	public void setR14_FIG_BAL_BWP2(BigDecimal r14_FIG_BAL_BWP2) {
		R14_FIG_BAL_BWP2 = r14_FIG_BAL_BWP2;
	}

	public BigDecimal getR14_AMT_ADJ_BWP1() {
		return R14_AMT_ADJ_BWP1;
	}

	public void setR14_AMT_ADJ_BWP1(BigDecimal r14_AMT_ADJ_BWP1) {
		R14_AMT_ADJ_BWP1 = r14_AMT_ADJ_BWP1;
	}

	public BigDecimal getR14_AMT_ADJ_BWP2() {
		return R14_AMT_ADJ_BWP2;
	}

	public void setR14_AMT_ADJ_BWP2(BigDecimal r14_AMT_ADJ_BWP2) {
		R14_AMT_ADJ_BWP2 = r14_AMT_ADJ_BWP2;
	}

	public BigDecimal getR14_NET_AMT_BWP1() {
		return R14_NET_AMT_BWP1;
	}

	public void setR14_NET_AMT_BWP1(BigDecimal r14_NET_AMT_BWP1) {
		R14_NET_AMT_BWP1 = r14_NET_AMT_BWP1;
	}

	public BigDecimal getR14_NET_AMT_BWP2() {
		return R14_NET_AMT_BWP2;
	}

	public void setR14_NET_AMT_BWP2(BigDecimal r14_NET_AMT_BWP2) {
		R14_NET_AMT_BWP2 = r14_NET_AMT_BWP2;
	}

	public BigDecimal getR14_BAL_SUB_BWP1() {
		return R14_BAL_SUB_BWP1;
	}

	public void setR14_BAL_SUB_BWP1(BigDecimal r14_BAL_SUB_BWP1) {
		R14_BAL_SUB_BWP1 = r14_BAL_SUB_BWP1;
	}

	public BigDecimal getR14_BAL_SUB_BWP2() {
		return R14_BAL_SUB_BWP2;
	}

	public void setR14_BAL_SUB_BWP2(BigDecimal r14_BAL_SUB_BWP2) {
		R14_BAL_SUB_BWP2 = r14_BAL_SUB_BWP2;
	}

	public BigDecimal getR14_BAL_ACT_SUB_BWP1() {
		return R14_BAL_ACT_SUB_BWP1;
	}

	public void setR14_BAL_ACT_SUB_BWP1(BigDecimal r14_BAL_ACT_SUB_BWP1) {
		R14_BAL_ACT_SUB_BWP1 = r14_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR14_BAL_ACT_SUB_BWP2() {
		return R14_BAL_ACT_SUB_BWP2;
	}

	public void setR14_BAL_ACT_SUB_BWP2(BigDecimal r14_BAL_ACT_SUB_BWP2) {
		R14_BAL_ACT_SUB_BWP2 = r14_BAL_ACT_SUB_BWP2;
	}

	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}

	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}

	public BigDecimal getR15_FIG_BAL_BWP1() {
		return R15_FIG_BAL_BWP1;
	}

	public void setR15_FIG_BAL_BWP1(BigDecimal r15_FIG_BAL_BWP1) {
		R15_FIG_BAL_BWP1 = r15_FIG_BAL_BWP1;
	}

	public BigDecimal getR15_FIG_BAL_BWP2() {
		return R15_FIG_BAL_BWP2;
	}

	public void setR15_FIG_BAL_BWP2(BigDecimal r15_FIG_BAL_BWP2) {
		R15_FIG_BAL_BWP2 = r15_FIG_BAL_BWP2;
	}

	public BigDecimal getR15_AMT_ADJ_BWP1() {
		return R15_AMT_ADJ_BWP1;
	}

	public void setR15_AMT_ADJ_BWP1(BigDecimal r15_AMT_ADJ_BWP1) {
		R15_AMT_ADJ_BWP1 = r15_AMT_ADJ_BWP1;
	}

	public BigDecimal getR15_AMT_ADJ_BWP2() {
		return R15_AMT_ADJ_BWP2;
	}

	public void setR15_AMT_ADJ_BWP2(BigDecimal r15_AMT_ADJ_BWP2) {
		R15_AMT_ADJ_BWP2 = r15_AMT_ADJ_BWP2;
	}

	public BigDecimal getR15_NET_AMT_BWP1() {
		return R15_NET_AMT_BWP1;
	}

	public void setR15_NET_AMT_BWP1(BigDecimal r15_NET_AMT_BWP1) {
		R15_NET_AMT_BWP1 = r15_NET_AMT_BWP1;
	}

	public BigDecimal getR15_NET_AMT_BWP2() {
		return R15_NET_AMT_BWP2;
	}

	public void setR15_NET_AMT_BWP2(BigDecimal r15_NET_AMT_BWP2) {
		R15_NET_AMT_BWP2 = r15_NET_AMT_BWP2;
	}

	public BigDecimal getR15_BAL_SUB_BWP1() {
		return R15_BAL_SUB_BWP1;
	}

	public void setR15_BAL_SUB_BWP1(BigDecimal r15_BAL_SUB_BWP1) {
		R15_BAL_SUB_BWP1 = r15_BAL_SUB_BWP1;
	}

	public BigDecimal getR15_BAL_SUB_BWP2() {
		return R15_BAL_SUB_BWP2;
	}

	public void setR15_BAL_SUB_BWP2(BigDecimal r15_BAL_SUB_BWP2) {
		R15_BAL_SUB_BWP2 = r15_BAL_SUB_BWP2;
	}

	public BigDecimal getR15_BAL_ACT_SUB_BWP1() {
		return R15_BAL_ACT_SUB_BWP1;
	}

	public void setR15_BAL_ACT_SUB_BWP1(BigDecimal r15_BAL_ACT_SUB_BWP1) {
		R15_BAL_ACT_SUB_BWP1 = r15_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR15_BAL_ACT_SUB_BWP2() {
		return R15_BAL_ACT_SUB_BWP2;
	}

	public void setR15_BAL_ACT_SUB_BWP2(BigDecimal r15_BAL_ACT_SUB_BWP2) {
		R15_BAL_ACT_SUB_BWP2 = r15_BAL_ACT_SUB_BWP2;
	}

	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}

	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}

	public BigDecimal getR16_FIG_BAL_BWP1() {
		return R16_FIG_BAL_BWP1;
	}

	public void setR16_FIG_BAL_BWP1(BigDecimal r16_FIG_BAL_BWP1) {
		R16_FIG_BAL_BWP1 = r16_FIG_BAL_BWP1;
	}

	public BigDecimal getR16_FIG_BAL_BWP2() {
		return R16_FIG_BAL_BWP2;
	}

	public void setR16_FIG_BAL_BWP2(BigDecimal r16_FIG_BAL_BWP2) {
		R16_FIG_BAL_BWP2 = r16_FIG_BAL_BWP2;
	}

	public BigDecimal getR16_AMT_ADJ_BWP1() {
		return R16_AMT_ADJ_BWP1;
	}

	public void setR16_AMT_ADJ_BWP1(BigDecimal r16_AMT_ADJ_BWP1) {
		R16_AMT_ADJ_BWP1 = r16_AMT_ADJ_BWP1;
	}

	public BigDecimal getR16_AMT_ADJ_BWP2() {
		return R16_AMT_ADJ_BWP2;
	}

	public void setR16_AMT_ADJ_BWP2(BigDecimal r16_AMT_ADJ_BWP2) {
		R16_AMT_ADJ_BWP2 = r16_AMT_ADJ_BWP2;
	}

	public BigDecimal getR16_NET_AMT_BWP1() {
		return R16_NET_AMT_BWP1;
	}

	public void setR16_NET_AMT_BWP1(BigDecimal r16_NET_AMT_BWP1) {
		R16_NET_AMT_BWP1 = r16_NET_AMT_BWP1;
	}

	public BigDecimal getR16_NET_AMT_BWP2() {
		return R16_NET_AMT_BWP2;
	}

	public void setR16_NET_AMT_BWP2(BigDecimal r16_NET_AMT_BWP2) {
		R16_NET_AMT_BWP2 = r16_NET_AMT_BWP2;
	}

	public BigDecimal getR16_BAL_SUB_BWP1() {
		return R16_BAL_SUB_BWP1;
	}

	public void setR16_BAL_SUB_BWP1(BigDecimal r16_BAL_SUB_BWP1) {
		R16_BAL_SUB_BWP1 = r16_BAL_SUB_BWP1;
	}

	public BigDecimal getR16_BAL_SUB_BWP2() {
		return R16_BAL_SUB_BWP2;
	}

	public void setR16_BAL_SUB_BWP2(BigDecimal r16_BAL_SUB_BWP2) {
		R16_BAL_SUB_BWP2 = r16_BAL_SUB_BWP2;
	}

	public BigDecimal getR16_BAL_ACT_SUB_BWP1() {
		return R16_BAL_ACT_SUB_BWP1;
	}

	public void setR16_BAL_ACT_SUB_BWP1(BigDecimal r16_BAL_ACT_SUB_BWP1) {
		R16_BAL_ACT_SUB_BWP1 = r16_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR16_BAL_ACT_SUB_BWP2() {
		return R16_BAL_ACT_SUB_BWP2;
	}

	public void setR16_BAL_ACT_SUB_BWP2(BigDecimal r16_BAL_ACT_SUB_BWP2) {
		R16_BAL_ACT_SUB_BWP2 = r16_BAL_ACT_SUB_BWP2;
	}

	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}

	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}

	public BigDecimal getR17_FIG_BAL_BWP1() {
		return R17_FIG_BAL_BWP1;
	}

	public void setR17_FIG_BAL_BWP1(BigDecimal r17_FIG_BAL_BWP1) {
		R17_FIG_BAL_BWP1 = r17_FIG_BAL_BWP1;
	}

	public BigDecimal getR17_FIG_BAL_BWP2() {
		return R17_FIG_BAL_BWP2;
	}

	public void setR17_FIG_BAL_BWP2(BigDecimal r17_FIG_BAL_BWP2) {
		R17_FIG_BAL_BWP2 = r17_FIG_BAL_BWP2;
	}

	public BigDecimal getR17_AMT_ADJ_BWP1() {
		return R17_AMT_ADJ_BWP1;
	}

	public void setR17_AMT_ADJ_BWP1(BigDecimal r17_AMT_ADJ_BWP1) {
		R17_AMT_ADJ_BWP1 = r17_AMT_ADJ_BWP1;
	}

	public BigDecimal getR17_AMT_ADJ_BWP2() {
		return R17_AMT_ADJ_BWP2;
	}

	public void setR17_AMT_ADJ_BWP2(BigDecimal r17_AMT_ADJ_BWP2) {
		R17_AMT_ADJ_BWP2 = r17_AMT_ADJ_BWP2;
	}

	public BigDecimal getR17_NET_AMT_BWP1() {
		return R17_NET_AMT_BWP1;
	}

	public void setR17_NET_AMT_BWP1(BigDecimal r17_NET_AMT_BWP1) {
		R17_NET_AMT_BWP1 = r17_NET_AMT_BWP1;
	}

	public BigDecimal getR17_NET_AMT_BWP2() {
		return R17_NET_AMT_BWP2;
	}

	public void setR17_NET_AMT_BWP2(BigDecimal r17_NET_AMT_BWP2) {
		R17_NET_AMT_BWP2 = r17_NET_AMT_BWP2;
	}

	public BigDecimal getR17_BAL_SUB_BWP1() {
		return R17_BAL_SUB_BWP1;
	}

	public void setR17_BAL_SUB_BWP1(BigDecimal r17_BAL_SUB_BWP1) {
		R17_BAL_SUB_BWP1 = r17_BAL_SUB_BWP1;
	}

	public BigDecimal getR17_BAL_SUB_BWP2() {
		return R17_BAL_SUB_BWP2;
	}

	public void setR17_BAL_SUB_BWP2(BigDecimal r17_BAL_SUB_BWP2) {
		R17_BAL_SUB_BWP2 = r17_BAL_SUB_BWP2;
	}

	public BigDecimal getR17_BAL_ACT_SUB_BWP1() {
		return R17_BAL_ACT_SUB_BWP1;
	}

	public void setR17_BAL_ACT_SUB_BWP1(BigDecimal r17_BAL_ACT_SUB_BWP1) {
		R17_BAL_ACT_SUB_BWP1 = r17_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR17_BAL_ACT_SUB_BWP2() {
		return R17_BAL_ACT_SUB_BWP2;
	}

	public void setR17_BAL_ACT_SUB_BWP2(BigDecimal r17_BAL_ACT_SUB_BWP2) {
		R17_BAL_ACT_SUB_BWP2 = r17_BAL_ACT_SUB_BWP2;
	}

	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}

	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}

	public BigDecimal getR18_FIG_BAL_BWP1() {
		return R18_FIG_BAL_BWP1;
	}

	public void setR18_FIG_BAL_BWP1(BigDecimal r18_FIG_BAL_BWP1) {
		R18_FIG_BAL_BWP1 = r18_FIG_BAL_BWP1;
	}

	public BigDecimal getR18_FIG_BAL_BWP2() {
		return R18_FIG_BAL_BWP2;
	}

	public void setR18_FIG_BAL_BWP2(BigDecimal r18_FIG_BAL_BWP2) {
		R18_FIG_BAL_BWP2 = r18_FIG_BAL_BWP2;
	}

	public BigDecimal getR18_AMT_ADJ_BWP1() {
		return R18_AMT_ADJ_BWP1;
	}

	public void setR18_AMT_ADJ_BWP1(BigDecimal r18_AMT_ADJ_BWP1) {
		R18_AMT_ADJ_BWP1 = r18_AMT_ADJ_BWP1;
	}

	public BigDecimal getR18_AMT_ADJ_BWP2() {
		return R18_AMT_ADJ_BWP2;
	}

	public void setR18_AMT_ADJ_BWP2(BigDecimal r18_AMT_ADJ_BWP2) {
		R18_AMT_ADJ_BWP2 = r18_AMT_ADJ_BWP2;
	}

	public BigDecimal getR18_NET_AMT_BWP1() {
		return R18_NET_AMT_BWP1;
	}

	public void setR18_NET_AMT_BWP1(BigDecimal r18_NET_AMT_BWP1) {
		R18_NET_AMT_BWP1 = r18_NET_AMT_BWP1;
	}

	public BigDecimal getR18_NET_AMT_BWP2() {
		return R18_NET_AMT_BWP2;
	}

	public void setR18_NET_AMT_BWP2(BigDecimal r18_NET_AMT_BWP2) {
		R18_NET_AMT_BWP2 = r18_NET_AMT_BWP2;
	}

	public BigDecimal getR18_BAL_SUB_BWP1() {
		return R18_BAL_SUB_BWP1;
	}

	public void setR18_BAL_SUB_BWP1(BigDecimal r18_BAL_SUB_BWP1) {
		R18_BAL_SUB_BWP1 = r18_BAL_SUB_BWP1;
	}

	public BigDecimal getR18_BAL_SUB_BWP2() {
		return R18_BAL_SUB_BWP2;
	}

	public void setR18_BAL_SUB_BWP2(BigDecimal r18_BAL_SUB_BWP2) {
		R18_BAL_SUB_BWP2 = r18_BAL_SUB_BWP2;
	}

	public BigDecimal getR18_BAL_ACT_SUB_BWP1() {
		return R18_BAL_ACT_SUB_BWP1;
	}

	public void setR18_BAL_ACT_SUB_BWP1(BigDecimal r18_BAL_ACT_SUB_BWP1) {
		R18_BAL_ACT_SUB_BWP1 = r18_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR18_BAL_ACT_SUB_BWP2() {
		return R18_BAL_ACT_SUB_BWP2;
	}

	public void setR18_BAL_ACT_SUB_BWP2(BigDecimal r18_BAL_ACT_SUB_BWP2) {
		R18_BAL_ACT_SUB_BWP2 = r18_BAL_ACT_SUB_BWP2;
	}

	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}

	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}

	public BigDecimal getR19_FIG_BAL_BWP1() {
		return R19_FIG_BAL_BWP1;
	}

	public void setR19_FIG_BAL_BWP1(BigDecimal r19_FIG_BAL_BWP1) {
		R19_FIG_BAL_BWP1 = r19_FIG_BAL_BWP1;
	}

	public BigDecimal getR19_FIG_BAL_BWP2() {
		return R19_FIG_BAL_BWP2;
	}

	public void setR19_FIG_BAL_BWP2(BigDecimal r19_FIG_BAL_BWP2) {
		R19_FIG_BAL_BWP2 = r19_FIG_BAL_BWP2;
	}

	public BigDecimal getR19_AMT_ADJ_BWP1() {
		return R19_AMT_ADJ_BWP1;
	}

	public void setR19_AMT_ADJ_BWP1(BigDecimal r19_AMT_ADJ_BWP1) {
		R19_AMT_ADJ_BWP1 = r19_AMT_ADJ_BWP1;
	}

	public BigDecimal getR19_AMT_ADJ_BWP2() {
		return R19_AMT_ADJ_BWP2;
	}

	public void setR19_AMT_ADJ_BWP2(BigDecimal r19_AMT_ADJ_BWP2) {
		R19_AMT_ADJ_BWP2 = r19_AMT_ADJ_BWP2;
	}

	public BigDecimal getR19_NET_AMT_BWP1() {
		return R19_NET_AMT_BWP1;
	}

	public void setR19_NET_AMT_BWP1(BigDecimal r19_NET_AMT_BWP1) {
		R19_NET_AMT_BWP1 = r19_NET_AMT_BWP1;
	}

	public BigDecimal getR19_NET_AMT_BWP2() {
		return R19_NET_AMT_BWP2;
	}

	public void setR19_NET_AMT_BWP2(BigDecimal r19_NET_AMT_BWP2) {
		R19_NET_AMT_BWP2 = r19_NET_AMT_BWP2;
	}

	public BigDecimal getR19_BAL_SUB_BWP1() {
		return R19_BAL_SUB_BWP1;
	}

	public void setR19_BAL_SUB_BWP1(BigDecimal r19_BAL_SUB_BWP1) {
		R19_BAL_SUB_BWP1 = r19_BAL_SUB_BWP1;
	}

	public BigDecimal getR19_BAL_SUB_BWP2() {
		return R19_BAL_SUB_BWP2;
	}

	public void setR19_BAL_SUB_BWP2(BigDecimal r19_BAL_SUB_BWP2) {
		R19_BAL_SUB_BWP2 = r19_BAL_SUB_BWP2;
	}

	public BigDecimal getR19_BAL_ACT_SUB_BWP1() {
		return R19_BAL_ACT_SUB_BWP1;
	}

	public void setR19_BAL_ACT_SUB_BWP1(BigDecimal r19_BAL_ACT_SUB_BWP1) {
		R19_BAL_ACT_SUB_BWP1 = r19_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR19_BAL_ACT_SUB_BWP2() {
		return R19_BAL_ACT_SUB_BWP2;
	}

	public void setR19_BAL_ACT_SUB_BWP2(BigDecimal r19_BAL_ACT_SUB_BWP2) {
		R19_BAL_ACT_SUB_BWP2 = r19_BAL_ACT_SUB_BWP2;
	}

	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}

	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}

	public BigDecimal getR20_FIG_BAL_BWP1() {
		return R20_FIG_BAL_BWP1;
	}

	public void setR20_FIG_BAL_BWP1(BigDecimal r20_FIG_BAL_BWP1) {
		R20_FIG_BAL_BWP1 = r20_FIG_BAL_BWP1;
	}

	public BigDecimal getR20_FIG_BAL_BWP2() {
		return R20_FIG_BAL_BWP2;
	}

	public void setR20_FIG_BAL_BWP2(BigDecimal r20_FIG_BAL_BWP2) {
		R20_FIG_BAL_BWP2 = r20_FIG_BAL_BWP2;
	}

	public BigDecimal getR20_AMT_ADJ_BWP1() {
		return R20_AMT_ADJ_BWP1;
	}

	public void setR20_AMT_ADJ_BWP1(BigDecimal r20_AMT_ADJ_BWP1) {
		R20_AMT_ADJ_BWP1 = r20_AMT_ADJ_BWP1;
	}

	public BigDecimal getR20_AMT_ADJ_BWP2() {
		return R20_AMT_ADJ_BWP2;
	}

	public void setR20_AMT_ADJ_BWP2(BigDecimal r20_AMT_ADJ_BWP2) {
		R20_AMT_ADJ_BWP2 = r20_AMT_ADJ_BWP2;
	}

	public BigDecimal getR20_NET_AMT_BWP1() {
		return R20_NET_AMT_BWP1;
	}

	public void setR20_NET_AMT_BWP1(BigDecimal r20_NET_AMT_BWP1) {
		R20_NET_AMT_BWP1 = r20_NET_AMT_BWP1;
	}

	public BigDecimal getR20_NET_AMT_BWP2() {
		return R20_NET_AMT_BWP2;
	}

	public void setR20_NET_AMT_BWP2(BigDecimal r20_NET_AMT_BWP2) {
		R20_NET_AMT_BWP2 = r20_NET_AMT_BWP2;
	}

	public BigDecimal getR20_BAL_SUB_BWP1() {
		return R20_BAL_SUB_BWP1;
	}

	public void setR20_BAL_SUB_BWP1(BigDecimal r20_BAL_SUB_BWP1) {
		R20_BAL_SUB_BWP1 = r20_BAL_SUB_BWP1;
	}

	public BigDecimal getR20_BAL_SUB_BWP2() {
		return R20_BAL_SUB_BWP2;
	}

	public void setR20_BAL_SUB_BWP2(BigDecimal r20_BAL_SUB_BWP2) {
		R20_BAL_SUB_BWP2 = r20_BAL_SUB_BWP2;
	}

	public BigDecimal getR20_BAL_ACT_SUB_BWP1() {
		return R20_BAL_ACT_SUB_BWP1;
	}

	public void setR20_BAL_ACT_SUB_BWP1(BigDecimal r20_BAL_ACT_SUB_BWP1) {
		R20_BAL_ACT_SUB_BWP1 = r20_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR20_BAL_ACT_SUB_BWP2() {
		return R20_BAL_ACT_SUB_BWP2;
	}

	public void setR20_BAL_ACT_SUB_BWP2(BigDecimal r20_BAL_ACT_SUB_BWP2) {
		R20_BAL_ACT_SUB_BWP2 = r20_BAL_ACT_SUB_BWP2;
	}

	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}

	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}

	public BigDecimal getR21_FIG_BAL_BWP1() {
		return R21_FIG_BAL_BWP1;
	}

	public void setR21_FIG_BAL_BWP1(BigDecimal r21_FIG_BAL_BWP1) {
		R21_FIG_BAL_BWP1 = r21_FIG_BAL_BWP1;
	}

	public BigDecimal getR21_FIG_BAL_BWP2() {
		return R21_FIG_BAL_BWP2;
	}

	public void setR21_FIG_BAL_BWP2(BigDecimal r21_FIG_BAL_BWP2) {
		R21_FIG_BAL_BWP2 = r21_FIG_BAL_BWP2;
	}

	public BigDecimal getR21_AMT_ADJ_BWP1() {
		return R21_AMT_ADJ_BWP1;
	}

	public void setR21_AMT_ADJ_BWP1(BigDecimal r21_AMT_ADJ_BWP1) {
		R21_AMT_ADJ_BWP1 = r21_AMT_ADJ_BWP1;
	}

	public BigDecimal getR21_AMT_ADJ_BWP2() {
		return R21_AMT_ADJ_BWP2;
	}

	public void setR21_AMT_ADJ_BWP2(BigDecimal r21_AMT_ADJ_BWP2) {
		R21_AMT_ADJ_BWP2 = r21_AMT_ADJ_BWP2;
	}

	public BigDecimal getR21_NET_AMT_BWP1() {
		return R21_NET_AMT_BWP1;
	}

	public void setR21_NET_AMT_BWP1(BigDecimal r21_NET_AMT_BWP1) {
		R21_NET_AMT_BWP1 = r21_NET_AMT_BWP1;
	}

	public BigDecimal getR21_NET_AMT_BWP2() {
		return R21_NET_AMT_BWP2;
	}

	public void setR21_NET_AMT_BWP2(BigDecimal r21_NET_AMT_BWP2) {
		R21_NET_AMT_BWP2 = r21_NET_AMT_BWP2;
	}

	public BigDecimal getR21_BAL_SUB_BWP1() {
		return R21_BAL_SUB_BWP1;
	}

	public void setR21_BAL_SUB_BWP1(BigDecimal r21_BAL_SUB_BWP1) {
		R21_BAL_SUB_BWP1 = r21_BAL_SUB_BWP1;
	}

	public BigDecimal getR21_BAL_SUB_BWP2() {
		return R21_BAL_SUB_BWP2;
	}

	public void setR21_BAL_SUB_BWP2(BigDecimal r21_BAL_SUB_BWP2) {
		R21_BAL_SUB_BWP2 = r21_BAL_SUB_BWP2;
	}

	public BigDecimal getR21_BAL_ACT_SUB_BWP1() {
		return R21_BAL_ACT_SUB_BWP1;
	}

	public void setR21_BAL_ACT_SUB_BWP1(BigDecimal r21_BAL_ACT_SUB_BWP1) {
		R21_BAL_ACT_SUB_BWP1 = r21_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR21_BAL_ACT_SUB_BWP2() {
		return R21_BAL_ACT_SUB_BWP2;
	}

	public void setR21_BAL_ACT_SUB_BWP2(BigDecimal r21_BAL_ACT_SUB_BWP2) {
		R21_BAL_ACT_SUB_BWP2 = r21_BAL_ACT_SUB_BWP2;
	}

	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}

	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}

	public BigDecimal getR22_FIG_BAL_BWP1() {
		return R22_FIG_BAL_BWP1;
	}

	public void setR22_FIG_BAL_BWP1(BigDecimal r22_FIG_BAL_BWP1) {
		R22_FIG_BAL_BWP1 = r22_FIG_BAL_BWP1;
	}

	public BigDecimal getR22_FIG_BAL_BWP2() {
		return R22_FIG_BAL_BWP2;
	}

	public void setR22_FIG_BAL_BWP2(BigDecimal r22_FIG_BAL_BWP2) {
		R22_FIG_BAL_BWP2 = r22_FIG_BAL_BWP2;
	}

	public BigDecimal getR22_AMT_ADJ_BWP1() {
		return R22_AMT_ADJ_BWP1;
	}

	public void setR22_AMT_ADJ_BWP1(BigDecimal r22_AMT_ADJ_BWP1) {
		R22_AMT_ADJ_BWP1 = r22_AMT_ADJ_BWP1;
	}

	public BigDecimal getR22_AMT_ADJ_BWP2() {
		return R22_AMT_ADJ_BWP2;
	}

	public void setR22_AMT_ADJ_BWP2(BigDecimal r22_AMT_ADJ_BWP2) {
		R22_AMT_ADJ_BWP2 = r22_AMT_ADJ_BWP2;
	}

	public BigDecimal getR22_NET_AMT_BWP1() {
		return R22_NET_AMT_BWP1;
	}

	public void setR22_NET_AMT_BWP1(BigDecimal r22_NET_AMT_BWP1) {
		R22_NET_AMT_BWP1 = r22_NET_AMT_BWP1;
	}

	public BigDecimal getR22_NET_AMT_BWP2() {
		return R22_NET_AMT_BWP2;
	}

	public void setR22_NET_AMT_BWP2(BigDecimal r22_NET_AMT_BWP2) {
		R22_NET_AMT_BWP2 = r22_NET_AMT_BWP2;
	}

	public BigDecimal getR22_BAL_SUB_BWP1() {
		return R22_BAL_SUB_BWP1;
	}

	public void setR22_BAL_SUB_BWP1(BigDecimal r22_BAL_SUB_BWP1) {
		R22_BAL_SUB_BWP1 = r22_BAL_SUB_BWP1;
	}

	public BigDecimal getR22_BAL_SUB_BWP2() {
		return R22_BAL_SUB_BWP2;
	}

	public void setR22_BAL_SUB_BWP2(BigDecimal r22_BAL_SUB_BWP2) {
		R22_BAL_SUB_BWP2 = r22_BAL_SUB_BWP2;
	}

	public BigDecimal getR22_BAL_ACT_SUB_BWP1() {
		return R22_BAL_ACT_SUB_BWP1;
	}

	public void setR22_BAL_ACT_SUB_BWP1(BigDecimal r22_BAL_ACT_SUB_BWP1) {
		R22_BAL_ACT_SUB_BWP1 = r22_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR22_BAL_ACT_SUB_BWP2() {
		return R22_BAL_ACT_SUB_BWP2;
	}

	public void setR22_BAL_ACT_SUB_BWP2(BigDecimal r22_BAL_ACT_SUB_BWP2) {
		R22_BAL_ACT_SUB_BWP2 = r22_BAL_ACT_SUB_BWP2;
	}

	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}

	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}

	public BigDecimal getR23_FIG_BAL_BWP1() {
		return R23_FIG_BAL_BWP1;
	}

	public void setR23_FIG_BAL_BWP1(BigDecimal r23_FIG_BAL_BWP1) {
		R23_FIG_BAL_BWP1 = r23_FIG_BAL_BWP1;
	}

	public BigDecimal getR23_FIG_BAL_BWP2() {
		return R23_FIG_BAL_BWP2;
	}

	public void setR23_FIG_BAL_BWP2(BigDecimal r23_FIG_BAL_BWP2) {
		R23_FIG_BAL_BWP2 = r23_FIG_BAL_BWP2;
	}

	public BigDecimal getR23_AMT_ADJ_BWP1() {
		return R23_AMT_ADJ_BWP1;
	}

	public void setR23_AMT_ADJ_BWP1(BigDecimal r23_AMT_ADJ_BWP1) {
		R23_AMT_ADJ_BWP1 = r23_AMT_ADJ_BWP1;
	}

	public BigDecimal getR23_AMT_ADJ_BWP2() {
		return R23_AMT_ADJ_BWP2;
	}

	public void setR23_AMT_ADJ_BWP2(BigDecimal r23_AMT_ADJ_BWP2) {
		R23_AMT_ADJ_BWP2 = r23_AMT_ADJ_BWP2;
	}

	public BigDecimal getR23_NET_AMT_BWP1() {
		return R23_NET_AMT_BWP1;
	}

	public void setR23_NET_AMT_BWP1(BigDecimal r23_NET_AMT_BWP1) {
		R23_NET_AMT_BWP1 = r23_NET_AMT_BWP1;
	}

	public BigDecimal getR23_NET_AMT_BWP2() {
		return R23_NET_AMT_BWP2;
	}

	public void setR23_NET_AMT_BWP2(BigDecimal r23_NET_AMT_BWP2) {
		R23_NET_AMT_BWP2 = r23_NET_AMT_BWP2;
	}

	public BigDecimal getR23_BAL_SUB_BWP1() {
		return R23_BAL_SUB_BWP1;
	}

	public void setR23_BAL_SUB_BWP1(BigDecimal r23_BAL_SUB_BWP1) {
		R23_BAL_SUB_BWP1 = r23_BAL_SUB_BWP1;
	}

	public BigDecimal getR23_BAL_SUB_BWP2() {
		return R23_BAL_SUB_BWP2;
	}

	public void setR23_BAL_SUB_BWP2(BigDecimal r23_BAL_SUB_BWP2) {
		R23_BAL_SUB_BWP2 = r23_BAL_SUB_BWP2;
	}

	public BigDecimal getR23_BAL_ACT_SUB_BWP1() {
		return R23_BAL_ACT_SUB_BWP1;
	}

	public void setR23_BAL_ACT_SUB_BWP1(BigDecimal r23_BAL_ACT_SUB_BWP1) {
		R23_BAL_ACT_SUB_BWP1 = r23_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR23_BAL_ACT_SUB_BWP2() {
		return R23_BAL_ACT_SUB_BWP2;
	}

	public void setR23_BAL_ACT_SUB_BWP2(BigDecimal r23_BAL_ACT_SUB_BWP2) {
		R23_BAL_ACT_SUB_BWP2 = r23_BAL_ACT_SUB_BWP2;
	}

	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}

	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}

	public BigDecimal getR24_FIG_BAL_BWP1() {
		return R24_FIG_BAL_BWP1;
	}

	public void setR24_FIG_BAL_BWP1(BigDecimal r24_FIG_BAL_BWP1) {
		R24_FIG_BAL_BWP1 = r24_FIG_BAL_BWP1;
	}

	public BigDecimal getR24_FIG_BAL_BWP2() {
		return R24_FIG_BAL_BWP2;
	}

	public void setR24_FIG_BAL_BWP2(BigDecimal r24_FIG_BAL_BWP2) {
		R24_FIG_BAL_BWP2 = r24_FIG_BAL_BWP2;
	}

	public BigDecimal getR24_AMT_ADJ_BWP1() {
		return R24_AMT_ADJ_BWP1;
	}

	public void setR24_AMT_ADJ_BWP1(BigDecimal r24_AMT_ADJ_BWP1) {
		R24_AMT_ADJ_BWP1 = r24_AMT_ADJ_BWP1;
	}

	public BigDecimal getR24_AMT_ADJ_BWP2() {
		return R24_AMT_ADJ_BWP2;
	}

	public void setR24_AMT_ADJ_BWP2(BigDecimal r24_AMT_ADJ_BWP2) {
		R24_AMT_ADJ_BWP2 = r24_AMT_ADJ_BWP2;
	}

	public BigDecimal getR24_NET_AMT_BWP1() {
		return R24_NET_AMT_BWP1;
	}

	public void setR24_NET_AMT_BWP1(BigDecimal r24_NET_AMT_BWP1) {
		R24_NET_AMT_BWP1 = r24_NET_AMT_BWP1;
	}

	public BigDecimal getR24_NET_AMT_BWP2() {
		return R24_NET_AMT_BWP2;
	}

	public void setR24_NET_AMT_BWP2(BigDecimal r24_NET_AMT_BWP2) {
		R24_NET_AMT_BWP2 = r24_NET_AMT_BWP2;
	}

	public BigDecimal getR24_BAL_SUB_BWP1() {
		return R24_BAL_SUB_BWP1;
	}

	public void setR24_BAL_SUB_BWP1(BigDecimal r24_BAL_SUB_BWP1) {
		R24_BAL_SUB_BWP1 = r24_BAL_SUB_BWP1;
	}

	public BigDecimal getR24_BAL_SUB_BWP2() {
		return R24_BAL_SUB_BWP2;
	}

	public void setR24_BAL_SUB_BWP2(BigDecimal r24_BAL_SUB_BWP2) {
		R24_BAL_SUB_BWP2 = r24_BAL_SUB_BWP2;
	}

	public BigDecimal getR24_BAL_ACT_SUB_BWP1() {
		return R24_BAL_ACT_SUB_BWP1;
	}

	public void setR24_BAL_ACT_SUB_BWP1(BigDecimal r24_BAL_ACT_SUB_BWP1) {
		R24_BAL_ACT_SUB_BWP1 = r24_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR24_BAL_ACT_SUB_BWP2() {
		return R24_BAL_ACT_SUB_BWP2;
	}

	public void setR24_BAL_ACT_SUB_BWP2(BigDecimal r24_BAL_ACT_SUB_BWP2) {
		R24_BAL_ACT_SUB_BWP2 = r24_BAL_ACT_SUB_BWP2;
	}

	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}

	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}

	public BigDecimal getR25_FIG_BAL_BWP1() {
		return R25_FIG_BAL_BWP1;
	}

	public void setR25_FIG_BAL_BWP1(BigDecimal r25_FIG_BAL_BWP1) {
		R25_FIG_BAL_BWP1 = r25_FIG_BAL_BWP1;
	}

	public BigDecimal getR25_FIG_BAL_BWP2() {
		return R25_FIG_BAL_BWP2;
	}

	public void setR25_FIG_BAL_BWP2(BigDecimal r25_FIG_BAL_BWP2) {
		R25_FIG_BAL_BWP2 = r25_FIG_BAL_BWP2;
	}

	public BigDecimal getR25_AMT_ADJ_BWP1() {
		return R25_AMT_ADJ_BWP1;
	}

	public void setR25_AMT_ADJ_BWP1(BigDecimal r25_AMT_ADJ_BWP1) {
		R25_AMT_ADJ_BWP1 = r25_AMT_ADJ_BWP1;
	}

	public BigDecimal getR25_AMT_ADJ_BWP2() {
		return R25_AMT_ADJ_BWP2;
	}

	public void setR25_AMT_ADJ_BWP2(BigDecimal r25_AMT_ADJ_BWP2) {
		R25_AMT_ADJ_BWP2 = r25_AMT_ADJ_BWP2;
	}

	public BigDecimal getR25_NET_AMT_BWP1() {
		return R25_NET_AMT_BWP1;
	}

	public void setR25_NET_AMT_BWP1(BigDecimal r25_NET_AMT_BWP1) {
		R25_NET_AMT_BWP1 = r25_NET_AMT_BWP1;
	}

	public BigDecimal getR25_NET_AMT_BWP2() {
		return R25_NET_AMT_BWP2;
	}

	public void setR25_NET_AMT_BWP2(BigDecimal r25_NET_AMT_BWP2) {
		R25_NET_AMT_BWP2 = r25_NET_AMT_BWP2;
	}

	public BigDecimal getR25_BAL_SUB_BWP1() {
		return R25_BAL_SUB_BWP1;
	}

	public void setR25_BAL_SUB_BWP1(BigDecimal r25_BAL_SUB_BWP1) {
		R25_BAL_SUB_BWP1 = r25_BAL_SUB_BWP1;
	}

	public BigDecimal getR25_BAL_SUB_BWP2() {
		return R25_BAL_SUB_BWP2;
	}

	public void setR25_BAL_SUB_BWP2(BigDecimal r25_BAL_SUB_BWP2) {
		R25_BAL_SUB_BWP2 = r25_BAL_SUB_BWP2;
	}

	public BigDecimal getR25_BAL_ACT_SUB_BWP1() {
		return R25_BAL_ACT_SUB_BWP1;
	}

	public void setR25_BAL_ACT_SUB_BWP1(BigDecimal r25_BAL_ACT_SUB_BWP1) {
		R25_BAL_ACT_SUB_BWP1 = r25_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR25_BAL_ACT_SUB_BWP2() {
		return R25_BAL_ACT_SUB_BWP2;
	}

	public void setR25_BAL_ACT_SUB_BWP2(BigDecimal r25_BAL_ACT_SUB_BWP2) {
		R25_BAL_ACT_SUB_BWP2 = r25_BAL_ACT_SUB_BWP2;
	}

	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}

	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}

	public BigDecimal getR26_FIG_BAL_BWP1() {
		return R26_FIG_BAL_BWP1;
	}

	public void setR26_FIG_BAL_BWP1(BigDecimal r26_FIG_BAL_BWP1) {
		R26_FIG_BAL_BWP1 = r26_FIG_BAL_BWP1;
	}

	public BigDecimal getR26_FIG_BAL_BWP2() {
		return R26_FIG_BAL_BWP2;
	}

	public void setR26_FIG_BAL_BWP2(BigDecimal r26_FIG_BAL_BWP2) {
		R26_FIG_BAL_BWP2 = r26_FIG_BAL_BWP2;
	}

	public BigDecimal getR26_AMT_ADJ_BWP1() {
		return R26_AMT_ADJ_BWP1;
	}

	public void setR26_AMT_ADJ_BWP1(BigDecimal r26_AMT_ADJ_BWP1) {
		R26_AMT_ADJ_BWP1 = r26_AMT_ADJ_BWP1;
	}

	public BigDecimal getR26_AMT_ADJ_BWP2() {
		return R26_AMT_ADJ_BWP2;
	}

	public void setR26_AMT_ADJ_BWP2(BigDecimal r26_AMT_ADJ_BWP2) {
		R26_AMT_ADJ_BWP2 = r26_AMT_ADJ_BWP2;
	}

	public BigDecimal getR26_NET_AMT_BWP1() {
		return R26_NET_AMT_BWP1;
	}

	public void setR26_NET_AMT_BWP1(BigDecimal r26_NET_AMT_BWP1) {
		R26_NET_AMT_BWP1 = r26_NET_AMT_BWP1;
	}

	public BigDecimal getR26_NET_AMT_BWP2() {
		return R26_NET_AMT_BWP2;
	}

	public void setR26_NET_AMT_BWP2(BigDecimal r26_NET_AMT_BWP2) {
		R26_NET_AMT_BWP2 = r26_NET_AMT_BWP2;
	}

	public BigDecimal getR26_BAL_SUB_BWP1() {
		return R26_BAL_SUB_BWP1;
	}

	public void setR26_BAL_SUB_BWP1(BigDecimal r26_BAL_SUB_BWP1) {
		R26_BAL_SUB_BWP1 = r26_BAL_SUB_BWP1;
	}

	public BigDecimal getR26_BAL_SUB_BWP2() {
		return R26_BAL_SUB_BWP2;
	}

	public void setR26_BAL_SUB_BWP2(BigDecimal r26_BAL_SUB_BWP2) {
		R26_BAL_SUB_BWP2 = r26_BAL_SUB_BWP2;
	}

	public BigDecimal getR26_BAL_ACT_SUB_BWP1() {
		return R26_BAL_ACT_SUB_BWP1;
	}

	public void setR26_BAL_ACT_SUB_BWP1(BigDecimal r26_BAL_ACT_SUB_BWP1) {
		R26_BAL_ACT_SUB_BWP1 = r26_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR26_BAL_ACT_SUB_BWP2() {
		return R26_BAL_ACT_SUB_BWP2;
	}

	public void setR26_BAL_ACT_SUB_BWP2(BigDecimal r26_BAL_ACT_SUB_BWP2) {
		R26_BAL_ACT_SUB_BWP2 = r26_BAL_ACT_SUB_BWP2;
	}

	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}

	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}

	public BigDecimal getR27_FIG_BAL_BWP1() {
		return R27_FIG_BAL_BWP1;
	}

	public void setR27_FIG_BAL_BWP1(BigDecimal r27_FIG_BAL_BWP1) {
		R27_FIG_BAL_BWP1 = r27_FIG_BAL_BWP1;
	}

	public BigDecimal getR27_FIG_BAL_BWP2() {
		return R27_FIG_BAL_BWP2;
	}

	public void setR27_FIG_BAL_BWP2(BigDecimal r27_FIG_BAL_BWP2) {
		R27_FIG_BAL_BWP2 = r27_FIG_BAL_BWP2;
	}

	public BigDecimal getR27_AMT_ADJ_BWP1() {
		return R27_AMT_ADJ_BWP1;
	}

	public void setR27_AMT_ADJ_BWP1(BigDecimal r27_AMT_ADJ_BWP1) {
		R27_AMT_ADJ_BWP1 = r27_AMT_ADJ_BWP1;
	}

	public BigDecimal getR27_AMT_ADJ_BWP2() {
		return R27_AMT_ADJ_BWP2;
	}

	public void setR27_AMT_ADJ_BWP2(BigDecimal r27_AMT_ADJ_BWP2) {
		R27_AMT_ADJ_BWP2 = r27_AMT_ADJ_BWP2;
	}

	public BigDecimal getR27_NET_AMT_BWP1() {
		return R27_NET_AMT_BWP1;
	}

	public void setR27_NET_AMT_BWP1(BigDecimal r27_NET_AMT_BWP1) {
		R27_NET_AMT_BWP1 = r27_NET_AMT_BWP1;
	}

	public BigDecimal getR27_NET_AMT_BWP2() {
		return R27_NET_AMT_BWP2;
	}

	public void setR27_NET_AMT_BWP2(BigDecimal r27_NET_AMT_BWP2) {
		R27_NET_AMT_BWP2 = r27_NET_AMT_BWP2;
	}

	public BigDecimal getR27_BAL_SUB_BWP1() {
		return R27_BAL_SUB_BWP1;
	}

	public void setR27_BAL_SUB_BWP1(BigDecimal r27_BAL_SUB_BWP1) {
		R27_BAL_SUB_BWP1 = r27_BAL_SUB_BWP1;
	}

	public BigDecimal getR27_BAL_SUB_BWP2() {
		return R27_BAL_SUB_BWP2;
	}

	public void setR27_BAL_SUB_BWP2(BigDecimal r27_BAL_SUB_BWP2) {
		R27_BAL_SUB_BWP2 = r27_BAL_SUB_BWP2;
	}

	public BigDecimal getR27_BAL_ACT_SUB_BWP1() {
		return R27_BAL_ACT_SUB_BWP1;
	}

	public void setR27_BAL_ACT_SUB_BWP1(BigDecimal r27_BAL_ACT_SUB_BWP1) {
		R27_BAL_ACT_SUB_BWP1 = r27_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR27_BAL_ACT_SUB_BWP2() {
		return R27_BAL_ACT_SUB_BWP2;
	}

	public void setR27_BAL_ACT_SUB_BWP2(BigDecimal r27_BAL_ACT_SUB_BWP2) {
		R27_BAL_ACT_SUB_BWP2 = r27_BAL_ACT_SUB_BWP2;
	}

	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}

	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}

	public BigDecimal getR34_FIG_BAL_BWP1() {
		return R34_FIG_BAL_BWP1;
	}

	public void setR34_FIG_BAL_BWP1(BigDecimal r34_FIG_BAL_BWP1) {
		R34_FIG_BAL_BWP1 = r34_FIG_BAL_BWP1;
	}

	public BigDecimal getR34_FIG_BAL_BWP2() {
		return R34_FIG_BAL_BWP2;
	}

	public void setR34_FIG_BAL_BWP2(BigDecimal r34_FIG_BAL_BWP2) {
		R34_FIG_BAL_BWP2 = r34_FIG_BAL_BWP2;
	}

	public BigDecimal getR34_AMT_ADJ_BWP1() {
		return R34_AMT_ADJ_BWP1;
	}

	public void setR34_AMT_ADJ_BWP1(BigDecimal r34_AMT_ADJ_BWP1) {
		R34_AMT_ADJ_BWP1 = r34_AMT_ADJ_BWP1;
	}

	public BigDecimal getR34_AMT_ADJ_BWP2() {
		return R34_AMT_ADJ_BWP2;
	}

	public void setR34_AMT_ADJ_BWP2(BigDecimal r34_AMT_ADJ_BWP2) {
		R34_AMT_ADJ_BWP2 = r34_AMT_ADJ_BWP2;
	}

	public BigDecimal getR34_NET_AMT_BWP1() {
		return R34_NET_AMT_BWP1;
	}

	public void setR34_NET_AMT_BWP1(BigDecimal r34_NET_AMT_BWP1) {
		R34_NET_AMT_BWP1 = r34_NET_AMT_BWP1;
	}

	public BigDecimal getR34_NET_AMT_BWP2() {
		return R34_NET_AMT_BWP2;
	}

	public void setR34_NET_AMT_BWP2(BigDecimal r34_NET_AMT_BWP2) {
		R34_NET_AMT_BWP2 = r34_NET_AMT_BWP2;
	}

	public BigDecimal getR34_BAL_SUB_BWP1() {
		return R34_BAL_SUB_BWP1;
	}

	public void setR34_BAL_SUB_BWP1(BigDecimal r34_BAL_SUB_BWP1) {
		R34_BAL_SUB_BWP1 = r34_BAL_SUB_BWP1;
	}

	public BigDecimal getR34_BAL_SUB_BWP2() {
		return R34_BAL_SUB_BWP2;
	}

	public void setR34_BAL_SUB_BWP2(BigDecimal r34_BAL_SUB_BWP2) {
		R34_BAL_SUB_BWP2 = r34_BAL_SUB_BWP2;
	}

	public BigDecimal getR34_BAL_ACT_SUB_BWP1() {
		return R34_BAL_ACT_SUB_BWP1;
	}

	public void setR34_BAL_ACT_SUB_BWP1(BigDecimal r34_BAL_ACT_SUB_BWP1) {
		R34_BAL_ACT_SUB_BWP1 = r34_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR34_BAL_ACT_SUB_BWP2() {
		return R34_BAL_ACT_SUB_BWP2;
	}

	public void setR34_BAL_ACT_SUB_BWP2(BigDecimal r34_BAL_ACT_SUB_BWP2) {
		R34_BAL_ACT_SUB_BWP2 = r34_BAL_ACT_SUB_BWP2;
	}

	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}

	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}

	public BigDecimal getR35_FIG_BAL_BWP1() {
		return R35_FIG_BAL_BWP1;
	}

	public void setR35_FIG_BAL_BWP1(BigDecimal r35_FIG_BAL_BWP1) {
		R35_FIG_BAL_BWP1 = r35_FIG_BAL_BWP1;
	}

	public BigDecimal getR35_FIG_BAL_BWP2() {
		return R35_FIG_BAL_BWP2;
	}

	public void setR35_FIG_BAL_BWP2(BigDecimal r35_FIG_BAL_BWP2) {
		R35_FIG_BAL_BWP2 = r35_FIG_BAL_BWP2;
	}

	public BigDecimal getR35_AMT_ADJ_BWP1() {
		return R35_AMT_ADJ_BWP1;
	}

	public void setR35_AMT_ADJ_BWP1(BigDecimal r35_AMT_ADJ_BWP1) {
		R35_AMT_ADJ_BWP1 = r35_AMT_ADJ_BWP1;
	}

	public BigDecimal getR35_AMT_ADJ_BWP2() {
		return R35_AMT_ADJ_BWP2;
	}

	public void setR35_AMT_ADJ_BWP2(BigDecimal r35_AMT_ADJ_BWP2) {
		R35_AMT_ADJ_BWP2 = r35_AMT_ADJ_BWP2;
	}

	public BigDecimal getR35_NET_AMT_BWP1() {
		return R35_NET_AMT_BWP1;
	}

	public void setR35_NET_AMT_BWP1(BigDecimal r35_NET_AMT_BWP1) {
		R35_NET_AMT_BWP1 = r35_NET_AMT_BWP1;
	}

	public BigDecimal getR35_NET_AMT_BWP2() {
		return R35_NET_AMT_BWP2;
	}

	public void setR35_NET_AMT_BWP2(BigDecimal r35_NET_AMT_BWP2) {
		R35_NET_AMT_BWP2 = r35_NET_AMT_BWP2;
	}

	public BigDecimal getR35_BAL_SUB_BWP1() {
		return R35_BAL_SUB_BWP1;
	}

	public void setR35_BAL_SUB_BWP1(BigDecimal r35_BAL_SUB_BWP1) {
		R35_BAL_SUB_BWP1 = r35_BAL_SUB_BWP1;
	}

	public BigDecimal getR35_BAL_SUB_BWP2() {
		return R35_BAL_SUB_BWP2;
	}

	public void setR35_BAL_SUB_BWP2(BigDecimal r35_BAL_SUB_BWP2) {
		R35_BAL_SUB_BWP2 = r35_BAL_SUB_BWP2;
	}

	public BigDecimal getR35_BAL_ACT_SUB_BWP1() {
		return R35_BAL_ACT_SUB_BWP1;
	}

	public void setR35_BAL_ACT_SUB_BWP1(BigDecimal r35_BAL_ACT_SUB_BWP1) {
		R35_BAL_ACT_SUB_BWP1 = r35_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR35_BAL_ACT_SUB_BWP2() {
		return R35_BAL_ACT_SUB_BWP2;
	}

	public void setR35_BAL_ACT_SUB_BWP2(BigDecimal r35_BAL_ACT_SUB_BWP2) {
		R35_BAL_ACT_SUB_BWP2 = r35_BAL_ACT_SUB_BWP2;
	}

	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}

	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}

	public BigDecimal getR36_FIG_BAL_BWP1() {
		return R36_FIG_BAL_BWP1;
	}

	public void setR36_FIG_BAL_BWP1(BigDecimal r36_FIG_BAL_BWP1) {
		R36_FIG_BAL_BWP1 = r36_FIG_BAL_BWP1;
	}

	public BigDecimal getR36_FIG_BAL_BWP2() {
		return R36_FIG_BAL_BWP2;
	}

	public void setR36_FIG_BAL_BWP2(BigDecimal r36_FIG_BAL_BWP2) {
		R36_FIG_BAL_BWP2 = r36_FIG_BAL_BWP2;
	}

	public BigDecimal getR36_AMT_ADJ_BWP1() {
		return R36_AMT_ADJ_BWP1;
	}

	public void setR36_AMT_ADJ_BWP1(BigDecimal r36_AMT_ADJ_BWP1) {
		R36_AMT_ADJ_BWP1 = r36_AMT_ADJ_BWP1;
	}

	public BigDecimal getR36_AMT_ADJ_BWP2() {
		return R36_AMT_ADJ_BWP2;
	}

	public void setR36_AMT_ADJ_BWP2(BigDecimal r36_AMT_ADJ_BWP2) {
		R36_AMT_ADJ_BWP2 = r36_AMT_ADJ_BWP2;
	}

	public BigDecimal getR36_NET_AMT_BWP1() {
		return R36_NET_AMT_BWP1;
	}

	public void setR36_NET_AMT_BWP1(BigDecimal r36_NET_AMT_BWP1) {
		R36_NET_AMT_BWP1 = r36_NET_AMT_BWP1;
	}

	public BigDecimal getR36_NET_AMT_BWP2() {
		return R36_NET_AMT_BWP2;
	}

	public void setR36_NET_AMT_BWP2(BigDecimal r36_NET_AMT_BWP2) {
		R36_NET_AMT_BWP2 = r36_NET_AMT_BWP2;
	}

	public BigDecimal getR36_BAL_SUB_BWP1() {
		return R36_BAL_SUB_BWP1;
	}

	public void setR36_BAL_SUB_BWP1(BigDecimal r36_BAL_SUB_BWP1) {
		R36_BAL_SUB_BWP1 = r36_BAL_SUB_BWP1;
	}

	public BigDecimal getR36_BAL_SUB_BWP2() {
		return R36_BAL_SUB_BWP2;
	}

	public void setR36_BAL_SUB_BWP2(BigDecimal r36_BAL_SUB_BWP2) {
		R36_BAL_SUB_BWP2 = r36_BAL_SUB_BWP2;
	}

	public BigDecimal getR36_BAL_ACT_SUB_BWP1() {
		return R36_BAL_ACT_SUB_BWP1;
	}

	public void setR36_BAL_ACT_SUB_BWP1(BigDecimal r36_BAL_ACT_SUB_BWP1) {
		R36_BAL_ACT_SUB_BWP1 = r36_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR36_BAL_ACT_SUB_BWP2() {
		return R36_BAL_ACT_SUB_BWP2;
	}

	public void setR36_BAL_ACT_SUB_BWP2(BigDecimal r36_BAL_ACT_SUB_BWP2) {
		R36_BAL_ACT_SUB_BWP2 = r36_BAL_ACT_SUB_BWP2;
	}

	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}

	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}

	public BigDecimal getR37_FIG_BAL_BWP1() {
		return R37_FIG_BAL_BWP1;
	}

	public void setR37_FIG_BAL_BWP1(BigDecimal r37_FIG_BAL_BWP1) {
		R37_FIG_BAL_BWP1 = r37_FIG_BAL_BWP1;
	}

	public BigDecimal getR37_FIG_BAL_BWP2() {
		return R37_FIG_BAL_BWP2;
	}

	public void setR37_FIG_BAL_BWP2(BigDecimal r37_FIG_BAL_BWP2) {
		R37_FIG_BAL_BWP2 = r37_FIG_BAL_BWP2;
	}

	public BigDecimal getR37_AMT_ADJ_BWP1() {
		return R37_AMT_ADJ_BWP1;
	}

	public void setR37_AMT_ADJ_BWP1(BigDecimal r37_AMT_ADJ_BWP1) {
		R37_AMT_ADJ_BWP1 = r37_AMT_ADJ_BWP1;
	}

	public BigDecimal getR37_AMT_ADJ_BWP2() {
		return R37_AMT_ADJ_BWP2;
	}

	public void setR37_AMT_ADJ_BWP2(BigDecimal r37_AMT_ADJ_BWP2) {
		R37_AMT_ADJ_BWP2 = r37_AMT_ADJ_BWP2;
	}

	public BigDecimal getR37_NET_AMT_BWP1() {
		return R37_NET_AMT_BWP1;
	}

	public void setR37_NET_AMT_BWP1(BigDecimal r37_NET_AMT_BWP1) {
		R37_NET_AMT_BWP1 = r37_NET_AMT_BWP1;
	}

	public BigDecimal getR37_NET_AMT_BWP2() {
		return R37_NET_AMT_BWP2;
	}

	public void setR37_NET_AMT_BWP2(BigDecimal r37_NET_AMT_BWP2) {
		R37_NET_AMT_BWP2 = r37_NET_AMT_BWP2;
	}

	public BigDecimal getR37_BAL_SUB_BWP1() {
		return R37_BAL_SUB_BWP1;
	}

	public void setR37_BAL_SUB_BWP1(BigDecimal r37_BAL_SUB_BWP1) {
		R37_BAL_SUB_BWP1 = r37_BAL_SUB_BWP1;
	}

	public BigDecimal getR37_BAL_SUB_BWP2() {
		return R37_BAL_SUB_BWP2;
	}

	public void setR37_BAL_SUB_BWP2(BigDecimal r37_BAL_SUB_BWP2) {
		R37_BAL_SUB_BWP2 = r37_BAL_SUB_BWP2;
	}

	public BigDecimal getR37_BAL_ACT_SUB_BWP1() {
		return R37_BAL_ACT_SUB_BWP1;
	}

	public void setR37_BAL_ACT_SUB_BWP1(BigDecimal r37_BAL_ACT_SUB_BWP1) {
		R37_BAL_ACT_SUB_BWP1 = r37_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR37_BAL_ACT_SUB_BWP2() {
		return R37_BAL_ACT_SUB_BWP2;
	}

	public void setR37_BAL_ACT_SUB_BWP2(BigDecimal r37_BAL_ACT_SUB_BWP2) {
		R37_BAL_ACT_SUB_BWP2 = r37_BAL_ACT_SUB_BWP2;
	}

	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}

	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}

	public BigDecimal getR38_FIG_BAL_BWP1() {
		return R38_FIG_BAL_BWP1;
	}

	public void setR38_FIG_BAL_BWP1(BigDecimal r38_FIG_BAL_BWP1) {
		R38_FIG_BAL_BWP1 = r38_FIG_BAL_BWP1;
	}

	public BigDecimal getR38_FIG_BAL_BWP2() {
		return R38_FIG_BAL_BWP2;
	}

	public void setR38_FIG_BAL_BWP2(BigDecimal r38_FIG_BAL_BWP2) {
		R38_FIG_BAL_BWP2 = r38_FIG_BAL_BWP2;
	}

	public BigDecimal getR38_AMT_ADJ_BWP1() {
		return R38_AMT_ADJ_BWP1;
	}

	public void setR38_AMT_ADJ_BWP1(BigDecimal r38_AMT_ADJ_BWP1) {
		R38_AMT_ADJ_BWP1 = r38_AMT_ADJ_BWP1;
	}

	public BigDecimal getR38_AMT_ADJ_BWP2() {
		return R38_AMT_ADJ_BWP2;
	}

	public void setR38_AMT_ADJ_BWP2(BigDecimal r38_AMT_ADJ_BWP2) {
		R38_AMT_ADJ_BWP2 = r38_AMT_ADJ_BWP2;
	}

	public BigDecimal getR38_NET_AMT_BWP1() {
		return R38_NET_AMT_BWP1;
	}

	public void setR38_NET_AMT_BWP1(BigDecimal r38_NET_AMT_BWP1) {
		R38_NET_AMT_BWP1 = r38_NET_AMT_BWP1;
	}

	public BigDecimal getR38_NET_AMT_BWP2() {
		return R38_NET_AMT_BWP2;
	}

	public void setR38_NET_AMT_BWP2(BigDecimal r38_NET_AMT_BWP2) {
		R38_NET_AMT_BWP2 = r38_NET_AMT_BWP2;
	}

	public BigDecimal getR38_BAL_SUB_BWP1() {
		return R38_BAL_SUB_BWP1;
	}

	public void setR38_BAL_SUB_BWP1(BigDecimal r38_BAL_SUB_BWP1) {
		R38_BAL_SUB_BWP1 = r38_BAL_SUB_BWP1;
	}

	public BigDecimal getR38_BAL_SUB_BWP2() {
		return R38_BAL_SUB_BWP2;
	}

	public void setR38_BAL_SUB_BWP2(BigDecimal r38_BAL_SUB_BWP2) {
		R38_BAL_SUB_BWP2 = r38_BAL_SUB_BWP2;
	}

	public BigDecimal getR38_BAL_ACT_SUB_BWP1() {
		return R38_BAL_ACT_SUB_BWP1;
	}

	public void setR38_BAL_ACT_SUB_BWP1(BigDecimal r38_BAL_ACT_SUB_BWP1) {
		R38_BAL_ACT_SUB_BWP1 = r38_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR38_BAL_ACT_SUB_BWP2() {
		return R38_BAL_ACT_SUB_BWP2;
	}

	public void setR38_BAL_ACT_SUB_BWP2(BigDecimal r38_BAL_ACT_SUB_BWP2) {
		R38_BAL_ACT_SUB_BWP2 = r38_BAL_ACT_SUB_BWP2;
	}

	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}

	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}

	public BigDecimal getR39_FIG_BAL_BWP1() {
		return R39_FIG_BAL_BWP1;
	}

	public void setR39_FIG_BAL_BWP1(BigDecimal r39_FIG_BAL_BWP1) {
		R39_FIG_BAL_BWP1 = r39_FIG_BAL_BWP1;
	}

	public BigDecimal getR39_FIG_BAL_BWP2() {
		return R39_FIG_BAL_BWP2;
	}

	public void setR39_FIG_BAL_BWP2(BigDecimal r39_FIG_BAL_BWP2) {
		R39_FIG_BAL_BWP2 = r39_FIG_BAL_BWP2;
	}

	public BigDecimal getR39_AMT_ADJ_BWP1() {
		return R39_AMT_ADJ_BWP1;
	}

	public void setR39_AMT_ADJ_BWP1(BigDecimal r39_AMT_ADJ_BWP1) {
		R39_AMT_ADJ_BWP1 = r39_AMT_ADJ_BWP1;
	}

	public BigDecimal getR39_AMT_ADJ_BWP2() {
		return R39_AMT_ADJ_BWP2;
	}

	public void setR39_AMT_ADJ_BWP2(BigDecimal r39_AMT_ADJ_BWP2) {
		R39_AMT_ADJ_BWP2 = r39_AMT_ADJ_BWP2;
	}

	public BigDecimal getR39_NET_AMT_BWP1() {
		return R39_NET_AMT_BWP1;
	}

	public void setR39_NET_AMT_BWP1(BigDecimal r39_NET_AMT_BWP1) {
		R39_NET_AMT_BWP1 = r39_NET_AMT_BWP1;
	}

	public BigDecimal getR39_NET_AMT_BWP2() {
		return R39_NET_AMT_BWP2;
	}

	public void setR39_NET_AMT_BWP2(BigDecimal r39_NET_AMT_BWP2) {
		R39_NET_AMT_BWP2 = r39_NET_AMT_BWP2;
	}

	public BigDecimal getR39_BAL_SUB_BWP1() {
		return R39_BAL_SUB_BWP1;
	}

	public void setR39_BAL_SUB_BWP1(BigDecimal r39_BAL_SUB_BWP1) {
		R39_BAL_SUB_BWP1 = r39_BAL_SUB_BWP1;
	}

	public BigDecimal getR39_BAL_SUB_BWP2() {
		return R39_BAL_SUB_BWP2;
	}

	public void setR39_BAL_SUB_BWP2(BigDecimal r39_BAL_SUB_BWP2) {
		R39_BAL_SUB_BWP2 = r39_BAL_SUB_BWP2;
	}

	public BigDecimal getR39_BAL_ACT_SUB_BWP1() {
		return R39_BAL_ACT_SUB_BWP1;
	}

	public void setR39_BAL_ACT_SUB_BWP1(BigDecimal r39_BAL_ACT_SUB_BWP1) {
		R39_BAL_ACT_SUB_BWP1 = r39_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR39_BAL_ACT_SUB_BWP2() {
		return R39_BAL_ACT_SUB_BWP2;
	}

	public void setR39_BAL_ACT_SUB_BWP2(BigDecimal r39_BAL_ACT_SUB_BWP2) {
		R39_BAL_ACT_SUB_BWP2 = r39_BAL_ACT_SUB_BWP2;
	}

	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}

	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}

	public BigDecimal getR40_FIG_BAL_BWP1() {
		return R40_FIG_BAL_BWP1;
	}

	public void setR40_FIG_BAL_BWP1(BigDecimal r40_FIG_BAL_BWP1) {
		R40_FIG_BAL_BWP1 = r40_FIG_BAL_BWP1;
	}

	public BigDecimal getR40_FIG_BAL_BWP2() {
		return R40_FIG_BAL_BWP2;
	}

	public void setR40_FIG_BAL_BWP2(BigDecimal r40_FIG_BAL_BWP2) {
		R40_FIG_BAL_BWP2 = r40_FIG_BAL_BWP2;
	}

	public BigDecimal getR40_AMT_ADJ_BWP1() {
		return R40_AMT_ADJ_BWP1;
	}

	public void setR40_AMT_ADJ_BWP1(BigDecimal r40_AMT_ADJ_BWP1) {
		R40_AMT_ADJ_BWP1 = r40_AMT_ADJ_BWP1;
	}

	public BigDecimal getR40_AMT_ADJ_BWP2() {
		return R40_AMT_ADJ_BWP2;
	}

	public void setR40_AMT_ADJ_BWP2(BigDecimal r40_AMT_ADJ_BWP2) {
		R40_AMT_ADJ_BWP2 = r40_AMT_ADJ_BWP2;
	}

	public BigDecimal getR40_NET_AMT_BWP1() {
		return R40_NET_AMT_BWP1;
	}

	public void setR40_NET_AMT_BWP1(BigDecimal r40_NET_AMT_BWP1) {
		R40_NET_AMT_BWP1 = r40_NET_AMT_BWP1;
	}

	public BigDecimal getR40_NET_AMT_BWP2() {
		return R40_NET_AMT_BWP2;
	}

	public void setR40_NET_AMT_BWP2(BigDecimal r40_NET_AMT_BWP2) {
		R40_NET_AMT_BWP2 = r40_NET_AMT_BWP2;
	}

	public BigDecimal getR40_BAL_SUB_BWP1() {
		return R40_BAL_SUB_BWP1;
	}

	public void setR40_BAL_SUB_BWP1(BigDecimal r40_BAL_SUB_BWP1) {
		R40_BAL_SUB_BWP1 = r40_BAL_SUB_BWP1;
	}

	public BigDecimal getR40_BAL_SUB_BWP2() {
		return R40_BAL_SUB_BWP2;
	}

	public void setR40_BAL_SUB_BWP2(BigDecimal r40_BAL_SUB_BWP2) {
		R40_BAL_SUB_BWP2 = r40_BAL_SUB_BWP2;
	}

	public BigDecimal getR40_BAL_ACT_SUB_BWP1() {
		return R40_BAL_ACT_SUB_BWP1;
	}

	public void setR40_BAL_ACT_SUB_BWP1(BigDecimal r40_BAL_ACT_SUB_BWP1) {
		R40_BAL_ACT_SUB_BWP1 = r40_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR40_BAL_ACT_SUB_BWP2() {
		return R40_BAL_ACT_SUB_BWP2;
	}

	public void setR40_BAL_ACT_SUB_BWP2(BigDecimal r40_BAL_ACT_SUB_BWP2) {
		R40_BAL_ACT_SUB_BWP2 = r40_BAL_ACT_SUB_BWP2;
	}

	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}

	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}

	public BigDecimal getR41_FIG_BAL_BWP1() {
		return R41_FIG_BAL_BWP1;
	}

	public void setR41_FIG_BAL_BWP1(BigDecimal r41_FIG_BAL_BWP1) {
		R41_FIG_BAL_BWP1 = r41_FIG_BAL_BWP1;
	}

	public BigDecimal getR41_FIG_BAL_BWP2() {
		return R41_FIG_BAL_BWP2;
	}

	public void setR41_FIG_BAL_BWP2(BigDecimal r41_FIG_BAL_BWP2) {
		R41_FIG_BAL_BWP2 = r41_FIG_BAL_BWP2;
	}

	public BigDecimal getR41_AMT_ADJ_BWP1() {
		return R41_AMT_ADJ_BWP1;
	}

	public void setR41_AMT_ADJ_BWP1(BigDecimal r41_AMT_ADJ_BWP1) {
		R41_AMT_ADJ_BWP1 = r41_AMT_ADJ_BWP1;
	}

	public BigDecimal getR41_AMT_ADJ_BWP2() {
		return R41_AMT_ADJ_BWP2;
	}

	public void setR41_AMT_ADJ_BWP2(BigDecimal r41_AMT_ADJ_BWP2) {
		R41_AMT_ADJ_BWP2 = r41_AMT_ADJ_BWP2;
	}

	public BigDecimal getR41_NET_AMT_BWP1() {
		return R41_NET_AMT_BWP1;
	}

	public void setR41_NET_AMT_BWP1(BigDecimal r41_NET_AMT_BWP1) {
		R41_NET_AMT_BWP1 = r41_NET_AMT_BWP1;
	}

	public BigDecimal getR41_NET_AMT_BWP2() {
		return R41_NET_AMT_BWP2;
	}

	public void setR41_NET_AMT_BWP2(BigDecimal r41_NET_AMT_BWP2) {
		R41_NET_AMT_BWP2 = r41_NET_AMT_BWP2;
	}

	public BigDecimal getR41_BAL_SUB_BWP1() {
		return R41_BAL_SUB_BWP1;
	}

	public void setR41_BAL_SUB_BWP1(BigDecimal r41_BAL_SUB_BWP1) {
		R41_BAL_SUB_BWP1 = r41_BAL_SUB_BWP1;
	}

	public BigDecimal getR41_BAL_SUB_BWP2() {
		return R41_BAL_SUB_BWP2;
	}

	public void setR41_BAL_SUB_BWP2(BigDecimal r41_BAL_SUB_BWP2) {
		R41_BAL_SUB_BWP2 = r41_BAL_SUB_BWP2;
	}

	public BigDecimal getR41_BAL_ACT_SUB_BWP1() {
		return R41_BAL_ACT_SUB_BWP1;
	}

	public void setR41_BAL_ACT_SUB_BWP1(BigDecimal r41_BAL_ACT_SUB_BWP1) {
		R41_BAL_ACT_SUB_BWP1 = r41_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR41_BAL_ACT_SUB_BWP2() {
		return R41_BAL_ACT_SUB_BWP2;
	}

	public void setR41_BAL_ACT_SUB_BWP2(BigDecimal r41_BAL_ACT_SUB_BWP2) {
		R41_BAL_ACT_SUB_BWP2 = r41_BAL_ACT_SUB_BWP2;
	}

	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}

	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}

	public BigDecimal getR42_FIG_BAL_BWP1() {
		return R42_FIG_BAL_BWP1;
	}

	public void setR42_FIG_BAL_BWP1(BigDecimal r42_FIG_BAL_BWP1) {
		R42_FIG_BAL_BWP1 = r42_FIG_BAL_BWP1;
	}

	public BigDecimal getR42_FIG_BAL_BWP2() {
		return R42_FIG_BAL_BWP2;
	}

	public void setR42_FIG_BAL_BWP2(BigDecimal r42_FIG_BAL_BWP2) {
		R42_FIG_BAL_BWP2 = r42_FIG_BAL_BWP2;
	}

	public BigDecimal getR42_AMT_ADJ_BWP1() {
		return R42_AMT_ADJ_BWP1;
	}

	public void setR42_AMT_ADJ_BWP1(BigDecimal r42_AMT_ADJ_BWP1) {
		R42_AMT_ADJ_BWP1 = r42_AMT_ADJ_BWP1;
	}

	public BigDecimal getR42_AMT_ADJ_BWP2() {
		return R42_AMT_ADJ_BWP2;
	}

	public void setR42_AMT_ADJ_BWP2(BigDecimal r42_AMT_ADJ_BWP2) {
		R42_AMT_ADJ_BWP2 = r42_AMT_ADJ_BWP2;
	}

	public BigDecimal getR42_NET_AMT_BWP1() {
		return R42_NET_AMT_BWP1;
	}

	public void setR42_NET_AMT_BWP1(BigDecimal r42_NET_AMT_BWP1) {
		R42_NET_AMT_BWP1 = r42_NET_AMT_BWP1;
	}

	public BigDecimal getR42_NET_AMT_BWP2() {
		return R42_NET_AMT_BWP2;
	}

	public void setR42_NET_AMT_BWP2(BigDecimal r42_NET_AMT_BWP2) {
		R42_NET_AMT_BWP2 = r42_NET_AMT_BWP2;
	}

	public BigDecimal getR42_BAL_SUB_BWP1() {
		return R42_BAL_SUB_BWP1;
	}

	public void setR42_BAL_SUB_BWP1(BigDecimal r42_BAL_SUB_BWP1) {
		R42_BAL_SUB_BWP1 = r42_BAL_SUB_BWP1;
	}

	public BigDecimal getR42_BAL_SUB_BWP2() {
		return R42_BAL_SUB_BWP2;
	}

	public void setR42_BAL_SUB_BWP2(BigDecimal r42_BAL_SUB_BWP2) {
		R42_BAL_SUB_BWP2 = r42_BAL_SUB_BWP2;
	}

	public BigDecimal getR42_BAL_ACT_SUB_BWP1() {
		return R42_BAL_ACT_SUB_BWP1;
	}

	public void setR42_BAL_ACT_SUB_BWP1(BigDecimal r42_BAL_ACT_SUB_BWP1) {
		R42_BAL_ACT_SUB_BWP1 = r42_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR42_BAL_ACT_SUB_BWP2() {
		return R42_BAL_ACT_SUB_BWP2;
	}

	public void setR42_BAL_ACT_SUB_BWP2(BigDecimal r42_BAL_ACT_SUB_BWP2) {
		R42_BAL_ACT_SUB_BWP2 = r42_BAL_ACT_SUB_BWP2;
	}

	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}

	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}

	public BigDecimal getR43_FIG_BAL_BWP1() {
		return R43_FIG_BAL_BWP1;
	}

	public void setR43_FIG_BAL_BWP1(BigDecimal r43_FIG_BAL_BWP1) {
		R43_FIG_BAL_BWP1 = r43_FIG_BAL_BWP1;
	}

	public BigDecimal getR43_FIG_BAL_BWP2() {
		return R43_FIG_BAL_BWP2;
	}

	public void setR43_FIG_BAL_BWP2(BigDecimal r43_FIG_BAL_BWP2) {
		R43_FIG_BAL_BWP2 = r43_FIG_BAL_BWP2;
	}

	public BigDecimal getR43_AMT_ADJ_BWP1() {
		return R43_AMT_ADJ_BWP1;
	}

	public void setR43_AMT_ADJ_BWP1(BigDecimal r43_AMT_ADJ_BWP1) {
		R43_AMT_ADJ_BWP1 = r43_AMT_ADJ_BWP1;
	}

	public BigDecimal getR43_AMT_ADJ_BWP2() {
		return R43_AMT_ADJ_BWP2;
	}

	public void setR43_AMT_ADJ_BWP2(BigDecimal r43_AMT_ADJ_BWP2) {
		R43_AMT_ADJ_BWP2 = r43_AMT_ADJ_BWP2;
	}

	public BigDecimal getR43_NET_AMT_BWP1() {
		return R43_NET_AMT_BWP1;
	}

	public void setR43_NET_AMT_BWP1(BigDecimal r43_NET_AMT_BWP1) {
		R43_NET_AMT_BWP1 = r43_NET_AMT_BWP1;
	}

	public BigDecimal getR43_NET_AMT_BWP2() {
		return R43_NET_AMT_BWP2;
	}

	public void setR43_NET_AMT_BWP2(BigDecimal r43_NET_AMT_BWP2) {
		R43_NET_AMT_BWP2 = r43_NET_AMT_BWP2;
	}

	public BigDecimal getR43_BAL_SUB_BWP1() {
		return R43_BAL_SUB_BWP1;
	}

	public void setR43_BAL_SUB_BWP1(BigDecimal r43_BAL_SUB_BWP1) {
		R43_BAL_SUB_BWP1 = r43_BAL_SUB_BWP1;
	}

	public BigDecimal getR43_BAL_SUB_BWP2() {
		return R43_BAL_SUB_BWP2;
	}

	public void setR43_BAL_SUB_BWP2(BigDecimal r43_BAL_SUB_BWP2) {
		R43_BAL_SUB_BWP2 = r43_BAL_SUB_BWP2;
	}

	public BigDecimal getR43_BAL_ACT_SUB_BWP1() {
		return R43_BAL_ACT_SUB_BWP1;
	}

	public void setR43_BAL_ACT_SUB_BWP1(BigDecimal r43_BAL_ACT_SUB_BWP1) {
		R43_BAL_ACT_SUB_BWP1 = r43_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR43_BAL_ACT_SUB_BWP2() {
		return R43_BAL_ACT_SUB_BWP2;
	}

	public void setR43_BAL_ACT_SUB_BWP2(BigDecimal r43_BAL_ACT_SUB_BWP2) {
		R43_BAL_ACT_SUB_BWP2 = r43_BAL_ACT_SUB_BWP2;
	}

	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}

	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}

	public BigDecimal getR44_FIG_BAL_BWP1() {
		return R44_FIG_BAL_BWP1;
	}

	public void setR44_FIG_BAL_BWP1(BigDecimal r44_FIG_BAL_BWP1) {
		R44_FIG_BAL_BWP1 = r44_FIG_BAL_BWP1;
	}

	public BigDecimal getR44_FIG_BAL_BWP2() {
		return R44_FIG_BAL_BWP2;
	}

	public void setR44_FIG_BAL_BWP2(BigDecimal r44_FIG_BAL_BWP2) {
		R44_FIG_BAL_BWP2 = r44_FIG_BAL_BWP2;
	}

	public BigDecimal getR44_AMT_ADJ_BWP1() {
		return R44_AMT_ADJ_BWP1;
	}

	public void setR44_AMT_ADJ_BWP1(BigDecimal r44_AMT_ADJ_BWP1) {
		R44_AMT_ADJ_BWP1 = r44_AMT_ADJ_BWP1;
	}

	public BigDecimal getR44_AMT_ADJ_BWP2() {
		return R44_AMT_ADJ_BWP2;
	}

	public void setR44_AMT_ADJ_BWP2(BigDecimal r44_AMT_ADJ_BWP2) {
		R44_AMT_ADJ_BWP2 = r44_AMT_ADJ_BWP2;
	}

	public BigDecimal getR44_NET_AMT_BWP1() {
		return R44_NET_AMT_BWP1;
	}

	public void setR44_NET_AMT_BWP1(BigDecimal r44_NET_AMT_BWP1) {
		R44_NET_AMT_BWP1 = r44_NET_AMT_BWP1;
	}

	public BigDecimal getR44_NET_AMT_BWP2() {
		return R44_NET_AMT_BWP2;
	}

	public void setR44_NET_AMT_BWP2(BigDecimal r44_NET_AMT_BWP2) {
		R44_NET_AMT_BWP2 = r44_NET_AMT_BWP2;
	}

	public BigDecimal getR44_BAL_SUB_BWP1() {
		return R44_BAL_SUB_BWP1;
	}

	public void setR44_BAL_SUB_BWP1(BigDecimal r44_BAL_SUB_BWP1) {
		R44_BAL_SUB_BWP1 = r44_BAL_SUB_BWP1;
	}

	public BigDecimal getR44_BAL_SUB_BWP2() {
		return R44_BAL_SUB_BWP2;
	}

	public void setR44_BAL_SUB_BWP2(BigDecimal r44_BAL_SUB_BWP2) {
		R44_BAL_SUB_BWP2 = r44_BAL_SUB_BWP2;
	}

	public BigDecimal getR44_BAL_ACT_SUB_BWP1() {
		return R44_BAL_ACT_SUB_BWP1;
	}

	public void setR44_BAL_ACT_SUB_BWP1(BigDecimal r44_BAL_ACT_SUB_BWP1) {
		R44_BAL_ACT_SUB_BWP1 = r44_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR44_BAL_ACT_SUB_BWP2() {
		return R44_BAL_ACT_SUB_BWP2;
	}

	public void setR44_BAL_ACT_SUB_BWP2(BigDecimal r44_BAL_ACT_SUB_BWP2) {
		R44_BAL_ACT_SUB_BWP2 = r44_BAL_ACT_SUB_BWP2;
	}

	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}

	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}

	public BigDecimal getR45_FIG_BAL_BWP1() {
		return R45_FIG_BAL_BWP1;
	}

	public void setR45_FIG_BAL_BWP1(BigDecimal r45_FIG_BAL_BWP1) {
		R45_FIG_BAL_BWP1 = r45_FIG_BAL_BWP1;
	}

	public BigDecimal getR45_FIG_BAL_BWP2() {
		return R45_FIG_BAL_BWP2;
	}

	public void setR45_FIG_BAL_BWP2(BigDecimal r45_FIG_BAL_BWP2) {
		R45_FIG_BAL_BWP2 = r45_FIG_BAL_BWP2;
	}

	public BigDecimal getR45_AMT_ADJ_BWP1() {
		return R45_AMT_ADJ_BWP1;
	}

	public void setR45_AMT_ADJ_BWP1(BigDecimal r45_AMT_ADJ_BWP1) {
		R45_AMT_ADJ_BWP1 = r45_AMT_ADJ_BWP1;
	}

	public BigDecimal getR45_AMT_ADJ_BWP2() {
		return R45_AMT_ADJ_BWP2;
	}

	public void setR45_AMT_ADJ_BWP2(BigDecimal r45_AMT_ADJ_BWP2) {
		R45_AMT_ADJ_BWP2 = r45_AMT_ADJ_BWP2;
	}

	public BigDecimal getR45_NET_AMT_BWP1() {
		return R45_NET_AMT_BWP1;
	}

	public void setR45_NET_AMT_BWP1(BigDecimal r45_NET_AMT_BWP1) {
		R45_NET_AMT_BWP1 = r45_NET_AMT_BWP1;
	}

	public BigDecimal getR45_NET_AMT_BWP2() {
		return R45_NET_AMT_BWP2;
	}

	public void setR45_NET_AMT_BWP2(BigDecimal r45_NET_AMT_BWP2) {
		R45_NET_AMT_BWP2 = r45_NET_AMT_BWP2;
	}

	public BigDecimal getR45_BAL_SUB_BWP1() {
		return R45_BAL_SUB_BWP1;
	}

	public void setR45_BAL_SUB_BWP1(BigDecimal r45_BAL_SUB_BWP1) {
		R45_BAL_SUB_BWP1 = r45_BAL_SUB_BWP1;
	}

	public BigDecimal getR45_BAL_SUB_BWP2() {
		return R45_BAL_SUB_BWP2;
	}

	public void setR45_BAL_SUB_BWP2(BigDecimal r45_BAL_SUB_BWP2) {
		R45_BAL_SUB_BWP2 = r45_BAL_SUB_BWP2;
	}

	public BigDecimal getR45_BAL_ACT_SUB_BWP1() {
		return R45_BAL_ACT_SUB_BWP1;
	}

	public void setR45_BAL_ACT_SUB_BWP1(BigDecimal r45_BAL_ACT_SUB_BWP1) {
		R45_BAL_ACT_SUB_BWP1 = r45_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR45_BAL_ACT_SUB_BWP2() {
		return R45_BAL_ACT_SUB_BWP2;
	}

	public void setR45_BAL_ACT_SUB_BWP2(BigDecimal r45_BAL_ACT_SUB_BWP2) {
		R45_BAL_ACT_SUB_BWP2 = r45_BAL_ACT_SUB_BWP2;
	}

	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}

	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}

	public BigDecimal getR46_FIG_BAL_BWP1() {
		return R46_FIG_BAL_BWP1;
	}

	public void setR46_FIG_BAL_BWP1(BigDecimal r46_FIG_BAL_BWP1) {
		R46_FIG_BAL_BWP1 = r46_FIG_BAL_BWP1;
	}

	public BigDecimal getR46_FIG_BAL_BWP2() {
		return R46_FIG_BAL_BWP2;
	}

	public void setR46_FIG_BAL_BWP2(BigDecimal r46_FIG_BAL_BWP2) {
		R46_FIG_BAL_BWP2 = r46_FIG_BAL_BWP2;
	}

	public BigDecimal getR46_AMT_ADJ_BWP1() {
		return R46_AMT_ADJ_BWP1;
	}

	public void setR46_AMT_ADJ_BWP1(BigDecimal r46_AMT_ADJ_BWP1) {
		R46_AMT_ADJ_BWP1 = r46_AMT_ADJ_BWP1;
	}

	public BigDecimal getR46_AMT_ADJ_BWP2() {
		return R46_AMT_ADJ_BWP2;
	}

	public void setR46_AMT_ADJ_BWP2(BigDecimal r46_AMT_ADJ_BWP2) {
		R46_AMT_ADJ_BWP2 = r46_AMT_ADJ_BWP2;
	}

	public BigDecimal getR46_NET_AMT_BWP1() {
		return R46_NET_AMT_BWP1;
	}

	public void setR46_NET_AMT_BWP1(BigDecimal r46_NET_AMT_BWP1) {
		R46_NET_AMT_BWP1 = r46_NET_AMT_BWP1;
	}

	public BigDecimal getR46_NET_AMT_BWP2() {
		return R46_NET_AMT_BWP2;
	}

	public void setR46_NET_AMT_BWP2(BigDecimal r46_NET_AMT_BWP2) {
		R46_NET_AMT_BWP2 = r46_NET_AMT_BWP2;
	}

	public BigDecimal getR46_BAL_SUB_BWP1() {
		return R46_BAL_SUB_BWP1;
	}

	public void setR46_BAL_SUB_BWP1(BigDecimal r46_BAL_SUB_BWP1) {
		R46_BAL_SUB_BWP1 = r46_BAL_SUB_BWP1;
	}

	public BigDecimal getR46_BAL_SUB_BWP2() {
		return R46_BAL_SUB_BWP2;
	}

	public void setR46_BAL_SUB_BWP2(BigDecimal r46_BAL_SUB_BWP2) {
		R46_BAL_SUB_BWP2 = r46_BAL_SUB_BWP2;
	}

	public BigDecimal getR46_BAL_ACT_SUB_BWP1() {
		return R46_BAL_ACT_SUB_BWP1;
	}

	public void setR46_BAL_ACT_SUB_BWP1(BigDecimal r46_BAL_ACT_SUB_BWP1) {
		R46_BAL_ACT_SUB_BWP1 = r46_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR46_BAL_ACT_SUB_BWP2() {
		return R46_BAL_ACT_SUB_BWP2;
	}

	public void setR46_BAL_ACT_SUB_BWP2(BigDecimal r46_BAL_ACT_SUB_BWP2) {
		R46_BAL_ACT_SUB_BWP2 = r46_BAL_ACT_SUB_BWP2;
	}

	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}

	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}

	public BigDecimal getR47_FIG_BAL_BWP1() {
		return R47_FIG_BAL_BWP1;
	}

	public void setR47_FIG_BAL_BWP1(BigDecimal r47_FIG_BAL_BWP1) {
		R47_FIG_BAL_BWP1 = r47_FIG_BAL_BWP1;
	}

	public BigDecimal getR47_FIG_BAL_BWP2() {
		return R47_FIG_BAL_BWP2;
	}

	public void setR47_FIG_BAL_BWP2(BigDecimal r47_FIG_BAL_BWP2) {
		R47_FIG_BAL_BWP2 = r47_FIG_BAL_BWP2;
	}

	public BigDecimal getR47_AMT_ADJ_BWP1() {
		return R47_AMT_ADJ_BWP1;
	}

	public void setR47_AMT_ADJ_BWP1(BigDecimal r47_AMT_ADJ_BWP1) {
		R47_AMT_ADJ_BWP1 = r47_AMT_ADJ_BWP1;
	}

	public BigDecimal getR47_AMT_ADJ_BWP2() {
		return R47_AMT_ADJ_BWP2;
	}

	public void setR47_AMT_ADJ_BWP2(BigDecimal r47_AMT_ADJ_BWP2) {
		R47_AMT_ADJ_BWP2 = r47_AMT_ADJ_BWP2;
	}

	public BigDecimal getR47_NET_AMT_BWP1() {
		return R47_NET_AMT_BWP1;
	}

	public void setR47_NET_AMT_BWP1(BigDecimal r47_NET_AMT_BWP1) {
		R47_NET_AMT_BWP1 = r47_NET_AMT_BWP1;
	}

	public BigDecimal getR47_NET_AMT_BWP2() {
		return R47_NET_AMT_BWP2;
	}

	public void setR47_NET_AMT_BWP2(BigDecimal r47_NET_AMT_BWP2) {
		R47_NET_AMT_BWP2 = r47_NET_AMT_BWP2;
	}

	public BigDecimal getR47_BAL_SUB_BWP1() {
		return R47_BAL_SUB_BWP1;
	}

	public void setR47_BAL_SUB_BWP1(BigDecimal r47_BAL_SUB_BWP1) {
		R47_BAL_SUB_BWP1 = r47_BAL_SUB_BWP1;
	}

	public BigDecimal getR47_BAL_SUB_BWP2() {
		return R47_BAL_SUB_BWP2;
	}

	public void setR47_BAL_SUB_BWP2(BigDecimal r47_BAL_SUB_BWP2) {
		R47_BAL_SUB_BWP2 = r47_BAL_SUB_BWP2;
	}

	public BigDecimal getR47_BAL_ACT_SUB_BWP1() {
		return R47_BAL_ACT_SUB_BWP1;
	}

	public void setR47_BAL_ACT_SUB_BWP1(BigDecimal r47_BAL_ACT_SUB_BWP1) {
		R47_BAL_ACT_SUB_BWP1 = r47_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR47_BAL_ACT_SUB_BWP2() {
		return R47_BAL_ACT_SUB_BWP2;
	}

	public void setR47_BAL_ACT_SUB_BWP2(BigDecimal r47_BAL_ACT_SUB_BWP2) {
		R47_BAL_ACT_SUB_BWP2 = r47_BAL_ACT_SUB_BWP2;
	}

	public String getR48_PRODUCT() {
		return R48_PRODUCT;
	}

	public void setR48_PRODUCT(String r48_PRODUCT) {
		R48_PRODUCT = r48_PRODUCT;
	}

	public BigDecimal getR48_FIG_BAL_BWP1() {
		return R48_FIG_BAL_BWP1;
	}

	public void setR48_FIG_BAL_BWP1(BigDecimal r48_FIG_BAL_BWP1) {
		R48_FIG_BAL_BWP1 = r48_FIG_BAL_BWP1;
	}

	public BigDecimal getR48_FIG_BAL_BWP2() {
		return R48_FIG_BAL_BWP2;
	}

	public void setR48_FIG_BAL_BWP2(BigDecimal r48_FIG_BAL_BWP2) {
		R48_FIG_BAL_BWP2 = r48_FIG_BAL_BWP2;
	}

	public BigDecimal getR48_AMT_ADJ_BWP1() {
		return R48_AMT_ADJ_BWP1;
	}

	public void setR48_AMT_ADJ_BWP1(BigDecimal r48_AMT_ADJ_BWP1) {
		R48_AMT_ADJ_BWP1 = r48_AMT_ADJ_BWP1;
	}

	public BigDecimal getR48_AMT_ADJ_BWP2() {
		return R48_AMT_ADJ_BWP2;
	}

	public void setR48_AMT_ADJ_BWP2(BigDecimal r48_AMT_ADJ_BWP2) {
		R48_AMT_ADJ_BWP2 = r48_AMT_ADJ_BWP2;
	}

	public BigDecimal getR48_NET_AMT_BWP1() {
		return R48_NET_AMT_BWP1;
	}

	public void setR48_NET_AMT_BWP1(BigDecimal r48_NET_AMT_BWP1) {
		R48_NET_AMT_BWP1 = r48_NET_AMT_BWP1;
	}

	public BigDecimal getR48_NET_AMT_BWP2() {
		return R48_NET_AMT_BWP2;
	}

	public void setR48_NET_AMT_BWP2(BigDecimal r48_NET_AMT_BWP2) {
		R48_NET_AMT_BWP2 = r48_NET_AMT_BWP2;
	}

	public BigDecimal getR48_BAL_SUB_BWP1() {
		return R48_BAL_SUB_BWP1;
	}

	public void setR48_BAL_SUB_BWP1(BigDecimal r48_BAL_SUB_BWP1) {
		R48_BAL_SUB_BWP1 = r48_BAL_SUB_BWP1;
	}

	public BigDecimal getR48_BAL_SUB_BWP2() {
		return R48_BAL_SUB_BWP2;
	}

	public void setR48_BAL_SUB_BWP2(BigDecimal r48_BAL_SUB_BWP2) {
		R48_BAL_SUB_BWP2 = r48_BAL_SUB_BWP2;
	}

	public BigDecimal getR48_BAL_ACT_SUB_BWP1() {
		return R48_BAL_ACT_SUB_BWP1;
	}

	public void setR48_BAL_ACT_SUB_BWP1(BigDecimal r48_BAL_ACT_SUB_BWP1) {
		R48_BAL_ACT_SUB_BWP1 = r48_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR48_BAL_ACT_SUB_BWP2() {
		return R48_BAL_ACT_SUB_BWP2;
	}

	public void setR48_BAL_ACT_SUB_BWP2(BigDecimal r48_BAL_ACT_SUB_BWP2) {
		R48_BAL_ACT_SUB_BWP2 = r48_BAL_ACT_SUB_BWP2;
	}

	public String getR49_PRODUCT() {
		return R49_PRODUCT;
	}

	public void setR49_PRODUCT(String r49_PRODUCT) {
		R49_PRODUCT = r49_PRODUCT;
	}

	public BigDecimal getR49_FIG_BAL_BWP1() {
		return R49_FIG_BAL_BWP1;
	}

	public void setR49_FIG_BAL_BWP1(BigDecimal r49_FIG_BAL_BWP1) {
		R49_FIG_BAL_BWP1 = r49_FIG_BAL_BWP1;
	}

	public BigDecimal getR49_FIG_BAL_BWP2() {
		return R49_FIG_BAL_BWP2;
	}

	public void setR49_FIG_BAL_BWP2(BigDecimal r49_FIG_BAL_BWP2) {
		R49_FIG_BAL_BWP2 = r49_FIG_BAL_BWP2;
	}

	public BigDecimal getR49_AMT_ADJ_BWP1() {
		return R49_AMT_ADJ_BWP1;
	}

	public void setR49_AMT_ADJ_BWP1(BigDecimal r49_AMT_ADJ_BWP1) {
		R49_AMT_ADJ_BWP1 = r49_AMT_ADJ_BWP1;
	}

	public BigDecimal getR49_AMT_ADJ_BWP2() {
		return R49_AMT_ADJ_BWP2;
	}

	public void setR49_AMT_ADJ_BWP2(BigDecimal r49_AMT_ADJ_BWP2) {
		R49_AMT_ADJ_BWP2 = r49_AMT_ADJ_BWP2;
	}

	public BigDecimal getR49_NET_AMT_BWP1() {
		return R49_NET_AMT_BWP1;
	}

	public void setR49_NET_AMT_BWP1(BigDecimal r49_NET_AMT_BWP1) {
		R49_NET_AMT_BWP1 = r49_NET_AMT_BWP1;
	}

	public BigDecimal getR49_NET_AMT_BWP2() {
		return R49_NET_AMT_BWP2;
	}

	public void setR49_NET_AMT_BWP2(BigDecimal r49_NET_AMT_BWP2) {
		R49_NET_AMT_BWP2 = r49_NET_AMT_BWP2;
	}

	public BigDecimal getR49_BAL_SUB_BWP1() {
		return R49_BAL_SUB_BWP1;
	}

	public void setR49_BAL_SUB_BWP1(BigDecimal r49_BAL_SUB_BWP1) {
		R49_BAL_SUB_BWP1 = r49_BAL_SUB_BWP1;
	}

	public BigDecimal getR49_BAL_SUB_BWP2() {
		return R49_BAL_SUB_BWP2;
	}

	public void setR49_BAL_SUB_BWP2(BigDecimal r49_BAL_SUB_BWP2) {
		R49_BAL_SUB_BWP2 = r49_BAL_SUB_BWP2;
	}

	public BigDecimal getR49_BAL_ACT_SUB_BWP1() {
		return R49_BAL_ACT_SUB_BWP1;
	}

	public void setR49_BAL_ACT_SUB_BWP1(BigDecimal r49_BAL_ACT_SUB_BWP1) {
		R49_BAL_ACT_SUB_BWP1 = r49_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR49_BAL_ACT_SUB_BWP2() {
		return R49_BAL_ACT_SUB_BWP2;
	}

	public void setR49_BAL_ACT_SUB_BWP2(BigDecimal r49_BAL_ACT_SUB_BWP2) {
		R49_BAL_ACT_SUB_BWP2 = r49_BAL_ACT_SUB_BWP2;
	}

	public String getR50_PRODUCT() {
		return R50_PRODUCT;
	}

	public void setR50_PRODUCT(String r50_PRODUCT) {
		R50_PRODUCT = r50_PRODUCT;
	}

	public BigDecimal getR50_FIG_BAL_BWP1() {
		return R50_FIG_BAL_BWP1;
	}

	public void setR50_FIG_BAL_BWP1(BigDecimal r50_FIG_BAL_BWP1) {
		R50_FIG_BAL_BWP1 = r50_FIG_BAL_BWP1;
	}

	public BigDecimal getR50_FIG_BAL_BWP2() {
		return R50_FIG_BAL_BWP2;
	}

	public void setR50_FIG_BAL_BWP2(BigDecimal r50_FIG_BAL_BWP2) {
		R50_FIG_BAL_BWP2 = r50_FIG_BAL_BWP2;
	}

	public BigDecimal getR50_AMT_ADJ_BWP1() {
		return R50_AMT_ADJ_BWP1;
	}

	public void setR50_AMT_ADJ_BWP1(BigDecimal r50_AMT_ADJ_BWP1) {
		R50_AMT_ADJ_BWP1 = r50_AMT_ADJ_BWP1;
	}

	public BigDecimal getR50_AMT_ADJ_BWP2() {
		return R50_AMT_ADJ_BWP2;
	}

	public void setR50_AMT_ADJ_BWP2(BigDecimal r50_AMT_ADJ_BWP2) {
		R50_AMT_ADJ_BWP2 = r50_AMT_ADJ_BWP2;
	}

	public BigDecimal getR50_NET_AMT_BWP1() {
		return R50_NET_AMT_BWP1;
	}

	public void setR50_NET_AMT_BWP1(BigDecimal r50_NET_AMT_BWP1) {
		R50_NET_AMT_BWP1 = r50_NET_AMT_BWP1;
	}

	public BigDecimal getR50_NET_AMT_BWP2() {
		return R50_NET_AMT_BWP2;
	}

	public void setR50_NET_AMT_BWP2(BigDecimal r50_NET_AMT_BWP2) {
		R50_NET_AMT_BWP2 = r50_NET_AMT_BWP2;
	}

	public BigDecimal getR50_BAL_SUB_BWP1() {
		return R50_BAL_SUB_BWP1;
	}

	public void setR50_BAL_SUB_BWP1(BigDecimal r50_BAL_SUB_BWP1) {
		R50_BAL_SUB_BWP1 = r50_BAL_SUB_BWP1;
	}

	public BigDecimal getR50_BAL_SUB_BWP2() {
		return R50_BAL_SUB_BWP2;
	}

	public void setR50_BAL_SUB_BWP2(BigDecimal r50_BAL_SUB_BWP2) {
		R50_BAL_SUB_BWP2 = r50_BAL_SUB_BWP2;
	}

	public BigDecimal getR50_BAL_ACT_SUB_BWP1() {
		return R50_BAL_ACT_SUB_BWP1;
	}

	public void setR50_BAL_ACT_SUB_BWP1(BigDecimal r50_BAL_ACT_SUB_BWP1) {
		R50_BAL_ACT_SUB_BWP1 = r50_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR50_BAL_ACT_SUB_BWP2() {
		return R50_BAL_ACT_SUB_BWP2;
	}

	public void setR50_BAL_ACT_SUB_BWP2(BigDecimal r50_BAL_ACT_SUB_BWP2) {
		R50_BAL_ACT_SUB_BWP2 = r50_BAL_ACT_SUB_BWP2;
	}

	public String getR51_PRODUCT() {
		return R51_PRODUCT;
	}

	public void setR51_PRODUCT(String r51_PRODUCT) {
		R51_PRODUCT = r51_PRODUCT;
	}

	public BigDecimal getR51_FIG_BAL_BWP1() {
		return R51_FIG_BAL_BWP1;
	}

	public void setR51_FIG_BAL_BWP1(BigDecimal r51_FIG_BAL_BWP1) {
		R51_FIG_BAL_BWP1 = r51_FIG_BAL_BWP1;
	}

	public BigDecimal getR51_FIG_BAL_BWP2() {
		return R51_FIG_BAL_BWP2;
	}

	public void setR51_FIG_BAL_BWP2(BigDecimal r51_FIG_BAL_BWP2) {
		R51_FIG_BAL_BWP2 = r51_FIG_BAL_BWP2;
	}

	public BigDecimal getR51_AMT_ADJ_BWP1() {
		return R51_AMT_ADJ_BWP1;
	}

	public void setR51_AMT_ADJ_BWP1(BigDecimal r51_AMT_ADJ_BWP1) {
		R51_AMT_ADJ_BWP1 = r51_AMT_ADJ_BWP1;
	}

	public BigDecimal getR51_AMT_ADJ_BWP2() {
		return R51_AMT_ADJ_BWP2;
	}

	public void setR51_AMT_ADJ_BWP2(BigDecimal r51_AMT_ADJ_BWP2) {
		R51_AMT_ADJ_BWP2 = r51_AMT_ADJ_BWP2;
	}

	public BigDecimal getR51_NET_AMT_BWP1() {
		return R51_NET_AMT_BWP1;
	}

	public void setR51_NET_AMT_BWP1(BigDecimal r51_NET_AMT_BWP1) {
		R51_NET_AMT_BWP1 = r51_NET_AMT_BWP1;
	}

	public BigDecimal getR51_NET_AMT_BWP2() {
		return R51_NET_AMT_BWP2;
	}

	public void setR51_NET_AMT_BWP2(BigDecimal r51_NET_AMT_BWP2) {
		R51_NET_AMT_BWP2 = r51_NET_AMT_BWP2;
	}

	public BigDecimal getR51_BAL_SUB_BWP1() {
		return R51_BAL_SUB_BWP1;
	}

	public void setR51_BAL_SUB_BWP1(BigDecimal r51_BAL_SUB_BWP1) {
		R51_BAL_SUB_BWP1 = r51_BAL_SUB_BWP1;
	}

	public BigDecimal getR51_BAL_SUB_BWP2() {
		return R51_BAL_SUB_BWP2;
	}

	public void setR51_BAL_SUB_BWP2(BigDecimal r51_BAL_SUB_BWP2) {
		R51_BAL_SUB_BWP2 = r51_BAL_SUB_BWP2;
	}

	public BigDecimal getR51_BAL_ACT_SUB_BWP1() {
		return R51_BAL_ACT_SUB_BWP1;
	}

	public void setR51_BAL_ACT_SUB_BWP1(BigDecimal r51_BAL_ACT_SUB_BWP1) {
		R51_BAL_ACT_SUB_BWP1 = r51_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR51_BAL_ACT_SUB_BWP2() {
		return R51_BAL_ACT_SUB_BWP2;
	}

	public void setR51_BAL_ACT_SUB_BWP2(BigDecimal r51_BAL_ACT_SUB_BWP2) {
		R51_BAL_ACT_SUB_BWP2 = r51_BAL_ACT_SUB_BWP2;
	}

	public String getR52_PRODUCT() {
		return R52_PRODUCT;
	}

	public void setR52_PRODUCT(String r52_PRODUCT) {
		R52_PRODUCT = r52_PRODUCT;
	}

	public BigDecimal getR52_FIG_BAL_BWP1() {
		return R52_FIG_BAL_BWP1;
	}

	public void setR52_FIG_BAL_BWP1(BigDecimal r52_FIG_BAL_BWP1) {
		R52_FIG_BAL_BWP1 = r52_FIG_BAL_BWP1;
	}

	public BigDecimal getR52_FIG_BAL_BWP2() {
		return R52_FIG_BAL_BWP2;
	}

	public void setR52_FIG_BAL_BWP2(BigDecimal r52_FIG_BAL_BWP2) {
		R52_FIG_BAL_BWP2 = r52_FIG_BAL_BWP2;
	}

	public BigDecimal getR52_AMT_ADJ_BWP1() {
		return R52_AMT_ADJ_BWP1;
	}

	public void setR52_AMT_ADJ_BWP1(BigDecimal r52_AMT_ADJ_BWP1) {
		R52_AMT_ADJ_BWP1 = r52_AMT_ADJ_BWP1;
	}

	public BigDecimal getR52_AMT_ADJ_BWP2() {
		return R52_AMT_ADJ_BWP2;
	}

	public void setR52_AMT_ADJ_BWP2(BigDecimal r52_AMT_ADJ_BWP2) {
		R52_AMT_ADJ_BWP2 = r52_AMT_ADJ_BWP2;
	}

	public BigDecimal getR52_NET_AMT_BWP1() {
		return R52_NET_AMT_BWP1;
	}

	public void setR52_NET_AMT_BWP1(BigDecimal r52_NET_AMT_BWP1) {
		R52_NET_AMT_BWP1 = r52_NET_AMT_BWP1;
	}

	public BigDecimal getR52_NET_AMT_BWP2() {
		return R52_NET_AMT_BWP2;
	}

	public void setR52_NET_AMT_BWP2(BigDecimal r52_NET_AMT_BWP2) {
		R52_NET_AMT_BWP2 = r52_NET_AMT_BWP2;
	}

	public BigDecimal getR52_BAL_SUB_BWP1() {
		return R52_BAL_SUB_BWP1;
	}

	public void setR52_BAL_SUB_BWP1(BigDecimal r52_BAL_SUB_BWP1) {
		R52_BAL_SUB_BWP1 = r52_BAL_SUB_BWP1;
	}

	public BigDecimal getR52_BAL_SUB_BWP2() {
		return R52_BAL_SUB_BWP2;
	}

	public void setR52_BAL_SUB_BWP2(BigDecimal r52_BAL_SUB_BWP2) {
		R52_BAL_SUB_BWP2 = r52_BAL_SUB_BWP2;
	}

	public BigDecimal getR52_BAL_ACT_SUB_BWP1() {
		return R52_BAL_ACT_SUB_BWP1;
	}

	public void setR52_BAL_ACT_SUB_BWP1(BigDecimal r52_BAL_ACT_SUB_BWP1) {
		R52_BAL_ACT_SUB_BWP1 = r52_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR52_BAL_ACT_SUB_BWP2() {
		return R52_BAL_ACT_SUB_BWP2;
	}

	public void setR52_BAL_ACT_SUB_BWP2(BigDecimal r52_BAL_ACT_SUB_BWP2) {
		R52_BAL_ACT_SUB_BWP2 = r52_BAL_ACT_SUB_BWP2;
	}

	public String getR53_PRODUCT() {
		return R53_PRODUCT;
	}

	public void setR53_PRODUCT(String r53_PRODUCT) {
		R53_PRODUCT = r53_PRODUCT;
	}

	public BigDecimal getR53_FIG_BAL_BWP1() {
		return R53_FIG_BAL_BWP1;
	}

	public void setR53_FIG_BAL_BWP1(BigDecimal r53_FIG_BAL_BWP1) {
		R53_FIG_BAL_BWP1 = r53_FIG_BAL_BWP1;
	}

	public BigDecimal getR53_FIG_BAL_BWP2() {
		return R53_FIG_BAL_BWP2;
	}

	public void setR53_FIG_BAL_BWP2(BigDecimal r53_FIG_BAL_BWP2) {
		R53_FIG_BAL_BWP2 = r53_FIG_BAL_BWP2;
	}

	public BigDecimal getR53_AMT_ADJ_BWP1() {
		return R53_AMT_ADJ_BWP1;
	}

	public void setR53_AMT_ADJ_BWP1(BigDecimal r53_AMT_ADJ_BWP1) {
		R53_AMT_ADJ_BWP1 = r53_AMT_ADJ_BWP1;
	}

	public BigDecimal getR53_AMT_ADJ_BWP2() {
		return R53_AMT_ADJ_BWP2;
	}

	public void setR53_AMT_ADJ_BWP2(BigDecimal r53_AMT_ADJ_BWP2) {
		R53_AMT_ADJ_BWP2 = r53_AMT_ADJ_BWP2;
	}

	public BigDecimal getR53_NET_AMT_BWP1() {
		return R53_NET_AMT_BWP1;
	}

	public void setR53_NET_AMT_BWP1(BigDecimal r53_NET_AMT_BWP1) {
		R53_NET_AMT_BWP1 = r53_NET_AMT_BWP1;
	}

	public BigDecimal getR53_NET_AMT_BWP2() {
		return R53_NET_AMT_BWP2;
	}

	public void setR53_NET_AMT_BWP2(BigDecimal r53_NET_AMT_BWP2) {
		R53_NET_AMT_BWP2 = r53_NET_AMT_BWP2;
	}

	public BigDecimal getR53_BAL_SUB_BWP1() {
		return R53_BAL_SUB_BWP1;
	}

	public void setR53_BAL_SUB_BWP1(BigDecimal r53_BAL_SUB_BWP1) {
		R53_BAL_SUB_BWP1 = r53_BAL_SUB_BWP1;
	}

	public BigDecimal getR53_BAL_SUB_BWP2() {
		return R53_BAL_SUB_BWP2;
	}

	public void setR53_BAL_SUB_BWP2(BigDecimal r53_BAL_SUB_BWP2) {
		R53_BAL_SUB_BWP2 = r53_BAL_SUB_BWP2;
	}

	public BigDecimal getR53_BAL_ACT_SUB_BWP1() {
		return R53_BAL_ACT_SUB_BWP1;
	}

	public void setR53_BAL_ACT_SUB_BWP1(BigDecimal r53_BAL_ACT_SUB_BWP1) {
		R53_BAL_ACT_SUB_BWP1 = r53_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR53_BAL_ACT_SUB_BWP2() {
		return R53_BAL_ACT_SUB_BWP2;
	}

	public void setR53_BAL_ACT_SUB_BWP2(BigDecimal r53_BAL_ACT_SUB_BWP2) {
		R53_BAL_ACT_SUB_BWP2 = r53_BAL_ACT_SUB_BWP2;
	}

	public String getR54_PRODUCT() {
		return R54_PRODUCT;
	}

	public void setR54_PRODUCT(String r54_PRODUCT) {
		R54_PRODUCT = r54_PRODUCT;
	}

	public BigDecimal getR54_FIG_BAL_BWP1() {
		return R54_FIG_BAL_BWP1;
	}

	public void setR54_FIG_BAL_BWP1(BigDecimal r54_FIG_BAL_BWP1) {
		R54_FIG_BAL_BWP1 = r54_FIG_BAL_BWP1;
	}

	public BigDecimal getR54_FIG_BAL_BWP2() {
		return R54_FIG_BAL_BWP2;
	}

	public void setR54_FIG_BAL_BWP2(BigDecimal r54_FIG_BAL_BWP2) {
		R54_FIG_BAL_BWP2 = r54_FIG_BAL_BWP2;
	}

	public BigDecimal getR54_AMT_ADJ_BWP1() {
		return R54_AMT_ADJ_BWP1;
	}

	public void setR54_AMT_ADJ_BWP1(BigDecimal r54_AMT_ADJ_BWP1) {
		R54_AMT_ADJ_BWP1 = r54_AMT_ADJ_BWP1;
	}

	public BigDecimal getR54_AMT_ADJ_BWP2() {
		return R54_AMT_ADJ_BWP2;
	}

	public void setR54_AMT_ADJ_BWP2(BigDecimal r54_AMT_ADJ_BWP2) {
		R54_AMT_ADJ_BWP2 = r54_AMT_ADJ_BWP2;
	}

	public BigDecimal getR54_NET_AMT_BWP1() {
		return R54_NET_AMT_BWP1;
	}

	public void setR54_NET_AMT_BWP1(BigDecimal r54_NET_AMT_BWP1) {
		R54_NET_AMT_BWP1 = r54_NET_AMT_BWP1;
	}

	public BigDecimal getR54_NET_AMT_BWP2() {
		return R54_NET_AMT_BWP2;
	}

	public void setR54_NET_AMT_BWP2(BigDecimal r54_NET_AMT_BWP2) {
		R54_NET_AMT_BWP2 = r54_NET_AMT_BWP2;
	}

	public BigDecimal getR54_BAL_SUB_BWP1() {
		return R54_BAL_SUB_BWP1;
	}

	public void setR54_BAL_SUB_BWP1(BigDecimal r54_BAL_SUB_BWP1) {
		R54_BAL_SUB_BWP1 = r54_BAL_SUB_BWP1;
	}

	public BigDecimal getR54_BAL_SUB_BWP2() {
		return R54_BAL_SUB_BWP2;
	}

	public void setR54_BAL_SUB_BWP2(BigDecimal r54_BAL_SUB_BWP2) {
		R54_BAL_SUB_BWP2 = r54_BAL_SUB_BWP2;
	}

	public BigDecimal getR54_BAL_ACT_SUB_BWP1() {
		return R54_BAL_ACT_SUB_BWP1;
	}

	public void setR54_BAL_ACT_SUB_BWP1(BigDecimal r54_BAL_ACT_SUB_BWP1) {
		R54_BAL_ACT_SUB_BWP1 = r54_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR54_BAL_ACT_SUB_BWP2() {
		return R54_BAL_ACT_SUB_BWP2;
	}

	public void setR54_BAL_ACT_SUB_BWP2(BigDecimal r54_BAL_ACT_SUB_BWP2) {
		R54_BAL_ACT_SUB_BWP2 = r54_BAL_ACT_SUB_BWP2;
	}

	public String getR55_PRODUCT() {
		return R55_PRODUCT;
	}

	public void setR55_PRODUCT(String r55_PRODUCT) {
		R55_PRODUCT = r55_PRODUCT;
	}

	public BigDecimal getR55_FIG_BAL_BWP1() {
		return R55_FIG_BAL_BWP1;
	}

	public void setR55_FIG_BAL_BWP1(BigDecimal r55_FIG_BAL_BWP1) {
		R55_FIG_BAL_BWP1 = r55_FIG_BAL_BWP1;
	}

	public BigDecimal getR55_FIG_BAL_BWP2() {
		return R55_FIG_BAL_BWP2;
	}

	public void setR55_FIG_BAL_BWP2(BigDecimal r55_FIG_BAL_BWP2) {
		R55_FIG_BAL_BWP2 = r55_FIG_BAL_BWP2;
	}

	public BigDecimal getR55_AMT_ADJ_BWP1() {
		return R55_AMT_ADJ_BWP1;
	}

	public void setR55_AMT_ADJ_BWP1(BigDecimal r55_AMT_ADJ_BWP1) {
		R55_AMT_ADJ_BWP1 = r55_AMT_ADJ_BWP1;
	}

	public BigDecimal getR55_AMT_ADJ_BWP2() {
		return R55_AMT_ADJ_BWP2;
	}

	public void setR55_AMT_ADJ_BWP2(BigDecimal r55_AMT_ADJ_BWP2) {
		R55_AMT_ADJ_BWP2 = r55_AMT_ADJ_BWP2;
	}

	public BigDecimal getR55_NET_AMT_BWP1() {
		return R55_NET_AMT_BWP1;
	}

	public void setR55_NET_AMT_BWP1(BigDecimal r55_NET_AMT_BWP1) {
		R55_NET_AMT_BWP1 = r55_NET_AMT_BWP1;
	}

	public BigDecimal getR55_NET_AMT_BWP2() {
		return R55_NET_AMT_BWP2;
	}

	public void setR55_NET_AMT_BWP2(BigDecimal r55_NET_AMT_BWP2) {
		R55_NET_AMT_BWP2 = r55_NET_AMT_BWP2;
	}

	public BigDecimal getR55_BAL_SUB_BWP1() {
		return R55_BAL_SUB_BWP1;
	}

	public void setR55_BAL_SUB_BWP1(BigDecimal r55_BAL_SUB_BWP1) {
		R55_BAL_SUB_BWP1 = r55_BAL_SUB_BWP1;
	}

	public BigDecimal getR55_BAL_SUB_BWP2() {
		return R55_BAL_SUB_BWP2;
	}

	public void setR55_BAL_SUB_BWP2(BigDecimal r55_BAL_SUB_BWP2) {
		R55_BAL_SUB_BWP2 = r55_BAL_SUB_BWP2;
	}

	public BigDecimal getR55_BAL_ACT_SUB_BWP1() {
		return R55_BAL_ACT_SUB_BWP1;
	}

	public void setR55_BAL_ACT_SUB_BWP1(BigDecimal r55_BAL_ACT_SUB_BWP1) {
		R55_BAL_ACT_SUB_BWP1 = r55_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR55_BAL_ACT_SUB_BWP2() {
		return R55_BAL_ACT_SUB_BWP2;
	}

	public void setR55_BAL_ACT_SUB_BWP2(BigDecimal r55_BAL_ACT_SUB_BWP2) {
		R55_BAL_ACT_SUB_BWP2 = r55_BAL_ACT_SUB_BWP2;
	}

	public String getR56_PRODUCT() {
		return R56_PRODUCT;
	}

	public void setR56_PRODUCT(String r56_PRODUCT) {
		R56_PRODUCT = r56_PRODUCT;
	}

	public BigDecimal getR56_FIG_BAL_BWP1() {
		return R56_FIG_BAL_BWP1;
	}

	public void setR56_FIG_BAL_BWP1(BigDecimal r56_FIG_BAL_BWP1) {
		R56_FIG_BAL_BWP1 = r56_FIG_BAL_BWP1;
	}

	public BigDecimal getR56_FIG_BAL_BWP2() {
		return R56_FIG_BAL_BWP2;
	}

	public void setR56_FIG_BAL_BWP2(BigDecimal r56_FIG_BAL_BWP2) {
		R56_FIG_BAL_BWP2 = r56_FIG_BAL_BWP2;
	}

	public BigDecimal getR56_AMT_ADJ_BWP1() {
		return R56_AMT_ADJ_BWP1;
	}

	public void setR56_AMT_ADJ_BWP1(BigDecimal r56_AMT_ADJ_BWP1) {
		R56_AMT_ADJ_BWP1 = r56_AMT_ADJ_BWP1;
	}

	public BigDecimal getR56_AMT_ADJ_BWP2() {
		return R56_AMT_ADJ_BWP2;
	}

	public void setR56_AMT_ADJ_BWP2(BigDecimal r56_AMT_ADJ_BWP2) {
		R56_AMT_ADJ_BWP2 = r56_AMT_ADJ_BWP2;
	}

	public BigDecimal getR56_NET_AMT_BWP1() {
		return R56_NET_AMT_BWP1;
	}

	public void setR56_NET_AMT_BWP1(BigDecimal r56_NET_AMT_BWP1) {
		R56_NET_AMT_BWP1 = r56_NET_AMT_BWP1;
	}

	public BigDecimal getR56_NET_AMT_BWP2() {
		return R56_NET_AMT_BWP2;
	}

	public void setR56_NET_AMT_BWP2(BigDecimal r56_NET_AMT_BWP2) {
		R56_NET_AMT_BWP2 = r56_NET_AMT_BWP2;
	}

	public BigDecimal getR56_BAL_SUB_BWP1() {
		return R56_BAL_SUB_BWP1;
	}

	public void setR56_BAL_SUB_BWP1(BigDecimal r56_BAL_SUB_BWP1) {
		R56_BAL_SUB_BWP1 = r56_BAL_SUB_BWP1;
	}

	public BigDecimal getR56_BAL_SUB_BWP2() {
		return R56_BAL_SUB_BWP2;
	}

	public void setR56_BAL_SUB_BWP2(BigDecimal r56_BAL_SUB_BWP2) {
		R56_BAL_SUB_BWP2 = r56_BAL_SUB_BWP2;
	}

	public BigDecimal getR56_BAL_ACT_SUB_BWP1() {
		return R56_BAL_ACT_SUB_BWP1;
	}

	public void setR56_BAL_ACT_SUB_BWP1(BigDecimal r56_BAL_ACT_SUB_BWP1) {
		R56_BAL_ACT_SUB_BWP1 = r56_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR56_BAL_ACT_SUB_BWP2() {
		return R56_BAL_ACT_SUB_BWP2;
	}

	public void setR56_BAL_ACT_SUB_BWP2(BigDecimal r56_BAL_ACT_SUB_BWP2) {
		R56_BAL_ACT_SUB_BWP2 = r56_BAL_ACT_SUB_BWP2;
	}

	public String getR57_PRODUCT() {
		return R57_PRODUCT;
	}

	public void setR57_PRODUCT(String r57_PRODUCT) {
		R57_PRODUCT = r57_PRODUCT;
	}

	public BigDecimal getR57_FIG_BAL_BWP1() {
		return R57_FIG_BAL_BWP1;
	}

	public void setR57_FIG_BAL_BWP1(BigDecimal r57_FIG_BAL_BWP1) {
		R57_FIG_BAL_BWP1 = r57_FIG_BAL_BWP1;
	}

	public BigDecimal getR57_FIG_BAL_BWP2() {
		return R57_FIG_BAL_BWP2;
	}

	public void setR57_FIG_BAL_BWP2(BigDecimal r57_FIG_BAL_BWP2) {
		R57_FIG_BAL_BWP2 = r57_FIG_BAL_BWP2;
	}

	public BigDecimal getR57_AMT_ADJ_BWP1() {
		return R57_AMT_ADJ_BWP1;
	}

	public void setR57_AMT_ADJ_BWP1(BigDecimal r57_AMT_ADJ_BWP1) {
		R57_AMT_ADJ_BWP1 = r57_AMT_ADJ_BWP1;
	}

	public BigDecimal getR57_AMT_ADJ_BWP2() {
		return R57_AMT_ADJ_BWP2;
	}

	public void setR57_AMT_ADJ_BWP2(BigDecimal r57_AMT_ADJ_BWP2) {
		R57_AMT_ADJ_BWP2 = r57_AMT_ADJ_BWP2;
	}

	public BigDecimal getR57_NET_AMT_BWP1() {
		return R57_NET_AMT_BWP1;
	}

	public void setR57_NET_AMT_BWP1(BigDecimal r57_NET_AMT_BWP1) {
		R57_NET_AMT_BWP1 = r57_NET_AMT_BWP1;
	}

	public BigDecimal getR57_NET_AMT_BWP2() {
		return R57_NET_AMT_BWP2;
	}

	public void setR57_NET_AMT_BWP2(BigDecimal r57_NET_AMT_BWP2) {
		R57_NET_AMT_BWP2 = r57_NET_AMT_BWP2;
	}

	public BigDecimal getR57_BAL_SUB_BWP1() {
		return R57_BAL_SUB_BWP1;
	}

	public void setR57_BAL_SUB_BWP1(BigDecimal r57_BAL_SUB_BWP1) {
		R57_BAL_SUB_BWP1 = r57_BAL_SUB_BWP1;
	}

	public BigDecimal getR57_BAL_SUB_BWP2() {
		return R57_BAL_SUB_BWP2;
	}

	public void setR57_BAL_SUB_BWP2(BigDecimal r57_BAL_SUB_BWP2) {
		R57_BAL_SUB_BWP2 = r57_BAL_SUB_BWP2;
	}

	public BigDecimal getR57_BAL_ACT_SUB_BWP1() {
		return R57_BAL_ACT_SUB_BWP1;
	}

	public void setR57_BAL_ACT_SUB_BWP1(BigDecimal r57_BAL_ACT_SUB_BWP1) {
		R57_BAL_ACT_SUB_BWP1 = r57_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR57_BAL_ACT_SUB_BWP2() {
		return R57_BAL_ACT_SUB_BWP2;
	}

	public void setR57_BAL_ACT_SUB_BWP2(BigDecimal r57_BAL_ACT_SUB_BWP2) {
		R57_BAL_ACT_SUB_BWP2 = r57_BAL_ACT_SUB_BWP2;
	}

	public String getR58_PRODUCT() {
		return R58_PRODUCT;
	}

	public void setR58_PRODUCT(String r58_PRODUCT) {
		R58_PRODUCT = r58_PRODUCT;
	}

	public BigDecimal getR58_FIG_BAL_BWP1() {
		return R58_FIG_BAL_BWP1;
	}

	public void setR58_FIG_BAL_BWP1(BigDecimal r58_FIG_BAL_BWP1) {
		R58_FIG_BAL_BWP1 = r58_FIG_BAL_BWP1;
	}

	public BigDecimal getR58_FIG_BAL_BWP2() {
		return R58_FIG_BAL_BWP2;
	}

	public void setR58_FIG_BAL_BWP2(BigDecimal r58_FIG_BAL_BWP2) {
		R58_FIG_BAL_BWP2 = r58_FIG_BAL_BWP2;
	}

	public BigDecimal getR58_AMT_ADJ_BWP1() {
		return R58_AMT_ADJ_BWP1;
	}

	public void setR58_AMT_ADJ_BWP1(BigDecimal r58_AMT_ADJ_BWP1) {
		R58_AMT_ADJ_BWP1 = r58_AMT_ADJ_BWP1;
	}

	public BigDecimal getR58_AMT_ADJ_BWP2() {
		return R58_AMT_ADJ_BWP2;
	}

	public void setR58_AMT_ADJ_BWP2(BigDecimal r58_AMT_ADJ_BWP2) {
		R58_AMT_ADJ_BWP2 = r58_AMT_ADJ_BWP2;
	}

	public BigDecimal getR58_NET_AMT_BWP1() {
		return R58_NET_AMT_BWP1;
	}

	public void setR58_NET_AMT_BWP1(BigDecimal r58_NET_AMT_BWP1) {
		R58_NET_AMT_BWP1 = r58_NET_AMT_BWP1;
	}

	public BigDecimal getR58_NET_AMT_BWP2() {
		return R58_NET_AMT_BWP2;
	}

	public void setR58_NET_AMT_BWP2(BigDecimal r58_NET_AMT_BWP2) {
		R58_NET_AMT_BWP2 = r58_NET_AMT_BWP2;
	}

	public BigDecimal getR58_BAL_SUB_BWP1() {
		return R58_BAL_SUB_BWP1;
	}

	public void setR58_BAL_SUB_BWP1(BigDecimal r58_BAL_SUB_BWP1) {
		R58_BAL_SUB_BWP1 = r58_BAL_SUB_BWP1;
	}

	public BigDecimal getR58_BAL_SUB_BWP2() {
		return R58_BAL_SUB_BWP2;
	}

	public void setR58_BAL_SUB_BWP2(BigDecimal r58_BAL_SUB_BWP2) {
		R58_BAL_SUB_BWP2 = r58_BAL_SUB_BWP2;
	}

	public BigDecimal getR58_BAL_ACT_SUB_BWP1() {
		return R58_BAL_ACT_SUB_BWP1;
	}

	public void setR58_BAL_ACT_SUB_BWP1(BigDecimal r58_BAL_ACT_SUB_BWP1) {
		R58_BAL_ACT_SUB_BWP1 = r58_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR58_BAL_ACT_SUB_BWP2() {
		return R58_BAL_ACT_SUB_BWP2;
	}

	public void setR58_BAL_ACT_SUB_BWP2(BigDecimal r58_BAL_ACT_SUB_BWP2) {
		R58_BAL_ACT_SUB_BWP2 = r58_BAL_ACT_SUB_BWP2;
	}

	public String getR59_PRODUCT() {
		return R59_PRODUCT;
	}

	public void setR59_PRODUCT(String r59_PRODUCT) {
		R59_PRODUCT = r59_PRODUCT;
	}

	public BigDecimal getR59_FIG_BAL_BWP1() {
		return R59_FIG_BAL_BWP1;
	}

	public void setR59_FIG_BAL_BWP1(BigDecimal r59_FIG_BAL_BWP1) {
		R59_FIG_BAL_BWP1 = r59_FIG_BAL_BWP1;
	}

	public BigDecimal getR59_FIG_BAL_BWP2() {
		return R59_FIG_BAL_BWP2;
	}

	public void setR59_FIG_BAL_BWP2(BigDecimal r59_FIG_BAL_BWP2) {
		R59_FIG_BAL_BWP2 = r59_FIG_BAL_BWP2;
	}

	public BigDecimal getR59_AMT_ADJ_BWP1() {
		return R59_AMT_ADJ_BWP1;
	}

	public void setR59_AMT_ADJ_BWP1(BigDecimal r59_AMT_ADJ_BWP1) {
		R59_AMT_ADJ_BWP1 = r59_AMT_ADJ_BWP1;
	}

	public BigDecimal getR59_AMT_ADJ_BWP2() {
		return R59_AMT_ADJ_BWP2;
	}

	public void setR59_AMT_ADJ_BWP2(BigDecimal r59_AMT_ADJ_BWP2) {
		R59_AMT_ADJ_BWP2 = r59_AMT_ADJ_BWP2;
	}

	public BigDecimal getR59_NET_AMT_BWP1() {
		return R59_NET_AMT_BWP1;
	}

	public void setR59_NET_AMT_BWP1(BigDecimal r59_NET_AMT_BWP1) {
		R59_NET_AMT_BWP1 = r59_NET_AMT_BWP1;
	}

	public BigDecimal getR59_NET_AMT_BWP2() {
		return R59_NET_AMT_BWP2;
	}

	public void setR59_NET_AMT_BWP2(BigDecimal r59_NET_AMT_BWP2) {
		R59_NET_AMT_BWP2 = r59_NET_AMT_BWP2;
	}

	public BigDecimal getR59_BAL_SUB_BWP1() {
		return R59_BAL_SUB_BWP1;
	}

	public void setR59_BAL_SUB_BWP1(BigDecimal r59_BAL_SUB_BWP1) {
		R59_BAL_SUB_BWP1 = r59_BAL_SUB_BWP1;
	}

	public BigDecimal getR59_BAL_SUB_BWP2() {
		return R59_BAL_SUB_BWP2;
	}

	public void setR59_BAL_SUB_BWP2(BigDecimal r59_BAL_SUB_BWP2) {
		R59_BAL_SUB_BWP2 = r59_BAL_SUB_BWP2;
	}

	public BigDecimal getR59_BAL_ACT_SUB_BWP1() {
		return R59_BAL_ACT_SUB_BWP1;
	}

	public void setR59_BAL_ACT_SUB_BWP1(BigDecimal r59_BAL_ACT_SUB_BWP1) {
		R59_BAL_ACT_SUB_BWP1 = r59_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR59_BAL_ACT_SUB_BWP2() {
		return R59_BAL_ACT_SUB_BWP2;
	}

	public void setR59_BAL_ACT_SUB_BWP2(BigDecimal r59_BAL_ACT_SUB_BWP2) {
		R59_BAL_ACT_SUB_BWP2 = r59_BAL_ACT_SUB_BWP2;
	}

	public String getR60_PRODUCT() {
		return R60_PRODUCT;
	}

	public void setR60_PRODUCT(String r60_PRODUCT) {
		R60_PRODUCT = r60_PRODUCT;
	}

	public BigDecimal getR60_FIG_BAL_BWP1() {
		return R60_FIG_BAL_BWP1;
	}

	public void setR60_FIG_BAL_BWP1(BigDecimal r60_FIG_BAL_BWP1) {
		R60_FIG_BAL_BWP1 = r60_FIG_BAL_BWP1;
	}

	public BigDecimal getR60_FIG_BAL_BWP2() {
		return R60_FIG_BAL_BWP2;
	}

	public void setR60_FIG_BAL_BWP2(BigDecimal r60_FIG_BAL_BWP2) {
		R60_FIG_BAL_BWP2 = r60_FIG_BAL_BWP2;
	}

	public BigDecimal getR60_AMT_ADJ_BWP1() {
		return R60_AMT_ADJ_BWP1;
	}

	public void setR60_AMT_ADJ_BWP1(BigDecimal r60_AMT_ADJ_BWP1) {
		R60_AMT_ADJ_BWP1 = r60_AMT_ADJ_BWP1;
	}

	public BigDecimal getR60_AMT_ADJ_BWP2() {
		return R60_AMT_ADJ_BWP2;
	}

	public void setR60_AMT_ADJ_BWP2(BigDecimal r60_AMT_ADJ_BWP2) {
		R60_AMT_ADJ_BWP2 = r60_AMT_ADJ_BWP2;
	}

	public BigDecimal getR60_NET_AMT_BWP1() {
		return R60_NET_AMT_BWP1;
	}

	public void setR60_NET_AMT_BWP1(BigDecimal r60_NET_AMT_BWP1) {
		R60_NET_AMT_BWP1 = r60_NET_AMT_BWP1;
	}

	public BigDecimal getR60_NET_AMT_BWP2() {
		return R60_NET_AMT_BWP2;
	}

	public void setR60_NET_AMT_BWP2(BigDecimal r60_NET_AMT_BWP2) {
		R60_NET_AMT_BWP2 = r60_NET_AMT_BWP2;
	}

	public BigDecimal getR60_BAL_SUB_BWP1() {
		return R60_BAL_SUB_BWP1;
	}

	public void setR60_BAL_SUB_BWP1(BigDecimal r60_BAL_SUB_BWP1) {
		R60_BAL_SUB_BWP1 = r60_BAL_SUB_BWP1;
	}

	public BigDecimal getR60_BAL_SUB_BWP2() {
		return R60_BAL_SUB_BWP2;
	}

	public void setR60_BAL_SUB_BWP2(BigDecimal r60_BAL_SUB_BWP2) {
		R60_BAL_SUB_BWP2 = r60_BAL_SUB_BWP2;
	}

	public BigDecimal getR60_BAL_ACT_SUB_BWP1() {
		return R60_BAL_ACT_SUB_BWP1;
	}

	public void setR60_BAL_ACT_SUB_BWP1(BigDecimal r60_BAL_ACT_SUB_BWP1) {
		R60_BAL_ACT_SUB_BWP1 = r60_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR60_BAL_ACT_SUB_BWP2() {
		return R60_BAL_ACT_SUB_BWP2;
	}

	public void setR60_BAL_ACT_SUB_BWP2(BigDecimal r60_BAL_ACT_SUB_BWP2) {
		R60_BAL_ACT_SUB_BWP2 = r60_BAL_ACT_SUB_BWP2;
	}

	public String getR61_PRODUCT() {
		return R61_PRODUCT;
	}

	public void setR61_PRODUCT(String r61_PRODUCT) {
		R61_PRODUCT = r61_PRODUCT;
	}

	public BigDecimal getR61_FIG_BAL_BWP1() {
		return R61_FIG_BAL_BWP1;
	}

	public void setR61_FIG_BAL_BWP1(BigDecimal r61_FIG_BAL_BWP1) {
		R61_FIG_BAL_BWP1 = r61_FIG_BAL_BWP1;
	}

	public BigDecimal getR61_FIG_BAL_BWP2() {
		return R61_FIG_BAL_BWP2;
	}

	public void setR61_FIG_BAL_BWP2(BigDecimal r61_FIG_BAL_BWP2) {
		R61_FIG_BAL_BWP2 = r61_FIG_BAL_BWP2;
	}

	public BigDecimal getR61_AMT_ADJ_BWP1() {
		return R61_AMT_ADJ_BWP1;
	}

	public void setR61_AMT_ADJ_BWP1(BigDecimal r61_AMT_ADJ_BWP1) {
		R61_AMT_ADJ_BWP1 = r61_AMT_ADJ_BWP1;
	}

	public BigDecimal getR61_AMT_ADJ_BWP2() {
		return R61_AMT_ADJ_BWP2;
	}

	public void setR61_AMT_ADJ_BWP2(BigDecimal r61_AMT_ADJ_BWP2) {
		R61_AMT_ADJ_BWP2 = r61_AMT_ADJ_BWP2;
	}

	public BigDecimal getR61_NET_AMT_BWP1() {
		return R61_NET_AMT_BWP1;
	}

	public void setR61_NET_AMT_BWP1(BigDecimal r61_NET_AMT_BWP1) {
		R61_NET_AMT_BWP1 = r61_NET_AMT_BWP1;
	}

	public BigDecimal getR61_NET_AMT_BWP2() {
		return R61_NET_AMT_BWP2;
	}

	public void setR61_NET_AMT_BWP2(BigDecimal r61_NET_AMT_BWP2) {
		R61_NET_AMT_BWP2 = r61_NET_AMT_BWP2;
	}

	public BigDecimal getR61_BAL_SUB_BWP1() {
		return R61_BAL_SUB_BWP1;
	}

	public void setR61_BAL_SUB_BWP1(BigDecimal r61_BAL_SUB_BWP1) {
		R61_BAL_SUB_BWP1 = r61_BAL_SUB_BWP1;
	}

	public BigDecimal getR61_BAL_SUB_BWP2() {
		return R61_BAL_SUB_BWP2;
	}

	public void setR61_BAL_SUB_BWP2(BigDecimal r61_BAL_SUB_BWP2) {
		R61_BAL_SUB_BWP2 = r61_BAL_SUB_BWP2;
	}

	public BigDecimal getR61_BAL_ACT_SUB_BWP1() {
		return R61_BAL_ACT_SUB_BWP1;
	}

	public void setR61_BAL_ACT_SUB_BWP1(BigDecimal r61_BAL_ACT_SUB_BWP1) {
		R61_BAL_ACT_SUB_BWP1 = r61_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR61_BAL_ACT_SUB_BWP2() {
		return R61_BAL_ACT_SUB_BWP2;
	}

	public void setR61_BAL_ACT_SUB_BWP2(BigDecimal r61_BAL_ACT_SUB_BWP2) {
		R61_BAL_ACT_SUB_BWP2 = r61_BAL_ACT_SUB_BWP2;
	}

	public String getR62_PRODUCT() {
		return R62_PRODUCT;
	}

	public void setR62_PRODUCT(String r62_PRODUCT) {
		R62_PRODUCT = r62_PRODUCT;
	}

	public BigDecimal getR62_FIG_BAL_BWP1() {
		return R62_FIG_BAL_BWP1;
	}

	public void setR62_FIG_BAL_BWP1(BigDecimal r62_FIG_BAL_BWP1) {
		R62_FIG_BAL_BWP1 = r62_FIG_BAL_BWP1;
	}

	public BigDecimal getR62_FIG_BAL_BWP2() {
		return R62_FIG_BAL_BWP2;
	}

	public void setR62_FIG_BAL_BWP2(BigDecimal r62_FIG_BAL_BWP2) {
		R62_FIG_BAL_BWP2 = r62_FIG_BAL_BWP2;
	}

	public BigDecimal getR62_AMT_ADJ_BWP1() {
		return R62_AMT_ADJ_BWP1;
	}

	public void setR62_AMT_ADJ_BWP1(BigDecimal r62_AMT_ADJ_BWP1) {
		R62_AMT_ADJ_BWP1 = r62_AMT_ADJ_BWP1;
	}

	public BigDecimal getR62_AMT_ADJ_BWP2() {
		return R62_AMT_ADJ_BWP2;
	}

	public void setR62_AMT_ADJ_BWP2(BigDecimal r62_AMT_ADJ_BWP2) {
		R62_AMT_ADJ_BWP2 = r62_AMT_ADJ_BWP2;
	}

	public BigDecimal getR62_NET_AMT_BWP1() {
		return R62_NET_AMT_BWP1;
	}

	public void setR62_NET_AMT_BWP1(BigDecimal r62_NET_AMT_BWP1) {
		R62_NET_AMT_BWP1 = r62_NET_AMT_BWP1;
	}

	public BigDecimal getR62_NET_AMT_BWP2() {
		return R62_NET_AMT_BWP2;
	}

	public void setR62_NET_AMT_BWP2(BigDecimal r62_NET_AMT_BWP2) {
		R62_NET_AMT_BWP2 = r62_NET_AMT_BWP2;
	}

	public BigDecimal getR62_BAL_SUB_BWP1() {
		return R62_BAL_SUB_BWP1;
	}

	public void setR62_BAL_SUB_BWP1(BigDecimal r62_BAL_SUB_BWP1) {
		R62_BAL_SUB_BWP1 = r62_BAL_SUB_BWP1;
	}

	public BigDecimal getR62_BAL_SUB_BWP2() {
		return R62_BAL_SUB_BWP2;
	}

	public void setR62_BAL_SUB_BWP2(BigDecimal r62_BAL_SUB_BWP2) {
		R62_BAL_SUB_BWP2 = r62_BAL_SUB_BWP2;
	}

	public BigDecimal getR62_BAL_ACT_SUB_BWP1() {
		return R62_BAL_ACT_SUB_BWP1;
	}

	public void setR62_BAL_ACT_SUB_BWP1(BigDecimal r62_BAL_ACT_SUB_BWP1) {
		R62_BAL_ACT_SUB_BWP1 = r62_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR62_BAL_ACT_SUB_BWP2() {
		return R62_BAL_ACT_SUB_BWP2;
	}

	public void setR62_BAL_ACT_SUB_BWP2(BigDecimal r62_BAL_ACT_SUB_BWP2) {
		R62_BAL_ACT_SUB_BWP2 = r62_BAL_ACT_SUB_BWP2;
	}

	public String getR63_PRODUCT() {
		return R63_PRODUCT;
	}

	public void setR63_PRODUCT(String r63_PRODUCT) {
		R63_PRODUCT = r63_PRODUCT;
	}

	public BigDecimal getR63_FIG_BAL_BWP1() {
		return R63_FIG_BAL_BWP1;
	}

	public void setR63_FIG_BAL_BWP1(BigDecimal r63_FIG_BAL_BWP1) {
		R63_FIG_BAL_BWP1 = r63_FIG_BAL_BWP1;
	}

	public BigDecimal getR63_FIG_BAL_BWP2() {
		return R63_FIG_BAL_BWP2;
	}

	public void setR63_FIG_BAL_BWP2(BigDecimal r63_FIG_BAL_BWP2) {
		R63_FIG_BAL_BWP2 = r63_FIG_BAL_BWP2;
	}

	public BigDecimal getR63_AMT_ADJ_BWP1() {
		return R63_AMT_ADJ_BWP1;
	}

	public void setR63_AMT_ADJ_BWP1(BigDecimal r63_AMT_ADJ_BWP1) {
		R63_AMT_ADJ_BWP1 = r63_AMT_ADJ_BWP1;
	}

	public BigDecimal getR63_AMT_ADJ_BWP2() {
		return R63_AMT_ADJ_BWP2;
	}

	public void setR63_AMT_ADJ_BWP2(BigDecimal r63_AMT_ADJ_BWP2) {
		R63_AMT_ADJ_BWP2 = r63_AMT_ADJ_BWP2;
	}

	public BigDecimal getR63_NET_AMT_BWP1() {
		return R63_NET_AMT_BWP1;
	}

	public void setR63_NET_AMT_BWP1(BigDecimal r63_NET_AMT_BWP1) {
		R63_NET_AMT_BWP1 = r63_NET_AMT_BWP1;
	}

	public BigDecimal getR63_NET_AMT_BWP2() {
		return R63_NET_AMT_BWP2;
	}

	public void setR63_NET_AMT_BWP2(BigDecimal r63_NET_AMT_BWP2) {
		R63_NET_AMT_BWP2 = r63_NET_AMT_BWP2;
	}

	public BigDecimal getR63_BAL_SUB_BWP1() {
		return R63_BAL_SUB_BWP1;
	}

	public void setR63_BAL_SUB_BWP1(BigDecimal r63_BAL_SUB_BWP1) {
		R63_BAL_SUB_BWP1 = r63_BAL_SUB_BWP1;
	}

	public BigDecimal getR63_BAL_SUB_BWP2() {
		return R63_BAL_SUB_BWP2;
	}

	public void setR63_BAL_SUB_BWP2(BigDecimal r63_BAL_SUB_BWP2) {
		R63_BAL_SUB_BWP2 = r63_BAL_SUB_BWP2;
	}

	public BigDecimal getR63_BAL_ACT_SUB_BWP1() {
		return R63_BAL_ACT_SUB_BWP1;
	}

	public void setR63_BAL_ACT_SUB_BWP1(BigDecimal r63_BAL_ACT_SUB_BWP1) {
		R63_BAL_ACT_SUB_BWP1 = r63_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR63_BAL_ACT_SUB_BWP2() {
		return R63_BAL_ACT_SUB_BWP2;
	}

	public void setR63_BAL_ACT_SUB_BWP2(BigDecimal r63_BAL_ACT_SUB_BWP2) {
		R63_BAL_ACT_SUB_BWP2 = r63_BAL_ACT_SUB_BWP2;
	}

	public String getR64_PRODUCT() {
		return R64_PRODUCT;
	}

	public void setR64_PRODUCT(String r64_PRODUCT) {
		R64_PRODUCT = r64_PRODUCT;
	}

	public BigDecimal getR64_FIG_BAL_BWP1() {
		return R64_FIG_BAL_BWP1;
	}

	public void setR64_FIG_BAL_BWP1(BigDecimal r64_FIG_BAL_BWP1) {
		R64_FIG_BAL_BWP1 = r64_FIG_BAL_BWP1;
	}

	public BigDecimal getR64_FIG_BAL_BWP2() {
		return R64_FIG_BAL_BWP2;
	}

	public void setR64_FIG_BAL_BWP2(BigDecimal r64_FIG_BAL_BWP2) {
		R64_FIG_BAL_BWP2 = r64_FIG_BAL_BWP2;
	}

	public BigDecimal getR64_AMT_ADJ_BWP1() {
		return R64_AMT_ADJ_BWP1;
	}

	public void setR64_AMT_ADJ_BWP1(BigDecimal r64_AMT_ADJ_BWP1) {
		R64_AMT_ADJ_BWP1 = r64_AMT_ADJ_BWP1;
	}

	public BigDecimal getR64_AMT_ADJ_BWP2() {
		return R64_AMT_ADJ_BWP2;
	}

	public void setR64_AMT_ADJ_BWP2(BigDecimal r64_AMT_ADJ_BWP2) {
		R64_AMT_ADJ_BWP2 = r64_AMT_ADJ_BWP2;
	}

	public BigDecimal getR64_NET_AMT_BWP1() {
		return R64_NET_AMT_BWP1;
	}

	public void setR64_NET_AMT_BWP1(BigDecimal r64_NET_AMT_BWP1) {
		R64_NET_AMT_BWP1 = r64_NET_AMT_BWP1;
	}

	public BigDecimal getR64_NET_AMT_BWP2() {
		return R64_NET_AMT_BWP2;
	}

	public void setR64_NET_AMT_BWP2(BigDecimal r64_NET_AMT_BWP2) {
		R64_NET_AMT_BWP2 = r64_NET_AMT_BWP2;
	}

	public BigDecimal getR64_BAL_SUB_BWP1() {
		return R64_BAL_SUB_BWP1;
	}

	public void setR64_BAL_SUB_BWP1(BigDecimal r64_BAL_SUB_BWP1) {
		R64_BAL_SUB_BWP1 = r64_BAL_SUB_BWP1;
	}

	public BigDecimal getR64_BAL_SUB_BWP2() {
		return R64_BAL_SUB_BWP2;
	}

	public void setR64_BAL_SUB_BWP2(BigDecimal r64_BAL_SUB_BWP2) {
		R64_BAL_SUB_BWP2 = r64_BAL_SUB_BWP2;
	}

	public BigDecimal getR64_BAL_ACT_SUB_BWP1() {
		return R64_BAL_ACT_SUB_BWP1;
	}

	public void setR64_BAL_ACT_SUB_BWP1(BigDecimal r64_BAL_ACT_SUB_BWP1) {
		R64_BAL_ACT_SUB_BWP1 = r64_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR64_BAL_ACT_SUB_BWP2() {
		return R64_BAL_ACT_SUB_BWP2;
	}

	public void setR64_BAL_ACT_SUB_BWP2(BigDecimal r64_BAL_ACT_SUB_BWP2) {
		R64_BAL_ACT_SUB_BWP2 = r64_BAL_ACT_SUB_BWP2;
	}

	public String getR65_PRODUCT() {
		return R65_PRODUCT;
	}

	public void setR65_PRODUCT(String r65_PRODUCT) {
		R65_PRODUCT = r65_PRODUCT;
	}

	public BigDecimal getR65_FIG_BAL_BWP1() {
		return R65_FIG_BAL_BWP1;
	}

	public void setR65_FIG_BAL_BWP1(BigDecimal r65_FIG_BAL_BWP1) {
		R65_FIG_BAL_BWP1 = r65_FIG_BAL_BWP1;
	}

	public BigDecimal getR65_FIG_BAL_BWP2() {
		return R65_FIG_BAL_BWP2;
	}

	public void setR65_FIG_BAL_BWP2(BigDecimal r65_FIG_BAL_BWP2) {
		R65_FIG_BAL_BWP2 = r65_FIG_BAL_BWP2;
	}

	public BigDecimal getR65_AMT_ADJ_BWP1() {
		return R65_AMT_ADJ_BWP1;
	}

	public void setR65_AMT_ADJ_BWP1(BigDecimal r65_AMT_ADJ_BWP1) {
		R65_AMT_ADJ_BWP1 = r65_AMT_ADJ_BWP1;
	}

	public BigDecimal getR65_AMT_ADJ_BWP2() {
		return R65_AMT_ADJ_BWP2;
	}

	public void setR65_AMT_ADJ_BWP2(BigDecimal r65_AMT_ADJ_BWP2) {
		R65_AMT_ADJ_BWP2 = r65_AMT_ADJ_BWP2;
	}

	public BigDecimal getR65_NET_AMT_BWP1() {
		return R65_NET_AMT_BWP1;
	}

	public void setR65_NET_AMT_BWP1(BigDecimal r65_NET_AMT_BWP1) {
		R65_NET_AMT_BWP1 = r65_NET_AMT_BWP1;
	}

	public BigDecimal getR65_NET_AMT_BWP2() {
		return R65_NET_AMT_BWP2;
	}

	public void setR65_NET_AMT_BWP2(BigDecimal r65_NET_AMT_BWP2) {
		R65_NET_AMT_BWP2 = r65_NET_AMT_BWP2;
	}

	public BigDecimal getR65_BAL_SUB_BWP1() {
		return R65_BAL_SUB_BWP1;
	}

	public void setR65_BAL_SUB_BWP1(BigDecimal r65_BAL_SUB_BWP1) {
		R65_BAL_SUB_BWP1 = r65_BAL_SUB_BWP1;
	}

	public BigDecimal getR65_BAL_SUB_BWP2() {
		return R65_BAL_SUB_BWP2;
	}

	public void setR65_BAL_SUB_BWP2(BigDecimal r65_BAL_SUB_BWP2) {
		R65_BAL_SUB_BWP2 = r65_BAL_SUB_BWP2;
	}

	public BigDecimal getR65_BAL_ACT_SUB_BWP1() {
		return R65_BAL_ACT_SUB_BWP1;
	}

	public void setR65_BAL_ACT_SUB_BWP1(BigDecimal r65_BAL_ACT_SUB_BWP1) {
		R65_BAL_ACT_SUB_BWP1 = r65_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR65_BAL_ACT_SUB_BWP2() {
		return R65_BAL_ACT_SUB_BWP2;
	}

	public void setR65_BAL_ACT_SUB_BWP2(BigDecimal r65_BAL_ACT_SUB_BWP2) {
		R65_BAL_ACT_SUB_BWP2 = r65_BAL_ACT_SUB_BWP2;
	}

	public String getR66_PRODUCT() {
		return R66_PRODUCT;
	}

	public void setR66_PRODUCT(String r66_PRODUCT) {
		R66_PRODUCT = r66_PRODUCT;
	}

	public BigDecimal getR66_FIG_BAL_BWP1() {
		return R66_FIG_BAL_BWP1;
	}

	public void setR66_FIG_BAL_BWP1(BigDecimal r66_FIG_BAL_BWP1) {
		R66_FIG_BAL_BWP1 = r66_FIG_BAL_BWP1;
	}

	public BigDecimal getR66_FIG_BAL_BWP2() {
		return R66_FIG_BAL_BWP2;
	}

	public void setR66_FIG_BAL_BWP2(BigDecimal r66_FIG_BAL_BWP2) {
		R66_FIG_BAL_BWP2 = r66_FIG_BAL_BWP2;
	}

	public BigDecimal getR66_AMT_ADJ_BWP1() {
		return R66_AMT_ADJ_BWP1;
	}

	public void setR66_AMT_ADJ_BWP1(BigDecimal r66_AMT_ADJ_BWP1) {
		R66_AMT_ADJ_BWP1 = r66_AMT_ADJ_BWP1;
	}

	public BigDecimal getR66_AMT_ADJ_BWP2() {
		return R66_AMT_ADJ_BWP2;
	}

	public void setR66_AMT_ADJ_BWP2(BigDecimal r66_AMT_ADJ_BWP2) {
		R66_AMT_ADJ_BWP2 = r66_AMT_ADJ_BWP2;
	}

	public BigDecimal getR66_NET_AMT_BWP1() {
		return R66_NET_AMT_BWP1;
	}

	public void setR66_NET_AMT_BWP1(BigDecimal r66_NET_AMT_BWP1) {
		R66_NET_AMT_BWP1 = r66_NET_AMT_BWP1;
	}

	public BigDecimal getR66_NET_AMT_BWP2() {
		return R66_NET_AMT_BWP2;
	}

	public void setR66_NET_AMT_BWP2(BigDecimal r66_NET_AMT_BWP2) {
		R66_NET_AMT_BWP2 = r66_NET_AMT_BWP2;
	}

	public BigDecimal getR66_BAL_SUB_BWP1() {
		return R66_BAL_SUB_BWP1;
	}

	public void setR66_BAL_SUB_BWP1(BigDecimal r66_BAL_SUB_BWP1) {
		R66_BAL_SUB_BWP1 = r66_BAL_SUB_BWP1;
	}

	public BigDecimal getR66_BAL_SUB_BWP2() {
		return R66_BAL_SUB_BWP2;
	}

	public void setR66_BAL_SUB_BWP2(BigDecimal r66_BAL_SUB_BWP2) {
		R66_BAL_SUB_BWP2 = r66_BAL_SUB_BWP2;
	}

	public BigDecimal getR66_BAL_ACT_SUB_BWP1() {
		return R66_BAL_ACT_SUB_BWP1;
	}

	public void setR66_BAL_ACT_SUB_BWP1(BigDecimal r66_BAL_ACT_SUB_BWP1) {
		R66_BAL_ACT_SUB_BWP1 = r66_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR66_BAL_ACT_SUB_BWP2() {
		return R66_BAL_ACT_SUB_BWP2;
	}

	public void setR66_BAL_ACT_SUB_BWP2(BigDecimal r66_BAL_ACT_SUB_BWP2) {
		R66_BAL_ACT_SUB_BWP2 = r66_BAL_ACT_SUB_BWP2;
	}

	public String getR71_PRODUCT() {
		return R71_PRODUCT;
	}

	public void setR71_PRODUCT(String r71_PRODUCT) {
		R71_PRODUCT = r71_PRODUCT;
	}

	public BigDecimal getR71_FIG_BAL_BWP1() {
		return R71_FIG_BAL_BWP1;
	}

	public void setR71_FIG_BAL_BWP1(BigDecimal r71_FIG_BAL_BWP1) {
		R71_FIG_BAL_BWP1 = r71_FIG_BAL_BWP1;
	}

	public BigDecimal getR71_FIG_BAL_BWP2() {
		return R71_FIG_BAL_BWP2;
	}

	public void setR71_FIG_BAL_BWP2(BigDecimal r71_FIG_BAL_BWP2) {
		R71_FIG_BAL_BWP2 = r71_FIG_BAL_BWP2;
	}

	public BigDecimal getR71_AMT_ADJ_BWP1() {
		return R71_AMT_ADJ_BWP1;
	}

	public void setR71_AMT_ADJ_BWP1(BigDecimal r71_AMT_ADJ_BWP1) {
		R71_AMT_ADJ_BWP1 = r71_AMT_ADJ_BWP1;
	}

	public BigDecimal getR71_AMT_ADJ_BWP2() {
		return R71_AMT_ADJ_BWP2;
	}

	public void setR71_AMT_ADJ_BWP2(BigDecimal r71_AMT_ADJ_BWP2) {
		R71_AMT_ADJ_BWP2 = r71_AMT_ADJ_BWP2;
	}

	public BigDecimal getR71_NET_AMT_BWP1() {
		return R71_NET_AMT_BWP1;
	}

	public void setR71_NET_AMT_BWP1(BigDecimal r71_NET_AMT_BWP1) {
		R71_NET_AMT_BWP1 = r71_NET_AMT_BWP1;
	}

	public BigDecimal getR71_NET_AMT_BWP2() {
		return R71_NET_AMT_BWP2;
	}

	public void setR71_NET_AMT_BWP2(BigDecimal r71_NET_AMT_BWP2) {
		R71_NET_AMT_BWP2 = r71_NET_AMT_BWP2;
	}

	public BigDecimal getR71_BAL_SUB_BWP1() {
		return R71_BAL_SUB_BWP1;
	}

	public void setR71_BAL_SUB_BWP1(BigDecimal r71_BAL_SUB_BWP1) {
		R71_BAL_SUB_BWP1 = r71_BAL_SUB_BWP1;
	}

	public BigDecimal getR71_BAL_SUB_BWP2() {
		return R71_BAL_SUB_BWP2;
	}

	public void setR71_BAL_SUB_BWP2(BigDecimal r71_BAL_SUB_BWP2) {
		R71_BAL_SUB_BWP2 = r71_BAL_SUB_BWP2;
	}

	public BigDecimal getR71_BAL_ACT_SUB_BWP1() {
		return R71_BAL_ACT_SUB_BWP1;
	}

	public void setR71_BAL_ACT_SUB_BWP1(BigDecimal r71_BAL_ACT_SUB_BWP1) {
		R71_BAL_ACT_SUB_BWP1 = r71_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR71_BAL_ACT_SUB_BWP2() {
		return R71_BAL_ACT_SUB_BWP2;
	}

	public void setR71_BAL_ACT_SUB_BWP2(BigDecimal r71_BAL_ACT_SUB_BWP2) {
		R71_BAL_ACT_SUB_BWP2 = r71_BAL_ACT_SUB_BWP2;
	}

	public String getR72_PRODUCT() {
		return R72_PRODUCT;
	}

	public void setR72_PRODUCT(String r72_PRODUCT) {
		R72_PRODUCT = r72_PRODUCT;
	}

	public BigDecimal getR72_FIG_BAL_BWP1() {
		return R72_FIG_BAL_BWP1;
	}

	public void setR72_FIG_BAL_BWP1(BigDecimal r72_FIG_BAL_BWP1) {
		R72_FIG_BAL_BWP1 = r72_FIG_BAL_BWP1;
	}

	public BigDecimal getR72_FIG_BAL_BWP2() {
		return R72_FIG_BAL_BWP2;
	}

	public void setR72_FIG_BAL_BWP2(BigDecimal r72_FIG_BAL_BWP2) {
		R72_FIG_BAL_BWP2 = r72_FIG_BAL_BWP2;
	}

	public BigDecimal getR72_AMT_ADJ_BWP1() {
		return R72_AMT_ADJ_BWP1;
	}

	public void setR72_AMT_ADJ_BWP1(BigDecimal r72_AMT_ADJ_BWP1) {
		R72_AMT_ADJ_BWP1 = r72_AMT_ADJ_BWP1;
	}

	public BigDecimal getR72_AMT_ADJ_BWP2() {
		return R72_AMT_ADJ_BWP2;
	}

	public void setR72_AMT_ADJ_BWP2(BigDecimal r72_AMT_ADJ_BWP2) {
		R72_AMT_ADJ_BWP2 = r72_AMT_ADJ_BWP2;
	}

	public BigDecimal getR72_NET_AMT_BWP1() {
		return R72_NET_AMT_BWP1;
	}

	public void setR72_NET_AMT_BWP1(BigDecimal r72_NET_AMT_BWP1) {
		R72_NET_AMT_BWP1 = r72_NET_AMT_BWP1;
	}

	public BigDecimal getR72_NET_AMT_BWP2() {
		return R72_NET_AMT_BWP2;
	}

	public void setR72_NET_AMT_BWP2(BigDecimal r72_NET_AMT_BWP2) {
		R72_NET_AMT_BWP2 = r72_NET_AMT_BWP2;
	}

	public BigDecimal getR72_BAL_SUB_BWP1() {
		return R72_BAL_SUB_BWP1;
	}

	public void setR72_BAL_SUB_BWP1(BigDecimal r72_BAL_SUB_BWP1) {
		R72_BAL_SUB_BWP1 = r72_BAL_SUB_BWP1;
	}

	public BigDecimal getR72_BAL_SUB_BWP2() {
		return R72_BAL_SUB_BWP2;
	}

	public void setR72_BAL_SUB_BWP2(BigDecimal r72_BAL_SUB_BWP2) {
		R72_BAL_SUB_BWP2 = r72_BAL_SUB_BWP2;
	}

	public BigDecimal getR72_BAL_ACT_SUB_BWP1() {
		return R72_BAL_ACT_SUB_BWP1;
	}

	public void setR72_BAL_ACT_SUB_BWP1(BigDecimal r72_BAL_ACT_SUB_BWP1) {
		R72_BAL_ACT_SUB_BWP1 = r72_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR72_BAL_ACT_SUB_BWP2() {
		return R72_BAL_ACT_SUB_BWP2;
	}

	public void setR72_BAL_ACT_SUB_BWP2(BigDecimal r72_BAL_ACT_SUB_BWP2) {
		R72_BAL_ACT_SUB_BWP2 = r72_BAL_ACT_SUB_BWP2;
	}

	public String getR73_PRODUCT() {
		return R73_PRODUCT;
	}

	public void setR73_PRODUCT(String r73_PRODUCT) {
		R73_PRODUCT = r73_PRODUCT;
	}

	public BigDecimal getR73_FIG_BAL_BWP1() {
		return R73_FIG_BAL_BWP1;
	}

	public void setR73_FIG_BAL_BWP1(BigDecimal r73_FIG_BAL_BWP1) {
		R73_FIG_BAL_BWP1 = r73_FIG_BAL_BWP1;
	}

	public BigDecimal getR73_FIG_BAL_BWP2() {
		return R73_FIG_BAL_BWP2;
	}

	public void setR73_FIG_BAL_BWP2(BigDecimal r73_FIG_BAL_BWP2) {
		R73_FIG_BAL_BWP2 = r73_FIG_BAL_BWP2;
	}

	public BigDecimal getR73_AMT_ADJ_BWP1() {
		return R73_AMT_ADJ_BWP1;
	}

	public void setR73_AMT_ADJ_BWP1(BigDecimal r73_AMT_ADJ_BWP1) {
		R73_AMT_ADJ_BWP1 = r73_AMT_ADJ_BWP1;
	}

	public BigDecimal getR73_AMT_ADJ_BWP2() {
		return R73_AMT_ADJ_BWP2;
	}

	public void setR73_AMT_ADJ_BWP2(BigDecimal r73_AMT_ADJ_BWP2) {
		R73_AMT_ADJ_BWP2 = r73_AMT_ADJ_BWP2;
	}

	public BigDecimal getR73_NET_AMT_BWP1() {
		return R73_NET_AMT_BWP1;
	}

	public void setR73_NET_AMT_BWP1(BigDecimal r73_NET_AMT_BWP1) {
		R73_NET_AMT_BWP1 = r73_NET_AMT_BWP1;
	}

	public BigDecimal getR73_NET_AMT_BWP2() {
		return R73_NET_AMT_BWP2;
	}

	public void setR73_NET_AMT_BWP2(BigDecimal r73_NET_AMT_BWP2) {
		R73_NET_AMT_BWP2 = r73_NET_AMT_BWP2;
	}

	public BigDecimal getR73_BAL_SUB_BWP1() {
		return R73_BAL_SUB_BWP1;
	}

	public void setR73_BAL_SUB_BWP1(BigDecimal r73_BAL_SUB_BWP1) {
		R73_BAL_SUB_BWP1 = r73_BAL_SUB_BWP1;
	}

	public BigDecimal getR73_BAL_SUB_BWP2() {
		return R73_BAL_SUB_BWP2;
	}

	public void setR73_BAL_SUB_BWP2(BigDecimal r73_BAL_SUB_BWP2) {
		R73_BAL_SUB_BWP2 = r73_BAL_SUB_BWP2;
	}

	public BigDecimal getR73_BAL_ACT_SUB_BWP1() {
		return R73_BAL_ACT_SUB_BWP1;
	}

	public void setR73_BAL_ACT_SUB_BWP1(BigDecimal r73_BAL_ACT_SUB_BWP1) {
		R73_BAL_ACT_SUB_BWP1 = r73_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR73_BAL_ACT_SUB_BWP2() {
		return R73_BAL_ACT_SUB_BWP2;
	}

	public void setR73_BAL_ACT_SUB_BWP2(BigDecimal r73_BAL_ACT_SUB_BWP2) {
		R73_BAL_ACT_SUB_BWP2 = r73_BAL_ACT_SUB_BWP2;
	}

	public String getR74_PRODUCT() {
		return R74_PRODUCT;
	}

	public void setR74_PRODUCT(String r74_PRODUCT) {
		R74_PRODUCT = r74_PRODUCT;
	}

	public BigDecimal getR74_FIG_BAL_BWP1() {
		return R74_FIG_BAL_BWP1;
	}

	public void setR74_FIG_BAL_BWP1(BigDecimal r74_FIG_BAL_BWP1) {
		R74_FIG_BAL_BWP1 = r74_FIG_BAL_BWP1;
	}

	public BigDecimal getR74_FIG_BAL_BWP2() {
		return R74_FIG_BAL_BWP2;
	}

	public void setR74_FIG_BAL_BWP2(BigDecimal r74_FIG_BAL_BWP2) {
		R74_FIG_BAL_BWP2 = r74_FIG_BAL_BWP2;
	}

	public BigDecimal getR74_AMT_ADJ_BWP1() {
		return R74_AMT_ADJ_BWP1;
	}

	public void setR74_AMT_ADJ_BWP1(BigDecimal r74_AMT_ADJ_BWP1) {
		R74_AMT_ADJ_BWP1 = r74_AMT_ADJ_BWP1;
	}

	public BigDecimal getR74_AMT_ADJ_BWP2() {
		return R74_AMT_ADJ_BWP2;
	}

	public void setR74_AMT_ADJ_BWP2(BigDecimal r74_AMT_ADJ_BWP2) {
		R74_AMT_ADJ_BWP2 = r74_AMT_ADJ_BWP2;
	}

	public BigDecimal getR74_NET_AMT_BWP1() {
		return R74_NET_AMT_BWP1;
	}

	public void setR74_NET_AMT_BWP1(BigDecimal r74_NET_AMT_BWP1) {
		R74_NET_AMT_BWP1 = r74_NET_AMT_BWP1;
	}

	public BigDecimal getR74_NET_AMT_BWP2() {
		return R74_NET_AMT_BWP2;
	}

	public void setR74_NET_AMT_BWP2(BigDecimal r74_NET_AMT_BWP2) {
		R74_NET_AMT_BWP2 = r74_NET_AMT_BWP2;
	}

	public BigDecimal getR74_BAL_SUB_BWP1() {
		return R74_BAL_SUB_BWP1;
	}

	public void setR74_BAL_SUB_BWP1(BigDecimal r74_BAL_SUB_BWP1) {
		R74_BAL_SUB_BWP1 = r74_BAL_SUB_BWP1;
	}

	public BigDecimal getR74_BAL_SUB_BWP2() {
		return R74_BAL_SUB_BWP2;
	}

	public void setR74_BAL_SUB_BWP2(BigDecimal r74_BAL_SUB_BWP2) {
		R74_BAL_SUB_BWP2 = r74_BAL_SUB_BWP2;
	}

	public BigDecimal getR74_BAL_ACT_SUB_BWP1() {
		return R74_BAL_ACT_SUB_BWP1;
	}

	public void setR74_BAL_ACT_SUB_BWP1(BigDecimal r74_BAL_ACT_SUB_BWP1) {
		R74_BAL_ACT_SUB_BWP1 = r74_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR74_BAL_ACT_SUB_BWP2() {
		return R74_BAL_ACT_SUB_BWP2;
	}

	public void setR74_BAL_ACT_SUB_BWP2(BigDecimal r74_BAL_ACT_SUB_BWP2) {
		R74_BAL_ACT_SUB_BWP2 = r74_BAL_ACT_SUB_BWP2;
	}

	public String getR75_PRODUCT() {
		return R75_PRODUCT;
	}

	public void setR75_PRODUCT(String r75_PRODUCT) {
		R75_PRODUCT = r75_PRODUCT;
	}

	public BigDecimal getR75_FIG_BAL_BWP1() {
		return R75_FIG_BAL_BWP1;
	}

	public void setR75_FIG_BAL_BWP1(BigDecimal r75_FIG_BAL_BWP1) {
		R75_FIG_BAL_BWP1 = r75_FIG_BAL_BWP1;
	}

	public BigDecimal getR75_FIG_BAL_BWP2() {
		return R75_FIG_BAL_BWP2;
	}

	public void setR75_FIG_BAL_BWP2(BigDecimal r75_FIG_BAL_BWP2) {
		R75_FIG_BAL_BWP2 = r75_FIG_BAL_BWP2;
	}

	public BigDecimal getR75_AMT_ADJ_BWP1() {
		return R75_AMT_ADJ_BWP1;
	}

	public void setR75_AMT_ADJ_BWP1(BigDecimal r75_AMT_ADJ_BWP1) {
		R75_AMT_ADJ_BWP1 = r75_AMT_ADJ_BWP1;
	}

	public BigDecimal getR75_AMT_ADJ_BWP2() {
		return R75_AMT_ADJ_BWP2;
	}

	public void setR75_AMT_ADJ_BWP2(BigDecimal r75_AMT_ADJ_BWP2) {
		R75_AMT_ADJ_BWP2 = r75_AMT_ADJ_BWP2;
	}

	public BigDecimal getR75_NET_AMT_BWP1() {
		return R75_NET_AMT_BWP1;
	}

	public void setR75_NET_AMT_BWP1(BigDecimal r75_NET_AMT_BWP1) {
		R75_NET_AMT_BWP1 = r75_NET_AMT_BWP1;
	}

	public BigDecimal getR75_NET_AMT_BWP2() {
		return R75_NET_AMT_BWP2;
	}

	public void setR75_NET_AMT_BWP2(BigDecimal r75_NET_AMT_BWP2) {
		R75_NET_AMT_BWP2 = r75_NET_AMT_BWP2;
	}

	public BigDecimal getR75_BAL_SUB_BWP1() {
		return R75_BAL_SUB_BWP1;
	}

	public void setR75_BAL_SUB_BWP1(BigDecimal r75_BAL_SUB_BWP1) {
		R75_BAL_SUB_BWP1 = r75_BAL_SUB_BWP1;
	}

	public BigDecimal getR75_BAL_SUB_BWP2() {
		return R75_BAL_SUB_BWP2;
	}

	public void setR75_BAL_SUB_BWP2(BigDecimal r75_BAL_SUB_BWP2) {
		R75_BAL_SUB_BWP2 = r75_BAL_SUB_BWP2;
	}

	public BigDecimal getR75_BAL_ACT_SUB_BWP1() {
		return R75_BAL_ACT_SUB_BWP1;
	}

	public void setR75_BAL_ACT_SUB_BWP1(BigDecimal r75_BAL_ACT_SUB_BWP1) {
		R75_BAL_ACT_SUB_BWP1 = r75_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR75_BAL_ACT_SUB_BWP2() {
		return R75_BAL_ACT_SUB_BWP2;
	}

	public void setR75_BAL_ACT_SUB_BWP2(BigDecimal r75_BAL_ACT_SUB_BWP2) {
		R75_BAL_ACT_SUB_BWP2 = r75_BAL_ACT_SUB_BWP2;
	}

	public String getR76_PRODUCT() {
		return R76_PRODUCT;
	}

	public void setR76_PRODUCT(String r76_PRODUCT) {
		R76_PRODUCT = r76_PRODUCT;
	}

	public BigDecimal getR76_FIG_BAL_BWP1() {
		return R76_FIG_BAL_BWP1;
	}

	public void setR76_FIG_BAL_BWP1(BigDecimal r76_FIG_BAL_BWP1) {
		R76_FIG_BAL_BWP1 = r76_FIG_BAL_BWP1;
	}

	public BigDecimal getR76_FIG_BAL_BWP2() {
		return R76_FIG_BAL_BWP2;
	}

	public void setR76_FIG_BAL_BWP2(BigDecimal r76_FIG_BAL_BWP2) {
		R76_FIG_BAL_BWP2 = r76_FIG_BAL_BWP2;
	}

	public BigDecimal getR76_AMT_ADJ_BWP1() {
		return R76_AMT_ADJ_BWP1;
	}

	public void setR76_AMT_ADJ_BWP1(BigDecimal r76_AMT_ADJ_BWP1) {
		R76_AMT_ADJ_BWP1 = r76_AMT_ADJ_BWP1;
	}

	public BigDecimal getR76_AMT_ADJ_BWP2() {
		return R76_AMT_ADJ_BWP2;
	}

	public void setR76_AMT_ADJ_BWP2(BigDecimal r76_AMT_ADJ_BWP2) {
		R76_AMT_ADJ_BWP2 = r76_AMT_ADJ_BWP2;
	}

	public BigDecimal getR76_NET_AMT_BWP1() {
		return R76_NET_AMT_BWP1;
	}

	public void setR76_NET_AMT_BWP1(BigDecimal r76_NET_AMT_BWP1) {
		R76_NET_AMT_BWP1 = r76_NET_AMT_BWP1;
	}

	public BigDecimal getR76_NET_AMT_BWP2() {
		return R76_NET_AMT_BWP2;
	}

	public void setR76_NET_AMT_BWP2(BigDecimal r76_NET_AMT_BWP2) {
		R76_NET_AMT_BWP2 = r76_NET_AMT_BWP2;
	}

	public BigDecimal getR76_BAL_SUB_BWP1() {
		return R76_BAL_SUB_BWP1;
	}

	public void setR76_BAL_SUB_BWP1(BigDecimal r76_BAL_SUB_BWP1) {
		R76_BAL_SUB_BWP1 = r76_BAL_SUB_BWP1;
	}

	public BigDecimal getR76_BAL_SUB_BWP2() {
		return R76_BAL_SUB_BWP2;
	}

	public void setR76_BAL_SUB_BWP2(BigDecimal r76_BAL_SUB_BWP2) {
		R76_BAL_SUB_BWP2 = r76_BAL_SUB_BWP2;
	}

	public BigDecimal getR76_BAL_ACT_SUB_BWP1() {
		return R76_BAL_ACT_SUB_BWP1;
	}

	public void setR76_BAL_ACT_SUB_BWP1(BigDecimal r76_BAL_ACT_SUB_BWP1) {
		R76_BAL_ACT_SUB_BWP1 = r76_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR76_BAL_ACT_SUB_BWP2() {
		return R76_BAL_ACT_SUB_BWP2;
	}

	public void setR76_BAL_ACT_SUB_BWP2(BigDecimal r76_BAL_ACT_SUB_BWP2) {
		R76_BAL_ACT_SUB_BWP2 = r76_BAL_ACT_SUB_BWP2;
	}

	public String getR77_PRODUCT() {
		return R77_PRODUCT;
	}

	public void setR77_PRODUCT(String r77_PRODUCT) {
		R77_PRODUCT = r77_PRODUCT;
	}

	public BigDecimal getR77_FIG_BAL_BWP1() {
		return R77_FIG_BAL_BWP1;
	}

	public void setR77_FIG_BAL_BWP1(BigDecimal r77_FIG_BAL_BWP1) {
		R77_FIG_BAL_BWP1 = r77_FIG_BAL_BWP1;
	}

	public BigDecimal getR77_FIG_BAL_BWP2() {
		return R77_FIG_BAL_BWP2;
	}

	public void setR77_FIG_BAL_BWP2(BigDecimal r77_FIG_BAL_BWP2) {
		R77_FIG_BAL_BWP2 = r77_FIG_BAL_BWP2;
	}

	public BigDecimal getR77_AMT_ADJ_BWP1() {
		return R77_AMT_ADJ_BWP1;
	}

	public void setR77_AMT_ADJ_BWP1(BigDecimal r77_AMT_ADJ_BWP1) {
		R77_AMT_ADJ_BWP1 = r77_AMT_ADJ_BWP1;
	}

	public BigDecimal getR77_AMT_ADJ_BWP2() {
		return R77_AMT_ADJ_BWP2;
	}

	public void setR77_AMT_ADJ_BWP2(BigDecimal r77_AMT_ADJ_BWP2) {
		R77_AMT_ADJ_BWP2 = r77_AMT_ADJ_BWP2;
	}

	public BigDecimal getR77_NET_AMT_BWP1() {
		return R77_NET_AMT_BWP1;
	}

	public void setR77_NET_AMT_BWP1(BigDecimal r77_NET_AMT_BWP1) {
		R77_NET_AMT_BWP1 = r77_NET_AMT_BWP1;
	}

	public BigDecimal getR77_NET_AMT_BWP2() {
		return R77_NET_AMT_BWP2;
	}

	public void setR77_NET_AMT_BWP2(BigDecimal r77_NET_AMT_BWP2) {
		R77_NET_AMT_BWP2 = r77_NET_AMT_BWP2;
	}

	public BigDecimal getR77_BAL_SUB_BWP1() {
		return R77_BAL_SUB_BWP1;
	}

	public void setR77_BAL_SUB_BWP1(BigDecimal r77_BAL_SUB_BWP1) {
		R77_BAL_SUB_BWP1 = r77_BAL_SUB_BWP1;
	}

	public BigDecimal getR77_BAL_SUB_BWP2() {
		return R77_BAL_SUB_BWP2;
	}

	public void setR77_BAL_SUB_BWP2(BigDecimal r77_BAL_SUB_BWP2) {
		R77_BAL_SUB_BWP2 = r77_BAL_SUB_BWP2;
	}

	public BigDecimal getR77_BAL_ACT_SUB_BWP1() {
		return R77_BAL_ACT_SUB_BWP1;
	}

	public void setR77_BAL_ACT_SUB_BWP1(BigDecimal r77_BAL_ACT_SUB_BWP1) {
		R77_BAL_ACT_SUB_BWP1 = r77_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR77_BAL_ACT_SUB_BWP2() {
		return R77_BAL_ACT_SUB_BWP2;
	}

	public void setR77_BAL_ACT_SUB_BWP2(BigDecimal r77_BAL_ACT_SUB_BWP2) {
		R77_BAL_ACT_SUB_BWP2 = r77_BAL_ACT_SUB_BWP2;
	}

	public String getR78_PRODUCT() {
		return R78_PRODUCT;
	}

	public void setR78_PRODUCT(String r78_PRODUCT) {
		R78_PRODUCT = r78_PRODUCT;
	}

	public BigDecimal getR78_FIG_BAL_BWP1() {
		return R78_FIG_BAL_BWP1;
	}

	public void setR78_FIG_BAL_BWP1(BigDecimal r78_FIG_BAL_BWP1) {
		R78_FIG_BAL_BWP1 = r78_FIG_BAL_BWP1;
	}

	public BigDecimal getR78_FIG_BAL_BWP2() {
		return R78_FIG_BAL_BWP2;
	}

	public void setR78_FIG_BAL_BWP2(BigDecimal r78_FIG_BAL_BWP2) {
		R78_FIG_BAL_BWP2 = r78_FIG_BAL_BWP2;
	}

	public BigDecimal getR78_AMT_ADJ_BWP1() {
		return R78_AMT_ADJ_BWP1;
	}

	public void setR78_AMT_ADJ_BWP1(BigDecimal r78_AMT_ADJ_BWP1) {
		R78_AMT_ADJ_BWP1 = r78_AMT_ADJ_BWP1;
	}

	public BigDecimal getR78_AMT_ADJ_BWP2() {
		return R78_AMT_ADJ_BWP2;
	}

	public void setR78_AMT_ADJ_BWP2(BigDecimal r78_AMT_ADJ_BWP2) {
		R78_AMT_ADJ_BWP2 = r78_AMT_ADJ_BWP2;
	}

	public BigDecimal getR78_NET_AMT_BWP1() {
		return R78_NET_AMT_BWP1;
	}

	public void setR78_NET_AMT_BWP1(BigDecimal r78_NET_AMT_BWP1) {
		R78_NET_AMT_BWP1 = r78_NET_AMT_BWP1;
	}

	public BigDecimal getR78_NET_AMT_BWP2() {
		return R78_NET_AMT_BWP2;
	}

	public void setR78_NET_AMT_BWP2(BigDecimal r78_NET_AMT_BWP2) {
		R78_NET_AMT_BWP2 = r78_NET_AMT_BWP2;
	}

	public BigDecimal getR78_BAL_SUB_BWP1() {
		return R78_BAL_SUB_BWP1;
	}

	public void setR78_BAL_SUB_BWP1(BigDecimal r78_BAL_SUB_BWP1) {
		R78_BAL_SUB_BWP1 = r78_BAL_SUB_BWP1;
	}

	public BigDecimal getR78_BAL_SUB_BWP2() {
		return R78_BAL_SUB_BWP2;
	}

	public void setR78_BAL_SUB_BWP2(BigDecimal r78_BAL_SUB_BWP2) {
		R78_BAL_SUB_BWP2 = r78_BAL_SUB_BWP2;
	}

	public BigDecimal getR78_BAL_ACT_SUB_BWP1() {
		return R78_BAL_ACT_SUB_BWP1;
	}

	public void setR78_BAL_ACT_SUB_BWP1(BigDecimal r78_BAL_ACT_SUB_BWP1) {
		R78_BAL_ACT_SUB_BWP1 = r78_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR78_BAL_ACT_SUB_BWP2() {
		return R78_BAL_ACT_SUB_BWP2;
	}

	public void setR78_BAL_ACT_SUB_BWP2(BigDecimal r78_BAL_ACT_SUB_BWP2) {
		R78_BAL_ACT_SUB_BWP2 = r78_BAL_ACT_SUB_BWP2;
	}

	public String getR79_PRODUCT() {
		return R79_PRODUCT;
	}

	public void setR79_PRODUCT(String r79_PRODUCT) {
		R79_PRODUCT = r79_PRODUCT;
	}

	public BigDecimal getR79_FIG_BAL_BWP1() {
		return R79_FIG_BAL_BWP1;
	}

	public void setR79_FIG_BAL_BWP1(BigDecimal r79_FIG_BAL_BWP1) {
		R79_FIG_BAL_BWP1 = r79_FIG_BAL_BWP1;
	}

	public BigDecimal getR79_FIG_BAL_BWP2() {
		return R79_FIG_BAL_BWP2;
	}

	public void setR79_FIG_BAL_BWP2(BigDecimal r79_FIG_BAL_BWP2) {
		R79_FIG_BAL_BWP2 = r79_FIG_BAL_BWP2;
	}

	public BigDecimal getR79_AMT_ADJ_BWP1() {
		return R79_AMT_ADJ_BWP1;
	}

	public void setR79_AMT_ADJ_BWP1(BigDecimal r79_AMT_ADJ_BWP1) {
		R79_AMT_ADJ_BWP1 = r79_AMT_ADJ_BWP1;
	}

	public BigDecimal getR79_AMT_ADJ_BWP2() {
		return R79_AMT_ADJ_BWP2;
	}

	public void setR79_AMT_ADJ_BWP2(BigDecimal r79_AMT_ADJ_BWP2) {
		R79_AMT_ADJ_BWP2 = r79_AMT_ADJ_BWP2;
	}

	public BigDecimal getR79_NET_AMT_BWP1() {
		return R79_NET_AMT_BWP1;
	}

	public void setR79_NET_AMT_BWP1(BigDecimal r79_NET_AMT_BWP1) {
		R79_NET_AMT_BWP1 = r79_NET_AMT_BWP1;
	}

	public BigDecimal getR79_NET_AMT_BWP2() {
		return R79_NET_AMT_BWP2;
	}

	public void setR79_NET_AMT_BWP2(BigDecimal r79_NET_AMT_BWP2) {
		R79_NET_AMT_BWP2 = r79_NET_AMT_BWP2;
	}

	public BigDecimal getR79_BAL_SUB_BWP1() {
		return R79_BAL_SUB_BWP1;
	}

	public void setR79_BAL_SUB_BWP1(BigDecimal r79_BAL_SUB_BWP1) {
		R79_BAL_SUB_BWP1 = r79_BAL_SUB_BWP1;
	}

	public BigDecimal getR79_BAL_SUB_BWP2() {
		return R79_BAL_SUB_BWP2;
	}

	public void setR79_BAL_SUB_BWP2(BigDecimal r79_BAL_SUB_BWP2) {
		R79_BAL_SUB_BWP2 = r79_BAL_SUB_BWP2;
	}

	public BigDecimal getR79_BAL_ACT_SUB_BWP1() {
		return R79_BAL_ACT_SUB_BWP1;
	}

	public void setR79_BAL_ACT_SUB_BWP1(BigDecimal r79_BAL_ACT_SUB_BWP1) {
		R79_BAL_ACT_SUB_BWP1 = r79_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR79_BAL_ACT_SUB_BWP2() {
		return R79_BAL_ACT_SUB_BWP2;
	}

	public void setR79_BAL_ACT_SUB_BWP2(BigDecimal r79_BAL_ACT_SUB_BWP2) {
		R79_BAL_ACT_SUB_BWP2 = r79_BAL_ACT_SUB_BWP2;
	}

	public String getR80_PRODUCT() {
		return R80_PRODUCT;
	}

	public void setR80_PRODUCT(String r80_PRODUCT) {
		R80_PRODUCT = r80_PRODUCT;
	}

	public BigDecimal getR80_FIG_BAL_BWP1() {
		return R80_FIG_BAL_BWP1;
	}

	public void setR80_FIG_BAL_BWP1(BigDecimal r80_FIG_BAL_BWP1) {
		R80_FIG_BAL_BWP1 = r80_FIG_BAL_BWP1;
	}

	public BigDecimal getR80_FIG_BAL_BWP2() {
		return R80_FIG_BAL_BWP2;
	}

	public void setR80_FIG_BAL_BWP2(BigDecimal r80_FIG_BAL_BWP2) {
		R80_FIG_BAL_BWP2 = r80_FIG_BAL_BWP2;
	}

	public BigDecimal getR80_AMT_ADJ_BWP1() {
		return R80_AMT_ADJ_BWP1;
	}

	public void setR80_AMT_ADJ_BWP1(BigDecimal r80_AMT_ADJ_BWP1) {
		R80_AMT_ADJ_BWP1 = r80_AMT_ADJ_BWP1;
	}

	public BigDecimal getR80_AMT_ADJ_BWP2() {
		return R80_AMT_ADJ_BWP2;
	}

	public void setR80_AMT_ADJ_BWP2(BigDecimal r80_AMT_ADJ_BWP2) {
		R80_AMT_ADJ_BWP2 = r80_AMT_ADJ_BWP2;
	}

	public BigDecimal getR80_NET_AMT_BWP1() {
		return R80_NET_AMT_BWP1;
	}

	public void setR80_NET_AMT_BWP1(BigDecimal r80_NET_AMT_BWP1) {
		R80_NET_AMT_BWP1 = r80_NET_AMT_BWP1;
	}

	public BigDecimal getR80_NET_AMT_BWP2() {
		return R80_NET_AMT_BWP2;
	}

	public void setR80_NET_AMT_BWP2(BigDecimal r80_NET_AMT_BWP2) {
		R80_NET_AMT_BWP2 = r80_NET_AMT_BWP2;
	}

	public BigDecimal getR80_BAL_SUB_BWP1() {
		return R80_BAL_SUB_BWP1;
	}

	public void setR80_BAL_SUB_BWP1(BigDecimal r80_BAL_SUB_BWP1) {
		R80_BAL_SUB_BWP1 = r80_BAL_SUB_BWP1;
	}

	public BigDecimal getR80_BAL_SUB_BWP2() {
		return R80_BAL_SUB_BWP2;
	}

	public void setR80_BAL_SUB_BWP2(BigDecimal r80_BAL_SUB_BWP2) {
		R80_BAL_SUB_BWP2 = r80_BAL_SUB_BWP2;
	}

	public BigDecimal getR80_BAL_ACT_SUB_BWP1() {
		return R80_BAL_ACT_SUB_BWP1;
	}

	public void setR80_BAL_ACT_SUB_BWP1(BigDecimal r80_BAL_ACT_SUB_BWP1) {
		R80_BAL_ACT_SUB_BWP1 = r80_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR80_BAL_ACT_SUB_BWP2() {
		return R80_BAL_ACT_SUB_BWP2;
	}

	public void setR80_BAL_ACT_SUB_BWP2(BigDecimal r80_BAL_ACT_SUB_BWP2) {
		R80_BAL_ACT_SUB_BWP2 = r80_BAL_ACT_SUB_BWP2;
	}

	public String getR81_PRODUCT() {
		return R81_PRODUCT;
	}

	public void setR81_PRODUCT(String r81_PRODUCT) {
		R81_PRODUCT = r81_PRODUCT;
	}

	public BigDecimal getR81_FIG_BAL_BWP1() {
		return R81_FIG_BAL_BWP1;
	}

	public void setR81_FIG_BAL_BWP1(BigDecimal r81_FIG_BAL_BWP1) {
		R81_FIG_BAL_BWP1 = r81_FIG_BAL_BWP1;
	}

	public BigDecimal getR81_FIG_BAL_BWP2() {
		return R81_FIG_BAL_BWP2;
	}

	public void setR81_FIG_BAL_BWP2(BigDecimal r81_FIG_BAL_BWP2) {
		R81_FIG_BAL_BWP2 = r81_FIG_BAL_BWP2;
	}

	public BigDecimal getR81_AMT_ADJ_BWP1() {
		return R81_AMT_ADJ_BWP1;
	}

	public void setR81_AMT_ADJ_BWP1(BigDecimal r81_AMT_ADJ_BWP1) {
		R81_AMT_ADJ_BWP1 = r81_AMT_ADJ_BWP1;
	}

	public BigDecimal getR81_AMT_ADJ_BWP2() {
		return R81_AMT_ADJ_BWP2;
	}

	public void setR81_AMT_ADJ_BWP2(BigDecimal r81_AMT_ADJ_BWP2) {
		R81_AMT_ADJ_BWP2 = r81_AMT_ADJ_BWP2;
	}

	public BigDecimal getR81_NET_AMT_BWP1() {
		return R81_NET_AMT_BWP1;
	}

	public void setR81_NET_AMT_BWP1(BigDecimal r81_NET_AMT_BWP1) {
		R81_NET_AMT_BWP1 = r81_NET_AMT_BWP1;
	}

	public BigDecimal getR81_NET_AMT_BWP2() {
		return R81_NET_AMT_BWP2;
	}

	public void setR81_NET_AMT_BWP2(BigDecimal r81_NET_AMT_BWP2) {
		R81_NET_AMT_BWP2 = r81_NET_AMT_BWP2;
	}

	public BigDecimal getR81_BAL_SUB_BWP1() {
		return R81_BAL_SUB_BWP1;
	}

	public void setR81_BAL_SUB_BWP1(BigDecimal r81_BAL_SUB_BWP1) {
		R81_BAL_SUB_BWP1 = r81_BAL_SUB_BWP1;
	}

	public BigDecimal getR81_BAL_SUB_BWP2() {
		return R81_BAL_SUB_BWP2;
	}

	public void setR81_BAL_SUB_BWP2(BigDecimal r81_BAL_SUB_BWP2) {
		R81_BAL_SUB_BWP2 = r81_BAL_SUB_BWP2;
	}

	public BigDecimal getR81_BAL_ACT_SUB_BWP1() {
		return R81_BAL_ACT_SUB_BWP1;
	}

	public void setR81_BAL_ACT_SUB_BWP1(BigDecimal r81_BAL_ACT_SUB_BWP1) {
		R81_BAL_ACT_SUB_BWP1 = r81_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR81_BAL_ACT_SUB_BWP2() {
		return R81_BAL_ACT_SUB_BWP2;
	}

	public void setR81_BAL_ACT_SUB_BWP2(BigDecimal r81_BAL_ACT_SUB_BWP2) {
		R81_BAL_ACT_SUB_BWP2 = r81_BAL_ACT_SUB_BWP2;
	}

	public String getR82_PRODUCT() {
		return R82_PRODUCT;
	}

	public void setR82_PRODUCT(String r82_PRODUCT) {
		R82_PRODUCT = r82_PRODUCT;
	}

	public BigDecimal getR82_FIG_BAL_BWP1() {
		return R82_FIG_BAL_BWP1;
	}

	public void setR82_FIG_BAL_BWP1(BigDecimal r82_FIG_BAL_BWP1) {
		R82_FIG_BAL_BWP1 = r82_FIG_BAL_BWP1;
	}

	public BigDecimal getR82_FIG_BAL_BWP2() {
		return R82_FIG_BAL_BWP2;
	}

	public void setR82_FIG_BAL_BWP2(BigDecimal r82_FIG_BAL_BWP2) {
		R82_FIG_BAL_BWP2 = r82_FIG_BAL_BWP2;
	}

	public BigDecimal getR82_AMT_ADJ_BWP1() {
		return R82_AMT_ADJ_BWP1;
	}

	public void setR82_AMT_ADJ_BWP1(BigDecimal r82_AMT_ADJ_BWP1) {
		R82_AMT_ADJ_BWP1 = r82_AMT_ADJ_BWP1;
	}

	public BigDecimal getR82_AMT_ADJ_BWP2() {
		return R82_AMT_ADJ_BWP2;
	}

	public void setR82_AMT_ADJ_BWP2(BigDecimal r82_AMT_ADJ_BWP2) {
		R82_AMT_ADJ_BWP2 = r82_AMT_ADJ_BWP2;
	}

	public BigDecimal getR82_NET_AMT_BWP1() {
		return R82_NET_AMT_BWP1;
	}

	public void setR82_NET_AMT_BWP1(BigDecimal r82_NET_AMT_BWP1) {
		R82_NET_AMT_BWP1 = r82_NET_AMT_BWP1;
	}

	public BigDecimal getR82_NET_AMT_BWP2() {
		return R82_NET_AMT_BWP2;
	}

	public void setR82_NET_AMT_BWP2(BigDecimal r82_NET_AMT_BWP2) {
		R82_NET_AMT_BWP2 = r82_NET_AMT_BWP2;
	}

	public BigDecimal getR82_BAL_SUB_BWP1() {
		return R82_BAL_SUB_BWP1;
	}

	public void setR82_BAL_SUB_BWP1(BigDecimal r82_BAL_SUB_BWP1) {
		R82_BAL_SUB_BWP1 = r82_BAL_SUB_BWP1;
	}

	public BigDecimal getR82_BAL_SUB_BWP2() {
		return R82_BAL_SUB_BWP2;
	}

	public void setR82_BAL_SUB_BWP2(BigDecimal r82_BAL_SUB_BWP2) {
		R82_BAL_SUB_BWP2 = r82_BAL_SUB_BWP2;
	}

	public BigDecimal getR82_BAL_ACT_SUB_BWP1() {
		return R82_BAL_ACT_SUB_BWP1;
	}

	public void setR82_BAL_ACT_SUB_BWP1(BigDecimal r82_BAL_ACT_SUB_BWP1) {
		R82_BAL_ACT_SUB_BWP1 = r82_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR82_BAL_ACT_SUB_BWP2() {
		return R82_BAL_ACT_SUB_BWP2;
	}

	public void setR82_BAL_ACT_SUB_BWP2(BigDecimal r82_BAL_ACT_SUB_BWP2) {
		R82_BAL_ACT_SUB_BWP2 = r82_BAL_ACT_SUB_BWP2;
	}

	public String getR83_PRODUCT() {
		return R83_PRODUCT;
	}

	public void setR83_PRODUCT(String r83_PRODUCT) {
		R83_PRODUCT = r83_PRODUCT;
	}

	public BigDecimal getR83_FIG_BAL_BWP1() {
		return R83_FIG_BAL_BWP1;
	}

	public void setR83_FIG_BAL_BWP1(BigDecimal r83_FIG_BAL_BWP1) {
		R83_FIG_BAL_BWP1 = r83_FIG_BAL_BWP1;
	}

	public BigDecimal getR83_FIG_BAL_BWP2() {
		return R83_FIG_BAL_BWP2;
	}

	public void setR83_FIG_BAL_BWP2(BigDecimal r83_FIG_BAL_BWP2) {
		R83_FIG_BAL_BWP2 = r83_FIG_BAL_BWP2;
	}

	public BigDecimal getR83_AMT_ADJ_BWP1() {
		return R83_AMT_ADJ_BWP1;
	}

	public void setR83_AMT_ADJ_BWP1(BigDecimal r83_AMT_ADJ_BWP1) {
		R83_AMT_ADJ_BWP1 = r83_AMT_ADJ_BWP1;
	}

	public BigDecimal getR83_AMT_ADJ_BWP2() {
		return R83_AMT_ADJ_BWP2;
	}

	public void setR83_AMT_ADJ_BWP2(BigDecimal r83_AMT_ADJ_BWP2) {
		R83_AMT_ADJ_BWP2 = r83_AMT_ADJ_BWP2;
	}

	public BigDecimal getR83_NET_AMT_BWP1() {
		return R83_NET_AMT_BWP1;
	}

	public void setR83_NET_AMT_BWP1(BigDecimal r83_NET_AMT_BWP1) {
		R83_NET_AMT_BWP1 = r83_NET_AMT_BWP1;
	}

	public BigDecimal getR83_NET_AMT_BWP2() {
		return R83_NET_AMT_BWP2;
	}

	public void setR83_NET_AMT_BWP2(BigDecimal r83_NET_AMT_BWP2) {
		R83_NET_AMT_BWP2 = r83_NET_AMT_BWP2;
	}

	public BigDecimal getR83_BAL_SUB_BWP1() {
		return R83_BAL_SUB_BWP1;
	}

	public void setR83_BAL_SUB_BWP1(BigDecimal r83_BAL_SUB_BWP1) {
		R83_BAL_SUB_BWP1 = r83_BAL_SUB_BWP1;
	}

	public BigDecimal getR83_BAL_SUB_BWP2() {
		return R83_BAL_SUB_BWP2;
	}

	public void setR83_BAL_SUB_BWP2(BigDecimal r83_BAL_SUB_BWP2) {
		R83_BAL_SUB_BWP2 = r83_BAL_SUB_BWP2;
	}

	public BigDecimal getR83_BAL_ACT_SUB_BWP1() {
		return R83_BAL_ACT_SUB_BWP1;
	}

	public void setR83_BAL_ACT_SUB_BWP1(BigDecimal r83_BAL_ACT_SUB_BWP1) {
		R83_BAL_ACT_SUB_BWP1 = r83_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR83_BAL_ACT_SUB_BWP2() {
		return R83_BAL_ACT_SUB_BWP2;
	}

	public void setR83_BAL_ACT_SUB_BWP2(BigDecimal r83_BAL_ACT_SUB_BWP2) {
		R83_BAL_ACT_SUB_BWP2 = r83_BAL_ACT_SUB_BWP2;
	}

	public String getR84_PRODUCT() {
		return R84_PRODUCT;
	}

	public void setR84_PRODUCT(String r84_PRODUCT) {
		R84_PRODUCT = r84_PRODUCT;
	}

	public BigDecimal getR84_FIG_BAL_BWP1() {
		return R84_FIG_BAL_BWP1;
	}

	public void setR84_FIG_BAL_BWP1(BigDecimal r84_FIG_BAL_BWP1) {
		R84_FIG_BAL_BWP1 = r84_FIG_BAL_BWP1;
	}

	public BigDecimal getR84_FIG_BAL_BWP2() {
		return R84_FIG_BAL_BWP2;
	}

	public void setR84_FIG_BAL_BWP2(BigDecimal r84_FIG_BAL_BWP2) {
		R84_FIG_BAL_BWP2 = r84_FIG_BAL_BWP2;
	}

	public BigDecimal getR84_AMT_ADJ_BWP1() {
		return R84_AMT_ADJ_BWP1;
	}

	public void setR84_AMT_ADJ_BWP1(BigDecimal r84_AMT_ADJ_BWP1) {
		R84_AMT_ADJ_BWP1 = r84_AMT_ADJ_BWP1;
	}

	public BigDecimal getR84_AMT_ADJ_BWP2() {
		return R84_AMT_ADJ_BWP2;
	}

	public void setR84_AMT_ADJ_BWP2(BigDecimal r84_AMT_ADJ_BWP2) {
		R84_AMT_ADJ_BWP2 = r84_AMT_ADJ_BWP2;
	}

	public BigDecimal getR84_NET_AMT_BWP1() {
		return R84_NET_AMT_BWP1;
	}

	public void setR84_NET_AMT_BWP1(BigDecimal r84_NET_AMT_BWP1) {
		R84_NET_AMT_BWP1 = r84_NET_AMT_BWP1;
	}

	public BigDecimal getR84_NET_AMT_BWP2() {
		return R84_NET_AMT_BWP2;
	}

	public void setR84_NET_AMT_BWP2(BigDecimal r84_NET_AMT_BWP2) {
		R84_NET_AMT_BWP2 = r84_NET_AMT_BWP2;
	}

	public BigDecimal getR84_BAL_SUB_BWP1() {
		return R84_BAL_SUB_BWP1;
	}

	public void setR84_BAL_SUB_BWP1(BigDecimal r84_BAL_SUB_BWP1) {
		R84_BAL_SUB_BWP1 = r84_BAL_SUB_BWP1;
	}

	public BigDecimal getR84_BAL_SUB_BWP2() {
		return R84_BAL_SUB_BWP2;
	}

	public void setR84_BAL_SUB_BWP2(BigDecimal r84_BAL_SUB_BWP2) {
		R84_BAL_SUB_BWP2 = r84_BAL_SUB_BWP2;
	}

	public BigDecimal getR84_BAL_ACT_SUB_BWP1() {
		return R84_BAL_ACT_SUB_BWP1;
	}

	public void setR84_BAL_ACT_SUB_BWP1(BigDecimal r84_BAL_ACT_SUB_BWP1) {
		R84_BAL_ACT_SUB_BWP1 = r84_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR84_BAL_ACT_SUB_BWP2() {
		return R84_BAL_ACT_SUB_BWP2;
	}

	public void setR84_BAL_ACT_SUB_BWP2(BigDecimal r84_BAL_ACT_SUB_BWP2) {
		R84_BAL_ACT_SUB_BWP2 = r84_BAL_ACT_SUB_BWP2;
	}

	public String getR87_PRODUCT() {
		return R87_PRODUCT;
	}

	public void setR87_PRODUCT(String r87_PRODUCT) {
		R87_PRODUCT = r87_PRODUCT;
	}

	public BigDecimal getR87_FIG_BAL_BWP1() {
		return R87_FIG_BAL_BWP1;
	}

	public void setR87_FIG_BAL_BWP1(BigDecimal r87_FIG_BAL_BWP1) {
		R87_FIG_BAL_BWP1 = r87_FIG_BAL_BWP1;
	}

	public BigDecimal getR87_FIG_BAL_BWP2() {
		return R87_FIG_BAL_BWP2;
	}

	public void setR87_FIG_BAL_BWP2(BigDecimal r87_FIG_BAL_BWP2) {
		R87_FIG_BAL_BWP2 = r87_FIG_BAL_BWP2;
	}

	public BigDecimal getR87_AMT_ADJ_BWP1() {
		return R87_AMT_ADJ_BWP1;
	}

	public void setR87_AMT_ADJ_BWP1(BigDecimal r87_AMT_ADJ_BWP1) {
		R87_AMT_ADJ_BWP1 = r87_AMT_ADJ_BWP1;
	}

	public BigDecimal getR87_AMT_ADJ_BWP2() {
		return R87_AMT_ADJ_BWP2;
	}

	public void setR87_AMT_ADJ_BWP2(BigDecimal r87_AMT_ADJ_BWP2) {
		R87_AMT_ADJ_BWP2 = r87_AMT_ADJ_BWP2;
	}

	public BigDecimal getR87_NET_AMT_BWP1() {
		return R87_NET_AMT_BWP1;
	}

	public void setR87_NET_AMT_BWP1(BigDecimal r87_NET_AMT_BWP1) {
		R87_NET_AMT_BWP1 = r87_NET_AMT_BWP1;
	}

	public BigDecimal getR87_NET_AMT_BWP2() {
		return R87_NET_AMT_BWP2;
	}

	public void setR87_NET_AMT_BWP2(BigDecimal r87_NET_AMT_BWP2) {
		R87_NET_AMT_BWP2 = r87_NET_AMT_BWP2;
	}

	public BigDecimal getR87_BAL_SUB_BWP1() {
		return R87_BAL_SUB_BWP1;
	}

	public void setR87_BAL_SUB_BWP1(BigDecimal r87_BAL_SUB_BWP1) {
		R87_BAL_SUB_BWP1 = r87_BAL_SUB_BWP1;
	}

	public BigDecimal getR87_BAL_SUB_BWP2() {
		return R87_BAL_SUB_BWP2;
	}

	public void setR87_BAL_SUB_BWP2(BigDecimal r87_BAL_SUB_BWP2) {
		R87_BAL_SUB_BWP2 = r87_BAL_SUB_BWP2;
	}

	public BigDecimal getR87_BAL_ACT_SUB_BWP1() {
		return R87_BAL_ACT_SUB_BWP1;
	}

	public void setR87_BAL_ACT_SUB_BWP1(BigDecimal r87_BAL_ACT_SUB_BWP1) {
		R87_BAL_ACT_SUB_BWP1 = r87_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR87_BAL_ACT_SUB_BWP2() {
		return R87_BAL_ACT_SUB_BWP2;
	}

	public void setR87_BAL_ACT_SUB_BWP2(BigDecimal r87_BAL_ACT_SUB_BWP2) {
		R87_BAL_ACT_SUB_BWP2 = r87_BAL_ACT_SUB_BWP2;
	}

	public String getR88_PRODUCT() {
		return R88_PRODUCT;
	}

	public void setR88_PRODUCT(String r88_PRODUCT) {
		R88_PRODUCT = r88_PRODUCT;
	}

	public BigDecimal getR88_FIG_BAL_BWP1() {
		return R88_FIG_BAL_BWP1;
	}

	public void setR88_FIG_BAL_BWP1(BigDecimal r88_FIG_BAL_BWP1) {
		R88_FIG_BAL_BWP1 = r88_FIG_BAL_BWP1;
	}

	public BigDecimal getR88_FIG_BAL_BWP2() {
		return R88_FIG_BAL_BWP2;
	}

	public void setR88_FIG_BAL_BWP2(BigDecimal r88_FIG_BAL_BWP2) {
		R88_FIG_BAL_BWP2 = r88_FIG_BAL_BWP2;
	}

	public BigDecimal getR88_AMT_ADJ_BWP1() {
		return R88_AMT_ADJ_BWP1;
	}

	public void setR88_AMT_ADJ_BWP1(BigDecimal r88_AMT_ADJ_BWP1) {
		R88_AMT_ADJ_BWP1 = r88_AMT_ADJ_BWP1;
	}

	public BigDecimal getR88_AMT_ADJ_BWP2() {
		return R88_AMT_ADJ_BWP2;
	}

	public void setR88_AMT_ADJ_BWP2(BigDecimal r88_AMT_ADJ_BWP2) {
		R88_AMT_ADJ_BWP2 = r88_AMT_ADJ_BWP2;
	}

	public BigDecimal getR88_NET_AMT_BWP1() {
		return R88_NET_AMT_BWP1;
	}

	public void setR88_NET_AMT_BWP1(BigDecimal r88_NET_AMT_BWP1) {
		R88_NET_AMT_BWP1 = r88_NET_AMT_BWP1;
	}

	public BigDecimal getR88_NET_AMT_BWP2() {
		return R88_NET_AMT_BWP2;
	}

	public void setR88_NET_AMT_BWP2(BigDecimal r88_NET_AMT_BWP2) {
		R88_NET_AMT_BWP2 = r88_NET_AMT_BWP2;
	}

	public BigDecimal getR88_BAL_SUB_BWP1() {
		return R88_BAL_SUB_BWP1;
	}

	public void setR88_BAL_SUB_BWP1(BigDecimal r88_BAL_SUB_BWP1) {
		R88_BAL_SUB_BWP1 = r88_BAL_SUB_BWP1;
	}

	public BigDecimal getR88_BAL_SUB_BWP2() {
		return R88_BAL_SUB_BWP2;
	}

	public void setR88_BAL_SUB_BWP2(BigDecimal r88_BAL_SUB_BWP2) {
		R88_BAL_SUB_BWP2 = r88_BAL_SUB_BWP2;
	}

	public BigDecimal getR88_BAL_ACT_SUB_BWP1() {
		return R88_BAL_ACT_SUB_BWP1;
	}

	public void setR88_BAL_ACT_SUB_BWP1(BigDecimal r88_BAL_ACT_SUB_BWP1) {
		R88_BAL_ACT_SUB_BWP1 = r88_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR88_BAL_ACT_SUB_BWP2() {
		return R88_BAL_ACT_SUB_BWP2;
	}

	public void setR88_BAL_ACT_SUB_BWP2(BigDecimal r88_BAL_ACT_SUB_BWP2) {
		R88_BAL_ACT_SUB_BWP2 = r88_BAL_ACT_SUB_BWP2;
	}

	public String getR89_PRODUCT() {
		return R89_PRODUCT;
	}

	public void setR89_PRODUCT(String r89_PRODUCT) {
		R89_PRODUCT = r89_PRODUCT;
	}

	public BigDecimal getR89_FIG_BAL_BWP1() {
		return R89_FIG_BAL_BWP1;
	}

	public void setR89_FIG_BAL_BWP1(BigDecimal r89_FIG_BAL_BWP1) {
		R89_FIG_BAL_BWP1 = r89_FIG_BAL_BWP1;
	}

	public BigDecimal getR89_FIG_BAL_BWP2() {
		return R89_FIG_BAL_BWP2;
	}

	public void setR89_FIG_BAL_BWP2(BigDecimal r89_FIG_BAL_BWP2) {
		R89_FIG_BAL_BWP2 = r89_FIG_BAL_BWP2;
	}

	public BigDecimal getR89_AMT_ADJ_BWP1() {
		return R89_AMT_ADJ_BWP1;
	}

	public void setR89_AMT_ADJ_BWP1(BigDecimal r89_AMT_ADJ_BWP1) {
		R89_AMT_ADJ_BWP1 = r89_AMT_ADJ_BWP1;
	}

	public BigDecimal getR89_AMT_ADJ_BWP2() {
		return R89_AMT_ADJ_BWP2;
	}

	public void setR89_AMT_ADJ_BWP2(BigDecimal r89_AMT_ADJ_BWP2) {
		R89_AMT_ADJ_BWP2 = r89_AMT_ADJ_BWP2;
	}

	public BigDecimal getR89_NET_AMT_BWP1() {
		return R89_NET_AMT_BWP1;
	}

	public void setR89_NET_AMT_BWP1(BigDecimal r89_NET_AMT_BWP1) {
		R89_NET_AMT_BWP1 = r89_NET_AMT_BWP1;
	}

	public BigDecimal getR89_NET_AMT_BWP2() {
		return R89_NET_AMT_BWP2;
	}

	public void setR89_NET_AMT_BWP2(BigDecimal r89_NET_AMT_BWP2) {
		R89_NET_AMT_BWP2 = r89_NET_AMT_BWP2;
	}

	public BigDecimal getR89_BAL_SUB_BWP1() {
		return R89_BAL_SUB_BWP1;
	}

	public void setR89_BAL_SUB_BWP1(BigDecimal r89_BAL_SUB_BWP1) {
		R89_BAL_SUB_BWP1 = r89_BAL_SUB_BWP1;
	}

	public BigDecimal getR89_BAL_SUB_BWP2() {
		return R89_BAL_SUB_BWP2;
	}

	public void setR89_BAL_SUB_BWP2(BigDecimal r89_BAL_SUB_BWP2) {
		R89_BAL_SUB_BWP2 = r89_BAL_SUB_BWP2;
	}

	public BigDecimal getR89_BAL_ACT_SUB_BWP1() {
		return R89_BAL_ACT_SUB_BWP1;
	}

	public void setR89_BAL_ACT_SUB_BWP1(BigDecimal r89_BAL_ACT_SUB_BWP1) {
		R89_BAL_ACT_SUB_BWP1 = r89_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR89_BAL_ACT_SUB_BWP2() {
		return R89_BAL_ACT_SUB_BWP2;
	}

	public void setR89_BAL_ACT_SUB_BWP2(BigDecimal r89_BAL_ACT_SUB_BWP2) {
		R89_BAL_ACT_SUB_BWP2 = r89_BAL_ACT_SUB_BWP2;
	}

	public String getR90_PRODUCT() {
		return R90_PRODUCT;
	}

	public void setR90_PRODUCT(String r90_PRODUCT) {
		R90_PRODUCT = r90_PRODUCT;
	}

	public BigDecimal getR90_FIG_BAL_BWP1() {
		return R90_FIG_BAL_BWP1;
	}

	public void setR90_FIG_BAL_BWP1(BigDecimal r90_FIG_BAL_BWP1) {
		R90_FIG_BAL_BWP1 = r90_FIG_BAL_BWP1;
	}

	public BigDecimal getR90_FIG_BAL_BWP2() {
		return R90_FIG_BAL_BWP2;
	}

	public void setR90_FIG_BAL_BWP2(BigDecimal r90_FIG_BAL_BWP2) {
		R90_FIG_BAL_BWP2 = r90_FIG_BAL_BWP2;
	}

	public BigDecimal getR90_AMT_ADJ_BWP1() {
		return R90_AMT_ADJ_BWP1;
	}

	public void setR90_AMT_ADJ_BWP1(BigDecimal r90_AMT_ADJ_BWP1) {
		R90_AMT_ADJ_BWP1 = r90_AMT_ADJ_BWP1;
	}

	public BigDecimal getR90_AMT_ADJ_BWP2() {
		return R90_AMT_ADJ_BWP2;
	}

	public void setR90_AMT_ADJ_BWP2(BigDecimal r90_AMT_ADJ_BWP2) {
		R90_AMT_ADJ_BWP2 = r90_AMT_ADJ_BWP2;
	}

	public BigDecimal getR90_NET_AMT_BWP1() {
		return R90_NET_AMT_BWP1;
	}

	public void setR90_NET_AMT_BWP1(BigDecimal r90_NET_AMT_BWP1) {
		R90_NET_AMT_BWP1 = r90_NET_AMT_BWP1;
	}

	public BigDecimal getR90_NET_AMT_BWP2() {
		return R90_NET_AMT_BWP2;
	}

	public void setR90_NET_AMT_BWP2(BigDecimal r90_NET_AMT_BWP2) {
		R90_NET_AMT_BWP2 = r90_NET_AMT_BWP2;
	}

	public BigDecimal getR90_BAL_SUB_BWP1() {
		return R90_BAL_SUB_BWP1;
	}

	public void setR90_BAL_SUB_BWP1(BigDecimal r90_BAL_SUB_BWP1) {
		R90_BAL_SUB_BWP1 = r90_BAL_SUB_BWP1;
	}

	public BigDecimal getR90_BAL_SUB_BWP2() {
		return R90_BAL_SUB_BWP2;
	}

	public void setR90_BAL_SUB_BWP2(BigDecimal r90_BAL_SUB_BWP2) {
		R90_BAL_SUB_BWP2 = r90_BAL_SUB_BWP2;
	}

	public BigDecimal getR90_BAL_ACT_SUB_BWP1() {
		return R90_BAL_ACT_SUB_BWP1;
	}

	public void setR90_BAL_ACT_SUB_BWP1(BigDecimal r90_BAL_ACT_SUB_BWP1) {
		R90_BAL_ACT_SUB_BWP1 = r90_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR90_BAL_ACT_SUB_BWP2() {
		return R90_BAL_ACT_SUB_BWP2;
	}

	public void setR90_BAL_ACT_SUB_BWP2(BigDecimal r90_BAL_ACT_SUB_BWP2) {
		R90_BAL_ACT_SUB_BWP2 = r90_BAL_ACT_SUB_BWP2;
	}

	public String getR91_PRODUCT() {
		return R91_PRODUCT;
	}

	public void setR91_PRODUCT(String r91_PRODUCT) {
		R91_PRODUCT = r91_PRODUCT;
	}

	public BigDecimal getR91_FIG_BAL_BWP1() {
		return R91_FIG_BAL_BWP1;
	}

	public void setR91_FIG_BAL_BWP1(BigDecimal r91_FIG_BAL_BWP1) {
		R91_FIG_BAL_BWP1 = r91_FIG_BAL_BWP1;
	}

	public BigDecimal getR91_FIG_BAL_BWP2() {
		return R91_FIG_BAL_BWP2;
	}

	public void setR91_FIG_BAL_BWP2(BigDecimal r91_FIG_BAL_BWP2) {
		R91_FIG_BAL_BWP2 = r91_FIG_BAL_BWP2;
	}

	public BigDecimal getR91_AMT_ADJ_BWP1() {
		return R91_AMT_ADJ_BWP1;
	}

	public void setR91_AMT_ADJ_BWP1(BigDecimal r91_AMT_ADJ_BWP1) {
		R91_AMT_ADJ_BWP1 = r91_AMT_ADJ_BWP1;
	}

	public BigDecimal getR91_AMT_ADJ_BWP2() {
		return R91_AMT_ADJ_BWP2;
	}

	public void setR91_AMT_ADJ_BWP2(BigDecimal r91_AMT_ADJ_BWP2) {
		R91_AMT_ADJ_BWP2 = r91_AMT_ADJ_BWP2;
	}

	public BigDecimal getR91_NET_AMT_BWP1() {
		return R91_NET_AMT_BWP1;
	}

	public void setR91_NET_AMT_BWP1(BigDecimal r91_NET_AMT_BWP1) {
		R91_NET_AMT_BWP1 = r91_NET_AMT_BWP1;
	}

	public BigDecimal getR91_NET_AMT_BWP2() {
		return R91_NET_AMT_BWP2;
	}

	public void setR91_NET_AMT_BWP2(BigDecimal r91_NET_AMT_BWP2) {
		R91_NET_AMT_BWP2 = r91_NET_AMT_BWP2;
	}

	public BigDecimal getR91_BAL_SUB_BWP1() {
		return R91_BAL_SUB_BWP1;
	}

	public void setR91_BAL_SUB_BWP1(BigDecimal r91_BAL_SUB_BWP1) {
		R91_BAL_SUB_BWP1 = r91_BAL_SUB_BWP1;
	}

	public BigDecimal getR91_BAL_SUB_BWP2() {
		return R91_BAL_SUB_BWP2;
	}

	public void setR91_BAL_SUB_BWP2(BigDecimal r91_BAL_SUB_BWP2) {
		R91_BAL_SUB_BWP2 = r91_BAL_SUB_BWP2;
	}

	public BigDecimal getR91_BAL_ACT_SUB_BWP1() {
		return R91_BAL_ACT_SUB_BWP1;
	}

	public void setR91_BAL_ACT_SUB_BWP1(BigDecimal r91_BAL_ACT_SUB_BWP1) {
		R91_BAL_ACT_SUB_BWP1 = r91_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR91_BAL_ACT_SUB_BWP2() {
		return R91_BAL_ACT_SUB_BWP2;
	}

	public void setR91_BAL_ACT_SUB_BWP2(BigDecimal r91_BAL_ACT_SUB_BWP2) {
		R91_BAL_ACT_SUB_BWP2 = r91_BAL_ACT_SUB_BWP2;
	}

	public String getR92_PRODUCT() {
		return R92_PRODUCT;
	}

	public void setR92_PRODUCT(String r92_PRODUCT) {
		R92_PRODUCT = r92_PRODUCT;
	}

	public BigDecimal getR92_FIG_BAL_BWP1() {
		return R92_FIG_BAL_BWP1;
	}

	public void setR92_FIG_BAL_BWP1(BigDecimal r92_FIG_BAL_BWP1) {
		R92_FIG_BAL_BWP1 = r92_FIG_BAL_BWP1;
	}

	public BigDecimal getR92_FIG_BAL_BWP2() {
		return R92_FIG_BAL_BWP2;
	}

	public void setR92_FIG_BAL_BWP2(BigDecimal r92_FIG_BAL_BWP2) {
		R92_FIG_BAL_BWP2 = r92_FIG_BAL_BWP2;
	}

	public BigDecimal getR92_AMT_ADJ_BWP1() {
		return R92_AMT_ADJ_BWP1;
	}

	public void setR92_AMT_ADJ_BWP1(BigDecimal r92_AMT_ADJ_BWP1) {
		R92_AMT_ADJ_BWP1 = r92_AMT_ADJ_BWP1;
	}

	public BigDecimal getR92_AMT_ADJ_BWP2() {
		return R92_AMT_ADJ_BWP2;
	}

	public void setR92_AMT_ADJ_BWP2(BigDecimal r92_AMT_ADJ_BWP2) {
		R92_AMT_ADJ_BWP2 = r92_AMT_ADJ_BWP2;
	}

	public BigDecimal getR92_NET_AMT_BWP1() {
		return R92_NET_AMT_BWP1;
	}

	public void setR92_NET_AMT_BWP1(BigDecimal r92_NET_AMT_BWP1) {
		R92_NET_AMT_BWP1 = r92_NET_AMT_BWP1;
	}

	public BigDecimal getR92_NET_AMT_BWP2() {
		return R92_NET_AMT_BWP2;
	}

	public void setR92_NET_AMT_BWP2(BigDecimal r92_NET_AMT_BWP2) {
		R92_NET_AMT_BWP2 = r92_NET_AMT_BWP2;
	}

	public BigDecimal getR92_BAL_SUB_BWP1() {
		return R92_BAL_SUB_BWP1;
	}

	public void setR92_BAL_SUB_BWP1(BigDecimal r92_BAL_SUB_BWP1) {
		R92_BAL_SUB_BWP1 = r92_BAL_SUB_BWP1;
	}

	public BigDecimal getR92_BAL_SUB_BWP2() {
		return R92_BAL_SUB_BWP2;
	}

	public void setR92_BAL_SUB_BWP2(BigDecimal r92_BAL_SUB_BWP2) {
		R92_BAL_SUB_BWP2 = r92_BAL_SUB_BWP2;
	}

	public BigDecimal getR92_BAL_ACT_SUB_BWP1() {
		return R92_BAL_ACT_SUB_BWP1;
	}

	public void setR92_BAL_ACT_SUB_BWP1(BigDecimal r92_BAL_ACT_SUB_BWP1) {
		R92_BAL_ACT_SUB_BWP1 = r92_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR92_BAL_ACT_SUB_BWP2() {
		return R92_BAL_ACT_SUB_BWP2;
	}

	public void setR92_BAL_ACT_SUB_BWP2(BigDecimal r92_BAL_ACT_SUB_BWP2) {
		R92_BAL_ACT_SUB_BWP2 = r92_BAL_ACT_SUB_BWP2;
	}

	public String getR93_PRODUCT() {
		return R93_PRODUCT;
	}

	public void setR93_PRODUCT(String r93_PRODUCT) {
		R93_PRODUCT = r93_PRODUCT;
	}

	public BigDecimal getR93_FIG_BAL_BWP1() {
		return R93_FIG_BAL_BWP1;
	}

	public void setR93_FIG_BAL_BWP1(BigDecimal r93_FIG_BAL_BWP1) {
		R93_FIG_BAL_BWP1 = r93_FIG_BAL_BWP1;
	}

	public BigDecimal getR93_FIG_BAL_BWP2() {
		return R93_FIG_BAL_BWP2;
	}

	public void setR93_FIG_BAL_BWP2(BigDecimal r93_FIG_BAL_BWP2) {
		R93_FIG_BAL_BWP2 = r93_FIG_BAL_BWP2;
	}

	public BigDecimal getR93_AMT_ADJ_BWP1() {
		return R93_AMT_ADJ_BWP1;
	}

	public void setR93_AMT_ADJ_BWP1(BigDecimal r93_AMT_ADJ_BWP1) {
		R93_AMT_ADJ_BWP1 = r93_AMT_ADJ_BWP1;
	}

	public BigDecimal getR93_AMT_ADJ_BWP2() {
		return R93_AMT_ADJ_BWP2;
	}

	public void setR93_AMT_ADJ_BWP2(BigDecimal r93_AMT_ADJ_BWP2) {
		R93_AMT_ADJ_BWP2 = r93_AMT_ADJ_BWP2;
	}

	public BigDecimal getR93_NET_AMT_BWP1() {
		return R93_NET_AMT_BWP1;
	}

	public void setR93_NET_AMT_BWP1(BigDecimal r93_NET_AMT_BWP1) {
		R93_NET_AMT_BWP1 = r93_NET_AMT_BWP1;
	}

	public BigDecimal getR93_NET_AMT_BWP2() {
		return R93_NET_AMT_BWP2;
	}

	public void setR93_NET_AMT_BWP2(BigDecimal r93_NET_AMT_BWP2) {
		R93_NET_AMT_BWP2 = r93_NET_AMT_BWP2;
	}

	public BigDecimal getR93_BAL_SUB_BWP1() {
		return R93_BAL_SUB_BWP1;
	}

	public void setR93_BAL_SUB_BWP1(BigDecimal r93_BAL_SUB_BWP1) {
		R93_BAL_SUB_BWP1 = r93_BAL_SUB_BWP1;
	}

	public BigDecimal getR93_BAL_SUB_BWP2() {
		return R93_BAL_SUB_BWP2;
	}

	public void setR93_BAL_SUB_BWP2(BigDecimal r93_BAL_SUB_BWP2) {
		R93_BAL_SUB_BWP2 = r93_BAL_SUB_BWP2;
	}

	public BigDecimal getR93_BAL_ACT_SUB_BWP1() {
		return R93_BAL_ACT_SUB_BWP1;
	}

	public void setR93_BAL_ACT_SUB_BWP1(BigDecimal r93_BAL_ACT_SUB_BWP1) {
		R93_BAL_ACT_SUB_BWP1 = r93_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR93_BAL_ACT_SUB_BWP2() {
		return R93_BAL_ACT_SUB_BWP2;
	}

	public void setR93_BAL_ACT_SUB_BWP2(BigDecimal r93_BAL_ACT_SUB_BWP2) {
		R93_BAL_ACT_SUB_BWP2 = r93_BAL_ACT_SUB_BWP2;
	}

	public String getR94_PRODUCT() {
		return R94_PRODUCT;
	}

	public void setR94_PRODUCT(String r94_PRODUCT) {
		R94_PRODUCT = r94_PRODUCT;
	}

	public BigDecimal getR94_FIG_BAL_BWP1() {
		return R94_FIG_BAL_BWP1;
	}

	public void setR94_FIG_BAL_BWP1(BigDecimal r94_FIG_BAL_BWP1) {
		R94_FIG_BAL_BWP1 = r94_FIG_BAL_BWP1;
	}

	public BigDecimal getR94_FIG_BAL_BWP2() {
		return R94_FIG_BAL_BWP2;
	}

	public void setR94_FIG_BAL_BWP2(BigDecimal r94_FIG_BAL_BWP2) {
		R94_FIG_BAL_BWP2 = r94_FIG_BAL_BWP2;
	}

	public BigDecimal getR94_AMT_ADJ_BWP1() {
		return R94_AMT_ADJ_BWP1;
	}

	public void setR94_AMT_ADJ_BWP1(BigDecimal r94_AMT_ADJ_BWP1) {
		R94_AMT_ADJ_BWP1 = r94_AMT_ADJ_BWP1;
	}

	public BigDecimal getR94_AMT_ADJ_BWP2() {
		return R94_AMT_ADJ_BWP2;
	}

	public void setR94_AMT_ADJ_BWP2(BigDecimal r94_AMT_ADJ_BWP2) {
		R94_AMT_ADJ_BWP2 = r94_AMT_ADJ_BWP2;
	}

	public BigDecimal getR94_NET_AMT_BWP1() {
		return R94_NET_AMT_BWP1;
	}

	public void setR94_NET_AMT_BWP1(BigDecimal r94_NET_AMT_BWP1) {
		R94_NET_AMT_BWP1 = r94_NET_AMT_BWP1;
	}

	public BigDecimal getR94_NET_AMT_BWP2() {
		return R94_NET_AMT_BWP2;
	}

	public void setR94_NET_AMT_BWP2(BigDecimal r94_NET_AMT_BWP2) {
		R94_NET_AMT_BWP2 = r94_NET_AMT_BWP2;
	}

	public BigDecimal getR94_BAL_SUB_BWP1() {
		return R94_BAL_SUB_BWP1;
	}

	public void setR94_BAL_SUB_BWP1(BigDecimal r94_BAL_SUB_BWP1) {
		R94_BAL_SUB_BWP1 = r94_BAL_SUB_BWP1;
	}

	public BigDecimal getR94_BAL_SUB_BWP2() {
		return R94_BAL_SUB_BWP2;
	}

	public void setR94_BAL_SUB_BWP2(BigDecimal r94_BAL_SUB_BWP2) {
		R94_BAL_SUB_BWP2 = r94_BAL_SUB_BWP2;
	}

	public BigDecimal getR94_BAL_ACT_SUB_BWP1() {
		return R94_BAL_ACT_SUB_BWP1;
	}

	public void setR94_BAL_ACT_SUB_BWP1(BigDecimal r94_BAL_ACT_SUB_BWP1) {
		R94_BAL_ACT_SUB_BWP1 = r94_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR94_BAL_ACT_SUB_BWP2() {
		return R94_BAL_ACT_SUB_BWP2;
	}

	public void setR94_BAL_ACT_SUB_BWP2(BigDecimal r94_BAL_ACT_SUB_BWP2) {
		R94_BAL_ACT_SUB_BWP2 = r94_BAL_ACT_SUB_BWP2;
	}

	public String getR102_PRODUCT() {
		return R102_PRODUCT;
	}

	public void setR102_PRODUCT(String r102_PRODUCT) {
		R102_PRODUCT = r102_PRODUCT;
	}

	public BigDecimal getR102_FIG_BAL_BWP1() {
		return R102_FIG_BAL_BWP1;
	}

	public void setR102_FIG_BAL_BWP1(BigDecimal r102_FIG_BAL_BWP1) {
		R102_FIG_BAL_BWP1 = r102_FIG_BAL_BWP1;
	}

	public BigDecimal getR102_FIG_BAL_BWP2() {
		return R102_FIG_BAL_BWP2;
	}

	public void setR102_FIG_BAL_BWP2(BigDecimal r102_FIG_BAL_BWP2) {
		R102_FIG_BAL_BWP2 = r102_FIG_BAL_BWP2;
	}

	public BigDecimal getR102_AMT_ADJ_BWP1() {
		return R102_AMT_ADJ_BWP1;
	}

	public void setR102_AMT_ADJ_BWP1(BigDecimal r102_AMT_ADJ_BWP1) {
		R102_AMT_ADJ_BWP1 = r102_AMT_ADJ_BWP1;
	}

	public BigDecimal getR102_AMT_ADJ_BWP2() {
		return R102_AMT_ADJ_BWP2;
	}

	public void setR102_AMT_ADJ_BWP2(BigDecimal r102_AMT_ADJ_BWP2) {
		R102_AMT_ADJ_BWP2 = r102_AMT_ADJ_BWP2;
	}

	public BigDecimal getR102_NET_AMT_BWP1() {
		return R102_NET_AMT_BWP1;
	}

	public void setR102_NET_AMT_BWP1(BigDecimal r102_NET_AMT_BWP1) {
		R102_NET_AMT_BWP1 = r102_NET_AMT_BWP1;
	}

	public BigDecimal getR102_NET_AMT_BWP2() {
		return R102_NET_AMT_BWP2;
	}

	public void setR102_NET_AMT_BWP2(BigDecimal r102_NET_AMT_BWP2) {
		R102_NET_AMT_BWP2 = r102_NET_AMT_BWP2;
	}

	public BigDecimal getR102_BAL_SUB_BWP1() {
		return R102_BAL_SUB_BWP1;
	}

	public void setR102_BAL_SUB_BWP1(BigDecimal r102_BAL_SUB_BWP1) {
		R102_BAL_SUB_BWP1 = r102_BAL_SUB_BWP1;
	}

	public BigDecimal getR102_BAL_SUB_BWP2() {
		return R102_BAL_SUB_BWP2;
	}

	public void setR102_BAL_SUB_BWP2(BigDecimal r102_BAL_SUB_BWP2) {
		R102_BAL_SUB_BWP2 = r102_BAL_SUB_BWP2;
	}

	public BigDecimal getR102_BAL_ACT_SUB_BWP1() {
		return R102_BAL_ACT_SUB_BWP1;
	}

	public void setR102_BAL_ACT_SUB_BWP1(BigDecimal r102_BAL_ACT_SUB_BWP1) {
		R102_BAL_ACT_SUB_BWP1 = r102_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR102_BAL_ACT_SUB_BWP2() {
		return R102_BAL_ACT_SUB_BWP2;
	}

	public void setR102_BAL_ACT_SUB_BWP2(BigDecimal r102_BAL_ACT_SUB_BWP2) {
		R102_BAL_ACT_SUB_BWP2 = r102_BAL_ACT_SUB_BWP2;
	}

	public String getR103_PRODUCT() {
		return R103_PRODUCT;
	}

	public void setR103_PRODUCT(String r103_PRODUCT) {
		R103_PRODUCT = r103_PRODUCT;
	}

	public BigDecimal getR103_FIG_BAL_BWP1() {
		return R103_FIG_BAL_BWP1;
	}

	public void setR103_FIG_BAL_BWP1(BigDecimal r103_FIG_BAL_BWP1) {
		R103_FIG_BAL_BWP1 = r103_FIG_BAL_BWP1;
	}

	public BigDecimal getR103_FIG_BAL_BWP2() {
		return R103_FIG_BAL_BWP2;
	}

	public void setR103_FIG_BAL_BWP2(BigDecimal r103_FIG_BAL_BWP2) {
		R103_FIG_BAL_BWP2 = r103_FIG_BAL_BWP2;
	}

	public BigDecimal getR103_AMT_ADJ_BWP1() {
		return R103_AMT_ADJ_BWP1;
	}

	public void setR103_AMT_ADJ_BWP1(BigDecimal r103_AMT_ADJ_BWP1) {
		R103_AMT_ADJ_BWP1 = r103_AMT_ADJ_BWP1;
	}

	public BigDecimal getR103_AMT_ADJ_BWP2() {
		return R103_AMT_ADJ_BWP2;
	}

	public void setR103_AMT_ADJ_BWP2(BigDecimal r103_AMT_ADJ_BWP2) {
		R103_AMT_ADJ_BWP2 = r103_AMT_ADJ_BWP2;
	}

	public BigDecimal getR103_NET_AMT_BWP1() {
		return R103_NET_AMT_BWP1;
	}

	public void setR103_NET_AMT_BWP1(BigDecimal r103_NET_AMT_BWP1) {
		R103_NET_AMT_BWP1 = r103_NET_AMT_BWP1;
	}

	public BigDecimal getR103_NET_AMT_BWP2() {
		return R103_NET_AMT_BWP2;
	}

	public void setR103_NET_AMT_BWP2(BigDecimal r103_NET_AMT_BWP2) {
		R103_NET_AMT_BWP2 = r103_NET_AMT_BWP2;
	}

	public BigDecimal getR103_BAL_SUB_BWP1() {
		return R103_BAL_SUB_BWP1;
	}

	public void setR103_BAL_SUB_BWP1(BigDecimal r103_BAL_SUB_BWP1) {
		R103_BAL_SUB_BWP1 = r103_BAL_SUB_BWP1;
	}

	public BigDecimal getR103_BAL_SUB_BWP2() {
		return R103_BAL_SUB_BWP2;
	}

	public void setR103_BAL_SUB_BWP2(BigDecimal r103_BAL_SUB_BWP2) {
		R103_BAL_SUB_BWP2 = r103_BAL_SUB_BWP2;
	}

	public BigDecimal getR103_BAL_ACT_SUB_BWP1() {
		return R103_BAL_ACT_SUB_BWP1;
	}

	public void setR103_BAL_ACT_SUB_BWP1(BigDecimal r103_BAL_ACT_SUB_BWP1) {
		R103_BAL_ACT_SUB_BWP1 = r103_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR103_BAL_ACT_SUB_BWP2() {
		return R103_BAL_ACT_SUB_BWP2;
	}

	public void setR103_BAL_ACT_SUB_BWP2(BigDecimal r103_BAL_ACT_SUB_BWP2) {
		R103_BAL_ACT_SUB_BWP2 = r103_BAL_ACT_SUB_BWP2;
	}

	public String getR104_PRODUCT() {
		return R104_PRODUCT;
	}

	public void setR104_PRODUCT(String r104_PRODUCT) {
		R104_PRODUCT = r104_PRODUCT;
	}

	public BigDecimal getR104_FIG_BAL_BWP1() {
		return R104_FIG_BAL_BWP1;
	}

	public void setR104_FIG_BAL_BWP1(BigDecimal r104_FIG_BAL_BWP1) {
		R104_FIG_BAL_BWP1 = r104_FIG_BAL_BWP1;
	}

	public BigDecimal getR104_FIG_BAL_BWP2() {
		return R104_FIG_BAL_BWP2;
	}

	public void setR104_FIG_BAL_BWP2(BigDecimal r104_FIG_BAL_BWP2) {
		R104_FIG_BAL_BWP2 = r104_FIG_BAL_BWP2;
	}

	public BigDecimal getR104_AMT_ADJ_BWP1() {
		return R104_AMT_ADJ_BWP1;
	}

	public void setR104_AMT_ADJ_BWP1(BigDecimal r104_AMT_ADJ_BWP1) {
		R104_AMT_ADJ_BWP1 = r104_AMT_ADJ_BWP1;
	}

	public BigDecimal getR104_AMT_ADJ_BWP2() {
		return R104_AMT_ADJ_BWP2;
	}

	public void setR104_AMT_ADJ_BWP2(BigDecimal r104_AMT_ADJ_BWP2) {
		R104_AMT_ADJ_BWP2 = r104_AMT_ADJ_BWP2;
	}

	public BigDecimal getR104_NET_AMT_BWP1() {
		return R104_NET_AMT_BWP1;
	}

	public void setR104_NET_AMT_BWP1(BigDecimal r104_NET_AMT_BWP1) {
		R104_NET_AMT_BWP1 = r104_NET_AMT_BWP1;
	}

	public BigDecimal getR104_NET_AMT_BWP2() {
		return R104_NET_AMT_BWP2;
	}

	public void setR104_NET_AMT_BWP2(BigDecimal r104_NET_AMT_BWP2) {
		R104_NET_AMT_BWP2 = r104_NET_AMT_BWP2;
	}

	public BigDecimal getR104_BAL_SUB_BWP1() {
		return R104_BAL_SUB_BWP1;
	}

	public void setR104_BAL_SUB_BWP1(BigDecimal r104_BAL_SUB_BWP1) {
		R104_BAL_SUB_BWP1 = r104_BAL_SUB_BWP1;
	}

	public BigDecimal getR104_BAL_SUB_BWP2() {
		return R104_BAL_SUB_BWP2;
	}

	public void setR104_BAL_SUB_BWP2(BigDecimal r104_BAL_SUB_BWP2) {
		R104_BAL_SUB_BWP2 = r104_BAL_SUB_BWP2;
	}

	public BigDecimal getR104_BAL_ACT_SUB_BWP1() {
		return R104_BAL_ACT_SUB_BWP1;
	}

	public void setR104_BAL_ACT_SUB_BWP1(BigDecimal r104_BAL_ACT_SUB_BWP1) {
		R104_BAL_ACT_SUB_BWP1 = r104_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR104_BAL_ACT_SUB_BWP2() {
		return R104_BAL_ACT_SUB_BWP2;
	}

	public void setR104_BAL_ACT_SUB_BWP2(BigDecimal r104_BAL_ACT_SUB_BWP2) {
		R104_BAL_ACT_SUB_BWP2 = r104_BAL_ACT_SUB_BWP2;
	}

	public String getR105_PRODUCT() {
		return R105_PRODUCT;
	}

	public void setR105_PRODUCT(String r105_PRODUCT) {
		R105_PRODUCT = r105_PRODUCT;
	}

	public BigDecimal getR105_FIG_BAL_BWP1() {
		return R105_FIG_BAL_BWP1;
	}

	public void setR105_FIG_BAL_BWP1(BigDecimal r105_FIG_BAL_BWP1) {
		R105_FIG_BAL_BWP1 = r105_FIG_BAL_BWP1;
	}

	public BigDecimal getR105_FIG_BAL_BWP2() {
		return R105_FIG_BAL_BWP2;
	}

	public void setR105_FIG_BAL_BWP2(BigDecimal r105_FIG_BAL_BWP2) {
		R105_FIG_BAL_BWP2 = r105_FIG_BAL_BWP2;
	}

	public BigDecimal getR105_AMT_ADJ_BWP1() {
		return R105_AMT_ADJ_BWP1;
	}

	public void setR105_AMT_ADJ_BWP1(BigDecimal r105_AMT_ADJ_BWP1) {
		R105_AMT_ADJ_BWP1 = r105_AMT_ADJ_BWP1;
	}

	public BigDecimal getR105_AMT_ADJ_BWP2() {
		return R105_AMT_ADJ_BWP2;
	}

	public void setR105_AMT_ADJ_BWP2(BigDecimal r105_AMT_ADJ_BWP2) {
		R105_AMT_ADJ_BWP2 = r105_AMT_ADJ_BWP2;
	}

	public BigDecimal getR105_NET_AMT_BWP1() {
		return R105_NET_AMT_BWP1;
	}

	public void setR105_NET_AMT_BWP1(BigDecimal r105_NET_AMT_BWP1) {
		R105_NET_AMT_BWP1 = r105_NET_AMT_BWP1;
	}

	public BigDecimal getR105_NET_AMT_BWP2() {
		return R105_NET_AMT_BWP2;
	}

	public void setR105_NET_AMT_BWP2(BigDecimal r105_NET_AMT_BWP2) {
		R105_NET_AMT_BWP2 = r105_NET_AMT_BWP2;
	}

	public BigDecimal getR105_BAL_SUB_BWP1() {
		return R105_BAL_SUB_BWP1;
	}

	public void setR105_BAL_SUB_BWP1(BigDecimal r105_BAL_SUB_BWP1) {
		R105_BAL_SUB_BWP1 = r105_BAL_SUB_BWP1;
	}

	public BigDecimal getR105_BAL_SUB_BWP2() {
		return R105_BAL_SUB_BWP2;
	}

	public void setR105_BAL_SUB_BWP2(BigDecimal r105_BAL_SUB_BWP2) {
		R105_BAL_SUB_BWP2 = r105_BAL_SUB_BWP2;
	}

	public BigDecimal getR105_BAL_ACT_SUB_BWP1() {
		return R105_BAL_ACT_SUB_BWP1;
	}

	public void setR105_BAL_ACT_SUB_BWP1(BigDecimal r105_BAL_ACT_SUB_BWP1) {
		R105_BAL_ACT_SUB_BWP1 = r105_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR105_BAL_ACT_SUB_BWP2() {
		return R105_BAL_ACT_SUB_BWP2;
	}

	public void setR105_BAL_ACT_SUB_BWP2(BigDecimal r105_BAL_ACT_SUB_BWP2) {
		R105_BAL_ACT_SUB_BWP2 = r105_BAL_ACT_SUB_BWP2;
	}

	public String getR106_PRODUCT() {
		return R106_PRODUCT;
	}

	public void setR106_PRODUCT(String r106_PRODUCT) {
		R106_PRODUCT = r106_PRODUCT;
	}

	public BigDecimal getR106_FIG_BAL_BWP1() {
		return R106_FIG_BAL_BWP1;
	}

	public void setR106_FIG_BAL_BWP1(BigDecimal r106_FIG_BAL_BWP1) {
		R106_FIG_BAL_BWP1 = r106_FIG_BAL_BWP1;
	}

	public BigDecimal getR106_FIG_BAL_BWP2() {
		return R106_FIG_BAL_BWP2;
	}

	public void setR106_FIG_BAL_BWP2(BigDecimal r106_FIG_BAL_BWP2) {
		R106_FIG_BAL_BWP2 = r106_FIG_BAL_BWP2;
	}

	public BigDecimal getR106_AMT_ADJ_BWP1() {
		return R106_AMT_ADJ_BWP1;
	}

	public void setR106_AMT_ADJ_BWP1(BigDecimal r106_AMT_ADJ_BWP1) {
		R106_AMT_ADJ_BWP1 = r106_AMT_ADJ_BWP1;
	}

	public BigDecimal getR106_AMT_ADJ_BWP2() {
		return R106_AMT_ADJ_BWP2;
	}

	public void setR106_AMT_ADJ_BWP2(BigDecimal r106_AMT_ADJ_BWP2) {
		R106_AMT_ADJ_BWP2 = r106_AMT_ADJ_BWP2;
	}

	public BigDecimal getR106_NET_AMT_BWP1() {
		return R106_NET_AMT_BWP1;
	}

	public void setR106_NET_AMT_BWP1(BigDecimal r106_NET_AMT_BWP1) {
		R106_NET_AMT_BWP1 = r106_NET_AMT_BWP1;
	}

	public BigDecimal getR106_NET_AMT_BWP2() {
		return R106_NET_AMT_BWP2;
	}

	public void setR106_NET_AMT_BWP2(BigDecimal r106_NET_AMT_BWP2) {
		R106_NET_AMT_BWP2 = r106_NET_AMT_BWP2;
	}

	public BigDecimal getR106_BAL_SUB_BWP1() {
		return R106_BAL_SUB_BWP1;
	}

	public void setR106_BAL_SUB_BWP1(BigDecimal r106_BAL_SUB_BWP1) {
		R106_BAL_SUB_BWP1 = r106_BAL_SUB_BWP1;
	}

	public BigDecimal getR106_BAL_SUB_BWP2() {
		return R106_BAL_SUB_BWP2;
	}

	public void setR106_BAL_SUB_BWP2(BigDecimal r106_BAL_SUB_BWP2) {
		R106_BAL_SUB_BWP2 = r106_BAL_SUB_BWP2;
	}

	public BigDecimal getR106_BAL_ACT_SUB_BWP1() {
		return R106_BAL_ACT_SUB_BWP1;
	}

	public void setR106_BAL_ACT_SUB_BWP1(BigDecimal r106_BAL_ACT_SUB_BWP1) {
		R106_BAL_ACT_SUB_BWP1 = r106_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR106_BAL_ACT_SUB_BWP2() {
		return R106_BAL_ACT_SUB_BWP2;
	}

	public void setR106_BAL_ACT_SUB_BWP2(BigDecimal r106_BAL_ACT_SUB_BWP2) {
		R106_BAL_ACT_SUB_BWP2 = r106_BAL_ACT_SUB_BWP2;
	}

	public String getR107_PRODUCT() {
		return R107_PRODUCT;
	}

	public void setR107_PRODUCT(String r107_PRODUCT) {
		R107_PRODUCT = r107_PRODUCT;
	}

	public BigDecimal getR107_FIG_BAL_BWP1() {
		return R107_FIG_BAL_BWP1;
	}

	public void setR107_FIG_BAL_BWP1(BigDecimal r107_FIG_BAL_BWP1) {
		R107_FIG_BAL_BWP1 = r107_FIG_BAL_BWP1;
	}

	public BigDecimal getR107_FIG_BAL_BWP2() {
		return R107_FIG_BAL_BWP2;
	}

	public void setR107_FIG_BAL_BWP2(BigDecimal r107_FIG_BAL_BWP2) {
		R107_FIG_BAL_BWP2 = r107_FIG_BAL_BWP2;
	}

	public BigDecimal getR107_AMT_ADJ_BWP1() {
		return R107_AMT_ADJ_BWP1;
	}

	public void setR107_AMT_ADJ_BWP1(BigDecimal r107_AMT_ADJ_BWP1) {
		R107_AMT_ADJ_BWP1 = r107_AMT_ADJ_BWP1;
	}

	public BigDecimal getR107_AMT_ADJ_BWP2() {
		return R107_AMT_ADJ_BWP2;
	}

	public void setR107_AMT_ADJ_BWP2(BigDecimal r107_AMT_ADJ_BWP2) {
		R107_AMT_ADJ_BWP2 = r107_AMT_ADJ_BWP2;
	}

	public BigDecimal getR107_NET_AMT_BWP1() {
		return R107_NET_AMT_BWP1;
	}

	public void setR107_NET_AMT_BWP1(BigDecimal r107_NET_AMT_BWP1) {
		R107_NET_AMT_BWP1 = r107_NET_AMT_BWP1;
	}

	public BigDecimal getR107_NET_AMT_BWP2() {
		return R107_NET_AMT_BWP2;
	}

	public void setR107_NET_AMT_BWP2(BigDecimal r107_NET_AMT_BWP2) {
		R107_NET_AMT_BWP2 = r107_NET_AMT_BWP2;
	}

	public BigDecimal getR107_BAL_SUB_BWP1() {
		return R107_BAL_SUB_BWP1;
	}

	public void setR107_BAL_SUB_BWP1(BigDecimal r107_BAL_SUB_BWP1) {
		R107_BAL_SUB_BWP1 = r107_BAL_SUB_BWP1;
	}

	public BigDecimal getR107_BAL_SUB_BWP2() {
		return R107_BAL_SUB_BWP2;
	}

	public void setR107_BAL_SUB_BWP2(BigDecimal r107_BAL_SUB_BWP2) {
		R107_BAL_SUB_BWP2 = r107_BAL_SUB_BWP2;
	}

	public BigDecimal getR107_BAL_ACT_SUB_BWP1() {
		return R107_BAL_ACT_SUB_BWP1;
	}

	public void setR107_BAL_ACT_SUB_BWP1(BigDecimal r107_BAL_ACT_SUB_BWP1) {
		R107_BAL_ACT_SUB_BWP1 = r107_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR107_BAL_ACT_SUB_BWP2() {
		return R107_BAL_ACT_SUB_BWP2;
	}

	public void setR107_BAL_ACT_SUB_BWP2(BigDecimal r107_BAL_ACT_SUB_BWP2) {
		R107_BAL_ACT_SUB_BWP2 = r107_BAL_ACT_SUB_BWP2;
	}

	public String getR108_PRODUCT() {
		return R108_PRODUCT;
	}

	public void setR108_PRODUCT(String r108_PRODUCT) {
		R108_PRODUCT = r108_PRODUCT;
	}

	public BigDecimal getR108_FIG_BAL_BWP1() {
		return R108_FIG_BAL_BWP1;
	}

	public void setR108_FIG_BAL_BWP1(BigDecimal r108_FIG_BAL_BWP1) {
		R108_FIG_BAL_BWP1 = r108_FIG_BAL_BWP1;
	}

	public BigDecimal getR108_FIG_BAL_BWP2() {
		return R108_FIG_BAL_BWP2;
	}

	public void setR108_FIG_BAL_BWP2(BigDecimal r108_FIG_BAL_BWP2) {
		R108_FIG_BAL_BWP2 = r108_FIG_BAL_BWP2;
	}

	public BigDecimal getR108_AMT_ADJ_BWP1() {
		return R108_AMT_ADJ_BWP1;
	}

	public void setR108_AMT_ADJ_BWP1(BigDecimal r108_AMT_ADJ_BWP1) {
		R108_AMT_ADJ_BWP1 = r108_AMT_ADJ_BWP1;
	}

	public BigDecimal getR108_AMT_ADJ_BWP2() {
		return R108_AMT_ADJ_BWP2;
	}

	public void setR108_AMT_ADJ_BWP2(BigDecimal r108_AMT_ADJ_BWP2) {
		R108_AMT_ADJ_BWP2 = r108_AMT_ADJ_BWP2;
	}

	public BigDecimal getR108_NET_AMT_BWP1() {
		return R108_NET_AMT_BWP1;
	}

	public void setR108_NET_AMT_BWP1(BigDecimal r108_NET_AMT_BWP1) {
		R108_NET_AMT_BWP1 = r108_NET_AMT_BWP1;
	}

	public BigDecimal getR108_NET_AMT_BWP2() {
		return R108_NET_AMT_BWP2;
	}

	public void setR108_NET_AMT_BWP2(BigDecimal r108_NET_AMT_BWP2) {
		R108_NET_AMT_BWP2 = r108_NET_AMT_BWP2;
	}

	public BigDecimal getR108_BAL_SUB_BWP1() {
		return R108_BAL_SUB_BWP1;
	}

	public void setR108_BAL_SUB_BWP1(BigDecimal r108_BAL_SUB_BWP1) {
		R108_BAL_SUB_BWP1 = r108_BAL_SUB_BWP1;
	}

	public BigDecimal getR108_BAL_SUB_BWP2() {
		return R108_BAL_SUB_BWP2;
	}

	public void setR108_BAL_SUB_BWP2(BigDecimal r108_BAL_SUB_BWP2) {
		R108_BAL_SUB_BWP2 = r108_BAL_SUB_BWP2;
	}

	public BigDecimal getR108_BAL_ACT_SUB_BWP1() {
		return R108_BAL_ACT_SUB_BWP1;
	}

	public void setR108_BAL_ACT_SUB_BWP1(BigDecimal r108_BAL_ACT_SUB_BWP1) {
		R108_BAL_ACT_SUB_BWP1 = r108_BAL_ACT_SUB_BWP1;
	}

	public BigDecimal getR108_BAL_ACT_SUB_BWP2() {
		return R108_BAL_ACT_SUB_BWP2;
	}

	public void setR108_BAL_ACT_SUB_BWP2(BigDecimal r108_BAL_ACT_SUB_BWP2) {
		R108_BAL_ACT_SUB_BWP2 = r108_BAL_ACT_SUB_BWP2;
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

	public GL_SCH_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}

   
    
    
    
}
