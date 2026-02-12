package com.bornfire.brrs.entities;
import java.io.Serializable;
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
@Table(name = "BRRS_M_LARADV_RESUBTABLE_SUMMARY1")
@IdClass(M_LARADV_PK.class)
public class M_LARADV_Resub_Summary_Entity1 implements Serializable{
	private static final long serialVersionUID = 1L;
	// Fields for R10
    private String R10_NO_OF_GROUP;
    private String R10_NO_OF_CUSTOMER;
    private String R10_SECTOR_TYPE;
    private String R10_FACILITY_TYPE;
    private BigDecimal R10_ORIGINAL_AMOUNT;
    private BigDecimal R10_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R10_EFFECTIVE_DATE;
    private String R10_REPAYMENT_PERIOD;
    private String R10_PERFORMANCE_STATUS;
    private String R10_SECURITY;
    private String R10_BOARD_APPROVAL;
    private BigDecimal R10_INTEREST_RATE;
    private BigDecimal R10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R10_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R11
    private String R11_NO_OF_GROUP;
    private String R11_NO_OF_CUSTOMER;
    private String R11_SECTOR_TYPE;
    private String R11_FACILITY_TYPE;
    private BigDecimal R11_ORIGINAL_AMOUNT;
    private BigDecimal R11_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R11_EFFECTIVE_DATE;
    private String R11_REPAYMENT_PERIOD;
    private String R11_PERFORMANCE_STATUS;
    private String R11_SECURITY;
    private String R11_BOARD_APPROVAL;
    private BigDecimal R11_INTEREST_RATE;
    private BigDecimal R11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R11_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R12
    private String R12_NO_OF_GROUP;
    private String R12_NO_OF_CUSTOMER;
    private String R12_SECTOR_TYPE;
    private String R12_FACILITY_TYPE;
    private BigDecimal R12_ORIGINAL_AMOUNT;
    private BigDecimal R12_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R12_EFFECTIVE_DATE;
    private String R12_REPAYMENT_PERIOD;
    private String R12_PERFORMANCE_STATUS;
    private String R12_SECURITY;
    private String R12_BOARD_APPROVAL;
    private BigDecimal R12_INTEREST_RATE;
    private BigDecimal R12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R12_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R13
    private String R13_NO_OF_GROUP;
    private String R13_NO_OF_CUSTOMER;
    private String R13_SECTOR_TYPE;
    private String R13_FACILITY_TYPE;
    private BigDecimal R13_ORIGINAL_AMOUNT;
    private BigDecimal R13_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R13_EFFECTIVE_DATE;
    private String R13_REPAYMENT_PERIOD;
    private String R13_PERFORMANCE_STATUS;
    private String R13_SECURITY;
    private String R13_BOARD_APPROVAL;
    private BigDecimal R13_INTEREST_RATE;
    private BigDecimal R13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R13_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R14
    private String R14_NO_OF_GROUP;
    private String R14_NO_OF_CUSTOMER;
    private String R14_SECTOR_TYPE;
    private String R14_FACILITY_TYPE;
    private BigDecimal R14_ORIGINAL_AMOUNT;
    private BigDecimal R14_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R14_EFFECTIVE_DATE;
    private String R14_REPAYMENT_PERIOD;
    private String R14_PERFORMANCE_STATUS;
    private String R14_SECURITY;
    private String R14_BOARD_APPROVAL;
    private BigDecimal R14_INTEREST_RATE;
    private BigDecimal R14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R14_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R15
    private String R15_NO_OF_GROUP;
    private String R15_NO_OF_CUSTOMER;
    private String R15_SECTOR_TYPE;
    private String R15_FACILITY_TYPE;
    private BigDecimal R15_ORIGINAL_AMOUNT;
    private BigDecimal R15_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R15_EFFECTIVE_DATE;
    private String R15_REPAYMENT_PERIOD;
    private String R15_PERFORMANCE_STATUS;
    private String R15_SECURITY;
    private String R15_BOARD_APPROVAL;
    private BigDecimal R15_INTEREST_RATE;
    private BigDecimal R15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R15_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R16
    private String R16_NO_OF_GROUP;
    private String R16_NO_OF_CUSTOMER;
    private String R16_SECTOR_TYPE;
    private String R16_FACILITY_TYPE;
    private BigDecimal R16_ORIGINAL_AMOUNT;
    private BigDecimal R16_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R16_EFFECTIVE_DATE;
    private String R16_REPAYMENT_PERIOD;
    private String R16_PERFORMANCE_STATUS;
    private String R16_SECURITY;
    private String R16_BOARD_APPROVAL;
    private BigDecimal R16_INTEREST_RATE;
    private BigDecimal R16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R16_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R17
    private String R17_NO_OF_GROUP;
    private String R17_NO_OF_CUSTOMER;
    private String R17_SECTOR_TYPE;
    private String R17_FACILITY_TYPE;
    private BigDecimal R17_ORIGINAL_AMOUNT;
    private BigDecimal R17_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R17_EFFECTIVE_DATE;
    private String R17_REPAYMENT_PERIOD;
    private String R17_PERFORMANCE_STATUS;
    private String R17_SECURITY;
    private String R17_BOARD_APPROVAL;
    private BigDecimal R17_INTEREST_RATE;
    private BigDecimal R17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R17_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R18
    private String R18_NO_OF_GROUP;
    private String R18_NO_OF_CUSTOMER;
    private String R18_SECTOR_TYPE;
    private String R18_FACILITY_TYPE;
    private BigDecimal R18_ORIGINAL_AMOUNT;
    private BigDecimal R18_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R18_EFFECTIVE_DATE;
    private String R18_REPAYMENT_PERIOD;
    private String R18_PERFORMANCE_STATUS;
    private String R18_SECURITY;
    private String R18_BOARD_APPROVAL;
    private BigDecimal R18_INTEREST_RATE;
    private BigDecimal R18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R18_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R19
    private String R19_NO_OF_GROUP;
    private String R19_NO_OF_CUSTOMER;
    private String R19_SECTOR_TYPE;
    private String R19_FACILITY_TYPE;
    private BigDecimal R19_ORIGINAL_AMOUNT;
    private BigDecimal R19_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R19_EFFECTIVE_DATE;
    private String R19_REPAYMENT_PERIOD;
    private String R19_PERFORMANCE_STATUS;
    private String R19_SECURITY;
    private String R19_BOARD_APPROVAL;
    private BigDecimal R19_INTEREST_RATE;
    private BigDecimal R19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R19_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R20
    private String R20_NO_OF_GROUP;
    private String R20_NO_OF_CUSTOMER;
    private String R20_SECTOR_TYPE;
    private String R20_FACILITY_TYPE;
    private BigDecimal R20_ORIGINAL_AMOUNT;
    private BigDecimal R20_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R20_EFFECTIVE_DATE;
    private String R20_REPAYMENT_PERIOD;
    private String R20_PERFORMANCE_STATUS;
    private String R20_SECURITY;
    private String R20_BOARD_APPROVAL;
    private BigDecimal R20_INTEREST_RATE;
    private BigDecimal R20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R20_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R21
    private String R21_NO_OF_GROUP;
    private String R21_NO_OF_CUSTOMER;
    private String R21_SECTOR_TYPE;
    private String R21_FACILITY_TYPE;
    private BigDecimal R21_ORIGINAL_AMOUNT;
    private BigDecimal R21_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R21_EFFECTIVE_DATE;
    private String R21_REPAYMENT_PERIOD;
    private String R21_PERFORMANCE_STATUS;
    private String R21_SECURITY;
    private String R21_BOARD_APPROVAL;
    private BigDecimal R21_INTEREST_RATE;
    private BigDecimal R21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R21_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R22
    private String R22_NO_OF_GROUP;
    private String R22_NO_OF_CUSTOMER;
    private String R22_SECTOR_TYPE;
    private String R22_FACILITY_TYPE;
    private BigDecimal R22_ORIGINAL_AMOUNT;
    private BigDecimal R22_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R22_EFFECTIVE_DATE;
    private String R22_REPAYMENT_PERIOD;
    private String R22_PERFORMANCE_STATUS;
    private String R22_SECURITY;
    private String R22_BOARD_APPROVAL;
    private BigDecimal R22_INTEREST_RATE;
    private BigDecimal R22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R22_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R23
    private String R23_NO_OF_GROUP;
    private String R23_NO_OF_CUSTOMER;
    private String R23_SECTOR_TYPE;
    private String R23_FACILITY_TYPE;
    private BigDecimal R23_ORIGINAL_AMOUNT;
    private BigDecimal R23_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R23_EFFECTIVE_DATE;
    private String R23_REPAYMENT_PERIOD;
    private String R23_PERFORMANCE_STATUS;
    private String R23_SECURITY;
    private String R23_BOARD_APPROVAL;
    private BigDecimal R23_INTEREST_RATE;
    private BigDecimal R23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R23_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R24
    private String R24_NO_OF_GROUP;
    private String R24_NO_OF_CUSTOMER;
    private String R24_SECTOR_TYPE;
    private String R24_FACILITY_TYPE;
    private BigDecimal R24_ORIGINAL_AMOUNT;
    private BigDecimal R24_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R24_EFFECTIVE_DATE;
    private String R24_REPAYMENT_PERIOD;
    private String R24_PERFORMANCE_STATUS;
    private String R24_SECURITY;
    private String R24_BOARD_APPROVAL;
    private BigDecimal R24_INTEREST_RATE;
    private BigDecimal R24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R24_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R25
    private String R25_NO_OF_GROUP;
    private String R25_NO_OF_CUSTOMER;
    private String R25_SECTOR_TYPE;
    private String R25_FACILITY_TYPE;
    private BigDecimal R25_ORIGINAL_AMOUNT;
    private BigDecimal R25_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R25_EFFECTIVE_DATE;
    private String R25_REPAYMENT_PERIOD;
    private String R25_PERFORMANCE_STATUS;
    private String R25_SECURITY;
    private String R25_BOARD_APPROVAL;
    private BigDecimal R25_INTEREST_RATE;
    private BigDecimal R25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R25_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R26
    private String R26_NO_OF_GROUP;
    private String R26_NO_OF_CUSTOMER;
    private String R26_SECTOR_TYPE;
    private String R26_FACILITY_TYPE;
    private BigDecimal R26_ORIGINAL_AMOUNT;
    private BigDecimal R26_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R26_EFFECTIVE_DATE;
    private String R26_REPAYMENT_PERIOD;
    private String R26_PERFORMANCE_STATUS;
    private String R26_SECURITY;
    private String R26_BOARD_APPROVAL;
    private BigDecimal R26_INTEREST_RATE;
    private BigDecimal R26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R26_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R27
    private String R27_NO_OF_GROUP;
    private String R27_NO_OF_CUSTOMER;
    private String R27_SECTOR_TYPE;
    private String R27_FACILITY_TYPE;
    private BigDecimal R27_ORIGINAL_AMOUNT;
    private BigDecimal R27_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R27_EFFECTIVE_DATE;
    private String R27_REPAYMENT_PERIOD;
    private String R27_PERFORMANCE_STATUS;
    private String R27_SECURITY;
    private String R27_BOARD_APPROVAL;
    private BigDecimal R27_INTEREST_RATE;
    private BigDecimal R27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R27_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R28
    private String R28_NO_OF_GROUP;
    private String R28_NO_OF_CUSTOMER;
    private String R28_SECTOR_TYPE;
    private String R28_FACILITY_TYPE;
    private BigDecimal R28_ORIGINAL_AMOUNT;
    private BigDecimal R28_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R28_EFFECTIVE_DATE;
    private String R28_REPAYMENT_PERIOD;
    private String R28_PERFORMANCE_STATUS;
    private String R28_SECURITY;
    private String R28_BOARD_APPROVAL;
    private BigDecimal R28_INTEREST_RATE;
    private BigDecimal R28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R28_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R29
    private String R29_NO_OF_GROUP;
    private String R29_NO_OF_CUSTOMER;
    private String R29_SECTOR_TYPE;
    private String R29_FACILITY_TYPE;
    private BigDecimal R29_ORIGINAL_AMOUNT;
    private BigDecimal R29_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R29_EFFECTIVE_DATE;
    private String R29_REPAYMENT_PERIOD;
    private String R29_PERFORMANCE_STATUS;
    private String R29_SECURITY;
    private String R29_BOARD_APPROVAL;
    private BigDecimal R29_INTEREST_RATE;
    private BigDecimal R29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R29_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R30
    private String R30_NO_OF_GROUP;
    private String R30_NO_OF_CUSTOMER;
    private String R30_SECTOR_TYPE;
    private String R30_FACILITY_TYPE;
    private BigDecimal R30_ORIGINAL_AMOUNT;
    private BigDecimal R30_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R30_EFFECTIVE_DATE;
    private String R30_REPAYMENT_PERIOD;
    private String R30_PERFORMANCE_STATUS;
    private String R30_SECURITY;
    private String R30_BOARD_APPROVAL;
    private BigDecimal R30_INTEREST_RATE;
    private BigDecimal R30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R30_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R31
    private String R31_NO_OF_GROUP;
    private String R31_NO_OF_CUSTOMER;
    private String R31_SECTOR_TYPE;
    private String R31_FACILITY_TYPE;
    private BigDecimal R31_ORIGINAL_AMOUNT;
    private BigDecimal R31_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R31_EFFECTIVE_DATE;
    private String R31_REPAYMENT_PERIOD;
    private String R31_PERFORMANCE_STATUS;
    private String R31_SECURITY;
    private String R31_BOARD_APPROVAL;
    private BigDecimal R31_INTEREST_RATE;
    private BigDecimal R31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R31_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R32
    private String R32_NO_OF_GROUP;
    private String R32_NO_OF_CUSTOMER;
    private String R32_SECTOR_TYPE;
    private String R32_FACILITY_TYPE;
    private BigDecimal R32_ORIGINAL_AMOUNT;
    private BigDecimal R32_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R32_EFFECTIVE_DATE;
    private String R32_REPAYMENT_PERIOD;
    private String R32_PERFORMANCE_STATUS;
    private String R32_SECURITY;
    private String R32_BOARD_APPROVAL;
    private BigDecimal R32_INTEREST_RATE;
    private BigDecimal R32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R32_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R33
    private String R33_NO_OF_GROUP;
    private String R33_NO_OF_CUSTOMER;
    private String R33_SECTOR_TYPE;
    private String R33_FACILITY_TYPE;
    private BigDecimal R33_ORIGINAL_AMOUNT;
    private BigDecimal R33_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R33_EFFECTIVE_DATE;
    private String R33_REPAYMENT_PERIOD;
    private String R33_PERFORMANCE_STATUS;
    private String R33_SECURITY;
    private String R33_BOARD_APPROVAL;
    private BigDecimal R33_INTEREST_RATE;
    private BigDecimal R33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R33_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R34
    private String R34_NO_OF_GROUP;
    private String R34_NO_OF_CUSTOMER;
    private String R34_SECTOR_TYPE;
    private String R34_FACILITY_TYPE;
    private BigDecimal R34_ORIGINAL_AMOUNT;
    private BigDecimal R34_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R34_EFFECTIVE_DATE;
    private String R34_REPAYMENT_PERIOD;
    private String R34_PERFORMANCE_STATUS;
    private String R34_SECURITY;
    private String R34_BOARD_APPROVAL;
    private BigDecimal R34_INTEREST_RATE;
    private BigDecimal R34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R34_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R35
    private String R35_NO_OF_GROUP;
    private String R35_NO_OF_CUSTOMER;
    private String R35_SECTOR_TYPE;
    private String R35_FACILITY_TYPE;
    private BigDecimal R35_ORIGINAL_AMOUNT;
    private BigDecimal R35_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R35_EFFECTIVE_DATE;
    private String R35_REPAYMENT_PERIOD;
    private String R35_PERFORMANCE_STATUS;
    private String R35_SECURITY;
    private String R35_BOARD_APPROVAL;
    private BigDecimal R35_INTEREST_RATE;
    private BigDecimal R35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R35_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R36
    private String R36_NO_OF_GROUP;
    private String R36_NO_OF_CUSTOMER;
    private String R36_SECTOR_TYPE;
    private String R36_FACILITY_TYPE;
    private BigDecimal R36_ORIGINAL_AMOUNT;
    private BigDecimal R36_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R36_EFFECTIVE_DATE;
    private String R36_REPAYMENT_PERIOD;
    private String R36_PERFORMANCE_STATUS;
    private String R36_SECURITY;
    private String R36_BOARD_APPROVAL;
    private BigDecimal R36_INTEREST_RATE;
    private BigDecimal R36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R36_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R37
    private String R37_NO_OF_GROUP;
    private String R37_NO_OF_CUSTOMER;
    private String R37_SECTOR_TYPE;
    private String R37_FACILITY_TYPE;
    private BigDecimal R37_ORIGINAL_AMOUNT;
    private BigDecimal R37_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R37_EFFECTIVE_DATE;
    private String R37_REPAYMENT_PERIOD;
    private String R37_PERFORMANCE_STATUS;
    private String R37_SECURITY;
    private String R37_BOARD_APPROVAL;
    private BigDecimal R37_INTEREST_RATE;
    private BigDecimal R37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R37_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R38
    private String R38_NO_OF_GROUP;
    private String R38_NO_OF_CUSTOMER;
    private String R38_SECTOR_TYPE;
    private String R38_FACILITY_TYPE;
    private BigDecimal R38_ORIGINAL_AMOUNT;
    private BigDecimal R38_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R38_EFFECTIVE_DATE;
    private String R38_REPAYMENT_PERIOD;
    private String R38_PERFORMANCE_STATUS;
    private String R38_SECURITY;
    private String R38_BOARD_APPROVAL;
    private BigDecimal R38_INTEREST_RATE;
    private BigDecimal R38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R38_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R39
    private String R39_NO_OF_GROUP;
    private String R39_NO_OF_CUSTOMER;
    private String R39_SECTOR_TYPE;
    private String R39_FACILITY_TYPE;
    private BigDecimal R39_ORIGINAL_AMOUNT;
    private BigDecimal R39_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R39_EFFECTIVE_DATE;
    private String R39_REPAYMENT_PERIOD;
    private String R39_PERFORMANCE_STATUS;
    private String R39_SECURITY;
    private String R39_BOARD_APPROVAL;
    private BigDecimal R39_INTEREST_RATE;
    private BigDecimal R39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R39_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R40
    private String R40_NO_OF_GROUP;
    private String R40_NO_OF_CUSTOMER;
    private String R40_SECTOR_TYPE;
    private String R40_FACILITY_TYPE;
    private BigDecimal R40_ORIGINAL_AMOUNT;
    private BigDecimal R40_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R40_EFFECTIVE_DATE;
    private String R40_REPAYMENT_PERIOD;
    private String R40_PERFORMANCE_STATUS;
    private String R40_SECURITY;
    private String R40_BOARD_APPROVAL;
    private BigDecimal R40_INTEREST_RATE;
    private BigDecimal R40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R40_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R41
    private String R41_NO_OF_GROUP;
    private String R41_NO_OF_CUSTOMER;
    private String R41_SECTOR_TYPE;
    private String R41_FACILITY_TYPE;
    private BigDecimal R41_ORIGINAL_AMOUNT;
    private BigDecimal R41_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R41_EFFECTIVE_DATE;
    private String R41_REPAYMENT_PERIOD;
    private String R41_PERFORMANCE_STATUS;
    private String R41_SECURITY;
    private String R41_BOARD_APPROVAL;
    private BigDecimal R41_INTEREST_RATE;
    private BigDecimal R41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R41_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R42
    private String R42_NO_OF_GROUP;
    private String R42_NO_OF_CUSTOMER;
    private String R42_SECTOR_TYPE;
    private String R42_FACILITY_TYPE;
    private BigDecimal R42_ORIGINAL_AMOUNT;
    private BigDecimal R42_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R42_EFFECTIVE_DATE;
    private String R42_REPAYMENT_PERIOD;
    private String R42_PERFORMANCE_STATUS;
    private String R42_SECURITY;
    private String R42_BOARD_APPROVAL;
    private BigDecimal R42_INTEREST_RATE;
    private BigDecimal R42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R42_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R43
    private String R43_NO_OF_GROUP;
    private String R43_NO_OF_CUSTOMER;
    private String R43_SECTOR_TYPE;
    private String R43_FACILITY_TYPE;
    private BigDecimal R43_ORIGINAL_AMOUNT;
    private BigDecimal R43_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R43_EFFECTIVE_DATE;
    private String R43_REPAYMENT_PERIOD;
    private String R43_PERFORMANCE_STATUS;
    private String R43_SECURITY;
    private String R43_BOARD_APPROVAL;
    private BigDecimal R43_INTEREST_RATE;
    private BigDecimal R43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R43_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R44
    private String R44_NO_OF_GROUP;
    private String R44_NO_OF_CUSTOMER;
    private String R44_SECTOR_TYPE;
    private String R44_FACILITY_TYPE;
    private BigDecimal R44_ORIGINAL_AMOUNT;
    private BigDecimal R44_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R44_EFFECTIVE_DATE;
    private String R44_REPAYMENT_PERIOD;
    private String R44_PERFORMANCE_STATUS;
    private String R44_SECURITY;
    private String R44_BOARD_APPROVAL;
    private BigDecimal R44_INTEREST_RATE;
    private BigDecimal R44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R44_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R45
    private String R45_NO_OF_GROUP;
    private String R45_NO_OF_CUSTOMER;
    private String R45_SECTOR_TYPE;
    private String R45_FACILITY_TYPE;
    private BigDecimal R45_ORIGINAL_AMOUNT;
    private BigDecimal R45_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R45_EFFECTIVE_DATE;
    private String R45_REPAYMENT_PERIOD;
    private String R45_PERFORMANCE_STATUS;
    private String R45_SECURITY;
    private String R45_BOARD_APPROVAL;
    private BigDecimal R45_INTEREST_RATE;
    private BigDecimal R45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R45_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R46
    private String R46_NO_OF_GROUP;
    private String R46_NO_OF_CUSTOMER;
    private String R46_SECTOR_TYPE;
    private String R46_FACILITY_TYPE;
    private BigDecimal R46_ORIGINAL_AMOUNT;
    private BigDecimal R46_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R46_EFFECTIVE_DATE;
    private String R46_REPAYMENT_PERIOD;
    private String R46_PERFORMANCE_STATUS;
    private String R46_SECURITY;
    private String R46_BOARD_APPROVAL;
    private BigDecimal R46_INTEREST_RATE;
    private BigDecimal R46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R46_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R47
    private String R47_NO_OF_GROUP;
    private String R47_NO_OF_CUSTOMER;
    private String R47_SECTOR_TYPE;
    private String R47_FACILITY_TYPE;
    private BigDecimal R47_ORIGINAL_AMOUNT;
    private BigDecimal R47_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R47_EFFECTIVE_DATE;
    private String R47_REPAYMENT_PERIOD;
    private String R47_PERFORMANCE_STATUS;
    private String R47_SECURITY;
    private String R47_BOARD_APPROVAL;
    private BigDecimal R47_INTEREST_RATE;
    private BigDecimal R47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R47_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R48
    private String R48_NO_OF_GROUP;
    private String R48_NO_OF_CUSTOMER;
    private String R48_SECTOR_TYPE;
    private String R48_FACILITY_TYPE;
    private BigDecimal R48_ORIGINAL_AMOUNT;
    private BigDecimal R48_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R48_EFFECTIVE_DATE;
    private String R48_REPAYMENT_PERIOD;
    private String R48_PERFORMANCE_STATUS;
    private String R48_SECURITY;
    private String R48_BOARD_APPROVAL;
    private BigDecimal R48_INTEREST_RATE;
    private BigDecimal R48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R48_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R49
    private String R49_NO_OF_GROUP;
    private String R49_NO_OF_CUSTOMER;
    private String R49_SECTOR_TYPE;
    private String R49_FACILITY_TYPE;
    private BigDecimal R49_ORIGINAL_AMOUNT;
    private BigDecimal R49_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R49_EFFECTIVE_DATE;
    private String R49_REPAYMENT_PERIOD;
    private String R49_PERFORMANCE_STATUS;
    private String R49_SECURITY;
    private String R49_BOARD_APPROVAL;
    private BigDecimal R49_INTEREST_RATE;
    private BigDecimal R49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R49_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R50
    private String R50_NO_OF_GROUP;
    private String R50_NO_OF_CUSTOMER;
    private String R50_SECTOR_TYPE;
    private String R50_FACILITY_TYPE;
    private BigDecimal R50_ORIGINAL_AMOUNT;
    private BigDecimal R50_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R50_EFFECTIVE_DATE;
    private String R50_REPAYMENT_PERIOD;
    private String R50_PERFORMANCE_STATUS;
    private String R50_SECURITY;
    private String R50_BOARD_APPROVAL;
    private BigDecimal R50_INTEREST_RATE;
    private BigDecimal R50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R50_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R51
    private String R51_NO_OF_GROUP;
    private String R51_NO_OF_CUSTOMER;
    private String R51_SECTOR_TYPE;
    private String R51_FACILITY_TYPE;
    private BigDecimal R51_ORIGINAL_AMOUNT;
    private BigDecimal R51_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R51_EFFECTIVE_DATE;
    private String R51_REPAYMENT_PERIOD;
    private String R51_PERFORMANCE_STATUS;
    private String R51_SECURITY;
    private String R51_BOARD_APPROVAL;
    private BigDecimal R51_INTEREST_RATE;
    private BigDecimal R51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R51_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R52
    private String R52_NO_OF_GROUP;
    private String R52_NO_OF_CUSTOMER;
    private String R52_SECTOR_TYPE;
    private String R52_FACILITY_TYPE;
    private BigDecimal R52_ORIGINAL_AMOUNT;
    private BigDecimal R52_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R52_EFFECTIVE_DATE;
    private String R52_REPAYMENT_PERIOD;
    private String R52_PERFORMANCE_STATUS;
    private String R52_SECURITY;
    private String R52_BOARD_APPROVAL;
    private BigDecimal R52_INTEREST_RATE;
    private BigDecimal R52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R52_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R53
    private String R53_NO_OF_GROUP;
    private String R53_NO_OF_CUSTOMER;
    private String R53_SECTOR_TYPE;
    private String R53_FACILITY_TYPE;
    private BigDecimal R53_ORIGINAL_AMOUNT;
    private BigDecimal R53_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R53_EFFECTIVE_DATE;
    private String R53_REPAYMENT_PERIOD;
    private String R53_PERFORMANCE_STATUS;
    private String R53_SECURITY;
    private String R53_BOARD_APPROVAL;
    private BigDecimal R53_INTEREST_RATE;
    private BigDecimal R53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R53_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R54
    private String R54_NO_OF_GROUP;
    private String R54_NO_OF_CUSTOMER;
    private String R54_SECTOR_TYPE;
    private String R54_FACILITY_TYPE;
    private BigDecimal R54_ORIGINAL_AMOUNT;
    private BigDecimal R54_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R54_EFFECTIVE_DATE;
    private String R54_REPAYMENT_PERIOD;
    private String R54_PERFORMANCE_STATUS;
    private String R54_SECURITY;
    private String R54_BOARD_APPROVAL;
    private BigDecimal R54_INTEREST_RATE;
    private BigDecimal R54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R54_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R55
    private String R55_NO_OF_GROUP;
    private String R55_NO_OF_CUSTOMER;
    private String R55_SECTOR_TYPE;
    private String R55_FACILITY_TYPE;
    private BigDecimal R55_ORIGINAL_AMOUNT;
    private BigDecimal R55_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R55_EFFECTIVE_DATE;
    private String R55_REPAYMENT_PERIOD;
    private String R55_PERFORMANCE_STATUS;
    private String R55_SECURITY;
    private String R55_BOARD_APPROVAL;
    private BigDecimal R55_INTEREST_RATE;
    private BigDecimal R55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R55_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R56
    private String R56_NO_OF_GROUP;
    private String R56_NO_OF_CUSTOMER;
    private String R56_SECTOR_TYPE;
    private String R56_FACILITY_TYPE;
    private BigDecimal R56_ORIGINAL_AMOUNT;
    private BigDecimal R56_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R56_EFFECTIVE_DATE;
    private String R56_REPAYMENT_PERIOD;
    private String R56_PERFORMANCE_STATUS;
    private String R56_SECURITY;
    private String R56_BOARD_APPROVAL;
    private BigDecimal R56_INTEREST_RATE;
    private BigDecimal R56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R56_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R57
    private String R57_NO_OF_GROUP;
    private String R57_NO_OF_CUSTOMER;
    private String R57_SECTOR_TYPE;
    private String R57_FACILITY_TYPE;
    private BigDecimal R57_ORIGINAL_AMOUNT;
    private BigDecimal R57_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R57_EFFECTIVE_DATE;
    private String R57_REPAYMENT_PERIOD;
    private String R57_PERFORMANCE_STATUS;
    private String R57_SECURITY;
    private String R57_BOARD_APPROVAL;
    private BigDecimal R57_INTEREST_RATE;
    private BigDecimal R57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R57_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R58
    private String R58_NO_OF_GROUP;
    private String R58_NO_OF_CUSTOMER;
    private String R58_SECTOR_TYPE;
    private String R58_FACILITY_TYPE;
    private BigDecimal R58_ORIGINAL_AMOUNT;
    private BigDecimal R58_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R58_EFFECTIVE_DATE;
    private String R58_REPAYMENT_PERIOD;
    private String R58_PERFORMANCE_STATUS;
    private String R58_SECURITY;
    private String R58_BOARD_APPROVAL;
    private BigDecimal R58_INTEREST_RATE;
    private BigDecimal R58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R58_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R59
    private String R59_NO_OF_GROUP;
    private String R59_NO_OF_CUSTOMER;
    private String R59_SECTOR_TYPE;
    private String R59_FACILITY_TYPE;
    private BigDecimal R59_ORIGINAL_AMOUNT;
    private BigDecimal R59_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R59_EFFECTIVE_DATE;
    private String R59_REPAYMENT_PERIOD;
    private String R59_PERFORMANCE_STATUS;
    private String R59_SECURITY;
    private String R59_BOARD_APPROVAL;
    private BigDecimal R59_INTEREST_RATE;
    private BigDecimal R59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R59_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R60
    private String R60_NO_OF_GROUP;
    private String R60_NO_OF_CUSTOMER;
    private String R60_SECTOR_TYPE;
    private String R60_FACILITY_TYPE;
    private BigDecimal R60_ORIGINAL_AMOUNT;
    private BigDecimal R60_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R60_EFFECTIVE_DATE;
    private String R60_REPAYMENT_PERIOD;
    private String R60_PERFORMANCE_STATUS;
    private String R60_SECURITY;
    private String R60_BOARD_APPROVAL;
    private BigDecimal R60_INTEREST_RATE;
    private BigDecimal R60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R60_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R61
    private String R61_NO_OF_GROUP;
    private String R61_NO_OF_CUSTOMER;
    private String R61_SECTOR_TYPE;
    private String R61_FACILITY_TYPE;
    private BigDecimal R61_ORIGINAL_AMOUNT;
    private BigDecimal R61_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R61_EFFECTIVE_DATE;
    private String R61_REPAYMENT_PERIOD;
    private String R61_PERFORMANCE_STATUS;
    private String R61_SECURITY;
    private String R61_BOARD_APPROVAL;
    private BigDecimal R61_INTEREST_RATE;
    private BigDecimal R61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R61_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R62
    private String R62_NO_OF_GROUP;
    private String R62_NO_OF_CUSTOMER;
    private String R62_SECTOR_TYPE;
    private String R62_FACILITY_TYPE;
    private BigDecimal R62_ORIGINAL_AMOUNT;
    private BigDecimal R62_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R62_EFFECTIVE_DATE;
    private String R62_REPAYMENT_PERIOD;
    private String R62_PERFORMANCE_STATUS;
    private String R62_SECURITY;
    private String R62_BOARD_APPROVAL;
    private BigDecimal R62_INTEREST_RATE;
    private BigDecimal R62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R62_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R63
    private String R63_NO_OF_GROUP;
    private String R63_NO_OF_CUSTOMER;
    private String R63_SECTOR_TYPE;
    private String R63_FACILITY_TYPE;
    private BigDecimal R63_ORIGINAL_AMOUNT;
    private BigDecimal R63_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R63_EFFECTIVE_DATE;
    private String R63_REPAYMENT_PERIOD;
    private String R63_PERFORMANCE_STATUS;
    private String R63_SECURITY;
    private String R63_BOARD_APPROVAL;
    private BigDecimal R63_INTEREST_RATE;
    private BigDecimal R63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R63_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R64
    private String R64_NO_OF_GROUP;
    private String R64_NO_OF_CUSTOMER;
    private String R64_SECTOR_TYPE;
    private String R64_FACILITY_TYPE;
    private BigDecimal R64_ORIGINAL_AMOUNT;
    private BigDecimal R64_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R64_EFFECTIVE_DATE;
    private String R64_REPAYMENT_PERIOD;
    private String R64_PERFORMANCE_STATUS;
    private String R64_SECURITY;
    private String R64_BOARD_APPROVAL;
    private BigDecimal R64_INTEREST_RATE;
    private BigDecimal R64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R64_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R65
    private String R65_NO_OF_GROUP;
    private String R65_NO_OF_CUSTOMER;
    private String R65_SECTOR_TYPE;
    private String R65_FACILITY_TYPE;
    private BigDecimal R65_ORIGINAL_AMOUNT;
    private BigDecimal R65_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R65_EFFECTIVE_DATE;
    private String R65_REPAYMENT_PERIOD;
    private String R65_PERFORMANCE_STATUS;
    private String R65_SECURITY;
    private String R65_BOARD_APPROVAL;
    private BigDecimal R65_INTEREST_RATE;
    private BigDecimal R65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R65_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R66
    private String R66_NO_OF_GROUP;
    private String R66_NO_OF_CUSTOMER;
    private String R66_SECTOR_TYPE;
    private String R66_FACILITY_TYPE;
    private BigDecimal R66_ORIGINAL_AMOUNT;
    private BigDecimal R66_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R66_EFFECTIVE_DATE;
    private String R66_REPAYMENT_PERIOD;
    private String R66_PERFORMANCE_STATUS;
    private String R66_SECURITY;
    private String R66_BOARD_APPROVAL;
    private BigDecimal R66_INTEREST_RATE;
    private BigDecimal R66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R66_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R67
    private String R67_NO_OF_GROUP;
    private String R67_NO_OF_CUSTOMER;
    private String R67_SECTOR_TYPE;
    private String R67_FACILITY_TYPE;
    private BigDecimal R67_ORIGINAL_AMOUNT;
    private BigDecimal R67_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R67_EFFECTIVE_DATE;
    private String R67_REPAYMENT_PERIOD;
    private String R67_PERFORMANCE_STATUS;
    private String R67_SECURITY;
    private String R67_BOARD_APPROVAL;
    private BigDecimal R67_INTEREST_RATE;
    private BigDecimal R67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R67_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R68
    private String R68_NO_OF_GROUP;
    private String R68_NO_OF_CUSTOMER;
    private String R68_SECTOR_TYPE;
    private String R68_FACILITY_TYPE;
    private BigDecimal R68_ORIGINAL_AMOUNT;
    private BigDecimal R68_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R68_EFFECTIVE_DATE;
    private String R68_REPAYMENT_PERIOD;
    private String R68_PERFORMANCE_STATUS;
    private String R68_SECURITY;
    private String R68_BOARD_APPROVAL;
    private BigDecimal R68_INTEREST_RATE;
    private BigDecimal R68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R68_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R69
    private String R69_NO_OF_GROUP;
    private String R69_NO_OF_CUSTOMER;
    private String R69_SECTOR_TYPE;
    private String R69_FACILITY_TYPE;
    private BigDecimal R69_ORIGINAL_AMOUNT;
    private BigDecimal R69_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R69_EFFECTIVE_DATE;
    private String R69_REPAYMENT_PERIOD;
    private String R69_PERFORMANCE_STATUS;
    private String R69_SECURITY;
    private String R69_BOARD_APPROVAL;
    private BigDecimal R69_INTEREST_RATE;
    private BigDecimal R69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R69_LIMIT_PCT_UNIMPAIRED_CAP;

    // Fields for R70
    private String R70_NO_OF_GROUP;
    private String R70_NO_OF_CUSTOMER;
    private String R70_SECTOR_TYPE;
    private String R70_FACILITY_TYPE;
    private BigDecimal R70_ORIGINAL_AMOUNT;
    private BigDecimal R70_UTILISATION_OUTSTANDING_BAL;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R70_EFFECTIVE_DATE;
    private String R70_REPAYMENT_PERIOD;
    private String R70_PERFORMANCE_STATUS;
    private String R70_SECURITY;
    private String R70_BOARD_APPROVAL;
    private BigDecimal R70_INTEREST_RATE;
    private BigDecimal R70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
    private BigDecimal R70_LIMIT_PCT_UNIMPAIRED_CAP;
	

    @Id
    private Date report_date;
    private BigDecimal report_version;

    @Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;
	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	public String getR10_NO_OF_GROUP() {
		return R10_NO_OF_GROUP;
	}
	public void setR10_NO_OF_GROUP(String r10_NO_OF_GROUP) {
		R10_NO_OF_GROUP = r10_NO_OF_GROUP;
	}
	public String getR10_NO_OF_CUSTOMER() {
		return R10_NO_OF_CUSTOMER;
	}
	public void setR10_NO_OF_CUSTOMER(String r10_NO_OF_CUSTOMER) {
		R10_NO_OF_CUSTOMER = r10_NO_OF_CUSTOMER;
	}
	public String getR10_SECTOR_TYPE() {
		return R10_SECTOR_TYPE;
	}
	public void setR10_SECTOR_TYPE(String r10_SECTOR_TYPE) {
		R10_SECTOR_TYPE = r10_SECTOR_TYPE;
	}
	public String getR10_FACILITY_TYPE() {
		return R10_FACILITY_TYPE;
	}
	public void setR10_FACILITY_TYPE(String r10_FACILITY_TYPE) {
		R10_FACILITY_TYPE = r10_FACILITY_TYPE;
	}
	public BigDecimal getR10_ORIGINAL_AMOUNT() {
		return R10_ORIGINAL_AMOUNT;
	}
	public void setR10_ORIGINAL_AMOUNT(BigDecimal r10_ORIGINAL_AMOUNT) {
		R10_ORIGINAL_AMOUNT = r10_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR10_UTILISATION_OUTSTANDING_BAL() {
		return R10_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR10_UTILISATION_OUTSTANDING_BAL(BigDecimal r10_UTILISATION_OUTSTANDING_BAL) {
		R10_UTILISATION_OUTSTANDING_BAL = r10_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR10_EFFECTIVE_DATE() {
		return R10_EFFECTIVE_DATE;
	}
	public void setR10_EFFECTIVE_DATE(Date r10_EFFECTIVE_DATE) {
		R10_EFFECTIVE_DATE = r10_EFFECTIVE_DATE;
	}
	public String getR10_REPAYMENT_PERIOD() {
		return R10_REPAYMENT_PERIOD;
	}
	public void setR10_REPAYMENT_PERIOD(String r10_REPAYMENT_PERIOD) {
		R10_REPAYMENT_PERIOD = r10_REPAYMENT_PERIOD;
	}
	public String getR10_PERFORMANCE_STATUS() {
		return R10_PERFORMANCE_STATUS;
	}
	public void setR10_PERFORMANCE_STATUS(String r10_PERFORMANCE_STATUS) {
		R10_PERFORMANCE_STATUS = r10_PERFORMANCE_STATUS;
	}
	public String getR10_SECURITY() {
		return R10_SECURITY;
	}
	public void setR10_SECURITY(String r10_SECURITY) {
		R10_SECURITY = r10_SECURITY;
	}
	public String getR10_BOARD_APPROVAL() {
		return R10_BOARD_APPROVAL;
	}
	public void setR10_BOARD_APPROVAL(String r10_BOARD_APPROVAL) {
		R10_BOARD_APPROVAL = r10_BOARD_APPROVAL;
	}
	public BigDecimal getR10_INTEREST_RATE() {
		return R10_INTEREST_RATE;
	}
	public void setR10_INTEREST_RATE(BigDecimal r10_INTEREST_RATE) {
		R10_INTEREST_RATE = r10_INTEREST_RATE;
	}
	public BigDecimal getR10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r10_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR10_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R10_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR10_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r10_LIMIT_PCT_UNIMPAIRED_CAP) {
		R10_LIMIT_PCT_UNIMPAIRED_CAP = r10_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR11_NO_OF_GROUP() {
		return R11_NO_OF_GROUP;
	}
	public void setR11_NO_OF_GROUP(String r11_NO_OF_GROUP) {
		R11_NO_OF_GROUP = r11_NO_OF_GROUP;
	}
	public String getR11_NO_OF_CUSTOMER() {
		return R11_NO_OF_CUSTOMER;
	}
	public void setR11_NO_OF_CUSTOMER(String r11_NO_OF_CUSTOMER) {
		R11_NO_OF_CUSTOMER = r11_NO_OF_CUSTOMER;
	}
	public String getR11_SECTOR_TYPE() {
		return R11_SECTOR_TYPE;
	}
	public void setR11_SECTOR_TYPE(String r11_SECTOR_TYPE) {
		R11_SECTOR_TYPE = r11_SECTOR_TYPE;
	}
	public String getR11_FACILITY_TYPE() {
		return R11_FACILITY_TYPE;
	}
	public void setR11_FACILITY_TYPE(String r11_FACILITY_TYPE) {
		R11_FACILITY_TYPE = r11_FACILITY_TYPE;
	}
	public BigDecimal getR11_ORIGINAL_AMOUNT() {
		return R11_ORIGINAL_AMOUNT;
	}
	public void setR11_ORIGINAL_AMOUNT(BigDecimal r11_ORIGINAL_AMOUNT) {
		R11_ORIGINAL_AMOUNT = r11_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR11_UTILISATION_OUTSTANDING_BAL() {
		return R11_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR11_UTILISATION_OUTSTANDING_BAL(BigDecimal r11_UTILISATION_OUTSTANDING_BAL) {
		R11_UTILISATION_OUTSTANDING_BAL = r11_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR11_EFFECTIVE_DATE() {
		return R11_EFFECTIVE_DATE;
	}
	public void setR11_EFFECTIVE_DATE(Date r11_EFFECTIVE_DATE) {
		R11_EFFECTIVE_DATE = r11_EFFECTIVE_DATE;
	}
	public String getR11_REPAYMENT_PERIOD() {
		return R11_REPAYMENT_PERIOD;
	}
	public void setR11_REPAYMENT_PERIOD(String r11_REPAYMENT_PERIOD) {
		R11_REPAYMENT_PERIOD = r11_REPAYMENT_PERIOD;
	}
	public String getR11_PERFORMANCE_STATUS() {
		return R11_PERFORMANCE_STATUS;
	}
	public void setR11_PERFORMANCE_STATUS(String r11_PERFORMANCE_STATUS) {
		R11_PERFORMANCE_STATUS = r11_PERFORMANCE_STATUS;
	}
	public String getR11_SECURITY() {
		return R11_SECURITY;
	}
	public void setR11_SECURITY(String r11_SECURITY) {
		R11_SECURITY = r11_SECURITY;
	}
	public String getR11_BOARD_APPROVAL() {
		return R11_BOARD_APPROVAL;
	}
	public void setR11_BOARD_APPROVAL(String r11_BOARD_APPROVAL) {
		R11_BOARD_APPROVAL = r11_BOARD_APPROVAL;
	}
	public BigDecimal getR11_INTEREST_RATE() {
		return R11_INTEREST_RATE;
	}
	public void setR11_INTEREST_RATE(BigDecimal r11_INTEREST_RATE) {
		R11_INTEREST_RATE = r11_INTEREST_RATE;
	}
	public BigDecimal getR11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r11_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR11_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R11_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR11_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r11_LIMIT_PCT_UNIMPAIRED_CAP) {
		R11_LIMIT_PCT_UNIMPAIRED_CAP = r11_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR12_NO_OF_GROUP() {
		return R12_NO_OF_GROUP;
	}
	public void setR12_NO_OF_GROUP(String r12_NO_OF_GROUP) {
		R12_NO_OF_GROUP = r12_NO_OF_GROUP;
	}
	public String getR12_NO_OF_CUSTOMER() {
		return R12_NO_OF_CUSTOMER;
	}
	public void setR12_NO_OF_CUSTOMER(String r12_NO_OF_CUSTOMER) {
		R12_NO_OF_CUSTOMER = r12_NO_OF_CUSTOMER;
	}
	public String getR12_SECTOR_TYPE() {
		return R12_SECTOR_TYPE;
	}
	public void setR12_SECTOR_TYPE(String r12_SECTOR_TYPE) {
		R12_SECTOR_TYPE = r12_SECTOR_TYPE;
	}
	public String getR12_FACILITY_TYPE() {
		return R12_FACILITY_TYPE;
	}
	public void setR12_FACILITY_TYPE(String r12_FACILITY_TYPE) {
		R12_FACILITY_TYPE = r12_FACILITY_TYPE;
	}
	public BigDecimal getR12_ORIGINAL_AMOUNT() {
		return R12_ORIGINAL_AMOUNT;
	}
	public void setR12_ORIGINAL_AMOUNT(BigDecimal r12_ORIGINAL_AMOUNT) {
		R12_ORIGINAL_AMOUNT = r12_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR12_UTILISATION_OUTSTANDING_BAL() {
		return R12_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR12_UTILISATION_OUTSTANDING_BAL(BigDecimal r12_UTILISATION_OUTSTANDING_BAL) {
		R12_UTILISATION_OUTSTANDING_BAL = r12_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR12_EFFECTIVE_DATE() {
		return R12_EFFECTIVE_DATE;
	}
	public void setR12_EFFECTIVE_DATE(Date r12_EFFECTIVE_DATE) {
		R12_EFFECTIVE_DATE = r12_EFFECTIVE_DATE;
	}
	public String getR12_REPAYMENT_PERIOD() {
		return R12_REPAYMENT_PERIOD;
	}
	public void setR12_REPAYMENT_PERIOD(String r12_REPAYMENT_PERIOD) {
		R12_REPAYMENT_PERIOD = r12_REPAYMENT_PERIOD;
	}
	public String getR12_PERFORMANCE_STATUS() {
		return R12_PERFORMANCE_STATUS;
	}
	public void setR12_PERFORMANCE_STATUS(String r12_PERFORMANCE_STATUS) {
		R12_PERFORMANCE_STATUS = r12_PERFORMANCE_STATUS;
	}
	public String getR12_SECURITY() {
		return R12_SECURITY;
	}
	public void setR12_SECURITY(String r12_SECURITY) {
		R12_SECURITY = r12_SECURITY;
	}
	public String getR12_BOARD_APPROVAL() {
		return R12_BOARD_APPROVAL;
	}
	public void setR12_BOARD_APPROVAL(String r12_BOARD_APPROVAL) {
		R12_BOARD_APPROVAL = r12_BOARD_APPROVAL;
	}
	public BigDecimal getR12_INTEREST_RATE() {
		return R12_INTEREST_RATE;
	}
	public void setR12_INTEREST_RATE(BigDecimal r12_INTEREST_RATE) {
		R12_INTEREST_RATE = r12_INTEREST_RATE;
	}
	public BigDecimal getR12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r12_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR12_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R12_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR12_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r12_LIMIT_PCT_UNIMPAIRED_CAP) {
		R12_LIMIT_PCT_UNIMPAIRED_CAP = r12_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR13_NO_OF_GROUP() {
		return R13_NO_OF_GROUP;
	}
	public void setR13_NO_OF_GROUP(String r13_NO_OF_GROUP) {
		R13_NO_OF_GROUP = r13_NO_OF_GROUP;
	}
	public String getR13_NO_OF_CUSTOMER() {
		return R13_NO_OF_CUSTOMER;
	}
	public void setR13_NO_OF_CUSTOMER(String r13_NO_OF_CUSTOMER) {
		R13_NO_OF_CUSTOMER = r13_NO_OF_CUSTOMER;
	}
	public String getR13_SECTOR_TYPE() {
		return R13_SECTOR_TYPE;
	}
	public void setR13_SECTOR_TYPE(String r13_SECTOR_TYPE) {
		R13_SECTOR_TYPE = r13_SECTOR_TYPE;
	}
	public String getR13_FACILITY_TYPE() {
		return R13_FACILITY_TYPE;
	}
	public void setR13_FACILITY_TYPE(String r13_FACILITY_TYPE) {
		R13_FACILITY_TYPE = r13_FACILITY_TYPE;
	}
	public BigDecimal getR13_ORIGINAL_AMOUNT() {
		return R13_ORIGINAL_AMOUNT;
	}
	public void setR13_ORIGINAL_AMOUNT(BigDecimal r13_ORIGINAL_AMOUNT) {
		R13_ORIGINAL_AMOUNT = r13_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR13_UTILISATION_OUTSTANDING_BAL() {
		return R13_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR13_UTILISATION_OUTSTANDING_BAL(BigDecimal r13_UTILISATION_OUTSTANDING_BAL) {
		R13_UTILISATION_OUTSTANDING_BAL = r13_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR13_EFFECTIVE_DATE() {
		return R13_EFFECTIVE_DATE;
	}
	public void setR13_EFFECTIVE_DATE(Date r13_EFFECTIVE_DATE) {
		R13_EFFECTIVE_DATE = r13_EFFECTIVE_DATE;
	}
	public String getR13_REPAYMENT_PERIOD() {
		return R13_REPAYMENT_PERIOD;
	}
	public void setR13_REPAYMENT_PERIOD(String r13_REPAYMENT_PERIOD) {
		R13_REPAYMENT_PERIOD = r13_REPAYMENT_PERIOD;
	}
	public String getR13_PERFORMANCE_STATUS() {
		return R13_PERFORMANCE_STATUS;
	}
	public void setR13_PERFORMANCE_STATUS(String r13_PERFORMANCE_STATUS) {
		R13_PERFORMANCE_STATUS = r13_PERFORMANCE_STATUS;
	}
	public String getR13_SECURITY() {
		return R13_SECURITY;
	}
	public void setR13_SECURITY(String r13_SECURITY) {
		R13_SECURITY = r13_SECURITY;
	}
	public String getR13_BOARD_APPROVAL() {
		return R13_BOARD_APPROVAL;
	}
	public void setR13_BOARD_APPROVAL(String r13_BOARD_APPROVAL) {
		R13_BOARD_APPROVAL = r13_BOARD_APPROVAL;
	}
	public BigDecimal getR13_INTEREST_RATE() {
		return R13_INTEREST_RATE;
	}
	public void setR13_INTEREST_RATE(BigDecimal r13_INTEREST_RATE) {
		R13_INTEREST_RATE = r13_INTEREST_RATE;
	}
	public BigDecimal getR13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r13_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR13_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R13_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR13_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r13_LIMIT_PCT_UNIMPAIRED_CAP) {
		R13_LIMIT_PCT_UNIMPAIRED_CAP = r13_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR14_NO_OF_GROUP() {
		return R14_NO_OF_GROUP;
	}
	public void setR14_NO_OF_GROUP(String r14_NO_OF_GROUP) {
		R14_NO_OF_GROUP = r14_NO_OF_GROUP;
	}
	public String getR14_NO_OF_CUSTOMER() {
		return R14_NO_OF_CUSTOMER;
	}
	public void setR14_NO_OF_CUSTOMER(String r14_NO_OF_CUSTOMER) {
		R14_NO_OF_CUSTOMER = r14_NO_OF_CUSTOMER;
	}
	public String getR14_SECTOR_TYPE() {
		return R14_SECTOR_TYPE;
	}
	public void setR14_SECTOR_TYPE(String r14_SECTOR_TYPE) {
		R14_SECTOR_TYPE = r14_SECTOR_TYPE;
	}
	public String getR14_FACILITY_TYPE() {
		return R14_FACILITY_TYPE;
	}
	public void setR14_FACILITY_TYPE(String r14_FACILITY_TYPE) {
		R14_FACILITY_TYPE = r14_FACILITY_TYPE;
	}
	public BigDecimal getR14_ORIGINAL_AMOUNT() {
		return R14_ORIGINAL_AMOUNT;
	}
	public void setR14_ORIGINAL_AMOUNT(BigDecimal r14_ORIGINAL_AMOUNT) {
		R14_ORIGINAL_AMOUNT = r14_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR14_UTILISATION_OUTSTANDING_BAL() {
		return R14_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR14_UTILISATION_OUTSTANDING_BAL(BigDecimal r14_UTILISATION_OUTSTANDING_BAL) {
		R14_UTILISATION_OUTSTANDING_BAL = r14_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR14_EFFECTIVE_DATE() {
		return R14_EFFECTIVE_DATE;
	}
	public void setR14_EFFECTIVE_DATE(Date r14_EFFECTIVE_DATE) {
		R14_EFFECTIVE_DATE = r14_EFFECTIVE_DATE;
	}
	public String getR14_REPAYMENT_PERIOD() {
		return R14_REPAYMENT_PERIOD;
	}
	public void setR14_REPAYMENT_PERIOD(String r14_REPAYMENT_PERIOD) {
		R14_REPAYMENT_PERIOD = r14_REPAYMENT_PERIOD;
	}
	public String getR14_PERFORMANCE_STATUS() {
		return R14_PERFORMANCE_STATUS;
	}
	public void setR14_PERFORMANCE_STATUS(String r14_PERFORMANCE_STATUS) {
		R14_PERFORMANCE_STATUS = r14_PERFORMANCE_STATUS;
	}
	public String getR14_SECURITY() {
		return R14_SECURITY;
	}
	public void setR14_SECURITY(String r14_SECURITY) {
		R14_SECURITY = r14_SECURITY;
	}
	public String getR14_BOARD_APPROVAL() {
		return R14_BOARD_APPROVAL;
	}
	public void setR14_BOARD_APPROVAL(String r14_BOARD_APPROVAL) {
		R14_BOARD_APPROVAL = r14_BOARD_APPROVAL;
	}
	public BigDecimal getR14_INTEREST_RATE() {
		return R14_INTEREST_RATE;
	}
	public void setR14_INTEREST_RATE(BigDecimal r14_INTEREST_RATE) {
		R14_INTEREST_RATE = r14_INTEREST_RATE;
	}
	public BigDecimal getR14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r14_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR14_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R14_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR14_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r14_LIMIT_PCT_UNIMPAIRED_CAP) {
		R14_LIMIT_PCT_UNIMPAIRED_CAP = r14_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR15_NO_OF_GROUP() {
		return R15_NO_OF_GROUP;
	}
	public void setR15_NO_OF_GROUP(String r15_NO_OF_GROUP) {
		R15_NO_OF_GROUP = r15_NO_OF_GROUP;
	}
	public String getR15_NO_OF_CUSTOMER() {
		return R15_NO_OF_CUSTOMER;
	}
	public void setR15_NO_OF_CUSTOMER(String r15_NO_OF_CUSTOMER) {
		R15_NO_OF_CUSTOMER = r15_NO_OF_CUSTOMER;
	}
	public String getR15_SECTOR_TYPE() {
		return R15_SECTOR_TYPE;
	}
	public void setR15_SECTOR_TYPE(String r15_SECTOR_TYPE) {
		R15_SECTOR_TYPE = r15_SECTOR_TYPE;
	}
	public String getR15_FACILITY_TYPE() {
		return R15_FACILITY_TYPE;
	}
	public void setR15_FACILITY_TYPE(String r15_FACILITY_TYPE) {
		R15_FACILITY_TYPE = r15_FACILITY_TYPE;
	}
	public BigDecimal getR15_ORIGINAL_AMOUNT() {
		return R15_ORIGINAL_AMOUNT;
	}
	public void setR15_ORIGINAL_AMOUNT(BigDecimal r15_ORIGINAL_AMOUNT) {
		R15_ORIGINAL_AMOUNT = r15_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR15_UTILISATION_OUTSTANDING_BAL() {
		return R15_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR15_UTILISATION_OUTSTANDING_BAL(BigDecimal r15_UTILISATION_OUTSTANDING_BAL) {
		R15_UTILISATION_OUTSTANDING_BAL = r15_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR15_EFFECTIVE_DATE() {
		return R15_EFFECTIVE_DATE;
	}
	public void setR15_EFFECTIVE_DATE(Date r15_EFFECTIVE_DATE) {
		R15_EFFECTIVE_DATE = r15_EFFECTIVE_DATE;
	}
	public String getR15_REPAYMENT_PERIOD() {
		return R15_REPAYMENT_PERIOD;
	}
	public void setR15_REPAYMENT_PERIOD(String r15_REPAYMENT_PERIOD) {
		R15_REPAYMENT_PERIOD = r15_REPAYMENT_PERIOD;
	}
	public String getR15_PERFORMANCE_STATUS() {
		return R15_PERFORMANCE_STATUS;
	}
	public void setR15_PERFORMANCE_STATUS(String r15_PERFORMANCE_STATUS) {
		R15_PERFORMANCE_STATUS = r15_PERFORMANCE_STATUS;
	}
	public String getR15_SECURITY() {
		return R15_SECURITY;
	}
	public void setR15_SECURITY(String r15_SECURITY) {
		R15_SECURITY = r15_SECURITY;
	}
	public String getR15_BOARD_APPROVAL() {
		return R15_BOARD_APPROVAL;
	}
	public void setR15_BOARD_APPROVAL(String r15_BOARD_APPROVAL) {
		R15_BOARD_APPROVAL = r15_BOARD_APPROVAL;
	}
	public BigDecimal getR15_INTEREST_RATE() {
		return R15_INTEREST_RATE;
	}
	public void setR15_INTEREST_RATE(BigDecimal r15_INTEREST_RATE) {
		R15_INTEREST_RATE = r15_INTEREST_RATE;
	}
	public BigDecimal getR15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r15_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR15_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R15_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR15_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r15_LIMIT_PCT_UNIMPAIRED_CAP) {
		R15_LIMIT_PCT_UNIMPAIRED_CAP = r15_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR16_NO_OF_GROUP() {
		return R16_NO_OF_GROUP;
	}
	public void setR16_NO_OF_GROUP(String r16_NO_OF_GROUP) {
		R16_NO_OF_GROUP = r16_NO_OF_GROUP;
	}
	public String getR16_NO_OF_CUSTOMER() {
		return R16_NO_OF_CUSTOMER;
	}
	public void setR16_NO_OF_CUSTOMER(String r16_NO_OF_CUSTOMER) {
		R16_NO_OF_CUSTOMER = r16_NO_OF_CUSTOMER;
	}
	public String getR16_SECTOR_TYPE() {
		return R16_SECTOR_TYPE;
	}
	public void setR16_SECTOR_TYPE(String r16_SECTOR_TYPE) {
		R16_SECTOR_TYPE = r16_SECTOR_TYPE;
	}
	public String getR16_FACILITY_TYPE() {
		return R16_FACILITY_TYPE;
	}
	public void setR16_FACILITY_TYPE(String r16_FACILITY_TYPE) {
		R16_FACILITY_TYPE = r16_FACILITY_TYPE;
	}
	public BigDecimal getR16_ORIGINAL_AMOUNT() {
		return R16_ORIGINAL_AMOUNT;
	}
	public void setR16_ORIGINAL_AMOUNT(BigDecimal r16_ORIGINAL_AMOUNT) {
		R16_ORIGINAL_AMOUNT = r16_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR16_UTILISATION_OUTSTANDING_BAL() {
		return R16_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR16_UTILISATION_OUTSTANDING_BAL(BigDecimal r16_UTILISATION_OUTSTANDING_BAL) {
		R16_UTILISATION_OUTSTANDING_BAL = r16_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR16_EFFECTIVE_DATE() {
		return R16_EFFECTIVE_DATE;
	}
	public void setR16_EFFECTIVE_DATE(Date r16_EFFECTIVE_DATE) {
		R16_EFFECTIVE_DATE = r16_EFFECTIVE_DATE;
	}
	public String getR16_REPAYMENT_PERIOD() {
		return R16_REPAYMENT_PERIOD;
	}
	public void setR16_REPAYMENT_PERIOD(String r16_REPAYMENT_PERIOD) {
		R16_REPAYMENT_PERIOD = r16_REPAYMENT_PERIOD;
	}
	public String getR16_PERFORMANCE_STATUS() {
		return R16_PERFORMANCE_STATUS;
	}
	public void setR16_PERFORMANCE_STATUS(String r16_PERFORMANCE_STATUS) {
		R16_PERFORMANCE_STATUS = r16_PERFORMANCE_STATUS;
	}
	public String getR16_SECURITY() {
		return R16_SECURITY;
	}
	public void setR16_SECURITY(String r16_SECURITY) {
		R16_SECURITY = r16_SECURITY;
	}
	public String getR16_BOARD_APPROVAL() {
		return R16_BOARD_APPROVAL;
	}
	public void setR16_BOARD_APPROVAL(String r16_BOARD_APPROVAL) {
		R16_BOARD_APPROVAL = r16_BOARD_APPROVAL;
	}
	public BigDecimal getR16_INTEREST_RATE() {
		return R16_INTEREST_RATE;
	}
	public void setR16_INTEREST_RATE(BigDecimal r16_INTEREST_RATE) {
		R16_INTEREST_RATE = r16_INTEREST_RATE;
	}
	public BigDecimal getR16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r16_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR16_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R16_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR16_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r16_LIMIT_PCT_UNIMPAIRED_CAP) {
		R16_LIMIT_PCT_UNIMPAIRED_CAP = r16_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR17_NO_OF_GROUP() {
		return R17_NO_OF_GROUP;
	}
	public void setR17_NO_OF_GROUP(String r17_NO_OF_GROUP) {
		R17_NO_OF_GROUP = r17_NO_OF_GROUP;
	}
	public String getR17_NO_OF_CUSTOMER() {
		return R17_NO_OF_CUSTOMER;
	}
	public void setR17_NO_OF_CUSTOMER(String r17_NO_OF_CUSTOMER) {
		R17_NO_OF_CUSTOMER = r17_NO_OF_CUSTOMER;
	}
	public String getR17_SECTOR_TYPE() {
		return R17_SECTOR_TYPE;
	}
	public void setR17_SECTOR_TYPE(String r17_SECTOR_TYPE) {
		R17_SECTOR_TYPE = r17_SECTOR_TYPE;
	}
	public String getR17_FACILITY_TYPE() {
		return R17_FACILITY_TYPE;
	}
	public void setR17_FACILITY_TYPE(String r17_FACILITY_TYPE) {
		R17_FACILITY_TYPE = r17_FACILITY_TYPE;
	}
	public BigDecimal getR17_ORIGINAL_AMOUNT() {
		return R17_ORIGINAL_AMOUNT;
	}
	public void setR17_ORIGINAL_AMOUNT(BigDecimal r17_ORIGINAL_AMOUNT) {
		R17_ORIGINAL_AMOUNT = r17_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR17_UTILISATION_OUTSTANDING_BAL() {
		return R17_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR17_UTILISATION_OUTSTANDING_BAL(BigDecimal r17_UTILISATION_OUTSTANDING_BAL) {
		R17_UTILISATION_OUTSTANDING_BAL = r17_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR17_EFFECTIVE_DATE() {
		return R17_EFFECTIVE_DATE;
	}
	public void setR17_EFFECTIVE_DATE(Date r17_EFFECTIVE_DATE) {
		R17_EFFECTIVE_DATE = r17_EFFECTIVE_DATE;
	}
	public String getR17_REPAYMENT_PERIOD() {
		return R17_REPAYMENT_PERIOD;
	}
	public void setR17_REPAYMENT_PERIOD(String r17_REPAYMENT_PERIOD) {
		R17_REPAYMENT_PERIOD = r17_REPAYMENT_PERIOD;
	}
	public String getR17_PERFORMANCE_STATUS() {
		return R17_PERFORMANCE_STATUS;
	}
	public void setR17_PERFORMANCE_STATUS(String r17_PERFORMANCE_STATUS) {
		R17_PERFORMANCE_STATUS = r17_PERFORMANCE_STATUS;
	}
	public String getR17_SECURITY() {
		return R17_SECURITY;
	}
	public void setR17_SECURITY(String r17_SECURITY) {
		R17_SECURITY = r17_SECURITY;
	}
	public String getR17_BOARD_APPROVAL() {
		return R17_BOARD_APPROVAL;
	}
	public void setR17_BOARD_APPROVAL(String r17_BOARD_APPROVAL) {
		R17_BOARD_APPROVAL = r17_BOARD_APPROVAL;
	}
	public BigDecimal getR17_INTEREST_RATE() {
		return R17_INTEREST_RATE;
	}
	public void setR17_INTEREST_RATE(BigDecimal r17_INTEREST_RATE) {
		R17_INTEREST_RATE = r17_INTEREST_RATE;
	}
	public BigDecimal getR17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r17_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR17_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R17_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR17_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r17_LIMIT_PCT_UNIMPAIRED_CAP) {
		R17_LIMIT_PCT_UNIMPAIRED_CAP = r17_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR18_NO_OF_GROUP() {
		return R18_NO_OF_GROUP;
	}
	public void setR18_NO_OF_GROUP(String r18_NO_OF_GROUP) {
		R18_NO_OF_GROUP = r18_NO_OF_GROUP;
	}
	public String getR18_NO_OF_CUSTOMER() {
		return R18_NO_OF_CUSTOMER;
	}
	public void setR18_NO_OF_CUSTOMER(String r18_NO_OF_CUSTOMER) {
		R18_NO_OF_CUSTOMER = r18_NO_OF_CUSTOMER;
	}
	public String getR18_SECTOR_TYPE() {
		return R18_SECTOR_TYPE;
	}
	public void setR18_SECTOR_TYPE(String r18_SECTOR_TYPE) {
		R18_SECTOR_TYPE = r18_SECTOR_TYPE;
	}
	public String getR18_FACILITY_TYPE() {
		return R18_FACILITY_TYPE;
	}
	public void setR18_FACILITY_TYPE(String r18_FACILITY_TYPE) {
		R18_FACILITY_TYPE = r18_FACILITY_TYPE;
	}
	public BigDecimal getR18_ORIGINAL_AMOUNT() {
		return R18_ORIGINAL_AMOUNT;
	}
	public void setR18_ORIGINAL_AMOUNT(BigDecimal r18_ORIGINAL_AMOUNT) {
		R18_ORIGINAL_AMOUNT = r18_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR18_UTILISATION_OUTSTANDING_BAL() {
		return R18_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR18_UTILISATION_OUTSTANDING_BAL(BigDecimal r18_UTILISATION_OUTSTANDING_BAL) {
		R18_UTILISATION_OUTSTANDING_BAL = r18_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR18_EFFECTIVE_DATE() {
		return R18_EFFECTIVE_DATE;
	}
	public void setR18_EFFECTIVE_DATE(Date r18_EFFECTIVE_DATE) {
		R18_EFFECTIVE_DATE = r18_EFFECTIVE_DATE;
	}
	public String getR18_REPAYMENT_PERIOD() {
		return R18_REPAYMENT_PERIOD;
	}
	public void setR18_REPAYMENT_PERIOD(String r18_REPAYMENT_PERIOD) {
		R18_REPAYMENT_PERIOD = r18_REPAYMENT_PERIOD;
	}
	public String getR18_PERFORMANCE_STATUS() {
		return R18_PERFORMANCE_STATUS;
	}
	public void setR18_PERFORMANCE_STATUS(String r18_PERFORMANCE_STATUS) {
		R18_PERFORMANCE_STATUS = r18_PERFORMANCE_STATUS;
	}
	public String getR18_SECURITY() {
		return R18_SECURITY;
	}
	public void setR18_SECURITY(String r18_SECURITY) {
		R18_SECURITY = r18_SECURITY;
	}
	public String getR18_BOARD_APPROVAL() {
		return R18_BOARD_APPROVAL;
	}
	public void setR18_BOARD_APPROVAL(String r18_BOARD_APPROVAL) {
		R18_BOARD_APPROVAL = r18_BOARD_APPROVAL;
	}
	public BigDecimal getR18_INTEREST_RATE() {
		return R18_INTEREST_RATE;
	}
	public void setR18_INTEREST_RATE(BigDecimal r18_INTEREST_RATE) {
		R18_INTEREST_RATE = r18_INTEREST_RATE;
	}
	public BigDecimal getR18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r18_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR18_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R18_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR18_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r18_LIMIT_PCT_UNIMPAIRED_CAP) {
		R18_LIMIT_PCT_UNIMPAIRED_CAP = r18_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR19_NO_OF_GROUP() {
		return R19_NO_OF_GROUP;
	}
	public void setR19_NO_OF_GROUP(String r19_NO_OF_GROUP) {
		R19_NO_OF_GROUP = r19_NO_OF_GROUP;
	}
	public String getR19_NO_OF_CUSTOMER() {
		return R19_NO_OF_CUSTOMER;
	}
	public void setR19_NO_OF_CUSTOMER(String r19_NO_OF_CUSTOMER) {
		R19_NO_OF_CUSTOMER = r19_NO_OF_CUSTOMER;
	}
	public String getR19_SECTOR_TYPE() {
		return R19_SECTOR_TYPE;
	}
	public void setR19_SECTOR_TYPE(String r19_SECTOR_TYPE) {
		R19_SECTOR_TYPE = r19_SECTOR_TYPE;
	}
	public String getR19_FACILITY_TYPE() {
		return R19_FACILITY_TYPE;
	}
	public void setR19_FACILITY_TYPE(String r19_FACILITY_TYPE) {
		R19_FACILITY_TYPE = r19_FACILITY_TYPE;
	}
	public BigDecimal getR19_ORIGINAL_AMOUNT() {
		return R19_ORIGINAL_AMOUNT;
	}
	public void setR19_ORIGINAL_AMOUNT(BigDecimal r19_ORIGINAL_AMOUNT) {
		R19_ORIGINAL_AMOUNT = r19_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR19_UTILISATION_OUTSTANDING_BAL() {
		return R19_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR19_UTILISATION_OUTSTANDING_BAL(BigDecimal r19_UTILISATION_OUTSTANDING_BAL) {
		R19_UTILISATION_OUTSTANDING_BAL = r19_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR19_EFFECTIVE_DATE() {
		return R19_EFFECTIVE_DATE;
	}
	public void setR19_EFFECTIVE_DATE(Date r19_EFFECTIVE_DATE) {
		R19_EFFECTIVE_DATE = r19_EFFECTIVE_DATE;
	}
	public String getR19_REPAYMENT_PERIOD() {
		return R19_REPAYMENT_PERIOD;
	}
	public void setR19_REPAYMENT_PERIOD(String r19_REPAYMENT_PERIOD) {
		R19_REPAYMENT_PERIOD = r19_REPAYMENT_PERIOD;
	}
	public String getR19_PERFORMANCE_STATUS() {
		return R19_PERFORMANCE_STATUS;
	}
	public void setR19_PERFORMANCE_STATUS(String r19_PERFORMANCE_STATUS) {
		R19_PERFORMANCE_STATUS = r19_PERFORMANCE_STATUS;
	}
	public String getR19_SECURITY() {
		return R19_SECURITY;
	}
	public void setR19_SECURITY(String r19_SECURITY) {
		R19_SECURITY = r19_SECURITY;
	}
	public String getR19_BOARD_APPROVAL() {
		return R19_BOARD_APPROVAL;
	}
	public void setR19_BOARD_APPROVAL(String r19_BOARD_APPROVAL) {
		R19_BOARD_APPROVAL = r19_BOARD_APPROVAL;
	}
	public BigDecimal getR19_INTEREST_RATE() {
		return R19_INTEREST_RATE;
	}
	public void setR19_INTEREST_RATE(BigDecimal r19_INTEREST_RATE) {
		R19_INTEREST_RATE = r19_INTEREST_RATE;
	}
	public BigDecimal getR19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r19_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR19_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R19_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR19_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r19_LIMIT_PCT_UNIMPAIRED_CAP) {
		R19_LIMIT_PCT_UNIMPAIRED_CAP = r19_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR20_NO_OF_GROUP() {
		return R20_NO_OF_GROUP;
	}
	public void setR20_NO_OF_GROUP(String r20_NO_OF_GROUP) {
		R20_NO_OF_GROUP = r20_NO_OF_GROUP;
	}
	public String getR20_NO_OF_CUSTOMER() {
		return R20_NO_OF_CUSTOMER;
	}
	public void setR20_NO_OF_CUSTOMER(String r20_NO_OF_CUSTOMER) {
		R20_NO_OF_CUSTOMER = r20_NO_OF_CUSTOMER;
	}
	public String getR20_SECTOR_TYPE() {
		return R20_SECTOR_TYPE;
	}
	public void setR20_SECTOR_TYPE(String r20_SECTOR_TYPE) {
		R20_SECTOR_TYPE = r20_SECTOR_TYPE;
	}
	public String getR20_FACILITY_TYPE() {
		return R20_FACILITY_TYPE;
	}
	public void setR20_FACILITY_TYPE(String r20_FACILITY_TYPE) {
		R20_FACILITY_TYPE = r20_FACILITY_TYPE;
	}
	public BigDecimal getR20_ORIGINAL_AMOUNT() {
		return R20_ORIGINAL_AMOUNT;
	}
	public void setR20_ORIGINAL_AMOUNT(BigDecimal r20_ORIGINAL_AMOUNT) {
		R20_ORIGINAL_AMOUNT = r20_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR20_UTILISATION_OUTSTANDING_BAL() {
		return R20_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR20_UTILISATION_OUTSTANDING_BAL(BigDecimal r20_UTILISATION_OUTSTANDING_BAL) {
		R20_UTILISATION_OUTSTANDING_BAL = r20_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR20_EFFECTIVE_DATE() {
		return R20_EFFECTIVE_DATE;
	}
	public void setR20_EFFECTIVE_DATE(Date r20_EFFECTIVE_DATE) {
		R20_EFFECTIVE_DATE = r20_EFFECTIVE_DATE;
	}
	public String getR20_REPAYMENT_PERIOD() {
		return R20_REPAYMENT_PERIOD;
	}
	public void setR20_REPAYMENT_PERIOD(String r20_REPAYMENT_PERIOD) {
		R20_REPAYMENT_PERIOD = r20_REPAYMENT_PERIOD;
	}
	public String getR20_PERFORMANCE_STATUS() {
		return R20_PERFORMANCE_STATUS;
	}
	public void setR20_PERFORMANCE_STATUS(String r20_PERFORMANCE_STATUS) {
		R20_PERFORMANCE_STATUS = r20_PERFORMANCE_STATUS;
	}
	public String getR20_SECURITY() {
		return R20_SECURITY;
	}
	public void setR20_SECURITY(String r20_SECURITY) {
		R20_SECURITY = r20_SECURITY;
	}
	public String getR20_BOARD_APPROVAL() {
		return R20_BOARD_APPROVAL;
	}
	public void setR20_BOARD_APPROVAL(String r20_BOARD_APPROVAL) {
		R20_BOARD_APPROVAL = r20_BOARD_APPROVAL;
	}
	public BigDecimal getR20_INTEREST_RATE() {
		return R20_INTEREST_RATE;
	}
	public void setR20_INTEREST_RATE(BigDecimal r20_INTEREST_RATE) {
		R20_INTEREST_RATE = r20_INTEREST_RATE;
	}
	public BigDecimal getR20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r20_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR20_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R20_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR20_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r20_LIMIT_PCT_UNIMPAIRED_CAP) {
		R20_LIMIT_PCT_UNIMPAIRED_CAP = r20_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR21_NO_OF_GROUP() {
		return R21_NO_OF_GROUP;
	}
	public void setR21_NO_OF_GROUP(String r21_NO_OF_GROUP) {
		R21_NO_OF_GROUP = r21_NO_OF_GROUP;
	}
	public String getR21_NO_OF_CUSTOMER() {
		return R21_NO_OF_CUSTOMER;
	}
	public void setR21_NO_OF_CUSTOMER(String r21_NO_OF_CUSTOMER) {
		R21_NO_OF_CUSTOMER = r21_NO_OF_CUSTOMER;
	}
	public String getR21_SECTOR_TYPE() {
		return R21_SECTOR_TYPE;
	}
	public void setR21_SECTOR_TYPE(String r21_SECTOR_TYPE) {
		R21_SECTOR_TYPE = r21_SECTOR_TYPE;
	}
	public String getR21_FACILITY_TYPE() {
		return R21_FACILITY_TYPE;
	}
	public void setR21_FACILITY_TYPE(String r21_FACILITY_TYPE) {
		R21_FACILITY_TYPE = r21_FACILITY_TYPE;
	}
	public BigDecimal getR21_ORIGINAL_AMOUNT() {
		return R21_ORIGINAL_AMOUNT;
	}
	public void setR21_ORIGINAL_AMOUNT(BigDecimal r21_ORIGINAL_AMOUNT) {
		R21_ORIGINAL_AMOUNT = r21_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR21_UTILISATION_OUTSTANDING_BAL() {
		return R21_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR21_UTILISATION_OUTSTANDING_BAL(BigDecimal r21_UTILISATION_OUTSTANDING_BAL) {
		R21_UTILISATION_OUTSTANDING_BAL = r21_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR21_EFFECTIVE_DATE() {
		return R21_EFFECTIVE_DATE;
	}
	public void setR21_EFFECTIVE_DATE(Date r21_EFFECTIVE_DATE) {
		R21_EFFECTIVE_DATE = r21_EFFECTIVE_DATE;
	}
	public String getR21_REPAYMENT_PERIOD() {
		return R21_REPAYMENT_PERIOD;
	}
	public void setR21_REPAYMENT_PERIOD(String r21_REPAYMENT_PERIOD) {
		R21_REPAYMENT_PERIOD = r21_REPAYMENT_PERIOD;
	}
	public String getR21_PERFORMANCE_STATUS() {
		return R21_PERFORMANCE_STATUS;
	}
	public void setR21_PERFORMANCE_STATUS(String r21_PERFORMANCE_STATUS) {
		R21_PERFORMANCE_STATUS = r21_PERFORMANCE_STATUS;
	}
	public String getR21_SECURITY() {
		return R21_SECURITY;
	}
	public void setR21_SECURITY(String r21_SECURITY) {
		R21_SECURITY = r21_SECURITY;
	}
	public String getR21_BOARD_APPROVAL() {
		return R21_BOARD_APPROVAL;
	}
	public void setR21_BOARD_APPROVAL(String r21_BOARD_APPROVAL) {
		R21_BOARD_APPROVAL = r21_BOARD_APPROVAL;
	}
	public BigDecimal getR21_INTEREST_RATE() {
		return R21_INTEREST_RATE;
	}
	public void setR21_INTEREST_RATE(BigDecimal r21_INTEREST_RATE) {
		R21_INTEREST_RATE = r21_INTEREST_RATE;
	}
	public BigDecimal getR21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r21_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR21_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R21_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR21_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r21_LIMIT_PCT_UNIMPAIRED_CAP) {
		R21_LIMIT_PCT_UNIMPAIRED_CAP = r21_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR22_NO_OF_GROUP() {
		return R22_NO_OF_GROUP;
	}
	public void setR22_NO_OF_GROUP(String r22_NO_OF_GROUP) {
		R22_NO_OF_GROUP = r22_NO_OF_GROUP;
	}
	public String getR22_NO_OF_CUSTOMER() {
		return R22_NO_OF_CUSTOMER;
	}
	public void setR22_NO_OF_CUSTOMER(String r22_NO_OF_CUSTOMER) {
		R22_NO_OF_CUSTOMER = r22_NO_OF_CUSTOMER;
	}
	public String getR22_SECTOR_TYPE() {
		return R22_SECTOR_TYPE;
	}
	public void setR22_SECTOR_TYPE(String r22_SECTOR_TYPE) {
		R22_SECTOR_TYPE = r22_SECTOR_TYPE;
	}
	public String getR22_FACILITY_TYPE() {
		return R22_FACILITY_TYPE;
	}
	public void setR22_FACILITY_TYPE(String r22_FACILITY_TYPE) {
		R22_FACILITY_TYPE = r22_FACILITY_TYPE;
	}
	public BigDecimal getR22_ORIGINAL_AMOUNT() {
		return R22_ORIGINAL_AMOUNT;
	}
	public void setR22_ORIGINAL_AMOUNT(BigDecimal r22_ORIGINAL_AMOUNT) {
		R22_ORIGINAL_AMOUNT = r22_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR22_UTILISATION_OUTSTANDING_BAL() {
		return R22_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR22_UTILISATION_OUTSTANDING_BAL(BigDecimal r22_UTILISATION_OUTSTANDING_BAL) {
		R22_UTILISATION_OUTSTANDING_BAL = r22_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR22_EFFECTIVE_DATE() {
		return R22_EFFECTIVE_DATE;
	}
	public void setR22_EFFECTIVE_DATE(Date r22_EFFECTIVE_DATE) {
		R22_EFFECTIVE_DATE = r22_EFFECTIVE_DATE;
	}
	public String getR22_REPAYMENT_PERIOD() {
		return R22_REPAYMENT_PERIOD;
	}
	public void setR22_REPAYMENT_PERIOD(String r22_REPAYMENT_PERIOD) {
		R22_REPAYMENT_PERIOD = r22_REPAYMENT_PERIOD;
	}
	public String getR22_PERFORMANCE_STATUS() {
		return R22_PERFORMANCE_STATUS;
	}
	public void setR22_PERFORMANCE_STATUS(String r22_PERFORMANCE_STATUS) {
		R22_PERFORMANCE_STATUS = r22_PERFORMANCE_STATUS;
	}
	public String getR22_SECURITY() {
		return R22_SECURITY;
	}
	public void setR22_SECURITY(String r22_SECURITY) {
		R22_SECURITY = r22_SECURITY;
	}
	public String getR22_BOARD_APPROVAL() {
		return R22_BOARD_APPROVAL;
	}
	public void setR22_BOARD_APPROVAL(String r22_BOARD_APPROVAL) {
		R22_BOARD_APPROVAL = r22_BOARD_APPROVAL;
	}
	public BigDecimal getR22_INTEREST_RATE() {
		return R22_INTEREST_RATE;
	}
	public void setR22_INTEREST_RATE(BigDecimal r22_INTEREST_RATE) {
		R22_INTEREST_RATE = r22_INTEREST_RATE;
	}
	public BigDecimal getR22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r22_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR22_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R22_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR22_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r22_LIMIT_PCT_UNIMPAIRED_CAP) {
		R22_LIMIT_PCT_UNIMPAIRED_CAP = r22_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR23_NO_OF_GROUP() {
		return R23_NO_OF_GROUP;
	}
	public void setR23_NO_OF_GROUP(String r23_NO_OF_GROUP) {
		R23_NO_OF_GROUP = r23_NO_OF_GROUP;
	}
	public String getR23_NO_OF_CUSTOMER() {
		return R23_NO_OF_CUSTOMER;
	}
	public void setR23_NO_OF_CUSTOMER(String r23_NO_OF_CUSTOMER) {
		R23_NO_OF_CUSTOMER = r23_NO_OF_CUSTOMER;
	}
	public String getR23_SECTOR_TYPE() {
		return R23_SECTOR_TYPE;
	}
	public void setR23_SECTOR_TYPE(String r23_SECTOR_TYPE) {
		R23_SECTOR_TYPE = r23_SECTOR_TYPE;
	}
	public String getR23_FACILITY_TYPE() {
		return R23_FACILITY_TYPE;
	}
	public void setR23_FACILITY_TYPE(String r23_FACILITY_TYPE) {
		R23_FACILITY_TYPE = r23_FACILITY_TYPE;
	}
	public BigDecimal getR23_ORIGINAL_AMOUNT() {
		return R23_ORIGINAL_AMOUNT;
	}
	public void setR23_ORIGINAL_AMOUNT(BigDecimal r23_ORIGINAL_AMOUNT) {
		R23_ORIGINAL_AMOUNT = r23_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR23_UTILISATION_OUTSTANDING_BAL() {
		return R23_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR23_UTILISATION_OUTSTANDING_BAL(BigDecimal r23_UTILISATION_OUTSTANDING_BAL) {
		R23_UTILISATION_OUTSTANDING_BAL = r23_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR23_EFFECTIVE_DATE() {
		return R23_EFFECTIVE_DATE;
	}
	public void setR23_EFFECTIVE_DATE(Date r23_EFFECTIVE_DATE) {
		R23_EFFECTIVE_DATE = r23_EFFECTIVE_DATE;
	}
	public String getR23_REPAYMENT_PERIOD() {
		return R23_REPAYMENT_PERIOD;
	}
	public void setR23_REPAYMENT_PERIOD(String r23_REPAYMENT_PERIOD) {
		R23_REPAYMENT_PERIOD = r23_REPAYMENT_PERIOD;
	}
	public String getR23_PERFORMANCE_STATUS() {
		return R23_PERFORMANCE_STATUS;
	}
	public void setR23_PERFORMANCE_STATUS(String r23_PERFORMANCE_STATUS) {
		R23_PERFORMANCE_STATUS = r23_PERFORMANCE_STATUS;
	}
	public String getR23_SECURITY() {
		return R23_SECURITY;
	}
	public void setR23_SECURITY(String r23_SECURITY) {
		R23_SECURITY = r23_SECURITY;
	}
	public String getR23_BOARD_APPROVAL() {
		return R23_BOARD_APPROVAL;
	}
	public void setR23_BOARD_APPROVAL(String r23_BOARD_APPROVAL) {
		R23_BOARD_APPROVAL = r23_BOARD_APPROVAL;
	}
	public BigDecimal getR23_INTEREST_RATE() {
		return R23_INTEREST_RATE;
	}
	public void setR23_INTEREST_RATE(BigDecimal r23_INTEREST_RATE) {
		R23_INTEREST_RATE = r23_INTEREST_RATE;
	}
	public BigDecimal getR23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r23_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR23_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R23_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR23_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r23_LIMIT_PCT_UNIMPAIRED_CAP) {
		R23_LIMIT_PCT_UNIMPAIRED_CAP = r23_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR24_NO_OF_GROUP() {
		return R24_NO_OF_GROUP;
	}
	public void setR24_NO_OF_GROUP(String r24_NO_OF_GROUP) {
		R24_NO_OF_GROUP = r24_NO_OF_GROUP;
	}
	public String getR24_NO_OF_CUSTOMER() {
		return R24_NO_OF_CUSTOMER;
	}
	public void setR24_NO_OF_CUSTOMER(String r24_NO_OF_CUSTOMER) {
		R24_NO_OF_CUSTOMER = r24_NO_OF_CUSTOMER;
	}
	public String getR24_SECTOR_TYPE() {
		return R24_SECTOR_TYPE;
	}
	public void setR24_SECTOR_TYPE(String r24_SECTOR_TYPE) {
		R24_SECTOR_TYPE = r24_SECTOR_TYPE;
	}
	public String getR24_FACILITY_TYPE() {
		return R24_FACILITY_TYPE;
	}
	public void setR24_FACILITY_TYPE(String r24_FACILITY_TYPE) {
		R24_FACILITY_TYPE = r24_FACILITY_TYPE;
	}
	public BigDecimal getR24_ORIGINAL_AMOUNT() {
		return R24_ORIGINAL_AMOUNT;
	}
	public void setR24_ORIGINAL_AMOUNT(BigDecimal r24_ORIGINAL_AMOUNT) {
		R24_ORIGINAL_AMOUNT = r24_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR24_UTILISATION_OUTSTANDING_BAL() {
		return R24_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR24_UTILISATION_OUTSTANDING_BAL(BigDecimal r24_UTILISATION_OUTSTANDING_BAL) {
		R24_UTILISATION_OUTSTANDING_BAL = r24_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR24_EFFECTIVE_DATE() {
		return R24_EFFECTIVE_DATE;
	}
	public void setR24_EFFECTIVE_DATE(Date r24_EFFECTIVE_DATE) {
		R24_EFFECTIVE_DATE = r24_EFFECTIVE_DATE;
	}
	public String getR24_REPAYMENT_PERIOD() {
		return R24_REPAYMENT_PERIOD;
	}
	public void setR24_REPAYMENT_PERIOD(String r24_REPAYMENT_PERIOD) {
		R24_REPAYMENT_PERIOD = r24_REPAYMENT_PERIOD;
	}
	public String getR24_PERFORMANCE_STATUS() {
		return R24_PERFORMANCE_STATUS;
	}
	public void setR24_PERFORMANCE_STATUS(String r24_PERFORMANCE_STATUS) {
		R24_PERFORMANCE_STATUS = r24_PERFORMANCE_STATUS;
	}
	public String getR24_SECURITY() {
		return R24_SECURITY;
	}
	public void setR24_SECURITY(String r24_SECURITY) {
		R24_SECURITY = r24_SECURITY;
	}
	public String getR24_BOARD_APPROVAL() {
		return R24_BOARD_APPROVAL;
	}
	public void setR24_BOARD_APPROVAL(String r24_BOARD_APPROVAL) {
		R24_BOARD_APPROVAL = r24_BOARD_APPROVAL;
	}
	public BigDecimal getR24_INTEREST_RATE() {
		return R24_INTEREST_RATE;
	}
	public void setR24_INTEREST_RATE(BigDecimal r24_INTEREST_RATE) {
		R24_INTEREST_RATE = r24_INTEREST_RATE;
	}
	public BigDecimal getR24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r24_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR24_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R24_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR24_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r24_LIMIT_PCT_UNIMPAIRED_CAP) {
		R24_LIMIT_PCT_UNIMPAIRED_CAP = r24_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR25_NO_OF_GROUP() {
		return R25_NO_OF_GROUP;
	}
	public void setR25_NO_OF_GROUP(String r25_NO_OF_GROUP) {
		R25_NO_OF_GROUP = r25_NO_OF_GROUP;
	}
	public String getR25_NO_OF_CUSTOMER() {
		return R25_NO_OF_CUSTOMER;
	}
	public void setR25_NO_OF_CUSTOMER(String r25_NO_OF_CUSTOMER) {
		R25_NO_OF_CUSTOMER = r25_NO_OF_CUSTOMER;
	}
	public String getR25_SECTOR_TYPE() {
		return R25_SECTOR_TYPE;
	}
	public void setR25_SECTOR_TYPE(String r25_SECTOR_TYPE) {
		R25_SECTOR_TYPE = r25_SECTOR_TYPE;
	}
	public String getR25_FACILITY_TYPE() {
		return R25_FACILITY_TYPE;
	}
	public void setR25_FACILITY_TYPE(String r25_FACILITY_TYPE) {
		R25_FACILITY_TYPE = r25_FACILITY_TYPE;
	}
	public BigDecimal getR25_ORIGINAL_AMOUNT() {
		return R25_ORIGINAL_AMOUNT;
	}
	public void setR25_ORIGINAL_AMOUNT(BigDecimal r25_ORIGINAL_AMOUNT) {
		R25_ORIGINAL_AMOUNT = r25_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR25_UTILISATION_OUTSTANDING_BAL() {
		return R25_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR25_UTILISATION_OUTSTANDING_BAL(BigDecimal r25_UTILISATION_OUTSTANDING_BAL) {
		R25_UTILISATION_OUTSTANDING_BAL = r25_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR25_EFFECTIVE_DATE() {
		return R25_EFFECTIVE_DATE;
	}
	public void setR25_EFFECTIVE_DATE(Date r25_EFFECTIVE_DATE) {
		R25_EFFECTIVE_DATE = r25_EFFECTIVE_DATE;
	}
	public String getR25_REPAYMENT_PERIOD() {
		return R25_REPAYMENT_PERIOD;
	}
	public void setR25_REPAYMENT_PERIOD(String r25_REPAYMENT_PERIOD) {
		R25_REPAYMENT_PERIOD = r25_REPAYMENT_PERIOD;
	}
	public String getR25_PERFORMANCE_STATUS() {
		return R25_PERFORMANCE_STATUS;
	}
	public void setR25_PERFORMANCE_STATUS(String r25_PERFORMANCE_STATUS) {
		R25_PERFORMANCE_STATUS = r25_PERFORMANCE_STATUS;
	}
	public String getR25_SECURITY() {
		return R25_SECURITY;
	}
	public void setR25_SECURITY(String r25_SECURITY) {
		R25_SECURITY = r25_SECURITY;
	}
	public String getR25_BOARD_APPROVAL() {
		return R25_BOARD_APPROVAL;
	}
	public void setR25_BOARD_APPROVAL(String r25_BOARD_APPROVAL) {
		R25_BOARD_APPROVAL = r25_BOARD_APPROVAL;
	}
	public BigDecimal getR25_INTEREST_RATE() {
		return R25_INTEREST_RATE;
	}
	public void setR25_INTEREST_RATE(BigDecimal r25_INTEREST_RATE) {
		R25_INTEREST_RATE = r25_INTEREST_RATE;
	}
	public BigDecimal getR25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r25_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR25_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R25_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR25_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r25_LIMIT_PCT_UNIMPAIRED_CAP) {
		R25_LIMIT_PCT_UNIMPAIRED_CAP = r25_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR26_NO_OF_GROUP() {
		return R26_NO_OF_GROUP;
	}
	public void setR26_NO_OF_GROUP(String r26_NO_OF_GROUP) {
		R26_NO_OF_GROUP = r26_NO_OF_GROUP;
	}
	public String getR26_NO_OF_CUSTOMER() {
		return R26_NO_OF_CUSTOMER;
	}
	public void setR26_NO_OF_CUSTOMER(String r26_NO_OF_CUSTOMER) {
		R26_NO_OF_CUSTOMER = r26_NO_OF_CUSTOMER;
	}
	public String getR26_SECTOR_TYPE() {
		return R26_SECTOR_TYPE;
	}
	public void setR26_SECTOR_TYPE(String r26_SECTOR_TYPE) {
		R26_SECTOR_TYPE = r26_SECTOR_TYPE;
	}
	public String getR26_FACILITY_TYPE() {
		return R26_FACILITY_TYPE;
	}
	public void setR26_FACILITY_TYPE(String r26_FACILITY_TYPE) {
		R26_FACILITY_TYPE = r26_FACILITY_TYPE;
	}
	public BigDecimal getR26_ORIGINAL_AMOUNT() {
		return R26_ORIGINAL_AMOUNT;
	}
	public void setR26_ORIGINAL_AMOUNT(BigDecimal r26_ORIGINAL_AMOUNT) {
		R26_ORIGINAL_AMOUNT = r26_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR26_UTILISATION_OUTSTANDING_BAL() {
		return R26_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR26_UTILISATION_OUTSTANDING_BAL(BigDecimal r26_UTILISATION_OUTSTANDING_BAL) {
		R26_UTILISATION_OUTSTANDING_BAL = r26_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR26_EFFECTIVE_DATE() {
		return R26_EFFECTIVE_DATE;
	}
	public void setR26_EFFECTIVE_DATE(Date r26_EFFECTIVE_DATE) {
		R26_EFFECTIVE_DATE = r26_EFFECTIVE_DATE;
	}
	public String getR26_REPAYMENT_PERIOD() {
		return R26_REPAYMENT_PERIOD;
	}
	public void setR26_REPAYMENT_PERIOD(String r26_REPAYMENT_PERIOD) {
		R26_REPAYMENT_PERIOD = r26_REPAYMENT_PERIOD;
	}
	public String getR26_PERFORMANCE_STATUS() {
		return R26_PERFORMANCE_STATUS;
	}
	public void setR26_PERFORMANCE_STATUS(String r26_PERFORMANCE_STATUS) {
		R26_PERFORMANCE_STATUS = r26_PERFORMANCE_STATUS;
	}
	public String getR26_SECURITY() {
		return R26_SECURITY;
	}
	public void setR26_SECURITY(String r26_SECURITY) {
		R26_SECURITY = r26_SECURITY;
	}
	public String getR26_BOARD_APPROVAL() {
		return R26_BOARD_APPROVAL;
	}
	public void setR26_BOARD_APPROVAL(String r26_BOARD_APPROVAL) {
		R26_BOARD_APPROVAL = r26_BOARD_APPROVAL;
	}
	public BigDecimal getR26_INTEREST_RATE() {
		return R26_INTEREST_RATE;
	}
	public void setR26_INTEREST_RATE(BigDecimal r26_INTEREST_RATE) {
		R26_INTEREST_RATE = r26_INTEREST_RATE;
	}
	public BigDecimal getR26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r26_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR26_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R26_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR26_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r26_LIMIT_PCT_UNIMPAIRED_CAP) {
		R26_LIMIT_PCT_UNIMPAIRED_CAP = r26_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR27_NO_OF_GROUP() {
		return R27_NO_OF_GROUP;
	}
	public void setR27_NO_OF_GROUP(String r27_NO_OF_GROUP) {
		R27_NO_OF_GROUP = r27_NO_OF_GROUP;
	}
	public String getR27_NO_OF_CUSTOMER() {
		return R27_NO_OF_CUSTOMER;
	}
	public void setR27_NO_OF_CUSTOMER(String r27_NO_OF_CUSTOMER) {
		R27_NO_OF_CUSTOMER = r27_NO_OF_CUSTOMER;
	}
	public String getR27_SECTOR_TYPE() {
		return R27_SECTOR_TYPE;
	}
	public void setR27_SECTOR_TYPE(String r27_SECTOR_TYPE) {
		R27_SECTOR_TYPE = r27_SECTOR_TYPE;
	}
	public String getR27_FACILITY_TYPE() {
		return R27_FACILITY_TYPE;
	}
	public void setR27_FACILITY_TYPE(String r27_FACILITY_TYPE) {
		R27_FACILITY_TYPE = r27_FACILITY_TYPE;
	}
	public BigDecimal getR27_ORIGINAL_AMOUNT() {
		return R27_ORIGINAL_AMOUNT;
	}
	public void setR27_ORIGINAL_AMOUNT(BigDecimal r27_ORIGINAL_AMOUNT) {
		R27_ORIGINAL_AMOUNT = r27_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR27_UTILISATION_OUTSTANDING_BAL() {
		return R27_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR27_UTILISATION_OUTSTANDING_BAL(BigDecimal r27_UTILISATION_OUTSTANDING_BAL) {
		R27_UTILISATION_OUTSTANDING_BAL = r27_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR27_EFFECTIVE_DATE() {
		return R27_EFFECTIVE_DATE;
	}
	public void setR27_EFFECTIVE_DATE(Date r27_EFFECTIVE_DATE) {
		R27_EFFECTIVE_DATE = r27_EFFECTIVE_DATE;
	}
	public String getR27_REPAYMENT_PERIOD() {
		return R27_REPAYMENT_PERIOD;
	}
	public void setR27_REPAYMENT_PERIOD(String r27_REPAYMENT_PERIOD) {
		R27_REPAYMENT_PERIOD = r27_REPAYMENT_PERIOD;
	}
	public String getR27_PERFORMANCE_STATUS() {
		return R27_PERFORMANCE_STATUS;
	}
	public void setR27_PERFORMANCE_STATUS(String r27_PERFORMANCE_STATUS) {
		R27_PERFORMANCE_STATUS = r27_PERFORMANCE_STATUS;
	}
	public String getR27_SECURITY() {
		return R27_SECURITY;
	}
	public void setR27_SECURITY(String r27_SECURITY) {
		R27_SECURITY = r27_SECURITY;
	}
	public String getR27_BOARD_APPROVAL() {
		return R27_BOARD_APPROVAL;
	}
	public void setR27_BOARD_APPROVAL(String r27_BOARD_APPROVAL) {
		R27_BOARD_APPROVAL = r27_BOARD_APPROVAL;
	}
	public BigDecimal getR27_INTEREST_RATE() {
		return R27_INTEREST_RATE;
	}
	public void setR27_INTEREST_RATE(BigDecimal r27_INTEREST_RATE) {
		R27_INTEREST_RATE = r27_INTEREST_RATE;
	}
	public BigDecimal getR27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r27_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR27_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R27_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR27_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r27_LIMIT_PCT_UNIMPAIRED_CAP) {
		R27_LIMIT_PCT_UNIMPAIRED_CAP = r27_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR28_NO_OF_GROUP() {
		return R28_NO_OF_GROUP;
	}
	public void setR28_NO_OF_GROUP(String r28_NO_OF_GROUP) {
		R28_NO_OF_GROUP = r28_NO_OF_GROUP;
	}
	public String getR28_NO_OF_CUSTOMER() {
		return R28_NO_OF_CUSTOMER;
	}
	public void setR28_NO_OF_CUSTOMER(String r28_NO_OF_CUSTOMER) {
		R28_NO_OF_CUSTOMER = r28_NO_OF_CUSTOMER;
	}
	public String getR28_SECTOR_TYPE() {
		return R28_SECTOR_TYPE;
	}
	public void setR28_SECTOR_TYPE(String r28_SECTOR_TYPE) {
		R28_SECTOR_TYPE = r28_SECTOR_TYPE;
	}
	public String getR28_FACILITY_TYPE() {
		return R28_FACILITY_TYPE;
	}
	public void setR28_FACILITY_TYPE(String r28_FACILITY_TYPE) {
		R28_FACILITY_TYPE = r28_FACILITY_TYPE;
	}
	public BigDecimal getR28_ORIGINAL_AMOUNT() {
		return R28_ORIGINAL_AMOUNT;
	}
	public void setR28_ORIGINAL_AMOUNT(BigDecimal r28_ORIGINAL_AMOUNT) {
		R28_ORIGINAL_AMOUNT = r28_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR28_UTILISATION_OUTSTANDING_BAL() {
		return R28_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR28_UTILISATION_OUTSTANDING_BAL(BigDecimal r28_UTILISATION_OUTSTANDING_BAL) {
		R28_UTILISATION_OUTSTANDING_BAL = r28_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR28_EFFECTIVE_DATE() {
		return R28_EFFECTIVE_DATE;
	}
	public void setR28_EFFECTIVE_DATE(Date r28_EFFECTIVE_DATE) {
		R28_EFFECTIVE_DATE = r28_EFFECTIVE_DATE;
	}
	public String getR28_REPAYMENT_PERIOD() {
		return R28_REPAYMENT_PERIOD;
	}
	public void setR28_REPAYMENT_PERIOD(String r28_REPAYMENT_PERIOD) {
		R28_REPAYMENT_PERIOD = r28_REPAYMENT_PERIOD;
	}
	public String getR28_PERFORMANCE_STATUS() {
		return R28_PERFORMANCE_STATUS;
	}
	public void setR28_PERFORMANCE_STATUS(String r28_PERFORMANCE_STATUS) {
		R28_PERFORMANCE_STATUS = r28_PERFORMANCE_STATUS;
	}
	public String getR28_SECURITY() {
		return R28_SECURITY;
	}
	public void setR28_SECURITY(String r28_SECURITY) {
		R28_SECURITY = r28_SECURITY;
	}
	public String getR28_BOARD_APPROVAL() {
		return R28_BOARD_APPROVAL;
	}
	public void setR28_BOARD_APPROVAL(String r28_BOARD_APPROVAL) {
		R28_BOARD_APPROVAL = r28_BOARD_APPROVAL;
	}
	public BigDecimal getR28_INTEREST_RATE() {
		return R28_INTEREST_RATE;
	}
	public void setR28_INTEREST_RATE(BigDecimal r28_INTEREST_RATE) {
		R28_INTEREST_RATE = r28_INTEREST_RATE;
	}
	public BigDecimal getR28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r28_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR28_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R28_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR28_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r28_LIMIT_PCT_UNIMPAIRED_CAP) {
		R28_LIMIT_PCT_UNIMPAIRED_CAP = r28_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR29_NO_OF_GROUP() {
		return R29_NO_OF_GROUP;
	}
	public void setR29_NO_OF_GROUP(String r29_NO_OF_GROUP) {
		R29_NO_OF_GROUP = r29_NO_OF_GROUP;
	}
	public String getR29_NO_OF_CUSTOMER() {
		return R29_NO_OF_CUSTOMER;
	}
	public void setR29_NO_OF_CUSTOMER(String r29_NO_OF_CUSTOMER) {
		R29_NO_OF_CUSTOMER = r29_NO_OF_CUSTOMER;
	}
	public String getR29_SECTOR_TYPE() {
		return R29_SECTOR_TYPE;
	}
	public void setR29_SECTOR_TYPE(String r29_SECTOR_TYPE) {
		R29_SECTOR_TYPE = r29_SECTOR_TYPE;
	}
	public String getR29_FACILITY_TYPE() {
		return R29_FACILITY_TYPE;
	}
	public void setR29_FACILITY_TYPE(String r29_FACILITY_TYPE) {
		R29_FACILITY_TYPE = r29_FACILITY_TYPE;
	}
	public BigDecimal getR29_ORIGINAL_AMOUNT() {
		return R29_ORIGINAL_AMOUNT;
	}
	public void setR29_ORIGINAL_AMOUNT(BigDecimal r29_ORIGINAL_AMOUNT) {
		R29_ORIGINAL_AMOUNT = r29_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR29_UTILISATION_OUTSTANDING_BAL() {
		return R29_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR29_UTILISATION_OUTSTANDING_BAL(BigDecimal r29_UTILISATION_OUTSTANDING_BAL) {
		R29_UTILISATION_OUTSTANDING_BAL = r29_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR29_EFFECTIVE_DATE() {
		return R29_EFFECTIVE_DATE;
	}
	public void setR29_EFFECTIVE_DATE(Date r29_EFFECTIVE_DATE) {
		R29_EFFECTIVE_DATE = r29_EFFECTIVE_DATE;
	}
	public String getR29_REPAYMENT_PERIOD() {
		return R29_REPAYMENT_PERIOD;
	}
	public void setR29_REPAYMENT_PERIOD(String r29_REPAYMENT_PERIOD) {
		R29_REPAYMENT_PERIOD = r29_REPAYMENT_PERIOD;
	}
	public String getR29_PERFORMANCE_STATUS() {
		return R29_PERFORMANCE_STATUS;
	}
	public void setR29_PERFORMANCE_STATUS(String r29_PERFORMANCE_STATUS) {
		R29_PERFORMANCE_STATUS = r29_PERFORMANCE_STATUS;
	}
	public String getR29_SECURITY() {
		return R29_SECURITY;
	}
	public void setR29_SECURITY(String r29_SECURITY) {
		R29_SECURITY = r29_SECURITY;
	}
	public String getR29_BOARD_APPROVAL() {
		return R29_BOARD_APPROVAL;
	}
	public void setR29_BOARD_APPROVAL(String r29_BOARD_APPROVAL) {
		R29_BOARD_APPROVAL = r29_BOARD_APPROVAL;
	}
	public BigDecimal getR29_INTEREST_RATE() {
		return R29_INTEREST_RATE;
	}
	public void setR29_INTEREST_RATE(BigDecimal r29_INTEREST_RATE) {
		R29_INTEREST_RATE = r29_INTEREST_RATE;
	}
	public BigDecimal getR29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r29_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR29_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R29_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR29_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r29_LIMIT_PCT_UNIMPAIRED_CAP) {
		R29_LIMIT_PCT_UNIMPAIRED_CAP = r29_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR30_NO_OF_GROUP() {
		return R30_NO_OF_GROUP;
	}
	public void setR30_NO_OF_GROUP(String r30_NO_OF_GROUP) {
		R30_NO_OF_GROUP = r30_NO_OF_GROUP;
	}
	public String getR30_NO_OF_CUSTOMER() {
		return R30_NO_OF_CUSTOMER;
	}
	public void setR30_NO_OF_CUSTOMER(String r30_NO_OF_CUSTOMER) {
		R30_NO_OF_CUSTOMER = r30_NO_OF_CUSTOMER;
	}
	public String getR30_SECTOR_TYPE() {
		return R30_SECTOR_TYPE;
	}
	public void setR30_SECTOR_TYPE(String r30_SECTOR_TYPE) {
		R30_SECTOR_TYPE = r30_SECTOR_TYPE;
	}
	public String getR30_FACILITY_TYPE() {
		return R30_FACILITY_TYPE;
	}
	public void setR30_FACILITY_TYPE(String r30_FACILITY_TYPE) {
		R30_FACILITY_TYPE = r30_FACILITY_TYPE;
	}
	public BigDecimal getR30_ORIGINAL_AMOUNT() {
		return R30_ORIGINAL_AMOUNT;
	}
	public void setR30_ORIGINAL_AMOUNT(BigDecimal r30_ORIGINAL_AMOUNT) {
		R30_ORIGINAL_AMOUNT = r30_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR30_UTILISATION_OUTSTANDING_BAL() {
		return R30_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR30_UTILISATION_OUTSTANDING_BAL(BigDecimal r30_UTILISATION_OUTSTANDING_BAL) {
		R30_UTILISATION_OUTSTANDING_BAL = r30_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR30_EFFECTIVE_DATE() {
		return R30_EFFECTIVE_DATE;
	}
	public void setR30_EFFECTIVE_DATE(Date r30_EFFECTIVE_DATE) {
		R30_EFFECTIVE_DATE = r30_EFFECTIVE_DATE;
	}
	public String getR30_REPAYMENT_PERIOD() {
		return R30_REPAYMENT_PERIOD;
	}
	public void setR30_REPAYMENT_PERIOD(String r30_REPAYMENT_PERIOD) {
		R30_REPAYMENT_PERIOD = r30_REPAYMENT_PERIOD;
	}
	public String getR30_PERFORMANCE_STATUS() {
		return R30_PERFORMANCE_STATUS;
	}
	public void setR30_PERFORMANCE_STATUS(String r30_PERFORMANCE_STATUS) {
		R30_PERFORMANCE_STATUS = r30_PERFORMANCE_STATUS;
	}
	public String getR30_SECURITY() {
		return R30_SECURITY;
	}
	public void setR30_SECURITY(String r30_SECURITY) {
		R30_SECURITY = r30_SECURITY;
	}
	public String getR30_BOARD_APPROVAL() {
		return R30_BOARD_APPROVAL;
	}
	public void setR30_BOARD_APPROVAL(String r30_BOARD_APPROVAL) {
		R30_BOARD_APPROVAL = r30_BOARD_APPROVAL;
	}
	public BigDecimal getR30_INTEREST_RATE() {
		return R30_INTEREST_RATE;
	}
	public void setR30_INTEREST_RATE(BigDecimal r30_INTEREST_RATE) {
		R30_INTEREST_RATE = r30_INTEREST_RATE;
	}
	public BigDecimal getR30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r30_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR30_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R30_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR30_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r30_LIMIT_PCT_UNIMPAIRED_CAP) {
		R30_LIMIT_PCT_UNIMPAIRED_CAP = r30_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR31_NO_OF_GROUP() {
		return R31_NO_OF_GROUP;
	}
	public void setR31_NO_OF_GROUP(String r31_NO_OF_GROUP) {
		R31_NO_OF_GROUP = r31_NO_OF_GROUP;
	}
	public String getR31_NO_OF_CUSTOMER() {
		return R31_NO_OF_CUSTOMER;
	}
	public void setR31_NO_OF_CUSTOMER(String r31_NO_OF_CUSTOMER) {
		R31_NO_OF_CUSTOMER = r31_NO_OF_CUSTOMER;
	}
	public String getR31_SECTOR_TYPE() {
		return R31_SECTOR_TYPE;
	}
	public void setR31_SECTOR_TYPE(String r31_SECTOR_TYPE) {
		R31_SECTOR_TYPE = r31_SECTOR_TYPE;
	}
	public String getR31_FACILITY_TYPE() {
		return R31_FACILITY_TYPE;
	}
	public void setR31_FACILITY_TYPE(String r31_FACILITY_TYPE) {
		R31_FACILITY_TYPE = r31_FACILITY_TYPE;
	}
	public BigDecimal getR31_ORIGINAL_AMOUNT() {
		return R31_ORIGINAL_AMOUNT;
	}
	public void setR31_ORIGINAL_AMOUNT(BigDecimal r31_ORIGINAL_AMOUNT) {
		R31_ORIGINAL_AMOUNT = r31_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR31_UTILISATION_OUTSTANDING_BAL() {
		return R31_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR31_UTILISATION_OUTSTANDING_BAL(BigDecimal r31_UTILISATION_OUTSTANDING_BAL) {
		R31_UTILISATION_OUTSTANDING_BAL = r31_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR31_EFFECTIVE_DATE() {
		return R31_EFFECTIVE_DATE;
	}
	public void setR31_EFFECTIVE_DATE(Date r31_EFFECTIVE_DATE) {
		R31_EFFECTIVE_DATE = r31_EFFECTIVE_DATE;
	}
	public String getR31_REPAYMENT_PERIOD() {
		return R31_REPAYMENT_PERIOD;
	}
	public void setR31_REPAYMENT_PERIOD(String r31_REPAYMENT_PERIOD) {
		R31_REPAYMENT_PERIOD = r31_REPAYMENT_PERIOD;
	}
	public String getR31_PERFORMANCE_STATUS() {
		return R31_PERFORMANCE_STATUS;
	}
	public void setR31_PERFORMANCE_STATUS(String r31_PERFORMANCE_STATUS) {
		R31_PERFORMANCE_STATUS = r31_PERFORMANCE_STATUS;
	}
	public String getR31_SECURITY() {
		return R31_SECURITY;
	}
	public void setR31_SECURITY(String r31_SECURITY) {
		R31_SECURITY = r31_SECURITY;
	}
	public String getR31_BOARD_APPROVAL() {
		return R31_BOARD_APPROVAL;
	}
	public void setR31_BOARD_APPROVAL(String r31_BOARD_APPROVAL) {
		R31_BOARD_APPROVAL = r31_BOARD_APPROVAL;
	}
	public BigDecimal getR31_INTEREST_RATE() {
		return R31_INTEREST_RATE;
	}
	public void setR31_INTEREST_RATE(BigDecimal r31_INTEREST_RATE) {
		R31_INTEREST_RATE = r31_INTEREST_RATE;
	}
	public BigDecimal getR31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r31_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR31_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R31_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR31_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r31_LIMIT_PCT_UNIMPAIRED_CAP) {
		R31_LIMIT_PCT_UNIMPAIRED_CAP = r31_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR32_NO_OF_GROUP() {
		return R32_NO_OF_GROUP;
	}
	public void setR32_NO_OF_GROUP(String r32_NO_OF_GROUP) {
		R32_NO_OF_GROUP = r32_NO_OF_GROUP;
	}
	public String getR32_NO_OF_CUSTOMER() {
		return R32_NO_OF_CUSTOMER;
	}
	public void setR32_NO_OF_CUSTOMER(String r32_NO_OF_CUSTOMER) {
		R32_NO_OF_CUSTOMER = r32_NO_OF_CUSTOMER;
	}
	public String getR32_SECTOR_TYPE() {
		return R32_SECTOR_TYPE;
	}
	public void setR32_SECTOR_TYPE(String r32_SECTOR_TYPE) {
		R32_SECTOR_TYPE = r32_SECTOR_TYPE;
	}
	public String getR32_FACILITY_TYPE() {
		return R32_FACILITY_TYPE;
	}
	public void setR32_FACILITY_TYPE(String r32_FACILITY_TYPE) {
		R32_FACILITY_TYPE = r32_FACILITY_TYPE;
	}
	public BigDecimal getR32_ORIGINAL_AMOUNT() {
		return R32_ORIGINAL_AMOUNT;
	}
	public void setR32_ORIGINAL_AMOUNT(BigDecimal r32_ORIGINAL_AMOUNT) {
		R32_ORIGINAL_AMOUNT = r32_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR32_UTILISATION_OUTSTANDING_BAL() {
		return R32_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR32_UTILISATION_OUTSTANDING_BAL(BigDecimal r32_UTILISATION_OUTSTANDING_BAL) {
		R32_UTILISATION_OUTSTANDING_BAL = r32_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR32_EFFECTIVE_DATE() {
		return R32_EFFECTIVE_DATE;
	}
	public void setR32_EFFECTIVE_DATE(Date r32_EFFECTIVE_DATE) {
		R32_EFFECTIVE_DATE = r32_EFFECTIVE_DATE;
	}
	public String getR32_REPAYMENT_PERIOD() {
		return R32_REPAYMENT_PERIOD;
	}
	public void setR32_REPAYMENT_PERIOD(String r32_REPAYMENT_PERIOD) {
		R32_REPAYMENT_PERIOD = r32_REPAYMENT_PERIOD;
	}
	public String getR32_PERFORMANCE_STATUS() {
		return R32_PERFORMANCE_STATUS;
	}
	public void setR32_PERFORMANCE_STATUS(String r32_PERFORMANCE_STATUS) {
		R32_PERFORMANCE_STATUS = r32_PERFORMANCE_STATUS;
	}
	public String getR32_SECURITY() {
		return R32_SECURITY;
	}
	public void setR32_SECURITY(String r32_SECURITY) {
		R32_SECURITY = r32_SECURITY;
	}
	public String getR32_BOARD_APPROVAL() {
		return R32_BOARD_APPROVAL;
	}
	public void setR32_BOARD_APPROVAL(String r32_BOARD_APPROVAL) {
		R32_BOARD_APPROVAL = r32_BOARD_APPROVAL;
	}
	public BigDecimal getR32_INTEREST_RATE() {
		return R32_INTEREST_RATE;
	}
	public void setR32_INTEREST_RATE(BigDecimal r32_INTEREST_RATE) {
		R32_INTEREST_RATE = r32_INTEREST_RATE;
	}
	public BigDecimal getR32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r32_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR32_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R32_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR32_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r32_LIMIT_PCT_UNIMPAIRED_CAP) {
		R32_LIMIT_PCT_UNIMPAIRED_CAP = r32_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR33_NO_OF_GROUP() {
		return R33_NO_OF_GROUP;
	}
	public void setR33_NO_OF_GROUP(String r33_NO_OF_GROUP) {
		R33_NO_OF_GROUP = r33_NO_OF_GROUP;
	}
	public String getR33_NO_OF_CUSTOMER() {
		return R33_NO_OF_CUSTOMER;
	}
	public void setR33_NO_OF_CUSTOMER(String r33_NO_OF_CUSTOMER) {
		R33_NO_OF_CUSTOMER = r33_NO_OF_CUSTOMER;
	}
	public String getR33_SECTOR_TYPE() {
		return R33_SECTOR_TYPE;
	}
	public void setR33_SECTOR_TYPE(String r33_SECTOR_TYPE) {
		R33_SECTOR_TYPE = r33_SECTOR_TYPE;
	}
	public String getR33_FACILITY_TYPE() {
		return R33_FACILITY_TYPE;
	}
	public void setR33_FACILITY_TYPE(String r33_FACILITY_TYPE) {
		R33_FACILITY_TYPE = r33_FACILITY_TYPE;
	}
	public BigDecimal getR33_ORIGINAL_AMOUNT() {
		return R33_ORIGINAL_AMOUNT;
	}
	public void setR33_ORIGINAL_AMOUNT(BigDecimal r33_ORIGINAL_AMOUNT) {
		R33_ORIGINAL_AMOUNT = r33_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR33_UTILISATION_OUTSTANDING_BAL() {
		return R33_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR33_UTILISATION_OUTSTANDING_BAL(BigDecimal r33_UTILISATION_OUTSTANDING_BAL) {
		R33_UTILISATION_OUTSTANDING_BAL = r33_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR33_EFFECTIVE_DATE() {
		return R33_EFFECTIVE_DATE;
	}
	public void setR33_EFFECTIVE_DATE(Date r33_EFFECTIVE_DATE) {
		R33_EFFECTIVE_DATE = r33_EFFECTIVE_DATE;
	}
	public String getR33_REPAYMENT_PERIOD() {
		return R33_REPAYMENT_PERIOD;
	}
	public void setR33_REPAYMENT_PERIOD(String r33_REPAYMENT_PERIOD) {
		R33_REPAYMENT_PERIOD = r33_REPAYMENT_PERIOD;
	}
	public String getR33_PERFORMANCE_STATUS() {
		return R33_PERFORMANCE_STATUS;
	}
	public void setR33_PERFORMANCE_STATUS(String r33_PERFORMANCE_STATUS) {
		R33_PERFORMANCE_STATUS = r33_PERFORMANCE_STATUS;
	}
	public String getR33_SECURITY() {
		return R33_SECURITY;
	}
	public void setR33_SECURITY(String r33_SECURITY) {
		R33_SECURITY = r33_SECURITY;
	}
	public String getR33_BOARD_APPROVAL() {
		return R33_BOARD_APPROVAL;
	}
	public void setR33_BOARD_APPROVAL(String r33_BOARD_APPROVAL) {
		R33_BOARD_APPROVAL = r33_BOARD_APPROVAL;
	}
	public BigDecimal getR33_INTEREST_RATE() {
		return R33_INTEREST_RATE;
	}
	public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
		R33_INTEREST_RATE = r33_INTEREST_RATE;
	}
	public BigDecimal getR33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r33_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR33_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R33_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR33_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r33_LIMIT_PCT_UNIMPAIRED_CAP) {
		R33_LIMIT_PCT_UNIMPAIRED_CAP = r33_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR34_NO_OF_GROUP() {
		return R34_NO_OF_GROUP;
	}
	public void setR34_NO_OF_GROUP(String r34_NO_OF_GROUP) {
		R34_NO_OF_GROUP = r34_NO_OF_GROUP;
	}
	public String getR34_NO_OF_CUSTOMER() {
		return R34_NO_OF_CUSTOMER;
	}
	public void setR34_NO_OF_CUSTOMER(String r34_NO_OF_CUSTOMER) {
		R34_NO_OF_CUSTOMER = r34_NO_OF_CUSTOMER;
	}
	public String getR34_SECTOR_TYPE() {
		return R34_SECTOR_TYPE;
	}
	public void setR34_SECTOR_TYPE(String r34_SECTOR_TYPE) {
		R34_SECTOR_TYPE = r34_SECTOR_TYPE;
	}
	public String getR34_FACILITY_TYPE() {
		return R34_FACILITY_TYPE;
	}
	public void setR34_FACILITY_TYPE(String r34_FACILITY_TYPE) {
		R34_FACILITY_TYPE = r34_FACILITY_TYPE;
	}
	public BigDecimal getR34_ORIGINAL_AMOUNT() {
		return R34_ORIGINAL_AMOUNT;
	}
	public void setR34_ORIGINAL_AMOUNT(BigDecimal r34_ORIGINAL_AMOUNT) {
		R34_ORIGINAL_AMOUNT = r34_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR34_UTILISATION_OUTSTANDING_BAL() {
		return R34_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR34_UTILISATION_OUTSTANDING_BAL(BigDecimal r34_UTILISATION_OUTSTANDING_BAL) {
		R34_UTILISATION_OUTSTANDING_BAL = r34_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR34_EFFECTIVE_DATE() {
		return R34_EFFECTIVE_DATE;
	}
	public void setR34_EFFECTIVE_DATE(Date r34_EFFECTIVE_DATE) {
		R34_EFFECTIVE_DATE = r34_EFFECTIVE_DATE;
	}
	public String getR34_REPAYMENT_PERIOD() {
		return R34_REPAYMENT_PERIOD;
	}
	public void setR34_REPAYMENT_PERIOD(String r34_REPAYMENT_PERIOD) {
		R34_REPAYMENT_PERIOD = r34_REPAYMENT_PERIOD;
	}
	public String getR34_PERFORMANCE_STATUS() {
		return R34_PERFORMANCE_STATUS;
	}
	public void setR34_PERFORMANCE_STATUS(String r34_PERFORMANCE_STATUS) {
		R34_PERFORMANCE_STATUS = r34_PERFORMANCE_STATUS;
	}
	public String getR34_SECURITY() {
		return R34_SECURITY;
	}
	public void setR34_SECURITY(String r34_SECURITY) {
		R34_SECURITY = r34_SECURITY;
	}
	public String getR34_BOARD_APPROVAL() {
		return R34_BOARD_APPROVAL;
	}
	public void setR34_BOARD_APPROVAL(String r34_BOARD_APPROVAL) {
		R34_BOARD_APPROVAL = r34_BOARD_APPROVAL;
	}
	public BigDecimal getR34_INTEREST_RATE() {
		return R34_INTEREST_RATE;
	}
	public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
		R34_INTEREST_RATE = r34_INTEREST_RATE;
	}
	public BigDecimal getR34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r34_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR34_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R34_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR34_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r34_LIMIT_PCT_UNIMPAIRED_CAP) {
		R34_LIMIT_PCT_UNIMPAIRED_CAP = r34_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR35_NO_OF_GROUP() {
		return R35_NO_OF_GROUP;
	}
	public void setR35_NO_OF_GROUP(String r35_NO_OF_GROUP) {
		R35_NO_OF_GROUP = r35_NO_OF_GROUP;
	}
	public String getR35_NO_OF_CUSTOMER() {
		return R35_NO_OF_CUSTOMER;
	}
	public void setR35_NO_OF_CUSTOMER(String r35_NO_OF_CUSTOMER) {
		R35_NO_OF_CUSTOMER = r35_NO_OF_CUSTOMER;
	}
	public String getR35_SECTOR_TYPE() {
		return R35_SECTOR_TYPE;
	}
	public void setR35_SECTOR_TYPE(String r35_SECTOR_TYPE) {
		R35_SECTOR_TYPE = r35_SECTOR_TYPE;
	}
	public String getR35_FACILITY_TYPE() {
		return R35_FACILITY_TYPE;
	}
	public void setR35_FACILITY_TYPE(String r35_FACILITY_TYPE) {
		R35_FACILITY_TYPE = r35_FACILITY_TYPE;
	}
	public BigDecimal getR35_ORIGINAL_AMOUNT() {
		return R35_ORIGINAL_AMOUNT;
	}
	public void setR35_ORIGINAL_AMOUNT(BigDecimal r35_ORIGINAL_AMOUNT) {
		R35_ORIGINAL_AMOUNT = r35_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR35_UTILISATION_OUTSTANDING_BAL() {
		return R35_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR35_UTILISATION_OUTSTANDING_BAL(BigDecimal r35_UTILISATION_OUTSTANDING_BAL) {
		R35_UTILISATION_OUTSTANDING_BAL = r35_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR35_EFFECTIVE_DATE() {
		return R35_EFFECTIVE_DATE;
	}
	public void setR35_EFFECTIVE_DATE(Date r35_EFFECTIVE_DATE) {
		R35_EFFECTIVE_DATE = r35_EFFECTIVE_DATE;
	}
	public String getR35_REPAYMENT_PERIOD() {
		return R35_REPAYMENT_PERIOD;
	}
	public void setR35_REPAYMENT_PERIOD(String r35_REPAYMENT_PERIOD) {
		R35_REPAYMENT_PERIOD = r35_REPAYMENT_PERIOD;
	}
	public String getR35_PERFORMANCE_STATUS() {
		return R35_PERFORMANCE_STATUS;
	}
	public void setR35_PERFORMANCE_STATUS(String r35_PERFORMANCE_STATUS) {
		R35_PERFORMANCE_STATUS = r35_PERFORMANCE_STATUS;
	}
	public String getR35_SECURITY() {
		return R35_SECURITY;
	}
	public void setR35_SECURITY(String r35_SECURITY) {
		R35_SECURITY = r35_SECURITY;
	}
	public String getR35_BOARD_APPROVAL() {
		return R35_BOARD_APPROVAL;
	}
	public void setR35_BOARD_APPROVAL(String r35_BOARD_APPROVAL) {
		R35_BOARD_APPROVAL = r35_BOARD_APPROVAL;
	}
	public BigDecimal getR35_INTEREST_RATE() {
		return R35_INTEREST_RATE;
	}
	public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
		R35_INTEREST_RATE = r35_INTEREST_RATE;
	}
	public BigDecimal getR35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r35_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR35_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R35_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR35_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r35_LIMIT_PCT_UNIMPAIRED_CAP) {
		R35_LIMIT_PCT_UNIMPAIRED_CAP = r35_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR36_NO_OF_GROUP() {
		return R36_NO_OF_GROUP;
	}
	public void setR36_NO_OF_GROUP(String r36_NO_OF_GROUP) {
		R36_NO_OF_GROUP = r36_NO_OF_GROUP;
	}
	public String getR36_NO_OF_CUSTOMER() {
		return R36_NO_OF_CUSTOMER;
	}
	public void setR36_NO_OF_CUSTOMER(String r36_NO_OF_CUSTOMER) {
		R36_NO_OF_CUSTOMER = r36_NO_OF_CUSTOMER;
	}
	public String getR36_SECTOR_TYPE() {
		return R36_SECTOR_TYPE;
	}
	public void setR36_SECTOR_TYPE(String r36_SECTOR_TYPE) {
		R36_SECTOR_TYPE = r36_SECTOR_TYPE;
	}
	public String getR36_FACILITY_TYPE() {
		return R36_FACILITY_TYPE;
	}
	public void setR36_FACILITY_TYPE(String r36_FACILITY_TYPE) {
		R36_FACILITY_TYPE = r36_FACILITY_TYPE;
	}
	public BigDecimal getR36_ORIGINAL_AMOUNT() {
		return R36_ORIGINAL_AMOUNT;
	}
	public void setR36_ORIGINAL_AMOUNT(BigDecimal r36_ORIGINAL_AMOUNT) {
		R36_ORIGINAL_AMOUNT = r36_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR36_UTILISATION_OUTSTANDING_BAL() {
		return R36_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR36_UTILISATION_OUTSTANDING_BAL(BigDecimal r36_UTILISATION_OUTSTANDING_BAL) {
		R36_UTILISATION_OUTSTANDING_BAL = r36_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR36_EFFECTIVE_DATE() {
		return R36_EFFECTIVE_DATE;
	}
	public void setR36_EFFECTIVE_DATE(Date r36_EFFECTIVE_DATE) {
		R36_EFFECTIVE_DATE = r36_EFFECTIVE_DATE;
	}
	public String getR36_REPAYMENT_PERIOD() {
		return R36_REPAYMENT_PERIOD;
	}
	public void setR36_REPAYMENT_PERIOD(String r36_REPAYMENT_PERIOD) {
		R36_REPAYMENT_PERIOD = r36_REPAYMENT_PERIOD;
	}
	public String getR36_PERFORMANCE_STATUS() {
		return R36_PERFORMANCE_STATUS;
	}
	public void setR36_PERFORMANCE_STATUS(String r36_PERFORMANCE_STATUS) {
		R36_PERFORMANCE_STATUS = r36_PERFORMANCE_STATUS;
	}
	public String getR36_SECURITY() {
		return R36_SECURITY;
	}
	public void setR36_SECURITY(String r36_SECURITY) {
		R36_SECURITY = r36_SECURITY;
	}
	public String getR36_BOARD_APPROVAL() {
		return R36_BOARD_APPROVAL;
	}
	public void setR36_BOARD_APPROVAL(String r36_BOARD_APPROVAL) {
		R36_BOARD_APPROVAL = r36_BOARD_APPROVAL;
	}
	public BigDecimal getR36_INTEREST_RATE() {
		return R36_INTEREST_RATE;
	}
	public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
		R36_INTEREST_RATE = r36_INTEREST_RATE;
	}
	public BigDecimal getR36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r36_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR36_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R36_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR36_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r36_LIMIT_PCT_UNIMPAIRED_CAP) {
		R36_LIMIT_PCT_UNIMPAIRED_CAP = r36_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR37_NO_OF_GROUP() {
		return R37_NO_OF_GROUP;
	}
	public void setR37_NO_OF_GROUP(String r37_NO_OF_GROUP) {
		R37_NO_OF_GROUP = r37_NO_OF_GROUP;
	}
	public String getR37_NO_OF_CUSTOMER() {
		return R37_NO_OF_CUSTOMER;
	}
	public void setR37_NO_OF_CUSTOMER(String r37_NO_OF_CUSTOMER) {
		R37_NO_OF_CUSTOMER = r37_NO_OF_CUSTOMER;
	}
	public String getR37_SECTOR_TYPE() {
		return R37_SECTOR_TYPE;
	}
	public void setR37_SECTOR_TYPE(String r37_SECTOR_TYPE) {
		R37_SECTOR_TYPE = r37_SECTOR_TYPE;
	}
	public String getR37_FACILITY_TYPE() {
		return R37_FACILITY_TYPE;
	}
	public void setR37_FACILITY_TYPE(String r37_FACILITY_TYPE) {
		R37_FACILITY_TYPE = r37_FACILITY_TYPE;
	}
	public BigDecimal getR37_ORIGINAL_AMOUNT() {
		return R37_ORIGINAL_AMOUNT;
	}
	public void setR37_ORIGINAL_AMOUNT(BigDecimal r37_ORIGINAL_AMOUNT) {
		R37_ORIGINAL_AMOUNT = r37_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR37_UTILISATION_OUTSTANDING_BAL() {
		return R37_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR37_UTILISATION_OUTSTANDING_BAL(BigDecimal r37_UTILISATION_OUTSTANDING_BAL) {
		R37_UTILISATION_OUTSTANDING_BAL = r37_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR37_EFFECTIVE_DATE() {
		return R37_EFFECTIVE_DATE;
	}
	public void setR37_EFFECTIVE_DATE(Date r37_EFFECTIVE_DATE) {
		R37_EFFECTIVE_DATE = r37_EFFECTIVE_DATE;
	}
	public String getR37_REPAYMENT_PERIOD() {
		return R37_REPAYMENT_PERIOD;
	}
	public void setR37_REPAYMENT_PERIOD(String r37_REPAYMENT_PERIOD) {
		R37_REPAYMENT_PERIOD = r37_REPAYMENT_PERIOD;
	}
	public String getR37_PERFORMANCE_STATUS() {
		return R37_PERFORMANCE_STATUS;
	}
	public void setR37_PERFORMANCE_STATUS(String r37_PERFORMANCE_STATUS) {
		R37_PERFORMANCE_STATUS = r37_PERFORMANCE_STATUS;
	}
	public String getR37_SECURITY() {
		return R37_SECURITY;
	}
	public void setR37_SECURITY(String r37_SECURITY) {
		R37_SECURITY = r37_SECURITY;
	}
	public String getR37_BOARD_APPROVAL() {
		return R37_BOARD_APPROVAL;
	}
	public void setR37_BOARD_APPROVAL(String r37_BOARD_APPROVAL) {
		R37_BOARD_APPROVAL = r37_BOARD_APPROVAL;
	}
	public BigDecimal getR37_INTEREST_RATE() {
		return R37_INTEREST_RATE;
	}
	public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
		R37_INTEREST_RATE = r37_INTEREST_RATE;
	}
	public BigDecimal getR37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r37_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR37_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R37_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR37_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r37_LIMIT_PCT_UNIMPAIRED_CAP) {
		R37_LIMIT_PCT_UNIMPAIRED_CAP = r37_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR38_NO_OF_GROUP() {
		return R38_NO_OF_GROUP;
	}
	public void setR38_NO_OF_GROUP(String r38_NO_OF_GROUP) {
		R38_NO_OF_GROUP = r38_NO_OF_GROUP;
	}
	public String getR38_NO_OF_CUSTOMER() {
		return R38_NO_OF_CUSTOMER;
	}
	public void setR38_NO_OF_CUSTOMER(String r38_NO_OF_CUSTOMER) {
		R38_NO_OF_CUSTOMER = r38_NO_OF_CUSTOMER;
	}
	public String getR38_SECTOR_TYPE() {
		return R38_SECTOR_TYPE;
	}
	public void setR38_SECTOR_TYPE(String r38_SECTOR_TYPE) {
		R38_SECTOR_TYPE = r38_SECTOR_TYPE;
	}
	public String getR38_FACILITY_TYPE() {
		return R38_FACILITY_TYPE;
	}
	public void setR38_FACILITY_TYPE(String r38_FACILITY_TYPE) {
		R38_FACILITY_TYPE = r38_FACILITY_TYPE;
	}
	public BigDecimal getR38_ORIGINAL_AMOUNT() {
		return R38_ORIGINAL_AMOUNT;
	}
	public void setR38_ORIGINAL_AMOUNT(BigDecimal r38_ORIGINAL_AMOUNT) {
		R38_ORIGINAL_AMOUNT = r38_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR38_UTILISATION_OUTSTANDING_BAL() {
		return R38_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR38_UTILISATION_OUTSTANDING_BAL(BigDecimal r38_UTILISATION_OUTSTANDING_BAL) {
		R38_UTILISATION_OUTSTANDING_BAL = r38_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR38_EFFECTIVE_DATE() {
		return R38_EFFECTIVE_DATE;
	}
	public void setR38_EFFECTIVE_DATE(Date r38_EFFECTIVE_DATE) {
		R38_EFFECTIVE_DATE = r38_EFFECTIVE_DATE;
	}
	public String getR38_REPAYMENT_PERIOD() {
		return R38_REPAYMENT_PERIOD;
	}
	public void setR38_REPAYMENT_PERIOD(String r38_REPAYMENT_PERIOD) {
		R38_REPAYMENT_PERIOD = r38_REPAYMENT_PERIOD;
	}
	public String getR38_PERFORMANCE_STATUS() {
		return R38_PERFORMANCE_STATUS;
	}
	public void setR38_PERFORMANCE_STATUS(String r38_PERFORMANCE_STATUS) {
		R38_PERFORMANCE_STATUS = r38_PERFORMANCE_STATUS;
	}
	public String getR38_SECURITY() {
		return R38_SECURITY;
	}
	public void setR38_SECURITY(String r38_SECURITY) {
		R38_SECURITY = r38_SECURITY;
	}
	public String getR38_BOARD_APPROVAL() {
		return R38_BOARD_APPROVAL;
	}
	public void setR38_BOARD_APPROVAL(String r38_BOARD_APPROVAL) {
		R38_BOARD_APPROVAL = r38_BOARD_APPROVAL;
	}
	public BigDecimal getR38_INTEREST_RATE() {
		return R38_INTEREST_RATE;
	}
	public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
		R38_INTEREST_RATE = r38_INTEREST_RATE;
	}
	public BigDecimal getR38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r38_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR38_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R38_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR38_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r38_LIMIT_PCT_UNIMPAIRED_CAP) {
		R38_LIMIT_PCT_UNIMPAIRED_CAP = r38_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR39_NO_OF_GROUP() {
		return R39_NO_OF_GROUP;
	}
	public void setR39_NO_OF_GROUP(String r39_NO_OF_GROUP) {
		R39_NO_OF_GROUP = r39_NO_OF_GROUP;
	}
	public String getR39_NO_OF_CUSTOMER() {
		return R39_NO_OF_CUSTOMER;
	}
	public void setR39_NO_OF_CUSTOMER(String r39_NO_OF_CUSTOMER) {
		R39_NO_OF_CUSTOMER = r39_NO_OF_CUSTOMER;
	}
	public String getR39_SECTOR_TYPE() {
		return R39_SECTOR_TYPE;
	}
	public void setR39_SECTOR_TYPE(String r39_SECTOR_TYPE) {
		R39_SECTOR_TYPE = r39_SECTOR_TYPE;
	}
	public String getR39_FACILITY_TYPE() {
		return R39_FACILITY_TYPE;
	}
	public void setR39_FACILITY_TYPE(String r39_FACILITY_TYPE) {
		R39_FACILITY_TYPE = r39_FACILITY_TYPE;
	}
	public BigDecimal getR39_ORIGINAL_AMOUNT() {
		return R39_ORIGINAL_AMOUNT;
	}
	public void setR39_ORIGINAL_AMOUNT(BigDecimal r39_ORIGINAL_AMOUNT) {
		R39_ORIGINAL_AMOUNT = r39_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR39_UTILISATION_OUTSTANDING_BAL() {
		return R39_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR39_UTILISATION_OUTSTANDING_BAL(BigDecimal r39_UTILISATION_OUTSTANDING_BAL) {
		R39_UTILISATION_OUTSTANDING_BAL = r39_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR39_EFFECTIVE_DATE() {
		return R39_EFFECTIVE_DATE;
	}
	public void setR39_EFFECTIVE_DATE(Date r39_EFFECTIVE_DATE) {
		R39_EFFECTIVE_DATE = r39_EFFECTIVE_DATE;
	}
	public String getR39_REPAYMENT_PERIOD() {
		return R39_REPAYMENT_PERIOD;
	}
	public void setR39_REPAYMENT_PERIOD(String r39_REPAYMENT_PERIOD) {
		R39_REPAYMENT_PERIOD = r39_REPAYMENT_PERIOD;
	}
	public String getR39_PERFORMANCE_STATUS() {
		return R39_PERFORMANCE_STATUS;
	}
	public void setR39_PERFORMANCE_STATUS(String r39_PERFORMANCE_STATUS) {
		R39_PERFORMANCE_STATUS = r39_PERFORMANCE_STATUS;
	}
	public String getR39_SECURITY() {
		return R39_SECURITY;
	}
	public void setR39_SECURITY(String r39_SECURITY) {
		R39_SECURITY = r39_SECURITY;
	}
	public String getR39_BOARD_APPROVAL() {
		return R39_BOARD_APPROVAL;
	}
	public void setR39_BOARD_APPROVAL(String r39_BOARD_APPROVAL) {
		R39_BOARD_APPROVAL = r39_BOARD_APPROVAL;
	}
	public BigDecimal getR39_INTEREST_RATE() {
		return R39_INTEREST_RATE;
	}
	public void setR39_INTEREST_RATE(BigDecimal r39_INTEREST_RATE) {
		R39_INTEREST_RATE = r39_INTEREST_RATE;
	}
	public BigDecimal getR39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r39_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR39_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R39_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR39_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r39_LIMIT_PCT_UNIMPAIRED_CAP) {
		R39_LIMIT_PCT_UNIMPAIRED_CAP = r39_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR40_NO_OF_GROUP() {
		return R40_NO_OF_GROUP;
	}
	public void setR40_NO_OF_GROUP(String r40_NO_OF_GROUP) {
		R40_NO_OF_GROUP = r40_NO_OF_GROUP;
	}
	public String getR40_NO_OF_CUSTOMER() {
		return R40_NO_OF_CUSTOMER;
	}
	public void setR40_NO_OF_CUSTOMER(String r40_NO_OF_CUSTOMER) {
		R40_NO_OF_CUSTOMER = r40_NO_OF_CUSTOMER;
	}
	public String getR40_SECTOR_TYPE() {
		return R40_SECTOR_TYPE;
	}
	public void setR40_SECTOR_TYPE(String r40_SECTOR_TYPE) {
		R40_SECTOR_TYPE = r40_SECTOR_TYPE;
	}
	public String getR40_FACILITY_TYPE() {
		return R40_FACILITY_TYPE;
	}
	public void setR40_FACILITY_TYPE(String r40_FACILITY_TYPE) {
		R40_FACILITY_TYPE = r40_FACILITY_TYPE;
	}
	public BigDecimal getR40_ORIGINAL_AMOUNT() {
		return R40_ORIGINAL_AMOUNT;
	}
	public void setR40_ORIGINAL_AMOUNT(BigDecimal r40_ORIGINAL_AMOUNT) {
		R40_ORIGINAL_AMOUNT = r40_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR40_UTILISATION_OUTSTANDING_BAL() {
		return R40_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR40_UTILISATION_OUTSTANDING_BAL(BigDecimal r40_UTILISATION_OUTSTANDING_BAL) {
		R40_UTILISATION_OUTSTANDING_BAL = r40_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR40_EFFECTIVE_DATE() {
		return R40_EFFECTIVE_DATE;
	}
	public void setR40_EFFECTIVE_DATE(Date r40_EFFECTIVE_DATE) {
		R40_EFFECTIVE_DATE = r40_EFFECTIVE_DATE;
	}
	public String getR40_REPAYMENT_PERIOD() {
		return R40_REPAYMENT_PERIOD;
	}
	public void setR40_REPAYMENT_PERIOD(String r40_REPAYMENT_PERIOD) {
		R40_REPAYMENT_PERIOD = r40_REPAYMENT_PERIOD;
	}
	public String getR40_PERFORMANCE_STATUS() {
		return R40_PERFORMANCE_STATUS;
	}
	public void setR40_PERFORMANCE_STATUS(String r40_PERFORMANCE_STATUS) {
		R40_PERFORMANCE_STATUS = r40_PERFORMANCE_STATUS;
	}
	public String getR40_SECURITY() {
		return R40_SECURITY;
	}
	public void setR40_SECURITY(String r40_SECURITY) {
		R40_SECURITY = r40_SECURITY;
	}
	public String getR40_BOARD_APPROVAL() {
		return R40_BOARD_APPROVAL;
	}
	public void setR40_BOARD_APPROVAL(String r40_BOARD_APPROVAL) {
		R40_BOARD_APPROVAL = r40_BOARD_APPROVAL;
	}
	public BigDecimal getR40_INTEREST_RATE() {
		return R40_INTEREST_RATE;
	}
	public void setR40_INTEREST_RATE(BigDecimal r40_INTEREST_RATE) {
		R40_INTEREST_RATE = r40_INTEREST_RATE;
	}
	public BigDecimal getR40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r40_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR40_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R40_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR40_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r40_LIMIT_PCT_UNIMPAIRED_CAP) {
		R40_LIMIT_PCT_UNIMPAIRED_CAP = r40_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR41_NO_OF_GROUP() {
		return R41_NO_OF_GROUP;
	}
	public void setR41_NO_OF_GROUP(String r41_NO_OF_GROUP) {
		R41_NO_OF_GROUP = r41_NO_OF_GROUP;
	}
	public String getR41_NO_OF_CUSTOMER() {
		return R41_NO_OF_CUSTOMER;
	}
	public void setR41_NO_OF_CUSTOMER(String r41_NO_OF_CUSTOMER) {
		R41_NO_OF_CUSTOMER = r41_NO_OF_CUSTOMER;
	}
	public String getR41_SECTOR_TYPE() {
		return R41_SECTOR_TYPE;
	}
	public void setR41_SECTOR_TYPE(String r41_SECTOR_TYPE) {
		R41_SECTOR_TYPE = r41_SECTOR_TYPE;
	}
	public String getR41_FACILITY_TYPE() {
		return R41_FACILITY_TYPE;
	}
	public void setR41_FACILITY_TYPE(String r41_FACILITY_TYPE) {
		R41_FACILITY_TYPE = r41_FACILITY_TYPE;
	}
	public BigDecimal getR41_ORIGINAL_AMOUNT() {
		return R41_ORIGINAL_AMOUNT;
	}
	public void setR41_ORIGINAL_AMOUNT(BigDecimal r41_ORIGINAL_AMOUNT) {
		R41_ORIGINAL_AMOUNT = r41_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR41_UTILISATION_OUTSTANDING_BAL() {
		return R41_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR41_UTILISATION_OUTSTANDING_BAL(BigDecimal r41_UTILISATION_OUTSTANDING_BAL) {
		R41_UTILISATION_OUTSTANDING_BAL = r41_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR41_EFFECTIVE_DATE() {
		return R41_EFFECTIVE_DATE;
	}
	public void setR41_EFFECTIVE_DATE(Date r41_EFFECTIVE_DATE) {
		R41_EFFECTIVE_DATE = r41_EFFECTIVE_DATE;
	}
	public String getR41_REPAYMENT_PERIOD() {
		return R41_REPAYMENT_PERIOD;
	}
	public void setR41_REPAYMENT_PERIOD(String r41_REPAYMENT_PERIOD) {
		R41_REPAYMENT_PERIOD = r41_REPAYMENT_PERIOD;
	}
	public String getR41_PERFORMANCE_STATUS() {
		return R41_PERFORMANCE_STATUS;
	}
	public void setR41_PERFORMANCE_STATUS(String r41_PERFORMANCE_STATUS) {
		R41_PERFORMANCE_STATUS = r41_PERFORMANCE_STATUS;
	}
	public String getR41_SECURITY() {
		return R41_SECURITY;
	}
	public void setR41_SECURITY(String r41_SECURITY) {
		R41_SECURITY = r41_SECURITY;
	}
	public String getR41_BOARD_APPROVAL() {
		return R41_BOARD_APPROVAL;
	}
	public void setR41_BOARD_APPROVAL(String r41_BOARD_APPROVAL) {
		R41_BOARD_APPROVAL = r41_BOARD_APPROVAL;
	}
	public BigDecimal getR41_INTEREST_RATE() {
		return R41_INTEREST_RATE;
	}
	public void setR41_INTEREST_RATE(BigDecimal r41_INTEREST_RATE) {
		R41_INTEREST_RATE = r41_INTEREST_RATE;
	}
	public BigDecimal getR41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r41_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR41_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R41_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR41_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r41_LIMIT_PCT_UNIMPAIRED_CAP) {
		R41_LIMIT_PCT_UNIMPAIRED_CAP = r41_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR42_NO_OF_GROUP() {
		return R42_NO_OF_GROUP;
	}
	public void setR42_NO_OF_GROUP(String r42_NO_OF_GROUP) {
		R42_NO_OF_GROUP = r42_NO_OF_GROUP;
	}
	public String getR42_NO_OF_CUSTOMER() {
		return R42_NO_OF_CUSTOMER;
	}
	public void setR42_NO_OF_CUSTOMER(String r42_NO_OF_CUSTOMER) {
		R42_NO_OF_CUSTOMER = r42_NO_OF_CUSTOMER;
	}
	public String getR42_SECTOR_TYPE() {
		return R42_SECTOR_TYPE;
	}
	public void setR42_SECTOR_TYPE(String r42_SECTOR_TYPE) {
		R42_SECTOR_TYPE = r42_SECTOR_TYPE;
	}
	public String getR42_FACILITY_TYPE() {
		return R42_FACILITY_TYPE;
	}
	public void setR42_FACILITY_TYPE(String r42_FACILITY_TYPE) {
		R42_FACILITY_TYPE = r42_FACILITY_TYPE;
	}
	public BigDecimal getR42_ORIGINAL_AMOUNT() {
		return R42_ORIGINAL_AMOUNT;
	}
	public void setR42_ORIGINAL_AMOUNT(BigDecimal r42_ORIGINAL_AMOUNT) {
		R42_ORIGINAL_AMOUNT = r42_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR42_UTILISATION_OUTSTANDING_BAL() {
		return R42_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR42_UTILISATION_OUTSTANDING_BAL(BigDecimal r42_UTILISATION_OUTSTANDING_BAL) {
		R42_UTILISATION_OUTSTANDING_BAL = r42_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR42_EFFECTIVE_DATE() {
		return R42_EFFECTIVE_DATE;
	}
	public void setR42_EFFECTIVE_DATE(Date r42_EFFECTIVE_DATE) {
		R42_EFFECTIVE_DATE = r42_EFFECTIVE_DATE;
	}
	public String getR42_REPAYMENT_PERIOD() {
		return R42_REPAYMENT_PERIOD;
	}
	public void setR42_REPAYMENT_PERIOD(String r42_REPAYMENT_PERIOD) {
		R42_REPAYMENT_PERIOD = r42_REPAYMENT_PERIOD;
	}
	public String getR42_PERFORMANCE_STATUS() {
		return R42_PERFORMANCE_STATUS;
	}
	public void setR42_PERFORMANCE_STATUS(String r42_PERFORMANCE_STATUS) {
		R42_PERFORMANCE_STATUS = r42_PERFORMANCE_STATUS;
	}
	public String getR42_SECURITY() {
		return R42_SECURITY;
	}
	public void setR42_SECURITY(String r42_SECURITY) {
		R42_SECURITY = r42_SECURITY;
	}
	public String getR42_BOARD_APPROVAL() {
		return R42_BOARD_APPROVAL;
	}
	public void setR42_BOARD_APPROVAL(String r42_BOARD_APPROVAL) {
		R42_BOARD_APPROVAL = r42_BOARD_APPROVAL;
	}
	public BigDecimal getR42_INTEREST_RATE() {
		return R42_INTEREST_RATE;
	}
	public void setR42_INTEREST_RATE(BigDecimal r42_INTEREST_RATE) {
		R42_INTEREST_RATE = r42_INTEREST_RATE;
	}
	public BigDecimal getR42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r42_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR42_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R42_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR42_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r42_LIMIT_PCT_UNIMPAIRED_CAP) {
		R42_LIMIT_PCT_UNIMPAIRED_CAP = r42_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR43_NO_OF_GROUP() {
		return R43_NO_OF_GROUP;
	}
	public void setR43_NO_OF_GROUP(String r43_NO_OF_GROUP) {
		R43_NO_OF_GROUP = r43_NO_OF_GROUP;
	}
	public String getR43_NO_OF_CUSTOMER() {
		return R43_NO_OF_CUSTOMER;
	}
	public void setR43_NO_OF_CUSTOMER(String r43_NO_OF_CUSTOMER) {
		R43_NO_OF_CUSTOMER = r43_NO_OF_CUSTOMER;
	}
	public String getR43_SECTOR_TYPE() {
		return R43_SECTOR_TYPE;
	}
	public void setR43_SECTOR_TYPE(String r43_SECTOR_TYPE) {
		R43_SECTOR_TYPE = r43_SECTOR_TYPE;
	}
	public String getR43_FACILITY_TYPE() {
		return R43_FACILITY_TYPE;
	}
	public void setR43_FACILITY_TYPE(String r43_FACILITY_TYPE) {
		R43_FACILITY_TYPE = r43_FACILITY_TYPE;
	}
	public BigDecimal getR43_ORIGINAL_AMOUNT() {
		return R43_ORIGINAL_AMOUNT;
	}
	public void setR43_ORIGINAL_AMOUNT(BigDecimal r43_ORIGINAL_AMOUNT) {
		R43_ORIGINAL_AMOUNT = r43_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR43_UTILISATION_OUTSTANDING_BAL() {
		return R43_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR43_UTILISATION_OUTSTANDING_BAL(BigDecimal r43_UTILISATION_OUTSTANDING_BAL) {
		R43_UTILISATION_OUTSTANDING_BAL = r43_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR43_EFFECTIVE_DATE() {
		return R43_EFFECTIVE_DATE;
	}
	public void setR43_EFFECTIVE_DATE(Date r43_EFFECTIVE_DATE) {
		R43_EFFECTIVE_DATE = r43_EFFECTIVE_DATE;
	}
	public String getR43_REPAYMENT_PERIOD() {
		return R43_REPAYMENT_PERIOD;
	}
	public void setR43_REPAYMENT_PERIOD(String r43_REPAYMENT_PERIOD) {
		R43_REPAYMENT_PERIOD = r43_REPAYMENT_PERIOD;
	}
	public String getR43_PERFORMANCE_STATUS() {
		return R43_PERFORMANCE_STATUS;
	}
	public void setR43_PERFORMANCE_STATUS(String r43_PERFORMANCE_STATUS) {
		R43_PERFORMANCE_STATUS = r43_PERFORMANCE_STATUS;
	}
	public String getR43_SECURITY() {
		return R43_SECURITY;
	}
	public void setR43_SECURITY(String r43_SECURITY) {
		R43_SECURITY = r43_SECURITY;
	}
	public String getR43_BOARD_APPROVAL() {
		return R43_BOARD_APPROVAL;
	}
	public void setR43_BOARD_APPROVAL(String r43_BOARD_APPROVAL) {
		R43_BOARD_APPROVAL = r43_BOARD_APPROVAL;
	}
	public BigDecimal getR43_INTEREST_RATE() {
		return R43_INTEREST_RATE;
	}
	public void setR43_INTEREST_RATE(BigDecimal r43_INTEREST_RATE) {
		R43_INTEREST_RATE = r43_INTEREST_RATE;
	}
	public BigDecimal getR43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r43_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR43_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R43_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR43_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r43_LIMIT_PCT_UNIMPAIRED_CAP) {
		R43_LIMIT_PCT_UNIMPAIRED_CAP = r43_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR44_NO_OF_GROUP() {
		return R44_NO_OF_GROUP;
	}
	public void setR44_NO_OF_GROUP(String r44_NO_OF_GROUP) {
		R44_NO_OF_GROUP = r44_NO_OF_GROUP;
	}
	public String getR44_NO_OF_CUSTOMER() {
		return R44_NO_OF_CUSTOMER;
	}
	public void setR44_NO_OF_CUSTOMER(String r44_NO_OF_CUSTOMER) {
		R44_NO_OF_CUSTOMER = r44_NO_OF_CUSTOMER;
	}
	public String getR44_SECTOR_TYPE() {
		return R44_SECTOR_TYPE;
	}
	public void setR44_SECTOR_TYPE(String r44_SECTOR_TYPE) {
		R44_SECTOR_TYPE = r44_SECTOR_TYPE;
	}
	public String getR44_FACILITY_TYPE() {
		return R44_FACILITY_TYPE;
	}
	public void setR44_FACILITY_TYPE(String r44_FACILITY_TYPE) {
		R44_FACILITY_TYPE = r44_FACILITY_TYPE;
	}
	public BigDecimal getR44_ORIGINAL_AMOUNT() {
		return R44_ORIGINAL_AMOUNT;
	}
	public void setR44_ORIGINAL_AMOUNT(BigDecimal r44_ORIGINAL_AMOUNT) {
		R44_ORIGINAL_AMOUNT = r44_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR44_UTILISATION_OUTSTANDING_BAL() {
		return R44_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR44_UTILISATION_OUTSTANDING_BAL(BigDecimal r44_UTILISATION_OUTSTANDING_BAL) {
		R44_UTILISATION_OUTSTANDING_BAL = r44_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR44_EFFECTIVE_DATE() {
		return R44_EFFECTIVE_DATE;
	}
	public void setR44_EFFECTIVE_DATE(Date r44_EFFECTIVE_DATE) {
		R44_EFFECTIVE_DATE = r44_EFFECTIVE_DATE;
	}
	public String getR44_REPAYMENT_PERIOD() {
		return R44_REPAYMENT_PERIOD;
	}
	public void setR44_REPAYMENT_PERIOD(String r44_REPAYMENT_PERIOD) {
		R44_REPAYMENT_PERIOD = r44_REPAYMENT_PERIOD;
	}
	public String getR44_PERFORMANCE_STATUS() {
		return R44_PERFORMANCE_STATUS;
	}
	public void setR44_PERFORMANCE_STATUS(String r44_PERFORMANCE_STATUS) {
		R44_PERFORMANCE_STATUS = r44_PERFORMANCE_STATUS;
	}
	public String getR44_SECURITY() {
		return R44_SECURITY;
	}
	public void setR44_SECURITY(String r44_SECURITY) {
		R44_SECURITY = r44_SECURITY;
	}
	public String getR44_BOARD_APPROVAL() {
		return R44_BOARD_APPROVAL;
	}
	public void setR44_BOARD_APPROVAL(String r44_BOARD_APPROVAL) {
		R44_BOARD_APPROVAL = r44_BOARD_APPROVAL;
	}
	public BigDecimal getR44_INTEREST_RATE() {
		return R44_INTEREST_RATE;
	}
	public void setR44_INTEREST_RATE(BigDecimal r44_INTEREST_RATE) {
		R44_INTEREST_RATE = r44_INTEREST_RATE;
	}
	public BigDecimal getR44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r44_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR44_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R44_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR44_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r44_LIMIT_PCT_UNIMPAIRED_CAP) {
		R44_LIMIT_PCT_UNIMPAIRED_CAP = r44_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR45_NO_OF_GROUP() {
		return R45_NO_OF_GROUP;
	}
	public void setR45_NO_OF_GROUP(String r45_NO_OF_GROUP) {
		R45_NO_OF_GROUP = r45_NO_OF_GROUP;
	}
	public String getR45_NO_OF_CUSTOMER() {
		return R45_NO_OF_CUSTOMER;
	}
	public void setR45_NO_OF_CUSTOMER(String r45_NO_OF_CUSTOMER) {
		R45_NO_OF_CUSTOMER = r45_NO_OF_CUSTOMER;
	}
	public String getR45_SECTOR_TYPE() {
		return R45_SECTOR_TYPE;
	}
	public void setR45_SECTOR_TYPE(String r45_SECTOR_TYPE) {
		R45_SECTOR_TYPE = r45_SECTOR_TYPE;
	}
	public String getR45_FACILITY_TYPE() {
		return R45_FACILITY_TYPE;
	}
	public void setR45_FACILITY_TYPE(String r45_FACILITY_TYPE) {
		R45_FACILITY_TYPE = r45_FACILITY_TYPE;
	}
	public BigDecimal getR45_ORIGINAL_AMOUNT() {
		return R45_ORIGINAL_AMOUNT;
	}
	public void setR45_ORIGINAL_AMOUNT(BigDecimal r45_ORIGINAL_AMOUNT) {
		R45_ORIGINAL_AMOUNT = r45_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR45_UTILISATION_OUTSTANDING_BAL() {
		return R45_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR45_UTILISATION_OUTSTANDING_BAL(BigDecimal r45_UTILISATION_OUTSTANDING_BAL) {
		R45_UTILISATION_OUTSTANDING_BAL = r45_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR45_EFFECTIVE_DATE() {
		return R45_EFFECTIVE_DATE;
	}
	public void setR45_EFFECTIVE_DATE(Date r45_EFFECTIVE_DATE) {
		R45_EFFECTIVE_DATE = r45_EFFECTIVE_DATE;
	}
	public String getR45_REPAYMENT_PERIOD() {
		return R45_REPAYMENT_PERIOD;
	}
	public void setR45_REPAYMENT_PERIOD(String r45_REPAYMENT_PERIOD) {
		R45_REPAYMENT_PERIOD = r45_REPAYMENT_PERIOD;
	}
	public String getR45_PERFORMANCE_STATUS() {
		return R45_PERFORMANCE_STATUS;
	}
	public void setR45_PERFORMANCE_STATUS(String r45_PERFORMANCE_STATUS) {
		R45_PERFORMANCE_STATUS = r45_PERFORMANCE_STATUS;
	}
	public String getR45_SECURITY() {
		return R45_SECURITY;
	}
	public void setR45_SECURITY(String r45_SECURITY) {
		R45_SECURITY = r45_SECURITY;
	}
	public String getR45_BOARD_APPROVAL() {
		return R45_BOARD_APPROVAL;
	}
	public void setR45_BOARD_APPROVAL(String r45_BOARD_APPROVAL) {
		R45_BOARD_APPROVAL = r45_BOARD_APPROVAL;
	}
	public BigDecimal getR45_INTEREST_RATE() {
		return R45_INTEREST_RATE;
	}
	public void setR45_INTEREST_RATE(BigDecimal r45_INTEREST_RATE) {
		R45_INTEREST_RATE = r45_INTEREST_RATE;
	}
	public BigDecimal getR45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r45_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR45_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R45_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR45_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r45_LIMIT_PCT_UNIMPAIRED_CAP) {
		R45_LIMIT_PCT_UNIMPAIRED_CAP = r45_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR46_NO_OF_GROUP() {
		return R46_NO_OF_GROUP;
	}
	public void setR46_NO_OF_GROUP(String r46_NO_OF_GROUP) {
		R46_NO_OF_GROUP = r46_NO_OF_GROUP;
	}
	public String getR46_NO_OF_CUSTOMER() {
		return R46_NO_OF_CUSTOMER;
	}
	public void setR46_NO_OF_CUSTOMER(String r46_NO_OF_CUSTOMER) {
		R46_NO_OF_CUSTOMER = r46_NO_OF_CUSTOMER;
	}
	public String getR46_SECTOR_TYPE() {
		return R46_SECTOR_TYPE;
	}
	public void setR46_SECTOR_TYPE(String r46_SECTOR_TYPE) {
		R46_SECTOR_TYPE = r46_SECTOR_TYPE;
	}
	public String getR46_FACILITY_TYPE() {
		return R46_FACILITY_TYPE;
	}
	public void setR46_FACILITY_TYPE(String r46_FACILITY_TYPE) {
		R46_FACILITY_TYPE = r46_FACILITY_TYPE;
	}
	public BigDecimal getR46_ORIGINAL_AMOUNT() {
		return R46_ORIGINAL_AMOUNT;
	}
	public void setR46_ORIGINAL_AMOUNT(BigDecimal r46_ORIGINAL_AMOUNT) {
		R46_ORIGINAL_AMOUNT = r46_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR46_UTILISATION_OUTSTANDING_BAL() {
		return R46_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR46_UTILISATION_OUTSTANDING_BAL(BigDecimal r46_UTILISATION_OUTSTANDING_BAL) {
		R46_UTILISATION_OUTSTANDING_BAL = r46_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR46_EFFECTIVE_DATE() {
		return R46_EFFECTIVE_DATE;
	}
	public void setR46_EFFECTIVE_DATE(Date r46_EFFECTIVE_DATE) {
		R46_EFFECTIVE_DATE = r46_EFFECTIVE_DATE;
	}
	public String getR46_REPAYMENT_PERIOD() {
		return R46_REPAYMENT_PERIOD;
	}
	public void setR46_REPAYMENT_PERIOD(String r46_REPAYMENT_PERIOD) {
		R46_REPAYMENT_PERIOD = r46_REPAYMENT_PERIOD;
	}
	public String getR46_PERFORMANCE_STATUS() {
		return R46_PERFORMANCE_STATUS;
	}
	public void setR46_PERFORMANCE_STATUS(String r46_PERFORMANCE_STATUS) {
		R46_PERFORMANCE_STATUS = r46_PERFORMANCE_STATUS;
	}
	public String getR46_SECURITY() {
		return R46_SECURITY;
	}
	public void setR46_SECURITY(String r46_SECURITY) {
		R46_SECURITY = r46_SECURITY;
	}
	public String getR46_BOARD_APPROVAL() {
		return R46_BOARD_APPROVAL;
	}
	public void setR46_BOARD_APPROVAL(String r46_BOARD_APPROVAL) {
		R46_BOARD_APPROVAL = r46_BOARD_APPROVAL;
	}
	public BigDecimal getR46_INTEREST_RATE() {
		return R46_INTEREST_RATE;
	}
	public void setR46_INTEREST_RATE(BigDecimal r46_INTEREST_RATE) {
		R46_INTEREST_RATE = r46_INTEREST_RATE;
	}
	public BigDecimal getR46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r46_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR46_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R46_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR46_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r46_LIMIT_PCT_UNIMPAIRED_CAP) {
		R46_LIMIT_PCT_UNIMPAIRED_CAP = r46_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR47_NO_OF_GROUP() {
		return R47_NO_OF_GROUP;
	}
	public void setR47_NO_OF_GROUP(String r47_NO_OF_GROUP) {
		R47_NO_OF_GROUP = r47_NO_OF_GROUP;
	}
	public String getR47_NO_OF_CUSTOMER() {
		return R47_NO_OF_CUSTOMER;
	}
	public void setR47_NO_OF_CUSTOMER(String r47_NO_OF_CUSTOMER) {
		R47_NO_OF_CUSTOMER = r47_NO_OF_CUSTOMER;
	}
	public String getR47_SECTOR_TYPE() {
		return R47_SECTOR_TYPE;
	}
	public void setR47_SECTOR_TYPE(String r47_SECTOR_TYPE) {
		R47_SECTOR_TYPE = r47_SECTOR_TYPE;
	}
	public String getR47_FACILITY_TYPE() {
		return R47_FACILITY_TYPE;
	}
	public void setR47_FACILITY_TYPE(String r47_FACILITY_TYPE) {
		R47_FACILITY_TYPE = r47_FACILITY_TYPE;
	}
	public BigDecimal getR47_ORIGINAL_AMOUNT() {
		return R47_ORIGINAL_AMOUNT;
	}
	public void setR47_ORIGINAL_AMOUNT(BigDecimal r47_ORIGINAL_AMOUNT) {
		R47_ORIGINAL_AMOUNT = r47_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR47_UTILISATION_OUTSTANDING_BAL() {
		return R47_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR47_UTILISATION_OUTSTANDING_BAL(BigDecimal r47_UTILISATION_OUTSTANDING_BAL) {
		R47_UTILISATION_OUTSTANDING_BAL = r47_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR47_EFFECTIVE_DATE() {
		return R47_EFFECTIVE_DATE;
	}
	public void setR47_EFFECTIVE_DATE(Date r47_EFFECTIVE_DATE) {
		R47_EFFECTIVE_DATE = r47_EFFECTIVE_DATE;
	}
	public String getR47_REPAYMENT_PERIOD() {
		return R47_REPAYMENT_PERIOD;
	}
	public void setR47_REPAYMENT_PERIOD(String r47_REPAYMENT_PERIOD) {
		R47_REPAYMENT_PERIOD = r47_REPAYMENT_PERIOD;
	}
	public String getR47_PERFORMANCE_STATUS() {
		return R47_PERFORMANCE_STATUS;
	}
	public void setR47_PERFORMANCE_STATUS(String r47_PERFORMANCE_STATUS) {
		R47_PERFORMANCE_STATUS = r47_PERFORMANCE_STATUS;
	}
	public String getR47_SECURITY() {
		return R47_SECURITY;
	}
	public void setR47_SECURITY(String r47_SECURITY) {
		R47_SECURITY = r47_SECURITY;
	}
	public String getR47_BOARD_APPROVAL() {
		return R47_BOARD_APPROVAL;
	}
	public void setR47_BOARD_APPROVAL(String r47_BOARD_APPROVAL) {
		R47_BOARD_APPROVAL = r47_BOARD_APPROVAL;
	}
	public BigDecimal getR47_INTEREST_RATE() {
		return R47_INTEREST_RATE;
	}
	public void setR47_INTEREST_RATE(BigDecimal r47_INTEREST_RATE) {
		R47_INTEREST_RATE = r47_INTEREST_RATE;
	}
	public BigDecimal getR47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r47_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR47_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R47_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR47_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r47_LIMIT_PCT_UNIMPAIRED_CAP) {
		R47_LIMIT_PCT_UNIMPAIRED_CAP = r47_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR48_NO_OF_GROUP() {
		return R48_NO_OF_GROUP;
	}
	public void setR48_NO_OF_GROUP(String r48_NO_OF_GROUP) {
		R48_NO_OF_GROUP = r48_NO_OF_GROUP;
	}
	public String getR48_NO_OF_CUSTOMER() {
		return R48_NO_OF_CUSTOMER;
	}
	public void setR48_NO_OF_CUSTOMER(String r48_NO_OF_CUSTOMER) {
		R48_NO_OF_CUSTOMER = r48_NO_OF_CUSTOMER;
	}
	public String getR48_SECTOR_TYPE() {
		return R48_SECTOR_TYPE;
	}
	public void setR48_SECTOR_TYPE(String r48_SECTOR_TYPE) {
		R48_SECTOR_TYPE = r48_SECTOR_TYPE;
	}
	public String getR48_FACILITY_TYPE() {
		return R48_FACILITY_TYPE;
	}
	public void setR48_FACILITY_TYPE(String r48_FACILITY_TYPE) {
		R48_FACILITY_TYPE = r48_FACILITY_TYPE;
	}
	public BigDecimal getR48_ORIGINAL_AMOUNT() {
		return R48_ORIGINAL_AMOUNT;
	}
	public void setR48_ORIGINAL_AMOUNT(BigDecimal r48_ORIGINAL_AMOUNT) {
		R48_ORIGINAL_AMOUNT = r48_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR48_UTILISATION_OUTSTANDING_BAL() {
		return R48_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR48_UTILISATION_OUTSTANDING_BAL(BigDecimal r48_UTILISATION_OUTSTANDING_BAL) {
		R48_UTILISATION_OUTSTANDING_BAL = r48_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR48_EFFECTIVE_DATE() {
		return R48_EFFECTIVE_DATE;
	}
	public void setR48_EFFECTIVE_DATE(Date r48_EFFECTIVE_DATE) {
		R48_EFFECTIVE_DATE = r48_EFFECTIVE_DATE;
	}
	public String getR48_REPAYMENT_PERIOD() {
		return R48_REPAYMENT_PERIOD;
	}
	public void setR48_REPAYMENT_PERIOD(String r48_REPAYMENT_PERIOD) {
		R48_REPAYMENT_PERIOD = r48_REPAYMENT_PERIOD;
	}
	public String getR48_PERFORMANCE_STATUS() {
		return R48_PERFORMANCE_STATUS;
	}
	public void setR48_PERFORMANCE_STATUS(String r48_PERFORMANCE_STATUS) {
		R48_PERFORMANCE_STATUS = r48_PERFORMANCE_STATUS;
	}
	public String getR48_SECURITY() {
		return R48_SECURITY;
	}
	public void setR48_SECURITY(String r48_SECURITY) {
		R48_SECURITY = r48_SECURITY;
	}
	public String getR48_BOARD_APPROVAL() {
		return R48_BOARD_APPROVAL;
	}
	public void setR48_BOARD_APPROVAL(String r48_BOARD_APPROVAL) {
		R48_BOARD_APPROVAL = r48_BOARD_APPROVAL;
	}
	public BigDecimal getR48_INTEREST_RATE() {
		return R48_INTEREST_RATE;
	}
	public void setR48_INTEREST_RATE(BigDecimal r48_INTEREST_RATE) {
		R48_INTEREST_RATE = r48_INTEREST_RATE;
	}
	public BigDecimal getR48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r48_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR48_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R48_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR48_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r48_LIMIT_PCT_UNIMPAIRED_CAP) {
		R48_LIMIT_PCT_UNIMPAIRED_CAP = r48_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR49_NO_OF_GROUP() {
		return R49_NO_OF_GROUP;
	}
	public void setR49_NO_OF_GROUP(String r49_NO_OF_GROUP) {
		R49_NO_OF_GROUP = r49_NO_OF_GROUP;
	}
	public String getR49_NO_OF_CUSTOMER() {
		return R49_NO_OF_CUSTOMER;
	}
	public void setR49_NO_OF_CUSTOMER(String r49_NO_OF_CUSTOMER) {
		R49_NO_OF_CUSTOMER = r49_NO_OF_CUSTOMER;
	}
	public String getR49_SECTOR_TYPE() {
		return R49_SECTOR_TYPE;
	}
	public void setR49_SECTOR_TYPE(String r49_SECTOR_TYPE) {
		R49_SECTOR_TYPE = r49_SECTOR_TYPE;
	}
	public String getR49_FACILITY_TYPE() {
		return R49_FACILITY_TYPE;
	}
	public void setR49_FACILITY_TYPE(String r49_FACILITY_TYPE) {
		R49_FACILITY_TYPE = r49_FACILITY_TYPE;
	}
	public BigDecimal getR49_ORIGINAL_AMOUNT() {
		return R49_ORIGINAL_AMOUNT;
	}
	public void setR49_ORIGINAL_AMOUNT(BigDecimal r49_ORIGINAL_AMOUNT) {
		R49_ORIGINAL_AMOUNT = r49_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR49_UTILISATION_OUTSTANDING_BAL() {
		return R49_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR49_UTILISATION_OUTSTANDING_BAL(BigDecimal r49_UTILISATION_OUTSTANDING_BAL) {
		R49_UTILISATION_OUTSTANDING_BAL = r49_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR49_EFFECTIVE_DATE() {
		return R49_EFFECTIVE_DATE;
	}
	public void setR49_EFFECTIVE_DATE(Date r49_EFFECTIVE_DATE) {
		R49_EFFECTIVE_DATE = r49_EFFECTIVE_DATE;
	}
	public String getR49_REPAYMENT_PERIOD() {
		return R49_REPAYMENT_PERIOD;
	}
	public void setR49_REPAYMENT_PERIOD(String r49_REPAYMENT_PERIOD) {
		R49_REPAYMENT_PERIOD = r49_REPAYMENT_PERIOD;
	}
	public String getR49_PERFORMANCE_STATUS() {
		return R49_PERFORMANCE_STATUS;
	}
	public void setR49_PERFORMANCE_STATUS(String r49_PERFORMANCE_STATUS) {
		R49_PERFORMANCE_STATUS = r49_PERFORMANCE_STATUS;
	}
	public String getR49_SECURITY() {
		return R49_SECURITY;
	}
	public void setR49_SECURITY(String r49_SECURITY) {
		R49_SECURITY = r49_SECURITY;
	}
	public String getR49_BOARD_APPROVAL() {
		return R49_BOARD_APPROVAL;
	}
	public void setR49_BOARD_APPROVAL(String r49_BOARD_APPROVAL) {
		R49_BOARD_APPROVAL = r49_BOARD_APPROVAL;
	}
	public BigDecimal getR49_INTEREST_RATE() {
		return R49_INTEREST_RATE;
	}
	public void setR49_INTEREST_RATE(BigDecimal r49_INTEREST_RATE) {
		R49_INTEREST_RATE = r49_INTEREST_RATE;
	}
	public BigDecimal getR49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r49_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR49_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R49_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR49_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r49_LIMIT_PCT_UNIMPAIRED_CAP) {
		R49_LIMIT_PCT_UNIMPAIRED_CAP = r49_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR50_NO_OF_GROUP() {
		return R50_NO_OF_GROUP;
	}
	public void setR50_NO_OF_GROUP(String r50_NO_OF_GROUP) {
		R50_NO_OF_GROUP = r50_NO_OF_GROUP;
	}
	public String getR50_NO_OF_CUSTOMER() {
		return R50_NO_OF_CUSTOMER;
	}
	public void setR50_NO_OF_CUSTOMER(String r50_NO_OF_CUSTOMER) {
		R50_NO_OF_CUSTOMER = r50_NO_OF_CUSTOMER;
	}
	public String getR50_SECTOR_TYPE() {
		return R50_SECTOR_TYPE;
	}
	public void setR50_SECTOR_TYPE(String r50_SECTOR_TYPE) {
		R50_SECTOR_TYPE = r50_SECTOR_TYPE;
	}
	public String getR50_FACILITY_TYPE() {
		return R50_FACILITY_TYPE;
	}
	public void setR50_FACILITY_TYPE(String r50_FACILITY_TYPE) {
		R50_FACILITY_TYPE = r50_FACILITY_TYPE;
	}
	public BigDecimal getR50_ORIGINAL_AMOUNT() {
		return R50_ORIGINAL_AMOUNT;
	}
	public void setR50_ORIGINAL_AMOUNT(BigDecimal r50_ORIGINAL_AMOUNT) {
		R50_ORIGINAL_AMOUNT = r50_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR50_UTILISATION_OUTSTANDING_BAL() {
		return R50_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR50_UTILISATION_OUTSTANDING_BAL(BigDecimal r50_UTILISATION_OUTSTANDING_BAL) {
		R50_UTILISATION_OUTSTANDING_BAL = r50_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR50_EFFECTIVE_DATE() {
		return R50_EFFECTIVE_DATE;
	}
	public void setR50_EFFECTIVE_DATE(Date r50_EFFECTIVE_DATE) {
		R50_EFFECTIVE_DATE = r50_EFFECTIVE_DATE;
	}
	public String getR50_REPAYMENT_PERIOD() {
		return R50_REPAYMENT_PERIOD;
	}
	public void setR50_REPAYMENT_PERIOD(String r50_REPAYMENT_PERIOD) {
		R50_REPAYMENT_PERIOD = r50_REPAYMENT_PERIOD;
	}
	public String getR50_PERFORMANCE_STATUS() {
		return R50_PERFORMANCE_STATUS;
	}
	public void setR50_PERFORMANCE_STATUS(String r50_PERFORMANCE_STATUS) {
		R50_PERFORMANCE_STATUS = r50_PERFORMANCE_STATUS;
	}
	public String getR50_SECURITY() {
		return R50_SECURITY;
	}
	public void setR50_SECURITY(String r50_SECURITY) {
		R50_SECURITY = r50_SECURITY;
	}
	public String getR50_BOARD_APPROVAL() {
		return R50_BOARD_APPROVAL;
	}
	public void setR50_BOARD_APPROVAL(String r50_BOARD_APPROVAL) {
		R50_BOARD_APPROVAL = r50_BOARD_APPROVAL;
	}
	public BigDecimal getR50_INTEREST_RATE() {
		return R50_INTEREST_RATE;
	}
	public void setR50_INTEREST_RATE(BigDecimal r50_INTEREST_RATE) {
		R50_INTEREST_RATE = r50_INTEREST_RATE;
	}
	public BigDecimal getR50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r50_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR50_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R50_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR50_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r50_LIMIT_PCT_UNIMPAIRED_CAP) {
		R50_LIMIT_PCT_UNIMPAIRED_CAP = r50_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR51_NO_OF_GROUP() {
		return R51_NO_OF_GROUP;
	}
	public void setR51_NO_OF_GROUP(String r51_NO_OF_GROUP) {
		R51_NO_OF_GROUP = r51_NO_OF_GROUP;
	}
	public String getR51_NO_OF_CUSTOMER() {
		return R51_NO_OF_CUSTOMER;
	}
	public void setR51_NO_OF_CUSTOMER(String r51_NO_OF_CUSTOMER) {
		R51_NO_OF_CUSTOMER = r51_NO_OF_CUSTOMER;
	}
	public String getR51_SECTOR_TYPE() {
		return R51_SECTOR_TYPE;
	}
	public void setR51_SECTOR_TYPE(String r51_SECTOR_TYPE) {
		R51_SECTOR_TYPE = r51_SECTOR_TYPE;
	}
	public String getR51_FACILITY_TYPE() {
		return R51_FACILITY_TYPE;
	}
	public void setR51_FACILITY_TYPE(String r51_FACILITY_TYPE) {
		R51_FACILITY_TYPE = r51_FACILITY_TYPE;
	}
	public BigDecimal getR51_ORIGINAL_AMOUNT() {
		return R51_ORIGINAL_AMOUNT;
	}
	public void setR51_ORIGINAL_AMOUNT(BigDecimal r51_ORIGINAL_AMOUNT) {
		R51_ORIGINAL_AMOUNT = r51_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR51_UTILISATION_OUTSTANDING_BAL() {
		return R51_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR51_UTILISATION_OUTSTANDING_BAL(BigDecimal r51_UTILISATION_OUTSTANDING_BAL) {
		R51_UTILISATION_OUTSTANDING_BAL = r51_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR51_EFFECTIVE_DATE() {
		return R51_EFFECTIVE_DATE;
	}
	public void setR51_EFFECTIVE_DATE(Date r51_EFFECTIVE_DATE) {
		R51_EFFECTIVE_DATE = r51_EFFECTIVE_DATE;
	}
	public String getR51_REPAYMENT_PERIOD() {
		return R51_REPAYMENT_PERIOD;
	}
	public void setR51_REPAYMENT_PERIOD(String r51_REPAYMENT_PERIOD) {
		R51_REPAYMENT_PERIOD = r51_REPAYMENT_PERIOD;
	}
	public String getR51_PERFORMANCE_STATUS() {
		return R51_PERFORMANCE_STATUS;
	}
	public void setR51_PERFORMANCE_STATUS(String r51_PERFORMANCE_STATUS) {
		R51_PERFORMANCE_STATUS = r51_PERFORMANCE_STATUS;
	}
	public String getR51_SECURITY() {
		return R51_SECURITY;
	}
	public void setR51_SECURITY(String r51_SECURITY) {
		R51_SECURITY = r51_SECURITY;
	}
	public String getR51_BOARD_APPROVAL() {
		return R51_BOARD_APPROVAL;
	}
	public void setR51_BOARD_APPROVAL(String r51_BOARD_APPROVAL) {
		R51_BOARD_APPROVAL = r51_BOARD_APPROVAL;
	}
	public BigDecimal getR51_INTEREST_RATE() {
		return R51_INTEREST_RATE;
	}
	public void setR51_INTEREST_RATE(BigDecimal r51_INTEREST_RATE) {
		R51_INTEREST_RATE = r51_INTEREST_RATE;
	}
	public BigDecimal getR51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r51_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR51_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R51_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR51_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r51_LIMIT_PCT_UNIMPAIRED_CAP) {
		R51_LIMIT_PCT_UNIMPAIRED_CAP = r51_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR52_NO_OF_GROUP() {
		return R52_NO_OF_GROUP;
	}
	public void setR52_NO_OF_GROUP(String r52_NO_OF_GROUP) {
		R52_NO_OF_GROUP = r52_NO_OF_GROUP;
	}
	public String getR52_NO_OF_CUSTOMER() {
		return R52_NO_OF_CUSTOMER;
	}
	public void setR52_NO_OF_CUSTOMER(String r52_NO_OF_CUSTOMER) {
		R52_NO_OF_CUSTOMER = r52_NO_OF_CUSTOMER;
	}
	public String getR52_SECTOR_TYPE() {
		return R52_SECTOR_TYPE;
	}
	public void setR52_SECTOR_TYPE(String r52_SECTOR_TYPE) {
		R52_SECTOR_TYPE = r52_SECTOR_TYPE;
	}
	public String getR52_FACILITY_TYPE() {
		return R52_FACILITY_TYPE;
	}
	public void setR52_FACILITY_TYPE(String r52_FACILITY_TYPE) {
		R52_FACILITY_TYPE = r52_FACILITY_TYPE;
	}
	public BigDecimal getR52_ORIGINAL_AMOUNT() {
		return R52_ORIGINAL_AMOUNT;
	}
	public void setR52_ORIGINAL_AMOUNT(BigDecimal r52_ORIGINAL_AMOUNT) {
		R52_ORIGINAL_AMOUNT = r52_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR52_UTILISATION_OUTSTANDING_BAL() {
		return R52_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR52_UTILISATION_OUTSTANDING_BAL(BigDecimal r52_UTILISATION_OUTSTANDING_BAL) {
		R52_UTILISATION_OUTSTANDING_BAL = r52_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR52_EFFECTIVE_DATE() {
		return R52_EFFECTIVE_DATE;
	}
	public void setR52_EFFECTIVE_DATE(Date r52_EFFECTIVE_DATE) {
		R52_EFFECTIVE_DATE = r52_EFFECTIVE_DATE;
	}
	public String getR52_REPAYMENT_PERIOD() {
		return R52_REPAYMENT_PERIOD;
	}
	public void setR52_REPAYMENT_PERIOD(String r52_REPAYMENT_PERIOD) {
		R52_REPAYMENT_PERIOD = r52_REPAYMENT_PERIOD;
	}
	public String getR52_PERFORMANCE_STATUS() {
		return R52_PERFORMANCE_STATUS;
	}
	public void setR52_PERFORMANCE_STATUS(String r52_PERFORMANCE_STATUS) {
		R52_PERFORMANCE_STATUS = r52_PERFORMANCE_STATUS;
	}
	public String getR52_SECURITY() {
		return R52_SECURITY;
	}
	public void setR52_SECURITY(String r52_SECURITY) {
		R52_SECURITY = r52_SECURITY;
	}
	public String getR52_BOARD_APPROVAL() {
		return R52_BOARD_APPROVAL;
	}
	public void setR52_BOARD_APPROVAL(String r52_BOARD_APPROVAL) {
		R52_BOARD_APPROVAL = r52_BOARD_APPROVAL;
	}
	public BigDecimal getR52_INTEREST_RATE() {
		return R52_INTEREST_RATE;
	}
	public void setR52_INTEREST_RATE(BigDecimal r52_INTEREST_RATE) {
		R52_INTEREST_RATE = r52_INTEREST_RATE;
	}
	public BigDecimal getR52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r52_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR52_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R52_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR52_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r52_LIMIT_PCT_UNIMPAIRED_CAP) {
		R52_LIMIT_PCT_UNIMPAIRED_CAP = r52_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR53_NO_OF_GROUP() {
		return R53_NO_OF_GROUP;
	}
	public void setR53_NO_OF_GROUP(String r53_NO_OF_GROUP) {
		R53_NO_OF_GROUP = r53_NO_OF_GROUP;
	}
	public String getR53_NO_OF_CUSTOMER() {
		return R53_NO_OF_CUSTOMER;
	}
	public void setR53_NO_OF_CUSTOMER(String r53_NO_OF_CUSTOMER) {
		R53_NO_OF_CUSTOMER = r53_NO_OF_CUSTOMER;
	}
	public String getR53_SECTOR_TYPE() {
		return R53_SECTOR_TYPE;
	}
	public void setR53_SECTOR_TYPE(String r53_SECTOR_TYPE) {
		R53_SECTOR_TYPE = r53_SECTOR_TYPE;
	}
	public String getR53_FACILITY_TYPE() {
		return R53_FACILITY_TYPE;
	}
	public void setR53_FACILITY_TYPE(String r53_FACILITY_TYPE) {
		R53_FACILITY_TYPE = r53_FACILITY_TYPE;
	}
	public BigDecimal getR53_ORIGINAL_AMOUNT() {
		return R53_ORIGINAL_AMOUNT;
	}
	public void setR53_ORIGINAL_AMOUNT(BigDecimal r53_ORIGINAL_AMOUNT) {
		R53_ORIGINAL_AMOUNT = r53_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR53_UTILISATION_OUTSTANDING_BAL() {
		return R53_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR53_UTILISATION_OUTSTANDING_BAL(BigDecimal r53_UTILISATION_OUTSTANDING_BAL) {
		R53_UTILISATION_OUTSTANDING_BAL = r53_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR53_EFFECTIVE_DATE() {
		return R53_EFFECTIVE_DATE;
	}
	public void setR53_EFFECTIVE_DATE(Date r53_EFFECTIVE_DATE) {
		R53_EFFECTIVE_DATE = r53_EFFECTIVE_DATE;
	}
	public String getR53_REPAYMENT_PERIOD() {
		return R53_REPAYMENT_PERIOD;
	}
	public void setR53_REPAYMENT_PERIOD(String r53_REPAYMENT_PERIOD) {
		R53_REPAYMENT_PERIOD = r53_REPAYMENT_PERIOD;
	}
	public String getR53_PERFORMANCE_STATUS() {
		return R53_PERFORMANCE_STATUS;
	}
	public void setR53_PERFORMANCE_STATUS(String r53_PERFORMANCE_STATUS) {
		R53_PERFORMANCE_STATUS = r53_PERFORMANCE_STATUS;
	}
	public String getR53_SECURITY() {
		return R53_SECURITY;
	}
	public void setR53_SECURITY(String r53_SECURITY) {
		R53_SECURITY = r53_SECURITY;
	}
	public String getR53_BOARD_APPROVAL() {
		return R53_BOARD_APPROVAL;
	}
	public void setR53_BOARD_APPROVAL(String r53_BOARD_APPROVAL) {
		R53_BOARD_APPROVAL = r53_BOARD_APPROVAL;
	}
	public BigDecimal getR53_INTEREST_RATE() {
		return R53_INTEREST_RATE;
	}
	public void setR53_INTEREST_RATE(BigDecimal r53_INTEREST_RATE) {
		R53_INTEREST_RATE = r53_INTEREST_RATE;
	}
	public BigDecimal getR53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r53_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR53_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R53_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR53_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r53_LIMIT_PCT_UNIMPAIRED_CAP) {
		R53_LIMIT_PCT_UNIMPAIRED_CAP = r53_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR54_NO_OF_GROUP() {
		return R54_NO_OF_GROUP;
	}
	public void setR54_NO_OF_GROUP(String r54_NO_OF_GROUP) {
		R54_NO_OF_GROUP = r54_NO_OF_GROUP;
	}
	public String getR54_NO_OF_CUSTOMER() {
		return R54_NO_OF_CUSTOMER;
	}
	public void setR54_NO_OF_CUSTOMER(String r54_NO_OF_CUSTOMER) {
		R54_NO_OF_CUSTOMER = r54_NO_OF_CUSTOMER;
	}
	public String getR54_SECTOR_TYPE() {
		return R54_SECTOR_TYPE;
	}
	public void setR54_SECTOR_TYPE(String r54_SECTOR_TYPE) {
		R54_SECTOR_TYPE = r54_SECTOR_TYPE;
	}
	public String getR54_FACILITY_TYPE() {
		return R54_FACILITY_TYPE;
	}
	public void setR54_FACILITY_TYPE(String r54_FACILITY_TYPE) {
		R54_FACILITY_TYPE = r54_FACILITY_TYPE;
	}
	public BigDecimal getR54_ORIGINAL_AMOUNT() {
		return R54_ORIGINAL_AMOUNT;
	}
	public void setR54_ORIGINAL_AMOUNT(BigDecimal r54_ORIGINAL_AMOUNT) {
		R54_ORIGINAL_AMOUNT = r54_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR54_UTILISATION_OUTSTANDING_BAL() {
		return R54_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR54_UTILISATION_OUTSTANDING_BAL(BigDecimal r54_UTILISATION_OUTSTANDING_BAL) {
		R54_UTILISATION_OUTSTANDING_BAL = r54_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR54_EFFECTIVE_DATE() {
		return R54_EFFECTIVE_DATE;
	}
	public void setR54_EFFECTIVE_DATE(Date r54_EFFECTIVE_DATE) {
		R54_EFFECTIVE_DATE = r54_EFFECTIVE_DATE;
	}
	public String getR54_REPAYMENT_PERIOD() {
		return R54_REPAYMENT_PERIOD;
	}
	public void setR54_REPAYMENT_PERIOD(String r54_REPAYMENT_PERIOD) {
		R54_REPAYMENT_PERIOD = r54_REPAYMENT_PERIOD;
	}
	public String getR54_PERFORMANCE_STATUS() {
		return R54_PERFORMANCE_STATUS;
	}
	public void setR54_PERFORMANCE_STATUS(String r54_PERFORMANCE_STATUS) {
		R54_PERFORMANCE_STATUS = r54_PERFORMANCE_STATUS;
	}
	public String getR54_SECURITY() {
		return R54_SECURITY;
	}
	public void setR54_SECURITY(String r54_SECURITY) {
		R54_SECURITY = r54_SECURITY;
	}
	public String getR54_BOARD_APPROVAL() {
		return R54_BOARD_APPROVAL;
	}
	public void setR54_BOARD_APPROVAL(String r54_BOARD_APPROVAL) {
		R54_BOARD_APPROVAL = r54_BOARD_APPROVAL;
	}
	public BigDecimal getR54_INTEREST_RATE() {
		return R54_INTEREST_RATE;
	}
	public void setR54_INTEREST_RATE(BigDecimal r54_INTEREST_RATE) {
		R54_INTEREST_RATE = r54_INTEREST_RATE;
	}
	public BigDecimal getR54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r54_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR54_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R54_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR54_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r54_LIMIT_PCT_UNIMPAIRED_CAP) {
		R54_LIMIT_PCT_UNIMPAIRED_CAP = r54_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR55_NO_OF_GROUP() {
		return R55_NO_OF_GROUP;
	}
	public void setR55_NO_OF_GROUP(String r55_NO_OF_GROUP) {
		R55_NO_OF_GROUP = r55_NO_OF_GROUP;
	}
	public String getR55_NO_OF_CUSTOMER() {
		return R55_NO_OF_CUSTOMER;
	}
	public void setR55_NO_OF_CUSTOMER(String r55_NO_OF_CUSTOMER) {
		R55_NO_OF_CUSTOMER = r55_NO_OF_CUSTOMER;
	}
	public String getR55_SECTOR_TYPE() {
		return R55_SECTOR_TYPE;
	}
	public void setR55_SECTOR_TYPE(String r55_SECTOR_TYPE) {
		R55_SECTOR_TYPE = r55_SECTOR_TYPE;
	}
	public String getR55_FACILITY_TYPE() {
		return R55_FACILITY_TYPE;
	}
	public void setR55_FACILITY_TYPE(String r55_FACILITY_TYPE) {
		R55_FACILITY_TYPE = r55_FACILITY_TYPE;
	}
	public BigDecimal getR55_ORIGINAL_AMOUNT() {
		return R55_ORIGINAL_AMOUNT;
	}
	public void setR55_ORIGINAL_AMOUNT(BigDecimal r55_ORIGINAL_AMOUNT) {
		R55_ORIGINAL_AMOUNT = r55_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR55_UTILISATION_OUTSTANDING_BAL() {
		return R55_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR55_UTILISATION_OUTSTANDING_BAL(BigDecimal r55_UTILISATION_OUTSTANDING_BAL) {
		R55_UTILISATION_OUTSTANDING_BAL = r55_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR55_EFFECTIVE_DATE() {
		return R55_EFFECTIVE_DATE;
	}
	public void setR55_EFFECTIVE_DATE(Date r55_EFFECTIVE_DATE) {
		R55_EFFECTIVE_DATE = r55_EFFECTIVE_DATE;
	}
	public String getR55_REPAYMENT_PERIOD() {
		return R55_REPAYMENT_PERIOD;
	}
	public void setR55_REPAYMENT_PERIOD(String r55_REPAYMENT_PERIOD) {
		R55_REPAYMENT_PERIOD = r55_REPAYMENT_PERIOD;
	}
	public String getR55_PERFORMANCE_STATUS() {
		return R55_PERFORMANCE_STATUS;
	}
	public void setR55_PERFORMANCE_STATUS(String r55_PERFORMANCE_STATUS) {
		R55_PERFORMANCE_STATUS = r55_PERFORMANCE_STATUS;
	}
	public String getR55_SECURITY() {
		return R55_SECURITY;
	}
	public void setR55_SECURITY(String r55_SECURITY) {
		R55_SECURITY = r55_SECURITY;
	}
	public String getR55_BOARD_APPROVAL() {
		return R55_BOARD_APPROVAL;
	}
	public void setR55_BOARD_APPROVAL(String r55_BOARD_APPROVAL) {
		R55_BOARD_APPROVAL = r55_BOARD_APPROVAL;
	}
	public BigDecimal getR55_INTEREST_RATE() {
		return R55_INTEREST_RATE;
	}
	public void setR55_INTEREST_RATE(BigDecimal r55_INTEREST_RATE) {
		R55_INTEREST_RATE = r55_INTEREST_RATE;
	}
	public BigDecimal getR55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r55_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR55_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R55_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR55_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r55_LIMIT_PCT_UNIMPAIRED_CAP) {
		R55_LIMIT_PCT_UNIMPAIRED_CAP = r55_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR56_NO_OF_GROUP() {
		return R56_NO_OF_GROUP;
	}
	public void setR56_NO_OF_GROUP(String r56_NO_OF_GROUP) {
		R56_NO_OF_GROUP = r56_NO_OF_GROUP;
	}
	public String getR56_NO_OF_CUSTOMER() {
		return R56_NO_OF_CUSTOMER;
	}
	public void setR56_NO_OF_CUSTOMER(String r56_NO_OF_CUSTOMER) {
		R56_NO_OF_CUSTOMER = r56_NO_OF_CUSTOMER;
	}
	public String getR56_SECTOR_TYPE() {
		return R56_SECTOR_TYPE;
	}
	public void setR56_SECTOR_TYPE(String r56_SECTOR_TYPE) {
		R56_SECTOR_TYPE = r56_SECTOR_TYPE;
	}
	public String getR56_FACILITY_TYPE() {
		return R56_FACILITY_TYPE;
	}
	public void setR56_FACILITY_TYPE(String r56_FACILITY_TYPE) {
		R56_FACILITY_TYPE = r56_FACILITY_TYPE;
	}
	public BigDecimal getR56_ORIGINAL_AMOUNT() {
		return R56_ORIGINAL_AMOUNT;
	}
	public void setR56_ORIGINAL_AMOUNT(BigDecimal r56_ORIGINAL_AMOUNT) {
		R56_ORIGINAL_AMOUNT = r56_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR56_UTILISATION_OUTSTANDING_BAL() {
		return R56_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR56_UTILISATION_OUTSTANDING_BAL(BigDecimal r56_UTILISATION_OUTSTANDING_BAL) {
		R56_UTILISATION_OUTSTANDING_BAL = r56_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR56_EFFECTIVE_DATE() {
		return R56_EFFECTIVE_DATE;
	}
	public void setR56_EFFECTIVE_DATE(Date r56_EFFECTIVE_DATE) {
		R56_EFFECTIVE_DATE = r56_EFFECTIVE_DATE;
	}
	public String getR56_REPAYMENT_PERIOD() {
		return R56_REPAYMENT_PERIOD;
	}
	public void setR56_REPAYMENT_PERIOD(String r56_REPAYMENT_PERIOD) {
		R56_REPAYMENT_PERIOD = r56_REPAYMENT_PERIOD;
	}
	public String getR56_PERFORMANCE_STATUS() {
		return R56_PERFORMANCE_STATUS;
	}
	public void setR56_PERFORMANCE_STATUS(String r56_PERFORMANCE_STATUS) {
		R56_PERFORMANCE_STATUS = r56_PERFORMANCE_STATUS;
	}
	public String getR56_SECURITY() {
		return R56_SECURITY;
	}
	public void setR56_SECURITY(String r56_SECURITY) {
		R56_SECURITY = r56_SECURITY;
	}
	public String getR56_BOARD_APPROVAL() {
		return R56_BOARD_APPROVAL;
	}
	public void setR56_BOARD_APPROVAL(String r56_BOARD_APPROVAL) {
		R56_BOARD_APPROVAL = r56_BOARD_APPROVAL;
	}
	public BigDecimal getR56_INTEREST_RATE() {
		return R56_INTEREST_RATE;
	}
	public void setR56_INTEREST_RATE(BigDecimal r56_INTEREST_RATE) {
		R56_INTEREST_RATE = r56_INTEREST_RATE;
	}
	public BigDecimal getR56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r56_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR56_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R56_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR56_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r56_LIMIT_PCT_UNIMPAIRED_CAP) {
		R56_LIMIT_PCT_UNIMPAIRED_CAP = r56_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR57_NO_OF_GROUP() {
		return R57_NO_OF_GROUP;
	}
	public void setR57_NO_OF_GROUP(String r57_NO_OF_GROUP) {
		R57_NO_OF_GROUP = r57_NO_OF_GROUP;
	}
	public String getR57_NO_OF_CUSTOMER() {
		return R57_NO_OF_CUSTOMER;
	}
	public void setR57_NO_OF_CUSTOMER(String r57_NO_OF_CUSTOMER) {
		R57_NO_OF_CUSTOMER = r57_NO_OF_CUSTOMER;
	}
	public String getR57_SECTOR_TYPE() {
		return R57_SECTOR_TYPE;
	}
	public void setR57_SECTOR_TYPE(String r57_SECTOR_TYPE) {
		R57_SECTOR_TYPE = r57_SECTOR_TYPE;
	}
	public String getR57_FACILITY_TYPE() {
		return R57_FACILITY_TYPE;
	}
	public void setR57_FACILITY_TYPE(String r57_FACILITY_TYPE) {
		R57_FACILITY_TYPE = r57_FACILITY_TYPE;
	}
	public BigDecimal getR57_ORIGINAL_AMOUNT() {
		return R57_ORIGINAL_AMOUNT;
	}
	public void setR57_ORIGINAL_AMOUNT(BigDecimal r57_ORIGINAL_AMOUNT) {
		R57_ORIGINAL_AMOUNT = r57_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR57_UTILISATION_OUTSTANDING_BAL() {
		return R57_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR57_UTILISATION_OUTSTANDING_BAL(BigDecimal r57_UTILISATION_OUTSTANDING_BAL) {
		R57_UTILISATION_OUTSTANDING_BAL = r57_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR57_EFFECTIVE_DATE() {
		return R57_EFFECTIVE_DATE;
	}
	public void setR57_EFFECTIVE_DATE(Date r57_EFFECTIVE_DATE) {
		R57_EFFECTIVE_DATE = r57_EFFECTIVE_DATE;
	}
	public String getR57_REPAYMENT_PERIOD() {
		return R57_REPAYMENT_PERIOD;
	}
	public void setR57_REPAYMENT_PERIOD(String r57_REPAYMENT_PERIOD) {
		R57_REPAYMENT_PERIOD = r57_REPAYMENT_PERIOD;
	}
	public String getR57_PERFORMANCE_STATUS() {
		return R57_PERFORMANCE_STATUS;
	}
	public void setR57_PERFORMANCE_STATUS(String r57_PERFORMANCE_STATUS) {
		R57_PERFORMANCE_STATUS = r57_PERFORMANCE_STATUS;
	}
	public String getR57_SECURITY() {
		return R57_SECURITY;
	}
	public void setR57_SECURITY(String r57_SECURITY) {
		R57_SECURITY = r57_SECURITY;
	}
	public String getR57_BOARD_APPROVAL() {
		return R57_BOARD_APPROVAL;
	}
	public void setR57_BOARD_APPROVAL(String r57_BOARD_APPROVAL) {
		R57_BOARD_APPROVAL = r57_BOARD_APPROVAL;
	}
	public BigDecimal getR57_INTEREST_RATE() {
		return R57_INTEREST_RATE;
	}
	public void setR57_INTEREST_RATE(BigDecimal r57_INTEREST_RATE) {
		R57_INTEREST_RATE = r57_INTEREST_RATE;
	}
	public BigDecimal getR57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r57_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR57_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R57_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR57_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r57_LIMIT_PCT_UNIMPAIRED_CAP) {
		R57_LIMIT_PCT_UNIMPAIRED_CAP = r57_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR58_NO_OF_GROUP() {
		return R58_NO_OF_GROUP;
	}
	public void setR58_NO_OF_GROUP(String r58_NO_OF_GROUP) {
		R58_NO_OF_GROUP = r58_NO_OF_GROUP;
	}
	public String getR58_NO_OF_CUSTOMER() {
		return R58_NO_OF_CUSTOMER;
	}
	public void setR58_NO_OF_CUSTOMER(String r58_NO_OF_CUSTOMER) {
		R58_NO_OF_CUSTOMER = r58_NO_OF_CUSTOMER;
	}
	public String getR58_SECTOR_TYPE() {
		return R58_SECTOR_TYPE;
	}
	public void setR58_SECTOR_TYPE(String r58_SECTOR_TYPE) {
		R58_SECTOR_TYPE = r58_SECTOR_TYPE;
	}
	public String getR58_FACILITY_TYPE() {
		return R58_FACILITY_TYPE;
	}
	public void setR58_FACILITY_TYPE(String r58_FACILITY_TYPE) {
		R58_FACILITY_TYPE = r58_FACILITY_TYPE;
	}
	public BigDecimal getR58_ORIGINAL_AMOUNT() {
		return R58_ORIGINAL_AMOUNT;
	}
	public void setR58_ORIGINAL_AMOUNT(BigDecimal r58_ORIGINAL_AMOUNT) {
		R58_ORIGINAL_AMOUNT = r58_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR58_UTILISATION_OUTSTANDING_BAL() {
		return R58_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR58_UTILISATION_OUTSTANDING_BAL(BigDecimal r58_UTILISATION_OUTSTANDING_BAL) {
		R58_UTILISATION_OUTSTANDING_BAL = r58_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR58_EFFECTIVE_DATE() {
		return R58_EFFECTIVE_DATE;
	}
	public void setR58_EFFECTIVE_DATE(Date r58_EFFECTIVE_DATE) {
		R58_EFFECTIVE_DATE = r58_EFFECTIVE_DATE;
	}
	public String getR58_REPAYMENT_PERIOD() {
		return R58_REPAYMENT_PERIOD;
	}
	public void setR58_REPAYMENT_PERIOD(String r58_REPAYMENT_PERIOD) {
		R58_REPAYMENT_PERIOD = r58_REPAYMENT_PERIOD;
	}
	public String getR58_PERFORMANCE_STATUS() {
		return R58_PERFORMANCE_STATUS;
	}
	public void setR58_PERFORMANCE_STATUS(String r58_PERFORMANCE_STATUS) {
		R58_PERFORMANCE_STATUS = r58_PERFORMANCE_STATUS;
	}
	public String getR58_SECURITY() {
		return R58_SECURITY;
	}
	public void setR58_SECURITY(String r58_SECURITY) {
		R58_SECURITY = r58_SECURITY;
	}
	public String getR58_BOARD_APPROVAL() {
		return R58_BOARD_APPROVAL;
	}
	public void setR58_BOARD_APPROVAL(String r58_BOARD_APPROVAL) {
		R58_BOARD_APPROVAL = r58_BOARD_APPROVAL;
	}
	public BigDecimal getR58_INTEREST_RATE() {
		return R58_INTEREST_RATE;
	}
	public void setR58_INTEREST_RATE(BigDecimal r58_INTEREST_RATE) {
		R58_INTEREST_RATE = r58_INTEREST_RATE;
	}
	public BigDecimal getR58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r58_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR58_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R58_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR58_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r58_LIMIT_PCT_UNIMPAIRED_CAP) {
		R58_LIMIT_PCT_UNIMPAIRED_CAP = r58_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR59_NO_OF_GROUP() {
		return R59_NO_OF_GROUP;
	}
	public void setR59_NO_OF_GROUP(String r59_NO_OF_GROUP) {
		R59_NO_OF_GROUP = r59_NO_OF_GROUP;
	}
	public String getR59_NO_OF_CUSTOMER() {
		return R59_NO_OF_CUSTOMER;
	}
	public void setR59_NO_OF_CUSTOMER(String r59_NO_OF_CUSTOMER) {
		R59_NO_OF_CUSTOMER = r59_NO_OF_CUSTOMER;
	}
	public String getR59_SECTOR_TYPE() {
		return R59_SECTOR_TYPE;
	}
	public void setR59_SECTOR_TYPE(String r59_SECTOR_TYPE) {
		R59_SECTOR_TYPE = r59_SECTOR_TYPE;
	}
	public String getR59_FACILITY_TYPE() {
		return R59_FACILITY_TYPE;
	}
	public void setR59_FACILITY_TYPE(String r59_FACILITY_TYPE) {
		R59_FACILITY_TYPE = r59_FACILITY_TYPE;
	}
	public BigDecimal getR59_ORIGINAL_AMOUNT() {
		return R59_ORIGINAL_AMOUNT;
	}
	public void setR59_ORIGINAL_AMOUNT(BigDecimal r59_ORIGINAL_AMOUNT) {
		R59_ORIGINAL_AMOUNT = r59_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR59_UTILISATION_OUTSTANDING_BAL() {
		return R59_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR59_UTILISATION_OUTSTANDING_BAL(BigDecimal r59_UTILISATION_OUTSTANDING_BAL) {
		R59_UTILISATION_OUTSTANDING_BAL = r59_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR59_EFFECTIVE_DATE() {
		return R59_EFFECTIVE_DATE;
	}
	public void setR59_EFFECTIVE_DATE(Date r59_EFFECTIVE_DATE) {
		R59_EFFECTIVE_DATE = r59_EFFECTIVE_DATE;
	}
	public String getR59_REPAYMENT_PERIOD() {
		return R59_REPAYMENT_PERIOD;
	}
	public void setR59_REPAYMENT_PERIOD(String r59_REPAYMENT_PERIOD) {
		R59_REPAYMENT_PERIOD = r59_REPAYMENT_PERIOD;
	}
	public String getR59_PERFORMANCE_STATUS() {
		return R59_PERFORMANCE_STATUS;
	}
	public void setR59_PERFORMANCE_STATUS(String r59_PERFORMANCE_STATUS) {
		R59_PERFORMANCE_STATUS = r59_PERFORMANCE_STATUS;
	}
	public String getR59_SECURITY() {
		return R59_SECURITY;
	}
	public void setR59_SECURITY(String r59_SECURITY) {
		R59_SECURITY = r59_SECURITY;
	}
	public String getR59_BOARD_APPROVAL() {
		return R59_BOARD_APPROVAL;
	}
	public void setR59_BOARD_APPROVAL(String r59_BOARD_APPROVAL) {
		R59_BOARD_APPROVAL = r59_BOARD_APPROVAL;
	}
	public BigDecimal getR59_INTEREST_RATE() {
		return R59_INTEREST_RATE;
	}
	public void setR59_INTEREST_RATE(BigDecimal r59_INTEREST_RATE) {
		R59_INTEREST_RATE = r59_INTEREST_RATE;
	}
	public BigDecimal getR59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r59_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR59_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R59_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR59_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r59_LIMIT_PCT_UNIMPAIRED_CAP) {
		R59_LIMIT_PCT_UNIMPAIRED_CAP = r59_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR60_NO_OF_GROUP() {
		return R60_NO_OF_GROUP;
	}
	public void setR60_NO_OF_GROUP(String r60_NO_OF_GROUP) {
		R60_NO_OF_GROUP = r60_NO_OF_GROUP;
	}
	public String getR60_NO_OF_CUSTOMER() {
		return R60_NO_OF_CUSTOMER;
	}
	public void setR60_NO_OF_CUSTOMER(String r60_NO_OF_CUSTOMER) {
		R60_NO_OF_CUSTOMER = r60_NO_OF_CUSTOMER;
	}
	public String getR60_SECTOR_TYPE() {
		return R60_SECTOR_TYPE;
	}
	public void setR60_SECTOR_TYPE(String r60_SECTOR_TYPE) {
		R60_SECTOR_TYPE = r60_SECTOR_TYPE;
	}
	public String getR60_FACILITY_TYPE() {
		return R60_FACILITY_TYPE;
	}
	public void setR60_FACILITY_TYPE(String r60_FACILITY_TYPE) {
		R60_FACILITY_TYPE = r60_FACILITY_TYPE;
	}
	public BigDecimal getR60_ORIGINAL_AMOUNT() {
		return R60_ORIGINAL_AMOUNT;
	}
	public void setR60_ORIGINAL_AMOUNT(BigDecimal r60_ORIGINAL_AMOUNT) {
		R60_ORIGINAL_AMOUNT = r60_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR60_UTILISATION_OUTSTANDING_BAL() {
		return R60_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR60_UTILISATION_OUTSTANDING_BAL(BigDecimal r60_UTILISATION_OUTSTANDING_BAL) {
		R60_UTILISATION_OUTSTANDING_BAL = r60_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR60_EFFECTIVE_DATE() {
		return R60_EFFECTIVE_DATE;
	}
	public void setR60_EFFECTIVE_DATE(Date r60_EFFECTIVE_DATE) {
		R60_EFFECTIVE_DATE = r60_EFFECTIVE_DATE;
	}
	public String getR60_REPAYMENT_PERIOD() {
		return R60_REPAYMENT_PERIOD;
	}
	public void setR60_REPAYMENT_PERIOD(String r60_REPAYMENT_PERIOD) {
		R60_REPAYMENT_PERIOD = r60_REPAYMENT_PERIOD;
	}
	public String getR60_PERFORMANCE_STATUS() {
		return R60_PERFORMANCE_STATUS;
	}
	public void setR60_PERFORMANCE_STATUS(String r60_PERFORMANCE_STATUS) {
		R60_PERFORMANCE_STATUS = r60_PERFORMANCE_STATUS;
	}
	public String getR60_SECURITY() {
		return R60_SECURITY;
	}
	public void setR60_SECURITY(String r60_SECURITY) {
		R60_SECURITY = r60_SECURITY;
	}
	public String getR60_BOARD_APPROVAL() {
		return R60_BOARD_APPROVAL;
	}
	public void setR60_BOARD_APPROVAL(String r60_BOARD_APPROVAL) {
		R60_BOARD_APPROVAL = r60_BOARD_APPROVAL;
	}
	public BigDecimal getR60_INTEREST_RATE() {
		return R60_INTEREST_RATE;
	}
	public void setR60_INTEREST_RATE(BigDecimal r60_INTEREST_RATE) {
		R60_INTEREST_RATE = r60_INTEREST_RATE;
	}
	public BigDecimal getR60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r60_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR60_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R60_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR60_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r60_LIMIT_PCT_UNIMPAIRED_CAP) {
		R60_LIMIT_PCT_UNIMPAIRED_CAP = r60_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR61_NO_OF_GROUP() {
		return R61_NO_OF_GROUP;
	}
	public void setR61_NO_OF_GROUP(String r61_NO_OF_GROUP) {
		R61_NO_OF_GROUP = r61_NO_OF_GROUP;
	}
	public String getR61_NO_OF_CUSTOMER() {
		return R61_NO_OF_CUSTOMER;
	}
	public void setR61_NO_OF_CUSTOMER(String r61_NO_OF_CUSTOMER) {
		R61_NO_OF_CUSTOMER = r61_NO_OF_CUSTOMER;
	}
	public String getR61_SECTOR_TYPE() {
		return R61_SECTOR_TYPE;
	}
	public void setR61_SECTOR_TYPE(String r61_SECTOR_TYPE) {
		R61_SECTOR_TYPE = r61_SECTOR_TYPE;
	}
	public String getR61_FACILITY_TYPE() {
		return R61_FACILITY_TYPE;
	}
	public void setR61_FACILITY_TYPE(String r61_FACILITY_TYPE) {
		R61_FACILITY_TYPE = r61_FACILITY_TYPE;
	}
	public BigDecimal getR61_ORIGINAL_AMOUNT() {
		return R61_ORIGINAL_AMOUNT;
	}
	public void setR61_ORIGINAL_AMOUNT(BigDecimal r61_ORIGINAL_AMOUNT) {
		R61_ORIGINAL_AMOUNT = r61_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR61_UTILISATION_OUTSTANDING_BAL() {
		return R61_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR61_UTILISATION_OUTSTANDING_BAL(BigDecimal r61_UTILISATION_OUTSTANDING_BAL) {
		R61_UTILISATION_OUTSTANDING_BAL = r61_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR61_EFFECTIVE_DATE() {
		return R61_EFFECTIVE_DATE;
	}
	public void setR61_EFFECTIVE_DATE(Date r61_EFFECTIVE_DATE) {
		R61_EFFECTIVE_DATE = r61_EFFECTIVE_DATE;
	}
	public String getR61_REPAYMENT_PERIOD() {
		return R61_REPAYMENT_PERIOD;
	}
	public void setR61_REPAYMENT_PERIOD(String r61_REPAYMENT_PERIOD) {
		R61_REPAYMENT_PERIOD = r61_REPAYMENT_PERIOD;
	}
	public String getR61_PERFORMANCE_STATUS() {
		return R61_PERFORMANCE_STATUS;
	}
	public void setR61_PERFORMANCE_STATUS(String r61_PERFORMANCE_STATUS) {
		R61_PERFORMANCE_STATUS = r61_PERFORMANCE_STATUS;
	}
	public String getR61_SECURITY() {
		return R61_SECURITY;
	}
	public void setR61_SECURITY(String r61_SECURITY) {
		R61_SECURITY = r61_SECURITY;
	}
	public String getR61_BOARD_APPROVAL() {
		return R61_BOARD_APPROVAL;
	}
	public void setR61_BOARD_APPROVAL(String r61_BOARD_APPROVAL) {
		R61_BOARD_APPROVAL = r61_BOARD_APPROVAL;
	}
	public BigDecimal getR61_INTEREST_RATE() {
		return R61_INTEREST_RATE;
	}
	public void setR61_INTEREST_RATE(BigDecimal r61_INTEREST_RATE) {
		R61_INTEREST_RATE = r61_INTEREST_RATE;
	}
	public BigDecimal getR61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r61_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR61_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R61_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR61_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r61_LIMIT_PCT_UNIMPAIRED_CAP) {
		R61_LIMIT_PCT_UNIMPAIRED_CAP = r61_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR62_NO_OF_GROUP() {
		return R62_NO_OF_GROUP;
	}
	public void setR62_NO_OF_GROUP(String r62_NO_OF_GROUP) {
		R62_NO_OF_GROUP = r62_NO_OF_GROUP;
	}
	public String getR62_NO_OF_CUSTOMER() {
		return R62_NO_OF_CUSTOMER;
	}
	public void setR62_NO_OF_CUSTOMER(String r62_NO_OF_CUSTOMER) {
		R62_NO_OF_CUSTOMER = r62_NO_OF_CUSTOMER;
	}
	public String getR62_SECTOR_TYPE() {
		return R62_SECTOR_TYPE;
	}
	public void setR62_SECTOR_TYPE(String r62_SECTOR_TYPE) {
		R62_SECTOR_TYPE = r62_SECTOR_TYPE;
	}
	public String getR62_FACILITY_TYPE() {
		return R62_FACILITY_TYPE;
	}
	public void setR62_FACILITY_TYPE(String r62_FACILITY_TYPE) {
		R62_FACILITY_TYPE = r62_FACILITY_TYPE;
	}
	public BigDecimal getR62_ORIGINAL_AMOUNT() {
		return R62_ORIGINAL_AMOUNT;
	}
	public void setR62_ORIGINAL_AMOUNT(BigDecimal r62_ORIGINAL_AMOUNT) {
		R62_ORIGINAL_AMOUNT = r62_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR62_UTILISATION_OUTSTANDING_BAL() {
		return R62_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR62_UTILISATION_OUTSTANDING_BAL(BigDecimal r62_UTILISATION_OUTSTANDING_BAL) {
		R62_UTILISATION_OUTSTANDING_BAL = r62_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR62_EFFECTIVE_DATE() {
		return R62_EFFECTIVE_DATE;
	}
	public void setR62_EFFECTIVE_DATE(Date r62_EFFECTIVE_DATE) {
		R62_EFFECTIVE_DATE = r62_EFFECTIVE_DATE;
	}
	public String getR62_REPAYMENT_PERIOD() {
		return R62_REPAYMENT_PERIOD;
	}
	public void setR62_REPAYMENT_PERIOD(String r62_REPAYMENT_PERIOD) {
		R62_REPAYMENT_PERIOD = r62_REPAYMENT_PERIOD;
	}
	public String getR62_PERFORMANCE_STATUS() {
		return R62_PERFORMANCE_STATUS;
	}
	public void setR62_PERFORMANCE_STATUS(String r62_PERFORMANCE_STATUS) {
		R62_PERFORMANCE_STATUS = r62_PERFORMANCE_STATUS;
	}
	public String getR62_SECURITY() {
		return R62_SECURITY;
	}
	public void setR62_SECURITY(String r62_SECURITY) {
		R62_SECURITY = r62_SECURITY;
	}
	public String getR62_BOARD_APPROVAL() {
		return R62_BOARD_APPROVAL;
	}
	public void setR62_BOARD_APPROVAL(String r62_BOARD_APPROVAL) {
		R62_BOARD_APPROVAL = r62_BOARD_APPROVAL;
	}
	public BigDecimal getR62_INTEREST_RATE() {
		return R62_INTEREST_RATE;
	}
	public void setR62_INTEREST_RATE(BigDecimal r62_INTEREST_RATE) {
		R62_INTEREST_RATE = r62_INTEREST_RATE;
	}
	public BigDecimal getR62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r62_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR62_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R62_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR62_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r62_LIMIT_PCT_UNIMPAIRED_CAP) {
		R62_LIMIT_PCT_UNIMPAIRED_CAP = r62_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR63_NO_OF_GROUP() {
		return R63_NO_OF_GROUP;
	}
	public void setR63_NO_OF_GROUP(String r63_NO_OF_GROUP) {
		R63_NO_OF_GROUP = r63_NO_OF_GROUP;
	}
	public String getR63_NO_OF_CUSTOMER() {
		return R63_NO_OF_CUSTOMER;
	}
	public void setR63_NO_OF_CUSTOMER(String r63_NO_OF_CUSTOMER) {
		R63_NO_OF_CUSTOMER = r63_NO_OF_CUSTOMER;
	}
	public String getR63_SECTOR_TYPE() {
		return R63_SECTOR_TYPE;
	}
	public void setR63_SECTOR_TYPE(String r63_SECTOR_TYPE) {
		R63_SECTOR_TYPE = r63_SECTOR_TYPE;
	}
	public String getR63_FACILITY_TYPE() {
		return R63_FACILITY_TYPE;
	}
	public void setR63_FACILITY_TYPE(String r63_FACILITY_TYPE) {
		R63_FACILITY_TYPE = r63_FACILITY_TYPE;
	}
	public BigDecimal getR63_ORIGINAL_AMOUNT() {
		return R63_ORIGINAL_AMOUNT;
	}
	public void setR63_ORIGINAL_AMOUNT(BigDecimal r63_ORIGINAL_AMOUNT) {
		R63_ORIGINAL_AMOUNT = r63_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR63_UTILISATION_OUTSTANDING_BAL() {
		return R63_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR63_UTILISATION_OUTSTANDING_BAL(BigDecimal r63_UTILISATION_OUTSTANDING_BAL) {
		R63_UTILISATION_OUTSTANDING_BAL = r63_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR63_EFFECTIVE_DATE() {
		return R63_EFFECTIVE_DATE;
	}
	public void setR63_EFFECTIVE_DATE(Date r63_EFFECTIVE_DATE) {
		R63_EFFECTIVE_DATE = r63_EFFECTIVE_DATE;
	}
	public String getR63_REPAYMENT_PERIOD() {
		return R63_REPAYMENT_PERIOD;
	}
	public void setR63_REPAYMENT_PERIOD(String r63_REPAYMENT_PERIOD) {
		R63_REPAYMENT_PERIOD = r63_REPAYMENT_PERIOD;
	}
	public String getR63_PERFORMANCE_STATUS() {
		return R63_PERFORMANCE_STATUS;
	}
	public void setR63_PERFORMANCE_STATUS(String r63_PERFORMANCE_STATUS) {
		R63_PERFORMANCE_STATUS = r63_PERFORMANCE_STATUS;
	}
	public String getR63_SECURITY() {
		return R63_SECURITY;
	}
	public void setR63_SECURITY(String r63_SECURITY) {
		R63_SECURITY = r63_SECURITY;
	}
	public String getR63_BOARD_APPROVAL() {
		return R63_BOARD_APPROVAL;
	}
	public void setR63_BOARD_APPROVAL(String r63_BOARD_APPROVAL) {
		R63_BOARD_APPROVAL = r63_BOARD_APPROVAL;
	}
	public BigDecimal getR63_INTEREST_RATE() {
		return R63_INTEREST_RATE;
	}
	public void setR63_INTEREST_RATE(BigDecimal r63_INTEREST_RATE) {
		R63_INTEREST_RATE = r63_INTEREST_RATE;
	}
	public BigDecimal getR63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r63_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR63_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R63_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR63_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r63_LIMIT_PCT_UNIMPAIRED_CAP) {
		R63_LIMIT_PCT_UNIMPAIRED_CAP = r63_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR64_NO_OF_GROUP() {
		return R64_NO_OF_GROUP;
	}
	public void setR64_NO_OF_GROUP(String r64_NO_OF_GROUP) {
		R64_NO_OF_GROUP = r64_NO_OF_GROUP;
	}
	public String getR64_NO_OF_CUSTOMER() {
		return R64_NO_OF_CUSTOMER;
	}
	public void setR64_NO_OF_CUSTOMER(String r64_NO_OF_CUSTOMER) {
		R64_NO_OF_CUSTOMER = r64_NO_OF_CUSTOMER;
	}
	public String getR64_SECTOR_TYPE() {
		return R64_SECTOR_TYPE;
	}
	public void setR64_SECTOR_TYPE(String r64_SECTOR_TYPE) {
		R64_SECTOR_TYPE = r64_SECTOR_TYPE;
	}
	public String getR64_FACILITY_TYPE() {
		return R64_FACILITY_TYPE;
	}
	public void setR64_FACILITY_TYPE(String r64_FACILITY_TYPE) {
		R64_FACILITY_TYPE = r64_FACILITY_TYPE;
	}
	public BigDecimal getR64_ORIGINAL_AMOUNT() {
		return R64_ORIGINAL_AMOUNT;
	}
	public void setR64_ORIGINAL_AMOUNT(BigDecimal r64_ORIGINAL_AMOUNT) {
		R64_ORIGINAL_AMOUNT = r64_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR64_UTILISATION_OUTSTANDING_BAL() {
		return R64_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR64_UTILISATION_OUTSTANDING_BAL(BigDecimal r64_UTILISATION_OUTSTANDING_BAL) {
		R64_UTILISATION_OUTSTANDING_BAL = r64_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR64_EFFECTIVE_DATE() {
		return R64_EFFECTIVE_DATE;
	}
	public void setR64_EFFECTIVE_DATE(Date r64_EFFECTIVE_DATE) {
		R64_EFFECTIVE_DATE = r64_EFFECTIVE_DATE;
	}
	public String getR64_REPAYMENT_PERIOD() {
		return R64_REPAYMENT_PERIOD;
	}
	public void setR64_REPAYMENT_PERIOD(String r64_REPAYMENT_PERIOD) {
		R64_REPAYMENT_PERIOD = r64_REPAYMENT_PERIOD;
	}
	public String getR64_PERFORMANCE_STATUS() {
		return R64_PERFORMANCE_STATUS;
	}
	public void setR64_PERFORMANCE_STATUS(String r64_PERFORMANCE_STATUS) {
		R64_PERFORMANCE_STATUS = r64_PERFORMANCE_STATUS;
	}
	public String getR64_SECURITY() {
		return R64_SECURITY;
	}
	public void setR64_SECURITY(String r64_SECURITY) {
		R64_SECURITY = r64_SECURITY;
	}
	public String getR64_BOARD_APPROVAL() {
		return R64_BOARD_APPROVAL;
	}
	public void setR64_BOARD_APPROVAL(String r64_BOARD_APPROVAL) {
		R64_BOARD_APPROVAL = r64_BOARD_APPROVAL;
	}
	public BigDecimal getR64_INTEREST_RATE() {
		return R64_INTEREST_RATE;
	}
	public void setR64_INTEREST_RATE(BigDecimal r64_INTEREST_RATE) {
		R64_INTEREST_RATE = r64_INTEREST_RATE;
	}
	public BigDecimal getR64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r64_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR64_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R64_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR64_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r64_LIMIT_PCT_UNIMPAIRED_CAP) {
		R64_LIMIT_PCT_UNIMPAIRED_CAP = r64_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR65_NO_OF_GROUP() {
		return R65_NO_OF_GROUP;
	}
	public void setR65_NO_OF_GROUP(String r65_NO_OF_GROUP) {
		R65_NO_OF_GROUP = r65_NO_OF_GROUP;
	}
	public String getR65_NO_OF_CUSTOMER() {
		return R65_NO_OF_CUSTOMER;
	}
	public void setR65_NO_OF_CUSTOMER(String r65_NO_OF_CUSTOMER) {
		R65_NO_OF_CUSTOMER = r65_NO_OF_CUSTOMER;
	}
	public String getR65_SECTOR_TYPE() {
		return R65_SECTOR_TYPE;
	}
	public void setR65_SECTOR_TYPE(String r65_SECTOR_TYPE) {
		R65_SECTOR_TYPE = r65_SECTOR_TYPE;
	}
	public String getR65_FACILITY_TYPE() {
		return R65_FACILITY_TYPE;
	}
	public void setR65_FACILITY_TYPE(String r65_FACILITY_TYPE) {
		R65_FACILITY_TYPE = r65_FACILITY_TYPE;
	}
	public BigDecimal getR65_ORIGINAL_AMOUNT() {
		return R65_ORIGINAL_AMOUNT;
	}
	public void setR65_ORIGINAL_AMOUNT(BigDecimal r65_ORIGINAL_AMOUNT) {
		R65_ORIGINAL_AMOUNT = r65_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR65_UTILISATION_OUTSTANDING_BAL() {
		return R65_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR65_UTILISATION_OUTSTANDING_BAL(BigDecimal r65_UTILISATION_OUTSTANDING_BAL) {
		R65_UTILISATION_OUTSTANDING_BAL = r65_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR65_EFFECTIVE_DATE() {
		return R65_EFFECTIVE_DATE;
	}
	public void setR65_EFFECTIVE_DATE(Date r65_EFFECTIVE_DATE) {
		R65_EFFECTIVE_DATE = r65_EFFECTIVE_DATE;
	}
	public String getR65_REPAYMENT_PERIOD() {
		return R65_REPAYMENT_PERIOD;
	}
	public void setR65_REPAYMENT_PERIOD(String r65_REPAYMENT_PERIOD) {
		R65_REPAYMENT_PERIOD = r65_REPAYMENT_PERIOD;
	}
	public String getR65_PERFORMANCE_STATUS() {
		return R65_PERFORMANCE_STATUS;
	}
	public void setR65_PERFORMANCE_STATUS(String r65_PERFORMANCE_STATUS) {
		R65_PERFORMANCE_STATUS = r65_PERFORMANCE_STATUS;
	}
	public String getR65_SECURITY() {
		return R65_SECURITY;
	}
	public void setR65_SECURITY(String r65_SECURITY) {
		R65_SECURITY = r65_SECURITY;
	}
	public String getR65_BOARD_APPROVAL() {
		return R65_BOARD_APPROVAL;
	}
	public void setR65_BOARD_APPROVAL(String r65_BOARD_APPROVAL) {
		R65_BOARD_APPROVAL = r65_BOARD_APPROVAL;
	}
	public BigDecimal getR65_INTEREST_RATE() {
		return R65_INTEREST_RATE;
	}
	public void setR65_INTEREST_RATE(BigDecimal r65_INTEREST_RATE) {
		R65_INTEREST_RATE = r65_INTEREST_RATE;
	}
	public BigDecimal getR65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r65_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR65_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R65_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR65_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r65_LIMIT_PCT_UNIMPAIRED_CAP) {
		R65_LIMIT_PCT_UNIMPAIRED_CAP = r65_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR66_NO_OF_GROUP() {
		return R66_NO_OF_GROUP;
	}
	public void setR66_NO_OF_GROUP(String r66_NO_OF_GROUP) {
		R66_NO_OF_GROUP = r66_NO_OF_GROUP;
	}
	public String getR66_NO_OF_CUSTOMER() {
		return R66_NO_OF_CUSTOMER;
	}
	public void setR66_NO_OF_CUSTOMER(String r66_NO_OF_CUSTOMER) {
		R66_NO_OF_CUSTOMER = r66_NO_OF_CUSTOMER;
	}
	public String getR66_SECTOR_TYPE() {
		return R66_SECTOR_TYPE;
	}
	public void setR66_SECTOR_TYPE(String r66_SECTOR_TYPE) {
		R66_SECTOR_TYPE = r66_SECTOR_TYPE;
	}
	public String getR66_FACILITY_TYPE() {
		return R66_FACILITY_TYPE;
	}
	public void setR66_FACILITY_TYPE(String r66_FACILITY_TYPE) {
		R66_FACILITY_TYPE = r66_FACILITY_TYPE;
	}
	public BigDecimal getR66_ORIGINAL_AMOUNT() {
		return R66_ORIGINAL_AMOUNT;
	}
	public void setR66_ORIGINAL_AMOUNT(BigDecimal r66_ORIGINAL_AMOUNT) {
		R66_ORIGINAL_AMOUNT = r66_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR66_UTILISATION_OUTSTANDING_BAL() {
		return R66_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR66_UTILISATION_OUTSTANDING_BAL(BigDecimal r66_UTILISATION_OUTSTANDING_BAL) {
		R66_UTILISATION_OUTSTANDING_BAL = r66_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR66_EFFECTIVE_DATE() {
		return R66_EFFECTIVE_DATE;
	}
	public void setR66_EFFECTIVE_DATE(Date r66_EFFECTIVE_DATE) {
		R66_EFFECTIVE_DATE = r66_EFFECTIVE_DATE;
	}
	public String getR66_REPAYMENT_PERIOD() {
		return R66_REPAYMENT_PERIOD;
	}
	public void setR66_REPAYMENT_PERIOD(String r66_REPAYMENT_PERIOD) {
		R66_REPAYMENT_PERIOD = r66_REPAYMENT_PERIOD;
	}
	public String getR66_PERFORMANCE_STATUS() {
		return R66_PERFORMANCE_STATUS;
	}
	public void setR66_PERFORMANCE_STATUS(String r66_PERFORMANCE_STATUS) {
		R66_PERFORMANCE_STATUS = r66_PERFORMANCE_STATUS;
	}
	public String getR66_SECURITY() {
		return R66_SECURITY;
	}
	public void setR66_SECURITY(String r66_SECURITY) {
		R66_SECURITY = r66_SECURITY;
	}
	public String getR66_BOARD_APPROVAL() {
		return R66_BOARD_APPROVAL;
	}
	public void setR66_BOARD_APPROVAL(String r66_BOARD_APPROVAL) {
		R66_BOARD_APPROVAL = r66_BOARD_APPROVAL;
	}
	public BigDecimal getR66_INTEREST_RATE() {
		return R66_INTEREST_RATE;
	}
	public void setR66_INTEREST_RATE(BigDecimal r66_INTEREST_RATE) {
		R66_INTEREST_RATE = r66_INTEREST_RATE;
	}
	public BigDecimal getR66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r66_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR66_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R66_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR66_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r66_LIMIT_PCT_UNIMPAIRED_CAP) {
		R66_LIMIT_PCT_UNIMPAIRED_CAP = r66_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR67_NO_OF_GROUP() {
		return R67_NO_OF_GROUP;
	}
	public void setR67_NO_OF_GROUP(String r67_NO_OF_GROUP) {
		R67_NO_OF_GROUP = r67_NO_OF_GROUP;
	}
	public String getR67_NO_OF_CUSTOMER() {
		return R67_NO_OF_CUSTOMER;
	}
	public void setR67_NO_OF_CUSTOMER(String r67_NO_OF_CUSTOMER) {
		R67_NO_OF_CUSTOMER = r67_NO_OF_CUSTOMER;
	}
	public String getR67_SECTOR_TYPE() {
		return R67_SECTOR_TYPE;
	}
	public void setR67_SECTOR_TYPE(String r67_SECTOR_TYPE) {
		R67_SECTOR_TYPE = r67_SECTOR_TYPE;
	}
	public String getR67_FACILITY_TYPE() {
		return R67_FACILITY_TYPE;
	}
	public void setR67_FACILITY_TYPE(String r67_FACILITY_TYPE) {
		R67_FACILITY_TYPE = r67_FACILITY_TYPE;
	}
	public BigDecimal getR67_ORIGINAL_AMOUNT() {
		return R67_ORIGINAL_AMOUNT;
	}
	public void setR67_ORIGINAL_AMOUNT(BigDecimal r67_ORIGINAL_AMOUNT) {
		R67_ORIGINAL_AMOUNT = r67_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR67_UTILISATION_OUTSTANDING_BAL() {
		return R67_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR67_UTILISATION_OUTSTANDING_BAL(BigDecimal r67_UTILISATION_OUTSTANDING_BAL) {
		R67_UTILISATION_OUTSTANDING_BAL = r67_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR67_EFFECTIVE_DATE() {
		return R67_EFFECTIVE_DATE;
	}
	public void setR67_EFFECTIVE_DATE(Date r67_EFFECTIVE_DATE) {
		R67_EFFECTIVE_DATE = r67_EFFECTIVE_DATE;
	}
	public String getR67_REPAYMENT_PERIOD() {
		return R67_REPAYMENT_PERIOD;
	}
	public void setR67_REPAYMENT_PERIOD(String r67_REPAYMENT_PERIOD) {
		R67_REPAYMENT_PERIOD = r67_REPAYMENT_PERIOD;
	}
	public String getR67_PERFORMANCE_STATUS() {
		return R67_PERFORMANCE_STATUS;
	}
	public void setR67_PERFORMANCE_STATUS(String r67_PERFORMANCE_STATUS) {
		R67_PERFORMANCE_STATUS = r67_PERFORMANCE_STATUS;
	}
	public String getR67_SECURITY() {
		return R67_SECURITY;
	}
	public void setR67_SECURITY(String r67_SECURITY) {
		R67_SECURITY = r67_SECURITY;
	}
	public String getR67_BOARD_APPROVAL() {
		return R67_BOARD_APPROVAL;
	}
	public void setR67_BOARD_APPROVAL(String r67_BOARD_APPROVAL) {
		R67_BOARD_APPROVAL = r67_BOARD_APPROVAL;
	}
	public BigDecimal getR67_INTEREST_RATE() {
		return R67_INTEREST_RATE;
	}
	public void setR67_INTEREST_RATE(BigDecimal r67_INTEREST_RATE) {
		R67_INTEREST_RATE = r67_INTEREST_RATE;
	}
	public BigDecimal getR67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r67_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR67_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R67_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR67_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r67_LIMIT_PCT_UNIMPAIRED_CAP) {
		R67_LIMIT_PCT_UNIMPAIRED_CAP = r67_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR68_NO_OF_GROUP() {
		return R68_NO_OF_GROUP;
	}
	public void setR68_NO_OF_GROUP(String r68_NO_OF_GROUP) {
		R68_NO_OF_GROUP = r68_NO_OF_GROUP;
	}
	public String getR68_NO_OF_CUSTOMER() {
		return R68_NO_OF_CUSTOMER;
	}
	public void setR68_NO_OF_CUSTOMER(String r68_NO_OF_CUSTOMER) {
		R68_NO_OF_CUSTOMER = r68_NO_OF_CUSTOMER;
	}
	public String getR68_SECTOR_TYPE() {
		return R68_SECTOR_TYPE;
	}
	public void setR68_SECTOR_TYPE(String r68_SECTOR_TYPE) {
		R68_SECTOR_TYPE = r68_SECTOR_TYPE;
	}
	public String getR68_FACILITY_TYPE() {
		return R68_FACILITY_TYPE;
	}
	public void setR68_FACILITY_TYPE(String r68_FACILITY_TYPE) {
		R68_FACILITY_TYPE = r68_FACILITY_TYPE;
	}
	public BigDecimal getR68_ORIGINAL_AMOUNT() {
		return R68_ORIGINAL_AMOUNT;
	}
	public void setR68_ORIGINAL_AMOUNT(BigDecimal r68_ORIGINAL_AMOUNT) {
		R68_ORIGINAL_AMOUNT = r68_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR68_UTILISATION_OUTSTANDING_BAL() {
		return R68_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR68_UTILISATION_OUTSTANDING_BAL(BigDecimal r68_UTILISATION_OUTSTANDING_BAL) {
		R68_UTILISATION_OUTSTANDING_BAL = r68_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR68_EFFECTIVE_DATE() {
		return R68_EFFECTIVE_DATE;
	}
	public void setR68_EFFECTIVE_DATE(Date r68_EFFECTIVE_DATE) {
		R68_EFFECTIVE_DATE = r68_EFFECTIVE_DATE;
	}
	public String getR68_REPAYMENT_PERIOD() {
		return R68_REPAYMENT_PERIOD;
	}
	public void setR68_REPAYMENT_PERIOD(String r68_REPAYMENT_PERIOD) {
		R68_REPAYMENT_PERIOD = r68_REPAYMENT_PERIOD;
	}
	public String getR68_PERFORMANCE_STATUS() {
		return R68_PERFORMANCE_STATUS;
	}
	public void setR68_PERFORMANCE_STATUS(String r68_PERFORMANCE_STATUS) {
		R68_PERFORMANCE_STATUS = r68_PERFORMANCE_STATUS;
	}
	public String getR68_SECURITY() {
		return R68_SECURITY;
	}
	public void setR68_SECURITY(String r68_SECURITY) {
		R68_SECURITY = r68_SECURITY;
	}
	public String getR68_BOARD_APPROVAL() {
		return R68_BOARD_APPROVAL;
	}
	public void setR68_BOARD_APPROVAL(String r68_BOARD_APPROVAL) {
		R68_BOARD_APPROVAL = r68_BOARD_APPROVAL;
	}
	public BigDecimal getR68_INTEREST_RATE() {
		return R68_INTEREST_RATE;
	}
	public void setR68_INTEREST_RATE(BigDecimal r68_INTEREST_RATE) {
		R68_INTEREST_RATE = r68_INTEREST_RATE;
	}
	public BigDecimal getR68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r68_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR68_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R68_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR68_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r68_LIMIT_PCT_UNIMPAIRED_CAP) {
		R68_LIMIT_PCT_UNIMPAIRED_CAP = r68_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR69_NO_OF_GROUP() {
		return R69_NO_OF_GROUP;
	}
	public void setR69_NO_OF_GROUP(String r69_NO_OF_GROUP) {
		R69_NO_OF_GROUP = r69_NO_OF_GROUP;
	}
	public String getR69_NO_OF_CUSTOMER() {
		return R69_NO_OF_CUSTOMER;
	}
	public void setR69_NO_OF_CUSTOMER(String r69_NO_OF_CUSTOMER) {
		R69_NO_OF_CUSTOMER = r69_NO_OF_CUSTOMER;
	}
	public String getR69_SECTOR_TYPE() {
		return R69_SECTOR_TYPE;
	}
	public void setR69_SECTOR_TYPE(String r69_SECTOR_TYPE) {
		R69_SECTOR_TYPE = r69_SECTOR_TYPE;
	}
	public String getR69_FACILITY_TYPE() {
		return R69_FACILITY_TYPE;
	}
	public void setR69_FACILITY_TYPE(String r69_FACILITY_TYPE) {
		R69_FACILITY_TYPE = r69_FACILITY_TYPE;
	}
	public BigDecimal getR69_ORIGINAL_AMOUNT() {
		return R69_ORIGINAL_AMOUNT;
	}
	public void setR69_ORIGINAL_AMOUNT(BigDecimal r69_ORIGINAL_AMOUNT) {
		R69_ORIGINAL_AMOUNT = r69_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR69_UTILISATION_OUTSTANDING_BAL() {
		return R69_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR69_UTILISATION_OUTSTANDING_BAL(BigDecimal r69_UTILISATION_OUTSTANDING_BAL) {
		R69_UTILISATION_OUTSTANDING_BAL = r69_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR69_EFFECTIVE_DATE() {
		return R69_EFFECTIVE_DATE;
	}
	public void setR69_EFFECTIVE_DATE(Date r69_EFFECTIVE_DATE) {
		R69_EFFECTIVE_DATE = r69_EFFECTIVE_DATE;
	}
	public String getR69_REPAYMENT_PERIOD() {
		return R69_REPAYMENT_PERIOD;
	}
	public void setR69_REPAYMENT_PERIOD(String r69_REPAYMENT_PERIOD) {
		R69_REPAYMENT_PERIOD = r69_REPAYMENT_PERIOD;
	}
	public String getR69_PERFORMANCE_STATUS() {
		return R69_PERFORMANCE_STATUS;
	}
	public void setR69_PERFORMANCE_STATUS(String r69_PERFORMANCE_STATUS) {
		R69_PERFORMANCE_STATUS = r69_PERFORMANCE_STATUS;
	}
	public String getR69_SECURITY() {
		return R69_SECURITY;
	}
	public void setR69_SECURITY(String r69_SECURITY) {
		R69_SECURITY = r69_SECURITY;
	}
	public String getR69_BOARD_APPROVAL() {
		return R69_BOARD_APPROVAL;
	}
	public void setR69_BOARD_APPROVAL(String r69_BOARD_APPROVAL) {
		R69_BOARD_APPROVAL = r69_BOARD_APPROVAL;
	}
	public BigDecimal getR69_INTEREST_RATE() {
		return R69_INTEREST_RATE;
	}
	public void setR69_INTEREST_RATE(BigDecimal r69_INTEREST_RATE) {
		R69_INTEREST_RATE = r69_INTEREST_RATE;
	}
	public BigDecimal getR69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r69_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR69_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R69_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR69_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r69_LIMIT_PCT_UNIMPAIRED_CAP) {
		R69_LIMIT_PCT_UNIMPAIRED_CAP = r69_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public String getR70_NO_OF_GROUP() {
		return R70_NO_OF_GROUP;
	}
	public void setR70_NO_OF_GROUP(String r70_NO_OF_GROUP) {
		R70_NO_OF_GROUP = r70_NO_OF_GROUP;
	}
	public String getR70_NO_OF_CUSTOMER() {
		return R70_NO_OF_CUSTOMER;
	}
	public void setR70_NO_OF_CUSTOMER(String r70_NO_OF_CUSTOMER) {
		R70_NO_OF_CUSTOMER = r70_NO_OF_CUSTOMER;
	}
	public String getR70_SECTOR_TYPE() {
		return R70_SECTOR_TYPE;
	}
	public void setR70_SECTOR_TYPE(String r70_SECTOR_TYPE) {
		R70_SECTOR_TYPE = r70_SECTOR_TYPE;
	}
	public String getR70_FACILITY_TYPE() {
		return R70_FACILITY_TYPE;
	}
	public void setR70_FACILITY_TYPE(String r70_FACILITY_TYPE) {
		R70_FACILITY_TYPE = r70_FACILITY_TYPE;
	}
	public BigDecimal getR70_ORIGINAL_AMOUNT() {
		return R70_ORIGINAL_AMOUNT;
	}
	public void setR70_ORIGINAL_AMOUNT(BigDecimal r70_ORIGINAL_AMOUNT) {
		R70_ORIGINAL_AMOUNT = r70_ORIGINAL_AMOUNT;
	}
	public BigDecimal getR70_UTILISATION_OUTSTANDING_BAL() {
		return R70_UTILISATION_OUTSTANDING_BAL;
	}
	public void setR70_UTILISATION_OUTSTANDING_BAL(BigDecimal r70_UTILISATION_OUTSTANDING_BAL) {
		R70_UTILISATION_OUTSTANDING_BAL = r70_UTILISATION_OUTSTANDING_BAL;
	}
	public Date getR70_EFFECTIVE_DATE() {
		return R70_EFFECTIVE_DATE;
	}
	public void setR70_EFFECTIVE_DATE(Date r70_EFFECTIVE_DATE) {
		R70_EFFECTIVE_DATE = r70_EFFECTIVE_DATE;
	}
	public String getR70_REPAYMENT_PERIOD() {
		return R70_REPAYMENT_PERIOD;
	}
	public void setR70_REPAYMENT_PERIOD(String r70_REPAYMENT_PERIOD) {
		R70_REPAYMENT_PERIOD = r70_REPAYMENT_PERIOD;
	}
	public String getR70_PERFORMANCE_STATUS() {
		return R70_PERFORMANCE_STATUS;
	}
	public void setR70_PERFORMANCE_STATUS(String r70_PERFORMANCE_STATUS) {
		R70_PERFORMANCE_STATUS = r70_PERFORMANCE_STATUS;
	}
	public String getR70_SECURITY() {
		return R70_SECURITY;
	}
	public void setR70_SECURITY(String r70_SECURITY) {
		R70_SECURITY = r70_SECURITY;
	}
	public String getR70_BOARD_APPROVAL() {
		return R70_BOARD_APPROVAL;
	}
	public void setR70_BOARD_APPROVAL(String r70_BOARD_APPROVAL) {
		R70_BOARD_APPROVAL = r70_BOARD_APPROVAL;
	}
	public BigDecimal getR70_INTEREST_RATE() {
		return R70_INTEREST_RATE;
	}
	public void setR70_INTEREST_RATE(BigDecimal r70_INTEREST_RATE) {
		R70_INTEREST_RATE = r70_INTEREST_RATE;
	}
	public BigDecimal getR70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP() {
		return R70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public void setR70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP(BigDecimal r70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP) {
		R70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP = r70_OUTSTANDING_BAL_PCT_UNIMPAIRED_CAP;
	}
	public BigDecimal getR70_LIMIT_PCT_UNIMPAIRED_CAP() {
		return R70_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public void setR70_LIMIT_PCT_UNIMPAIRED_CAP(BigDecimal r70_LIMIT_PCT_UNIMPAIRED_CAP) {
		R70_LIMIT_PCT_UNIMPAIRED_CAP = r70_LIMIT_PCT_UNIMPAIRED_CAP;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public BigDecimal getReport_version() {
		return report_version;
	}
	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public M_LARADV_Resub_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}
}
