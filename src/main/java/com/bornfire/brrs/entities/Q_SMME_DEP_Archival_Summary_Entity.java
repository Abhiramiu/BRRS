package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_Q_SMME_DEP_ARCHIVAL_SUMMARYTABLE")
@IdClass(Q_SMME_DEP_Archival_Summary_PK.class)

public class Q_SMME_DEP_Archival_Summary_Entity {	
	
	
	  // -------------------- R11 --------------------
    private String R11_PRODUCT;
    private BigDecimal R11_CURRENT;
    private BigDecimal R11_CALL;
    private BigDecimal R11_SAVINGS;
    private BigDecimal R11_0_31D_NOTICE;
    private BigDecimal R11_32_88D_NOTICE;
    private BigDecimal R11_91D_DEPOSIT;
    private BigDecimal R11_1_2M_FD;
    private BigDecimal R11_4_6M_FD;
    private BigDecimal R11_7_12M_FD;
    private BigDecimal R11_13_18M_FD;
    private BigDecimal R11_19_24M_FD;
    private BigDecimal R11_OVER24_FD;
    private BigDecimal R11_TOTAL;
    private BigDecimal R11_NOACC;

    // -------------------- R12 --------------------
    private String R12_PRODUCT;
    private BigDecimal R12_CURRENT;
    private BigDecimal R12_CALL;
    private BigDecimal R12_SAVINGS;
    private BigDecimal R12_0_31D_NOTICE;
    private BigDecimal R12_32_88D_NOTICE;
    private BigDecimal R12_91D_DEPOSIT;
    private BigDecimal R12_1_2M_FD;
    private BigDecimal R12_4_6M_FD;
    private BigDecimal R12_7_12M_FD;
    private BigDecimal R12_13_18M_FD;
    private BigDecimal R12_19_24M_FD;
    private BigDecimal R12_OVER24_FD;
    private BigDecimal R12_TOTAL;
    private BigDecimal R12_NOACC;

    // -------------------- R13 --------------------
    private String R13_PRODUCT;
    private BigDecimal R13_CURRENT;
    private BigDecimal R13_CALL;
    private BigDecimal R13_SAVINGS;
    private BigDecimal R13_0_31D_NOTICE;
    private BigDecimal R13_32_88D_NOTICE;
    private BigDecimal R13_91D_DEPOSIT;
    private BigDecimal R13_1_2M_FD;
    private BigDecimal R13_4_6M_FD;
    private BigDecimal R13_7_12M_FD;
    private BigDecimal R13_13_18M_FD;
    private BigDecimal R13_19_24M_FD;
    private BigDecimal R13_OVER24_FD;
    private BigDecimal R13_TOTAL;
    private BigDecimal R13_NOACC;

    // -------------------- R14 --------------------
    private String R14_PRODUCT;
    private BigDecimal R14_CURRENT;
    private BigDecimal R14_CALL;
    private BigDecimal R14_SAVINGS;
    private BigDecimal R14_0_31D_NOTICE;
    private BigDecimal R14_32_88D_NOTICE;
    private BigDecimal R14_91D_DEPOSIT;
    private BigDecimal R14_1_2M_FD;
    private BigDecimal R14_4_6M_FD;
    private BigDecimal R14_7_12M_FD;
    private BigDecimal R14_13_18M_FD;
    private BigDecimal R14_19_24M_FD;
    private BigDecimal R14_OVER24_FD;
    private BigDecimal R14_TOTAL;
    private BigDecimal R14_NOACC;

    // -------------------- R15 --------------------
    private String R15_PRODUCT;
    private BigDecimal R15_CURRENT;
    private BigDecimal R15_CALL;
    private BigDecimal R15_SAVINGS;
    private BigDecimal R15_0_31D_NOTICE;
    private BigDecimal R15_32_88D_NOTICE;
    private BigDecimal R15_91D_DEPOSIT;
    private BigDecimal R15_1_2M_FD;
    private BigDecimal R15_4_6M_FD;
    private BigDecimal R15_7_12M_FD;
    private BigDecimal R15_13_18M_FD;
    private BigDecimal R15_19_24M_FD;
    private BigDecimal R15_OVER24_FD;
    private BigDecimal R15_TOTAL;
    private BigDecimal R15_NOACC;

    // -------------------- R16 --------------------
    private String R16_PRODUCT;
    private BigDecimal R16_CURRENT;
    private BigDecimal R16_CALL;
    private BigDecimal R16_SAVINGS;
    private BigDecimal R16_0_31D_NOTICE;
    private BigDecimal R16_32_88D_NOTICE;
    private BigDecimal R16_91D_DEPOSIT;
    private BigDecimal R16_1_2M_FD;
    private BigDecimal R16_4_6M_FD;
    private BigDecimal R16_7_12M_FD;
    private BigDecimal R16_13_18M_FD;
    private BigDecimal R16_19_24M_FD;
    private BigDecimal R16_OVER24_FD;
    private BigDecimal R16_TOTAL;
    private BigDecimal R16_NOACC;

    // -------------------- R17 --------------------
    private String R17_PRODUCT;
    private BigDecimal R17_CURRENT;
    private BigDecimal R17_CALL;
    private BigDecimal R17_SAVINGS;
    private BigDecimal R17_0_31D_NOTICE;
    private BigDecimal R17_32_88D_NOTICE;
    private BigDecimal R17_91D_DEPOSIT;
    private BigDecimal R17_1_2M_FD;
    private BigDecimal R17_4_6M_FD;
    private BigDecimal R17_7_12M_FD;
    private BigDecimal R17_13_18M_FD;
    private BigDecimal R17_19_24M_FD;
    private BigDecimal R17_OVER24_FD;
    private BigDecimal R17_TOTAL;
    private BigDecimal R17_NOACC;

    // -------------------- R18 --------------------
    private String R18_PRODUCT;
    private BigDecimal R18_CURRENT;
    private BigDecimal R18_CALL;
    private BigDecimal R18_SAVINGS;
    private BigDecimal R18_0_31D_NOTICE;
    private BigDecimal R18_32_88D_NOTICE;
    private BigDecimal R18_91D_DEPOSIT;
    private BigDecimal R18_1_2M_FD;
    private BigDecimal R18_4_6M_FD;
    private BigDecimal R18_7_12M_FD;
    private BigDecimal R18_13_18M_FD;
    private BigDecimal R18_19_24M_FD;
    private BigDecimal R18_OVER24_FD;
    private BigDecimal R18_TOTAL;
    private BigDecimal R18_NOACC;

    // -------------------- R19 --------------------
    private String R19_PRODUCT;
    private BigDecimal R19_CURRENT;
    private BigDecimal R19_CALL;
    private BigDecimal R19_SAVINGS;
    private BigDecimal R19_0_31D_NOTICE;
    private BigDecimal R19_32_88D_NOTICE;
    private BigDecimal R19_91D_DEPOSIT;
    private BigDecimal R19_1_2M_FD;
    private BigDecimal R19_4_6M_FD;
    private BigDecimal R19_7_12M_FD;
    private BigDecimal R19_13_18M_FD;
    private BigDecimal R19_19_24M_FD;
    private BigDecimal R19_OVER24_FD;
    private BigDecimal R19_TOTAL;
    private BigDecimal R19_NOACC;

    // -------------------- R20 --------------------
    private String R20_PRODUCT;
    private BigDecimal R20_CURRENT;
    private BigDecimal R20_CALL;
    private BigDecimal R20_SAVINGS;
    private BigDecimal R20_0_31D_NOTICE;
    private BigDecimal R20_32_88D_NOTICE;
    private BigDecimal R20_91D_DEPOSIT;
    private BigDecimal R20_1_2M_FD;
    private BigDecimal R20_4_6M_FD;
    private BigDecimal R20_7_12M_FD;
    private BigDecimal R20_13_18M_FD;
    private BigDecimal R20_19_24M_FD;
    private BigDecimal R20_OVER24_FD;
    private BigDecimal R20_TOTAL;
    private BigDecimal R20_NOACC;

    // -------------------- R21 --------------------
    private String R21_PRODUCT;
    private BigDecimal R21_CURRENT;
    private BigDecimal R21_CALL;
    private BigDecimal R21_SAVINGS;
    private BigDecimal R21_0_31D_NOTICE;
    private BigDecimal R21_32_88D_NOTICE;
    private BigDecimal R21_91D_DEPOSIT;
    private BigDecimal R21_1_2M_FD;
    private BigDecimal R21_4_6M_FD;
    private BigDecimal R21_7_12M_FD;
    private BigDecimal R21_13_18M_FD;
    private BigDecimal R21_19_24M_FD;
    private BigDecimal R21_OVER24_FD;
    private BigDecimal R21_TOTAL;
    private BigDecimal R21_NOACC;

    // -------------------- R22 --------------------
    private String R22_PRODUCT;
    private BigDecimal R22_CURRENT;
    private BigDecimal R22_CALL;
    private BigDecimal R22_SAVINGS;
    private BigDecimal R22_0_31D_NOTICE;
    private BigDecimal R22_32_88D_NOTICE;
    private BigDecimal R22_91D_DEPOSIT;
    private BigDecimal R22_1_2M_FD;
    private BigDecimal R22_4_6M_FD;
    private BigDecimal R22_7_12M_FD;
    private BigDecimal R22_13_18M_FD;
    private BigDecimal R22_19_24M_FD;
    private BigDecimal R22_OVER24_FD;
    private BigDecimal R22_TOTAL;
    private BigDecimal R22_NOACC;

    // -------------------- R23 --------------------
    private String R23_PRODUCT;
    private BigDecimal R23_CURRENT;
    private BigDecimal R23_CALL;
    private BigDecimal R23_SAVINGS;
    private BigDecimal R23_0_31D_NOTICE;
    private BigDecimal R23_32_88D_NOTICE;
    private BigDecimal R23_91D_DEPOSIT;
    private BigDecimal R23_1_2M_FD;
    private BigDecimal R23_4_6M_FD;
    private BigDecimal R23_7_12M_FD;
    private BigDecimal R23_13_18M_FD;
    private BigDecimal R23_19_24M_FD;
    private BigDecimal R23_OVER24_FD;
    private BigDecimal R23_TOTAL;
    private BigDecimal R23_NOACC;

    // -------------------- R24 --------------------
    private String R24_PRODUCT;
    private BigDecimal R24_CURRENT;
    private BigDecimal R24_CALL;
    private BigDecimal R24_SAVINGS;
    private BigDecimal R24_0_31D_NOTICE;
    private BigDecimal R24_32_88D_NOTICE;
    private BigDecimal R24_91D_DEPOSIT;
    private BigDecimal R24_1_2M_FD;
    private BigDecimal R24_4_6M_FD;
    private BigDecimal R24_7_12M_FD;
    private BigDecimal R24_13_18M_FD;
    private BigDecimal R24_19_24M_FD;
    private BigDecimal R24_OVER24_FD;
    private BigDecimal R24_TOTAL;
    private BigDecimal R24_NOACC;

    // -------------------- R25 --------------------
    private String R25_PRODUCT;
    private BigDecimal R25_CURRENT;
    private BigDecimal R25_CALL;
    private BigDecimal R25_SAVINGS;
    private BigDecimal R25_0_31D_NOTICE;
    private BigDecimal R25_32_88D_NOTICE;
    private BigDecimal R25_91D_DEPOSIT;
    private BigDecimal R25_1_2M_FD;
    private BigDecimal R25_4_6M_FD;
    private BigDecimal R25_7_12M_FD;
    private BigDecimal R25_13_18M_FD;
    private BigDecimal R25_19_24M_FD;
    private BigDecimal R25_OVER24_FD;
    private BigDecimal R25_TOTAL;
    private BigDecimal R25_NOACC;

    // -------------------- R26 --------------------
    private String R26_PRODUCT;
    private BigDecimal R26_CURRENT;
    private BigDecimal R26_CALL;
    private BigDecimal R26_SAVINGS;
    private BigDecimal R26_0_31D_NOTICE;
    private BigDecimal R26_32_88D_NOTICE;
    private BigDecimal R26_91D_DEPOSIT;
    private BigDecimal R26_1_2M_FD;
    private BigDecimal R26_4_6M_FD;
    private BigDecimal R26_7_12M_FD;
    private BigDecimal R26_13_18M_FD;
    private BigDecimal R26_19_24M_FD;
    private BigDecimal R26_OVER24_FD;
    private BigDecimal R26_TOTAL;
    private BigDecimal R26_NOACC;

    // -------------------- R32 --------------------
    private String R32_PRODUCT;
    private BigDecimal R32_CURRENT;
    private BigDecimal R32_CALL;
    private BigDecimal R32_SAVINGS;
    private BigDecimal R32_0_31D_NOTICE;
    private BigDecimal R32_32_88D_NOTICE;
    private BigDecimal R32_91D_DEPOSIT;
    private BigDecimal R32_1_2M_FD;
    private BigDecimal R32_4_6M_FD;
    private BigDecimal R32_7_12M_FD;
    private BigDecimal R32_13_18M_FD;
    private BigDecimal R32_19_24M_FD;
    private BigDecimal R32_OVER24_FD;
    private BigDecimal R32_TOTAL;
    private BigDecimal R32_NOACC;

    // -------------------- R33 --------------------
    private String R33_PRODUCT;
    private BigDecimal R33_CURRENT;
    private BigDecimal R33_CALL;
    private BigDecimal R33_SAVINGS;
    private BigDecimal R33_0_31D_NOTICE;
    private BigDecimal R33_32_88D_NOTICE;
    private BigDecimal R33_91D_DEPOSIT;
    private BigDecimal R33_1_2M_FD;
    private BigDecimal R33_4_6M_FD;
    private BigDecimal R33_7_12M_FD;
    private BigDecimal R33_13_18M_FD;
    private BigDecimal R33_19_24M_FD;
    private BigDecimal R33_OVER24_FD;
    private BigDecimal R33_TOTAL;
    private BigDecimal R33_NOACC;

    // -------------------- R34 --------------------
    private String R34_PRODUCT;
    private BigDecimal R34_CURRENT;
    private BigDecimal R34_CALL;
    private BigDecimal R34_SAVINGS;
    private BigDecimal R34_0_31D_NOTICE;
    private BigDecimal R34_32_88D_NOTICE;
    private BigDecimal R34_91D_DEPOSIT;
    private BigDecimal R34_1_2M_FD;
    private BigDecimal R34_4_6M_FD;
    private BigDecimal R34_7_12M_FD;
    private BigDecimal R34_13_18M_FD;
    private BigDecimal R34_19_24M_FD;
    private BigDecimal R34_OVER24_FD;
    private BigDecimal R34_TOTAL;
    private BigDecimal R34_NOACC;

    // -------------------- R35 --------------------
    private String R35_PRODUCT;
    private BigDecimal R35_CURRENT;
    private BigDecimal R35_CALL;
    private BigDecimal R35_SAVINGS;
    private BigDecimal R35_0_31D_NOTICE;
    private BigDecimal R35_32_88D_NOTICE;
    private BigDecimal R35_91D_DEPOSIT;
    private BigDecimal R35_1_2M_FD;
    private BigDecimal R35_4_6M_FD;
    private BigDecimal R35_7_12M_FD;
    private BigDecimal R35_13_18M_FD;
    private BigDecimal R35_19_24M_FD;
    private BigDecimal R35_OVER24_FD;
    private BigDecimal R35_TOTAL;
    private BigDecimal R35_NOACC;

    // -------------------- R36 --------------------
    private String R36_PRODUCT;
    private BigDecimal R36_CURRENT;
    private BigDecimal R36_CALL;
    private BigDecimal R36_SAVINGS;
    private BigDecimal R36_0_31D_NOTICE;
    private BigDecimal R36_32_88D_NOTICE;
    private BigDecimal R36_91D_DEPOSIT;
    private BigDecimal R36_1_2M_FD;
    private BigDecimal R36_4_6M_FD;
    private BigDecimal R36_7_12M_FD;
    private BigDecimal R36_13_18M_FD;
    private BigDecimal R36_19_24M_FD;
    private BigDecimal R36_OVER24_FD;
    private BigDecimal R36_TOTAL;
    private BigDecimal R36_NOACC;

    // -------------------- R37 --------------------
    private String R37_PRODUCT;
    private BigDecimal R37_CURRENT;
    private BigDecimal R37_CALL;
    private BigDecimal R37_SAVINGS;
    private BigDecimal R37_0_31D_NOTICE;
    private BigDecimal R37_32_88D_NOTICE;
    private BigDecimal R37_91D_DEPOSIT;
    private BigDecimal R37_1_2M_FD;
    private BigDecimal R37_4_6M_FD;
    private BigDecimal R37_7_12M_FD;
    private BigDecimal R37_13_18M_FD;
    private BigDecimal R37_19_24M_FD;
    private BigDecimal R37_OVER24_FD;
    private BigDecimal R37_TOTAL;
    private BigDecimal R37_NOACC;

    // -------------------- R38 --------------------
    private String R38_PRODUCT;
    private BigDecimal R38_CURRENT;
    private BigDecimal R38_CALL;
    private BigDecimal R38_SAVINGS;
    private BigDecimal R38_0_31D_NOTICE;
    private BigDecimal R38_32_88D_NOTICE;
    private BigDecimal R38_91D_DEPOSIT;
    private BigDecimal R38_1_2M_FD;
    private BigDecimal R38_4_6M_FD;
    private BigDecimal R38_7_12M_FD;
    private BigDecimal R38_13_18M_FD;
    private BigDecimal R38_19_24M_FD;
    private BigDecimal R38_OVER24_FD;
    private BigDecimal R38_TOTAL;
    private BigDecimal R38_NOACC;

    // -------------------- R39 --------------------
    private String R39_PRODUCT;
    private BigDecimal R39_CURRENT;
    private BigDecimal R39_CALL;
    private BigDecimal R39_SAVINGS;
    private BigDecimal R39_0_31D_NOTICE;
    private BigDecimal R39_32_88D_NOTICE;
    private BigDecimal R39_91D_DEPOSIT;
    private BigDecimal R39_1_2M_FD;
    private BigDecimal R39_4_6M_FD;
    private BigDecimal R39_7_12M_FD;
    private BigDecimal R39_13_18M_FD;
    private BigDecimal R39_19_24M_FD;
    private BigDecimal R39_OVER24_FD;
    private BigDecimal R39_TOTAL;
    private BigDecimal R39_NOACC;

    // -------------------- R40 --------------------
    private String R40_PRODUCT;
    private BigDecimal R40_CURRENT;
    private BigDecimal R40_CALL;
    private BigDecimal R40_SAVINGS;
    private BigDecimal R40_0_31D_NOTICE;
    private BigDecimal R40_32_88D_NOTICE;
    private BigDecimal R40_91D_DEPOSIT;
    private BigDecimal R40_1_2M_FD;
    private BigDecimal R40_4_6M_FD;
    private BigDecimal R40_7_12M_FD;
    private BigDecimal R40_13_18M_FD;
    private BigDecimal R40_19_24M_FD;
    private BigDecimal R40_OVER24_FD;
    private BigDecimal R40_TOTAL;
    private BigDecimal R40_NOACC;

    // -------------------- R41 --------------------
    private String R41_PRODUCT;
    private BigDecimal R41_CURRENT;
    private BigDecimal R41_CALL;
    private BigDecimal R41_SAVINGS;
    private BigDecimal R41_0_31D_NOTICE;
    private BigDecimal R41_32_88D_NOTICE;
    private BigDecimal R41_91D_DEPOSIT;
    private BigDecimal R41_1_2M_FD;
    private BigDecimal R41_4_6M_FD;
    private BigDecimal R41_7_12M_FD;
    private BigDecimal R41_13_18M_FD;
    private BigDecimal R41_19_24M_FD;
    private BigDecimal R41_OVER24_FD;
    private BigDecimal R41_TOTAL;
    private BigDecimal R41_NOACC;

    // -------------------- R42 --------------------
    private String R42_PRODUCT;
    private BigDecimal R42_CURRENT;
    private BigDecimal R42_CALL;
    private BigDecimal R42_SAVINGS;
    private BigDecimal R42_0_31D_NOTICE;
    private BigDecimal R42_32_88D_NOTICE;
    private BigDecimal R42_91D_DEPOSIT;
    private BigDecimal R42_1_2M_FD;
    private BigDecimal R42_4_6M_FD;
    private BigDecimal R42_7_12M_FD;
    private BigDecimal R42_13_18M_FD;
    private BigDecimal R42_19_24M_FD;
    private BigDecimal R42_OVER24_FD;
    private BigDecimal R42_TOTAL;
    private BigDecimal R42_NOACC;

    // -------------------- R43 --------------------
    private String R43_PRODUCT;
    private BigDecimal R43_CURRENT;
    private BigDecimal R43_CALL;
    private BigDecimal R43_SAVINGS;
    private BigDecimal R43_0_31D_NOTICE;
    private BigDecimal R43_32_88D_NOTICE;
    private BigDecimal R43_91D_DEPOSIT;
    private BigDecimal R43_1_2M_FD;
    private BigDecimal R43_4_6M_FD;
    private BigDecimal R43_7_12M_FD;
    private BigDecimal R43_13_18M_FD;
    private BigDecimal R43_19_24M_FD;
    private BigDecimal R43_OVER24_FD;
    private BigDecimal R43_TOTAL;
    private BigDecimal R43_NOACC;

    // -------------------- R44 --------------------
    private String R44_PRODUCT;
    private BigDecimal R44_CURRENT;
    private BigDecimal R44_CALL;
    private BigDecimal R44_SAVINGS;
    private BigDecimal R44_0_31D_NOTICE;
    private BigDecimal R44_32_88D_NOTICE;
    private BigDecimal R44_91D_DEPOSIT;
    private BigDecimal R44_1_2M_FD;
    private BigDecimal R44_4_6M_FD;
    private BigDecimal R44_7_12M_FD;
    private BigDecimal R44_13_18M_FD;
    private BigDecimal R44_19_24M_FD;
    private BigDecimal R44_OVER24_FD;
    private BigDecimal R44_TOTAL;
    private BigDecimal R44_NOACC;

    // -------------------- R45 --------------------
    private String R45_PRODUCT;
    private BigDecimal R45_CURRENT;
    private BigDecimal R45_CALL;
    private BigDecimal R45_SAVINGS;
    private BigDecimal R45_0_31D_NOTICE;
    private BigDecimal R45_32_88D_NOTICE;
    private BigDecimal R45_91D_DEPOSIT;
    private BigDecimal R45_1_2M_FD;
    private BigDecimal R45_4_6M_FD;
    private BigDecimal R45_7_12M_FD;
    private BigDecimal R45_13_18M_FD;
    private BigDecimal R45_19_24M_FD;
    private BigDecimal R45_OVER24_FD;
    private BigDecimal R45_TOTAL;
    private BigDecimal R45_NOACC;

    // -------------------- R46 --------------------
    private String R46_PRODUCT;
    private BigDecimal R46_CURRENT;
    private BigDecimal R46_CALL;
    private BigDecimal R46_SAVINGS;
    private BigDecimal R46_0_31D_NOTICE;
    private BigDecimal R46_32_88D_NOTICE;
    private BigDecimal R46_91D_DEPOSIT;
    private BigDecimal R46_1_2M_FD;
    private BigDecimal R46_4_6M_FD;
    private BigDecimal R46_7_12M_FD;
    private BigDecimal R46_13_18M_FD;
    private BigDecimal R46_19_24M_FD;
    private BigDecimal R46_OVER24_FD;
    private BigDecimal R46_TOTAL;
    private BigDecimal R46_NOACC;

    // -------------------- R47 --------------------
    private String R47_PRODUCT;
    private BigDecimal R47_CURRENT;
    private BigDecimal R47_CALL;
    private BigDecimal R47_SAVINGS;
    private BigDecimal R47_0_31D_NOTICE;
    private BigDecimal R47_32_88D_NOTICE;
    private BigDecimal R47_91D_DEPOSIT;
    private BigDecimal R47_1_2M_FD;
    private BigDecimal R47_4_6M_FD;
    private BigDecimal R47_7_12M_FD;
    private BigDecimal R47_13_18M_FD;
    private BigDecimal R47_19_24M_FD;
    private BigDecimal R47_OVER24_FD;
    private BigDecimal R47_TOTAL;
    private BigDecimal R47_NOACC;

    @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	
		
	private Date	report_date;
	@Id
	private String	report_version;
	
	@Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	 del_flg;
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_CURRENT() {
		return R11_CURRENT;
	}
	public void setR11_CURRENT(BigDecimal r11_CURRENT) {
		R11_CURRENT = r11_CURRENT;
	}
	public BigDecimal getR11_CALL() {
		return R11_CALL;
	}
	public void setR11_CALL(BigDecimal r11_CALL) {
		R11_CALL = r11_CALL;
	}
	public BigDecimal getR11_SAVINGS() {
		return R11_SAVINGS;
	}
	public void setR11_SAVINGS(BigDecimal r11_SAVINGS) {
		R11_SAVINGS = r11_SAVINGS;
	}
	public BigDecimal getR11_0_31D_NOTICE() {
		return R11_0_31D_NOTICE;
	}
	public void setR11_0_31D_NOTICE(BigDecimal r11_0_31d_NOTICE) {
		R11_0_31D_NOTICE = r11_0_31d_NOTICE;
	}
	public BigDecimal getR11_32_88D_NOTICE() {
		return R11_32_88D_NOTICE;
	}
	public void setR11_32_88D_NOTICE(BigDecimal r11_32_88d_NOTICE) {
		R11_32_88D_NOTICE = r11_32_88d_NOTICE;
	}
	public BigDecimal getR11_91D_DEPOSIT() {
		return R11_91D_DEPOSIT;
	}
	public void setR11_91D_DEPOSIT(BigDecimal r11_91d_DEPOSIT) {
		R11_91D_DEPOSIT = r11_91d_DEPOSIT;
	}
	public BigDecimal getR11_1_2M_FD() {
		return R11_1_2M_FD;
	}
	public void setR11_1_2M_FD(BigDecimal r11_1_2m_FD) {
		R11_1_2M_FD = r11_1_2m_FD;
	}
	public BigDecimal getR11_4_6M_FD() {
		return R11_4_6M_FD;
	}
	public void setR11_4_6M_FD(BigDecimal r11_4_6m_FD) {
		R11_4_6M_FD = r11_4_6m_FD;
	}
	public BigDecimal getR11_7_12M_FD() {
		return R11_7_12M_FD;
	}
	public void setR11_7_12M_FD(BigDecimal r11_7_12m_FD) {
		R11_7_12M_FD = r11_7_12m_FD;
	}
	public BigDecimal getR11_13_18M_FD() {
		return R11_13_18M_FD;
	}
	public void setR11_13_18M_FD(BigDecimal r11_13_18m_FD) {
		R11_13_18M_FD = r11_13_18m_FD;
	}
	public BigDecimal getR11_19_24M_FD() {
		return R11_19_24M_FD;
	}
	public void setR11_19_24M_FD(BigDecimal r11_19_24m_FD) {
		R11_19_24M_FD = r11_19_24m_FD;
	}
	public BigDecimal getR11_OVER24_FD() {
		return R11_OVER24_FD;
	}
	public void setR11_OVER24_FD(BigDecimal r11_OVER24_FD) {
		R11_OVER24_FD = r11_OVER24_FD;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public BigDecimal getR11_NOACC() {
		return R11_NOACC;
	}
	public void setR11_NOACC(BigDecimal r11_NOACC) {
		R11_NOACC = r11_NOACC;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_CURRENT() {
		return R12_CURRENT;
	}
	public void setR12_CURRENT(BigDecimal r12_CURRENT) {
		R12_CURRENT = r12_CURRENT;
	}
	public BigDecimal getR12_CALL() {
		return R12_CALL;
	}
	public void setR12_CALL(BigDecimal r12_CALL) {
		R12_CALL = r12_CALL;
	}
	public BigDecimal getR12_SAVINGS() {
		return R12_SAVINGS;
	}
	public void setR12_SAVINGS(BigDecimal r12_SAVINGS) {
		R12_SAVINGS = r12_SAVINGS;
	}
	public BigDecimal getR12_0_31D_NOTICE() {
		return R12_0_31D_NOTICE;
	}
	public void setR12_0_31D_NOTICE(BigDecimal r12_0_31d_NOTICE) {
		R12_0_31D_NOTICE = r12_0_31d_NOTICE;
	}
	public BigDecimal getR12_32_88D_NOTICE() {
		return R12_32_88D_NOTICE;
	}
	public void setR12_32_88D_NOTICE(BigDecimal r12_32_88d_NOTICE) {
		R12_32_88D_NOTICE = r12_32_88d_NOTICE;
	}
	public BigDecimal getR12_91D_DEPOSIT() {
		return R12_91D_DEPOSIT;
	}
	public void setR12_91D_DEPOSIT(BigDecimal r12_91d_DEPOSIT) {
		R12_91D_DEPOSIT = r12_91d_DEPOSIT;
	}
	public BigDecimal getR12_1_2M_FD() {
		return R12_1_2M_FD;
	}
	public void setR12_1_2M_FD(BigDecimal r12_1_2m_FD) {
		R12_1_2M_FD = r12_1_2m_FD;
	}
	public BigDecimal getR12_4_6M_FD() {
		return R12_4_6M_FD;
	}
	public void setR12_4_6M_FD(BigDecimal r12_4_6m_FD) {
		R12_4_6M_FD = r12_4_6m_FD;
	}
	public BigDecimal getR12_7_12M_FD() {
		return R12_7_12M_FD;
	}
	public void setR12_7_12M_FD(BigDecimal r12_7_12m_FD) {
		R12_7_12M_FD = r12_7_12m_FD;
	}
	public BigDecimal getR12_13_18M_FD() {
		return R12_13_18M_FD;
	}
	public void setR12_13_18M_FD(BigDecimal r12_13_18m_FD) {
		R12_13_18M_FD = r12_13_18m_FD;
	}
	public BigDecimal getR12_19_24M_FD() {
		return R12_19_24M_FD;
	}
	public void setR12_19_24M_FD(BigDecimal r12_19_24m_FD) {
		R12_19_24M_FD = r12_19_24m_FD;
	}
	public BigDecimal getR12_OVER24_FD() {
		return R12_OVER24_FD;
	}
	public void setR12_OVER24_FD(BigDecimal r12_OVER24_FD) {
		R12_OVER24_FD = r12_OVER24_FD;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public BigDecimal getR12_NOACC() {
		return R12_NOACC;
	}
	public void setR12_NOACC(BigDecimal r12_NOACC) {
		R12_NOACC = r12_NOACC;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_CURRENT() {
		return R13_CURRENT;
	}
	public void setR13_CURRENT(BigDecimal r13_CURRENT) {
		R13_CURRENT = r13_CURRENT;
	}
	public BigDecimal getR13_CALL() {
		return R13_CALL;
	}
	public void setR13_CALL(BigDecimal r13_CALL) {
		R13_CALL = r13_CALL;
	}
	public BigDecimal getR13_SAVINGS() {
		return R13_SAVINGS;
	}
	public void setR13_SAVINGS(BigDecimal r13_SAVINGS) {
		R13_SAVINGS = r13_SAVINGS;
	}
	public BigDecimal getR13_0_31D_NOTICE() {
		return R13_0_31D_NOTICE;
	}
	public void setR13_0_31D_NOTICE(BigDecimal r13_0_31d_NOTICE) {
		R13_0_31D_NOTICE = r13_0_31d_NOTICE;
	}
	public BigDecimal getR13_32_88D_NOTICE() {
		return R13_32_88D_NOTICE;
	}
	public void setR13_32_88D_NOTICE(BigDecimal r13_32_88d_NOTICE) {
		R13_32_88D_NOTICE = r13_32_88d_NOTICE;
	}
	public BigDecimal getR13_91D_DEPOSIT() {
		return R13_91D_DEPOSIT;
	}
	public void setR13_91D_DEPOSIT(BigDecimal r13_91d_DEPOSIT) {
		R13_91D_DEPOSIT = r13_91d_DEPOSIT;
	}
	public BigDecimal getR13_1_2M_FD() {
		return R13_1_2M_FD;
	}
	public void setR13_1_2M_FD(BigDecimal r13_1_2m_FD) {
		R13_1_2M_FD = r13_1_2m_FD;
	}
	public BigDecimal getR13_4_6M_FD() {
		return R13_4_6M_FD;
	}
	public void setR13_4_6M_FD(BigDecimal r13_4_6m_FD) {
		R13_4_6M_FD = r13_4_6m_FD;
	}
	public BigDecimal getR13_7_12M_FD() {
		return R13_7_12M_FD;
	}
	public void setR13_7_12M_FD(BigDecimal r13_7_12m_FD) {
		R13_7_12M_FD = r13_7_12m_FD;
	}
	public BigDecimal getR13_13_18M_FD() {
		return R13_13_18M_FD;
	}
	public void setR13_13_18M_FD(BigDecimal r13_13_18m_FD) {
		R13_13_18M_FD = r13_13_18m_FD;
	}
	public BigDecimal getR13_19_24M_FD() {
		return R13_19_24M_FD;
	}
	public void setR13_19_24M_FD(BigDecimal r13_19_24m_FD) {
		R13_19_24M_FD = r13_19_24m_FD;
	}
	public BigDecimal getR13_OVER24_FD() {
		return R13_OVER24_FD;
	}
	public void setR13_OVER24_FD(BigDecimal r13_OVER24_FD) {
		R13_OVER24_FD = r13_OVER24_FD;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public BigDecimal getR13_NOACC() {
		return R13_NOACC;
	}
	public void setR13_NOACC(BigDecimal r13_NOACC) {
		R13_NOACC = r13_NOACC;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_CURRENT() {
		return R14_CURRENT;
	}
	public void setR14_CURRENT(BigDecimal r14_CURRENT) {
		R14_CURRENT = r14_CURRENT;
	}
	public BigDecimal getR14_CALL() {
		return R14_CALL;
	}
	public void setR14_CALL(BigDecimal r14_CALL) {
		R14_CALL = r14_CALL;
	}
	public BigDecimal getR14_SAVINGS() {
		return R14_SAVINGS;
	}
	public void setR14_SAVINGS(BigDecimal r14_SAVINGS) {
		R14_SAVINGS = r14_SAVINGS;
	}
	public BigDecimal getR14_0_31D_NOTICE() {
		return R14_0_31D_NOTICE;
	}
	public void setR14_0_31D_NOTICE(BigDecimal r14_0_31d_NOTICE) {
		R14_0_31D_NOTICE = r14_0_31d_NOTICE;
	}
	public BigDecimal getR14_32_88D_NOTICE() {
		return R14_32_88D_NOTICE;
	}
	public void setR14_32_88D_NOTICE(BigDecimal r14_32_88d_NOTICE) {
		R14_32_88D_NOTICE = r14_32_88d_NOTICE;
	}
	public BigDecimal getR14_91D_DEPOSIT() {
		return R14_91D_DEPOSIT;
	}
	public void setR14_91D_DEPOSIT(BigDecimal r14_91d_DEPOSIT) {
		R14_91D_DEPOSIT = r14_91d_DEPOSIT;
	}
	public BigDecimal getR14_1_2M_FD() {
		return R14_1_2M_FD;
	}
	public void setR14_1_2M_FD(BigDecimal r14_1_2m_FD) {
		R14_1_2M_FD = r14_1_2m_FD;
	}
	public BigDecimal getR14_4_6M_FD() {
		return R14_4_6M_FD;
	}
	public void setR14_4_6M_FD(BigDecimal r14_4_6m_FD) {
		R14_4_6M_FD = r14_4_6m_FD;
	}
	public BigDecimal getR14_7_12M_FD() {
		return R14_7_12M_FD;
	}
	public void setR14_7_12M_FD(BigDecimal r14_7_12m_FD) {
		R14_7_12M_FD = r14_7_12m_FD;
	}
	public BigDecimal getR14_13_18M_FD() {
		return R14_13_18M_FD;
	}
	public void setR14_13_18M_FD(BigDecimal r14_13_18m_FD) {
		R14_13_18M_FD = r14_13_18m_FD;
	}
	public BigDecimal getR14_19_24M_FD() {
		return R14_19_24M_FD;
	}
	public void setR14_19_24M_FD(BigDecimal r14_19_24m_FD) {
		R14_19_24M_FD = r14_19_24m_FD;
	}
	public BigDecimal getR14_OVER24_FD() {
		return R14_OVER24_FD;
	}
	public void setR14_OVER24_FD(BigDecimal r14_OVER24_FD) {
		R14_OVER24_FD = r14_OVER24_FD;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public BigDecimal getR14_NOACC() {
		return R14_NOACC;
	}
	public void setR14_NOACC(BigDecimal r14_NOACC) {
		R14_NOACC = r14_NOACC;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_CURRENT() {
		return R15_CURRENT;
	}
	public void setR15_CURRENT(BigDecimal r15_CURRENT) {
		R15_CURRENT = r15_CURRENT;
	}
	public BigDecimal getR15_CALL() {
		return R15_CALL;
	}
	public void setR15_CALL(BigDecimal r15_CALL) {
		R15_CALL = r15_CALL;
	}
	public BigDecimal getR15_SAVINGS() {
		return R15_SAVINGS;
	}
	public void setR15_SAVINGS(BigDecimal r15_SAVINGS) {
		R15_SAVINGS = r15_SAVINGS;
	}
	public BigDecimal getR15_0_31D_NOTICE() {
		return R15_0_31D_NOTICE;
	}
	public void setR15_0_31D_NOTICE(BigDecimal r15_0_31d_NOTICE) {
		R15_0_31D_NOTICE = r15_0_31d_NOTICE;
	}
	public BigDecimal getR15_32_88D_NOTICE() {
		return R15_32_88D_NOTICE;
	}
	public void setR15_32_88D_NOTICE(BigDecimal r15_32_88d_NOTICE) {
		R15_32_88D_NOTICE = r15_32_88d_NOTICE;
	}
	public BigDecimal getR15_91D_DEPOSIT() {
		return R15_91D_DEPOSIT;
	}
	public void setR15_91D_DEPOSIT(BigDecimal r15_91d_DEPOSIT) {
		R15_91D_DEPOSIT = r15_91d_DEPOSIT;
	}
	public BigDecimal getR15_1_2M_FD() {
		return R15_1_2M_FD;
	}
	public void setR15_1_2M_FD(BigDecimal r15_1_2m_FD) {
		R15_1_2M_FD = r15_1_2m_FD;
	}
	public BigDecimal getR15_4_6M_FD() {
		return R15_4_6M_FD;
	}
	public void setR15_4_6M_FD(BigDecimal r15_4_6m_FD) {
		R15_4_6M_FD = r15_4_6m_FD;
	}
	public BigDecimal getR15_7_12M_FD() {
		return R15_7_12M_FD;
	}
	public void setR15_7_12M_FD(BigDecimal r15_7_12m_FD) {
		R15_7_12M_FD = r15_7_12m_FD;
	}
	public BigDecimal getR15_13_18M_FD() {
		return R15_13_18M_FD;
	}
	public void setR15_13_18M_FD(BigDecimal r15_13_18m_FD) {
		R15_13_18M_FD = r15_13_18m_FD;
	}
	public BigDecimal getR15_19_24M_FD() {
		return R15_19_24M_FD;
	}
	public void setR15_19_24M_FD(BigDecimal r15_19_24m_FD) {
		R15_19_24M_FD = r15_19_24m_FD;
	}
	public BigDecimal getR15_OVER24_FD() {
		return R15_OVER24_FD;
	}
	public void setR15_OVER24_FD(BigDecimal r15_OVER24_FD) {
		R15_OVER24_FD = r15_OVER24_FD;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public BigDecimal getR15_NOACC() {
		return R15_NOACC;
	}
	public void setR15_NOACC(BigDecimal r15_NOACC) {
		R15_NOACC = r15_NOACC;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_CURRENT() {
		return R16_CURRENT;
	}
	public void setR16_CURRENT(BigDecimal r16_CURRENT) {
		R16_CURRENT = r16_CURRENT;
	}
	public BigDecimal getR16_CALL() {
		return R16_CALL;
	}
	public void setR16_CALL(BigDecimal r16_CALL) {
		R16_CALL = r16_CALL;
	}
	public BigDecimal getR16_SAVINGS() {
		return R16_SAVINGS;
	}
	public void setR16_SAVINGS(BigDecimal r16_SAVINGS) {
		R16_SAVINGS = r16_SAVINGS;
	}
	public BigDecimal getR16_0_31D_NOTICE() {
		return R16_0_31D_NOTICE;
	}
	public void setR16_0_31D_NOTICE(BigDecimal r16_0_31d_NOTICE) {
		R16_0_31D_NOTICE = r16_0_31d_NOTICE;
	}
	public BigDecimal getR16_32_88D_NOTICE() {
		return R16_32_88D_NOTICE;
	}
	public void setR16_32_88D_NOTICE(BigDecimal r16_32_88d_NOTICE) {
		R16_32_88D_NOTICE = r16_32_88d_NOTICE;
	}
	public BigDecimal getR16_91D_DEPOSIT() {
		return R16_91D_DEPOSIT;
	}
	public void setR16_91D_DEPOSIT(BigDecimal r16_91d_DEPOSIT) {
		R16_91D_DEPOSIT = r16_91d_DEPOSIT;
	}
	public BigDecimal getR16_1_2M_FD() {
		return R16_1_2M_FD;
	}
	public void setR16_1_2M_FD(BigDecimal r16_1_2m_FD) {
		R16_1_2M_FD = r16_1_2m_FD;
	}
	public BigDecimal getR16_4_6M_FD() {
		return R16_4_6M_FD;
	}
	public void setR16_4_6M_FD(BigDecimal r16_4_6m_FD) {
		R16_4_6M_FD = r16_4_6m_FD;
	}
	public BigDecimal getR16_7_12M_FD() {
		return R16_7_12M_FD;
	}
	public void setR16_7_12M_FD(BigDecimal r16_7_12m_FD) {
		R16_7_12M_FD = r16_7_12m_FD;
	}
	public BigDecimal getR16_13_18M_FD() {
		return R16_13_18M_FD;
	}
	public void setR16_13_18M_FD(BigDecimal r16_13_18m_FD) {
		R16_13_18M_FD = r16_13_18m_FD;
	}
	public BigDecimal getR16_19_24M_FD() {
		return R16_19_24M_FD;
	}
	public void setR16_19_24M_FD(BigDecimal r16_19_24m_FD) {
		R16_19_24M_FD = r16_19_24m_FD;
	}
	public BigDecimal getR16_OVER24_FD() {
		return R16_OVER24_FD;
	}
	public void setR16_OVER24_FD(BigDecimal r16_OVER24_FD) {
		R16_OVER24_FD = r16_OVER24_FD;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}
	public BigDecimal getR16_NOACC() {
		return R16_NOACC;
	}
	public void setR16_NOACC(BigDecimal r16_NOACC) {
		R16_NOACC = r16_NOACC;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_CURRENT() {
		return R17_CURRENT;
	}
	public void setR17_CURRENT(BigDecimal r17_CURRENT) {
		R17_CURRENT = r17_CURRENT;
	}
	public BigDecimal getR17_CALL() {
		return R17_CALL;
	}
	public void setR17_CALL(BigDecimal r17_CALL) {
		R17_CALL = r17_CALL;
	}
	public BigDecimal getR17_SAVINGS() {
		return R17_SAVINGS;
	}
	public void setR17_SAVINGS(BigDecimal r17_SAVINGS) {
		R17_SAVINGS = r17_SAVINGS;
	}
	public BigDecimal getR17_0_31D_NOTICE() {
		return R17_0_31D_NOTICE;
	}
	public void setR17_0_31D_NOTICE(BigDecimal r17_0_31d_NOTICE) {
		R17_0_31D_NOTICE = r17_0_31d_NOTICE;
	}
	public BigDecimal getR17_32_88D_NOTICE() {
		return R17_32_88D_NOTICE;
	}
	public void setR17_32_88D_NOTICE(BigDecimal r17_32_88d_NOTICE) {
		R17_32_88D_NOTICE = r17_32_88d_NOTICE;
	}
	public BigDecimal getR17_91D_DEPOSIT() {
		return R17_91D_DEPOSIT;
	}
	public void setR17_91D_DEPOSIT(BigDecimal r17_91d_DEPOSIT) {
		R17_91D_DEPOSIT = r17_91d_DEPOSIT;
	}
	public BigDecimal getR17_1_2M_FD() {
		return R17_1_2M_FD;
	}
	public void setR17_1_2M_FD(BigDecimal r17_1_2m_FD) {
		R17_1_2M_FD = r17_1_2m_FD;
	}
	public BigDecimal getR17_4_6M_FD() {
		return R17_4_6M_FD;
	}
	public void setR17_4_6M_FD(BigDecimal r17_4_6m_FD) {
		R17_4_6M_FD = r17_4_6m_FD;
	}
	public BigDecimal getR17_7_12M_FD() {
		return R17_7_12M_FD;
	}
	public void setR17_7_12M_FD(BigDecimal r17_7_12m_FD) {
		R17_7_12M_FD = r17_7_12m_FD;
	}
	public BigDecimal getR17_13_18M_FD() {
		return R17_13_18M_FD;
	}
	public void setR17_13_18M_FD(BigDecimal r17_13_18m_FD) {
		R17_13_18M_FD = r17_13_18m_FD;
	}
	public BigDecimal getR17_19_24M_FD() {
		return R17_19_24M_FD;
	}
	public void setR17_19_24M_FD(BigDecimal r17_19_24m_FD) {
		R17_19_24M_FD = r17_19_24m_FD;
	}
	public BigDecimal getR17_OVER24_FD() {
		return R17_OVER24_FD;
	}
	public void setR17_OVER24_FD(BigDecimal r17_OVER24_FD) {
		R17_OVER24_FD = r17_OVER24_FD;
	}
	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}
	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}
	public BigDecimal getR17_NOACC() {
		return R17_NOACC;
	}
	public void setR17_NOACC(BigDecimal r17_NOACC) {
		R17_NOACC = r17_NOACC;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_CURRENT() {
		return R18_CURRENT;
	}
	public void setR18_CURRENT(BigDecimal r18_CURRENT) {
		R18_CURRENT = r18_CURRENT;
	}
	public BigDecimal getR18_CALL() {
		return R18_CALL;
	}
	public void setR18_CALL(BigDecimal r18_CALL) {
		R18_CALL = r18_CALL;
	}
	public BigDecimal getR18_SAVINGS() {
		return R18_SAVINGS;
	}
	public void setR18_SAVINGS(BigDecimal r18_SAVINGS) {
		R18_SAVINGS = r18_SAVINGS;
	}
	public BigDecimal getR18_0_31D_NOTICE() {
		return R18_0_31D_NOTICE;
	}
	public void setR18_0_31D_NOTICE(BigDecimal r18_0_31d_NOTICE) {
		R18_0_31D_NOTICE = r18_0_31d_NOTICE;
	}
	public BigDecimal getR18_32_88D_NOTICE() {
		return R18_32_88D_NOTICE;
	}
	public void setR18_32_88D_NOTICE(BigDecimal r18_32_88d_NOTICE) {
		R18_32_88D_NOTICE = r18_32_88d_NOTICE;
	}
	public BigDecimal getR18_91D_DEPOSIT() {
		return R18_91D_DEPOSIT;
	}
	public void setR18_91D_DEPOSIT(BigDecimal r18_91d_DEPOSIT) {
		R18_91D_DEPOSIT = r18_91d_DEPOSIT;
	}
	public BigDecimal getR18_1_2M_FD() {
		return R18_1_2M_FD;
	}
	public void setR18_1_2M_FD(BigDecimal r18_1_2m_FD) {
		R18_1_2M_FD = r18_1_2m_FD;
	}
	public BigDecimal getR18_4_6M_FD() {
		return R18_4_6M_FD;
	}
	public void setR18_4_6M_FD(BigDecimal r18_4_6m_FD) {
		R18_4_6M_FD = r18_4_6m_FD;
	}
	public BigDecimal getR18_7_12M_FD() {
		return R18_7_12M_FD;
	}
	public void setR18_7_12M_FD(BigDecimal r18_7_12m_FD) {
		R18_7_12M_FD = r18_7_12m_FD;
	}
	public BigDecimal getR18_13_18M_FD() {
		return R18_13_18M_FD;
	}
	public void setR18_13_18M_FD(BigDecimal r18_13_18m_FD) {
		R18_13_18M_FD = r18_13_18m_FD;
	}
	public BigDecimal getR18_19_24M_FD() {
		return R18_19_24M_FD;
	}
	public void setR18_19_24M_FD(BigDecimal r18_19_24m_FD) {
		R18_19_24M_FD = r18_19_24m_FD;
	}
	public BigDecimal getR18_OVER24_FD() {
		return R18_OVER24_FD;
	}
	public void setR18_OVER24_FD(BigDecimal r18_OVER24_FD) {
		R18_OVER24_FD = r18_OVER24_FD;
	}
	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}
	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}
	public BigDecimal getR18_NOACC() {
		return R18_NOACC;
	}
	public void setR18_NOACC(BigDecimal r18_NOACC) {
		R18_NOACC = r18_NOACC;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_CURRENT() {
		return R19_CURRENT;
	}
	public void setR19_CURRENT(BigDecimal r19_CURRENT) {
		R19_CURRENT = r19_CURRENT;
	}
	public BigDecimal getR19_CALL() {
		return R19_CALL;
	}
	public void setR19_CALL(BigDecimal r19_CALL) {
		R19_CALL = r19_CALL;
	}
	public BigDecimal getR19_SAVINGS() {
		return R19_SAVINGS;
	}
	public void setR19_SAVINGS(BigDecimal r19_SAVINGS) {
		R19_SAVINGS = r19_SAVINGS;
	}
	public BigDecimal getR19_0_31D_NOTICE() {
		return R19_0_31D_NOTICE;
	}
	public void setR19_0_31D_NOTICE(BigDecimal r19_0_31d_NOTICE) {
		R19_0_31D_NOTICE = r19_0_31d_NOTICE;
	}
	public BigDecimal getR19_32_88D_NOTICE() {
		return R19_32_88D_NOTICE;
	}
	public void setR19_32_88D_NOTICE(BigDecimal r19_32_88d_NOTICE) {
		R19_32_88D_NOTICE = r19_32_88d_NOTICE;
	}
	public BigDecimal getR19_91D_DEPOSIT() {
		return R19_91D_DEPOSIT;
	}
	public void setR19_91D_DEPOSIT(BigDecimal r19_91d_DEPOSIT) {
		R19_91D_DEPOSIT = r19_91d_DEPOSIT;
	}
	public BigDecimal getR19_1_2M_FD() {
		return R19_1_2M_FD;
	}
	public void setR19_1_2M_FD(BigDecimal r19_1_2m_FD) {
		R19_1_2M_FD = r19_1_2m_FD;
	}
	public BigDecimal getR19_4_6M_FD() {
		return R19_4_6M_FD;
	}
	public void setR19_4_6M_FD(BigDecimal r19_4_6m_FD) {
		R19_4_6M_FD = r19_4_6m_FD;
	}
	public BigDecimal getR19_7_12M_FD() {
		return R19_7_12M_FD;
	}
	public void setR19_7_12M_FD(BigDecimal r19_7_12m_FD) {
		R19_7_12M_FD = r19_7_12m_FD;
	}
	public BigDecimal getR19_13_18M_FD() {
		return R19_13_18M_FD;
	}
	public void setR19_13_18M_FD(BigDecimal r19_13_18m_FD) {
		R19_13_18M_FD = r19_13_18m_FD;
	}
	public BigDecimal getR19_19_24M_FD() {
		return R19_19_24M_FD;
	}
	public void setR19_19_24M_FD(BigDecimal r19_19_24m_FD) {
		R19_19_24M_FD = r19_19_24m_FD;
	}
	public BigDecimal getR19_OVER24_FD() {
		return R19_OVER24_FD;
	}
	public void setR19_OVER24_FD(BigDecimal r19_OVER24_FD) {
		R19_OVER24_FD = r19_OVER24_FD;
	}
	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}
	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}
	public BigDecimal getR19_NOACC() {
		return R19_NOACC;
	}
	public void setR19_NOACC(BigDecimal r19_NOACC) {
		R19_NOACC = r19_NOACC;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_CURRENT() {
		return R20_CURRENT;
	}
	public void setR20_CURRENT(BigDecimal r20_CURRENT) {
		R20_CURRENT = r20_CURRENT;
	}
	public BigDecimal getR20_CALL() {
		return R20_CALL;
	}
	public void setR20_CALL(BigDecimal r20_CALL) {
		R20_CALL = r20_CALL;
	}
	public BigDecimal getR20_SAVINGS() {
		return R20_SAVINGS;
	}
	public void setR20_SAVINGS(BigDecimal r20_SAVINGS) {
		R20_SAVINGS = r20_SAVINGS;
	}
	public BigDecimal getR20_0_31D_NOTICE() {
		return R20_0_31D_NOTICE;
	}
	public void setR20_0_31D_NOTICE(BigDecimal r20_0_31d_NOTICE) {
		R20_0_31D_NOTICE = r20_0_31d_NOTICE;
	}
	public BigDecimal getR20_32_88D_NOTICE() {
		return R20_32_88D_NOTICE;
	}
	public void setR20_32_88D_NOTICE(BigDecimal r20_32_88d_NOTICE) {
		R20_32_88D_NOTICE = r20_32_88d_NOTICE;
	}
	public BigDecimal getR20_91D_DEPOSIT() {
		return R20_91D_DEPOSIT;
	}
	public void setR20_91D_DEPOSIT(BigDecimal r20_91d_DEPOSIT) {
		R20_91D_DEPOSIT = r20_91d_DEPOSIT;
	}
	public BigDecimal getR20_1_2M_FD() {
		return R20_1_2M_FD;
	}
	public void setR20_1_2M_FD(BigDecimal r20_1_2m_FD) {
		R20_1_2M_FD = r20_1_2m_FD;
	}
	public BigDecimal getR20_4_6M_FD() {
		return R20_4_6M_FD;
	}
	public void setR20_4_6M_FD(BigDecimal r20_4_6m_FD) {
		R20_4_6M_FD = r20_4_6m_FD;
	}
	public BigDecimal getR20_7_12M_FD() {
		return R20_7_12M_FD;
	}
	public void setR20_7_12M_FD(BigDecimal r20_7_12m_FD) {
		R20_7_12M_FD = r20_7_12m_FD;
	}
	public BigDecimal getR20_13_18M_FD() {
		return R20_13_18M_FD;
	}
	public void setR20_13_18M_FD(BigDecimal r20_13_18m_FD) {
		R20_13_18M_FD = r20_13_18m_FD;
	}
	public BigDecimal getR20_19_24M_FD() {
		return R20_19_24M_FD;
	}
	public void setR20_19_24M_FD(BigDecimal r20_19_24m_FD) {
		R20_19_24M_FD = r20_19_24m_FD;
	}
	public BigDecimal getR20_OVER24_FD() {
		return R20_OVER24_FD;
	}
	public void setR20_OVER24_FD(BigDecimal r20_OVER24_FD) {
		R20_OVER24_FD = r20_OVER24_FD;
	}
	public BigDecimal getR20_TOTAL() {
		return R20_TOTAL;
	}
	public void setR20_TOTAL(BigDecimal r20_TOTAL) {
		R20_TOTAL = r20_TOTAL;
	}
	public BigDecimal getR20_NOACC() {
		return R20_NOACC;
	}
	public void setR20_NOACC(BigDecimal r20_NOACC) {
		R20_NOACC = r20_NOACC;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_CURRENT() {
		return R21_CURRENT;
	}
	public void setR21_CURRENT(BigDecimal r21_CURRENT) {
		R21_CURRENT = r21_CURRENT;
	}
	public BigDecimal getR21_CALL() {
		return R21_CALL;
	}
	public void setR21_CALL(BigDecimal r21_CALL) {
		R21_CALL = r21_CALL;
	}
	public BigDecimal getR21_SAVINGS() {
		return R21_SAVINGS;
	}
	public void setR21_SAVINGS(BigDecimal r21_SAVINGS) {
		R21_SAVINGS = r21_SAVINGS;
	}
	public BigDecimal getR21_0_31D_NOTICE() {
		return R21_0_31D_NOTICE;
	}
	public void setR21_0_31D_NOTICE(BigDecimal r21_0_31d_NOTICE) {
		R21_0_31D_NOTICE = r21_0_31d_NOTICE;
	}
	public BigDecimal getR21_32_88D_NOTICE() {
		return R21_32_88D_NOTICE;
	}
	public void setR21_32_88D_NOTICE(BigDecimal r21_32_88d_NOTICE) {
		R21_32_88D_NOTICE = r21_32_88d_NOTICE;
	}
	public BigDecimal getR21_91D_DEPOSIT() {
		return R21_91D_DEPOSIT;
	}
	public void setR21_91D_DEPOSIT(BigDecimal r21_91d_DEPOSIT) {
		R21_91D_DEPOSIT = r21_91d_DEPOSIT;
	}
	public BigDecimal getR21_1_2M_FD() {
		return R21_1_2M_FD;
	}
	public void setR21_1_2M_FD(BigDecimal r21_1_2m_FD) {
		R21_1_2M_FD = r21_1_2m_FD;
	}
	public BigDecimal getR21_4_6M_FD() {
		return R21_4_6M_FD;
	}
	public void setR21_4_6M_FD(BigDecimal r21_4_6m_FD) {
		R21_4_6M_FD = r21_4_6m_FD;
	}
	public BigDecimal getR21_7_12M_FD() {
		return R21_7_12M_FD;
	}
	public void setR21_7_12M_FD(BigDecimal r21_7_12m_FD) {
		R21_7_12M_FD = r21_7_12m_FD;
	}
	public BigDecimal getR21_13_18M_FD() {
		return R21_13_18M_FD;
	}
	public void setR21_13_18M_FD(BigDecimal r21_13_18m_FD) {
		R21_13_18M_FD = r21_13_18m_FD;
	}
	public BigDecimal getR21_19_24M_FD() {
		return R21_19_24M_FD;
	}
	public void setR21_19_24M_FD(BigDecimal r21_19_24m_FD) {
		R21_19_24M_FD = r21_19_24m_FD;
	}
	public BigDecimal getR21_OVER24_FD() {
		return R21_OVER24_FD;
	}
	public void setR21_OVER24_FD(BigDecimal r21_OVER24_FD) {
		R21_OVER24_FD = r21_OVER24_FD;
	}
	public BigDecimal getR21_TOTAL() {
		return R21_TOTAL;
	}
	public void setR21_TOTAL(BigDecimal r21_TOTAL) {
		R21_TOTAL = r21_TOTAL;
	}
	public BigDecimal getR21_NOACC() {
		return R21_NOACC;
	}
	public void setR21_NOACC(BigDecimal r21_NOACC) {
		R21_NOACC = r21_NOACC;
	}
	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}
	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}
	public BigDecimal getR22_CURRENT() {
		return R22_CURRENT;
	}
	public void setR22_CURRENT(BigDecimal r22_CURRENT) {
		R22_CURRENT = r22_CURRENT;
	}
	public BigDecimal getR22_CALL() {
		return R22_CALL;
	}
	public void setR22_CALL(BigDecimal r22_CALL) {
		R22_CALL = r22_CALL;
	}
	public BigDecimal getR22_SAVINGS() {
		return R22_SAVINGS;
	}
	public void setR22_SAVINGS(BigDecimal r22_SAVINGS) {
		R22_SAVINGS = r22_SAVINGS;
	}
	public BigDecimal getR22_0_31D_NOTICE() {
		return R22_0_31D_NOTICE;
	}
	public void setR22_0_31D_NOTICE(BigDecimal r22_0_31d_NOTICE) {
		R22_0_31D_NOTICE = r22_0_31d_NOTICE;
	}
	public BigDecimal getR22_32_88D_NOTICE() {
		return R22_32_88D_NOTICE;
	}
	public void setR22_32_88D_NOTICE(BigDecimal r22_32_88d_NOTICE) {
		R22_32_88D_NOTICE = r22_32_88d_NOTICE;
	}
	public BigDecimal getR22_91D_DEPOSIT() {
		return R22_91D_DEPOSIT;
	}
	public void setR22_91D_DEPOSIT(BigDecimal r22_91d_DEPOSIT) {
		R22_91D_DEPOSIT = r22_91d_DEPOSIT;
	}
	public BigDecimal getR22_1_2M_FD() {
		return R22_1_2M_FD;
	}
	public void setR22_1_2M_FD(BigDecimal r22_1_2m_FD) {
		R22_1_2M_FD = r22_1_2m_FD;
	}
	public BigDecimal getR22_4_6M_FD() {
		return R22_4_6M_FD;
	}
	public void setR22_4_6M_FD(BigDecimal r22_4_6m_FD) {
		R22_4_6M_FD = r22_4_6m_FD;
	}
	public BigDecimal getR22_7_12M_FD() {
		return R22_7_12M_FD;
	}
	public void setR22_7_12M_FD(BigDecimal r22_7_12m_FD) {
		R22_7_12M_FD = r22_7_12m_FD;
	}
	public BigDecimal getR22_13_18M_FD() {
		return R22_13_18M_FD;
	}
	public void setR22_13_18M_FD(BigDecimal r22_13_18m_FD) {
		R22_13_18M_FD = r22_13_18m_FD;
	}
	public BigDecimal getR22_19_24M_FD() {
		return R22_19_24M_FD;
	}
	public void setR22_19_24M_FD(BigDecimal r22_19_24m_FD) {
		R22_19_24M_FD = r22_19_24m_FD;
	}
	public BigDecimal getR22_OVER24_FD() {
		return R22_OVER24_FD;
	}
	public void setR22_OVER24_FD(BigDecimal r22_OVER24_FD) {
		R22_OVER24_FD = r22_OVER24_FD;
	}
	public BigDecimal getR22_TOTAL() {
		return R22_TOTAL;
	}
	public void setR22_TOTAL(BigDecimal r22_TOTAL) {
		R22_TOTAL = r22_TOTAL;
	}
	public BigDecimal getR22_NOACC() {
		return R22_NOACC;
	}
	public void setR22_NOACC(BigDecimal r22_NOACC) {
		R22_NOACC = r22_NOACC;
	}
	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}
	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}
	public BigDecimal getR23_CURRENT() {
		return R23_CURRENT;
	}
	public void setR23_CURRENT(BigDecimal r23_CURRENT) {
		R23_CURRENT = r23_CURRENT;
	}
	public BigDecimal getR23_CALL() {
		return R23_CALL;
	}
	public void setR23_CALL(BigDecimal r23_CALL) {
		R23_CALL = r23_CALL;
	}
	public BigDecimal getR23_SAVINGS() {
		return R23_SAVINGS;
	}
	public void setR23_SAVINGS(BigDecimal r23_SAVINGS) {
		R23_SAVINGS = r23_SAVINGS;
	}
	public BigDecimal getR23_0_31D_NOTICE() {
		return R23_0_31D_NOTICE;
	}
	public void setR23_0_31D_NOTICE(BigDecimal r23_0_31d_NOTICE) {
		R23_0_31D_NOTICE = r23_0_31d_NOTICE;
	}
	public BigDecimal getR23_32_88D_NOTICE() {
		return R23_32_88D_NOTICE;
	}
	public void setR23_32_88D_NOTICE(BigDecimal r23_32_88d_NOTICE) {
		R23_32_88D_NOTICE = r23_32_88d_NOTICE;
	}
	public BigDecimal getR23_91D_DEPOSIT() {
		return R23_91D_DEPOSIT;
	}
	public void setR23_91D_DEPOSIT(BigDecimal r23_91d_DEPOSIT) {
		R23_91D_DEPOSIT = r23_91d_DEPOSIT;
	}
	public BigDecimal getR23_1_2M_FD() {
		return R23_1_2M_FD;
	}
	public void setR23_1_2M_FD(BigDecimal r23_1_2m_FD) {
		R23_1_2M_FD = r23_1_2m_FD;
	}
	public BigDecimal getR23_4_6M_FD() {
		return R23_4_6M_FD;
	}
	public void setR23_4_6M_FD(BigDecimal r23_4_6m_FD) {
		R23_4_6M_FD = r23_4_6m_FD;
	}
	public BigDecimal getR23_7_12M_FD() {
		return R23_7_12M_FD;
	}
	public void setR23_7_12M_FD(BigDecimal r23_7_12m_FD) {
		R23_7_12M_FD = r23_7_12m_FD;
	}
	public BigDecimal getR23_13_18M_FD() {
		return R23_13_18M_FD;
	}
	public void setR23_13_18M_FD(BigDecimal r23_13_18m_FD) {
		R23_13_18M_FD = r23_13_18m_FD;
	}
	public BigDecimal getR23_19_24M_FD() {
		return R23_19_24M_FD;
	}
	public void setR23_19_24M_FD(BigDecimal r23_19_24m_FD) {
		R23_19_24M_FD = r23_19_24m_FD;
	}
	public BigDecimal getR23_OVER24_FD() {
		return R23_OVER24_FD;
	}
	public void setR23_OVER24_FD(BigDecimal r23_OVER24_FD) {
		R23_OVER24_FD = r23_OVER24_FD;
	}
	public BigDecimal getR23_TOTAL() {
		return R23_TOTAL;
	}
	public void setR23_TOTAL(BigDecimal r23_TOTAL) {
		R23_TOTAL = r23_TOTAL;
	}
	public BigDecimal getR23_NOACC() {
		return R23_NOACC;
	}
	public void setR23_NOACC(BigDecimal r23_NOACC) {
		R23_NOACC = r23_NOACC;
	}
	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}
	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}
	public BigDecimal getR24_CURRENT() {
		return R24_CURRENT;
	}
	public void setR24_CURRENT(BigDecimal r24_CURRENT) {
		R24_CURRENT = r24_CURRENT;
	}
	public BigDecimal getR24_CALL() {
		return R24_CALL;
	}
	public void setR24_CALL(BigDecimal r24_CALL) {
		R24_CALL = r24_CALL;
	}
	public BigDecimal getR24_SAVINGS() {
		return R24_SAVINGS;
	}
	public void setR24_SAVINGS(BigDecimal r24_SAVINGS) {
		R24_SAVINGS = r24_SAVINGS;
	}
	public BigDecimal getR24_0_31D_NOTICE() {
		return R24_0_31D_NOTICE;
	}
	public void setR24_0_31D_NOTICE(BigDecimal r24_0_31d_NOTICE) {
		R24_0_31D_NOTICE = r24_0_31d_NOTICE;
	}
	public BigDecimal getR24_32_88D_NOTICE() {
		return R24_32_88D_NOTICE;
	}
	public void setR24_32_88D_NOTICE(BigDecimal r24_32_88d_NOTICE) {
		R24_32_88D_NOTICE = r24_32_88d_NOTICE;
	}
	public BigDecimal getR24_91D_DEPOSIT() {
		return R24_91D_DEPOSIT;
	}
	public void setR24_91D_DEPOSIT(BigDecimal r24_91d_DEPOSIT) {
		R24_91D_DEPOSIT = r24_91d_DEPOSIT;
	}
	public BigDecimal getR24_1_2M_FD() {
		return R24_1_2M_FD;
	}
	public void setR24_1_2M_FD(BigDecimal r24_1_2m_FD) {
		R24_1_2M_FD = r24_1_2m_FD;
	}
	public BigDecimal getR24_4_6M_FD() {
		return R24_4_6M_FD;
	}
	public void setR24_4_6M_FD(BigDecimal r24_4_6m_FD) {
		R24_4_6M_FD = r24_4_6m_FD;
	}
	public BigDecimal getR24_7_12M_FD() {
		return R24_7_12M_FD;
	}
	public void setR24_7_12M_FD(BigDecimal r24_7_12m_FD) {
		R24_7_12M_FD = r24_7_12m_FD;
	}
	public BigDecimal getR24_13_18M_FD() {
		return R24_13_18M_FD;
	}
	public void setR24_13_18M_FD(BigDecimal r24_13_18m_FD) {
		R24_13_18M_FD = r24_13_18m_FD;
	}
	public BigDecimal getR24_19_24M_FD() {
		return R24_19_24M_FD;
	}
	public void setR24_19_24M_FD(BigDecimal r24_19_24m_FD) {
		R24_19_24M_FD = r24_19_24m_FD;
	}
	public BigDecimal getR24_OVER24_FD() {
		return R24_OVER24_FD;
	}
	public void setR24_OVER24_FD(BigDecimal r24_OVER24_FD) {
		R24_OVER24_FD = r24_OVER24_FD;
	}
	public BigDecimal getR24_TOTAL() {
		return R24_TOTAL;
	}
	public void setR24_TOTAL(BigDecimal r24_TOTAL) {
		R24_TOTAL = r24_TOTAL;
	}
	public BigDecimal getR24_NOACC() {
		return R24_NOACC;
	}
	public void setR24_NOACC(BigDecimal r24_NOACC) {
		R24_NOACC = r24_NOACC;
	}
	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}
	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}
	public BigDecimal getR25_CURRENT() {
		return R25_CURRENT;
	}
	public void setR25_CURRENT(BigDecimal r25_CURRENT) {
		R25_CURRENT = r25_CURRENT;
	}
	public BigDecimal getR25_CALL() {
		return R25_CALL;
	}
	public void setR25_CALL(BigDecimal r25_CALL) {
		R25_CALL = r25_CALL;
	}
	public BigDecimal getR25_SAVINGS() {
		return R25_SAVINGS;
	}
	public void setR25_SAVINGS(BigDecimal r25_SAVINGS) {
		R25_SAVINGS = r25_SAVINGS;
	}
	public BigDecimal getR25_0_31D_NOTICE() {
		return R25_0_31D_NOTICE;
	}
	public void setR25_0_31D_NOTICE(BigDecimal r25_0_31d_NOTICE) {
		R25_0_31D_NOTICE = r25_0_31d_NOTICE;
	}
	public BigDecimal getR25_32_88D_NOTICE() {
		return R25_32_88D_NOTICE;
	}
	public void setR25_32_88D_NOTICE(BigDecimal r25_32_88d_NOTICE) {
		R25_32_88D_NOTICE = r25_32_88d_NOTICE;
	}
	public BigDecimal getR25_91D_DEPOSIT() {
		return R25_91D_DEPOSIT;
	}
	public void setR25_91D_DEPOSIT(BigDecimal r25_91d_DEPOSIT) {
		R25_91D_DEPOSIT = r25_91d_DEPOSIT;
	}
	public BigDecimal getR25_1_2M_FD() {
		return R25_1_2M_FD;
	}
	public void setR25_1_2M_FD(BigDecimal r25_1_2m_FD) {
		R25_1_2M_FD = r25_1_2m_FD;
	}
	public BigDecimal getR25_4_6M_FD() {
		return R25_4_6M_FD;
	}
	public void setR25_4_6M_FD(BigDecimal r25_4_6m_FD) {
		R25_4_6M_FD = r25_4_6m_FD;
	}
	public BigDecimal getR25_7_12M_FD() {
		return R25_7_12M_FD;
	}
	public void setR25_7_12M_FD(BigDecimal r25_7_12m_FD) {
		R25_7_12M_FD = r25_7_12m_FD;
	}
	public BigDecimal getR25_13_18M_FD() {
		return R25_13_18M_FD;
	}
	public void setR25_13_18M_FD(BigDecimal r25_13_18m_FD) {
		R25_13_18M_FD = r25_13_18m_FD;
	}
	public BigDecimal getR25_19_24M_FD() {
		return R25_19_24M_FD;
	}
	public void setR25_19_24M_FD(BigDecimal r25_19_24m_FD) {
		R25_19_24M_FD = r25_19_24m_FD;
	}
	public BigDecimal getR25_OVER24_FD() {
		return R25_OVER24_FD;
	}
	public void setR25_OVER24_FD(BigDecimal r25_OVER24_FD) {
		R25_OVER24_FD = r25_OVER24_FD;
	}
	public BigDecimal getR25_TOTAL() {
		return R25_TOTAL;
	}
	public void setR25_TOTAL(BigDecimal r25_TOTAL) {
		R25_TOTAL = r25_TOTAL;
	}
	public BigDecimal getR25_NOACC() {
		return R25_NOACC;
	}
	public void setR25_NOACC(BigDecimal r25_NOACC) {
		R25_NOACC = r25_NOACC;
	}
	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}
	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}
	public BigDecimal getR26_CURRENT() {
		return R26_CURRENT;
	}
	public void setR26_CURRENT(BigDecimal r26_CURRENT) {
		R26_CURRENT = r26_CURRENT;
	}
	public BigDecimal getR26_CALL() {
		return R26_CALL;
	}
	public void setR26_CALL(BigDecimal r26_CALL) {
		R26_CALL = r26_CALL;
	}
	public BigDecimal getR26_SAVINGS() {
		return R26_SAVINGS;
	}
	public void setR26_SAVINGS(BigDecimal r26_SAVINGS) {
		R26_SAVINGS = r26_SAVINGS;
	}
	public BigDecimal getR26_0_31D_NOTICE() {
		return R26_0_31D_NOTICE;
	}
	public void setR26_0_31D_NOTICE(BigDecimal r26_0_31d_NOTICE) {
		R26_0_31D_NOTICE = r26_0_31d_NOTICE;
	}
	public BigDecimal getR26_32_88D_NOTICE() {
		return R26_32_88D_NOTICE;
	}
	public void setR26_32_88D_NOTICE(BigDecimal r26_32_88d_NOTICE) {
		R26_32_88D_NOTICE = r26_32_88d_NOTICE;
	}
	public BigDecimal getR26_91D_DEPOSIT() {
		return R26_91D_DEPOSIT;
	}
	public void setR26_91D_DEPOSIT(BigDecimal r26_91d_DEPOSIT) {
		R26_91D_DEPOSIT = r26_91d_DEPOSIT;
	}
	public BigDecimal getR26_1_2M_FD() {
		return R26_1_2M_FD;
	}
	public void setR26_1_2M_FD(BigDecimal r26_1_2m_FD) {
		R26_1_2M_FD = r26_1_2m_FD;
	}
	public BigDecimal getR26_4_6M_FD() {
		return R26_4_6M_FD;
	}
	public void setR26_4_6M_FD(BigDecimal r26_4_6m_FD) {
		R26_4_6M_FD = r26_4_6m_FD;
	}
	public BigDecimal getR26_7_12M_FD() {
		return R26_7_12M_FD;
	}
	public void setR26_7_12M_FD(BigDecimal r26_7_12m_FD) {
		R26_7_12M_FD = r26_7_12m_FD;
	}
	public BigDecimal getR26_13_18M_FD() {
		return R26_13_18M_FD;
	}
	public void setR26_13_18M_FD(BigDecimal r26_13_18m_FD) {
		R26_13_18M_FD = r26_13_18m_FD;
	}
	public BigDecimal getR26_19_24M_FD() {
		return R26_19_24M_FD;
	}
	public void setR26_19_24M_FD(BigDecimal r26_19_24m_FD) {
		R26_19_24M_FD = r26_19_24m_FD;
	}
	public BigDecimal getR26_OVER24_FD() {
		return R26_OVER24_FD;
	}
	public void setR26_OVER24_FD(BigDecimal r26_OVER24_FD) {
		R26_OVER24_FD = r26_OVER24_FD;
	}
	public BigDecimal getR26_TOTAL() {
		return R26_TOTAL;
	}
	public void setR26_TOTAL(BigDecimal r26_TOTAL) {
		R26_TOTAL = r26_TOTAL;
	}
	public BigDecimal getR26_NOACC() {
		return R26_NOACC;
	}
	public void setR26_NOACC(BigDecimal r26_NOACC) {
		R26_NOACC = r26_NOACC;
	}
	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}
	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}
	public BigDecimal getR32_CURRENT() {
		return R32_CURRENT;
	}
	public void setR32_CURRENT(BigDecimal r32_CURRENT) {
		R32_CURRENT = r32_CURRENT;
	}
	public BigDecimal getR32_CALL() {
		return R32_CALL;
	}
	public void setR32_CALL(BigDecimal r32_CALL) {
		R32_CALL = r32_CALL;
	}
	public BigDecimal getR32_SAVINGS() {
		return R32_SAVINGS;
	}
	public void setR32_SAVINGS(BigDecimal r32_SAVINGS) {
		R32_SAVINGS = r32_SAVINGS;
	}
	public BigDecimal getR32_0_31D_NOTICE() {
		return R32_0_31D_NOTICE;
	}
	public void setR32_0_31D_NOTICE(BigDecimal r32_0_31d_NOTICE) {
		R32_0_31D_NOTICE = r32_0_31d_NOTICE;
	}
	public BigDecimal getR32_32_88D_NOTICE() {
		return R32_32_88D_NOTICE;
	}
	public void setR32_32_88D_NOTICE(BigDecimal r32_32_88d_NOTICE) {
		R32_32_88D_NOTICE = r32_32_88d_NOTICE;
	}
	public BigDecimal getR32_91D_DEPOSIT() {
		return R32_91D_DEPOSIT;
	}
	public void setR32_91D_DEPOSIT(BigDecimal r32_91d_DEPOSIT) {
		R32_91D_DEPOSIT = r32_91d_DEPOSIT;
	}
	public BigDecimal getR32_1_2M_FD() {
		return R32_1_2M_FD;
	}
	public void setR32_1_2M_FD(BigDecimal r32_1_2m_FD) {
		R32_1_2M_FD = r32_1_2m_FD;
	}
	public BigDecimal getR32_4_6M_FD() {
		return R32_4_6M_FD;
	}
	public void setR32_4_6M_FD(BigDecimal r32_4_6m_FD) {
		R32_4_6M_FD = r32_4_6m_FD;
	}
	public BigDecimal getR32_7_12M_FD() {
		return R32_7_12M_FD;
	}
	public void setR32_7_12M_FD(BigDecimal r32_7_12m_FD) {
		R32_7_12M_FD = r32_7_12m_FD;
	}
	public BigDecimal getR32_13_18M_FD() {
		return R32_13_18M_FD;
	}
	public void setR32_13_18M_FD(BigDecimal r32_13_18m_FD) {
		R32_13_18M_FD = r32_13_18m_FD;
	}
	public BigDecimal getR32_19_24M_FD() {
		return R32_19_24M_FD;
	}
	public void setR32_19_24M_FD(BigDecimal r32_19_24m_FD) {
		R32_19_24M_FD = r32_19_24m_FD;
	}
	public BigDecimal getR32_OVER24_FD() {
		return R32_OVER24_FD;
	}
	public void setR32_OVER24_FD(BigDecimal r32_OVER24_FD) {
		R32_OVER24_FD = r32_OVER24_FD;
	}
	public BigDecimal getR32_TOTAL() {
		return R32_TOTAL;
	}
	public void setR32_TOTAL(BigDecimal r32_TOTAL) {
		R32_TOTAL = r32_TOTAL;
	}
	public BigDecimal getR32_NOACC() {
		return R32_NOACC;
	}
	public void setR32_NOACC(BigDecimal r32_NOACC) {
		R32_NOACC = r32_NOACC;
	}
	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}
	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}
	public BigDecimal getR33_CURRENT() {
		return R33_CURRENT;
	}
	public void setR33_CURRENT(BigDecimal r33_CURRENT) {
		R33_CURRENT = r33_CURRENT;
	}
	public BigDecimal getR33_CALL() {
		return R33_CALL;
	}
	public void setR33_CALL(BigDecimal r33_CALL) {
		R33_CALL = r33_CALL;
	}
	public BigDecimal getR33_SAVINGS() {
		return R33_SAVINGS;
	}
	public void setR33_SAVINGS(BigDecimal r33_SAVINGS) {
		R33_SAVINGS = r33_SAVINGS;
	}
	public BigDecimal getR33_0_31D_NOTICE() {
		return R33_0_31D_NOTICE;
	}
	public void setR33_0_31D_NOTICE(BigDecimal r33_0_31d_NOTICE) {
		R33_0_31D_NOTICE = r33_0_31d_NOTICE;
	}
	public BigDecimal getR33_32_88D_NOTICE() {
		return R33_32_88D_NOTICE;
	}
	public void setR33_32_88D_NOTICE(BigDecimal r33_32_88d_NOTICE) {
		R33_32_88D_NOTICE = r33_32_88d_NOTICE;
	}
	public BigDecimal getR33_91D_DEPOSIT() {
		return R33_91D_DEPOSIT;
	}
	public void setR33_91D_DEPOSIT(BigDecimal r33_91d_DEPOSIT) {
		R33_91D_DEPOSIT = r33_91d_DEPOSIT;
	}
	public BigDecimal getR33_1_2M_FD() {
		return R33_1_2M_FD;
	}
	public void setR33_1_2M_FD(BigDecimal r33_1_2m_FD) {
		R33_1_2M_FD = r33_1_2m_FD;
	}
	public BigDecimal getR33_4_6M_FD() {
		return R33_4_6M_FD;
	}
	public void setR33_4_6M_FD(BigDecimal r33_4_6m_FD) {
		R33_4_6M_FD = r33_4_6m_FD;
	}
	public BigDecimal getR33_7_12M_FD() {
		return R33_7_12M_FD;
	}
	public void setR33_7_12M_FD(BigDecimal r33_7_12m_FD) {
		R33_7_12M_FD = r33_7_12m_FD;
	}
	public BigDecimal getR33_13_18M_FD() {
		return R33_13_18M_FD;
	}
	public void setR33_13_18M_FD(BigDecimal r33_13_18m_FD) {
		R33_13_18M_FD = r33_13_18m_FD;
	}
	public BigDecimal getR33_19_24M_FD() {
		return R33_19_24M_FD;
	}
	public void setR33_19_24M_FD(BigDecimal r33_19_24m_FD) {
		R33_19_24M_FD = r33_19_24m_FD;
	}
	public BigDecimal getR33_OVER24_FD() {
		return R33_OVER24_FD;
	}
	public void setR33_OVER24_FD(BigDecimal r33_OVER24_FD) {
		R33_OVER24_FD = r33_OVER24_FD;
	}
	public BigDecimal getR33_TOTAL() {
		return R33_TOTAL;
	}
	public void setR33_TOTAL(BigDecimal r33_TOTAL) {
		R33_TOTAL = r33_TOTAL;
	}
	public BigDecimal getR33_NOACC() {
		return R33_NOACC;
	}
	public void setR33_NOACC(BigDecimal r33_NOACC) {
		R33_NOACC = r33_NOACC;
	}
	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}
	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}
	public BigDecimal getR34_CURRENT() {
		return R34_CURRENT;
	}
	public void setR34_CURRENT(BigDecimal r34_CURRENT) {
		R34_CURRENT = r34_CURRENT;
	}
	public BigDecimal getR34_CALL() {
		return R34_CALL;
	}
	public void setR34_CALL(BigDecimal r34_CALL) {
		R34_CALL = r34_CALL;
	}
	public BigDecimal getR34_SAVINGS() {
		return R34_SAVINGS;
	}
	public void setR34_SAVINGS(BigDecimal r34_SAVINGS) {
		R34_SAVINGS = r34_SAVINGS;
	}
	public BigDecimal getR34_0_31D_NOTICE() {
		return R34_0_31D_NOTICE;
	}
	public void setR34_0_31D_NOTICE(BigDecimal r34_0_31d_NOTICE) {
		R34_0_31D_NOTICE = r34_0_31d_NOTICE;
	}
	public BigDecimal getR34_32_88D_NOTICE() {
		return R34_32_88D_NOTICE;
	}
	public void setR34_32_88D_NOTICE(BigDecimal r34_32_88d_NOTICE) {
		R34_32_88D_NOTICE = r34_32_88d_NOTICE;
	}
	public BigDecimal getR34_91D_DEPOSIT() {
		return R34_91D_DEPOSIT;
	}
	public void setR34_91D_DEPOSIT(BigDecimal r34_91d_DEPOSIT) {
		R34_91D_DEPOSIT = r34_91d_DEPOSIT;
	}
	public BigDecimal getR34_1_2M_FD() {
		return R34_1_2M_FD;
	}
	public void setR34_1_2M_FD(BigDecimal r34_1_2m_FD) {
		R34_1_2M_FD = r34_1_2m_FD;
	}
	public BigDecimal getR34_4_6M_FD() {
		return R34_4_6M_FD;
	}
	public void setR34_4_6M_FD(BigDecimal r34_4_6m_FD) {
		R34_4_6M_FD = r34_4_6m_FD;
	}
	public BigDecimal getR34_7_12M_FD() {
		return R34_7_12M_FD;
	}
	public void setR34_7_12M_FD(BigDecimal r34_7_12m_FD) {
		R34_7_12M_FD = r34_7_12m_FD;
	}
	public BigDecimal getR34_13_18M_FD() {
		return R34_13_18M_FD;
	}
	public void setR34_13_18M_FD(BigDecimal r34_13_18m_FD) {
		R34_13_18M_FD = r34_13_18m_FD;
	}
	public BigDecimal getR34_19_24M_FD() {
		return R34_19_24M_FD;
	}
	public void setR34_19_24M_FD(BigDecimal r34_19_24m_FD) {
		R34_19_24M_FD = r34_19_24m_FD;
	}
	public BigDecimal getR34_OVER24_FD() {
		return R34_OVER24_FD;
	}
	public void setR34_OVER24_FD(BigDecimal r34_OVER24_FD) {
		R34_OVER24_FD = r34_OVER24_FD;
	}
	public BigDecimal getR34_TOTAL() {
		return R34_TOTAL;
	}
	public void setR34_TOTAL(BigDecimal r34_TOTAL) {
		R34_TOTAL = r34_TOTAL;
	}
	public BigDecimal getR34_NOACC() {
		return R34_NOACC;
	}
	public void setR34_NOACC(BigDecimal r34_NOACC) {
		R34_NOACC = r34_NOACC;
	}
	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}
	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}
	public BigDecimal getR35_CURRENT() {
		return R35_CURRENT;
	}
	public void setR35_CURRENT(BigDecimal r35_CURRENT) {
		R35_CURRENT = r35_CURRENT;
	}
	public BigDecimal getR35_CALL() {
		return R35_CALL;
	}
	public void setR35_CALL(BigDecimal r35_CALL) {
		R35_CALL = r35_CALL;
	}
	public BigDecimal getR35_SAVINGS() {
		return R35_SAVINGS;
	}
	public void setR35_SAVINGS(BigDecimal r35_SAVINGS) {
		R35_SAVINGS = r35_SAVINGS;
	}
	public BigDecimal getR35_0_31D_NOTICE() {
		return R35_0_31D_NOTICE;
	}
	public void setR35_0_31D_NOTICE(BigDecimal r35_0_31d_NOTICE) {
		R35_0_31D_NOTICE = r35_0_31d_NOTICE;
	}
	public BigDecimal getR35_32_88D_NOTICE() {
		return R35_32_88D_NOTICE;
	}
	public void setR35_32_88D_NOTICE(BigDecimal r35_32_88d_NOTICE) {
		R35_32_88D_NOTICE = r35_32_88d_NOTICE;
	}
	public BigDecimal getR35_91D_DEPOSIT() {
		return R35_91D_DEPOSIT;
	}
	public void setR35_91D_DEPOSIT(BigDecimal r35_91d_DEPOSIT) {
		R35_91D_DEPOSIT = r35_91d_DEPOSIT;
	}
	public BigDecimal getR35_1_2M_FD() {
		return R35_1_2M_FD;
	}
	public void setR35_1_2M_FD(BigDecimal r35_1_2m_FD) {
		R35_1_2M_FD = r35_1_2m_FD;
	}
	public BigDecimal getR35_4_6M_FD() {
		return R35_4_6M_FD;
	}
	public void setR35_4_6M_FD(BigDecimal r35_4_6m_FD) {
		R35_4_6M_FD = r35_4_6m_FD;
	}
	public BigDecimal getR35_7_12M_FD() {
		return R35_7_12M_FD;
	}
	public void setR35_7_12M_FD(BigDecimal r35_7_12m_FD) {
		R35_7_12M_FD = r35_7_12m_FD;
	}
	public BigDecimal getR35_13_18M_FD() {
		return R35_13_18M_FD;
	}
	public void setR35_13_18M_FD(BigDecimal r35_13_18m_FD) {
		R35_13_18M_FD = r35_13_18m_FD;
	}
	public BigDecimal getR35_19_24M_FD() {
		return R35_19_24M_FD;
	}
	public void setR35_19_24M_FD(BigDecimal r35_19_24m_FD) {
		R35_19_24M_FD = r35_19_24m_FD;
	}
	public BigDecimal getR35_OVER24_FD() {
		return R35_OVER24_FD;
	}
	public void setR35_OVER24_FD(BigDecimal r35_OVER24_FD) {
		R35_OVER24_FD = r35_OVER24_FD;
	}
	public BigDecimal getR35_TOTAL() {
		return R35_TOTAL;
	}
	public void setR35_TOTAL(BigDecimal r35_TOTAL) {
		R35_TOTAL = r35_TOTAL;
	}
	public BigDecimal getR35_NOACC() {
		return R35_NOACC;
	}
	public void setR35_NOACC(BigDecimal r35_NOACC) {
		R35_NOACC = r35_NOACC;
	}
	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}
	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}
	public BigDecimal getR36_CURRENT() {
		return R36_CURRENT;
	}
	public void setR36_CURRENT(BigDecimal r36_CURRENT) {
		R36_CURRENT = r36_CURRENT;
	}
	public BigDecimal getR36_CALL() {
		return R36_CALL;
	}
	public void setR36_CALL(BigDecimal r36_CALL) {
		R36_CALL = r36_CALL;
	}
	public BigDecimal getR36_SAVINGS() {
		return R36_SAVINGS;
	}
	public void setR36_SAVINGS(BigDecimal r36_SAVINGS) {
		R36_SAVINGS = r36_SAVINGS;
	}
	public BigDecimal getR36_0_31D_NOTICE() {
		return R36_0_31D_NOTICE;
	}
	public void setR36_0_31D_NOTICE(BigDecimal r36_0_31d_NOTICE) {
		R36_0_31D_NOTICE = r36_0_31d_NOTICE;
	}
	public BigDecimal getR36_32_88D_NOTICE() {
		return R36_32_88D_NOTICE;
	}
	public void setR36_32_88D_NOTICE(BigDecimal r36_32_88d_NOTICE) {
		R36_32_88D_NOTICE = r36_32_88d_NOTICE;
	}
	public BigDecimal getR36_91D_DEPOSIT() {
		return R36_91D_DEPOSIT;
	}
	public void setR36_91D_DEPOSIT(BigDecimal r36_91d_DEPOSIT) {
		R36_91D_DEPOSIT = r36_91d_DEPOSIT;
	}
	public BigDecimal getR36_1_2M_FD() {
		return R36_1_2M_FD;
	}
	public void setR36_1_2M_FD(BigDecimal r36_1_2m_FD) {
		R36_1_2M_FD = r36_1_2m_FD;
	}
	public BigDecimal getR36_4_6M_FD() {
		return R36_4_6M_FD;
	}
	public void setR36_4_6M_FD(BigDecimal r36_4_6m_FD) {
		R36_4_6M_FD = r36_4_6m_FD;
	}
	public BigDecimal getR36_7_12M_FD() {
		return R36_7_12M_FD;
	}
	public void setR36_7_12M_FD(BigDecimal r36_7_12m_FD) {
		R36_7_12M_FD = r36_7_12m_FD;
	}
	public BigDecimal getR36_13_18M_FD() {
		return R36_13_18M_FD;
	}
	public void setR36_13_18M_FD(BigDecimal r36_13_18m_FD) {
		R36_13_18M_FD = r36_13_18m_FD;
	}
	public BigDecimal getR36_19_24M_FD() {
		return R36_19_24M_FD;
	}
	public void setR36_19_24M_FD(BigDecimal r36_19_24m_FD) {
		R36_19_24M_FD = r36_19_24m_FD;
	}
	public BigDecimal getR36_OVER24_FD() {
		return R36_OVER24_FD;
	}
	public void setR36_OVER24_FD(BigDecimal r36_OVER24_FD) {
		R36_OVER24_FD = r36_OVER24_FD;
	}
	public BigDecimal getR36_TOTAL() {
		return R36_TOTAL;
	}
	public void setR36_TOTAL(BigDecimal r36_TOTAL) {
		R36_TOTAL = r36_TOTAL;
	}
	public BigDecimal getR36_NOACC() {
		return R36_NOACC;
	}
	public void setR36_NOACC(BigDecimal r36_NOACC) {
		R36_NOACC = r36_NOACC;
	}
	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}
	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}
	public BigDecimal getR37_CURRENT() {
		return R37_CURRENT;
	}
	public void setR37_CURRENT(BigDecimal r37_CURRENT) {
		R37_CURRENT = r37_CURRENT;
	}
	public BigDecimal getR37_CALL() {
		return R37_CALL;
	}
	public void setR37_CALL(BigDecimal r37_CALL) {
		R37_CALL = r37_CALL;
	}
	public BigDecimal getR37_SAVINGS() {
		return R37_SAVINGS;
	}
	public void setR37_SAVINGS(BigDecimal r37_SAVINGS) {
		R37_SAVINGS = r37_SAVINGS;
	}
	public BigDecimal getR37_0_31D_NOTICE() {
		return R37_0_31D_NOTICE;
	}
	public void setR37_0_31D_NOTICE(BigDecimal r37_0_31d_NOTICE) {
		R37_0_31D_NOTICE = r37_0_31d_NOTICE;
	}
	public BigDecimal getR37_32_88D_NOTICE() {
		return R37_32_88D_NOTICE;
	}
	public void setR37_32_88D_NOTICE(BigDecimal r37_32_88d_NOTICE) {
		R37_32_88D_NOTICE = r37_32_88d_NOTICE;
	}
	public BigDecimal getR37_91D_DEPOSIT() {
		return R37_91D_DEPOSIT;
	}
	public void setR37_91D_DEPOSIT(BigDecimal r37_91d_DEPOSIT) {
		R37_91D_DEPOSIT = r37_91d_DEPOSIT;
	}
	public BigDecimal getR37_1_2M_FD() {
		return R37_1_2M_FD;
	}
	public void setR37_1_2M_FD(BigDecimal r37_1_2m_FD) {
		R37_1_2M_FD = r37_1_2m_FD;
	}
	public BigDecimal getR37_4_6M_FD() {
		return R37_4_6M_FD;
	}
	public void setR37_4_6M_FD(BigDecimal r37_4_6m_FD) {
		R37_4_6M_FD = r37_4_6m_FD;
	}
	public BigDecimal getR37_7_12M_FD() {
		return R37_7_12M_FD;
	}
	public void setR37_7_12M_FD(BigDecimal r37_7_12m_FD) {
		R37_7_12M_FD = r37_7_12m_FD;
	}
	public BigDecimal getR37_13_18M_FD() {
		return R37_13_18M_FD;
	}
	public void setR37_13_18M_FD(BigDecimal r37_13_18m_FD) {
		R37_13_18M_FD = r37_13_18m_FD;
	}
	public BigDecimal getR37_19_24M_FD() {
		return R37_19_24M_FD;
	}
	public void setR37_19_24M_FD(BigDecimal r37_19_24m_FD) {
		R37_19_24M_FD = r37_19_24m_FD;
	}
	public BigDecimal getR37_OVER24_FD() {
		return R37_OVER24_FD;
	}
	public void setR37_OVER24_FD(BigDecimal r37_OVER24_FD) {
		R37_OVER24_FD = r37_OVER24_FD;
	}
	public BigDecimal getR37_TOTAL() {
		return R37_TOTAL;
	}
	public void setR37_TOTAL(BigDecimal r37_TOTAL) {
		R37_TOTAL = r37_TOTAL;
	}
	public BigDecimal getR37_NOACC() {
		return R37_NOACC;
	}
	public void setR37_NOACC(BigDecimal r37_NOACC) {
		R37_NOACC = r37_NOACC;
	}
	public String getR38_PRODUCT() {
		return R38_PRODUCT;
	}
	public void setR38_PRODUCT(String r38_PRODUCT) {
		R38_PRODUCT = r38_PRODUCT;
	}
	public BigDecimal getR38_CURRENT() {
		return R38_CURRENT;
	}
	public void setR38_CURRENT(BigDecimal r38_CURRENT) {
		R38_CURRENT = r38_CURRENT;
	}
	public BigDecimal getR38_CALL() {
		return R38_CALL;
	}
	public void setR38_CALL(BigDecimal r38_CALL) {
		R38_CALL = r38_CALL;
	}
	public BigDecimal getR38_SAVINGS() {
		return R38_SAVINGS;
	}
	public void setR38_SAVINGS(BigDecimal r38_SAVINGS) {
		R38_SAVINGS = r38_SAVINGS;
	}
	public BigDecimal getR38_0_31D_NOTICE() {
		return R38_0_31D_NOTICE;
	}
	public void setR38_0_31D_NOTICE(BigDecimal r38_0_31d_NOTICE) {
		R38_0_31D_NOTICE = r38_0_31d_NOTICE;
	}
	public BigDecimal getR38_32_88D_NOTICE() {
		return R38_32_88D_NOTICE;
	}
	public void setR38_32_88D_NOTICE(BigDecimal r38_32_88d_NOTICE) {
		R38_32_88D_NOTICE = r38_32_88d_NOTICE;
	}
	public BigDecimal getR38_91D_DEPOSIT() {
		return R38_91D_DEPOSIT;
	}
	public void setR38_91D_DEPOSIT(BigDecimal r38_91d_DEPOSIT) {
		R38_91D_DEPOSIT = r38_91d_DEPOSIT;
	}
	public BigDecimal getR38_1_2M_FD() {
		return R38_1_2M_FD;
	}
	public void setR38_1_2M_FD(BigDecimal r38_1_2m_FD) {
		R38_1_2M_FD = r38_1_2m_FD;
	}
	public BigDecimal getR38_4_6M_FD() {
		return R38_4_6M_FD;
	}
	public void setR38_4_6M_FD(BigDecimal r38_4_6m_FD) {
		R38_4_6M_FD = r38_4_6m_FD;
	}
	public BigDecimal getR38_7_12M_FD() {
		return R38_7_12M_FD;
	}
	public void setR38_7_12M_FD(BigDecimal r38_7_12m_FD) {
		R38_7_12M_FD = r38_7_12m_FD;
	}
	public BigDecimal getR38_13_18M_FD() {
		return R38_13_18M_FD;
	}
	public void setR38_13_18M_FD(BigDecimal r38_13_18m_FD) {
		R38_13_18M_FD = r38_13_18m_FD;
	}
	public BigDecimal getR38_19_24M_FD() {
		return R38_19_24M_FD;
	}
	public void setR38_19_24M_FD(BigDecimal r38_19_24m_FD) {
		R38_19_24M_FD = r38_19_24m_FD;
	}
	public BigDecimal getR38_OVER24_FD() {
		return R38_OVER24_FD;
	}
	public void setR38_OVER24_FD(BigDecimal r38_OVER24_FD) {
		R38_OVER24_FD = r38_OVER24_FD;
	}
	public BigDecimal getR38_TOTAL() {
		return R38_TOTAL;
	}
	public void setR38_TOTAL(BigDecimal r38_TOTAL) {
		R38_TOTAL = r38_TOTAL;
	}
	public BigDecimal getR38_NOACC() {
		return R38_NOACC;
	}
	public void setR38_NOACC(BigDecimal r38_NOACC) {
		R38_NOACC = r38_NOACC;
	}
	public String getR39_PRODUCT() {
		return R39_PRODUCT;
	}
	public void setR39_PRODUCT(String r39_PRODUCT) {
		R39_PRODUCT = r39_PRODUCT;
	}
	public BigDecimal getR39_CURRENT() {
		return R39_CURRENT;
	}
	public void setR39_CURRENT(BigDecimal r39_CURRENT) {
		R39_CURRENT = r39_CURRENT;
	}
	public BigDecimal getR39_CALL() {
		return R39_CALL;
	}
	public void setR39_CALL(BigDecimal r39_CALL) {
		R39_CALL = r39_CALL;
	}
	public BigDecimal getR39_SAVINGS() {
		return R39_SAVINGS;
	}
	public void setR39_SAVINGS(BigDecimal r39_SAVINGS) {
		R39_SAVINGS = r39_SAVINGS;
	}
	public BigDecimal getR39_0_31D_NOTICE() {
		return R39_0_31D_NOTICE;
	}
	public void setR39_0_31D_NOTICE(BigDecimal r39_0_31d_NOTICE) {
		R39_0_31D_NOTICE = r39_0_31d_NOTICE;
	}
	public BigDecimal getR39_32_88D_NOTICE() {
		return R39_32_88D_NOTICE;
	}
	public void setR39_32_88D_NOTICE(BigDecimal r39_32_88d_NOTICE) {
		R39_32_88D_NOTICE = r39_32_88d_NOTICE;
	}
	public BigDecimal getR39_91D_DEPOSIT() {
		return R39_91D_DEPOSIT;
	}
	public void setR39_91D_DEPOSIT(BigDecimal r39_91d_DEPOSIT) {
		R39_91D_DEPOSIT = r39_91d_DEPOSIT;
	}
	public BigDecimal getR39_1_2M_FD() {
		return R39_1_2M_FD;
	}
	public void setR39_1_2M_FD(BigDecimal r39_1_2m_FD) {
		R39_1_2M_FD = r39_1_2m_FD;
	}
	public BigDecimal getR39_4_6M_FD() {
		return R39_4_6M_FD;
	}
	public void setR39_4_6M_FD(BigDecimal r39_4_6m_FD) {
		R39_4_6M_FD = r39_4_6m_FD;
	}
	public BigDecimal getR39_7_12M_FD() {
		return R39_7_12M_FD;
	}
	public void setR39_7_12M_FD(BigDecimal r39_7_12m_FD) {
		R39_7_12M_FD = r39_7_12m_FD;
	}
	public BigDecimal getR39_13_18M_FD() {
		return R39_13_18M_FD;
	}
	public void setR39_13_18M_FD(BigDecimal r39_13_18m_FD) {
		R39_13_18M_FD = r39_13_18m_FD;
	}
	public BigDecimal getR39_19_24M_FD() {
		return R39_19_24M_FD;
	}
	public void setR39_19_24M_FD(BigDecimal r39_19_24m_FD) {
		R39_19_24M_FD = r39_19_24m_FD;
	}
	public BigDecimal getR39_OVER24_FD() {
		return R39_OVER24_FD;
	}
	public void setR39_OVER24_FD(BigDecimal r39_OVER24_FD) {
		R39_OVER24_FD = r39_OVER24_FD;
	}
	public BigDecimal getR39_TOTAL() {
		return R39_TOTAL;
	}
	public void setR39_TOTAL(BigDecimal r39_TOTAL) {
		R39_TOTAL = r39_TOTAL;
	}
	public BigDecimal getR39_NOACC() {
		return R39_NOACC;
	}
	public void setR39_NOACC(BigDecimal r39_NOACC) {
		R39_NOACC = r39_NOACC;
	}
	public String getR40_PRODUCT() {
		return R40_PRODUCT;
	}
	public void setR40_PRODUCT(String r40_PRODUCT) {
		R40_PRODUCT = r40_PRODUCT;
	}
	public BigDecimal getR40_CURRENT() {
		return R40_CURRENT;
	}
	public void setR40_CURRENT(BigDecimal r40_CURRENT) {
		R40_CURRENT = r40_CURRENT;
	}
	public BigDecimal getR40_CALL() {
		return R40_CALL;
	}
	public void setR40_CALL(BigDecimal r40_CALL) {
		R40_CALL = r40_CALL;
	}
	public BigDecimal getR40_SAVINGS() {
		return R40_SAVINGS;
	}
	public void setR40_SAVINGS(BigDecimal r40_SAVINGS) {
		R40_SAVINGS = r40_SAVINGS;
	}
	public BigDecimal getR40_0_31D_NOTICE() {
		return R40_0_31D_NOTICE;
	}
	public void setR40_0_31D_NOTICE(BigDecimal r40_0_31d_NOTICE) {
		R40_0_31D_NOTICE = r40_0_31d_NOTICE;
	}
	public BigDecimal getR40_32_88D_NOTICE() {
		return R40_32_88D_NOTICE;
	}
	public void setR40_32_88D_NOTICE(BigDecimal r40_32_88d_NOTICE) {
		R40_32_88D_NOTICE = r40_32_88d_NOTICE;
	}
	public BigDecimal getR40_91D_DEPOSIT() {
		return R40_91D_DEPOSIT;
	}
	public void setR40_91D_DEPOSIT(BigDecimal r40_91d_DEPOSIT) {
		R40_91D_DEPOSIT = r40_91d_DEPOSIT;
	}
	public BigDecimal getR40_1_2M_FD() {
		return R40_1_2M_FD;
	}
	public void setR40_1_2M_FD(BigDecimal r40_1_2m_FD) {
		R40_1_2M_FD = r40_1_2m_FD;
	}
	public BigDecimal getR40_4_6M_FD() {
		return R40_4_6M_FD;
	}
	public void setR40_4_6M_FD(BigDecimal r40_4_6m_FD) {
		R40_4_6M_FD = r40_4_6m_FD;
	}
	public BigDecimal getR40_7_12M_FD() {
		return R40_7_12M_FD;
	}
	public void setR40_7_12M_FD(BigDecimal r40_7_12m_FD) {
		R40_7_12M_FD = r40_7_12m_FD;
	}
	public BigDecimal getR40_13_18M_FD() {
		return R40_13_18M_FD;
	}
	public void setR40_13_18M_FD(BigDecimal r40_13_18m_FD) {
		R40_13_18M_FD = r40_13_18m_FD;
	}
	public BigDecimal getR40_19_24M_FD() {
		return R40_19_24M_FD;
	}
	public void setR40_19_24M_FD(BigDecimal r40_19_24m_FD) {
		R40_19_24M_FD = r40_19_24m_FD;
	}
	public BigDecimal getR40_OVER24_FD() {
		return R40_OVER24_FD;
	}
	public void setR40_OVER24_FD(BigDecimal r40_OVER24_FD) {
		R40_OVER24_FD = r40_OVER24_FD;
	}
	public BigDecimal getR40_TOTAL() {
		return R40_TOTAL;
	}
	public void setR40_TOTAL(BigDecimal r40_TOTAL) {
		R40_TOTAL = r40_TOTAL;
	}
	public BigDecimal getR40_NOACC() {
		return R40_NOACC;
	}
	public void setR40_NOACC(BigDecimal r40_NOACC) {
		R40_NOACC = r40_NOACC;
	}
	public String getR41_PRODUCT() {
		return R41_PRODUCT;
	}
	public void setR41_PRODUCT(String r41_PRODUCT) {
		R41_PRODUCT = r41_PRODUCT;
	}
	public BigDecimal getR41_CURRENT() {
		return R41_CURRENT;
	}
	public void setR41_CURRENT(BigDecimal r41_CURRENT) {
		R41_CURRENT = r41_CURRENT;
	}
	public BigDecimal getR41_CALL() {
		return R41_CALL;
	}
	public void setR41_CALL(BigDecimal r41_CALL) {
		R41_CALL = r41_CALL;
	}
	public BigDecimal getR41_SAVINGS() {
		return R41_SAVINGS;
	}
	public void setR41_SAVINGS(BigDecimal r41_SAVINGS) {
		R41_SAVINGS = r41_SAVINGS;
	}
	public BigDecimal getR41_0_31D_NOTICE() {
		return R41_0_31D_NOTICE;
	}
	public void setR41_0_31D_NOTICE(BigDecimal r41_0_31d_NOTICE) {
		R41_0_31D_NOTICE = r41_0_31d_NOTICE;
	}
	public BigDecimal getR41_32_88D_NOTICE() {
		return R41_32_88D_NOTICE;
	}
	public void setR41_32_88D_NOTICE(BigDecimal r41_32_88d_NOTICE) {
		R41_32_88D_NOTICE = r41_32_88d_NOTICE;
	}
	public BigDecimal getR41_91D_DEPOSIT() {
		return R41_91D_DEPOSIT;
	}
	public void setR41_91D_DEPOSIT(BigDecimal r41_91d_DEPOSIT) {
		R41_91D_DEPOSIT = r41_91d_DEPOSIT;
	}
	public BigDecimal getR41_1_2M_FD() {
		return R41_1_2M_FD;
	}
	public void setR41_1_2M_FD(BigDecimal r41_1_2m_FD) {
		R41_1_2M_FD = r41_1_2m_FD;
	}
	public BigDecimal getR41_4_6M_FD() {
		return R41_4_6M_FD;
	}
	public void setR41_4_6M_FD(BigDecimal r41_4_6m_FD) {
		R41_4_6M_FD = r41_4_6m_FD;
	}
	public BigDecimal getR41_7_12M_FD() {
		return R41_7_12M_FD;
	}
	public void setR41_7_12M_FD(BigDecimal r41_7_12m_FD) {
		R41_7_12M_FD = r41_7_12m_FD;
	}
	public BigDecimal getR41_13_18M_FD() {
		return R41_13_18M_FD;
	}
	public void setR41_13_18M_FD(BigDecimal r41_13_18m_FD) {
		R41_13_18M_FD = r41_13_18m_FD;
	}
	public BigDecimal getR41_19_24M_FD() {
		return R41_19_24M_FD;
	}
	public void setR41_19_24M_FD(BigDecimal r41_19_24m_FD) {
		R41_19_24M_FD = r41_19_24m_FD;
	}
	public BigDecimal getR41_OVER24_FD() {
		return R41_OVER24_FD;
	}
	public void setR41_OVER24_FD(BigDecimal r41_OVER24_FD) {
		R41_OVER24_FD = r41_OVER24_FD;
	}
	public BigDecimal getR41_TOTAL() {
		return R41_TOTAL;
	}
	public void setR41_TOTAL(BigDecimal r41_TOTAL) {
		R41_TOTAL = r41_TOTAL;
	}
	public BigDecimal getR41_NOACC() {
		return R41_NOACC;
	}
	public void setR41_NOACC(BigDecimal r41_NOACC) {
		R41_NOACC = r41_NOACC;
	}
	public String getR42_PRODUCT() {
		return R42_PRODUCT;
	}
	public void setR42_PRODUCT(String r42_PRODUCT) {
		R42_PRODUCT = r42_PRODUCT;
	}
	public BigDecimal getR42_CURRENT() {
		return R42_CURRENT;
	}
	public void setR42_CURRENT(BigDecimal r42_CURRENT) {
		R42_CURRENT = r42_CURRENT;
	}
	public BigDecimal getR42_CALL() {
		return R42_CALL;
	}
	public void setR42_CALL(BigDecimal r42_CALL) {
		R42_CALL = r42_CALL;
	}
	public BigDecimal getR42_SAVINGS() {
		return R42_SAVINGS;
	}
	public void setR42_SAVINGS(BigDecimal r42_SAVINGS) {
		R42_SAVINGS = r42_SAVINGS;
	}
	public BigDecimal getR42_0_31D_NOTICE() {
		return R42_0_31D_NOTICE;
	}
	public void setR42_0_31D_NOTICE(BigDecimal r42_0_31d_NOTICE) {
		R42_0_31D_NOTICE = r42_0_31d_NOTICE;
	}
	public BigDecimal getR42_32_88D_NOTICE() {
		return R42_32_88D_NOTICE;
	}
	public void setR42_32_88D_NOTICE(BigDecimal r42_32_88d_NOTICE) {
		R42_32_88D_NOTICE = r42_32_88d_NOTICE;
	}
	public BigDecimal getR42_91D_DEPOSIT() {
		return R42_91D_DEPOSIT;
	}
	public void setR42_91D_DEPOSIT(BigDecimal r42_91d_DEPOSIT) {
		R42_91D_DEPOSIT = r42_91d_DEPOSIT;
	}
	public BigDecimal getR42_1_2M_FD() {
		return R42_1_2M_FD;
	}
	public void setR42_1_2M_FD(BigDecimal r42_1_2m_FD) {
		R42_1_2M_FD = r42_1_2m_FD;
	}
	public BigDecimal getR42_4_6M_FD() {
		return R42_4_6M_FD;
	}
	public void setR42_4_6M_FD(BigDecimal r42_4_6m_FD) {
		R42_4_6M_FD = r42_4_6m_FD;
	}
	public BigDecimal getR42_7_12M_FD() {
		return R42_7_12M_FD;
	}
	public void setR42_7_12M_FD(BigDecimal r42_7_12m_FD) {
		R42_7_12M_FD = r42_7_12m_FD;
	}
	public BigDecimal getR42_13_18M_FD() {
		return R42_13_18M_FD;
	}
	public void setR42_13_18M_FD(BigDecimal r42_13_18m_FD) {
		R42_13_18M_FD = r42_13_18m_FD;
	}
	public BigDecimal getR42_19_24M_FD() {
		return R42_19_24M_FD;
	}
	public void setR42_19_24M_FD(BigDecimal r42_19_24m_FD) {
		R42_19_24M_FD = r42_19_24m_FD;
	}
	public BigDecimal getR42_OVER24_FD() {
		return R42_OVER24_FD;
	}
	public void setR42_OVER24_FD(BigDecimal r42_OVER24_FD) {
		R42_OVER24_FD = r42_OVER24_FD;
	}
	public BigDecimal getR42_TOTAL() {
		return R42_TOTAL;
	}
	public void setR42_TOTAL(BigDecimal r42_TOTAL) {
		R42_TOTAL = r42_TOTAL;
	}
	public BigDecimal getR42_NOACC() {
		return R42_NOACC;
	}
	public void setR42_NOACC(BigDecimal r42_NOACC) {
		R42_NOACC = r42_NOACC;
	}
	public String getR43_PRODUCT() {
		return R43_PRODUCT;
	}
	public void setR43_PRODUCT(String r43_PRODUCT) {
		R43_PRODUCT = r43_PRODUCT;
	}
	public BigDecimal getR43_CURRENT() {
		return R43_CURRENT;
	}
	public void setR43_CURRENT(BigDecimal r43_CURRENT) {
		R43_CURRENT = r43_CURRENT;
	}
	public BigDecimal getR43_CALL() {
		return R43_CALL;
	}
	public void setR43_CALL(BigDecimal r43_CALL) {
		R43_CALL = r43_CALL;
	}
	public BigDecimal getR43_SAVINGS() {
		return R43_SAVINGS;
	}
	public void setR43_SAVINGS(BigDecimal r43_SAVINGS) {
		R43_SAVINGS = r43_SAVINGS;
	}
	public BigDecimal getR43_0_31D_NOTICE() {
		return R43_0_31D_NOTICE;
	}
	public void setR43_0_31D_NOTICE(BigDecimal r43_0_31d_NOTICE) {
		R43_0_31D_NOTICE = r43_0_31d_NOTICE;
	}
	public BigDecimal getR43_32_88D_NOTICE() {
		return R43_32_88D_NOTICE;
	}
	public void setR43_32_88D_NOTICE(BigDecimal r43_32_88d_NOTICE) {
		R43_32_88D_NOTICE = r43_32_88d_NOTICE;
	}
	public BigDecimal getR43_91D_DEPOSIT() {
		return R43_91D_DEPOSIT;
	}
	public void setR43_91D_DEPOSIT(BigDecimal r43_91d_DEPOSIT) {
		R43_91D_DEPOSIT = r43_91d_DEPOSIT;
	}
	public BigDecimal getR43_1_2M_FD() {
		return R43_1_2M_FD;
	}
	public void setR43_1_2M_FD(BigDecimal r43_1_2m_FD) {
		R43_1_2M_FD = r43_1_2m_FD;
	}
	public BigDecimal getR43_4_6M_FD() {
		return R43_4_6M_FD;
	}
	public void setR43_4_6M_FD(BigDecimal r43_4_6m_FD) {
		R43_4_6M_FD = r43_4_6m_FD;
	}
	public BigDecimal getR43_7_12M_FD() {
		return R43_7_12M_FD;
	}
	public void setR43_7_12M_FD(BigDecimal r43_7_12m_FD) {
		R43_7_12M_FD = r43_7_12m_FD;
	}
	public BigDecimal getR43_13_18M_FD() {
		return R43_13_18M_FD;
	}
	public void setR43_13_18M_FD(BigDecimal r43_13_18m_FD) {
		R43_13_18M_FD = r43_13_18m_FD;
	}
	public BigDecimal getR43_19_24M_FD() {
		return R43_19_24M_FD;
	}
	public void setR43_19_24M_FD(BigDecimal r43_19_24m_FD) {
		R43_19_24M_FD = r43_19_24m_FD;
	}
	public BigDecimal getR43_OVER24_FD() {
		return R43_OVER24_FD;
	}
	public void setR43_OVER24_FD(BigDecimal r43_OVER24_FD) {
		R43_OVER24_FD = r43_OVER24_FD;
	}
	public BigDecimal getR43_TOTAL() {
		return R43_TOTAL;
	}
	public void setR43_TOTAL(BigDecimal r43_TOTAL) {
		R43_TOTAL = r43_TOTAL;
	}
	public BigDecimal getR43_NOACC() {
		return R43_NOACC;
	}
	public void setR43_NOACC(BigDecimal r43_NOACC) {
		R43_NOACC = r43_NOACC;
	}
	public String getR44_PRODUCT() {
		return R44_PRODUCT;
	}
	public void setR44_PRODUCT(String r44_PRODUCT) {
		R44_PRODUCT = r44_PRODUCT;
	}
	public BigDecimal getR44_CURRENT() {
		return R44_CURRENT;
	}
	public void setR44_CURRENT(BigDecimal r44_CURRENT) {
		R44_CURRENT = r44_CURRENT;
	}
	public BigDecimal getR44_CALL() {
		return R44_CALL;
	}
	public void setR44_CALL(BigDecimal r44_CALL) {
		R44_CALL = r44_CALL;
	}
	public BigDecimal getR44_SAVINGS() {
		return R44_SAVINGS;
	}
	public void setR44_SAVINGS(BigDecimal r44_SAVINGS) {
		R44_SAVINGS = r44_SAVINGS;
	}
	public BigDecimal getR44_0_31D_NOTICE() {
		return R44_0_31D_NOTICE;
	}
	public void setR44_0_31D_NOTICE(BigDecimal r44_0_31d_NOTICE) {
		R44_0_31D_NOTICE = r44_0_31d_NOTICE;
	}
	public BigDecimal getR44_32_88D_NOTICE() {
		return R44_32_88D_NOTICE;
	}
	public void setR44_32_88D_NOTICE(BigDecimal r44_32_88d_NOTICE) {
		R44_32_88D_NOTICE = r44_32_88d_NOTICE;
	}
	public BigDecimal getR44_91D_DEPOSIT() {
		return R44_91D_DEPOSIT;
	}
	public void setR44_91D_DEPOSIT(BigDecimal r44_91d_DEPOSIT) {
		R44_91D_DEPOSIT = r44_91d_DEPOSIT;
	}
	public BigDecimal getR44_1_2M_FD() {
		return R44_1_2M_FD;
	}
	public void setR44_1_2M_FD(BigDecimal r44_1_2m_FD) {
		R44_1_2M_FD = r44_1_2m_FD;
	}
	public BigDecimal getR44_4_6M_FD() {
		return R44_4_6M_FD;
	}
	public void setR44_4_6M_FD(BigDecimal r44_4_6m_FD) {
		R44_4_6M_FD = r44_4_6m_FD;
	}
	public BigDecimal getR44_7_12M_FD() {
		return R44_7_12M_FD;
	}
	public void setR44_7_12M_FD(BigDecimal r44_7_12m_FD) {
		R44_7_12M_FD = r44_7_12m_FD;
	}
	public BigDecimal getR44_13_18M_FD() {
		return R44_13_18M_FD;
	}
	public void setR44_13_18M_FD(BigDecimal r44_13_18m_FD) {
		R44_13_18M_FD = r44_13_18m_FD;
	}
	public BigDecimal getR44_19_24M_FD() {
		return R44_19_24M_FD;
	}
	public void setR44_19_24M_FD(BigDecimal r44_19_24m_FD) {
		R44_19_24M_FD = r44_19_24m_FD;
	}
	public BigDecimal getR44_OVER24_FD() {
		return R44_OVER24_FD;
	}
	public void setR44_OVER24_FD(BigDecimal r44_OVER24_FD) {
		R44_OVER24_FD = r44_OVER24_FD;
	}
	public BigDecimal getR44_TOTAL() {
		return R44_TOTAL;
	}
	public void setR44_TOTAL(BigDecimal r44_TOTAL) {
		R44_TOTAL = r44_TOTAL;
	}
	public BigDecimal getR44_NOACC() {
		return R44_NOACC;
	}
	public void setR44_NOACC(BigDecimal r44_NOACC) {
		R44_NOACC = r44_NOACC;
	}
	public String getR45_PRODUCT() {
		return R45_PRODUCT;
	}
	public void setR45_PRODUCT(String r45_PRODUCT) {
		R45_PRODUCT = r45_PRODUCT;
	}
	public BigDecimal getR45_CURRENT() {
		return R45_CURRENT;
	}
	public void setR45_CURRENT(BigDecimal r45_CURRENT) {
		R45_CURRENT = r45_CURRENT;
	}
	public BigDecimal getR45_CALL() {
		return R45_CALL;
	}
	public void setR45_CALL(BigDecimal r45_CALL) {
		R45_CALL = r45_CALL;
	}
	public BigDecimal getR45_SAVINGS() {
		return R45_SAVINGS;
	}
	public void setR45_SAVINGS(BigDecimal r45_SAVINGS) {
		R45_SAVINGS = r45_SAVINGS;
	}
	public BigDecimal getR45_0_31D_NOTICE() {
		return R45_0_31D_NOTICE;
	}
	public void setR45_0_31D_NOTICE(BigDecimal r45_0_31d_NOTICE) {
		R45_0_31D_NOTICE = r45_0_31d_NOTICE;
	}
	public BigDecimal getR45_32_88D_NOTICE() {
		return R45_32_88D_NOTICE;
	}
	public void setR45_32_88D_NOTICE(BigDecimal r45_32_88d_NOTICE) {
		R45_32_88D_NOTICE = r45_32_88d_NOTICE;
	}
	public BigDecimal getR45_91D_DEPOSIT() {
		return R45_91D_DEPOSIT;
	}
	public void setR45_91D_DEPOSIT(BigDecimal r45_91d_DEPOSIT) {
		R45_91D_DEPOSIT = r45_91d_DEPOSIT;
	}
	public BigDecimal getR45_1_2M_FD() {
		return R45_1_2M_FD;
	}
	public void setR45_1_2M_FD(BigDecimal r45_1_2m_FD) {
		R45_1_2M_FD = r45_1_2m_FD;
	}
	public BigDecimal getR45_4_6M_FD() {
		return R45_4_6M_FD;
	}
	public void setR45_4_6M_FD(BigDecimal r45_4_6m_FD) {
		R45_4_6M_FD = r45_4_6m_FD;
	}
	public BigDecimal getR45_7_12M_FD() {
		return R45_7_12M_FD;
	}
	public void setR45_7_12M_FD(BigDecimal r45_7_12m_FD) {
		R45_7_12M_FD = r45_7_12m_FD;
	}
	public BigDecimal getR45_13_18M_FD() {
		return R45_13_18M_FD;
	}
	public void setR45_13_18M_FD(BigDecimal r45_13_18m_FD) {
		R45_13_18M_FD = r45_13_18m_FD;
	}
	public BigDecimal getR45_19_24M_FD() {
		return R45_19_24M_FD;
	}
	public void setR45_19_24M_FD(BigDecimal r45_19_24m_FD) {
		R45_19_24M_FD = r45_19_24m_FD;
	}
	public BigDecimal getR45_OVER24_FD() {
		return R45_OVER24_FD;
	}
	public void setR45_OVER24_FD(BigDecimal r45_OVER24_FD) {
		R45_OVER24_FD = r45_OVER24_FD;
	}
	public BigDecimal getR45_TOTAL() {
		return R45_TOTAL;
	}
	public void setR45_TOTAL(BigDecimal r45_TOTAL) {
		R45_TOTAL = r45_TOTAL;
	}
	public BigDecimal getR45_NOACC() {
		return R45_NOACC;
	}
	public void setR45_NOACC(BigDecimal r45_NOACC) {
		R45_NOACC = r45_NOACC;
	}
	public String getR46_PRODUCT() {
		return R46_PRODUCT;
	}
	public void setR46_PRODUCT(String r46_PRODUCT) {
		R46_PRODUCT = r46_PRODUCT;
	}
	public BigDecimal getR46_CURRENT() {
		return R46_CURRENT;
	}
	public void setR46_CURRENT(BigDecimal r46_CURRENT) {
		R46_CURRENT = r46_CURRENT;
	}
	public BigDecimal getR46_CALL() {
		return R46_CALL;
	}
	public void setR46_CALL(BigDecimal r46_CALL) {
		R46_CALL = r46_CALL;
	}
	public BigDecimal getR46_SAVINGS() {
		return R46_SAVINGS;
	}
	public void setR46_SAVINGS(BigDecimal r46_SAVINGS) {
		R46_SAVINGS = r46_SAVINGS;
	}
	public BigDecimal getR46_0_31D_NOTICE() {
		return R46_0_31D_NOTICE;
	}
	public void setR46_0_31D_NOTICE(BigDecimal r46_0_31d_NOTICE) {
		R46_0_31D_NOTICE = r46_0_31d_NOTICE;
	}
	public BigDecimal getR46_32_88D_NOTICE() {
		return R46_32_88D_NOTICE;
	}
	public void setR46_32_88D_NOTICE(BigDecimal r46_32_88d_NOTICE) {
		R46_32_88D_NOTICE = r46_32_88d_NOTICE;
	}
	public BigDecimal getR46_91D_DEPOSIT() {
		return R46_91D_DEPOSIT;
	}
	public void setR46_91D_DEPOSIT(BigDecimal r46_91d_DEPOSIT) {
		R46_91D_DEPOSIT = r46_91d_DEPOSIT;
	}
	public BigDecimal getR46_1_2M_FD() {
		return R46_1_2M_FD;
	}
	public void setR46_1_2M_FD(BigDecimal r46_1_2m_FD) {
		R46_1_2M_FD = r46_1_2m_FD;
	}
	public BigDecimal getR46_4_6M_FD() {
		return R46_4_6M_FD;
	}
	public void setR46_4_6M_FD(BigDecimal r46_4_6m_FD) {
		R46_4_6M_FD = r46_4_6m_FD;
	}
	public BigDecimal getR46_7_12M_FD() {
		return R46_7_12M_FD;
	}
	public void setR46_7_12M_FD(BigDecimal r46_7_12m_FD) {
		R46_7_12M_FD = r46_7_12m_FD;
	}
	public BigDecimal getR46_13_18M_FD() {
		return R46_13_18M_FD;
	}
	public void setR46_13_18M_FD(BigDecimal r46_13_18m_FD) {
		R46_13_18M_FD = r46_13_18m_FD;
	}
	public BigDecimal getR46_19_24M_FD() {
		return R46_19_24M_FD;
	}
	public void setR46_19_24M_FD(BigDecimal r46_19_24m_FD) {
		R46_19_24M_FD = r46_19_24m_FD;
	}
	public BigDecimal getR46_OVER24_FD() {
		return R46_OVER24_FD;
	}
	public void setR46_OVER24_FD(BigDecimal r46_OVER24_FD) {
		R46_OVER24_FD = r46_OVER24_FD;
	}
	public BigDecimal getR46_TOTAL() {
		return R46_TOTAL;
	}
	public void setR46_TOTAL(BigDecimal r46_TOTAL) {
		R46_TOTAL = r46_TOTAL;
	}
	public BigDecimal getR46_NOACC() {
		return R46_NOACC;
	}
	public void setR46_NOACC(BigDecimal r46_NOACC) {
		R46_NOACC = r46_NOACC;
	}
	public String getR47_PRODUCT() {
		return R47_PRODUCT;
	}
	public void setR47_PRODUCT(String r47_PRODUCT) {
		R47_PRODUCT = r47_PRODUCT;
	}
	public BigDecimal getR47_CURRENT() {
		return R47_CURRENT;
	}
	public void setR47_CURRENT(BigDecimal r47_CURRENT) {
		R47_CURRENT = r47_CURRENT;
	}
	public BigDecimal getR47_CALL() {
		return R47_CALL;
	}
	public void setR47_CALL(BigDecimal r47_CALL) {
		R47_CALL = r47_CALL;
	}
	public BigDecimal getR47_SAVINGS() {
		return R47_SAVINGS;
	}
	public void setR47_SAVINGS(BigDecimal r47_SAVINGS) {
		R47_SAVINGS = r47_SAVINGS;
	}
	public BigDecimal getR47_0_31D_NOTICE() {
		return R47_0_31D_NOTICE;
	}
	public void setR47_0_31D_NOTICE(BigDecimal r47_0_31d_NOTICE) {
		R47_0_31D_NOTICE = r47_0_31d_NOTICE;
	}
	public BigDecimal getR47_32_88D_NOTICE() {
		return R47_32_88D_NOTICE;
	}
	public void setR47_32_88D_NOTICE(BigDecimal r47_32_88d_NOTICE) {
		R47_32_88D_NOTICE = r47_32_88d_NOTICE;
	}
	public BigDecimal getR47_91D_DEPOSIT() {
		return R47_91D_DEPOSIT;
	}
	public void setR47_91D_DEPOSIT(BigDecimal r47_91d_DEPOSIT) {
		R47_91D_DEPOSIT = r47_91d_DEPOSIT;
	}
	public BigDecimal getR47_1_2M_FD() {
		return R47_1_2M_FD;
	}
	public void setR47_1_2M_FD(BigDecimal r47_1_2m_FD) {
		R47_1_2M_FD = r47_1_2m_FD;
	}
	public BigDecimal getR47_4_6M_FD() {
		return R47_4_6M_FD;
	}
	public void setR47_4_6M_FD(BigDecimal r47_4_6m_FD) {
		R47_4_6M_FD = r47_4_6m_FD;
	}
	public BigDecimal getR47_7_12M_FD() {
		return R47_7_12M_FD;
	}
	public void setR47_7_12M_FD(BigDecimal r47_7_12m_FD) {
		R47_7_12M_FD = r47_7_12m_FD;
	}
	public BigDecimal getR47_13_18M_FD() {
		return R47_13_18M_FD;
	}
	public void setR47_13_18M_FD(BigDecimal r47_13_18m_FD) {
		R47_13_18M_FD = r47_13_18m_FD;
	}
	public BigDecimal getR47_19_24M_FD() {
		return R47_19_24M_FD;
	}
	public void setR47_19_24M_FD(BigDecimal r47_19_24m_FD) {
		R47_19_24M_FD = r47_19_24m_FD;
	}
	public BigDecimal getR47_OVER24_FD() {
		return R47_OVER24_FD;
	}
	public void setR47_OVER24_FD(BigDecimal r47_OVER24_FD) {
		R47_OVER24_FD = r47_OVER24_FD;
	}
	public BigDecimal getR47_TOTAL() {
		return R47_TOTAL;
	}
	public void setR47_TOTAL(BigDecimal r47_TOTAL) {
		R47_TOTAL = r47_TOTAL;
	}
	public BigDecimal getR47_NOACC() {
		return R47_NOACC;
	}
	public void setR47_NOACC(BigDecimal r47_NOACC) {
		R47_NOACC = r47_NOACC;
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
	public Date getReportResubDate() {
		return reportResubDate;
	}
	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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
	public Q_SMME_DEP_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}


   


}
	