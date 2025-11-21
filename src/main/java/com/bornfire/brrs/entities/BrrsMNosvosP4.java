package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_NOSVOS_P4")
public class BrrsMNosvosP4 {

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
	
    @Column(name = "R1_NAME_OF_BANK_VOSTRO1")
    private String R1_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R1_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R1_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R1_PURPOSE_VOSTRO1")
    private String R1_PURPOSE_VOSTRO1;
    @Column(name = "R1_CURRENCY_VOSTRO1")
    private String R1_CURRENCY_VOSTRO1;
    @Column(name = "R1_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R1_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R1_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R1_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R2_NAME_OF_BANK_VOSTRO1")
    private String R2_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R2_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R2_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R2_PURPOSE_VOSTRO1")
    private String R2_PURPOSE_VOSTRO1;
    @Column(name = "R2_CURRENCY_VOSTRO1")
    private String R2_CURRENCY_VOSTRO1;
    @Column(name = "R2_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R2_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R2_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R2_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R3_NAME_OF_BANK_VOSTRO1")
    private String R3_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R3_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R3_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R3_PURPOSE_VOSTRO1")
    private String R3_PURPOSE_VOSTRO1;
    @Column(name = "R3_CURRENCY_VOSTRO1")
    private String R3_CURRENCY_VOSTRO1;
    @Column(name = "R3_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R3_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R3_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R3_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R4_NAME_OF_BANK_VOSTRO1")
    private String R4_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R4_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R4_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R4_PURPOSE_VOSTRO1")
    private String R4_PURPOSE_VOSTRO1;
    @Column(name = "R4_CURRENCY_VOSTRO1")
    private String R4_CURRENCY_VOSTRO1;
    @Column(name = "R4_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R4_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R4_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R4_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R5_NAME_OF_BANK_VOSTRO1")
    private String R5_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R5_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R5_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R5_PURPOSE_VOSTRO1")
    private String R5_PURPOSE_VOSTRO1;
    @Column(name = "R5_CURRENCY_VOSTRO1")
    private String R5_CURRENCY_VOSTRO1;
    @Column(name = "R5_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R5_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R5_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R5_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R6_NAME_OF_BANK_VOSTRO1")
    private String R6_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R6_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R6_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R6_PURPOSE_VOSTRO1")
    private String R6_PURPOSE_VOSTRO1;
    @Column(name = "R6_CURRENCY_VOSTRO1")
    private String R6_CURRENCY_VOSTRO1;
    @Column(name = "R6_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R6_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R6_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R6_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R7_NAME_OF_BANK_VOSTRO1")
    private String R7_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R7_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R7_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R7_PURPOSE_VOSTRO1")
    private String R7_PURPOSE_VOSTRO1;
    @Column(name = "R7_CURRENCY_VOSTRO1")
    private String R7_CURRENCY_VOSTRO1;
    @Column(name = "R7_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R7_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R7_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R7_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R8_NAME_OF_BANK_VOSTRO1")
    private String R8_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R8_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R8_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R8_PURPOSE_VOSTRO1")
    private String R8_PURPOSE_VOSTRO1;
    @Column(name = "R8_CURRENCY_VOSTRO1")
    private String R8_CURRENCY_VOSTRO1;
    @Column(name = "R8_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R8_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R8_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R8_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R9_NAME_OF_BANK_VOSTRO1")
    private String R9_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R9_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R9_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R9_PURPOSE_VOSTRO1")
    private String R9_PURPOSE_VOSTRO1;
    @Column(name = "R9_CURRENCY_VOSTRO1")
    private String R9_CURRENCY_VOSTRO1;
    @Column(name = "R9_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R9_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R9_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R9_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R10_NAME_OF_BANK_VOSTRO1")
    private String R10_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R10_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R10_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R10_PURPOSE_VOSTRO1")
    private String R10_PURPOSE_VOSTRO1;
    @Column(name = "R10_CURRENCY_VOSTRO1")
    private String R10_CURRENCY_VOSTRO1;
    @Column(name = "R10_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R10_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R10_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R10_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R11_NAME_OF_BANK_VOSTRO1")
    private String R11_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R11_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R11_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R11_PURPOSE_VOSTRO1")
    private String R11_PURPOSE_VOSTRO1;
    @Column(name = "R11_CURRENCY_VOSTRO1")
    private String R11_CURRENCY_VOSTRO1;
    @Column(name = "R11_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R11_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R11_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R11_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R12_NAME_OF_BANK_VOSTRO1")
    private String R12_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R12_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R12_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R12_PURPOSE_VOSTRO1")
    private String R12_PURPOSE_VOSTRO1;
    @Column(name = "R12_CURRENCY_VOSTRO1")
    private String R12_CURRENCY_VOSTRO1;
    @Column(name = "R12_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R12_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R12_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R12_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R13_NAME_OF_BANK_VOSTRO1")
    private String R13_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R13_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R13_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R13_PURPOSE_VOSTRO1")
    private String R13_PURPOSE_VOSTRO1;
    @Column(name = "R13_CURRENCY_VOSTRO1")
    private String R13_CURRENCY_VOSTRO1;
    @Column(name = "R13_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R13_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R13_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R13_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R14_NAME_OF_BANK_VOSTRO1")
    private String R14_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R14_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R14_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R14_PURPOSE_VOSTRO1")
    private String R14_PURPOSE_VOSTRO1;
    @Column(name = "R14_CURRENCY_VOSTRO1")
    private String R14_CURRENCY_VOSTRO1;
    @Column(name = "R14_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R14_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R14_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R14_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R15_NAME_OF_BANK_VOSTRO1")
    private String R15_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R15_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R15_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R15_PURPOSE_VOSTRO1")
    private String R15_PURPOSE_VOSTRO1;
    @Column(name = "R15_CURRENCY_VOSTRO1")
    private String R15_CURRENCY_VOSTRO1;
    @Column(name = "R15_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R15_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R15_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R15_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R16_NAME_OF_BANK_VOSTRO1")
    private String R16_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R16_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R16_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R16_PURPOSE_VOSTRO1")
    private String R16_PURPOSE_VOSTRO1;
    @Column(name = "R16_CURRENCY_VOSTRO1")
    private String R16_CURRENCY_VOSTRO1;
    @Column(name = "R16_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R16_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R16_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R16_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R17_NAME_OF_BANK_VOSTRO1")
    private String R17_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R17_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R17_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R17_PURPOSE_VOSTRO1")
    private String R17_PURPOSE_VOSTRO1;
    @Column(name = "R17_CURRENCY_VOSTRO1")
    private String R17_CURRENCY_VOSTRO1;
    @Column(name = "R17_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R17_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R17_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R17_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R18_NAME_OF_BANK_VOSTRO1")
    private String R18_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R18_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R18_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R18_PURPOSE_VOSTRO1")
    private String R18_PURPOSE_VOSTRO1;
    @Column(name = "R18_CURRENCY_VOSTRO1")
    private String R18_CURRENCY_VOSTRO1;
    @Column(name = "R18_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R18_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R18_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R18_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R19_NAME_OF_BANK_VOSTRO1")
    private String R19_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R19_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R19_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R19_PURPOSE_VOSTRO1")
    private String R19_PURPOSE_VOSTRO1;
    @Column(name = "R19_CURRENCY_VOSTRO1")
    private String R19_CURRENCY_VOSTRO1;
    @Column(name = "R19_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R19_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R19_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R19_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R20_NAME_OF_BANK_VOSTRO1")
    private String R20_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R20_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R20_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R20_PURPOSE_VOSTRO1")
    private String R20_PURPOSE_VOSTRO1;
    @Column(name = "R20_CURRENCY_VOSTRO1")
    private String R20_CURRENCY_VOSTRO1;
    @Column(name = "R20_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R20_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R20_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R20_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R21_NAME_OF_BANK_VOSTRO1")
    private String R21_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R21_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R21_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R21_PURPOSE_VOSTRO1")
    private String R21_PURPOSE_VOSTRO1;
    @Column(name = "R21_CURRENCY_VOSTRO1")
    private String R21_CURRENCY_VOSTRO1;
    @Column(name = "R21_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R21_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R21_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R21_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R22_NAME_OF_BANK_VOSTRO1")
    private String R22_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R22_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R22_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R22_PURPOSE_VOSTRO1")
    private String R22_PURPOSE_VOSTRO1;
    @Column(name = "R22_CURRENCY_VOSTRO1")
    private String R22_CURRENCY_VOSTRO1;
    @Column(name = "R22_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R22_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R22_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R22_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R23_NAME_OF_BANK_VOSTRO1")
    private String R23_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R23_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R23_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R23_PURPOSE_VOSTRO1")
    private String R23_PURPOSE_VOSTRO1;
    @Column(name = "R23_CURRENCY_VOSTRO1")
    private String R23_CURRENCY_VOSTRO1;
    @Column(name = "R23_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R23_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R23_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R23_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R24_NAME_OF_BANK_VOSTRO1")
    private String R24_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R24_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R24_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R24_PURPOSE_VOSTRO1")
    private String R24_PURPOSE_VOSTRO1;
    @Column(name = "R24_CURRENCY_VOSTRO1")
    private String R24_CURRENCY_VOSTRO1;
    @Column(name = "R24_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R24_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R24_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R24_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R25_NAME_OF_BANK_VOSTRO1")
    private String R25_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R25_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R25_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R25_PURPOSE_VOSTRO1")
    private String R25_PURPOSE_VOSTRO1;
    @Column(name = "R25_CURRENCY_VOSTRO1")
    private String R25_CURRENCY_VOSTRO1;
    @Column(name = "R25_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R25_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R25_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R25_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R26_NAME_OF_BANK_VOSTRO1")
    private String R26_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R26_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R26_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R26_PURPOSE_VOSTRO1")
    private String R26_PURPOSE_VOSTRO1;
    @Column(name = "R26_CURRENCY_VOSTRO1")
    private String R26_CURRENCY_VOSTRO1;
    @Column(name = "R26_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R26_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R26_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R26_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R27_NAME_OF_BANK_VOSTRO1")
    private String R27_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R27_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R27_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R27_PURPOSE_VOSTRO1")
    private String R27_PURPOSE_VOSTRO1;
    @Column(name = "R27_CURRENCY_VOSTRO1")
    private String R27_CURRENCY_VOSTRO1;
    @Column(name = "R27_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R27_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R27_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R27_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R28_NAME_OF_BANK_VOSTRO1")
    private String R28_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R28_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R28_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R28_PURPOSE_VOSTRO1")
    private String R28_PURPOSE_VOSTRO1;
    @Column(name = "R28_CURRENCY_VOSTRO1")
    private String R28_CURRENCY_VOSTRO1;
    @Column(name = "R28_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R28_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R28_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R28_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R29_NAME_OF_BANK_VOSTRO1")
    private String R29_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R29_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R29_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R29_PURPOSE_VOSTRO1")
    private String R29_PURPOSE_VOSTRO1;
    @Column(name = "R29_CURRENCY_VOSTRO1")
    private String R29_CURRENCY_VOSTRO1;
    @Column(name = "R29_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R29_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R29_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R29_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R30_NAME_OF_BANK_VOSTRO1")
    private String R30_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R30_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R30_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R30_PURPOSE_VOSTRO1")
    private String R30_PURPOSE_VOSTRO1;
    @Column(name = "R30_CURRENCY_VOSTRO1")
    private String R30_CURRENCY_VOSTRO1;
    @Column(name = "R30_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R30_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R30_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R30_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R31_NAME_OF_BANK_VOSTRO1")
    private String R31_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R31_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R31_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R31_PURPOSE_VOSTRO1")
    private String R31_PURPOSE_VOSTRO1;
    @Column(name = "R31_CURRENCY_VOSTRO1")
    private String R31_CURRENCY_VOSTRO1;
    @Column(name = "R31_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R31_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R31_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R31_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R32_NAME_OF_BANK_VOSTRO1")
    private String R32_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R32_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R32_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R32_PURPOSE_VOSTRO1")
    private String R32_PURPOSE_VOSTRO1;
    @Column(name = "R32_CURRENCY_VOSTRO1")
    private String R32_CURRENCY_VOSTRO1;
    @Column(name = "R32_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R32_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R32_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R32_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R33_NAME_OF_BANK_VOSTRO1")
    private String R33_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R33_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R33_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R33_PURPOSE_VOSTRO1")
    private String R33_PURPOSE_VOSTRO1;
    @Column(name = "R33_CURRENCY_VOSTRO1")
    private String R33_CURRENCY_VOSTRO1;
    @Column(name = "R33_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R33_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R33_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R33_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R34_NAME_OF_BANK_VOSTRO1")
    private String R34_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R34_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R34_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R34_PURPOSE_VOSTRO1")
    private String R34_PURPOSE_VOSTRO1;
    @Column(name = "R34_CURRENCY_VOSTRO1")
    private String R34_CURRENCY_VOSTRO1;
    @Column(name = "R34_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R34_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R34_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R34_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R35_NAME_OF_BANK_VOSTRO1")
    private String R35_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R35_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R35_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R35_PURPOSE_VOSTRO1")
    private String R35_PURPOSE_VOSTRO1;
    @Column(name = "R35_CURRENCY_VOSTRO1")
    private String R35_CURRENCY_VOSTRO1;
    @Column(name = "R35_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R35_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R35_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R35_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R36_NAME_OF_BANK_VOSTRO1")
    private String R36_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R36_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R36_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R36_PURPOSE_VOSTRO1")
    private String R36_PURPOSE_VOSTRO1;
    @Column(name = "R36_CURRENCY_VOSTRO1")
    private String R36_CURRENCY_VOSTRO1;
    @Column(name = "R36_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R36_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R36_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R36_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R37_NAME_OF_BANK_VOSTRO1")
    private String R37_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R37_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R37_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R37_PURPOSE_VOSTRO1")
    private String R37_PURPOSE_VOSTRO1;
    @Column(name = "R37_CURRENCY_VOSTRO1")
    private String R37_CURRENCY_VOSTRO1;
    @Column(name = "R37_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R37_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R37_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R37_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R38_NAME_OF_BANK_VOSTRO1")
    private String R38_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R38_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R38_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R38_PURPOSE_VOSTRO1")
    private String R38_PURPOSE_VOSTRO1;
    @Column(name = "R38_CURRENCY_VOSTRO1")
    private String R38_CURRENCY_VOSTRO1;
    @Column(name = "R38_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R38_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R38_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R38_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R39_NAME_OF_BANK_VOSTRO1")
    private String R39_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R39_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R39_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R39_PURPOSE_VOSTRO1")
    private String R39_PURPOSE_VOSTRO1;
    @Column(name = "R39_CURRENCY_VOSTRO1")
    private String R39_CURRENCY_VOSTRO1;
    @Column(name = "R39_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R39_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R39_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R39_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R40_NAME_OF_BANK_VOSTRO1")
    private String R40_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R40_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R40_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R40_PURPOSE_VOSTRO1")
    private String R40_PURPOSE_VOSTRO1;
    @Column(name = "R40_CURRENCY_VOSTRO1")
    private String R40_CURRENCY_VOSTRO1;
    @Column(name = "R40_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R40_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R40_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R40_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R41_NAME_OF_BANK_VOSTRO1")
    private String R41_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R41_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R41_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R41_PURPOSE_VOSTRO1")
    private String R41_PURPOSE_VOSTRO1;
    @Column(name = "R41_CURRENCY_VOSTRO1")
    private String R41_CURRENCY_VOSTRO1;
    @Column(name = "R41_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R41_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R41_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R41_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R42_NAME_OF_BANK_VOSTRO1")
    private String R42_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R42_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R42_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R42_PURPOSE_VOSTRO1")
    private String R42_PURPOSE_VOSTRO1;
    @Column(name = "R42_CURRENCY_VOSTRO1")
    private String R42_CURRENCY_VOSTRO1;
    @Column(name = "R42_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R42_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R42_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R42_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R43_NAME_OF_BANK_VOSTRO1")
    private String R43_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R43_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R43_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R43_PURPOSE_VOSTRO1")
    private String R43_PURPOSE_VOSTRO1;
    @Column(name = "R43_CURRENCY_VOSTRO1")
    private String R43_CURRENCY_VOSTRO1;
    @Column(name = "R43_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R43_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R43_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R43_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R44_NAME_OF_BANK_VOSTRO1")
    private String R44_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R44_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R44_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R44_PURPOSE_VOSTRO1")
    private String R44_PURPOSE_VOSTRO1;
    @Column(name = "R44_CURRENCY_VOSTRO1")
    private String R44_CURRENCY_VOSTRO1;
    @Column(name = "R44_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R44_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R44_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R44_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R45_NAME_OF_BANK_VOSTRO1")
    private String R45_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R45_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R45_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R45_PURPOSE_VOSTRO1")
    private String R45_PURPOSE_VOSTRO1;
    @Column(name = "R45_CURRENCY_VOSTRO1")
    private String R45_CURRENCY_VOSTRO1;
    @Column(name = "R45_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R45_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R45_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R45_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R46_NAME_OF_BANK_VOSTRO1")
    private String R46_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R46_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R46_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R46_PURPOSE_VOSTRO1")
    private String R46_PURPOSE_VOSTRO1;
    @Column(name = "R46_CURRENCY_VOSTRO1")
    private String R46_CURRENCY_VOSTRO1;
    @Column(name = "R46_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R46_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R46_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R46_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R47_NAME_OF_BANK_VOSTRO1")
    private String R47_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R47_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R47_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R47_PURPOSE_VOSTRO1")
    private String R47_PURPOSE_VOSTRO1;
    @Column(name = "R47_CURRENCY_VOSTRO1")
    private String R47_CURRENCY_VOSTRO1;
    @Column(name = "R47_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R47_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R47_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R47_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R48_NAME_OF_BANK_VOSTRO1")
    private String R48_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R48_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R48_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R48_PURPOSE_VOSTRO1")
    private String R48_PURPOSE_VOSTRO1;
    @Column(name = "R48_CURRENCY_VOSTRO1")
    private String R48_CURRENCY_VOSTRO1;
    @Column(name = "R48_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R48_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R48_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R48_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R49_NAME_OF_BANK_VOSTRO1")
    private String R49_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R49_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R49_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R49_PURPOSE_VOSTRO1")
    private String R49_PURPOSE_VOSTRO1;
    @Column(name = "R49_CURRENCY_VOSTRO1")
    private String R49_CURRENCY_VOSTRO1;
    @Column(name = "R49_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R49_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R49_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R49_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R50_NAME_OF_BANK_VOSTRO1")
    private String R50_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R50_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R50_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R50_PURPOSE_VOSTRO1")
    private String R50_PURPOSE_VOSTRO1;
    @Column(name = "R50_CURRENCY_VOSTRO1")
    private String R50_CURRENCY_VOSTRO1;
    @Column(name = "R50_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R50_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R50_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R50_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R51_NAME_OF_BANK_VOSTRO1")
    private String R51_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R51_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R51_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R51_PURPOSE_VOSTRO1")
    private String R51_PURPOSE_VOSTRO1;
    @Column(name = "R51_CURRENCY_VOSTRO1")
    private String R51_CURRENCY_VOSTRO1;
    @Column(name = "R51_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R51_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R51_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R51_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R52_NAME_OF_BANK_VOSTRO1")
    private String R52_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R52_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R52_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R52_PURPOSE_VOSTRO1")
    private String R52_PURPOSE_VOSTRO1;
    @Column(name = "R52_CURRENCY_VOSTRO1")
    private String R52_CURRENCY_VOSTRO1;
    @Column(name = "R52_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R52_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R52_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R52_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R53_NAME_OF_BANK_VOSTRO1")
    private String R53_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R53_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R53_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R53_PURPOSE_VOSTRO1")
    private String R53_PURPOSE_VOSTRO1;
    @Column(name = "R53_CURRENCY_VOSTRO1")
    private String R53_CURRENCY_VOSTRO1;
    @Column(name = "R53_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R53_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R53_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R53_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R54_NAME_OF_BANK_VOSTRO1")
    private String R54_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R54_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R54_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R54_PURPOSE_VOSTRO1")
    private String R54_PURPOSE_VOSTRO1;
    @Column(name = "R54_CURRENCY_VOSTRO1")
    private String R54_CURRENCY_VOSTRO1;
    @Column(name = "R54_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R54_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R54_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R54_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R55_NAME_OF_BANK_VOSTRO1")
    private String R55_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R55_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R55_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R55_PURPOSE_VOSTRO1")
    private String R55_PURPOSE_VOSTRO1;
    @Column(name = "R55_CURRENCY_VOSTRO1")
    private String R55_CURRENCY_VOSTRO1;
    @Column(name = "R55_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R55_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R55_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R55_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R56_NAME_OF_BANK_VOSTRO1")
    private String R56_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R56_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R56_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R56_PURPOSE_VOSTRO1")
    private String R56_PURPOSE_VOSTRO1;
    @Column(name = "R56_CURRENCY_VOSTRO1")
    private String R56_CURRENCY_VOSTRO1;
    @Column(name = "R56_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R56_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R56_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R56_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R57_NAME_OF_BANK_VOSTRO1")
    private String R57_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R57_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R57_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R57_PURPOSE_VOSTRO1")
    private String R57_PURPOSE_VOSTRO1;
    @Column(name = "R57_CURRENCY_VOSTRO1")
    private String R57_CURRENCY_VOSTRO1;
    @Column(name = "R57_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R57_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R57_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R57_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R58_NAME_OF_BANK_VOSTRO1")
    private String R58_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R58_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R58_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R58_PURPOSE_VOSTRO1")
    private String R58_PURPOSE_VOSTRO1;
    @Column(name = "R58_CURRENCY_VOSTRO1")
    private String R58_CURRENCY_VOSTRO1;
    @Column(name = "R58_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R58_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R58_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R58_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R59_NAME_OF_BANK_VOSTRO1")
    private String R59_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R59_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R59_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R59_PURPOSE_VOSTRO1")
    private String R59_PURPOSE_VOSTRO1;
    @Column(name = "R59_CURRENCY_VOSTRO1")
    private String R59_CURRENCY_VOSTRO1;
    @Column(name = "R59_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R59_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R59_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R59_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R60_NAME_OF_BANK_VOSTRO1")
    private String R60_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R60_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R60_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R60_PURPOSE_VOSTRO1")
    private String R60_PURPOSE_VOSTRO1;
    @Column(name = "R60_CURRENCY_VOSTRO1")
    private String R60_CURRENCY_VOSTRO1;
    @Column(name = "R60_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R60_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R60_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R60_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R61_NAME_OF_BANK_VOSTRO1")
    private String R61_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R61_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R61_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R61_PURPOSE_VOSTRO1")
    private String R61_PURPOSE_VOSTRO1;
    @Column(name = "R61_CURRENCY_VOSTRO1")
    private String R61_CURRENCY_VOSTRO1;
    @Column(name = "R61_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R61_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R61_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R61_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R62_NAME_OF_BANK_VOSTRO1")
    private String R62_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R62_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R62_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R62_PURPOSE_VOSTRO1")
    private String R62_PURPOSE_VOSTRO1;
    @Column(name = "R62_CURRENCY_VOSTRO1")
    private String R62_CURRENCY_VOSTRO1;
    @Column(name = "R62_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R62_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R62_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R62_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R63_NAME_OF_BANK_VOSTRO1")
    private String R63_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R63_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R63_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R63_PURPOSE_VOSTRO1")
    private String R63_PURPOSE_VOSTRO1;
    @Column(name = "R63_CURRENCY_VOSTRO1")
    private String R63_CURRENCY_VOSTRO1;
    @Column(name = "R63_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R63_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R63_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R63_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R64_NAME_OF_BANK_VOSTRO1")
    private String R64_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R64_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R64_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R64_PURPOSE_VOSTRO1")
    private String R64_PURPOSE_VOSTRO1;
    @Column(name = "R64_CURRENCY_VOSTRO1")
    private String R64_CURRENCY_VOSTRO1;
    @Column(name = "R64_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R64_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R64_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R64_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R65_NAME_OF_BANK_VOSTRO1")
    private String R65_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R65_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R65_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R65_PURPOSE_VOSTRO1")
    private String R65_PURPOSE_VOSTRO1;
    @Column(name = "R65_CURRENCY_VOSTRO1")
    private String R65_CURRENCY_VOSTRO1;
    @Column(name = "R65_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R65_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R65_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R65_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R66_NAME_OF_BANK_VOSTRO1")
    private String R66_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R66_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R66_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R66_PURPOSE_VOSTRO1")
    private String R66_PURPOSE_VOSTRO1;
    @Column(name = "R66_CURRENCY_VOSTRO1")
    private String R66_CURRENCY_VOSTRO1;
    @Column(name = "R66_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R66_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R66_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R66_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R67_NAME_OF_BANK_VOSTRO1")
    private String R67_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R67_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R67_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R67_PURPOSE_VOSTRO1")
    private String R67_PURPOSE_VOSTRO1;
    @Column(name = "R67_CURRENCY_VOSTRO1")
    private String R67_CURRENCY_VOSTRO1;
    @Column(name = "R67_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R67_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R67_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R67_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R68_NAME_OF_BANK_VOSTRO1")
    private String R68_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R68_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R68_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R68_PURPOSE_VOSTRO1")
    private String R68_PURPOSE_VOSTRO1;
    @Column(name = "R68_CURRENCY_VOSTRO1")
    private String R68_CURRENCY_VOSTRO1;
    @Column(name = "R68_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R68_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R68_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R68_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R69_NAME_OF_BANK_VOSTRO1")
    private String R69_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R69_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R69_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R69_PURPOSE_VOSTRO1")
    private String R69_PURPOSE_VOSTRO1;
    @Column(name = "R69_CURRENCY_VOSTRO1")
    private String R69_CURRENCY_VOSTRO1;
    @Column(name = "R69_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R69_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R69_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R69_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R70_NAME_OF_BANK_VOSTRO1")
    private String R70_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R70_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R70_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R70_PURPOSE_VOSTRO1")
    private String R70_PURPOSE_VOSTRO1;
    @Column(name = "R70_CURRENCY_VOSTRO1")
    private String R70_CURRENCY_VOSTRO1;
    @Column(name = "R70_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R70_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R70_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R70_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R71_NAME_OF_BANK_VOSTRO1")
    private String R71_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R71_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R71_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R71_PURPOSE_VOSTRO1")
    private String R71_PURPOSE_VOSTRO1;
    @Column(name = "R71_CURRENCY_VOSTRO1")
    private String R71_CURRENCY_VOSTRO1;
    @Column(name = "R71_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R71_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R71_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R71_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R72_NAME_OF_BANK_VOSTRO1")
    private String R72_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R72_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R72_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R72_PURPOSE_VOSTRO1")
    private String R72_PURPOSE_VOSTRO1;
    @Column(name = "R72_CURRENCY_VOSTRO1")
    private String R72_CURRENCY_VOSTRO1;
    @Column(name = "R72_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R72_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R72_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R72_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R73_NAME_OF_BANK_VOSTRO1")
    private String R73_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R73_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R73_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R73_PURPOSE_VOSTRO1")
    private String R73_PURPOSE_VOSTRO1;
    @Column(name = "R73_CURRENCY_VOSTRO1")
    private String R73_CURRENCY_VOSTRO1;
    @Column(name = "R73_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R73_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R73_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R73_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R74_NAME_OF_BANK_VOSTRO1")
    private String R74_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R74_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R74_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R74_PURPOSE_VOSTRO1")
    private String R74_PURPOSE_VOSTRO1;
    @Column(name = "R74_CURRENCY_VOSTRO1")
    private String R74_CURRENCY_VOSTRO1;
    @Column(name = "R74_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R74_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R74_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R74_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R75_NAME_OF_BANK_VOSTRO1")
    private String R75_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R75_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R75_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R75_PURPOSE_VOSTRO1")
    private String R75_PURPOSE_VOSTRO1;
    @Column(name = "R75_CURRENCY_VOSTRO1")
    private String R75_CURRENCY_VOSTRO1;
    @Column(name = "R75_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R75_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R75_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R75_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R76_NAME_OF_BANK_VOSTRO1")
    private String R76_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R76_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R76_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R76_PURPOSE_VOSTRO1")
    private String R76_PURPOSE_VOSTRO1;
    @Column(name = "R76_CURRENCY_VOSTRO1")
    private String R76_CURRENCY_VOSTRO1;
    @Column(name = "R76_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R76_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R76_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R76_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R77_NAME_OF_BANK_VOSTRO1")
    private String R77_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R77_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R77_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R77_PURPOSE_VOSTRO1")
    private String R77_PURPOSE_VOSTRO1;
    @Column(name = "R77_CURRENCY_VOSTRO1")
    private String R77_CURRENCY_VOSTRO1;
    @Column(name = "R77_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R77_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R77_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R77_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R78_NAME_OF_BANK_VOSTRO1")
    private String R78_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R78_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R78_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R78_PURPOSE_VOSTRO1")
    private String R78_PURPOSE_VOSTRO1;
    @Column(name = "R78_CURRENCY_VOSTRO1")
    private String R78_CURRENCY_VOSTRO1;
    @Column(name = "R78_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R78_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R78_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R78_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R79_NAME_OF_BANK_VOSTRO1")
    private String R79_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R79_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R79_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R79_PURPOSE_VOSTRO1")
    private String R79_PURPOSE_VOSTRO1;
    @Column(name = "R79_CURRENCY_VOSTRO1")
    private String R79_CURRENCY_VOSTRO1;
    @Column(name = "R79_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R79_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R79_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R79_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R80_NAME_OF_BANK_VOSTRO1")
    private String R80_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R80_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R80_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R80_PURPOSE_VOSTRO1")
    private String R80_PURPOSE_VOSTRO1;
    @Column(name = "R80_CURRENCY_VOSTRO1")
    private String R80_CURRENCY_VOSTRO1;
    @Column(name = "R80_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R80_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R80_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R80_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R81_NAME_OF_BANK_VOSTRO1")
    private String R81_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R81_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R81_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R81_PURPOSE_VOSTRO1")
    private String R81_PURPOSE_VOSTRO1;
    @Column(name = "R81_CURRENCY_VOSTRO1")
    private String R81_CURRENCY_VOSTRO1;
    @Column(name = "R81_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R81_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R81_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R81_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R82_NAME_OF_BANK_VOSTRO1")
    private String R82_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R82_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R82_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R82_PURPOSE_VOSTRO1")
    private String R82_PURPOSE_VOSTRO1;
    @Column(name = "R82_CURRENCY_VOSTRO1")
    private String R82_CURRENCY_VOSTRO1;
    @Column(name = "R82_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R82_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R82_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R82_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R83_NAME_OF_BANK_VOSTRO1")
    private String R83_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R83_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R83_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R83_PURPOSE_VOSTRO1")
    private String R83_PURPOSE_VOSTRO1;
    @Column(name = "R83_CURRENCY_VOSTRO1")
    private String R83_CURRENCY_VOSTRO1;
    @Column(name = "R83_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R83_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R83_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R83_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R84_NAME_OF_BANK_VOSTRO1")
    private String R84_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R84_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R84_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R84_PURPOSE_VOSTRO1")
    private String R84_PURPOSE_VOSTRO1;
    @Column(name = "R84_CURRENCY_VOSTRO1")
    private String R84_CURRENCY_VOSTRO1;
    @Column(name = "R84_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R84_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R84_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R84_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R85_NAME_OF_BANK_VOSTRO1")
    private String R85_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R85_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R85_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R85_PURPOSE_VOSTRO1")
    private String R85_PURPOSE_VOSTRO1;
    @Column(name = "R85_CURRENCY_VOSTRO1")
    private String R85_CURRENCY_VOSTRO1;
    @Column(name = "R85_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R85_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R85_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R85_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R86_NAME_OF_BANK_VOSTRO1")
    private String R86_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R86_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R86_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R86_PURPOSE_VOSTRO1")
    private String R86_PURPOSE_VOSTRO1;
    @Column(name = "R86_CURRENCY_VOSTRO1")
    private String R86_CURRENCY_VOSTRO1;
    @Column(name = "R86_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R86_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R86_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R86_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R87_NAME_OF_BANK_VOSTRO1")
    private String R87_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R87_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R87_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R87_PURPOSE_VOSTRO1")
    private String R87_PURPOSE_VOSTRO1;
    @Column(name = "R87_CURRENCY_VOSTRO1")
    private String R87_CURRENCY_VOSTRO1;
    @Column(name = "R87_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R87_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R87_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R87_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R88_NAME_OF_BANK_VOSTRO1")
    private String R88_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R88_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R88_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R88_PURPOSE_VOSTRO1")
    private String R88_PURPOSE_VOSTRO1;
    @Column(name = "R88_CURRENCY_VOSTRO1")
    private String R88_CURRENCY_VOSTRO1;
    @Column(name = "R88_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R88_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R88_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R88_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R89_NAME_OF_BANK_VOSTRO1")
    private String R89_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R89_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R89_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R89_PURPOSE_VOSTRO1")
    private String R89_PURPOSE_VOSTRO1;
    @Column(name = "R89_CURRENCY_VOSTRO1")
    private String R89_CURRENCY_VOSTRO1;
    @Column(name = "R89_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R89_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R89_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R89_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R90_NAME_OF_BANK_VOSTRO1")
    private String R90_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R90_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R90_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R90_PURPOSE_VOSTRO1")
    private String R90_PURPOSE_VOSTRO1;
    @Column(name = "R90_CURRENCY_VOSTRO1")
    private String R90_CURRENCY_VOSTRO1;
    @Column(name = "R90_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R90_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R90_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R90_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R91_NAME_OF_BANK_VOSTRO1")
    private String R91_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R91_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R91_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R91_PURPOSE_VOSTRO1")
    private String R91_PURPOSE_VOSTRO1;
    @Column(name = "R91_CURRENCY_VOSTRO1")
    private String R91_CURRENCY_VOSTRO1;
    @Column(name = "R91_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R91_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R91_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R91_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R92_NAME_OF_BANK_VOSTRO1")
    private String R92_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R92_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R92_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R92_PURPOSE_VOSTRO1")
    private String R92_PURPOSE_VOSTRO1;
    @Column(name = "R92_CURRENCY_VOSTRO1")
    private String R92_CURRENCY_VOSTRO1;
    @Column(name = "R92_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R92_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R92_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R92_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R93_NAME_OF_BANK_VOSTRO1")
    private String R93_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R93_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R93_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R93_PURPOSE_VOSTRO1")
    private String R93_PURPOSE_VOSTRO1;
    @Column(name = "R93_CURRENCY_VOSTRO1")
    private String R93_CURRENCY_VOSTRO1;
    @Column(name = "R93_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R93_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R93_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R93_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R94_NAME_OF_BANK_VOSTRO1")
    private String R94_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R94_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R94_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R94_PURPOSE_VOSTRO1")
    private String R94_PURPOSE_VOSTRO1;
    @Column(name = "R94_CURRENCY_VOSTRO1")
    private String R94_CURRENCY_VOSTRO1;
    @Column(name = "R94_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R94_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R94_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R94_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R95_NAME_OF_BANK_VOSTRO1")
    private String R95_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R95_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R95_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R95_PURPOSE_VOSTRO1")
    private String R95_PURPOSE_VOSTRO1;
    @Column(name = "R95_CURRENCY_VOSTRO1")
    private String R95_CURRENCY_VOSTRO1;
    @Column(name = "R95_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R95_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R95_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R95_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R96_NAME_OF_BANK_VOSTRO1")
    private String R96_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R96_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R96_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R96_PURPOSE_VOSTRO1")
    private String R96_PURPOSE_VOSTRO1;
    @Column(name = "R96_CURRENCY_VOSTRO1")
    private String R96_CURRENCY_VOSTRO1;
    @Column(name = "R96_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R96_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R96_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R96_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R97_NAME_OF_BANK_VOSTRO1")
    private String R97_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R97_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R97_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R97_PURPOSE_VOSTRO1")
    private String R97_PURPOSE_VOSTRO1;
    @Column(name = "R97_CURRENCY_VOSTRO1")
    private String R97_CURRENCY_VOSTRO1;
    @Column(name = "R97_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R97_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R97_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R97_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R98_NAME_OF_BANK_VOSTRO1")
    private String R98_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R98_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R98_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R98_PURPOSE_VOSTRO1")
    private String R98_PURPOSE_VOSTRO1;
    @Column(name = "R98_CURRENCY_VOSTRO1")
    private String R98_CURRENCY_VOSTRO1;
    @Column(name = "R98_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R98_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R98_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R98_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R99_NAME_OF_BANK_VOSTRO1")
    private String R99_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R99_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R99_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R99_PURPOSE_VOSTRO1")
    private String R99_PURPOSE_VOSTRO1;
    @Column(name = "R99_CURRENCY_VOSTRO1")
    private String R99_CURRENCY_VOSTRO1;
    @Column(name = "R99_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R99_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R99_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R99_AMOUNT_TIME_VOSTRO1;

    @Column(name = "R100_NAME_OF_BANK_VOSTRO1")
    private String R100_NAME_OF_BANK_VOSTRO1;
    @Column(name = "R100_TYPE_OF_ACCOUNT_VOSTRO1")
    private String R100_TYPE_OF_ACCOUNT_VOSTRO1;
    @Column(name = "R100_PURPOSE_VOSTRO1")
    private String R100_PURPOSE_VOSTRO1;
    @Column(name = "R100_CURRENCY_VOSTRO1")
    private String R100_CURRENCY_VOSTRO1;
    @Column(name = "R100_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R100_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R100_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R100_AMOUNT_TIME_VOSTRO1;
    
    @Column(name = "R101_TOTAL_AMOUNT_DEMAND_VOSTRO1")
    private BigDecimal R101_TOTAL_AMOUNT_DEMAND_VOSTRO1;
    @Column(name = "R101_TOTAL_AMOUNT_TIME_VOSTRO1")
    private BigDecimal R101_TOTAL_AMOUNT_TIME_VOSTRO1;
    
    
	public BigDecimal getR101_TOTAL_AMOUNT_DEMAND_VOSTRO1() {
		return R101_TOTAL_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR101_TOTAL_AMOUNT_DEMAND_VOSTRO1(BigDecimal r101_TOTAL_AMOUNT_DEMAND_VOSTRO1) {
		R101_TOTAL_AMOUNT_DEMAND_VOSTRO1 = r101_TOTAL_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR101_TOTAL_AMOUNT_TIME_VOSTRO1() {
		return R101_TOTAL_AMOUNT_TIME_VOSTRO1;
	}
	public void setR101_TOTAL_AMOUNT_TIME_VOSTRO1(BigDecimal r101_TOTAL_AMOUNT_TIME_VOSTRO1) {
		R101_TOTAL_AMOUNT_TIME_VOSTRO1 = r101_TOTAL_AMOUNT_TIME_VOSTRO1;
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
	public String getR1_NAME_OF_BANK_VOSTRO1() {
		return R1_NAME_OF_BANK_VOSTRO1;
	}
	public void setR1_NAME_OF_BANK_VOSTRO1(String r1_NAME_OF_BANK_VOSTRO1) {
		R1_NAME_OF_BANK_VOSTRO1 = r1_NAME_OF_BANK_VOSTRO1;
	}
	public String getR1_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R1_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR1_TYPE_OF_ACCOUNT_VOSTRO1(String r1_TYPE_OF_ACCOUNT_VOSTRO1) {
		R1_TYPE_OF_ACCOUNT_VOSTRO1 = r1_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR1_PURPOSE_VOSTRO1() {
		return R1_PURPOSE_VOSTRO1;
	}
	public void setR1_PURPOSE_VOSTRO1(String r1_PURPOSE_VOSTRO1) {
		R1_PURPOSE_VOSTRO1 = r1_PURPOSE_VOSTRO1;
	}
	public String getR1_CURRENCY_VOSTRO1() {
		return R1_CURRENCY_VOSTRO1;
	}
	public void setR1_CURRENCY_VOSTRO1(String r1_CURRENCY_VOSTRO1) {
		R1_CURRENCY_VOSTRO1 = r1_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR1_AMOUNT_DEMAND_VOSTRO1() {
		return R1_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR1_AMOUNT_DEMAND_VOSTRO1(BigDecimal r1_AMOUNT_DEMAND_VOSTRO1) {
		R1_AMOUNT_DEMAND_VOSTRO1 = r1_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR1_AMOUNT_TIME_VOSTRO1() {
		return R1_AMOUNT_TIME_VOSTRO1;
	}
	public void setR1_AMOUNT_TIME_VOSTRO1(BigDecimal r1_AMOUNT_TIME_VOSTRO1) {
		R1_AMOUNT_TIME_VOSTRO1 = r1_AMOUNT_TIME_VOSTRO1;
	}
	public String getR2_NAME_OF_BANK_VOSTRO1() {
		return R2_NAME_OF_BANK_VOSTRO1;
	}
	public void setR2_NAME_OF_BANK_VOSTRO1(String r2_NAME_OF_BANK_VOSTRO1) {
		R2_NAME_OF_BANK_VOSTRO1 = r2_NAME_OF_BANK_VOSTRO1;
	}
	public String getR2_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R2_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR2_TYPE_OF_ACCOUNT_VOSTRO1(String r2_TYPE_OF_ACCOUNT_VOSTRO1) {
		R2_TYPE_OF_ACCOUNT_VOSTRO1 = r2_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR2_PURPOSE_VOSTRO1() {
		return R2_PURPOSE_VOSTRO1;
	}
	public void setR2_PURPOSE_VOSTRO1(String r2_PURPOSE_VOSTRO1) {
		R2_PURPOSE_VOSTRO1 = r2_PURPOSE_VOSTRO1;
	}
	public String getR2_CURRENCY_VOSTRO1() {
		return R2_CURRENCY_VOSTRO1;
	}
	public void setR2_CURRENCY_VOSTRO1(String r2_CURRENCY_VOSTRO1) {
		R2_CURRENCY_VOSTRO1 = r2_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR2_AMOUNT_DEMAND_VOSTRO1() {
		return R2_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR2_AMOUNT_DEMAND_VOSTRO1(BigDecimal r2_AMOUNT_DEMAND_VOSTRO1) {
		R2_AMOUNT_DEMAND_VOSTRO1 = r2_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR2_AMOUNT_TIME_VOSTRO1() {
		return R2_AMOUNT_TIME_VOSTRO1;
	}
	public void setR2_AMOUNT_TIME_VOSTRO1(BigDecimal r2_AMOUNT_TIME_VOSTRO1) {
		R2_AMOUNT_TIME_VOSTRO1 = r2_AMOUNT_TIME_VOSTRO1;
	}
	public String getR3_NAME_OF_BANK_VOSTRO1() {
		return R3_NAME_OF_BANK_VOSTRO1;
	}
	public void setR3_NAME_OF_BANK_VOSTRO1(String r3_NAME_OF_BANK_VOSTRO1) {
		R3_NAME_OF_BANK_VOSTRO1 = r3_NAME_OF_BANK_VOSTRO1;
	}
	public String getR3_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R3_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR3_TYPE_OF_ACCOUNT_VOSTRO1(String r3_TYPE_OF_ACCOUNT_VOSTRO1) {
		R3_TYPE_OF_ACCOUNT_VOSTRO1 = r3_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR3_PURPOSE_VOSTRO1() {
		return R3_PURPOSE_VOSTRO1;
	}
	public void setR3_PURPOSE_VOSTRO1(String r3_PURPOSE_VOSTRO1) {
		R3_PURPOSE_VOSTRO1 = r3_PURPOSE_VOSTRO1;
	}
	public String getR3_CURRENCY_VOSTRO1() {
		return R3_CURRENCY_VOSTRO1;
	}
	public void setR3_CURRENCY_VOSTRO1(String r3_CURRENCY_VOSTRO1) {
		R3_CURRENCY_VOSTRO1 = r3_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR3_AMOUNT_DEMAND_VOSTRO1() {
		return R3_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR3_AMOUNT_DEMAND_VOSTRO1(BigDecimal r3_AMOUNT_DEMAND_VOSTRO1) {
		R3_AMOUNT_DEMAND_VOSTRO1 = r3_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR3_AMOUNT_TIME_VOSTRO1() {
		return R3_AMOUNT_TIME_VOSTRO1;
	}
	public void setR3_AMOUNT_TIME_VOSTRO1(BigDecimal r3_AMOUNT_TIME_VOSTRO1) {
		R3_AMOUNT_TIME_VOSTRO1 = r3_AMOUNT_TIME_VOSTRO1;
	}
	public String getR4_NAME_OF_BANK_VOSTRO1() {
		return R4_NAME_OF_BANK_VOSTRO1;
	}
	public void setR4_NAME_OF_BANK_VOSTRO1(String r4_NAME_OF_BANK_VOSTRO1) {
		R4_NAME_OF_BANK_VOSTRO1 = r4_NAME_OF_BANK_VOSTRO1;
	}
	public String getR4_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R4_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR4_TYPE_OF_ACCOUNT_VOSTRO1(String r4_TYPE_OF_ACCOUNT_VOSTRO1) {
		R4_TYPE_OF_ACCOUNT_VOSTRO1 = r4_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR4_PURPOSE_VOSTRO1() {
		return R4_PURPOSE_VOSTRO1;
	}
	public void setR4_PURPOSE_VOSTRO1(String r4_PURPOSE_VOSTRO1) {
		R4_PURPOSE_VOSTRO1 = r4_PURPOSE_VOSTRO1;
	}
	public String getR4_CURRENCY_VOSTRO1() {
		return R4_CURRENCY_VOSTRO1;
	}
	public void setR4_CURRENCY_VOSTRO1(String r4_CURRENCY_VOSTRO1) {
		R4_CURRENCY_VOSTRO1 = r4_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR4_AMOUNT_DEMAND_VOSTRO1() {
		return R4_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR4_AMOUNT_DEMAND_VOSTRO1(BigDecimal r4_AMOUNT_DEMAND_VOSTRO1) {
		R4_AMOUNT_DEMAND_VOSTRO1 = r4_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR4_AMOUNT_TIME_VOSTRO1() {
		return R4_AMOUNT_TIME_VOSTRO1;
	}
	public void setR4_AMOUNT_TIME_VOSTRO1(BigDecimal r4_AMOUNT_TIME_VOSTRO1) {
		R4_AMOUNT_TIME_VOSTRO1 = r4_AMOUNT_TIME_VOSTRO1;
	}
	public String getR5_NAME_OF_BANK_VOSTRO1() {
		return R5_NAME_OF_BANK_VOSTRO1;
	}
	public void setR5_NAME_OF_BANK_VOSTRO1(String r5_NAME_OF_BANK_VOSTRO1) {
		R5_NAME_OF_BANK_VOSTRO1 = r5_NAME_OF_BANK_VOSTRO1;
	}
	public String getR5_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R5_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR5_TYPE_OF_ACCOUNT_VOSTRO1(String r5_TYPE_OF_ACCOUNT_VOSTRO1) {
		R5_TYPE_OF_ACCOUNT_VOSTRO1 = r5_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR5_PURPOSE_VOSTRO1() {
		return R5_PURPOSE_VOSTRO1;
	}
	public void setR5_PURPOSE_VOSTRO1(String r5_PURPOSE_VOSTRO1) {
		R5_PURPOSE_VOSTRO1 = r5_PURPOSE_VOSTRO1;
	}
	public String getR5_CURRENCY_VOSTRO1() {
		return R5_CURRENCY_VOSTRO1;
	}
	public void setR5_CURRENCY_VOSTRO1(String r5_CURRENCY_VOSTRO1) {
		R5_CURRENCY_VOSTRO1 = r5_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR5_AMOUNT_DEMAND_VOSTRO1() {
		return R5_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR5_AMOUNT_DEMAND_VOSTRO1(BigDecimal r5_AMOUNT_DEMAND_VOSTRO1) {
		R5_AMOUNT_DEMAND_VOSTRO1 = r5_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR5_AMOUNT_TIME_VOSTRO1() {
		return R5_AMOUNT_TIME_VOSTRO1;
	}
	public void setR5_AMOUNT_TIME_VOSTRO1(BigDecimal r5_AMOUNT_TIME_VOSTRO1) {
		R5_AMOUNT_TIME_VOSTRO1 = r5_AMOUNT_TIME_VOSTRO1;
	}
	public String getR6_NAME_OF_BANK_VOSTRO1() {
		return R6_NAME_OF_BANK_VOSTRO1;
	}
	public void setR6_NAME_OF_BANK_VOSTRO1(String r6_NAME_OF_BANK_VOSTRO1) {
		R6_NAME_OF_BANK_VOSTRO1 = r6_NAME_OF_BANK_VOSTRO1;
	}
	public String getR6_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R6_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR6_TYPE_OF_ACCOUNT_VOSTRO1(String r6_TYPE_OF_ACCOUNT_VOSTRO1) {
		R6_TYPE_OF_ACCOUNT_VOSTRO1 = r6_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR6_PURPOSE_VOSTRO1() {
		return R6_PURPOSE_VOSTRO1;
	}
	public void setR6_PURPOSE_VOSTRO1(String r6_PURPOSE_VOSTRO1) {
		R6_PURPOSE_VOSTRO1 = r6_PURPOSE_VOSTRO1;
	}
	public String getR6_CURRENCY_VOSTRO1() {
		return R6_CURRENCY_VOSTRO1;
	}
	public void setR6_CURRENCY_VOSTRO1(String r6_CURRENCY_VOSTRO1) {
		R6_CURRENCY_VOSTRO1 = r6_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR6_AMOUNT_DEMAND_VOSTRO1() {
		return R6_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR6_AMOUNT_DEMAND_VOSTRO1(BigDecimal r6_AMOUNT_DEMAND_VOSTRO1) {
		R6_AMOUNT_DEMAND_VOSTRO1 = r6_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR6_AMOUNT_TIME_VOSTRO1() {
		return R6_AMOUNT_TIME_VOSTRO1;
	}
	public void setR6_AMOUNT_TIME_VOSTRO1(BigDecimal r6_AMOUNT_TIME_VOSTRO1) {
		R6_AMOUNT_TIME_VOSTRO1 = r6_AMOUNT_TIME_VOSTRO1;
	}
	public String getR7_NAME_OF_BANK_VOSTRO1() {
		return R7_NAME_OF_BANK_VOSTRO1;
	}
	public void setR7_NAME_OF_BANK_VOSTRO1(String r7_NAME_OF_BANK_VOSTRO1) {
		R7_NAME_OF_BANK_VOSTRO1 = r7_NAME_OF_BANK_VOSTRO1;
	}
	public String getR7_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R7_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR7_TYPE_OF_ACCOUNT_VOSTRO1(String r7_TYPE_OF_ACCOUNT_VOSTRO1) {
		R7_TYPE_OF_ACCOUNT_VOSTRO1 = r7_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR7_PURPOSE_VOSTRO1() {
		return R7_PURPOSE_VOSTRO1;
	}
	public void setR7_PURPOSE_VOSTRO1(String r7_PURPOSE_VOSTRO1) {
		R7_PURPOSE_VOSTRO1 = r7_PURPOSE_VOSTRO1;
	}
	public String getR7_CURRENCY_VOSTRO1() {
		return R7_CURRENCY_VOSTRO1;
	}
	public void setR7_CURRENCY_VOSTRO1(String r7_CURRENCY_VOSTRO1) {
		R7_CURRENCY_VOSTRO1 = r7_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR7_AMOUNT_DEMAND_VOSTRO1() {
		return R7_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR7_AMOUNT_DEMAND_VOSTRO1(BigDecimal r7_AMOUNT_DEMAND_VOSTRO1) {
		R7_AMOUNT_DEMAND_VOSTRO1 = r7_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR7_AMOUNT_TIME_VOSTRO1() {
		return R7_AMOUNT_TIME_VOSTRO1;
	}
	public void setR7_AMOUNT_TIME_VOSTRO1(BigDecimal r7_AMOUNT_TIME_VOSTRO1) {
		R7_AMOUNT_TIME_VOSTRO1 = r7_AMOUNT_TIME_VOSTRO1;
	}
	public String getR8_NAME_OF_BANK_VOSTRO1() {
		return R8_NAME_OF_BANK_VOSTRO1;
	}
	public void setR8_NAME_OF_BANK_VOSTRO1(String r8_NAME_OF_BANK_VOSTRO1) {
		R8_NAME_OF_BANK_VOSTRO1 = r8_NAME_OF_BANK_VOSTRO1;
	}
	public String getR8_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R8_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR8_TYPE_OF_ACCOUNT_VOSTRO1(String r8_TYPE_OF_ACCOUNT_VOSTRO1) {
		R8_TYPE_OF_ACCOUNT_VOSTRO1 = r8_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR8_PURPOSE_VOSTRO1() {
		return R8_PURPOSE_VOSTRO1;
	}
	public void setR8_PURPOSE_VOSTRO1(String r8_PURPOSE_VOSTRO1) {
		R8_PURPOSE_VOSTRO1 = r8_PURPOSE_VOSTRO1;
	}
	public String getR8_CURRENCY_VOSTRO1() {
		return R8_CURRENCY_VOSTRO1;
	}
	public void setR8_CURRENCY_VOSTRO1(String r8_CURRENCY_VOSTRO1) {
		R8_CURRENCY_VOSTRO1 = r8_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR8_AMOUNT_DEMAND_VOSTRO1() {
		return R8_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR8_AMOUNT_DEMAND_VOSTRO1(BigDecimal r8_AMOUNT_DEMAND_VOSTRO1) {
		R8_AMOUNT_DEMAND_VOSTRO1 = r8_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR8_AMOUNT_TIME_VOSTRO1() {
		return R8_AMOUNT_TIME_VOSTRO1;
	}
	public void setR8_AMOUNT_TIME_VOSTRO1(BigDecimal r8_AMOUNT_TIME_VOSTRO1) {
		R8_AMOUNT_TIME_VOSTRO1 = r8_AMOUNT_TIME_VOSTRO1;
	}
	public String getR9_NAME_OF_BANK_VOSTRO1() {
		return R9_NAME_OF_BANK_VOSTRO1;
	}
	public void setR9_NAME_OF_BANK_VOSTRO1(String r9_NAME_OF_BANK_VOSTRO1) {
		R9_NAME_OF_BANK_VOSTRO1 = r9_NAME_OF_BANK_VOSTRO1;
	}
	public String getR9_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R9_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR9_TYPE_OF_ACCOUNT_VOSTRO1(String r9_TYPE_OF_ACCOUNT_VOSTRO1) {
		R9_TYPE_OF_ACCOUNT_VOSTRO1 = r9_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR9_PURPOSE_VOSTRO1() {
		return R9_PURPOSE_VOSTRO1;
	}
	public void setR9_PURPOSE_VOSTRO1(String r9_PURPOSE_VOSTRO1) {
		R9_PURPOSE_VOSTRO1 = r9_PURPOSE_VOSTRO1;
	}
	public String getR9_CURRENCY_VOSTRO1() {
		return R9_CURRENCY_VOSTRO1;
	}
	public void setR9_CURRENCY_VOSTRO1(String r9_CURRENCY_VOSTRO1) {
		R9_CURRENCY_VOSTRO1 = r9_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR9_AMOUNT_DEMAND_VOSTRO1() {
		return R9_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR9_AMOUNT_DEMAND_VOSTRO1(BigDecimal r9_AMOUNT_DEMAND_VOSTRO1) {
		R9_AMOUNT_DEMAND_VOSTRO1 = r9_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR9_AMOUNT_TIME_VOSTRO1() {
		return R9_AMOUNT_TIME_VOSTRO1;
	}
	public void setR9_AMOUNT_TIME_VOSTRO1(BigDecimal r9_AMOUNT_TIME_VOSTRO1) {
		R9_AMOUNT_TIME_VOSTRO1 = r9_AMOUNT_TIME_VOSTRO1;
	}
	public String getR10_NAME_OF_BANK_VOSTRO1() {
		return R10_NAME_OF_BANK_VOSTRO1;
	}
	public void setR10_NAME_OF_BANK_VOSTRO1(String r10_NAME_OF_BANK_VOSTRO1) {
		R10_NAME_OF_BANK_VOSTRO1 = r10_NAME_OF_BANK_VOSTRO1;
	}
	public String getR10_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R10_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR10_TYPE_OF_ACCOUNT_VOSTRO1(String r10_TYPE_OF_ACCOUNT_VOSTRO1) {
		R10_TYPE_OF_ACCOUNT_VOSTRO1 = r10_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR10_PURPOSE_VOSTRO1() {
		return R10_PURPOSE_VOSTRO1;
	}
	public void setR10_PURPOSE_VOSTRO1(String r10_PURPOSE_VOSTRO1) {
		R10_PURPOSE_VOSTRO1 = r10_PURPOSE_VOSTRO1;
	}
	public String getR10_CURRENCY_VOSTRO1() {
		return R10_CURRENCY_VOSTRO1;
	}
	public void setR10_CURRENCY_VOSTRO1(String r10_CURRENCY_VOSTRO1) {
		R10_CURRENCY_VOSTRO1 = r10_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR10_AMOUNT_DEMAND_VOSTRO1() {
		return R10_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR10_AMOUNT_DEMAND_VOSTRO1(BigDecimal r10_AMOUNT_DEMAND_VOSTRO1) {
		R10_AMOUNT_DEMAND_VOSTRO1 = r10_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR10_AMOUNT_TIME_VOSTRO1() {
		return R10_AMOUNT_TIME_VOSTRO1;
	}
	public void setR10_AMOUNT_TIME_VOSTRO1(BigDecimal r10_AMOUNT_TIME_VOSTRO1) {
		R10_AMOUNT_TIME_VOSTRO1 = r10_AMOUNT_TIME_VOSTRO1;
	}
	public String getR11_NAME_OF_BANK_VOSTRO1() {
		return R11_NAME_OF_BANK_VOSTRO1;
	}
	public void setR11_NAME_OF_BANK_VOSTRO1(String r11_NAME_OF_BANK_VOSTRO1) {
		R11_NAME_OF_BANK_VOSTRO1 = r11_NAME_OF_BANK_VOSTRO1;
	}
	public String getR11_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R11_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR11_TYPE_OF_ACCOUNT_VOSTRO1(String r11_TYPE_OF_ACCOUNT_VOSTRO1) {
		R11_TYPE_OF_ACCOUNT_VOSTRO1 = r11_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR11_PURPOSE_VOSTRO1() {
		return R11_PURPOSE_VOSTRO1;
	}
	public void setR11_PURPOSE_VOSTRO1(String r11_PURPOSE_VOSTRO1) {
		R11_PURPOSE_VOSTRO1 = r11_PURPOSE_VOSTRO1;
	}
	public String getR11_CURRENCY_VOSTRO1() {
		return R11_CURRENCY_VOSTRO1;
	}
	public void setR11_CURRENCY_VOSTRO1(String r11_CURRENCY_VOSTRO1) {
		R11_CURRENCY_VOSTRO1 = r11_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR11_AMOUNT_DEMAND_VOSTRO1() {
		return R11_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR11_AMOUNT_DEMAND_VOSTRO1(BigDecimal r11_AMOUNT_DEMAND_VOSTRO1) {
		R11_AMOUNT_DEMAND_VOSTRO1 = r11_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR11_AMOUNT_TIME_VOSTRO1() {
		return R11_AMOUNT_TIME_VOSTRO1;
	}
	public void setR11_AMOUNT_TIME_VOSTRO1(BigDecimal r11_AMOUNT_TIME_VOSTRO1) {
		R11_AMOUNT_TIME_VOSTRO1 = r11_AMOUNT_TIME_VOSTRO1;
	}
	public String getR12_NAME_OF_BANK_VOSTRO1() {
		return R12_NAME_OF_BANK_VOSTRO1;
	}
	public void setR12_NAME_OF_BANK_VOSTRO1(String r12_NAME_OF_BANK_VOSTRO1) {
		R12_NAME_OF_BANK_VOSTRO1 = r12_NAME_OF_BANK_VOSTRO1;
	}
	public String getR12_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R12_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR12_TYPE_OF_ACCOUNT_VOSTRO1(String r12_TYPE_OF_ACCOUNT_VOSTRO1) {
		R12_TYPE_OF_ACCOUNT_VOSTRO1 = r12_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR12_PURPOSE_VOSTRO1() {
		return R12_PURPOSE_VOSTRO1;
	}
	public void setR12_PURPOSE_VOSTRO1(String r12_PURPOSE_VOSTRO1) {
		R12_PURPOSE_VOSTRO1 = r12_PURPOSE_VOSTRO1;
	}
	public String getR12_CURRENCY_VOSTRO1() {
		return R12_CURRENCY_VOSTRO1;
	}
	public void setR12_CURRENCY_VOSTRO1(String r12_CURRENCY_VOSTRO1) {
		R12_CURRENCY_VOSTRO1 = r12_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR12_AMOUNT_DEMAND_VOSTRO1() {
		return R12_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR12_AMOUNT_DEMAND_VOSTRO1(BigDecimal r12_AMOUNT_DEMAND_VOSTRO1) {
		R12_AMOUNT_DEMAND_VOSTRO1 = r12_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR12_AMOUNT_TIME_VOSTRO1() {
		return R12_AMOUNT_TIME_VOSTRO1;
	}
	public void setR12_AMOUNT_TIME_VOSTRO1(BigDecimal r12_AMOUNT_TIME_VOSTRO1) {
		R12_AMOUNT_TIME_VOSTRO1 = r12_AMOUNT_TIME_VOSTRO1;
	}
	public String getR13_NAME_OF_BANK_VOSTRO1() {
		return R13_NAME_OF_BANK_VOSTRO1;
	}
	public void setR13_NAME_OF_BANK_VOSTRO1(String r13_NAME_OF_BANK_VOSTRO1) {
		R13_NAME_OF_BANK_VOSTRO1 = r13_NAME_OF_BANK_VOSTRO1;
	}
	public String getR13_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R13_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR13_TYPE_OF_ACCOUNT_VOSTRO1(String r13_TYPE_OF_ACCOUNT_VOSTRO1) {
		R13_TYPE_OF_ACCOUNT_VOSTRO1 = r13_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR13_PURPOSE_VOSTRO1() {
		return R13_PURPOSE_VOSTRO1;
	}
	public void setR13_PURPOSE_VOSTRO1(String r13_PURPOSE_VOSTRO1) {
		R13_PURPOSE_VOSTRO1 = r13_PURPOSE_VOSTRO1;
	}
	public String getR13_CURRENCY_VOSTRO1() {
		return R13_CURRENCY_VOSTRO1;
	}
	public void setR13_CURRENCY_VOSTRO1(String r13_CURRENCY_VOSTRO1) {
		R13_CURRENCY_VOSTRO1 = r13_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR13_AMOUNT_DEMAND_VOSTRO1() {
		return R13_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR13_AMOUNT_DEMAND_VOSTRO1(BigDecimal r13_AMOUNT_DEMAND_VOSTRO1) {
		R13_AMOUNT_DEMAND_VOSTRO1 = r13_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR13_AMOUNT_TIME_VOSTRO1() {
		return R13_AMOUNT_TIME_VOSTRO1;
	}
	public void setR13_AMOUNT_TIME_VOSTRO1(BigDecimal r13_AMOUNT_TIME_VOSTRO1) {
		R13_AMOUNT_TIME_VOSTRO1 = r13_AMOUNT_TIME_VOSTRO1;
	}
	public String getR14_NAME_OF_BANK_VOSTRO1() {
		return R14_NAME_OF_BANK_VOSTRO1;
	}
	public void setR14_NAME_OF_BANK_VOSTRO1(String r14_NAME_OF_BANK_VOSTRO1) {
		R14_NAME_OF_BANK_VOSTRO1 = r14_NAME_OF_BANK_VOSTRO1;
	}
	public String getR14_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R14_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR14_TYPE_OF_ACCOUNT_VOSTRO1(String r14_TYPE_OF_ACCOUNT_VOSTRO1) {
		R14_TYPE_OF_ACCOUNT_VOSTRO1 = r14_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR14_PURPOSE_VOSTRO1() {
		return R14_PURPOSE_VOSTRO1;
	}
	public void setR14_PURPOSE_VOSTRO1(String r14_PURPOSE_VOSTRO1) {
		R14_PURPOSE_VOSTRO1 = r14_PURPOSE_VOSTRO1;
	}
	public String getR14_CURRENCY_VOSTRO1() {
		return R14_CURRENCY_VOSTRO1;
	}
	public void setR14_CURRENCY_VOSTRO1(String r14_CURRENCY_VOSTRO1) {
		R14_CURRENCY_VOSTRO1 = r14_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR14_AMOUNT_DEMAND_VOSTRO1() {
		return R14_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR14_AMOUNT_DEMAND_VOSTRO1(BigDecimal r14_AMOUNT_DEMAND_VOSTRO1) {
		R14_AMOUNT_DEMAND_VOSTRO1 = r14_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR14_AMOUNT_TIME_VOSTRO1() {
		return R14_AMOUNT_TIME_VOSTRO1;
	}
	public void setR14_AMOUNT_TIME_VOSTRO1(BigDecimal r14_AMOUNT_TIME_VOSTRO1) {
		R14_AMOUNT_TIME_VOSTRO1 = r14_AMOUNT_TIME_VOSTRO1;
	}
	public String getR15_NAME_OF_BANK_VOSTRO1() {
		return R15_NAME_OF_BANK_VOSTRO1;
	}
	public void setR15_NAME_OF_BANK_VOSTRO1(String r15_NAME_OF_BANK_VOSTRO1) {
		R15_NAME_OF_BANK_VOSTRO1 = r15_NAME_OF_BANK_VOSTRO1;
	}
	public String getR15_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R15_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR15_TYPE_OF_ACCOUNT_VOSTRO1(String r15_TYPE_OF_ACCOUNT_VOSTRO1) {
		R15_TYPE_OF_ACCOUNT_VOSTRO1 = r15_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR15_PURPOSE_VOSTRO1() {
		return R15_PURPOSE_VOSTRO1;
	}
	public void setR15_PURPOSE_VOSTRO1(String r15_PURPOSE_VOSTRO1) {
		R15_PURPOSE_VOSTRO1 = r15_PURPOSE_VOSTRO1;
	}
	public String getR15_CURRENCY_VOSTRO1() {
		return R15_CURRENCY_VOSTRO1;
	}
	public void setR15_CURRENCY_VOSTRO1(String r15_CURRENCY_VOSTRO1) {
		R15_CURRENCY_VOSTRO1 = r15_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR15_AMOUNT_DEMAND_VOSTRO1() {
		return R15_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR15_AMOUNT_DEMAND_VOSTRO1(BigDecimal r15_AMOUNT_DEMAND_VOSTRO1) {
		R15_AMOUNT_DEMAND_VOSTRO1 = r15_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR15_AMOUNT_TIME_VOSTRO1() {
		return R15_AMOUNT_TIME_VOSTRO1;
	}
	public void setR15_AMOUNT_TIME_VOSTRO1(BigDecimal r15_AMOUNT_TIME_VOSTRO1) {
		R15_AMOUNT_TIME_VOSTRO1 = r15_AMOUNT_TIME_VOSTRO1;
	}
	public String getR16_NAME_OF_BANK_VOSTRO1() {
		return R16_NAME_OF_BANK_VOSTRO1;
	}
	public void setR16_NAME_OF_BANK_VOSTRO1(String r16_NAME_OF_BANK_VOSTRO1) {
		R16_NAME_OF_BANK_VOSTRO1 = r16_NAME_OF_BANK_VOSTRO1;
	}
	public String getR16_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R16_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR16_TYPE_OF_ACCOUNT_VOSTRO1(String r16_TYPE_OF_ACCOUNT_VOSTRO1) {
		R16_TYPE_OF_ACCOUNT_VOSTRO1 = r16_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR16_PURPOSE_VOSTRO1() {
		return R16_PURPOSE_VOSTRO1;
	}
	public void setR16_PURPOSE_VOSTRO1(String r16_PURPOSE_VOSTRO1) {
		R16_PURPOSE_VOSTRO1 = r16_PURPOSE_VOSTRO1;
	}
	public String getR16_CURRENCY_VOSTRO1() {
		return R16_CURRENCY_VOSTRO1;
	}
	public void setR16_CURRENCY_VOSTRO1(String r16_CURRENCY_VOSTRO1) {
		R16_CURRENCY_VOSTRO1 = r16_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR16_AMOUNT_DEMAND_VOSTRO1() {
		return R16_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR16_AMOUNT_DEMAND_VOSTRO1(BigDecimal r16_AMOUNT_DEMAND_VOSTRO1) {
		R16_AMOUNT_DEMAND_VOSTRO1 = r16_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR16_AMOUNT_TIME_VOSTRO1() {
		return R16_AMOUNT_TIME_VOSTRO1;
	}
	public void setR16_AMOUNT_TIME_VOSTRO1(BigDecimal r16_AMOUNT_TIME_VOSTRO1) {
		R16_AMOUNT_TIME_VOSTRO1 = r16_AMOUNT_TIME_VOSTRO1;
	}
	public String getR17_NAME_OF_BANK_VOSTRO1() {
		return R17_NAME_OF_BANK_VOSTRO1;
	}
	public void setR17_NAME_OF_BANK_VOSTRO1(String r17_NAME_OF_BANK_VOSTRO1) {
		R17_NAME_OF_BANK_VOSTRO1 = r17_NAME_OF_BANK_VOSTRO1;
	}
	public String getR17_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R17_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR17_TYPE_OF_ACCOUNT_VOSTRO1(String r17_TYPE_OF_ACCOUNT_VOSTRO1) {
		R17_TYPE_OF_ACCOUNT_VOSTRO1 = r17_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR17_PURPOSE_VOSTRO1() {
		return R17_PURPOSE_VOSTRO1;
	}
	public void setR17_PURPOSE_VOSTRO1(String r17_PURPOSE_VOSTRO1) {
		R17_PURPOSE_VOSTRO1 = r17_PURPOSE_VOSTRO1;
	}
	public String getR17_CURRENCY_VOSTRO1() {
		return R17_CURRENCY_VOSTRO1;
	}
	public void setR17_CURRENCY_VOSTRO1(String r17_CURRENCY_VOSTRO1) {
		R17_CURRENCY_VOSTRO1 = r17_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR17_AMOUNT_DEMAND_VOSTRO1() {
		return R17_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR17_AMOUNT_DEMAND_VOSTRO1(BigDecimal r17_AMOUNT_DEMAND_VOSTRO1) {
		R17_AMOUNT_DEMAND_VOSTRO1 = r17_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR17_AMOUNT_TIME_VOSTRO1() {
		return R17_AMOUNT_TIME_VOSTRO1;
	}
	public void setR17_AMOUNT_TIME_VOSTRO1(BigDecimal r17_AMOUNT_TIME_VOSTRO1) {
		R17_AMOUNT_TIME_VOSTRO1 = r17_AMOUNT_TIME_VOSTRO1;
	}
	public String getR18_NAME_OF_BANK_VOSTRO1() {
		return R18_NAME_OF_BANK_VOSTRO1;
	}
	public void setR18_NAME_OF_BANK_VOSTRO1(String r18_NAME_OF_BANK_VOSTRO1) {
		R18_NAME_OF_BANK_VOSTRO1 = r18_NAME_OF_BANK_VOSTRO1;
	}
	public String getR18_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R18_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR18_TYPE_OF_ACCOUNT_VOSTRO1(String r18_TYPE_OF_ACCOUNT_VOSTRO1) {
		R18_TYPE_OF_ACCOUNT_VOSTRO1 = r18_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR18_PURPOSE_VOSTRO1() {
		return R18_PURPOSE_VOSTRO1;
	}
	public void setR18_PURPOSE_VOSTRO1(String r18_PURPOSE_VOSTRO1) {
		R18_PURPOSE_VOSTRO1 = r18_PURPOSE_VOSTRO1;
	}
	public String getR18_CURRENCY_VOSTRO1() {
		return R18_CURRENCY_VOSTRO1;
	}
	public void setR18_CURRENCY_VOSTRO1(String r18_CURRENCY_VOSTRO1) {
		R18_CURRENCY_VOSTRO1 = r18_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR18_AMOUNT_DEMAND_VOSTRO1() {
		return R18_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR18_AMOUNT_DEMAND_VOSTRO1(BigDecimal r18_AMOUNT_DEMAND_VOSTRO1) {
		R18_AMOUNT_DEMAND_VOSTRO1 = r18_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR18_AMOUNT_TIME_VOSTRO1() {
		return R18_AMOUNT_TIME_VOSTRO1;
	}
	public void setR18_AMOUNT_TIME_VOSTRO1(BigDecimal r18_AMOUNT_TIME_VOSTRO1) {
		R18_AMOUNT_TIME_VOSTRO1 = r18_AMOUNT_TIME_VOSTRO1;
	}
	public String getR19_NAME_OF_BANK_VOSTRO1() {
		return R19_NAME_OF_BANK_VOSTRO1;
	}
	public void setR19_NAME_OF_BANK_VOSTRO1(String r19_NAME_OF_BANK_VOSTRO1) {
		R19_NAME_OF_BANK_VOSTRO1 = r19_NAME_OF_BANK_VOSTRO1;
	}
	public String getR19_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R19_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR19_TYPE_OF_ACCOUNT_VOSTRO1(String r19_TYPE_OF_ACCOUNT_VOSTRO1) {
		R19_TYPE_OF_ACCOUNT_VOSTRO1 = r19_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR19_PURPOSE_VOSTRO1() {
		return R19_PURPOSE_VOSTRO1;
	}
	public void setR19_PURPOSE_VOSTRO1(String r19_PURPOSE_VOSTRO1) {
		R19_PURPOSE_VOSTRO1 = r19_PURPOSE_VOSTRO1;
	}
	public String getR19_CURRENCY_VOSTRO1() {
		return R19_CURRENCY_VOSTRO1;
	}
	public void setR19_CURRENCY_VOSTRO1(String r19_CURRENCY_VOSTRO1) {
		R19_CURRENCY_VOSTRO1 = r19_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR19_AMOUNT_DEMAND_VOSTRO1() {
		return R19_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR19_AMOUNT_DEMAND_VOSTRO1(BigDecimal r19_AMOUNT_DEMAND_VOSTRO1) {
		R19_AMOUNT_DEMAND_VOSTRO1 = r19_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR19_AMOUNT_TIME_VOSTRO1() {
		return R19_AMOUNT_TIME_VOSTRO1;
	}
	public void setR19_AMOUNT_TIME_VOSTRO1(BigDecimal r19_AMOUNT_TIME_VOSTRO1) {
		R19_AMOUNT_TIME_VOSTRO1 = r19_AMOUNT_TIME_VOSTRO1;
	}
	public String getR20_NAME_OF_BANK_VOSTRO1() {
		return R20_NAME_OF_BANK_VOSTRO1;
	}
	public void setR20_NAME_OF_BANK_VOSTRO1(String r20_NAME_OF_BANK_VOSTRO1) {
		R20_NAME_OF_BANK_VOSTRO1 = r20_NAME_OF_BANK_VOSTRO1;
	}
	public String getR20_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R20_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR20_TYPE_OF_ACCOUNT_VOSTRO1(String r20_TYPE_OF_ACCOUNT_VOSTRO1) {
		R20_TYPE_OF_ACCOUNT_VOSTRO1 = r20_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR20_PURPOSE_VOSTRO1() {
		return R20_PURPOSE_VOSTRO1;
	}
	public void setR20_PURPOSE_VOSTRO1(String r20_PURPOSE_VOSTRO1) {
		R20_PURPOSE_VOSTRO1 = r20_PURPOSE_VOSTRO1;
	}
	public String getR20_CURRENCY_VOSTRO1() {
		return R20_CURRENCY_VOSTRO1;
	}
	public void setR20_CURRENCY_VOSTRO1(String r20_CURRENCY_VOSTRO1) {
		R20_CURRENCY_VOSTRO1 = r20_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR20_AMOUNT_DEMAND_VOSTRO1() {
		return R20_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR20_AMOUNT_DEMAND_VOSTRO1(BigDecimal r20_AMOUNT_DEMAND_VOSTRO1) {
		R20_AMOUNT_DEMAND_VOSTRO1 = r20_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR20_AMOUNT_TIME_VOSTRO1() {
		return R20_AMOUNT_TIME_VOSTRO1;
	}
	public void setR20_AMOUNT_TIME_VOSTRO1(BigDecimal r20_AMOUNT_TIME_VOSTRO1) {
		R20_AMOUNT_TIME_VOSTRO1 = r20_AMOUNT_TIME_VOSTRO1;
	}
	public String getR21_NAME_OF_BANK_VOSTRO1() {
		return R21_NAME_OF_BANK_VOSTRO1;
	}
	public void setR21_NAME_OF_BANK_VOSTRO1(String r21_NAME_OF_BANK_VOSTRO1) {
		R21_NAME_OF_BANK_VOSTRO1 = r21_NAME_OF_BANK_VOSTRO1;
	}
	public String getR21_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R21_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR21_TYPE_OF_ACCOUNT_VOSTRO1(String r21_TYPE_OF_ACCOUNT_VOSTRO1) {
		R21_TYPE_OF_ACCOUNT_VOSTRO1 = r21_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR21_PURPOSE_VOSTRO1() {
		return R21_PURPOSE_VOSTRO1;
	}
	public void setR21_PURPOSE_VOSTRO1(String r21_PURPOSE_VOSTRO1) {
		R21_PURPOSE_VOSTRO1 = r21_PURPOSE_VOSTRO1;
	}
	public String getR21_CURRENCY_VOSTRO1() {
		return R21_CURRENCY_VOSTRO1;
	}
	public void setR21_CURRENCY_VOSTRO1(String r21_CURRENCY_VOSTRO1) {
		R21_CURRENCY_VOSTRO1 = r21_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR21_AMOUNT_DEMAND_VOSTRO1() {
		return R21_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR21_AMOUNT_DEMAND_VOSTRO1(BigDecimal r21_AMOUNT_DEMAND_VOSTRO1) {
		R21_AMOUNT_DEMAND_VOSTRO1 = r21_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR21_AMOUNT_TIME_VOSTRO1() {
		return R21_AMOUNT_TIME_VOSTRO1;
	}
	public void setR21_AMOUNT_TIME_VOSTRO1(BigDecimal r21_AMOUNT_TIME_VOSTRO1) {
		R21_AMOUNT_TIME_VOSTRO1 = r21_AMOUNT_TIME_VOSTRO1;
	}
	public String getR22_NAME_OF_BANK_VOSTRO1() {
		return R22_NAME_OF_BANK_VOSTRO1;
	}
	public void setR22_NAME_OF_BANK_VOSTRO1(String r22_NAME_OF_BANK_VOSTRO1) {
		R22_NAME_OF_BANK_VOSTRO1 = r22_NAME_OF_BANK_VOSTRO1;
	}
	public String getR22_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R22_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR22_TYPE_OF_ACCOUNT_VOSTRO1(String r22_TYPE_OF_ACCOUNT_VOSTRO1) {
		R22_TYPE_OF_ACCOUNT_VOSTRO1 = r22_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR22_PURPOSE_VOSTRO1() {
		return R22_PURPOSE_VOSTRO1;
	}
	public void setR22_PURPOSE_VOSTRO1(String r22_PURPOSE_VOSTRO1) {
		R22_PURPOSE_VOSTRO1 = r22_PURPOSE_VOSTRO1;
	}
	public String getR22_CURRENCY_VOSTRO1() {
		return R22_CURRENCY_VOSTRO1;
	}
	public void setR22_CURRENCY_VOSTRO1(String r22_CURRENCY_VOSTRO1) {
		R22_CURRENCY_VOSTRO1 = r22_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR22_AMOUNT_DEMAND_VOSTRO1() {
		return R22_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR22_AMOUNT_DEMAND_VOSTRO1(BigDecimal r22_AMOUNT_DEMAND_VOSTRO1) {
		R22_AMOUNT_DEMAND_VOSTRO1 = r22_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR22_AMOUNT_TIME_VOSTRO1() {
		return R22_AMOUNT_TIME_VOSTRO1;
	}
	public void setR22_AMOUNT_TIME_VOSTRO1(BigDecimal r22_AMOUNT_TIME_VOSTRO1) {
		R22_AMOUNT_TIME_VOSTRO1 = r22_AMOUNT_TIME_VOSTRO1;
	}
	public String getR23_NAME_OF_BANK_VOSTRO1() {
		return R23_NAME_OF_BANK_VOSTRO1;
	}
	public void setR23_NAME_OF_BANK_VOSTRO1(String r23_NAME_OF_BANK_VOSTRO1) {
		R23_NAME_OF_BANK_VOSTRO1 = r23_NAME_OF_BANK_VOSTRO1;
	}
	public String getR23_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R23_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR23_TYPE_OF_ACCOUNT_VOSTRO1(String r23_TYPE_OF_ACCOUNT_VOSTRO1) {
		R23_TYPE_OF_ACCOUNT_VOSTRO1 = r23_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR23_PURPOSE_VOSTRO1() {
		return R23_PURPOSE_VOSTRO1;
	}
	public void setR23_PURPOSE_VOSTRO1(String r23_PURPOSE_VOSTRO1) {
		R23_PURPOSE_VOSTRO1 = r23_PURPOSE_VOSTRO1;
	}
	public String getR23_CURRENCY_VOSTRO1() {
		return R23_CURRENCY_VOSTRO1;
	}
	public void setR23_CURRENCY_VOSTRO1(String r23_CURRENCY_VOSTRO1) {
		R23_CURRENCY_VOSTRO1 = r23_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR23_AMOUNT_DEMAND_VOSTRO1() {
		return R23_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR23_AMOUNT_DEMAND_VOSTRO1(BigDecimal r23_AMOUNT_DEMAND_VOSTRO1) {
		R23_AMOUNT_DEMAND_VOSTRO1 = r23_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR23_AMOUNT_TIME_VOSTRO1() {
		return R23_AMOUNT_TIME_VOSTRO1;
	}
	public void setR23_AMOUNT_TIME_VOSTRO1(BigDecimal r23_AMOUNT_TIME_VOSTRO1) {
		R23_AMOUNT_TIME_VOSTRO1 = r23_AMOUNT_TIME_VOSTRO1;
	}
	public String getR24_NAME_OF_BANK_VOSTRO1() {
		return R24_NAME_OF_BANK_VOSTRO1;
	}
	public void setR24_NAME_OF_BANK_VOSTRO1(String r24_NAME_OF_BANK_VOSTRO1) {
		R24_NAME_OF_BANK_VOSTRO1 = r24_NAME_OF_BANK_VOSTRO1;
	}
	public String getR24_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R24_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR24_TYPE_OF_ACCOUNT_VOSTRO1(String r24_TYPE_OF_ACCOUNT_VOSTRO1) {
		R24_TYPE_OF_ACCOUNT_VOSTRO1 = r24_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR24_PURPOSE_VOSTRO1() {
		return R24_PURPOSE_VOSTRO1;
	}
	public void setR24_PURPOSE_VOSTRO1(String r24_PURPOSE_VOSTRO1) {
		R24_PURPOSE_VOSTRO1 = r24_PURPOSE_VOSTRO1;
	}
	public String getR24_CURRENCY_VOSTRO1() {
		return R24_CURRENCY_VOSTRO1;
	}
	public void setR24_CURRENCY_VOSTRO1(String r24_CURRENCY_VOSTRO1) {
		R24_CURRENCY_VOSTRO1 = r24_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR24_AMOUNT_DEMAND_VOSTRO1() {
		return R24_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR24_AMOUNT_DEMAND_VOSTRO1(BigDecimal r24_AMOUNT_DEMAND_VOSTRO1) {
		R24_AMOUNT_DEMAND_VOSTRO1 = r24_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR24_AMOUNT_TIME_VOSTRO1() {
		return R24_AMOUNT_TIME_VOSTRO1;
	}
	public void setR24_AMOUNT_TIME_VOSTRO1(BigDecimal r24_AMOUNT_TIME_VOSTRO1) {
		R24_AMOUNT_TIME_VOSTRO1 = r24_AMOUNT_TIME_VOSTRO1;
	}
	public String getR25_NAME_OF_BANK_VOSTRO1() {
		return R25_NAME_OF_BANK_VOSTRO1;
	}
	public void setR25_NAME_OF_BANK_VOSTRO1(String r25_NAME_OF_BANK_VOSTRO1) {
		R25_NAME_OF_BANK_VOSTRO1 = r25_NAME_OF_BANK_VOSTRO1;
	}
	public String getR25_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R25_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR25_TYPE_OF_ACCOUNT_VOSTRO1(String r25_TYPE_OF_ACCOUNT_VOSTRO1) {
		R25_TYPE_OF_ACCOUNT_VOSTRO1 = r25_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR25_PURPOSE_VOSTRO1() {
		return R25_PURPOSE_VOSTRO1;
	}
	public void setR25_PURPOSE_VOSTRO1(String r25_PURPOSE_VOSTRO1) {
		R25_PURPOSE_VOSTRO1 = r25_PURPOSE_VOSTRO1;
	}
	public String getR25_CURRENCY_VOSTRO1() {
		return R25_CURRENCY_VOSTRO1;
	}
	public void setR25_CURRENCY_VOSTRO1(String r25_CURRENCY_VOSTRO1) {
		R25_CURRENCY_VOSTRO1 = r25_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR25_AMOUNT_DEMAND_VOSTRO1() {
		return R25_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR25_AMOUNT_DEMAND_VOSTRO1(BigDecimal r25_AMOUNT_DEMAND_VOSTRO1) {
		R25_AMOUNT_DEMAND_VOSTRO1 = r25_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR25_AMOUNT_TIME_VOSTRO1() {
		return R25_AMOUNT_TIME_VOSTRO1;
	}
	public void setR25_AMOUNT_TIME_VOSTRO1(BigDecimal r25_AMOUNT_TIME_VOSTRO1) {
		R25_AMOUNT_TIME_VOSTRO1 = r25_AMOUNT_TIME_VOSTRO1;
	}
	public String getR26_NAME_OF_BANK_VOSTRO1() {
		return R26_NAME_OF_BANK_VOSTRO1;
	}
	public void setR26_NAME_OF_BANK_VOSTRO1(String r26_NAME_OF_BANK_VOSTRO1) {
		R26_NAME_OF_BANK_VOSTRO1 = r26_NAME_OF_BANK_VOSTRO1;
	}
	public String getR26_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R26_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR26_TYPE_OF_ACCOUNT_VOSTRO1(String r26_TYPE_OF_ACCOUNT_VOSTRO1) {
		R26_TYPE_OF_ACCOUNT_VOSTRO1 = r26_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR26_PURPOSE_VOSTRO1() {
		return R26_PURPOSE_VOSTRO1;
	}
	public void setR26_PURPOSE_VOSTRO1(String r26_PURPOSE_VOSTRO1) {
		R26_PURPOSE_VOSTRO1 = r26_PURPOSE_VOSTRO1;
	}
	public String getR26_CURRENCY_VOSTRO1() {
		return R26_CURRENCY_VOSTRO1;
	}
	public void setR26_CURRENCY_VOSTRO1(String r26_CURRENCY_VOSTRO1) {
		R26_CURRENCY_VOSTRO1 = r26_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR26_AMOUNT_DEMAND_VOSTRO1() {
		return R26_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR26_AMOUNT_DEMAND_VOSTRO1(BigDecimal r26_AMOUNT_DEMAND_VOSTRO1) {
		R26_AMOUNT_DEMAND_VOSTRO1 = r26_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR26_AMOUNT_TIME_VOSTRO1() {
		return R26_AMOUNT_TIME_VOSTRO1;
	}
	public void setR26_AMOUNT_TIME_VOSTRO1(BigDecimal r26_AMOUNT_TIME_VOSTRO1) {
		R26_AMOUNT_TIME_VOSTRO1 = r26_AMOUNT_TIME_VOSTRO1;
	}
	public String getR27_NAME_OF_BANK_VOSTRO1() {
		return R27_NAME_OF_BANK_VOSTRO1;
	}
	public void setR27_NAME_OF_BANK_VOSTRO1(String r27_NAME_OF_BANK_VOSTRO1) {
		R27_NAME_OF_BANK_VOSTRO1 = r27_NAME_OF_BANK_VOSTRO1;
	}
	public String getR27_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R27_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR27_TYPE_OF_ACCOUNT_VOSTRO1(String r27_TYPE_OF_ACCOUNT_VOSTRO1) {
		R27_TYPE_OF_ACCOUNT_VOSTRO1 = r27_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR27_PURPOSE_VOSTRO1() {
		return R27_PURPOSE_VOSTRO1;
	}
	public void setR27_PURPOSE_VOSTRO1(String r27_PURPOSE_VOSTRO1) {
		R27_PURPOSE_VOSTRO1 = r27_PURPOSE_VOSTRO1;
	}
	public String getR27_CURRENCY_VOSTRO1() {
		return R27_CURRENCY_VOSTRO1;
	}
	public void setR27_CURRENCY_VOSTRO1(String r27_CURRENCY_VOSTRO1) {
		R27_CURRENCY_VOSTRO1 = r27_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR27_AMOUNT_DEMAND_VOSTRO1() {
		return R27_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR27_AMOUNT_DEMAND_VOSTRO1(BigDecimal r27_AMOUNT_DEMAND_VOSTRO1) {
		R27_AMOUNT_DEMAND_VOSTRO1 = r27_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR27_AMOUNT_TIME_VOSTRO1() {
		return R27_AMOUNT_TIME_VOSTRO1;
	}
	public void setR27_AMOUNT_TIME_VOSTRO1(BigDecimal r27_AMOUNT_TIME_VOSTRO1) {
		R27_AMOUNT_TIME_VOSTRO1 = r27_AMOUNT_TIME_VOSTRO1;
	}
	public String getR28_NAME_OF_BANK_VOSTRO1() {
		return R28_NAME_OF_BANK_VOSTRO1;
	}
	public void setR28_NAME_OF_BANK_VOSTRO1(String r28_NAME_OF_BANK_VOSTRO1) {
		R28_NAME_OF_BANK_VOSTRO1 = r28_NAME_OF_BANK_VOSTRO1;
	}
	public String getR28_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R28_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR28_TYPE_OF_ACCOUNT_VOSTRO1(String r28_TYPE_OF_ACCOUNT_VOSTRO1) {
		R28_TYPE_OF_ACCOUNT_VOSTRO1 = r28_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR28_PURPOSE_VOSTRO1() {
		return R28_PURPOSE_VOSTRO1;
	}
	public void setR28_PURPOSE_VOSTRO1(String r28_PURPOSE_VOSTRO1) {
		R28_PURPOSE_VOSTRO1 = r28_PURPOSE_VOSTRO1;
	}
	public String getR28_CURRENCY_VOSTRO1() {
		return R28_CURRENCY_VOSTRO1;
	}
	public void setR28_CURRENCY_VOSTRO1(String r28_CURRENCY_VOSTRO1) {
		R28_CURRENCY_VOSTRO1 = r28_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR28_AMOUNT_DEMAND_VOSTRO1() {
		return R28_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR28_AMOUNT_DEMAND_VOSTRO1(BigDecimal r28_AMOUNT_DEMAND_VOSTRO1) {
		R28_AMOUNT_DEMAND_VOSTRO1 = r28_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR28_AMOUNT_TIME_VOSTRO1() {
		return R28_AMOUNT_TIME_VOSTRO1;
	}
	public void setR28_AMOUNT_TIME_VOSTRO1(BigDecimal r28_AMOUNT_TIME_VOSTRO1) {
		R28_AMOUNT_TIME_VOSTRO1 = r28_AMOUNT_TIME_VOSTRO1;
	}
	public String getR29_NAME_OF_BANK_VOSTRO1() {
		return R29_NAME_OF_BANK_VOSTRO1;
	}
	public void setR29_NAME_OF_BANK_VOSTRO1(String r29_NAME_OF_BANK_VOSTRO1) {
		R29_NAME_OF_BANK_VOSTRO1 = r29_NAME_OF_BANK_VOSTRO1;
	}
	public String getR29_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R29_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR29_TYPE_OF_ACCOUNT_VOSTRO1(String r29_TYPE_OF_ACCOUNT_VOSTRO1) {
		R29_TYPE_OF_ACCOUNT_VOSTRO1 = r29_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR29_PURPOSE_VOSTRO1() {
		return R29_PURPOSE_VOSTRO1;
	}
	public void setR29_PURPOSE_VOSTRO1(String r29_PURPOSE_VOSTRO1) {
		R29_PURPOSE_VOSTRO1 = r29_PURPOSE_VOSTRO1;
	}
	public String getR29_CURRENCY_VOSTRO1() {
		return R29_CURRENCY_VOSTRO1;
	}
	public void setR29_CURRENCY_VOSTRO1(String r29_CURRENCY_VOSTRO1) {
		R29_CURRENCY_VOSTRO1 = r29_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR29_AMOUNT_DEMAND_VOSTRO1() {
		return R29_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR29_AMOUNT_DEMAND_VOSTRO1(BigDecimal r29_AMOUNT_DEMAND_VOSTRO1) {
		R29_AMOUNT_DEMAND_VOSTRO1 = r29_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR29_AMOUNT_TIME_VOSTRO1() {
		return R29_AMOUNT_TIME_VOSTRO1;
	}
	public void setR29_AMOUNT_TIME_VOSTRO1(BigDecimal r29_AMOUNT_TIME_VOSTRO1) {
		R29_AMOUNT_TIME_VOSTRO1 = r29_AMOUNT_TIME_VOSTRO1;
	}
	public String getR30_NAME_OF_BANK_VOSTRO1() {
		return R30_NAME_OF_BANK_VOSTRO1;
	}
	public void setR30_NAME_OF_BANK_VOSTRO1(String r30_NAME_OF_BANK_VOSTRO1) {
		R30_NAME_OF_BANK_VOSTRO1 = r30_NAME_OF_BANK_VOSTRO1;
	}
	public String getR30_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R30_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR30_TYPE_OF_ACCOUNT_VOSTRO1(String r30_TYPE_OF_ACCOUNT_VOSTRO1) {
		R30_TYPE_OF_ACCOUNT_VOSTRO1 = r30_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR30_PURPOSE_VOSTRO1() {
		return R30_PURPOSE_VOSTRO1;
	}
	public void setR30_PURPOSE_VOSTRO1(String r30_PURPOSE_VOSTRO1) {
		R30_PURPOSE_VOSTRO1 = r30_PURPOSE_VOSTRO1;
	}
	public String getR30_CURRENCY_VOSTRO1() {
		return R30_CURRENCY_VOSTRO1;
	}
	public void setR30_CURRENCY_VOSTRO1(String r30_CURRENCY_VOSTRO1) {
		R30_CURRENCY_VOSTRO1 = r30_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR30_AMOUNT_DEMAND_VOSTRO1() {
		return R30_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR30_AMOUNT_DEMAND_VOSTRO1(BigDecimal r30_AMOUNT_DEMAND_VOSTRO1) {
		R30_AMOUNT_DEMAND_VOSTRO1 = r30_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR30_AMOUNT_TIME_VOSTRO1() {
		return R30_AMOUNT_TIME_VOSTRO1;
	}
	public void setR30_AMOUNT_TIME_VOSTRO1(BigDecimal r30_AMOUNT_TIME_VOSTRO1) {
		R30_AMOUNT_TIME_VOSTRO1 = r30_AMOUNT_TIME_VOSTRO1;
	}
	public String getR31_NAME_OF_BANK_VOSTRO1() {
		return R31_NAME_OF_BANK_VOSTRO1;
	}
	public void setR31_NAME_OF_BANK_VOSTRO1(String r31_NAME_OF_BANK_VOSTRO1) {
		R31_NAME_OF_BANK_VOSTRO1 = r31_NAME_OF_BANK_VOSTRO1;
	}
	public String getR31_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R31_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR31_TYPE_OF_ACCOUNT_VOSTRO1(String r31_TYPE_OF_ACCOUNT_VOSTRO1) {
		R31_TYPE_OF_ACCOUNT_VOSTRO1 = r31_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR31_PURPOSE_VOSTRO1() {
		return R31_PURPOSE_VOSTRO1;
	}
	public void setR31_PURPOSE_VOSTRO1(String r31_PURPOSE_VOSTRO1) {
		R31_PURPOSE_VOSTRO1 = r31_PURPOSE_VOSTRO1;
	}
	public String getR31_CURRENCY_VOSTRO1() {
		return R31_CURRENCY_VOSTRO1;
	}
	public void setR31_CURRENCY_VOSTRO1(String r31_CURRENCY_VOSTRO1) {
		R31_CURRENCY_VOSTRO1 = r31_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR31_AMOUNT_DEMAND_VOSTRO1() {
		return R31_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR31_AMOUNT_DEMAND_VOSTRO1(BigDecimal r31_AMOUNT_DEMAND_VOSTRO1) {
		R31_AMOUNT_DEMAND_VOSTRO1 = r31_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR31_AMOUNT_TIME_VOSTRO1() {
		return R31_AMOUNT_TIME_VOSTRO1;
	}
	public void setR31_AMOUNT_TIME_VOSTRO1(BigDecimal r31_AMOUNT_TIME_VOSTRO1) {
		R31_AMOUNT_TIME_VOSTRO1 = r31_AMOUNT_TIME_VOSTRO1;
	}
	public String getR32_NAME_OF_BANK_VOSTRO1() {
		return R32_NAME_OF_BANK_VOSTRO1;
	}
	public void setR32_NAME_OF_BANK_VOSTRO1(String r32_NAME_OF_BANK_VOSTRO1) {
		R32_NAME_OF_BANK_VOSTRO1 = r32_NAME_OF_BANK_VOSTRO1;
	}
	public String getR32_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R32_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR32_TYPE_OF_ACCOUNT_VOSTRO1(String r32_TYPE_OF_ACCOUNT_VOSTRO1) {
		R32_TYPE_OF_ACCOUNT_VOSTRO1 = r32_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR32_PURPOSE_VOSTRO1() {
		return R32_PURPOSE_VOSTRO1;
	}
	public void setR32_PURPOSE_VOSTRO1(String r32_PURPOSE_VOSTRO1) {
		R32_PURPOSE_VOSTRO1 = r32_PURPOSE_VOSTRO1;
	}
	public String getR32_CURRENCY_VOSTRO1() {
		return R32_CURRENCY_VOSTRO1;
	}
	public void setR32_CURRENCY_VOSTRO1(String r32_CURRENCY_VOSTRO1) {
		R32_CURRENCY_VOSTRO1 = r32_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR32_AMOUNT_DEMAND_VOSTRO1() {
		return R32_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR32_AMOUNT_DEMAND_VOSTRO1(BigDecimal r32_AMOUNT_DEMAND_VOSTRO1) {
		R32_AMOUNT_DEMAND_VOSTRO1 = r32_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR32_AMOUNT_TIME_VOSTRO1() {
		return R32_AMOUNT_TIME_VOSTRO1;
	}
	public void setR32_AMOUNT_TIME_VOSTRO1(BigDecimal r32_AMOUNT_TIME_VOSTRO1) {
		R32_AMOUNT_TIME_VOSTRO1 = r32_AMOUNT_TIME_VOSTRO1;
	}
	public String getR33_NAME_OF_BANK_VOSTRO1() {
		return R33_NAME_OF_BANK_VOSTRO1;
	}
	public void setR33_NAME_OF_BANK_VOSTRO1(String r33_NAME_OF_BANK_VOSTRO1) {
		R33_NAME_OF_BANK_VOSTRO1 = r33_NAME_OF_BANK_VOSTRO1;
	}
	public String getR33_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R33_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR33_TYPE_OF_ACCOUNT_VOSTRO1(String r33_TYPE_OF_ACCOUNT_VOSTRO1) {
		R33_TYPE_OF_ACCOUNT_VOSTRO1 = r33_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR33_PURPOSE_VOSTRO1() {
		return R33_PURPOSE_VOSTRO1;
	}
	public void setR33_PURPOSE_VOSTRO1(String r33_PURPOSE_VOSTRO1) {
		R33_PURPOSE_VOSTRO1 = r33_PURPOSE_VOSTRO1;
	}
	public String getR33_CURRENCY_VOSTRO1() {
		return R33_CURRENCY_VOSTRO1;
	}
	public void setR33_CURRENCY_VOSTRO1(String r33_CURRENCY_VOSTRO1) {
		R33_CURRENCY_VOSTRO1 = r33_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR33_AMOUNT_DEMAND_VOSTRO1() {
		return R33_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR33_AMOUNT_DEMAND_VOSTRO1(BigDecimal r33_AMOUNT_DEMAND_VOSTRO1) {
		R33_AMOUNT_DEMAND_VOSTRO1 = r33_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR33_AMOUNT_TIME_VOSTRO1() {
		return R33_AMOUNT_TIME_VOSTRO1;
	}
	public void setR33_AMOUNT_TIME_VOSTRO1(BigDecimal r33_AMOUNT_TIME_VOSTRO1) {
		R33_AMOUNT_TIME_VOSTRO1 = r33_AMOUNT_TIME_VOSTRO1;
	}
	public String getR34_NAME_OF_BANK_VOSTRO1() {
		return R34_NAME_OF_BANK_VOSTRO1;
	}
	public void setR34_NAME_OF_BANK_VOSTRO1(String r34_NAME_OF_BANK_VOSTRO1) {
		R34_NAME_OF_BANK_VOSTRO1 = r34_NAME_OF_BANK_VOSTRO1;
	}
	public String getR34_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R34_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR34_TYPE_OF_ACCOUNT_VOSTRO1(String r34_TYPE_OF_ACCOUNT_VOSTRO1) {
		R34_TYPE_OF_ACCOUNT_VOSTRO1 = r34_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR34_PURPOSE_VOSTRO1() {
		return R34_PURPOSE_VOSTRO1;
	}
	public void setR34_PURPOSE_VOSTRO1(String r34_PURPOSE_VOSTRO1) {
		R34_PURPOSE_VOSTRO1 = r34_PURPOSE_VOSTRO1;
	}
	public String getR34_CURRENCY_VOSTRO1() {
		return R34_CURRENCY_VOSTRO1;
	}
	public void setR34_CURRENCY_VOSTRO1(String r34_CURRENCY_VOSTRO1) {
		R34_CURRENCY_VOSTRO1 = r34_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR34_AMOUNT_DEMAND_VOSTRO1() {
		return R34_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR34_AMOUNT_DEMAND_VOSTRO1(BigDecimal r34_AMOUNT_DEMAND_VOSTRO1) {
		R34_AMOUNT_DEMAND_VOSTRO1 = r34_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR34_AMOUNT_TIME_VOSTRO1() {
		return R34_AMOUNT_TIME_VOSTRO1;
	}
	public void setR34_AMOUNT_TIME_VOSTRO1(BigDecimal r34_AMOUNT_TIME_VOSTRO1) {
		R34_AMOUNT_TIME_VOSTRO1 = r34_AMOUNT_TIME_VOSTRO1;
	}
	public String getR35_NAME_OF_BANK_VOSTRO1() {
		return R35_NAME_OF_BANK_VOSTRO1;
	}
	public void setR35_NAME_OF_BANK_VOSTRO1(String r35_NAME_OF_BANK_VOSTRO1) {
		R35_NAME_OF_BANK_VOSTRO1 = r35_NAME_OF_BANK_VOSTRO1;
	}
	public String getR35_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R35_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR35_TYPE_OF_ACCOUNT_VOSTRO1(String r35_TYPE_OF_ACCOUNT_VOSTRO1) {
		R35_TYPE_OF_ACCOUNT_VOSTRO1 = r35_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR35_PURPOSE_VOSTRO1() {
		return R35_PURPOSE_VOSTRO1;
	}
	public void setR35_PURPOSE_VOSTRO1(String r35_PURPOSE_VOSTRO1) {
		R35_PURPOSE_VOSTRO1 = r35_PURPOSE_VOSTRO1;
	}
	public String getR35_CURRENCY_VOSTRO1() {
		return R35_CURRENCY_VOSTRO1;
	}
	public void setR35_CURRENCY_VOSTRO1(String r35_CURRENCY_VOSTRO1) {
		R35_CURRENCY_VOSTRO1 = r35_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR35_AMOUNT_DEMAND_VOSTRO1() {
		return R35_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR35_AMOUNT_DEMAND_VOSTRO1(BigDecimal r35_AMOUNT_DEMAND_VOSTRO1) {
		R35_AMOUNT_DEMAND_VOSTRO1 = r35_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR35_AMOUNT_TIME_VOSTRO1() {
		return R35_AMOUNT_TIME_VOSTRO1;
	}
	public void setR35_AMOUNT_TIME_VOSTRO1(BigDecimal r35_AMOUNT_TIME_VOSTRO1) {
		R35_AMOUNT_TIME_VOSTRO1 = r35_AMOUNT_TIME_VOSTRO1;
	}
	public String getR36_NAME_OF_BANK_VOSTRO1() {
		return R36_NAME_OF_BANK_VOSTRO1;
	}
	public void setR36_NAME_OF_BANK_VOSTRO1(String r36_NAME_OF_BANK_VOSTRO1) {
		R36_NAME_OF_BANK_VOSTRO1 = r36_NAME_OF_BANK_VOSTRO1;
	}
	public String getR36_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R36_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR36_TYPE_OF_ACCOUNT_VOSTRO1(String r36_TYPE_OF_ACCOUNT_VOSTRO1) {
		R36_TYPE_OF_ACCOUNT_VOSTRO1 = r36_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR36_PURPOSE_VOSTRO1() {
		return R36_PURPOSE_VOSTRO1;
	}
	public void setR36_PURPOSE_VOSTRO1(String r36_PURPOSE_VOSTRO1) {
		R36_PURPOSE_VOSTRO1 = r36_PURPOSE_VOSTRO1;
	}
	public String getR36_CURRENCY_VOSTRO1() {
		return R36_CURRENCY_VOSTRO1;
	}
	public void setR36_CURRENCY_VOSTRO1(String r36_CURRENCY_VOSTRO1) {
		R36_CURRENCY_VOSTRO1 = r36_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR36_AMOUNT_DEMAND_VOSTRO1() {
		return R36_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR36_AMOUNT_DEMAND_VOSTRO1(BigDecimal r36_AMOUNT_DEMAND_VOSTRO1) {
		R36_AMOUNT_DEMAND_VOSTRO1 = r36_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR36_AMOUNT_TIME_VOSTRO1() {
		return R36_AMOUNT_TIME_VOSTRO1;
	}
	public void setR36_AMOUNT_TIME_VOSTRO1(BigDecimal r36_AMOUNT_TIME_VOSTRO1) {
		R36_AMOUNT_TIME_VOSTRO1 = r36_AMOUNT_TIME_VOSTRO1;
	}
	public String getR37_NAME_OF_BANK_VOSTRO1() {
		return R37_NAME_OF_BANK_VOSTRO1;
	}
	public void setR37_NAME_OF_BANK_VOSTRO1(String r37_NAME_OF_BANK_VOSTRO1) {
		R37_NAME_OF_BANK_VOSTRO1 = r37_NAME_OF_BANK_VOSTRO1;
	}
	public String getR37_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R37_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR37_TYPE_OF_ACCOUNT_VOSTRO1(String r37_TYPE_OF_ACCOUNT_VOSTRO1) {
		R37_TYPE_OF_ACCOUNT_VOSTRO1 = r37_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR37_PURPOSE_VOSTRO1() {
		return R37_PURPOSE_VOSTRO1;
	}
	public void setR37_PURPOSE_VOSTRO1(String r37_PURPOSE_VOSTRO1) {
		R37_PURPOSE_VOSTRO1 = r37_PURPOSE_VOSTRO1;
	}
	public String getR37_CURRENCY_VOSTRO1() {
		return R37_CURRENCY_VOSTRO1;
	}
	public void setR37_CURRENCY_VOSTRO1(String r37_CURRENCY_VOSTRO1) {
		R37_CURRENCY_VOSTRO1 = r37_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR37_AMOUNT_DEMAND_VOSTRO1() {
		return R37_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR37_AMOUNT_DEMAND_VOSTRO1(BigDecimal r37_AMOUNT_DEMAND_VOSTRO1) {
		R37_AMOUNT_DEMAND_VOSTRO1 = r37_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR37_AMOUNT_TIME_VOSTRO1() {
		return R37_AMOUNT_TIME_VOSTRO1;
	}
	public void setR37_AMOUNT_TIME_VOSTRO1(BigDecimal r37_AMOUNT_TIME_VOSTRO1) {
		R37_AMOUNT_TIME_VOSTRO1 = r37_AMOUNT_TIME_VOSTRO1;
	}
	public String getR38_NAME_OF_BANK_VOSTRO1() {
		return R38_NAME_OF_BANK_VOSTRO1;
	}
	public void setR38_NAME_OF_BANK_VOSTRO1(String r38_NAME_OF_BANK_VOSTRO1) {
		R38_NAME_OF_BANK_VOSTRO1 = r38_NAME_OF_BANK_VOSTRO1;
	}
	public String getR38_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R38_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR38_TYPE_OF_ACCOUNT_VOSTRO1(String r38_TYPE_OF_ACCOUNT_VOSTRO1) {
		R38_TYPE_OF_ACCOUNT_VOSTRO1 = r38_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR38_PURPOSE_VOSTRO1() {
		return R38_PURPOSE_VOSTRO1;
	}
	public void setR38_PURPOSE_VOSTRO1(String r38_PURPOSE_VOSTRO1) {
		R38_PURPOSE_VOSTRO1 = r38_PURPOSE_VOSTRO1;
	}
	public String getR38_CURRENCY_VOSTRO1() {
		return R38_CURRENCY_VOSTRO1;
	}
	public void setR38_CURRENCY_VOSTRO1(String r38_CURRENCY_VOSTRO1) {
		R38_CURRENCY_VOSTRO1 = r38_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR38_AMOUNT_DEMAND_VOSTRO1() {
		return R38_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR38_AMOUNT_DEMAND_VOSTRO1(BigDecimal r38_AMOUNT_DEMAND_VOSTRO1) {
		R38_AMOUNT_DEMAND_VOSTRO1 = r38_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR38_AMOUNT_TIME_VOSTRO1() {
		return R38_AMOUNT_TIME_VOSTRO1;
	}
	public void setR38_AMOUNT_TIME_VOSTRO1(BigDecimal r38_AMOUNT_TIME_VOSTRO1) {
		R38_AMOUNT_TIME_VOSTRO1 = r38_AMOUNT_TIME_VOSTRO1;
	}
	public String getR39_NAME_OF_BANK_VOSTRO1() {
		return R39_NAME_OF_BANK_VOSTRO1;
	}
	public void setR39_NAME_OF_BANK_VOSTRO1(String r39_NAME_OF_BANK_VOSTRO1) {
		R39_NAME_OF_BANK_VOSTRO1 = r39_NAME_OF_BANK_VOSTRO1;
	}
	public String getR39_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R39_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR39_TYPE_OF_ACCOUNT_VOSTRO1(String r39_TYPE_OF_ACCOUNT_VOSTRO1) {
		R39_TYPE_OF_ACCOUNT_VOSTRO1 = r39_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR39_PURPOSE_VOSTRO1() {
		return R39_PURPOSE_VOSTRO1;
	}
	public void setR39_PURPOSE_VOSTRO1(String r39_PURPOSE_VOSTRO1) {
		R39_PURPOSE_VOSTRO1 = r39_PURPOSE_VOSTRO1;
	}
	public String getR39_CURRENCY_VOSTRO1() {
		return R39_CURRENCY_VOSTRO1;
	}
	public void setR39_CURRENCY_VOSTRO1(String r39_CURRENCY_VOSTRO1) {
		R39_CURRENCY_VOSTRO1 = r39_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR39_AMOUNT_DEMAND_VOSTRO1() {
		return R39_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR39_AMOUNT_DEMAND_VOSTRO1(BigDecimal r39_AMOUNT_DEMAND_VOSTRO1) {
		R39_AMOUNT_DEMAND_VOSTRO1 = r39_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR39_AMOUNT_TIME_VOSTRO1() {
		return R39_AMOUNT_TIME_VOSTRO1;
	}
	public void setR39_AMOUNT_TIME_VOSTRO1(BigDecimal r39_AMOUNT_TIME_VOSTRO1) {
		R39_AMOUNT_TIME_VOSTRO1 = r39_AMOUNT_TIME_VOSTRO1;
	}
	public String getR40_NAME_OF_BANK_VOSTRO1() {
		return R40_NAME_OF_BANK_VOSTRO1;
	}
	public void setR40_NAME_OF_BANK_VOSTRO1(String r40_NAME_OF_BANK_VOSTRO1) {
		R40_NAME_OF_BANK_VOSTRO1 = r40_NAME_OF_BANK_VOSTRO1;
	}
	public String getR40_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R40_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR40_TYPE_OF_ACCOUNT_VOSTRO1(String r40_TYPE_OF_ACCOUNT_VOSTRO1) {
		R40_TYPE_OF_ACCOUNT_VOSTRO1 = r40_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR40_PURPOSE_VOSTRO1() {
		return R40_PURPOSE_VOSTRO1;
	}
	public void setR40_PURPOSE_VOSTRO1(String r40_PURPOSE_VOSTRO1) {
		R40_PURPOSE_VOSTRO1 = r40_PURPOSE_VOSTRO1;
	}
	public String getR40_CURRENCY_VOSTRO1() {
		return R40_CURRENCY_VOSTRO1;
	}
	public void setR40_CURRENCY_VOSTRO1(String r40_CURRENCY_VOSTRO1) {
		R40_CURRENCY_VOSTRO1 = r40_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR40_AMOUNT_DEMAND_VOSTRO1() {
		return R40_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR40_AMOUNT_DEMAND_VOSTRO1(BigDecimal r40_AMOUNT_DEMAND_VOSTRO1) {
		R40_AMOUNT_DEMAND_VOSTRO1 = r40_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR40_AMOUNT_TIME_VOSTRO1() {
		return R40_AMOUNT_TIME_VOSTRO1;
	}
	public void setR40_AMOUNT_TIME_VOSTRO1(BigDecimal r40_AMOUNT_TIME_VOSTRO1) {
		R40_AMOUNT_TIME_VOSTRO1 = r40_AMOUNT_TIME_VOSTRO1;
	}
	public String getR41_NAME_OF_BANK_VOSTRO1() {
		return R41_NAME_OF_BANK_VOSTRO1;
	}
	public void setR41_NAME_OF_BANK_VOSTRO1(String r41_NAME_OF_BANK_VOSTRO1) {
		R41_NAME_OF_BANK_VOSTRO1 = r41_NAME_OF_BANK_VOSTRO1;
	}
	public String getR41_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R41_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR41_TYPE_OF_ACCOUNT_VOSTRO1(String r41_TYPE_OF_ACCOUNT_VOSTRO1) {
		R41_TYPE_OF_ACCOUNT_VOSTRO1 = r41_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR41_PURPOSE_VOSTRO1() {
		return R41_PURPOSE_VOSTRO1;
	}
	public void setR41_PURPOSE_VOSTRO1(String r41_PURPOSE_VOSTRO1) {
		R41_PURPOSE_VOSTRO1 = r41_PURPOSE_VOSTRO1;
	}
	public String getR41_CURRENCY_VOSTRO1() {
		return R41_CURRENCY_VOSTRO1;
	}
	public void setR41_CURRENCY_VOSTRO1(String r41_CURRENCY_VOSTRO1) {
		R41_CURRENCY_VOSTRO1 = r41_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR41_AMOUNT_DEMAND_VOSTRO1() {
		return R41_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR41_AMOUNT_DEMAND_VOSTRO1(BigDecimal r41_AMOUNT_DEMAND_VOSTRO1) {
		R41_AMOUNT_DEMAND_VOSTRO1 = r41_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR41_AMOUNT_TIME_VOSTRO1() {
		return R41_AMOUNT_TIME_VOSTRO1;
	}
	public void setR41_AMOUNT_TIME_VOSTRO1(BigDecimal r41_AMOUNT_TIME_VOSTRO1) {
		R41_AMOUNT_TIME_VOSTRO1 = r41_AMOUNT_TIME_VOSTRO1;
	}
	public String getR42_NAME_OF_BANK_VOSTRO1() {
		return R42_NAME_OF_BANK_VOSTRO1;
	}
	public void setR42_NAME_OF_BANK_VOSTRO1(String r42_NAME_OF_BANK_VOSTRO1) {
		R42_NAME_OF_BANK_VOSTRO1 = r42_NAME_OF_BANK_VOSTRO1;
	}
	public String getR42_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R42_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR42_TYPE_OF_ACCOUNT_VOSTRO1(String r42_TYPE_OF_ACCOUNT_VOSTRO1) {
		R42_TYPE_OF_ACCOUNT_VOSTRO1 = r42_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR42_PURPOSE_VOSTRO1() {
		return R42_PURPOSE_VOSTRO1;
	}
	public void setR42_PURPOSE_VOSTRO1(String r42_PURPOSE_VOSTRO1) {
		R42_PURPOSE_VOSTRO1 = r42_PURPOSE_VOSTRO1;
	}
	public String getR42_CURRENCY_VOSTRO1() {
		return R42_CURRENCY_VOSTRO1;
	}
	public void setR42_CURRENCY_VOSTRO1(String r42_CURRENCY_VOSTRO1) {
		R42_CURRENCY_VOSTRO1 = r42_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR42_AMOUNT_DEMAND_VOSTRO1() {
		return R42_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR42_AMOUNT_DEMAND_VOSTRO1(BigDecimal r42_AMOUNT_DEMAND_VOSTRO1) {
		R42_AMOUNT_DEMAND_VOSTRO1 = r42_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR42_AMOUNT_TIME_VOSTRO1() {
		return R42_AMOUNT_TIME_VOSTRO1;
	}
	public void setR42_AMOUNT_TIME_VOSTRO1(BigDecimal r42_AMOUNT_TIME_VOSTRO1) {
		R42_AMOUNT_TIME_VOSTRO1 = r42_AMOUNT_TIME_VOSTRO1;
	}
	public String getR43_NAME_OF_BANK_VOSTRO1() {
		return R43_NAME_OF_BANK_VOSTRO1;
	}
	public void setR43_NAME_OF_BANK_VOSTRO1(String r43_NAME_OF_BANK_VOSTRO1) {
		R43_NAME_OF_BANK_VOSTRO1 = r43_NAME_OF_BANK_VOSTRO1;
	}
	public String getR43_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R43_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR43_TYPE_OF_ACCOUNT_VOSTRO1(String r43_TYPE_OF_ACCOUNT_VOSTRO1) {
		R43_TYPE_OF_ACCOUNT_VOSTRO1 = r43_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR43_PURPOSE_VOSTRO1() {
		return R43_PURPOSE_VOSTRO1;
	}
	public void setR43_PURPOSE_VOSTRO1(String r43_PURPOSE_VOSTRO1) {
		R43_PURPOSE_VOSTRO1 = r43_PURPOSE_VOSTRO1;
	}
	public String getR43_CURRENCY_VOSTRO1() {
		return R43_CURRENCY_VOSTRO1;
	}
	public void setR43_CURRENCY_VOSTRO1(String r43_CURRENCY_VOSTRO1) {
		R43_CURRENCY_VOSTRO1 = r43_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR43_AMOUNT_DEMAND_VOSTRO1() {
		return R43_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR43_AMOUNT_DEMAND_VOSTRO1(BigDecimal r43_AMOUNT_DEMAND_VOSTRO1) {
		R43_AMOUNT_DEMAND_VOSTRO1 = r43_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR43_AMOUNT_TIME_VOSTRO1() {
		return R43_AMOUNT_TIME_VOSTRO1;
	}
	public void setR43_AMOUNT_TIME_VOSTRO1(BigDecimal r43_AMOUNT_TIME_VOSTRO1) {
		R43_AMOUNT_TIME_VOSTRO1 = r43_AMOUNT_TIME_VOSTRO1;
	}
	public String getR44_NAME_OF_BANK_VOSTRO1() {
		return R44_NAME_OF_BANK_VOSTRO1;
	}
	public void setR44_NAME_OF_BANK_VOSTRO1(String r44_NAME_OF_BANK_VOSTRO1) {
		R44_NAME_OF_BANK_VOSTRO1 = r44_NAME_OF_BANK_VOSTRO1;
	}
	public String getR44_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R44_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR44_TYPE_OF_ACCOUNT_VOSTRO1(String r44_TYPE_OF_ACCOUNT_VOSTRO1) {
		R44_TYPE_OF_ACCOUNT_VOSTRO1 = r44_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR44_PURPOSE_VOSTRO1() {
		return R44_PURPOSE_VOSTRO1;
	}
	public void setR44_PURPOSE_VOSTRO1(String r44_PURPOSE_VOSTRO1) {
		R44_PURPOSE_VOSTRO1 = r44_PURPOSE_VOSTRO1;
	}
	public String getR44_CURRENCY_VOSTRO1() {
		return R44_CURRENCY_VOSTRO1;
	}
	public void setR44_CURRENCY_VOSTRO1(String r44_CURRENCY_VOSTRO1) {
		R44_CURRENCY_VOSTRO1 = r44_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR44_AMOUNT_DEMAND_VOSTRO1() {
		return R44_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR44_AMOUNT_DEMAND_VOSTRO1(BigDecimal r44_AMOUNT_DEMAND_VOSTRO1) {
		R44_AMOUNT_DEMAND_VOSTRO1 = r44_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR44_AMOUNT_TIME_VOSTRO1() {
		return R44_AMOUNT_TIME_VOSTRO1;
	}
	public void setR44_AMOUNT_TIME_VOSTRO1(BigDecimal r44_AMOUNT_TIME_VOSTRO1) {
		R44_AMOUNT_TIME_VOSTRO1 = r44_AMOUNT_TIME_VOSTRO1;
	}
	public String getR45_NAME_OF_BANK_VOSTRO1() {
		return R45_NAME_OF_BANK_VOSTRO1;
	}
	public void setR45_NAME_OF_BANK_VOSTRO1(String r45_NAME_OF_BANK_VOSTRO1) {
		R45_NAME_OF_BANK_VOSTRO1 = r45_NAME_OF_BANK_VOSTRO1;
	}
	public String getR45_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R45_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR45_TYPE_OF_ACCOUNT_VOSTRO1(String r45_TYPE_OF_ACCOUNT_VOSTRO1) {
		R45_TYPE_OF_ACCOUNT_VOSTRO1 = r45_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR45_PURPOSE_VOSTRO1() {
		return R45_PURPOSE_VOSTRO1;
	}
	public void setR45_PURPOSE_VOSTRO1(String r45_PURPOSE_VOSTRO1) {
		R45_PURPOSE_VOSTRO1 = r45_PURPOSE_VOSTRO1;
	}
	public String getR45_CURRENCY_VOSTRO1() {
		return R45_CURRENCY_VOSTRO1;
	}
	public void setR45_CURRENCY_VOSTRO1(String r45_CURRENCY_VOSTRO1) {
		R45_CURRENCY_VOSTRO1 = r45_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR45_AMOUNT_DEMAND_VOSTRO1() {
		return R45_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR45_AMOUNT_DEMAND_VOSTRO1(BigDecimal r45_AMOUNT_DEMAND_VOSTRO1) {
		R45_AMOUNT_DEMAND_VOSTRO1 = r45_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR45_AMOUNT_TIME_VOSTRO1() {
		return R45_AMOUNT_TIME_VOSTRO1;
	}
	public void setR45_AMOUNT_TIME_VOSTRO1(BigDecimal r45_AMOUNT_TIME_VOSTRO1) {
		R45_AMOUNT_TIME_VOSTRO1 = r45_AMOUNT_TIME_VOSTRO1;
	}
	public String getR46_NAME_OF_BANK_VOSTRO1() {
		return R46_NAME_OF_BANK_VOSTRO1;
	}
	public void setR46_NAME_OF_BANK_VOSTRO1(String r46_NAME_OF_BANK_VOSTRO1) {
		R46_NAME_OF_BANK_VOSTRO1 = r46_NAME_OF_BANK_VOSTRO1;
	}
	public String getR46_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R46_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR46_TYPE_OF_ACCOUNT_VOSTRO1(String r46_TYPE_OF_ACCOUNT_VOSTRO1) {
		R46_TYPE_OF_ACCOUNT_VOSTRO1 = r46_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR46_PURPOSE_VOSTRO1() {
		return R46_PURPOSE_VOSTRO1;
	}
	public void setR46_PURPOSE_VOSTRO1(String r46_PURPOSE_VOSTRO1) {
		R46_PURPOSE_VOSTRO1 = r46_PURPOSE_VOSTRO1;
	}
	public String getR46_CURRENCY_VOSTRO1() {
		return R46_CURRENCY_VOSTRO1;
	}
	public void setR46_CURRENCY_VOSTRO1(String r46_CURRENCY_VOSTRO1) {
		R46_CURRENCY_VOSTRO1 = r46_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR46_AMOUNT_DEMAND_VOSTRO1() {
		return R46_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR46_AMOUNT_DEMAND_VOSTRO1(BigDecimal r46_AMOUNT_DEMAND_VOSTRO1) {
		R46_AMOUNT_DEMAND_VOSTRO1 = r46_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR46_AMOUNT_TIME_VOSTRO1() {
		return R46_AMOUNT_TIME_VOSTRO1;
	}
	public void setR46_AMOUNT_TIME_VOSTRO1(BigDecimal r46_AMOUNT_TIME_VOSTRO1) {
		R46_AMOUNT_TIME_VOSTRO1 = r46_AMOUNT_TIME_VOSTRO1;
	}
	public String getR47_NAME_OF_BANK_VOSTRO1() {
		return R47_NAME_OF_BANK_VOSTRO1;
	}
	public void setR47_NAME_OF_BANK_VOSTRO1(String r47_NAME_OF_BANK_VOSTRO1) {
		R47_NAME_OF_BANK_VOSTRO1 = r47_NAME_OF_BANK_VOSTRO1;
	}
	public String getR47_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R47_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR47_TYPE_OF_ACCOUNT_VOSTRO1(String r47_TYPE_OF_ACCOUNT_VOSTRO1) {
		R47_TYPE_OF_ACCOUNT_VOSTRO1 = r47_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR47_PURPOSE_VOSTRO1() {
		return R47_PURPOSE_VOSTRO1;
	}
	public void setR47_PURPOSE_VOSTRO1(String r47_PURPOSE_VOSTRO1) {
		R47_PURPOSE_VOSTRO1 = r47_PURPOSE_VOSTRO1;
	}
	public String getR47_CURRENCY_VOSTRO1() {
		return R47_CURRENCY_VOSTRO1;
	}
	public void setR47_CURRENCY_VOSTRO1(String r47_CURRENCY_VOSTRO1) {
		R47_CURRENCY_VOSTRO1 = r47_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR47_AMOUNT_DEMAND_VOSTRO1() {
		return R47_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR47_AMOUNT_DEMAND_VOSTRO1(BigDecimal r47_AMOUNT_DEMAND_VOSTRO1) {
		R47_AMOUNT_DEMAND_VOSTRO1 = r47_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR47_AMOUNT_TIME_VOSTRO1() {
		return R47_AMOUNT_TIME_VOSTRO1;
	}
	public void setR47_AMOUNT_TIME_VOSTRO1(BigDecimal r47_AMOUNT_TIME_VOSTRO1) {
		R47_AMOUNT_TIME_VOSTRO1 = r47_AMOUNT_TIME_VOSTRO1;
	}
	public String getR48_NAME_OF_BANK_VOSTRO1() {
		return R48_NAME_OF_BANK_VOSTRO1;
	}
	public void setR48_NAME_OF_BANK_VOSTRO1(String r48_NAME_OF_BANK_VOSTRO1) {
		R48_NAME_OF_BANK_VOSTRO1 = r48_NAME_OF_BANK_VOSTRO1;
	}
	public String getR48_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R48_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR48_TYPE_OF_ACCOUNT_VOSTRO1(String r48_TYPE_OF_ACCOUNT_VOSTRO1) {
		R48_TYPE_OF_ACCOUNT_VOSTRO1 = r48_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR48_PURPOSE_VOSTRO1() {
		return R48_PURPOSE_VOSTRO1;
	}
	public void setR48_PURPOSE_VOSTRO1(String r48_PURPOSE_VOSTRO1) {
		R48_PURPOSE_VOSTRO1 = r48_PURPOSE_VOSTRO1;
	}
	public String getR48_CURRENCY_VOSTRO1() {
		return R48_CURRENCY_VOSTRO1;
	}
	public void setR48_CURRENCY_VOSTRO1(String r48_CURRENCY_VOSTRO1) {
		R48_CURRENCY_VOSTRO1 = r48_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR48_AMOUNT_DEMAND_VOSTRO1() {
		return R48_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR48_AMOUNT_DEMAND_VOSTRO1(BigDecimal r48_AMOUNT_DEMAND_VOSTRO1) {
		R48_AMOUNT_DEMAND_VOSTRO1 = r48_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR48_AMOUNT_TIME_VOSTRO1() {
		return R48_AMOUNT_TIME_VOSTRO1;
	}
	public void setR48_AMOUNT_TIME_VOSTRO1(BigDecimal r48_AMOUNT_TIME_VOSTRO1) {
		R48_AMOUNT_TIME_VOSTRO1 = r48_AMOUNT_TIME_VOSTRO1;
	}
	public String getR49_NAME_OF_BANK_VOSTRO1() {
		return R49_NAME_OF_BANK_VOSTRO1;
	}
	public void setR49_NAME_OF_BANK_VOSTRO1(String r49_NAME_OF_BANK_VOSTRO1) {
		R49_NAME_OF_BANK_VOSTRO1 = r49_NAME_OF_BANK_VOSTRO1;
	}
	public String getR49_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R49_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR49_TYPE_OF_ACCOUNT_VOSTRO1(String r49_TYPE_OF_ACCOUNT_VOSTRO1) {
		R49_TYPE_OF_ACCOUNT_VOSTRO1 = r49_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR49_PURPOSE_VOSTRO1() {
		return R49_PURPOSE_VOSTRO1;
	}
	public void setR49_PURPOSE_VOSTRO1(String r49_PURPOSE_VOSTRO1) {
		R49_PURPOSE_VOSTRO1 = r49_PURPOSE_VOSTRO1;
	}
	public String getR49_CURRENCY_VOSTRO1() {
		return R49_CURRENCY_VOSTRO1;
	}
	public void setR49_CURRENCY_VOSTRO1(String r49_CURRENCY_VOSTRO1) {
		R49_CURRENCY_VOSTRO1 = r49_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR49_AMOUNT_DEMAND_VOSTRO1() {
		return R49_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR49_AMOUNT_DEMAND_VOSTRO1(BigDecimal r49_AMOUNT_DEMAND_VOSTRO1) {
		R49_AMOUNT_DEMAND_VOSTRO1 = r49_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR49_AMOUNT_TIME_VOSTRO1() {
		return R49_AMOUNT_TIME_VOSTRO1;
	}
	public void setR49_AMOUNT_TIME_VOSTRO1(BigDecimal r49_AMOUNT_TIME_VOSTRO1) {
		R49_AMOUNT_TIME_VOSTRO1 = r49_AMOUNT_TIME_VOSTRO1;
	}
	public String getR50_NAME_OF_BANK_VOSTRO1() {
		return R50_NAME_OF_BANK_VOSTRO1;
	}
	public void setR50_NAME_OF_BANK_VOSTRO1(String r50_NAME_OF_BANK_VOSTRO1) {
		R50_NAME_OF_BANK_VOSTRO1 = r50_NAME_OF_BANK_VOSTRO1;
	}
	public String getR50_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R50_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR50_TYPE_OF_ACCOUNT_VOSTRO1(String r50_TYPE_OF_ACCOUNT_VOSTRO1) {
		R50_TYPE_OF_ACCOUNT_VOSTRO1 = r50_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR50_PURPOSE_VOSTRO1() {
		return R50_PURPOSE_VOSTRO1;
	}
	public void setR50_PURPOSE_VOSTRO1(String r50_PURPOSE_VOSTRO1) {
		R50_PURPOSE_VOSTRO1 = r50_PURPOSE_VOSTRO1;
	}
	public String getR50_CURRENCY_VOSTRO1() {
		return R50_CURRENCY_VOSTRO1;
	}
	public void setR50_CURRENCY_VOSTRO1(String r50_CURRENCY_VOSTRO1) {
		R50_CURRENCY_VOSTRO1 = r50_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR50_AMOUNT_DEMAND_VOSTRO1() {
		return R50_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR50_AMOUNT_DEMAND_VOSTRO1(BigDecimal r50_AMOUNT_DEMAND_VOSTRO1) {
		R50_AMOUNT_DEMAND_VOSTRO1 = r50_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR50_AMOUNT_TIME_VOSTRO1() {
		return R50_AMOUNT_TIME_VOSTRO1;
	}
	public void setR50_AMOUNT_TIME_VOSTRO1(BigDecimal r50_AMOUNT_TIME_VOSTRO1) {
		R50_AMOUNT_TIME_VOSTRO1 = r50_AMOUNT_TIME_VOSTRO1;
	}
	public String getR51_NAME_OF_BANK_VOSTRO1() {
		return R51_NAME_OF_BANK_VOSTRO1;
	}
	public void setR51_NAME_OF_BANK_VOSTRO1(String r51_NAME_OF_BANK_VOSTRO1) {
		R51_NAME_OF_BANK_VOSTRO1 = r51_NAME_OF_BANK_VOSTRO1;
	}
	public String getR51_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R51_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR51_TYPE_OF_ACCOUNT_VOSTRO1(String r51_TYPE_OF_ACCOUNT_VOSTRO1) {
		R51_TYPE_OF_ACCOUNT_VOSTRO1 = r51_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR51_PURPOSE_VOSTRO1() {
		return R51_PURPOSE_VOSTRO1;
	}
	public void setR51_PURPOSE_VOSTRO1(String r51_PURPOSE_VOSTRO1) {
		R51_PURPOSE_VOSTRO1 = r51_PURPOSE_VOSTRO1;
	}
	public String getR51_CURRENCY_VOSTRO1() {
		return R51_CURRENCY_VOSTRO1;
	}
	public void setR51_CURRENCY_VOSTRO1(String r51_CURRENCY_VOSTRO1) {
		R51_CURRENCY_VOSTRO1 = r51_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR51_AMOUNT_DEMAND_VOSTRO1() {
		return R51_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR51_AMOUNT_DEMAND_VOSTRO1(BigDecimal r51_AMOUNT_DEMAND_VOSTRO1) {
		R51_AMOUNT_DEMAND_VOSTRO1 = r51_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR51_AMOUNT_TIME_VOSTRO1() {
		return R51_AMOUNT_TIME_VOSTRO1;
	}
	public void setR51_AMOUNT_TIME_VOSTRO1(BigDecimal r51_AMOUNT_TIME_VOSTRO1) {
		R51_AMOUNT_TIME_VOSTRO1 = r51_AMOUNT_TIME_VOSTRO1;
	}
	public String getR52_NAME_OF_BANK_VOSTRO1() {
		return R52_NAME_OF_BANK_VOSTRO1;
	}
	public void setR52_NAME_OF_BANK_VOSTRO1(String r52_NAME_OF_BANK_VOSTRO1) {
		R52_NAME_OF_BANK_VOSTRO1 = r52_NAME_OF_BANK_VOSTRO1;
	}
	public String getR52_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R52_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR52_TYPE_OF_ACCOUNT_VOSTRO1(String r52_TYPE_OF_ACCOUNT_VOSTRO1) {
		R52_TYPE_OF_ACCOUNT_VOSTRO1 = r52_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR52_PURPOSE_VOSTRO1() {
		return R52_PURPOSE_VOSTRO1;
	}
	public void setR52_PURPOSE_VOSTRO1(String r52_PURPOSE_VOSTRO1) {
		R52_PURPOSE_VOSTRO1 = r52_PURPOSE_VOSTRO1;
	}
	public String getR52_CURRENCY_VOSTRO1() {
		return R52_CURRENCY_VOSTRO1;
	}
	public void setR52_CURRENCY_VOSTRO1(String r52_CURRENCY_VOSTRO1) {
		R52_CURRENCY_VOSTRO1 = r52_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR52_AMOUNT_DEMAND_VOSTRO1() {
		return R52_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR52_AMOUNT_DEMAND_VOSTRO1(BigDecimal r52_AMOUNT_DEMAND_VOSTRO1) {
		R52_AMOUNT_DEMAND_VOSTRO1 = r52_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR52_AMOUNT_TIME_VOSTRO1() {
		return R52_AMOUNT_TIME_VOSTRO1;
	}
	public void setR52_AMOUNT_TIME_VOSTRO1(BigDecimal r52_AMOUNT_TIME_VOSTRO1) {
		R52_AMOUNT_TIME_VOSTRO1 = r52_AMOUNT_TIME_VOSTRO1;
	}
	public String getR53_NAME_OF_BANK_VOSTRO1() {
		return R53_NAME_OF_BANK_VOSTRO1;
	}
	public void setR53_NAME_OF_BANK_VOSTRO1(String r53_NAME_OF_BANK_VOSTRO1) {
		R53_NAME_OF_BANK_VOSTRO1 = r53_NAME_OF_BANK_VOSTRO1;
	}
	public String getR53_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R53_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR53_TYPE_OF_ACCOUNT_VOSTRO1(String r53_TYPE_OF_ACCOUNT_VOSTRO1) {
		R53_TYPE_OF_ACCOUNT_VOSTRO1 = r53_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR53_PURPOSE_VOSTRO1() {
		return R53_PURPOSE_VOSTRO1;
	}
	public void setR53_PURPOSE_VOSTRO1(String r53_PURPOSE_VOSTRO1) {
		R53_PURPOSE_VOSTRO1 = r53_PURPOSE_VOSTRO1;
	}
	public String getR53_CURRENCY_VOSTRO1() {
		return R53_CURRENCY_VOSTRO1;
	}
	public void setR53_CURRENCY_VOSTRO1(String r53_CURRENCY_VOSTRO1) {
		R53_CURRENCY_VOSTRO1 = r53_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR53_AMOUNT_DEMAND_VOSTRO1() {
		return R53_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR53_AMOUNT_DEMAND_VOSTRO1(BigDecimal r53_AMOUNT_DEMAND_VOSTRO1) {
		R53_AMOUNT_DEMAND_VOSTRO1 = r53_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR53_AMOUNT_TIME_VOSTRO1() {
		return R53_AMOUNT_TIME_VOSTRO1;
	}
	public void setR53_AMOUNT_TIME_VOSTRO1(BigDecimal r53_AMOUNT_TIME_VOSTRO1) {
		R53_AMOUNT_TIME_VOSTRO1 = r53_AMOUNT_TIME_VOSTRO1;
	}
	public String getR54_NAME_OF_BANK_VOSTRO1() {
		return R54_NAME_OF_BANK_VOSTRO1;
	}
	public void setR54_NAME_OF_BANK_VOSTRO1(String r54_NAME_OF_BANK_VOSTRO1) {
		R54_NAME_OF_BANK_VOSTRO1 = r54_NAME_OF_BANK_VOSTRO1;
	}
	public String getR54_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R54_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR54_TYPE_OF_ACCOUNT_VOSTRO1(String r54_TYPE_OF_ACCOUNT_VOSTRO1) {
		R54_TYPE_OF_ACCOUNT_VOSTRO1 = r54_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR54_PURPOSE_VOSTRO1() {
		return R54_PURPOSE_VOSTRO1;
	}
	public void setR54_PURPOSE_VOSTRO1(String r54_PURPOSE_VOSTRO1) {
		R54_PURPOSE_VOSTRO1 = r54_PURPOSE_VOSTRO1;
	}
	public String getR54_CURRENCY_VOSTRO1() {
		return R54_CURRENCY_VOSTRO1;
	}
	public void setR54_CURRENCY_VOSTRO1(String r54_CURRENCY_VOSTRO1) {
		R54_CURRENCY_VOSTRO1 = r54_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR54_AMOUNT_DEMAND_VOSTRO1() {
		return R54_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR54_AMOUNT_DEMAND_VOSTRO1(BigDecimal r54_AMOUNT_DEMAND_VOSTRO1) {
		R54_AMOUNT_DEMAND_VOSTRO1 = r54_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR54_AMOUNT_TIME_VOSTRO1() {
		return R54_AMOUNT_TIME_VOSTRO1;
	}
	public void setR54_AMOUNT_TIME_VOSTRO1(BigDecimal r54_AMOUNT_TIME_VOSTRO1) {
		R54_AMOUNT_TIME_VOSTRO1 = r54_AMOUNT_TIME_VOSTRO1;
	}
	public String getR55_NAME_OF_BANK_VOSTRO1() {
		return R55_NAME_OF_BANK_VOSTRO1;
	}
	public void setR55_NAME_OF_BANK_VOSTRO1(String r55_NAME_OF_BANK_VOSTRO1) {
		R55_NAME_OF_BANK_VOSTRO1 = r55_NAME_OF_BANK_VOSTRO1;
	}
	public String getR55_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R55_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR55_TYPE_OF_ACCOUNT_VOSTRO1(String r55_TYPE_OF_ACCOUNT_VOSTRO1) {
		R55_TYPE_OF_ACCOUNT_VOSTRO1 = r55_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR55_PURPOSE_VOSTRO1() {
		return R55_PURPOSE_VOSTRO1;
	}
	public void setR55_PURPOSE_VOSTRO1(String r55_PURPOSE_VOSTRO1) {
		R55_PURPOSE_VOSTRO1 = r55_PURPOSE_VOSTRO1;
	}
	public String getR55_CURRENCY_VOSTRO1() {
		return R55_CURRENCY_VOSTRO1;
	}
	public void setR55_CURRENCY_VOSTRO1(String r55_CURRENCY_VOSTRO1) {
		R55_CURRENCY_VOSTRO1 = r55_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR55_AMOUNT_DEMAND_VOSTRO1() {
		return R55_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR55_AMOUNT_DEMAND_VOSTRO1(BigDecimal r55_AMOUNT_DEMAND_VOSTRO1) {
		R55_AMOUNT_DEMAND_VOSTRO1 = r55_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR55_AMOUNT_TIME_VOSTRO1() {
		return R55_AMOUNT_TIME_VOSTRO1;
	}
	public void setR55_AMOUNT_TIME_VOSTRO1(BigDecimal r55_AMOUNT_TIME_VOSTRO1) {
		R55_AMOUNT_TIME_VOSTRO1 = r55_AMOUNT_TIME_VOSTRO1;
	}
	public String getR56_NAME_OF_BANK_VOSTRO1() {
		return R56_NAME_OF_BANK_VOSTRO1;
	}
	public void setR56_NAME_OF_BANK_VOSTRO1(String r56_NAME_OF_BANK_VOSTRO1) {
		R56_NAME_OF_BANK_VOSTRO1 = r56_NAME_OF_BANK_VOSTRO1;
	}
	public String getR56_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R56_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR56_TYPE_OF_ACCOUNT_VOSTRO1(String r56_TYPE_OF_ACCOUNT_VOSTRO1) {
		R56_TYPE_OF_ACCOUNT_VOSTRO1 = r56_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR56_PURPOSE_VOSTRO1() {
		return R56_PURPOSE_VOSTRO1;
	}
	public void setR56_PURPOSE_VOSTRO1(String r56_PURPOSE_VOSTRO1) {
		R56_PURPOSE_VOSTRO1 = r56_PURPOSE_VOSTRO1;
	}
	public String getR56_CURRENCY_VOSTRO1() {
		return R56_CURRENCY_VOSTRO1;
	}
	public void setR56_CURRENCY_VOSTRO1(String r56_CURRENCY_VOSTRO1) {
		R56_CURRENCY_VOSTRO1 = r56_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR56_AMOUNT_DEMAND_VOSTRO1() {
		return R56_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR56_AMOUNT_DEMAND_VOSTRO1(BigDecimal r56_AMOUNT_DEMAND_VOSTRO1) {
		R56_AMOUNT_DEMAND_VOSTRO1 = r56_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR56_AMOUNT_TIME_VOSTRO1() {
		return R56_AMOUNT_TIME_VOSTRO1;
	}
	public void setR56_AMOUNT_TIME_VOSTRO1(BigDecimal r56_AMOUNT_TIME_VOSTRO1) {
		R56_AMOUNT_TIME_VOSTRO1 = r56_AMOUNT_TIME_VOSTRO1;
	}
	public String getR57_NAME_OF_BANK_VOSTRO1() {
		return R57_NAME_OF_BANK_VOSTRO1;
	}
	public void setR57_NAME_OF_BANK_VOSTRO1(String r57_NAME_OF_BANK_VOSTRO1) {
		R57_NAME_OF_BANK_VOSTRO1 = r57_NAME_OF_BANK_VOSTRO1;
	}
	public String getR57_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R57_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR57_TYPE_OF_ACCOUNT_VOSTRO1(String r57_TYPE_OF_ACCOUNT_VOSTRO1) {
		R57_TYPE_OF_ACCOUNT_VOSTRO1 = r57_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR57_PURPOSE_VOSTRO1() {
		return R57_PURPOSE_VOSTRO1;
	}
	public void setR57_PURPOSE_VOSTRO1(String r57_PURPOSE_VOSTRO1) {
		R57_PURPOSE_VOSTRO1 = r57_PURPOSE_VOSTRO1;
	}
	public String getR57_CURRENCY_VOSTRO1() {
		return R57_CURRENCY_VOSTRO1;
	}
	public void setR57_CURRENCY_VOSTRO1(String r57_CURRENCY_VOSTRO1) {
		R57_CURRENCY_VOSTRO1 = r57_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR57_AMOUNT_DEMAND_VOSTRO1() {
		return R57_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR57_AMOUNT_DEMAND_VOSTRO1(BigDecimal r57_AMOUNT_DEMAND_VOSTRO1) {
		R57_AMOUNT_DEMAND_VOSTRO1 = r57_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR57_AMOUNT_TIME_VOSTRO1() {
		return R57_AMOUNT_TIME_VOSTRO1;
	}
	public void setR57_AMOUNT_TIME_VOSTRO1(BigDecimal r57_AMOUNT_TIME_VOSTRO1) {
		R57_AMOUNT_TIME_VOSTRO1 = r57_AMOUNT_TIME_VOSTRO1;
	}
	public String getR58_NAME_OF_BANK_VOSTRO1() {
		return R58_NAME_OF_BANK_VOSTRO1;
	}
	public void setR58_NAME_OF_BANK_VOSTRO1(String r58_NAME_OF_BANK_VOSTRO1) {
		R58_NAME_OF_BANK_VOSTRO1 = r58_NAME_OF_BANK_VOSTRO1;
	}
	public String getR58_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R58_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR58_TYPE_OF_ACCOUNT_VOSTRO1(String r58_TYPE_OF_ACCOUNT_VOSTRO1) {
		R58_TYPE_OF_ACCOUNT_VOSTRO1 = r58_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR58_PURPOSE_VOSTRO1() {
		return R58_PURPOSE_VOSTRO1;
	}
	public void setR58_PURPOSE_VOSTRO1(String r58_PURPOSE_VOSTRO1) {
		R58_PURPOSE_VOSTRO1 = r58_PURPOSE_VOSTRO1;
	}
	public String getR58_CURRENCY_VOSTRO1() {
		return R58_CURRENCY_VOSTRO1;
	}
	public void setR58_CURRENCY_VOSTRO1(String r58_CURRENCY_VOSTRO1) {
		R58_CURRENCY_VOSTRO1 = r58_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR58_AMOUNT_DEMAND_VOSTRO1() {
		return R58_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR58_AMOUNT_DEMAND_VOSTRO1(BigDecimal r58_AMOUNT_DEMAND_VOSTRO1) {
		R58_AMOUNT_DEMAND_VOSTRO1 = r58_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR58_AMOUNT_TIME_VOSTRO1() {
		return R58_AMOUNT_TIME_VOSTRO1;
	}
	public void setR58_AMOUNT_TIME_VOSTRO1(BigDecimal r58_AMOUNT_TIME_VOSTRO1) {
		R58_AMOUNT_TIME_VOSTRO1 = r58_AMOUNT_TIME_VOSTRO1;
	}
	public String getR59_NAME_OF_BANK_VOSTRO1() {
		return R59_NAME_OF_BANK_VOSTRO1;
	}
	public void setR59_NAME_OF_BANK_VOSTRO1(String r59_NAME_OF_BANK_VOSTRO1) {
		R59_NAME_OF_BANK_VOSTRO1 = r59_NAME_OF_BANK_VOSTRO1;
	}
	public String getR59_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R59_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR59_TYPE_OF_ACCOUNT_VOSTRO1(String r59_TYPE_OF_ACCOUNT_VOSTRO1) {
		R59_TYPE_OF_ACCOUNT_VOSTRO1 = r59_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR59_PURPOSE_VOSTRO1() {
		return R59_PURPOSE_VOSTRO1;
	}
	public void setR59_PURPOSE_VOSTRO1(String r59_PURPOSE_VOSTRO1) {
		R59_PURPOSE_VOSTRO1 = r59_PURPOSE_VOSTRO1;
	}
	public String getR59_CURRENCY_VOSTRO1() {
		return R59_CURRENCY_VOSTRO1;
	}
	public void setR59_CURRENCY_VOSTRO1(String r59_CURRENCY_VOSTRO1) {
		R59_CURRENCY_VOSTRO1 = r59_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR59_AMOUNT_DEMAND_VOSTRO1() {
		return R59_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR59_AMOUNT_DEMAND_VOSTRO1(BigDecimal r59_AMOUNT_DEMAND_VOSTRO1) {
		R59_AMOUNT_DEMAND_VOSTRO1 = r59_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR59_AMOUNT_TIME_VOSTRO1() {
		return R59_AMOUNT_TIME_VOSTRO1;
	}
	public void setR59_AMOUNT_TIME_VOSTRO1(BigDecimal r59_AMOUNT_TIME_VOSTRO1) {
		R59_AMOUNT_TIME_VOSTRO1 = r59_AMOUNT_TIME_VOSTRO1;
	}
	public String getR60_NAME_OF_BANK_VOSTRO1() {
		return R60_NAME_OF_BANK_VOSTRO1;
	}
	public void setR60_NAME_OF_BANK_VOSTRO1(String r60_NAME_OF_BANK_VOSTRO1) {
		R60_NAME_OF_BANK_VOSTRO1 = r60_NAME_OF_BANK_VOSTRO1;
	}
	public String getR60_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R60_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR60_TYPE_OF_ACCOUNT_VOSTRO1(String r60_TYPE_OF_ACCOUNT_VOSTRO1) {
		R60_TYPE_OF_ACCOUNT_VOSTRO1 = r60_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR60_PURPOSE_VOSTRO1() {
		return R60_PURPOSE_VOSTRO1;
	}
	public void setR60_PURPOSE_VOSTRO1(String r60_PURPOSE_VOSTRO1) {
		R60_PURPOSE_VOSTRO1 = r60_PURPOSE_VOSTRO1;
	}
	public String getR60_CURRENCY_VOSTRO1() {
		return R60_CURRENCY_VOSTRO1;
	}
	public void setR60_CURRENCY_VOSTRO1(String r60_CURRENCY_VOSTRO1) {
		R60_CURRENCY_VOSTRO1 = r60_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR60_AMOUNT_DEMAND_VOSTRO1() {
		return R60_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR60_AMOUNT_DEMAND_VOSTRO1(BigDecimal r60_AMOUNT_DEMAND_VOSTRO1) {
		R60_AMOUNT_DEMAND_VOSTRO1 = r60_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR60_AMOUNT_TIME_VOSTRO1() {
		return R60_AMOUNT_TIME_VOSTRO1;
	}
	public void setR60_AMOUNT_TIME_VOSTRO1(BigDecimal r60_AMOUNT_TIME_VOSTRO1) {
		R60_AMOUNT_TIME_VOSTRO1 = r60_AMOUNT_TIME_VOSTRO1;
	}
	public String getR61_NAME_OF_BANK_VOSTRO1() {
		return R61_NAME_OF_BANK_VOSTRO1;
	}
	public void setR61_NAME_OF_BANK_VOSTRO1(String r61_NAME_OF_BANK_VOSTRO1) {
		R61_NAME_OF_BANK_VOSTRO1 = r61_NAME_OF_BANK_VOSTRO1;
	}
	public String getR61_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R61_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR61_TYPE_OF_ACCOUNT_VOSTRO1(String r61_TYPE_OF_ACCOUNT_VOSTRO1) {
		R61_TYPE_OF_ACCOUNT_VOSTRO1 = r61_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR61_PURPOSE_VOSTRO1() {
		return R61_PURPOSE_VOSTRO1;
	}
	public void setR61_PURPOSE_VOSTRO1(String r61_PURPOSE_VOSTRO1) {
		R61_PURPOSE_VOSTRO1 = r61_PURPOSE_VOSTRO1;
	}
	public String getR61_CURRENCY_VOSTRO1() {
		return R61_CURRENCY_VOSTRO1;
	}
	public void setR61_CURRENCY_VOSTRO1(String r61_CURRENCY_VOSTRO1) {
		R61_CURRENCY_VOSTRO1 = r61_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR61_AMOUNT_DEMAND_VOSTRO1() {
		return R61_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR61_AMOUNT_DEMAND_VOSTRO1(BigDecimal r61_AMOUNT_DEMAND_VOSTRO1) {
		R61_AMOUNT_DEMAND_VOSTRO1 = r61_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR61_AMOUNT_TIME_VOSTRO1() {
		return R61_AMOUNT_TIME_VOSTRO1;
	}
	public void setR61_AMOUNT_TIME_VOSTRO1(BigDecimal r61_AMOUNT_TIME_VOSTRO1) {
		R61_AMOUNT_TIME_VOSTRO1 = r61_AMOUNT_TIME_VOSTRO1;
	}
	public String getR62_NAME_OF_BANK_VOSTRO1() {
		return R62_NAME_OF_BANK_VOSTRO1;
	}
	public void setR62_NAME_OF_BANK_VOSTRO1(String r62_NAME_OF_BANK_VOSTRO1) {
		R62_NAME_OF_BANK_VOSTRO1 = r62_NAME_OF_BANK_VOSTRO1;
	}
	public String getR62_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R62_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR62_TYPE_OF_ACCOUNT_VOSTRO1(String r62_TYPE_OF_ACCOUNT_VOSTRO1) {
		R62_TYPE_OF_ACCOUNT_VOSTRO1 = r62_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR62_PURPOSE_VOSTRO1() {
		return R62_PURPOSE_VOSTRO1;
	}
	public void setR62_PURPOSE_VOSTRO1(String r62_PURPOSE_VOSTRO1) {
		R62_PURPOSE_VOSTRO1 = r62_PURPOSE_VOSTRO1;
	}
	public String getR62_CURRENCY_VOSTRO1() {
		return R62_CURRENCY_VOSTRO1;
	}
	public void setR62_CURRENCY_VOSTRO1(String r62_CURRENCY_VOSTRO1) {
		R62_CURRENCY_VOSTRO1 = r62_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR62_AMOUNT_DEMAND_VOSTRO1() {
		return R62_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR62_AMOUNT_DEMAND_VOSTRO1(BigDecimal r62_AMOUNT_DEMAND_VOSTRO1) {
		R62_AMOUNT_DEMAND_VOSTRO1 = r62_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR62_AMOUNT_TIME_VOSTRO1() {
		return R62_AMOUNT_TIME_VOSTRO1;
	}
	public void setR62_AMOUNT_TIME_VOSTRO1(BigDecimal r62_AMOUNT_TIME_VOSTRO1) {
		R62_AMOUNT_TIME_VOSTRO1 = r62_AMOUNT_TIME_VOSTRO1;
	}
	public String getR63_NAME_OF_BANK_VOSTRO1() {
		return R63_NAME_OF_BANK_VOSTRO1;
	}
	public void setR63_NAME_OF_BANK_VOSTRO1(String r63_NAME_OF_BANK_VOSTRO1) {
		R63_NAME_OF_BANK_VOSTRO1 = r63_NAME_OF_BANK_VOSTRO1;
	}
	public String getR63_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R63_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR63_TYPE_OF_ACCOUNT_VOSTRO1(String r63_TYPE_OF_ACCOUNT_VOSTRO1) {
		R63_TYPE_OF_ACCOUNT_VOSTRO1 = r63_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR63_PURPOSE_VOSTRO1() {
		return R63_PURPOSE_VOSTRO1;
	}
	public void setR63_PURPOSE_VOSTRO1(String r63_PURPOSE_VOSTRO1) {
		R63_PURPOSE_VOSTRO1 = r63_PURPOSE_VOSTRO1;
	}
	public String getR63_CURRENCY_VOSTRO1() {
		return R63_CURRENCY_VOSTRO1;
	}
	public void setR63_CURRENCY_VOSTRO1(String r63_CURRENCY_VOSTRO1) {
		R63_CURRENCY_VOSTRO1 = r63_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR63_AMOUNT_DEMAND_VOSTRO1() {
		return R63_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR63_AMOUNT_DEMAND_VOSTRO1(BigDecimal r63_AMOUNT_DEMAND_VOSTRO1) {
		R63_AMOUNT_DEMAND_VOSTRO1 = r63_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR63_AMOUNT_TIME_VOSTRO1() {
		return R63_AMOUNT_TIME_VOSTRO1;
	}
	public void setR63_AMOUNT_TIME_VOSTRO1(BigDecimal r63_AMOUNT_TIME_VOSTRO1) {
		R63_AMOUNT_TIME_VOSTRO1 = r63_AMOUNT_TIME_VOSTRO1;
	}
	public String getR64_NAME_OF_BANK_VOSTRO1() {
		return R64_NAME_OF_BANK_VOSTRO1;
	}
	public void setR64_NAME_OF_BANK_VOSTRO1(String r64_NAME_OF_BANK_VOSTRO1) {
		R64_NAME_OF_BANK_VOSTRO1 = r64_NAME_OF_BANK_VOSTRO1;
	}
	public String getR64_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R64_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR64_TYPE_OF_ACCOUNT_VOSTRO1(String r64_TYPE_OF_ACCOUNT_VOSTRO1) {
		R64_TYPE_OF_ACCOUNT_VOSTRO1 = r64_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR64_PURPOSE_VOSTRO1() {
		return R64_PURPOSE_VOSTRO1;
	}
	public void setR64_PURPOSE_VOSTRO1(String r64_PURPOSE_VOSTRO1) {
		R64_PURPOSE_VOSTRO1 = r64_PURPOSE_VOSTRO1;
	}
	public String getR64_CURRENCY_VOSTRO1() {
		return R64_CURRENCY_VOSTRO1;
	}
	public void setR64_CURRENCY_VOSTRO1(String r64_CURRENCY_VOSTRO1) {
		R64_CURRENCY_VOSTRO1 = r64_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR64_AMOUNT_DEMAND_VOSTRO1() {
		return R64_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR64_AMOUNT_DEMAND_VOSTRO1(BigDecimal r64_AMOUNT_DEMAND_VOSTRO1) {
		R64_AMOUNT_DEMAND_VOSTRO1 = r64_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR64_AMOUNT_TIME_VOSTRO1() {
		return R64_AMOUNT_TIME_VOSTRO1;
	}
	public void setR64_AMOUNT_TIME_VOSTRO1(BigDecimal r64_AMOUNT_TIME_VOSTRO1) {
		R64_AMOUNT_TIME_VOSTRO1 = r64_AMOUNT_TIME_VOSTRO1;
	}
	public String getR65_NAME_OF_BANK_VOSTRO1() {
		return R65_NAME_OF_BANK_VOSTRO1;
	}
	public void setR65_NAME_OF_BANK_VOSTRO1(String r65_NAME_OF_BANK_VOSTRO1) {
		R65_NAME_OF_BANK_VOSTRO1 = r65_NAME_OF_BANK_VOSTRO1;
	}
	public String getR65_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R65_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR65_TYPE_OF_ACCOUNT_VOSTRO1(String r65_TYPE_OF_ACCOUNT_VOSTRO1) {
		R65_TYPE_OF_ACCOUNT_VOSTRO1 = r65_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR65_PURPOSE_VOSTRO1() {
		return R65_PURPOSE_VOSTRO1;
	}
	public void setR65_PURPOSE_VOSTRO1(String r65_PURPOSE_VOSTRO1) {
		R65_PURPOSE_VOSTRO1 = r65_PURPOSE_VOSTRO1;
	}
	public String getR65_CURRENCY_VOSTRO1() {
		return R65_CURRENCY_VOSTRO1;
	}
	public void setR65_CURRENCY_VOSTRO1(String r65_CURRENCY_VOSTRO1) {
		R65_CURRENCY_VOSTRO1 = r65_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR65_AMOUNT_DEMAND_VOSTRO1() {
		return R65_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR65_AMOUNT_DEMAND_VOSTRO1(BigDecimal r65_AMOUNT_DEMAND_VOSTRO1) {
		R65_AMOUNT_DEMAND_VOSTRO1 = r65_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR65_AMOUNT_TIME_VOSTRO1() {
		return R65_AMOUNT_TIME_VOSTRO1;
	}
	public void setR65_AMOUNT_TIME_VOSTRO1(BigDecimal r65_AMOUNT_TIME_VOSTRO1) {
		R65_AMOUNT_TIME_VOSTRO1 = r65_AMOUNT_TIME_VOSTRO1;
	}
	public String getR66_NAME_OF_BANK_VOSTRO1() {
		return R66_NAME_OF_BANK_VOSTRO1;
	}
	public void setR66_NAME_OF_BANK_VOSTRO1(String r66_NAME_OF_BANK_VOSTRO1) {
		R66_NAME_OF_BANK_VOSTRO1 = r66_NAME_OF_BANK_VOSTRO1;
	}
	public String getR66_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R66_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR66_TYPE_OF_ACCOUNT_VOSTRO1(String r66_TYPE_OF_ACCOUNT_VOSTRO1) {
		R66_TYPE_OF_ACCOUNT_VOSTRO1 = r66_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR66_PURPOSE_VOSTRO1() {
		return R66_PURPOSE_VOSTRO1;
	}
	public void setR66_PURPOSE_VOSTRO1(String r66_PURPOSE_VOSTRO1) {
		R66_PURPOSE_VOSTRO1 = r66_PURPOSE_VOSTRO1;
	}
	public String getR66_CURRENCY_VOSTRO1() {
		return R66_CURRENCY_VOSTRO1;
	}
	public void setR66_CURRENCY_VOSTRO1(String r66_CURRENCY_VOSTRO1) {
		R66_CURRENCY_VOSTRO1 = r66_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR66_AMOUNT_DEMAND_VOSTRO1() {
		return R66_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR66_AMOUNT_DEMAND_VOSTRO1(BigDecimal r66_AMOUNT_DEMAND_VOSTRO1) {
		R66_AMOUNT_DEMAND_VOSTRO1 = r66_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR66_AMOUNT_TIME_VOSTRO1() {
		return R66_AMOUNT_TIME_VOSTRO1;
	}
	public void setR66_AMOUNT_TIME_VOSTRO1(BigDecimal r66_AMOUNT_TIME_VOSTRO1) {
		R66_AMOUNT_TIME_VOSTRO1 = r66_AMOUNT_TIME_VOSTRO1;
	}
	public String getR67_NAME_OF_BANK_VOSTRO1() {
		return R67_NAME_OF_BANK_VOSTRO1;
	}
	public void setR67_NAME_OF_BANK_VOSTRO1(String r67_NAME_OF_BANK_VOSTRO1) {
		R67_NAME_OF_BANK_VOSTRO1 = r67_NAME_OF_BANK_VOSTRO1;
	}
	public String getR67_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R67_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR67_TYPE_OF_ACCOUNT_VOSTRO1(String r67_TYPE_OF_ACCOUNT_VOSTRO1) {
		R67_TYPE_OF_ACCOUNT_VOSTRO1 = r67_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR67_PURPOSE_VOSTRO1() {
		return R67_PURPOSE_VOSTRO1;
	}
	public void setR67_PURPOSE_VOSTRO1(String r67_PURPOSE_VOSTRO1) {
		R67_PURPOSE_VOSTRO1 = r67_PURPOSE_VOSTRO1;
	}
	public String getR67_CURRENCY_VOSTRO1() {
		return R67_CURRENCY_VOSTRO1;
	}
	public void setR67_CURRENCY_VOSTRO1(String r67_CURRENCY_VOSTRO1) {
		R67_CURRENCY_VOSTRO1 = r67_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR67_AMOUNT_DEMAND_VOSTRO1() {
		return R67_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR67_AMOUNT_DEMAND_VOSTRO1(BigDecimal r67_AMOUNT_DEMAND_VOSTRO1) {
		R67_AMOUNT_DEMAND_VOSTRO1 = r67_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR67_AMOUNT_TIME_VOSTRO1() {
		return R67_AMOUNT_TIME_VOSTRO1;
	}
	public void setR67_AMOUNT_TIME_VOSTRO1(BigDecimal r67_AMOUNT_TIME_VOSTRO1) {
		R67_AMOUNT_TIME_VOSTRO1 = r67_AMOUNT_TIME_VOSTRO1;
	}
	public String getR68_NAME_OF_BANK_VOSTRO1() {
		return R68_NAME_OF_BANK_VOSTRO1;
	}
	public void setR68_NAME_OF_BANK_VOSTRO1(String r68_NAME_OF_BANK_VOSTRO1) {
		R68_NAME_OF_BANK_VOSTRO1 = r68_NAME_OF_BANK_VOSTRO1;
	}
	public String getR68_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R68_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR68_TYPE_OF_ACCOUNT_VOSTRO1(String r68_TYPE_OF_ACCOUNT_VOSTRO1) {
		R68_TYPE_OF_ACCOUNT_VOSTRO1 = r68_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR68_PURPOSE_VOSTRO1() {
		return R68_PURPOSE_VOSTRO1;
	}
	public void setR68_PURPOSE_VOSTRO1(String r68_PURPOSE_VOSTRO1) {
		R68_PURPOSE_VOSTRO1 = r68_PURPOSE_VOSTRO1;
	}
	public String getR68_CURRENCY_VOSTRO1() {
		return R68_CURRENCY_VOSTRO1;
	}
	public void setR68_CURRENCY_VOSTRO1(String r68_CURRENCY_VOSTRO1) {
		R68_CURRENCY_VOSTRO1 = r68_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR68_AMOUNT_DEMAND_VOSTRO1() {
		return R68_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR68_AMOUNT_DEMAND_VOSTRO1(BigDecimal r68_AMOUNT_DEMAND_VOSTRO1) {
		R68_AMOUNT_DEMAND_VOSTRO1 = r68_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR68_AMOUNT_TIME_VOSTRO1() {
		return R68_AMOUNT_TIME_VOSTRO1;
	}
	public void setR68_AMOUNT_TIME_VOSTRO1(BigDecimal r68_AMOUNT_TIME_VOSTRO1) {
		R68_AMOUNT_TIME_VOSTRO1 = r68_AMOUNT_TIME_VOSTRO1;
	}
	public String getR69_NAME_OF_BANK_VOSTRO1() {
		return R69_NAME_OF_BANK_VOSTRO1;
	}
	public void setR69_NAME_OF_BANK_VOSTRO1(String r69_NAME_OF_BANK_VOSTRO1) {
		R69_NAME_OF_BANK_VOSTRO1 = r69_NAME_OF_BANK_VOSTRO1;
	}
	public String getR69_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R69_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR69_TYPE_OF_ACCOUNT_VOSTRO1(String r69_TYPE_OF_ACCOUNT_VOSTRO1) {
		R69_TYPE_OF_ACCOUNT_VOSTRO1 = r69_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR69_PURPOSE_VOSTRO1() {
		return R69_PURPOSE_VOSTRO1;
	}
	public void setR69_PURPOSE_VOSTRO1(String r69_PURPOSE_VOSTRO1) {
		R69_PURPOSE_VOSTRO1 = r69_PURPOSE_VOSTRO1;
	}
	public String getR69_CURRENCY_VOSTRO1() {
		return R69_CURRENCY_VOSTRO1;
	}
	public void setR69_CURRENCY_VOSTRO1(String r69_CURRENCY_VOSTRO1) {
		R69_CURRENCY_VOSTRO1 = r69_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR69_AMOUNT_DEMAND_VOSTRO1() {
		return R69_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR69_AMOUNT_DEMAND_VOSTRO1(BigDecimal r69_AMOUNT_DEMAND_VOSTRO1) {
		R69_AMOUNT_DEMAND_VOSTRO1 = r69_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR69_AMOUNT_TIME_VOSTRO1() {
		return R69_AMOUNT_TIME_VOSTRO1;
	}
	public void setR69_AMOUNT_TIME_VOSTRO1(BigDecimal r69_AMOUNT_TIME_VOSTRO1) {
		R69_AMOUNT_TIME_VOSTRO1 = r69_AMOUNT_TIME_VOSTRO1;
	}
	public String getR70_NAME_OF_BANK_VOSTRO1() {
		return R70_NAME_OF_BANK_VOSTRO1;
	}
	public void setR70_NAME_OF_BANK_VOSTRO1(String r70_NAME_OF_BANK_VOSTRO1) {
		R70_NAME_OF_BANK_VOSTRO1 = r70_NAME_OF_BANK_VOSTRO1;
	}
	public String getR70_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R70_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR70_TYPE_OF_ACCOUNT_VOSTRO1(String r70_TYPE_OF_ACCOUNT_VOSTRO1) {
		R70_TYPE_OF_ACCOUNT_VOSTRO1 = r70_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR70_PURPOSE_VOSTRO1() {
		return R70_PURPOSE_VOSTRO1;
	}
	public void setR70_PURPOSE_VOSTRO1(String r70_PURPOSE_VOSTRO1) {
		R70_PURPOSE_VOSTRO1 = r70_PURPOSE_VOSTRO1;
	}
	public String getR70_CURRENCY_VOSTRO1() {
		return R70_CURRENCY_VOSTRO1;
	}
	public void setR70_CURRENCY_VOSTRO1(String r70_CURRENCY_VOSTRO1) {
		R70_CURRENCY_VOSTRO1 = r70_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR70_AMOUNT_DEMAND_VOSTRO1() {
		return R70_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR70_AMOUNT_DEMAND_VOSTRO1(BigDecimal r70_AMOUNT_DEMAND_VOSTRO1) {
		R70_AMOUNT_DEMAND_VOSTRO1 = r70_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR70_AMOUNT_TIME_VOSTRO1() {
		return R70_AMOUNT_TIME_VOSTRO1;
	}
	public void setR70_AMOUNT_TIME_VOSTRO1(BigDecimal r70_AMOUNT_TIME_VOSTRO1) {
		R70_AMOUNT_TIME_VOSTRO1 = r70_AMOUNT_TIME_VOSTRO1;
	}
	public String getR71_NAME_OF_BANK_VOSTRO1() {
		return R71_NAME_OF_BANK_VOSTRO1;
	}
	public void setR71_NAME_OF_BANK_VOSTRO1(String r71_NAME_OF_BANK_VOSTRO1) {
		R71_NAME_OF_BANK_VOSTRO1 = r71_NAME_OF_BANK_VOSTRO1;
	}
	public String getR71_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R71_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR71_TYPE_OF_ACCOUNT_VOSTRO1(String r71_TYPE_OF_ACCOUNT_VOSTRO1) {
		R71_TYPE_OF_ACCOUNT_VOSTRO1 = r71_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR71_PURPOSE_VOSTRO1() {
		return R71_PURPOSE_VOSTRO1;
	}
	public void setR71_PURPOSE_VOSTRO1(String r71_PURPOSE_VOSTRO1) {
		R71_PURPOSE_VOSTRO1 = r71_PURPOSE_VOSTRO1;
	}
	public String getR71_CURRENCY_VOSTRO1() {
		return R71_CURRENCY_VOSTRO1;
	}
	public void setR71_CURRENCY_VOSTRO1(String r71_CURRENCY_VOSTRO1) {
		R71_CURRENCY_VOSTRO1 = r71_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR71_AMOUNT_DEMAND_VOSTRO1() {
		return R71_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR71_AMOUNT_DEMAND_VOSTRO1(BigDecimal r71_AMOUNT_DEMAND_VOSTRO1) {
		R71_AMOUNT_DEMAND_VOSTRO1 = r71_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR71_AMOUNT_TIME_VOSTRO1() {
		return R71_AMOUNT_TIME_VOSTRO1;
	}
	public void setR71_AMOUNT_TIME_VOSTRO1(BigDecimal r71_AMOUNT_TIME_VOSTRO1) {
		R71_AMOUNT_TIME_VOSTRO1 = r71_AMOUNT_TIME_VOSTRO1;
	}
	public String getR72_NAME_OF_BANK_VOSTRO1() {
		return R72_NAME_OF_BANK_VOSTRO1;
	}
	public void setR72_NAME_OF_BANK_VOSTRO1(String r72_NAME_OF_BANK_VOSTRO1) {
		R72_NAME_OF_BANK_VOSTRO1 = r72_NAME_OF_BANK_VOSTRO1;
	}
	public String getR72_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R72_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR72_TYPE_OF_ACCOUNT_VOSTRO1(String r72_TYPE_OF_ACCOUNT_VOSTRO1) {
		R72_TYPE_OF_ACCOUNT_VOSTRO1 = r72_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR72_PURPOSE_VOSTRO1() {
		return R72_PURPOSE_VOSTRO1;
	}
	public void setR72_PURPOSE_VOSTRO1(String r72_PURPOSE_VOSTRO1) {
		R72_PURPOSE_VOSTRO1 = r72_PURPOSE_VOSTRO1;
	}
	public String getR72_CURRENCY_VOSTRO1() {
		return R72_CURRENCY_VOSTRO1;
	}
	public void setR72_CURRENCY_VOSTRO1(String r72_CURRENCY_VOSTRO1) {
		R72_CURRENCY_VOSTRO1 = r72_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR72_AMOUNT_DEMAND_VOSTRO1() {
		return R72_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR72_AMOUNT_DEMAND_VOSTRO1(BigDecimal r72_AMOUNT_DEMAND_VOSTRO1) {
		R72_AMOUNT_DEMAND_VOSTRO1 = r72_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR72_AMOUNT_TIME_VOSTRO1() {
		return R72_AMOUNT_TIME_VOSTRO1;
	}
	public void setR72_AMOUNT_TIME_VOSTRO1(BigDecimal r72_AMOUNT_TIME_VOSTRO1) {
		R72_AMOUNT_TIME_VOSTRO1 = r72_AMOUNT_TIME_VOSTRO1;
	}
	public String getR73_NAME_OF_BANK_VOSTRO1() {
		return R73_NAME_OF_BANK_VOSTRO1;
	}
	public void setR73_NAME_OF_BANK_VOSTRO1(String r73_NAME_OF_BANK_VOSTRO1) {
		R73_NAME_OF_BANK_VOSTRO1 = r73_NAME_OF_BANK_VOSTRO1;
	}
	public String getR73_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R73_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR73_TYPE_OF_ACCOUNT_VOSTRO1(String r73_TYPE_OF_ACCOUNT_VOSTRO1) {
		R73_TYPE_OF_ACCOUNT_VOSTRO1 = r73_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR73_PURPOSE_VOSTRO1() {
		return R73_PURPOSE_VOSTRO1;
	}
	public void setR73_PURPOSE_VOSTRO1(String r73_PURPOSE_VOSTRO1) {
		R73_PURPOSE_VOSTRO1 = r73_PURPOSE_VOSTRO1;
	}
	public String getR73_CURRENCY_VOSTRO1() {
		return R73_CURRENCY_VOSTRO1;
	}
	public void setR73_CURRENCY_VOSTRO1(String r73_CURRENCY_VOSTRO1) {
		R73_CURRENCY_VOSTRO1 = r73_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR73_AMOUNT_DEMAND_VOSTRO1() {
		return R73_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR73_AMOUNT_DEMAND_VOSTRO1(BigDecimal r73_AMOUNT_DEMAND_VOSTRO1) {
		R73_AMOUNT_DEMAND_VOSTRO1 = r73_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR73_AMOUNT_TIME_VOSTRO1() {
		return R73_AMOUNT_TIME_VOSTRO1;
	}
	public void setR73_AMOUNT_TIME_VOSTRO1(BigDecimal r73_AMOUNT_TIME_VOSTRO1) {
		R73_AMOUNT_TIME_VOSTRO1 = r73_AMOUNT_TIME_VOSTRO1;
	}
	public String getR74_NAME_OF_BANK_VOSTRO1() {
		return R74_NAME_OF_BANK_VOSTRO1;
	}
	public void setR74_NAME_OF_BANK_VOSTRO1(String r74_NAME_OF_BANK_VOSTRO1) {
		R74_NAME_OF_BANK_VOSTRO1 = r74_NAME_OF_BANK_VOSTRO1;
	}
	public String getR74_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R74_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR74_TYPE_OF_ACCOUNT_VOSTRO1(String r74_TYPE_OF_ACCOUNT_VOSTRO1) {
		R74_TYPE_OF_ACCOUNT_VOSTRO1 = r74_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR74_PURPOSE_VOSTRO1() {
		return R74_PURPOSE_VOSTRO1;
	}
	public void setR74_PURPOSE_VOSTRO1(String r74_PURPOSE_VOSTRO1) {
		R74_PURPOSE_VOSTRO1 = r74_PURPOSE_VOSTRO1;
	}
	public String getR74_CURRENCY_VOSTRO1() {
		return R74_CURRENCY_VOSTRO1;
	}
	public void setR74_CURRENCY_VOSTRO1(String r74_CURRENCY_VOSTRO1) {
		R74_CURRENCY_VOSTRO1 = r74_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR74_AMOUNT_DEMAND_VOSTRO1() {
		return R74_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR74_AMOUNT_DEMAND_VOSTRO1(BigDecimal r74_AMOUNT_DEMAND_VOSTRO1) {
		R74_AMOUNT_DEMAND_VOSTRO1 = r74_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR74_AMOUNT_TIME_VOSTRO1() {
		return R74_AMOUNT_TIME_VOSTRO1;
	}
	public void setR74_AMOUNT_TIME_VOSTRO1(BigDecimal r74_AMOUNT_TIME_VOSTRO1) {
		R74_AMOUNT_TIME_VOSTRO1 = r74_AMOUNT_TIME_VOSTRO1;
	}
	public String getR75_NAME_OF_BANK_VOSTRO1() {
		return R75_NAME_OF_BANK_VOSTRO1;
	}
	public void setR75_NAME_OF_BANK_VOSTRO1(String r75_NAME_OF_BANK_VOSTRO1) {
		R75_NAME_OF_BANK_VOSTRO1 = r75_NAME_OF_BANK_VOSTRO1;
	}
	public String getR75_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R75_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR75_TYPE_OF_ACCOUNT_VOSTRO1(String r75_TYPE_OF_ACCOUNT_VOSTRO1) {
		R75_TYPE_OF_ACCOUNT_VOSTRO1 = r75_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR75_PURPOSE_VOSTRO1() {
		return R75_PURPOSE_VOSTRO1;
	}
	public void setR75_PURPOSE_VOSTRO1(String r75_PURPOSE_VOSTRO1) {
		R75_PURPOSE_VOSTRO1 = r75_PURPOSE_VOSTRO1;
	}
	public String getR75_CURRENCY_VOSTRO1() {
		return R75_CURRENCY_VOSTRO1;
	}
	public void setR75_CURRENCY_VOSTRO1(String r75_CURRENCY_VOSTRO1) {
		R75_CURRENCY_VOSTRO1 = r75_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR75_AMOUNT_DEMAND_VOSTRO1() {
		return R75_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR75_AMOUNT_DEMAND_VOSTRO1(BigDecimal r75_AMOUNT_DEMAND_VOSTRO1) {
		R75_AMOUNT_DEMAND_VOSTRO1 = r75_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR75_AMOUNT_TIME_VOSTRO1() {
		return R75_AMOUNT_TIME_VOSTRO1;
	}
	public void setR75_AMOUNT_TIME_VOSTRO1(BigDecimal r75_AMOUNT_TIME_VOSTRO1) {
		R75_AMOUNT_TIME_VOSTRO1 = r75_AMOUNT_TIME_VOSTRO1;
	}
	public String getR76_NAME_OF_BANK_VOSTRO1() {
		return R76_NAME_OF_BANK_VOSTRO1;
	}
	public void setR76_NAME_OF_BANK_VOSTRO1(String r76_NAME_OF_BANK_VOSTRO1) {
		R76_NAME_OF_BANK_VOSTRO1 = r76_NAME_OF_BANK_VOSTRO1;
	}
	public String getR76_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R76_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR76_TYPE_OF_ACCOUNT_VOSTRO1(String r76_TYPE_OF_ACCOUNT_VOSTRO1) {
		R76_TYPE_OF_ACCOUNT_VOSTRO1 = r76_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR76_PURPOSE_VOSTRO1() {
		return R76_PURPOSE_VOSTRO1;
	}
	public void setR76_PURPOSE_VOSTRO1(String r76_PURPOSE_VOSTRO1) {
		R76_PURPOSE_VOSTRO1 = r76_PURPOSE_VOSTRO1;
	}
	public String getR76_CURRENCY_VOSTRO1() {
		return R76_CURRENCY_VOSTRO1;
	}
	public void setR76_CURRENCY_VOSTRO1(String r76_CURRENCY_VOSTRO1) {
		R76_CURRENCY_VOSTRO1 = r76_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR76_AMOUNT_DEMAND_VOSTRO1() {
		return R76_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR76_AMOUNT_DEMAND_VOSTRO1(BigDecimal r76_AMOUNT_DEMAND_VOSTRO1) {
		R76_AMOUNT_DEMAND_VOSTRO1 = r76_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR76_AMOUNT_TIME_VOSTRO1() {
		return R76_AMOUNT_TIME_VOSTRO1;
	}
	public void setR76_AMOUNT_TIME_VOSTRO1(BigDecimal r76_AMOUNT_TIME_VOSTRO1) {
		R76_AMOUNT_TIME_VOSTRO1 = r76_AMOUNT_TIME_VOSTRO1;
	}
	public String getR77_NAME_OF_BANK_VOSTRO1() {
		return R77_NAME_OF_BANK_VOSTRO1;
	}
	public void setR77_NAME_OF_BANK_VOSTRO1(String r77_NAME_OF_BANK_VOSTRO1) {
		R77_NAME_OF_BANK_VOSTRO1 = r77_NAME_OF_BANK_VOSTRO1;
	}
	public String getR77_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R77_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR77_TYPE_OF_ACCOUNT_VOSTRO1(String r77_TYPE_OF_ACCOUNT_VOSTRO1) {
		R77_TYPE_OF_ACCOUNT_VOSTRO1 = r77_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR77_PURPOSE_VOSTRO1() {
		return R77_PURPOSE_VOSTRO1;
	}
	public void setR77_PURPOSE_VOSTRO1(String r77_PURPOSE_VOSTRO1) {
		R77_PURPOSE_VOSTRO1 = r77_PURPOSE_VOSTRO1;
	}
	public String getR77_CURRENCY_VOSTRO1() {
		return R77_CURRENCY_VOSTRO1;
	}
	public void setR77_CURRENCY_VOSTRO1(String r77_CURRENCY_VOSTRO1) {
		R77_CURRENCY_VOSTRO1 = r77_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR77_AMOUNT_DEMAND_VOSTRO1() {
		return R77_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR77_AMOUNT_DEMAND_VOSTRO1(BigDecimal r77_AMOUNT_DEMAND_VOSTRO1) {
		R77_AMOUNT_DEMAND_VOSTRO1 = r77_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR77_AMOUNT_TIME_VOSTRO1() {
		return R77_AMOUNT_TIME_VOSTRO1;
	}
	public void setR77_AMOUNT_TIME_VOSTRO1(BigDecimal r77_AMOUNT_TIME_VOSTRO1) {
		R77_AMOUNT_TIME_VOSTRO1 = r77_AMOUNT_TIME_VOSTRO1;
	}
	public String getR78_NAME_OF_BANK_VOSTRO1() {
		return R78_NAME_OF_BANK_VOSTRO1;
	}
	public void setR78_NAME_OF_BANK_VOSTRO1(String r78_NAME_OF_BANK_VOSTRO1) {
		R78_NAME_OF_BANK_VOSTRO1 = r78_NAME_OF_BANK_VOSTRO1;
	}
	public String getR78_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R78_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR78_TYPE_OF_ACCOUNT_VOSTRO1(String r78_TYPE_OF_ACCOUNT_VOSTRO1) {
		R78_TYPE_OF_ACCOUNT_VOSTRO1 = r78_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR78_PURPOSE_VOSTRO1() {
		return R78_PURPOSE_VOSTRO1;
	}
	public void setR78_PURPOSE_VOSTRO1(String r78_PURPOSE_VOSTRO1) {
		R78_PURPOSE_VOSTRO1 = r78_PURPOSE_VOSTRO1;
	}
	public String getR78_CURRENCY_VOSTRO1() {
		return R78_CURRENCY_VOSTRO1;
	}
	public void setR78_CURRENCY_VOSTRO1(String r78_CURRENCY_VOSTRO1) {
		R78_CURRENCY_VOSTRO1 = r78_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR78_AMOUNT_DEMAND_VOSTRO1() {
		return R78_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR78_AMOUNT_DEMAND_VOSTRO1(BigDecimal r78_AMOUNT_DEMAND_VOSTRO1) {
		R78_AMOUNT_DEMAND_VOSTRO1 = r78_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR78_AMOUNT_TIME_VOSTRO1() {
		return R78_AMOUNT_TIME_VOSTRO1;
	}
	public void setR78_AMOUNT_TIME_VOSTRO1(BigDecimal r78_AMOUNT_TIME_VOSTRO1) {
		R78_AMOUNT_TIME_VOSTRO1 = r78_AMOUNT_TIME_VOSTRO1;
	}
	public String getR79_NAME_OF_BANK_VOSTRO1() {
		return R79_NAME_OF_BANK_VOSTRO1;
	}
	public void setR79_NAME_OF_BANK_VOSTRO1(String r79_NAME_OF_BANK_VOSTRO1) {
		R79_NAME_OF_BANK_VOSTRO1 = r79_NAME_OF_BANK_VOSTRO1;
	}
	public String getR79_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R79_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR79_TYPE_OF_ACCOUNT_VOSTRO1(String r79_TYPE_OF_ACCOUNT_VOSTRO1) {
		R79_TYPE_OF_ACCOUNT_VOSTRO1 = r79_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR79_PURPOSE_VOSTRO1() {
		return R79_PURPOSE_VOSTRO1;
	}
	public void setR79_PURPOSE_VOSTRO1(String r79_PURPOSE_VOSTRO1) {
		R79_PURPOSE_VOSTRO1 = r79_PURPOSE_VOSTRO1;
	}
	public String getR79_CURRENCY_VOSTRO1() {
		return R79_CURRENCY_VOSTRO1;
	}
	public void setR79_CURRENCY_VOSTRO1(String r79_CURRENCY_VOSTRO1) {
		R79_CURRENCY_VOSTRO1 = r79_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR79_AMOUNT_DEMAND_VOSTRO1() {
		return R79_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR79_AMOUNT_DEMAND_VOSTRO1(BigDecimal r79_AMOUNT_DEMAND_VOSTRO1) {
		R79_AMOUNT_DEMAND_VOSTRO1 = r79_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR79_AMOUNT_TIME_VOSTRO1() {
		return R79_AMOUNT_TIME_VOSTRO1;
	}
	public void setR79_AMOUNT_TIME_VOSTRO1(BigDecimal r79_AMOUNT_TIME_VOSTRO1) {
		R79_AMOUNT_TIME_VOSTRO1 = r79_AMOUNT_TIME_VOSTRO1;
	}
	public String getR80_NAME_OF_BANK_VOSTRO1() {
		return R80_NAME_OF_BANK_VOSTRO1;
	}
	public void setR80_NAME_OF_BANK_VOSTRO1(String r80_NAME_OF_BANK_VOSTRO1) {
		R80_NAME_OF_BANK_VOSTRO1 = r80_NAME_OF_BANK_VOSTRO1;
	}
	public String getR80_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R80_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR80_TYPE_OF_ACCOUNT_VOSTRO1(String r80_TYPE_OF_ACCOUNT_VOSTRO1) {
		R80_TYPE_OF_ACCOUNT_VOSTRO1 = r80_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR80_PURPOSE_VOSTRO1() {
		return R80_PURPOSE_VOSTRO1;
	}
	public void setR80_PURPOSE_VOSTRO1(String r80_PURPOSE_VOSTRO1) {
		R80_PURPOSE_VOSTRO1 = r80_PURPOSE_VOSTRO1;
	}
	public String getR80_CURRENCY_VOSTRO1() {
		return R80_CURRENCY_VOSTRO1;
	}
	public void setR80_CURRENCY_VOSTRO1(String r80_CURRENCY_VOSTRO1) {
		R80_CURRENCY_VOSTRO1 = r80_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR80_AMOUNT_DEMAND_VOSTRO1() {
		return R80_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR80_AMOUNT_DEMAND_VOSTRO1(BigDecimal r80_AMOUNT_DEMAND_VOSTRO1) {
		R80_AMOUNT_DEMAND_VOSTRO1 = r80_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR80_AMOUNT_TIME_VOSTRO1() {
		return R80_AMOUNT_TIME_VOSTRO1;
	}
	public void setR80_AMOUNT_TIME_VOSTRO1(BigDecimal r80_AMOUNT_TIME_VOSTRO1) {
		R80_AMOUNT_TIME_VOSTRO1 = r80_AMOUNT_TIME_VOSTRO1;
	}
	public String getR81_NAME_OF_BANK_VOSTRO1() {
		return R81_NAME_OF_BANK_VOSTRO1;
	}
	public void setR81_NAME_OF_BANK_VOSTRO1(String r81_NAME_OF_BANK_VOSTRO1) {
		R81_NAME_OF_BANK_VOSTRO1 = r81_NAME_OF_BANK_VOSTRO1;
	}
	public String getR81_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R81_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR81_TYPE_OF_ACCOUNT_VOSTRO1(String r81_TYPE_OF_ACCOUNT_VOSTRO1) {
		R81_TYPE_OF_ACCOUNT_VOSTRO1 = r81_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR81_PURPOSE_VOSTRO1() {
		return R81_PURPOSE_VOSTRO1;
	}
	public void setR81_PURPOSE_VOSTRO1(String r81_PURPOSE_VOSTRO1) {
		R81_PURPOSE_VOSTRO1 = r81_PURPOSE_VOSTRO1;
	}
	public String getR81_CURRENCY_VOSTRO1() {
		return R81_CURRENCY_VOSTRO1;
	}
	public void setR81_CURRENCY_VOSTRO1(String r81_CURRENCY_VOSTRO1) {
		R81_CURRENCY_VOSTRO1 = r81_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR81_AMOUNT_DEMAND_VOSTRO1() {
		return R81_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR81_AMOUNT_DEMAND_VOSTRO1(BigDecimal r81_AMOUNT_DEMAND_VOSTRO1) {
		R81_AMOUNT_DEMAND_VOSTRO1 = r81_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR81_AMOUNT_TIME_VOSTRO1() {
		return R81_AMOUNT_TIME_VOSTRO1;
	}
	public void setR81_AMOUNT_TIME_VOSTRO1(BigDecimal r81_AMOUNT_TIME_VOSTRO1) {
		R81_AMOUNT_TIME_VOSTRO1 = r81_AMOUNT_TIME_VOSTRO1;
	}
	public String getR82_NAME_OF_BANK_VOSTRO1() {
		return R82_NAME_OF_BANK_VOSTRO1;
	}
	public void setR82_NAME_OF_BANK_VOSTRO1(String r82_NAME_OF_BANK_VOSTRO1) {
		R82_NAME_OF_BANK_VOSTRO1 = r82_NAME_OF_BANK_VOSTRO1;
	}
	public String getR82_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R82_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR82_TYPE_OF_ACCOUNT_VOSTRO1(String r82_TYPE_OF_ACCOUNT_VOSTRO1) {
		R82_TYPE_OF_ACCOUNT_VOSTRO1 = r82_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR82_PURPOSE_VOSTRO1() {
		return R82_PURPOSE_VOSTRO1;
	}
	public void setR82_PURPOSE_VOSTRO1(String r82_PURPOSE_VOSTRO1) {
		R82_PURPOSE_VOSTRO1 = r82_PURPOSE_VOSTRO1;
	}
	public String getR82_CURRENCY_VOSTRO1() {
		return R82_CURRENCY_VOSTRO1;
	}
	public void setR82_CURRENCY_VOSTRO1(String r82_CURRENCY_VOSTRO1) {
		R82_CURRENCY_VOSTRO1 = r82_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR82_AMOUNT_DEMAND_VOSTRO1() {
		return R82_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR82_AMOUNT_DEMAND_VOSTRO1(BigDecimal r82_AMOUNT_DEMAND_VOSTRO1) {
		R82_AMOUNT_DEMAND_VOSTRO1 = r82_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR82_AMOUNT_TIME_VOSTRO1() {
		return R82_AMOUNT_TIME_VOSTRO1;
	}
	public void setR82_AMOUNT_TIME_VOSTRO1(BigDecimal r82_AMOUNT_TIME_VOSTRO1) {
		R82_AMOUNT_TIME_VOSTRO1 = r82_AMOUNT_TIME_VOSTRO1;
	}
	public String getR83_NAME_OF_BANK_VOSTRO1() {
		return R83_NAME_OF_BANK_VOSTRO1;
	}
	public void setR83_NAME_OF_BANK_VOSTRO1(String r83_NAME_OF_BANK_VOSTRO1) {
		R83_NAME_OF_BANK_VOSTRO1 = r83_NAME_OF_BANK_VOSTRO1;
	}
	public String getR83_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R83_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR83_TYPE_OF_ACCOUNT_VOSTRO1(String r83_TYPE_OF_ACCOUNT_VOSTRO1) {
		R83_TYPE_OF_ACCOUNT_VOSTRO1 = r83_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR83_PURPOSE_VOSTRO1() {
		return R83_PURPOSE_VOSTRO1;
	}
	public void setR83_PURPOSE_VOSTRO1(String r83_PURPOSE_VOSTRO1) {
		R83_PURPOSE_VOSTRO1 = r83_PURPOSE_VOSTRO1;
	}
	public String getR83_CURRENCY_VOSTRO1() {
		return R83_CURRENCY_VOSTRO1;
	}
	public void setR83_CURRENCY_VOSTRO1(String r83_CURRENCY_VOSTRO1) {
		R83_CURRENCY_VOSTRO1 = r83_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR83_AMOUNT_DEMAND_VOSTRO1() {
		return R83_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR83_AMOUNT_DEMAND_VOSTRO1(BigDecimal r83_AMOUNT_DEMAND_VOSTRO1) {
		R83_AMOUNT_DEMAND_VOSTRO1 = r83_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR83_AMOUNT_TIME_VOSTRO1() {
		return R83_AMOUNT_TIME_VOSTRO1;
	}
	public void setR83_AMOUNT_TIME_VOSTRO1(BigDecimal r83_AMOUNT_TIME_VOSTRO1) {
		R83_AMOUNT_TIME_VOSTRO1 = r83_AMOUNT_TIME_VOSTRO1;
	}
	public String getR84_NAME_OF_BANK_VOSTRO1() {
		return R84_NAME_OF_BANK_VOSTRO1;
	}
	public void setR84_NAME_OF_BANK_VOSTRO1(String r84_NAME_OF_BANK_VOSTRO1) {
		R84_NAME_OF_BANK_VOSTRO1 = r84_NAME_OF_BANK_VOSTRO1;
	}
	public String getR84_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R84_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR84_TYPE_OF_ACCOUNT_VOSTRO1(String r84_TYPE_OF_ACCOUNT_VOSTRO1) {
		R84_TYPE_OF_ACCOUNT_VOSTRO1 = r84_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR84_PURPOSE_VOSTRO1() {
		return R84_PURPOSE_VOSTRO1;
	}
	public void setR84_PURPOSE_VOSTRO1(String r84_PURPOSE_VOSTRO1) {
		R84_PURPOSE_VOSTRO1 = r84_PURPOSE_VOSTRO1;
	}
	public String getR84_CURRENCY_VOSTRO1() {
		return R84_CURRENCY_VOSTRO1;
	}
	public void setR84_CURRENCY_VOSTRO1(String r84_CURRENCY_VOSTRO1) {
		R84_CURRENCY_VOSTRO1 = r84_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR84_AMOUNT_DEMAND_VOSTRO1() {
		return R84_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR84_AMOUNT_DEMAND_VOSTRO1(BigDecimal r84_AMOUNT_DEMAND_VOSTRO1) {
		R84_AMOUNT_DEMAND_VOSTRO1 = r84_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR84_AMOUNT_TIME_VOSTRO1() {
		return R84_AMOUNT_TIME_VOSTRO1;
	}
	public void setR84_AMOUNT_TIME_VOSTRO1(BigDecimal r84_AMOUNT_TIME_VOSTRO1) {
		R84_AMOUNT_TIME_VOSTRO1 = r84_AMOUNT_TIME_VOSTRO1;
	}
	public String getR85_NAME_OF_BANK_VOSTRO1() {
		return R85_NAME_OF_BANK_VOSTRO1;
	}
	public void setR85_NAME_OF_BANK_VOSTRO1(String r85_NAME_OF_BANK_VOSTRO1) {
		R85_NAME_OF_BANK_VOSTRO1 = r85_NAME_OF_BANK_VOSTRO1;
	}
	public String getR85_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R85_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR85_TYPE_OF_ACCOUNT_VOSTRO1(String r85_TYPE_OF_ACCOUNT_VOSTRO1) {
		R85_TYPE_OF_ACCOUNT_VOSTRO1 = r85_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR85_PURPOSE_VOSTRO1() {
		return R85_PURPOSE_VOSTRO1;
	}
	public void setR85_PURPOSE_VOSTRO1(String r85_PURPOSE_VOSTRO1) {
		R85_PURPOSE_VOSTRO1 = r85_PURPOSE_VOSTRO1;
	}
	public String getR85_CURRENCY_VOSTRO1() {
		return R85_CURRENCY_VOSTRO1;
	}
	public void setR85_CURRENCY_VOSTRO1(String r85_CURRENCY_VOSTRO1) {
		R85_CURRENCY_VOSTRO1 = r85_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR85_AMOUNT_DEMAND_VOSTRO1() {
		return R85_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR85_AMOUNT_DEMAND_VOSTRO1(BigDecimal r85_AMOUNT_DEMAND_VOSTRO1) {
		R85_AMOUNT_DEMAND_VOSTRO1 = r85_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR85_AMOUNT_TIME_VOSTRO1() {
		return R85_AMOUNT_TIME_VOSTRO1;
	}
	public void setR85_AMOUNT_TIME_VOSTRO1(BigDecimal r85_AMOUNT_TIME_VOSTRO1) {
		R85_AMOUNT_TIME_VOSTRO1 = r85_AMOUNT_TIME_VOSTRO1;
	}
	public String getR86_NAME_OF_BANK_VOSTRO1() {
		return R86_NAME_OF_BANK_VOSTRO1;
	}
	public void setR86_NAME_OF_BANK_VOSTRO1(String r86_NAME_OF_BANK_VOSTRO1) {
		R86_NAME_OF_BANK_VOSTRO1 = r86_NAME_OF_BANK_VOSTRO1;
	}
	public String getR86_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R86_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR86_TYPE_OF_ACCOUNT_VOSTRO1(String r86_TYPE_OF_ACCOUNT_VOSTRO1) {
		R86_TYPE_OF_ACCOUNT_VOSTRO1 = r86_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR86_PURPOSE_VOSTRO1() {
		return R86_PURPOSE_VOSTRO1;
	}
	public void setR86_PURPOSE_VOSTRO1(String r86_PURPOSE_VOSTRO1) {
		R86_PURPOSE_VOSTRO1 = r86_PURPOSE_VOSTRO1;
	}
	public String getR86_CURRENCY_VOSTRO1() {
		return R86_CURRENCY_VOSTRO1;
	}
	public void setR86_CURRENCY_VOSTRO1(String r86_CURRENCY_VOSTRO1) {
		R86_CURRENCY_VOSTRO1 = r86_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR86_AMOUNT_DEMAND_VOSTRO1() {
		return R86_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR86_AMOUNT_DEMAND_VOSTRO1(BigDecimal r86_AMOUNT_DEMAND_VOSTRO1) {
		R86_AMOUNT_DEMAND_VOSTRO1 = r86_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR86_AMOUNT_TIME_VOSTRO1() {
		return R86_AMOUNT_TIME_VOSTRO1;
	}
	public void setR86_AMOUNT_TIME_VOSTRO1(BigDecimal r86_AMOUNT_TIME_VOSTRO1) {
		R86_AMOUNT_TIME_VOSTRO1 = r86_AMOUNT_TIME_VOSTRO1;
	}
	public String getR87_NAME_OF_BANK_VOSTRO1() {
		return R87_NAME_OF_BANK_VOSTRO1;
	}
	public void setR87_NAME_OF_BANK_VOSTRO1(String r87_NAME_OF_BANK_VOSTRO1) {
		R87_NAME_OF_BANK_VOSTRO1 = r87_NAME_OF_BANK_VOSTRO1;
	}
	public String getR87_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R87_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR87_TYPE_OF_ACCOUNT_VOSTRO1(String r87_TYPE_OF_ACCOUNT_VOSTRO1) {
		R87_TYPE_OF_ACCOUNT_VOSTRO1 = r87_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR87_PURPOSE_VOSTRO1() {
		return R87_PURPOSE_VOSTRO1;
	}
	public void setR87_PURPOSE_VOSTRO1(String r87_PURPOSE_VOSTRO1) {
		R87_PURPOSE_VOSTRO1 = r87_PURPOSE_VOSTRO1;
	}
	public String getR87_CURRENCY_VOSTRO1() {
		return R87_CURRENCY_VOSTRO1;
	}
	public void setR87_CURRENCY_VOSTRO1(String r87_CURRENCY_VOSTRO1) {
		R87_CURRENCY_VOSTRO1 = r87_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR87_AMOUNT_DEMAND_VOSTRO1() {
		return R87_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR87_AMOUNT_DEMAND_VOSTRO1(BigDecimal r87_AMOUNT_DEMAND_VOSTRO1) {
		R87_AMOUNT_DEMAND_VOSTRO1 = r87_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR87_AMOUNT_TIME_VOSTRO1() {
		return R87_AMOUNT_TIME_VOSTRO1;
	}
	public void setR87_AMOUNT_TIME_VOSTRO1(BigDecimal r87_AMOUNT_TIME_VOSTRO1) {
		R87_AMOUNT_TIME_VOSTRO1 = r87_AMOUNT_TIME_VOSTRO1;
	}
	public String getR88_NAME_OF_BANK_VOSTRO1() {
		return R88_NAME_OF_BANK_VOSTRO1;
	}
	public void setR88_NAME_OF_BANK_VOSTRO1(String r88_NAME_OF_BANK_VOSTRO1) {
		R88_NAME_OF_BANK_VOSTRO1 = r88_NAME_OF_BANK_VOSTRO1;
	}
	public String getR88_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R88_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR88_TYPE_OF_ACCOUNT_VOSTRO1(String r88_TYPE_OF_ACCOUNT_VOSTRO1) {
		R88_TYPE_OF_ACCOUNT_VOSTRO1 = r88_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR88_PURPOSE_VOSTRO1() {
		return R88_PURPOSE_VOSTRO1;
	}
	public void setR88_PURPOSE_VOSTRO1(String r88_PURPOSE_VOSTRO1) {
		R88_PURPOSE_VOSTRO1 = r88_PURPOSE_VOSTRO1;
	}
	public String getR88_CURRENCY_VOSTRO1() {
		return R88_CURRENCY_VOSTRO1;
	}
	public void setR88_CURRENCY_VOSTRO1(String r88_CURRENCY_VOSTRO1) {
		R88_CURRENCY_VOSTRO1 = r88_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR88_AMOUNT_DEMAND_VOSTRO1() {
		return R88_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR88_AMOUNT_DEMAND_VOSTRO1(BigDecimal r88_AMOUNT_DEMAND_VOSTRO1) {
		R88_AMOUNT_DEMAND_VOSTRO1 = r88_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR88_AMOUNT_TIME_VOSTRO1() {
		return R88_AMOUNT_TIME_VOSTRO1;
	}
	public void setR88_AMOUNT_TIME_VOSTRO1(BigDecimal r88_AMOUNT_TIME_VOSTRO1) {
		R88_AMOUNT_TIME_VOSTRO1 = r88_AMOUNT_TIME_VOSTRO1;
	}
	public String getR89_NAME_OF_BANK_VOSTRO1() {
		return R89_NAME_OF_BANK_VOSTRO1;
	}
	public void setR89_NAME_OF_BANK_VOSTRO1(String r89_NAME_OF_BANK_VOSTRO1) {
		R89_NAME_OF_BANK_VOSTRO1 = r89_NAME_OF_BANK_VOSTRO1;
	}
	public String getR89_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R89_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR89_TYPE_OF_ACCOUNT_VOSTRO1(String r89_TYPE_OF_ACCOUNT_VOSTRO1) {
		R89_TYPE_OF_ACCOUNT_VOSTRO1 = r89_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR89_PURPOSE_VOSTRO1() {
		return R89_PURPOSE_VOSTRO1;
	}
	public void setR89_PURPOSE_VOSTRO1(String r89_PURPOSE_VOSTRO1) {
		R89_PURPOSE_VOSTRO1 = r89_PURPOSE_VOSTRO1;
	}
	public String getR89_CURRENCY_VOSTRO1() {
		return R89_CURRENCY_VOSTRO1;
	}
	public void setR89_CURRENCY_VOSTRO1(String r89_CURRENCY_VOSTRO1) {
		R89_CURRENCY_VOSTRO1 = r89_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR89_AMOUNT_DEMAND_VOSTRO1() {
		return R89_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR89_AMOUNT_DEMAND_VOSTRO1(BigDecimal r89_AMOUNT_DEMAND_VOSTRO1) {
		R89_AMOUNT_DEMAND_VOSTRO1 = r89_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR89_AMOUNT_TIME_VOSTRO1() {
		return R89_AMOUNT_TIME_VOSTRO1;
	}
	public void setR89_AMOUNT_TIME_VOSTRO1(BigDecimal r89_AMOUNT_TIME_VOSTRO1) {
		R89_AMOUNT_TIME_VOSTRO1 = r89_AMOUNT_TIME_VOSTRO1;
	}
	public String getR90_NAME_OF_BANK_VOSTRO1() {
		return R90_NAME_OF_BANK_VOSTRO1;
	}
	public void setR90_NAME_OF_BANK_VOSTRO1(String r90_NAME_OF_BANK_VOSTRO1) {
		R90_NAME_OF_BANK_VOSTRO1 = r90_NAME_OF_BANK_VOSTRO1;
	}
	public String getR90_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R90_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR90_TYPE_OF_ACCOUNT_VOSTRO1(String r90_TYPE_OF_ACCOUNT_VOSTRO1) {
		R90_TYPE_OF_ACCOUNT_VOSTRO1 = r90_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR90_PURPOSE_VOSTRO1() {
		return R90_PURPOSE_VOSTRO1;
	}
	public void setR90_PURPOSE_VOSTRO1(String r90_PURPOSE_VOSTRO1) {
		R90_PURPOSE_VOSTRO1 = r90_PURPOSE_VOSTRO1;
	}
	public String getR90_CURRENCY_VOSTRO1() {
		return R90_CURRENCY_VOSTRO1;
	}
	public void setR90_CURRENCY_VOSTRO1(String r90_CURRENCY_VOSTRO1) {
		R90_CURRENCY_VOSTRO1 = r90_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR90_AMOUNT_DEMAND_VOSTRO1() {
		return R90_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR90_AMOUNT_DEMAND_VOSTRO1(BigDecimal r90_AMOUNT_DEMAND_VOSTRO1) {
		R90_AMOUNT_DEMAND_VOSTRO1 = r90_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR90_AMOUNT_TIME_VOSTRO1() {
		return R90_AMOUNT_TIME_VOSTRO1;
	}
	public void setR90_AMOUNT_TIME_VOSTRO1(BigDecimal r90_AMOUNT_TIME_VOSTRO1) {
		R90_AMOUNT_TIME_VOSTRO1 = r90_AMOUNT_TIME_VOSTRO1;
	}
	public String getR91_NAME_OF_BANK_VOSTRO1() {
		return R91_NAME_OF_BANK_VOSTRO1;
	}
	public void setR91_NAME_OF_BANK_VOSTRO1(String r91_NAME_OF_BANK_VOSTRO1) {
		R91_NAME_OF_BANK_VOSTRO1 = r91_NAME_OF_BANK_VOSTRO1;
	}
	public String getR91_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R91_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR91_TYPE_OF_ACCOUNT_VOSTRO1(String r91_TYPE_OF_ACCOUNT_VOSTRO1) {
		R91_TYPE_OF_ACCOUNT_VOSTRO1 = r91_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR91_PURPOSE_VOSTRO1() {
		return R91_PURPOSE_VOSTRO1;
	}
	public void setR91_PURPOSE_VOSTRO1(String r91_PURPOSE_VOSTRO1) {
		R91_PURPOSE_VOSTRO1 = r91_PURPOSE_VOSTRO1;
	}
	public String getR91_CURRENCY_VOSTRO1() {
		return R91_CURRENCY_VOSTRO1;
	}
	public void setR91_CURRENCY_VOSTRO1(String r91_CURRENCY_VOSTRO1) {
		R91_CURRENCY_VOSTRO1 = r91_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR91_AMOUNT_DEMAND_VOSTRO1() {
		return R91_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR91_AMOUNT_DEMAND_VOSTRO1(BigDecimal r91_AMOUNT_DEMAND_VOSTRO1) {
		R91_AMOUNT_DEMAND_VOSTRO1 = r91_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR91_AMOUNT_TIME_VOSTRO1() {
		return R91_AMOUNT_TIME_VOSTRO1;
	}
	public void setR91_AMOUNT_TIME_VOSTRO1(BigDecimal r91_AMOUNT_TIME_VOSTRO1) {
		R91_AMOUNT_TIME_VOSTRO1 = r91_AMOUNT_TIME_VOSTRO1;
	}
	public String getR92_NAME_OF_BANK_VOSTRO1() {
		return R92_NAME_OF_BANK_VOSTRO1;
	}
	public void setR92_NAME_OF_BANK_VOSTRO1(String r92_NAME_OF_BANK_VOSTRO1) {
		R92_NAME_OF_BANK_VOSTRO1 = r92_NAME_OF_BANK_VOSTRO1;
	}
	public String getR92_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R92_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR92_TYPE_OF_ACCOUNT_VOSTRO1(String r92_TYPE_OF_ACCOUNT_VOSTRO1) {
		R92_TYPE_OF_ACCOUNT_VOSTRO1 = r92_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR92_PURPOSE_VOSTRO1() {
		return R92_PURPOSE_VOSTRO1;
	}
	public void setR92_PURPOSE_VOSTRO1(String r92_PURPOSE_VOSTRO1) {
		R92_PURPOSE_VOSTRO1 = r92_PURPOSE_VOSTRO1;
	}
	public String getR92_CURRENCY_VOSTRO1() {
		return R92_CURRENCY_VOSTRO1;
	}
	public void setR92_CURRENCY_VOSTRO1(String r92_CURRENCY_VOSTRO1) {
		R92_CURRENCY_VOSTRO1 = r92_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR92_AMOUNT_DEMAND_VOSTRO1() {
		return R92_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR92_AMOUNT_DEMAND_VOSTRO1(BigDecimal r92_AMOUNT_DEMAND_VOSTRO1) {
		R92_AMOUNT_DEMAND_VOSTRO1 = r92_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR92_AMOUNT_TIME_VOSTRO1() {
		return R92_AMOUNT_TIME_VOSTRO1;
	}
	public void setR92_AMOUNT_TIME_VOSTRO1(BigDecimal r92_AMOUNT_TIME_VOSTRO1) {
		R92_AMOUNT_TIME_VOSTRO1 = r92_AMOUNT_TIME_VOSTRO1;
	}
	public String getR93_NAME_OF_BANK_VOSTRO1() {
		return R93_NAME_OF_BANK_VOSTRO1;
	}
	public void setR93_NAME_OF_BANK_VOSTRO1(String r93_NAME_OF_BANK_VOSTRO1) {
		R93_NAME_OF_BANK_VOSTRO1 = r93_NAME_OF_BANK_VOSTRO1;
	}
	public String getR93_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R93_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR93_TYPE_OF_ACCOUNT_VOSTRO1(String r93_TYPE_OF_ACCOUNT_VOSTRO1) {
		R93_TYPE_OF_ACCOUNT_VOSTRO1 = r93_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR93_PURPOSE_VOSTRO1() {
		return R93_PURPOSE_VOSTRO1;
	}
	public void setR93_PURPOSE_VOSTRO1(String r93_PURPOSE_VOSTRO1) {
		R93_PURPOSE_VOSTRO1 = r93_PURPOSE_VOSTRO1;
	}
	public String getR93_CURRENCY_VOSTRO1() {
		return R93_CURRENCY_VOSTRO1;
	}
	public void setR93_CURRENCY_VOSTRO1(String r93_CURRENCY_VOSTRO1) {
		R93_CURRENCY_VOSTRO1 = r93_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR93_AMOUNT_DEMAND_VOSTRO1() {
		return R93_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR93_AMOUNT_DEMAND_VOSTRO1(BigDecimal r93_AMOUNT_DEMAND_VOSTRO1) {
		R93_AMOUNT_DEMAND_VOSTRO1 = r93_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR93_AMOUNT_TIME_VOSTRO1() {
		return R93_AMOUNT_TIME_VOSTRO1;
	}
	public void setR93_AMOUNT_TIME_VOSTRO1(BigDecimal r93_AMOUNT_TIME_VOSTRO1) {
		R93_AMOUNT_TIME_VOSTRO1 = r93_AMOUNT_TIME_VOSTRO1;
	}
	public String getR94_NAME_OF_BANK_VOSTRO1() {
		return R94_NAME_OF_BANK_VOSTRO1;
	}
	public void setR94_NAME_OF_BANK_VOSTRO1(String r94_NAME_OF_BANK_VOSTRO1) {
		R94_NAME_OF_BANK_VOSTRO1 = r94_NAME_OF_BANK_VOSTRO1;
	}
	public String getR94_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R94_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR94_TYPE_OF_ACCOUNT_VOSTRO1(String r94_TYPE_OF_ACCOUNT_VOSTRO1) {
		R94_TYPE_OF_ACCOUNT_VOSTRO1 = r94_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR94_PURPOSE_VOSTRO1() {
		return R94_PURPOSE_VOSTRO1;
	}
	public void setR94_PURPOSE_VOSTRO1(String r94_PURPOSE_VOSTRO1) {
		R94_PURPOSE_VOSTRO1 = r94_PURPOSE_VOSTRO1;
	}
	public String getR94_CURRENCY_VOSTRO1() {
		return R94_CURRENCY_VOSTRO1;
	}
	public void setR94_CURRENCY_VOSTRO1(String r94_CURRENCY_VOSTRO1) {
		R94_CURRENCY_VOSTRO1 = r94_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR94_AMOUNT_DEMAND_VOSTRO1() {
		return R94_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR94_AMOUNT_DEMAND_VOSTRO1(BigDecimal r94_AMOUNT_DEMAND_VOSTRO1) {
		R94_AMOUNT_DEMAND_VOSTRO1 = r94_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR94_AMOUNT_TIME_VOSTRO1() {
		return R94_AMOUNT_TIME_VOSTRO1;
	}
	public void setR94_AMOUNT_TIME_VOSTRO1(BigDecimal r94_AMOUNT_TIME_VOSTRO1) {
		R94_AMOUNT_TIME_VOSTRO1 = r94_AMOUNT_TIME_VOSTRO1;
	}
	public String getR95_NAME_OF_BANK_VOSTRO1() {
		return R95_NAME_OF_BANK_VOSTRO1;
	}
	public void setR95_NAME_OF_BANK_VOSTRO1(String r95_NAME_OF_BANK_VOSTRO1) {
		R95_NAME_OF_BANK_VOSTRO1 = r95_NAME_OF_BANK_VOSTRO1;
	}
	public String getR95_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R95_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR95_TYPE_OF_ACCOUNT_VOSTRO1(String r95_TYPE_OF_ACCOUNT_VOSTRO1) {
		R95_TYPE_OF_ACCOUNT_VOSTRO1 = r95_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR95_PURPOSE_VOSTRO1() {
		return R95_PURPOSE_VOSTRO1;
	}
	public void setR95_PURPOSE_VOSTRO1(String r95_PURPOSE_VOSTRO1) {
		R95_PURPOSE_VOSTRO1 = r95_PURPOSE_VOSTRO1;
	}
	public String getR95_CURRENCY_VOSTRO1() {
		return R95_CURRENCY_VOSTRO1;
	}
	public void setR95_CURRENCY_VOSTRO1(String r95_CURRENCY_VOSTRO1) {
		R95_CURRENCY_VOSTRO1 = r95_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR95_AMOUNT_DEMAND_VOSTRO1() {
		return R95_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR95_AMOUNT_DEMAND_VOSTRO1(BigDecimal r95_AMOUNT_DEMAND_VOSTRO1) {
		R95_AMOUNT_DEMAND_VOSTRO1 = r95_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR95_AMOUNT_TIME_VOSTRO1() {
		return R95_AMOUNT_TIME_VOSTRO1;
	}
	public void setR95_AMOUNT_TIME_VOSTRO1(BigDecimal r95_AMOUNT_TIME_VOSTRO1) {
		R95_AMOUNT_TIME_VOSTRO1 = r95_AMOUNT_TIME_VOSTRO1;
	}
	public String getR96_NAME_OF_BANK_VOSTRO1() {
		return R96_NAME_OF_BANK_VOSTRO1;
	}
	public void setR96_NAME_OF_BANK_VOSTRO1(String r96_NAME_OF_BANK_VOSTRO1) {
		R96_NAME_OF_BANK_VOSTRO1 = r96_NAME_OF_BANK_VOSTRO1;
	}
	public String getR96_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R96_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR96_TYPE_OF_ACCOUNT_VOSTRO1(String r96_TYPE_OF_ACCOUNT_VOSTRO1) {
		R96_TYPE_OF_ACCOUNT_VOSTRO1 = r96_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR96_PURPOSE_VOSTRO1() {
		return R96_PURPOSE_VOSTRO1;
	}
	public void setR96_PURPOSE_VOSTRO1(String r96_PURPOSE_VOSTRO1) {
		R96_PURPOSE_VOSTRO1 = r96_PURPOSE_VOSTRO1;
	}
	public String getR96_CURRENCY_VOSTRO1() {
		return R96_CURRENCY_VOSTRO1;
	}
	public void setR96_CURRENCY_VOSTRO1(String r96_CURRENCY_VOSTRO1) {
		R96_CURRENCY_VOSTRO1 = r96_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR96_AMOUNT_DEMAND_VOSTRO1() {
		return R96_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR96_AMOUNT_DEMAND_VOSTRO1(BigDecimal r96_AMOUNT_DEMAND_VOSTRO1) {
		R96_AMOUNT_DEMAND_VOSTRO1 = r96_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR96_AMOUNT_TIME_VOSTRO1() {
		return R96_AMOUNT_TIME_VOSTRO1;
	}
	public void setR96_AMOUNT_TIME_VOSTRO1(BigDecimal r96_AMOUNT_TIME_VOSTRO1) {
		R96_AMOUNT_TIME_VOSTRO1 = r96_AMOUNT_TIME_VOSTRO1;
	}
	public String getR97_NAME_OF_BANK_VOSTRO1() {
		return R97_NAME_OF_BANK_VOSTRO1;
	}
	public void setR97_NAME_OF_BANK_VOSTRO1(String r97_NAME_OF_BANK_VOSTRO1) {
		R97_NAME_OF_BANK_VOSTRO1 = r97_NAME_OF_BANK_VOSTRO1;
	}
	public String getR97_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R97_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR97_TYPE_OF_ACCOUNT_VOSTRO1(String r97_TYPE_OF_ACCOUNT_VOSTRO1) {
		R97_TYPE_OF_ACCOUNT_VOSTRO1 = r97_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR97_PURPOSE_VOSTRO1() {
		return R97_PURPOSE_VOSTRO1;
	}
	public void setR97_PURPOSE_VOSTRO1(String r97_PURPOSE_VOSTRO1) {
		R97_PURPOSE_VOSTRO1 = r97_PURPOSE_VOSTRO1;
	}
	public String getR97_CURRENCY_VOSTRO1() {
		return R97_CURRENCY_VOSTRO1;
	}
	public void setR97_CURRENCY_VOSTRO1(String r97_CURRENCY_VOSTRO1) {
		R97_CURRENCY_VOSTRO1 = r97_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR97_AMOUNT_DEMAND_VOSTRO1() {
		return R97_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR97_AMOUNT_DEMAND_VOSTRO1(BigDecimal r97_AMOUNT_DEMAND_VOSTRO1) {
		R97_AMOUNT_DEMAND_VOSTRO1 = r97_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR97_AMOUNT_TIME_VOSTRO1() {
		return R97_AMOUNT_TIME_VOSTRO1;
	}
	public void setR97_AMOUNT_TIME_VOSTRO1(BigDecimal r97_AMOUNT_TIME_VOSTRO1) {
		R97_AMOUNT_TIME_VOSTRO1 = r97_AMOUNT_TIME_VOSTRO1;
	}
	public String getR98_NAME_OF_BANK_VOSTRO1() {
		return R98_NAME_OF_BANK_VOSTRO1;
	}
	public void setR98_NAME_OF_BANK_VOSTRO1(String r98_NAME_OF_BANK_VOSTRO1) {
		R98_NAME_OF_BANK_VOSTRO1 = r98_NAME_OF_BANK_VOSTRO1;
	}
	public String getR98_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R98_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR98_TYPE_OF_ACCOUNT_VOSTRO1(String r98_TYPE_OF_ACCOUNT_VOSTRO1) {
		R98_TYPE_OF_ACCOUNT_VOSTRO1 = r98_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR98_PURPOSE_VOSTRO1() {
		return R98_PURPOSE_VOSTRO1;
	}
	public void setR98_PURPOSE_VOSTRO1(String r98_PURPOSE_VOSTRO1) {
		R98_PURPOSE_VOSTRO1 = r98_PURPOSE_VOSTRO1;
	}
	public String getR98_CURRENCY_VOSTRO1() {
		return R98_CURRENCY_VOSTRO1;
	}
	public void setR98_CURRENCY_VOSTRO1(String r98_CURRENCY_VOSTRO1) {
		R98_CURRENCY_VOSTRO1 = r98_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR98_AMOUNT_DEMAND_VOSTRO1() {
		return R98_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR98_AMOUNT_DEMAND_VOSTRO1(BigDecimal r98_AMOUNT_DEMAND_VOSTRO1) {
		R98_AMOUNT_DEMAND_VOSTRO1 = r98_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR98_AMOUNT_TIME_VOSTRO1() {
		return R98_AMOUNT_TIME_VOSTRO1;
	}
	public void setR98_AMOUNT_TIME_VOSTRO1(BigDecimal r98_AMOUNT_TIME_VOSTRO1) {
		R98_AMOUNT_TIME_VOSTRO1 = r98_AMOUNT_TIME_VOSTRO1;
	}
	public String getR99_NAME_OF_BANK_VOSTRO1() {
		return R99_NAME_OF_BANK_VOSTRO1;
	}
	public void setR99_NAME_OF_BANK_VOSTRO1(String r99_NAME_OF_BANK_VOSTRO1) {
		R99_NAME_OF_BANK_VOSTRO1 = r99_NAME_OF_BANK_VOSTRO1;
	}
	public String getR99_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R99_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR99_TYPE_OF_ACCOUNT_VOSTRO1(String r99_TYPE_OF_ACCOUNT_VOSTRO1) {
		R99_TYPE_OF_ACCOUNT_VOSTRO1 = r99_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR99_PURPOSE_VOSTRO1() {
		return R99_PURPOSE_VOSTRO1;
	}
	public void setR99_PURPOSE_VOSTRO1(String r99_PURPOSE_VOSTRO1) {
		R99_PURPOSE_VOSTRO1 = r99_PURPOSE_VOSTRO1;
	}
	public String getR99_CURRENCY_VOSTRO1() {
		return R99_CURRENCY_VOSTRO1;
	}
	public void setR99_CURRENCY_VOSTRO1(String r99_CURRENCY_VOSTRO1) {
		R99_CURRENCY_VOSTRO1 = r99_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR99_AMOUNT_DEMAND_VOSTRO1() {
		return R99_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR99_AMOUNT_DEMAND_VOSTRO1(BigDecimal r99_AMOUNT_DEMAND_VOSTRO1) {
		R99_AMOUNT_DEMAND_VOSTRO1 = r99_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR99_AMOUNT_TIME_VOSTRO1() {
		return R99_AMOUNT_TIME_VOSTRO1;
	}
	public void setR99_AMOUNT_TIME_VOSTRO1(BigDecimal r99_AMOUNT_TIME_VOSTRO1) {
		R99_AMOUNT_TIME_VOSTRO1 = r99_AMOUNT_TIME_VOSTRO1;
	}
	public String getR100_NAME_OF_BANK_VOSTRO1() {
		return R100_NAME_OF_BANK_VOSTRO1;
	}
	public void setR100_NAME_OF_BANK_VOSTRO1(String r100_NAME_OF_BANK_VOSTRO1) {
		R100_NAME_OF_BANK_VOSTRO1 = r100_NAME_OF_BANK_VOSTRO1;
	}
	public String getR100_TYPE_OF_ACCOUNT_VOSTRO1() {
		return R100_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public void setR100_TYPE_OF_ACCOUNT_VOSTRO1(String r100_TYPE_OF_ACCOUNT_VOSTRO1) {
		R100_TYPE_OF_ACCOUNT_VOSTRO1 = r100_TYPE_OF_ACCOUNT_VOSTRO1;
	}
	public String getR100_PURPOSE_VOSTRO1() {
		return R100_PURPOSE_VOSTRO1;
	}
	public void setR100_PURPOSE_VOSTRO1(String r100_PURPOSE_VOSTRO1) {
		R100_PURPOSE_VOSTRO1 = r100_PURPOSE_VOSTRO1;
	}
	public String getR100_CURRENCY_VOSTRO1() {
		return R100_CURRENCY_VOSTRO1;
	}
	public void setR100_CURRENCY_VOSTRO1(String r100_CURRENCY_VOSTRO1) {
		R100_CURRENCY_VOSTRO1 = r100_CURRENCY_VOSTRO1;
	}
	public BigDecimal getR100_AMOUNT_DEMAND_VOSTRO1() {
		return R100_AMOUNT_DEMAND_VOSTRO1;
	}
	public void setR100_AMOUNT_DEMAND_VOSTRO1(BigDecimal r100_AMOUNT_DEMAND_VOSTRO1) {
		R100_AMOUNT_DEMAND_VOSTRO1 = r100_AMOUNT_DEMAND_VOSTRO1;
	}
	public BigDecimal getR100_AMOUNT_TIME_VOSTRO1() {
		return R100_AMOUNT_TIME_VOSTRO1;
	}
	public void setR100_AMOUNT_TIME_VOSTRO1(BigDecimal r100_AMOUNT_TIME_VOSTRO1) {
		R100_AMOUNT_TIME_VOSTRO1 = r100_AMOUNT_TIME_VOSTRO1;
	}

}