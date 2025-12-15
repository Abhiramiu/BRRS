package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_MARKET_RISK_SUMMARYTABLE")
public class Market_Risk_Summary_Entity {

    /* ================= R5 ================= */

    @Column(name = "R5_QUALITATIVE_DISCLOSURES")
    private String R5_QUALITATIVE_DISCLOSURES;

    @Column(name = "R5_PRODUCT")
    private String R5_PRODUCT;

    @Column(name = "R5_QUAN_DIS")
    private BigDecimal R5_QUAN_DIS;

    /* ================= R6 ================= */

    @Column(name = "R6_QUALITATIVE_DISCLOSURES")
    private String R6_QUALITATIVE_DISCLOSURES;

    @Column(name = "R6_PRODUCT")
    private String R6_PRODUCT;

    @Column(name = "R6_QUAN_DIS")
    private BigDecimal R6_QUAN_DIS;

    /* ================= R11 ================= */

    @Column(name = "R11_QUALITATIVE_DISCLOSURES")
    private String R11_QUALITATIVE_DISCLOSURES;

    @Column(name = "R11_PRODUCT")
    private String R11_PRODUCT;

    @Column(name = "R11_QUAN_DIS")
    private BigDecimal R11_QUAN_DIS;

    /* ================= R12 ================= */

    @Column(name = "R12_QUALITATIVE_DISCLOSURES")
    private String R12_QUALITATIVE_DISCLOSURES;

    @Column(name = "R12_PRODUCT")
    private String R12_PRODUCT;

    @Column(name = "R12_QUAN_DIS")
    private BigDecimal R12_QUAN_DIS;

    /* ================= R13 ================= */

    @Column(name = "R13_QUALITATIVE_DISCLOSURES")
    private String R13_QUALITATIVE_DISCLOSURES;

    @Column(name = "R13_PRODUCT")
    private String R13_PRODUCT;

    @Column(name = "R13_QUAN_DIS")
    private BigDecimal R13_QUAN_DIS;

    /* ================= R14 ================= */

    @Column(name = "R14_QUALITATIVE_DISCLOSURES")
    private String R14_QUALITATIVE_DISCLOSURES;

    @Column(name = "R14_PRODUCT")
    private String R14_PRODUCT;

    @Column(name = "R14_QUAN_DIS")
    private BigDecimal R14_QUAN_DIS;

    /* ================= R15 ================= */

    @Column(name = "R15_QUALITATIVE_DISCLOSURES")
    private String R15_QUALITATIVE_DISCLOSURES;

    @Column(name = "R15_PRODUCT")
    private String R15_PRODUCT;

    @Column(name = "R15_QUAN_DIS")
    private BigDecimal R15_QUAN_DIS;

    /* ================= R16 ================= */

    @Column(name = "R16_QUALITATIVE_DISCLOSURES")
    private String R16_QUALITATIVE_DISCLOSURES;

    @Column(name = "R16_PRODUCT")
    private String R16_PRODUCT;

    @Column(name = "R16_QUAN_DIS")
    private BigDecimal R16_QUAN_DIS;

    /* ================= R20 ================= */

    @Column(name = "R20_QUALITATIVE_DISCLOSURES")
    private String R20_QUALITATIVE_DISCLOSURES;

    @Column(name = "R20_PRODUCT")
    private String R20_PRODUCT;

    @Column(name = "R20_QUAN_DIS")
    private BigDecimal R20_QUAN_DIS;
    /* ================= R21 ================= */
    @Column(name = "R21_QUALITATIVE_DISCLOSURES")
    private String R21_QUALITATIVE_DISCLOSURES;
    
    @Column(name = "R21_PRODUCT")
    private String R21_PRODUCT;

    @Column(name = "R21_QUAN_DIS")
    private BigDecimal R21_QUAN_DIS;

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

    /* ================= CONSTRUCTOR ================= */

    public Market_Risk_Summary_Entity() {
        super();
    }

    public String getR5_QUALITATIVE_DISCLOSURES() {
        return R5_QUALITATIVE_DISCLOSURES;
    }

    public void setR5_QUALITATIVE_DISCLOSURES(String r5_QUALITATIVE_DISCLOSURES) {
        R5_QUALITATIVE_DISCLOSURES = r5_QUALITATIVE_DISCLOSURES;
    }

    public String getR5_PRODUCT() {
        return R5_PRODUCT;
    }

    public void setR5_PRODUCT(String r5_PRODUCT) {
        R5_PRODUCT = r5_PRODUCT;
    }

    public BigDecimal getR5_QUAN_DIS() {
        return R5_QUAN_DIS;
    }

    public void setR5_QUAN_DIS(BigDecimal r5_QUAN_DIS) {
        R5_QUAN_DIS = r5_QUAN_DIS;
    }

    public String getR6_QUALITATIVE_DISCLOSURES() {
        return R6_QUALITATIVE_DISCLOSURES;
    }

    public void setR6_QUALITATIVE_DISCLOSURES(String r6_QUALITATIVE_DISCLOSURES) {
        R6_QUALITATIVE_DISCLOSURES = r6_QUALITATIVE_DISCLOSURES;
    }

    public String getR6_PRODUCT() {
        return R6_PRODUCT;
    }

    public void setR6_PRODUCT(String r6_PRODUCT) {
        R6_PRODUCT = r6_PRODUCT;
    }

    public BigDecimal getR6_QUAN_DIS() {
        return R6_QUAN_DIS;
    }

    public void setR6_QUAN_DIS(BigDecimal r6_QUAN_DIS) {
        R6_QUAN_DIS = r6_QUAN_DIS;
    }

    public String getR11_QUALITATIVE_DISCLOSURES() {
        return R11_QUALITATIVE_DISCLOSURES;
    }

    public void setR11_QUALITATIVE_DISCLOSURES(String r11_QUALITATIVE_DISCLOSURES) {
        R11_QUALITATIVE_DISCLOSURES = r11_QUALITATIVE_DISCLOSURES;
    }

    public String getR11_PRODUCT() {
        return R11_PRODUCT;
    }

    public void setR11_PRODUCT(String r11_PRODUCT) {
        R11_PRODUCT = r11_PRODUCT;
    }

    public BigDecimal getR11_QUAN_DIS() {
        return R11_QUAN_DIS;
    }

    public void setR11_QUAN_DIS(BigDecimal r11_QUAN_DIS) {
        R11_QUAN_DIS = r11_QUAN_DIS;
    }

    public String getR12_QUALITATIVE_DISCLOSURES() {
        return R12_QUALITATIVE_DISCLOSURES;
    }

    public void setR12_QUALITATIVE_DISCLOSURES(String r12_QUALITATIVE_DISCLOSURES) {
        R12_QUALITATIVE_DISCLOSURES = r12_QUALITATIVE_DISCLOSURES;
    }

    public String getR12_PRODUCT() {
        return R12_PRODUCT;
    }

    public void setR12_PRODUCT(String r12_PRODUCT) {
        R12_PRODUCT = r12_PRODUCT;
    }

    public BigDecimal getR12_QUAN_DIS() {
        return R12_QUAN_DIS;
    }

    public void setR12_QUAN_DIS(BigDecimal r12_QUAN_DIS) {
        R12_QUAN_DIS = r12_QUAN_DIS;
    }

    public String getR13_QUALITATIVE_DISCLOSURES() {
        return R13_QUALITATIVE_DISCLOSURES;
    }

    public void setR13_QUALITATIVE_DISCLOSURES(String r13_QUALITATIVE_DISCLOSURES) {
        R13_QUALITATIVE_DISCLOSURES = r13_QUALITATIVE_DISCLOSURES;
    }

    public String getR13_PRODUCT() {
        return R13_PRODUCT;
    }

    public void setR13_PRODUCT(String r13_PRODUCT) {
        R13_PRODUCT = r13_PRODUCT;
    }

    public BigDecimal getR13_QUAN_DIS() {
        return R13_QUAN_DIS;
    }

    public void setR13_QUAN_DIS(BigDecimal r13_QUAN_DIS) {
        R13_QUAN_DIS = r13_QUAN_DIS;
    }

    public String getR14_QUALITATIVE_DISCLOSURES() {
        return R14_QUALITATIVE_DISCLOSURES;
    }

    public void setR14_QUALITATIVE_DISCLOSURES(String r14_QUALITATIVE_DISCLOSURES) {
        R14_QUALITATIVE_DISCLOSURES = r14_QUALITATIVE_DISCLOSURES;
    }

    public String getR14_PRODUCT() {
        return R14_PRODUCT;
    }

    public void setR14_PRODUCT(String r14_PRODUCT) {
        R14_PRODUCT = r14_PRODUCT;
    }

    public BigDecimal getR14_QUAN_DIS() {
        return R14_QUAN_DIS;
    }

    public void setR14_QUAN_DIS(BigDecimal r14_QUAN_DIS) {
        R14_QUAN_DIS = r14_QUAN_DIS;
    }

    public String getR15_QUALITATIVE_DISCLOSURES() {
        return R15_QUALITATIVE_DISCLOSURES;
    }

    public void setR15_QUALITATIVE_DISCLOSURES(String r15_QUALITATIVE_DISCLOSURES) {
        R15_QUALITATIVE_DISCLOSURES = r15_QUALITATIVE_DISCLOSURES;
    }

    public String getR15_PRODUCT() {
        return R15_PRODUCT;
    }

    public void setR15_PRODUCT(String r15_PRODUCT) {
        R15_PRODUCT = r15_PRODUCT;
    }

    public BigDecimal getR15_QUAN_DIS() {
        return R15_QUAN_DIS;
    }

    public void setR15_QUAN_DIS(BigDecimal r15_QUAN_DIS) {
        R15_QUAN_DIS = r15_QUAN_DIS;
    }

    public String getR16_QUALITATIVE_DISCLOSURES() {
        return R16_QUALITATIVE_DISCLOSURES;
    }

    public void setR16_QUALITATIVE_DISCLOSURES(String r16_QUALITATIVE_DISCLOSURES) {
        R16_QUALITATIVE_DISCLOSURES = r16_QUALITATIVE_DISCLOSURES;
    }

    public String getR16_PRODUCT() {
        return R16_PRODUCT;
    }

    public void setR16_PRODUCT(String r16_PRODUCT) {
        R16_PRODUCT = r16_PRODUCT;
    }

    public BigDecimal getR16_QUAN_DIS() {
        return R16_QUAN_DIS;
    }

    public void setR16_QUAN_DIS(BigDecimal r16_QUAN_DIS) {
        R16_QUAN_DIS = r16_QUAN_DIS;
    }

    public String getR20_QUALITATIVE_DISCLOSURES() {
        return R20_QUALITATIVE_DISCLOSURES;
    }

    public void setR20_QUALITATIVE_DISCLOSURES(String r20_QUALITATIVE_DISCLOSURES) {
        R20_QUALITATIVE_DISCLOSURES = r20_QUALITATIVE_DISCLOSURES;
    }

    public String getR20_PRODUCT() {
        return R20_PRODUCT;
    }

    public void setR20_PRODUCT(String r20_PRODUCT) {
        R20_PRODUCT = r20_PRODUCT;
    }

    public String getR21_PRODUCT() {
        return R21_PRODUCT;
    }

    public void setR21_PRODUCT(String r21_PRODUCT) {
        R21_PRODUCT = r21_PRODUCT;
    }

    public BigDecimal getR21_QUAN_DIS() {
        return R21_QUAN_DIS;
    }

    public void setR21_QUAN_DIS(BigDecimal r21_QUAN_DIS) {
        R21_QUAN_DIS = r21_QUAN_DIS;
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

    public BigDecimal getR20_QUAN_DIS() {
        return R20_QUAN_DIS;
    }

    public void setR20_QUAN_DIS(BigDecimal r20_QUAN_DIS) {
        R20_QUAN_DIS = r20_QUAN_DIS;
    }

    public String getR21_QUALITATIVE_DISCLOSURES() {
        return R21_QUALITATIVE_DISCLOSURES;
    }

    public void setR21_QUALITATIVE_DISCLOSURES(String r21_QUALITATIVE_DISCLOSURES) {
        R21_QUALITATIVE_DISCLOSURES = r21_QUALITATIVE_DISCLOSURES;
    }
    
    
}
