package com.bornfire.brrs.entities;

import java.lang.reflect.Method;
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
@Table(name = "BRRS_M_TOP_100_BORROWER_MANUAL_SUMMARYTABLE1")
public class M_TOP_100_BORROWER_Manual_Summary_Entity1 {
	
	private String R03_GROUP_CODE;
	private String R03_GROUP_NAME;
	private BigDecimal R03_CRM;
	private BigDecimal R03_NFBLT;
	private BigDecimal R03_NFBOS;
	private BigDecimal R03_CRM_2;
	private BigDecimal R03_NFB;
	private String R03_BOND;
	private String R03_CP;
	private String R03_EQULITY;
	private String R03_FOREX;
	private String R03_OTHERS;
	private String R03_INT_BANK;
	private String R03_DERIVATIVE;

	
	private String R04_GROUP_CODE;
	private String R04_GROUP_NAME;
	private BigDecimal R04_CRM;
	private BigDecimal R04_NFBLT;
	private BigDecimal R04_NFBOS;
	private BigDecimal R04_CRM_2;
	private BigDecimal R04_NFB;
	private String R04_BOND;
	private String R04_CP;
	private String R04_EQULITY;
	private String R04_FOREX;
	private String R04_OTHERS;
	private String R04_INT_BANK;
	private String R04_DERIVATIVE;

	private String R05_GROUP_CODE;
	private String R05_GROUP_NAME;
	private BigDecimal R05_CRM;
	private BigDecimal R05_NFBLT;
	private BigDecimal R05_NFBOS;
	private BigDecimal R05_CRM_2;
	private BigDecimal R05_NFB;
	private String R05_BOND;
	private String R05_CP;
	private String R05_EQULITY;
	private String R05_FOREX;
	private String R05_OTHERS;
	private String R05_INT_BANK;
	private String R05_DERIVATIVE;

	private String R06_GROUP_CODE;
	private String R06_GROUP_NAME;
	private BigDecimal R06_CRM;
	private BigDecimal R06_NFBLT;
	private BigDecimal R06_NFBOS;
	private BigDecimal R06_CRM_2;
	private BigDecimal R06_NFB;
	private String R06_BOND;
	private String R06_CP;
	private String R06_EQULITY;
	private String R06_FOREX;
	private String R06_OTHERS;
	private String R06_INT_BANK;
	private String R06_DERIVATIVE;

	private String R07_GROUP_CODE;
	private String R07_GROUP_NAME;
	private BigDecimal R07_CRM;
	private BigDecimal R07_NFBLT;
	private BigDecimal R07_NFBOS;
	private BigDecimal R07_CRM_2;
	private BigDecimal R07_NFB;
	private String R07_BOND;
	private String R07_CP;
	private String R07_EQULITY;
	private String R07_FOREX;
	private String R07_OTHERS;
	private String R07_INT_BANK;
	private String R07_DERIVATIVE;

	private String R08_GROUP_CODE;
	private String R08_GROUP_NAME;
	private BigDecimal R08_CRM;
	private BigDecimal R08_NFBLT;
	private BigDecimal R08_NFBOS;
	private BigDecimal R08_CRM_2;
	private BigDecimal R08_NFB;
	private String R08_BOND;
	private String R08_CP;
	private String R08_EQULITY;
	private String R08_FOREX;
	private String R08_OTHERS;
	private String R08_INT_BANK;
	private String R08_DERIVATIVE;

	private String R09_GROUP_CODE;
	private String R09_GROUP_NAME;
	private BigDecimal R09_CRM;
	private BigDecimal R09_NFBLT;
	private BigDecimal R09_NFBOS;
	private BigDecimal R09_CRM_2;
	private BigDecimal R09_NFB;
	private String R09_BOND;
	private String R09_CP;
	private String R09_EQULITY;
	private String R09_FOREX;
	private String R09_OTHERS;
	private String R09_INT_BANK;
	private String R09_DERIVATIVE;

	private String R10_GROUP_CODE;
	private String R10_GROUP_NAME;
	private BigDecimal R10_CRM;
	private BigDecimal R10_NFBLT;
	private BigDecimal R10_NFBOS;
	private BigDecimal R10_CRM_2;
	private BigDecimal R10_NFB;
	private String R10_BOND;
	private String R10_CP;
	private String R10_EQULITY;
	private String R10_FOREX;
	private String R10_OTHERS;
	private String R10_INT_BANK;
	private String R10_DERIVATIVE;

	private String R11_GROUP_CODE;
	private String R11_GROUP_NAME;
	private BigDecimal R11_CRM;
	private BigDecimal R11_NFBLT;
	private BigDecimal R11_NFBOS;
	private BigDecimal R11_CRM_2;
	private BigDecimal R11_NFB;
	private String R11_BOND;
	private String R11_CP;
	private String R11_EQULITY;
	private String R11_FOREX;
	private String R11_OTHERS;
	private String R11_INT_BANK;
	private String R11_DERIVATIVE;

	private String R12_GROUP_CODE;
	private String R12_GROUP_NAME;
	private BigDecimal R12_CRM;
	private BigDecimal R12_NFBLT;
	private BigDecimal R12_NFBOS;
	private BigDecimal R12_CRM_2;
	private BigDecimal R12_NFB;
	private String R12_BOND;
	private String R12_CP;
	private String R12_EQULITY;
	private String R12_FOREX;
	private String R12_OTHERS;
	private String R12_INT_BANK;
	private String R12_DERIVATIVE;

	private String R13_GROUP_CODE;
	private String R13_GROUP_NAME;
	private BigDecimal R13_CRM;
	private BigDecimal R13_NFBLT;
	private BigDecimal R13_NFBOS;
	private BigDecimal R13_CRM_2;
	private BigDecimal R13_NFB;
	private String R13_BOND;
	private String R13_CP;
	private String R13_EQULITY;
	private String R13_FOREX;
	private String R13_OTHERS;
	private String R13_INT_BANK;
	private String R13_DERIVATIVE;

	private String R14_GROUP_CODE;
	private String R14_GROUP_NAME;
	private BigDecimal R14_CRM;
	private BigDecimal R14_NFBLT;
	private BigDecimal R14_NFBOS;
	private BigDecimal R14_CRM_2;
	private BigDecimal R14_NFB;
	private String R14_BOND;
	private String R14_CP;
	private String R14_EQULITY;
	private String R14_FOREX;
	private String R14_OTHERS;
	private String R14_INT_BANK;
	private String R14_DERIVATIVE;

	private String R15_GROUP_CODE;
	private String R15_GROUP_NAME;
	private BigDecimal R15_CRM;
	private BigDecimal R15_NFBLT;
	private BigDecimal R15_NFBOS;
	private BigDecimal R15_CRM_2;
	private BigDecimal R15_NFB;
	private String R15_BOND;
	private String R15_CP;
	private String R15_EQULITY;
	private String R15_FOREX;
	private String R15_OTHERS;
	private String R15_INT_BANK;
	private String R15_DERIVATIVE;

	private String R16_GROUP_CODE;
	private String R16_GROUP_NAME;
	private BigDecimal R16_CRM;
	private BigDecimal R16_NFBLT;
	private BigDecimal R16_NFBOS;
	private BigDecimal R16_CRM_2;
	private BigDecimal R16_NFB;
	private String R16_BOND;
	private String R16_CP;
	private String R16_EQULITY;
	private String R16_FOREX;
	private String R16_OTHERS;
	private String R16_INT_BANK;
	private String R16_DERIVATIVE;

	private String R17_GROUP_CODE;
	private String R17_GROUP_NAME;
	private BigDecimal R17_CRM;
	private BigDecimal R17_NFBLT;
	private BigDecimal R17_NFBOS;
	private BigDecimal R17_CRM_2;
	private BigDecimal R17_NFB;
	private String R17_BOND;
	private String R17_CP;
	private String R17_EQULITY;
	private String R17_FOREX;
	private String R17_OTHERS;
	private String R17_INT_BANK;
	private String R17_DERIVATIVE;

	private String R18_GROUP_CODE;
	private String R18_GROUP_NAME;
	private BigDecimal R18_CRM;
	private BigDecimal R18_NFBLT;
	private BigDecimal R18_NFBOS;
	private BigDecimal R18_CRM_2;
	private BigDecimal R18_NFB;
	private String R18_BOND;
	private String R18_CP;
	private String R18_EQULITY;
	private String R18_FOREX;
	private String R18_OTHERS;
	private String R18_INT_BANK;
	private String R18_DERIVATIVE;

	private String R19_GROUP_CODE;
	private String R19_GROUP_NAME;
	private BigDecimal R19_CRM;
	private BigDecimal R19_NFBLT;
	private BigDecimal R19_NFBOS;
	private BigDecimal R19_CRM_2;
	private BigDecimal R19_NFB;
	private String R19_BOND;
	private String R19_CP;
	private String R19_EQULITY;
	private String R19_FOREX;
	private String R19_OTHERS;
	private String R19_INT_BANK;
	private String R19_DERIVATIVE;

	private String R20_GROUP_CODE;
	private String R20_GROUP_NAME;
	private BigDecimal R20_CRM;
	private BigDecimal R20_NFBLT;
	private BigDecimal R20_NFBOS;
	private BigDecimal R20_CRM_2;
	private BigDecimal R20_NFB;
	private String R20_BOND;
	private String R20_CP;
	private String R20_EQULITY;
	private String R20_FOREX;
	private String R20_OTHERS;
	private String R20_INT_BANK;
	private String R20_DERIVATIVE;

	private String R21_GROUP_CODE;
	private String R21_GROUP_NAME;
	private BigDecimal R21_CRM;
	private BigDecimal R21_NFBLT;
	private BigDecimal R21_NFBOS;
	private BigDecimal R21_CRM_2;
	private BigDecimal R21_NFB;
	private String R21_BOND;
	private String R21_CP;
	private String R21_EQULITY;
	private String R21_FOREX;
	private String R21_OTHERS;
	private String R21_INT_BANK;
	private String R21_DERIVATIVE;

	private String R22_GROUP_CODE;
	private String R22_GROUP_NAME;
	private BigDecimal R22_CRM;
	private BigDecimal R22_NFBLT;
	private BigDecimal R22_NFBOS;
	private BigDecimal R22_CRM_2;
	private BigDecimal R22_NFB;
	private String R22_BOND;
	private String R22_CP;
	private String R22_EQULITY;
	private String R22_FOREX;
	private String R22_OTHERS;
	private String R22_INT_BANK;
	private String R22_DERIVATIVE;

	private String R23_GROUP_CODE;
	private String R23_GROUP_NAME;
	private BigDecimal R23_CRM;
	private BigDecimal R23_NFBLT;
	private BigDecimal R23_NFBOS;
	private BigDecimal R23_CRM_2;
	private BigDecimal R23_NFB;
	private String R23_BOND;
	private String R23_CP;
	private String R23_EQULITY;
	private String R23_FOREX;
	private String R23_OTHERS;
	private String R23_INT_BANK;
	private String R23_DERIVATIVE;

	private String R24_GROUP_CODE;
	private String R24_GROUP_NAME;
	private BigDecimal R24_CRM;
	private BigDecimal R24_NFBLT;
	private BigDecimal R24_NFBOS;
	private BigDecimal R24_CRM_2;
	private BigDecimal R24_NFB;
	private String R24_BOND;
	private String R24_CP;
	private String R24_EQULITY;
	private String R24_FOREX;
	private String R24_OTHERS;
	private String R24_INT_BANK;
	private String R24_DERIVATIVE;

	private String R25_GROUP_CODE;
	private String R25_GROUP_NAME;
	private BigDecimal R25_CRM;
	private BigDecimal R25_NFBLT;
	private BigDecimal R25_NFBOS;
	private BigDecimal R25_CRM_2;
	private BigDecimal R25_NFB;
	private String R25_BOND;
	private String R25_CP;
	private String R25_EQULITY;
	private String R25_FOREX;
	private String R25_OTHERS;
	private String R25_INT_BANK;
	private String R25_DERIVATIVE;

	private String R26_GROUP_CODE;
	private String R26_GROUP_NAME;
	private BigDecimal R26_CRM;
	private BigDecimal R26_NFBLT;
	private BigDecimal R26_NFBOS;
	private BigDecimal R26_CRM_2;
	private BigDecimal R26_NFB;
	private String R26_BOND;
	private String R26_CP;
	private String R26_EQULITY;
	private String R26_FOREX;
	private String R26_OTHERS;
	private String R26_INT_BANK;
	private String R26_DERIVATIVE;

	private String R27_GROUP_CODE;
	private String R27_GROUP_NAME;
	private BigDecimal R27_CRM;
	private BigDecimal R27_NFBLT;
	private BigDecimal R27_NFBOS;
	private BigDecimal R27_CRM_2;
	private BigDecimal R27_NFB;
	private String R27_BOND;
	private String R27_CP;
	private String R27_EQULITY;
	private String R27_FOREX;
	private String R27_OTHERS;
	private String R27_INT_BANK;
	private String R27_DERIVATIVE;

	private String R28_GROUP_CODE;
	private String R28_GROUP_NAME;
	private BigDecimal R28_CRM;
	private BigDecimal R28_NFBLT;
	private BigDecimal R28_NFBOS;
	private BigDecimal R28_CRM_2;
	private BigDecimal R28_NFB;
	private String R28_BOND;
	private String R28_CP;
	private String R28_EQULITY;
	private String R28_FOREX;
	private String R28_OTHERS;
	private String R28_INT_BANK;
	private String R28_DERIVATIVE;

	private String R29_GROUP_CODE;
	private String R29_GROUP_NAME;
	private BigDecimal R29_CRM;
	private BigDecimal R29_NFBLT;
	private BigDecimal R29_NFBOS;
	private BigDecimal R29_CRM_2;
	private BigDecimal R29_NFB;
	private String R29_BOND;
	private String R29_CP;
	private String R29_EQULITY;
	private String R29_FOREX;
	private String R29_OTHERS;
	private String R29_INT_BANK;
	private String R29_DERIVATIVE;

	private String R30_GROUP_CODE;
	private String R30_GROUP_NAME;
	private BigDecimal R30_CRM;
	private BigDecimal R30_NFBLT;
	private BigDecimal R30_NFBOS;
	private BigDecimal R30_CRM_2;
	private BigDecimal R30_NFB;
	private String R30_BOND;
	private String R30_CP;
	private String R30_EQULITY;
	private String R30_FOREX;
	private String R30_OTHERS;
	private String R30_INT_BANK;
	private String R30_DERIVATIVE;

	private String R31_GROUP_CODE;
	private String R31_GROUP_NAME;
	private BigDecimal R31_CRM;
	private BigDecimal R31_NFBLT;
	private BigDecimal R31_NFBOS;
	private BigDecimal R31_CRM_2;
	private BigDecimal R31_NFB;
	private String R31_BOND;
	private String R31_CP;
	private String R31_EQULITY;
	private String R31_FOREX;
	private String R31_OTHERS;
	private String R31_INT_BANK;
	private String R31_DERIVATIVE;

	private String R32_GROUP_CODE;
	private String R32_GROUP_NAME;
	private BigDecimal R32_CRM;
	private BigDecimal R32_NFBLT;
	private BigDecimal R32_NFBOS;
	private BigDecimal R32_CRM_2;
	private BigDecimal R32_NFB;
	private String R32_BOND;
	private String R32_CP;
	private String R32_EQULITY;
	private String R32_FOREX;
	private String R32_OTHERS;
	private String R32_INT_BANK;
	private String R32_DERIVATIVE;

	private String R33_GROUP_CODE;
	private String R33_GROUP_NAME;
	private BigDecimal R33_CRM;
	private BigDecimal R33_NFBLT;
	private BigDecimal R33_NFBOS;
	private BigDecimal R33_CRM_2;
	private BigDecimal R33_NFB;
	private String R33_BOND;
	private String R33_CP;
	private String R33_EQULITY;
	private String R33_FOREX;
	private String R33_OTHERS;
	private String R33_INT_BANK;
	private String R33_DERIVATIVE;

	private String R34_GROUP_CODE;
	private String R34_GROUP_NAME;
	private BigDecimal R34_CRM;
	private BigDecimal R34_NFBLT;
	private BigDecimal R34_NFBOS;
	private BigDecimal R34_CRM_2;
	private BigDecimal R34_NFB;
	private String R34_BOND;
	private String R34_CP;
	private String R34_EQULITY;
	private String R34_FOREX;
	private String R34_OTHERS;
	private String R34_INT_BANK;
	private String R34_DERIVATIVE;

	private String R35_GROUP_CODE;
	private String R35_GROUP_NAME;
	private BigDecimal R35_CRM;
	private BigDecimal R35_NFBLT;
	private BigDecimal R35_NFBOS;
	private BigDecimal R35_CRM_2;
	private BigDecimal R35_NFB;
	private String R35_BOND;
	private String R35_CP;
	private String R35_EQULITY;
	private String R35_FOREX;
	private String R35_OTHERS;
	private String R35_INT_BANK;
	private String R35_DERIVATIVE;

	private String R36_GROUP_CODE;
	private String R36_GROUP_NAME;
	private BigDecimal R36_CRM;
	private BigDecimal R36_NFBLT;
	private BigDecimal R36_NFBOS;
	private BigDecimal R36_CRM_2;
	private BigDecimal R36_NFB;
	private String R36_BOND;
	private String R36_CP;
	private String R36_EQULITY;
	private String R36_FOREX;
	private String R36_OTHERS;
	private String R36_INT_BANK;
	private String R36_DERIVATIVE;

	private String R37_GROUP_CODE;
	private String R37_GROUP_NAME;
	private BigDecimal R37_CRM;
	private BigDecimal R37_NFBLT;
	private BigDecimal R37_NFBOS;
	private BigDecimal R37_CRM_2;
	private BigDecimal R37_NFB;
	private String R37_BOND;
	private String R37_CP;
	private String R37_EQULITY;
	private String R37_FOREX;
	private String R37_OTHERS;
	private String R37_INT_BANK;
	private String R37_DERIVATIVE;

	private String R38_GROUP_CODE;
	private String R38_GROUP_NAME;
	private BigDecimal R38_CRM;
	private BigDecimal R38_NFBLT;
	private BigDecimal R38_NFBOS;
	private BigDecimal R38_CRM_2;
	private BigDecimal R38_NFB;
	private String R38_BOND;
	private String R38_CP;
	private String R38_EQULITY;
	private String R38_FOREX;
	private String R38_OTHERS;
	private String R38_INT_BANK;
	private String R38_DERIVATIVE;

	private String R39_GROUP_CODE;
	private String R39_GROUP_NAME;
	private BigDecimal R39_CRM;
	private BigDecimal R39_NFBLT;
	private BigDecimal R39_NFBOS;
	private BigDecimal R39_CRM_2;
	private BigDecimal R39_NFB;
	private String R39_BOND;
	private String R39_CP;
	private String R39_EQULITY;
	private String R39_FOREX;
	private String R39_OTHERS;
	private String R39_INT_BANK;
	private String R39_DERIVATIVE;

	private String R40_GROUP_CODE;
	private String R40_GROUP_NAME;
	private BigDecimal R40_CRM;
	private BigDecimal R40_NFBLT;
	private BigDecimal R40_NFBOS;
	private BigDecimal R40_CRM_2;
	private BigDecimal R40_NFB;
	private String R40_BOND;
	private String R40_CP;
	private String R40_EQULITY;
	private String R40_FOREX;
	private String R40_OTHERS;
	private String R40_INT_BANK;
	private String R40_DERIVATIVE;

	private String R41_GROUP_CODE;
	private String R41_GROUP_NAME;
	private BigDecimal R41_CRM;
	private BigDecimal R41_NFBLT;
	private BigDecimal R41_NFBOS;
	private BigDecimal R41_CRM_2;
	private BigDecimal R41_NFB;
	private String R41_BOND;
	private String R41_CP;
	private String R41_EQULITY;
	private String R41_FOREX;
	private String R41_OTHERS;
	private String R41_INT_BANK;
	private String R41_DERIVATIVE;

	private String R42_GROUP_CODE;
	private String R42_GROUP_NAME;
	private BigDecimal R42_CRM;
	private BigDecimal R42_NFBLT;
	private BigDecimal R42_NFBOS;
	private BigDecimal R42_CRM_2;
	private BigDecimal R42_NFB;
	private String R42_BOND;
	private String R42_CP;
	private String R42_EQULITY;
	private String R42_FOREX;
	private String R42_OTHERS;
	private String R42_INT_BANK;
	private String R42_DERIVATIVE;

	private String R43_GROUP_CODE;
	private String R43_GROUP_NAME;
	private BigDecimal R43_CRM;
	private BigDecimal R43_NFBLT;
	private BigDecimal R43_NFBOS;
	private BigDecimal R43_CRM_2;
	private BigDecimal R43_NFB;
	private String R43_BOND;
	private String R43_CP;
	private String R43_EQULITY;
	private String R43_FOREX;
	private String R43_OTHERS;
	private String R43_INT_BANK;
	private String R43_DERIVATIVE;

	private String R44_GROUP_CODE;
	private String R44_GROUP_NAME;
	private BigDecimal R44_CRM;
	private BigDecimal R44_NFBLT;
	private BigDecimal R44_NFBOS;
	private BigDecimal R44_CRM_2;
	private BigDecimal R44_NFB;
	private String R44_BOND;
	private String R44_CP;
	private String R44_EQULITY;
	private String R44_FOREX;
	private String R44_OTHERS;
	private String R44_INT_BANK;
	private String R44_DERIVATIVE;

	private String R45_GROUP_CODE;
	private String R45_GROUP_NAME;
	private BigDecimal R45_CRM;
	private BigDecimal R45_NFBLT;
	private BigDecimal R45_NFBOS;
	private BigDecimal R45_CRM_2;
	private BigDecimal R45_NFB;
	private String R45_BOND;
	private String R45_CP;
	private String R45_EQULITY;
	private String R45_FOREX;
	private String R45_OTHERS;
	private String R45_INT_BANK;
	private String R45_DERIVATIVE;

	private String R46_GROUP_CODE;
	private String R46_GROUP_NAME;
	private BigDecimal R46_CRM;
	private BigDecimal R46_NFBLT;
	private BigDecimal R46_NFBOS;
	private BigDecimal R46_CRM_2;
	private BigDecimal R46_NFB;
	private String R46_BOND;
	private String R46_CP;
	private String R46_EQULITY;
	private String R46_FOREX;
	private String R46_OTHERS;
	private String R46_INT_BANK;
	private String R46_DERIVATIVE;

	private String R47_GROUP_CODE;
	private String R47_GROUP_NAME;
	private BigDecimal R47_CRM;
	private BigDecimal R47_NFBLT;
	private BigDecimal R47_NFBOS;
	private BigDecimal R47_CRM_2;
	private BigDecimal R47_NFB;
	private String R47_BOND;
	private String R47_CP;
	private String R47_EQULITY;
	private String R47_FOREX;
	private String R47_OTHERS;
	private String R47_INT_BANK;
	private String R47_DERIVATIVE;

	private String R48_GROUP_CODE;
	private String R48_GROUP_NAME;
	private BigDecimal R48_CRM;
	private BigDecimal R48_NFBLT;
	private BigDecimal R48_NFBOS;
	private BigDecimal R48_CRM_2;
	private BigDecimal R48_NFB;
	private String R48_BOND;
	private String R48_CP;
	private String R48_EQULITY;
	private String R48_FOREX;
	private String R48_OTHERS;
	private String R48_INT_BANK;
	private String R48_DERIVATIVE;

	private String R49_GROUP_CODE;
	private String R49_GROUP_NAME;
	private BigDecimal R49_CRM;
	private BigDecimal R49_NFBLT;
	private BigDecimal R49_NFBOS;
	private BigDecimal R49_CRM_2;
	private BigDecimal R49_NFB;
	private String R49_BOND;
	private String R49_CP;
	private String R49_EQULITY;
	private String R49_FOREX;
	private String R49_OTHERS;
	private String R49_INT_BANK;
	private String R49_DERIVATIVE;

	private String R50_GROUP_CODE;
	private String R50_GROUP_NAME;
	private BigDecimal R50_CRM;
	private BigDecimal R50_NFBLT;
	private BigDecimal R50_NFBOS;
	private BigDecimal R50_CRM_2;
	private BigDecimal R50_NFB;
	private String R50_BOND;
	private String R50_CP;
	private String R50_EQULITY;
	private String R50_FOREX;
	private String R50_OTHERS;
	private String R50_INT_BANK;
	private String R50_DERIVATIVE;

	private String R51_GROUP_CODE;
	private String R51_GROUP_NAME;
	private BigDecimal R51_CRM;
	private BigDecimal R51_NFBLT;
	private BigDecimal R51_NFBOS;
	private BigDecimal R51_CRM_2;
	private BigDecimal R51_NFB;
	private String R51_BOND;
	private String R51_CP;
	private String R51_EQULITY;
	private String R51_FOREX;
	private String R51_OTHERS;
	private String R51_INT_BANK;
	private String R51_DERIVATIVE;

	private String R52_GROUP_CODE;
	private String R52_GROUP_NAME;
	private BigDecimal R52_CRM;
	private BigDecimal R52_NFBLT;
	private BigDecimal R52_NFBOS;
	private BigDecimal R52_CRM_2;
	private BigDecimal R52_NFB;
	private String R52_BOND;
	private String R52_CP;
	private String R52_EQULITY;
	private String R52_FOREX;
	private String R52_OTHERS;
	private String R52_INT_BANK;
	private String R52_DERIVATIVE;

	private String R53_GROUP_CODE;
	private String R53_GROUP_NAME;
	private BigDecimal R53_CRM;
	private BigDecimal R53_NFBLT;
	private BigDecimal R53_NFBOS;
	private BigDecimal R53_CRM_2;
	private BigDecimal R53_NFB;
	private String R53_BOND;
	private String R53_CP;
	private String R53_EQULITY;
	private String R53_FOREX;
	private String R53_OTHERS;
	private String R53_INT_BANK;
	private String R53_DERIVATIVE;

	private String R54_GROUP_CODE;
	private String R54_GROUP_NAME;
	private BigDecimal R54_CRM;
	private BigDecimal R54_NFBLT;
	private BigDecimal R54_NFBOS;
	private BigDecimal R54_CRM_2;
	private BigDecimal R54_NFB;
	private String R54_BOND;
	private String R54_CP;
	private String R54_EQULITY;
	private String R54_FOREX;
	private String R54_OTHERS;
	private String R54_INT_BANK;
	private String R54_DERIVATIVE;

	private String R55_GROUP_CODE;
	private String R55_GROUP_NAME;
	private BigDecimal R55_CRM;
	private BigDecimal R55_NFBLT;
	private BigDecimal R55_NFBOS;
	private BigDecimal R55_CRM_2;
	private BigDecimal R55_NFB;
	private String R55_BOND;
	private String R55_CP;
	private String R55_EQULITY;
	private String R55_FOREX;
	private String R55_OTHERS;
	private String R55_INT_BANK;
	private String R55_DERIVATIVE;

	private String R56_GROUP_CODE;
	private String R56_GROUP_NAME;
	private BigDecimal R56_CRM;
	private BigDecimal R56_NFBLT;
	private BigDecimal R56_NFBOS;
	private BigDecimal R56_CRM_2;
	private BigDecimal R56_NFB;
	private String R56_BOND;
	private String R56_CP;
	private String R56_EQULITY;
	private String R56_FOREX;
	private String R56_OTHERS;
	private String R56_INT_BANK;
	private String R56_DERIVATIVE;

	private String R57_GROUP_CODE;
	private String R57_GROUP_NAME;
	private BigDecimal R57_CRM;
	private BigDecimal R57_NFBLT;
	private BigDecimal R57_NFBOS;
	private BigDecimal R57_CRM_2;
	private BigDecimal R57_NFB;
	private String R57_BOND;
	private String R57_CP;
	private String R57_EQULITY;
	private String R57_FOREX;
	private String R57_OTHERS;
	private String R57_INT_BANK;
	private String R57_DERIVATIVE;

	private String R58_GROUP_CODE;
	private String R58_GROUP_NAME;
	private BigDecimal R58_CRM;
	private BigDecimal R58_NFBLT;
	private BigDecimal R58_NFBOS;
	private BigDecimal R58_CRM_2;
	private BigDecimal R58_NFB;
	private String R58_BOND;
	private String R58_CP;
	private String R58_EQULITY;
	private String R58_FOREX;
	private String R58_OTHERS;
	private String R58_INT_BANK;
	private String R58_DERIVATIVE;

	private String R59_GROUP_CODE;
	private String R59_GROUP_NAME;
	private BigDecimal R59_CRM;
	private BigDecimal R59_NFBLT;
	private BigDecimal R59_NFBOS;
	private BigDecimal R59_CRM_2;
	private BigDecimal R59_NFB;
	private String R59_BOND;
	private String R59_CP;
	private String R59_EQULITY;
	private String R59_FOREX;
	private String R59_OTHERS;
	private String R59_INT_BANK;
	private String R59_DERIVATIVE;

	private String R60_GROUP_CODE;
	private String R60_GROUP_NAME;
	private BigDecimal R60_CRM;
	private BigDecimal R60_NFBLT;
	private BigDecimal R60_NFBOS;
	private BigDecimal R60_CRM_2;
	private BigDecimal R60_NFB;
	private String R60_BOND;
	private String R60_CP;
	private String R60_EQULITY;
	private String R60_FOREX;
	private String R60_OTHERS;
	private String R60_INT_BANK;
	private String R60_DERIVATIVE;

	private String R61_GROUP_CODE;
	private String R61_GROUP_NAME;
	private BigDecimal R61_CRM;
	private BigDecimal R61_NFBLT;
	private BigDecimal R61_NFBOS;
	private BigDecimal R61_CRM_2;
	private BigDecimal R61_NFB;
	private String R61_BOND;
	private String R61_CP;
	private String R61_EQULITY;
	private String R61_FOREX;
	private String R61_OTHERS;
	private String R61_INT_BANK;
	private String R61_DERIVATIVE;

	private String R62_GROUP_CODE;
	private String R62_GROUP_NAME;
	private BigDecimal R62_CRM;
	private BigDecimal R62_NFBLT;
	private BigDecimal R62_NFBOS;
	private BigDecimal R62_CRM_2;
	private BigDecimal R62_NFB;
	private String R62_BOND;
	private String R62_CP;
	private String R62_EQULITY;
	private String R62_FOREX;
	private String R62_OTHERS;
	private String R62_INT_BANK;
	private String R62_DERIVATIVE;

	private String R63_GROUP_CODE;
	private String R63_GROUP_NAME;
	private BigDecimal R63_CRM;
	private BigDecimal R63_NFBLT;
	private BigDecimal R63_NFBOS;
	private BigDecimal R63_CRM_2;
	private BigDecimal R63_NFB;
	private String R63_BOND;
	private String R63_CP;
	private String R63_EQULITY;
	private String R63_FOREX;
	private String R63_OTHERS;
	private String R63_INT_BANK;
	private String R63_DERIVATIVE;

	private String R64_GROUP_CODE;
	private String R64_GROUP_NAME;
	private BigDecimal R64_CRM;
	private BigDecimal R64_NFBLT;
	private BigDecimal R64_NFBOS;
	private BigDecimal R64_CRM_2;
	private BigDecimal R64_NFB;
	private String R64_BOND;
	private String R64_CP;
	private String R64_EQULITY;
	private String R64_FOREX;
	private String R64_OTHERS;
	private String R64_INT_BANK;
	private String R64_DERIVATIVE;

	private String R65_GROUP_CODE;
	private String R65_GROUP_NAME;
	private BigDecimal R65_CRM;
	private BigDecimal R65_NFBLT;
	private BigDecimal R65_NFBOS;
	private BigDecimal R65_CRM_2;
	private BigDecimal R65_NFB;
	private String R65_BOND;
	private String R65_CP;
	private String R65_EQULITY;
	private String R65_FOREX;
	private String R65_OTHERS;
	private String R65_INT_BANK;
	private String R65_DERIVATIVE;

	private String R66_GROUP_CODE;
	private String R66_GROUP_NAME;
	private BigDecimal R66_CRM;
	private BigDecimal R66_NFBLT;
	private BigDecimal R66_NFBOS;
	private BigDecimal R66_CRM_2;
	private BigDecimal R66_NFB;
	private String R66_BOND;
	private String R66_CP;
	private String R66_EQULITY;
	private String R66_FOREX;
	private String R66_OTHERS;
	private String R66_INT_BANK;
	private String R66_DERIVATIVE;

	private String R67_GROUP_CODE;
	private String R67_GROUP_NAME;
	private BigDecimal R67_CRM;
	private BigDecimal R67_NFBLT;
	private BigDecimal R67_NFBOS;
	private BigDecimal R67_CRM_2;
	private BigDecimal R67_NFB;
	private String R67_BOND;
	private String R67_CP;
	private String R67_EQULITY;
	private String R67_FOREX;
	private String R67_OTHERS;
	private String R67_INT_BANK;
	private String R67_DERIVATIVE;

	private String R68_GROUP_CODE;
	private String R68_GROUP_NAME;
	private BigDecimal R68_CRM;
	private BigDecimal R68_NFBLT;
	private BigDecimal R68_NFBOS;
	private BigDecimal R68_CRM_2;
	private BigDecimal R68_NFB;
	private String R68_BOND;
	private String R68_CP;
	private String R68_EQULITY;
	private String R68_FOREX;
	private String R68_OTHERS;
	private String R68_INT_BANK;
	private String R68_DERIVATIVE;

	private String R69_GROUP_CODE;
	private String R69_GROUP_NAME;
	private BigDecimal R69_CRM;
	private BigDecimal R69_NFBLT;
	private BigDecimal R69_NFBOS;
	private BigDecimal R69_CRM_2;
	private BigDecimal R69_NFB;
	private String R69_BOND;
	private String R69_CP;
	private String R69_EQULITY;
	private String R69_FOREX;
	private String R69_OTHERS;
	private String R69_INT_BANK;
	private String R69_DERIVATIVE;

	private String R70_GROUP_CODE;
	private String R70_GROUP_NAME;
	private BigDecimal R70_CRM;
	private BigDecimal R70_NFBLT;
	private BigDecimal R70_NFBOS;
	private BigDecimal R70_CRM_2;
	private BigDecimal R70_NFB;
	private String R70_BOND;
	private String R70_CP;
	private String R70_EQULITY;
	private String R70_FOREX;
	private String R70_OTHERS;
	private String R70_INT_BANK;
	private String R70_DERIVATIVE;
	
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
    

	public String getR03_GROUP_CODE() {
		return R03_GROUP_CODE;
	}
	public void setR03_GROUP_CODE(String r03_GROUP_CODE) {
		R03_GROUP_CODE = r03_GROUP_CODE;
	}
	public String getR03_GROUP_NAME() {
		return R03_GROUP_NAME;
	}
	public void setR03_GROUP_NAME(String r03_GROUP_NAME) {
		R03_GROUP_NAME = r03_GROUP_NAME;
	}
	public BigDecimal getR03_CRM() {
		return R03_CRM;
	}
	public void setR03_CRM(BigDecimal r03_CRM) {
		R03_CRM = r03_CRM;
	}
	public BigDecimal getR03_NFBLT() {
		return R03_NFBLT;
	}
	public void setR03_NFBLT(BigDecimal r03_NFBLT) {
		R03_NFBLT = r03_NFBLT;
	}
	public BigDecimal getR03_NFBOS() {
		return R03_NFBOS;
	}
	public void setR03_NFBOS(BigDecimal r03_NFBOS) {
		R03_NFBOS = r03_NFBOS;
	}
	public BigDecimal getR03_CRM_2() {
		return R03_CRM_2;
	}
	public void setR03_CRM_2(BigDecimal r03_CRM_2) {
		R03_CRM_2 = r03_CRM_2;
	}
	public BigDecimal getR03_NFB() {
		return R03_NFB;
	}
	public void setR03_NFB(BigDecimal r03_NFB) {
		R03_NFB = r03_NFB;
	}
	public String getR03_BOND() {
		return R03_BOND;
	}
	public void setR03_BOND(String r03_BOND) {
		R03_BOND = r03_BOND;
	}
	public String getR03_CP() {
		return R03_CP;
	}
	public void setR03_CP(String r03_CP) {
		R03_CP = r03_CP;
	}
	public String getR03_EQULITY() {
		return R03_EQULITY;
	}
	public void setR03_EQULITY(String r03_EQULITY) {
		R03_EQULITY = r03_EQULITY;
	}
	public String getR03_FOREX() {
		return R03_FOREX;
	}
	public void setR03_FOREX(String r03_FOREX) {
		R03_FOREX = r03_FOREX;
	}
	public String getR03_OTHERS() {
		return R03_OTHERS;
	}
	public void setR03_OTHERS(String r03_OTHERS) {
		R03_OTHERS = r03_OTHERS;
	}
	public String getR03_INT_BANK() {
		return R03_INT_BANK;
	}
	public void setR03_INT_BANK(String r03_INT_BANK) {
		R03_INT_BANK = r03_INT_BANK;
	}
	public String getR03_DERIVATIVE() {
		return R03_DERIVATIVE;
	}
	public void setR03_DERIVATIVE(String r03_DERIVATIVE) {
		R03_DERIVATIVE = r03_DERIVATIVE;
	}
	public String getR04_GROUP_CODE() {
		return R04_GROUP_CODE;
	}
	public void setR04_GROUP_CODE(String r04_GROUP_CODE) {
		R04_GROUP_CODE = r04_GROUP_CODE;
	}
	public String getR04_GROUP_NAME() {
		return R04_GROUP_NAME;
	}
	public void setR04_GROUP_NAME(String r04_GROUP_NAME) {
		R04_GROUP_NAME = r04_GROUP_NAME;
	}
	public BigDecimal getR04_CRM() {
		return R04_CRM;
	}
	public void setR04_CRM(BigDecimal r04_CRM) {
		R04_CRM = r04_CRM;
	}
	public BigDecimal getR04_NFBLT() {
		return R04_NFBLT;
	}
	public void setR04_NFBLT(BigDecimal r04_NFBLT) {
		R04_NFBLT = r04_NFBLT;
	}
	public BigDecimal getR04_NFBOS() {
		return R04_NFBOS;
	}
	public void setR04_NFBOS(BigDecimal r04_NFBOS) {
		R04_NFBOS = r04_NFBOS;
	}
	public BigDecimal getR04_CRM_2() {
		return R04_CRM_2;
	}
	public void setR04_CRM_2(BigDecimal r04_CRM_2) {
		R04_CRM_2 = r04_CRM_2;
	}
	public BigDecimal getR04_NFB() {
		return R04_NFB;
	}
	public void setR04_NFB(BigDecimal r04_NFB) {
		R04_NFB = r04_NFB;
	}
	public String getR04_BOND() {
		return R04_BOND;
	}
	public void setR04_BOND(String r04_BOND) {
		R04_BOND = r04_BOND;
	}
	public String getR04_CP() {
		return R04_CP;
	}
	public void setR04_CP(String r04_CP) {
		R04_CP = r04_CP;
	}
	public String getR04_EQULITY() {
		return R04_EQULITY;
	}
	public void setR04_EQULITY(String r04_EQULITY) {
		R04_EQULITY = r04_EQULITY;
	}
	public String getR04_FOREX() {
		return R04_FOREX;
	}
	public void setR04_FOREX(String r04_FOREX) {
		R04_FOREX = r04_FOREX;
	}
	public String getR04_OTHERS() {
		return R04_OTHERS;
	}
	public void setR04_OTHERS(String r04_OTHERS) {
		R04_OTHERS = r04_OTHERS;
	}
	public String getR04_INT_BANK() {
		return R04_INT_BANK;
	}
	public void setR04_INT_BANK(String r04_INT_BANK) {
		R04_INT_BANK = r04_INT_BANK;
	}
	public String getR04_DERIVATIVE() {
		return R04_DERIVATIVE;
	}
	public void setR04_DERIVATIVE(String r04_DERIVATIVE) {
		R04_DERIVATIVE = r04_DERIVATIVE;
	}
	public String getR05_GROUP_CODE() {
		return R05_GROUP_CODE;
	}
	public void setR05_GROUP_CODE(String r05_GROUP_CODE) {
		R05_GROUP_CODE = r05_GROUP_CODE;
	}
	public String getR05_GROUP_NAME() {
		return R05_GROUP_NAME;
	}
	public void setR05_GROUP_NAME(String r05_GROUP_NAME) {
		R05_GROUP_NAME = r05_GROUP_NAME;
	}
	public BigDecimal getR05_CRM() {
		return R05_CRM;
	}
	public void setR05_CRM(BigDecimal r05_CRM) {
		R05_CRM = r05_CRM;
	}
	public BigDecimal getR05_NFBLT() {
		return R05_NFBLT;
	}
	public void setR05_NFBLT(BigDecimal r05_NFBLT) {
		R05_NFBLT = r05_NFBLT;
	}
	public BigDecimal getR05_NFBOS() {
		return R05_NFBOS;
	}
	public void setR05_NFBOS(BigDecimal r05_NFBOS) {
		R05_NFBOS = r05_NFBOS;
	}
	public BigDecimal getR05_CRM_2() {
		return R05_CRM_2;
	}
	public void setR05_CRM_2(BigDecimal r05_CRM_2) {
		R05_CRM_2 = r05_CRM_2;
	}
	public BigDecimal getR05_NFB() {
		return R05_NFB;
	}
	public void setR05_NFB(BigDecimal r05_NFB) {
		R05_NFB = r05_NFB;
	}
	public String getR05_BOND() {
		return R05_BOND;
	}
	public void setR05_BOND(String r05_BOND) {
		R05_BOND = r05_BOND;
	}
	public String getR05_CP() {
		return R05_CP;
	}
	public void setR05_CP(String r05_CP) {
		R05_CP = r05_CP;
	}
	public String getR05_EQULITY() {
		return R05_EQULITY;
	}
	public void setR05_EQULITY(String r05_EQULITY) {
		R05_EQULITY = r05_EQULITY;
	}
	public String getR05_FOREX() {
		return R05_FOREX;
	}
	public void setR05_FOREX(String r05_FOREX) {
		R05_FOREX = r05_FOREX;
	}
	public String getR05_OTHERS() {
		return R05_OTHERS;
	}
	public void setR05_OTHERS(String r05_OTHERS) {
		R05_OTHERS = r05_OTHERS;
	}
	public String getR05_INT_BANK() {
		return R05_INT_BANK;
	}
	public void setR05_INT_BANK(String r05_INT_BANK) {
		R05_INT_BANK = r05_INT_BANK;
	}
	public String getR05_DERIVATIVE() {
		return R05_DERIVATIVE;
	}
	public void setR05_DERIVATIVE(String r05_DERIVATIVE) {
		R05_DERIVATIVE = r05_DERIVATIVE;
	}
	public String getR06_GROUP_CODE() {
		return R06_GROUP_CODE;
	}
	public void setR06_GROUP_CODE(String r06_GROUP_CODE) {
		R06_GROUP_CODE = r06_GROUP_CODE;
	}
	public String getR06_GROUP_NAME() {
		return R06_GROUP_NAME;
	}
	public void setR06_GROUP_NAME(String r06_GROUP_NAME) {
		R06_GROUP_NAME = r06_GROUP_NAME;
	}
	public BigDecimal getR06_CRM() {
		return R06_CRM;
	}
	public void setR06_CRM(BigDecimal r06_CRM) {
		R06_CRM = r06_CRM;
	}
	public BigDecimal getR06_NFBLT() {
		return R06_NFBLT;
	}
	public void setR06_NFBLT(BigDecimal r06_NFBLT) {
		R06_NFBLT = r06_NFBLT;
	}
	public BigDecimal getR06_NFBOS() {
		return R06_NFBOS;
	}
	public void setR06_NFBOS(BigDecimal r06_NFBOS) {
		R06_NFBOS = r06_NFBOS;
	}
	public BigDecimal getR06_CRM_2() {
		return R06_CRM_2;
	}
	public void setR06_CRM_2(BigDecimal r06_CRM_2) {
		R06_CRM_2 = r06_CRM_2;
	}
	public BigDecimal getR06_NFB() {
		return R06_NFB;
	}
	public void setR06_NFB(BigDecimal r06_NFB) {
		R06_NFB = r06_NFB;
	}
	public String getR06_BOND() {
		return R06_BOND;
	}
	public void setR06_BOND(String r06_BOND) {
		R06_BOND = r06_BOND;
	}
	public String getR06_CP() {
		return R06_CP;
	}
	public void setR06_CP(String r06_CP) {
		R06_CP = r06_CP;
	}
	public String getR06_EQULITY() {
		return R06_EQULITY;
	}
	public void setR06_EQULITY(String r06_EQULITY) {
		R06_EQULITY = r06_EQULITY;
	}
	public String getR06_FOREX() {
		return R06_FOREX;
	}
	public void setR06_FOREX(String r06_FOREX) {
		R06_FOREX = r06_FOREX;
	}
	public String getR06_OTHERS() {
		return R06_OTHERS;
	}
	public void setR06_OTHERS(String r06_OTHERS) {
		R06_OTHERS = r06_OTHERS;
	}
	public String getR06_INT_BANK() {
		return R06_INT_BANK;
	}
	public void setR06_INT_BANK(String r06_INT_BANK) {
		R06_INT_BANK = r06_INT_BANK;
	}
	public String getR06_DERIVATIVE() {
		return R06_DERIVATIVE;
	}
	public void setR06_DERIVATIVE(String r06_DERIVATIVE) {
		R06_DERIVATIVE = r06_DERIVATIVE;
	}
	public String getR07_GROUP_CODE() {
		return R07_GROUP_CODE;
	}
	public void setR07_GROUP_CODE(String r07_GROUP_CODE) {
		R07_GROUP_CODE = r07_GROUP_CODE;
	}
	public String getR07_GROUP_NAME() {
		return R07_GROUP_NAME;
	}
	public void setR07_GROUP_NAME(String r07_GROUP_NAME) {
		R07_GROUP_NAME = r07_GROUP_NAME;
	}
	public BigDecimal getR07_CRM() {
		return R07_CRM;
	}
	public void setR07_CRM(BigDecimal r07_CRM) {
		R07_CRM = r07_CRM;
	}
	public BigDecimal getR07_NFBLT() {
		return R07_NFBLT;
	}
	public void setR07_NFBLT(BigDecimal r07_NFBLT) {
		R07_NFBLT = r07_NFBLT;
	}
	public BigDecimal getR07_NFBOS() {
		return R07_NFBOS;
	}
	public void setR07_NFBOS(BigDecimal r07_NFBOS) {
		R07_NFBOS = r07_NFBOS;
	}
	public BigDecimal getR07_CRM_2() {
		return R07_CRM_2;
	}
	public void setR07_CRM_2(BigDecimal r07_CRM_2) {
		R07_CRM_2 = r07_CRM_2;
	}
	public BigDecimal getR07_NFB() {
		return R07_NFB;
	}
	public void setR07_NFB(BigDecimal r07_NFB) {
		R07_NFB = r07_NFB;
	}
	public String getR07_BOND() {
		return R07_BOND;
	}
	public void setR07_BOND(String r07_BOND) {
		R07_BOND = r07_BOND;
	}
	public String getR07_CP() {
		return R07_CP;
	}
	public void setR07_CP(String r07_CP) {
		R07_CP = r07_CP;
	}
	public String getR07_EQULITY() {
		return R07_EQULITY;
	}
	public void setR07_EQULITY(String r07_EQULITY) {
		R07_EQULITY = r07_EQULITY;
	}
	public String getR07_FOREX() {
		return R07_FOREX;
	}
	public void setR07_FOREX(String r07_FOREX) {
		R07_FOREX = r07_FOREX;
	}
	public String getR07_OTHERS() {
		return R07_OTHERS;
	}
	public void setR07_OTHERS(String r07_OTHERS) {
		R07_OTHERS = r07_OTHERS;
	}
	public String getR07_INT_BANK() {
		return R07_INT_BANK;
	}
	public void setR07_INT_BANK(String r07_INT_BANK) {
		R07_INT_BANK = r07_INT_BANK;
	}
	public String getR07_DERIVATIVE() {
		return R07_DERIVATIVE;
	}
	public void setR07_DERIVATIVE(String r07_DERIVATIVE) {
		R07_DERIVATIVE = r07_DERIVATIVE;
	}
	public String getR08_GROUP_CODE() {
		return R08_GROUP_CODE;
	}
	public void setR08_GROUP_CODE(String r08_GROUP_CODE) {
		R08_GROUP_CODE = r08_GROUP_CODE;
	}
	public String getR08_GROUP_NAME() {
		return R08_GROUP_NAME;
	}
	public void setR08_GROUP_NAME(String r08_GROUP_NAME) {
		R08_GROUP_NAME = r08_GROUP_NAME;
	}
	public BigDecimal getR08_CRM() {
		return R08_CRM;
	}
	public void setR08_CRM(BigDecimal r08_CRM) {
		R08_CRM = r08_CRM;
	}
	public BigDecimal getR08_NFBLT() {
		return R08_NFBLT;
	}
	public void setR08_NFBLT(BigDecimal r08_NFBLT) {
		R08_NFBLT = r08_NFBLT;
	}
	public BigDecimal getR08_NFBOS() {
		return R08_NFBOS;
	}
	public void setR08_NFBOS(BigDecimal r08_NFBOS) {
		R08_NFBOS = r08_NFBOS;
	}
	public BigDecimal getR08_CRM_2() {
		return R08_CRM_2;
	}
	public void setR08_CRM_2(BigDecimal r08_CRM_2) {
		R08_CRM_2 = r08_CRM_2;
	}
	public BigDecimal getR08_NFB() {
		return R08_NFB;
	}
	public void setR08_NFB(BigDecimal r08_NFB) {
		R08_NFB = r08_NFB;
	}
	public String getR08_BOND() {
		return R08_BOND;
	}
	public void setR08_BOND(String r08_BOND) {
		R08_BOND = r08_BOND;
	}
	public String getR08_CP() {
		return R08_CP;
	}
	public void setR08_CP(String r08_CP) {
		R08_CP = r08_CP;
	}
	public String getR08_EQULITY() {
		return R08_EQULITY;
	}
	public void setR08_EQULITY(String r08_EQULITY) {
		R08_EQULITY = r08_EQULITY;
	}
	public String getR08_FOREX() {
		return R08_FOREX;
	}
	public void setR08_FOREX(String r08_FOREX) {
		R08_FOREX = r08_FOREX;
	}
	public String getR08_OTHERS() {
		return R08_OTHERS;
	}
	public void setR08_OTHERS(String r08_OTHERS) {
		R08_OTHERS = r08_OTHERS;
	}
	public String getR08_INT_BANK() {
		return R08_INT_BANK;
	}
	public void setR08_INT_BANK(String r08_INT_BANK) {
		R08_INT_BANK = r08_INT_BANK;
	}
	public String getR08_DERIVATIVE() {
		return R08_DERIVATIVE;
	}
	public void setR08_DERIVATIVE(String r08_DERIVATIVE) {
		R08_DERIVATIVE = r08_DERIVATIVE;
	}
	public String getR09_GROUP_CODE() {
		return R09_GROUP_CODE;
	}
	public void setR09_GROUP_CODE(String r09_GROUP_CODE) {
		R09_GROUP_CODE = r09_GROUP_CODE;
	}
	public String getR09_GROUP_NAME() {
		return R09_GROUP_NAME;
	}
	public void setR09_GROUP_NAME(String r09_GROUP_NAME) {
		R09_GROUP_NAME = r09_GROUP_NAME;
	}
	public BigDecimal getR09_CRM() {
		return R09_CRM;
	}
	public void setR09_CRM(BigDecimal r09_CRM) {
		R09_CRM = r09_CRM;
	}
	public BigDecimal getR09_NFBLT() {
		return R09_NFBLT;
	}
	public void setR09_NFBLT(BigDecimal r09_NFBLT) {
		R09_NFBLT = r09_NFBLT;
	}
	public BigDecimal getR09_NFBOS() {
		return R09_NFBOS;
	}
	public void setR09_NFBOS(BigDecimal r09_NFBOS) {
		R09_NFBOS = r09_NFBOS;
	}
	public BigDecimal getR09_CRM_2() {
		return R09_CRM_2;
	}
	public void setR09_CRM_2(BigDecimal r09_CRM_2) {
		R09_CRM_2 = r09_CRM_2;
	}
	public BigDecimal getR09_NFB() {
		return R09_NFB;
	}
	public void setR09_NFB(BigDecimal r09_NFB) {
		R09_NFB = r09_NFB;
	}
	public String getR09_BOND() {
		return R09_BOND;
	}
	public void setR09_BOND(String r09_BOND) {
		R09_BOND = r09_BOND;
	}
	public String getR09_CP() {
		return R09_CP;
	}
	public void setR09_CP(String r09_CP) {
		R09_CP = r09_CP;
	}
	public String getR09_EQULITY() {
		return R09_EQULITY;
	}
	public void setR09_EQULITY(String r09_EQULITY) {
		R09_EQULITY = r09_EQULITY;
	}
	public String getR09_FOREX() {
		return R09_FOREX;
	}
	public void setR09_FOREX(String r09_FOREX) {
		R09_FOREX = r09_FOREX;
	}
	public String getR09_OTHERS() {
		return R09_OTHERS;
	}
	public void setR09_OTHERS(String r09_OTHERS) {
		R09_OTHERS = r09_OTHERS;
	}
	public String getR09_INT_BANK() {
		return R09_INT_BANK;
	}
	public void setR09_INT_BANK(String r09_INT_BANK) {
		R09_INT_BANK = r09_INT_BANK;
	}
	public String getR09_DERIVATIVE() {
		return R09_DERIVATIVE;
	}
	public void setR09_DERIVATIVE(String r09_DERIVATIVE) {
		R09_DERIVATIVE = r09_DERIVATIVE;
	}
	public String getR10_GROUP_CODE() {
		return R10_GROUP_CODE;
	}
	public void setR10_GROUP_CODE(String r10_GROUP_CODE) {
		R10_GROUP_CODE = r10_GROUP_CODE;
	}
	public String getR10_GROUP_NAME() {
		return R10_GROUP_NAME;
	}
	public void setR10_GROUP_NAME(String r10_GROUP_NAME) {
		R10_GROUP_NAME = r10_GROUP_NAME;
	}
	public BigDecimal getR10_CRM() {
		return R10_CRM;
	}
	public void setR10_CRM(BigDecimal r10_CRM) {
		R10_CRM = r10_CRM;
	}
	public BigDecimal getR10_NFBLT() {
		return R10_NFBLT;
	}
	public void setR10_NFBLT(BigDecimal r10_NFBLT) {
		R10_NFBLT = r10_NFBLT;
	}
	public BigDecimal getR10_NFBOS() {
		return R10_NFBOS;
	}
	public void setR10_NFBOS(BigDecimal r10_NFBOS) {
		R10_NFBOS = r10_NFBOS;
	}
	public BigDecimal getR10_CRM_2() {
		return R10_CRM_2;
	}
	public void setR10_CRM_2(BigDecimal r10_CRM_2) {
		R10_CRM_2 = r10_CRM_2;
	}
	public BigDecimal getR10_NFB() {
		return R10_NFB;
	}
	public void setR10_NFB(BigDecimal r10_NFB) {
		R10_NFB = r10_NFB;
	}
	public String getR10_BOND() {
		return R10_BOND;
	}
	public void setR10_BOND(String r10_BOND) {
		R10_BOND = r10_BOND;
	}
	public String getR10_CP() {
		return R10_CP;
	}
	public void setR10_CP(String r10_CP) {
		R10_CP = r10_CP;
	}
	public String getR10_EQULITY() {
		return R10_EQULITY;
	}
	public void setR10_EQULITY(String r10_EQULITY) {
		R10_EQULITY = r10_EQULITY;
	}
	public String getR10_FOREX() {
		return R10_FOREX;
	}
	public void setR10_FOREX(String r10_FOREX) {
		R10_FOREX = r10_FOREX;
	}
	public String getR10_OTHERS() {
		return R10_OTHERS;
	}
	public void setR10_OTHERS(String r10_OTHERS) {
		R10_OTHERS = r10_OTHERS;
	}
	public String getR10_INT_BANK() {
		return R10_INT_BANK;
	}
	public void setR10_INT_BANK(String r10_INT_BANK) {
		R10_INT_BANK = r10_INT_BANK;
	}
	public String getR10_DERIVATIVE() {
		return R10_DERIVATIVE;
	}
	public void setR10_DERIVATIVE(String r10_DERIVATIVE) {
		R10_DERIVATIVE = r10_DERIVATIVE;
	}
	public String getR11_GROUP_CODE() {
		return R11_GROUP_CODE;
	}
	public void setR11_GROUP_CODE(String r11_GROUP_CODE) {
		R11_GROUP_CODE = r11_GROUP_CODE;
	}
	public String getR11_GROUP_NAME() {
		return R11_GROUP_NAME;
	}
	public void setR11_GROUP_NAME(String r11_GROUP_NAME) {
		R11_GROUP_NAME = r11_GROUP_NAME;
	}
	public BigDecimal getR11_CRM() {
		return R11_CRM;
	}
	public void setR11_CRM(BigDecimal r11_CRM) {
		R11_CRM = r11_CRM;
	}
	public BigDecimal getR11_NFBLT() {
		return R11_NFBLT;
	}
	public void setR11_NFBLT(BigDecimal r11_NFBLT) {
		R11_NFBLT = r11_NFBLT;
	}
	public BigDecimal getR11_NFBOS() {
		return R11_NFBOS;
	}
	public void setR11_NFBOS(BigDecimal r11_NFBOS) {
		R11_NFBOS = r11_NFBOS;
	}
	public BigDecimal getR11_CRM_2() {
		return R11_CRM_2;
	}
	public void setR11_CRM_2(BigDecimal r11_CRM_2) {
		R11_CRM_2 = r11_CRM_2;
	}
	public BigDecimal getR11_NFB() {
		return R11_NFB;
	}
	public void setR11_NFB(BigDecimal r11_NFB) {
		R11_NFB = r11_NFB;
	}
	public String getR11_BOND() {
		return R11_BOND;
	}
	public void setR11_BOND(String r11_BOND) {
		R11_BOND = r11_BOND;
	}
	public String getR11_CP() {
		return R11_CP;
	}
	public void setR11_CP(String r11_CP) {
		R11_CP = r11_CP;
	}
	public String getR11_EQULITY() {
		return R11_EQULITY;
	}
	public void setR11_EQULITY(String r11_EQULITY) {
		R11_EQULITY = r11_EQULITY;
	}
	public String getR11_FOREX() {
		return R11_FOREX;
	}
	public void setR11_FOREX(String r11_FOREX) {
		R11_FOREX = r11_FOREX;
	}
	public String getR11_OTHERS() {
		return R11_OTHERS;
	}
	public void setR11_OTHERS(String r11_OTHERS) {
		R11_OTHERS = r11_OTHERS;
	}
	public String getR11_INT_BANK() {
		return R11_INT_BANK;
	}
	public void setR11_INT_BANK(String r11_INT_BANK) {
		R11_INT_BANK = r11_INT_BANK;
	}
	public String getR11_DERIVATIVE() {
		return R11_DERIVATIVE;
	}
	public void setR11_DERIVATIVE(String r11_DERIVATIVE) {
		R11_DERIVATIVE = r11_DERIVATIVE;
	}
	public String getR12_GROUP_CODE() {
		return R12_GROUP_CODE;
	}
	public void setR12_GROUP_CODE(String r12_GROUP_CODE) {
		R12_GROUP_CODE = r12_GROUP_CODE;
	}
	public String getR12_GROUP_NAME() {
		return R12_GROUP_NAME;
	}
	public void setR12_GROUP_NAME(String r12_GROUP_NAME) {
		R12_GROUP_NAME = r12_GROUP_NAME;
	}
	public BigDecimal getR12_CRM() {
		return R12_CRM;
	}
	public void setR12_CRM(BigDecimal r12_CRM) {
		R12_CRM = r12_CRM;
	}
	public BigDecimal getR12_NFBLT() {
		return R12_NFBLT;
	}
	public void setR12_NFBLT(BigDecimal r12_NFBLT) {
		R12_NFBLT = r12_NFBLT;
	}
	public BigDecimal getR12_NFBOS() {
		return R12_NFBOS;
	}
	public void setR12_NFBOS(BigDecimal r12_NFBOS) {
		R12_NFBOS = r12_NFBOS;
	}
	public BigDecimal getR12_CRM_2() {
		return R12_CRM_2;
	}
	public void setR12_CRM_2(BigDecimal r12_CRM_2) {
		R12_CRM_2 = r12_CRM_2;
	}
	public BigDecimal getR12_NFB() {
		return R12_NFB;
	}
	public void setR12_NFB(BigDecimal r12_NFB) {
		R12_NFB = r12_NFB;
	}
	public String getR12_BOND() {
		return R12_BOND;
	}
	public void setR12_BOND(String r12_BOND) {
		R12_BOND = r12_BOND;
	}
	public String getR12_CP() {
		return R12_CP;
	}
	public void setR12_CP(String r12_CP) {
		R12_CP = r12_CP;
	}
	public String getR12_EQULITY() {
		return R12_EQULITY;
	}
	public void setR12_EQULITY(String r12_EQULITY) {
		R12_EQULITY = r12_EQULITY;
	}
	public String getR12_FOREX() {
		return R12_FOREX;
	}
	public void setR12_FOREX(String r12_FOREX) {
		R12_FOREX = r12_FOREX;
	}
	public String getR12_OTHERS() {
		return R12_OTHERS;
	}
	public void setR12_OTHERS(String r12_OTHERS) {
		R12_OTHERS = r12_OTHERS;
	}
	public String getR12_INT_BANK() {
		return R12_INT_BANK;
	}
	public void setR12_INT_BANK(String r12_INT_BANK) {
		R12_INT_BANK = r12_INT_BANK;
	}
	public String getR12_DERIVATIVE() {
		return R12_DERIVATIVE;
	}
	public void setR12_DERIVATIVE(String r12_DERIVATIVE) {
		R12_DERIVATIVE = r12_DERIVATIVE;
	}
	public String getR13_GROUP_CODE() {
		return R13_GROUP_CODE;
	}
	public void setR13_GROUP_CODE(String r13_GROUP_CODE) {
		R13_GROUP_CODE = r13_GROUP_CODE;
	}
	public String getR13_GROUP_NAME() {
		return R13_GROUP_NAME;
	}
	public void setR13_GROUP_NAME(String r13_GROUP_NAME) {
		R13_GROUP_NAME = r13_GROUP_NAME;
	}
	public BigDecimal getR13_CRM() {
		return R13_CRM;
	}
	public void setR13_CRM(BigDecimal r13_CRM) {
		R13_CRM = r13_CRM;
	}
	public BigDecimal getR13_NFBLT() {
		return R13_NFBLT;
	}
	public void setR13_NFBLT(BigDecimal r13_NFBLT) {
		R13_NFBLT = r13_NFBLT;
	}
	public BigDecimal getR13_NFBOS() {
		return R13_NFBOS;
	}
	public void setR13_NFBOS(BigDecimal r13_NFBOS) {
		R13_NFBOS = r13_NFBOS;
	}
	public BigDecimal getR13_CRM_2() {
		return R13_CRM_2;
	}
	public void setR13_CRM_2(BigDecimal r13_CRM_2) {
		R13_CRM_2 = r13_CRM_2;
	}
	public BigDecimal getR13_NFB() {
		return R13_NFB;
	}
	public void setR13_NFB(BigDecimal r13_NFB) {
		R13_NFB = r13_NFB;
	}
	public String getR13_BOND() {
		return R13_BOND;
	}
	public void setR13_BOND(String r13_BOND) {
		R13_BOND = r13_BOND;
	}
	public String getR13_CP() {
		return R13_CP;
	}
	public void setR13_CP(String r13_CP) {
		R13_CP = r13_CP;
	}
	public String getR13_EQULITY() {
		return R13_EQULITY;
	}
	public void setR13_EQULITY(String r13_EQULITY) {
		R13_EQULITY = r13_EQULITY;
	}
	public String getR13_FOREX() {
		return R13_FOREX;
	}
	public void setR13_FOREX(String r13_FOREX) {
		R13_FOREX = r13_FOREX;
	}
	public String getR13_OTHERS() {
		return R13_OTHERS;
	}
	public void setR13_OTHERS(String r13_OTHERS) {
		R13_OTHERS = r13_OTHERS;
	}
	public String getR13_INT_BANK() {
		return R13_INT_BANK;
	}
	public void setR13_INT_BANK(String r13_INT_BANK) {
		R13_INT_BANK = r13_INT_BANK;
	}
	public String getR13_DERIVATIVE() {
		return R13_DERIVATIVE;
	}
	public void setR13_DERIVATIVE(String r13_DERIVATIVE) {
		R13_DERIVATIVE = r13_DERIVATIVE;
	}
	public String getR14_GROUP_CODE() {
		return R14_GROUP_CODE;
	}
	public void setR14_GROUP_CODE(String r14_GROUP_CODE) {
		R14_GROUP_CODE = r14_GROUP_CODE;
	}
	public String getR14_GROUP_NAME() {
		return R14_GROUP_NAME;
	}
	public void setR14_GROUP_NAME(String r14_GROUP_NAME) {
		R14_GROUP_NAME = r14_GROUP_NAME;
	}
	public BigDecimal getR14_CRM() {
		return R14_CRM;
	}
	public void setR14_CRM(BigDecimal r14_CRM) {
		R14_CRM = r14_CRM;
	}
	public BigDecimal getR14_NFBLT() {
		return R14_NFBLT;
	}
	public void setR14_NFBLT(BigDecimal r14_NFBLT) {
		R14_NFBLT = r14_NFBLT;
	}
	public BigDecimal getR14_NFBOS() {
		return R14_NFBOS;
	}
	public void setR14_NFBOS(BigDecimal r14_NFBOS) {
		R14_NFBOS = r14_NFBOS;
	}
	public BigDecimal getR14_CRM_2() {
		return R14_CRM_2;
	}
	public void setR14_CRM_2(BigDecimal r14_CRM_2) {
		R14_CRM_2 = r14_CRM_2;
	}
	public BigDecimal getR14_NFB() {
		return R14_NFB;
	}
	public void setR14_NFB(BigDecimal r14_NFB) {
		R14_NFB = r14_NFB;
	}
	public String getR14_BOND() {
		return R14_BOND;
	}
	public void setR14_BOND(String r14_BOND) {
		R14_BOND = r14_BOND;
	}
	public String getR14_CP() {
		return R14_CP;
	}
	public void setR14_CP(String r14_CP) {
		R14_CP = r14_CP;
	}
	public String getR14_EQULITY() {
		return R14_EQULITY;
	}
	public void setR14_EQULITY(String r14_EQULITY) {
		R14_EQULITY = r14_EQULITY;
	}
	public String getR14_FOREX() {
		return R14_FOREX;
	}
	public void setR14_FOREX(String r14_FOREX) {
		R14_FOREX = r14_FOREX;
	}
	public String getR14_OTHERS() {
		return R14_OTHERS;
	}
	public void setR14_OTHERS(String r14_OTHERS) {
		R14_OTHERS = r14_OTHERS;
	}
	public String getR14_INT_BANK() {
		return R14_INT_BANK;
	}
	public void setR14_INT_BANK(String r14_INT_BANK) {
		R14_INT_BANK = r14_INT_BANK;
	}
	public String getR14_DERIVATIVE() {
		return R14_DERIVATIVE;
	}
	public void setR14_DERIVATIVE(String r14_DERIVATIVE) {
		R14_DERIVATIVE = r14_DERIVATIVE;
	}
	public String getR15_GROUP_CODE() {
		return R15_GROUP_CODE;
	}
	public void setR15_GROUP_CODE(String r15_GROUP_CODE) {
		R15_GROUP_CODE = r15_GROUP_CODE;
	}
	public String getR15_GROUP_NAME() {
		return R15_GROUP_NAME;
	}
	public void setR15_GROUP_NAME(String r15_GROUP_NAME) {
		R15_GROUP_NAME = r15_GROUP_NAME;
	}
	public BigDecimal getR15_CRM() {
		return R15_CRM;
	}
	public void setR15_CRM(BigDecimal r15_CRM) {
		R15_CRM = r15_CRM;
	}
	public BigDecimal getR15_NFBLT() {
		return R15_NFBLT;
	}
	public void setR15_NFBLT(BigDecimal r15_NFBLT) {
		R15_NFBLT = r15_NFBLT;
	}
	public BigDecimal getR15_NFBOS() {
		return R15_NFBOS;
	}
	public void setR15_NFBOS(BigDecimal r15_NFBOS) {
		R15_NFBOS = r15_NFBOS;
	}
	public BigDecimal getR15_CRM_2() {
		return R15_CRM_2;
	}
	public void setR15_CRM_2(BigDecimal r15_CRM_2) {
		R15_CRM_2 = r15_CRM_2;
	}
	public BigDecimal getR15_NFB() {
		return R15_NFB;
	}
	public void setR15_NFB(BigDecimal r15_NFB) {
		R15_NFB = r15_NFB;
	}
	public String getR15_BOND() {
		return R15_BOND;
	}
	public void setR15_BOND(String r15_BOND) {
		R15_BOND = r15_BOND;
	}
	public String getR15_CP() {
		return R15_CP;
	}
	public void setR15_CP(String r15_CP) {
		R15_CP = r15_CP;
	}
	public String getR15_EQULITY() {
		return R15_EQULITY;
	}
	public void setR15_EQULITY(String r15_EQULITY) {
		R15_EQULITY = r15_EQULITY;
	}
	public String getR15_FOREX() {
		return R15_FOREX;
	}
	public void setR15_FOREX(String r15_FOREX) {
		R15_FOREX = r15_FOREX;
	}
	public String getR15_OTHERS() {
		return R15_OTHERS;
	}
	public void setR15_OTHERS(String r15_OTHERS) {
		R15_OTHERS = r15_OTHERS;
	}
	public String getR15_INT_BANK() {
		return R15_INT_BANK;
	}
	public void setR15_INT_BANK(String r15_INT_BANK) {
		R15_INT_BANK = r15_INT_BANK;
	}
	public String getR15_DERIVATIVE() {
		return R15_DERIVATIVE;
	}
	public void setR15_DERIVATIVE(String r15_DERIVATIVE) {
		R15_DERIVATIVE = r15_DERIVATIVE;
	}
	public String getR16_GROUP_CODE() {
		return R16_GROUP_CODE;
	}
	public void setR16_GROUP_CODE(String r16_GROUP_CODE) {
		R16_GROUP_CODE = r16_GROUP_CODE;
	}
	public String getR16_GROUP_NAME() {
		return R16_GROUP_NAME;
	}
	public void setR16_GROUP_NAME(String r16_GROUP_NAME) {
		R16_GROUP_NAME = r16_GROUP_NAME;
	}
	public BigDecimal getR16_CRM() {
		return R16_CRM;
	}
	public void setR16_CRM(BigDecimal r16_CRM) {
		R16_CRM = r16_CRM;
	}
	public BigDecimal getR16_NFBLT() {
		return R16_NFBLT;
	}
	public void setR16_NFBLT(BigDecimal r16_NFBLT) {
		R16_NFBLT = r16_NFBLT;
	}
	public BigDecimal getR16_NFBOS() {
		return R16_NFBOS;
	}
	public void setR16_NFBOS(BigDecimal r16_NFBOS) {
		R16_NFBOS = r16_NFBOS;
	}
	public BigDecimal getR16_CRM_2() {
		return R16_CRM_2;
	}
	public void setR16_CRM_2(BigDecimal r16_CRM_2) {
		R16_CRM_2 = r16_CRM_2;
	}
	public BigDecimal getR16_NFB() {
		return R16_NFB;
	}
	public void setR16_NFB(BigDecimal r16_NFB) {
		R16_NFB = r16_NFB;
	}
	public String getR16_BOND() {
		return R16_BOND;
	}
	public void setR16_BOND(String r16_BOND) {
		R16_BOND = r16_BOND;
	}
	public String getR16_CP() {
		return R16_CP;
	}
	public void setR16_CP(String r16_CP) {
		R16_CP = r16_CP;
	}
	public String getR16_EQULITY() {
		return R16_EQULITY;
	}
	public void setR16_EQULITY(String r16_EQULITY) {
		R16_EQULITY = r16_EQULITY;
	}
	public String getR16_FOREX() {
		return R16_FOREX;
	}
	public void setR16_FOREX(String r16_FOREX) {
		R16_FOREX = r16_FOREX;
	}
	public String getR16_OTHERS() {
		return R16_OTHERS;
	}
	public void setR16_OTHERS(String r16_OTHERS) {
		R16_OTHERS = r16_OTHERS;
	}
	public String getR16_INT_BANK() {
		return R16_INT_BANK;
	}
	public void setR16_INT_BANK(String r16_INT_BANK) {
		R16_INT_BANK = r16_INT_BANK;
	}
	public String getR16_DERIVATIVE() {
		return R16_DERIVATIVE;
	}
	public void setR16_DERIVATIVE(String r16_DERIVATIVE) {
		R16_DERIVATIVE = r16_DERIVATIVE;
	}
	public String getR17_GROUP_CODE() {
		return R17_GROUP_CODE;
	}
	public void setR17_GROUP_CODE(String r17_GROUP_CODE) {
		R17_GROUP_CODE = r17_GROUP_CODE;
	}
	public String getR17_GROUP_NAME() {
		return R17_GROUP_NAME;
	}
	public void setR17_GROUP_NAME(String r17_GROUP_NAME) {
		R17_GROUP_NAME = r17_GROUP_NAME;
	}
	public BigDecimal getR17_CRM() {
		return R17_CRM;
	}
	public void setR17_CRM(BigDecimal r17_CRM) {
		R17_CRM = r17_CRM;
	}
	public BigDecimal getR17_NFBLT() {
		return R17_NFBLT;
	}
	public void setR17_NFBLT(BigDecimal r17_NFBLT) {
		R17_NFBLT = r17_NFBLT;
	}
	public BigDecimal getR17_NFBOS() {
		return R17_NFBOS;
	}
	public void setR17_NFBOS(BigDecimal r17_NFBOS) {
		R17_NFBOS = r17_NFBOS;
	}
	public BigDecimal getR17_CRM_2() {
		return R17_CRM_2;
	}
	public void setR17_CRM_2(BigDecimal r17_CRM_2) {
		R17_CRM_2 = r17_CRM_2;
	}
	public BigDecimal getR17_NFB() {
		return R17_NFB;
	}
	public void setR17_NFB(BigDecimal r17_NFB) {
		R17_NFB = r17_NFB;
	}
	public String getR17_BOND() {
		return R17_BOND;
	}
	public void setR17_BOND(String r17_BOND) {
		R17_BOND = r17_BOND;
	}
	public String getR17_CP() {
		return R17_CP;
	}
	public void setR17_CP(String r17_CP) {
		R17_CP = r17_CP;
	}
	public String getR17_EQULITY() {
		return R17_EQULITY;
	}
	public void setR17_EQULITY(String r17_EQULITY) {
		R17_EQULITY = r17_EQULITY;
	}
	public String getR17_FOREX() {
		return R17_FOREX;
	}
	public void setR17_FOREX(String r17_FOREX) {
		R17_FOREX = r17_FOREX;
	}
	public String getR17_OTHERS() {
		return R17_OTHERS;
	}
	public void setR17_OTHERS(String r17_OTHERS) {
		R17_OTHERS = r17_OTHERS;
	}
	public String getR17_INT_BANK() {
		return R17_INT_BANK;
	}
	public void setR17_INT_BANK(String r17_INT_BANK) {
		R17_INT_BANK = r17_INT_BANK;
	}
	public String getR17_DERIVATIVE() {
		return R17_DERIVATIVE;
	}
	public void setR17_DERIVATIVE(String r17_DERIVATIVE) {
		R17_DERIVATIVE = r17_DERIVATIVE;
	}
	public String getR18_GROUP_CODE() {
		return R18_GROUP_CODE;
	}
	public void setR18_GROUP_CODE(String r18_GROUP_CODE) {
		R18_GROUP_CODE = r18_GROUP_CODE;
	}
	public String getR18_GROUP_NAME() {
		return R18_GROUP_NAME;
	}
	public void setR18_GROUP_NAME(String r18_GROUP_NAME) {
		R18_GROUP_NAME = r18_GROUP_NAME;
	}
	public BigDecimal getR18_CRM() {
		return R18_CRM;
	}
	public void setR18_CRM(BigDecimal r18_CRM) {
		R18_CRM = r18_CRM;
	}
	public BigDecimal getR18_NFBLT() {
		return R18_NFBLT;
	}
	public void setR18_NFBLT(BigDecimal r18_NFBLT) {
		R18_NFBLT = r18_NFBLT;
	}
	public BigDecimal getR18_NFBOS() {
		return R18_NFBOS;
	}
	public void setR18_NFBOS(BigDecimal r18_NFBOS) {
		R18_NFBOS = r18_NFBOS;
	}
	public BigDecimal getR18_CRM_2() {
		return R18_CRM_2;
	}
	public void setR18_CRM_2(BigDecimal r18_CRM_2) {
		R18_CRM_2 = r18_CRM_2;
	}
	public BigDecimal getR18_NFB() {
		return R18_NFB;
	}
	public void setR18_NFB(BigDecimal r18_NFB) {
		R18_NFB = r18_NFB;
	}
	public String getR18_BOND() {
		return R18_BOND;
	}
	public void setR18_BOND(String r18_BOND) {
		R18_BOND = r18_BOND;
	}
	public String getR18_CP() {
		return R18_CP;
	}
	public void setR18_CP(String r18_CP) {
		R18_CP = r18_CP;
	}
	public String getR18_EQULITY() {
		return R18_EQULITY;
	}
	public void setR18_EQULITY(String r18_EQULITY) {
		R18_EQULITY = r18_EQULITY;
	}
	public String getR18_FOREX() {
		return R18_FOREX;
	}
	public void setR18_FOREX(String r18_FOREX) {
		R18_FOREX = r18_FOREX;
	}
	public String getR18_OTHERS() {
		return R18_OTHERS;
	}
	public void setR18_OTHERS(String r18_OTHERS) {
		R18_OTHERS = r18_OTHERS;
	}
	public String getR18_INT_BANK() {
		return R18_INT_BANK;
	}
	public void setR18_INT_BANK(String r18_INT_BANK) {
		R18_INT_BANK = r18_INT_BANK;
	}
	public String getR18_DERIVATIVE() {
		return R18_DERIVATIVE;
	}
	public void setR18_DERIVATIVE(String r18_DERIVATIVE) {
		R18_DERIVATIVE = r18_DERIVATIVE;
	}
	public String getR19_GROUP_CODE() {
		return R19_GROUP_CODE;
	}
	public void setR19_GROUP_CODE(String r19_GROUP_CODE) {
		R19_GROUP_CODE = r19_GROUP_CODE;
	}
	public String getR19_GROUP_NAME() {
		return R19_GROUP_NAME;
	}
	public void setR19_GROUP_NAME(String r19_GROUP_NAME) {
		R19_GROUP_NAME = r19_GROUP_NAME;
	}
	public BigDecimal getR19_CRM() {
		return R19_CRM;
	}
	public void setR19_CRM(BigDecimal r19_CRM) {
		R19_CRM = r19_CRM;
	}
	public BigDecimal getR19_NFBLT() {
		return R19_NFBLT;
	}
	public void setR19_NFBLT(BigDecimal r19_NFBLT) {
		R19_NFBLT = r19_NFBLT;
	}
	public BigDecimal getR19_NFBOS() {
		return R19_NFBOS;
	}
	public void setR19_NFBOS(BigDecimal r19_NFBOS) {
		R19_NFBOS = r19_NFBOS;
	}
	public BigDecimal getR19_CRM_2() {
		return R19_CRM_2;
	}
	public void setR19_CRM_2(BigDecimal r19_CRM_2) {
		R19_CRM_2 = r19_CRM_2;
	}
	public BigDecimal getR19_NFB() {
		return R19_NFB;
	}
	public void setR19_NFB(BigDecimal r19_NFB) {
		R19_NFB = r19_NFB;
	}
	public String getR19_BOND() {
		return R19_BOND;
	}
	public void setR19_BOND(String r19_BOND) {
		R19_BOND = r19_BOND;
	}
	public String getR19_CP() {
		return R19_CP;
	}
	public void setR19_CP(String r19_CP) {
		R19_CP = r19_CP;
	}
	public String getR19_EQULITY() {
		return R19_EQULITY;
	}
	public void setR19_EQULITY(String r19_EQULITY) {
		R19_EQULITY = r19_EQULITY;
	}
	public String getR19_FOREX() {
		return R19_FOREX;
	}
	public void setR19_FOREX(String r19_FOREX) {
		R19_FOREX = r19_FOREX;
	}
	public String getR19_OTHERS() {
		return R19_OTHERS;
	}
	public void setR19_OTHERS(String r19_OTHERS) {
		R19_OTHERS = r19_OTHERS;
	}
	public String getR19_INT_BANK() {
		return R19_INT_BANK;
	}
	public void setR19_INT_BANK(String r19_INT_BANK) {
		R19_INT_BANK = r19_INT_BANK;
	}
	public String getR19_DERIVATIVE() {
		return R19_DERIVATIVE;
	}
	public void setR19_DERIVATIVE(String r19_DERIVATIVE) {
		R19_DERIVATIVE = r19_DERIVATIVE;
	}
	public String getR20_GROUP_CODE() {
		return R20_GROUP_CODE;
	}
	public void setR20_GROUP_CODE(String r20_GROUP_CODE) {
		R20_GROUP_CODE = r20_GROUP_CODE;
	}
	public String getR20_GROUP_NAME() {
		return R20_GROUP_NAME;
	}
	public void setR20_GROUP_NAME(String r20_GROUP_NAME) {
		R20_GROUP_NAME = r20_GROUP_NAME;
	}
	public BigDecimal getR20_CRM() {
		return R20_CRM;
	}
	public void setR20_CRM(BigDecimal r20_CRM) {
		R20_CRM = r20_CRM;
	}
	public BigDecimal getR20_NFBLT() {
		return R20_NFBLT;
	}
	public void setR20_NFBLT(BigDecimal r20_NFBLT) {
		R20_NFBLT = r20_NFBLT;
	}
	public BigDecimal getR20_NFBOS() {
		return R20_NFBOS;
	}
	public void setR20_NFBOS(BigDecimal r20_NFBOS) {
		R20_NFBOS = r20_NFBOS;
	}
	public BigDecimal getR20_CRM_2() {
		return R20_CRM_2;
	}
	public void setR20_CRM_2(BigDecimal r20_CRM_2) {
		R20_CRM_2 = r20_CRM_2;
	}
	public BigDecimal getR20_NFB() {
		return R20_NFB;
	}
	public void setR20_NFB(BigDecimal r20_NFB) {
		R20_NFB = r20_NFB;
	}
	public String getR20_BOND() {
		return R20_BOND;
	}
	public void setR20_BOND(String r20_BOND) {
		R20_BOND = r20_BOND;
	}
	public String getR20_CP() {
		return R20_CP;
	}
	public void setR20_CP(String r20_CP) {
		R20_CP = r20_CP;
	}
	public String getR20_EQULITY() {
		return R20_EQULITY;
	}
	public void setR20_EQULITY(String r20_EQULITY) {
		R20_EQULITY = r20_EQULITY;
	}
	public String getR20_FOREX() {
		return R20_FOREX;
	}
	public void setR20_FOREX(String r20_FOREX) {
		R20_FOREX = r20_FOREX;
	}
	public String getR20_OTHERS() {
		return R20_OTHERS;
	}
	public void setR20_OTHERS(String r20_OTHERS) {
		R20_OTHERS = r20_OTHERS;
	}
	public String getR20_INT_BANK() {
		return R20_INT_BANK;
	}
	public void setR20_INT_BANK(String r20_INT_BANK) {
		R20_INT_BANK = r20_INT_BANK;
	}
	public String getR20_DERIVATIVE() {
		return R20_DERIVATIVE;
	}
	public void setR20_DERIVATIVE(String r20_DERIVATIVE) {
		R20_DERIVATIVE = r20_DERIVATIVE;
	}
	public String getR21_GROUP_CODE() {
		return R21_GROUP_CODE;
	}
	public void setR21_GROUP_CODE(String r21_GROUP_CODE) {
		R21_GROUP_CODE = r21_GROUP_CODE;
	}
	public String getR21_GROUP_NAME() {
		return R21_GROUP_NAME;
	}
	public void setR21_GROUP_NAME(String r21_GROUP_NAME) {
		R21_GROUP_NAME = r21_GROUP_NAME;
	}
	public BigDecimal getR21_CRM() {
		return R21_CRM;
	}
	public void setR21_CRM(BigDecimal r21_CRM) {
		R21_CRM = r21_CRM;
	}
	public BigDecimal getR21_NFBLT() {
		return R21_NFBLT;
	}
	public void setR21_NFBLT(BigDecimal r21_NFBLT) {
		R21_NFBLT = r21_NFBLT;
	}
	public BigDecimal getR21_NFBOS() {
		return R21_NFBOS;
	}
	public void setR21_NFBOS(BigDecimal r21_NFBOS) {
		R21_NFBOS = r21_NFBOS;
	}
	public BigDecimal getR21_CRM_2() {
		return R21_CRM_2;
	}
	public void setR21_CRM_2(BigDecimal r21_CRM_2) {
		R21_CRM_2 = r21_CRM_2;
	}
	public BigDecimal getR21_NFB() {
		return R21_NFB;
	}
	public void setR21_NFB(BigDecimal r21_NFB) {
		R21_NFB = r21_NFB;
	}
	public String getR21_BOND() {
		return R21_BOND;
	}
	public void setR21_BOND(String r21_BOND) {
		R21_BOND = r21_BOND;
	}
	public String getR21_CP() {
		return R21_CP;
	}
	public void setR21_CP(String r21_CP) {
		R21_CP = r21_CP;
	}
	public String getR21_EQULITY() {
		return R21_EQULITY;
	}
	public void setR21_EQULITY(String r21_EQULITY) {
		R21_EQULITY = r21_EQULITY;
	}
	public String getR21_FOREX() {
		return R21_FOREX;
	}
	public void setR21_FOREX(String r21_FOREX) {
		R21_FOREX = r21_FOREX;
	}
	public String getR21_OTHERS() {
		return R21_OTHERS;
	}
	public void setR21_OTHERS(String r21_OTHERS) {
		R21_OTHERS = r21_OTHERS;
	}
	public String getR21_INT_BANK() {
		return R21_INT_BANK;
	}
	public void setR21_INT_BANK(String r21_INT_BANK) {
		R21_INT_BANK = r21_INT_BANK;
	}
	public String getR21_DERIVATIVE() {
		return R21_DERIVATIVE;
	}
	public void setR21_DERIVATIVE(String r21_DERIVATIVE) {
		R21_DERIVATIVE = r21_DERIVATIVE;
	}
	public String getR22_GROUP_CODE() {
		return R22_GROUP_CODE;
	}
	public void setR22_GROUP_CODE(String r22_GROUP_CODE) {
		R22_GROUP_CODE = r22_GROUP_CODE;
	}
	public String getR22_GROUP_NAME() {
		return R22_GROUP_NAME;
	}
	public void setR22_GROUP_NAME(String r22_GROUP_NAME) {
		R22_GROUP_NAME = r22_GROUP_NAME;
	}
	public BigDecimal getR22_CRM() {
		return R22_CRM;
	}
	public void setR22_CRM(BigDecimal r22_CRM) {
		R22_CRM = r22_CRM;
	}
	public BigDecimal getR22_NFBLT() {
		return R22_NFBLT;
	}
	public void setR22_NFBLT(BigDecimal r22_NFBLT) {
		R22_NFBLT = r22_NFBLT;
	}
	public BigDecimal getR22_NFBOS() {
		return R22_NFBOS;
	}
	public void setR22_NFBOS(BigDecimal r22_NFBOS) {
		R22_NFBOS = r22_NFBOS;
	}
	public BigDecimal getR22_CRM_2() {
		return R22_CRM_2;
	}
	public void setR22_CRM_2(BigDecimal r22_CRM_2) {
		R22_CRM_2 = r22_CRM_2;
	}
	public BigDecimal getR22_NFB() {
		return R22_NFB;
	}
	public void setR22_NFB(BigDecimal r22_NFB) {
		R22_NFB = r22_NFB;
	}
	public String getR22_BOND() {
		return R22_BOND;
	}
	public void setR22_BOND(String r22_BOND) {
		R22_BOND = r22_BOND;
	}
	public String getR22_CP() {
		return R22_CP;
	}
	public void setR22_CP(String r22_CP) {
		R22_CP = r22_CP;
	}
	public String getR22_EQULITY() {
		return R22_EQULITY;
	}
	public void setR22_EQULITY(String r22_EQULITY) {
		R22_EQULITY = r22_EQULITY;
	}
	public String getR22_FOREX() {
		return R22_FOREX;
	}
	public void setR22_FOREX(String r22_FOREX) {
		R22_FOREX = r22_FOREX;
	}
	public String getR22_OTHERS() {
		return R22_OTHERS;
	}
	public void setR22_OTHERS(String r22_OTHERS) {
		R22_OTHERS = r22_OTHERS;
	}
	public String getR22_INT_BANK() {
		return R22_INT_BANK;
	}
	public void setR22_INT_BANK(String r22_INT_BANK) {
		R22_INT_BANK = r22_INT_BANK;
	}
	public String getR22_DERIVATIVE() {
		return R22_DERIVATIVE;
	}
	public void setR22_DERIVATIVE(String r22_DERIVATIVE) {
		R22_DERIVATIVE = r22_DERIVATIVE;
	}
	public String getR23_GROUP_CODE() {
		return R23_GROUP_CODE;
	}
	public void setR23_GROUP_CODE(String r23_GROUP_CODE) {
		R23_GROUP_CODE = r23_GROUP_CODE;
	}
	public String getR23_GROUP_NAME() {
		return R23_GROUP_NAME;
	}
	public void setR23_GROUP_NAME(String r23_GROUP_NAME) {
		R23_GROUP_NAME = r23_GROUP_NAME;
	}
	public BigDecimal getR23_CRM() {
		return R23_CRM;
	}
	public void setR23_CRM(BigDecimal r23_CRM) {
		R23_CRM = r23_CRM;
	}
	public BigDecimal getR23_NFBLT() {
		return R23_NFBLT;
	}
	public void setR23_NFBLT(BigDecimal r23_NFBLT) {
		R23_NFBLT = r23_NFBLT;
	}
	public BigDecimal getR23_NFBOS() {
		return R23_NFBOS;
	}
	public void setR23_NFBOS(BigDecimal r23_NFBOS) {
		R23_NFBOS = r23_NFBOS;
	}
	public BigDecimal getR23_CRM_2() {
		return R23_CRM_2;
	}
	public void setR23_CRM_2(BigDecimal r23_CRM_2) {
		R23_CRM_2 = r23_CRM_2;
	}
	public BigDecimal getR23_NFB() {
		return R23_NFB;
	}
	public void setR23_NFB(BigDecimal r23_NFB) {
		R23_NFB = r23_NFB;
	}
	public String getR23_BOND() {
		return R23_BOND;
	}
	public void setR23_BOND(String r23_BOND) {
		R23_BOND = r23_BOND;
	}
	public String getR23_CP() {
		return R23_CP;
	}
	public void setR23_CP(String r23_CP) {
		R23_CP = r23_CP;
	}
	public String getR23_EQULITY() {
		return R23_EQULITY;
	}
	public void setR23_EQULITY(String r23_EQULITY) {
		R23_EQULITY = r23_EQULITY;
	}
	public String getR23_FOREX() {
		return R23_FOREX;
	}
	public void setR23_FOREX(String r23_FOREX) {
		R23_FOREX = r23_FOREX;
	}
	public String getR23_OTHERS() {
		return R23_OTHERS;
	}
	public void setR23_OTHERS(String r23_OTHERS) {
		R23_OTHERS = r23_OTHERS;
	}
	public String getR23_INT_BANK() {
		return R23_INT_BANK;
	}
	public void setR23_INT_BANK(String r23_INT_BANK) {
		R23_INT_BANK = r23_INT_BANK;
	}
	public String getR23_DERIVATIVE() {
		return R23_DERIVATIVE;
	}
	public void setR23_DERIVATIVE(String r23_DERIVATIVE) {
		R23_DERIVATIVE = r23_DERIVATIVE;
	}
	public String getR24_GROUP_CODE() {
		return R24_GROUP_CODE;
	}
	public void setR24_GROUP_CODE(String r24_GROUP_CODE) {
		R24_GROUP_CODE = r24_GROUP_CODE;
	}
	public String getR24_GROUP_NAME() {
		return R24_GROUP_NAME;
	}
	public void setR24_GROUP_NAME(String r24_GROUP_NAME) {
		R24_GROUP_NAME = r24_GROUP_NAME;
	}
	public BigDecimal getR24_CRM() {
		return R24_CRM;
	}
	public void setR24_CRM(BigDecimal r24_CRM) {
		R24_CRM = r24_CRM;
	}
	public BigDecimal getR24_NFBLT() {
		return R24_NFBLT;
	}
	public void setR24_NFBLT(BigDecimal r24_NFBLT) {
		R24_NFBLT = r24_NFBLT;
	}
	public BigDecimal getR24_NFBOS() {
		return R24_NFBOS;
	}
	public void setR24_NFBOS(BigDecimal r24_NFBOS) {
		R24_NFBOS = r24_NFBOS;
	}
	public BigDecimal getR24_CRM_2() {
		return R24_CRM_2;
	}
	public void setR24_CRM_2(BigDecimal r24_CRM_2) {
		R24_CRM_2 = r24_CRM_2;
	}
	public BigDecimal getR24_NFB() {
		return R24_NFB;
	}
	public void setR24_NFB(BigDecimal r24_NFB) {
		R24_NFB = r24_NFB;
	}
	public String getR24_BOND() {
		return R24_BOND;
	}
	public void setR24_BOND(String r24_BOND) {
		R24_BOND = r24_BOND;
	}
	public String getR24_CP() {
		return R24_CP;
	}
	public void setR24_CP(String r24_CP) {
		R24_CP = r24_CP;
	}
	public String getR24_EQULITY() {
		return R24_EQULITY;
	}
	public void setR24_EQULITY(String r24_EQULITY) {
		R24_EQULITY = r24_EQULITY;
	}
	public String getR24_FOREX() {
		return R24_FOREX;
	}
	public void setR24_FOREX(String r24_FOREX) {
		R24_FOREX = r24_FOREX;
	}
	public String getR24_OTHERS() {
		return R24_OTHERS;
	}
	public void setR24_OTHERS(String r24_OTHERS) {
		R24_OTHERS = r24_OTHERS;
	}
	public String getR24_INT_BANK() {
		return R24_INT_BANK;
	}
	public void setR24_INT_BANK(String r24_INT_BANK) {
		R24_INT_BANK = r24_INT_BANK;
	}
	public String getR24_DERIVATIVE() {
		return R24_DERIVATIVE;
	}
	public void setR24_DERIVATIVE(String r24_DERIVATIVE) {
		R24_DERIVATIVE = r24_DERIVATIVE;
	}
	public String getR25_GROUP_CODE() {
		return R25_GROUP_CODE;
	}
	public void setR25_GROUP_CODE(String r25_GROUP_CODE) {
		R25_GROUP_CODE = r25_GROUP_CODE;
	}
	public String getR25_GROUP_NAME() {
		return R25_GROUP_NAME;
	}
	public void setR25_GROUP_NAME(String r25_GROUP_NAME) {
		R25_GROUP_NAME = r25_GROUP_NAME;
	}
	public BigDecimal getR25_CRM() {
		return R25_CRM;
	}
	public void setR25_CRM(BigDecimal r25_CRM) {
		R25_CRM = r25_CRM;
	}
	public BigDecimal getR25_NFBLT() {
		return R25_NFBLT;
	}
	public void setR25_NFBLT(BigDecimal r25_NFBLT) {
		R25_NFBLT = r25_NFBLT;
	}
	public BigDecimal getR25_NFBOS() {
		return R25_NFBOS;
	}
	public void setR25_NFBOS(BigDecimal r25_NFBOS) {
		R25_NFBOS = r25_NFBOS;
	}
	public BigDecimal getR25_CRM_2() {
		return R25_CRM_2;
	}
	public void setR25_CRM_2(BigDecimal r25_CRM_2) {
		R25_CRM_2 = r25_CRM_2;
	}
	public BigDecimal getR25_NFB() {
		return R25_NFB;
	}
	public void setR25_NFB(BigDecimal r25_NFB) {
		R25_NFB = r25_NFB;
	}
	public String getR25_BOND() {
		return R25_BOND;
	}
	public void setR25_BOND(String r25_BOND) {
		R25_BOND = r25_BOND;
	}
	public String getR25_CP() {
		return R25_CP;
	}
	public void setR25_CP(String r25_CP) {
		R25_CP = r25_CP;
	}
	public String getR25_EQULITY() {
		return R25_EQULITY;
	}
	public void setR25_EQULITY(String r25_EQULITY) {
		R25_EQULITY = r25_EQULITY;
	}
	public String getR25_FOREX() {
		return R25_FOREX;
	}
	public void setR25_FOREX(String r25_FOREX) {
		R25_FOREX = r25_FOREX;
	}
	public String getR25_OTHERS() {
		return R25_OTHERS;
	}
	public void setR25_OTHERS(String r25_OTHERS) {
		R25_OTHERS = r25_OTHERS;
	}
	public String getR25_INT_BANK() {
		return R25_INT_BANK;
	}
	public void setR25_INT_BANK(String r25_INT_BANK) {
		R25_INT_BANK = r25_INT_BANK;
	}
	public String getR25_DERIVATIVE() {
		return R25_DERIVATIVE;
	}
	public void setR25_DERIVATIVE(String r25_DERIVATIVE) {
		R25_DERIVATIVE = r25_DERIVATIVE;
	}
	public String getR26_GROUP_CODE() {
		return R26_GROUP_CODE;
	}
	public void setR26_GROUP_CODE(String r26_GROUP_CODE) {
		R26_GROUP_CODE = r26_GROUP_CODE;
	}
	public String getR26_GROUP_NAME() {
		return R26_GROUP_NAME;
	}
	public void setR26_GROUP_NAME(String r26_GROUP_NAME) {
		R26_GROUP_NAME = r26_GROUP_NAME;
	}
	public BigDecimal getR26_CRM() {
		return R26_CRM;
	}
	public void setR26_CRM(BigDecimal r26_CRM) {
		R26_CRM = r26_CRM;
	}
	public BigDecimal getR26_NFBLT() {
		return R26_NFBLT;
	}
	public void setR26_NFBLT(BigDecimal r26_NFBLT) {
		R26_NFBLT = r26_NFBLT;
	}
	public BigDecimal getR26_NFBOS() {
		return R26_NFBOS;
	}
	public void setR26_NFBOS(BigDecimal r26_NFBOS) {
		R26_NFBOS = r26_NFBOS;
	}
	public BigDecimal getR26_CRM_2() {
		return R26_CRM_2;
	}
	public void setR26_CRM_2(BigDecimal r26_CRM_2) {
		R26_CRM_2 = r26_CRM_2;
	}
	public BigDecimal getR26_NFB() {
		return R26_NFB;
	}
	public void setR26_NFB(BigDecimal r26_NFB) {
		R26_NFB = r26_NFB;
	}
	public String getR26_BOND() {
		return R26_BOND;
	}
	public void setR26_BOND(String r26_BOND) {
		R26_BOND = r26_BOND;
	}
	public String getR26_CP() {
		return R26_CP;
	}
	public void setR26_CP(String r26_CP) {
		R26_CP = r26_CP;
	}
	public String getR26_EQULITY() {
		return R26_EQULITY;
	}
	public void setR26_EQULITY(String r26_EQULITY) {
		R26_EQULITY = r26_EQULITY;
	}
	public String getR26_FOREX() {
		return R26_FOREX;
	}
	public void setR26_FOREX(String r26_FOREX) {
		R26_FOREX = r26_FOREX;
	}
	public String getR26_OTHERS() {
		return R26_OTHERS;
	}
	public void setR26_OTHERS(String r26_OTHERS) {
		R26_OTHERS = r26_OTHERS;
	}
	public String getR26_INT_BANK() {
		return R26_INT_BANK;
	}
	public void setR26_INT_BANK(String r26_INT_BANK) {
		R26_INT_BANK = r26_INT_BANK;
	}
	public String getR26_DERIVATIVE() {
		return R26_DERIVATIVE;
	}
	public void setR26_DERIVATIVE(String r26_DERIVATIVE) {
		R26_DERIVATIVE = r26_DERIVATIVE;
	}
	public String getR27_GROUP_CODE() {
		return R27_GROUP_CODE;
	}
	public void setR27_GROUP_CODE(String r27_GROUP_CODE) {
		R27_GROUP_CODE = r27_GROUP_CODE;
	}
	public String getR27_GROUP_NAME() {
		return R27_GROUP_NAME;
	}
	public void setR27_GROUP_NAME(String r27_GROUP_NAME) {
		R27_GROUP_NAME = r27_GROUP_NAME;
	}
	public BigDecimal getR27_CRM() {
		return R27_CRM;
	}
	public void setR27_CRM(BigDecimal r27_CRM) {
		R27_CRM = r27_CRM;
	}
	public BigDecimal getR27_NFBLT() {
		return R27_NFBLT;
	}
	public void setR27_NFBLT(BigDecimal r27_NFBLT) {
		R27_NFBLT = r27_NFBLT;
	}
	public BigDecimal getR27_NFBOS() {
		return R27_NFBOS;
	}
	public void setR27_NFBOS(BigDecimal r27_NFBOS) {
		R27_NFBOS = r27_NFBOS;
	}
	public BigDecimal getR27_CRM_2() {
		return R27_CRM_2;
	}
	public void setR27_CRM_2(BigDecimal r27_CRM_2) {
		R27_CRM_2 = r27_CRM_2;
	}
	public BigDecimal getR27_NFB() {
		return R27_NFB;
	}
	public void setR27_NFB(BigDecimal r27_NFB) {
		R27_NFB = r27_NFB;
	}
	public String getR27_BOND() {
		return R27_BOND;
	}
	public void setR27_BOND(String r27_BOND) {
		R27_BOND = r27_BOND;
	}
	public String getR27_CP() {
		return R27_CP;
	}
	public void setR27_CP(String r27_CP) {
		R27_CP = r27_CP;
	}
	public String getR27_EQULITY() {
		return R27_EQULITY;
	}
	public void setR27_EQULITY(String r27_EQULITY) {
		R27_EQULITY = r27_EQULITY;
	}
	public String getR27_FOREX() {
		return R27_FOREX;
	}
	public void setR27_FOREX(String r27_FOREX) {
		R27_FOREX = r27_FOREX;
	}
	public String getR27_OTHERS() {
		return R27_OTHERS;
	}
	public void setR27_OTHERS(String r27_OTHERS) {
		R27_OTHERS = r27_OTHERS;
	}
	public String getR27_INT_BANK() {
		return R27_INT_BANK;
	}
	public void setR27_INT_BANK(String r27_INT_BANK) {
		R27_INT_BANK = r27_INT_BANK;
	}
	public String getR27_DERIVATIVE() {
		return R27_DERIVATIVE;
	}
	public void setR27_DERIVATIVE(String r27_DERIVATIVE) {
		R27_DERIVATIVE = r27_DERIVATIVE;
	}
	public String getR28_GROUP_CODE() {
		return R28_GROUP_CODE;
	}
	public void setR28_GROUP_CODE(String r28_GROUP_CODE) {
		R28_GROUP_CODE = r28_GROUP_CODE;
	}
	public String getR28_GROUP_NAME() {
		return R28_GROUP_NAME;
	}
	public void setR28_GROUP_NAME(String r28_GROUP_NAME) {
		R28_GROUP_NAME = r28_GROUP_NAME;
	}
	public BigDecimal getR28_CRM() {
		return R28_CRM;
	}
	public void setR28_CRM(BigDecimal r28_CRM) {
		R28_CRM = r28_CRM;
	}
	public BigDecimal getR28_NFBLT() {
		return R28_NFBLT;
	}
	public void setR28_NFBLT(BigDecimal r28_NFBLT) {
		R28_NFBLT = r28_NFBLT;
	}
	public BigDecimal getR28_NFBOS() {
		return R28_NFBOS;
	}
	public void setR28_NFBOS(BigDecimal r28_NFBOS) {
		R28_NFBOS = r28_NFBOS;
	}
	public BigDecimal getR28_CRM_2() {
		return R28_CRM_2;
	}
	public void setR28_CRM_2(BigDecimal r28_CRM_2) {
		R28_CRM_2 = r28_CRM_2;
	}
	public BigDecimal getR28_NFB() {
		return R28_NFB;
	}
	public void setR28_NFB(BigDecimal r28_NFB) {
		R28_NFB = r28_NFB;
	}
	public String getR28_BOND() {
		return R28_BOND;
	}
	public void setR28_BOND(String r28_BOND) {
		R28_BOND = r28_BOND;
	}
	public String getR28_CP() {
		return R28_CP;
	}
	public void setR28_CP(String r28_CP) {
		R28_CP = r28_CP;
	}
	public String getR28_EQULITY() {
		return R28_EQULITY;
	}
	public void setR28_EQULITY(String r28_EQULITY) {
		R28_EQULITY = r28_EQULITY;
	}
	public String getR28_FOREX() {
		return R28_FOREX;
	}
	public void setR28_FOREX(String r28_FOREX) {
		R28_FOREX = r28_FOREX;
	}
	public String getR28_OTHERS() {
		return R28_OTHERS;
	}
	public void setR28_OTHERS(String r28_OTHERS) {
		R28_OTHERS = r28_OTHERS;
	}
	public String getR28_INT_BANK() {
		return R28_INT_BANK;
	}
	public void setR28_INT_BANK(String r28_INT_BANK) {
		R28_INT_BANK = r28_INT_BANK;
	}
	public String getR28_DERIVATIVE() {
		return R28_DERIVATIVE;
	}
	public void setR28_DERIVATIVE(String r28_DERIVATIVE) {
		R28_DERIVATIVE = r28_DERIVATIVE;
	}
	public String getR29_GROUP_CODE() {
		return R29_GROUP_CODE;
	}
	public void setR29_GROUP_CODE(String r29_GROUP_CODE) {
		R29_GROUP_CODE = r29_GROUP_CODE;
	}
	public String getR29_GROUP_NAME() {
		return R29_GROUP_NAME;
	}
	public void setR29_GROUP_NAME(String r29_GROUP_NAME) {
		R29_GROUP_NAME = r29_GROUP_NAME;
	}
	public BigDecimal getR29_CRM() {
		return R29_CRM;
	}
	public void setR29_CRM(BigDecimal r29_CRM) {
		R29_CRM = r29_CRM;
	}
	public BigDecimal getR29_NFBLT() {
		return R29_NFBLT;
	}
	public void setR29_NFBLT(BigDecimal r29_NFBLT) {
		R29_NFBLT = r29_NFBLT;
	}
	public BigDecimal getR29_NFBOS() {
		return R29_NFBOS;
	}
	public void setR29_NFBOS(BigDecimal r29_NFBOS) {
		R29_NFBOS = r29_NFBOS;
	}
	public BigDecimal getR29_CRM_2() {
		return R29_CRM_2;
	}
	public void setR29_CRM_2(BigDecimal r29_CRM_2) {
		R29_CRM_2 = r29_CRM_2;
	}
	public BigDecimal getR29_NFB() {
		return R29_NFB;
	}
	public void setR29_NFB(BigDecimal r29_NFB) {
		R29_NFB = r29_NFB;
	}
	public String getR29_BOND() {
		return R29_BOND;
	}
	public void setR29_BOND(String r29_BOND) {
		R29_BOND = r29_BOND;
	}
	public String getR29_CP() {
		return R29_CP;
	}
	public void setR29_CP(String r29_CP) {
		R29_CP = r29_CP;
	}
	public String getR29_EQULITY() {
		return R29_EQULITY;
	}
	public void setR29_EQULITY(String r29_EQULITY) {
		R29_EQULITY = r29_EQULITY;
	}
	public String getR29_FOREX() {
		return R29_FOREX;
	}
	public void setR29_FOREX(String r29_FOREX) {
		R29_FOREX = r29_FOREX;
	}
	public String getR29_OTHERS() {
		return R29_OTHERS;
	}
	public void setR29_OTHERS(String r29_OTHERS) {
		R29_OTHERS = r29_OTHERS;
	}
	public String getR29_INT_BANK() {
		return R29_INT_BANK;
	}
	public void setR29_INT_BANK(String r29_INT_BANK) {
		R29_INT_BANK = r29_INT_BANK;
	}
	public String getR29_DERIVATIVE() {
		return R29_DERIVATIVE;
	}
	public void setR29_DERIVATIVE(String r29_DERIVATIVE) {
		R29_DERIVATIVE = r29_DERIVATIVE;
	}
	public String getR30_GROUP_CODE() {
		return R30_GROUP_CODE;
	}
	public void setR30_GROUP_CODE(String r30_GROUP_CODE) {
		R30_GROUP_CODE = r30_GROUP_CODE;
	}
	public String getR30_GROUP_NAME() {
		return R30_GROUP_NAME;
	}
	public void setR30_GROUP_NAME(String r30_GROUP_NAME) {
		R30_GROUP_NAME = r30_GROUP_NAME;
	}
	public BigDecimal getR30_CRM() {
		return R30_CRM;
	}
	public void setR30_CRM(BigDecimal r30_CRM) {
		R30_CRM = r30_CRM;
	}
	public BigDecimal getR30_NFBLT() {
		return R30_NFBLT;
	}
	public void setR30_NFBLT(BigDecimal r30_NFBLT) {
		R30_NFBLT = r30_NFBLT;
	}
	public BigDecimal getR30_NFBOS() {
		return R30_NFBOS;
	}
	public void setR30_NFBOS(BigDecimal r30_NFBOS) {
		R30_NFBOS = r30_NFBOS;
	}
	public BigDecimal getR30_CRM_2() {
		return R30_CRM_2;
	}
	public void setR30_CRM_2(BigDecimal r30_CRM_2) {
		R30_CRM_2 = r30_CRM_2;
	}
	public BigDecimal getR30_NFB() {
		return R30_NFB;
	}
	public void setR30_NFB(BigDecimal r30_NFB) {
		R30_NFB = r30_NFB;
	}
	public String getR30_BOND() {
		return R30_BOND;
	}
	public void setR30_BOND(String r30_BOND) {
		R30_BOND = r30_BOND;
	}
	public String getR30_CP() {
		return R30_CP;
	}
	public void setR30_CP(String r30_CP) {
		R30_CP = r30_CP;
	}
	public String getR30_EQULITY() {
		return R30_EQULITY;
	}
	public void setR30_EQULITY(String r30_EQULITY) {
		R30_EQULITY = r30_EQULITY;
	}
	public String getR30_FOREX() {
		return R30_FOREX;
	}
	public void setR30_FOREX(String r30_FOREX) {
		R30_FOREX = r30_FOREX;
	}
	public String getR30_OTHERS() {
		return R30_OTHERS;
	}
	public void setR30_OTHERS(String r30_OTHERS) {
		R30_OTHERS = r30_OTHERS;
	}
	public String getR30_INT_BANK() {
		return R30_INT_BANK;
	}
	public void setR30_INT_BANK(String r30_INT_BANK) {
		R30_INT_BANK = r30_INT_BANK;
	}
	public String getR30_DERIVATIVE() {
		return R30_DERIVATIVE;
	}
	public void setR30_DERIVATIVE(String r30_DERIVATIVE) {
		R30_DERIVATIVE = r30_DERIVATIVE;
	}
	public String getR31_GROUP_CODE() {
		return R31_GROUP_CODE;
	}
	public void setR31_GROUP_CODE(String r31_GROUP_CODE) {
		R31_GROUP_CODE = r31_GROUP_CODE;
	}
	public String getR31_GROUP_NAME() {
		return R31_GROUP_NAME;
	}
	public void setR31_GROUP_NAME(String r31_GROUP_NAME) {
		R31_GROUP_NAME = r31_GROUP_NAME;
	}
	public BigDecimal getR31_CRM() {
		return R31_CRM;
	}
	public void setR31_CRM(BigDecimal r31_CRM) {
		R31_CRM = r31_CRM;
	}
	public BigDecimal getR31_NFBLT() {
		return R31_NFBLT;
	}
	public void setR31_NFBLT(BigDecimal r31_NFBLT) {
		R31_NFBLT = r31_NFBLT;
	}
	public BigDecimal getR31_NFBOS() {
		return R31_NFBOS;
	}
	public void setR31_NFBOS(BigDecimal r31_NFBOS) {
		R31_NFBOS = r31_NFBOS;
	}
	public BigDecimal getR31_CRM_2() {
		return R31_CRM_2;
	}
	public void setR31_CRM_2(BigDecimal r31_CRM_2) {
		R31_CRM_2 = r31_CRM_2;
	}
	public BigDecimal getR31_NFB() {
		return R31_NFB;
	}
	public void setR31_NFB(BigDecimal r31_NFB) {
		R31_NFB = r31_NFB;
	}
	public String getR31_BOND() {
		return R31_BOND;
	}
	public void setR31_BOND(String r31_BOND) {
		R31_BOND = r31_BOND;
	}
	public String getR31_CP() {
		return R31_CP;
	}
	public void setR31_CP(String r31_CP) {
		R31_CP = r31_CP;
	}
	public String getR31_EQULITY() {
		return R31_EQULITY;
	}
	public void setR31_EQULITY(String r31_EQULITY) {
		R31_EQULITY = r31_EQULITY;
	}
	public String getR31_FOREX() {
		return R31_FOREX;
	}
	public void setR31_FOREX(String r31_FOREX) {
		R31_FOREX = r31_FOREX;
	}
	public String getR31_OTHERS() {
		return R31_OTHERS;
	}
	public void setR31_OTHERS(String r31_OTHERS) {
		R31_OTHERS = r31_OTHERS;
	}
	public String getR31_INT_BANK() {
		return R31_INT_BANK;
	}
	public void setR31_INT_BANK(String r31_INT_BANK) {
		R31_INT_BANK = r31_INT_BANK;
	}
	public String getR31_DERIVATIVE() {
		return R31_DERIVATIVE;
	}
	public void setR31_DERIVATIVE(String r31_DERIVATIVE) {
		R31_DERIVATIVE = r31_DERIVATIVE;
	}
	public String getR32_GROUP_CODE() {
		return R32_GROUP_CODE;
	}
	public void setR32_GROUP_CODE(String r32_GROUP_CODE) {
		R32_GROUP_CODE = r32_GROUP_CODE;
	}
	public String getR32_GROUP_NAME() {
		return R32_GROUP_NAME;
	}
	public void setR32_GROUP_NAME(String r32_GROUP_NAME) {
		R32_GROUP_NAME = r32_GROUP_NAME;
	}
	public BigDecimal getR32_CRM() {
		return R32_CRM;
	}
	public void setR32_CRM(BigDecimal r32_CRM) {
		R32_CRM = r32_CRM;
	}
	public BigDecimal getR32_NFBLT() {
		return R32_NFBLT;
	}
	public void setR32_NFBLT(BigDecimal r32_NFBLT) {
		R32_NFBLT = r32_NFBLT;
	}
	public BigDecimal getR32_NFBOS() {
		return R32_NFBOS;
	}
	public void setR32_NFBOS(BigDecimal r32_NFBOS) {
		R32_NFBOS = r32_NFBOS;
	}
	public BigDecimal getR32_CRM_2() {
		return R32_CRM_2;
	}
	public void setR32_CRM_2(BigDecimal r32_CRM_2) {
		R32_CRM_2 = r32_CRM_2;
	}
	public BigDecimal getR32_NFB() {
		return R32_NFB;
	}
	public void setR32_NFB(BigDecimal r32_NFB) {
		R32_NFB = r32_NFB;
	}
	public String getR32_BOND() {
		return R32_BOND;
	}
	public void setR32_BOND(String r32_BOND) {
		R32_BOND = r32_BOND;
	}
	public String getR32_CP() {
		return R32_CP;
	}
	public void setR32_CP(String r32_CP) {
		R32_CP = r32_CP;
	}
	public String getR32_EQULITY() {
		return R32_EQULITY;
	}
	public void setR32_EQULITY(String r32_EQULITY) {
		R32_EQULITY = r32_EQULITY;
	}
	public String getR32_FOREX() {
		return R32_FOREX;
	}
	public void setR32_FOREX(String r32_FOREX) {
		R32_FOREX = r32_FOREX;
	}
	public String getR32_OTHERS() {
		return R32_OTHERS;
	}
	public void setR32_OTHERS(String r32_OTHERS) {
		R32_OTHERS = r32_OTHERS;
	}
	public String getR32_INT_BANK() {
		return R32_INT_BANK;
	}
	public void setR32_INT_BANK(String r32_INT_BANK) {
		R32_INT_BANK = r32_INT_BANK;
	}
	public String getR32_DERIVATIVE() {
		return R32_DERIVATIVE;
	}
	public void setR32_DERIVATIVE(String r32_DERIVATIVE) {
		R32_DERIVATIVE = r32_DERIVATIVE;
	}
	public String getR33_GROUP_CODE() {
		return R33_GROUP_CODE;
	}
	public void setR33_GROUP_CODE(String r33_GROUP_CODE) {
		R33_GROUP_CODE = r33_GROUP_CODE;
	}
	public String getR33_GROUP_NAME() {
		return R33_GROUP_NAME;
	}
	public void setR33_GROUP_NAME(String r33_GROUP_NAME) {
		R33_GROUP_NAME = r33_GROUP_NAME;
	}
	public BigDecimal getR33_CRM() {
		return R33_CRM;
	}
	public void setR33_CRM(BigDecimal r33_CRM) {
		R33_CRM = r33_CRM;
	}
	public BigDecimal getR33_NFBLT() {
		return R33_NFBLT;
	}
	public void setR33_NFBLT(BigDecimal r33_NFBLT) {
		R33_NFBLT = r33_NFBLT;
	}
	public BigDecimal getR33_NFBOS() {
		return R33_NFBOS;
	}
	public void setR33_NFBOS(BigDecimal r33_NFBOS) {
		R33_NFBOS = r33_NFBOS;
	}
	public BigDecimal getR33_CRM_2() {
		return R33_CRM_2;
	}
	public void setR33_CRM_2(BigDecimal r33_CRM_2) {
		R33_CRM_2 = r33_CRM_2;
	}
	public BigDecimal getR33_NFB() {
		return R33_NFB;
	}
	public void setR33_NFB(BigDecimal r33_NFB) {
		R33_NFB = r33_NFB;
	}
	public String getR33_BOND() {
		return R33_BOND;
	}
	public void setR33_BOND(String r33_BOND) {
		R33_BOND = r33_BOND;
	}
	public String getR33_CP() {
		return R33_CP;
	}
	public void setR33_CP(String r33_CP) {
		R33_CP = r33_CP;
	}
	public String getR33_EQULITY() {
		return R33_EQULITY;
	}
	public void setR33_EQULITY(String r33_EQULITY) {
		R33_EQULITY = r33_EQULITY;
	}
	public String getR33_FOREX() {
		return R33_FOREX;
	}
	public void setR33_FOREX(String r33_FOREX) {
		R33_FOREX = r33_FOREX;
	}
	public String getR33_OTHERS() {
		return R33_OTHERS;
	}
	public void setR33_OTHERS(String r33_OTHERS) {
		R33_OTHERS = r33_OTHERS;
	}
	public String getR33_INT_BANK() {
		return R33_INT_BANK;
	}
	public void setR33_INT_BANK(String r33_INT_BANK) {
		R33_INT_BANK = r33_INT_BANK;
	}
	public String getR33_DERIVATIVE() {
		return R33_DERIVATIVE;
	}
	public void setR33_DERIVATIVE(String r33_DERIVATIVE) {
		R33_DERIVATIVE = r33_DERIVATIVE;
	}
	public String getR34_GROUP_CODE() {
		return R34_GROUP_CODE;
	}
	public void setR34_GROUP_CODE(String r34_GROUP_CODE) {
		R34_GROUP_CODE = r34_GROUP_CODE;
	}
	public String getR34_GROUP_NAME() {
		return R34_GROUP_NAME;
	}
	public void setR34_GROUP_NAME(String r34_GROUP_NAME) {
		R34_GROUP_NAME = r34_GROUP_NAME;
	}
	public BigDecimal getR34_CRM() {
		return R34_CRM;
	}
	public void setR34_CRM(BigDecimal r34_CRM) {
		R34_CRM = r34_CRM;
	}
	public BigDecimal getR34_NFBLT() {
		return R34_NFBLT;
	}
	public void setR34_NFBLT(BigDecimal r34_NFBLT) {
		R34_NFBLT = r34_NFBLT;
	}
	public BigDecimal getR34_NFBOS() {
		return R34_NFBOS;
	}
	public void setR34_NFBOS(BigDecimal r34_NFBOS) {
		R34_NFBOS = r34_NFBOS;
	}
	public BigDecimal getR34_CRM_2() {
		return R34_CRM_2;
	}
	public void setR34_CRM_2(BigDecimal r34_CRM_2) {
		R34_CRM_2 = r34_CRM_2;
	}
	public BigDecimal getR34_NFB() {
		return R34_NFB;
	}
	public void setR34_NFB(BigDecimal r34_NFB) {
		R34_NFB = r34_NFB;
	}
	public String getR34_BOND() {
		return R34_BOND;
	}
	public void setR34_BOND(String r34_BOND) {
		R34_BOND = r34_BOND;
	}
	public String getR34_CP() {
		return R34_CP;
	}
	public void setR34_CP(String r34_CP) {
		R34_CP = r34_CP;
	}
	public String getR34_EQULITY() {
		return R34_EQULITY;
	}
	public void setR34_EQULITY(String r34_EQULITY) {
		R34_EQULITY = r34_EQULITY;
	}
	public String getR34_FOREX() {
		return R34_FOREX;
	}
	public void setR34_FOREX(String r34_FOREX) {
		R34_FOREX = r34_FOREX;
	}
	public String getR34_OTHERS() {
		return R34_OTHERS;
	}
	public void setR34_OTHERS(String r34_OTHERS) {
		R34_OTHERS = r34_OTHERS;
	}
	public String getR34_INT_BANK() {
		return R34_INT_BANK;
	}
	public void setR34_INT_BANK(String r34_INT_BANK) {
		R34_INT_BANK = r34_INT_BANK;
	}
	public String getR34_DERIVATIVE() {
		return R34_DERIVATIVE;
	}
	public void setR34_DERIVATIVE(String r34_DERIVATIVE) {
		R34_DERIVATIVE = r34_DERIVATIVE;
	}
	public String getR35_GROUP_CODE() {
		return R35_GROUP_CODE;
	}
	public void setR35_GROUP_CODE(String r35_GROUP_CODE) {
		R35_GROUP_CODE = r35_GROUP_CODE;
	}
	public String getR35_GROUP_NAME() {
		return R35_GROUP_NAME;
	}
	public void setR35_GROUP_NAME(String r35_GROUP_NAME) {
		R35_GROUP_NAME = r35_GROUP_NAME;
	}
	public BigDecimal getR35_CRM() {
		return R35_CRM;
	}
	public void setR35_CRM(BigDecimal r35_CRM) {
		R35_CRM = r35_CRM;
	}
	public BigDecimal getR35_NFBLT() {
		return R35_NFBLT;
	}
	public void setR35_NFBLT(BigDecimal r35_NFBLT) {
		R35_NFBLT = r35_NFBLT;
	}
	public BigDecimal getR35_NFBOS() {
		return R35_NFBOS;
	}
	public void setR35_NFBOS(BigDecimal r35_NFBOS) {
		R35_NFBOS = r35_NFBOS;
	}
	public BigDecimal getR35_CRM_2() {
		return R35_CRM_2;
	}
	public void setR35_CRM_2(BigDecimal r35_CRM_2) {
		R35_CRM_2 = r35_CRM_2;
	}
	public BigDecimal getR35_NFB() {
		return R35_NFB;
	}
	public void setR35_NFB(BigDecimal r35_NFB) {
		R35_NFB = r35_NFB;
	}
	public String getR35_BOND() {
		return R35_BOND;
	}
	public void setR35_BOND(String r35_BOND) {
		R35_BOND = r35_BOND;
	}
	public String getR35_CP() {
		return R35_CP;
	}
	public void setR35_CP(String r35_CP) {
		R35_CP = r35_CP;
	}
	public String getR35_EQULITY() {
		return R35_EQULITY;
	}
	public void setR35_EQULITY(String r35_EQULITY) {
		R35_EQULITY = r35_EQULITY;
	}
	public String getR35_FOREX() {
		return R35_FOREX;
	}
	public void setR35_FOREX(String r35_FOREX) {
		R35_FOREX = r35_FOREX;
	}
	public String getR35_OTHERS() {
		return R35_OTHERS;
	}
	public void setR35_OTHERS(String r35_OTHERS) {
		R35_OTHERS = r35_OTHERS;
	}
	public String getR35_INT_BANK() {
		return R35_INT_BANK;
	}
	public void setR35_INT_BANK(String r35_INT_BANK) {
		R35_INT_BANK = r35_INT_BANK;
	}
	public String getR35_DERIVATIVE() {
		return R35_DERIVATIVE;
	}
	public void setR35_DERIVATIVE(String r35_DERIVATIVE) {
		R35_DERIVATIVE = r35_DERIVATIVE;
	}
	public String getR36_GROUP_CODE() {
		return R36_GROUP_CODE;
	}
	public void setR36_GROUP_CODE(String r36_GROUP_CODE) {
		R36_GROUP_CODE = r36_GROUP_CODE;
	}
	public String getR36_GROUP_NAME() {
		return R36_GROUP_NAME;
	}
	public void setR36_GROUP_NAME(String r36_GROUP_NAME) {
		R36_GROUP_NAME = r36_GROUP_NAME;
	}
	public BigDecimal getR36_CRM() {
		return R36_CRM;
	}
	public void setR36_CRM(BigDecimal r36_CRM) {
		R36_CRM = r36_CRM;
	}
	public BigDecimal getR36_NFBLT() {
		return R36_NFBLT;
	}
	public void setR36_NFBLT(BigDecimal r36_NFBLT) {
		R36_NFBLT = r36_NFBLT;
	}
	public BigDecimal getR36_NFBOS() {
		return R36_NFBOS;
	}
	public void setR36_NFBOS(BigDecimal r36_NFBOS) {
		R36_NFBOS = r36_NFBOS;
	}
	public BigDecimal getR36_CRM_2() {
		return R36_CRM_2;
	}
	public void setR36_CRM_2(BigDecimal r36_CRM_2) {
		R36_CRM_2 = r36_CRM_2;
	}
	public BigDecimal getR36_NFB() {
		return R36_NFB;
	}
	public void setR36_NFB(BigDecimal r36_NFB) {
		R36_NFB = r36_NFB;
	}
	public String getR36_BOND() {
		return R36_BOND;
	}
	public void setR36_BOND(String r36_BOND) {
		R36_BOND = r36_BOND;
	}
	public String getR36_CP() {
		return R36_CP;
	}
	public void setR36_CP(String r36_CP) {
		R36_CP = r36_CP;
	}
	public String getR36_EQULITY() {
		return R36_EQULITY;
	}
	public void setR36_EQULITY(String r36_EQULITY) {
		R36_EQULITY = r36_EQULITY;
	}
	public String getR36_FOREX() {
		return R36_FOREX;
	}
	public void setR36_FOREX(String r36_FOREX) {
		R36_FOREX = r36_FOREX;
	}
	public String getR36_OTHERS() {
		return R36_OTHERS;
	}
	public void setR36_OTHERS(String r36_OTHERS) {
		R36_OTHERS = r36_OTHERS;
	}
	public String getR36_INT_BANK() {
		return R36_INT_BANK;
	}
	public void setR36_INT_BANK(String r36_INT_BANK) {
		R36_INT_BANK = r36_INT_BANK;
	}
	public String getR36_DERIVATIVE() {
		return R36_DERIVATIVE;
	}
	public void setR36_DERIVATIVE(String r36_DERIVATIVE) {
		R36_DERIVATIVE = r36_DERIVATIVE;
	}
	public String getR37_GROUP_CODE() {
		return R37_GROUP_CODE;
	}
	public void setR37_GROUP_CODE(String r37_GROUP_CODE) {
		R37_GROUP_CODE = r37_GROUP_CODE;
	}
	public String getR37_GROUP_NAME() {
		return R37_GROUP_NAME;
	}
	public void setR37_GROUP_NAME(String r37_GROUP_NAME) {
		R37_GROUP_NAME = r37_GROUP_NAME;
	}
	public BigDecimal getR37_CRM() {
		return R37_CRM;
	}
	public void setR37_CRM(BigDecimal r37_CRM) {
		R37_CRM = r37_CRM;
	}
	public BigDecimal getR37_NFBLT() {
		return R37_NFBLT;
	}
	public void setR37_NFBLT(BigDecimal r37_NFBLT) {
		R37_NFBLT = r37_NFBLT;
	}
	public BigDecimal getR37_NFBOS() {
		return R37_NFBOS;
	}
	public void setR37_NFBOS(BigDecimal r37_NFBOS) {
		R37_NFBOS = r37_NFBOS;
	}
	public BigDecimal getR37_CRM_2() {
		return R37_CRM_2;
	}
	public void setR37_CRM_2(BigDecimal r37_CRM_2) {
		R37_CRM_2 = r37_CRM_2;
	}
	public BigDecimal getR37_NFB() {
		return R37_NFB;
	}
	public void setR37_NFB(BigDecimal r37_NFB) {
		R37_NFB = r37_NFB;
	}
	public String getR37_BOND() {
		return R37_BOND;
	}
	public void setR37_BOND(String r37_BOND) {
		R37_BOND = r37_BOND;
	}
	public String getR37_CP() {
		return R37_CP;
	}
	public void setR37_CP(String r37_CP) {
		R37_CP = r37_CP;
	}
	public String getR37_EQULITY() {
		return R37_EQULITY;
	}
	public void setR37_EQULITY(String r37_EQULITY) {
		R37_EQULITY = r37_EQULITY;
	}
	public String getR37_FOREX() {
		return R37_FOREX;
	}
	public void setR37_FOREX(String r37_FOREX) {
		R37_FOREX = r37_FOREX;
	}
	public String getR37_OTHERS() {
		return R37_OTHERS;
	}
	public void setR37_OTHERS(String r37_OTHERS) {
		R37_OTHERS = r37_OTHERS;
	}
	public String getR37_INT_BANK() {
		return R37_INT_BANK;
	}
	public void setR37_INT_BANK(String r37_INT_BANK) {
		R37_INT_BANK = r37_INT_BANK;
	}
	public String getR37_DERIVATIVE() {
		return R37_DERIVATIVE;
	}
	public void setR37_DERIVATIVE(String r37_DERIVATIVE) {
		R37_DERIVATIVE = r37_DERIVATIVE;
	}
	public String getR38_GROUP_CODE() {
		return R38_GROUP_CODE;
	}
	public void setR38_GROUP_CODE(String r38_GROUP_CODE) {
		R38_GROUP_CODE = r38_GROUP_CODE;
	}
	public String getR38_GROUP_NAME() {
		return R38_GROUP_NAME;
	}
	public void setR38_GROUP_NAME(String r38_GROUP_NAME) {
		R38_GROUP_NAME = r38_GROUP_NAME;
	}
	public BigDecimal getR38_CRM() {
		return R38_CRM;
	}
	public void setR38_CRM(BigDecimal r38_CRM) {
		R38_CRM = r38_CRM;
	}
	public BigDecimal getR38_NFBLT() {
		return R38_NFBLT;
	}
	public void setR38_NFBLT(BigDecimal r38_NFBLT) {
		R38_NFBLT = r38_NFBLT;
	}
	public BigDecimal getR38_NFBOS() {
		return R38_NFBOS;
	}
	public void setR38_NFBOS(BigDecimal r38_NFBOS) {
		R38_NFBOS = r38_NFBOS;
	}
	public BigDecimal getR38_CRM_2() {
		return R38_CRM_2;
	}
	public void setR38_CRM_2(BigDecimal r38_CRM_2) {
		R38_CRM_2 = r38_CRM_2;
	}
	public BigDecimal getR38_NFB() {
		return R38_NFB;
	}
	public void setR38_NFB(BigDecimal r38_NFB) {
		R38_NFB = r38_NFB;
	}
	public String getR38_BOND() {
		return R38_BOND;
	}
	public void setR38_BOND(String r38_BOND) {
		R38_BOND = r38_BOND;
	}
	public String getR38_CP() {
		return R38_CP;
	}
	public void setR38_CP(String r38_CP) {
		R38_CP = r38_CP;
	}
	public String getR38_EQULITY() {
		return R38_EQULITY;
	}
	public void setR38_EQULITY(String r38_EQULITY) {
		R38_EQULITY = r38_EQULITY;
	}
	public String getR38_FOREX() {
		return R38_FOREX;
	}
	public void setR38_FOREX(String r38_FOREX) {
		R38_FOREX = r38_FOREX;
	}
	public String getR38_OTHERS() {
		return R38_OTHERS;
	}
	public void setR38_OTHERS(String r38_OTHERS) {
		R38_OTHERS = r38_OTHERS;
	}
	public String getR38_INT_BANK() {
		return R38_INT_BANK;
	}
	public void setR38_INT_BANK(String r38_INT_BANK) {
		R38_INT_BANK = r38_INT_BANK;
	}
	public String getR38_DERIVATIVE() {
		return R38_DERIVATIVE;
	}
	public void setR38_DERIVATIVE(String r38_DERIVATIVE) {
		R38_DERIVATIVE = r38_DERIVATIVE;
	}
	public String getR39_GROUP_CODE() {
		return R39_GROUP_CODE;
	}
	public void setR39_GROUP_CODE(String r39_GROUP_CODE) {
		R39_GROUP_CODE = r39_GROUP_CODE;
	}
	public String getR39_GROUP_NAME() {
		return R39_GROUP_NAME;
	}
	public void setR39_GROUP_NAME(String r39_GROUP_NAME) {
		R39_GROUP_NAME = r39_GROUP_NAME;
	}
	public BigDecimal getR39_CRM() {
		return R39_CRM;
	}
	public void setR39_CRM(BigDecimal r39_CRM) {
		R39_CRM = r39_CRM;
	}
	public BigDecimal getR39_NFBLT() {
		return R39_NFBLT;
	}
	public void setR39_NFBLT(BigDecimal r39_NFBLT) {
		R39_NFBLT = r39_NFBLT;
	}
	public BigDecimal getR39_NFBOS() {
		return R39_NFBOS;
	}
	public void setR39_NFBOS(BigDecimal r39_NFBOS) {
		R39_NFBOS = r39_NFBOS;
	}
	public BigDecimal getR39_CRM_2() {
		return R39_CRM_2;
	}
	public void setR39_CRM_2(BigDecimal r39_CRM_2) {
		R39_CRM_2 = r39_CRM_2;
	}
	public BigDecimal getR39_NFB() {
		return R39_NFB;
	}
	public void setR39_NFB(BigDecimal r39_NFB) {
		R39_NFB = r39_NFB;
	}
	public String getR39_BOND() {
		return R39_BOND;
	}
	public void setR39_BOND(String r39_BOND) {
		R39_BOND = r39_BOND;
	}
	public String getR39_CP() {
		return R39_CP;
	}
	public void setR39_CP(String r39_CP) {
		R39_CP = r39_CP;
	}
	public String getR39_EQULITY() {
		return R39_EQULITY;
	}
	public void setR39_EQULITY(String r39_EQULITY) {
		R39_EQULITY = r39_EQULITY;
	}
	public String getR39_FOREX() {
		return R39_FOREX;
	}
	public void setR39_FOREX(String r39_FOREX) {
		R39_FOREX = r39_FOREX;
	}
	public String getR39_OTHERS() {
		return R39_OTHERS;
	}
	public void setR39_OTHERS(String r39_OTHERS) {
		R39_OTHERS = r39_OTHERS;
	}
	public String getR39_INT_BANK() {
		return R39_INT_BANK;
	}
	public void setR39_INT_BANK(String r39_INT_BANK) {
		R39_INT_BANK = r39_INT_BANK;
	}
	public String getR39_DERIVATIVE() {
		return R39_DERIVATIVE;
	}
	public void setR39_DERIVATIVE(String r39_DERIVATIVE) {
		R39_DERIVATIVE = r39_DERIVATIVE;
	}
	public String getR40_GROUP_CODE() {
		return R40_GROUP_CODE;
	}
	public void setR40_GROUP_CODE(String r40_GROUP_CODE) {
		R40_GROUP_CODE = r40_GROUP_CODE;
	}
	public String getR40_GROUP_NAME() {
		return R40_GROUP_NAME;
	}
	public void setR40_GROUP_NAME(String r40_GROUP_NAME) {
		R40_GROUP_NAME = r40_GROUP_NAME;
	}
	public BigDecimal getR40_CRM() {
		return R40_CRM;
	}
	public void setR40_CRM(BigDecimal r40_CRM) {
		R40_CRM = r40_CRM;
	}
	public BigDecimal getR40_NFBLT() {
		return R40_NFBLT;
	}
	public void setR40_NFBLT(BigDecimal r40_NFBLT) {
		R40_NFBLT = r40_NFBLT;
	}
	public BigDecimal getR40_NFBOS() {
		return R40_NFBOS;
	}
	public void setR40_NFBOS(BigDecimal r40_NFBOS) {
		R40_NFBOS = r40_NFBOS;
	}
	public BigDecimal getR40_CRM_2() {
		return R40_CRM_2;
	}
	public void setR40_CRM_2(BigDecimal r40_CRM_2) {
		R40_CRM_2 = r40_CRM_2;
	}
	public BigDecimal getR40_NFB() {
		return R40_NFB;
	}
	public void setR40_NFB(BigDecimal r40_NFB) {
		R40_NFB = r40_NFB;
	}
	public String getR40_BOND() {
		return R40_BOND;
	}
	public void setR40_BOND(String r40_BOND) {
		R40_BOND = r40_BOND;
	}
	public String getR40_CP() {
		return R40_CP;
	}
	public void setR40_CP(String r40_CP) {
		R40_CP = r40_CP;
	}
	public String getR40_EQULITY() {
		return R40_EQULITY;
	}
	public void setR40_EQULITY(String r40_EQULITY) {
		R40_EQULITY = r40_EQULITY;
	}
	public String getR40_FOREX() {
		return R40_FOREX;
	}
	public void setR40_FOREX(String r40_FOREX) {
		R40_FOREX = r40_FOREX;
	}
	public String getR40_OTHERS() {
		return R40_OTHERS;
	}
	public void setR40_OTHERS(String r40_OTHERS) {
		R40_OTHERS = r40_OTHERS;
	}
	public String getR40_INT_BANK() {
		return R40_INT_BANK;
	}
	public void setR40_INT_BANK(String r40_INT_BANK) {
		R40_INT_BANK = r40_INT_BANK;
	}
	public String getR40_DERIVATIVE() {
		return R40_DERIVATIVE;
	}
	public void setR40_DERIVATIVE(String r40_DERIVATIVE) {
		R40_DERIVATIVE = r40_DERIVATIVE;
	}
	public String getR41_GROUP_CODE() {
		return R41_GROUP_CODE;
	}
	public void setR41_GROUP_CODE(String r41_GROUP_CODE) {
		R41_GROUP_CODE = r41_GROUP_CODE;
	}
	public String getR41_GROUP_NAME() {
		return R41_GROUP_NAME;
	}
	public void setR41_GROUP_NAME(String r41_GROUP_NAME) {
		R41_GROUP_NAME = r41_GROUP_NAME;
	}
	public BigDecimal getR41_CRM() {
		return R41_CRM;
	}
	public void setR41_CRM(BigDecimal r41_CRM) {
		R41_CRM = r41_CRM;
	}
	public BigDecimal getR41_NFBLT() {
		return R41_NFBLT;
	}
	public void setR41_NFBLT(BigDecimal r41_NFBLT) {
		R41_NFBLT = r41_NFBLT;
	}
	public BigDecimal getR41_NFBOS() {
		return R41_NFBOS;
	}
	public void setR41_NFBOS(BigDecimal r41_NFBOS) {
		R41_NFBOS = r41_NFBOS;
	}
	public BigDecimal getR41_CRM_2() {
		return R41_CRM_2;
	}
	public void setR41_CRM_2(BigDecimal r41_CRM_2) {
		R41_CRM_2 = r41_CRM_2;
	}
	public BigDecimal getR41_NFB() {
		return R41_NFB;
	}
	public void setR41_NFB(BigDecimal r41_NFB) {
		R41_NFB = r41_NFB;
	}
	public String getR41_BOND() {
		return R41_BOND;
	}
	public void setR41_BOND(String r41_BOND) {
		R41_BOND = r41_BOND;
	}
	public String getR41_CP() {
		return R41_CP;
	}
	public void setR41_CP(String r41_CP) {
		R41_CP = r41_CP;
	}
	public String getR41_EQULITY() {
		return R41_EQULITY;
	}
	public void setR41_EQULITY(String r41_EQULITY) {
		R41_EQULITY = r41_EQULITY;
	}
	public String getR41_FOREX() {
		return R41_FOREX;
	}
	public void setR41_FOREX(String r41_FOREX) {
		R41_FOREX = r41_FOREX;
	}
	public String getR41_OTHERS() {
		return R41_OTHERS;
	}
	public void setR41_OTHERS(String r41_OTHERS) {
		R41_OTHERS = r41_OTHERS;
	}
	public String getR41_INT_BANK() {
		return R41_INT_BANK;
	}
	public void setR41_INT_BANK(String r41_INT_BANK) {
		R41_INT_BANK = r41_INT_BANK;
	}
	public String getR41_DERIVATIVE() {
		return R41_DERIVATIVE;
	}
	public void setR41_DERIVATIVE(String r41_DERIVATIVE) {
		R41_DERIVATIVE = r41_DERIVATIVE;
	}
	public String getR42_GROUP_CODE() {
		return R42_GROUP_CODE;
	}
	public void setR42_GROUP_CODE(String r42_GROUP_CODE) {
		R42_GROUP_CODE = r42_GROUP_CODE;
	}
	public String getR42_GROUP_NAME() {
		return R42_GROUP_NAME;
	}
	public void setR42_GROUP_NAME(String r42_GROUP_NAME) {
		R42_GROUP_NAME = r42_GROUP_NAME;
	}
	public BigDecimal getR42_CRM() {
		return R42_CRM;
	}
	public void setR42_CRM(BigDecimal r42_CRM) {
		R42_CRM = r42_CRM;
	}
	public BigDecimal getR42_NFBLT() {
		return R42_NFBLT;
	}
	public void setR42_NFBLT(BigDecimal r42_NFBLT) {
		R42_NFBLT = r42_NFBLT;
	}
	public BigDecimal getR42_NFBOS() {
		return R42_NFBOS;
	}
	public void setR42_NFBOS(BigDecimal r42_NFBOS) {
		R42_NFBOS = r42_NFBOS;
	}
	public BigDecimal getR42_CRM_2() {
		return R42_CRM_2;
	}
	public void setR42_CRM_2(BigDecimal r42_CRM_2) {
		R42_CRM_2 = r42_CRM_2;
	}
	public BigDecimal getR42_NFB() {
		return R42_NFB;
	}
	public void setR42_NFB(BigDecimal r42_NFB) {
		R42_NFB = r42_NFB;
	}
	public String getR42_BOND() {
		return R42_BOND;
	}
	public void setR42_BOND(String r42_BOND) {
		R42_BOND = r42_BOND;
	}
	public String getR42_CP() {
		return R42_CP;
	}
	public void setR42_CP(String r42_CP) {
		R42_CP = r42_CP;
	}
	public String getR42_EQULITY() {
		return R42_EQULITY;
	}
	public void setR42_EQULITY(String r42_EQULITY) {
		R42_EQULITY = r42_EQULITY;
	}
	public String getR42_FOREX() {
		return R42_FOREX;
	}
	public void setR42_FOREX(String r42_FOREX) {
		R42_FOREX = r42_FOREX;
	}
	public String getR42_OTHERS() {
		return R42_OTHERS;
	}
	public void setR42_OTHERS(String r42_OTHERS) {
		R42_OTHERS = r42_OTHERS;
	}
	public String getR42_INT_BANK() {
		return R42_INT_BANK;
	}
	public void setR42_INT_BANK(String r42_INT_BANK) {
		R42_INT_BANK = r42_INT_BANK;
	}
	public String getR42_DERIVATIVE() {
		return R42_DERIVATIVE;
	}
	public void setR42_DERIVATIVE(String r42_DERIVATIVE) {
		R42_DERIVATIVE = r42_DERIVATIVE;
	}
	public String getR43_GROUP_CODE() {
		return R43_GROUP_CODE;
	}
	public void setR43_GROUP_CODE(String r43_GROUP_CODE) {
		R43_GROUP_CODE = r43_GROUP_CODE;
	}
	public String getR43_GROUP_NAME() {
		return R43_GROUP_NAME;
	}
	public void setR43_GROUP_NAME(String r43_GROUP_NAME) {
		R43_GROUP_NAME = r43_GROUP_NAME;
	}
	public BigDecimal getR43_CRM() {
		return R43_CRM;
	}
	public void setR43_CRM(BigDecimal r43_CRM) {
		R43_CRM = r43_CRM;
	}
	public BigDecimal getR43_NFBLT() {
		return R43_NFBLT;
	}
	public void setR43_NFBLT(BigDecimal r43_NFBLT) {
		R43_NFBLT = r43_NFBLT;
	}
	public BigDecimal getR43_NFBOS() {
		return R43_NFBOS;
	}
	public void setR43_NFBOS(BigDecimal r43_NFBOS) {
		R43_NFBOS = r43_NFBOS;
	}
	public BigDecimal getR43_CRM_2() {
		return R43_CRM_2;
	}
	public void setR43_CRM_2(BigDecimal r43_CRM_2) {
		R43_CRM_2 = r43_CRM_2;
	}
	public BigDecimal getR43_NFB() {
		return R43_NFB;
	}
	public void setR43_NFB(BigDecimal r43_NFB) {
		R43_NFB = r43_NFB;
	}
	public String getR43_BOND() {
		return R43_BOND;
	}
	public void setR43_BOND(String r43_BOND) {
		R43_BOND = r43_BOND;
	}
	public String getR43_CP() {
		return R43_CP;
	}
	public void setR43_CP(String r43_CP) {
		R43_CP = r43_CP;
	}
	public String getR43_EQULITY() {
		return R43_EQULITY;
	}
	public void setR43_EQULITY(String r43_EQULITY) {
		R43_EQULITY = r43_EQULITY;
	}
	public String getR43_FOREX() {
		return R43_FOREX;
	}
	public void setR43_FOREX(String r43_FOREX) {
		R43_FOREX = r43_FOREX;
	}
	public String getR43_OTHERS() {
		return R43_OTHERS;
	}
	public void setR43_OTHERS(String r43_OTHERS) {
		R43_OTHERS = r43_OTHERS;
	}
	public String getR43_INT_BANK() {
		return R43_INT_BANK;
	}
	public void setR43_INT_BANK(String r43_INT_BANK) {
		R43_INT_BANK = r43_INT_BANK;
	}
	public String getR43_DERIVATIVE() {
		return R43_DERIVATIVE;
	}
	public void setR43_DERIVATIVE(String r43_DERIVATIVE) {
		R43_DERIVATIVE = r43_DERIVATIVE;
	}
	public String getR44_GROUP_CODE() {
		return R44_GROUP_CODE;
	}
	public void setR44_GROUP_CODE(String r44_GROUP_CODE) {
		R44_GROUP_CODE = r44_GROUP_CODE;
	}
	public String getR44_GROUP_NAME() {
		return R44_GROUP_NAME;
	}
	public void setR44_GROUP_NAME(String r44_GROUP_NAME) {
		R44_GROUP_NAME = r44_GROUP_NAME;
	}
	public BigDecimal getR44_CRM() {
		return R44_CRM;
	}
	public void setR44_CRM(BigDecimal r44_CRM) {
		R44_CRM = r44_CRM;
	}
	public BigDecimal getR44_NFBLT() {
		return R44_NFBLT;
	}
	public void setR44_NFBLT(BigDecimal r44_NFBLT) {
		R44_NFBLT = r44_NFBLT;
	}
	public BigDecimal getR44_NFBOS() {
		return R44_NFBOS;
	}
	public void setR44_NFBOS(BigDecimal r44_NFBOS) {
		R44_NFBOS = r44_NFBOS;
	}
	public BigDecimal getR44_CRM_2() {
		return R44_CRM_2;
	}
	public void setR44_CRM_2(BigDecimal r44_CRM_2) {
		R44_CRM_2 = r44_CRM_2;
	}
	public BigDecimal getR44_NFB() {
		return R44_NFB;
	}
	public void setR44_NFB(BigDecimal r44_NFB) {
		R44_NFB = r44_NFB;
	}
	public String getR44_BOND() {
		return R44_BOND;
	}
	public void setR44_BOND(String r44_BOND) {
		R44_BOND = r44_BOND;
	}
	public String getR44_CP() {
		return R44_CP;
	}
	public void setR44_CP(String r44_CP) {
		R44_CP = r44_CP;
	}
	public String getR44_EQULITY() {
		return R44_EQULITY;
	}
	public void setR44_EQULITY(String r44_EQULITY) {
		R44_EQULITY = r44_EQULITY;
	}
	public String getR44_FOREX() {
		return R44_FOREX;
	}
	public void setR44_FOREX(String r44_FOREX) {
		R44_FOREX = r44_FOREX;
	}
	public String getR44_OTHERS() {
		return R44_OTHERS;
	}
	public void setR44_OTHERS(String r44_OTHERS) {
		R44_OTHERS = r44_OTHERS;
	}
	public String getR44_INT_BANK() {
		return R44_INT_BANK;
	}
	public void setR44_INT_BANK(String r44_INT_BANK) {
		R44_INT_BANK = r44_INT_BANK;
	}
	public String getR44_DERIVATIVE() {
		return R44_DERIVATIVE;
	}
	public void setR44_DERIVATIVE(String r44_DERIVATIVE) {
		R44_DERIVATIVE = r44_DERIVATIVE;
	}
	public String getR45_GROUP_CODE() {
		return R45_GROUP_CODE;
	}
	public void setR45_GROUP_CODE(String r45_GROUP_CODE) {
		R45_GROUP_CODE = r45_GROUP_CODE;
	}
	public String getR45_GROUP_NAME() {
		return R45_GROUP_NAME;
	}
	public void setR45_GROUP_NAME(String r45_GROUP_NAME) {
		R45_GROUP_NAME = r45_GROUP_NAME;
	}
	public BigDecimal getR45_CRM() {
		return R45_CRM;
	}
	public void setR45_CRM(BigDecimal r45_CRM) {
		R45_CRM = r45_CRM;
	}
	public BigDecimal getR45_NFBLT() {
		return R45_NFBLT;
	}
	public void setR45_NFBLT(BigDecimal r45_NFBLT) {
		R45_NFBLT = r45_NFBLT;
	}
	public BigDecimal getR45_NFBOS() {
		return R45_NFBOS;
	}
	public void setR45_NFBOS(BigDecimal r45_NFBOS) {
		R45_NFBOS = r45_NFBOS;
	}
	public BigDecimal getR45_CRM_2() {
		return R45_CRM_2;
	}
	public void setR45_CRM_2(BigDecimal r45_CRM_2) {
		R45_CRM_2 = r45_CRM_2;
	}
	public BigDecimal getR45_NFB() {
		return R45_NFB;
	}
	public void setR45_NFB(BigDecimal r45_NFB) {
		R45_NFB = r45_NFB;
	}
	public String getR45_BOND() {
		return R45_BOND;
	}
	public void setR45_BOND(String r45_BOND) {
		R45_BOND = r45_BOND;
	}
	public String getR45_CP() {
		return R45_CP;
	}
	public void setR45_CP(String r45_CP) {
		R45_CP = r45_CP;
	}
	public String getR45_EQULITY() {
		return R45_EQULITY;
	}
	public void setR45_EQULITY(String r45_EQULITY) {
		R45_EQULITY = r45_EQULITY;
	}
	public String getR45_FOREX() {
		return R45_FOREX;
	}
	public void setR45_FOREX(String r45_FOREX) {
		R45_FOREX = r45_FOREX;
	}
	public String getR45_OTHERS() {
		return R45_OTHERS;
	}
	public void setR45_OTHERS(String r45_OTHERS) {
		R45_OTHERS = r45_OTHERS;
	}
	public String getR45_INT_BANK() {
		return R45_INT_BANK;
	}
	public void setR45_INT_BANK(String r45_INT_BANK) {
		R45_INT_BANK = r45_INT_BANK;
	}
	public String getR45_DERIVATIVE() {
		return R45_DERIVATIVE;
	}
	public void setR45_DERIVATIVE(String r45_DERIVATIVE) {
		R45_DERIVATIVE = r45_DERIVATIVE;
	}
	public String getR46_GROUP_CODE() {
		return R46_GROUP_CODE;
	}
	public void setR46_GROUP_CODE(String r46_GROUP_CODE) {
		R46_GROUP_CODE = r46_GROUP_CODE;
	}
	public String getR46_GROUP_NAME() {
		return R46_GROUP_NAME;
	}
	public void setR46_GROUP_NAME(String r46_GROUP_NAME) {
		R46_GROUP_NAME = r46_GROUP_NAME;
	}
	public BigDecimal getR46_CRM() {
		return R46_CRM;
	}
	public void setR46_CRM(BigDecimal r46_CRM) {
		R46_CRM = r46_CRM;
	}
	public BigDecimal getR46_NFBLT() {
		return R46_NFBLT;
	}
	public void setR46_NFBLT(BigDecimal r46_NFBLT) {
		R46_NFBLT = r46_NFBLT;
	}
	public BigDecimal getR46_NFBOS() {
		return R46_NFBOS;
	}
	public void setR46_NFBOS(BigDecimal r46_NFBOS) {
		R46_NFBOS = r46_NFBOS;
	}
	public BigDecimal getR46_CRM_2() {
		return R46_CRM_2;
	}
	public void setR46_CRM_2(BigDecimal r46_CRM_2) {
		R46_CRM_2 = r46_CRM_2;
	}
	public BigDecimal getR46_NFB() {
		return R46_NFB;
	}
	public void setR46_NFB(BigDecimal r46_NFB) {
		R46_NFB = r46_NFB;
	}
	public String getR46_BOND() {
		return R46_BOND;
	}
	public void setR46_BOND(String r46_BOND) {
		R46_BOND = r46_BOND;
	}
	public String getR46_CP() {
		return R46_CP;
	}
	public void setR46_CP(String r46_CP) {
		R46_CP = r46_CP;
	}
	public String getR46_EQULITY() {
		return R46_EQULITY;
	}
	public void setR46_EQULITY(String r46_EQULITY) {
		R46_EQULITY = r46_EQULITY;
	}
	public String getR46_FOREX() {
		return R46_FOREX;
	}
	public void setR46_FOREX(String r46_FOREX) {
		R46_FOREX = r46_FOREX;
	}
	public String getR46_OTHERS() {
		return R46_OTHERS;
	}
	public void setR46_OTHERS(String r46_OTHERS) {
		R46_OTHERS = r46_OTHERS;
	}
	public String getR46_INT_BANK() {
		return R46_INT_BANK;
	}
	public void setR46_INT_BANK(String r46_INT_BANK) {
		R46_INT_BANK = r46_INT_BANK;
	}
	public String getR46_DERIVATIVE() {
		return R46_DERIVATIVE;
	}
	public void setR46_DERIVATIVE(String r46_DERIVATIVE) {
		R46_DERIVATIVE = r46_DERIVATIVE;
	}
	public String getR47_GROUP_CODE() {
		return R47_GROUP_CODE;
	}
	public void setR47_GROUP_CODE(String r47_GROUP_CODE) {
		R47_GROUP_CODE = r47_GROUP_CODE;
	}
	public String getR47_GROUP_NAME() {
		return R47_GROUP_NAME;
	}
	public void setR47_GROUP_NAME(String r47_GROUP_NAME) {
		R47_GROUP_NAME = r47_GROUP_NAME;
	}
	public BigDecimal getR47_CRM() {
		return R47_CRM;
	}
	public void setR47_CRM(BigDecimal r47_CRM) {
		R47_CRM = r47_CRM;
	}
	public BigDecimal getR47_NFBLT() {
		return R47_NFBLT;
	}
	public void setR47_NFBLT(BigDecimal r47_NFBLT) {
		R47_NFBLT = r47_NFBLT;
	}
	public BigDecimal getR47_NFBOS() {
		return R47_NFBOS;
	}
	public void setR47_NFBOS(BigDecimal r47_NFBOS) {
		R47_NFBOS = r47_NFBOS;
	}
	public BigDecimal getR47_CRM_2() {
		return R47_CRM_2;
	}
	public void setR47_CRM_2(BigDecimal r47_CRM_2) {
		R47_CRM_2 = r47_CRM_2;
	}
	public BigDecimal getR47_NFB() {
		return R47_NFB;
	}
	public void setR47_NFB(BigDecimal r47_NFB) {
		R47_NFB = r47_NFB;
	}
	public String getR47_BOND() {
		return R47_BOND;
	}
	public void setR47_BOND(String r47_BOND) {
		R47_BOND = r47_BOND;
	}
	public String getR47_CP() {
		return R47_CP;
	}
	public void setR47_CP(String r47_CP) {
		R47_CP = r47_CP;
	}
	public String getR47_EQULITY() {
		return R47_EQULITY;
	}
	public void setR47_EQULITY(String r47_EQULITY) {
		R47_EQULITY = r47_EQULITY;
	}
	public String getR47_FOREX() {
		return R47_FOREX;
	}
	public void setR47_FOREX(String r47_FOREX) {
		R47_FOREX = r47_FOREX;
	}
	public String getR47_OTHERS() {
		return R47_OTHERS;
	}
	public void setR47_OTHERS(String r47_OTHERS) {
		R47_OTHERS = r47_OTHERS;
	}
	public String getR47_INT_BANK() {
		return R47_INT_BANK;
	}
	public void setR47_INT_BANK(String r47_INT_BANK) {
		R47_INT_BANK = r47_INT_BANK;
	}
	public String getR47_DERIVATIVE() {
		return R47_DERIVATIVE;
	}
	public void setR47_DERIVATIVE(String r47_DERIVATIVE) {
		R47_DERIVATIVE = r47_DERIVATIVE;
	}
	public String getR48_GROUP_CODE() {
		return R48_GROUP_CODE;
	}
	public void setR48_GROUP_CODE(String r48_GROUP_CODE) {
		R48_GROUP_CODE = r48_GROUP_CODE;
	}
	public String getR48_GROUP_NAME() {
		return R48_GROUP_NAME;
	}
	public void setR48_GROUP_NAME(String r48_GROUP_NAME) {
		R48_GROUP_NAME = r48_GROUP_NAME;
	}
	public BigDecimal getR48_CRM() {
		return R48_CRM;
	}
	public void setR48_CRM(BigDecimal r48_CRM) {
		R48_CRM = r48_CRM;
	}
	public BigDecimal getR48_NFBLT() {
		return R48_NFBLT;
	}
	public void setR48_NFBLT(BigDecimal r48_NFBLT) {
		R48_NFBLT = r48_NFBLT;
	}
	public BigDecimal getR48_NFBOS() {
		return R48_NFBOS;
	}
	public void setR48_NFBOS(BigDecimal r48_NFBOS) {
		R48_NFBOS = r48_NFBOS;
	}
	public BigDecimal getR48_CRM_2() {
		return R48_CRM_2;
	}
	public void setR48_CRM_2(BigDecimal r48_CRM_2) {
		R48_CRM_2 = r48_CRM_2;
	}
	public BigDecimal getR48_NFB() {
		return R48_NFB;
	}
	public void setR48_NFB(BigDecimal r48_NFB) {
		R48_NFB = r48_NFB;
	}
	public String getR48_BOND() {
		return R48_BOND;
	}
	public void setR48_BOND(String r48_BOND) {
		R48_BOND = r48_BOND;
	}
	public String getR48_CP() {
		return R48_CP;
	}
	public void setR48_CP(String r48_CP) {
		R48_CP = r48_CP;
	}
	public String getR48_EQULITY() {
		return R48_EQULITY;
	}
	public void setR48_EQULITY(String r48_EQULITY) {
		R48_EQULITY = r48_EQULITY;
	}
	public String getR48_FOREX() {
		return R48_FOREX;
	}
	public void setR48_FOREX(String r48_FOREX) {
		R48_FOREX = r48_FOREX;
	}
	public String getR48_OTHERS() {
		return R48_OTHERS;
	}
	public void setR48_OTHERS(String r48_OTHERS) {
		R48_OTHERS = r48_OTHERS;
	}
	public String getR48_INT_BANK() {
		return R48_INT_BANK;
	}
	public void setR48_INT_BANK(String r48_INT_BANK) {
		R48_INT_BANK = r48_INT_BANK;
	}
	public String getR48_DERIVATIVE() {
		return R48_DERIVATIVE;
	}
	public void setR48_DERIVATIVE(String r48_DERIVATIVE) {
		R48_DERIVATIVE = r48_DERIVATIVE;
	}
	public String getR49_GROUP_CODE() {
		return R49_GROUP_CODE;
	}
	public void setR49_GROUP_CODE(String r49_GROUP_CODE) {
		R49_GROUP_CODE = r49_GROUP_CODE;
	}
	public String getR49_GROUP_NAME() {
		return R49_GROUP_NAME;
	}
	public void setR49_GROUP_NAME(String r49_GROUP_NAME) {
		R49_GROUP_NAME = r49_GROUP_NAME;
	}
	public BigDecimal getR49_CRM() {
		return R49_CRM;
	}
	public void setR49_CRM(BigDecimal r49_CRM) {
		R49_CRM = r49_CRM;
	}
	public BigDecimal getR49_NFBLT() {
		return R49_NFBLT;
	}
	public void setR49_NFBLT(BigDecimal r49_NFBLT) {
		R49_NFBLT = r49_NFBLT;
	}
	public BigDecimal getR49_NFBOS() {
		return R49_NFBOS;
	}
	public void setR49_NFBOS(BigDecimal r49_NFBOS) {
		R49_NFBOS = r49_NFBOS;
	}
	public BigDecimal getR49_CRM_2() {
		return R49_CRM_2;
	}
	public void setR49_CRM_2(BigDecimal r49_CRM_2) {
		R49_CRM_2 = r49_CRM_2;
	}
	public BigDecimal getR49_NFB() {
		return R49_NFB;
	}
	public void setR49_NFB(BigDecimal r49_NFB) {
		R49_NFB = r49_NFB;
	}
	public String getR49_BOND() {
		return R49_BOND;
	}
	public void setR49_BOND(String r49_BOND) {
		R49_BOND = r49_BOND;
	}
	public String getR49_CP() {
		return R49_CP;
	}
	public void setR49_CP(String r49_CP) {
		R49_CP = r49_CP;
	}
	public String getR49_EQULITY() {
		return R49_EQULITY;
	}
	public void setR49_EQULITY(String r49_EQULITY) {
		R49_EQULITY = r49_EQULITY;
	}
	public String getR49_FOREX() {
		return R49_FOREX;
	}
	public void setR49_FOREX(String r49_FOREX) {
		R49_FOREX = r49_FOREX;
	}
	public String getR49_OTHERS() {
		return R49_OTHERS;
	}
	public void setR49_OTHERS(String r49_OTHERS) {
		R49_OTHERS = r49_OTHERS;
	}
	public String getR49_INT_BANK() {
		return R49_INT_BANK;
	}
	public void setR49_INT_BANK(String r49_INT_BANK) {
		R49_INT_BANK = r49_INT_BANK;
	}
	public String getR49_DERIVATIVE() {
		return R49_DERIVATIVE;
	}
	public void setR49_DERIVATIVE(String r49_DERIVATIVE) {
		R49_DERIVATIVE = r49_DERIVATIVE;
	}
	public String getR50_GROUP_CODE() {
		return R50_GROUP_CODE;
	}
	public void setR50_GROUP_CODE(String r50_GROUP_CODE) {
		R50_GROUP_CODE = r50_GROUP_CODE;
	}
	public String getR50_GROUP_NAME() {
		return R50_GROUP_NAME;
	}
	public void setR50_GROUP_NAME(String r50_GROUP_NAME) {
		R50_GROUP_NAME = r50_GROUP_NAME;
	}
	public BigDecimal getR50_CRM() {
		return R50_CRM;
	}
	public void setR50_CRM(BigDecimal r50_CRM) {
		R50_CRM = r50_CRM;
	}
	public BigDecimal getR50_NFBLT() {
		return R50_NFBLT;
	}
	public void setR50_NFBLT(BigDecimal r50_NFBLT) {
		R50_NFBLT = r50_NFBLT;
	}
	public BigDecimal getR50_NFBOS() {
		return R50_NFBOS;
	}
	public void setR50_NFBOS(BigDecimal r50_NFBOS) {
		R50_NFBOS = r50_NFBOS;
	}
	public BigDecimal getR50_CRM_2() {
		return R50_CRM_2;
	}
	public void setR50_CRM_2(BigDecimal r50_CRM_2) {
		R50_CRM_2 = r50_CRM_2;
	}
	public BigDecimal getR50_NFB() {
		return R50_NFB;
	}
	public void setR50_NFB(BigDecimal r50_NFB) {
		R50_NFB = r50_NFB;
	}
	public String getR50_BOND() {
		return R50_BOND;
	}
	public void setR50_BOND(String r50_BOND) {
		R50_BOND = r50_BOND;
	}
	public String getR50_CP() {
		return R50_CP;
	}
	public void setR50_CP(String r50_CP) {
		R50_CP = r50_CP;
	}
	public String getR50_EQULITY() {
		return R50_EQULITY;
	}
	public void setR50_EQULITY(String r50_EQULITY) {
		R50_EQULITY = r50_EQULITY;
	}
	public String getR50_FOREX() {
		return R50_FOREX;
	}
	public void setR50_FOREX(String r50_FOREX) {
		R50_FOREX = r50_FOREX;
	}
	public String getR50_OTHERS() {
		return R50_OTHERS;
	}
	public void setR50_OTHERS(String r50_OTHERS) {
		R50_OTHERS = r50_OTHERS;
	}
	public String getR50_INT_BANK() {
		return R50_INT_BANK;
	}
	public void setR50_INT_BANK(String r50_INT_BANK) {
		R50_INT_BANK = r50_INT_BANK;
	}
	public String getR50_DERIVATIVE() {
		return R50_DERIVATIVE;
	}
	public void setR50_DERIVATIVE(String r50_DERIVATIVE) {
		R50_DERIVATIVE = r50_DERIVATIVE;
	}
	public String getR51_GROUP_CODE() {
		return R51_GROUP_CODE;
	}
	public void setR51_GROUP_CODE(String r51_GROUP_CODE) {
		R51_GROUP_CODE = r51_GROUP_CODE;
	}
	public String getR51_GROUP_NAME() {
		return R51_GROUP_NAME;
	}
	public void setR51_GROUP_NAME(String r51_GROUP_NAME) {
		R51_GROUP_NAME = r51_GROUP_NAME;
	}
	public BigDecimal getR51_CRM() {
		return R51_CRM;
	}
	public void setR51_CRM(BigDecimal r51_CRM) {
		R51_CRM = r51_CRM;
	}
	public BigDecimal getR51_NFBLT() {
		return R51_NFBLT;
	}
	public void setR51_NFBLT(BigDecimal r51_NFBLT) {
		R51_NFBLT = r51_NFBLT;
	}
	public BigDecimal getR51_NFBOS() {
		return R51_NFBOS;
	}
	public void setR51_NFBOS(BigDecimal r51_NFBOS) {
		R51_NFBOS = r51_NFBOS;
	}
	public BigDecimal getR51_CRM_2() {
		return R51_CRM_2;
	}
	public void setR51_CRM_2(BigDecimal r51_CRM_2) {
		R51_CRM_2 = r51_CRM_2;
	}
	public BigDecimal getR51_NFB() {
		return R51_NFB;
	}
	public void setR51_NFB(BigDecimal r51_NFB) {
		R51_NFB = r51_NFB;
	}
	public String getR51_BOND() {
		return R51_BOND;
	}
	public void setR51_BOND(String r51_BOND) {
		R51_BOND = r51_BOND;
	}
	public String getR51_CP() {
		return R51_CP;
	}
	public void setR51_CP(String r51_CP) {
		R51_CP = r51_CP;
	}
	public String getR51_EQULITY() {
		return R51_EQULITY;
	}
	public void setR51_EQULITY(String r51_EQULITY) {
		R51_EQULITY = r51_EQULITY;
	}
	public String getR51_FOREX() {
		return R51_FOREX;
	}
	public void setR51_FOREX(String r51_FOREX) {
		R51_FOREX = r51_FOREX;
	}
	public String getR51_OTHERS() {
		return R51_OTHERS;
	}
	public void setR51_OTHERS(String r51_OTHERS) {
		R51_OTHERS = r51_OTHERS;
	}
	public String getR51_INT_BANK() {
		return R51_INT_BANK;
	}
	public void setR51_INT_BANK(String r51_INT_BANK) {
		R51_INT_BANK = r51_INT_BANK;
	}
	public String getR51_DERIVATIVE() {
		return R51_DERIVATIVE;
	}
	public void setR51_DERIVATIVE(String r51_DERIVATIVE) {
		R51_DERIVATIVE = r51_DERIVATIVE;
	}
	public String getR52_GROUP_CODE() {
		return R52_GROUP_CODE;
	}
	public void setR52_GROUP_CODE(String r52_GROUP_CODE) {
		R52_GROUP_CODE = r52_GROUP_CODE;
	}
	public String getR52_GROUP_NAME() {
		return R52_GROUP_NAME;
	}
	public void setR52_GROUP_NAME(String r52_GROUP_NAME) {
		R52_GROUP_NAME = r52_GROUP_NAME;
	}
	public BigDecimal getR52_CRM() {
		return R52_CRM;
	}
	public void setR52_CRM(BigDecimal r52_CRM) {
		R52_CRM = r52_CRM;
	}
	public BigDecimal getR52_NFBLT() {
		return R52_NFBLT;
	}
	public void setR52_NFBLT(BigDecimal r52_NFBLT) {
		R52_NFBLT = r52_NFBLT;
	}
	public BigDecimal getR52_NFBOS() {
		return R52_NFBOS;
	}
	public void setR52_NFBOS(BigDecimal r52_NFBOS) {
		R52_NFBOS = r52_NFBOS;
	}
	public BigDecimal getR52_CRM_2() {
		return R52_CRM_2;
	}
	public void setR52_CRM_2(BigDecimal r52_CRM_2) {
		R52_CRM_2 = r52_CRM_2;
	}
	public BigDecimal getR52_NFB() {
		return R52_NFB;
	}
	public void setR52_NFB(BigDecimal r52_NFB) {
		R52_NFB = r52_NFB;
	}
	public String getR52_BOND() {
		return R52_BOND;
	}
	public void setR52_BOND(String r52_BOND) {
		R52_BOND = r52_BOND;
	}
	public String getR52_CP() {
		return R52_CP;
	}
	public void setR52_CP(String r52_CP) {
		R52_CP = r52_CP;
	}
	public String getR52_EQULITY() {
		return R52_EQULITY;
	}
	public void setR52_EQULITY(String r52_EQULITY) {
		R52_EQULITY = r52_EQULITY;
	}
	public String getR52_FOREX() {
		return R52_FOREX;
	}
	public void setR52_FOREX(String r52_FOREX) {
		R52_FOREX = r52_FOREX;
	}
	public String getR52_OTHERS() {
		return R52_OTHERS;
	}
	public void setR52_OTHERS(String r52_OTHERS) {
		R52_OTHERS = r52_OTHERS;
	}
	public String getR52_INT_BANK() {
		return R52_INT_BANK;
	}
	public void setR52_INT_BANK(String r52_INT_BANK) {
		R52_INT_BANK = r52_INT_BANK;
	}
	public String getR52_DERIVATIVE() {
		return R52_DERIVATIVE;
	}
	public void setR52_DERIVATIVE(String r52_DERIVATIVE) {
		R52_DERIVATIVE = r52_DERIVATIVE;
	}
	public String getR53_GROUP_CODE() {
		return R53_GROUP_CODE;
	}
	public void setR53_GROUP_CODE(String r53_GROUP_CODE) {
		R53_GROUP_CODE = r53_GROUP_CODE;
	}
	public String getR53_GROUP_NAME() {
		return R53_GROUP_NAME;
	}
	public void setR53_GROUP_NAME(String r53_GROUP_NAME) {
		R53_GROUP_NAME = r53_GROUP_NAME;
	}
	public BigDecimal getR53_CRM() {
		return R53_CRM;
	}
	public void setR53_CRM(BigDecimal r53_CRM) {
		R53_CRM = r53_CRM;
	}
	public BigDecimal getR53_NFBLT() {
		return R53_NFBLT;
	}
	public void setR53_NFBLT(BigDecimal r53_NFBLT) {
		R53_NFBLT = r53_NFBLT;
	}
	public BigDecimal getR53_NFBOS() {
		return R53_NFBOS;
	}
	public void setR53_NFBOS(BigDecimal r53_NFBOS) {
		R53_NFBOS = r53_NFBOS;
	}
	public BigDecimal getR53_CRM_2() {
		return R53_CRM_2;
	}
	public void setR53_CRM_2(BigDecimal r53_CRM_2) {
		R53_CRM_2 = r53_CRM_2;
	}
	public BigDecimal getR53_NFB() {
		return R53_NFB;
	}
	public void setR53_NFB(BigDecimal r53_NFB) {
		R53_NFB = r53_NFB;
	}
	public String getR53_BOND() {
		return R53_BOND;
	}
	public void setR53_BOND(String r53_BOND) {
		R53_BOND = r53_BOND;
	}
	public String getR53_CP() {
		return R53_CP;
	}
	public void setR53_CP(String r53_CP) {
		R53_CP = r53_CP;
	}
	public String getR53_EQULITY() {
		return R53_EQULITY;
	}
	public void setR53_EQULITY(String r53_EQULITY) {
		R53_EQULITY = r53_EQULITY;
	}
	public String getR53_FOREX() {
		return R53_FOREX;
	}
	public void setR53_FOREX(String r53_FOREX) {
		R53_FOREX = r53_FOREX;
	}
	public String getR53_OTHERS() {
		return R53_OTHERS;
	}
	public void setR53_OTHERS(String r53_OTHERS) {
		R53_OTHERS = r53_OTHERS;
	}
	public String getR53_INT_BANK() {
		return R53_INT_BANK;
	}
	public void setR53_INT_BANK(String r53_INT_BANK) {
		R53_INT_BANK = r53_INT_BANK;
	}
	public String getR53_DERIVATIVE() {
		return R53_DERIVATIVE;
	}
	public void setR53_DERIVATIVE(String r53_DERIVATIVE) {
		R53_DERIVATIVE = r53_DERIVATIVE;
	}
	public String getR54_GROUP_CODE() {
		return R54_GROUP_CODE;
	}
	public void setR54_GROUP_CODE(String r54_GROUP_CODE) {
		R54_GROUP_CODE = r54_GROUP_CODE;
	}
	public String getR54_GROUP_NAME() {
		return R54_GROUP_NAME;
	}
	public void setR54_GROUP_NAME(String r54_GROUP_NAME) {
		R54_GROUP_NAME = r54_GROUP_NAME;
	}
	public BigDecimal getR54_CRM() {
		return R54_CRM;
	}
	public void setR54_CRM(BigDecimal r54_CRM) {
		R54_CRM = r54_CRM;
	}
	public BigDecimal getR54_NFBLT() {
		return R54_NFBLT;
	}
	public void setR54_NFBLT(BigDecimal r54_NFBLT) {
		R54_NFBLT = r54_NFBLT;
	}
	public BigDecimal getR54_NFBOS() {
		return R54_NFBOS;
	}
	public void setR54_NFBOS(BigDecimal r54_NFBOS) {
		R54_NFBOS = r54_NFBOS;
	}
	public BigDecimal getR54_CRM_2() {
		return R54_CRM_2;
	}
	public void setR54_CRM_2(BigDecimal r54_CRM_2) {
		R54_CRM_2 = r54_CRM_2;
	}
	public BigDecimal getR54_NFB() {
		return R54_NFB;
	}
	public void setR54_NFB(BigDecimal r54_NFB) {
		R54_NFB = r54_NFB;
	}
	public String getR54_BOND() {
		return R54_BOND;
	}
	public void setR54_BOND(String r54_BOND) {
		R54_BOND = r54_BOND;
	}
	public String getR54_CP() {
		return R54_CP;
	}
	public void setR54_CP(String r54_CP) {
		R54_CP = r54_CP;
	}
	public String getR54_EQULITY() {
		return R54_EQULITY;
	}
	public void setR54_EQULITY(String r54_EQULITY) {
		R54_EQULITY = r54_EQULITY;
	}
	public String getR54_FOREX() {
		return R54_FOREX;
	}
	public void setR54_FOREX(String r54_FOREX) {
		R54_FOREX = r54_FOREX;
	}
	public String getR54_OTHERS() {
		return R54_OTHERS;
	}
	public void setR54_OTHERS(String r54_OTHERS) {
		R54_OTHERS = r54_OTHERS;
	}
	public String getR54_INT_BANK() {
		return R54_INT_BANK;
	}
	public void setR54_INT_BANK(String r54_INT_BANK) {
		R54_INT_BANK = r54_INT_BANK;
	}
	public String getR54_DERIVATIVE() {
		return R54_DERIVATIVE;
	}
	public void setR54_DERIVATIVE(String r54_DERIVATIVE) {
		R54_DERIVATIVE = r54_DERIVATIVE;
	}
	public String getR55_GROUP_CODE() {
		return R55_GROUP_CODE;
	}
	public void setR55_GROUP_CODE(String r55_GROUP_CODE) {
		R55_GROUP_CODE = r55_GROUP_CODE;
	}
	public String getR55_GROUP_NAME() {
		return R55_GROUP_NAME;
	}
	public void setR55_GROUP_NAME(String r55_GROUP_NAME) {
		R55_GROUP_NAME = r55_GROUP_NAME;
	}
	public BigDecimal getR55_CRM() {
		return R55_CRM;
	}
	public void setR55_CRM(BigDecimal r55_CRM) {
		R55_CRM = r55_CRM;
	}
	public BigDecimal getR55_NFBLT() {
		return R55_NFBLT;
	}
	public void setR55_NFBLT(BigDecimal r55_NFBLT) {
		R55_NFBLT = r55_NFBLT;
	}
	public BigDecimal getR55_NFBOS() {
		return R55_NFBOS;
	}
	public void setR55_NFBOS(BigDecimal r55_NFBOS) {
		R55_NFBOS = r55_NFBOS;
	}
	public BigDecimal getR55_CRM_2() {
		return R55_CRM_2;
	}
	public void setR55_CRM_2(BigDecimal r55_CRM_2) {
		R55_CRM_2 = r55_CRM_2;
	}
	public BigDecimal getR55_NFB() {
		return R55_NFB;
	}
	public void setR55_NFB(BigDecimal r55_NFB) {
		R55_NFB = r55_NFB;
	}
	public String getR55_BOND() {
		return R55_BOND;
	}
	public void setR55_BOND(String r55_BOND) {
		R55_BOND = r55_BOND;
	}
	public String getR55_CP() {
		return R55_CP;
	}
	public void setR55_CP(String r55_CP) {
		R55_CP = r55_CP;
	}
	public String getR55_EQULITY() {
		return R55_EQULITY;
	}
	public void setR55_EQULITY(String r55_EQULITY) {
		R55_EQULITY = r55_EQULITY;
	}
	public String getR55_FOREX() {
		return R55_FOREX;
	}
	public void setR55_FOREX(String r55_FOREX) {
		R55_FOREX = r55_FOREX;
	}
	public String getR55_OTHERS() {
		return R55_OTHERS;
	}
	public void setR55_OTHERS(String r55_OTHERS) {
		R55_OTHERS = r55_OTHERS;
	}
	public String getR55_INT_BANK() {
		return R55_INT_BANK;
	}
	public void setR55_INT_BANK(String r55_INT_BANK) {
		R55_INT_BANK = r55_INT_BANK;
	}
	public String getR55_DERIVATIVE() {
		return R55_DERIVATIVE;
	}
	public void setR55_DERIVATIVE(String r55_DERIVATIVE) {
		R55_DERIVATIVE = r55_DERIVATIVE;
	}
	public String getR56_GROUP_CODE() {
		return R56_GROUP_CODE;
	}
	public void setR56_GROUP_CODE(String r56_GROUP_CODE) {
		R56_GROUP_CODE = r56_GROUP_CODE;
	}
	public String getR56_GROUP_NAME() {
		return R56_GROUP_NAME;
	}
	public void setR56_GROUP_NAME(String r56_GROUP_NAME) {
		R56_GROUP_NAME = r56_GROUP_NAME;
	}
	public BigDecimal getR56_CRM() {
		return R56_CRM;
	}
	public void setR56_CRM(BigDecimal r56_CRM) {
		R56_CRM = r56_CRM;
	}
	public BigDecimal getR56_NFBLT() {
		return R56_NFBLT;
	}
	public void setR56_NFBLT(BigDecimal r56_NFBLT) {
		R56_NFBLT = r56_NFBLT;
	}
	public BigDecimal getR56_NFBOS() {
		return R56_NFBOS;
	}
	public void setR56_NFBOS(BigDecimal r56_NFBOS) {
		R56_NFBOS = r56_NFBOS;
	}
	public BigDecimal getR56_CRM_2() {
		return R56_CRM_2;
	}
	public void setR56_CRM_2(BigDecimal r56_CRM_2) {
		R56_CRM_2 = r56_CRM_2;
	}
	public BigDecimal getR56_NFB() {
		return R56_NFB;
	}
	public void setR56_NFB(BigDecimal r56_NFB) {
		R56_NFB = r56_NFB;
	}
	public String getR56_BOND() {
		return R56_BOND;
	}
	public void setR56_BOND(String r56_BOND) {
		R56_BOND = r56_BOND;
	}
	public String getR56_CP() {
		return R56_CP;
	}
	public void setR56_CP(String r56_CP) {
		R56_CP = r56_CP;
	}
	public String getR56_EQULITY() {
		return R56_EQULITY;
	}
	public void setR56_EQULITY(String r56_EQULITY) {
		R56_EQULITY = r56_EQULITY;
	}
	public String getR56_FOREX() {
		return R56_FOREX;
	}
	public void setR56_FOREX(String r56_FOREX) {
		R56_FOREX = r56_FOREX;
	}
	public String getR56_OTHERS() {
		return R56_OTHERS;
	}
	public void setR56_OTHERS(String r56_OTHERS) {
		R56_OTHERS = r56_OTHERS;
	}
	public String getR56_INT_BANK() {
		return R56_INT_BANK;
	}
	public void setR56_INT_BANK(String r56_INT_BANK) {
		R56_INT_BANK = r56_INT_BANK;
	}
	public String getR56_DERIVATIVE() {
		return R56_DERIVATIVE;
	}
	public void setR56_DERIVATIVE(String r56_DERIVATIVE) {
		R56_DERIVATIVE = r56_DERIVATIVE;
	}
	public String getR57_GROUP_CODE() {
		return R57_GROUP_CODE;
	}
	public void setR57_GROUP_CODE(String r57_GROUP_CODE) {
		R57_GROUP_CODE = r57_GROUP_CODE;
	}
	public String getR57_GROUP_NAME() {
		return R57_GROUP_NAME;
	}
	public void setR57_GROUP_NAME(String r57_GROUP_NAME) {
		R57_GROUP_NAME = r57_GROUP_NAME;
	}
	public BigDecimal getR57_CRM() {
		return R57_CRM;
	}
	public void setR57_CRM(BigDecimal r57_CRM) {
		R57_CRM = r57_CRM;
	}
	public BigDecimal getR57_NFBLT() {
		return R57_NFBLT;
	}
	public void setR57_NFBLT(BigDecimal r57_NFBLT) {
		R57_NFBLT = r57_NFBLT;
	}
	public BigDecimal getR57_NFBOS() {
		return R57_NFBOS;
	}
	public void setR57_NFBOS(BigDecimal r57_NFBOS) {
		R57_NFBOS = r57_NFBOS;
	}
	public BigDecimal getR57_CRM_2() {
		return R57_CRM_2;
	}
	public void setR57_CRM_2(BigDecimal r57_CRM_2) {
		R57_CRM_2 = r57_CRM_2;
	}
	public BigDecimal getR57_NFB() {
		return R57_NFB;
	}
	public void setR57_NFB(BigDecimal r57_NFB) {
		R57_NFB = r57_NFB;
	}
	public String getR57_BOND() {
		return R57_BOND;
	}
	public void setR57_BOND(String r57_BOND) {
		R57_BOND = r57_BOND;
	}
	public String getR57_CP() {
		return R57_CP;
	}
	public void setR57_CP(String r57_CP) {
		R57_CP = r57_CP;
	}
	public String getR57_EQULITY() {
		return R57_EQULITY;
	}
	public void setR57_EQULITY(String r57_EQULITY) {
		R57_EQULITY = r57_EQULITY;
	}
	public String getR57_FOREX() {
		return R57_FOREX;
	}
	public void setR57_FOREX(String r57_FOREX) {
		R57_FOREX = r57_FOREX;
	}
	public String getR57_OTHERS() {
		return R57_OTHERS;
	}
	public void setR57_OTHERS(String r57_OTHERS) {
		R57_OTHERS = r57_OTHERS;
	}
	public String getR57_INT_BANK() {
		return R57_INT_BANK;
	}
	public void setR57_INT_BANK(String r57_INT_BANK) {
		R57_INT_BANK = r57_INT_BANK;
	}
	public String getR57_DERIVATIVE() {
		return R57_DERIVATIVE;
	}
	public void setR57_DERIVATIVE(String r57_DERIVATIVE) {
		R57_DERIVATIVE = r57_DERIVATIVE;
	}
	public String getR58_GROUP_CODE() {
		return R58_GROUP_CODE;
	}
	public void setR58_GROUP_CODE(String r58_GROUP_CODE) {
		R58_GROUP_CODE = r58_GROUP_CODE;
	}
	public String getR58_GROUP_NAME() {
		return R58_GROUP_NAME;
	}
	public void setR58_GROUP_NAME(String r58_GROUP_NAME) {
		R58_GROUP_NAME = r58_GROUP_NAME;
	}
	public BigDecimal getR58_CRM() {
		return R58_CRM;
	}
	public void setR58_CRM(BigDecimal r58_CRM) {
		R58_CRM = r58_CRM;
	}
	public BigDecimal getR58_NFBLT() {
		return R58_NFBLT;
	}
	public void setR58_NFBLT(BigDecimal r58_NFBLT) {
		R58_NFBLT = r58_NFBLT;
	}
	public BigDecimal getR58_NFBOS() {
		return R58_NFBOS;
	}
	public void setR58_NFBOS(BigDecimal r58_NFBOS) {
		R58_NFBOS = r58_NFBOS;
	}
	public BigDecimal getR58_CRM_2() {
		return R58_CRM_2;
	}
	public void setR58_CRM_2(BigDecimal r58_CRM_2) {
		R58_CRM_2 = r58_CRM_2;
	}
	public BigDecimal getR58_NFB() {
		return R58_NFB;
	}
	public void setR58_NFB(BigDecimal r58_NFB) {
		R58_NFB = r58_NFB;
	}
	public String getR58_BOND() {
		return R58_BOND;
	}
	public void setR58_BOND(String r58_BOND) {
		R58_BOND = r58_BOND;
	}
	public String getR58_CP() {
		return R58_CP;
	}
	public void setR58_CP(String r58_CP) {
		R58_CP = r58_CP;
	}
	public String getR58_EQULITY() {
		return R58_EQULITY;
	}
	public void setR58_EQULITY(String r58_EQULITY) {
		R58_EQULITY = r58_EQULITY;
	}
	public String getR58_FOREX() {
		return R58_FOREX;
	}
	public void setR58_FOREX(String r58_FOREX) {
		R58_FOREX = r58_FOREX;
	}
	public String getR58_OTHERS() {
		return R58_OTHERS;
	}
	public void setR58_OTHERS(String r58_OTHERS) {
		R58_OTHERS = r58_OTHERS;
	}
	public String getR58_INT_BANK() {
		return R58_INT_BANK;
	}
	public void setR58_INT_BANK(String r58_INT_BANK) {
		R58_INT_BANK = r58_INT_BANK;
	}
	public String getR58_DERIVATIVE() {
		return R58_DERIVATIVE;
	}
	public void setR58_DERIVATIVE(String r58_DERIVATIVE) {
		R58_DERIVATIVE = r58_DERIVATIVE;
	}
	public String getR59_GROUP_CODE() {
		return R59_GROUP_CODE;
	}
	public void setR59_GROUP_CODE(String r59_GROUP_CODE) {
		R59_GROUP_CODE = r59_GROUP_CODE;
	}
	public String getR59_GROUP_NAME() {
		return R59_GROUP_NAME;
	}
	public void setR59_GROUP_NAME(String r59_GROUP_NAME) {
		R59_GROUP_NAME = r59_GROUP_NAME;
	}
	public BigDecimal getR59_CRM() {
		return R59_CRM;
	}
	public void setR59_CRM(BigDecimal r59_CRM) {
		R59_CRM = r59_CRM;
	}
	public BigDecimal getR59_NFBLT() {
		return R59_NFBLT;
	}
	public void setR59_NFBLT(BigDecimal r59_NFBLT) {
		R59_NFBLT = r59_NFBLT;
	}
	public BigDecimal getR59_NFBOS() {
		return R59_NFBOS;
	}
	public void setR59_NFBOS(BigDecimal r59_NFBOS) {
		R59_NFBOS = r59_NFBOS;
	}
	public BigDecimal getR59_CRM_2() {
		return R59_CRM_2;
	}
	public void setR59_CRM_2(BigDecimal r59_CRM_2) {
		R59_CRM_2 = r59_CRM_2;
	}
	public BigDecimal getR59_NFB() {
		return R59_NFB;
	}
	public void setR59_NFB(BigDecimal r59_NFB) {
		R59_NFB = r59_NFB;
	}
	public String getR59_BOND() {
		return R59_BOND;
	}
	public void setR59_BOND(String r59_BOND) {
		R59_BOND = r59_BOND;
	}
	public String getR59_CP() {
		return R59_CP;
	}
	public void setR59_CP(String r59_CP) {
		R59_CP = r59_CP;
	}
	public String getR59_EQULITY() {
		return R59_EQULITY;
	}
	public void setR59_EQULITY(String r59_EQULITY) {
		R59_EQULITY = r59_EQULITY;
	}
	public String getR59_FOREX() {
		return R59_FOREX;
	}
	public void setR59_FOREX(String r59_FOREX) {
		R59_FOREX = r59_FOREX;
	}
	public String getR59_OTHERS() {
		return R59_OTHERS;
	}
	public void setR59_OTHERS(String r59_OTHERS) {
		R59_OTHERS = r59_OTHERS;
	}
	public String getR59_INT_BANK() {
		return R59_INT_BANK;
	}
	public void setR59_INT_BANK(String r59_INT_BANK) {
		R59_INT_BANK = r59_INT_BANK;
	}
	public String getR59_DERIVATIVE() {
		return R59_DERIVATIVE;
	}
	public void setR59_DERIVATIVE(String r59_DERIVATIVE) {
		R59_DERIVATIVE = r59_DERIVATIVE;
	}
	public String getR60_GROUP_CODE() {
		return R60_GROUP_CODE;
	}
	public void setR60_GROUP_CODE(String r60_GROUP_CODE) {
		R60_GROUP_CODE = r60_GROUP_CODE;
	}
	public String getR60_GROUP_NAME() {
		return R60_GROUP_NAME;
	}
	public void setR60_GROUP_NAME(String r60_GROUP_NAME) {
		R60_GROUP_NAME = r60_GROUP_NAME;
	}
	public BigDecimal getR60_CRM() {
		return R60_CRM;
	}
	public void setR60_CRM(BigDecimal r60_CRM) {
		R60_CRM = r60_CRM;
	}
	public BigDecimal getR60_NFBLT() {
		return R60_NFBLT;
	}
	public void setR60_NFBLT(BigDecimal r60_NFBLT) {
		R60_NFBLT = r60_NFBLT;
	}
	public BigDecimal getR60_NFBOS() {
		return R60_NFBOS;
	}
	public void setR60_NFBOS(BigDecimal r60_NFBOS) {
		R60_NFBOS = r60_NFBOS;
	}
	public BigDecimal getR60_CRM_2() {
		return R60_CRM_2;
	}
	public void setR60_CRM_2(BigDecimal r60_CRM_2) {
		R60_CRM_2 = r60_CRM_2;
	}
	public BigDecimal getR60_NFB() {
		return R60_NFB;
	}
	public void setR60_NFB(BigDecimal r60_NFB) {
		R60_NFB = r60_NFB;
	}
	public String getR60_BOND() {
		return R60_BOND;
	}
	public void setR60_BOND(String r60_BOND) {
		R60_BOND = r60_BOND;
	}
	public String getR60_CP() {
		return R60_CP;
	}
	public void setR60_CP(String r60_CP) {
		R60_CP = r60_CP;
	}
	public String getR60_EQULITY() {
		return R60_EQULITY;
	}
	public void setR60_EQULITY(String r60_EQULITY) {
		R60_EQULITY = r60_EQULITY;
	}
	public String getR60_FOREX() {
		return R60_FOREX;
	}
	public void setR60_FOREX(String r60_FOREX) {
		R60_FOREX = r60_FOREX;
	}
	public String getR60_OTHERS() {
		return R60_OTHERS;
	}
	public void setR60_OTHERS(String r60_OTHERS) {
		R60_OTHERS = r60_OTHERS;
	}
	public String getR60_INT_BANK() {
		return R60_INT_BANK;
	}
	public void setR60_INT_BANK(String r60_INT_BANK) {
		R60_INT_BANK = r60_INT_BANK;
	}
	public String getR60_DERIVATIVE() {
		return R60_DERIVATIVE;
	}
	public void setR60_DERIVATIVE(String r60_DERIVATIVE) {
		R60_DERIVATIVE = r60_DERIVATIVE;
	}
	public String getR61_GROUP_CODE() {
		return R61_GROUP_CODE;
	}
	public void setR61_GROUP_CODE(String r61_GROUP_CODE) {
		R61_GROUP_CODE = r61_GROUP_CODE;
	}
	public String getR61_GROUP_NAME() {
		return R61_GROUP_NAME;
	}
	public void setR61_GROUP_NAME(String r61_GROUP_NAME) {
		R61_GROUP_NAME = r61_GROUP_NAME;
	}
	public BigDecimal getR61_CRM() {
		return R61_CRM;
	}
	public void setR61_CRM(BigDecimal r61_CRM) {
		R61_CRM = r61_CRM;
	}
	public BigDecimal getR61_NFBLT() {
		return R61_NFBLT;
	}
	public void setR61_NFBLT(BigDecimal r61_NFBLT) {
		R61_NFBLT = r61_NFBLT;
	}
	public BigDecimal getR61_NFBOS() {
		return R61_NFBOS;
	}
	public void setR61_NFBOS(BigDecimal r61_NFBOS) {
		R61_NFBOS = r61_NFBOS;
	}
	public BigDecimal getR61_CRM_2() {
		return R61_CRM_2;
	}
	public void setR61_CRM_2(BigDecimal r61_CRM_2) {
		R61_CRM_2 = r61_CRM_2;
	}
	public BigDecimal getR61_NFB() {
		return R61_NFB;
	}
	public void setR61_NFB(BigDecimal r61_NFB) {
		R61_NFB = r61_NFB;
	}
	public String getR61_BOND() {
		return R61_BOND;
	}
	public void setR61_BOND(String r61_BOND) {
		R61_BOND = r61_BOND;
	}
	public String getR61_CP() {
		return R61_CP;
	}
	public void setR61_CP(String r61_CP) {
		R61_CP = r61_CP;
	}
	public String getR61_EQULITY() {
		return R61_EQULITY;
	}
	public void setR61_EQULITY(String r61_EQULITY) {
		R61_EQULITY = r61_EQULITY;
	}
	public String getR61_FOREX() {
		return R61_FOREX;
	}
	public void setR61_FOREX(String r61_FOREX) {
		R61_FOREX = r61_FOREX;
	}
	public String getR61_OTHERS() {
		return R61_OTHERS;
	}
	public void setR61_OTHERS(String r61_OTHERS) {
		R61_OTHERS = r61_OTHERS;
	}
	public String getR61_INT_BANK() {
		return R61_INT_BANK;
	}
	public void setR61_INT_BANK(String r61_INT_BANK) {
		R61_INT_BANK = r61_INT_BANK;
	}
	public String getR61_DERIVATIVE() {
		return R61_DERIVATIVE;
	}
	public void setR61_DERIVATIVE(String r61_DERIVATIVE) {
		R61_DERIVATIVE = r61_DERIVATIVE;
	}
	public String getR62_GROUP_CODE() {
		return R62_GROUP_CODE;
	}
	public void setR62_GROUP_CODE(String r62_GROUP_CODE) {
		R62_GROUP_CODE = r62_GROUP_CODE;
	}
	public String getR62_GROUP_NAME() {
		return R62_GROUP_NAME;
	}
	public void setR62_GROUP_NAME(String r62_GROUP_NAME) {
		R62_GROUP_NAME = r62_GROUP_NAME;
	}
	public BigDecimal getR62_CRM() {
		return R62_CRM;
	}
	public void setR62_CRM(BigDecimal r62_CRM) {
		R62_CRM = r62_CRM;
	}
	public BigDecimal getR62_NFBLT() {
		return R62_NFBLT;
	}
	public void setR62_NFBLT(BigDecimal r62_NFBLT) {
		R62_NFBLT = r62_NFBLT;
	}
	public BigDecimal getR62_NFBOS() {
		return R62_NFBOS;
	}
	public void setR62_NFBOS(BigDecimal r62_NFBOS) {
		R62_NFBOS = r62_NFBOS;
	}
	public BigDecimal getR62_CRM_2() {
		return R62_CRM_2;
	}
	public void setR62_CRM_2(BigDecimal r62_CRM_2) {
		R62_CRM_2 = r62_CRM_2;
	}
	public BigDecimal getR62_NFB() {
		return R62_NFB;
	}
	public void setR62_NFB(BigDecimal r62_NFB) {
		R62_NFB = r62_NFB;
	}
	public String getR62_BOND() {
		return R62_BOND;
	}
	public void setR62_BOND(String r62_BOND) {
		R62_BOND = r62_BOND;
	}
	public String getR62_CP() {
		return R62_CP;
	}
	public void setR62_CP(String r62_CP) {
		R62_CP = r62_CP;
	}
	public String getR62_EQULITY() {
		return R62_EQULITY;
	}
	public void setR62_EQULITY(String r62_EQULITY) {
		R62_EQULITY = r62_EQULITY;
	}
	public String getR62_FOREX() {
		return R62_FOREX;
	}
	public void setR62_FOREX(String r62_FOREX) {
		R62_FOREX = r62_FOREX;
	}
	public String getR62_OTHERS() {
		return R62_OTHERS;
	}
	public void setR62_OTHERS(String r62_OTHERS) {
		R62_OTHERS = r62_OTHERS;
	}
	public String getR62_INT_BANK() {
		return R62_INT_BANK;
	}
	public void setR62_INT_BANK(String r62_INT_BANK) {
		R62_INT_BANK = r62_INT_BANK;
	}
	public String getR62_DERIVATIVE() {
		return R62_DERIVATIVE;
	}
	public void setR62_DERIVATIVE(String r62_DERIVATIVE) {
		R62_DERIVATIVE = r62_DERIVATIVE;
	}
	public String getR63_GROUP_CODE() {
		return R63_GROUP_CODE;
	}
	public void setR63_GROUP_CODE(String r63_GROUP_CODE) {
		R63_GROUP_CODE = r63_GROUP_CODE;
	}
	public String getR63_GROUP_NAME() {
		return R63_GROUP_NAME;
	}
	public void setR63_GROUP_NAME(String r63_GROUP_NAME) {
		R63_GROUP_NAME = r63_GROUP_NAME;
	}
	public BigDecimal getR63_CRM() {
		return R63_CRM;
	}
	public void setR63_CRM(BigDecimal r63_CRM) {
		R63_CRM = r63_CRM;
	}
	public BigDecimal getR63_NFBLT() {
		return R63_NFBLT;
	}
	public void setR63_NFBLT(BigDecimal r63_NFBLT) {
		R63_NFBLT = r63_NFBLT;
	}
	public BigDecimal getR63_NFBOS() {
		return R63_NFBOS;
	}
	public void setR63_NFBOS(BigDecimal r63_NFBOS) {
		R63_NFBOS = r63_NFBOS;
	}
	public BigDecimal getR63_CRM_2() {
		return R63_CRM_2;
	}
	public void setR63_CRM_2(BigDecimal r63_CRM_2) {
		R63_CRM_2 = r63_CRM_2;
	}
	public BigDecimal getR63_NFB() {
		return R63_NFB;
	}
	public void setR63_NFB(BigDecimal r63_NFB) {
		R63_NFB = r63_NFB;
	}
	public String getR63_BOND() {
		return R63_BOND;
	}
	public void setR63_BOND(String r63_BOND) {
		R63_BOND = r63_BOND;
	}
	public String getR63_CP() {
		return R63_CP;
	}
	public void setR63_CP(String r63_CP) {
		R63_CP = r63_CP;
	}
	public String getR63_EQULITY() {
		return R63_EQULITY;
	}
	public void setR63_EQULITY(String r63_EQULITY) {
		R63_EQULITY = r63_EQULITY;
	}
	public String getR63_FOREX() {
		return R63_FOREX;
	}
	public void setR63_FOREX(String r63_FOREX) {
		R63_FOREX = r63_FOREX;
	}
	public String getR63_OTHERS() {
		return R63_OTHERS;
	}
	public void setR63_OTHERS(String r63_OTHERS) {
		R63_OTHERS = r63_OTHERS;
	}
	public String getR63_INT_BANK() {
		return R63_INT_BANK;
	}
	public void setR63_INT_BANK(String r63_INT_BANK) {
		R63_INT_BANK = r63_INT_BANK;
	}
	public String getR63_DERIVATIVE() {
		return R63_DERIVATIVE;
	}
	public void setR63_DERIVATIVE(String r63_DERIVATIVE) {
		R63_DERIVATIVE = r63_DERIVATIVE;
	}
	public String getR64_GROUP_CODE() {
		return R64_GROUP_CODE;
	}
	public void setR64_GROUP_CODE(String r64_GROUP_CODE) {
		R64_GROUP_CODE = r64_GROUP_CODE;
	}
	public String getR64_GROUP_NAME() {
		return R64_GROUP_NAME;
	}
	public void setR64_GROUP_NAME(String r64_GROUP_NAME) {
		R64_GROUP_NAME = r64_GROUP_NAME;
	}
	public BigDecimal getR64_CRM() {
		return R64_CRM;
	}
	public void setR64_CRM(BigDecimal r64_CRM) {
		R64_CRM = r64_CRM;
	}
	public BigDecimal getR64_NFBLT() {
		return R64_NFBLT;
	}
	public void setR64_NFBLT(BigDecimal r64_NFBLT) {
		R64_NFBLT = r64_NFBLT;
	}
	public BigDecimal getR64_NFBOS() {
		return R64_NFBOS;
	}
	public void setR64_NFBOS(BigDecimal r64_NFBOS) {
		R64_NFBOS = r64_NFBOS;
	}
	public BigDecimal getR64_CRM_2() {
		return R64_CRM_2;
	}
	public void setR64_CRM_2(BigDecimal r64_CRM_2) {
		R64_CRM_2 = r64_CRM_2;
	}
	public BigDecimal getR64_NFB() {
		return R64_NFB;
	}
	public void setR64_NFB(BigDecimal r64_NFB) {
		R64_NFB = r64_NFB;
	}
	public String getR64_BOND() {
		return R64_BOND;
	}
	public void setR64_BOND(String r64_BOND) {
		R64_BOND = r64_BOND;
	}
	public String getR64_CP() {
		return R64_CP;
	}
	public void setR64_CP(String r64_CP) {
		R64_CP = r64_CP;
	}
	public String getR64_EQULITY() {
		return R64_EQULITY;
	}
	public void setR64_EQULITY(String r64_EQULITY) {
		R64_EQULITY = r64_EQULITY;
	}
	public String getR64_FOREX() {
		return R64_FOREX;
	}
	public void setR64_FOREX(String r64_FOREX) {
		R64_FOREX = r64_FOREX;
	}
	public String getR64_OTHERS() {
		return R64_OTHERS;
	}
	public void setR64_OTHERS(String r64_OTHERS) {
		R64_OTHERS = r64_OTHERS;
	}
	public String getR64_INT_BANK() {
		return R64_INT_BANK;
	}
	public void setR64_INT_BANK(String r64_INT_BANK) {
		R64_INT_BANK = r64_INT_BANK;
	}
	public String getR64_DERIVATIVE() {
		return R64_DERIVATIVE;
	}
	public void setR64_DERIVATIVE(String r64_DERIVATIVE) {
		R64_DERIVATIVE = r64_DERIVATIVE;
	}
	public String getR65_GROUP_CODE() {
		return R65_GROUP_CODE;
	}
	public void setR65_GROUP_CODE(String r65_GROUP_CODE) {
		R65_GROUP_CODE = r65_GROUP_CODE;
	}
	public String getR65_GROUP_NAME() {
		return R65_GROUP_NAME;
	}
	public void setR65_GROUP_NAME(String r65_GROUP_NAME) {
		R65_GROUP_NAME = r65_GROUP_NAME;
	}
	public BigDecimal getR65_CRM() {
		return R65_CRM;
	}
	public void setR65_CRM(BigDecimal r65_CRM) {
		R65_CRM = r65_CRM;
	}
	public BigDecimal getR65_NFBLT() {
		return R65_NFBLT;
	}
	public void setR65_NFBLT(BigDecimal r65_NFBLT) {
		R65_NFBLT = r65_NFBLT;
	}
	public BigDecimal getR65_NFBOS() {
		return R65_NFBOS;
	}
	public void setR65_NFBOS(BigDecimal r65_NFBOS) {
		R65_NFBOS = r65_NFBOS;
	}
	public BigDecimal getR65_CRM_2() {
		return R65_CRM_2;
	}
	public void setR65_CRM_2(BigDecimal r65_CRM_2) {
		R65_CRM_2 = r65_CRM_2;
	}
	public BigDecimal getR65_NFB() {
		return R65_NFB;
	}
	public void setR65_NFB(BigDecimal r65_NFB) {
		R65_NFB = r65_NFB;
	}
	public String getR65_BOND() {
		return R65_BOND;
	}
	public void setR65_BOND(String r65_BOND) {
		R65_BOND = r65_BOND;
	}
	public String getR65_CP() {
		return R65_CP;
	}
	public void setR65_CP(String r65_CP) {
		R65_CP = r65_CP;
	}
	public String getR65_EQULITY() {
		return R65_EQULITY;
	}
	public void setR65_EQULITY(String r65_EQULITY) {
		R65_EQULITY = r65_EQULITY;
	}
	public String getR65_FOREX() {
		return R65_FOREX;
	}
	public void setR65_FOREX(String r65_FOREX) {
		R65_FOREX = r65_FOREX;
	}
	public String getR65_OTHERS() {
		return R65_OTHERS;
	}
	public void setR65_OTHERS(String r65_OTHERS) {
		R65_OTHERS = r65_OTHERS;
	}
	public String getR65_INT_BANK() {
		return R65_INT_BANK;
	}
	public void setR65_INT_BANK(String r65_INT_BANK) {
		R65_INT_BANK = r65_INT_BANK;
	}
	public String getR65_DERIVATIVE() {
		return R65_DERIVATIVE;
	}
	public void setR65_DERIVATIVE(String r65_DERIVATIVE) {
		R65_DERIVATIVE = r65_DERIVATIVE;
	}
	public String getR66_GROUP_CODE() {
		return R66_GROUP_CODE;
	}
	public void setR66_GROUP_CODE(String r66_GROUP_CODE) {
		R66_GROUP_CODE = r66_GROUP_CODE;
	}
	public String getR66_GROUP_NAME() {
		return R66_GROUP_NAME;
	}
	public void setR66_GROUP_NAME(String r66_GROUP_NAME) {
		R66_GROUP_NAME = r66_GROUP_NAME;
	}
	public BigDecimal getR66_CRM() {
		return R66_CRM;
	}
	public void setR66_CRM(BigDecimal r66_CRM) {
		R66_CRM = r66_CRM;
	}
	public BigDecimal getR66_NFBLT() {
		return R66_NFBLT;
	}
	public void setR66_NFBLT(BigDecimal r66_NFBLT) {
		R66_NFBLT = r66_NFBLT;
	}
	public BigDecimal getR66_NFBOS() {
		return R66_NFBOS;
	}
	public void setR66_NFBOS(BigDecimal r66_NFBOS) {
		R66_NFBOS = r66_NFBOS;
	}
	public BigDecimal getR66_CRM_2() {
		return R66_CRM_2;
	}
	public void setR66_CRM_2(BigDecimal r66_CRM_2) {
		R66_CRM_2 = r66_CRM_2;
	}
	public BigDecimal getR66_NFB() {
		return R66_NFB;
	}
	public void setR66_NFB(BigDecimal r66_NFB) {
		R66_NFB = r66_NFB;
	}
	public String getR66_BOND() {
		return R66_BOND;
	}
	public void setR66_BOND(String r66_BOND) {
		R66_BOND = r66_BOND;
	}
	public String getR66_CP() {
		return R66_CP;
	}
	public void setR66_CP(String r66_CP) {
		R66_CP = r66_CP;
	}
	public String getR66_EQULITY() {
		return R66_EQULITY;
	}
	public void setR66_EQULITY(String r66_EQULITY) {
		R66_EQULITY = r66_EQULITY;
	}
	public String getR66_FOREX() {
		return R66_FOREX;
	}
	public void setR66_FOREX(String r66_FOREX) {
		R66_FOREX = r66_FOREX;
	}
	public String getR66_OTHERS() {
		return R66_OTHERS;
	}
	public void setR66_OTHERS(String r66_OTHERS) {
		R66_OTHERS = r66_OTHERS;
	}
	public String getR66_INT_BANK() {
		return R66_INT_BANK;
	}
	public void setR66_INT_BANK(String r66_INT_BANK) {
		R66_INT_BANK = r66_INT_BANK;
	}
	public String getR66_DERIVATIVE() {
		return R66_DERIVATIVE;
	}
	public void setR66_DERIVATIVE(String r66_DERIVATIVE) {
		R66_DERIVATIVE = r66_DERIVATIVE;
	}
	public String getR67_GROUP_CODE() {
		return R67_GROUP_CODE;
	}
	public void setR67_GROUP_CODE(String r67_GROUP_CODE) {
		R67_GROUP_CODE = r67_GROUP_CODE;
	}
	public String getR67_GROUP_NAME() {
		return R67_GROUP_NAME;
	}
	public void setR67_GROUP_NAME(String r67_GROUP_NAME) {
		R67_GROUP_NAME = r67_GROUP_NAME;
	}
	public BigDecimal getR67_CRM() {
		return R67_CRM;
	}
	public void setR67_CRM(BigDecimal r67_CRM) {
		R67_CRM = r67_CRM;
	}
	public BigDecimal getR67_NFBLT() {
		return R67_NFBLT;
	}
	public void setR67_NFBLT(BigDecimal r67_NFBLT) {
		R67_NFBLT = r67_NFBLT;
	}
	public BigDecimal getR67_NFBOS() {
		return R67_NFBOS;
	}
	public void setR67_NFBOS(BigDecimal r67_NFBOS) {
		R67_NFBOS = r67_NFBOS;
	}
	public BigDecimal getR67_CRM_2() {
		return R67_CRM_2;
	}
	public void setR67_CRM_2(BigDecimal r67_CRM_2) {
		R67_CRM_2 = r67_CRM_2;
	}
	public BigDecimal getR67_NFB() {
		return R67_NFB;
	}
	public void setR67_NFB(BigDecimal r67_NFB) {
		R67_NFB = r67_NFB;
	}
	public String getR67_BOND() {
		return R67_BOND;
	}
	public void setR67_BOND(String r67_BOND) {
		R67_BOND = r67_BOND;
	}
	public String getR67_CP() {
		return R67_CP;
	}
	public void setR67_CP(String r67_CP) {
		R67_CP = r67_CP;
	}
	public String getR67_EQULITY() {
		return R67_EQULITY;
	}
	public void setR67_EQULITY(String r67_EQULITY) {
		R67_EQULITY = r67_EQULITY;
	}
	public String getR67_FOREX() {
		return R67_FOREX;
	}
	public void setR67_FOREX(String r67_FOREX) {
		R67_FOREX = r67_FOREX;
	}
	public String getR67_OTHERS() {
		return R67_OTHERS;
	}
	public void setR67_OTHERS(String r67_OTHERS) {
		R67_OTHERS = r67_OTHERS;
	}
	public String getR67_INT_BANK() {
		return R67_INT_BANK;
	}
	public void setR67_INT_BANK(String r67_INT_BANK) {
		R67_INT_BANK = r67_INT_BANK;
	}
	public String getR67_DERIVATIVE() {
		return R67_DERIVATIVE;
	}
	public void setR67_DERIVATIVE(String r67_DERIVATIVE) {
		R67_DERIVATIVE = r67_DERIVATIVE;
	}
	public String getR68_GROUP_CODE() {
		return R68_GROUP_CODE;
	}
	public void setR68_GROUP_CODE(String r68_GROUP_CODE) {
		R68_GROUP_CODE = r68_GROUP_CODE;
	}
	public String getR68_GROUP_NAME() {
		return R68_GROUP_NAME;
	}
	public void setR68_GROUP_NAME(String r68_GROUP_NAME) {
		R68_GROUP_NAME = r68_GROUP_NAME;
	}
	public BigDecimal getR68_CRM() {
		return R68_CRM;
	}
	public void setR68_CRM(BigDecimal r68_CRM) {
		R68_CRM = r68_CRM;
	}
	public BigDecimal getR68_NFBLT() {
		return R68_NFBLT;
	}
	public void setR68_NFBLT(BigDecimal r68_NFBLT) {
		R68_NFBLT = r68_NFBLT;
	}
	public BigDecimal getR68_NFBOS() {
		return R68_NFBOS;
	}
	public void setR68_NFBOS(BigDecimal r68_NFBOS) {
		R68_NFBOS = r68_NFBOS;
	}
	public BigDecimal getR68_CRM_2() {
		return R68_CRM_2;
	}
	public void setR68_CRM_2(BigDecimal r68_CRM_2) {
		R68_CRM_2 = r68_CRM_2;
	}
	public BigDecimal getR68_NFB() {
		return R68_NFB;
	}
	public void setR68_NFB(BigDecimal r68_NFB) {
		R68_NFB = r68_NFB;
	}
	public String getR68_BOND() {
		return R68_BOND;
	}
	public void setR68_BOND(String r68_BOND) {
		R68_BOND = r68_BOND;
	}
	public String getR68_CP() {
		return R68_CP;
	}
	public void setR68_CP(String r68_CP) {
		R68_CP = r68_CP;
	}
	public String getR68_EQULITY() {
		return R68_EQULITY;
	}
	public void setR68_EQULITY(String r68_EQULITY) {
		R68_EQULITY = r68_EQULITY;
	}
	public String getR68_FOREX() {
		return R68_FOREX;
	}
	public void setR68_FOREX(String r68_FOREX) {
		R68_FOREX = r68_FOREX;
	}
	public String getR68_OTHERS() {
		return R68_OTHERS;
	}
	public void setR68_OTHERS(String r68_OTHERS) {
		R68_OTHERS = r68_OTHERS;
	}
	public String getR68_INT_BANK() {
		return R68_INT_BANK;
	}
	public void setR68_INT_BANK(String r68_INT_BANK) {
		R68_INT_BANK = r68_INT_BANK;
	}
	public String getR68_DERIVATIVE() {
		return R68_DERIVATIVE;
	}
	public void setR68_DERIVATIVE(String r68_DERIVATIVE) {
		R68_DERIVATIVE = r68_DERIVATIVE;
	}
	public String getR69_GROUP_CODE() {
		return R69_GROUP_CODE;
	}
	public void setR69_GROUP_CODE(String r69_GROUP_CODE) {
		R69_GROUP_CODE = r69_GROUP_CODE;
	}
	public String getR69_GROUP_NAME() {
		return R69_GROUP_NAME;
	}
	public void setR69_GROUP_NAME(String r69_GROUP_NAME) {
		R69_GROUP_NAME = r69_GROUP_NAME;
	}
	public BigDecimal getR69_CRM() {
		return R69_CRM;
	}
	public void setR69_CRM(BigDecimal r69_CRM) {
		R69_CRM = r69_CRM;
	}
	public BigDecimal getR69_NFBLT() {
		return R69_NFBLT;
	}
	public void setR69_NFBLT(BigDecimal r69_NFBLT) {
		R69_NFBLT = r69_NFBLT;
	}
	public BigDecimal getR69_NFBOS() {
		return R69_NFBOS;
	}
	public void setR69_NFBOS(BigDecimal r69_NFBOS) {
		R69_NFBOS = r69_NFBOS;
	}
	public BigDecimal getR69_CRM_2() {
		return R69_CRM_2;
	}
	public void setR69_CRM_2(BigDecimal r69_CRM_2) {
		R69_CRM_2 = r69_CRM_2;
	}
	public BigDecimal getR69_NFB() {
		return R69_NFB;
	}
	public void setR69_NFB(BigDecimal r69_NFB) {
		R69_NFB = r69_NFB;
	}
	public String getR69_BOND() {
		return R69_BOND;
	}
	public void setR69_BOND(String r69_BOND) {
		R69_BOND = r69_BOND;
	}
	public String getR69_CP() {
		return R69_CP;
	}
	public void setR69_CP(String r69_CP) {
		R69_CP = r69_CP;
	}
	public String getR69_EQULITY() {
		return R69_EQULITY;
	}
	public void setR69_EQULITY(String r69_EQULITY) {
		R69_EQULITY = r69_EQULITY;
	}
	public String getR69_FOREX() {
		return R69_FOREX;
	}
	public void setR69_FOREX(String r69_FOREX) {
		R69_FOREX = r69_FOREX;
	}
	public String getR69_OTHERS() {
		return R69_OTHERS;
	}
	public void setR69_OTHERS(String r69_OTHERS) {
		R69_OTHERS = r69_OTHERS;
	}
	public String getR69_INT_BANK() {
		return R69_INT_BANK;
	}
	public void setR69_INT_BANK(String r69_INT_BANK) {
		R69_INT_BANK = r69_INT_BANK;
	}
	public String getR69_DERIVATIVE() {
		return R69_DERIVATIVE;
	}
	public void setR69_DERIVATIVE(String r69_DERIVATIVE) {
		R69_DERIVATIVE = r69_DERIVATIVE;
	}
	public String getR70_GROUP_CODE() {
		return R70_GROUP_CODE;
	}
	public void setR70_GROUP_CODE(String r70_GROUP_CODE) {
		R70_GROUP_CODE = r70_GROUP_CODE;
	}
	public String getR70_GROUP_NAME() {
		return R70_GROUP_NAME;
	}
	public void setR70_GROUP_NAME(String r70_GROUP_NAME) {
		R70_GROUP_NAME = r70_GROUP_NAME;
	}
	public BigDecimal getR70_CRM() {
		return R70_CRM;
	}
	public void setR70_CRM(BigDecimal r70_CRM) {
		R70_CRM = r70_CRM;
	}
	public BigDecimal getR70_NFBLT() {
		return R70_NFBLT;
	}
	public void setR70_NFBLT(BigDecimal r70_NFBLT) {
		R70_NFBLT = r70_NFBLT;
	}
	public BigDecimal getR70_NFBOS() {
		return R70_NFBOS;
	}
	public void setR70_NFBOS(BigDecimal r70_NFBOS) {
		R70_NFBOS = r70_NFBOS;
	}
	public BigDecimal getR70_CRM_2() {
		return R70_CRM_2;
	}
	public void setR70_CRM_2(BigDecimal r70_CRM_2) {
		R70_CRM_2 = r70_CRM_2;
	}
	public BigDecimal getR70_NFB() {
		return R70_NFB;
	}
	public void setR70_NFB(BigDecimal r70_NFB) {
		R70_NFB = r70_NFB;
	}
	public String getR70_BOND() {
		return R70_BOND;
	}
	public void setR70_BOND(String r70_BOND) {
		R70_BOND = r70_BOND;
	}
	public String getR70_CP() {
		return R70_CP;
	}
	public void setR70_CP(String r70_CP) {
		R70_CP = r70_CP;
	}
	public String getR70_EQULITY() {
		return R70_EQULITY;
	}
	public void setR70_EQULITY(String r70_EQULITY) {
		R70_EQULITY = r70_EQULITY;
	}
	public String getR70_FOREX() {
		return R70_FOREX;
	}
	public void setR70_FOREX(String r70_FOREX) {
		R70_FOREX = r70_FOREX;
	}
	public String getR70_OTHERS() {
		return R70_OTHERS;
	}
	public void setR70_OTHERS(String r70_OTHERS) {
		R70_OTHERS = r70_OTHERS;
	}
	public String getR70_INT_BANK() {
		return R70_INT_BANK;
	}
	public void setR70_INT_BANK(String r70_INT_BANK) {
		R70_INT_BANK = r70_INT_BANK;
	}
	public String getR70_DERIVATIVE() {
		return R70_DERIVATIVE;
	}
	public void setR70_DERIVATIVE(String r70_DERIVATIVE) {
		R70_DERIVATIVE = r70_DERIVATIVE;
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
	public M_TOP_100_BORROWER_Manual_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    
    
    

}
