package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_NOSVOS_P2_ARCHIVAL_DETAIL")
@IdClass(BRRS_NOSVOS_Summary_PK.class)
public class BrrsMNosvosP2ArchivalDetail {
	@Id
	@Column(name = "REPORT_DATE")
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

	@Column(name = "R1_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R1_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R1_TYPE_OF_ACCOUNT_VOSTRO")
	private String R1_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R1_PURPOSE_VOSTRO")
	private String R1_PURPOSE_VOSTRO;

	@Column(name = "R1_CURRENCY_VOSTRO")
	private String R1_CURRENCY_VOSTRO;

	@Column(name = "R1_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R1_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R1_AMOUNT_TIME_VOSTRO")
	private BigDecimal R1_AMOUNT_TIME_VOSTRO;

	@Column(name = "R2_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R2_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R2_TYPE_OF_ACCOUNT_VOSTRO")
	private String R2_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R2_PURPOSE_VOSTRO")
	private String R2_PURPOSE_VOSTRO;

	@Column(name = "R2_CURRENCY_VOSTRO")
	private String R2_CURRENCY_VOSTRO;

	@Column(name = "R2_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R2_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R2_AMOUNT_TIME_VOSTRO")
	private BigDecimal R2_AMOUNT_TIME_VOSTRO;

	@Column(name = "R3_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R3_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R3_TYPE_OF_ACCOUNT_VOSTRO")
	private String R3_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R3_PURPOSE_VOSTRO")
	private String R3_PURPOSE_VOSTRO;

	@Column(name = "R3_CURRENCY_VOSTRO")
	private String R3_CURRENCY_VOSTRO;

	@Column(name = "R3_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R3_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R3_AMOUNT_TIME_VOSTRO")
	private BigDecimal R3_AMOUNT_TIME_VOSTRO;

	@Column(name = "R4_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R4_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R4_TYPE_OF_ACCOUNT_VOSTRO")
	private String R4_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R4_PURPOSE_VOSTRO")
	private String R4_PURPOSE_VOSTRO;

	@Column(name = "R4_CURRENCY_VOSTRO")
	private String R4_CURRENCY_VOSTRO;

	@Column(name = "R4_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R4_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R4_AMOUNT_TIME_VOSTRO")
	private BigDecimal R4_AMOUNT_TIME_VOSTRO;

	@Column(name = "R5_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R5_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R5_TYPE_OF_ACCOUNT_VOSTRO")
	private String R5_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R5_PURPOSE_VOSTRO")
	private String R5_PURPOSE_VOSTRO;

	@Column(name = "R5_CURRENCY_VOSTRO")
	private String R5_CURRENCY_VOSTRO;

	@Column(name = "R5_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R5_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R5_AMOUNT_TIME_VOSTRO")
	private BigDecimal R5_AMOUNT_TIME_VOSTRO;

	@Column(name = "R6_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R6_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R6_TYPE_OF_ACCOUNT_VOSTRO")
	private String R6_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R6_PURPOSE_VOSTRO")
	private String R6_PURPOSE_VOSTRO;

	@Column(name = "R6_CURRENCY_VOSTRO")
	private String R6_CURRENCY_VOSTRO;

	@Column(name = "R6_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R6_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R6_AMOUNT_TIME_VOSTRO")
	private BigDecimal R6_AMOUNT_TIME_VOSTRO;

	@Column(name = "R7_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R7_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R7_TYPE_OF_ACCOUNT_VOSTRO")
	private String R7_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R7_PURPOSE_VOSTRO")
	private String R7_PURPOSE_VOSTRO;

	@Column(name = "R7_CURRENCY_VOSTRO")
	private String R7_CURRENCY_VOSTRO;

	@Column(name = "R7_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R7_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R7_AMOUNT_TIME_VOSTRO")
	private BigDecimal R7_AMOUNT_TIME_VOSTRO;

	@Column(name = "R8_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R8_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R8_TYPE_OF_ACCOUNT_VOSTRO")
	private String R8_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R8_PURPOSE_VOSTRO")
	private String R8_PURPOSE_VOSTRO;

	@Column(name = "R8_CURRENCY_VOSTRO")
	private String R8_CURRENCY_VOSTRO;

	@Column(name = "R8_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R8_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R8_AMOUNT_TIME_VOSTRO")
	private BigDecimal R8_AMOUNT_TIME_VOSTRO;

	@Column(name = "R9_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R9_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R9_TYPE_OF_ACCOUNT_VOSTRO")
	private String R9_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R9_PURPOSE_VOSTRO")
	private String R9_PURPOSE_VOSTRO;

	@Column(name = "R9_CURRENCY_VOSTRO")
	private String R9_CURRENCY_VOSTRO;

	@Column(name = "R9_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R9_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R9_AMOUNT_TIME_VOSTRO")
	private BigDecimal R9_AMOUNT_TIME_VOSTRO;

	@Column(name = "R10_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R10_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R10_TYPE_OF_ACCOUNT_VOSTRO")
	private String R10_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R10_PURPOSE_VOSTRO")
	private String R10_PURPOSE_VOSTRO;

	@Column(name = "R10_CURRENCY_VOSTRO")
	private String R10_CURRENCY_VOSTRO;

	@Column(name = "R10_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R10_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R10_AMOUNT_TIME_VOSTRO")
	private BigDecimal R10_AMOUNT_TIME_VOSTRO;

	@Column(name = "R11_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R11_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R11_TYPE_OF_ACCOUNT_VOSTRO")
	private String R11_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R11_PURPOSE_VOSTRO")
	private String R11_PURPOSE_VOSTRO;

	@Column(name = "R11_CURRENCY_VOSTRO")
	private String R11_CURRENCY_VOSTRO;

	@Column(name = "R11_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R11_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R11_AMOUNT_TIME_VOSTRO")
	private BigDecimal R11_AMOUNT_TIME_VOSTRO;

	@Column(name = "R12_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R12_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R12_TYPE_OF_ACCOUNT_VOSTRO")
	private String R12_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R12_PURPOSE_VOSTRO")
	private String R12_PURPOSE_VOSTRO;

	@Column(name = "R12_CURRENCY_VOSTRO")
	private String R12_CURRENCY_VOSTRO;

	@Column(name = "R12_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R12_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R12_AMOUNT_TIME_VOSTRO")
	private BigDecimal R12_AMOUNT_TIME_VOSTRO;

	@Column(name = "R13_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R13_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R13_TYPE_OF_ACCOUNT_VOSTRO")
	private String R13_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R13_PURPOSE_VOSTRO")
	private String R13_PURPOSE_VOSTRO;

	@Column(name = "R13_CURRENCY_VOSTRO")
	private String R13_CURRENCY_VOSTRO;

	@Column(name = "R13_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R13_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R13_AMOUNT_TIME_VOSTRO")
	private BigDecimal R13_AMOUNT_TIME_VOSTRO;

	@Column(name = "R14_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R14_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R14_TYPE_OF_ACCOUNT_VOSTRO")
	private String R14_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R14_PURPOSE_VOSTRO")
	private String R14_PURPOSE_VOSTRO;

	@Column(name = "R14_CURRENCY_VOSTRO")
	private String R14_CURRENCY_VOSTRO;

	@Column(name = "R14_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R14_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R14_AMOUNT_TIME_VOSTRO")
	private BigDecimal R14_AMOUNT_TIME_VOSTRO;

	@Column(name = "R15_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R15_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R15_TYPE_OF_ACCOUNT_VOSTRO")
	private String R15_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R15_PURPOSE_VOSTRO")
	private String R15_PURPOSE_VOSTRO;

	@Column(name = "R15_CURRENCY_VOSTRO")
	private String R15_CURRENCY_VOSTRO;

	@Column(name = "R15_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R15_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R15_AMOUNT_TIME_VOSTRO")
	private BigDecimal R15_AMOUNT_TIME_VOSTRO;

	@Column(name = "R16_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R16_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R16_TYPE_OF_ACCOUNT_VOSTRO")
	private String R16_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R16_PURPOSE_VOSTRO")
	private String R16_PURPOSE_VOSTRO;

	@Column(name = "R16_CURRENCY_VOSTRO")
	private String R16_CURRENCY_VOSTRO;

	@Column(name = "R16_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R16_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R16_AMOUNT_TIME_VOSTRO")
	private BigDecimal R16_AMOUNT_TIME_VOSTRO;

	@Column(name = "R17_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R17_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R17_TYPE_OF_ACCOUNT_VOSTRO")
	private String R17_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R17_PURPOSE_VOSTRO")
	private String R17_PURPOSE_VOSTRO;

	@Column(name = "R17_CURRENCY_VOSTRO")
	private String R17_CURRENCY_VOSTRO;

	@Column(name = "R17_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R17_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R17_AMOUNT_TIME_VOSTRO")
	private BigDecimal R17_AMOUNT_TIME_VOSTRO;

	@Column(name = "R18_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R18_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R18_TYPE_OF_ACCOUNT_VOSTRO")
	private String R18_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R18_PURPOSE_VOSTRO")
	private String R18_PURPOSE_VOSTRO;

	@Column(name = "R18_CURRENCY_VOSTRO")
	private String R18_CURRENCY_VOSTRO;

	@Column(name = "R18_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R18_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R18_AMOUNT_TIME_VOSTRO")
	private BigDecimal R18_AMOUNT_TIME_VOSTRO;

	@Column(name = "R19_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R19_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R19_TYPE_OF_ACCOUNT_VOSTRO")
	private String R19_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R19_PURPOSE_VOSTRO")
	private String R19_PURPOSE_VOSTRO;

	@Column(name = "R19_CURRENCY_VOSTRO")
	private String R19_CURRENCY_VOSTRO;

	@Column(name = "R19_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R19_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R19_AMOUNT_TIME_VOSTRO")
	private BigDecimal R19_AMOUNT_TIME_VOSTRO;

	@Column(name = "R20_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R20_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R20_TYPE_OF_ACCOUNT_VOSTRO")
	private String R20_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R20_PURPOSE_VOSTRO")
	private String R20_PURPOSE_VOSTRO;

	@Column(name = "R20_CURRENCY_VOSTRO")
	private String R20_CURRENCY_VOSTRO;

	@Column(name = "R20_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R20_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R20_AMOUNT_TIME_VOSTRO")
	private BigDecimal R20_AMOUNT_TIME_VOSTRO;

	@Column(name = "R21_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R21_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R21_TYPE_OF_ACCOUNT_VOSTRO")
	private String R21_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R21_PURPOSE_VOSTRO")
	private String R21_PURPOSE_VOSTRO;

	@Column(name = "R21_CURRENCY_VOSTRO")
	private String R21_CURRENCY_VOSTRO;

	@Column(name = "R21_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R21_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R21_AMOUNT_TIME_VOSTRO")
	private BigDecimal R21_AMOUNT_TIME_VOSTRO;

	@Column(name = "R22_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R22_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R22_TYPE_OF_ACCOUNT_VOSTRO")
	private String R22_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R22_PURPOSE_VOSTRO")
	private String R22_PURPOSE_VOSTRO;

	@Column(name = "R22_CURRENCY_VOSTRO")
	private String R22_CURRENCY_VOSTRO;

	@Column(name = "R22_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R22_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R22_AMOUNT_TIME_VOSTRO")
	private BigDecimal R22_AMOUNT_TIME_VOSTRO;

	@Column(name = "R23_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R23_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R23_TYPE_OF_ACCOUNT_VOSTRO")
	private String R23_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R23_PURPOSE_VOSTRO")
	private String R23_PURPOSE_VOSTRO;

	@Column(name = "R23_CURRENCY_VOSTRO")
	private String R23_CURRENCY_VOSTRO;

	@Column(name = "R23_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R23_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R23_AMOUNT_TIME_VOSTRO")
	private BigDecimal R23_AMOUNT_TIME_VOSTRO;

	@Column(name = "R24_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R24_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R24_TYPE_OF_ACCOUNT_VOSTRO")
	private String R24_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R24_PURPOSE_VOSTRO")
	private String R24_PURPOSE_VOSTRO;

	@Column(name = "R24_CURRENCY_VOSTRO")
	private String R24_CURRENCY_VOSTRO;

	@Column(name = "R24_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R24_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R24_AMOUNT_TIME_VOSTRO")
	private BigDecimal R24_AMOUNT_TIME_VOSTRO;

	@Column(name = "R25_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R25_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R25_TYPE_OF_ACCOUNT_VOSTRO")
	private String R25_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R25_PURPOSE_VOSTRO")
	private String R25_PURPOSE_VOSTRO;

	@Column(name = "R25_CURRENCY_VOSTRO")
	private String R25_CURRENCY_VOSTRO;

	@Column(name = "R25_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R25_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R25_AMOUNT_TIME_VOSTRO")
	private BigDecimal R25_AMOUNT_TIME_VOSTRO;

	@Column(name = "R26_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R26_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R26_TYPE_OF_ACCOUNT_VOSTRO")
	private String R26_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R26_PURPOSE_VOSTRO")
	private String R26_PURPOSE_VOSTRO;

	@Column(name = "R26_CURRENCY_VOSTRO")
	private String R26_CURRENCY_VOSTRO;

	@Column(name = "R26_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R26_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R26_AMOUNT_TIME_VOSTRO")
	private BigDecimal R26_AMOUNT_TIME_VOSTRO;

	@Column(name = "R27_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R27_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R27_TYPE_OF_ACCOUNT_VOSTRO")
	private String R27_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R27_PURPOSE_VOSTRO")
	private String R27_PURPOSE_VOSTRO;

	@Column(name = "R27_CURRENCY_VOSTRO")
	private String R27_CURRENCY_VOSTRO;

	@Column(name = "R27_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R27_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R27_AMOUNT_TIME_VOSTRO")
	private BigDecimal R27_AMOUNT_TIME_VOSTRO;

	@Column(name = "R28_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R28_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R28_TYPE_OF_ACCOUNT_VOSTRO")
	private String R28_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R28_PURPOSE_VOSTRO")
	private String R28_PURPOSE_VOSTRO;

	@Column(name = "R28_CURRENCY_VOSTRO")
	private String R28_CURRENCY_VOSTRO;

	@Column(name = "R28_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R28_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R28_AMOUNT_TIME_VOSTRO")
	private BigDecimal R28_AMOUNT_TIME_VOSTRO;

	@Column(name = "R29_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R29_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R29_TYPE_OF_ACCOUNT_VOSTRO")
	private String R29_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R29_PURPOSE_VOSTRO")
	private String R29_PURPOSE_VOSTRO;

	@Column(name = "R29_CURRENCY_VOSTRO")
	private String R29_CURRENCY_VOSTRO;

	@Column(name = "R29_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R29_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R29_AMOUNT_TIME_VOSTRO")
	private BigDecimal R29_AMOUNT_TIME_VOSTRO;

	@Column(name = "R30_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R30_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R30_TYPE_OF_ACCOUNT_VOSTRO")
	private String R30_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R30_PURPOSE_VOSTRO")
	private String R30_PURPOSE_VOSTRO;

	@Column(name = "R30_CURRENCY_VOSTRO")
	private String R30_CURRENCY_VOSTRO;

	@Column(name = "R30_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R30_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R30_AMOUNT_TIME_VOSTRO")
	private BigDecimal R30_AMOUNT_TIME_VOSTRO;

	@Column(name = "R31_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R31_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R31_TYPE_OF_ACCOUNT_VOSTRO")
	private String R31_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R31_PURPOSE_VOSTRO")
	private String R31_PURPOSE_VOSTRO;

	@Column(name = "R31_CURRENCY_VOSTRO")
	private String R31_CURRENCY_VOSTRO;

	@Column(name = "R31_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R31_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R31_AMOUNT_TIME_VOSTRO")
	private BigDecimal R31_AMOUNT_TIME_VOSTRO;

	@Column(name = "R32_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R32_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R32_TYPE_OF_ACCOUNT_VOSTRO")
	private String R32_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R32_PURPOSE_VOSTRO")
	private String R32_PURPOSE_VOSTRO;

	@Column(name = "R32_CURRENCY_VOSTRO")
	private String R32_CURRENCY_VOSTRO;

	@Column(name = "R32_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R32_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R32_AMOUNT_TIME_VOSTRO")
	private BigDecimal R32_AMOUNT_TIME_VOSTRO;

	@Column(name = "R33_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R33_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R33_TYPE_OF_ACCOUNT_VOSTRO")
	private String R33_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R33_PURPOSE_VOSTRO")
	private String R33_PURPOSE_VOSTRO;

	@Column(name = "R33_CURRENCY_VOSTRO")
	private String R33_CURRENCY_VOSTRO;

	@Column(name = "R33_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R33_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R33_AMOUNT_TIME_VOSTRO")
	private BigDecimal R33_AMOUNT_TIME_VOSTRO;

	@Column(name = "R34_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R34_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R34_TYPE_OF_ACCOUNT_VOSTRO")
	private String R34_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R34_PURPOSE_VOSTRO")
	private String R34_PURPOSE_VOSTRO;

	@Column(name = "R34_CURRENCY_VOSTRO")
	private String R34_CURRENCY_VOSTRO;

	@Column(name = "R34_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R34_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R34_AMOUNT_TIME_VOSTRO")
	private BigDecimal R34_AMOUNT_TIME_VOSTRO;

	@Column(name = "R35_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R35_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R35_TYPE_OF_ACCOUNT_VOSTRO")
	private String R35_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R35_PURPOSE_VOSTRO")
	private String R35_PURPOSE_VOSTRO;

	@Column(name = "R35_CURRENCY_VOSTRO")
	private String R35_CURRENCY_VOSTRO;

	@Column(name = "R35_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R35_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R35_AMOUNT_TIME_VOSTRO")
	private BigDecimal R35_AMOUNT_TIME_VOSTRO;

	@Column(name = "R36_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R36_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R36_TYPE_OF_ACCOUNT_VOSTRO")
	private String R36_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R36_PURPOSE_VOSTRO")
	private String R36_PURPOSE_VOSTRO;

	@Column(name = "R36_CURRENCY_VOSTRO")
	private String R36_CURRENCY_VOSTRO;

	@Column(name = "R36_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R36_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R36_AMOUNT_TIME_VOSTRO")
	private BigDecimal R36_AMOUNT_TIME_VOSTRO;

	@Column(name = "R37_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R37_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R37_TYPE_OF_ACCOUNT_VOSTRO")
	private String R37_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R37_PURPOSE_VOSTRO")
	private String R37_PURPOSE_VOSTRO;

	@Column(name = "R37_CURRENCY_VOSTRO")
	private String R37_CURRENCY_VOSTRO;

	@Column(name = "R37_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R37_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R37_AMOUNT_TIME_VOSTRO")
	private BigDecimal R37_AMOUNT_TIME_VOSTRO;

	@Column(name = "R38_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R38_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R38_TYPE_OF_ACCOUNT_VOSTRO")
	private String R38_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R38_PURPOSE_VOSTRO")
	private String R38_PURPOSE_VOSTRO;

	@Column(name = "R38_CURRENCY_VOSTRO")
	private String R38_CURRENCY_VOSTRO;

	@Column(name = "R38_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R38_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R38_AMOUNT_TIME_VOSTRO")
	private BigDecimal R38_AMOUNT_TIME_VOSTRO;

	@Column(name = "R39_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R39_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R39_TYPE_OF_ACCOUNT_VOSTRO")
	private String R39_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R39_PURPOSE_VOSTRO")
	private String R39_PURPOSE_VOSTRO;

	@Column(name = "R39_CURRENCY_VOSTRO")
	private String R39_CURRENCY_VOSTRO;

	@Column(name = "R39_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R39_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R39_AMOUNT_TIME_VOSTRO")
	private BigDecimal R39_AMOUNT_TIME_VOSTRO;

	@Column(name = "R40_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R40_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R40_TYPE_OF_ACCOUNT_VOSTRO")
	private String R40_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R40_PURPOSE_VOSTRO")
	private String R40_PURPOSE_VOSTRO;

	@Column(name = "R40_CURRENCY_VOSTRO")
	private String R40_CURRENCY_VOSTRO;

	@Column(name = "R40_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R40_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R40_AMOUNT_TIME_VOSTRO")
	private BigDecimal R40_AMOUNT_TIME_VOSTRO;

	@Column(name = "R41_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R41_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R41_TYPE_OF_ACCOUNT_VOSTRO")
	private String R41_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R41_PURPOSE_VOSTRO")
	private String R41_PURPOSE_VOSTRO;

	@Column(name = "R41_CURRENCY_VOSTRO")
	private String R41_CURRENCY_VOSTRO;

	@Column(name = "R41_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R41_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R41_AMOUNT_TIME_VOSTRO")
	private BigDecimal R41_AMOUNT_TIME_VOSTRO;

	@Column(name = "R42_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R42_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R42_TYPE_OF_ACCOUNT_VOSTRO")
	private String R42_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R42_PURPOSE_VOSTRO")
	private String R42_PURPOSE_VOSTRO;

	@Column(name = "R42_CURRENCY_VOSTRO")
	private String R42_CURRENCY_VOSTRO;

	@Column(name = "R42_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R42_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R42_AMOUNT_TIME_VOSTRO")
	private BigDecimal R42_AMOUNT_TIME_VOSTRO;

	@Column(name = "R43_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R43_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R43_TYPE_OF_ACCOUNT_VOSTRO")
	private String R43_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R43_PURPOSE_VOSTRO")
	private String R43_PURPOSE_VOSTRO;

	@Column(name = "R43_CURRENCY_VOSTRO")
	private String R43_CURRENCY_VOSTRO;

	@Column(name = "R43_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R43_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R43_AMOUNT_TIME_VOSTRO")
	private BigDecimal R43_AMOUNT_TIME_VOSTRO;

	@Column(name = "R44_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R44_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R44_TYPE_OF_ACCOUNT_VOSTRO")
	private String R44_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R44_PURPOSE_VOSTRO")
	private String R44_PURPOSE_VOSTRO;

	@Column(name = "R44_CURRENCY_VOSTRO")
	private String R44_CURRENCY_VOSTRO;

	@Column(name = "R44_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R44_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R44_AMOUNT_TIME_VOSTRO")
	private BigDecimal R44_AMOUNT_TIME_VOSTRO;

	@Column(name = "R45_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R45_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R45_TYPE_OF_ACCOUNT_VOSTRO")
	private String R45_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R45_PURPOSE_VOSTRO")
	private String R45_PURPOSE_VOSTRO;

	@Column(name = "R45_CURRENCY_VOSTRO")
	private String R45_CURRENCY_VOSTRO;

	@Column(name = "R45_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R45_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R45_AMOUNT_TIME_VOSTRO")
	private BigDecimal R45_AMOUNT_TIME_VOSTRO;

	@Column(name = "R46_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R46_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R46_TYPE_OF_ACCOUNT_VOSTRO")
	private String R46_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R46_PURPOSE_VOSTRO")
	private String R46_PURPOSE_VOSTRO;

	@Column(name = "R46_CURRENCY_VOSTRO")
	private String R46_CURRENCY_VOSTRO;

	@Column(name = "R46_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R46_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R46_AMOUNT_TIME_VOSTRO")
	private BigDecimal R46_AMOUNT_TIME_VOSTRO;

	@Column(name = "R47_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R47_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R47_TYPE_OF_ACCOUNT_VOSTRO")
	private String R47_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R47_PURPOSE_VOSTRO")
	private String R47_PURPOSE_VOSTRO;

	@Column(name = "R47_CURRENCY_VOSTRO")
	private String R47_CURRENCY_VOSTRO;

	@Column(name = "R47_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R47_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R47_AMOUNT_TIME_VOSTRO")
	private BigDecimal R47_AMOUNT_TIME_VOSTRO;

	@Column(name = "R48_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R48_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R48_TYPE_OF_ACCOUNT_VOSTRO")
	private String R48_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R48_PURPOSE_VOSTRO")
	private String R48_PURPOSE_VOSTRO;

	@Column(name = "R48_CURRENCY_VOSTRO")
	private String R48_CURRENCY_VOSTRO;

	@Column(name = "R48_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R48_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R48_AMOUNT_TIME_VOSTRO")
	private BigDecimal R48_AMOUNT_TIME_VOSTRO;

	@Column(name = "R49_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R49_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R49_TYPE_OF_ACCOUNT_VOSTRO")
	private String R49_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R49_PURPOSE_VOSTRO")
	private String R49_PURPOSE_VOSTRO;

	@Column(name = "R49_CURRENCY_VOSTRO")
	private String R49_CURRENCY_VOSTRO;

	@Column(name = "R49_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R49_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R49_AMOUNT_TIME_VOSTRO")
	private BigDecimal R49_AMOUNT_TIME_VOSTRO;

	@Column(name = "R50_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R50_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R50_TYPE_OF_ACCOUNT_VOSTRO")
	private String R50_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R50_PURPOSE_VOSTRO")
	private String R50_PURPOSE_VOSTRO;

	@Column(name = "R50_CURRENCY_VOSTRO")
	private String R50_CURRENCY_VOSTRO;

	@Column(name = "R50_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R50_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R50_AMOUNT_TIME_VOSTRO")
	private BigDecimal R50_AMOUNT_TIME_VOSTRO;

	@Column(name = "R51_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R51_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R51_TYPE_OF_ACCOUNT_VOSTRO")
	private String R51_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R51_PURPOSE_VOSTRO")
	private String R51_PURPOSE_VOSTRO;

	@Column(name = "R51_CURRENCY_VOSTRO")
	private String R51_CURRENCY_VOSTRO;

	@Column(name = "R51_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R51_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R51_AMOUNT_TIME_VOSTRO")
	private BigDecimal R51_AMOUNT_TIME_VOSTRO;

	@Column(name = "R52_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R52_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R52_TYPE_OF_ACCOUNT_VOSTRO")
	private String R52_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R52_PURPOSE_VOSTRO")
	private String R52_PURPOSE_VOSTRO;

	@Column(name = "R52_CURRENCY_VOSTRO")
	private String R52_CURRENCY_VOSTRO;

	@Column(name = "R52_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R52_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R52_AMOUNT_TIME_VOSTRO")
	private BigDecimal R52_AMOUNT_TIME_VOSTRO;

	@Column(name = "R53_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R53_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R53_TYPE_OF_ACCOUNT_VOSTRO")
	private String R53_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R53_PURPOSE_VOSTRO")
	private String R53_PURPOSE_VOSTRO;

	@Column(name = "R53_CURRENCY_VOSTRO")
	private String R53_CURRENCY_VOSTRO;

	@Column(name = "R53_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R53_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R53_AMOUNT_TIME_VOSTRO")
	private BigDecimal R53_AMOUNT_TIME_VOSTRO;

	@Column(name = "R54_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R54_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R54_TYPE_OF_ACCOUNT_VOSTRO")
	private String R54_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R54_PURPOSE_VOSTRO")
	private String R54_PURPOSE_VOSTRO;

	@Column(name = "R54_CURRENCY_VOSTRO")
	private String R54_CURRENCY_VOSTRO;

	@Column(name = "R54_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R54_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R54_AMOUNT_TIME_VOSTRO")
	private BigDecimal R54_AMOUNT_TIME_VOSTRO;

	@Column(name = "R55_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R55_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R55_TYPE_OF_ACCOUNT_VOSTRO")
	private String R55_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R55_PURPOSE_VOSTRO")
	private String R55_PURPOSE_VOSTRO;

	@Column(name = "R55_CURRENCY_VOSTRO")
	private String R55_CURRENCY_VOSTRO;

	@Column(name = "R55_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R55_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R55_AMOUNT_TIME_VOSTRO")
	private BigDecimal R55_AMOUNT_TIME_VOSTRO;

	@Column(name = "R56_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R56_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R56_TYPE_OF_ACCOUNT_VOSTRO")
	private String R56_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R56_PURPOSE_VOSTRO")
	private String R56_PURPOSE_VOSTRO;

	@Column(name = "R56_CURRENCY_VOSTRO")
	private String R56_CURRENCY_VOSTRO;

	@Column(name = "R56_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R56_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R56_AMOUNT_TIME_VOSTRO")
	private BigDecimal R56_AMOUNT_TIME_VOSTRO;

	@Column(name = "R57_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R57_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R57_TYPE_OF_ACCOUNT_VOSTRO")
	private String R57_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R57_PURPOSE_VOSTRO")
	private String R57_PURPOSE_VOSTRO;

	@Column(name = "R57_CURRENCY_VOSTRO")
	private String R57_CURRENCY_VOSTRO;

	@Column(name = "R57_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R57_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R57_AMOUNT_TIME_VOSTRO")
	private BigDecimal R57_AMOUNT_TIME_VOSTRO;

	@Column(name = "R58_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R58_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R58_TYPE_OF_ACCOUNT_VOSTRO")
	private String R58_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R58_PURPOSE_VOSTRO")
	private String R58_PURPOSE_VOSTRO;

	@Column(name = "R58_CURRENCY_VOSTRO")
	private String R58_CURRENCY_VOSTRO;

	@Column(name = "R58_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R58_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R58_AMOUNT_TIME_VOSTRO")
	private BigDecimal R58_AMOUNT_TIME_VOSTRO;

	@Column(name = "R59_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R59_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R59_TYPE_OF_ACCOUNT_VOSTRO")
	private String R59_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R59_PURPOSE_VOSTRO")
	private String R59_PURPOSE_VOSTRO;

	@Column(name = "R59_CURRENCY_VOSTRO")
	private String R59_CURRENCY_VOSTRO;

	@Column(name = "R59_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R59_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R59_AMOUNT_TIME_VOSTRO")
	private BigDecimal R59_AMOUNT_TIME_VOSTRO;

	@Column(name = "R60_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R60_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R60_TYPE_OF_ACCOUNT_VOSTRO")
	private String R60_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R60_PURPOSE_VOSTRO")
	private String R60_PURPOSE_VOSTRO;

	@Column(name = "R60_CURRENCY_VOSTRO")
	private String R60_CURRENCY_VOSTRO;

	@Column(name = "R60_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R60_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R60_AMOUNT_TIME_VOSTRO")
	private BigDecimal R60_AMOUNT_TIME_VOSTRO;

	@Column(name = "R61_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R61_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R61_TYPE_OF_ACCOUNT_VOSTRO")
	private String R61_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R61_PURPOSE_VOSTRO")
	private String R61_PURPOSE_VOSTRO;

	@Column(name = "R61_CURRENCY_VOSTRO")
	private String R61_CURRENCY_VOSTRO;

	@Column(name = "R61_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R61_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R61_AMOUNT_TIME_VOSTRO")
	private BigDecimal R61_AMOUNT_TIME_VOSTRO;

	@Column(name = "R62_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R62_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R62_TYPE_OF_ACCOUNT_VOSTRO")
	private String R62_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R62_PURPOSE_VOSTRO")
	private String R62_PURPOSE_VOSTRO;

	@Column(name = "R62_CURRENCY_VOSTRO")
	private String R62_CURRENCY_VOSTRO;

	@Column(name = "R62_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R62_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R62_AMOUNT_TIME_VOSTRO")
	private BigDecimal R62_AMOUNT_TIME_VOSTRO;

	@Column(name = "R63_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R63_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R63_TYPE_OF_ACCOUNT_VOSTRO")
	private String R63_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R63_PURPOSE_VOSTRO")
	private String R63_PURPOSE_VOSTRO;

	@Column(name = "R63_CURRENCY_VOSTRO")
	private String R63_CURRENCY_VOSTRO;

	@Column(name = "R63_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R63_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R63_AMOUNT_TIME_VOSTRO")
	private BigDecimal R63_AMOUNT_TIME_VOSTRO;

	@Column(name = "R64_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R64_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R64_TYPE_OF_ACCOUNT_VOSTRO")
	private String R64_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R64_PURPOSE_VOSTRO")
	private String R64_PURPOSE_VOSTRO;

	@Column(name = "R64_CURRENCY_VOSTRO")
	private String R64_CURRENCY_VOSTRO;

	@Column(name = "R64_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R64_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R64_AMOUNT_TIME_VOSTRO")
	private BigDecimal R64_AMOUNT_TIME_VOSTRO;

	@Column(name = "R65_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R65_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R65_TYPE_OF_ACCOUNT_VOSTRO")
	private String R65_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R65_PURPOSE_VOSTRO")
	private String R65_PURPOSE_VOSTRO;

	@Column(name = "R65_CURRENCY_VOSTRO")
	private String R65_CURRENCY_VOSTRO;

	@Column(name = "R65_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R65_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R65_AMOUNT_TIME_VOSTRO")
	private BigDecimal R65_AMOUNT_TIME_VOSTRO;

	@Column(name = "R66_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R66_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R66_TYPE_OF_ACCOUNT_VOSTRO")
	private String R66_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R66_PURPOSE_VOSTRO")
	private String R66_PURPOSE_VOSTRO;

	@Column(name = "R66_CURRENCY_VOSTRO")
	private String R66_CURRENCY_VOSTRO;

	@Column(name = "R66_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R66_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R66_AMOUNT_TIME_VOSTRO")
	private BigDecimal R66_AMOUNT_TIME_VOSTRO;

	@Column(name = "R67_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R67_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R67_TYPE_OF_ACCOUNT_VOSTRO")
	private String R67_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R67_PURPOSE_VOSTRO")
	private String R67_PURPOSE_VOSTRO;

	@Column(name = "R67_CURRENCY_VOSTRO")
	private String R67_CURRENCY_VOSTRO;

	@Column(name = "R67_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R67_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R67_AMOUNT_TIME_VOSTRO")
	private BigDecimal R67_AMOUNT_TIME_VOSTRO;

	@Column(name = "R68_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R68_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R68_TYPE_OF_ACCOUNT_VOSTRO")
	private String R68_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R68_PURPOSE_VOSTRO")
	private String R68_PURPOSE_VOSTRO;

	@Column(name = "R68_CURRENCY_VOSTRO")
	private String R68_CURRENCY_VOSTRO;

	@Column(name = "R68_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R68_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R68_AMOUNT_TIME_VOSTRO")
	private BigDecimal R68_AMOUNT_TIME_VOSTRO;

	@Column(name = "R69_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R69_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R69_TYPE_OF_ACCOUNT_VOSTRO")
	private String R69_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R69_PURPOSE_VOSTRO")
	private String R69_PURPOSE_VOSTRO;

	@Column(name = "R69_CURRENCY_VOSTRO")
	private String R69_CURRENCY_VOSTRO;

	@Column(name = "R69_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R69_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R69_AMOUNT_TIME_VOSTRO")
	private BigDecimal R69_AMOUNT_TIME_VOSTRO;

	@Column(name = "R70_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R70_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R70_TYPE_OF_ACCOUNT_VOSTRO")
	private String R70_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R70_PURPOSE_VOSTRO")
	private String R70_PURPOSE_VOSTRO;

	@Column(name = "R70_CURRENCY_VOSTRO")
	private String R70_CURRENCY_VOSTRO;

	@Column(name = "R70_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R70_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R70_AMOUNT_TIME_VOSTRO")
	private BigDecimal R70_AMOUNT_TIME_VOSTRO;

	@Column(name = "R71_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R71_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R71_TYPE_OF_ACCOUNT_VOSTRO")
	private String R71_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R71_PURPOSE_VOSTRO")
	private String R71_PURPOSE_VOSTRO;

	@Column(name = "R71_CURRENCY_VOSTRO")
	private String R71_CURRENCY_VOSTRO;

	@Column(name = "R71_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R71_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R71_AMOUNT_TIME_VOSTRO")
	private BigDecimal R71_AMOUNT_TIME_VOSTRO;

	@Column(name = "R72_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R72_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R72_TYPE_OF_ACCOUNT_VOSTRO")
	private String R72_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R72_PURPOSE_VOSTRO")
	private String R72_PURPOSE_VOSTRO;

	@Column(name = "R72_CURRENCY_VOSTRO")
	private String R72_CURRENCY_VOSTRO;

	@Column(name = "R72_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R72_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R72_AMOUNT_TIME_VOSTRO")
	private BigDecimal R72_AMOUNT_TIME_VOSTRO;

	@Column(name = "R73_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R73_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R73_TYPE_OF_ACCOUNT_VOSTRO")
	private String R73_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R73_PURPOSE_VOSTRO")
	private String R73_PURPOSE_VOSTRO;

	@Column(name = "R73_CURRENCY_VOSTRO")
	private String R73_CURRENCY_VOSTRO;

	@Column(name = "R73_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R73_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R73_AMOUNT_TIME_VOSTRO")
	private BigDecimal R73_AMOUNT_TIME_VOSTRO;

	@Column(name = "R74_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R74_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R74_TYPE_OF_ACCOUNT_VOSTRO")
	private String R74_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R74_PURPOSE_VOSTRO")
	private String R74_PURPOSE_VOSTRO;

	@Column(name = "R74_CURRENCY_VOSTRO")
	private String R74_CURRENCY_VOSTRO;

	@Column(name = "R74_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R74_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R74_AMOUNT_TIME_VOSTRO")
	private BigDecimal R74_AMOUNT_TIME_VOSTRO;

	@Column(name = "R75_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R75_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R75_TYPE_OF_ACCOUNT_VOSTRO")
	private String R75_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R75_PURPOSE_VOSTRO")
	private String R75_PURPOSE_VOSTRO;

	@Column(name = "R75_CURRENCY_VOSTRO")
	private String R75_CURRENCY_VOSTRO;

	@Column(name = "R75_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R75_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R75_AMOUNT_TIME_VOSTRO")
	private BigDecimal R75_AMOUNT_TIME_VOSTRO;

	@Column(name = "R76_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R76_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R76_TYPE_OF_ACCOUNT_VOSTRO")
	private String R76_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R76_PURPOSE_VOSTRO")
	private String R76_PURPOSE_VOSTRO;

	@Column(name = "R76_CURRENCY_VOSTRO")
	private String R76_CURRENCY_VOSTRO;

	@Column(name = "R76_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R76_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R76_AMOUNT_TIME_VOSTRO")
	private BigDecimal R76_AMOUNT_TIME_VOSTRO;

	@Column(name = "R77_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R77_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R77_TYPE_OF_ACCOUNT_VOSTRO")
	private String R77_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R77_PURPOSE_VOSTRO")
	private String R77_PURPOSE_VOSTRO;

	@Column(name = "R77_CURRENCY_VOSTRO")
	private String R77_CURRENCY_VOSTRO;

	@Column(name = "R77_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R77_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R77_AMOUNT_TIME_VOSTRO")
	private BigDecimal R77_AMOUNT_TIME_VOSTRO;

	@Column(name = "R78_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R78_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R78_TYPE_OF_ACCOUNT_VOSTRO")
	private String R78_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R78_PURPOSE_VOSTRO")
	private String R78_PURPOSE_VOSTRO;

	@Column(name = "R78_CURRENCY_VOSTRO")
	private String R78_CURRENCY_VOSTRO;

	@Column(name = "R78_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R78_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R78_AMOUNT_TIME_VOSTRO")
	private BigDecimal R78_AMOUNT_TIME_VOSTRO;

	@Column(name = "R79_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R79_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R79_TYPE_OF_ACCOUNT_VOSTRO")
	private String R79_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R79_PURPOSE_VOSTRO")
	private String R79_PURPOSE_VOSTRO;

	@Column(name = "R79_CURRENCY_VOSTRO")
	private String R79_CURRENCY_VOSTRO;

	@Column(name = "R79_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R79_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R79_AMOUNT_TIME_VOSTRO")
	private BigDecimal R79_AMOUNT_TIME_VOSTRO;

	@Column(name = "R80_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R80_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R80_TYPE_OF_ACCOUNT_VOSTRO")
	private String R80_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R80_PURPOSE_VOSTRO")
	private String R80_PURPOSE_VOSTRO;

	@Column(name = "R80_CURRENCY_VOSTRO")
	private String R80_CURRENCY_VOSTRO;

	@Column(name = "R80_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R80_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R80_AMOUNT_TIME_VOSTRO")
	private BigDecimal R80_AMOUNT_TIME_VOSTRO;

	@Column(name = "R81_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R81_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R81_TYPE_OF_ACCOUNT_VOSTRO")
	private String R81_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R81_PURPOSE_VOSTRO")
	private String R81_PURPOSE_VOSTRO;

	@Column(name = "R81_CURRENCY_VOSTRO")
	private String R81_CURRENCY_VOSTRO;

	@Column(name = "R81_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R81_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R81_AMOUNT_TIME_VOSTRO")
	private BigDecimal R81_AMOUNT_TIME_VOSTRO;

	@Column(name = "R82_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R82_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R82_TYPE_OF_ACCOUNT_VOSTRO")
	private String R82_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R82_PURPOSE_VOSTRO")
	private String R82_PURPOSE_VOSTRO;

	@Column(name = "R82_CURRENCY_VOSTRO")
	private String R82_CURRENCY_VOSTRO;

	@Column(name = "R82_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R82_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R82_AMOUNT_TIME_VOSTRO")
	private BigDecimal R82_AMOUNT_TIME_VOSTRO;

	@Column(name = "R83_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R83_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R83_TYPE_OF_ACCOUNT_VOSTRO")
	private String R83_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R83_PURPOSE_VOSTRO")
	private String R83_PURPOSE_VOSTRO;

	@Column(name = "R83_CURRENCY_VOSTRO")
	private String R83_CURRENCY_VOSTRO;

	@Column(name = "R83_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R83_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R83_AMOUNT_TIME_VOSTRO")
	private BigDecimal R83_AMOUNT_TIME_VOSTRO;

	@Column(name = "R84_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R84_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R84_TYPE_OF_ACCOUNT_VOSTRO")
	private String R84_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R84_PURPOSE_VOSTRO")
	private String R84_PURPOSE_VOSTRO;

	@Column(name = "R84_CURRENCY_VOSTRO")
	private String R84_CURRENCY_VOSTRO;

	@Column(name = "R84_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R84_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R84_AMOUNT_TIME_VOSTRO")
	private BigDecimal R84_AMOUNT_TIME_VOSTRO;

	@Column(name = "R85_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R85_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R85_TYPE_OF_ACCOUNT_VOSTRO")
	private String R85_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R85_PURPOSE_VOSTRO")
	private String R85_PURPOSE_VOSTRO;

	@Column(name = "R85_CURRENCY_VOSTRO")
	private String R85_CURRENCY_VOSTRO;

	@Column(name = "R85_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R85_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R85_AMOUNT_TIME_VOSTRO")
	private BigDecimal R85_AMOUNT_TIME_VOSTRO;

	@Column(name = "R86_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R86_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R86_TYPE_OF_ACCOUNT_VOSTRO")
	private String R86_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R86_PURPOSE_VOSTRO")
	private String R86_PURPOSE_VOSTRO;

	@Column(name = "R86_CURRENCY_VOSTRO")
	private String R86_CURRENCY_VOSTRO;

	@Column(name = "R86_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R86_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R86_AMOUNT_TIME_VOSTRO")
	private BigDecimal R86_AMOUNT_TIME_VOSTRO;

	@Column(name = "R87_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R87_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R87_TYPE_OF_ACCOUNT_VOSTRO")
	private String R87_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R87_PURPOSE_VOSTRO")
	private String R87_PURPOSE_VOSTRO;

	@Column(name = "R87_CURRENCY_VOSTRO")
	private String R87_CURRENCY_VOSTRO;

	@Column(name = "R87_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R87_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R87_AMOUNT_TIME_VOSTRO")
	private BigDecimal R87_AMOUNT_TIME_VOSTRO;

	@Column(name = "R88_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R88_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R88_TYPE_OF_ACCOUNT_VOSTRO")
	private String R88_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R88_PURPOSE_VOSTRO")
	private String R88_PURPOSE_VOSTRO;

	@Column(name = "R88_CURRENCY_VOSTRO")
	private String R88_CURRENCY_VOSTRO;

	@Column(name = "R88_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R88_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R88_AMOUNT_TIME_VOSTRO")
	private BigDecimal R88_AMOUNT_TIME_VOSTRO;

	@Column(name = "R89_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R89_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R89_TYPE_OF_ACCOUNT_VOSTRO")
	private String R89_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R89_PURPOSE_VOSTRO")
	private String R89_PURPOSE_VOSTRO;

	@Column(name = "R89_CURRENCY_VOSTRO")
	private String R89_CURRENCY_VOSTRO;

	@Column(name = "R89_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R89_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R89_AMOUNT_TIME_VOSTRO")
	private BigDecimal R89_AMOUNT_TIME_VOSTRO;

	@Column(name = "R90_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R90_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R90_TYPE_OF_ACCOUNT_VOSTRO")
	private String R90_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R90_PURPOSE_VOSTRO")
	private String R90_PURPOSE_VOSTRO;

	@Column(name = "R90_CURRENCY_VOSTRO")
	private String R90_CURRENCY_VOSTRO;

	@Column(name = "R90_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R90_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R90_AMOUNT_TIME_VOSTRO")
	private BigDecimal R90_AMOUNT_TIME_VOSTRO;

	@Column(name = "R91_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R91_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R91_TYPE_OF_ACCOUNT_VOSTRO")
	private String R91_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R91_PURPOSE_VOSTRO")
	private String R91_PURPOSE_VOSTRO;

	@Column(name = "R91_CURRENCY_VOSTRO")
	private String R91_CURRENCY_VOSTRO;

	@Column(name = "R91_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R91_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R91_AMOUNT_TIME_VOSTRO")
	private BigDecimal R91_AMOUNT_TIME_VOSTRO;

	@Column(name = "R92_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R92_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R92_TYPE_OF_ACCOUNT_VOSTRO")
	private String R92_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R92_PURPOSE_VOSTRO")
	private String R92_PURPOSE_VOSTRO;

	@Column(name = "R92_CURRENCY_VOSTRO")
	private String R92_CURRENCY_VOSTRO;

	@Column(name = "R92_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R92_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R92_AMOUNT_TIME_VOSTRO")
	private BigDecimal R92_AMOUNT_TIME_VOSTRO;

	@Column(name = "R93_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R93_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R93_TYPE_OF_ACCOUNT_VOSTRO")
	private String R93_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R93_PURPOSE_VOSTRO")
	private String R93_PURPOSE_VOSTRO;

	@Column(name = "R93_CURRENCY_VOSTRO")
	private String R93_CURRENCY_VOSTRO;

	@Column(name = "R93_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R93_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R93_AMOUNT_TIME_VOSTRO")
	private BigDecimal R93_AMOUNT_TIME_VOSTRO;

	@Column(name = "R94_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R94_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R94_TYPE_OF_ACCOUNT_VOSTRO")
	private String R94_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R94_PURPOSE_VOSTRO")
	private String R94_PURPOSE_VOSTRO;

	@Column(name = "R94_CURRENCY_VOSTRO")
	private String R94_CURRENCY_VOSTRO;

	@Column(name = "R94_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R94_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R94_AMOUNT_TIME_VOSTRO")
	private BigDecimal R94_AMOUNT_TIME_VOSTRO;

	@Column(name = "R95_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R95_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R95_TYPE_OF_ACCOUNT_VOSTRO")
	private String R95_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R95_PURPOSE_VOSTRO")
	private String R95_PURPOSE_VOSTRO;

	@Column(name = "R95_CURRENCY_VOSTRO")
	private String R95_CURRENCY_VOSTRO;

	@Column(name = "R95_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R95_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R95_AMOUNT_TIME_VOSTRO")
	private BigDecimal R95_AMOUNT_TIME_VOSTRO;

	@Column(name = "R96_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R96_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R96_TYPE_OF_ACCOUNT_VOSTRO")
	private String R96_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R96_PURPOSE_VOSTRO")
	private String R96_PURPOSE_VOSTRO;

	@Column(name = "R96_CURRENCY_VOSTRO")
	private String R96_CURRENCY_VOSTRO;

	@Column(name = "R96_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R96_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R96_AMOUNT_TIME_VOSTRO")
	private BigDecimal R96_AMOUNT_TIME_VOSTRO;

	@Column(name = "R97_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R97_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R97_TYPE_OF_ACCOUNT_VOSTRO")
	private String R97_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R97_PURPOSE_VOSTRO")
	private String R97_PURPOSE_VOSTRO;

	@Column(name = "R97_CURRENCY_VOSTRO")
	private String R97_CURRENCY_VOSTRO;

	@Column(name = "R97_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R97_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R97_AMOUNT_TIME_VOSTRO")
	private BigDecimal R97_AMOUNT_TIME_VOSTRO;

	@Column(name = "R98_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R98_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R98_TYPE_OF_ACCOUNT_VOSTRO")
	private String R98_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R98_PURPOSE_VOSTRO")
	private String R98_PURPOSE_VOSTRO;

	@Column(name = "R98_CURRENCY_VOSTRO")
	private String R98_CURRENCY_VOSTRO;

	@Column(name = "R98_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R98_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R98_AMOUNT_TIME_VOSTRO")
	private BigDecimal R98_AMOUNT_TIME_VOSTRO;

	@Column(name = "R99_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R99_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R99_TYPE_OF_ACCOUNT_VOSTRO")
	private String R99_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R99_PURPOSE_VOSTRO")
	private String R99_PURPOSE_VOSTRO;

	@Column(name = "R99_CURRENCY_VOSTRO")
	private String R99_CURRENCY_VOSTRO;

	@Column(name = "R99_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R99_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R99_AMOUNT_TIME_VOSTRO")
	private BigDecimal R99_AMOUNT_TIME_VOSTRO;

	@Column(name = "R100_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R100_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R100_TYPE_OF_ACCOUNT_VOSTRO")
	private String R100_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R100_PURPOSE_VOSTRO")
	private String R100_PURPOSE_VOSTRO;

	@Column(name = "R100_CURRENCY_VOSTRO")
	private String R100_CURRENCY_VOSTRO;

	@Column(name = "R100_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R100_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R100_AMOUNT_TIME_VOSTRO")
	private BigDecimal R100_AMOUNT_TIME_VOSTRO;
	
	@Column(name = "R101_TOTAL_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R101_TOTAL_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R101_TOTAL_AMOUNT_TIME_VOSTRO")
	private BigDecimal R101_TOTAL_AMOUNT_TIME_VOSTRO;
	
	@Column(name = "R101_NAME_OF_BANK_AND_COUNTRY_VOSTRO")
	private String R101_NAME_OF_BANK_AND_COUNTRY_VOSTRO;

	@Column(name = "R101_TYPE_OF_ACCOUNT_VOSTRO")
	private String R101_TYPE_OF_ACCOUNT_VOSTRO;

	@Column(name = "R101_PURPOSE_VOSTRO")
	private String R101_PURPOSE_VOSTRO;

	@Column(name = "R101_CURRENCY_VOSTRO")
	private String R101_CURRENCY_VOSTRO;

	@Column(name = "R101_AMOUNT_DEMAND_VOSTRO")
	private BigDecimal R101_AMOUNT_DEMAND_VOSTRO;

	@Column(name = "R101_AMOUNT_TIME_VOSTRO")
	private BigDecimal R101_AMOUNT_TIME_VOSTRO;

	
	
	
	

	public String getR101_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R101_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR101_TYPE_OF_ACCOUNT_VOSTRO() {
		return R101_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR101_PURPOSE_VOSTRO() {
		return R101_PURPOSE_VOSTRO;
	}

	public String getR101_CURRENCY_VOSTRO() {
		return R101_CURRENCY_VOSTRO;
	}

	public BigDecimal getR101_AMOUNT_DEMAND_VOSTRO() {
		return R101_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR101_AMOUNT_TIME_VOSTRO() {
		return R101_AMOUNT_TIME_VOSTRO;
	}

	public void setR101_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r101_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R101_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r101_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR101_TYPE_OF_ACCOUNT_VOSTRO(String r101_TYPE_OF_ACCOUNT_VOSTRO) {
		R101_TYPE_OF_ACCOUNT_VOSTRO = r101_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR101_PURPOSE_VOSTRO(String r101_PURPOSE_VOSTRO) {
		R101_PURPOSE_VOSTRO = r101_PURPOSE_VOSTRO;
	}

	public void setR101_CURRENCY_VOSTRO(String r101_CURRENCY_VOSTRO) {
		R101_CURRENCY_VOSTRO = r101_CURRENCY_VOSTRO;
	}

	public void setR101_AMOUNT_DEMAND_VOSTRO(BigDecimal r101_AMOUNT_DEMAND_VOSTRO) {
		R101_AMOUNT_DEMAND_VOSTRO = r101_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR101_AMOUNT_TIME_VOSTRO(BigDecimal r101_AMOUNT_TIME_VOSTRO) {
		R101_AMOUNT_TIME_VOSTRO = r101_AMOUNT_TIME_VOSTRO;
	}

	public BigDecimal getR101_TOTAL_AMOUNT_DEMAND_VOSTRO() {
		return R101_TOTAL_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR101_TOTAL_AMOUNT_DEMAND_VOSTRO(BigDecimal r101_TOTAL_AMOUNT_DEMAND_VOSTRO) {
		R101_TOTAL_AMOUNT_DEMAND_VOSTRO = r101_TOTAL_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR101_TOTAL_AMOUNT_TIME_VOSTRO() {
		return R101_TOTAL_AMOUNT_TIME_VOSTRO;
	}

	public void setR101_TOTAL_AMOUNT_TIME_VOSTRO(BigDecimal r101_TOTAL_AMOUNT_TIME_VOSTRO) {
		R101_TOTAL_AMOUNT_TIME_VOSTRO = r101_TOTAL_AMOUNT_TIME_VOSTRO;
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

	public String getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R1_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r1_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R1_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r1_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR1_TYPE_OF_ACCOUNT_VOSTRO() {
		return R1_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR1_TYPE_OF_ACCOUNT_VOSTRO(String r1_TYPE_OF_ACCOUNT_VOSTRO) {
		R1_TYPE_OF_ACCOUNT_VOSTRO = r1_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR1_PURPOSE_VOSTRO() {
		return R1_PURPOSE_VOSTRO;
	}

	public void setR1_PURPOSE_VOSTRO(String r1_PURPOSE_VOSTRO) {
		R1_PURPOSE_VOSTRO = r1_PURPOSE_VOSTRO;
	}

	public String getR1_CURRENCY_VOSTRO() {
		return R1_CURRENCY_VOSTRO;
	}

	public void setR1_CURRENCY_VOSTRO(String r1_CURRENCY_VOSTRO) {
		R1_CURRENCY_VOSTRO = r1_CURRENCY_VOSTRO;
	}

	public BigDecimal getR1_AMOUNT_DEMAND_VOSTRO() {
		return R1_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR1_AMOUNT_DEMAND_VOSTRO(BigDecimal r1_AMOUNT_DEMAND_VOSTRO) {
		R1_AMOUNT_DEMAND_VOSTRO = r1_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR1_AMOUNT_TIME_VOSTRO() {
		return R1_AMOUNT_TIME_VOSTRO;
	}

	public void setR1_AMOUNT_TIME_VOSTRO(BigDecimal r1_AMOUNT_TIME_VOSTRO) {
		R1_AMOUNT_TIME_VOSTRO = r1_AMOUNT_TIME_VOSTRO;
	}

	public String getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R2_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r2_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R2_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r2_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR2_TYPE_OF_ACCOUNT_VOSTRO() {
		return R2_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR2_TYPE_OF_ACCOUNT_VOSTRO(String r2_TYPE_OF_ACCOUNT_VOSTRO) {
		R2_TYPE_OF_ACCOUNT_VOSTRO = r2_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR2_PURPOSE_VOSTRO() {
		return R2_PURPOSE_VOSTRO;
	}

	public void setR2_PURPOSE_VOSTRO(String r2_PURPOSE_VOSTRO) {
		R2_PURPOSE_VOSTRO = r2_PURPOSE_VOSTRO;
	}

	public String getR2_CURRENCY_VOSTRO() {
		return R2_CURRENCY_VOSTRO;
	}

	public void setR2_CURRENCY_VOSTRO(String r2_CURRENCY_VOSTRO) {
		R2_CURRENCY_VOSTRO = r2_CURRENCY_VOSTRO;
	}

	public BigDecimal getR2_AMOUNT_DEMAND_VOSTRO() {
		return R2_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR2_AMOUNT_DEMAND_VOSTRO(BigDecimal r2_AMOUNT_DEMAND_VOSTRO) {
		R2_AMOUNT_DEMAND_VOSTRO = r2_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR2_AMOUNT_TIME_VOSTRO() {
		return R2_AMOUNT_TIME_VOSTRO;
	}

	public void setR2_AMOUNT_TIME_VOSTRO(BigDecimal r2_AMOUNT_TIME_VOSTRO) {
		R2_AMOUNT_TIME_VOSTRO = r2_AMOUNT_TIME_VOSTRO;
	}

	public String getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R3_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r3_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R3_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r3_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR3_TYPE_OF_ACCOUNT_VOSTRO() {
		return R3_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR3_TYPE_OF_ACCOUNT_VOSTRO(String r3_TYPE_OF_ACCOUNT_VOSTRO) {
		R3_TYPE_OF_ACCOUNT_VOSTRO = r3_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR3_PURPOSE_VOSTRO() {
		return R3_PURPOSE_VOSTRO;
	}

	public void setR3_PURPOSE_VOSTRO(String r3_PURPOSE_VOSTRO) {
		R3_PURPOSE_VOSTRO = r3_PURPOSE_VOSTRO;
	}

	public String getR3_CURRENCY_VOSTRO() {
		return R3_CURRENCY_VOSTRO;
	}

	public void setR3_CURRENCY_VOSTRO(String r3_CURRENCY_VOSTRO) {
		R3_CURRENCY_VOSTRO = r3_CURRENCY_VOSTRO;
	}

	public BigDecimal getR3_AMOUNT_DEMAND_VOSTRO() {
		return R3_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR3_AMOUNT_DEMAND_VOSTRO(BigDecimal r3_AMOUNT_DEMAND_VOSTRO) {
		R3_AMOUNT_DEMAND_VOSTRO = r3_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR3_AMOUNT_TIME_VOSTRO() {
		return R3_AMOUNT_TIME_VOSTRO;
	}

	public void setR3_AMOUNT_TIME_VOSTRO(BigDecimal r3_AMOUNT_TIME_VOSTRO) {
		R3_AMOUNT_TIME_VOSTRO = r3_AMOUNT_TIME_VOSTRO;
	}

	public String getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R4_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r4_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R4_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r4_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR4_TYPE_OF_ACCOUNT_VOSTRO() {
		return R4_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR4_TYPE_OF_ACCOUNT_VOSTRO(String r4_TYPE_OF_ACCOUNT_VOSTRO) {
		R4_TYPE_OF_ACCOUNT_VOSTRO = r4_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR4_PURPOSE_VOSTRO() {
		return R4_PURPOSE_VOSTRO;
	}

	public void setR4_PURPOSE_VOSTRO(String r4_PURPOSE_VOSTRO) {
		R4_PURPOSE_VOSTRO = r4_PURPOSE_VOSTRO;
	}

	public String getR4_CURRENCY_VOSTRO() {
		return R4_CURRENCY_VOSTRO;
	}

	public void setR4_CURRENCY_VOSTRO(String r4_CURRENCY_VOSTRO) {
		R4_CURRENCY_VOSTRO = r4_CURRENCY_VOSTRO;
	}

	public BigDecimal getR4_AMOUNT_DEMAND_VOSTRO() {
		return R4_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR4_AMOUNT_DEMAND_VOSTRO(BigDecimal r4_AMOUNT_DEMAND_VOSTRO) {
		R4_AMOUNT_DEMAND_VOSTRO = r4_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR4_AMOUNT_TIME_VOSTRO() {
		return R4_AMOUNT_TIME_VOSTRO;
	}

	public void setR4_AMOUNT_TIME_VOSTRO(BigDecimal r4_AMOUNT_TIME_VOSTRO) {
		R4_AMOUNT_TIME_VOSTRO = r4_AMOUNT_TIME_VOSTRO;
	}

	public String getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R5_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r5_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R5_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r5_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR5_TYPE_OF_ACCOUNT_VOSTRO() {
		return R5_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR5_TYPE_OF_ACCOUNT_VOSTRO(String r5_TYPE_OF_ACCOUNT_VOSTRO) {
		R5_TYPE_OF_ACCOUNT_VOSTRO = r5_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR5_PURPOSE_VOSTRO() {
		return R5_PURPOSE_VOSTRO;
	}

	public void setR5_PURPOSE_VOSTRO(String r5_PURPOSE_VOSTRO) {
		R5_PURPOSE_VOSTRO = r5_PURPOSE_VOSTRO;
	}

	public String getR5_CURRENCY_VOSTRO() {
		return R5_CURRENCY_VOSTRO;
	}

	public void setR5_CURRENCY_VOSTRO(String r5_CURRENCY_VOSTRO) {
		R5_CURRENCY_VOSTRO = r5_CURRENCY_VOSTRO;
	}

	public BigDecimal getR5_AMOUNT_DEMAND_VOSTRO() {
		return R5_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR5_AMOUNT_DEMAND_VOSTRO(BigDecimal r5_AMOUNT_DEMAND_VOSTRO) {
		R5_AMOUNT_DEMAND_VOSTRO = r5_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR5_AMOUNT_TIME_VOSTRO() {
		return R5_AMOUNT_TIME_VOSTRO;
	}

	public void setR5_AMOUNT_TIME_VOSTRO(BigDecimal r5_AMOUNT_TIME_VOSTRO) {
		R5_AMOUNT_TIME_VOSTRO = r5_AMOUNT_TIME_VOSTRO;
	}

	public String getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R6_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r6_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R6_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r6_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR6_TYPE_OF_ACCOUNT_VOSTRO() {
		return R6_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR6_TYPE_OF_ACCOUNT_VOSTRO(String r6_TYPE_OF_ACCOUNT_VOSTRO) {
		R6_TYPE_OF_ACCOUNT_VOSTRO = r6_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR6_PURPOSE_VOSTRO() {
		return R6_PURPOSE_VOSTRO;
	}

	public void setR6_PURPOSE_VOSTRO(String r6_PURPOSE_VOSTRO) {
		R6_PURPOSE_VOSTRO = r6_PURPOSE_VOSTRO;
	}

	public String getR6_CURRENCY_VOSTRO() {
		return R6_CURRENCY_VOSTRO;
	}

	public void setR6_CURRENCY_VOSTRO(String r6_CURRENCY_VOSTRO) {
		R6_CURRENCY_VOSTRO = r6_CURRENCY_VOSTRO;
	}

	public BigDecimal getR6_AMOUNT_DEMAND_VOSTRO() {
		return R6_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR6_AMOUNT_DEMAND_VOSTRO(BigDecimal r6_AMOUNT_DEMAND_VOSTRO) {
		R6_AMOUNT_DEMAND_VOSTRO = r6_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR6_AMOUNT_TIME_VOSTRO() {
		return R6_AMOUNT_TIME_VOSTRO;
	}

	public void setR6_AMOUNT_TIME_VOSTRO(BigDecimal r6_AMOUNT_TIME_VOSTRO) {
		R6_AMOUNT_TIME_VOSTRO = r6_AMOUNT_TIME_VOSTRO;
	}

	public String getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R7_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r7_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R7_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r7_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR7_TYPE_OF_ACCOUNT_VOSTRO() {
		return R7_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR7_TYPE_OF_ACCOUNT_VOSTRO(String r7_TYPE_OF_ACCOUNT_VOSTRO) {
		R7_TYPE_OF_ACCOUNT_VOSTRO = r7_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR7_PURPOSE_VOSTRO() {
		return R7_PURPOSE_VOSTRO;
	}

	public void setR7_PURPOSE_VOSTRO(String r7_PURPOSE_VOSTRO) {
		R7_PURPOSE_VOSTRO = r7_PURPOSE_VOSTRO;
	}

	public String getR7_CURRENCY_VOSTRO() {
		return R7_CURRENCY_VOSTRO;
	}

	public void setR7_CURRENCY_VOSTRO(String r7_CURRENCY_VOSTRO) {
		R7_CURRENCY_VOSTRO = r7_CURRENCY_VOSTRO;
	}

	public BigDecimal getR7_AMOUNT_DEMAND_VOSTRO() {
		return R7_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR7_AMOUNT_DEMAND_VOSTRO(BigDecimal r7_AMOUNT_DEMAND_VOSTRO) {
		R7_AMOUNT_DEMAND_VOSTRO = r7_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR7_AMOUNT_TIME_VOSTRO() {
		return R7_AMOUNT_TIME_VOSTRO;
	}

	public void setR7_AMOUNT_TIME_VOSTRO(BigDecimal r7_AMOUNT_TIME_VOSTRO) {
		R7_AMOUNT_TIME_VOSTRO = r7_AMOUNT_TIME_VOSTRO;
	}

	public String getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R8_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r8_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R8_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r8_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR8_TYPE_OF_ACCOUNT_VOSTRO() {
		return R8_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR8_TYPE_OF_ACCOUNT_VOSTRO(String r8_TYPE_OF_ACCOUNT_VOSTRO) {
		R8_TYPE_OF_ACCOUNT_VOSTRO = r8_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR8_PURPOSE_VOSTRO() {
		return R8_PURPOSE_VOSTRO;
	}

	public void setR8_PURPOSE_VOSTRO(String r8_PURPOSE_VOSTRO) {
		R8_PURPOSE_VOSTRO = r8_PURPOSE_VOSTRO;
	}

	public String getR8_CURRENCY_VOSTRO() {
		return R8_CURRENCY_VOSTRO;
	}

	public void setR8_CURRENCY_VOSTRO(String r8_CURRENCY_VOSTRO) {
		R8_CURRENCY_VOSTRO = r8_CURRENCY_VOSTRO;
	}

	public BigDecimal getR8_AMOUNT_DEMAND_VOSTRO() {
		return R8_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR8_AMOUNT_DEMAND_VOSTRO(BigDecimal r8_AMOUNT_DEMAND_VOSTRO) {
		R8_AMOUNT_DEMAND_VOSTRO = r8_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR8_AMOUNT_TIME_VOSTRO() {
		return R8_AMOUNT_TIME_VOSTRO;
	}

	public void setR8_AMOUNT_TIME_VOSTRO(BigDecimal r8_AMOUNT_TIME_VOSTRO) {
		R8_AMOUNT_TIME_VOSTRO = r8_AMOUNT_TIME_VOSTRO;
	}

	public String getR9_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R9_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR9_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r9_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R9_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r9_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR9_TYPE_OF_ACCOUNT_VOSTRO() {
		return R9_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR9_TYPE_OF_ACCOUNT_VOSTRO(String r9_TYPE_OF_ACCOUNT_VOSTRO) {
		R9_TYPE_OF_ACCOUNT_VOSTRO = r9_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR9_PURPOSE_VOSTRO() {
		return R9_PURPOSE_VOSTRO;
	}

	public void setR9_PURPOSE_VOSTRO(String r9_PURPOSE_VOSTRO) {
		R9_PURPOSE_VOSTRO = r9_PURPOSE_VOSTRO;
	}

	public String getR9_CURRENCY_VOSTRO() {
		return R9_CURRENCY_VOSTRO;
	}

	public void setR9_CURRENCY_VOSTRO(String r9_CURRENCY_VOSTRO) {
		R9_CURRENCY_VOSTRO = r9_CURRENCY_VOSTRO;
	}

	public BigDecimal getR9_AMOUNT_DEMAND_VOSTRO() {
		return R9_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR9_AMOUNT_DEMAND_VOSTRO(BigDecimal r9_AMOUNT_DEMAND_VOSTRO) {
		R9_AMOUNT_DEMAND_VOSTRO = r9_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR9_AMOUNT_TIME_VOSTRO() {
		return R9_AMOUNT_TIME_VOSTRO;
	}

	public void setR9_AMOUNT_TIME_VOSTRO(BigDecimal r9_AMOUNT_TIME_VOSTRO) {
		R9_AMOUNT_TIME_VOSTRO = r9_AMOUNT_TIME_VOSTRO;
	}

	public String getR10_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R10_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR10_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r10_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R10_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r10_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR10_TYPE_OF_ACCOUNT_VOSTRO() {
		return R10_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR10_TYPE_OF_ACCOUNT_VOSTRO(String r10_TYPE_OF_ACCOUNT_VOSTRO) {
		R10_TYPE_OF_ACCOUNT_VOSTRO = r10_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR10_PURPOSE_VOSTRO() {
		return R10_PURPOSE_VOSTRO;
	}

	public void setR10_PURPOSE_VOSTRO(String r10_PURPOSE_VOSTRO) {
		R10_PURPOSE_VOSTRO = r10_PURPOSE_VOSTRO;
	}

	public String getR10_CURRENCY_VOSTRO() {
		return R10_CURRENCY_VOSTRO;
	}

	public void setR10_CURRENCY_VOSTRO(String r10_CURRENCY_VOSTRO) {
		R10_CURRENCY_VOSTRO = r10_CURRENCY_VOSTRO;
	}

	public BigDecimal getR10_AMOUNT_DEMAND_VOSTRO() {
		return R10_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR10_AMOUNT_DEMAND_VOSTRO(BigDecimal r10_AMOUNT_DEMAND_VOSTRO) {
		R10_AMOUNT_DEMAND_VOSTRO = r10_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR10_AMOUNT_TIME_VOSTRO() {
		return R10_AMOUNT_TIME_VOSTRO;
	}

	public void setR10_AMOUNT_TIME_VOSTRO(BigDecimal r10_AMOUNT_TIME_VOSTRO) {
		R10_AMOUNT_TIME_VOSTRO = r10_AMOUNT_TIME_VOSTRO;
	}

	public String getR11_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R11_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR11_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r11_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R11_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r11_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR11_TYPE_OF_ACCOUNT_VOSTRO() {
		return R11_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR11_TYPE_OF_ACCOUNT_VOSTRO(String r11_TYPE_OF_ACCOUNT_VOSTRO) {
		R11_TYPE_OF_ACCOUNT_VOSTRO = r11_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR11_PURPOSE_VOSTRO() {
		return R11_PURPOSE_VOSTRO;
	}

	public void setR11_PURPOSE_VOSTRO(String r11_PURPOSE_VOSTRO) {
		R11_PURPOSE_VOSTRO = r11_PURPOSE_VOSTRO;
	}

	public String getR11_CURRENCY_VOSTRO() {
		return R11_CURRENCY_VOSTRO;
	}

	public void setR11_CURRENCY_VOSTRO(String r11_CURRENCY_VOSTRO) {
		R11_CURRENCY_VOSTRO = r11_CURRENCY_VOSTRO;
	}

	public BigDecimal getR11_AMOUNT_DEMAND_VOSTRO() {
		return R11_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR11_AMOUNT_DEMAND_VOSTRO(BigDecimal r11_AMOUNT_DEMAND_VOSTRO) {
		R11_AMOUNT_DEMAND_VOSTRO = r11_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR11_AMOUNT_TIME_VOSTRO() {
		return R11_AMOUNT_TIME_VOSTRO;
	}

	public void setR11_AMOUNT_TIME_VOSTRO(BigDecimal r11_AMOUNT_TIME_VOSTRO) {
		R11_AMOUNT_TIME_VOSTRO = r11_AMOUNT_TIME_VOSTRO;
	}

	public String getR12_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R12_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR12_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r12_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R12_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r12_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR12_TYPE_OF_ACCOUNT_VOSTRO() {
		return R12_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR12_TYPE_OF_ACCOUNT_VOSTRO(String r12_TYPE_OF_ACCOUNT_VOSTRO) {
		R12_TYPE_OF_ACCOUNT_VOSTRO = r12_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR12_PURPOSE_VOSTRO() {
		return R12_PURPOSE_VOSTRO;
	}

	public void setR12_PURPOSE_VOSTRO(String r12_PURPOSE_VOSTRO) {
		R12_PURPOSE_VOSTRO = r12_PURPOSE_VOSTRO;
	}

	public String getR12_CURRENCY_VOSTRO() {
		return R12_CURRENCY_VOSTRO;
	}

	public void setR12_CURRENCY_VOSTRO(String r12_CURRENCY_VOSTRO) {
		R12_CURRENCY_VOSTRO = r12_CURRENCY_VOSTRO;
	}

	public BigDecimal getR12_AMOUNT_DEMAND_VOSTRO() {
		return R12_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR12_AMOUNT_DEMAND_VOSTRO(BigDecimal r12_AMOUNT_DEMAND_VOSTRO) {
		R12_AMOUNT_DEMAND_VOSTRO = r12_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR12_AMOUNT_TIME_VOSTRO() {
		return R12_AMOUNT_TIME_VOSTRO;
	}

	public void setR12_AMOUNT_TIME_VOSTRO(BigDecimal r12_AMOUNT_TIME_VOSTRO) {
		R12_AMOUNT_TIME_VOSTRO = r12_AMOUNT_TIME_VOSTRO;
	}

	public String getR13_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R13_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR13_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r13_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R13_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r13_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR13_TYPE_OF_ACCOUNT_VOSTRO() {
		return R13_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR13_TYPE_OF_ACCOUNT_VOSTRO(String r13_TYPE_OF_ACCOUNT_VOSTRO) {
		R13_TYPE_OF_ACCOUNT_VOSTRO = r13_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR13_PURPOSE_VOSTRO() {
		return R13_PURPOSE_VOSTRO;
	}

	public void setR13_PURPOSE_VOSTRO(String r13_PURPOSE_VOSTRO) {
		R13_PURPOSE_VOSTRO = r13_PURPOSE_VOSTRO;
	}

	public String getR13_CURRENCY_VOSTRO() {
		return R13_CURRENCY_VOSTRO;
	}

	public void setR13_CURRENCY_VOSTRO(String r13_CURRENCY_VOSTRO) {
		R13_CURRENCY_VOSTRO = r13_CURRENCY_VOSTRO;
	}

	public BigDecimal getR13_AMOUNT_DEMAND_VOSTRO() {
		return R13_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR13_AMOUNT_DEMAND_VOSTRO(BigDecimal r13_AMOUNT_DEMAND_VOSTRO) {
		R13_AMOUNT_DEMAND_VOSTRO = r13_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR13_AMOUNT_TIME_VOSTRO() {
		return R13_AMOUNT_TIME_VOSTRO;
	}

	public void setR13_AMOUNT_TIME_VOSTRO(BigDecimal r13_AMOUNT_TIME_VOSTRO) {
		R13_AMOUNT_TIME_VOSTRO = r13_AMOUNT_TIME_VOSTRO;
	}

	public String getR14_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R14_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR14_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r14_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R14_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r14_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR14_TYPE_OF_ACCOUNT_VOSTRO() {
		return R14_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR14_TYPE_OF_ACCOUNT_VOSTRO(String r14_TYPE_OF_ACCOUNT_VOSTRO) {
		R14_TYPE_OF_ACCOUNT_VOSTRO = r14_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR14_PURPOSE_VOSTRO() {
		return R14_PURPOSE_VOSTRO;
	}

	public void setR14_PURPOSE_VOSTRO(String r14_PURPOSE_VOSTRO) {
		R14_PURPOSE_VOSTRO = r14_PURPOSE_VOSTRO;
	}

	public String getR14_CURRENCY_VOSTRO() {
		return R14_CURRENCY_VOSTRO;
	}

	public void setR14_CURRENCY_VOSTRO(String r14_CURRENCY_VOSTRO) {
		R14_CURRENCY_VOSTRO = r14_CURRENCY_VOSTRO;
	}

	public BigDecimal getR14_AMOUNT_DEMAND_VOSTRO() {
		return R14_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR14_AMOUNT_DEMAND_VOSTRO(BigDecimal r14_AMOUNT_DEMAND_VOSTRO) {
		R14_AMOUNT_DEMAND_VOSTRO = r14_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR14_AMOUNT_TIME_VOSTRO() {
		return R14_AMOUNT_TIME_VOSTRO;
	}

	public void setR14_AMOUNT_TIME_VOSTRO(BigDecimal r14_AMOUNT_TIME_VOSTRO) {
		R14_AMOUNT_TIME_VOSTRO = r14_AMOUNT_TIME_VOSTRO;
	}

	public String getR15_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R15_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR15_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r15_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R15_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r15_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR15_TYPE_OF_ACCOUNT_VOSTRO() {
		return R15_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR15_TYPE_OF_ACCOUNT_VOSTRO(String r15_TYPE_OF_ACCOUNT_VOSTRO) {
		R15_TYPE_OF_ACCOUNT_VOSTRO = r15_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR15_PURPOSE_VOSTRO() {
		return R15_PURPOSE_VOSTRO;
	}

	public void setR15_PURPOSE_VOSTRO(String r15_PURPOSE_VOSTRO) {
		R15_PURPOSE_VOSTRO = r15_PURPOSE_VOSTRO;
	}

	public String getR15_CURRENCY_VOSTRO() {
		return R15_CURRENCY_VOSTRO;
	}

	public void setR15_CURRENCY_VOSTRO(String r15_CURRENCY_VOSTRO) {
		R15_CURRENCY_VOSTRO = r15_CURRENCY_VOSTRO;
	}

	public BigDecimal getR15_AMOUNT_DEMAND_VOSTRO() {
		return R15_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR15_AMOUNT_DEMAND_VOSTRO(BigDecimal r15_AMOUNT_DEMAND_VOSTRO) {
		R15_AMOUNT_DEMAND_VOSTRO = r15_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR15_AMOUNT_TIME_VOSTRO() {
		return R15_AMOUNT_TIME_VOSTRO;
	}

	public void setR15_AMOUNT_TIME_VOSTRO(BigDecimal r15_AMOUNT_TIME_VOSTRO) {
		R15_AMOUNT_TIME_VOSTRO = r15_AMOUNT_TIME_VOSTRO;
	}

	public String getR16_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R16_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR16_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r16_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R16_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r16_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR16_TYPE_OF_ACCOUNT_VOSTRO() {
		return R16_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR16_TYPE_OF_ACCOUNT_VOSTRO(String r16_TYPE_OF_ACCOUNT_VOSTRO) {
		R16_TYPE_OF_ACCOUNT_VOSTRO = r16_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR16_PURPOSE_VOSTRO() {
		return R16_PURPOSE_VOSTRO;
	}

	public void setR16_PURPOSE_VOSTRO(String r16_PURPOSE_VOSTRO) {
		R16_PURPOSE_VOSTRO = r16_PURPOSE_VOSTRO;
	}

	public String getR16_CURRENCY_VOSTRO() {
		return R16_CURRENCY_VOSTRO;
	}

	public void setR16_CURRENCY_VOSTRO(String r16_CURRENCY_VOSTRO) {
		R16_CURRENCY_VOSTRO = r16_CURRENCY_VOSTRO;
	}

	public BigDecimal getR16_AMOUNT_DEMAND_VOSTRO() {
		return R16_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR16_AMOUNT_DEMAND_VOSTRO(BigDecimal r16_AMOUNT_DEMAND_VOSTRO) {
		R16_AMOUNT_DEMAND_VOSTRO = r16_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR16_AMOUNT_TIME_VOSTRO() {
		return R16_AMOUNT_TIME_VOSTRO;
	}

	public void setR16_AMOUNT_TIME_VOSTRO(BigDecimal r16_AMOUNT_TIME_VOSTRO) {
		R16_AMOUNT_TIME_VOSTRO = r16_AMOUNT_TIME_VOSTRO;
	}

	public String getR17_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R17_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR17_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r17_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R17_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r17_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR17_TYPE_OF_ACCOUNT_VOSTRO() {
		return R17_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR17_TYPE_OF_ACCOUNT_VOSTRO(String r17_TYPE_OF_ACCOUNT_VOSTRO) {
		R17_TYPE_OF_ACCOUNT_VOSTRO = r17_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR17_PURPOSE_VOSTRO() {
		return R17_PURPOSE_VOSTRO;
	}

	public void setR17_PURPOSE_VOSTRO(String r17_PURPOSE_VOSTRO) {
		R17_PURPOSE_VOSTRO = r17_PURPOSE_VOSTRO;
	}

	public String getR17_CURRENCY_VOSTRO() {
		return R17_CURRENCY_VOSTRO;
	}

	public void setR17_CURRENCY_VOSTRO(String r17_CURRENCY_VOSTRO) {
		R17_CURRENCY_VOSTRO = r17_CURRENCY_VOSTRO;
	}

	public BigDecimal getR17_AMOUNT_DEMAND_VOSTRO() {
		return R17_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR17_AMOUNT_DEMAND_VOSTRO(BigDecimal r17_AMOUNT_DEMAND_VOSTRO) {
		R17_AMOUNT_DEMAND_VOSTRO = r17_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR17_AMOUNT_TIME_VOSTRO() {
		return R17_AMOUNT_TIME_VOSTRO;
	}

	public void setR17_AMOUNT_TIME_VOSTRO(BigDecimal r17_AMOUNT_TIME_VOSTRO) {
		R17_AMOUNT_TIME_VOSTRO = r17_AMOUNT_TIME_VOSTRO;
	}

	public String getR18_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R18_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR18_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r18_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R18_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r18_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR18_TYPE_OF_ACCOUNT_VOSTRO() {
		return R18_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR18_TYPE_OF_ACCOUNT_VOSTRO(String r18_TYPE_OF_ACCOUNT_VOSTRO) {
		R18_TYPE_OF_ACCOUNT_VOSTRO = r18_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR18_PURPOSE_VOSTRO() {
		return R18_PURPOSE_VOSTRO;
	}

	public void setR18_PURPOSE_VOSTRO(String r18_PURPOSE_VOSTRO) {
		R18_PURPOSE_VOSTRO = r18_PURPOSE_VOSTRO;
	}

	public String getR18_CURRENCY_VOSTRO() {
		return R18_CURRENCY_VOSTRO;
	}

	public void setR18_CURRENCY_VOSTRO(String r18_CURRENCY_VOSTRO) {
		R18_CURRENCY_VOSTRO = r18_CURRENCY_VOSTRO;
	}

	public BigDecimal getR18_AMOUNT_DEMAND_VOSTRO() {
		return R18_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR18_AMOUNT_DEMAND_VOSTRO(BigDecimal r18_AMOUNT_DEMAND_VOSTRO) {
		R18_AMOUNT_DEMAND_VOSTRO = r18_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR18_AMOUNT_TIME_VOSTRO() {
		return R18_AMOUNT_TIME_VOSTRO;
	}

	public void setR18_AMOUNT_TIME_VOSTRO(BigDecimal r18_AMOUNT_TIME_VOSTRO) {
		R18_AMOUNT_TIME_VOSTRO = r18_AMOUNT_TIME_VOSTRO;
	}

	public String getR19_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R19_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR19_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r19_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R19_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r19_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR19_TYPE_OF_ACCOUNT_VOSTRO() {
		return R19_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR19_TYPE_OF_ACCOUNT_VOSTRO(String r19_TYPE_OF_ACCOUNT_VOSTRO) {
		R19_TYPE_OF_ACCOUNT_VOSTRO = r19_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR19_PURPOSE_VOSTRO() {
		return R19_PURPOSE_VOSTRO;
	}

	public void setR19_PURPOSE_VOSTRO(String r19_PURPOSE_VOSTRO) {
		R19_PURPOSE_VOSTRO = r19_PURPOSE_VOSTRO;
	}

	public String getR19_CURRENCY_VOSTRO() {
		return R19_CURRENCY_VOSTRO;
	}

	public void setR19_CURRENCY_VOSTRO(String r19_CURRENCY_VOSTRO) {
		R19_CURRENCY_VOSTRO = r19_CURRENCY_VOSTRO;
	}

	public BigDecimal getR19_AMOUNT_DEMAND_VOSTRO() {
		return R19_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR19_AMOUNT_DEMAND_VOSTRO(BigDecimal r19_AMOUNT_DEMAND_VOSTRO) {
		R19_AMOUNT_DEMAND_VOSTRO = r19_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR19_AMOUNT_TIME_VOSTRO() {
		return R19_AMOUNT_TIME_VOSTRO;
	}

	public void setR19_AMOUNT_TIME_VOSTRO(BigDecimal r19_AMOUNT_TIME_VOSTRO) {
		R19_AMOUNT_TIME_VOSTRO = r19_AMOUNT_TIME_VOSTRO;
	}

	public String getR20_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R20_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR20_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r20_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R20_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r20_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR20_TYPE_OF_ACCOUNT_VOSTRO() {
		return R20_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR20_TYPE_OF_ACCOUNT_VOSTRO(String r20_TYPE_OF_ACCOUNT_VOSTRO) {
		R20_TYPE_OF_ACCOUNT_VOSTRO = r20_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR20_PURPOSE_VOSTRO() {
		return R20_PURPOSE_VOSTRO;
	}

	public void setR20_PURPOSE_VOSTRO(String r20_PURPOSE_VOSTRO) {
		R20_PURPOSE_VOSTRO = r20_PURPOSE_VOSTRO;
	}

	public String getR20_CURRENCY_VOSTRO() {
		return R20_CURRENCY_VOSTRO;
	}

	public void setR20_CURRENCY_VOSTRO(String r20_CURRENCY_VOSTRO) {
		R20_CURRENCY_VOSTRO = r20_CURRENCY_VOSTRO;
	}

	public BigDecimal getR20_AMOUNT_DEMAND_VOSTRO() {
		return R20_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR20_AMOUNT_DEMAND_VOSTRO(BigDecimal r20_AMOUNT_DEMAND_VOSTRO) {
		R20_AMOUNT_DEMAND_VOSTRO = r20_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR20_AMOUNT_TIME_VOSTRO() {
		return R20_AMOUNT_TIME_VOSTRO;
	}

	public void setR20_AMOUNT_TIME_VOSTRO(BigDecimal r20_AMOUNT_TIME_VOSTRO) {
		R20_AMOUNT_TIME_VOSTRO = r20_AMOUNT_TIME_VOSTRO;
	}

	public String getR21_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R21_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR21_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r21_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R21_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r21_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR21_TYPE_OF_ACCOUNT_VOSTRO() {
		return R21_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR21_TYPE_OF_ACCOUNT_VOSTRO(String r21_TYPE_OF_ACCOUNT_VOSTRO) {
		R21_TYPE_OF_ACCOUNT_VOSTRO = r21_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR21_PURPOSE_VOSTRO() {
		return R21_PURPOSE_VOSTRO;
	}

	public void setR21_PURPOSE_VOSTRO(String r21_PURPOSE_VOSTRO) {
		R21_PURPOSE_VOSTRO = r21_PURPOSE_VOSTRO;
	}

	public String getR21_CURRENCY_VOSTRO() {
		return R21_CURRENCY_VOSTRO;
	}

	public void setR21_CURRENCY_VOSTRO(String r21_CURRENCY_VOSTRO) {
		R21_CURRENCY_VOSTRO = r21_CURRENCY_VOSTRO;
	}

	public BigDecimal getR21_AMOUNT_DEMAND_VOSTRO() {
		return R21_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR21_AMOUNT_DEMAND_VOSTRO(BigDecimal r21_AMOUNT_DEMAND_VOSTRO) {
		R21_AMOUNT_DEMAND_VOSTRO = r21_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR21_AMOUNT_TIME_VOSTRO() {
		return R21_AMOUNT_TIME_VOSTRO;
	}

	public void setR21_AMOUNT_TIME_VOSTRO(BigDecimal r21_AMOUNT_TIME_VOSTRO) {
		R21_AMOUNT_TIME_VOSTRO = r21_AMOUNT_TIME_VOSTRO;
	}

	public String getR22_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R22_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR22_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r22_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R22_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r22_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR22_TYPE_OF_ACCOUNT_VOSTRO() {
		return R22_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR22_TYPE_OF_ACCOUNT_VOSTRO(String r22_TYPE_OF_ACCOUNT_VOSTRO) {
		R22_TYPE_OF_ACCOUNT_VOSTRO = r22_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR22_PURPOSE_VOSTRO() {
		return R22_PURPOSE_VOSTRO;
	}

	public void setR22_PURPOSE_VOSTRO(String r22_PURPOSE_VOSTRO) {
		R22_PURPOSE_VOSTRO = r22_PURPOSE_VOSTRO;
	}

	public String getR22_CURRENCY_VOSTRO() {
		return R22_CURRENCY_VOSTRO;
	}

	public void setR22_CURRENCY_VOSTRO(String r22_CURRENCY_VOSTRO) {
		R22_CURRENCY_VOSTRO = r22_CURRENCY_VOSTRO;
	}

	public BigDecimal getR22_AMOUNT_DEMAND_VOSTRO() {
		return R22_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR22_AMOUNT_DEMAND_VOSTRO(BigDecimal r22_AMOUNT_DEMAND_VOSTRO) {
		R22_AMOUNT_DEMAND_VOSTRO = r22_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR22_AMOUNT_TIME_VOSTRO() {
		return R22_AMOUNT_TIME_VOSTRO;
	}

	public void setR22_AMOUNT_TIME_VOSTRO(BigDecimal r22_AMOUNT_TIME_VOSTRO) {
		R22_AMOUNT_TIME_VOSTRO = r22_AMOUNT_TIME_VOSTRO;
	}

	public String getR23_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R23_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR23_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r23_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R23_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r23_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR23_TYPE_OF_ACCOUNT_VOSTRO() {
		return R23_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR23_TYPE_OF_ACCOUNT_VOSTRO(String r23_TYPE_OF_ACCOUNT_VOSTRO) {
		R23_TYPE_OF_ACCOUNT_VOSTRO = r23_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR23_PURPOSE_VOSTRO() {
		return R23_PURPOSE_VOSTRO;
	}

	public void setR23_PURPOSE_VOSTRO(String r23_PURPOSE_VOSTRO) {
		R23_PURPOSE_VOSTRO = r23_PURPOSE_VOSTRO;
	}

	public String getR23_CURRENCY_VOSTRO() {
		return R23_CURRENCY_VOSTRO;
	}

	public void setR23_CURRENCY_VOSTRO(String r23_CURRENCY_VOSTRO) {
		R23_CURRENCY_VOSTRO = r23_CURRENCY_VOSTRO;
	}

	public BigDecimal getR23_AMOUNT_DEMAND_VOSTRO() {
		return R23_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR23_AMOUNT_DEMAND_VOSTRO(BigDecimal r23_AMOUNT_DEMAND_VOSTRO) {
		R23_AMOUNT_DEMAND_VOSTRO = r23_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR23_AMOUNT_TIME_VOSTRO() {
		return R23_AMOUNT_TIME_VOSTRO;
	}

	public void setR23_AMOUNT_TIME_VOSTRO(BigDecimal r23_AMOUNT_TIME_VOSTRO) {
		R23_AMOUNT_TIME_VOSTRO = r23_AMOUNT_TIME_VOSTRO;
	}

	public String getR24_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R24_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR24_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r24_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R24_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r24_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR24_TYPE_OF_ACCOUNT_VOSTRO() {
		return R24_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR24_TYPE_OF_ACCOUNT_VOSTRO(String r24_TYPE_OF_ACCOUNT_VOSTRO) {
		R24_TYPE_OF_ACCOUNT_VOSTRO = r24_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR24_PURPOSE_VOSTRO() {
		return R24_PURPOSE_VOSTRO;
	}

	public void setR24_PURPOSE_VOSTRO(String r24_PURPOSE_VOSTRO) {
		R24_PURPOSE_VOSTRO = r24_PURPOSE_VOSTRO;
	}

	public String getR24_CURRENCY_VOSTRO() {
		return R24_CURRENCY_VOSTRO;
	}

	public void setR24_CURRENCY_VOSTRO(String r24_CURRENCY_VOSTRO) {
		R24_CURRENCY_VOSTRO = r24_CURRENCY_VOSTRO;
	}

	public BigDecimal getR24_AMOUNT_DEMAND_VOSTRO() {
		return R24_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR24_AMOUNT_DEMAND_VOSTRO(BigDecimal r24_AMOUNT_DEMAND_VOSTRO) {
		R24_AMOUNT_DEMAND_VOSTRO = r24_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR24_AMOUNT_TIME_VOSTRO() {
		return R24_AMOUNT_TIME_VOSTRO;
	}

	public void setR24_AMOUNT_TIME_VOSTRO(BigDecimal r24_AMOUNT_TIME_VOSTRO) {
		R24_AMOUNT_TIME_VOSTRO = r24_AMOUNT_TIME_VOSTRO;
	}

	public String getR25_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R25_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR25_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r25_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R25_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r25_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR25_TYPE_OF_ACCOUNT_VOSTRO() {
		return R25_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR25_TYPE_OF_ACCOUNT_VOSTRO(String r25_TYPE_OF_ACCOUNT_VOSTRO) {
		R25_TYPE_OF_ACCOUNT_VOSTRO = r25_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR25_PURPOSE_VOSTRO() {
		return R25_PURPOSE_VOSTRO;
	}

	public void setR25_PURPOSE_VOSTRO(String r25_PURPOSE_VOSTRO) {
		R25_PURPOSE_VOSTRO = r25_PURPOSE_VOSTRO;
	}

	public String getR25_CURRENCY_VOSTRO() {
		return R25_CURRENCY_VOSTRO;
	}

	public void setR25_CURRENCY_VOSTRO(String r25_CURRENCY_VOSTRO) {
		R25_CURRENCY_VOSTRO = r25_CURRENCY_VOSTRO;
	}

	public BigDecimal getR25_AMOUNT_DEMAND_VOSTRO() {
		return R25_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR25_AMOUNT_DEMAND_VOSTRO(BigDecimal r25_AMOUNT_DEMAND_VOSTRO) {
		R25_AMOUNT_DEMAND_VOSTRO = r25_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR25_AMOUNT_TIME_VOSTRO() {
		return R25_AMOUNT_TIME_VOSTRO;
	}

	public void setR25_AMOUNT_TIME_VOSTRO(BigDecimal r25_AMOUNT_TIME_VOSTRO) {
		R25_AMOUNT_TIME_VOSTRO = r25_AMOUNT_TIME_VOSTRO;
	}

	public String getR26_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R26_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR26_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r26_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R26_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r26_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR26_TYPE_OF_ACCOUNT_VOSTRO() {
		return R26_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR26_TYPE_OF_ACCOUNT_VOSTRO(String r26_TYPE_OF_ACCOUNT_VOSTRO) {
		R26_TYPE_OF_ACCOUNT_VOSTRO = r26_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR26_PURPOSE_VOSTRO() {
		return R26_PURPOSE_VOSTRO;
	}

	public void setR26_PURPOSE_VOSTRO(String r26_PURPOSE_VOSTRO) {
		R26_PURPOSE_VOSTRO = r26_PURPOSE_VOSTRO;
	}

	public String getR26_CURRENCY_VOSTRO() {
		return R26_CURRENCY_VOSTRO;
	}

	public void setR26_CURRENCY_VOSTRO(String r26_CURRENCY_VOSTRO) {
		R26_CURRENCY_VOSTRO = r26_CURRENCY_VOSTRO;
	}

	public BigDecimal getR26_AMOUNT_DEMAND_VOSTRO() {
		return R26_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR26_AMOUNT_DEMAND_VOSTRO(BigDecimal r26_AMOUNT_DEMAND_VOSTRO) {
		R26_AMOUNT_DEMAND_VOSTRO = r26_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR26_AMOUNT_TIME_VOSTRO() {
		return R26_AMOUNT_TIME_VOSTRO;
	}

	public void setR26_AMOUNT_TIME_VOSTRO(BigDecimal r26_AMOUNT_TIME_VOSTRO) {
		R26_AMOUNT_TIME_VOSTRO = r26_AMOUNT_TIME_VOSTRO;
	}

	public String getR27_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R27_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR27_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r27_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R27_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r27_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR27_TYPE_OF_ACCOUNT_VOSTRO() {
		return R27_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR27_TYPE_OF_ACCOUNT_VOSTRO(String r27_TYPE_OF_ACCOUNT_VOSTRO) {
		R27_TYPE_OF_ACCOUNT_VOSTRO = r27_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR27_PURPOSE_VOSTRO() {
		return R27_PURPOSE_VOSTRO;
	}

	public void setR27_PURPOSE_VOSTRO(String r27_PURPOSE_VOSTRO) {
		R27_PURPOSE_VOSTRO = r27_PURPOSE_VOSTRO;
	}

	public String getR27_CURRENCY_VOSTRO() {
		return R27_CURRENCY_VOSTRO;
	}

	public void setR27_CURRENCY_VOSTRO(String r27_CURRENCY_VOSTRO) {
		R27_CURRENCY_VOSTRO = r27_CURRENCY_VOSTRO;
	}

	public BigDecimal getR27_AMOUNT_DEMAND_VOSTRO() {
		return R27_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR27_AMOUNT_DEMAND_VOSTRO(BigDecimal r27_AMOUNT_DEMAND_VOSTRO) {
		R27_AMOUNT_DEMAND_VOSTRO = r27_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR27_AMOUNT_TIME_VOSTRO() {
		return R27_AMOUNT_TIME_VOSTRO;
	}

	public void setR27_AMOUNT_TIME_VOSTRO(BigDecimal r27_AMOUNT_TIME_VOSTRO) {
		R27_AMOUNT_TIME_VOSTRO = r27_AMOUNT_TIME_VOSTRO;
	}

	public String getR28_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R28_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR28_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r28_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R28_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r28_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR28_TYPE_OF_ACCOUNT_VOSTRO() {
		return R28_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR28_TYPE_OF_ACCOUNT_VOSTRO(String r28_TYPE_OF_ACCOUNT_VOSTRO) {
		R28_TYPE_OF_ACCOUNT_VOSTRO = r28_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR28_PURPOSE_VOSTRO() {
		return R28_PURPOSE_VOSTRO;
	}

	public void setR28_PURPOSE_VOSTRO(String r28_PURPOSE_VOSTRO) {
		R28_PURPOSE_VOSTRO = r28_PURPOSE_VOSTRO;
	}

	public String getR28_CURRENCY_VOSTRO() {
		return R28_CURRENCY_VOSTRO;
	}

	public void setR28_CURRENCY_VOSTRO(String r28_CURRENCY_VOSTRO) {
		R28_CURRENCY_VOSTRO = r28_CURRENCY_VOSTRO;
	}

	public BigDecimal getR28_AMOUNT_DEMAND_VOSTRO() {
		return R28_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR28_AMOUNT_DEMAND_VOSTRO(BigDecimal r28_AMOUNT_DEMAND_VOSTRO) {
		R28_AMOUNT_DEMAND_VOSTRO = r28_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR28_AMOUNT_TIME_VOSTRO() {
		return R28_AMOUNT_TIME_VOSTRO;
	}

	public void setR28_AMOUNT_TIME_VOSTRO(BigDecimal r28_AMOUNT_TIME_VOSTRO) {
		R28_AMOUNT_TIME_VOSTRO = r28_AMOUNT_TIME_VOSTRO;
	}

	public String getR29_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R29_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR29_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r29_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R29_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r29_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR29_TYPE_OF_ACCOUNT_VOSTRO() {
		return R29_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR29_TYPE_OF_ACCOUNT_VOSTRO(String r29_TYPE_OF_ACCOUNT_VOSTRO) {
		R29_TYPE_OF_ACCOUNT_VOSTRO = r29_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR29_PURPOSE_VOSTRO() {
		return R29_PURPOSE_VOSTRO;
	}

	public void setR29_PURPOSE_VOSTRO(String r29_PURPOSE_VOSTRO) {
		R29_PURPOSE_VOSTRO = r29_PURPOSE_VOSTRO;
	}

	public String getR29_CURRENCY_VOSTRO() {
		return R29_CURRENCY_VOSTRO;
	}

	public void setR29_CURRENCY_VOSTRO(String r29_CURRENCY_VOSTRO) {
		R29_CURRENCY_VOSTRO = r29_CURRENCY_VOSTRO;
	}

	public BigDecimal getR29_AMOUNT_DEMAND_VOSTRO() {
		return R29_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR29_AMOUNT_DEMAND_VOSTRO(BigDecimal r29_AMOUNT_DEMAND_VOSTRO) {
		R29_AMOUNT_DEMAND_VOSTRO = r29_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR29_AMOUNT_TIME_VOSTRO() {
		return R29_AMOUNT_TIME_VOSTRO;
	}

	public void setR29_AMOUNT_TIME_VOSTRO(BigDecimal r29_AMOUNT_TIME_VOSTRO) {
		R29_AMOUNT_TIME_VOSTRO = r29_AMOUNT_TIME_VOSTRO;
	}

	public String getR30_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R30_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR30_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r30_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R30_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r30_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR30_TYPE_OF_ACCOUNT_VOSTRO() {
		return R30_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR30_TYPE_OF_ACCOUNT_VOSTRO(String r30_TYPE_OF_ACCOUNT_VOSTRO) {
		R30_TYPE_OF_ACCOUNT_VOSTRO = r30_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR30_PURPOSE_VOSTRO() {
		return R30_PURPOSE_VOSTRO;
	}

	public void setR30_PURPOSE_VOSTRO(String r30_PURPOSE_VOSTRO) {
		R30_PURPOSE_VOSTRO = r30_PURPOSE_VOSTRO;
	}

	public String getR30_CURRENCY_VOSTRO() {
		return R30_CURRENCY_VOSTRO;
	}

	public void setR30_CURRENCY_VOSTRO(String r30_CURRENCY_VOSTRO) {
		R30_CURRENCY_VOSTRO = r30_CURRENCY_VOSTRO;
	}

	public BigDecimal getR30_AMOUNT_DEMAND_VOSTRO() {
		return R30_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR30_AMOUNT_DEMAND_VOSTRO(BigDecimal r30_AMOUNT_DEMAND_VOSTRO) {
		R30_AMOUNT_DEMAND_VOSTRO = r30_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR30_AMOUNT_TIME_VOSTRO() {
		return R30_AMOUNT_TIME_VOSTRO;
	}

	public void setR30_AMOUNT_TIME_VOSTRO(BigDecimal r30_AMOUNT_TIME_VOSTRO) {
		R30_AMOUNT_TIME_VOSTRO = r30_AMOUNT_TIME_VOSTRO;
	}

	public String getR31_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R31_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR31_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r31_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R31_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r31_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR31_TYPE_OF_ACCOUNT_VOSTRO() {
		return R31_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR31_TYPE_OF_ACCOUNT_VOSTRO(String r31_TYPE_OF_ACCOUNT_VOSTRO) {
		R31_TYPE_OF_ACCOUNT_VOSTRO = r31_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR31_PURPOSE_VOSTRO() {
		return R31_PURPOSE_VOSTRO;
	}

	public void setR31_PURPOSE_VOSTRO(String r31_PURPOSE_VOSTRO) {
		R31_PURPOSE_VOSTRO = r31_PURPOSE_VOSTRO;
	}

	public String getR31_CURRENCY_VOSTRO() {
		return R31_CURRENCY_VOSTRO;
	}

	public void setR31_CURRENCY_VOSTRO(String r31_CURRENCY_VOSTRO) {
		R31_CURRENCY_VOSTRO = r31_CURRENCY_VOSTRO;
	}

	public BigDecimal getR31_AMOUNT_DEMAND_VOSTRO() {
		return R31_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR31_AMOUNT_DEMAND_VOSTRO(BigDecimal r31_AMOUNT_DEMAND_VOSTRO) {
		R31_AMOUNT_DEMAND_VOSTRO = r31_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR31_AMOUNT_TIME_VOSTRO() {
		return R31_AMOUNT_TIME_VOSTRO;
	}

	public void setR31_AMOUNT_TIME_VOSTRO(BigDecimal r31_AMOUNT_TIME_VOSTRO) {
		R31_AMOUNT_TIME_VOSTRO = r31_AMOUNT_TIME_VOSTRO;
	}

	public String getR32_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R32_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR32_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r32_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R32_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r32_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR32_TYPE_OF_ACCOUNT_VOSTRO() {
		return R32_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR32_TYPE_OF_ACCOUNT_VOSTRO(String r32_TYPE_OF_ACCOUNT_VOSTRO) {
		R32_TYPE_OF_ACCOUNT_VOSTRO = r32_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR32_PURPOSE_VOSTRO() {
		return R32_PURPOSE_VOSTRO;
	}

	public void setR32_PURPOSE_VOSTRO(String r32_PURPOSE_VOSTRO) {
		R32_PURPOSE_VOSTRO = r32_PURPOSE_VOSTRO;
	}

	public String getR32_CURRENCY_VOSTRO() {
		return R32_CURRENCY_VOSTRO;
	}

	public void setR32_CURRENCY_VOSTRO(String r32_CURRENCY_VOSTRO) {
		R32_CURRENCY_VOSTRO = r32_CURRENCY_VOSTRO;
	}

	public BigDecimal getR32_AMOUNT_DEMAND_VOSTRO() {
		return R32_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR32_AMOUNT_DEMAND_VOSTRO(BigDecimal r32_AMOUNT_DEMAND_VOSTRO) {
		R32_AMOUNT_DEMAND_VOSTRO = r32_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR32_AMOUNT_TIME_VOSTRO() {
		return R32_AMOUNT_TIME_VOSTRO;
	}

	public void setR32_AMOUNT_TIME_VOSTRO(BigDecimal r32_AMOUNT_TIME_VOSTRO) {
		R32_AMOUNT_TIME_VOSTRO = r32_AMOUNT_TIME_VOSTRO;
	}

	public String getR33_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R33_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR33_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r33_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R33_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r33_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR33_TYPE_OF_ACCOUNT_VOSTRO() {
		return R33_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR33_TYPE_OF_ACCOUNT_VOSTRO(String r33_TYPE_OF_ACCOUNT_VOSTRO) {
		R33_TYPE_OF_ACCOUNT_VOSTRO = r33_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR33_PURPOSE_VOSTRO() {
		return R33_PURPOSE_VOSTRO;
	}

	public void setR33_PURPOSE_VOSTRO(String r33_PURPOSE_VOSTRO) {
		R33_PURPOSE_VOSTRO = r33_PURPOSE_VOSTRO;
	}

	public String getR33_CURRENCY_VOSTRO() {
		return R33_CURRENCY_VOSTRO;
	}

	public void setR33_CURRENCY_VOSTRO(String r33_CURRENCY_VOSTRO) {
		R33_CURRENCY_VOSTRO = r33_CURRENCY_VOSTRO;
	}

	public BigDecimal getR33_AMOUNT_DEMAND_VOSTRO() {
		return R33_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR33_AMOUNT_DEMAND_VOSTRO(BigDecimal r33_AMOUNT_DEMAND_VOSTRO) {
		R33_AMOUNT_DEMAND_VOSTRO = r33_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR33_AMOUNT_TIME_VOSTRO() {
		return R33_AMOUNT_TIME_VOSTRO;
	}

	public void setR33_AMOUNT_TIME_VOSTRO(BigDecimal r33_AMOUNT_TIME_VOSTRO) {
		R33_AMOUNT_TIME_VOSTRO = r33_AMOUNT_TIME_VOSTRO;
	}

	public String getR34_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R34_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR34_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r34_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R34_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r34_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR34_TYPE_OF_ACCOUNT_VOSTRO() {
		return R34_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR34_TYPE_OF_ACCOUNT_VOSTRO(String r34_TYPE_OF_ACCOUNT_VOSTRO) {
		R34_TYPE_OF_ACCOUNT_VOSTRO = r34_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR34_PURPOSE_VOSTRO() {
		return R34_PURPOSE_VOSTRO;
	}

	public void setR34_PURPOSE_VOSTRO(String r34_PURPOSE_VOSTRO) {
		R34_PURPOSE_VOSTRO = r34_PURPOSE_VOSTRO;
	}

	public String getR34_CURRENCY_VOSTRO() {
		return R34_CURRENCY_VOSTRO;
	}

	public void setR34_CURRENCY_VOSTRO(String r34_CURRENCY_VOSTRO) {
		R34_CURRENCY_VOSTRO = r34_CURRENCY_VOSTRO;
	}

	public BigDecimal getR34_AMOUNT_DEMAND_VOSTRO() {
		return R34_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR34_AMOUNT_DEMAND_VOSTRO(BigDecimal r34_AMOUNT_DEMAND_VOSTRO) {
		R34_AMOUNT_DEMAND_VOSTRO = r34_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR34_AMOUNT_TIME_VOSTRO() {
		return R34_AMOUNT_TIME_VOSTRO;
	}

	public void setR34_AMOUNT_TIME_VOSTRO(BigDecimal r34_AMOUNT_TIME_VOSTRO) {
		R34_AMOUNT_TIME_VOSTRO = r34_AMOUNT_TIME_VOSTRO;
	}

	public String getR35_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R35_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR35_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r35_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R35_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r35_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR35_TYPE_OF_ACCOUNT_VOSTRO() {
		return R35_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR35_TYPE_OF_ACCOUNT_VOSTRO(String r35_TYPE_OF_ACCOUNT_VOSTRO) {
		R35_TYPE_OF_ACCOUNT_VOSTRO = r35_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR35_PURPOSE_VOSTRO() {
		return R35_PURPOSE_VOSTRO;
	}

	public void setR35_PURPOSE_VOSTRO(String r35_PURPOSE_VOSTRO) {
		R35_PURPOSE_VOSTRO = r35_PURPOSE_VOSTRO;
	}

	public String getR35_CURRENCY_VOSTRO() {
		return R35_CURRENCY_VOSTRO;
	}

	public void setR35_CURRENCY_VOSTRO(String r35_CURRENCY_VOSTRO) {
		R35_CURRENCY_VOSTRO = r35_CURRENCY_VOSTRO;
	}

	public BigDecimal getR35_AMOUNT_DEMAND_VOSTRO() {
		return R35_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR35_AMOUNT_DEMAND_VOSTRO(BigDecimal r35_AMOUNT_DEMAND_VOSTRO) {
		R35_AMOUNT_DEMAND_VOSTRO = r35_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR35_AMOUNT_TIME_VOSTRO() {
		return R35_AMOUNT_TIME_VOSTRO;
	}

	public void setR35_AMOUNT_TIME_VOSTRO(BigDecimal r35_AMOUNT_TIME_VOSTRO) {
		R35_AMOUNT_TIME_VOSTRO = r35_AMOUNT_TIME_VOSTRO;
	}

	public String getR36_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R36_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR36_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r36_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R36_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r36_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR36_TYPE_OF_ACCOUNT_VOSTRO() {
		return R36_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR36_TYPE_OF_ACCOUNT_VOSTRO(String r36_TYPE_OF_ACCOUNT_VOSTRO) {
		R36_TYPE_OF_ACCOUNT_VOSTRO = r36_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR36_PURPOSE_VOSTRO() {
		return R36_PURPOSE_VOSTRO;
	}

	public void setR36_PURPOSE_VOSTRO(String r36_PURPOSE_VOSTRO) {
		R36_PURPOSE_VOSTRO = r36_PURPOSE_VOSTRO;
	}

	public String getR36_CURRENCY_VOSTRO() {
		return R36_CURRENCY_VOSTRO;
	}

	public void setR36_CURRENCY_VOSTRO(String r36_CURRENCY_VOSTRO) {
		R36_CURRENCY_VOSTRO = r36_CURRENCY_VOSTRO;
	}

	public BigDecimal getR36_AMOUNT_DEMAND_VOSTRO() {
		return R36_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR36_AMOUNT_DEMAND_VOSTRO(BigDecimal r36_AMOUNT_DEMAND_VOSTRO) {
		R36_AMOUNT_DEMAND_VOSTRO = r36_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR36_AMOUNT_TIME_VOSTRO() {
		return R36_AMOUNT_TIME_VOSTRO;
	}

	public void setR36_AMOUNT_TIME_VOSTRO(BigDecimal r36_AMOUNT_TIME_VOSTRO) {
		R36_AMOUNT_TIME_VOSTRO = r36_AMOUNT_TIME_VOSTRO;
	}

	public String getR37_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R37_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR37_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r37_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R37_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r37_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR37_TYPE_OF_ACCOUNT_VOSTRO() {
		return R37_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR37_TYPE_OF_ACCOUNT_VOSTRO(String r37_TYPE_OF_ACCOUNT_VOSTRO) {
		R37_TYPE_OF_ACCOUNT_VOSTRO = r37_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR37_PURPOSE_VOSTRO() {
		return R37_PURPOSE_VOSTRO;
	}

	public void setR37_PURPOSE_VOSTRO(String r37_PURPOSE_VOSTRO) {
		R37_PURPOSE_VOSTRO = r37_PURPOSE_VOSTRO;
	}

	public String getR37_CURRENCY_VOSTRO() {
		return R37_CURRENCY_VOSTRO;
	}

	public void setR37_CURRENCY_VOSTRO(String r37_CURRENCY_VOSTRO) {
		R37_CURRENCY_VOSTRO = r37_CURRENCY_VOSTRO;
	}

	public BigDecimal getR37_AMOUNT_DEMAND_VOSTRO() {
		return R37_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR37_AMOUNT_DEMAND_VOSTRO(BigDecimal r37_AMOUNT_DEMAND_VOSTRO) {
		R37_AMOUNT_DEMAND_VOSTRO = r37_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR37_AMOUNT_TIME_VOSTRO() {
		return R37_AMOUNT_TIME_VOSTRO;
	}

	public void setR37_AMOUNT_TIME_VOSTRO(BigDecimal r37_AMOUNT_TIME_VOSTRO) {
		R37_AMOUNT_TIME_VOSTRO = r37_AMOUNT_TIME_VOSTRO;
	}

	public String getR38_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R38_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR38_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r38_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R38_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r38_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR38_TYPE_OF_ACCOUNT_VOSTRO() {
		return R38_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR38_TYPE_OF_ACCOUNT_VOSTRO(String r38_TYPE_OF_ACCOUNT_VOSTRO) {
		R38_TYPE_OF_ACCOUNT_VOSTRO = r38_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR38_PURPOSE_VOSTRO() {
		return R38_PURPOSE_VOSTRO;
	}

	public void setR38_PURPOSE_VOSTRO(String r38_PURPOSE_VOSTRO) {
		R38_PURPOSE_VOSTRO = r38_PURPOSE_VOSTRO;
	}

	public String getR38_CURRENCY_VOSTRO() {
		return R38_CURRENCY_VOSTRO;
	}

	public void setR38_CURRENCY_VOSTRO(String r38_CURRENCY_VOSTRO) {
		R38_CURRENCY_VOSTRO = r38_CURRENCY_VOSTRO;
	}

	public BigDecimal getR38_AMOUNT_DEMAND_VOSTRO() {
		return R38_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR38_AMOUNT_DEMAND_VOSTRO(BigDecimal r38_AMOUNT_DEMAND_VOSTRO) {
		R38_AMOUNT_DEMAND_VOSTRO = r38_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR38_AMOUNT_TIME_VOSTRO() {
		return R38_AMOUNT_TIME_VOSTRO;
	}

	public void setR38_AMOUNT_TIME_VOSTRO(BigDecimal r38_AMOUNT_TIME_VOSTRO) {
		R38_AMOUNT_TIME_VOSTRO = r38_AMOUNT_TIME_VOSTRO;
	}

	public String getR39_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R39_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR39_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r39_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R39_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r39_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR39_TYPE_OF_ACCOUNT_VOSTRO() {
		return R39_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR39_TYPE_OF_ACCOUNT_VOSTRO(String r39_TYPE_OF_ACCOUNT_VOSTRO) {
		R39_TYPE_OF_ACCOUNT_VOSTRO = r39_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR39_PURPOSE_VOSTRO() {
		return R39_PURPOSE_VOSTRO;
	}

	public void setR39_PURPOSE_VOSTRO(String r39_PURPOSE_VOSTRO) {
		R39_PURPOSE_VOSTRO = r39_PURPOSE_VOSTRO;
	}

	public String getR39_CURRENCY_VOSTRO() {
		return R39_CURRENCY_VOSTRO;
	}

	public void setR39_CURRENCY_VOSTRO(String r39_CURRENCY_VOSTRO) {
		R39_CURRENCY_VOSTRO = r39_CURRENCY_VOSTRO;
	}

	public BigDecimal getR39_AMOUNT_DEMAND_VOSTRO() {
		return R39_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR39_AMOUNT_DEMAND_VOSTRO(BigDecimal r39_AMOUNT_DEMAND_VOSTRO) {
		R39_AMOUNT_DEMAND_VOSTRO = r39_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR39_AMOUNT_TIME_VOSTRO() {
		return R39_AMOUNT_TIME_VOSTRO;
	}

	public void setR39_AMOUNT_TIME_VOSTRO(BigDecimal r39_AMOUNT_TIME_VOSTRO) {
		R39_AMOUNT_TIME_VOSTRO = r39_AMOUNT_TIME_VOSTRO;
	}

	public String getR40_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R40_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR40_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r40_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R40_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r40_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR40_TYPE_OF_ACCOUNT_VOSTRO() {
		return R40_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR40_TYPE_OF_ACCOUNT_VOSTRO(String r40_TYPE_OF_ACCOUNT_VOSTRO) {
		R40_TYPE_OF_ACCOUNT_VOSTRO = r40_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR40_PURPOSE_VOSTRO() {
		return R40_PURPOSE_VOSTRO;
	}

	public void setR40_PURPOSE_VOSTRO(String r40_PURPOSE_VOSTRO) {
		R40_PURPOSE_VOSTRO = r40_PURPOSE_VOSTRO;
	}

	public String getR40_CURRENCY_VOSTRO() {
		return R40_CURRENCY_VOSTRO;
	}

	public void setR40_CURRENCY_VOSTRO(String r40_CURRENCY_VOSTRO) {
		R40_CURRENCY_VOSTRO = r40_CURRENCY_VOSTRO;
	}

	public BigDecimal getR40_AMOUNT_DEMAND_VOSTRO() {
		return R40_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR40_AMOUNT_DEMAND_VOSTRO(BigDecimal r40_AMOUNT_DEMAND_VOSTRO) {
		R40_AMOUNT_DEMAND_VOSTRO = r40_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR40_AMOUNT_TIME_VOSTRO() {
		return R40_AMOUNT_TIME_VOSTRO;
	}

	public void setR40_AMOUNT_TIME_VOSTRO(BigDecimal r40_AMOUNT_TIME_VOSTRO) {
		R40_AMOUNT_TIME_VOSTRO = r40_AMOUNT_TIME_VOSTRO;
	}

	public String getR41_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R41_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR41_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r41_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R41_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r41_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR41_TYPE_OF_ACCOUNT_VOSTRO() {
		return R41_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR41_TYPE_OF_ACCOUNT_VOSTRO(String r41_TYPE_OF_ACCOUNT_VOSTRO) {
		R41_TYPE_OF_ACCOUNT_VOSTRO = r41_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR41_PURPOSE_VOSTRO() {
		return R41_PURPOSE_VOSTRO;
	}

	public void setR41_PURPOSE_VOSTRO(String r41_PURPOSE_VOSTRO) {
		R41_PURPOSE_VOSTRO = r41_PURPOSE_VOSTRO;
	}

	public String getR41_CURRENCY_VOSTRO() {
		return R41_CURRENCY_VOSTRO;
	}

	public void setR41_CURRENCY_VOSTRO(String r41_CURRENCY_VOSTRO) {
		R41_CURRENCY_VOSTRO = r41_CURRENCY_VOSTRO;
	}

	public BigDecimal getR41_AMOUNT_DEMAND_VOSTRO() {
		return R41_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR41_AMOUNT_DEMAND_VOSTRO(BigDecimal r41_AMOUNT_DEMAND_VOSTRO) {
		R41_AMOUNT_DEMAND_VOSTRO = r41_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR41_AMOUNT_TIME_VOSTRO() {
		return R41_AMOUNT_TIME_VOSTRO;
	}

	public void setR41_AMOUNT_TIME_VOSTRO(BigDecimal r41_AMOUNT_TIME_VOSTRO) {
		R41_AMOUNT_TIME_VOSTRO = r41_AMOUNT_TIME_VOSTRO;
	}

	public String getR42_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R42_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR42_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r42_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R42_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r42_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR42_TYPE_OF_ACCOUNT_VOSTRO() {
		return R42_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR42_TYPE_OF_ACCOUNT_VOSTRO(String r42_TYPE_OF_ACCOUNT_VOSTRO) {
		R42_TYPE_OF_ACCOUNT_VOSTRO = r42_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR42_PURPOSE_VOSTRO() {
		return R42_PURPOSE_VOSTRO;
	}

	public void setR42_PURPOSE_VOSTRO(String r42_PURPOSE_VOSTRO) {
		R42_PURPOSE_VOSTRO = r42_PURPOSE_VOSTRO;
	}

	public String getR42_CURRENCY_VOSTRO() {
		return R42_CURRENCY_VOSTRO;
	}

	public void setR42_CURRENCY_VOSTRO(String r42_CURRENCY_VOSTRO) {
		R42_CURRENCY_VOSTRO = r42_CURRENCY_VOSTRO;
	}

	public BigDecimal getR42_AMOUNT_DEMAND_VOSTRO() {
		return R42_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR42_AMOUNT_DEMAND_VOSTRO(BigDecimal r42_AMOUNT_DEMAND_VOSTRO) {
		R42_AMOUNT_DEMAND_VOSTRO = r42_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR42_AMOUNT_TIME_VOSTRO() {
		return R42_AMOUNT_TIME_VOSTRO;
	}

	public void setR42_AMOUNT_TIME_VOSTRO(BigDecimal r42_AMOUNT_TIME_VOSTRO) {
		R42_AMOUNT_TIME_VOSTRO = r42_AMOUNT_TIME_VOSTRO;
	}

	public String getR43_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R43_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR43_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r43_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R43_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r43_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR43_TYPE_OF_ACCOUNT_VOSTRO() {
		return R43_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR43_TYPE_OF_ACCOUNT_VOSTRO(String r43_TYPE_OF_ACCOUNT_VOSTRO) {
		R43_TYPE_OF_ACCOUNT_VOSTRO = r43_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR43_PURPOSE_VOSTRO() {
		return R43_PURPOSE_VOSTRO;
	}

	public void setR43_PURPOSE_VOSTRO(String r43_PURPOSE_VOSTRO) {
		R43_PURPOSE_VOSTRO = r43_PURPOSE_VOSTRO;
	}

	public String getR43_CURRENCY_VOSTRO() {
		return R43_CURRENCY_VOSTRO;
	}

	public void setR43_CURRENCY_VOSTRO(String r43_CURRENCY_VOSTRO) {
		R43_CURRENCY_VOSTRO = r43_CURRENCY_VOSTRO;
	}

	public BigDecimal getR43_AMOUNT_DEMAND_VOSTRO() {
		return R43_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR43_AMOUNT_DEMAND_VOSTRO(BigDecimal r43_AMOUNT_DEMAND_VOSTRO) {
		R43_AMOUNT_DEMAND_VOSTRO = r43_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR43_AMOUNT_TIME_VOSTRO() {
		return R43_AMOUNT_TIME_VOSTRO;
	}

	public void setR43_AMOUNT_TIME_VOSTRO(BigDecimal r43_AMOUNT_TIME_VOSTRO) {
		R43_AMOUNT_TIME_VOSTRO = r43_AMOUNT_TIME_VOSTRO;
	}

	public String getR44_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R44_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR44_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r44_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R44_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r44_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR44_TYPE_OF_ACCOUNT_VOSTRO() {
		return R44_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR44_TYPE_OF_ACCOUNT_VOSTRO(String r44_TYPE_OF_ACCOUNT_VOSTRO) {
		R44_TYPE_OF_ACCOUNT_VOSTRO = r44_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR44_PURPOSE_VOSTRO() {
		return R44_PURPOSE_VOSTRO;
	}

	public void setR44_PURPOSE_VOSTRO(String r44_PURPOSE_VOSTRO) {
		R44_PURPOSE_VOSTRO = r44_PURPOSE_VOSTRO;
	}

	public String getR44_CURRENCY_VOSTRO() {
		return R44_CURRENCY_VOSTRO;
	}

	public void setR44_CURRENCY_VOSTRO(String r44_CURRENCY_VOSTRO) {
		R44_CURRENCY_VOSTRO = r44_CURRENCY_VOSTRO;
	}

	public BigDecimal getR44_AMOUNT_DEMAND_VOSTRO() {
		return R44_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR44_AMOUNT_DEMAND_VOSTRO(BigDecimal r44_AMOUNT_DEMAND_VOSTRO) {
		R44_AMOUNT_DEMAND_VOSTRO = r44_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR44_AMOUNT_TIME_VOSTRO() {
		return R44_AMOUNT_TIME_VOSTRO;
	}

	public void setR44_AMOUNT_TIME_VOSTRO(BigDecimal r44_AMOUNT_TIME_VOSTRO) {
		R44_AMOUNT_TIME_VOSTRO = r44_AMOUNT_TIME_VOSTRO;
	}

	public String getR45_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R45_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR45_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r45_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R45_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r45_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR45_TYPE_OF_ACCOUNT_VOSTRO() {
		return R45_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR45_TYPE_OF_ACCOUNT_VOSTRO(String r45_TYPE_OF_ACCOUNT_VOSTRO) {
		R45_TYPE_OF_ACCOUNT_VOSTRO = r45_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR45_PURPOSE_VOSTRO() {
		return R45_PURPOSE_VOSTRO;
	}

	public void setR45_PURPOSE_VOSTRO(String r45_PURPOSE_VOSTRO) {
		R45_PURPOSE_VOSTRO = r45_PURPOSE_VOSTRO;
	}

	public String getR45_CURRENCY_VOSTRO() {
		return R45_CURRENCY_VOSTRO;
	}

	public void setR45_CURRENCY_VOSTRO(String r45_CURRENCY_VOSTRO) {
		R45_CURRENCY_VOSTRO = r45_CURRENCY_VOSTRO;
	}

	public BigDecimal getR45_AMOUNT_DEMAND_VOSTRO() {
		return R45_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR45_AMOUNT_DEMAND_VOSTRO(BigDecimal r45_AMOUNT_DEMAND_VOSTRO) {
		R45_AMOUNT_DEMAND_VOSTRO = r45_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR45_AMOUNT_TIME_VOSTRO() {
		return R45_AMOUNT_TIME_VOSTRO;
	}

	public void setR45_AMOUNT_TIME_VOSTRO(BigDecimal r45_AMOUNT_TIME_VOSTRO) {
		R45_AMOUNT_TIME_VOSTRO = r45_AMOUNT_TIME_VOSTRO;
	}

	public String getR46_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R46_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR46_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r46_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R46_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r46_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR46_TYPE_OF_ACCOUNT_VOSTRO() {
		return R46_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR46_TYPE_OF_ACCOUNT_VOSTRO(String r46_TYPE_OF_ACCOUNT_VOSTRO) {
		R46_TYPE_OF_ACCOUNT_VOSTRO = r46_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR46_PURPOSE_VOSTRO() {
		return R46_PURPOSE_VOSTRO;
	}

	public void setR46_PURPOSE_VOSTRO(String r46_PURPOSE_VOSTRO) {
		R46_PURPOSE_VOSTRO = r46_PURPOSE_VOSTRO;
	}

	public String getR46_CURRENCY_VOSTRO() {
		return R46_CURRENCY_VOSTRO;
	}

	public void setR46_CURRENCY_VOSTRO(String r46_CURRENCY_VOSTRO) {
		R46_CURRENCY_VOSTRO = r46_CURRENCY_VOSTRO;
	}

	public BigDecimal getR46_AMOUNT_DEMAND_VOSTRO() {
		return R46_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR46_AMOUNT_DEMAND_VOSTRO(BigDecimal r46_AMOUNT_DEMAND_VOSTRO) {
		R46_AMOUNT_DEMAND_VOSTRO = r46_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR46_AMOUNT_TIME_VOSTRO() {
		return R46_AMOUNT_TIME_VOSTRO;
	}

	public void setR46_AMOUNT_TIME_VOSTRO(BigDecimal r46_AMOUNT_TIME_VOSTRO) {
		R46_AMOUNT_TIME_VOSTRO = r46_AMOUNT_TIME_VOSTRO;
	}

	public String getR47_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R47_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR47_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r47_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R47_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r47_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR47_TYPE_OF_ACCOUNT_VOSTRO() {
		return R47_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR47_TYPE_OF_ACCOUNT_VOSTRO(String r47_TYPE_OF_ACCOUNT_VOSTRO) {
		R47_TYPE_OF_ACCOUNT_VOSTRO = r47_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR47_PURPOSE_VOSTRO() {
		return R47_PURPOSE_VOSTRO;
	}

	public void setR47_PURPOSE_VOSTRO(String r47_PURPOSE_VOSTRO) {
		R47_PURPOSE_VOSTRO = r47_PURPOSE_VOSTRO;
	}

	public String getR47_CURRENCY_VOSTRO() {
		return R47_CURRENCY_VOSTRO;
	}

	public void setR47_CURRENCY_VOSTRO(String r47_CURRENCY_VOSTRO) {
		R47_CURRENCY_VOSTRO = r47_CURRENCY_VOSTRO;
	}

	public BigDecimal getR47_AMOUNT_DEMAND_VOSTRO() {
		return R47_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR47_AMOUNT_DEMAND_VOSTRO(BigDecimal r47_AMOUNT_DEMAND_VOSTRO) {
		R47_AMOUNT_DEMAND_VOSTRO = r47_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR47_AMOUNT_TIME_VOSTRO() {
		return R47_AMOUNT_TIME_VOSTRO;
	}

	public void setR47_AMOUNT_TIME_VOSTRO(BigDecimal r47_AMOUNT_TIME_VOSTRO) {
		R47_AMOUNT_TIME_VOSTRO = r47_AMOUNT_TIME_VOSTRO;
	}

	public String getR48_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R48_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR48_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r48_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R48_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r48_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR48_TYPE_OF_ACCOUNT_VOSTRO() {
		return R48_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR48_TYPE_OF_ACCOUNT_VOSTRO(String r48_TYPE_OF_ACCOUNT_VOSTRO) {
		R48_TYPE_OF_ACCOUNT_VOSTRO = r48_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR48_PURPOSE_VOSTRO() {
		return R48_PURPOSE_VOSTRO;
	}

	public void setR48_PURPOSE_VOSTRO(String r48_PURPOSE_VOSTRO) {
		R48_PURPOSE_VOSTRO = r48_PURPOSE_VOSTRO;
	}

	public String getR48_CURRENCY_VOSTRO() {
		return R48_CURRENCY_VOSTRO;
	}

	public void setR48_CURRENCY_VOSTRO(String r48_CURRENCY_VOSTRO) {
		R48_CURRENCY_VOSTRO = r48_CURRENCY_VOSTRO;
	}

	public BigDecimal getR48_AMOUNT_DEMAND_VOSTRO() {
		return R48_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR48_AMOUNT_DEMAND_VOSTRO(BigDecimal r48_AMOUNT_DEMAND_VOSTRO) {
		R48_AMOUNT_DEMAND_VOSTRO = r48_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR48_AMOUNT_TIME_VOSTRO() {
		return R48_AMOUNT_TIME_VOSTRO;
	}

	public void setR48_AMOUNT_TIME_VOSTRO(BigDecimal r48_AMOUNT_TIME_VOSTRO) {
		R48_AMOUNT_TIME_VOSTRO = r48_AMOUNT_TIME_VOSTRO;
	}

	public String getR49_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R49_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR49_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r49_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R49_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r49_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR49_TYPE_OF_ACCOUNT_VOSTRO() {
		return R49_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR49_TYPE_OF_ACCOUNT_VOSTRO(String r49_TYPE_OF_ACCOUNT_VOSTRO) {
		R49_TYPE_OF_ACCOUNT_VOSTRO = r49_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR49_PURPOSE_VOSTRO() {
		return R49_PURPOSE_VOSTRO;
	}

	public void setR49_PURPOSE_VOSTRO(String r49_PURPOSE_VOSTRO) {
		R49_PURPOSE_VOSTRO = r49_PURPOSE_VOSTRO;
	}

	public String getR49_CURRENCY_VOSTRO() {
		return R49_CURRENCY_VOSTRO;
	}

	public void setR49_CURRENCY_VOSTRO(String r49_CURRENCY_VOSTRO) {
		R49_CURRENCY_VOSTRO = r49_CURRENCY_VOSTRO;
	}

	public BigDecimal getR49_AMOUNT_DEMAND_VOSTRO() {
		return R49_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR49_AMOUNT_DEMAND_VOSTRO(BigDecimal r49_AMOUNT_DEMAND_VOSTRO) {
		R49_AMOUNT_DEMAND_VOSTRO = r49_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR49_AMOUNT_TIME_VOSTRO() {
		return R49_AMOUNT_TIME_VOSTRO;
	}

	public void setR49_AMOUNT_TIME_VOSTRO(BigDecimal r49_AMOUNT_TIME_VOSTRO) {
		R49_AMOUNT_TIME_VOSTRO = r49_AMOUNT_TIME_VOSTRO;
	}

	public String getR50_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R50_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR50_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r50_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R50_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r50_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR50_TYPE_OF_ACCOUNT_VOSTRO() {
		return R50_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR50_TYPE_OF_ACCOUNT_VOSTRO(String r50_TYPE_OF_ACCOUNT_VOSTRO) {
		R50_TYPE_OF_ACCOUNT_VOSTRO = r50_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR50_PURPOSE_VOSTRO() {
		return R50_PURPOSE_VOSTRO;
	}

	public void setR50_PURPOSE_VOSTRO(String r50_PURPOSE_VOSTRO) {
		R50_PURPOSE_VOSTRO = r50_PURPOSE_VOSTRO;
	}

	public String getR50_CURRENCY_VOSTRO() {
		return R50_CURRENCY_VOSTRO;
	}

	public void setR50_CURRENCY_VOSTRO(String r50_CURRENCY_VOSTRO) {
		R50_CURRENCY_VOSTRO = r50_CURRENCY_VOSTRO;
	}

	public BigDecimal getR50_AMOUNT_DEMAND_VOSTRO() {
		return R50_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR50_AMOUNT_DEMAND_VOSTRO(BigDecimal r50_AMOUNT_DEMAND_VOSTRO) {
		R50_AMOUNT_DEMAND_VOSTRO = r50_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR50_AMOUNT_TIME_VOSTRO() {
		return R50_AMOUNT_TIME_VOSTRO;
	}

	public void setR50_AMOUNT_TIME_VOSTRO(BigDecimal r50_AMOUNT_TIME_VOSTRO) {
		R50_AMOUNT_TIME_VOSTRO = r50_AMOUNT_TIME_VOSTRO;
	}

	public String getR51_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R51_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR51_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r51_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R51_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r51_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR51_TYPE_OF_ACCOUNT_VOSTRO() {
		return R51_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR51_TYPE_OF_ACCOUNT_VOSTRO(String r51_TYPE_OF_ACCOUNT_VOSTRO) {
		R51_TYPE_OF_ACCOUNT_VOSTRO = r51_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR51_PURPOSE_VOSTRO() {
		return R51_PURPOSE_VOSTRO;
	}

	public void setR51_PURPOSE_VOSTRO(String r51_PURPOSE_VOSTRO) {
		R51_PURPOSE_VOSTRO = r51_PURPOSE_VOSTRO;
	}

	public String getR51_CURRENCY_VOSTRO() {
		return R51_CURRENCY_VOSTRO;
	}

	public void setR51_CURRENCY_VOSTRO(String r51_CURRENCY_VOSTRO) {
		R51_CURRENCY_VOSTRO = r51_CURRENCY_VOSTRO;
	}

	public BigDecimal getR51_AMOUNT_DEMAND_VOSTRO() {
		return R51_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR51_AMOUNT_DEMAND_VOSTRO(BigDecimal r51_AMOUNT_DEMAND_VOSTRO) {
		R51_AMOUNT_DEMAND_VOSTRO = r51_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR51_AMOUNT_TIME_VOSTRO() {
		return R51_AMOUNT_TIME_VOSTRO;
	}

	public void setR51_AMOUNT_TIME_VOSTRO(BigDecimal r51_AMOUNT_TIME_VOSTRO) {
		R51_AMOUNT_TIME_VOSTRO = r51_AMOUNT_TIME_VOSTRO;
	}

	public String getR52_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R52_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR52_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r52_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R52_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r52_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR52_TYPE_OF_ACCOUNT_VOSTRO() {
		return R52_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR52_TYPE_OF_ACCOUNT_VOSTRO(String r52_TYPE_OF_ACCOUNT_VOSTRO) {
		R52_TYPE_OF_ACCOUNT_VOSTRO = r52_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR52_PURPOSE_VOSTRO() {
		return R52_PURPOSE_VOSTRO;
	}

	public void setR52_PURPOSE_VOSTRO(String r52_PURPOSE_VOSTRO) {
		R52_PURPOSE_VOSTRO = r52_PURPOSE_VOSTRO;
	}

	public String getR52_CURRENCY_VOSTRO() {
		return R52_CURRENCY_VOSTRO;
	}

	public void setR52_CURRENCY_VOSTRO(String r52_CURRENCY_VOSTRO) {
		R52_CURRENCY_VOSTRO = r52_CURRENCY_VOSTRO;
	}

	public BigDecimal getR52_AMOUNT_DEMAND_VOSTRO() {
		return R52_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR52_AMOUNT_DEMAND_VOSTRO(BigDecimal r52_AMOUNT_DEMAND_VOSTRO) {
		R52_AMOUNT_DEMAND_VOSTRO = r52_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR52_AMOUNT_TIME_VOSTRO() {
		return R52_AMOUNT_TIME_VOSTRO;
	}

	public void setR52_AMOUNT_TIME_VOSTRO(BigDecimal r52_AMOUNT_TIME_VOSTRO) {
		R52_AMOUNT_TIME_VOSTRO = r52_AMOUNT_TIME_VOSTRO;
	}

	public String getR53_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R53_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR53_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r53_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R53_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r53_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR53_TYPE_OF_ACCOUNT_VOSTRO() {
		return R53_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR53_TYPE_OF_ACCOUNT_VOSTRO(String r53_TYPE_OF_ACCOUNT_VOSTRO) {
		R53_TYPE_OF_ACCOUNT_VOSTRO = r53_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR53_PURPOSE_VOSTRO() {
		return R53_PURPOSE_VOSTRO;
	}

	public void setR53_PURPOSE_VOSTRO(String r53_PURPOSE_VOSTRO) {
		R53_PURPOSE_VOSTRO = r53_PURPOSE_VOSTRO;
	}

	public String getR53_CURRENCY_VOSTRO() {
		return R53_CURRENCY_VOSTRO;
	}

	public void setR53_CURRENCY_VOSTRO(String r53_CURRENCY_VOSTRO) {
		R53_CURRENCY_VOSTRO = r53_CURRENCY_VOSTRO;
	}

	public BigDecimal getR53_AMOUNT_DEMAND_VOSTRO() {
		return R53_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR53_AMOUNT_DEMAND_VOSTRO(BigDecimal r53_AMOUNT_DEMAND_VOSTRO) {
		R53_AMOUNT_DEMAND_VOSTRO = r53_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR53_AMOUNT_TIME_VOSTRO() {
		return R53_AMOUNT_TIME_VOSTRO;
	}

	public void setR53_AMOUNT_TIME_VOSTRO(BigDecimal r53_AMOUNT_TIME_VOSTRO) {
		R53_AMOUNT_TIME_VOSTRO = r53_AMOUNT_TIME_VOSTRO;
	}

	public String getR54_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R54_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR54_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r54_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R54_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r54_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR54_TYPE_OF_ACCOUNT_VOSTRO() {
		return R54_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR54_TYPE_OF_ACCOUNT_VOSTRO(String r54_TYPE_OF_ACCOUNT_VOSTRO) {
		R54_TYPE_OF_ACCOUNT_VOSTRO = r54_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR54_PURPOSE_VOSTRO() {
		return R54_PURPOSE_VOSTRO;
	}

	public void setR54_PURPOSE_VOSTRO(String r54_PURPOSE_VOSTRO) {
		R54_PURPOSE_VOSTRO = r54_PURPOSE_VOSTRO;
	}

	public String getR54_CURRENCY_VOSTRO() {
		return R54_CURRENCY_VOSTRO;
	}

	public void setR54_CURRENCY_VOSTRO(String r54_CURRENCY_VOSTRO) {
		R54_CURRENCY_VOSTRO = r54_CURRENCY_VOSTRO;
	}

	public BigDecimal getR54_AMOUNT_DEMAND_VOSTRO() {
		return R54_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR54_AMOUNT_DEMAND_VOSTRO(BigDecimal r54_AMOUNT_DEMAND_VOSTRO) {
		R54_AMOUNT_DEMAND_VOSTRO = r54_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR54_AMOUNT_TIME_VOSTRO() {
		return R54_AMOUNT_TIME_VOSTRO;
	}

	public void setR54_AMOUNT_TIME_VOSTRO(BigDecimal r54_AMOUNT_TIME_VOSTRO) {
		R54_AMOUNT_TIME_VOSTRO = r54_AMOUNT_TIME_VOSTRO;
	}

	public String getR55_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R55_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR55_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r55_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R55_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r55_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR55_TYPE_OF_ACCOUNT_VOSTRO() {
		return R55_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR55_TYPE_OF_ACCOUNT_VOSTRO(String r55_TYPE_OF_ACCOUNT_VOSTRO) {
		R55_TYPE_OF_ACCOUNT_VOSTRO = r55_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR55_PURPOSE_VOSTRO() {
		return R55_PURPOSE_VOSTRO;
	}

	public void setR55_PURPOSE_VOSTRO(String r55_PURPOSE_VOSTRO) {
		R55_PURPOSE_VOSTRO = r55_PURPOSE_VOSTRO;
	}

	public String getR55_CURRENCY_VOSTRO() {
		return R55_CURRENCY_VOSTRO;
	}

	public void setR55_CURRENCY_VOSTRO(String r55_CURRENCY_VOSTRO) {
		R55_CURRENCY_VOSTRO = r55_CURRENCY_VOSTRO;
	}

	public BigDecimal getR55_AMOUNT_DEMAND_VOSTRO() {
		return R55_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR55_AMOUNT_DEMAND_VOSTRO(BigDecimal r55_AMOUNT_DEMAND_VOSTRO) {
		R55_AMOUNT_DEMAND_VOSTRO = r55_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR55_AMOUNT_TIME_VOSTRO() {
		return R55_AMOUNT_TIME_VOSTRO;
	}

	public void setR55_AMOUNT_TIME_VOSTRO(BigDecimal r55_AMOUNT_TIME_VOSTRO) {
		R55_AMOUNT_TIME_VOSTRO = r55_AMOUNT_TIME_VOSTRO;
	}

	public String getR56_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R56_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR56_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r56_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R56_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r56_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR56_TYPE_OF_ACCOUNT_VOSTRO() {
		return R56_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR56_TYPE_OF_ACCOUNT_VOSTRO(String r56_TYPE_OF_ACCOUNT_VOSTRO) {
		R56_TYPE_OF_ACCOUNT_VOSTRO = r56_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR56_PURPOSE_VOSTRO() {
		return R56_PURPOSE_VOSTRO;
	}

	public void setR56_PURPOSE_VOSTRO(String r56_PURPOSE_VOSTRO) {
		R56_PURPOSE_VOSTRO = r56_PURPOSE_VOSTRO;
	}

	public String getR56_CURRENCY_VOSTRO() {
		return R56_CURRENCY_VOSTRO;
	}

	public void setR56_CURRENCY_VOSTRO(String r56_CURRENCY_VOSTRO) {
		R56_CURRENCY_VOSTRO = r56_CURRENCY_VOSTRO;
	}

	public BigDecimal getR56_AMOUNT_DEMAND_VOSTRO() {
		return R56_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR56_AMOUNT_DEMAND_VOSTRO(BigDecimal r56_AMOUNT_DEMAND_VOSTRO) {
		R56_AMOUNT_DEMAND_VOSTRO = r56_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR56_AMOUNT_TIME_VOSTRO() {
		return R56_AMOUNT_TIME_VOSTRO;
	}

	public void setR56_AMOUNT_TIME_VOSTRO(BigDecimal r56_AMOUNT_TIME_VOSTRO) {
		R56_AMOUNT_TIME_VOSTRO = r56_AMOUNT_TIME_VOSTRO;
	}

	public String getR57_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R57_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR57_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r57_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R57_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r57_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR57_TYPE_OF_ACCOUNT_VOSTRO() {
		return R57_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR57_TYPE_OF_ACCOUNT_VOSTRO(String r57_TYPE_OF_ACCOUNT_VOSTRO) {
		R57_TYPE_OF_ACCOUNT_VOSTRO = r57_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR57_PURPOSE_VOSTRO() {
		return R57_PURPOSE_VOSTRO;
	}

	public void setR57_PURPOSE_VOSTRO(String r57_PURPOSE_VOSTRO) {
		R57_PURPOSE_VOSTRO = r57_PURPOSE_VOSTRO;
	}

	public String getR57_CURRENCY_VOSTRO() {
		return R57_CURRENCY_VOSTRO;
	}

	public void setR57_CURRENCY_VOSTRO(String r57_CURRENCY_VOSTRO) {
		R57_CURRENCY_VOSTRO = r57_CURRENCY_VOSTRO;
	}

	public BigDecimal getR57_AMOUNT_DEMAND_VOSTRO() {
		return R57_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR57_AMOUNT_DEMAND_VOSTRO(BigDecimal r57_AMOUNT_DEMAND_VOSTRO) {
		R57_AMOUNT_DEMAND_VOSTRO = r57_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR57_AMOUNT_TIME_VOSTRO() {
		return R57_AMOUNT_TIME_VOSTRO;
	}

	public void setR57_AMOUNT_TIME_VOSTRO(BigDecimal r57_AMOUNT_TIME_VOSTRO) {
		R57_AMOUNT_TIME_VOSTRO = r57_AMOUNT_TIME_VOSTRO;
	}

	public String getR58_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R58_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR58_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r58_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R58_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r58_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR58_TYPE_OF_ACCOUNT_VOSTRO() {
		return R58_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR58_TYPE_OF_ACCOUNT_VOSTRO(String r58_TYPE_OF_ACCOUNT_VOSTRO) {
		R58_TYPE_OF_ACCOUNT_VOSTRO = r58_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR58_PURPOSE_VOSTRO() {
		return R58_PURPOSE_VOSTRO;
	}

	public void setR58_PURPOSE_VOSTRO(String r58_PURPOSE_VOSTRO) {
		R58_PURPOSE_VOSTRO = r58_PURPOSE_VOSTRO;
	}

	public String getR58_CURRENCY_VOSTRO() {
		return R58_CURRENCY_VOSTRO;
	}

	public void setR58_CURRENCY_VOSTRO(String r58_CURRENCY_VOSTRO) {
		R58_CURRENCY_VOSTRO = r58_CURRENCY_VOSTRO;
	}

	public BigDecimal getR58_AMOUNT_DEMAND_VOSTRO() {
		return R58_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR58_AMOUNT_DEMAND_VOSTRO(BigDecimal r58_AMOUNT_DEMAND_VOSTRO) {
		R58_AMOUNT_DEMAND_VOSTRO = r58_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR58_AMOUNT_TIME_VOSTRO() {
		return R58_AMOUNT_TIME_VOSTRO;
	}

	public void setR58_AMOUNT_TIME_VOSTRO(BigDecimal r58_AMOUNT_TIME_VOSTRO) {
		R58_AMOUNT_TIME_VOSTRO = r58_AMOUNT_TIME_VOSTRO;
	}

	public String getR59_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R59_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR59_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r59_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R59_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r59_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR59_TYPE_OF_ACCOUNT_VOSTRO() {
		return R59_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR59_TYPE_OF_ACCOUNT_VOSTRO(String r59_TYPE_OF_ACCOUNT_VOSTRO) {
		R59_TYPE_OF_ACCOUNT_VOSTRO = r59_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR59_PURPOSE_VOSTRO() {
		return R59_PURPOSE_VOSTRO;
	}

	public void setR59_PURPOSE_VOSTRO(String r59_PURPOSE_VOSTRO) {
		R59_PURPOSE_VOSTRO = r59_PURPOSE_VOSTRO;
	}

	public String getR59_CURRENCY_VOSTRO() {
		return R59_CURRENCY_VOSTRO;
	}

	public void setR59_CURRENCY_VOSTRO(String r59_CURRENCY_VOSTRO) {
		R59_CURRENCY_VOSTRO = r59_CURRENCY_VOSTRO;
	}

	public BigDecimal getR59_AMOUNT_DEMAND_VOSTRO() {
		return R59_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR59_AMOUNT_DEMAND_VOSTRO(BigDecimal r59_AMOUNT_DEMAND_VOSTRO) {
		R59_AMOUNT_DEMAND_VOSTRO = r59_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR59_AMOUNT_TIME_VOSTRO() {
		return R59_AMOUNT_TIME_VOSTRO;
	}

	public void setR59_AMOUNT_TIME_VOSTRO(BigDecimal r59_AMOUNT_TIME_VOSTRO) {
		R59_AMOUNT_TIME_VOSTRO = r59_AMOUNT_TIME_VOSTRO;
	}

	public String getR60_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R60_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR60_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r60_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R60_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r60_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR60_TYPE_OF_ACCOUNT_VOSTRO() {
		return R60_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR60_TYPE_OF_ACCOUNT_VOSTRO(String r60_TYPE_OF_ACCOUNT_VOSTRO) {
		R60_TYPE_OF_ACCOUNT_VOSTRO = r60_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR60_PURPOSE_VOSTRO() {
		return R60_PURPOSE_VOSTRO;
	}

	public void setR60_PURPOSE_VOSTRO(String r60_PURPOSE_VOSTRO) {
		R60_PURPOSE_VOSTRO = r60_PURPOSE_VOSTRO;
	}

	public String getR60_CURRENCY_VOSTRO() {
		return R60_CURRENCY_VOSTRO;
	}

	public void setR60_CURRENCY_VOSTRO(String r60_CURRENCY_VOSTRO) {
		R60_CURRENCY_VOSTRO = r60_CURRENCY_VOSTRO;
	}

	public BigDecimal getR60_AMOUNT_DEMAND_VOSTRO() {
		return R60_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR60_AMOUNT_DEMAND_VOSTRO(BigDecimal r60_AMOUNT_DEMAND_VOSTRO) {
		R60_AMOUNT_DEMAND_VOSTRO = r60_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR60_AMOUNT_TIME_VOSTRO() {
		return R60_AMOUNT_TIME_VOSTRO;
	}

	public void setR60_AMOUNT_TIME_VOSTRO(BigDecimal r60_AMOUNT_TIME_VOSTRO) {
		R60_AMOUNT_TIME_VOSTRO = r60_AMOUNT_TIME_VOSTRO;
	}

	public String getR61_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R61_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR61_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r61_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R61_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r61_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR61_TYPE_OF_ACCOUNT_VOSTRO() {
		return R61_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR61_TYPE_OF_ACCOUNT_VOSTRO(String r61_TYPE_OF_ACCOUNT_VOSTRO) {
		R61_TYPE_OF_ACCOUNT_VOSTRO = r61_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR61_PURPOSE_VOSTRO() {
		return R61_PURPOSE_VOSTRO;
	}

	public void setR61_PURPOSE_VOSTRO(String r61_PURPOSE_VOSTRO) {
		R61_PURPOSE_VOSTRO = r61_PURPOSE_VOSTRO;
	}

	public String getR61_CURRENCY_VOSTRO() {
		return R61_CURRENCY_VOSTRO;
	}

	public void setR61_CURRENCY_VOSTRO(String r61_CURRENCY_VOSTRO) {
		R61_CURRENCY_VOSTRO = r61_CURRENCY_VOSTRO;
	}

	public BigDecimal getR61_AMOUNT_DEMAND_VOSTRO() {
		return R61_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR61_AMOUNT_DEMAND_VOSTRO(BigDecimal r61_AMOUNT_DEMAND_VOSTRO) {
		R61_AMOUNT_DEMAND_VOSTRO = r61_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR61_AMOUNT_TIME_VOSTRO() {
		return R61_AMOUNT_TIME_VOSTRO;
	}

	public void setR61_AMOUNT_TIME_VOSTRO(BigDecimal r61_AMOUNT_TIME_VOSTRO) {
		R61_AMOUNT_TIME_VOSTRO = r61_AMOUNT_TIME_VOSTRO;
	}

	public String getR62_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R62_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR62_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r62_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R62_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r62_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR62_TYPE_OF_ACCOUNT_VOSTRO() {
		return R62_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR62_TYPE_OF_ACCOUNT_VOSTRO(String r62_TYPE_OF_ACCOUNT_VOSTRO) {
		R62_TYPE_OF_ACCOUNT_VOSTRO = r62_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR62_PURPOSE_VOSTRO() {
		return R62_PURPOSE_VOSTRO;
	}

	public void setR62_PURPOSE_VOSTRO(String r62_PURPOSE_VOSTRO) {
		R62_PURPOSE_VOSTRO = r62_PURPOSE_VOSTRO;
	}

	public String getR62_CURRENCY_VOSTRO() {
		return R62_CURRENCY_VOSTRO;
	}

	public void setR62_CURRENCY_VOSTRO(String r62_CURRENCY_VOSTRO) {
		R62_CURRENCY_VOSTRO = r62_CURRENCY_VOSTRO;
	}

	public BigDecimal getR62_AMOUNT_DEMAND_VOSTRO() {
		return R62_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR62_AMOUNT_DEMAND_VOSTRO(BigDecimal r62_AMOUNT_DEMAND_VOSTRO) {
		R62_AMOUNT_DEMAND_VOSTRO = r62_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR62_AMOUNT_TIME_VOSTRO() {
		return R62_AMOUNT_TIME_VOSTRO;
	}

	public void setR62_AMOUNT_TIME_VOSTRO(BigDecimal r62_AMOUNT_TIME_VOSTRO) {
		R62_AMOUNT_TIME_VOSTRO = r62_AMOUNT_TIME_VOSTRO;
	}

	public String getR63_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R63_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR63_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r63_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R63_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r63_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR63_TYPE_OF_ACCOUNT_VOSTRO() {
		return R63_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR63_TYPE_OF_ACCOUNT_VOSTRO(String r63_TYPE_OF_ACCOUNT_VOSTRO) {
		R63_TYPE_OF_ACCOUNT_VOSTRO = r63_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR63_PURPOSE_VOSTRO() {
		return R63_PURPOSE_VOSTRO;
	}

	public void setR63_PURPOSE_VOSTRO(String r63_PURPOSE_VOSTRO) {
		R63_PURPOSE_VOSTRO = r63_PURPOSE_VOSTRO;
	}

	public String getR63_CURRENCY_VOSTRO() {
		return R63_CURRENCY_VOSTRO;
	}

	public void setR63_CURRENCY_VOSTRO(String r63_CURRENCY_VOSTRO) {
		R63_CURRENCY_VOSTRO = r63_CURRENCY_VOSTRO;
	}

	public BigDecimal getR63_AMOUNT_DEMAND_VOSTRO() {
		return R63_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR63_AMOUNT_DEMAND_VOSTRO(BigDecimal r63_AMOUNT_DEMAND_VOSTRO) {
		R63_AMOUNT_DEMAND_VOSTRO = r63_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR63_AMOUNT_TIME_VOSTRO() {
		return R63_AMOUNT_TIME_VOSTRO;
	}

	public void setR63_AMOUNT_TIME_VOSTRO(BigDecimal r63_AMOUNT_TIME_VOSTRO) {
		R63_AMOUNT_TIME_VOSTRO = r63_AMOUNT_TIME_VOSTRO;
	}

	public String getR64_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R64_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR64_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r64_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R64_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r64_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR64_TYPE_OF_ACCOUNT_VOSTRO() {
		return R64_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR64_TYPE_OF_ACCOUNT_VOSTRO(String r64_TYPE_OF_ACCOUNT_VOSTRO) {
		R64_TYPE_OF_ACCOUNT_VOSTRO = r64_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR64_PURPOSE_VOSTRO() {
		return R64_PURPOSE_VOSTRO;
	}

	public void setR64_PURPOSE_VOSTRO(String r64_PURPOSE_VOSTRO) {
		R64_PURPOSE_VOSTRO = r64_PURPOSE_VOSTRO;
	}

	public String getR64_CURRENCY_VOSTRO() {
		return R64_CURRENCY_VOSTRO;
	}

	public void setR64_CURRENCY_VOSTRO(String r64_CURRENCY_VOSTRO) {
		R64_CURRENCY_VOSTRO = r64_CURRENCY_VOSTRO;
	}

	public BigDecimal getR64_AMOUNT_DEMAND_VOSTRO() {
		return R64_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR64_AMOUNT_DEMAND_VOSTRO(BigDecimal r64_AMOUNT_DEMAND_VOSTRO) {
		R64_AMOUNT_DEMAND_VOSTRO = r64_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR64_AMOUNT_TIME_VOSTRO() {
		return R64_AMOUNT_TIME_VOSTRO;
	}

	public void setR64_AMOUNT_TIME_VOSTRO(BigDecimal r64_AMOUNT_TIME_VOSTRO) {
		R64_AMOUNT_TIME_VOSTRO = r64_AMOUNT_TIME_VOSTRO;
	}

	public String getR65_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R65_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR65_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r65_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R65_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r65_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR65_TYPE_OF_ACCOUNT_VOSTRO() {
		return R65_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR65_TYPE_OF_ACCOUNT_VOSTRO(String r65_TYPE_OF_ACCOUNT_VOSTRO) {
		R65_TYPE_OF_ACCOUNT_VOSTRO = r65_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR65_PURPOSE_VOSTRO() {
		return R65_PURPOSE_VOSTRO;
	}

	public void setR65_PURPOSE_VOSTRO(String r65_PURPOSE_VOSTRO) {
		R65_PURPOSE_VOSTRO = r65_PURPOSE_VOSTRO;
	}

	public String getR65_CURRENCY_VOSTRO() {
		return R65_CURRENCY_VOSTRO;
	}

	public void setR65_CURRENCY_VOSTRO(String r65_CURRENCY_VOSTRO) {
		R65_CURRENCY_VOSTRO = r65_CURRENCY_VOSTRO;
	}

	public BigDecimal getR65_AMOUNT_DEMAND_VOSTRO() {
		return R65_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR65_AMOUNT_DEMAND_VOSTRO(BigDecimal r65_AMOUNT_DEMAND_VOSTRO) {
		R65_AMOUNT_DEMAND_VOSTRO = r65_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR65_AMOUNT_TIME_VOSTRO() {
		return R65_AMOUNT_TIME_VOSTRO;
	}

	public void setR65_AMOUNT_TIME_VOSTRO(BigDecimal r65_AMOUNT_TIME_VOSTRO) {
		R65_AMOUNT_TIME_VOSTRO = r65_AMOUNT_TIME_VOSTRO;
	}

	public String getR66_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R66_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR66_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r66_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R66_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r66_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR66_TYPE_OF_ACCOUNT_VOSTRO() {
		return R66_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR66_TYPE_OF_ACCOUNT_VOSTRO(String r66_TYPE_OF_ACCOUNT_VOSTRO) {
		R66_TYPE_OF_ACCOUNT_VOSTRO = r66_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR66_PURPOSE_VOSTRO() {
		return R66_PURPOSE_VOSTRO;
	}

	public void setR66_PURPOSE_VOSTRO(String r66_PURPOSE_VOSTRO) {
		R66_PURPOSE_VOSTRO = r66_PURPOSE_VOSTRO;
	}

	public String getR66_CURRENCY_VOSTRO() {
		return R66_CURRENCY_VOSTRO;
	}

	public void setR66_CURRENCY_VOSTRO(String r66_CURRENCY_VOSTRO) {
		R66_CURRENCY_VOSTRO = r66_CURRENCY_VOSTRO;
	}

	public BigDecimal getR66_AMOUNT_DEMAND_VOSTRO() {
		return R66_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR66_AMOUNT_DEMAND_VOSTRO(BigDecimal r66_AMOUNT_DEMAND_VOSTRO) {
		R66_AMOUNT_DEMAND_VOSTRO = r66_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR66_AMOUNT_TIME_VOSTRO() {
		return R66_AMOUNT_TIME_VOSTRO;
	}

	public void setR66_AMOUNT_TIME_VOSTRO(BigDecimal r66_AMOUNT_TIME_VOSTRO) {
		R66_AMOUNT_TIME_VOSTRO = r66_AMOUNT_TIME_VOSTRO;
	}

	public String getR67_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R67_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR67_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r67_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R67_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r67_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR67_TYPE_OF_ACCOUNT_VOSTRO() {
		return R67_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR67_TYPE_OF_ACCOUNT_VOSTRO(String r67_TYPE_OF_ACCOUNT_VOSTRO) {
		R67_TYPE_OF_ACCOUNT_VOSTRO = r67_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR67_PURPOSE_VOSTRO() {
		return R67_PURPOSE_VOSTRO;
	}

	public void setR67_PURPOSE_VOSTRO(String r67_PURPOSE_VOSTRO) {
		R67_PURPOSE_VOSTRO = r67_PURPOSE_VOSTRO;
	}

	public String getR67_CURRENCY_VOSTRO() {
		return R67_CURRENCY_VOSTRO;
	}

	public void setR67_CURRENCY_VOSTRO(String r67_CURRENCY_VOSTRO) {
		R67_CURRENCY_VOSTRO = r67_CURRENCY_VOSTRO;
	}

	public BigDecimal getR67_AMOUNT_DEMAND_VOSTRO() {
		return R67_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR67_AMOUNT_DEMAND_VOSTRO(BigDecimal r67_AMOUNT_DEMAND_VOSTRO) {
		R67_AMOUNT_DEMAND_VOSTRO = r67_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR67_AMOUNT_TIME_VOSTRO() {
		return R67_AMOUNT_TIME_VOSTRO;
	}

	public void setR67_AMOUNT_TIME_VOSTRO(BigDecimal r67_AMOUNT_TIME_VOSTRO) {
		R67_AMOUNT_TIME_VOSTRO = r67_AMOUNT_TIME_VOSTRO;
	}

	public String getR68_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R68_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR68_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r68_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R68_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r68_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR68_TYPE_OF_ACCOUNT_VOSTRO() {
		return R68_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR68_TYPE_OF_ACCOUNT_VOSTRO(String r68_TYPE_OF_ACCOUNT_VOSTRO) {
		R68_TYPE_OF_ACCOUNT_VOSTRO = r68_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR68_PURPOSE_VOSTRO() {
		return R68_PURPOSE_VOSTRO;
	}

	public void setR68_PURPOSE_VOSTRO(String r68_PURPOSE_VOSTRO) {
		R68_PURPOSE_VOSTRO = r68_PURPOSE_VOSTRO;
	}

	public String getR68_CURRENCY_VOSTRO() {
		return R68_CURRENCY_VOSTRO;
	}

	public void setR68_CURRENCY_VOSTRO(String r68_CURRENCY_VOSTRO) {
		R68_CURRENCY_VOSTRO = r68_CURRENCY_VOSTRO;
	}

	public BigDecimal getR68_AMOUNT_DEMAND_VOSTRO() {
		return R68_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR68_AMOUNT_DEMAND_VOSTRO(BigDecimal r68_AMOUNT_DEMAND_VOSTRO) {
		R68_AMOUNT_DEMAND_VOSTRO = r68_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR68_AMOUNT_TIME_VOSTRO() {
		return R68_AMOUNT_TIME_VOSTRO;
	}

	public void setR68_AMOUNT_TIME_VOSTRO(BigDecimal r68_AMOUNT_TIME_VOSTRO) {
		R68_AMOUNT_TIME_VOSTRO = r68_AMOUNT_TIME_VOSTRO;
	}

	public String getR69_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R69_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR69_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r69_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R69_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r69_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR69_TYPE_OF_ACCOUNT_VOSTRO() {
		return R69_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR69_TYPE_OF_ACCOUNT_VOSTRO(String r69_TYPE_OF_ACCOUNT_VOSTRO) {
		R69_TYPE_OF_ACCOUNT_VOSTRO = r69_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR69_PURPOSE_VOSTRO() {
		return R69_PURPOSE_VOSTRO;
	}

	public void setR69_PURPOSE_VOSTRO(String r69_PURPOSE_VOSTRO) {
		R69_PURPOSE_VOSTRO = r69_PURPOSE_VOSTRO;
	}

	public String getR69_CURRENCY_VOSTRO() {
		return R69_CURRENCY_VOSTRO;
	}

	public void setR69_CURRENCY_VOSTRO(String r69_CURRENCY_VOSTRO) {
		R69_CURRENCY_VOSTRO = r69_CURRENCY_VOSTRO;
	}

	public BigDecimal getR69_AMOUNT_DEMAND_VOSTRO() {
		return R69_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR69_AMOUNT_DEMAND_VOSTRO(BigDecimal r69_AMOUNT_DEMAND_VOSTRO) {
		R69_AMOUNT_DEMAND_VOSTRO = r69_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR69_AMOUNT_TIME_VOSTRO() {
		return R69_AMOUNT_TIME_VOSTRO;
	}

	public void setR69_AMOUNT_TIME_VOSTRO(BigDecimal r69_AMOUNT_TIME_VOSTRO) {
		R69_AMOUNT_TIME_VOSTRO = r69_AMOUNT_TIME_VOSTRO;
	}

	public String getR70_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R70_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR70_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r70_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R70_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r70_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR70_TYPE_OF_ACCOUNT_VOSTRO() {
		return R70_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR70_TYPE_OF_ACCOUNT_VOSTRO(String r70_TYPE_OF_ACCOUNT_VOSTRO) {
		R70_TYPE_OF_ACCOUNT_VOSTRO = r70_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR70_PURPOSE_VOSTRO() {
		return R70_PURPOSE_VOSTRO;
	}

	public void setR70_PURPOSE_VOSTRO(String r70_PURPOSE_VOSTRO) {
		R70_PURPOSE_VOSTRO = r70_PURPOSE_VOSTRO;
	}

	public String getR70_CURRENCY_VOSTRO() {
		return R70_CURRENCY_VOSTRO;
	}

	public void setR70_CURRENCY_VOSTRO(String r70_CURRENCY_VOSTRO) {
		R70_CURRENCY_VOSTRO = r70_CURRENCY_VOSTRO;
	}

	public BigDecimal getR70_AMOUNT_DEMAND_VOSTRO() {
		return R70_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR70_AMOUNT_DEMAND_VOSTRO(BigDecimal r70_AMOUNT_DEMAND_VOSTRO) {
		R70_AMOUNT_DEMAND_VOSTRO = r70_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR70_AMOUNT_TIME_VOSTRO() {
		return R70_AMOUNT_TIME_VOSTRO;
	}

	public void setR70_AMOUNT_TIME_VOSTRO(BigDecimal r70_AMOUNT_TIME_VOSTRO) {
		R70_AMOUNT_TIME_VOSTRO = r70_AMOUNT_TIME_VOSTRO;
	}

	public String getR71_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R71_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR71_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r71_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R71_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r71_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR71_TYPE_OF_ACCOUNT_VOSTRO() {
		return R71_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR71_TYPE_OF_ACCOUNT_VOSTRO(String r71_TYPE_OF_ACCOUNT_VOSTRO) {
		R71_TYPE_OF_ACCOUNT_VOSTRO = r71_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR71_PURPOSE_VOSTRO() {
		return R71_PURPOSE_VOSTRO;
	}

	public void setR71_PURPOSE_VOSTRO(String r71_PURPOSE_VOSTRO) {
		R71_PURPOSE_VOSTRO = r71_PURPOSE_VOSTRO;
	}

	public String getR71_CURRENCY_VOSTRO() {
		return R71_CURRENCY_VOSTRO;
	}

	public void setR71_CURRENCY_VOSTRO(String r71_CURRENCY_VOSTRO) {
		R71_CURRENCY_VOSTRO = r71_CURRENCY_VOSTRO;
	}

	public BigDecimal getR71_AMOUNT_DEMAND_VOSTRO() {
		return R71_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR71_AMOUNT_DEMAND_VOSTRO(BigDecimal r71_AMOUNT_DEMAND_VOSTRO) {
		R71_AMOUNT_DEMAND_VOSTRO = r71_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR71_AMOUNT_TIME_VOSTRO() {
		return R71_AMOUNT_TIME_VOSTRO;
	}

	public void setR71_AMOUNT_TIME_VOSTRO(BigDecimal r71_AMOUNT_TIME_VOSTRO) {
		R71_AMOUNT_TIME_VOSTRO = r71_AMOUNT_TIME_VOSTRO;
	}

	public String getR72_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R72_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR72_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r72_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R72_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r72_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR72_TYPE_OF_ACCOUNT_VOSTRO() {
		return R72_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR72_TYPE_OF_ACCOUNT_VOSTRO(String r72_TYPE_OF_ACCOUNT_VOSTRO) {
		R72_TYPE_OF_ACCOUNT_VOSTRO = r72_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR72_PURPOSE_VOSTRO() {
		return R72_PURPOSE_VOSTRO;
	}

	public void setR72_PURPOSE_VOSTRO(String r72_PURPOSE_VOSTRO) {
		R72_PURPOSE_VOSTRO = r72_PURPOSE_VOSTRO;
	}

	public String getR72_CURRENCY_VOSTRO() {
		return R72_CURRENCY_VOSTRO;
	}

	public void setR72_CURRENCY_VOSTRO(String r72_CURRENCY_VOSTRO) {
		R72_CURRENCY_VOSTRO = r72_CURRENCY_VOSTRO;
	}

	public BigDecimal getR72_AMOUNT_DEMAND_VOSTRO() {
		return R72_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR72_AMOUNT_DEMAND_VOSTRO(BigDecimal r72_AMOUNT_DEMAND_VOSTRO) {
		R72_AMOUNT_DEMAND_VOSTRO = r72_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR72_AMOUNT_TIME_VOSTRO() {
		return R72_AMOUNT_TIME_VOSTRO;
	}

	public void setR72_AMOUNT_TIME_VOSTRO(BigDecimal r72_AMOUNT_TIME_VOSTRO) {
		R72_AMOUNT_TIME_VOSTRO = r72_AMOUNT_TIME_VOSTRO;
	}

	public String getR73_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R73_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR73_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r73_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R73_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r73_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR73_TYPE_OF_ACCOUNT_VOSTRO() {
		return R73_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR73_TYPE_OF_ACCOUNT_VOSTRO(String r73_TYPE_OF_ACCOUNT_VOSTRO) {
		R73_TYPE_OF_ACCOUNT_VOSTRO = r73_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR73_PURPOSE_VOSTRO() {
		return R73_PURPOSE_VOSTRO;
	}

	public void setR73_PURPOSE_VOSTRO(String r73_PURPOSE_VOSTRO) {
		R73_PURPOSE_VOSTRO = r73_PURPOSE_VOSTRO;
	}

	public String getR73_CURRENCY_VOSTRO() {
		return R73_CURRENCY_VOSTRO;
	}

	public void setR73_CURRENCY_VOSTRO(String r73_CURRENCY_VOSTRO) {
		R73_CURRENCY_VOSTRO = r73_CURRENCY_VOSTRO;
	}

	public BigDecimal getR73_AMOUNT_DEMAND_VOSTRO() {
		return R73_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR73_AMOUNT_DEMAND_VOSTRO(BigDecimal r73_AMOUNT_DEMAND_VOSTRO) {
		R73_AMOUNT_DEMAND_VOSTRO = r73_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR73_AMOUNT_TIME_VOSTRO() {
		return R73_AMOUNT_TIME_VOSTRO;
	}

	public void setR73_AMOUNT_TIME_VOSTRO(BigDecimal r73_AMOUNT_TIME_VOSTRO) {
		R73_AMOUNT_TIME_VOSTRO = r73_AMOUNT_TIME_VOSTRO;
	}

	public String getR74_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R74_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR74_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r74_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R74_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r74_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR74_TYPE_OF_ACCOUNT_VOSTRO() {
		return R74_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR74_TYPE_OF_ACCOUNT_VOSTRO(String r74_TYPE_OF_ACCOUNT_VOSTRO) {
		R74_TYPE_OF_ACCOUNT_VOSTRO = r74_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR74_PURPOSE_VOSTRO() {
		return R74_PURPOSE_VOSTRO;
	}

	public void setR74_PURPOSE_VOSTRO(String r74_PURPOSE_VOSTRO) {
		R74_PURPOSE_VOSTRO = r74_PURPOSE_VOSTRO;
	}

	public String getR74_CURRENCY_VOSTRO() {
		return R74_CURRENCY_VOSTRO;
	}

	public void setR74_CURRENCY_VOSTRO(String r74_CURRENCY_VOSTRO) {
		R74_CURRENCY_VOSTRO = r74_CURRENCY_VOSTRO;
	}

	public BigDecimal getR74_AMOUNT_DEMAND_VOSTRO() {
		return R74_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR74_AMOUNT_DEMAND_VOSTRO(BigDecimal r74_AMOUNT_DEMAND_VOSTRO) {
		R74_AMOUNT_DEMAND_VOSTRO = r74_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR74_AMOUNT_TIME_VOSTRO() {
		return R74_AMOUNT_TIME_VOSTRO;
	}

	public void setR74_AMOUNT_TIME_VOSTRO(BigDecimal r74_AMOUNT_TIME_VOSTRO) {
		R74_AMOUNT_TIME_VOSTRO = r74_AMOUNT_TIME_VOSTRO;
	}

	public String getR75_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R75_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR75_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r75_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R75_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r75_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR75_TYPE_OF_ACCOUNT_VOSTRO() {
		return R75_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR75_TYPE_OF_ACCOUNT_VOSTRO(String r75_TYPE_OF_ACCOUNT_VOSTRO) {
		R75_TYPE_OF_ACCOUNT_VOSTRO = r75_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR75_PURPOSE_VOSTRO() {
		return R75_PURPOSE_VOSTRO;
	}

	public void setR75_PURPOSE_VOSTRO(String r75_PURPOSE_VOSTRO) {
		R75_PURPOSE_VOSTRO = r75_PURPOSE_VOSTRO;
	}

	public String getR75_CURRENCY_VOSTRO() {
		return R75_CURRENCY_VOSTRO;
	}

	public void setR75_CURRENCY_VOSTRO(String r75_CURRENCY_VOSTRO) {
		R75_CURRENCY_VOSTRO = r75_CURRENCY_VOSTRO;
	}

	public BigDecimal getR75_AMOUNT_DEMAND_VOSTRO() {
		return R75_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR75_AMOUNT_DEMAND_VOSTRO(BigDecimal r75_AMOUNT_DEMAND_VOSTRO) {
		R75_AMOUNT_DEMAND_VOSTRO = r75_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR75_AMOUNT_TIME_VOSTRO() {
		return R75_AMOUNT_TIME_VOSTRO;
	}

	public void setR75_AMOUNT_TIME_VOSTRO(BigDecimal r75_AMOUNT_TIME_VOSTRO) {
		R75_AMOUNT_TIME_VOSTRO = r75_AMOUNT_TIME_VOSTRO;
	}

	public String getR76_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R76_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR76_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r76_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R76_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r76_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR76_TYPE_OF_ACCOUNT_VOSTRO() {
		return R76_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR76_TYPE_OF_ACCOUNT_VOSTRO(String r76_TYPE_OF_ACCOUNT_VOSTRO) {
		R76_TYPE_OF_ACCOUNT_VOSTRO = r76_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR76_PURPOSE_VOSTRO() {
		return R76_PURPOSE_VOSTRO;
	}

	public void setR76_PURPOSE_VOSTRO(String r76_PURPOSE_VOSTRO) {
		R76_PURPOSE_VOSTRO = r76_PURPOSE_VOSTRO;
	}

	public String getR76_CURRENCY_VOSTRO() {
		return R76_CURRENCY_VOSTRO;
	}

	public void setR76_CURRENCY_VOSTRO(String r76_CURRENCY_VOSTRO) {
		R76_CURRENCY_VOSTRO = r76_CURRENCY_VOSTRO;
	}

	public BigDecimal getR76_AMOUNT_DEMAND_VOSTRO() {
		return R76_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR76_AMOUNT_DEMAND_VOSTRO(BigDecimal r76_AMOUNT_DEMAND_VOSTRO) {
		R76_AMOUNT_DEMAND_VOSTRO = r76_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR76_AMOUNT_TIME_VOSTRO() {
		return R76_AMOUNT_TIME_VOSTRO;
	}

	public void setR76_AMOUNT_TIME_VOSTRO(BigDecimal r76_AMOUNT_TIME_VOSTRO) {
		R76_AMOUNT_TIME_VOSTRO = r76_AMOUNT_TIME_VOSTRO;
	}

	public String getR77_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R77_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR77_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r77_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R77_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r77_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR77_TYPE_OF_ACCOUNT_VOSTRO() {
		return R77_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR77_TYPE_OF_ACCOUNT_VOSTRO(String r77_TYPE_OF_ACCOUNT_VOSTRO) {
		R77_TYPE_OF_ACCOUNT_VOSTRO = r77_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR77_PURPOSE_VOSTRO() {
		return R77_PURPOSE_VOSTRO;
	}

	public void setR77_PURPOSE_VOSTRO(String r77_PURPOSE_VOSTRO) {
		R77_PURPOSE_VOSTRO = r77_PURPOSE_VOSTRO;
	}

	public String getR77_CURRENCY_VOSTRO() {
		return R77_CURRENCY_VOSTRO;
	}

	public void setR77_CURRENCY_VOSTRO(String r77_CURRENCY_VOSTRO) {
		R77_CURRENCY_VOSTRO = r77_CURRENCY_VOSTRO;
	}

	public BigDecimal getR77_AMOUNT_DEMAND_VOSTRO() {
		return R77_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR77_AMOUNT_DEMAND_VOSTRO(BigDecimal r77_AMOUNT_DEMAND_VOSTRO) {
		R77_AMOUNT_DEMAND_VOSTRO = r77_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR77_AMOUNT_TIME_VOSTRO() {
		return R77_AMOUNT_TIME_VOSTRO;
	}

	public void setR77_AMOUNT_TIME_VOSTRO(BigDecimal r77_AMOUNT_TIME_VOSTRO) {
		R77_AMOUNT_TIME_VOSTRO = r77_AMOUNT_TIME_VOSTRO;
	}

	public String getR78_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R78_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR78_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r78_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R78_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r78_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR78_TYPE_OF_ACCOUNT_VOSTRO() {
		return R78_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR78_TYPE_OF_ACCOUNT_VOSTRO(String r78_TYPE_OF_ACCOUNT_VOSTRO) {
		R78_TYPE_OF_ACCOUNT_VOSTRO = r78_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR78_PURPOSE_VOSTRO() {
		return R78_PURPOSE_VOSTRO;
	}

	public void setR78_PURPOSE_VOSTRO(String r78_PURPOSE_VOSTRO) {
		R78_PURPOSE_VOSTRO = r78_PURPOSE_VOSTRO;
	}

	public String getR78_CURRENCY_VOSTRO() {
		return R78_CURRENCY_VOSTRO;
	}

	public void setR78_CURRENCY_VOSTRO(String r78_CURRENCY_VOSTRO) {
		R78_CURRENCY_VOSTRO = r78_CURRENCY_VOSTRO;
	}

	public BigDecimal getR78_AMOUNT_DEMAND_VOSTRO() {
		return R78_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR78_AMOUNT_DEMAND_VOSTRO(BigDecimal r78_AMOUNT_DEMAND_VOSTRO) {
		R78_AMOUNT_DEMAND_VOSTRO = r78_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR78_AMOUNT_TIME_VOSTRO() {
		return R78_AMOUNT_TIME_VOSTRO;
	}

	public void setR78_AMOUNT_TIME_VOSTRO(BigDecimal r78_AMOUNT_TIME_VOSTRO) {
		R78_AMOUNT_TIME_VOSTRO = r78_AMOUNT_TIME_VOSTRO;
	}

	public String getR79_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R79_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR79_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r79_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R79_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r79_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR79_TYPE_OF_ACCOUNT_VOSTRO() {
		return R79_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR79_TYPE_OF_ACCOUNT_VOSTRO(String r79_TYPE_OF_ACCOUNT_VOSTRO) {
		R79_TYPE_OF_ACCOUNT_VOSTRO = r79_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR79_PURPOSE_VOSTRO() {
		return R79_PURPOSE_VOSTRO;
	}

	public void setR79_PURPOSE_VOSTRO(String r79_PURPOSE_VOSTRO) {
		R79_PURPOSE_VOSTRO = r79_PURPOSE_VOSTRO;
	}

	public String getR79_CURRENCY_VOSTRO() {
		return R79_CURRENCY_VOSTRO;
	}

	public void setR79_CURRENCY_VOSTRO(String r79_CURRENCY_VOSTRO) {
		R79_CURRENCY_VOSTRO = r79_CURRENCY_VOSTRO;
	}

	public BigDecimal getR79_AMOUNT_DEMAND_VOSTRO() {
		return R79_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR79_AMOUNT_DEMAND_VOSTRO(BigDecimal r79_AMOUNT_DEMAND_VOSTRO) {
		R79_AMOUNT_DEMAND_VOSTRO = r79_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR79_AMOUNT_TIME_VOSTRO() {
		return R79_AMOUNT_TIME_VOSTRO;
	}

	public void setR79_AMOUNT_TIME_VOSTRO(BigDecimal r79_AMOUNT_TIME_VOSTRO) {
		R79_AMOUNT_TIME_VOSTRO = r79_AMOUNT_TIME_VOSTRO;
	}

	public String getR80_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R80_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR80_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r80_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R80_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r80_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR80_TYPE_OF_ACCOUNT_VOSTRO() {
		return R80_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR80_TYPE_OF_ACCOUNT_VOSTRO(String r80_TYPE_OF_ACCOUNT_VOSTRO) {
		R80_TYPE_OF_ACCOUNT_VOSTRO = r80_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR80_PURPOSE_VOSTRO() {
		return R80_PURPOSE_VOSTRO;
	}

	public void setR80_PURPOSE_VOSTRO(String r80_PURPOSE_VOSTRO) {
		R80_PURPOSE_VOSTRO = r80_PURPOSE_VOSTRO;
	}

	public String getR80_CURRENCY_VOSTRO() {
		return R80_CURRENCY_VOSTRO;
	}

	public void setR80_CURRENCY_VOSTRO(String r80_CURRENCY_VOSTRO) {
		R80_CURRENCY_VOSTRO = r80_CURRENCY_VOSTRO;
	}

	public BigDecimal getR80_AMOUNT_DEMAND_VOSTRO() {
		return R80_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR80_AMOUNT_DEMAND_VOSTRO(BigDecimal r80_AMOUNT_DEMAND_VOSTRO) {
		R80_AMOUNT_DEMAND_VOSTRO = r80_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR80_AMOUNT_TIME_VOSTRO() {
		return R80_AMOUNT_TIME_VOSTRO;
	}

	public void setR80_AMOUNT_TIME_VOSTRO(BigDecimal r80_AMOUNT_TIME_VOSTRO) {
		R80_AMOUNT_TIME_VOSTRO = r80_AMOUNT_TIME_VOSTRO;
	}

	public String getR81_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R81_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR81_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r81_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R81_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r81_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR81_TYPE_OF_ACCOUNT_VOSTRO() {
		return R81_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR81_TYPE_OF_ACCOUNT_VOSTRO(String r81_TYPE_OF_ACCOUNT_VOSTRO) {
		R81_TYPE_OF_ACCOUNT_VOSTRO = r81_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR81_PURPOSE_VOSTRO() {
		return R81_PURPOSE_VOSTRO;
	}

	public void setR81_PURPOSE_VOSTRO(String r81_PURPOSE_VOSTRO) {
		R81_PURPOSE_VOSTRO = r81_PURPOSE_VOSTRO;
	}

	public String getR81_CURRENCY_VOSTRO() {
		return R81_CURRENCY_VOSTRO;
	}

	public void setR81_CURRENCY_VOSTRO(String r81_CURRENCY_VOSTRO) {
		R81_CURRENCY_VOSTRO = r81_CURRENCY_VOSTRO;
	}

	public BigDecimal getR81_AMOUNT_DEMAND_VOSTRO() {
		return R81_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR81_AMOUNT_DEMAND_VOSTRO(BigDecimal r81_AMOUNT_DEMAND_VOSTRO) {
		R81_AMOUNT_DEMAND_VOSTRO = r81_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR81_AMOUNT_TIME_VOSTRO() {
		return R81_AMOUNT_TIME_VOSTRO;
	}

	public void setR81_AMOUNT_TIME_VOSTRO(BigDecimal r81_AMOUNT_TIME_VOSTRO) {
		R81_AMOUNT_TIME_VOSTRO = r81_AMOUNT_TIME_VOSTRO;
	}

	public String getR82_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R82_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR82_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r82_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R82_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r82_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR82_TYPE_OF_ACCOUNT_VOSTRO() {
		return R82_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR82_TYPE_OF_ACCOUNT_VOSTRO(String r82_TYPE_OF_ACCOUNT_VOSTRO) {
		R82_TYPE_OF_ACCOUNT_VOSTRO = r82_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR82_PURPOSE_VOSTRO() {
		return R82_PURPOSE_VOSTRO;
	}

	public void setR82_PURPOSE_VOSTRO(String r82_PURPOSE_VOSTRO) {
		R82_PURPOSE_VOSTRO = r82_PURPOSE_VOSTRO;
	}

	public String getR82_CURRENCY_VOSTRO() {
		return R82_CURRENCY_VOSTRO;
	}

	public void setR82_CURRENCY_VOSTRO(String r82_CURRENCY_VOSTRO) {
		R82_CURRENCY_VOSTRO = r82_CURRENCY_VOSTRO;
	}

	public BigDecimal getR82_AMOUNT_DEMAND_VOSTRO() {
		return R82_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR82_AMOUNT_DEMAND_VOSTRO(BigDecimal r82_AMOUNT_DEMAND_VOSTRO) {
		R82_AMOUNT_DEMAND_VOSTRO = r82_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR82_AMOUNT_TIME_VOSTRO() {
		return R82_AMOUNT_TIME_VOSTRO;
	}

	public void setR82_AMOUNT_TIME_VOSTRO(BigDecimal r82_AMOUNT_TIME_VOSTRO) {
		R82_AMOUNT_TIME_VOSTRO = r82_AMOUNT_TIME_VOSTRO;
	}

	public String getR83_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R83_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR83_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r83_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R83_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r83_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR83_TYPE_OF_ACCOUNT_VOSTRO() {
		return R83_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR83_TYPE_OF_ACCOUNT_VOSTRO(String r83_TYPE_OF_ACCOUNT_VOSTRO) {
		R83_TYPE_OF_ACCOUNT_VOSTRO = r83_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR83_PURPOSE_VOSTRO() {
		return R83_PURPOSE_VOSTRO;
	}

	public void setR83_PURPOSE_VOSTRO(String r83_PURPOSE_VOSTRO) {
		R83_PURPOSE_VOSTRO = r83_PURPOSE_VOSTRO;
	}

	public String getR83_CURRENCY_VOSTRO() {
		return R83_CURRENCY_VOSTRO;
	}

	public void setR83_CURRENCY_VOSTRO(String r83_CURRENCY_VOSTRO) {
		R83_CURRENCY_VOSTRO = r83_CURRENCY_VOSTRO;
	}

	public BigDecimal getR83_AMOUNT_DEMAND_VOSTRO() {
		return R83_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR83_AMOUNT_DEMAND_VOSTRO(BigDecimal r83_AMOUNT_DEMAND_VOSTRO) {
		R83_AMOUNT_DEMAND_VOSTRO = r83_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR83_AMOUNT_TIME_VOSTRO() {
		return R83_AMOUNT_TIME_VOSTRO;
	}

	public void setR83_AMOUNT_TIME_VOSTRO(BigDecimal r83_AMOUNT_TIME_VOSTRO) {
		R83_AMOUNT_TIME_VOSTRO = r83_AMOUNT_TIME_VOSTRO;
	}

	public String getR84_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R84_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR84_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r84_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R84_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r84_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR84_TYPE_OF_ACCOUNT_VOSTRO() {
		return R84_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR84_TYPE_OF_ACCOUNT_VOSTRO(String r84_TYPE_OF_ACCOUNT_VOSTRO) {
		R84_TYPE_OF_ACCOUNT_VOSTRO = r84_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR84_PURPOSE_VOSTRO() {
		return R84_PURPOSE_VOSTRO;
	}

	public void setR84_PURPOSE_VOSTRO(String r84_PURPOSE_VOSTRO) {
		R84_PURPOSE_VOSTRO = r84_PURPOSE_VOSTRO;
	}

	public String getR84_CURRENCY_VOSTRO() {
		return R84_CURRENCY_VOSTRO;
	}

	public void setR84_CURRENCY_VOSTRO(String r84_CURRENCY_VOSTRO) {
		R84_CURRENCY_VOSTRO = r84_CURRENCY_VOSTRO;
	}

	public BigDecimal getR84_AMOUNT_DEMAND_VOSTRO() {
		return R84_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR84_AMOUNT_DEMAND_VOSTRO(BigDecimal r84_AMOUNT_DEMAND_VOSTRO) {
		R84_AMOUNT_DEMAND_VOSTRO = r84_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR84_AMOUNT_TIME_VOSTRO() {
		return R84_AMOUNT_TIME_VOSTRO;
	}

	public void setR84_AMOUNT_TIME_VOSTRO(BigDecimal r84_AMOUNT_TIME_VOSTRO) {
		R84_AMOUNT_TIME_VOSTRO = r84_AMOUNT_TIME_VOSTRO;
	}

	public String getR85_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R85_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR85_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r85_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R85_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r85_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR85_TYPE_OF_ACCOUNT_VOSTRO() {
		return R85_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR85_TYPE_OF_ACCOUNT_VOSTRO(String r85_TYPE_OF_ACCOUNT_VOSTRO) {
		R85_TYPE_OF_ACCOUNT_VOSTRO = r85_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR85_PURPOSE_VOSTRO() {
		return R85_PURPOSE_VOSTRO;
	}

	public void setR85_PURPOSE_VOSTRO(String r85_PURPOSE_VOSTRO) {
		R85_PURPOSE_VOSTRO = r85_PURPOSE_VOSTRO;
	}

	public String getR85_CURRENCY_VOSTRO() {
		return R85_CURRENCY_VOSTRO;
	}

	public void setR85_CURRENCY_VOSTRO(String r85_CURRENCY_VOSTRO) {
		R85_CURRENCY_VOSTRO = r85_CURRENCY_VOSTRO;
	}

	public BigDecimal getR85_AMOUNT_DEMAND_VOSTRO() {
		return R85_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR85_AMOUNT_DEMAND_VOSTRO(BigDecimal r85_AMOUNT_DEMAND_VOSTRO) {
		R85_AMOUNT_DEMAND_VOSTRO = r85_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR85_AMOUNT_TIME_VOSTRO() {
		return R85_AMOUNT_TIME_VOSTRO;
	}

	public void setR85_AMOUNT_TIME_VOSTRO(BigDecimal r85_AMOUNT_TIME_VOSTRO) {
		R85_AMOUNT_TIME_VOSTRO = r85_AMOUNT_TIME_VOSTRO;
	}

	public String getR86_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R86_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR86_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r86_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R86_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r86_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR86_TYPE_OF_ACCOUNT_VOSTRO() {
		return R86_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR86_TYPE_OF_ACCOUNT_VOSTRO(String r86_TYPE_OF_ACCOUNT_VOSTRO) {
		R86_TYPE_OF_ACCOUNT_VOSTRO = r86_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR86_PURPOSE_VOSTRO() {
		return R86_PURPOSE_VOSTRO;
	}

	public void setR86_PURPOSE_VOSTRO(String r86_PURPOSE_VOSTRO) {
		R86_PURPOSE_VOSTRO = r86_PURPOSE_VOSTRO;
	}

	public String getR86_CURRENCY_VOSTRO() {
		return R86_CURRENCY_VOSTRO;
	}

	public void setR86_CURRENCY_VOSTRO(String r86_CURRENCY_VOSTRO) {
		R86_CURRENCY_VOSTRO = r86_CURRENCY_VOSTRO;
	}

	public BigDecimal getR86_AMOUNT_DEMAND_VOSTRO() {
		return R86_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR86_AMOUNT_DEMAND_VOSTRO(BigDecimal r86_AMOUNT_DEMAND_VOSTRO) {
		R86_AMOUNT_DEMAND_VOSTRO = r86_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR86_AMOUNT_TIME_VOSTRO() {
		return R86_AMOUNT_TIME_VOSTRO;
	}

	public void setR86_AMOUNT_TIME_VOSTRO(BigDecimal r86_AMOUNT_TIME_VOSTRO) {
		R86_AMOUNT_TIME_VOSTRO = r86_AMOUNT_TIME_VOSTRO;
	}

	public String getR87_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R87_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR87_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r87_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R87_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r87_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR87_TYPE_OF_ACCOUNT_VOSTRO() {
		return R87_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR87_TYPE_OF_ACCOUNT_VOSTRO(String r87_TYPE_OF_ACCOUNT_VOSTRO) {
		R87_TYPE_OF_ACCOUNT_VOSTRO = r87_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR87_PURPOSE_VOSTRO() {
		return R87_PURPOSE_VOSTRO;
	}

	public void setR87_PURPOSE_VOSTRO(String r87_PURPOSE_VOSTRO) {
		R87_PURPOSE_VOSTRO = r87_PURPOSE_VOSTRO;
	}

	public String getR87_CURRENCY_VOSTRO() {
		return R87_CURRENCY_VOSTRO;
	}

	public void setR87_CURRENCY_VOSTRO(String r87_CURRENCY_VOSTRO) {
		R87_CURRENCY_VOSTRO = r87_CURRENCY_VOSTRO;
	}

	public BigDecimal getR87_AMOUNT_DEMAND_VOSTRO() {
		return R87_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR87_AMOUNT_DEMAND_VOSTRO(BigDecimal r87_AMOUNT_DEMAND_VOSTRO) {
		R87_AMOUNT_DEMAND_VOSTRO = r87_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR87_AMOUNT_TIME_VOSTRO() {
		return R87_AMOUNT_TIME_VOSTRO;
	}

	public void setR87_AMOUNT_TIME_VOSTRO(BigDecimal r87_AMOUNT_TIME_VOSTRO) {
		R87_AMOUNT_TIME_VOSTRO = r87_AMOUNT_TIME_VOSTRO;
	}

	public String getR88_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R88_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR88_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r88_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R88_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r88_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR88_TYPE_OF_ACCOUNT_VOSTRO() {
		return R88_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR88_TYPE_OF_ACCOUNT_VOSTRO(String r88_TYPE_OF_ACCOUNT_VOSTRO) {
		R88_TYPE_OF_ACCOUNT_VOSTRO = r88_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR88_PURPOSE_VOSTRO() {
		return R88_PURPOSE_VOSTRO;
	}

	public void setR88_PURPOSE_VOSTRO(String r88_PURPOSE_VOSTRO) {
		R88_PURPOSE_VOSTRO = r88_PURPOSE_VOSTRO;
	}

	public String getR88_CURRENCY_VOSTRO() {
		return R88_CURRENCY_VOSTRO;
	}

	public void setR88_CURRENCY_VOSTRO(String r88_CURRENCY_VOSTRO) {
		R88_CURRENCY_VOSTRO = r88_CURRENCY_VOSTRO;
	}

	public BigDecimal getR88_AMOUNT_DEMAND_VOSTRO() {
		return R88_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR88_AMOUNT_DEMAND_VOSTRO(BigDecimal r88_AMOUNT_DEMAND_VOSTRO) {
		R88_AMOUNT_DEMAND_VOSTRO = r88_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR88_AMOUNT_TIME_VOSTRO() {
		return R88_AMOUNT_TIME_VOSTRO;
	}

	public void setR88_AMOUNT_TIME_VOSTRO(BigDecimal r88_AMOUNT_TIME_VOSTRO) {
		R88_AMOUNT_TIME_VOSTRO = r88_AMOUNT_TIME_VOSTRO;
	}

	public String getR89_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R89_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR89_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r89_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R89_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r89_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR89_TYPE_OF_ACCOUNT_VOSTRO() {
		return R89_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR89_TYPE_OF_ACCOUNT_VOSTRO(String r89_TYPE_OF_ACCOUNT_VOSTRO) {
		R89_TYPE_OF_ACCOUNT_VOSTRO = r89_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR89_PURPOSE_VOSTRO() {
		return R89_PURPOSE_VOSTRO;
	}

	public void setR89_PURPOSE_VOSTRO(String r89_PURPOSE_VOSTRO) {
		R89_PURPOSE_VOSTRO = r89_PURPOSE_VOSTRO;
	}

	public String getR89_CURRENCY_VOSTRO() {
		return R89_CURRENCY_VOSTRO;
	}

	public void setR89_CURRENCY_VOSTRO(String r89_CURRENCY_VOSTRO) {
		R89_CURRENCY_VOSTRO = r89_CURRENCY_VOSTRO;
	}

	public BigDecimal getR89_AMOUNT_DEMAND_VOSTRO() {
		return R89_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR89_AMOUNT_DEMAND_VOSTRO(BigDecimal r89_AMOUNT_DEMAND_VOSTRO) {
		R89_AMOUNT_DEMAND_VOSTRO = r89_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR89_AMOUNT_TIME_VOSTRO() {
		return R89_AMOUNT_TIME_VOSTRO;
	}

	public void setR89_AMOUNT_TIME_VOSTRO(BigDecimal r89_AMOUNT_TIME_VOSTRO) {
		R89_AMOUNT_TIME_VOSTRO = r89_AMOUNT_TIME_VOSTRO;
	}

	public String getR90_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R90_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR90_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r90_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R90_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r90_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR90_TYPE_OF_ACCOUNT_VOSTRO() {
		return R90_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR90_TYPE_OF_ACCOUNT_VOSTRO(String r90_TYPE_OF_ACCOUNT_VOSTRO) {
		R90_TYPE_OF_ACCOUNT_VOSTRO = r90_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR90_PURPOSE_VOSTRO() {
		return R90_PURPOSE_VOSTRO;
	}

	public void setR90_PURPOSE_VOSTRO(String r90_PURPOSE_VOSTRO) {
		R90_PURPOSE_VOSTRO = r90_PURPOSE_VOSTRO;
	}

	public String getR90_CURRENCY_VOSTRO() {
		return R90_CURRENCY_VOSTRO;
	}

	public void setR90_CURRENCY_VOSTRO(String r90_CURRENCY_VOSTRO) {
		R90_CURRENCY_VOSTRO = r90_CURRENCY_VOSTRO;
	}

	public BigDecimal getR90_AMOUNT_DEMAND_VOSTRO() {
		return R90_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR90_AMOUNT_DEMAND_VOSTRO(BigDecimal r90_AMOUNT_DEMAND_VOSTRO) {
		R90_AMOUNT_DEMAND_VOSTRO = r90_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR90_AMOUNT_TIME_VOSTRO() {
		return R90_AMOUNT_TIME_VOSTRO;
	}

	public void setR90_AMOUNT_TIME_VOSTRO(BigDecimal r90_AMOUNT_TIME_VOSTRO) {
		R90_AMOUNT_TIME_VOSTRO = r90_AMOUNT_TIME_VOSTRO;
	}

	public String getR91_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R91_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR91_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r91_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R91_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r91_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR91_TYPE_OF_ACCOUNT_VOSTRO() {
		return R91_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR91_TYPE_OF_ACCOUNT_VOSTRO(String r91_TYPE_OF_ACCOUNT_VOSTRO) {
		R91_TYPE_OF_ACCOUNT_VOSTRO = r91_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR91_PURPOSE_VOSTRO() {
		return R91_PURPOSE_VOSTRO;
	}

	public void setR91_PURPOSE_VOSTRO(String r91_PURPOSE_VOSTRO) {
		R91_PURPOSE_VOSTRO = r91_PURPOSE_VOSTRO;
	}

	public String getR91_CURRENCY_VOSTRO() {
		return R91_CURRENCY_VOSTRO;
	}

	public void setR91_CURRENCY_VOSTRO(String r91_CURRENCY_VOSTRO) {
		R91_CURRENCY_VOSTRO = r91_CURRENCY_VOSTRO;
	}

	public BigDecimal getR91_AMOUNT_DEMAND_VOSTRO() {
		return R91_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR91_AMOUNT_DEMAND_VOSTRO(BigDecimal r91_AMOUNT_DEMAND_VOSTRO) {
		R91_AMOUNT_DEMAND_VOSTRO = r91_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR91_AMOUNT_TIME_VOSTRO() {
		return R91_AMOUNT_TIME_VOSTRO;
	}

	public void setR91_AMOUNT_TIME_VOSTRO(BigDecimal r91_AMOUNT_TIME_VOSTRO) {
		R91_AMOUNT_TIME_VOSTRO = r91_AMOUNT_TIME_VOSTRO;
	}

	public String getR92_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R92_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR92_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r92_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R92_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r92_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR92_TYPE_OF_ACCOUNT_VOSTRO() {
		return R92_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR92_TYPE_OF_ACCOUNT_VOSTRO(String r92_TYPE_OF_ACCOUNT_VOSTRO) {
		R92_TYPE_OF_ACCOUNT_VOSTRO = r92_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR92_PURPOSE_VOSTRO() {
		return R92_PURPOSE_VOSTRO;
	}

	public void setR92_PURPOSE_VOSTRO(String r92_PURPOSE_VOSTRO) {
		R92_PURPOSE_VOSTRO = r92_PURPOSE_VOSTRO;
	}

	public String getR92_CURRENCY_VOSTRO() {
		return R92_CURRENCY_VOSTRO;
	}

	public void setR92_CURRENCY_VOSTRO(String r92_CURRENCY_VOSTRO) {
		R92_CURRENCY_VOSTRO = r92_CURRENCY_VOSTRO;
	}

	public BigDecimal getR92_AMOUNT_DEMAND_VOSTRO() {
		return R92_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR92_AMOUNT_DEMAND_VOSTRO(BigDecimal r92_AMOUNT_DEMAND_VOSTRO) {
		R92_AMOUNT_DEMAND_VOSTRO = r92_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR92_AMOUNT_TIME_VOSTRO() {
		return R92_AMOUNT_TIME_VOSTRO;
	}

	public void setR92_AMOUNT_TIME_VOSTRO(BigDecimal r92_AMOUNT_TIME_VOSTRO) {
		R92_AMOUNT_TIME_VOSTRO = r92_AMOUNT_TIME_VOSTRO;
	}

	public String getR93_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R93_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR93_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r93_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R93_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r93_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR93_TYPE_OF_ACCOUNT_VOSTRO() {
		return R93_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR93_TYPE_OF_ACCOUNT_VOSTRO(String r93_TYPE_OF_ACCOUNT_VOSTRO) {
		R93_TYPE_OF_ACCOUNT_VOSTRO = r93_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR93_PURPOSE_VOSTRO() {
		return R93_PURPOSE_VOSTRO;
	}

	public void setR93_PURPOSE_VOSTRO(String r93_PURPOSE_VOSTRO) {
		R93_PURPOSE_VOSTRO = r93_PURPOSE_VOSTRO;
	}

	public String getR93_CURRENCY_VOSTRO() {
		return R93_CURRENCY_VOSTRO;
	}

	public void setR93_CURRENCY_VOSTRO(String r93_CURRENCY_VOSTRO) {
		R93_CURRENCY_VOSTRO = r93_CURRENCY_VOSTRO;
	}

	public BigDecimal getR93_AMOUNT_DEMAND_VOSTRO() {
		return R93_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR93_AMOUNT_DEMAND_VOSTRO(BigDecimal r93_AMOUNT_DEMAND_VOSTRO) {
		R93_AMOUNT_DEMAND_VOSTRO = r93_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR93_AMOUNT_TIME_VOSTRO() {
		return R93_AMOUNT_TIME_VOSTRO;
	}

	public void setR93_AMOUNT_TIME_VOSTRO(BigDecimal r93_AMOUNT_TIME_VOSTRO) {
		R93_AMOUNT_TIME_VOSTRO = r93_AMOUNT_TIME_VOSTRO;
	}

	public String getR94_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R94_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR94_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r94_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R94_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r94_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR94_TYPE_OF_ACCOUNT_VOSTRO() {
		return R94_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR94_TYPE_OF_ACCOUNT_VOSTRO(String r94_TYPE_OF_ACCOUNT_VOSTRO) {
		R94_TYPE_OF_ACCOUNT_VOSTRO = r94_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR94_PURPOSE_VOSTRO() {
		return R94_PURPOSE_VOSTRO;
	}

	public void setR94_PURPOSE_VOSTRO(String r94_PURPOSE_VOSTRO) {
		R94_PURPOSE_VOSTRO = r94_PURPOSE_VOSTRO;
	}

	public String getR94_CURRENCY_VOSTRO() {
		return R94_CURRENCY_VOSTRO;
	}

	public void setR94_CURRENCY_VOSTRO(String r94_CURRENCY_VOSTRO) {
		R94_CURRENCY_VOSTRO = r94_CURRENCY_VOSTRO;
	}

	public BigDecimal getR94_AMOUNT_DEMAND_VOSTRO() {
		return R94_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR94_AMOUNT_DEMAND_VOSTRO(BigDecimal r94_AMOUNT_DEMAND_VOSTRO) {
		R94_AMOUNT_DEMAND_VOSTRO = r94_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR94_AMOUNT_TIME_VOSTRO() {
		return R94_AMOUNT_TIME_VOSTRO;
	}

	public void setR94_AMOUNT_TIME_VOSTRO(BigDecimal r94_AMOUNT_TIME_VOSTRO) {
		R94_AMOUNT_TIME_VOSTRO = r94_AMOUNT_TIME_VOSTRO;
	}

	public String getR95_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R95_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR95_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r95_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R95_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r95_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR95_TYPE_OF_ACCOUNT_VOSTRO() {
		return R95_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR95_TYPE_OF_ACCOUNT_VOSTRO(String r95_TYPE_OF_ACCOUNT_VOSTRO) {
		R95_TYPE_OF_ACCOUNT_VOSTRO = r95_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR95_PURPOSE_VOSTRO() {
		return R95_PURPOSE_VOSTRO;
	}

	public void setR95_PURPOSE_VOSTRO(String r95_PURPOSE_VOSTRO) {
		R95_PURPOSE_VOSTRO = r95_PURPOSE_VOSTRO;
	}

	public String getR95_CURRENCY_VOSTRO() {
		return R95_CURRENCY_VOSTRO;
	}

	public void setR95_CURRENCY_VOSTRO(String r95_CURRENCY_VOSTRO) {
		R95_CURRENCY_VOSTRO = r95_CURRENCY_VOSTRO;
	}

	public BigDecimal getR95_AMOUNT_DEMAND_VOSTRO() {
		return R95_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR95_AMOUNT_DEMAND_VOSTRO(BigDecimal r95_AMOUNT_DEMAND_VOSTRO) {
		R95_AMOUNT_DEMAND_VOSTRO = r95_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR95_AMOUNT_TIME_VOSTRO() {
		return R95_AMOUNT_TIME_VOSTRO;
	}

	public void setR95_AMOUNT_TIME_VOSTRO(BigDecimal r95_AMOUNT_TIME_VOSTRO) {
		R95_AMOUNT_TIME_VOSTRO = r95_AMOUNT_TIME_VOSTRO;
	}

	public String getR96_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R96_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR96_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r96_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R96_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r96_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR96_TYPE_OF_ACCOUNT_VOSTRO() {
		return R96_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR96_TYPE_OF_ACCOUNT_VOSTRO(String r96_TYPE_OF_ACCOUNT_VOSTRO) {
		R96_TYPE_OF_ACCOUNT_VOSTRO = r96_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR96_PURPOSE_VOSTRO() {
		return R96_PURPOSE_VOSTRO;
	}

	public void setR96_PURPOSE_VOSTRO(String r96_PURPOSE_VOSTRO) {
		R96_PURPOSE_VOSTRO = r96_PURPOSE_VOSTRO;
	}

	public String getR96_CURRENCY_VOSTRO() {
		return R96_CURRENCY_VOSTRO;
	}

	public void setR96_CURRENCY_VOSTRO(String r96_CURRENCY_VOSTRO) {
		R96_CURRENCY_VOSTRO = r96_CURRENCY_VOSTRO;
	}

	public BigDecimal getR96_AMOUNT_DEMAND_VOSTRO() {
		return R96_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR96_AMOUNT_DEMAND_VOSTRO(BigDecimal r96_AMOUNT_DEMAND_VOSTRO) {
		R96_AMOUNT_DEMAND_VOSTRO = r96_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR96_AMOUNT_TIME_VOSTRO() {
		return R96_AMOUNT_TIME_VOSTRO;
	}

	public void setR96_AMOUNT_TIME_VOSTRO(BigDecimal r96_AMOUNT_TIME_VOSTRO) {
		R96_AMOUNT_TIME_VOSTRO = r96_AMOUNT_TIME_VOSTRO;
	}

	public String getR97_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R97_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR97_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r97_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R97_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r97_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR97_TYPE_OF_ACCOUNT_VOSTRO() {
		return R97_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR97_TYPE_OF_ACCOUNT_VOSTRO(String r97_TYPE_OF_ACCOUNT_VOSTRO) {
		R97_TYPE_OF_ACCOUNT_VOSTRO = r97_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR97_PURPOSE_VOSTRO() {
		return R97_PURPOSE_VOSTRO;
	}

	public void setR97_PURPOSE_VOSTRO(String r97_PURPOSE_VOSTRO) {
		R97_PURPOSE_VOSTRO = r97_PURPOSE_VOSTRO;
	}

	public String getR97_CURRENCY_VOSTRO() {
		return R97_CURRENCY_VOSTRO;
	}

	public void setR97_CURRENCY_VOSTRO(String r97_CURRENCY_VOSTRO) {
		R97_CURRENCY_VOSTRO = r97_CURRENCY_VOSTRO;
	}

	public BigDecimal getR97_AMOUNT_DEMAND_VOSTRO() {
		return R97_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR97_AMOUNT_DEMAND_VOSTRO(BigDecimal r97_AMOUNT_DEMAND_VOSTRO) {
		R97_AMOUNT_DEMAND_VOSTRO = r97_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR97_AMOUNT_TIME_VOSTRO() {
		return R97_AMOUNT_TIME_VOSTRO;
	}

	public void setR97_AMOUNT_TIME_VOSTRO(BigDecimal r97_AMOUNT_TIME_VOSTRO) {
		R97_AMOUNT_TIME_VOSTRO = r97_AMOUNT_TIME_VOSTRO;
	}

	public String getR98_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R98_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR98_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r98_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R98_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r98_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR98_TYPE_OF_ACCOUNT_VOSTRO() {
		return R98_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR98_TYPE_OF_ACCOUNT_VOSTRO(String r98_TYPE_OF_ACCOUNT_VOSTRO) {
		R98_TYPE_OF_ACCOUNT_VOSTRO = r98_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR98_PURPOSE_VOSTRO() {
		return R98_PURPOSE_VOSTRO;
	}

	public void setR98_PURPOSE_VOSTRO(String r98_PURPOSE_VOSTRO) {
		R98_PURPOSE_VOSTRO = r98_PURPOSE_VOSTRO;
	}

	public String getR98_CURRENCY_VOSTRO() {
		return R98_CURRENCY_VOSTRO;
	}

	public void setR98_CURRENCY_VOSTRO(String r98_CURRENCY_VOSTRO) {
		R98_CURRENCY_VOSTRO = r98_CURRENCY_VOSTRO;
	}

	public BigDecimal getR98_AMOUNT_DEMAND_VOSTRO() {
		return R98_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR98_AMOUNT_DEMAND_VOSTRO(BigDecimal r98_AMOUNT_DEMAND_VOSTRO) {
		R98_AMOUNT_DEMAND_VOSTRO = r98_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR98_AMOUNT_TIME_VOSTRO() {
		return R98_AMOUNT_TIME_VOSTRO;
	}

	public void setR98_AMOUNT_TIME_VOSTRO(BigDecimal r98_AMOUNT_TIME_VOSTRO) {
		R98_AMOUNT_TIME_VOSTRO = r98_AMOUNT_TIME_VOSTRO;
	}

	public String getR99_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R99_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR99_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r99_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R99_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r99_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR99_TYPE_OF_ACCOUNT_VOSTRO() {
		return R99_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR99_TYPE_OF_ACCOUNT_VOSTRO(String r99_TYPE_OF_ACCOUNT_VOSTRO) {
		R99_TYPE_OF_ACCOUNT_VOSTRO = r99_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR99_PURPOSE_VOSTRO() {
		return R99_PURPOSE_VOSTRO;
	}

	public void setR99_PURPOSE_VOSTRO(String r99_PURPOSE_VOSTRO) {
		R99_PURPOSE_VOSTRO = r99_PURPOSE_VOSTRO;
	}

	public String getR99_CURRENCY_VOSTRO() {
		return R99_CURRENCY_VOSTRO;
	}

	public void setR99_CURRENCY_VOSTRO(String r99_CURRENCY_VOSTRO) {
		R99_CURRENCY_VOSTRO = r99_CURRENCY_VOSTRO;
	}

	public BigDecimal getR99_AMOUNT_DEMAND_VOSTRO() {
		return R99_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR99_AMOUNT_DEMAND_VOSTRO(BigDecimal r99_AMOUNT_DEMAND_VOSTRO) {
		R99_AMOUNT_DEMAND_VOSTRO = r99_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR99_AMOUNT_TIME_VOSTRO() {
		return R99_AMOUNT_TIME_VOSTRO;
	}

	public void setR99_AMOUNT_TIME_VOSTRO(BigDecimal r99_AMOUNT_TIME_VOSTRO) {
		R99_AMOUNT_TIME_VOSTRO = r99_AMOUNT_TIME_VOSTRO;
	}

	public String getR100_NAME_OF_BANK_AND_COUNTRY_VOSTRO() {
		return R100_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public void setR100_NAME_OF_BANK_AND_COUNTRY_VOSTRO(String r100_NAME_OF_BANK_AND_COUNTRY_VOSTRO) {
		R100_NAME_OF_BANK_AND_COUNTRY_VOSTRO = r100_NAME_OF_BANK_AND_COUNTRY_VOSTRO;
	}

	public String getR100_TYPE_OF_ACCOUNT_VOSTRO() {
		return R100_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public void setR100_TYPE_OF_ACCOUNT_VOSTRO(String r100_TYPE_OF_ACCOUNT_VOSTRO) {
		R100_TYPE_OF_ACCOUNT_VOSTRO = r100_TYPE_OF_ACCOUNT_VOSTRO;
	}

	public String getR100_PURPOSE_VOSTRO() {
		return R100_PURPOSE_VOSTRO;
	}

	public void setR100_PURPOSE_VOSTRO(String r100_PURPOSE_VOSTRO) {
		R100_PURPOSE_VOSTRO = r100_PURPOSE_VOSTRO;
	}

	public String getR100_CURRENCY_VOSTRO() {
		return R100_CURRENCY_VOSTRO;
	}

	public void setR100_CURRENCY_VOSTRO(String r100_CURRENCY_VOSTRO) {
		R100_CURRENCY_VOSTRO = r100_CURRENCY_VOSTRO;
	}

	public BigDecimal getR100_AMOUNT_DEMAND_VOSTRO() {
		return R100_AMOUNT_DEMAND_VOSTRO;
	}

	public void setR100_AMOUNT_DEMAND_VOSTRO(BigDecimal r100_AMOUNT_DEMAND_VOSTRO) {
		R100_AMOUNT_DEMAND_VOSTRO = r100_AMOUNT_DEMAND_VOSTRO;
	}

	public BigDecimal getR100_AMOUNT_TIME_VOSTRO() {
		return R100_AMOUNT_TIME_VOSTRO;
	}

	public void setR100_AMOUNT_TIME_VOSTRO(BigDecimal r100_AMOUNT_TIME_VOSTRO) {
		R100_AMOUNT_TIME_VOSTRO = r100_AMOUNT_TIME_VOSTRO;
	}

}