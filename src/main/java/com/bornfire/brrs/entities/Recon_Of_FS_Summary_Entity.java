package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "BRRS_RECON_OF_FS_SUMMARYTABLE", schema = "BRRS")
public class Recon_Of_FS_Summary_Entity {

    /*
     * =========================
     * Primary Key (Logical)
     * =========================
     */
    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "REPORT_DATE")
    private Date REPORT_DATE;

    /*
     * =========================
     * R7 – R10
     * =========================
     */
    @Column(name = "R7_PRODUCT")
    private String R7_PRODUCT;
    @Column(name = "R7_BAL_SHEET_PUB_FS")
    private BigDecimal R7_BAL_SHEET_PUB_FS;
    @Column(name = "R7_UNDER_REG_SOC")
    private BigDecimal R7_UNDER_REG_SOC;

    @Column(name = "R8_PRODUCT")
    private String R8_PRODUCT;
    @Column(name = "R8_BAL_SHEET_PUB_FS")
    private BigDecimal R8_BAL_SHEET_PUB_FS;
    @Column(name = "R8_UNDER_REG_SOC")
    private BigDecimal R8_UNDER_REG_SOC;

    @Column(name = "R9_PRODUCT")
    private String R9_PRODUCT;
    @Column(name = "R9_BAL_SHEET_PUB_FS")
    private BigDecimal R9_BAL_SHEET_PUB_FS;
    @Column(name = "R9_UNDER_REG_SOC")
    private BigDecimal R9_UNDER_REG_SOC;

    @Column(name = "R10_PRODUCT")
    private String R10_PRODUCT;
    @Column(name = "R10_BAL_SHEET_PUB_FS")
    private BigDecimal R10_BAL_SHEET_PUB_FS;
    @Column(name = "R10_UNDER_REG_SOC")
    private BigDecimal R10_UNDER_REG_SOC;

    /*
     * =========================
     * R11 – R20
     * =========================
     */
    @Column(name = "R11_PRODUCT")
    private String R11_PRODUCT;
    @Column(name = "R11_BAL_SHEET_PUB_FS")
    private BigDecimal R11_BAL_SHEET_PUB_FS;
    @Column(name = "R11_UNDER_REG_SOC")
    private BigDecimal R11_UNDER_REG_SOC;

    @Column(name = "R12_PRODUCT")
    private String R12_PRODUCT;
    @Column(name = "R12_BAL_SHEET_PUB_FS")
    private BigDecimal R12_BAL_SHEET_PUB_FS;
    @Column(name = "R12_UNDER_REG_SOC")
    private BigDecimal R12_UNDER_REG_SOC;

    @Column(name = "R13_PRODUCT")
    private String R13_PRODUCT;
    @Column(name = "R13_BAL_SHEET_PUB_FS")
    private BigDecimal R13_BAL_SHEET_PUB_FS;
    @Column(name = "R13_UNDER_REG_SOC")
    private BigDecimal R13_UNDER_REG_SOC;

    @Column(name = "R14_PRODUCT")
    private String R14_PRODUCT;
    @Column(name = "R14_BAL_SHEET_PUB_FS")
    private BigDecimal R14_BAL_SHEET_PUB_FS;
    @Column(name = "R14_UNDER_REG_SOC")
    private BigDecimal R14_UNDER_REG_SOC;

    @Column(name = "R15_PRODUCT")
    private String R15_PRODUCT;
    @Column(name = "R15_BAL_SHEET_PUB_FS")
    private BigDecimal R15_BAL_SHEET_PUB_FS;
    @Column(name = "R15_UNDER_REG_SOC")
    private BigDecimal R15_UNDER_REG_SOC;

    @Column(name = "R16_PRODUCT")
    private String R16_PRODUCT;
    @Column(name = "R16_BAL_SHEET_PUB_FS")
    private BigDecimal R16_BAL_SHEET_PUB_FS;
    @Column(name = "R16_UNDER_REG_SOC")
    private BigDecimal R16_UNDER_REG_SOC;

    @Column(name = "R17_PRODUCT")
    private String R17_PRODUCT;
    @Column(name = "R17_BAL_SHEET_PUB_FS")
    private BigDecimal R17_BAL_SHEET_PUB_FS;
    @Column(name = "R17_UNDER_REG_SOC")
    private BigDecimal R17_UNDER_REG_SOC;

    @Column(name = "R18_PRODUCT")
    private String R18_PRODUCT;
    @Column(name = "R18_BAL_SHEET_PUB_FS")
    private BigDecimal R18_BAL_SHEET_PUB_FS;
    @Column(name = "R18_UNDER_REG_SOC")
    private BigDecimal R18_UNDER_REG_SOC;

    @Column(name = "R19_PRODUCT")
    private String R19_PRODUCT;
    @Column(name = "R19_BAL_SHEET_PUB_FS")
    private BigDecimal R19_BAL_SHEET_PUB_FS;
    @Column(name = "R19_UNDER_REG_SOC")
    private BigDecimal R19_UNDER_REG_SOC;

    @Column(name = "R20_PRODUCT")
    private String R20_PRODUCT;
    @Column(name = "R20_BAL_SHEET_PUB_FS")
    private BigDecimal R20_BAL_SHEET_PUB_FS;
    @Column(name = "R20_UNDER_REG_SOC")
    private BigDecimal R20_UNDER_REG_SOC;

    /*
     * =========================
     * R21 – R41
     * =========================
     */
    @Column(name = "R21_PRODUCT")
    private String R21_PRODUCT;
    @Column(name = "R21_BAL_SHEET_PUB_FS")
    private BigDecimal R21_BAL_SHEET_PUB_FS;
    @Column(name = "R21_UNDER_REG_SOC")
    private BigDecimal R21_UNDER_REG_SOC;

    @Column(name = "R23_PRODUCT")
    private String R23_PRODUCT;
    @Column(name = "R23_BAL_SHEET_PUB_FS")
    private BigDecimal R23_BAL_SHEET_PUB_FS;
    @Column(name = "R23_UNDER_REG_SOC")
    private BigDecimal R23_UNDER_REG_SOC;

    @Column(name = "R24_PRODUCT")
    private String R24_PRODUCT;
    @Column(name = "R24_BAL_SHEET_PUB_FS")
    private BigDecimal R24_BAL_SHEET_PUB_FS;
    @Column(name = "R24_UNDER_REG_SOC")
    private BigDecimal R24_UNDER_REG_SOC;

    @Column(name = "R25_PRODUCT")
    private String R25_PRODUCT;
    @Column(name = "R25_BAL_SHEET_PUB_FS")
    private BigDecimal R25_BAL_SHEET_PUB_FS;
    @Column(name = "R25_UNDER_REG_SOC")
    private BigDecimal R25_UNDER_REG_SOC;

    @Column(name = "R26_PRODUCT")
    private String R26_PRODUCT;
    @Column(name = "R26_BAL_SHEET_PUB_FS")
    private BigDecimal R26_BAL_SHEET_PUB_FS;
    @Column(name = "R26_UNDER_REG_SOC")
    private BigDecimal R26_UNDER_REG_SOC;

    @Column(name = "R27_PRODUCT")
    private String R27_PRODUCT;
    @Column(name = "R27_BAL_SHEET_PUB_FS")
    private BigDecimal R27_BAL_SHEET_PUB_FS;
    @Column(name = "R27_UNDER_REG_SOC")
    private BigDecimal R27_UNDER_REG_SOC;

    @Column(name = "R28_PRODUCT")
    private String R28_PRODUCT;
    @Column(name = "R28_BAL_SHEET_PUB_FS")
    private BigDecimal R28_BAL_SHEET_PUB_FS;
    @Column(name = "R28_UNDER_REG_SOC")
    private BigDecimal R28_UNDER_REG_SOC;

    @Column(name = "R29_PRODUCT")
    private String R29_PRODUCT;
    @Column(name = "R29_BAL_SHEET_PUB_FS")
    private BigDecimal R29_BAL_SHEET_PUB_FS;
    @Column(name = "R29_UNDER_REG_SOC")
    private BigDecimal R29_UNDER_REG_SOC;

    @Column(name = "R30_PRODUCT")
    private String R30_PRODUCT;
    @Column(name = "R30_BAL_SHEET_PUB_FS")
    private BigDecimal R30_BAL_SHEET_PUB_FS;
    @Column(name = "R30_UNDER_REG_SOC")
    private BigDecimal R30_UNDER_REG_SOC;

    @Column(name = "R31_PRODUCT")
    private String R31_PRODUCT;
    @Column(name = "R31_BAL_SHEET_PUB_FS")
    private BigDecimal R31_BAL_SHEET_PUB_FS;
    @Column(name = "R31_UNDER_REG_SOC")
    private BigDecimal R31_UNDER_REG_SOC;

    @Column(name = "R32_PRODUCT")
    private String R32_PRODUCT;
    @Column(name = "R32_BAL_SHEET_PUB_FS")
    private BigDecimal R32_BAL_SHEET_PUB_FS;
    @Column(name = "R32_UNDER_REG_SOC")
    private BigDecimal R32_UNDER_REG_SOC;

    @Column(name = "R33_PRODUCT")
    private String R33_PRODUCT;
    @Column(name = "R33_BAL_SHEET_PUB_FS")
    private BigDecimal R33_BAL_SHEET_PUB_FS;
    @Column(name = "R33_UNDER_REG_SOC")
    private BigDecimal R33_UNDER_REG_SOC;

    @Column(name = "R34_PRODUCT")
    private String R34_PRODUCT;
    @Column(name = "R34_BAL_SHEET_PUB_FS")
    private BigDecimal R34_BAL_SHEET_PUB_FS;
    @Column(name = "R34_UNDER_REG_SOC")
    private BigDecimal R34_UNDER_REG_SOC;

    @Column(name = "R35_PRODUCT")
    private String R35_PRODUCT;
    @Column(name = "R35_BAL_SHEET_PUB_FS")
    private BigDecimal R35_BAL_SHEET_PUB_FS;
    @Column(name = "R35_UNDER_REG_SOC")
    private BigDecimal R35_UNDER_REG_SOC;

    @Column(name = "R36_PRODUCT")
    private String R36_PRODUCT;
    @Column(name = "R36_BAL_SHEET_PUB_FS")
    private BigDecimal R36_BAL_SHEET_PUB_FS;
    @Column(name = "R36_UNDER_REG_SOC")
    private BigDecimal R36_UNDER_REG_SOC;

    @Column(name = "R38_PRODUCT")
    private String R38_PRODUCT;
    @Column(name = "R38_BAL_SHEET_PUB_FS")
    private BigDecimal R38_BAL_SHEET_PUB_FS;
    @Column(name = "R38_UNDER_REG_SOC")
    private BigDecimal R38_UNDER_REG_SOC;

    @Column(name = "R39_PRODUCT")
    private String R39_PRODUCT;
    @Column(name = "R39_BAL_SHEET_PUB_FS")
    private BigDecimal R39_BAL_SHEET_PUB_FS;
    @Column(name = "R39_UNDER_REG_SOC")
    private BigDecimal R39_UNDER_REG_SOC;

    @Column(name = "R40_PRODUCT")
    private String R40_PRODUCT;
    @Column(name = "R40_BAL_SHEET_PUB_FS")
    private BigDecimal R40_BAL_SHEET_PUB_FS;
    @Column(name = "R40_UNDER_REG_SOC")
    private BigDecimal R40_UNDER_REG_SOC;

    @Column(name = "R41_PRODUCT")
    private String R41_PRODUCT;
    @Column(name = "R41_BAL_SHEET_PUB_FS")
    private BigDecimal R41_BAL_SHEET_PUB_FS;
    @Column(name = "R41_UNDER_REG_SOC")
    private BigDecimal R41_UNDER_REG_SOC;

    /*
     * =========================
     * Metadata Columns
     * =========================
     */
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

    public Date getREPORT_DATE() {
        return REPORT_DATE;
    }

    public void setREPORT_DATE(Date rEPORT_DATE) {
        REPORT_DATE = rEPORT_DATE;
    }

    public String getR7_PRODUCT() {
        return R7_PRODUCT;
    }

    public void setR7_PRODUCT(String r7_PRODUCT) {
        R7_PRODUCT = r7_PRODUCT;
    }

    public BigDecimal getR7_BAL_SHEET_PUB_FS() {
        return R7_BAL_SHEET_PUB_FS;
    }

    public void setR7_BAL_SHEET_PUB_FS(BigDecimal r7_BAL_SHEET_PUB_FS) {
        R7_BAL_SHEET_PUB_FS = r7_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR7_UNDER_REG_SOC() {
        return R7_UNDER_REG_SOC;
    }

    public void setR7_UNDER_REG_SOC(BigDecimal r7_UNDER_REG_SOC) {
        R7_UNDER_REG_SOC = r7_UNDER_REG_SOC;
    }

    public String getR8_PRODUCT() {
        return R8_PRODUCT;
    }

    public void setR8_PRODUCT(String r8_PRODUCT) {
        R8_PRODUCT = r8_PRODUCT;
    }

    public BigDecimal getR8_BAL_SHEET_PUB_FS() {
        return R8_BAL_SHEET_PUB_FS;
    }

    public void setR8_BAL_SHEET_PUB_FS(BigDecimal r8_BAL_SHEET_PUB_FS) {
        R8_BAL_SHEET_PUB_FS = r8_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR8_UNDER_REG_SOC() {
        return R8_UNDER_REG_SOC;
    }

    public void setR8_UNDER_REG_SOC(BigDecimal r8_UNDER_REG_SOC) {
        R8_UNDER_REG_SOC = r8_UNDER_REG_SOC;
    }

    public String getR9_PRODUCT() {
        return R9_PRODUCT;
    }

    public void setR9_PRODUCT(String r9_PRODUCT) {
        R9_PRODUCT = r9_PRODUCT;
    }

    public BigDecimal getR9_BAL_SHEET_PUB_FS() {
        return R9_BAL_SHEET_PUB_FS;
    }

    public void setR9_BAL_SHEET_PUB_FS(BigDecimal r9_BAL_SHEET_PUB_FS) {
        R9_BAL_SHEET_PUB_FS = r9_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR9_UNDER_REG_SOC() {
        return R9_UNDER_REG_SOC;
    }

    public void setR9_UNDER_REG_SOC(BigDecimal r9_UNDER_REG_SOC) {
        R9_UNDER_REG_SOC = r9_UNDER_REG_SOC;
    }

    public String getR10_PRODUCT() {
        return R10_PRODUCT;
    }

    public void setR10_PRODUCT(String r10_PRODUCT) {
        R10_PRODUCT = r10_PRODUCT;
    }

    public BigDecimal getR10_BAL_SHEET_PUB_FS() {
        return R10_BAL_SHEET_PUB_FS;
    }

    public void setR10_BAL_SHEET_PUB_FS(BigDecimal r10_BAL_SHEET_PUB_FS) {
        R10_BAL_SHEET_PUB_FS = r10_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR10_UNDER_REG_SOC() {
        return R10_UNDER_REG_SOC;
    }

    public void setR10_UNDER_REG_SOC(BigDecimal r10_UNDER_REG_SOC) {
        R10_UNDER_REG_SOC = r10_UNDER_REG_SOC;
    }

    public String getR11_PRODUCT() {
        return R11_PRODUCT;
    }

    public void setR11_PRODUCT(String r11_PRODUCT) {
        R11_PRODUCT = r11_PRODUCT;
    }

    public BigDecimal getR11_BAL_SHEET_PUB_FS() {
        return R11_BAL_SHEET_PUB_FS;
    }

    public void setR11_BAL_SHEET_PUB_FS(BigDecimal r11_BAL_SHEET_PUB_FS) {
        R11_BAL_SHEET_PUB_FS = r11_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR11_UNDER_REG_SOC() {
        return R11_UNDER_REG_SOC;
    }

    public void setR11_UNDER_REG_SOC(BigDecimal r11_UNDER_REG_SOC) {
        R11_UNDER_REG_SOC = r11_UNDER_REG_SOC;
    }

    public String getR12_PRODUCT() {
        return R12_PRODUCT;
    }

    public void setR12_PRODUCT(String r12_PRODUCT) {
        R12_PRODUCT = r12_PRODUCT;
    }

    public BigDecimal getR12_BAL_SHEET_PUB_FS() {
        return R12_BAL_SHEET_PUB_FS;
    }

    public void setR12_BAL_SHEET_PUB_FS(BigDecimal r12_BAL_SHEET_PUB_FS) {
        R12_BAL_SHEET_PUB_FS = r12_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR12_UNDER_REG_SOC() {
        return R12_UNDER_REG_SOC;
    }

    public void setR12_UNDER_REG_SOC(BigDecimal r12_UNDER_REG_SOC) {
        R12_UNDER_REG_SOC = r12_UNDER_REG_SOC;
    }

    public String getR13_PRODUCT() {
        return R13_PRODUCT;
    }

    public void setR13_PRODUCT(String r13_PRODUCT) {
        R13_PRODUCT = r13_PRODUCT;
    }

    public BigDecimal getR13_BAL_SHEET_PUB_FS() {
        return R13_BAL_SHEET_PUB_FS;
    }

    public void setR13_BAL_SHEET_PUB_FS(BigDecimal r13_BAL_SHEET_PUB_FS) {
        R13_BAL_SHEET_PUB_FS = r13_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR13_UNDER_REG_SOC() {
        return R13_UNDER_REG_SOC;
    }

    public void setR13_UNDER_REG_SOC(BigDecimal r13_UNDER_REG_SOC) {
        R13_UNDER_REG_SOC = r13_UNDER_REG_SOC;
    }

    public String getR14_PRODUCT() {
        return R14_PRODUCT;
    }

    public void setR14_PRODUCT(String r14_PRODUCT) {
        R14_PRODUCT = r14_PRODUCT;
    }

    public BigDecimal getR14_BAL_SHEET_PUB_FS() {
        return R14_BAL_SHEET_PUB_FS;
    }

    public void setR14_BAL_SHEET_PUB_FS(BigDecimal r14_BAL_SHEET_PUB_FS) {
        R14_BAL_SHEET_PUB_FS = r14_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR14_UNDER_REG_SOC() {
        return R14_UNDER_REG_SOC;
    }

    public void setR14_UNDER_REG_SOC(BigDecimal r14_UNDER_REG_SOC) {
        R14_UNDER_REG_SOC = r14_UNDER_REG_SOC;
    }

    public String getR15_PRODUCT() {
        return R15_PRODUCT;
    }

    public void setR15_PRODUCT(String r15_PRODUCT) {
        R15_PRODUCT = r15_PRODUCT;
    }

    public BigDecimal getR15_BAL_SHEET_PUB_FS() {
        return R15_BAL_SHEET_PUB_FS;
    }

    public void setR15_BAL_SHEET_PUB_FS(BigDecimal r15_BAL_SHEET_PUB_FS) {
        R15_BAL_SHEET_PUB_FS = r15_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR15_UNDER_REG_SOC() {
        return R15_UNDER_REG_SOC;
    }

    public void setR15_UNDER_REG_SOC(BigDecimal r15_UNDER_REG_SOC) {
        R15_UNDER_REG_SOC = r15_UNDER_REG_SOC;
    }

    public String getR16_PRODUCT() {
        return R16_PRODUCT;
    }

    public void setR16_PRODUCT(String r16_PRODUCT) {
        R16_PRODUCT = r16_PRODUCT;
    }

    public BigDecimal getR16_BAL_SHEET_PUB_FS() {
        return R16_BAL_SHEET_PUB_FS;
    }

    public void setR16_BAL_SHEET_PUB_FS(BigDecimal r16_BAL_SHEET_PUB_FS) {
        R16_BAL_SHEET_PUB_FS = r16_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR16_UNDER_REG_SOC() {
        return R16_UNDER_REG_SOC;
    }

    public void setR16_UNDER_REG_SOC(BigDecimal r16_UNDER_REG_SOC) {
        R16_UNDER_REG_SOC = r16_UNDER_REG_SOC;
    }

    public String getR17_PRODUCT() {
        return R17_PRODUCT;
    }

    public void setR17_PRODUCT(String r17_PRODUCT) {
        R17_PRODUCT = r17_PRODUCT;
    }

    public BigDecimal getR17_BAL_SHEET_PUB_FS() {
        return R17_BAL_SHEET_PUB_FS;
    }

    public void setR17_BAL_SHEET_PUB_FS(BigDecimal r17_BAL_SHEET_PUB_FS) {
        R17_BAL_SHEET_PUB_FS = r17_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR17_UNDER_REG_SOC() {
        return R17_UNDER_REG_SOC;
    }

    public void setR17_UNDER_REG_SOC(BigDecimal r17_UNDER_REG_SOC) {
        R17_UNDER_REG_SOC = r17_UNDER_REG_SOC;
    }

    public String getR18_PRODUCT() {
        return R18_PRODUCT;
    }

    public void setR18_PRODUCT(String r18_PRODUCT) {
        R18_PRODUCT = r18_PRODUCT;
    }

    public BigDecimal getR18_BAL_SHEET_PUB_FS() {
        return R18_BAL_SHEET_PUB_FS;
    }

    public void setR18_BAL_SHEET_PUB_FS(BigDecimal r18_BAL_SHEET_PUB_FS) {
        R18_BAL_SHEET_PUB_FS = r18_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR18_UNDER_REG_SOC() {
        return R18_UNDER_REG_SOC;
    }

    public void setR18_UNDER_REG_SOC(BigDecimal r18_UNDER_REG_SOC) {
        R18_UNDER_REG_SOC = r18_UNDER_REG_SOC;
    }

    public String getR19_PRODUCT() {
        return R19_PRODUCT;
    }

    public void setR19_PRODUCT(String r19_PRODUCT) {
        R19_PRODUCT = r19_PRODUCT;
    }

    public BigDecimal getR19_BAL_SHEET_PUB_FS() {
        return R19_BAL_SHEET_PUB_FS;
    }

    public void setR19_BAL_SHEET_PUB_FS(BigDecimal r19_BAL_SHEET_PUB_FS) {
        R19_BAL_SHEET_PUB_FS = r19_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR19_UNDER_REG_SOC() {
        return R19_UNDER_REG_SOC;
    }

    public void setR19_UNDER_REG_SOC(BigDecimal r19_UNDER_REG_SOC) {
        R19_UNDER_REG_SOC = r19_UNDER_REG_SOC;
    }

    public String getR20_PRODUCT() {
        return R20_PRODUCT;
    }

    public void setR20_PRODUCT(String r20_PRODUCT) {
        R20_PRODUCT = r20_PRODUCT;
    }

    public BigDecimal getR20_BAL_SHEET_PUB_FS() {
        return R20_BAL_SHEET_PUB_FS;
    }

    public void setR20_BAL_SHEET_PUB_FS(BigDecimal r20_BAL_SHEET_PUB_FS) {
        R20_BAL_SHEET_PUB_FS = r20_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR20_UNDER_REG_SOC() {
        return R20_UNDER_REG_SOC;
    }

    public void setR20_UNDER_REG_SOC(BigDecimal r20_UNDER_REG_SOC) {
        R20_UNDER_REG_SOC = r20_UNDER_REG_SOC;
    }

    public String getR21_PRODUCT() {
        return R21_PRODUCT;
    }

    public void setR21_PRODUCT(String r21_PRODUCT) {
        R21_PRODUCT = r21_PRODUCT;
    }

    public BigDecimal getR21_BAL_SHEET_PUB_FS() {
        return R21_BAL_SHEET_PUB_FS;
    }

    public void setR21_BAL_SHEET_PUB_FS(BigDecimal r21_BAL_SHEET_PUB_FS) {
        R21_BAL_SHEET_PUB_FS = r21_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR21_UNDER_REG_SOC() {
        return R21_UNDER_REG_SOC;
    }

    public void setR21_UNDER_REG_SOC(BigDecimal r21_UNDER_REG_SOC) {
        R21_UNDER_REG_SOC = r21_UNDER_REG_SOC;
    }

    public String getR23_PRODUCT() {
        return R23_PRODUCT;
    }

    public void setR23_PRODUCT(String r23_PRODUCT) {
        R23_PRODUCT = r23_PRODUCT;
    }

    public BigDecimal getR23_BAL_SHEET_PUB_FS() {
        return R23_BAL_SHEET_PUB_FS;
    }

    public void setR23_BAL_SHEET_PUB_FS(BigDecimal r23_BAL_SHEET_PUB_FS) {
        R23_BAL_SHEET_PUB_FS = r23_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR23_UNDER_REG_SOC() {
        return R23_UNDER_REG_SOC;
    }

    public void setR23_UNDER_REG_SOC(BigDecimal r23_UNDER_REG_SOC) {
        R23_UNDER_REG_SOC = r23_UNDER_REG_SOC;
    }

    public String getR24_PRODUCT() {
        return R24_PRODUCT;
    }

    public void setR24_PRODUCT(String r24_PRODUCT) {
        R24_PRODUCT = r24_PRODUCT;
    }

    public BigDecimal getR24_BAL_SHEET_PUB_FS() {
        return R24_BAL_SHEET_PUB_FS;
    }

    public void setR24_BAL_SHEET_PUB_FS(BigDecimal r24_BAL_SHEET_PUB_FS) {
        R24_BAL_SHEET_PUB_FS = r24_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR24_UNDER_REG_SOC() {
        return R24_UNDER_REG_SOC;
    }

    public void setR24_UNDER_REG_SOC(BigDecimal r24_UNDER_REG_SOC) {
        R24_UNDER_REG_SOC = r24_UNDER_REG_SOC;
    }

    public String getR25_PRODUCT() {
        return R25_PRODUCT;
    }

    public void setR25_PRODUCT(String r25_PRODUCT) {
        R25_PRODUCT = r25_PRODUCT;
    }

    public BigDecimal getR25_BAL_SHEET_PUB_FS() {
        return R25_BAL_SHEET_PUB_FS;
    }

    public void setR25_BAL_SHEET_PUB_FS(BigDecimal r25_BAL_SHEET_PUB_FS) {
        R25_BAL_SHEET_PUB_FS = r25_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR25_UNDER_REG_SOC() {
        return R25_UNDER_REG_SOC;
    }

    public void setR25_UNDER_REG_SOC(BigDecimal r25_UNDER_REG_SOC) {
        R25_UNDER_REG_SOC = r25_UNDER_REG_SOC;
    }

    public String getR26_PRODUCT() {
        return R26_PRODUCT;
    }

    public void setR26_PRODUCT(String r26_PRODUCT) {
        R26_PRODUCT = r26_PRODUCT;
    }

    public BigDecimal getR26_BAL_SHEET_PUB_FS() {
        return R26_BAL_SHEET_PUB_FS;
    }

    public void setR26_BAL_SHEET_PUB_FS(BigDecimal r26_BAL_SHEET_PUB_FS) {
        R26_BAL_SHEET_PUB_FS = r26_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR26_UNDER_REG_SOC() {
        return R26_UNDER_REG_SOC;
    }

    public void setR26_UNDER_REG_SOC(BigDecimal r26_UNDER_REG_SOC) {
        R26_UNDER_REG_SOC = r26_UNDER_REG_SOC;
    }

    public String getR27_PRODUCT() {
        return R27_PRODUCT;
    }

    public void setR27_PRODUCT(String r27_PRODUCT) {
        R27_PRODUCT = r27_PRODUCT;
    }

    public BigDecimal getR27_BAL_SHEET_PUB_FS() {
        return R27_BAL_SHEET_PUB_FS;
    }

    public void setR27_BAL_SHEET_PUB_FS(BigDecimal r27_BAL_SHEET_PUB_FS) {
        R27_BAL_SHEET_PUB_FS = r27_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR27_UNDER_REG_SOC() {
        return R27_UNDER_REG_SOC;
    }

    public void setR27_UNDER_REG_SOC(BigDecimal r27_UNDER_REG_SOC) {
        R27_UNDER_REG_SOC = r27_UNDER_REG_SOC;
    }

    public String getR28_PRODUCT() {
        return R28_PRODUCT;
    }

    public void setR28_PRODUCT(String r28_PRODUCT) {
        R28_PRODUCT = r28_PRODUCT;
    }

    public BigDecimal getR28_BAL_SHEET_PUB_FS() {
        return R28_BAL_SHEET_PUB_FS;
    }

    public void setR28_BAL_SHEET_PUB_FS(BigDecimal r28_BAL_SHEET_PUB_FS) {
        R28_BAL_SHEET_PUB_FS = r28_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR28_UNDER_REG_SOC() {
        return R28_UNDER_REG_SOC;
    }

    public void setR28_UNDER_REG_SOC(BigDecimal r28_UNDER_REG_SOC) {
        R28_UNDER_REG_SOC = r28_UNDER_REG_SOC;
    }

    public String getR29_PRODUCT() {
        return R29_PRODUCT;
    }

    public void setR29_PRODUCT(String r29_PRODUCT) {
        R29_PRODUCT = r29_PRODUCT;
    }

    public BigDecimal getR29_BAL_SHEET_PUB_FS() {
        return R29_BAL_SHEET_PUB_FS;
    }

    public void setR29_BAL_SHEET_PUB_FS(BigDecimal r29_BAL_SHEET_PUB_FS) {
        R29_BAL_SHEET_PUB_FS = r29_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR29_UNDER_REG_SOC() {
        return R29_UNDER_REG_SOC;
    }

    public void setR29_UNDER_REG_SOC(BigDecimal r29_UNDER_REG_SOC) {
        R29_UNDER_REG_SOC = r29_UNDER_REG_SOC;
    }

    public String getR30_PRODUCT() {
        return R30_PRODUCT;
    }

    public void setR30_PRODUCT(String r30_PRODUCT) {
        R30_PRODUCT = r30_PRODUCT;
    }

    public BigDecimal getR30_BAL_SHEET_PUB_FS() {
        return R30_BAL_SHEET_PUB_FS;
    }

    public void setR30_BAL_SHEET_PUB_FS(BigDecimal r30_BAL_SHEET_PUB_FS) {
        R30_BAL_SHEET_PUB_FS = r30_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR30_UNDER_REG_SOC() {
        return R30_UNDER_REG_SOC;
    }

    public void setR30_UNDER_REG_SOC(BigDecimal r30_UNDER_REG_SOC) {
        R30_UNDER_REG_SOC = r30_UNDER_REG_SOC;
    }

    public String getR31_PRODUCT() {
        return R31_PRODUCT;
    }

    public void setR31_PRODUCT(String r31_PRODUCT) {
        R31_PRODUCT = r31_PRODUCT;
    }

    public BigDecimal getR31_BAL_SHEET_PUB_FS() {
        return R31_BAL_SHEET_PUB_FS;
    }

    public void setR31_BAL_SHEET_PUB_FS(BigDecimal r31_BAL_SHEET_PUB_FS) {
        R31_BAL_SHEET_PUB_FS = r31_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR31_UNDER_REG_SOC() {
        return R31_UNDER_REG_SOC;
    }

    public void setR31_UNDER_REG_SOC(BigDecimal r31_UNDER_REG_SOC) {
        R31_UNDER_REG_SOC = r31_UNDER_REG_SOC;
    }

    public String getR32_PRODUCT() {
        return R32_PRODUCT;
    }

    public void setR32_PRODUCT(String r32_PRODUCT) {
        R32_PRODUCT = r32_PRODUCT;
    }

    public BigDecimal getR32_BAL_SHEET_PUB_FS() {
        return R32_BAL_SHEET_PUB_FS;
    }

    public void setR32_BAL_SHEET_PUB_FS(BigDecimal r32_BAL_SHEET_PUB_FS) {
        R32_BAL_SHEET_PUB_FS = r32_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR32_UNDER_REG_SOC() {
        return R32_UNDER_REG_SOC;
    }

    public void setR32_UNDER_REG_SOC(BigDecimal r32_UNDER_REG_SOC) {
        R32_UNDER_REG_SOC = r32_UNDER_REG_SOC;
    }

    public String getR33_PRODUCT() {
        return R33_PRODUCT;
    }

    public void setR33_PRODUCT(String r33_PRODUCT) {
        R33_PRODUCT = r33_PRODUCT;
    }

    public BigDecimal getR33_BAL_SHEET_PUB_FS() {
        return R33_BAL_SHEET_PUB_FS;
    }

    public void setR33_BAL_SHEET_PUB_FS(BigDecimal r33_BAL_SHEET_PUB_FS) {
        R33_BAL_SHEET_PUB_FS = r33_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR33_UNDER_REG_SOC() {
        return R33_UNDER_REG_SOC;
    }

    public void setR33_UNDER_REG_SOC(BigDecimal r33_UNDER_REG_SOC) {
        R33_UNDER_REG_SOC = r33_UNDER_REG_SOC;
    }

    public String getR34_PRODUCT() {
        return R34_PRODUCT;
    }

    public void setR34_PRODUCT(String r34_PRODUCT) {
        R34_PRODUCT = r34_PRODUCT;
    }

    public BigDecimal getR34_BAL_SHEET_PUB_FS() {
        return R34_BAL_SHEET_PUB_FS;
    }

    public void setR34_BAL_SHEET_PUB_FS(BigDecimal r34_BAL_SHEET_PUB_FS) {
        R34_BAL_SHEET_PUB_FS = r34_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR34_UNDER_REG_SOC() {
        return R34_UNDER_REG_SOC;
    }

    public void setR34_UNDER_REG_SOC(BigDecimal r34_UNDER_REG_SOC) {
        R34_UNDER_REG_SOC = r34_UNDER_REG_SOC;
    }

    public String getR35_PRODUCT() {
        return R35_PRODUCT;
    }

    public void setR35_PRODUCT(String r35_PRODUCT) {
        R35_PRODUCT = r35_PRODUCT;
    }

    public BigDecimal getR35_BAL_SHEET_PUB_FS() {
        return R35_BAL_SHEET_PUB_FS;
    }

    public void setR35_BAL_SHEET_PUB_FS(BigDecimal r35_BAL_SHEET_PUB_FS) {
        R35_BAL_SHEET_PUB_FS = r35_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR35_UNDER_REG_SOC() {
        return R35_UNDER_REG_SOC;
    }

    public void setR35_UNDER_REG_SOC(BigDecimal r35_UNDER_REG_SOC) {
        R35_UNDER_REG_SOC = r35_UNDER_REG_SOC;
    }

    public String getR36_PRODUCT() {
        return R36_PRODUCT;
    }

    public void setR36_PRODUCT(String r36_PRODUCT) {
        R36_PRODUCT = r36_PRODUCT;
    }

    public BigDecimal getR36_BAL_SHEET_PUB_FS() {
        return R36_BAL_SHEET_PUB_FS;
    }

    public void setR36_BAL_SHEET_PUB_FS(BigDecimal r36_BAL_SHEET_PUB_FS) {
        R36_BAL_SHEET_PUB_FS = r36_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR36_UNDER_REG_SOC() {
        return R36_UNDER_REG_SOC;
    }

    public void setR36_UNDER_REG_SOC(BigDecimal r36_UNDER_REG_SOC) {
        R36_UNDER_REG_SOC = r36_UNDER_REG_SOC;
    }

    public String getR38_PRODUCT() {
        return R38_PRODUCT;
    }

    public void setR38_PRODUCT(String r38_PRODUCT) {
        R38_PRODUCT = r38_PRODUCT;
    }

    public BigDecimal getR38_BAL_SHEET_PUB_FS() {
        return R38_BAL_SHEET_PUB_FS;
    }

    public void setR38_BAL_SHEET_PUB_FS(BigDecimal r38_BAL_SHEET_PUB_FS) {
        R38_BAL_SHEET_PUB_FS = r38_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR38_UNDER_REG_SOC() {
        return R38_UNDER_REG_SOC;
    }

    public void setR38_UNDER_REG_SOC(BigDecimal r38_UNDER_REG_SOC) {
        R38_UNDER_REG_SOC = r38_UNDER_REG_SOC;
    }

    public String getR39_PRODUCT() {
        return R39_PRODUCT;
    }

    public void setR39_PRODUCT(String r39_PRODUCT) {
        R39_PRODUCT = r39_PRODUCT;
    }

    public BigDecimal getR39_BAL_SHEET_PUB_FS() {
        return R39_BAL_SHEET_PUB_FS;
    }

    public void setR39_BAL_SHEET_PUB_FS(BigDecimal r39_BAL_SHEET_PUB_FS) {
        R39_BAL_SHEET_PUB_FS = r39_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR39_UNDER_REG_SOC() {
        return R39_UNDER_REG_SOC;
    }

    public void setR39_UNDER_REG_SOC(BigDecimal r39_UNDER_REG_SOC) {
        R39_UNDER_REG_SOC = r39_UNDER_REG_SOC;
    }

    public String getR40_PRODUCT() {
        return R40_PRODUCT;
    }

    public void setR40_PRODUCT(String r40_PRODUCT) {
        R40_PRODUCT = r40_PRODUCT;
    }

    public BigDecimal getR40_BAL_SHEET_PUB_FS() {
        return R40_BAL_SHEET_PUB_FS;
    }

    public void setR40_BAL_SHEET_PUB_FS(BigDecimal r40_BAL_SHEET_PUB_FS) {
        R40_BAL_SHEET_PUB_FS = r40_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR40_UNDER_REG_SOC() {
        return R40_UNDER_REG_SOC;
    }

    public void setR40_UNDER_REG_SOC(BigDecimal r40_UNDER_REG_SOC) {
        R40_UNDER_REG_SOC = r40_UNDER_REG_SOC;
    }

    public String getR41_PRODUCT() {
        return R41_PRODUCT;
    }

    public void setR41_PRODUCT(String r41_PRODUCT) {
        R41_PRODUCT = r41_PRODUCT;
    }

    public BigDecimal getR41_BAL_SHEET_PUB_FS() {
        return R41_BAL_SHEET_PUB_FS;
    }

    public void setR41_BAL_SHEET_PUB_FS(BigDecimal r41_BAL_SHEET_PUB_FS) {
        R41_BAL_SHEET_PUB_FS = r41_BAL_SHEET_PUB_FS;
    }

    public BigDecimal getR41_UNDER_REG_SOC() {
        return R41_UNDER_REG_SOC;
    }

    public void setR41_UNDER_REG_SOC(BigDecimal r41_UNDER_REG_SOC) {
        R41_UNDER_REG_SOC = r41_UNDER_REG_SOC;
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

    public Recon_Of_FS_Summary_Entity() {
        super();
    }

    
}
