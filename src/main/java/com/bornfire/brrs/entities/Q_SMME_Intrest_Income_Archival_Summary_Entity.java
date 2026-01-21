
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
@Table(name = "BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY")
public class Q_SMME_Intrest_Income_Archival_Summary_Entity {

    private String r15_caoin;
    private BigDecimal r15_res_carry_amt;
    private BigDecimal r15_non_res_carry_amt;
    private String r16_caoin;
    private BigDecimal r16_res_carry_amt;
    private BigDecimal r16_non_res_carry_amt;
    private String r17_caoin;
    private BigDecimal r17_res_carry_amt;
    private BigDecimal r17_non_res_carry_amt;
    private String r18_caoin;
    private BigDecimal r18_res_carry_amt;
    private BigDecimal r18_non_res_carry_amt;
    private String r19_caoin;
    private BigDecimal r19_res_carry_amt;
    private BigDecimal r19_non_res_carry_amt;
    private String r20_caoin;
    private BigDecimal r20_res_carry_amt;
    private BigDecimal r20_non_res_carry_amt;
    private String r21_caoin;
    private BigDecimal r21_res_carry_amt;
    private BigDecimal r21_non_res_carry_amt;
    private String r22_caoin;
    private BigDecimal r22_res_carry_amt;
    private BigDecimal r22_non_res_carry_amt;
    private String r23_caoin;
    private BigDecimal r23_res_carry_amt;
    private BigDecimal r23_non_res_carry_amt;
    private String r24_caoin;
    private BigDecimal r24_res_carry_amt;
    private BigDecimal r24_non_res_carry_amt;
    private String r25_caoin;
    private BigDecimal r25_res_carry_amt;
    private BigDecimal r25_non_res_carry_amt;
    private String r26_caoin;
    private BigDecimal r26_res_carry_amt;
    private BigDecimal r26_non_res_carry_amt;
    private String r27_caoin;
    private BigDecimal r27_res_carry_amt;
    private BigDecimal r27_non_res_carry_amt;
    private String r28_caoin;
    private BigDecimal r28_res_carry_amt;
    private BigDecimal r28_non_res_carry_amt;
    private String r29_caoin;
    private BigDecimal r29_res_carry_amt;
    private BigDecimal r29_non_res_carry_amt;
    private String r30_caoin;
    private BigDecimal r30_res_carry_amt;
    private BigDecimal r30_non_res_carry_amt;
    private String r31_caoin;
    private BigDecimal r31_res_carry_amt;
    private BigDecimal r31_non_res_carry_amt;
    private String r32_caoin;
    private BigDecimal r32_res_carry_amt;
    private BigDecimal r32_non_res_carry_amt;
    private String r33_caoin;
    private BigDecimal r33_res_carry_amt;
    private BigDecimal r33_non_res_carry_amt;
    private String r34_caoin;
    private BigDecimal r34_res_carry_amt;
    private BigDecimal r34_non_res_carry_amt;
    private String r35_caoin;
    private BigDecimal r35_res_carry_amt;
    private BigDecimal r35_non_res_carry_amt;
    private String r36_caoin;
    private BigDecimal r36_res_carry_amt;
    private BigDecimal r36_non_res_carry_amt;
    private String r37_caoin;
    private BigDecimal r37_res_carry_amt;
    private BigDecimal r37_non_res_carry_amt;
    private String r38_caoin;
    private BigDecimal r38_res_carry_amt;
    private BigDecimal r38_non_res_carry_amt;
    private String r39_caoin;
    private BigDecimal r39_res_carry_amt;
    private BigDecimal r39_non_res_carry_amt;
    private String r40_caoin;
    private BigDecimal r40_res_carry_amt;
    private BigDecimal r40_non_res_carry_amt;
    private String r41_caoin;
    private BigDecimal r41_res_carry_amt;
    private BigDecimal r41_non_res_carry_amt;
    private String r42_caoin;
    private BigDecimal r42_res_carry_amt;
    private BigDecimal r42_non_res_carry_amt;
    private String r43_caoin;
    private BigDecimal r43_res_carry_amt;
    private BigDecimal r43_non_res_carry_amt;
    private String r44_caoin;
    private BigDecimal r44_res_carry_amt;
    private BigDecimal r44_non_res_carry_amt;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "REPORT_DATE")
    private Date REPORT_DATE;
    private BigDecimal report_version;
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;

    public Q_SMME_Intrest_Income_Archival_Summary_Entity() {
        super();
    }

    public String getR15_caoin() {
        return r15_caoin;
    }

    public void setR15_caoin(String r15_caoin) {
        this.r15_caoin = r15_caoin;
    }

    public BigDecimal getR15_res_carry_amt() {
        return r15_res_carry_amt;
    }

    public void setR15_res_carry_amt(BigDecimal r15_res_carry_amt) {
        this.r15_res_carry_amt = r15_res_carry_amt;
    }

    public BigDecimal getR15_non_res_carry_amt() {
        return r15_non_res_carry_amt;
    }

    public void setR15_non_res_carry_amt(BigDecimal r15_non_res_carry_amt) {
        this.r15_non_res_carry_amt = r15_non_res_carry_amt;
    }

    public String getR16_caoin() {
        return r16_caoin;
    }

    public void setR16_caoin(String r16_caoin) {
        this.r16_caoin = r16_caoin;
    }

    public BigDecimal getR16_res_carry_amt() {
        return r16_res_carry_amt;
    }

    public void setR16_res_carry_amt(BigDecimal r16_res_carry_amt) {
        this.r16_res_carry_amt = r16_res_carry_amt;
    }

    public BigDecimal getR16_non_res_carry_amt() {
        return r16_non_res_carry_amt;
    }

    public void setR16_non_res_carry_amt(BigDecimal r16_non_res_carry_amt) {
        this.r16_non_res_carry_amt = r16_non_res_carry_amt;
    }

    public String getR17_caoin() {
        return r17_caoin;
    }

    public void setR17_caoin(String r17_caoin) {
        this.r17_caoin = r17_caoin;
    }

    public BigDecimal getR17_res_carry_amt() {
        return r17_res_carry_amt;
    }

    public void setR17_res_carry_amt(BigDecimal r17_res_carry_amt) {
        this.r17_res_carry_amt = r17_res_carry_amt;
    }

    public BigDecimal getR17_non_res_carry_amt() {
        return r17_non_res_carry_amt;
    }

    public void setR17_non_res_carry_amt(BigDecimal r17_non_res_carry_amt) {
        this.r17_non_res_carry_amt = r17_non_res_carry_amt;
    }

    public String getR18_caoin() {
        return r18_caoin;
    }

    public void setR18_caoin(String r18_caoin) {
        this.r18_caoin = r18_caoin;
    }

    public BigDecimal getR18_res_carry_amt() {
        return r18_res_carry_amt;
    }

    public void setR18_res_carry_amt(BigDecimal r18_res_carry_amt) {
        this.r18_res_carry_amt = r18_res_carry_amt;
    }

    public BigDecimal getR18_non_res_carry_amt() {
        return r18_non_res_carry_amt;
    }

    public void setR18_non_res_carry_amt(BigDecimal r18_non_res_carry_amt) {
        this.r18_non_res_carry_amt = r18_non_res_carry_amt;
    }

    public String getR19_caoin() {
        return r19_caoin;
    }

    public void setR19_caoin(String r19_caoin) {
        this.r19_caoin = r19_caoin;
    }

    public BigDecimal getR19_res_carry_amt() {
        return r19_res_carry_amt;
    }

    public void setR19_res_carry_amt(BigDecimal r19_res_carry_amt) {
        this.r19_res_carry_amt = r19_res_carry_amt;
    }

    public BigDecimal getR19_non_res_carry_amt() {
        return r19_non_res_carry_amt;
    }

    public void setR19_non_res_carry_amt(BigDecimal r19_non_res_carry_amt) {
        this.r19_non_res_carry_amt = r19_non_res_carry_amt;
    }

    public String getR20_caoin() {
        return r20_caoin;
    }

    public void setR20_caoin(String r20_caoin) {
        this.r20_caoin = r20_caoin;
    }

    public BigDecimal getR20_res_carry_amt() {
        return r20_res_carry_amt;
    }

    public void setR20_res_carry_amt(BigDecimal r20_res_carry_amt) {
        this.r20_res_carry_amt = r20_res_carry_amt;
    }

    public BigDecimal getR20_non_res_carry_amt() {
        return r20_non_res_carry_amt;
    }

    public void setR20_non_res_carry_amt(BigDecimal r20_non_res_carry_amt) {
        this.r20_non_res_carry_amt = r20_non_res_carry_amt;
    }

    public String getR21_caoin() {
        return r21_caoin;
    }

    public void setR21_caoin(String r21_caoin) {
        this.r21_caoin = r21_caoin;
    }

    public BigDecimal getR21_res_carry_amt() {
        return r21_res_carry_amt;
    }

    public void setR21_res_carry_amt(BigDecimal r21_res_carry_amt) {
        this.r21_res_carry_amt = r21_res_carry_amt;
    }

    public BigDecimal getR21_non_res_carry_amt() {
        return r21_non_res_carry_amt;
    }

    public void setR21_non_res_carry_amt(BigDecimal r21_non_res_carry_amt) {
        this.r21_non_res_carry_amt = r21_non_res_carry_amt;
    }

    public String getR22_caoin() {
        return r22_caoin;
    }

    public void setR22_caoin(String r22_caoin) {
        this.r22_caoin = r22_caoin;
    }

    public BigDecimal getR22_res_carry_amt() {
        return r22_res_carry_amt;
    }

    public void setR22_res_carry_amt(BigDecimal r22_res_carry_amt) {
        this.r22_res_carry_amt = r22_res_carry_amt;
    }

    public BigDecimal getR22_non_res_carry_amt() {
        return r22_non_res_carry_amt;
    }

    public void setR22_non_res_carry_amt(BigDecimal r22_non_res_carry_amt) {
        this.r22_non_res_carry_amt = r22_non_res_carry_amt;
    }

    public String getR23_caoin() {
        return r23_caoin;
    }

    public void setR23_caoin(String r23_caoin) {
        this.r23_caoin = r23_caoin;
    }

    public BigDecimal getR23_res_carry_amt() {
        return r23_res_carry_amt;
    }

    public void setR23_res_carry_amt(BigDecimal r23_res_carry_amt) {
        this.r23_res_carry_amt = r23_res_carry_amt;
    }

    public BigDecimal getR23_non_res_carry_amt() {
        return r23_non_res_carry_amt;
    }

    public void setR23_non_res_carry_amt(BigDecimal r23_non_res_carry_amt) {
        this.r23_non_res_carry_amt = r23_non_res_carry_amt;
    }

    public String getR24_caoin() {
        return r24_caoin;
    }

    public void setR24_caoin(String r24_caoin) {
        this.r24_caoin = r24_caoin;
    }

    public BigDecimal getR24_res_carry_amt() {
        return r24_res_carry_amt;
    }

    public void setR24_res_carry_amt(BigDecimal r24_res_carry_amt) {
        this.r24_res_carry_amt = r24_res_carry_amt;
    }

    public BigDecimal getR24_non_res_carry_amt() {
        return r24_non_res_carry_amt;
    }

    public void setR24_non_res_carry_amt(BigDecimal r24_non_res_carry_amt) {
        this.r24_non_res_carry_amt = r24_non_res_carry_amt;
    }

    public String getR25_caoin() {
        return r25_caoin;
    }

    public void setR25_caoin(String r25_caoin) {
        this.r25_caoin = r25_caoin;
    }

    public BigDecimal getR25_res_carry_amt() {
        return r25_res_carry_amt;
    }

    public void setR25_res_carry_amt(BigDecimal r25_res_carry_amt) {
        this.r25_res_carry_amt = r25_res_carry_amt;
    }

    public BigDecimal getR25_non_res_carry_amt() {
        return r25_non_res_carry_amt;
    }

    public void setR25_non_res_carry_amt(BigDecimal r25_non_res_carry_amt) {
        this.r25_non_res_carry_amt = r25_non_res_carry_amt;
    }

    public String getR26_caoin() {
        return r26_caoin;
    }

    public void setR26_caoin(String r26_caoin) {
        this.r26_caoin = r26_caoin;
    }

    public BigDecimal getR26_res_carry_amt() {
        return r26_res_carry_amt;
    }

    public void setR26_res_carry_amt(BigDecimal r26_res_carry_amt) {
        this.r26_res_carry_amt = r26_res_carry_amt;
    }

    public BigDecimal getR26_non_res_carry_amt() {
        return r26_non_res_carry_amt;
    }

    public void setR26_non_res_carry_amt(BigDecimal r26_non_res_carry_amt) {
        this.r26_non_res_carry_amt = r26_non_res_carry_amt;
    }

    public String getR27_caoin() {
        return r27_caoin;
    }

    public void setR27_caoin(String r27_caoin) {
        this.r27_caoin = r27_caoin;
    }

    public BigDecimal getR27_res_carry_amt() {
        return r27_res_carry_amt;
    }

    public void setR27_res_carry_amt(BigDecimal r27_res_carry_amt) {
        this.r27_res_carry_amt = r27_res_carry_amt;
    }

    public BigDecimal getR27_non_res_carry_amt() {
        return r27_non_res_carry_amt;
    }

    public void setR27_non_res_carry_amt(BigDecimal r27_non_res_carry_amt) {
        this.r27_non_res_carry_amt = r27_non_res_carry_amt;
    }

    public String getR28_caoin() {
        return r28_caoin;
    }

    public void setR28_caoin(String r28_caoin) {
        this.r28_caoin = r28_caoin;
    }

    public BigDecimal getR28_res_carry_amt() {
        return r28_res_carry_amt;
    }

    public void setR28_res_carry_amt(BigDecimal r28_res_carry_amt) {
        this.r28_res_carry_amt = r28_res_carry_amt;
    }

    public BigDecimal getR28_non_res_carry_amt() {
        return r28_non_res_carry_amt;
    }

    public void setR28_non_res_carry_amt(BigDecimal r28_non_res_carry_amt) {
        this.r28_non_res_carry_amt = r28_non_res_carry_amt;
    }

    public String getR29_caoin() {
        return r29_caoin;
    }

    public void setR29_caoin(String r29_caoin) {
        this.r29_caoin = r29_caoin;
    }

    public BigDecimal getR29_res_carry_amt() {
        return r29_res_carry_amt;
    }

    public void setR29_res_carry_amt(BigDecimal r29_res_carry_amt) {
        this.r29_res_carry_amt = r29_res_carry_amt;
    }

    public BigDecimal getR29_non_res_carry_amt() {
        return r29_non_res_carry_amt;
    }

    public void setR29_non_res_carry_amt(BigDecimal r29_non_res_carry_amt) {
        this.r29_non_res_carry_amt = r29_non_res_carry_amt;
    }

    public String getR30_caoin() {
        return r30_caoin;
    }

    public void setR30_caoin(String r30_caoin) {
        this.r30_caoin = r30_caoin;
    }

    public BigDecimal getR30_res_carry_amt() {
        return r30_res_carry_amt;
    }

    public void setR30_res_carry_amt(BigDecimal r30_res_carry_amt) {
        this.r30_res_carry_amt = r30_res_carry_amt;
    }

    public BigDecimal getR30_non_res_carry_amt() {
        return r30_non_res_carry_amt;
    }

    public void setR30_non_res_carry_amt(BigDecimal r30_non_res_carry_amt) {
        this.r30_non_res_carry_amt = r30_non_res_carry_amt;
    }

    public String getR31_caoin() {
        return r31_caoin;
    }

    public void setR31_caoin(String r31_caoin) {
        this.r31_caoin = r31_caoin;
    }

    public BigDecimal getR31_res_carry_amt() {
        return r31_res_carry_amt;
    }

    public void setR31_res_carry_amt(BigDecimal r31_res_carry_amt) {
        this.r31_res_carry_amt = r31_res_carry_amt;
    }

    public BigDecimal getR31_non_res_carry_amt() {
        return r31_non_res_carry_amt;
    }

    public void setR31_non_res_carry_amt(BigDecimal r31_non_res_carry_amt) {
        this.r31_non_res_carry_amt = r31_non_res_carry_amt;
    }

    public String getR32_caoin() {
        return r32_caoin;
    }

    public void setR32_caoin(String r32_caoin) {
        this.r32_caoin = r32_caoin;
    }

    public BigDecimal getR32_res_carry_amt() {
        return r32_res_carry_amt;
    }

    public void setR32_res_carry_amt(BigDecimal r32_res_carry_amt) {
        this.r32_res_carry_amt = r32_res_carry_amt;
    }

    public BigDecimal getR32_non_res_carry_amt() {
        return r32_non_res_carry_amt;
    }

    public void setR32_non_res_carry_amt(BigDecimal r32_non_res_carry_amt) {
        this.r32_non_res_carry_amt = r32_non_res_carry_amt;
    }

    public String getR33_caoin() {
        return r33_caoin;
    }

    public void setR33_caoin(String r33_caoin) {
        this.r33_caoin = r33_caoin;
    }

    public BigDecimal getR33_res_carry_amt() {
        return r33_res_carry_amt;
    }

    public void setR33_res_carry_amt(BigDecimal r33_res_carry_amt) {
        this.r33_res_carry_amt = r33_res_carry_amt;
    }

    public BigDecimal getR33_non_res_carry_amt() {
        return r33_non_res_carry_amt;
    }

    public void setR33_non_res_carry_amt(BigDecimal r33_non_res_carry_amt) {
        this.r33_non_res_carry_amt = r33_non_res_carry_amt;
    }

    public String getR34_caoin() {
        return r34_caoin;
    }

    public void setR34_caoin(String r34_caoin) {
        this.r34_caoin = r34_caoin;
    }

    public BigDecimal getR34_res_carry_amt() {
        return r34_res_carry_amt;
    }

    public void setR34_res_carry_amt(BigDecimal r34_res_carry_amt) {
        this.r34_res_carry_amt = r34_res_carry_amt;
    }

    public BigDecimal getR34_non_res_carry_amt() {
        return r34_non_res_carry_amt;
    }

    public void setR34_non_res_carry_amt(BigDecimal r34_non_res_carry_amt) {
        this.r34_non_res_carry_amt = r34_non_res_carry_amt;
    }

    public String getR35_caoin() {
        return r35_caoin;
    }

    public void setR35_caoin(String r35_caoin) {
        this.r35_caoin = r35_caoin;
    }

    public BigDecimal getR35_res_carry_amt() {
        return r35_res_carry_amt;
    }

    public void setR35_res_carry_amt(BigDecimal r35_res_carry_amt) {
        this.r35_res_carry_amt = r35_res_carry_amt;
    }

    public BigDecimal getR35_non_res_carry_amt() {
        return r35_non_res_carry_amt;
    }

    public void setR35_non_res_carry_amt(BigDecimal r35_non_res_carry_amt) {
        this.r35_non_res_carry_amt = r35_non_res_carry_amt;
    }

    public String getR36_caoin() {
        return r36_caoin;
    }

    public void setR36_caoin(String r36_caoin) {
        this.r36_caoin = r36_caoin;
    }

    public BigDecimal getR36_res_carry_amt() {
        return r36_res_carry_amt;
    }

    public void setR36_res_carry_amt(BigDecimal r36_res_carry_amt) {
        this.r36_res_carry_amt = r36_res_carry_amt;
    }

    public BigDecimal getR36_non_res_carry_amt() {
        return r36_non_res_carry_amt;
    }

    public void setR36_non_res_carry_amt(BigDecimal r36_non_res_carry_amt) {
        this.r36_non_res_carry_amt = r36_non_res_carry_amt;
    }

    public String getR37_caoin() {
        return r37_caoin;
    }

    public void setR37_caoin(String r37_caoin) {
        this.r37_caoin = r37_caoin;
    }

    public BigDecimal getR37_res_carry_amt() {
        return r37_res_carry_amt;
    }

    public void setR37_res_carry_amt(BigDecimal r37_res_carry_amt) {
        this.r37_res_carry_amt = r37_res_carry_amt;
    }

    public BigDecimal getR37_non_res_carry_amt() {
        return r37_non_res_carry_amt;
    }

    public void setR37_non_res_carry_amt(BigDecimal r37_non_res_carry_amt) {
        this.r37_non_res_carry_amt = r37_non_res_carry_amt;
    }

    public String getR38_caoin() {
        return r38_caoin;
    }

    public void setR38_caoin(String r38_caoin) {
        this.r38_caoin = r38_caoin;
    }

    public BigDecimal getR38_res_carry_amt() {
        return r38_res_carry_amt;
    }

    public void setR38_res_carry_amt(BigDecimal r38_res_carry_amt) {
        this.r38_res_carry_amt = r38_res_carry_amt;
    }

    public BigDecimal getR38_non_res_carry_amt() {
        return r38_non_res_carry_amt;
    }

    public void setR38_non_res_carry_amt(BigDecimal r38_non_res_carry_amt) {
        this.r38_non_res_carry_amt = r38_non_res_carry_amt;
    }

    public String getR39_caoin() {
        return r39_caoin;
    }

    public void setR39_caoin(String r39_caoin) {
        this.r39_caoin = r39_caoin;
    }

    public BigDecimal getR39_res_carry_amt() {
        return r39_res_carry_amt;
    }

    public void setR39_res_carry_amt(BigDecimal r39_res_carry_amt) {
        this.r39_res_carry_amt = r39_res_carry_amt;
    }

    public BigDecimal getR39_non_res_carry_amt() {
        return r39_non_res_carry_amt;
    }

    public void setR39_non_res_carry_amt(BigDecimal r39_non_res_carry_amt) {
        this.r39_non_res_carry_amt = r39_non_res_carry_amt;
    }

    public String getR40_caoin() {
        return r40_caoin;
    }

    public void setR40_caoin(String r40_caoin) {
        this.r40_caoin = r40_caoin;
    }

    public BigDecimal getR40_res_carry_amt() {
        return r40_res_carry_amt;
    }

    public void setR40_res_carry_amt(BigDecimal r40_res_carry_amt) {
        this.r40_res_carry_amt = r40_res_carry_amt;
    }

    public BigDecimal getR40_non_res_carry_amt() {
        return r40_non_res_carry_amt;
    }

    public void setR40_non_res_carry_amt(BigDecimal r40_non_res_carry_amt) {
        this.r40_non_res_carry_amt = r40_non_res_carry_amt;
    }

    public String getR41_caoin() {
        return r41_caoin;
    }

    public void setR41_caoin(String r41_caoin) {
        this.r41_caoin = r41_caoin;
    }

    public BigDecimal getR41_res_carry_amt() {
        return r41_res_carry_amt;
    }

    public void setR41_res_carry_amt(BigDecimal r41_res_carry_amt) {
        this.r41_res_carry_amt = r41_res_carry_amt;
    }

    public BigDecimal getR41_non_res_carry_amt() {
        return r41_non_res_carry_amt;
    }

    public void setR41_non_res_carry_amt(BigDecimal r41_non_res_carry_amt) {
        this.r41_non_res_carry_amt = r41_non_res_carry_amt;
    }

    public String getR42_caoin() {
        return r42_caoin;
    }

    public void setR42_caoin(String r42_caoin) {
        this.r42_caoin = r42_caoin;
    }

    public BigDecimal getR42_res_carry_amt() {
        return r42_res_carry_amt;
    }

    public void setR42_res_carry_amt(BigDecimal r42_res_carry_amt) {
        this.r42_res_carry_amt = r42_res_carry_amt;
    }

    public BigDecimal getR42_non_res_carry_amt() {
        return r42_non_res_carry_amt;
    }

    public void setR42_non_res_carry_amt(BigDecimal r42_non_res_carry_amt) {
        this.r42_non_res_carry_amt = r42_non_res_carry_amt;
    }

    public String getR43_caoin() {
        return r43_caoin;
    }

    public void setR43_caoin(String r43_caoin) {
        this.r43_caoin = r43_caoin;
    }

    public BigDecimal getR43_res_carry_amt() {
        return r43_res_carry_amt;
    }

    public void setR43_res_carry_amt(BigDecimal r43_res_carry_amt) {
        this.r43_res_carry_amt = r43_res_carry_amt;
    }

    public BigDecimal getR43_non_res_carry_amt() {
        return r43_non_res_carry_amt;
    }

    public void setR43_non_res_carry_amt(BigDecimal r43_non_res_carry_amt) {
        this.r43_non_res_carry_amt = r43_non_res_carry_amt;
    }

    public String getR44_caoin() {
        return r44_caoin;
    }

    public void setR44_caoin(String r44_caoin) {
        this.r44_caoin = r44_caoin;
    }

    public BigDecimal getR44_res_carry_amt() {
        return r44_res_carry_amt;
    }

    public void setR44_res_carry_amt(BigDecimal r44_res_carry_amt) {
        this.r44_res_carry_amt = r44_res_carry_amt;
    }

    public BigDecimal getR44_non_res_carry_amt() {
        return r44_non_res_carry_amt;
    }

    public void setR44_non_res_carry_amt(BigDecimal r44_non_res_carry_amt) {
        this.r44_non_res_carry_amt = r44_non_res_carry_amt;
    }

    public Date getReport_date() {
        return REPORT_DATE;
    }

    public void setReport_date(Date REPORT_DATE) {
        this.REPORT_DATE = REPORT_DATE;
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

}