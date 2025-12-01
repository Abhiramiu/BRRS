package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_PD_ARCHIVAL_TABLE_SUMMARY1")
public class M_PD_Archival_Summary_Entity1 {

	private	String	R8_PRODUCT;
	private	BigDecimal	R8_30D_90D_PASTDUE;
	private	BigDecimal	R8_NON_PERFORM_LOANS;
	private	BigDecimal	R8_NON_ACCRUALS1;
	private	BigDecimal	R8_SPECIFIC_PROV1;
	private	BigDecimal	R8_NO_OF_ACC1;
	private	BigDecimal	R8_90D_180D_PASTDUE;
	private	BigDecimal	R8_NON_ACCRUALS2;
	private	BigDecimal	R8_SPECIFIC_PROV2;
	private	BigDecimal	R8_NO_OF_ACC2;
	private	BigDecimal	R8_180D_ABOVE_PASTDUE;
	private	BigDecimal	R8_NON_ACCRUALS3;
	private	BigDecimal	R8_SPECIFIC_PROV3;
	private	BigDecimal	R8_NO_OF_ACC3;
	private	BigDecimal	R8_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R8_TOTAL_DUE_LOANS;
	private	BigDecimal	R8_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R8_VALUE_OF_COLLATERAL;
	private	BigDecimal	R8_TOTAL_VALUE_NPL;
	private	BigDecimal	R8_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R8_SPECIFIC_PROV_NPL;
	private	String	R9_PRODUCT;
	private	BigDecimal	R9_30D_90D_PASTDUE;
	private	BigDecimal	R9_NON_PERFORM_LOANS;
	private	BigDecimal	R9_NON_ACCRUALS1;
	private	BigDecimal	R9_SPECIFIC_PROV1;
	private	BigDecimal	R9_NO_OF_ACC1;
	private	BigDecimal	R9_90D_180D_PASTDUE;
	private	BigDecimal	R9_NON_ACCRUALS2;
	private	BigDecimal	R9_SPECIFIC_PROV2;
	private	BigDecimal	R9_NO_OF_ACC2;
	private	BigDecimal	R9_180D_ABOVE_PASTDUE;
	private	BigDecimal	R9_NON_ACCRUALS3;
	private	BigDecimal	R9_SPECIFIC_PROV3;
	private	BigDecimal	R9_NO_OF_ACC3;
	private	BigDecimal	R9_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R9_TOTAL_DUE_LOANS;
	private	BigDecimal	R9_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R9_VALUE_OF_COLLATERAL;
	private	BigDecimal	R9_TOTAL_VALUE_NPL;
	private	BigDecimal	R9_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R9_SPECIFIC_PROV_NPL;
	private	String	R10_PRODUCT;
	private	BigDecimal	R10_30D_90D_PASTDUE;
	private	BigDecimal	R10_NON_PERFORM_LOANS;
	private	BigDecimal	R10_NON_ACCRUALS1;
	private	BigDecimal	R10_SPECIFIC_PROV1;
	private	BigDecimal	R10_NO_OF_ACC1;
	private	BigDecimal	R10_90D_180D_PASTDUE;
	private	BigDecimal	R10_NON_ACCRUALS2;
	private	BigDecimal	R10_SPECIFIC_PROV2;
	private	BigDecimal	R10_NO_OF_ACC2;
	private	BigDecimal	R10_180D_ABOVE_PASTDUE;
	private	BigDecimal	R10_NON_ACCRUALS3;
	private	BigDecimal	R10_SPECIFIC_PROV3;
	private	BigDecimal	R10_NO_OF_ACC3;
	private	BigDecimal	R10_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R10_TOTAL_DUE_LOANS;
	private	BigDecimal	R10_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R10_VALUE_OF_COLLATERAL;
	private	BigDecimal	R10_TOTAL_VALUE_NPL;
	private	BigDecimal	R10_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R10_SPECIFIC_PROV_NPL;
	private	String	R11_PRODUCT;
	private	BigDecimal	R11_30D_90D_PASTDUE;
	private	BigDecimal	R11_NON_PERFORM_LOANS;
	private	BigDecimal	R11_NON_ACCRUALS1;
	private	BigDecimal	R11_SPECIFIC_PROV1;
	private	BigDecimal	R11_NO_OF_ACC1;
	private	BigDecimal	R11_90D_180D_PASTDUE;
	private	BigDecimal	R11_NON_ACCRUALS2;
	private	BigDecimal	R11_SPECIFIC_PROV2;
	private	BigDecimal	R11_NO_OF_ACC2;
	private	BigDecimal	R11_180D_ABOVE_PASTDUE;
	private	BigDecimal	R11_NON_ACCRUALS3;
	private	BigDecimal	R11_SPECIFIC_PROV3;
	private	BigDecimal	R11_NO_OF_ACC3;
	private	BigDecimal	R11_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R11_TOTAL_DUE_LOANS;
	private	BigDecimal	R11_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R11_VALUE_OF_COLLATERAL;
	private	BigDecimal	R11_TOTAL_VALUE_NPL;
	private	BigDecimal	R11_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R11_SPECIFIC_PROV_NPL;
	private	String	R12_PRODUCT;
	private	BigDecimal	R12_30D_90D_PASTDUE;
	private	BigDecimal	R12_NON_PERFORM_LOANS;
	private	BigDecimal	R12_NON_ACCRUALS1;
	private	BigDecimal	R12_SPECIFIC_PROV1;
	private	BigDecimal	R12_NO_OF_ACC1;
	private	BigDecimal	R12_90D_180D_PASTDUE;
	private	BigDecimal	R12_NON_ACCRUALS2;
	private	BigDecimal	R12_SPECIFIC_PROV2;
	private	BigDecimal	R12_NO_OF_ACC2;
	private	BigDecimal	R12_180D_ABOVE_PASTDUE;
	private	BigDecimal	R12_NON_ACCRUALS3;
	private	BigDecimal	R12_SPECIFIC_PROV3;
	private	BigDecimal	R12_NO_OF_ACC3;
	private	BigDecimal	R12_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R12_TOTAL_DUE_LOANS;
	private	BigDecimal	R12_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R12_VALUE_OF_COLLATERAL;
	private	BigDecimal	R12_TOTAL_VALUE_NPL;
	private	BigDecimal	R12_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R12_SPECIFIC_PROV_NPL;
	private	String	R13_PRODUCT;
	private	BigDecimal	R13_30D_90D_PASTDUE;
	private	BigDecimal	R13_NON_PERFORM_LOANS;
	private	BigDecimal	R13_NON_ACCRUALS1;
	private	BigDecimal	R13_SPECIFIC_PROV1;
	private	BigDecimal	R13_NO_OF_ACC1;
	private	BigDecimal	R13_90D_180D_PASTDUE;
	private	BigDecimal	R13_NON_ACCRUALS2;
	private	BigDecimal	R13_SPECIFIC_PROV2;
	private	BigDecimal	R13_NO_OF_ACC2;
	private	BigDecimal	R13_180D_ABOVE_PASTDUE;
	private	BigDecimal	R13_NON_ACCRUALS3;
	private	BigDecimal	R13_SPECIFIC_PROV3;
	private	BigDecimal	R13_NO_OF_ACC3;
	private	BigDecimal	R13_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R13_TOTAL_DUE_LOANS;
	private	BigDecimal	R13_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R13_VALUE_OF_COLLATERAL;
	private	BigDecimal	R13_TOTAL_VALUE_NPL;
	private	BigDecimal	R13_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R13_SPECIFIC_PROV_NPL;
	private	String	R14_PRODUCT;
	private	BigDecimal	R14_30D_90D_PASTDUE;
	private	BigDecimal	R14_NON_PERFORM_LOANS;
	private	BigDecimal	R14_NON_ACCRUALS1;
	private	BigDecimal	R14_SPECIFIC_PROV1;
	private	BigDecimal	R14_NO_OF_ACC1;
	private	BigDecimal	R14_90D_180D_PASTDUE;
	private	BigDecimal	R14_NON_ACCRUALS2;
	private	BigDecimal	R14_SPECIFIC_PROV2;
	private	BigDecimal	R14_NO_OF_ACC2;
	private	BigDecimal	R14_180D_ABOVE_PASTDUE;
	private	BigDecimal	R14_NON_ACCRUALS3;
	private	BigDecimal	R14_SPECIFIC_PROV3;
	private	BigDecimal	R14_NO_OF_ACC3;
	private	BigDecimal	R14_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R14_TOTAL_DUE_LOANS;
	private	BigDecimal	R14_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R14_VALUE_OF_COLLATERAL;
	private	BigDecimal	R14_TOTAL_VALUE_NPL;
	private	BigDecimal	R14_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R14_SPECIFIC_PROV_NPL;
	private	String	R15_PRODUCT;
	private	BigDecimal	R15_30D_90D_PASTDUE;
	private	BigDecimal	R15_NON_PERFORM_LOANS;
	private	BigDecimal	R15_NON_ACCRUALS1;
	private	BigDecimal	R15_SPECIFIC_PROV1;
	private	BigDecimal	R15_NO_OF_ACC1;
	private	BigDecimal	R15_90D_180D_PASTDUE;
	private	BigDecimal	R15_NON_ACCRUALS2;
	private	BigDecimal	R15_SPECIFIC_PROV2;
	private	BigDecimal	R15_NO_OF_ACC2;
	private	BigDecimal	R15_180D_ABOVE_PASTDUE;
	private	BigDecimal	R15_NON_ACCRUALS3;
	private	BigDecimal	R15_SPECIFIC_PROV3;
	private	BigDecimal	R15_NO_OF_ACC3;
	private	BigDecimal	R15_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R15_TOTAL_DUE_LOANS;
	private	BigDecimal	R15_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R15_VALUE_OF_COLLATERAL;
	private	BigDecimal	R15_TOTAL_VALUE_NPL;
	private	BigDecimal	R15_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R15_SPECIFIC_PROV_NPL;
	private	String	R16_PRODUCT;
	private	BigDecimal	R16_30D_90D_PASTDUE;
	private	BigDecimal	R16_NON_PERFORM_LOANS;
	private	BigDecimal	R16_NON_ACCRUALS1;
	private	BigDecimal	R16_SPECIFIC_PROV1;
	private	BigDecimal	R16_NO_OF_ACC1;
	private	BigDecimal	R16_90D_180D_PASTDUE;
	private	BigDecimal	R16_NON_ACCRUALS2;
	private	BigDecimal	R16_SPECIFIC_PROV2;
	private	BigDecimal	R16_NO_OF_ACC2;
	private	BigDecimal	R16_180D_ABOVE_PASTDUE;
	private	BigDecimal	R16_NON_ACCRUALS3;
	private	BigDecimal	R16_SPECIFIC_PROV3;
	private	BigDecimal	R16_NO_OF_ACC3;
	private	BigDecimal	R16_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R16_TOTAL_DUE_LOANS;
	private	BigDecimal	R16_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R16_VALUE_OF_COLLATERAL;
	private	BigDecimal	R16_TOTAL_VALUE_NPL;
	private	BigDecimal	R16_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R16_SPECIFIC_PROV_NPL;
	private	String	R17_PRODUCT;
	private	BigDecimal	R17_30D_90D_PASTDUE;
	private	BigDecimal	R17_NON_PERFORM_LOANS;
	private	BigDecimal	R17_NON_ACCRUALS1;
	private	BigDecimal	R17_SPECIFIC_PROV1;
	private	BigDecimal	R17_NO_OF_ACC1;
	private	BigDecimal	R17_90D_180D_PASTDUE;
	private	BigDecimal	R17_NON_ACCRUALS2;
	private	BigDecimal	R17_SPECIFIC_PROV2;
	private	BigDecimal	R17_NO_OF_ACC2;
	private	BigDecimal	R17_180D_ABOVE_PASTDUE;
	private	BigDecimal	R17_NON_ACCRUALS3;
	private	BigDecimal	R17_SPECIFIC_PROV3;
	private	BigDecimal	R17_NO_OF_ACC3;
	private	BigDecimal	R17_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R17_TOTAL_DUE_LOANS;
	private	BigDecimal	R17_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R17_VALUE_OF_COLLATERAL;
	private	BigDecimal	R17_TOTAL_VALUE_NPL;
	private	BigDecimal	R17_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R17_SPECIFIC_PROV_NPL;
	private	String	R18_PRODUCT;
	private	BigDecimal	R18_30D_90D_PASTDUE;
	private	BigDecimal	R18_NON_PERFORM_LOANS;
	private	BigDecimal	R18_NON_ACCRUALS1;
	private	BigDecimal	R18_SPECIFIC_PROV1;
	private	BigDecimal	R18_NO_OF_ACC1;
	private	BigDecimal	R18_90D_180D_PASTDUE;
	private	BigDecimal	R18_NON_ACCRUALS2;
	private	BigDecimal	R18_SPECIFIC_PROV2;
	private	BigDecimal	R18_NO_OF_ACC2;
	private	BigDecimal	R18_180D_ABOVE_PASTDUE;
	private	BigDecimal	R18_NON_ACCRUALS3;
	private	BigDecimal	R18_SPECIFIC_PROV3;
	private	BigDecimal	R18_NO_OF_ACC3;
	private	BigDecimal	R18_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R18_TOTAL_DUE_LOANS;
	private	BigDecimal	R18_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R18_VALUE_OF_COLLATERAL;
	private	BigDecimal	R18_TOTAL_VALUE_NPL;
	private	BigDecimal	R18_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R18_SPECIFIC_PROV_NPL;
	private	String	R19_PRODUCT;
	private	BigDecimal	R19_30D_90D_PASTDUE;
	private	BigDecimal	R19_NON_PERFORM_LOANS;
	private	BigDecimal	R19_NON_ACCRUALS1;
	private	BigDecimal	R19_SPECIFIC_PROV1;
	private	BigDecimal	R19_NO_OF_ACC1;
	private	BigDecimal	R19_90D_180D_PASTDUE;
	private	BigDecimal	R19_NON_ACCRUALS2;
	private	BigDecimal	R19_SPECIFIC_PROV2;
	private	BigDecimal	R19_NO_OF_ACC2;
	private	BigDecimal	R19_180D_ABOVE_PASTDUE;
	private	BigDecimal	R19_NON_ACCRUALS3;
	private	BigDecimal	R19_SPECIFIC_PROV3;
	private	BigDecimal	R19_NO_OF_ACC3;
	private	BigDecimal	R19_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R19_TOTAL_DUE_LOANS;
	private	BigDecimal	R19_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R19_VALUE_OF_COLLATERAL;
	private	BigDecimal	R19_TOTAL_VALUE_NPL;
	private	BigDecimal	R19_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R19_SPECIFIC_PROV_NPL;
	private	String	R20_PRODUCT;
	private	BigDecimal	R20_30D_90D_PASTDUE;
	private	BigDecimal	R20_NON_PERFORM_LOANS;
	private	BigDecimal	R20_NON_ACCRUALS1;
	private	BigDecimal	R20_SPECIFIC_PROV1;
	private	BigDecimal	R20_NO_OF_ACC1;
	private	BigDecimal	R20_90D_180D_PASTDUE;
	private	BigDecimal	R20_NON_ACCRUALS2;
	private	BigDecimal	R20_SPECIFIC_PROV2;
	private	BigDecimal	R20_NO_OF_ACC2;
	private	BigDecimal	R20_180D_ABOVE_PASTDUE;
	private	BigDecimal	R20_NON_ACCRUALS3;
	private	BigDecimal	R20_SPECIFIC_PROV3;
	private	BigDecimal	R20_NO_OF_ACC3;
	private	BigDecimal	R20_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R20_TOTAL_DUE_LOANS;
	private	BigDecimal	R20_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R20_VALUE_OF_COLLATERAL;
	private	BigDecimal	R20_TOTAL_VALUE_NPL;
	private	BigDecimal	R20_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R20_SPECIFIC_PROV_NPL;
	private	String	R21_PRODUCT;
	private	BigDecimal	R21_30D_90D_PASTDUE;
	private	BigDecimal	R21_NON_PERFORM_LOANS;
	private	BigDecimal	R21_NON_ACCRUALS1;
	private	BigDecimal	R21_SPECIFIC_PROV1;
	private	BigDecimal	R21_NO_OF_ACC1;
	private	BigDecimal	R21_90D_180D_PASTDUE;
	private	BigDecimal	R21_NON_ACCRUALS2;
	private	BigDecimal	R21_SPECIFIC_PROV2;
	private	BigDecimal	R21_NO_OF_ACC2;
	private	BigDecimal	R21_180D_ABOVE_PASTDUE;
	private	BigDecimal	R21_NON_ACCRUALS3;
	private	BigDecimal	R21_SPECIFIC_PROV3;
	private	BigDecimal	R21_NO_OF_ACC3;
	private	BigDecimal	R21_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R21_TOTAL_DUE_LOANS;
	private	BigDecimal	R21_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R21_VALUE_OF_COLLATERAL;
	private	BigDecimal	R21_TOTAL_VALUE_NPL;
	private	BigDecimal	R21_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R21_SPECIFIC_PROV_NPL;
	private	String	R22_PRODUCT;
	private	BigDecimal	R22_30D_90D_PASTDUE;
	private	BigDecimal	R22_NON_PERFORM_LOANS;
	private	BigDecimal	R22_NON_ACCRUALS1;
	private	BigDecimal	R22_SPECIFIC_PROV1;
	private	BigDecimal	R22_NO_OF_ACC1;
	private	BigDecimal	R22_90D_180D_PASTDUE;
	private	BigDecimal	R22_NON_ACCRUALS2;
	private	BigDecimal	R22_SPECIFIC_PROV2;
	private	BigDecimal	R22_NO_OF_ACC2;
	private	BigDecimal	R22_180D_ABOVE_PASTDUE;
	private	BigDecimal	R22_NON_ACCRUALS3;
	private	BigDecimal	R22_SPECIFIC_PROV3;
	private	BigDecimal	R22_NO_OF_ACC3;
	private	BigDecimal	R22_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R22_TOTAL_DUE_LOANS;
	private	BigDecimal	R22_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R22_VALUE_OF_COLLATERAL;
	private	BigDecimal	R22_TOTAL_VALUE_NPL;
	private	BigDecimal	R22_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R22_SPECIFIC_PROV_NPL;
	private	String	R23_PRODUCT;
	private	BigDecimal	R23_30D_90D_PASTDUE;
	private	BigDecimal	R23_NON_PERFORM_LOANS;
	private	BigDecimal	R23_NON_ACCRUALS1;
	private	BigDecimal	R23_SPECIFIC_PROV1;
	private	BigDecimal	R23_NO_OF_ACC1;
	private	BigDecimal	R23_90D_180D_PASTDUE;
	private	BigDecimal	R23_NON_ACCRUALS2;
	private	BigDecimal	R23_SPECIFIC_PROV2;
	private	BigDecimal	R23_NO_OF_ACC2;
	private	BigDecimal	R23_180D_ABOVE_PASTDUE;
	private	BigDecimal	R23_NON_ACCRUALS3;
	private	BigDecimal	R23_SPECIFIC_PROV3;
	private	BigDecimal	R23_NO_OF_ACC3;
	private	BigDecimal	R23_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R23_TOTAL_DUE_LOANS;
	private	BigDecimal	R23_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R23_VALUE_OF_COLLATERAL;
	private	BigDecimal	R23_TOTAL_VALUE_NPL;
	private	BigDecimal	R23_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R23_SPECIFIC_PROV_NPL;
	private	String	R24_PRODUCT;
	private	BigDecimal	R24_30D_90D_PASTDUE;
	private	BigDecimal	R24_NON_PERFORM_LOANS;
	private	BigDecimal	R24_NON_ACCRUALS1;
	private	BigDecimal	R24_SPECIFIC_PROV1;
	private	BigDecimal	R24_NO_OF_ACC1;
	private	BigDecimal	R24_90D_180D_PASTDUE;
	private	BigDecimal	R24_NON_ACCRUALS2;
	private	BigDecimal	R24_SPECIFIC_PROV2;
	private	BigDecimal	R24_NO_OF_ACC2;
	private	BigDecimal	R24_180D_ABOVE_PASTDUE;
	private	BigDecimal	R24_NON_ACCRUALS3;
	private	BigDecimal	R24_SPECIFIC_PROV3;
	private	BigDecimal	R24_NO_OF_ACC3;
	private	BigDecimal	R24_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R24_TOTAL_DUE_LOANS;
	private	BigDecimal	R24_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R24_VALUE_OF_COLLATERAL;
	private	BigDecimal	R24_TOTAL_VALUE_NPL;
	private	BigDecimal	R24_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R24_SPECIFIC_PROV_NPL;
	private	String	R25_PRODUCT;
	private	BigDecimal	R25_30D_90D_PASTDUE;
	private	BigDecimal	R25_NON_PERFORM_LOANS;
	private	BigDecimal	R25_NON_ACCRUALS1;
	private	BigDecimal	R25_SPECIFIC_PROV1;
	private	BigDecimal	R25_NO_OF_ACC1;
	private	BigDecimal	R25_90D_180D_PASTDUE;
	private	BigDecimal	R25_NON_ACCRUALS2;
	private	BigDecimal	R25_SPECIFIC_PROV2;
	private	BigDecimal	R25_NO_OF_ACC2;
	private	BigDecimal	R25_180D_ABOVE_PASTDUE;
	private	BigDecimal	R25_NON_ACCRUALS3;
	private	BigDecimal	R25_SPECIFIC_PROV3;
	private	BigDecimal	R25_NO_OF_ACC3;
	private	BigDecimal	R25_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R25_TOTAL_DUE_LOANS;
	private	BigDecimal	R25_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R25_VALUE_OF_COLLATERAL;
	private	BigDecimal	R25_TOTAL_VALUE_NPL;
	private	BigDecimal	R25_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R25_SPECIFIC_PROV_NPL;
	private	String	R26_PRODUCT;
	private	BigDecimal	R26_30D_90D_PASTDUE;
	private	BigDecimal	R26_NON_PERFORM_LOANS;
	private	BigDecimal	R26_NON_ACCRUALS1;
	private	BigDecimal	R26_SPECIFIC_PROV1;
	private	BigDecimal	R26_NO_OF_ACC1;
	private	BigDecimal	R26_90D_180D_PASTDUE;
	private	BigDecimal	R26_NON_ACCRUALS2;
	private	BigDecimal	R26_SPECIFIC_PROV2;
	private	BigDecimal	R26_NO_OF_ACC2;
	private	BigDecimal	R26_180D_ABOVE_PASTDUE;
	private	BigDecimal	R26_NON_ACCRUALS3;
	private	BigDecimal	R26_SPECIFIC_PROV3;
	private	BigDecimal	R26_NO_OF_ACC3;
	private	BigDecimal	R26_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R26_TOTAL_DUE_LOANS;
	private	BigDecimal	R26_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R26_VALUE_OF_COLLATERAL;
	private	BigDecimal	R26_TOTAL_VALUE_NPL;
	private	BigDecimal	R26_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R26_SPECIFIC_PROV_NPL;
	private	String	R27_PRODUCT;
	private	BigDecimal	R27_30D_90D_PASTDUE;
	private	BigDecimal	R27_NON_PERFORM_LOANS;
	private	BigDecimal	R27_NON_ACCRUALS1;
	private	BigDecimal	R27_SPECIFIC_PROV1;
	private	BigDecimal	R27_NO_OF_ACC1;
	private	BigDecimal	R27_90D_180D_PASTDUE;
	private	BigDecimal	R27_NON_ACCRUALS2;
	private	BigDecimal	R27_SPECIFIC_PROV2;
	private	BigDecimal	R27_NO_OF_ACC2;
	private	BigDecimal	R27_180D_ABOVE_PASTDUE;
	private	BigDecimal	R27_NON_ACCRUALS3;
	private	BigDecimal	R27_SPECIFIC_PROV3;
	private	BigDecimal	R27_NO_OF_ACC3;
	private	BigDecimal	R27_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R27_TOTAL_DUE_LOANS;
	private	BigDecimal	R27_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R27_VALUE_OF_COLLATERAL;
	private	BigDecimal	R27_TOTAL_VALUE_NPL;
	private	BigDecimal	R27_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R27_SPECIFIC_PROV_NPL;
	private	String	R28_PRODUCT;
	private	BigDecimal	R28_30D_90D_PASTDUE;
	private	BigDecimal	R28_NON_PERFORM_LOANS;
	private	BigDecimal	R28_NON_ACCRUALS1;
	private	BigDecimal	R28_SPECIFIC_PROV1;
	private	BigDecimal	R28_NO_OF_ACC1;
	private	BigDecimal	R28_90D_180D_PASTDUE;
	private	BigDecimal	R28_NON_ACCRUALS2;
	private	BigDecimal	R28_SPECIFIC_PROV2;
	private	BigDecimal	R28_NO_OF_ACC2;
	private	BigDecimal	R28_180D_ABOVE_PASTDUE;
	private	BigDecimal	R28_NON_ACCRUALS3;
	private	BigDecimal	R28_SPECIFIC_PROV3;
	private	BigDecimal	R28_NO_OF_ACC3;
	private	BigDecimal	R28_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R28_TOTAL_DUE_LOANS;
	private	BigDecimal	R28_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R28_VALUE_OF_COLLATERAL;
	private	BigDecimal	R28_TOTAL_VALUE_NPL;
	private	BigDecimal	R28_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R28_SPECIFIC_PROV_NPL;
	private	String	R29_PRODUCT;
	private	BigDecimal	R29_30D_90D_PASTDUE;
	private	BigDecimal	R29_NON_PERFORM_LOANS;
	private	BigDecimal	R29_NON_ACCRUALS1;
	private	BigDecimal	R29_SPECIFIC_PROV1;
	private	BigDecimal	R29_NO_OF_ACC1;
	private	BigDecimal	R29_90D_180D_PASTDUE;
	private	BigDecimal	R29_NON_ACCRUALS2;
	private	BigDecimal	R29_SPECIFIC_PROV2;
	private	BigDecimal	R29_NO_OF_ACC2;
	private	BigDecimal	R29_180D_ABOVE_PASTDUE;
	private	BigDecimal	R29_NON_ACCRUALS3;
	private	BigDecimal	R29_SPECIFIC_PROV3;
	private	BigDecimal	R29_NO_OF_ACC3;
	private	BigDecimal	R29_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R29_TOTAL_DUE_LOANS;
	private	BigDecimal	R29_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R29_VALUE_OF_COLLATERAL;
	private	BigDecimal	R29_TOTAL_VALUE_NPL;
	private	BigDecimal	R29_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R29_SPECIFIC_PROV_NPL;
	private	String	R30_PRODUCT;
	private	BigDecimal	R30_30D_90D_PASTDUE;
	private	BigDecimal	R30_NON_PERFORM_LOANS;
	private	BigDecimal	R30_NON_ACCRUALS1;
	private	BigDecimal	R30_SPECIFIC_PROV1;
	private	BigDecimal	R30_NO_OF_ACC1;
	private	BigDecimal	R30_90D_180D_PASTDUE;
	private	BigDecimal	R30_NON_ACCRUALS2;
	private	BigDecimal	R30_SPECIFIC_PROV2;
	private	BigDecimal	R30_NO_OF_ACC2;
	private	BigDecimal	R30_180D_ABOVE_PASTDUE;
	private	BigDecimal	R30_NON_ACCRUALS3;
	private	BigDecimal	R30_SPECIFIC_PROV3;
	private	BigDecimal	R30_NO_OF_ACC3;
	private	BigDecimal	R30_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R30_TOTAL_DUE_LOANS;
	private	BigDecimal	R30_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R30_VALUE_OF_COLLATERAL;
	private	BigDecimal	R30_TOTAL_VALUE_NPL;
	private	BigDecimal	R30_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R30_SPECIFIC_PROV_NPL;
	private	String	R31_PRODUCT;
	private	BigDecimal	R31_30D_90D_PASTDUE;
	private	BigDecimal	R31_NON_PERFORM_LOANS;
	private	BigDecimal	R31_NON_ACCRUALS1;
	private	BigDecimal	R31_SPECIFIC_PROV1;
	private	BigDecimal	R31_NO_OF_ACC1;
	private	BigDecimal	R31_90D_180D_PASTDUE;
	private	BigDecimal	R31_NON_ACCRUALS2;
	private	BigDecimal	R31_SPECIFIC_PROV2;
	private	BigDecimal	R31_NO_OF_ACC2;
	private	BigDecimal	R31_180D_ABOVE_PASTDUE;
	private	BigDecimal	R31_NON_ACCRUALS3;
	private	BigDecimal	R31_SPECIFIC_PROV3;
	private	BigDecimal	R31_NO_OF_ACC3;
	private	BigDecimal	R31_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R31_TOTAL_DUE_LOANS;
	private	BigDecimal	R31_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R31_VALUE_OF_COLLATERAL;
	private	BigDecimal	R31_TOTAL_VALUE_NPL;
	private	BigDecimal	R31_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R31_SPECIFIC_PROV_NPL;
	private	String	R32_PRODUCT;
	private	BigDecimal	R32_30D_90D_PASTDUE;
	private	BigDecimal	R32_NON_PERFORM_LOANS;
	private	BigDecimal	R32_NON_ACCRUALS1;
	private	BigDecimal	R32_SPECIFIC_PROV1;
	private	BigDecimal	R32_NO_OF_ACC1;
	private	BigDecimal	R32_90D_180D_PASTDUE;
	private	BigDecimal	R32_NON_ACCRUALS2;
	private	BigDecimal	R32_SPECIFIC_PROV2;
	private	BigDecimal	R32_NO_OF_ACC2;
	private	BigDecimal	R32_180D_ABOVE_PASTDUE;
	private	BigDecimal	R32_NON_ACCRUALS3;
	private	BigDecimal	R32_SPECIFIC_PROV3;
	private	BigDecimal	R32_NO_OF_ACC3;
	private	BigDecimal	R32_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R32_TOTAL_DUE_LOANS;
	private	BigDecimal	R32_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R32_VALUE_OF_COLLATERAL;
	private	BigDecimal	R32_TOTAL_VALUE_NPL;
	private	BigDecimal	R32_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R32_SPECIFIC_PROV_NPL;
	private	String	R33_PRODUCT;
	private	BigDecimal	R33_30D_90D_PASTDUE;
	private	BigDecimal	R33_NON_PERFORM_LOANS;
	private	BigDecimal	R33_NON_ACCRUALS1;
	private	BigDecimal	R33_SPECIFIC_PROV1;
	private	BigDecimal	R33_NO_OF_ACC1;
	private	BigDecimal	R33_90D_180D_PASTDUE;
	private	BigDecimal	R33_NON_ACCRUALS2;
	private	BigDecimal	R33_SPECIFIC_PROV2;
	private	BigDecimal	R33_NO_OF_ACC2;
	private	BigDecimal	R33_180D_ABOVE_PASTDUE;
	private	BigDecimal	R33_NON_ACCRUALS3;
	private	BigDecimal	R33_SPECIFIC_PROV3;
	private	BigDecimal	R33_NO_OF_ACC3;
	private	BigDecimal	R33_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R33_TOTAL_DUE_LOANS;
	private	BigDecimal	R33_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R33_VALUE_OF_COLLATERAL;
	private	BigDecimal	R33_TOTAL_VALUE_NPL;
	private	BigDecimal	R33_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R33_SPECIFIC_PROV_NPL;
	private	String	R34_PRODUCT;
	private	BigDecimal	R34_30D_90D_PASTDUE;
	private	BigDecimal	R34_NON_PERFORM_LOANS;
	private	BigDecimal	R34_NON_ACCRUALS1;
	private	BigDecimal	R34_SPECIFIC_PROV1;
	private	BigDecimal	R34_NO_OF_ACC1;
	private	BigDecimal	R34_90D_180D_PASTDUE;
	private	BigDecimal	R34_NON_ACCRUALS2;
	private	BigDecimal	R34_SPECIFIC_PROV2;
	private	BigDecimal	R34_NO_OF_ACC2;
	private	BigDecimal	R34_180D_ABOVE_PASTDUE;
	private	BigDecimal	R34_NON_ACCRUALS3;
	private	BigDecimal	R34_SPECIFIC_PROV3;
	private	BigDecimal	R34_NO_OF_ACC3;
	private	BigDecimal	R34_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R34_TOTAL_DUE_LOANS;
	private	BigDecimal	R34_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R34_VALUE_OF_COLLATERAL;
	private	BigDecimal	R34_TOTAL_VALUE_NPL;
	private	BigDecimal	R34_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R34_SPECIFIC_PROV_NPL;
	private	String	R35_PRODUCT;
	private	BigDecimal	R35_30D_90D_PASTDUE;
	private	BigDecimal	R35_NON_PERFORM_LOANS;
	private	BigDecimal	R35_NON_ACCRUALS1;
	private	BigDecimal	R35_SPECIFIC_PROV1;
	private	BigDecimal	R35_NO_OF_ACC1;
	private	BigDecimal	R35_90D_180D_PASTDUE;
	private	BigDecimal	R35_NON_ACCRUALS2;
	private	BigDecimal	R35_SPECIFIC_PROV2;
	private	BigDecimal	R35_NO_OF_ACC2;
	private	BigDecimal	R35_180D_ABOVE_PASTDUE;
	private	BigDecimal	R35_NON_ACCRUALS3;
	private	BigDecimal	R35_SPECIFIC_PROV3;
	private	BigDecimal	R35_NO_OF_ACC3;
	private	BigDecimal	R35_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R35_TOTAL_DUE_LOANS;
	private	BigDecimal	R35_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R35_VALUE_OF_COLLATERAL;
	private	BigDecimal	R35_TOTAL_VALUE_NPL;
	private	BigDecimal	R35_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R35_SPECIFIC_PROV_NPL;
	private	String	R36_PRODUCT;
	private	BigDecimal	R36_30D_90D_PASTDUE;
	private	BigDecimal	R36_NON_PERFORM_LOANS;
	private	BigDecimal	R36_NON_ACCRUALS1;
	private	BigDecimal	R36_SPECIFIC_PROV1;
	private	BigDecimal	R36_NO_OF_ACC1;
	private	BigDecimal	R36_90D_180D_PASTDUE;
	private	BigDecimal	R36_NON_ACCRUALS2;
	private	BigDecimal	R36_SPECIFIC_PROV2;
	private	BigDecimal	R36_NO_OF_ACC2;
	private	BigDecimal	R36_180D_ABOVE_PASTDUE;
	private	BigDecimal	R36_NON_ACCRUALS3;
	private	BigDecimal	R36_SPECIFIC_PROV3;
	private	BigDecimal	R36_NO_OF_ACC3;
	private	BigDecimal	R36_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R36_TOTAL_DUE_LOANS;
	private	BigDecimal	R36_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R36_VALUE_OF_COLLATERAL;
	private	BigDecimal	R36_TOTAL_VALUE_NPL;
	private	BigDecimal	R36_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R36_SPECIFIC_PROV_NPL;
	private	String	R37_PRODUCT;
	private	BigDecimal	R37_30D_90D_PASTDUE;
	private	BigDecimal	R37_NON_PERFORM_LOANS;
	private	BigDecimal	R37_NON_ACCRUALS1;
	private	BigDecimal	R37_SPECIFIC_PROV1;
	private	BigDecimal	R37_NO_OF_ACC1;
	private	BigDecimal	R37_90D_180D_PASTDUE;
	private	BigDecimal	R37_NON_ACCRUALS2;
	private	BigDecimal	R37_SPECIFIC_PROV2;
	private	BigDecimal	R37_NO_OF_ACC2;
	private	BigDecimal	R37_180D_ABOVE_PASTDUE;
	private	BigDecimal	R37_NON_ACCRUALS3;
	private	BigDecimal	R37_SPECIFIC_PROV3;
	private	BigDecimal	R37_NO_OF_ACC3;
	private	BigDecimal	R37_TOTAL_NON_ACCRUAL;
	private	BigDecimal	R37_TOTAL_DUE_LOANS;
	private	BigDecimal	R37_TOTAL_PERFORMING_LOAN;
	private	BigDecimal	R37_VALUE_OF_COLLATERAL;
	private	BigDecimal	R37_TOTAL_VALUE_NPL;
	private	BigDecimal	R37_TOTAL_SPECIFIC_PROV;
	private	BigDecimal	R37_SPECIFIC_PROV_NPL;



    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Id
    private Date report_date;
    private String REPORT_VERSION;
    private String REPORT_CODE;
    private String REPORT_DESC;
    private String ENTITY_FLG;
    private String MODIFY_FLG;
    private String DEL_FLG;


    
    
   





	public String getR8_PRODUCT() {
		return R8_PRODUCT;
	}










	public void setR8_PRODUCT(String r8_PRODUCT) {
		R8_PRODUCT = r8_PRODUCT;
	}










	public BigDecimal getR8_30D_90D_PASTDUE() {
		return R8_30D_90D_PASTDUE;
	}










	public void setR8_30D_90D_PASTDUE(BigDecimal r8_30d_90d_PASTDUE) {
		R8_30D_90D_PASTDUE = r8_30d_90d_PASTDUE;
	}










	public BigDecimal getR8_NON_PERFORM_LOANS() {
		return R8_NON_PERFORM_LOANS;
	}










	public void setR8_NON_PERFORM_LOANS(BigDecimal r8_NON_PERFORM_LOANS) {
		R8_NON_PERFORM_LOANS = r8_NON_PERFORM_LOANS;
	}










	public BigDecimal getR8_NON_ACCRUALS1() {
		return R8_NON_ACCRUALS1;
	}










	public void setR8_NON_ACCRUALS1(BigDecimal r8_NON_ACCRUALS1) {
		R8_NON_ACCRUALS1 = r8_NON_ACCRUALS1;
	}










	public BigDecimal getR8_SPECIFIC_PROV1() {
		return R8_SPECIFIC_PROV1;
	}










	public void setR8_SPECIFIC_PROV1(BigDecimal r8_SPECIFIC_PROV1) {
		R8_SPECIFIC_PROV1 = r8_SPECIFIC_PROV1;
	}










	public BigDecimal getR8_NO_OF_ACC1() {
		return R8_NO_OF_ACC1;
	}










	public void setR8_NO_OF_ACC1(BigDecimal r8_NO_OF_ACC1) {
		R8_NO_OF_ACC1 = r8_NO_OF_ACC1;
	}










	public BigDecimal getR8_90D_180D_PASTDUE() {
		return R8_90D_180D_PASTDUE;
	}










	public void setR8_90D_180D_PASTDUE(BigDecimal r8_90d_180d_PASTDUE) {
		R8_90D_180D_PASTDUE = r8_90d_180d_PASTDUE;
	}










	public BigDecimal getR8_NON_ACCRUALS2() {
		return R8_NON_ACCRUALS2;
	}










	public void setR8_NON_ACCRUALS2(BigDecimal r8_NON_ACCRUALS2) {
		R8_NON_ACCRUALS2 = r8_NON_ACCRUALS2;
	}










	public BigDecimal getR8_SPECIFIC_PROV2() {
		return R8_SPECIFIC_PROV2;
	}










	public void setR8_SPECIFIC_PROV2(BigDecimal r8_SPECIFIC_PROV2) {
		R8_SPECIFIC_PROV2 = r8_SPECIFIC_PROV2;
	}










	public BigDecimal getR8_NO_OF_ACC2() {
		return R8_NO_OF_ACC2;
	}










	public void setR8_NO_OF_ACC2(BigDecimal r8_NO_OF_ACC2) {
		R8_NO_OF_ACC2 = r8_NO_OF_ACC2;
	}










	public BigDecimal getR8_180D_ABOVE_PASTDUE() {
		return R8_180D_ABOVE_PASTDUE;
	}










	public void setR8_180D_ABOVE_PASTDUE(BigDecimal r8_180d_ABOVE_PASTDUE) {
		R8_180D_ABOVE_PASTDUE = r8_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR8_NON_ACCRUALS3() {
		return R8_NON_ACCRUALS3;
	}










	public void setR8_NON_ACCRUALS3(BigDecimal r8_NON_ACCRUALS3) {
		R8_NON_ACCRUALS3 = r8_NON_ACCRUALS3;
	}










	public BigDecimal getR8_SPECIFIC_PROV3() {
		return R8_SPECIFIC_PROV3;
	}










	public void setR8_SPECIFIC_PROV3(BigDecimal r8_SPECIFIC_PROV3) {
		R8_SPECIFIC_PROV3 = r8_SPECIFIC_PROV3;
	}










	public BigDecimal getR8_NO_OF_ACC3() {
		return R8_NO_OF_ACC3;
	}










	public void setR8_NO_OF_ACC3(BigDecimal r8_NO_OF_ACC3) {
		R8_NO_OF_ACC3 = r8_NO_OF_ACC3;
	}










	public BigDecimal getR8_TOTAL_NON_ACCRUAL() {
		return R8_TOTAL_NON_ACCRUAL;
	}










	public void setR8_TOTAL_NON_ACCRUAL(BigDecimal r8_TOTAL_NON_ACCRUAL) {
		R8_TOTAL_NON_ACCRUAL = r8_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR8_TOTAL_DUE_LOANS() {
		return R8_TOTAL_DUE_LOANS;
	}










	public void setR8_TOTAL_DUE_LOANS(BigDecimal r8_TOTAL_DUE_LOANS) {
		R8_TOTAL_DUE_LOANS = r8_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR8_TOTAL_PERFORMING_LOAN() {
		return R8_TOTAL_PERFORMING_LOAN;
	}










	public void setR8_TOTAL_PERFORMING_LOAN(BigDecimal r8_TOTAL_PERFORMING_LOAN) {
		R8_TOTAL_PERFORMING_LOAN = r8_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR8_VALUE_OF_COLLATERAL() {
		return R8_VALUE_OF_COLLATERAL;
	}










	public void setR8_VALUE_OF_COLLATERAL(BigDecimal r8_VALUE_OF_COLLATERAL) {
		R8_VALUE_OF_COLLATERAL = r8_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR8_TOTAL_VALUE_NPL() {
		return R8_TOTAL_VALUE_NPL;
	}










	public void setR8_TOTAL_VALUE_NPL(BigDecimal r8_TOTAL_VALUE_NPL) {
		R8_TOTAL_VALUE_NPL = r8_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR8_TOTAL_SPECIFIC_PROV() {
		return R8_TOTAL_SPECIFIC_PROV;
	}










	public void setR8_TOTAL_SPECIFIC_PROV(BigDecimal r8_TOTAL_SPECIFIC_PROV) {
		R8_TOTAL_SPECIFIC_PROV = r8_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR8_SPECIFIC_PROV_NPL() {
		return R8_SPECIFIC_PROV_NPL;
	}










	public void setR8_SPECIFIC_PROV_NPL(BigDecimal r8_SPECIFIC_PROV_NPL) {
		R8_SPECIFIC_PROV_NPL = r8_SPECIFIC_PROV_NPL;
	}










	public String getR9_PRODUCT() {
		return R9_PRODUCT;
	}










	public void setR9_PRODUCT(String r9_PRODUCT) {
		R9_PRODUCT = r9_PRODUCT;
	}










	public BigDecimal getR9_30D_90D_PASTDUE() {
		return R9_30D_90D_PASTDUE;
	}










	public void setR9_30D_90D_PASTDUE(BigDecimal r9_30d_90d_PASTDUE) {
		R9_30D_90D_PASTDUE = r9_30d_90d_PASTDUE;
	}










	public BigDecimal getR9_NON_PERFORM_LOANS() {
		return R9_NON_PERFORM_LOANS;
	}










	public void setR9_NON_PERFORM_LOANS(BigDecimal r9_NON_PERFORM_LOANS) {
		R9_NON_PERFORM_LOANS = r9_NON_PERFORM_LOANS;
	}










	public BigDecimal getR9_NON_ACCRUALS1() {
		return R9_NON_ACCRUALS1;
	}










	public void setR9_NON_ACCRUALS1(BigDecimal r9_NON_ACCRUALS1) {
		R9_NON_ACCRUALS1 = r9_NON_ACCRUALS1;
	}










	public BigDecimal getR9_SPECIFIC_PROV1() {
		return R9_SPECIFIC_PROV1;
	}










	public void setR9_SPECIFIC_PROV1(BigDecimal r9_SPECIFIC_PROV1) {
		R9_SPECIFIC_PROV1 = r9_SPECIFIC_PROV1;
	}










	public BigDecimal getR9_NO_OF_ACC1() {
		return R9_NO_OF_ACC1;
	}










	public void setR9_NO_OF_ACC1(BigDecimal r9_NO_OF_ACC1) {
		R9_NO_OF_ACC1 = r9_NO_OF_ACC1;
	}










	public BigDecimal getR9_90D_180D_PASTDUE() {
		return R9_90D_180D_PASTDUE;
	}










	public void setR9_90D_180D_PASTDUE(BigDecimal r9_90d_180d_PASTDUE) {
		R9_90D_180D_PASTDUE = r9_90d_180d_PASTDUE;
	}










	public BigDecimal getR9_NON_ACCRUALS2() {
		return R9_NON_ACCRUALS2;
	}










	public void setR9_NON_ACCRUALS2(BigDecimal r9_NON_ACCRUALS2) {
		R9_NON_ACCRUALS2 = r9_NON_ACCRUALS2;
	}










	public BigDecimal getR9_SPECIFIC_PROV2() {
		return R9_SPECIFIC_PROV2;
	}










	public void setR9_SPECIFIC_PROV2(BigDecimal r9_SPECIFIC_PROV2) {
		R9_SPECIFIC_PROV2 = r9_SPECIFIC_PROV2;
	}










	public BigDecimal getR9_NO_OF_ACC2() {
		return R9_NO_OF_ACC2;
	}










	public void setR9_NO_OF_ACC2(BigDecimal r9_NO_OF_ACC2) {
		R9_NO_OF_ACC2 = r9_NO_OF_ACC2;
	}










	public BigDecimal getR9_180D_ABOVE_PASTDUE() {
		return R9_180D_ABOVE_PASTDUE;
	}










	public void setR9_180D_ABOVE_PASTDUE(BigDecimal r9_180d_ABOVE_PASTDUE) {
		R9_180D_ABOVE_PASTDUE = r9_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR9_NON_ACCRUALS3() {
		return R9_NON_ACCRUALS3;
	}










	public void setR9_NON_ACCRUALS3(BigDecimal r9_NON_ACCRUALS3) {
		R9_NON_ACCRUALS3 = r9_NON_ACCRUALS3;
	}










	public BigDecimal getR9_SPECIFIC_PROV3() {
		return R9_SPECIFIC_PROV3;
	}










	public void setR9_SPECIFIC_PROV3(BigDecimal r9_SPECIFIC_PROV3) {
		R9_SPECIFIC_PROV3 = r9_SPECIFIC_PROV3;
	}










	public BigDecimal getR9_NO_OF_ACC3() {
		return R9_NO_OF_ACC3;
	}










	public void setR9_NO_OF_ACC3(BigDecimal r9_NO_OF_ACC3) {
		R9_NO_OF_ACC3 = r9_NO_OF_ACC3;
	}










	public BigDecimal getR9_TOTAL_NON_ACCRUAL() {
		return R9_TOTAL_NON_ACCRUAL;
	}










	public void setR9_TOTAL_NON_ACCRUAL(BigDecimal r9_TOTAL_NON_ACCRUAL) {
		R9_TOTAL_NON_ACCRUAL = r9_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR9_TOTAL_DUE_LOANS() {
		return R9_TOTAL_DUE_LOANS;
	}










	public void setR9_TOTAL_DUE_LOANS(BigDecimal r9_TOTAL_DUE_LOANS) {
		R9_TOTAL_DUE_LOANS = r9_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR9_TOTAL_PERFORMING_LOAN() {
		return R9_TOTAL_PERFORMING_LOAN;
	}










	public void setR9_TOTAL_PERFORMING_LOAN(BigDecimal r9_TOTAL_PERFORMING_LOAN) {
		R9_TOTAL_PERFORMING_LOAN = r9_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR9_VALUE_OF_COLLATERAL() {
		return R9_VALUE_OF_COLLATERAL;
	}










	public void setR9_VALUE_OF_COLLATERAL(BigDecimal r9_VALUE_OF_COLLATERAL) {
		R9_VALUE_OF_COLLATERAL = r9_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR9_TOTAL_VALUE_NPL() {
		return R9_TOTAL_VALUE_NPL;
	}










	public void setR9_TOTAL_VALUE_NPL(BigDecimal r9_TOTAL_VALUE_NPL) {
		R9_TOTAL_VALUE_NPL = r9_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR9_TOTAL_SPECIFIC_PROV() {
		return R9_TOTAL_SPECIFIC_PROV;
	}










	public void setR9_TOTAL_SPECIFIC_PROV(BigDecimal r9_TOTAL_SPECIFIC_PROV) {
		R9_TOTAL_SPECIFIC_PROV = r9_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR9_SPECIFIC_PROV_NPL() {
		return R9_SPECIFIC_PROV_NPL;
	}










	public void setR9_SPECIFIC_PROV_NPL(BigDecimal r9_SPECIFIC_PROV_NPL) {
		R9_SPECIFIC_PROV_NPL = r9_SPECIFIC_PROV_NPL;
	}










	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}










	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}










	public BigDecimal getR10_30D_90D_PASTDUE() {
		return R10_30D_90D_PASTDUE;
	}










	public void setR10_30D_90D_PASTDUE(BigDecimal r10_30d_90d_PASTDUE) {
		R10_30D_90D_PASTDUE = r10_30d_90d_PASTDUE;
	}










	public BigDecimal getR10_NON_PERFORM_LOANS() {
		return R10_NON_PERFORM_LOANS;
	}










	public void setR10_NON_PERFORM_LOANS(BigDecimal r10_NON_PERFORM_LOANS) {
		R10_NON_PERFORM_LOANS = r10_NON_PERFORM_LOANS;
	}










	public BigDecimal getR10_NON_ACCRUALS1() {
		return R10_NON_ACCRUALS1;
	}










	public void setR10_NON_ACCRUALS1(BigDecimal r10_NON_ACCRUALS1) {
		R10_NON_ACCRUALS1 = r10_NON_ACCRUALS1;
	}










	public BigDecimal getR10_SPECIFIC_PROV1() {
		return R10_SPECIFIC_PROV1;
	}










	public void setR10_SPECIFIC_PROV1(BigDecimal r10_SPECIFIC_PROV1) {
		R10_SPECIFIC_PROV1 = r10_SPECIFIC_PROV1;
	}










	public BigDecimal getR10_NO_OF_ACC1() {
		return R10_NO_OF_ACC1;
	}










	public void setR10_NO_OF_ACC1(BigDecimal r10_NO_OF_ACC1) {
		R10_NO_OF_ACC1 = r10_NO_OF_ACC1;
	}










	public BigDecimal getR10_90D_180D_PASTDUE() {
		return R10_90D_180D_PASTDUE;
	}










	public void setR10_90D_180D_PASTDUE(BigDecimal r10_90d_180d_PASTDUE) {
		R10_90D_180D_PASTDUE = r10_90d_180d_PASTDUE;
	}










	public BigDecimal getR10_NON_ACCRUALS2() {
		return R10_NON_ACCRUALS2;
	}










	public void setR10_NON_ACCRUALS2(BigDecimal r10_NON_ACCRUALS2) {
		R10_NON_ACCRUALS2 = r10_NON_ACCRUALS2;
	}










	public BigDecimal getR10_SPECIFIC_PROV2() {
		return R10_SPECIFIC_PROV2;
	}










	public void setR10_SPECIFIC_PROV2(BigDecimal r10_SPECIFIC_PROV2) {
		R10_SPECIFIC_PROV2 = r10_SPECIFIC_PROV2;
	}










	public BigDecimal getR10_NO_OF_ACC2() {
		return R10_NO_OF_ACC2;
	}










	public void setR10_NO_OF_ACC2(BigDecimal r10_NO_OF_ACC2) {
		R10_NO_OF_ACC2 = r10_NO_OF_ACC2;
	}










	public BigDecimal getR10_180D_ABOVE_PASTDUE() {
		return R10_180D_ABOVE_PASTDUE;
	}










	public void setR10_180D_ABOVE_PASTDUE(BigDecimal r10_180d_ABOVE_PASTDUE) {
		R10_180D_ABOVE_PASTDUE = r10_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR10_NON_ACCRUALS3() {
		return R10_NON_ACCRUALS3;
	}










	public void setR10_NON_ACCRUALS3(BigDecimal r10_NON_ACCRUALS3) {
		R10_NON_ACCRUALS3 = r10_NON_ACCRUALS3;
	}










	public BigDecimal getR10_SPECIFIC_PROV3() {
		return R10_SPECIFIC_PROV3;
	}










	public void setR10_SPECIFIC_PROV3(BigDecimal r10_SPECIFIC_PROV3) {
		R10_SPECIFIC_PROV3 = r10_SPECIFIC_PROV3;
	}










	public BigDecimal getR10_NO_OF_ACC3() {
		return R10_NO_OF_ACC3;
	}










	public void setR10_NO_OF_ACC3(BigDecimal r10_NO_OF_ACC3) {
		R10_NO_OF_ACC3 = r10_NO_OF_ACC3;
	}










	public BigDecimal getR10_TOTAL_NON_ACCRUAL() {
		return R10_TOTAL_NON_ACCRUAL;
	}










	public void setR10_TOTAL_NON_ACCRUAL(BigDecimal r10_TOTAL_NON_ACCRUAL) {
		R10_TOTAL_NON_ACCRUAL = r10_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR10_TOTAL_DUE_LOANS() {
		return R10_TOTAL_DUE_LOANS;
	}










	public void setR10_TOTAL_DUE_LOANS(BigDecimal r10_TOTAL_DUE_LOANS) {
		R10_TOTAL_DUE_LOANS = r10_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR10_TOTAL_PERFORMING_LOAN() {
		return R10_TOTAL_PERFORMING_LOAN;
	}










	public void setR10_TOTAL_PERFORMING_LOAN(BigDecimal r10_TOTAL_PERFORMING_LOAN) {
		R10_TOTAL_PERFORMING_LOAN = r10_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR10_VALUE_OF_COLLATERAL() {
		return R10_VALUE_OF_COLLATERAL;
	}










	public void setR10_VALUE_OF_COLLATERAL(BigDecimal r10_VALUE_OF_COLLATERAL) {
		R10_VALUE_OF_COLLATERAL = r10_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR10_TOTAL_VALUE_NPL() {
		return R10_TOTAL_VALUE_NPL;
	}










	public void setR10_TOTAL_VALUE_NPL(BigDecimal r10_TOTAL_VALUE_NPL) {
		R10_TOTAL_VALUE_NPL = r10_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR10_TOTAL_SPECIFIC_PROV() {
		return R10_TOTAL_SPECIFIC_PROV;
	}










	public void setR10_TOTAL_SPECIFIC_PROV(BigDecimal r10_TOTAL_SPECIFIC_PROV) {
		R10_TOTAL_SPECIFIC_PROV = r10_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR10_SPECIFIC_PROV_NPL() {
		return R10_SPECIFIC_PROV_NPL;
	}










	public void setR10_SPECIFIC_PROV_NPL(BigDecimal r10_SPECIFIC_PROV_NPL) {
		R10_SPECIFIC_PROV_NPL = r10_SPECIFIC_PROV_NPL;
	}










	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}










	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}










	public BigDecimal getR11_30D_90D_PASTDUE() {
		return R11_30D_90D_PASTDUE;
	}










	public void setR11_30D_90D_PASTDUE(BigDecimal r11_30d_90d_PASTDUE) {
		R11_30D_90D_PASTDUE = r11_30d_90d_PASTDUE;
	}










	public BigDecimal getR11_NON_PERFORM_LOANS() {
		return R11_NON_PERFORM_LOANS;
	}










	public void setR11_NON_PERFORM_LOANS(BigDecimal r11_NON_PERFORM_LOANS) {
		R11_NON_PERFORM_LOANS = r11_NON_PERFORM_LOANS;
	}










	public BigDecimal getR11_NON_ACCRUALS1() {
		return R11_NON_ACCRUALS1;
	}










	public void setR11_NON_ACCRUALS1(BigDecimal r11_NON_ACCRUALS1) {
		R11_NON_ACCRUALS1 = r11_NON_ACCRUALS1;
	}










	public BigDecimal getR11_SPECIFIC_PROV1() {
		return R11_SPECIFIC_PROV1;
	}










	public void setR11_SPECIFIC_PROV1(BigDecimal r11_SPECIFIC_PROV1) {
		R11_SPECIFIC_PROV1 = r11_SPECIFIC_PROV1;
	}










	public BigDecimal getR11_NO_OF_ACC1() {
		return R11_NO_OF_ACC1;
	}










	public void setR11_NO_OF_ACC1(BigDecimal r11_NO_OF_ACC1) {
		R11_NO_OF_ACC1 = r11_NO_OF_ACC1;
	}










	public BigDecimal getR11_90D_180D_PASTDUE() {
		return R11_90D_180D_PASTDUE;
	}










	public void setR11_90D_180D_PASTDUE(BigDecimal r11_90d_180d_PASTDUE) {
		R11_90D_180D_PASTDUE = r11_90d_180d_PASTDUE;
	}










	public BigDecimal getR11_NON_ACCRUALS2() {
		return R11_NON_ACCRUALS2;
	}










	public void setR11_NON_ACCRUALS2(BigDecimal r11_NON_ACCRUALS2) {
		R11_NON_ACCRUALS2 = r11_NON_ACCRUALS2;
	}










	public BigDecimal getR11_SPECIFIC_PROV2() {
		return R11_SPECIFIC_PROV2;
	}










	public void setR11_SPECIFIC_PROV2(BigDecimal r11_SPECIFIC_PROV2) {
		R11_SPECIFIC_PROV2 = r11_SPECIFIC_PROV2;
	}










	public BigDecimal getR11_NO_OF_ACC2() {
		return R11_NO_OF_ACC2;
	}










	public void setR11_NO_OF_ACC2(BigDecimal r11_NO_OF_ACC2) {
		R11_NO_OF_ACC2 = r11_NO_OF_ACC2;
	}










	public BigDecimal getR11_180D_ABOVE_PASTDUE() {
		return R11_180D_ABOVE_PASTDUE;
	}










	public void setR11_180D_ABOVE_PASTDUE(BigDecimal r11_180d_ABOVE_PASTDUE) {
		R11_180D_ABOVE_PASTDUE = r11_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR11_NON_ACCRUALS3() {
		return R11_NON_ACCRUALS3;
	}










	public void setR11_NON_ACCRUALS3(BigDecimal r11_NON_ACCRUALS3) {
		R11_NON_ACCRUALS3 = r11_NON_ACCRUALS3;
	}










	public BigDecimal getR11_SPECIFIC_PROV3() {
		return R11_SPECIFIC_PROV3;
	}










	public void setR11_SPECIFIC_PROV3(BigDecimal r11_SPECIFIC_PROV3) {
		R11_SPECIFIC_PROV3 = r11_SPECIFIC_PROV3;
	}










	public BigDecimal getR11_NO_OF_ACC3() {
		return R11_NO_OF_ACC3;
	}










	public void setR11_NO_OF_ACC3(BigDecimal r11_NO_OF_ACC3) {
		R11_NO_OF_ACC3 = r11_NO_OF_ACC3;
	}










	public BigDecimal getR11_TOTAL_NON_ACCRUAL() {
		return R11_TOTAL_NON_ACCRUAL;
	}










	public void setR11_TOTAL_NON_ACCRUAL(BigDecimal r11_TOTAL_NON_ACCRUAL) {
		R11_TOTAL_NON_ACCRUAL = r11_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR11_TOTAL_DUE_LOANS() {
		return R11_TOTAL_DUE_LOANS;
	}










	public void setR11_TOTAL_DUE_LOANS(BigDecimal r11_TOTAL_DUE_LOANS) {
		R11_TOTAL_DUE_LOANS = r11_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR11_TOTAL_PERFORMING_LOAN() {
		return R11_TOTAL_PERFORMING_LOAN;
	}










	public void setR11_TOTAL_PERFORMING_LOAN(BigDecimal r11_TOTAL_PERFORMING_LOAN) {
		R11_TOTAL_PERFORMING_LOAN = r11_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR11_VALUE_OF_COLLATERAL() {
		return R11_VALUE_OF_COLLATERAL;
	}










	public void setR11_VALUE_OF_COLLATERAL(BigDecimal r11_VALUE_OF_COLLATERAL) {
		R11_VALUE_OF_COLLATERAL = r11_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR11_TOTAL_VALUE_NPL() {
		return R11_TOTAL_VALUE_NPL;
	}










	public void setR11_TOTAL_VALUE_NPL(BigDecimal r11_TOTAL_VALUE_NPL) {
		R11_TOTAL_VALUE_NPL = r11_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR11_TOTAL_SPECIFIC_PROV() {
		return R11_TOTAL_SPECIFIC_PROV;
	}










	public void setR11_TOTAL_SPECIFIC_PROV(BigDecimal r11_TOTAL_SPECIFIC_PROV) {
		R11_TOTAL_SPECIFIC_PROV = r11_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR11_SPECIFIC_PROV_NPL() {
		return R11_SPECIFIC_PROV_NPL;
	}










	public void setR11_SPECIFIC_PROV_NPL(BigDecimal r11_SPECIFIC_PROV_NPL) {
		R11_SPECIFIC_PROV_NPL = r11_SPECIFIC_PROV_NPL;
	}










	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}










	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}










	public BigDecimal getR12_30D_90D_PASTDUE() {
		return R12_30D_90D_PASTDUE;
	}










	public void setR12_30D_90D_PASTDUE(BigDecimal r12_30d_90d_PASTDUE) {
		R12_30D_90D_PASTDUE = r12_30d_90d_PASTDUE;
	}










	public BigDecimal getR12_NON_PERFORM_LOANS() {
		return R12_NON_PERFORM_LOANS;
	}










	public void setR12_NON_PERFORM_LOANS(BigDecimal r12_NON_PERFORM_LOANS) {
		R12_NON_PERFORM_LOANS = r12_NON_PERFORM_LOANS;
	}










	public BigDecimal getR12_NON_ACCRUALS1() {
		return R12_NON_ACCRUALS1;
	}










	public void setR12_NON_ACCRUALS1(BigDecimal r12_NON_ACCRUALS1) {
		R12_NON_ACCRUALS1 = r12_NON_ACCRUALS1;
	}










	public BigDecimal getR12_SPECIFIC_PROV1() {
		return R12_SPECIFIC_PROV1;
	}










	public void setR12_SPECIFIC_PROV1(BigDecimal r12_SPECIFIC_PROV1) {
		R12_SPECIFIC_PROV1 = r12_SPECIFIC_PROV1;
	}










	public BigDecimal getR12_NO_OF_ACC1() {
		return R12_NO_OF_ACC1;
	}










	public void setR12_NO_OF_ACC1(BigDecimal r12_NO_OF_ACC1) {
		R12_NO_OF_ACC1 = r12_NO_OF_ACC1;
	}










	public BigDecimal getR12_90D_180D_PASTDUE() {
		return R12_90D_180D_PASTDUE;
	}










	public void setR12_90D_180D_PASTDUE(BigDecimal r12_90d_180d_PASTDUE) {
		R12_90D_180D_PASTDUE = r12_90d_180d_PASTDUE;
	}










	public BigDecimal getR12_NON_ACCRUALS2() {
		return R12_NON_ACCRUALS2;
	}










	public void setR12_NON_ACCRUALS2(BigDecimal r12_NON_ACCRUALS2) {
		R12_NON_ACCRUALS2 = r12_NON_ACCRUALS2;
	}










	public BigDecimal getR12_SPECIFIC_PROV2() {
		return R12_SPECIFIC_PROV2;
	}










	public void setR12_SPECIFIC_PROV2(BigDecimal r12_SPECIFIC_PROV2) {
		R12_SPECIFIC_PROV2 = r12_SPECIFIC_PROV2;
	}










	public BigDecimal getR12_NO_OF_ACC2() {
		return R12_NO_OF_ACC2;
	}










	public void setR12_NO_OF_ACC2(BigDecimal r12_NO_OF_ACC2) {
		R12_NO_OF_ACC2 = r12_NO_OF_ACC2;
	}










	public BigDecimal getR12_180D_ABOVE_PASTDUE() {
		return R12_180D_ABOVE_PASTDUE;
	}










	public void setR12_180D_ABOVE_PASTDUE(BigDecimal r12_180d_ABOVE_PASTDUE) {
		R12_180D_ABOVE_PASTDUE = r12_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR12_NON_ACCRUALS3() {
		return R12_NON_ACCRUALS3;
	}










	public void setR12_NON_ACCRUALS3(BigDecimal r12_NON_ACCRUALS3) {
		R12_NON_ACCRUALS3 = r12_NON_ACCRUALS3;
	}










	public BigDecimal getR12_SPECIFIC_PROV3() {
		return R12_SPECIFIC_PROV3;
	}










	public void setR12_SPECIFIC_PROV3(BigDecimal r12_SPECIFIC_PROV3) {
		R12_SPECIFIC_PROV3 = r12_SPECIFIC_PROV3;
	}










	public BigDecimal getR12_NO_OF_ACC3() {
		return R12_NO_OF_ACC3;
	}










	public void setR12_NO_OF_ACC3(BigDecimal r12_NO_OF_ACC3) {
		R12_NO_OF_ACC3 = r12_NO_OF_ACC3;
	}










	public BigDecimal getR12_TOTAL_NON_ACCRUAL() {
		return R12_TOTAL_NON_ACCRUAL;
	}










	public void setR12_TOTAL_NON_ACCRUAL(BigDecimal r12_TOTAL_NON_ACCRUAL) {
		R12_TOTAL_NON_ACCRUAL = r12_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR12_TOTAL_DUE_LOANS() {
		return R12_TOTAL_DUE_LOANS;
	}










	public void setR12_TOTAL_DUE_LOANS(BigDecimal r12_TOTAL_DUE_LOANS) {
		R12_TOTAL_DUE_LOANS = r12_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR12_TOTAL_PERFORMING_LOAN() {
		return R12_TOTAL_PERFORMING_LOAN;
	}










	public void setR12_TOTAL_PERFORMING_LOAN(BigDecimal r12_TOTAL_PERFORMING_LOAN) {
		R12_TOTAL_PERFORMING_LOAN = r12_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR12_VALUE_OF_COLLATERAL() {
		return R12_VALUE_OF_COLLATERAL;
	}










	public void setR12_VALUE_OF_COLLATERAL(BigDecimal r12_VALUE_OF_COLLATERAL) {
		R12_VALUE_OF_COLLATERAL = r12_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR12_TOTAL_VALUE_NPL() {
		return R12_TOTAL_VALUE_NPL;
	}










	public void setR12_TOTAL_VALUE_NPL(BigDecimal r12_TOTAL_VALUE_NPL) {
		R12_TOTAL_VALUE_NPL = r12_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR12_TOTAL_SPECIFIC_PROV() {
		return R12_TOTAL_SPECIFIC_PROV;
	}










	public void setR12_TOTAL_SPECIFIC_PROV(BigDecimal r12_TOTAL_SPECIFIC_PROV) {
		R12_TOTAL_SPECIFIC_PROV = r12_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR12_SPECIFIC_PROV_NPL() {
		return R12_SPECIFIC_PROV_NPL;
	}










	public void setR12_SPECIFIC_PROV_NPL(BigDecimal r12_SPECIFIC_PROV_NPL) {
		R12_SPECIFIC_PROV_NPL = r12_SPECIFIC_PROV_NPL;
	}










	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}










	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}










	public BigDecimal getR13_30D_90D_PASTDUE() {
		return R13_30D_90D_PASTDUE;
	}










	public void setR13_30D_90D_PASTDUE(BigDecimal r13_30d_90d_PASTDUE) {
		R13_30D_90D_PASTDUE = r13_30d_90d_PASTDUE;
	}










	public BigDecimal getR13_NON_PERFORM_LOANS() {
		return R13_NON_PERFORM_LOANS;
	}










	public void setR13_NON_PERFORM_LOANS(BigDecimal r13_NON_PERFORM_LOANS) {
		R13_NON_PERFORM_LOANS = r13_NON_PERFORM_LOANS;
	}










	public BigDecimal getR13_NON_ACCRUALS1() {
		return R13_NON_ACCRUALS1;
	}










	public void setR13_NON_ACCRUALS1(BigDecimal r13_NON_ACCRUALS1) {
		R13_NON_ACCRUALS1 = r13_NON_ACCRUALS1;
	}










	public BigDecimal getR13_SPECIFIC_PROV1() {
		return R13_SPECIFIC_PROV1;
	}










	public void setR13_SPECIFIC_PROV1(BigDecimal r13_SPECIFIC_PROV1) {
		R13_SPECIFIC_PROV1 = r13_SPECIFIC_PROV1;
	}










	public BigDecimal getR13_NO_OF_ACC1() {
		return R13_NO_OF_ACC1;
	}










	public void setR13_NO_OF_ACC1(BigDecimal r13_NO_OF_ACC1) {
		R13_NO_OF_ACC1 = r13_NO_OF_ACC1;
	}










	public BigDecimal getR13_90D_180D_PASTDUE() {
		return R13_90D_180D_PASTDUE;
	}










	public void setR13_90D_180D_PASTDUE(BigDecimal r13_90d_180d_PASTDUE) {
		R13_90D_180D_PASTDUE = r13_90d_180d_PASTDUE;
	}










	public BigDecimal getR13_NON_ACCRUALS2() {
		return R13_NON_ACCRUALS2;
	}










	public void setR13_NON_ACCRUALS2(BigDecimal r13_NON_ACCRUALS2) {
		R13_NON_ACCRUALS2 = r13_NON_ACCRUALS2;
	}










	public BigDecimal getR13_SPECIFIC_PROV2() {
		return R13_SPECIFIC_PROV2;
	}










	public void setR13_SPECIFIC_PROV2(BigDecimal r13_SPECIFIC_PROV2) {
		R13_SPECIFIC_PROV2 = r13_SPECIFIC_PROV2;
	}










	public BigDecimal getR13_NO_OF_ACC2() {
		return R13_NO_OF_ACC2;
	}










	public void setR13_NO_OF_ACC2(BigDecimal r13_NO_OF_ACC2) {
		R13_NO_OF_ACC2 = r13_NO_OF_ACC2;
	}










	public BigDecimal getR13_180D_ABOVE_PASTDUE() {
		return R13_180D_ABOVE_PASTDUE;
	}










	public void setR13_180D_ABOVE_PASTDUE(BigDecimal r13_180d_ABOVE_PASTDUE) {
		R13_180D_ABOVE_PASTDUE = r13_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR13_NON_ACCRUALS3() {
		return R13_NON_ACCRUALS3;
	}










	public void setR13_NON_ACCRUALS3(BigDecimal r13_NON_ACCRUALS3) {
		R13_NON_ACCRUALS3 = r13_NON_ACCRUALS3;
	}










	public BigDecimal getR13_SPECIFIC_PROV3() {
		return R13_SPECIFIC_PROV3;
	}










	public void setR13_SPECIFIC_PROV3(BigDecimal r13_SPECIFIC_PROV3) {
		R13_SPECIFIC_PROV3 = r13_SPECIFIC_PROV3;
	}










	public BigDecimal getR13_NO_OF_ACC3() {
		return R13_NO_OF_ACC3;
	}










	public void setR13_NO_OF_ACC3(BigDecimal r13_NO_OF_ACC3) {
		R13_NO_OF_ACC3 = r13_NO_OF_ACC3;
	}










	public BigDecimal getR13_TOTAL_NON_ACCRUAL() {
		return R13_TOTAL_NON_ACCRUAL;
	}










	public void setR13_TOTAL_NON_ACCRUAL(BigDecimal r13_TOTAL_NON_ACCRUAL) {
		R13_TOTAL_NON_ACCRUAL = r13_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR13_TOTAL_DUE_LOANS() {
		return R13_TOTAL_DUE_LOANS;
	}










	public void setR13_TOTAL_DUE_LOANS(BigDecimal r13_TOTAL_DUE_LOANS) {
		R13_TOTAL_DUE_LOANS = r13_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR13_TOTAL_PERFORMING_LOAN() {
		return R13_TOTAL_PERFORMING_LOAN;
	}










	public void setR13_TOTAL_PERFORMING_LOAN(BigDecimal r13_TOTAL_PERFORMING_LOAN) {
		R13_TOTAL_PERFORMING_LOAN = r13_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR13_VALUE_OF_COLLATERAL() {
		return R13_VALUE_OF_COLLATERAL;
	}










	public void setR13_VALUE_OF_COLLATERAL(BigDecimal r13_VALUE_OF_COLLATERAL) {
		R13_VALUE_OF_COLLATERAL = r13_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR13_TOTAL_VALUE_NPL() {
		return R13_TOTAL_VALUE_NPL;
	}










	public void setR13_TOTAL_VALUE_NPL(BigDecimal r13_TOTAL_VALUE_NPL) {
		R13_TOTAL_VALUE_NPL = r13_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR13_TOTAL_SPECIFIC_PROV() {
		return R13_TOTAL_SPECIFIC_PROV;
	}










	public void setR13_TOTAL_SPECIFIC_PROV(BigDecimal r13_TOTAL_SPECIFIC_PROV) {
		R13_TOTAL_SPECIFIC_PROV = r13_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR13_SPECIFIC_PROV_NPL() {
		return R13_SPECIFIC_PROV_NPL;
	}










	public void setR13_SPECIFIC_PROV_NPL(BigDecimal r13_SPECIFIC_PROV_NPL) {
		R13_SPECIFIC_PROV_NPL = r13_SPECIFIC_PROV_NPL;
	}










	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}










	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}










	public BigDecimal getR14_30D_90D_PASTDUE() {
		return R14_30D_90D_PASTDUE;
	}










	public void setR14_30D_90D_PASTDUE(BigDecimal r14_30d_90d_PASTDUE) {
		R14_30D_90D_PASTDUE = r14_30d_90d_PASTDUE;
	}










	public BigDecimal getR14_NON_PERFORM_LOANS() {
		return R14_NON_PERFORM_LOANS;
	}










	public void setR14_NON_PERFORM_LOANS(BigDecimal r14_NON_PERFORM_LOANS) {
		R14_NON_PERFORM_LOANS = r14_NON_PERFORM_LOANS;
	}










	public BigDecimal getR14_NON_ACCRUALS1() {
		return R14_NON_ACCRUALS1;
	}










	public void setR14_NON_ACCRUALS1(BigDecimal r14_NON_ACCRUALS1) {
		R14_NON_ACCRUALS1 = r14_NON_ACCRUALS1;
	}










	public BigDecimal getR14_SPECIFIC_PROV1() {
		return R14_SPECIFIC_PROV1;
	}










	public void setR14_SPECIFIC_PROV1(BigDecimal r14_SPECIFIC_PROV1) {
		R14_SPECIFIC_PROV1 = r14_SPECIFIC_PROV1;
	}










	public BigDecimal getR14_NO_OF_ACC1() {
		return R14_NO_OF_ACC1;
	}










	public void setR14_NO_OF_ACC1(BigDecimal r14_NO_OF_ACC1) {
		R14_NO_OF_ACC1 = r14_NO_OF_ACC1;
	}










	public BigDecimal getR14_90D_180D_PASTDUE() {
		return R14_90D_180D_PASTDUE;
	}










	public void setR14_90D_180D_PASTDUE(BigDecimal r14_90d_180d_PASTDUE) {
		R14_90D_180D_PASTDUE = r14_90d_180d_PASTDUE;
	}










	public BigDecimal getR14_NON_ACCRUALS2() {
		return R14_NON_ACCRUALS2;
	}










	public void setR14_NON_ACCRUALS2(BigDecimal r14_NON_ACCRUALS2) {
		R14_NON_ACCRUALS2 = r14_NON_ACCRUALS2;
	}










	public BigDecimal getR14_SPECIFIC_PROV2() {
		return R14_SPECIFIC_PROV2;
	}










	public void setR14_SPECIFIC_PROV2(BigDecimal r14_SPECIFIC_PROV2) {
		R14_SPECIFIC_PROV2 = r14_SPECIFIC_PROV2;
	}










	public BigDecimal getR14_NO_OF_ACC2() {
		return R14_NO_OF_ACC2;
	}










	public void setR14_NO_OF_ACC2(BigDecimal r14_NO_OF_ACC2) {
		R14_NO_OF_ACC2 = r14_NO_OF_ACC2;
	}










	public BigDecimal getR14_180D_ABOVE_PASTDUE() {
		return R14_180D_ABOVE_PASTDUE;
	}










	public void setR14_180D_ABOVE_PASTDUE(BigDecimal r14_180d_ABOVE_PASTDUE) {
		R14_180D_ABOVE_PASTDUE = r14_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR14_NON_ACCRUALS3() {
		return R14_NON_ACCRUALS3;
	}










	public void setR14_NON_ACCRUALS3(BigDecimal r14_NON_ACCRUALS3) {
		R14_NON_ACCRUALS3 = r14_NON_ACCRUALS3;
	}










	public BigDecimal getR14_SPECIFIC_PROV3() {
		return R14_SPECIFIC_PROV3;
	}










	public void setR14_SPECIFIC_PROV3(BigDecimal r14_SPECIFIC_PROV3) {
		R14_SPECIFIC_PROV3 = r14_SPECIFIC_PROV3;
	}










	public BigDecimal getR14_NO_OF_ACC3() {
		return R14_NO_OF_ACC3;
	}










	public void setR14_NO_OF_ACC3(BigDecimal r14_NO_OF_ACC3) {
		R14_NO_OF_ACC3 = r14_NO_OF_ACC3;
	}










	public BigDecimal getR14_TOTAL_NON_ACCRUAL() {
		return R14_TOTAL_NON_ACCRUAL;
	}










	public void setR14_TOTAL_NON_ACCRUAL(BigDecimal r14_TOTAL_NON_ACCRUAL) {
		R14_TOTAL_NON_ACCRUAL = r14_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR14_TOTAL_DUE_LOANS() {
		return R14_TOTAL_DUE_LOANS;
	}










	public void setR14_TOTAL_DUE_LOANS(BigDecimal r14_TOTAL_DUE_LOANS) {
		R14_TOTAL_DUE_LOANS = r14_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR14_TOTAL_PERFORMING_LOAN() {
		return R14_TOTAL_PERFORMING_LOAN;
	}










	public void setR14_TOTAL_PERFORMING_LOAN(BigDecimal r14_TOTAL_PERFORMING_LOAN) {
		R14_TOTAL_PERFORMING_LOAN = r14_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR14_VALUE_OF_COLLATERAL() {
		return R14_VALUE_OF_COLLATERAL;
	}










	public void setR14_VALUE_OF_COLLATERAL(BigDecimal r14_VALUE_OF_COLLATERAL) {
		R14_VALUE_OF_COLLATERAL = r14_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR14_TOTAL_VALUE_NPL() {
		return R14_TOTAL_VALUE_NPL;
	}










	public void setR14_TOTAL_VALUE_NPL(BigDecimal r14_TOTAL_VALUE_NPL) {
		R14_TOTAL_VALUE_NPL = r14_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR14_TOTAL_SPECIFIC_PROV() {
		return R14_TOTAL_SPECIFIC_PROV;
	}










	public void setR14_TOTAL_SPECIFIC_PROV(BigDecimal r14_TOTAL_SPECIFIC_PROV) {
		R14_TOTAL_SPECIFIC_PROV = r14_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR14_SPECIFIC_PROV_NPL() {
		return R14_SPECIFIC_PROV_NPL;
	}










	public void setR14_SPECIFIC_PROV_NPL(BigDecimal r14_SPECIFIC_PROV_NPL) {
		R14_SPECIFIC_PROV_NPL = r14_SPECIFIC_PROV_NPL;
	}










	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}










	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}










	public BigDecimal getR15_30D_90D_PASTDUE() {
		return R15_30D_90D_PASTDUE;
	}










	public void setR15_30D_90D_PASTDUE(BigDecimal r15_30d_90d_PASTDUE) {
		R15_30D_90D_PASTDUE = r15_30d_90d_PASTDUE;
	}










	public BigDecimal getR15_NON_PERFORM_LOANS() {
		return R15_NON_PERFORM_LOANS;
	}










	public void setR15_NON_PERFORM_LOANS(BigDecimal r15_NON_PERFORM_LOANS) {
		R15_NON_PERFORM_LOANS = r15_NON_PERFORM_LOANS;
	}










	public BigDecimal getR15_NON_ACCRUALS1() {
		return R15_NON_ACCRUALS1;
	}










	public void setR15_NON_ACCRUALS1(BigDecimal r15_NON_ACCRUALS1) {
		R15_NON_ACCRUALS1 = r15_NON_ACCRUALS1;
	}










	public BigDecimal getR15_SPECIFIC_PROV1() {
		return R15_SPECIFIC_PROV1;
	}










	public void setR15_SPECIFIC_PROV1(BigDecimal r15_SPECIFIC_PROV1) {
		R15_SPECIFIC_PROV1 = r15_SPECIFIC_PROV1;
	}










	public BigDecimal getR15_NO_OF_ACC1() {
		return R15_NO_OF_ACC1;
	}










	public void setR15_NO_OF_ACC1(BigDecimal r15_NO_OF_ACC1) {
		R15_NO_OF_ACC1 = r15_NO_OF_ACC1;
	}










	public BigDecimal getR15_90D_180D_PASTDUE() {
		return R15_90D_180D_PASTDUE;
	}










	public void setR15_90D_180D_PASTDUE(BigDecimal r15_90d_180d_PASTDUE) {
		R15_90D_180D_PASTDUE = r15_90d_180d_PASTDUE;
	}










	public BigDecimal getR15_NON_ACCRUALS2() {
		return R15_NON_ACCRUALS2;
	}










	public void setR15_NON_ACCRUALS2(BigDecimal r15_NON_ACCRUALS2) {
		R15_NON_ACCRUALS2 = r15_NON_ACCRUALS2;
	}










	public BigDecimal getR15_SPECIFIC_PROV2() {
		return R15_SPECIFIC_PROV2;
	}










	public void setR15_SPECIFIC_PROV2(BigDecimal r15_SPECIFIC_PROV2) {
		R15_SPECIFIC_PROV2 = r15_SPECIFIC_PROV2;
	}










	public BigDecimal getR15_NO_OF_ACC2() {
		return R15_NO_OF_ACC2;
	}










	public void setR15_NO_OF_ACC2(BigDecimal r15_NO_OF_ACC2) {
		R15_NO_OF_ACC2 = r15_NO_OF_ACC2;
	}










	public BigDecimal getR15_180D_ABOVE_PASTDUE() {
		return R15_180D_ABOVE_PASTDUE;
	}










	public void setR15_180D_ABOVE_PASTDUE(BigDecimal r15_180d_ABOVE_PASTDUE) {
		R15_180D_ABOVE_PASTDUE = r15_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR15_NON_ACCRUALS3() {
		return R15_NON_ACCRUALS3;
	}










	public void setR15_NON_ACCRUALS3(BigDecimal r15_NON_ACCRUALS3) {
		R15_NON_ACCRUALS3 = r15_NON_ACCRUALS3;
	}










	public BigDecimal getR15_SPECIFIC_PROV3() {
		return R15_SPECIFIC_PROV3;
	}










	public void setR15_SPECIFIC_PROV3(BigDecimal r15_SPECIFIC_PROV3) {
		R15_SPECIFIC_PROV3 = r15_SPECIFIC_PROV3;
	}










	public BigDecimal getR15_NO_OF_ACC3() {
		return R15_NO_OF_ACC3;
	}










	public void setR15_NO_OF_ACC3(BigDecimal r15_NO_OF_ACC3) {
		R15_NO_OF_ACC3 = r15_NO_OF_ACC3;
	}










	public BigDecimal getR15_TOTAL_NON_ACCRUAL() {
		return R15_TOTAL_NON_ACCRUAL;
	}










	public void setR15_TOTAL_NON_ACCRUAL(BigDecimal r15_TOTAL_NON_ACCRUAL) {
		R15_TOTAL_NON_ACCRUAL = r15_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR15_TOTAL_DUE_LOANS() {
		return R15_TOTAL_DUE_LOANS;
	}










	public void setR15_TOTAL_DUE_LOANS(BigDecimal r15_TOTAL_DUE_LOANS) {
		R15_TOTAL_DUE_LOANS = r15_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR15_TOTAL_PERFORMING_LOAN() {
		return R15_TOTAL_PERFORMING_LOAN;
	}










	public void setR15_TOTAL_PERFORMING_LOAN(BigDecimal r15_TOTAL_PERFORMING_LOAN) {
		R15_TOTAL_PERFORMING_LOAN = r15_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR15_VALUE_OF_COLLATERAL() {
		return R15_VALUE_OF_COLLATERAL;
	}










	public void setR15_VALUE_OF_COLLATERAL(BigDecimal r15_VALUE_OF_COLLATERAL) {
		R15_VALUE_OF_COLLATERAL = r15_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR15_TOTAL_VALUE_NPL() {
		return R15_TOTAL_VALUE_NPL;
	}










	public void setR15_TOTAL_VALUE_NPL(BigDecimal r15_TOTAL_VALUE_NPL) {
		R15_TOTAL_VALUE_NPL = r15_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR15_TOTAL_SPECIFIC_PROV() {
		return R15_TOTAL_SPECIFIC_PROV;
	}










	public void setR15_TOTAL_SPECIFIC_PROV(BigDecimal r15_TOTAL_SPECIFIC_PROV) {
		R15_TOTAL_SPECIFIC_PROV = r15_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR15_SPECIFIC_PROV_NPL() {
		return R15_SPECIFIC_PROV_NPL;
	}










	public void setR15_SPECIFIC_PROV_NPL(BigDecimal r15_SPECIFIC_PROV_NPL) {
		R15_SPECIFIC_PROV_NPL = r15_SPECIFIC_PROV_NPL;
	}










	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}










	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}










	public BigDecimal getR16_30D_90D_PASTDUE() {
		return R16_30D_90D_PASTDUE;
	}










	public void setR16_30D_90D_PASTDUE(BigDecimal r16_30d_90d_PASTDUE) {
		R16_30D_90D_PASTDUE = r16_30d_90d_PASTDUE;
	}










	public BigDecimal getR16_NON_PERFORM_LOANS() {
		return R16_NON_PERFORM_LOANS;
	}










	public void setR16_NON_PERFORM_LOANS(BigDecimal r16_NON_PERFORM_LOANS) {
		R16_NON_PERFORM_LOANS = r16_NON_PERFORM_LOANS;
	}










	public BigDecimal getR16_NON_ACCRUALS1() {
		return R16_NON_ACCRUALS1;
	}










	public void setR16_NON_ACCRUALS1(BigDecimal r16_NON_ACCRUALS1) {
		R16_NON_ACCRUALS1 = r16_NON_ACCRUALS1;
	}










	public BigDecimal getR16_SPECIFIC_PROV1() {
		return R16_SPECIFIC_PROV1;
	}










	public void setR16_SPECIFIC_PROV1(BigDecimal r16_SPECIFIC_PROV1) {
		R16_SPECIFIC_PROV1 = r16_SPECIFIC_PROV1;
	}










	public BigDecimal getR16_NO_OF_ACC1() {
		return R16_NO_OF_ACC1;
	}










	public void setR16_NO_OF_ACC1(BigDecimal r16_NO_OF_ACC1) {
		R16_NO_OF_ACC1 = r16_NO_OF_ACC1;
	}










	public BigDecimal getR16_90D_180D_PASTDUE() {
		return R16_90D_180D_PASTDUE;
	}










	public void setR16_90D_180D_PASTDUE(BigDecimal r16_90d_180d_PASTDUE) {
		R16_90D_180D_PASTDUE = r16_90d_180d_PASTDUE;
	}










	public BigDecimal getR16_NON_ACCRUALS2() {
		return R16_NON_ACCRUALS2;
	}










	public void setR16_NON_ACCRUALS2(BigDecimal r16_NON_ACCRUALS2) {
		R16_NON_ACCRUALS2 = r16_NON_ACCRUALS2;
	}










	public BigDecimal getR16_SPECIFIC_PROV2() {
		return R16_SPECIFIC_PROV2;
	}










	public void setR16_SPECIFIC_PROV2(BigDecimal r16_SPECIFIC_PROV2) {
		R16_SPECIFIC_PROV2 = r16_SPECIFIC_PROV2;
	}










	public BigDecimal getR16_NO_OF_ACC2() {
		return R16_NO_OF_ACC2;
	}










	public void setR16_NO_OF_ACC2(BigDecimal r16_NO_OF_ACC2) {
		R16_NO_OF_ACC2 = r16_NO_OF_ACC2;
	}










	public BigDecimal getR16_180D_ABOVE_PASTDUE() {
		return R16_180D_ABOVE_PASTDUE;
	}










	public void setR16_180D_ABOVE_PASTDUE(BigDecimal r16_180d_ABOVE_PASTDUE) {
		R16_180D_ABOVE_PASTDUE = r16_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR16_NON_ACCRUALS3() {
		return R16_NON_ACCRUALS3;
	}










	public void setR16_NON_ACCRUALS3(BigDecimal r16_NON_ACCRUALS3) {
		R16_NON_ACCRUALS3 = r16_NON_ACCRUALS3;
	}










	public BigDecimal getR16_SPECIFIC_PROV3() {
		return R16_SPECIFIC_PROV3;
	}










	public void setR16_SPECIFIC_PROV3(BigDecimal r16_SPECIFIC_PROV3) {
		R16_SPECIFIC_PROV3 = r16_SPECIFIC_PROV3;
	}










	public BigDecimal getR16_NO_OF_ACC3() {
		return R16_NO_OF_ACC3;
	}










	public void setR16_NO_OF_ACC3(BigDecimal r16_NO_OF_ACC3) {
		R16_NO_OF_ACC3 = r16_NO_OF_ACC3;
	}










	public BigDecimal getR16_TOTAL_NON_ACCRUAL() {
		return R16_TOTAL_NON_ACCRUAL;
	}










	public void setR16_TOTAL_NON_ACCRUAL(BigDecimal r16_TOTAL_NON_ACCRUAL) {
		R16_TOTAL_NON_ACCRUAL = r16_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR16_TOTAL_DUE_LOANS() {
		return R16_TOTAL_DUE_LOANS;
	}










	public void setR16_TOTAL_DUE_LOANS(BigDecimal r16_TOTAL_DUE_LOANS) {
		R16_TOTAL_DUE_LOANS = r16_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR16_TOTAL_PERFORMING_LOAN() {
		return R16_TOTAL_PERFORMING_LOAN;
	}










	public void setR16_TOTAL_PERFORMING_LOAN(BigDecimal r16_TOTAL_PERFORMING_LOAN) {
		R16_TOTAL_PERFORMING_LOAN = r16_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR16_VALUE_OF_COLLATERAL() {
		return R16_VALUE_OF_COLLATERAL;
	}










	public void setR16_VALUE_OF_COLLATERAL(BigDecimal r16_VALUE_OF_COLLATERAL) {
		R16_VALUE_OF_COLLATERAL = r16_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR16_TOTAL_VALUE_NPL() {
		return R16_TOTAL_VALUE_NPL;
	}










	public void setR16_TOTAL_VALUE_NPL(BigDecimal r16_TOTAL_VALUE_NPL) {
		R16_TOTAL_VALUE_NPL = r16_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR16_TOTAL_SPECIFIC_PROV() {
		return R16_TOTAL_SPECIFIC_PROV;
	}










	public void setR16_TOTAL_SPECIFIC_PROV(BigDecimal r16_TOTAL_SPECIFIC_PROV) {
		R16_TOTAL_SPECIFIC_PROV = r16_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR16_SPECIFIC_PROV_NPL() {
		return R16_SPECIFIC_PROV_NPL;
	}










	public void setR16_SPECIFIC_PROV_NPL(BigDecimal r16_SPECIFIC_PROV_NPL) {
		R16_SPECIFIC_PROV_NPL = r16_SPECIFIC_PROV_NPL;
	}










	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}










	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}










	public BigDecimal getR17_30D_90D_PASTDUE() {
		return R17_30D_90D_PASTDUE;
	}










	public void setR17_30D_90D_PASTDUE(BigDecimal r17_30d_90d_PASTDUE) {
		R17_30D_90D_PASTDUE = r17_30d_90d_PASTDUE;
	}










	public BigDecimal getR17_NON_PERFORM_LOANS() {
		return R17_NON_PERFORM_LOANS;
	}










	public void setR17_NON_PERFORM_LOANS(BigDecimal r17_NON_PERFORM_LOANS) {
		R17_NON_PERFORM_LOANS = r17_NON_PERFORM_LOANS;
	}










	public BigDecimal getR17_NON_ACCRUALS1() {
		return R17_NON_ACCRUALS1;
	}










	public void setR17_NON_ACCRUALS1(BigDecimal r17_NON_ACCRUALS1) {
		R17_NON_ACCRUALS1 = r17_NON_ACCRUALS1;
	}










	public BigDecimal getR17_SPECIFIC_PROV1() {
		return R17_SPECIFIC_PROV1;
	}










	public void setR17_SPECIFIC_PROV1(BigDecimal r17_SPECIFIC_PROV1) {
		R17_SPECIFIC_PROV1 = r17_SPECIFIC_PROV1;
	}










	public BigDecimal getR17_NO_OF_ACC1() {
		return R17_NO_OF_ACC1;
	}










	public void setR17_NO_OF_ACC1(BigDecimal r17_NO_OF_ACC1) {
		R17_NO_OF_ACC1 = r17_NO_OF_ACC1;
	}










	public BigDecimal getR17_90D_180D_PASTDUE() {
		return R17_90D_180D_PASTDUE;
	}










	public void setR17_90D_180D_PASTDUE(BigDecimal r17_90d_180d_PASTDUE) {
		R17_90D_180D_PASTDUE = r17_90d_180d_PASTDUE;
	}










	public BigDecimal getR17_NON_ACCRUALS2() {
		return R17_NON_ACCRUALS2;
	}










	public void setR17_NON_ACCRUALS2(BigDecimal r17_NON_ACCRUALS2) {
		R17_NON_ACCRUALS2 = r17_NON_ACCRUALS2;
	}










	public BigDecimal getR17_SPECIFIC_PROV2() {
		return R17_SPECIFIC_PROV2;
	}










	public void setR17_SPECIFIC_PROV2(BigDecimal r17_SPECIFIC_PROV2) {
		R17_SPECIFIC_PROV2 = r17_SPECIFIC_PROV2;
	}










	public BigDecimal getR17_NO_OF_ACC2() {
		return R17_NO_OF_ACC2;
	}










	public void setR17_NO_OF_ACC2(BigDecimal r17_NO_OF_ACC2) {
		R17_NO_OF_ACC2 = r17_NO_OF_ACC2;
	}










	public BigDecimal getR17_180D_ABOVE_PASTDUE() {
		return R17_180D_ABOVE_PASTDUE;
	}










	public void setR17_180D_ABOVE_PASTDUE(BigDecimal r17_180d_ABOVE_PASTDUE) {
		R17_180D_ABOVE_PASTDUE = r17_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR17_NON_ACCRUALS3() {
		return R17_NON_ACCRUALS3;
	}










	public void setR17_NON_ACCRUALS3(BigDecimal r17_NON_ACCRUALS3) {
		R17_NON_ACCRUALS3 = r17_NON_ACCRUALS3;
	}










	public BigDecimal getR17_SPECIFIC_PROV3() {
		return R17_SPECIFIC_PROV3;
	}










	public void setR17_SPECIFIC_PROV3(BigDecimal r17_SPECIFIC_PROV3) {
		R17_SPECIFIC_PROV3 = r17_SPECIFIC_PROV3;
	}










	public BigDecimal getR17_NO_OF_ACC3() {
		return R17_NO_OF_ACC3;
	}










	public void setR17_NO_OF_ACC3(BigDecimal r17_NO_OF_ACC3) {
		R17_NO_OF_ACC3 = r17_NO_OF_ACC3;
	}










	public BigDecimal getR17_TOTAL_NON_ACCRUAL() {
		return R17_TOTAL_NON_ACCRUAL;
	}










	public void setR17_TOTAL_NON_ACCRUAL(BigDecimal r17_TOTAL_NON_ACCRUAL) {
		R17_TOTAL_NON_ACCRUAL = r17_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR17_TOTAL_DUE_LOANS() {
		return R17_TOTAL_DUE_LOANS;
	}










	public void setR17_TOTAL_DUE_LOANS(BigDecimal r17_TOTAL_DUE_LOANS) {
		R17_TOTAL_DUE_LOANS = r17_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR17_TOTAL_PERFORMING_LOAN() {
		return R17_TOTAL_PERFORMING_LOAN;
	}










	public void setR17_TOTAL_PERFORMING_LOAN(BigDecimal r17_TOTAL_PERFORMING_LOAN) {
		R17_TOTAL_PERFORMING_LOAN = r17_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR17_VALUE_OF_COLLATERAL() {
		return R17_VALUE_OF_COLLATERAL;
	}










	public void setR17_VALUE_OF_COLLATERAL(BigDecimal r17_VALUE_OF_COLLATERAL) {
		R17_VALUE_OF_COLLATERAL = r17_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR17_TOTAL_VALUE_NPL() {
		return R17_TOTAL_VALUE_NPL;
	}










	public void setR17_TOTAL_VALUE_NPL(BigDecimal r17_TOTAL_VALUE_NPL) {
		R17_TOTAL_VALUE_NPL = r17_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR17_TOTAL_SPECIFIC_PROV() {
		return R17_TOTAL_SPECIFIC_PROV;
	}










	public void setR17_TOTAL_SPECIFIC_PROV(BigDecimal r17_TOTAL_SPECIFIC_PROV) {
		R17_TOTAL_SPECIFIC_PROV = r17_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR17_SPECIFIC_PROV_NPL() {
		return R17_SPECIFIC_PROV_NPL;
	}










	public void setR17_SPECIFIC_PROV_NPL(BigDecimal r17_SPECIFIC_PROV_NPL) {
		R17_SPECIFIC_PROV_NPL = r17_SPECIFIC_PROV_NPL;
	}










	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}










	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}










	public BigDecimal getR18_30D_90D_PASTDUE() {
		return R18_30D_90D_PASTDUE;
	}










	public void setR18_30D_90D_PASTDUE(BigDecimal r18_30d_90d_PASTDUE) {
		R18_30D_90D_PASTDUE = r18_30d_90d_PASTDUE;
	}










	public BigDecimal getR18_NON_PERFORM_LOANS() {
		return R18_NON_PERFORM_LOANS;
	}










	public void setR18_NON_PERFORM_LOANS(BigDecimal r18_NON_PERFORM_LOANS) {
		R18_NON_PERFORM_LOANS = r18_NON_PERFORM_LOANS;
	}










	public BigDecimal getR18_NON_ACCRUALS1() {
		return R18_NON_ACCRUALS1;
	}










	public void setR18_NON_ACCRUALS1(BigDecimal r18_NON_ACCRUALS1) {
		R18_NON_ACCRUALS1 = r18_NON_ACCRUALS1;
	}










	public BigDecimal getR18_SPECIFIC_PROV1() {
		return R18_SPECIFIC_PROV1;
	}










	public void setR18_SPECIFIC_PROV1(BigDecimal r18_SPECIFIC_PROV1) {
		R18_SPECIFIC_PROV1 = r18_SPECIFIC_PROV1;
	}










	public BigDecimal getR18_NO_OF_ACC1() {
		return R18_NO_OF_ACC1;
	}










	public void setR18_NO_OF_ACC1(BigDecimal r18_NO_OF_ACC1) {
		R18_NO_OF_ACC1 = r18_NO_OF_ACC1;
	}










	public BigDecimal getR18_90D_180D_PASTDUE() {
		return R18_90D_180D_PASTDUE;
	}










	public void setR18_90D_180D_PASTDUE(BigDecimal r18_90d_180d_PASTDUE) {
		R18_90D_180D_PASTDUE = r18_90d_180d_PASTDUE;
	}










	public BigDecimal getR18_NON_ACCRUALS2() {
		return R18_NON_ACCRUALS2;
	}










	public void setR18_NON_ACCRUALS2(BigDecimal r18_NON_ACCRUALS2) {
		R18_NON_ACCRUALS2 = r18_NON_ACCRUALS2;
	}










	public BigDecimal getR18_SPECIFIC_PROV2() {
		return R18_SPECIFIC_PROV2;
	}










	public void setR18_SPECIFIC_PROV2(BigDecimal r18_SPECIFIC_PROV2) {
		R18_SPECIFIC_PROV2 = r18_SPECIFIC_PROV2;
	}










	public BigDecimal getR18_NO_OF_ACC2() {
		return R18_NO_OF_ACC2;
	}










	public void setR18_NO_OF_ACC2(BigDecimal r18_NO_OF_ACC2) {
		R18_NO_OF_ACC2 = r18_NO_OF_ACC2;
	}










	public BigDecimal getR18_180D_ABOVE_PASTDUE() {
		return R18_180D_ABOVE_PASTDUE;
	}










	public void setR18_180D_ABOVE_PASTDUE(BigDecimal r18_180d_ABOVE_PASTDUE) {
		R18_180D_ABOVE_PASTDUE = r18_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR18_NON_ACCRUALS3() {
		return R18_NON_ACCRUALS3;
	}










	public void setR18_NON_ACCRUALS3(BigDecimal r18_NON_ACCRUALS3) {
		R18_NON_ACCRUALS3 = r18_NON_ACCRUALS3;
	}










	public BigDecimal getR18_SPECIFIC_PROV3() {
		return R18_SPECIFIC_PROV3;
	}










	public void setR18_SPECIFIC_PROV3(BigDecimal r18_SPECIFIC_PROV3) {
		R18_SPECIFIC_PROV3 = r18_SPECIFIC_PROV3;
	}










	public BigDecimal getR18_NO_OF_ACC3() {
		return R18_NO_OF_ACC3;
	}










	public void setR18_NO_OF_ACC3(BigDecimal r18_NO_OF_ACC3) {
		R18_NO_OF_ACC3 = r18_NO_OF_ACC3;
	}










	public BigDecimal getR18_TOTAL_NON_ACCRUAL() {
		return R18_TOTAL_NON_ACCRUAL;
	}










	public void setR18_TOTAL_NON_ACCRUAL(BigDecimal r18_TOTAL_NON_ACCRUAL) {
		R18_TOTAL_NON_ACCRUAL = r18_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR18_TOTAL_DUE_LOANS() {
		return R18_TOTAL_DUE_LOANS;
	}










	public void setR18_TOTAL_DUE_LOANS(BigDecimal r18_TOTAL_DUE_LOANS) {
		R18_TOTAL_DUE_LOANS = r18_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR18_TOTAL_PERFORMING_LOAN() {
		return R18_TOTAL_PERFORMING_LOAN;
	}










	public void setR18_TOTAL_PERFORMING_LOAN(BigDecimal r18_TOTAL_PERFORMING_LOAN) {
		R18_TOTAL_PERFORMING_LOAN = r18_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR18_VALUE_OF_COLLATERAL() {
		return R18_VALUE_OF_COLLATERAL;
	}










	public void setR18_VALUE_OF_COLLATERAL(BigDecimal r18_VALUE_OF_COLLATERAL) {
		R18_VALUE_OF_COLLATERAL = r18_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR18_TOTAL_VALUE_NPL() {
		return R18_TOTAL_VALUE_NPL;
	}










	public void setR18_TOTAL_VALUE_NPL(BigDecimal r18_TOTAL_VALUE_NPL) {
		R18_TOTAL_VALUE_NPL = r18_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR18_TOTAL_SPECIFIC_PROV() {
		return R18_TOTAL_SPECIFIC_PROV;
	}










	public void setR18_TOTAL_SPECIFIC_PROV(BigDecimal r18_TOTAL_SPECIFIC_PROV) {
		R18_TOTAL_SPECIFIC_PROV = r18_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR18_SPECIFIC_PROV_NPL() {
		return R18_SPECIFIC_PROV_NPL;
	}










	public void setR18_SPECIFIC_PROV_NPL(BigDecimal r18_SPECIFIC_PROV_NPL) {
		R18_SPECIFIC_PROV_NPL = r18_SPECIFIC_PROV_NPL;
	}










	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}










	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}










	public BigDecimal getR19_30D_90D_PASTDUE() {
		return R19_30D_90D_PASTDUE;
	}










	public void setR19_30D_90D_PASTDUE(BigDecimal r19_30d_90d_PASTDUE) {
		R19_30D_90D_PASTDUE = r19_30d_90d_PASTDUE;
	}










	public BigDecimal getR19_NON_PERFORM_LOANS() {
		return R19_NON_PERFORM_LOANS;
	}










	public void setR19_NON_PERFORM_LOANS(BigDecimal r19_NON_PERFORM_LOANS) {
		R19_NON_PERFORM_LOANS = r19_NON_PERFORM_LOANS;
	}










	public BigDecimal getR19_NON_ACCRUALS1() {
		return R19_NON_ACCRUALS1;
	}










	public void setR19_NON_ACCRUALS1(BigDecimal r19_NON_ACCRUALS1) {
		R19_NON_ACCRUALS1 = r19_NON_ACCRUALS1;
	}










	public BigDecimal getR19_SPECIFIC_PROV1() {
		return R19_SPECIFIC_PROV1;
	}










	public void setR19_SPECIFIC_PROV1(BigDecimal r19_SPECIFIC_PROV1) {
		R19_SPECIFIC_PROV1 = r19_SPECIFIC_PROV1;
	}










	public BigDecimal getR19_NO_OF_ACC1() {
		return R19_NO_OF_ACC1;
	}










	public void setR19_NO_OF_ACC1(BigDecimal r19_NO_OF_ACC1) {
		R19_NO_OF_ACC1 = r19_NO_OF_ACC1;
	}










	public BigDecimal getR19_90D_180D_PASTDUE() {
		return R19_90D_180D_PASTDUE;
	}










	public void setR19_90D_180D_PASTDUE(BigDecimal r19_90d_180d_PASTDUE) {
		R19_90D_180D_PASTDUE = r19_90d_180d_PASTDUE;
	}










	public BigDecimal getR19_NON_ACCRUALS2() {
		return R19_NON_ACCRUALS2;
	}










	public void setR19_NON_ACCRUALS2(BigDecimal r19_NON_ACCRUALS2) {
		R19_NON_ACCRUALS2 = r19_NON_ACCRUALS2;
	}










	public BigDecimal getR19_SPECIFIC_PROV2() {
		return R19_SPECIFIC_PROV2;
	}










	public void setR19_SPECIFIC_PROV2(BigDecimal r19_SPECIFIC_PROV2) {
		R19_SPECIFIC_PROV2 = r19_SPECIFIC_PROV2;
	}










	public BigDecimal getR19_NO_OF_ACC2() {
		return R19_NO_OF_ACC2;
	}










	public void setR19_NO_OF_ACC2(BigDecimal r19_NO_OF_ACC2) {
		R19_NO_OF_ACC2 = r19_NO_OF_ACC2;
	}










	public BigDecimal getR19_180D_ABOVE_PASTDUE() {
		return R19_180D_ABOVE_PASTDUE;
	}










	public void setR19_180D_ABOVE_PASTDUE(BigDecimal r19_180d_ABOVE_PASTDUE) {
		R19_180D_ABOVE_PASTDUE = r19_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR19_NON_ACCRUALS3() {
		return R19_NON_ACCRUALS3;
	}










	public void setR19_NON_ACCRUALS3(BigDecimal r19_NON_ACCRUALS3) {
		R19_NON_ACCRUALS3 = r19_NON_ACCRUALS3;
	}










	public BigDecimal getR19_SPECIFIC_PROV3() {
		return R19_SPECIFIC_PROV3;
	}










	public void setR19_SPECIFIC_PROV3(BigDecimal r19_SPECIFIC_PROV3) {
		R19_SPECIFIC_PROV3 = r19_SPECIFIC_PROV3;
	}










	public BigDecimal getR19_NO_OF_ACC3() {
		return R19_NO_OF_ACC3;
	}










	public void setR19_NO_OF_ACC3(BigDecimal r19_NO_OF_ACC3) {
		R19_NO_OF_ACC3 = r19_NO_OF_ACC3;
	}










	public BigDecimal getR19_TOTAL_NON_ACCRUAL() {
		return R19_TOTAL_NON_ACCRUAL;
	}










	public void setR19_TOTAL_NON_ACCRUAL(BigDecimal r19_TOTAL_NON_ACCRUAL) {
		R19_TOTAL_NON_ACCRUAL = r19_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR19_TOTAL_DUE_LOANS() {
		return R19_TOTAL_DUE_LOANS;
	}










	public void setR19_TOTAL_DUE_LOANS(BigDecimal r19_TOTAL_DUE_LOANS) {
		R19_TOTAL_DUE_LOANS = r19_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR19_TOTAL_PERFORMING_LOAN() {
		return R19_TOTAL_PERFORMING_LOAN;
	}










	public void setR19_TOTAL_PERFORMING_LOAN(BigDecimal r19_TOTAL_PERFORMING_LOAN) {
		R19_TOTAL_PERFORMING_LOAN = r19_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR19_VALUE_OF_COLLATERAL() {
		return R19_VALUE_OF_COLLATERAL;
	}










	public void setR19_VALUE_OF_COLLATERAL(BigDecimal r19_VALUE_OF_COLLATERAL) {
		R19_VALUE_OF_COLLATERAL = r19_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR19_TOTAL_VALUE_NPL() {
		return R19_TOTAL_VALUE_NPL;
	}










	public void setR19_TOTAL_VALUE_NPL(BigDecimal r19_TOTAL_VALUE_NPL) {
		R19_TOTAL_VALUE_NPL = r19_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR19_TOTAL_SPECIFIC_PROV() {
		return R19_TOTAL_SPECIFIC_PROV;
	}










	public void setR19_TOTAL_SPECIFIC_PROV(BigDecimal r19_TOTAL_SPECIFIC_PROV) {
		R19_TOTAL_SPECIFIC_PROV = r19_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR19_SPECIFIC_PROV_NPL() {
		return R19_SPECIFIC_PROV_NPL;
	}










	public void setR19_SPECIFIC_PROV_NPL(BigDecimal r19_SPECIFIC_PROV_NPL) {
		R19_SPECIFIC_PROV_NPL = r19_SPECIFIC_PROV_NPL;
	}










	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}










	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}










	public BigDecimal getR20_30D_90D_PASTDUE() {
		return R20_30D_90D_PASTDUE;
	}










	public void setR20_30D_90D_PASTDUE(BigDecimal r20_30d_90d_PASTDUE) {
		R20_30D_90D_PASTDUE = r20_30d_90d_PASTDUE;
	}










	public BigDecimal getR20_NON_PERFORM_LOANS() {
		return R20_NON_PERFORM_LOANS;
	}










	public void setR20_NON_PERFORM_LOANS(BigDecimal r20_NON_PERFORM_LOANS) {
		R20_NON_PERFORM_LOANS = r20_NON_PERFORM_LOANS;
	}










	public BigDecimal getR20_NON_ACCRUALS1() {
		return R20_NON_ACCRUALS1;
	}










	public void setR20_NON_ACCRUALS1(BigDecimal r20_NON_ACCRUALS1) {
		R20_NON_ACCRUALS1 = r20_NON_ACCRUALS1;
	}










	public BigDecimal getR20_SPECIFIC_PROV1() {
		return R20_SPECIFIC_PROV1;
	}










	public void setR20_SPECIFIC_PROV1(BigDecimal r20_SPECIFIC_PROV1) {
		R20_SPECIFIC_PROV1 = r20_SPECIFIC_PROV1;
	}










	public BigDecimal getR20_NO_OF_ACC1() {
		return R20_NO_OF_ACC1;
	}










	public void setR20_NO_OF_ACC1(BigDecimal r20_NO_OF_ACC1) {
		R20_NO_OF_ACC1 = r20_NO_OF_ACC1;
	}










	public BigDecimal getR20_90D_180D_PASTDUE() {
		return R20_90D_180D_PASTDUE;
	}










	public void setR20_90D_180D_PASTDUE(BigDecimal r20_90d_180d_PASTDUE) {
		R20_90D_180D_PASTDUE = r20_90d_180d_PASTDUE;
	}










	public BigDecimal getR20_NON_ACCRUALS2() {
		return R20_NON_ACCRUALS2;
	}










	public void setR20_NON_ACCRUALS2(BigDecimal r20_NON_ACCRUALS2) {
		R20_NON_ACCRUALS2 = r20_NON_ACCRUALS2;
	}










	public BigDecimal getR20_SPECIFIC_PROV2() {
		return R20_SPECIFIC_PROV2;
	}










	public void setR20_SPECIFIC_PROV2(BigDecimal r20_SPECIFIC_PROV2) {
		R20_SPECIFIC_PROV2 = r20_SPECIFIC_PROV2;
	}










	public BigDecimal getR20_NO_OF_ACC2() {
		return R20_NO_OF_ACC2;
	}










	public void setR20_NO_OF_ACC2(BigDecimal r20_NO_OF_ACC2) {
		R20_NO_OF_ACC2 = r20_NO_OF_ACC2;
	}










	public BigDecimal getR20_180D_ABOVE_PASTDUE() {
		return R20_180D_ABOVE_PASTDUE;
	}










	public void setR20_180D_ABOVE_PASTDUE(BigDecimal r20_180d_ABOVE_PASTDUE) {
		R20_180D_ABOVE_PASTDUE = r20_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR20_NON_ACCRUALS3() {
		return R20_NON_ACCRUALS3;
	}










	public void setR20_NON_ACCRUALS3(BigDecimal r20_NON_ACCRUALS3) {
		R20_NON_ACCRUALS3 = r20_NON_ACCRUALS3;
	}










	public BigDecimal getR20_SPECIFIC_PROV3() {
		return R20_SPECIFIC_PROV3;
	}










	public void setR20_SPECIFIC_PROV3(BigDecimal r20_SPECIFIC_PROV3) {
		R20_SPECIFIC_PROV3 = r20_SPECIFIC_PROV3;
	}










	public BigDecimal getR20_NO_OF_ACC3() {
		return R20_NO_OF_ACC3;
	}










	public void setR20_NO_OF_ACC3(BigDecimal r20_NO_OF_ACC3) {
		R20_NO_OF_ACC3 = r20_NO_OF_ACC3;
	}










	public BigDecimal getR20_TOTAL_NON_ACCRUAL() {
		return R20_TOTAL_NON_ACCRUAL;
	}










	public void setR20_TOTAL_NON_ACCRUAL(BigDecimal r20_TOTAL_NON_ACCRUAL) {
		R20_TOTAL_NON_ACCRUAL = r20_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR20_TOTAL_DUE_LOANS() {
		return R20_TOTAL_DUE_LOANS;
	}










	public void setR20_TOTAL_DUE_LOANS(BigDecimal r20_TOTAL_DUE_LOANS) {
		R20_TOTAL_DUE_LOANS = r20_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR20_TOTAL_PERFORMING_LOAN() {
		return R20_TOTAL_PERFORMING_LOAN;
	}










	public void setR20_TOTAL_PERFORMING_LOAN(BigDecimal r20_TOTAL_PERFORMING_LOAN) {
		R20_TOTAL_PERFORMING_LOAN = r20_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR20_VALUE_OF_COLLATERAL() {
		return R20_VALUE_OF_COLLATERAL;
	}










	public void setR20_VALUE_OF_COLLATERAL(BigDecimal r20_VALUE_OF_COLLATERAL) {
		R20_VALUE_OF_COLLATERAL = r20_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR20_TOTAL_VALUE_NPL() {
		return R20_TOTAL_VALUE_NPL;
	}










	public void setR20_TOTAL_VALUE_NPL(BigDecimal r20_TOTAL_VALUE_NPL) {
		R20_TOTAL_VALUE_NPL = r20_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR20_TOTAL_SPECIFIC_PROV() {
		return R20_TOTAL_SPECIFIC_PROV;
	}










	public void setR20_TOTAL_SPECIFIC_PROV(BigDecimal r20_TOTAL_SPECIFIC_PROV) {
		R20_TOTAL_SPECIFIC_PROV = r20_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR20_SPECIFIC_PROV_NPL() {
		return R20_SPECIFIC_PROV_NPL;
	}










	public void setR20_SPECIFIC_PROV_NPL(BigDecimal r20_SPECIFIC_PROV_NPL) {
		R20_SPECIFIC_PROV_NPL = r20_SPECIFIC_PROV_NPL;
	}










	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}










	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}










	public BigDecimal getR21_30D_90D_PASTDUE() {
		return R21_30D_90D_PASTDUE;
	}










	public void setR21_30D_90D_PASTDUE(BigDecimal r21_30d_90d_PASTDUE) {
		R21_30D_90D_PASTDUE = r21_30d_90d_PASTDUE;
	}










	public BigDecimal getR21_NON_PERFORM_LOANS() {
		return R21_NON_PERFORM_LOANS;
	}










	public void setR21_NON_PERFORM_LOANS(BigDecimal r21_NON_PERFORM_LOANS) {
		R21_NON_PERFORM_LOANS = r21_NON_PERFORM_LOANS;
	}










	public BigDecimal getR21_NON_ACCRUALS1() {
		return R21_NON_ACCRUALS1;
	}










	public void setR21_NON_ACCRUALS1(BigDecimal r21_NON_ACCRUALS1) {
		R21_NON_ACCRUALS1 = r21_NON_ACCRUALS1;
	}










	public BigDecimal getR21_SPECIFIC_PROV1() {
		return R21_SPECIFIC_PROV1;
	}










	public void setR21_SPECIFIC_PROV1(BigDecimal r21_SPECIFIC_PROV1) {
		R21_SPECIFIC_PROV1 = r21_SPECIFIC_PROV1;
	}










	public BigDecimal getR21_NO_OF_ACC1() {
		return R21_NO_OF_ACC1;
	}










	public void setR21_NO_OF_ACC1(BigDecimal r21_NO_OF_ACC1) {
		R21_NO_OF_ACC1 = r21_NO_OF_ACC1;
	}










	public BigDecimal getR21_90D_180D_PASTDUE() {
		return R21_90D_180D_PASTDUE;
	}










	public void setR21_90D_180D_PASTDUE(BigDecimal r21_90d_180d_PASTDUE) {
		R21_90D_180D_PASTDUE = r21_90d_180d_PASTDUE;
	}










	public BigDecimal getR21_NON_ACCRUALS2() {
		return R21_NON_ACCRUALS2;
	}










	public void setR21_NON_ACCRUALS2(BigDecimal r21_NON_ACCRUALS2) {
		R21_NON_ACCRUALS2 = r21_NON_ACCRUALS2;
	}










	public BigDecimal getR21_SPECIFIC_PROV2() {
		return R21_SPECIFIC_PROV2;
	}










	public void setR21_SPECIFIC_PROV2(BigDecimal r21_SPECIFIC_PROV2) {
		R21_SPECIFIC_PROV2 = r21_SPECIFIC_PROV2;
	}










	public BigDecimal getR21_NO_OF_ACC2() {
		return R21_NO_OF_ACC2;
	}










	public void setR21_NO_OF_ACC2(BigDecimal r21_NO_OF_ACC2) {
		R21_NO_OF_ACC2 = r21_NO_OF_ACC2;
	}










	public BigDecimal getR21_180D_ABOVE_PASTDUE() {
		return R21_180D_ABOVE_PASTDUE;
	}










	public void setR21_180D_ABOVE_PASTDUE(BigDecimal r21_180d_ABOVE_PASTDUE) {
		R21_180D_ABOVE_PASTDUE = r21_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR21_NON_ACCRUALS3() {
		return R21_NON_ACCRUALS3;
	}










	public void setR21_NON_ACCRUALS3(BigDecimal r21_NON_ACCRUALS3) {
		R21_NON_ACCRUALS3 = r21_NON_ACCRUALS3;
	}










	public BigDecimal getR21_SPECIFIC_PROV3() {
		return R21_SPECIFIC_PROV3;
	}










	public void setR21_SPECIFIC_PROV3(BigDecimal r21_SPECIFIC_PROV3) {
		R21_SPECIFIC_PROV3 = r21_SPECIFIC_PROV3;
	}










	public BigDecimal getR21_NO_OF_ACC3() {
		return R21_NO_OF_ACC3;
	}










	public void setR21_NO_OF_ACC3(BigDecimal r21_NO_OF_ACC3) {
		R21_NO_OF_ACC3 = r21_NO_OF_ACC3;
	}










	public BigDecimal getR21_TOTAL_NON_ACCRUAL() {
		return R21_TOTAL_NON_ACCRUAL;
	}










	public void setR21_TOTAL_NON_ACCRUAL(BigDecimal r21_TOTAL_NON_ACCRUAL) {
		R21_TOTAL_NON_ACCRUAL = r21_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR21_TOTAL_DUE_LOANS() {
		return R21_TOTAL_DUE_LOANS;
	}










	public void setR21_TOTAL_DUE_LOANS(BigDecimal r21_TOTAL_DUE_LOANS) {
		R21_TOTAL_DUE_LOANS = r21_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR21_TOTAL_PERFORMING_LOAN() {
		return R21_TOTAL_PERFORMING_LOAN;
	}










	public void setR21_TOTAL_PERFORMING_LOAN(BigDecimal r21_TOTAL_PERFORMING_LOAN) {
		R21_TOTAL_PERFORMING_LOAN = r21_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR21_VALUE_OF_COLLATERAL() {
		return R21_VALUE_OF_COLLATERAL;
	}










	public void setR21_VALUE_OF_COLLATERAL(BigDecimal r21_VALUE_OF_COLLATERAL) {
		R21_VALUE_OF_COLLATERAL = r21_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR21_TOTAL_VALUE_NPL() {
		return R21_TOTAL_VALUE_NPL;
	}










	public void setR21_TOTAL_VALUE_NPL(BigDecimal r21_TOTAL_VALUE_NPL) {
		R21_TOTAL_VALUE_NPL = r21_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR21_TOTAL_SPECIFIC_PROV() {
		return R21_TOTAL_SPECIFIC_PROV;
	}










	public void setR21_TOTAL_SPECIFIC_PROV(BigDecimal r21_TOTAL_SPECIFIC_PROV) {
		R21_TOTAL_SPECIFIC_PROV = r21_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR21_SPECIFIC_PROV_NPL() {
		return R21_SPECIFIC_PROV_NPL;
	}










	public void setR21_SPECIFIC_PROV_NPL(BigDecimal r21_SPECIFIC_PROV_NPL) {
		R21_SPECIFIC_PROV_NPL = r21_SPECIFIC_PROV_NPL;
	}










	public String getR22_PRODUCT() {
		return R22_PRODUCT;
	}










	public void setR22_PRODUCT(String r22_PRODUCT) {
		R22_PRODUCT = r22_PRODUCT;
	}










	public BigDecimal getR22_30D_90D_PASTDUE() {
		return R22_30D_90D_PASTDUE;
	}










	public void setR22_30D_90D_PASTDUE(BigDecimal r22_30d_90d_PASTDUE) {
		R22_30D_90D_PASTDUE = r22_30d_90d_PASTDUE;
	}










	public BigDecimal getR22_NON_PERFORM_LOANS() {
		return R22_NON_PERFORM_LOANS;
	}










	public void setR22_NON_PERFORM_LOANS(BigDecimal r22_NON_PERFORM_LOANS) {
		R22_NON_PERFORM_LOANS = r22_NON_PERFORM_LOANS;
	}










	public BigDecimal getR22_NON_ACCRUALS1() {
		return R22_NON_ACCRUALS1;
	}










	public void setR22_NON_ACCRUALS1(BigDecimal r22_NON_ACCRUALS1) {
		R22_NON_ACCRUALS1 = r22_NON_ACCRUALS1;
	}










	public BigDecimal getR22_SPECIFIC_PROV1() {
		return R22_SPECIFIC_PROV1;
	}










	public void setR22_SPECIFIC_PROV1(BigDecimal r22_SPECIFIC_PROV1) {
		R22_SPECIFIC_PROV1 = r22_SPECIFIC_PROV1;
	}










	public BigDecimal getR22_NO_OF_ACC1() {
		return R22_NO_OF_ACC1;
	}










	public void setR22_NO_OF_ACC1(BigDecimal r22_NO_OF_ACC1) {
		R22_NO_OF_ACC1 = r22_NO_OF_ACC1;
	}










	public BigDecimal getR22_90D_180D_PASTDUE() {
		return R22_90D_180D_PASTDUE;
	}










	public void setR22_90D_180D_PASTDUE(BigDecimal r22_90d_180d_PASTDUE) {
		R22_90D_180D_PASTDUE = r22_90d_180d_PASTDUE;
	}










	public BigDecimal getR22_NON_ACCRUALS2() {
		return R22_NON_ACCRUALS2;
	}










	public void setR22_NON_ACCRUALS2(BigDecimal r22_NON_ACCRUALS2) {
		R22_NON_ACCRUALS2 = r22_NON_ACCRUALS2;
	}










	public BigDecimal getR22_SPECIFIC_PROV2() {
		return R22_SPECIFIC_PROV2;
	}










	public void setR22_SPECIFIC_PROV2(BigDecimal r22_SPECIFIC_PROV2) {
		R22_SPECIFIC_PROV2 = r22_SPECIFIC_PROV2;
	}










	public BigDecimal getR22_NO_OF_ACC2() {
		return R22_NO_OF_ACC2;
	}










	public void setR22_NO_OF_ACC2(BigDecimal r22_NO_OF_ACC2) {
		R22_NO_OF_ACC2 = r22_NO_OF_ACC2;
	}










	public BigDecimal getR22_180D_ABOVE_PASTDUE() {
		return R22_180D_ABOVE_PASTDUE;
	}










	public void setR22_180D_ABOVE_PASTDUE(BigDecimal r22_180d_ABOVE_PASTDUE) {
		R22_180D_ABOVE_PASTDUE = r22_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR22_NON_ACCRUALS3() {
		return R22_NON_ACCRUALS3;
	}










	public void setR22_NON_ACCRUALS3(BigDecimal r22_NON_ACCRUALS3) {
		R22_NON_ACCRUALS3 = r22_NON_ACCRUALS3;
	}










	public BigDecimal getR22_SPECIFIC_PROV3() {
		return R22_SPECIFIC_PROV3;
	}










	public void setR22_SPECIFIC_PROV3(BigDecimal r22_SPECIFIC_PROV3) {
		R22_SPECIFIC_PROV3 = r22_SPECIFIC_PROV3;
	}










	public BigDecimal getR22_NO_OF_ACC3() {
		return R22_NO_OF_ACC3;
	}










	public void setR22_NO_OF_ACC3(BigDecimal r22_NO_OF_ACC3) {
		R22_NO_OF_ACC3 = r22_NO_OF_ACC3;
	}










	public BigDecimal getR22_TOTAL_NON_ACCRUAL() {
		return R22_TOTAL_NON_ACCRUAL;
	}










	public void setR22_TOTAL_NON_ACCRUAL(BigDecimal r22_TOTAL_NON_ACCRUAL) {
		R22_TOTAL_NON_ACCRUAL = r22_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR22_TOTAL_DUE_LOANS() {
		return R22_TOTAL_DUE_LOANS;
	}










	public void setR22_TOTAL_DUE_LOANS(BigDecimal r22_TOTAL_DUE_LOANS) {
		R22_TOTAL_DUE_LOANS = r22_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR22_TOTAL_PERFORMING_LOAN() {
		return R22_TOTAL_PERFORMING_LOAN;
	}










	public void setR22_TOTAL_PERFORMING_LOAN(BigDecimal r22_TOTAL_PERFORMING_LOAN) {
		R22_TOTAL_PERFORMING_LOAN = r22_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR22_VALUE_OF_COLLATERAL() {
		return R22_VALUE_OF_COLLATERAL;
	}










	public void setR22_VALUE_OF_COLLATERAL(BigDecimal r22_VALUE_OF_COLLATERAL) {
		R22_VALUE_OF_COLLATERAL = r22_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR22_TOTAL_VALUE_NPL() {
		return R22_TOTAL_VALUE_NPL;
	}










	public void setR22_TOTAL_VALUE_NPL(BigDecimal r22_TOTAL_VALUE_NPL) {
		R22_TOTAL_VALUE_NPL = r22_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR22_TOTAL_SPECIFIC_PROV() {
		return R22_TOTAL_SPECIFIC_PROV;
	}










	public void setR22_TOTAL_SPECIFIC_PROV(BigDecimal r22_TOTAL_SPECIFIC_PROV) {
		R22_TOTAL_SPECIFIC_PROV = r22_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR22_SPECIFIC_PROV_NPL() {
		return R22_SPECIFIC_PROV_NPL;
	}










	public void setR22_SPECIFIC_PROV_NPL(BigDecimal r22_SPECIFIC_PROV_NPL) {
		R22_SPECIFIC_PROV_NPL = r22_SPECIFIC_PROV_NPL;
	}










	public String getR23_PRODUCT() {
		return R23_PRODUCT;
	}










	public void setR23_PRODUCT(String r23_PRODUCT) {
		R23_PRODUCT = r23_PRODUCT;
	}










	public BigDecimal getR23_30D_90D_PASTDUE() {
		return R23_30D_90D_PASTDUE;
	}










	public void setR23_30D_90D_PASTDUE(BigDecimal r23_30d_90d_PASTDUE) {
		R23_30D_90D_PASTDUE = r23_30d_90d_PASTDUE;
	}










	public BigDecimal getR23_NON_PERFORM_LOANS() {
		return R23_NON_PERFORM_LOANS;
	}










	public void setR23_NON_PERFORM_LOANS(BigDecimal r23_NON_PERFORM_LOANS) {
		R23_NON_PERFORM_LOANS = r23_NON_PERFORM_LOANS;
	}










	public BigDecimal getR23_NON_ACCRUALS1() {
		return R23_NON_ACCRUALS1;
	}










	public void setR23_NON_ACCRUALS1(BigDecimal r23_NON_ACCRUALS1) {
		R23_NON_ACCRUALS1 = r23_NON_ACCRUALS1;
	}










	public BigDecimal getR23_SPECIFIC_PROV1() {
		return R23_SPECIFIC_PROV1;
	}










	public void setR23_SPECIFIC_PROV1(BigDecimal r23_SPECIFIC_PROV1) {
		R23_SPECIFIC_PROV1 = r23_SPECIFIC_PROV1;
	}










	public BigDecimal getR23_NO_OF_ACC1() {
		return R23_NO_OF_ACC1;
	}










	public void setR23_NO_OF_ACC1(BigDecimal r23_NO_OF_ACC1) {
		R23_NO_OF_ACC1 = r23_NO_OF_ACC1;
	}










	public BigDecimal getR23_90D_180D_PASTDUE() {
		return R23_90D_180D_PASTDUE;
	}










	public void setR23_90D_180D_PASTDUE(BigDecimal r23_90d_180d_PASTDUE) {
		R23_90D_180D_PASTDUE = r23_90d_180d_PASTDUE;
	}










	public BigDecimal getR23_NON_ACCRUALS2() {
		return R23_NON_ACCRUALS2;
	}










	public void setR23_NON_ACCRUALS2(BigDecimal r23_NON_ACCRUALS2) {
		R23_NON_ACCRUALS2 = r23_NON_ACCRUALS2;
	}










	public BigDecimal getR23_SPECIFIC_PROV2() {
		return R23_SPECIFIC_PROV2;
	}










	public void setR23_SPECIFIC_PROV2(BigDecimal r23_SPECIFIC_PROV2) {
		R23_SPECIFIC_PROV2 = r23_SPECIFIC_PROV2;
	}










	public BigDecimal getR23_NO_OF_ACC2() {
		return R23_NO_OF_ACC2;
	}










	public void setR23_NO_OF_ACC2(BigDecimal r23_NO_OF_ACC2) {
		R23_NO_OF_ACC2 = r23_NO_OF_ACC2;
	}










	public BigDecimal getR23_180D_ABOVE_PASTDUE() {
		return R23_180D_ABOVE_PASTDUE;
	}










	public void setR23_180D_ABOVE_PASTDUE(BigDecimal r23_180d_ABOVE_PASTDUE) {
		R23_180D_ABOVE_PASTDUE = r23_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR23_NON_ACCRUALS3() {
		return R23_NON_ACCRUALS3;
	}










	public void setR23_NON_ACCRUALS3(BigDecimal r23_NON_ACCRUALS3) {
		R23_NON_ACCRUALS3 = r23_NON_ACCRUALS3;
	}










	public BigDecimal getR23_SPECIFIC_PROV3() {
		return R23_SPECIFIC_PROV3;
	}










	public void setR23_SPECIFIC_PROV3(BigDecimal r23_SPECIFIC_PROV3) {
		R23_SPECIFIC_PROV3 = r23_SPECIFIC_PROV3;
	}










	public BigDecimal getR23_NO_OF_ACC3() {
		return R23_NO_OF_ACC3;
	}










	public void setR23_NO_OF_ACC3(BigDecimal r23_NO_OF_ACC3) {
		R23_NO_OF_ACC3 = r23_NO_OF_ACC3;
	}










	public BigDecimal getR23_TOTAL_NON_ACCRUAL() {
		return R23_TOTAL_NON_ACCRUAL;
	}










	public void setR23_TOTAL_NON_ACCRUAL(BigDecimal r23_TOTAL_NON_ACCRUAL) {
		R23_TOTAL_NON_ACCRUAL = r23_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR23_TOTAL_DUE_LOANS() {
		return R23_TOTAL_DUE_LOANS;
	}










	public void setR23_TOTAL_DUE_LOANS(BigDecimal r23_TOTAL_DUE_LOANS) {
		R23_TOTAL_DUE_LOANS = r23_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR23_TOTAL_PERFORMING_LOAN() {
		return R23_TOTAL_PERFORMING_LOAN;
	}










	public void setR23_TOTAL_PERFORMING_LOAN(BigDecimal r23_TOTAL_PERFORMING_LOAN) {
		R23_TOTAL_PERFORMING_LOAN = r23_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR23_VALUE_OF_COLLATERAL() {
		return R23_VALUE_OF_COLLATERAL;
	}










	public void setR23_VALUE_OF_COLLATERAL(BigDecimal r23_VALUE_OF_COLLATERAL) {
		R23_VALUE_OF_COLLATERAL = r23_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR23_TOTAL_VALUE_NPL() {
		return R23_TOTAL_VALUE_NPL;
	}










	public void setR23_TOTAL_VALUE_NPL(BigDecimal r23_TOTAL_VALUE_NPL) {
		R23_TOTAL_VALUE_NPL = r23_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR23_TOTAL_SPECIFIC_PROV() {
		return R23_TOTAL_SPECIFIC_PROV;
	}










	public void setR23_TOTAL_SPECIFIC_PROV(BigDecimal r23_TOTAL_SPECIFIC_PROV) {
		R23_TOTAL_SPECIFIC_PROV = r23_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR23_SPECIFIC_PROV_NPL() {
		return R23_SPECIFIC_PROV_NPL;
	}










	public void setR23_SPECIFIC_PROV_NPL(BigDecimal r23_SPECIFIC_PROV_NPL) {
		R23_SPECIFIC_PROV_NPL = r23_SPECIFIC_PROV_NPL;
	}










	public String getR24_PRODUCT() {
		return R24_PRODUCT;
	}










	public void setR24_PRODUCT(String r24_PRODUCT) {
		R24_PRODUCT = r24_PRODUCT;
	}










	public BigDecimal getR24_30D_90D_PASTDUE() {
		return R24_30D_90D_PASTDUE;
	}










	public void setR24_30D_90D_PASTDUE(BigDecimal r24_30d_90d_PASTDUE) {
		R24_30D_90D_PASTDUE = r24_30d_90d_PASTDUE;
	}










	public BigDecimal getR24_NON_PERFORM_LOANS() {
		return R24_NON_PERFORM_LOANS;
	}










	public void setR24_NON_PERFORM_LOANS(BigDecimal r24_NON_PERFORM_LOANS) {
		R24_NON_PERFORM_LOANS = r24_NON_PERFORM_LOANS;
	}










	public BigDecimal getR24_NON_ACCRUALS1() {
		return R24_NON_ACCRUALS1;
	}










	public void setR24_NON_ACCRUALS1(BigDecimal r24_NON_ACCRUALS1) {
		R24_NON_ACCRUALS1 = r24_NON_ACCRUALS1;
	}










	public BigDecimal getR24_SPECIFIC_PROV1() {
		return R24_SPECIFIC_PROV1;
	}










	public void setR24_SPECIFIC_PROV1(BigDecimal r24_SPECIFIC_PROV1) {
		R24_SPECIFIC_PROV1 = r24_SPECIFIC_PROV1;
	}










	public BigDecimal getR24_NO_OF_ACC1() {
		return R24_NO_OF_ACC1;
	}










	public void setR24_NO_OF_ACC1(BigDecimal r24_NO_OF_ACC1) {
		R24_NO_OF_ACC1 = r24_NO_OF_ACC1;
	}










	public BigDecimal getR24_90D_180D_PASTDUE() {
		return R24_90D_180D_PASTDUE;
	}










	public void setR24_90D_180D_PASTDUE(BigDecimal r24_90d_180d_PASTDUE) {
		R24_90D_180D_PASTDUE = r24_90d_180d_PASTDUE;
	}










	public BigDecimal getR24_NON_ACCRUALS2() {
		return R24_NON_ACCRUALS2;
	}










	public void setR24_NON_ACCRUALS2(BigDecimal r24_NON_ACCRUALS2) {
		R24_NON_ACCRUALS2 = r24_NON_ACCRUALS2;
	}










	public BigDecimal getR24_SPECIFIC_PROV2() {
		return R24_SPECIFIC_PROV2;
	}










	public void setR24_SPECIFIC_PROV2(BigDecimal r24_SPECIFIC_PROV2) {
		R24_SPECIFIC_PROV2 = r24_SPECIFIC_PROV2;
	}










	public BigDecimal getR24_NO_OF_ACC2() {
		return R24_NO_OF_ACC2;
	}










	public void setR24_NO_OF_ACC2(BigDecimal r24_NO_OF_ACC2) {
		R24_NO_OF_ACC2 = r24_NO_OF_ACC2;
	}










	public BigDecimal getR24_180D_ABOVE_PASTDUE() {
		return R24_180D_ABOVE_PASTDUE;
	}










	public void setR24_180D_ABOVE_PASTDUE(BigDecimal r24_180d_ABOVE_PASTDUE) {
		R24_180D_ABOVE_PASTDUE = r24_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR24_NON_ACCRUALS3() {
		return R24_NON_ACCRUALS3;
	}










	public void setR24_NON_ACCRUALS3(BigDecimal r24_NON_ACCRUALS3) {
		R24_NON_ACCRUALS3 = r24_NON_ACCRUALS3;
	}










	public BigDecimal getR24_SPECIFIC_PROV3() {
		return R24_SPECIFIC_PROV3;
	}










	public void setR24_SPECIFIC_PROV3(BigDecimal r24_SPECIFIC_PROV3) {
		R24_SPECIFIC_PROV3 = r24_SPECIFIC_PROV3;
	}










	public BigDecimal getR24_NO_OF_ACC3() {
		return R24_NO_OF_ACC3;
	}










	public void setR24_NO_OF_ACC3(BigDecimal r24_NO_OF_ACC3) {
		R24_NO_OF_ACC3 = r24_NO_OF_ACC3;
	}










	public BigDecimal getR24_TOTAL_NON_ACCRUAL() {
		return R24_TOTAL_NON_ACCRUAL;
	}










	public void setR24_TOTAL_NON_ACCRUAL(BigDecimal r24_TOTAL_NON_ACCRUAL) {
		R24_TOTAL_NON_ACCRUAL = r24_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR24_TOTAL_DUE_LOANS() {
		return R24_TOTAL_DUE_LOANS;
	}










	public void setR24_TOTAL_DUE_LOANS(BigDecimal r24_TOTAL_DUE_LOANS) {
		R24_TOTAL_DUE_LOANS = r24_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR24_TOTAL_PERFORMING_LOAN() {
		return R24_TOTAL_PERFORMING_LOAN;
	}










	public void setR24_TOTAL_PERFORMING_LOAN(BigDecimal r24_TOTAL_PERFORMING_LOAN) {
		R24_TOTAL_PERFORMING_LOAN = r24_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR24_VALUE_OF_COLLATERAL() {
		return R24_VALUE_OF_COLLATERAL;
	}










	public void setR24_VALUE_OF_COLLATERAL(BigDecimal r24_VALUE_OF_COLLATERAL) {
		R24_VALUE_OF_COLLATERAL = r24_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR24_TOTAL_VALUE_NPL() {
		return R24_TOTAL_VALUE_NPL;
	}










	public void setR24_TOTAL_VALUE_NPL(BigDecimal r24_TOTAL_VALUE_NPL) {
		R24_TOTAL_VALUE_NPL = r24_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR24_TOTAL_SPECIFIC_PROV() {
		return R24_TOTAL_SPECIFIC_PROV;
	}










	public void setR24_TOTAL_SPECIFIC_PROV(BigDecimal r24_TOTAL_SPECIFIC_PROV) {
		R24_TOTAL_SPECIFIC_PROV = r24_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR24_SPECIFIC_PROV_NPL() {
		return R24_SPECIFIC_PROV_NPL;
	}










	public void setR24_SPECIFIC_PROV_NPL(BigDecimal r24_SPECIFIC_PROV_NPL) {
		R24_SPECIFIC_PROV_NPL = r24_SPECIFIC_PROV_NPL;
	}










	public String getR25_PRODUCT() {
		return R25_PRODUCT;
	}










	public void setR25_PRODUCT(String r25_PRODUCT) {
		R25_PRODUCT = r25_PRODUCT;
	}










	public BigDecimal getR25_30D_90D_PASTDUE() {
		return R25_30D_90D_PASTDUE;
	}










	public void setR25_30D_90D_PASTDUE(BigDecimal r25_30d_90d_PASTDUE) {
		R25_30D_90D_PASTDUE = r25_30d_90d_PASTDUE;
	}










	public BigDecimal getR25_NON_PERFORM_LOANS() {
		return R25_NON_PERFORM_LOANS;
	}










	public void setR25_NON_PERFORM_LOANS(BigDecimal r25_NON_PERFORM_LOANS) {
		R25_NON_PERFORM_LOANS = r25_NON_PERFORM_LOANS;
	}










	public BigDecimal getR25_NON_ACCRUALS1() {
		return R25_NON_ACCRUALS1;
	}










	public void setR25_NON_ACCRUALS1(BigDecimal r25_NON_ACCRUALS1) {
		R25_NON_ACCRUALS1 = r25_NON_ACCRUALS1;
	}










	public BigDecimal getR25_SPECIFIC_PROV1() {
		return R25_SPECIFIC_PROV1;
	}










	public void setR25_SPECIFIC_PROV1(BigDecimal r25_SPECIFIC_PROV1) {
		R25_SPECIFIC_PROV1 = r25_SPECIFIC_PROV1;
	}










	public BigDecimal getR25_NO_OF_ACC1() {
		return R25_NO_OF_ACC1;
	}










	public void setR25_NO_OF_ACC1(BigDecimal r25_NO_OF_ACC1) {
		R25_NO_OF_ACC1 = r25_NO_OF_ACC1;
	}










	public BigDecimal getR25_90D_180D_PASTDUE() {
		return R25_90D_180D_PASTDUE;
	}










	public void setR25_90D_180D_PASTDUE(BigDecimal r25_90d_180d_PASTDUE) {
		R25_90D_180D_PASTDUE = r25_90d_180d_PASTDUE;
	}










	public BigDecimal getR25_NON_ACCRUALS2() {
		return R25_NON_ACCRUALS2;
	}










	public void setR25_NON_ACCRUALS2(BigDecimal r25_NON_ACCRUALS2) {
		R25_NON_ACCRUALS2 = r25_NON_ACCRUALS2;
	}










	public BigDecimal getR25_SPECIFIC_PROV2() {
		return R25_SPECIFIC_PROV2;
	}










	public void setR25_SPECIFIC_PROV2(BigDecimal r25_SPECIFIC_PROV2) {
		R25_SPECIFIC_PROV2 = r25_SPECIFIC_PROV2;
	}










	public BigDecimal getR25_NO_OF_ACC2() {
		return R25_NO_OF_ACC2;
	}










	public void setR25_NO_OF_ACC2(BigDecimal r25_NO_OF_ACC2) {
		R25_NO_OF_ACC2 = r25_NO_OF_ACC2;
	}










	public BigDecimal getR25_180D_ABOVE_PASTDUE() {
		return R25_180D_ABOVE_PASTDUE;
	}










	public void setR25_180D_ABOVE_PASTDUE(BigDecimal r25_180d_ABOVE_PASTDUE) {
		R25_180D_ABOVE_PASTDUE = r25_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR25_NON_ACCRUALS3() {
		return R25_NON_ACCRUALS3;
	}










	public void setR25_NON_ACCRUALS3(BigDecimal r25_NON_ACCRUALS3) {
		R25_NON_ACCRUALS3 = r25_NON_ACCRUALS3;
	}










	public BigDecimal getR25_SPECIFIC_PROV3() {
		return R25_SPECIFIC_PROV3;
	}










	public void setR25_SPECIFIC_PROV3(BigDecimal r25_SPECIFIC_PROV3) {
		R25_SPECIFIC_PROV3 = r25_SPECIFIC_PROV3;
	}










	public BigDecimal getR25_NO_OF_ACC3() {
		return R25_NO_OF_ACC3;
	}










	public void setR25_NO_OF_ACC3(BigDecimal r25_NO_OF_ACC3) {
		R25_NO_OF_ACC3 = r25_NO_OF_ACC3;
	}










	public BigDecimal getR25_TOTAL_NON_ACCRUAL() {
		return R25_TOTAL_NON_ACCRUAL;
	}










	public void setR25_TOTAL_NON_ACCRUAL(BigDecimal r25_TOTAL_NON_ACCRUAL) {
		R25_TOTAL_NON_ACCRUAL = r25_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR25_TOTAL_DUE_LOANS() {
		return R25_TOTAL_DUE_LOANS;
	}










	public void setR25_TOTAL_DUE_LOANS(BigDecimal r25_TOTAL_DUE_LOANS) {
		R25_TOTAL_DUE_LOANS = r25_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR25_TOTAL_PERFORMING_LOAN() {
		return R25_TOTAL_PERFORMING_LOAN;
	}










	public void setR25_TOTAL_PERFORMING_LOAN(BigDecimal r25_TOTAL_PERFORMING_LOAN) {
		R25_TOTAL_PERFORMING_LOAN = r25_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR25_VALUE_OF_COLLATERAL() {
		return R25_VALUE_OF_COLLATERAL;
	}










	public void setR25_VALUE_OF_COLLATERAL(BigDecimal r25_VALUE_OF_COLLATERAL) {
		R25_VALUE_OF_COLLATERAL = r25_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR25_TOTAL_VALUE_NPL() {
		return R25_TOTAL_VALUE_NPL;
	}










	public void setR25_TOTAL_VALUE_NPL(BigDecimal r25_TOTAL_VALUE_NPL) {
		R25_TOTAL_VALUE_NPL = r25_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR25_TOTAL_SPECIFIC_PROV() {
		return R25_TOTAL_SPECIFIC_PROV;
	}










	public void setR25_TOTAL_SPECIFIC_PROV(BigDecimal r25_TOTAL_SPECIFIC_PROV) {
		R25_TOTAL_SPECIFIC_PROV = r25_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR25_SPECIFIC_PROV_NPL() {
		return R25_SPECIFIC_PROV_NPL;
	}










	public void setR25_SPECIFIC_PROV_NPL(BigDecimal r25_SPECIFIC_PROV_NPL) {
		R25_SPECIFIC_PROV_NPL = r25_SPECIFIC_PROV_NPL;
	}










	public String getR26_PRODUCT() {
		return R26_PRODUCT;
	}










	public void setR26_PRODUCT(String r26_PRODUCT) {
		R26_PRODUCT = r26_PRODUCT;
	}










	public BigDecimal getR26_30D_90D_PASTDUE() {
		return R26_30D_90D_PASTDUE;
	}










	public void setR26_30D_90D_PASTDUE(BigDecimal r26_30d_90d_PASTDUE) {
		R26_30D_90D_PASTDUE = r26_30d_90d_PASTDUE;
	}










	public BigDecimal getR26_NON_PERFORM_LOANS() {
		return R26_NON_PERFORM_LOANS;
	}










	public void setR26_NON_PERFORM_LOANS(BigDecimal r26_NON_PERFORM_LOANS) {
		R26_NON_PERFORM_LOANS = r26_NON_PERFORM_LOANS;
	}










	public BigDecimal getR26_NON_ACCRUALS1() {
		return R26_NON_ACCRUALS1;
	}










	public void setR26_NON_ACCRUALS1(BigDecimal r26_NON_ACCRUALS1) {
		R26_NON_ACCRUALS1 = r26_NON_ACCRUALS1;
	}










	public BigDecimal getR26_SPECIFIC_PROV1() {
		return R26_SPECIFIC_PROV1;
	}










	public void setR26_SPECIFIC_PROV1(BigDecimal r26_SPECIFIC_PROV1) {
		R26_SPECIFIC_PROV1 = r26_SPECIFIC_PROV1;
	}










	public BigDecimal getR26_NO_OF_ACC1() {
		return R26_NO_OF_ACC1;
	}










	public void setR26_NO_OF_ACC1(BigDecimal r26_NO_OF_ACC1) {
		R26_NO_OF_ACC1 = r26_NO_OF_ACC1;
	}










	public BigDecimal getR26_90D_180D_PASTDUE() {
		return R26_90D_180D_PASTDUE;
	}










	public void setR26_90D_180D_PASTDUE(BigDecimal r26_90d_180d_PASTDUE) {
		R26_90D_180D_PASTDUE = r26_90d_180d_PASTDUE;
	}










	public BigDecimal getR26_NON_ACCRUALS2() {
		return R26_NON_ACCRUALS2;
	}










	public void setR26_NON_ACCRUALS2(BigDecimal r26_NON_ACCRUALS2) {
		R26_NON_ACCRUALS2 = r26_NON_ACCRUALS2;
	}










	public BigDecimal getR26_SPECIFIC_PROV2() {
		return R26_SPECIFIC_PROV2;
	}










	public void setR26_SPECIFIC_PROV2(BigDecimal r26_SPECIFIC_PROV2) {
		R26_SPECIFIC_PROV2 = r26_SPECIFIC_PROV2;
	}










	public BigDecimal getR26_NO_OF_ACC2() {
		return R26_NO_OF_ACC2;
	}










	public void setR26_NO_OF_ACC2(BigDecimal r26_NO_OF_ACC2) {
		R26_NO_OF_ACC2 = r26_NO_OF_ACC2;
	}










	public BigDecimal getR26_180D_ABOVE_PASTDUE() {
		return R26_180D_ABOVE_PASTDUE;
	}










	public void setR26_180D_ABOVE_PASTDUE(BigDecimal r26_180d_ABOVE_PASTDUE) {
		R26_180D_ABOVE_PASTDUE = r26_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR26_NON_ACCRUALS3() {
		return R26_NON_ACCRUALS3;
	}










	public void setR26_NON_ACCRUALS3(BigDecimal r26_NON_ACCRUALS3) {
		R26_NON_ACCRUALS3 = r26_NON_ACCRUALS3;
	}










	public BigDecimal getR26_SPECIFIC_PROV3() {
		return R26_SPECIFIC_PROV3;
	}










	public void setR26_SPECIFIC_PROV3(BigDecimal r26_SPECIFIC_PROV3) {
		R26_SPECIFIC_PROV3 = r26_SPECIFIC_PROV3;
	}










	public BigDecimal getR26_NO_OF_ACC3() {
		return R26_NO_OF_ACC3;
	}










	public void setR26_NO_OF_ACC3(BigDecimal r26_NO_OF_ACC3) {
		R26_NO_OF_ACC3 = r26_NO_OF_ACC3;
	}










	public BigDecimal getR26_TOTAL_NON_ACCRUAL() {
		return R26_TOTAL_NON_ACCRUAL;
	}










	public void setR26_TOTAL_NON_ACCRUAL(BigDecimal r26_TOTAL_NON_ACCRUAL) {
		R26_TOTAL_NON_ACCRUAL = r26_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR26_TOTAL_DUE_LOANS() {
		return R26_TOTAL_DUE_LOANS;
	}










	public void setR26_TOTAL_DUE_LOANS(BigDecimal r26_TOTAL_DUE_LOANS) {
		R26_TOTAL_DUE_LOANS = r26_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR26_TOTAL_PERFORMING_LOAN() {
		return R26_TOTAL_PERFORMING_LOAN;
	}










	public void setR26_TOTAL_PERFORMING_LOAN(BigDecimal r26_TOTAL_PERFORMING_LOAN) {
		R26_TOTAL_PERFORMING_LOAN = r26_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR26_VALUE_OF_COLLATERAL() {
		return R26_VALUE_OF_COLLATERAL;
	}










	public void setR26_VALUE_OF_COLLATERAL(BigDecimal r26_VALUE_OF_COLLATERAL) {
		R26_VALUE_OF_COLLATERAL = r26_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR26_TOTAL_VALUE_NPL() {
		return R26_TOTAL_VALUE_NPL;
	}










	public void setR26_TOTAL_VALUE_NPL(BigDecimal r26_TOTAL_VALUE_NPL) {
		R26_TOTAL_VALUE_NPL = r26_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR26_TOTAL_SPECIFIC_PROV() {
		return R26_TOTAL_SPECIFIC_PROV;
	}










	public void setR26_TOTAL_SPECIFIC_PROV(BigDecimal r26_TOTAL_SPECIFIC_PROV) {
		R26_TOTAL_SPECIFIC_PROV = r26_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR26_SPECIFIC_PROV_NPL() {
		return R26_SPECIFIC_PROV_NPL;
	}










	public void setR26_SPECIFIC_PROV_NPL(BigDecimal r26_SPECIFIC_PROV_NPL) {
		R26_SPECIFIC_PROV_NPL = r26_SPECIFIC_PROV_NPL;
	}










	public String getR27_PRODUCT() {
		return R27_PRODUCT;
	}










	public void setR27_PRODUCT(String r27_PRODUCT) {
		R27_PRODUCT = r27_PRODUCT;
	}










	public BigDecimal getR27_30D_90D_PASTDUE() {
		return R27_30D_90D_PASTDUE;
	}










	public void setR27_30D_90D_PASTDUE(BigDecimal r27_30d_90d_PASTDUE) {
		R27_30D_90D_PASTDUE = r27_30d_90d_PASTDUE;
	}










	public BigDecimal getR27_NON_PERFORM_LOANS() {
		return R27_NON_PERFORM_LOANS;
	}










	public void setR27_NON_PERFORM_LOANS(BigDecimal r27_NON_PERFORM_LOANS) {
		R27_NON_PERFORM_LOANS = r27_NON_PERFORM_LOANS;
	}










	public BigDecimal getR27_NON_ACCRUALS1() {
		return R27_NON_ACCRUALS1;
	}










	public void setR27_NON_ACCRUALS1(BigDecimal r27_NON_ACCRUALS1) {
		R27_NON_ACCRUALS1 = r27_NON_ACCRUALS1;
	}










	public BigDecimal getR27_SPECIFIC_PROV1() {
		return R27_SPECIFIC_PROV1;
	}










	public void setR27_SPECIFIC_PROV1(BigDecimal r27_SPECIFIC_PROV1) {
		R27_SPECIFIC_PROV1 = r27_SPECIFIC_PROV1;
	}










	public BigDecimal getR27_NO_OF_ACC1() {
		return R27_NO_OF_ACC1;
	}










	public void setR27_NO_OF_ACC1(BigDecimal r27_NO_OF_ACC1) {
		R27_NO_OF_ACC1 = r27_NO_OF_ACC1;
	}










	public BigDecimal getR27_90D_180D_PASTDUE() {
		return R27_90D_180D_PASTDUE;
	}










	public void setR27_90D_180D_PASTDUE(BigDecimal r27_90d_180d_PASTDUE) {
		R27_90D_180D_PASTDUE = r27_90d_180d_PASTDUE;
	}










	public BigDecimal getR27_NON_ACCRUALS2() {
		return R27_NON_ACCRUALS2;
	}










	public void setR27_NON_ACCRUALS2(BigDecimal r27_NON_ACCRUALS2) {
		R27_NON_ACCRUALS2 = r27_NON_ACCRUALS2;
	}










	public BigDecimal getR27_SPECIFIC_PROV2() {
		return R27_SPECIFIC_PROV2;
	}










	public void setR27_SPECIFIC_PROV2(BigDecimal r27_SPECIFIC_PROV2) {
		R27_SPECIFIC_PROV2 = r27_SPECIFIC_PROV2;
	}










	public BigDecimal getR27_NO_OF_ACC2() {
		return R27_NO_OF_ACC2;
	}










	public void setR27_NO_OF_ACC2(BigDecimal r27_NO_OF_ACC2) {
		R27_NO_OF_ACC2 = r27_NO_OF_ACC2;
	}










	public BigDecimal getR27_180D_ABOVE_PASTDUE() {
		return R27_180D_ABOVE_PASTDUE;
	}










	public void setR27_180D_ABOVE_PASTDUE(BigDecimal r27_180d_ABOVE_PASTDUE) {
		R27_180D_ABOVE_PASTDUE = r27_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR27_NON_ACCRUALS3() {
		return R27_NON_ACCRUALS3;
	}










	public void setR27_NON_ACCRUALS3(BigDecimal r27_NON_ACCRUALS3) {
		R27_NON_ACCRUALS3 = r27_NON_ACCRUALS3;
	}










	public BigDecimal getR27_SPECIFIC_PROV3() {
		return R27_SPECIFIC_PROV3;
	}










	public void setR27_SPECIFIC_PROV3(BigDecimal r27_SPECIFIC_PROV3) {
		R27_SPECIFIC_PROV3 = r27_SPECIFIC_PROV3;
	}










	public BigDecimal getR27_NO_OF_ACC3() {
		return R27_NO_OF_ACC3;
	}










	public void setR27_NO_OF_ACC3(BigDecimal r27_NO_OF_ACC3) {
		R27_NO_OF_ACC3 = r27_NO_OF_ACC3;
	}










	public BigDecimal getR27_TOTAL_NON_ACCRUAL() {
		return R27_TOTAL_NON_ACCRUAL;
	}










	public void setR27_TOTAL_NON_ACCRUAL(BigDecimal r27_TOTAL_NON_ACCRUAL) {
		R27_TOTAL_NON_ACCRUAL = r27_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR27_TOTAL_DUE_LOANS() {
		return R27_TOTAL_DUE_LOANS;
	}










	public void setR27_TOTAL_DUE_LOANS(BigDecimal r27_TOTAL_DUE_LOANS) {
		R27_TOTAL_DUE_LOANS = r27_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR27_TOTAL_PERFORMING_LOAN() {
		return R27_TOTAL_PERFORMING_LOAN;
	}










	public void setR27_TOTAL_PERFORMING_LOAN(BigDecimal r27_TOTAL_PERFORMING_LOAN) {
		R27_TOTAL_PERFORMING_LOAN = r27_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR27_VALUE_OF_COLLATERAL() {
		return R27_VALUE_OF_COLLATERAL;
	}










	public void setR27_VALUE_OF_COLLATERAL(BigDecimal r27_VALUE_OF_COLLATERAL) {
		R27_VALUE_OF_COLLATERAL = r27_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR27_TOTAL_VALUE_NPL() {
		return R27_TOTAL_VALUE_NPL;
	}










	public void setR27_TOTAL_VALUE_NPL(BigDecimal r27_TOTAL_VALUE_NPL) {
		R27_TOTAL_VALUE_NPL = r27_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR27_TOTAL_SPECIFIC_PROV() {
		return R27_TOTAL_SPECIFIC_PROV;
	}










	public void setR27_TOTAL_SPECIFIC_PROV(BigDecimal r27_TOTAL_SPECIFIC_PROV) {
		R27_TOTAL_SPECIFIC_PROV = r27_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR27_SPECIFIC_PROV_NPL() {
		return R27_SPECIFIC_PROV_NPL;
	}










	public void setR27_SPECIFIC_PROV_NPL(BigDecimal r27_SPECIFIC_PROV_NPL) {
		R27_SPECIFIC_PROV_NPL = r27_SPECIFIC_PROV_NPL;
	}










	public String getR28_PRODUCT() {
		return R28_PRODUCT;
	}










	public void setR28_PRODUCT(String r28_PRODUCT) {
		R28_PRODUCT = r28_PRODUCT;
	}










	public BigDecimal getR28_30D_90D_PASTDUE() {
		return R28_30D_90D_PASTDUE;
	}










	public void setR28_30D_90D_PASTDUE(BigDecimal r28_30d_90d_PASTDUE) {
		R28_30D_90D_PASTDUE = r28_30d_90d_PASTDUE;
	}










	public BigDecimal getR28_NON_PERFORM_LOANS() {
		return R28_NON_PERFORM_LOANS;
	}










	public void setR28_NON_PERFORM_LOANS(BigDecimal r28_NON_PERFORM_LOANS) {
		R28_NON_PERFORM_LOANS = r28_NON_PERFORM_LOANS;
	}










	public BigDecimal getR28_NON_ACCRUALS1() {
		return R28_NON_ACCRUALS1;
	}










	public void setR28_NON_ACCRUALS1(BigDecimal r28_NON_ACCRUALS1) {
		R28_NON_ACCRUALS1 = r28_NON_ACCRUALS1;
	}










	public BigDecimal getR28_SPECIFIC_PROV1() {
		return R28_SPECIFIC_PROV1;
	}










	public void setR28_SPECIFIC_PROV1(BigDecimal r28_SPECIFIC_PROV1) {
		R28_SPECIFIC_PROV1 = r28_SPECIFIC_PROV1;
	}










	public BigDecimal getR28_NO_OF_ACC1() {
		return R28_NO_OF_ACC1;
	}










	public void setR28_NO_OF_ACC1(BigDecimal r28_NO_OF_ACC1) {
		R28_NO_OF_ACC1 = r28_NO_OF_ACC1;
	}










	public BigDecimal getR28_90D_180D_PASTDUE() {
		return R28_90D_180D_PASTDUE;
	}










	public void setR28_90D_180D_PASTDUE(BigDecimal r28_90d_180d_PASTDUE) {
		R28_90D_180D_PASTDUE = r28_90d_180d_PASTDUE;
	}










	public BigDecimal getR28_NON_ACCRUALS2() {
		return R28_NON_ACCRUALS2;
	}










	public void setR28_NON_ACCRUALS2(BigDecimal r28_NON_ACCRUALS2) {
		R28_NON_ACCRUALS2 = r28_NON_ACCRUALS2;
	}










	public BigDecimal getR28_SPECIFIC_PROV2() {
		return R28_SPECIFIC_PROV2;
	}










	public void setR28_SPECIFIC_PROV2(BigDecimal r28_SPECIFIC_PROV2) {
		R28_SPECIFIC_PROV2 = r28_SPECIFIC_PROV2;
	}










	public BigDecimal getR28_NO_OF_ACC2() {
		return R28_NO_OF_ACC2;
	}










	public void setR28_NO_OF_ACC2(BigDecimal r28_NO_OF_ACC2) {
		R28_NO_OF_ACC2 = r28_NO_OF_ACC2;
	}










	public BigDecimal getR28_180D_ABOVE_PASTDUE() {
		return R28_180D_ABOVE_PASTDUE;
	}










	public void setR28_180D_ABOVE_PASTDUE(BigDecimal r28_180d_ABOVE_PASTDUE) {
		R28_180D_ABOVE_PASTDUE = r28_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR28_NON_ACCRUALS3() {
		return R28_NON_ACCRUALS3;
	}










	public void setR28_NON_ACCRUALS3(BigDecimal r28_NON_ACCRUALS3) {
		R28_NON_ACCRUALS3 = r28_NON_ACCRUALS3;
	}










	public BigDecimal getR28_SPECIFIC_PROV3() {
		return R28_SPECIFIC_PROV3;
	}










	public void setR28_SPECIFIC_PROV3(BigDecimal r28_SPECIFIC_PROV3) {
		R28_SPECIFIC_PROV3 = r28_SPECIFIC_PROV3;
	}










	public BigDecimal getR28_NO_OF_ACC3() {
		return R28_NO_OF_ACC3;
	}










	public void setR28_NO_OF_ACC3(BigDecimal r28_NO_OF_ACC3) {
		R28_NO_OF_ACC3 = r28_NO_OF_ACC3;
	}










	public BigDecimal getR28_TOTAL_NON_ACCRUAL() {
		return R28_TOTAL_NON_ACCRUAL;
	}










	public void setR28_TOTAL_NON_ACCRUAL(BigDecimal r28_TOTAL_NON_ACCRUAL) {
		R28_TOTAL_NON_ACCRUAL = r28_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR28_TOTAL_DUE_LOANS() {
		return R28_TOTAL_DUE_LOANS;
	}










	public void setR28_TOTAL_DUE_LOANS(BigDecimal r28_TOTAL_DUE_LOANS) {
		R28_TOTAL_DUE_LOANS = r28_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR28_TOTAL_PERFORMING_LOAN() {
		return R28_TOTAL_PERFORMING_LOAN;
	}










	public void setR28_TOTAL_PERFORMING_LOAN(BigDecimal r28_TOTAL_PERFORMING_LOAN) {
		R28_TOTAL_PERFORMING_LOAN = r28_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR28_VALUE_OF_COLLATERAL() {
		return R28_VALUE_OF_COLLATERAL;
	}










	public void setR28_VALUE_OF_COLLATERAL(BigDecimal r28_VALUE_OF_COLLATERAL) {
		R28_VALUE_OF_COLLATERAL = r28_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR28_TOTAL_VALUE_NPL() {
		return R28_TOTAL_VALUE_NPL;
	}










	public void setR28_TOTAL_VALUE_NPL(BigDecimal r28_TOTAL_VALUE_NPL) {
		R28_TOTAL_VALUE_NPL = r28_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR28_TOTAL_SPECIFIC_PROV() {
		return R28_TOTAL_SPECIFIC_PROV;
	}










	public void setR28_TOTAL_SPECIFIC_PROV(BigDecimal r28_TOTAL_SPECIFIC_PROV) {
		R28_TOTAL_SPECIFIC_PROV = r28_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR28_SPECIFIC_PROV_NPL() {
		return R28_SPECIFIC_PROV_NPL;
	}










	public void setR28_SPECIFIC_PROV_NPL(BigDecimal r28_SPECIFIC_PROV_NPL) {
		R28_SPECIFIC_PROV_NPL = r28_SPECIFIC_PROV_NPL;
	}










	public String getR29_PRODUCT() {
		return R29_PRODUCT;
	}










	public void setR29_PRODUCT(String r29_PRODUCT) {
		R29_PRODUCT = r29_PRODUCT;
	}










	public BigDecimal getR29_30D_90D_PASTDUE() {
		return R29_30D_90D_PASTDUE;
	}










	public void setR29_30D_90D_PASTDUE(BigDecimal r29_30d_90d_PASTDUE) {
		R29_30D_90D_PASTDUE = r29_30d_90d_PASTDUE;
	}










	public BigDecimal getR29_NON_PERFORM_LOANS() {
		return R29_NON_PERFORM_LOANS;
	}










	public void setR29_NON_PERFORM_LOANS(BigDecimal r29_NON_PERFORM_LOANS) {
		R29_NON_PERFORM_LOANS = r29_NON_PERFORM_LOANS;
	}










	public BigDecimal getR29_NON_ACCRUALS1() {
		return R29_NON_ACCRUALS1;
	}










	public void setR29_NON_ACCRUALS1(BigDecimal r29_NON_ACCRUALS1) {
		R29_NON_ACCRUALS1 = r29_NON_ACCRUALS1;
	}










	public BigDecimal getR29_SPECIFIC_PROV1() {
		return R29_SPECIFIC_PROV1;
	}










	public void setR29_SPECIFIC_PROV1(BigDecimal r29_SPECIFIC_PROV1) {
		R29_SPECIFIC_PROV1 = r29_SPECIFIC_PROV1;
	}










	public BigDecimal getR29_NO_OF_ACC1() {
		return R29_NO_OF_ACC1;
	}










	public void setR29_NO_OF_ACC1(BigDecimal r29_NO_OF_ACC1) {
		R29_NO_OF_ACC1 = r29_NO_OF_ACC1;
	}










	public BigDecimal getR29_90D_180D_PASTDUE() {
		return R29_90D_180D_PASTDUE;
	}










	public void setR29_90D_180D_PASTDUE(BigDecimal r29_90d_180d_PASTDUE) {
		R29_90D_180D_PASTDUE = r29_90d_180d_PASTDUE;
	}










	public BigDecimal getR29_NON_ACCRUALS2() {
		return R29_NON_ACCRUALS2;
	}










	public void setR29_NON_ACCRUALS2(BigDecimal r29_NON_ACCRUALS2) {
		R29_NON_ACCRUALS2 = r29_NON_ACCRUALS2;
	}










	public BigDecimal getR29_SPECIFIC_PROV2() {
		return R29_SPECIFIC_PROV2;
	}










	public void setR29_SPECIFIC_PROV2(BigDecimal r29_SPECIFIC_PROV2) {
		R29_SPECIFIC_PROV2 = r29_SPECIFIC_PROV2;
	}










	public BigDecimal getR29_NO_OF_ACC2() {
		return R29_NO_OF_ACC2;
	}










	public void setR29_NO_OF_ACC2(BigDecimal r29_NO_OF_ACC2) {
		R29_NO_OF_ACC2 = r29_NO_OF_ACC2;
	}










	public BigDecimal getR29_180D_ABOVE_PASTDUE() {
		return R29_180D_ABOVE_PASTDUE;
	}










	public void setR29_180D_ABOVE_PASTDUE(BigDecimal r29_180d_ABOVE_PASTDUE) {
		R29_180D_ABOVE_PASTDUE = r29_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR29_NON_ACCRUALS3() {
		return R29_NON_ACCRUALS3;
	}










	public void setR29_NON_ACCRUALS3(BigDecimal r29_NON_ACCRUALS3) {
		R29_NON_ACCRUALS3 = r29_NON_ACCRUALS3;
	}










	public BigDecimal getR29_SPECIFIC_PROV3() {
		return R29_SPECIFIC_PROV3;
	}










	public void setR29_SPECIFIC_PROV3(BigDecimal r29_SPECIFIC_PROV3) {
		R29_SPECIFIC_PROV3 = r29_SPECIFIC_PROV3;
	}










	public BigDecimal getR29_NO_OF_ACC3() {
		return R29_NO_OF_ACC3;
	}










	public void setR29_NO_OF_ACC3(BigDecimal r29_NO_OF_ACC3) {
		R29_NO_OF_ACC3 = r29_NO_OF_ACC3;
	}










	public BigDecimal getR29_TOTAL_NON_ACCRUAL() {
		return R29_TOTAL_NON_ACCRUAL;
	}










	public void setR29_TOTAL_NON_ACCRUAL(BigDecimal r29_TOTAL_NON_ACCRUAL) {
		R29_TOTAL_NON_ACCRUAL = r29_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR29_TOTAL_DUE_LOANS() {
		return R29_TOTAL_DUE_LOANS;
	}










	public void setR29_TOTAL_DUE_LOANS(BigDecimal r29_TOTAL_DUE_LOANS) {
		R29_TOTAL_DUE_LOANS = r29_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR29_TOTAL_PERFORMING_LOAN() {
		return R29_TOTAL_PERFORMING_LOAN;
	}










	public void setR29_TOTAL_PERFORMING_LOAN(BigDecimal r29_TOTAL_PERFORMING_LOAN) {
		R29_TOTAL_PERFORMING_LOAN = r29_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR29_VALUE_OF_COLLATERAL() {
		return R29_VALUE_OF_COLLATERAL;
	}










	public void setR29_VALUE_OF_COLLATERAL(BigDecimal r29_VALUE_OF_COLLATERAL) {
		R29_VALUE_OF_COLLATERAL = r29_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR29_TOTAL_VALUE_NPL() {
		return R29_TOTAL_VALUE_NPL;
	}










	public void setR29_TOTAL_VALUE_NPL(BigDecimal r29_TOTAL_VALUE_NPL) {
		R29_TOTAL_VALUE_NPL = r29_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR29_TOTAL_SPECIFIC_PROV() {
		return R29_TOTAL_SPECIFIC_PROV;
	}










	public void setR29_TOTAL_SPECIFIC_PROV(BigDecimal r29_TOTAL_SPECIFIC_PROV) {
		R29_TOTAL_SPECIFIC_PROV = r29_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR29_SPECIFIC_PROV_NPL() {
		return R29_SPECIFIC_PROV_NPL;
	}










	public void setR29_SPECIFIC_PROV_NPL(BigDecimal r29_SPECIFIC_PROV_NPL) {
		R29_SPECIFIC_PROV_NPL = r29_SPECIFIC_PROV_NPL;
	}










	public String getR30_PRODUCT() {
		return R30_PRODUCT;
	}










	public void setR30_PRODUCT(String r30_PRODUCT) {
		R30_PRODUCT = r30_PRODUCT;
	}










	public BigDecimal getR30_30D_90D_PASTDUE() {
		return R30_30D_90D_PASTDUE;
	}










	public void setR30_30D_90D_PASTDUE(BigDecimal r30_30d_90d_PASTDUE) {
		R30_30D_90D_PASTDUE = r30_30d_90d_PASTDUE;
	}










	public BigDecimal getR30_NON_PERFORM_LOANS() {
		return R30_NON_PERFORM_LOANS;
	}










	public void setR30_NON_PERFORM_LOANS(BigDecimal r30_NON_PERFORM_LOANS) {
		R30_NON_PERFORM_LOANS = r30_NON_PERFORM_LOANS;
	}










	public BigDecimal getR30_NON_ACCRUALS1() {
		return R30_NON_ACCRUALS1;
	}










	public void setR30_NON_ACCRUALS1(BigDecimal r30_NON_ACCRUALS1) {
		R30_NON_ACCRUALS1 = r30_NON_ACCRUALS1;
	}










	public BigDecimal getR30_SPECIFIC_PROV1() {
		return R30_SPECIFIC_PROV1;
	}










	public void setR30_SPECIFIC_PROV1(BigDecimal r30_SPECIFIC_PROV1) {
		R30_SPECIFIC_PROV1 = r30_SPECIFIC_PROV1;
	}










	public BigDecimal getR30_NO_OF_ACC1() {
		return R30_NO_OF_ACC1;
	}










	public void setR30_NO_OF_ACC1(BigDecimal r30_NO_OF_ACC1) {
		R30_NO_OF_ACC1 = r30_NO_OF_ACC1;
	}










	public BigDecimal getR30_90D_180D_PASTDUE() {
		return R30_90D_180D_PASTDUE;
	}










	public void setR30_90D_180D_PASTDUE(BigDecimal r30_90d_180d_PASTDUE) {
		R30_90D_180D_PASTDUE = r30_90d_180d_PASTDUE;
	}










	public BigDecimal getR30_NON_ACCRUALS2() {
		return R30_NON_ACCRUALS2;
	}










	public void setR30_NON_ACCRUALS2(BigDecimal r30_NON_ACCRUALS2) {
		R30_NON_ACCRUALS2 = r30_NON_ACCRUALS2;
	}










	public BigDecimal getR30_SPECIFIC_PROV2() {
		return R30_SPECIFIC_PROV2;
	}










	public void setR30_SPECIFIC_PROV2(BigDecimal r30_SPECIFIC_PROV2) {
		R30_SPECIFIC_PROV2 = r30_SPECIFIC_PROV2;
	}










	public BigDecimal getR30_NO_OF_ACC2() {
		return R30_NO_OF_ACC2;
	}










	public void setR30_NO_OF_ACC2(BigDecimal r30_NO_OF_ACC2) {
		R30_NO_OF_ACC2 = r30_NO_OF_ACC2;
	}










	public BigDecimal getR30_180D_ABOVE_PASTDUE() {
		return R30_180D_ABOVE_PASTDUE;
	}










	public void setR30_180D_ABOVE_PASTDUE(BigDecimal r30_180d_ABOVE_PASTDUE) {
		R30_180D_ABOVE_PASTDUE = r30_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR30_NON_ACCRUALS3() {
		return R30_NON_ACCRUALS3;
	}










	public void setR30_NON_ACCRUALS3(BigDecimal r30_NON_ACCRUALS3) {
		R30_NON_ACCRUALS3 = r30_NON_ACCRUALS3;
	}










	public BigDecimal getR30_SPECIFIC_PROV3() {
		return R30_SPECIFIC_PROV3;
	}










	public void setR30_SPECIFIC_PROV3(BigDecimal r30_SPECIFIC_PROV3) {
		R30_SPECIFIC_PROV3 = r30_SPECIFIC_PROV3;
	}










	public BigDecimal getR30_NO_OF_ACC3() {
		return R30_NO_OF_ACC3;
	}










	public void setR30_NO_OF_ACC3(BigDecimal r30_NO_OF_ACC3) {
		R30_NO_OF_ACC3 = r30_NO_OF_ACC3;
	}










	public BigDecimal getR30_TOTAL_NON_ACCRUAL() {
		return R30_TOTAL_NON_ACCRUAL;
	}










	public void setR30_TOTAL_NON_ACCRUAL(BigDecimal r30_TOTAL_NON_ACCRUAL) {
		R30_TOTAL_NON_ACCRUAL = r30_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR30_TOTAL_DUE_LOANS() {
		return R30_TOTAL_DUE_LOANS;
	}










	public void setR30_TOTAL_DUE_LOANS(BigDecimal r30_TOTAL_DUE_LOANS) {
		R30_TOTAL_DUE_LOANS = r30_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR30_TOTAL_PERFORMING_LOAN() {
		return R30_TOTAL_PERFORMING_LOAN;
	}










	public void setR30_TOTAL_PERFORMING_LOAN(BigDecimal r30_TOTAL_PERFORMING_LOAN) {
		R30_TOTAL_PERFORMING_LOAN = r30_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR30_VALUE_OF_COLLATERAL() {
		return R30_VALUE_OF_COLLATERAL;
	}










	public void setR30_VALUE_OF_COLLATERAL(BigDecimal r30_VALUE_OF_COLLATERAL) {
		R30_VALUE_OF_COLLATERAL = r30_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR30_TOTAL_VALUE_NPL() {
		return R30_TOTAL_VALUE_NPL;
	}










	public void setR30_TOTAL_VALUE_NPL(BigDecimal r30_TOTAL_VALUE_NPL) {
		R30_TOTAL_VALUE_NPL = r30_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR30_TOTAL_SPECIFIC_PROV() {
		return R30_TOTAL_SPECIFIC_PROV;
	}










	public void setR30_TOTAL_SPECIFIC_PROV(BigDecimal r30_TOTAL_SPECIFIC_PROV) {
		R30_TOTAL_SPECIFIC_PROV = r30_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR30_SPECIFIC_PROV_NPL() {
		return R30_SPECIFIC_PROV_NPL;
	}










	public void setR30_SPECIFIC_PROV_NPL(BigDecimal r30_SPECIFIC_PROV_NPL) {
		R30_SPECIFIC_PROV_NPL = r30_SPECIFIC_PROV_NPL;
	}










	public String getR31_PRODUCT() {
		return R31_PRODUCT;
	}










	public void setR31_PRODUCT(String r31_PRODUCT) {
		R31_PRODUCT = r31_PRODUCT;
	}










	public BigDecimal getR31_30D_90D_PASTDUE() {
		return R31_30D_90D_PASTDUE;
	}










	public void setR31_30D_90D_PASTDUE(BigDecimal r31_30d_90d_PASTDUE) {
		R31_30D_90D_PASTDUE = r31_30d_90d_PASTDUE;
	}










	public BigDecimal getR31_NON_PERFORM_LOANS() {
		return R31_NON_PERFORM_LOANS;
	}










	public void setR31_NON_PERFORM_LOANS(BigDecimal r31_NON_PERFORM_LOANS) {
		R31_NON_PERFORM_LOANS = r31_NON_PERFORM_LOANS;
	}










	public BigDecimal getR31_NON_ACCRUALS1() {
		return R31_NON_ACCRUALS1;
	}










	public void setR31_NON_ACCRUALS1(BigDecimal r31_NON_ACCRUALS1) {
		R31_NON_ACCRUALS1 = r31_NON_ACCRUALS1;
	}










	public BigDecimal getR31_SPECIFIC_PROV1() {
		return R31_SPECIFIC_PROV1;
	}










	public void setR31_SPECIFIC_PROV1(BigDecimal r31_SPECIFIC_PROV1) {
		R31_SPECIFIC_PROV1 = r31_SPECIFIC_PROV1;
	}










	public BigDecimal getR31_NO_OF_ACC1() {
		return R31_NO_OF_ACC1;
	}










	public void setR31_NO_OF_ACC1(BigDecimal r31_NO_OF_ACC1) {
		R31_NO_OF_ACC1 = r31_NO_OF_ACC1;
	}










	public BigDecimal getR31_90D_180D_PASTDUE() {
		return R31_90D_180D_PASTDUE;
	}










	public void setR31_90D_180D_PASTDUE(BigDecimal r31_90d_180d_PASTDUE) {
		R31_90D_180D_PASTDUE = r31_90d_180d_PASTDUE;
	}










	public BigDecimal getR31_NON_ACCRUALS2() {
		return R31_NON_ACCRUALS2;
	}










	public void setR31_NON_ACCRUALS2(BigDecimal r31_NON_ACCRUALS2) {
		R31_NON_ACCRUALS2 = r31_NON_ACCRUALS2;
	}










	public BigDecimal getR31_SPECIFIC_PROV2() {
		return R31_SPECIFIC_PROV2;
	}










	public void setR31_SPECIFIC_PROV2(BigDecimal r31_SPECIFIC_PROV2) {
		R31_SPECIFIC_PROV2 = r31_SPECIFIC_PROV2;
	}










	public BigDecimal getR31_NO_OF_ACC2() {
		return R31_NO_OF_ACC2;
	}










	public void setR31_NO_OF_ACC2(BigDecimal r31_NO_OF_ACC2) {
		R31_NO_OF_ACC2 = r31_NO_OF_ACC2;
	}










	public BigDecimal getR31_180D_ABOVE_PASTDUE() {
		return R31_180D_ABOVE_PASTDUE;
	}










	public void setR31_180D_ABOVE_PASTDUE(BigDecimal r31_180d_ABOVE_PASTDUE) {
		R31_180D_ABOVE_PASTDUE = r31_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR31_NON_ACCRUALS3() {
		return R31_NON_ACCRUALS3;
	}










	public void setR31_NON_ACCRUALS3(BigDecimal r31_NON_ACCRUALS3) {
		R31_NON_ACCRUALS3 = r31_NON_ACCRUALS3;
	}










	public BigDecimal getR31_SPECIFIC_PROV3() {
		return R31_SPECIFIC_PROV3;
	}










	public void setR31_SPECIFIC_PROV3(BigDecimal r31_SPECIFIC_PROV3) {
		R31_SPECIFIC_PROV3 = r31_SPECIFIC_PROV3;
	}










	public BigDecimal getR31_NO_OF_ACC3() {
		return R31_NO_OF_ACC3;
	}










	public void setR31_NO_OF_ACC3(BigDecimal r31_NO_OF_ACC3) {
		R31_NO_OF_ACC3 = r31_NO_OF_ACC3;
	}










	public BigDecimal getR31_TOTAL_NON_ACCRUAL() {
		return R31_TOTAL_NON_ACCRUAL;
	}










	public void setR31_TOTAL_NON_ACCRUAL(BigDecimal r31_TOTAL_NON_ACCRUAL) {
		R31_TOTAL_NON_ACCRUAL = r31_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR31_TOTAL_DUE_LOANS() {
		return R31_TOTAL_DUE_LOANS;
	}










	public void setR31_TOTAL_DUE_LOANS(BigDecimal r31_TOTAL_DUE_LOANS) {
		R31_TOTAL_DUE_LOANS = r31_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR31_TOTAL_PERFORMING_LOAN() {
		return R31_TOTAL_PERFORMING_LOAN;
	}










	public void setR31_TOTAL_PERFORMING_LOAN(BigDecimal r31_TOTAL_PERFORMING_LOAN) {
		R31_TOTAL_PERFORMING_LOAN = r31_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR31_VALUE_OF_COLLATERAL() {
		return R31_VALUE_OF_COLLATERAL;
	}










	public void setR31_VALUE_OF_COLLATERAL(BigDecimal r31_VALUE_OF_COLLATERAL) {
		R31_VALUE_OF_COLLATERAL = r31_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR31_TOTAL_VALUE_NPL() {
		return R31_TOTAL_VALUE_NPL;
	}










	public void setR31_TOTAL_VALUE_NPL(BigDecimal r31_TOTAL_VALUE_NPL) {
		R31_TOTAL_VALUE_NPL = r31_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR31_TOTAL_SPECIFIC_PROV() {
		return R31_TOTAL_SPECIFIC_PROV;
	}










	public void setR31_TOTAL_SPECIFIC_PROV(BigDecimal r31_TOTAL_SPECIFIC_PROV) {
		R31_TOTAL_SPECIFIC_PROV = r31_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR31_SPECIFIC_PROV_NPL() {
		return R31_SPECIFIC_PROV_NPL;
	}










	public void setR31_SPECIFIC_PROV_NPL(BigDecimal r31_SPECIFIC_PROV_NPL) {
		R31_SPECIFIC_PROV_NPL = r31_SPECIFIC_PROV_NPL;
	}










	public String getR32_PRODUCT() {
		return R32_PRODUCT;
	}










	public void setR32_PRODUCT(String r32_PRODUCT) {
		R32_PRODUCT = r32_PRODUCT;
	}










	public BigDecimal getR32_30D_90D_PASTDUE() {
		return R32_30D_90D_PASTDUE;
	}










	public void setR32_30D_90D_PASTDUE(BigDecimal r32_30d_90d_PASTDUE) {
		R32_30D_90D_PASTDUE = r32_30d_90d_PASTDUE;
	}










	public BigDecimal getR32_NON_PERFORM_LOANS() {
		return R32_NON_PERFORM_LOANS;
	}










	public void setR32_NON_PERFORM_LOANS(BigDecimal r32_NON_PERFORM_LOANS) {
		R32_NON_PERFORM_LOANS = r32_NON_PERFORM_LOANS;
	}










	public BigDecimal getR32_NON_ACCRUALS1() {
		return R32_NON_ACCRUALS1;
	}










	public void setR32_NON_ACCRUALS1(BigDecimal r32_NON_ACCRUALS1) {
		R32_NON_ACCRUALS1 = r32_NON_ACCRUALS1;
	}










	public BigDecimal getR32_SPECIFIC_PROV1() {
		return R32_SPECIFIC_PROV1;
	}










	public void setR32_SPECIFIC_PROV1(BigDecimal r32_SPECIFIC_PROV1) {
		R32_SPECIFIC_PROV1 = r32_SPECIFIC_PROV1;
	}










	public BigDecimal getR32_NO_OF_ACC1() {
		return R32_NO_OF_ACC1;
	}










	public void setR32_NO_OF_ACC1(BigDecimal r32_NO_OF_ACC1) {
		R32_NO_OF_ACC1 = r32_NO_OF_ACC1;
	}










	public BigDecimal getR32_90D_180D_PASTDUE() {
		return R32_90D_180D_PASTDUE;
	}










	public void setR32_90D_180D_PASTDUE(BigDecimal r32_90d_180d_PASTDUE) {
		R32_90D_180D_PASTDUE = r32_90d_180d_PASTDUE;
	}










	public BigDecimal getR32_NON_ACCRUALS2() {
		return R32_NON_ACCRUALS2;
	}










	public void setR32_NON_ACCRUALS2(BigDecimal r32_NON_ACCRUALS2) {
		R32_NON_ACCRUALS2 = r32_NON_ACCRUALS2;
	}










	public BigDecimal getR32_SPECIFIC_PROV2() {
		return R32_SPECIFIC_PROV2;
	}










	public void setR32_SPECIFIC_PROV2(BigDecimal r32_SPECIFIC_PROV2) {
		R32_SPECIFIC_PROV2 = r32_SPECIFIC_PROV2;
	}










	public BigDecimal getR32_NO_OF_ACC2() {
		return R32_NO_OF_ACC2;
	}










	public void setR32_NO_OF_ACC2(BigDecimal r32_NO_OF_ACC2) {
		R32_NO_OF_ACC2 = r32_NO_OF_ACC2;
	}










	public BigDecimal getR32_180D_ABOVE_PASTDUE() {
		return R32_180D_ABOVE_PASTDUE;
	}










	public void setR32_180D_ABOVE_PASTDUE(BigDecimal r32_180d_ABOVE_PASTDUE) {
		R32_180D_ABOVE_PASTDUE = r32_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR32_NON_ACCRUALS3() {
		return R32_NON_ACCRUALS3;
	}










	public void setR32_NON_ACCRUALS3(BigDecimal r32_NON_ACCRUALS3) {
		R32_NON_ACCRUALS3 = r32_NON_ACCRUALS3;
	}










	public BigDecimal getR32_SPECIFIC_PROV3() {
		return R32_SPECIFIC_PROV3;
	}










	public void setR32_SPECIFIC_PROV3(BigDecimal r32_SPECIFIC_PROV3) {
		R32_SPECIFIC_PROV3 = r32_SPECIFIC_PROV3;
	}










	public BigDecimal getR32_NO_OF_ACC3() {
		return R32_NO_OF_ACC3;
	}










	public void setR32_NO_OF_ACC3(BigDecimal r32_NO_OF_ACC3) {
		R32_NO_OF_ACC3 = r32_NO_OF_ACC3;
	}










	public BigDecimal getR32_TOTAL_NON_ACCRUAL() {
		return R32_TOTAL_NON_ACCRUAL;
	}










	public void setR32_TOTAL_NON_ACCRUAL(BigDecimal r32_TOTAL_NON_ACCRUAL) {
		R32_TOTAL_NON_ACCRUAL = r32_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR32_TOTAL_DUE_LOANS() {
		return R32_TOTAL_DUE_LOANS;
	}










	public void setR32_TOTAL_DUE_LOANS(BigDecimal r32_TOTAL_DUE_LOANS) {
		R32_TOTAL_DUE_LOANS = r32_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR32_TOTAL_PERFORMING_LOAN() {
		return R32_TOTAL_PERFORMING_LOAN;
	}










	public void setR32_TOTAL_PERFORMING_LOAN(BigDecimal r32_TOTAL_PERFORMING_LOAN) {
		R32_TOTAL_PERFORMING_LOAN = r32_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR32_VALUE_OF_COLLATERAL() {
		return R32_VALUE_OF_COLLATERAL;
	}










	public void setR32_VALUE_OF_COLLATERAL(BigDecimal r32_VALUE_OF_COLLATERAL) {
		R32_VALUE_OF_COLLATERAL = r32_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR32_TOTAL_VALUE_NPL() {
		return R32_TOTAL_VALUE_NPL;
	}










	public void setR32_TOTAL_VALUE_NPL(BigDecimal r32_TOTAL_VALUE_NPL) {
		R32_TOTAL_VALUE_NPL = r32_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR32_TOTAL_SPECIFIC_PROV() {
		return R32_TOTAL_SPECIFIC_PROV;
	}










	public void setR32_TOTAL_SPECIFIC_PROV(BigDecimal r32_TOTAL_SPECIFIC_PROV) {
		R32_TOTAL_SPECIFIC_PROV = r32_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR32_SPECIFIC_PROV_NPL() {
		return R32_SPECIFIC_PROV_NPL;
	}










	public void setR32_SPECIFIC_PROV_NPL(BigDecimal r32_SPECIFIC_PROV_NPL) {
		R32_SPECIFIC_PROV_NPL = r32_SPECIFIC_PROV_NPL;
	}










	public String getR33_PRODUCT() {
		return R33_PRODUCT;
	}










	public void setR33_PRODUCT(String r33_PRODUCT) {
		R33_PRODUCT = r33_PRODUCT;
	}










	public BigDecimal getR33_30D_90D_PASTDUE() {
		return R33_30D_90D_PASTDUE;
	}










	public void setR33_30D_90D_PASTDUE(BigDecimal r33_30d_90d_PASTDUE) {
		R33_30D_90D_PASTDUE = r33_30d_90d_PASTDUE;
	}










	public BigDecimal getR33_NON_PERFORM_LOANS() {
		return R33_NON_PERFORM_LOANS;
	}










	public void setR33_NON_PERFORM_LOANS(BigDecimal r33_NON_PERFORM_LOANS) {
		R33_NON_PERFORM_LOANS = r33_NON_PERFORM_LOANS;
	}










	public BigDecimal getR33_NON_ACCRUALS1() {
		return R33_NON_ACCRUALS1;
	}










	public void setR33_NON_ACCRUALS1(BigDecimal r33_NON_ACCRUALS1) {
		R33_NON_ACCRUALS1 = r33_NON_ACCRUALS1;
	}










	public BigDecimal getR33_SPECIFIC_PROV1() {
		return R33_SPECIFIC_PROV1;
	}










	public void setR33_SPECIFIC_PROV1(BigDecimal r33_SPECIFIC_PROV1) {
		R33_SPECIFIC_PROV1 = r33_SPECIFIC_PROV1;
	}










	public BigDecimal getR33_NO_OF_ACC1() {
		return R33_NO_OF_ACC1;
	}










	public void setR33_NO_OF_ACC1(BigDecimal r33_NO_OF_ACC1) {
		R33_NO_OF_ACC1 = r33_NO_OF_ACC1;
	}










	public BigDecimal getR33_90D_180D_PASTDUE() {
		return R33_90D_180D_PASTDUE;
	}










	public void setR33_90D_180D_PASTDUE(BigDecimal r33_90d_180d_PASTDUE) {
		R33_90D_180D_PASTDUE = r33_90d_180d_PASTDUE;
	}










	public BigDecimal getR33_NON_ACCRUALS2() {
		return R33_NON_ACCRUALS2;
	}










	public void setR33_NON_ACCRUALS2(BigDecimal r33_NON_ACCRUALS2) {
		R33_NON_ACCRUALS2 = r33_NON_ACCRUALS2;
	}










	public BigDecimal getR33_SPECIFIC_PROV2() {
		return R33_SPECIFIC_PROV2;
	}










	public void setR33_SPECIFIC_PROV2(BigDecimal r33_SPECIFIC_PROV2) {
		R33_SPECIFIC_PROV2 = r33_SPECIFIC_PROV2;
	}










	public BigDecimal getR33_NO_OF_ACC2() {
		return R33_NO_OF_ACC2;
	}










	public void setR33_NO_OF_ACC2(BigDecimal r33_NO_OF_ACC2) {
		R33_NO_OF_ACC2 = r33_NO_OF_ACC2;
	}










	public BigDecimal getR33_180D_ABOVE_PASTDUE() {
		return R33_180D_ABOVE_PASTDUE;
	}










	public void setR33_180D_ABOVE_PASTDUE(BigDecimal r33_180d_ABOVE_PASTDUE) {
		R33_180D_ABOVE_PASTDUE = r33_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR33_NON_ACCRUALS3() {
		return R33_NON_ACCRUALS3;
	}










	public void setR33_NON_ACCRUALS3(BigDecimal r33_NON_ACCRUALS3) {
		R33_NON_ACCRUALS3 = r33_NON_ACCRUALS3;
	}










	public BigDecimal getR33_SPECIFIC_PROV3() {
		return R33_SPECIFIC_PROV3;
	}










	public void setR33_SPECIFIC_PROV3(BigDecimal r33_SPECIFIC_PROV3) {
		R33_SPECIFIC_PROV3 = r33_SPECIFIC_PROV3;
	}










	public BigDecimal getR33_NO_OF_ACC3() {
		return R33_NO_OF_ACC3;
	}










	public void setR33_NO_OF_ACC3(BigDecimal r33_NO_OF_ACC3) {
		R33_NO_OF_ACC3 = r33_NO_OF_ACC3;
	}










	public BigDecimal getR33_TOTAL_NON_ACCRUAL() {
		return R33_TOTAL_NON_ACCRUAL;
	}










	public void setR33_TOTAL_NON_ACCRUAL(BigDecimal r33_TOTAL_NON_ACCRUAL) {
		R33_TOTAL_NON_ACCRUAL = r33_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR33_TOTAL_DUE_LOANS() {
		return R33_TOTAL_DUE_LOANS;
	}










	public void setR33_TOTAL_DUE_LOANS(BigDecimal r33_TOTAL_DUE_LOANS) {
		R33_TOTAL_DUE_LOANS = r33_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR33_TOTAL_PERFORMING_LOAN() {
		return R33_TOTAL_PERFORMING_LOAN;
	}










	public void setR33_TOTAL_PERFORMING_LOAN(BigDecimal r33_TOTAL_PERFORMING_LOAN) {
		R33_TOTAL_PERFORMING_LOAN = r33_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR33_VALUE_OF_COLLATERAL() {
		return R33_VALUE_OF_COLLATERAL;
	}










	public void setR33_VALUE_OF_COLLATERAL(BigDecimal r33_VALUE_OF_COLLATERAL) {
		R33_VALUE_OF_COLLATERAL = r33_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR33_TOTAL_VALUE_NPL() {
		return R33_TOTAL_VALUE_NPL;
	}










	public void setR33_TOTAL_VALUE_NPL(BigDecimal r33_TOTAL_VALUE_NPL) {
		R33_TOTAL_VALUE_NPL = r33_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR33_TOTAL_SPECIFIC_PROV() {
		return R33_TOTAL_SPECIFIC_PROV;
	}










	public void setR33_TOTAL_SPECIFIC_PROV(BigDecimal r33_TOTAL_SPECIFIC_PROV) {
		R33_TOTAL_SPECIFIC_PROV = r33_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR33_SPECIFIC_PROV_NPL() {
		return R33_SPECIFIC_PROV_NPL;
	}










	public void setR33_SPECIFIC_PROV_NPL(BigDecimal r33_SPECIFIC_PROV_NPL) {
		R33_SPECIFIC_PROV_NPL = r33_SPECIFIC_PROV_NPL;
	}










	public String getR34_PRODUCT() {
		return R34_PRODUCT;
	}










	public void setR34_PRODUCT(String r34_PRODUCT) {
		R34_PRODUCT = r34_PRODUCT;
	}










	public BigDecimal getR34_30D_90D_PASTDUE() {
		return R34_30D_90D_PASTDUE;
	}










	public void setR34_30D_90D_PASTDUE(BigDecimal r34_30d_90d_PASTDUE) {
		R34_30D_90D_PASTDUE = r34_30d_90d_PASTDUE;
	}










	public BigDecimal getR34_NON_PERFORM_LOANS() {
		return R34_NON_PERFORM_LOANS;
	}










	public void setR34_NON_PERFORM_LOANS(BigDecimal r34_NON_PERFORM_LOANS) {
		R34_NON_PERFORM_LOANS = r34_NON_PERFORM_LOANS;
	}










	public BigDecimal getR34_NON_ACCRUALS1() {
		return R34_NON_ACCRUALS1;
	}










	public void setR34_NON_ACCRUALS1(BigDecimal r34_NON_ACCRUALS1) {
		R34_NON_ACCRUALS1 = r34_NON_ACCRUALS1;
	}










	public BigDecimal getR34_SPECIFIC_PROV1() {
		return R34_SPECIFIC_PROV1;
	}










	public void setR34_SPECIFIC_PROV1(BigDecimal r34_SPECIFIC_PROV1) {
		R34_SPECIFIC_PROV1 = r34_SPECIFIC_PROV1;
	}










	public BigDecimal getR34_NO_OF_ACC1() {
		return R34_NO_OF_ACC1;
	}










	public void setR34_NO_OF_ACC1(BigDecimal r34_NO_OF_ACC1) {
		R34_NO_OF_ACC1 = r34_NO_OF_ACC1;
	}










	public BigDecimal getR34_90D_180D_PASTDUE() {
		return R34_90D_180D_PASTDUE;
	}










	public void setR34_90D_180D_PASTDUE(BigDecimal r34_90d_180d_PASTDUE) {
		R34_90D_180D_PASTDUE = r34_90d_180d_PASTDUE;
	}










	public BigDecimal getR34_NON_ACCRUALS2() {
		return R34_NON_ACCRUALS2;
	}










	public void setR34_NON_ACCRUALS2(BigDecimal r34_NON_ACCRUALS2) {
		R34_NON_ACCRUALS2 = r34_NON_ACCRUALS2;
	}










	public BigDecimal getR34_SPECIFIC_PROV2() {
		return R34_SPECIFIC_PROV2;
	}










	public void setR34_SPECIFIC_PROV2(BigDecimal r34_SPECIFIC_PROV2) {
		R34_SPECIFIC_PROV2 = r34_SPECIFIC_PROV2;
	}










	public BigDecimal getR34_NO_OF_ACC2() {
		return R34_NO_OF_ACC2;
	}










	public void setR34_NO_OF_ACC2(BigDecimal r34_NO_OF_ACC2) {
		R34_NO_OF_ACC2 = r34_NO_OF_ACC2;
	}










	public BigDecimal getR34_180D_ABOVE_PASTDUE() {
		return R34_180D_ABOVE_PASTDUE;
	}










	public void setR34_180D_ABOVE_PASTDUE(BigDecimal r34_180d_ABOVE_PASTDUE) {
		R34_180D_ABOVE_PASTDUE = r34_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR34_NON_ACCRUALS3() {
		return R34_NON_ACCRUALS3;
	}










	public void setR34_NON_ACCRUALS3(BigDecimal r34_NON_ACCRUALS3) {
		R34_NON_ACCRUALS3 = r34_NON_ACCRUALS3;
	}










	public BigDecimal getR34_SPECIFIC_PROV3() {
		return R34_SPECIFIC_PROV3;
	}










	public void setR34_SPECIFIC_PROV3(BigDecimal r34_SPECIFIC_PROV3) {
		R34_SPECIFIC_PROV3 = r34_SPECIFIC_PROV3;
	}










	public BigDecimal getR34_NO_OF_ACC3() {
		return R34_NO_OF_ACC3;
	}










	public void setR34_NO_OF_ACC3(BigDecimal r34_NO_OF_ACC3) {
		R34_NO_OF_ACC3 = r34_NO_OF_ACC3;
	}










	public BigDecimal getR34_TOTAL_NON_ACCRUAL() {
		return R34_TOTAL_NON_ACCRUAL;
	}










	public void setR34_TOTAL_NON_ACCRUAL(BigDecimal r34_TOTAL_NON_ACCRUAL) {
		R34_TOTAL_NON_ACCRUAL = r34_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR34_TOTAL_DUE_LOANS() {
		return R34_TOTAL_DUE_LOANS;
	}










	public void setR34_TOTAL_DUE_LOANS(BigDecimal r34_TOTAL_DUE_LOANS) {
		R34_TOTAL_DUE_LOANS = r34_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR34_TOTAL_PERFORMING_LOAN() {
		return R34_TOTAL_PERFORMING_LOAN;
	}










	public void setR34_TOTAL_PERFORMING_LOAN(BigDecimal r34_TOTAL_PERFORMING_LOAN) {
		R34_TOTAL_PERFORMING_LOAN = r34_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR34_VALUE_OF_COLLATERAL() {
		return R34_VALUE_OF_COLLATERAL;
	}










	public void setR34_VALUE_OF_COLLATERAL(BigDecimal r34_VALUE_OF_COLLATERAL) {
		R34_VALUE_OF_COLLATERAL = r34_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR34_TOTAL_VALUE_NPL() {
		return R34_TOTAL_VALUE_NPL;
	}










	public void setR34_TOTAL_VALUE_NPL(BigDecimal r34_TOTAL_VALUE_NPL) {
		R34_TOTAL_VALUE_NPL = r34_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR34_TOTAL_SPECIFIC_PROV() {
		return R34_TOTAL_SPECIFIC_PROV;
	}










	public void setR34_TOTAL_SPECIFIC_PROV(BigDecimal r34_TOTAL_SPECIFIC_PROV) {
		R34_TOTAL_SPECIFIC_PROV = r34_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR34_SPECIFIC_PROV_NPL() {
		return R34_SPECIFIC_PROV_NPL;
	}










	public void setR34_SPECIFIC_PROV_NPL(BigDecimal r34_SPECIFIC_PROV_NPL) {
		R34_SPECIFIC_PROV_NPL = r34_SPECIFIC_PROV_NPL;
	}










	public String getR35_PRODUCT() {
		return R35_PRODUCT;
	}










	public void setR35_PRODUCT(String r35_PRODUCT) {
		R35_PRODUCT = r35_PRODUCT;
	}










	public BigDecimal getR35_30D_90D_PASTDUE() {
		return R35_30D_90D_PASTDUE;
	}










	public void setR35_30D_90D_PASTDUE(BigDecimal r35_30d_90d_PASTDUE) {
		R35_30D_90D_PASTDUE = r35_30d_90d_PASTDUE;
	}










	public BigDecimal getR35_NON_PERFORM_LOANS() {
		return R35_NON_PERFORM_LOANS;
	}










	public void setR35_NON_PERFORM_LOANS(BigDecimal r35_NON_PERFORM_LOANS) {
		R35_NON_PERFORM_LOANS = r35_NON_PERFORM_LOANS;
	}










	public BigDecimal getR35_NON_ACCRUALS1() {
		return R35_NON_ACCRUALS1;
	}










	public void setR35_NON_ACCRUALS1(BigDecimal r35_NON_ACCRUALS1) {
		R35_NON_ACCRUALS1 = r35_NON_ACCRUALS1;
	}










	public BigDecimal getR35_SPECIFIC_PROV1() {
		return R35_SPECIFIC_PROV1;
	}










	public void setR35_SPECIFIC_PROV1(BigDecimal r35_SPECIFIC_PROV1) {
		R35_SPECIFIC_PROV1 = r35_SPECIFIC_PROV1;
	}










	public BigDecimal getR35_NO_OF_ACC1() {
		return R35_NO_OF_ACC1;
	}










	public void setR35_NO_OF_ACC1(BigDecimal r35_NO_OF_ACC1) {
		R35_NO_OF_ACC1 = r35_NO_OF_ACC1;
	}










	public BigDecimal getR35_90D_180D_PASTDUE() {
		return R35_90D_180D_PASTDUE;
	}










	public void setR35_90D_180D_PASTDUE(BigDecimal r35_90d_180d_PASTDUE) {
		R35_90D_180D_PASTDUE = r35_90d_180d_PASTDUE;
	}










	public BigDecimal getR35_NON_ACCRUALS2() {
		return R35_NON_ACCRUALS2;
	}










	public void setR35_NON_ACCRUALS2(BigDecimal r35_NON_ACCRUALS2) {
		R35_NON_ACCRUALS2 = r35_NON_ACCRUALS2;
	}










	public BigDecimal getR35_SPECIFIC_PROV2() {
		return R35_SPECIFIC_PROV2;
	}










	public void setR35_SPECIFIC_PROV2(BigDecimal r35_SPECIFIC_PROV2) {
		R35_SPECIFIC_PROV2 = r35_SPECIFIC_PROV2;
	}










	public BigDecimal getR35_NO_OF_ACC2() {
		return R35_NO_OF_ACC2;
	}










	public void setR35_NO_OF_ACC2(BigDecimal r35_NO_OF_ACC2) {
		R35_NO_OF_ACC2 = r35_NO_OF_ACC2;
	}










	public BigDecimal getR35_180D_ABOVE_PASTDUE() {
		return R35_180D_ABOVE_PASTDUE;
	}










	public void setR35_180D_ABOVE_PASTDUE(BigDecimal r35_180d_ABOVE_PASTDUE) {
		R35_180D_ABOVE_PASTDUE = r35_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR35_NON_ACCRUALS3() {
		return R35_NON_ACCRUALS3;
	}










	public void setR35_NON_ACCRUALS3(BigDecimal r35_NON_ACCRUALS3) {
		R35_NON_ACCRUALS3 = r35_NON_ACCRUALS3;
	}










	public BigDecimal getR35_SPECIFIC_PROV3() {
		return R35_SPECIFIC_PROV3;
	}










	public void setR35_SPECIFIC_PROV3(BigDecimal r35_SPECIFIC_PROV3) {
		R35_SPECIFIC_PROV3 = r35_SPECIFIC_PROV3;
	}










	public BigDecimal getR35_NO_OF_ACC3() {
		return R35_NO_OF_ACC3;
	}










	public void setR35_NO_OF_ACC3(BigDecimal r35_NO_OF_ACC3) {
		R35_NO_OF_ACC3 = r35_NO_OF_ACC3;
	}










	public BigDecimal getR35_TOTAL_NON_ACCRUAL() {
		return R35_TOTAL_NON_ACCRUAL;
	}










	public void setR35_TOTAL_NON_ACCRUAL(BigDecimal r35_TOTAL_NON_ACCRUAL) {
		R35_TOTAL_NON_ACCRUAL = r35_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR35_TOTAL_DUE_LOANS() {
		return R35_TOTAL_DUE_LOANS;
	}










	public void setR35_TOTAL_DUE_LOANS(BigDecimal r35_TOTAL_DUE_LOANS) {
		R35_TOTAL_DUE_LOANS = r35_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR35_TOTAL_PERFORMING_LOAN() {
		return R35_TOTAL_PERFORMING_LOAN;
	}










	public void setR35_TOTAL_PERFORMING_LOAN(BigDecimal r35_TOTAL_PERFORMING_LOAN) {
		R35_TOTAL_PERFORMING_LOAN = r35_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR35_VALUE_OF_COLLATERAL() {
		return R35_VALUE_OF_COLLATERAL;
	}










	public void setR35_VALUE_OF_COLLATERAL(BigDecimal r35_VALUE_OF_COLLATERAL) {
		R35_VALUE_OF_COLLATERAL = r35_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR35_TOTAL_VALUE_NPL() {
		return R35_TOTAL_VALUE_NPL;
	}










	public void setR35_TOTAL_VALUE_NPL(BigDecimal r35_TOTAL_VALUE_NPL) {
		R35_TOTAL_VALUE_NPL = r35_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR35_TOTAL_SPECIFIC_PROV() {
		return R35_TOTAL_SPECIFIC_PROV;
	}










	public void setR35_TOTAL_SPECIFIC_PROV(BigDecimal r35_TOTAL_SPECIFIC_PROV) {
		R35_TOTAL_SPECIFIC_PROV = r35_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR35_SPECIFIC_PROV_NPL() {
		return R35_SPECIFIC_PROV_NPL;
	}










	public void setR35_SPECIFIC_PROV_NPL(BigDecimal r35_SPECIFIC_PROV_NPL) {
		R35_SPECIFIC_PROV_NPL = r35_SPECIFIC_PROV_NPL;
	}










	public String getR36_PRODUCT() {
		return R36_PRODUCT;
	}










	public void setR36_PRODUCT(String r36_PRODUCT) {
		R36_PRODUCT = r36_PRODUCT;
	}










	public BigDecimal getR36_30D_90D_PASTDUE() {
		return R36_30D_90D_PASTDUE;
	}










	public void setR36_30D_90D_PASTDUE(BigDecimal r36_30d_90d_PASTDUE) {
		R36_30D_90D_PASTDUE = r36_30d_90d_PASTDUE;
	}










	public BigDecimal getR36_NON_PERFORM_LOANS() {
		return R36_NON_PERFORM_LOANS;
	}










	public void setR36_NON_PERFORM_LOANS(BigDecimal r36_NON_PERFORM_LOANS) {
		R36_NON_PERFORM_LOANS = r36_NON_PERFORM_LOANS;
	}










	public BigDecimal getR36_NON_ACCRUALS1() {
		return R36_NON_ACCRUALS1;
	}










	public void setR36_NON_ACCRUALS1(BigDecimal r36_NON_ACCRUALS1) {
		R36_NON_ACCRUALS1 = r36_NON_ACCRUALS1;
	}










	public BigDecimal getR36_SPECIFIC_PROV1() {
		return R36_SPECIFIC_PROV1;
	}










	public void setR36_SPECIFIC_PROV1(BigDecimal r36_SPECIFIC_PROV1) {
		R36_SPECIFIC_PROV1 = r36_SPECIFIC_PROV1;
	}










	public BigDecimal getR36_NO_OF_ACC1() {
		return R36_NO_OF_ACC1;
	}










	public void setR36_NO_OF_ACC1(BigDecimal r36_NO_OF_ACC1) {
		R36_NO_OF_ACC1 = r36_NO_OF_ACC1;
	}










	public BigDecimal getR36_90D_180D_PASTDUE() {
		return R36_90D_180D_PASTDUE;
	}










	public void setR36_90D_180D_PASTDUE(BigDecimal r36_90d_180d_PASTDUE) {
		R36_90D_180D_PASTDUE = r36_90d_180d_PASTDUE;
	}










	public BigDecimal getR36_NON_ACCRUALS2() {
		return R36_NON_ACCRUALS2;
	}










	public void setR36_NON_ACCRUALS2(BigDecimal r36_NON_ACCRUALS2) {
		R36_NON_ACCRUALS2 = r36_NON_ACCRUALS2;
	}










	public BigDecimal getR36_SPECIFIC_PROV2() {
		return R36_SPECIFIC_PROV2;
	}










	public void setR36_SPECIFIC_PROV2(BigDecimal r36_SPECIFIC_PROV2) {
		R36_SPECIFIC_PROV2 = r36_SPECIFIC_PROV2;
	}










	public BigDecimal getR36_NO_OF_ACC2() {
		return R36_NO_OF_ACC2;
	}










	public void setR36_NO_OF_ACC2(BigDecimal r36_NO_OF_ACC2) {
		R36_NO_OF_ACC2 = r36_NO_OF_ACC2;
	}










	public BigDecimal getR36_180D_ABOVE_PASTDUE() {
		return R36_180D_ABOVE_PASTDUE;
	}










	public void setR36_180D_ABOVE_PASTDUE(BigDecimal r36_180d_ABOVE_PASTDUE) {
		R36_180D_ABOVE_PASTDUE = r36_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR36_NON_ACCRUALS3() {
		return R36_NON_ACCRUALS3;
	}










	public void setR36_NON_ACCRUALS3(BigDecimal r36_NON_ACCRUALS3) {
		R36_NON_ACCRUALS3 = r36_NON_ACCRUALS3;
	}










	public BigDecimal getR36_SPECIFIC_PROV3() {
		return R36_SPECIFIC_PROV3;
	}










	public void setR36_SPECIFIC_PROV3(BigDecimal r36_SPECIFIC_PROV3) {
		R36_SPECIFIC_PROV3 = r36_SPECIFIC_PROV3;
	}










	public BigDecimal getR36_NO_OF_ACC3() {
		return R36_NO_OF_ACC3;
	}










	public void setR36_NO_OF_ACC3(BigDecimal r36_NO_OF_ACC3) {
		R36_NO_OF_ACC3 = r36_NO_OF_ACC3;
	}










	public BigDecimal getR36_TOTAL_NON_ACCRUAL() {
		return R36_TOTAL_NON_ACCRUAL;
	}










	public void setR36_TOTAL_NON_ACCRUAL(BigDecimal r36_TOTAL_NON_ACCRUAL) {
		R36_TOTAL_NON_ACCRUAL = r36_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR36_TOTAL_DUE_LOANS() {
		return R36_TOTAL_DUE_LOANS;
	}










	public void setR36_TOTAL_DUE_LOANS(BigDecimal r36_TOTAL_DUE_LOANS) {
		R36_TOTAL_DUE_LOANS = r36_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR36_TOTAL_PERFORMING_LOAN() {
		return R36_TOTAL_PERFORMING_LOAN;
	}










	public void setR36_TOTAL_PERFORMING_LOAN(BigDecimal r36_TOTAL_PERFORMING_LOAN) {
		R36_TOTAL_PERFORMING_LOAN = r36_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR36_VALUE_OF_COLLATERAL() {
		return R36_VALUE_OF_COLLATERAL;
	}










	public void setR36_VALUE_OF_COLLATERAL(BigDecimal r36_VALUE_OF_COLLATERAL) {
		R36_VALUE_OF_COLLATERAL = r36_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR36_TOTAL_VALUE_NPL() {
		return R36_TOTAL_VALUE_NPL;
	}










	public void setR36_TOTAL_VALUE_NPL(BigDecimal r36_TOTAL_VALUE_NPL) {
		R36_TOTAL_VALUE_NPL = r36_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR36_TOTAL_SPECIFIC_PROV() {
		return R36_TOTAL_SPECIFIC_PROV;
	}










	public void setR36_TOTAL_SPECIFIC_PROV(BigDecimal r36_TOTAL_SPECIFIC_PROV) {
		R36_TOTAL_SPECIFIC_PROV = r36_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR36_SPECIFIC_PROV_NPL() {
		return R36_SPECIFIC_PROV_NPL;
	}










	public void setR36_SPECIFIC_PROV_NPL(BigDecimal r36_SPECIFIC_PROV_NPL) {
		R36_SPECIFIC_PROV_NPL = r36_SPECIFIC_PROV_NPL;
	}










	public String getR37_PRODUCT() {
		return R37_PRODUCT;
	}










	public void setR37_PRODUCT(String r37_PRODUCT) {
		R37_PRODUCT = r37_PRODUCT;
	}










	public BigDecimal getR37_30D_90D_PASTDUE() {
		return R37_30D_90D_PASTDUE;
	}










	public void setR37_30D_90D_PASTDUE(BigDecimal r37_30d_90d_PASTDUE) {
		R37_30D_90D_PASTDUE = r37_30d_90d_PASTDUE;
	}










	public BigDecimal getR37_NON_PERFORM_LOANS() {
		return R37_NON_PERFORM_LOANS;
	}










	public void setR37_NON_PERFORM_LOANS(BigDecimal r37_NON_PERFORM_LOANS) {
		R37_NON_PERFORM_LOANS = r37_NON_PERFORM_LOANS;
	}










	public BigDecimal getR37_NON_ACCRUALS1() {
		return R37_NON_ACCRUALS1;
	}










	public void setR37_NON_ACCRUALS1(BigDecimal r37_NON_ACCRUALS1) {
		R37_NON_ACCRUALS1 = r37_NON_ACCRUALS1;
	}










	public BigDecimal getR37_SPECIFIC_PROV1() {
		return R37_SPECIFIC_PROV1;
	}










	public void setR37_SPECIFIC_PROV1(BigDecimal r37_SPECIFIC_PROV1) {
		R37_SPECIFIC_PROV1 = r37_SPECIFIC_PROV1;
	}










	public BigDecimal getR37_NO_OF_ACC1() {
		return R37_NO_OF_ACC1;
	}










	public void setR37_NO_OF_ACC1(BigDecimal r37_NO_OF_ACC1) {
		R37_NO_OF_ACC1 = r37_NO_OF_ACC1;
	}










	public BigDecimal getR37_90D_180D_PASTDUE() {
		return R37_90D_180D_PASTDUE;
	}










	public void setR37_90D_180D_PASTDUE(BigDecimal r37_90d_180d_PASTDUE) {
		R37_90D_180D_PASTDUE = r37_90d_180d_PASTDUE;
	}










	public BigDecimal getR37_NON_ACCRUALS2() {
		return R37_NON_ACCRUALS2;
	}










	public void setR37_NON_ACCRUALS2(BigDecimal r37_NON_ACCRUALS2) {
		R37_NON_ACCRUALS2 = r37_NON_ACCRUALS2;
	}










	public BigDecimal getR37_SPECIFIC_PROV2() {
		return R37_SPECIFIC_PROV2;
	}










	public void setR37_SPECIFIC_PROV2(BigDecimal r37_SPECIFIC_PROV2) {
		R37_SPECIFIC_PROV2 = r37_SPECIFIC_PROV2;
	}










	public BigDecimal getR37_NO_OF_ACC2() {
		return R37_NO_OF_ACC2;
	}










	public void setR37_NO_OF_ACC2(BigDecimal r37_NO_OF_ACC2) {
		R37_NO_OF_ACC2 = r37_NO_OF_ACC2;
	}










	public BigDecimal getR37_180D_ABOVE_PASTDUE() {
		return R37_180D_ABOVE_PASTDUE;
	}










	public void setR37_180D_ABOVE_PASTDUE(BigDecimal r37_180d_ABOVE_PASTDUE) {
		R37_180D_ABOVE_PASTDUE = r37_180d_ABOVE_PASTDUE;
	}










	public BigDecimal getR37_NON_ACCRUALS3() {
		return R37_NON_ACCRUALS3;
	}










	public void setR37_NON_ACCRUALS3(BigDecimal r37_NON_ACCRUALS3) {
		R37_NON_ACCRUALS3 = r37_NON_ACCRUALS3;
	}










	public BigDecimal getR37_SPECIFIC_PROV3() {
		return R37_SPECIFIC_PROV3;
	}










	public void setR37_SPECIFIC_PROV3(BigDecimal r37_SPECIFIC_PROV3) {
		R37_SPECIFIC_PROV3 = r37_SPECIFIC_PROV3;
	}










	public BigDecimal getR37_NO_OF_ACC3() {
		return R37_NO_OF_ACC3;
	}










	public void setR37_NO_OF_ACC3(BigDecimal r37_NO_OF_ACC3) {
		R37_NO_OF_ACC3 = r37_NO_OF_ACC3;
	}










	public BigDecimal getR37_TOTAL_NON_ACCRUAL() {
		return R37_TOTAL_NON_ACCRUAL;
	}










	public void setR37_TOTAL_NON_ACCRUAL(BigDecimal r37_TOTAL_NON_ACCRUAL) {
		R37_TOTAL_NON_ACCRUAL = r37_TOTAL_NON_ACCRUAL;
	}










	public BigDecimal getR37_TOTAL_DUE_LOANS() {
		return R37_TOTAL_DUE_LOANS;
	}










	public void setR37_TOTAL_DUE_LOANS(BigDecimal r37_TOTAL_DUE_LOANS) {
		R37_TOTAL_DUE_LOANS = r37_TOTAL_DUE_LOANS;
	}










	public BigDecimal getR37_TOTAL_PERFORMING_LOAN() {
		return R37_TOTAL_PERFORMING_LOAN;
	}










	public void setR37_TOTAL_PERFORMING_LOAN(BigDecimal r37_TOTAL_PERFORMING_LOAN) {
		R37_TOTAL_PERFORMING_LOAN = r37_TOTAL_PERFORMING_LOAN;
	}










	public BigDecimal getR37_VALUE_OF_COLLATERAL() {
		return R37_VALUE_OF_COLLATERAL;
	}










	public void setR37_VALUE_OF_COLLATERAL(BigDecimal r37_VALUE_OF_COLLATERAL) {
		R37_VALUE_OF_COLLATERAL = r37_VALUE_OF_COLLATERAL;
	}










	public BigDecimal getR37_TOTAL_VALUE_NPL() {
		return R37_TOTAL_VALUE_NPL;
	}










	public void setR37_TOTAL_VALUE_NPL(BigDecimal r37_TOTAL_VALUE_NPL) {
		R37_TOTAL_VALUE_NPL = r37_TOTAL_VALUE_NPL;
	}










	public BigDecimal getR37_TOTAL_SPECIFIC_PROV() {
		return R37_TOTAL_SPECIFIC_PROV;
	}










	public void setR37_TOTAL_SPECIFIC_PROV(BigDecimal r37_TOTAL_SPECIFIC_PROV) {
		R37_TOTAL_SPECIFIC_PROV = r37_TOTAL_SPECIFIC_PROV;
	}










	public BigDecimal getR37_SPECIFIC_PROV_NPL() {
		return R37_SPECIFIC_PROV_NPL;
	}










	public void setR37_SPECIFIC_PROV_NPL(BigDecimal r37_SPECIFIC_PROV_NPL) {
		R37_SPECIFIC_PROV_NPL = r37_SPECIFIC_PROV_NPL;
	}










	public Date getReport_date() {
		return report_date;
	}










	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}










	public String getREPORT_VERSION() {
		return REPORT_VERSION;
	}










	public void setREPORT_VERSION(String rEPORT_VERSION) {
		REPORT_VERSION = rEPORT_VERSION;
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










	public M_PD_Archival_Summary_Entity1() {
        super();
    }

}